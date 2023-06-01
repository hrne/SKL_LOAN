package com.st1.itx.trade.L9;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.db.service.springjpa.cm.L9139ServiceImpl;
import com.st1.itx.util.common.MakeExcel;
import com.st1.itx.util.common.MakeReport;
import com.st1.itx.util.common.data.ReportVo;
import com.st1.itx.util.format.StringCut;
import com.st1.itx.util.parse.Parse;

@Component
@Scope("prototype")

public class L9139Report extends MakeReport {

	@Autowired
	L9139ServiceImpl oL9139ServiceImpl;

	@Autowired
	MakeExcel makeExcel;

	@Autowired
	Parse parse;

	@Override
	public void printTitle() {
		this.info("printTitle nowRow = " + this.NowRow);

	}

	public Boolean exec(TitaVo titaVo) throws LogicException {

		List<Map<String, String>> oL9139List = null;
		try {
			oL9139List = oL9139ServiceImpl.findAll(titaVo);
		} catch (Exception e) {
			this.info("L9139ServiceImpl.findAll error = " + e.toString());
			return false;
		}
		setExcel(titaVo, oL9139List);
		return true;
	}

	private void setExcel(TitaVo titaVo, List<Map<String, String>> oL9139List) throws LogicException {

		
		int reportDate = titaVo.getEntDyI() + 19110000;
		int startDate =parse.stringToInteger(titaVo.getParam("StartDate"))+ 19110000;
		String brno = titaVo.getBrno();
		String txcd = "L9139";
		String fileItem = "暫收款日餘額前後差異比較表";
		String fileName = "L9139暫收款日餘額前後差異比較表";
		String defaultExcel = "L9139_底稿_暫收款日餘額前後差異比較表.xlsx";
		String defaultSheet = "暫收款日餘額前後差異比較表";

		ReportVo reportVo = ReportVo.builder().setRptDate(reportDate).setBrno(brno).setRptCode(txcd)
				.setRptItem(fileItem).build();

		// 開啟報表
		makeExcel.open(titaVo, reportVo, fileName, defaultExcel, defaultSheet);


		int row = 3;
		BigDecimal total = BigDecimal.ZERO;
		if (oL9139List != null && oL9139List.size() != 0) {
			for (Map<String, String> map : oL9139List) {

				String value = "";
				BigDecimal bValue = getBigDecimal(0);
				int col = 0;
				for (int i = 0; i <= 6; i++) {

					value = map.get("F" + i);

					if (value == null) {
						value = "";
					}

					col++;
					switch (i) {
					case 0: // 會計備份日期
						makeExcel.setValue(row, col, value);
						break;
					case 1: // 戶號
						makeExcel.setValue(row, col, value);
						break;
					case 2: // 戶名
//						makeExcel.setValue(row, col, StringCut.stringMask(value));
						makeExcel.setValue(row, col, value);
						break;
					case 3: // 今日暫收支票
						bValue = getBigDecimal(value);
						makeExcel.setValue(row, col, bValue, "#,##0");
						total = total.add(bValue);
						break;
					case 4: // 今日暫收非支票
						bValue= getBigDecimal(value);
						makeExcel.setValue(row, col, bValue, "#,##0");
						total = total.add(bValue);
						break;
					case 5: // 昨日暫收支票
						bValue = getBigDecimal(value);
						makeExcel.setValue(row, col, bValue, "#,##0");
						total = total.add(bValue);
						break;
					case 6: // 昨日暫收非支票
						bValue = getBigDecimal(value);
						makeExcel.setValue(row, col, bValue, "#,##0");
						total = total.add(bValue);
						break;
					default:
//						makeExcel.setValue(row, col, parse.isNumeric(value) ? getBigDecimal(value) : value);
						break;
					}
				} // for
				row++;
			} // for
//			makeExcel.setFormula(row, 10, total, "SUBTOTAL(9,J2:J" + (row - 1) + ")", "#,##0");

		} else {
			makeExcel.setValue(1, 2, "本日無資料");
		}
		long sno = makeExcel.close();
		makeExcel.toExcel(sno);
	}

}
