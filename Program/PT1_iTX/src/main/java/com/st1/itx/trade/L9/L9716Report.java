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
import com.st1.itx.db.service.springjpa.cm.L9716ServiceImpl;
import com.st1.itx.util.common.MakeExcel;
import com.st1.itx.util.common.MakeReport;

@Component
@Scope("prototype")

public class L9716Report extends MakeReport {

	@Autowired
	L9716ServiceImpl l9716ServiceImpl;

	@Autowired
	MakeExcel makeExcel;

	String TXCD = "L9716";
	String TXName = "逾放處理催收明細表";

	// pivot position for data inputs
	int pivotRow = 2; // 1-based
	int pivotCol = 1; // 1-based

	public boolean exec(TitaVo titaVo) throws LogicException {
		this.info(TXCD + "Report exec start ...");

		List<Map<String, String>> lL9716_1 = null;
		List<Map<String, String>> lL9716_2 = null;

		try {

			lL9716_1 = l9716ServiceImpl.findAll(titaVo);
			lL9716_2 = l9716ServiceImpl.ovduFindAll(titaVo);

		} catch (Exception e) {
			StringWriter errors = new StringWriter();
			e.printStackTrace(new PrintWriter(errors));
			this.error(TXCD + "ServiceImpl.findAll error = " + errors.toString());
		}

		exportExcel(titaVo, lL9716_1);
		exportExcel2(titaVo, lL9716_2);

		makeExcel.close();
		//makeExcel.toExcel(sno);

		return true;

	}

	private void exportExcel(TitaVo titaVo, List<Map<String, String>> lList) throws LogicException {

		String SheetName = titaVo.getParam("inputYear") + titaVo.getParam("inputMonth") + "工作表";
		String tmpValue = "";

		this.info(TXCD + "Report exportExcel");


		
		makeExcel.open(titaVo, titaVo.getEntDyI(), titaVo.getKinbr(), TXCD, TXName, TXCD + "_" + TXName,
				TXCD + "_底稿_" + TXName + ".xlsx", 1, SheetName);

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

					// switch by code of Column; i.e. Col A, Col B...
					// breaks if more than 26 columns!
					tmpValue = tLDVo.get("F" + i);
					this.info("L9716.tmpValue" + tmpValue + ",F?:" + i);
					switch (String.valueOf((char) (65 + i))) {
					// if specific column needs special treatment, insert case here.
					default:
						try {
							makeExcel.setValue(row, col, new BigDecimal(tmpValue), "L");
						} catch (Exception e) {
							this.info("L9716.catch1" + tmpValue);
							makeExcel.setValue(row, col, tmpValue, "L");
						}
						break;
					}



				} // for

				rowShift++;

			} // for

			makeExcel.formulaCaculate(1, 2);
			makeExcel.formulaCaculate(1, 9);
		} else {
			makeExcel.setValue(pivotRow, pivotCol, "本月無資料");
		}

	}

	private void exportExcel2(TitaVo titaVo, List<Map<String, String>> lList) throws LogicException {

		int pivotRow = 2; // 1-based
		int pivotCol = 2; // 1-based

		String SheetName = "催收明細表";
		String tmpValue = "";

		this.info(TXCD + "Report exportExcel2");

		makeExcel.setSheet(SheetName);

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

					// switch by code of Column; i.e. Col A, Col B...
					// breaks if more than 26 columns!
					tmpValue = tLDVo.get("F" + i);

					if ("F" + i == "F13") {
						this.info("L9716.catch3=" + tmpValue);
						makeExcel.setValue(row, col, tmpValue, "L");
						break;
					}

					switch (String.valueOf((char) (65 + i))) {
					// if specific column needs special treatment, insert case here.
					default:
						try {
							makeExcel.setValue(row, col, new BigDecimal(tmpValue), "L");
						} catch (Exception e) {
							this.info("L9716.catch2=" + tmpValue);
							makeExcel.setValue(row, col, tmpValue, "L");
						}
						break;
					}

				} // for

				rowShift++;

			} // for
			for (int i = 1; i <= lList.size(); i++) {
				makeExcel.formulaCaculate(1 + i, 1);
			}

			makeExcel.formulaCaculate(1, 6);
			makeExcel.formulaCaculate(1, 9);
			makeExcel.formulaCaculate(1, 39);
		} else {
			makeExcel.setValue(pivotRow, pivotCol, "本月無資料");
		}

	}
}
