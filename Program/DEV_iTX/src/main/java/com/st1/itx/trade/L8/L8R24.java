package com.st1.itx.trade.L8;

import java.util.ArrayList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.dataVO.TotaVo;
import com.st1.itx.db.domain.JcicZ052;
import com.st1.itx.db.service.JcicZ052Service;
import com.st1.itx.tradeService.TradeBuffer;

@Service("L8R24")
@Scope("prototype")
/**
 * 
 * 
 * @author Mata
 * @version 1.0.0
 */
public class L8R24 extends TradeBuffer {
	/* DB服務注入 */
	@Autowired
	public JcicZ052Service iJcicZ052Service;


	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active L8r24 ");
		this.totaVo.init(titaVo);
		String iUkey = titaVo.getParam("RimUkey");
		JcicZ052 iJcicZ052 = new JcicZ052();
		iJcicZ052 = iJcicZ052Service.ukeyFirst(iUkey, titaVo);
		
		if (iJcicZ052 == null) {
			throw new LogicException(titaVo, "E0001", ""); // 無此代號錯誤
		}else {
			totaVo.putParam("L8r24CustId", iJcicZ052.getCustId());
			totaVo.putParam("L8r24SubmitKey", iJcicZ052.getSubmitKey());
			totaVo.putParam("L8r24RcDate",iJcicZ052.getRcDate());
			totaVo.putParam("L8r24BankCode1",iJcicZ052.getBankCode1());
			totaVo.putParam("L8r24DataCode1",iJcicZ052.getDataCode1());
			totaVo.putParam("L8r24BankCode2",iJcicZ052.getBankCode2());
			totaVo.putParam("L8r24DataCode2",iJcicZ052.getDataCode2());
			totaVo.putParam("L8r24BankCode3",iJcicZ052.getBankCode3());
			totaVo.putParam("L8r24DataCode3",iJcicZ052.getDataCode3());
			totaVo.putParam("L8r24BankCode4",iJcicZ052.getBankCode4());
			totaVo.putParam("L8r24DataCode4",iJcicZ052.getDataCode4());
			totaVo.putParam("L8r24BankCode5",iJcicZ052.getBankCode5());
			totaVo.putParam("L8r24DataCode5",iJcicZ052.getDataCode5());
			totaVo.putParam("L8r24ChangePayDate",iJcicZ052.getChangePayDate());
			totaVo.putParam("L8r24TranKey", iJcicZ052.getTranKey());	
			totaVo.putParam("L8r24OutJcicTxtDate", iJcicZ052.getOutJcicTxtDate());
		}
		this.addList(this.totaVo);
		return this.sendList();
	}
}