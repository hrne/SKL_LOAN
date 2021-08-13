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
import com.st1.itx.db.service.springjpa.cm.LM033ServiceImpl;
import com.st1.itx.util.common.MakeExcel;
import com.st1.itx.util.common.MakeReport;

@Component
@Scope("prototype")

public class LM033Report extends MakeReport {
	// private static final Logger logger = LoggerFactory.getLogger(LM033Report.class);

	@Autowired
	LM033ServiceImpl lM033ServiceImpl;

	@Autowired
	MakeExcel makeExcel;

	public Boolean exec(TitaVo titaVo) throws LogicException {

		List<Map<String, String>> listLM033 = null;

		try {
			listLM033 = lM033ServiceImpl.doQuery(titaVo);
		} catch (Exception e) {
			StringWriter errors = new StringWriter();
			e.printStackTrace(new PrintWriter(errors));
			this.error("LM033ServiceImpl.findAll error = " + errors.toString());
			return false;
		}

		exportExcel(titaVo, listLM033);
		return true;
	}

	private void exportExcel(TitaVo titaVo, List<Map<String, String>> listLM033) throws LogicException {
		this.info("LM033Report exportExcel");

		makeExcel.open(titaVo, titaVo.getEntDyI(), titaVo.getKinbr(), "LM033", "新撥案件明細", "LM033-新撥案件明細",
				"LM033-新撥案件明細.xlsx", "D9701211");

		int listSize = listLM033.size();

		if (listSize == 0) {
			makeExcel.setValue(2, 1, "本日無資料");
		} else {

			// 從第二列開始印資料
			int row = 2;

			for (Map<String, String> tLDVo : listLM033) {

				int col = 0;

				for (int i = 0; i < tLDVo.size(); i++) {

					String value = tLDVo.get("F" + col);

					col++;

					switch (col) {
					case 2: // 申請日期
					case 3: // 准駁日期
					case 10: // 循環動用期限

						// 日期類,顯示西曆日期yyyymmdd,無值時顯示0

						if (!value.isEmpty() && !value.equals("0")) {
							makeExcel.setValue(row, col, showBcDate(value, 2), "C");
						} else {
							makeExcel.setValue(row, col, value);
						}
						break;
					case 6: // 核准金額
					case 7: // 撥款金額
					case 8: // 已用額度
						// 金額
						BigDecimal amt = value == null || value.isEmpty() ? BigDecimal.ZERO : new BigDecimal(value);

						makeExcel.setValue(row, col, amt, "0", "R");
						break;
					case 12: // 利率
						BigDecimal loanRate = value == null || value.isEmpty() ? BigDecimal.ZERO
								: new BigDecimal(value);

						makeExcel.setValue(row, col, loanRate, "0.0000", "R");
						break;
					case 15: // 已刪除的欄位,不顯示
						break;
					default:
						makeExcel.setValue(row, col, value);
						break;
					}
				} // for
				row++;
			} // for
		}
		
		makeExcel.formulaCaculate(1,4);
		makeExcel.formulaCaculate(1,6);
		makeExcel.formulaCaculate(1,7);
		
		long sno = makeExcel.close();
		makeExcel.toExcel(sno);
	}

}
