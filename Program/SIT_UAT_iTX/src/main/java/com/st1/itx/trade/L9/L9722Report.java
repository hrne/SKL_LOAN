package com.st1.itx.trade.L9;

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
import com.st1.itx.db.service.springjpa.cm.L9722ServiceImpl;
import com.st1.itx.util.common.MakeExcel;
import com.st1.itx.util.common.MakeReport;

@Component
@Scope("prototype")

public class L9722Report extends MakeReport {
	private static final Logger logger = LoggerFactory.getLogger(L9722Report.class);

	@Autowired
	L9722ServiceImpl l9722ServiceImpl;

	@Autowired
	MakeExcel makeExcel;
	
	String TXCD = "L9722";
	String TXName = "ICS放款資料";
	String SheetName = "D20200911_0921";
	
	// pivot position for data inputs
	int pivotRow = 2; // 1-based
	int pivotCol = 1; // 1-based

	public boolean exec(TitaVo titaVo) throws LogicException {
		this.info(TXCD + "Report exec start ...");

		List<Map<String, String>> lL9722 = null;

		try {
			lL9722 = l9722ServiceImpl.findAll(titaVo);
		} catch (Exception e) {
			StringWriter errors = new StringWriter();
			e.printStackTrace(new PrintWriter(errors));
			this.error(TXCD + "ServiceImpl.findAll error = " + errors.toString());
		}

		exportExcel(titaVo, lL9722);
		
		return true;

	}

	private void exportExcel(TitaVo titaVo, List<Map<String, String>> lList) throws LogicException {
		
		this.info(TXCD + "Report exportExcel");	
		
		makeExcel.open(titaVo, titaVo.getEntDyI(), titaVo.getKinbr(), TXCD, TXName,
				TXCD + "_" + TXName,
				TXCD + "_底稿_" + TXName + ".xlsx",
				1, SheetName);

		if (lList != null && lList.size() != 0) {

			int rowShift = 0;
			
			for (Map<String, String> tLDVo : lList) {
				
				int colShift = 0;
				String ClNo = "";
				
				for (int i = 0; i < tLDVo.size(); i++) {

					int col = i + pivotCol + colShift; // 1-based
					int row = pivotRow + rowShift; // 1-based
					
					// Query received will have column names in the format of F0, even if no alias is set in SQL
					// notice it's 0-based for those names
					String tmpValue = tLDVo.get("F" + i); 
					
					switch (i) {
					// if specific column needs special treatment, insert case here.
					case 7: // ClCode1
						ClNo += tmpValue + "-";
						colShift--; // doesn't write
						break;
					case 8: // ClCode2
						ClNo += tmpValue + "-";
						colShift--; // doesn't write
						break;
					case 9: // ClNo
						ClNo += tmpValue;
						makeExcel.setValue(row, col, ClNo, "C");
						break;
					default:
						makeExcel.setValue(row, col, tmpValue, "C");
						break;
					}
					
				} // for
				
				rowShift++;
				
			} // for
		} else {
			makeExcel.setValue(pivotRow, pivotCol, "本月無資料");
		}

		long sno = makeExcel.close();
		makeExcel.toExcel(sno);
	}
}
