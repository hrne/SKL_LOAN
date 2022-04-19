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
 * L9705
 * 
 * @author ChihWei
 * @version 1.0.0
 */
@Service("L9705")
@Scope("prototype")
public class L9705 extends TradeBuffer {
	// private static final Logger logger = LoggerFactory.getLogger(L9705.class);

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active L9705 ");
		this.totaVo.init(titaVo);

		MySpring.newTask("L9705p", this.txBuffer, titaVo);

		this.addList(this.totaVo);
		return this.sendList();
	}
}