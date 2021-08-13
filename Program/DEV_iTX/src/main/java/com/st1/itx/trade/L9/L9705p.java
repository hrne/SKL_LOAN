package com.st1.itx.trade.L9;

import java.util.ArrayList;

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

/**
 * L9705p
 * 
 * @author ChihWei
 * @version 1.0.0
 */
@Service("L9705p")
@Scope("prototype")
public class L9705p extends TradeBuffer {
	// private static final Logger logger = LoggerFactory.getLogger(L9705p.class);

	@Autowired
	L9705Report l9705Report;

	@Autowired
	WebClient webClient;

	@Autowired
	DateUtil dDateUtil;

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active L9705p ");
		this.totaVo.init(titaVo);
		TxBuffer txbuffer = this.getTxBuffer();

		this.info("L9705p titaVo.getTxcd() = " + titaVo.getTxcd());
		String parentTranCode = titaVo.getTxcd();

		l9705Report.setParentTranCode(parentTranCode);

		l9705Report.exec(titaVo, txbuffer);

		webClient.sendPost(dDateUtil.getNowStringBc(), "1800", titaVo.getParam("TLRNO"), "Y", "LC009",
				titaVo.getParam("TLRNO"), "L9705放款本息攤還表暨繳息通知單已完成", titaVo);

		this.addList(this.totaVo);
		return this.sendList();
	}

}