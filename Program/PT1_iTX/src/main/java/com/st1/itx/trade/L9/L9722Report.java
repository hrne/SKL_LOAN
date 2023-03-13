package com.st1.itx.trade.L9;

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
import com.st1.itx.db.service.springjpa.cm.L9722ServiceImpl;
import com.st1.itx.util.common.MakeExcel;
import com.st1.itx.util.common.MakeReport;
import com.st1.itx.util.common.data.ReportVo;

@Component
@Scope("prototype")

public class L9722Report extends MakeReport {

	@Autowired
	L9722ServiceImpl l9722ServiceImpl;

	@Autowired
	MakeExcel makeExcel;

	String txcd = "L9722";
	String txName = "ICS放款資料";
	String sheetName = "D20200911_0921";

	String yearMonth = "";

	public boolean exec(TitaVo titaVo) throws LogicException {
		this.info(txcd + "Report exec start ...");

		int inputBCYear = Integer.parseInt(titaVo.getParam("InputYear")) + 1911;
		int inputMonth = Integer.parseInt(titaVo.getParam("InputMonth"));

		inputBCYear *= 100;
		inputBCYear += inputMonth;

		yearMonth = String.valueOf(inputBCYear);

		List<Map<String, String>> lL9722 = null;

		try {
			lL9722 = l9722ServiceImpl.findAll(yearMonth, titaVo);
		} catch (Exception e) {
			StringWriter errors = new StringWriter();
			e.printStackTrace(new PrintWriter(errors));
			this.error(txcd + "ServiceImpl.findAll error = " + errors.toString());
		}

		exportExcel(titaVo, lL9722);

		return true;

	}

	private void exportExcel(TitaVo titaVo, List<Map<String, String>> lList) throws LogicException {

		this.info(txcd + "Report exportExcel");

		
		ReportVo reportVo = ReportVo.builder().setRptDate(titaVo.getEntDyI()).setBrno(titaVo.getBrno()).setRptCode(txcd)
				.setRptItem(txName).build();

		// 開啟報表
		makeExcel.open(titaVo, reportVo, txcd + "_" + txName,  txcd + "_底稿_" + txName + ".xlsx",1 );
		
		makeExcel.setSheet(sheetName);

		if (lList != null && lList.size() != 0) {

			int row = 2;

			for (Map<String, String> tLDVo : lList) {

				// F0 戶號
				String custNo = tLDVo.get("F0");
				makeExcel.setValue(row, 1, custNo);

				// F1 額度
				String facmNo = tLDVo.get("F1");
				makeExcel.setValue(row, 2, facmNo);

				// F2 企金別
				String entCode = tLDVo.get("F2");
				makeExcel.setValue(row, 3, entCode);

				// F3 通路
				String departmentCode = tLDVo.get("F3");
				makeExcel.setValue(row, 4, departmentCode);

				// F4 貸款成數
				BigDecimal loanToValue = this.getBigDecimal(tLDVo.get("F4"));
				makeExcel.setValue(row, 5, loanToValue, "#,##0.00");

				// F5 逾期期數
				String ovduTerm = tLDVo.get("F5");
				makeExcel.setValue(row, 6, ovduTerm);

				// F6 戶況
				String custStatus = tLDVo.get("F6");
				makeExcel.setValue(row, 7, custStatus);

				// F7 押品別
				String clNo = tLDVo.get("F7");
				makeExcel.setValue(row, 8, clNo);

				// F8 評估淨值
				BigDecimal evaNetWorth = this.getBigDecimal(tLDVo.get("F8"));
				makeExcel.setValue(row, 9, evaNetWorth, "#,##0");

				// F9 放款餘額
				BigDecimal loanBal = this.getBigDecimal(tLDVo.get("F9"));
				makeExcel.setValue(row, 10, loanBal, "#,##0");

				// F10 轉銷呆帳金額
				BigDecimal badDebtBal = this.getBigDecimal(tLDVo.get("F10"));
				makeExcel.setValue(row, 11, badDebtBal, "#,##0");

				// F11 撥款日期
				String drawdownDate = tLDVo.get("F11");
				makeExcel.setValue(row, 12, drawdownDate);

				// F12 到期日
				String maturityDate = tLDVo.get("F12");
				makeExcel.setValue(row, 13, maturityDate);

				row++;

			} // for
		} else {
			makeExcel.setValue(2, 1, yearMonth + "無資料");
		}

		long sno = makeExcel.close();
		makeExcel.toExcel(sno);
	}
}
