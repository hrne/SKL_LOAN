package com.st1.itx.trade.L8;

import java.util.ArrayList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.dataVO.TotaVo;
import com.st1.itx.db.domain.JcicZ063;
import com.st1.itx.db.service.JcicZ063Service;
import com.st1.itx.tradeService.TradeBuffer;

@Service("L8R32")
@Scope("prototype")
/**
 * 
 * 
 * @author Luisito / Mata
 * @version 1.0.0
 */
public class L8R32 extends TradeBuffer {
	/* DB服務注入 */
	@Autowired
	public JcicZ063Service iJcicZ063Service;


	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active L8r32 ");
		this.totaVo.init(titaVo);
		String iUkey = titaVo.getParam("RimUkey");
		JcicZ063 iJcicZ063 = new JcicZ063();
		iJcicZ063 = iJcicZ063Service.ukeyFirst(iUkey, titaVo);
		
		if (iJcicZ063 == null) {
			throw new LogicException(titaVo, "E0001", ""); // 無此代號錯誤
		}else {
			totaVo.putParam("L8r32TranKey", iJcicZ063.getTranKey());
			totaVo.putParam("L8r32CustId", iJcicZ063.getCustId());
			totaVo.putParam("L8r32SubmitKey", iJcicZ063.getSubmitKey());
			totaVo.putParam("L8r32RcDate", iJcicZ063.getRcDate());
			totaVo.putParam("L8r32ChangePayDate", iJcicZ063.getChangePayDate());
			totaVo.putParam("L8r32ClosedDate", iJcicZ063.getClosedDate());
			totaVo.putParam("L8r32ClosedResult", iJcicZ063.getClosedResult());			
			totaVo.putParam("L8r32OutJcicTxtDate", iJcicZ063.getOutJcicTxtDate());		
		}
		this.addList(this.totaVo);
		return this.sendList();
	}
}