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
 * L9719p
 * 
 * @author ChihWei
 * @version 1.0.0
 */
@Service("L9719p")
@Scope("prototype")
public class L9719p extends TradeBuffer {

	@Autowired
	L9719Report l9719Report;

	@Autowired
	DateUtil dDateUtil;

	@Autowired
	WebClient webClient;

	String tranCode = "L9719";
	String tranName = "放款利息法折溢價攤銷表";

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active " + tranCode + "p");
		this.totaVo.init(titaVo);

		this.info(tranCode + "p titaVo.getTxcd() = " + titaVo.getTxcd());
		String parentTranCode = titaVo.getTxcd();

		l9719Report.setParentTranCode(parentTranCode);

		boolean isFinish = l9719Report.exec(titaVo);

		if (isFinish) {
			webClient.sendPost(dDateUtil.getNowStringBc(), "1800", titaVo.getParam("TLRNO"), "Y", "LC009", titaVo.getParam("TLRNO")+"L9719", tranCode + tranName + "已完成", titaVo);
		} else {
			webClient.sendPost(dDateUtil.getNowStringBc(), "1800", titaVo.getParam("TLRNO"), "Y", "LC009", titaVo.getParam("TLRNO")+"L9719", tranCode + tranName + "查無資料", titaVo);
		}

		this.addList(this.totaVo);
		return this.sendList();
	}
}