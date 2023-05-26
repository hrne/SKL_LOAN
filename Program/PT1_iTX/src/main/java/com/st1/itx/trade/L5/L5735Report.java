package com.st1.itx.trade.L5;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.db.service.springjpa.cm.L5735ServiceImpl;
import com.st1.itx.util.common.MakeExcel;
import com.st1.itx.util.common.MakeReport;
import com.st1.itx.util.common.data.ReportVo;
import com.st1.itx.util.parse.Parse;

/**
 * L5735 建商餘額明細
 * 
 * @author ST1-ChihWei
 * @version 1.0.0
 */
@Component
@Scope("prototype")
public class L5735Report extends MakeReport {

	@Autowired
	L5735ServiceImpl l5735ServiceImpl;

	@Autowired
	Parse parse;

	@Autowired
	MakeExcel makeExcel;

	String txCD = "L5735";
	String txName = "建商餘額明細";

	boolean isFinished = true;

	public boolean exec(TitaVo titaVo) throws LogicException {
		this.info("exec ... ");

		// 取得輸入值
		int inputDrawdownDate = parse.stringToInteger(titaVo.getParam("DrawdownDate"));

		List<Map<String, String>> resultList = l5735ServiceImpl.getConstructionCompanyLoanData(inputDrawdownDate,
				titaVo);

		List<Map<String, String>> resultListOnline = l5735ServiceImpl.getBankOnline(titaVo);
		this.info("resultListOnline = " + resultListOnline.get(0).get("Int"));

		List<Map<String, String>> resultListMon = l5735ServiceImpl.getBankMonth(titaVo);

		this.info("resultListMon = " + resultListMon.get(0).get("Int"));

		// open excel
		int reportDate = titaVo.getEntDyI() + 19110000;
		String brno = titaVo.getBrno();
		String txcd = this.txCD;
		String fileItem = this.txName;
		String fileName = txcd + "_" + fileItem;
		String defaultExcel = "L5735_底稿_建商餘額明細.xlsx";
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
//				this.info("LoanRatio = " + parse.stringToBigDecimal(result.get("LoanRatio")));
//				makeExcel.setValue(row, 15, parse.stringToBigDecimal(result.get("LoanRatio")), "0%");
				makeExcel.setValue(row, 15, parse.stringToBigDecimal(result.get("LoanRatio")),"0%");

//												"0".equals(result.get("EvaNetWorth")) ||  "0".equals(result.get("LineAmt")) ? BigDecimal.ZERO
//						: parse.stringToBigDecimal(result.get("LineAmt")).divide(
//								parse.stringToBigDecimal(result.get("EvaNetWorth"))).setScale(2, BigDecimal.ROUND_HALF_UP);
				// 設公式
//				makeExcel.setFormula(row, 15, ltv, "ROUND(F" + row + "/N" + row + ",2)", "0%");

				// 重新計算
//				makeExcel.formulaCaculate(row, 15);

				row++;
			}
		}

		makeExcel.close();

		return isFinished;
	}

}
