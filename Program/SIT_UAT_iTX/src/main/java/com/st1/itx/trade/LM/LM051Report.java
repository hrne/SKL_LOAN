package com.st1.itx.trade.LM;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.db.service.springjpa.cm.LM051ServiceImpl;
import com.st1.itx.util.common.MakeExcel;
import com.st1.itx.util.common.MakeReport;

@Component
@Scope("prototype")

public class LM051Report extends MakeReport {

	@Autowired
	LM051ServiceImpl lM051ServiceImpl;

	@Autowired
	MakeExcel makeExcel;

	// 起始列印行數
	int row = 2;

	public void exec(TitaVo titaVo) throws LogicException {

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


		// 上個月初
		calendar.set(Calendar.YEAR, iYear);
		calendar.set(Calendar.MONTH, iMonth - 1);
		calendar.set(Calendar.DATE, calendar.getActualMinimum(Calendar.DATE));
		int lastMonthEndDate = Integer.valueOf(dateFormat.format(calendar.getTime()));

		makeExcel.open(titaVo, titaVo.getEntDyI(), titaVo.getKinbr(), "LM051", "放款資產分類案件明細表_內部控管",
				"LM051_放款資產分類案件明細表_內部控管", "LM051_底稿_放款資產分類案件明細表_內部控管.xlsx", "10804工作表");

		makeExcel.setSheet("10804工作表", titaVo.get("ENTDY").substring(1, 6) + "工作表");

		String mm = String.valueOf(Integer.valueOf(titaVo.get("ENTDY").substring(4, 6)));

		makeExcel.setValue(1, 2, "資產分類案件明細表" + titaVo.get("ENTDY").substring(1, 4) + "." + mm);

		List<Map<String, String>> lM051List = null;

		int dataSize = 0;

		// 有四種不同條件，要select 4次
		for (int i = 1; i <= 4; i++) {
			try {

				lM051List = lM051ServiceImpl.findAll(titaVo, i);

			} catch (Exception e) {
				// TODO Auto-generated catch block
				StringWriter errors = new StringWriter();
				e.printStackTrace(new PrintWriter(errors));
				this.info("LM051ServiceImpl.findAll error = " + errors.toString());
			}
			dataSize += lM051List.size();

			if (lM051List.size() > 0) {

				exportExcel(lM051List);

			}

		}

		if (dataSize == 0) {
			makeExcel.setValue(3, 1, "本日無資料");
		}

		makeExcel.setSheet("總表");

		makeExcel.setValue(12, 9, iMonth + "月月報表數", "C");
		makeExcel.setValue(14, 5, thisMonthEndDate, "C");
		makeExcel.setValue(14, 6, lastMonthEndDate, "C");

		// 有3個表格，要select 3次
		for (int i = 1; i <= 3; i++) {

			try {

				lM051List = lM051ServiceImpl.findAll2(titaVo, i);

			} catch (Exception e) {
				// TODO Auto-generated catch block
				StringWriter errors = new StringWriter();
				e.printStackTrace(new PrintWriter(errors));
				this.info("LM051ServiceImpl.findAll error = " + errors.toString());
			}
			if (lM051List.size() > 0) {
				exportExcel2(lM051List, i);
			}
		}

		long sno = makeExcel.close();
		makeExcel.toExcel(sno);
	}

	// YYYMM工作表

	// F0+F1
	// F0 戶號：2
	// F1 額度：3
	// F2 利變；4
	// F3 戶名；5
	// F4 本金餘額；6
	// F5 科目；7
	// F6 逾期數；8
	// F7 地區別；9
	// F8 繳息日期；10
	// F9分類項目:11
	// F4 五類金額(用F16區分F4)=；12~17
	// F10 分類標準(文字)；18
	// F11 金額 19
	// F12 備註；20
	// F13 基本利率代碼(商品代號)；21

	private void exportExcel(List<Map<String, String>> LDList) throws LogicException {
		this.info("LM051Report exportExcel");

		for (Map<String, String> tLDVo : LDList) {
			row++;
			// F0+F1:1
			makeExcel.setValue(row, 1, tLDVo.get("F0") + tLDVo.get("F1"));
			// F0 戶號：2
			makeExcel.setValue(row, 2, Integer.valueOf(tLDVo.get("F0")));
			// F1 額度：3
			makeExcel.setValue(row, 3, Integer.valueOf(tLDVo.get("F1")), "C");
			// F2 利變；4
			makeExcel.setValue(row, 4, tLDVo.get("F2") == null || tLDVo.get("F2").length() == 0 ? ' ' : tLDVo.get("F2"),
					"C");
			// F3 戶名；5
			makeExcel.setValue(row, 5, tLDVo.get("F3"), "L");
			// F4 本金餘額；6
			if (tLDVo.get("F4").equals("")) {
				makeExcel.setValue(row, 6, 0);
			} else {
				makeExcel.setValue(row, 6, Float.valueOf(tLDVo.get("F4")), "#,##0");
			}

			// F5 科目；7
			makeExcel.setValue(row, 7, Integer.valueOf(tLDVo.get("F5")), "L");
			// F6 逾期數；8

			String ovduText = "";
			if (!tLDVo.get("F18").isEmpty()) {
				if (Integer.valueOf(tLDVo.get("F6")) == 99) {
					// 協or協*or催協
					ovduText = tLDVo.get("F18");
				} else {
					// *協-逾期數
					ovduText = tLDVo.get("F18") + tLDVo.get("F6");
				}
			} else {
				// 逾期數
				ovduText = tLDVo.get("F6");
			}

			makeExcel.setValue(row, 8, ovduText, "C");
			// F7 地區別；9
			makeExcel.setValue(row, 9,
					tLDVo.get("F7") == null || tLDVo.get("F7").length() == 0 ? 0 : Integer.valueOf(tLDVo.get("F7")),
					"C");
			// F8 繳息日期；10
			makeExcel.setValue(row, 10,
					tLDVo.get("F8") == null || tLDVo.get("F8").length() == 0 ? 0 : Integer.valueOf(tLDVo.get("F8")),
					"C");
			// F9分類項目:11
//				makeExcel.setValue(row, 11, tLDVo.get("F9"), "C");
			// F4 五類金額(用F16區分F4)=；12~17
			putAsset(row, tLDVo.get("F4"), tLDVo.get("F16"));
			// F10 分類標準(文字)；18
			String classText = "";
			if (tLDVo.get("F13") == "60" || tLDVo.get("F13") == "61" || tLDVo.get("F13") == "62") {
				if (!tLDVo.get("F10").isEmpty()) {
					classText = "協議戶 /" + tLDVo.get("F10");
				} else {
					classText = "協議戶 ";
				}
			} else {
				classText = tLDVo.get("F10");
			}
			makeExcel.setValue(row, 17, tLDVo.get("F10").isEmpty() ? "核貸估價" : classText, "L");
			// F11 金額 19
			makeExcel.setValue(row, 18, Integer.valueOf(tLDVo.get("F11")), "#,##0");
			// F12 備註；20
			makeExcel.setValue(row, 19, tLDVo.get("F12"), "L");
			// F13 基本利率代碼(商品代號)；21
			makeExcel.setValue(row, 20, tLDVo.get("F13"), "C");
		}

	}

	// 總表
	private void exportExcel2(List<Map<String, String>> LDList, int formNum) throws LogicException {
		this.info("LM051Report exportExcel2");

		int row = 0;
		int col = 0;

		BigDecimal tempAmt = BigDecimal.ZERO;

		for (Map<String, String> tLDVo : LDList) {

			switch (formNum) {
			// F0
			// 1 = 購置住宅+修繕貸款
			// 2 = 建築貸款
			// 3 = 100年後政策性貸款
			// 4 = 股票質押
			// 5 = 無意義
			// TOTAL = 放款餘額

			case 1:
				if (!tLDVo.get("F0").equals("99") && !tLDVo.get("F0").equals("5")) {

					if (tLDVo.get("F0").equals("TOTAL")) {
						row = 12;
						col = 8;
					} else if (tLDVo.get("F0").equals("1")) {
						row = 7;
						col = 3;
					} else if (tLDVo.get("F0").equals("2")) {
						row = 7;
						col = 4;
					} else if (tLDVo.get("F0").equals("3")) {
						row = 7;
						col = 5;
					} else if (tLDVo.get("F0").equals("4")) {
						row = 7;
						col = 6;
					}
					tempAmt = tLDVo.get("F1") == null ? BigDecimal.ZERO : new BigDecimal(tLDVo.get("F1"));
					this.info("row=" + row + ",col=" + col + ",tempAmt=" + tempAmt);

					makeExcel.setValue(row, col, tempAmt, "#,##0");

				}
				break;
			case 2:
				if (!tLDVo.get("F1").equals("99")) {

					if (tLDVo.get("F0").equals("1")) {
						row = 18;
					} else {
						row = 19;
					}

					if (tLDVo.get("F1").equals("1")) {
						col = 3;
					} else {
						col = 4;
					}

					tempAmt = tLDVo.get("F2") == null ? BigDecimal.ZERO : new BigDecimal(tLDVo.get("F2"));

					this.info("row=" + row + ",col=" + col + ",tempAmt=" + tempAmt);

					makeExcel.setValue(row, col, tempAmt, "#,##0");

				}

				break;

			case 3:
				if (!tLDVo.get("F0").equals("99") || !tLDVo.get("F1").equals("99")) {
					if (tLDVo.get("F0").equals("C")) {
						row = 27;
					} else if (tLDVo.get("F0").equals("D")) {
						row = 28;
					} else {
						row = 29;
					}

					if (tLDVo.get("F1").equals("1")) {
						col = 3;
					} else {
						col = 4;
					}

					tempAmt = tLDVo.get("F2") == null ? BigDecimal.ZERO : new BigDecimal(tLDVo.get("F2"));

					this.info("row=" + row + ",col=" + col + ",tempAmt=" + tempAmt);

					makeExcel.setValue(row, col, tempAmt, "#,##0");
				}
				break;
			default:

				break;
			}
			row = 0;
			col = 0;

		}
		// 表格一
		makeExcel.formulaCaculate(10, 3);// C10 特定放款資產 合計
		makeExcel.formulaCaculate(10, 5);// E10 100年後政策性貸款 合計
		makeExcel.formulaCaculate(7, 7);// G7 非特定-個金不動產抵押貸款
		makeExcel.formulaCaculate(9, 3);// C9 特定放款資產 提存
		makeExcel.formulaCaculate(9, 7);// G9 非特定放款資產 提存
		makeExcel.formulaCaculate(4, 8);// H4 法定備呆提存總額1
		makeExcel.formulaCaculate(7, 8);// H7 法定備呆提存總額2
		makeExcel.formulaCaculate(8, 8);// H8 法定備呆提存總額3
		makeExcel.formulaCaculate(11, 8);// H11 放款金額
		// 表格二
		makeExcel.formulaCaculate(20, 3);// C20 購置住宅+修繕貸款
		makeExcel.formulaCaculate(20, 4);// D20 建築貸款
		makeExcel.formulaCaculate(18, 5);// E18 一般
		makeExcel.formulaCaculate(19, 5);// E19 利變
		makeExcel.formulaCaculate(20, 5);// E20 縱向合計
		makeExcel.formulaCaculate(20, 6);// F20 橫向合計
		// 表格三
		makeExcel.formulaCaculate(30, 3);// C30 購置住宅+修繕貸款
		makeExcel.formulaCaculate(30, 4);// D30 建築貸款
		makeExcel.formulaCaculate(27, 5);// E27 C
		makeExcel.formulaCaculate(28, 5);// E28 D
		makeExcel.formulaCaculate(29, 5);// E29 Z
		makeExcel.formulaCaculate(30, 5);// E30 縱向合計
		makeExcel.formulaCaculate(30, 6);// F31 橫向合計

	}

	/**
	 * 資產五分類 欄位位置
	 * 
	 * @param row         列數
	 * @param Map<String, String>
	 * 
	 */

	private void putAsset(int row, String prinBalance, String assetClass) throws LogicException {
		int col = 0;
//		String memo = "";
		switch (assetClass) {
		case "21":
			col = 11;

			break;
		case "22":
			col = 12;
			break;
		case "23":
			col = 13;
			break;
		case "3":
			col = 14;
			break;
		case "4":
			col = 15;
			break;
		case "5":
			col = 16;
			break;
		default:
			col = 0;
			break;
		}
		if (col > 0) {
			makeExcel.setValue(row, col, Float.valueOf(prinBalance), "#,##0");
		}
	}

}
