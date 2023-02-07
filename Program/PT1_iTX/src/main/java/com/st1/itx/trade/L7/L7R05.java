package com.st1.itx.trade.L7;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.dataVO.TotaVo;
import com.st1.itx.db.domain.TxAmlRating;
import com.st1.itx.db.domain.TxAmlRatingAppl;
import com.st1.itx.db.service.TxAmlRatingApplService;
import com.st1.itx.db.service.TxAmlRatingService;
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.parse.Parse;

@Service("L7R05")
@Scope("prototype")
public class L7R05 extends TradeBuffer {

	/* DB服務注入 */
	@Autowired
	public TxAmlRatingService txAmlRatingService;
	@Autowired
	public TxAmlRatingApplService txAmlRatingApplService;

	@Autowired
	public Parse parse;

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active L7R05 ");
		this.totaVo.init(titaVo);

		// RimCustNo=9,7
		int iItemCode = parse.stringToInteger(titaVo.getParam("ItemCode"));
		// RimFacmNo=9,3
		Long iLogNo = Long.valueOf(titaVo.getParam("LogNo"));

		if (iItemCode == 1) {
			MoveTota(txAmlRatingService.findById(iLogNo, titaVo), titaVo);
		} else {
			MoveApplTota(txAmlRatingApplService.findById(iLogNo, titaVo), titaVo);
		}

		this.addList(this.totaVo);
		return this.sendList();
	}

	private void MoveApplTota(TxAmlRatingAppl t, TitaVo titaVo) {
		this.totaVo.putParam("L7r05Unit", t.getUnit());
		this.totaVo.putParam("L7r05AcceptanceUnit", t.getAcceptanceUnit());
		this.totaVo.putParam("L7r05RoleId", t.getRoleId());
		this.totaVo.putParam("L7r05TransactionId", t.getTransactionId());
		this.totaVo.putParam("L7r05AcctNo", t.getAcctNo());
		this.totaVo.putParam("L7r05CaseNo", t.getCaseNo());
		this.totaVo.putParam("L7r05AcctId", t.getAcctId());
		this.totaVo.putParam("L7r05InsurCount", t.getInsurCount());
		this.totaVo.putParam("L7r05BirthEstDt", t.getBirthEstDt());
		this.totaVo.putParam("L7r05SourceId", t.getSourceId());
		this.totaVo.putParam("L7r05ModifyDate", t.getModifyDate());
		this.totaVo.putParam("L7r05OcupCd", t.getOcupCd());
		this.totaVo.putParam("L7r05OrgType", t.getOrgType());
		this.totaVo.putParam("L7r05Bcode", t.getBcode());
		this.totaVo.putParam("L7r05OcupNote", t.getOcupNote());
		this.totaVo.putParam("L7r05PayMethod", t.getPayMethod());
		this.totaVo.putParam("L7r05PayType", t.getPayType());
		this.totaVo.putParam("L7r05Channel", t.getChannel());
		this.totaVo.putParam("L7r05PolicyType", t.getPolicyType());
		this.totaVo.putParam("L7r05InsuranceCurrency", t.getInsuranceCurrency());
		this.totaVo.putParam("L7r05InsuranceAmount", t.getInsuranceAmount());
		this.totaVo.putParam("L7r05AddnCd", t.getAddnCd());
		this.totaVo.putParam("L7r05InsrStakesCd", t.getInsrStakesCd());
		this.totaVo.putParam("L7r05BnfcryNHdrGrpCd", t.getBnfcryNHdrGrpCd());
		this.totaVo.putParam("L7r05AgentTradeCd", t.getAgentTradeCd());
		this.totaVo.putParam("L7r05FstPayerNHdrGrpCd", t.getFstPayerNHdrGrpCd());
		this.totaVo.putParam("L7r05FstPayerNHdrStksCd", t.getFstPayerNHdrStksCd());
		this.totaVo.putParam("L7r05FstPrmOvrseaAcctCd", t.getFstPrmOvrseaAcctCd());
		this.totaVo.putParam("L7r05TotalAmtCd", t.getTotalAmtCd());
		this.totaVo.putParam("L7r05DeclinatureCd", t.getDeclinatureCd());
		this.totaVo.putParam("L7r05FstInsuredCd", t.getFstInsuredCd());
		this.totaVo.putParam("L7r05TWAddrHoldCd", t.getTWAddrHoldCd());
		this.totaVo.putParam("L7r05TWAddrLegalCd", t.getTWAddrLegalCd());
		this.totaVo.putParam("L7r05DurationCd", t.getDurationCd());
		this.totaVo.putParam("L7r05SpecialIdentity", t.getSpecialIdentity());
		this.totaVo.putParam("L7r05LawForceWarranty", t.getLawForceWarranty());
		this.totaVo.putParam("L7r05MovableGrnteeCd", t.getMovableGrnteeCd());
		this.totaVo.putParam("L7r05BearerScursGrnteeCd", t.getBearerScursGrnteeCd());
		this.totaVo.putParam("L7r05AgreeDefaultFineCd", t.getAgreeDefaultFineCd());
		this.totaVo.putParam("L7r05NonBuyingRealEstateCd", t.getNonBuyingRealEstateCd());
		this.totaVo.putParam("L7r05NonStkHolderGrnteeCd", t.getNonStkHolderGrnteeCd());
		this.totaVo.putParam("L7r05ReachCase", t.getReachCase());
		this.totaVo.putParam("L7r05AccountTypeCd", t.getAccountTypeCd());
		this.totaVo.putParam("L7r05QueryType", t.getQueryType());
		this.totaVo.putParam("L7r05IdentityCd", t.getIdentityCd());
		this.totaVo.putParam("L7r05RspRequestId", t.getRspRequestId());
		this.totaVo.putParam("L7r05RspStatus", t.getRspStatus());
		this.totaVo.putParam("L7r05RspStatusCode", t.getRspStatusCode());
		this.totaVo.putParam("L7r05RspStatusDesc", t.getRspStatusDesc());
		this.totaVo.putParam("L7r05RspUnit", t.getRspUnit());
		this.totaVo.putParam("L7r05RspTransactionId", t.getRspTransactionId());
		this.totaVo.putParam("L7r05RspAcctNo", t.getRspAcctNo());
		this.totaVo.putParam("L7r05RspCaseNo", t.getRspCaseNo());
		this.totaVo.putParam("L7r05RspInsurCount", t.getRspInsurCount());
		this.totaVo.putParam("L7r05RspTotalRatingsScore", t.getRspTotalRatingsScore());
		this.totaVo.putParam("L7r05RspTotalRatings", t.getRspTotalRatings());
	}

	private void MoveTota(TxAmlRating t, TitaVo titaVo) {
		this.totaVo.putParam("L7r05Unit", t.getUnit());
		this.totaVo.putParam("L7r05AcceptanceUnit", t.getAcceptanceUnit());
		this.totaVo.putParam("L7r05RoleId", t.getRoleId());
		this.totaVo.putParam("L7r05TransactionId", t.getTransactionId());
		this.totaVo.putParam("L7r05AcctNo", t.getAcctNo());
		this.totaVo.putParam("L7r05CaseNo", t.getCaseNo());
		this.totaVo.putParam("L7r05AcctId", t.getAcctId());
		this.totaVo.putParam("L7r05InsurCount", t.getInsurCount());
		this.totaVo.putParam("L7r05BirthEstDt", t.getBirthEstDt());
		this.totaVo.putParam("L7r05SourceId", t.getSourceId());
		this.totaVo.putParam("L7r05ModifyDate", t.getModifyDate());
		this.totaVo.putParam("L7r05OcupCd", t.getOcupCd());
		this.totaVo.putParam("L7r05OrgType", t.getOrgType());
		this.totaVo.putParam("L7r05Bcode", t.getBcode());
		this.totaVo.putParam("L7r05OcupNote", t.getOcupNote());
		this.totaVo.putParam("L7r05PayMethod", t.getPayMethod());
		this.totaVo.putParam("L7r05PayType", t.getPayType());
		this.totaVo.putParam("L7r05Channel", t.getChannel());
		this.totaVo.putParam("L7r05PolicyType", t.getPolicyType());
		this.totaVo.putParam("L7r05InsuranceCurrency", t.getInsuranceCurrency());
		this.totaVo.putParam("L7r05InsuranceAmount", t.getInsuranceAmount());
		this.totaVo.putParam("L7r05AddnCd", t.getAddnCd());
		this.totaVo.putParam("L7r05InsrStakesCd", t.getInsrStakesCd());
		this.totaVo.putParam("L7r05BnfcryNHdrGrpCd", t.getBnfcryNHdrGrpCd());
		this.totaVo.putParam("L7r05AgentTradeCd", t.getAgentTradeCd());
		this.totaVo.putParam("L7r05FstPayerNHdrGrpCd", t.getFstPayerNHdrGrpCd());
		this.totaVo.putParam("L7r05FstPayerNHdrStksCd", t.getFstPayerNHdrStksCd());
		this.totaVo.putParam("L7r05FstPrmOvrseaAcctCd", t.getFstPrmOvrseaAcctCd());
		this.totaVo.putParam("L7r05TotalAmtCd", t.getTotalAmtCd());
		this.totaVo.putParam("L7r05DeclinatureCd", t.getDeclinatureCd());
		this.totaVo.putParam("L7r05FstInsuredCd", t.getFstInsuredCd());
		this.totaVo.putParam("L7r05TWAddrHoldCd", t.getTWAddrHoldCd());
		this.totaVo.putParam("L7r05TWAddrLegalCd", t.getTWAddrLegalCd());
		this.totaVo.putParam("L7r05DurationCd", t.getDurationCd());
		this.totaVo.putParam("L7r05SpecialIdentity", t.getSpecialIdentity());
		this.totaVo.putParam("L7r05LawForceWarranty", t.getLawForceWarranty());
		this.totaVo.putParam("L7r05MovableGrnteeCd", t.getMovableGrnteeCd());
		this.totaVo.putParam("L7r05BearerScursGrnteeCd", t.getBearerScursGrnteeCd());
		this.totaVo.putParam("L7r05AgreeDefaultFineCd", t.getAgreeDefaultFineCd());
		this.totaVo.putParam("L7r05NonBuyingRealEstateCd", t.getNonBuyingRealEstateCd());
		this.totaVo.putParam("L7r05NonStkHolderGrnteeCd", t.getNonStkHolderGrnteeCd());
		this.totaVo.putParam("L7r05ReachCase", t.getReachCase());
		this.totaVo.putParam("L7r05AccountTypeCd", t.getAccountTypeCd());
		this.totaVo.putParam("L7r05QueryType", t.getQueryType());
		this.totaVo.putParam("L7r05IdentityCd", t.getIdentityCd());
		this.totaVo.putParam("L7r05RspRequestId", t.getRspRequestId());
		this.totaVo.putParam("L7r05RspStatus", t.getRspStatus());
		this.totaVo.putParam("L7r05RspStatusCode", t.getRspStatusCode());
		this.totaVo.putParam("L7r05RspStatusDesc", t.getRspStatusDesc());
		this.totaVo.putParam("L7r05RspUnit", t.getRspUnit());
		this.totaVo.putParam("L7r05RspTransactionId", t.getRspTransactionId());
		this.totaVo.putParam("L7r05RspAcctNo", t.getRspAcctNo());
		this.totaVo.putParam("L7r05RspCaseNo", t.getRspCaseNo());
		this.totaVo.putParam("L7r05RspInsurCount", t.getRspInsurCount());
		this.totaVo.putParam("L7r05RspTotalRatingsScore", t.getRspTotalRatingsScore());
		this.totaVo.putParam("L7r05RspTotalRatings", t.getRspTotalRatings());
	}
}