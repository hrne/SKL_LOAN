package com.st1.itx.trade.L3;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
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
import com.st1.itx.db.service.LoanIntDetailService;
import com.st1.itx.db.service.LoanOverdueService;
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.common.AcRepayCom;
import com.st1.itx.util.common.BaTxCom;
import com.st1.itx.util.common.LoanAvailableAmt;
import com.st1.itx.util.common.LoanCalcRepayIntCom;
import com.st1.itx.util.common.LoanCom;
import com.st1.itx.util.common.LoanSetRepayIntCom;
import com.st1.itx.util.common.PfDetailCom;
import com.st1.itx.util.common.SendRsp;
import com.st1.itx.util.common.data.BaTxVo;
import com.st1.itx.util.common.data.CalcRepayIntVo;
import com.st1.itx.util.common.data.PfDetailVo;
import com.st1.itx.util.data.DataLog;
import com.st1.itx.util.date.DateUtil;
import com.st1.itx.util.parse.Parse;

/*
 * L3420 結案登錄-不可欠繳
 * a.正常結案時不可短繳
 * b.展期時,需輸入新核准號碼
 * c.原則上舊額度之期款利息應繳齊才可辦展期。
 * d.展期時,需以整張額度作業，不可單一撥款或戶號處理。
 * e.[借新還舊]交易登打順序必須先執行結案後再執行新撥貸。
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
	public FacCloseService facCloseService;

	@Autowired
	Parse parse;
	@Autowired
	SendRsp sendRsp;
	@Autowired
	DataLog datalog;
	@Autowired
	AcRepayCom acRepayCom;
	@Autowired
	private BaTxCom baTxCom;
	@Autowired
	LoanCom loanCom;
	@Autowired
	private LoanSetRepayIntCom loanSetRepayIntCom;
	@Autowired
	private LoanCalcRepayIntCom loanCalcRepayIntCom;
	@Autowired
	private PfDetailCom pfDetailCom;
	@Autowired
	private LoanAvailableAmt loanAvailableAmt;
	@Autowired
	DateUtil dDateUtil;

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
	private String iNote = "";

	private BigDecimal iShortfallInt;
	private BigDecimal iShortfallPrin;
	private BigDecimal iShortCloseBreach;
	private BigDecimal iCloseBreachAmt;
	private int iOverRpFg; // 1.短收 2.溢收
	private BigDecimal iOverAmt; // 溢收金額
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
	private int renewCnt = 0;
	private int oldFacmNo = 0;
	private int wkTotaCount = 0;
	private int wkIntStartDate = 9991231;
	private int wkIntEndDate = 0;
	private int wkPaidTerms = 0;
	private int wkBorMainStatus = 88;
	private int wkDueDate = 0;
	private int wkCloseNo = 0;

	private BigDecimal wkAfterLoanBal = BigDecimal.ZERO;
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
	private BigDecimal wkTrfBreach = BigDecimal.ZERO;
	private BigDecimal wkTrfInt = BigDecimal.ZERO;
	private BigDecimal wkTrfPrin = BigDecimal.ZERO;
	private BigDecimal wkFireFee = BigDecimal.ZERO;
	private BigDecimal wkLawFee = BigDecimal.ZERO;
	private BigDecimal wkExtraRepay = BigDecimal.ZERO;
	private BigDecimal wkShortfallPrincipal = BigDecimal.ZERO; // 累短收 - 本金
	private BigDecimal wkShortfallInterest = BigDecimal.ZERO; // 累短收-利息
	private BigDecimal wkShortCloseBreach = BigDecimal.ZERO; // 累短收 - 清償違約金
	private BigDecimal wkShortfall = BigDecimal.ZERO; // 累短繳金額
	private DecimalFormat df = new DecimalFormat("##,###,###,###,##0");
	private TempVo tTempVo = new TempVo();
	private FacMain tFacMain;
	private FacMain tOldFacMain;
	private FacMain tNewFacMain;
	private LoanBorMain tLoanBorMain;
	private LoanBorTx tLoanBorTx;
	private LoanBorTxId tLoanBorTxId;
	private LoanOverdue tLoanOverdue;
	private LoanOverdueId tLoanOverdueId;
	private LoanIntDetail tLoanIntDetail;
	private LoanIntDetailId tLoanIntDetailId;
	private FacClose tFacClose;
	private List<LoanOverdue> lLoanOverdue = new ArrayList<LoanOverdue>();
	private List<LoanBorMain> lLoanBorMain;
	private List<LoanBorTx> lLoanBorTx = new ArrayList<LoanBorTx>();
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
		if (titaVo.get("Description") != null) {
			iNote = titaVo.getParam("Description");// 新增摘要-董事會通過核定日期訊息
		}
		iNewApplNo = this.parse.stringToInteger(titaVo.getParam("NewApplNo"));
		iNewFacmNo = this.parse.stringToInteger(titaVo.getParam("NewFacmNo"));
		iRenewCode = this.parse.stringToInteger(titaVo.getParam("RenewCode"));
		iAdvanceCloseCode = titaVo.getParam("AdvanceCloseCode");
		iReduceAmt = this.parse.stringToBigDecimal(titaVo.getParam("TimReduceAmt"));
		iTrfBadAmt = this.parse.stringToBigDecimal(titaVo.getParam("TimTrfBadAmt"));
		iTotalRepayAmt = this.parse.stringToBigDecimal(titaVo.getParam("TotalRepayAmt"));
		iRealRepayAmt = this.parse.stringToBigDecimal(titaVo.getParam("RealRepayAmt"));
		iRqspFlag = titaVo.getParam("RqspFlag");
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
		iOverRpFg = this.parse.stringToInteger(titaVo.getParam("OverRpFg")); // 1->短收 2->溢收
		iOverAmt = this.parse.stringToBigDecimal(titaVo.getParam("OverRpAmt"));
		// 不可有短繳金額
		if (iOverRpFg == 1 && iOverAmt.compareTo(BigDecimal.ZERO) > 0) {
			throw new LogicException(titaVo, "E3094", "短繳金額 = " + iOverAmt); // 不可有短繳金額
		}

		// 還款來源
		iRpCode = this.parse.stringToInteger(titaVo.getParam("RpCode1"));

		// 放款交易明細檔的交易金額為實際支付金額
		iTxAmt = iRealRepayAmt;

		// 系統交易記錄檔的金額為實際支付金額或支付總金額
		if (iTxAmt.compareTo(BigDecimal.ZERO) > 0) {
			titaVo.setTxAmt(iTxAmt);
		} else if (iTrfBadAmt.compareTo(BigDecimal.ZERO) > 0) {
			titaVo.setTxAmt(iTrfBadAmt);
		} else {
			titaVo.setTxAmt(iTotalRepayAmt);
		}

		// Check Input
		checkInputRoutine();

		// 檢查到同戶帳務交易需由最近一筆交易開始訂正
		if (titaVo.isHcodeErase()) {
			loanCom.checkEraseCustNoTxSeqNo(iCustNo, titaVo);
		}

		// 展期處理
		if (iCaseCloseCode == 1) {
			facRenew(titaVo);
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

		// 設定額度撥款起止序號
		if (iFacmNo > 0) {
			wkFacmNoStart = iFacmNo;
			wkFacmNoEnd = iFacmNo;
		}
		if (iBormNo > 0) {
			wkBormNoStart = iBormNo;
			wkBormNoEnd = iBormNo;
		}

		// 費用、短繳期金
		if (titaVo.isHcodeNormal()) {
			batxSettleUnpaid();
		}

		// put Batch Tita
		if (titaVo.isHcodeNormal() && titaVo.isTrmtypBatch()) {
			putBatchTita();
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

		// 清償作業檔處理
		FacCloseRoutine();

		// 更新疑似洗錢交易訪談記錄檔
		loanCom.updateMlaundryRecord(iCustNo, iFacmNo, iBormNo, iEntryDate, iTxAmt, titaVo);

		// 帳務處理
		if (iCaseCloseCode < 9) {
			acRepayCom.setTxBuffer(this.txBuffer);
			acRepayCom.settleRun(this.lLoanBorTx, this.baTxList, titaVo);
		}

		// Check output
		checkOutputRoutine();

		// end
		this.addList(this.totaVo);
		return this.sendList();
	}

	private void checkInputRoutine() throws LogicException {

	}

	private void checkOutputRoutine() throws LogicException {

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

	// 未計息排前面
	private int getCalcBorm(LoanBorMain ln) {
		int isCalcBorm = 0;
		for (int i = 1; i <= 20; i++) {
			if (titaVo.get("FacmBormNo" + i) != null
					&& Integer.parseInt(titaVo.get("FacmBormNo" + i).substring(0, 3)) > 0) {
				if (ln.getFacmNo() == Integer.parseInt(titaVo.get("FacmBormNo" + i).substring(0, 3))
						&& ln.getBormNo() == Integer.parseInt(titaVo.get("FacmBormNo" + i).substring(4, 7))) {
					isCalcBorm = 1;
					break;
				}
			}

		}
		return isCalcBorm;
	}

	// 0:正常 1:展期-一般 2:展期-協議 3:轉催收 4:催收戶本人清償 5:催收戶保證人代償 6:催收戶強制執行
	private void CaseClose0NormalRoutine() throws LogicException {
		this.info("CaseClose0NormalRoutine ...");

		Slice<LoanBorMain> slLoanBorMain = loanBorMainService.bormCustNoEq(iCustNo, wkFacmNoStart, wkFacmNoEnd,
				wkBormNoStart, wkBormNoEnd, 0, Integer.MAX_VALUE);
		lLoanBorMain = slLoanBorMain == null ? null : new ArrayList<LoanBorMain>(slLoanBorMain.getContent());
		if (lLoanBorMain == null || lLoanBorMain.size() == 0) {
			throw new LogicException(titaVo, "E0001", "放款主檔"); // 查詢資料不存在
		}
		Collections.sort(lLoanBorMain, new Comparator<LoanBorMain>() {
			public int compare(LoanBorMain c1, LoanBorMain c2) {
				// 沒計息排前面
				if (getCalcBorm(c1) != getCalcBorm(c2)) {
					return getCalcBorm(c1) - getCalcBorm(c2);
				}
				if (c1.getFacmNo() != c2.getFacmNo()) {
					return c1.getFacmNo() - c2.getFacmNo();
				}
				if (c1.getBormNo() != c2.getBormNo()) {
					return c1.getBormNo() - c2.getBormNo();
				}
				return 0;
			}
		});
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
			wkOvduIntAmt = BigDecimal.ZERO; // 轉催收利息
			if (iCaseCloseCode == 4 || iCaseCloseCode == 5 || iCaseCloseCode == 6) {
				tLoanOverdue = loanOverdueService.holdById(new LoanOverdueId(iCustNo, wkFacmNo, wkBormNo, wkOvduNo));
				if (tLoanOverdue == null) {
					throw new LogicException(titaVo, "E0006", "催收呆帳檔 Key = " + tLoanOverdueId); // 鎖定資料時，發生錯誤
				}
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

			// 清償違約金
			wkCloseBreachAmt = BigDecimal.ZERO;
			if (iCloseBreachAmt.compareTo(BigDecimal.ZERO) > 0) {
				for (int i = 1; i <= 50; i++) {
					if (titaVo.get("FacmBormNo" + i) != null) {
						if (titaVo.get("FacmBormNo" + i).equals(
								parse.IntegerToString(wkFacmNo, 3) + "-" + parse.IntegerToString(wkBormNo, 3))) {
							wkCloseBreachAmt = parse.stringToBigDecimal(titaVo.get("CloseBreachAmt" + i));
							break;
						}
					}
				}
			}

			// 轉催收，限本金、利息
			if (iCaseCloseCode == 3) {
				wkDelayInt = BigDecimal.ZERO;
				wkBreachAmt = BigDecimal.ZERO;
			} else
			// 催收結案，除轉催收利息外；利息、延滯息、違約金息均以違約金列帳
			if (iCaseCloseCode == 4 || iCaseCloseCode == 5 || iCaseCloseCode == 6) {
				wkBreachAmt = wkBreachAmt.add(wkInterest).add(wkDelayInt).subtract(wkOvduIntAmt);
				wkInterest = BigDecimal.ZERO;
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
			wkPaidTerms = loanCalcRepayIntCom.getPaidTerms();
			wkTotalRepayAmt = wkTotalRepayAmt.add(wkPrincipal).add(wkInterest).add(wkDelayInt).add(wkBreachAmt);

			// 提前還款金額
			if (iCaseCloseCode == 0 || iCaseCloseCode == 4 || iCaseCloseCode == 5 || iCaseCloseCode == 6) {
				wkExtraRepay = loanCalcRepayIntCom.getExtraAmt();
			} else {
				wkExtraRepay = BigDecimal.ZERO;
			}

			wkTotaCount++;

			// 短繳收回
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
			if (wkBormNo == 0) {
				// 註記交易內容檔(費用)
				loanCom.setFacmBorTxHcode(iCustNo, wkFacmNo, wkBorxNo, titaVo);
			} else {
				// 還原金額處理
				wkPrincipal = this.parse.stringToBigDecimal(tTempVo.get("LoanBal"));
				wkExtraRepay = tx.getExtraRepay();
				// 更新額度主檔
				UpdFacMainRoutine();
				// 還原撥款主檔
				RestoredLoanBorMainRoutine();
				// 註記交易內容檔
				loanCom.setLoanBorTxHcode(wkCustNo, wkFacmNo, wkBormNo, wkBorxNo, wkNewBorxNo,
						tx.getLoanBal().add(tx.getPrincipal()), titaVo);
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
	}

	// 7:轉列呆帳
	private void CaseClose7NormalRoutine() throws LogicException {
		this.info("CaseClose7NormalRoutine ...");
		// 火險費、法務費轉銷呆帳金額
		getTrfBadUnpaid();
		wkTrfBadAmt = wkTrfBadAmt.subtract(wkFireFee).subtract(wkLawFee);

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
			// 更新催收呆帳檔
			UpdLoanOvdu7Routine(od);
		}
	}

	// 8:催收部分轉呆
	private void CaseClose8NormalRoutine() throws LogicException {
		this.info("CaseClose8NormalRoutine ...");
		// 火險費、法務費轉銷呆帳金額
		getTrfBadUnpaid();
		wkTrfBadAmt = wkTrfBadAmt.subtract(wkFireFee).subtract(wkLawFee);

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
			if (wkTrfBadAmt.compareTo(BigDecimal.ZERO) == 0) {
				break;
			}
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
			// 計算轉呆帳金額
			CaculateTrfAmt8Routine(od);
			// 新增放款交易內容檔
			AddLoanBorTx8Routine(od);
			// 更新額度主檔
			UpdFacMainRoutine();
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
			if (tFacMain.getUtilAmt().compareTo(BigDecimal.ZERO) == 0) {
				isAllClose = true;
			}
			tFacMain.setAdvanceCloseCode(parse.stringToInteger(iAdvanceCloseCode));
		} else {
			if (tFacMain.getUtilAmt().compareTo(BigDecimal.ZERO) == 0) {
				isAllClose = true;
			}
			tFacMain.setUtilAmt(tFacMain.getUtilAmt().add(wkPrincipal));
			if (tFacMain.getRecycleCode().equals("1")) {
				tFacMain.setUtilBal(tFacMain.getUtilBal().add(wkPrincipal));
			}
			tFacMain.setAdvanceCloseCode(0);
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

		// tLoanOverdue.setBadDebtDate(wkTbsDy);
		tLoanOverdue.setBadDebtDate(parse.stringToInteger(titaVo.getParam("BadDebtDateN")));// 改放轉呆時畫面輸入的董事會通過核定日期
		tLoanOverdue.setBadDebtAmt(od.getBadDebtAmt().add(od.getOvduBal()));
		tLoanOverdue.setBadDebtBal(od.getBadDebtBal().add(od.getOvduBal()));
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

//		if (tLoanOverdue.getBadDebtDate() == 0) {
//			tLoanOverdue.setBadDebtDate(wkTbsDy);
//		}
		tLoanOverdue.setBadDebtDate(parse.stringToInteger(titaVo.getParam("BadDebtDateN")));// 改放部分轉呆時畫面輸入的董事會通過核定日期
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
		tLoanBorTx.setTxDescCode("342" + iCaseCloseCode);
		if (iCaseCloseCode >= 4) {
			tLoanBorTx.setAcctCode("990");
		} else {
			tLoanBorTx.setAcctCode(tFacMain.getAcctCode());
		}
		tLoanBorTx.setRepayCode(iRpCode); // 還款來源
		tLoanBorTx.setEntryDate(iEntryDate);
		tLoanBorTx.setDueDate(wkDueDate);
		tLoanBorTx.setLoanBal(BigDecimal.ZERO);
		tLoanBorTx.setRate(tLoanBorMain.getStoreRate());
		tLoanBorTx.setIntStartDate(wkIntStartDate);
		tLoanBorTx.setIntEndDate(wkIntEndDate);
		tLoanBorTx.setPaidTerms(0);
		tLoanBorTx.setPrincipal(wkPrincipal);
		tLoanBorTx.setInterest(wkInterest);
		tLoanBorTx.setDelayInt(wkDelayInt);
		tLoanBorTx.setBreachAmt(wkBreachAmt);
		tLoanBorTx.setCloseBreachAmt(wkCloseBreachAmt);
		tLoanBorTx.setExtraRepay(wkExtraRepay);
		tLoanBorTx.setUnpaidPrincipal(BigDecimal.ZERO);
		tLoanBorTx.setUnpaidInterest(BigDecimal.ZERO);
		tLoanBorTx.setShortfall(wkShortfall); // 累短收收回金額

		// 繳息首筆、繳息次筆
		if (isFirstBorm) {
			tLoanBorTx.setDisplayflag("F"); // 繳息首筆
		} else {
			tLoanBorTx.setDisplayflag("I"); // 繳息次筆
		}

		/* OtherFields */
		// 交易總金額
		if (iTxAmt.compareTo(BigDecimal.ZERO) > 0) {
			tTempVo.putParam("TxAmt", iTxAmt);
		}
		tTempVo.putParam("CaseCloseCode", iCaseCloseCode);
		tTempVo.putParam("Note", iNote);
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

		// 支票繳款
		if (iRpCode == 4) {
			tTempVo.putParam("ChequeAmt", titaVo.get("ChequeAmt"));
			tTempVo.putParam("ChequeAcctNo", titaVo.get("ChequeAcctNo"));
			tTempVo.putParam("ChequeNo", titaVo.get("ChequeNo"));
			// 利息免印花稅
			tTempVo.putParam("StampFreeAmt", wkInterest.add(wkDelayInt).add(wkBreachAmt).add(wkCloseBreachAmt));
		}

		if (titaVo.getBacthNo().trim() != "") {
			tTempVo.putParam("BatchNo", titaVo.getBacthNo()); // 整批批號
			tTempVo.putParam("DetailSeq", titaVo.get("RpDetailSeq1")); // 明細序號
			tTempVo.putParam("ReconCode", titaVo.get("RpAcctCode1")); // 對帳類別
			tTempVo.putParam("DscptCode", titaVo.get("RpDscpt1")); // 摘要代碼
		}

		tLoanBorTx.setOtherFields(tTempVo.getJsonString());

		try {
			loanBorTxService.insert(tLoanBorTx, titaVo);
		} catch (DBException e) {
			throw new LogicException(titaVo, "E0005", "放款交易內容檔 " + e.getErrorMsg() + " Key = " + tLoanBorTxId); // 新增資料時，發生錯誤
		}
		this.lLoanBorTx.add(tLoanBorTx);
	}

	// 新增放款交易內容檔
	private void AddLoanBorTx7Routine(LoanOverdue od) throws LogicException {
		this.info("AddLoanBorTx7Routine ... ");

		tLoanBorTx = new LoanBorTx();
		tLoanBorTxId = new LoanBorTxId();
		loanCom.setLoanBorTx(tLoanBorTx, tLoanBorTxId, iCustNo, wkFacmNo, wkBormNo, wkBorxNo, titaVo);
		tLoanBorTx.setRepayCode(iRpCode); // 還款來源
		tLoanBorTx.setTxDescCode("340" + iCaseCloseCode);
		tLoanBorTx.setEntryDate(iEntryDate);
		tLoanBorTx.setLoanBal(BigDecimal.ZERO);
		tLoanBorTx.setAcctCode("990");
		tLoanBorTx.setPrincipal(od.getOvduBal());
		tLoanBorTx.setDisplayflag("A"); // 帳務

		// 其他欄位
		tTempVo.putParam("CaseCloseCode", iCaseCloseCode);
		tTempVo.putParam("Note", iNote);
		tTempVo.putParam("ReduceAmt", iReduceAmt); // 減免金額
		tLoanBorTx.setOtherFields(tTempVo.getJsonString());

		try {
			loanBorTxService.insert(tLoanBorTx, titaVo);
		} catch (DBException e) {
			throw new LogicException(titaVo, "E0005", "放款交易內容檔 " + e.getErrorMsg() + " Key = " + tLoanBorTxId); // 新增資料時，發生錯誤
		}
		this.lLoanBorTx.add(tLoanBorTx);
	}

	// 計算轉呆帳金額
	private void CaculateTrfAmt8Routine(LoanOverdue od) throws LogicException {
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
	}

	// 新增放款交易內容檔
	private void AddLoanBorTx8Routine(LoanOverdue od) throws LogicException {
		this.info("AddLoanBorTx8Routine ... ");
		tLoanBorTx = new LoanBorTx();
		tLoanBorTxId = new LoanBorTxId();
		loanCom.setLoanBorTx(tLoanBorTx, tLoanBorTxId, iCustNo, wkFacmNo, wkBormNo, wkBorxNo, titaVo);
		tLoanBorTx.setRepayCode(iRpCode); // 還款來源
		tLoanBorTx.setTxDescCode("340" + iCaseCloseCode);
		tLoanBorTx.setEntryDate(iEntryDate);
		tLoanBorTx.setAcctCode("990");
		tLoanBorTx.setLoanBal(od.getOvduBal().subtract(wkTrfPrin.add(wkTrfInt).add(wkTrfBreach)));
		tLoanBorTx.setDisplayflag("A");
		tLoanBorTx.setPrincipal(wkTrfPrin.add(wkTrfInt).add(wkTrfBreach));
		// 其他欄位
		tTempVo.putParam("CaseCloseCode", iCaseCloseCode);
		tTempVo.putParam("Note", iNote);
		tTempVo.putParam("PaidTerms", 0);
		tLoanBorTx.setOtherFields(tTempVo.getJsonString());

		try {
			loanBorTxService.insert(tLoanBorTx, titaVo);
		} catch (DBException e) {
			throw new LogicException(titaVo, "E0005", "放款交易內容檔 " + e.getErrorMsg() + " Key = " + tLoanBorTxId); // 新增資料時，發生錯誤
		}
		this.lLoanBorTx.add(tLoanBorTx);
	}

	// 新增放款交易內容檔
	private void AddLoanBorTx9Routine(LoanOverdue od) throws LogicException {
		this.info("AddLoanBorTx9Routine ... ");

		tLoanBorTx = new LoanBorTx();
		tLoanBorTxId = new LoanBorTxId();
		loanCom.setLoanBorTx(tLoanBorTx, tLoanBorTxId, iCustNo, wkFacmNo, wkBormNo, wkBorxNo, titaVo);
		tLoanBorTx.setRepayCode(iRpCode); // 還款來源
		tLoanBorTx.setTxDescCode("340" + iCaseCloseCode);
		tLoanBorTx.setEntryDate(iEntryDate);
		tLoanBorTx.setAcctCode("990");
		tLoanBorTx.setLoanBal(BigDecimal.ZERO);
		tLoanBorTx.setDisplayflag("Y");
		// 其他欄位
		tTempVo.putParam("CaseCloseCode", iCaseCloseCode);
		tTempVo.putParam("Note", iNote);
		tTempVo.putParam("ReduceAmt", iReduceAmt); // 減免金額
		tTempVo.putParam("PaidTerms", 0);
		tLoanBorTx.setOtherFields(tTempVo.getJsonString());

		try {
			loanBorTxService.insert(tLoanBorTx, titaVo);
		} catch (DBException e) {
			throw new LogicException(titaVo, "E0005", "放款交易內容檔 " + e.getErrorMsg() + " Key = " + tLoanBorTxId); // 新增資料時，發生錯誤
		}
		this.lLoanBorTx.add(tLoanBorTx);
	}

	// 費用、短繳期金
	private void batxSettleUnpaid() throws LogicException {
		// call 應繳試算
		this.baTxList = baTxCom.settingUnPaid(iEntryDate, iCustNo, iFacmNo, iBormNo, 99, iTxAmt, titaVo); // 99-費用全部(含未到期)
		if (iCaseCloseCode >= 7 || iCaseCloseCode == 3) {
			if (this.baTxList != null) {
				for (BaTxVo ba : this.baTxList) {
					if (ba.getDataKind() == 1 && ba.getRepayType() >= 4) {
						ba.setAcctAmt(BigDecimal.ZERO);
					}
				}
			}
		}
	}

	// 貸方：短繳
	private void getSettleUnpaid() throws LogicException {
		this.wkShortfallInterest = BigDecimal.ZERO; // 累短收 - 利息
		this.wkShortfallPrincipal = BigDecimal.ZERO; // 累短收 - 本金
		this.wkShortCloseBreach = BigDecimal.ZERO; // 累短收 - 清償違約金
		this.wkShortfall = BigDecimal.ZERO;
		// 轉催收不出費用
		if (iCaseCloseCode == 3) {
			return;
		}
		// RepayType 同撥款：01-期款, 第一筆：04-帳管費, 05-火險費, 06-契變手續費, 07-法務費
		if (this.baTxList != null) {
			for (BaTxVo ba : this.baTxList) {
				if (ba.getDataKind() == 1 && ba.getRepayType() <= 3 && ba.getAcctAmt().compareTo(BigDecimal.ZERO) > 0
						&& ba.getUnPaidAmt().compareTo(BigDecimal.ZERO) > 0) {
					if ((ba.getFacmNo() == wkFacmNo || ba.getFacmNo() == 0)
							&& (ba.getBormNo() == wkBormNo || ba.getBormNo() == 0)) {
						this.wkShortfall = this.wkShortfallInterest.add(ba.getAcctAmt());
						this.wkShortfallPrincipal = this.wkShortfallPrincipal.add(ba.getPrincipal());
						this.wkShortfallInterest = this.wkShortfallInterest.add(ba.getInterest());
						this.wkShortCloseBreach = this.wkShortCloseBreach.add(ba.getCloseBreachAmt());
						ba.setUnPaidAmt(BigDecimal.ZERO);
					}
				}
			}
		}
		this.info("wkShortfallInterest=" + wkShortfallInterest + ", wkShortfallPrincipal=" + wkShortfallPrincipal
				+ ", wkShortCloseBreach=" + wkShortCloseBreach);
	}

	// 抓取火險費、法務費轉銷呆帳金額
	private void getTrfBadUnpaid() throws LogicException {
		// RepayType 05-火險費 , 07-法務費
		// tAcctCode F25 催收款項－火險費用 , F24 催收款項－法務費用
		this.wkFireFee = BigDecimal.ZERO;
		this.wkLawFee = BigDecimal.ZERO;
		if (this.baTxList != null) {
			for (BaTxVo ba : this.baTxList) {
				if (ba.getDataKind() == 1 && ba.getUnPaidAmt().compareTo(BigDecimal.ZERO) > 0) {
					if (("Y".equals(iFireFg) && ba.getRepayType() == 5 && !"F25".equals(ba.getAcctCode()))
							|| ("Y".equals(iCollFireFg) && ba.getRepayType() == 5 && "F25".equals(ba.getAcctCode()))
							|| ("Y".equals(iLawFg) && ba.getRepayType() == 7 && !"F24".equals(ba.getAcctCode()))
							|| ("Y".equals(iCollLawFg) && ba.getRepayType() == 7 && "F24".equals(ba.getAcctCode()))) {

						ba.setAcctAmt(ba.getUnPaidAmt());
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

		// 結清時判斷該戶號額度下主要擔保品的其他額度是否已全部結清
		if (isAllClose) {
			isAllClose = loanAvailableAmt.isAllCloseClFac(iCustNo, iFacmNo, titaVo);
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
			tFacClose.setCloseInd(parse.IntegerToString(iCaseCloseCode, 1));
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
				facCloseService.delete(tFacClose, titaVo);
			} catch (DBException e) {
				throw new LogicException(titaVo, "E0005", "清償作業檔 Key = " + iCustNo + wkCloseNo); // 新增資料時，發生錯誤 }
			}
		} else {
			tFacClose = facCloseService.holdById(tFacClose, titaVo);
			tFacClose.setCloseDate(0);
			try {
				facCloseService.update(tFacClose, titaVo);
			} catch (DBException e) {
				throw new LogicException(titaVo, "E0005", "清償作業檔 Key = " + iCustNo + wkCloseNo); // 新增資料時，發生錯誤 }
			}
		}
	}

	// 展期處理
	private void facRenew(TitaVo titaVo) throws LogicException {
		tOldFacMain = new FacMain();
		tNewFacMain = new FacMain();
		renewCnt = 0;
		oldFacmNo = 0;
//		取舊額度的展期次數+1擺進新額度的展期次數
//		舊額度擺進新額度的舊額度編號
		if (titaVo.isHcodeNormal()) {
			tOldFacMain = facMainService.findById(new FacMainId(iCustNo, iFacmNo), titaVo);
			if (tOldFacMain != null) {
				renewCnt = tOldFacMain.getRenewCnt() + 1;
				oldFacmNo = iFacmNo;
			}
			tNewFacMain = facMainService.holdById(new FacMainId(iCustNo, iNewFacmNo), titaVo);
			if (tNewFacMain != null) {
				tNewFacMain.setRenewCnt(renewCnt);
				tNewFacMain.setOldFacmNo(oldFacmNo);
				try {
					facMainService.update(tNewFacMain, titaVo);
				} catch (DBException e) {
					throw new LogicException(titaVo, "E0007", "額度主檔 " + e.getErrorMsg()); // 更新資料時，發生錯誤
				}

			}
//			復原
		} else {
			tNewFacMain = facMainService.holdById(new FacMainId(iCustNo, iNewFacmNo), titaVo);
			if (tNewFacMain != null) {
				tNewFacMain.setRenewCnt(0);
				tNewFacMain.setOldFacmNo(0);
				try {
					facMainService.update(tNewFacMain, titaVo);
				} catch (DBException e) {
					throw new LogicException(titaVo, "E0007", "額度主檔 " + e.getErrorMsg()); // 更新資料時，發生錯誤
				}

			}
		}
	}

	// put批次Tita
	private void putBatchTita() throws LogicException {

		titaVo.put("PrincipalX", df.format(iPrincipal));// 本金
		titaVo.put("InterestX", df.format(iInterest));// 利息
		titaVo.put("DelayIntX", df.format(iDelayInt));// 延遲息
		titaVo.put("BreachAmtX", df.format(iBreachAmt));// 違約金
		titaVo.put("AcctFee1X", df.format(iAcctFee1));// 帳管費
		titaVo.put("ModifyFee1X", df.format(iModifyFee1));// 契變手續費
		titaVo.put("FireFee1X", df.format(iFireFee1));// 火險費
		titaVo.put("LawFee1X", df.format(iLawFee1));// 法務費
		if (iCaseCloseCode == 7) {
			titaVo.put("ShortfallX", df.format("0"));// 累短收
		} else {
			titaVo.put("ShortfallX", df.format(baTxCom.getShortfall()));// 累短收
		}
		titaVo.put("ShortfallXX", "");// 累短收(本戶)
		titaVo.put("CloseBreachAmtX", df.format(iCloseBreachAmt));// 清償違約金
		titaVo.put("ExcessiveX", df.format(baTxCom.getExcessive().add(baTxCom.getExcessiveOther())));// 累溢收
		titaVo.put("TwReduceAmt", df.format(iReduceAmt));// 減免金額

	}
}