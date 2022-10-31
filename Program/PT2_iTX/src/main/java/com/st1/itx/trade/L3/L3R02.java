package com.st1.itx.trade.L3;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;

import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.dataVO.TotaVo;
import com.st1.itx.db.domain.FacCaseAppl;
import com.st1.itx.db.domain.FacMain;
import com.st1.itx.db.domain.FacMainId;
import com.st1.itx.db.domain.LoanBorMain;
import com.st1.itx.db.domain.LoanBorMainId;
import com.st1.itx.db.domain.LoanRateChange;
import com.st1.itx.db.service.FacCaseApplService;
import com.st1.itx.db.service.FacMainService;
import com.st1.itx.db.service.LoanBorMainService;
import com.st1.itx.db.service.LoanRateChangeService;
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.parse.Parse;

/**
 * L3R02 查尋放款主檔
 * 
 * @author iza
 * @version 1.0.0
 */
@Service("L3R02")
@Scope("prototype")
public class L3R02 extends TradeBuffer {

	/* DB服務注入 */
	@Autowired
	public LoanBorMainService loanBorMainService;

	/* DB服務注入 */
	@Autowired
	public FacMainService sFacMainService;

	@Autowired
	public LoanRateChangeService loanRateChangeService;
	@Autowired
	public FacCaseApplService facCaseApplService;

	@Autowired
	Parse parse;

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active L3R02 ");
		this.totaVo.init(titaVo);

		// 取得輸入資料
		String iTxCode = titaVo.getParam("RimTxCode");
		String iFuncCode = titaVo.getParam("RimFuncCode");
		int iFKey = this.parse.stringToInteger(titaVo.getParam("RimFKey"));
		int iCustNo = this.parse.stringToInteger(titaVo.getParam("RimCustNo"));
		int iFacmNo = this.parse.stringToInteger(titaVo.getParam("RimFacmNo"));
		int iBormNo = this.parse.stringToInteger(titaVo.getParam("RimBormNo"));

		// work area
		int wkPrevPayIntDate = 0;
		int wkPrevRepaidDate = 0;
		LoanRateChange tLoanRateChange = new LoanRateChange();
		// 查詢額度主檔
		FacMain tFacMain = sFacMainService.findById(new FacMainId(iCustNo, iFacmNo), titaVo);
		if (tFacMain == null) {
			throw new LogicException(titaVo, "E0001", " 額度主檔 借款人戶號 = " + iCustNo + " 額度編號 = " + iFacmNo); // 查詢資料不存在
		}

		FacCaseAppl tFacCaseAppl = facCaseApplService.findById(tFacMain.getApplNo(), titaVo);
		if (tFacCaseAppl == null) {
			throw new LogicException(titaVo, "E0001", " 案件申請檔"); // 查無資料
		}
		// 查詢放款主檔
		if (iBormNo == 0) {
			Slice<LoanBorMain> lLoanBorMain = loanBorMainService.bormCustNoEq(iCustNo, iFacmNo, iFacmNo, 1, 900,
					this.index, this.limit, titaVo);
			if (lLoanBorMain != null) {
				for (LoanBorMain ln : lLoanBorMain.getContent()) {
					if (ln.getStatus() == 0 || ln.getStatus() == 4) { // 0: 正常戶 4: 逾期戶
						iBormNo = ln.getBormNo();
						break;
					}
				}
			}
		}
		LoanBorMain tLoanBorMain = loanBorMainService.findById(new LoanBorMainId(iCustNo, iFacmNo, iBormNo), titaVo);
		if (tLoanBorMain == null) {
			if (iBormNo > 900) {
				throw new LogicException(titaVo, "E0001",
						" 放款主檔 戶號 = " + iCustNo + " 額度編號 =  " + iFacmNo + " 預約序號 = " + iBormNo); // 查詢資料不存在
			} else {
				throw new LogicException(titaVo, "E0001",
						" 放款主檔 戶號 = " + iCustNo + " 額度編號 =  " + iFacmNo + " 撥款序號 = " + iBormNo); // 查詢資料不存在
			}
		}
		if (!"L3721".equals(titaVo.getTxcd()) && tLoanBorMain.getActFg() == 1 && iFKey == 0) {
			throw new LogicException(titaVo, "E0021",
					" 放款主檔 戶號 = " + iCustNo + " 額度編號 =  " + iFacmNo + " 撥款序號 = " + iBormNo); // 該筆資料待放行中
		}
		if (iBormNo <= 900) {
			tLoanRateChange = loanRateChangeService.rateChangeEffectDateDescFirst(iCustNo, iFacmNo, iBormNo,
					titaVo.getEntDyI() + 19110000, titaVo);
			if (tLoanRateChange == null) {
				throw new LogicException(titaVo, "E0001",
						"放款利率變動檔  借款人戶號 = " + iCustNo + "-" + iFacmNo + "-" + iBormNo); // 查詢資料不存在
			}

		}

		wkPrevPayIntDate = tLoanBorMain.getPrevPayIntDate();
		wkPrevRepaidDate = tLoanBorMain.getPrevRepaidDate();
		if (wkPrevPayIntDate == 0) {
			wkPrevPayIntDate = tLoanBorMain.getDrawdownDate();
		}
		if (wkPrevRepaidDate == 0) {
			wkPrevRepaidDate = tLoanBorMain.getDrawdownDate();
		}
		switch (iTxCode) {
		case "L3120": // 預約撥款刪除
			if (iFuncCode.equals("4")) {
				if (tLoanBorMain.getStatus() == 97) {
					throw new LogicException(titaVo, "E0011",
							" 放款主檔 戶號 = " + iCustNo + " 額度編號 =  " + iFacmNo + " 預約序號 = " + iBormNo); // 該筆資料已被刪除
				}
				if (tLoanBorMain.getStatus() == 98) {
					throw new LogicException(titaVo, "E3054",
							" 放款主檔 戶號 = " + iCustNo + " 額度編號 =  " + iFacmNo + " 預約序號 = " + iBormNo); // 該筆預約撥款資料已撥款
				}
				if (tLoanBorMain.getStatus() != 99) {
					throw new LogicException(titaVo, "E3055",
							" 放款主檔 戶號 = " + iCustNo + " 額度編號 =  " + iFacmNo + " 預約序號 = " + iBormNo); // 該筆資料非預約撥款
				}
			}
			break;
		case "L3711": // 應繳日變更-不可欠繳
		case "L3712": // 應繳日變更-可欠繳
		case "L3923": // 應繳日試算
			if ("2".equals(tFacMain.getAmortizedCode())) {
				throw new LogicException(titaVo, "E0010", "攤還方式為到期取息時,不可做應繳日試算"); // 攤還方式為2時不可做應繳日試算
			}
			if ("3".equals(tFacMain.getFreqBase())) {
				throw new LogicException(titaVo, "E0010", "週期基準為週時,不可做應繳日試算"); // 週期基準為週時不可做應繳日試算
			}

			if (tLoanBorMain.getStatus() != 0) {
				throw new LogicException(titaVo, "E3063", ""); // 該筆放款戶況非正常戶
			}
			if (tLoanBorMain.getNextPayIntDate() <= this.txBuffer.getTxCom().getTbsdy()) {
				throw new LogicException(titaVo, "E3062", " 應繳息日 = " + tLoanBorMain.getNextPayIntDate()); // 有1期(含)以上期款未繳,不可變更繳款日
			}
			break;
		case "L3926": // 變更期款試算
			if (tLoanBorMain.getStatus() != 0) {
				throw new LogicException(titaVo, "E3063", ""); // 該筆放款戶況非正常戶
			}
			if (!(tLoanBorMain.getAmortizedCode().equals("3") || tLoanBorMain.getAmortizedCode().equals("4"))) {
				throw new LogicException(titaVo, "E3066", " 攤還方式 = " + tLoanBorMain.getAmortizedCode()); // 該筆放款攤還方式非本息平均法及本金平均法
			}
			break;
		}
		this.totaVo.putParam("L3r02CustNo", tLoanBorMain.getCustNo());
		this.totaVo.putParam("L3r02FacmNo", tLoanBorMain.getFacmNo());
		this.totaVo.putParam("L3r02BormNo", tLoanBorMain.getBormNo());
		this.totaVo.putParam("L3r02Status", tLoanBorMain.getStatus());
		this.totaVo.putParam("L3r02RateIncr", tLoanBorMain.getRateIncr());
		this.totaVo.putParam("L3r02IndividualIncr", tLoanBorMain.getIndividualIncr());
		this.totaVo.putParam("L3r02ApproveRate", tLoanBorMain.getApproveRate());
		if (iBormNo <= 900) {
			this.totaVo.putParam("L3r02StoreRate", tLoanRateChange.getFitRate());
		} else {
			this.totaVo.putParam("L3r02StoreRate", tLoanBorMain.getStoreRate());
		}
		this.totaVo.putParam("L3r02RateCode", tLoanBorMain.getRateCode());
		this.totaVo.putParam("L3r02RateAdjFreq", tLoanBorMain.getRateAdjFreq());
		this.totaVo.putParam("L3r02DrawdownCode", tLoanBorMain.getDrawdownCode());
		this.totaVo.putParam("L3r02CurrencyCode", tLoanBorMain.getCurrencyCode());
		this.totaVo.putParam("L3r02DrawdownAmt", tLoanBorMain.getDrawdownAmt());
		this.totaVo.putParam("L3r02LoanBal", tLoanBorMain.getLoanBal());
		this.totaVo.putParam("L3r02DrawdownDate", tLoanBorMain.getDrawdownDate());
		this.totaVo.putParam("L3r02LoanTermYy", tLoanBorMain.getLoanTermYy());
		this.totaVo.putParam("L3r02LoanTermMm", tLoanBorMain.getLoanTermMm());
		this.totaVo.putParam("L3r02LoanTermDd", tLoanBorMain.getLoanTermDd());
		this.totaVo.putParam("L3r02MaturityDate", tLoanBorMain.getMaturityDate());
		this.totaVo.putParam("L3r02AmortizedCode", tLoanBorMain.getAmortizedCode());
		this.totaVo.putParam("L3r02FreqBase", tLoanBorMain.getFreqBase());
		this.totaVo.putParam("L3r02PayIntFreq", tLoanBorMain.getPayIntFreq());
		this.totaVo.putParam("L3r02RepayFreq", tLoanBorMain.getRepayFreq());
		this.totaVo.putParam("L3r02TotalPeriod", tLoanBorMain.getTotalPeriod());
		this.totaVo.putParam("L3r02RepaidPeriod", tLoanBorMain.getRepaidPeriod());
		this.totaVo.putParam("L3r02PaidTerms", tLoanBorMain.getPaidTerms());
		this.totaVo.putParam("L3r02PrevPayIntDate", wkPrevPayIntDate);
		this.totaVo.putParam("L3r02PrevRepaidDate", wkPrevRepaidDate);
		this.totaVo.putParam("L3r02NextPayIntDate", tLoanBorMain.getNextPayIntDate());
		this.totaVo.putParam("L3r02NextRepayDate", tLoanBorMain.getNextRepayDate());
		this.totaVo.putParam("L3r02DueAmt", tLoanBorMain.getDueAmt());
		this.totaVo.putParam("L3r02GracePeriod", tLoanBorMain.getGracePeriod());
		this.totaVo.putParam("L3r02GraceDate", tLoanBorMain.getGraceDate());
		this.totaVo.putParam("L3r02SpecificDd", tLoanBorMain.getSpecificDd());
		this.totaVo.putParam("L3r02SpecificDate", tLoanBorMain.getSpecificDate());
		this.totaVo.putParam("L3r02FirstDueDate", tLoanBorMain.getFirstDueDate());
		this.totaVo.putParam("L3r02FirstRateAdjDate", tLoanBorMain.getFirstAdjRateDate());
		this.totaVo.putParam("L3r02NextRateAdjDate", tLoanBorMain.getNextAdjRateDate());
		this.totaVo.putParam("L3r02AcctFee", tLoanBorMain.getAcctFee());
		this.totaVo.putParam("L3r02FinalBal", tLoanBorMain.getFinalBal());
		this.totaVo.putParam("L3r02NotYetFlag", tLoanBorMain.getNotYetFlag());
		this.totaVo.putParam("L3r02RenewFlag", tLoanBorMain.getRenewFlag());
		this.totaVo.putParam("L3r02PieceCode", tLoanBorMain.getPieceCode());
		this.totaVo.putParam("L3r02PieceCodeSecond", tLoanBorMain.getPieceCodeSecond());
		this.totaVo.putParam("L3r02PieceCodeSecondAmt", tLoanBorMain.getPieceCodeSecondAmt());
		this.totaVo.putParam("L3r02UsageCode", tLoanBorMain.getUsageCode());
		this.totaVo.putParam("L3r02SyndNo", tFacCaseAppl.getSyndNo());
		this.totaVo.putParam("L3r02RelationCode", tLoanBorMain.getRelationCode());
		this.totaVo.putParam("L3r02RelationName", tLoanBorMain.getRelationName());
		this.totaVo.putParam("L3r02RelationId", tLoanBorMain.getRelationId());
		this.totaVo.putParam("L3r02RelationBirthday", tLoanBorMain.getRelationBirthday());
		this.totaVo.putParam("L3r02RelationGender", tLoanBorMain.getRelationGender());
		this.totaVo.putParam("L3r02ActFg", tLoanBorMain.getActFg());
		this.totaVo.putParam("L3r02LastEntDy", tLoanBorMain.getLastEntDy());
		this.totaVo.putParam("L3r02LastKinbr", tLoanBorMain.getLastKinbr());
		this.totaVo.putParam("L3r02LastTlrNo", tLoanBorMain.getLastTlrNo());
		this.totaVo.putParam("L3r02LastTxtNo", tLoanBorMain.getLastTxtNo());
		this.totaVo.putParam("L3r02RemitBank", tLoanBorMain.getRemitBank());
		this.totaVo.putParam("L3r02RemitBranch", tLoanBorMain.getRemitBranch());
		this.totaVo.putParam("L3r02RemitAcctNo", tLoanBorMain.getRemitAcctNo());
		this.totaVo.putParam("L3r02CompensateAcct", tLoanBorMain.getCompensateAcct());
		this.totaVo.putParam("L3r02PaymentBank", tLoanBorMain.getPaymentBank());
		this.totaVo.putParam("L3r02Remark", tLoanBorMain.getRemark());
		this.totaVo.putParam("L3r02AcDate", tLoanBorMain.getAcDate());
		this.totaVo.putParam("L3r02NextAcDate", tLoanBorMain.getNextAcDate());
		this.totaVo.putParam("L3r02IntCalcCode", tLoanBorMain.getIntCalcCode());
		this.totaVo.putParam("L3r02HandlingFee", tLoanBorMain.getHandlingFee());

		this.addList(this.totaVo);
		return this.sendList();
	}

}