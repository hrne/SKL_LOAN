package com.st1.itx.trade.L9;

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
import com.st1.itx.util.common.data.ReportVo;
import com.st1.itx.util.parse.Parse;

@Component
@Scope("prototype")

public class LM031Report extends MakeReport {

	@Autowired
	LM031ServiceImpl lM031ServiceImpl;

	@Autowired
	MakeExcel makeExcel;

	@Autowired
	Parse parse;

	@Override
	public void printTitle() {

	}

	public Boolean exec(TitaVo titaVo) throws LogicException {

		List<Map<String, String>> LM031List = null;
		
		int reportDate = titaVo.getEntDyI() + 19110000;
		
		try {
			LM031List = lM031ServiceImpl.findAll(titaVo,reportDate);
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
		
		this.info("LM031Report exportExcel()");

		int reportDate = titaVo.getEntDyI() + 19110000;
		String brno = titaVo.getBrno();
		String txcd = "LM031";
		String fileItem = "企業動用率";
		String fileName = "LM031-企業動用率";
		String defaultExcel = "LM031_底稿_企業動用率.xlsx";
		String defaultSheet = "10810";
		String newSheet = showDate(String.valueOf(reportDate));
		
		ReportVo reportVo = ReportVo.builder().setRptDate(reportDate).setBrno(brno).setRptCode(txcd)
				.setRptItem(fileItem).build();

		// 開啟報表
		makeExcel.open(titaVo, reportVo, fileName, defaultExcel, defaultSheet);
		
		makeExcel.setSheet(defaultSheet, newSheet);

		if (LDList == null || LDList.isEmpty()) {
			makeExcel.setValue(3, 1, "本日無資料");
		} else {
			
			int row = 3;
			
			BigDecimal totalLineAmt = BigDecimal.ZERO;
			BigDecimal totalUtilBal = BigDecimal.ZERO;
			String lastCustNo = "";
			String lastFacmNo = "";
			int set1 = 0;// 判斷戶號是否與上一筆相同
			int set2 = 0;// 判斷額度是否與上一筆相同
			

			
			for (Map<String, String> tLDVo : LDList) {

				for (int i = 0; i <= 10; i++) {

					String value = tLDVo.get("F" + i);
					int col = i + 1;

					switch (col) {
					case 1:// 戶號
						makeExcel.setValue(row, col, parse.isNumeric(value) ? parse.stringToInteger(value) : value, "R");
						if (lastCustNo.equals(value)) {
							set1 = 1;
						} else {
							set1 = 0;
						}
						lastCustNo = value;
						break;
					case 2://額度
						makeExcel.setValue(row, col, parse.isNumeric(value) ? parse.stringToInteger(value) : value, "R");
						if (lastFacmNo.equals(value)) {
							set2 = 1;
						} else {
							set2 = 0;
						}
						lastFacmNo = value;
						break;
					case 3://戶名
						makeExcel.setValue(row, col, value, "L");
						break;
					case 5://核貸金額
						if (set1 == 0 || set2 == 0) {
							BigDecimal bd = getBigDecimal(value);
							makeExcel.setValue(row, col, bd, "#,##0", "R");
							totalLineAmt = totalLineAmt.add(bd);
						}
						break;
					case 6:// 放款餘額
						BigDecimal bd = getBigDecimal(value);
						makeExcel.setValue(row, col, bd, "#,##0", "R");
						totalUtilBal = totalUtilBal.add(bd);
						break;
					case 7://企金別
						makeExcel.setValue(row, col, parse.isNumeric(value) ? parse.stringToInteger(value) : value, "L");
						break;
					default:
						//4:額度
						//8:循環動用
						//9:循環動用期限
						//10:動支期限	
						//11:繳息迄日		
						makeExcel.setValue(row, col, parse.isNumeric(value) ? parse.stringToInteger(value) : value, "R");
						break;
					}
					
			
					
				} // for
				makeExcel.setValue(1, 5, totalLineAmt, "#,##0");
				makeExcel.setValue(1, 6, totalUtilBal, "#,##0");
				row++;
			} // for

		}

		makeExcel.close();
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
