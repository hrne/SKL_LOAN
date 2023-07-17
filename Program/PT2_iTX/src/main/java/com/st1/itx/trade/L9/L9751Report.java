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
import com.st1.itx.db.service.springjpa.cm.L9751ServiceImpl;
import com.st1.itx.util.common.MakeExcel;
import com.st1.itx.util.common.MakeReport;
import com.st1.itx.util.common.data.ReportVo;
import com.st1.itx.util.date.DateUtil;
import com.st1.itx.util.format.FormatUtil;
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
		
		this.info("dDateUtil.getNowStringBc()" + dDateUtil.getNowStringBc());
		this.info("titaVo.getParam(\"TLRNO\"):" + titaVo.getParam("TLRNO"));
		String ntxbuf = titaVo.getTlrNo() + FormatUtil.padX("L9751", 60) + titaVo.getEntDyI();
		this.info("ntxbuf = " + ntxbuf);

		// 3.完成訊息
		webClient.sendPost(dDateUtil.getNowStringBc(), "1800", titaVo.getParam("TLRNO"), "Y", "LC009",
				titaVo.getParam("TLRNO"), tranCode + tranName + "已完成", titaVo);

	}
	
	private void makeReport(TitaVo titaVo) throws LogicException {
		this.info("L9751Report makeReport");
		ReportVo reportVo = ReportVo.builder().setRptDate(titaVo.getEntDyI()).setBrno(titaVo.getKinbr())
				.setRptCode(tranCode).setRptItem(tranName).build();
		
		String fileName = tranCode + "-"+ tranName;
		String defaultExcel = "L9751_底稿_暫收款-火險費餘額表.xlsx";
		String defaultSheet = "yyymm";
		makeExcel.open(titaVo, reportVo, fileName, defaultExcel, defaultSheet);

		if (resultList == null || resultList.isEmpty()) {
			makeExcel.setValue(4, 1, "查無資料");
		} else {
			int rowCursor = 4;
			Map<String, String> result, preResult;
			
			for (int i = 1; i < resultList.size(); i++) {
				result = resultList.get(i);
				preResult = resultList.get(i - 1);  //前日
				
				/**
				 * 報表欄位資料
				 * A欄-已繳火險費尚未請款金額(正常件)
				 * B欄-已繳火險費尚未請款金額(補請款件)或退費客戶: 固定0
				 * C欄-已繳火險費尚未沖銷借支: 固定0
				 * D欄-當期火險費暫收款餘額: A欄+B欄+C欄
				 * E欄-當期火險費暫收款變動額: 本日A欄-前日A欄
				 * F欄-當期火險費暫收款與當期傳票變動額之差額: E欄-L欄
				 * G欄-日期
				 * H欄-固定差額數
				 * I欄-會計帳累積餘額數
				 * J欄-當期借方傳票
				 * K欄-當期貸方傳票
				 * L欄-當期傳票變動額
				 * M欄-備註
				 * N欄-日期
				 * O欄-火險檔餘額
				 * P欄-沖銷請款/借支/轉催收傳票金額
				 * Q欄-放款火險費餘額變動: 前日O欄-P欄
				 * R欄-放款火險費餘額變動與當期傳票變動額之差額: Q-L欄
				 * 
				 */
				// 當期火險費暫收款變動額
				int acctMasterBalChg = Integer.parseInt(result.get("AcctMasterBal")) - Integer.parseInt(preResult.get("AcctMasterBal"));
				// 當期傳票變動額
				int amtChg = Integer.parseInt(result.get("CrAmt")) - Integer.parseInt(result.get("DbAmt"));
				// 放款火險費餘額變動
				int feeChg = Integer.parseInt(preResult.get("MasterClsAmt")) - Integer.parseInt(result.get("TxAmt"));
				// 日期
				result.get("AcDate=" + result.get("AcDate"));

				String date = (Integer.parseInt(result.get("AcDate").substring(0, 4)) - 1911) + "/"
						+ result.get("AcDate").substring(4, 6) + "/" + result.get("AcDate").substring(6);
				
				makeExcel.setValue(rowCursor, 1, result.get("AcctMasterBal"), "#,##0");
				makeExcel.setValue(rowCursor, 2, 0, "#,##0");
				makeExcel.setValue(rowCursor, 3, 0, "#,##0");
				makeExcel.setValue(rowCursor, 4, result.get("AcctMasterBal"), "#,##0");
				makeExcel.setValue(rowCursor, 5, acctMasterBalChg, "#,##0");
				makeExcel.setValue(rowCursor, 6, acctMasterBalChg - amtChg, "#,##0");
				makeExcel.setValue(rowCursor, 7, date, "#,##0");
				makeExcel.setValue(rowCursor, 8, 0, "#,##0");
				makeExcel.setValue(rowCursor, 9, result.get("TdBal"), "#,##0");
				makeExcel.setValue(rowCursor, 10, result.get("DbAmt"), "#,##0");
				makeExcel.setValue(rowCursor, 11, result.get("CrAmt"), "#,##0");
				makeExcel.setValue(rowCursor, 12, amtChg, "#,##0");
				makeExcel.setValue(rowCursor, 14, date, "#,##0"); //
				makeExcel.setValue(rowCursor, 15, result.get("MasterClsAmt"), "#,##0");
				makeExcel.setValue(rowCursor, 16, result.get("TxAmt"), "#,##0");
				makeExcel.setValue(rowCursor, 17, Integer.parseInt(result.get("MasterClsAmt")) - feeChg - amtChg, "#,##0");
				makeExcel.setValue(rowCursor, 18, feeChg, "#,##0");

				rowCursor++;
			}
			
			makeExcel.close();
		}
	
	}
}
