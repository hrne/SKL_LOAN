package com.st1.itx.trade.L9;

import java.util.ArrayList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.dataVO.TotaVo;
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.MySpring;

/**
 * L9720
 * 
 * @author ChihWei
 * @version 1.0.0
 */
@Service("L9720")
@Scope("prototype")
public class L9720 extends TradeBuffer {
	private static final Logger logger = LoggerFactory.getLogger(L9720.class);

	String TXCD = "L9720";

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active " + TXCD);
		this.totaVo.init(titaVo);

		MySpring.newTask(TXCD + "p", this.txBuffer, titaVo);

		this.addList(this.totaVo);
		return this.sendList();
	}
}