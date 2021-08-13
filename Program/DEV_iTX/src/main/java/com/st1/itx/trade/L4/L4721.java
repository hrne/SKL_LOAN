package com.st1.itx.trade.L4;

import java.util.ArrayList;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.dataVO.TotaVo;
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.MySpring;

@Service("L4721")
@Scope("prototype")
/**
 * 
 * 
 * @author Zijin
 * @version 1.0.0
 */
public class L4721 extends TradeBuffer {
	// private static final Logger logger = LoggerFactory.getLogger(L4721.class);

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active L4721 ");
		this.totaVo.init(titaVo);

//		還本繳息對帳單.pdf
		// 執行交易
		MySpring.newTask("BS432", this.txBuffer, titaVo);

		this.addList(this.totaVo);
		return this.sendList();
	}
}