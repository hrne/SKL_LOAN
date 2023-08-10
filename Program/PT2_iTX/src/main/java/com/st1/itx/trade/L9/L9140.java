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
 * L9140
 * 
 * @author ST1
 * @version 1.0.0
 */
@Service("L9140")
@Scope("prototype")
public class L9140 extends TradeBuffer {

	@Autowired
	L9140Report L9140Report;

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active L9140 ");
		this.totaVo.init(titaVo);

		L9140Report.exec(titaVo);

		this.addList(this.totaVo);

		this.info("active L9140 End");
		return this.sendList();
	}
}