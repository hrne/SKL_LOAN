package com.st1.itx.trade.LQ;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.db.service.springjpa.cm.LQ005ServiceImpl;
import com.st1.itx.util.common.MakeExcel;
import com.st1.itx.util.common.MakeReport;
import com.st1.itx.util.format.FormatUtil;

@Component
@Scope("prototype")

public class LQ005Report extends MakeReport {

	@Autowired
	LQ005ServiceImpl lQ005ServiceImpl;

	@Autowired
	MakeExcel makeExcel;

	public void exec(TitaVo titaVo) throws LogicException {

		int entDy = titaVo.getEntDyI();

		entDy = entDy + 19110000;

		int nowYearMonth = entDy / 100;

		int inputYearMonthStart = 0;
		int inputYearMonthEnd = 0;

		int nowYear = nowYearMonth / 100;
		int nowMonth = nowYearMonth % 100;

		this.info("entDy = " + entDy);
		this.info("nowYearMonth = " + nowYearMonth);
		this.info("nowYear = " + nowYear);
		this.info("nowMonth = " + nowMonth);

		if (nowMonth >= 1 && nowMonth <= 3) {
			inputYearMonthStart = nowYear * 100 + 1;
			inputYearMonthEnd = nowYear * 100 + 3;
		} else if (nowMonth >= 4 && nowMonth <= 6) {
			inputYearMonthStart = nowYear * 100 + 4;
			inputYearMonthEnd = nowYear * 100 + 6;
		} else if (nowMonth >= 7 && nowMonth <= 9) {
			inputYearMonthStart = nowYear * 100 + 7;
			inputYearMonthEnd = nowYear * 100 + 9;
		} else if (nowMonth >= 10 && nowMonth <= 12) {
			inputYearMonthStart = nowYear * 100 + 10;
			inputYearMonthEnd = nowYear * 100 + 12;
		} else {
			this.error("nowMonth out of range (01~12).");
		}

		List<Map<String, String>> listLQ005 = null;

		try {
			listLQ005 = lQ005ServiceImpl.findAll(inputYearMonthStart, inputYearMonthEnd, titaVo);
		} catch (Exception e) {
			StringWriter errors = new StringWriter();
			e.printStackTrace(new PrintWriter(errors));
			this.error("LQ005ServiceImpl.testExcel error = " + errors.toString());
		}
		exportExcel(listLQ005, titaVo);
	}

	private void exportExcel(List<Map<String, String>> listLQ005, TitaVo titaVo) throws LogicException {
		this.info("exportExcel ... ");

		makeExcel.open(titaVo, titaVo.getEntDyI(), titaVo.getKinbr(), "LQ005", "表A18_會計部申報表", "LQ005表A18_會計部申報表",
				"表A18_會計部申報表.xlsx", "108.03.31");

		makeExcel.setSheet("108.03.31", this.showRocDate(titaVo.getEntDyI(), 6));

		makeExcel.setValue(2, 1, "民國" + this.showRocDate(titaVo.getEntDyI(), 0));

		int rowCursor = 8;

		int seq = 1;

		BigDecimal highestLoanBalTotal = BigDecimal.ZERO;
		BigDecimal loanBalTotal = BigDecimal.ZERO;

		if (listLQ005 != null && listLQ005.size() > 0) {

			if (listLQ005.size() > 1) {
				// 將表格往下移，移出空間
				makeExcel.setShiftRow(rowCursor + 1, listLQ005.size() - 1);
			}

			for (Map<String, String> tLQ005 : listLQ005) {

				String outputSeq = FormatUtil.pad9("" + seq, 4);
				makeExcel.setValue(rowCursor, 1, outputSeq); // 交易序號

				makeExcel.setValue(rowCursor, 2, "02"); // 交易對象別代號
				makeExcel.setValue(rowCursor, 3, "03458902"); // 交易對象名稱-統編
				makeExcel.setValue(rowCursor, 4, "新光人壽"); // 交易對象名稱-姓名
				makeExcel.setValue(rowCursor, 5, "B"); // 交易對象別代號

				String custId = tLQ005.get("F0"); // F0 資金或信用收受者-統編
				makeExcel.setValue(rowCursor, 6, custId);

				String custName = tLQ005.get("F1"); // F1 資金或信用收受者-姓名
				makeExcel.setValue(rowCursor, 7, custName);

				makeExcel.setValue(rowCursor, 8, "放款02"); // 交易性質

				BigDecimal highestLoanBal = getBigDecimal(tLQ005.get("F2")); // F2 當季最高餘額
				makeExcel.setValue(rowCursor, 9, formatMillion(highestLoanBal));

				BigDecimal loanBal = getBigDecimal(tLQ005.get("F3")); // F3 季底交易帳列餘額
				makeExcel.setValue(rowCursor, 10, formatMillion(loanBal));

				highestLoanBalTotal = highestLoanBalTotal.add(highestLoanBal);
				loanBalTotal = loanBalTotal.add(loanBal);

				seq++;
				rowCursor++;
			}
		} else {
			makeExcel.setValue(rowCursor, 1, "本日無資料");
			
			makeExcel.setMergedRegion(rowCursor, rowCursor, 1, 10);
		}

		// 印合計
		makeExcel.setValue(rowCursor, 9, formatMillion(highestLoanBalTotal));
		makeExcel.setValue(rowCursor, 10, formatMillion(loanBalTotal));

		long sno = makeExcel.close();
		makeExcel.toExcel(sno);
	}

	private BigDecimal million = getBigDecimal("1000000");

	private BigDecimal formatMillion(BigDecimal amt) {
		return this.computeDivide(amt, million, 2);
	}

}
