package com.st1.itx.trade.LM;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.st1.itx.Exception.DBException;
import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.db.domain.MonthlyLM052Loss;
import com.st1.itx.db.service.MonthlyLM052LossService;
import com.st1.itx.db.service.springjpa.cm.LM052ServiceImpl;
import com.st1.itx.util.common.MakeExcel;
import com.st1.itx.util.common.MakeReport;
import com.st1.itx.util.parse.Parse;

@Service
@Scope("prototype")

public class LM052Report extends MakeReport {

	@Autowired
	LM052ServiceImpl lM052ServiceImpl;

	@Autowired
	MonthlyLM052LossService sMonthlyLM052LossService;

	@Autowired
	MakeExcel makeExcel;

	@Autowired
	Parse parse;

	// 五類資產評估合計
	BigDecimal assetClassTotal = BigDecimal.ZERO;

	// 法定備抵損失提撥
	BigDecimal lossTotal = BigDecimal.ZERO;

	@Override
	public void printTitle() {

	}

	/**
	 * 執行報表輸出
	 * 
	 * @param titaVo
	 * @param yearMonth 西元年月
	 * 
	 */
	public void exec(TitaVo titaVo, int yearMonth) throws LogicException {

		// 上年月
		int lastYM = yearMonth - 1;

		// 判斷是否為一月
		if (yearMonth % 100 == 1) {
			lastYM = ((yearMonth / 100) - 1) * 100 + 12;
		}

		this.info("yymm=" + yearMonth + ",lyymm=" + lastYM);
		this.info("LM052Report exportExcel");

		makeExcel.open(titaVo, titaVo.getEntDyI(), titaVo.getKinbr(), "LM052", "放款資產分類-會計部備呆計提", "LM052_放款資產分類-會計部備呆計提",
				"LM052_底稿_放款資產分類-會計部備呆計提.xlsx", "備呆總表");

		String formTitle = "";

		formTitle = ((yearMonth / 100) - 1911) + "年 " + String.format("%02d", yearMonth % 100) + "月底   放款資產品質分類";
		makeExcel.setValue(1, 1, formTitle);

		formTitle = (lastYM - 191100) + "\n" + "放款總額";
		makeExcel.setValue(16, 3, formTitle, "C");

		List<Map<String, String>> lM052List = null;

		for (int formNum = 1; formNum <= 5; formNum++) {

			try {

				lM052List = lM052ServiceImpl.findAll(titaVo, yearMonth, lastYM, formNum);

			} catch (Exception e) {

				// TODO Auto-generated catch block
				StringWriter errors = new StringWriter();
				e.printStackTrace(new PrintWriter(errors));
				this.info("LM052ServiceImpl.findAll error = " + errors.toString());

			}

			exportExcel(lM052List, formNum, titaVo, yearMonth);
		}
		// 更新MonthlyLM052Loss(五類資產評估合計、法定備抵損失提撥、會計部核定備抵損失)
		checkAndUpdateData(titaVo, yearMonth);

		writeLoss(titaVo, yearMonth);

		makeExcel.close();

	}

	private void writeLoss(TitaVo titaVo, int yearMonth) throws LogicException {

		MonthlyLM052Loss thisMonthlyLM052Loss = new MonthlyLM052Loss();
		MonthlyLM052Loss lastMonthlyLM052Loss = new MonthlyLM052Loss();
		MonthlyLM052Loss last2MonthlyLM052Loss = new MonthlyLM052Loss();

		int lyearMonth = yearMonth == 1 ? 12 : yearMonth - 1;
		int l2yearMonth = lyearMonth == 1 ? 12 : lyearMonth - 1;

		try {
			thisMonthlyLM052Loss = sMonthlyLM052LossService.findById(yearMonth, titaVo);
			lastMonthlyLM052Loss = sMonthlyLM052LossService.findById(lyearMonth, titaVo);
			last2MonthlyLM052Loss = sMonthlyLM052LossService.findById(l2yearMonth, titaVo);

		} catch (Exception e) {
			StringWriter errors = new StringWriter();
			e.printStackTrace(new PrintWriter(errors));
			this.error("llMonthlyLM052Loss findAll error = " + errors.toString());
		}

		String assetClass = "0";
		String legalLoss = "0";
		String approvedLoss = "0";
		String approvedLossDiff = "0";

		if (thisMonthlyLM052Loss != null) {
			assetClass = formatAmt(thisMonthlyLM052Loss.getAssetEvaTotal().toString(), 2, 6);
			legalLoss = formatAmt(thisMonthlyLM052Loss.getLegalLoss().toString(), 2, 6);
			approvedLoss = formatAmt(thisMonthlyLM052Loss.getApprovedLoss().toString(), 2, 6);
		}

		if (lastMonthlyLM052Loss != null && last2MonthlyLM052Loss != null) {
			// 前兩個月的備抵損失 減去 上個月的備抵損失 = 本月預期損失金額
			approvedLossDiff = formatAmt(
					(last2MonthlyLM052Loss.getApprovedLoss().subtract(lastMonthlyLM052Loss.getApprovedLoss()))
							.toString(),
					2, 6);
		}

		makeExcel.setFontType(1);
		makeExcel.setValue(31, 2, "一、依放款資產評估辦法三項標準評估後，以五類資產評估 (Ａ)金額 " + assetClass + " 百萬元為高。\n"
				+ "二、依金管會104年07月24日金管保財字第10402506096號令備抵損失提存比率為放款餘額\n" + "1.5%評估金額為 " + legalLoss + " 百萬元。\n"
				+ "三、IFRS 9預期損失金額依據放款各相關權責單位：PD違約機率（放款審查課）、LGD違約損失率\n"
				+ " （放款管理課）、EAD曝險額（放款服務課）提供相關數據，並由放款服務課 於預期損失計算系\n" + "統完成核帳。本月預期損失金額為 " + approvedLossDiff + " 百萬元。\n"
				+ "四、公司備抵損失至108 年03月實際提列 " + approvedLoss + " 百萬元，較最高『IFRS9預期\n" + " 損失金額』相比，提列金額尚足。\n" + "五、陳核。");

	}

	/*
	 * 應收利息提列2%：用MonthlyFacBal的應收利息加總、前者*0.02
	 * 
	 * 五類資產評估合計：M13+M14 1-5類總額(含應收息)提列1%：F13+L14 *0.01 無擔保案件總金額：F11
	 * 
	 * 法定備抵損失提撥(含應收息1%)：特定和非特定資產的提存金額 (G28+L28)+ 應收利息提列2%(L14)
	 * 
	 * 
	 * 問題： B30的 第三項 最後面金額 本月預期損失金額 30.89百萬元 (從IFRS9) 第四項 的4月份 十計提列 638.283百萬元
	 * 
	 * 
	 */
	private void exportExcel(List<Map<String, String>> LDList, int formNum, TitaVo titaVo, int yearMonth)
			throws LogicException {

		BigDecimal amt = BigDecimal.ZERO;

		if (LDList.size() > 0) {

			int row = 0;
			int col = 0;
			BigDecimal rate = BigDecimal.ZERO;
			for (Map<String, String> tLDVo : LDList) {

				switch (formNum) {
				case 1:

					if ("11".equals(tLDVo.get("F0"))) {
						row = 4;
						rate = new BigDecimal("0.005");
					}
					if ("12".equals(tLDVo.get("F0"))) {
						row = 5;
						rate = new BigDecimal("0.015");
					}
					if ("21".equals(tLDVo.get("F0"))) {
						row = 6;
						rate = new BigDecimal("0.02");
					}
					if ("22".equals(tLDVo.get("F0"))) {
						row = 7;
						rate = new BigDecimal("0.02");
					}
					if ("23".equals(tLDVo.get("F0"))) {
						row = 8;
						rate = new BigDecimal("0.02");
					}
					if ("3".equals(tLDVo.get("F0"))) {
						row = 9;
						rate = new BigDecimal("0.1");
					}
					if ("4".equals(tLDVo.get("F0"))) {
						row = 10;
						rate = new BigDecimal("0.5");
					}
					if ("5".equals(tLDVo.get("F0"))) {
						row = 11;
						rate = new BigDecimal("1");
					}
					if ("61".equals(tLDVo.get("F0"))) {
						row = 12;
						rate = new BigDecimal("0.005");
					}
					if ("62".equals(tLDVo.get("F0"))) {
						row = 13;
						rate = new BigDecimal("0.2");
					}
					if ("7".equals(tLDVo.get("F0"))) {
						row = 15;
						rate = new BigDecimal("0.02");
					}

					if ("00A".equals(tLDVo.get("F1"))) {
						col = 3;
					}

					if ("201".equals(tLDVo.get("F1"))) {
						col = 4;
					}
					if ("61".equals(tLDVo.get("F0")) && "999".equals(tLDVo.get("F1"))) {
						col = 6;
					}

					if ("62".equals(tLDVo.get("F0")) && "999".equals(tLDVo.get("F1"))) {
						col = 6;
					}

					if ("7".equals(tLDVo.get("F0")) && "999".equals(tLDVo.get("F1"))) {
						col = 12;
					}

					amt = tLDVo.get("F2").isEmpty() ? BigDecimal.ZERO : new BigDecimal(tLDVo.get("F2"));

					assetClassTotal = assetClassTotal.add(amt.multiply(rate).setScale(0, BigDecimal.ROUND_HALF_UP));

					break;
				case 2:

					if ("1".equals(tLDVo.get("F0"))) {
						row = 18;
					}
					if ("21".equals(tLDVo.get("F0"))) {
						row = 19;
					}
					if ("22".equals(tLDVo.get("F0"))) {
						row = 20;
					}
					if ("23".equals(tLDVo.get("F0"))) {
						row = 21;
					}
					if ("3".equals(tLDVo.get("F0"))) {
						row = 22;
					}
					if ("4".equals(tLDVo.get("F0"))) {
						row = 23;
					}
					if ("5".equals(tLDVo.get("F0"))) {
						row = 24;
					}
					if ("61".equals(tLDVo.get("F0")) || "62".equals(tLDVo.get("F0"))) {
						row = 25;
					}

					col = 3;

					amt = tLDVo.get("F1").isEmpty() ? BigDecimal.ZERO : new BigDecimal(tLDVo.get("F1"));

					break;
				case 3:

					row = "1".equals(tLDVo.get("F0")) ? 17 : "2".equals(tLDVo.get("F0")) ? 18 : 19;

					col = "310".equals(tLDVo.get("F1")) ? 9 : "320".equals(tLDVo.get("F1")) ? 8 : 7;

					amt = tLDVo.get("F2").isEmpty() ? BigDecimal.ZERO : new BigDecimal(tLDVo.get("F2"));

					break;

				case 4:
					row = 28;

					if ("S1".equals(tLDVo.get("F0"))) {
						col = 7;
						rate = new BigDecimal("0.015");
					}
					if ("S2".equals(tLDVo.get("F0"))) {
						col = 8;
						rate = new BigDecimal("0.015");
					}
					if ("NS1".equals(tLDVo.get("F0"))) {
						col = 9;
						rate = new BigDecimal("0.01");
					}
					if ("NS2".equals(tLDVo.get("F0"))) {
						col = 11;
						rate = new BigDecimal("0.01");
					}
					if ("NS3".equals(tLDVo.get("F0"))) {
						col = 12;
						rate = new BigDecimal("0.01");
					}

					amt = tLDVo.get("F1").isEmpty() ? BigDecimal.ZERO : new BigDecimal(tLDVo.get("F1"));

					lossTotal = lossTotal.add(amt.multiply(rate).setScale(0, BigDecimal.ROUND_HALF_UP));

					break;

				case 5:

					amt = tLDVo.get("F1").isEmpty() ? BigDecimal.ZERO : new BigDecimal(tLDVo.get("F1"));
					makeExcel.setValue(28, 15, amt, "#,##0");
					makeExcel.setValue(28, 16, "五類資產評估合計");

					amt = tLDVo.get("F2").isEmpty() ? BigDecimal.ZERO : new BigDecimal(tLDVo.get("F2"));
					makeExcel.setValue(29, 15, amt, "#,##0");
					makeExcel.setValue(29, 16, "法定備抵損失提撥");

					amt = tLDVo.get("F3").isEmpty() ? BigDecimal.ZERO : new BigDecimal(tLDVo.get("F3"));
					makeExcel.setValue(30, 15, amt, "#,##0");
					makeExcel.setValue(30, 16, "會計部核定備抵損失");

					col = 0;
					break;
				}

				if (row != 0 && col != 0) {
					this.info("formNum" + formNum + ":row,col=" + row + "," + col);
					makeExcel.setValue(row, col, amt, "#,##0");
				}

			}

			// C14~E14
			makeExcel.formulaCaculate(14, 3);
			makeExcel.formulaCaculate(14, 4);
			makeExcel.formulaCaculate(14, 5);

			// F4~F11
			for (int r = 4; r <= 11; r++) {
				makeExcel.formulaCaculate(r, 6);
			}

			// F14
			makeExcel.formulaCaculate(14, 6);

			// J4~M13 M14
			for (int r = 4; r <= 14; r++) {
				for (int c = 10; c <= 13; c++) {
					makeExcel.formulaCaculate(r, c);
				}
			}

			// M15
			makeExcel.formulaCaculate(15, 13);

			// C26 D18~D26
			makeExcel.formulaCaculate(26, 3);
			for (int r = 18; r <= 26; r++) {
				makeExcel.formulaCaculate(r, 4);
			}

			// G20 H20 I20 G21
			makeExcel.formulaCaculate(20, 7);
			makeExcel.formulaCaculate(20, 8);
			makeExcel.formulaCaculate(20, 9);
			makeExcel.formulaCaculate(21, 7);

			// M17~19
			makeExcel.formulaCaculate(17, 13);
			makeExcel.formulaCaculate(18, 13);
			makeExcel.formulaCaculate(19, 13);

			// G29 L29 M25
			makeExcel.formulaCaculate(29, 7);
			makeExcel.formulaCaculate(29, 12);
			makeExcel.formulaCaculate(25, 13);

			// B31
			makeExcel.formulaCaculate(31, 2);

			// set height row 1,4~13,23,24
			makeExcel.setHeight(1, 40);

			for (int r = 4; r <= 13; r++) {
				makeExcel.setHeight(r, 40);
			}

			makeExcel.setHeight(23, 20);
			makeExcel.setHeight(24, 40);

		}

	}

	private void checkAndUpdateData(TitaVo titaVo, int yearMonth) throws LogicException {

		MonthlyLM052Loss fMonthlyLM052Loss = new MonthlyLM052Loss();

		try {
			fMonthlyLM052Loss = sMonthlyLM052LossService.findById(yearMonth, titaVo);
		} catch (Exception e) {
			StringWriter errors = new StringWriter();
			e.printStackTrace(new PrintWriter(errors));
			this.error("llMonthlyLM052Loss findAll error = " + errors.toString());
		}

//		this.info("fMonthlyLM052Loss=" + fMonthlyLM052Loss.toString());

		// 判斷有無當月資料
		if (fMonthlyLM052Loss == null) {
			this.info("insert data");
			MonthlyLM052Loss insMonthlyLM052Loss = new MonthlyLM052Loss();

			insMonthlyLM052Loss.setYearMonth(yearMonth);
			insMonthlyLM052Loss.setApprovedLoss(lossTotal);
			insMonthlyLM052Loss.setAssetEvaTotal(assetClassTotal);
			insMonthlyLM052Loss.setLegalLoss(lossTotal);
			insMonthlyLM052Loss.setCreateDate(
					parse.IntegerToSqlDateO(dDateUtil.getNowIntegerForBC(), dDateUtil.getNowIntegerTime()));
			insMonthlyLM052Loss.setCreateEmpNo("999999");
			insMonthlyLM052Loss.setLastUpdate(
					parse.IntegerToSqlDateO(dDateUtil.getNowIntegerForBC(), dDateUtil.getNowIntegerTime()));
			insMonthlyLM052Loss.setLastUpdateEmpNo("999999");

			try {

				sMonthlyLM052LossService.insert(insMonthlyLM052Loss, titaVo);
			} catch (DBException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} else {

			// 如果不等於0就不用更新
			if (fMonthlyLM052Loss.getLegalLoss().compareTo(BigDecimal.ZERO) != 0) {
				this.info("no update");
				return;
			}

			this.info("update data");

			fMonthlyLM052Loss.setApprovedLoss(fMonthlyLM052Loss.getApprovedLoss());
			fMonthlyLM052Loss.setAssetEvaTotal(assetClassTotal);
			fMonthlyLM052Loss.setLegalLoss(lossTotal);

			fMonthlyLM052Loss.setCreateDate(
					parse.IntegerToSqlDateO(dDateUtil.getNowIntegerForBC(), dDateUtil.getNowIntegerTime()));
			fMonthlyLM052Loss.setCreateEmpNo("999999");
			fMonthlyLM052Loss.setLastUpdate(
					parse.IntegerToSqlDateO(dDateUtil.getNowIntegerForBC(), dDateUtil.getNowIntegerTime()));
			fMonthlyLM052Loss.setLastUpdateEmpNo("999999");

			try {

				sMonthlyLM052LossService.update(fMonthlyLM052Loss, titaVo);
			} catch (DBException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

	}

}