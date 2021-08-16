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
import com.st1.itx.db.domain.CdVarValue;
import com.st1.itx.db.service.CdVarValueService;
import com.st1.itx.db.service.springjpa.cm.LM049ServiceImpl;
import com.st1.itx.util.common.MakeExcel;
import com.st1.itx.util.common.MakeReport;
import com.st1.itx.util.format.FormatUtil;

@Component
@Scope("prototype")

public class LM049Report extends MakeReport {

	@Autowired
	LM049ServiceImpl lM049ServiceImpl;

	@Autowired
	MakeExcel makeExcel;

	@Autowired
	CdVarValueService cdVarValueService;

	private BigDecimal totalOfLoanBal = BigDecimal.ZERO;

	private int netValueDataDate = 0;
	private BigDecimal netValue = BigDecimal.ZERO;

	@Override
	public void printTitle() {

	}

	public void exec(TitaVo titaVo) throws LogicException {

		// 先取得淨值

		int entdy = titaVo.getEntDyI() + 19110000;

		entdy = entdy / 10000;

		CdVarValue tCdVarValue = cdVarValueService.findYearMonthFirst(entdy, titaVo);

		if (tCdVarValue != null) {
			this.info("淨值年月=" + tCdVarValue.getYearMonth());
			this.info("淨值=" + tCdVarValue.getTotalequity());
			netValueDataDate = tCdVarValue.getYearMonth();
			netValue = tCdVarValue.getTotalequity();
		}

		List<Map<String, String>> listLM049 = null;

		try {
			listLM049 = lM049ServiceImpl.findAll(titaVo);
		} catch (Exception e) {
			StringWriter errors = new StringWriter();
			e.printStackTrace(new PrintWriter(errors));
			this.info("LM049ServiceImpl.testExcel error = " + errors.toString());
		}
		exportExcel(titaVo, listLM049);
	}

	private void exportExcel(TitaVo titaVo, List<Map<String, String>> listLM049) throws LogicException {
		this.info("LM049Report exportExcel");

		makeExcel.open(titaVo, titaVo.getEntDyI(), titaVo.getKinbr(), "LM049", "放款金控法第44條利害關係人放款餘額表",
				"LM049放款金控法第44條利害關係人放款餘額表_限額控管", "LM049_底稿_放款金控法第44條利害關係人放款餘額表_限額控管.xlsx", "108.04金控子公司表7-1");

		String entdy = titaVo.getEntDy();

		makeExcel.setSheet("108.04金控子公司表7-1", entdy.substring(1, 6) + "金控子公司表7-1");

		makeExcel.setValue(1, 2, "           " + this.showRocDate(entdy, 0) + " 新光人壽對金控法第四十四條授信限制對象授信明細表");

		if (listLM049 == null || listLM049.isEmpty()) {
			makeExcel.setValue(4, 2, "本日無資料");
			makeExcel.close();
			return;
		}

		// 寫入淨值
		// 年.月 淨值（核閱數）
		String outputNetValueDataDate = "";
		if (netValueDataDate > 191100) {
			netValueDataDate -= 191100;
			outputNetValueDataDate = String.valueOf(netValueDataDate);
			outputNetValueDataDate = outputNetValueDataDate.substring(0, 3) + "." + outputNetValueDataDate.substring(3);
		}
		makeExcel.setValue(2, 5, outputNetValueDataDate + " 淨值（核閱數）");
		makeExcel.setValue(2, 6, divThousand(netValue), "#,##0");

		// 整理資料
		// 根據CusType & CusSCD 區分大類 A B C D
		// CusType = 1 = 金控公司負責人及大股東 = A
		// CusType = 2 & CusSCD = 1 = 前者為獨資、合夥經營事業或擔任負責人企業或為代表人團體 = B
		// CusType = 2 & CusSCD = 2 = 有半數以上董事與金控公司或其子公司相同之公司 = C
		// CusType = 3 = 金控公司子公司及其負責人、大股東 = D

		List<Map<String, String>> listA = new ArrayList<>();
		List<Map<String, String>> listB = new ArrayList<>();
		List<Map<String, String>> listC = new ArrayList<>();
		List<Map<String, String>> listD = new ArrayList<>();

		for (Map<String, String> tLM049 : listLM049) {

			String tCusType = tLM049.get("F0");
			String tCusSCD = tLM049.get("F1");

			this.info("tCusType = " + tCusType);
			this.info("tCusSCD = " + tCusSCD);

			switch (tCusType) {
			case "1":
				listA = putData(tLM049, listA);
				break;
			case "2":
				if (tCusSCD.equals("1")) {
					listB = putData(tLM049, listB);
				} else if (tCusSCD.equals("2")) {
					listC = putData(tLM049, listC);
				}
				break;
			case "3":
				listD = putData(tLM049, listD);
				break;
			default:
				this.error("out of cases. tCusType = " + tCusType);
				break;
			}
		} // for

		int rowCursorA = 4;
		int rowCursorB = 7;
		int rowCursorC = 10;
		int rowCursorD = 13;
		int rowCursorTotal = 15;

		if (listA.size() > 1) {
			// 將表格往下移，移出空間
			makeExcel.setShiftRow(rowCursorA, listA.size());
			// 更新行數指標
			rowCursorB += listA.size() - 1;
			rowCursorC += listA.size() - 1;
			rowCursorD += listA.size() - 1;
			rowCursorTotal += listA.size() - 1;
			// 寫入資料
			setValueToExcel(rowCursorA, listA);
		}
		if (listB.size() > 1) {
			// 將表格往下移，移出空間
			makeExcel.setShiftRow(rowCursorB, listB.size());
			// 更新行數指標
			rowCursorC += listB.size() - 1;
			rowCursorD += listB.size() - 1;
			rowCursorTotal += listB.size() - 1;
			// 寫入資料
			setValueToExcel(rowCursorB, listB);
		}
		if (listC.size() > 1) {
			// 將表格往下移，移出空間
			makeExcel.setShiftRow(rowCursorC, listC.size());
			// 更新行數指標
			rowCursorD += listC.size() - 1;
			rowCursorTotal += listC.size() - 1;
			// 寫入資料
			setValueToExcel(rowCursorC, listC);
		}
		if (listD.size() > 1) {
			// 將表格往下移，移出空間
			makeExcel.setShiftRow(rowCursorD, listD.size());
			// 更新行數指標
			rowCursorTotal += listD.size() - 1;
			// 寫入資料
			setValueToExcel(rowCursorD, listD);
		}

		// 寫入合計資料
		makeExcel.setValue(rowCursorTotal, 13, divThousand(totalOfLoanBal), "#,##0");

		long sno = makeExcel.close();
		makeExcel.toExcel(sno);
	}

	private List<Map<String, String>> putData(Map<String, String> tLM049, List<Map<String, String>> tmpList) {
		Map<String, String> tmpMap = new HashMap<>();

		tmpMap.put("CustName", tLM049.get("F4")); // 戶名
		tmpMap.put("CusscdName", tLM049.get("F2")); // 子公司名稱
		tmpMap.put("STSName", tLM049.get("F5") == null ? "" : tLM049.get("F5")); // 職位名稱
		tmpMap.put("DrawdownDate", showRocDate(tLM049.get("F9"), 1)); // 放款期間-起日(撥款日)
		tmpMap.put("MaturityDate", showRocDate(tLM049.get("F10"), 1)); // 放款期間-止日(到期日)
		tmpMap.put("StoreRate", tLM049.get("F11")); // 放款利率
		tmpMap.put("EvaAmt", tLM049.get("F12")); // 擔保品估價
		tmpMap.put("LoanBal", tLM049.get("F13")); // 授信餘額
		tmpMap.put("FacmNo", FormatUtil.pad9(tLM049.get("F8"), 3)); // 額度

		tmpMap.put("CustNo", FormatUtil.pad9(tLM049.get("F7"), 7)); // 戶號

		boolean isSameCollateral = tLM049.get("F14").equals("Y");
		tmpMap.put("IsSameCollateral", isSameCollateral ? "同擔保品" : ""); // 備註說明
		tmpMap.put("ClNo", isSameCollateral ? tLM049.get("F15") : ""); // 擔保品號碼

		tmpList.add(tmpMap);
		return tmpList;
	}

	private void setValueToExcel(int rowCursor, List<Map<String, String>> list) throws LogicException {

		// 小計-授信餘額
		BigDecimal loanBalTotal = BigDecimal.ZERO;

		Map<String, BigDecimal> clEvaAmt = new HashMap<>();
		Map<String, BigDecimal> clLoanBal = new HashMap<>();

		// 貸放成數特殊邏輯
		// 同擔保品時,先合計放款餘額再除以擔保品估價
		for (Map<String, String> map : list) {

			String custNoClNo = map.get("CustNo") + "-" + map.get("ClNo");

			clEvaAmt.put(custNoClNo, getBigDecimal(map.get("EvaAmt")));

			BigDecimal loanBal = getBigDecimal(map.get("LoanBal"));

			loanBalTotal = loanBalTotal.add(loanBal);

			BigDecimal computeLoanBal = loanBal;

			if (clLoanBal.containsKey(custNoClNo)) {
				computeLoanBal = computeLoanBal.add(clLoanBal.get(custNoClNo));
			}

			clLoanBal.put(custNoClNo, computeLoanBal);
		}

		Map<String, int[]> sameCollateralRange = new HashMap<>();

		for (Map<String, String> map : list) {

			// 戶名
			makeExcel.setValue(rowCursor, 2, map.get("CustName"));
			// 金控公司負責人及大股東
			makeExcel.setValue(rowCursor, 3, map.get("CusscdName") + "(" + map.get("STSName") + ")");
			// 放款期間
			makeExcel.setValue(rowCursor, 4, map.get("DrawdownDate"));
			makeExcel.setValue(rowCursor, 5, map.get("MaturityDate"));
			// 放款利率
			makeExcel.setValue(rowCursor, 6, map.get("StoreRate"));

			// 貸放成數特殊邏輯
			// 同擔保品時,先合計放款餘額再除以擔保品估價
			String custNoClNo = map.get("CustNo") + "-" + map.get("ClNo");

			BigDecimal evaAmt = clEvaAmt.get(custNoClNo);
			BigDecimal loanBalGroupByCollateral = clLoanBal.get(custNoClNo);
			BigDecimal loanToValue = computeDivide(loanBalGroupByCollateral.multiply(getBigDecimal("100")), evaAmt, 2);

			// 擔保品估價
			makeExcel.setValue(rowCursor, 7, divThousand(evaAmt), "#,##0");
			// 貸放成數
			makeExcel.setValue(rowCursor, 8, loanToValue, "#,##0.00%");

			// 十足擔保
			if (evaAmt.compareTo(loanBalGroupByCollateral) > 0) {
				makeExcel.setValue(rowCursor, 9, "v");
			} else {
				makeExcel.setValue(rowCursor, 9, "-");
			}
			// 不優於同類授信對象
			makeExcel.setValue(rowCursor, 10, "v");
			// 達1億元且經董事會決議
			makeExcel.setValue(rowCursor, 11, "-");
			// 符合金控法第44條
			makeExcel.setValue(rowCursor, 12, "v");

			// 授信餘額
			makeExcel.setValue(rowCursor, 13, divThousand(loanBalGroupByCollateral), "#,##0");
			// 額度
			makeExcel.setValue(rowCursor, 15, map.get("FacmNo"));
			// 備註說明
			makeExcel.setValue(rowCursor, 16, map.get("IsSameCollateral"));

			// 本程式putData有將此欄位"IsSameCollateral"值update成文字,query結果為Y/N
			if (map.get("IsSameCollateral").equals("同擔保品")) {
				// 同擔保品時紀錄行數起訖
				// 迴圈結束後做合併

				this.info("this row is same collateral. custNoClNo = " + custNoClNo);

				int[] tmpRecord = new int[2];

				if (sameCollateralRange.containsKey(custNoClNo)) {

					tmpRecord = sameCollateralRange.get(custNoClNo);

					if (tmpRecord[0] > rowCursor) {
						tmpRecord[0] = rowCursor;
					}
					if (tmpRecord[1] < rowCursor) {
						tmpRecord[1] = rowCursor;
					}

				} else {
					tmpRecord[0] = rowCursor;
					tmpRecord[1] = rowCursor;
				}
				this.info("tmpRecord[0] = " + tmpRecord[0]);
				this.info("tmpRecord[1] = " + tmpRecord[1]);
				sameCollateralRange.put("custNoClNo", tmpRecord);
			}

			rowCursor++;
		}

		// 合併儲存格
		if (sameCollateralRange != null && sameCollateralRange.isEmpty()) {
			mergeColumns(sameCollateralRange);
		}

		// 印小計
		makeExcel.setValue(rowCursor, 13, divThousand(loanBalTotal), "#,##0");

		// 計算總計
		totalOfLoanBal = totalOfLoanBal.add(loanBalTotal);
	}

	/**
	 * 合併一批資料的儲存格
	 * 
	 * @param sameCollateralRange 一批資料中需要合併的起訖行數
	 */
	private void mergeColumns(Map<String, int[]> sameCollateralRange) {

		sameCollateralRange.forEach((k, v) -> {
			this.info("mergeColumns k " + k);
			this.info("mergeColumns v[0] " + v[0]);
			this.info("mergeColumns v[1] " + v[1]);

			// 戶名
			makeExcel.setMergedRegion(v[0], v[1], 2, 2);
			// 金控公司負責人及大股東
			makeExcel.setMergedRegion(v[0], v[1], 3, 3);
			// 擔保品估價
			makeExcel.setMergedRegion(v[0], v[1], 7, 7);
			// 貸放成數
			makeExcel.setMergedRegion(v[0], v[1], 8, 8);
			// 授信餘額
			makeExcel.setMergedRegion(v[0], v[1], 13, 13);
			// 佔淨值比
			makeExcel.setMergedRegion(v[0], v[1], 14, 14);
		});

	}

	/**
	 * 千元單位運算
	 * 
	 * @param input 數值，此報表應僅有淨值、擔保品估價及授信餘額需要做千元單位運算
	 * @return 傳入值除1000之結果
	 */
	private BigDecimal divThousand(BigDecimal input) {

		this.info("divThousand input = " + input);

		if (input == null) {
			return BigDecimal.ZERO;
		}

		input = computeDivide(input, new BigDecimal("1000"), 3);

		return input;
	}
}