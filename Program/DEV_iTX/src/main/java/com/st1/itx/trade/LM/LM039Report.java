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
import com.st1.itx.db.service.springjpa.cm.LM039ServiceImpl;
import com.st1.itx.util.common.MakeExcel;
import com.st1.itx.util.common.MakeReport;

@Component
@Scope("prototype")

public class LM039Report extends MakeReport {

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

		if (listLM039 == null || listLM039.isEmpty()) {
			makeExcel.setValue(4, 1, "本日無資料");
		} else {

			int row = 2;
			BigDecimal F7 = BigDecimal.ZERO;
			BigDecimal F25 = BigDecimal.ZERO;
			for (Map<String, String> tLDVo : listLM039) {

				for (int i = 0; i <= 31; i++) {

					int col = i + 1;

					String value = tLDVo.get("F"+i);
					value = value == null ? "" : value.trim();

					switch (i) {
					case 1:
					case 5:
					case 6:
					case 8:
					case 12:
					case 13:
					case 14:
					case 15:
					case 17:
					case 20:
					case 21:
					case 29:
						makeExcel.setValue(row, col, value.isEmpty() ? " " : value, "R");
						break;
					case 7:
					case 9:
					case 10:
					case 11:
					case 23:
					case 24:
					case 25:
						// 金額
						BigDecimal bd = getBigDecimal(value);
						makeExcel.setValue(row, col, bd, "#,##0");
						if (i == 7) {
							F7 = F7.add(bd);
						}
						if (i == 25) {
							F25 = F25.add(bd);
						}
						break;
					case 22:
						makeExcel.setValue(row, col, getBigDecimal(value), "#,##0.0000", "R");
						break;
					default:
						makeExcel.setValue(row, col, value.isEmpty() ? " " : value);
						break;
					}
				} // for

				row++;
			} // for
			makeExcel.setValue(1, 26, F25, "#,##0");
			makeExcel.setValue(1, 5, row - 2);
			makeExcel.setValue(1, 8, F7, "#,##0");

		}

		long sno = makeExcel.close();
		makeExcel.toExcel(sno);
	}

}
