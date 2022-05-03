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
	L2633Report l2633Report;
	@Autowired
	public WebClient webClient;
	@Autowired
	public DateUtil dateUtil;

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active L2633Batch ");

//		產出清償日報
		doRptA(titaVo);

		String checkMsg = "清償日報表產檔已完成。";
		webClient.sendPost(dateUtil.getNowStringBc(), "2300", titaVo.getTlrNo(), "Y", "LC009",
				titaVo.getTlrNo() + "L2633", checkMsg, titaVo);

		return this.sendList();
	}

	public void doRptA(TitaVo titaVo) throws LogicException {
		this.info("L2633 doRpt started.");
		l2633Report.setTxBuffer(txBuffer);
		String parentTranCode = titaVo.getTxcd();

		l2633Report.setParentTranCode(parentTranCode);

		// 撈資料組報表
		l2633Report.exec(titaVo);

		// 寫產檔記錄到TxReport
		long rptNo = l2633Report.close();

		// 產生PDF檔案
		l2633Report.toPdf(rptNo);

		this.info("L2633 doRpt finished.");

	}

}