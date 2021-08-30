package com.st1.itx.trade.L8;

import java.util.ArrayList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.dataVO.TotaVo;
import com.st1.itx.db.domain.JcicZ041;
import com.st1.itx.db.service.JcicZ041Service;
import com.st1.itx.tradeService.TradeBuffer;

@Service("L8R12")
@Scope("prototype")
/**
 * 
 * 
 * @author Mata
 * @version 1.0.0
 */
public class L8R12 extends TradeBuffer {
	/* DB服務注入 */
	@Autowired
	public JcicZ041Service iJcicZ041Service;


	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active L8r12 ");
		this.totaVo.init(titaVo);
		String iUkey = titaVo.getParam("RimUkey");
		JcicZ041 iJcicZ041 = new JcicZ041();
		iJcicZ041 = iJcicZ041Service.ukeyFirst(iUkey, titaVo);
		
		if (iJcicZ041 == null) {
			throw new LogicException(titaVo, "E0001", ""); // 無此代號錯誤
		}else {
			totaVo.putParam("L8r12CustId", iJcicZ041.getCustId());
			totaVo.putParam("L8r12SubmitKey", iJcicZ041.getSubmitKey());
			totaVo.putParam("L8r12RcDate",iJcicZ041.getRcDate());
			totaVo.putParam("L8r12ScDate", iJcicZ041.getScDate());
			totaVo.putParam("L8r12NegoStartDate", iJcicZ041.getNegoStartDate());
			totaVo.putParam("L8r12NonFinClaimAmt", iJcicZ041.getNonFinClaimAmt());
			totaVo.putParam("L8r12TranKey", iJcicZ041.getTranKey());	
			totaVo.putParam("L8r12OutJcicTxtDate", iJcicZ041.getOutJcicTxtDate());
		}	
		this.addList(this.totaVo);
		return this.sendList();
	}
}