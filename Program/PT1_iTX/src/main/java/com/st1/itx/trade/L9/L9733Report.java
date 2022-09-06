package com.st1.itx.trade.L9;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.db.service.springjpa.cm.L9733ServiceImpl;
import com.st1.itx.util.common.MakeExcel;
import com.st1.itx.util.common.MakeReport;
import com.st1.itx.util.date.DateUtil;
import com.st1.itx.util.parse.Parse;

@Component
@Scope("prototype")
public class L9733Report extends MakeReport {

	@Autowired
	L9733ServiceImpl l9733ServiceImpl;

	@Autowired
	Parse parse;

	@Autowired
	DateUtil dateUtil;

	@Autowired
	MakeExcel makeExcel;

	String txcd = "L9733";
	String txName = "利率調整檢核-下調日為月底日";

	public boolean exec(TitaVo titaVo) throws LogicException {
		this.info(txcd + "Report exec start ...");

		List<Map<String, String>> listL9733 = null;

		int inputStartDateNext = parse.stringToInteger(titaVo.get("InputStartDateNext")) + 19110000;
		int inputEndDateNext = parse.stringToInteger(titaVo.get("InputEndDateNext")) + 19110000;

		try {
			listL9733 = l9733ServiceImpl.findAll(inputStartDateNext, inputEndDateNext, titaVo);
		} catch (Exception e) {
			StringWriter errors = new StringWriter();
			e.printStackTrace(new PrintWriter(errors));
			this.error(txcd + "ServiceImpl.findAll error = " + errors.toString());
		}

		return exportExcel(listL9733, titaVo); // 不 catch 它的 Exceptions, 讓前端接
	}

	public boolean exportExcel(List<Map<String, String>> listL9733, TitaVo titaVo) throws LogicException {

		boolean result = true;

		makeExcel.open(titaVo, titaVo.getEntDyI(), titaVo.getKinbr(), txcd, txName, txcd + "_" + txName, "L9733_底稿_利率調整檢核-下調日為月底日.xlsx", "X817");

		int row = 2;

		if (listL9733 == null || listL9733.isEmpty()) {
			makeExcel.setValue(row, 1, "本日無資料", "R");
			result = false;
		} else {
			for (Map<String, String> tL9733 : listL9733) {

				makeExcel.setValue(row, 1, tL9733.get("CustNo"), "R");
				makeExcel.setValue(row, 2, tL9733.get("FacmNo"), "R");
				makeExcel.setValue(row, 3, tL9733.get("BormNo"), "R");
				makeExcel.setValue(row, 4, this.showBcDate(tL9733.get("FirstDrawdownDate"), 0), "C");
				makeExcel.setValue(row, 5, this.showBcDate(tL9733.get("FirstAdjRateDate"), 0), "C");
				makeExcel.setValue(row, 6, this.showBcDate(tL9733.get("NextAdjRateDate"), 0), "C");
				makeExcel.setValue(row, 7, tL9733.get("RateAdjFreq"), "R");
				makeExcel.setValue(row, 8, tL9733.get("FirstDrawdownDateNEW"), "C");
				makeExcel.setValue(row, 9, tL9733.get("FirstAdjRateDateNEW"), "C");
				makeExcel.setValue(row, 10, tL9733.get("NextAdjRateDateNEW"), "C");
				makeExcel.setValue(row, 11, this.showBcDate(tL9733.get("MaturityDate"), 0), "C");

				row++;
			}
		}
//		long sno = 
		makeExcel.close();
//		makeExcel.toExcel(sno);

		return result;
	}
}
