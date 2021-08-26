package com.st1.itx.trade.L8;

import java.util.ArrayList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.dataVO.TotaVo;
import com.st1.itx.db.domain.JcicZ054;
import com.st1.itx.db.service.JcicZ054Service;
import com.st1.itx.tradeService.TradeBuffer;

@Service("L8R26")
@Scope("prototype")
/**
 * 
 * 
 * @author Mata
 * @version 1.0.0
 */
public class L8R26 extends TradeBuffer {
	/* DB服務注入 */
	@Autowired
	public JcicZ054Service iJcicZ054Service;


	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active L8r26 ");
		this.totaVo.init(titaVo);
		String iUkey = titaVo.getParam("RimUkey");
		JcicZ054 iJcicZ054 = new JcicZ054();
		iJcicZ054 = iJcicZ054Service.ukeyFirst(iUkey, titaVo);
		
		if (iJcicZ054 == null) {
			throw new LogicException(titaVo, "E0001", ""); // 無此代號錯誤
		}else {
			totaVo.putParam("L8r26CustId", iJcicZ054.getCustId());
			totaVo.putParam("L8r26SubmitKey", iJcicZ054.getSubmitKey());
			totaVo.putParam("L8r26RcDate",iJcicZ054.getRcDate());
			totaVo.putParam("L8r26MaxMainCode",iJcicZ054.getMaxMainCode());
			totaVo.putParam("L8r26PayOffDate", iJcicZ054.getPayOffDate());
			totaVo.putParam("L8r26PayOffResult",iJcicZ054.getPayOffResult());
			totaVo.putParam("L8r26TranKey", iJcicZ054.getTranKey());	
			totaVo.putParam("L8r26OutJcicTxtDate", iJcicZ054.getOutJcicTxtDate());
		}

		
		this.addList(this.totaVo);
		return this.sendList();
	}
}