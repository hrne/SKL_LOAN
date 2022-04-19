package com.st1.itx.trade.L8;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.dataVO.TotaVo;
import com.st1.itx.db.domain.JcicZ060;
import com.st1.itx.db.service.JcicZ060Service;
import com.st1.itx.tradeService.TradeBuffer;

@Service("L8R29")
@Scope("prototype")
/**
 * 
 * 
 * @author Luisito / Mata
 * @version 1.0.0
 */
public class L8R29 extends TradeBuffer {
	/* DB服務注入 */
	@Autowired
	public JcicZ060Service iJcicZ060Service;

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active L8r29 ");
		this.totaVo.init(titaVo);
		String iUkey = titaVo.getParam("RimUkey");
		JcicZ060 iJcicZ060 = new JcicZ060();
		iJcicZ060 = iJcicZ060Service.ukeyFirst(iUkey, titaVo);

		if (iJcicZ060 == null) {
			throw new LogicException(titaVo, "E0001", ""); // 無此代號錯誤
		} else {
			totaVo.putParam("L8r29TranKey", iJcicZ060.getTranKey());// 交易代碼
			totaVo.putParam("L8r29CustId", iJcicZ060.getCustId());// 債務人IDN
			totaVo.putParam("L8r29SubmitKey", iJcicZ060.getSubmitKey());// 報送單位代號
			totaVo.putParam("L8r29RcDate", iJcicZ060.getRcDate());// 原前置協商申請日
			totaVo.putParam("L8r29ChangePayDate", iJcicZ060.getChangePayDate());// 申請變更還款條件日
			totaVo.putParam("L8r29YM", iJcicZ060.getYM() - 191100);// 已清分足月期付金年月
			totaVo.putParam("L8r29OutJcicTxtDate", iJcicZ060.getOutJcicTxtDate());// 轉JCIC文字檔日期
		}
		this.addList(this.totaVo);
		return this.sendList();
	}
}