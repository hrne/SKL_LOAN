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
	MakeExcel makeExcel;

	@Autowired
	Parse parse;

	private int iAcDate = 0;
	// 輸出行數減一
	// 每個評等的起始列
	private int startRowA = 4;
	private int startRowB = 10;
	private int startRowC = 16;
	private int netRowA = 4;
	private int netRowB = 1;
	private int netRowC = 16;
	// 每個評等的結束列
	private int endRowA = 0;
	private int endRowB = 0;
	private int endRowC = 0;

	// 產業起始列
	private int sDustRow = 1;
	private int eDustRow = 1;

	// 戶號起始列
	private int sCustNoRow = 1;
	private int eCustNoRow = 1;

	// 同一企業起始列
	private int sGroupRow = 1;
	private int eGroupRow = 1;

	private BigDecimal tLoanBalTotal = BigDecimal.ZERO;
	private BigDecimal tLineAmtTotal = BigDecimal.ZERO;
	private BigDecimal tAvailableBalTotal = BigDecimal.ZERO;

	private BigDecimal tLineAmtCalculate = BigDecimal.ZERO;
	private BigDecimal tLoanBalCalculate = BigDecimal.ZERO;
	private BigDecimal tAvailableBalCalculate = BigDecimal.ZERO;

	private List<Integer> lIndustryRatingListA = new ArrayList<Integer>();
	private List<Integer> lIndustryRatingListB = new ArrayList<Integer>();
	private List<Integer> lIndustryRatingListC = new ArrayList<Integer>();

	private List<Map<String, String>> listLineAmt = new ArrayList<Map<String, String>>();

	public boolean exec(TitaVo titaVo) throws LogicException {
		this.info("LM048Report exec ...");

		int iYearMonth = Integer.parseInt(titaVo.getParam("YearMonth")) + 191100;

		iAcDate = titaVo.getEntDyI() + 19110000;

		List<Map<String, String>> lLM048List = new ArrayList<Map<String, String>>();
		List<Map<String, String>> lLM048List2 = new ArrayList<Map<String, String>>();
		List<Map<String, String>> lLM048List3 = new ArrayList<Map<String, String>>();

		// 明細
		try {
			lLM048List = lM048ServiceImpl.queryDetail(iYearMonth, titaVo);
		} catch (Exception e) {
			StringWriter errors = new StringWriter();
			e.printStackTrace(new PrintWriter(errors));
			this.error("lM048ServiceImpl.queryDetail error = " + errors.toString());
		}
		// 可運用資金及放款總額
		try {
			lLM048List2 = lM048ServiceImpl.queryLoanBal(iYearMonth, titaVo);
		} catch (Exception e) {
			StringWriter errors = new StringWriter();
			e.printStackTrace(new PrintWriter(errors));
			this.error("lM048ServiceImpl.queryLoanBal error = " + errors.toString());
		}

		try {
			listLineAmt = lM048ServiceImpl.groupLineAmt(iYearMonth, titaVo);
		} catch (Exception e) {
			StringWriter errors = new StringWriter();
			e.printStackTrace(new PrintWriter(errors));
			this.error("lM048ServiceImpl.groupLineAmt error = " + errors.toString());
		}

		// 明細
		try {
			lLM048List3 = lM048ServiceImpl.riskLimit(iAcDate, titaVo);
		} catch (Exception e) {
			StringWriter errors = new StringWriter();
			e.printStackTrace(new PrintWriter(errors));
			this.error("lM048ServiceImpl.riskLimit error = " + errors.toString());
		}

		exportExcel(lLM048List, lLM048List2, lLM048List3, iYearMonth, titaVo);

		return true;

	}

	private void exportExcel(List<Map<String, String>> lLM048List, List<Map<String, String>> lLM048List2,
			List<Map<String, String>> lLM048List3, int iYearMonth, TitaVo titaVo) throws LogicException {
		this.info("LM048Report exportExcel");

		// 去年底日
		int lastYear12AcDate = iAcDate / 10000 * 10000 + 1231;

		String rocDate = this.showRocDate(iAcDate, 6);
		String rocLastYear12YDate = this.showRocDate(lastYear12AcDate, 6);
		String rocDateCh = this.showRocDate(iAcDate, 0);

		String rocYYYMM = ((iAcDate - 19110000) / 100) + "";

		int reportDate = titaVo.getEntDyI() + 19110000;
		String brno = titaVo.getBrno();
		String txcd = "LM048";
		String fileItem = "企業放款風險承擔限額控管表" + rocYYYMM;
		String fileName = "LM048_企業放款風險承擔限額控管表.xlsx" + rocYYYMM;
		String defaultExcel = "LM048_底稿_企業放款風險承擔限額控管表.xlsx";
		String defaultSheet = "明細總表";
		String reNameSheet = "明細總表" + rocDate.substring(0, 6);

		ReportVo reportVo = ReportVo.builder().setRptDate(reportDate).setBrno(brno).setRptCode(txcd)
				.setRptItem(fileItem).build();
		// 開啟報表
		makeExcel.open(titaVo, reportVo, fileName, defaultExcel, defaultSheet);
		makeExcel.setSheet(defaultSheet, reNameSheet);
		// 表頭日期
		makeExcel.setValue(2, 8, rocDateCh, "C");

		if (lLM048List.isEmpty() || lLM048List == null) {
			makeExcel.setValue(5, 2, "本日無資料");
		} else {
			checkIndustryRatingAndInsertRow(lLM048List);

			this.info("startRowA = " + startRowA);
			this.info("startRowB = " + startRowB);
			this.info("startRowC = " + startRowC);

			int tInDustryCode = 0;
			String tInDustryRating = "";
			String tInDustryItem = "";
			int tCustNo = 0;
			String tCustName = "";
			String tmpCustName = "";
			String tRecycleCode = "";
			BigDecimal tAvailableBal = BigDecimal.ZERO;
			BigDecimal tLineAmt = BigDecimal.ZERO;
			BigDecimal tLoanBal = BigDecimal.ZERO;
			BigDecimal tStoreRate = BigDecimal.ZERO;
			String tMaturityDate = "";
			String tPrevPayIntDate = "";
//			String tGroupName = "";

			int tCustNoMain = 0;

			int tmpInDustryCode = 0;
			int tmpCustNoMain = 0;
			int count = 0;
			int row = 0;
			int tmpCustNo = 0;

			int tInDustryCodeNext = 0;
			String tInDustryRatingNext = "";

			BigDecimal tLoanTotal = BigDecimal.ZERO;
			BigDecimal tNetWorth = BigDecimal.ZERO;

			// 先取得 放款總餘額(含催收款) 和 淨值
			for (Map<String, String> r2 : lLM048List2) {

				if ("LoanBal".equals(r2.get("Name"))) {
					tLoanTotal = getBigDecimal(r2.get("LoanBal"));
				}
				if ("NetWorth".equals(r2.get("Name"))) {
					tNetWorth = getBigDecimal(r2.get("LoanBal"));
				}

			}

			for (Map<String, String> r : lLM048List) {

				tInDustryCode = r.get("IndustryCode") == null ? 0 : parse.stringToInteger(r.get("IndustryCode"));
				tInDustryRating = r.get("IndustryRating");
				tInDustryItem = r.get("IndustryItem");

				if (lLM048List.size() > count + 1) {
					tInDustryCodeNext = lLM048List.get(count + 1).get("IndustryCode") == null ? 0
							: parse.stringToInteger(lLM048List.get(count + 1).get("IndustryCode"));

					tInDustryRatingNext = lLM048List.get(count + 1).get("IndustryRating");
				}

				row = checkIndustryRatingtAddRow(tInDustryRating, false);

				// 不同產業 && 同時判斷不同產業的合併
				if (tmpInDustryCode != tInDustryCode) {
					tmpInDustryCode = tInDustryCode;
					if (count > 0 && sDustRow - eDustRow != 0) {
						this.info("sDustRow =" + sDustRow);
						this.info("eDustRow =" + eDustRow);
						makeExcel.setMergedRegionValue(sDustRow, eDustRow, 1, 1, tInDustryRating, "C");
						makeExcel.setMergedRegionValue(sDustRow, eDustRow, 2, 2, tInDustryItem, "C");
					}
					sDustRow = row;// 37
					eDustRow = row;// 37

					makeExcel.setValue(row, 1, tInDustryRating, "C");
					makeExcel.setValue(row, 2, tInDustryItem, "L");
				} else {
					eDustRow = row + 3;// 64
				}

				count++;

				tCustNo = r.get("CustNo") == null ? 0 : parse.stringToInteger(r.get("CustNo"));
				tCustName = r.get("CustName") == null ? "" : r.get("CustName");

				if (tCustNo == 0) {
					sDustRow = 1;
					eDustRow = 1;
					continue;
				}

				if (tmpCustNo != tCustNo) {

					if (count > 0 && sCustNoRow - eCustNoRow != 0) {
						this.info("sCustNoRow =" + sCustNoRow);
						this.info("eCustNoRow =" + eCustNoRow);
						makeExcel.setMergedRegionValue(sCustNoRow, eCustNoRow, 3, 3, tmpCustNo, "C");
						makeExcel.setMergedRegionValue(sCustNoRow, eCustNoRow, 4, 4, tmpCustName, "C");
					}
					tmpCustNo = tCustNo;
					tmpCustName = tCustName;

					sCustNoRow = row;// 37
					eCustNoRow = row;// 37

					makeExcel.setValue(row, 3, tCustNo, "C");
					makeExcel.setValue(row, 4, tCustName, "L");

				} else {
					eCustNoRow = row;
				}

				tRecycleCode = r.get("RecycleCode");
				makeExcel.setValue(row, 5, tRecycleCode, "C");

				tLineAmt = getBigDecimal(r.get("LineAmt"));
				makeExcel.setValue(row, 6, tLineAmt, "#,##0", "L");
				tLineAmtCalculate = tLineAmtCalculate.add(tLineAmt);

				tLoanBal = getBigDecimal(r.get("LoanBal"));
				makeExcel.setValue(row, 7, tLoanBal, "#,##0", "L");
				tLoanBalCalculate = tLoanBalCalculate.add(tLoanBal);

				tAvailableBal = getBigDecimal(r.get("AvailableBal"));
				makeExcel.setValue(row, 8, tAvailableBal, "#,##0", "L");
				tAvailableBalCalculate = tAvailableBalCalculate.add(tAvailableBal);

				tStoreRate = getBigDecimal(r.get("StoreRate"));
				makeExcel.setValue(row, 9, tStoreRate, "0.0000", "C");

				tMaturityDate = this.showBcDate(r.get("MaturityDate"), 0);
				makeExcel.setValue(row, 10, tMaturityDate, "C");

				tPrevPayIntDate = this.showBcDate(r.get("PrevPayIntDate"), 0);
				makeExcel.setValue(row, 11, tPrevPayIntDate, "C");

				makeExcel.setValue(row, 14, tLineAmt.compareTo(BigDecimal.ZERO) == 0 ? BigDecimal.ZERO
						: tLineAmt.divide(tNetWorth, 4, BigDecimal.ROUND_HALF_UP), "0.00%", "C");

				tCustNoMain = r.get("CustNoMain") == null ? 0 : parse.stringToInteger(r.get("CustNoMain"));

				// 不同集團
				if (tmpCustNoMain != tCustNoMain) {

					BigDecimal tmpLineAmt = groupLineAmt(tmpCustNoMain);
					String tmpGroupName = groupName(tmpCustNoMain);
					boolean isGroup = tmpLineAmt.compareTo(BigDecimal.ZERO) > 0;

					this.info("1.isGroup = " + isGroup);

					if (count > 0 && sGroupRow - eGroupRow != 0) {

						this.info("sGroupRow =" + sGroupRow);
						this.info("eGroupRow =" + eGroupRow);

						if (isGroup) {

							makeExcel.setMergedRegionValue(sGroupRow, eGroupRow, 12, 12, tmpGroupName, "C");
							makeExcel.setMergedRegionValue(sGroupRow, eGroupRow, 13, 13,
									tmpLineAmt.divide(tNetWorth, 4, BigDecimal.ROUND_HALF_UP), "0.00%", "C");

							isGroup = false;

						}

					}

					tmpCustNoMain = tCustNoMain;

					if (isGroup) {
						makeExcel.setValue(eGroupRow, 12, tmpGroupName, "C");
						makeExcel.setValue(eGroupRow, 13, tmpLineAmt.divide(tNetWorth, 4, BigDecimal.ROUND_HALF_UP),
								"C");
					}

					sGroupRow = row;// 37
					eGroupRow = row;// 37

				} else {

					eGroupRow = row;
				}

				// CustMain需有值
				if (tCustNoMain != 0) {
					// 當前產業與下一筆的產業不同 或 最後一筆時 需輸出單一行業合計
					if (tInDustryCode != tInDustryCodeNext || lLM048List.size() == count) {
						tLineAmtTotal = tLineAmtTotal.add(tLineAmtCalculate);
						tLoanBalTotal = tLoanBalTotal.add(tLoanBalCalculate);
						tAvailableBalTotal = tAvailableBalTotal.add(tAvailableBalCalculate);

						row = checkIndustryRatingtAddRow(tInDustryRating, true);
						// 輸出單一合計
						makeExcelSetValCal(row);

						// 當前評等與下一筆不一樣 或 最後一筆時 需輸出評等合計
						this.info("Count =" + count);
						if (!tInDustryRating.equals(tInDustryRatingNext) || lLM048List.size() == count) {

							this.info("change");
							row++;
							makeExcel.setValue(row, 6, tLineAmtTotal, "#,##0", "R");
							makeExcel.setValue(row, 7, tLoanBalTotal, "#,##0", "R");
							makeExcel.setValue(row, 8, tAvailableBalCalculate, "#,##0", "R");
							// 單一行業核貸金額占淨值比
							String sDustryRate = "F" + row + "/" + "E" + (endRowC + 9);
							makeExcel.setFormula(row, 15, BigDecimal.ZERO, sDustryRate, "0.00%");

							tLineAmtTotal = BigDecimal.ZERO;
							tLoanBalTotal = BigDecimal.ZERO;
							tAvailableBalCalculate = BigDecimal.ZERO;
						}

					}

					tLineAmtCalculate = BigDecimal.ZERO;
					tLoanBalCalculate = BigDecimal.ZERO;
					tAvailableBalCalculate = BigDecimal.ZERO;
				}
			}

			// 各評等公式
			String formulaInDustryRatingA = sumInDustryRatingFormulua("A", "F");
			String formulaInDustryRatingB = sumInDustryRatingFormulua("B", "F");
			String formulaInDustryRatingC = sumInDustryRatingFormulua("C", "F");

			// 各評等合計
			// 核貸金額
			makeExcel.setFormula(endRowA, 6, BigDecimal.ZERO, formulaInDustryRatingA, "#,##0");
			makeExcel.setFormula(endRowB, 6, BigDecimal.ZERO, formulaInDustryRatingB, "#,##0");
			makeExcel.setFormula(endRowC, 6, BigDecimal.ZERO, formulaInDustryRatingC, "#,##0");
			// 放款金額
			makeExcel.setFormula(endRowA, 7, BigDecimal.ZERO, sumInDustryRatingFormulua("A", "G"), "#,##0");
			makeExcel.setFormula(endRowB, 7, BigDecimal.ZERO, sumInDustryRatingFormulua("B", "G"), "#,##0");
			makeExcel.setFormula(endRowC, 7, BigDecimal.ZERO, sumInDustryRatingFormulua("C", "G"), "#,##0");
			// 可用餘額
			makeExcel.setFormula(endRowA, 8, BigDecimal.ZERO, sumInDustryRatingFormulua("A", "H"), "#,##0");
			makeExcel.setFormula(endRowB, 8, BigDecimal.ZERO, sumInDustryRatingFormulua("B", "H"), "#,##0");
			makeExcel.setFormula(endRowC, 8, BigDecimal.ZERO, sumInDustryRatingFormulua("C", "H"), "#,##0");

			// 總合計
			row = endRowC + 1;
			String formuLineAmt = "SUM(F" + endRowA + ",F" + endRowB + ",F" + endRowC + ")";
			makeExcel.setFormula(row, 6, BigDecimal.ZERO, formuLineAmt, "#,##0");
			String formuLoanBal = "SUM(G" + endRowA + ",G" + endRowB + ",G" + endRowC + ")";
			makeExcel.setFormula(row, 7, BigDecimal.ZERO, formuLoanBal, "#,##0");
			String formuAvailableBal = "SUM(H" + endRowA + ",H" + endRowB + ",H" + endRowC + ")";
			makeExcel.setFormula(row, 8, BigDecimal.ZERO, formuAvailableBal, "#,##0");

			// 單一行業核貸金額占淨值比 合計
			String pbrFornulaA = "F" + endRowA + "/" + "E" + (row + 8);
			String pbrFornulaB = "F" + endRowB + "/" + "E" + (row + 8);
			String pbrFornulaC = "F" + endRowC + "/" + "E" + (row + 8);
			String pbrFornula = "F" + row + "/" + "E" + (row + 8);

			makeExcel.setFormula(endRowA, 15, BigDecimal.ZERO, pbrFornulaA, "0.00%");
			makeExcel.setFormula(endRowB, 15, BigDecimal.ZERO, pbrFornulaB, "0.00%");
			makeExcel.setFormula(endRowC, 15, BigDecimal.ZERO, pbrFornulaC, "0.00%");
			makeExcel.setFormula(row, 15, BigDecimal.ZERO, pbrFornula, "0.00%");

			// 放款總餘額
			row = row + 7;
			makeExcel.setValue(row, 1, rocDate);
			makeExcel.setValue(row, 5, tLoanTotal, "#,##0", "R");
			// 淨值
			row = row + 1;
			makeExcel.setValue(row, 1, rocLastYear12YDate);
			makeExcel.setValue(row, 5, tNetWorth, "#,##0", "R");

			// 風險控管限額表準(表格右側)
			if (lLM048List3.size() != 0) {
				riskForm(lLM048List3.get(0), row);
			}

		}

		long sno = makeExcel.close();
		makeExcel.toExcel(sno);
	}

	/**
	 * 檢查行業評等 並 插入相應數量行數
	 * 
	 * 1.調整各評等初始的行數 2.插入相應評等的行數
	 * 
	 * 
	 */
	private void checkIndustryRatingAndInsertRow(List<Map<String, String>> lLM048List) throws LogicException {

		int rowA = 0;
		int rowB = 0;
		int rowC = 0;
		int rowTemp = 0;

		int tInDustryCode = 0;
		int tInDustryCodeNext = 0;
		int custNoMain = 0;
		int count = 0;

		for (Map<String, String> r : lLM048List) {

			String tInDustryRating = r.get("IndustryRating");

			tInDustryCode = r.get("IndustryCode") == null ? 0 : parse.stringToInteger(r.get("IndustryCode"));

			if (lLM048List.size() > count + 1) {
				tInDustryCodeNext = lLM048List.get(count + 1).get("IndustryCode") == null ? 0
						: parse.stringToInteger(lLM048List.get(count + 1).get("IndustryCode"));
			}

			count++;
			custNoMain = r.get("CustNoMain") == null ? 0 : parse.stringToInteger(r.get("CustNoMain"));

			if (custNoMain == 0) {
				rowTemp = 0;
			} else {

				if (tInDustryCode == tInDustryCodeNext) {
					rowTemp = 0;
				} else {
					rowTemp = 1;
				}
			}

			switch (tInDustryRating) {
			case "A":
				rowA = rowA + 1 + rowTemp;
				break;
			case "B":
				rowB = rowB + 1 + rowTemp;
				break;
			case "C":
				rowC = rowC + 1 + rowTemp;
				break;
			default:
				break;
			}
		}
		rowA = rowA - 5;
		rowB = rowB - 5;
		rowC = rowC - 5;

		this.info("rowA = " + rowA);
		this.info("rowB = " + rowB);
		this.info("rowC = " + rowC);
		// 判斷每個評等的資料數分別插入的行數
		if (rowC > 0) {
			makeExcel.setShiftRow(18, rowC);
		}
		if (rowB > 0) {
			startRowC = startRowC + rowB;
			makeExcel.setShiftRow(12, rowB);
		}
		if (rowA > 0) {
			startRowC = startRowC + rowA;
			startRowB = startRowB + rowA;
			makeExcel.setShiftRow(6, rowA);
		}

		netRowA = startRowA + 1;
		netRowB = startRowB + 1;
		netRowC = startRowC + 1;

		// 再判斷一次是因為要確定每個評等合計的行數為何
		if (rowA >= 0) {
			endRowA = startRowA + 6 + rowA;
		} else {
			endRowA = startRowA + 6;
		}
		if (rowB >= 0) {
			endRowB = startRowB + 6 + rowB;
		} else {
			endRowB = startRowB + 6;
		}
		if (rowC >= 0) {
			endRowC = startRowC + 6 + rowC;
		} else {
			endRowC = startRowC + 6;
		}

		this.info("endRowA = " + endRowA);
		this.info("endRowB = " + endRowB);
		this.info("endRowC = " + endRowC);
	}

	/**
	 * 檢查行業評等 並 確定當前行數
	 * 
	 * @param tInDustryRating 評等
	 * @param isCal           是否計算
	 */
	private Integer checkIndustryRatingtAddRow(String tInDustryRating, boolean isCal) throws LogicException {
		int row = 0;

		switch (tInDustryRating) {
		case "A":
			startRowA++;
			row = startRowA;

			if (isCal) {
				lIndustryRatingListA.add(row);
			}
			break;
		case "B":
			startRowB++;
			row = startRowB;
			if (isCal) {
				lIndustryRatingListB.add(row);
			}
			break;
		case "C":
			startRowC++;
			row = startRowC;
			if (isCal) {
				lIndustryRatingListC.add(row);
			}
			break;
		default:
			break;
		}

		return row;
	}

	/**
	 * 
	 * 輸出 單一行業小計
	 * 
	 */
	private void makeExcelSetValCal(int trow) {
		try {
			makeExcel.setMergedRegionValue(trow, trow, 3, 4, "單一行業小計", "R");
			makeExcel.setValue(trow, 6, tLineAmtCalculate, "#,##0", "R");
			makeExcel.setValue(trow, 7, tLoanBalCalculate, "#,##0", "R");
			makeExcel.setValue(trow, 8, tAvailableBalTotal, "#,##0", "R");
		} catch (LogicException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 合計評等行業別
	 * 
	 * @param tInDustryRating 評等
	 * @param col             欄位
	 * 
	 */
	private String sumInDustryRatingFormulua(String tInDustryRating, String col) {
		String result = "";

		result = "SUM(";
		switch (tInDustryRating) {
		case "A":
			for (int row : lIndustryRatingListA) {
				result = result + col + row + ",";
			}
			break;
		case "B":
			for (int row : lIndustryRatingListB) {
				result = result + col + row + ",";
			}
			break;
		case "C":
			for (int row : lIndustryRatingListC) {
				result = result + col + row + ",";
			}
			break;
		default:
			break;
		}

		// 去除最後一個逗號
		if (result.length() > 4) {
			result = result.substring(0, result.length() - 1) + ")";
		} else {
			result = "";
		}

		this.info("result = " + result);

		return result;
	}

	private BigDecimal groupLineAmt(int custNoGroup) throws LogicException {
		BigDecimal result = BigDecimal.ZERO;

		this.info("custNoGroup = " + custNoGroup);

		if (custNoGroup == 0) {
			return result;
		}

		if (listLineAmt.size() == 0) {

		} else {

			for (Map<String, String> r : listLineAmt) {

				if (custNoGroup == parse.stringToInteger(r.get("CustNoMain"))) {
					result = getBigDecimal(r.get("ToTalLineAmt"));
					continue;
				}
			}
		}

		return result;
	}

	private String groupName(int custNoGroup) throws LogicException {
		String result = "";

		this.info("custNoGroup = " + custNoGroup);

		if (custNoGroup == 0) {
			return result;
		}

		if (listLineAmt.size() == 0) {

		} else {

			for (Map<String, String> r : listLineAmt) {

				if (custNoGroup == parse.stringToInteger(r.get("CustNoMain"))) {
					result = r.get("CustNameMain");
					continue;
				}
			}
		}

		return result;
	}

	/*
	 * 待新交易[維護評等]處理
	 */
	private void riskForm(Map<String, String> result, int netRow) throws LogicException {

		String percent1 = result.get("LimitRateA1") + "%";
		String percent2 = result.get("LimitRateA2") + "%";
		String percent3 = result.get("LimitRateA3") + "%";

		String netRate1 = "淨值 " + percent1;
		String netRate2 = "淨值 " + percent2;
		String netRate3 = "淨值 " + percent3;

		String netRateFormula1 = "E" + netRow + "*" + percent1;
		String netRateFormula2 = "E" + netRow + "*" + percent2;
		String netRateFormula3 = "E" + netRow + "*" + percent3;

		String limitLoan1 = result.get("LimitLoanA1");
		String limitLoan2 = result.get("LimitLoanA2");
		String limitLoan3 = result.get("LimitLoanA3");

		String mark1 = "0".equals(limitLoan1) ? "" : "放款總額\n不得逾" + limitLoan1 + "億元";
		String mark2 = "0".equals(limitLoan2) ? "" : "放款總額\n不得逾" + limitLoan2 + "億元";
		String mark3 = "0".equals(limitLoan3) ? "" : "放款總額\n不得逾" + limitLoan3 + "億元";

		makeExcel.setValue(netRowA + 1, 17, netRate1, "C");
		makeExcel.setValue(netRowA + 1, 18, netRate2, "C");
		makeExcel.setValue(netRowA + 1, 19, netRate3, "C");

		makeExcel.setFormula(netRowA + 2, 17, BigDecimal.ZERO, netRateFormula1, "#,##0");
		makeExcel.setFormula(netRowA + 2, 18, BigDecimal.ZERO, netRateFormula2, "#,##0");
		makeExcel.setFormula(netRowA + 2, 19, BigDecimal.ZERO, netRateFormula3, "#,##0");

		makeExcel.setMergedRegionValue(netRowA + 3, netRowA + 4, 17, 17, mark1, "C");
		makeExcel.setMergedRegionValue(netRowA + 3, netRowA + 4, 18, 18, mark2, "C");
		makeExcel.setMergedRegionValue(netRowA + 3, netRowA + 4, 19, 19, mark3, "C");

		makeExcel.formulaCaculate(netRowA + 3, 17);
		makeExcel.formulaCaculate(netRowA + 3, 18);

		percent1 = result.get("LimitRateB1") + "%";
		percent2 = result.get("LimitRateB2") + "%";
		percent3 = result.get("LimitRateB3") + "%";

		netRate1 = "淨值 " + percent1;
		netRate2 = "淨值 " + percent2;
		netRate3 = "淨值 " + percent3;

		netRateFormula1 = "E" + netRow + "*" + percent1;
		netRateFormula2 = "E" + netRow + "*" + percent2;
		netRateFormula3 = "E" + netRow + "*" + percent3;

		limitLoan1 = result.get("LimitLoanB1");
		limitLoan2 = result.get("LimitLoanB2");
		limitLoan3 = result.get("LimitLoanB3");

		mark1 = "0".equals(limitLoan1) ? "" : "放款總額\n不得逾" + limitLoan1 + "億元";
		mark2 = "0".equals(limitLoan2) ? "" : "放款總額\n不得逾" + limitLoan2 + "億元";
		mark3 = "0".equals(limitLoan3) ? "" : "放款總額\n不得逾" + limitLoan3 + "億元";

		makeExcel.setValue(netRowB + 1, 17, netRate1, "C");
		makeExcel.setValue(netRowB + 1, 18, netRate2, "C");
		makeExcel.setValue(netRowB + 1, 19, netRate3, "C");

		makeExcel.setFormula(netRowB + 2, 17, BigDecimal.ZERO, netRateFormula1, "#,##0");
		makeExcel.setFormula(netRowB + 2, 18, BigDecimal.ZERO, netRateFormula2, "#,##0");
		makeExcel.setFormula(netRowB + 2, 19, BigDecimal.ZERO, netRateFormula3, "#,##0");

		makeExcel.setMergedRegionValue(netRowB + 3, netRowB + 4, 17, 17, mark1, "C");
		makeExcel.setMergedRegionValue(netRowB + 3, netRowB + 4, 18, 18, mark2, "C");
		makeExcel.setMergedRegionValue(netRowB + 3, netRowB + 4, 19, 19, mark3, "C");

		String rateAB = result.get("LimitRateAB3") + "%";
//		String limitLoanAB = result.get("LimitLoanAB3");

		makeExcel.setMergedRegionValue(netRowB + 8, netRowB + 9, 19, 19, "A級與B級行業別\n之款總額餘額");
		String bFormulaAB = "F" + (endRowC + 1) + "*" + rateAB;
		makeExcel.setFormula(netRowB + 10, 19, BigDecimal.ZERO, bFormulaAB, "#,##0");
		makeExcel.setMergedRegionValue(netRowB + 11, netRowB + 12, 19, 19, "不得低於企業\n之款總額餘額之" + rateAB);

		makeExcel.formulaCaculate(netRowB + 3, 17);
		makeExcel.formulaCaculate(netRowB + 3, 18);
		makeExcel.formulaCaculate(netRowB + 3, 19);
		makeExcel.formulaCaculate(netRowB + 8, 19);
		makeExcel.formulaCaculate(netRowB + 11, 19);

		percent1 = result.get("LimitRateC1") + "%";
		percent2 = result.get("LimitRateC2") + "%";
		percent3 = result.get("LimitRateC3") + "%";

		netRate1 = "淨值 " + percent1;
		netRate2 = "淨值 " + percent2;
		netRate3 = "淨值 " + percent3;

		netRateFormula1 = "E" + netRow + "*" + percent1;
		netRateFormula2 = "E" + netRow + "*" + percent2;
		netRateFormula3 = "E" + netRow + "*" + percent3;

		limitLoan1 = result.get("LimitLoanC1");
		limitLoan2 = result.get("LimitLoanC2");
		limitLoan3 = result.get("LimitLoanC3");

		mark1 = "0".equals(limitLoan1) ? "" : "放款總額\n不得逾" + limitLoan1 + "億元";
		mark2 = "0".equals(limitLoan2) ? "" : "放款總額\n不得逾" + limitLoan2 + "億元";
		mark3 = "0".equals(limitLoan3) ? "" : "放款總額\n不得逾" + limitLoan3 + "億元";

		makeExcel.setValue(netRowC + 1, 17, netRate1, "C");
		makeExcel.setValue(netRowC + 1, 18, netRate2, "C");
		makeExcel.setValue(netRowC + 1, 19, netRate3, "C");

		makeExcel.setFormula(netRowC + 2, 17, BigDecimal.ZERO, netRateFormula1, "#,##0");
		makeExcel.setFormula(netRowC + 2, 18, BigDecimal.ZERO, netRateFormula2, "#,##0");
		makeExcel.setFormula(netRowC + 2, 19, BigDecimal.ZERO, netRateFormula3, "#,##0");

		makeExcel.setMergedRegionValue(netRowC + 3, netRowC + 4, 17, 17, mark1, "C");
		makeExcel.setMergedRegionValue(netRowC + 3, netRowC + 4, 18, 18, mark2, "C");
		makeExcel.setMergedRegionValue(netRowC + 3, netRowC + 4, 19, 19, mark3, "C");

		makeExcel.setMergedRegionValue(netRowA, endRowA, 16, 16, "V", "C");
		makeExcel.setMergedRegionValue(netRowB, endRowB, 16, 16, "V", "C");
		makeExcel.setMergedRegionValue(netRowC, endRowC, 16, 16, "V", "C");

	}

}
