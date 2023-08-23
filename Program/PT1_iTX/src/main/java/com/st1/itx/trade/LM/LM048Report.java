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
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Component;

import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.db.domain.InnFundApl;
import com.st1.itx.db.service.InnFundAplService;
import com.st1.itx.db.service.springjpa.cm.LM048ServiceImpl;
import com.st1.itx.util.common.MakeExcel;
import com.st1.itx.util.common.MakeReport;
import com.st1.itx.util.common.data.ReportVo;
import com.st1.itx.util.parse.Parse;

@Component
@Scope("prototype")
public class LM048Report extends MakeReport {

	@Autowired
	LM048ServiceImpl lM048ServiceImpl;

	@Autowired
	InnFundAplService sInnFundAplService;

	@Autowired
	MakeExcel makeExcel;

	@Autowired
	Parse parse;

	private List<Map<String, String>> listA = null;
	private List<Map<String, String>> listB = null;
	private List<Map<String, String>> listC = null;

	// 紀錄各行業別的核貸金額加總及放款本金餘額加總
	private Map<String, BigDecimal[]> mapIndustry = null;

	private BigDecimal totalOfLineAmt = BigDecimal.ZERO;
	private BigDecimal totalOfLoanBal = BigDecimal.ZERO;

	private int netValueDataDate = 0;
	private BigDecimal netValue = BigDecimal.ZERO;
	private BigDecimal allLoanBal = BigDecimal.ZERO;
	private BigDecimal entLoanBal = BigDecimal.ZERO;
	private BigDecimal abLoanBal = BigDecimal.ZERO;

	private BigDecimal thousand = BigDecimal.ZERO;

	private void classifyData(List<Map<String, String>> listLM048) {

		listA = new ArrayList<>();
		listB = new ArrayList<>();
		listC = new ArrayList<>();

		Map<String, String> mapBElse = new HashMap<>();

		mapIndustry = new HashMap<>();

		// 先跑一輪計算單一行業小計
		for (Map<String, String> mapLM048 : listLM048) {

			// 這一筆的評等
			String rating = mapLM048.get("F0");
			// 這一筆的行業別
			String industryItem = mapLM048.get("F1");

			BigDecimal lineAmt = getBigDecimal(mapLM048.get("F4")); // 核貸金額
			BigDecimal loanBal = getBigDecimal(mapLM048.get("F5")); // 放款本金餘額

			BigDecimal[] tmpTotal = new BigDecimal[2];

			tmpTotal[0] = lineAmt;
			tmpTotal[1] = loanBal;

			switch (rating) {
			case "A":
			case "B":
			case "B_Empty":
			case "C":
				// 計算單一行業小計
				if (mapIndustry.containsKey(industryItem)) {
					BigDecimal[] oriTotal = mapIndustry.get(industryItem);
					tmpTotal[0] = tmpTotal[0].add(oriTotal[0]);
					tmpTotal[1] = tmpTotal[1].add(oriTotal[1]);
				}
				mapIndustry.put(industryItem, tmpTotal);
				break;
			case "B_Else":
				// 不做事
				break;
			default:
				break;
			}
		}

		String lastRating = "";
		String lastIndustry = "";

		for (Map<String, String> mapLM048 : listLM048) {

			// 這一筆的評等
			String rating = mapLM048.get("F0");
			// 這一筆的行業別
			String industry = mapLM048.get("F1");

			// 若上一筆評等不是空，且 (此筆評等與上一筆評等不同 或 此筆行業別與上一筆行業別不同)
			if (!lastRating.isEmpty() && (!rating.equals(lastRating) || !industry.equals(lastIndustry))) {
				// 將上一筆的行業別加總資料插入上一筆評等對應的清單
				putIndustryTotalData(lastRating, lastIndustry);
			}

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

			// 記錄此筆資料
			lastRating = rating;
			lastIndustry = industry;
		}

		// 迴圈結束補最後一筆的行業別加總資料
		putIndustryTotalData(lastRating, lastIndustry);

		if (!mapBElse.isEmpty()) {
			this.info("mapBElse is not empty.");
			listB.add(mapBElse);
		}

	}

	public void exec(int mfbsdy,TitaVo titaVo) throws LogicException {

		thousand = new BigDecimal("1000");

		List<Map<String, String>> listLM048 = null;
		List<Map<String, String>> listLoanBalQuery = null;

		int inputYearMonth = Integer.parseInt(titaVo.getParam("YearMonth")) + 191100;

		String entLoanBalLimit = titaVo.getParam("EntLoanBalLimit");

		List<InnFundApl> lInnFundApl = new ArrayList<InnFundApl>();
		// 先取得淨值
		Slice<InnFundApl> slInnFundApl = sInnFundAplService.acDateYearEq(mfbsdy, mfbsdy, 0, Integer.MAX_VALUE, titaVo);

		lInnFundApl = slInnFundApl == null ? null : slInnFundApl.getContent();

//		CdVarValue tCdVarValue = cdVarValueService.findYearMonthFirst(inputYearMonth, titaVo);
//
//		if (tCdVarValue != null) {
//			this.info("淨值年月=" + tCdVarValue.getYearMonth());
//			this.info("淨值=" + tCdVarValue.getTotalequity());
//			netValueDataDate = tCdVarValue.getYearMonth();
//			netValue = tCdVarValue.getTotalequity();
//		}
		if (lInnFundApl != null) {

			this.info("淨值日期=" + lInnFundApl.get(0).getAcDate());
			this.info("淨值=" + lInnFundApl.get(0).getStockHoldersEqt());
			netValueDataDate = lInnFundApl.get(0).getAcDate() / 100;
			netValue = lInnFundApl.get(0).getStockHoldersEqt();
		}

		try {
			listLM048 = lM048ServiceImpl.queryDetail(inputYearMonth, entLoanBalLimit, titaVo);

			listLoanBalQuery = lM048ServiceImpl.queryLoanBal(inputYearMonth, titaVo);
		} catch (Exception e) {
			StringWriter errors = new StringWriter();
			e.printStackTrace(new PrintWriter(errors));
			this.error("LM048Report.exec error = " + errors.toString());
		}

		if (listLoanBalQuery != null && !listLoanBalQuery.isEmpty()) {
			for (Map<String, String> loanBalQuery : listLoanBalQuery) {
				String entCode = loanBalQuery.get("F0");
				BigDecimal loanBal = getBigDecimal(loanBalQuery.get("F1"));

				allLoanBal = allLoanBal.add(loanBal);

				if (entCode.equals("1")) {
					entLoanBal = entLoanBal.add(loanBal);
				}
			}
		}

		exportSheet1(inputYearMonth, titaVo, listLM048);

		exportSheet2();

		long sno = makeExcel.close();
		makeExcel.toExcel(sno);
	}

	private void exportSheet1(int inputYearMonth, TitaVo titaVo, List<Map<String, String>> listLM048)
			throws LogicException {

		this.info("LM048Report exportExcel");

		int reportDate = titaVo.getEntDyI() + 19110000;
		String brno = titaVo.getBrno();
		String txcd = "LM048";
		String fileItem = "企業放款風險承擔限額控管表";
		String fileName = "LM048企業放款風險承擔限額控管表";
		String defaultExcel = "LM048_底稿_企業放款風險承擔限額控管表.xlsx";
		String defaultSheet = "明細總表108.04";

		this.info("reportVo open");

		ReportVo reportVo = ReportVo.builder().setRptDate(reportDate).setBrno(brno).setRptCode(txcd)
				.setRptItem(fileItem).build();
		// 開啟報表
		makeExcel.open(titaVo, reportVo, fileName, defaultExcel, defaultSheet);

//		makeExcel.open(titaVo, titaVo.getEntDyI(), titaVo.getKinbr(), "LM048", "企業放款風險承擔限額控管表", "LM048企業放款風險承擔限額控管表",
//				"LM048_底稿_企業放款風險承擔限額控管表.xlsx", "明細總表108.04");

		String outputYearMonth = "";
		if (inputYearMonth > 191100) {
			inputYearMonth -= 191100;
			outputYearMonth = String.valueOf(inputYearMonth);
			outputYearMonth = outputYearMonth.substring(0, 3) + "." + outputYearMonth.substring(3);
		}

		makeExcel.setSheet("明細總表108.04", "明細總表" + outputYearMonth);

		makeExcel.setValue(2, 3, outputYearMonth.replace(".", "年") + "月", "C");

		// 寫入放款總餘額
		makeExcel.setValue(17, 1, outputYearMonth, "C");
		makeExcel.setValue(17, 6, formatThousand(allLoanBal), "#,##0");

		// 寫入淨值
		// 年.月 淨值（核閱數）
		String outputNetValueDataDate = "";
		if (netValueDataDate > 191100) {
			netValueDataDate -= 191100;
			outputNetValueDataDate = String.valueOf(netValueDataDate);
			outputNetValueDataDate = outputNetValueDataDate.substring(0, 3) + "." + outputNetValueDataDate.substring(3);
		}
		makeExcel.setValue(18, 1, outputNetValueDataDate, "C");
		makeExcel.setValue(18, 6, formatThousand(netValue), "#,##0");

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

			// 紀錄AB放款總餘額
			abLoanBal = abLoanBal.add(totalOfLoanBal);

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

			BigDecimal industryPercentage = computeDivide(totalOfLineAmt, netValue, 8);

			makeExcel.setValue(rowCursorTotal, 13, industryPercentage, "#,##0.00%");
		}

	}

	private void exportSheet2() throws LogicException {

		// 更新第二頁數值
		makeExcel.setSheet("風險控管限額標準");

		// A
		// 淨值30%
		makeExcel.setValue(4, 3, formatThousand(netValue.multiply(getBigDecimal("0.3"))));
		// 淨值20%
		makeExcel.setValue(4, 4, formatThousand(netValue.multiply(getBigDecimal("0.2"))));
		// 放款總額30%
		makeExcel.setValue(4, 5, formatThousand(allLoanBal.multiply(getBigDecimal("0.3"))));

		// B
		// 淨值25%
		makeExcel.setValue(8, 3, formatThousand(netValue.multiply(getBigDecimal("0.25"))));
		// 淨值15%
		makeExcel.setValue(8, 4, formatThousand(netValue.multiply(getBigDecimal("0.15"))));
		// 放款總額25%
		makeExcel.setValue(8, 5, formatThousand(allLoanBal.multiply(getBigDecimal("0.25"))));

		// A級與B級行業別之放款總餘額
		makeExcel.setValue(11, 5, formatThousand(abLoanBal));

		// 不得低於企業放款總餘額之75%
		makeExcel.setValue(15, 5, formatThousand(entLoanBal.multiply(getBigDecimal("0.25"))));

		// C
		// 淨值20%
		makeExcel.setValue(17, 3, formatThousand(netValue.multiply(getBigDecimal("0.2"))));
		// 淨值10%
		makeExcel.setValue(17, 4, formatThousand(netValue.multiply(getBigDecimal("0.1"))));
		// 放款總額20%
		makeExcel.setValue(17, 5, formatThousand(allLoanBal.multiply(getBigDecimal("0.2"))));

	}

	private BigDecimal formatThousand(BigDecimal val) {
		return computeDivide(val, thousand, 0);
	}

	/**
	 * 合併一批資料的儲存格
	 * 
	 * @param mergeRowRange 一批資料中需要合併的起訖行數
	 * @param type          種類
	 */
	private void mergeColumns(Map<String, int[]> mergeRowRange, int type) {

		mergeRowRange.forEach((k, v) -> {
//			this.info("mergeColumns k " + k);
//			this.info("mergeColumns v[0] " + v[0]);
//			this.info("mergeColumns v[1] " + v[1]);

			if (v[0] < v[1]) {
				if (type == 1) {
					// 評等
					makeExcel.setMergedRegion(v[0], v[1], 1, 1);
					// 行業別名稱
					makeExcel.setMergedRegion(v[0], v[1], 2, 2);
				}
				if (type == 2) {
					// 戶號
					makeExcel.setMergedRegion(v[0], v[1], 3, 3);
					// 戶名
					makeExcel.setMergedRegion(v[0], v[1], 4, 4);
					// 同一產業單一授信對象核貸金額占放款總餘額比
					makeExcel.setMergedRegion(v[0], v[1], 12, 12);
				}
				if (type == 3) {
					// 同一關係企業或集團
					makeExcel.setMergedRegion(v[0], v[1], 10, 10);
					// 同一關係企業或集團核貸金額占淨值比例
					makeExcel.setMergedRegion(v[0], v[1], 11, 11);
				}
			}
		});

	}

	@Override
	public void printTitle() {

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

	private void putIndustryTotalData(String lastRating, String lastIndustry) {

		BigDecimal[] industryTotal = mapIndustry.get(lastIndustry);

		Map<String, String> mapTmp = new HashMap<>();

		mapTmp.put("Rating", lastRating);
		mapTmp.put("IndustryItem", lastIndustry);
		mapTmp.put("CustNo", "");
		mapTmp.put("CustName", "單一行業小計");

		if (industryTotal == null) {
			mapTmp.put("LineAmt", "0");
			mapTmp.put("LoanBal", "0");
		} else {
			mapTmp.put("LineAmt", industryTotal[0] == null ? "0" : industryTotal[0].toString());
			mapTmp.put("LoanBal", industryTotal[1] == null ? "0" : industryTotal[1].toString());
		}
		mapTmp.put("StoreRate", "");
		mapTmp.put("MaturityDate", "");
		mapTmp.put("PrevPayIntDate", "");
		mapTmp.put("GroupName", "");
		mapTmp.put("GroupPercentage", "");
		mapTmp.put("CustPercentage", "");

		switch (lastRating) {
		case "A":
			listA.add(mapTmp);
			break;
		case "B":
		case "B_Empty":
			listB.add(mapTmp);
			break;
		case "B_Else":
			break;
		case "C":
			listC.add(mapTmp);
			break;
		default:
			break;
		}
	}

	private void setValueToExcel(int rowCursor, List<Map<String, String>> list) throws LogicException {

		BigDecimal lineAmtTotal = BigDecimal.ZERO;
		BigDecimal loanBalTotal = BigDecimal.ZERO;

		Map<String, int[]> mergeRowRange1 = new HashMap<>();
		Map<String, int[]> mergeRowRange2 = new HashMap<>();
		Map<String, int[]> mergeRowRange3 = new HashMap<>();

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
			String custNo = map.get("CustNo");
			makeExcel.setValue(rowCursor, 3, custNo);

			// 戶名
			String custName = map.get("CustName");
			if (custName.equals("單一行業小計")) {
				makeExcel.setValue(rowCursor, 4, custName, "R");
			} else {
				makeExcel.setValue(rowCursor, 4, custName);
			}

			// 核貸金額
			BigDecimal lineAmt = getBigDecimal(map.get("LineAmt"));
			makeExcel.setValue(rowCursor, 5, formatThousand(lineAmt), "#,##0");
			if (map.get("CustNo").isEmpty()) {
				// 戶號為空才加總
				lineAmtTotal = lineAmtTotal.add(lineAmt);
			}

			// 放款本金餘額
			BigDecimal loanBal = getBigDecimal(map.get("LoanBal"));
			makeExcel.setValue(rowCursor, 6, formatThousand(loanBal), "#,##0");
			if (map.get("CustNo").isEmpty()) {
				// 戶號為空才加總
				loanBalTotal = loanBalTotal.add(loanBal);
			}

			// 利率
			BigDecimal storeRate = getBigDecimal(map.get("StoreRate"));
			if (!custNo.isEmpty()) {
				makeExcel.setValue(rowCursor, 7, storeRate, "#,##0.0000");
			}

			// 到期日
			makeExcel.setValue(rowCursor, 8, showRocDate(map.get("MaturityDate"), 1));

			// 繳息迄日
			makeExcel.setValue(rowCursor, 9, showRocDate(map.get("PrevPayIntDate"), 1));

			// 同一關係企業或集團
			String groupName = map.get("GroupName");
			makeExcel.setValue(rowCursor, 10, groupName);

			// 同一關係企業或集團核貸金額占淨值比例
			if (!groupName.isEmpty()) {
				BigDecimal groupPercentage = getBigDecimal(map.get("GroupPercentage"));
				makeExcel.setValue(rowCursor, 11, groupPercentage, "#,##0.00%");
			}

			// 同一產業單一授信對象核貸金額占放款總餘額比
			if (!custNo.isEmpty()) {
				BigDecimal custPercentage = getBigDecimal(map.get("CustPercentage"));
				makeExcel.setValue(rowCursor, 12, custPercentage, "#,##0.00%");
			}

			// 單一行業核貸金額佔淨值比
			if (custNo.isEmpty()) {
				BigDecimal industryPercentage = computeDivide(lineAmt, netValue, 8);
				makeExcel.setValue(rowCursor, 13, industryPercentage, "#,##0.00%");
			}

			// 取得key1
			String key1 = map.get("IndustryItem");

			if (!key1.isEmpty()) {
				int[] rowRange = new int[2];

				if (mergeRowRange1.containsKey(key1)) {

					rowRange = mergeRowRange1.get(key1);

					if (rowRange[0] > rowCursor) {
						rowRange[0] = rowCursor;
					}
					if (rowRange[1] < rowCursor) {
						rowRange[1] = rowCursor;
					}
				} else {
					rowRange[0] = rowCursor;
					rowRange[1] = rowCursor;
				}
				this.info("rowRange[0] = " + rowRange[0]);
				this.info("rowRange[1] = " + rowRange[1]);

				mergeRowRange1.put(key1, rowRange);
			}

			// 取得key2
			String key2 = map.get("CustName");

			if (!key2.equals("單一行業小計")) {
				int[] rowRange = new int[2];

				if (mergeRowRange2.containsKey(key2)) {

					rowRange = mergeRowRange2.get(key2);

					if (rowRange[0] > rowCursor) {
						rowRange[0] = rowCursor;
					}
					if (rowRange[1] < rowCursor) {
						rowRange[1] = rowCursor;
					}
				} else {
					rowRange[0] = rowCursor;
					rowRange[1] = rowCursor;
				}
				this.info("rowRange[0] = " + rowRange[0]);
				this.info("rowRange[1] = " + rowRange[1]);

				mergeRowRange2.put(key2, rowRange);
			}

			// 取得key3 (行業別+同一關係企業或集團相同時才合併儲存格)
			String key3 = key1 + map.get("GroupName");

			if (!key3.isEmpty()) {
				int[] rowRange = new int[2];

				if (mergeRowRange3.containsKey(key3)) {

					rowRange = mergeRowRange3.get(key3);

					if (rowRange[0] > rowCursor) {
						rowRange[0] = rowCursor;
					}
					if (rowRange[1] < rowCursor) {
						rowRange[1] = rowCursor;
					}
				} else {
					rowRange[0] = rowCursor;
					rowRange[1] = rowCursor;
				}
				this.info("rowRange[0] = " + rowRange[0]);
				this.info("rowRange[1] = " + rowRange[1]);

				mergeRowRange3.put(key3, rowRange);
			}

			rowCursor++;
		}

		// 合併儲存格
//		if (mergeRowRange1 != null && !mergeRowRange1.isEmpty()) {
//			mergeColumns(mergeRowRange1, 1);
//		}
//		if (mergeRowRange2 != null && !mergeRowRange2.isEmpty()) {
//			mergeColumns(mergeRowRange2, 2);
//		}
//		if (mergeRowRange3 != null && !mergeRowRange3.isEmpty()) {
//			mergeColumns(mergeRowRange3, 3);
//		}
		// 印評等產業合計
		// 評等產業合計-核貸金額
		makeExcel.setValue(rowCursor, 5, formatThousand(lineAmtTotal), "#,##0");
		// 評等產業合計-放款本金餘額
		makeExcel.setValue(rowCursor, 6, formatThousand(loanBalTotal), "#,##0");

		BigDecimal industryPercentage = computeDivide(lineAmtTotal, netValue, 8);

		makeExcel.setValue(rowCursor, 13, industryPercentage, "#,##0.00%");

		// 計算總合計
		totalOfLineAmt = totalOfLineAmt.add(lineAmtTotal);
		totalOfLoanBal = totalOfLoanBal.add(loanBalTotal);
	}
}
