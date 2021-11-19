package com.st1.itx.trade.L4;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.dataVO.TotaVo;
import com.st1.itx.db.domain.BankDeductDtl;
import com.st1.itx.db.domain.BankDeductDtlId;
import com.st1.itx.db.service.BankDeductDtlService;
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.parse.Parse;

@Service("L4R27")
@Scope("prototype")

public class L4R27 extends TradeBuffer {

	/* DB服務注入 */
	@Autowired
	public BankDeductDtlService bankDeductDtlService;
	
	@Autowired
	public Parse parse;

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active L4R27 ");
		this.totaVo.init(titaVo);

//		L4611新增續保調rim

		int iCustNo = parse.stringToInteger(titaVo.getParam("RimCustNo"));
		int iFacmNo = parse.stringToInteger(titaVo.getParam("RimFacmNo"));
		int iEntryDate = parse.stringToInteger(titaVo.getParam("RimEntryDate"));
		int iPayIntDate = parse.stringToInteger(titaVo.getParam("RimPayIntDate"));
		int RepayType = parse.stringToInteger(titaVo.getParam("RimRepayType"));
		

		BankDeductDtlId tBankDeductDtlId = new BankDeductDtlId();
		tBankDeductDtlId.setCustNo(iCustNo);
		tBankDeductDtlId.setFacmNo(iFacmNo);
		tBankDeductDtlId.setEntryDate(iEntryDate);
		tBankDeductDtlId.setPayIntDate(iPayIntDate);
		tBankDeductDtlId.setRepayType(RepayType);
		
		BankDeductDtl tBankDeductDtl = bankDeductDtlService.findById(tBankDeductDtlId, titaVo);
		
		if(tBankDeductDtl != null) {
			this.totaVo.putParam("L4r27RepayBank", tBankDeductDtl.getRepayBank());
			this.totaVo.putParam("L4r27RepayAcctNo", tBankDeductDtl.getRepayAcctNo());
			this.totaVo.putParam("L4r27AcctCode", tBankDeductDtl.getAcctCode());
			this.totaVo.putParam("L4r27TitaTxtNo", tBankDeductDtl.getTitaTxtNo());
			this.totaVo.putParam("L4r27IntStartDate", tBankDeductDtl.getIntStartDate());
			this.totaVo.putParam("L4r27IntEndDate", tBankDeductDtl.getIntEndDate());
			this.totaVo.putParam("L4r27BatchNo", String.valueOf(tBankDeductDtl.getRepayAcctNo()).substring(0,2));
		} else {
			this.totaVo.putParam("L4r27RepayBank", "");
			this.totaVo.putParam("L4r27RepayAcctNo", "");
			this.totaVo.putParam("L4r27AcctCode", "");
			this.totaVo.putParam("L4r27TitaTxtNo", "");
			this.totaVo.putParam("L4r27IntStartDate", "");
			this.totaVo.putParam("L4r27IntEndDate", "");
			this.totaVo.putParam("L4r27BatchNo", "");
		}
		
		
		this.addList(this.totaVo);
		return this.sendList();
	}
}