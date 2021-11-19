package com.st1.itx.trade.L8;

import java.util.ArrayList;


import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.st1.itx.Exception.LogicException;

import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.dataVO.TotaVo;
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.MySpring;


/**
 * Tita RecordDateStart=9,7 RecordDateEnd=9,7 TxCodeX=X,5
 * END=X,1
 */

@Service("L8205") // 疑似洗錢報表
@Scope("prototype")
/**
 *
 *
 * @author ChihCheng
 * @version 1.0.0
 */

public class L8205 extends TradeBuffer {

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active L8205 ");
		this.totaVo.init(titaVo);
		
		
		MySpring.newTask("L8205p", this.txBuffer, titaVo);
		
		this.addList(this.totaVo);
		return this.sendList();
	}

}
