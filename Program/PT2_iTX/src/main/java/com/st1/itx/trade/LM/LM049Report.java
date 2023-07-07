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
import com.st1.itx.util.common.data.ExcelFontStyleVo;
import com.st1.itx.util.common.data.ReportVo;
import com.st1.itx.util.format.FormatUtil;
import com.st1.itx.util.parse.Parse;

@Component
@Scope("prototype")

public class LM049Report extends MakeReport {

	@Autowired
	LM049ServiceImpl lM049ServiceImpl;

	@Autowired
	MakeExcel makeExcel;

	@Autowired
	Parse parse;

	private BigDecimal totalOfLoanBal = BigDecimal.ZERO;

//	private int netValueDataDate = 0;
//	private BigDecimal netValue = BigDecimal.ZERO;

	@Override
	public void printTitle() {

	}

	public void exec(TitaVo titaVo, int mfbsdy) throws LogicException {
		this.info("LM049Report exec");

		List<Map<String, String>> findStockHoldersEqt = null;
		List<Map<String, String>> listLM049 = null;

		try {
			this.info("lM049ServiceImpl.findStockHoldersEqt...");
			findStockHoldersEqt = lM049ServiceImpl.findStockHoldersEqt(mfbsdy, titaVo);

			this.info("lM049ServiceImpl.findAll...");
			listLM049 = lM049ServiceImpl.findAll(mfbsdy, titaVo);

		} catch (Exception e) {
			StringWriter errors = new StringWriter();
			e.printStackTrace(new PrintWriter(errors));
			this.info("LM049ServiceImpl error = " + errors.toString());
		}

		exportExcel(listLM049, findStockHoldersEqt, mfbsdy, titaVo);
	}

	private void exportExcel(List<Map<String, String>> listLM049, List<Map<String, String>> findStockHoldersEqt,
			int date, TitaVo titaVo) throws LogicException {

		int reportDate = titaVo.getEntDyI() + 19110000;
		String brno = titaVo.getBrno();
		String txcd = "LM049";
		String fileItem = "放款金控法第44條利害關係人放款餘額表";
		String fileName = "LM049放款金控法第44條利害關係人放款餘額表_限額控管";
		String defaultExcel = "LM049_底稿_放款金控法第44條利害關係人放款餘額表_限額控管.xlsx";
		String defaultSheet = "108.04金控子公司表7-1";

		int rocYM = reportDate / 100;

		this.info("reportVo open");

		ReportVo reportVo = ReportVo.builder().setRptDate(reportDate).setBrno(brno).setRptCode(txcd)
				.setRptItem(fileItem).build();

		// 開啟報表
		makeExcel.open(titaVo, reportVo, fileName, defaultExcel, defaultSheet);

		makeExcel.setSheet("108.04金控子公司表7-1", rocYM + "金控子公司表7-1");

		makeExcel.setValue(1, 2, "           " + this.showRocDate(date, 0) + " 新光人壽對金控法第四十四條授信限制對象授信明細表", "C");

		if (listLM049 == null || listLM049.isEmpty()) {
			makeExcel.setValue(4, 2, "本日無資料");
			makeExcel.close();
			return;
		}

		// 寫入淨值
		// 年.月 淨值（核閱數）
		String outputNetValueDataDate = String
				.valueOf(parse.stringToInteger(findStockHoldersEqt.get(0).get("AcDate")) - 19110000);
		outputNetValueDataDate = outputNetValueDataDate.substring(0, 3) + "." + outputNetValueDataDate.substring(3, 5)
				+ "." + outputNetValueDataDate.substring(5);

		makeExcel.setValue(2, 5, outputNetValueDataDate + " 淨值（核閱數）");
		makeExcel.setValue(2, 7, divThousand(new BigDecimal(findStockHoldersEqt.get(0).get("StockHoldersEqt"))),
				"#,##0");

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

			String tCusType = tLM049.get("comSeq");

			switch (tCusType) {
			case "1":
				listA = putData(tLM049, listA);
				break;
			case "2":
				listB = putData(tLM049, listB);
				break;
			case "3":
				listC = putData(tLM049, listC);
				break;

			case "4":
				listD = putData(tLM049, listD);
				break;
			default:
				break;
			}
		} // for

		int rowCursorA = 4;
		int rowCursorB = 7;
		int rowCursorC = 10;
		int rowCursorD = 13;
		int rowCursorTotal = 15;

		int[] a = new int[2];
		int[] b = new int[2];
		int[] c = new int[2];
		int[] d = new int[2];

		a[0] = rowCursorA;

		if (listA.size() > 1) {
			// 將表格往下移，移出空間
			makeExcel.setShiftRow(rowCursorA + 1, listA.size() - 1);
			// 更新行數指標
			rowCursorB += listA.size() - 1;
			rowCursorC += listA.size() - 1;
			rowCursorD += listA.size() - 1;
			rowCursorTotal += listA.size() - 1;
		}
		// ex : 4 + 5 - 1 = 第8列
		a[1] = a[0] + listA.size() - 1;

		if (listA.size() > 0) {
			// 寫入資料
			setValueToExcel(rowCursorA, listA);
		}

		b[0] = rowCursorB;

		if (listB.size() > 1) {
			// 將表格往下移，移出空間
			makeExcel.setShiftRow(rowCursorB + 1, listB.size() - 1);
			// 更新行數指標
			rowCursorC += listB.size() - 1;
			rowCursorD += listB.size() - 1;
			rowCursorTotal += listB.size() - 1;
		}

		b[1] = b[0] + listB.size() - 1;
		if (listB.size() > 0) {
			// 寫入資料
			setValueToExcel(rowCursorB, listB);
		}

		c[0] = rowCursorC;

		if (listC.size() > 1) {
			// 將表格往下移，移出空間
			makeExcel.setShiftRow(rowCursorC + 1, listC.size() - 1);
			// 更新行數指標
			rowCursorD += listC.size() - 1;
			rowCursorTotal += listC.size() - 1;
		}

		c[1] = c[0] + listC.size() - 1;
		if (listC.size() > 0) {
			// 寫入資料
			setValueToExcel(rowCursorC, listC);
		}

		d[0] = rowCursorD;
		if (listD.size() > 1) {
			// 將表格往下移，移出空間
			makeExcel.setShiftRow(rowCursorD + 1, listD.size() - 1);
			// 更新行數指標
			rowCursorTotal += listD.size() - 1;
		}
		d[1] = d[0] + listD.size() - 1;

		if (listD.size() > 0) {
			// 寫入資料
			setValueToExcel(rowCursorD, listD);
		}

		if (listA.size() > 0) {
			makeExcel.setFormula(a[1] + 1, 11, BigDecimal.ZERO, "SUM(K" + a[0] + ":K" + a[1] + ")", "#,##0");
			makeExcel.setFormula(a[1] + 1, 12, BigDecimal.ZERO, "(K" + a[1] + 1 + "/$G$2) * 100 ", "0.00");
			makeExcel.formulaCaculate(a[1] + 1, 11);
			makeExcel.formulaCaculate(a[1] + 1, 12);
		}

		if (listB.size() > 0) {
			makeExcel.setFormula(b[1] + 1, 11, BigDecimal.ZERO, "SUM(K" + b[0] + ":K" + b[1] + ")", "#,##0");
			makeExcel.setFormula(b[1] + 1, 12, BigDecimal.ZERO, "(K" + b[1] + 1 + "/$G$2) * 100 ", "0.00");
			makeExcel.formulaCaculate(b[1] + 1, 11);
			makeExcel.formulaCaculate(b[1] + 1, 12);
		}

		if (listC.size() > 0) {
			makeExcel.setFormula(c[1] + 1, 11, BigDecimal.ZERO, "SUM(K" + c[0] + ":K" + c[1] + ")", "#,##0");
			makeExcel.setFormula(c[1] + 1, 12, BigDecimal.ZERO, "(K" + c[1] + 1 + "/$G$2) * 100 ", "0.00");
			makeExcel.formulaCaculate(c[1] + 1, 11);
			makeExcel.formulaCaculate(c[1] + 1, 12);
		}

		if (listD.size() > 0) {
			makeExcel.setFormula(d[1] + 1, 11, BigDecimal.ZERO, "SUM(K" + d[0] + ":K" + d[1] + ")", "#,##0");
			makeExcel.setFormula(d[1] + 1, 12, BigDecimal.ZERO, "(K" + d[1] + 1 + "/$G$2) * 100 ", "0.00");
			makeExcel.formulaCaculate(d[1] + 1, 11);
			makeExcel.formulaCaculate(d[1] + 1, 12);
		}
		// 合計
		makeExcel.setFormula(d[1] + 2, 11, BigDecimal.ZERO,
				"SUM(K" + (a[1] + 1) + ",K" + (b[1] + 1) + ",K" + (c[1] + 1) + ",K" + (d[1] + 1) + ")", "#,##0");
		makeExcel.setFormula(d[1] + 2, 12, BigDecimal.ZERO, "(K" + d[1] + 2 + "/$G$2) * 100 ", "0.00");
		makeExcel.formulaCaculate(d[1] + 2, 11);
		makeExcel.formulaCaculate(d[1] + 2, 12);

		// 寫入合計資料
//		makeExcel.setValue(rowCursorTotal, 13, divThousand(totalOfLoanBal), "#,##0");

		// 合計之佔淨值比
//		makeExcel.setValue(rowCursorTotal, 14, computeDivide(totalOfLoanBal, netValue, 8), "#,##0.00");

		ExcelFontStyleVo efsVo = new ExcelFontStyleVo();

		efsVo.setAlign("L");
		efsVo.setBold(true);
		efsVo.setFont((short) 1);
		efsVo.setSize((short) 14);

		// 寫簽核
		makeExcel.setMergedRegion(rowCursorTotal + 2, rowCursorTotal + 2, 2, 16);
		makeExcel.setValue(rowCursorTotal + 2, 2,
				"經 辦：                      經理：                       風險管理人：                       部主管：", efsVo);

		long sno = makeExcel.close();
		makeExcel.toExcel(sno);
	}

	private List<Map<String, String>> putData(Map<String, String> tLM049, List<Map<String, String>> tmpList) {
		Map<String, String> tmpMap = new HashMap<>();

		tmpMap.put("Name", tLM049.get("Name")); // 戶名
		tmpMap.put("CompanyName", tLM049.get("BusName")); // 子公司名稱+ 職位名稱
		tmpMap.put("FirstDrawdownDate", showRocDate(tLM049.get("FirstDrawdownDate"), 1)); // 放款期間-起日(撥款日)
		tmpMap.put("MaturityDate", showRocDate(tLM049.get("MaturityDate"), 1)); // 放款期間-止日(到期日)
		tmpMap.put("StoreRate", tLM049.get("StoreRate")); // 放款利率
		tmpMap.put("EvaNetWorth", tLM049.get("EvaNetWorth")); // 擔保品估價
		tmpMap.put("LoanRatio", tLM049.get("LoanRatio")); // 核貸成數
		tmpMap.put("LoanBal", tLM049.get("LoanBal")); // 授信餘額
		tmpMap.put("FacmNo", FormatUtil.pad9(tLM049.get("FacmNo"), 3)); // 額度
		tmpMap.put("CustNo", FormatUtil.pad9(tLM049.get("CustNo"), 7)); // 戶號
		boolean upToOneHundredMillion = tLM049.get("upToOneHundredMillion").equals("Y");
		tmpMap.put("upToOneHundredMillion", upToOneHundredMillion ? "v" : "-"); // 達1億元且經董事會決議
		tmpMap.put("ClNo", tLM049.get("ClNo")); // 擔保品號碼

		tmpList.add(tmpMap);
		return tmpList;
	}

	private void setValueToExcel(int rowCursor, List<Map<String, String>> list) throws LogicException {

		// 小計-授信餘額
		BigDecimal loanBalTotal = BigDecimal.ZERO;

		String tmpCustNo = "";
		boolean isSame = false;
		String tmpClNo = "";
		String sameClNo = "";
		int tmpRow = 0;
		int clNocnt = 0;
		int cnt = 0;
		for (Map<String, String> map : list) {

			if (!tmpCustNo.equals(map.get("CustNo"))) {
				tmpCustNo = map.get("CustNo");
				isSame = false;
			} else {
				isSame = true;
			}

			// 戶名

			makeExcel.setValue(rowCursor, 2, isSame ? "" : map.get("Name"));
			// 金控公司負責人及大股東
			makeExcel.setValue(rowCursor, 3, isSame ? "" : map.get("CompanyName"));
			// 放款期間
			makeExcel.setValue(rowCursor, 4, map.get("FirstDrawdownDate"));
			makeExcel.setValue(rowCursor, 5, map.get("MaturityDate"));
			// 放款利率
			makeExcel.setValue(rowCursor, 6, getBigDecimal(map.get("StoreRate")), "#,##0.000");

			// 擔保品估價
			makeExcel.setValue(rowCursor, 7, isSame ? BigDecimal.ZERO : getBigDecimal(map.get("EvaNetWorth")), "#,##0");
			// 貸放成數
			makeExcel.setValue(rowCursor, 8, isSame ? BigDecimal.ZERO : getBigDecimal(map.get("LoanRatio")),
					"#,##0.00%");

			// 十足擔保(舜雯說已取消欄位)

			// 不優於同類授信對象
			makeExcel.setValue(rowCursor, 9, isSame ? "" : "v");
			// 達1億元且經董事會決議
			makeExcel.setValue(rowCursor, 10, isSame ? "" : map.get("upToOneHundredMillion"));
			// 符合金控法第44條(舜雯說已取消欄位)
//			makeExcel.setValue(rowCursor, 12, "v");

			// 授信餘額
			makeExcel.setValue(rowCursor, 11, isSame ? BigDecimal.ZERO : getBigDecimal(map.get("LoanBal")), "#,##0");
			// 佔淨值比
			if (!isSame) {
				makeExcel.setFormula(rowCursor, 12, BigDecimal.ZERO, "(K" + rowCursor + "/$G$2) * 100", "0.00");

				makeExcel.formulaCaculate(rowCursor, 12);
			}
			// 額度
			makeExcel.setValue(rowCursor, 13, map.get("FacmNo"));

			// 計算同押品別的數量
			clNocnt++;
			// 計算筆數
			cnt++;

			// 備註說明
			if (!tmpClNo.equals(map.get("ClNo"))) {

				clNocnt = 1;
				tmpClNo = map.get("ClNo");
				if (cnt > 1) {
					makeExcel.setValue(tmpRow, 14, sameClNo);
				}
				tmpRow = rowCursor;
				sameClNo = "";

			} else {
				if (clNocnt > 1) {
					sameClNo = "同押品";
				}
			}

			if (cnt == list.size()) {
				makeExcel.setValue(tmpRow, 14, sameClNo);
			}

			rowCursor++;

		}

		// 印小計
//		makeExcel.setValue(rowCursor, 13, divThousand(loanBalTotal), "#,##0");

		// 小計之佔淨值比
//		makeExcel.setValue(rowCursor, 14, computeDivide(loanBalTotal, netValue, 8), "#,##0.00");

		// 計算總計
		totalOfLoanBal = totalOfLoanBal.add(loanBalTotal);
	}

	/**
	 * 千元單位運算
	 * 
	 * @param input 數值，此報表應僅有淨值、擔保品估價及授信餘額需要做千元單位運算
	 * @return 傳入值除1000之結果
	 */
	private BigDecimal divThousand(BigDecimal input) {

//		this.info("divThousand input = " + input);

		if (input == null) {
			return BigDecimal.ZERO;
		}

		input = computeDivide(input, new BigDecimal("1000"), 3);

//		this.info("divThousand return = " + input);

		return input;
	}
}