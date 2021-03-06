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
 * L9728p
 * 
 * @author xiangwei
 * @version 1.0.0
 */
@Service("L9729p")
@Scope("prototype")
public class L9729p extends TradeBuffer {

	@Autowired
	L9729Report L9729Report;

	@Autowired
	DateUtil dDateUtil;

	@Autowired
	WebClient webClient;

	String txcd = "L9729";
	String txname = "歷史封存資料搬運結果明細";

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active " + txcd + "p");
		this.totaVo.init(titaVo);

		this.info(txcd + "p titaVo.getTxcd() = " + titaVo.getTxcd());
		String parentTranCode = titaVo.getTxcd();

		L9729Report.setParentTranCode(parentTranCode);

		boolean isFinish = L9729Report.exec(titaVo);

		if (isFinish) {
			webClient.sendPost(dDateUtil.getNowStringBc(), "1800", titaVo.getTlrNo(), "Y", "LC009", titaVo.getTlrNo()+"L9729",
					txcd + txname + "已完成", titaVo);
		} else {
			webClient.sendPost(dDateUtil.getNowStringBc(), "1800", titaVo.getTlrNo(), "Y", "LC009", titaVo.getTlrNo()+"L9729",
					txcd + txname + "查無資料", titaVo);
		}

		this.addList(this.totaVo);
		return this.sendList();
	}
}