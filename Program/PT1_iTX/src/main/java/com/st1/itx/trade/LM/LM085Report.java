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
import com.st1.itx.db.service.springjpa.cm.LM085ServiceImpl;
import com.st1.itx.util.common.MakeExcel;
import com.st1.itx.util.common.MakeReport;

@Component
@Scope("prototype")

public class LM085Report extends MakeReport {

	@Autowired
	LM085ServiceImpl lm085ServiceImpl;

	@Autowired
	MakeExcel makeExcel;

	@Override
	public void printTitle() {

	}

	/**
	 * 執行報表輸出
	 * 
	 * @param titaVo
	 * @param yearMonth     當月西元年月
	 * @param lastYearMonth 上個西元年月
	 */
	public boolean exec(TitaVo titaVo, int yearMonth, int lastYearMonth) throws LogicException {

		this.info("LM085Report exec");

		// 有無資料
		boolean isEmpty = false;
		// 西元年月
		int iYearMonth = yearMonth;
		// 民國年月
		int iRocYeatMonth = iYearMonth - 191100;

		String unitName = titaVo.getParam("UnitName").toString();
		String unitCode = titaVo.getParam("UnitCode").toString();

		// 開啟指定檔案
		makeExcel.open(titaVo, titaVo.getEntDyI(), titaVo.getKinbr(), "LM085",
				"放款逾期月報表-" + iRocYeatMonth + "(單位：" + unitName + ")", "LM085-" + iRocYeatMonth + "_放款逾期月報表",
				"LM085_底稿_放款逾期月報表.xlsx", "X月");

		makeExcel.setValue(2, 1,
				"－" + (iRocYeatMonth / 100) + "." + String.format("%02d", (iRocYeatMonth % 100)) + "－");

		makeExcel.setValue(3, 7, "單位：" + unitName, "R");
		makeExcel.setValue(26, 7, "單位：" + unitName, "R");
		makeExcel.setValue(39, 7, "單位：" + unitName, "R");

		List<Map<String, String>> fnAllList = new ArrayList<>();

		try {

			fnAllList = lm085ServiceImpl.findPart1(titaVo, yearMonth, unitCode);
			isEmpty = fnAllList.size() == 0 ? isEmpty : true;
			exportPart1(fnAllList);

			// SQL未完成 待確認
//			fnAllList = lm085ServiceImpl.findPart2_1(titaVo, yearMonth);
//			isEmpty = fnAllList.size() == 0 ? isEmpty : true;
//			exportPart2(fnAllList,1);
			fnAllList = lm085ServiceImpl.findPart2_2(titaVo, yearMonth);
			isEmpty = fnAllList.size() == 0 ? isEmpty : true;
			exportPart2(fnAllList, 2);
//			fnAllList = lm085ServiceImpl.findPart2_3(titaVo, yearMonth);
//			isEmpty = fnAllList.size() == 0 ? isEmpty : true;
//			exportPart2(fnAllList,3);
			// 上月資料
			fnAllList = lm085ServiceImpl.findPart3(titaVo, lastYearMonth, unitCode);
			isEmpty = fnAllList.size() == 0 ? isEmpty : true;
			exportPart3(fnAllList, lastYearMonth, 1);
			// 去年同期資料
			fnAllList = lm085ServiceImpl.findPart3(titaVo, yearMonth - 100, unitCode);
			isEmpty = fnAllList.size() == 0 ? isEmpty : true;
			exportPart3(fnAllList, yearMonth - 100, 2);

			fnAllList = lm085ServiceImpl.findPart4(titaVo, tranAllYearData(yearMonth, lastYearMonth), unitCode);
			isEmpty = fnAllList.size() == 0 ? isEmpty : true;
			exportPart4(fnAllList, tranAllYearData(yearMonth, lastYearMonth).size());

		} catch (Exception e) {

			StringWriter errors = new StringWriter();
			e.printStackTrace(new PrintWriter(errors));
			this.info("LM085ServiceImpl.findAll error = " + errors.toString());

		}

		if (!isEmpty) {
			makeExcel.setValue(6, 3, "本日無資料");
		}
		// 關閉
		makeExcel.close();

		return isEmpty;

	}

	private void exportPart1(List<Map<String, String>> dataList) throws LogicException {

		int col = 0;
		int row = 0;
		BigDecimal amt = BigDecimal.ZERO;

		for (Map<String, String> r : dataList) {

			col = enToNumber(r.get("F0").toString().substring(0, 1));
			row = Integer.valueOf(r.get("F0").toString().substring(1, 2));
			amt = getBigDecimal(r.get("F1").toString());

			this.info("col=" + col + ",row=" + row);

			// 限制超出L欄以後不設值 略過
			if (col > 12) {
				break;
			}

			makeExcel.setValue(row, col, amt, "#,##0", "R");

		}

		makeExcel.formulaCalculate(6, 6);
		makeExcel.formulaCalculate(7, 6);
		makeExcel.formulaCalculate(8, 6);
		makeExcel.formulaCalculate(9, 6);

		makeExcel.formulaCalculate(7, 9);

		makeExcel.formulaCalculate(10, 2);
		makeExcel.formulaCalculate(10, 3);
		makeExcel.formulaCalculate(10, 4);
		makeExcel.formulaCalculate(10, 5);
		makeExcel.formulaCalculate(10, 6);
		makeExcel.formulaCalculate(10, 7);

	}

	private void exportPart2(List<Map<String, String>> dataList, int form) throws LogicException {
		int row = 0;

		switch (form) {
		case 1:
			// B16
			makeExcel.formulaCalculate(16, 2);
			// B18
			makeExcel.formulaCalculate(18, 2);

			break;
		case 2:
			row = 17;
			for (Map<String, String> r : dataList) {
				int rowSpace = Integer.valueOf(r.get("F0"));
				BigDecimal percent = r.get("F1").isEmpty() || r.get("F1") == "0" ? BigDecimal.ZERO
						: new BigDecimal(r.get("F1"));

				makeExcel.setValue(row + rowSpace, 6, percent, "0.000 %");
			}

			// F15
			makeExcel.formulaCalculate(15, 6);
			break;
		case 3:
			break;
		}

	}

	private void exportPart3(List<Map<String, String>> dataList, int yearMonth, int form) throws LogicException {

		int col = 0;
		int row = 0;
		BigDecimal amt = BigDecimal.ZERO;

		String rocYear = String.valueOf((yearMonth - 191100) / 100);
		String rocMonth = String.format("%02d", yearMonth % 100);

		switch (form) {
		case 1:

			makeExcel.setValue(27, 2, rocYear + "." + rocMonth + "(B)", "C");

			for (Map<String, String> r : dataList) {

				col = enToNumber(r.get("F0").toString().substring(0, 1));
				row = Integer.valueOf(r.get("F0").toString().substring(1, 3));
				amt = getBigDecimal(r.get("F1").toString());

//				this.info("col=" + col + ",row=" + row);

				// 限制超出L欄以後不設值 略過
				if (col > 12) {
					break;
				}

				makeExcel.setValue(row, col, amt, "#,##0", "R");

			}

			makeExcel.formulaCalculate(29, 2);
			makeExcel.formulaCalculate(33, 2);
			makeExcel.formulaCalculate(38, 2);
			makeExcel.formulaCalculate(29, 4);
			makeExcel.formulaCalculate(30, 4);
			makeExcel.formulaCalculate(31, 4);
			makeExcel.formulaCalculate(32, 4);
			makeExcel.formulaCalculate(33, 4);
			makeExcel.formulaCalculate(34, 4);
			makeExcel.formulaCalculate(35, 4);
			makeExcel.formulaCalculate(36, 4);
			makeExcel.formulaCalculate(37, 4);
			makeExcel.formulaCalculate(38, 4);

			break;
		case 2:

			makeExcel.setValue(27, 6, rocYear + "." + rocMonth + "(C)", "C");

			for (Map<String, String> r : dataList) {
				// 因欄位不同無法更改
				col = enToNumber(r.get("F0").toString().substring(0, 1)) + 4;
				row = Integer.valueOf(r.get("F0").toString().substring(1, 3));
				amt = getBigDecimal(r.get("F1").toString());

//				this.info("col=" + col + ",row=" + row);

				// 限制超出L欄以後不設值 略過
				if (col > 12) {
					break;
				}

				makeExcel.setValue(row, col, amt, "#,##0", "R");

			}

			makeExcel.formulaCalculate(29, 6);
			makeExcel.formulaCalculate(33, 6);
			makeExcel.formulaCalculate(38, 6);
			makeExcel.formulaCalculate(29, 7);
			makeExcel.formulaCalculate(30, 7);
			makeExcel.formulaCalculate(31, 7);
			makeExcel.formulaCalculate(32, 7);
			makeExcel.formulaCalculate(33, 7);
			makeExcel.formulaCalculate(34, 7);
			makeExcel.formulaCalculate(35, 7);
			makeExcel.formulaCalculate(36, 7);
			makeExcel.formulaCalculate(37, 7);
			makeExcel.formulaCalculate(38, 7);
			break;
		}

	}

	private void exportPart4(List<Map<String, String>> dataList, int dataSize) throws LogicException {

		int col = 0;
		int varRow = 41;
		BigDecimal amt = BigDecimal.ZERO;

		// 最少11列數，最多14列數，基礎為11個列數 根據資料筆數 做插入多少列
		for (int i = 0; i < dataSize - 11; i++) {
			makeExcel.setShiftRow(50, 1);
		}

		// F0 年月
		// F1 B欄位
		// F2 放款餘額(含催收款)
		// F3 D欄位
		// F4 逾放總額
		// F5 G欄位
		// F6 當年度轉呆金額
		for (Map<String, String> r : dataList) {
			int rocYear = (Integer.valueOf(r.get("F0")) - 191100) / 100;
			int rocMonth = Integer.valueOf(r.get("F0")) % 100;
			String yearMonthText = rocYear + "";
			switch (rocMonth) {
			case 3:
				yearMonthText = yearMonthText + " / Q1";
				break;
			case 6:
				yearMonthText = yearMonthText + " / Q2";
				break;
			case 9:
				yearMonthText = yearMonthText + " / Q3";
				break;
			case 12:
				yearMonthText = yearMonthText + " / Q4";
				break;
			default:
				yearMonthText = yearMonthText + " / " + rocMonth;
				break;

			}

			// 年月
			makeExcel.setValue(varRow, 1, yearMonthText, "C");

			// 放款餘額
			col = enToNumber(r.get("F1").toString());
			amt = getBigDecimal(r.get("F2").toString());

			makeExcel.setValue(varRow, col, amt, "#,###.##0", "R");

			// 逾放總額
			col = enToNumber(r.get("F3").toString());
			amt = getBigDecimal(r.get("F4").toString());

			makeExcel.setValue(varRow, col, amt, "#,###.##0", "R");

			// 逾放比
			makeExcel.setValue(varRow, 6, getBigDecimal(r.get("F4").toString())
					.divide(getBigDecimal(r.get("F2").toString()), 5, BigDecimal.ROUND_HALF_UP), "0.####0", "R");

			// 當年度轉呆金額
			col = enToNumber(r.get("F5").toString());
			amt = getBigDecimal(r.get("F6").toString());

			makeExcel.setValue(varRow, col, amt, "#,###.##0", "R");

			varRow++;
		}

		for (int r = 45; r <= 55; r++) {
			makeExcel.formulaCalculate(r, 5);
		}

		for (int c = 2; c <= 6; c++) {
			makeExcel.formulaCalculate(56, c);
			makeExcel.formulaCalculate(57, c);
		}

	}

	/**
	 * 英文轉數字
	 * 
	 * @param colText 英文字母
	 */
	private int enToNumber(String colText) {
		String colTxt = "";
		int col = 0;

		colTxt = colText.toUpperCase();

		char[] tokens = colTxt.toCharArray();

		for (char token : tokens) {
			col = Integer.valueOf(token) - 64;
		}

		return col;

	}

	/**
	 * 年月份分群處理
	 * 
	 * @param yearMonth     當西元年月
	 * @param lastYearMonth 上西元年月
	 * @return (0)全年月份資料 (1各)年度轉呆金額 (2)各年月轉呆金額
	 */
	private List<String> tranAllYearData(int yearMonth, int lastYearMonth) {

		List<String> list = new ArrayList<String>();

		int iYear = yearMonth / 100;
		int ilYear = lastYearMonth / 100;
		int ilMonth = lastYearMonth % 100;

		List<Integer> data = new ArrayList<Integer>();

		data.add(yearMonth);
		data.add(lastYearMonth);
		if (ilMonth > 9) {

			data.add(ilYear * 100 + 9);
			data.add(ilYear * 100 + 6);
			data.add(ilYear * 100 + 3);

			data.add((ilYear - 1) * 100 + 12);
			data.add((ilYear - 1) * 100 + 9);
			data.add((ilYear - 1) * 100 + 6);
			data.add((ilYear - 1) * 100 + 3);

			data.add((ilYear - 2) * 100 + 12);
			data.add((ilYear - 3) * 100 + 12);
			data.add((ilYear - 4) * 100 + 12);
			data.add((ilYear - 5) * 100 + 12);
			data.add((ilYear - 6) * 100 + 12);

		} else if (ilMonth > 6) {

			data.add(iYear * 100 + 6);
			data.add(iYear * 100 + 3);

			data.add((ilYear - 1) * 100 + 12);
			data.add((ilYear - 1) * 100 + 9);
			data.add((ilYear - 1) * 100 + 6);
			data.add((ilYear - 1) * 100 + 3);

			data.add((ilYear - 2) * 100 + 12);
			data.add((ilYear - 3) * 100 + 12);
			data.add((ilYear - 4) * 100 + 12);
			data.add((ilYear - 5) * 100 + 12);
			data.add((ilYear - 6) * 100 + 12);

		} else if (ilMonth > 3) {

			data.add(iYear * 100 + 3);

			data.add((ilYear - 1) * 100 + 12);
			data.add((ilYear - 1) * 100 + 9);
			data.add((ilYear - 1) * 100 + 6);
			data.add((ilYear - 1) * 100 + 3);

			data.add((ilYear - 2) * 100 + 12);
			data.add((ilYear - 3) * 100 + 12);
			data.add((ilYear - 4) * 100 + 12);
			data.add((ilYear - 5) * 100 + 12);
			data.add((ilYear - 6) * 100 + 12);

		} else {

			data.add((ilYear - 1) * 100 + 12);
			data.add((ilYear - 1) * 100 + 9);
			data.add((ilYear - 1) * 100 + 6);
			data.add((ilYear - 1) * 100 + 3);

			data.add((ilYear - 2) * 100 + 12);
			data.add((ilYear - 3) * 100 + 12);
			data.add((ilYear - 4) * 100 + 12);
			data.add((ilYear - 5) * 100 + 12);
			data.add((ilYear - 6) * 100 + 12);

		}

		String yearMonthGroup = "";
		String yearGroupBadDebt = "";
		String yearMonthGroupBadDebt = "";
		for (int i = 0; i < data.size(); i++) {

			yearMonthGroup = yearMonthGroup + String.valueOf(data.get(i)) + ",";

			if (i >= data.size() - 5) {
				yearGroupBadDebt = yearGroupBadDebt + String.valueOf(data.get(i)).substring(0, 4) + ",";
			}
			if (i < data.size() - 5) {
				yearMonthGroupBadDebt = yearMonthGroupBadDebt + String.valueOf(data.get(i)) + ",";
			}
		}
		// 全歷年資料
		list.add(yearMonthGroup.substring(0, yearMonthGroup.length() - 1));
		// 當年轉呆資料
		list.add(yearGroupBadDebt.substring(0, yearGroupBadDebt.length() - 1));
		// 當年月轉呆資料
		list.add(yearMonthGroupBadDebt.substring(0, yearMonthGroupBadDebt.length() - 1));

		return list;
	}
}
