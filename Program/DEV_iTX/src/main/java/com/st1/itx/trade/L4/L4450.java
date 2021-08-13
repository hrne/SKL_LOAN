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
 * Tita<br>
 * EntryDate=9,7<br>
 * PostSpecificDd=9,2<br>
 * PostSecondSpecificDd=9,2<br>
 * AchSpecificDdFrom=9,2<br>
 * AchSpecificDdTo=9,2<br>
 */

@Service("L4450")
@Scope("prototype")
/**
 * 
 * 
 * @author Zi-Jun,Huang
 * @version 1.0.0
 */
public class L4450 extends TradeBuffer {
	// private static final Logger logger = LoggerFactory.getLogger(L4450.class);

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active L4450 ");
		this.totaVo.init(titaVo);

		// 執行交易
		MySpring.newTask("BS440", this.txBuffer, titaVo);

		this.addList(this.totaVo);
		return this.sendList();
	}
}