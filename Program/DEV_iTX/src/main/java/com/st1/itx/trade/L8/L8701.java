package com.st1.itx.trade.L8;

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
 */

@Service("L8701")
@Scope("prototype")
/**
 * 產製公務人員報送資料<BR>
 * 
 * 
 * @author Lai
 * @version 1.0.0
 */
public class L8701 extends TradeBuffer {
	// private static final Logger logger = LoggerFactory.getLogger(L8701.class);

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active L8701 ");
		this.totaVo.init(titaVo);

		// 執行背景交易
		MySpring.newTask("L8701Batch", this.txBuffer, titaVo);

		this.addList(this.totaVo);
		return this.sendList();
	}
}