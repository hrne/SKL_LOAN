package com.st1.itx.trade.L8;

import java.util.ArrayList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.dataVO.TotaVo;
import com.st1.itx.db.domain.JcicZ051;
import com.st1.itx.db.service.JcicZ051Service;
import com.st1.itx.tradeService.TradeBuffer;

@Service("L8R23")
@Scope("prototype")
/**
 * 
 * 
 * @author Mata
 * @version 1.0.0
 */
public class L8R23 extends TradeBuffer {
	/* DB服務注入 */
	@Autowired
	public JcicZ051Service iJcicZ051Service;

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active L8r23 ");
		this.totaVo.init(titaVo);
		String iUkey = titaVo.getParam("RimUkey");
		JcicZ051 iJcicZ051 = new JcicZ051();
		iJcicZ051 = iJcicZ051Service.ukeyFirst(iUkey, titaVo);

		if (iJcicZ051 == null) {
			throw new LogicException(titaVo, "E0001", ""); // 無此代號錯誤
		} else {
			totaVo.putParam("L8r23CustId", iJcicZ051.getCustId());
			totaVo.putParam("L8r23SubmitKey", iJcicZ051.getSubmitKey());
			totaVo.putParam("L8r23RcDate", iJcicZ051.getRcDate());
			totaVo.putParam("L8r23DelayCode", iJcicZ051.getDelayCode());
			totaVo.putParam("L8r23DelayYM", iJcicZ051.getDelayYM() - 191100);
			totaVo.putParam("L8r23DelayDesc", iJcicZ051.getDelayDesc());
			totaVo.putParam("L8r23TranKey", iJcicZ051.getTranKey());
			totaVo.putParam("L8r23OutJcicTxtDate", iJcicZ051.getOutJcicTxtDate());
		}
		this.addList(this.totaVo);
		return this.sendList();
	}
}