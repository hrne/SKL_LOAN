package com.st1.itx.trade.L3;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;

import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.dataVO.TotaVo;
import com.st1.itx.db.domain.FacMain;
import com.st1.itx.db.domain.FacMainId;
import com.st1.itx.db.domain.LoanBorMain;
import com.st1.itx.db.domain.LoanBorMainId;
import com.st1.itx.db.service.FacMainService;
import com.st1.itx.db.service.LoanBorMainService;
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.parse.Parse;

/*
 * Tita
 * RimTxCode=X,5
 * RimFKey=9,1 0:登錄,1:更正,2:放行,3:審核/在途登錄,5:更正重登,6:在途設定,7修改,9檢視journal
 * RimFuncCode=X,1 1:新增 2:修改 3:拷貝 4:刪除 5:查詢
 * RimCustNo=9,7
 * RimFacmNo=9,3
 * RimBormNoNo=9,7
 */
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
		// 查詢額度主檔
		FacMain tFacMain = sFacMainService.findById(new FacMainId(iCustNo, iFacmNo), titaVo);

		// 查詢放款主檔
		if (iBormNo == 0) {
			Slice<LoanBorMain> lLoanBorMain = loanBorMainService.bormCustNoEq(iCustNo, iFacmNo, iFacmNo, 1, 900, this.index, this.limit, titaVo);
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
				throw new LogicException(titaVo, "E0001", "L3R02 放款主檔 戶號 = " + iCustNo + " 額度編號 =  " + iFacmNo + " 預約序號 = " + iBormNo); // 查詢資料不存在
			} else {
				throw new LogicException(titaVo, "E0001", "L3R02 放款主檔 戶號 = " + iCustNo + " 額度編號 =  " + iFacmNo + " 撥款序號 = " + iBormNo); // 查詢資料不存在
			}
		}
		if (tLoanBorMain.getActFg() == 1 && iFKey == 0) {
			throw new LogicException(titaVo, "E0021", "L3R02 放款主檔 戶號 = " + iCustNo + " 額度編號 =  " + iFacmNo + " 撥款序號 = " + iBormNo); // 該筆資料待放行中
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
					throw new LogicException(titaVo, "E0011", "L3R02 放款主檔 戶號 = " + iCustNo + " 額度編號 =  " + iFacmNo + " 預約序號 = " + iBormNo); // 該筆資料已被刪除
				}
				if (tLoanBorMain.getStatus() == 98) {
					throw new LogicException(titaVo, "E3054", "L3R02 放款主檔 戶號 = " + iCustNo + " 額度編號 =  " + iFacmNo + " 預約序號 = " + iBormNo); // 該筆預約撥款資料已撥款
				}
				if (tLoanBorMain.getStatus() != 99) {
					throw new LogicException(titaVo, "E3055", "L3R02 放款主檔 戶號 = " + iCustNo + " 額度編號 =  " + iFacmNo + " 預約序號 = " + iBormNo); // 該筆資料非預約撥款
				}
			}
			break;
		case "L3711": // 應繳日變更-不可欠繳
		case "L3712": // 應繳日變更-可欠繳
		case "L3923": // 應繳日試算
			if ("2".equals(tFacMain.getAmortizedCode())) {
				throw new LogicException(titaVo, "E0010", "L3R02攤還方式為到期取息時,不可做應繳日試算"); // 攤還方式為2時不可做應繳日試算
			}
			if ("3".equals(tFacMain.getFreqBase())) {
				throw new LogicException(titaVo, "E0010", "L3R02週期基準為週時,不可做應繳日試算"); // 週期基準為週時不可做應繳日試算
			}

			if (tLoanBorMain.getStatus() != 0) {
				throw new LogicException(titaVo, "E3063", "L3R02"); // 該筆放款戶況非正常戶
			}
			if (tLoanBorMain.getNextPayIntDate() <= this.txBuffer.getTxCom().getTbsdy()) {
				throw new LogicException(titaVo, "E3062", "L3R02 應繳息日 = " + tLoanBorMain.getNextPayIntDate()); // 有1期(含)以上期款未繳,不可變更繳款日
			}
			break;
		case "L3926": // 變更期款試算
			if (tLoanBorMain.getStatus() != 0) {
				throw new LogicException(titaVo, "E3063", "L3R02"); // 該筆放款戶況非正常戶
			}
			if (!(tLoanBorMain.getAmortizedCode().equals("3") || tLoanBorMain.getAmortizedCode().equals("4"))) {
				throw new LogicException(titaVo, "E3066", "L3R02 攤還方式 = " + tLoanBorMain.getAmortizedCode()); // 該筆放款攤還方式非本息平均法及本金平均法
			}
			break;
		}
		this.totaVo.putParam("OCustNo", tLoanBorMain.getCustNo());
		this.totaVo.putParam("OFacmNo", tLoanBorMain.getFacmNo());
		this.totaVo.putParam("OBormNo", tLoanBorMain.getBormNo());
		this.totaVo.putParam("OStatus", tLoanBorMain.getStatus());
		this.totaVo.putParam("ORateIncr", tLoanBorMain.getRateIncr());
		this.totaVo.putParam("OIndividualIncr", tLoanBorMain.getIndividualIncr());
		this.totaVo.putParam("OApproveRate", tLoanBorMain.getApproveRate());
		this.totaVo.putParam("OStoreRate", tLoanBorMain.getStoreRate());
		this.totaVo.putParam("ORateCode", tLoanBorMain.getRateCode());
		this.totaVo.putParam("ORateAdjFreq", tLoanBorMain.getRateAdjFreq());
		this.totaVo.putParam("ODrawdownCode", tLoanBorMain.getDrawdownCode());
		this.totaVo.putParam("OCurrencyCode", tLoanBorMain.getCurrencyCode());
		this.totaVo.putParam("ODrawdownAmt", tLoanBorMain.getDrawdownAmt());
		this.totaVo.putParam("OLoanBal", tLoanBorMain.getLoanBal());
		this.totaVo.putParam("ODrawdownDate", tLoanBorMain.getDrawdownDate());
		this.totaVo.putParam("OLoanTermYy", tLoanBorMain.getLoanTermYy());
		this.totaVo.putParam("OLoanTermMm", tLoanBorMain.getLoanTermMm());
		this.totaVo.putParam("OLoanTermDd", tLoanBorMain.getLoanTermDd());
		this.totaVo.putParam("OMaturityDate", tLoanBorMain.getMaturityDate());
		this.totaVo.putParam("OAmortizedCode", tLoanBorMain.getAmortizedCode());
		this.totaVo.putParam("OFreqBase", tLoanBorMain.getFreqBase());
		this.totaVo.putParam("OPayIntFreq", tLoanBorMain.getPayIntFreq());
		this.totaVo.putParam("ORepayFreq", tLoanBorMain.getRepayFreq());
		this.totaVo.putParam("OTotalPeriod", tLoanBorMain.getTotalPeriod());
		this.totaVo.putParam("ORepaidPeriod", tLoanBorMain.getRepaidPeriod());
		this.totaVo.putParam("OPaidTerms", tLoanBorMain.getPaidTerms());
		this.totaVo.putParam("OPrevPayIntDate", wkPrevPayIntDate);
		this.totaVo.putParam("OPrevRepaidDate", wkPrevRepaidDate);
		this.totaVo.putParam("ONextPayIntDate", tLoanBorMain.getNextPayIntDate());
		this.totaVo.putParam("ONextRepayDate", tLoanBorMain.getNextRepayDate());
		this.totaVo.putParam("ODueAmt", tLoanBorMain.getDueAmt());
		this.totaVo.putParam("OGracePeriod", tLoanBorMain.getGracePeriod());
		this.totaVo.putParam("OGraceDate", tLoanBorMain.getGraceDate());
		this.totaVo.putParam("OSpecificDd", tLoanBorMain.getSpecificDd());
		this.totaVo.putParam("OSpecificDate", tLoanBorMain.getSpecificDate());
		this.totaVo.putParam("OFirstDueDate", tLoanBorMain.getFirstDueDate());
		this.totaVo.putParam("OFirstRateAdjDate", tLoanBorMain.getFirstAdjRateDate());
		this.totaVo.putParam("ONextRateAdjDate", tLoanBorMain.getNextAdjRateDate());
		this.totaVo.putParam("OAcctFee", tLoanBorMain.getAcctFee());
		this.totaVo.putParam("OFinalBal", tLoanBorMain.getFinalBal());
		this.totaVo.putParam("ONotYetFlag", tLoanBorMain.getNotYetFlag());
		this.totaVo.putParam("ORenewFlag", tLoanBorMain.getRenewFlag());
		this.totaVo.putParam("OPieceCode", tLoanBorMain.getPieceCode());
		this.totaVo.putParam("OPieceCodeSecond", tLoanBorMain.getPieceCodeSecond());
		this.totaVo.putParam("OPieceCodeSecondAmt", tLoanBorMain.getPieceCodeSecondAmt());
		this.totaVo.putParam("OUsageCode", tLoanBorMain.getUsageCode());
		this.totaVo.putParam("OSyndNo", tLoanBorMain.getSyndNo());
		this.totaVo.putParam("ORelationCode", tLoanBorMain.getRelationCode());
		this.totaVo.putParam("ORelationName", tLoanBorMain.getRelationName());
		this.totaVo.putParam("ORelationId", tLoanBorMain.getRelationId());
		this.totaVo.putParam("ORelationBirthday", tLoanBorMain.getRelationBirthday());
		this.totaVo.putParam("ORelationGender", tLoanBorMain.getRelationGender());
		this.totaVo.putParam("OActFg", tLoanBorMain.getActFg());
		this.totaVo.putParam("OLastEntDy", tLoanBorMain.getLastEntDy());
		this.totaVo.putParam("OLastKinbr", tLoanBorMain.getLastKinbr());
		this.totaVo.putParam("OLastTlrNo", tLoanBorMain.getLastTlrNo());
		this.totaVo.putParam("OLastTxtNo", tLoanBorMain.getLastTxtNo());
		this.totaVo.putParam("ORemitBank", tLoanBorMain.getRemitBank());
		this.totaVo.putParam("ORemitBranch", tLoanBorMain.getRemitBranch());
		this.totaVo.putParam("ORemitAcctNo", tLoanBorMain.getRemitAcctNo());
		this.totaVo.putParam("OCompensateAcct", tLoanBorMain.getCompensateAcct());
		this.totaVo.putParam("OPaymentBank", tLoanBorMain.getPaymentBank());
		this.totaVo.putParam("ORemark", tLoanBorMain.getRemark());
		this.totaVo.putParam("OAcDate", tLoanBorMain.getAcDate());
		this.totaVo.putParam("ONextAcDate", tLoanBorMain.getNextAcDate());
		this.totaVo.putParam("OIntCalcCode", tLoanBorMain.getIntCalcCode());

		this.addList(this.totaVo);
		return this.sendList();
	}

}