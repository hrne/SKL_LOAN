package com.st1.itx.trade.LY;

import java.util.ArrayList;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.dataVO.TotaVo;
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.MySpring;

@Service("LY007")
@Scope("prototype")
/**
 * 
 * 
 * @author Ted Lin
 * @version 1.0.0
 */
public class LY007 extends TradeBuffer {

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active LY007 ");
		this.totaVo.init(titaVo);

		MySpring.newTask("LY007p", this.txBuffer, titaVo);

		this.addList(this.totaVo);
		return this.sendList();
	}
}

