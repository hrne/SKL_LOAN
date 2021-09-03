package com.st1.itx.trade.LM;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.db.service.springjpa.cm.LM048ServiceImpl;
import com.st1.itx.util.common.MakeExcel;
import com.st1.itx.util.common.MakeReport;

@Component
@Scope("prototype")
public class LM048Report extends MakeReport {

	@Autowired
	LM048ServiceImpl lM048ServiceImpl;

	@Autowired
	MakeExcel makeExcel;

	private List<Map<String, String>> listA = null;
	private List<Map<String, String>> listB = null;
	private List<Map<String, String>> listC = null;

	private BigDecimal totalOfLineAmt = BigDecimal.ZERO;
	private BigDecimal totalOfLoanBal = BigDecimal.ZERO;

	@Override
	public void printTitle() {

	}

	public void exec(TitaVo titaVo) throws LogicException {
		List<Map<String, String>> listLM048 = null;

		int inputYearMonth = Integer.parseInt(titaVo.getParam("YearMonth")) + 191100;

		String entLoanBalLimit = titaVo.getParam("EntLoanBalLimit");

		try {
			listLM048 = lM048ServiceImpl.queryDetail(inputYearMonth, entLoanBalLimit, titaVo);
		} catch (Exception e) {
			StringWriter errors = new StringWriter();
			e.printStackTrace(new PrintWriter(errors));
			this.info("LM048ServiceImpl.testExcel error = " + errors.toString());
		}

		exportExcel(titaVo, listLM048);
	}

	private void exportExcel(TitaVo titaVo, List<Map<String, String>> listLM048) throws LogicException {

		this.info("LM048Report exportExcel");

		makeExcel.open(titaVo, titaVo.getEntDyI(), titaVo.getKinbr(), "LM048", "企業放款風險承擔限額控管表", "LM048企業放款風險承擔限額控管表",
				"LM048_底稿_企業放款風險承擔限額控管表.xlsx", "明細總表108.04");

		int enyDy = titaVo.getEntDyI() + 19110000;

		makeExcel.setSheet("明細總表108.04", "明細總表" + showRocDate(enyDy, 6).substring(0, 6));

		makeExcel.setValue(2, 3, showRocDate(enyDy, 0), "C");

		if (listLM048 == null || listLM048.size() == 0) {

			makeExcel.setValue(2, 1, "本日無資料");

		} else {

			classifyData(listLM048);

			int rowCursorA = 4;
			int rowCursorB = 6;
			int rowCursorC = 8;
			int rowCursorTotal = 10;

			if (listA.size() > 1) {
				// 將表格往下移，移出空間
				makeExcel.setShiftRow(rowCursorA + 1, listA.size() - 1);
				// 更新行數指標
				rowCursorB += listA.size() - 1;
				rowCursorC += listA.size() - 1;
				rowCursorTotal += listA.size() - 1;
			}
			if (listA.size() > 0) {
				// 寫入資料
				setValueToExcel(rowCursorA, listA);
			}
			if (listB.size() > 1) {
				// 將表格往下移，移出空間
				makeExcel.setShiftRow(rowCursorB + 1, listB.size() - 1);
				// 更新行數指標
				rowCursorC += listB.size() - 1;
				rowCursorTotal += listB.size() - 1;
			}
			if (listB.size() > 0) {
				// 寫入資料
				setValueToExcel(rowCursorB, listB);
			}
			if (listC.size() > 1) {
				// 將表格往下移，移出空間
				makeExcel.setShiftRow(rowCursorC + 1, listC.size() - 1);
				// 更新行數指標
				rowCursorTotal += listC.size() - 1;
			}
			if (listC.size() > 0) {
				// 寫入資料
				setValueToExcel(rowCursorC, listC);
			}

			// 印總合計
			// 總合計-核貸金額
			makeExcel.setValue(rowCursorTotal, 5, formatThousand(totalOfLineAmt), "#,##0");
			// 總合計-放款本金餘額
			makeExcel.setValue(rowCursorTotal, 6, formatThousand(totalOfLoanBal), "#,##0");
		}

		long sno = makeExcel.close();
		makeExcel.toExcel(sno);
	}

	private void classifyData(List<Map<String, String>> listLM048) {

		listA = new ArrayList<>();
		listB = new ArrayList<>();
		listC = new ArrayList<>();

		Map<String, String> mapBElse = new HashMap<>();

		for (Map<String, String> mapLM048 : listLM048) {

			// 這一筆的評等
			String rating = mapLM048.get("F0");

			switch (rating) {
			case "A":
				listA = putData(listA, mapLM048);
				break;
			case "B":
			case "B_Empty":
				listB = putData(listB, mapLM048);
				break;
			case "B_Else":
				putDataBElse(mapBElse, mapLM048);
				break;
			case "C":
				listC = putData(listC, mapLM048);
				break;
			default:
				break;
			}
		}

		if (!mapBElse.isEmpty()) {
			this.info("mapBElse is not empty.");
			listB.add(mapBElse);
		}

	}

	private List<Map<String, String>> putData(List<Map<String, String>> listTmp, Map<String, String> mapLM048) {

		String rating = mapLM048.get("F0"); // 評等
		String industryItem = mapLM048.get("F1"); // 行業別名稱
		String custNo = mapLM048.get("F2"); // 戶號
		String custName = mapLM048.get("F3"); // 戶名
		String lineAmt = mapLM048.get("F4"); // 核貸金額
		String loanBal = mapLM048.get("F5"); // 放款本金餘額
		String storeRate = mapLM048.get("F6"); // 利率
		String maturityDate = mapLM048.get("F7"); // 到期日
		String prevPayIntDate = mapLM048.get("F8"); // 繳息迄日
		String groupName = mapLM048.get("F9"); // 同一關係企業或集團名稱
		String groupPercentage = mapLM048.get("F10"); // 同一關係企業或集團核貸金額占淨值比例
		String custPercentage = mapLM048.get("F11"); // 單一授信對象核貸金額占淨值比例

		Map<String, String> mapTmp = new HashMap<>();

		mapTmp.put("Rating", rating);
		mapTmp.put("IndustryItem", industryItem);
		mapTmp.put("CustNo", custNo);
		mapTmp.put("CustName", custName);
		mapTmp.put("LineAmt", lineAmt);
		mapTmp.put("LoanBal", loanBal);
		mapTmp.put("StoreRate", storeRate);
		mapTmp.put("MaturityDate", maturityDate);
		mapTmp.put("PrevPayIntDate", prevPayIntDate);
		mapTmp.put("GroupName", groupName);
		mapTmp.put("GroupPercentage", groupPercentage);
		mapTmp.put("CustPercentage", custPercentage);

		listTmp.add(mapTmp);

		return listTmp;
	}

	private void putDataBElse(Map<String, String> mapTmp, Map<String, String> mapLM048) {

		String lineAmt = mapLM048.get("F4"); // 核貸金額
		String loanBal = mapLM048.get("F5"); // 放款本金餘額

		if (!mapTmp.isEmpty()) {
			BigDecimal oriLineAmt = getBigDecimal(mapTmp.get("LineAmt"));
			lineAmt = getBigDecimal(lineAmt).add(oriLineAmt).toString();
			BigDecimal oriLoanBal = getBigDecimal(mapTmp.get("LoanBal"));
			loanBal = getBigDecimal(loanBal).add(oriLoanBal).toString();
		}

		mapTmp.put("Rating", "B");
		mapTmp.put("IndustryItem", "其他");
		mapTmp.put("CustNo", "");
		mapTmp.put("CustName", "");
		mapTmp.put("LineAmt", lineAmt);
		mapTmp.put("LoanBal", loanBal);
		mapTmp.put("StoreRate", "");
		mapTmp.put("MaturityDate", "");
		mapTmp.put("PrevPayIntDate", "");
		mapTmp.put("GroupName", "");
		mapTmp.put("GroupPercentage", "");
		mapTmp.put("CustPercentage", "");
	}

	private void setValueToExcel(int rowCursor, List<Map<String, String>> list) throws LogicException {

		BigDecimal lineAmtTotal = BigDecimal.ZERO;
		BigDecimal loanBalTotal = BigDecimal.ZERO;

		for (Map<String, String> map : list) {

			// 評等
			String rating = map.get("Rating");
			if (rating.equals("B_Empty")) {
				rating = " ";
			}
			makeExcel.setValue(rowCursor, 1, rating);

			// 行業名稱
			makeExcel.setValue(rowCursor, 2, map.get("IndustryItem"));
			// 戶號
			makeExcel.setValue(rowCursor, 3, map.get("CustNo"));
			// 戶名
			makeExcel.setValue(rowCursor, 4, map.get("CustName"));

			// 核貸金額
			BigDecimal lineAmt = getBigDecimal(map.get("LineAmt"));
			makeExcel.setValue(rowCursor, 5, formatThousand(lineAmt), "#,##0");
			lineAmtTotal = lineAmtTotal.add(lineAmt);

			// 放款本金餘額
			BigDecimal loanBal = getBigDecimal(map.get("LoanBal"));
			makeExcel.setValue(rowCursor, 6, formatThousand(loanBal), "#,##0");
			loanBalTotal = loanBalTotal.add(loanBal);

			// 利率
			BigDecimal storeRate = getBigDecimal(map.get("StoreRate"));
			makeExcel.setValue(rowCursor, 7, storeRate, "#,##0.0000");

			// 到期日
			makeExcel.setValue(rowCursor, 8, showRocDate(map.get("MaturityDate"), 1));
			// 繳息迄日
			makeExcel.setValue(rowCursor, 9, showRocDate(map.get("PrevPayIntDate"), 1));
			// 同一關係企業或集團
			makeExcel.setValue(rowCursor, 10, map.get("GroupName"));

			// 同一關係企業或集團核貸金額占淨值比例
			BigDecimal groupPercentage = getBigDecimal(map.get("GroupPercentage"));
			makeExcel.setValue(rowCursor, 11, groupPercentage, "#,##0.00%");

			// 同一產業單一授信對象核貸金額占放款總餘額比
			BigDecimal custPercentage = getBigDecimal(map.get("CustPercentage"));
			makeExcel.setValue(rowCursor, 12, custPercentage, "#,##0.00%");

			rowCursor++;
		}

		// 印評等產業合計
		// 評等產業合計-核貸金額
		makeExcel.setValue(rowCursor, 5, formatThousand(lineAmtTotal), "#,##0");
		// 評等產業合計-放款本金餘額
		makeExcel.setValue(rowCursor, 6, formatThousand(loanBalTotal), "#,##0");

		// 計算總合計
		totalOfLineAmt = totalOfLineAmt.add(lineAmtTotal);
		totalOfLoanBal = totalOfLoanBal.add(loanBalTotal);
	}

	private final BigDecimal thousand = getBigDecimal("1000");

	private BigDecimal formatThousand(BigDecimal val) {
		return computeDivide(val, thousand, 0);
	}
}
