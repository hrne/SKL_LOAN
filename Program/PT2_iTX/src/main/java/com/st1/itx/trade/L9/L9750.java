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
 * L9750
 * 
 * @author ST1
 * @version 1.0.0
 */
@Service("L9750")
@Scope("prototype")
public class L9750 extends TradeBuffer {
	//會計師查核
	@Autowired
	L9750Report l9750Report;

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active L9750 ");
		this.totaVo.init(titaVo);

		l9750Report.exec(titaVo);

		this.addList(this.totaVo);
		return this.sendList();
	}
}