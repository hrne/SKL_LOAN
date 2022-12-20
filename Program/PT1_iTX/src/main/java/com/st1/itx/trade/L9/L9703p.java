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
 * L9703p
 * 
 * @author ChihWei
 * @version 1.0.0
 */
@Service("L9703p")
@Scope("prototype")
public class L9703p extends TradeBuffer {

	@Autowired
	L9703Report l9703report;

	@Autowired
	WebClient webClient;

	@Autowired
	DateUtil dDateUtil;

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active L9703p ");
		this.totaVo.init(titaVo);

		this.info("L9703p titaVo.getTxcd() = " + titaVo.getTxcd());
		
		String tran = titaVo.getTxCode().isEmpty() ? "L9703" : titaVo.getTxCode();
		
		String parentTranCode = titaVo.getTxcd();

		l9703report.setParentTranCode(parentTranCode);

		l9703report.exec(titaVo, this.getTxBuffer());

		webClient.sendPost(dDateUtil.getNowStringBc(), "1800", titaVo.getParam("TLRNO"), "Y", "LC009",
				titaVo.getParam("TLRNO") + tran, tran + " 滯繳客戶明細表 已完成", titaVo);

		this.addList(this.totaVo);
		return this.sendList();
	}

}