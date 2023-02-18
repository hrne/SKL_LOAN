package com.st1.itx.trade.LY;

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

@Service("LY006p")
@Scope("prototype")
/**
 * 
 * 
 * @author Ted Lin
 * @version 1.0.0
 */
public class LY006p extends TradeBuffer {

	@Autowired
	LY006Report lY006Report;

	@Autowired
	DateUtil dDateUtil;

	@Autowired
	public WebClient webClient;

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active lY006p");
		this.totaVo.init(titaVo);

		this.info("LY006p titaVo.getTxcd() = " + titaVo.getTxcd());
		String parentTranCode = titaVo.getTxcd();

		lY006Report.setParentTranCode(parentTranCode);

		boolean isFinish = lY006Report.exec(titaVo);
		if (isFinish) {
			webClient.sendPost(dDateUtil.getNowStringBc(), dDateUtil.getNowStringTime(), titaVo.getParam("TLRNO"), "Y", "LC009", titaVo.getParam("TLRNO"), "LY006 B117關係人明細表已完成", titaVo);
		} else {
			webClient.sendPost(dDateUtil.getNowStringBc(), dDateUtil.getNowStringTime(), titaVo.getParam("TLRNO"), "Y", "LC009", titaVo.getParam("TLRNO"), "LY006 B117關係人明細表查無資料", titaVo);
		}

		this.addList(this.totaVo);
		return this.sendList();
	}
}