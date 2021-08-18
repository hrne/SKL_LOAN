package com.st1.itx.trade.L8;

import java.util.ArrayList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.dataVO.TotaVo;
import com.st1.itx.db.domain.JcicZ061;
import com.st1.itx.db.service.JcicZ061Service;
import com.st1.itx.tradeService.TradeBuffer;

@Service("L8R30")
@Scope("prototype")
/**
 * 
 * @author Luisito / Mata
 * @version 1.0.0
 */
public class L8R30 extends TradeBuffer {
	/* DB服務注入 */
	@Autowired
	public JcicZ061Service iJcicZ061Service;

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active L8r30 ");
		this.totaVo.init(titaVo);
		String iUkey = titaVo.getParam("RimUkey");
		JcicZ061 iJcicZ061 = new JcicZ061();
		iJcicZ061 = iJcicZ061Service.ukeyFirst(iUkey, titaVo);

		if (iJcicZ061 == null) {
			throw new LogicException(titaVo, "E0001", ""); // 無此代號錯誤
		} else {
			totaVo.putParam("L8r30TranKey",iJcicZ061.getTranKey());// 交易代碼
			totaVo.putParam("L8r30CustId", iJcicZ061.getCustId());// 債務人IDN
			totaVo.putParam("L8r30SubmitKey", iJcicZ061.getSubmitKey());// 報送單位代號
			totaVo.putParam("L8r30RcDate", iJcicZ061.getRcDate());// 原前置協商申請日
			totaVo.putParam("L8r30ChangePayDate", iJcicZ061.getChangePayDate());// 申請變更還款條件日
			totaVo.putParam("L8r30MaxMainCode",iJcicZ061.getMaxMainCode());//報送單位代號
			totaVo.putParam("L8r30ExpBalanceAmt", iJcicZ061.getExpBalanceAmt());// 信用貸款協商剩餘債權餘額
			totaVo.putParam("L8r30CashBalanceAmt", iJcicZ061.getCashBalanceAmt());// 現金卡協商剩餘債權餘額
			totaVo.putParam("L8r30CreditBalanceAmt", iJcicZ061.getCreditBalanceAmt());// 信用卡協商剩餘債權餘額
			totaVo.putParam("L8r30MaxMainNote", iJcicZ061.getMaxMainNote());// 最大債權金融機構報送註記
			totaVo.putParam("L8r30IsGuarantor", iJcicZ061.getIsGuarantor());// 是否有保證人
			totaVo.putParam("L8r30IsChangePayment", iJcicZ061.getIsChangePayment());// 是否同意債務人申請變更還款條件方案
			totaVo.putParam("L8r30OutJcicTxtDate", iJcicZ061.getOutJcicTxtDate());// 轉JCIC文字檔日期
		}
		this.addList(this.totaVo);
		return this.sendList();
	}
}