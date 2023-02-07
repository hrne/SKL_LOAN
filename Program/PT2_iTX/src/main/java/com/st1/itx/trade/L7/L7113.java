package com.st1.itx.trade.L7;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.st1.itx.Exception.DBException;
import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.dataVO.TotaVo;
import com.st1.itx.db.domain.TxAmlRatingAppl;
import com.st1.itx.db.service.TxAmlRatingApplService;
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.common.SendRsp;
import com.st1.itx.util.data.DataLog;
import com.st1.itx.util.parse.Parse;

/**
 * L7113 (eloan)評級資訊維護
 * 
 * @author iza
 * @version 1.0.0
 */
@Service("L7113")
@Scope("prototype")
public class L7113 extends TradeBuffer {

	/* DB服務注入 */
	@Autowired
	public TxAmlRatingApplService txAmlRatingApplService;
	@Autowired
	public DataLog iDataLog;
	@Autowired
	public SendRsp sendRsp;
	@Autowired
	Parse parse;

	// input area
	private TitaVo titaVo = new TitaVo();
	private int iFuncCode;
	private int iItemCode;
	private String iCaseNo;
	private Long iLogNo = Long.valueOf(0);

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active L7113 ");
		this.totaVo.init(titaVo);
		this.titaVo = titaVo;
		iFuncCode = this.parse.stringToInteger(titaVo.getParam("FunCd"));
		iItemCode = this.parse.stringToInteger(titaVo.getParam("ItemCode"));
		iCaseNo = titaVo.getParam("CaseNo");
		if (!titaVo.getParam("LogNo").isEmpty()) {
			iLogNo = Long.valueOf(titaVo.getParam("LogNo"));
		}
		if (iItemCode == 1) {
			throw new LogicException("E0015", "Eloan評級檔僅可查詢"); // E0015 檢查錯誤
		}
		switch (iFuncCode) {
		case 1: // 新增
			insertTxAmlRatingAppl();
			break;
		case 2: // 修改
			updTxAmlRatingAppl();
			break;
		case 4: // 刪除
			deleteTxAmlRatingAppl();
			break;
		default:
			break;
		}
		this.addList(this.totaVo);
		return this.sendList();
	}

	private void insertTxAmlRatingAppl() throws LogicException {
		this.info("insertTxAmlRatingAppl ...");
		TxAmlRatingAppl txAmlRatingAppl = new TxAmlRatingAppl();
		txAmlRatingAppl.setUnit(titaVo.getParam("Unit"));
		txAmlRatingAppl.setAcceptanceUnit(titaVo.getParam("AcceptanceUnit"));
		txAmlRatingAppl.setRoleId(titaVo.getParam("RoleId"));
		txAmlRatingAppl.setTransactionId(titaVo.getParam("TransactionId"));
		txAmlRatingAppl.setAcctNo(titaVo.getParam("AcctNo"));
		txAmlRatingAppl.setCaseNo(titaVo.getParam("CaseNo"));
		txAmlRatingAppl.setAcctId(titaVo.getParam("AcctId"));
		txAmlRatingAppl.setInsurCount(parse.stringToInteger(titaVo.getParam("InsurCount")));
		txAmlRatingAppl.setBirthEstDt(titaVo.getParam("BirthEstDt"));
		txAmlRatingAppl.setSourceId(titaVo.getParam("SourceId"));
		txAmlRatingAppl.setModifyDate(titaVo.getParam("ModifyDate"));
		txAmlRatingAppl.setOcupCd(titaVo.getParam("OcupCd"));
		txAmlRatingAppl.setOrgType(titaVo.getParam("OrgType"));
		txAmlRatingAppl.setBcode(titaVo.getParam("Bcode"));
		txAmlRatingAppl.setOcupNote(titaVo.getParam("OcupNote"));
		txAmlRatingAppl.setPayMethod(titaVo.getParam("PayMethod"));
		txAmlRatingAppl.setPayType(titaVo.getParam("PayType"));
		txAmlRatingAppl.setChannel(titaVo.getParam("Channel"));
		txAmlRatingAppl.setPolicyType(titaVo.getParam("PolicyType"));
		txAmlRatingAppl.setInsuranceCurrency(titaVo.getParam("InsuranceCurrency"));
		txAmlRatingAppl.setInsuranceAmount(parse.stringToBigDecimal(titaVo.getParam("InsuranceAmount")));
		txAmlRatingAppl.setAddnCd(titaVo.getParam("AddnCd"));
		txAmlRatingAppl.setInsrStakesCd(titaVo.getParam("InsrStakesCd"));
		txAmlRatingAppl.setBnfcryNHdrGrpCd(titaVo.getParam("BnfcryNHdrGrpCd"));
		txAmlRatingAppl.setAgentTradeCd(titaVo.getParam("AgentTradeCd"));
		txAmlRatingAppl.setFstPayerNHdrGrpCd(titaVo.getParam("FstPayerNHdrGrpCd"));
		txAmlRatingAppl.setFstPayerNHdrStksCd(titaVo.getParam("FstPayerNHdrStksCd"));
		txAmlRatingAppl.setFstPrmOvrseaAcctCd(titaVo.getParam("FstPrmOvrseaAcctCd"));
		txAmlRatingAppl.setTotalAmtCd(parse.stringToBigDecimal(titaVo.getParam("TotalAmtCd")));
		txAmlRatingAppl.setDeclinatureCd(titaVo.getParam("DeclinatureCd"));
		txAmlRatingAppl.setFstInsuredCd(titaVo.getParam("FstInsuredCd"));
		txAmlRatingAppl.setTWAddrHoldCd(titaVo.getParam("TWAddrHoldCd"));
		txAmlRatingAppl.setTWAddrLegalCd(titaVo.getParam("TWAddrLegalCd"));
		txAmlRatingAppl.setDurationCd(titaVo.getParam("DurationCd"));
		txAmlRatingAppl.setSpecialIdentity(titaVo.getParam("SpecialIdentity"));
		txAmlRatingAppl.setLawForceWarranty(titaVo.getParam("LawForceWarranty"));
		txAmlRatingAppl.setMovableGrnteeCd(titaVo.getParam("MovableGrnteeCd"));
		txAmlRatingAppl.setBearerScursGrnteeCd(titaVo.getParam("BearerScursGrnteeCd"));
		txAmlRatingAppl.setAgreeDefaultFineCd(titaVo.getParam("AgreeDefaultFineCd"));
		txAmlRatingAppl.setNonBuyingRealEstateCd(titaVo.getParam("NonBuyingRealEstateCd"));
		txAmlRatingAppl.setNonStkHolderGrnteeCd(titaVo.getParam("NonStkHolderGrnteeCd"));
		txAmlRatingAppl.setReachCase(parse.stringToInteger(titaVo.getParam("ReachCase")));
		txAmlRatingAppl.setAccountTypeCd(titaVo.getParam("AccountTypeCd"));
		txAmlRatingAppl.setQueryType(titaVo.getParam("QueryType"));
		txAmlRatingAppl.setIdentityCd(titaVo.getParam("IdentityCd"));
		txAmlRatingAppl.setRspRequestId(titaVo.getParam("RspRequestId"));
		txAmlRatingAppl.setRspStatus(titaVo.getParam("RspStatus"));
		txAmlRatingAppl.setRspStatusCode(titaVo.getParam("RspStatusCode"));
		txAmlRatingAppl.setRspStatusDesc(titaVo.getParam("RspStatusDesc"));
		txAmlRatingAppl.setRspUnit(titaVo.getParam("RspUnit"));
		txAmlRatingAppl.setRspTransactionId(titaVo.getParam("RspTransactionId"));
		txAmlRatingAppl.setRspAcctNo(titaVo.getParam("RspAcctNo"));
		txAmlRatingAppl.setRspCaseNo(titaVo.getParam("RspCaseNo"));
		txAmlRatingAppl.setRspInsurCount(parse.stringToInteger(titaVo.getParam("RspInsurCount")));
		txAmlRatingAppl.setRspTotalRatingsScore(parse.stringToBigDecimal(titaVo.getParam("RspTotalRatingsScore")));
		txAmlRatingAppl.setRspTotalRatings(titaVo.getParam("RspTotalRatings"));
		try {
			txAmlRatingApplService.insert(txAmlRatingAppl, titaVo);
		} catch (DBException e) {
			throw new LogicException("E0005", "Eloan評級案件申請留存檔" + e.getErrorMsg());
		}
	}

	private void updTxAmlRatingAppl() throws LogicException {
		this.info("updTxAmlRatingAppl ...");
		TxAmlRatingAppl txAmlRatingAppl = txAmlRatingApplService.holdById(iLogNo, titaVo);
		// 變更前
		TxAmlRatingAppl beforeTxAmlRatingAppl = (TxAmlRatingAppl) iDataLog.clone(txAmlRatingAppl);
		if (txAmlRatingAppl != null) {
			txAmlRatingAppl.setUnit(titaVo.getParam("Unit"));
			txAmlRatingAppl.setAcceptanceUnit(titaVo.getParam("AcceptanceUnit"));
			txAmlRatingAppl.setRoleId(titaVo.getParam("RoleId"));
			txAmlRatingAppl.setTransactionId(titaVo.getParam("TransactionId"));
			txAmlRatingAppl.setAcctNo(titaVo.getParam("AcctNo"));
			txAmlRatingAppl.setCaseNo(titaVo.getParam("CaseNo"));
			txAmlRatingAppl.setAcctId(titaVo.getParam("AcctId"));
			txAmlRatingAppl.setInsurCount(parse.stringToInteger(titaVo.getParam("InsurCount")));
			txAmlRatingAppl.setBirthEstDt(titaVo.getParam("BirthEstDt"));
			txAmlRatingAppl.setSourceId(titaVo.getParam("SourceId"));
			txAmlRatingAppl.setModifyDate(titaVo.getParam("ModifyDate"));
			txAmlRatingAppl.setOcupCd(titaVo.getParam("OcupCd"));
			txAmlRatingAppl.setOrgType(titaVo.getParam("OrgType"));
			txAmlRatingAppl.setBcode(titaVo.getParam("Bcode"));
			txAmlRatingAppl.setOcupNote(titaVo.getParam("OcupNote"));
			txAmlRatingAppl.setPayMethod(titaVo.getParam("PayMethod"));
			txAmlRatingAppl.setPayType(titaVo.getParam("PayType"));
			txAmlRatingAppl.setChannel(titaVo.getParam("Channel"));
			txAmlRatingAppl.setPolicyType(titaVo.getParam("PolicyType"));
			txAmlRatingAppl.setInsuranceCurrency(titaVo.getParam("InsuranceCurrency"));
			txAmlRatingAppl.setInsuranceAmount(parse.stringToBigDecimal(titaVo.getParam("InsuranceAmount")));
			txAmlRatingAppl.setAddnCd(titaVo.getParam("AddnCd"));
			txAmlRatingAppl.setInsrStakesCd(titaVo.getParam("InsrStakesCd"));
			txAmlRatingAppl.setBnfcryNHdrGrpCd(titaVo.getParam("BnfcryNHdrGrpCd"));
			txAmlRatingAppl.setAgentTradeCd(titaVo.getParam("AgentTradeCd"));
			txAmlRatingAppl.setFstPayerNHdrGrpCd(titaVo.getParam("FstPayerNHdrGrpCd"));
			txAmlRatingAppl.setFstPayerNHdrStksCd(titaVo.getParam("FstPayerNHdrStksCd"));
			txAmlRatingAppl.setFstPrmOvrseaAcctCd(titaVo.getParam("FstPrmOvrseaAcctCd"));
			txAmlRatingAppl.setTotalAmtCd(parse.stringToBigDecimal(titaVo.getParam("TotalAmtCd")));
			txAmlRatingAppl.setDeclinatureCd(titaVo.getParam("DeclinatureCd"));
			txAmlRatingAppl.setFstInsuredCd(titaVo.getParam("FstInsuredCd"));
			txAmlRatingAppl.setTWAddrHoldCd(titaVo.getParam("TWAddrHoldCd"));
			txAmlRatingAppl.setTWAddrLegalCd(titaVo.getParam("TWAddrLegalCd"));
			txAmlRatingAppl.setDurationCd(titaVo.getParam("DurationCd"));
			txAmlRatingAppl.setSpecialIdentity(titaVo.getParam("SpecialIdentity"));
			txAmlRatingAppl.setLawForceWarranty(titaVo.getParam("LawForceWarranty"));
			txAmlRatingAppl.setMovableGrnteeCd(titaVo.getParam("MovableGrnteeCd"));
			txAmlRatingAppl.setBearerScursGrnteeCd(titaVo.getParam("BearerScursGrnteeCd"));
			txAmlRatingAppl.setAgreeDefaultFineCd(titaVo.getParam("AgreeDefaultFineCd"));
			txAmlRatingAppl.setNonBuyingRealEstateCd(titaVo.getParam("NonBuyingRealEstateCd"));
			txAmlRatingAppl.setNonStkHolderGrnteeCd(titaVo.getParam("NonStkHolderGrnteeCd"));
			txAmlRatingAppl.setReachCase(parse.stringToInteger(titaVo.getParam("ReachCase")));
			txAmlRatingAppl.setAccountTypeCd(titaVo.getParam("AccountTypeCd"));
			txAmlRatingAppl.setQueryType(titaVo.getParam("QueryType"));
			txAmlRatingAppl.setIdentityCd(titaVo.getParam("IdentityCd"));
			txAmlRatingAppl.setRspRequestId(titaVo.getParam("RspRequestId"));
			txAmlRatingAppl.setRspStatus(titaVo.getParam("RspStatus"));
			txAmlRatingAppl.setRspStatusCode(titaVo.getParam("RspStatusCode"));
			txAmlRatingAppl.setRspStatusDesc(titaVo.getParam("RspStatusDesc"));
			txAmlRatingAppl.setRspUnit(titaVo.getParam("RspUnit"));
			txAmlRatingAppl.setRspTransactionId(titaVo.getParam("RspTransactionId"));
			txAmlRatingAppl.setRspAcctNo(titaVo.getParam("RspAcctNo"));
			txAmlRatingAppl.setRspCaseNo(titaVo.getParam("RspCaseNo"));
			txAmlRatingAppl.setRspInsurCount(parse.stringToInteger(titaVo.getParam("RspInsurCount")));
			txAmlRatingAppl.setRspTotalRatingsScore(parse.stringToBigDecimal(titaVo.getParam("RspTotalRatingsScore")));
			txAmlRatingAppl.setRspTotalRatings(titaVo.getParam("RspTotalRatings"));
			try {
				txAmlRatingAppl = txAmlRatingApplService.update2(txAmlRatingAppl, titaVo);
			} catch (DBException e) {
				throw new LogicException("E0007", "Eloan評級案件申請留存檔" + e.getErrorMsg()); // E0007 更新資料時，發生錯誤
			}
			// 紀錄變更前變更後
			iDataLog.setEnv(titaVo, beforeTxAmlRatingAppl, txAmlRatingAppl);
			iDataLog.exec("修改Eloan評級案件申請留存檔");
		} else {
			throw new LogicException("E0003", "Eloan評級案件申請留存檔"); // E0003 修改資料不存在
		}
	}

	private void deleteTxAmlRatingAppl() throws LogicException {
		this.info("deleteTxAmlRatingAppl ...");
		TxAmlRatingAppl txAmlRatingAppl = txAmlRatingApplService.holdById(iLogNo, titaVo);

		if (txAmlRatingAppl != null) {
			// 刪除須刷主管卡
			if (titaVo.getEmpNos().trim().isEmpty()) {
				sendRsp.addvReason(this.txBuffer, titaVo, "0004", ""); // 交易需主管核可
			}
			try {
				txAmlRatingApplService.delete(txAmlRatingAppl, titaVo);
			} catch (DBException e) {
				throw new LogicException("E0008", "Eloan評級案件申請留存檔" + e.getErrorMsg()); // E0008 刪除資料時，發生錯誤
			}
			// 紀錄變更前變更後
			iDataLog.setEnv(titaVo, txAmlRatingAppl, txAmlRatingAppl);
			iDataLog.exec("刪除Eloan評級案件申請留存檔");
		} else {
			throw new LogicException("E0004", "Eloan評級案件申請留存檔"); // E0004 刪除資料不存在
		}
	}
}