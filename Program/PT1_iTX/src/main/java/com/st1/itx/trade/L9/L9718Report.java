package com.st1.itx.trade.L9;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.db.service.springjpa.cm.L9718ServiceImpl;
import com.st1.itx.util.common.MakeExcel;
import com.st1.itx.util.common.MakeReport;

@Component
@Scope("prototype")

public class L9718Report extends MakeReport {

	@Autowired
	L9718ServiceImpl l9718ServiceImpl;

	@Autowired
	MakeExcel makeExcel;

	String TXCD = "L9718";

	// TXName非完整, 在exportExcel中加上種類前綴
	String TXName = "放款催繳處理結果";

	// pivot position for data inputs
	int pivotRow = 3; // 1-based
	int pivotCol = 1; // 1-based

	int totalAmount = 0;

	// number with commas
	NumberFormat nfNum = NumberFormat.getNumberInstance();

	public boolean exec(TitaVo titaVo) throws LogicException {
		this.info(TXCD + "Report exec start ...");

		List<Map<String, String>> lL9718Ovdu = null;
		List<Map<String, String>> lL9718Others = null;

		try {
			lL9718Ovdu = l9718ServiceImpl.findAll(titaVo, true);
			lL9718Others = l9718ServiceImpl.findAll(titaVo, false);
		} catch (Exception e) {
			StringWriter errors = new StringWriter();
			e.printStackTrace(new PrintWriter(errors));
			this.error(TXCD + "ServiceImpl.findAll error = " + errors.toString());
		}

		exportExcel(titaVo, lL9718Ovdu, "催收");
		exportExcel(titaVo, lL9718Others, "逾期");

		return true;

	}

	private void exportExcel(TitaVo titaVo, List<Map<String, String>> lList, String typeText) throws LogicException {

		this.info(TXCD + "Report exportExcel");

		makeExcel.open(titaVo, titaVo.getEntDyI(), titaVo.getKinbr(), TXCD, typeText+TXName, TXCD + "_" + typeText + TXName, TXCD + "_底稿_" + typeText + "成果統計表" + ".xlsx", 1,
				titaVo.getParam("inputYearMonth").substring(0, 3) + "年" + titaVo.getParam("inputYearMonth").substring(3) + "月" + typeText);

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
					String tmpValue = tLDVo.get("F" + i);

					if (typeText.equals("逾期")) {
						// switch by code of Column; i.e. Col A, Col B...
						// breaks if more than 26 columns!
						switch (String.valueOf((char) (65 + i))) {
						// if specific column needs special treatment, insert case here.
						case "I":
							// only setValue if there is value.
							if (tmpValue != "") {
								makeExcel.setValue(row, col, tmpValue, "R");
							}
							break;
						case "J":
							makeExcel.setValue(row, col, Integer.parseInt(tmpValue), "#,##0");
							break;
						case "L":
							makeExcel.setValue(row, col, Integer.parseInt(tmpValue), "#,##0");
							break;
						case "O":
							// percentage
							if (Integer.parseInt(tLDVo.get("F9")) != 0) {
								makeExcel.setValue(row, col, Double.toString(Math.floor(Integer.parseInt(tLDVo.get("F11")) / Integer.parseInt(tLDVo.get("F9"))) * 100) + '%', "R");
							} else {
								makeExcel.setValue(row, col, "", "R");
							}
							break;
						default:
							try {
								makeExcel.setValue(row, col, new BigDecimal(tmpValue), "R");
							} catch (Exception e) {
								makeExcel.setValue(row, col, tmpValue, "R");
							}
							break;
						}

					} else if (typeText.equals("催收")) {
						// switch by code of Column; i.e. Col A, Col B...
						// breaks if more than 26 columns!
						switch (String.valueOf((char) (65 + i))) {
						// if specific column needs special treatment, insert case here.
						case "I":
							// only setValue if there is value.
							if (tmpValue != "") {
								makeExcel.setValue(row, col, tmpValue, "R");
							}
							break;
						case "J":
							makeExcel.setValue(row, col, Integer.parseInt(tmpValue), "#,##0");
							break;
						case "K":
							makeExcel.setValue(row, col, Integer.parseInt(tmpValue), "#,##0");
							break;
						case "L":
							makeExcel.setValue(row, col, Integer.parseInt(tmpValue), "#,##0");
							break;
						case "M":
							makeExcel.setValue(row, col, Integer.parseInt(tmpValue), "#,##0");
							break;
						case "Q":
							// percentage
							if (Integer.parseInt(tLDVo.get("F12")) != 0) {
								makeExcel.setValue(row, col, Double.toString(Math.floor(Integer.parseInt(tLDVo.get("F14")) / Integer.parseInt(tLDVo.get("F12"))) * 100) + '%', "R");
							} else {
								makeExcel.setValue(row, col, "", "R");
							}
							break;
						default:
							try {
								makeExcel.setValue(row, col, new BigDecimal(tmpValue), "R");
							} catch (Exception e) {
								makeExcel.setValue(row, col, tmpValue, "R");
							}
							break;
						}
					}

				}

				rowShift++;
			} // for

			if (typeText.equals("逾期")) {
				makeExcel.formulaCaculate(2, 6);
				makeExcel.formulaCaculate(2, 8);
				makeExcel.formulaCaculate(2, 9);
				makeExcel.formulaCaculate(2, 10);
				makeExcel.formulaCaculate(2, 12);
			} else if (typeText.equals("催收")) {
				makeExcel.formulaCaculate(2, 6);
				makeExcel.formulaCaculate(2, 8);
				makeExcel.formulaCaculate(2, 9);
				makeExcel.formulaCaculate(2, 13);
				makeExcel.formulaCaculate(2, 15);
			}
		} else {
			makeExcel.setValue(pivotRow, pivotCol, "本月無資料");
		}

		makeExcel.close();
		//makeExcel.toExcel(sno);
	}
}
