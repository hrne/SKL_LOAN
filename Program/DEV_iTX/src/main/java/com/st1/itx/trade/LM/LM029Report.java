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

@Component
@Scope("prototype")
public class LM029Report extends MakeReport {
	// private static final Logger logger = LoggerFactory.getLogger(LM029Report.class);

	@Autowired
	LM029ServiceImpl lM029ServiceImpl;

	@Autowired
	MakeExcel makeExcel;

	public void exec(TitaVo titaVo) throws LogicException {

		List<Map<String, String>> listLM029 = null;

		try {
			listLM029 = lM029ServiceImpl.findAll(titaVo);
			exportExcel(titaVo, listLM029);

		} catch (Exception e) {
			StringWriter errors = new StringWriter();
			e.printStackTrace(new PrintWriter(errors));
			this.error("LM029ServiceImpl findAll error = " + errors.toString());
		}
	}

	private void exportExcel(TitaVo titaVo, List<Map<String, String>> listLM029) throws LogicException {
		this.info("LM029Report exportExcel");

		makeExcel.open(titaVo, titaVo.getEntDyI(), titaVo.getKinbr(), "LM029", "放款餘額明細表", "LM029-放款餘額明細表",
				"LM029-放款餘額明細表.xlsx", "la$w30p");

		if (listLM029.size() == 0) {

			makeExcel.setValue(2, 1, "本日無資料", "L");

		} else {

			int row = 2;

			BigDecimal loanBalTotal = BigDecimal.ZERO;

			for (Map<String, String> tLDVo : listLM029) {

				for (int i = 0; i < tLDVo.size(); i++) {

					String fieldValue = tLDVo.get("F" + i);

					int col = i + 1;

					switch (col) {
					case 1:
					case 2:
					case 3:
						makeExcel.setValue(row, col, fieldValue,"#0");
						break;
					case 7:
					case 8:
					case 11:
					case 12:
						if (fieldValue != null && !fieldValue.isEmpty() && !fieldValue.equals("0")) {
							makeExcel.setValue(row, col + 1, showBcDate(fieldValue, 0), "C");
						}
						break;
					case 9:
						BigDecimal rate = new BigDecimal(fieldValue);

						makeExcel.setValue(row, col + 1, rate, "0.0000", "R");
						break;
					case 20:
					case 21:
					case 22:

						BigDecimal amt = new BigDecimal(fieldValue);

						makeExcel.setValue(row, col + 1, amt, "#,##0", "R");

						if (col == 22) {
							loanBalTotal = loanBalTotal.add(amt);
						}
						break;
					case 26:
						makeExcel.setValue(row, 4, fieldValue, "L"); // 帳冊別
						break;
					default:
						makeExcel.setValue(row, col + 1, fieldValue, "L");
						break;
					}
				} // for

				row++;
			} // for

			// 印放款餘額總計
			// makeExcel.setValue(1, 23, loanBalTotal, "#,##0", "R");

			makeExcel.formulaCaculate(1, 23);
		}

		long sno = makeExcel.close();
		makeExcel.toExcel(sno);
	}

}
