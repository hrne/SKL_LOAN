package com.st1.itx.trade.LM;

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

@Service("LM031p")
@Scope("prototype")
/**
 * 
 * 
 * @author ChihWei
 * @version 1.0.0
 */
public class LM031p extends TradeBuffer {

	@Autowired
	LM031Report lM031Report;

	@Autowired
	DateUtil dDateUtil;

	@Autowired
	public WebClient webClient;

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active LM031p");
		this.totaVo.init(titaVo);

		this.info("LM031p titaVo.getTxcd() = " + titaVo.getTxcd());
		String parentTranCode = titaVo.getTxcd();

		lM031Report.setParentTranCode(parentTranCode);

		boolean isFinish = lM031Report.exec(titaVo);

		if (isFinish) {
			webClient.sendPost(dDateUtil.getNowStringBc(), "1800", titaVo.getParam("TLRNO"), "Y", "LC009", titaVo.getParam("TLRNO"), "LM031企業動用率已完成", titaVo);
		} else {
			webClient.sendPost(dDateUtil.getNowStringBc(), "1800", titaVo.getParam("TLRNO"), "Y", "LC009", titaVo.getParam("TLRNO"), "LM031企業動用率查無資料", titaVo);
		}

		this.addList(this.totaVo);
		return this.sendList();
	}
}