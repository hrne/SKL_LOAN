package com.st1.itx.trade.LY;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.db.service.springjpa.cm.LY002ServiceImpl;
import com.st1.itx.util.common.MakeExcel;
import com.st1.itx.util.common.MakeReport;
import com.st1.itx.util.common.data.ReportVo;

@Component
@Scope("prototype")

public class LY002Report extends MakeReport {

	@Autowired
	public LY002ServiceImpl lY002ServiceImpl;

	@Autowired
	public MakeExcel makeExcel;

	@Override
	public void printTitle() {

	}

	List<Map<String, Object>> mergeEva = new ArrayList<Map<String, Object>>();
	Map<String, Object> mergeEvaMap = null;
	int countEva = 1;

	List<Map<String, Object>> mergeLine = new ArrayList<Map<String, Object>>();
	Map<String, Object> mergeLineMap = null;
	int countLine = 1;

	// 初始列
	public int row = 7;

	public boolean exec(TitaVo titaVo) throws LogicException {
		this.info("LY002.exportExcel active");

		int reportDate = titaVo.getEntDyI() + 19110000;
		String brno = titaVo.getBrno();
		String txcd = titaVo.getTxCode();
		String fileItem = "非RBC_表14-1_會計部年度檢查報表";
		String fileName = "LY002-非RBC_表14-1_會計部年度檢查報表";
		String defaultExcel = "LY002_底稿_非RBC_表14-2_會計部年度檢查報表.xlsx";
		String defaultSheet = "表14-1";

	
		ReportVo reportVo = ReportVo.builder().setRptDate(reportDate).setBrno(brno).setRptCode(txcd)
				.setRptItem(fileItem).build();

		// 開啟報表

		makeExcel.open(titaVo, reportVo, fileName, defaultExcel, defaultSheet);
	
//		makeExcel.open(titaVo, titaVo.getEntDyI(), titaVo.getKinbr(), "LY002", "非RBC_表14-1_會計部年度檢查報表",
//				"LY002_非RBC_表14-1_會計部年度檢查報表", "LY002_底稿_非RBC_表14-1_會計部年度檢查報表.xlsx", "表14-1");

		
		
		List<Map<String, String>> lY002List = null;		
		// 年月底
		int endOfYearMonth = (Integer.valueOf(titaVo.getParam("RocYear")) + 1911) * 100 + 12;
		
		int rocYear = Integer.valueOf(titaVo.getParam("RocYear"));
		int rocMonth = 12;
				
		makeExcel.setValue(1, 2, "新光人壽保險股份有限公司 " + rocYear + "年度(" + rocMonth + ")報表");
		

		try {

			lY002List = lY002ServiceImpl.findAll(titaVo, endOfYearMonth, "N");
			
			makeExcel.setShiftRow(row, lY002List.size() + 4);
			
			eptExcel(lY002List);
			
			lY002List = lY002ServiceImpl.findAll(titaVo, endOfYearMonth, "Y");
			
			eptExcel(lY002List);

		} catch (Exception e) {
			// TODO Auto-generated catch block
			StringWriter errors = new StringWriter();
			e.printStackTrace(new PrintWriter(errors));
			this.info("LY002ServiceImpl.exportExcel error = " + errors.toString());
		}
	
		if (lY002List.size() == 0) {
	
			makeExcel.setValue(7, 3, "本日無資料");

		}

		makeExcel.close();


		return true;

	}

	
	private void eptExcel(List<Map<String, String>> lM054tLDVo) throws LogicException {

		String tempNo = "";
		String memo = "";

		for (Map<String, String> lM054Vo : lM054tLDVo) {
			row++;
			// 項目(戶號+額度)
			makeExcel.setValue(row, 1, lM054Vo.get("F25"), "C");
			// 放款代號(戶號)
			makeExcel.setValue(row, 2, lM054Vo.get("F0"), "C");
			// 放款種類
			makeExcel.setValue(row, 3, lM054Vo.get("F1"), "C");
			// 放款對象名稱
			makeExcel.setValue(row, 4, lM054Vo.get("F2"), "L");
			// 放款對象關係人代碼
			makeExcel.setValue(row, 5, lM054Vo.get("F3"), "C");
			// 利害關係人代碼
			makeExcel.setValue(row, 6, lM054Vo.get("F4"), "C");
			// 是否為專案運用公共及社會福利事業投資
			makeExcel.setValue(row, 7, lM054Vo.get("F5"), "C");
			// 是否為聯合貸款
			makeExcel.setValue(row, 8, lM054Vo.get("F6"), "C");
			// 持有資產幣別
			makeExcel.setValue(row, 9, lM054Vo.get("F7"), "C");
			// 放款日期
			makeExcel.setValue(row, 10, lM054Vo.get("F8"), "C");
			// 到期日期
			makeExcel.setValue(row, 11, lM054Vo.get("F9"), "C");
			// 放款年利率
			makeExcel.setValue(row, 12, lM054Vo.get("F10"), "R");
			// 放款餘額
			makeExcel.setValue(row, 13, new BigDecimal(lM054Vo.get("F11")), "#,##0");
			// 應收利息
			makeExcel.setValue(row, 14, new BigDecimal(lM054Vo.get("F12")), "#,##0");
			// 擔保品設定順位
			makeExcel.setValue(row, 15, lM054Vo.get("F13"), "C");

			// 擔保品估計總值
			BigDecimal templineAmt = BigDecimal.ZERO;
			BigDecimal f14 = new BigDecimal(lM054Vo.get("F14").toString());
			// decimal 等於0表示相同
			if (templineAmt.compareTo(f14) == 0) {
				templineAmt = BigDecimal.ZERO;
			} else {
				templineAmt = f14;
			}
			if (!tempNo.equals(lM054Vo.get("F0"))) {
				makeExcel.setValue(row, 16, templineAmt, "#,##0");
				// 擔保品核貸金額
				makeExcel.setValue(row, 17, new BigDecimal(lM054Vo.get("F15")), "#,##0");
			}

			// 轉催收日期
			makeExcel.setValue(row, 18, lM054Vo.get("F16"), "C");
			// 催收狀態
			makeExcel.setValue(row, 19, lM054Vo.get("F17"), "C");
			// 催收狀態執行日期
			makeExcel.setValue(row, 20, lM054Vo.get("F18"), "C");

			BigDecimal allowanceForLose = BigDecimal.ZERO;
			// 備抵損失總額
			// 參考報表中公式
			if (lM054Vo.get("F20").equals("1")) {
				allowanceForLose = new BigDecimal(lM054Vo.get("F11")).multiply(new BigDecimal("0.005"));
			} else if (lM054Vo.get("F20").equals("2")) {
				allowanceForLose = new BigDecimal(lM054Vo.get("F11")).multiply(new BigDecimal("0.02"));
			} else if (lM054Vo.get("F20").equals("3")) {
				allowanceForLose = new BigDecimal(lM054Vo.get("F11")).multiply(new BigDecimal("0.1"));
			} else if (lM054Vo.get("F20").equals("4")) {
				allowanceForLose = new BigDecimal(lM054Vo.get("F11")).multiply(new BigDecimal("0.5"));
			} else if (lM054Vo.get("F20").equals("5")) {
				allowanceForLose = new BigDecimal(lM054Vo.get("F11"));
			}
			makeExcel.setValue(row, 21, allowanceForLose.intValue(), "#,##0");

			// 評估分類
			makeExcel.setValue(row, 22, lM054Vo.get("F20"), "C");

			int ifrs9 = 0;
			// IFRS9評估階段
			// 參考報表中公式
			if (Integer.valueOf(lM054Vo.get("F24")) >= 0 && Integer.valueOf(lM054Vo.get("F24")) < 30) {
				ifrs9 = 1;
			} else if (Integer.valueOf(lM054Vo.get("F24")) < 90) {
				ifrs9 = 2;
			} else if (Integer.valueOf(lM054Vo.get("F24")) >= 90) {
				ifrs9 = 3;
			} else if (Integer.valueOf(lM054Vo.get("F24")) == -1) {
				ifrs9 = 2;
			}
			makeExcel.setValue(row, 23, ifrs9, "C");

			ArrayList<String> mark = new ArrayList<String>();
			if (lM054Vo.get("F22").length() > 1) {
				mark.add(lM054Vo.get("F22"));
			}
			if (lM054Vo.get("F23").length() > 1) {
				mark.add(lM054Vo.get("F23"));
			}
			if (tempNo.equals(lM054Vo.get("F0")) || lM054Vo.get("F0").length() != 8) {
				mark.add("同一擔保品");
			}

			for (int i = 0; i < mark.size(); i++) {
				memo += mark.get(i) + "、";
			}

			tempNo = lM054Vo.get("F0");
			// 備註
			makeExcel.setValue(row, 24, memo.length() > 0 ? memo.substring(0, memo.length() - 1) : memo, "C");
			memo = "";
			mark = null;

			// 逾期天數
			makeExcel.setValue(row, 25,
					Integer.valueOf(lM054Vo.get("F24")) == -1 ? 0 : Integer.valueOf(lM054Vo.get("F24")), "C");

		}

	}
	
	
	/*
	 * F0 戶號 F1 ID 統編或身分證 F2 客戶名稱 F3 利害關係人 F4 與本公司之關係 F5 放款種類 F6 放款科目 F7 放款年月日 F8
	 * 放款到期年月日 F9 放款年利率% F10 付息方式 F11 最後腳昔日 F12 提供人代號(統編或身分證) F13 提供人姓名 F14 設定順位 F15
	 * 估計總值 F16 核貸金額 F17 NTD F18 放款餘額 F19 應收利息 F20 資產分類
	 * 
	 */
	/**
	 * 報表輸出
	 * 
	 * @param lDList
	 */
	private void exportExcel(List<Map<String, String>> lDList) throws LogicException {

		int row = 6;

		int count = 0;

		String tempId = "";

		BigDecimal tempEvaAmt = BigDecimal.ZERO;
		BigDecimal tempLineAmt = BigDecimal.ZERO;
		BigDecimal tempLoanAmt = BigDecimal.ZERO;

		String custNo = "";
		String facmNo = "";
		String bormNo = "";
		String clNo = "";

		for (Map<String, String> tLDVo : lDList) {

			row++;

			makeExcel.setShiftRow(row, 1);

			if (!tempId.equals(tLDVo.get("F1").toString())) {
				count++;
				tempId = tLDVo.get("F1").toString();
			}

			// 列號
			makeExcel.setValue(row, 2, count, "C");

			// F1 代號(統一編號或身分證)
			makeExcel.setValue(row, 3, tempId, "L");

			// F2 名稱
			makeExcel.setValue(row, 4, tLDVo.get("F2"), "L");

			// F3 是否為關係人
			makeExcel.setValue(row, 5, tLDVo.get("F3"), "C");

			// F4 與本公司之關係
			makeExcel.setValue(row, 6, tLDVo.get("F4"), "C");

			// F5 放款種類
			makeExcel.setValue(row, 7, tLDVo.get("F5"), "C");

			// F6 放款科目
			makeExcel.setValue(row, 8, tLDVo.get("F6"), "C");

			// F7 放款年月日 (設定格式)
			makeExcel.setValue(row, 9, tLDVo.get("F7"), "C");

			// F8 到期年月日 (設定格式)
			makeExcel.setValue(row, 10, tLDVo.get("F8"), "C");

			// F9 放款年利率% (設定格式)
			makeExcel.setValue(row, 11, tLDVo.get("F9"), "C");

			// F10 付息方式
			makeExcel.setValue(row, 12, tLDVo.get("F10"), "C");

			// F11 最後繳息日 (設定格式)
			makeExcel.setValue(row, 13, tLDVo.get("F11"), "C");

			// F12 提供人代號
			makeExcel.setValue(row, 14, tLDVo.get("F12"), "L");

			// F13 提供人名稱
			makeExcel.setValue(row, 15, tLDVo.get("F13"), "L");

			// F14 設定順位
			makeExcel.setValue(row, 16, tLDVo.get("F14"), "C");

			// F15 估計總值
			tempEvaAmt = tLDVo.get("F15").isEmpty() ? BigDecimal.ZERO : new BigDecimal(tLDVo.get("F15"));

			// F16 核貸金額
			tempLineAmt = tLDVo.get("F16").isEmpty() ? BigDecimal.ZERO : new BigDecimal(tLDVo.get("F16"));

			// F0 戶號
			custNo = tLDVo.get("F0");
			// F21 額度
			facmNo = tLDVo.get("F21");
			// F22 擔保品號碼
			clNo = tLDVo.get("F22");

			bormNo = tLDVo.get("F23");

			makeExcel.setValue(row, 1, custNo + facmNo + bormNo, "L");

			// 合併另外做處理
			checkMergeRegionValue(custNo, facmNo, clNo, tempEvaAmt, tempLineAmt);

			// 對照用
//			makeExcel.setValue(row, 25, custNo, "C");
//			makeExcel.setValue(row, 26, facmNo, "C");
//			makeExcel.setValue(row, 27, clNo, "C");
//			makeExcel.setValue(row, 28, tempEvaAmt, "C");
//			makeExcel.setValue(row, 29, tempLineAmt, "C");

			// F17 幣別
			makeExcel.setValue(row, 19, tLDVo.get("F17"), "C");

			// F18 放款餘額
			tempLoanAmt = tLDVo.get("F18").isEmpty() ? BigDecimal.ZERO : new BigDecimal(tLDVo.get("F18"));
			makeExcel.setValue(row, 20, tempLoanAmt, "#,##0");

			// 占資金總額比率
			makeExcel.setValue(row, 21, 0, "#,##0");

			// 占上年度業主權益比率
			makeExcel.setValue(row, 22, 0, "#,##0");

			// F19 應收利息
			tempLoanAmt = tLDVo.get("F19").isEmpty() ? BigDecimal.ZERO : new BigDecimal(tLDVo.get("F19"));
			makeExcel.setValue(row, 23, tempLoanAmt, "#,##0");

			// F20 資產分類
			int col = tLDVo.get("F20").isEmpty() ? 31 : 30 + Integer.valueOf(tLDVo.get("F20").substring(0, 1));
			makeExcel.setValue(row, col, "V", "C");

		}

		makeExcel.setValue(row + 1, 2, count + 1, "C");
		makeExcel.setValue(row + 2, 2, count + 2, "C");
		makeExcel.setValue(row + 3, 2, count + 3, "C");
		makeExcel.setValue(row + 4, 2, count + 4, "C");
		makeExcel.setValue(row + 5, 2, count + 5, "C");

		// 要給擔保品判斷列數
		this.row = row;

		this.info("mergeEva=" + mergeEva.toString());

		this.info("mergeLine=" + mergeLine.toString());

		int sRow = 7;
		int eRow = 0;
		this.info("eva-----");
		// Test2
//		for (int i = 0, length = mergeEva.size(); i < length; i++) {

		Iterator<Map<String, Object>> iter = mergeEva.iterator();

		Map<String, Object> itData = null;

		while (iter.hasNext()) {

			itData = iter.next();

			tempEvaAmt = new BigDecimal(itData.get("eva").toString());

			eRow = sRow + Integer.valueOf(itData.get("count").toString()) - 1;

			if (sRow == eRow) {

				makeExcel.setValue(sRow, 17, tempEvaAmt, "#,##0");

			} else {

				makeExcel.setMergedRegionValue(sRow, eRow, 17, 17, tempEvaAmt, "#,##0");

			}

			sRow = eRow + 1;

		}

		sRow = 7;
		eRow = 0;

		iter = mergeLine.iterator();

		while (iter.hasNext()) {

			itData = iter.next();

			tempLineAmt = new BigDecimal(itData.get("line").toString());

			eRow = sRow + Integer.valueOf(itData.get("count").toString()) - 1;

			if (sRow == eRow) {

				makeExcel.setValue(sRow, 18, tempLineAmt, "#,##0");

			} else {

				makeExcel.setMergedRegionValue(sRow, eRow, 18, 18, tempLineAmt, "#,##0");

			}

			sRow = eRow + 1;

		}

	}

	// 下方表格最後 擔保品放款的分類金額
	/**
	 * 報表輸出(擔保品部分)
	 * 
	 * @param lY002List
	 * @param lrow      目前列數
	 */
	private void exportColl(List<Map<String, String>> lY002List, int lrow)
			throws NumberFormatException, LogicException {
		this.info("go exportColl");
//		BigDecimal evaAmt = BigDecimal.ZERO;
		BigDecimal lineAmt = BigDecimal.ZERO;
		BigDecimal loanAmt = BigDecimal.ZERO;
		BigDecimal interest = BigDecimal.ZERO;

		int row = 0;

		for (Map<String, String> tLDVo : lY002List) {
			// 判斷擔保類型
			row = tLDVo.get("TYPE").equals("A") ? 1
					: tLDVo.get("TYPE").equals("B") ? 2
							: tLDVo.get("TYPE").equals("C") ? 3 : tLDVo.get("TYPE").equals("D") ? 4 : 5;

			// 加上明細最後一筆資料的列數
			row = row + lrow;

			lineAmt = tLDVo.get("LineAmt").isEmpty() ? BigDecimal.ZERO : new BigDecimal(tLDVo.get("LineAmt"));
			loanAmt = tLDVo.get("LoanBalance").isEmpty() ? BigDecimal.ZERO : new BigDecimal(tLDVo.get("LoanBalance"));
			interest = tLDVo.get("Interest").isEmpty() ? BigDecimal.ZERO : new BigDecimal(tLDVo.get("Interest"));

			makeExcel.setValue(row, 18, lineAmt, "#,##0");
			makeExcel.setValue(row, 20, loanAmt, "#,##0");
			makeExcel.setValue(row, 23, interest, "#,##0");

		}

	}

	/**
	 * 估計總值和核貸金額 格式合併處理
	 * 
	 * @param custNo  戶號
	 * @param facmNo  額度
	 * @param clNo    擔保品號碼
	 * @param evaAmt  估計總值
	 * @param lineAmt 核貸金額
	 * 
	 */

	private void checkMergeRegionValue(String custNo, String facmNo, String clNo, BigDecimal evaAmt,
			BigDecimal lineAmt) {

		String tempCustNo = "";
		String tempFacmNo = "";
		String tempClNo = "";

		mergeEvaMap = new HashMap<String, Object>();
		mergeLineMap = new HashMap<String, Object>();

		int l = mergeLine.size() - 1;
		int e = mergeEva.size() - 1;

		// 核貸
		if (mergeLine.size() > 0) {

			BigDecimal tempLine = new BigDecimal(mergeLine.get(l).get("line").toString());
			tempCustNo = mergeLine.get(l).get("cust").toString();
			tempFacmNo = mergeLine.get(l).get("facm").toString();

			// 與前一筆 戶號額度是否一樣
			if (tempCustNo.equals(custNo) && tempFacmNo.equals(facmNo)) {

				countLine++;

				mergeLineMap.put("count", countLine);
				mergeLineMap.put("cust", custNo);
				mergeLineMap.put("facm", facmNo);
				// 核貸 與前一筆金額是否一樣
				if (tempLine.compareTo(lineAmt) == 0) {
					mergeLineMap.put("line", lineAmt);
				} else {
					mergeLineMap.put("line", tempLine.add(lineAmt));
				}
				mergeLine.set(l, mergeLineMap);

			} else {

				countLine = 1;

				mergeLineMap.put("count", countLine);
				mergeLineMap.put("cust", custNo);
				mergeLineMap.put("facm", facmNo);
				mergeLineMap.put("line", lineAmt);
				mergeLine.add(mergeLineMap);

			}

		} else {

			countLine = 1;

			mergeLineMap.put("count", countLine);
			mergeLineMap.put("cust", custNo);
			mergeLineMap.put("facm", facmNo);
			mergeLineMap.put("line", lineAmt);
			mergeLine.add(mergeLineMap);

		}

		// 估計總值
		if (mergeEva.size() > 0) {

			BigDecimal tempEva = new BigDecimal(mergeEva.get(e).get("eva").toString());
			tempClNo = mergeEva.get(e).get("clno").toString();

			// 和前一筆 是否為同一擔保品號碼
			if (tempClNo.equals(clNo)) {

				countEva++;

				mergeEvaMap.put("count", countEva);
				mergeEvaMap.put("clno", clNo);
				// 估計 與前一筆金額是否一樣(一樣為同一擔保品)
				if (tempEva.compareTo(evaAmt) == 0) {
					mergeEvaMap.put("eva", evaAmt);
				} else {
					mergeEvaMap.put("eva", tempEva.add(evaAmt));
				}
				mergeEva.set(e, mergeEvaMap);

			} else {
				countEva = 1;

				mergeEvaMap.put("count", countEva);
				mergeEvaMap.put("clno", clNo);
				mergeEvaMap.put("eva", evaAmt);
				mergeEva.add(mergeEvaMap);

			}

		} else {

			countEva = 1;

			mergeEvaMap.put("count", countEva);
			mergeEvaMap.put("clno", clNo);
			mergeEvaMap.put("eva", evaAmt);
			mergeEva.add(mergeEvaMap);
		}
	}

}
