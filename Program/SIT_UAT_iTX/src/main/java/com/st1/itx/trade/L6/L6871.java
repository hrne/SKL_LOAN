package com.st1.itx.trade.L6;

import java.util.ArrayList;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.dataVO.TotaVo;
import com.st1.itx.tradeService.TradeBuffer;

@Service("L6871")
@Scope("prototype")
/**
 * 月底日日終維護
 * 
 * @author ChihWei
 * @version 1.0.0
 */
public class L6871 extends TradeBuffer {
	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active L6871 ");
		this.totaVo.init(titaVo);

		this.info("L6871 執行月底日日終維護");

		titaVo.setBatchJobId("eomFlow");
		
		titaVo.setBatchJobId("jcicFlow");

		this.info("L6871 exit.");

		this.addList(this.totaVo);
		return this.sendList();
	}
}