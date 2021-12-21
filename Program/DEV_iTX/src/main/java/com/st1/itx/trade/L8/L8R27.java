package com.st1.itx.trade.L8;

import java.util.ArrayList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.dataVO.TotaVo;
import com.st1.itx.db.domain.JcicZ055;
import com.st1.itx.db.service.JcicZ055Service;
import com.st1.itx.tradeService.TradeBuffer;

@Service("L8R27")
@Scope("prototype")
/**
 * 
 * 
 * @author Fegie / Mata
 * @version 1.0.0
 */
public class L8R27 extends TradeBuffer {
	/* DB服務注入 */
	@Autowired
	public JcicZ055Service iJcicZ055Service;

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active L8r27 ");
		this.totaVo.init(titaVo);
		String iUkey = titaVo.getParam("RimUkey");
		JcicZ055 iJcicZ055 = new JcicZ055();
		iJcicZ055 = iJcicZ055Service.ukeyFirst(iUkey, titaVo);

		if (iJcicZ055 == null) {
			throw new LogicException(titaVo, "E0001", ""); // 無此代號錯誤
		} else {
			totaVo.putParam("L8r27CustId", iJcicZ055.getCustId());
			totaVo.putParam("L8r27SubmitKey", iJcicZ055.getSubmitKey());
			totaVo.putParam("L8r27CaseStatus", iJcicZ055.getCaseStatus());
			totaVo.putParam("L8r27ClaimDate", iJcicZ055.getClaimDate());
			totaVo.putParam("L8r27CourtCode", iJcicZ055.getCourtCode());
			totaVo.putParam("L8r27Year", iJcicZ055.getYear() - 1911);
			totaVo.putParam("L8r27CourtDiv", iJcicZ055.getCourtDiv());
			totaVo.putParam("L8r27CourtCaseNo", iJcicZ055.getCourtCaseNo());
			totaVo.putParam("L8r27PayDate", iJcicZ055.getPayDate());
			totaVo.putParam("L8r27PayEndDate", iJcicZ055.getPayEndDate());
			totaVo.putParam("L8r27Period", iJcicZ055.getPeriod());
			totaVo.putParam("L8r27Rate", iJcicZ055.getRate());
			totaVo.putParam("L8r27IsImplement", iJcicZ055.getIsImplement());
			totaVo.putParam("L8r27InspectName", iJcicZ055.getInspectName());
			totaVo.putParam("L8r27OutstandAmt", iJcicZ055.getOutstandAmt());
			totaVo.putParam("L8r27SubAmt", iJcicZ055.getSubAmt());
			totaVo.putParam("L8r27ClaimStatus1", iJcicZ055.getClaimStatus1());
			totaVo.putParam("L8r27SaveDate", iJcicZ055.getSaveDate());
			totaVo.putParam("L8r27ClaimStatus2", iJcicZ055.getClaimStatus2());
			totaVo.putParam("L8r27SaveEndDate", iJcicZ055.getSaveEndDate());
			totaVo.putParam("L8r27TranKey", iJcicZ055.getTranKey());
			totaVo.putParam("L8r27OutJcicTxtDate", iJcicZ055.getOutJcicTxtDate());
		}

		this.addList(this.totaVo);
		return this.sendList();
	}
}