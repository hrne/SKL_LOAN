package com.st1.itx.trade.LM;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.db.service.springjpa.cm.LM023ServiceImpl;
import com.st1.itx.util.common.MakeExcel;
import com.st1.itx.util.common.MakeReport;

@Component
@Scope("prototype")

public class LM023Report extends MakeReport {

	@Autowired
	public LM023ServiceImpl lM023ServiceImpl;

	@Autowired
	MakeExcel makeExcel;

	@Override
	public void printHeader() {

	}

	public void exec(TitaVo titaVo) throws LogicException {

		makeExcel.open(titaVo, titaVo.getEntDyI(), titaVo.getKinbr(), "LM023", "利息收入", "LM023_利息收入",
				"LM023_底稿_利息收入.xlsx", "利息收入");

		List<Map<String, String>> lM023List = null;

		try {

			lM023List = lM023ServiceImpl.findAll2(titaVo);

		} catch (Exception e) {

			StringWriter errors = new StringWriter();
			e.printStackTrace(new PrintWriter(errors));
			this.info("lM023ServiceImpl.findAll error = " + errors.toString());

		}

		DecimalFormat df1 = new DecimalFormat("#,##0");

		if (lM023List.size() != 0 && lM023List != null) {

			BigDecimal tempAmt = BigDecimal.ZERO;

			BigDecimal total0 = BigDecimal.ZERO;
			BigDecimal total1 = BigDecimal.ZERO;
			BigDecimal total2 = BigDecimal.ZERO;

			BigDecimal resTotal = BigDecimal.ZERO;
			BigDecimal percent = new BigDecimal("100");

			int col = 0;

			String year = "";

			String dataSeq = "";

			int count = 0;

			for (int i = 0; i < lM023List.size(); i++) {

				if (!dataSeq.equals(lM023List.get(i).get("F0").toString())) {
					count = 0;
				}
				count++;

				// 0 今年 1預算 2上年度
				dataSeq = Integer.valueOf(lM023List.get(i).get("F0")).toString();

				// 利息金額
				tempAmt = lM023List.get(i).get("F3").isEmpty() ? BigDecimal.ZERO
						: new BigDecimal(lM023List.get(i).get("F3")).divide(new BigDecimal("100000000"), 2,
								BigDecimal.ROUND_HALF_UP);
				// 欄位(月份判斷)
				col = Integer.valueOf(lM023List.get(i).get("F2")) + 1;

				if (dataSeq.equals("0")) {

					year = String.valueOf(Integer.valueOf(lM023List.get(i).get("F1")) - 1911);

					makeExcel.setValue(4, 1, year + "年度");

					total0 = total0.add(tempAmt);

					// 當年度
					makeExcel.setValue(4, col, df1.format(tempAmt), "L");

					// 息收合計
					makeExcel.setValue(7, col, df1.format(total0), "L");

				}

				if (dataSeq.equals("1")) {
					total1 = total1.add(tempAmt);

					// 預算
					makeExcel.setValue(5, col, df1.format(tempAmt), "L");

				}

				if (dataSeq.equals("2")) {
					total2 = total2.add(tempAmt);

					// 上年度同期
					makeExcel.setValue(6, col, df1.format(tempAmt), "L");

				}

			}

			makeExcel.setValue(4, 14, df1.format(total0), "L");
			makeExcel.setValue(5, 14, df1.format(total1), "L");
			makeExcel.setValue(6, 14, df1.format(total2), "L");

			resTotal = total0 == BigDecimal.ZERO || total2 == BigDecimal.ZERO ? BigDecimal.ZERO
					: total0.divide(total2, 4, BigDecimal.ROUND_HALF_UP).multiply(percent);

			makeExcel.setValue(8, 14, resTotal + "%", "L");

			makeExcel.setValue(11, 9, year + "年度預算" + total1 + "億。", "L");

			
//			makeExcel.formulaCaculate(4, 14);
//			makeExcel.formulaCaculate(5, 14);
//			makeExcel.formulaCaculate(6, 14);
			makeExcel.formulaCaculate(12, 9);

		}
		long sno = makeExcel.close();
		makeExcel.toExcel(sno);
	}

}
