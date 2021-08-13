package com.st1.itx.trade.L9;

import java.util.ArrayList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.st1.itx.Exception.LogicException;
import com.st1.itx.buffer.TxBuffer;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.dataVO.TotaVo;
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.date.DateUtil;
import com.st1.itx.util.http.WebClient;

@Service("L9715p")
@Scope("prototype")
/**
 * 
 * 
 * @author Eric Chang
 * @version 1.0.0
 */
public class L9715p extends TradeBuffer {
	private static final Logger logger = LoggerFactory.getLogger(L9715p.class);

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
		TxBuffer txbuffer = this.getTxBuffer();

		this.info("L9715p titaVo.getTxcd() = " + titaVo.getTxcd());
		String parentTranCode = titaVo.getTxcd();

		l9715report.setParentTranCode(parentTranCode);
		
		l9715report.exec(titaVo, txbuffer);
		
		webClient.sendPost(dDateUtil.getNowStringBc(), "1800", titaVo.getParam("TLRNO"), "Y", "LC009", titaVo.getParam("TLRNO"), "L9715放款到期明細表及通知單已完成", titaVo);
		
		this.addList(this.totaVo);
		return this.sendList();
	}

}