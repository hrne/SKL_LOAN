package com.st1.itx.trade.LM;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.st1.itx.Exception.DBException;
import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.db.domain.MonthlyLM052Loss;
import com.st1.itx.db.service.MonthlyLM052LossService;
import com.st1.itx.db.service.springjpa.cm.LM051ServiceImpl;
import com.st1.itx.util.common.MakeExcel;
import com.st1.itx.util.common.MakeReport;
import com.st1.itx.util.common.data.ReportVo;
import com.st1.itx.util.parse.Parse;

@Component
@Scope("prototype")

public class LM051Report extends MakeReport {

	@Autowired
	LM051ServiceImpl LM051ServiceImpl;

	@Autowired
	MonthlyLM052LossService sMonthlyLM052LossService;

	@Autowired
	MakeExcel makeExcel;

	@Autowired
	Parse parse;

	// 起始列印行數
	int row = 2;

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
	 * @throws LogicException
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

		int reportDate = titaVo.getEntDyI() + 19110000;
		String brno = titaVo.getBrno();
		String txcd = "LM051";
		String fileItem = "放款資產分類案件明細表_內部控管";
		String fileName = "LM051_放款資產分類案件明細表_內部控管";
		String defaultExcel = "LM051_底稿_放款資產分類案件明細表_內部控管.xlsx";
		String defaultSheet = "備呆總表";

		this.info("reportVo open");

		ReportVo reportVo = ReportVo.builder().setRptDate(reportDate).setBrno(brno).setRptCode(txcd)
				.setRptItem(fileItem).build();
		// 開啟報表
		makeExcel.open(titaVo, reportVo, fileName, defaultExcel, defaultSheet);

//		makeExcel.open(titaVo, titaVo.getEntDyI(), titaVo.getKinbr(), "LM051", "放款資產分類案件明細表_內部控管",
//				"LM051_放款資產分類案件明細表_內部控管", "LM051_底稿_放款資產分類案件明細表_內部控管.xlsx", "備呆總表");

		String formTitle = "";

		formTitle = ((yearMonth / 100) - 1911) + "年 " + String.format("%02d", yearMonth % 100) + "月底   放款資產品質分類";
		makeExcel.setValue(1, 1, formTitle);

		formTitle = (lastYM - 191100) + "\n" + "放款總額";
		makeExcel.setValue(16, 3, formTitle, "C");

		List<Map<String, String>> lM051List = null;

		for (int formNum = 1; formNum <= 5; formNum++) {

			try {

				lM051List = LM051ServiceImpl.findAll(titaVo, yearMonth, lastYM, formNum);

			} catch (Exception e) {

				// TODO Auto-generated catch block
				StringWriter errors = new StringWriter();
				e.printStackTrace(new PrintWriter(errors));
				this.info("LM051ServiceImpl.findAll error = " + errors.toString());

			}

			exportExcel(lM051List, formNum, titaVo, yearMonth);
		}
		// 更新MonthlyLM052Loss(五類資產評估合計、法定備抵損失提撥、會計部核定備抵損失)
		checkAndUpdateData(titaVo, yearMonth);

		writeLoss(titaVo, yearMonth);

		makeExcel.setSheet("10804工作表", yearMonth + "工作表");

		makeExcel.setValue(1, 2, "資產分類案件明細表" + yearMonth / 100 + "." + yearMonth % 100);

		try {

			lM051List = LM051ServiceImpl.findAll2(titaVo, yearMonth);

		} catch (Exception e) {
			// TODO Auto-generated catch block
			StringWriter errors = new StringWriter();
			e.printStackTrace(new PrintWriter(errors));
			this.info("LM051ServiceImpl.findAll error = " + errors.toString());
		}

		if (lM051List.size() > 0) {
			exportExcel2(lM051List);
		}

		if (lM051List.size() == 0) {
			makeExcel.setValue(3, 1, "本日無資料");
		}

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

					this.info("asset=" + amt.multiply(rate).setScale(0, BigDecimal.ROUND_HALF_UP));

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

					this.info("loss=" + amt.multiply(rate).setScale(0, BigDecimal.ROUND_HALF_UP));

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

	// YYYMM工作表

	// F0+F1
	// F0 戶號：2
	// F1 額度：3
	// F2 利變；4
	// F3 戶名；5
	// F4 本金餘額；6
	// F5 科目；7
	// F6 逾期數；8
	// F7 地區別；9
	// F8 繳息日期；10
	// F9分類項目:11
	// F4 五類金額(用F16區分F4)=；12~17
	// F10 分類標準(文字)；18
	// F11 金額 19
	// F12 備註；20
	// F13 基本利率代碼(商品代號)；21

	private void exportExcel2(List<Map<String, String>> LDList) throws LogicException {
		this.info("LM051Report exportExcel");
		if (LDList != null && !LDList.isEmpty()) {

			for (Map<String, String> tLDVo : LDList) {
				row++;
				// F0+F1:1
				makeExcel.setValue(row, 1, tLDVo.get("F0") + tLDVo.get("F1"));
				// F0 戶號：2
				makeExcel.setValue(row, 2, Integer.valueOf(tLDVo.get("F0")));
				// F1 額度：3
				makeExcel.setValue(row, 3, Integer.valueOf(tLDVo.get("F1")), "C");
				// F2 利變；4
				makeExcel.setValue(row, 4,
						tLDVo.get("F2") == null || tLDVo.get("F2").length() == 0 ? ' ' : tLDVo.get("F2"), "C");
				// F3 戶名；5
				makeExcel.setValue(row, 5, tLDVo.get("F3"), "L");
				// F4 本金餘額；6
				if (tLDVo.get("F4").equals("")) {
					makeExcel.setValue(row, 6, 0);
				} else {
					makeExcel.setValue(row, 6, Float.valueOf(tLDVo.get("F4")), "#,##0");
				}

				// F5 科目；7
				makeExcel.setValue(row, 7, (tLDVo.get("F5").equals("   ")||tLDVo.get("F5")==null)?0:Integer.valueOf(tLDVo.get("F5")), "L");
				// F6 逾期數；8

				String ovduText = tLDVo.get("F6");

				makeExcel.setValue(row, 8, ovduText, "C");
				// F7 地區別；9
				makeExcel.setValue(row, 9,
						tLDVo.get("F7") == null || tLDVo.get("F7").length() == 0 ? 0 : Integer.valueOf(tLDVo.get("F7")),
						"C");
				// F8 繳息日期；10
				makeExcel.setValue(row, 10,
						tLDVo.get("F8") == null || tLDVo.get("F8").length() == 0 ? 0 : Integer.valueOf(tLDVo.get("F8")),
						"C");
				// F9分類項目:11
				makeExcel.setValue(row, 11, tLDVo.get("F9"), "C");
				// F4 五類金額(用F16區分F4)=；12~17
				putAsset(row, tLDVo.get("F17"), tLDVo.get("F16"));
				// F10 分類標準(文字)；18
				String classText = "";
				if (tLDVo.get("F13") == "60" || tLDVo.get("F13") == "61" || tLDVo.get("F13") == "62") {
					if (!tLDVo.get("F10").isEmpty()) {
						classText = "協議戶 /" + tLDVo.get("F10");
					} else {
						classText = "協議戶 ";
					}
				} else {
					classText = tLDVo.get("F10");
				}
				makeExcel.setValue(row, 18, tLDVo.get("F10").isEmpty() ? "核貸估價" : classText, "L");
				// F11 金額 19
				makeExcel.setValue(row, 19, (tLDVo.get("F11").isEmpty())?0:Integer.valueOf(tLDVo.get("F11")), "#,##0");
				// F12 備註；20
				makeExcel.setValue(row, 20, tLDVo.get("F12"), "L");
				// F13 基本利率代碼(商品代號)；21
				makeExcel.setValue(row, 21, tLDVo.get("F13"), "C");

				// F17 無擔保金額
				// 本身資產分類不是5 且 金額不等於0 時，放入值
				BigDecimal class5 = tLDVo.get("F15").isEmpty() || tLDVo.get("F15") == null ? BigDecimal.ZERO
						: new BigDecimal(tLDVo.get("F15"));
				if (tLDVo.get("F16") != "5" && !BigDecimal.ZERO.equals(class5)) {
					makeExcel.setValue(row, 17, class5, "#,##0");

				}

			}
		} else {
			makeExcel.setValue(3, 1, "本日無資料", "L");
		}

	}

	/**
	 * 資產五分類 欄位位置
	 * 
	 * @param row         列數
	 * @param prinBalance 放款金額
	 * @param assetClass  資產五分類
	 * 
	 * 
	 */

	private void putAsset(int row, String prinBalance, String assetClass) throws LogicException {
		int col = 0;
//			String memo = "";
		switch (assetClass) {
		case "21":
			col = 12;

			break;
		case "22":
			col = 13;
			break;
		case "23":
			col = 14;
			break;
		case "3":
			col = 15;
			break;
		case "4":
			col = 16;
			break;
		case "5":
			col = 17;
			break;
		default:
			col = 0;
			break;
		}
		if (col > 0) {
			makeExcel.setValue(row, col, Float.valueOf(prinBalance), "#,##0");
		}
	}

}
