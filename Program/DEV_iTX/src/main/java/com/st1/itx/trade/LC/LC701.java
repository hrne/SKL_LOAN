package com.st1.itx.trade.LC;

import java.util.ArrayList;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.dataVO.TotaVo;
import com.st1.itx.tradeService.TradeBuffer;

@Service("LC701")
@Scope("prototype")
/**
 * 月底日日終維護
 * 
 * @author ChihWei
 * @version 1.0.0
 */
public class LC701 extends TradeBuffer {

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active LC701 ");
		this.totaVo.init(titaVo);

		this.info("LC701 執行月底日日終維護");

//		titaVo.setBatchJobId("eomFlow");
		
		titaVo.setBatchJobId("jcicFlow");

		this.info("LC701 exit.");

		this.addList(this.totaVo);
		return this.sendList();
	}

}