package com.st1.itx.trade.L9;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.NumberFormat;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.db.service.springjpa.cm.L9718ServiceImpl;
import com.st1.itx.db.service.springjpa.cm.L9718ServiceImpl.ReportType;
import com.st1.itx.util.common.MakeExcel;
import com.st1.itx.util.common.MakeReport;

@Component
@Scope("prototype")

public class L9718Report extends MakeReport {
	private static final Logger logger = LoggerFactory.getLogger(L9718Report.class);

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
		logger.info(TXCD + "Report exec start ...");

		List<Map<String, String>> lL9718Ovdu = null;
		List<Map<String, String>> lL9718Others = null;

		try {
			lL9718Ovdu = l9718ServiceImpl.findAll(titaVo, ReportType.Acct990);
			lL9718Others = l9718ServiceImpl.findAll(titaVo, ReportType.AcctOthers);
		} catch (Exception e) {
			StringWriter errors = new StringWriter();
			e.printStackTrace(new PrintWriter(errors));
			logger.error(TXCD + "ServiceImpl.findAll error = " + errors.toString());
		}

		exportExcel(titaVo, lL9718Ovdu, ReportType.Acct990);
		exportExcel(titaVo, lL9718Others, ReportType.AcctOthers);

		return true;

	}

	private void exportExcel(TitaVo titaVo, List<Map<String, String>> lList, ReportType reportType) throws LogicException {

		logger.info(TXCD + "Report exportExcel");

		makeExcel.open(titaVo, titaVo.getEntDyI(), titaVo.getKinbr(), TXCD, TXName, TXCD + "_" + reportType.getDesc() + TXName, TXCD + "_底稿_" + reportType.getDesc() + "成果統計表" + ".xlsx", 1,
				titaVo.getParam("inputYearMonth").substring(0, 3) + "年" + titaVo.getParam("inputYearMonth").substring(3) + "月" + reportType.getDesc());

		if (lList != null && !lList.isEmpty()) {

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
					if (tmpValue == null) {
						// 因為reportType分別對應不同的query, 這邊先用null判定Fi到底存不存在...
						tmpValue = "";
					}

					BigDecimal output = BigDecimal.ZERO;

					switch (reportType) {
					case Acct990:
						// F9,F10,F11,F12
						// ,F14
						// formatAmt

						// F16是百分比
						// 用F14 / F12 * 100 可算出來

						switch (i) {
						case 9:
						case 10:
						case 11:
						case 12:
						case 14:
							output = BigDecimal.ZERO;

							try {
								output = new BigDecimal(tmpValue);
							} catch (Exception e) {
								this.info("L9718Report-990, F" + i + ": " + tmpValue);
							}

							makeExcel.setValue(row, col, formatAmt(output, 0));
							break;
						case 16:
							output = computeDivide(getBigDecimal(tLDVo.get("F14")), getBigDecimal(tLDVo.get("F12")), 3).multiply(getBigDecimal("100")).setScale(0, RoundingMode.HALF_UP);

							makeExcel.setValue(row, col, output + "%");
							break;
						default:
							makeExcel.setValue(row, col, tmpValue);
						}

						break;
					case AcctOthers:
						// F9, F11
						// formatAmt

						// F14是百分比

						// 用F11 / F9求出

						switch (i) {

						case 9:
						case 11:

							output = getBigDecimal(tmpValue);

							makeExcel.setValue(row, col, formatAmt(output, 0));

							break;

						case 14:

							output = computeDivide(getBigDecimal(tLDVo.get("F11")), getBigDecimal(tLDVo.get("F9")), 2).multiply(getBigDecimal("100")).setScale(0, RoundingMode.HALF_UP);
							makeExcel.setValue(row, col, output + "%");
							break;

						default:
							makeExcel.setValue(row, col, tmpValue);
							break;
						}

						break;

					default:
						this.warn("L9718Report weird ReportType");
						break;
					}

				}

				rowShift++;
			}

			if (reportType == ReportType.AcctOthers) {
				makeExcel.formulaCaculate(2, 6);
				makeExcel.formulaCaculate(2, 8);
				makeExcel.formulaCaculate(2, 9);
				makeExcel.formulaCaculate(2, 10);
				makeExcel.formulaCaculate(2, 12);
			} else if (reportType == ReportType.Acct990) {
				makeExcel.formulaCaculate(2, 6);
				makeExcel.formulaCaculate(2, 8);
				makeExcel.formulaCaculate(2, 9);
				makeExcel.formulaCaculate(2, 13);
				makeExcel.formulaCaculate(2, 15);
			}

		} else {
			makeExcel.setValue(pivotRow, pivotCol, "本月無資料");
		}

		long sno = makeExcel.close();
		// makeExcel.toExcel(sno);
	}
}
