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

@Service("LY005p")
@Scope("prototype")
/**
 * 
 * 
 * @author Ted Lin
 * @version 1.0.0
 */
public class LY005p extends TradeBuffer {

	@Autowired
	LY005Report lY005Report;

	@Autowired
	DateUtil dDateUtil;

	@Autowired
	public WebClient webClient;

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active lY005p");
		this.totaVo.init(titaVo);

		this.info("LY005p titaVo.getTxcd() = " + titaVo.getTxcd());
		String parentTranCode = titaVo.getTxcd();

		lY005Report.setParentTranCode(parentTranCode);
		
		lY005Report.exec(titaVo);

		webClient.sendPost(dDateUtil.getNowStringBc(), dDateUtil.getNowStringTime(), titaVo.getParam("TLRNO"), "Y",
				"LC009", titaVo.getParam("TLRNO"), "LY005 非RBC_表20_會計部年度檢查報表 已完成", titaVo);

		this.addList(this.totaVo);
		return this.sendList();
	}
}