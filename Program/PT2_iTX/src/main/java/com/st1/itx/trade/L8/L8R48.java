package com.st1.itx.trade.L8;

import java.util.ArrayList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.dataVO.TotaVo;
import com.st1.itx.db.domain.JcicZ454;
import com.st1.itx.db.service.JcicZ454Service;
import com.st1.itx.tradeService.TradeBuffer;

@Service("L8R48")
@Scope("prototype")
/**
 * 
 * @author Luisito
 * @version 1.0.0
 */
public class L8R48 extends TradeBuffer {
	/* DB服務注入 */
	@Autowired
	public JcicZ454Service iJcicZ454Service;

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active L8r48 ");
		this.totaVo.init(titaVo);
		String iUkey = titaVo.getParam("RimUkey");
		JcicZ454 iJcicZ454 = new JcicZ454();
		iJcicZ454 = iJcicZ454Service.ukeyFirst(iUkey, titaVo);

		if (iJcicZ454 == null) {
			throw new LogicException(titaVo, "E0001", ""); // 無此代號錯誤
		} else {
			totaVo.putParam("L8r48TranKey", iJcicZ454.getTranKey());
			totaVo.putParam("L8r48CustId", iJcicZ454.getCustId());
			totaVo.putParam("L8r48SubmitKey", iJcicZ454.getSubmitKey());
			totaVo.putParam("L8r48ApplyDate", iJcicZ454.getApplyDate());
			totaVo.putParam("L8r48CourtCode", iJcicZ454.getCourtCode());// 異動債權金機構代號
			totaVo.putParam("L8r48MaxMainCode", iJcicZ454.getMaxMainCode());// 最大債權金融機構代號
			totaVo.putParam("L8r48PayOffResult", iJcicZ454.getPayOffResult());// 單獨全數受清償原因
			totaVo.putParam("L8r48PayOffDate", iJcicZ454.getPayOffDate());// 單獨全數受清償日期
			totaVo.putParam("L8r48OutJcicTxtDate", iJcicZ454.getOutJcicTxtDate());
		}

		this.addList(this.totaVo);
		return this.sendList();
	}
}