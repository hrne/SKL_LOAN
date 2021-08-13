package com.st1.itx.trade.L4;

import java.util.ArrayList;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.dataVO.TotaVo;
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.MySpring;

@Service("L4320")
@Scope("prototype")
/**
 * 
 * 
 * @author Zi-Jun,Huang
 * @version 1.0.0
 */
public class L4320 extends TradeBuffer {
	// private static final Logger logger = LoggerFactory.getLogger(L4320.class);

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active L4320 ");
		this.totaVo.init(titaVo);

		// 執行交易
		MySpring.newTask("BS430", this.txBuffer, titaVo);
		
		this.addList(this.totaVo);
		return this.sendList();
	}
}