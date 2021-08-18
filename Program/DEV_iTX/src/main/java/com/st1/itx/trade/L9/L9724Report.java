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
import com.st1.itx.db.service.springjpa.cm.L9724ServiceImpl;
import com.st1.itx.util.common.MakeExcel;
import com.st1.itx.util.common.MakeReport;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

@Component
@Scope("prototype")

public class L9724Report extends MakeReport {

	@Autowired
	L9724ServiceImpl l9724ServiceImpl;

	@Autowired
	MakeExcel makeExcel;

	String TXCD = "L9724";
	String TXName = "應收利息之帳齡分析表";

	// pivot position for data inputs
	int pivotRow = 2; // 1-based
	int pivotCol = 1; // 1-based

	public boolean exec(TitaVo titaVo) throws LogicException {
		this.info("L9724Report exec start ...");

		List<Map<String, String>> lL9724 = null;

		try {
			lL9724 = l9724ServiceImpl.findAll(titaVo, 9);
			exportExcel(titaVo, lL9724, true, false, "LA$W22P");
			lL9724 = l9724ServiceImpl.findAll(titaVo, 0);
			exportExcel(titaVo, lL9724, false, false, "一個月以下");
			lL9724 = l9724ServiceImpl.findAll(titaVo, 1);
			exportExcel(titaVo, lL9724, false, false, "一~三個月");
			lL9724 = l9724ServiceImpl.findAll(titaVo, 2);
			exportExcel(titaVo, lL9724, false, false, "三~六個月");
			lL9724 = l9724ServiceImpl.findAll(titaVo, 3);
			exportExcel(titaVo, lL9724, false, true, "六個月以上");
		} catch (Exception e) {
			StringWriter errors = new StringWriter();
			e.printStackTrace(new PrintWriter(errors));
			this.error(TXCD + "ServiceImpl.findAll error = " + errors.toString());
		}

		return true;

	}

	private void exportExcel(TitaVo titaVo, List<Map<String, String>> lList, boolean newExcel, boolean closeExcel, String SheetName) throws LogicException {

		this.info("L9724Report exportExcel");

		if (newExcel == true) {
			makeExcel.open(titaVo, titaVo.getEntDyI(), titaVo.getKinbr(), TXCD, TXName, TXCD + "_" + TXName, TXCD + "_底稿_" + TXName + ".xlsx", SheetName, SheetName);
		} else {
			makeExcel.setSheet(SheetName);
		}

		// fill in inputDate with proper format
		String targetDate = Integer.toString(Integer.parseInt(titaVo.getParam("inputEndOfMonthDate")) + 19110000); // YYYYMMDD
		// at F1
		makeExcel.setValue(1, 6, "月差 " + targetDate.substring(0, 4) + "/" + targetDate.substring(4, 6) + "/" + targetDate.substring(6));

		if (lList != null && lList.size() != 0) {

			int rowShift = 0;
			int totalInterest = 0;

			for (Map<String, String> tLDVo : lList) {

				int row = pivotRow + rowShift; // 1-based

				// for later date difference calculation uses.
				// make LocalDates
				LocalDate endDate = LocalDate.of(Integer.parseInt(targetDate.substring(0, 4)), Integer.parseInt(targetDate.substring(4, 6)), Integer.parseInt(targetDate.substring(6)));
				LocalDate startDate = LocalDate.of(Integer.parseInt(tLDVo.get("F4").substring(0, 4)), Integer.parseInt(tLDVo.get("F4").substring(4, 6)),
						Integer.parseInt(tLDVo.get("F4").substring(6)));

				// withDayOfMonth(1) since for cases like 3/31 and 4/1, it should show as 1
				// month too.
				long monthsBetween = ChronoUnit.MONTHS.between(startDate.withDayOfMonth(1), endDate.withDayOfMonth(1));
				long daysDiffTotal = ChronoUnit.DAYS.between(startDate, endDate);
				int daysDiffSameMonth = endDate.getDayOfMonth() - startDate.getDayOfMonth();

				// switch by Column: there're more Column than actual query result.
				for (int col = 0; col < 8; col++) {

					String tmpValue = "";

					switch (col) // notice it's 0 based for this
					{

					case 3:
						tmpValue = tLDVo.get("F" + col);
						totalInterest += Integer.parseInt(tmpValue);
						makeExcel.setValue(row, col + pivotCol, Integer.parseInt(tmpValue), "R");
						break;
					case 4:
						tmpValue = tLDVo.get("F" + col);
						// format to YYYY/M/DD
						makeExcel.setValue(row, col + pivotCol, tmpValue.substring(0, 4) + "/" + Integer.toString(Integer.parseInt(tmpValue.substring(4, 6))) + "/" + tmpValue.substring(6), "R");
						break;
					case 5:
						// month diff
						if (monthsBetween >= 0) {
							makeExcel.setValue(row, col + pivotCol, monthsBetween, "R");
						}
						break;
					case 6:
						// day diff (month excluded)
						// daysDiffTotal: need to make sure endDate is after startDate.
						// daysDiffSameMonth: 3/31 and 4/1 shoudn't have 30 days difference; that kinda
						// task goes to daysDiffTotal.
						if (daysDiffTotal >= 0 && daysDiffSameMonth >= 0) {
							makeExcel.setValue(row, col + pivotCol, daysDiffSameMonth, "R");
						}
						break;
					case 7:
						// total day diff (month included)
						if (daysDiffTotal >= 0) {
							makeExcel.setValue(row, col + pivotCol, daysDiffTotal, "R");
						}
						break;
					default:
						tmpValue = tLDVo.get("F" + col);
						makeExcel.setValue(row, col + pivotCol, Integer.parseInt(tmpValue), "R");
						break;

					}

				} // for

				rowShift++;

			} // for

			// total Interest
			makeExcel.setValue(pivotRow + rowShift, 4, totalInterest, "R");
			makeExcel.setValue(1, 9, totalInterest, "_-$* #,##0_-;-$* #,##0_-;_-$* \"-\"??_-;_-@_-");

		} else {
			makeExcel.setValue(pivotRow, pivotCol, "本月無資料");
		}

		if (closeExcel == true) {
			long sno = makeExcel.close();
			makeExcel.toExcel(sno);
		}
	}
}
