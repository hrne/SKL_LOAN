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
import com.st1.itx.db.service.springjpa.cm.LM034ServiceImpl;
import com.st1.itx.util.common.MakeExcel;
import com.st1.itx.util.common.MakeReport;

@Component
@Scope("prototype")

public class LM034Report extends MakeReport {
	private static final Logger logger = LoggerFactory.getLogger(LM034Report.class);

	@Autowired
	LM034ServiceImpl lM034ServiceImpl;

	@Autowired
	MakeExcel makeExcel;

	public void exec(TitaVo titaVo) throws LogicException {
		List<Map<String, String>> listLM034 = null;
		try {
			listLM034 = lM034ServiceImpl.findAll(titaVo);
		} catch (Exception e) {
			StringWriter errors = new StringWriter();
			e.printStackTrace(new PrintWriter(errors));
			this.error("LM034ServiceImpl.findAll error = " + errors.toString());
		}
		exportExcel(titaVo, listLM034);
	}

	private void exportExcel(TitaVo titaVo, List<Map<String, String>> listLM034) throws LogicException {
		this.info("LM034Report exportExcel");

		makeExcel.open(titaVo, titaVo.getEntDyI(), titaVo.getKinbr(), "LM034", "新增逾放案件明細", "LM034-新增逾放案件明細", "LM034-新增逾放案件明細.xlsx", "工作表1", "D9701212");

		// 今日
		if (listLM034 != null && listLM034.size() != 0) {
			int row = 2;
			for (Map<String, String> tLDVo : listLM034) {
				String ad = "";
				int col = 0;
				for (int i = 0; i < tLDVo.size(); i++) {
					ad = "F" + String.valueOf(col);
					col++;
					switch (i) {
					case 4:
					case 5:
					case 6:
					case 11:
						makeExcel.setValue(row, col, tLDVo.get(ad) == null || tLDVo.get(ad) == "" ? BigDecimal.ZERO : new BigDecimal(tLDVo.get(ad)), "R");
						break;
					case 14:
						makeExcel.setValue(row, col, tLDVo.get(ad) == null || tLDVo.get(ad) == "" ? "0" : tLDVo.get(ad), "L");
						break;
					default:
						makeExcel.setValue(row, col, tLDVo.get(ad) == null || tLDVo.get(ad) == "" ? "0" : tLDVo.get(ad), "R");
						break;
					}
				}
				row++;
			}

		} else {
			makeExcel.setValue(3, 1, "無資料");
		}
		long sno = makeExcel.close();
		makeExcel.toExcel(sno);
	}

	private boolean isStringCanBeParsedToInteger(String str) {
		try {
			Integer.parseInt(str);
			return true;
		} catch (NumberFormatException e) {
			return false;
		}
	}
}
