package com.st1.itx.trade.L8;

import java.util.ArrayList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.dataVO.TotaVo;
import com.st1.itx.db.domain.JcicZ451;
import com.st1.itx.db.service.JcicZ451Service;
import com.st1.itx.tradeService.TradeBuffer;


@Service("L8R47")
@Scope("prototype")
/**
 * 
 * @author Fegie / Mata
 * @version 1.0.0
 */
public class L8R47 extends TradeBuffer {
	/* DB服務注入 */
	@Autowired
	public JcicZ451Service iJcicZ451Service;

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active L8r47 ");
		this.totaVo.init(titaVo);
		String iUkey = titaVo.getParam("RimUkey");
		JcicZ451 iJcicZ451 = new JcicZ451();
		iJcicZ451 = iJcicZ451Service.ukeyFirst(iUkey, titaVo);
		
		if (iJcicZ451 == null) {
			throw new LogicException(titaVo, "E0001", ""); // 無此代號錯誤
		}else {
			totaVo.putParam("L8r47TranKey", iJcicZ451.getTranKey());
			totaVo.putParam("L8r47CustId", iJcicZ451.getCustId());
			totaVo.putParam("L8r47SubmitKey", iJcicZ451.getSubmitKey());
			totaVo.putParam("L8r47ApplyDate", iJcicZ451.getApplyDate());
			totaVo.putParam("L8r47CourtCode", iJcicZ451.getCourtCode());
			totaVo.putParam("L8r47DelayYM", iJcicZ451.getDelayYM());
			totaVo.putParam("L8r47DelayCode", iJcicZ451.getDelayCode());
			totaVo.putParam("L8r47OutJcicTxtDate", iJcicZ451.getOutJcicTxtDate());		
		}

		
		this.addList(this.totaVo);
		return this.sendList();
	}
}