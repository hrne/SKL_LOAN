package com.st1.itx.trade.L5;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.db.service.springjpa.cm.L5736ServiceImpl;
import com.st1.itx.util.common.MakeExcel;
import com.st1.itx.util.common.MakeReport;
import com.st1.itx.util.common.data.ReportVo;
import com.st1.itx.util.parse.Parse;

/**
 * L5736 正常戶餘額明細
 * 
 * @author ST1-ChihWei
 * @version 1.0.0
 */
@Component
@Scope("prototype")
public class L5736Report extends MakeReport {

	@Autowired
	L5736ServiceImpl l5736ServiceImpl;

	@Autowired
	Parse parse;

	@Autowired
	MakeExcel makeExcel;

	public void exec(String subTxCD, String txName, TitaVo titaVo) throws LogicException {
		this.info("exec ... ");

		int reportDate = titaVo.getEntDyI() + 19110000;
		// 取得輸入值
		int inputDrawdownDate = reportDate;


		List<Map<String, String>> resultList = new ArrayList<Map<String, String>>();
//		L5736C-首購餘額明細-催收戶
//		L5736F-催收戶餘額明細
//		L5736H-住宅貸款餘額明細-催收戶

		resultList = l5736ServiceImpl.getOverdueCustomerLoanData(inputDrawdownDate, subTxCD, titaVo);

		// open excel

		String brno = titaVo.getBrno();
		String txcd = titaVo.getTxcd();
		String fileItem = txName;
		String fileName = subTxCD + "_" + txName;
		String defaultExcel = subTxCD + "_底稿_公會報送-" + txName + ".xlsx";
		String defaultSheet = "yyymmdd";

		ReportVo reportVo = ReportVo.builder().setRptDate(reportDate).setBrno(brno).setRptCode(txcd)
				.setRptItem(fileItem).build();

		// 開啟報表
		makeExcel.open(titaVo, reportVo, fileName, defaultExcel, defaultSheet);

		makeExcel.setSheet(defaultSheet, titaVo.getEntDyI() + "");

		int row = 2;

		if (resultList == null || resultList.isEmpty()) {
			makeExcel.setValue(row, 1, "本日無資料", "R");
		} else {
			for (Map<String, String> result : resultList) {

				makeExcel.setValue(row, 1, result.get("CustName"), "L");
				makeExcel.setValue(row, 2, parse.stringToInteger(result.get("CustNo")));
				makeExcel.setValue(row, 3, parse.stringToInteger(result.get("FacmNo")));
				makeExcel.setValue(row, 4, parse.stringToInteger(result.get("BormNo")));
				makeExcel.setValue(row, 5, parse.stringToBigDecimal(result.get("LoanBalance")), "#,###");
				makeExcel.setValue(row, 6, parse.stringToBigDecimal(result.get("LineAmt")), "#,###");
				makeExcel.setValue(row, 7, this.showBcDate(result.get("PrevPayIntDate"), 0), "C");
				makeExcel.setValue(row, 8, parse.stringToInteger(result.get("ClCode1")));
				makeExcel.setValue(row, 9, parse.stringToInteger(result.get("UsageCode")));
				makeExcel.setValue(row, 10, parse.stringToInteger(result.get("AcctCode")));
				makeExcel.setValue(row, 11, result.get("ProdNo"), "L");
				makeExcel.setValue(row, 12, parse.stringToInteger(result.get("Status")));
				makeExcel.setValue(row, 13, this.showBcDate(result.get("DrawdownDate"), 0), "C");
				makeExcel.setValue(row, 14, parse.stringToBigDecimal(result.get("EvaNetWorth")), "#,##0");
				this.info("LoanRatio = " + parse.stringToBigDecimal(result.get("LoanRatio")));
				makeExcel.setValue(row, 15, parse.stringToBigDecimal(result.get("LoanRatio")), "0%");

				row++;
			}
		}

		makeExcel.close();

	}

}
