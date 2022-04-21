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
 * L9730p
 * 
 * @author xiangwei
 * @version 1.0.0
 */
@Service("L9730p")
@Scope("prototype")
public class L9730p extends TradeBuffer {

	@Autowired
	L9730Report l9730Report;

	@Autowired
	DateUtil dDateUtil;

	@Autowired
	WebClient webClient;

	String txcd = "L9730";
	String txname = "定期機動資料檢核";

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active " + txcd + "p");
		this.totaVo.init(titaVo);

		this.info(txcd + "p titaVo.getTxcd() = " + titaVo.getTxcd());
		String parentTranCode = titaVo.getTxcd();

		l9730Report.setParentTranCode(parentTranCode);

		boolean isFinish = l9730Report.exec(titaVo);

		if (isFinish) {
			webClient.sendPost(dDateUtil.getNowStringBc(), "1800", titaVo.getTlrNo(), "Y", "LC009", titaVo.getTlrNo(),
					txcd + txname + "已完成", titaVo);
		} else {
			webClient.sendPost(dDateUtil.getNowStringBc(), "1800", titaVo.getTlrNo(), "Y", "LC009", titaVo.getTlrNo(),
					txcd + txname + "查無資料", titaVo);
		}

		this.addList(this.totaVo);
		return this.sendList();
	}
}