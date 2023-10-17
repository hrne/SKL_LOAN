package com.st1.itx.trade.LM;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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
import com.st1.itx.db.domain.MonthlyLM052AssetClass;
import com.st1.itx.db.service.MonthlyLM042RBCService;
import com.st1.itx.db.service.MonthlyLM042StatisService;
import com.st1.itx.db.service.MonthlyLM052AssetClassService;
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
	MonthlyLM052AssetClassService sMonthlyLM052AssetClassService;

	@Autowired
	Parse parse;

	public int iEntdy = 0;
	public int iYear = 0;
	public int iMonth = 0;
	public int yearMonth = 0;
	private BigDecimal oApprovedLoss = BigDecimal.ZERO;
	private BigDecimal oProDiff = BigDecimal.ZERO;
	private BigDecimal oApprovedLossDiff = BigDecimal.ZERO;
	private List<MonthlyLM042Statis> lMonthlyLM042Statis = new ArrayList<MonthlyLM042Statis>();
	private List<MonthlyLM042RBC> lMonthlyLM042RBC = new ArrayList<MonthlyLM042RBC>();
	private DecimalFormat df = new DecimalFormat("##,###,###,###,##0");

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
		// Load MonthlyLM042Statis LM042RBC統計數 (update by L7205)
		Slice<MonthlyLM042Statis> slMonthlyLM042Statis = sMonthlyLM042StatisService.findYearMonthAll(yearMonth, 0,
				Integer.MAX_VALUE, titaVo);
		if (slMonthlyLM042Statis == null) {
			throw new LogicException(titaVo, "E0001", "MonthlyLM042Statis LM042RBC統計數"); // 查詢資料不存在
		}
		this.lMonthlyLM042Statis = slMonthlyLM042Statis.getContent();

		// 更新LM042RBC
		updateRBC(titaVo);
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

		Slice<MonthlyLM052AssetClass> slMonthlyLM052AssetClass = sMonthlyLM052AssetClassService
				.findYearMonthAll(yearMonth, 0, Integer.MAX_VALUE, titaVo);
		if (slMonthlyLM052AssetClass == null) {
			throw new LogicException(titaVo, "E0015", "需先執行 L7205-五類資產分類上傳轉檔作業 "); // 檢查錯誤
		}
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
		// 專案貸款
		BigDecimal sProAmt = BigDecimal.ZERO;
		BigDecimal sProLoan = BigDecimal.ZERO;

		if (statisticsList1.size() > 0 && statisticsList2.size() > 0 && statisticsList3.size() > 0) {

			int row = 0;
			int col = 0;

			for (Map<String, String> lm42Vo1 : statisticsList1) {
				int assetClass = Integer.valueOf(lm42Vo1.get("AssetClass"));
				String kind = lm42Vo1.get("KIND");
				String relCd = lm42Vo1.get("RelCd");
				BigDecimal amt = getBigDecimal(lm42Vo1.get("AMT"));

				// 擔保品C 利關人 資產分類1
				if ("C".equals(kind) && "Y".equals(relCd) && assetClass == 1) {
					row = 21;
					col = 3;
				}
				// 擔保品C 非利關人 資產分類1
				if ("C".equals(kind) && "N".equals(relCd) && assetClass == 1) {
					row = 22;
					col = 3;
				}
				// 擔保品C 利關人 資產分類2
				if ("C".equals(kind) && "N".equals(relCd) && assetClass == 2) {
					row = 22;
					col = 4;
				}
				// 擔保品C 非利關人 資產分類3
				if ("C".equals(kind) && "N".equals(relCd) && assetClass == 3) {
					row = 22;
					col = 5;
				}
				// 擔保品C 非利關人 資產分類5
				if ("C".equals(kind) && "N".equals(relCd) && assetClass == 5) {
					row = 22;
					col = 6;
				}
				// 擔保品D 非利關人 資產分類1
				if ("D".equals(kind) && "N".equals(relCd) && assetClass == 1) {
					row = 23;
					col = 3;
				}
				// 擔保品Z 利關人 資產分類1
				if ("Z".equals(kind) && "Y".equals(relCd) && assetClass == 1) {
					row = 24;
					col = 3;
				}
				// 擔保品Z 非利關人 資產分類1
				if ("Z".equals(kind) && "N".equals(relCd) && assetClass == 1) {
					row = 25;
					col = 3;
				}
				// 擔保品Z 非利關人 資產分類2
				if ("Z".equals(kind) && "N".equals(relCd) && assetClass == 2) {
					row = 25;
					col = 4;
				}
				// 擔保品Z 非利關人 資產分類3
				if ("Z".equals(kind) && "N".equals(relCd) && assetClass == 3) {
					row = 25;
					col = 4;
				}
				// 擔保品Z 非利關人 資產分類5
				if ("Z".equals(kind) && "N".equals(relCd) && assetClass == 5) {
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

			for (Map<String, String> lm42Vo2 : statisticsList2) {
				String item = lm42Vo2.get("Item");
				String relCd = lm42Vo2.get("RelCd");
				int assetClass = parse.stringToInteger(lm42Vo2.get("AssetClass"));
				BigDecimal amt = getBigDecimal(lm42Vo2.get("AMT"));
				// 應收利息
				if ("IntRecv".equals(item)) {
					row = 13;
					col = 3;
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
					oApprovedLoss = oApprovedLoss.add(amt);

				}

				// 利關人_職員數
				if ("RelEmp".equals(item.substring(0, 6))) {
					row = 16;
					col = 2 + assetClass;

				}
				// 利關人_金額
				if ("RelAmt".equals(item)) {
					row = 17;
					col = 2 + assetClass;

				}
				// 科子目淨額O14
				if ("NetLoanAmt".equals(item)) {
					row = 14;
					col = 15;
				}

				makeExcel.setValue(row, col, amt, "#,##0");
			}
			// 專案差異
			oProDiff = sProAmt.subtract(sProLoan);
			makeExcel.setValue(15, 3, oProDiff, "#,##0");
			// 1.5% 提存差異 oApprovedLossDiff(會計室備呆減不含應付利息的五分類提存數(MonthlyAssetClass)
			oApprovedLossDiff = oApprovedLoss;
			for (MonthlyLM052AssetClass t : slMonthlyLM052AssetClass.getContent()) {
				if (!"7".equals(t.getAssetClassNo())) {
					oApprovedLossDiff = oApprovedLossDiff.subtract(t.getStorageAmt());
				}
			}
			for (MonthlyLM042Statis t1 : this.lMonthlyLM042Statis) {
				String item = t1.getLoanItem();
				String relCd = t1.getRelatedCode();
				int assetClass = parse.stringToInteger(t1.getAssetClass());
				BigDecimal amt = t1.getLoanBal();
				// 統計數 C
				if ("C".equals(item)) {
					if ("Y".equals(relCd)) {
						row = 5;
					} else {
						row = 6;
					}
					col = 2 + assetClass;
				}
				if ("D".equals(item)) {
					row = 7;
					col = 2 + assetClass;
				}
				if ("Z".equals(item)) {
					if ("Y".equals(relCd)) {
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
				}
				// 折溢價與費用 備抵損失I
				if ("6".equals(item)) {
					row = 10;
					col = 9 + assetClass;
					makeExcel.setValue(row, col, amt, "#,##0");
				}

				makeExcel.setValue(row, col, amt, "#,##0");
			}

			this.info("===============================");

			// 重整合計公式C11~I11
			for (int i = 3; i <= 9; i++) {
				makeExcel.formulaCaculate(11, i);
			}

			BigDecimal cHouseAndRepair = BigDecimal.ZERO;
			BigDecimal zHouseAndRepair = BigDecimal.ZERO;

			// P欄位 (購置住宅+修繕貸款)
			for (Map<String, String> lm42Vo3 : statisticsList3) {

				String type = lm42Vo3.get("TYPE");
				BigDecimal amt = getBigDecimal(lm42Vo3.get("AMT"));

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

			for (MonthlyLM042Statis t3 : lMonthlyLM042Statis) {
				int assetClass = parse.stringToInteger(t3.getAssetClass());
				String item = t3.getLoanItem();
				String relCd = t3.getRelatedCode();
				BigDecimal lossAmt = t3.getReserveLossAmt();
				switch (item) {
				case "C":
					if ("Y".equals(relCd)) {
						row = 5;
					} else {
						row = 6;
					}
					break;

				case "D":
					row = 7;
					break;
				case "Z":
					if ("Y".equals(relCd)) {
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

				makeExcel.setValue(row, col, lossAmt, "#,##0");
				
				// P欄位 (購置住宅+修繕貸款)
				if (t3.getHouseAndRepairBal().compareTo(BigDecimal.ZERO) != 0) {
					makeExcel.setValue(row, 16, t3.getHouseAndRepairBal(), "#,##0");
				}
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

		makeExcel.setSheet("明細表");

		makeExcel.setValue(2, 2, this.showRocDate(tYMD, 6), "C");
		int row = 5;
		for (MonthlyLM042RBC t : lMonthlyLM042RBC) {
			switch (t.getLoanType()) {
			case "1":
				switch (t.getLoanItem()) {
				case "C":
					row = 5;
				case "B":
					row = 6;
				case "D":
					row = 7;
				case "A":
					row = 8;
				case "E":
					row = 11;
				case "F":
					row = 12;
				}
				break;
			case "2":
				switch (t.getLoanItem()) {
				case "C":
					row = 17;
				case "B":
					row = 18;
				case "D":
					row = 19;
				case "A":
					row = 20;
				case "E":
					row = 23;
				case "F":
					row = 24;
				}
				break;
			}
			makeExcel.setValue(row, 3, t.getLoanAmount(), "#,##0");
		}

		makeExcel.formulaCalculate(9, 3);
		makeExcel.formulaCalculate(13, 3);
		makeExcel.formulaCalculate(14, 3);
		makeExcel.formulaCalculate(21, 3);
		makeExcel.formulaCalculate(25, 3);
		makeExcel.formulaCalculate(26, 3);
		makeExcel.formulaCalculate(27, 3);

		makeExcel.setValue(28, 1, "註：各類放款總餘額(含催收款)已扣除備抵呆帳(" + df.format(oApprovedLoss) + ")。");

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

		makeExcel.setValue(26, 1, "註1：各類放款總餘額(含催收款)已扣除備抵損失 （ " + df.format(oApprovedLoss) + "）。");

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
		BigDecimal amt = BigDecimal.ZERO;
		BigDecimal risk = BigDecimal.ZERO;
		BigDecimal riskAmt = BigDecimal.ZERO;

		int row = 2 * colNo;
		this.info("lm042RBCList=" + lm042RBCList.toString());
		for (Map<String, String> lm42RBCVo : lm042RBCList) {

			loanType = lm42RBCVo.get("LoanType");
			loanItem = lm42RBCVo.get("LoanItem");
			amt = new BigDecimal(lm42RBCVo.get("LoanAmount"));
			risk = new BigDecimal(lm42RBCVo.get("RiskFactor"));
			riskAmt = new BigDecimal(lm42RBCVo.get("RiskFactorAmount"));

			// 一般
			if ("1".equals(loanType)) {
				// 非授信限制對象
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
				// 具控制
				if ("E".equals(loanItem)) {
					makeExcel.setValue(13, 3, risk, "0.0000");
					makeExcel.setValue(13, 4 + row, amt, "#,##0");
					makeExcel.setValue(13, 5 + row, riskAmt, "#,##0");
				}
				// 非據控制
				if ("F".equals(loanItem)) {
					makeExcel.setValue(14, 3, risk, "0.0000");
					makeExcel.setValue(14, 4 + row, amt, "#,##0");
					makeExcel.setValue(14, 5 + row, riskAmt, "#,##0");
				}
			}
			// 專案
			if ("2".equals(loanType)) {
				// 非授信限制對象
				// 不動產
				if ("C".equals(loanItem)) {
					makeExcel.setValue(19, 3, risk, "0.0000");
					makeExcel.setValue(19, 4 + row, amt, "#,##0");
					makeExcel.setValue(19, 5 + row, riskAmt, "#,##0");
				}
				// 具控制
				if ("E".equals(loanItem)) {
					makeExcel.setValue(22, 3, risk, "0.0000");
					makeExcel.setValue(22, 4 + row, amt, "#,##0");
					makeExcel.setValue(22, 5 + row, riskAmt, "#,##0");
				}
				// 非據控制
				if ("F".equals(loanItem)) {
					makeExcel.setValue(23, 3, risk, "0.0000");
					makeExcel.setValue(23, 4 + row, amt, "#,##0");
					makeExcel.setValue(23, 5 + row, riskAmt, "#,##0");
				}
			}

		}

	}

	private void updateRBC(TitaVo titaVo) throws LogicException {
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
		for (MonthlyLM042Statis t : lMonthlyLM042Statis) {
			MonthlyLM042RBC cMonthlyLM042RBC = new MonthlyLM042RBC();
			cMonthlyLM042RBC.setYearMonth(t.getYearMonth());
			// LoanType=1,LoanItem=C 含折溢價及費用
			if ("6".equals(t.getLoanItem())) {
				cMonthlyLM042RBC.setLoanType("1");
				cMonthlyLM042RBC.setLoanItem("C");
			} else {
				if ("Z".equals(t.getLoanItem())) {
					cMonthlyLM042RBC.setLoanType("2");
				} else {
					cMonthlyLM042RBC.setLoanType("1");
				}
				if ("Y".equals(t.getRelatedCode())) {
					cMonthlyLM042RBC.setLoanItem("F");
				} else {
					cMonthlyLM042RBC.setLoanItem(t.getLoanItem());
				}
			}
			// 放款餘額扣除備呆
			cMonthlyLM042RBC.setLoanAmount(t.getNetAmt());
			addRBC(cMonthlyLM042RBC, titaVo);
		}

		// 放款金額*風險係數
		for (MonthlyLM042RBC t : lMonthlyLM042RBC) {
			t.setRiskFactorAmount(t.getLoanAmount().multiply(t.getRiskFactor()).setScale(0, BigDecimal.ROUND_FLOOR));
		}
		try {
			sMonthlyLM042RBCService.insertAll(lMonthlyLM042RBC, titaVo);
		} catch (DBException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public void addStatis(MonthlyLM042Statis t, TitaVo titaVo) {
		Boolean isfind = false;
		for (MonthlyLM042Statis tMonthlyLM042Statis : lMonthlyLM042Statis) {
			if (t.getLoanItem().equals(tMonthlyLM042Statis.getLoanItem())
					&& t.getRelatedCode().equals(tMonthlyLM042Statis.getRelatedCode())
					&& t.getAssetClass().equals(tMonthlyLM042Statis.getAssetClass())) {
				isfind = true;
				tMonthlyLM042Statis.setLoanBal(tMonthlyLM042Statis.getLoanBal().add(t.getLoanBal()));
				tMonthlyLM042Statis
						.setReserveLossAmt(tMonthlyLM042Statis.getReserveLossAmt().add(t.getReserveLossAmt()));
				tMonthlyLM042Statis
						.setReserveLossDiff(tMonthlyLM042Statis.getReserveLossDiff().add(t.getReserveLossDiff()));
			}
		}
		if (!isfind) {
			MonthlyLM042StatisId monthlyLM042StatisId = new MonthlyLM042StatisId();
			monthlyLM042StatisId.setYearMonth(t.getYearMonth());
			monthlyLM042StatisId.setLoanItem(t.getLoanItem());
			monthlyLM042StatisId.setRelatedCode(t.getRelatedCode());
			monthlyLM042StatisId.setAssetClass(t.getAssetClass());
			t.setMonthlyLM042StatisId(monthlyLM042StatisId);
			lMonthlyLM042Statis.add(t);
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
