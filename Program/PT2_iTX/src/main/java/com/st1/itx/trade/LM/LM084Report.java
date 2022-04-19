package com.st1.itx.trade.LM;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.db.service.springjpa.cm.LM084ServiceImpl;
import com.st1.itx.util.common.MakeReport;

@Component
@Scope("prototype")
public class LM084Report extends MakeReport {

	@Autowired
	LM084ServiceImpl lM084ServiceImpl;

	String txcd = "LM084";
	String txnm = "應收利息之帳齡分析表";

	String today = "";
	String nowTime = "";

	String thisMonthEndBsDate = "";

	String lastAging = "";
	String thisPageAging = "一個月以下";

	public void exec(TitaVo titaVo) throws LogicException {
		this.info("LM084Report exec start ...");

		today = dDateUtil.getNowStringBc();
		nowTime = dDateUtil.getNowStringTime();

		String inputDate = Integer.toString(Integer.parseInt(titaVo.getParam("inputYear")) + 1911) + titaVo.getParam("inputMonth") + "01";

		getMonthEndDate(inputDate);

		List<Map<String, String>> listLM084 = null;

		try {
			listLM084 = lM084ServiceImpl.findAll(titaVo);
		} catch (Exception e) {
			StringWriter errors = new StringWriter();
			e.printStackTrace(new PrintWriter(errors));
			this.error("LM084ServiceImpl.findAll error = " + errors.toString());
		}

		if (listLM084 != null && listLM084.size() > 0) {
			lastAging = listLM084.get(0).get("F0");
			thisPageAging = getAgingText(lastAging);
		}

		this.open(titaVo, titaVo.getEntDyI(), titaVo.getKinbr(), txcd, txnm, "", "A4", "P");

		if (listLM084 != null && listLM084.size() > 0) {
			printDetail(listLM084);
		} else {
			this.print(1, 1, "本日無資料");
		}

		// 測試用
		long sno = this.close();

		// 測試用
		this.toPdf(sno);
	}

	private void printDetail(List<Map<String, String>> listLM084) {

		this.print(1, 3, thisPageAging);

		// 小計-應收利息
		BigDecimal interestTotal = BigDecimal.ZERO;

		for (Map<String, String> m : listLM084) {

			String aging = m.get("F0");

			this.info("aging = " + aging);
			this.info("lastAging = " + lastAging);
			this.info("m.get(\"F1\") = " + m.get("F1"));

			if (!aging.equals(lastAging)) {

				// 列印小計
				printTotal(interestTotal);

				lastAging = aging;
				thisPageAging = getAgingText(lastAging);
				interestTotal = BigDecimal.ZERO;
				this.newPage();
				this.print(1, 3, thisPageAging);
			}

			this.print(1, 14, m.get("F1")); // 戶號
			this.print(0, 25, m.get("F2")); // 戶名
			this.print(0, 63, this.showRocDate(m.get("F3"), 1)); // 繳息迄日
			this.print(0, 74, this.showRocDate(m.get("F4"), 1)); // 應繳日

			// 應收利息
			BigDecimal interest = getBigDecimal(m.get("F5"));
			this.print(0, 96, this.formatAmt(interest, 0), "R");

			// 計算小計-應收利息
			interestTotal = interestTotal.add(interest);
		}

		// 列印小計
		printTotal(interestTotal);

	}

	private void printTotal(BigDecimal interestTotal) {
		if ((this.NowRow + 3) > (8 + 38)) {
			this.newPage();
		}
		this.print(1, 1, "　－－－－－　－－－－－　－－－－－－－－－－－－－－－－－－－－　－－－－－　－－－－－　－－－－－－");
		this.print(1, 1, "　　　　　　　小　　　計　　　　　　　　　　　　　　　　　　　　　　　　　　　　金　　額：");
		this.print(0, 96, this.formatAmt(interestTotal, 0), "R");
		this.print(1, 1, "　－－－－－　－－－－－　－－－－－－－－－－－－－－－－－－－－　－－－－－　－－－－－　－－－－－－");
	}

	private String getAgingText(String aging) {
		String agingText = "";
		switch (aging) {
		case "0":
			agingText = "一個月以下";
			break;
		case "1":
			agingText = "一至三個月";
			break;
		case "2":
			agingText = "三至六個月";
			break;
		case "3":
			agingText = "六個月以上";
			break;
		default:
			break;
		}
		return agingText;
	}

	private void getMonthEndDate(String inputDate) throws LogicException {
		this.dDateUtil.init();
		dDateUtil.setDate_1(inputDate);
		dDateUtil.setMons(1); // 往後一個月
		int nextMonthStartDate = dDateUtil.getCalenderDay();
		this.dDateUtil.init();
		dDateUtil.setDate_1(nextMonthStartDate);
		dDateUtil.setDays(-1); // 往前1天
		int thisMonthEndDate = dDateUtil.getCalenderDay();

		while (dDateUtil.isHoliDay()) {
			this.dDateUtil.init();
			dDateUtil.setDate_1(thisMonthEndDate);
			dDateUtil.setDays(-1);
			thisMonthEndDate = dDateUtil.getCalenderDay();
		}
		thisMonthEndBsDate = "" + thisMonthEndDate;
	}

	@Override
	public void printHeader() {

		this.print(-1, 3, "程式ID：" + this.getParentTranCode());
		this.print(-1, 50, "新光人壽保險股份有限公司", "C");
		this.print(-1, 81, "日　　期：" + this.showBcDate(today, 1));

		this.print(-2, 3, "報　表：" + this.getRptCode());
		this.print(-2, 50, txnm, "C");
		this.print(-2, 81, "時　　間：" + this.showTime(nowTime));

		this.print(-3, 81, "頁　　次：　" + this.getNowPage());

		this.print(-4, 50, this.showRocDate(thisMonthEndBsDate, 0), "C");

		this.print(-5, 1, "　帳　　　齡：" + thisPageAging);

		this.print(-6, 1, "　帳　　　齡　戶　　　號　戶　　　名　　　　　　　　　　　　　　　　繳息迄日　　應繳日　　　　應收利息");
		this.print(-7, 1, "　－－－－－　－－－－－　－－－－－－－－－－－－－－－－－－－－　－－－－－　－－－－－　－－－－－－");

		// 明細起始列(自訂亦必須)
		this.setBeginRow(8);

		// 設定明細列數(自訂亦必須)
		this.setMaxRows(38);

	}
}
