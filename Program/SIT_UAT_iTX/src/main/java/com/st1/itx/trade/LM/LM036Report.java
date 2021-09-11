package com.st1.itx.trade.LM;

import java.math.BigDecimal;
import java.util.ArrayList;
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

	@Override
	public void printTitle() {

	}

	public void exec(TitaVo titaVo) throws LogicException {

		makeExcel.open(titaVo, titaVo.getEntDyI(), titaVo.getKinbr(), "LM036", "第一類各項統計表", "LM036第一類各項統計表",
				"LM036_底稿_第一類各項統計表.xlsx", "Portfolio");

		int entdy = titaVo.getEntDyI() + 19110000;

		int startMonth = entdy / 100 - 100;
		int endMonth = entdy / 100;

		this.info("startMonth = " + startMonth);
		this.info("endMonth = " + endMonth);

//		this.dDateUtil.init();
//		this.dDateUtil.setDate_1(entdy);
//		this.dDateUtil.setMons(-12);
//		this.dDateUtil.getCalenderDay();
//		this.info("" + this.dDateUtil.getDate_2Integer());

		Slice<MonthlyLM036Portfolio> sMonthlyLM036Portfolio = sMonthlyLM036PortfolioService
				.findDataMonthBetween(startMonth, endMonth, 0, Integer.MAX_VALUE, titaVo);

		if (sMonthlyLM036Portfolio == null || sMonthlyLM036Portfolio.isEmpty()) {
			this.info("sMonthlyLM036Portfolio is null or empty");
			makeExcel.setValue(3, 3, "本日無資料");
		} else {
			List<MonthlyLM036Portfolio> lMonthlyLM036Portfolio = new ArrayList<>(sMonthlyLM036Portfolio.getContent());
			setPortfolio(titaVo, lMonthlyLM036Portfolio);
		}

//		makeExcel.setSheet("Bad Rate-房貸(by件數)");
//		List<Map<String, String>> LM036List1 = null;
//		exportExcel1(titaVo, LM036List1);
//
//		makeExcel.setSheet("Bad Rate-房貸(by金額)");
//		List<Map<String, String>> LM036List2 = null;
//		exportExcel2(titaVo, LM036List2);
//
//		makeExcel.setSheet("Deliquency ");
//		List<Map<String, String>> LM036List3 = null;
//		exportExcel3(titaVo, LM036List3);
//
//		makeExcel.setSheet("Collection");
//		List<Map<String, String>> LM036List4 = null;
//		exportExcel4(titaVo, LM036List4);

		long sno = makeExcel.close();
		makeExcel.toExcel(sno);
	}

	private void setPortfolio(TitaVo titaVo, List<MonthlyLM036Portfolio> list) throws LogicException {
		this.info("setPortfolio");

		if (list == null || list.isEmpty()) {
			makeExcel.setValue(3, 3, "本日無資料");
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

	private BigDecimal formatMillion(BigDecimal portfolioTotal) {
		return computeDivide(portfolioTotal, getBigDecimal("1000000"), 0);
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

	private void exportExcel1(TitaVo titaVo, List<Map<String, String>> LDList) throws LogicException {
		this.info("===========in testExcel");
		if (LDList == null) {
			makeExcel.setValue(4, 2, "本日無資料");
		}
		String entdy = titaVo.get("ENTDY");
		int ym = Integer.parseInt(entdy) / 100;
		for (int i = 0; i < 37; i++) {
			makeExcel.setValue(2, 40 - i, ym);
			makeExcel.setValue(43 - i, 1, ym);
			if ((ym - 1) % 100 == 0) {
				ym -= 89;
			} else {
				ym -= 1;
			}
		}
		for (int i = 0; i < 3; i++) {
			makeExcel.setValue(6 - i, 1, ym);
			if ((ym - 1) % 100 == 0) {
				ym -= 89;
			} else {
				ym -= 1;
			}
		}
	}

	private void exportExcel2(TitaVo titaVo, List<Map<String, String>> LDList) throws LogicException {
		this.info("===========in testExcel");
		if (LDList == null) {
			makeExcel.setValue(4, 2, "本日無資料");
		}
		String entdy = titaVo.get("ENTDY");
		int ym = Integer.parseInt(entdy) / 100;
		for (int i = 0; i < 37; i++) {
			makeExcel.setValue(2, 40 - i, ym);
			makeExcel.setValue(43 - i, 1, ym);
			if ((ym - 1) % 100 == 0) {
				ym -= 89;
			} else {
				ym -= 1;
			}
		}
		for (int i = 0; i < 3; i++) {
			makeExcel.setValue(6 - i, 1, ym);
			if ((ym - 1) % 100 == 0) {
				ym -= 89;
			} else {
				ym -= 1;
			}
		}
	}

	private void exportExcel3(TitaVo titaVo, List<Map<String, String>> LDList) throws LogicException {
		this.info("===========in testExcel");
		if (LDList == null) {
			makeExcel.setValue(3, 2, "本日無資料");
		}
		String entdy = titaVo.get("ENTDY");
		int yy = Integer.parseInt(entdy) / 10000 + 1911;
		int mm = Integer.parseInt(entdy) % 10000 / 100;
		int dd[] = { 31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31 };
		makeExcel.setValue(2, 29, yy + "/" + mm + "/" + dd[mm - 1]);
		int i = 1;
		int col = 29;
		while (i < 28) {
			if (mm - i == 0) {
				yy -= 1;
				mm += 12;
			}
			makeExcel.setValue(2, col - i, yy + "/" + (mm - i) + "/" + dd[mm - i - 1]);
			i++;
		}
	}

	private void exportExcel4(TitaVo titaVo, List<Map<String, String>> LDList) throws LogicException {
		this.info("===========in testExcel");
		if (LDList == null) {
			makeExcel.setValue(4, 2, "本日無資料");
		}
	}
//	private void testExcel(TitaVo titaVo) throws LogicException {
//		this.info("===========in testExcel");
//		makeExcel.open(titaVo, titaVo.getEntDyI(), titaVo.getKinbr(), "LM036", "第一類各項統計表", "LM036-Dashboard_第一類各項統計表_5份", "Dashboard_第一類各項統計表_5份.xlsx", "Portfolio");
//
//		makeExcel.setValue(3, 3, "55,555");
//		makeExcel.setValue(3, 4, "55,555");
//		makeExcel.setValue(4, 3, "44,444");
//		makeExcel.setValue(4, 4, "44,444");
//		makeExcel.setValue(5, 3, "33,333");
//		makeExcel.setValue(5, 4, "33,333");
//		makeExcel.setValue(6, 3, "2,222");
//		makeExcel.setValue(6, 4, "2,222");
//		makeExcel.setValue(7, 3, "9");
//		makeExcel.setValue(7, 4, "9");
//		makeExcel.setValue(16, 3, "81%");
//		makeExcel.setValue(16, 4, "81%");
//
//		makeExcel.setValue(31, 3, "1.66%");
//		makeExcel.setValue(31, 4, "1.33%");
//		makeExcel.setValue(32, 3, "1.08%");
//		makeExcel.setValue(32, 4, "1.12%");
//		makeExcel.setValue(33, 3, "1.13%");
//		makeExcel.setValue(33, 4, "1.21%");
//
//		makeExcel.setSheet("Bad Rate-房貸(by件數)");
//		makeExcel.setValue(4, 2, "100");
//		makeExcel.setValue(5, 2, "100");
//		makeExcel.setValue(6, 42, "200");
//		makeExcel.setValue(44, 2, "100");
//		makeExcel.setValue(44, 42, "200");
//		makeExcel.setValue(46, 3, "1.66%");
//
//		makeExcel.setSheet("Bad Rate-房貸(by金額)");
//		makeExcel.setValue(4, 2, "33,333,333,333");
//		makeExcel.setValue(5, 2, "33,333,333,333");
//		makeExcel.setValue(6, 42, "66,666,666,666");
//		makeExcel.setValue(44, 2, "66,666,666,666");
//		makeExcel.setValue(44, 42, "66,666,666,666");
//		makeExcel.setValue(49, 4, "0.02%");
//
//		makeExcel.setSheet("Deliquency ");
//		makeExcel.setValue(4, 2, "5,555,555");
//		makeExcel.setValue(4, 3, "5,555,555");
//		makeExcel.setValue(5, 2, "44,444");
//		makeExcel.setValue(5, 3, "44,444");
//		makeExcel.setValue(6, 2, "33,333");
//		makeExcel.setValue(6, 3, "33,333");
//		makeExcel.setValue(7, 2, "22,222,222");
//		makeExcel.setValue(7, 3, "22,222,222");
//
//		makeExcel.setSheet("Collection");
//		makeExcel.setValue(4, 2, "1.22%");
//		makeExcel.setValue(4, 3, "2.13%");
//		makeExcel.setValue(4, 26, "1.675%");
//
//		makeExcel.setValue(6, 2, "2.22%");
//		makeExcel.setValue(6, 3, "0.89%");
//		makeExcel.setValue(6, 26, "1.555%");
//		long sno = makeExcel.close();
//		makeExcel.toExcel(sno);
//	}

}
