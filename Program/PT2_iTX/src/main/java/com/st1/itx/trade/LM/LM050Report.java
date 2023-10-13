package com.st1.itx.trade.LM;

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
import com.st1.itx.db.service.springjpa.cm.LM050ServiceImpl;

import com.st1.itx.util.common.MakeExcel;
import com.st1.itx.util.common.MakeReport;
import com.st1.itx.util.common.data.ReportVo;

@Component
@Scope("prototype")

public class LM050Report extends MakeReport {

	@Autowired
	LM050ServiceImpl lM050ServiceImpl;

	@Autowired
	MakeExcel makeExcel;

	// 淨值
	BigDecimal equity = BigDecimal.ZERO;

	// 明細加總
	BigDecimal detailTotal = BigDecimal.ZERO;

	// 職員放款總額
	BigDecimal empLoanBal = BigDecimal.ZERO;

	// 關係人放款總額
	BigDecimal relLoanBal = BigDecimal.ZERO;

	// 一般客戶合計
	BigDecimal custLoanBal = BigDecimal.ZERO;

	// 放款總額
	BigDecimal total = BigDecimal.ZERO;

	private List<Map<String, String>> lM050list1 = null;
	private List<Map<String, String>> lM050list2 = null;
	private List<Map<String, String>> lM050list3 = null;
	private List<Map<String, String>> lM050list4 = null;

	public void exec(TitaVo titaVo) throws LogicException {
		this.info("LM050Report exec");

		int entdyBc = titaVo.getEntDyI() + 19110000;

		String brno = titaVo.getBrno();
		String txcd = "LM050";
		String fileItem = "放款保險法第3條利害關係人放款餘額表_限額控管";
		String fileName = "LM050放款保險法第3條利害關係人放款餘額表_限額控管";
		String defaultExcel = "LM050_底稿_放款保險法第3條利害關係人放款餘額表_限額控管.xlsx";
		String defaultSheet = "108.04";

		ReportVo reportVo = ReportVo.builder().setRptDate(entdyBc).setBrno(brno).setRptCode(txcd).setRptItem(fileItem)
				.build();

		// 開啟報表
		makeExcel.open(titaVo, reportVo, fileName, defaultExcel, defaultSheet);

		// 取得民國年帳務日
		String entdy = titaVo.getEntDy();

		List<Map<String, String>> equityList = null;

		try {

			equityList = lM050ServiceImpl.fnEquity(entdyBc, titaVo);
		} catch (Exception e) {
			StringWriter errors = new StringWriter();
			e.printStackTrace(new PrintWriter(errors));
			this.error("LM050ServiceImpl.fnEquity error = " + errors.toString());
		}

		makeExcel.setSheet("108.04", entdy.substring(1, 4) + "." + entdy.substring(4, 6));
		makeExcel.setValue(1, 2, entdy.substring(1, 4) + "年" + entdy.substring(4, 6) + "月" + entdy.substring(6, 8)
				+ "日依「保險業利害關係人放款管理辦法」第3條利害關係人放款餘額表");

		if (equityList != null && equityList.size() > 0) {

			String value = equityList.get(0).get("StockHoldersEqt");

			BigDecimal amt = getBigDecimal(value);
			String acDate = String.valueOf(Integer.valueOf(equityList.get(0).get("AcDate")) - 19110000);

			equity = amt;

			makeExcel.setValue(2, 4,
					acDate.substring(0, 3) + "." + acDate.substring(3, 5) + "." + acDate.substring(5, 7) + " 淨值（核閱數）");

			makeExcel.setValue(2, 6, formatThousand(amt), "#,##0");
		}

		// 找資料
		findData(entdyBc, titaVo);
		// 輸出檔案
		exportExcel(entdyBc, titaVo);
		makeExcel.close();

	}

	private void findData(int entdyBc, TitaVo titaVo) throws LogicException {

		try {
			// 負責人明細
			lM050list1 = lM050ServiceImpl.findAll(entdyBc, "1", titaVo);
			// 職員放款總額
			lM050list2 = lM050ServiceImpl.findAll(entdyBc, "2", titaVo);
			// 一般客戶放款總額
			lM050list3 = lM050ServiceImpl.findAll(entdyBc, "3", titaVo);
			// 哲溢價與催收費用
			lM050list4 = lM050ServiceImpl.findAmtTotal(entdyBc, titaVo);

		} catch (Exception e) {
			StringWriter errors = new StringWriter();
			e.printStackTrace(new PrintWriter(errors));
			this.error("LM050ServiceImpl.findAll error = " + errors.toString());
		}

	}

	private void exportExcel(int entdyBc, TitaVo titaVo) throws LogicException {
		this.info("LM050Report exportExcel");

		// 明細沒有列印本日無資料
		if (lM050list1 == null) {
			makeExcel.setValue(4, 2, "本日無資料", "C");
			return;
		}

		int rowCursor = 4;

		makeExcel.setShiftRow(rowCursor + 1, lM050list1.size());

		makeExcel.setIBU("");

		// 負責人
		for (Map<String, String> tLM050 : lM050list1) {

			BigDecimal loanBal = getBigDecimal(tLM050.get("LoanBal"));

			String custNo = tLM050.get("CustNo");
			String custName = tLM050.get("CustName");
			String remark = tLM050.get("Remark");

			makeExcel.setValue(rowCursor, 2, custNo);
			makeExcel.setValue(rowCursor, 3, custName);
			makeExcel.setValue(rowCursor, 4, formatThousand(loanBal), "#,##0", "R");
			makeExcel.setValue(rowCursor, 5, this.computeDivide(loanBal, equity, 6), "##0.0000%");
			this.info("bal:" + loanBal);
			this.info("淨值:" + equity);
			this.info("占淨比值:" + this.computeDivide(loanBal, equity, 6));

			String limitPercent = "1".equals(tLM050.get("EntCode")) ? "10%" : "2%";

			makeExcel.setValue(rowCursor, 6, limitPercent); // 限額標準 法人10% 個人2%
			makeExcel.setValue(rowCursor, 7, remark); // 備註

			detailTotal = detailTotal.add(loanBal);
			rowCursor++;

		}

		// 職員
		if (lM050list2.size() > 0) {
			empLoanBal = getBigDecimal(lM050list2.get(0).get("LoanBal"));
		}

		// 一般客戶 加 折溢價與催收費用
		if (lM050list3.size() > 0 && lM050list4.size() > 0) {
			custLoanBal = getBigDecimal(lM050list3.get(0).get("LoanBal"));

			custLoanBal = custLoanBal.add(getBigDecimal(lM050list4.get(0).get("LoanBal")));
		}

		// 關係人放款總額 = 負責人 + 職員
		relLoanBal = detailTotal.add(empLoanBal);

		// 放款總額 =負責人 + 職員 + (一般客戶 + 折溢價與催收費用)
		total = empLoanBal.add(custLoanBal).add(custLoanBal);

		printTotal(rowCursor + 1);
	}

	private BigDecimal formatThousand(BigDecimal amt) {

		return this.computeDivide(amt, getBigDecimal("1000"), 3);
	}

	private void printTotal(int rowCursor) throws LogicException {

		// 合計
		makeExcel.setValue(rowCursor, 4, formatThousand(detailTotal), "#,##0");
		makeExcel.setValue(rowCursor, 5, computeDivide(detailTotal, equity, 4), "##0.00%");
		makeExcel.setValue(rowCursor, 6, "30%", "R");

		rowCursor++;

		// 職員
		makeExcel.setValue(rowCursor, 4, formatThousand(empLoanBal), "#,##0");

		rowCursor++;

		// 關係人放款總額
		makeExcel.setValue(rowCursor, 4, formatThousand(relLoanBal), "#,##0");
		makeExcel.setValue(rowCursor, 5, computeDivide(relLoanBal, equity, 4), "##0.00%");
		makeExcel.setValue(rowCursor, 6, "150%", "R");

		rowCursor++;

		// 佔總放款比
		makeExcel.setValue(rowCursor, 4, computeDivide(relLoanBal, total, 4), "##0.00%");

		rowCursor++;

		// 一般客戶合計
		makeExcel.setValue(rowCursor, 4, formatThousand(custLoanBal), "#,##0");

		rowCursor++;

		// 佔總放款比
		makeExcel.setValue(rowCursor, 4, computeDivide(custLoanBal, total, 4), "##0.00%");

		rowCursor++;

		// 放款總額
		makeExcel.setValue(rowCursor, 4, formatThousand(total), "#,##0");
	}
}
