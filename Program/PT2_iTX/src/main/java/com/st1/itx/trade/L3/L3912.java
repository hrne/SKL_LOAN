package com.st1.itx.trade.L3;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.ArrayList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.TempVo;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.dataVO.TotaVo;
import com.st1.itx.db.domain.LoanBorTx;
import com.st1.itx.db.domain.LoanBorTxId;
import com.st1.itx.db.service.LoanBorMainService;
import com.st1.itx.db.service.LoanBorTxService;
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.common.LoanCom;
import com.st1.itx.util.parse.Parse;

/*
 * Tita
 * AcctDate=9,7
 * TellerNo=X,6
 * TxtNo=9,8
 */
/**
 * L3912 交易內容查詢
 * 
 * @author iza
 * @version 1.0.0
 */
@Service("L3912")
@Scope("prototype")
public class L3912 extends TradeBuffer {

	/* DB服務注入 */
	@Autowired
	public LoanBorMainService loanBorMainService;
	@Autowired
	public LoanBorTxService loanBorTxService;

	@Autowired
	Parse parse;
	@Autowired
	LoanCom loanCom;
	private DecimalFormat df = new DecimalFormat("##,###,###,###,##0");

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active L3912 ");
		this.totaVo.init(titaVo);
		loanCom.setTxBuffer(txBuffer);

		// 取得輸入資料
		int iAcctDate = this.parse.stringToInteger(titaVo.getParam("AcctDate"));
		String iTellerNo = titaVo.getParam("TellerNo");
		String iTxtNo = titaVo.getParam("TxtNo");
		int iCustNo = this.parse.stringToInteger(titaVo.getParam("CustNo"));
		int iFacmNo = this.parse.stringToInteger(titaVo.getParam("FacmNo"));
		int iBormNo = this.parse.stringToInteger(titaVo.getParam("BormNo"));
		int iBorxNo = this.parse.stringToInteger(titaVo.get("BorxNo"));

		// work area
		LoanBorTx tLoanBorTx;
		int RPTFG = 8;
		BigDecimal wkReduceAmt = BigDecimal.ZERO;
		if (iBorxNo > 0) {
			tLoanBorTx = loanBorTxService.findById(new LoanBorTxId(iCustNo, iFacmNo, iBormNo, iBorxNo), titaVo);
		} else {
			// 查詢放款交易內容檔
			if (iCustNo == 0) {
				tLoanBorTx = loanBorTxService.borxTxtNoFirst(iAcctDate + 19110000, iTellerNo, iTxtNo, titaVo);
			} else {
				tLoanBorTx = loanBorTxService.custNoTxtNoFirst(iCustNo, iFacmNo, iBormNo, iAcctDate + 19110000,
						iTellerNo, iTxtNo, titaVo);
			}
			if (tLoanBorTx == null) {
				throw new LogicException(titaVo, "E0001", "放款交易內容檔"); // 查詢資料不存在
			}
		}

		TempVo tTempVo = new TempVo();
		tTempVo = tTempVo.getVo(tLoanBorTx.getOtherFields());
		TempVo rvTempVo = new TempVo();
		rvTempVo = tTempVo.getVo(tTempVo.getParam("RvJsonFields"));
		if (tLoanBorTx.getTitaTxCd().equals("L3100")) {
			RPTFG = 2;
		} else if (tLoanBorTx.getTitaTxCd().equals("L3200") || tLoanBorTx.getTitaTxCd().equals("L3240")) {
			RPTFG = 3;
		} else if (tLoanBorTx.getTitaTxCd().equals("L3711")) {
			RPTFG = 4;
		} else if (tLoanBorTx.getTitaTxCd().equals("L3712")) {
			RPTFG = 4;
		} else if (tLoanBorTx.getTitaTxCd().equals("L3721")) {
			RPTFG = 5;
		} else if ("L618C".equals(tLoanBorTx.getTitaTxCd()) || "L618B".equals(tLoanBorTx.getTitaTxCd())) {
			RPTFG = 9;
		} else if (tLoanBorTx.getTitaTxCd().equals("L3410")) {
			RPTFG = 11;
		} else if (tLoanBorTx.getTitaTxCd().equals("L3420")) {
			if (parse.stringToInteger(tTempVo.getParam("CaseCloseCode")) >= 7) {
				RPTFG = 16;
			} else {
				RPTFG = 11;
			}
		} else if (tLoanBorTx.getTitaTxCd().equals("L3440")) {
			RPTFG = 12;
		} else if (tLoanBorTx.getTitaTxCd().equals("L3210")) {
			RPTFG = 13;
		} else if (tLoanBorTx.getTitaTxCd().equals("L3220")) {
			RPTFG = 14;
		} else if (tLoanBorTx.getTitaTxCd().equals("L3230")) {
			RPTFG = 15;
		} else if (tLoanBorTx.getBorxNo() == 1 && tLoanBorTx.getTitaTxCd().equals("L3101")) {
			RPTFG = 2;
		} else if (tLoanBorTx.getTitaTxCd().equals("L3731")) {
			RPTFG = 17;
		} else {
			RPTFG = 8;
		}
//		C(5,#OTxCode,L3100,E(0,@RPTFG,2),$)
//		C(5,#OTxCode,L3200,E(0,@RPTFG,3),$)
//		C(5,#OTxCode,L3711,E(0,@RPTFG,4),$)
//		C(5,#OTxCode,L3712,E(0,@RPTFG,4),$)
//		C(5,#OTxCode,L3721,E(0,@RPTFG,5),$)
//		C(5,#OTxCode,L3600,E(0,@RPTFG,6),$)
//		C(5,#OTxCode,L3610,E(0,@RPTFG,7),$)
//		C(5,#OTxCode,L3410,E(0,@RPTFG,11),$)
//		C(5,#OTxCode,L3420,E(0,@RPTFG,11),$)
//		C(5,#OTxCode,L3440,E(0,@RPTFG,12),$)

//		3240、3200、(同畫面) =3
//		3420、3410 =11
//		交易型態TX.SlipSumNo >0(批次({彙總批號})) ==0(人工)

		BigDecimal shortFall = BigDecimal.ZERO;
		String shortFallX = "";
		shortFall = tLoanBorTx.getUnpaidInterest().add(tLoanBorTx.getUnpaidPrincipal());
		if (tLoanBorTx.getUnpaidInterest().compareTo(BigDecimal.ZERO) > 0) {
			shortFallX = df.format(shortFall) + "（利息）";
		} else if (tLoanBorTx.getUnpaidPrincipal().compareTo(BigDecimal.ZERO) > 0) {
			shortFallX = df.format(shortFall) + "（本金）";
		} else {
			shortFallX = df.format(shortFall);
		}
		this.info("BorxNo     =" + tLoanBorTx.getBorxNo());
		this.info("TitaTxCd   =" + tLoanBorTx.getTitaTxCd());
		this.info("RPTFG      =" + RPTFG);
		// Put Tota
		wkReduceAmt = this.parse.stringToBigDecimal(tTempVo.getParam("ReduceAmt"));

		BigDecimal wkAcctFee = parse.stringToBigDecimal(tTempVo.getParam("AcctFee"));
		BigDecimal wkModifyFee = parse.stringToBigDecimal(tTempVo.getParam("ModifyFee"));
		BigDecimal wkFireFee = parse.stringToBigDecimal(tTempVo.getParam("FireFee"));
		BigDecimal wkLawFee = parse.stringToBigDecimal(tTempVo.getParam("LawFee"));
		BigDecimal wkRepayAmt = tLoanBorTx.getPrincipal().add(tLoanBorTx.getInterest()).add(tLoanBorTx.getDelayInt())
				.add(tLoanBorTx.getBreachAmt()).add(tLoanBorTx.getCloseBreachAmt()).add(wkAcctFee).add(wkModifyFee)
				.add(wkFireFee).add(wkLawFee);

		BigDecimal wkTempRepay = BigDecimal.ZERO; // 暫收抵繳
		if (tLoanBorTx.getTempAmt().compareTo(tLoanBorTx.getOverflow()) > 0) {
			wkTempRepay = tLoanBorTx.getTempAmt().subtract(tLoanBorTx.getOverflow());
		}

		String rvNo = tTempVo.getParam("RvNo");
		if (wkFireFee.compareTo(BigDecimal.ZERO) > 0 && !"".equals(rvTempVo.getParam("InsuYearMonth"))) {
			rvNo = "" + (parse.stringToInteger(rvTempVo.getParam("InsuYearMonth")) - 191100) + " " + rvNo;
		}
		this.totaVo.putParam("ORPTFG", RPTFG); // rptfg
		this.totaVo.putParam("OCustNo", tLoanBorTx.getCustNo());
		this.totaVo.putParam("OFacmNo", tLoanBorTx.getFacmNo());
		this.totaVo.putParam("OBormNo", tLoanBorTx.getBormNo());
		this.totaVo.putParam("OCustNoX", loanCom.getCustNameByNo(tLoanBorTx.getCustNo()));
		this.totaVo.putParam("OTxCode", tLoanBorTx.getTitaTxCd());
		this.totaVo.putParam("OTxCodeX", loanCom.getTxDescCodeX(tLoanBorTx, titaVo));
		this.totaVo.putParam("OAcctDate", tLoanBorTx.getAcDate());
		this.totaVo.putParam("OTellerNo", tLoanBorTx.getTitaTlrNo());
		this.totaVo.putParam("OTellerNoX", loanCom.getEmpFullnameByEmpNo(tLoanBorTx.getTitaTlrNo()));
		this.totaVo.putParam("OTxtNo", tLoanBorTx.getTitaTxtNo());
		this.totaVo.putParam("OHCode", tLoanBorTx.getTitaHCode());
		this.totaVo.putParam("OEntryDate", tLoanBorTx.getEntryDate());
		this.totaVo.putParam("OCurrencyCode", tLoanBorTx.getTitaCurCd());
		if ("1".equals(tLoanBorTx.getTitaHCode()) || "3".equals(tLoanBorTx.getTitaHCode())) {
			this.totaVo.putParam("OTxAmt", BigDecimal.ZERO.subtract(tLoanBorTx.getTxAmt()));
			this.totaVo.putParam("OPrinciPal", BigDecimal.ZERO.subtract(tLoanBorTx.getPrincipal()));
			this.totaVo.putParam("OInterest", BigDecimal.ZERO.subtract(tLoanBorTx.getInterest()));
			this.totaVo.putParam("ODelayInt", BigDecimal.ZERO.subtract(tLoanBorTx.getDelayInt()));
			this.totaVo.putParam("OBreachAmt", BigDecimal.ZERO.subtract(tLoanBorTx.getBreachAmt()));
			this.totaVo.putParam("OTempRepay",
					BigDecimal.ZERO.subtract(wkTempRepay));
			this.totaVo.putParam("ORepayAmt", BigDecimal.ZERO.subtract(wkRepayAmt));
			this.totaVo.putParam("OCloseBreachAmt", BigDecimal.ZERO.subtract(tLoanBorTx.getCloseBreachAmt()));
			this.totaVo.putParam("OExtraRepay", "-" + tLoanBorTx.getExtraRepay());
		} else {
			this.totaVo.putParam("OTxAmt", tLoanBorTx.getTxAmt());
			this.totaVo.putParam("OPrinciPal", tLoanBorTx.getPrincipal());
			this.totaVo.putParam("OInterest", tLoanBorTx.getInterest());
			this.totaVo.putParam("ODelayInt", tLoanBorTx.getDelayInt());
			this.totaVo.putParam("OBreachAmt", tLoanBorTx.getBreachAmt());
			this.totaVo.putParam("OTempAmt", tLoanBorTx.getTempAmt()); // 暫收借
			this.totaVo.putParam("OTempRepay", wkTempRepay);// 暫收款金額
			this.totaVo.putParam("ORepayAmt", wkRepayAmt);
			this.totaVo.putParam("OCloseBreachAmt", tLoanBorTx.getCloseBreachAmt());
			this.totaVo.putParam("OExtraRepay", tLoanBorTx.getExtraRepay());
		}

		this.totaVo.putParam("OOverflow", tLoanBorTx.getOverflow()); // 累溢收
		this.totaVo.putParam("OLoanBal", tLoanBorTx.getLoanBal());
		this.totaVo.putParam("OIntStartDate", tLoanBorTx.getIntStartDate());
		this.totaVo.putParam("OIntEndDate", tLoanBorTx.getIntEndDate());
		this.totaVo.putParam("OUnpaidInterest", tLoanBorTx.getUnpaidInterest());
		this.totaVo.putParam("OUnpaidPrinciPal", tLoanBorTx.getUnpaidPrincipal());
		this.totaVo.putParam("OUnpaidBreach", tLoanBorTx.getUnpaidCloseBreach());
		this.totaVo.putParam("OCaseCloseCode", tTempVo.getParam("CaseCloseCode"));
		this.totaVo.putParam("OChequeAmt", tTempVo.getParam("ChequeAmt"));
		this.totaVo.putParam("ORemitAmt", tTempVo.getParam("RemitAmt"));
		this.totaVo.putParam("ORemitBank", tTempVo.getParam("RemitBank"));
		this.totaVo.putParam("ORemitBankX", loanCom.getBranchItemByBankCode(tTempVo.getParam("RemitBank")));
		this.totaVo.putParam("ORemitAcctNo", tTempVo.getParam("RemitAcctNo"));
		this.totaVo.putParam("OShortFallX", shortFallX);
		this.totaVo.putParam("OSupperNo", tLoanBorTx.getTitaEmpNoS());
		this.totaVo.putParam("OSupperNoX", loanCom.getEmpFullnameByEmpNo(tLoanBorTx.getTitaEmpNoS()));
		this.totaVo.putParam("OReduceAmt", wkReduceAmt);
		this.totaVo.putParam("OTxDate", tLoanBorTx.getTitaCalDy());
		int titaCalTm = tLoanBorTx.getTitaCalTm();
		if (tLoanBorTx.getTitaCalTm() != 0) {
			titaCalTm = titaCalTm / 100;
		}
		this.totaVo.putParam("OTxTime", titaCalTm);
		this.totaVo.putParam("OStampFreeAmt", tTempVo.getParam("StampFreeAmt"));
		this.totaVo.putParam("OTempTax", tTempVo.getParam("TempTax"));
		this.totaVo.putParam("OBatchNo", tTempVo.getParam("BatchNo"));
		this.totaVo.putParam("ORepayCode", tLoanBorTx.getRepayCode());
		this.totaVo.putParam("ORemitSeq", tTempVo.getParam("RemitSeq"));
		this.totaVo.putParam("OTempReasonCode", tTempVo.getParam("TempReasonCode"));
		this.totaVo.putParam("ORepayBank", tTempVo.getParam("RepayBank"));
		this.totaVo.putParam("ORepayBankX", loanCom.getBankItemByBankCode(tTempVo.getParam("RepayBank")));
		this.totaVo.putParam("OAcctDivisionCode", tTempVo.getParam("AcctDivisionCode"));
		this.totaVo.putParam("OChequeAcctNo", tTempVo.getParam("ChequeAcctNo"));
		this.totaVo.putParam("ORepayPeriod", tLoanBorTx.getPaidTerms());
		this.totaVo.putParam("OChequeNo", tTempVo.getParam("ChequeNo"));
		this.totaVo.putParam("ONewDueAmt", tTempVo.getParam("NewDueAmt"));
		this.totaVo.putParam("ONewTotalPeriod", tTempVo.getParam("NewTotalPeriod"));
		this.totaVo.putParam("ORepayTerms", tLoanBorTx.getPaidTerms());
		this.totaVo.putParam("OIncludeIntFlag", tTempVo.getParam("IncludeIntFlag"));
		this.totaVo.putParam("OUnpaidIntFlag", tTempVo.getParam("UnpaidIntFlag"));
		if (tLoanBorTx.getExtraRepay().compareTo(BigDecimal.ZERO) > 0) {
			this.totaVo.putParam("OExtraUnpaidInt", tLoanBorTx.getUnpaidInterest());
		} else {
			this.totaVo.putParam("OExtraUnpaidInt", BigDecimal.ZERO);
		}
		this.totaVo.putParam("OPayMethod", tTempVo.getParam("PayMethod"));
		this.totaVo.putParam("OOldSpecificDd", tTempVo.getParam("OldSpecificDd"));
		this.totaVo.putParam("ONewSpecificDd", tTempVo.getParam("NewSpecificDd"));
		this.totaVo.putParam("ORateCode", tTempVo.getParam("RateCode"));
		this.totaVo.putParam("OEffectDate", tTempVo.getParam("EffectDate"));
		this.totaVo.putParam("OProdNo", tTempVo.getParam("ProdNo"));
		this.totaVo.putParam("OProdName", tTempVo.getParam("ProdName"));
		this.totaVo.putParam("OProdRate", tTempVo.getParam("ProdRate"));
		this.totaVo.putParam("OBaseRateCode", tTempVo.getParam("BaseRateCode"));
		this.totaVo.putParam("OBaseRate", tTempVo.getParam("BaseRate"));
		this.totaVo.putParam("OFitRate", tTempVo.getParam("FitRate"));
		this.totaVo.putParam("OIncrFlag", tTempVo.getParam("IncrFlag"));
		this.totaVo.putParam("ORateIncr", tTempVo.getParam("RateIncr"));
		this.totaVo.putParam("OIndividualIncr", tTempVo.getParam("IndividualIncr"));
		this.totaVo.putParam("ORemark", tTempVo.getParam("Remark").toString());
		this.totaVo.putParam("OAcctFee", tTempVo.getParam("AcctFee"));
		this.totaVo.putParam("OModifyFee", tTempVo.getParam("ModifyFee"));
		this.totaVo.putParam("OFireFee", tTempVo.getParam("FireFee"));
		this.totaVo.putParam("OLawFee", tTempVo.getParam("LawFee"));
		this.totaVo.putParam("OOvduPaidPrin", tTempVo.getParam("OvduPaidPrin"));
		this.totaVo.putParam("OOvduPaidInt", tTempVo.getParam("OvduPaidInt"));
		this.totaVo.putParam("OOvduPaidBreach", tTempVo.getParam("OvduPaidBreach"));
		this.totaVo.putParam("OOvduReduceInt", tTempVo.getParam("OvduReduceInt"));
		this.totaVo.putParam("OOvduReduceBreach", tTempVo.getParam("OvduReduceBreach"));
		this.totaVo.putParam("OTempReasonCode", tTempVo.getParam("TempReasonCode"));
		this.totaVo.putParam("OTempSourceCode", tTempVo.getParam("TempSourceCode"));
		this.totaVo.putParam("OTempItemCode", tTempVo.getParam("TempItemCode"));
		this.totaVo.putParam("ODescription", tTempVo.getParam("Note"));
		this.totaVo.putParam("ORemitCustName", tTempVo.getParam("RemitCustName"));
		this.totaVo.putParam("ORemitRemark", tTempVo.getParam("RemitRemark"));
		// 聯貸案
		this.totaVo.putParam("OLeadingFee", tTempVo.getParam("LeadingFee"));
		this.totaVo.putParam("OManageFee", tTempVo.getParam("ManageFee"));
		this.totaVo.putParam("OCommitFee", tTempVo.getParam("CommitFee"));
		this.totaVo.putParam("OPartFee", tTempVo.getParam("PartFee"));
		this.totaVo.putParam("OAgencyFee", tTempVo.getParam("AgencyFee"));
		this.totaVo.putParam("OUnderwriteFee", tTempVo.getParam("UnderwriteFee"));
		this.totaVo.putParam("OOtherFee", tTempVo.getParam("OtherFee"));
		this.totaVo.putParam("OSumFee", tTempVo.getParam("SumFee"));
		this.totaVo.putParam("OIncomeFee", tTempVo.getParam("IncomeFee"));
		this.totaVo.putParam("OPrepaidFee", tTempVo.getParam("PrepaidFee"));
		this.totaVo.putParam("OLeadingBank", tTempVo.getParam("LeadingBank"));
		this.totaVo.putParam("OSigningDate", tTempVo.getParam("SigningDate"));
		this.totaVo.putParam("ODrawdownStartDate", tTempVo.getParam("DrawdownStartDate"));
		this.totaVo.putParam("ODrawdownEndDate", tTempVo.getParam("DrawdownEndDate"));
		this.totaVo.putParam("OCommitFeeFlag", tTempVo.getParam("CommitFeeFlag"));
		this.totaVo.putParam("OSyndAmt", tTempVo.getParam("SyndAmt"));
		this.totaVo.putParam("OPartAmt", tTempVo.getParam("PartAmt"));
		this.totaVo.putParam("OAgentBank", tTempVo.getParam("AgentBank"));
		this.totaVo.putParam("OCentralBankPercent", tTempVo.getParam("CentralBankPercent"));
		this.totaVo.putParam("OMasterCustId", tTempVo.getParam("MasterCustId"));
		this.totaVo.putParam("OMasterCustIdX", loanCom.getCustNameById(tTempVo.getParam("MasterCustId")));
		this.totaVo.putParam("ORvNo", rvNo);
		this.totaVo.putParam("OSupervisor", tTempVo.getParam("Supervisor"));
		this.totaVo.putParam("OSupervisorX", loanCom.getEmpFullnameByEmpNo(tTempVo.getParam("Supervisor")));
		this.totaVo.putParam("ORate", tLoanBorTx.getRate());
		String slipSumNoX = "";
		if (tLoanBorTx.getSlipSumNo() > 0) {
			slipSumNoX = "批次(" + parse.IntegerToString(tLoanBorTx.getSlipSumNo(), 2) + ")";
		} else {
			slipSumNoX = "人工";
		}
		this.totaVo.putParam("OSlipSumNoX", slipSumNoX);

		this.addList(this.totaVo);
		return this.sendList();
	}

}