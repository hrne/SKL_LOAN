package com.st1.itx.trade.L2;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.dataVO.TotaVo;
import com.st1.itx.db.domain.FacCaseAppl;
import com.st1.itx.db.domain.FacMain;
import com.st1.itx.db.service.FacCaseApplService;
import com.st1.itx.db.service.FacMainService;
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.parse.Parse;

@Service("L2R50")
@Scope("prototype")
/**
 * 
 * 
 * @author YuJiaXing
 * @version 1.0.0
 */
public class L2R50 extends TradeBuffer {

	@Autowired
	public FacMainService facMainService;
	@Autowired
	public FacCaseApplService facCaseApplService;
	@Autowired
	Parse parse;

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active L2R50 ");
		this.totaVo.init(titaVo);

		// 取得輸入資料
		int iCustNo = this.parse.stringToInteger(titaVo.getParam("RimCustNo"));
		int iCreditSysNo = this.parse.stringToInteger(titaVo.getParam("RimCreditSysNo"));
		int iApplNo = this.parse.stringToInteger(titaVo.getParam("RimApplNo"));

		FacMain tFacMain = new FacMain();
		tFacMain = facMainService.fildCustNoCreditSysNoFirst(iCustNo, iCreditSysNo, titaVo);
		FacCaseAppl tFacCaseAppl = new FacCaseAppl();
		tFacCaseAppl = facCaseApplService.findById(iApplNo, titaVo);
		if (tFacCaseAppl == null) {
			throw new LogicException(titaVo, "E2019", "申請號碼 = " + iApplNo); // 此申請號碼不存在案件申請檔

		}

		this.totaVo.putParam("L2r50Exist", "N");
		this.totaVo.putParam("L2r50ApplNo", "0");
		this.totaVo.putParam("L2r50CreditSysNo", "0");
		this.totaVo.putParam("L2r50ProdNo", "");
		this.totaVo.putParam("L2r50BaseRateCode", "");
		this.totaVo.putParam("L2r50RateIncr", "0");
		this.totaVo.putParam("L2r50ApproveRate", "0");
		this.totaVo.putParam("L2r50RateCode", "");
		this.totaVo.putParam("L2r50FirstRateAdjFreq", "0");
		this.totaVo.putParam("L2r50RateAdjFreq", "0");
		this.totaVo.putParam("L2r50CurrencyCode", "");
		this.totaVo.putParam("L2r50LineAmt", "0");
		this.totaVo.putParam("L2r50AcctCode", "");
		this.totaVo.putParam("L2r50LoanTermYy", "0");
		this.totaVo.putParam("L2r50LoanTermMm", "0");
		this.totaVo.putParam("L2r50LoanTermDd", "0");
		this.totaVo.putParam("L2r50AmortizedCode", "");
		this.totaVo.putParam("L2r50FreqBase", "");
		this.totaVo.putParam("L2r50PayIntFreq", "0");
		this.totaVo.putParam("L2r50RepayFreq", "0");
		this.totaVo.putParam("L2r50UtilDeadline", "0");
		this.totaVo.putParam("L2r50GracePeriod", "0");
		this.totaVo.putParam("L2r50AcctFee", "0");
		this.totaVo.putParam("L2r50ExtraRepayCode", "");
		this.totaVo.putParam("L2r50IntCalcCode", "");
		this.totaVo.putParam("L2r50CustTypeCode", "");
		this.totaVo.putParam("L2r50RuleCode", "");
		this.totaVo.putParam("L2r50RecycleCode", "");
		this.totaVo.putParam("L2r50RecycleDeadline", "0");
		this.totaVo.putParam("L2r50UsageCode", "");
		this.totaVo.putParam("L2r50DepartmentCode", "");
		this.totaVo.putParam("L2r50IncomeTaxFlag", "");
		this.totaVo.putParam("L2r50CompensateFlag", "");
		this.totaVo.putParam("L2r50IrrevocableFlag", "");
		this.totaVo.putParam("L2r50RateAdjNoticeCode", "");
		this.totaVo.putParam("L2r50Introducer", "");
		this.totaVo.putParam("L2r50District", "");
		this.totaVo.putParam("L2r50FireOfficer", "");
		this.totaVo.putParam("L2r50Estimate", "");
		this.totaVo.putParam("L2r50CreditOfficer", "");
		this.totaVo.putParam("L2r50LoanOfficer", "");
		this.totaVo.putParam("L2r50BusinessOfficer", "");
		this.totaVo.putParam("L2r50Supervisor", "");
		this.totaVo.putParam("L2r50Coorgnizer", "");
		this.totaVo.putParam("L2r50CreditScore", "0");
		this.totaVo.putParam("L2r50GuaranteeDate", "0");

		if (tFacMain != null) {

			this.totaVo.putParam("L2r50Exist", "Y");
			this.totaVo.putParam("L2r50ApplNo", tFacMain.getApplNo());
			this.totaVo.putParam("L2r50CreditSysNo", tFacMain.getCreditSysNo());
			this.totaVo.putParam("L2r50ProdNo", tFacCaseAppl.getProdNo());
			this.totaVo.putParam("L2r50BaseRateCode", tFacMain.getBaseRateCode());
			this.totaVo.putParam("L2r50RateIncr", tFacMain.getRateIncr());
			this.totaVo.putParam("L2r50ApproveRate", tFacMain.getApproveRate());
			this.totaVo.putParam("L2r50RateCode", tFacMain.getRateCode());
			this.totaVo.putParam("L2r50FirstRateAdjFreq", tFacMain.getFirstRateAdjFreq());
			this.totaVo.putParam("L2r50RateAdjFreq", tFacMain.getRateAdjFreq());
			this.totaVo.putParam("L2r50CurrencyCode", tFacCaseAppl.getCurrencyCode());
			this.totaVo.putParam("L2r50LineAmt", tFacCaseAppl.getApplAmt());
			this.totaVo.putParam("L2r50AcctCode", tFacMain.getAcctCode());
			this.totaVo.putParam("L2r50LoanTermYy", tFacMain.getLoanTermYy());
			this.totaVo.putParam("L2r50LoanTermMm", tFacMain.getLoanTermMm());
			this.totaVo.putParam("L2r50LoanTermDd", tFacMain.getLoanTermDd());
			this.totaVo.putParam("L2r50AmortizedCode", tFacMain.getAmortizedCode());
			this.totaVo.putParam("L2r50FreqBase", tFacMain.getFreqBase());
			this.totaVo.putParam("L2r50PayIntFreq", tFacMain.getPayIntFreq());
			this.totaVo.putParam("L2r50RepayFreq", tFacMain.getRepayFreq());
			this.totaVo.putParam("L2r50UtilDeadline", tFacMain.getUtilDeadline());
			this.totaVo.putParam("L2r50GracePeriod", tFacMain.getGracePeriod());
			this.totaVo.putParam("L2r50AcctFee", tFacMain.getAcctFee());
			this.totaVo.putParam("L2r50ExtraRepayCode", tFacMain.getExtraRepayCode());
			this.totaVo.putParam("L2r50IntCalcCode", tFacMain.getIntCalcCode());
			this.totaVo.putParam("L2r50CustTypeCode", tFacMain.getCustTypeCode());
			this.totaVo.putParam("L2r50RuleCode", tFacMain.getRuleCode());
			this.totaVo.putParam("L2r50RecycleCode", tFacMain.getRecycleCode());
			this.totaVo.putParam("L2r50RecycleDeadline", tFacMain.getRecycleDeadline());
			this.totaVo.putParam("L2r50UsageCode", tFacMain.getUsageCode());
			this.totaVo.putParam("L2r50DepartmentCode", tFacMain.getDepartmentCode());
			this.totaVo.putParam("L2r50IncomeTaxFlag", tFacMain.getIncomeTaxFlag());
			this.totaVo.putParam("L2r50CompensateFlag", tFacMain.getCompensateFlag());
			this.totaVo.putParam("L2r50IrrevocableFlag", tFacMain.getIrrevocableFlag());
			this.totaVo.putParam("L2r50RateAdjNoticeCode", tFacMain.getRateAdjNoticeCode());
			this.totaVo.putParam("L2r50Introducer", tFacCaseAppl.getIntroducer());
			this.totaVo.putParam("L2r50District", tFacMain.getDistrict());
			this.totaVo.putParam("L2r50FireOfficer", tFacMain.getFireOfficer());
			this.totaVo.putParam("L2r50Estimate", tFacCaseAppl.getEstimate());
			this.totaVo.putParam("L2r50CreditOfficer", tFacCaseAppl.getCreditOfficer());
			this.totaVo.putParam("L2r50LoanOfficer", tFacCaseAppl.getLoanOfficer());
			this.totaVo.putParam("L2r50BusinessOfficer", tFacMain.getBusinessOfficer());
			this.totaVo.putParam("L2r50Supervisor", tFacCaseAppl.getSupervisor());
			this.totaVo.putParam("L2r50Coorgnizer", tFacCaseAppl.getCoorgnizer());
			this.totaVo.putParam("L2r50CreditScore", tFacMain.getCreditScore());
			this.totaVo.putParam("L2r50GuaranteeDate", tFacMain.getGuaranteeDate());
		}

		this.addList(this.totaVo);
		return this.sendList();
	}
}