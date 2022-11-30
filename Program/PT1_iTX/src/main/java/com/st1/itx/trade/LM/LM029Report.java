package com.st1.itx.trade.LM;

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
import com.st1.itx.db.service.springjpa.cm.LM029ServiceImpl;

import com.st1.itx.util.common.MakeExcel;
import com.st1.itx.util.common.MakeReport;
import com.st1.itx.util.common.data.ReportVo;

@Component
@Scope("prototype")
public class LM029Report extends MakeReport {

	@Autowired
	LM029ServiceImpl lM029ServiceImpl;

	@Autowired
	MakeExcel makeExcel;

	public int iYearMonth;

	/**
	 * 執行報表輸出
	 * 
	 * @param titaVo
	 * @param yearMonth 西元年月
	 * @throws LogicException
	 * 
	 */
	public void exec(TitaVo titaVo, int yearMonth) throws LogicException {

		List<Map<String, String>> listLM029 = null;

		iYearMonth = yearMonth;

		try {
			listLM029 = lM029ServiceImpl.findAll(titaVo, yearMonth);
			exportExcel(titaVo, listLM029);

		} catch (Exception e) {
			StringWriter errors = new StringWriter();
			e.printStackTrace(new PrintWriter(errors));
			this.error("LM029ServiceImpl findAll error = " + errors.toString());
		}

		try {
			listLM029 = lM029ServiceImpl.findAll2(titaVo, yearMonth);
			exportExcel2(titaVo, listLM029);

		} catch (Exception e) {
			StringWriter errors = new StringWriter();
			e.printStackTrace(new PrintWriter(errors));
			this.error("LM029ServiceImpl findAll error = " + errors.toString());
		}

		makeExcel.close();
	}

	private void exportExcel(TitaVo titaVo, List<Map<String, String>> listLM029) throws LogicException {
		this.info("LM029Report exportExcel");


		int reportDate = titaVo.getEntDyI() + 19110000;
		String brno = titaVo.getBrno();
		String txcd = "LM029";
		String fileItem = "放款餘額明細表";
		String fileName = "LM029-放款餘額明細表";
		String defaultExcel = "LM029_底稿_放款餘額明細表.xlsx";
		String defaultSheet = "la$w30p";

		ReportVo reportVo = ReportVo.builder().setRptDate(reportDate).setBrno(brno).setRptCode(txcd)
				.setRptItem(fileItem).build();

		// 開啟報表
		makeExcel.open(titaVo, reportVo, fileName, defaultExcel, defaultSheet);
		
		
//		makeExcel.open(titaVo, titaVo.getEntDyI(), titaVo.getKinbr(), "LM029", "放款餘額明細表", "LM029-放款餘額明細表",
//				"LM029_底稿_放款餘額明細表.xlsx", "la$w30p");

		String today = dDateUtil.getNowStringBc();

		// 表頭
		makeExcel.setValue(2, 20, "日　　期：" + this.showBcDate(today, 1));
		makeExcel.setValue(3, 20, "時　　間：" + dDateUtil.getNowStringTime().substring(0, 2) + ":"
				+ dDateUtil.getNowStringTime().substring(2, 4) + ":" + dDateUtil.getNowStringTime().substring(4, 6));

		if (listLM029 == null || listLM029.isEmpty()) {

			makeExcel.setValue(6, 1, "本日無資料", "L");

		} else {

			int row = 6;

			for (Map<String, String> r : listLM029) {

				for (int i = 0; i <= 25; i++) {

					String fieldValue = r.get("F" + i);

					int col = i + 1;

					switch (i) {
					case 0:
					case 1:
					case 2:
						makeExcel.setValue(row, col, fieldValue, "#0");
						break;
					case 6:
					case 7:
					case 10:
					case 11:
						if (fieldValue != null && !fieldValue.isEmpty() && !fieldValue.equals("0")) {
							makeExcel.setValue(row, col + 1, showBcDate(fieldValue, 0), "C");
						}
						break;
					case 8:
						BigDecimal rate = getBigDecimal(fieldValue);
						makeExcel.setValue(row, col + 1, rate, "0.0000", "R");
						break;
					case 13:
					case 14:
					case 15:
						BigDecimal amt = getBigDecimal(fieldValue);
						makeExcel.setValue(row, col + 1, amt, "#,##0", "R");
						break;
					case 16:
						makeExcel.setValue(row, 4, "00A".equals(fieldValue) ? "利變帳冊年金-" : "傳統帳冊" + fieldValue, "L"); // 帳冊別
						break;
					case 17:
						makeExcel.setValue(row, col, fieldValue, "L"); // 餘期數
						break;
					case 18:
					case 19:
					case 20:
					case 21:
					case 22:
						makeExcel.setValue(row, col, fieldValue, "L"); 
						break;
					default:
						makeExcel.setValue(row, col + 1, fieldValue, "L");
						break;
					}
				} // for

				row++;
			} // for

			// 放款餘額總計的 excel formula
			makeExcel.formulaCaculate(5, 17);
		}

	}

	private void exportExcel2(TitaVo titaVo, List<Map<String, String>> listLM029) throws LogicException {
		this.info("LM029Report exportExcel2");

		makeExcel.setSheet("Deliquency");
		
		// 起始欄
		int col = 2;
		
		// 單位元位置
		int unitCol = iYearMonth / 100 + 1;
		makeExcel.setValue(2, unitCol, "單位：元", "R");

		if (listLM029 == null || listLM029.isEmpty()) {

			makeExcel.setValue(3, 2, "本日無資料", "L");

		} else {


			for (Map<String, String> r : listLM029) {

				// 項目(年月日)
				int yearMonth = Integer.valueOf(r.get("YearMonth"));
				int year = yearMonth / 100;
				int month = yearMonth % 100;
				makeExcel.setValue(2, col, year + "/" + month, "C");

				// 逾1-2期金額
				BigDecimal onetwoAmt = r.get("12Amt").isEmpty() || r.get("12Amt") == null ? BigDecimal.ZERO
						: new BigDecimal(r.get("12Amt"));
				makeExcel.setValue(3, col, onetwoAmt, "#,##0", "R");

				// 放款總餘額
				BigDecimal totalAmt = r.get("totalAmt").isEmpty() || r.get("totalAmt") == null ? BigDecimal.ZERO
						: new BigDecimal(r.get("totalAmt"));
				makeExcel.setValue(4, col, totalAmt, "#,##0", "R");

				// 逾1~2期佔總額比
				BigDecimal onetwoRate = r.get("12Rate").isEmpty() || r.get("12Rate") == null ? BigDecimal.ZERO
						: new BigDecimal(r.get("12Rate"));
				makeExcel.setValue(5, col, onetwoRate, "R");

				// 逾放總額
				BigDecimal threeAmt = r.get("oAmt").isEmpty() || r.get("oAmt") == null ? BigDecimal.ZERO
						: new BigDecimal(r.get("oAmt"));
				makeExcel.setValue(6, col, threeAmt, "#,##0", "R");

				// 放款逾放比
				BigDecimal threeRate = r.get("oRate").isEmpty() || r.get("oRate") == null ? BigDecimal.ZERO
						: new BigDecimal(r.get("oRate"));
				makeExcel.setValue(7, col, threeRate, "R");

				col++;

			} // for

		}

		

	}

}
