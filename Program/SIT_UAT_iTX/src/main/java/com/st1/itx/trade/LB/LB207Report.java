package com.st1.itx.trade.LB;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.db.service.springjpa.cm.LB207ServiceImpl;
import com.st1.itx.util.common.MakeExcel;
import com.st1.itx.util.common.MakeReport;
import com.st1.itx.util.common.data.L8ConstantEum;
import com.st1.itx.util.common.MakeFile;

import java.util.Date;
import java.text.SimpleDateFormat;

@Component("lB207Report")
@Scope("prototype")

public class LB207Report extends MakeReport {

	Date dateNow = new Date();
	SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
	String strToday = String.valueOf(Integer.parseInt(sdf.format(dateNow)) - 19110000); // 民國年月日
	String strTodayMM = strToday.substring(3, 5); // 月
	String strTodaydd = strToday.substring(5, 7); // 日
	int listCount = 0;

	@Autowired
	public LB207ServiceImpl lB207ServiceImpl;

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
		// LB207 授信戶基本資料檔
		try {
			this.info("-----strToday=" + strToday);
			this.info("-----strTodayMM=" + strTodayMM);
			this.info("-----strTodaydd=" + strTodaydd);

			List<Map<String, String>> LBList = lB207ServiceImpl.findAll(titaVo);
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
			this.info("LB207ServiceImpl.findAll error = " + errors.toString());
		}
	}

	private void genFile(TitaVo titaVo, List<Map<String, String>> LBList) throws LogicException {
		this.info("=========== LB207 genFile : ");
		String txt = "F0;F1;F2;F3;F4;F5;F6;F7;F8;F9;F10;F11;F12;F13;F14;F15;F16;F17;F18;F19;F20;F21;F22;F23;F24;F25;F26;F27;F28;F29";
		String txt1[] = txt.split(";");

		Pattern pattern = Pattern.compile("[0-9]*");
		Matcher isNum = null;

		try {
			String strContent = "";

			String strFileName = "458" + strTodayMM + strTodaydd + "1" + ".207"; // 458+月日+序號(1).207
			makeFile.open(titaVo, titaVo.getEntDyI(), titaVo.getKinbr(), "B207", "授信戶基本資料檔", strFileName, 2);

			// 首筆
			strContent = "JCIC-DAT-B207-V01-458" + StringUtils.repeat(" ", 5) + strToday + "01"
					+ StringUtils.repeat(" ", 10) + makeFile.fillStringR(L8ConstantEum.phoneNum, 16, ' ')
					+ makeFile.fillStringR("審查單位聯絡人－" + L8ConstantEum.contact, 80, ' ') + StringUtils.repeat(" ", 459);
			makeFile.put(strContent);

			// 欄位內容
			if (LBList.size() == 0) { // 無資料時，會出空檔

			} else {
				for (Map<String, String> tLBVo : LBList) {
					strContent = "";
					for (int j = 1; j <= 30; j++) {
						String strField = "";
						if (tLBVo.get(txt1[j - 1]) == null) {
							strField = "";
						} else {
							strField = tLBVo.get(txt1[j - 1]).trim();
						}
						// 格式處理
						switch (j) {
						case 1:
							strField = makeFile.fillStringR(strField, 1, ' ');
							break;
						case 2:
							strField = makeFile.fillStringL(strField, 3, '0');
							break;
						case 3:
							strField = makeFile.fillStringR(strField, 4, ' ');
							break;
						case 4:
							strField = makeFile.fillStringL(strField, 7, '0');
							break;
						case 5:
							strField = makeFile.fillStringR(strField, 10, ' ');
							break;
						case 6:
							strField = makeFile.fillStringR(strField, 20, ' ');
							break;
						case 7:
							strField = makeFile.fillStringR(strField, 20, ' ');
							break;
						case 8:
							strField = makeFile.fillStringL(strField, 7, '0');
							break;
						case 9:
							strField = makeFile.fillStringR(strField, 66, ' ');
							break;
						case 10:
							strField = makeFile.fillStringR(strField, 5, ' ');
							break;
						case 11:
							strField = makeFile.fillStringR(strField, 66, ' ');
							break;
						case 12:
							strField = makeFile.fillStringR(strField, 16, ' ');
							break;
						case 13:
							strField = makeFile.fillStringR(strField, 16, ' ');
							break;
						case 14:
							strField = makeFile.fillStringR(strField, 5, ' ');
							break;
						case 15:
							strField = makeFile.fillStringR(strField, 1, ' ');
							break;
						case 16:
							strField = makeFile.fillStringR(strField, 1, ' ');
							break;
						case 17:
							strField = makeFile.fillStringR(strField, 30, ' ');
							break;
						case 18:
							strField = makeFile.fillStringR(strField, 8, ' ');
							break;
						case 19:
							strField = makeFile.fillStringL(strField, 6, '0');
							break;
						case 20:
							strField = makeFile.fillStringR(strField, 16, ' ');
							break;
						case 21:
							strField = makeFile.fillStringR(strField, 10, ' ');
							break;
						case 22: // 服務年資(429~430);
							isNum = pattern.matcher(strField);
							if (isNum.matches()) {
								strField = makeFile.fillStringL(strField, 2, '0');
							} else {
								strField = makeFile.fillStringR(strField, 2, ' ');
							}
							break;
						case 23:
							strField = makeFile.fillStringL(strField, 6, '0');
							break;
						case 24:
							strField = makeFile.fillStringL(strField, 5, '0');
							break;
						case 25:
							strField = makeFile.fillStringR(strField, 1, ' ');
							break;
						case 26:
							strField = makeFile.fillStringR(strField, 2, ' ');
							break;
						case 27:
							strField = makeFile.fillStringR(strField, 20, ' ');
							break;
						case 28:
							strField = makeFile.fillStringR(strField, 10, ' ');
							break;
						case 29:
							strField = makeFile.fillStringR(strField, 200, ' ');
							break;
						case 30:
							strField = makeFile.fillStringR(strField, 36, ' ');
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
			strContent = "TRLR" + makeFile.fillStringL(String.valueOf(listCount), 8, '0') + StringUtils.repeat(" ", 588);
			makeFile.put(strContent);

			makeFile.close();

		} catch (Exception e) {
			StringWriter errors = new StringWriter();
			e.printStackTrace(new PrintWriter(errors));
			this.info("LB207ServiceImpl.genFile error = " + errors.toString());
		}
	}

	private void genExcel(TitaVo titaVo, List<Map<String, String>> LBList) throws LogicException {
		this.info("=========== LB207 genExcel: ");
		this.info("LB207 genExcel TitaVo=" + titaVo);

		// 自訂標題 inf (首筆/尾筆)
		String inf = "";
		String txt = "";

		// B207 授信戶基本資料檔
		inf = "交易代碼(1),總行代號(2~4),空白(5~8),資料日期(9~15),授信戶IDN(16~25),中文姓名(26~45),英文姓名(46~65),出生日期(66~72),"
				+ "戶籍地址(73~138),聯絡地址郵遞區號(139~143),聯絡地址(144~209),聯絡電話(210~225),行動電話(226~241),空白(242~246),"
				+ "教育程度代號(247),自有住宅有無(248),任職機構名稱(249~278),任職機構統一編號(279~286),職業類別(287~292),任職機構電話(293~308),"
				+ "職位名稱(309~318),服務年資(319~320),年收入(321~326),年收入資料年月(327~331),性別(332),國籍(333~334),護照號碼(335~354),"
				+ "舊有稅籍編號(355~364),中文姓名超逾10個字之全名(365~564),空白(565~600)";
		txt = "F0;F1;F2;F3;F4;F5;F6;F7;F8;F9;F10;F11;F12;F13;F14;F15;F16;F17;F18;F19;F20;F21;F22;F23;F24;F25;F26;F27;F28;F29";

		String txt1[] = txt.split(";");
		Pattern pattern = Pattern.compile("[0-9]*");
		Matcher isNum = null;

		try {
			String strContent = "";
			String strFileName = "458" + strTodayMM + strTodaydd + "1" + ".207.CSV"; // 458+月日+序號(1)+.207.CSV
			this.info("------------titaVo.getEntDyI()=" + titaVo.getEntDyI());
			this.info("------------titaVo.getKinbr()=" + titaVo.getKinbr());
			makeFile.open(titaVo, titaVo.getEntDyI(), titaVo.getKinbr(), "B207", "授信戶基本資料檔", strFileName, 2);

			// 標題列
			strContent = inf;

			makeFile.put(strContent);

			// 欄位內容
			if (LBList.size() == 0) { // 無資料時，會出空檔

			} else {
				for (Map<String, String> tLBVo : LBList) {
					strContent = "";
					for (int j = 1; j <= 30; j++) {
						String strField = "";
						if (tLBVo.get(txt1[j - 1]) == null) {
							strField = "";
						} else {
							strField = tLBVo.get(txt1[j - 1]).trim();
						}
						// 格式處理
						switch (j) {
						case 1:
							strField = makeFile.fillStringR(strField, 1, ' ');
							break;
						case 2:
							strField = makeFile.fillStringL(strField, 3, '0');
							break;
						case 3:
							strField = makeFile.fillStringR(strField, 4, ' ');
							break;
						case 4:
							strField = makeFile.fillStringL(strField, 7, '0');
							break;
						case 5:
							strField = makeFile.fillStringR(strField, 10, ' ');
							break;
						case 6:
							strField = makeFile.fillStringR(strField, 20, ' ');
							break;
						case 7:
							strField = makeFile.fillStringR(strField, 20, ' ');
							break;
						case 8:
							strField = makeFile.fillStringL(strField, 7, '0');
							break;
						case 9:
							strField = makeFile.fillStringR(strField, 66, ' ');
							break;
						case 10:
							strField = makeFile.fillStringR(strField, 5, ' ');
							break;
						case 11:
							strField = makeFile.fillStringR(strField, 66, ' ');
							break;
						case 12:
							strField = makeFile.fillStringR(strField, 16, ' ');
							break;
						case 13:
							strField = makeFile.fillStringR(strField, 16, ' ');
							break;
						case 14:
							strField = makeFile.fillStringR(strField, 5, ' ');
							break;
						case 15:
							strField = makeFile.fillStringR(strField, 1, ' ');
							break;
						case 16:
							strField = makeFile.fillStringR(strField, 1, ' ');
							break;
						case 17:
							strField = makeFile.fillStringR(strField, 30, ' ');
							break;
						case 18:
							strField = makeFile.fillStringR(strField, 8, ' ');
							break;
						case 19:
							strField = makeFile.fillStringL(strField, 6, '0');
							break;
						case 20:
							strField = makeFile.fillStringR(strField, 16, ' ');
							break;
						case 21:
							strField = makeFile.fillStringR(strField, 10, ' ');
							break;
						case 22: // 服務年資(429~430);
							isNum = pattern.matcher(strField);
							if (isNum.matches()) {
								strField = makeFile.fillStringL(strField, 2, '0');
							} else {
								strField = makeFile.fillStringR(strField, 2, ' ');
							}
							break;
						case 23:
							strField = makeFile.fillStringL(strField, 6, '0');
							break;
						case 24:
							strField = makeFile.fillStringL(strField, 5, '0');
							break;
						case 25:
							strField = makeFile.fillStringR(strField, 1, ' ');
							break;
						case 26:
							strField = makeFile.fillStringR(strField, 2, ' ');
							break;
						case 27:
							strField = makeFile.fillStringR(strField, 20, ' ');
							break;
						case 28:
							strField = makeFile.fillStringR(strField, 10, ' ');
							break;
						case 29:
							strField = makeFile.fillStringR(strField, 200, ' ');
							break;
						case 30:
							strField = makeFile.fillStringR(strField, 36, ' ');
							break;
						default:
							strField = "";
							break;
						}
						strContent = strContent + strField;
						if (j != 30) {
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
			this.info("LB207ServiceImpl.genExcel error = " + errors.toString());
		}
	}

}
