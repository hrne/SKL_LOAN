package com.st1.itx.trade.LD;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.db.service.springjpa.cm.LD007ServiceImpl;
import com.st1.itx.util.common.MakeExcel;
import com.st1.itx.util.common.MakeReport;
import com.st1.itx.util.parse.Parse;

@Component
@Scope("prototype")

public class LD007Report extends MakeReport {

	@Autowired
	LD007ServiceImpl lD007ServiceImpl;

	@Autowired
	MakeExcel makeExcel;
	
	@Autowired
	Parse parse;

	@Override
	public void printTitle() {
		this.info("printTitle nowRow = " + this.NowRow);

	}

	public Boolean exec(TitaVo titaVo) throws LogicException {

		List<Map<String, String>> LD007List = null;
		try {
			LD007List = lD007ServiceImpl.findAll(titaVo);
		} catch (Exception e) {
			this.info("lD007ServiceImpl.findAll error = " + e.toString());
			return false;
		}
		testExcel(titaVo, LD007List);
		return true;
	}

	private void testExcel(TitaVo titaVo, List<Map<String, String>> LD007List) throws LogicException {

		makeExcel.open(titaVo, titaVo.getEntDyI(), titaVo.getKinbr(), "LD007", "房貸專員明細統計", "LD007房貸專員明細統計", "LD007房貸專員明細統計.xls", "房貸專員明細統計");

		int row = 2;
		BigDecimal total = BigDecimal.ZERO;
		if (LD007List != null && LD007List.size() != 0) {
			for (Map<String, String> tLDVo : LD007List) {

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
			makeExcel.setValue(row, 10, total, "#,##0");
		} else {
			makeExcel.setValue(2, 1, "本日無資料");
		}
		long sno = makeExcel.close();
		makeExcel.toExcel(sno);
	}

}
