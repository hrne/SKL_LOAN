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
import com.st1.itx.db.service.springjpa.cm.LM081ServiceImpl;
import com.st1.itx.util.common.MakeExcel;
import com.st1.itx.util.common.MakeReport;

@Component
@Scope("prototype")

public class LM081Report extends MakeReport {
	// private static final Logger logger = LoggerFactory.getLogger(LM081Report.class);

	@Autowired
	LM081ServiceImpl lM081ServiceImpl;

	@Autowired
	MakeExcel makeExcel;

	public boolean exec(TitaVo titaVo) throws LogicException {
		this.info("LM081Report exec start ...");
		
		int iAcDate = titaVo.getEntDyI() + 19110000;
		this.info("LM081Report exec AcDate = " + iAcDate);

		List<Map<String, String>> lLM081 = null;

		try {
			lLM081 = lM081ServiceImpl.findAll(titaVo, Integer.toString(iAcDate).substring(0,6));
		} catch (Exception e) {
			StringWriter errors = new StringWriter();
			e.printStackTrace(new PrintWriter(errors));
			this.error("LM081ServiceImpl.findAll error = " + errors.toString());
		}

		exportExcel(titaVo, lLM081, iAcDate);
		
		return true;

	}

	private void exportExcel(TitaVo titaVo, List<Map<String, String>> lList, int date) throws LogicException {
		
		// pivot position for data inputs
		int pivotRow = 7; // 1-based
		int pivotCol = 3; // 1-based
		
		this.info("LM081Report exportExcel");
		String entdy = String.valueOf(date - 19110000); // expects date to be in BC Date format.
		String YearMonth = entdy.substring(0,3) + " 年 " + entdy.substring(3,5) + " 月";		
		
		makeExcel.open(titaVo, titaVo.getEntDyI(), titaVo.getKinbr(), "LM081", "B047金融機構承作「自然人購屋貸款」統計表(110.3.19(含)起辦理案件)",
				"LM081_B047金融機構承作「自然人購屋貸款」統計表(110.3.19(含)起辦理案件)" + showRocDate(entdy, 0).substring(0, 7), "LM081_底稿_B047金融機構承作「自然人購屋貸款」統計表(110.3.19(含)起辦理案件).xlsx", 1, "FOA");
		
		// 資料期間 E3
		makeExcel.setValue(3, 5, "民國 " + YearMonth, "R");

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
						// RuleCode: 01 - col 3~6 (+0)
						//           03 - col 7~10 (+4)
						if (tmpValue.equals("01")) { colShift = 0; } 
						else if (tmpValue.equals("03")) { colShift = 4; } 
						else 
						{ 
							this.info("LM081Report exportExcel: RuleCodeException, got " + tmpValue);
							break; 
						};
						colShift--; // doesn't write
						break;
					case 2:
						// CityCode: already properly converted
						rowShift = Integer.parseInt(tmpValue) - 1;
						colShift--; // doesn't write
						break;
					case 4:
						// Newly Drawdown Amount: hundred million
						makeExcel.setValue(row + rowShift, col + colShift, new BigDecimal(tmpValue).divide(new BigDecimal("100000000"), 2, BigDecimal.ROUND_UP), "R");
						break;
					case 5:
						// Weighted Average of Loan: Percent
						makeExcel.setValue(row + rowShift, col + colShift, new BigDecimal(tmpValue).setScale(2, BigDecimal.ROUND_UP), "R");
						break;
					case 6:
						// Weighted Average of Loan Rate: Percent
						makeExcel.setValue(row + rowShift, col + colShift, new BigDecimal(tmpValue).setScale(2, BigDecimal.ROUND_UP), "R");
						break;
					default:
						makeExcel.setValue(row + rowShift, col + colShift, tmpValue, "R");
						break;
					}
				} // for

			} // for
			
			for (int i = 3; i < 11; i ++)
			{
				makeExcel.formulaCaculate(14, i);
			}
			
		} else {
			makeExcel.setValue(4, 1, "本月無資料");
		}

		long sno = makeExcel.close();
		makeExcel.toExcel(sno);
	}
}
