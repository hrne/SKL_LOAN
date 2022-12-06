package com.st1.itx.trade.LM;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.dataVO.TotaVo;
import com.st1.itx.tradeService.TradeBuffer;

/**
 * LM086p
 * 
 * 
 * @author ChihWei
 * @version 1.0.0
 */
@Service("LM086p")
@Scope("prototype")
public class LM086p extends TradeBuffer {

	@Autowired
	LM086Report lM086Report;

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active LM086p");
		this.totaVo.init(titaVo);
		lM086Report.setParentTranCode(titaVo.getTxcd());
		lM086Report.exec(titaVo);
		this.addList(this.totaVo);
		return this.sendList();
	}
}