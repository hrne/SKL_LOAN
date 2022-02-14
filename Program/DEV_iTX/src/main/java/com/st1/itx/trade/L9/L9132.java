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

@Service("L9132")
@Scope("prototype")
/**
 * L9132 傳票媒體明細表（核心）
 * 
 * @author ChihWei
 * @version 1.0.0
 */
public class L9132 extends TradeBuffer {

	/* 報表服務注入 */
	@Autowired
	L9132Report l9132Report;

	@Autowired
	WebClient webClient;

	@Autowired
	DateUtil dDateUtil;

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active L9132 ");
		this.totaVo.init(titaVo);

		// 取出tita值
		// 會計日期 #AcDate=D,7,I
		int iAcDate = Integer.parseInt(titaVo.getParam("AcDate"));

		// 傳票批號 #BatchNo=A,2,I
		int iBatchNo = Integer.parseInt(titaVo.getParam("BatchNo"));

		// 媒體類別 #MediaType=A,1,I
		// 1: 出納票據; 2: 核心傳票; 3: 放款承諾;
//		int iMediaType = Integer.parseInt(titaVo.getParam("MediaType"));

		this.info("L9132 iAcDate = " + iAcDate);
		this.info("L9132 iBatchNo = " + iBatchNo);
//		this.info("L9132 iMediaType = " + iMediaType);

		doRpt(titaVo);

		// MSG帶入預設值
		String ntxbuf = titaVo.getTlrNo() + FormatUtil.padX("L9132", 60) + iAcDate;

		this.info("ntxbuf = " + ntxbuf);

		webClient.sendPost(dDateUtil.getNowStringBc(), "2300", titaVo.getTlrNo(), "Y", "LC009", ntxbuf, "L9132傳票媒體明細表(核心)已完成", titaVo);

		this.addList(this.totaVo);
		return this.sendList();
	}

	public void doRpt(TitaVo titaVo) throws LogicException {
		this.info("L9132 doRpt started.");

		String parentTranCode = titaVo.getTxcd();

		l9132Report.setParentTranCode(parentTranCode);

		// 撈資料組報表
		l9132Report.exec(titaVo);

		// 寫產檔記錄到TxReport
		long rptNo = l9132Report.close();

		// 產生PDF檔案
		l9132Report.toPdf(rptNo);

		this.info("L9132 doRpt finished.");

	}
}