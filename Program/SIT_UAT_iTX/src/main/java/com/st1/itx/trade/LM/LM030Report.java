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
import com.st1.itx.db.service.springjpa.cm.LM030ServiceImpl;
import com.st1.itx.util.common.MakeExcel;
import com.st1.itx.util.common.MakeReport;
import com.st1.itx.util.date.DateUtil;
import com.st1.itx.util.format.FormatUtil;
import com.st1.itx.util.parse.Parse;

@Component
@Scope("prototype")
public class LM030Report extends MakeReport {

	@Autowired
	LM030ServiceImpl lM030ServiceImpl;

	@Autowired
	MakeExcel makeExcel;

	@Autowired
	DateUtil dateUtil;

	@Autowired
	Parse parse;

	public Boolean exec(TitaVo titaVo) throws LogicException {

		List<Map<String, String>> listLM030 = null;

		try {
			listLM030 = lM030ServiceImpl.findAll(titaVo);
			exportExcel(titaVo, listLM030);

		} catch (Exception e) {
			StringWriter errors = new StringWriter();
			e.printStackTrace(new PrintWriter(errors));
			this.info("LM030ServiceImpl.testExcel error = " + errors.toString());
			return false;
		}

		return true;
	}

	private void exportExcel(TitaVo titaVo, List<Map<String, String>> listLM030) throws LogicException {
		this.info("LM030Report exportExcel");

		makeExcel.open(titaVo, titaVo.getEntDyI(), titaVo.getKinbr(), "LM030", "轉催收明細總表", "LM030轉催收案件明細_核定總表",
				"LM030轉催收案件明細_核定總表.xlsx", "11005");
		String yy = titaVo.get("ENTDY").substring(1, 4);
		String mm = titaVo.get("ENTDY").substring(4, 6);
		makeExcel.setSheet("11005", yy + mm);
		makeExcel.setValue(1, 1, yy + "." + mm + "  轉催收明細總表");
		BigDecimal total = BigDecimal.ZERO;

		int row = 3;

		if (listLM030 == null || listLM030.isEmpty()) {
			makeExcel.setValue(3, 1, "本日無資料");
		} else {
			for (Map<String, String> tLDVo : listLM030) {

				String value = "";
				int col = 0;
				for (int i = 0; i <= 11; i++) {

					value = tLDVo.get("F" + i);
					col++;
					switch (i) {
					case 5:
					case 10:
					case 11:
						makeExcel.setValue(row, col,
								parse.isNumeric(value) ? parse.stringToInteger(this.showRocDate(value, 3)) : value);
						break;
					case 6:
					case 7:
						// 金額
						BigDecimal bd = getBigDecimal(value);
						makeExcel.setValue(row, col, bd, "#,##0");
						total = total.add(bd);
						break;
					case 8:
						// 利率
						makeExcel.setValue(row, col, getBigDecimal(value), "#,##0.0000");
						break;
					case 9:
						makeExcel.setValue(row, col,
								parse.isNumeric(value) ? parse.stringToInteger(this.showRocDate(value, 3)) : value);
						break;
					default:
						makeExcel.setValue(row, col, parse.isNumeric(value) ? parse.stringToBigDecimal(value) : value);
						break;
					}
				} // for
				row++;
			} // for
		}

		row += 10;

		// block 1
		makeExcel.setFontType(1);
		makeExcel.setSize(14);
		makeExcel.setIBU("B");
		makeExcel.setValue(row, 1, "放款管理課", "C");
		// block 2
		makeExcel.setFontType(1);
		makeExcel.setSize(14);
		makeExcel.setIBU("B");
		makeExcel.setValue(row, 6, "放款服務課", "C");
		// block 3
		makeExcel.setFontType(1);
		makeExcel.setSize(14);
		makeExcel.setIBU("B");
		makeExcel.setValue(row, 10, "部室主管", "C");
		// block 4
		makeExcel.setFontType(1);
		if (listLM030 == null || listLM030.isEmpty()) {
			makeExcel.setValue(row + 1, 1, "一、經查本月逾期放款，無清償期屆滿六個\r\n" + "    月需轉列催收款之案件。\r\n" + "二、陳核。 ");
		} else {
			makeExcel.setValue(row + 1, 1, "一、經查本月逾期放款清償期屆滿六個月案件\r\n" + "    ，依規將本金及應收利息轉列催收款項，\r\n" + "    金額共計 $"
					+ FormatUtil.formatAmt(total, 0) + "元。");
			makeExcel.setValue(row + 2, 1, "二、本月轉入催收款案件未發生『撥款後繳款\r\n" + "    期數未滿18個月即轉入催收戶』之情事。\r\n" + "三、陳核。 ");
		}
		makeExcel.setColor("RED");
		makeExcel.setValue(row + 3, 5, listLM030.size(), "C");
		makeExcel.setColor("RED");
		makeExcel.setValue(row + 3, 7, total, "#,##0");
		makeExcel.setValue(row + 5, 1, "一、李案為年期延長後再協議案件，因未符合免列報逾放條件，故轉列催收款項。");
		makeExcel.setValue(row + 13, 5, listLM030.size());
		makeExcel.setValue(row + 13, 7, total, "#,##0");

		makeExcel.close();
		// makeExcel.toExcel(sno);
	}

}
