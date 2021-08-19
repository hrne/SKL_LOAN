package com.st1.itx.trade.L8;

import java.util.ArrayList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.dataVO.TotaVo;
import com.st1.itx.db.domain.JcicZ443;
import com.st1.itx.db.service.JcicZ443Service;
import com.st1.itx.tradeService.TradeBuffer;

@Service("L8R41")
@Scope("prototype")
/**
 * 
 * @author Fegie / Mata
 * @version 1.0.0
 */
public class L8R41 extends TradeBuffer {
	/* DB服務注入 */
	@Autowired
	public JcicZ443Service iJcicZ443Service;

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active L8r43 ");
		this.totaVo.init(titaVo);
		String iUkey = titaVo.getParam("RimUkey");
		JcicZ443 iJcicZ443 = new JcicZ443();
		iJcicZ443 = iJcicZ443Service.ukeyFirst(iUkey, titaVo);

		if (iJcicZ443 == null) {
			throw new LogicException(titaVo, "E0001", ""); // 無此代號錯誤
		} else {
			totaVo.putParam("L8r44TranKey",iJcicZ443.getTranKey());// 交易代碼
			totaVo.putParam("L8r44CustId", iJcicZ443.getCustId());// 債務人IDN
			totaVo.putParam("L8r44SubmitKey", iJcicZ443.getSubmitKey());// 報送單位代號
            totaVo.putParam("L8r44ApplyDate", iJcicZ443.getApplyDate());// 調解申請日
            totaVo.putParam("L8r44CourtCode", iJcicZ443.getCourtCode());// 受理調解機構代號
            
            totaVo.putParam("L8r44OutJcicTxtDate", iJcicZ443.getOutJcicTxtDate());// 轉JCIC文字檔日期  
		}
		this.addList(this.totaVo);
		return this.sendList();
	}
}