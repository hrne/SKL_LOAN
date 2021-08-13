package com.st1.itx.trade.LM;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.db.service.springjpa.cm.LM002ServiceImpl;
import com.st1.itx.util.common.MakeExcel;
import com.st1.itx.util.common.MakeReport;

@Component
@Scope("prototype")
public class LM002Report extends MakeReport {
	private static final Logger logger = LoggerFactory.getLogger(LM002Report.class);

	@Autowired
	LM002ServiceImpl lM002ServiceImpl;

	@Autowired
	MakeExcel makeExcel;

	@Override
	public void printHeader() {

//		this.info("MakeReport.printHeader");
//		printHeaderP();
//		if (page == 0) {
//			printHeaderP();
//		} else {
//			printHeaderP1();
//		}
//		this.setBeginRow(2);
//
//		this.setMaxRows(maxRowsLM002);
	}

	@Override
	public void printTitle() {
	}

//	public void printHeaderP() {
//		this.print(-1, 147, "機密等級:密");
//		this.print(-1, 67, "房 貸 專 案 貸 款");
//		this.print(-1, 2, "預警系統申請作業");
//	}

//	public void printHeaderP1() {
//		this.print(-3, 60, "PAGE  " + this.getNowPage());
//	}

	public void exec(TitaVo titaVo) throws LogicException {

		this.info("LM002Report exec ...");
		exportExcel(titaVo);
		

	}

	private void exportExcel(TitaVo titaVo) throws LogicException {
		this.info("LM062Report exportExcel------");
		List<Map<String, String>> fnAllList = new ArrayList<>();

		// 民國年
		int rocYY = Integer.parseInt(titaVo.get("ENTDY").substring(0, 4));
		
		int currentYear = rocYY+1911;

		// 打開excel
		makeExcel.open(titaVo, titaVo.getEntDyI(), titaVo.getKinbr(), "LM002", "專案放款", "LM002-專案放款", "LM002_專案放款.xls",
				"預警申請表");
		// 工作表更名
		makeExcel.setSheet("預警申請表", rocYY + "預警");

		makeExcel.setValue(2, 2, rocYY-2 + " 年 度");
		makeExcel.setValue(10, 2, rocYY-1 + " 年 度");
		makeExcel.setValue(18, 2, rocYY + " 年 度");

		try {
			fnAllList = lM002ServiceImpl.doQuery(titaVo);

		} catch (Exception e) {
			StringWriter errors = new StringWriter();
			e.printStackTrace(new PrintWriter(errors));
			this.info("LM002ServiceImpl.findAll error = " + errors.toString());
		}

		if (fnAllList.size() > 0) {
			
			String[] F = new String[4];
			Arrays.fill(F, "");
			
			for (Map<String, String> tLDVo : fnAllList)
			{				
				// F0 Year
				// F1 Type
				// F2 Month
				// F3 Amount
				
				for (int i = 0; i < 4; i++)
				{
					F[i] = tLDVo.get("F"+i);
				}
				
				try
				{
					// say currentYear is 2020
					// 2018 - 2020 + 2 = 0
					// 2020 - 2020 + 2 = 2
					makeExcel.setValue(4 + (Integer.parseInt(F[0]) - currentYear + 2)*8 + Integer.parseInt(F[1])-1 ,
							           (Integer.parseInt(F[2]))+1, 
							           new BigDecimal(F[3]),
							           "R");
				}
				catch (Exception e)
				{
					this.warn("LM002 trying to parse data failed: ");
					this.info("F0: " + F[0]);
					this.info("F1: " + F[1]);
					this.info("F2: " + F[2]);
					this.info("F3: " + F[3]);
				}

				
				for (int y = 0; y < 3; y++)
				{
					for (int x = 0; x < 12; x++)
					{
						makeExcel.formulaCaculate(8 + y*8, 2 + x);
					}
				}
				
			}
			
		} else {
			makeExcel.setValue(4, 2, "本日無資料");
		}

		long sno = makeExcel.close();
		makeExcel.toExcel(sno);

	}

//	/**
//	 * 取值,若查詢結果有符合該月份的金額就取出該值,否則擺零.
//	 * 
//	 * @param listResult 傳入參數-查詢結果List
//	 * @param tmpMonth   傳入參數-月份int
//	 * @return
//	 */
//	private BigDecimal checkResult(List<Map<String, String>> listResult, int tmpMonth) {
//		BigDecimal tmpMonthAmt = BigDecimal.ZERO;
//		if (listResult != null && listResult.size() > 0) {
//			for (Map<String, String> result : listResult) {
//				if (result.get("F0").equals(String.valueOf(tmpMonth))) {
//					tmpMonthAmt = tmpMonthAmt.add(new BigDecimal(result.get("F1")));
//					break;
//				}
//			}
//		}
//		return tmpMonthAmt;
//	}

	
// ***********
// Below is obsoleted pdf version. leaving them here in case we need it again.
// ***********
	
//	private BigDecimal caculator(Map<Integer, BigDecimal> monthTotal, int tmpMonth, BigDecimal tmpMonthAmt) {
//		BigDecimal result = BigDecimal.ZERO;
//
//		if (monthTotal.containsKey(tmpMonth)) {
//			result = result.add(monthTotal.get(tmpMonth));
//		}
//		result = result.add(tmpMonthAmt);
//		return result;
//	}

	// PDF Making
//	private void exportReport(TitaVo titaVo) throws Exception {
//		this.info("LM002Report exec DB = " + titaVo.getParam(ContentName.dataBase));
//
//		int iTbsdyf = this.txBuffer.getTxCom().getTbsdyf();
//
//		this.info("LM002Report exec iTbsdyf = " + iTbsdyf);
//
//		this.setFontSize(9);
//		this.open(titaVo, titaVo.getEntDyI(), titaVo.getKinbr(), "LM002", "房貸專案放款", "", "A4", "P");
//
//		// 起始年度
//		int startYear = 2017;
//
//		// 產表年度
//		int rptYear = iTbsdyf / 10000;
//
//		List<Integer> yearList = new ArrayList<Integer>();
//
//		// 製作報表範圍年份List
//		for (; startYear <= rptYear; startYear++) {
//			yearList.add(startYear);
//
//		}
//
//		List<Integer> monthList = new ArrayList<Integer>();
//
//		// 製作每月List
//		for (int month = 1; month <= 12; month++) {
//			monthList.add(month);
//		}
//
//		// 金額格式工具(整數)
//		DecimalFormat df1 = new DecimalFormat("#,##0");
//		/*
//		 * 產製報表
//		 */
//		for (int tmpYear : yearList) {
//
//			this.info("LM002Report maxRowsLM002 = " + maxRowsLM002);
////			this.info("LM002Report NowRow = " + this.NowRow);
//			// 若此頁剩餘行數不足容納完整表格,則先換頁
//			if (maxRowsLM002 - this.NowRow < 21) {
//				this.info("LM002Report 不足完整表格,先換頁.");
//				this.newPage();
//			}
//
//			this.info("LM002Report exec tmpYear = " + tmpYear);
//
//			// 月合計容器
//			Map<Integer, BigDecimal> monthTotal = new HashMap<Integer, BigDecimal>();
//
//			// 報表年度西元轉民國
//			String year = String.valueOf(tmpYear - 1911);
//
//			this.setFontSize(9);
//			this.print(1, 1, "╔═════╦═══════════════════════════════════════════════════════════════════════╗");
//			this.print(1, 1, "║　　　　　║　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　║");
//			this.print(0, 1, "　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　" + year + "　年　度");
//			this.print(1, 1, "╠═════╬═══════════════════════════════════════════════════════════════════════╣");
//			this.print(1, 1, "║　　　　　║　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　║");
//			this.print(0, 1, "　　　　　　　　　１月　　　　２月　　　　３月　　　　４月　　　　５月　　　　６月　　　　７月　　　　８月　　　　９月　　　１０月　　　１１月　　　１２月　　");
//			this.print(1, 1, "╠═════╬═════╦═════╦═════╦═════╦═════╦═════╦═════╦═════╦═════╦═════╦═════╦═════╣");
//			this.print(1, 1, "║　　　　　║　　　　　║　　　　　║　　　　　║　　　　　║　　　　　║　　　　　║　　　　　║　　　　　║　　　　　║　　　　　║　　　　　║　　　　　║");
//			this.print(0, 2, " 921  ");
//			this.print(1, 1, "║　　　　　║　　　　　║　　　　　║　　　　　║　　　　　║　　　　　║　　　　　║　　　　　║　　　　　║　　　　　║　　　　　║　　　　　║　　　　　║");
//
//			// 查DB (dataType = 1 : 921)
//			List<Map<String, String>> listResult = lM002ServiceImpl.doQuery(tmpYear, titaVo);
//
//			// 倒出每月資料
//			for (int tmpMonth : monthList) {
//				// 取值
//				BigDecimal tmpMonthAmt = checkResult(listResult, tmpMonth);
//
//				// 印值
//				this.print(0, 25 + (12 * (tmpMonth - 1)), df1.format(tmpMonthAmt), "R");
//
//				// 月合計-運算過程
//				monthTotal.put(tmpMonth, caculator(monthTotal, tmpMonth, tmpMonthAmt));
//			}
//
//			this.print(1, 1, "╠═════╬═════╬═════╬═════╬═════╬═════╬═════╬═════╬═════╬═════╬═════╬═════╬═════╣");
//			this.print(1, 1, "║　　　　　║　　　　　║　　　　　║　　　　　║　　　　　║　　　　　║　　　　　║　　　　　║　　　　　║　　　　　║　　　　　║　　　　　║　　　　　║");
//			this.print(0, 1, "　政府優惠");
//			this.print(1, 1, "║　　　　　║　　　　　║　　　　　║　　　　　║　　　　　║　　　　　║　　　　　║　　　　　║　　　　　║　　　　　║　　　　　║　　　　　║　　　　　║");
//
//			// 查DB (dataType = 2 : 政府優惠)
//			listResult = lM002ServiceImpl.doQuery(tmpYear, titaVo);
//
//			// 倒出每月資料
//			for (int tmpMonth : monthList) {
//				// 取值
//				BigDecimal tmpMonthAmt = checkResult(listResult, tmpMonth);
//
//				// 印值
//				this.print(0, 25 + (12 * (tmpMonth - 1)), df1.format(tmpMonthAmt), "R");
//
//				// 月合計-運算過程
//				monthTotal.put(tmpMonth, caculator(monthTotal, tmpMonth, tmpMonthAmt));
//			}
//
//			this.print(1, 1, "╠═════╬═════╬═════╬═════╬═════╬═════╬═════╬═════╬═════╬═════╬═════╬═════╬═════╣");
//			this.print(1, 1, "║　　　　　║　　　　　║　　　　　║　　　　　║　　　　　║　　　　　║　　　　　║　　　　　║　　　　　║　　　　　║　　　　　║　　　　　║　　　　　║");
//			this.print(0, 1, "　首購");
//			this.print(1, 1, "║　　　　　║　　　　　║　　　　　║　　　　　║　　　　　║　　　　　║　　　　　║　　　　　║　　　　　║　　　　　║　　　　　║　　　　　║　　　　　║");
//
//			// 查DB (dataType = 3 : 首購)
//			listResult = lM002ServiceImpl.doQuery(tmpYear, titaVo);
//
//			// 倒出每月資料
//			for (int tmpMonth : monthList) {
//				// 取值
//				BigDecimal tmpMonthAmt = checkResult(listResult, tmpMonth);
//
//				// 印值
//				this.print(0, 25 + (12 * (tmpMonth - 1)), df1.format(tmpMonthAmt), "R");
//
//				// 月合計-運算過程
//				monthTotal.put(tmpMonth, caculator(monthTotal, tmpMonth, tmpMonthAmt));
//			}
//
//			this.print(1, 1, "╠═════╬═════╬═════╬═════╬═════╬═════╬═════╬═════╬═════╬═════╬═════╬═════╬═════╣");
//			this.print(1, 1, "║　　　　　║　　　　　║　　　　　║　　　　　║　　　　　║　　　　　║　　　　　║　　　　　║　　　　　║　　　　　║　　　　　║　　　　　║　　　　　║");
//			this.print(0, 1, "　首購－催收");
//			this.print(1, 1, "║　　　　　║　　　　　║　　　　　║　　　　　║　　　　　║　　　　　║　　　　　║　　　　　║　　　　　║　　　　　║　　　　　║　　　　　║　　　　　║");
//
//			// 查DB (dataType = 4 : 首購-催收)
//			listResult = lM002ServiceImpl.doQuery(tmpYear, titaVo);
//
//			// 倒出每月資料
//			for (int tmpMonth : monthList) {
//				// 取值
//				BigDecimal tmpMonthAmt = checkResult(listResult, tmpMonth);
//
//				// 印值
//				this.print(0, 25 + (12 * (tmpMonth - 1)), df1.format(tmpMonthAmt), "R");
//
//				// 月合計-運算過程
//				monthTotal.put(tmpMonth, caculator(monthTotal, tmpMonth, tmpMonthAmt));
//			}
//
//			this.print(1, 1, "╠═════╬═════╬═════╬═════╬═════╬═════╬═════╬═════╬═════╬═════╬═════╬═════╬═════╣");
//			this.print(1, 1, "║　　　　　║　　　　　║　　　　　║　　　　　║　　　　　║　　　　　║　　　　　║　　　　　║　　　　　║　　　　　║　　　　　║　　　　　║　　　　　║");
//			this.print(0, 1, "　合計");
//			this.print(1, 1, "║　　　　　║　　　　　║　　　　　║　　　　　║　　　　　║　　　　　║　　　　　║　　　　　║　　　　　║　　　　　║　　　　　║　　　　　║　　　　　║");
//
//			// 月合計-輸出
//			for (int tmpMonth : monthList) {
//				BigDecimal tmpMonthAmt = monthTotal.get(tmpMonth);
//				this.print(0, 25 + (12 * (tmpMonth - 1)), df1.format(tmpMonthAmt), "R");
//			}
//
//			this.print(1, 1, "╚═════╩═════╩═════╩═════╩═════╩═════╩═════╩═════╩═════╩═════╩═════╩═════╩═════╝");
//
//			this.print(1, 1, " ");
//		}
//
//		// 關閉報表
//		long sno = this.close();
//		this.toPdf(sno);
//	}

}
