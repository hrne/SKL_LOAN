package com.st1.itx.trade.LB;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.db.service.springjpa.cm.LB096ServiceImpl;
import com.st1.itx.util.common.MakeExcel;
import com.st1.itx.util.common.MakeReport;
import com.st1.itx.util.common.data.L8ConstantEum;
import com.st1.itx.util.common.MakeFile;

import java.util.Date;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;

@Component("lB096Report")
@Scope("prototype")

public class LB096Report extends MakeReport {

	Date dateNow = new Date();
	SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
	String strToday = String.valueOf(Integer.parseInt(sdf.format(dateNow)) - 19110000); // 民國年月日
	String strTodayMM = strToday.substring(3, 5); // 月
	String strTodaydd = strToday.substring(5, 7); // 日
	int listCount = 0;

	@Autowired
	public LB096ServiceImpl lB096ServiceImpl;

	@Autowired
	public MakeExcel makeExcel;

	@Autowired
	public MakeFile makeFile;

	// 自訂明細標題
	@Override
	public void printTitle() {
		this.info("printTitle nowRow = " + this.NowRow);
	}

	public void exec(TitaVo titaVo) throws LogicException {
		// LB096 不動產擔保品明細-地號附加檔
		try {
			this.info("-----strToday=" + strToday);
			this.info("-----strTodayMM=" + strTodayMM);
			this.info("-----strTodaydd=" + strTodaydd);

			List<Map<String, String>> LBList = lB096ServiceImpl.findAll(titaVo);
			if (LBList == null) {
				listCount = 0;
			} else {
				listCount = LBList.size();
			}
			this.info("--------LBList.size()=" + listCount);

			// txt
			genFile(titaVo, LBList);
			// excel-CSV
			genExcel(titaVo, LBList);
		} catch (Exception e) {
			StringWriter errors = new StringWriter();
			e.printStackTrace(new PrintWriter(errors));
			this.info("LB096ServiceImpl.findAll error = " + errors.toString());
		}
	}

	private void genFile(TitaVo titaVo, List<Map<String, String>> LBList) throws LogicException {
		this.info("=========== LB096 genFile : ");
		String txt = "F0;F1;F2;F3;F4;F5;F6;F7;F8;F9;F10;F11;F12;F13;F14;F15;F16;F17;F18";
		String txt1[] = txt.split(";");

		Pattern.compile("[0-9]*");
		try {
			String strContent = "";
			DecimalFormat formatter = new DecimalFormat("0");

			String strFileName = "458" + strTodayMM + strTodaydd + "1" + ".096"; // 458+月日+序號(1).096
			makeFile.open(titaVo, titaVo.getEntDyI(), titaVo.getKinbr(), "B096", "不動產擔保品明細－地號附加檔", strFileName, 2);

			// 首筆
			strContent = "JCIC-DAT-B096-V01-458" + StringUtils.repeat(" ", 5) + strToday + "01" + StringUtils.repeat(" ", 10) + makeFile.fillStringR(L8ConstantEum.phoneNum, 16, ' ')
					+ makeFile.fillStringR("審查單位聯絡人－" + L8ConstantEum.contact, 80, ' ') + StringUtils.repeat(" ", 9);
			makeFile.put(strContent);

			// 欄位內容
			if (LBList.size() == 0) { // 無資料時，會出空檔

			} else {
				for (Map<String, String> tLBVo : LBList) {
					strContent = "";
					for (int j = 1; j <= 19; j++) {
						String strField = "";
						if (tLBVo.get(txt1[j - 1]) == null) {
							strField = "";
						} else {
							strField = tLBVo.get(txt1[j - 1]).trim();
						}
						// 格式處理
						switch (j) {
						case 1:
							strField = makeFile.fillStringL(strField, 2, '0');
							break;
						case 2:
							strField = makeFile.fillStringL(strField, 3, '0');
							break;
						case 3:
							strField = makeFile.fillStringL(strField, 4, '0');
							break;
						case 4:
							strField = makeFile.fillStringR(strField, 2, ' ');
							break;
						case 5:
							strField = makeFile.fillStringR(strField, 50, ' ');
							break;
						case 6:
							strField = makeFile.fillStringR(strField, 10, ' ');
							break;
						case 7:
							strField = makeFile.fillStringR(strField, 1, ' ');
							break;
						case 8:
							strField = makeFile.fillStringL(strField, 2, '0');
							break;
						case 9:
							strField = makeFile.fillStringR(strField, 4, ' ');
							break;
						case 10:
							strField = makeFile.fillStringL(strField, 4, '0');
							break;
						case 11:
							strField = makeFile.fillStringL(strField, 4, '0');
							break;
						case 12:
							strField = makeFile.fillStringR(strField, 1, ' ');
							break;
//						    case 13: // 面積(88~97)
//										isNum = pattern.matcher(strField); 
//										if( isNum.matches() ) {  
//											strField = makeFile.fillStringL(strField, 10, '0');		
//										} else {
//											strField = makeFile.fillStringR(strField, 10, ' ');	
//										}
//										break;
//						    case 13: 	strField = makeFile.fillStringL(strField, 10, '0');   break;  // 面積(88~97)
						case 13:
							formatter.applyPattern("0000000.00");
							strField = formatter.format(Float.parseFloat(strField));
							break; // 面積(88~97)
						case 14:
							strField = makeFile.fillStringR(strField, 1, ' ');
							break;
						case 15:
							strField = makeFile.fillStringR(strField, 2, ' ');
							break;
						case 16:
							strField = makeFile.fillStringL(strField, 10, '0');
							break;
						case 17:
							strField = makeFile.fillStringL(strField, 5, '0');
							break;
						case 18:
							strField = makeFile.fillStringR(strField, 30, ' ');
							break;
						case 19:
							strField = makeFile.fillStringL(strField, 5, '0');
							break;
						default:
							strField = "";
							break;
						}
						strContent = strContent + strField;
					}
					makeFile.put(strContent);
				}
			}

			// 末筆
			strContent = "TRLR" + makeFile.fillStringL(String.valueOf(listCount), 8, '0') + StringUtils.repeat(" ", 138);
			makeFile.put(strContent);

			makeFile.close();

		} catch (Exception e) {
			StringWriter errors = new StringWriter();
			e.printStackTrace(new PrintWriter(errors));
			this.info("LB096ServiceImpl.genFile error = " + errors.toString());
		}
	}

	private void genExcel(TitaVo titaVo, List<Map<String, String>> LBList) throws LogicException {
		this.info("=========== LB096 genExcel: ");
		this.info("LB096 genExcel TitaVo=" + titaVo);

		// 自訂標題 inf (首筆/尾筆)
		String inf = "";
		String txt = "";

		// B096 不動產擔保品明細-地號附加檔
		inf = "資料別(1~2),總行代號(3~5),分行代號(6~9),空白(10~11),擔保品控制編碼(12~61),擔保品所有權人或代表人IDN/BAN(62~71),縣市別(72~72),"
				+ "鄉鎮市區別(73~74),段、小段號(75~78),地號-前四碼(79~82),地號-後四碼(83~86),地目(87),面積(88~97),使用分區(98),使用地類別(99~100)," + "公告土地現值(101~110),公告土地現值年月(111~115),空白(116~145),資料所屬年月(146~150)";
		txt = "F0;F1;F2;F3;F4;F5;F6;F7;F8;F9;F10;F11;F12;F13;F14;F15;F16;F17;F18";

		String txt1[] = txt.split(";");
		Pattern.compile("[0-9]*");

		try {
			String strContent = "";
			DecimalFormat formatter = new DecimalFormat("0");
			String strFileName = "458" + strTodayMM + strTodaydd + "1" + ".096.CSV"; // 458+月日+序號(1).096.CSV
			this.info("------------titaVo.getEntDyI()=" + titaVo.getEntDyI());
			this.info("------------titaVo.getKinbr()=" + titaVo.getKinbr());
			makeFile.open(titaVo, titaVo.getEntDyI(), titaVo.getKinbr(), "B096", "不動產擔保品明細－地號附加檔", strFileName, 2);

			// 標題列
			strContent = inf;

			makeFile.put(strContent);

			// 欄位內容
			if (LBList.size() == 0) { // 無資料時，會出空檔

			} else {
				for (Map<String, String> tLBVo : LBList) {
					strContent = "";
					for (int j = 1; j <= 19; j++) {
						String strField = "";
						if (tLBVo.get(txt1[j - 1]) == null) {
							strField = "";
						} else {
							strField = tLBVo.get(txt1[j - 1]).trim();
						}
						// 格式處理
						switch (j) {
						case 1:
							strField = makeFile.fillStringL(strField, 2, '0');
							break;
						case 2:
							strField = makeFile.fillStringL(strField, 3, '0');
							break;
						case 3:
							strField = makeFile.fillStringL(strField, 4, '0');
							break;
						case 4:
							strField = makeFile.fillStringR(strField, 2, ' ');
							break;
						case 5:
							strField = makeFile.fillStringR(strField, 50, ' ');
							break;
						case 6:
							strField = makeFile.fillStringR(strField, 10, ' ');
							break;
						case 7:
							strField = makeFile.fillStringR(strField, 1, ' ');
							break;
						case 8:
							strField = makeFile.fillStringL(strField, 2, '0');
							break;
						case 9:
							strField = makeFile.fillStringR(strField, 4, ' ');
							break;
						case 10:
							strField = makeFile.fillStringL(strField, 4, '0');
							break;
						case 11:
							strField = makeFile.fillStringL(strField, 4, '0');
							break;
						case 12:
							strField = makeFile.fillStringR(strField, 1, ' ');
							break;
						case 13:
							formatter.applyPattern("0000000.00");
							strField = formatter.format(Float.parseFloat(strField));
							break; // 面積(88~97)
						case 14:
							strField = makeFile.fillStringR(strField, 1, ' ');
							break;
						case 15:
							strField = makeFile.fillStringR(strField, 2, ' ');
							break;
						case 16:
							strField = makeFile.fillStringL(strField, 10, '0');
							break;
						case 17:
							strField = makeFile.fillStringL(strField, 5, '0');
							break;
						case 18:
							strField = makeFile.fillStringR(strField, 30, ' ');
							break;
						case 19:
							strField = makeFile.fillStringL(strField, 5, '0');
							break;
						default:
							strField = "";
							break;
						}
						strContent = strContent + strField;
						if (j != 19) {
							strContent = strContent + ",";
						}
					}
					makeFile.put(strContent);
				}
			}

			makeFile.close();

		} catch (Exception e) {
			StringWriter errors = new StringWriter();
			e.printStackTrace(new PrintWriter(errors));
			this.info("LB096ServiceImpl.genExcel error = " + errors.toString());
		}
	}

}
