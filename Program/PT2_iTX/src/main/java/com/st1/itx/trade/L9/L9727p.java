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
 * L9727p
 * 
 * @author ChihWei
 * @version 1.0.0
 */
@Service("L9727p")
@Scope("prototype")
public class L9727p extends TradeBuffer {

	@Autowired
	L9727Report l9727Report;

	@Autowired
	DateUtil dDateUtil;

	@Autowired
	WebClient webClient;

	String txcd = "L9727";
	String txname = "金檢Q53資料";

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active " + txcd + "p");
		this.totaVo.init(titaVo);

		this.info(txcd + "p titaVo.getTxcd() = " + titaVo.getTxcd());
		String parentTranCode = titaVo.getTxcd();

		l9727Report.setParentTranCode(parentTranCode);

		boolean isFinish = l9727Report.exec(titaVo);

		if (isFinish) {
			webClient.sendPost(dDateUtil.getNowStringBc(), "1800", titaVo.getTlrNo(), "Y", "LC009", titaVo.getTlrNo() + "L9727", txcd + txname + "已完成", titaVo);
		}

		this.addList(this.totaVo);
		return this.sendList();
	}
}