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
import com.st1.itx.db.service.springjpa.cm.LM083ServiceImpl;
import com.st1.itx.util.common.MakeExcel;
import com.st1.itx.util.common.MakeReport;
import com.st1.itx.util.common.data.ReportVo;

@Component
@Scope("prototype")
public class LM083Report extends MakeReport {

	@Autowired
	LM083ServiceImpl lM083ServiceImpl;

	@Autowired
	MakeExcel makeExcel;

	String txcd = "LM083";
	String txName = "ICS放款資料";
	String sheetName = "1091231";

	String yearMonth = "";

	public boolean exec(TitaVo titaVo) throws LogicException {
		this.info(txcd + "Report exec start ...");

		int inputBCYear = Integer.parseInt(titaVo.getParam("InputYear")) + 1911;
		int inputMonth = Integer.parseInt(titaVo.getParam("InputMonth"));

		inputBCYear *= 100;
		inputBCYear += inputMonth;

		yearMonth = String.valueOf(inputBCYear);

		List<Map<String, String>> listLM083 = null;

		try {
			listLM083 = lM083ServiceImpl.findAll(yearMonth, titaVo);
		} catch (Exception e) {
			StringWriter errors = new StringWriter();
			e.printStackTrace(new PrintWriter(errors));
			this.error(txcd + "ServiceImpl.findAll error = " + errors.toString());
		}

		exportExcel(titaVo, listLM083);

		return true;

	}

	private void exportExcel(TitaVo titaVo, List<Map<String, String>> lList) throws LogicException {

		this.info(txcd + "Report exportExcel");

		int reportDate = titaVo.getEntDyI() + 19110000;
		String brno = titaVo.getBrno();
		String txcd = this.txcd;
		String fileItem = txName;
		String fileName = txcd + "_" + txName;
		String defaultExcel =  txcd + "_底稿_" + txName + ".xlsx";
		String defaultSheet = sheetName;

		ReportVo reportVo = ReportVo.builder().setRptDate(reportDate).setBrno(brno).setRptCode(txcd)
				.setRptItem(fileItem).build();

		// 開啟報表
		makeExcel.open(titaVo, reportVo, fileName, defaultExcel, defaultSheet);
		
		
//		makeExcel.open(titaVo, titaVo.getEntDyI(), titaVo.getKinbr(), 
//				txcd, txName, txcd + "_" + txName, txcd + "_底稿_" + txName + ".xlsx", sheetName);

		String inputYearMonth = titaVo.getParam("InputYear") + titaVo.getParam("InputMonth");

		makeExcel.setSheet(sheetName, inputYearMonth);

		if (lList != null && lList.size() != 0) {

			int rowCursor = 2;

			if (lList.size() > 1) {

				makeExcel.setShiftRow(rowCursor + 1, lList.size() - 1);
			}

			for (Map<String, String> tLDVo : lList) {

				// F0 戶名
				String custName = tLDVo.get("F0");
				makeExcel.setValue(rowCursor, 1, custName);

				// F1 戶號-額度
				String custNo = tLDVo.get("F1");
				makeExcel.setValue(rowCursor, 2, custNo);

				// F2 貸款類別
				String loanType = tLDVo.get("F2");
				makeExcel.setValue(rowCursor, 3, loanType);

				// F3 貸款成數
				BigDecimal loanToValue = this.getBigDecimal(tLDVo.get("F3"));
				makeExcel.setValue(rowCursor, 5, loanToValue, "#,##0.00");

				// F4 是否已遲付貸款及喪失贖回權貸款
				String isOverdue = tLDVo.get("F4");
				makeExcel.setValue(rowCursor, 6, isOverdue);

				// F5 是否已經在帳上提呆
				String isBadDebt = tLDVo.get("F5");
				makeExcel.setValue(rowCursor, 7, isBadDebt);

				// F6 是否有抵押品/擔保品
				String haveClNo = tLDVo.get("F6");
				makeExcel.setValue(rowCursor, 8, haveClNo);

				// F7 抵押品/擔保品評估之市價金額
				BigDecimal evaNetWorth = getBigDecimal(tLDVo.get("F7"));
				makeExcel.setValue(rowCursor, 9, evaNetWorth, "#,##0");

				// F8 帳上剩餘金額
				BigDecimal loanBal = this.getBigDecimal(tLDVo.get("F8"));
				makeExcel.setValue(rowCursor, 10, loanBal, "#,##0");

				// F9 撥款日期
				String drawdownDate = tLDVo.get("F9");
				makeExcel.setValue(rowCursor, 11, drawdownDate);

				// F10 到期日
				String maturityDate = tLDVo.get("F10");
				makeExcel.setValue(rowCursor, 12, maturityDate);
				
				// F11 區隔帳冊
				String acSubBookCode = tLDVo.get("F11");
				makeExcel.setValue(rowCursor, 13, acSubBookCode);

				rowCursor++;

			} // for
		} else {
			makeExcel.setValue(2, 1, yearMonth + "無資料");
		}

		long sno = makeExcel.close();
		makeExcel.toExcel(sno);
	}
}
