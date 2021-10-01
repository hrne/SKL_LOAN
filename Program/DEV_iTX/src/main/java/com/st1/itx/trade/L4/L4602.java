package com.st1.itx.trade.L4;

import java.util.ArrayList;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.dataVO.TotaVo;
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.MySpring;

/**
 * L4602
 * 
 * @author Zi-Jun,Huang
 * @version 1.0.0
 */
@Service("L4602")
@Scope("prototype")
public class L4602 extends TradeBuffer {

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active L4602 ");
		this.totaVo.init(titaVo);

		MySpring.newTask("L4602p", this.txBuffer, titaVo);

		this.addList(this.totaVo);
		return this.sendList();
	}
}
