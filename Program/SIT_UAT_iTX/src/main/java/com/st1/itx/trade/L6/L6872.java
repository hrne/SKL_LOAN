package com.st1.itx.trade.L6;

import java.util.ArrayList;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.dataVO.TotaVo;
import com.st1.itx.tradeService.TradeBuffer;

@Service("L6872")
@Scope("prototype")
/**
 * 年底日日終維護
 * 
 * @author ChihWei
 * @version 1.0.0
 */
public class L6872 extends TradeBuffer {
	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active L6872 ");
		this.totaVo.init(titaVo);

		this.info("L6872 執行年底日日終維護");

		titaVo.setBatchJobId("eoyFlow");

		this.info("L6872 exit.");
		
		this.addList(this.totaVo);
		return this.sendList();
	}
}