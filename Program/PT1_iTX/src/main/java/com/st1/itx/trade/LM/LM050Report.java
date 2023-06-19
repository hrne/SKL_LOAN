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

	public void exec(TitaVo titaVo) throws LogicException {
		this.info("LM050Report exec");
//		makeExcel.open(titaVo, titaVo.getEntDyI(), titaVo.getKinbr(),
//				"LM050", "放款保險法第3條利害關係人放款餘額表_限額控管", 
//				"LM050放款保險法第3條利害關係人放款餘額表_限額控管",
//				"LM050_底稿_放款保險法第3條利害關係人放款餘額表_限額控管.xlsx", "108.04");
		int reportDate = titaVo.getEntDyI() + 19110000;
		String brno = titaVo.getBrno();
		String txcd = "LM050";
		String fileItem = "放款保險法第3條利害關係人放款餘額表_限額控管";
		String fileName = "LM050放款保險法第3條利害關係人放款餘額表_限額控管";
		String defaultExcel = "LM050_底稿_放款保險法第3條利害關係人放款餘額表_限額控管.xlsx";
		String defaultSheet = "108.04";

		ReportVo reportVo = ReportVo.builder().setRptDate(reportDate).setBrno(brno).setRptCode(txcd)
				.setRptItem(fileItem).build();

		// 開啟報表
		makeExcel.open(titaVo, reportVo, fileName, defaultExcel, defaultSheet);

		// 取得民國年帳務日
		String entdy = titaVo.getEntDy();

		makeExcel.setSheet("108.04", entdy.substring(1, 4) + "." + entdy.substring(4, 6));
		makeExcel.setValue(1, 2, entdy.substring(1, 4) + "年" + entdy.substring(4, 6) + "月" + entdy.substring(6, 8)
				+ "日依「保險業利害關係人放款管理辦法」第3條利害關係人放款餘額表");
		makeExcel.setValue(2, 4,
				entdy.substring(1, 4) + "." + entdy.substring(4, 6) + "." + entdy.substring(6, 8) + " 淨值（核閱數）");

		List<Map<String, String>> equityList = null;

		try {

			equityList = lM050ServiceImpl.fnEquity(titaVo);
		} catch (Exception e) {
			StringWriter errors = new StringWriter();
			e.printStackTrace(new PrintWriter(errors));
			this.error("LM050ServiceImpl.fnEquity error = " + errors.toString());
		}

		if (equityList != null && equityList.size() > 0) {

			String value = equityList.get(0).get("F0");

			BigDecimal amt = getBigDecimal(value);

			equity = amt;

			makeExcel.setValue(2, 6, formatThousand(amt), "#,##0");
		}

		fnAll(titaVo);

		makeExcel.close();

	}

	private BigDecimal formatThousand(BigDecimal amt) {

		return this.computeDivide(amt, getBigDecimal("1000"), 3);
	}

	private void fnAll(TitaVo titaVo) throws LogicException {

		List<Map<String, String>> listLM050 = null;

		try {

			listLM050 = lM050ServiceImpl.findAll(titaVo);
		} catch (Exception e) {
			StringWriter errors = new StringWriter();
			e.printStackTrace(new PrintWriter(errors));
			this.error("LM050ServiceImpl.fnall error = " + errors.toString());
		}

		exportExcel(listLM050);
	}

//
	private void exportExcel(List<Map<String, String>> listLM050) throws LogicException {
		this.info("LM050Report exportExcel");

		if (listLM050 == null || listLM050.isEmpty()) {
			makeExcel.setValue(4, 2, "本日無資料", "C");
			return;
		}

		int rowCursor = 4;

		String tmpCustNo = "";

		int size = 0;

		for (Map<String, String> tLM050 : listLM050) {
			String rptType = tLM050.get("F0");
			if (rptType.equals("1")) {
				size++;
			}

		}
		makeExcel.setShiftRow(rowCursor, size - 1);

		for (Map<String, String> tLM050 : listLM050) {

			String rptType = tLM050.get("F0");
			BigDecimal loanBal = getBigDecimal(tLM050.get("F3"));

			if (rptType.equals("1")) { // 保險業利害關係人放款管理辦法第3條利害關係人

				String custNo = tLM050.get("F1");
				String custName = tLM050.get("F2");
				String remark = tLM050.get("Remark");

				if (!custNo.equals(tmpCustNo)) {
					tmpCustNo = custNo;

				} else {
					continue;
				}

				makeExcel.setValue(rowCursor, 2, custNo);
				makeExcel.setValue(rowCursor, 3, custName);
				makeExcel.setValue(rowCursor, 4, formatThousand(loanBal), "#,##0");
				makeExcel.setValue(rowCursor, 5, this.computeDivide(loanBal.multiply(new BigDecimal("100")), equity, 4),
						"#,##0.00%");
				this.info("bal:" + loanBal);
				this.info("淨值:" + equity);
				this.info("占淨比值:" + this.computeDivide(loanBal.multiply(new BigDecimal("100")), equity, 4));
				makeExcel.setValue(rowCursor, 6, "2%"); // 限額標準 ???
				makeExcel.setValue(rowCursor, 7, remark); // 備註

				detailTotal = detailTotal.add(loanBal);
				rowCursor++;

			} else if (rptType.equals("2")) { // 職員
				empLoanBal = empLoanBal.add(loanBal);
			} else if (rptType.equals("3")) { // 一般客戶
				custLoanBal = custLoanBal.add(loanBal);
			}
		}

		relLoanBal = relLoanBal.add(detailTotal);
		relLoanBal = relLoanBal.add(empLoanBal);

		total = relLoanBal.add(custLoanBal);

		printTotal(rowCursor + 2);
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
