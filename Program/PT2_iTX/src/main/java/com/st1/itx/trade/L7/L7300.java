package com.st1.itx.trade.L7;

import java.util.ArrayList;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.dataVO.TotaVo;
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.MySpring;

/**
 * ICS資產資料傳輸
 *
 * @author Wei
 * @version 1.0.0
 */
@Service("L7300")
@Scope("prototype")
public class L7300 extends TradeBuffer {

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active L7300");
		this.totaVo.init(titaVo);

		MySpring.newTask("L7300p", this.txBuffer, titaVo);

		this.addList(this.totaVo);
		return this.sendList();
	}
}