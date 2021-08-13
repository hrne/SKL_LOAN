package com.st1.itx.trade.L9;

import java.util.ArrayList;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.dataVO.TotaVo;
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.MySpring;

@Service("L9715")
@Scope("prototype")
/**
 * 
 * 
 * @author Eric Chang
 * @version 1.0.0
 */
public class L9715 extends TradeBuffer {
	@SuppressWarnings("unused")
	// private static final Logger logger = LoggerFactory.getLogger(L9715.class);

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active L9715 ");
		this.totaVo.init(titaVo);

		MySpring.newTask("L9715p", this.txBuffer, titaVo);

		this.addList(this.totaVo);
		return this.sendList();
	}
}