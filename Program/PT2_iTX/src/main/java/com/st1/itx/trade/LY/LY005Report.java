package com.st1.itx.trade.LY;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.db.service.InnFundAplService;
import com.st1.itx.db.service.springjpa.cm.LY005ServiceImpl;
import com.st1.itx.db.service.springjpa.cm.LY007ServiceImpl;
import com.st1.itx.util.common.MakeExcel;
import com.st1.itx.util.common.MakeReport;
import com.st1.itx.util.common.data.ReportVo;
import com.st1.itx.util.parse.Parse;

@Component
@Scope("prototype")
public class LY005Report extends MakeReport {

	@Autowired
	LY005ServiceImpl lY005ServiceImpl;

	@Autowired
	LY007ServiceImpl lY007ServiceImpl;

	@Autowired
	InnFundAplService sInnFundAplService;

	@Autowired
	Parse parse;

	@Autowired
	MakeExcel makeExcel;

	private BigDecimal totalEquity = BigDecimal.ZERO;
	private String equityDataMonthOutput = "";

	public boolean exec(TitaVo titaVo) throws LogicException {

		int inputYearMonth = (Integer.valueOf(titaVo.getParam("RocYear")) + 1911) * 100 + 12;

		List<Map<String, String>> findStockHoldersEqt = null;

		try {
			// 查核數(可運用資金中的股東權益)
			findStockHoldersEqt = lY007ServiceImpl.findStockHoldersEqt(inputYearMonth, titaVo);

		} catch (Exception e) {

			StringWriter errors = new StringWriter();
			e.printStackTrace(new PrintWriter(errors));
			this.info("LY007erviceImpl.findStockHoldersEqt error = " + errors.toString());
		}

		if (findStockHoldersEqt != null) {

			int equityDataMonth = (parse.stringToInteger(findStockHoldersEqt.get(0).get("AcDate")) / 100) - 191100;
			totalEquity = parse.stringToBigDecimal(findStockHoldersEqt.get(0).get("StockHoldersEqt"));
			equityDataMonthOutput = String.valueOf(equityDataMonth);
			equityDataMonthOutput = equityDataMonthOutput.substring(0, 3) + "." + equityDataMonthOutput.substring(3)
					+ ".31";
		}
		this.info("equityDataMonthOutput = " + equityDataMonthOutput);

		List<Map<String, String>> lY005List = null;

		try {
			lY005List = lY005ServiceImpl.queryDetail(inputYearMonth, titaVo);
		} catch (Exception e) {
			StringWriter errors = new StringWriter();
			e.printStackTrace(new PrintWriter(errors));
			this.error("LY005ServiceImpl.findAll error = " + errors.toString());
		}

		int reportDate = titaVo.getEntDyI() + 19110000;
		String brno = titaVo.getBrno();
		String txcd = "LY005";
		String fileItem = "非RBC_表20_會計部年度檢查報表";
		String fileName = "LY005_非RBC_表20_會計部年度檢查報表_" + showRocDate(inputYearMonth * 100 + 31, 6);
		String defaultExcel = "LY005_底稿_非RBC_表20_會計部年度檢查報表.xlsx";
		String defaultSheet = "YYY.MM";

		this.info("reportVo open");

		ReportVo reportVo = ReportVo.builder().setRptDate(reportDate).setBrno(brno).setRptCode(txcd)
				.setRptItem(fileItem).build();
		// 開啟報表
		makeExcel.open(titaVo, reportVo, fileName, defaultExcel, defaultSheet);
		// makeExcel.open(titaVo, titaVo.getEntDyI(), titaVo.getKinbr(), "LY005",
		// "非RBC_表20_會計部年度檢查報表", "LY005_非RBC_表20_會計部年度檢查報表",
		// "LY005_底稿_非RBC_表20_會計部年度檢查報表.xlsx", "YYY.MM");

		String entdy = String.valueOf(inputYearMonth - 191100);

		String year = entdy.substring(0, 3);
		String month = entdy.substring(3, 5);

		makeExcel.setSheet("YYY.MM", year + "." + month);

		// 通用處理
		// 設定表中顯示的日期
		makeExcel.setValue(1, 2, "新光人壽股份有限公司 " + titaVo.getParam("RocYear") + "年度(季)報表");

		makeExcel.setValue(1, 16, showRocDate(inputYearMonth * 100 + 31, 6)); // 資料日期

		int iYear = Integer.valueOf(titaVo.getParam("RocYear"));
		if (lY005List != null && !lY005List.isEmpty()) {

			int rowCursor = 5; // 列指標

			makeExcel.setShiftRow(6, lY005List.size() - 2);

			BigDecimal txAmtTotal = BigDecimal.ZERO;

			// 列號
			int seq = 1;

			for (Map<String, String> tLDVo : lY005List) {

				makeExcel.setValue(rowCursor, 2, seq, "C"); // 列號

				BigDecimal loanBal = getBigDecimal(tLDVo.get("LoanBal"));
				BigDecimal gPercent = this.computeDivide(loanBal, totalEquity, 4);
				makeExcel.setValue(rowCursor, 3, tLDVo.get("Rel"));// 與本公司之關係
				makeExcel.setValue(rowCursor, 4, tLDVo.get("CustNo"));// 交易對象代號
				makeExcel.setValue(rowCursor, 5, tLDVo.get("CustName"));// 交易對象名稱
				makeExcel.setValue(rowCursor, 6, tLDVo.get("F3"));// 交易種類
				makeExcel.setValue(rowCursor, 7, tLDVo.get("F4"));// 交易型態
				makeExcel.setValue(rowCursor, 8, tLDVo.get("BdLocation"));// 交易標的內容
				makeExcel.setValue(rowCursor, 9, tLDVo.get("DrawdownDate"));// 交易日期
				makeExcel.setValue(rowCursor, 10, loanBal);// 交易金額
				makeExcel.setValue(rowCursor, 11, tLDVo.get("F8"));// 最近交易日之參考市價
				makeExcel.setValue(rowCursor, 12, tLDVo.get("F9"));// 已實現損益
				makeExcel.setValue(rowCursor, 13, tLDVo.get("F10"));// 未實現損益
				makeExcel.setValue(rowCursor, 14, gPercent, "C");// 交易金額占業主權益比率%
				makeExcel.setValue(rowCursor, 15, tLDVo.get("Supervisor"), "C");// 最後決定權人員
				makeExcel.setValue(rowCursor, 16, tLDVo.get("F13"));// 備註

				rowCursor++;
				seq++;
			}

			// 交易金額 total 輸出
			makeExcel.setValue(rowCursor, 10, txAmtTotal, "#,##0");

			makeExcel.setValue(2, 17, iYear + ".12.31", "C"); // 核閱數資料日期

			makeExcel.setValue(3, 17, formatAmt(totalEquity, 0), "C");// 核閱數

		} else {
			// 無資料時處理
			makeExcel.setValue(5, 5, "本日無資料", "L");

			makeExcel.close();

			return false;
		}

		makeExcel.close();

		return true;

	}

}
