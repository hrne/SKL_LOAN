package com.st1.itx.trade.L8;

import java.util.ArrayList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.dataVO.TotaVo;
import com.st1.itx.db.domain.JcicZ056;
import com.st1.itx.db.service.JcicZ056Service;
import com.st1.itx.tradeService.TradeBuffer;

@Service("L8R28")
@Scope("prototype")
/**
 * 
 * 
 * @author Fegie / Mata
 * @version 1.0.0
 */
public class L8R28 extends TradeBuffer {
	/* DB服務注入 */
	@Autowired
	public JcicZ056Service iJcicZ056Service;

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active L8r28 ");
		this.totaVo.init(titaVo);
		String iUkey = titaVo.getParam("RimUkey");
		JcicZ056 iJcicZ056 = new JcicZ056();
		iJcicZ056 = iJcicZ056Service.ukeyFirst(iUkey, titaVo);

		if (iJcicZ056 == null) {
			throw new LogicException(titaVo, "E0001", ""); // 無此代號錯誤
		} else {
			totaVo.putParam("L8r28CustId", iJcicZ056.getCustId());
			totaVo.putParam("L8r28SubmitKey", iJcicZ056.getSubmitKey());
			totaVo.putParam("L8r28CaseStatus", iJcicZ056.getCaseStatus());
			totaVo.putParam("L8r28ClaimDate", iJcicZ056.getClaimDate());
			totaVo.putParam("L8r28CourtCode", iJcicZ056.getCourtCode());
			totaVo.putParam("L8r28Year", iJcicZ056.getYear() - 1911);
			totaVo.putParam("L8r28CourtDiv", iJcicZ056.getCourtDiv());
			totaVo.putParam("L8r28CourtCaseNo", iJcicZ056.getCourtCaseNo());
			totaVo.putParam("L8r28Approve", iJcicZ056.getApprove());
			totaVo.putParam("L8r28OutstandAmt", iJcicZ056.getOutstandAmt());
			totaVo.putParam("L8r28ClaimStatus1", iJcicZ056.getClaimStatus1());
			totaVo.putParam("L8r28SaveDate", iJcicZ056.getSaveDate());
			totaVo.putParam("L8r28ClaimStatus2", iJcicZ056.getClaimStatus2());
			totaVo.putParam("L8r28SaveEndDate", iJcicZ056.getSaveEndDate());
			totaVo.putParam("L8r28SubAmt", iJcicZ056.getSubAmt());
			totaVo.putParam("L8r28AdminName", iJcicZ056.getAdminName());
			totaVo.putParam("L8r28TranKey", iJcicZ056.getTranKey());
			totaVo.putParam("L8r28OutJcicTxtDate", iJcicZ056.getOutJcicTxtDate());
		}

		this.addList(this.totaVo);
		return this.sendList();
	}
}