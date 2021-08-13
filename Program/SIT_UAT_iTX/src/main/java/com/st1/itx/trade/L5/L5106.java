package com.st1.itx.trade.L5;

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

@Service("L5106")
@Scope("prototype")
/**
 *
 *
 * @author Fegie
 * @version 1.0.0
 */
public class L5106 extends TradeBuffer {
	private static final Logger logger = LoggerFactory.getLogger(L5106.class);

	/* DB服務注入 */

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active L5106 ");
		this.totaVo.init(titaVo);

		// 執行交易 
		MySpring.newTask("L5106Batch", this.txBuffer, titaVo);
				
		this.addList(this.totaVo);
		return this.sendList();
	}
}
