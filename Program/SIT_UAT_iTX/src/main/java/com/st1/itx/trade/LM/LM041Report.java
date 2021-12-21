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
import com.st1.itx.db.service.springjpa.cm.LM041ServiceImpl;
import com.st1.itx.util.common.MakeExcel;
import com.st1.itx.util.common.MakeReport;
import com.st1.itx.util.parse.Parse;

@Component
@Scope("prototype")

public class LM041Report extends MakeReport {

	@Autowired
	LM041ServiceImpl lM041ServiceImpl;

	@Autowired
	MakeExcel makeExcel;

	@Autowired
	Parse parse;

	@Override
	public void printTitle() {

	}

	public void exec(TitaVo titaVo) throws LogicException {
		List<Map<String, String>> LM041List = null;
		try {
			LM041List = lM041ServiceImpl.findAll(titaVo);

		} catch (Exception e) {
			// TODO Auto-generated catch block
			StringWriter errors = new StringWriter();
			e.printStackTrace(new PrintWriter(errors));
			this.info("LM041ServiceImpl.testExcel error = " + errors.toString());
		}
		exportExcel(titaVo, LM041List);
	}

	private void exportExcel(TitaVo titaVo, List<Map<String, String>> LMList) throws LogicException {
		this.info("LM041Report exportExcel");
		makeExcel.open(titaVo, titaVo.getEntDyI(), titaVo.getKinbr(), "LM041", "催收及呆帳戶暫收款明細表", "LM041催收及呆帳戶暫收款明細表", "LM041催收及呆帳戶暫收款明細表.xlsx", "D961211M");

		if (LMList == null || LMList.isEmpty()) {

			makeExcel.setValue(3, 1, "本日無資料");

		} else {
			int row = 3;
			BigDecimal total = BigDecimal.ZERO;
			for (Map<String, String> tLDVo : LMList) {

				for (int i = 0; i <= 6; i++) {

					String value = tLDVo.get("F" + i);
					int col = i + 1;

					switch (i) {
					case 6:
						BigDecimal bd = getBigDecimal(value);
						total = total.add(bd);
						makeExcel.setValue(row, col, bd, "#,##0", "R");
						break;
					default:
						makeExcel.setValue(row, col, value);
						break;
					}
				} // for

				row++;
			} // for
			int entdy = parse.stringToInteger(titaVo.get("ENTDY"));
			int year = entdy / 10000;
			int month = entdy / 100 % 100;

			makeExcel.setMergedRegion(row + 3, row + 3, 1, 7);
			makeExcel.setValue(row + 3, 1, "一、擬 " + year + "年" + month + "月份呆帳戶之暫收款項金額共計 $" + formatAmt(total, 0) + "元入呆帳回收。");
			makeExcel.setMergedRegion(row + 4, row + 4, 1, 7);
			makeExcel.setValue(row + 4, 1, "二、陳核。");

		}

		long sno = makeExcel.close();
		// makeExcel.toExcel(sno);
	}

}
