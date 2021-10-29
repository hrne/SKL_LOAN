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
import com.st1.itx.util.parse.Parse;

@Component
@Scope("prototype")

public class LM046Report extends MakeReport {

	@Autowired
	LM046ServiceImpl lM046ServiceImpl;

	@Autowired
	MakeExcel makeExcel;
	
	@Autowired
	Parse parse;
	
	static final BigDecimal thousand = new BigDecimal("1000");
	static final BigDecimal hundred = new BigDecimal("100");

	@Override
	public void printTitle() {

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
		StringBuilder sb = new StringBuilder(); // defined out here for setLength() instead of new StringBuilder() every iteration

		for (Map<String, String> tLDVo : LDList) {
			// 第一區塊
			
			BigDecimal[] bd = new BigDecimal[13];
			
			sb.setLength(0);
			
			// F0 is formatted date string, hence not used in bd[]
			for (int i = 1; i <= 12; i++)
			{
				bd[i] = getBigDecimal(tLDVo.get("F" + i));
				sb.append("F" + i + ": " + bd.toString() + "; ");
			}
			
			this.info("LM046 result check: ");
			this.info(sb.toString());
			
			makeExcel.setValue(4 + count, 1, tLDVo.get("F0"));
			makeExcel.setValue(4 + count, 3, computeDivide(bd[2].add(bd[8]), thousand, 0), "#,##0");
			makeExcel.setValue(4 + count, 6,
					computeDivide(
					 bd[4].add(bd[6]).add(bd[10]).add(bd[12])
					,thousand
					,0),
					"#,##0");
			makeExcel.setValue(4 + count, 9,
					bd[3].add(bd[5]).add(bd[9]).add(bd[11]), "#,##0");
			makeExcel.setValue(4 + count, 11,
					computeDivide(
  							  bd[4].add(bd[6]).add(bd[10]).add(bd[12])
							, bd[2].add(bd[8])
							, 5),
					"0.00%");

			// 第二區塊

			// 法人
			makeExcel.setValue(19 + count * 3, 1, tLDVo.get("F0"));
			makeExcel.setValue(19 + count * 3, 3, bd[1], "#,##0");
			makeExcel.setValue(19 + count * 3, 4, computeDivide(bd[2], thousand, 0));
			makeExcel.setValue(19 + count * 3, 5, bd[3], "#,##0");
			makeExcel.setValue(19 + count * 3, 6, computeDivide(bd[3], bd[1], 5), "0.00%");
			makeExcel.setValue(19 + count * 3, 7, computeDivide(bd[4], thousand, 0));
			makeExcel.setValue(19 + count * 3, 8, computeDivide(bd[4], bd[2], 5), "0.00%");
			makeExcel.setValue(19 + count * 3, 9, bd[5], "#,##0");
			makeExcel.setValue(19 + count * 3, 10, computeDivide(bd[5], bd[1], 5), "0.00%");
			makeExcel.setValue(19 + count * 3, 11, computeDivide(bd[6], thousand, 0));
			makeExcel.setValue(19 + count * 3, 12, computeDivide(bd[6], bd[2], 5), "0.00%");

			// 自然人
			makeExcel.setValue(19 + count * 3 + 1, 3, bd[7], "#,##0");
			makeExcel.setValue(19 + count * 3 + 1, 4, computeDivide(bd[8], thousand, 0));
			makeExcel.setValue(19 + count * 3 + 1, 5, bd[9], "#,##0");
			makeExcel.setValue(19 + count * 3 + 1, 6, computeDivide(bd[9], bd[7], 5), "0.00%");
			makeExcel.setValue(19 + count * 3 + 1, 7, computeDivide(bd[10], thousand, 0));
			makeExcel.setValue(19 + count * 3 + 1, 8, computeDivide(bd[10], bd[8], 5), "0.00%");
			makeExcel.setValue(19 + count * 3 + 1, 9, bd[11], "#,##0");
			makeExcel.setValue(19 + count * 3 + 1, 10, computeDivide(bd[11], bd[7], 5), "0.00%");
			makeExcel.setValue(19 + count * 3 + 1, 11, computeDivide(bd[12], thousand, 0));
			makeExcel.setValue(19 + count * 3 + 1, 12, computeDivide(bd[12], bd[8], 5), "0.00%");

			// 小計
			BigDecimal[] totals = new BigDecimal[6];
//			totals[0] = new BigDecimal(tLDVo.get("F1")).add(new BigDecimal(tLDVo.get("F7")));
//          ...
//			totals[5] = new BigDecimal(tLDVo.get("F6")).add(new BigDecimal(tLDVo.get("F12")));			
			for (int i = 0; i <= 5; i++)
			{
				totals[i] = bd[1+i].add(bd[7+i]);
			}

			makeExcel.setValue(19 + count * 3 + 2, 3, totals[0], "#,##0");
			makeExcel.setValue(19 + count * 3 + 2, 4, computeDivide(totals[1], thousand, 0));
			makeExcel.setValue(19 + count * 3 + 2, 5, totals[2], "#,##0");
			makeExcel.setValue(19 + count * 3 + 2, 6, computeDivide(totals[2], totals[0], 5).multiply(hundred).setScale(2, RoundingMode.HALF_UP) + "%");
			makeExcel.setValue(19 + count * 3 + 2, 7, computeDivide(totals[3], thousand, 0), "#,##0");
			makeExcel.setValue(19 + count * 3 + 2, 8, computeDivide(totals[3], totals[1], 5).multiply(hundred).setScale(2, RoundingMode.HALF_UP) + "%");
			makeExcel.setValue(19 + count * 3 + 2, 9, totals[4], "#,##0");
			makeExcel.setValue(19 + count * 3 + 2, 10, computeDivide(totals[4], totals[0], 5).multiply(hundred).setScale(2, RoundingMode.HALF_UP) + "%");
			makeExcel.setValue(19 + count * 3 + 2, 11, computeDivide(totals[5], thousand, 0), "#,##0");
			makeExcel.setValue(19 + count * 3 + 2, 12, computeDivide(totals[5], totals[1], 5).multiply(hundred).setScale(2, RoundingMode.HALF_UP) + "%");

			// 結束
			count++;
		}

	}
}
