package com.st1.itx.trade.L2;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.dataVO.TotaVo;
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.date.DateUtil;
import com.st1.itx.util.http.WebClient;
import com.st1.itx.util.parse.Parse;

@Service("L2633Batch")
@Scope("prototype")
/**
 * 清償日報
 * 
 * @author Zi-Jun,Huang
 * @version 1.0.0
 */
public class L2633Batch extends TradeBuffer {

	/* 報表服務注入 */
	@Autowired
	L2633ReportA l2633ReportA;
	@Autowired
	L2633ReportB l2633ReportB;
	@Autowired
	public WebClient webClient;
	@Autowired
	public DateUtil dateUtil;
	@Autowired
	public Parse parse;

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active L2633Batch ");

		int iEntryDate = parse.stringToInteger(titaVo.getParam("TranDate"));
		int iApplDate = parse.stringToInteger(titaVo.getParam("ApplDate"));
		if (iEntryDate > 0) {
//			產出清償日報(一般結案)
			doRptA(titaVo);
		}
		if (iApplDate > 0) {
//			產出清償日報(限補領補發)
			doRptB(titaVo);
		}

		String checkMsg = "清償日報表產檔已完成。";
		webClient.sendPost(dateUtil.getNowStringBc(), "2300", titaVo.getTlrNo(), "Y", "LC009", titaVo.getTlrNo() + "L2633", checkMsg, titaVo);

		return this.sendList();
	}

	public void doRptA(TitaVo titaVo) throws LogicException {
		this.info("L2633ReportA started.");
		l2633ReportA.setTxBuffer(txBuffer);
		String parentTranCode = titaVo.getTxcd();

		l2633ReportA.setParentTranCode(parentTranCode);

		// 撈資料組報表
		l2633ReportA.exec(titaVo);

		// 寫產檔記錄到TxReport
		l2633ReportA.close();

		this.info("L2633 doRpt finished.");

	}

	public void doRptB(TitaVo titaVo) throws LogicException {
		this.info("L2633ReportB started.");
		l2633ReportB.setTxBuffer(txBuffer);
		String parentTranCode = titaVo.getTxcd();

		l2633ReportB.setParentTranCode(parentTranCode);

		// 撈資料組報表
		l2633ReportB.exec(titaVo);

		// 寫產檔記錄到TxReport
		l2633ReportB.close();

		this.info("L2633 doRpt finished.");

	}

}