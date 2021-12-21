package com.st1.itx.trade.L8;

import java.util.ArrayList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.dataVO.TotaVo;
import com.st1.itx.db.domain.JcicZ048;
import com.st1.itx.db.service.JcicZ048Service;
import com.st1.itx.tradeService.TradeBuffer;

@Service("L8R20")
@Scope("prototype")
/**
 * 
 * 
 * @author Mata
 * @version 1.0.0
 */
public class L8R20 extends TradeBuffer {
	/* DB服務注入 */
	@Autowired
	public JcicZ048Service iJcicZ048Service;

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active L8r20 ");
		this.totaVo.init(titaVo);
		String iUkey = titaVo.getParam("RimUkey");
		JcicZ048 iJcicZ048 = new JcicZ048();
		iJcicZ048 = iJcicZ048Service.ukeyFirst(iUkey, titaVo);

		if (iJcicZ048 == null) {
			throw new LogicException(titaVo, "E0001", ""); // 無此代號錯誤
		} else {
			totaVo.putParam("L8r20CustId", iJcicZ048.getCustId());
			totaVo.putParam("L8r20SubmitKey", iJcicZ048.getSubmitKey());
			totaVo.putParam("L8r20RcDate", iJcicZ048.getRcDate());
			totaVo.putParam("L8r20CustRegAddr", iJcicZ048.getCustRegAddr());
			totaVo.putParam("L8r20CustComAddr", iJcicZ048.getCustComAddr());
			totaVo.putParam("L8r20CustRegTelNo", iJcicZ048.getCustRegTelNo());
			totaVo.putParam("L8r20CustComTelNo", iJcicZ048.getCustComTelNo());
			totaVo.putParam("L8r20CustMobilNo", iJcicZ048.getCustMobilNo());
			totaVo.putParam("L8r20TranKey", iJcicZ048.getTranKey());
			totaVo.putParam("L8r20OutJcicTxtDate", iJcicZ048.getOutJcicTxtDate());
		}
		this.addList(this.totaVo);
		return this.sendList();
	}
}