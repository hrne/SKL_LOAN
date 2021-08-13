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

@Service("LM040")
@Scope("prototype")
/**
 * 
 * 
 * @author ChihWei
 * @version 1.0.0
 */
public class LM040 extends TradeBuffer {
	private static final Logger logger = LoggerFactory.getLogger(LM040.class);

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active LM040 ");
		this.totaVo.init(titaVo);

		MySpring.newTask("LM040p", this.txBuffer, titaVo);

		this.addList(this.totaVo);
		return this.sendList();
	}
}