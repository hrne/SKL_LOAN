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
import com.st1.itx.db.service.springjpa.cm.L9721ServiceImpl;
import com.st1.itx.util.common.MakeExcel;
import com.st1.itx.util.common.MakeReport;

@Component
@Scope("prototype")

public class L9721Report extends MakeReport {
	// private static final Logger logger = LoggerFactory.getLogger(L9721Report.class);

	@Autowired
	L9721ServiceImpl l9721ServiceImpl;

	@Autowired
	MakeExcel makeExcel;
	
	String TXCD = "L9721";
	String TXName = "員工房貸利率明細";
	String SheetName = "D109052107";
	
	// pivot position for data inputs
	int pivotRow = 3; // 1-based
	int pivotCol = 1; // 1-based

	public boolean exec(TitaVo titaVo) throws LogicException {
		this.info(TXCD + "Report exec start ...");

		List<Map<String, String>> lL9721 = null;

		try {
			lL9721 = l9721ServiceImpl.findAll(titaVo);
		} catch (Exception e) {
			StringWriter errors = new StringWriter();
			e.printStackTrace(new PrintWriter(errors));
			this.error(TXCD + "ServiceImpl.findAll error = " + errors.toString());
		}

		exportExcel(titaVo, lL9721);
		
		return true;

	}

	private void exportExcel(TitaVo titaVo, List<Map<String, String>> lList) throws LogicException {

		
		this.info(TXCD +"Report exportExcel");	
		
		makeExcel.open(titaVo, titaVo.getEntDyI(), titaVo.getKinbr(), TXCD, TXName,
				TXCD + "_" + TXName,
				TXCD + "_底稿_" + TXName + ".xlsx",
				1, SheetName);

		if (lList != null && lList.size() != 0) {
			
			int rowShift = 0;

			for (Map<String, String> tLDVo : lList) {
				
				int colShift = 0;
				
				for (int i = 0; i < tLDVo.size(); i++) {

					int col = i + pivotCol + colShift; // 1-based
					int row = pivotRow + rowShift; // 1-based
					
					// Query received will have column names in the format of F0, even if no alias is set in SQL
					// notice it's 0-based for those names
					String tmpValue = tLDVo.get("F" + i); 
					
					// switch by code of Column; i.e. Col A, Col B...
					// breaks if more than 26 columns!
					switch (String.valueOf((char)(65+i))) {
					// if specific column needs special treatment, insert case here.
					case "D":
						makeExcel.setValue(row, col, tmpValue, "L");
						break;
					case "E":
						makeExcel.setValue(row, col, tmpValue, "L");
						break;
					case "H":
						makeExcel.setValue(row, col, tmpValue, "L");
						break;
					case "I":
						makeExcel.setValue(row, col, tmpValue, "L");
						break;
					case "J":
						makeExcel.setValue(row, col, tmpValue, "L");
						break;
					case "P":
						makeExcel.setValue(row, col, tmpValue, "L");
						break;
					default:
						makeExcel.setValue(row, col, tmpValue.equals("") ? "" : Integer.parseInt(tmpValue), "L");
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
