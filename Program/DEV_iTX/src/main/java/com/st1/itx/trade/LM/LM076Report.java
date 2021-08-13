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
import com.st1.itx.db.service.springjpa.cm.LM076ServiceImpl;
import com.st1.itx.util.common.MakeExcel;
import com.st1.itx.util.common.MakeReport;

@Component
@Scope("prototype")

public class LM076Report extends MakeReport {
	// private static final Logger logger = LoggerFactory.getLogger(LM076Report.class);

	@Autowired
	LM076ServiceImpl lM076ServiceImpl;

	@Autowired
	MakeExcel makeExcel;

	public boolean exec(TitaVo titaVo) throws LogicException {
		this.info("LM076Report exec start ...");
		
		int iAcDate = titaVo.getEntDyI() + 19110000;
		this.info("LM076Report exec AcDate = " + iAcDate);

		List<Map<String, String>> lLM076 = null;

		try {
			lLM076 = lM076ServiceImpl.findAll(titaVo, Integer.toString(iAcDate).substring(0,6));
		} catch (Exception e) {
			StringWriter errors = new StringWriter();
			e.printStackTrace(new PrintWriter(errors));
			this.error("LM076ServiceImpl.findAll error = " + errors.toString());
		}

		exportExcel(titaVo, lLM076, iAcDate);
		
		return true;

	}

	private void exportExcel(TitaVo titaVo, List<Map<String, String>> lList, int date) throws LogicException {
		
		// pivot position for data inputs
		int pivotRow = 5; // 1-based
		int pivotCol = 3; // 1-based
		
		this.info("LM076Report exportExcel");
		String entdy = String.valueOf(date - 19110000); // expects date to be in BC Date format.
		String YearMonth = entdy.substring(0,3) + " 年 " + entdy.substring(3,5) + " 月";		
		
		makeExcel.open(titaVo, titaVo.getEntDyI(), titaVo.getKinbr(), "LM076", "B042金融機構承作「購地貸款」統計表",
				"LM076_B042金融機構承作「購地貸款」統計表" + showRocDate(entdy, 0).substring(0, 7), "LM076_底稿_B042金融機構承作「購地貸款」統計表.xlsx", 1, "FOA");
		
		// 資料期間 C2
		makeExcel.setValue(2, 3, "民國 " + YearMonth, "R");

		if (lList != null && lList.size() != 0) {

			for (Map<String, String> tLDVo : lList) {
				int colShift = 0;
				int rowShift = 0;
				
				for (int i = 0; i < tLDVo.size(); i++) {

					int col = i + pivotCol; // 1-based
					int row = pivotRow; // 1-based
					
					// Query received will have column names in the format of F0, even if no alias is set in SQL
					// notice it's 0-based for those names
					String tmpValue = tLDVo.get("F" + i); 
					
					switch (i+1) {
					// if specific column needs special treatment, insert case here.
					case 1:
						// CityCode: already properly converted
						rowShift = Integer.parseInt(tmpValue) - 1;
						colShift--; // doesn't write
						break;
					case 2:
						// Newly Drawdown Amount: hundred million
						makeExcel.setValue(row + rowShift, col + colShift, new BigDecimal(tmpValue).divide(new BigDecimal("100000000"), 2, BigDecimal.ROUND_UP), "R");
						break;
					case 3:
						// Weighted Average of Loan: Percent
						makeExcel.setValue(row + rowShift, col + colShift, new BigDecimal(tmpValue).setScale(2, BigDecimal.ROUND_UP), "R");
						break;
					case 4:
						// Weighted Average of Loan Rate: Percent
						makeExcel.setValue(row + rowShift, col + colShift, new BigDecimal(tmpValue).setScale(2, BigDecimal.ROUND_UP), "R");
						break;
					default:
						makeExcel.setValue(row + rowShift, col + colShift, tmpValue, "R");
						break;
					}
				} // for

			} // for
			
			for (int i = 3; i < 6; i ++)
			{
				makeExcel.formulaCaculate(12, i);
			}
		} else {
			makeExcel.setValue(3, 1, "本月無資料");
		}

		long sno = makeExcel.close();
		makeExcel.toExcel(sno);
	}
}
