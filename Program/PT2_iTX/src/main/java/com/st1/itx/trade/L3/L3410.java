package com.st1.itx.trade.L3;

import java.math.BigDecimal;
import java.util.ArrayList;
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
import com.st1.itx.db.domain.FacMain;
import com.st1.itx.db.domain.FacMainId;
import com.st1.itx.db.domain.LoanBorMain;
import com.st1.itx.db.domain.LoanBorMainId;
import com.st1.itx.db.domain.LoanBorTx;
import com.st1.itx.db.domain.LoanBorTxId;
import com.st1.itx.db.domain.LoanIntDetail;
import com.st1.itx.db.domain.LoanIntDetailId;
import com.st1.itx.db.service.FacMainService;
import com.st1.itx.db.service.LoanBorMainService;
import com.st1.itx.db.service.LoanBorTxService;
import com.st1.itx.db.service.LoanIntDetailService;
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.common.AcReceivableCom;
import com.st1.itx.util.common.AcRepayCom;
import com.st1.itx.util.common.BaTxCom;
import com.st1.itx.util.common.LoanCalcRepayIntCom;
import com.st1.itx.util.common.LoanCom;
import com.st1.itx.util.common.LoanSetRepayIntCom;
import com.st1.itx.util.common.SendRsp;
import com.st1.itx.util.common.data.BaTxVo;
import com.st1.itx.util.common.data.CalcRepayIntVo;
import com.st1.itx.util.data.DataLog;
import com.st1.itx.util.parse.Parse;

/*
 * L3410 結案登錄-可欠繳
 * a.戶況需為正常戶或逾期戶
 * b.原則上舊額度之期款利息應繳齊才可辦展期。
 * c.展期時,需以整張額度作業，不可單一撥款或戶號處理。
 * d.[借新還舊]中可欠繳的利息掛在新貸的首筆中，首期回收時先收取。
 * e.[借新還舊]可以多筆新貸對應多筆舊貸。
 * f.[借新還舊]交易登打順序必須先執行結案後再執行新撥貸。
 */
/* 1.本金金額為收付欄總金額(借新還舊+暫收抵繳)
 * 2.累短收、費用由暫收可抵繳扣除
 * 3.延遲息、違約金需全額減免、減免後利息記短繳
 * Tita
 * TimCustNo=9,7
 * FacmNo=9,3
 * CaseCloseCode=9,1 結案區分 0:正常 1:展期-一般 2:展期-協議 3:轉催收 4:催收戶本人清償 5:催收戶保證人代償 6:催收戶強制執行 7:轉列呆帳 8:催收部分轉呆
 * EntryDate=9,7
 * NewApplNo=9,7
 * NewFacmNo=9,3
 * TimReduceAmt=9,14.2
 * LawFee=9,14.2
 * RqspFlag=X,1
 */
/**
 * L3410 結案登錄-可欠繳
 * 
 * @author iza
 * @version 1.0.0
 */
@Service("L3410")
@Scope("prototype")
public class L3410 extends TradeBuffer {

	/* DB服務注入 */
	@Autowired
	public FacMainService facMainService;
	@Autowired
	public LoanBorMainService loanBorMainService;
	@Autowired
	public LoanBorTxService loanBorTxService;
	@Autowired
	public LoanIntDetailService loanIntDetailService;

	@Autowired
	Parse parse;
	@Autowired
	SendRsp sendRsp;
	@Autowired
	DataLog datalog;
	@Autowired
	AcRepayCom acRepayCom;
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

	private TitaVo titaVo = new TitaVo();
	private int iCustNo;
	private int iFacmNo;
	private int iEntryDate;
	private int iCaseCloseCode;
	private int iNewApplNo;
	private int iNewFacmNo;
	private int iRenewCode;
	private int iRpCode; // 還款來源
	private BigDecimal iReduceAmt;
	private BigDecimal iShortAmt;
	private String iRqspFlag;
	private BigDecimal iPrincipal;
	private BigDecimal iInterest;
	private BigDecimal iDelayInt;
	private BigDecimal iBreachAmt;
	private BigDecimal iTmpAmt = BigDecimal.ZERO; // 暫收抵繳金額

	// work area
	private int wkCustNo = 0;
	private int wkFacmNo = 0;
	private int wkBormNo = 0;
	private int wkBorxNo = 0;
	private int wkNewBorxNo = 0;
	private int wkNewBormNo = 0;
	private int wkTotaCount = 0;
	private int wkIntStartDate = 9991231;
	private int wkIntEndDate = 0;
	private int wkPaidTerms = 0;
	private int wkDueDate = 0;
	private int renewCnt = 0;
	private int oldFacmNo = 0;
	private BigDecimal wkPrincipal = BigDecimal.ZERO;
	private BigDecimal wkInterest = BigDecimal.ZERO;
	private BigDecimal wkDelayInt = BigDecimal.ZERO;
	private BigDecimal wkBreachAmt = BigDecimal.ZERO;
	private BigDecimal wkReduceAmt = BigDecimal.ZERO;
	private BigDecimal wkReduceAmtRemaind = BigDecimal.ZERO;
	private BigDecimal wkReduceBreachAmt = BigDecimal.ZERO; // 減免違約金+減免延滯息
	private BigDecimal wkReduceBreachAmtRemaind = BigDecimal.ZERO;
	private BigDecimal wkReduceDelayIntRemaind = BigDecimal.ZERO;
	private BigDecimal wkReduceInterestRemaind = BigDecimal.ZERO;
	private BigDecimal wkLoanBal = BigDecimal.ZERO;
	private BigDecimal wkBeforeLoanBal = BigDecimal.ZERO;
	private BigDecimal wkTotalLoanBal = BigDecimal.ZERO;
	private BigDecimal wkTotalPrincipal = BigDecimal.ZERO;
	private BigDecimal wkTotalInterest = BigDecimal.ZERO;
	private BigDecimal wkTotalDelayInt = BigDecimal.ZERO;
	private BigDecimal wkTotalBreachAmt = BigDecimal.ZERO;
	private BigDecimal wkTotalUnpaidInt = BigDecimal.ZERO;
	private BigDecimal wkTotalRcv = BigDecimal.ZERO;
	private BigDecimal wkShortfall = BigDecimal.ZERO; // 累短收
	private BigDecimal wkShortfallPrincipal = BigDecimal.ZERO; // 累短收 - 本金
	private BigDecimal wkShortfallInterest = BigDecimal.ZERO; // 累短收-利息
	private BigDecimal wkShortCloseBreach = BigDecimal.ZERO; // 累短收 - 清償違約金
	private BigDecimal wkRolloverAmt = BigDecimal.ZERO;
	private TempVo tTempVo = new TempVo();
	private FacMain tFacMain;
	private FacMain tOldFacMain;
	private FacMain tNewFacMain;
	private LoanBorMain tLoanBorMain;
	private LoanBorTx tLoanBorTx;
	private LoanBorTxId tLoanBorTxId;
	private LoanIntDetail tLoanIntDetail;
	private LoanIntDetailId tLoanIntDetailId;
	private List<LoanBorTx> lLoanBorTx = new ArrayList<LoanBorTx>();
	private List<LoanBorMain> lLoanBorMain = new ArrayList<LoanBorMain>();
	private ArrayList<CalcRepayIntVo> lCalcRepayIntVo = new ArrayList<CalcRepayIntVo>();
	private ArrayList<BaTxVo> baTxList = new ArrayList<BaTxVo>();
	private boolean isFirstBorm = true;

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active L3410 ");
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
		loanCom.setTxBuffer(this.txBuffer);
		loanSetRepayIntCom.setTxBuffer(this.txBuffer);
		baTxCom.setTxBuffer(this.txBuffer);
		acRepayCom.setTxBuffer(this.txBuffer);

		// 取得輸入資料
		iCustNo = this.parse.stringToInteger(titaVo.getParam("TimCustNo"));
		iFacmNo = this.parse.stringToInteger(titaVo.getParam("FacmNo"));
		iEntryDate = this.parse.stringToInteger(titaVo.getParam("EntryDate"));
		iCaseCloseCode = this.parse.stringToInteger(titaVo.getParam("CaseCloseCode"));
		iNewApplNo = this.parse.stringToInteger(titaVo.getParam("NewApplNo"));
		iNewFacmNo = this.parse.stringToInteger(titaVo.getParam("NewFacmNo"));
		iRenewCode = this.parse.stringToInteger(titaVo.getParam("RenewCode"));
		iReduceAmt = this.parse.stringToBigDecimal(titaVo.getParam("TimReduceAmt"));
		iShortAmt = this.parse.stringToBigDecimal(titaVo.getParam("TimShortAmt")); // 利息+延滯息+違約金
		iRqspFlag = titaVo.getParam("RqspFlag");
		iRpCode = this.parse.stringToInteger(titaVo.getParam("RpCode1"));
		// 收取本金、利息、費用、短繳金額
		iPrincipal = this.parse.stringToBigDecimal(titaVo.getParam("TimPrincipal"));
		iInterest = this.parse.stringToBigDecimal(titaVo.getParam("TimInterest"));
		iDelayInt = this.parse.stringToBigDecimal(titaVo.getParam("TimDelayInt"));
		iBreachAmt = this.parse.stringToBigDecimal(titaVo.getParam("TimBreachAmt"));
		this.info("iPrincipal=" + iPrincipal + ",iInterest=" + iInterest);
		this.info("iDelayInt=" + iDelayInt + ",iBreachAmt=" + iBreachAmt);

		// 收付欄金額
		for (int i = 1; i <= 50; i++) {
			if (titaVo.get("RpCode" + i) == null || parse.stringToInteger(titaVo.getParam("RpCode" + i)) == 0)
				break;
			if (parse.stringToInteger(titaVo.getParam("RpCode" + i)) != 90) {
				wkRolloverAmt = wkRolloverAmt.add(parse.stringToBigDecimal(titaVo.getParam("RpAmt" + i))); // 展期金額
			}
			if (parse.stringToInteger(titaVo.getParam("RpCode" + i)) == 90) {
				iTmpAmt = iTmpAmt.subtract(parse.stringToBigDecimal(titaVo.getParam("RpAmt" + i))); // 暫收抵繳金額
			}
		}

		// 系統交易記錄檔的金額為展期金額
		titaVo.setTxAmt(wkRolloverAmt);

		// Check Input
		checkInputRoutine();
		
		// 檢查到同戶帳務交易需由最近一筆交易開始訂正
		if (titaVo.isHcodeErase()) {
			loanCom.checkEraseCustNoTxSeqNo(iCustNo, titaVo);
		}

		// 展期處理
		if (iCaseCloseCode == 1) {
			FacRenew(titaVo);
		}

		// 按違約金、 延滯息、利息順序減免
		if (iReduceAmt.compareTo(BigDecimal.ZERO) > 0) {
			wkReduceAmtRemaind = iReduceAmt;
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

		// 費用、短繳期金
		if (titaVo.isHcodeNormal()) {
			batxSettleUnpaid();
		}
		//
		if (titaVo.isHcodeNormal()) {
			caseCloseNormalRoutine();
		} else {
			caseCloseEraseRoutine();
		}

		// 帳務處理
		acRepayCom.setTxBuffer(this.txBuffer);
		acRepayCom.settleLoanRun(this.lLoanBorTx, this.baTxList, titaVo);

		// Check output
		checkOutputRoutine();

		// end
		this.addList(this.totaVo);
		return this.sendList();
	}

	private void checkInputRoutine() throws LogicException {
		// 本金不可短收
		if (this.parse.stringToInteger(titaVo.getParam("OverRpFg")) == 1
				&& this.parse.stringToBigDecimal(titaVo.getParam("OverRpAmt")).compareTo(BigDecimal.ZERO) > 0) {
			throw new LogicException(titaVo, "E3071", "本金不可短收"); // 金額不足
		}
		// 延遲息、違約金需全額減免
		if (iReduceAmt.compareTo(iDelayInt.add(iBreachAmt)) < 0) {
			throw new LogicException(titaVo, "E3071", "延遲息、違約金需全額減免"); // 金額不足
		}

	}

	private void checkOutputRoutine() throws LogicException {
	}

	private void caseCloseNormalRoutine() throws LogicException {
		this.info("caseCloseNormalRoutine ...");

		// 減免金額超過限額，需主管核可

		if (titaVo.isHcodeNormal()) {
			// 減免金額超過限額，需主管核可
			if (iRqspFlag.equals("Y")) {
				String iSupvReasonCode = "0007";
				if (!titaVo.getHsupCode().equals("1")) {
					if (iReduceAmt.compareTo(new BigDecimal(this.txBuffer.getSystemParas().getReduceAmtLimit3())) > 0) {
						iSupvReasonCode = "0027";
					} else if (iReduceAmt
							.compareTo(new BigDecimal(this.txBuffer.getSystemParas().getReduceAmtLimit2())) > 0) {
						iSupvReasonCode = "0017";
					} else {
						iSupvReasonCode = "0007";
					}
					sendRsp.addvReason(this.txBuffer, titaVo, iSupvReasonCode, "");
				}
			}
		}
		// 查詢額度檔
		tFacMain = facMainService.findById(new FacMainId(iCustNo, iFacmNo));
		if (tFacMain == null) {
			throw new LogicException(titaVo, "E3011", "額度主檔 戶號 = " + iCustNo + " 額度編號 = " + iFacmNo); // 鎖定資料時，發生錯誤
		}
		if (tFacMain.getActFg() == 1) {
			throw new LogicException(titaVo, "E0021",
					"額度檔 戶號 = " + tFacMain.getCustNo() + " 額度編號 =  " + tFacMain.getFacmNo()); // 該筆資料待放行中
		}
		Slice<LoanBorMain> slLoanBorMain = loanBorMainService.bormCustNoEq(iCustNo, iFacmNo, iFacmNo, 1, 900, 0,
				Integer.MAX_VALUE);
		lLoanBorMain = slLoanBorMain == null ? null : slLoanBorMain.getContent();
		if (lLoanBorMain == null || lLoanBorMain.size() == 0) {
			throw new LogicException(titaVo, "E0001", "放款主檔"); // 查詢資料不存在
		}
		for (LoanBorMain ln : lLoanBorMain) {
			if (!(ln.getStatus() == 0 || ln.getStatus() == 4)) {
				continue;
			}
			if (ln.getActFg() == 1) {
				throw new LogicException(titaVo, "E0021",
						"放款主檔 戶號 = " + ln.getCustNo() + " 額度編號 =  " + ln.getFacmNo() + " 撥款序號 = " + ln.getBormNo()); // 該筆資料待放行中
			}
			wkCustNo = ln.getCustNo();
			wkFacmNo = ln.getFacmNo();
			wkBormNo = ln.getBormNo();
			wkBorxNo = ln.getLastBorxNo() + 1;
			wkIntStartDate = 9991231;
			wkIntEndDate = 0;
			wkLoanBal = ln.getLoanBal();
			wkBeforeLoanBal = ln.getLoanBal();
			wkDueDate = ln.getNextPayIntDate();
			loanCalcRepayIntCom = loanSetRepayIntCom.setRepayInt(ln, 0, iEntryDate, 1, iEntryDate, titaVo);
			loanCalcRepayIntCom.setCaseCloseFlag("Y");
			lCalcRepayIntVo = loanCalcRepayIntCom.getRepayInt(titaVo);
			wkPrincipal = loanCalcRepayIntCom.getPrincipal();
			wkInterest = loanCalcRepayIntCom.getInterest();
			wkDelayInt = loanCalcRepayIntCom.getDelayInt();
			wkBreachAmt = loanCalcRepayIntCom.getBreachAmt();
			// 減免前金額
			wkReduceAmt = wkBreachAmt.add(wkDelayInt).add(wkInterest); // 減免金額
			wkReduceBreachAmt = wkBreachAmt.add(wkDelayInt); // 減免違約金+減免延滯息
			if (iReduceAmt.compareTo(BigDecimal.ZERO) > 0) {
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
			}
			// 本筆減免=減免前金額 - 減免後金額
			wkReduceAmt = wkReduceAmt.subtract(wkBreachAmt).subtract(wkDelayInt).subtract(wkInterest); // 減免金額
			wkReduceBreachAmt = wkReduceBreachAmt.subtract(wkBreachAmt).subtract(wkDelayInt); // 減免違約金+減免延滯息
			wkTotalLoanBal = wkTotalLoanBal.add(loanCalcRepayIntCom.getLoanBal());
			wkTotalPrincipal = wkTotalPrincipal.add(loanCalcRepayIntCom.getPrincipal());
			wkTotalInterest = wkTotalInterest.add(loanCalcRepayIntCom.getInterest());
			wkTotalDelayInt = wkTotalDelayInt.add(loanCalcRepayIntCom.getDelayInt());
			wkTotalBreachAmt = wkTotalBreachAmt.add(loanCalcRepayIntCom.getBreachAmt());
			wkTotalUnpaidInt = wkTotalInterest.add(wkTotalDelayInt).add(wkTotalBreachAmt);
			wkPaidTerms = loanCalcRepayIntCom.getPaidTerms();
			wkTotaCount++;
			// 鎖定撥款主檔
			tLoanBorMain = loanBorMainService.holdById(new LoanBorMainId(wkCustNo, wkFacmNo, wkBormNo));
			if (tLoanBorMain == null) {
				throw new LogicException(titaVo, "E0006",
						"撥款主檔 戶號 = " + wkCustNo + " 額度編號 = " + wkFacmNo + " 撥款序號 = " + wkBormNo); // 鎖定資料時，發生錯誤
			}
			if (tLoanBorMain.getActFg() == 1) {
				throw new LogicException(titaVo, "E0021", "放款主檔 戶號 = " + tLoanBorMain.getCustNo() + " 額度編號 =  "
						+ tLoanBorMain.getFacmNo() + " 撥款序號 = " + tLoanBorMain.getBormNo()); // 該筆資料待放行中
			}
			// 新撥款序號
			if (iNewFacmNo == iFacmNo) {
				wkNewBormNo = tFacMain.getLastBormNo() + 1;
			} else {
				FacMain tFacMainNew = facMainService.findById(new FacMainId(iCustNo, iNewFacmNo));
				wkNewBormNo = tFacMainNew.getLastBormNo() + 1;
			}
			// 短繳、費用收回
			getSettleUnpaid();
			// 更新額度主檔
			UpdFacMainRoutine();
			// 新增交易暫存檔(放款資料)
			// initialize tTempVo
			tTempVo.clear();
			addTxTempBormRoutine();
			// 更新撥款主檔
			updLoanBorMainRoutine();
			// 新增計息明細
			addLoanIntDetailRoutine();
			// 新增放款交易內容檔
			addLoanBorTxRoutine();
			isFirstBorm = false;
		}
		if (wkTotaCount == 0) {
			throw new LogicException(titaVo, "E3081", ""); // 無符合結案區分之撥款資料
		}
		if (iShortAmt.compareTo(wkTotalUnpaidInt) != 0) {
			throw new LogicException(titaVo, "E0013", "短繳金額不符" + iShortAmt + "/" + wkTotalUnpaidInt); // 程式邏輯有誤
		}
	}

	private void caseCloseEraseRoutine() throws LogicException {
		this.info("caseCloseEraseRoutine ...");

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
			wkInterest = parse.stringToBigDecimal(tTempVo.getParam("Interest"));
			wkDelayInt = parse.stringToBigDecimal(tTempVo.getParam("DelayInt"));
			wkBreachAmt = parse.stringToBigDecimal(tTempVo.getParam("BreachAmt"));
			wkNewBormNo = parse.stringToInteger(tTempVo.getParam("NewBormNo"));
			// 還原金額處理
			wkPrincipal = tx.getPrincipal();
			// 更新額度主檔
			UpdFacMainRoutine();
			// 還原撥款主檔
			RestoredLoanBorMainRoutine();
			// 註記交易內容檔
			loanCom.setLoanBorTxHcode(wkCustNo, wkFacmNo, wkBormNo, wkBorxNo, wkNewBorxNo, tLoanBorMain.getLoanBal(),
					titaVo);
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
		tLoanBorMain.setStoreRate(this.parse.stringToBigDecimal(tTempVo.get("StoreRate")));
		tLoanBorMain.setLoanBal(this.parse.stringToBigDecimal(tTempVo.get("LoanBal")));
		tLoanBorMain.setRepaidPeriod(this.parse.stringToInteger(tTempVo.get("RepaidPeriod")));
		tLoanBorMain.setPaidTerms(this.parse.stringToInteger(tTempVo.get("PaidTerms")));
		tLoanBorMain.setPrevPayIntDate(this.parse.stringToInteger(tTempVo.get("PrevPayIntDate")));
		tLoanBorMain.setPrevRepaidDate(this.parse.stringToInteger(tTempVo.get("PrevRepaidDate")));
		tLoanBorMain.setNextPayIntDate(this.parse.stringToInteger(tTempVo.get("NextPayIntDate")));
		tLoanBorMain.setNextRepayDate(this.parse.stringToInteger(tTempVo.get("NextRepayDate")));
		tLoanBorMain.setDueAmt(this.parse.stringToBigDecimal(tTempVo.get("DueAmt")));
		tLoanBorMain.setStatus(this.parse.stringToInteger(tTempVo.get("Status")));
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

	// 新增交易暫存檔(放款資料)
	private void addTxTempBormRoutine() throws LogicException {
		this.info("addTxTempBormRoutine ... ");

		tTempVo.putParam("BorxNo", wkBorxNo);
		tTempVo.putParam("StoreRate", tLoanBorMain.getStoreRate());
		tTempVo.putParam("LoanBal", tLoanBorMain.getLoanBal());
		tTempVo.putParam("Principal", loanCalcRepayIntCom.getPrincipal());
		tTempVo.putParam("RepaidPeriod", tLoanBorMain.getRepaidPeriod());
		tTempVo.putParam("PaidTerms", tLoanBorMain.getPaidTerms());
		tTempVo.putParam("PrevPayIntDate", tLoanBorMain.getPrevPayIntDate());
		tTempVo.putParam("PrevRepaidDate", tLoanBorMain.getPrevRepaidDate());
		tTempVo.putParam("NextPayIntDate", tLoanBorMain.getNextPayIntDate());
		tTempVo.putParam("NextRepayDate", tLoanBorMain.getNextRepayDate());
		tTempVo.putParam("DueAmt", tLoanBorMain.getDueAmt());
		tTempVo.putParam("Status", tLoanBorMain.getStatus());
		tTempVo.putParam("LastEntDy", tLoanBorMain.getLastEntDy());
		tTempVo.putParam("LastKinbr", tLoanBorMain.getLastKinbr());
		tTempVo.putParam("LastTlrNo", tLoanBorMain.getLastTlrNo());
		tTempVo.putParam("LastTxtNo", tLoanBorMain.getLastTxtNo());
		tTempVo.putParam("Interest", wkInterest);
		tTempVo.putParam("DelayInt", wkDelayInt);
		tTempVo.putParam("BreachAmt", wkBreachAmt);
		tTempVo.putParam("NewBormNo", wkNewBormNo);
	}

	// 更新額度主檔
	private void UpdFacMainRoutine() throws LogicException {
		this.info("UpdFacMainRoutine ...");
		tFacMain = facMainService.holdById(new FacMainId(iCustNo, wkFacmNo));
		if (tFacMain.getActFg() == 1) {
			throw new LogicException(titaVo, "E0021",
					"額度檔 戶號 = " + tFacMain.getCustNo() + " 額度編號 =  " + tFacMain.getFacmNo()); // 該筆資料待放行中
		}
		// 貸出金額(放款餘額)、 已動用額度餘額
		if (titaVo.isHcodeNormal()) {
			tFacMain.setUtilAmt(tFacMain.getUtilAmt().subtract(wkPrincipal));
			if (tFacMain.getRecycleCode().equals("1")) {
				tFacMain.setUtilBal(tFacMain.getUtilBal().subtract(wkPrincipal));
			}
			tFacMain.setAdvanceCloseCode(8);
		} else {
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
	private void updLoanBorMainRoutine() throws LogicException {
		this.info("updLoanBorMainRoutine ... ");

		tLoanBorMain.setLastBorxNo(wkBorxNo);
		tLoanBorMain.setStatus(1); // 1:展期
		tLoanBorMain.setStoreRate(loanCalcRepayIntCom.getStoreRate());
		if (tLoanBorMain.getAmortizedCode().equals("3")) {
			tLoanBorMain.setDueAmt(loanCalcRepayIntCom.getDueAmt());
		}
		tLoanBorMain.setLoanBal(loanCalcRepayIntCom.getLoanBal());
		tLoanBorMain.setRepaidPeriod(tLoanBorMain.getRepaidPeriod() + loanCalcRepayIntCom.getRepaidPeriod());
		tLoanBorMain.setPaidTerms(loanCalcRepayIntCom.getPaidTerms());
		tLoanBorMain.setPrevPayIntDate(loanCalcRepayIntCom.getPrevPaidIntDate());
		tLoanBorMain.setPrevRepaidDate(loanCalcRepayIntCom.getPrevRepaidDate());
		tLoanBorMain.setNextPayIntDate(0);
		tLoanBorMain.setNextRepayDate(0);
		tLoanBorMain.setLastEntDy(titaVo.getEntDyI());
		tLoanBorMain.setLastKinbr(titaVo.getKinbr());
		tLoanBorMain.setLastTlrNo(titaVo.getTlrNo());
		tLoanBorMain.setLastTxtNo(titaVo.getTxtNo());
		try {
			loanBorMainService.update(tLoanBorMain);
		} catch (DBException e) {
			throw new LogicException(titaVo, "E0007",
					"撥款主檔 戶號 = " + wkCustNo + " 額度編號 = " + wkFacmNo + " 撥款序號 = " + wkBormNo); // 更新資料時，發生錯誤
		}
	}

	// 新增計息明細
	private void addLoanIntDetailRoutine() throws LogicException {
		this.info("addLoanIntDetailRoutine ... ");

		if (lCalcRepayIntVo == null || lCalcRepayIntVo.size() == 0) {
			return;
		}
		int wkIntSeq = 0;

		for (CalcRepayIntVo c : lCalcRepayIntVo) {
			wkIntSeq++;
			wkIntStartDate = c.getStartDate() < wkIntStartDate ? c.getStartDate() : wkIntStartDate;
			wkIntEndDate = c.getEndDate() > wkIntEndDate ? c.getEndDate() : wkIntEndDate;
			wkLoanBal = wkLoanBal.subtract(c.getPrincipal());
			tLoanIntDetailId = new LoanIntDetailId();
			tLoanIntDetailId.setCustNo(c.getCustNo());
			tLoanIntDetailId.setFacmNo(c.getFacmNo());
			tLoanIntDetailId.setBormNo(c.getBormNo());
			tLoanIntDetailId.setAcDate(this.txBuffer.getTxCom().getTbsdy());
			tLoanIntDetailId.setTlrNo(titaVo.getTlrNo());
			tLoanIntDetailId.setTxtNo(titaVo.getTxtNo());
			tLoanIntDetailId.setIntSeq(wkIntSeq);
			tLoanIntDetail = new LoanIntDetail();
			tLoanIntDetail.setCustNo(c.getCustNo());
			tLoanIntDetail.setFacmNo(c.getFacmNo());
			tLoanIntDetail.setBormNo(c.getBormNo());
			tLoanIntDetail.setAcDate(this.txBuffer.getTxCom().getTbsdy());
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
			tLoanIntDetail.setLoanBal(wkLoanBal);
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
	private void addLoanBorTxRoutine() throws LogicException {
		this.info("addLoanBorTxRoutine ... ");

		tLoanBorTx = new LoanBorTx();
		tLoanBorTxId = new LoanBorTxId();
		loanCom.setLoanBorTx(tLoanBorTx, tLoanBorTxId, wkCustNo, wkFacmNo, wkBormNo, wkBorxNo, titaVo);
		tLoanBorTx.setAcctCode(tFacMain.getAcctCode());
		tLoanBorTx.setTxDescCode("342" + iCaseCloseCode);
		tLoanBorTx.setRepayCode(iRpCode); // 還款來源
		tLoanBorTx.setEntryDate(0);
		tLoanBorTx.setDueDate(wkDueDate);
		// 交易金額
		tLoanBorTx.setLoanBal(tLoanBorMain.getLoanBal());
		tLoanBorTx.setRate(tLoanBorMain.getStoreRate());
		tLoanBorTx.setIntStartDate(wkIntStartDate);
		tLoanBorTx.setIntEndDate(wkIntEndDate);
		tLoanBorTx.setPaidTerms(0);
		tLoanBorTx.setPrincipal(wkPrincipal);
		tLoanBorTx.setInterest(wkShortfallInterest); // 短繳收回
		tLoanBorTx.setDelayInt(BigDecimal.ZERO);
		tLoanBorTx.setBreachAmt(BigDecimal.ZERO);
		tLoanBorTx.setCloseBreachAmt(wkShortCloseBreach); // 短繳收回
		tLoanBorTx.setExtraRepay(BigDecimal.ZERO);
		tLoanBorTx.setUnpaidInterest(wkInterest.add(wkDelayInt).add(wkBreachAmt));
		tLoanBorTx.setShortfall(wkShortfall); // 短收收回金額
		// 繳息首筆、繳息次筆
		if (isFirstBorm) {
			tLoanBorTx.setDisplayflag("F"); // 繳息首筆
		} else {
			tLoanBorTx.setDisplayflag("I"); // 繳息次筆
		}
		// 其他欄位
		tTempVo.putParam("CaseCloseCode", iCaseCloseCode); // 1:展期(新額度) 2:借新還舊(同額度)
		tTempVo.putParam("NewApplNo", iNewApplNo);
		tTempVo.putParam("NewFacmNo", iNewFacmNo);
		tTempVo.putParam("RenewCode", iRenewCode);
		tTempVo.putParam("BeforeLoanBal", wkBeforeLoanBal); // 交易前放款餘額
		tTempVo.putParam("PaidTerms", wkPaidTerms);
		// 減免金額
		if (wkReduceAmt.compareTo(BigDecimal.ZERO) > 0) {
			tTempVo.putParam("ReduceAmt", wkReduceAmt);
		}
		if (wkReduceBreachAmt.compareTo(BigDecimal.ZERO) > 0) {
			tTempVo.putParam("ReduceBreachAmt", wkReduceBreachAmt); // 減免違約金+減免延滯息
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
		tLoanBorTx.setOtherFields(tTempVo.getJsonString());
		
		this.lLoanBorTx.add(tLoanBorTx); 

	}

	// 費用、短繳期金
	private void batxSettleUnpaid() throws LogicException {
		this.baTxList = new ArrayList<BaTxVo>();
		// call 應繳試算
		this.baTxList = baTxCom.settingUnPaid(iEntryDate, iCustNo, iFacmNo, 0, 99, BigDecimal.ZERO, titaVo); // //
																												// 99-費用全部(含未到期)
		wkTotalRcv = baTxCom.getShortfall().add(baTxCom.getModifyFee()).add(baTxCom.getAcctFee())
				.add(baTxCom.getFireFee()).add(baTxCom.getLawFee()).add(baTxCom.getCollFireFee())
				.add(baTxCom.getCollLawFee()).add(iDelayInt).add(iBreachAmt);
		if (baTxCom.getExcessive().compareTo(wkTotalRcv) < 0) {
			throw new LogicException(titaVo, "E3071", "暫收金額=" + baTxCom.getExcessive() + ",應收金額 = " + wkTotalRcv); // 金額不足
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

	// 展期處理
	private void FacRenew(TitaVo titaVo) throws LogicException {
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
		} else {
//			復原
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
}