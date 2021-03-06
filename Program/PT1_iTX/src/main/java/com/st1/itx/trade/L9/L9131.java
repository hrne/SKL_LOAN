package com.st1.itx.trade.L9;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.dataVO.TotaVo;
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.date.DateUtil;
import com.st1.itx.util.format.FormatUtil;
import com.st1.itx.util.http.WebClient;

@Service("L9131")
@Scope("prototype")
/**
 * L9131 核心日結單代傳票列印
 * 
 * @author ChihWei
 * @version 1.0.0
 */
public class L9131 extends TradeBuffer {

	/* 報表服務注入 */
	@Autowired
	L9131Report l9131Report;

	@Autowired
	WebClient webClient;

	@Autowired
	DateUtil dDateUtil;

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active L9131 ");
		this.totaVo.init(titaVo);

		// 取出tita值
		// 會計日期 #AcDate=D,7,I
		int iAcDate = Integer.parseInt(titaVo.getParam("AcDate"));

		// 傳票批號 #BatchNo=A,2,I
		int iBatchNo = Integer.parseInt(titaVo.getParam("BatchNo"));

		this.info("L9131 iAcDate = " + iAcDate);
		this.info("L9131 iBatchNo = " + iBatchNo);

		doRpt(titaVo);

		// MSG帶入預設值
		String ntxbuf = titaVo.getTlrNo() + FormatUtil.padX("L9131", 60) + iAcDate;

		this.info("ntxbuf = " + ntxbuf);
		
		webClient.sendPost(dDateUtil.getNowStringBc(), "2300", titaVo.getTlrNo(), "Y", "LC009", ntxbuf, "L9131總帳日結單代傳票已完成", titaVo);

		this.addList(this.totaVo);
		return this.sendList();
	}

	public void doRpt(TitaVo titaVo) throws LogicException {
		this.info("L9131 doRpt started.");

		String parentTranCode = titaVo.getTxcd();

		l9131Report.setParentTranCode(parentTranCode);

		// 撈資料組報表
		l9131Report.exec(titaVo);

		// 寫產檔記錄到TxReport
		l9131Report.close();

		this.info("L9131 doRpt finished.");

	}
}