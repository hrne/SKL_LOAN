package com.st1.itx.trade.L5;

import java.util.ArrayList;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.dataVO.TotaVo;
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.MySpring;

/**
 * L5811
 * 
 * @author ChihCheng
 * @version 1.0.0
 */
@Service("L5811")
@Scope("prototype")
public class L5811 extends TradeBuffer {

	
	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active L5811 ");
		this.totaVo.init(titaVo);

		// 執行交易
		MySpring.newTask("L5811Batch", this.txBuffer, titaVo);

		this.totaVo.setWarnMsg("背景作業中,待處理完畢訊息通知");
		
		this.addList(this.totaVo);
		return this.sendList();
	}
	
	
}