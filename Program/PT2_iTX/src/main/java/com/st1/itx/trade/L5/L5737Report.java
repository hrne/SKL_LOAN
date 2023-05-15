package com.st1.itx.trade.L5;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.db.service.springjpa.cm.L5737ServiceImpl;
import com.st1.itx.util.common.MakeExcel;
import com.st1.itx.util.common.MakeReport;
import com.st1.itx.util.common.data.ReportVo;
import com.st1.itx.util.parse.Parse;

/**
 * L5737 催收戶餘額明細
 * 
 * @author ST1-ChihWei
 * @version 1.0.0
 */
@Component
@Scope("prototype")
public class L5737Report extends MakeReport {

	@Autowired
	L5737ServiceImpl l5737ServiceImpl;

	@Autowired
	Parse parse;

	@Autowired
	MakeExcel makeExcel;

	String txCD = "L5737";
	String txName = "催收戶餘額明細";

	boolean isFinished = true;

	public boolean exec(TitaVo titaVo) throws LogicException {
		this.info("exec ... ");

		// 取得輸入值
		int inputDrawdownDate = parse.stringToInteger(titaVo.getParam("DrawdownDate"));

		List<Map<String, String>> resultList = l5737ServiceImpl.getOverdueCustomerLoanData(inputDrawdownDate,
				titaVo);

		// open excel
		
		int reportDate = titaVo.getEntDyI() + 19110000;
		String brno = titaVo.getBrno();
		String txcd = this.txCD;
		String fileItem = this.txName;
		String fileName = txcd + "_" + fileItem;
		String defaultExcel = "L5737_底稿_催收戶餘額明細.xlsx";
		String defaultSheet = "yyymmdd";

		ReportVo reportVo = ReportVo.builder().setRptDate(reportDate).setBrno(brno).setRptCode(txcd)
				.setRptItem(fileItem).build();

		// 開啟報表
		makeExcel.open(titaVo, reportVo, fileName, defaultExcel, defaultSheet);

		makeExcel.setSheet(defaultSheet, titaVo.getParam("DrawdownDate"));

		int row = 2;

		if (resultList == null || resultList.isEmpty()) {
			makeExcel.setValue(row, 1, "本日無資料", "R");
			isFinished = false;
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
				makeExcel.setValue(row, 15, parse.stringToBigDecimal(result.get("LoanRatio")), "0.00%");
				row++;
			}
		}

		makeExcel.close();

		return isFinished;
	}

}
