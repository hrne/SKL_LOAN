package com.st1.itx.trade.L9;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.db.service.springjpa.cm.L9725ServiceImpl;
import com.st1.itx.util.common.MakeExcel;
import com.st1.itx.util.common.MakeReport;

@Component
@Scope("prototype")

public class L9725Report extends MakeReport {

	@Autowired
	L9725ServiceImpl l9725ServiceImpl;

	@Autowired
	MakeExcel makeExcel;

	String TXCD = "L9725";
	String TXName = "防制洗錢機構風險評估(IRA)定期量化撈件";
	String SheetName = "D109040904";

	// pivot position for data inputs
	int pivotRow = 2; // 1-based

	public boolean exec(TitaVo titaVo) throws LogicException {
		this.info("L9725Report exec start ...");

		List<Map<String, String>> lL9725 = null;

		try {
			lL9725 = l9725ServiceImpl.findAll(titaVo);
		} catch (Exception e) {
			StringWriter errors = new StringWriter();
			e.printStackTrace(new PrintWriter(errors));
			this.error("L9725ServiceImpl.findAll error = " + errors.toString());
		}

		exportExcel(titaVo, lL9725);

		return true;

	}

	private void exportExcel(TitaVo titaVo, List<Map<String, String>> lList) throws LogicException {

		this.info("L9725Report exportExcel");

		makeExcel.open(titaVo, titaVo.getEntDyI(), titaVo.getKinbr(), TXCD, TXName, TXCD + "_" + TXName,
				TXCD + "_底稿_" + TXName + ".xlsx", 1, SheetName);

		if (lList != null && lList.size() != 0) {

			int rowShift = 0;

			for (Map<String, String> tLDVo : lList) {

				int row = pivotRow + rowShift; // 1-base
				makeExcel.setValue(row, 0, tLDVo.get("F0"), "C");
				makeExcel.setValue(row, 1, tLDVo.get("F1"), "L");
				makeExcel.setValue(row, 2, tLDVo.get("F2"), "C");
				makeExcel.setValue(row, 3, tLDVo.get("F3"), "C");
				makeExcel.setValue(row, 4, tLDVo.get("F4"), "C");
				makeExcel.setValue(row, 5, tLDVo.get("F5"), "C");
				
				rowShift++;

			} // for
		} else {
			makeExcel.setValue(2, 1, "本月無資料");
		}

		long sno = makeExcel.close();
		makeExcel.toExcel(sno);
	}
}
