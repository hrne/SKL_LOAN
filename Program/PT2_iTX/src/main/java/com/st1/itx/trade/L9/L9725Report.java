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
import com.st1.itx.db.service.springjpa.cm.L9725ServiceImpl;
import com.st1.itx.util.common.MakeExcel;
import com.st1.itx.util.common.MakeReport;
import com.st1.itx.util.common.data.ReportVo;

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
	int pivotCol = 1; // 1-based

	public boolean exec(TitaVo titaVo) throws LogicException {
		this.info("L9725Report exec start ...");

		List<Map<String, String>> lL9725 = null;
		List<Map<String, String>> lL9725B = null;

		try {
			lL9725 = l9725ServiceImpl.findAll(titaVo);
			lL9725B = l9725ServiceImpl.findAll2(titaVo);
		} catch (Exception e) {
			StringWriter errors = new StringWriter();
			e.printStackTrace(new PrintWriter(errors));
			this.error("L9725ServiceImpl.findAll error = " + errors.toString());
		}

		exportExcel(titaVo, lL9725);
		exportExcelB(titaVo, lL9725B);

		return true;

	}

	private void exportExcel(TitaVo titaVo, List<Map<String, String>> lList) throws LogicException {

		this.info("L9725Report exportExcel");
		
		int reportDate = titaVo.getEntDyI() + 19110000;
		String brno = titaVo.getBrno();
		String txcd = TXCD;
		String fileItem = TXName+"(D109040904)";
		String fileName = TXCD + "_" + TXName+"(D109040904)";
//		String defaultExcel = TXCD + "_底稿_" + TXName + ".xlsx";
		String defaultSheet = SheetName;

		ReportVo reportVo = ReportVo.builder().setRptDate(reportDate).setBrno(brno).setRptCode(txcd)
				.setRptItem(fileItem).build();

		// 開啟報表
		makeExcel.open(titaVo, reportVo, fileName);

		makeExcel.setSheet(TXCD, defaultSheet+"_"+titaVo.getEntDyI());
		
		makeExcel.setValue(1, 1,"戶號","C");
		makeExcel.setValue(1, 2,"戶名","C");
		makeExcel.setValue(1, 3,"餘額","C");
		makeExcel.setValue(1, 4,"企金別","C");
		makeExcel.setValue(1, 5,"AML職業別","C");
		makeExcel.setValue(1, 6,"AML組織","C");
		
//		makeExcel.open(titaVo, titaVo.getEntDyI(), titaVo.getKinbr(), TXCD, TXName, TXCD + "_" + TXName, TXCD + "_底稿_" + TXName + ".xlsx", 1, SheetName);

		if (lList != null && lList.size() != 0) {

			int rowShift = 0;

			for (Map<String, String> tLDVo : lList) {

				int colShift = 0;

				for (int i = 0; i < tLDVo.size(); i++) {

					int col = i + pivotCol + colShift; // 1-based
					int row = pivotRow + rowShift; // 1-based

					// Query received will have column names in the format of F0, even if no alias
					// is set in SQL
					// notice it's 0-based for those names
					String tmpValue = tLDVo.get("F" + i);

					// switch by code of Column; i.e. Col A, Col B...
					// breaks if more than 26 columns!
					switch (String.valueOf((char) (65 + i))) {
					// if specific column needs special treatment, insert case here.
					case "B":
						makeExcel.setValue(row, col, tmpValue, "L");
						break;
					default:
						makeExcel.setValue(row, col, tmpValue, "C");
						break;
					}

				} // for

				rowShift++;

			} // for
		} else {
			makeExcel.setValue(2, 1, "本月無資料");
		}

		makeExcel.close();
		//makeExcel.toExcel(sno);
	}
	
	private void exportExcelB(TitaVo titaVo, List<Map<String, String>> lList) throws LogicException {

		this.info("L9725ReportB exportExcel");
		
		int reportDate = titaVo.getEntDyI() + 19110000;
		String brno = titaVo.getBrno();
		String txcd = TXCD;
		String fileItem = TXName+"(D1110505B)";
		String fileName = TXCD + "_" + TXName+"(D1110505B)";
//		String defaultExcel = TXCD + "_底稿_" + TXName + ".xlsx";
		String defaultSheet = "D1110505B";
		
		ReportVo reportVo = ReportVo.builder().setRptDate(reportDate).setBrno(brno).setRptCode(txcd)
				.setRptItem(fileItem).build();

		// 開啟報表
		makeExcel.open(titaVo, reportVo, fileName);
		makeExcel.setSheet(TXCD,defaultSheet+"_"+titaVo.getEntDyI());
		
//		makeExcel.open(titaVo, titaVo.getEntDyI(), titaVo.getKinbr(), TXCD, TXName, TXCD + "_" + TXName, TXCD + "_底稿_" + TXName + ".xlsx", 1, SheetName);
		makeExcel.setValue(1, 1,"戶號","C");
		makeExcel.setValue(1, 2,"額度","C");
		makeExcel.setValue(1, 3,"撥款","C");
		makeExcel.setValue(1, 4,"利率代碼","C");
		makeExcel.setValue(1, 5,"撥款日","C");
		makeExcel.setValue(1, 6,"到期日","C");
		makeExcel.setValue(1, 7,"核准額度","C");
		makeExcel.setValue(1, 8,"餘額","C");
		makeExcel.setValue(1, 9,"計件代碼","C");
		makeExcel.setValue(1, 10,"戶別","C");
		makeExcel.setValue(1, 11,"企金別","C");
		
		
		if (lList != null && lList.size() != 0) {

			int row = 2;

			for (Map<String, String> tLDVo : lList) {
				makeExcel.setValue(row, 1,Integer.valueOf(tLDVo.get("CustNo")),"R");
				makeExcel.setValue(row, 2,Integer.valueOf(tLDVo.get("FacmNo")),"R");
				makeExcel.setValue(row, 3,Integer.valueOf(tLDVo.get("BormNo")),"R");
				makeExcel.setValue(row, 4,tLDVo.get("ProdNo"),"L");
				makeExcel.setValue(row, 5,Integer.valueOf(tLDVo.get("DrawdownDate")),"C");
				makeExcel.setValue(row, 6,Integer.valueOf(tLDVo.get("MaturityDate")),"C");
				makeExcel.setValue(row, 7,new BigDecimal(tLDVo.get("LineAmt")),"R");
				makeExcel.setValue(row, 8,new BigDecimal(tLDVo.get("LoanBal")),"R");
				makeExcel.setValue(row, 9,tLDVo.get("PieceCode"),"L");
				makeExcel.setValue(row, 10,tLDVo.get("CuscCd"),"L");
				makeExcel.setValue(row, 11,tLDVo.get("EntCode"),"L");
				row++;

			} // for
		} else {
			makeExcel.setValue(2, 1, "本月無資料");
		}

		makeExcel.close();
		//makeExcel.toExcel(sno);
	}
}
