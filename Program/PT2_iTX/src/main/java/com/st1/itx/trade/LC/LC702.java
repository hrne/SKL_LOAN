package com.st1.itx.trade.LC;

import java.util.ArrayList;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.dataVO.TotaVo;
import com.st1.itx.tradeService.TradeBuffer;

@Service("LC702")
@Scope("prototype")
/**
 * 年底日日終維護
 * 
 * @author ChihWei
 * @version 1.0.0
 */
public class LC702 extends TradeBuffer {
	// private static final Logger logger = LoggerFactory.getLogger(LC701.class);

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active LC702 ");
		this.totaVo.init(titaVo);

		this.info("LC702 執行年底日日終維護");

		titaVo.setBatchJobId("eoyFlow");

		this.info("LC702 exit.");

		this.addList(this.totaVo);
		return this.sendList();
	}

}