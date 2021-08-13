package com.st1.itx.trade.LN;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.db.service.springjpa.cm.LNM39HPServiceImpl;
import com.st1.itx.util.common.MakeExcel;
import com.st1.itx.util.common.MakeReport;
import com.st1.itx.util.common.MakeFile;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.util.Date;
import java.util.Calendar;
import java.text.SimpleDateFormat;

@Component("lNM39HPReport")
@Scope("prototype")

public class LNM39HPReport extends MakeReport {
	// private static final Logger logger = LoggerFactory.getLogger(LNM39HPReport.class);

	Date dateNow = new Date();
	SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
	String strToday = String.valueOf(Integer.parseInt(sdf.format(dateNow)) - 19110000); // 民國年

	Calendar calendar = Calendar.getInstance();
	String monthlyEndDay = "00000000";		// 會計日當月的月底日曆日
	
	@Autowired
	public LNM39HPServiceImpl lNM39HPServiceImpl;

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
		// LNM39HP 欄位清單８
		try {
			this.info("---------- LNM39HPReport exec titaVo: " + titaVo);
			List<HashMap<String, String>> LNM39HPList = lNM39HPServiceImpl.findAll(titaVo);
			genFile(titaVo, LNM39HPList);
//			genExcel(titaVo, LNM39HPList);
		} catch (Exception e) {
			StringWriter errors = new StringWriter();
			e.printStackTrace(new PrintWriter(errors));
			this.info("LNM39HPServiceImpl.findAll error = " + errors.toString());
		}
	}

	private void genFile(TitaVo titaVo, List<HashMap<String, String>> L7List) throws LogicException {
		this.info("=========== LNM39HP genFile : ");
		String txt = "F0;F1;F2;F3;F4;F5;F6;F7;F8;F9;F10;F11;F12;F13;F14;F15;F16;F17;F18;F19;F20";
		String txt1[] = txt.split(";");
		DecimalFormat formatter = new DecimalFormat("0");

		try {
			String strContent = "";

			makeFile.open(titaVo, titaVo.getEntDyI(), titaVo.getKinbr(), "LNFHP", "LNM39HP 欄位清單８", "LNFHP.TXT", 1);  // UTF-8

			// 標題列
			// strContent = "";
			// makeFile.put(strContent);

			if (L7List.size() == 0) {	// 無資料時，會出空檔

			} else {
				this.info("*** : " + L7List);
				// 欄位內容
				for (HashMap<String, String> tL7Vo : L7List) {
					strContent = "";
					for (int j = 1; j <= tL7Vo.size(); j++) {
						String strField = "";
						if (tL7Vo.get(txt1[j - 1]) == null) {
							strField = "";
						} else {
							strField = tL7Vo.get(txt1[j - 1]).replace(",", "，").trim(); // csv檔: 逗號轉全形
							strField = tL7Vo.get(txt1[j - 1]).trim();
						}

						// 格式處理
						switch (j) {
							case 1 : strField = makeFile.fillStringL(strField,  7, '0');	break;		// 戶號
							case 2 : strField = makeFile.fillStringR(" ",10, ' ');			break;		// 借款人統編  放空白
							case 3 : strField = makeFile.fillStringL(strField,  3, '0');	break;		// 額度編號
							case 4 : strField = makeFile.fillStringL(strField,  7, '0');	break;		// 核准號碼
							case 5 : strField = makeFile.fillStringL(strField,  1, '0');	break;		// 1=企業戶(含企金自然人) 2=個人戶
							case 6 : strField = makeFile.fillStringL(strField,  8, '0');	break;		// 核准日期
							case 7 : strField = makeFile.fillStringL(strField,  8, '0');	break;		// 初貸日期
							case 8 : formatter.applyPattern("00000000000");
										strField = formatter.format(Float.parseFloat(strField = (strField.isEmpty() ? "0" : strField)));
										strField = makeFile.fillStringL(strField, 11, '0');	break;    	// 核准金額(台幣)						
							case 9 : strField = makeFile.fillStringR(strField,  2, ' ');	break;		// 產品別
							case 10: formatter.applyPattern("00000000000");
										strField = formatter.format(Float.parseFloat(strField = (strField.isEmpty() ? "0" : strField)));
										strField = makeFile.fillStringL(strField, 11, '0');	break;    	// 可動用餘額(台幣)						
							case 11: strField = makeFile.fillStringL(strField,  1, '0');	break;		// 該筆額度是否可循環動用  0: 非循環動用  1: 循環動用
							case 12: strField = makeFile.fillStringL(strField,  1, '0');	break;		// 該筆額度是否為不可撤銷  1=是 0=否
							case 13: strField = strField.substring(1);
										strField = makeFile.fillStringL(strField,  5, '0');	break;		// 主計處行業別代碼                   
							case 14: strField = makeFile.fillStringR(strField,  1, ' ');	break;		// 原始認列時時信用評等    
							case 15: strField = makeFile.fillStringR(strField,  1, ' ');	break;		// 原始認列時信用評等模型  
							case 16: strField = makeFile.fillStringR(strField,  1, ' ');	break;		// 財務報導日時信用評等    
							case 17: strField = makeFile.fillStringR(strField,  1, ' ');	break;		// 財務報導日時信用評等模型
							case 18: strField = makeFile.fillStringL(strField,  2, '0');	break;		// 違約損失率模型          
							case 19: formatter.applyPattern("00.00000");
										strField = formatter.format(Float.parseFloat(strField = (strField.isEmpty() ? "0" : strField)));
										strField = makeFile.fillStringL(strField,  8, '0');	break;    	// 違約損失率
							case 20: formatter.applyPattern("00000000000");
										strField = formatter.format(Float.parseFloat(strField = (strField.isEmpty() ? "0" : strField)));
										strField = makeFile.fillStringL(strField, 11, '0');	break;    	// 核准金額(交易幣)						
							case 21: formatter.applyPattern("00000000000");
										strField = formatter.format(Float.parseFloat(strField = (strField.isEmpty() ? "0" : strField)));
										strField = makeFile.fillStringL(strField, 11, '0');	break;    	// 可動用餘額(交易幣)						
							default: strField = "";										   	break;
						}
						strContent = strContent + strField;
						if (j != tL7Vo.size()) {
							strContent = strContent + ",";
						}
					}
					makeFile.put(strContent);
				}
			}

			long sno = makeFile.close();
			this.info("=========== LNM39HP genFile close === ");
			// makeFile.toFile(sno);	// 不直接下傳

			// 產製[控制檔]
			makeFile.open(titaVo, titaVo.getEntDyI(), titaVo.getKinbr(), "LNFHPIDX", "LNM39HP 欄位清單８控制檔", "LNFHP.IDX", 1);  // UTF-8
			strContent = sdf.format(dateNow) + "," + calendarEntDyMonthlyEndDate(titaVo) + "," + String.format("%06d",L7List.size());
			makeFile.put(strContent);
			long snoIdx = makeFile.close();
			this.info("=========== LNM39HP genFile IDX close === ");
			// makeFile.toFile(snoIdx);	// 不直接下傳

		} catch (Exception e) {
			StringWriter errors = new StringWriter();
			e.printStackTrace(new PrintWriter(errors));
			this.info("LNM39HPServiceImpl.genFile error = " + errors.toString());
		}
	}

	private	String calendarEntDyMonthlyEndDate(TitaVo titaVo) throws LogicException {
		// 會計日當月的月底日曆日
		try { 
			this.info("=========== titaVo.getEntDy()=" + titaVo.getEntDy());
			Date dateEntDy = sdf.parse(String.valueOf(Integer.parseInt(titaVo.getEntDy()) + 19110000));
			calendar.setTime(dateEntDy);
			// 月份+1，天設置為0。下個月第0天，就是這個月最後一天
			calendar.add(Calendar.MONTH, 1);
			calendar.set(Calendar.DAY_OF_MONTH, 0); 
			monthlyEndDay = sdf.format(calendar.getTime());
			this.info("=========== lastMonthlyDay=" + monthlyEndDay);
		} catch (ParseException e) { 
			e.printStackTrace();
		}
		return monthlyEndDay; 
	}
	
	private void genExcel(TitaVo titaVo, List<HashMap<String, String>> LNList) throws LogicException {
		this.info("=========== LNM39HP genExcel: ");
		this.info("LNM39HP genExcel TitaVo=" + titaVo);

		// 自訂標題 inf (首筆/尾筆)
		String inf = "";
		// String infLast = "";
		String txt = "";

		// 清單7
		inf = "";
		txt = "F0;F1;F2;F3;F4;F5;F6;F7;F8;F9;F10;F11;F12;F13;F14;F15;F16;F17;F18;F19;F20";

		// 自訂標題列寫入記號
		boolean infFg = true;
		if ((inf.trim()).equals("")) {
			infFg = false;
		}

		String inf1[] = inf.split(";");
		String txt1[] = txt.split(";");

		try {
//			List<HashMap<String, String>> LNList = null;
//			LNList = lNM39HPServiceImpl.findAll(titaVo);

//			this.info("-----------------" + LNList);

			// 若有底稿，需自行判斷列印起始行數 i
			// 若無底稿，設定列印起始行數 i=1
			int i = 1;
			this.info("------------titaVo.getEntDyI()=" + titaVo.getEntDyI());
			this.info("------------titaVo.getKinbr()=" + titaVo.getKinbr());
			makeExcel.open(titaVo, titaVo.getEntDyI(), titaVo.getKinbr(), "LNFHP", "LNM39HP 欄位清單８", "LNFHP");

			// 自訂標題列
			if (infFg == true) {
				for (int j = 1; j <= inf1.length; j++) {
					makeExcel.setValue(i, j, inf1[j - 1]);
				}
				infFg = false;
				i++;
			}

			// 欄位內容
			if (LNList != null) {
				this.info("--------LNList.size=" + LNList.size());
				for (HashMap<String, String> tLBVo : LNList) {
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

			// 設定欄位寬度
			if (LNList != null) {
				makeExcel.setWidth(1,  9);
				makeExcel.setWidth(2, 12);
				makeExcel.setWidth(3,  5);
				makeExcel.setWidth(4,  9);
				makeExcel.setWidth(5,  3);
				makeExcel.setWidth(6, 10);
				makeExcel.setWidth(7, 10);
				makeExcel.setWidth(8, 13);
				makeExcel.setWidth(9,  4);
				makeExcel.setWidth(10,13);
				makeExcel.setWidth(11, 3);
				makeExcel.setWidth(12, 3);
				makeExcel.setWidth(13, 7);
				makeExcel.setWidth(14, 3);
				makeExcel.setWidth(15, 3);
				makeExcel.setWidth(16, 3);
				makeExcel.setWidth(17, 3);
				makeExcel.setWidth(18, 4);
				makeExcel.setWidth(19,10);
				makeExcel.setWidth(20,13);
				makeExcel.setWidth(21,13);
			}

			long sno = makeExcel.close();
			// makeExcel.toExcel(sno);	// 不直接下傳

		} catch (Exception e) {
			StringWriter errors = new StringWriter();
			e.printStackTrace(new PrintWriter(errors));
			this.info("LNM39HPServiceImpl.genExcel error = " + errors.toString());
		}
	}

}
