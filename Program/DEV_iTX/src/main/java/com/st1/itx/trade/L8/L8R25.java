package com.st1.itx.trade.L8;

import java.util.ArrayList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.dataVO.TotaVo;
import com.st1.itx.db.domain.JcicZ053;
import com.st1.itx.db.service.JcicZ053Service;
import com.st1.itx.tradeService.TradeBuffer;

@Service("L8R25")
@Scope("prototype")
/**
 * 
 * 
 * @author Mata
 * @version 1.0.0
 */
public class L8R25 extends TradeBuffer {
	/* DB服務注入 */
	@Autowired
	public JcicZ053Service iJcicZ053Service;

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active L8r25 ");
		this.totaVo.init(titaVo);
		String iUkey = titaVo.getParam("RimUkey");
		JcicZ053 iJcicZ053 = new JcicZ053();
		iJcicZ053 = iJcicZ053Service.ukeyFirst(iUkey, titaVo);

		if (iJcicZ053 == null) {
			throw new LogicException(titaVo, "E0001", ""); // 無此代號錯誤
		} else {
			totaVo.putParam("L8r25CustId", iJcicZ053.getCustId());
			totaVo.putParam("L8r25SubmitKey", iJcicZ053.getSubmitKey());
			totaVo.putParam("L8r25RcDate", iJcicZ053.getRcDate());
			totaVo.putParam("L8r25MaxMainCode", iJcicZ053.getMaxMainCode());
			totaVo.putParam("L8r25AgreeSend", iJcicZ053.getAgreeSend());
			totaVo.putParam("L8r25AgreeSendData1", iJcicZ053.getAgreeSendData1());
			totaVo.putParam("L8r25AgreeSendData2", iJcicZ053.getAgreeSendData2());
			totaVo.putParam("L8r25ChangePayDate", iJcicZ053.getChangePayDate());
			totaVo.putParam("L8r25TranKey", iJcicZ053.getTranKey());
			totaVo.putParam("L8r25OutJcicTxtDate", iJcicZ053.getOutJcicTxtDate());
		}

		this.addList(this.totaVo);
		return this.sendList();
	}
}