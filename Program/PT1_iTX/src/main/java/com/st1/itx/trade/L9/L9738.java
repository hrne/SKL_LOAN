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
 * L9738 評估淨值明細
 * 
 * @author ST1-ChihWei
 * @version 1.0.0
 */
@Service("L9738")
@Scope("prototype")
public class L9738 extends TradeBuffer {

	String txcd = "L9738";

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active " + txcd);
		this.totaVo.init(titaVo);

		MySpring.newTask(txcd + "p", this.txBuffer, titaVo);

		this.addList(this.totaVo);
		return this.sendList();
	}
}