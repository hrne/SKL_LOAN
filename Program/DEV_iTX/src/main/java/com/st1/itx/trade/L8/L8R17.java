package com.st1.itx.trade.L8;

import java.util.ArrayList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.dataVO.TotaVo;
import com.st1.itx.db.domain.JcicZ046;
import com.st1.itx.db.service.JcicZ046Service;
import com.st1.itx.tradeService.TradeBuffer;

@Service("L8R17")
@Scope("prototype")
/**
 * 
 * 
 * @author Mata
 * @version 1.0.0
 */
public class L8R17 extends TradeBuffer {
	/* DB服務注入 */
	@Autowired
	public JcicZ046Service iJcicZ046Service;


	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active L8r17 ");
		this.totaVo.init(titaVo);
		String iUkey = titaVo.getParam("RimUkey");
		JcicZ046 iJcicZ046 = new JcicZ046();
		iJcicZ046 = iJcicZ046Service.ukeyFirst(iUkey, titaVo);
		
		if (iJcicZ046 == null) {
			throw new LogicException(titaVo, "E0001", ""); // 無此代號錯誤
		}else {
			totaVo.putParam("L8r17CustId", iJcicZ046.getCustId());
			totaVo.putParam("L8r17SubmitKey", iJcicZ046.getSubmitKey());
			totaVo.putParam("L8r17RcDate",iJcicZ046.getRcDate());
			totaVo.putParam("L8r17CloseCode",iJcicZ046.getCloseCode()); 
			totaVo.putParam("L8r17BreakCode",iJcicZ046.getBreakCode()); 
			totaVo.putParam("L8r17CloseDate",iJcicZ046.getCloseDate()); 
			totaVo.putParam("L8r17TranKey", iJcicZ046.getTranKey());	
			totaVo.putParam("L8r17OutJcicTxtDate", iJcicZ046.getOutJcicTxtDate());
		}	
		this.addList(this.totaVo);
		return this.sendList();
	}
}