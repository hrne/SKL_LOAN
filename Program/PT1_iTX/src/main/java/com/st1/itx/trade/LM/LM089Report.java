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
import com.st1.itx.db.service.springjpa.cm.LM089ServiceImpl;
import com.st1.itx.util.common.MakeExcel;
import com.st1.itx.util.common.MakeReport;
import com.st1.itx.util.common.data.ReportVo;
import com.st1.itx.util.parse.Parse;

@Component
@Scope("prototype")

public class LM089Report extends MakeReport {

	@Autowired
	LM089ServiceImpl LM089ServiceImpl;

	@Autowired
	MakeExcel makeExcel;

	@Autowired
	Parse parse;

	public boolean exec(TitaVo titaVo) throws LogicException {
		this.info("LM089Report exec start ...");

		int iAcDate = titaVo.getEntDyI() + 19110000;
		this.info("LM089Report exec AcDate = " + iAcDate);

		List<Map<String, String>> lLM077List = new ArrayList<Map<String, String>>();

		try {
			lLM077List = LM089ServiceImpl.findAll(titaVo, iAcDate / 100);
		} catch (Exception e) {
			StringWriter errors = new StringWriter();
			e.printStackTrace(new PrintWriter(errors));
			this.error("LM089ServiceImpl.findAll error = " + errors.toString());
		}

		exportExcel(titaVo, lLM077List, iAcDate);

		return true;

	}

	private void exportExcel(TitaVo titaVo, List<Map<String, String>> list, int date) throws LogicException {
		this.info("LM089Report exportExcel");
		int entdy = date - 19110000; // expects date to be in BC Date format.
		String YearMonth = entdy / 10000 + " 年 " + String.format("%02d", entdy / 100 % 100) + " 月";

		int reportDate = titaVo.getEntDyI() + 19110000;
		String brno = titaVo.getBrno();
		String txcd = "LM089";
		String fileItem = "B049金融機構承作「自然人特定地區第2戶購屋貸款」";
		String fileName = "LM089_B049金融機構承作「自然人特定地區第2戶購屋貸款」" + showRocDate(entdy, 0).substring(0, 7);
		String defaultExcel = "LM089_底稿_B049金融機構承作「自然人特定地區第2戶購屋貸款」.xlsx";
		String defaultSheet = "FOA";

		ReportVo reportVo = ReportVo.builder().setRptDate(reportDate).setBrno(brno).setRptCode(txcd)
				.setRptItem(fileItem).build();
		// 開啟報表
		makeExcel.open(titaVo, reportVo, fileName, defaultExcel, defaultSheet);

		// 資料期間 E2
		makeExcel.setValue(2, 5, "新光人壽保險股份有限公司", "C");
		// 民國年月E3
		makeExcel.setValue(3, 5, "民國 " + YearMonth, "C");

		if (list == null) {
			makeExcel.setValue(4, 1, "本月無資料");

		} else {

			int row = 0;
			int col = 0;
			BigDecimal amt = BigDecimal.ZERO;
			BigDecimal wLTV = BigDecimal.ZERO;
			BigDecimal maxLTV = BigDecimal.ZERO;
			BigDecimal minLTV = BigDecimal.ZERO;
			BigDecimal wRate = BigDecimal.ZERO;
			BigDecimal maxRate = BigDecimal.ZERO;
			BigDecimal minRate = BigDecimal.ZERO;
		
			int count = 0;

			for (Map<String, String> r : list) {

				row = 7 + parse.stringToInteger(r.get("CitySeq"));

				if (row == 99) {
					continue;
				}
				// 3 or 7 or 11 待B047完整在判斷
				col =  3;

				count = parse.stringToInteger(r.get("Count"));
				amt = getBigDecimal(r.get("LineAmt"));
				
				wLTV = getBigDecimal(r.get("wAvgLTV"));
				maxLTV = getBigDecimal(r.get("maxLTV"));
				minLTV = getBigDecimal(r.get("minLTV"));
				
				wRate = getBigDecimal(r.get("wAvgRate"));
				maxRate = getBigDecimal(r.get("maxRate"));
				minRate = getBigDecimal(r.get("minRate"));			

				makeExcel.setValue(row, col, count, "0", "R");
				makeExcel.setValue(row, col + 1, amt, "0.00", "R");
				makeExcel.setValue(row, col + 2, wLTV, "0.00", "R");
				makeExcel.setValue(row, col + 3, maxLTV, "0.00", "R");
				makeExcel.setValue(row, col + 4, minLTV, "0.00", "R");
				
				makeExcel.setValue(row, col + 5, wRate, "0.00", "R");
				makeExcel.setValue(row, col + 6, maxRate, "0.00", "R");
				makeExcel.setValue(row, col + 7, minRate, "0.00", "R");
		
				

			}

			// 處理此日期範圍的全國合計
			for (int i = 3; i <= 10; i++) {
				makeExcel.formulaCaculate(16, i);
			}
		}

		makeExcel.close();

	}
}
