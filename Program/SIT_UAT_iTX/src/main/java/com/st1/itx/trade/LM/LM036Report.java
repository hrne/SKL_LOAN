package com.st1.itx.trade.LM;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Component;

import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.db.domain.MonthlyLM036Portfolio;
import com.st1.itx.db.service.MonthlyLM036PortfolioService;
import com.st1.itx.db.service.springjpa.cm.LM036ServiceImpl;
import com.st1.itx.util.common.MakeExcel;
import com.st1.itx.util.common.MakeReport;
import com.st1.itx.util.parse.Parse;

@Component
@Scope("prototype")
public class LM036Report extends MakeReport {

	@Autowired
	LM036ServiceImpl lM036ServiceImpl;

	@Autowired
	MakeExcel makeExcel;

	@Autowired
	MonthlyLM036PortfolioService sMonthlyLM036PortfolioService;

	@Autowired
	Parse parse;
	
	public int startRow = 0;

	public void exec(TitaVo titaVo, int thisYM) throws LogicException {

//		int entdy = titaVo.getEntDyI() + 19110000;

		int startMonth = thisYM - 100;
		int endMonth = thisYM;
		int startMonth12Seasons = getStartMonth12Seasons(endMonth);

		// 只看月份來判斷要產多少筆
		int iMonth = thisYM % 100;
		// 因一季3個月 所以 月份/3 餘數 1=1個月,2=2個月,0=3個月 基本要找 39個月( XXX年一季~XXX+3年一季)
		int useExcel = iMonth % 3 == 1 ? 2 : iMonth % 3 == 2 ? 3 : 1;
		this.startRow =  iMonth % 3 == 1 ? 47 : iMonth % 3 == 2 ? 45 : 46;
//		this.info("Excel" + useExcel);

		this.info("startMonth = " + startMonth);
		this.info("endMonth = " + endMonth);
		this.info("startMonth12Seasons = " + startMonth12Seasons);

		makeExcel.open(titaVo, titaVo.getEntDyI(), titaVo.getKinbr(), "LM036", "第一類各項統計表", "LM036第一類各項統計表",
				"LM036_底稿_第一類各項統計表" + useExcel + ".xlsx", "Portfolio");

		// 表一 Portfolio
		// 表二 BadRateCount
		// 表三 BadRateAmt
		// 表四 Deliquency
		// 表五 Collection

		// 表一
		startPortfolio(startMonth, endMonth, titaVo);

		// 表二
		startBadRateCount(startMonth12Seasons, endMonth, titaVo);

		// 表三
		startBadRateAmt(startMonth12Seasons, endMonth, titaVo);

		// 表四
		startDeliquency(startMonth, endMonth, titaVo);

		// 表五
		startCollection(startMonth, endMonth, titaVo);

//		long sno = 
		makeExcel.close();
//		makeExcel.toExcel(sno);
	}

	private void startPortfolio(int startMonth, int endMonth, TitaVo titaVo) {
		Slice<MonthlyLM036Portfolio> sMonthlyLM036Portfolio = sMonthlyLM036PortfolioService
				.findDataMonthBetween(startMonth, endMonth, 0, Integer.MAX_VALUE, titaVo);

		List<MonthlyLM036Portfolio> lMonthlyLM036Portfolio = new ArrayList<>(sMonthlyLM036Portfolio.getContent());
		try {
			setPortfolio(lMonthlyLM036Portfolio);
		} catch (LogicException e) {
			this.error(ExceptionUtils.getStackTrace(e));
		}
	}

	private void startBadRateCount(int startMonth12Seasons, int endMonth, TitaVo titaVo) {
		List<Map<String, String>> listBadRateCounts = null;
		List<Map<String, String>> listBadRateCounts2 = null;

		// 表二的範圍為三年，完整的12季
		listBadRateCounts = lM036ServiceImpl.queryBadRateCounts(startMonth12Seasons, endMonth, 0, titaVo);
		listBadRateCounts2 = lM036ServiceImpl.queryBadRateCounts(startMonth12Seasons, endMonth, 1, titaVo);

		try {
			setBadRateCount(listBadRateCounts, listBadRateCounts2);
		} catch (LogicException e) {
			this.error(ExceptionUtils.getStackTrace(e));
		}
	}

	private void startBadRateAmt(int startMonth12Seasons, int endMonth, TitaVo titaVo) {
		List<Map<String, String>> listBadRateAmt = null;
		List<Map<String, String>> listBadRateAmt2 = null;
		listBadRateAmt = lM036ServiceImpl.queryBadRateAmt(startMonth12Seasons, endMonth, 0, titaVo);
		listBadRateAmt2 = lM036ServiceImpl.queryBadRateAmt(startMonth12Seasons, endMonth, 1, titaVo);

		try {
			setBadRateAmt(listBadRateAmt, listBadRateAmt2);
		} catch (LogicException e) {
			this.error(ExceptionUtils.getStackTrace(e));
		}

	}

	private void startDeliquency(int startMonth, int endMonth, TitaVo titaVo) {
		List<Map<String, String>> listDelinquency = null;
		listDelinquency = lM036ServiceImpl.queryDelinquency(startMonth, endMonth, titaVo);

		try {
			setDelinquency(listDelinquency);
		} catch (LogicException e) {
			this.error(ExceptionUtils.getStackTrace(e));
		}
	}

	private void startCollection(int startMonth, int endMonth, TitaVo titaVo) {
		List<Map<String, String>> listCollection = null;
		listCollection = lM036ServiceImpl.queryCollection(startMonth, endMonth, titaVo);

		try {
			setCollection(listCollection);
		} catch (LogicException e) {
			this.error(ExceptionUtils.getStackTrace(e));
		}
	}

	private static enum monthNames {
		Zero, Jan, Feb, Mar, Apr, May, Jun, Jul, Aug, Sep, Oct, Nov, Dec;
	}

	private String formatMonth(int dataMonth) {
		int year = dataMonth / 100;
		int month = dataMonth % 100;

		if (month < 1 || 12 < month) {
			this.warn("formatMonth got weird input ! ");
			this.warn("dataMonth = " + dataMonth);
			return "";
		}

		String formatMonth = "";

		formatMonth = monthNames.values()[month].name();
		formatMonth += "-" + (year % 100);
		return formatMonth;
	}

	private int getStartMonth12Seasons(int endMonth) throws LogicException {

		// % 100 = 月份
		// % 3 => 是否被3整除 = 是否為季底月
//		if (endMonth % 100 % 3 != 0) {
//			// 未被整除的情況，找出最近的季底月
//			if (endMonth % 100 >= 1 && endMonth % 100 <= 3) {
//				endMonth = ((endMonth / 100) - 1) * 100 + 12;
//			} else {
//				endMonth = endMonth - (endMonth % 100 % 3);
//			}
//		}

		// 只看月份來判斷要產多少筆
		int iMonth = endMonth % 100;
		// 因一季3個月 所以 月份/3 餘數 1=1個月,2=2個月,0=3個月 基本要找 39個月( XXX年一季~XXX+3年一季)
		int rowCount = iMonth % 3 == 1? 2 :iMonth % 3 == 2? 0 : 1;
		// 只有 (餘數1)40 (餘數2)38 (餘數3)39  筆

		// 回推12季(36個月)
		endMonth = endMonth * 100 + 1;

		this.info("enddate = " + endMonth);

		dDateUtil.init();
		dDateUtil.setDate_1(endMonth);
		dDateUtil.setMons(-38 - rowCount); // XXX年一季~XXX+3年一季 + 多的月份
		int startdate = dDateUtil.getCalenderDay();

		this.info("startdate = " + startdate);

		return startdate / 100;
	}

	private void setBadRateAmt(List<Map<String, String>> list, List<Map<String, String>> list2) throws LogicException {

		// 指向工作表Bad Rate-房貸(by金額)
		makeExcel.setSheet("Bad Rate-房貸(by金額)");

		if (list == null || list.isEmpty()) {
			this.info("list BadRateAmt is null or empty");
			makeExcel.setValue(3, 2, "本日無資料");
			return;
		}

		int rowCursor = 4;

		BigDecimal total = BigDecimal.ZERO;

		// 民國年月份列
		Map<Integer, Integer> rowCursorMap = new HashMap<>();
		// 民國年月份欄
		Map<Integer, Integer> columnCursorMap = new HashMap<>();

		// 起始欄位
		int columnCursor = 4;

		// 計次
		int num = 0;

		// 輸出初貸年月(包含逾期90天的)和件數，並紀錄位置
		for (Map<String, String> m : list) {

			int yearMonth = parse.stringToInteger(m.get("F0")) - 191100; // 初貸年月

			BigDecimal counts = getBigDecimal(m.get("F1")); // 初貸件數

			int badDebtMonth = parse.stringToInteger(m.get("F2")) == 0 ? 0
					: parse.stringToInteger(m.get("F2")) - 191100; // 逾90天時年月
			if (badDebtMonth == 0) {
				num++;
				// 因列數(2)比欄數(A)少3個月，跑到第四個月才開始記年月份
				if (num > 3) {

					// 先拆成年、月
					int tmpYear = yearMonth / 100;
					int tmpMonth = yearMonth % 100;

					// 民國年
					int tmpBadDebtMonth = tmpYear * 100 + tmpMonth;

					makeExcel.setValue(2, columnCursor, tmpBadDebtMonth); // 逾90天時年月

					// 紀錄欄位
					columnCursorMap.put(tmpBadDebtMonth, columnCursor);

					columnCursor++;
				}

				makeExcel.setValue(rowCursor, 1, yearMonth); // 初貸年月

				makeExcel.setValue(rowCursor, 2, counts, "#,##0"); // 初貸件數

				// 紀錄行列數
				rowCursorMap.put(yearMonth, rowCursor);

				total = total.add(counts);

				rowCursor++;
			}

		}
		// 數量合計
//		makeExcel.setValue(rowCursor, 2, total, "#,##0"); 
		makeExcel.formulaCalculate(rowCursor, 2);

		// 輸出90天 件數
		for (Map<String, String> m : list2) {
			int yearMonth = parse.stringToInteger(m.get("F0")) - 191100; // 初貸年月

//			BigDecimal counts = getBigDecimal(m.get("F1")); // 初貸件數

			int badDebtMonth = parse.stringToInteger(m.get("F2")) == 0 ? 0
					: parse.stringToInteger(m.get("F2")) - 191100; // 逾90天時年月

			BigDecimal badDebtAmt = getBigDecimal(m.get("F3")); // 金額

			// 計算初貸年月件數
			if (badDebtMonth > 0) {
				
				// 數量合計
				int rowTemp = rowCursorMap.get(yearMonth) == null ? 0 : rowCursorMap.get(yearMonth) - 3;
				int colTemp = columnCursorMap.get(badDebtMonth) == null ? 0 : columnCursorMap.get(badDebtMonth);

				this.info("row,col=" + rowTemp + "," + colTemp);
				if (rowTemp != 0 && colTemp != 0) {
//					makeExcel.setValue(rowTemp, colTemp, badDebtCounts, "#,##0");

					makeExcel.setValue(rowTemp, colTemp, badDebtAmt, "#,##0");
				}

			}

		}

		// 公式重新整理
		for (int y = 4; y <= 44; y++) {
			makeExcel.formulaCalculate(rowCursor, 3);
		}

		num = 0;
		// 處理累計逾期比率\往來期數 標題
		for (Map<String, String> m : list) {
			int yearMonth = parse.stringToInteger(m.get("F0")) - 191100; // 初貸年月
			int iYear = yearMonth / 100;
			int iMonth = yearMonth % 100;

			int badDebtMonth = parse.stringToInteger(m.get("F2")) == 0 ? 0
					: parse.stringToInteger(m.get("F1")) - 191100; // 逾90天時年月
			if (badDebtMonth == 0) {
				String textQ = "";
				switch (iMonth) {
				case 3:
					textQ = "一";
					break;
				case 6:
					textQ = "二";
					break;
				case 9:
					textQ = "三";
					break;
				case 12:
					textQ = "四";
					break;
				default:
					break;
				}

				int startRow = iMonth % 3 == 1 ? 2 : iMonth % 3 == 2 ? 3 : 1;
				
				if (iMonth == 3 || iMonth == 6 || iMonth == 9 || iMonth == 12) {
					String text = iYear + "年第" + textQ + "季";
					info("text=" + text);
					makeExcel.setValue(this.startRow + num, 2, text, "L");
					num++;
				}
			}

		}

		for (int y = 47; y <= 59; y++) {
			for (int x = 3; x <= 38; x++) {
				makeExcel.formulaCalculate(y, x);

			}
		}

	}

	/**
	 * Bad Rate-房貸(by件數) 資料與表格處理
	 */
	private void setBadRateCount(List<Map<String, String>> list, List<Map<String, String>> list2)
			throws LogicException {

		// 指向工作表Bad Rate-房貸(by件數)
		makeExcel.setSheet("Bad Rate-房貸(by件數)");

		if (list == null || list.isEmpty()) {
			this.info("list BadRateCount is null or empty");
			makeExcel.setValue(3, 2, "本日無資料");
			return;
		}

		int rowCursor = 4;

		BigDecimal total = BigDecimal.ZERO;

		// 民國年月份列
		Map<Integer, Integer> rowCursorMap = new HashMap<>();
		// 民國年月份欄
		Map<Integer, Integer> columnCursorMap = new HashMap<>();

		// 起始欄位
		int columnCursor = 4;

		// 計次
		int num = 0;

		// 輸出初貸年月(包含逾期90天的)和件數，並紀錄位置
		for (Map<String, String> m : list) {

			int yearMonth = parse.stringToInteger(m.get("F0")) - 191100; // 初貸年月

			BigDecimal counts = getBigDecimal(m.get("F1")); // 初貸件數

			int badDebtMonth = parse.stringToInteger(m.get("F2")) == 0 ? 0
					: parse.stringToInteger(m.get("F2")) - 191100; // 逾90天時年月
			if (badDebtMonth == 0) {
				num++;
				// 因列數(2)比欄數(A)少3個月，跑到第四個月才開始記年月份
				if (num > 3) {

					// 先拆成年、月
					int tmpYear = yearMonth / 100;
					int tmpMonth = yearMonth % 100;

					// 民國年
					int tmpBadDebtMonth = tmpYear * 100 + tmpMonth;

					makeExcel.setValue(2, columnCursor, tmpBadDebtMonth); // 逾90天時年月

					// 紀錄欄位
					columnCursorMap.put(tmpBadDebtMonth, columnCursor);
					
					columnCursor++;
				}

				makeExcel.setValue(rowCursor, 1, yearMonth); // 初貸年月

				makeExcel.setValue(rowCursor, 2, counts, "#,##0"); // 初貸件數

				// 紀錄行列數
				rowCursorMap.put(yearMonth, rowCursor);

				total = total.add(counts);

				rowCursor++;
			}

		}
		// 數量合計
//		makeExcel.setValue(rowCursor, 2, total, "#,##0"); 
		makeExcel.formulaCalculate(rowCursor, 2);

		// 輸出90天 件數
		for (Map<String, String> m : list2) {
			int yearMonth = parse.stringToInteger(m.get("F0")) - 191100; // 初貸年月

//			BigDecimal counts = getBigDecimal(m.get("F1")); // 初貸件數

			int badDebtMonth = parse.stringToInteger(m.get("F2")) == 0 ? 0
					: parse.stringToInteger(m.get("F2")) - 191100; // 逾90天時年月

			BigDecimal badDebtCounts = getBigDecimal(m.get("F3")); // 逾90天件數

			// 計算初貸年月件數
			if (badDebtMonth > 0) {

				// 數量合計
				int rowTemp = rowCursorMap.get(yearMonth) == null ? 0 : rowCursorMap.get(yearMonth) - 3;
				int colTemp = columnCursorMap.get(badDebtMonth) == null ? 0 : columnCursorMap.get(badDebtMonth);

				this.info("row,col=" + rowTemp + "," + colTemp);
				if (rowTemp != 0 && colTemp != 0) {
					makeExcel.setValue(rowTemp, colTemp, badDebtCounts, "#,##0");

				}

			}

		}

		// 公式重新整理
		for (int y = 4; y <= 44; y++) {
			makeExcel.formulaCalculate(rowCursor, 3);
		}

		num = 0;
		// 處理累計逾期比率\往來期數 標題
		for (Map<String, String> m : list) {
			int yearMonth = parse.stringToInteger(m.get("F0")) - 191100; // 初貸年月
			int iYear = yearMonth / 100;
			int iMonth = yearMonth % 100;

			int badDebtMonth = parse.stringToInteger(m.get("F2")) == 0 ? 0
					: parse.stringToInteger(m.get("F1")) - 191100; // 逾90天時年月
			if (badDebtMonth == 0) {
				String textQ = "";
				switch (iMonth) {
				case 3:
					textQ = "一";
					break;
				case 6:
					textQ = "二";
					break;
				case 9:
					textQ = "三";
					break;
				case 12:
					textQ = "四";
					break;
				default:
					break;
				}

				
				if (iMonth == 3 || iMonth == 6 || iMonth == 9 || iMonth == 12) {
					String text = iYear + "年第" + textQ + "季";
					this.info("text=" + text);
					makeExcel.setValue(this.startRow + num, 2, text, "L"); // 逾90天時年月
					num++;
				}
			}

		}

		for (int y = 47; y <= 59; y++) {
			for (int x = 3; x <= 38; x++) {
				makeExcel.formulaCalculate(y, x);

			}
		}

	}

	private void setCollection(List<Map<String, String>> list) throws LogicException {

		// 指向工作表Collection
		makeExcel.setSheet("Collection");

		if (list == null || list.isEmpty()) {
			this.info("list Collection is null or empty");
			makeExcel.setValue(3, 2, "本日無資料");
			return;
		}

		int columnCursor = 1;

		int lastColumnMonth = 0;

		for (Map<String, String> m : list) {

			int thisColumnMonth = parse.stringToInteger(m.get("F0"));

			if (lastColumnMonth == 0 || lastColumnMonth != thisColumnMonth) {
				lastColumnMonth = thisColumnMonth;
				columnCursor++;
			}

			// 月份
			makeExcel.setValue(2, columnCursor, formatMonth(thisColumnMonth));

			// 種類
			String type = m.get("F1");

			// 滾動率
			BigDecimal rollingRate = getBigDecimal(m.get("F5"));

			int rowCursor = 0;

			switch (type) {
			case "2": // type 2 = M1 to M2
				rowCursor = 4;
				break;
			case "3": // type 3 = M2 to M3
				rowCursor = 6;
				break;
			case "4": // type 4 = M3 to M4
				rowCursor = 8;
				break;
			case "5": // type 5 = M4 to M5
				rowCursor = 10;
				break;
			case "6": // type 6 = M5 to M6
				rowCursor = 12;
				break;
			case "7": // type 7 = M6 to 轉催收
				rowCursor = 14;
				break;
			case "8": // type 8 = 轉催收 to Loss
				rowCursor = 16;
				break;
			case "9": // type 9 = 催收款回收率(當月逾期總額/上月逾放總額)
				rowCursor = 18;
				break;
			case "10": // type 10 = 年度呆帳累計回收率 (年初到收回呆帳及過期帳-放款/上月追索債權-放款
				rowCursor = 21;
				break;
			default:
				continue;
			}

			makeExcel.setValue(rowCursor, columnCursor, rollingRate, "0.00%"); // 滾動率
		}
	}

	/**
	 * 預期 map 為 ServiceImpl 回傳結果<br>
	 * 從中 get 出所有 F[queryItemNumbers] 的數字進行加總
	 * 
	 * @param map              query 結果
	 * @param queryItemNumbers 指定要用 map 中的哪些 items
	 * @return 指定的 query 結果項目之加總
	 */
	private BigDecimal getTotal(Map<String, String> map, int... queryItemNumbers) {
		BigDecimal bd = BigDecimal.ZERO;

		for (int i : queryItemNumbers) {
			bd = bd.add(getBigDecimal(map.get("F" + i)));
		}

		return bd;
	}

	/**
	 * 純粹在這裡用來稍微比較好讀的除法函數
	 * 
	 * @param dividend 被除數
	 * @param divisor  除數
	 * @param scale    小數點後留幾位
	 * @return 除出來的結果
	 */
	private BigDecimal doDivide(BigDecimal dividend, BigDecimal divisor, int scale) {
		if (dividend == null || divisor == null || divisor.compareTo(BigDecimal.ZERO) == 0) {
			this.error("doDivide got weird input: dividend=" + dividend + " divisor=" + divisor);
			return BigDecimal.ZERO;
		}

		return dividend.divide(divisor, scale, RoundingMode.HALF_UP);
	}

	private void setDelinquency(List<Map<String, String>> list) throws LogicException {

		// 指向工作表Deliquency
		makeExcel.setSheet("Deliquency");

		if (list == null || list.isEmpty()) {
			this.info("list Deliquency is null or empty");
			makeExcel.setValue(3, 2, "本日無資料");
			return;
		}

		int columnCursor = 2;

		for (Map<String, String> m : list) {

			// 月份
			makeExcel.setValue(2, columnCursor, formatMonth(parse.stringToInteger(m.get("F0"))));

			// 法人
			makeExcel.setValue(4, columnCursor, formatAmt(m.get("F1"), 0, 6)); // F1 法人正常
			makeExcel.setValue(5, columnCursor, formatAmt(m.get("F2"), 0, 6)); // F2 法人逾1~2期
			makeExcel.setValue(6, columnCursor, formatAmt(m.get("F3"), 0, 6)); // F3 法人逾3~6期
			makeExcel.setValue(7, columnCursor, formatAmt(m.get("F4"), 0, 6)); // F4 法人催收
			makeExcel.setValue(8, columnCursor, formatAmt(m.get("F5"), 0, 6)); // F5 法人轉銷損失金額
			makeExcel.setValue(9, columnCursor, doDivide(getTotal(m, 3, 4), getTotal(m, 1, 2, 3, 4), 4), "0.00%"); // 法人放款逾放比
																													// F3+F4
																													// /
																													// F1+F2+F3+F4

			// 自然人
			makeExcel.setValue(11, columnCursor, formatAmt(m.get("F6"), 0, 6)); // F6 自然人正常
			makeExcel.setValue(12, columnCursor, formatAmt(m.get("F7"), 0, 6)); // F7 自然人逾1~2期
			makeExcel.setValue(13, columnCursor, formatAmt(m.get("F8"), 0, 6)); // F8 自然人逾3~6期
			makeExcel.setValue(14, columnCursor, formatAmt(m.get("F9"), 0, 6)); // F9 自然人催收
			makeExcel.setValue(15, columnCursor, formatAmt(m.get("F10"), 0, 6)); // F10 自然人轉銷損失金額
			makeExcel.setValue(16, columnCursor, doDivide(getTotal(m, 8, 9), getTotal(m, 6, 7, 8, 9), 4), "0.00%"); // 自然人逾放比
																													// F8+F9
																													// 
																													// F6+F7+F8+F9

			// 總額
			makeExcel.setValue(18, columnCursor, formatAmt(m.get("F11"), 0, 6)); // F11 總額正常
			makeExcel.setValue(19, columnCursor, formatAmt(m.get("F12"), 0, 6)); // F12 總額逾1~2期
			makeExcel.setValue(20, columnCursor, formatAmt(m.get("F13"), 0, 6)); // F13 總額逾3~6期
			makeExcel.setValue(21, columnCursor, formatAmt(m.get("F14"), 0, 6)); // F14 總額催收
			makeExcel.setValue(22, columnCursor, formatAmt(m.get("F15"), 0, 6)); // F15 轉銷損失金額
			makeExcel.setValue(23, columnCursor, formatAmt(m.get("F16"), 0, 6)); // F16溢折價與催收費用
			makeExcel.setValue(24, columnCursor, formatAmt(m.get("F17"), 0, 6)); // F17 放款總餘額
			makeExcel.setValue(25, columnCursor, doDivide(getTotal(m, 13, 14,16), getTotal(m, 17), 4), "0.00%"); // 放款逾放比
																										// F13+F14+F16溢折價 /
																										// F17

			columnCursor++;
		}

	}

	private void setPortfolio(List<MonthlyLM036Portfolio> list) throws LogicException {
		this.info("setPortfolio");

		if (list == null || list.isEmpty()) {
			this.info("list MonthlyLM036Portfolio is null or empty");
			makeExcel.setValue(3, 3, "本日無資料");
			return;
		}

		int columnCursor = 3;

		for (MonthlyLM036Portfolio m : list) {

			// 月底日
			makeExcel.setValue(2, columnCursor, formatMonth(m.getDataMonth()));
			// 授信組合餘額
			makeExcel.setValue(3, columnCursor, formatAmt(m.getPortfolioTotal(), 0, 6));
			// 自然人放款
			makeExcel.setValue(4, columnCursor, formatAmt(m.getNaturalPersonLoanBal(), 0, 6));
			// 法人放款
			makeExcel.setValue(5, columnCursor, formatAmt(m.getLegalPersonLoanBal(), 0, 6));
			// 聯貸案
			makeExcel.setValue(6, columnCursor, formatAmt(m.getSyndLoanBal(), 0, 6));
			// 股票質押
			makeExcel.setValue(7, columnCursor, formatAmt(m.getStockLoanBal(), 0, 6));
			// 一般法人放款
			makeExcel.setValue(8, columnCursor, formatAmt(m.getOtherLoanbal(), 0, 6));
			// 溢折價與催收費用
			makeExcel.setValue(9, columnCursor, formatAmt(m.getAmortizeTotal().add(m.getOvduExpense()), 0, 6));

			// 大額授信件餘額 - 自然人1000萬以上 - 件數
			makeExcel.setValue(11, columnCursor, m.getNaturalPersonLargeCounts());
			// 大額授信件餘額 - 自然人1000萬以上 - 金額
			makeExcel.setValue(12, columnCursor, formatAmt(m.getNaturalPersonLargeTotal(), 0, 6));
			// 大額授信件餘額 - 法人3000萬以上 - 件數
			makeExcel.setValue(13, columnCursor, m.getLegalPersonLargeCounts());
			// 大額授信件餘額 - 法人人1000萬以上 - 金額
			makeExcel.setValue(14, columnCursor, formatAmt(m.getLegalPersonLargeTotal(), 0, 6));

			// 授信組合占比 - 自然人放款占比
			makeExcel.setValue(16, columnCursor, m.getNaturalPersonPercent());
			// 授信組合占比 - 法人放款占比
			makeExcel.setValue(17, columnCursor, m.getLegalPersonPercent());
			// 授信組合占比 - 聯貸案占比
			makeExcel.setValue(18, columnCursor, m.getSyndPercent());
			// 授信組合占比 - 股票質押占比
			makeExcel.setValue(19, columnCursor, m.getStockPercent());
			// 授信組合占比 - 一般法人放款占比
			makeExcel.setValue(20, columnCursor, m.getOtherPercent());

			// 企業放款動用率
			makeExcel.setValue(21, columnCursor, m.getEntUsedPercent());
			// 保單分紅利率
			makeExcel.setValue(22, columnCursor, m.getInsuDividendRate());

			// 當月平均利率 - 自然人放款
			makeExcel.setValue(24, columnCursor, m.getNaturalPersonRate());
			// 當月平均利率 - 法人放款
			makeExcel.setValue(25, columnCursor, m.getLegalPersonRate());
			// 當月平均利率 - 聯貸案
			makeExcel.setValue(26, columnCursor, m.getSyndRate());
			// 當月平均利率 - 股票質押
			makeExcel.setValue(27, columnCursor, m.getStockRate());
			// 當月平均利率 - 一般法人放款
			makeExcel.setValue(28, columnCursor, m.getOtherRate());
			// 放款平均利率
			makeExcel.setValue(29, columnCursor, m.getAvgRate());

			// 房貸通路
			makeExcel.setValue(31, columnCursor, m.getHouseRateOfReturn());
			// 企金通路
			makeExcel.setValue(32, columnCursor, m.getEntRateOfReturn());
			// 放款毛報酬率
			makeExcel.setValue(33, columnCursor, m.getRateOfReturn());

			columnCursor++;
		}

		// 重新計算公式
		for (int i = 2; i <= 33; i++) {
			makeExcel.formulaCaculate(i, columnCursor);
		}
	}
}