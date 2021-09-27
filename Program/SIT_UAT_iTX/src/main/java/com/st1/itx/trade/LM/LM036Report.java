package com.st1.itx.trade.LM;

import java.io.PrintWriter;
import java.io.StringWriter;
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

		setPortfolio(lMonthlyLM036Portfolio);

		List<Map<String, String>> listDelinquency = null;

		try {
			listDelinquency = lM036ServiceImpl.queryDelinquency(startMonth, endMonth, titaVo);
		} catch (Exception e) {
			StringWriter errors = new StringWriter();
			e.printStackTrace(new PrintWriter(errors));
			this.error("lM012ServiceImpl.findAll error = " + errors.toString());
		}

		setDelinquency(listDelinquency);

		long sno = makeExcel.close();
		makeExcel.toExcel(sno);
	}

	private void setDelinquency(List<Map<String, String>> list) throws LogicException {

		// 指向工作表Deliquency
		makeExcel.setSheet("Deliquency");

		if (list == null || list.isEmpty()) {
			this.info("list Deliquency is null or empty");
			makeExcel.setValue(3, 3, "本日無資料");
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

	private BigDecimal formatMillion(String portfolioTotal) {
		return formatMillion(getBigDecimal(portfolioTotal));
	}

	private BigDecimal formatMillion(BigDecimal portfolioTotal) {
		return computeDivide(portfolioTotal, million, 0);
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
}