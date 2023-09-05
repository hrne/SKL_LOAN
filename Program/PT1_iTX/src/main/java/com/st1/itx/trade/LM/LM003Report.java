package com.st1.itx.trade.LM;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.db.service.springjpa.cm.LM003ServiceImpl;
import com.st1.itx.util.common.MakeExcel;
import com.st1.itx.util.common.MakeReport;
import com.st1.itx.util.common.data.ReportVo;
import com.st1.itx.util.parse.Parse;

@Component
@Scope("prototype")
public class LM003Report extends MakeReport {

	@Autowired
	LM003ServiceImpl lM003ServiceImpl;

	@Autowired
	MakeExcel makeExcel;

	@Autowired
	Parse parse;

	public boolean exec(TitaVo titaVo) throws LogicException {
		this.info("LM003Report exec ...");

		List<Map<String, String>> lM003List = new ArrayList<>();

		try {
			lM003List = lM003ServiceImpl.findAll(titaVo);
		} catch (Exception e) {
			StringWriter errors = new StringWriter();
			e.printStackTrace(new PrintWriter(errors));
			this.info("LM003ServiceImpl.findAll error = " + errors.toString());
		}

		exportExcel(lM003List, titaVo);

		return true;

	}

	private void exportExcel(List<Map<String, String>> rList, TitaVo titaVo) throws LogicException {

		int yearMonthMin = parse
				.stringToInteger(titaVo.getParam("inputYearStart") + "" + titaVo.getParam("inputMonthStart")) + 191100;
		int yearMonthMax = parse
				.stringToInteger(titaVo.getParam("inputYearEnd") + "" + titaVo.getParam("inputMonthEnd")) + 191100;

		int rocYMMin = yearMonthMin - 191100;
		int rocYMMax = yearMonthMax - 191100;

		int rocY = rocYMMax / 100;
		int rocM = rocYMMax % 100;

		int reportDate = titaVo.getEntDyI() + 19110000;
		String brno = titaVo.getBrno();
		String txcd = "LM003";
		String fileItem = "撥款／還款金額比較月報表" + rocYMMin + "~" + rocYMMax ;
		String fileName = "LM003-撥款／還款金額比較月報表 " + rocYMMin + "~" + rocYMMax;
		String defaultExcel = "LM003_底稿_撥款還款比較月報表.xlsx";
		String defaultSheet = "還款-撥還款比較";

		ReportVo reportVo = ReportVo.builder().setRptDate(reportDate).setBrno(brno).setRptCode(txcd)
				.setRptItem(fileItem).build();
		// 開啟報表
		makeExcel.open(titaVo, reportVo, fileName, defaultExcel, defaultSheet);

		makeExcel.setValue(19, 14, rocM);
		makeExcel.setValue(20, 14, rocY);

		BigDecimal entToTalAmt = BigDecimal.ZERO;
		BigDecimal ent0Amt = BigDecimal.ZERO;
		BigDecimal ent1Amt = BigDecimal.ZERO;

		BigDecimal memo3Amt = BigDecimal.ZERO;
		BigDecimal turnOvduAmt = BigDecimal.ZERO;
		BigDecimal entPerAmt = BigDecimal.ZERO;

		BigDecimal repayAllTotal = BigDecimal.ZERO;
		this.info("data size = " + rList.size());

		makeExcel.formulaCaculate(1, 1);
		makeExcel.formulaCaculate(6, 1);
		makeExcel.formulaCaculate(18, 1);
		makeExcel.formulaCaculate(19, 1);
		if (rList.size() == 0 || rList == null) {
			makeExcel.setValue(6, 3, "本日無資料");
		} else {

			int row = 0;
			BigDecimal lDrawdownAmt = BigDecimal.ZERO;
			BigDecimal lCloseAmtSeq1 = BigDecimal.ZERO;
			BigDecimal lCloseAmtSeq2 = BigDecimal.ZERO;
			BigDecimal lCloseAmtSeq3 = BigDecimal.ZERO;
			BigDecimal lNoCloseAmtSeq1 = BigDecimal.ZERO;
			BigDecimal lNoCloseAmtSeq2 = BigDecimal.ZERO;
			BigDecimal lNoCloseAmtSeq3 = BigDecimal.ZERO;
			BigDecimal lEnt0Amt = BigDecimal.ZERO;
			BigDecimal lRepayTotal = BigDecimal.ZERO;
			for (Map<String, String> r : rList) {

				lDrawdownAmt = getBigDecimal(r.get("DrawdownAmt"));
				lCloseAmtSeq1 = getBigDecimal(r.get("CloseAmtSeq1"));
				lCloseAmtSeq2 = getBigDecimal(r.get("CloseAmtSeq2"));
				lCloseAmtSeq3 = getBigDecimal(r.get("CloseAmtSeq3"));
				lNoCloseAmtSeq1 = getBigDecimal(r.get("NoCloseAmtSeq1"));
				lNoCloseAmtSeq2 = getBigDecimal(r.get("NoCloseAmtSeq2"));
				lNoCloseAmtSeq3 = getBigDecimal(r.get("NoCloseAmtSeq3"));
				lEnt0Amt = getBigDecimal(r.get("Ent0Amt"));
				lRepayTotal = getBigDecimal(r.get("RepayTotal"));
				this.info("DrawdownAmt =" + lCloseAmtSeq1);
				this.info("CloseAmtSeq1 =" + lCloseAmtSeq1);
				this.info("CloseAmtSeq2 =" + lCloseAmtSeq2);
				this.info("CloseAmtSeq3 =" + lCloseAmtSeq3);
				this.info("NoCloseAmtSeq1 =" + lNoCloseAmtSeq1);
				this.info("NoCloseAmtSeq2 =" + lNoCloseAmtSeq2);
				this.info("NoCloseAmtSeq3 =" + lNoCloseAmtSeq3);
				this.info("Ent0Amt =" + lEnt0Amt);
				this.info("RepayTotal =" + lRepayTotal);
				// 1月在第6列,初始值訂5,row=5+月份
				row = 5 + parse.stringToInteger(r.get("YearMonth")) % 100;
				// 撥款
				makeExcel.setValue(row, 2, lDrawdownAmt, "0.00", "R");
				// 結清-利率高轉貸
				makeExcel.setValue(row, 3, lCloseAmtSeq1, "0.00", "R");
				// 結清-買賣
				makeExcel.setValue(row, 4, lCloseAmtSeq2, "0.00", "R");
				// 結清-自行還款等
				makeExcel.setValue(row, 5, lCloseAmtSeq3, "0.00", "R");
				// 非結清-部分還款
				makeExcel.setValue(row, 7, lNoCloseAmtSeq1, "0.00", "R");
				// 非結清-本金攤提
				makeExcel.setValue(row, 8, lNoCloseAmtSeq2, "0.00", "R");
				// 非結清-轉催收
				makeExcel.setValue(row, 9, lNoCloseAmtSeq3, "0.00", "R");
				// 月底餘額
				makeExcel.setValue(row, 13, lEnt0Amt, "#,##0.00", "R");

				makeExcel.formulaCaculate(row, 6);
				makeExcel.formulaCaculate(row, 10);
				makeExcel.formulaCaculate(row, 11);
				makeExcel.formulaCaculate(row, 12);

				ent0Amt = getBigDecimal(r.get("Ent0Amt"));
				ent1Amt = getBigDecimal(r.get("Ent1Amt"));
				entToTalAmt = getBigDecimal(r.get("EntTotalAmt"));

				this.info("CloseAmt7 =" + getBigDecimal(r.get("CloseAmt7")));
				this.info("EntPerAmt =" + getBigDecimal(r.get("EntPerAmt")));
				// 內部代償?合計
				memo3Amt = memo3Amt.add(getBigDecimal(r.get("CloseAmt7")));
				// 轉催收合計
				turnOvduAmt = turnOvduAmt.add(getBigDecimal(r.get("NoCloseAmtSeq3")));
				// 企金自然人金額合計
				entPerAmt = entPerAmt.add(getBigDecimal(r.get("EntPerAmt")));
				// 帳載
				repayAllTotal = repayAllTotal.add(lRepayTotal);
			}

			int rowP = 22;

			lRepayTotal = lRepayTotal.subtract(lNoCloseAmtSeq3);

			makeExcel.setValue(rowP, 3, lCloseAmtSeq1.divide(lRepayTotal, 4, RoundingMode.HALF_UP), "0#.00%", "C");
			makeExcel.setValue(rowP, 4, lCloseAmtSeq2.divide(lRepayTotal, 4, RoundingMode.HALF_UP), "0#.00%", "C");
			makeExcel.setValue(rowP, 5, lCloseAmtSeq3.divide(lRepayTotal, 4, RoundingMode.HALF_UP), "0#.00%", "C");
			makeExcel.setValue(rowP, 7, lNoCloseAmtSeq1.divide(lRepayTotal, 4, RoundingMode.HALF_UP), "0#.00%", "C");
			makeExcel.setValue(rowP, 8, lNoCloseAmtSeq2.divide(lRepayTotal, 4, RoundingMode.HALF_UP), "0#.00%", "C");

			makeExcel.formulaCaculate(rowP, 6);
			makeExcel.formulaCaculate(rowP, 10);
			makeExcel.formulaCaculate(rowP, 11);

			for (int col = 2; col <= 12; col++) {
				makeExcel.formulaCaculate(18, col);
				makeExcel.formulaCaculate(19, col);
			}

		}

		BigDecimal memo4Diff = repayAllTotal.subtract(memo3Amt).subtract(turnOvduAmt).subtract(entPerAmt);

		// 備註
//		String memo1 = "=CONCATENATE(\"●\",N20,\"年\",N19,\"月\",\"底貸款總餘額：\",TEXT(" + entToTalAmt
//				+ ",\"0.00\"),\"億元     ●企金：\",TEXT(" + ent1Amt + ",\"0.00\"),\"億元   ●房貸：\",TEXT(" + ent0Amt
//				+ ",\"0.00\"),\"億元\")";
//		String memo3 = "CONCATENATE(\"●自行還款含內部代償、借新還舊、大額還款（1月~\",N19,\"月累積數\",TEXT(" + memo3Amt + ",\"0.00\"),\"億）。\")";
//		String memo4 = "=CONCATENATE(\"●\",N19,\"月實際還款數：\",TEXT(K18,\"0.00\"),\"（帳載） -\",TEXT(" + memo3Amt
//				+ ",\"0.00\"),\"（內部轉帳）-\",TEXT(I18,\"0.00\"),\"（轉催收）-\",TEXT(" + turnOvduAmt
//				+ ",\"0.00\"),\"(企金件以自然人申貸還款) ＝\",TEXT(" + entPerAmt + "),\"0.00\"),\"億\")";
		String memo1 = "●" + rocY + "年" + rocM + "月底貸款總餘額：" + entToTalAmt + "億元     ●企金：" + ent1Amt + "億元   ●房貸："
				+ ent0Amt + "億元";
		String memo2 = "●依報表：LN6361編製；撥款金額含催收回復，還款金額含轉催收。";
		String memo3 = "●自行還款含內部代償、借新還舊、大額還款（1月~" + rocM + "月累積數" + memo3Amt + "億)";
		String memo4 = "●" + rocM + "月實際還款數：" + repayAllTotal + "（帳載） -" + memo3Amt + "（內部轉帳）-" + turnOvduAmt
				+ "（轉催收）- " + entPerAmt + "(企金件以自然人申貸還款) ＝" + memo4Diff + "億";

		makeExcel.setValue(24, 1, memo1);
		makeExcel.setValue(25, 1, memo2);
		makeExcel.setValue(26, 1, memo3);
		makeExcel.setValue(27, 1, memo4);

		makeExcel.close();
	}

}
