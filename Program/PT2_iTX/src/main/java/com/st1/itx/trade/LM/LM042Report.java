package com.st1.itx.trade.LM;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Component;

import com.st1.itx.Exception.DBException;
import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.db.domain.MonthlyLM042RBC;
import com.st1.itx.db.domain.MonthlyLM042RBCId;
import com.st1.itx.db.domain.MonthlyLM042Statis;
import com.st1.itx.db.domain.MonthlyLM042StatisId;
import com.st1.itx.db.service.MonthlyLM042RBCService;
import com.st1.itx.db.service.MonthlyLM042StatisService;
import com.st1.itx.db.service.springjpa.cm.LM042ServiceImpl;
import com.st1.itx.util.common.MakeExcel;
import com.st1.itx.util.common.MakeReport;
import com.st1.itx.util.common.data.ReportVo;
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
	MonthlyLM042StatisService sMonthlyLM042StatisService;

	@Autowired
	Parse parse;

	public int iEntdy = 0;
	public int iYear = 0;
	public int iMonth = 0;
	public int yearMonth = 0;
	/* 統計數--------------------------------------- */
	HashMap<String, BigDecimal> loanBal = new HashMap<>();
	HashMap<String, BigDecimal> storeAmt = new HashMap<>();
	/*--------------------------------------------*/

	/* 統計表(左上合計)-------------------------------------------- */
	BigDecimal cYToTalAmt = BigDecimal.ZERO;
	BigDecimal cNToTalAmt = BigDecimal.ZERO;
	BigDecimal dNToTalAmt = BigDecimal.ZERO;
	BigDecimal zYToTalAmt = BigDecimal.ZERO;
	BigDecimal zNToTalAmt = BigDecimal.ZERO;
	BigDecimal i10ToTalAmt = BigDecimal.ZERO;
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
	BigDecimal oBadDebt = BigDecimal.ZERO;

	private ArrayList<MonthlyLM042Statis> lMonthlyLM042Statis = new ArrayList<>();
	private ArrayList<MonthlyLM042RBC> lMonthlyLM042RBC = new ArrayList<>();

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
	 * @return
	 * @throws LogicException
	 */
	public boolean exec(TitaVo titaVo, int lastYMD, int thisYMD) throws LogicException {
		this.info("LM042Report.exportExcel");

		int reportDate = titaVo.getEntDyI() + 19110000;
		String brno = titaVo.getBrno();
		String txcd = "LM042";
		String fileItem = "RBC表_會計部";
		String fileName = "LM042-RBC表_會計部";
		String defaultExcel = "LM042_底稿_RBC表_會計部_共三份.xlsx";
		String defaultSheet = "統計數";

		this.info("reportVo open");

		ReportVo reportVo = ReportVo.builder().setRptDate(reportDate).setBrno(brno).setRptCode(txcd)
				.setRptItem(fileItem).build();
		// 開啟報表
		makeExcel.open(titaVo, reportVo, fileName, defaultExcel, defaultSheet);

//		makeExcel.open(titaVo, titaVo.getEntDyI(), titaVo.getKinbr(), "LM042", "RBC表_會計部", "LM042-RBC表_會計部",
//				"LM042_底稿_RBC表_會計部_共三份.xlsx", "統計數");

		iYear = thisYMD / 10000;
		iMonth = (thisYMD / 100) % 100;
		yearMonth = this.parse.stringToInteger(titaVo.getParam("YearMonth")) + 191100;

//		this.info("yearMonth=" + yearMonth);
		// 工作表:統計表
		exportFindStatistics(titaVo, thisYMD / 100);

		// 更新LM042RBC統計數
		updateStatis(titaVo, iYear * 100 + iMonth);
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
	 * @param yearMonth 西元年月
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
				}
				// 擔保品C 非利關人 資產分類1
				if ("C".equals(kind) && "N".equals(rptId) && assetClass == 1) {
					row = 22;
					col = 3;
				}
				// 擔保品C 利關人 資產分類2
				if ("C".equals(kind) && "N".equals(rptId) && assetClass == 2) {
					row = 22;
					col = 4;
				}
				// 擔保品C 非利關人 資產分類3
				if ("C".equals(kind) && "N".equals(rptId) && assetClass == 3) {
					row = 22;
					col = 5;
				}
				// 擔保品C 非利關人 資產分類5
				if ("C".equals(kind) && "N".equals(rptId) && assetClass == 5) {
					row = 22;
					col = 6;
				}
				// 擔保品D 非利關人 資產分類1
				if ("D".equals(kind) && "N".equals(rptId) && assetClass == 1) {
					row = 23;
					col = 3;
				}
				// 擔保品Z 利關人 資產分類1
				if ("Z".equals(kind) && "Y".equals(rptId) && assetClass == 1) {
					row = 24;
					col = 3;
				}
				// 擔保品Z 非利關人 資產分類1
				if ("Z".equals(kind) && "N".equals(rptId) && assetClass == 1) {
					row = 25;
					col = 3;
				}
				// 擔保品Z 非利關人 資產分類2
				if ("Z".equals(kind) && "N".equals(rptId) && assetClass == 2) {
					row = 25;
					col = 4;
				}
				// 擔保品Z 非利關人 資產分類3
				if ("Z".equals(kind) && "N".equals(rptId) && assetClass == 3) {
					row = 25;
					col = 4;
				}
				// 擔保品Z 非利關人 資產分類5
				if ("Z".equals(kind) && "N".equals(rptId) && assetClass == 5) {
					row = 25;
					col = 7;
				}

				makeExcel.setValue(row, col, amt, "#,##0");

			}

			// R=27 C=3~6
			for (int i = 3; i <= 6; i++) {
				makeExcel.formulaCaculate(27, i);
			}
			// R=21~27 C=8
			for (int i = 21; i <= 27; i++) {
				makeExcel.formulaCaculate(i, 8);
			}

			BigDecimal tDisPreRemFees = BigDecimal.ZERO;
			BigDecimal oDisPreRemFees = BigDecimal.ZERO;
			BigDecimal sIntRecv = BigDecimal.ZERO;
			BigDecimal sProLoan = BigDecimal.ZERO;
			BigDecimal sTotalLoanAmt = BigDecimal.ZERO;
			BigDecimal sApprovedLoss = BigDecimal.ZERO;
			BigDecimal sProDiff = BigDecimal.ZERO;
			BigDecimal sProAmt = BigDecimal.ZERO;
			BigDecimal sRelEmpAmt = BigDecimal.ZERO;
			BigDecimal sRelAmt = BigDecimal.ZERO;

			for (Map<String, String> lm42Vo2 : statisticsList2) {
				String item = lm42Vo2.get("F0");
				String rptId = lm42Vo2.get("F1");
				int assetClass = parse.stringToInteger(lm42Vo2.get("F2"));
				BigDecimal amt = getBigDecimal(lm42Vo2.get("F3"));
				if (item.length() == 1) {
					loanBal.put(item + rptId + assetClass, amt);
				}
				// 統計數 C
				if ("C".equals(item)) {
					if ("Y".equals(rptId)) {
						row = 5;
					} else {
						row = 6;
					}
					col = 2 + assetClass;
				}
				// D
				if ("D".equals(item)) {
					row = 7;
					col = 2 + assetClass;
				}
				// Z
				if ("Z".equals(item)) {
					if ("Y".equals(rptId)) {
						row = 8;
					} else {
						row = 9;
					}
					col = 2 + assetClass;
				}
				// 各金額項目
				// 擔保品-折溢價與費用
				if ("6".equals(item)) {
					row = 10;
					col = 2 + assetClass;
					makeExcel.setValue(row, col, amt, "#,##0");

					row = 12;
					col = assetClass == 1 ? 3 : 5;

					tDisPreRemFees = tDisPreRemFees.add(amt);
				}
				// 折溢價與費用 備抵損失I
				if ("6".equals(item)) {

					row = 10;
					col = 9 + assetClass;
					makeExcel.setValue(row, col, amt, "#,##0");
				}
				// 應收利息
				if ("IntRecv".equals(item)) {
					row = 13;
					col = 3;
					sIntRecv = sIntRecv.add(amt);
				}
				// 專案貸款
				if ("ProLoan".equals(item)) {
					row = 14;
					col = 3;
					sProLoan = sProLoan.add(amt);

				}
				// 放款總額 I13
				if ("TotalLoanAmt".equals(item)) {
					row = 13;
					col = 9;
					sTotalLoanAmt = sTotalLoanAmt.add(amt);

				}
				// 服務課專案數字
				if ("ProAmt".equals(item)) {
					row = 14;
					col = 9;
					sProAmt = sProAmt.add(amt);

				}
				// 會計核准備呆 P13
				if ("ApprovedLoss".equals(item)) {
					row = 13;
					col = 16;
					sApprovedLoss = sApprovedLoss.add(amt);

				}

				// 利關人_職員數
				if ("RelEmp".equals(item.substring(0, 6))) {
					row = 16;
					col = 2 + assetClass;
					sRelEmpAmt = sRelEmpAmt.add(amt);

				}
				// 利關人_金額
				if ("RelAmt".equals(item)) {
					row = 17;
					col = 2 + assetClass;
					sRelAmt = sRelAmt.add(amt);

				}
				// 科子目淨額O14
				if ("NetLoanAmt".equals(item)) {
					row = 14;
					col = 15;

				}

				makeExcel.setValue(row, col, amt, "#,##0");

			}

			// 專案差異
			sProDiff = sProAmt.subtract(sProLoan);
			makeExcel.setValue(15, 3, sProDiff, "#,##0");
			loanBal.put("CN1", loanBal.get("CN1").subtract(sProDiff));
			loanBal.put("ZN1", loanBal.get("ZN1").add(sProDiff));

			this.info("===============================");

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
				col = 16;
				makeExcel.setValue(row, col, amt, "#,##0");
			}

			BigDecimal percent0_005 = new BigDecimal("0.005"); // 0.5%
			BigDecimal percent0_015 = new BigDecimal("0.015");// 1.5%
			BigDecimal percent0_02 = new BigDecimal("0.02");// 2%
			BigDecimal percent0_1 = new BigDecimal("0.1");// 10%
			BigDecimal percent0_5 = new BigDecimal("0.5");// 50%
			BigDecimal percent1 = new BigDecimal("1");// 100%

			BigDecimal lossAmt = BigDecimal.ZERO;
			BigDecimal priBal = BigDecimal.ZERO;

			Set<String> loanBalKey = loanBal.keySet();

			for (Iterator<String> loanBalkey = loanBalKey.iterator(); loanBalkey.hasNext();) {
				String key = loanBalkey.next();
				int assetClass = parse.stringToInteger(key.substring(2, 3));
				String item = key.substring(0, 1);
				String rptId = key.substring(1, 2);
				if ("1".equals(assetClass)) {
					priBal = loanBal.get(key);
					if ("CN1".equals(key)) {
						lossAmt = (priBal.subtract(cHouseAndRepair)).multiply(percent0_005)
								.add((cHouseAndRepair).multiply(percent0_015)).setScale(0, BigDecimal.ROUND_HALF_UP);
					} else if ("ZN1".equals(key)) {
						lossAmt = (priBal.subtract(zHouseAndRepair)).multiply(percent0_005)
								.add((zHouseAndRepair).multiply(percent0_015)).setScale(0, BigDecimal.ROUND_HALF_UP);
					} else {
						lossAmt = priBal.multiply(percent0_005).setScale(0, BigDecimal.ROUND_HALF_UP);
					}
				} else {
					switch (assetClass) {
					case 2:
						lossAmt = loanBal.get(key).multiply(percent0_02).setScale(0, BigDecimal.ROUND_HALF_UP);
						break;
					case 3:
						lossAmt = loanBal.get(key).multiply(percent0_1).setScale(0, BigDecimal.ROUND_HALF_UP);
						break;
					case 4:
						lossAmt = loanBal.get(key).multiply(percent0_5).setScale(0, BigDecimal.ROUND_HALF_UP);
						break;
					case 5:
						lossAmt = loanBal.get(key).multiply(percent1).setScale(0, BigDecimal.ROUND_HALF_UP);
						break;
					}
				}
				switch (item) {
				case "C":
					if ("Y".equals(rptId)) {
						row = 5;
					} else {
						row = 6;
					}
					break;

				case "D":
					row = 7;
					break;
				case "Z":
					if ("Y".equals(rptId)) {
						row = 8;
					} else {
						row = 9;
					}
					break;
				default:
					row = 10;
					break;
				}
				col = 9 + assetClass;
				storeAmt.put(key, lossAmt);
				makeExcel.setValue(row, col, lossAmt, "#,##0");
			}

			// 重整合計公式J11~P11
			for (int j = 5; j <= 11; j++) {
				for (int i = 10; i <= 16; i++) {
					makeExcel.formulaCaculate(j, i);
				}
			}
			// 重整合計公式
			makeExcel.formulaCaculate(13, 15);// O13
			makeExcel.formulaCaculate(15, 15);// O15
			makeExcel.formulaRangeCalculate(5, 13, 9, 9);// I5~I13
			makeExcel.formulaRangeCalculate(5, 13, 15, 15);// O5~O13
			makeExcel.formulaRangeCalculate(5, 13, 17, 17);// Q5~Q13

		}

	}

	/**
	 * 輸出表單"明細表"
	 * 
	 * @param titaVo
	 * @param tYMD   當月底日
	 */
	private void exportLoanSchedule(TitaVo titaVo, int tYMD) throws LogicException {
		MonthlyLM042RBC tMonthlyLM042RBC = new MonthlyLM042RBC();
		tMonthlyLM042RBC.setYearMonth(this.yearMonth);
		tMonthlyLM042RBC.setLoanType("1");
		tMonthlyLM042RBC.setLoanItem("A");
		tMonthlyLM042RBC.setRiskFactor(parse.stringToBigDecimal(titaVo.get("RiskFactor1")));
		lMonthlyLM042RBC.add(tMonthlyLM042RBC);
		tMonthlyLM042RBC = new MonthlyLM042RBC();
		tMonthlyLM042RBC.setYearMonth(this.yearMonth);
		tMonthlyLM042RBC.setLoanType("1");
		tMonthlyLM042RBC.setLoanItem("B");
		tMonthlyLM042RBC.setRiskFactor(parse.stringToBigDecimal(titaVo.get("RiskFactor2")));
		lMonthlyLM042RBC.add(tMonthlyLM042RBC);
		tMonthlyLM042RBC = new MonthlyLM042RBC();
		tMonthlyLM042RBC.setYearMonth(this.yearMonth);
		tMonthlyLM042RBC.setLoanType("1");
		tMonthlyLM042RBC.setLoanItem("C");
		tMonthlyLM042RBC.setRiskFactor(parse.stringToBigDecimal(titaVo.get("RiskFactor3")));
		lMonthlyLM042RBC.add(tMonthlyLM042RBC);
		tMonthlyLM042RBC = new MonthlyLM042RBC();
		tMonthlyLM042RBC.setYearMonth(this.yearMonth);
		tMonthlyLM042RBC.setLoanType("1");
		tMonthlyLM042RBC.setLoanItem("D");
		tMonthlyLM042RBC.setRiskFactor(parse.stringToBigDecimal(titaVo.get("RiskFactor4")));
		lMonthlyLM042RBC.add(tMonthlyLM042RBC);
		tMonthlyLM042RBC = new MonthlyLM042RBC();
		tMonthlyLM042RBC.setYearMonth(this.yearMonth);
		tMonthlyLM042RBC.setLoanType("1");
		tMonthlyLM042RBC.setLoanItem("E");
		tMonthlyLM042RBC.setRiskFactor(parse.stringToBigDecimal(titaVo.get("RiskFactor5")));
		lMonthlyLM042RBC.add(tMonthlyLM042RBC);
		tMonthlyLM042RBC = new MonthlyLM042RBC();
		tMonthlyLM042RBC.setYearMonth(this.yearMonth);
		tMonthlyLM042RBC.setLoanType("1");
		tMonthlyLM042RBC.setLoanItem("F");
		tMonthlyLM042RBC.setRiskFactor(parse.stringToBigDecimal(titaVo.get("RiskFactor6")));
		lMonthlyLM042RBC.add(tMonthlyLM042RBC);
		tMonthlyLM042RBC = new MonthlyLM042RBC();
		tMonthlyLM042RBC.setYearMonth(this.yearMonth);
		tMonthlyLM042RBC.setLoanType("2");
		tMonthlyLM042RBC.setLoanItem("A");
		tMonthlyLM042RBC.setRiskFactor(parse.stringToBigDecimal(titaVo.get("RiskFactor1")));
		lMonthlyLM042RBC.add(tMonthlyLM042RBC);
		tMonthlyLM042RBC = new MonthlyLM042RBC();
		tMonthlyLM042RBC.setYearMonth(this.yearMonth);
		tMonthlyLM042RBC.setLoanType("2");
		tMonthlyLM042RBC.setLoanItem("B");
		tMonthlyLM042RBC.setRiskFactor(parse.stringToBigDecimal(titaVo.get("RiskFactor2")));
		lMonthlyLM042RBC.add(tMonthlyLM042RBC);
		tMonthlyLM042RBC = new MonthlyLM042RBC();
		tMonthlyLM042RBC.setYearMonth(this.yearMonth);
		tMonthlyLM042RBC.setLoanType("2");
		tMonthlyLM042RBC.setLoanItem("C");
		tMonthlyLM042RBC.setRiskFactor(parse.stringToBigDecimal(titaVo.get("RiskFactor3")));
		lMonthlyLM042RBC.add(tMonthlyLM042RBC);
		tMonthlyLM042RBC = new MonthlyLM042RBC();
		tMonthlyLM042RBC.setYearMonth(this.yearMonth);
		tMonthlyLM042RBC.setLoanType("2");
		tMonthlyLM042RBC.setLoanItem("D");
		tMonthlyLM042RBC.setRiskFactor(parse.stringToBigDecimal(titaVo.get("RiskFactor4")));
		lMonthlyLM042RBC.add(tMonthlyLM042RBC);
		tMonthlyLM042RBC = new MonthlyLM042RBC();
		tMonthlyLM042RBC.setYearMonth(this.yearMonth);
		tMonthlyLM042RBC.setLoanType("2");
		tMonthlyLM042RBC.setLoanItem("E");
		tMonthlyLM042RBC.setRiskFactor(parse.stringToBigDecimal(titaVo.get("RiskFactor5")));
		lMonthlyLM042RBC.add(tMonthlyLM042RBC);
		tMonthlyLM042RBC = new MonthlyLM042RBC();
		tMonthlyLM042RBC.setYearMonth(this.yearMonth);
		tMonthlyLM042RBC.setLoanType("2");
		tMonthlyLM042RBC.setLoanItem("F");
		tMonthlyLM042RBC.setRiskFactor(parse.stringToBigDecimal(titaVo.get("RiskFactor6")));
		lMonthlyLM042RBC.add(tMonthlyLM042RBC);

		updateRBC(titaVo);

		makeExcel.setSheet("明細表");

		makeExcel.setValue(2, 2, this.showRocDate(tYMD, 6), "C");

		// (缺扣除備抵呆帳)
		// 擔保品C/非利關人總計餘額 + 折溢價與費用 - 擔保品C/非利關人/備抵損失總計 - (備抵呆帳-全擔保品備抵損失) - 折溢價與費用的備抵損失
		cN1Total = cNToTalAmt.subtract(cNloseToTalAmt);
		makeExcel.setValue(5, 3, cN1Total, "#,##0");
		// 擔保品D 總計餘額 - 擔保品D 備抵損失總計
		cN3Total = dNToTalAmt.subtract(dNloseToTalAmt);
		makeExcel.setValue(7, 3, cN3Total, "#,##0");

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

		makeExcel.formulaCalculate(9, 3);
		makeExcel.formulaCalculate(13, 3);
		makeExcel.formulaCalculate(14, 3);
		makeExcel.formulaCalculate(21, 3);
		makeExcel.formulaCalculate(25, 3);
		makeExcel.formulaCalculate(26, 3);
		makeExcel.formulaCalculate(27, 3);

		// 08/29 oBadDebt SUM(TdBal)科目10620300000、10604
		makeExcel.setValue(28, 1, "註：各類放款總餘額(含催收款)已扣除備抵呆帳(" + oBadDebt + ")。");

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

	private void updateStatis(TitaVo titaVo, int yearMonth) throws LogicException {

		Slice<MonthlyLM042Statis> slMonthlyLM042Statis = sMonthlyLM042StatisService.findYearMonthAll(yearMonth, 0,
				Integer.MAX_VALUE, titaVo);
		if (slMonthlyLM042Statis != null) {
			try {
				sMonthlyLM042StatisService.deleteAll(slMonthlyLM042Statis.getContent(), titaVo);
			} catch (DBException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		Set<String> storeAmtKey = storeAmt.keySet();

		for (Iterator<String> storeAmtkey = storeAmtKey.iterator(); storeAmtkey.hasNext();) {
			String key = storeAmtkey.next();
			String item = key.substring(0, 1);
			String rptId = key.substring(1, 2);
			int assetClass = parse.stringToInteger(key.substring(2, 3));
			MonthlyLM042StatisId monthlyLM042StatisId = new MonthlyLM042StatisId();
			MonthlyLM042Statis tMonthlyLM042Statis = new MonthlyLM042Statis();
			monthlyLM042StatisId.setYearMonth(yearMonth);
			monthlyLM042StatisId.setLoanItem(item);
			monthlyLM042StatisId.setRelatedCode(rptId);
			monthlyLM042StatisId.setAssetClass("" + assetClass);
			tMonthlyLM042Statis.setMonthlyLM042StatisId(monthlyLM042StatisId);
			tMonthlyLM042Statis.setYearMonth(yearMonth);
			tMonthlyLM042Statis.setLoanItem(item);
			tMonthlyLM042Statis.setRelatedCode(rptId);
			tMonthlyLM042Statis.setAssetClass("" + assetClass);
			tMonthlyLM042Statis.setLoanBal(loanBal.get(key));
			tMonthlyLM042Statis.setReserveLossAmt(storeAmt.get(key));
			lMonthlyLM042Statis.add(tMonthlyLM042Statis);
		}
		try {
			sMonthlyLM042StatisService.insertAll(lMonthlyLM042Statis, titaVo);
		} catch (DBException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

	}

	private void updateRBC(TitaVo titaVo) throws LogicException {

		for (MonthlyLM042Statis t : lMonthlyLM042Statis) {
			if ("6".equals(t.getLoanItem())) {
				continue;
			}
			MonthlyLM042RBC cMonthlyLM042RBC = new MonthlyLM042RBC();
			cMonthlyLM042RBC.setYearMonth(t.getYearMonth());
			cMonthlyLM042RBC.setLoanType("Z".equals(t.getLoanItem()) ? "2" : "1");
			cMonthlyLM042RBC.setLoanItem("Y".equals(t.getRelatedCode()) ? "E" : t.getLoanItem());
			cMonthlyLM042RBC.setLoanAmount(t.getLoanBal().subtract(t.getReserveLossAmt()));

			addRBC(cMonthlyLM042RBC, titaVo);
		}

		for (MonthlyLM042RBC tMonthlyLM042RBC : lMonthlyLM042RBC) {
			tMonthlyLM042RBC.setRiskFactorAmount(tMonthlyLM042RBC.getLoanAmount()
					.multiply(tMonthlyLM042RBC.getRiskFactor()).setScale(0, BigDecimal.ROUND_FLOOR));
		}
		try {
			sMonthlyLM042RBCService.insertAll(lMonthlyLM042RBC, titaVo);
		} catch (DBException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public void addRBC(MonthlyLM042RBC t, TitaVo titaVo) {
		Boolean isfind = false;
		for (MonthlyLM042RBC tMonthlyLM042RBC : lMonthlyLM042RBC) {
			if (t.getLoanType().equals(tMonthlyLM042RBC.getLoanType())
					&& t.getLoanItem().equals(tMonthlyLM042RBC.getLoanItem())) {
				isfind = true;
				tMonthlyLM042RBC.setLoanAmount(tMonthlyLM042RBC.getLoanAmount().add(t.getLoanAmount()));
			}
		}
		if (!isfind) {
			MonthlyLM042RBCId cMonthlyLM042RBCId = new MonthlyLM042RBCId();
			cMonthlyLM042RBCId.setYearMonth(t.getYearMonth());
			cMonthlyLM042RBCId.setLoanType(t.getLoanType());
			cMonthlyLM042RBCId.setLoanItem(t.getLoanItem());
			t.setMonthlyLM042RBCId(cMonthlyLM042RBCId);
			lMonthlyLM042RBC.add(t);
		}
	}

}
