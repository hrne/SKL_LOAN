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

@Component
@Scope("prototype")

public class LM041Report extends MakeReport {

	@Autowired
	LM041ServiceImpl lM041ServiceImpl;

	@Autowired
	MakeExcel makeExcel;

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
		if (LMList.size() == 0) {
			makeExcel.setValue(3, 1, "本日無資料");
		}
		int row = 3;
		BigDecimal total = BigDecimal.ZERO;
		for (Map<String, String> tLDVo : LMList) {

			String ad = "";
			int col = 0;
			for (int i = 0; i < tLDVo.size(); i++) {

				ad = "F" + String.valueOf(col);
				col++;
				switch (col) {
				case 7:
					total = total.add(tLDVo.get(ad) == null ? BigDecimal.ZERO : new BigDecimal(tLDVo.get(ad)));
					makeExcel.setValue(row, col, tLDVo.get(ad) == null ? BigDecimal.ZERO : new BigDecimal(tLDVo.get(ad)), "#,##0", "R");
					break;
				default:
					makeExcel.setValue(row, col, tLDVo.get(ad));
					break;
				}
			} // for

			row++;
		} // for
		String entdy = String.valueOf((Integer.valueOf(titaVo.get("ENTDY").toString()) / 100));
		String year = entdy.substring(0, 3);
		int month = Integer.parseInt(entdy.substring(3, 5));
		
		makeExcel.setMergedRegion(row + 3, row + 3, 1, 7);
		makeExcel.setValue(row + 3, 1, "一、擬 " + year + "年" + String.valueOf(month) + "月份呆帳戶之暫收款項金額共計 $" + total + "元入呆帳回收。", "#,##0");
		makeExcel.setMergedRegion(row + 4, row + 4, 1, 7);
		makeExcel.setValue(row + 4, 1, "二、陳核。");
		
		long sno = makeExcel.close();
		makeExcel.toExcel(sno);
	}

}
