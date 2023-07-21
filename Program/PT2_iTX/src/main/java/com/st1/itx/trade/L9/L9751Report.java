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
import com.st1.itx.db.service.springjpa.cm.L9751ServiceImpl;
import com.st1.itx.util.common.MakeExcel;
import com.st1.itx.util.common.MakeReport;
import com.st1.itx.util.common.data.ReportVo;
import com.st1.itx.util.date.DateUtil;
import com.st1.itx.util.http.WebClient;

@Component
@Scope("prototype")
public class L9751Report extends MakeReport {
	@Autowired
	L9751ServiceImpl l9751ServiceImpl;
	
	@Autowired
	private MakeExcel makeExcel;
	
	@Autowired
	WebClient webClient;
	
	@Autowired
	DateUtil dDateUtil;
	
	private List<Map<String, String>> resultList;
	private final String tranCode = "L9751";
	private final String tranName = "暫收款-火險費餘額表";

	public void exec(TitaVo titaVo) throws LogicException {
		this.info("L9751Report exec");
		
		// 1.撈資料
		try {
			resultList = l9751ServiceImpl.findAll(titaVo);
		} catch (LogicException e) {
			StringWriter errors = new StringWriter();
			e.printStackTrace(new PrintWriter(errors));
			this.error("l9751ServiceImpl.findAll error = " + errors.toString());
		}
		
		// 2.製作報表
		makeReport(titaVo);
		
		// 3.完成訊息
		webClient.sendPost(dDateUtil.getNowStringBc(), "1800", titaVo.getParam("TLRNO"), "Y", "LC009",
				titaVo.getParam("TLRNO"), tranCode + tranName + "已完成", titaVo);

	}
	
	private void makeReport(TitaVo titaVo) throws LogicException {
		this.info("L9751Report makeReport");
		ReportVo reportVo = ReportVo.builder().setRptDate(titaVo.getEntDyI()).setBrno(titaVo.getKinbr())
				.setRptCode(tranCode).setRptItem(tranName).build();
		
		// 條件：日期
		String inputDate = titaVo.get("EndDate");
		String yyy = inputDate.substring(0, 3);
		String mm = inputDate.substring(3, 5);
		
		String fileName = tranCode + "-" + tranName;
		String defaultExcel = "L9751_底稿_暫收款-火險費餘額表.xlsx";
		String defaultSheet = "yyymm";
		

		makeExcel.open(titaVo, reportVo, fileName, defaultExcel, defaultSheet);
		makeExcel.setSheet("yyymm", yyy + mm);
		// 報表標題
		String title = String.format("%s %s暫收款-火險費 餘額表 20222010000", yyy, mm);
		makeExcel.setValue(1, 1, title, "C");

		int rowCursor = 4;

		// 調整excel樣式，如有兩筆資料以上資料將樣式插入帶下。
		// size>3是因為撈出資料第一筆是提供後端計算用，實際呈現於excel只有size-1筆資料。
		if(resultList.size() > 3) {
			makeExcel.setShiftRow(rowCursor + 1, resultList.size() - 2);
		}
		
		if (resultList.size() > 1) {		
			this.info("有資料，開始產製excel");
			/**
			 * 報表欄位資料
			 * A欄-已繳火險費尚未請款金額(正常件)
			 * B欄-已繳火險費尚未請款金額(補請款件)或退費客戶: 固定0
			 * C欄-已繳火險費尚未沖銷借支: 固定0
			 * D欄-當期火險費暫收款餘額: A欄+B欄+C欄 
			 * E欄-當期火險費暫收款變動額: 本日A欄-前日A欄
			 * F欄-當期火險費暫收款與當期傳票變動額之差額: E欄-L欄
			 * G欄-日期
			 * H欄-固定差額數: D欄-I欄
			 * I欄-會計帳累積餘額數: 前日I欄+K欄-J欄  !!!!
			 * J欄-當期借方傳票
			 * K欄-當期貸方傳票
			 * L欄-當期傳票變動額
			 * M欄-備註
			 * N欄-日期
			 * O欄-火險檔餘額
			 * P欄-沖銷請款/借支/轉催收傳票金額
			 * Q欄-放款火險費餘額變動: 前日O欄-P欄  !!!!
			 * R欄-放款火險費餘額變動與當期傳票變動額之差額: Q欄-L欄
			 * 
			 */
			Map<String, String> result, preResult;
			
			for (int i = 1; i < resultList.size(); i++) {
				result = resultList.get(i);
				// A欄-已繳火險費尚未請款金額(正常件)
				BigDecimal acctMasterBal = getBigDecimal(result.get("AcctMasterBal"));
				// G欄、N欄-日期
				String date = (Integer.parseInt(result.get("AcDate").substring(0, 4)) - 1911) + "/"
						+ result.get("AcDate").substring(4, 6) + "/" + result.get("AcDate").substring(6);
				// I欄-會計帳累積餘額數
				BigDecimal dbAmt = getBigDecimal(result.get("DbAmt"));
				// J欄-當期借方傳票
				BigDecimal tdBal = getBigDecimal(result.get("TdBal"));
				// K欄-當期貸方傳票
				BigDecimal crAmt = getBigDecimal(result.get("CrAmt"));
				// O欄-火險檔餘額
				BigDecimal masterClsAmt = getBigDecimal(result.get("MasterClsAmt"));
				// P欄-沖銷請款/借支/轉催收傳票金額
				BigDecimal txAmt = getBigDecimal(result.get("TxAmt"));

				// 公式
				// D欄-當期火險費暫收款餘額: A欄+B欄+C欄 
				String columnD = String.format("SUM(A%s:C%s)", rowCursor, rowCursor);
				// E欄-當期火險費暫收款變動額: 本日A欄-前日A欄
				String columnE = String.format("D%s-D%s", rowCursor, rowCursor - 1);
				// F欄-當期火險費暫收款與當期傳票變動額之差額: E欄-L欄
				String columnF = String.format("E%s-L%s", rowCursor, rowCursor);
				// H欄-固定差額數
				String columnH = String.format("D%s-I%s", rowCursor, rowCursor);
				// L欄-當期傳票變動額
				String columnL = String.format("K%s-J%s", rowCursor, rowCursor);
				// Q欄-放款火險費餘額變動: 前日O欄-P欄 
				String columnQ = String.format("O%s-O%s-P%s", rowCursor, rowCursor - 1, rowCursor);
				// R欄-放款火險費餘額變動與當期傳票變動額之差額
				String columnR = String.format("Q%s-L%s", rowCursor, rowCursor);
				
				makeExcel.setValue(rowCursor, 1, acctMasterBal, "#,##0");
				makeExcel.setValue(rowCursor, 2, BigDecimal.ZERO, "#,##0");
				makeExcel.setValue(rowCursor, 3, BigDecimal.ZERO, "#,##0");
				makeExcel.setFormula(rowCursor, 4, BigDecimal.ZERO, columnD, "#,##0");
				makeExcel.setFormula(rowCursor, 6, BigDecimal.ZERO, columnF, "#,##0");
				makeExcel.setValue(rowCursor, 7, date, "#,##0");
				makeExcel.setFormula(rowCursor, 8, BigDecimal.ZERO, columnH, "#,##0");
				makeExcel.setValue(rowCursor, 9, tdBal, "#,##0");
				makeExcel.setValue(rowCursor, 10, dbAmt, "#,##0");
				makeExcel.setValue(rowCursor, 11, crAmt, "#,##0");
				makeExcel.setFormula(rowCursor, 12, BigDecimal.ZERO, columnL, "#,##0");
				makeExcel.setValue(rowCursor, 14, date, "#,##0"); 
				makeExcel.setValue(rowCursor, 15, masterClsAmt, "#,##0");
				makeExcel.setValue(rowCursor, 16, txAmt, "#,##0");
				makeExcel.setFormula(rowCursor, 18, BigDecimal.ZERO, columnR, "#,##0");
				
				// 第一筆因報表無前一筆資料，所以無法用公式算出，需在後端進行處理
				if (i == 1) {
					// 前日資料
					preResult = resultList.get(i - 1);
					
					// E欄-當期火險費暫收款變動額: 本日A欄-前日A欄
					BigDecimal acctMasterBalChg = acctMasterBal.subtract(getBigDecimal(preResult.get("AcctMasterBal")));
					// (前一筆)O欄-火險檔餘額
					BigDecimal preMasterClsAmt = getBigDecimal(preResult.get("MasterClsAmt"));
					
					makeExcel.setValue(rowCursor, 5, acctMasterBalChg, "#,##0");
					makeExcel.setValue(rowCursor, 17, masterClsAmt.subtract(preMasterClsAmt).subtract(txAmt), "#,##0");
					
				} else {
					makeExcel.setFormula(rowCursor, 5, BigDecimal.ZERO, columnE, "#,##0");
					makeExcel.setFormula(rowCursor, 17, BigDecimal.ZERO, columnQ, "#,##0");
				}
				rowCursor++;
			}
			makeExcel.formulaRangeCalculate(4, rowCursor , 1, 18);
		} else {
			this.info("無資料");
			makeExcel.setValue(rowCursor, 1, "查無資料");
			
		}
		makeExcel.close();
	}
}
