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
import com.st1.itx.db.service.springjpa.cm.LM050ServiceImpl;
import com.st1.itx.util.common.MakeExcel;
import com.st1.itx.util.common.MakeReport;

@Component
@Scope("prototype")

public class LM050Report extends MakeReport {
	private static final Logger logger = LoggerFactory.getLogger(LM050Report.class);

	@Autowired
	LM050ServiceImpl lM050ServiceImpl;

	@Autowired
	MakeExcel makeExcel;

	BigDecimal equity = BigDecimal.ZERO;
	BigDecimal empbal = BigDecimal.ZERO;
	BigDecimal loanbal = BigDecimal.ZERO;
	BigDecimal divamt = BigDecimal.ZERO;
	BigDecimal tot = BigDecimal.ZERO;
	int row = 3;

	public void exec(TitaVo titaVo) throws LogicException {

		makeExcel.open(titaVo, titaVo.getEntDyI(), titaVo.getKinbr(), "LM050", "放款保險法第3條利害關係人放款餘額表_限額控管",
				"LM050_放款保險法第3條利害關係人放款餘額表_限額控管", "LM050放款保險法第3條利害關係人放款餘額表_限額控管.xlsx", "108.04");

		// 取得民國年帳務日
		String entdy = titaVo.getEntDy();

		makeExcel.setSheet("108.04", entdy.substring(1, 4) + "." + entdy.substring(4, 6));
		makeExcel.setValue(1, 2, entdy.substring(1, 4) + "年" + entdy.substring(4, 6) + "月" + entdy.substring(6, 8)
				+ "日依「保險業利害關係人放款管理辦法」第3條利害關係人放款餘額表");
		makeExcel.setValue(2, 4,
				entdy.substring(1, 4) + "." + entdy.substring(4, 6) + "." + entdy.substring(6, 8) + " 淨值（核閱數）");

		List<Map<String, String>> equityList = null;

		try {
			equityList = lM050ServiceImpl.fnEquity(titaVo);

		} catch (Exception e) {
			StringWriter errors = new StringWriter();
			e.printStackTrace(new PrintWriter(errors));
			this.error("LM050ServiceImpl.fnEquity error = " + errors.toString());
		}

		if (equityList.size() > 0) {

			String value = equityList.get(0).get("F0");

			BigDecimal amt = value == null || value.isEmpty() ? BigDecimal.ZERO : new BigDecimal(value);

			equity = amt;
			makeExcel.setValue(2, 6, amt, "#,##0");
		}

		divamt = equity;

		fnAll(titaVo);

		long sno = makeExcel.close();

		makeExcel.toExcel(sno);
	}

	private void fnAll(TitaVo titaVo) throws LogicException {

		List<Map<String, String>> listLM050 = null;
		try {
			listLM050 = lM050ServiceImpl.findAll(titaVo);

		} catch (Exception e) {
			StringWriter errors = new StringWriter();
			e.printStackTrace(new PrintWriter(errors));
			this.error("LM050ServiceImpl.fnall error = " + errors.toString());
		}

		exportExcel(listLM050);
	}

	private void exportExcel(List<Map<String, String>> listLM050) throws LogicException {
		this.info("LM050Report exportExcel");

		if (listLM050.size() == 0) {
			makeExcel.setValue(4, 1, "本日無資料");
		}

		String type = "";

		for (Map<String, String> tLDVo : listLM050) {
			type = tLDVo.get("F6");

			if (type.equals("1")) {

				empbal = tLDVo.get("F2") == null ? BigDecimal.ZERO : new BigDecimal(tLDVo.get("F2"));

			} else if (type.equals("0")) {

				loanbal = tLDVo.get("F2") == null ? BigDecimal.ZERO : new BigDecimal(tLDVo.get("F2"));

			} else {
				row++;

				for (int i = 0; i < tLDVo.size(); i++) {

					String value = tLDVo.get("F" + i);

					switch (i) {
					case 0:

						makeExcel.setValue(row, i + 2, value);

						break;
					case 2:

						BigDecimal amt = value == null || value.isEmpty() ? BigDecimal.ZERO : new BigDecimal(value);

						makeExcel.setValue(row, i + 2, this.computeDivide(amt, new BigDecimal("1000"), 0), "#,##0");

						tot = tot.add(amt);

						break;
					case 3:

						String tmpValue = tLDVo.get("F2");

						BigDecimal tmpAmt = tmpValue == null || tmpValue.isEmpty() ? BigDecimal.ZERO
								: new BigDecimal(tmpValue);

						makeExcel.setValue(row, i + 2, this.computeDivide(tmpAmt, divamt, 4), "##0.00%");

						break;
					case 4:
						if (value.equals("1")) {
							makeExcel.setValue(row, i + 2, "10%");
						} else {
							makeExcel.setValue(row, i + 2, "2%");
						}
						break;
					case 6:
					case 7:
						break;
					default:
						makeExcel.setValue(row, i + 2, value);
						break;
					}
				}
			}
		}
		printTotal();

	}

	private void printTotal() throws LogicException {
		row++;
		makeExcel.setValue(row, 2, "合     計");
		makeExcel.setValue(row, 4, tot, "#,##0");
		makeExcel.setValue(row, 5, this.computeDivide(tot, divamt, 4), "##0.00%");
		makeExcel.setValue(row, 6, "30%", "R");

		row++;
		makeExcel.setValue(row, 2, "職    員");
		makeExcel.setValue(row, 4, empbal, "#,##0");

		row++;
		tot = tot.add(empbal);
		makeExcel.setValue(row, 2, "關 係 人 放 款 總 額");
		makeExcel.setValue(row, 4, tot, "#,##0");
		makeExcel.setValue(row, 5, this.computeDivide(tot, divamt, 4), "##0.00%");
		makeExcel.setValue(row, 6, "150%", "R");

		row++;
		divamt = loanbal;
		divamt = divamt.add(tot);
		makeExcel.setValue(row, 2, "關 係 人 放 款 總 額");
		makeExcel.setValue(row, 4, this.computeDivide(tot, divamt, 4), "##0.00%");

		row++;
		makeExcel.setValue(row, 2, "一 般 客 戶合計");
		makeExcel.setValue(row, 4, loanbal, "#,##0");

		row++;
		makeExcel.setValue(row, 2, "佔 總 放 款 比");
		makeExcel.setValue(row, 4, this.computeDivide(loanbal, divamt, 4), "##0.00%");

		row++;
		makeExcel.setValue(row, 2, "放 款 總 額  ＊");
		makeExcel.setValue(row, 4, divamt, "#,##0");

	}

}
