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
 * L9715p
 * 
 * @author Ted
 * @version 1.0.0
 */
@Service("L9715p")
@Scope("prototype")
public class L9715p extends TradeBuffer {

	@Autowired
	public L9715Report l9715report;

	@Autowired
	public WebClient webClient;

	@Autowired
	public DateUtil dDateUtil;

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active L9715p ");
		this.totaVo.init(titaVo);

		this.info("L9715p titaVo.getTxcd() = " + titaVo.getTxcd());
		String parentTranCode = titaVo.getTxcd();

		l9715report.setParentTranCode(parentTranCode);

		l9715report.exec(titaVo, this.getTxBuffer());

		webClient.sendPost(dDateUtil.getNowStringBc(), "1800", titaVo.getParam("TLRNO"), "Y", "LC009", titaVo.getParam("TLRNO")+"L9715", "L9715業務專辦照顧十八個月明細表已完成", titaVo);

		this.addList(this.totaVo);
		return this.sendList();
	}

}