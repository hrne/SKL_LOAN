package com.st1.itx.trade.L9;

import java.util.ArrayList;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.dataVO.TotaVo;
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.MySpring;

/**
 * L9734
 * 
 * @author 
 * @version 1.0.0
 */
@Service("L9734")
@Scope("prototype")
public class L9734 extends TradeBuffer {

	String txcd = "L9734";

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active " + txcd);
		this.totaVo.init(titaVo);

		int count = 0;
		
		int totalItem = Integer.parseInt(titaVo.getParam("TotalItem"));


		for (int i = 1; i <= totalItem; i++) {

			if (titaVo.getParam("BtnShell" + i).equals("V")) {
				count++;
			}
		}

		if(count == 0) {
			throw new LogicException(titaVo, "E0019", "請勾選報表項目");
		}
		
		MySpring.newTask(txcd + "p", this.txBuffer, titaVo);
	
		this.addList(this.totaVo);
		return this.sendList();
	}
}