package com.st1.itx.trade.L9;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.db.service.springjpa.cm.L9735ServiceImpl;
import com.st1.itx.util.common.MakeExcel;
import com.st1.itx.util.common.MakeReport;
import com.st1.itx.util.parse.Parse;

/**
 * L9735 建商餘額明細
 * 
 * @author ST1-ChihWei
 * @version 1.0.0
 */
@Component
@Scope("prototype")
public class L9735Report extends MakeReport {

	@Autowired
	L9735ServiceImpl l9735ServiceImpl;

	@Autowired
	Parse parse;

	@Autowired
	MakeExcel makeExcel;

	String txCD = "L9735";
	String txName = "建商餘額明細";

	boolean isFinished = true;

	public boolean exec(TitaVo titaVo) throws LogicException {
		this.info("exec ... ");

		// 取得輸入值
		int inputDrawdownDate = parse.stringToInteger(titaVo.getParam("DrawdownDate"));

		List<Map<String, String>> resultList = l9735ServiceImpl.getConstructionCompanyLoanData(inputDrawdownDate,
				titaVo);

		// open excel
		makeExcel.open(titaVo, titaVo.getEntDyI(), titaVo.getKinbr(), txCD, txName, txCD + "_" + txName,
				"L9735_底稿_建商餘額明細.xlsx", "yyymmdd", titaVo.getParam("DrawdownDate"));

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

				row++;
			}
		}

		long sno = makeExcel.close();

		makeExcel.toExcel(sno);

		return isFinished;
	}

}
