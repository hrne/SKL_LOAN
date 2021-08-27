package com.st1.itx.trade.L8;

import java.util.ArrayList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.dataVO.TotaVo;
import com.st1.itx.db.domain.JcicZ045;
import com.st1.itx.db.service.JcicZ045Service;
import com.st1.itx.tradeService.TradeBuffer;

@Service("L8R16")
@Scope("prototype")
/**
 * 
 * 
 * @author Mata
 * @version 1.0.0
 */
public class L8R16 extends TradeBuffer {
	/* DB服務注入 */
	@Autowired
	public JcicZ045Service iJcicZ045Service;


	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active L8r16 ");
		this.totaVo.init(titaVo);
		String iUkey = titaVo.getParam("RimUkey");
		JcicZ045 iJcicZ045 = new JcicZ045();
		iJcicZ045 = iJcicZ045Service.ukeyFirst(iUkey, titaVo);
		
		if (iJcicZ045 == null) {
			throw new LogicException(titaVo, "E0001", ""); // 無此代號錯誤
		}else {
			totaVo.putParam("L8r16CustId", iJcicZ045.getCustId());
			totaVo.putParam("L8r16SubmitKey", iJcicZ045.getSubmitKey());
			totaVo.putParam("L8r16RcDate",iJcicZ045.getRcDate());
			totaVo.putParam("L8r16MaxMainCode", iJcicZ045.getMaxMainCode());
			totaVo.putParam("L8r16AgreeCode",iJcicZ045.getAgreeCode()); 
			totaVo.putParam("L8r16TranKey", iJcicZ045.getTranKey());	
			totaVo.putParam("L8r16OutJcicTxtDate", iJcicZ045.getOutJcicTxtDate());
		}	
		this.addList(this.totaVo);
		return this.sendList();
	}
}