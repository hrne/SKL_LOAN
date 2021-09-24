package com.st1.itx.trade.LM;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.db.service.springjpa.cm.LM046ServiceImpl;
import com.st1.itx.util.common.MakeExcel;
import com.st1.itx.util.common.MakeReport;

@Component
@Scope("prototype")

public class LM046Report extends MakeReport {

	@Autowired
	LM046ServiceImpl lM046ServiceImpl;

	@Autowired
	MakeExcel makeExcel;

	BigDecimal amtUnit = new BigDecimal("1000");

	@Override
	public void printTitle() {

	}

	private BigDecimal safelyDivide(BigDecimal divided, BigDecimal divisor, int scale) {
		if (divided.compareTo(BigDecimal.ZERO) == 0 || divisor.compareTo(BigDecimal.ZERO) == 0) {
			return BigDecimal.ZERO;
		} else {
			return divided.divide(divisor, scale, RoundingMode.HALF_EVEN);
		}
	}

	private BigDecimal safelyDivide(String divided, String divisor, int scale) {
		return safelyDivide(new BigDecimal(divided), new BigDecimal(divisor), scale);
	}

	private BigDecimal safelyDivide(String divided, int divisor, int scale) {
		return safelyDivide(new BigDecimal(divided), BigDecimal.valueOf(divisor), scale);
	}

	private BigDecimal safelyDivide(BigDecimal divided, int divisor, int scale) {
		return safelyDivide(divided, BigDecimal.valueOf(divisor), scale);
	}

	public void exec(TitaVo titaVo) throws LogicException {

		makeExcel.open(titaVo, titaVo.getEntDyI(), titaVo.getKinbr(), "LM046", "年度擔保放款信用風險分析_內部控管", "LM046_年度擔保放款信用風險分析_內部控管", "LM046年度擔保放款信用風險分析_內部控管.xlsx", "衡式");

		String yy = titaVo.get("ENTDY").substring(1, 4);
		String mm = titaVo.get("ENTDY").substring(4, 6);

		makeExcel.setValue(1, 1, yy + "年度 擔保放款信用風險分析");
		makeExcel.setValue(4, 1, yy + "年" + mm);
		makeExcel.setValue(9, 1, yy + "年" + mm);

		List<Map<String, String>> LM046List = null;
		try {
			LM046List = lM046ServiceImpl.findAll(titaVo);

		} catch (Exception e) {
			StringWriter errors = new StringWriter();
			e.printStackTrace(new PrintWriter(errors));
			this.info("LM046ServiceImpl.testExcel error = " + errors.toString());
		}
		exportExcel(titaVo, LM046List);
		long sno = makeExcel.close();
		makeExcel.toExcel(sno);
	}

	private void exportExcel(TitaVo titaVo, List<Map<String, String>> LDList) throws LogicException {
		this.info("LM046Report exportExcel");
		if (LDList.size() == 0) {
			makeExcel.setValue(2, 5, "本日無資料");
			return;
		}

		int count = 0;

		for (Map<String, String> tLDVo : LDList) {
			// 第一區塊
			makeExcel.setValue(4 + count, 1, tLDVo.get("F0"));
			makeExcel.setValue(4 + count, 3, safelyDivide(tLDVo.get("F2"), 1000, 0).add(safelyDivide(tLDVo.get("F8"), 1000, 0)), "#,##0");
			makeExcel.setValue(4 + count, 6,
					safelyDivide(tLDVo.get("F10"), 1000, 0).add(safelyDivide(tLDVo.get("F12"), 1000, 0)).add(safelyDivide(tLDVo.get("F4"), 1000, 0)).add(safelyDivide(tLDVo.get("F6"), 1000, 0)),
					"#,##0");
			makeExcel.setValue(4 + count, 9,
					safelyDivide(tLDVo.get("F3"), 1, 0).add(safelyDivide(tLDVo.get("F5"), 1, 0)).add(safelyDivide(tLDVo.get("F9"), 1, 0).add(safelyDivide(tLDVo.get("F11"), 1, 0))), "#,##0");
			makeExcel.setValue(4 + count, 11,
					safelyDivide(safelyDivide(tLDVo.get("F10"), 1, 5).add(safelyDivide(tLDVo.get("F12"), 1, 5)).add(safelyDivide(tLDVo.get("F4"), 1, 5)).add(safelyDivide(tLDVo.get("F6"), 1, 5)),
							safelyDivide(tLDVo.get("F2"), 1, 5).add(safelyDivide(tLDVo.get("F8"), 1, 0)), 5),
					"0.00%");

			// 第二區塊

			// 法人
			makeExcel.setValue(19 + count * 3, 1, tLDVo.get("F0"));
			makeExcel.setValue(19 + count * 3, 3, new BigDecimal(tLDVo.get("F1")), "#,##0");
			makeExcel.setValue(19 + count * 3, 4, safelyDivide(tLDVo.get("F2"), 1000, 0));
			makeExcel.setValue(19 + count * 3, 5, new BigDecimal(tLDVo.get("F3")), "#,##0");
			makeExcel.setValue(19 + count * 3, 6, safelyDivide(tLDVo.get("F3"), tLDVo.get("F1"), 5), "0.00%");
			makeExcel.setValue(19 + count * 3, 7, safelyDivide(tLDVo.get("F4"), 1000, 0));
			makeExcel.setValue(19 + count * 3, 8, safelyDivide(tLDVo.get("F4"), tLDVo.get("F2"), 5), "0.00%");
			makeExcel.setValue(19 + count * 3, 9, new BigDecimal(tLDVo.get("F5")), "#,##0");
			makeExcel.setValue(19 + count * 3, 10, safelyDivide(tLDVo.get("F5"), tLDVo.get("F1"), 5), "0.00%");
			makeExcel.setValue(19 + count * 3, 11, safelyDivide(tLDVo.get("F6"), 1000, 0));
			makeExcel.setValue(19 + count * 3, 12, safelyDivide(tLDVo.get("F6"), tLDVo.get("F2"), 5), "0.00%");

			// 自然人
			makeExcel.setValue(19 + count * 3 + 1, 3, new BigDecimal(tLDVo.get("F7")), "#,##0");
			makeExcel.setValue(19 + count * 3 + 1, 4, safelyDivide(tLDVo.get("F8"), 1000, 0));
			makeExcel.setValue(19 + count * 3 + 1, 5, new BigDecimal(tLDVo.get("F9")), "#,##0");
			makeExcel.setValue(19 + count * 3 + 1, 6, safelyDivide(tLDVo.get("F9"), tLDVo.get("F7"), 5), "0.00%");
			makeExcel.setValue(19 + count * 3 + 1, 7, safelyDivide(tLDVo.get("F10"), 1000, 0));
			makeExcel.setValue(19 + count * 3 + 1, 8, safelyDivide(tLDVo.get("F10"), tLDVo.get("F8"), 5), "0.00%");
			makeExcel.setValue(19 + count * 3 + 1, 9, new BigDecimal(tLDVo.get("F11")), "#,##0");
			makeExcel.setValue(19 + count * 3 + 1, 10, safelyDivide(tLDVo.get("F11"), tLDVo.get("F7"), 5), "0.00%");
			makeExcel.setValue(19 + count * 3 + 1, 11, safelyDivide(tLDVo.get("F12"), 1000, 0));
			makeExcel.setValue(19 + count * 3 + 1, 12, safelyDivide(tLDVo.get("F12"), tLDVo.get("F8"), 5), "0.00%");

			// 小計
			BigDecimal[] totals = new BigDecimal[6];
			totals[0] = new BigDecimal(tLDVo.get("F1")).add(new BigDecimal(tLDVo.get("F7")));
			totals[1] = safelyDivide(new BigDecimal(tLDVo.get("F2")).add(new BigDecimal(tLDVo.get("F8"))), 1000, 0);
			totals[2] = new BigDecimal(tLDVo.get("F3")).add(new BigDecimal(tLDVo.get("F9")));
			totals[3] = safelyDivide(new BigDecimal(tLDVo.get("F4")).add(new BigDecimal(tLDVo.get("F10"))), 1000, 0);
			totals[4] = new BigDecimal(tLDVo.get("F5")).add(new BigDecimal(tLDVo.get("F11")));
			totals[5] = safelyDivide(new BigDecimal(tLDVo.get("F6")).add(new BigDecimal(tLDVo.get("F12"))), 1000, 0);

			makeExcel.setValue(19 + count * 3 + 2, 3, totals[0], "#,##0");
			makeExcel.setValue(19 + count * 3 + 2, 4, totals[1]);
			makeExcel.setValue(19 + count * 3 + 2, 5, totals[2], "#,##0");
			makeExcel.setValue(19 + count * 3 + 2, 6, safelyDivide(totals[2], totals[0], 5), "0.00%");
			makeExcel.setValue(19 + count * 3 + 2, 7, totals[3], "#,##0");
			makeExcel.setValue(19 + count * 3 + 2, 8, safelyDivide(totals[3], totals[1], 5), "0.00%");
			makeExcel.setValue(19 + count * 3 + 2, 9, totals[4], "#,##0");
			makeExcel.setValue(19 + count * 3 + 2, 10, safelyDivide(totals[4], totals[0], 5), "0.00%");
			makeExcel.setValue(19 + count * 3 + 2, 11, totals[5], "#,##0");
			makeExcel.setValue(19 + count * 3 + 2, 12, safelyDivide(totals[5], totals[1], 5), "0.00%");

			// 結束
			count++;
		}

		for (int r = 21; r <= 51; r += 3) {
			for (int c = 3; c <= 12; c++) {
				makeExcel.formulaCaculate(r, c);
			}
		}

	}
}
