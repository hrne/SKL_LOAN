package com.st1.itx.trade.L9;

import java.util.ArrayList;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.dataVO.TotaVo;
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.MySpring;

/**
 * L9741
 * 
 * @author Linda
 * @version 1.0.0
 */
@Service("L9741")
@Scope("prototype")
public class L9741 extends TradeBuffer {

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active L9741 ");
		this.totaVo.init(titaVo);

		MySpring.newTask("L9741p", this.txBuffer, titaVo);

		this.addList(this.totaVo);
		return this.sendList();
	}
}