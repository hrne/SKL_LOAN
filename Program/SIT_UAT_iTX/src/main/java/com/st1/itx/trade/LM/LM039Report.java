package com.st1.itx.trade.LM;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.db.service.springjpa.cm.LM039ServiceImpl;
import com.st1.itx.util.common.MakeExcel;
import com.st1.itx.util.common.MakeReport;

@Component
@Scope("prototype")

public class LM039Report extends MakeReport {
	private static final Logger logger = LoggerFactory.getLogger(LM039Report.class);

	@Autowired
	LM039ServiceImpl lM039ServiceImpl;

	@Autowired
	MakeExcel makeExcel;

	public void exec(TitaVo titaVo) throws LogicException {
		List<Map<String, String>> listLM039 = null;
		try {
			listLM039 = lM039ServiceImpl.findAll(titaVo);

		} catch (Exception e) {
			StringWriter errors = new StringWriter();
			e.printStackTrace(new PrintWriter(errors));
			this.error("LM039ServiceImpl.testExcel error = " + errors.toString());
		}
		exportExcel(titaVo, listLM039);
	}

	private void exportExcel(TitaVo titaVo, List<Map<String, String>> listLM039) throws LogicException {
		this.info("LM039Report exportExcel");
		makeExcel.open(titaVo, titaVo.getEntDyI(), titaVo.getKinbr(), "LM039", "催收案件明細", "LM039催收案件明細",
				"LM039催收案件明細.xls", "D9210083");

		if (listLM039.size() == 0) {
			makeExcel.setValue(4, 1, "本日無資料");
		}

		int row = 2;
		BigDecimal col8 = BigDecimal.ZERO;
		BigDecimal col26 = BigDecimal.ZERO;
		for (Map<String, String> tLDVo : listLM039) {

			String ad = "";
			int col = 0;
			for (int i = 0; i < tLDVo.size(); i++) {

				ad = "F" + col;
				col++;

				String value = tLDVo.get(ad).trim();

				switch (col) {
				case 2:
				case 6:
				case 7:
				case 9:
				case 13:
				case 14:
				case 15:
				case 16:
				case 18:
				case 21:
				case 22:
				case 30:
					makeExcel.setValue(row, col, value == null || value.isEmpty() ? " " : value, "R");
					break;
				case 8:
				case 10:
				case 11:
				case 12:
				case 24:
				case 25:
				case 26:
					// 金額
					makeExcel.setValue(row, col,
							value == null || value.isEmpty() ? BigDecimal.ZERO : new BigDecimal(value), "#,##0");
					if(col == 8) {
						col8 = col8.add(value == null || value.isEmpty() ? BigDecimal.ZERO : new BigDecimal(value));
					}
					if(col == 26) {
						col26 = col26.add(value == null || value.isEmpty() ? BigDecimal.ZERO : new BigDecimal(value));
					}
					break;
				case 23:
					makeExcel.setValue(row, col,
							value == null || value.isEmpty() ? BigDecimal.ZERO : new BigDecimal(value), "#,##0.0000", "R");
					break;
				default:
					makeExcel.setValue(row, col, value == null || value.isEmpty() ? " " : value);
					break;
				}
			} // for

			row++;
		} // for
//		makeExcel.setColor("RED");
		makeExcel.setValue(1, 26, col26, "#,##0");
//		makeExcel.setColor("RED");
		makeExcel.setValue(1, 5, row - 2);
//		makeExcel.setColor("RED");
		makeExcel.setValue(1, 8, col8, "#,##0");
		long sno = makeExcel.close();
		makeExcel.toExcel(sno);
	}

}
