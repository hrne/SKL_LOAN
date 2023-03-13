package com.st1.itx.trade.L9;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.db.service.springjpa.cm.L9745ServiceImpl;
import com.st1.itx.util.common.MakeExcel;
import com.st1.itx.util.common.MakeReport;
import com.st1.itx.util.common.data.ReportVo;
import com.st1.itx.util.format.StringCut;
import com.st1.itx.util.parse.Parse;

@Component
@Scope("prototype")

public class L9745Report extends MakeReport {

	@Autowired
	L9745ServiceImpl L9745ServiceImpl;

	@Autowired
	MakeExcel makeExcel;

	@Autowired
	Parse parse;

	@Override
	public void printTitle() {
		this.info("printTitle nowRow = " + this.NowRow);

	}

	public Boolean exec(TitaVo titaVo) throws LogicException {

		List<Map<String, String>> L9745List = null;
		try {
			L9745List = L9745ServiceImpl.findAll(titaVo);
		} catch (Exception e) {
			this.info("L9745ServiceImpl.findAll error = " + e.toString());
			return false;
		}
		testExcel(titaVo, L9745List);
		return true;
	}

	private void testExcel(TitaVo titaVo, List<Map<String, String>> L9745List) throws LogicException {


		
		int reportDate = titaVo.getEntDyI() + 19110000;
		String brno = titaVo.getBrno();
		String txcd = "L9745";
		String fileItem = "房貸專員明細統計";
		String fileName = "L9745房貸專員明細統計";
		String defaultExcel = "L9745_底稿_房貸專員明細統計.xls";
		String defaultSheet = "房貸專員明細統計";

		ReportVo reportVo = ReportVo.builder().setRptDate(reportDate).setBrno(brno).setRptCode(txcd)
				.setRptItem(fileItem).build();

		// 開啟報表
		makeExcel.open(titaVo, reportVo, fileName, defaultExcel, defaultSheet);


		int row = 2;
		BigDecimal total = BigDecimal.ZERO;
		if (L9745List != null && L9745List.size() != 0) {
			for (Map<String, String> tLDVo : L9745List) {

				String value = "";
				int col = 0;
				for (int i = 0; i <= 15; i++) {

					value = tLDVo.get("F" + i);

					if (value == null) {
						value = "";
					}

					col++;
					switch (i) {
					case 0:
						if (value.equals("業務推展部")) {
							makeExcel.setValue(row, col, "業　推　部");
						} else if (value.equals("營業推展部")) {
							makeExcel.setValue(row, col, "營　推　部");
						} else if (value.equals("營業管理部")) {
							makeExcel.setValue(row, col, "營　管　部");
						} else if (value.equals("業務開發部")) {
							makeExcel.setValue(row, col, "業　開　部");
						} else {
							makeExcel.setValue(row, col, value);
						}
						break;
					case 2: // 戶名
						makeExcel.setValue(row, col, StringCut.stringMask(value));
						break;
					case 9:
						BigDecimal bd = getBigDecimal(value);
						makeExcel.setValue(row, col, bd, "#,##0");
						total = total.add(bd);
						break;
					case 15: // 員工代號
						makeExcel.setValue(row, col, value);
						break;
					default:
						makeExcel.setValue(row, col, parse.isNumeric(value) ? getBigDecimal(value) : value);
						break;
					}
				} // for
				row++;
			} // for
			makeExcel.setFormula(row, 10, total, "SUBTOTAL(9,J2:J" + (row - 1) + ")", "#,##0");

		} else {
			makeExcel.setValue(2, 1, "本日無資料");
		}
		long sno = makeExcel.close();
		makeExcel.toExcel(sno);
	}

}
