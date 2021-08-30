package com.st1.itx.trade.L8;

import java.util.ArrayList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.dataVO.TotaVo;
import com.st1.itx.db.domain.JcicZ040;
import com.st1.itx.db.service.JcicZ040Service;
import com.st1.itx.tradeService.TradeBuffer;

@Service("L8R11")
@Scope("prototype")
/**
 * 
 * 
 * @author Mata
 * @version 1.0.0
 */
public class L8R11 extends TradeBuffer {
	/* DB服務注入 */
	@Autowired
	public JcicZ040Service iJcicZ040Service;


	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active L8r11 ");
		this.totaVo.init(titaVo);
		String iUkey = titaVo.getParam("RimUkey");
		JcicZ040 iJcicZ040 = new JcicZ040();
		iJcicZ040 = iJcicZ040Service.ukeyFirst(iUkey, titaVo);
		
		if (iJcicZ040 == null) {
			throw new LogicException(titaVo, "E0001", ""); // 無此代號錯誤
		}else {
			totaVo.putParam("L8r11CustId", iJcicZ040.getCustId());
			totaVo.putParam("L8r11SubmitKey", iJcicZ040.getSubmitKey());
			totaVo.putParam("L8r11RcDate",iJcicZ040.getRcDate());
			totaVo.putParam("L8r11RbDate", iJcicZ040.getRbDate());
			totaVo.putParam("L8r11ApplyType", iJcicZ040.getApplyType());
			totaVo.putParam("L8r11RefBankId", iJcicZ040.getRefBankId());
			totaVo.putParam("L8r11NotBankId1", iJcicZ040.getNotBankId1());
			totaVo.putParam("L8r11NotBankId2", iJcicZ040.getNotBankId2());
			totaVo.putParam("L8r11NotBankId3", iJcicZ040.getNotBankId3());
			totaVo.putParam("L8r11NotBankId4", iJcicZ040.getNotBankId4());
			totaVo.putParam("L8r11NotBankId5", iJcicZ040.getNotBankId5());
			totaVo.putParam("L8r11NotBankId6", iJcicZ040.getNotBankId6());
			totaVo.putParam("L8r11TranKey", iJcicZ040.getTranKey());	
			totaVo.putParam("L8r11OutJcicTxtDate", iJcicZ040.getOutJcicTxtDate());
		}	
		this.addList(this.totaVo);
		return this.sendList();
	}
}