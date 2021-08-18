package com.st1.itx.trade.L8;

import java.util.ArrayList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.dataVO.TotaVo;
import com.st1.itx.db.domain.JcicZ444;
import com.st1.itx.db.service.JcicZ444Service;
import com.st1.itx.tradeService.TradeBuffer;

@Service("L8R42")
@Scope("prototype")
/**
 * 
 * @author Fegie / Mata
 * @version 1.0.0
 */
public class L8R42 extends TradeBuffer {
	/* DB服務注入 */
	@Autowired
	public JcicZ444Service iJcicZ444Service;

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active L8r44 ");
		this.totaVo.init(titaVo);
		String iUkey = titaVo.getParam("RimUkey");
		JcicZ444 iJcicZ444 = new JcicZ444();
		iJcicZ444 = iJcicZ444Service.ukeyFirst(iUkey, titaVo);

		if (iJcicZ444 == null) {
			throw new LogicException(titaVo, "E0001", ""); // 無此代號錯誤
		} else {
			totaVo.putParam("L8r44TranKey",iJcicZ444.getTranKey());// 交易代碼
			totaVo.putParam("L8r44CustId", iJcicZ444.getCustId());// 債務人IDN
			totaVo.putParam("L8r44SubmitKey", iJcicZ444.getSubmitKey());// 報送單位代號
            totaVo.putParam("L8r44ApplyDate", iJcicZ444.getApplyDate());// 調解申請日
            totaVo.putParam("L8r44CourtCode", iJcicZ444.getCourtCode());// 受理調解機構代號
            totaVo.putParam("L8r44CustRegAddr",iJcicZ444.getCustRegAddr());
            totaVo.putParam("L8r44CustComAddr",iJcicZ444.getCustComAddr());
            totaVo.putParam("L8r44CustRegTelNo",iJcicZ444.getCustRegTelNo());
            totaVo.putParam("L8r44CustComTelNo",iJcicZ444.getCustComTelNo());
            totaVo.putParam("L8r44CustMobilNo",iJcicZ444.getCustMobilNo());
			totaVo.putParam("L8r44OutJcicTxtDate", iJcicZ444.getOutJcicTxtDate());// 轉JCIC文字檔日期  
		}
		this.addList(this.totaVo);
		return this.sendList();
	}
}