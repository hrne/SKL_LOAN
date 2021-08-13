package com.st1.itx.trade.LM;

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

@Service("LM003")
@Scope("prototype")
/**
 * 
 * 
 * @author ChihWei
 * @version 1.0.0
 */
public class LM003 extends TradeBuffer {
	private static final Logger logger = LoggerFactory.getLogger(LM003.class);

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active LM003 ");
		this.totaVo.init(titaVo);

		MySpring.newTask("LM003p", this.txBuffer, titaVo);

		this.addList(this.totaVo);
		return this.sendList();
	}
}