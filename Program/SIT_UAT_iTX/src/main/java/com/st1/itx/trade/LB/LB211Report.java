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
import com.st1.itx.db.service.springjpa.cm.LB211ServiceImpl;
import com.st1.itx.util.common.MakeExcel;
import com.st1.itx.util.common.MakeReport;
import com.st1.itx.util.common.MakeFile;

import java.util.Date;
import java.text.SimpleDateFormat;
//import java.text.DecimalFormat;

@Component("lB211Report")
@Scope("prototype")

public class LB211Report extends MakeReport {
	private static final Logger logger = LoggerFactory.getLogger(LB211Report.class);

	Date dateNow = new Date();
	SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
	String strToday = String.valueOf(Integer.parseInt(sdf.format(dateNow)) - 19110000); // 民國年月日
	String strTodayMM = strToday.substring(3,5); // 月
	String strTodaydd= strToday.substring(5,7);  // 日
	int listCount = 0;

	@Autowired
	LB211ServiceImpl lB211ServiceImpl;

	@Autowired
	MakeExcel makeExcel;

	@Autowired
	public MakeFile makeFile;

	// 自訂明細標題
	@Override
	public void printTitle() {
		this.info("printTitle nowRow = " + this.NowRow);
	}

	public void exec(TitaVo titaVo) throws LogicException {
		// LB211 聯徵每日授信餘額變動資料檔
		try {
			this.info("-----strToday=" + strToday);
			this.info("-----strTodayMM=" + strTodayMM);
			this.info("-----strTodaydd=" + strTodaydd);
			
			List<HashMap<String, String>> LBList = lB211ServiceImpl.findAll(titaVo);
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
			this.info("LB211ServiceImpl.findAll error = " + errors.toString());
		}
	}

	private void genFile(TitaVo titaVo, List<HashMap<String, String>> LBList) throws LogicException {
		this.info("=========== LB211 genFile : ");

		String acctDate = titaVo.getEntDy();   // 8位 民國年
		this.info("-----LB211 genFile acctDate=" + acctDate);

		String txt = "F0;F1;F2;F3;F4;F5;F6;F7;F8;F9;F10;F11;F12;F13;F14;F15;F16;F17";
		String txt1[] = txt.split(";");

		try {
			int sumTxAmt = 0;		// 本筆撥款／還款總金額
			int sumLoanBal = 0;		// 本筆撥款／還款後餘額
			String strContent = "";
			// DecimalFormat formatter = new DecimalFormat("0");
					
//			String strFileName = "458"+ acctDate.substring(4,8) + "1" + ".211";		// 458+月日+序號(1).211
			String strFileName = "458"+ strTodayMM + strTodaydd + "1" + ".211";		// 458+月日+序號(1).211
			makeFile.open(titaVo, titaVo.getEntDyI(), titaVo.getKinbr(), "B211", "聯徵每日授信餘額變動資料檔", strFileName, 2);

			// 首筆
			strContent = "JCIC-DAT-B211-V01-458" + StringUtils.repeat(" ",5) + strToday + "01" + StringUtils.repeat(" ",10) 
					   + makeFile.fillStringR("審查聯絡人－王薏涵", 20, ' ') + makeFile.fillStringR("02-23895858#7067", 16, ' ') 
					   + makeFile.fillStringR(" ", 20, ' ') + makeFile.fillStringR(" ", 16, ' ') 
					   + StringUtils.repeat(" ", 80) + StringUtils.repeat(" ", 43) ;
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
							case 1 :	strField = makeFile.fillStringL(strField, 3, '0');			break;
							case 2 :	strField = makeFile.fillStringL(strField, 4, '0');			break;
							case 3 :	strField = makeFile.fillStringR(strField, 1, ' ');			break;
							case 4 :	strField = makeFile.fillStringR(strField,10, ' ');			break;
							case 5 :	strField = makeFile.fillStringR(strField, 1, ' ');			break;
							case 6 :	strField = makeFile.fillStringL(strField, 7, '0');			break;
							case 7 :	strField = makeFile.fillStringR(strField,50, ' ');			break;
							case 8 :	// 本筆撥款／還款金額
										strField = makeFile.fillStringL(strField,10, '0');			
										sumTxAmt = sumTxAmt + Integer.parseInt(strField);
										break;
							case 9 :	// 本筆撥款／還款後餘額
										strField = makeFile.fillStringL(strField,10, '0');			
										sumLoanBal = sumLoanBal + Integer.parseInt(strField);
										break;
							case 10:	strField = makeFile.fillStringR(strField, 1, ' ');			break;
							case 11:	strField = makeFile.fillStringR(strField, 3, ' ');			break;
							case 12:	strField = makeFile.fillStringR(strField, 1, ' ');			break;
							case 13:	strField = makeFile.fillStringR(strField, 1, ' ');			break;
							case 14:	// 呆帳轉銷年月
										if (strField.trim().equals("0")) {
											strField = StringUtils.repeat(" ",5);
										} else {
											strField = makeFile.fillStringL(strField, 5, '0');
										}
										break;
							case 15:	strField = makeFile.fillStringR(strField, 1, ' ');			break;
							case 16:	strField = makeFile.fillStringR(strField, 1, ' ');			break;
							case 17:	strField = makeFile.fillStringR(strField, 1, ' ');			break;
							case 18:	strField = makeFile.fillStringR(strField,130, ' ');			break;
							default:	strField = "";                                              break;
						}
						strContent = strContent + strField;
					}
										
					makeFile.put(strContent);
				}
			}

			// 末筆
			strContent = "TRLR" + StringUtils.repeat(" ",3)
					   + makeFile.fillStringL(String.valueOf(sumTxAmt), 13, '0')    // 本筆撥款／還款總金額
					   + makeFile.fillStringL(String.valueOf(sumLoanBal), 12, '0') 	// 本筆撥款／還款後餘額
					   + StringUtils.repeat(" ",2)
					   + makeFile.fillStringL(String.valueOf(listCount), 9, '0') 
					   + StringUtils.repeat(" ",197);
			makeFile.put(strContent);

			long sno = makeFile.close();
			// makeFile.toFile(sno);	// 不直接下傳

		} catch (Exception e) {
			StringWriter errors = new StringWriter();
			e.printStackTrace(new PrintWriter(errors));
			this.info("LB211ServiceImpl.genFile error = " + errors.toString());
		}
	}

	private void genExcel(TitaVo titaVo, List<HashMap<String, String>> LBList) throws LogicException {
		this.info("=========== LB211 genExcel: ");
		this.info("LB211 genExcel TitaVo=" + titaVo);

		// 自訂標題 inf (首筆/尾筆)
		String inf = "";
		// String infLast = "";
		String txt = "";

		// B211 聯徵每日授信餘額變動資料檔
		inf = "總行代號(1~3);分行代號(4~7);交易代碼(8~8);授信戶IDN/BAN(9~18);交易屬性(19~19);交易日期(20~26);" 
			+ "本筆撥款／還款帳號(27~76);本筆撥款／還款金額(77~86);本筆撥款／還款後餘額(87~96);本筆還款後之還款紀錄(97~97);本筆還款後之債權結案註記(98~100);" 
			+ "科目別(101~101);科目別註記(102~102);呆帳轉銷年月(103~107);個人消費性貸款註記(108~108);融資業務分類(109~109);用途別(110~110);空白(111~240)" ;
		txt = "F0;F1;F2;F3;F4;F5;F6;F7;F8;F9;F10;F11;F12;F13;F14;F15;F16;F17";

		// 自訂標題列寫入記號
		boolean infFg = true;
		if ((inf.trim()).equals("")) {
			infFg = false;
		}

		String inf1[] = inf.split(";");
		String txt1[] = txt.split(";");

		try {
			// String strContent = "";
			// 若有底稿，需自行判斷列印起始行數 i
			// 若無底稿，設定列印起始行數 i=1
			int i = 1;
			String strFileName = "458"+ strTodayMM + strTodaydd + "1" + ".211";		// 458+月日+序號(1).211
			this.info("------------titaVo.getEntDyI()=" + titaVo.getEntDyI());
			this.info("------------titaVo.getKinbr()=" + titaVo.getKinbr());
			makeExcel.open(titaVo, titaVo.getEntDyI(), titaVo.getKinbr(), "B211", "聯徵每日授信餘額變動資料檔", strFileName);

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
						
						if (j==14) {  // 呆帳轉銷年月
							if ( tLBVo.get(txt1[j - 1]).trim().equals("0") ) {
								makeExcel.setValue(i, j, "");
							}
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
			this.info("LB211ServiceImpl.genExcel error = " + errors.toString());
		}
	}

	private void setAllWidth() throws LogicException {
		this.info("=========== setAllWidth()");
		makeExcel.setWidth(1,  5);
		makeExcel.setWidth(2,  6);
		makeExcel.setWidth(3,  3);
		makeExcel.setWidth(4, 12);
		makeExcel.setWidth(5,  3);
		makeExcel.setWidth(6,  9);
		makeExcel.setWidth(7, 52);
		makeExcel.setWidth(8, 12);
		makeExcel.setWidth(9, 12);
		makeExcel.setWidth(10, 3);
		makeExcel.setWidth(11, 5);
		makeExcel.setWidth(12, 3);
		makeExcel.setWidth(13, 3);
		makeExcel.setWidth(14, 7);
		makeExcel.setWidth(15, 3);
		makeExcel.setWidth(16, 3);
		makeExcel.setWidth(17, 3);
		makeExcel.setWidth(18, 6);
	}
	
}
