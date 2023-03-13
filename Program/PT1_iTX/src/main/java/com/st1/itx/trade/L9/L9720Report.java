package com.st1.itx.trade.L9;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.db.service.springjpa.cm.L9720ServiceImpl;
import com.st1.itx.util.common.MakeExcel;
import com.st1.itx.util.common.MakeReport;
import com.st1.itx.util.common.data.ReportVo;

@Component("L9720Report")
@Scope("prototype")
public class L9720Report extends MakeReport {

	@Autowired
	L9720ServiceImpl l9720ServiceImpl;

	@Autowired
	MakeExcel makeExcel;

	String TXCD = "L9720";
	String TXName = "理財型商品續約檢核報表";

	// pivot position for data inputs
	int pivotRow = 3; // 1-based
	int pivotCol = 1; // 1-based

	public boolean exec(TitaVo titaVo) throws LogicException {
		this.info(TXCD + "Report exec start ...");

		List<Map<String, String>> lL9720 = null;

		try {
			lL9720 = l9720ServiceImpl.findAll(titaVo);
		} catch (Exception e) {
			StringWriter errors = new StringWriter();
			e.printStackTrace(new PrintWriter(errors));
			this.error(TXCD + "ServiceImpl.findAll error = " + errors.toString());
		}

		exportExcel(titaVo, lL9720);

		return true;

	}

	private void exportExcel(TitaVo titaVo, List<Map<String, String>> lList) throws LogicException {

		String SheetName = "LNW99UP";
		String tmpValue = "";

		this.info(TXCD + "Report exportExcel");

		// YYYMM續約檢核結果(YYYMM及YYYMM月).xlsx

		String EntDy = Integer.toString(titaVo.getEntDyI() + 19110000);
		LocalDate validDatePivot = LocalDate.of(Integer.parseInt(EntDy.substring(0, 4)),
				Integer.parseInt(EntDy.substring(4, 6)), Integer.parseInt(EntDy.substring(6)));
		LocalDate validDateFirst = validDatePivot.minusMonths(10);
		LocalDate validDateSecond = validDatePivot.minusMonths(22);

		int reportDate = titaVo.getEntDyI() + 19110000;
		String brno = titaVo.getBrno();
		String txcd = TXCD;
		String fileItem = TXName;
		String fileName = TXCD + "_" + Integer.toString(validDatePivot.getYear() - 1911)
				+ String.format("%02d", validDatePivot.getMonthValue()) + "續約檢核結果("
				+ Integer.toString(validDateFirst.getYear() - 1911)
				+ String.format("%02d", validDateFirst.getMonthValue()) + "及"
				+ Integer.toString(validDateSecond.getYear() - 1911)
				+ String.format("%02d", validDateSecond.getMonthValue()) + "月)";
		String defaultExcel = TXCD + "_底稿_" + TXName + ".xlsx";
//		String defaultSheet = "la$w30p";

		ReportVo reportVo = ReportVo.builder().setRptDate(reportDate).setBrno(brno).setRptCode(txcd)
				.setRptItem(fileItem).build();

		// 開啟報表
		makeExcel.open(titaVo, reportVo, fileName, defaultExcel, 1);

		makeExcel.setSheet(1, SheetName);

		if (lList != null && lList.size() != 0) {

			int rowShift = 0;

			for (Map<String, String> tLDVo : lList) {

				int colShift = 0;

				for (int i = 0; i < tLDVo.size(); i++) {

					int col = i + pivotCol + colShift; // 1-based
					int row = pivotRow + rowShift; // 1-based

					// Query received will have column names in the format of F0, even if no alias
					// is set in SQL
					// notice it's 0-based for those names

					tmpValue = tLDVo.get("F" + i);
					switch (i) {
					// if specific column needs special treatment, insert case here.
					default:
						try {
							makeExcel.setValue(row, col, new BigDecimal(tmpValue), "L");
						} catch (Exception e) {
							makeExcel.setValue(row, col, tmpValue, "L");
						}
						break;
					}

				} // for

				rowShift++;

			} // for

		} else {
			makeExcel.setValue(pivotRow, pivotCol, "本月無資料");
		}

		long sno = makeExcel.close();
		makeExcel.toExcel(sno);
	}
}
