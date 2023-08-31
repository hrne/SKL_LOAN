package com.st1.itx.trade.L4;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.dataVO.TotaVo;
import com.st1.itx.db.domain.CdCode;
import com.st1.itx.db.domain.CdCodeId;
import com.st1.itx.db.domain.EmpDeductDtl;
import com.st1.itx.db.domain.EmpDeductDtlId;
import com.st1.itx.db.service.CdCodeService;
import com.st1.itx.db.service.EmpDeductDtlService;
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.parse.Parse;

@Service("L4R29")
@Scope("prototype")

public class L4R29 extends TradeBuffer {

	/* DB服務注入 */

	@Autowired
	public EmpDeductDtlService empDeductDtlService;
	@Autowired
	public CdCodeService cdCodeService;

	@Autowired
	public Parse parse;

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active L4R29 ");
		this.totaVo.init(titaVo);

//		L4512調資料

		int EntryDate = parse.stringToInteger(titaVo.getParam("RimEntryDate"));
		int PerfMonth = parse.stringToInteger(titaVo.getParam("RimPerfMonth")) + 191100;
		String ProcCode = titaVo.getParam("RimProcCode");
		String AcctCode = titaVo.getParam("RimAcctCode");
		String RepayCode = titaVo.getParam("RimRepayCode");
		int AchRepayCode = parse.stringToInteger(titaVo.getParam("RimAchRepayCode"));
		int CustNo = parse.stringToInteger(titaVo.getParam("RimCustNo"));
		int FacmNo = parse.stringToInteger(titaVo.getParam("RimFacmNo"));
		int BormNo = parse.stringToInteger(titaVo.getParam("RimBormNo"));

		EmpDeductDtl tEmpDeductDtl = new EmpDeductDtl();
		EmpDeductDtlId tEmpDeductDtlId = new EmpDeductDtlId();

		tEmpDeductDtlId.setEntryDate(EntryDate);
		tEmpDeductDtlId.setPerfMonth(PerfMonth);
		tEmpDeductDtlId.setProcCode(ProcCode);
		tEmpDeductDtlId.setAcctCode(AcctCode);
		tEmpDeductDtlId.setRepayCode(RepayCode);
		tEmpDeductDtlId.setAchRepayCode(AchRepayCode);
		tEmpDeductDtlId.setCustNo(CustNo);
		tEmpDeductDtlId.setFacmNo(FacmNo);
		tEmpDeductDtlId.setBormNo(BormNo);
		tEmpDeductDtl.setEmpDeductDtlId(tEmpDeductDtlId);

		tEmpDeductDtl = empDeductDtlService.findById(tEmpDeductDtlId, titaVo);

		if (tEmpDeductDtl != null) {
			this.totaVo.putParam("L4R29TxAmt", tEmpDeductDtl.getTxAmt());
			this.totaVo.putParam("L4R29DeptCode", tEmpDeductDtl.getDeptCode());
			this.totaVo.putParam("L4R29RepayAmt", tEmpDeductDtl.getRepayAmt());
			this.totaVo.putParam("L4R29UnitCode", tEmpDeductDtl.getUnitCode());
			this.totaVo.putParam("L4R29Principal", tEmpDeductDtl.getPrincipal());
			this.totaVo.putParam("L4R29ResignCode", tEmpDeductDtl.getResignCode());
			this.totaVo.putParam("L4R29Interest", tEmpDeductDtl.getInterest());
			this.totaVo.putParam("L4R29PositCode", tEmpDeductDtl.getPositCode());
			this.totaVo.putParam("L4R29SumOvpayAmt", tEmpDeductDtl.getSumOvpayAmt());
			this.totaVo.putParam("L4R29IntStartDate", tEmpDeductDtl.getIntStartDate());
			this.totaVo.putParam("L4R29BatchNo", tEmpDeductDtl.getBatchNo());
			this.totaVo.putParam("L4R29IntEndDate", tEmpDeductDtl.getIntEndDate());
			this.totaVo.putParam("L4R29ErrMsg", tEmpDeductDtl.getErrMsg().trim());
			CdCode tCdCode = cdCodeService.findById(new CdCodeId("ProcCode", "004" + tEmpDeductDtl.getErrMsg().trim()),
					titaVo);
			this.totaVo.putParam("L4R29ErrMsgX", tCdCode == null ? "" : tCdCode.getItem().trim());
			this.totaVo.putParam("L4R29Acdate", tEmpDeductDtl.getAcdate());
			this.totaVo.putParam("L4R29TitaTxtNo", tEmpDeductDtl.getTitaTxtNo());
		} else {
			throw new LogicException("E0001", "EmpDeductDtl");
		}

		this.addList(this.totaVo);
		return this.sendList();
	}
}