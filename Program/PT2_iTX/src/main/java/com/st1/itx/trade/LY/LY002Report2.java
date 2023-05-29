package com.st1.itx.trade.LY;

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
import com.st1.itx.db.service.springjpa.cm.LY002ServiceImpl;
import com.st1.itx.util.common.MakeExcel;
import com.st1.itx.util.common.MakeReport;
import com.st1.itx.util.common.data.ReportVo;

@Component
@Scope("prototype")

public class LY002Report2 extends MakeReport {

	@Autowired
	public LY002ServiceImpl lY002ServiceImpl;

	@Autowired
	public MakeExcel makeExcel;

	@Override
	public void printTitle() {

	}

	// 估計總值
	List<Map<String, Object>> mergeEva = new ArrayList<Map<String, Object>>();
	Map<String, Object> mergeEvaMap = null;
	int countEva = 1;

	// 核貸金額
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
		String fileItem = "A141重要放款餘額明細表";
		String fileName = "LY002-A141重要放款餘額明細表";

		// 1 舊版報表，2新版報表
		String defaultExcel = "LY002_底稿_A141重要放款餘額明細表(新).xlsx";

		String defaultSheet = "表14-1";

		ReportVo reportVo = ReportVo.builder().setRptDate(reportDate).setBrno(brno).setRptCode(txcd)
				.setRptItem(fileItem).build();

		// 開啟報表

		makeExcel.open(titaVo, reportVo, fileName, defaultExcel, defaultSheet);

		List<Map<String, String>> lY002List = null;
		// 年月底
		int endOfYearMonth = (Integer.valueOf(titaVo.getParam("RocYear")) + 1911) * 100 + 12;

		int rocYear = Integer.valueOf(titaVo.getParam("RocYear"));
		int rocMonth = 12;

		makeExcel.setValue(2, 3, (rocYear + 1911) * 100 + rocMonth);

		try {

			// 各筆資料
			lY002List = lY002ServiceImpl.findAll(titaVo, endOfYearMonth, "N");

			makeExcel.setShiftRow(row, lY002List.size() + 4);

			eptExcel(lY002List);

			// 科目
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

	private void eptExcel(List<Map<String, String>> lY002tLDVo) throws LogicException {

		String tempNo = "";
		String memo = "";

		for (Map<String, String> r : lY002tLDVo) {

			// 項目(戶號+額度)
			makeExcel.setValue(row, 1, r.get("F25"), "C");
			// 放款代號(戶號)
			makeExcel.setValue(row, 2, r.get("F0"), "C");
			// 放款種類
			makeExcel.setValue(row, 3, r.get("F1"), "C");
			// 放款對象名稱
			makeExcel.setValue(row, 4, r.get("F2"), "L");
			// 放款對象關係人代碼
			makeExcel.setValue(row, 5, r.get("F3"), "C");
			// 利害關係人代碼
			makeExcel.setValue(row, 6, r.get("F4"), "C");
			// 是否為專案運用公共及社會福利事業投資
			makeExcel.setValue(row, 7, r.get("F5"), "C");
			// 是否為聯合貸款
			makeExcel.setValue(row, 8, r.get("F6"), "C");
			// 持有資產幣別
			makeExcel.setValue(row, 9, r.get("F7"), "C");
			// 放款日期
			makeExcel.setValue(row, 10, r.get("F8"), "C");
			// 到期日期
			makeExcel.setValue(row, 11, r.get("F9"), "C");
			// 放款年利率
			makeExcel.setValue(row, 12, r.get("F10"), "R");
			// 放款餘額
			makeExcel.setValue(row, 13, new BigDecimal(r.get("F11")), "#,##0");
			// 應收利息
			makeExcel.setValue(row, 14, new BigDecimal(r.get("F12")), "#,##0");
			// 擔保品設定順位
			makeExcel.setValue(row, 15, r.get("F13"), "C");

			// 擔保品估計總值
			BigDecimal templineAmt = BigDecimal.ZERO;
			BigDecimal f14 = new BigDecimal(r.get("F14").toString());
			// decimal 等於0表示相同
			if (templineAmt.compareTo(f14) == 0) {
				templineAmt = BigDecimal.ZERO;
			} else {
				templineAmt = f14;
			}
			if (!tempNo.equals(r.get("F0"))) {
				makeExcel.setValue(row, 16, templineAmt, "#,##0");
				// 擔保品核貸金額
				makeExcel.setValue(row, 17, new BigDecimal(r.get("F15")), "#,##0");
			}

			// 轉催收日期
			makeExcel.setValue(row, 18, r.get("F16"), "C");
			// 催收狀態
			makeExcel.setValue(row, 19, r.get("F17"), "C");
			// 催收狀態執行日期
			makeExcel.setValue(row, 20, r.get("F18"), "C");

			BigDecimal allowanceForLose = BigDecimal.ZERO;
			// 備抵損失總額
			// 參考報表中公式
			if (r.get("F20").equals("1")) {
				allowanceForLose = new BigDecimal(r.get("F11")).multiply(new BigDecimal("0.005"));
			} else if (r.get("F20").equals("2")) {
				allowanceForLose = new BigDecimal(r.get("F11")).multiply(new BigDecimal("0.02"));
			} else if (r.get("F20").equals("3")) {
				allowanceForLose = new BigDecimal(r.get("F11")).multiply(new BigDecimal("0.1"));
			} else if (r.get("F20").equals("4")) {
				allowanceForLose = new BigDecimal(r.get("F11")).multiply(new BigDecimal("0.5"));
			} else if (r.get("F20").equals("5")) {
				allowanceForLose = new BigDecimal(r.get("F11"));
			}
			makeExcel.setValue(row, 21, allowanceForLose.intValue(), "#,##0");

			// 評估分類
			makeExcel.setValue(row, 22, r.get("F20"), "C");

			int ifrs9 = 0;
			// IFRS9評估階段
			// 參考報表中公式
			if (Integer.valueOf(r.get("F24")) >= 0 && Integer.valueOf(r.get("F24")) < 30) {
				ifrs9 = 1;
			} else if (Integer.valueOf(r.get("F24")) < 90) {
				ifrs9 = 2;
			} else if (Integer.valueOf(r.get("F24")) >= 90) {
				ifrs9 = 3;
			} else if (Integer.valueOf(r.get("F24")) == -1) {
				ifrs9 = 2;
			}
			makeExcel.setValue(row, 23, ifrs9, "C");

			ArrayList<String> mark = new ArrayList<String>();
			if (r.get("F22").length() > 1) {
				mark.add(r.get("F22"));
			}
			if (r.get("F23").length() > 1) {
				mark.add(r.get("F23"));
			}
			if (tempNo.equals(r.get("F0")) || r.get("F0").length() != 8) {
				mark.add("同一擔保品");
			}

			for (int i = 0; i < mark.size(); i++) {
				memo += mark.get(i) + "、";
			}

			tempNo = r.get("F0");
			// 備註
			makeExcel.setValue(row, 24, memo.length() > 0 ? memo.substring(0, memo.length() - 1) : memo, "C");
			memo = "";
			mark = null;

			// 逾期天數
			makeExcel.setValue(row, 25, Integer.valueOf(r.get("F24")) == -1 ? 0 : Integer.valueOf(r.get("F24")), "C");
			row++;
		}

	}

}
