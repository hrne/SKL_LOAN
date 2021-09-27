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

@Component
@Scope("prototype")
public class LM050Report extends MakeReport {

	@Autowired
	LM050ServiceImpl lM050ServiceImpl;

	@Autowired
	MakeExcel makeExcel;

	// 淨值
	BigDecimal equity = BigDecimal.ZERO;

	// 取千元
	BigDecimal thousand = getBigDecimal("1000");

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

		makeExcel.open(titaVo, titaVo.getEntDyI(), titaVo.getKinbr(), "LM050", "放款保險法第3條利害關係人放款餘額表_限額控管",
				"LM050放款保險法第3條利害關係人放款餘額表_限額控管", "LM050_底稿_放款保險法第3條利害關係人放款餘額表_限額控管.xlsx", "108.04");

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

		long sno = makeExcel.close();

		makeExcel.toExcel(sno);
	}

	private BigDecimal formatThousand(BigDecimal amt) {

		return this.computeDivide(amt, thousand, 3);
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

	private void exportExcel(List<Map<String, String>> listLM050) throws LogicException {
		this.info("LM050Report exportExcel");

		if (listLM050 == null || listLM050.isEmpty()) {
			makeExcel.setValue(4, 1, "本日無資料");
			return;
		}

		int rowCursor = 4;
		
		if (listLM050.size() > 1) {
			makeExcel.setShiftRow(rowCursor + 1, listLM050.size() - 1);
		}

		for (Map<String, String> tLM050 : listLM050) {
			String rptType = tLM050.get("F0");
			BigDecimal loanBal = getBigDecimal(tLM050.get("F3"));

			if (rptType.equals("1")) { // 保險業利害關係人放款管理辦法第3條利害關係人
				String custNo = tLM050.get("F1");
				String custName = tLM050.get("F2");

				makeExcel.setValue(rowCursor, 2, custNo);
				makeExcel.setValue(rowCursor, 3, custName);
				makeExcel.setValue(rowCursor, 4, formatThousand(loanBal), "#,##0");
				makeExcel.setValue(rowCursor, 5, this.computeDivide(loanBal, equity, 4), "#,##0.00%");
				makeExcel.setValue(rowCursor, 6, "2%"); // 限額標準 ???

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

		printTotal(rowCursor);

	}

	private void printTotal(int rowCursor) throws LogicException {

		makeExcel.setValue(rowCursor, 2, "合     計");
		makeExcel.setValue(rowCursor, 4, formatThousand(detailTotal), "#,##0");
		makeExcel.setValue(rowCursor, 5, computeDivide(detailTotal, equity, 4), "##0.00%");
		makeExcel.setValue(rowCursor, 6, "30%", "R");

		rowCursor++;
		makeExcel.setValue(rowCursor, 2, "職    員");
		makeExcel.setValue(rowCursor, 4, formatThousand(empLoanBal), "#,##0");

		rowCursor++;
		makeExcel.setValue(rowCursor, 2, "關 係 人 放 款 總 額");
		makeExcel.setValue(rowCursor, 4, formatThousand(relLoanBal), "#,##0");
		makeExcel.setValue(rowCursor, 5, computeDivide(relLoanBal, equity, 4), "##0.00%");
		makeExcel.setValue(rowCursor, 6, "150%", "R");

		rowCursor++;
		makeExcel.setValue(rowCursor, 2, "佔 總 放 款 比");
		makeExcel.setValue(rowCursor, 4, computeDivide(relLoanBal, total, 4), "##0.00%");

		rowCursor++;
		makeExcel.setValue(rowCursor, 2, "一 般 客 戶合計");
		makeExcel.setValue(rowCursor, 4, formatThousand(custLoanBal), "#,##0");

		rowCursor++;
		makeExcel.setValue(rowCursor, 2, "佔 總 放 款 比");
		makeExcel.setValue(rowCursor, 4, computeDivide(custLoanBal, total, 4), "##0.00%");

		rowCursor++;
		makeExcel.setValue(rowCursor, 2, "放 款 總 額  ＊");
		makeExcel.setValue(rowCursor, 4, formatThousand(total), "#,##0");

	}

}
