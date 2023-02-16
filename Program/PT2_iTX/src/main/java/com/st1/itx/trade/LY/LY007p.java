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

@Service("LY007p")
@Scope("prototype")
/**
 * 
 * 
 * @author Ted Lin
 * @version 1.0.0
 */
public class LY007p extends TradeBuffer {

	@Autowired
	LY007Report lY007Report;

	@Autowired
	DateUtil dDateUtil;

	@Autowired
	public WebClient webClient;

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active lY007p");
		this.totaVo.init(titaVo);

		this.info("LY007p titaVo.getTxcd() = " + titaVo.getTxcd());
		String parentTranCode = titaVo.getTxcd();

		lY007Report.setParentTranCode(parentTranCode);

		boolean isFinish = lY007Report.exec(titaVo);
		if (isFinish) {
			webClient.sendPost(dDateUtil.getNowStringBc(), dDateUtil.getNowStringTime(), titaVo.getParam("TLRNO"), "Y", "LC009", titaVo.getParam("TLRNO"), "LY007 Z100關係人交易明細表已完成", titaVo);
		} else {
			webClient.sendPost(dDateUtil.getNowStringBc(), dDateUtil.getNowStringTime(), titaVo.getParam("TLRNO"), "Y", "LC009", titaVo.getParam("TLRNO"), "LY007 Z100關係人交易明細表查無資料", titaVo);
		}

		this.addList(this.totaVo);
		return this.sendList();
	}
}