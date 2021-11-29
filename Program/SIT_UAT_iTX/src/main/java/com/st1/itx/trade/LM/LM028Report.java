package com.st1.itx.trade.LM;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Component;

import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.db.domain.MonthlyLM028;
import com.st1.itx.db.service.MonthlyLM028Service;
import com.st1.itx.util.common.MakeExcel;
import com.st1.itx.util.common.MakeReport;

@Component
@Scope("prototype")
public class LM028Report extends MakeReport {

	@Autowired
	MonthlyLM028Service sMonthlyLM028Service;

	@Autowired
	MakeExcel makeExcel;

	public void exec(TitaVo titaVo) throws LogicException {
		this.info("LM028Report exec start ...");

		int inputDataMonth = titaVo.getEntDyI() + 19110000;

		inputDataMonth = inputDataMonth / 100;

		Slice<MonthlyLM028> sMonthlyLM028 = null;

		try {
			sMonthlyLM028 = sMonthlyLM028Service.findByMonth(inputDataMonth, 0, Integer.MAX_VALUE, titaVo);
		} catch (Exception e) {
			StringWriter errors = new StringWriter();
			e.printStackTrace(new PrintWriter(errors));
			this.error("sMonthlyLM028Service findAll error = " + errors.toString());
		}

		List<MonthlyLM028> lMonthlyLM028 = sMonthlyLM028 == null ? null : sMonthlyLM028.getContent();

		exportExcel(titaVo, lMonthlyLM028);
	}

	private void exportExcel(TitaVo titaVo, List<MonthlyLM028> lMonthlyLM028) throws LogicException {
		this.info("LM028Report exportExcel start ...");

		makeExcel.open(titaVo, titaVo.getEntDyI(), titaVo.getKinbr(), "LM028", "預估現金流量", "LM028預估現金流量", "預估現金流量.xlsx",
				"DEL5");
		if (lMonthlyLM028 == null || lMonthlyLM028.isEmpty()) {
			makeExcel.setValue(2, 1, "本日無資料");
		} else {
			// 有標題列,從第2列開始塞值
			int row = 2;

			for (MonthlyLM028 tMonthlyLM028 : lMonthlyLM028) {

				makeExcel.setValue(row, 1, tMonthlyLM028.getStatus());
				makeExcel.setValue(row, 2, tMonthlyLM028.getEntCode());
				makeExcel.setValue(row, 3, tMonthlyLM028.getBranchNo());
				makeExcel.setValue(row, 4, tMonthlyLM028.getCustNo());
				makeExcel.setValue(row, 5, tMonthlyLM028.getFacmNo());
				makeExcel.setValue(row, 6, tMonthlyLM028.getBormNo());
				makeExcel.setValue(row, 7, tMonthlyLM028.getStoreRate(), "###0.0000");
				makeExcel.setValue(row, 8, tMonthlyLM028.getPayIntFreq());
				makeExcel.setValue(row, 9, tMonthlyLM028.getRepayBank());
				makeExcel.setValue(row, 10, tMonthlyLM028.getLoanTermMm());
				makeExcel.setValue(row, 11, tMonthlyLM028.getLoanTermDd());
				makeExcel.setValue(row, 12, tMonthlyLM028.getLoanBal(), "#,##0");
				makeExcel.setValue(row, 13, tMonthlyLM028.getRateCode());
				makeExcel.setValue(row, 14, tMonthlyLM028.getPostDepCode());
				makeExcel.setValue(row, 15, tMonthlyLM028.getSpecificDd());
				makeExcel.setValue(row, 16, tMonthlyLM028.getFirstRateAdjFreq());
				makeExcel.setValue(row, 17, tMonthlyLM028.getBaseRateCode());
				makeExcel.setValue(row, 18, tMonthlyLM028.getFitRate1(), "###0.0000");
				makeExcel.setValue(row, 19, tMonthlyLM028.getFitRate2(), "###0.0000");
				makeExcel.setValue(row, 20, tMonthlyLM028.getFitRate3(), "###0.0000");
				makeExcel.setValue(row, 21, tMonthlyLM028.getFitRate4(), "###0.0000");
				makeExcel.setValue(row, 22, tMonthlyLM028.getFitRate5(), "###0.0000");
				makeExcel.setValue(row, 23, tMonthlyLM028.getClCode1());
				makeExcel.setValue(row, 24, tMonthlyLM028.getClCode2());
				makeExcel.setValue(row, 25, tMonthlyLM028.getDrawdownYear());
				makeExcel.setValue(row, 26, tMonthlyLM028.getDrawdownMonth());
				makeExcel.setValue(row, 27, tMonthlyLM028.getDrawdownDay());
				makeExcel.setValue(row, 28, tMonthlyLM028.getW08Code());
				makeExcel.setValue(row, 29, tMonthlyLM028.getIsRelation());
				makeExcel.setValue(row, 30, tMonthlyLM028.getAgType1());
				makeExcel.setValue(row, 31, tMonthlyLM028.getAcctSource());

				row++;
			}
		}
		for (int i = 1; i <= 31; i++) {
			makeExcel.setWidth(i, 15);
		}

		long sno = makeExcel.close();
		makeExcel.toExcel(sno);
	}

}
