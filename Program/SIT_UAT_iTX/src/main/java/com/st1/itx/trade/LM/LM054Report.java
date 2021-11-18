package com.st1.itx.trade.LM;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
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

@Component
@Scope("prototype")

public class LM054Report extends MakeReport {

	@Autowired
	LM054ServiceImpl lM054ServiceImpl;

	@Autowired
	MakeExcel makeExcel;

	@Override
	public void printTitle() {

	}

	// 初始列
	public int row = 7;

	public void exec(TitaVo titaVo) throws LogicException {

		List<Map<String, String>> fnAllList = new ArrayList<>();
		// 取得會計日(同頁面上會計日)
		// 年月日
		int iEntdy = Integer.valueOf(titaVo.get("ENTDY")) + 19110000;
		// 年
		int iYear = (Integer.valueOf(titaVo.get("ENTDY")) + 19110000) / 10000;
		// 月
		int iMonth = ((Integer.valueOf(titaVo.get("ENTDY")) + 19110000) / 100) % 100;

		// 格式
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");

		// 當前日期
		int nowDate = Integer.valueOf(iEntdy);

		Calendar calendar = Calendar.getInstance();

		// 設當年月底日
		// calendar.set(iYear, iMonth, 0);
		calendar.set(Calendar.YEAR, iYear);
		calendar.set(Calendar.MONTH, iMonth - 1);
		calendar.set(Calendar.DATE, calendar.getActualMaximum(Calendar.DATE));

		// 以當前月份取得月底日期 並格式化處理
		int thisMonthEndDate = Integer.valueOf(dateFormat.format(calendar.getTime()));

		this.info("1.thisMonthEndDate=" + thisMonthEndDate);

		String[] dayItem = { "日", "一", "二", "三", "四", "五", "六" };
		// 星期 X (排除六日用) 代號 0~6對應 日到六
		int day = calendar.get(Calendar.DAY_OF_WEEK);
		this.info("day = " + dayItem[day - 1]);
		int diff = 0;
		if (day == 1) {
			diff = -2;
		} else if (day == 6) {
			diff = 1;
		}
		this.info("diff=" + diff);
		calendar.add(Calendar.DATE, diff);
		// 矯正月底日
		thisMonthEndDate = Integer.valueOf(dateFormat.format(calendar.getTime()));
		this.info("2.thisMonthEndDate=" + thisMonthEndDate);
		// 確認是否為1月
		boolean isMonthZero = iMonth - 1 == 0;

		// 當前日期 比 當月底日期 前面 就取上個月底日
		if (nowDate < thisMonthEndDate) {
			iYear = isMonthZero ? (iYear - 1) : iYear;
			iMonth = isMonthZero ? 12 : iMonth - 1;
		}

		this.info("LM054Report exec");

		makeExcel.open(titaVo, titaVo.getEntDyI(), titaVo.getKinbr(), "LM054", "A041重要放款餘額明細表", "LM054-A041重要放款餘額明細表",
				"LM054_底稿_A041放款餘額彙總表.xlsx", "A041重要放款餘額明細表(大額、逾期、催收、國外)");
		makeExcel.setValue(2, 3, iYear * 100 + iMonth);
		try {
			fnAllList = lM054ServiceImpl.findAll(titaVo, "N");

			makeExcel.setShiftRow(row + 1, fnAllList.size() + 1);

			this.info("size=" + fnAllList.size());

			exportExcel(fnAllList);

			fnAllList = lM054ServiceImpl.findAll(titaVo, "Y");

			exportExcel(fnAllList);

		} catch (Exception e) {
			StringWriter errors = new StringWriter();
			e.printStackTrace(new PrintWriter(errors));
			this.info("LM054ServiceImpl.findAll error = " + errors.toString());
		}

		long sno = makeExcel.close();
		makeExcel.toExcel(sno);
	}

	private void exportExcel(List<Map<String, String>> lM054tLDVo) throws LogicException {

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
			//decimal 等於0表示相同 
			if (templineAmt.compareTo(f14) == 0) {
				templineAmt = BigDecimal.ZERO;
			} else {
				templineAmt = f14;
			}
			makeExcel.setValue(row, 16, templineAmt, "#,##0");
			// 擔保品核貸金額
			makeExcel.setValue(row, 17, new BigDecimal(lM054Vo.get("F15")), "#,##0");
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

			memo = lM054Vo.get("F22") + "  " + lM054Vo.get("F23");

			if (tempNo.equals(lM054Vo.get("F0"))) {

				memo += "  同一擔保品";
			}

			tempNo = lM054Vo.get("F0");
			// 備註
			makeExcel.setValue(row, 24, memo, "C");

			// 逾期天數
			makeExcel.setValue(row, 25,
					Integer.valueOf(lM054Vo.get("F24")) == -1 ? 0 : Integer.valueOf(lM054Vo.get("F24")), "C");

		}

	}

	// A041重要放款餘額明細表
	private void sheetA01() throws LogicException {

		makeExcel.setSheet("A041重要放款餘額明細表(大額、逾期、催收、國外)");

		// 申報年月
		makeExcel.setValue(2, 3, "");

		// B 放款代號
		makeExcel.formulaCaculate(7, 2);
		// C 放款種類
		makeExcel.setValue(7, 3, "");
		// D 放款對象名稱
		makeExcel.formulaCaculate(7, 4);
		// E 放款對象關係人代碼
		makeExcel.setValue(7, 5, "");
		// F 利害關係人代碼
		makeExcel.setValue(7, 6, "");
		// G 是否為專案運用公共及社會福利事業投資
		makeExcel.formulaCaculate(7, 7);
		// H 聯合貸款
		makeExcel.formulaCaculate(7, 8);
		// I 持有資產幣別
		makeExcel.setValue(7, 9, "");
		// J 放款日期
		makeExcel.formulaCaculate(7, 10);
		// K 到期日期
		makeExcel.formulaCaculate(7, 11);
		// L 放款年利率
		makeExcel.formulaCaculate(7, 12);
		// M 到放款餘額
		makeExcel.formulaCaculate(7, 13);
		// N 應收利息
		makeExcel.formulaCaculate(7, 14);
		// O 擔保品 設定順位
		makeExcel.setValue(7, 15, "");
		// P 擔保品 估計總值
		makeExcel.setValue(7, 16, "");
		// Q 擔保品 核貸金額
		makeExcel.formulaCaculate(7, 17);
		// R 轉催收日期
		makeExcel.formulaCaculate(7, 18);
		// S 催收狀態
		makeExcel.setValue(7, 19, "");
		// T 評估分類
		makeExcel.setValue(7, 20, "");
		// U 轉催收日期
		makeExcel.formulaCaculate(7, 21);
		// V 評估分類
		makeExcel.setValue(7, 22, "");
		// W IFRS9評估階段
		makeExcel.setValue(7, 23, "");
		// Y 逾期繳款天數
		makeExcel.formulaCaculate(7, 25);

	}
}
