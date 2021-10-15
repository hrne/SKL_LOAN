package com.st1.itx.trade.L3;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;

import com.st1.itx.Exception.DBException;
import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.TempVo;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.dataVO.TotaVo;
import com.st1.itx.db.domain.AcDetail;
import com.st1.itx.db.domain.FacClose;
import com.st1.itx.db.domain.FacCloseId;
import com.st1.itx.db.domain.FacMain;
import com.st1.itx.db.domain.FacMainId;
import com.st1.itx.db.domain.LoanBorMain;
import com.st1.itx.db.domain.LoanBorMainId;
import com.st1.itx.db.domain.LoanBorTx;
import com.st1.itx.db.domain.LoanBorTxId;
import com.st1.itx.db.domain.LoanIntDetail;
import com.st1.itx.db.domain.LoanIntDetailId;
import com.st1.itx.db.domain.LoanOverdue;
import com.st1.itx.db.domain.LoanOverdueId;
import com.st1.itx.db.service.FacCloseService;
import com.st1.itx.db.service.FacMainService;
import com.st1.itx.db.service.LoanBorMainService;
import com.st1.itx.db.service.LoanBorTxService;
import com.st1.itx.db.service.LoanChequeService;
import com.st1.itx.db.service.LoanIntDetailService;
import com.st1.itx.db.service.LoanOverdueService;
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.common.AcDetailCom;
import com.st1.itx.util.common.AcPaymentCom;
import com.st1.itx.util.common.AcReceivableCom;
import com.st1.itx.util.common.BaTxCom;
import com.st1.itx.util.common.LoanCalcRepayIntCom;
import com.st1.itx.util.common.LoanCom;
import com.st1.itx.util.common.LoanSetRepayIntCom;
import com.st1.itx.util.common.PfDetailCom;
import com.st1.itx.util.common.SendRsp;
import com.st1.itx.util.common.data.BaTxVo;
import com.st1.itx.util.common.data.CalcRepayIntVo;
import com.st1.itx.util.common.data.PfDetailVo;
import com.st1.itx.util.data.DataLog;
import com.st1.itx.util.parse.Parse;

/*
 * L3420 結案登錄-不可欠繳
 * a.正常結案時不可短繳
 * b.展期時,需輸入新核准號碼
 * c.原則上舊額度之期款利息應繳齊才可辦展期。
 * d.展期時,需以整張額度作業，不可單一撥款或戶號處理。
 * e.[借新還舊]交易登打順序必須先執行結案後再執行新撥貸。
 */
/*
 * Tita
 * TimCustNo=9,7
 * FacmNo=9,3
 * BormNo=9,3
 * CaseCloseCode=9,1 結案區分 0:正常 1:展期(新額度) 2:借新還舊(同額度) 3:轉催收 4:催收戶本人清償 5:催收戶保證人代償 6:催收戶強制執行 7:轉列呆帳 8:催收部分轉呆 
 * EntryDate=9,7
 * NewApplNo=9,7
 * NewFacmNo=9,3
 * TimReduceAmt=9,14.2
 * AdvanceCloseCode=9,2
 * TimTrfBadAmt=9,14.2
 * TotalRepayAmt=9,14.2
 * RealRepayAmt=9,14.2
 * RqspFlag=X,1
 * AcctFee1=9,14.2
 * ModifyFee1=9,14.2
 * FireFee1=9,14.2
 * LawFee1=9,14.2
 */
/**
 * L3420 結案登錄-不可欠繳
 * 
 * @author iza
 * @version 1.0.0
 */
@Service("L3420")
@Scope("prototype")
public class L3420 extends TradeBuffer {

	/* DB服務注入 */
	@Autowired
	public FacMainService facMainService;
	@Autowired
	public LoanBorTxService loanBorTxService;
	@Autowired
	public LoanOverdueService loanOverdueService;
	@Autowired
	public LoanBorMainService loanBorMainService;
	@Autowired
	public LoanIntDetailService loanIntDetailService;
	@Autowired
	public LoanChequeService loanChequeService;
	@Autowired
	public FacCloseService facCloseService;

	@Autowired
	Parse parse;
	@Autowired
	SendRsp sendRsp;
	@Autowired
	DataLog datalog;
	@Autowired
	AcDetailCom acDetailCom;
	@Autowired
	AcPaymentCom acPaymentCom;
	@Autowired
	BaTxCom baTxCom;
	@Autowired
	LoanCom loanCom;
	@Autowired
	LoanSetRepayIntCom loanSetRepayIntCom;
	@Autowired
	LoanCalcRepayIntCom loanCalcRepayIntCom;
	@Autowired
	AcReceivableCom acReceivableCom;
	@Autowired
	PfDetailCom pfDetailCom;

	private TitaVo titaVo = new TitaVo();
	private int iCustNo;
	private int iFacmNo;
	private int iBormNo;
	private int iEntryDate;
	private int iCaseCloseCode;
	private int iNewApplNo;
	private int iNewFacmNo;
	private int iRenewCode;
	private String iAdvanceCloseCode;
	private int iRpCode; // 還款來源
	private BigDecimal iTxAmt;
	private BigDecimal iReduceAmt;
	private BigDecimal iTrfBadAmt;
	private BigDecimal iTotalRepayAmt;
	private BigDecimal iRealRepayAmt;
	private String iRqspFlag;
	private BigDecimal iPrincipal;
	private BigDecimal iInterest;
	private BigDecimal iDelayInt;
	private BigDecimal iBreachAmt;
	private BigDecimal iAcctFee1;
	private BigDecimal iModifyFee1;
	private BigDecimal iFireFee1;
	private BigDecimal iLawFee1;
	private String iLawFg;
	private String iCollLawFg;
	private String iFireFg;
	private String iCollFireFg;

	private BigDecimal iShortfallInt;
	private BigDecimal iShortfallPrin;
	private BigDecimal iShortCloseBreach;
	private BigDecimal iCloseBreachAmt;
	private int iOverRpFg; // 1.短收 2.溢收
	private BigDecimal iOverRpAmt; // 短溢收金額
	// work area
	private int wkTbsDy;
	private int wkCustNo = 0;
	private int wkFacmNo = 0;
	private int wkBormNo = 0;
	private int wkBorxNo = 0;
	private int wkOvduNo = 0;
	private int wkNewBorxNo = 0;
	private int wkFacmNoStart = 1;
	private int wkFacmNoEnd = 999;
	private int wkBormNoStart = 1;
	private int wkBormNoEnd = 900;
	private int wkTotaCount = 0;
	private int wkIntStartDate = 9991231;
	private int wkIntEndDate = 0;
	private int wkRepaidPeriod = 0;
	private int wkPaidTerms = 0;
	private int wkBorMainStatus = 88;
	private int wkDueDate = 0;
	private int wkCloseNo = 0;
	private BigDecimal wkAfterLoanBal = BigDecimal.ZERO;
	private BigDecimal wkTempAmt = BigDecimal.ZERO;
	private BigDecimal wkPrincipal = BigDecimal.ZERO;
	private BigDecimal wkInterest = BigDecimal.ZERO;
	private BigDecimal wkDelayInt = BigDecimal.ZERO;
	private BigDecimal wkBreachAmt = BigDecimal.ZERO;
	private BigDecimal wkCloseBreachAmt = BigDecimal.ZERO;
	private BigDecimal wkReduceAmtRemaind = BigDecimal.ZERO;
	private BigDecimal wkReduceAmt = BigDecimal.ZERO;
	private BigDecimal wkReduceBreachAmt = BigDecimal.ZERO; // 減免清償違約金+減免違約金+減免延滯息
	private BigDecimal wkReduceCloseBreachAmtRemaind = BigDecimal.ZERO;
	private BigDecimal wkReduceBreachAmtRemaind = BigDecimal.ZERO;
	private BigDecimal wkReduceDelayIntRemaind = BigDecimal.ZERO;
	private BigDecimal wkReduceInterestRemaind = BigDecimal.ZERO;
	private BigDecimal wkTrfBadAmt = BigDecimal.ZERO;
	private BigDecimal wkTotalRepayAmt = BigDecimal.ZERO;
	private BigDecimal wkOvduIntAmt = BigDecimal.ZERO;
	private BigDecimal wkOvduReduceInt = BigDecimal.ZERO;
	private BigDecimal wkBeforeOvduBal = BigDecimal.ZERO;
	private BigDecimal wkTrfBreach = BigDecimal.ZERO;
	private BigDecimal wkTrfInt = BigDecimal.ZERO;
	private BigDecimal wkTrfPrin = BigDecimal.ZERO;
	private BigDecimal wkAcctFee = BigDecimal.ZERO;
	private BigDecimal wkModifyFee = BigDecimal.ZERO;
	private BigDecimal wkFireFee = BigDecimal.ZERO;
	private BigDecimal wkLawFee = BigDecimal.ZERO;
	private BigDecimal wkExtraRepay = BigDecimal.ZERO;
	private BigDecimal wkShortfallPrincipal = BigDecimal.ZERO; // 累短收 - 本金
	private BigDecimal wkShortfallInterest = BigDecimal.ZERO; // 累短收-利息
	private BigDecimal wkShortCloseBreach = BigDecimal.ZERO; // 累短收 - 清償違約金
	private AcDetail acDetail;
	private TempVo tTempVo = new TempVo();
	private FacMain tFacMain;
	private LoanBorMain tLoanBorMain;
	private LoanBorTx tLoanBorTx;
	private LoanBorTxId tLoanBorTxId;
	private LoanOverdue tLoanOverdue;
	private LoanOverdueId tLoanOverdueId;
	private LoanIntDetail tLoanIntDetail;
	private LoanIntDetailId tLoanIntDetailId;
	private FacClose tFacClose;
	private List<AcDetail> lAcDetail = new ArrayList<AcDetail>();
	private List<AcDetail> lAcDetailFee = new ArrayList<AcDetail>();
	private List<LoanOverdue> lLoanOverdue = new ArrayList<LoanOverdue>();
	private List<LoanBorMain> lLoanBorMain = new ArrayList<LoanBorMain>();
	private List<LoanBorTx> lLoanBorTx;
	private List<PfDetailVo> pfList = new ArrayList<PfDetailVo>();
	private ArrayList<CalcRepayIntVo> lCalcRepayIntVo = new ArrayList<CalcRepayIntVo>();
	private ArrayList<BaTxVo> baTxList;
	private boolean isFirstBorm = true;
	private boolean isAllClose = false;

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active L3420 ");
		this.info("   isActfgEntry   = " + titaVo.isActfgEntry());
		this.info("   isActfgRelease = " + titaVo.isActfgRelease());
		this.info("   isHcodeNormal  = " + titaVo.isHcodeNormal());
		this.info("   isHcodeErase   = " + titaVo.isHcodeErase());
		this.info("   isHcodeModify  = " + titaVo.isHcodeModify());
		this.info("   EntdyI         = " + titaVo.getEntDyI());
		this.info("   Kinbr          = " + titaVo.getKinbr());
		this.info("   TlrNo          = " + titaVo.getTlrNo());
		this.info("   Tno            = " + titaVo.getTxtNo());
		this.info("   OrgEntdyI      = " + titaVo.getOrgEntdyI());
		this.info("   OrgKin         = " + titaVo.getOrgKin());
		this.info("   OrgTlr         = " + titaVo.getOrgTlr());
		this.info("   OrgTno         = " + titaVo.getOrgTno());

		this.totaVo.init(titaVo);
		this.titaVo = titaVo;
		this.wkTbsDy = this.txBuffer.getTxCom().getTbsdy();
		loanCom.setTxBuffer(this.txBuffer);
		loanSetRepayIntCom.setTxBuffer(this.txBuffer);
		baTxCom.setTxBuffer(this.txBuffer);

		// 取得輸入資料
		iCustNo = this.parse.stringToInteger(titaVo.getParam("TimCustNo"));
		iFacmNo = this.parse.stringToInteger(titaVo.getParam("FacmNo"));
		iBormNo = this.parse.stringToInteger(titaVo.getParam("BormNo"));
		iEntryDate = this.parse.stringToInteger(titaVo.getParam("EntryDate"));
		iCaseCloseCode = this.parse.stringToInteger(titaVo.getParam("CaseCloseCode"));
		iNewApplNo = this.parse.stringToInteger(titaVo.getParam("NewApplNo"));
		iNewFacmNo = this.parse.stringToInteger(titaVo.getParam("NewFacmNo"));
		iRenewCode = this.parse.stringToInteger(titaVo.getParam("RenewCode"));
		iAdvanceCloseCode = titaVo.getParam("AdvanceCloseCode");
		iReduceAmt = this.parse.stringToBigDecimal(titaVo.getParam("TimReduceAmt"));
		iTrfBadAmt = this.parse.stringToBigDecimal(titaVo.getParam("TimTrfBadAmt"));
		iTotalRepayAmt = this.parse.stringToBigDecimal(titaVo.getParam("TotalRepayAmt"));
		iRealRepayAmt = this.parse.stringToBigDecimal(titaVo.getParam("RealRepayAmt"));
		iRqspFlag = titaVo.getParam("RqspFlag");
		iTxAmt = parse.stringToBigDecimal(titaVo.getTxAmt());
		// 收取本金、利息
		iPrincipal = this.parse.stringToBigDecimal(titaVo.getParam("TimPrincipal"));
		iInterest = this.parse.stringToBigDecimal(titaVo.getParam("TimInterest"));
		iDelayInt = this.parse.stringToBigDecimal(titaVo.getParam("TimDelayInt"));
		iBreachAmt = this.parse.stringToBigDecimal(titaVo.getParam("TimBreachAmt"));
		this.info("iPrincipal=" + iPrincipal + ",iInterest=" + iInterest);
		this.info("iDelayInt=" + iDelayInt + ",iBreachAmt=" + iBreachAmt);
		// 收取費用、短繳金額
		if (iCaseCloseCode <= 6 && iCaseCloseCode != 3) {
			iAcctFee1 = this.parse.stringToBigDecimal(titaVo.getParam("AcctFee1"));
			iModifyFee1 = this.parse.stringToBigDecimal(titaVo.getParam("ModifyFee1"));
			iFireFee1 = this.parse.stringToBigDecimal(titaVo.getParam("FireFee1"));
			iLawFee1 = this.parse.stringToBigDecimal(titaVo.getParam("LawFee1"));
			this.info("TotalRepayAmt = " + iTotalRepayAmt + ",RealRepayAmt=" + iRealRepayAmt);
			this.info("iAcctFee1=" + iAcctFee1 + ",iModifyFee1=" + iModifyFee1);
			this.info("iFireFee1=" + iFireFee1 + ",iLawFee1=" + iLawFee1);
		} else {
			iLawFg = titaVo.getParam("LawFg");
			iCollLawFg = titaVo.getParam("CollLawFg");
			iFireFg = titaVo.getParam("FireFg");
			iCollFireFg = titaVo.getParam("CollFireFg");
			iAcctFee1 = BigDecimal.ZERO;
			iModifyFee1 = BigDecimal.ZERO;
			iFireFee1 = BigDecimal.ZERO;
			iLawFee1 = BigDecimal.ZERO;
		}
		iShortfallPrin = this.parse.stringToBigDecimal(titaVo.getParam("ShortfallPrin"));
		iShortfallInt = this.parse.stringToBigDecimal(titaVo.getParam("ShortfallInt"));
		iShortCloseBreach = this.parse.stringToBigDecimal(titaVo.getParam("ShortCloseBreach"));
		this.info("iShortfall=" + iShortfallPrin + "," + iShortfallInt + "," + iShortCloseBreach);
		iCloseBreachAmt = this.parse.stringToBigDecimal(titaVo.getParam("CloseBreachAmt"));
		iRpCode = this.parse.stringToInteger(titaVo.getParam("RpCode1"));
		iOverRpFg = this.parse.stringToInteger(titaVo.getParam("OverRpFg")); // 1->短收 2->溢收
		iOverRpAmt = this.parse.stringToBigDecimal(titaVo.getParam("OverRpAmt"));
		// 不可有短繳金額
		if (iOverRpFg == 1 && iOverRpAmt.compareTo(BigDecimal.ZERO) > 0) {
			throw new LogicException(titaVo, "E3094", "短繳金額 = " + iOverRpAmt); // 不可有短繳金額
		}
		// 按清償違約金、違約金、 延滯息、利息順序減免
		if (iReduceAmt.compareTo(BigDecimal.ZERO) > 0) {
			wkReduceAmtRemaind = iReduceAmt;
			if (wkReduceAmtRemaind.compareTo(BigDecimal.ZERO) > 0) {
				if (wkReduceAmtRemaind.compareTo(iCloseBreachAmt) >= 0) {
					wkReduceAmtRemaind = wkReduceAmtRemaind.subtract(iCloseBreachAmt);
					wkReduceCloseBreachAmtRemaind = iCloseBreachAmt;
				} else {
					wkReduceCloseBreachAmtRemaind = wkReduceAmtRemaind;
					wkReduceAmtRemaind = BigDecimal.ZERO;
				}
			}
			if (wkReduceAmtRemaind.compareTo(BigDecimal.ZERO) > 0) {
				if (wkReduceAmtRemaind.compareTo(iBreachAmt) >= 0) {
					wkReduceAmtRemaind = wkReduceAmtRemaind.subtract(iBreachAmt);
					wkReduceBreachAmtRemaind = iBreachAmt;
				} else {
					wkReduceBreachAmtRemaind = wkReduceAmtRemaind;
					wkReduceAmtRemaind = BigDecimal.ZERO;
				}
			}
			if (wkReduceAmtRemaind.compareTo(BigDecimal.ZERO) > 0) {
				if (wkReduceAmtRemaind.compareTo(iDelayInt) >= 0) {
					wkReduceAmtRemaind = wkReduceAmtRemaind.subtract(iDelayInt);
					wkReduceDelayIntRemaind = iDelayInt;
				} else {
					wkReduceDelayIntRemaind = wkReduceAmtRemaind;
					wkReduceAmtRemaind = BigDecimal.ZERO;
				}
			}
			if (wkReduceAmtRemaind.compareTo(BigDecimal.ZERO) > 0) {
				if (wkReduceAmtRemaind.compareTo(iInterest) >= 0) {
					wkReduceAmtRemaind = wkReduceAmtRemaind.subtract(iInterest);
					wkReduceInterestRemaind = iInterest;
				} else {
					wkReduceInterestRemaind = wkReduceAmtRemaind;
					wkReduceAmtRemaind = BigDecimal.ZERO;
				}
			}
		}
		wkTrfBadAmt = iTrfBadAmt;

		// 暫收款金額(暫收抵繳為負)
		for (int i = 1; i <= 50; i++) {
			if (titaVo.get("RpCode" + i) == null || parse.stringToInteger(titaVo.getParam("RpCode" + i)) == 0)
				break;
			if (parse.stringToInteger(titaVo.getParam("RpCode" + i)) == 90) {
				wkTempAmt = wkTempAmt.subtract(parse.stringToBigDecimal(titaVo.getParam("RpAmt" + i)));
			}
		}

		// 設定額度撥款起止序號
		if (iFacmNo > 0) {
			wkFacmNoStart = iFacmNo;
			wkFacmNoEnd = iFacmNo;
		}
		if (iBormNo > 0) {
			wkBormNoStart = iBormNo;
			wkBormNoEnd = iBormNo;
		}

		// 帳務處理
		if (this.txBuffer.getTxCom().isBookAcYes()) {
			// call 應繳試算
			this.baTxList = baTxCom.settingUnPaid(iEntryDate, iCustNo, iFacmNo, iBormNo, 99, iTxAmt, titaVo); // 99-費用全部(含未到期)
			// 借方：收付欄
			if (iCaseCloseCode <= 6) {
				acPaymentCom.setTxBuffer(this.getTxBuffer());
				acPaymentCom.run(titaVo);
			}
		}

		if (titaVo.isHcodeNormal()) {
			// 結案檢核
			CaseCloseCheckRoutine();
			// 減免金額超過限額，需主管核可
			if (iRqspFlag.equals("Y")) {
				if (!titaVo.getHsupCode().equals("1")) {
					sendRsp.addvReason(this.txBuffer, titaVo, "0007", "");
				}
			}
			switch (iCaseCloseCode) { // 結案區分
			case 0: // 0:正常結案
				wkBorMainStatus = 3; // 3:結案戶
				CaseClose0NormalRoutine();
				break;
			case 1: // 1:展期(新額度)
			case 2: // 2:借新還舊(同額度)
				wkBorMainStatus = 1; // 1:展期
				CaseClose0NormalRoutine();
				break;
			case 3: // 3:轉催收
				wkBorMainStatus = 2; // 2:催收戶
				CaseClose0NormalRoutine();
				break;
			case 4: // 4:催收戶本人清償
			case 5: // 5:催收戶保證人代償
			case 6: // 6:催收戶強制執行
				wkBorMainStatus = 5; // 5: 催收結案戶
				CaseClose0NormalRoutine();
				break;
			case 7: // 7:轉列呆帳
				wkBorMainStatus = 6; // 6: 呆帳戶
				CaseClose7NormalRoutine();
				break;
			case 8: // 8:催收部分轉呆
				wkBorMainStatus = 7; // 7: 部分轉呆戶
				CaseClose8NormalRoutine();
				break;
			case 9: // 9:債權轉讓戶
				wkBorMainStatus = 8; // 8: 債權轉讓戶
				CaseClose9NormalRoutine();
				break;
			default:
				throw new LogicException(titaVo, "E0010", "結案區分 = " + iCaseCloseCode); // 功能選擇錯誤
			}
		} else {
			CaseCloseEraseRoutine();
		}

		// 兌現票入帳處理
		LoanChequeRoutine();

		// 清償作業檔處理
		FacCloseRoutine();

		// 帳務處理
		if (this.txBuffer.getTxCom().isBookAcYes()) {
			// 貸方： 本金利息
			this.txBuffer.addAllAcDetailList(lAcDetail);

			// 產生會計分錄
			acDetailCom.setTxBuffer(this.txBuffer);
			acDetailCom.run(titaVo);
		}

		// end
		this.addList(this.totaVo);
		return this.sendList();
	}

	// 結案檢核
	private void CaseCloseCheckRoutine() throws LogicException {
		if (iCaseCloseCode == 3) {
			if (iAcctFee1.compareTo(BigDecimal.ZERO) > 0) {
				this.totaVo.setWarnMsg("有未收帳管費" + iAcctFee1);
			}
			if (iModifyFee1.compareTo(BigDecimal.ZERO) > 0) {
				this.totaVo.setWarnMsg("有契變手續費" + iModifyFee1);
			}
		}
	}

	// 0:正常 1:展期-一般 2:展期-協議 3:轉催收 4:催收戶本人清償 5:催收戶保證人代償 6:催收戶強制執行
	private void CaseClose0NormalRoutine() throws LogicException {
		this.info("CaseClose0NormalRoutine ...");
		Slice<LoanBorMain> slLoanBorMain = loanBorMainService.bormCustNoEq(iCustNo, wkFacmNoStart, wkFacmNoEnd,
				wkBormNoStart, wkBormNoEnd, 0, Integer.MAX_VALUE);
		lLoanBorMain = slLoanBorMain == null ? null : slLoanBorMain.getContent();
		if (lLoanBorMain == null || lLoanBorMain.size() == 0) {
			throw new LogicException(titaVo, "E0001", "放款主檔"); // 查詢資料不存在
		}
		// 戶況 0:正常戶1:展期2:催收戶3: 結案戶4:逾期戶5:催收結案戶6:呆帳戶7:部分轉呆戶8:債權轉讓戶9:呆帳結案戶
		isFirstBorm = true;
		for (LoanBorMain ln : lLoanBorMain) {
			if (iBormNo > 0) {
				switch (ln.getStatus()) {
				case 3:
					throw new LogicException(titaVo, "E3078",
							"撥款主檔 戶號 = " + iCustNo + " 額度編號 = " + ln.getFacmNo() + " 撥款序號 = " + ln.getBormNo()); // 該筆撥款戶況為結案戶
				case 5:
					throw new LogicException(titaVo, "E3079",
							"撥款主檔 戶號 = " + iCustNo + " 額度編號 = " + ln.getFacmNo() + " 撥款序號 = " + ln.getBormNo()); // 該筆撥款戶況為催收結案戶
				case 9:
					throw new LogicException(titaVo, "E3080",
							"撥款主檔 戶號 = " + iCustNo + " 額度編號 = " + ln.getFacmNo() + " 撥款序號 = " + ln.getBormNo()); // 該筆撥款戶況為呆帳結案戶
				}
			} else {
				if (ln.getStatus() == 3 || ln.getStatus() == 5 || ln.getStatus() == 9) {
					continue;
				}
			}
			if (iCaseCloseCode == 4 || iCaseCloseCode == 5 || iCaseCloseCode == 6) {
				if (ln.getStatus() != 2) { // 2: 催收戶
					if (iBormNo > 0) {
						throw new LogicException(titaVo, "E3069",
								"撥款主檔 戶號 = " + iCustNo + " 額度編號 = " + ln.getFacmNo() + " 撥款序號 = " + ln.getBormNo()); // 該筆放款戶況非催收戶
					} else {
						continue;
					}
				}
			} else {
				if (!(ln.getStatus() == 0 || ln.getStatus() == 4)) {
					if (iBormNo > 0) {
						throw new LogicException(titaVo, "E3068",
								"撥款主檔 戶號 = " + iCustNo + " 額度編號 = " + ln.getFacmNo() + " 撥款序號 = " + ln.getBormNo()); // 該筆放款戶況非正常戶及逾期戶
					} else {
						continue;
					}
				}
			}
			if (ln.getActFg() == 1) {
				throw new LogicException(titaVo, "E0021",
						"放款主檔 戶號 = " + ln.getCustNo() + " 額度編號 =  " + ln.getFacmNo() + " 撥款序號 = " + ln.getBormNo()); // 該筆資料待放行中
			}
			wkCustNo = ln.getCustNo();
			wkFacmNo = ln.getFacmNo();
			wkBormNo = ln.getBormNo();
			wkBorxNo = ln.getLastBorxNo() + 1;
			wkOvduNo = iCaseCloseCode == 3 ? ln.getLastOvduNo() + 1 : ln.getLastOvduNo();
			wkDueDate = ln.getNextPayIntDate();
			wkIntStartDate = 9991231;
			wkIntEndDate = 0;
			// 鎖定撥款主檔
			tLoanBorMain = loanBorMainService.holdById(new LoanBorMainId(iCustNo, wkFacmNo, wkBormNo));
			if (tLoanBorMain == null || tLoanBorMain.getPrevPayIntDate() != ln.getPrevPayIntDate()) {
				throw new LogicException(titaVo, "E0006",
						"撥款主檔 戶號 = " + iCustNo + " 額度編號 = " + wkFacmNo + " 撥款序號 = " + wkBormNo); // 鎖定資料時，發生錯誤
			}
			if (tLoanBorMain.getActFg() == 1) {
				throw new LogicException(titaVo, "E0021", "放款主檔 戶號 = " + tLoanBorMain.getCustNo() + " 額度編號 =  "
						+ tLoanBorMain.getFacmNo() + " 撥款序號 = " + tLoanBorMain.getBormNo()); // 該筆資料待放行中
			}
			// 鎖定催收呆帳檔
			wkOvduIntAmt = BigDecimal.ZERO; // 催收餘額
			wkBeforeOvduBal = BigDecimal.ZERO; // 轉催收利息
			if (iCaseCloseCode == 4 || iCaseCloseCode == 5 || iCaseCloseCode == 6) {
				tLoanOverdue = loanOverdueService.holdById(new LoanOverdueId(iCustNo, wkFacmNo, wkBormNo, wkOvduNo));
				if (tLoanOverdue == null) {
					throw new LogicException(titaVo, "E0006", "催收呆帳檔 Key = " + tLoanOverdueId); // 鎖定資料時，發生錯誤
				}
				wkBeforeOvduBal = tLoanOverdue.getOvduBal();
				wkOvduIntAmt = tLoanOverdue.getOvduIntAmt();
			}
			loanCalcRepayIntCom = loanSetRepayIntCom.setRepayInt(ln, 0, iEntryDate, 1, iEntryDate, titaVo);
			loanCalcRepayIntCom.setCaseCloseFlag("Y");

			// 回收計算
			lCalcRepayIntVo = loanCalcRepayIntCom.getRepayInt(titaVo);

			// 計算金額
			wkPrincipal = loanCalcRepayIntCom.getPrincipal();
			wkInterest = loanCalcRepayIntCom.getInterest();
			wkDelayInt = loanCalcRepayIntCom.getDelayInt();
			wkBreachAmt = loanCalcRepayIntCom.getBreachAmt();

			// 清償違約金放首筆
			if (isFirstBorm) {
				wkCloseBreachAmt = iCloseBreachAmt;
			} else {
				wkCloseBreachAmt = BigDecimal.ZERO;
			}

			// 轉催收，限本金、利息
			if (iCaseCloseCode == 3) {
				wkDelayInt = BigDecimal.ZERO;
				wkBreachAmt = BigDecimal.ZERO;
			} else
			// 催收結案，除轉催收利息外；利息、延滯息、違約金息均以違約金列帳
			if (iCaseCloseCode == 4 || iCaseCloseCode == 5 || iCaseCloseCode == 6) {
				wkBreachAmt = wkBreachAmt.add(wkInterest).add(wkDelayInt).subtract(wkOvduIntAmt);
				wkInterest = wkOvduIntAmt;
				wkDelayInt = BigDecimal.ZERO;
			}

			// 轉催收含收回欠繳本金
			if (iCaseCloseCode == 3) {
				wkAfterLoanBal = ln.getLoanBal();
			} else {
				wkAfterLoanBal = BigDecimal.ZERO;
			}

			// 扣除減免
			wkOvduReduceInt = BigDecimal.ZERO;
			// 減免前金額
			wkReduceAmt = wkCloseBreachAmt.add(wkBreachAmt).add(wkDelayInt).add(wkInterest); // 減免金額
			wkReduceBreachAmt = wkCloseBreachAmt.add(wkBreachAmt).add(wkDelayInt); // 減免清償違約金+減免違約金+減免延滯息
			if (iReduceAmt.compareTo(BigDecimal.ZERO) > 0) {
				if (wkReduceCloseBreachAmtRemaind.compareTo(BigDecimal.ZERO) > 0) {
					if (wkReduceCloseBreachAmtRemaind.compareTo(wkCloseBreachAmt) >= 0) {
						wkReduceCloseBreachAmtRemaind = wkReduceCloseBreachAmtRemaind.subtract(wkCloseBreachAmt);
						wkCloseBreachAmt = BigDecimal.ZERO;
					} else {
						wkCloseBreachAmt = wkCloseBreachAmt.subtract(wkReduceCloseBreachAmtRemaind);
						wkReduceAmtRemaind = BigDecimal.ZERO;
					}
				}
				if (wkReduceBreachAmtRemaind.compareTo(BigDecimal.ZERO) > 0) {
					if (wkReduceBreachAmtRemaind.compareTo(wkBreachAmt) >= 0) {
						wkReduceBreachAmtRemaind = wkReduceBreachAmtRemaind.subtract(wkBreachAmt);
						wkBreachAmt = BigDecimal.ZERO;
					} else {
						wkBreachAmt = wkBreachAmt.subtract(wkReduceBreachAmtRemaind);
						wkReduceBreachAmtRemaind = BigDecimal.ZERO;
					}
				}
				if (wkReduceDelayIntRemaind.compareTo(BigDecimal.ZERO) > 0) {
					if (wkReduceDelayIntRemaind.compareTo(wkDelayInt) >= 0) {
						wkReduceDelayIntRemaind = wkReduceDelayIntRemaind.subtract(wkDelayInt);
						wkDelayInt = BigDecimal.ZERO;
					} else {
						wkDelayInt = wkDelayInt.subtract(wkReduceDelayIntRemaind);
						wkReduceDelayIntRemaind = BigDecimal.ZERO;
					}
				}
				if (wkReduceInterestRemaind.compareTo(BigDecimal.ZERO) > 0) {
					if (wkReduceInterestRemaind.compareTo(wkInterest) >= 0) {
						wkReduceInterestRemaind = wkReduceInterestRemaind.subtract(wkInterest);
						wkInterest = BigDecimal.ZERO;
					} else {
						wkInterest = wkInterest.subtract(wkReduceInterestRemaind);
						wkReduceInterestRemaind = BigDecimal.ZERO;
					}
				}
				// 利息減免金額(轉催收利息減免金額)
				if (wkReduceAmtRemaind.compareTo(BigDecimal.ZERO) > 0) {
					if (wkReduceAmtRemaind.compareTo(wkInterest) >= 0) {
						if (iCaseCloseCode == 3) {
							wkOvduReduceInt = wkOvduReduceInt.add(wkInterest);
						}
						wkReduceAmtRemaind = wkReduceAmtRemaind.subtract(wkInterest);
						wkInterest = BigDecimal.ZERO;
					} else {
						if (iCaseCloseCode == 3) {
							wkOvduReduceInt = wkOvduReduceInt.add(wkReduceAmtRemaind);
						}
						wkInterest = wkInterest.subtract(wkReduceAmtRemaind);
						wkReduceAmtRemaind = BigDecimal.ZERO;
					}
				}
			}

			// 本筆減免=減免前金額 - 減免後金額
			wkReduceAmt = wkReduceAmt.subtract(wkCloseBreachAmt).subtract(wkBreachAmt).subtract(wkDelayInt)
					.subtract(wkInterest); // 減免金額
			wkReduceBreachAmt = wkReduceBreachAmt.subtract(wkCloseBreachAmt).subtract(wkBreachAmt).subtract(wkDelayInt); // 減免清償違約金+減免違約金+減免延滯息
			wkRepaidPeriod = loanCalcRepayIntCom.getRepaidPeriod();
			wkPaidTerms = loanCalcRepayIntCom.getPaidTerms();
			wkTotalRepayAmt = wkTotalRepayAmt.add(wkPrincipal).add(wkInterest).add(wkDelayInt).add(wkBreachAmt);

			// 提前還款金額
			if (iCaseCloseCode == 0 || iCaseCloseCode == 4 || iCaseCloseCode == 5 || iCaseCloseCode == 6) {
				wkExtraRepay = loanCalcRepayIntCom.getExtraAmt();
			} else {
				wkExtraRepay = BigDecimal.ZERO;
			}

			wkTotaCount++;

			// 短繳、費用收回
			getSettleUnpaid();

			// Principal 實收本金 =>還款本金已含短繳本金回收金額
			// Interest 實收利息 => 扣除減免含收回欠繳利息
			wkInterest = wkInterest.add(wkShortfallInterest);
			// CloseBreachAmt 實收清償違約金= 清償違約金 + 收回欠繳清償違約金
			wkCloseBreachAmt = wkCloseBreachAmt.add(wkShortCloseBreach);

			// initialize tTempVo
			tTempVo.clear();
			// 新增交易暫存
			AddTxTempBormRoutine();
			// 更新撥款主檔
			UpdLoanBorMainRoutine();
			// 更新額度主檔
			UpdFacMainRoutine();
			// 轉催收新增催呆檔 3:轉催收
			if (iCaseCloseCode == 3) {
				AddLoanOvduRoutine();
			}
			// 催收結案另更新催呆檔 4:催收戶本人清償 5:催收戶保證人代償 6:催收戶強制執行
			if (iCaseCloseCode == 4 || iCaseCloseCode == 5 || iCaseCloseCode == 6) {
				AddTxTempOvduRoutine(tLoanOverdue);// 新增交易暫存
				UpdLoanOvdu4Routine(); // 更新催呆檔
			}
			// 新增計息明細
			AddLoanIntDetailRoutine();
			// 新增放款交易內容檔
			AddLoanBorTx0Routine();
			// 結案金額帳務處理
			AcDetailCloseRoutine();
			// 業績處理
			PfDetailRoutine();
			// isFirstBorm
			isFirstBorm = false;
		}
		if (wkTotaCount == 0) {
			throw new LogicException(titaVo, "E3081", ""); // 無符合結案區分之撥款資料
		}

	}

	// 訂正
	private void CaseCloseEraseRoutine() throws LogicException {
		this.info("CaseCloseEraseRoutine ...");
		Slice<LoanBorTx> slLoanBortx = loanBorTxService.custNoTxtNoEq(iCustNo, titaVo.getOrgEntdyI() + 19110000,
				titaVo.getOrgKin(), titaVo.getOrgTlr(), titaVo.getOrgTno(), 0, Integer.MAX_VALUE, titaVo);
		lLoanBorTx = slLoanBortx == null ? null : slLoanBortx.getContent();
		if (lLoanBorTx == null || lLoanBorTx.size() == 0) {
			throw new LogicException(titaVo, "E0001",
					"交易明細檔 交易序號=" + titaVo.getOrgKin() + titaVo.getOrgTlr() + titaVo.getOrgTno()); // 查詢資料不存在
		}
		for (LoanBorTx tx : lLoanBorTx) {
			wkCustNo = tx.getCustNo();
			wkFacmNo = tx.getFacmNo();
			wkBormNo = tx.getBormNo();
			wkBorxNo = tx.getBorxNo();
			tTempVo = tTempVo.getVo(tx.getOtherFields());
			// 還原金額處理
			wkPrincipal = this.parse.stringToBigDecimal(tTempVo.get("LoanBal"));
			wkExtraRepay = tx.getExtraRepay();
			// 更新額度主檔
			UpdFacMainRoutine();
			// 還原撥款主檔
			RestoredLoanBorMainRoutine();
			// 註記交易內容檔
			loanCom.setLoanBorTxHcode(wkCustNo, wkFacmNo, wkBormNo, wkBorxNo, wkNewBorxNo, tLoanBorMain.getLoanBal(),
					titaVo);
			// 業績處理
			PfDetailRoutine();

			// 催收檔處理
			if (iCaseCloseCode >= 3) {
				wkOvduNo = parse.stringToInteger(tTempVo.get("OvduNo"));
				// 轉催收刪除催收檔
				if (iCaseCloseCode == 3) {
					deleteLoanOvduRoutine();
				}
				// 還原催收檔
				else {
					RestoredOverdueRoutine();
				}
			}
			// FirstBorm
			isFirstBorm = false;
		}
	}

	// 7:轉列呆帳
	private void CaseClose7NormalRoutine() throws LogicException {
		this.info("CaseClose7NormalRoutine ...");
		List<Integer> lStatus = new ArrayList<Integer>(); // 1:催收 2:部分轉呆 3:呆帳 4:催收回復
		lStatus.add(1);
		lStatus.add(2);
		Slice<LoanOverdue> slLoanOverdue = loanOverdueService.ovduCustNoRange(iCustNo, wkFacmNoStart, wkFacmNoEnd,
				wkBormNoStart, wkBormNoEnd, 1, 999, lStatus, 0, Integer.MAX_VALUE);
		lLoanOverdue = slLoanOverdue == null ? null : slLoanOverdue.getContent();
		if (lLoanOverdue == null || lLoanOverdue.size() == 0) {
			throw new LogicException(titaVo, "E0001", "催收呆帳檔"); // 查詢資料不存在
		}
		for (LoanOverdue od : lLoanOverdue) {
			wkCustNo = od.getCustNo();
			wkFacmNo = od.getFacmNo();
			wkBormNo = od.getBormNo();
			wkOvduNo = od.getOvduNo();
			// 鎖定催收呆帳檔、撥款主檔、額度檔
			holdByOverdueRoutine(od);
			wkBorxNo = tLoanBorMain.getLastBorxNo() + 1;
			wkAfterLoanBal = BigDecimal.ZERO; // 轉列呆帳放款餘額清零
			wkPrincipal = tLoanBorMain.getLoanBal();
			// initialize tTempVo
			tTempVo.clear();
			// 新增交易暫存檔
			AddTxTempOvduRoutine(od);
			AddTxTempBormRoutine();
			// 更新撥款主檔
			UpdLoanBorMainRoutine();
			// 新增放款交易內容檔
			AddLoanBorTx7Routine(od);
			// 更新額度主檔
			UpdFacMainRoutine();
			// 帳務處理
			AcDetailDbCr7Routine(od);
			// 更新催收呆帳檔
			UpdLoanOvdu7Routine(od);
		}
	}

	// 8:催收部分轉呆
	private void CaseClose8NormalRoutine() throws LogicException {
		this.info("CaseClose8NormalRoutine ...");
		List<Integer> lStatus = new ArrayList<Integer>(); // 1:催收 2:部分轉呆 3:呆帳 4:催收回復
		lStatus.add(1);
		lStatus.add(2);
		Slice<LoanOverdue> slLoanOverdue = loanOverdueService.ovduCustNoRange(iCustNo, wkFacmNoStart, wkFacmNoEnd,
				wkBormNoStart, wkBormNoEnd, 1, 999, lStatus, 0, Integer.MAX_VALUE);
		lLoanOverdue = slLoanOverdue == null ? null : slLoanOverdue.getContent();

		if (lLoanOverdue == null || lLoanOverdue.size() == 0) {
			throw new LogicException(titaVo, "E0001", "催收呆帳檔"); // 查詢資料不存在
		}
		for (LoanOverdue od : lLoanOverdue) {
			wkCustNo = od.getCustNo();
			wkFacmNo = od.getFacmNo();
			wkBormNo = od.getBormNo();
			wkOvduNo = od.getOvduNo();
			// 鎖定催收呆帳檔、撥款主檔、額度檔
			holdByOverdueRoutine(od);
			// 更新欄
			wkAfterLoanBal = tLoanBorMain.getLoanBal();
			wkPrincipal = BigDecimal.ZERO;
			wkBorxNo = tLoanBorMain.getLastBorxNo() + 1;
			// initialize tTempVo
			tTempVo.clear();
			// 新增交易暫存檔
			AddTxTempOvduRoutine(od);
			AddTxTempBormRoutine();
			// 更新撥款主檔
			UpdLoanBorMainRoutine();
			// 新增放款交易內容檔
			AddLoanBorTx8Routine(od);
			// 更新額度主檔
			UpdFacMainRoutine();
			// 帳務處理
			AcDetailDbCr8Routine();
			// 更新催收呆帳檔
			UpdLoanOvdu8Routine(od);
		}
	}

	// 9:債權轉讓戶
	private void CaseClose9NormalRoutine() throws LogicException {
		this.info("CaseClose9NormalRoutine ...");
		List<Integer> lStatus = new ArrayList<Integer>(); // 1:催收 2:部分轉呆 3:呆帳 4:催收回復
		lStatus.add(1);
		lStatus.add(2);
		Slice<LoanOverdue> slLoanOverdue = loanOverdueService.ovduCustNoRange(iCustNo, wkFacmNoStart, wkFacmNoEnd,
				wkBormNoStart, wkBormNoEnd, 1, 999, lStatus, 0, Integer.MAX_VALUE);
		lLoanOverdue = slLoanOverdue == null ? null : slLoanOverdue.getContent();

		if (lLoanOverdue == null || lLoanOverdue.size() == 0) {
			throw new LogicException(titaVo, "E0001", "催收呆帳檔"); // 查詢資料不存在
		}
		for (LoanOverdue od : lLoanOverdue) {
			wkCustNo = od.getCustNo();
			wkFacmNo = od.getFacmNo();
			wkBormNo = od.getBormNo();
			wkOvduNo = od.getOvduNo();
			// 鎖定催收呆帳檔、撥款主檔、額度檔
			holdByOverdueRoutine(od);
			// 更新欄
			wkAfterLoanBal = BigDecimal.ZERO; // 債權轉讓戶放款餘額清零
			wkPrincipal = tLoanBorMain.getLoanBal();
			wkBorxNo = tLoanBorMain.getLastBorxNo() + 1;
			// initialize tTempVo
			tTempVo.clear();
			// 新增交易暫存檔
			AddTxTempOvduRoutine(od);
			AddTxTempBormRoutine();
			// 更新撥款主檔
			UpdLoanBorMainRoutine();
			// 新增放款交易內容檔
			AddLoanBorTx9Routine(od);
			// 更新催收呆帳檔
			UpdLoanOvdu9Routine(od);
		}
	}

	// 鎖定催收呆帳檔、撥款主檔、額度檔
	private void holdByOverdueRoutine(LoanOverdue od) throws LogicException {
		// 查詢額度檔
		tFacMain = facMainService.findById(new FacMainId(iCustNo, od.getFacmNo()));
		if (tFacMain == null) {
			throw new LogicException(titaVo, "E3011", "額度主檔 戶號 = " + iCustNo + " 額度編號 = " + od.getFacmNo()); // 鎖定資料時，發生錯誤
		}
		if (tFacMain.getActFg() == 1) {
			throw new LogicException(titaVo, "E0021",
					"額度檔 戶號 = " + tFacMain.getCustNo() + " 額度編號 =  " + tFacMain.getFacmNo()); // 該筆資料待放行中
		}
		// 鎖定催收呆帳檔主檔
		tLoanOverdue = loanOverdueService.holdById(new LoanOverdueId(wkCustNo, wkFacmNo, wkBormNo, wkOvduNo));
		if (tLoanOverdue == null) {
			throw new LogicException(titaVo, "E0006",
					"催收呆帳檔 戶號 = " + wkCustNo + " 額度編號 = " + wkFacmNo + " 撥款序號 = " + wkBormNo + " 催收序號 = " + wkOvduNo); // 鎖定資料時，發生錯誤
		}
		// 鎖定撥款主檔
		tLoanBorMain = loanBorMainService.holdById(new LoanBorMainId(iCustNo, wkFacmNo, wkBormNo));
		if (tLoanBorMain == null) {
			throw new LogicException(titaVo, "E0006",
					"撥款主檔 戶號 = " + iCustNo + " 額度編號 = " + wkFacmNo + " 撥款序號 = " + wkBormNo); // 鎖定資料時，發生錯誤
		}
		if (tLoanBorMain.getActFg() == 1) {
			throw new LogicException(titaVo, "E0021", "放款主檔 戶號 = " + tLoanBorMain.getCustNo() + " 額度編號 =  "
					+ tLoanBorMain.getFacmNo() + " 撥款序號 = " + tLoanBorMain.getBormNo()); // 該筆資料待放行中
		}
	}

	// 新增交易暫存檔
	private void AddTxTempBormRoutine() throws LogicException {
		this.info("AddTxTempBormRoutine ... ");
		if (iCaseCloseCode < 3 || iCaseCloseCode == 4 || iCaseCloseCode == 5 || iCaseCloseCode == 6) {
			tTempVo.putParam("StoreRate", tLoanBorMain.getStoreRate());
			tTempVo.putParam("RepaidPeriod", tLoanBorMain.getRepaidPeriod());
			tTempVo.putParam("PaidTerms", tLoanBorMain.getPaidTerms());
			tTempVo.putParam("PrevPayIntDate", tLoanBorMain.getPrevPayIntDate());
			tTempVo.putParam("PrevRepaidDate", tLoanBorMain.getPrevRepaidDate());
			tTempVo.putParam("NextPayIntDate", tLoanBorMain.getNextPayIntDate());
			tTempVo.putParam("NextRepayDate", tLoanBorMain.getNextRepayDate());
			tTempVo.putParam("DueAmt", tLoanBorMain.getDueAmt());
		}
		// 3.轉催收
		if (iCaseCloseCode == 3) {
			tTempVo.putParam("OvduNo", wkOvduNo);
		}
		tTempVo.putParam("LoanBal", tLoanBorMain.getLoanBal());
		tTempVo.putParam("Status", tLoanBorMain.getStatus());
		tTempVo.putParam("LastEntDy", tLoanBorMain.getLastEntDy());
		tTempVo.putParam("LastKinbr", tLoanBorMain.getLastKinbr());
		tTempVo.putParam("LastTlrNo", tLoanBorMain.getLastTlrNo());
		tTempVo.putParam("LastTxtNo", tLoanBorMain.getLastTxtNo());

	}

	// 新增交易暫存檔
	private void AddTxTempOvduRoutine(LoanOverdue od) throws LogicException {
		this.info("AddTxTemp7Routine ... ");
		tTempVo.putParam("OvduNo", od.getOvduNo());
		tTempVo.putParam("OvduPrinBal", od.getOvduPrinBal());
		tTempVo.putParam("OvduIntBal", od.getOvduIntBal());
		tTempVo.putParam("OvduBreachBal", od.getOvduBreachBal());
		tTempVo.putParam("OvduBal", od.getOvduBal());
		tTempVo.putParam("BadDebtDate", od.getBadDebtDate());
		tTempVo.putParam("BadDebtAmt", od.getBadDebtAmt());
		tTempVo.putParam("BadDebtBal", od.getBadDebtBal());
		tTempVo.putParam("OvduStatus", od.getStatus());
		tTempVo.putParam("AcDate", od.getAcDate());
	}

	// 更新額度主檔
	private void UpdFacMainRoutine() throws LogicException {
		this.info("UpdFacMainRoutine ...");
		tFacMain = facMainService.holdById(new FacMainId(iCustNo, wkFacmNo));
		if (tFacMain.getActFg() == 1) {
			throw new LogicException(titaVo, "E0021",
					"額度檔 戶號 = " + tFacMain.getCustNo() + " 額度編號 =  " + tFacMain.getFacmNo()); // 該筆資料待放行中
		}
		// 不更新 3:轉催收 8:催收部分轉呆
		if (iCaseCloseCode == 3 || iCaseCloseCode == 8) {
			return;
		}
		// 貸出金額(放款餘額)、 已動用額度餘額
		if (titaVo.isHcodeNormal()) {
			tFacMain.setUtilAmt(tFacMain.getUtilAmt().subtract(wkPrincipal));
			if (tFacMain.getRecycleCode().equals("1")) {
				tFacMain.setUtilBal(tFacMain.getUtilBal().subtract(wkPrincipal));
			}
		} else {
			tFacMain.setUtilAmt(tFacMain.getUtilAmt().add(wkPrincipal));
			if (tFacMain.getRecycleCode().equals("1")) {
				tFacMain.setUtilBal(tFacMain.getUtilBal().add(wkPrincipal));
			}
		}
		if (tFacMain.getUtilAmt().compareTo(BigDecimal.ZERO) == 0) {
			isAllClose = true;
		}
		try {
			facMainService.update(tFacMain);
		} catch (DBException e) {
			throw new LogicException(titaVo, "E0007", "額度主檔 戶號 = " + iCustNo + " 額度編號 = " + wkFacmNo); // 更新資料時，發生錯誤
		}
	}

	// 更新撥款主檔
	private void UpdLoanBorMainRoutine() throws LogicException {
		this.info("updLoanBorMainRoutine ... ");
		// 還款結案
		if (iCaseCloseCode < 3 || iCaseCloseCode == 4 || iCaseCloseCode == 5 || iCaseCloseCode == 6) {
			tLoanBorMain.setStoreRate(loanCalcRepayIntCom.getStoreRate());
			if (tLoanBorMain.getAmortizedCode().equals("3")) {
				tLoanBorMain.setDueAmt(loanCalcRepayIntCom.getDueAmt());
			}
			tLoanBorMain.setRepaidPeriod(tLoanBorMain.getRepaidPeriod() + loanCalcRepayIntCom.getRepaidPeriod());
			tLoanBorMain.setPaidTerms(loanCalcRepayIntCom.getPaidTerms());
			tLoanBorMain.setPrevPayIntDate(loanCalcRepayIntCom.getPrevPaidIntDate());
			tLoanBorMain.setPrevRepaidDate(loanCalcRepayIntCom.getPrevRepaidDate());
			tLoanBorMain.setNextPayIntDate(0);
			tLoanBorMain.setNextRepayDate(0);
		}

		// 3.轉催收
		if (iCaseCloseCode == 3) {
			tLoanBorMain.setLastOvduNo(wkOvduNo);
		}

		// 全部
		tLoanBorMain.setStatus(wkBorMainStatus);
		tLoanBorMain.setLoanBal(wkAfterLoanBal);
		tLoanBorMain.setLastBorxNo(wkBorxNo);
		tLoanBorMain.setLastEntDy(titaVo.getEntDyI());
		tLoanBorMain.setLastKinbr(titaVo.getKinbr());
		tLoanBorMain.setLastTlrNo(titaVo.getTlrNo());
		tLoanBorMain.setLastTxtNo(titaVo.getTxtNo());
		try {
			loanBorMainService.update(tLoanBorMain);
		} catch (DBException e) {
			throw new LogicException(titaVo, "E0007",
					"撥款主檔 戶號 = " + iCustNo + " 額度編號 = " + wkFacmNo + " 撥款序號 = " + wkBormNo); // 更新資料時，發生錯誤
		}
	}

	// 還原撥款主檔
	private void RestoredLoanBorMainRoutine() throws LogicException {
		this.info("RestoredLoanBorMainRoutine ... ");

		tLoanBorMain = loanBorMainService.holdById(new LoanBorMainId(wkCustNo, wkFacmNo, wkBormNo));
		if (tLoanBorMain == null) {
			throw new LogicException(titaVo, "E0006",
					"撥款主檔 戶號 = " + wkCustNo + " 額度編號 = " + wkFacmNo + " 撥款序號 = " + wkBormNo); // 鎖定資料時，發生錯誤
		}
		if (tLoanBorMain.getActFg() == 1) {
			throw new LogicException(titaVo, "E0021", "放款主檔 戶號 = " + tLoanBorMain.getCustNo() + " 額度編號 =  "
					+ tLoanBorMain.getFacmNo() + " 撥款序號 = " + tLoanBorMain.getBormNo()); // 該筆資料待放行中
		}
		wkNewBorxNo = tLoanBorMain.getLastBorxNo() + 1;
		// 放款交易訂正交易須由最後一筆交易開始訂正
		loanCom.checkEraseBormTxSeqNo(tLoanBorMain, titaVo);
		tLoanBorMain.setLastBorxNo(wkNewBorxNo);

		// 還款結案
		if (iCaseCloseCode < 3 || iCaseCloseCode == 4 || iCaseCloseCode == 5 || iCaseCloseCode == 6) {
			tLoanBorMain.setStoreRate(this.parse.stringToBigDecimal(tTempVo.get("StoreRate")));
			tLoanBorMain.setRepaidPeriod(this.parse.stringToInteger(tTempVo.get("RepaidPeriod")));
			tLoanBorMain.setPaidTerms(this.parse.stringToInteger(tTempVo.get("PaidTerms")));
			tLoanBorMain.setPrevPayIntDate(this.parse.stringToInteger(tTempVo.get("PrevPayIntDate")));
			tLoanBorMain.setPrevRepaidDate(this.parse.stringToInteger(tTempVo.get("PrevRepaidDate")));
			tLoanBorMain.setNextPayIntDate(this.parse.stringToInteger(tTempVo.get("NextPayIntDate")));
			tLoanBorMain.setNextRepayDate(this.parse.stringToInteger(tTempVo.get("NextRepayDate")));
			tLoanBorMain.setDueAmt(this.parse.stringToBigDecimal(tTempVo.get("DueAmt")));
		}

		// 轉催收
		if (iCaseCloseCode == 3) {
			tLoanBorMain.setLastOvduNo(this.parse.stringToInteger(tTempVo.get("OvduNo")) - 1);
		}

		// 全部
		tLoanBorMain.setStatus(this.parse.stringToInteger(tTempVo.get("Status")));
		tLoanBorMain.setLoanBal(this.parse.stringToBigDecimal(tTempVo.get("LoanBal")));
		tLoanBorMain.setLastEntDy(this.parse.stringToInteger(tTempVo.get("LastEntDy")));
		tLoanBorMain.setLastKinbr(tTempVo.get("LastKinbr"));
		tLoanBorMain.setLastTlrNo(tTempVo.get("LastTlrNo"));
		tLoanBorMain.setLastTxtNo(tTempVo.get("LastTxtNo"));
		try {
			loanBorMainService.update(tLoanBorMain);
		} catch (DBException e) {
			throw new LogicException(titaVo, "E0007",
					"撥款主檔 戶號 = " + wkCustNo + " 額度編號 = " + wkFacmNo + " 撥款序號 = " + wkBormNo); // 更新資料時，發生錯誤
		}
	}

	// 轉催收新增催收檔
	private void AddLoanOvduRoutine() throws LogicException {
		tLoanOverdue = new LoanOverdue();
		tLoanOverdueId = new LoanOverdueId();
		tLoanOverdueId.setCustNo(iCustNo);
		tLoanOverdueId.setFacmNo(wkFacmNo);
		tLoanOverdueId.setBormNo(wkBormNo);
		tLoanOverdueId.setOvduNo(wkOvduNo);
		tLoanOverdue.setCustNo(iCustNo);
		tLoanOverdue.setFacmNo(wkFacmNo);
		tLoanOverdue.setBormNo(wkBormNo);
		tLoanOverdue.setOvduNo(wkOvduNo);
		tLoanOverdue.setLoanOverdueId(tLoanOverdueId);
		tLoanOverdue.setStatus(1); // 1:催收
		tLoanOverdue.setAcctCode("990"); // 990:催收款項
		tLoanOverdue.setOvduDate(iEntryDate);
		tLoanOverdue.setBadDebtDate(0);
		tLoanOverdue.setReplyDate(0);
		tLoanOverdue.setOvduPrinAmt(loanCalcRepayIntCom.getPrincipal());
		tLoanOverdue.setOvduIntAmt(wkInterest.add(wkDelayInt));
		tLoanOverdue.setOvduBreachAmt(wkBreachAmt);
		tLoanOverdue.setOvduAmt(loanCalcRepayIntCom.getPrincipal().add(wkInterest).add(wkDelayInt).add(wkBreachAmt));
		tLoanOverdue.setOvduPrinBal(loanCalcRepayIntCom.getPrincipal());
		tLoanOverdue.setOvduIntBal(wkInterest.add(wkDelayInt));
		tLoanOverdue.setOvduBreachBal(wkBreachAmt);
		tLoanOverdue.setOvduBal(loanCalcRepayIntCom.getPrincipal().add(wkInterest).add(wkDelayInt).add(wkBreachAmt));
		tLoanOverdue.setReduceInt(wkOvduReduceInt);
		tLoanOverdue.setReduceBreach(BigDecimal.ZERO);
		tLoanOverdue.setBadDebtAmt(BigDecimal.ZERO);
		tLoanOverdue.setBadDebtBal(BigDecimal.ZERO);
		tLoanOverdue.setReplyReduceAmt(BigDecimal.ZERO);
		tLoanOverdue.setOvduSituaction("");
		tLoanOverdue.setRemark("");
		tLoanOverdue.setAcDate(wkTbsDy);
		try {
			loanOverdueService.insert(tLoanOverdue);
		} catch (DBException e) {
			throw new LogicException(titaVo, "E0005", "催收呆帳檔 Key = " + tLoanOverdueId); // 新增資料時，發生錯誤
		}
	}

	// 催收收回更新催收呆帳檔
	private void UpdLoanOvdu4Routine() throws LogicException {
		this.info("UpdLoanOvdu4Routine ... ");

		tLoanOverdue.setOvduPrinBal(BigDecimal.ZERO);
		tLoanOverdue.setOvduIntBal(BigDecimal.ZERO);
		tLoanOverdue.setOvduBreachBal(BigDecimal.ZERO);
		tLoanOverdue.setOvduBal(BigDecimal.ZERO);
		tLoanOverdue.setAcDate(wkTbsDy);
		tLoanOverdue.setStatus(5); // 5.催呆結案
		try {
			loanOverdueService.update(tLoanOverdue);
		} catch (DBException e) {
			throw new LogicException(titaVo, "E0007",
					"催收呆帳檔 戶號 = " + wkCustNo + " 額度編號 = " + wkFacmNo + " 撥款序號 = " + wkBormNo + " 催收序號 = " + wkOvduNo); // 更新資料時，發生錯誤
		}
	}

	// 轉呆帳更新催收呆帳檔
	private void UpdLoanOvdu7Routine(LoanOverdue od) throws LogicException {
		this.info("UpdLoanOvdu7Routine ... ");

		tLoanOverdue.setBadDebtDate(wkTbsDy);
		tLoanOverdue.setBadDebtAmt(od.getOvduBal());
		tLoanOverdue.setBadDebtBal(od.getOvduBal());
		tLoanOverdue.setOvduPrinBal(BigDecimal.ZERO);
		tLoanOverdue.setOvduIntBal(BigDecimal.ZERO);
		tLoanOverdue.setOvduBreachBal(BigDecimal.ZERO);
		tLoanOverdue.setOvduBal(BigDecimal.ZERO);
		tLoanOverdue.setAcDate(wkTbsDy);
		tLoanOverdue.setStatus(3); // 3: 呆帳
		try {
			loanOverdueService.update(tLoanOverdue);
		} catch (DBException e) {
			throw new LogicException(titaVo, "E0007",
					"催收呆帳檔 戶號 = " + wkCustNo + " 額度編號 = " + wkFacmNo + " 撥款序號 = " + wkBormNo + " 催收序號 = " + wkOvduNo); // 更新資料時，發生錯誤
		}
	}

	// 部分轉呆更新催收呆帳檔
	private void UpdLoanOvdu8Routine(LoanOverdue od) throws LogicException {
		this.info("UpdLoanOvdu8Routine ... ");

		if (tLoanOverdue.getBadDebtDate() == 0) {
			tLoanOverdue.setBadDebtDate(wkTbsDy);
		}
		tLoanOverdue.setBadDebtAmt(od.getBadDebtAmt().add(wkTrfPrin).add(wkTrfInt).add(wkTrfBreach));
		tLoanOverdue.setBadDebtBal(od.getBadDebtBal().add(wkTrfPrin).add(wkTrfInt).add(wkTrfBreach));
		tLoanOverdue.setOvduPrinBal(od.getOvduPrinBal().subtract(wkTrfPrin));
		tLoanOverdue.setOvduIntBal(od.getOvduIntBal().subtract(wkTrfInt));
		tLoanOverdue.setOvduBreachBal(od.getOvduBreachBal().subtract(wkTrfBreach));
		tLoanOverdue.setOvduBal(od.getOvduBal().subtract(wkTrfPrin).subtract(wkTrfInt).subtract(wkTrfBreach));
		tLoanOverdue.setAcDate(wkTbsDy);
		tLoanOverdue.setStatus(2); // 2. 部分轉呆
		try {
			loanOverdueService.update(tLoanOverdue);
		} catch (DBException e) {
			throw new LogicException(titaVo, "E0007",
					"催收呆帳檔 戶號 = " + wkCustNo + " 額度編號 = " + wkFacmNo + " 撥款序號 = " + wkBormNo + " 催收序號 = " + wkOvduNo); // 更新資料時，發生錯誤
		}
	}

	// 債權轉讓戶更新催收呆帳檔
	private void UpdLoanOvdu9Routine(LoanOverdue od) throws LogicException {
		this.info("UpdLoanOvdu8Routine ... ");
		tLoanOverdue.setAcDate(wkTbsDy);
		tLoanOverdue.setStatus(5); // 5.催呆結案
		try {
			loanOverdueService.update(tLoanOverdue);
		} catch (DBException e) {
			throw new LogicException(titaVo, "E0007",
					"催收呆帳檔 戶號 = " + wkCustNo + " 額度編號 = " + wkFacmNo + " 撥款序號 = " + wkBormNo + " 催收序號 = " + wkOvduNo); // 更新資料時，發生錯誤
		}
	}

	// 還原催收檔
	private void RestoredOverdueRoutine() throws LogicException {
		this.info("RestoredOverdueRoutine ... ");
		this.info("   iCaseCloseCode = " + iCaseCloseCode);

		tLoanOverdue = loanOverdueService.holdById(new LoanOverdueId(wkCustNo, wkFacmNo, wkBormNo, wkOvduNo));
		if (tLoanOverdue == null) {
			throw new LogicException(titaVo, "E0006",
					"催收呆帳檔 戶號 = " + wkCustNo + " 額度編號 = " + wkFacmNo + " 撥款序號 = " + wkBormNo + " 催收序號 = " + wkOvduNo); // 鎖定資料時，發生錯誤
		}
		switch (iCaseCloseCode) { // 結案區分
		case 4: // 4:催收戶本人清償
		case 5: // 5:催收戶保證人代償
		case 6: // 6:催收戶強制執行
			tLoanOverdue.setOvduPrinBal(this.parse.stringToBigDecimal(tTempVo.get("OvduPrinBal")));
			tLoanOverdue.setOvduIntBal(this.parse.stringToBigDecimal(tTempVo.get("OvduIntBal")));
			tLoanOverdue.setOvduBreachBal(this.parse.stringToBigDecimal(tTempVo.get("OvduBreachBal")));
			tLoanOverdue.setOvduBal(this.parse.stringToBigDecimal(tTempVo.get("OvduBal")));
			break;
		case 7: // 7:轉列呆帳
		case 8: // 8:催收部分轉呆
			tLoanOverdue.setBadDebtDate(this.parse.stringToInteger(tTempVo.get("BadDebtDate")));
			tLoanOverdue.setBadDebtAmt(this.parse.stringToBigDecimal(tTempVo.get("BadDebtAmt")));
			tLoanOverdue.setBadDebtBal(this.parse.stringToBigDecimal(tTempVo.get("BadDebtBal")));
			tLoanOverdue.setOvduPrinBal(this.parse.stringToBigDecimal(tTempVo.get("OvduPrinBal")));
			tLoanOverdue.setOvduIntBal(this.parse.stringToBigDecimal(tTempVo.get("OvduIntBal")));
			tLoanOverdue.setOvduBreachBal(this.parse.stringToBigDecimal(tTempVo.get("OvduBreachBal")));
			tLoanOverdue.setOvduBal(this.parse.stringToBigDecimal(tTempVo.get("OvduBal")));
			break;
		default:
			break;
		}
		tLoanOverdue.setStatus(this.parse.stringToInteger(tTempVo.get("OvduStatus")));
		tLoanOverdue.setAcDate(this.parse.stringToInteger(tTempVo.get("AcDate")));
		try {
			loanOverdueService.update(tLoanOverdue);
		} catch (DBException e) {
			throw new LogicException(titaVo, "E0007",
					"催收呆帳檔 戶號 = " + wkCustNo + " 額度編號 = " + wkFacmNo + " 撥款序號 = " + wkBormNo + " 催收序號 = " + wkOvduNo); // 更新資料時，發生錯誤
		}
	}

	// 刪除催收呆帳檔
	private void deleteLoanOvduRoutine() throws LogicException {
		tLoanOverdue = loanOverdueService.holdById(new LoanOverdueId(wkCustNo, wkFacmNo, wkBormNo, wkOvduNo));
		if (tLoanOverdue == null) {
			throw new LogicException(titaVo, "E0006",
					"催收呆帳檔 戶號 = " + wkCustNo + " 額度編號 = " + wkFacmNo + " 撥款序號 = " + wkBormNo + " 催收序號 = " + wkOvduNo); // 鎖定資料時，發生錯誤
		}
		try {
			loanOverdueService.delete(tLoanOverdue);
		} catch (DBException e) {
			throw new LogicException(titaVo, "E0007",
					"催收呆帳檔 戶號 = " + wkCustNo + " 額度編號 = " + wkFacmNo + " 撥款序號 = " + wkBormNo + " 催收序號 = " + wkOvduNo); // 更新資料時，發生錯誤
		}
	}

	// 新增計息明細
	private void AddLoanIntDetailRoutine() throws LogicException {
		this.info("AddLoanIntDetailRoutine ... ");

		if (iCaseCloseCode >= 7) {
			return;
		}
		if (lCalcRepayIntVo == null || lCalcRepayIntVo.size() == 0) {
			return;
		}

		int wkIntSeq = 0;

		for (CalcRepayIntVo c : lCalcRepayIntVo) {
			wkIntSeq++;
			wkIntStartDate = c.getStartDate() < wkIntStartDate ? c.getStartDate() : wkIntStartDate;
			wkIntEndDate = c.getEndDate() > wkIntEndDate ? c.getEndDate() : wkIntEndDate;
			tLoanIntDetailId = new LoanIntDetailId();
			tLoanIntDetailId.setCustNo(c.getCustNo());
			tLoanIntDetailId.setFacmNo(c.getFacmNo());
			tLoanIntDetailId.setBormNo(c.getBormNo());
			tLoanIntDetailId.setAcDate(wkTbsDy);
			tLoanIntDetailId.setTlrNo(titaVo.getTlrNo());
			tLoanIntDetailId.setTxtNo(titaVo.getTxtNo());
			tLoanIntDetailId.setIntSeq(wkIntSeq);
			tLoanIntDetail = new LoanIntDetail();
			tLoanIntDetail.setCustNo(c.getCustNo());
			tLoanIntDetail.setFacmNo(c.getFacmNo());
			tLoanIntDetail.setBormNo(c.getBormNo());
			tLoanIntDetail.setAcDate(wkTbsDy);
			tLoanIntDetail.setTlrNo(titaVo.getTlrNo());
			tLoanIntDetail.setTxtNo(titaVo.getTxtNo());
			tLoanIntDetail.setIntSeq(wkIntSeq);
			tLoanIntDetail.setLoanIntDetailId(tLoanIntDetailId);
			tLoanIntDetail.setIntStartDate(c.getStartDate());
			tLoanIntDetail.setIntEndDate(c.getEndDate());
			tLoanIntDetail.setIntDays(c.getDays());
			tLoanIntDetail.setBreachDays(c.getOdDays());
			tLoanIntDetail.setMonthLimit(c.getMonthLimit());
			tLoanIntDetail.setIntFlag(c.getInterestFlag());
			tLoanIntDetail.setCurrencyCode(tFacMain.getCurrencyCode());
			tLoanIntDetail.setIntRate(c.getStoreRate());
			tLoanIntDetail.setRateIncr(c.getRateIncr());
			tLoanIntDetail.setAmount(c.getAmount());
			tLoanIntDetail.setIndividualIncr(c.getIndividualIncr());
			tLoanIntDetail.setPrincipal(c.getPrincipal());
			tLoanIntDetail.setInterest(c.getInterest());
			tLoanIntDetail.setDelayInt(c.getDelayInt());
			tLoanIntDetail.setBreachAmt(c.getBreachAmt());
			tLoanIntDetail.setCloseBreachAmt(c.getCloseBreachAmt());
			tLoanIntDetail.setBreachGetCode(c.getBreachGetCode());
			tLoanIntDetail.setLoanBal(c.getLoanBal());
			tLoanIntDetail.setExtraRepayFlag(c.getExtraRepayFlag());
			tLoanIntDetail.setProdNo(tFacMain.getProdNo());
			tLoanIntDetail.setBaseRateCode(tFacMain.getBaseRateCode());
			try {
				loanIntDetailService.insert(tLoanIntDetail);
			} catch (DBException e) {
				throw new LogicException(titaVo, "E0005", "計息明細 Key = " + tLoanIntDetailId); // 新增資料時，發生錯誤
			}
		}
	}

	// 新增放款交易內容檔
	private void AddLoanBorTx0Routine() throws LogicException {
		this.info("AddLoanBorTx0Routine ... ");

		tLoanBorTx = new LoanBorTx();
		tLoanBorTxId = new LoanBorTxId();
		loanCom.setLoanBorTx(tLoanBorTx, tLoanBorTxId, iCustNo, wkFacmNo, wkBormNo, wkBorxNo, titaVo);
		switch (iCaseCloseCode) {
		case 0:
			tLoanBorTx.setDesc("正常結案");
			break;
		case 1:
			tLoanBorTx.setDesc("展期");
			break;
		case 2:
			tLoanBorTx.setDesc("借新還舊");
			break;
		case 3:
			tLoanBorTx.setDesc("轉催收");
			break;
		case 4:
			tLoanBorTx.setDesc("催收戶本人清償 ");
			break;
		case 5:
			tLoanBorTx.setDesc("催收戶保證人代償");
			break;
		case 6:
			tLoanBorTx.setDesc("催收戶強制執行");
			break;
		}
		tLoanBorTx.setEntryDate(iEntryDate);
		tLoanBorTx.setDueDate(wkDueDate);
		tLoanBorTx.setLoanBal(wkAfterLoanBal);
		tLoanBorTx.setRate(tLoanBorMain.getStoreRate());
		tLoanBorTx.setIntStartDate(wkIntStartDate);
		tLoanBorTx.setIntEndDate(wkIntEndDate);
		tLoanBorTx.setRepaidPeriod(wkRepaidPeriod);
		tLoanBorTx.setPrincipal(wkPrincipal);
		// 利息收入金額=利息收入扣除轉催收利息
		tLoanBorTx.setInterest(wkInterest.subtract(wkOvduIntAmt));
		tLoanBorTx.setDelayInt(wkDelayInt);
		tLoanBorTx.setBreachAmt(wkBreachAmt);
		tLoanBorTx.setCloseBreachAmt(wkCloseBreachAmt);
		tLoanBorTx.setExtraRepay(wkExtraRepay);
		tLoanBorTx.setUnpaidPrincipal(BigDecimal.ZERO);
		tLoanBorTx.setUnpaidInterest(BigDecimal.ZERO);
		tLoanBorTx.setShortfall(BigDecimal.ZERO);
		// 繳息首筆、繳息次筆
		if (isFirstBorm) {
			tLoanBorTx.setDisplayflag("F"); // 繳息首筆
			tLoanBorTx.setTxAmt(iTxAmt); //
			tLoanBorTx.setTempAmt(wkTempAmt);
			if (iOverRpFg == 1) // 1->短收 2->溢收
				tLoanBorTx.setShortfall(iOverRpAmt);
			else
				tLoanBorTx.setOverflow(iOverRpAmt);
		} else {
			tLoanBorTx.setDisplayflag("I"); // 繳息次筆
		}

		// 其他欄位
		tTempVo.putParam("CaseCloseCode", iCaseCloseCode);
		tTempVo.putParam("AdvanceCloseCode", iAdvanceCloseCode);
		tTempVo.putParam("PaidTerms", wkPaidTerms);
		tTempVo.putParam("NewApplNo", iNewApplNo);
		tTempVo.putParam("NewFacmNo", iNewFacmNo);
		tTempVo.putParam("RenewCode", iRenewCode);

		// 減免金額
		if (wkReduceAmt.compareTo(BigDecimal.ZERO) > 0) {
			tTempVo.putParam("ReduceAmt", wkReduceAmt);
		}
		if (wkReduceBreachAmt.compareTo(BigDecimal.ZERO) > 0) {
			tTempVo.putParam("ReduceBreachAmt", wkReduceBreachAmt); // 減免清償違約金+減免違約金+減免延滯息
		}
		// 短繳金額收回
		if (wkShortfallPrincipal.compareTo(BigDecimal.ZERO) > 0) {
			tTempVo.putParam("ShortfallPrin", wkShortfallPrincipal);
		}
		if (wkShortfallInterest.compareTo(BigDecimal.ZERO) > 0) {
			tTempVo.putParam("ShortfallInt", wkShortfallInterest);
		}
		if (wkShortCloseBreach.compareTo(BigDecimal.ZERO) > 0) {
			tTempVo.putParam("ShortCloseBreach", wkShortCloseBreach);
		}
		if (wkAcctFee.compareTo(BigDecimal.ZERO) > 0) {
			tTempVo.putParam("AcctFee", wkAcctFee);
		}
		if (wkModifyFee.compareTo(BigDecimal.ZERO) > 0) {
			tTempVo.putParam("ModifyFee", wkModifyFee);
		}
		if (wkFireFee.compareTo(BigDecimal.ZERO) > 0) {
			tTempVo.putParam("FireFee", wkFireFee);
		}
		if (wkLawFee.compareTo(BigDecimal.ZERO) > 0) {
			tTempVo.putParam("LawFee", wkLawFee);
		}
		if (titaVo.get("RpCode1") != null) {
			tTempVo.putParam("RepayCode", titaVo.get("RpCode1")); // 還款來源
		}

		if (isFirstBorm) {
			if (titaVo.get("RpDscpt1") != null) {
				tTempVo.putParam("DscptCode", titaVo.get("RpDscpt1")); // 摘要代碼
			}
			if (titaVo.getBacthNo().trim() != "") {
				tTempVo.putParam("BatchNo", titaVo.getBacthNo()); // 整批批號
				tTempVo.putParam("DetailSeq", titaVo.get("RpDetailSeq1")); // 明細序號
			}
		}
		tLoanBorTx.setOtherFields(tTempVo.getJsonString());
		try {
			loanBorTxService.insert(tLoanBorTx);
		} catch (DBException e) {
			throw new LogicException(titaVo, "E0005", "放款交易內容檔 " + e.getErrorMsg() + " Key = " + tLoanBorTxId); // 新增資料時，發生錯誤
		}
	}

	// 新增放款交易內容檔
	private void AddLoanBorTx7Routine(LoanOverdue od) throws LogicException {
		this.info("AddLoanBorTx7Routine ... ");
		// 抓取火險費、法務費轉銷呆帳金額
		getTrfBadUnpaid();
		wkTrfBadAmt = wkTrfBadAmt.subtract(wkFireFee).subtract(wkLawFee);

		tLoanBorTx = new LoanBorTx();
		tLoanBorTxId = new LoanBorTxId();
		loanCom.setLoanBorTx(tLoanBorTx, tLoanBorTxId, iCustNo, wkFacmNo, wkBormNo, wkBorxNo, titaVo);
		tLoanBorTx.setDesc("催收轉列呆帳");
		tLoanBorTx.setEntryDate(iEntryDate);
		tLoanBorTx.setTxAmt(BigDecimal.ZERO);
		tLoanBorTx.setLoanBal(tLoanBorMain.getLoanBal());
		tLoanBorTx.setRate(tLoanBorMain.getStoreRate());
		tLoanBorTx.setIntStartDate(0);
		tLoanBorTx.setIntEndDate(0);
		tLoanBorTx.setRepaidPeriod(0);
		tLoanBorTx.setPrincipal(od.getOvduPrinBal());
		tLoanBorTx.setInterest(od.getOvduIntBal());
		tLoanBorTx.setDelayInt(BigDecimal.ZERO);
		tLoanBorTx.setBreachAmt(od.getOvduBreachBal());
		if (isFirstBorm) {
			tLoanBorTx.setDisplayflag("A"); // 帳務
			isFirstBorm = false;
		} else {
			tLoanBorTx.setDisplayflag("Y");
		}

		// 其他欄位
		tTempVo.putParam("CaseCloseCode", iCaseCloseCode); // 0:正常 1:展期(新額度) 2:借新還舊(同額度) 3:轉催收 4:催收戶本人清償 5:催收戶保證人代償
															// 6:催收戶強制執行
															// 7:轉列呆帳 8:催收部分轉呆
		tTempVo.putParam("ReduceAmt", iReduceAmt); // 減免金額
		tTempVo.putParam("PaidTerms", 0);
		if (wkFireFee.compareTo(BigDecimal.ZERO) > 0) {
			tTempVo.putParam("FireFee", wkFireFee);
		}
		if (wkLawFee.compareTo(BigDecimal.ZERO) > 0) {
			tTempVo.putParam("LawFee", wkLawFee);
		}
		tLoanBorTx.setOtherFields(tTempVo.getJsonString());
		try {
			loanBorTxService.insert(tLoanBorTx);
		} catch (DBException e) {
			throw new LogicException(titaVo, "E0005", "放款交易內容檔 " + e.getErrorMsg() + " Key = " + tLoanBorTxId); // 新增資料時，發生錯誤
		}
	}

	// 新增放款交易內容檔
	private void AddLoanBorTx8Routine(LoanOverdue od) throws LogicException {
		this.info("AddLoanBorTx8Routine ... ");
		// 抓取火險費、法務費轉銷呆帳金額
		getTrfBadUnpaid();
		wkTrfBadAmt = wkTrfBadAmt.subtract(wkFireFee).subtract(wkLawFee);

		wkTrfBreach = BigDecimal.ZERO;
		wkTrfInt = BigDecimal.ZERO;
		wkTrfPrin = BigDecimal.ZERO;
		if (wkTrfBadAmt.compareTo(BigDecimal.ZERO) > 0) {
			if (wkTrfBadAmt.compareTo(od.getOvduBreachBal()) > 0) {
				wkTrfBreach = od.getOvduBreachBal();
				wkTrfBadAmt = wkTrfBadAmt.subtract(wkTrfBreach);
			} else {
				wkTrfBreach = wkTrfBadAmt;
				wkTrfBadAmt = BigDecimal.ZERO;
			}
		}
		if (wkTrfBadAmt.compareTo(BigDecimal.ZERO) > 0) {
			if (wkTrfBadAmt.compareTo(od.getOvduIntBal()) > 0) {
				wkTrfInt = od.getOvduIntBal();
				wkTrfBadAmt = wkTrfBadAmt.subtract(wkTrfInt);
			} else {
				wkTrfInt = wkTrfBadAmt;
				wkTrfBadAmt = BigDecimal.ZERO;
			}
		}
		if (wkTrfBadAmt.compareTo(BigDecimal.ZERO) > 0) {
			if (wkTrfBadAmt.compareTo(od.getOvduPrinBal()) > 0) {
				wkTrfPrin = od.getOvduPrinBal();
				wkTrfBadAmt = wkTrfBadAmt.subtract(wkTrfPrin);
			} else {
				wkTrfPrin = wkTrfBadAmt;
				wkTrfBadAmt = BigDecimal.ZERO;
			}
		}

		tLoanBorTx = new LoanBorTx();
		tLoanBorTxId = new LoanBorTxId();
		loanCom.setLoanBorTx(tLoanBorTx, tLoanBorTxId, iCustNo, wkFacmNo, wkBormNo, wkBorxNo, titaVo);
		tLoanBorTx.setDesc("催收部分轉呆");
		tLoanBorTx.setEntryDate(iEntryDate);
		tLoanBorTx.setTxAmt(BigDecimal.ZERO);
		tLoanBorTx.setLoanBal(tLoanBorMain.getLoanBal());
		tLoanBorTx.setRate(tLoanBorMain.getStoreRate());
		tLoanBorTx.setIntStartDate(0);
		tLoanBorTx.setIntEndDate(0);
		tLoanBorTx.setRepaidPeriod(0);
		tLoanBorTx.setPrincipal(wkTrfPrin);
		tLoanBorTx.setInterest(wkTrfInt);
		tLoanBorTx.setDelayInt(BigDecimal.ZERO);
		tLoanBorTx.setBreachAmt(wkTrfBreach);
		if (isFirstBorm) {
			tLoanBorTx.setDisplayflag("A"); // 帳務
			isFirstBorm = false;
		} else {
			tLoanBorTx.setDisplayflag("Y");
		}
		// 其他欄位
		tTempVo.putParam("CaseCloseCode", iCaseCloseCode);
		tTempVo.putParam("PaidTerms", 0);
		tLoanBorTx.setOtherFields(tTempVo.getJsonString());
		if (wkFireFee.compareTo(BigDecimal.ZERO) > 0) {
			tTempVo.putParam("FireFee", wkFireFee);
		}
		if (wkLawFee.compareTo(BigDecimal.ZERO) > 0) {
			tTempVo.putParam("LawFee", wkLawFee);
		}
		try {
			loanBorTxService.insert(tLoanBorTx);
		} catch (DBException e) {
			throw new LogicException(titaVo, "E0005", "放款交易內容檔 " + e.getErrorMsg() + " Key = " + tLoanBorTxId); // 新增資料時，發生錯誤
		}
	}

	// 新增放款交易內容檔
	private void AddLoanBorTx9Routine(LoanOverdue od) throws LogicException {
		this.info("AddLoanBorTx9Routine ... ");

		tLoanBorTx = new LoanBorTx();
		tLoanBorTxId = new LoanBorTxId();
		loanCom.setLoanBorTx(tLoanBorTx, tLoanBorTxId, iCustNo, wkFacmNo, wkBormNo, wkBorxNo, titaVo);
		tLoanBorTx.setDesc("債權轉讓戶");
		tLoanBorTx.setEntryDate(iEntryDate);
		tLoanBorTx.setTxAmt(BigDecimal.ZERO);
		tLoanBorTx.setLoanBal(tLoanBorMain.getLoanBal());
		tLoanBorTx.setRate(tLoanBorMain.getStoreRate());
		tLoanBorTx.setIntStartDate(0);
		tLoanBorTx.setIntEndDate(0);
		tLoanBorTx.setRepaidPeriod(0);
		tLoanBorTx.setPrincipal(od.getOvduPrinBal());
		tLoanBorTx.setInterest(od.getOvduIntBal());
		tLoanBorTx.setDelayInt(BigDecimal.ZERO);
		tLoanBorTx.setBreachAmt(od.getOvduBreachBal());
		if (isFirstBorm) {
			tLoanBorTx.setDisplayflag("Y");
			isFirstBorm = false;
		} else {
			tLoanBorTx.setDisplayflag("Y");
		}
		// 其他欄位
		tTempVo.putParam("CaseCloseCode", iCaseCloseCode);
		tTempVo.putParam("ReduceAmt", iReduceAmt); // 減免金額
		tTempVo.putParam("PaidTerms", 0);
		tLoanBorTx.setOtherFields(tTempVo.getJsonString());
		try {
			loanBorTxService.insert(tLoanBorTx);
		} catch (DBException e) {
			throw new LogicException(titaVo, "E0005", "放款交易內容檔 " + e.getErrorMsg() + " Key = " + tLoanBorTxId); // 新增資料時，發生錯誤
		}
	}

	// 結案帳務
	private void AcDetailCloseRoutine() throws LogicException {
		this.info("AcDetailCrRoutine ... ");
		this.info("   isBookAcYes = " + this.txBuffer.getTxCom().isBookAcYes());

		if (!this.txBuffer.getTxCom().isBookAcYes()) {
			return;
		}
		// 貸方：費用、短繳期金
		lAcDetail.addAll(lAcDetailFee);

		if (iCaseCloseCode == 0 || iCaseCloseCode == 1 || iCaseCloseCode == 2) {
			AcDetailClose0();
		}
		if (iCaseCloseCode == 3) {
			AcDetailClose3();
		}
		if (iCaseCloseCode == 4 || iCaseCloseCode == 5 || iCaseCloseCode == 6) {
			AcDetailClose4();
		}
	}

	// 0:正常 1:展期-一般 2:展期-協議 帳務處理
	private void AcDetailClose0() throws LogicException {
		// 貸: 放款 = 還款本金
		acDetail = new AcDetail();
		acDetail.setDbCr("C");
		acDetail.setAcctCode(tFacMain.getAcctCode());
		acDetail.setTxAmt(wkPrincipal.subtract(wkShortfallPrincipal)); // 回收短繳另外出帳
		acDetail.setCustNo(wkCustNo);
		acDetail.setFacmNo(wkFacmNo);
		acDetail.setBormNo(wkBormNo);
		lAcDetail.add(acDetail);
		// 貸: 利息
		acDetail = new AcDetail();
		acDetail.setDbCr("C");
		acDetail.setAcctCode(loanCom.setIntAcctCode(tFacMain.getAcctCode()));
		acDetail.setTxAmt(wkInterest.subtract(wkShortfallInterest)); // 回收短繳另外出帳
		acDetail.setCustNo(wkCustNo);
		acDetail.setFacmNo(wkFacmNo);
		acDetail.setBormNo(wkBormNo);
		lAcDetail.add(acDetail);
		// 貸: 延滯息
		acDetail = new AcDetail();
		acDetail.setDbCr("C");
		acDetail.setAcctCode("IOV");
		acDetail.setTxAmt(wkDelayInt);
		acDetail.setCustNo(wkCustNo);
		acDetail.setFacmNo(wkFacmNo);
		acDetail.setBormNo(wkBormNo);
		lAcDetail.add(acDetail);
		// 貸: 違約金
		acDetail = new AcDetail();
		acDetail.setDbCr("C");
		acDetail.setAcctCode("IOP");
		acDetail.setTxAmt(wkBreachAmt);
		acDetail.setCustNo(wkCustNo);
		acDetail.setFacmNo(wkFacmNo);
		acDetail.setBormNo(wkBormNo);
		lAcDetail.add(acDetail);
		// 貸方 清償違約金
		acDetail = new AcDetail();
		acDetail.setDbCr("C");
		acDetail.setAcctCode("IOP");
		acDetail.setTxAmt(wkCloseBreachAmt.subtract(wkShortCloseBreach));// 回收短繳另外出帳
		acDetail.setCustNo(iCustNo);
		acDetail.setFacmNo(iFacmNo);
		acDetail.setBormNo(iBormNo);
		lAcDetail.add(acDetail);
	}

	// 3:轉催收 帳務處理
	private void AcDetailClose3() throws LogicException {
		// 借: 催收款項 = 本金 + 利息
		acDetail = new AcDetail();
		acDetail.setDbCr("D");
		acDetail.setAcctCode("990");
		acDetail.setTxAmt(wkPrincipal.add(wkInterest));
		acDetail.setCustNo(wkCustNo);
		acDetail.setFacmNo(wkFacmNo);
		acDetail.setBormNo(wkBormNo);
		lAcDetail.add(acDetail);
		// 貸: 放款 = 本金
		acDetail = new AcDetail();
		acDetail.setDbCr("C");
		acDetail.setAcctCode(tFacMain.getAcctCode());
		acDetail.setTxAmt(wkPrincipal);
		acDetail.setCustNo(wkCustNo);
		acDetail.setFacmNo(wkFacmNo);
		acDetail.setBormNo(wkBormNo);
		lAcDetail.add(acDetail);
		// 貸: 利息收入
		acDetail = new AcDetail();
		acDetail.setDbCr("C");
		acDetail.setAcctCode(loanCom.setIntAcctCode(tFacMain.getAcctCode()));
		acDetail.setTxAmt(wkInterest);
		acDetail.setCustNo(wkCustNo);
		acDetail.setFacmNo(wkFacmNo);
		acDetail.setBormNo(wkBormNo);
		lAcDetail.add(acDetail);
	}

	// 4:催收戶本人清償 5:催收戶保證人代償 6:催收戶強制執行 帳務處理
	private void AcDetailClose4() throws LogicException {
		this.info("AcDetailDbCr4Routine ... ");

		// 貸: 催收款項
		acDetail = new AcDetail();
		acDetail.setDbCr("C");
		acDetail.setAcctCode("990");
		acDetail.setTxAmt(wkBeforeOvduBal);
		acDetail.setCustNo(iCustNo);
		acDetail.setFacmNo(wkFacmNo);
		acDetail.setBormNo(wkBormNo);
		lAcDetail.add(acDetail);

		// 貸: 利息 (利息 - 轉催收利息)
		acDetail = new AcDetail();
		acDetail.setDbCr("C");
		acDetail.setAcctCode(loanCom.setIntAcctCode(tFacMain.getAcctCode()));
		acDetail.setTxAmt(wkInterest.subtract(wkOvduIntAmt));
		acDetail.setCustNo(wkCustNo);
		acDetail.setFacmNo(wkFacmNo);
		acDetail.setBormNo(wkBormNo);
		lAcDetail.add(acDetail);

		// 貸: 延滯息
		acDetail = new AcDetail();
		acDetail.setDbCr("C");
		acDetail.setAcctCode("IOV");
		acDetail.setTxAmt(wkDelayInt);
		acDetail.setCustNo(wkCustNo);
		acDetail.setFacmNo(wkFacmNo);
		acDetail.setBormNo(wkBormNo);
		lAcDetail.add(acDetail);
		// 貸: 違約金
		acDetail = new AcDetail();
		acDetail.setDbCr("C");
		acDetail.setAcctCode("IOP");
		acDetail.setTxAmt(wkBreachAmt);
		acDetail.setCustNo(wkCustNo);
		acDetail.setFacmNo(wkFacmNo);
		acDetail.setBormNo(wkBormNo);
		lAcDetail.add(acDetail);
	}

	// 轉呆帳帳務處理
	private void AcDetailDbCr7Routine(LoanOverdue od) throws LogicException {
		this.info("AcDetailDbCr7Routine ... ");

		if (!this.txBuffer.getTxCom().isBookAcYes()) {
			return;
		}
		// 借:備抵呆帳
		acDetail = new AcDetail();
		acDetail.setDbCr("D");
		acDetail.setAcctCode("F18");
		acDetail.setTxAmt(od.getOvduBal().add(wkFireFee).add(wkLawFee));
		acDetail.setCustNo(iCustNo);
		acDetail.setFacmNo(wkFacmNo);
		acDetail.setBormNo(wkBormNo);
		lAcDetail.add(acDetail);

		// 貸:火險、法務費
		lAcDetail.addAll(lAcDetailFee);

		// 貸:催收款項
		acDetail = new AcDetail();
		acDetail.setDbCr("C");
		acDetail.setAcctCode("990");
		acDetail.setTxAmt(od.getOvduBal());
		acDetail.setCustNo(iCustNo);
		acDetail.setFacmNo(wkFacmNo);
		acDetail.setBormNo(wkBormNo);
		lAcDetail.add(acDetail);

	}

	// 催收部分轉呆帳帳務處理
	private void AcDetailDbCr8Routine() throws LogicException {
		this.info("AcDetailDbCr7Routine ... ");

		if (!this.txBuffer.getTxCom().isBookAcYes()) {
			return;
		}
		// 借:備抵呆帳
		acDetail = new AcDetail();
		acDetail.setDbCr("D");
		acDetail.setAcctCode("F18");
		acDetail.setTxAmt(wkTrfPrin.add(wkTrfInt).add(wkTrfBreach).add(wkFireFee).add(wkLawFee));
		acDetail.setCustNo(iCustNo);
		acDetail.setFacmNo(wkFacmNo);
		acDetail.setBormNo(wkBormNo);
		lAcDetail.add(acDetail);

		// 貸:火險、法務費
		lAcDetail.addAll(lAcDetailFee);

		// 貸:催收款項
		acDetail = new AcDetail();
		acDetail.setDbCr("C");
		acDetail.setAcctCode("990");
		acDetail.setTxAmt(wkTrfPrin.add(wkTrfInt).add(wkTrfBreach));
		acDetail.setCustNo(iCustNo);
		acDetail.setFacmNo(wkFacmNo);
		acDetail.setBormNo(wkBormNo);
		lAcDetail.add(acDetail);
	}

	// 貸方：費用、短繳期金
	private void getSettleUnpaid() throws LogicException {
		lAcDetailFee = new ArrayList<AcDetail>();
		this.wkAcctFee = BigDecimal.ZERO;
		this.wkModifyFee = BigDecimal.ZERO;
		this.wkFireFee = BigDecimal.ZERO;
		this.wkLawFee = BigDecimal.ZERO;
		this.wkShortfallInterest = BigDecimal.ZERO; // 累短收 - 利息
		this.wkShortfallPrincipal = BigDecimal.ZERO; // 累短收 - 本金
		this.wkShortCloseBreach = BigDecimal.ZERO; // 累短收 - 清償違約金
		// 轉催收不出費用
		if (iCaseCloseCode == 3) {
			return;
		}
		// RepayType 同撥款：01-期款, 第一筆：04-帳管費, 05-火險費, 06-契變手續費, 07-法務費
		if (this.baTxList != null) {
			for (BaTxVo ba : this.baTxList) {
				if (ba.getDataKind() == 1 && ba.getAcctAmt().compareTo(BigDecimal.ZERO) > 0) {
					if ((ba.getFacmNo() == wkFacmNo && ba.getBormNo() == wkBormNo && ba.getRepayType() == 1)
							|| ba.getRepayType() >= 1) {
						acDetail = new AcDetail();
						acDetail.setDbCr("C");
						acDetail.setAcctCode(ba.getAcctCode());
						acDetail.setTxAmt(ba.getAcctAmt());
						acDetail.setCustNo(ba.getCustNo());
						acDetail.setFacmNo(ba.getFacmNo());
						acDetail.setBormNo(ba.getBormNo());
						acDetail.setRvNo(ba.getRvNo());
						acDetail.setReceivableFlag(ba.getReceivableFlag());
						lAcDetailFee.add(acDetail);
						// 短繳
						if (ba.getRepayType() == 1) {
							this.wkShortfallPrincipal = ba.getPrincipal();
							this.wkShortfallInterest = ba.getInterest();
							this.wkShortCloseBreach = ba.getCloseBreachAmt();
						} else if (ba.getRepayType() == 4) {
							this.wkAcctFee = this.wkAcctFee.add(ba.getAcctAmt());
						} else if (ba.getRepayType() == 5) {
							this.wkFireFee = this.wkFireFee.add(ba.getAcctAmt());
						} else if (ba.getRepayType() == 6) {
							this.wkModifyFee = this.wkModifyFee.add(ba.getAcctAmt());
						} else if (ba.getRepayType() == 7) {
							this.wkLawFee = this.wkLawFee.add(ba.getAcctAmt());
						}
						ba.setAcctAmt(BigDecimal.ZERO);
					}
				}
			}
		}
	}

	// 抓取火險費、法務費轉銷呆帳金額
	private void getTrfBadUnpaid() throws LogicException {
		// RepayType 05-火險費 , 07-法務費
		// tAcctCode F25 催收款項－火險費用 , F24 催收款項－法務費用
		lAcDetailFee = new ArrayList<AcDetail>();
		this.wkFireFee = BigDecimal.ZERO;
		this.wkLawFee = BigDecimal.ZERO;
		if (this.baTxList != null) {
			for (BaTxVo ba : this.baTxList) {
				if (ba.getFacmNo() == wkFacmNo && ba.getDataKind() == 1
						&& ba.getAcctAmt().compareTo(BigDecimal.ZERO) > 0) {
					if (("Y".equals(iFireFg) && ba.getRepayType() == 5 && !"F25".equals(ba.getAcctCode()))
							|| ("Y".equals(iCollFireFg) && ba.getRepayType() == 5 && "F25".equals(ba.getAcctCode()))
							|| ("Y".equals(iLawFg) && ba.getRepayType() == 7 && !"F24".equals(ba.getAcctCode()))
							|| ("Y".equals(iCollLawFg) && ba.getRepayType() == 7 && "F24".equals(ba.getAcctCode()))) {
						acDetail = new AcDetail();
						acDetail.setDbCr("C");
						acDetail.setAcctCode(ba.getAcctCode());
						acDetail.setTxAmt(ba.getAcctAmt());
						acDetail.setCustNo(ba.getCustNo());
						acDetail.setFacmNo(ba.getFacmNo());
						acDetail.setBormNo(ba.getBormNo());
						acDetail.setRvNo(ba.getRvNo());
						acDetail.setReceivableFlag(ba.getReceivableFlag());
						lAcDetailFee.add(acDetail);
						if (ba.getRepayType() == 5) {
							this.wkFireFee = this.wkFireFee.add(ba.getAcctAmt());
						} else {
							this.wkLawFee = this.wkLawFee.add(ba.getAcctAmt());
						}
						ba.setAcctAmt(BigDecimal.ZERO);
					}
				}
			}
		}
	}

	// 業績處理
	private void PfDetailRoutine() throws LogicException {
		this.info("PfDetailRoutine ... " + iCaseCloseCode + " " + iEntryDate + " " + tLoanBorMain.getMaturityDate());
		if (iCaseCloseCode != 0) { // 結案區分 0:正常結案
			return;
		}
		if (iEntryDate >= tLoanBorMain.getMaturityDate()) { // 提前結案
			return;
		}
		PfDetailVo pf = new PfDetailVo();
		pf.setCustNo(iCustNo); // 借款人戶號
		pf.setFacmNo(wkFacmNo); // 額度編號
		pf.setBormNo(wkBormNo); // 撥款序號
		pf.setBorxNo(wkBorxNo); // 交易內容檔序號
		pf.setPieceCode(tLoanBorMain.getPieceCode()); // 計件代碼
		pf.setRepayType(3); // 還款類別 0.撥款 2.部分償還 3.提前結案
		pf.setDrawdownAmt(wkExtraRepay);// 撥款金額/追回金額
		pf.setDrawdownDate(tLoanBorMain.getDrawdownDate());// 撥款日期
		pf.setRepaidPeriod(tLoanBorMain.getRepaidPeriod()); // 已攤還期數
		// 產生業績明細
		pfDetailCom.setTxBuffer(this.getTxBuffer());
		pfDetailCom.addDetail(pf, titaVo);
	}

	// 清償作業檔處理
	private void FacCloseRoutine() throws LogicException {
		// 結案區分 0:正常 1:展期-一般 2:展期-協議 3:轉催收 4:催收戶本人清償 5:催收戶保證人代償 6:催收戶強制執行 7:轉列呆帳
		// 8:催收部分轉呆 9:債權轉讓戶
		wkCloseNo = 0;

		if (iCaseCloseCode != 0 && iCaseCloseCode != 4 && iCaseCloseCode != 5 && iCaseCloseCode != 6) {
			return;
		}

		if (isAllClose) {
			if (titaVo.isHcodeNormal()) {
				FacCloseNormal();
			} else {
				FacCloseErase();
			}
		}
	}

	private void FacCloseNormal() throws LogicException {
		// 0:清償(必須為尚未結案)
		tFacClose = facCloseService.findFacmNoFirst(iCustNo, iFacmNo, Arrays.asList(new String[] { "0" }), titaVo);
		if (tFacClose == null) {
			FacClose t2FacClose = facCloseService.findMaxCloseNoFirst(iCustNo);
			if (t2FacClose == null) {
				wkCloseNo = 1;
			} else {
				wkCloseNo = t2FacClose.getCloseNo() + 1;
			}
			tFacClose = new FacClose();
			tFacClose.setFacCloseId(new FacCloseId(iCustNo, wkCloseNo));
			tFacClose.setCustNo(iCustNo);
			tFacClose.setCloseNo(wkCloseNo);
			tFacClose.setFacmNo(iFacmNo);
			tFacClose.setApplDate(0);// 申請日期自動寫入時為0
			tFacClose.setFunCode("0");
			tFacClose.setCloseDate(iEntryDate);
			tFacClose.setEntryDate(iEntryDate);
			tFacClose.setCloseReasonCode(iAdvanceCloseCode);
			tFacClose.setCloseAmt(iRealRepayAmt);
			try {
				facCloseService.insert(tFacClose);
			} catch (DBException e) {
				throw new LogicException(titaVo, "E0005", "清償作業檔 Key = " + iCustNo + wkCloseNo); // 新增資料時，發生錯誤 }
			}
		} else {
			tFacClose = facCloseService.holdById(tFacClose, titaVo);
			tFacClose.setCloseDate(iEntryDate);
			try {
				facCloseService.update(tFacClose);
			} catch (DBException e) {
				throw new LogicException(titaVo, "E0005", "清償作業檔 Key = " + iCustNo + wkCloseNo); // 新增資料時，發生錯誤 }
			}
		}

	}

	private void FacCloseErase() throws LogicException {
		tFacClose = facCloseService.findFacmNoFirst(iCustNo, iFacmNo, Arrays.asList(new String[] { "0" }), titaVo);
		if (tFacClose == null) {
			return;
		}
		wkCloseNo = tFacClose.getCloseNo();
		tFacClose = facCloseService.holdById(new FacCloseId(iCustNo, wkCloseNo), titaVo);
		// 申請日期自動寫入時為0
		if (tFacClose.getApplDate() == 0) {
			try {
				facCloseService.delete(tFacClose);
			} catch (DBException e) {
				throw new LogicException(titaVo, "E0005", "清償作業檔 Key = " + iCustNo + wkCloseNo); // 新增資料時，發生錯誤 }
			}
		} else {
			tFacClose = facCloseService.holdById(tFacClose, titaVo);
			tFacClose.setCloseDate(0);
			try {
				facCloseService.update(tFacClose);
			} catch (DBException e) {
				throw new LogicException(titaVo, "E0005", "清償作業檔 Key = " + iCustNo + wkCloseNo); // 新增資料時，發生錯誤 }
			}
		}
	}

	// 兌現票入帳處理
	private void LoanChequeRoutine() throws LogicException {
		this.info("LoanCheckRoutine ...");
		this.info("   iRpCode = " + iRpCode);

		if (iRpCode == 4) {
			acPaymentCom.setTxBuffer(this.getTxBuffer());
			acPaymentCom.loanCheque(titaVo);
		}
	}
}