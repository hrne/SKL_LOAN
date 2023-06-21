package com.st1.itx.trade.L4;

import java.util.ArrayList;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.dataVO.TotaVo;
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.MySpring;

@Service("L4962")
@Scope("prototype")
/**
 * 
 * 
 * @author Zi-Jun,Huang
 * @version 1.0.0
 */
public class L4962 extends TradeBuffer {

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active L4962 ");
		this.totaVo.init(titaVo);

		String flagA = titaVo.getParam("FlagA");
		String flagB = titaVo.getParam("FlagB");
		String CommericalFlag = titaVo.getParam("CommericalFlag");
		String flagD = titaVo.getParam("FlagD");

		if ("N".equals(flagA) && "N".equals(flagB) && "N".equals(CommericalFlag) && "N".equals(flagD)) {
			throw new LogicException("E0019", ("檢核表要擇一選擇為Y"));
		}
		// 執行交易
		MySpring.newTask("L4962Batch", this.txBuffer, titaVo);
		this.totaVo.setWarnMsg("背景作業中,待處理完畢訊息通知");
		this.addList(totaVo);
		return this.sendList();
	}
}