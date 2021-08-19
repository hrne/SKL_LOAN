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
import com.st1.itx.db.service.springjpa.cm.LM038ServiceImpl;
import com.st1.itx.util.common.MakeExcel;
import com.st1.itx.util.common.MakeReport;

@Component
@Scope("prototype")

public class LM038Report extends MakeReport {

	@Autowired
	LM038ServiceImpl lM038ServiceImpl;

	@Autowired
	MakeExcel makeExcel;

	@Override
	public void printTitle() {

	}

	public void exec(TitaVo titaVo) throws LogicException {

		List<Map<String, String>> LM038List = null;
		try {
			LM038List = lM038ServiceImpl.findAll(titaVo);
			exportExcel(titaVo, LM038List);

		} catch (Exception e) {
			// TODO Auto-generated catch block
			StringWriter errors = new StringWriter();
			e.printStackTrace(new PrintWriter(errors));
			this.info("LM038ServiceImpl.testExcel error = " + errors.toString());
		}
	}

	private void exportExcel(TitaVo titaVo, List<Map<String, String>> LDList) throws LogicException {
		this.info("LM038Report exportExcel");
		makeExcel.open(titaVo, titaVo.getEntDyI(), titaVo.getKinbr(), "LM038", "逾期案件明細", "LM038-逾期案件明細", "LM038逾期案件明細.xls", "D9210081");
		if (LDList.size() == 0) {
			makeExcel.setValue(4, 1, "本日無資料");
		}
		int row = 4;

		BigDecimal total_DataCount = BigDecimal.ZERO;
		BigDecimal total_LoanBal = BigDecimal.ZERO;
		BigDecimal total_Principal = BigDecimal.ZERO;
		BigDecimal total_Interest = BigDecimal.ZERO;

		for (Map<String, String> tLDVo : LDList) {

			String ad = "";
			int col = 0;
			for (int i = 0; i < tLDVo.size(); i++) {

				ad = "F" + String.valueOf(col);
				String tmpValue = tLDVo.get(ad);
				col++;
				switch (col) {
				case 6:
					total_DataCount = total_DataCount.add(new BigDecimal(1));
					makeExcel.setValue(row, col, ad);
					break;

				case 8:
					BigDecimal tmpLoanBal = tmpValue == null || tmpValue.isEmpty() ? BigDecimal.ZERO : new BigDecimal(tmpValue);
					total_LoanBal = total_LoanBal.add(tmpLoanBal);
					makeExcel.setValue(row, col, tmpLoanBal, "#,##0");
					break;

				case 12:
					BigDecimal tmpPrincipal = tmpValue == null || tmpValue.isEmpty() ? BigDecimal.ZERO : new BigDecimal(tmpValue);
					total_Principal = total_Principal.add(tmpPrincipal);
					makeExcel.setValue(row, col, tmpPrincipal, "#,##0");
					break;

				case 13:
					BigDecimal tmpInterest = tmpValue == null || tmpValue.isEmpty() ? BigDecimal.ZERO : new BigDecimal(tmpValue);
					total_Interest = total_Interest.add(tmpInterest);
					makeExcel.setValue(row, col, tmpInterest, "#,##0");
					break;

				case 26:
					makeExcel.setValue(row, col, tLDVo.get(ad), "R");
					break;
				case 21:
					makeExcel.setValue(row, col, tLDVo.get(ad), "L");
					break;
				case 15:
					BigDecimal tmpAmt = tmpValue == null || tmpValue.isEmpty() ? BigDecimal.ZERO : new BigDecimal(tmpValue);
					// 金額
					makeExcel.setValue(row, col, tmpAmt, "#,##0");
					break;
				default:
					makeExcel.setValue(row, col, tLDVo.get(ad));
					break;
				}
			} // for

			row++;
		} // for

		// total output

		makeExcel.setValue(1, 6, total_DataCount, "#,##0");
		makeExcel.setValue(1, 8, total_LoanBal, "#,##0");
		makeExcel.setValue(1, 12, total_Principal, "#,##0");
		makeExcel.setValue(1, 13, total_Interest, "#,##0");

		long sno = makeExcel.close();
		makeExcel.toExcel(sno);
	}

}
