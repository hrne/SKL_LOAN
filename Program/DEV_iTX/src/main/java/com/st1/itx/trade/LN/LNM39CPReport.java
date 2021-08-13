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
import com.st1.itx.db.service.springjpa.cm.LNM39CPServiceImpl;
import com.st1.itx.util.common.MakeExcel;
import com.st1.itx.util.common.MakeReport;
import com.st1.itx.util.common.MakeFile;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.util.Date;
import java.util.Calendar;
import java.text.SimpleDateFormat;

@Component("lNM39CPReport")
@Scope("prototype")

public class LNM39CPReport extends MakeReport {
	// private static final Logger logger = LoggerFactory.getLogger(LNM39CPReport.class);

	Date dateNow = new Date();
	SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
	String strToday = String.valueOf(Integer.parseInt(sdf.format(dateNow)) - 19110000); // 民國年

	Calendar calendar = Calendar.getInstance();
	String monthlyEndDay = "00000000";		// 會計日當月的月底日曆日
	
	@Autowired
	public LNM39CPServiceImpl lNM39CPServiceImpl;

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
		// LNM39CP 欄位清單３
		try {
			this.info("---------- LNM39CPReport exec titaVo: " + titaVo);
			List<HashMap<String, String>> LNM39CPList = lNM39CPServiceImpl.findAll(titaVo);
			genFile(titaVo, LNM39CPList);
//			genExcel(titaVo, LNM39CPList);
		} catch (Exception e) {
			StringWriter errors = new StringWriter();
			e.printStackTrace(new PrintWriter(errors));
			this.info("LNM39CPServiceImpl.findAll error = " + errors.toString());
		}
	}

	private void genFile(TitaVo titaVo, List<HashMap<String, String>> L7List) throws LogicException {
		this.info("=========== LNM39CP genFile : ");
		String txt = "F0;F1;F2;F3;F4;F5;F6;F7";
		String txt1[] = txt.split(";");
		DecimalFormat formatter = new DecimalFormat("0");

		try {
			String strContent = "";

			makeFile.open(titaVo, titaVo.getEntDyI(), titaVo.getKinbr(), "LNFCP", "LNM39CP 欄位清單３", "LNFCP.TXT", 1);  // UTF-8

			// 標題列
			// strContent = "";
			// makeFile.put(strContent);

			if (L7List.size() == 0) {	// 無資料時，會出空檔

			} else {
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
							case 1 : strField = makeFile.fillStringL(strField, 7, '0');		break;
							case 2 : strField = makeFile.fillStringR(" ",10, ' ');			break;		// 借款人統編  放空白
							case 3 : strField = makeFile.fillStringL(strField, 3, '0');		break;
							case 4 : strField = makeFile.fillStringL(strField, 3, '0');		break;
							case 5 : strField = makeFile.fillStringR(strField, 1, ' ');		break;		// 約定還款方式
							case 6 : strField = makeFile.fillStringL(strField, 2, '0');		break;		// 繳息週期
							case 7 : strField = makeFile.fillStringL(strField, 2, '0');		break;		// 還本週期
							case 8 : strField = makeFile.fillStringL(strField, 8, '0');		break;		// 生效日期
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
			this.info("=========== LNM39CP genFile close === ");
			// makeFile.toFile(sno);	// 不直接下傳

			// 產製[控制檔]
			makeFile.open(titaVo, titaVo.getEntDyI(), titaVo.getKinbr(), "LNFCPIDX", "LNM39CP 欄位清單３控制檔", "LNFCP.IDX", 1);  // UTF-8
			strContent = sdf.format(dateNow) + "," + calendarEntDyMonthlyEndDate(titaVo) + "," + String.format("%06d",L7List.size());
			makeFile.put(strContent);
			long snoIdx = makeFile.close();
			this.info("=========== LNM39CP genFile IDX close === ");
			// makeFile.toFile(snoIdx);	// 不直接下傳

		} catch (Exception e) {
			StringWriter errors = new StringWriter();
			e.printStackTrace(new PrintWriter(errors));
			this.info("LNM39CPServiceImpl.genFile error = " + errors.toString());
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
		this.info("=========== LNM39CP genExcel: ");
		this.info("LNM39CP genExcel TitaVo=" + titaVo);

		// 自訂標題 inf (首筆/尾筆)
		String inf = "";
		// String infLast = "";
		String txt = "";

		// 清單3
		inf = "";
		txt = "F0;F1;F2;F3;F4;F5;F6;F7";

		// 自訂標題列寫入記號
		boolean infFg = true;
		if ((inf.trim()).equals("")) {
			infFg = false;
		}

		String inf1[] = inf.split(";");
		String txt1[] = txt.split(";");

		try {
//			List<HashMap<String, String>> LNList = null;
//			LNList = lNM39CPServiceImpl.findAll(titaVo);

//			this.info("-----------------" + LNList);

			// 若有底稿，需自行判斷列印起始行數 i
			// 若無底稿，設定列印起始行數 i=1
			int i = 1;
			this.info("------------titaVo.getEntDyI()=" + titaVo.getEntDyI());
			this.info("------------titaVo.getKinbr()=" + titaVo.getKinbr());
			makeExcel.open(titaVo, titaVo.getEntDyI(), titaVo.getKinbr(), "LNFCP", "LNM39CP 欄位清單３", "LNFCP");

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
			/*
			if (LNList != null) {
				makeExcel.setWidth(1, 9);
				makeExcel.setWidth(2, 12);
				makeExcel.setWidth(3, 5);
				makeExcel.setWidth(4, 5);
				makeExcel.setWidth(5, 3);
				makeExcel.setWidth(6, 4);
				makeExcel.setWidth(7, 4);
				makeExcel.setWidth(8, 10);
			}
			*/

			long sno = makeExcel.close();
			// makeExcel.toExcel(sno);	// 不直接下傳

		} catch (Exception e) {
			StringWriter errors = new StringWriter();
			e.printStackTrace(new PrintWriter(errors));
			this.info("LNM39CPServiceImpl.genExcel error = " + errors.toString());
		}
	}

}
