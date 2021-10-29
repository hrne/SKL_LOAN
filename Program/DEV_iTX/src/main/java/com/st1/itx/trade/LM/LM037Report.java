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
import com.st1.itx.db.service.springjpa.cm.LM037ServiceImpl;
import com.st1.itx.util.common.MakeExcel;
import com.st1.itx.util.common.MakeReport;
import com.st1.itx.util.parse.Parse;

@Component
@Scope("prototype")

public class LM037Report extends MakeReport {

	@Autowired
	LM037ServiceImpl lM037ServiceImpl;

	@Autowired
	MakeExcel makeExcel;
	
	@Autowired
	Parse parse;

	@Override
	public void printTitle() {

	}

	public void exec(TitaVo titaVo) throws LogicException {

		List<Map<String, String>> LM037List = null;

		try {

			LM037List = lM037ServiceImpl.findAll(titaVo);
			exportExcel(titaVo, LM037List);

		} catch (Exception e) {
			// TODO Auto-generated catch block
			StringWriter errors = new StringWriter();
			e.printStackTrace(new PrintWriter(errors));
			this.info("LM037ServiceImpl.testExcel error = " + errors.toString());
		}
	}

	private void exportExcel(TitaVo titaVo, List<Map<String, String>> LMList) throws LogicException {
		this.info("LM037Report exportExcel");
		makeExcel.open(titaVo, titaVo.getEntDyI(), titaVo.getKinbr(), "LM037", "地區別催收總金額", "LM037地區別催收總金額", "LM037地區別催收總金額.xlsx", "D9206092");
		if (LMList.size() == 0) {
			makeExcel.setValue(3, 1, "本日無資料");
		}
		int row = 3;
		String lastEntCode = "";
		int set0 = 0;
		int set1 = 0;
		BigDecimal firstTotal = BigDecimal.ZERO;
		BigDecimal secondTotal = BigDecimal.ZERO;
		BigDecimal thirdTotal = BigDecimal.ZERO;
		for (Map<String, String> tLDVo : LMList) {
			int col = 0;
			for (int i = 0; i <= 2; i++) {

				String value = tLDVo.get("F" + i);
				col++;
				switch (i) {
				case 0:
					if (row == 3) {
						lastEntCode = value;
					} else {
						if (!value.equals(lastEntCode)) {
							makeExcel.setValue(row, 1, lastEntCode);
							makeExcel.setValue(row, 2, "0");
							if (lastEntCode.contentEquals("0")) {
								makeExcel.setValue(row, 3, firstTotal, "#,##0");
								set0 = 1;
							} else if (lastEntCode.contentEquals("1")) {
								makeExcel.setValue(row, 3, secondTotal, "#,##0");
								set1 = 1;
							}
							row++;
						}
					}
					lastEntCode = value;
					makeExcel.setValue(row, col, value);
					break;
				case 2:
					// 金額
					BigDecimal bd = getBigDecimal(value);
					makeExcel.setValue(row, col, bd, "#,##0", "R");
					if (tLDVo.get("F0").equals("0")) {
						firstTotal = firstTotal.add(bd);
					} else if (tLDVo.get("F0").equals("1")) {
						secondTotal = secondTotal.add(bd);
					} else if (tLDVo.get("F0").equals("2")) {
						thirdTotal = thirdTotal.add(bd);
					}
					break;
				default:
					makeExcel.setValue(row, col, parse.isNumeric(value) ? parse.stringToBigDecimal(value) : 0);
					break;
				}
			} // for

			row++;
		} // for
		if (set0 == 0 && firstTotal.compareTo(BigDecimal.ZERO) > 0) {
			makeExcel.setValue(row, 1, "0");
			makeExcel.setValue(row, 2, "0");
			makeExcel.setValue(row, 3, firstTotal, "#,##0");
			row++;
		}
		if (set1 == 0 && secondTotal.compareTo(BigDecimal.ZERO) > 0) {
			makeExcel.setValue(row, 1, "0");
			makeExcel.setValue(row, 2, "0");
			makeExcel.setValue(row, 3, secondTotal, "#,##0");
			row++;
		}
		if (thirdTotal.compareTo(BigDecimal.ZERO) > 0) {
			makeExcel.setValue(row, 1, "2");
			makeExcel.setValue(row, 2, "0");
			makeExcel.setValue(row, 3, thirdTotal, "#,##0");
			row++;
		}
		makeExcel.setValue(row, 2, "0");
		makeExcel.setValue(row, 3, thirdTotal.add(secondTotal.add(firstTotal)), "#,##0");

		long sno = makeExcel.close();
		makeExcel.toExcel(sno);
	}

}
