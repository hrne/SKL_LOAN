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

	@Autowired
	LM029ServiceImpl lM029ServiceImpl;

	@Autowired
	MakeExcel makeExcel;

	/**
	 * 執行報表輸出
	 * 
	 * @param titaVo
	 * @param yearMonth 西元年月
	 * 
	 */
	public void exec(TitaVo titaVo, int yearMonth) throws LogicException {

		List<Map<String, String>> listLM029 = null;

		try {
			listLM029 = lM029ServiceImpl.findAll(titaVo, yearMonth);
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
				"LM029_底稿_放款餘額明細表.xlsx", "la$w30p");
		
		String today = dDateUtil.getNowStringBc();
		
		// 表頭
		makeExcel.setValue(2, 20, "日　　期：" + this.showBcDate(today, 1));
		makeExcel.setValue(3, 20, "時　　間：" + dDateUtil.getNowStringTime().substring(0, 2) + ":"
				+ dDateUtil.getNowStringTime().substring(2, 4) + ":" + dDateUtil.getNowStringTime().substring(4, 6));

		
		
		if (listLM029 == null || listLM029.isEmpty()) {

			makeExcel.setValue(6, 1, "本日無資料", "L");

		} else {
			

			int row = 6;

			for (Map<String, String> tLDVo : listLM029) {

				for (int i = 0; i <= 25; i++) {

					String fieldValue = tLDVo.get("F" + i);

					int col = i + 1;

					switch (i) {
					case 0:
					case 1:
					case 2:
						makeExcel.setValue(row, col, fieldValue, "#0");
						break;
					case 6:
					case 7:
					case 10:
					case 11:
						if (fieldValue != null && !fieldValue.isEmpty() && !fieldValue.equals("0")) {
							makeExcel.setValue(row, col + 1, showBcDate(fieldValue, 0), "C");
						}
						break;
					case 8:
						BigDecimal rate = getBigDecimal(fieldValue);
						makeExcel.setValue(row, col + 1, rate, "0.0000", "R");
						break;
					case 13:
					case 14:
					case 15:
						BigDecimal amt = getBigDecimal(fieldValue);
						makeExcel.setValue(row, col + 1, amt, "#,##0", "R");
						break;
					case 16:
						makeExcel.setValue(row, 4, "00A".equals(fieldValue) ? "利變帳冊年金-" : "傳統帳冊" + fieldValue, "L"); // 帳冊別
						break;
					case 17:
						makeExcel.setValue(row, col, fieldValue, "L"); // 餘期數
						break;
					case 18:
					case 19:
					case 20:
						makeExcel.setValue(row, col, fieldValue, "L"); // 餘期數
						break;
					default:
						makeExcel.setValue(row, col + 1, fieldValue, "L");
						break;
					}
				} // for

				row++;
			} // for

			// 放款餘額總計的 excel formula
			makeExcel.formulaCaculate(5, 17);
		}

		makeExcel.close();

	}

}
