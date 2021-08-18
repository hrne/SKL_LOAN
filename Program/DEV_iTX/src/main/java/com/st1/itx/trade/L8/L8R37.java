package com.st1.itx.trade.L8;

import java.util.ArrayList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.dataVO.TotaVo;
import com.st1.itx.db.domain.JcicZ574;
import com.st1.itx.db.service.JcicZ574Service;
import com.st1.itx.tradeService.TradeBuffer;

@Service("L8R37")
@Scope("prototype")
/**
 * 
 * @author Luisito
 * @version 1.0.0
 */
public class L8R37 extends TradeBuffer {
	/* DB服務注入 */
	@Autowired
	public JcicZ574Service iJcicZ574Service;

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active L8r37 ");
		this.totaVo.init(titaVo);
		String iUkey = titaVo.getParam("RimUkey");
		JcicZ574 iJcicZ574 = new JcicZ574();
		iJcicZ574 = iJcicZ574Service.ukeyFirst(iUkey, titaVo);
		
		if (iJcicZ574 == null) {
			throw new LogicException(titaVo, "E0001", ""); // 無此代號錯誤
		}else {
			totaVo.putParam("L8r37TranKey", iJcicZ574.getTranKey());
			totaVo.putParam("L8r37CustId", iJcicZ574.getCustId());
			totaVo.putParam("L8r37SubmitKey", iJcicZ574.getSubmitKey());
			totaVo.putParam("L8r37ApplyDate", iJcicZ574.getApplyDate());
			totaVo.putParam("L8r37CloseDate", iJcicZ574.getCloseDate());
			totaVo.putParam("L8r37CloseMark", iJcicZ574.getCloseMark());
			totaVo.putParam("L8r37PhoneNo", iJcicZ574.getPhoneNo());
			totaVo.putParam("L8r37OutJcicTxtDate", iJcicZ574.getOutJcicTxtDate());		
		}

		
		this.addList(this.totaVo);
		return this.sendList();
	}
}