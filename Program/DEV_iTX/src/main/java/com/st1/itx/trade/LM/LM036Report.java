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
import com.st1.itx.db.domain.MonthlyLM036Portfolio;
import com.st1.itx.db.service.MonthlyLM036PortfolioService;
import com.st1.itx.db.service.springjpa.cm.LM036ServiceImpl;
import com.st1.itx.util.common.MakeExcel;
import com.st1.itx.util.common.MakeReport;

@Component
@Scope("prototype")
public class LM036Report extends MakeReport {

	@Autowired
	LM036ServiceImpl lM036ServiceImpl;

	@Autowired
	MakeExcel makeExcel;

	@Autowired
	MonthlyLM036PortfolioService sMonthlyLM036PortfolioService;

	private BigDecimal million = getBigDecimal("1000000");

	public void exec(TitaVo titaVo) throws LogicException {

		makeExcel.open(titaVo, titaVo.getEntDyI(), titaVo.getKinbr(), "LM036", "第一類各項統計表", "LM036第一類各項統計表",
				"LM036_底稿_第一類各項統計表.xlsx", "Portfolio");

		int entdy = titaVo.getEntDyI() + 19110000;

		int startMonth = entdy / 100 - 100;
		int endMonth = entdy / 100;

		this.info("startMonth = " + startMonth);
		this.info("endMonth = " + endMonth);

		Slice<MonthlyLM036Portfolio> sMonthlyLM036Portfolio = sMonthlyLM036PortfolioService
				.findDataMonthBetween(startMonth, endMonth, 0, Integer.MAX_VALUE, titaVo);

		List<MonthlyLM036Portfolio> lMonthlyLM036Portfolio = new ArrayList<>(sMonthlyLM036Portfolio.getContent());

		// 表一
		setPortfolio(lMonthlyLM036Portfolio);

		List<Map<String, String>> listBadRateCounts = null;

		// 表二的範圍為三年，完整的12季
		int startMonth12Seasons = getStartMonth12Seasons(endMonth);

		try {
			listBadRateCounts = lM036ServiceImpl.queryBadRateCounts(startMonth12Seasons, endMonth, titaVo);
		} catch (Exception e) {
			StringWriter errors = new StringWriter();
			e.printStackTrace(new PrintWriter(errors));
			this.error("lM036ServiceImpl queryDelinquency error = " + errors.toString());
		}

		// 表二
		setBadRateCount(listBadRateCounts);

		List<Map<String, String>> listBadRateAmt = null;

		try {
			listBadRateAmt = lM036ServiceImpl.queryBadRateAmt(startMonth12Seasons, endMonth, titaVo);
		} catch (Exception e) {
			StringWriter errors = new StringWriter();
			e.printStackTrace(new PrintWriter(errors));
			this.error("lM036ServiceImpl queryDelinquency error = " + errors.toString());
		}

		// 表三
		setBadRateAmt(listBadRateAmt);

		List<Map<String, String>> listDelinquency = null;

		try {
			listDelinquency = lM036ServiceImpl.queryDelinquency(startMonth, endMonth, titaVo);
		} catch (Exception e) {
			StringWriter errors = new StringWriter();
			e.printStackTrace(new PrintWriter(errors));
			this.error("lM036ServiceImpl queryDelinquency error = " + errors.toString());
		}

		// 表四
		setDelinquency(listDelinquency);

		List<Map<String, String>> listCollection = null;

		try {
			listCollection = lM036ServiceImpl.queryCollection(startMonth, endMonth, titaVo);
		} catch (Exception e) {
			StringWriter errors = new StringWriter();
			e.printStackTrace(new PrintWriter(errors));
			this.error("lM036ServiceImpl queryCollection error = " + errors.toString());
		}

		// 表五
		setCollection(listCollection);

		long sno = makeExcel.close();
		makeExcel.toExcel(sno);
	}

	private BigDecimal formatMillion(BigDecimal portfolioTotal) {
		return computeDivide(portfolioTotal, million, 0);
	}

	private BigDecimal formatMillion(String portfolioTotal) {
		return formatMillion(getBigDecimal(portfolioTotal));
	}

	private String formatMonth(int dataMonth) {
		int year = dataMonth / 100;
		int month = dataMonth % 100;

		String formatMonth = "";
		switch (month) {
		case 1:
			formatMonth = "Jan";
			break;
		case 2:
			formatMonth = "Feb";
			break;
		case 3:
			formatMonth = "Mar";
			break;
		case 4:
			formatMonth = "Apr";
			break;
		case 5:
			formatMonth = "May";
			break;
		case 6:
			formatMonth = "Jun";
			break;
		case 7:
			formatMonth = "Jul";
			break;
		case 8:
			formatMonth = "Aug";
			break;
		case 9:
			formatMonth = "Sep";
			break;
		case 10:
			formatMonth = "Oct";
			break;
		case 11:
			formatMonth = "Nov";
			break;
		case 12:
			formatMonth = "Dec";
			break;
		default:
			break;
		}
		formatMonth += "-" + year % 100;
		return formatMonth;
	}

	private int getStartMonth12Seasons(int endMonth) throws LogicException {

		// % 100 = 月份
		// % 3 => 是否被3整除 = 是否為季底月
		if (endMonth % 100 % 3 != 0) {
			// 未被整除的情況，找出最近的季底月
			if (endMonth % 100 >= 1 && endMonth % 100 <= 3) {
				endMonth = ((endMonth / 100) - 1) * 100 + 12;
			} else {
				endMonth = endMonth - (endMonth % 100 % 3);
			}
		}
		// 回推12季(36個月)
		endMonth = endMonth * 100 + 1;

		this.info("endMonth = " + endMonth);

		dDateUtil.init();
		dDateUtil.setDate_1(endMonth);
		dDateUtil.setMons(-35); // 完整12季的季初月
		int startdate = dDateUtil.getCalenderDay();

		this.info("startdate = " + startdate);

		return startdate / 100;
	}

	private void setBadRateAmt(List<Map<String, String>> list) throws LogicException {

		// 指向工作表Bad Rate-房貸(by金額)
		makeExcel.setSheet("Bad Rate-房貸(by金額)");

		if (list == null || list.isEmpty()) {
			this.info("list BadRateAmt is null or empty");
			makeExcel.setValue(3, 2, "本日無資料");
			return;
		}

		int rowCursor = 4;

		BigDecimal total = BigDecimal.ZERO;
		BigDecimal totalOfBadDebtAmt = BigDecimal.ZERO;

		Map<Integer, BigDecimal> badDebtAmtTotal = new HashMap<>();

		Map<Integer, Integer> rowCursorMap = new HashMap<>();
		Map<Integer, Integer> columnCursorMap = new HashMap<>();

		int columnCursor = 4;

		// 列印逾90天時年月標題，並記錄欄位位置
		for (Map<String, String> m : list) {
			int yearMonth = Integer.parseInt(m.get("F0")) - 191100; // 初貸年月
			int badDebtMonth = Integer.parseInt(m.get("F2")) == 0 ? 0 : Integer.parseInt(m.get("F1")) - 191100; // 逾90天時年月
			if (badDebtMonth == 0) {
				// 加三個月
				// 先拆成年、月
				int tmpYear = yearMonth / 100;
				int tmpMonth = yearMonth % 100;
				// 若跨年,年+1,月+3-12,否則用原年月+3
				int tmpBadDebtMonth = (tmpMonth + 3) > 12 ? (((tmpYear + 1) * 100) + (tmpMonth + 3 - 12))
						: (yearMonth + 3);
				makeExcel.setValue(2, columnCursor, tmpBadDebtMonth); // 逾90天時年月
				columnCursorMap.put(tmpBadDebtMonth, columnCursor);
				columnCursor++;
			}
		}

		for (Map<String, String> m : list) {
			int yearMonth = Integer.parseInt(m.get("F0")) - 191100; // 初貸年月
			BigDecimal counts = getBigDecimal(m.get("F1")); // 初貸件數
			int badDebtMonth = Integer.parseInt(m.get("F2")) == 0 ? 0 : Integer.parseInt(m.get("F1")) - 191100; // 逾90天時年月
			BigDecimal badDebtAmt = getBigDecimal(m.get("F3")); // 逾90天時餘額

			if (badDebtMonth == 0) {
				makeExcel.setValue(rowCursor, 1, yearMonth); // 初貸年月
				makeExcel.setValue(rowCursor, 2, counts, "#,##0"); // 初貸件數
				total = total.add(counts);
				rowCursorMap.put(yearMonth, rowCursor);
				rowCursor++;
			} else {
				if (columnCursorMap != null && columnCursorMap.get(badDebtMonth) != null) {
					columnCursor = columnCursorMap.get(badDebtMonth);
					makeExcel.setValue(rowCursor, columnCursor, badDebtAmt, "#,##0"); // 逾90天件數
				}

				// 計算 每個初貸年月的 逾90天件數 加總
				if (badDebtMonth > 0 && badDebtAmt.compareTo(BigDecimal.ZERO) > 0) {
					if (badDebtAmtTotal.containsKey(yearMonth)) {
						badDebtAmt = badDebtAmt.add(badDebtAmtTotal.get(yearMonth));
					}
					badDebtAmtTotal.put(yearMonth, badDebtAmt);
				}
			}
		}

		for (Map.Entry<Integer, BigDecimal> entrySet : badDebtAmtTotal.entrySet()) {
			int yearMonth = entrySet.getKey();
			BigDecimal badDebtAmt = entrySet.getValue();

			rowCursor = rowCursorMap.get(yearMonth);
			makeExcel.setValue(rowCursor, 3, badDebtAmt, "#,##0"); // 逾90天件數加總
			totalOfBadDebtAmt = totalOfBadDebtAmt.add(badDebtAmt);
		}

		makeExcel.setValue(42, 2, total, "#,##0");
		makeExcel.setValue(42, 3, totalOfBadDebtAmt, "#,##0");
	}

	private void setBadRateCount(List<Map<String, String>> list) throws LogicException {

		// 指向工作表Bad Rate-房貸(by件數)
		makeExcel.setSheet("Bad Rate-房貸(by件數)");

		if (list == null || list.isEmpty()) {
			this.info("list BadRateCount is null or empty");
			makeExcel.setValue(3, 2, "本日無資料");
			return;
		}

		int rowCursor = 4;

		BigDecimal total = BigDecimal.ZERO;
		BigDecimal totalOfBadDebtCounts = BigDecimal.ZERO;

		Map<Integer, BigDecimal> badDebtCountsTotal = new HashMap<>();

		Map<Integer, Integer> rowCursorMap = new HashMap<>();
		Map<Integer, Integer> columnCursorMap = new HashMap<>();

		int columnCursor = 4;

		// 列印逾90天時年月標題，並記錄欄位位置
		for (Map<String, String> m : list) {
			int yearMonth = Integer.parseInt(m.get("F0")) - 191100; // 初貸年月
			int badDebtMonth = Integer.parseInt(m.get("F2")) == 0 ? 0 : Integer.parseInt(m.get("F1")) - 191100; // 逾90天時年月
			if (badDebtMonth == 0) {
				// 加三個月
				// 先拆成年、月
				int tmpYear = yearMonth / 100;
				int tmpMonth = yearMonth % 100;
				// 若跨年,年+1,月+3-12,否則用原年月+3
				int tmpBadDebtMonth = (tmpMonth + 3) > 12 ? (((tmpYear + 1) * 100) + (tmpMonth + 3 - 12))
						: (yearMonth + 3);
				makeExcel.setValue(2, columnCursor, tmpBadDebtMonth); // 逾90天時年月
				columnCursorMap.put(tmpBadDebtMonth, columnCursor);
				columnCursor++;
			}
		}

		for (Map<String, String> m : list) {
			int yearMonth = Integer.parseInt(m.get("F0")) - 191100; // 初貸年月
			BigDecimal counts = getBigDecimal(m.get("F1")); // 初貸件數
			int badDebtMonth = Integer.parseInt(m.get("F2")) == 0 ? 0 : Integer.parseInt(m.get("F1")) - 191100; // 逾90天時年月
			BigDecimal badDebtCounts = getBigDecimal(m.get("F3")); // 逾90天件數

			if (badDebtMonth == 0) {
				makeExcel.setValue(rowCursor, 1, yearMonth); // 初貸年月
				makeExcel.setValue(rowCursor, 2, counts, "#,##0"); // 初貸件數
				total = total.add(counts);
				rowCursorMap.put(yearMonth, rowCursor);
				rowCursor++;
			} else {
				if (columnCursorMap != null && columnCursorMap.get(badDebtMonth) != null) {
					columnCursor = columnCursorMap.get(badDebtMonth);
					makeExcel.setValue(rowCursor, columnCursor, badDebtCounts, "#,##0"); // 逾90天件數
				}

				// 計算 每個初貸年月的 逾90天件數 加總
				if (badDebtMonth > 0 && badDebtCounts.compareTo(BigDecimal.ZERO) > 0) {
					if (badDebtCountsTotal.containsKey(yearMonth)) {
						badDebtCounts = badDebtCounts.add(badDebtCountsTotal.get(yearMonth));
					}
					badDebtCountsTotal.put(yearMonth, badDebtCounts);
				}
			}
		}

		for (Map.Entry<Integer, BigDecimal> entrySet : badDebtCountsTotal.entrySet()) {
			int yearMonth = entrySet.getKey();
			BigDecimal badDebtCounts = entrySet.getValue();

			rowCursor = rowCursorMap.get(yearMonth);
			makeExcel.setValue(rowCursor, 3, badDebtCounts, "#,##0"); // 逾90天件數加總
			totalOfBadDebtCounts = totalOfBadDebtCounts.add(badDebtCounts);
		}

		makeExcel.setValue(42, 2, total, "#,##0");
		makeExcel.setValue(42, 3, totalOfBadDebtCounts, "#,##0");
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

			int thisColumnMonth = Integer.parseInt(m.get("F0"));

//			this.info("thisColumnMonth = " + thisColumnMonth);
//			this.info("lastColumnMonth = " + lastColumnMonth);

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
				rowCursor = 6;
				break;
			default:
				continue;
			}

			makeExcel.setValue(rowCursor, columnCursor, rollingRate, "0.00%"); // 滾動率
		}
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
			makeExcel.setValue(2, columnCursor, formatMonth(Integer.parseInt(m.get("F0"))));

			// 法人
			makeExcel.setValue(4, columnCursor, formatMillion(m.get("F1"))); // F1 法人正常
			makeExcel.setValue(5, columnCursor, formatMillion(m.get("F2"))); // F2 法人逾1~2期
			makeExcel.setValue(6, columnCursor, formatMillion(m.get("F3"))); // F3 法人逾3~6期
			makeExcel.setValue(7, columnCursor, formatMillion(m.get("F4"))); // F4 法人催收
			makeExcel.setValue(8, columnCursor, formatMillion("0"));
			makeExcel.setValue(9, columnCursor, formatMillion("0"));

			// 自然人
			makeExcel.setValue(11, columnCursor, formatMillion(m.get("F5"))); // F5 自然人正常
			makeExcel.setValue(12, columnCursor, formatMillion(m.get("F6"))); // F6 自然人逾1~2期
			makeExcel.setValue(13, columnCursor, formatMillion(m.get("F7"))); // F7 自然人逾3~6期
			makeExcel.setValue(14, columnCursor, formatMillion(m.get("F8"))); // F8 自然人催收
			makeExcel.setValue(15, columnCursor, formatMillion("0"));
			makeExcel.setValue(16, columnCursor, formatMillion("0"));

			// 總額
			makeExcel.setValue(18, columnCursor, formatMillion(m.get("F9"))); // F9 總額正常
			makeExcel.setValue(19, columnCursor, formatMillion(m.get("F10"))); // F10 總額逾1~2期
			makeExcel.setValue(20, columnCursor, formatMillion(m.get("F11"))); // F11 總額逾3~6期
			makeExcel.setValue(21, columnCursor, formatMillion(m.get("F12"))); // F12 總額催收
			makeExcel.setValue(22, columnCursor, formatMillion("0"));
			makeExcel.setValue(23, columnCursor, formatMillion("0"));
			makeExcel.setValue(24, columnCursor, formatMillion(m.get("F13"))); // F13 放款總餘額
			makeExcel.setValue(25, columnCursor, formatMillion("0"));

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
			makeExcel.setValue(3, columnCursor, formatMillion(m.getPortfolioTotal()));
			// 自然人放款
			makeExcel.setValue(4, columnCursor, formatMillion(m.getNaturalPersonLoanBal()));
			// 法人放款
			makeExcel.setValue(5, columnCursor, formatMillion(m.getLegalPersonLoanBal()));
			// 聯貸案
			makeExcel.setValue(6, columnCursor, formatMillion(m.getSyndLoanBal()));
			// 股票質押
			makeExcel.setValue(7, columnCursor, formatMillion(m.getStockLoanBal()));
			// 一般法人放款
			makeExcel.setValue(8, columnCursor, formatMillion(m.getOtherLoanbal()));
			// 溢折價與催收費用
			makeExcel.setValue(9, columnCursor, formatMillion(m.getAmortizeTotal().add(m.getOvduExpense())));

			// 大額授信件餘額 - 自然人1000萬以上 - 件數
			makeExcel.setValue(11, columnCursor, m.getNaturalPersonLargeCounts());
			// 大額授信件餘額 - 自然人1000萬以上 - 金額
			makeExcel.setValue(12, columnCursor, formatMillion(m.getNaturalPersonLargeTotal()));
			// 大額授信件餘額 - 法人3000萬以上 - 件數
			makeExcel.setValue(13, columnCursor, m.getLegalPersonLargeCounts());
			// 大額授信件餘額 - 法人人1000萬以上 - 金額
			makeExcel.setValue(14, columnCursor, formatMillion(m.getLegalPersonLargeTotal()));

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