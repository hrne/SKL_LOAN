package com.st1.itx.trade.L8;

import java.util.ArrayList;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.dataVO.TotaVo;
import com.st1.itx.db.domain.JcicZ571;
import com.st1.itx.db.service.JcicZ571Service;
import com.st1.itx.tradeService.TradeBuffer;


@Service("L8R34")
@Scope("prototype")
/**
 * 
 * @author Fegie  / Mata
 * @version 1.0.0
 */
public class L8R34 extends TradeBuffer {
	private static final Logger logger = LoggerFactory.getLogger(L8R34.class);
	/* DB服務注入 */
	@Autowired
	public JcicZ571Service iJcicZ571Service;

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active L8r34 ");
		this.totaVo.init(titaVo);
		logger.info("L8r34rimstart");
		String iUkey = titaVo.getParam("RimUkey");
        JcicZ571 iJcicZ571 = new JcicZ571();
		iJcicZ571 = iJcicZ571Service.ukeyFirst(iUkey, titaVo);

		if (iJcicZ571 == null) {
			throw new LogicException(titaVo, "E0001", ""); // 無此代號錯誤
		}else {
			logger.info("L8R34update");
			totaVo.putParam("L8r34TranKey", iJcicZ571.getTranKey());// 交易代碼
			totaVo.putParam("L8r34CustId", iJcicZ571.getCustId());// 債務人IDN
			totaVo.putParam("L8r34SubmitKey", iJcicZ571.getSubmitKey());// 報送單位代號
			totaVo.putParam("L8r34ApplyDate", iJcicZ571.getApplyDate());// 更生款項統一收付申請日
			totaVo.putParam("L8r34BankId", iJcicZ571.getBankId());// 受理款項統一收付之債權金融機構代號
			totaVo.putParam("L8r34OwnerYn", iJcicZ571.getOwnerYn());// 是否為更生債權人
			totaVo.putParam("L8r34PayYn", iJcicZ571.getPayYn());//債務人是否仍依更生方案正常還款予本金融機構
			totaVo.putParam("L8r34OwnerAmt", iJcicZ571.getOwnerAmt());// 本金融機構更生債權總金額
			totaVo.putParam("L8r34AllotAmt", iJcicZ571.getAllotAmt());// 參與分配債權金額
			totaVo.putParam("L8r34UnallotAmt", iJcicZ571.getUnallotAmt());//未參與分配債權金額
			totaVo.putParam("L8r34OutJcicTxtDate", iJcicZ571.getOutJcicTxtDate());// 轉JCIC文字檔日期
		}
		this.addList(this.totaVo);
		return this.sendList();
	}
}