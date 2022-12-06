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
 * LM086
 * 
 * @author ChihWei
 * @version 1.0.0
 */
@Service("LM086")
@Scope("prototype")
public class LM086 extends TradeBuffer {

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active LM086");
		this.totaVo.init(titaVo);
		MySpring.newTask("LM086p", this.txBuffer, titaVo);
		this.addList(this.totaVo);
		return this.sendList();
	}
}