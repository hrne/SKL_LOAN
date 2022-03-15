package com.st1.itx.trade.L9;

import java.util.ArrayList;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.dataVO.TotaVo;
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.MySpring;

@Service("L9708")
@Scope("prototype")
/**
 * 
 * 
 * @author Eric Chang
 * @version 1.0.0
 */
public class L9708 extends TradeBuffer {
	// private static final Logger logger = LoggerFactory.getLogger(L9708.class);

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active L9708 ");
		this.totaVo.init(titaVo);

		MySpring.newTask("L9708p", this.txBuffer, titaVo);

		this.addList(this.totaVo);
		return this.sendList();
	}

}