package com.st1.itx.trade.LM;

import java.util.ArrayList;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.dataVO.TotaVo;
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.MySpring;

/**
 * LM085 逾期月報表
 * 
 * @author kaichieh
 * @version 1.0.0
 */
@Service("LM085")
@Scope("prototype")
public class LM085 extends TradeBuffer {

	String txcd = "LM085";

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active " + txcd);
		this.totaVo.init(titaVo);

		MySpring.newTask(txcd + "p", this.txBuffer, titaVo);

		this.addList(this.totaVo);
		return this.sendList();
	}
}