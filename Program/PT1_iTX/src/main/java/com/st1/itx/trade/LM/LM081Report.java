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
import com.st1.itx.db.service.springjpa.cm.LM081ServiceImpl;
import com.st1.itx.util.common.MakeExcel;
import com.st1.itx.util.common.MakeReport;
import com.st1.itx.util.common.data.ReportVo;
import com.st1.itx.util.parse.Parse;

@Component
@Scope("prototype")

public class LM081Report extends MakeReport {

	@Autowired
	LM081ServiceImpl lM081ServiceImpl;

	@Autowired
	MakeExcel makeExcel;
	
	@Autowired
	Parse parse;
	
	boolean hasOutputted;
	
	private static BigDecimal hundredMillion = new BigDecimal("100000000");

	public boolean exec(TitaVo titaVo) throws LogicException {
		this.info("LM081Report exec start ...");

		int iAcDate = titaVo.getEntDyI() + 19110000;
		this.info("LM081Report exec AcDate = " + iAcDate);

		List<List<Map<String, String>>> lLM081List = new ArrayList<List<Map<String, String>>>();

		try {
			lLM081List.add(lM081ServiceImpl.findAll(titaVo, iAcDate/100, 20210319, 20211216, "01"));
			lLM081List.add(lM081ServiceImpl.findAll(titaVo, iAcDate/100, 20210319, 20211216, "03"));
			lLM081List.add(lM081ServiceImpl.findAll(titaVo, iAcDate/100, 20211217, 99999999, "01", "03"));
		} catch (Exception e) {
			StringWriter errors = new StringWriter();
			e.printStackTrace(new PrintWriter(errors));
			this.error("LM081ServiceImpl.findAll error = " + errors.toString());
		}

		exportExcel(titaVo, lLM081List, iAcDate);

		return true;

	}

	private void exportExcel(TitaVo titaVo, List<List<Map<String, String>>> foundList, int date) throws LogicException {

		this.info("LM081Report exportExcel");
		int entdy = date - 19110000; // expects date to be in BC Date format.
		String YearMonth = entdy/10000 + " 年 " + String.format("%02d", entdy/100%100) + " 月";

		int reportDate = titaVo.getEntDyI() + 19110000;
		String brno = titaVo.getBrno();
		String txcd = "LM081";
		String fileItem = "B047金融機構承作「自然人購屋貸款」統計表(110.3.19(含)起辦理案件)";
		String fileName = "LM081_B047金融機構承作「自然人購屋貸款」統計表(110.3.19(含)起辦理案件)" + showRocDate(entdy, 0).substring(0, 7);
		String defaultExcel = "LM081_底稿_B047金融機構承作「自然人購屋貸款」統計表(110.3.19(含)起辦理案件).xlsx";
		String defaultSheet = "FOA";

		this.info("reportVo open");

		ReportVo reportVo = ReportVo.builder().setRptDate(reportDate).setBrno(brno).setRptCode(txcd)
				.setRptItem(fileItem).build();
		// 開啟報表
		makeExcel.open(titaVo, reportVo, fileName, defaultExcel, defaultSheet);
		
		
//		makeExcel.open(titaVo, titaVo.getEntDyI(), titaVo.getKinbr(), "LM081", "B047金融機構承作「自然人購屋貸款」統計表(110.3.19(含)起辦理案件)",
//				"LM081_B047金融機構承作「自然人購屋貸款」統計表(110.3.19(含)起辦理案件)" + showRocDate(entdy, 0).substring(0, 7), "LM081_底稿_B047金融機構承作「自然人購屋貸款」統計表(110.3.19(含)起辦理案件).xlsx", 1, "FOA");

		// 資料期間 E3
		makeExcel.setValue(3, 7, "民國 " + YearMonth, "R");

		if (foundList != null && !foundList.isEmpty()) {

			for (int i = 0; i < foundList.size(); i++)
			{
				doOutput(foundList.get(i), 3 + i*4);
			}

			for (int i = 3; i < 3 + foundList.size() * 4; i++) {
				makeExcel.formulaCaculate(15, i);
			}

		}
		
		if (!hasOutputted)
			makeExcel.setValue(4, 1, "本月無資料");
		else
			makeExcel.formulaRangeCalculate(8,15,15,41);
		
		// long sno =
		makeExcel.close();
		//makeExcel.toExcel(sno);
	}
	
	private void doOutput(List<Map<String, String>> lList, int startColumn) throws LogicException 
	{
		if (lList == null || lList.isEmpty())
			return;
		
		hasOutputted = true;
		
		for (Map<String, String> tLDVo : lList) {
			int colShift = 0;
			int rowShift = 0;

			for (int i = 0; i <= 5; i++) {

				int col = startColumn + i; // 1-based
				int row = 8; // 1-based

				// Query received will have column names in the format of F0, even if no alias
				// is set in SQL
				// notice it's 0-based for those names
				String value = tLDVo.get("F" + i);

				switch (i) {
				// if specific column needs special treatment, insert case here.
				case 0:
					colShift--; // doesn't write
					break;
				case 1:
					// CityCode: already properly converted
					rowShift = parse.stringToInteger(value) - 1;
					colShift--; // doesn't write
					break;
				case 3:
					// Newly Drawdown Amount: hundred million 10^8
					makeExcel.setValue(row + rowShift, col + colShift, getBigDecimal(value).divide(hundredMillion, 2, BigDecimal.ROUND_UP), "R");
					break;
				case 4:
					// Weighted Average of Loan: Percent
					makeExcel.setValue(row + rowShift, col + colShift, getBigDecimal(value).setScale(2, BigDecimal.ROUND_UP), "R");
					break;
				case 5:
					// Weighted Average of Loan Rate: Percent
					makeExcel.setValue(row + rowShift, col + colShift, getBigDecimal(value).setScale(2, BigDecimal.ROUND_UP), "R");
					break;
				default:
					makeExcel.setValue(row + rowShift, col + colShift, parse.isNumeric(value) ? getBigDecimal(value) : value, "R");
					break;
				}
			} // for

		} // for
	}
}
