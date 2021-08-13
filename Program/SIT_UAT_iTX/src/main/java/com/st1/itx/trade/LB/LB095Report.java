package com.st1.itx.trade.LB;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.db.service.springjpa.cm.LB095ServiceImpl;
import com.st1.itx.util.common.MakeExcel;
import com.st1.itx.util.common.MakeReport;
import com.st1.itx.util.common.MakeFile;

import java.util.Date;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;

@Component("lB095Report")
@Scope("prototype")

public class LB095Report extends MakeReport {
	private static final Logger logger = LoggerFactory.getLogger(LB095Report.class);

	Date dateNow = new Date();
	SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
	String strToday = String.valueOf(Integer.parseInt(sdf.format(dateNow)) - 19110000); // 民國年月日
	String strTodayMM = strToday.substring(3,5); // 月
	String strTodaydd= strToday.substring(5,7);  // 日
	int listCount = 0;

	@Autowired
	public LB095ServiceImpl lB095ServiceImpl;

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
		// LB095 不動產擔保品明細-建號附加檔
		try {
			this.info("-----strToday=" + strToday);
			this.info("-----strTodayMM=" + strTodayMM);
			this.info("-----strTodaydd=" + strTodaydd);
			
			List<HashMap<String, String>> LBList = lB095ServiceImpl.findAll(titaVo);
//			this.info("-----------------" + LBList);
			if (LBList == null) {
				listCount = 0;
			} else {
				listCount = LBList.size();
			}
			this.info("--------LBList.size()=" + listCount);

			// txt
			genFile(titaVo, LBList);
			// excel
			genExcel(titaVo, LBList);
		} catch (Exception e) {
			StringWriter errors = new StringWriter();
			e.printStackTrace(new PrintWriter(errors));
			this.info("LB095ServiceImpl.findAll error = " + errors.toString());
		}
	}

	private void genFile(TitaVo titaVo, List<HashMap<String, String>> LBList) throws LogicException {
		this.info("=========== LB095 genFile : ");
		String txt = "F0;F1;F2;F3;F4;F5;F6;F7;F8;F9;F10;F11;F12;F13;F14;F15;F16;F17;F18;F19;F20;F21;F22;F23;F24;F25";
		String txt1[] = txt.split(";");

		try {
			String strContent = "";
			DecimalFormat formatter = new DecimalFormat("0");
			
			String strFileName = "458"+ strTodayMM + strTodaydd + "1" + ".095";		// 458+月日+序號(1).095
			makeFile.open(titaVo, titaVo.getEntDyI(), titaVo.getKinbr(), "B095", "不動產擔保品明細－建號附加檔", strFileName, 2);

			// 首筆
			strContent = "JCIC-DAT-B095-V01-458" + StringUtils.repeat(" ",5) + strToday + "01" + StringUtils.repeat(" ",10) 
			   		   + makeFile.fillStringR("02-23895858#7067", 16, ' ') + makeFile.fillStringR("審查單位聯絡人－許高政", 80, ' ')
			   		   + StringUtils.repeat(" ",159);
			makeFile.put(strContent);

			// 欄位內容
			if (LBList.size() == 0) {	// 無資料時，會出空檔

			} else {
				for (HashMap<String, String> tLBVo : LBList) {
					strContent = "";
					for (int j = 1; j <= tLBVo.size(); j++) {
						String strField = "";
						if (tLBVo.get(txt1[j - 1]) == null) {
							strField = "";
						} else {
							strField = tLBVo.get(txt1[j - 1]).trim();
						}
						// 格式處理
						switch (j)	{
						    case 1 : 	strField = makeFile.fillStringL(strField,  2, '0');   break;
						    case 2 :	strField = makeFile.fillStringL(strField,  3, '0');   break;
						    case 3 :	strField = makeFile.fillStringL(strField,  4, '0');   break;
						    case 4 :	strField = makeFile.fillStringR(strField,  2, ' ');   break;
						    case 5 :	strField = makeFile.fillStringR(strField, 50, ' ');   break;
						    case 6 :	strField = makeFile.fillStringR(strField, 10, ' ');   break;
						    case 7 :	strField = makeFile.fillStringR(strField,  1, ' ');   break;
						    case 8 :	strField = makeFile.fillStringL(strField,  2, '0');   break;
						    case 9 :	strField = makeFile.fillStringR(strField,  4, ' ');   break;
						    case 10:	strField = makeFile.fillStringL(strField,  5, '0');   break;
						    case 11:	strField = makeFile.fillStringL(strField,  3, '0');   break;
						    case 12:	strField = makeFile.fillStringR(strField, 12, ' ');   break;
						    case 13:	strField = makeFile.fillStringR(strField, 12, ' ');   break;
						    case 14:	strField = makeFile.fillStringR(strField, 76, ' ');   break;
						    case 15:	strField = makeFile.fillStringR(strField,  1, ' ');   break;
						    case 16:	strField = makeFile.fillStringR(strField,  1, ' ');   break;
						    case 17:	strField = makeFile.fillStringR(strField,  6, ' ');   break;
						    case 18:	strField = makeFile.fillStringL(strField,  3, '0');   break;
						    case 19:	strField = makeFile.fillStringR(strField,  7, ' ');   break;
						    case 20:	strField = makeFile.fillStringL(strField,  7, '0');   break;
							case 21:	formatter.applyPattern("0000000.00");
										strField = formatter.format(Float.parseFloat(strField));	break;
							case 22:	formatter.applyPattern("0000000.00");
										strField = formatter.format(Float.parseFloat(strField));	break;
							case 23:	formatter.applyPattern("0000000.00");
										strField = formatter.format(Float.parseFloat(strField));	break;
							case 24:	formatter.applyPattern("0000000.00");
										strField = formatter.format(Float.parseFloat(strField));	break;
						    case 25:	strField = makeFile.fillStringR(strField, 44, ' ');   break;
						    case 26: 	strField = makeFile.fillStringL(strField,  5, '0');   break;
							default:  	strField = "";                                        break;
						}
						strContent = strContent + strField;
					}
					makeFile.put(strContent);
				}
			}

			// 末筆
			strContent = "TRLR" + makeFile.fillStringL(String.valueOf(listCount), 8, '0') + StringUtils.repeat(" ",288);
			makeFile.put(strContent);

			long sno = makeFile.close();
			// makeFile.toFile(sno);	// 不直接下傳

		} catch (Exception e) {
			StringWriter errors = new StringWriter();
			e.printStackTrace(new PrintWriter(errors));
			this.info("LB095ServiceImpl.genFile error = " + errors.toString());
		}
	}

	private void genExcel(TitaVo titaVo, List<HashMap<String, String>> LBList) throws LogicException {
		this.info("=========== LB095 genExcel: ");
		this.info("LB095 genExcel TitaVo=" + titaVo);

		// 自訂標題 inf (首筆/尾筆)
		String inf = "";
		String infLast = "";
		String txt = "";

		// B095 不動產擔保品明細-建號附加檔
		inf = "資料別(1~2);總行代號(3~5);分行代號(6~9);空白(10~11);擔保品控制編碼(12~61);擔保品所有權人或代表人IDN/BAN(62~71);縣市別(72~72);鄉鎮市區別(73~74);段、小段號(75~78);建號-前五碼(79~83);建號-後三碼(84~86);縣市名稱(87~98);鄉鎮市區名稱(99~110);村里/街路/段/巷/弄/號/樓(111~186);主要用途(187~187);主要建材(結構體)(188~188);附屬建物用途(189~194);層數(標的所在樓高)(195~197);層次(標的所在樓層)(198~204);建築完成日期(屋齡)(205~211);建物總面積(212~221);主建物(層次)面積(222~231);附屬建物面積(232~241);共同部份持分面積(242~251);空白(252~295);資料所屬年月(296~300)";
		txt = "F0;F1;F2;F3;F4;F5;F6;F7;F8;F9;F10;F11;F12;F13;F14;F15;F16;F17;F18;F19;F20;F21;F22;F23;F24;F25";

		// 自訂標題列寫入記號
		boolean infFg = true;
		if ((inf.trim()).equals("")) {
			infFg = false;
		}

		String inf1[] = inf.split(";");
		String txt1[] = txt.split(";");

		try {
			String strContent = "";
			// 若有底稿，需自行判斷列印起始行數 i
			// 若無底稿，設定列印起始行數 i=1
			int i = 1;
			String strFileName = "458"+ strTodayMM + strTodaydd + "1" + ".095";		// 458+月日+序號(1).095
			this.info("------------titaVo.getEntDyI()=" + titaVo.getEntDyI());
			this.info("------------titaVo.getKinbr()=" + titaVo.getKinbr());
			makeExcel.open(titaVo, titaVo.getEntDyI(), titaVo.getKinbr(), "B095", "不動產擔保品明細－建號附加檔", strFileName);

			// 不設定CellStyle
			makeExcel.setNeedStyle(false);

			// 設定欄位寬度
			setAllWidth();

			// 自訂標題列
			if (infFg == true) {
				for (int j = 1; j <= inf1.length; j++) {
					makeExcel.setValue(i, j, inf1[j - 1]);
				}
				infFg = false;
				i++;
			}

			// 欄位內容
			if (LBList.size() == 0) {	// 無資料時，會出空檔

			} else {
				for (HashMap<String, String> tLBVo : LBList) {
					for (int j = 1; j <= tLBVo.size(); j++) {
						if (tLBVo.get(txt1[j - 1]) == null) {
							makeExcel.setValue(i, j, "");
						} else {
							makeExcel.setValue(i, j, tLBVo.get(txt1[j - 1]));
						}
					}
					i++;
				}
			}

			long sno = makeExcel.close();
			// makeExcel.toExcel(sno);	// 不直接下傳

		} catch (Exception e) {
			StringWriter errors = new StringWriter();
			e.printStackTrace(new PrintWriter(errors));
			this.info("LB095ServiceImpl.genExcel error = " + errors.toString());
		}
	}

	private void setAllWidth() throws LogicException {
		this.info("=========== setAllWidth()");
		makeExcel.setWidth(1, 6);
		makeExcel.setWidth(2, 7);
		makeExcel.setWidth(3, 8);
		makeExcel.setWidth(4, 6);
		makeExcel.setWidth(5, 54);
		makeExcel.setWidth(6, 14);
		makeExcel.setWidth(7, 5);
		makeExcel.setWidth(8, 6);
		makeExcel.setWidth(9, 8);
		makeExcel.setWidth(10, 9);
		makeExcel.setWidth(11, 7);
		makeExcel.setWidth(12, 16);
		makeExcel.setWidth(13, 16);
		makeExcel.setWidth(14, 80);
		makeExcel.setWidth(15, 5);
		makeExcel.setWidth(16, 5);
		makeExcel.setWidth(17, 10);
		makeExcel.setWidth(18, 7);
		makeExcel.setWidth(19, 11);
		makeExcel.setWidth(20, 11);
		makeExcel.setWidth(21, 14);
		makeExcel.setWidth(22, 14);
		makeExcel.setWidth(23, 14);
		makeExcel.setWidth(24, 14);
		makeExcel.setWidth(25, 48);
		makeExcel.setWidth(26, 9);
	}
	
}

