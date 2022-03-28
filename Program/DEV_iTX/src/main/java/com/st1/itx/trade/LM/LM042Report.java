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
import com.st1.itx.db.domain.MonthlyLM042RBC;
import com.st1.itx.db.domain.MonthlyLM042RBCId;
import com.st1.itx.db.service.MonthlyLM042RBCService;
import com.st1.itx.db.service.springjpa.cm.LM042ServiceImpl;
import com.st1.itx.util.common.MakeExcel;
import com.st1.itx.util.common.MakeReport;
import com.st1.itx.util.parse.Parse;

@Component
@Scope("prototype")

public class LM042Report extends MakeReport {

	@Autowired
	LM042ServiceImpl lM042ServiceImpl;

	@Autowired
	MakeExcel makeExcel;

	@Autowired
	MonthlyLM042RBCService sMonthlyLM042RBCService;

	@Autowired
	Parse parse;

	public int iEntdy = 0;
	public int iYear = 0;
	public int iMonth = 0;

	/* 統計數(左上表)--------------------------------------- */
	BigDecimal cY1Amt = BigDecimal.ZERO;
	BigDecimal cN1Amt = BigDecimal.ZERO;
	BigDecimal cN2Amt = BigDecimal.ZERO;
	BigDecimal cN3Amt = BigDecimal.ZERO;
	BigDecimal cN5Amt = BigDecimal.ZERO;
	BigDecimal dN1Amt = BigDecimal.ZERO;
	BigDecimal zY1Amt = BigDecimal.ZERO;
	BigDecimal zN1Amt = BigDecimal.ZERO;
	BigDecimal zN2Amt = BigDecimal.ZERO;
	BigDecimal zN3Amt = BigDecimal.ZERO;
	BigDecimal zN5Amt = BigDecimal.ZERO;
	/*--------------------------------------------*/

	/* 統計表(左上合計)-------------------------------------------- */
	BigDecimal cYToTalAmt = BigDecimal.ZERO;
	BigDecimal cNToTalAmt = BigDecimal.ZERO;
	BigDecimal dNToTalAmt = BigDecimal.ZERO;
	BigDecimal zYToTalAmt = BigDecimal.ZERO;
	BigDecimal zNToTalAmt = BigDecimal.ZERO;
	/*--------------------------------------------*/

	/* 統計表右上(合計)-------------------------------------------- */
	BigDecimal cYloseToTalAmt = BigDecimal.ZERO;
	BigDecimal cNloseToTalAmt = BigDecimal.ZERO;
	BigDecimal dNloseToTalAmt = BigDecimal.ZERO;
	BigDecimal zYloseToTalAmt = BigDecimal.ZERO;
	BigDecimal zNloseToTalAmt = BigDecimal.ZERO;
	/*--------------------------------------------*/

	/* 明細表-------------------------------------------- */
	BigDecimal cN1Total = BigDecimal.ZERO;
	BigDecimal cN2Total = BigDecimal.ZERO;
	BigDecimal cN3Total = BigDecimal.ZERO;
	BigDecimal cN4Total = BigDecimal.ZERO;
	BigDecimal cNTotalAmt = BigDecimal.ZERO;
	BigDecimal cY1Total = BigDecimal.ZERO;
	BigDecimal cY2Total = BigDecimal.ZERO;
	BigDecimal cYTotalAmt = BigDecimal.ZERO;
	BigDecimal cTotalAmt = BigDecimal.ZERO;
	BigDecimal zN1Total = BigDecimal.ZERO;
	BigDecimal zN2Total = BigDecimal.ZERO;
	BigDecimal zN3Total = BigDecimal.ZERO;
	BigDecimal zN4Total = BigDecimal.ZERO;
	BigDecimal zNTotalAmt = BigDecimal.ZERO;
	BigDecimal zY1Total = BigDecimal.ZERO;
	BigDecimal zY2Total = BigDecimal.ZERO;
	BigDecimal zYTotalAmt = BigDecimal.ZERO;
	BigDecimal zTotalAmt = BigDecimal.ZERO;
	BigDecimal allTotalAmt = BigDecimal.ZERO;
	/*--------------------------------------------*/

	@Override
	public void printTitle() {

	}

	/**
	 * 執行報表產出
	 * 
	 * @param titaVo
	 * @param lastYMD 上月底日
	 * @param thisYMD 當月底日
	 */
	public boolean exec(TitaVo titaVo, int lastYMD, int thisYMD) throws LogicException {
		this.info("LM042Report.exportExcel");

		makeExcel.open(titaVo, titaVo.getEntDyI(), titaVo.getKinbr(), "LM042", "RBC表_會計部", "LM042-RBC表_會計部",
				"LM042_底稿_RBC表_會計部_共三份.xlsx", "統計數");

//		Object test = makeExcel.getValue(7, 3);
//		this.info("LM042 get value" + test.toString());
//		iEntdy = Integer.valueOf(titaVo.get("ENTDY")) + 19110000;
		iYear = thisYMD / 10000;
		iMonth = (thisYMD / 100) % 100;
//		int yearMonth = this.parse.stringToInteger(titaVo.getParam("YearMonth")) + 191100;

//		this.info("yearMonth=" + yearMonth);
		// 工作表:統計表
		exportFindStatistics(titaVo, thisYMD / 100);
		// 工作表:明細表
		exportLoanSchedule(titaVo, thisYMD);
		// 工作表:RBC
		exportRBC(titaVo, lastYMD, thisYMD);
		makeExcel.close();

		return true;
	}

	/**
	 * 輸出表單"統計數"
	 * 
	 * @param titaVo
	 * @param yearMonth   西元年月
	 */
	private void exportFindStatistics(TitaVo titaVo, int yearMonth) throws LogicException {

		List<Map<String, String>> statisticsList1 = null;
		List<Map<String, String>> statisticsList2 = null;
		List<Map<String, String>> statisticsList3 = null;

		try {

			statisticsList1 = lM042ServiceImpl.findStatistics1(titaVo, yearMonth);
			statisticsList2 = lM042ServiceImpl.findStatistics2(titaVo, yearMonth);
			statisticsList3 = lM042ServiceImpl.findStatistics3(titaVo, yearMonth);

		} catch (Exception e) {

			// TODO Auto-generated catch block
			StringWriter errors = new StringWriter();
			e.printStackTrace(new PrintWriter(errors));
			this.info("LM042ServiceImpl.exportExcel error = " + errors.toString());

		}

		if (statisticsList1.size() == 0 && statisticsList2.size() == 0 && statisticsList3.size() == 0) {
			return;
		}

		if (statisticsList1.size() > 0 && statisticsList2.size() > 0 && statisticsList3.size() > 0) {

			int row = 0;
			int col = 0;

			for (Map<String, String> lm42Vo1 : statisticsList1) {
				int assetClass = Integer.valueOf(lm42Vo1.get("F1"));
				String kind = lm42Vo1.get("F2");
				String rptId = lm42Vo1.get("F3");
				BigDecimal amt = getBigDecimal(lm42Vo1.get("F4"));

				// 擔保品C 利關人 資產分類1
				if ("C".equals(kind) && "Y".equals(rptId) && assetClass == 1) {
					row = 21;
					col = 3;
					cY1Amt = cY1Amt.add(amt);
				}
				// 擔保品C 非利關人 資產分類1
				if ("C".equals(kind) && "N".equals(rptId) && assetClass == 1) {
					row = 22;
					col = 3;
					cN1Amt = cN1Amt.add(amt);
				}
				// 擔保品C 利關人 資產分類2
				if ("C".equals(kind) && "N".equals(rptId) && assetClass == 2) {
					row = 22;
					col = 4;
					cN2Amt = cN2Amt.add(amt);
				}
				// 擔保品C 非利關人 資產分類3
				if ("C".equals(kind) && "N".equals(rptId) && assetClass == 3) {
					row = 22;
					col = 5;
					cN3Amt = cN3Amt.add(amt);
				}
				// 擔保品C 非利關人 資產分類5
				if ("C".equals(kind) && "N".equals(rptId) && assetClass == 5) {
					row = 22;
					col = 6;
					cN5Amt = cN5Amt.add(amt);
				}
				// 擔保品D 非利關人 資產分類1
				if ("D".equals(kind) && "N".equals(rptId) && assetClass == 1) {
					row = 23;
					col = 3;
					dN1Amt = dN1Amt.add(amt);
				}
				// 擔保品Z 利關人 資產分類1
				if ("Z".equals(kind) && "Y".equals(rptId) && assetClass == 1) {
					row = 24;
					col = 3;
					zY1Amt = zY1Amt.add(amt);
				}
				// 擔保品Z 非利關人 資產分類1
				if ("Z".equals(kind) && "N".equals(rptId) && assetClass == 1) {
					row = 25;
					col = 3;
					zN1Amt = zN1Amt.add(amt);
				}
				// 擔保品Z 非利關人 資產分類2
				if ("Z".equals(kind) && "N".equals(rptId) && assetClass == 2) {
					row = 25;
					col = 4;
					zN2Amt = zN2Amt.add(amt);
				}
				// 擔保品Z 非利關人 資產分類3
				if ("Z".equals(kind) && "N".equals(rptId) && assetClass == 3) {
					row = 25;
					col = 4;
					zN3Amt = zN3Amt.add(amt);
				}
				// 擔保品Z 非利關人 資產分類5
				if ("Z".equals(kind) && "N".equals(rptId) && assetClass == 5) {
					row = 25;
					col = 7;
					zN5Amt = zN5Amt.add(amt);
				}
				makeExcel.setValue(row, col, amt, "#,##0");

			}

			for (int i = 3; i <= 6; i++) {
				makeExcel.formulaCaculate(27, i);
			}

			for (int i = 21; i <= 27; i++) {
				makeExcel.formulaCaculate(i, 8);
			}

			BigDecimal sDisPreRemFees = BigDecimal.ZERO;
			BigDecimal sIntRecv = BigDecimal.ZERO;
			BigDecimal sProLoan = BigDecimal.ZERO;
			BigDecimal sProDiff = BigDecimal.ZERO;
			BigDecimal sStakeholder = BigDecimal.ZERO;

			for (Map<String, String> lm42Vo2 : statisticsList2) {
				String item = lm42Vo2.get("F0");
				BigDecimal amt = getBigDecimal(lm42Vo2.get("F1"));

				// 各金額項目
				// 折溢價及催收費用
				if ("DisPreRemFees".equals(item)) {
					row = 12;
					col = 2;
					sDisPreRemFees = sDisPreRemFees.add(amt);
				}
				// 應收利息
				if ("IntRecv".equals(item)) {
					row = 13;
					sIntRecv = sIntRecv.add(amt);
				}
				// 專案貸款
				if ("ProLoan".equals(item)) {
					row = 14;
					sProLoan = sProLoan.add(amt);

				}
				// 專案差異(待修正)
				if ("ProDiff".equals(item)) {
					row = 15;
					cN1Amt = cN1Amt.subtract(amt);
					sProDiff = sProDiff.add(amt);
				}
				// 利關人_職員數
				if ("Stakeholder".equals(item)) {
					row = 16;
					cY1Amt = cY1Amt.add(amt);
					cN1Amt = cN1Amt.subtract(amt);
					sStakeholder = sStakeholder.add(amt);

				}

				col = 3;

				makeExcel.setValue(row, col, amt, "#,##0");

//				for (int x = 3; x <= 16; x++) {
//					for (int y = 5; y <= 16; y++) {
//						makeExcel.formulaCaculate(y, x);
//					}
//				}

			}

			// 統計數 C5
			makeExcel.setValue(5, 3, cY1Amt, "#,##0");
			// 統計數 C6
			makeExcel.setValue(6, 3, cN1Amt, "#,##0");
			// 統計數 D6
			makeExcel.setValue(6, 4, cN2Amt, "#,##0");
			// 統計數 E6
			makeExcel.setValue(6, 5, cN3Amt, "#,##0");
			// 統計數 G6
			makeExcel.setValue(6, 7, cN5Amt, "#,##0");

			// 統計數 C7
			makeExcel.setValue(7, 3, dN1Amt, "#,##0");
			// 統計數 C8
			makeExcel.setValue(8, 3, zY1Amt, "#,##0");
			// 統計數 C9
			makeExcel.setValue(9, 3, zN1Amt, "#,##0");
			// 統計數 D9
			makeExcel.setValue(9, 4, zN2Amt, "#,##0");
			// 統計數 E9
			makeExcel.setValue(9, 5, zN3Amt, "#,##0");
			// 統計數 G9
			makeExcel.setValue(9, 5, zN3Amt, "#,##0");

			// 統計數I5
			cYToTalAmt = cYToTalAmt.add(cY1Amt);
			makeExcel.setValue(5, 9, cYToTalAmt, "#,##0");
			// 統計數I6
			cNToTalAmt = cNToTalAmt.add(cN1Amt).add(cN2Amt).add(cN3Amt).add(cN5Amt).add(sDisPreRemFees);
			makeExcel.setValue(6, 9, cNToTalAmt, "#,##0");
			// 統計數I7
			dNToTalAmt = dNToTalAmt.add(dN1Amt);
			makeExcel.setValue(7, 9, dNToTalAmt, "#,##0");
			// 統計數I8
			zYToTalAmt = zYToTalAmt.add(zY1Amt);
			makeExcel.setValue(8, 9, zYToTalAmt, "#,##0");
			// 統計數I9
			zNToTalAmt = zNToTalAmt.add(zN1Amt).add(zN2Amt).add(zN3Amt).add(zN5Amt);
			makeExcel.setValue(9, 9, zYToTalAmt, "#,##0");

			// 重整合計公式C11~I11
			for (int i = 3; i <= 9; i++) {
				makeExcel.formulaCaculate(11, i);
			}

			BigDecimal cHouseAndRepair = BigDecimal.ZERO;
			BigDecimal zHouseAndRepair = BigDecimal.ZERO;

			// P欄位 (購置住宅+修繕貸款)
			for (Map<String, String> lm42Vo3 : statisticsList3) {

				String type = lm42Vo3.get("F0");
				BigDecimal amt = getBigDecimal(lm42Vo3.get("F2"));

				// 擔保品類別
				switch (type) {
				case "C":
					row = 6;
					cHouseAndRepair = cHouseAndRepair.add(amt);
					break;
				case "Z":
					row = 9;
					zHouseAndRepair = zHouseAndRepair.add(amt);
					break;
				}

				col = 11;

				makeExcel.setValue(row, col, amt, "#,##0");

			}

			BigDecimal percent0005 = new BigDecimal("0.005");
			BigDecimal percent0015 = new BigDecimal("0.015");
			BigDecimal percent0020 = new BigDecimal("0.02");
			BigDecimal percent0100 = new BigDecimal("0.01");
			BigDecimal percent1000 = new BigDecimal("1");

			BigDecimal cY1loseToTalAmt = BigDecimal.ZERO;
			BigDecimal cN1loseToTalAmt = BigDecimal.ZERO;
			BigDecimal dN1loseToTalAmt = BigDecimal.ZERO;
			BigDecimal zY1loseToTalAmt = BigDecimal.ZERO;
			BigDecimal zN1loseToTalAmt = BigDecimal.ZERO;
			BigDecimal cN2loseToTalAmt = BigDecimal.ZERO;
			BigDecimal zN2loseToTalAmt = BigDecimal.ZERO;
			BigDecimal cY3loseToTalAmt = BigDecimal.ZERO;
			BigDecimal cN5loseToTalAmt = BigDecimal.ZERO;
			BigDecimal zN5loseToTalAmt = BigDecimal.ZERO;

			cY1loseToTalAmt = cY1Amt.multiply(percent0005).setScale(0, BigDecimal.ROUND_HALF_UP);
			makeExcel.setValue(5, 10, cY1loseToTalAmt, "#,##0");

			cN1loseToTalAmt = cY1Amt.subtract(cHouseAndRepair).add(sDisPreRemFees);
			cN1loseToTalAmt = cN1loseToTalAmt.multiply(percent0005).setScale(0, BigDecimal.ROUND_HALF_UP);
			cN1loseToTalAmt = cN1loseToTalAmt
					.add(cHouseAndRepair.multiply(percent0015).setScale(0, BigDecimal.ROUND_HALF_UP));
			makeExcel.setValue(6, 10, cN1loseToTalAmt, "#,##0");

			dN1loseToTalAmt = dN1Amt.multiply(percent0005).setScale(0, BigDecimal.ROUND_HALF_UP);
			makeExcel.setValue(7, 10, dN1loseToTalAmt, "#,##0");

			zY1loseToTalAmt = zY1Amt.multiply(percent0005).setScale(0, BigDecimal.ROUND_HALF_UP);
			makeExcel.setValue(8, 10, zY1loseToTalAmt, "#,##0");

			zN1loseToTalAmt = zN1Amt.multiply(percent0005).setScale(0, BigDecimal.ROUND_HALF_UP);
			makeExcel.setValue(9, 10, zN1loseToTalAmt, "#,##0");

			cN2loseToTalAmt = cN2Amt.add(sIntRecv);
			cN2loseToTalAmt = cN2loseToTalAmt.multiply(percent0020).setScale(0, BigDecimal.ROUND_HALF_UP);
			cN2loseToTalAmt = cN2loseToTalAmt.add(new BigDecimal("0.4")).setScale(0, BigDecimal.ROUND_HALF_UP);
			makeExcel.setValue(6, 11, cN2loseToTalAmt, "#,##0");

			zN2loseToTalAmt = zN2Amt.multiply(percent0020).setScale(0, BigDecimal.ROUND_HALF_UP);
			makeExcel.setValue(9, 11, zN2loseToTalAmt, "#,##0");

			cY3loseToTalAmt = cN3Amt.multiply(percent0100).setScale(0, BigDecimal.ROUND_HALF_UP);
			makeExcel.setValue(9, 11, cY3loseToTalAmt, "#,##0");

			cN5loseToTalAmt = cN5Amt.multiply(percent1000).setScale(0, BigDecimal.ROUND_HALF_UP);
			makeExcel.setValue(9, 14, cN5loseToTalAmt, "#,##0");

			zN5loseToTalAmt = zN5Amt.multiply(percent1000).setScale(0, BigDecimal.ROUND_HALF_UP);
			makeExcel.setValue(9, 14, zN5loseToTalAmt, "#,##0");

			cYloseToTalAmt = cY1loseToTalAmt;
			makeExcel.setValue(5, 15, cYloseToTalAmt, "#,##0");
			cNloseToTalAmt = cN1loseToTalAmt.add(cN2loseToTalAmt).add(cN5loseToTalAmt);
			makeExcel.setValue(6, 15, cNloseToTalAmt, "#,##0");
			dNloseToTalAmt = dN1loseToTalAmt;
			makeExcel.setValue(7, 15, dNloseToTalAmt, "#,##0");
			zYloseToTalAmt = zY1loseToTalAmt;
			makeExcel.setValue(8, 15, zYloseToTalAmt, "#,##0");
			zNloseToTalAmt = zN1loseToTalAmt.add(zN2loseToTalAmt).add(zN5loseToTalAmt);
			makeExcel.setValue(9, 15, zNloseToTalAmt, "#,##0");

			// 重整合計公式J11~P11
			for (int i = 10; i <= 16; i++) {
				makeExcel.formulaCaculate(11, i);
			}

		}

	}

	/**
	 * 輸出表單"明細表"
	 * 
	 * @param titaVo
	 * @param tYMD   當月底日
	 */
	private void exportLoanSchedule(TitaVo titaVo, int tYMD) throws LogicException {
		makeExcel.setSheet("明細表");

		makeExcel.setValue(2, 2, this.showRocDate(tYMD, 6), "C");

		// (缺扣除備抵呆帳)
		cN1Total = cNToTalAmt.subtract(cNloseToTalAmt);
		makeExcel.setValue(5, 3, cN1Total, "#,##0");

		cN3Total = dNToTalAmt.subtract(dNloseToTalAmt);
		makeExcel.setValue(7, 3, cN1Total, "#,##0");

		cNTotalAmt = cN1Total.add(cN3Total);
		makeExcel.setValue(9, 3, cNTotalAmt, "#,##0");

		cY2Total = cYToTalAmt.subtract(cYloseToTalAmt);
		makeExcel.setValue(12, 3, cY2Total, "#,##0");

		cYTotalAmt = cY2Total;
		makeExcel.setValue(13, 3, cYTotalAmt, "#,##0");

		cTotalAmt = cNTotalAmt.add(cYTotalAmt);
		makeExcel.setValue(14, 3, cTotalAmt, "#,##0");

		zN1Total = zNToTalAmt.subtract(zNloseToTalAmt);
		makeExcel.setValue(17, 3, zN1Total, "#,##0");

		zNTotalAmt = zN1Total;
		makeExcel.setValue(21, 3, zNTotalAmt, "#,##0");

		zY2Total = zYToTalAmt.subtract(zYloseToTalAmt);
		makeExcel.setValue(24, 3, zY2Total, "#,##0");

		zYTotalAmt = zY2Total;
		makeExcel.setValue(25, 3, zYTotalAmt, "#,##0");

		zTotalAmt = zNTotalAmt.add(zYTotalAmt);
		makeExcel.setValue(26, 3, zYTotalAmt, "#,##0");

		allTotalAmt = cTotalAmt.add(zTotalAmt);
		makeExcel.setValue(27, 3, allTotalAmt, "#,##0");

		BigDecimal loss = BigDecimal.ZERO;
		makeExcel.setValue(28, 1, "註：各類放款總餘額(含催收款)已扣除備抵呆帳(" + loss + ")。");

		// 更新金額
		updateData(titaVo, iYear * 100 + iMonth);

	}

	/**
	 * 輸出表單"RBC"
	 * 
	 * @param titaVo
	 * @param lYMD   上月底日
	 * @param tYMD   當月底日
	 */
	private void exportRBC(TitaVo titaVo, int lYMD, int tYMD) throws LogicException {

		makeExcel.setSheet("YYYMMRBC", (tYMD / 100) + "RBC");

		// 20200530
		// 去年底
		int leYMD = ((tYMD / 10000) - 1) * 10000 + 1231;

		makeExcel.setValue(2, 1, this.showRocDate(tYMD, 1), "C");
		makeExcel.setValue(4, 4, this.showRocDate(leYMD, 6), "C");
		makeExcel.setValue(4, 6, this.showRocDate(lYMD, 6), "C");
		makeExcel.setValue(4, 8, this.showRocDate(tYMD, 6), "C");

		List<Map<String, String>> leYMlm042RBCList = null;
		List<Map<String, String>> lYMlm042RBCList = null;
		List<Map<String, String>> tYMlm042RBCList = null;

		try {

			leYMlm042RBCList = lM042ServiceImpl.findRBC(titaVo, tYMD, lYMD);
			lYMlm042RBCList = lM042ServiceImpl.findRBC(titaVo, tYMD, lYMD);
			tYMlm042RBCList = lM042ServiceImpl.findRBC(titaVo, tYMD, lYMD);

		} catch (Exception e) {
			// TODO Auto-generated catch block
			StringWriter errors = new StringWriter();
			e.printStackTrace(new PrintWriter(errors));
			this.info("LM042ServiceImpl.exportExcel error = " + errors.toString());

		}

		if (leYMlm042RBCList.size() == 0 && lYMlm042RBCList.size() == 0 && tYMlm042RBCList.size() == 0) {
			return;
		}

		if (leYMlm042RBCList.size() > 0 || lYMlm042RBCList.size() > 0 || tYMlm042RBCList.size() > 0) {

			dataProcess(leYMlm042RBCList, 0);
			dataProcess(lYMlm042RBCList, 1);
			dataProcess(tYMlm042RBCList, 2);

		}

		BigDecimal loss = BigDecimal.ZERO;
		makeExcel.setValue(26, 1, "註1：各類放款總餘額(含催收款)已扣除備抵損失 （ " + loss + "）。");

		// 重整公式
		for (int x = 4; x <= 9; x++) {
			makeExcel.formulaCaculate(11, x);
			makeExcel.formulaCaculate(15, x);
			makeExcel.formulaCaculate(16, x);
			makeExcel.formulaCaculate(17, x);
			makeExcel.formulaCaculate(20, x);
			makeExcel.formulaCaculate(24, x);
			makeExcel.formulaCaculate(25, x);
		}
		// 重整公式
		for (int y = 7; y <= 25; y++) {
			makeExcel.formulaCaculate(y, 10);
			makeExcel.formulaCaculate(y, 11);
		}

	}

	/**
	 * 工作表 RBC資料處理
	 * 
	 * @param lm042RBCList 月份資料
	 * @param colNo        欄位間隔控制
	 */
	private void dataProcess(List<Map<String, String>> lm042RBCList, int colNo) throws LogicException {

		String loanType = "";
		String loanItem = "";
		String RelatedCode = "";
		BigDecimal amt = BigDecimal.ZERO;
		BigDecimal risk = BigDecimal.ZERO;
		BigDecimal riskAmt = BigDecimal.ZERO;

		int row = 2 * colNo;
		this.info("lm042RBCList=" + lm042RBCList.toString());
		for (Map<String, String> lm42RBCVo : lm042RBCList) {

			loanType = lm42RBCVo.get("F1");
			loanItem = lm42RBCVo.get("F2");
			RelatedCode = lm42RBCVo.get("F3");
			amt = new BigDecimal(lm42RBCVo.get("F4"));
			risk = new BigDecimal(lm42RBCVo.get("F5"));
			riskAmt = amt.multiply(risk).setScale(0, BigDecimal.ROUND_FLOOR);

			// 一般
			if ("1".equals(loanType)) {
				// 非授信限制對象
				if ("N".equals(RelatedCode)) {
					// 不動產
					if ("C".equals(loanItem)) {
						makeExcel.setValue(7, 3, risk, "0.0000");
						makeExcel.setValue(7, 4 + row, amt, "#,##0");
						makeExcel.setValue(7, 5 + row, riskAmt, "#,##0");
					}
					// 動產
					if ("B".equals(loanItem)) {
						makeExcel.setValue(8, 3, risk, "0.0000");
						makeExcel.setValue(8, 4 + row, amt, "#,##0");
						makeExcel.setValue(8, 5 + row, riskAmt, "#,##0");
					}
					// 有價證券
					if ("D".equals(loanItem)) {
						makeExcel.setValue(9, 3, risk, "0.0000");
						makeExcel.setValue(9, 4 + row, amt, "#,##0");
						makeExcel.setValue(9, 5 + row, riskAmt, "#,##0");

					}
					// 銀行
					if ("A".equals(loanItem)) {
						makeExcel.setValue(10, 3, risk, "0.0000");
						makeExcel.setValue(10, 4 + row, amt, "#,##0");
						makeExcel.setValue(10, 5 + row, riskAmt, "#,##0");

					}
				}
				// 授信限制對象
				if ("Y".equals(RelatedCode)) {
					// 具控制
					if ("F".equals(loanItem)) {
						makeExcel.setValue(13, 3, risk, "0.0000");
						makeExcel.setValue(13, 4 + row, amt, "#,##0");
						makeExcel.setValue(13, 5 + row, riskAmt, "#,##0");
					}
					// 非據控制
					if ("E".equals(loanItem)) {
						makeExcel.setValue(14, 3, risk, "0.0000");
						makeExcel.setValue(14, 4 + row, amt, "#,##0");
						makeExcel.setValue(14, 5 + row, riskAmt, "#,##0");
					}
				}

			}
			// 專案
			if ("2".equals(loanType)) {
				// 非授信限制對象
				if ("N".equals(RelatedCode)) {
					// 不動產
					if ("C".equals(loanItem)) {
						makeExcel.setValue(19, 3, risk, "0.0000");
						makeExcel.setValue(19, 4 + row, amt, "#,##0");
						makeExcel.setValue(19, 5 + row, riskAmt, "#,##0");
					}
					// 動產
					if ("B".equals(loanItem)) {
//							makeExcel.setValue(8, 3 + row, risk, "0.0000");
//							makeExcel.setValue(8, 4 + row, amt, "#,##0");
//							makeExcel.setValue(8, 5 + row, riskAmt, "#,##0");
					}
					// 有價證券
					if ("D".equals(loanItem)) {
//							makeExcel.setValue(9, 3 + row, risk, "0.0000");
//							makeExcel.setValue(9, 4 + row, amt, "#,##0");
//							makeExcel.setValue(9, 5 + row, riskAmt, "#,##0");

					}
					// 銀行
					if ("A".equals(loanItem)) {
//							makeExcel.setValue(10, 3 + row, risk, "0.0000");
//							makeExcel.setValue(10, 4 + row, amt, "#,##0");
//							makeExcel.setValue(10, 5 + row, riskAmt, "#,##0");

					}
				}
				// 授信限制對象
				if ("Y".equals(RelatedCode)) {
					// 具控制
					if ("F".equals(loanItem)) {
						makeExcel.setValue(22, 3, risk, "0.0000");
						makeExcel.setValue(22, 4 + row, amt, "#,##0");
						makeExcel.setValue(22, 5 + row, riskAmt, "#,##0");
					}
					// 非據控制
					if ("E".equals(loanItem)) {
						makeExcel.setValue(23, 3, risk, "0.0000");
						makeExcel.setValue(23, 4 + row, amt, "#,##0");
						makeExcel.setValue(23, 5 + row, riskAmt, "#,##0");
					}
				}

			}

		}

	}

	/**
	 * 更新各項目金額
	 * 
	 * @param titaVo
	 * @param yearMonth 年月
	 * 
	 */
	private void updateData(TitaVo titaVo, int yearMonth) {

		try {

			// 1 A A 一般-銀行保證-非利關
			updateAmt(titaVo, yearMonth, "1", "A", "N", cN4Total);
			// 1 B A 一般-動產-非利關
			updateAmt(titaVo, yearMonth, "1", "B", "N", cN2Total);
			// 1 C A 一般-不動產-非利關
			updateAmt(titaVo, yearMonth, "1", "C", "N", cN1Total);
			// 1 D A 一般-有價證券-非利關
			updateAmt(titaVo, yearMonth, "1", "D", "N", cN3Total);
			// 1 N B 一般-無-關係(非控)
			updateAmt(titaVo, yearMonth, "1", "E", "Y", cY2Total);
			// 1 N A 一般-無-關係(具控)
			updateAmt(titaVo, yearMonth, "1", "F", "Y", cY1Total);

			// 2 A A 專案-銀行保證-非利關
			updateAmt(titaVo, yearMonth, "2", "A", "N", zN4Total);
			// 2 B A 專案-動產-非利關
			updateAmt(titaVo, yearMonth, "2", "B", "N", zN2Total);
			// 2 C A 專案-不動產-非利關
			updateAmt(titaVo, yearMonth, "2", "C", "N", zN1Total);
			// 2 D A 專案-有價證券-非利關
			updateAmt(titaVo, yearMonth, "2", "D", "N", zN3Total);
			// 2 N B 專案-無-關係(非控)
			updateAmt(titaVo, yearMonth, "2", "E", "Y", zY1Total);
			// 2 N A 專案-無-關係(具控)
			updateAmt(titaVo, yearMonth, "2", "F", "Y", zY2Total);

		} catch (LogicException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	/**
	 * 更新金額
	 * 
	 * @param titaVo
	 * @param yearMonth   年月
	 * @param loanType    放款類型
	 * @param loanItem    放款項目
	 * @param relatedCode 是否為利害關係人
	 * @param amt         金額
	 */
	private void updateAmt(TitaVo titaVo, int yearMonth, String loanType, String loanItem, String relatedCode,
			BigDecimal amt) throws LogicException {
		MonthlyLM042RBC cMonthlyLM042RBC = new MonthlyLM042RBC();
		MonthlyLM042RBCId cMonthlyLM042RBCId = new MonthlyLM042RBCId();

		cMonthlyLM042RBCId.setYearMonth(yearMonth);
		cMonthlyLM042RBCId.setLoanType(loanType);
		cMonthlyLM042RBCId.setLoanItem(loanItem);
		cMonthlyLM042RBCId.setRelatedCode(relatedCode);

		cMonthlyLM042RBC = sMonthlyLM042RBCService.findById(cMonthlyLM042RBCId, titaVo);

		// 有，進入確認risk
		if (cMonthlyLM042RBC != null) {
			this.info("cMonthlyLM042RBC is not null=" + cMonthlyLM042RBC.toString());

			// 主要是更新risk 不同才更新
			if (!amt.equals(cMonthlyLM042RBC.getLoanAmount())) {

				cMonthlyLM042RBC.setLoanAmount(amt);
				try {
					sMonthlyLM042RBCService.update(cMonthlyLM042RBC, titaVo);
				} catch (DBException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}

	// (Test)
	public void insertData(TitaVo titaVo) {
		MonthlyLM042RBC monthlyLM042RBC = new MonthlyLM042RBC();
		MonthlyLM042RBCId monthlyLM042RBCId = new MonthlyLM042RBCId();

		this.info("YMONTH=" + (iYear * 100 + iMonth));
		this.info("allTotalAmt=" + allTotalAmt);

		// 設 PK值
		monthlyLM042RBCId.setYearMonth(iYear * 100 + iMonth);
		monthlyLM042RBCId.setLoanType("1");
		monthlyLM042RBCId.setLoanItem("C");
		monthlyLM042RBCId.setRelatedCode("A");

		// 把ID 放到一般 設值
		monthlyLM042RBC.setMonthlyLM042RBCId(monthlyLM042RBCId);

		monthlyLM042RBC.setRiskFactor(new BigDecimal("0.0018"));
		monthlyLM042RBC.setLoanAmount(allTotalAmt);
		monthlyLM042RBC
				.setCreateDate(parse.IntegerToSqlDateO(dDateUtil.getNowIntegerForBC(), dDateUtil.getNowIntegerTime()));
		monthlyLM042RBC.setCreateEmpNo("001718");
		monthlyLM042RBC
				.setLastUpdate(parse.IntegerToSqlDateO(dDateUtil.getNowIntegerForBC(), dDateUtil.getNowIntegerTime()));
		monthlyLM042RBC.setLastUpdateEmpNo("001718");

		this.info("monthlyLM042RBC List=" + monthlyLM042RBC.toString());
		// 新增一筆
		try {

			sMonthlyLM042RBCService.insert(monthlyLM042RBC, titaVo);
		} catch (DBException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		this.info("finish");
	}

	// 找要的某一筆資料更新 (Test)
	public void updateData(TitaVo titaVo) {

		MonthlyLM042RBC uMonthlyLM042RBC = new MonthlyLM042RBC();
		MonthlyLM042RBCId monthlyLM042RBCId = new MonthlyLM042RBCId();

		monthlyLM042RBCId.setYearMonth(iYear * 100 + iMonth);
		monthlyLM042RBCId.setLoanType("1");
		monthlyLM042RBCId.setLoanItem("C");
		monthlyLM042RBCId.setRelatedCode("A");

		uMonthlyLM042RBC = sMonthlyLM042RBCService.holdById(monthlyLM042RBCId, titaVo);

		this.info("uMonthlyLM042RBC=" + uMonthlyLM042RBC);

		uMonthlyLM042RBC.setRiskFactor(new BigDecimal("0.0032"));
//		monthlyLM042RBC.setLoanAmount(allTotalAmt);

		try {
			sMonthlyLM042RBCService.update(uMonthlyLM042RBC, titaVo);
		} catch (DBException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		this.info("update finish");

	}

	// (Test)
	public void findData(TitaVo titaVo) {
		MonthlyLM042RBC fMonthlyLM042RBC = new MonthlyLM042RBC();
		MonthlyLM042RBCId fMonthlyLM042RBCId = new MonthlyLM042RBCId();

		fMonthlyLM042RBCId.setYearMonth(iYear * 100 + iMonth);
		fMonthlyLM042RBCId.setLoanType("1");
		fMonthlyLM042RBCId.setLoanItem("C");
		fMonthlyLM042RBCId.setRelatedCode("A");

//		fMonthlyLM042RBC.setMonthlyLM042RBCId(fMonthlyLM042RBCId);

		fMonthlyLM042RBC = sMonthlyLM042RBCService.findById(fMonthlyLM042RBCId, titaVo);

		this.info("LM042RBC=" + fMonthlyLM042RBC.toString());
	}
}
