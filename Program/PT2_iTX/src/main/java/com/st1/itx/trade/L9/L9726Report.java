package com.st1.itx.trade.L9;

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
import com.st1.itx.db.service.springjpa.cm.L9726ServiceImpl;
import com.st1.itx.util.common.MakeExcel;
import com.st1.itx.util.common.MakeReport;
import com.st1.itx.util.common.data.ReportVo;

@Component
@Scope("prototype")
public class L9726Report extends MakeReport {

	@Autowired
	L9726ServiceImpl l9726ServiceImpl;

	@Autowired
	MakeExcel makeExcel;

	String txcd = "L9726";
	String txname = "企金往來客戶統計表";
	String sheetName = "企金人員撥款明細";

	public boolean exec(TitaVo titaVo) throws LogicException {
		this.info("L9726Report exec start ...");

		int dataYear = Integer.parseInt(titaVo.getParam("DataYear"));

		String inputYear = String.valueOf(dataYear + 1911);

		List<Map<String, String>> listL9726 = null;

		try {
			listL9726 = l9726ServiceImpl.findAll(inputYear, titaVo);
		} catch (Exception e) {
			StringWriter errors = new StringWriter();
			e.printStackTrace(new PrintWriter(errors));
			this.error("L9726ServiceImpl.findAll error = " + errors.toString());
		}

		if (listL9726 == null || listL9726.isEmpty()) {
			return false;
		} else {
			exportExcel(titaVo, listL9726);
		}
		return true;
	}

	private void exportExcel(TitaVo titaVo, List<Map<String, String>> listL9726) throws LogicException {

		this.info("L9726Report exportExcel");

		ReportVo reportVo = ReportVo.builder().setRptDate(titaVo.getEntDyI()).setBrno(titaVo.getBrno()).setRptCode(txcd)
				.setRptItem(txname).build();

		// 開啟報表
		makeExcel.open(titaVo, reportVo,  txcd + "_" + txname,  txcd + "_底稿_" + txname + ".xlsx",1 );
		
		makeExcel.setSheet(1, sheetName);

		makeExcel.setValue(2, 3, titaVo.getParam("DataYear") + "年" + txname);

		int rowCursor = 6;

		BigDecimal totalOfDrawdownAmt = BigDecimal.ZERO;

		if (listL9726.size() > 1) {
			makeExcel.setShiftRow(rowCursor + 1, listL9726.size() - 1);
		}

		for (Map<String, String> m : listL9726) {

			// 戶號
			makeExcel.setValue(rowCursor, 2, m.get("F0"));
			// 戶名
			makeExcel.setValue(rowCursor, 3, m.get("F1"));
			// 統一編號
			makeExcel.setValue(rowCursor, 4, m.get("F2"));

			// 放款日期
			String drawdownDate = this.showRocDate(m.get("F3"), 2); // 轉回民國年
			this.info("drawdownDate = " + drawdownDate);
			String[] splitDrawdownDate = drawdownDate.split("-");
			for (String s : splitDrawdownDate) {
				this.info(s);
			}
			this.info("splitDrawdownDate.length = " + splitDrawdownDate.length);
			String drawdownYear = "";
			String drawdownMonth = "";
			String drawdownDay = "";
			if (splitDrawdownDate.length > 0) {
				drawdownYear = splitDrawdownDate[0];
				drawdownMonth = splitDrawdownDate[1];
				drawdownDay = splitDrawdownDate[2];
			}
			// 放款日期-年
			makeExcel.setValue(rowCursor, 5, getBigDecimal(drawdownYear), "#0");
			// 放款日期-月
			makeExcel.setValue(rowCursor, 6, getBigDecimal(drawdownMonth), "#0");
			// 放款日期-日
			makeExcel.setValue(rowCursor, 7, getBigDecimal(drawdownDay), "#0");

			// 核准額度
			makeExcel.setValue(rowCursor, 8, getBigDecimal(m.get("F4")), "#,##0");

			// 撥貸金額
			BigDecimal drawdownAmt = getBigDecimal(m.get("F5"));
			makeExcel.setValue(rowCursor, 9, drawdownAmt, "#,##0");
			// 計算 合計-撥貸金額
			totalOfDrawdownAmt = totalOfDrawdownAmt.add(drawdownAmt);

			// 目前餘額
			makeExcel.setValue(rowCursor, 10, getBigDecimal(m.get("F6")), "#,##0");
			// 可用額度
			makeExcel.setValue(rowCursor, 11, getBigDecimal(m.get("F7")), "#,##0");
			// 目前利率
			makeExcel.setValue(rowCursor, 12, getBigDecimal(m.get("F8")), "#,##0.0000");
			// 放款成數
			makeExcel.setValue(rowCursor, 13, getBigDecimal(m.get("F9")), "#,##0.00%");

			// 循環動用
			String isRecycle = m.get("F10");
			if (isRecycle == null || isRecycle.isEmpty() || isRecycle.equals("0")) {
				isRecycle = "N";
			} else {
				isRecycle = "Y";
			}
			makeExcel.setValue(rowCursor, 14, isRecycle);

			// 放款性質
			String clItem = m.get("F11"); // 擔保品類別
			String ruleItem = m.get("F12"); // 管制代碼
			String idType = m.get("F13"); // 身分別
			String cityItem = m.get("F14"); // 擔保品縣市別

			String loanType = "";
			if (clItem != null && !clItem.isEmpty()) {
				loanType += clItem;
			}
			if (ruleItem != null && !ruleItem.isEmpty()) {
				if (!loanType.isEmpty()) {
					loanType += "_";
				}
				loanType += ruleItem;
			}
			if (idType != null && !idType.isEmpty()) {
				if (!loanType.isEmpty()) {
					loanType += "_";
				}
				loanType += idType;
			}
			if (cityItem != null && !cityItem.isEmpty()) {
				if (!loanType.isEmpty()) {
					loanType += "_";
				}
				loanType += cityItem;
			}

			makeExcel.setValue(rowCursor, 15, loanType);

			// 企金人員
			makeExcel.setValue(rowCursor, 16, m.get("F15"));

			rowCursor++;
		}

		// 列印 合計-撥貸金額
		makeExcel.setValue(rowCursor, 9, totalOfDrawdownAmt, "#,##0");

		long sno = makeExcel.close();
		makeExcel.toExcel(sno);
	}
}
