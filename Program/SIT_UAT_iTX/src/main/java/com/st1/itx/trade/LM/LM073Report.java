package com.st1.itx.trade.LM;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.db.service.springjpa.cm.LM073ServiceImpl;
import com.st1.itx.util.common.MakeExcel;
import com.st1.itx.util.common.MakeReport;

@Component
@Scope("prototype")

public class LM073Report extends MakeReport {
	private static final Logger logger = LoggerFactory.getLogger(LM073Report.class);

	@Autowired
	LM073ServiceImpl lM073ServiceImpl;

	@Autowired
	MakeExcel makeExcel;

	public boolean exec(TitaVo titaVo) throws LogicException {
		this.info("LM073Report exec start ...");
		
		int iAcDate = titaVo.getEntDyI() + 19110000;
		
		this.info("LM073Report exec AcDate = " + iAcDate);

		List<Map<String, String>> lLM073 = null;

		try {
			lLM073 = lM073ServiceImpl.findAll(titaVo, Integer.toString(iAcDate).substring(0,6));
		} catch (Exception e) {
			StringWriter errors = new StringWriter();
			e.printStackTrace(new PrintWriter(errors));
			this.error("LM073ServiceImpl.findAll error = " + errors.toString());
		}

		exportExcel(titaVo, lLM073, iAcDate);
		
		return true;

	}


	private void exportExcel(TitaVo titaVo, List<Map<String, String>> lList, int date) throws LogicException {
		
		// pivot position for data inputs
		int pivotRow = 2; // 1-based
		int pivotCol = 1; // 1-based
		
		this.info("LM073Report exportExcel");
		String entdy = String.valueOf(date - 19110000); // expects date to be in BC Date format.
		String YearMonth = entdy.substring(0,3) + " 年 " + entdy.substring(3,5) + " 月";
		
		makeExcel.open(titaVo, titaVo.getEntDyI(), titaVo.getKinbr(), "LM073", "央行報送明細資料",
				"LM073央行報送明細資料" + showRocDate(entdy, 0).substring(0, 7), "LM073央行報送明細資料.xlsx", "明細", "央行報送 " + YearMonth);
		
		int row = pivotRow;
		
		if (lList != null && lList.size() != 0) {

			for (Map<String, String> tLDVo : lList) {
				int colShift = 0;
				
				for (int i = 0; i < tLDVo.size(); i++) {

					int col = i + pivotCol; // 1-based for col
					
					// Query received will have column names in the format of F0, even if no alias is set in SQL
					// notice it's 0-based for those names
					String tmpValue = tLDVo.get("F" + i); 
					
					switch (i + 1) {
					// if specific column needs special treatment, insert case here.
					case 2:
						tmpValue = tLDVo.get("F1") + " " + tLDVo.get("F2"); // 戶號 中文敘述
						makeExcel.setValue(row, col + colShift, tmpValue, "R");
						break;
					case 3:
						colShift--; // shifts writing col to left by 1 grid
						break;
					default:
						makeExcel.setValue(row, col + colShift, tmpValue, "R");
						break;
					}
				} // for
				row++;
			} // for
		} else {
			makeExcel.setValue(pivotRow, pivotCol, "本日無資料");
		}

		long sno = makeExcel.close();
		makeExcel.toExcel(sno);
	}
}
