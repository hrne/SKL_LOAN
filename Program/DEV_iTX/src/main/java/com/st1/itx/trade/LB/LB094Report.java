package com.st1.itx.trade.LB;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.db.service.springjpa.cm.LB094ServiceImpl;
import com.st1.itx.util.common.MakeExcel;
import com.st1.itx.util.common.MakeReport;
import com.st1.itx.util.common.MakeFile;

import java.util.Date;
import java.text.SimpleDateFormat;

@Component("lB094Report")
@Scope("prototype")

public class LB094Report extends MakeReport {
	// private static final Logger logger = LoggerFactory.getLogger(LB094Report.class);

	Date dateNow = new Date();
	SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
	String strToday = String.valueOf(Integer.parseInt(sdf.format(dateNow)) - 19110000); // 民國年月日
	String strTodayMM = strToday.substring(3,5); // 月
	String strTodaydd= strToday.substring(5,7);  // 日
	int listCount = 0;

	@Autowired
	public LB094ServiceImpl lB094ServiceImpl;

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
		// LB094 股票擔保品明細檔
		try {
			this.info("-----strToday=" + strToday);
			this.info("-----strTodayMM=" + strTodayMM);
			this.info("-----strTodaydd=" + strTodaydd);
			
			List<HashMap<String, String>> LBList = lB094ServiceImpl.findAll(titaVo);
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
			this.info("LB094ServiceImpl.findAll error = " + errors.toString());
		}
	}

	private void genFile(TitaVo titaVo, List<HashMap<String, String>> LBList) throws LogicException {
		this.info("=========== LB094 genFile : ");
		String txt = "F0;F1;F2;F3;F4;F5;F6;F7;F8;F9;F10;F11;F12;F13;F14;F15;F16;F17;F18;F19;F20;F21;F22;F23";
		String txt1[] = txt.split(";");

		try {
			String strContent = "";
			
			String strFileName = "458"+ strTodayMM + strTodaydd + "1" + ".094";		// 458+月日+序號(1).094
			makeFile.open(titaVo, titaVo.getEntDyI(), titaVo.getKinbr(), "B094", "股票擔保品明細檔", strFileName, 2);

			// 首筆
			strContent = "JCIC-DAT-B093-V01-458" + StringUtils.repeat(" ",5) + strToday + "01" + StringUtils.repeat(" ",10) 
			   		   + makeFile.fillStringR("02-23895858#7067", 16, ' ') + makeFile.fillStringR("審查單位聯絡人－許高政", 80, ' ')
			   		   + StringUtils.repeat(" ",51);
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
							case 1 :	strField = makeFile.fillStringL(strField,  2, '0');		break;
							case 2 :	strField = makeFile.fillStringL(strField,  3, '0');		break;
							case 3 :	strField = makeFile.fillStringL(strField,  4, '0');		break;
							case 4 :	strField = makeFile.fillStringR(strField,  2, ' ');		break;
							case 5 :	strField = makeFile.fillStringR(strField, 50, ' ');		break;
							case 6 :	strField = makeFile.fillStringR(strField,  2, ' ');		break;
							case 7 :	strField = makeFile.fillStringR(strField, 10, ' ');		break;
							case 8 :	strField = makeFile.fillStringL(strField,  8, '0');		break;
							case 9 :	strField = makeFile.fillStringL(strField,  7, '0');		break;
							case 10:	strField = makeFile.fillStringL(strField, 10, '0');		break;
							case 11:	strField = makeFile.fillStringL(strField,  7, '0');		break;
							case 12:	strField = makeFile.fillStringR(strField,  8, ' ');		break;
							case 13:	strField = makeFile.fillStringR(strField,  2, ' ');		break;
							case 14:	strField = makeFile.fillStringR(strField, 10, ' ');		break;
							case 15:	strField = makeFile.fillStringL(strField,  1, '0');		break;
							case 16:	strField = makeFile.fillStringR(strField,  3, ' ');		break;
							case 17:	strField = makeFile.fillStringL(strField, 14, '0');		break;
							case 18:	strField = makeFile.fillStringL(strField, 10, '0');		break;
							case 19:	strField = makeFile.fillStringR(strField,  1, ' ');		break;
							case 20:	strField = makeFile.fillStringR(strField,  1, ' ');		break;
							case 21:	strField = makeFile.fillStringR(strField, 10, ' ');		break;
							case 22:	strField = makeFile.fillStringL(strField,  8, '0');		break;
							case 23:	strField = makeFile.fillStringR(strField, 14, ' ');		break;
							case 24:	strField = makeFile.fillStringL(strField,  5, '0');		break;
							default:  	strField = "";                                          break;
						}
						strContent = strContent + strField;
					}
					makeFile.put(strContent);
				}
			}

			// 末筆
			strContent = "TRLR" + makeFile.fillStringL(String.valueOf(listCount), 8, '0') + StringUtils.repeat(" ",180);
			makeFile.put(strContent);

			long sno = makeFile.close();
			// makeFile.toFile(sno);	// 不直接下傳

		} catch (Exception e) {
			StringWriter errors = new StringWriter();
			e.printStackTrace(new PrintWriter(errors));
			this.info("LB094ServiceImpl.genFile error = " + errors.toString());
		}
	}

	private void genExcel(TitaVo titaVo, List<HashMap<String, String>> LBList) throws LogicException {
		this.info("=========== LB094 genExcel: ");
		this.info("LB094 genExcel TitaVo=" + titaVo);

		// 自訂標題 inf (首筆/尾筆)
		String inf = "";
		String infLast = "";
		String txt = "";

		// B094 股票擔保品明細檔
		inf = "資料別(1~2);總行代號(3~5);分行代號(6~9);空白(10~11);擔保品控制編碼(12~61);擔保品類別(62~63);擔保品所有權人或代表人IDN/BAN(64~73);鑑估值(74~81);鑑估日期(82~88);可放款值(89~98);設質日期(99~105);發行機構BAN(106~113);發行機構所在國別(114~115);股票代號(116~125);股票種類(126~126);幣別(127~129);設定股數餘額(130~143);股票質押授信餘額(144~153);公司內部人職稱(154~154);公司內部人身分註記(155~155);公司內部人法定關係人(156~165);處分價格(166~173);空白(174~187);資料所屬年月(188~192)";
		txt = "F0;F1;F2;F3;F4;F5;F6;F7;F8;F9;F10;F11;F12;F13;F14;F15;F16;F17;F18;F19;F20;F21;F22;F23";

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
			String strFileName = "458"+ strTodayMM + strTodaydd + "1" + ".094";		// 458+月日+序號(1).094
			this.info("------------titaVo.getEntDyI()=" + titaVo.getEntDyI());
			this.info("------------titaVo.getKinbr()=" + titaVo.getKinbr());
			makeExcel.open(titaVo, titaVo.getEntDyI(), titaVo.getKinbr(), "B094", "股票擔保品明細檔", strFileName);

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
			this.info("LB094ServiceImpl.genExcel error = " + errors.toString());
		}
	}

	private void setAllWidth() throws LogicException {
		this.info("=========== setAllWidth()");
		makeExcel.setWidth(1, 6);
		makeExcel.setWidth(2, 7);
		makeExcel.setWidth(3, 8);
		makeExcel.setWidth(4, 6);
		makeExcel.setWidth(5, 54);
		makeExcel.setWidth(6, 6);
		makeExcel.setWidth(7, 14);
		makeExcel.setWidth(8, 12);
		makeExcel.setWidth(9, 11);
		makeExcel.setWidth(10, 14);
		makeExcel.setWidth(11, 11);
		makeExcel.setWidth(12, 12);
		makeExcel.setWidth(13, 6);
		makeExcel.setWidth(14, 14);
		makeExcel.setWidth(15, 5);
		makeExcel.setWidth(16, 7);
		makeExcel.setWidth(17, 18);
		makeExcel.setWidth(18, 14);
		makeExcel.setWidth(19, 5);
		makeExcel.setWidth(20, 5);
		makeExcel.setWidth(21, 14);
		makeExcel.setWidth(22, 12);
		makeExcel.setWidth(23, 18);
		makeExcel.setWidth(24, 9);
	}
	
}
