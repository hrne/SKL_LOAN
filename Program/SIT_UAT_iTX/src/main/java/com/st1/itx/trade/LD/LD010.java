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
 * LD010<BR>
 * 介紹人換算業績報酬檢核表
 * 
 * @author ChihWei
 * @version 1.0.0
 */
@Service("LD010")
@Scope("prototype")
public class LD010 extends TradeBuffer {

	String tranCode = "LD010";

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active " + tranCode);
		this.totaVo.init(titaVo);

		MySpring.newTask(tranCode + "p", this.txBuffer, titaVo);

		this.addList(this.totaVo);
		return this.sendList();
	}
}