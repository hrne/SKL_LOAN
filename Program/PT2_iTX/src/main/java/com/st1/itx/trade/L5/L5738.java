package com.st1.itx.trade.L5;

import java.util.ArrayList;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.dataVO.TotaVo;
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.MySpring;

/**
 * L5738
 * 
 * @author 
 * @version 1.0.0
 */
@Service("L5738")
@Scope("prototype")
public class L5738 extends TradeBuffer {

	String txcd = "L5738";

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active " + txcd);
		this.totaVo.init(titaVo);

		MySpring.newTask(txcd + "p", this.txBuffer, titaVo);
	
		this.addList(this.totaVo);
		return this.sendList();
	}
}