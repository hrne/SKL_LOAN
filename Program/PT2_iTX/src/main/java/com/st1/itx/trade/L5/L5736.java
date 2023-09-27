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
 * L5736 正常戶餘額明細
 * 
 * @author ST1-ChihWei
 * @version 1.0.0
 */
@Service("L5736")
@Scope("prototype")
public class L5736 extends TradeBuffer {

	String txcd = "L5736";

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active " + txcd);
		this.totaVo.init(titaVo);

		int totalItem = Integer.parseInt(titaVo.getParam("TotalItem"));

		int cnt = 0;
		for (int i = 1; i <= totalItem; i++) {
			if (titaVo.getParam("BtnShell" + i).equals("V")) {
				cnt++;
			}
		}

		if (cnt == 0) {
			throw new LogicException(titaVo, "E0019", "請勾選報表項目");
		} else {
			MySpring.newTask(txcd + "p", this.txBuffer, titaVo);
		}

		this.addList(this.totaVo);
		return this.sendList();
	}
}