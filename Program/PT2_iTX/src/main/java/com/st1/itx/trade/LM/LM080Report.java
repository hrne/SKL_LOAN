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
import com.st1.itx.db.service.springjpa.cm.LM080ServiceImpl;
import com.st1.itx.util.common.MakeExcel;
import com.st1.itx.util.common.MakeReport;
import com.st1.itx.util.common.data.ReportVo;
import com.st1.itx.util.parse.Parse;

@Component
@Scope("prototype")

public class LM080Report extends MakeReport {

	@Autowired
	LM080ServiceImpl lM080ServiceImpl;

	@Autowired
	MakeExcel makeExcel;
	
	@Autowired
	Parse parse;
	

	public boolean exec(TitaVo titaVo) throws LogicException {
		this.info("LM080Report exec start ...");

		int iAcDate = titaVo.getEntDyI() + 19110000;
		this.info("LM080Report exec AcDate = " + iAcDate);

		List<Map<String, String>> lLM077List = new ArrayList<Map<String, String>>();

		try {
			lLM077List = lM080ServiceImpl.findAll(titaVo, iAcDate / 100);
		} catch (Exception e) {
			StringWriter errors = new StringWriter();
			e.printStackTrace(new PrintWriter(errors));
			this.error("LM080ServiceImpl.findAll error = " + errors.toString());
		}

		exportExcel(titaVo, lLM077List, iAcDate);

		return true;

	}

	private void exportExcel(TitaVo titaVo, List<Map<String, String>> list, int date) throws LogicException {
		this.info("LM080Report exportExcel");
		int entdy = date - 19110000; // expects date to be in BC Date format.
		String YearMonth = entdy / 10000 + " 年 " + String.format("%02d", entdy / 100 % 100) + " 月";

		int reportDate = titaVo.getEntDyI() + 19110000;
		String brno = titaVo.getBrno();
		String txcd = "LM080";
		String fileItem = "B046金融機構承作「公司法人購置住宅貸款」統計表(110.3.19(含)起辦理案件)";
		String fileName = "LM080_B046金融機構承作「公司法人購置住宅貸款」統計表(110.3.19(含)起辦理案件)" + showRocDate(entdy, 0).substring(0, 7);
		String defaultExcel = "LM080_底稿_B046金融機構承作「公司法人購置住宅貸款」統計表(110.3.19(含)起辦理案件).xlsx";
		String defaultSheet = "FOA";

		ReportVo reportVo = ReportVo.builder().setRptDate(reportDate).setBrno(brno).setRptCode(txcd)
				.setRptItem(fileItem).build();
		// 開啟報表
		makeExcel.open(titaVo, reportVo, fileName, defaultExcel, defaultSheet);

		// 資料期間 D2
		makeExcel.setValue(2, 4, "新光人壽保險股份有限公司", "C");
		// 民國年月D3
		makeExcel.setValue(3, 4, "民國 " + YearMonth, "C");

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
				col = 7;

				count = parse.stringToInteger(r.get("Count"));
				amt = getBigDecimal(r.get("LineAmt"));
				wLTV = getBigDecimal(r.get("wAvgLTV"));
				wRate = getBigDecimal(r.get("wAvgRate"));

				makeExcel.setValue(row, col, count, "0", "R");
				makeExcel.setValue(row, col + 1, amt, "0.00", "R");
				makeExcel.setValue(row, col + 2, wLTV, "0.00", "R");
				makeExcel.setValue(row, col + 3, wRate, "0.00", "R");

			}

			// 處理此日期範圍的全國合計
			for (int i = 3; i <= 6; i++) {
				makeExcel.formulaCaculate(14, i);
			}
		}

		makeExcel.close();

	}
}
