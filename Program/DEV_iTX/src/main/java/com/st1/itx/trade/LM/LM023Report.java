package com.st1.itx.trade.LM;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.db.service.springjpa.cm.LM023ServiceImpl;
import com.st1.itx.util.common.MakeReport;

@Component
@Scope("prototype")

public class LM023Report extends MakeReport {

	@Autowired
	public LM023ServiceImpl lM023ServiceImpl;

	@Override
	public void printHeader() {
		this.info("MakeReport.printHeader");

		this.setBeginRow(8);
		this.setMaxRows(45);
		this.setFontSize(10);
	}

	public void exec(TitaVo titaVo) throws LogicException {
		// 讀取VAR參數

		this.open(titaVo, titaVo.getEntDyI(), titaVo.getKinbr(), "LM023", "利息收入", "密", "", "L");
		this.setCharSpaces(0);

		List<Map<String, String>> lM023List = null;
		List<Map<String, String>> lM023List2 = null;

		try {
			lM023List = lM023ServiceImpl.findAll(titaVo);
			lM023List2 = lM023ServiceImpl.findAll2(titaVo);
		} catch (Exception e) {
			StringWriter errors = new StringWriter();
			e.printStackTrace(new PrintWriter(errors));
			this.info("lM023ServiceImpl.findAll error = " + errors.toString());
		}

		this.setFontSize(8);
		this.print(-4, 200, "機密等級：□極機密 □機密 ■密 □普通", "R");
		this.print(-5, 200, "文件持有人請嚴加管控本項文件", "R");

		this.setFontSize(10);
		this.print(-6, 10, "科目代號            科目中文             帳冊別              借餘          貸餘             原餘額");
		this.print(-7, 10, "──────────────────────────────────────────────────────────────");
		this.print(-7, 148, "10804 科子目", "C");

		String no = "";

		this.setFontSize(10);

		DecimalFormat df1 = new DecimalFormat("#,##0");
		if (lM023List.size() != 0 && lM023List != null) {
			for (int i = 0; i < lM023List.size(); i++) {

				if (i == 0) {
					no = lM023List.get(i).get("F0").toString();
					this.print(1, 9, lM023List.get(i).get("F0").toString());
					this.print(0, 21, lM023List.get(i).get("F1").toString());
					this.print(0, 46, lM023List.get(i).get("F2").toString());
					this.print(0, 50, lM023List.get(i).get("F3").toString());

				} else {
					if (no.equals(lM023List.get(i).get("F0").toString())) { // 是否同科目代號
						this.print(1, 46, lM023List.get(i).get("F2").toString());
						this.print(0, 50, lM023List.get(i).get("F3").toString());
					} else {
						no = lM023List.get(i).get("F0").toString();
						this.print(1, 9, lM023List.get(i).get("F0").toString());
						this.print(0, 21, lM023List.get(i).get("F1").toString());
						this.print(0, 46, lM023List.get(i).get("F2").toString());
						this.print(0, 50, lM023List.get(i).get("F3").toString());

					}

				}

				BigDecimal f4 = new BigDecimal(lM023List.get(i).get("F4").toString());
				if (f4.intValue() >= 0) {
					this.print(0, 76, df1.format(f4), "R");
					this.print(0, 90, "-", "R");
					this.print(0, 110, df1.format(f4), "R");
				} else {
					this.print(0, 76, "-", "R");
					this.print(0, 90, df1.format(f4), "R");
					this.print(0, 110, df1.format(f4), "R");
				}

			}
		} else {
			this.print(-8, 10, "本日無資料");
		}

		if (lM023List2.size() != 0 && lM023List2 != null) {
			String year = "";

			this.print(1, 1, "");
			this.newPage();
			this.setFontSize(12);
			this.print(-6, 5, "利息收入");

			this.print(-7, 5, "        M");

			for (int i = 0; i < 12; i++) {
				this.print(-7, 16 + (i * 9), (i + 1) + "月");
			}

			this.print(-7, 124, "Total");
			this.print(-8, 5, "");
			this.print(-9, 5, "預算       ");
			this.print(-10, 5, "上年度同期     ");
			this.print(-11, 5, "息收合計      ");
			this.print(1, 5, "         ");
			this.print(1, 2, "");
			this.print(1, 2, "");
			this.print(-15, 100, "　年度預算");
			this.print(-16, 100, "　年度現階段累積達成率");

			BigDecimal tempAmt = BigDecimal.ZERO;

			BigDecimal total0 = BigDecimal.ZERO;
			BigDecimal total1 = BigDecimal.ZERO;
			BigDecimal total2 = BigDecimal.ZERO;

//			DecimalFormat df2 = new DecimalFormat("#,##0");
			BigDecimal resTotal = BigDecimal.ZERO;
			BigDecimal percent = new BigDecimal("100");
			int colSpace = 0;

			int totalPos = 0;
			String dataSeq = "";
//			DecimalFormat df2 = new DecimalFormat("%.2f");

			int count = 0;

			for (int i = 0; i < lM023List2.size(); i++) {

				if (!dataSeq.equals(lM023List2.get(i).get("F0").toString())) {
					count = 0;
				}
				count++;
 
				// 0 今年 1預算 2上年度
				dataSeq = Integer.valueOf(lM023List2.get(i).get("F0")).toString();

				// 利息金額
				tempAmt = lM023List2.get(i).get("F3").isEmpty() ? BigDecimal.ZERO
						: new BigDecimal(lM023List2.get(i).get("F3"));
				// 欄位間隔
				colSpace = Integer.valueOf(lM023List2.get(i).get("F2")) - 1;

				if (dataSeq.equals("0")) {

					year = String.valueOf(Integer.valueOf(lM023List2.get(i).get("F1")) - 1911);

					total0 = total0.add(tempAmt);

					this.print(-8, 16 + (colSpace * 9), df1.format(tempAmt), "L");

					this.print(-11, 16 + (colSpace * 9), df1.format(total0), "L");

				}

				if (dataSeq.equals("1")) {
					total1 = total1.add(tempAmt);
					this.print(-9, 16 + (colSpace * 9), df1.format(tempAmt), "L");

				}

				if (dataSeq.equals("2")) {
					total2 = total2.add(tempAmt);
					this.print(-10, 16 + (colSpace * 9), df1.format(tempAmt), "L");

				}

			}

			totalPos = 124;

			this.print(-8, totalPos, df1.format(total0), "L");
			this.print(-9, totalPos, df1.format(total1), "L");
			this.print(-10, totalPos, df1.format(total2), "L");

			resTotal = total0 == BigDecimal.ZERO || total2 == BigDecimal.ZERO ? BigDecimal.ZERO
					: total0.divide(total2, 4, BigDecimal.ROUND_HALF_UP).multiply(percent);
			this.print(-12, totalPos, resTotal + "%", "L");

			this.print(-8, 5, year + "年度");
			this.print(-15, 111, total1 + "億。");
			this.print(-16, 122, resTotal + "%");

		} else {

			this.print(-8, 10, "本日無資料");

		}

		long sno = this.close();
		this.toPdf(sno);
	}

}
