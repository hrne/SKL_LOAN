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
import com.st1.itx.db.service.springjpa.cm.LM078ServiceImpl;
import com.st1.itx.util.common.MakeExcel;
import com.st1.itx.util.common.MakeReport;

@Component
@Scope("prototype")

public class LM078Report extends MakeReport {

	@Autowired
	LM078ServiceImpl lM077ServiceImpl;

	@Autowired
	MakeExcel makeExcel;

	private static final BigDecimal hundredMillion = new BigDecimal("100000000");

	public boolean exec(TitaVo titaVo) throws LogicException {
		this.info("LM078Report exec start ...");

		int iAcDate = titaVo.getEntDyI() + 19110000;
		this.info("LM078Report exec AcDate = " + iAcDate);

		List<Map<String, String>> lLM078 = null;

		try {
			lLM078 = lM077ServiceImpl.findAll(titaVo, iAcDate / 100);
		} catch (Exception e) {
			StringWriter errors = new StringWriter();
			e.printStackTrace(new PrintWriter(errors));
			this.error("LM078ServiceImpl.findAll error = " + errors.toString());
		}

		exportExcel(titaVo, lLM078, iAcDate);

		return true;

	}

	private void exportExcel(TitaVo titaVo, List<Map<String, String>> lList, int date) throws LogicException {

		// pivot position for data inputs
		int pivotRow = 7; // 1-based
		int pivotCol = 3; // 1-based

		this.info("LM078Report exportExcel");
		int entdy = date - 19110000; // expects date to be in BC Date format.
		String YearMonth = entdy / 10000 + " 年 " + String.format("%02d", date / 100 % 100) + " 月";

		makeExcel.open(titaVo, titaVo.getEntDyI(), titaVo.getKinbr(), "LM078", "B044「借款戶向金融機構申請並經錄案」之不動產抵押貸款案件辦理情形", "LM078_B044「借款戶向金融機構申請並經錄案」之不動產抵押貸款案件辦理情形" + showRocDate(entdy, 0).substring(0, 7),
				"LM078_底稿_B044「借款戶向金融機構申請並經錄案」之不動產抵押貸款案件辦理情形.xlsx", 1, "FOA");

		// 資料期間 E3
		makeExcel.setValue(3, 5, "民國 " + YearMonth, "R");

		if (lList != null && !lList.isEmpty()) {

			for (Map<String, String> tLDVo : lList) {
				int colShift = 0;
				int rowShift = 0;

				for (int i = 0; i <= 2; i++) {

					int col = i + pivotCol; // 1-based
					int row = pivotRow; // 1-based

					// Query received will have column names in the format of F0, even if no alias
					// is set in SQL
					// notice it's 0-based for those names
					String value = tLDVo.get("F" + i);

					switch (i) {
					// if specific column needs special treatment, insert case here.
					case 0:
						// RuleCode: 01/02/03/04 - col 3~4 (+0)
						// 06 - col 5~6 (+2)
						// 07 - col 7~8 (+4)
						// 08 - col 9~10 (+6)
						// 09 - col 11~12 (+8)
						// 10 - col 13~14 (+10)
						switch (value) {
						case "01":
						case "02":
						case "03":
						case "04":
							colShift = 0;
							break;
						case "06":
							colShift = 2;
							break;
						case "07":
							colShift = 4;
							break;
						case "08":
							colShift = 6;
							break;
						case "09":
							colShift = 8;
							break;
						case "10":
							colShift = 10;
							break;
						default:
							break;
						}
						colShift--; // doesn't write
						break;
					case 2:
						// Newly Drawdown Amount: hundred million
						makeExcel.setValue(row + rowShift, col + colShift, computeDivide(getBigDecimal(value), hundredMillion, 2), "R");
						break;
					default:
						makeExcel.setValue(row + rowShift, col + colShift, value, "R");
						break;
					}
				} // for

			} // for
		} else {
			makeExcel.setValue(4, 1, "本月無資料");
		}

		long sno = makeExcel.close();
		// makeExcel.toExcel(sno);
	}
}
