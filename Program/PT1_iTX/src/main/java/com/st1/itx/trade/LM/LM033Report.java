package com.st1.itx.trade.LM;

import java.io.PrintWriter;
import java.io.StringWriter;
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

		makeExcel.open(titaVo, titaVo.getEntDyI(), titaVo.getKinbr(), "LM033", "新撥案件明細", "LM033-新撥案件明細", "LM033-新撥案件明細.xlsx", "D9701211");

		if (listLM033 == null || listLM033.isEmpty()) {
			makeExcel.setValue(2, 1, "本日無資料");
		} else {

			// 從第二列開始印資料
			int row = 2;

			for (Map<String, String> tLDVo : listLM033) {

				for (int i = 0; i <= 13; i++) {

					String value = tLDVo.get("F" + i);
					int col = i + 1;

					switch (i) {
					case 1: // 申請日期
					case 2: // 准駁日期
					case 9: // 循環動用期限

						// 日期類,顯示西曆日期yyyymmdd,無值時顯示0

						if (value != null && !value.isEmpty() && !value.equals("0")) {
							makeExcel.setValue(row, col, showBcDate(value, 2), "C");
						} else {
							makeExcel.setValue(row, col, "0");
						}
						break;
					case 5: // 核准金額
					case 6: // 撥款金額
					case 7: // 已用額度
						// 金額
						makeExcel.setValue(row, col, getBigDecimal(value), "0", "R");
						break;
					case 11: // 利率
						makeExcel.setValue(row, col, getBigDecimal(value), "0.0000", "R");
						break;
					case 14: // 已刪除的欄位,不顯示
						break;
					default:
						makeExcel.setValue(row, col, value);
						break;
					}
				} // for
				row++;
			} // for
		}

		makeExcel.formulaCaculate(1, 4);
		makeExcel.formulaCaculate(1, 6);
		makeExcel.formulaCaculate(1, 7);

		makeExcel.close();
		//makeExcel.toExcel(sno);
	}

}
