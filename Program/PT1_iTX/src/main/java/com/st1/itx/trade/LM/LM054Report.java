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
import com.st1.itx.db.service.springjpa.cm.LM054ServiceImpl;
import com.st1.itx.util.common.MakeExcel;
import com.st1.itx.util.common.MakeReport;
import com.st1.itx.util.common.data.ReportVo;
import com.st1.itx.util.parse.Parse;

@Component
@Scope("prototype")

public class LM054Report extends MakeReport {

	@Autowired
	LM054ServiceImpl lM054ServiceImpl;

	@Autowired
	MakeExcel makeExcel;

	@Autowired
	Parse parse;

	@Override
	public void printTitle() {

	}

	// 初始列
	public int row = 7;

	/**
	 * 執行報表輸出
	 * 
	 * @param titaVo
	 * @param monthDate 西元年月底日
	 * @throws LogicException
	 * 
	 */
	public void exec(TitaVo titaVo, int monthDate) throws LogicException {

		List<Map<String, String>> fnAllList = new ArrayList<>();

		this.info("LM054Report exec");

		// LM054
		String txcd = "LM054";
		// 檔案名稱
		String rptItem = "A041重要放款餘額明細表";
		// 輸出檔名
		String fileName = "LM054-A041重要放款餘額明細表";
		// 底稿名稱
		String defaultName = "LM054_底稿_A041放款餘額彙總表.xlsx";
		// 底稿工作表名
		String defaultSheetName = "A041重要放款餘額明細表(大額、逾期、催收、國外)";

		ReportVo reportVo = ReportVo.builder().setBrno(titaVo.getBrno()).setRptDate(titaVo.getEntDyI()).setRptCode(txcd)
				.setRptItem(rptItem).build();

		makeExcel.open(titaVo, reportVo, fileName, defaultName, defaultSheetName);
//		makeExcel.open(titaVo, titaVo.getEntDyI(), titaVo.getKinbr(), "LM054", "A041重要放款餘額明細表", "LM054-A041重要放款餘額明細表",
//				"LM054_底稿_A041放款餘額彙總表.xlsx", "A041重要放款餘額明細表(大額、逾期、催收、國外)");
		makeExcel.setValue(2, 3, monthDate / 100);
		try {
			fnAllList = lM054ServiceImpl.findAll(titaVo, monthDate, "N");

			makeExcel.setShiftRow(row + 1, fnAllList.size() + 4);

			this.info("size=" + fnAllList.size());

			exportExcel(fnAllList);

			fnAllList = lM054ServiceImpl.findAll(titaVo, monthDate, "Y");

			exportExcel(fnAllList);

		} catch (Exception e) {
			StringWriter errors = new StringWriter();
			e.printStackTrace(new PrintWriter(errors));
			this.info("LM054ServiceImpl.findAll error = " + errors.toString());
		}

		if (fnAllList.size() == 0) {

			makeExcel.setValue(7, 3, "本日無資料");

		}

		makeExcel.close();

	}

	private void exportExcel(List<Map<String, String>> lM054tLDVo) throws LogicException {

		String tempNo = "";
		String memo = "";
		String tmpClNo = lM054tLDVo.get(0).get("ClNo").toString();
		// 筆數
		int cnt = 0;

		for (Map<String, String> lM054Vo : lM054tLDVo) {
			cnt++;
			row++;
			// 項目(戶號+額度)
			makeExcel.setValue(row, 1, lM054Vo.get("CFBNo"), "C");
			// 放款代號(戶號)
			makeExcel.setValue(row, 2, lM054Vo.get("CustNo"), "C");
			// 放款種類
			makeExcel.setValue(row, 3, lM054Vo.get("LoanType"), "C");
			// 放款對象名稱
			makeExcel.setValue(row, 4, lM054Vo.get("CustName"), "L");
			// 放款對象關係人代碼
			makeExcel.setValue(row, 5, lM054Vo.get("LoanRelCode"), "C");
			// 利害關係人代碼
			makeExcel.setValue(row, 6, lM054Vo.get("RelCode"), "C");
			// 是否為專案運用公共及社會福利事業投資
			makeExcel.setValue(row, 7, lM054Vo.get("isNotGoverDisCode"), "C");
			// 是否為聯合貸款
			makeExcel.setValue(row, 8, lM054Vo.get("SyndNo"), "C");
			// 持有資產幣別
			makeExcel.setValue(row, 9, lM054Vo.get("Currency"), "C");
			// 放款日期
			makeExcel.setValue(row, 10, parse.stringToInteger(lM054Vo.get("DrawdownDate")), "C");
			// 到期日期
			makeExcel.setValue(row, 11, parse.stringToInteger(lM054Vo.get("MaturityDate")), "C");
			// 放款年利率
			makeExcel.setValue(row, 12, lM054Vo.get("StoreRate"), "0.0000", "R");
			// 放款餘額
			makeExcel.setValue(row, 13, new BigDecimal(lM054Vo.get("LoanBalance")), "#,##0");
			// 應收利息
			makeExcel.setValue(row, 14, new BigDecimal(lM054Vo.get("Int")), "#,##0");
			// 擔保品設定順位
			makeExcel.setValue(row, 15, lM054Vo.get("LoanSeq"), "C");

			// 擔保品估計總值
			BigDecimal templineAmt = BigDecimal.ZERO;
			BigDecimal f14 = new BigDecimal(lM054Vo.get("EvaAmt").toString());
			// decimal 等於0表示相同
			if (templineAmt.compareTo(f14) == 0) {
				templineAmt = BigDecimal.ZERO;
			} else {
				templineAmt = f14;
			}
			if (!tempNo.equals(lM054Vo.get("F0"))) {
				makeExcel.setValue(row, 16, templineAmt, "#,##0");
				// 擔保品核貸金額
				makeExcel.setValue(row, 17, new BigDecimal(lM054Vo.get("LineAmt")), "#,##0");
			}

			// 轉催收日期
			makeExcel.setValue(row, 18, lM054Vo.get("OvduDate"), "C");
			// 催收狀態
			makeExcel.setValue(row, 19, lM054Vo.get("ResultCode"), "C");
			// 催收狀態執行日期
			makeExcel.setValue(row, 20, lM054Vo.get("TelDate"), "C");

			BigDecimal allowanceForLose = BigDecimal.ZERO;
			// 備抵損失總額
			// 參考報表中公式
			if (lM054Vo.get("Class").equals("1")) {
				allowanceForLose = new BigDecimal(lM054Vo.get("LoanBalance")).multiply(new BigDecimal("0.005")).setScale(0,
						BigDecimal.ROUND_HALF_UP);
			} else if (lM054Vo.get("Class").equals("2")) {
				allowanceForLose = new BigDecimal(lM054Vo.get("LoanBalance")).multiply(new BigDecimal("0.02")).setScale(0,
						BigDecimal.ROUND_HALF_UP);
			} else if (lM054Vo.get("Class").equals("3")) {
				allowanceForLose = new BigDecimal(lM054Vo.get("LoanBalance")).multiply(new BigDecimal("0.1")).setScale(0,
						BigDecimal.ROUND_HALF_UP);
			} else if (lM054Vo.get("Class").equals("4")) {
				allowanceForLose = new BigDecimal(lM054Vo.get("LoanBalance")).multiply(new BigDecimal("0.5")).setScale(0,
						BigDecimal.ROUND_HALF_UP);
			} else if (lM054Vo.get("Class").equals("5")) {
				allowanceForLose = new BigDecimal(lM054Vo.get("LoanBalance"));
			}
			makeExcel.setValue(row, 21, allowanceForLose.intValue(), "#,##0");

			// 評估分類
			makeExcel.setValue(row, 22, lM054Vo.get("Class"), "C");

			int ifrs9 = 0;
			// IFRS9評估階段
			// 參考報表中公式
			if (Integer.valueOf(lM054Vo.get("OvduDays")) >= 0 && Integer.valueOf(lM054Vo.get("OvduDays")) < 30) {
				ifrs9 = 1;
			} else if (Integer.valueOf(lM054Vo.get("OvduDays")) < 90) {
				ifrs9 = 2;
			} else if (Integer.valueOf(lM054Vo.get("OvduDays")) >= 90) {
				ifrs9 = 3;
			} else if (Integer.valueOf(lM054Vo.get("OvduDays")) == -1) {
				ifrs9 = 2;
			}
			makeExcel.setValue(row, 23, ifrs9, "C");

			ArrayList<String> mark = new ArrayList<String>();
			if (lM054Vo.get("Protocol").length() > 1) {
				mark.add(lM054Vo.get("Protocol"));
			}
			if (lM054Vo.get("GoverDis").length() > 1) {
				mark.add(lM054Vo.get("GoverDis"));
			}

			if (!"999".equals(tmpClNo) && tmpClNo.equals(lM054Vo.get("ClNo")) && cnt > 1) {
				mark.add("同一擔保品");
			}


			for (int i = 0; i < mark.size(); i++) {
				memo += mark.get(i) + "、";
			}

			tempNo = lM054Vo.get("CustNo");
			// 備註
			makeExcel.setValue(row, 24, memo.length() > 0 ? memo.substring(0, memo.length() - 1) : memo, "C");
			memo = "";
			mark = null;

			// 逾期天數
			makeExcel.setValue(row, 25,
					Integer.valueOf(lM054Vo.get("OvduDays")) == -1 ? 0 : Integer.valueOf(lM054Vo.get("OvduDays")), "C");

		}

	}

}
