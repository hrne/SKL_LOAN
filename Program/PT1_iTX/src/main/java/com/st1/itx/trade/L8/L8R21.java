package com.st1.itx.trade.L8;

import java.util.ArrayList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.dataVO.TotaVo;
import com.st1.itx.db.domain.JcicZ049;
import com.st1.itx.db.service.JcicZ049Service;
import com.st1.itx.tradeService.TradeBuffer;

@Service("L8R21")
@Scope("prototype")
/**
 * 
 * 
 * @author Mata
 * @version 1.0.0
 */
public class L8R21 extends TradeBuffer {
	/* DB服務注入 */
	@Autowired
	public JcicZ049Service iJcicZ049Service;

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active L8r21 ");
		this.totaVo.init(titaVo);
		String iUkey = titaVo.getParam("RimUkey");
		JcicZ049 iJcicZ049 = new JcicZ049();
		iJcicZ049 = iJcicZ049Service.ukeyFirst(iUkey, titaVo);

		if (iJcicZ049 == null) {
			throw new LogicException(titaVo, "E0001", ""); // 無此代號錯誤
		} else {
			totaVo.putParam("L8r21CustId", iJcicZ049.getCustId());
			totaVo.putParam("L8r21SubmitKey", iJcicZ049.getSubmitKey());
			totaVo.putParam("L8r21RcDate", iJcicZ049.getRcDate());
			totaVo.putParam("L8r21ClaimStatus", iJcicZ049.getClaimStatus());
			totaVo.putParam("L8r21ApplyDate", iJcicZ049.getApplyDate());
			totaVo.putParam("L8r21CourtCode", iJcicZ049.getCourtCode());
			totaVo.putParam("L8r21Year", iJcicZ049.getYear() - 1911);
			totaVo.putParam("L8r21CourtDiv", iJcicZ049.getCourtDiv());
			totaVo.putParam("L8r21CourtCaseNo", iJcicZ049.getCourtCaseNo());
			totaVo.putParam("L8r21Approve", iJcicZ049.getApprove());
			totaVo.putParam("L8r21ClaimDate", iJcicZ049.getClaimDate());
			totaVo.putParam("L8r21TranKey", iJcicZ049.getTranKey());
			totaVo.putParam("L8r21OutJcicTxtDate", iJcicZ049.getOutJcicTxtDate());
		}
		this.addList(this.totaVo);
		return this.sendList();
	}
}