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

@Component
@Scope("prototype")
public class LM003Report extends MakeReport {

	@Autowired
	LM003ServiceImpl lM003ServiceImpl;

	// 四捨五入
	RoundingMode roundingModeLM003 = RoundingMode.HALF_UP;

	private enum Columns {

		// 這裡的宣告順序建議和輸出順序一樣

		// 期間 44 L ; 57 R

		// 撥款金額 73 R

		// 結清:利率高轉貸 91 R
		// 結清:買賣 107 R
		// 結清:自行還款等 119 R
		// 結清小計 133 R

		// 非結清:部分還款 149 R
		// 非結清:本金攤提 162 R
		// 非結清:轉催收 175 R
		// 非結清小計: 191 R

		// 合計: 209 R
		// 淨增減: 224 R
		// 餘額: 240 R

		yearMonth("F0", 60, 44), drawdownAmt("F1", 76), highRate("F2", 94, true, 86), trade("F3", 106, true, 101),
		others("F4", 120, true, 114), closedRepaySum("F5", 134, true, 128), partlyRepay("F6", 150, true, 143), tenty("F7", 162, true, 157),
		turnOvdu("F8", 178, true, 171), unclosedRepaySum("F9", 192, true, 186), repayTotal("F10", 212, true, 203), net("F16", 228),
		EOMBalance("F11", 242);

		private String keyword = "";
		private int outputXPosR = 0;
		private int outputXPosL = 0;
		private int outputXPosC = 0;
		private BigDecimal sum = BigDecimal.ZERO;
		private Boolean hasRatioOutput = false;
		
		// 邊做表邊弄constructors, 顯亂
		// 之後需更新時可整合成統一格式

		Columns(String _keyword, int _outputXPosR) {
			this.keyword = _keyword;
			this.outputXPosR = _outputXPosR;
		}

		Columns(String _keyword, int _outputXPosR, Boolean _hasRatioOutput, int _outputXPosC) {
			this.keyword = _keyword;
			this.outputXPosR = _outputXPosR;
			this.outputXPosC = _outputXPosC;
			this.hasRatioOutput = _hasRatioOutput;
		}

		Columns(String _keyword, int _outputXPosR, int _outputXPosL) {
			this.keyword = _keyword;
			this.outputXPosR = _outputXPosR;
			this.outputXPosL = _outputXPosL;
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
		
		public void setSum(BigDecimal result) {
			this.sum = result;
		}
	}

	private class ColumnOutput {

		public LM003Report lm003Report = null;

		/**
		 * 
		 * @param tLDVo current tLDVo
		 */
		public void dealSingleMonth(Map<String, String> tLDVo) {
			// 單月輸出

			for (int i = 0; i < Columns.values().length; i++) {
				Columns c = Columns.values()[i];
				String v = tLDVo.get(c.getKeyword());

				switch (c) {
				case yearMonth:
					// 年月份輸出
					if (v.length() >= 6) {
						lm003Report.print(0, c.getOutputXPosR(),
								(Integer.valueOf(v.substring(0, 4)) - 1911) + "年" + v.substring(4, 6) + "月", "R");
					}
					break;
				default:
					// 金額輸出與加總
					lm003Report.print(0, c.getOutputXPosR(), formatAmt(getBillionAmt(v), 2), "R");
					c.setSum(c.getSum().add(new BigDecimal(v)));
					break;
				}
			}
		}

		/**
		 * 
		 * @param lastF0 F0 lof lastTLDVo
		 */
		public void printYearlyTotal(String lastF0) {
			// 年合計輸出
			for (int i = 0; i < Columns.values().length; i++) {
				Columns c = Columns.values()[i];

				switch (c) {
				case yearMonth:
					// 年月份輸出
					if (lastF0.length() >= 6) {
						lm003Report.print(0, c.getOutputXPosL(),
								(Integer.valueOf(lastF0.substring(0, 4)) - 1911) + "年合計", "L");
					}
					break;
				default:
					if (c.hasRatioOutput)
					{
						// 年合計金額輸出
						lm003Report.print(0, c.getOutputXPosR(), formatAmt(getBillionAmt(c.getSum()), 2), "R");
						break;
					}
				}
			}
		}

		/**
		 * 
		 * @param lastF0 F0 of lastTLDVo
		 */
		public void printYearlyAverage(String lastF0) {
			// 月平均輸出
			for (int i = 0; i < Columns.values().length; i++) {
				Columns c = Columns.values()[i];
				BigDecimal monthCount = null;

				if (lastF0.length() >= 6) {
					monthCount = new BigDecimal(lastF0.substring(4, 6));
				} else {
					monthCount = new BigDecimal(1);
				}

				switch (c) {
				case yearMonth:
					// 年月份輸出
					if (lastF0.length() >= 6) {
						lm003Report.print(0, c.getOutputXPosL(),
								(Integer.valueOf(lastF0.substring(0, 4)) - 1911) + "年月平均", "L");
					}
					break;
				default:
					if (c.hasRatioOutput)
					{
						// 月平均金額輸出
						if (c.sum.compareTo(BigDecimal.ZERO) != 0) {
							lm003Report.print(0, c.getOutputXPosR(),
									formatAmt(getBillionAmt(c.getSum().divide(monthCount, 2, roundingModeLM003)), 2),
									"R");
						} else {
							lm003Report.print(0, c.getOutputXPosR(), formatAmt(BigDecimal.ZERO, 2), "R");
						}
					}
					break;
				}
			}
		}

		/**
		 * 
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
						dividend = new BigDecimal(finalTLDVo.get(Columns.values()[i].getKeyword()));
					} catch (Exception e) {
						lm003Report.error("LM003Report.printRepayRatios() - dividend");
						lm003Report.error("Received weird " + Columns.values()[i].getKeyword() + ": "
								+ finalTLDVo.get(Columns.values()[i].getKeyword()));
					}

					lm003Report.info(Columns.values()[i].toString());
					lm003Report.info("dividend: " + dividend.toString());

					if (dividend.compareTo(BigDecimal.ZERO) > 0 && divisor.compareTo(BigDecimal.ZERO) > 0) {
						lm003Report.print(0, Columns.values()[i].outputXPosC, dividend.divide(divisor, 5, roundingModeLM003).multiply(new BigDecimal(100)).setScale(2, roundingModeLM003) + "%", "C");
					} else
					{
						lm003Report.print(0, Columns.values()[i].outputXPosC, "---", "C");
					}
				}
			}
		}
	}

	ColumnOutput columnOutput = new ColumnOutput();

	@Override
	public void printHeader() {

		this.setFontSize(16);
		this.print(-2, 52, titaVo.get("inputYearEnd") + "年" + titaVo.get("inputMonthEnd") + "月個人房貸戶 - 撥款/還款金額比較月報表",
				"C");
		this.setFontSize(7);
		this.print(-4, 243, "機密等級：密", "R");
		this.setFontSize(8);
		this.print(-5, 166, "單位：億元", "R");
		this.setFontSize(6);

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

		this.open(titaVo, titaVo.getEntDyI(), titaVo.getKinbr(), "LM003",
				titaVo.get("inputYearEnd") + "年" + titaVo.get("inputMonthEnd") + "月個人房貸戶 - 撥款／還款金額比較月報表", "", "A4",
				"L");

		print(1, 1, "");
		print(1, 42,
				"╔════════╦═══════╦═══════════════════════════════════════════════════════════════════╦═══════╦══════╗");
		print(1, 42,
				"║　　　　 　　 　║　　　　　　　║　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　║　　　　　　　║　　　　　　║");

		print(0, 136, "還款(b)");
		print(1, 42,
				"║　　　　　　　  ║　　　　　　　╠════════════════════════════╦════════════════════════════╦═════════╣　　　　　　  ║　　　　　　║");
		print(0, 218, "淨增減");
		print(0, 234, "月底");
		print(1, 42,
				"║　　　　　　　  ║ 　　　　　 　║　　　　　　　　    　　　　　　　　　　　　　　　　　  ║　　　　　　　　　　　　　　　　　　　　　　　　　　    ║　　 　　　　     ║　　　　　　　║　　　　　　║");
		print(0, 50, "期間");
		print(0, 66, "撥款(a) ");
		print(0, 101, "結清");
		print(0, 160, "非結清");
		print(1, 42,
				"║　　　　　　　　║　　　　　　　╠════════╦═════╦══════╦══════╬═══════╦═════╦═══════╦══════╣　　　　　　　　　║　　　　　　　║　　　　　　║");
		print(0, 202, "合計");
		print(1, 42,
				"║　　　　　　　　║　　　　　　　║　　　　　　　　║　　　　　║　　　　　　║　　　　　　║　　　　　　　║　　　　　║　　　　　　　║　　　　　　║　　　　　　　　　║　　　　　　　║　　　　　　║");

		print(0, 81, "利率高轉貸");
		print(0, 99, "買賣");
		print(0, 109, "自行還款等");
		print(0, 126, "小計");

		print(0, 140, "部份還款");
		print(0, 153, "本金攤提");

		print(0, 168, "轉催收");
		print(0, 184, "小計");

		print(0, 221, "（ｃ＝ａ－ｂ）", "C");

		print(0, 234, "餘額");

		// 期間 44 L ; 57 R

		// 撥款金額 73 R

		// 結清:利率高轉貸 91 R
		// 結清:買賣 107 R
		// 結清:自行還款等 119 R
		// 結清小計 133 R

		// 非結清:部分還款 149 R
		// 非結清:本金攤提 162 R
		// 非結清:轉催收 175 R
		// 非結清小計: 191 R

		// 合計: 209 R
		// 淨增減: 224 R
		// 餘額: 240 R

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
						print(1, 42,
								"╟────────╫───────╫────────╫─────╫──────╫──────╫───────╫─────╫───────╫──────╫─────────╫───────╫──────╢");
						print(1, 42,
								"║　　　　　　　　║　　　　　　　║　　　　　　　　║　　　　　║　　　　　　║　　　　　　║　　　　　　　║　　　　　║　　　　　　　║　　　　　　║　　　　　　　　　║　　　　　　　║　　　　　　║");

						columnOutput.printYearlyTotal(lastTLDVo.get("F0"));

						print(1, 42,
								"╟────────╫───────╫────────╫─────╫──────╫──────╫───────╫─────╫───────╫──────╫─────────╫───────╫──────╢");
						print(1, 42,
								"║　　　　　　　　║　　　　　　　║　　　　　　　　║　　　　　║　　　　　　║　　　　　　║　　　　　　　║　　　　　║　　　　　　　║　　　　　　║　　　　　　　　　║　　　　　　　║　　　　　　║");

						columnOutput.printYearlyAverage(lastTLDVo.get("F0"));

						Columns.resetAllSum(); // 每次發生年份變動就重置所有累計
					}
				}

				print(1, 42,
						"╟────────╫───────╫────────╫─────╫──────╫──────╫───────╫─────╫───────╫──────╫─────────╫───────╫──────╢");
				print(1, 42,
						"║　　　　　　　　║　　　　　　　║　　　　　　　　║　　　　　║　　　　　　║　　　　　　║　　　　　　　║　　　　　║　　　　　　　║　　　　　　║　　　　　　　　　║　　　　　　　║　　　　　　║");

				columnOutput.dealSingleMonth(tLDVo);

				lastTLDVo = tLDVo;

			}

			// 出最後一年的年月

			print(1, 42,
					"╟────────╫───────╫────────╫─────╫──────╫──────╫───────╫─────╫───────╫──────╫─────────╫───────╫──────╢");
			print(1, 42,
					"║　　　　　　　　║　　　　　　　║　　　　　　　　║　　　　　║　　　　　　║　　　　　　║　　　　　　　║　　　　　║　　　　　　　║　　　　　　║　　　　　　　　　║　　　　　　　║　　　　　　║");

			columnOutput.printYearlyTotal(lastTLDVo.get("F0"));

			print(1, 42,
					"╟────────╫───────╫────────╫─────╫──────╫──────╫───────╫─────╫───────╫──────╫─────────╫───────╫──────╢");
			print(1, 42,
					"║　　　　　　　　║　　　　　　　║　　　　　　　　║　　　　　║　　　　　　║　　　　　　║　　　　　　　║　　　　　║　　　　　　　║　　　　　　║　　　　　　　　　║　　　　　　　║　　　　　　║");

			columnOutput.printYearlyAverage(lastTLDVo.get("F0"));

		} else if (listLM003 == null || listLM003.size() == 0) {
			print(0, 50, "本日無資料!!", "C");
		}

		print(1, 42,
				"╚════════╩═══════╩════════╩═════╩══════╩══════╩═══════╩═════╩═══════╩══════╩═════════╩═══════╩══════╝");

		print(1, 42,
				"╔════════════════╦════════╦═════╦══════╦══════╦═══════╦═════╦═══════╦══════╦═════════╦═══════╦══════╗");
		print(1, 42,
				"║　　　　當月還款分布%　　　 　　║　　　　　　　　║　　　　　║　　　　　　║　　　　　　║　　　　　　　║　　　　　║　　　　　　　║　　　　　　║　　　　　　　　　║　　　　　　　║　　　　　　║");
		
		// 無資料時這個會是null
		// 有資料時會是最後一個tLDVo
		if (lastTLDVo != null) {
			this.info("LM003Report do printRepayRatios()");
			StringBuilder sb = new StringBuilder("lastTLDVo content: ");
			
			for (int i = 0; i < Columns.values().length; i++)
			{
				sb.append("F"+i+" :" + lastTLDVo.get(Columns.values()[i].getKeyword()));
			}

			columnOutput.printRepayRatios(lastTLDVo);
		}

		print(1, 42,
				"╚════════════════╩════════╩═════╩══════╩══════╩═══════╩═════╩═══════╩══════╩═════════╩═══════╩══════╝");
		
		if (lastTLDVo != null) {
			
		print(1, 1, "");
		print(1, 42, " ●" + (Integer.valueOf(lastTLDVo.get("F0").substring(0,4))-1911) + "年" + lastTLDVo.get("F0").substring(4,6) + "月底貸款總餘額："
				+ formatAmt(getBillionAmt(lastTLDVo.get("F11")), 2) + "億元 ●企金：" + formatAmt(getBillionAmt(lastTLDVo.get("F12")), 2) + "億元 ●房貸：" + formatAmt(getBillionAmt(lastTLDVo.get("F13")), 2) + "億元");
		print(1, 42, " ●依報表：LN6361編制：撥款金額含催收回復，還款金額含轉催收。");
		print(1, 42, " ●自行還款含內部代償、借新還舊、大額還款（1月~" + Integer.valueOf(lastTLDVo.get("F0").substring(4,6)) + "月累積數"+formatAmt(getBillionAmt(lastTLDVo.get("F15")), 2)+"億）。");

		print(1, 42, " ●"+ Integer.valueOf(lastTLDVo.get("F0").substring(4,6)) + "月實際還款數：" + formatAmt(getBillionAmt(Columns.repayTotal.getSum()), 2) + "（帳載）-" + formatAmt(getBillionAmt(lastTLDVo.get("F15")), 2) + "（內部轉帳）-" + formatAmt(getBillionAmt(Columns.turnOvdu.getSum()), 2) + "（轉催收）-"
				+ formatAmt(getBillionAmt(lastTLDVo.get("F14")), 2) + "（企金件以自然人申貸還款）＝" + formatAmt(getBillionAmt(Columns.repayTotal.getSum().subtract(new BigDecimal(lastTLDVo.get("F15")).subtract(Columns.turnOvdu.getSum()).subtract(new BigDecimal(lastTLDVo.get("F14"))))), 2) + "億");
		print(1, 42, " ●撥款金額包含企金件以自然人申貸撥款" + formatAmt(getBillionAmt(new BigDecimal(lastTLDVo.get("F14"))), 2) + "億");		
		}
		
		long sno = this.close();
		this.toPdf(sno);

		return true;
	}

	/**
	 * 取到億元,四捨五入到小數點後第二位
	 * 
	 * @param inputBigDeicmal 從DB撈到的數值
	 * @return 處理後的數值
	 */
	private BigDecimal getBillionAmt(BigDecimal inputBigDecimal) {
		return getBillionAmt(inputBigDecimal.toString());
	}

	/**
	 * 取到億元,四捨五入到小數點後第二位
	 * 
	 * @param inputAmt 從DB撈到的數值
	 * @return 處理後的數值
	 */
	private BigDecimal getBillionAmt(String inputAmt) {
		BigDecimal result = BigDecimal.ZERO;
		BigDecimal billion = new BigDecimal("100000000");

		if (inputAmt == null || inputAmt.isEmpty()) {
			return result;
		}

		try {
			result = new BigDecimal(inputAmt).divide(billion, 2, roundingModeLM003);
		} catch (Exception e)
		{
			this.error("LM003Report.getBillionAmt(): Tried to turn string " + inputAmt + " into BigDecimal and failed miserably.");
			return result;
		}

		return result;
	}
}
