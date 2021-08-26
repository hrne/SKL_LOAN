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
import com.st1.itx.util.date.DateUtil;

@Component
@Scope("prototype")

public class LM023Report extends MakeReport {

	@Autowired
	public LM023ServiceImpl lM023ServiceImpl;

	@Autowired
	DateUtil dDateUtil;

	@Override
	public void printHeader() {
		this.info("MakeReport.printHeader");

		this.setBeginRow(6);
		this.setMaxRows(45);
		this.setFontSize(8);
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

		String No = "";
		
		
		
		this.setFontSize(10);

		DecimalFormat df1 = new DecimalFormat("#,##0");
		if (lM023List.size() != 0 && lM023List != null) {
			for (int i = 0; i < lM023List.size(); i++) {

				if (i == 0) {
					No = lM023List.get(i).get("F0").toString();
					this.print(1, 9, lM023List.get(i).get("F0").toString());
					this.print(0, 18, lM023List.get(i).get("F1").toString());
					this.print(0, 43, lM023List.get(i).get("F2").toString());
					this.print(0, 47, lM023List.get(i).get("F3").toString());

				} else {
					if (No.equals(lM023List.get(i).get("F0").toString())) { // 是否同科目代號
						this.print(1, 43, lM023List.get(i).get("F2").toString());
						this.print(0, 47, lM023List.get(i).get("F3").toString());
					} else {
						No = lM023List.get(i).get("F0").toString();
						this.print(1, 9, lM023List.get(i).get("F0").toString());
						this.print(0, 18, lM023List.get(i).get("F1").toString());
						this.print(0, 43, lM023List.get(i).get("F2").toString());
						this.print(0, 47, lM023List.get(i).get("F3").toString());

					}

				}

				BigDecimal f4 = new BigDecimal(lM023List.get(i).get("F4").toString());
				if (f4.intValue() >= 0) {
					this.print(0, 73, df1.format(f4), "R");
					this.print(0, 87, "-");
					this.print(0, 107, df1.format(f4), "R");
				} else {
					this.print(0, 71, "-");
					this.print(0, 89, df1.format(f4), "R");
					this.print(0, 107, df1.format(f4), "R");
				}

			}
		} else {
			this.print(-8, 10, "本日無資料");
		}
		;

		if (lM023List2.size() != 0 && lM023List2 != null) {
			String year = "";

			this.print(1, 1, "");
			this.newPage();
			this.setFontSize(12);
			this.print(-6, 5, "利息收入");
			this.print(-7, 5,
					"        M 1月      2月      3月      4月      5月       6月      7月      8月      9月      10月      11月      12月      Total");
			this.print(-8, 5, "");
			this.print(-9, 5, "預算       ");
			this.print(-10, 5, "上年度同期     ");
			this.print(-11, 5, "息收合計      ");
			this.print(1, 5, "         ");
			this.print(1, 2, "");
			this.print(1, 2, "");
			this.print(-15, 100, "　年度預算　億。");
			this.print(-16, 100, "　年度現階段累積達成率　%");

			BigDecimal total = BigDecimal.ZERO;
//			DecimalFormat df1 = new DecimalFormat("#,##0");
			int totalPos = 0;
			for (int i = 0; i < lM023List2.size(); i++) {

				year = String.valueOf(Integer.valueOf(lM023List2.get(i).get("F1")) - 1911);
//				lM023List2.get(i).get("F0").toString();

				lM023List2.get(i).get("F2").toString();
				lM023List2.get(i).get("F3").toString();

				total = total.add(new BigDecimal(lM023List2.get(i).get("F1")));
				this.print(-8, 15 + (i * 9), df1.format(new BigDecimal(lM023List2.get(i).get("F1"))), "L");
				totalPos = 15 + (i * 9) + 9;
			}
			this.print(-8, totalPos, df1.format(total), "L");
			this.print(-8, 5, year);

		} else {
			this.print(-8, 10, "本日無資料");
		}

		long sno = this.close();
		this.toPdf(sno);
	}

//	if (lM023List.size() !=0 && lM023List != null) {
//	this.setFontSize(8);
//	this.print(-1, 120, "機密等級：□極機密 □機密 ■密 □普通");
//	this.print(-2, 124, "文件持有人請嚴加管控本項文件");
//
//	this.setFontSize(10);
//	this.print(1, 5, "科目代號            科目中文             帳冊別              借餘          貸餘             原餘額");
//	this.print(1, 5, "──────────────────────────────────────────────────────────────                           10804 科子目");
//	String No = "";
//
//	DecimalFormat df1 = new DecimalFormat("#,##0");
//	for (int i = 0; i < lM023List.size(); i++) {
//
//		if (i == 0) {
//			No = lM023List.get(i).get("F0").toString();
//			this.print(1, 9, lM023List.get(i).get("F0").toString());
//			this.print(0, 18, lM023List.get(i).get("F1").toString());
//			this.print(0, 43, lM023List.get(i).get("F2").toString());
//			this.print(0, 47, lM023List.get(i).get("F3").toString());
//
//		} else {
//			if (No.equals(lM023List.get(i).get("F0").toString())) { // 是否同科目代號
//				this.print(1, 43, lM023List.get(i).get("F2").toString());
//				this.print(0, 47, lM023List.get(i).get("F3").toString());
//			} else {
//				No = lM023List.get(i).get("F0").toString();
//				this.print(1, 9, lM023List.get(i).get("F0").toString());
//				this.print(0, 18, lM023List.get(i).get("F1").toString());
//				this.print(0, 43, lM023List.get(i).get("F2").toString());
//				this.print(0, 47, lM023List.get(i).get("F3").toString());
//
//			}
//
//		}
//
//		BigDecimal f4 = new BigDecimal(lM023List.get(i).get("F4").toString());
//		if (f4.intValue() >= 0) {
//			this.print(0, 73, df1.format(f4), "R");
//			this.print(0, 87, "-");
//			this.print(0, 107, df1.format(f4), "R");
//		} else {
//			this.print(0, 71, "-");
//			this.print(0, 89, df1.format(f4), "R");
//			this.print(0, 107, df1.format(f4), "R");
//		}
//
//	}
//
//	this.newPage();
//	this.print(1, 5, "利息收入");
//	this.print(1, 5, "        M 1月      2月      3月      4月      5月       6月      7月      8月      9月      10月      11月      12月      Total");
//	this.print(1, 5, "108年度  ");
//	this.print(1, 5, "預算       ");
//	this.print(1, 5, "上年度同期     ");
//	this.print(1, 5, "息收合計      ");
//	this.print(1, 5, "         ");
//	this.print(1, 2, "");
//	this.print(1, 2, "");
//	this.print(1, 60, "　年度預算　億。");
//	this.print(1, 60, "　年度現階段累積達成率　%");
//
//}else {
//	this.print(-3, 160, "機密等級：□極機密 □機密 ■密 □普通","R");
//	this.print(-4, 160, "文件持有人請嚴加管控本項文件","R");
//	
//	this.print(-6, 5, "科目代號            科目中文             帳冊別              借餘          貸餘             原餘額");
//	this.print(-7, 5, "──────────────────────────────────────────────────────────────");
//	
//	this.print(-7, 150, "10804 科子目","C");
//	newPage();
//	this.print(1, 5, "利息收入");
//	this.print(1, 5, "        M 1月      2月      3月      4月      5月       6月      7月      8月      9月      10月      11月      12月      Total");
//	this.print(1, 5, "108年度       ");
//	this.print(1, 5, "預算           ");
//	this.print(1, 5, "上年度同期     ");
//	this.print(1, 5, "息收合計     ");
//
//}

}
