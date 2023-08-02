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
import com.st1.itx.db.service.springjpa.cm.LM030ServiceImpl;
import com.st1.itx.util.common.BaTxCom;
import com.st1.itx.util.common.MakeExcel;
import com.st1.itx.util.common.MakeReport;
import com.st1.itx.util.common.data.ReportVo;
import com.st1.itx.util.date.DateUtil;
import com.st1.itx.util.format.FormatUtil;
import com.st1.itx.util.parse.Parse;

@Component
@Scope("prototype")
public class LM030Report extends MakeReport {

	@Autowired
	LM030ServiceImpl lM030ServiceImpl;

	@Autowired
	MakeExcel makeExcel;

	@Autowired
	DateUtil dateUtil;

	@Autowired
	Parse parse;

	@Autowired
	public BaTxCom baTxCom;

	public Boolean exec(TitaVo titaVo) throws LogicException {

		List<Map<String, String>> listLM030 = null;

		try {
			listLM030 = lM030ServiceImpl.findAll(titaVo);
			exportExcel(titaVo, listLM030);

		} catch (Exception e) {
			StringWriter errors = new StringWriter();
			e.printStackTrace(new PrintWriter(errors));
			this.info("LM030ServiceImpl.testExcel error = " + errors.toString());
			return false;
		}

		return true;
	}

	private void exportExcel(TitaVo titaVo, List<Map<String, String>> listLM030) throws LogicException {
		this.info("LM030Report exportExcel");

		int reportDate = titaVo.getEntDyI() + 19110000;
		String brno = titaVo.getBrno();
		String txcd = "LM030";
		String fileItem = "轉催收案件明細";
		String fileName = "LM030轉催收案件明細";
		String defaultExcel = "LM030_底稿_轉催收案件明細_核定總表.xlsx";
		String defaultSheet = "YYYMM";

		ReportVo reportVo = ReportVo.builder().setRptDate(reportDate).setBrno(brno).setRptCode(txcd)
				.setRptItem(fileItem).build();

		// 開啟報表
		makeExcel.open(titaVo, reportVo, fileName, defaultExcel, defaultSheet);

		int yy = Integer.valueOf(titaVo.getParam("YearMonth")) / 100;
		int mm = Integer.valueOf(titaVo.getParam("YearMonth")) % 100;

		int nextYY = 0;
		int nextMM = 0;

		if (mm == 12) {
			mm = 1;
			yy = yy + 1;

		} else {
			mm = mm + 1;
		}

		if (mm == 12) {
			nextMM = 2;
			nextYY = yy + 1;
		} else if (mm == 11) {
			nextMM = 1;
			nextYY = yy + 1;
		} else {
			nextMM = mm + 1;
			nextYY = yy;
		}

		int nextDueDate = nextYY * 10000 + nextMM * 100 + 1;

		this.info("nextDueDate = " + nextDueDate);

		int rocYM = yy * 100 + mm;

		makeExcel.setSheet("YYYMM", rocYM + "");
		makeExcel.setValue(1, 1, yy + "年 " + mm + " 月轉催收明細表");
		BigDecimal total = BigDecimal.ZERO;

		int row = 3;

		if (listLM030 == null || listLM030.isEmpty()) {
			makeExcel.setValue(3, 1, "本日無資料");
		} else {

			int custNoCnt = 0;
			int tmpCustNo = 0;
			for (Map<String, String> tLDVo : listLM030) {

				int custNo = parse.stringToInteger(tLDVo.get("CustNo"));
				int facmNo = parse.stringToInteger(tLDVo.get("FacmNo"));
				// 一樣表示是同一擔保品戶號
				if (tmpCustNo == custNo) {
					custNoCnt++;
				}

				if (tmpCustNo != custNo) {
					tmpCustNo = custNo;
					custNoCnt = 1;
				}

				String value = "";
				int col = 0;
				for (int i = 0; i <= 11; i++) {
					makeExcel.setFontType(1);
					value = tLDVo.get("F" + i);
					col++;
					switch (i) {
					case 5:
					case 9:
					case 10:
						makeExcel.setValue(row, col,
								parse.isNumeric(value) ? parse.stringToInteger(this.showRocDate(value, 3))
										: parse.stringToInteger(value),
								"R");
						break;
					case 6:
						// 金額
						BigDecimal bd = getBigDecimal(value);
						makeExcel.setValue(row, col, bd, "#,##0", "R");
						total = total.add(bd);
						break;
					case 7:// 利息金額(不顯示)

						makeExcel.setValue(row, col, ovduIntAmt(custNo, facmNo, nextDueDate, titaVo), "#,##0", "R");

						break;
					case 8:
						// 利率
						makeExcel.setValue(row, col, getBigDecimal(value), "#,##0.0000", "R");
						if (custNoCnt > 1) {
							makeExcel.setValue(row, 13, "同一擔保品", "C");
						}
						break;

					default:
						makeExcel.setValue(row, col, parse.isNumeric(value) ? parse.stringToBigDecimal(value) : value,
								"C");
						break;
					}
				} // for
				row++;
			} // for
		}

		row += 10;

		// block 1
		makeExcel.setFontType(1);
		makeExcel.setSize(14);
		makeExcel.setIBU("B");
		makeExcel.setValue(row, 1, "放款管理課", "C");
		// block 2
		makeExcel.setFontType(1);
		makeExcel.setSize(14);
		makeExcel.setIBU("B");
		makeExcel.setValue(row, 6, "放款服務課", "C");
		// block 3
		makeExcel.setFontType(1);
		makeExcel.setSize(14);
		makeExcel.setIBU("B");
		makeExcel.setValue(row, 10, "部室主管", "C");
		// block 4
		makeExcel.setFontType(1);
		if (listLM030 == null || listLM030.isEmpty()) {
			makeExcel.setMergedRegionValue(row + 1, row + 1, 1, 7, "一、經查本月逾期放款，無清償期屆滿六個月需轉列催收款之案件。\r\n" + "二、陳核。 ");
		} else {
			makeExcel.setMergedRegionValue(row + 1, row + 1, 1, 7,
					"一、經查本月逾期放款清償期屆滿六個月案件，依規將本金及應收利息轉列催收款項，金額共計 $" + FormatUtil.formatAmt(total, 0) + "元。 \r\n"
							+ "二、本月轉入催收款案件未發生『撥款後繳款期數未滿18個月即轉入催收戶』之情事。\r\n" + "三、陳核。 ");
		}
		makeExcel.setColor("RED");
		makeExcel.setValue(row + 3, 5, listLM030.size(), "C");
		makeExcel.setColor("RED");
		makeExcel.setValue(row + 3, 7, total, "#,##0");
		makeExcel.setValue(row + 5, 1, "一、李案為年期延長後再協議案件，因未符合免列報逾放條件，故轉列催收款項。");

		makeExcel.close();
	}

	private BigDecimal ovduIntAmt(int iCustno, int iFacmNo, int payIntDate, TitaVo titaVo) throws LogicException {
		int tbsdy = this.txBuffer.getTxCom().getTbsdy();

		BigDecimal ovduIntAmt = BigDecimal.ZERO; // 轉催收利息

		try {
			baTxCom.settingUnPaid(tbsdy, iCustno, iFacmNo, 0, 3, BigDecimal.ZERO, titaVo); // 3-結案
		} catch (LogicException e) {
			this.info("ErrorMsg :" + e.getErrorMsg(titaVo) + " " + iCustno + "-" + iFacmNo);
		}
		// 轉催收利息 = 短繳利息 + 利息
		ovduIntAmt = baTxCom.getInterest().add(baTxCom.getShortfallInterest());

		this.info("ovduIntAmt =" + ovduIntAmt);
		return ovduIntAmt;
	}

}
