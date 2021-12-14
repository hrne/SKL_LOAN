package com.st1.itx.trade.L8;

import java.util.ArrayList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.dataVO.TotaVo;
import com.st1.itx.db.domain.JcicZ050;
import com.st1.itx.db.service.JcicZ050Service;
import com.st1.itx.tradeService.TradeBuffer;

@Service("L8R22")
@Scope("prototype")
/**
 * 
 * 
 * @author Mata
 * @version 1.0.0
 */
public class L8R22 extends TradeBuffer {
	/* DB服務注入 */
	@Autowired
	public JcicZ050Service iJcicZ050Service;

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active L8r22 ");
		this.totaVo.init(titaVo);
		String iUkey = titaVo.getParam("RimUkey");
		JcicZ050 iJcicZ050 = new JcicZ050();
		iJcicZ050 = iJcicZ050Service.ukeyFirst(iUkey, titaVo);

		if (iJcicZ050 == null) {
			throw new LogicException(titaVo, "E0001", ""); // 無此代號錯誤
		} else {
			totaVo.putParam("L8r22CustId", iJcicZ050.getCustId());
			totaVo.putParam("L8r22SubmitKey", iJcicZ050.getSubmitKey());
			totaVo.putParam("L8r22RcDate", iJcicZ050.getRcDate());
			totaVo.putParam("L8r22PayDate", iJcicZ050.getPayDate());
			totaVo.putParam("L8r22Status", iJcicZ050.getStatus());
			totaVo.putParam("L8r22PayAmt", iJcicZ050.getPayAmt());
			totaVo.putParam("L8r22SumRepayActualAmt", iJcicZ050.getSumRepayActualAmt());
			totaVo.putParam("L8r22SumRepayShouldAmt", iJcicZ050.getSumRepayShouldAmt());
			totaVo.putParam("L8r22SecondRepayYM", iJcicZ050.getSecondRepayYM()-191100);
			totaVo.putParam("L8r22TranKey", iJcicZ050.getTranKey());
			totaVo.putParam("L8r22OutJcicTxtDate", iJcicZ050.getOutJcicTxtDate());
		}
		this.addList(this.totaVo);
		return this.sendList();
	}
}