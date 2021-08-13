package com.st1.itx.trade.LM;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.db.service.springjpa.cm.LM012ServiceImpl;
import com.st1.itx.util.common.MakeReport;
import com.st1.itx.util.date.DateUtil;
import com.st1.itx.util.format.FormatUtil;

@Component
@Scope("prototype")
public class LM012Report extends MakeReport {
	// private static final Logger logger = LoggerFactory.getLogger(LM012Report.class);

	@Autowired
	LM012ServiceImpl lM012ServiceImpl;

	@Autowired
	DateUtil dateUtil;
	
	String dataDate = "";
	@Override
	public void printHeader() {
		
		this.print(-1, 123, "機密等級：密");
		this.print(-2, 1, "　 程式ID：" + this.getParentTranCode());
		this.print(-2, 68, "新光人壽保險股份有限公司", "C");
		this.print(-2, 123, "日　期：" + this.showBcDate(dateUtil.getNowStringBc(), 1));
		this.print(-3, 1, "　 報  表：" + this.getRptCode());
		this.print(-3, 68, "放款餘額利率分佈表", "C");
		this.print(-3, 123, "時　間：" + dateUtil.getNowStringTime().substring(0, 2) + ":"
				+ dateUtil.getNowStringTime().substring(2, 4) + ":" + dateUtil.getNowStringTime().substring(4, 6));
		this.print(-4, 123, "頁　數：" + this.getNowPage());
		this.print(-5, 68, getshowRocDate(Integer.parseInt(dataDate)), "C");
		this.print(-5, 123, "單　位：百萬元");
		this.print(-7, 1, "┌─────────┬────────────┬────────────┬────────────┬────────────┬────────────┐");
		this.print(-8, 1, "│　　　　　　　　　│　　　　　　　　　　　　│　　　　　　　　　　　　│　　　　　　　　　　　　│　　　　　　　　　　　　│　　　　　　　　　　　　│");
		this.print(-8, 27, "短期擔保放款");
		this.print(-8, 50, "中期擔保放款");
		this.print(-8, 74, "長期擔保放款");
		this.print(-8, 98, "30　年房屋貸款");
		this.print(-8, 122, "合　　　　計");
		this.print(-9, 1, "│　　　　　　　　　├──────┬─────┼──────┬─────┼──────┬─────┼──────┬─────┼──────┬─────┤");
		this.print(-9, 7, "利　　率％");
		this.print(-10, 1, "│　　　　　　　　　│　　　　　　│　　　　　│　　　　　　│　　　　　│　　　　　　│　　　　　│　　　　　　│　　　　　│　　　　　　│　　　　　│");
		this.print(-11, 1, "│　　　　　　　　　│　　　　　　│　　　　　│　　　　　　│　　　　　│　　　　　　│　　　　　│　　　　　　│　　　　　│　　　　　　│　　　　　│");
		this.print(-12, 1, "├─────────┼──────┼─────┼──────┼─────┼──────┼─────┼──────┼─────┼──────┼─────┤");
		int temp = 22;
		for (int i = 0; i < 5; i++) {
			if (i != 4) {
				this.print(-10, 15 + temp, "對科目");
				this.print(-11, -1 + temp, "　餘　　額　");
				this.print(-11, 15 + temp, "比重％");
				temp = temp + 24;
			} else {
				this.print(-10, 15 + temp, "對總計");
				this.print(-11, -1 + temp, "　餘　　額　");
				this.print(-11, 15 + temp, "比重％");
				temp = 12;
			}
		}
		this.setBeginRow(13);

		this.setMaxRows(30);
	}
	public void printFooter() {
		this.print(0, 1, "└─────────┴──────┴─────┴──────┴─────┴──────┴─────┴──────┴─────┴──────┴─────┘");
	}

	public boolean exec(TitaVo titaVo) throws LogicException {

		// TODO:
		// 前端增加輸入條件,應根據該輸入參數調整報表內容
		// 利率下限上限: #RateMinimum – #RateMaximum
		// 利率區間 : #RateRange
		// 年月 : #DataYear / #DataMonth

		/**
		 * 利率下限
		 */
		String sRateMinimum = titaVo.getParam("RateMinimum");
		
		/**
		 * 利率上限
		 */
		String sRateMaximum = titaVo.getParam("RateMaximum");
		
		/**
		 * 利率區間
		 */
		String sRateRange = titaVo.getParam("RateRange");
		String sDataYear = titaVo.getParam("DataYear");
		String sDataMonth = titaVo.getParam("DataMonth");
		dataDate = String.valueOf(Integer.parseInt(sDataYear) + 1911) + sDataMonth + getLastDayOfMonth(sDataYear, sDataMonth);
		BigDecimal bRateMinimum = sRateMinimum == null ? BigDecimal.ZERO : new BigDecimal(sRateMinimum);
		BigDecimal bRateMaximum = sRateMaximum == null ? BigDecimal.ZERO : new BigDecimal(sRateMaximum);
		BigDecimal bRateRange = sRateRange == null ? BigDecimal.ZERO : new BigDecimal(sRateRange);
		
		this.open(titaVo, titaVo.getEntDyI(), titaVo.getKinbr(), "LM012", "放款利率分佈表", "", "A4", "L");

		List<Map<String, String>> listLM012 = null;

		try {
			listLM012 = lM012ServiceImpl.findAll(titaVo);
		} catch (Exception e) {
			StringWriter errors = new StringWriter();
			e.printStackTrace(new PrintWriter(errors));
			this.error("lM012ServiceImpl.findAll error = " + errors.toString());
		}
		int size = 2;
		if(bRateRange.compareTo(BigDecimal.ZERO) == 1) {
			size = bRateMaximum.subtract(bRateMinimum).divide(bRateRange, 0, 4).intValue() + 2;
			this.info("size = " + size);
		}
		BigDecimal rate[] = new BigDecimal[size - 1];
		for (int i = 0; i < size - 1; i++) {
			BigDecimal j = bRateMinimum;
			rate[i] = j.add(new BigDecimal(String.valueOf(i)).multiply(bRateRange));
		}
		BigDecimal value[][] = new BigDecimal[size][5];
		BigDecimal total[] = new BigDecimal[5];
		for (int i = 0; i < size; i++) {
			for (int j = 0; j < 5; j++) {
				value[i][j] = BigDecimal.ZERO;
			}
		}
		for (int i = 0; i < 5; i++) {
			total[i] = BigDecimal.ZERO;
		}
		for (Map<String, String> LM12Vo : listLM012) {
			int i = 0;// 先算總金額
			if (Float.parseFloat(LM12Vo.get("F0")) == -1) {
				i = 0;
			} else if (Float.parseFloat(LM12Vo.get("F0")) == 999) {
				i = size - 1;
			} else {
				i = (int) ((Double.parseDouble(LM12Vo.get("F0")) - 1) / bRateRange.floatValue() + 1);
				this.info("i = " + i);
			}
			BigDecimal totalLoanBalance = BigDecimal.ZERO;
			for (int k = 1; k < 5; k++) {
				String ad = "F" + String.valueOf(k);
//				value[i][k-1] = value[i][k-1].add(new BigDecimal(LM12Vo.get(ad)));
				value[i][k - 1] = value[i][k - 1]
						.add(new BigDecimal(LM12Vo.get(ad)).divide(new BigDecimal("1000000"), 0, 4));// Last Version
//				total[k-1] = total[k-1].add(new BigDecimal(LM12Vo.get(ad)));
				total[k - 1] = total[k - 1].add(new BigDecimal(LM12Vo.get(ad)).divide(new BigDecimal("1000000"), 0, 4));// Last
																														// Version
				totalLoanBalance = totalLoanBalance.add(value[i][k - 1]);
			}

			value[i][4] = value[i][4].add(totalLoanBalance);
			totalLoanBalance = BigDecimal.ZERO;
		}
		DecimalFormat df3 = new DecimalFormat("0.0000");
		for (int i = 0; i < 4; i++) {
			if (total[i] != null || total[i] != BigDecimal.ZERO) {
				total[4] = total[4].add(total[i]);
			}
		}
		if (listLM012 != null && listLM012.size() != 0) {

//			setFontSize(8);
			
			int temp = 22;
			for (int i = 0; i < size; i++) {
				this.print(1, 1, "│　　　　　　　　　│　　　　　　│　　　　　│　　　　　　│　　　　　│　　　　　　│　　　　　│　　　　　　│　　　　　│　　　　　　│　　　　　│");
				if (i == 0) {
					this.print(0, 11, FormatUtil.padLeft(df3.format(rate[0]), 7), "R");
					this.print(0, 16, "以下", "R");
				} else if (i == size - 1) {
					this.print(0, 11, FormatUtil.padLeft(df3.format(rate[size - 2]), 7), "R");
					this.print(0, 16, "以上", "R");
				} else {
					this.print(0, 11, FormatUtil.padLeft(df3.format(rate[i - 1]), 7), "R");
					this.print(0, 19, "-" + FormatUtil.padLeft(df3.format(rate[i]), 7), "R");
				}
				temp = 0;
				for (int j = 0; j < 5; j++) {
//					BigDecimal result = this.computeDivide(value[i][j], new BigDecimal("1000000"), 0);
					BigDecimal result = value[i][j];// Last Version
					this.print(0, 31 + temp, this.formatAmt(result, 0), "R");
					if (!value[i][j].equals(BigDecimal.ZERO)) {
						BigDecimal percent = this.computeDivide(value[i][j], total[j], 4)
								.multiply(new BigDecimal("100"));
						this.print(0, 42 + temp, this.formatAmt(percent, 2), "R");
					} else {
						this.print(0, 42 + temp, "0.00", "R");
					}
					temp += 24;
				}
			} // for
			this.print(1, 1, "├─────────┼──────┼─────┼──────┼─────┼──────┼─────┼──────┼─────┼──────┼─────┤");
			this.print(1, 1, "│　　　　　　　　　│　　　　　　│　　　　　│　　　　　　│　　　　　│　　　　　　│　　　　　│　　　　　　│　　　　　│　　　　　　│　　　　　│");
			this.print(0, 7, "總　　　計");
			this.info("Print total!!!");
//			BigDecimal result = this.computeDivide(total[0], new BigDecimal("1000000"), 0);
			BigDecimal result = total[0];// Last Version
			this.print(0, 31, this.formatAmt(result, 0), "R");
//			result = this.computeDivide(total[1], new BigDecimal("1000000"), 0);
			result = total[1];// Last Version
			this.print(0, 55, this.formatAmt(result, 0), "R");
//			result = this.computeDivide(total[2], new BigDecimal("1000000"), 0);
			result = total[2];// Last Version
			this.print(0, 79, this.formatAmt(result, 0), "R");
//			result = this.computeDivide(total[3], new BigDecimal("1000000"), 0);
			result = total[3];// Last Version
			this.print(0, 103, this.formatAmt(result, 0), "R");
//			result = this.computeDivide(total[4], new BigDecimal("1000000"), 0);
			result = total[4];// Last Version
			this.print(0, 127, this.formatAmt(result, 0), "R");

			this.print(0, 42, "100.00", "R");
			this.print(0, 66, "100.00", "R");
			this.print(0, 90, "100.00", "R");
			this.print(0, 114, "100.00", "R");
			this.print(0, 138, "100.00", "R");

			this.print(1, 1, "└─────────┴──────┴─────┴──────┴─────┴──────┴─────┴──────┴─────┴──────┴─────┘");

		} else {
			this.print(-7, 1, "┌─────────┬────────────┬────────────┬────────────┬────────────┬────────────┐");
			this.print(-8, 1, "│　　　　　　　　　│　　　　　　　　　　　　│　　　　　　　　　　　　│　　　　　　　　　　　　│　　　　　　　　　　　　│　　　　　　　　　　　　│");
			/**
			 * ------------------------1---------2---------3---------4---------5---------6---------7---------8---------9---------0---------1---------2---------3---------4---------5---------6
			 * ---------------1234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890
			 */
			this.print(-8, 27, "短期擔保放款");
			this.print(-8, 50, "中期擔保放款");
			this.print(-8, 74, "長期擔保放款");
			this.print(-8, 98, "30　年房屋貸款");
			this.print(-8, 122, "合　　　　計");
			this.print(-9, 1, "│　　　　　　　　　├──────┬─────┼──────┬─────┼──────┬─────┼──────┬─────┼──────┬─────┤");
			this.print(-9, 7, "利　　率％");
			this.print(-10, 1, "│　　　　　　　　　│　　　　　　│　　　　　│　　　　　　│　　　　　│　　　　　　│　　　　　│　　　　　　│　　　　　│　　　　　　│　　　　　│");
			this.print(-11, 1, "│　　　　　　　　　│　　　　　　│　　　　　│　　　　　　│　　　　　│　　　　　　│　　　　　│　　　　　　│　　　　　│　　　　　　│　　　　　│");
			this.print(-12, 1, "├─────────┼──────┼─────┼──────┼─────┼──────┼─────┼──────┼─────┼──────┼─────┤");
			int temp = 22;
			for (int i = 0; i < 5; i++) {
				if (i != 4) {
					this.print(-10, 15 + temp, "對科目");
					this.print(-11, -1 + temp, "　餘　　額　");
					this.print(-11, 15 + temp, "比重％");
					temp = temp + 24;
				} else {
					this.print(-10, 15 + temp, "對總計");
					this.print(-11, -1 + temp, "　餘　　額　");
					this.print(-11, 15 + temp, "比重％");
					temp = 12;
				}
			}
			for (int i = 0; i < size; i++) {
//				String temp1 = String.valueOf(Double.parseDouble(te) + 0.25);
//				BigDecimal[] sum = new BigDecimal[5];
				this.print(1, 1, "│　　　　　　　　　│　　　　　　│　　　　　│　　　　　　│　　　　　│　　　　　　│　　　　　│　　　　　　│　　　　　│　　　　　　│　　　　　│");
				if (i == 0) {
					this.print(0, 11, FormatUtil.padLeft(df3.format(rate[0]), 7), "R");
					this.print(0, 16, "以下", "R");
					this.print(0, 31, "本日無資料", "R");
				} else if (i == size - 1) {
					this.print(0, 11, FormatUtil.padLeft(df3.format(rate[size - 2]), 7), "R");
					this.print(0, 16, "以上", "R");
				} else {
					this.print(0, 11, FormatUtil.padLeft(df3.format(rate[i - 1]), 7), "R");
					this.print(0, 19, "-" + FormatUtil.padLeft(df3.format(rate[i]), 7), "R");
				}
			}
			this.print(1, 1, "├─────────┼──────┼─────┼──────┼─────┼──────┼─────┼──────┼─────┼──────┼─────┤");
			this.print(1, 1, "│　　　　　　　　　│　　　　　　│　　　　　│　　　　　　│　　　　　│　　　　　　│　　　　　│　　　　　　│　　　　　│　　　　　　│　　　　　│");
			this.print(0, 7, "總　　　計");
			this.print(1, 1, "└─────────┴──────┴─────┴──────┴─────┴──────┴─────┴──────┴─────┴──────┴─────┘");
		}

		long sno = this.close();
		this.toPdf(sno);

		return true;
	}
	public static String getLastDayOfMonth(String year, String month) {
		Calendar cal = Calendar.getInstance();
	    cal.set(Calendar.YEAR, Integer.parseInt(year));
	    cal.set(Calendar.MONTH, Integer.parseInt(month) - 1);
	    cal.set(Calendar.DATE, 1);
	    cal.add(Calendar.MONTH,1);
	    cal.add(Calendar.DATE, -1);
	    return String.valueOf(cal.get(Calendar.DAY_OF_MONTH));
	}
}
