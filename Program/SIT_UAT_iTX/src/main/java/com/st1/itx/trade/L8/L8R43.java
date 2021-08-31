package com.st1.itx.trade.L8;

import java.util.ArrayList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.dataVO.TotaVo;
import com.st1.itx.db.domain.JcicZ446;
import com.st1.itx.db.service.JcicZ446Service;
import com.st1.itx.tradeService.TradeBuffer;

@Service("L8R43")
@Scope("prototype")
/** 
 * 
 * @author Luisito / Mata
 * @version 1.0.0
 */
public class L8R43 extends TradeBuffer {
	/* DB服務注入 */
	@Autowired
	public JcicZ446Service iJcicZ446Service;

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active L8r43 ");
		this.totaVo.init(titaVo);
		String iUkey = titaVo.getParam("RimUkey");
		JcicZ446 iJcicZ446 = new JcicZ446();
		iJcicZ446 = iJcicZ446Service.ukeyFirst(iUkey, titaVo);

		if (iJcicZ446 == null) {
			throw new LogicException(titaVo, "E0001", ""); // 無此代號錯誤
		} else {
			totaVo.putParam("L8r43TranKey",iJcicZ446.getTranKey());// 交易代碼
			totaVo.putParam("L8r43CustId", iJcicZ446.getCustId());// 債務人IDN
			totaVo.putParam("L8r43SubmitKey", iJcicZ446.getSubmitKey());// 報送單位代號
            totaVo.putParam("L8r43ApplyDate", iJcicZ446.getApplyDate());// 調解申請日
            totaVo.putParam("L8r43CourtCode", iJcicZ446.getCourtCode());// 受理調解機構代號
            totaVo.putParam("L8r43CloseCode", iJcicZ446.getCloseCode());
            totaVo.putParam("L8r43CloseDate", iJcicZ446.getCloseDate());
			totaVo.putParam("L8r43OutJcicTxtDate", iJcicZ446.getOutJcicTxtDate());// 轉JCIC文字檔日期  
		}
		this.addList(this.totaVo);
		return this.sendList();
	}
}