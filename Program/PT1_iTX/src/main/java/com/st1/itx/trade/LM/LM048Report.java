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
	MakeExcel makeExcel;

	@Autowired
	Parse parse;

	// 輸出行數減一
	// 每個評等的起始列
	private int startRowA = 4;
	private int startRowB = 10;
	private int startRowC = 16;
	// 每個評等的結束列
	private int endRowA = 0;
	private int endRowB = 0;
	private int endRowC = 0;

	private int sDustRow = 1;
	private int eDustRow = 1;

	private int sCustNoRow = 1;
	private int eCustNoRow = 1;

	private int sGroupRow = 1;
	private int eGroupRow = 1;

	private BigDecimal tLoanBalTotal = BigDecimal.ZERO;
	private BigDecimal tLineAmtTotal = BigDecimal.ZERO;

	private BigDecimal tLineAmtCalculate = BigDecimal.ZERO;
	private BigDecimal tLoanBalCalculate = BigDecimal.ZERO;

	private List<Integer> lIndustryRatingListA = new ArrayList<Integer>();
	private List<Integer> lIndustryRatingListB = new ArrayList<Integer>();
	private List<Integer> lIndustryRatingListC = new ArrayList<Integer>();

	public boolean exec(TitaVo titaVo) throws LogicException {
		this.info("LM048Report exec ...");

		int iYearMonth = Integer.parseInt(titaVo.getParam("YearMonth")) + 191100;

		// 明細
		List<Map<String, String>> lLM048List = new ArrayList<Map<String, String>>();
		List<Map<String, String>> lLM048List2 = new ArrayList<Map<String, String>>();

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

		exportExcel(lLM048List, lLM048List2, iYearMonth, titaVo);

		return true;

	}

	private void exportExcel(List<Map<String, String>> lLM048List, List<Map<String, String>> lLM048List2,
			int iYearMonth, TitaVo titaVo) throws LogicException {
		this.info("LM048Report exportExcel");

		int iAcDate = titaVo.getEntDyI() + 19110000;
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
		makeExcel.setValue(2, 6, rocDateCh, "C");

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
			BigDecimal tLineAmt = BigDecimal.ZERO;
			BigDecimal tLoanBal = BigDecimal.ZERO;
			BigDecimal tStoreRate = BigDecimal.ZERO;
			String tMaturityDate = "";
			String tPrevPayIntDate = "";
			String tGroupName = "";
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

			this.info(" lLM048List.size() = " + lLM048List.size());
			for (Map<String, String> r : lLM048List) {

				tInDustryCode = r.get("IndustryCode") == null ? 0 : parse.stringToInteger(r.get("IndustryCode"));
				tInDustryRating = r.get("IndustryRating");
				tInDustryItem = r.get("IndustryItem");

				tCustNoMain = r.get("CustNoMain") == null ? 0 : parse.stringToInteger(r.get("CustNoMain"));

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

					makeExcel.setValue(row, 1, tInDustryRating, "C	");
					makeExcel.setValue(row, 2, tInDustryItem, "L");
				} else {
					eDustRow = row + 3;// 64
				}

				count++;

				tCustNo = r.get("CustNo") == null ? 0 : parse.stringToInteger(r.get("CustNo"));
				if (tCustNo == 0) {
					sDustRow = 1;
					eDustRow = 1;
					continue;
				}

				if (tmpCustNo != tCustNo) {
					tmpCustNo = tCustNo;

					if (count > 0 && sCustNoRow - eCustNoRow != 0) {
						this.info("sCustNoRow =" + sCustNoRow);
						this.info("eCustNoRow =" + eCustNoRow);
						makeExcel.setMergedRegionValue(sCustNoRow, eCustNoRow, 3, 3, tCustNo, "C");
						makeExcel.setMergedRegionValue(sCustNoRow, eCustNoRow, 4, 4, tCustName, "C");
					}

					sCustNoRow = row;// 37
					eCustNoRow = row;// 37

					makeExcel.setValue(row, 3, tCustNo, "C");
					tCustName = r.get("CustName");
					makeExcel.setValue(row, 4, tCustName, "L");

				} else {
					eCustNoRow = row;
				}

				tLineAmt = getBigDecimal(r.get("LineAmt"));
				makeExcel.setValue(row, 5, tLineAmt, "#,##0", "L");
				tLineAmtCalculate = tLineAmtCalculate.add(tLineAmt);

				tLoanBal = getBigDecimal(r.get("LoanBal"));
				makeExcel.setValue(row, 6, tLoanBal, "#,##0", "L");
				tLoanBalCalculate = tLoanBalCalculate.add(tLoanBal);

				tStoreRate = getBigDecimal(r.get("StoreRate"));
				makeExcel.setValue(row, 7, tStoreRate, "0.0000", "C");

				tMaturityDate = this.showBcDate(r.get("MaturityDate"), 0);
				makeExcel.setValue(row, 8, tMaturityDate, "C");

				tPrevPayIntDate = this.showBcDate(r.get("PrevPayIntDate"), 0);
				makeExcel.setValue(row, 9, tPrevPayIntDate, "C");

				this.info("tLineAmt = " + tLineAmt);
				this.info("tNetWorth =" + tNetWorth);

				makeExcel.setValue(row, 12, tLineAmt.divide(tNetWorth, 4, BigDecimal.ROUND_HALF_UP), "0.00%", "C");

				tGroupName = r.get("CustNameMain");
				// 不同集團
				if (tmpCustNoMain != tCustNoMain) {

					tmpCustNoMain = tCustNoMain;

					if (count > 0 && sGroupRow - eGroupRow != 0) {
						this.info("sGroupRow =" + sGroupRow);
						this.info("eGroupRow =" + eGroupRow);
						makeExcel.setMergedRegionValue(sGroupRow, eGroupRow, 10, 10, tGroupName, "C");
						makeExcel.setMergedRegionValue(sGroupRow, eGroupRow, 11, 11,
								groupLineAmt(reportDate / 100, tCustNoMain).divide(tNetWorth, 4,
										BigDecimal.ROUND_HALF_UP),
								"0.00%", "C");
					}

					sGroupRow = row;// 37
					eGroupRow = row;// 37

//					tmpCustNoMain = tCustNoMain;
					if (tGroupName == null || tGroupName.trim() == null) {
						makeExcel.setValue(row, 10, tGroupName, "C");
					}

				} else {
					eGroupRow = row;
				}

				// CustMain需有值
				if (tCustNoMain != 0) {
					// 當前產業與下一筆的產業不同 或 最後一筆時 需輸出單一行業合計
					if (tInDustryCode != tInDustryCodeNext || lLM048List.size() == count) {
						tLineAmtTotal = tLineAmtTotal.add(tLineAmtCalculate);
						tLoanBalTotal = tLoanBalTotal.add(tLoanBalCalculate);

						row = checkIndustryRatingtAddRow(tInDustryRating, true);
						// 輸出單一合計
						makeExcelSetValCal(row);

						// 當前評等與下一筆不一樣 或 最後一筆時 需輸出評等合計
						this.info("Count =" + count);
						if (!tInDustryRating.equals(tInDustryRatingNext) || lLM048List.size() == count) {

							this.info("change");
							row++;
							makeExcel.setValue(row, 5, tLineAmtTotal, "#,##0", "R");
							makeExcel.setValue(row, 6, tLoanBalTotal, "#,##0", "R");

							tLineAmtTotal = BigDecimal.ZERO;
							tLoanBalTotal = BigDecimal.ZERO;
						}

						tLineAmtCalculate = BigDecimal.ZERO;
						tLoanBalCalculate = BigDecimal.ZERO;
					}
				}
			}

			String sumInDustryRatingA = sumInDustryRatingFormulua("A", "E");
			String sumInDustryRatingB = sumInDustryRatingFormulua("B", "E");
			String sumInDustryRatingC = sumInDustryRatingFormulua("C", "E");

			// 各評等合計
			makeExcel.setFormula(endRowA, 5, BigDecimal.ZERO, sumInDustryRatingA, "#,##0");
			makeExcel.setFormula(endRowB, 5, BigDecimal.ZERO, sumInDustryRatingB, "#,##0");
			makeExcel.setFormula(endRowC, 5, BigDecimal.ZERO, sumInDustryRatingC, "#,##0");

			makeExcel.setFormula(endRowA, 6, BigDecimal.ZERO, sumInDustryRatingFormulua("A", "F"), "#,##0");
			makeExcel.setFormula(endRowB, 6, BigDecimal.ZERO, sumInDustryRatingFormulua("B", "F"), "#,##0");
			makeExcel.setFormula(endRowC, 6, BigDecimal.ZERO, sumInDustryRatingFormulua("C", "F"), "#,##0");

			BigDecimal sumLineAmtA = getBigDecimal(sumInDustryRatingA);
			BigDecimal sumLineAmtB = getBigDecimal(sumInDustryRatingB);
			BigDecimal sumLineAmtC = getBigDecimal(sumInDustryRatingC);

			makeExcel.setFormula(endRowA, 13, sumLineAmtA.divide(tNetWorth, 4, BigDecimal.ROUND_HALF_UP), "", "0.00%");
			makeExcel.setFormula(endRowB, 13, sumLineAmtA.divide(tNetWorth, 4, BigDecimal.ROUND_HALF_UP), "", "0.00%");
			makeExcel.setFormula(endRowC, 13, sumLineAmtA.divide(tNetWorth, 4, BigDecimal.ROUND_HALF_UP), "", "0.00%");

			// 總合計
			row = endRowC + 1;
			String formuLineAmt = "SUM(E" + endRowA + ",E" + endRowB + ",E" + endRowC + ")";
			makeExcel.setFormula(row, 5, BigDecimal.ZERO, formuLineAmt, "#,##0");
			String formuLoanBal = "SUM(F" + endRowA + ",F" + endRowB + ",F" + endRowC + ")";
			makeExcel.setFormula(row, 6, BigDecimal.ZERO, formuLoanBal, "#,##0");

			String pbrFornula = "E" + row + "/" + "E" + (row + 8);
			makeExcel.setFormula(row, 13, BigDecimal.ZERO, pbrFornula, "0.00%");

			// 放款總餘額
			row = row + 7;
			makeExcel.setValue(row, 1, rocDate);
			makeExcel.setValue(row, 5, tLoanTotal, "#,##0", "R");
			// 淨值
			row = row + 1;
			makeExcel.setValue(row, 1, rocLastYear12YDate);
			makeExcel.setValue(row, 5, tNetWorth, "#,##0", "R");

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

//			this.info("custNoMain =" + custNoMain);
			if (custNoMain == 0) {
				rowTemp = 0;
			} else {

//				this.info("tInDustryCode = " + tInDustryCode);
//				this.info("tInDustryCodeNext = " + tInDustryCodeNext);
//				this.info("------------------------------------");

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
			makeExcel.setValue(trow, 5, tLineAmtCalculate, "#,##0", "R");
			makeExcel.setValue(trow, 6, tLoanBalCalculate, "#,##0", "R");
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

		if (result.length() > 4) {

			result = result.substring(0, result.length() - 1);

		}

		result = result + ")";
		this.info("result = " + result);

		return result;
	}

	private BigDecimal groupLineAmt(int iYearMonth, int custNoGroup) {
		BigDecimal result = BigDecimal.ZERO;

		List<Map<String, String>> list = new ArrayList<Map<String, String>>();

		try {
			list = lM048ServiceImpl.groupLineAmt(iYearMonth, custNoGroup, titaVo);
		} catch (Exception e) {
			StringWriter errors = new StringWriter();
			e.printStackTrace(new PrintWriter(errors));
			this.error("lM048ServiceImpl.groupLineAmt error = " + errors.toString());
		}

		if (list == null || list.size() == 0) {

		} else {

			for (Map<String, String> r : list) {

				result = getBigDecimal(r.get("ToTalLineAmt"));
			}
		}

		return result;
	}

}
