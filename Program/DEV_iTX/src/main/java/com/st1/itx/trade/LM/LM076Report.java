package com.st1.itx.trade.LM;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.db.service.springjpa.cm.LM076ServiceImpl;
import com.st1.itx.util.common.MakeExcel;
import com.st1.itx.util.common.MakeReport;
import com.st1.itx.util.parse.Parse;

@Component
@Scope("prototype")

public class LM076Report extends MakeReport {

	@Autowired
	LM076ServiceImpl lM076ServiceImpl;

	@Autowired
	Parse parse;

	@Autowired
	MakeExcel makeExcel;
	
	private static final BigDecimal billion = new BigDecimal("100000000");

	public boolean exec(TitaVo titaVo) throws LogicException {
		this.info("LM076Report exec start ...");

		int iAcDate = titaVo.getEntDyI() + 19110000;
		this.info("LM076Report exec AcDate = " + iAcDate);

		List<Map<String, String>> lLM076Before = null;
		List<Map<String, String>> lLM076After = null;
		
		try {
			lLM076Before = lM076ServiceImpl.findAll(titaVo, iAcDate/100, true);
			lLM076After = lM076ServiceImpl.findAll(titaVo, iAcDate/100, true);
		} catch (Exception e) {
			StringWriter errors = new StringWriter();
			e.printStackTrace(new PrintWriter(errors));
			this.error("LM076ServiceImpl.findAll error = " + errors.toString());
		}

		exportExcel(titaVo, lLM076Before, lLM076After, iAcDate);

		return true;

	}

	private void exportExcel(TitaVo titaVo, List<Map<String, String>> lListBefore, List<Map<String, String>> lListAfter, int date) throws LogicException {

		this.info("LM076Report exportExcel");
		int entdy = date - 19110000; // expects date to be in BC Date format.
		String YearMonth = entdy/10000 + " 年 " + String.format("%02d", entdy/100%100) + " 月";

		makeExcel.open(titaVo, titaVo.getEntDyI(), titaVo.getKinbr(), "LM076", "B042金融機構承作「購地貸款」統計表", "LM076_B042金融機構承作「購地貸款」統計表" + showRocDate(entdy, 0).substring(0, 7),
				"LM076_底稿_B042金融機構承作「購地貸款」統計表.xlsx", 1, "FOA");

		// 資料期間 C2
		makeExcel.setValue(3, 4, "民國 " + YearMonth, "R");

		if ((lListBefore != null && !lListBefore.isEmpty()) || (lListAfter != null && !lListAfter.isEmpty())) {
			
			doOutput(lListBefore, 3);
			doOutput(lListAfter, 6);

			for (int i = 3; i < 9; i++) {
				makeExcel.formulaCaculate(12, i);
			}
		} else {
			makeExcel.setValue(3, 1, "本月無資料");
		}

		long sno = makeExcel.close();
		//makeExcel.toExcel(sno);
	}
	
	
	private void doOutput(List<Map<String, String>> list, int startColumn) throws LogicException
	{		
		for (Map<String, String> tLDVo : list) {
			int colShift = 0;
			int rowShift = 0;

			for (int i = 0; i <= 3 ; i++) {

				int col = startColumn + i; // 1-based
				int row = 7; // 1-based

				// Query received will have column names in the format of F0, even if no alias
				// is set in SQL
				// notice it's 0-based for those names
				String value = tLDVo.get("F" + i);

				switch (i) {
				// if specific column needs special treatment, insert case here.
				case 0:
					// CityCode: already properly converted
					rowShift = parse.stringToInteger(value) - 1;
					colShift--; // doesn't write
					break;
				case 1:
					// Newly Drawdown Amount: hundred million
					makeExcel.setValue(row + rowShift, col + colShift, computeDivide(getBigDecimal(value), billion, 2), "R");
					break;
				case 2:
					// Weighted Average of Loan: Percent
					makeExcel.setValue(row + rowShift, col + colShift, getBigDecimal(value).setScale(2, BigDecimal.ROUND_UP), "R");
					break;
				case 3:
					// Weighted Average of Loan Rate: Percent
					makeExcel.setValue(row + rowShift, col + colShift, getBigDecimal(value).setScale(2, BigDecimal.ROUND_UP), "R");
					break;
				default:
					makeExcel.setValue(row + rowShift, col + colShift, value, "R");
					break;
				}
			} // for

		} // for

	}
}
