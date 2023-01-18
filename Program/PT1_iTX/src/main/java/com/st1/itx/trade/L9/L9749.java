package com.st1.itx.trade.L9;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.dataVO.TotaVo;
import com.st1.itx.tradeService.TradeBuffer;

/**
 * L9749
 * 
 * @author ST1-ChihWei
 * @version 1.0.0
 */
@Service("L9749")
@Scope("prototype")
public class L9749 extends TradeBuffer {

	@Autowired
	L9749Report L9749Report;

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active L9749 ");
		this.totaVo.init(titaVo);

		L9749Report.exec(titaVo);

		this.addList(this.totaVo);
		return this.sendList();
	}
}