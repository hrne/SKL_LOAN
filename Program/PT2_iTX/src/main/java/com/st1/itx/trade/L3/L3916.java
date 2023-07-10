package com.st1.itx.trade.L3;

import java.math.BigDecimal;
import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.TempVo;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.dataVO.TotaVo;
import com.st1.itx.db.domain.BankRemit;
import com.st1.itx.db.domain.CdBank;
import com.st1.itx.db.domain.CdBankId;
import com.st1.itx.db.domain.FacMain;
import com.st1.itx.db.domain.FacMainId;
import com.st1.itx.db.domain.FacProd;
import com.st1.itx.db.domain.LoanBorMain;
import com.st1.itx.db.domain.LoanBorMainId;
import com.st1.itx.db.domain.LoanOverdue;
import com.st1.itx.db.domain.LoanOverdueId;
import com.st1.itx.db.domain.LoanRateChange;
import com.st1.itx.db.service.BankRemitService;
import com.st1.itx.db.service.CdBankService;
import com.st1.itx.db.service.FacMainService;
import com.st1.itx.db.service.FacProdService;
import com.st1.itx.db.service.LoanBorMainService;
import com.st1.itx.db.service.LoanOverdueService;
import com.st1.itx.db.service.LoanRateChangeService;
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.common.AuthLogCom;
import com.st1.itx.util.common.BaTxCom;
import com.st1.itx.util.date.DateUtil;
import com.st1.itx.util.format.FormatUtil;
import com.st1.itx.util.parse.Parse;

/**
 * L3916 撥款內容查詢
 * 
 * @author iza
 * @version 1.0.0
 */
@Service("L3916")
@Scope("prototype")
public class L3916 extends TradeBuffer {

	/* DB服務注入 */
	@Autowired
	public FacProdService facProdService;
	@Autowired
	public FacMainService facMainService;
	@Autowired
	public LoanBorMainService loanBorMainService;
	@Autowired
	public LoanOverdueService loanOverdueService;
	@Autowired
	public CdBankService cdBankService;
	@Autowired
	public LoanRateChangeService loanRateChangeService;
	@Autowired
	public BankRemitService bankRemitService;
	@Autowired
	public AuthLogCom authLogCom;
	@Autowired
	BaTxCom baTxCom;

	private TempVo tempVo = new TempVo();

	/* 日期工具 */
	@Autowired
	public DateUtil dDateUtil;

	@Autowired
	Parse parse;

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active L3916 ");
		this.totaVo.init(titaVo);

		// 取得輸入資料
		int iCaseNo = this.parse.stringToInteger(titaVo.getParam("CaseNo"));
		int iCustNo = this.parse.stringToInteger(titaVo.getParam("TimCustNo"));
		int iFacmNo = this.parse.stringToInteger(titaVo.getParam("FacmNo"));
		int iBormNo = this.parse.stringToInteger(titaVo.getParam("BormNo"));

		// work area
		int wkCustNo = iCustNo;
		int wkOvduDate = 0;
		String wkOvduSituaction = "";
		BigDecimal wkOvduAmt = BigDecimal.ZERO;
		BigDecimal wkNplRepay = BigDecimal.ZERO;
		BigDecimal wkBadDebtAmt = BigDecimal.ZERO;
		BigDecimal wkBadDebtBal = BigDecimal.ZERO;
		FacMain tFacMain;
		LoanOverdue tLoanOverdue;

		if (iCaseNo > 0) {
			// 由案件編號取得戶號
			tFacMain = facMainService.facmCreditSysNoFirst(iCaseNo, iCaseNo, iFacmNo, iFacmNo, titaVo);
			if (tFacMain == null) {
				throw new LogicException(titaVo, "E0001", "額度主檔 案件編號 = " + iCaseNo); // 查詢資料不存在
			}
			wkCustNo = tFacMain.getCustNo();
		} else {
			tFacMain = facMainService.findById(new FacMainId(iCustNo, iFacmNo), titaVo);
			if (tFacMain == null) {
				throw new LogicException(titaVo, "E0001", "額度主檔 借款人戶號 = " + iCustNo + " 額度編號 = " + iFacmNo); // 查詢資料不存在
			}
		}

		// 查詢商品參數檔
		FacProd tFacProd = facProdService.findById(tFacMain.getProdNo(), titaVo);
		if (tFacProd == null) {
			throw new LogicException(titaVo, "E0001", "商品參數檔"); // 查詢資料不存在
		}

		// 查詢放款主檔
		LoanBorMain tLoanBorMain = loanBorMainService.findById(new LoanBorMainId(wkCustNo, iFacmNo, iBormNo), titaVo);
		if (tLoanBorMain == null) {
			throw new LogicException(titaVo, "E0001",
					"放款主檔  借款人戶號 = " + wkCustNo + "額度編號 = " + iFacmNo + "撥款序號 = " + iBormNo); // 查詢資料不存在
		}

		tempVo = authLogCom.exec(wkCustNo, iFacmNo, titaVo);
		if (tempVo == null) {
			throw new LogicException(titaVo, "E0001", " 額度主檔 借款人戶號 = " + wkCustNo + " 額度編號 = " + iFacmNo); // 查詢資料不存在
		}

		// 查詢行庫代號檔
		String wkRepayBankItem = "";
//		String iBankCode = FormatUtil.padX(tFacMain.getRepayBank().trim(), 7);
		String iBankCode = FormatUtil.padX(tempVo.getParam("RepayBank").trim(), 7);
		String bankCode = FormatUtil.padX(iBankCode, 3);
		String branchCode = FormatUtil.right(iBankCode, 4);
		CdBank tCdBank = cdBankService.findById(new CdBankId(bankCode, branchCode), titaVo);
		if (tCdBank != null) {
			wkRepayBankItem = tCdBank.getBankItem();
		}

		// 查詢各項費用
		baTxCom.settingUnPaid(this.txBuffer.getTxCom().getTbsdy(), iCustNo, iFacmNo, iBormNo, 99, BigDecimal.ZERO,
				titaVo); // 99-費用全部(含未到期)
		// 2: 催收戶 5: 催收結案戶 6: 呆帳戶 7: 部分轉呆戶8:債權轉讓戶 9: 呆帳結案戶
		if (tLoanBorMain.getStatus() == 2 || tLoanBorMain.getStatus() == 5 || tLoanBorMain.getStatus() == 6
				|| tLoanBorMain.getStatus() == 7 || tLoanBorMain.getStatus() == 8 || tLoanBorMain.getStatus() == 9) {
			tLoanOverdue = loanOverdueService.findById(new LoanOverdueId(tLoanBorMain.getCustNo(),
					tLoanBorMain.getFacmNo(), tLoanBorMain.getBormNo(), tLoanBorMain.getLastOvduNo()), titaVo);
			if (tLoanOverdue == null) {
				throw new LogicException(titaVo, "E0001",
						"催收呆帳檔 戶號 = " + tLoanBorMain.getCustNo() + " 額度編號 = " + tLoanBorMain.getFacmNo() + " 撥款序號 = "
								+ tLoanBorMain.getBormNo() + " 催收序號 = " + tLoanBorMain.getLastOvduNo()); // 查詢資料不存在
			}
			wkOvduDate = tLoanOverdue.getOvduDate(); // 催收開始日
			wkOvduAmt = tLoanOverdue.getOvduAmt(); // 轉催收金額
			wkNplRepay = tLoanOverdue.getOvduAmt().subtract(tLoanOverdue.getOvduBal())
					.subtract(tLoanOverdue.getBadDebtAmt()); // 催收還款金額
			wkBadDebtAmt = wkBadDebtAmt.add(tLoanOverdue.getBadDebtAmt()); // 轉呆帳金額
			wkBadDebtBal = wkBadDebtBal.add(tLoanOverdue.getBadDebtBal());// 呆帳餘額
			wkOvduSituaction = tLoanOverdue.getOvduSituaction(); // 催收處理情形

		}
		int Prohibitperiod = 0;

		if (tFacMain.getProhibitMonth() > 0 && tFacMain.getFirstDrawdownDate() > 0) {
			dDateUtil.init();
			dDateUtil.setDate_1(tFacMain.getFirstDrawdownDate());
			dDateUtil.setMons(tFacMain.getProhibitMonth());
			Prohibitperiod = dDateUtil.getCalenderDay(); // 綁約期限
		}
//		目前利率
		LoanRateChange tloanRateChange = loanRateChangeService.rateChangeEffectDateDescFirst(tLoanBorMain.getCustNo(),
				tLoanBorMain.getFacmNo(), tLoanBorMain.getBormNo(), titaVo.getEntDyI() + 19110000, titaVo);
		BigDecimal storeRate = BigDecimal.ZERO;
		if (tloanRateChange != null) {
			storeRate = tloanRateChange.getFitRate();
		}
//		if (tFacProd.getProhibitMonth() > 0) {
//			Prohibitperiod = tFacMain.getFirstDrawdownDate() + (tFacProd.getProhibitMonth() * 120000);
//		} else {
//			Prohibitperiod = 0;
//		}
//		this.info("Prohibitperiod = " + Prohibitperiod);

		this.totaVo.putParam("OCustNo", tLoanBorMain.getCustNo());
		this.totaVo.putParam("OFacmNo", tLoanBorMain.getFacmNo());
		this.totaVo.putParam("OBormNo", tLoanBorMain.getBormNo());
		this.totaVo.putParam("ProdNo", tFacMain.getProdNo());
		this.totaVo.putParam("ProdName", tFacProd.getProdName());
		this.totaVo.putParam("OApplNo", tFacMain.getApplNo());
		this.totaVo.putParam("PROHIBITPERIOD", Prohibitperiod);
		this.totaVo.putParam("AcctCode", tFacMain.getAcctCode());
		this.totaVo.putParam("Status", tLoanBorMain.getStatus());
		if (tLoanBorMain.getStatus() == 2 || tLoanBorMain.getStatus() >= 5 && tLoanBorMain.getStatus() < 90) {
			this.totaVo.putParam("AcctCode", 990);
		}
		this.totaVo.putParam("CurrencyCode", tLoanBorMain.getCurrencyCode());
		this.totaVo.putParam("DrawdownAmt", tLoanBorMain.getDrawdownAmt());
		this.totaVo.putParam("LoanBal", tLoanBorMain.getLoanBal());
		this.totaVo.putParam("FinalBal", tLoanBorMain.getFinalBal());

		// 改抓BankRemit
		this.totaVo.putParam("RemitBank", "");
		this.totaVo.putParam("RemitBranch", "");
		this.totaVo.putParam("RemitAcctNo", 0);
		this.totaVo.putParam("PaymentBank", "");
		this.totaVo.putParam("CompensateAcct", "");
		BankRemit tBankRemit = bankRemitService.findBormNoFirst(tLoanBorMain.getCustNo(), tLoanBorMain.getFacmNo(),
				tLoanBorMain.getBormNo(), titaVo);
		if (tBankRemit != null) {
			CdBank tCdBank1 = cdBankService
					.findById(new CdBankId(tBankRemit.getRemitBank(), tBankRemit.getRemitBranch()), titaVo);
			this.totaVo.putParam("RemitBank", tBankRemit.getRemitBank());
			this.totaVo.putParam("RemitBranch", tBankRemit.getRemitBranch());
			if (tCdBank1 != null) {
				this.totaVo.putParam("RemitBank", tBankRemit.getRemitBank() + " " + tCdBank1.getBankItem());
				this.totaVo.putParam("RemitBranch", tBankRemit.getRemitBranch() + " " + tCdBank1.getBranchItem());
			}
			this.totaVo.putParam("RemitAcctNo", tBankRemit.getRemitAcctNo());
			this.totaVo.putParam("PaymentBank", tBankRemit.getRemitBank() + tBankRemit.getRemitBranch());
			this.totaVo.putParam("CompensateAcct", tBankRemit.getCustName());// 收款戶名
		}
		this.totaVo.putParam("Remark", tLoanBorMain.getRemark());
		this.totaVo.putParam("DrawdownDate", tLoanBorMain.getDrawdownDate());
		this.totaVo.putParam("DrawdownCode", tLoanBorMain.getDrawdownCode());
		this.totaVo.putParam("MaturityDate", tLoanBorMain.getMaturityDate());
		this.totaVo.putParam("FirstDueDate", tLoanBorMain.getFirstDueDate());
		this.totaVo.putParam("RepayCode", tFacMain.getRepayCode());
		this.totaVo.putParam("AcctFee", tLoanBorMain.getAcctFee());
		this.totaVo.putParam("TotalPeriod", tLoanBorMain.getTotalPeriod());
		this.totaVo.putParam("RepaidPeriod", tLoanBorMain.getRepaidPeriod());
		this.totaVo.putParam("IntCalcCode", tLoanBorMain.getIntCalcCode());
		this.totaVo.putParam("AmortizedCode", tLoanBorMain.getAmortizedCode());
		this.totaVo.putParam("DueAmt", tLoanBorMain.getDueAmt());
		this.totaVo.putParam("FreqBase", tLoanBorMain.getFreqBase());
		this.totaVo.putParam("RepayFreq", tLoanBorMain.getRepayFreq());
		this.totaVo.putParam("PayIntFreq", tLoanBorMain.getPayIntFreq());
		this.totaVo.putParam("GraceDate", tLoanBorMain.getGraceDate());
		this.totaVo.putParam("RenewFlag", tLoanBorMain.getRenewFlag());
		this.totaVo.putParam("PieceCode", tLoanBorMain.getPieceCode());
		this.totaVo.putParam("StoreRate", storeRate);
		this.totaVo.putParam("RateCode", tLoanBorMain.getRateCode());
		this.totaVo.putParam("RateIncr", tLoanBorMain.getRateIncr());
		this.totaVo.putParam("FirstAdjRateDate", tLoanBorMain.getFirstAdjRateDate());
		this.totaVo.putParam("IndividualIncr", tLoanBorMain.getIndividualIncr());
		this.totaVo.putParam("RateAdjFreq", tLoanBorMain.getRateAdjFreq());
		this.totaVo.putParam("RepayBank", tempVo.getParam("RepayBank"));
		this.totaVo.putParam("RepayBankItem", wkRepayBankItem);
		this.totaVo.putParam("RepayAcctNo", tempVo.getParam("RepayAcctNo"));
		if (tLoanBorMain.getPrevPayIntDate() == 0) {
			this.totaVo.putParam("PrevIntDate", tLoanBorMain.getDrawdownDate());
		} else {
			this.totaVo.putParam("PrevIntDate", tLoanBorMain.getPrevPayIntDate());
		}
		this.totaVo.putParam("Shortfall", baTxCom.getShortfall());
		this.totaVo.putParam("ShortfallInt", baTxCom.getShortfallInterest());
		this.totaVo.putParam("ShortfallPrin", baTxCom.getShortfallPrincipal());
		this.totaVo.putParam("PrevRepayDate", tLoanBorMain.getPrevRepaidDate());
		this.totaVo.putParam("NextRepayDate", tLoanBorMain.getNextRepayDate());
		this.totaVo.putParam("NplRepay", wkNplRepay);
		this.totaVo.putParam("NplTrfDate", wkOvduDate);
		this.totaVo.putParam("NplTrfAmt", wkOvduAmt);
		this.totaVo.putParam("NplProcSitu", wkOvduSituaction);
		this.totaVo.putParam("BadDebtAmt", wkBadDebtAmt);
		this.totaVo.putParam("BadDebtBal", wkBadDebtBal);
		this.totaVo.putParam("RelationCode", tLoanBorMain.getRelationCode());
		this.totaVo.putParam("RelationName", tLoanBorMain.getRelationName());
		this.totaVo.putParam("RelationId", tLoanBorMain.getRelationId());
		this.totaVo.putParam("RelationBirthday", tLoanBorMain.getRelationBirthday());
		this.totaVo.putParam("RelationGender", tLoanBorMain.getRelationGender());

		this.addList(this.totaVo);
		return this.sendList();
	}
}