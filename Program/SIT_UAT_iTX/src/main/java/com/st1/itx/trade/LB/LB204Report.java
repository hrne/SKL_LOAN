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
import com.st1.itx.db.service.springjpa.cm.LB204ServiceImpl;
import com.st1.itx.util.common.MakeExcel;
import com.st1.itx.util.common.MakeReport;
import com.st1.itx.util.common.MakeFile;

import java.util.Date;
import java.text.SimpleDateFormat;
import java.text.DecimalFormat;

@Component("lB204Report")
@Scope("prototype")

public class LB204Report extends MakeReport {
	private static final Logger logger = LoggerFactory.getLogger(LB204Report.class);

	Date dateNow = new Date();
	SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
	String strToday = String.valueOf(Integer.parseInt(sdf.format(dateNow)) - 19110000); // 民國年月日
	String strTodayMM = strToday.substring(3,5); // 月
	String strTodaydd= strToday.substring(5,7);  // 日
	int listCount = 0;

	@Autowired
	LB204ServiceImpl lB204ServiceImpl;

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
		// LB204 聯徵授信餘額日報檔
		try {
			this.info("-----strToday=" + strToday);
			this.info("-----strTodayMM=" + strTodayMM);
			this.info("-----strTodaydd=" + strTodaydd);
			
			List<HashMap<String, String>> LBList = lB204ServiceImpl.findAll(titaVo);
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
			this.info("LB204ServiceImpl.findAll error = " + errors.toString());
		}
	}

	private void genFile(TitaVo titaVo, List<HashMap<String, String>> LBList) throws LogicException {
		this.info("=========== LB204 genFile : ");

		String acctDate = titaVo.getEntDy();   // 8位 民國年
		this.info("-----LB204 genFile acctDate=" + acctDate);

		String txt = "F0;F1;F2;F3;F4;F5;F6;F7;F8;F9;F10;F11;F12";
		String txt1[] = txt.split(";");

		try {
			int sumLineAmt = 0;			// 訂約總金額
			int sumDrawdownAmt = 0;		// 授信／清償金額總金額
			String strContent = "";
			DecimalFormat formatter = new DecimalFormat("0");
					
//			String strFileName = "458"+ acctDate.substring(4,8) + "1" + ".204";		// 458+月日+序號(1).204
			String strFileName = "458"+ strTodayMM + strTodaydd + "1" + ".204";		// 458+月日+序號(1).204
			makeFile.open(titaVo, titaVo.getEntDyI(), titaVo.getKinbr(), "B204", "聯徵授信餘額日報檔", strFileName, 2);

			// 首筆
			strContent = "JCIC-DAT-B204-V01-458" + StringUtils.repeat(" ",5) + strToday + "01" + StringUtils.repeat(" ",10) 
					   + makeFile.fillStringR(" ", 16, ' ')   // 聯絡電話
					   + StringUtils.repeat(" ", 67) ;
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
							case 3 :	strField = makeFile.fillStringL(strField, 7, '0');			break;
							case 4 :	strField = makeFile.fillStringR(strField,50, ' ');			break;
							case 5 :	strField = makeFile.fillStringR(strField,10, ' ');			break;
							case 6 :	strField = makeFile.fillStringR(strField, 1, ' ');			break;
							case 7 :	strField = makeFile.fillStringR(strField, 1, ' ');			break;
							case 8 :	strField = makeFile.fillStringR(strField, 1, ' ');			break;
							case 9 :	// 訂約金額
										if (Float.parseFloat(strField) < 0) {
											formatter.applyPattern("000000000");		// 數值會自動補-,所以格式少一位
											strField = formatter.format(Float.parseFloat(strField));
										} else {
											formatter.applyPattern("0000000000");
											strField = formatter.format(Float.parseFloat(strField));
										}
										sumLineAmt = sumLineAmt + Integer.parseInt(strField);
										break;							
							case 10:	// 新增核准額度當日動撥／清償金額
										strField = makeFile.fillStringL(strField,10, '0');
										sumDrawdownAmt = sumDrawdownAmt + Integer.parseInt(strField);
										break;							
							case 11:	// 本筆新增核准額度應計入DBR22倍規範之金額
										if (Float.parseFloat(strField) < 0) {
											formatter.applyPattern("000000000");		// 數值會自動補-,所以格式少一位
											strField = formatter.format(Float.parseFloat(strField));
										} else {
											formatter.applyPattern("0000000000");
											strField = formatter.format(Float.parseFloat(strField));
										}
										break;							
							case 12:	strField = makeFile.fillStringR(strField, 1, ' ');			break;
							case 13:	strField = makeFile.fillStringR(strField,20, ' ');			break;
							default:	strField = "";                                              break;
						}
						strContent = strContent + strField;
					}
										
					makeFile.put(strContent);
				}
			}

			// 末筆
			strContent = "TRLR" + StringUtils.repeat(" ",3)
					   + makeFile.fillStringL(String.valueOf(sumLineAmt), 11, '0')    	// 訂約總金額
					   + StringUtils.repeat(" ",3)
					   + makeFile.fillStringL(String.valueOf(sumDrawdownAmt), 11, '0')	// 授信／清償金額總金額
					   + StringUtils.repeat(" ",3)
					   + makeFile.fillStringL(String.valueOf(listCount), 7, '0') 
					   + StringUtils.repeat(" ",86);
			makeFile.put(strContent);

			long sno = makeFile.close();
			// makeFile.toFile(sno);	// 不直接下傳

		} catch (Exception e) {
			StringWriter errors = new StringWriter();
			e.printStackTrace(new PrintWriter(errors));
			this.info("LB204ServiceImpl.genFile error = " + errors.toString());
		}
	}

	private void genExcel(TitaVo titaVo, List<HashMap<String, String>> LBList) throws LogicException {
		this.info("=========== LB204 genExcel: ");
		this.info("LB204 genExcel TitaVo=" + titaVo);

		// 自訂標題 inf (首筆/尾筆)
		String inf = "";
		// String infLast = "";
		String txt = "";

		// B204 聯徵授信餘額日報檔
		inf = "總行代號(1~4);分行代號(5~9);新增核准額度日期／清償日期／額度到期或解約日期(10~17);額度控制編碼／帳號(18~68);" 
			+ "授信戶IDN/BAN(69~79);科目別(80~81);科目別註記(82~83);交易別(84~85);訂約金額(86~96);新增核准額度當日動撥／清償金額(97~107);" 
			+ "本筆新增核准額度應計入DBR22倍規範之金額(108~118);1~7欄資料值相同之交易序號(119~120);空白(121~141)" ; 
		txt = "F0;F1;F2;F3;F4;F5;F6;F7;F8;F9;F10;F11;F12";

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
			String strFileName = "458"+ strTodayMM + strTodaydd + "1" + ".204";		// 458+月日+序號(1).204
			this.info("------------titaVo.getEntDyI()=" + titaVo.getEntDyI());
			this.info("------------titaVo.getKinbr()=" + titaVo.getKinbr());
			makeExcel.open(titaVo, titaVo.getEntDyI(), titaVo.getKinbr(), "B204", "聯徵授信餘額日報檔", strFileName);

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
			this.info("LB204ServiceImpl.genExcel error = " + errors.toString());
		}
	}

	private void setAllWidth() throws LogicException {
		this.info("=========== setAllWidth()");
		makeExcel.setWidth(1, 5);
		makeExcel.setWidth(2, 6);
		makeExcel.setWidth(3, 9);
		makeExcel.setWidth(4, 52);
		makeExcel.setWidth(5, 12);
		makeExcel.setWidth(6, 3);
		makeExcel.setWidth(7, 3);
		makeExcel.setWidth(8, 3);
		makeExcel.setWidth(9, 12);
		makeExcel.setWidth(10, 12);
		makeExcel.setWidth(11, 12);
		makeExcel.setWidth(12, 3);
		makeExcel.setWidth(13, 6);
	}
	
}
