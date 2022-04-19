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

@Service("L8110")
@Scope("prototype")
/**
 * 產製AML每日有效客戶名單<BR>
 * 本交易設定為排程
 * 
 * @author Lai
 * @version 1.0.0
 */
public class L8110 extends TradeBuffer {
	// private static final Logger logger = LoggerFactory.getLogger(L8110.class);

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active L8110 ");
		this.totaVo.init(titaVo);

		// 執行背景交易
		MySpring.newTask("L8110Batch", this.txBuffer, titaVo);

		this.addList(this.totaVo);
		return this.sendList();
	}
}