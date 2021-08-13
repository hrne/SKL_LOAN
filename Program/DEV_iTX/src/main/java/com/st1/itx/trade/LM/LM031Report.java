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
import com.st1.itx.db.service.springjpa.cm.LM031ServiceImpl;
import com.st1.itx.util.common.MakeExcel;
import com.st1.itx.util.common.MakeReport;

@Component
@Scope("prototype")

public class LM031Report extends MakeReport {
	// private static final Logger logger = LoggerFactory.getLogger(LM031Report.class);

	@Autowired
	LM031ServiceImpl lM031ServiceImpl;

	@Autowired
	MakeExcel makeExcel;

	@Override
	public void printTitle() {

	}

	public Boolean exec(TitaVo titaVo) throws LogicException {

		List<Map<String, String>> LM031List = null;
		try {
			LM031List = lM031ServiceImpl.findAll(titaVo);
			exportExcel(titaVo, LM031List);
			
			return true;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			StringWriter errors = new StringWriter();
			e.printStackTrace(new PrintWriter(errors));
			this.info("LM031ServiceImpl.testExcel error = " + errors.toString());
			
			return false;
		}
	}

	private void exportExcel(TitaVo titaVo, List<Map<String, String>> LDList) throws LogicException {
		this.info("===========in testExcel");
		String entdy = String.valueOf(Integer.valueOf(titaVo.get("ENTDY").toString()));

		makeExcel.open(titaVo, titaVo.getEntDyI(), titaVo.getKinbr(), "LM031", "企業動用率", "LM031企業動用率", "LM031企業動用率.xlsx", "10810", showDate(entdy));
		if (LDList.size() == 0) {
			makeExcel.setValue(3, 1, "本日無資料");
		}
		int row = 3;
		BigDecimal totalLineAmt = BigDecimal.ZERO;
		BigDecimal totalUtilBal = BigDecimal.ZERO;
		String lastCustNo = "";
		String lastFacmNo = "";
		int set1 = 0;//判斷戶號是否與上一筆相同
		int set2 = 0;//判斷額度是否與上一筆相同
		for (Map<String, String> tLDVo : LDList) {

			String ad = "";
			int col = 0;
			for (int i = 0; i < tLDVo.size(); i++) {

				ad = "F" + String.valueOf(col);
				col++;

				switch (col) {
				case 1:
					makeExcel.setValue(row, col, tLDVo.get(ad), "R");
					if(lastCustNo.equals(tLDVo.get(ad))) {
						set1 = 1;
					} else {
						set1 = 0;
					}
					lastCustNo = tLDVo.get(ad);
					break;
				case 2:
					makeExcel.setValue(row, col, tLDVo.get(ad), "R");
					if(lastFacmNo.equals(tLDVo.get(ad))) {
						set2 = 1;
					} else {
						set2 = 0;
					}
					lastFacmNo = tLDVo.get(ad);
					break;
				case 5:
					if(set1 == 0 || set2 == 0) {
						makeExcel.setValue(row, col, tLDVo.get(ad) == null || tLDVo.get(ad) == "" ? BigDecimal.ZERO : new BigDecimal(tLDVo.get(ad)), "#,##0", "R");
						totalLineAmt = totalLineAmt.add(tLDVo.get(ad) == null || tLDVo.get(ad) == "" ? BigDecimal.ZERO : new BigDecimal(tLDVo.get(ad)));
					}
					break;
				case 6:
					// 金額
					makeExcel.setValue(row, col, tLDVo.get(ad) == null || tLDVo.get(ad) == "" ? BigDecimal.ZERO : new BigDecimal(tLDVo.get(ad)), "#,##0", "R");
					totalUtilBal = totalUtilBal.add(tLDVo.get(ad) == null || tLDVo.get(ad) == "" ? BigDecimal.ZERO : new BigDecimal(tLDVo.get(ad)));
					break;
				case 7:
					makeExcel.setValue(row, col, tLDVo.get(ad), "L");
					break;
				case 9:
					makeExcel.setValue(row, col, tLDVo.get(ad), "C");
					break;
				default:
					makeExcel.setValue(row, col, tLDVo.get(ad), "R");
					break;
				}
			} // for
			makeExcel.setValue(1, 5, totalLineAmt, "#,##0");
			makeExcel.setValue(1, 6, totalUtilBal, "#,##0");
			row++;
		} // for
		
		long sno = makeExcel.close();
		makeExcel.toExcel(sno);
	}

	public String showDate(String date) {
		this.info("MakeReport.toPdf showRocDate1 = " + date);

		if (date.length() == 7) {
			return date.substring(0, 5);
		} else {
			return date.substring(0, 4);
		}

	}

}
