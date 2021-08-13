package com.st1.itx.trade.LB;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.List;
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
import com.st1.itx.util.common.MakeFile;

import java.util.Date;
import java.text.SimpleDateFormat;

@Component("lB207Report")
@Scope("prototype")

public class LB207Report extends MakeReport {
	// private static final Logger logger = LoggerFactory.getLogger(LB207Report.class);

	Date dateNow = new Date();
	SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
	String strToday = String.valueOf(Integer.parseInt(sdf.format(dateNow)) - 19110000); // 民國年月日
	String strTodayMM = strToday.substring(3,5); // 月
	String strTodaydd= strToday.substring(5,7);  // 日
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
			
			List<HashMap<String, String>> LBList = lB207ServiceImpl.findAll(titaVo);
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
			this.info("LB207ServiceImpl.findAll error = " + errors.toString());
		}
	}

	private void genFile(TitaVo titaVo, List<HashMap<String, String>> LBList) throws LogicException {
		this.info("=========== LB207 genFile : ");
		String txt = "F0;F1;F2;F3;F4;F5;F6;F7;F8;F9;F10;F11;F12;F13;F14;F15;F16;F17;F18;F19;F20;F21;F22;F23;F24;F25;F26;F27;F28;F29";
		String txt1[] = txt.split(";");

		Pattern pattern = Pattern.compile("[0-9]*");
		Matcher isNum = null; 

		try {
			String strContent = "";

			String strFileName = "458"+ strTodayMM + strTodaydd + "1" + ".207";		// 458+月日+序號(1).207
			makeFile.open(titaVo, titaVo.getEntDyI(), titaVo.getKinbr(), "B207", "授信戶基本資料檔", strFileName, 2);

			// 首筆
			strContent = "JCIC-DAT-B207-V01-458" + StringUtils.repeat(" ",5) + strToday + "01" + StringUtils.repeat(" ",10) 
					   + makeFile.fillStringR("02-23895858#7067", 16, ' ') + makeFile.fillStringR("審查單位聯絡人－許高政", 80, ' ') 
					   + StringUtils.repeat(" ", 459) ;
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
						    case 1 : strField = makeFile.fillStringR(strField,  1, ' ');   break;
						    case 2 : strField = makeFile.fillStringL(strField,  3, '0');   break;
						    case 3 : strField = makeFile.fillStringR(strField,  4, ' ');   break;
						    case 4 : strField = makeFile.fillStringL(strField,  7, '0');   break;
						    case 5 : strField = makeFile.fillStringR(strField, 10, ' ');   break;
						    case 6 : strField = makeFile.fillStringR(strField, 20, ' ');   break;
						    case 7 : strField = makeFile.fillStringR(strField, 20, ' ');   break;
						    case 8 : strField = makeFile.fillStringL(strField,  7, '0');   break;
						    case 9 : strField = makeFile.fillStringR(strField, 66, ' ');   break;
						    case 10: strField = makeFile.fillStringR(strField,  5, ' ');   break;
						    case 11: strField = makeFile.fillStringR(strField, 66, ' ');   break;
						    case 12: strField = makeFile.fillStringR(strField, 16, ' ');   break;
						    case 13: strField = makeFile.fillStringR(strField, 16, ' ');   break;
						    case 14: strField = makeFile.fillStringR(strField,  5, ' ');   break;
						    case 15: strField = makeFile.fillStringR(strField,  1, ' ');   break;
						    case 16: strField = makeFile.fillStringR(strField,  1, ' ');   break;
						    case 17: strField = makeFile.fillStringR(strField, 30, ' ');   break;
						    case 18: strField = makeFile.fillStringR(strField,  8, ' ');   break;
						    case 19: strField = makeFile.fillStringL(strField,  6, '0');   break;
						    case 20: strField = makeFile.fillStringR(strField, 16, ' ');   break;
						    case 21: strField = makeFile.fillStringR(strField, 10, ' ');   break;
						    case 22: // 服務年資(429~430);
									 isNum = pattern.matcher(strField); 
									 if( isNum.matches() ) { 
										 strField = makeFile.fillStringL(strField, 2, '0');		
									 } else {
										 strField = makeFile.fillStringR(strField, 2, ' ');	
									 }
									 break;
						    case 23: strField = makeFile.fillStringL(strField,  6, '0');   break;
						    case 24: strField = makeFile.fillStringL(strField,  5, '0');   break;
						    case 25: strField = makeFile.fillStringR(strField,  1, ' ');   break;
						    case 26: strField = makeFile.fillStringR(strField,  2, ' ');   break;
						    case 27: strField = makeFile.fillStringR(strField, 20, ' ');   break;
						    case 28: strField = makeFile.fillStringR(strField, 10, ' ');   break;
						    case 29: strField = makeFile.fillStringR(strField,200, ' ');   break;
						    case 30: strField = makeFile.fillStringR(strField, 36, ' ');   break;
							default: strField = "";										   break;
						}
						strContent = strContent + strField;
					}
					makeFile.put(strContent);
				}
			}

			// 末筆
			strContent = "TRLR"
					   + makeFile.fillStringL(String.valueOf(listCount), 8, '0') 
					   + StringUtils.repeat(" ",588);
			makeFile.put(strContent);

			long sno = makeFile.close();
			// makeFile.toFile(sno);	// 不直接下傳

		} catch (Exception e) {
			StringWriter errors = new StringWriter();
			e.printStackTrace(new PrintWriter(errors));
			this.info("LB207ServiceImpl.genFile error = " + errors.toString());
		}
	}

	private void genExcel(TitaVo titaVo, List<HashMap<String, String>> LBList) throws LogicException {
		this.info("=========== LB207 genExcel: ");
		this.info("LB207 genExcel TitaVo=" + titaVo);

		// 自訂標題 inf (首筆/尾筆)
		String inf = "";
		String infLast = "";
		String txt = "";

		// B207 授信戶基本資料檔
		inf = "交易代碼(1~1);總行代號(2~4);空白(5~8);資料日期(9~15);授信戶IDN(16~25);中文姓名(26~55);英文姓名(56~75);出生日期(76~82);戶籍地址(83~181);聯絡地址郵遞區號(182~186);聯絡地址(187~285);聯絡電話(286~315);行動電話(316~331);空白(332~336);教育程度代號(337~337);自有住宅有無(338~338);任職機構名稱(339~383);任職機構統一編號(384~391);職業類別(392~397);任職機構電話(398~413);職位名稱(414~428);服務年資(429~430);年收入(431~436);年收入資料年月(437~441);性別(442~442);國籍(443~444);護照號碼(445~464);舊有稅籍編號(465~474);中文姓名超逾10個字之全名(475~574);空白(575~610)";
		txt = "F0;F1;F2;F3;F4;F5;F6;F7;F8;F9;F10;F11;F12;F13;F14;F15;F16;F17;F18;F19;F20;F21;F22;F23;F24;F25;F26;F27;F28;F29";

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
			String strFileName = "458"+ strTodayMM + strTodaydd + "1" + ".207";		// 458+月日+序號(1).207
			this.info("------------titaVo.getEntDyI()=" + titaVo.getEntDyI());
			this.info("------------titaVo.getKinbr()=" + titaVo.getKinbr());
			makeExcel.open(titaVo, titaVo.getEntDyI(), titaVo.getKinbr(), "B207", "授信戶基本資料檔", strFileName);

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
			this.info("LB207ServiceImpl.genExcel error = " + errors.toString());
		}
	}

	private void setAllWidth() throws LogicException {
		this.info("=========== setAllWidth()");
		makeExcel.setWidth(1, 5);
		makeExcel.setWidth(2, 7);
		makeExcel.setWidth(3, 8);
		makeExcel.setWidth(4, 11);
		makeExcel.setWidth(5, 14);
		makeExcel.setWidth(6, 34);
		makeExcel.setWidth(7, 24);
		makeExcel.setWidth(8, 11);
		makeExcel.setWidth(9, 103);
		makeExcel.setWidth(10, 9);
		makeExcel.setWidth(11, 103);
		makeExcel.setWidth(12, 34);
		makeExcel.setWidth(13, 20);
		makeExcel.setWidth(14, 9);
		makeExcel.setWidth(15, 5);
		makeExcel.setWidth(16, 5);
		makeExcel.setWidth(17, 49);
		makeExcel.setWidth(18, 12);
		makeExcel.setWidth(19, 10);
		makeExcel.setWidth(20, 20);
		makeExcel.setWidth(21, 19);
		makeExcel.setWidth(22, 6);
		makeExcel.setWidth(23, 10);
		makeExcel.setWidth(24, 9);
		makeExcel.setWidth(25, 5);
		makeExcel.setWidth(26, 6);
		makeExcel.setWidth(27, 24);
		makeExcel.setWidth(28, 14);
		makeExcel.setWidth(29, 104);
		makeExcel.setWidth(30, 40);
	}
	
}
