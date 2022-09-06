package com.st1.itx.trade.L9;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.db.service.springjpa.cm.L9738ServiceImpl;
import com.st1.itx.util.common.MakeExcel;
import com.st1.itx.util.common.MakeReport;
import com.st1.itx.util.parse.Parse;

/**
 * L9738 評估淨值明細
 * 
 * @author ST1-ChihWei
 * @version 1.0.0
 */
@Component
@Scope("prototype")
public class L9738Report extends MakeReport {

	@Autowired
	L9738ServiceImpl l9738ServiceImpl;

	@Autowired
	Parse parse;

	@Autowired
	MakeExcel makeExcel;

	String txCD = "L9738";
	String txName = "評估淨值明細";

	boolean isFinished = true;

	public boolean exec(TitaVo titaVo) throws LogicException {
		this.info("exec ... ");

		// 取得輸入值
		int inputDrawdownDate = parse.stringToInteger(titaVo.getParam("DrawdownDate"));

		List<Map<String, String>> resultList = l9738ServiceImpl.getEvaAmtData(inputDrawdownDate, titaVo);

		// open excel
		makeExcel.open(titaVo, titaVo.getEntDyI(), titaVo.getKinbr(), txCD, txName, txCD + "_" + txName, "L9738_底稿_評估淨值明細.xlsx", "yyymmdd", titaVo.getParam("DrawdownDate"));

		int row = 2;

		if (resultList == null || resultList.isEmpty()) {
			makeExcel.setValue(row, 1, "本日無資料", "R");
			isFinished = false;
		} else {
			for (Map<String, String> result : resultList) {

				makeExcel.setValue(row, 1, parse.stringToInteger(result.get("CustNo")));
				makeExcel.setValue(row, 2, parse.stringToInteger(result.get("FacmNo")));
				makeExcel.setValue(row, 3, parse.stringToBigDecimal(result.get("LineAmt")), "#,###");
				makeExcel.setValue(row, 4, parse.stringToInteger(result.get("UsageCode")));
				makeExcel.setValue(row, 5, parse.stringToInteger(result.get("ClCode1")));
				makeExcel.setValue(row, 6, parse.stringToInteger(result.get("ClCode2")));
				makeExcel.setValue(row, 7, parse.stringToInteger(result.get("ClNo")));
				makeExcel.setValue(row, 8, result.get("MainFlag"), "L");
				makeExcel.setValue(row, 9, parse.stringToBigDecimal(result.get("EvaNetWorth")), "#,###");
				makeExcel.setValue(row, 10, this.showBcDate(result.get("EvaDate"), 0), "C");
				makeExcel.setValue(row, 11, result.get("Location"), "L");
				makeExcel.setValue(row, 12, result.get("BdNoLdNo"), "L");

				row++;
			}
		}

		long sno = makeExcel.close();

		makeExcel.toExcel(sno);

		return isFinished;
	}

}
