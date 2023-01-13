package com.st1.itx.trade.LM;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.db.service.springjpa.cm.LM003ServiceImpl;
import com.st1.itx.util.common.MakeReport;
import com.st1.itx.util.common.data.ReportVo;
import com.st1.itx.util.parse.Parse;

@Component
@Scope("prototype")
public class LM003Report extends MakeReport {

	@Autowired
	LM003ServiceImpl lM003ServiceImpl;

	@Autowired
	Parse parse;

	private enum Columns {

		// 這裡的宣告順序建議和輸出順序一樣
		// 期間
		yearMonth("F0", 8, 0, 18, false),
		// 撥款
		drawdownAmt("F1", 0, 0, 37, false),
		// 利率高轉貸
		highRate("F2", 0, 47, 54, true),
		// 買賣
		trade("F3", 0, 62, 66, true),
		// 自行還款等
		others("F4", 0, 75, 80, true),
		// (結清)小計
		closedRepaySum("F5", 0, 89, 94, true),
		// 部分還款
		partlyRepay("F6", 0, 104, 110, true),
		// 本金攤提
		tenty("F7", 0, 118, 122, true),
		// 轉催收
		turnOvdu("F8", 0, 132, 138, true),
		// (非結清)小計
		unclosedRepaySum("F9", 0, 147, 152, true),
		// 合計
		repayTotal("F10", 0, 164, 171, true),
		// 淨增減
		net("F16", 0, 183, 188, false),
		// 月底餘額
		EOMBalance("F11", 0, 197, 202, false);

		private String keyword = "";
		private int outputXPosR = 0;
		private int outputXPosL = 0;
		private int outputXPosC = 0;
		private BigDecimal sum = BigDecimal.ZERO;
		private Boolean hasRatioOutput = false;

		/**
		 * _keyword 欄位代號 _outputXPosL 靠左位置 _outputXPosC 置中位置 _outputXPosR 靠右位置
		 * _hasRatioOutput 是否為百分比型態
		 * 
		 */
		Columns(String _keyword, int _outputXPosL, int _outputXPosC, int _outputXPosR, Boolean _hasRatioOutput) {
			this.keyword = _keyword;
			this.outputXPosL = _outputXPosL;
			this.outputXPosC = _outputXPosC;
			this.outputXPosR = _outputXPosR;
			this.hasRatioOutput = _hasRatioOutput;
		}

		public static void resetAllSum() {
			for (int i = 0; i < Columns.values().length; i++) {
				Columns.values()[i].sum = BigDecimal.ZERO;
			}
		}

		public String getKeyword() {
			return this.keyword;
		}

		public int getOutputXPosR() {
			return this.outputXPosR;
		}

		public int getOutputXPosL() {
			return this.outputXPosL;
		}

		public BigDecimal getSum() {
			return this.sum;
		}

		public void addToSum(BigDecimal apply) {
			this.sum = this.sum.add(apply);
		}
	}

	private class ColumnOutput {

		public LM003Report lm003Report = null;

		/**
		 * @param tLDVo current tLDVo
		 * @throws LogicException
		 */
		public void dealSingleMonth(Map<String, String> tLDVo) throws LogicException {
			// 單月輸出

			for (int i = 0; i < Columns.values().length; i++) {
				Columns c = Columns.values()[i];
				String v = tLDVo.get(c.getKeyword());

				switch (c) {
				case yearMonth:
					// 年月份輸出
					if (v.length() >= 6) {
						lm003Report.print(0, c.getOutputXPosR(),
								(parse.stringToInteger(v.substring(0, 4)) - 1911) + "年" + v.substring(4, 6) + "月", "R");
					}
					break;
				default:
					// 金額輸出與加總
					lm003Report.print(0, c.getOutputXPosR(), formatAmt(v, 2, 8), "R");
					c.addToSum(parse.stringToBigDecimal(v));
					break;
				}
			}
		}

		/**
		 * @param lastF0 F0 of lastTLDVo
		 * @throws LogicException
		 */
		public void printYearlyTotal(String lastF0) throws LogicException {
			// 年合計輸出
			for (int i = 0; i < Columns.values().length; i++) {
				Columns c = Columns.values()[i];

				switch (c) {
				case yearMonth:
					// 年月份輸出
					if (lastF0.length() >= 6) {
						lm003Report.print(0, c.getOutputXPosL(),
								(parse.stringToInteger(lastF0.substring(0, 4)) - 1911) + "年合計", "L");
					}
					break;
				default:
					if (c.hasRatioOutput) // 目前此表要出年合計的 = 有顯示平均的; 如果未來有修改這裡需改
					{
						// 年合計金額輸出
						lm003Report.print(0, c.getOutputXPosR(), formatAmt(c.getSum(), 2, 8), "R");
						break;
					}
				}
			}
		}

		/**
		 * @param lastF0 F0 of lastTLDVo
		 * @throws LogicException
		 */
		public void printYearlyAverage(String lastF0) throws LogicException {
			// 月平均輸出
			for (int i = 0; i < Columns.values().length; i++) {
				Columns c = Columns.values()[i];
				BigDecimal monthCount = null;

				if (lastF0.length() >= 6) {
					monthCount = parse.stringToBigDecimal(lastF0.substring(4, 6));
				} else {
					monthCount = BigDecimal.ONE;
				}

				switch (c) {
				case yearMonth:
					// 年月份輸出
					if (lastF0.length() >= 6) {
						lm003Report.print(0, c.getOutputXPosL(),
								(parse.stringToInteger(lastF0.substring(0, 4)) - 1911) + "年月平均", "L");
					}
					break;
				default:
					if (c.hasRatioOutput) {
						// 月平均金額輸出
						if (c.sum.compareTo(BigDecimal.ZERO) != 0) {
							lm003Report.print(0, c.getOutputXPosR(),
									formatAmt(c.getSum().divide(monthCount, 2, RoundingMode.HALF_UP), 2, 8), "R");
						} else {
							lm003Report.print(0, c.getOutputXPosR(), formatAmt(BigDecimal.ZERO, 2), "R");
						}
					}
					break;
				}
			}
		}

		/**
		 * @param finalTLDVo the last tLDVo (latest month in this output)
		 */
		public void printRepayRatios(Map<String, String> finalTLDVo) {
			// 分母與分子
			BigDecimal divisor = BigDecimal.ZERO;
			BigDecimal dividend = BigDecimal.ZERO;

			// 取分母
			// 當月還款分布, 所以是取TLDVo中的數字
			try {
				divisor = new BigDecimal(finalTLDVo.get(Columns.repayTotal.keyword)); // hard coded...
			} catch (Exception e) {
				lm003Report.error("LM003Report.printRepayRatios() - divisor");
				lm003Report.error("received weird " + Columns.repayTotal.getKeyword() + ": "
						+ finalTLDVo.get(Columns.repayTotal.getKeyword()));
			}

			lm003Report.info("lm003 Report doing printRepayRatios");
			lm003Report.info("divisor: " + divisor.toString());

			for (int i = 0; i < Columns.values().length; i++) {
				// 只有設定為hasRatioOutput的欄位需要輸出
				if (Columns.values()[i].hasRatioOutput) {
					// 取分子

					try {
						dividend = parse.stringToBigDecimal(finalTLDVo.get(Columns.values()[i].getKeyword()));
					} catch (Exception e) {
						lm003Report.error("LM003Report.printRepayRatios() - dividend");
						lm003Report.error("Received weird " + Columns.values()[i].getKeyword() + ": "
								+ finalTLDVo.get(Columns.values()[i].getKeyword()));
					}

					lm003Report.info(Columns.values()[i].toString());
					lm003Report.info("dividend: " + dividend.toString());

					if (dividend.compareTo(BigDecimal.ZERO) > 0 && divisor.compareTo(BigDecimal.ZERO) > 0) {
						lm003Report.print(0, Columns.values()[i].outputXPosR,
								dividend.divide(divisor, 5, RoundingMode.HALF_UP).multiply(getBigDecimal(100))
										.setScale(2, RoundingMode.HALF_UP) + "%",
								"R");
					} else {
						lm003Report.print(0, Columns.values()[i].outputXPosC, "---", "C");
					}
				}
			}
		}
	}

	ColumnOutput columnOutput = new ColumnOutput();

	@Override
	public void printHeader() {

//		int leftPos = 2;
		int centerPos = 55;
		int rightPos = 180;

		this.setFontSize(14);
		this.print(-2, centerPos,
				titaVo.get("inputYearEnd") + "年" + titaVo.get("inputMonthEnd") + "月個人房貸戶 - 撥款/還款金額比較月報表", "C");

		this.setFontSize(7);

		this.print(-5, rightPos, "機密等級：密");

		this.print(-6, rightPos, "單位：億元");

		this.setBeginRow(7);
		this.setMaxRows(100);
	}

	public Boolean exec(TitaVo titaVo) throws LogicException {

		columnOutput.lm003Report = this;

		this.setCharSpaces(0);

		/*
		 * 抓SQL資料
		 */
		List<Map<String, String>> listLM003 = null;

		try {
			listLM003 = lM003ServiceImpl.findAll(titaVo);
		} catch (Exception e) {
			this.error("lM003ServiceImpl.findAll error = " + e.getMessage());
		}

		String txcd = titaVo.getTxcd();
		String reportName = titaVo.get("inputYearEnd") + "年" + titaVo.get("inputMonthEnd") + "月個人房貸戶－撥款／還款金額比較月報表";

		ReportVo reportVo = ReportVo.builder().setRptDate(titaVo.getEntDyI()).setBrno(titaVo.getKinbr())
				.setRptCode(txcd).setRptItem(reportName).setSecurity("").setRptSize("A4").setPageOrientation("L")
				.build();

		this.open(titaVo, reportVo);

		// 42-12=30
		int basePos = 3;
		print(2, basePos,
				"┌────────┬───────┬───────────────────────────────────────────────────────────────────┬───────┬──────┐");

		print(1, basePos,
				"│　　　　　　　　│　　　　　　　│　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　│　　　　　　　│　　　　　　│");
		print(0, basePos + 100, "還款(b)");
		print(1, basePos,
				"│　　　　　　　　│　　　　　　　├────────────────────────────┬────────────────────────────┬─────────┤　　　　　　　│　　　　　　│");
		print(0, basePos + 176, "淨增減");
		print(0, basePos + 192, "月底");
		print(1, basePos,
				"│　　　　　　　　│　　　　　　　│　　　　　　　　　　　　　　　　　　　　　　　　　　　　│　　　　　　　　　　　　　　　　　　　　　　　　　　　　│　　　　　　　　　│　　　　　　　│　　　　　　│");
		print(0, basePos + 8, "期間");
		print(0, basePos + 24, "撥款(a) ");
		print(0, basePos + 63, "結清");
		print(0, basePos + 118, "非結清");
		print(1, basePos,
				"│　　　　　　　　│　　　　　　　├────────┬─────┬──────┬──────┼───────┬─────┬───────┬──────┤　　　　　　　　　│　　　　　　　│　　　　　　│");
		print(0, basePos + 158, "合計");
		print(0, basePos + 192, "餘額");
		print(1, basePos,
				"│　　　　　　　　│　　　　　　　│　　　　　　　　│　　　　　│　　　　　　│　　　　　　│　　　　　　　│　　　　　│　　　　　　　│　　　　　　│　　　　　　　　　│　　　　　　　│　　　　　　│");
		print(0, basePos + 44, "利率高轉貸", "C");
		print(0, basePos + 59, "買賣", "C");
		print(0, basePos + 72, "自行還款等", "C");
		print(0, basePos + 86, "小計", "C");
		print(0, basePos + 101, "部份還款", "C");
		print(0, basePos + 115, "本金攤提", "C");
		print(0, basePos + 129, "轉催收", "C");
		print(0, basePos + 142, "小計");
		print(0, basePos + 161, "（ｃ＝ａ－ｂ）", "C");

		Map<String, String> lastTLDVo = null;

		if (listLM003 != null && listLM003.size() != 0) {

			// 每月輸出
			// 偵測到年份更換時 先出合計與月平均
			// 重置所有累計器

			// 初始化
			Columns.resetAllSum();

			for (Map<String, String> tLDVo : listLM003) {
				if (lastTLDVo != null) {
					// 上一筆資料年份與這一筆資料年份不同時
					// 出上一筆資料的年的合計與月平均

					if (!lastTLDVo.get("F0").substring(0, 4).equals(tLDVo.get("F0").substring(0, 4))) {
						print(1, basePos,
								"├────────┼───────┼────────┼─────┼──────┼──────┼───────┼─────┼───────┼──────┼─────────┼───────┼──────┤");
						print(1, basePos,
								"│　　　　　　　　│　　　　　　　│　　　　　　　　│　　　　　│　　　　　　│　　　　　　│　　　　　　　│　　　　　│　　　　　　　│　　　　　　│　　　　　　　　　│　　　　　　　│　　　　　　│");

						columnOutput.printYearlyTotal(lastTLDVo.get("F0"));

						print(1, basePos,
								"├────────┼───────┼────────┼─────┼──────┼──────┼───────┼─────┼───────┼──────┼─────────┼───────┼──────┤");
						print(1, basePos,
								"│　　　　　　　　│　　　　　　　│　　　　　　　　│　　　　　│　　　　　　│　　　　　　│　　　　　　　│　　　　　│　　　　　　　│　　　　　　│　　　　　　　　　│　　　　　　　│　　　　　　│");

						columnOutput.printYearlyAverage(lastTLDVo.get("F0"));

						Columns.resetAllSum(); // 每次發生年份變動就重置所有累計
					}
				}

				print(1, basePos,
						"├────────┼───────┼────────┼─────┼──────┼──────┼───────┼─────┼───────┼──────┼─────────┼───────┼──────┤");
				print(1, basePos,
						"│　　　　　　　　│　　　　　　　│　　　　　　　　│　　　　　│　　　　　　│　　　　　　│　　　　　　　│　　　　　│　　　　　　　│　　　　　　│　　　　　　　　　│　　　　　　　│　　　　　　│");

				columnOutput.dealSingleMonth(tLDVo);

				lastTLDVo = tLDVo;

			}

			// 出最後一年的年月

			print(1, basePos,
					"├────────┼───────┼────────┼─────┼──────┼──────┼───────┼─────┼───────┼──────┼─────────┼───────┼──────┤");
			print(1, basePos,
					"│　　　　　　　　│　　　　　　　│　　　　　　　　│　　　　　│　　　　　　│　　　　　　│　　　　　　　│　　　　　│　　　　　　　│　　　　　　│　　　　　　　　　│　　　　　　　│　　　　　　│");

			columnOutput.printYearlyTotal(lastTLDVo.get("F0"));

			print(1, basePos,
					"├────────┼───────┼────────┼─────┼──────┼──────┼───────┼─────┼───────┼──────┼─────────┼───────┼──────┤");
			print(1, basePos,
					"│　　　　　　　　│　　　　　　　│　　　　　　　　│　　　　　│　　　　　　│　　　　　　│　　　　　　　│　　　　　│　　　　　　　│　　　　　　│　　　　　　　　　│　　　　　　　│　　　　　　│");

			columnOutput.printYearlyAverage(lastTLDVo.get("F0"));

		} else if (listLM003 == null || listLM003.size() == 0) {
			print(0, 50, "本日無資料!!", "C");
		}

		print(1, basePos,
				"└────────┴───────┴────────┴─────┴──────┴──────┴───────┴─────┴───────┴──────┴─────────┴───────┴──────┘");

		print(1, basePos,
				"┌────────────────┬────────┬─────┬──────┬──────┬───────┬─────┬───────┬──────┬─────────┬──────────────┐");
		print(1, basePos,
				"│　　　　當月還款分布％　　　　　│　　　　　　　　│　　　　　│　　　　　　│　　　　　　│　　　　　　　│　　　　　│　　　　　　　│　　　　　　│　　　　　　　　　│　　　　　　　　　　　　　　│");
		// 無資料時這個會是null
		// 有資料時會是最後一個tLDVo
		if (lastTLDVo != null) {
			this.info("LM003Report do printRepayRatios()");
			StringBuilder sb = new StringBuilder("lastTLDVo content: ");

			for (int i = 0; i < Columns.values().length; i++) {
				sb.append("F" + i + " :" + lastTLDVo.get(Columns.values()[i].getKeyword()));
			}

			columnOutput.printRepayRatios(lastTLDVo);
		}

		print(1, basePos,
				"└────────────────┴────────┴─────┴──────┴──────┴───────┴─────┴───────┴──────┴─────────┴──────────────┘");

		if (lastTLDVo != null) {

			int year = parse.stringToInteger(lastTLDVo.get("F0").substring(0, 4)) - 1911;
			int month = parse.stringToInteger(lastTLDVo.get("F0").substring(4, 6));
			String EOMBalance = formatAmt(lastTLDVo.get("F11"), 2, 8);
			String EOMBalanceEnt = formatAmt(lastTLDVo.get("F12"), 2, 8);
			String EOMBalanceNat = formatAmt(lastTLDVo.get("F13"), 2, 8);
			String Internal = formatAmt(lastTLDVo.get("F15"), 2, 8);
			String RepayTotal = formatAmt(Columns.repayTotal.getSum(), 2, 8);
			String TurnOvdu = formatAmt(Columns.turnOvdu.getSum(), 2, 8);
			String NatEnt = formatAmt(lastTLDVo.get("F14"), 2, 8);

			String ActualRepay = formatAmt(Columns.repayTotal.getSum()
					.subtract(parse.stringToBigDecimal(lastTLDVo.get("F15"))).subtract(Columns.turnOvdu.getSum())
					.subtract(parse.stringToBigDecimal(lastTLDVo.get("F14"))), 2, 8);

			print(1, 1, "");
			print(1, basePos, " ●" + year + "年" + month + "月底貸款總餘額：" + EOMBalance + "億元 ●企金：" + EOMBalanceEnt
					+ "億元 ●房貸：" + EOMBalanceNat + "億元");
			print(1, basePos, " ●依報表：LN6361編制：撥款金額含催收回復，還款金額含轉催收。");
			print(1, basePos, " ●自行還款含內部代償、借新還舊、大額還款（1月~" + month + "月累積數" + Internal + "億）。");

			print(1, basePos, " ●" + month + "月實際還款數：" + RepayTotal + "（帳載）-" + Internal + "（內部轉帳）-" + TurnOvdu
					+ "（轉催收）-" + NatEnt + "（企金件以自然人申貸還款）＝" + ActualRepay + "億");
			print(1, basePos, " ●撥款金額包含企金件以自然人申貸撥款" + NatEnt + "億");
		}

		this.close();

		return true;
	}

//	public String drawCustomized(String... vars) {
//
//		String text = "";
//		String s = "";
//		int c = 0;
//
//		// 兩個參數為一組，偶數數量參數
//		if (vars.length % 2 == 0) {
//			for (int i = 0; i < vars.length; i += 2) {
//
//				s = "".equals(vars[i]) ? "　" : vars[i];
//				c = Integer.valueOf(vars[i + 1]);
//
//				for (int j = 1; j <= c; j++) {
//					text += s;
//				}
//
//			}
//		}
//
//		return text;
//	}

}
