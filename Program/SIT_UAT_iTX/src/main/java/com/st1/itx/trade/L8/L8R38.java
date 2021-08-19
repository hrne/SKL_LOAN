package com.st1.itx.trade.L8;

import java.util.ArrayList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.dataVO.TotaVo;
import com.st1.itx.db.domain.JcicZ575;
import com.st1.itx.db.service.JcicZ575Service;
import com.st1.itx.tradeService.TradeBuffer;


@Service("L8R38")
@Scope("prototype")
/**
 * 
 * @author Fegie /Mata
 * @version 1.0.0
 */
public class L8R38 extends TradeBuffer {
	/* DB服務注入 */
	@Autowired
	public JcicZ575Service iJcicZ575Service;

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active L8r38 ");
		this.totaVo.init(titaVo);
		String iUkey = titaVo.getParam("RimUkey");
		JcicZ575 iJcicZ575 = new JcicZ575();
		iJcicZ575 = iJcicZ575Service.ukeyFirst(iUkey, titaVo);
		
		if (iJcicZ575 == null) {
			throw new LogicException(titaVo, "E0001", ""); // 無此代號錯誤
		}else {
			totaVo.putParam("L8r38TranKey", iJcicZ575.getTranKey());
			totaVo.putParam("L8r38CustId", iJcicZ575.getCustId());
			totaVo.putParam("L8r38SubmitKey", iJcicZ575.getSubmitKey());
			totaVo.putParam("L8r38ApplyDate", iJcicZ575.getApplyDate());
			totaVo.putParam("L8r38BankId", iJcicZ575.getBankId());
			totaVo.putParam("L8r38ModifyType", iJcicZ575.getModifyType());
			totaVo.putParam("L8r38OutJcicTxtDate", iJcicZ575.getOutJcicTxtDate());		
		}

		
		this.addList(this.totaVo);
		return this.sendList();
	}
}