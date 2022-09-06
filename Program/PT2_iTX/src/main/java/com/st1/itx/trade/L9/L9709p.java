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
 * 
 * 
 * @author Eric Chang
 * @version 1.0.0
 */
@Service("L9709p")
@Scope("prototype")
public class L9709p extends TradeBuffer {

	@Autowired
	public L9709Report l9709Report;

	@Autowired
	DateUtil dDateUtil;

	@Autowired
	WebClient webClient;

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active L9709p ");
		this.totaVo.init(titaVo);

		this.info("L9709p titaVo.getTxcd() = " + titaVo.getTxcd());
		String parentTranCode = titaVo.getTxcd();

		l9709Report.setParentTranCode(parentTranCode);

		l9709Report.exec(titaVo);

		webClient.sendPost(dDateUtil.getNowStringBc(), "1800", titaVo.getTlrNo(), "Y", "LC009", titaVo.getTlrNo() + "L9709", "L9709暫收放貸核心傳票檔資料已完成", titaVo);

		this.addList(this.totaVo);
		return this.sendList();
	}

}