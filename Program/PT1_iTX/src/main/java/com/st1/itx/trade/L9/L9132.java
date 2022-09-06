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
	L9132ReportA l9132ReportA;
	@Autowired
	L9132ReportB l9132ReportB;
	@Autowired
	L9132ReportC l9132ReportC;
	@Autowired
	L9132ReportD l9132ReportD;

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

		webClient.sendPost(dDateUtil.getNowStringBc(), "2300", titaVo.getTlrNo(), "Y", "LC009", ntxbuf, "L9132傳票媒體明細表(總帳)已完成", titaVo);

		this.addList(this.totaVo);
		return this.sendList();
	}

	public void doRpt(TitaVo titaVo) throws LogicException {
		this.info("L9132 doRpt started.");

		String parentTranCode = titaVo.getTxcd();

		l9132Report.setParentTranCode(parentTranCode);
		l9132ReportA.setParentTranCode(parentTranCode);
		l9132ReportB.setParentTranCode(parentTranCode);
		l9132ReportC.setParentTranCode(parentTranCode);
		l9132ReportD.setParentTranCode(parentTranCode);

		// 製作報表-傳票媒體明細表
		l9132Report.exec(titaVo);
		l9132Report.close();

		// 製作報表-傳票媒體總表
		l9132ReportA.exec(titaVo);
		l9132ReportA.close();

		// 製作報表-傳票媒體明細表-交易序號
		l9132ReportB.exec(titaVo);
		l9132ReportB.close();

		// 製作報表-傳票媒體明細表-櫃員編號
		l9132ReportC.exec(titaVo);
		l9132ReportC.close();

		// 製作報表-放款部日計表
		l9132ReportD.exec(titaVo);
		l9132ReportD.close();

		this.info("L9132 doRpt finished.");
	}
}