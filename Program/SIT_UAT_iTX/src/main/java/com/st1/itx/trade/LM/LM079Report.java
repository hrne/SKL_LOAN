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
import com.st1.itx.db.service.springjpa.cm.LM079ServiceImpl;
import com.st1.itx.util.common.MakeExcel;
import com.st1.itx.util.common.MakeReport;
import com.st1.itx.util.parse.Parse;

@Component
@Scope("prototype")

public class LM079Report extends MakeReport {

	@Autowired
	LM079ServiceImpl lM079ServiceImpl;

	@Autowired
	MakeExcel makeExcel;
	
	@Autowired
	Parse parse;
	
	boolean hasOutputted = false;
	
	private static BigDecimal hundredMillion = new BigDecimal("100000000");

	public boolean exec(TitaVo titaVo) throws LogicException {
		this.info("LM079Report exec start ...");

		int iAcDate = titaVo.getEntDyI() + 19110000;
		this.info("LM079Report exec AcDate = " + iAcDate);

		List<List<Map<String, String>>> lLM079List = new ArrayList<List<Map<String, String>>>();
		
		try {
			lLM079List.add(lM079ServiceImpl.findAll(titaVo, iAcDate/100, 20210319, 20210923));
			lLM079List.add(lM079ServiceImpl.findAll(titaVo, iAcDate/100, 20210924, 20211216));
			lLM079List.add(lM079ServiceImpl.findAll(titaVo, iAcDate/100, 20211217, 99999999));
		} catch (Exception e) {
			StringWriter errors = new StringWriter();
			e.printStackTrace(new PrintWriter(errors));
			this.error("LM079ServiceImpl.findAll error = " + errors.toString());
		}

		exportExcel(titaVo, lLM079List, iAcDate);

		return true;

	}

	private void exportExcel(TitaVo titaVo, List<List<Map<String, String>>> foundList, int date) throws LogicException {

		// pivot position for data inputs
		
		this.info("LM079Report exportExcel");
		int entdy = date - 19110000; // expects date to be in BC Date format.
		String YearMonth = entdy/10000 + " 年 " + String.format("%02d", entdy/100%100) + " 月";

		makeExcel.open(titaVo, titaVo.getEntDyI(), titaVo.getKinbr(), "LM079", "B045金融機構承作「工業區閒置土地抵押貸款」統計表", "LM079_B045金融機構承作「工業區閒置土地抵押貸款」統計表" + showRocDate(entdy, 0).substring(0, 7),
				"LM079_底稿_B045金融機構承作「工業區閒置土地抵押貸款」統計表.xlsx", 1, "FOA");

		// 資料期間 C2
		makeExcel.setValue(3, 6, "民國 " + YearMonth, "R");

		if (foundList != null && !foundList.isEmpty()) {
			
			for (int i = 0; i < foundList.size(); i++)
			{
				doOutput(foundList.get(i), 3 + 3*i);
			}
			for (int i = 3; i < 3 + foundList.size() * 3; i++) {
				makeExcel.formulaCaculate(12, i);
			}
		}
		
		if (!hasOutputted)
			makeExcel.setValue(4, 1, "本月無資料");
		else
			makeExcel.formulaRangeCalculate(7,14,12,32);
		
		// long sno = 
		makeExcel.close();
		//makeExcel.toExcel(sno);
	}
	
	private void doOutput(List<Map<String, String>> list, int startColumn) throws LogicException
	{
		if (list == null || list.isEmpty())
			return;
		
		hasOutputted = true;
		
		for (Map<String, String> tLDVo : list) {
			int colShift = 0;
			int rowShift = 0;

			for (int i = 0; i <= 3; i++) {

				int col = i + startColumn; // 1-based
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
					// Newly Drawdown Amount: hundred million 10^8
					makeExcel.setValue(row + rowShift, col + colShift, getBigDecimal(value).divide(hundredMillion, 2, BigDecimal.ROUND_UP), "R");
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
					makeExcel.setValue(row + rowShift, col + colShift, parse.isNumeric(value) ? getBigDecimal(value) : value, "R");
					break;
				}
			} // for

		} // for
	}
}
