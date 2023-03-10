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
import com.st1.itx.db.service.springjpa.cm.LM074ServiceImpl;
import com.st1.itx.util.common.MakeExcel;
import com.st1.itx.util.common.MakeReport;
import com.st1.itx.util.common.data.ReportVo;
import com.st1.itx.util.parse.Parse;

@Component
@Scope("prototype")

public class LM074Report extends MakeReport {

	@Autowired
	LM074ServiceImpl lM074ServiceImpl;
	
	@Autowired
	Parse parse;

	@Autowired
	MakeExcel makeExcel;
	
	private static final BigDecimal hundredMillion = new BigDecimal("100000000");

	public boolean exec(TitaVo titaVo) throws LogicException {
		this.info("LM074Report exec start ...");

		int iAcDate = titaVo.getEntDyI() + 19110000;
		this.info("LM074Report exec AcDate = " + iAcDate);

		List<Map<String, String>> lLM074 = null;

		try {
			lLM074 = lM074ServiceImpl.findAll(titaVo, iAcDate/100);
		} catch (Exception e) {
			StringWriter errors = new StringWriter();
			e.printStackTrace(new PrintWriter(errors));
			this.error("LM074ServiceImpl.findAll error = " + errors.toString());
		}

		exportExcel(titaVo, lLM074, iAcDate);

		return true;

	}

	private void exportExcel(TitaVo titaVo, List<Map<String, String>> lList, int date) throws LogicException {

		// pivot position for data inputs
		int pivotRow = 7; // 1-based
		int pivotCol = 3; // 1-based

		this.info("LM074Report exportExcel");
		int entdy = date - 19110000; // expects date to be in BC Date format.
		String YearMonth = entdy / 10000 + " 年 " + String.format("%02d", date/100%100) + " 月";

		int reportDate = titaVo.getEntDyI() + 19110000;
		String brno = titaVo.getBrno();
		String txcd = "LM074";
		String fileItem = "B040金融機構承作｢公司法人購置住宅貸款｣統計表";
		String fileName = "LM074_B040金融機構承作｢公司法人購置住宅貸款｣統計表" + showRocDate(entdy, 0).substring(0, 7);
		String defaultExcel = "LM074_底稿_B040金融機構承作｢公司法人購置住宅貸款｣統計表.xlsx";
		String defaultSheet = "FOA";

		this.info("reportVo open");

		ReportVo reportVo = ReportVo.builder().setRptDate(reportDate).setBrno(brno).setRptCode(txcd)
				.setRptItem(fileItem).build();
		// 開啟報表
		makeExcel.open(titaVo, reportVo, fileName, defaultExcel, defaultSheet);
		
//		makeExcel.open(titaVo, titaVo.getEntDyI(), titaVo.getKinbr(), "LM074", "B040金融機構承作｢公司法人購置住宅貸款｣統計表", "LM074_B040金融機構承作｢公司法人購置住宅貸款｣統計表" + showRocDate(entdy, 0).substring(0, 7),
//				"LM074_底稿_B040金融機構承作｢公司法人購置住宅貸款｣統計表.xlsx", 1, "FOA");

		// 資料期間 E3
		makeExcel.setValue(3, 5, "民國 " + YearMonth, "R");

		if (lList != null && !lList.isEmpty()) {

			for (Map<String, String> tLDVo : lList) {
				int colShift = 0;
				int rowShift = 0;

				for (int i = 0; i <= 5; i++) {

					int col = i + pivotCol; // 1-based
					int row = pivotRow; // 1-based

					// Query received will have column names in the format of F0, even if no alias
					// is set in SQL
					// notice it's 0-based for those names
					String value = tLDVo.get("F" + i);

					switch (i) {
					// if specific column needs special treatment, insert case here.
					case 0:
						// RuleCode: 06 - col 3~6 (+0)
						// 07 - col 7~10 (+4)
						if ("06".equals(value)) {
							colShift = 0;
						} else if ("07".equals(value)) {
							colShift = 4;
						} else {
							this.info("LM074Report exportExcel: RuleCodeException, got " + value);
							break;
						}
						;
						colShift--; // doesn't write
						break;
					case 1:
						// CityCode: already properly converted
						rowShift = parse.stringToInteger(value) - 1;
						colShift--; // doesn't write
						break;
					case 3:
						// Newly Drawdown Amount: hundred million
						makeExcel.setValue(row + rowShift, col + colShift, computeDivide(getBigDecimal(value), hundredMillion, 2), "R");
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
						makeExcel.setValue(row + rowShift, col + colShift, value, "R");
						break;
					}
				} // for

			} // for

			for (int i = 3; i < 11; i++) {
				makeExcel.formulaCaculate(14, i);
			}
		} else {
			makeExcel.setValue(4, 1, "本月無資料");
		}

		makeExcel.close();
		//makeExcel.toExcel(sno);
	}
}
