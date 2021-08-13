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

	private BigDecimal totalOfLoanBal = BigDecimal.ZERO;

	@Override
	public void printTitle() {

	}

	public void exec(TitaVo titaVo) throws LogicException {
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

		if (listLM049.size() == 0) {
			makeExcel.setValue(4, 2, "本日無資料");
		}

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
		makeExcel.setValue(rowCursorTotal, 13, formatAmt(totalOfLoanBal, 0));

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
		tmpMap.put("StoreRate", formatAmt(tLM049.get("F11"), 3)); // 放款利率
		tmpMap.put("EvaAmt", formatAmt(tLM049.get("F12"), 0)); // 擔保品估價
		tmpMap.put("LoanBal", formatAmt(tLM049.get("F13"), 0)); // 授信餘額
		tmpMap.put("FacmNo", FormatUtil.pad9(tLM049.get("F8"), 3)); // 額度

		tmpMap.put("CustNo", FormatUtil.pad9(tLM049.get("F7"), 7)); // 戶號

		boolean isSameCollateral = tLM049.get("F14").equals("Y");
		tmpMap.put("IsSameCollateral", isSameCollateral ? "同押品" : ""); // 備註說明
		tmpMap.put("ClNo", isSameCollateral ? tLM049.get("F15") : ""); // 擔保品號碼

		tmpList.add(tmpMap);
		return tmpList;
	}

	private void setValueToExcel(int rowCursor, List<Map<String, String>> list) throws LogicException {

		// 小計-授信餘額
		BigDecimal loanBalTotal = BigDecimal.ZERO;

		Map<String, BigDecimal> clEvaAmt = new HashMap<>();
		Map<String, BigDecimal> clLoanBal = new HashMap<>();

		for (Map<String, String> map : list) {

			String custNoClNo = map.get("CustNo") + "-" + map.get("ClNo");

			clEvaAmt.put(custNoClNo, new BigDecimal(map.get("EvaAmt")));

			BigDecimal loanBal = new BigDecimal(map.get("LoanBal"));

			loanBalTotal = loanBalTotal.add(loanBal);

			BigDecimal computeLoanBal = loanBal;

			if (clLoanBal.containsKey(custNoClNo)) {
				computeLoanBal = computeLoanBal.add(clLoanBal.get(custNoClNo));
			}

			clLoanBal.put(custNoClNo, computeLoanBal);
		}

		for (Map<String, String> map : list) {

			makeExcel.setValue(rowCursor, 2, map.get("CustName"));
			makeExcel.setValue(rowCursor, 3, map.get("CusscdName") + "(" + map.get("STSName") + ")");
			makeExcel.setValue(rowCursor, 4, map.get("DrawdownDate"));
			makeExcel.setValue(rowCursor, 5, map.get("MaturityDate"));
			makeExcel.setValue(rowCursor, 6, map.get("StoreRate"));

			// 貸放成數特殊邏輯
			// 同擔保品時,先合計放款餘額再除以擔保品估價
			String custNoClNo = map.get("CustNo") + "-" + map.get("ClNo");

			BigDecimal evaAmt = clEvaAmt.get(custNoClNo);
			BigDecimal loanBalGroupByCollateral = clLoanBal.get(custNoClNo);
			BigDecimal loanToValue = this.computeDivide(loanBalGroupByCollateral.multiply(new BigDecimal("100")),
					evaAmt, 2);

			makeExcel.setValue(rowCursor, 7, formatAmt(evaAmt, 0), "R");
			makeExcel.setValue(rowCursor, 8, formatAmt(loanToValue, 2) + "%", "R");

			makeExcel.setValue(rowCursor, 13, formatAmt(loanBalGroupByCollateral, 0), "R");
			makeExcel.setValue(rowCursor, 15, map.get("FacmNo"));
			makeExcel.setValue(rowCursor, 16, map.get("IsSameCollateral") + map.get("ClNo"));

			rowCursor++;
		}

		// 印小計
		makeExcel.setValue(rowCursor, 13, formatAmt(loanBalTotal, 0), "R");

		// 計算總計
		totalOfLoanBal = totalOfLoanBal.add(loanBalTotal);
	}
}