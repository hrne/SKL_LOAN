package com.st1.itx.trade.LM;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.db.service.springjpa.cm.LM087ServiceImpl;
import com.st1.itx.util.common.MakeExcel;
import com.st1.itx.util.common.MakeReport;
import com.st1.itx.util.common.data.ReportVo;
import com.st1.itx.util.parse.Parse;

@Component
@Scope("prototype")
public class LM087Report extends MakeReport {

	@Autowired
	LM087ServiceImpl lM087ServiceImpl;

	@Autowired
	MakeExcel makeExcel;

	@Autowired
	Parse parse;

	private List<Map<String, String>> lLM087List1 = new ArrayList<Map<String, String>>();
	private List<Map<String, String>> lLM087List2 = new ArrayList<Map<String, String>>();
	private List<Map<String, String>> lLM087List3 = new ArrayList<Map<String, String>>();
	private List<Map<String, String>> lLM087List4 = new ArrayList<Map<String, String>>();
	private List<Map<String, String>> lLM087List5 = new ArrayList<Map<String, String>>();
	private List<Map<String, String>> lLM087List6 = new ArrayList<Map<String, String>>();

	public boolean exec(TitaVo titaVo) throws LogicException {
		this.info("LM087Report exec ...");

		int iAcDate = titaVo.getEntDyI() + 19110000;
		int iYearMonth = iAcDate / 100;

		try {
			// 保險業利害關係人放款管理辦法第3條
			lLM087List1 = lM087ServiceImpl.findData1(iYearMonth, titaVo);
			// 金控法第44條
			lLM087List2 = lM087ServiceImpl.findData2(iYearMonth, titaVo);
			// 保險業對同一人同一關係人或同一關係企業之放款及其他交易管理辦法第2條
			lLM087List3 = lM087ServiceImpl.findData3(iYearMonth, titaVo);
			// 放款逾放比、總逾放比、逾放金額、放款總額
			lLM087List4 = lM087ServiceImpl.findData4(iYearMonth, titaVo);
			// 淨值和可運用資金
			lLM087List5 = lM087ServiceImpl.fnEquity(iYearMonth, titaVo);
			// RBC放款總餘額和風險量
			lLM087List6 = lM087ServiceImpl.findRBC(iYearMonth, titaVo);

		} catch (Exception e) {
			StringWriter errors = new StringWriter();
			e.printStackTrace(new PrintWriter(errors));
			this.error("LM087ServiceImpl.findAll error = " + errors.toString());
		}

		exportExcel(iAcDate, titaVo);

		return true;

	}

	private void exportExcel(int iAcDate, TitaVo titaVo) throws LogicException {
		this.info("LM087Report exportExcel");

		String rocDate = this.showRocDate(iAcDate, 6);
		String rocYMch = this.showRocDate(iAcDate, 5);
		String rocYYYMM = ((iAcDate - 19110000) / 100) + "";
		int reportDate = titaVo.getEntDyI() + 19110000;
		String brno = titaVo.getBrno();
		String txcd = "LM087";
		String fileItem = "放款內部控制月報表" + rocYYYMM;
		String fileName = "LM087_8-1放款內部控制月報表" + rocYYYMM;
		String defaultExcel = "LM087_底稿_8-1 放款內部控制月報表.xlsx";
		String defaultSheet = "YYY.MM";
		String reNameSheet = rocDate.substring(0, rocDate.length() - 3);

		ReportVo reportVo = ReportVo.builder().setRptDate(reportDate).setBrno(brno).setRptCode(txcd)
				.setRptItem(fileItem).build();
		// 開啟報表
		makeExcel.open(titaVo, reportVo, fileName, defaultExcel, defaultSheet);
		makeExcel.setSheet(defaultSheet, reNameSheet);

		makeExcel.setValue(1, 1, "放款" + rocYMch + "內部控制月報表", "L");

		makeExcel.setValue(3, 5, "戶名", "C");
		makeExcel.setValue(3, 6, "戶號", "C");
		makeExcel.setValue(3, 7, "ID", "C");
		makeExcel.setValue(3, 8, "餘額", "C");

		if (lLM087List1.size() == 0 && lLM087List2.size() == 0 && lLM087List3.size() == 0 && lLM087List4.size() == 0
				&& lLM087List5.size() == 0 && lLM087List6.size() == 0) {
			makeExcel.setValue(5, 7, "本日無資料");
		} else {
			String custName = "";
			BigDecimal loan = BigDecimal.ZERO;
			int row = 0;
			// 保險業利害關係人放款管理辦法第3條
			if (lLM087List1.size() > 0) {
				for (Map<String, String> r : lLM087List1) {
					custName = r.get("CustName");
					loan = getBigDecimal(r.get("LoanBal"));

					switch (r.get("Group")) {
					case "0":
						row = 5;
						break;
					case "1":
						row = 6;
						break;
					case "2":
						row = 7;
						break;
					case "9":
						row = 8;
						break;
					default:
						break;
					}
					makeExcel.setValue(row, 7, custName, "C");
					makeExcel.setValue(row, 8, loan, "#,##0", "R");
				}

			}
			// 金控法第44條
			if (lLM087List2.size() > 0) {

				for (Map<String, String> r : lLM087List2) {
					custName = r.get("CustName");
					loan = getBigDecimal(r.get("LoanBal"));

					switch (r.get("Group")) {
					case "0":
						row = 10;
						break;
					case "1":
						row = 11;
						break;
					case "9":
						row = 12;
						break;
					default:
						break;
					}
					makeExcel.setValue(row, 7, custName, "C");
					makeExcel.setValue(row, 8, loan, "#,##0", "R");
				}
			}
			// 保險業對同一人同一關係人或同一關係企業之放款及其他交易管理辦法第2條
			if (lLM087List3.size() > 0) {

				for (Map<String, String> r : lLM087List3) {
					custName = r.get("CustName");
					loan = getBigDecimal(r.get("LoanBal"));

					switch (r.get("Group")) {
					case "0":
						row = 14;
						break;
					case "1":
						row = 15;
						break;
					case "2":
						row = 16;
						break;
					case "3":
						row = 17;
						break;
					case "4":
						row = 18;
						break;
					default:
						break;
					}
					makeExcel.setValue(row, 7, custName, "C");
					makeExcel.setValue(row, 8, loan, "#,##0", "R");
				}
			}

			// 來自逾期月報表數值
			if (lLM087List4.size() > 0) {
				BigDecimal loanBal = BigDecimal.ZERO;
				BigDecimal ovduLoanBal = BigDecimal.ZERO;
				BigDecimal totalLoanBal = BigDecimal.ZERO;
				BigDecimal ovduTotalRate = BigDecimal.ZERO;
				BigDecimal ovduRate = BigDecimal.ZERO;

				// 只會有一筆
				Map<String, String> r = lLM087List4.get(0);

				loanBal = getBigDecimal(r.get("LoanBal"));
				ovduLoanBal = getBigDecimal(r.get("OvduLoanBal"));
				totalLoanBal = getBigDecimal(r.get("TotalLoanBal"));
				ovduRate = getBigDecimal(r.get("OvduRate"));
				ovduTotalRate = getBigDecimal(r.get("OvduTotalRate"));
				// 放款總餘額(H21)
				makeExcel.setValue(21, 8, loanBal, "#,##0", "R");
				// 逾放總額(L24)
				makeExcel.setValue(24, 12, ovduLoanBal, "#,##0", "C");
				// 放款總額含保貸(L25)
				makeExcel.setValue(25, 12, totalLoanBal, "#,##0", "C");
				// 放款逾放比(G24)
				makeExcel.setValue(24, 7, ovduRate, "0.00%", "C");
				// 總逾放比(G25)
				makeExcel.setValue(24, 8, ovduTotalRate, "0.00%", "C");

			}
			// 淨值和可運用資金
			if (lLM087List5.size() > 0) {
				BigDecimal availableFunds = BigDecimal.ZERO;
				BigDecimal stockHoldersEqt = BigDecimal.ZERO;
				String lacdate = "";
				// 只會有一筆
				Map<String, String> r = lLM087List5.get(0);

				availableFunds = getBigDecimal(r.get("AvailableFunds"));
				stockHoldersEqt = getBigDecimal(r.get("StockHoldersEqt"));
				lacdate = this.showRocDate(parse.stringToInteger(r.get("AcDate")), 6);

				// 淨值 日期(A23)
				// 可運用資金 日期(A24)
				// 放款總餘額 日期(A25)
				makeExcel.setValue(23, 1, lacdate, "C");
				makeExcel.setValue(24, 1, lacdate, "C");
				makeExcel.setValue(25, 1, rocDate, "C");

				// 淨值(D23)
				makeExcel.setValue(23, 4, stockHoldersEqt, "#,##0", "R");
				// 可運用資金(D24)
				makeExcel.setValue(24, 4, availableFunds, "#,##0", "R");

			}

			// 淨值和可運用資金
			if (lLM087List6.size() > 0) {
				BigDecimal loanbal = BigDecimal.ZERO;
				BigDecimal riskAmount = BigDecimal.ZERO;
				// 撈出來的年月
				int yymm = 0;
				int yymmdd = 0;
				// 現在年月
				int nowYM = reportDate / 100;
				int col = 0;

				for (Map<String, String> r : lLM087List6) {
					yymmdd = parse.stringToInteger(r.get("YearMonth"));
					yymm = yymmdd / 100;
					loanbal = getBigDecimal(r.get("LoanBal"));
					riskAmount = getBigDecimal(r.get("RiskFactorAmount"));
					if (nowYM - yymm == 0) {
						// 當月
						col = 10;
					} else if (nowYM - yymm == 1 || nowYM - yymm == 89) {
						// 上個月 202112-202111 = 1 或 202201 - 202112
						col = 9;
					} else {
						// 去年12月
						col = 8;
					}

					makeExcel.setValue(27, col, this.showRocDate(yymmdd, 6), "C");
					makeExcel.setValue(28, col, loanbal, "#,##0", "C");
					makeExcel.setValue(29, col, riskAmount, "#,##0", "C");

				}

			}
			for (int i = 5; i <= 21; i++) {
				makeExcel.formulaCaculate(i, 5);
				makeExcel.formulaCaculate(i, 9);
			}
			// 單一借戶名稱 G20
			makeExcel.formulaCaculate(20, 7);
			// 放款總餘額數字 D25
			makeExcel.formulaCaculate(25, 4);

			// C28~D29
			makeExcel.formulaCaculate(28, 3);
			makeExcel.formulaCaculate(28, 4);
			makeExcel.formulaCaculate(29, 3);
			makeExcel.formulaCaculate(29, 4);

			// M28,M29
			makeExcel.formulaCaculate(28, 13);
			makeExcel.formulaCaculate(29, 13);
			
			makeExcel.setHeight(23, 30);
			makeExcel.setHeight(24, 30);
			makeExcel.setHeight(25, 30);
			
		}

		makeExcel.close();
	}

}
