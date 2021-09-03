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

/**
 * LM048 企業放款風險承擔限額控管表
 * 
 * @author Chih Wei Huang
 * @version 1.0.0
 */
@Service("LM048p")
@Scope("prototype")
public class LM048p extends TradeBuffer {

	@Autowired
	LM048Report lM048Report;

	@Autowired
	WebClient webClient;

	@Autowired
	DateUtil dDateUtil;

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active LM048p ");
		this.totaVo.init(titaVo);


		this.info("LM048p titaVo.getTxcd() = " + titaVo.getTxcd());
		String parentTranCode = titaVo.getTxcd();

		lM048Report.setParentTranCode(parentTranCode);
		
		lM048Report.exec(titaVo);

		webClient.sendPost(dDateUtil.getNowStringBc(), dDateUtil.getNowStringTime(), titaVo.getTlrNo(), "Y", "LC009",
				titaVo.getTlrNo(), "LM049企業放款風險承擔限額控管表", titaVo);
		
		this.addList(this.totaVo);
		return this.sendList();
	}
}