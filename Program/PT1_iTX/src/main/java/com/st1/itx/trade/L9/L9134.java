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

@Service("L9134")
@Scope("prototype")
/**
 * L9134 暫收款傳票金額表(累計/明細)
 * 
 * @author ChihWei
 * @version 1.0.0
 */
public class L9134 extends TradeBuffer {

	/* 報表服務注入 */
	@Autowired
	L9134Report l9134Report;

	/* 報表服務注入 */
	@Autowired
	L9134Report2 l9134Report2;
	
	/* 報表服務注入 */
	@Autowired
	L9134Report3 l9134Report3;
	
	/* 報表服務注入 */
	@Autowired
	L9134Report4 l9134Report4;

	
	@Autowired
	WebClient webClient;

	@Autowired
	DateUtil dDateUtil;

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active L9134 ");
		this.totaVo.init(titaVo);

		int startDate = Integer.parseInt(titaVo.getParam("StartDate")) + 19110000;
		int endDate = Integer.parseInt(titaVo.getParam("EndDate")) + 19110000;

		this.info("startDate = " + startDate);
		this.info("endDate = " + endDate);

		String parentTranCode = titaVo.getTxcd();

		l9134Report.setParentTranCode(parentTranCode);
		l9134Report.exec(startDate, endDate, titaVo);

		l9134Report2.setParentTranCode(parentTranCode);
		l9134Report2.exec(startDate, endDate, titaVo);
		

		l9134Report3.setParentTranCode(parentTranCode);
		l9134Report3.exec(endDate, titaVo);
		
		
		l9134Report4.setParentTranCode(parentTranCode);
		l9134Report4.exec(endDate,titaVo);


		// 交易櫃員
		String empNo = titaVo.getTlrNo();

		// MSG帶入預設值
		String ntxbuf = titaVo.getTlrNo() + FormatUtil.padX("L9134", 60) + (endDate - 19110000);

		this.info("ntxbuf = " + ntxbuf);

		webClient.sendPost(dDateUtil.getNowStringBc(), "2300", empNo, "Y", "LC009", ntxbuf, "L9134暫收款傳票金額表已完成", titaVo);

		this.addList(this.totaVo);
		return this.sendList();
	}
}