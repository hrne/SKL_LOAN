package com.st1.itx.trade.LD;

import java.util.ArrayList;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.dataVO.TotaVo;
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.MySpring;

/**
 * LD006
 * 
 * @author ChihWei
 * @version 1.0.0
 */
@Service("LD006")
@Scope("prototype")
public class LD006 extends TradeBuffer {

	String tranCode = "LD006";

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active " + tranCode);
		this.totaVo.init(titaVo);

		MySpring.newTask(tranCode + "p", this.txBuffer, titaVo);

		this.addList(this.totaVo);
		return this.sendList();
	}
}