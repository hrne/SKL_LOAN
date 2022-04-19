package com.st1.itx.trade.L8;

import java.util.ArrayList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.dataVO.TotaVo;
import com.st1.itx.db.domain.JcicZ043;
import com.st1.itx.db.service.JcicZ043Service;
import com.st1.itx.tradeService.TradeBuffer;

@Service("L8R14")
@Scope("prototype")
/**
 * 
 * 
 * @author Mata
 * @version 1.0.0
 */
public class L8R14 extends TradeBuffer {
	/* DB服務注入 */
	@Autowired
	public JcicZ043Service iJcicZ043Service;

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active L8r14 ");
		this.totaVo.init(titaVo);
		String iUkey = titaVo.getParam("RimUkey");
		JcicZ043 iJcicZ043 = new JcicZ043();
		iJcicZ043 = iJcicZ043Service.ukeyFirst(iUkey, titaVo);

		if (iJcicZ043 == null) {
			throw new LogicException(titaVo, "E0001", ""); // 無此代號錯誤
		} else {
			totaVo.putParam("L8r14CustId", iJcicZ043.getCustId());
			totaVo.putParam("L8r14SubmitKey", iJcicZ043.getSubmitKey());
			totaVo.putParam("L8r14RcDate", iJcicZ043.getRcDate());
			totaVo.putParam("L8r14MaxMainCode", iJcicZ043.getMaxMainCode());
			totaVo.putParam("L8r14Account", iJcicZ043.getAccount());
			totaVo.putParam("L8r14CollateralType", iJcicZ043.getCollateralType());
			totaVo.putParam("L8r14OriginLoanAmt", iJcicZ043.getOriginLoanAmt());
			totaVo.putParam("L8r14CreditBalance", iJcicZ043.getCreditBalance());
			totaVo.putParam("L8r14PerPeriordAmt", iJcicZ043.getPerPeriordAmt());
			totaVo.putParam("L8r14LastPayAmt", iJcicZ043.getLastPayAmt());
			totaVo.putParam("L8r14LastPayDate", iJcicZ043.getLastPayDate());
			totaVo.putParam("L8r14OutstandAmt", iJcicZ043.getOutstandAmt());
			totaVo.putParam("L8r14RepayPerMonDay", iJcicZ043.getRepayPerMonDay());
			totaVo.putParam("L8r14ContractStartYM", iJcicZ043.getContractStartYM());
			totaVo.putParam("L8r14ContractEndYM", iJcicZ043.getContractEndYM());
			totaVo.putParam("L8r14TranKey", iJcicZ043.getTranKey());
			totaVo.putParam("L8r14OutJcicTxtDate", iJcicZ043.getOutJcicTxtDate());
		}
		this.addList(this.totaVo);
		return this.sendList();
	}
}