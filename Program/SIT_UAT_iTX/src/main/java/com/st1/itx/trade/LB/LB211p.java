package com.st1.itx.trade.LB;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.dataVO.TotaVo;
import com.st1.itx.tradeService.TradeBuffer;


/**
 * 
 * 
 * @author Eric Chang
 * @version 1.0.0
 */

@Service("LB211p")
@Scope("prototype")

public class LB211p extends TradeBuffer {

	@Autowired
	public LB211Report lb211Report;

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active LB211p ");
		this.totaVo.init(titaVo);

		lb211Report.exec(titaVo);

		this.addList(this.totaVo);
		return this.sendList();
	}

}