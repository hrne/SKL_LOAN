package com.st1.itx.trade.L4;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.dataVO.TotaVo;
import com.st1.itx.db.service.springjpa.cm.L4211AServiceImpl;
import com.st1.itx.db.service.springjpa.cm.L4211BServiceImpl;
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.MySpring;

@Service("L4211")
@Scope("prototype")
/**
 * 
 * 
 * @author Zi-Jun,Huang
 * @version 1.0.0
 */

public class L4211 extends TradeBuffer {

	@Autowired
	public L4211AServiceImpl l4211ARServiceImpl;

	@Autowired
	public L4211BServiceImpl l4211BRServiceImpl;

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active L4211 ");
		this.totaVo.init(titaVo);

		MySpring.newTask("L4211Batch", this.txBuffer, titaVo);

		this.addList(this.totaVo);
		return this.sendList();
	}
}