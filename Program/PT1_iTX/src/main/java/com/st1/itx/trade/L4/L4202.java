package com.st1.itx.trade.L4;

import java.util.ArrayList;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.dataVO.TotaVo;
import com.st1.itx.tradeService.TradeBuffer;

/**
 * Tita<br>
 */

@Service("L4202")
@Scope("prototype")
/**
 * 
 * 
 * @author Zi-Jun,Huang
 * @version 1.0.0
 */
public class L4202 extends TradeBuffer {
	// private static final Logger logger = LoggerFactory.getLogger(L4202.class);

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active L4202 ");
		this.totaVo.init(titaVo);

		this.addList(this.totaVo);
		return this.sendList();
	}
}