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

@Service("L9732p")
@Scope("prototype")
/**
 * 
 * 
 * @author Mata Wang
 * @version 1.0.0
 */
public class L9732p extends TradeBuffer {

	@Autowired
	L9732Report L9732Report;

	@Autowired
	DateUtil dDateUtil;

	@Autowired
	public WebClient webClient;

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active L9732p ");
		this.totaVo.init(titaVo);

		this.info("L9732p titaVo.getTxcd() = " + titaVo.getTxcd());
		String parentTranCode = titaVo.getTxcd();

		String content = "";

		L9732Report.setParentTranCode(parentTranCode);

		if (L9732Report.exec(titaVo)) {
			content = "L9732質押股票明細表已完成";
		} else {
			content = "L9732質押股票明細表查無資料";
		}

		webClient.sendPost(dDateUtil.getNowStringBc(), "1800", titaVo.getParam("TLRNO"), "Y", "LC009", titaVo.getParam("TLRNO") + "L9732", content, titaVo);

		this.addList(this.totaVo);
		return this.sendList();
	}

}