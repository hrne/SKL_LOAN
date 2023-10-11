package com.st1.itx.trade.LM;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.db.service.springjpa.cm.LM082ServiceImpl;
import com.st1.itx.util.common.MakeExcel;
import com.st1.itx.util.common.MakeReport;
import com.st1.itx.util.common.data.ReportVo;
import com.st1.itx.util.parse.Parse;

@Component
@Scope("prototype")

public class LM082Report extends MakeReport {

	@Autowired
	LM082ServiceImpl lM082ServiceImpl;

	@Autowired
	MakeExcel makeExcel;
	
	@Autowired
	Parse parse;
	
	private int limitAcDate = 20211217;

	public boolean exec(TitaVo titaVo) throws LogicException {
		this.info("LM082Report exec start ...");

		int iAcDate = titaVo.getEntDyI() + 19110000;
		this.info("LM082Report exec AcDate = " + iAcDate);

		List<Map<String, String>> lLM077List = new ArrayList<Map<String, String>>();

		try {
			lLM077List = lM082ServiceImpl.findAll(titaVo, iAcDate / 100);
		} catch (Exception e) {
			StringWriter errors = new StringWriter();
			e.printStackTrace(new PrintWriter(errors));
			this.error("LM082ServiceImpl.findAll error = " + errors.toString());
		}

		exportExcel(titaVo, lLM077List, iAcDate);

		return true;

	}

	private void exportExcel(TitaVo titaVo, List<Map<String, String>> list, int date) throws LogicException {
		this.info("LM082Report exportExcel");
		int entdy = date - 19110000; // expects date to be in BC Date format.
		String YearMonth = entdy / 10000 + " 年 " + String.format("%02d", entdy / 100 % 100) + " 月";

		int reportDate = titaVo.getEntDyI() + 19110000;
		String brno = titaVo.getBrno();
		String txcd = "LM082";
		String fileItem = "B048金融機構承作「自然人購置高價住宅貸款」統計表(110.3.19(含)起辦理案件)";
		String fileName = "LM082_B048金融機構承作「自然人購置高價住宅貸款」統計表(110.3.19(含)起辦理案件)" + showRocDate(entdy, 0).substring(0, 7);
		String defaultExcel = "LM082_底稿_B048金融機構承作「自然人購置高價住宅貸款」統計表(110.3.19(含)起辦理案件).xlsx";
		String defaultSheet = "FOA";

		ReportVo reportVo = ReportVo.builder().setRptDate(reportDate).setBrno(brno).setRptCode(txcd)
				.setRptItem(fileItem).build();
		// 開啟報表
		makeExcel.open(titaVo, reportVo, fileName, defaultExcel, defaultSheet);

		// 資料期間 G2
		makeExcel.setValue(2, 7, "新光人壽保險股份有限公司", "C");
		// 民國年月G3
		makeExcel.setValue(3, 7, "民國 " + YearMonth, "C");

		if (list == null) {
			makeExcel.setValue(4, 1, "本月無資料");

		} else {

			int row = 0;
			int col = 0;
			BigDecimal amt = BigDecimal.ZERO;
			BigDecimal wLTV = BigDecimal.ZERO;
			BigDecimal wRate = BigDecimal.ZERO;
			int count = 0;

			for (Map<String, String> r : list) {

				row = 6 + parse.stringToInteger(r.get("CitySeq"));
				// 3 or 7 or 11 待B047完整在判斷
				col = parse.stringToInteger(r.get("ApplDate")) >= limitAcDate ? 11 : 3;

				count = parse.stringToInteger(r.get("Count"));
				amt = getBigDecimal(r.get("DrawdownAmt"));
				wLTV = getBigDecimal(r.get("wAvgLTV"));
				wRate = getBigDecimal(r.get("wAvgRate"));

				makeExcel.setValue(row, col, count, "0", "R");
				makeExcel.setValue(row, col + 1, amt, "0.00", "R");
				makeExcel.setValue(row, col + 2, wLTV, "0.00", "R");
				makeExcel.setValue(row, col + 3, wRate, "0.00", "R");

			}

			// 處理此日期範圍的全國合計
			for (int i = 3; i <= 14; i++) {
				makeExcel.formulaCaculate(15, i);
			}
		}

		makeExcel.close();

	}
}
