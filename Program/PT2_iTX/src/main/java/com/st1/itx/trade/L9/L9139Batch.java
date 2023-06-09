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
import com.st1.itx.util.http.WebClient;

/**
 * L9139Batch
 * 
 * @author Chih Wei
 * @version 1.0.0
 */
@Service("L9139Batch")
@Scope("step")
public class L9139Batch extends TradeBuffer {

	@Autowired
	L9139Report oL9139Report;

	@Autowired
	DateUtil dateUtil;

	@Autowired
	WebClient webClient;

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {

		String tranCode = "L9139";
		String tranName = "暫收款日餘額前後差異比較表";
		
		this.info("active L9139Batch ");
		this.totaVo.init(titaVo);

		this.info(tranCode + "p titaVo.getTxcd() = " + titaVo.getTxcd());
		String parentTranCode = titaVo.getTxcd();

		oL9139Report.setParentTranCode(parentTranCode);

		// 帳務日(民國)
		int tbsdy = this.txBuffer.getTxCom().getTbsdy();

		// ServiceImpl.findAll 接收民國年月日
		titaVo.putParam("StartDate", tbsdy);	
				
		boolean isFinish = oL9139Report.exec(titaVo);

		if (isFinish) {
			webClient.sendPost(dateUtil.getNowStringBc(), "1800", titaVo.getParam("TLRNO"), "Y", "LC009", titaVo.getParam("TLRNO"), tranCode + tranName + "已完成", titaVo);
		} else {
			webClient.sendPost(dateUtil.getNowStringBc(), "1800", titaVo.getParam("TLRNO"), "Y", "LC009", titaVo.getParam("TLRNO"), tranCode + tranName + "查無資料", titaVo);
		}

		this.addList(this.totaVo);
		return this.sendList();
	}
}