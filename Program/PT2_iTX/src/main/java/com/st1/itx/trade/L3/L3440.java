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
import com.st1.itx.dataVO.OccursList;
import com.st1.itx.dataVO.TempVo;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.dataVO.TotaVo;
import com.st1.itx.db.domain.FacMain;
import com.st1.itx.db.domain.FacMainId;
import com.st1.itx.db.domain.LoanBorMain;
import com.st1.itx.db.domain.LoanBorMainId;
import com.st1.itx.db.domain.LoanBorTx;
import com.st1.itx.db.domain.LoanBorTxId;
import com.st1.itx.db.domain.LoanCheque;
import com.st1.itx.db.domain.LoanChequeId;
import com.st1.itx.db.domain.LoanIntDetail;
import com.st1.itx.db.domain.LoanIntDetailId;
import com.st1.itx.db.domain.LoanOverdue;
import com.st1.itx.db.domain.LoanOverdueId;
import com.st1.itx.db.service.FacMainService;
import com.st1.itx.db.service.LoanBorMainService;
import com.st1.itx.db.service.LoanBorTxService;
import com.st1.itx.db.service.LoanChequeService;
import com.st1.itx.db.service.LoanIntDetailService;
import com.st1.itx.db.service.LoanOverdueService;
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.common.AcRepayCom;
import com.st1.itx.util.common.BaTxCom;
import com.st1.itx.util.common.LoanCalcRepayIntCom;
import com.st1.itx.util.common.LoanCom;
import com.st1.itx.util.common.LoanDueAmtCom;
import com.st1.itx.util.common.LoanSetRepayIntCom;
import com.st1.itx.util.common.SendRsp;
import com.st1.itx.util.common.data.BaTxVo;
import com.st1.itx.util.common.data.CalcRepayIntVo;
import com.st1.itx.util.data.DataLog;
import com.st1.itx.util.date.DateUtil;
import com.st1.itx.util.parse.Parse;

/*
 * L3440 催收回復登錄
 * a.回收金額不可不足
 * b.先催收回復到放款正常戶，再依上次繳息日計算應繳金額。
 * c.收付欄的資金來源為暫收抵繳時，若有相關費用本交易需一併收取。
 */
/*
 * Tita
 * TimCustNo=9,7
 * FacmNo=9,3
 * EntryDate=9,7
 * TimReduceAmt=9,14.2
 * TotalRepayAmt=9,14.2
 * RealRepayAmt=9,14.2
 * RqspFlag=X,1
 */
/**
 * L3440 催收回復登錄
 * 
 * @author iza
 * @version 1.0.0
 */
@Service("L3440")
@Scope("prototype")
public class L3440 extends TradeBuffer {

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
	Parse parse;
	@Autowired
	SendRsp sendRsp;
	@Autowired
	DataLog datalog;
	@Autowired
	DateUtil dDateUtil;
	@Autowired
	LoanDueAmtCom loanDueAmtCom;

	@Autowired
	LoanCom loanCom;
	@Autowired
	LoanSetRepayIntCom loanSetRepayIntCom;
	@Autowired
	LoanCalcRepayIntCom loanCalcRepayIntCom;
	@Autowired
	AcRepayCom acRepayCom;
	@Autowired
	BaTxCom baTxCom;

	private TitaVo titaVo = new TitaVo();
	private int iCustNo;
	private int iFacmNo;
	private int iEntryDate;
	private BigDecimal iReduceAmt;
	private int iRpCode; // 還款來源
	private BigDecimal iTotalRepayAmt;
	private BigDecimal iRealRepayAmt;
	private BigDecimal iTxAmt;
	private String iRqspFlag;
	private BigDecimal iPrincipal;
	private BigDecimal iInterest;
	private BigDecimal iDelayInt;
	private BigDecimal iBreachAmt;
	private BigDecimal iAcctFee;
	private BigDecimal iModifyFee;
	private BigDecimal iFireFee;
	private BigDecimal iLawFee;
	private BigDecimal iShortfall;
	private int iOverRpFg; // 1.短收 2.溢收
	private BigDecimal iOverAmt; // 短溢收金額

	// work area
	private int wkCustNo;
	private int wkFacmNo;
	private int wkBormNo;
	private int wkBorxNo;
	private int wkOvduNo;
	private int wkNewBorxNo;
	private int wkTbsDy;
	private int wkDueDate = 0;
	private int wkIntSeq = 0;
	private int wkIntStartDate = 9991231;
	private int wkIntEndDate = 0;
	private int wkRepaidPeriod = 0;
	private int wkPaidTerms = 0;
	private BigDecimal wkLoanBal = BigDecimal.ZERO;
	private BigDecimal wkBeforeLoanBal = BigDecimal.ZERO;
	private BigDecimal wkPrincipal = BigDecimal.ZERO;
	private BigDecimal wkInterest = BigDecimal.ZERO;
	private BigDecimal wkDelayInt = BigDecimal.ZERO;
	private BigDecimal wkBreachAmt = BigDecimal.ZERO;
	private BigDecimal wkCloseBreachAmt = BigDecimal.ZERO;
	private BigDecimal wkReduceAmt = BigDecimal.ZERO;
	private BigDecimal wkReduceAmtRemaind = BigDecimal.ZERO;
	private BigDecimal wkReduceBreachAmt = BigDecimal.ZERO; // 減免違約金+減免延滯息
	private BigDecimal wkReduceBreachAmtRemaind = BigDecimal.ZERO;
	private BigDecimal wkReduceDelayIntRemaind = BigDecimal.ZERO;
	private BigDecimal wkReduceInterestRemaind = BigDecimal.ZERO;
	private BigDecimal wkTotalLoanBal = BigDecimal.ZERO;
	private BigDecimal wkTotalPrincipal = BigDecimal.ZERO;
	private BigDecimal wkTotalInterest = BigDecimal.ZERO;
	private BigDecimal wkTotalDelayInt = BigDecimal.ZERO;
	private BigDecimal wkTotalBreachAmt = BigDecimal.ZERO;
	private BigDecimal wkShortfallPrincipal = BigDecimal.ZERO; // 累短收 - 本金
	private BigDecimal wkShortfallInterest = BigDecimal.ZERO; // 累短收-利息
	private BigDecimal wkShortCloseBreach = BigDecimal.ZERO; // 累短收 - 清償違約金
	private BigDecimal wkShortfall = BigDecimal.ZERO; // 累短繳金額
	private BigDecimal wkTotalFee = BigDecimal.ZERO;
	private List<LoanBorTx> lLoanBorTx = new ArrayList<LoanBorTx>();
	private TempVo tTempVo = new TempVo();
	private FacMain tFacMain;
	private LoanBorTx tLoanBorTx;
	private LoanBorTxId tLoanBorTxId;
	private LoanBorMain tLoanBorMain;
	private LoanOverdue tLoanOverdue;
	private LoanIntDetail tLoanIntDetail;
	private LoanIntDetailId tLoanIntDetailId;
	private List<LoanOverdue> lLoanOverdue = new ArrayList<LoanOverdue>();
	private ArrayList<CalcRepayIntVo> lCalcRepayIntVo = new ArrayList<CalcRepayIntVo>();
	private ArrayList<BaTxVo> baTxList;
	private boolean isFirstOvdu = true;

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active L3440 ");
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
		iEntryDate = this.parse.stringToInteger(titaVo.getParam("EntryDate"));
		iReduceAmt = this.parse.stringToBigDecimal(titaVo.getParam("TimReduceAmt"));
		iTotalRepayAmt = this.parse.stringToBigDecimal(titaVo.getParam("TotalRepayAmt"));
		iRealRepayAmt = this.parse.stringToBigDecimal(titaVo.getParam("RealRepayAmt"));
		// 收取本金、利息、費用、短繳金額
		iPrincipal = this.parse.stringToBigDecimal(titaVo.getParam("TimPrincipal"));
		iInterest = this.parse.stringToBigDecimal(titaVo.getParam("TimInterest"));
		iDelayInt = this.parse.stringToBigDecimal(titaVo.getParam("TimDelayInt"));
		iBreachAmt = this.parse.stringToBigDecimal(titaVo.getParam("TimBreachAmt"));
		iAcctFee = this.parse.stringToBigDecimal(titaVo.getParam("AcctFee"));
		iModifyFee = this.parse.stringToBigDecimal(titaVo.getParam("ModifyFee"));
		iFireFee = this.parse.stringToBigDecimal(titaVo.getParam("FireFee"));
		iLawFee = this.parse.stringToBigDecimal(titaVo.getParam("LawFee"));
		iShortfall = this.parse.stringToBigDecimal(titaVo.getParam("Shortfall"));
		this.info("iPrincipal=" + iPrincipal + ",iInterest=" + iInterest);
		this.info("iDelayInt=" + iDelayInt + ",iBreachAmt=" + iBreachAmt);
		this.info("iAcctFee=" + iAcctFee + ",iModifyFee=" + iModifyFee);
		this.info("iFireFee=" + iFireFee + ",iLawFee=" + iLawFee);
		this.info("iShortfall=" + iShortfall);
		this.info("iTotalRepayAmt=" + iTotalRepayAmt + ",iRealRepayAmt=" + iRealRepayAmt);
		iRqspFlag = titaVo.getParam("RqspFlag");
		iOverRpFg = this.parse.stringToInteger(titaVo.getParam("OverRpFg")); // 1->短收 2->溢收
		iOverAmt = this.parse.stringToBigDecimal(titaVo.getParam("OverRpAmt"));
		iTxAmt = this.parse.stringToBigDecimal(titaVo.getTxAmt());
		iRpCode = this.parse.stringToInteger(titaVo.getParam("RpCode1"));
		// 不可有短繳金額
		if (iOverRpFg == 1 && iOverAmt.compareTo(BigDecimal.ZERO) > 0) {
			throw new LogicException(titaVo, "E3094", "短繳金額 = " + iOverAmt); // 不可有短繳金額
		}
		// 系統交易記錄檔的金額為實際支付金額或支付總金額
		if (iTxAmt.compareTo(BigDecimal.ZERO) > 0) {
			titaVo.setTxAmt(iTxAmt);
		} else {
			titaVo.setTxAmt(iTotalRepayAmt);
		}

		// 檢查到同戶帳務交易需由最近一筆交易開始訂正
		if (titaVo.isHcodeErase()) {
			loanCom.checkEraseCustNoTxSeqNo(iCustNo, titaVo);
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

		if (titaVo.isHcodeNormal()) {
			ReplyNormalRoutine();
		} else {
			ReplyEraseRoutine();
		}

		// 帳務處理
		acRepayCom.setTxBuffer(this.txBuffer);
		acRepayCom.settleRun(this.lLoanBorTx, this.baTxList, titaVo);

		this.addList(this.totaVo);
		return this.sendList();
	}

	private void ReplyNormalRoutine() throws LogicException {
		this.info("ReplyNormaloutine ...");

		// 減免金額超過限額，需主管核可
		if (iRqspFlag.equals("Y")) {
			if (!titaVo.getHsupCode().equals("1")) {
				sendRsp.addvReason(this.txBuffer, titaVo, "0007", "");
			}
		}

		List<Integer> lStatus = new ArrayList<Integer>(); // 1:催收 2:部分轉呆 3:呆帳 4:催收回復
		lStatus.add(1);
		lStatus.add(2);
		Slice<LoanOverdue> slLoanOverdue = loanOverdueService.ovduCustNoRange(iCustNo, iFacmNo, iFacmNo, 1, 900, 1, 999,
				lStatus, 0, Integer.MAX_VALUE);
		lLoanOverdue = slLoanOverdue == null ? null : slLoanOverdue.getContent();
		if (lLoanOverdue == null || lLoanOverdue.size() == 0) {
			throw new LogicException(titaVo, "E0001", "催收呆帳檔"); // 查詢資料不存在
		}
		for (LoanOverdue od : lLoanOverdue) {
			wkCustNo = od.getCustNo();
			wkFacmNo = od.getFacmNo();
			wkBormNo = od.getBormNo();
			wkOvduNo = od.getOvduNo();
			wkIntStartDate = 9991231;
			wkIntEndDate = 0;
			// 查詢額度檔
			tFacMain = facMainService.findById(new FacMainId(iCustNo, iFacmNo));
			if (tFacMain == null) {
				throw new LogicException(titaVo, "E3011", "額度主檔 戶號 = " + iCustNo + " 額度編號 = " + od.getFacmNo()); // 鎖定資料時，發生錯誤
			}
			if (tFacMain.getActFg() == 1) {
				throw new LogicException(titaVo, "E0021",
						"額度檔 戶號 = " + tFacMain.getCustNo() + " 額度編號 =  " + tFacMain.getFacmNo()); // 該筆資料待放行中
			}
			// 鎖定催收呆帳檔
			tLoanOverdue = loanOverdueService.holdById(new LoanOverdueId(iCustNo, iFacmNo, wkBormNo, wkOvduNo));
			if (tLoanOverdue == null) {
				throw new LogicException(titaVo, "E0006",
						"催收呆帳檔 戶號 = " + iCustNo + " 額度編號 = " + iFacmNo + " 撥款序號 = " + wkBormNo + " 催收序號 = " + wkOvduNo); // 鎖定資料時，發生錯誤
			}
			// 鎖定撥款主檔
			tLoanBorMain = loanBorMainService.holdById(new LoanBorMainId(iCustNo, iFacmNo, wkBormNo));
			if (tLoanBorMain == null) {
				throw new LogicException(titaVo, "E0006",
						"撥款主檔 戶號 = " + iCustNo + " 額度編號 = " + iFacmNo + " 撥款序號 = " + wkBormNo); // 鎖定資料時，發生錯誤
			}
			if (tLoanBorMain.getActFg() == 1) {
				throw new LogicException(titaVo, "E0021", "放款主檔 戶號 = " + tLoanBorMain.getCustNo() + " 額度編號 =  "
						+ tLoanBorMain.getFacmNo() + " 撥款序號 = " + tLoanBorMain.getBormNo()); // 該筆資料待放行中
			}
			wkBorxNo = tLoanBorMain.getLastBorxNo() + 1;
			wkLoanBal = tLoanBorMain.getLoanBal();
			wkBeforeLoanBal = tLoanBorMain.getLoanBal();
			wkDueDate = tLoanBorMain.getNextPayIntDate();
			// 計算至入帳日期應繳之金額
			CalcRepayRoutine(od);
			// 更新額度檔
			updFacMainRoutine();
			// initialize tTempVo
			tTempVo.clear();
			// 新增交易暫存檔(放款資料)
			AddTxTempBormRoutine(od);
			// 更新撥款主檔
			UpdLoanBorMainRoutine();
			// 新增放款交易內容檔
			AddLoanBorTxRoutine(od);
			// 更新催收呆帳檔
			UpdLoanOvduRoutine();
			// FirstBorm
			isFirstOvdu = false;
		}
	}

	// 調整下次利率調整日 > 會計日
	private int NextAdjRateDateRoutine() throws LogicException {
		int nextAdjDate = tLoanBorMain.getNextAdjRateDate();
		if (tLoanBorMain.getNextAdjRateDate() > 0 && tLoanBorMain.getRateAdjFreq() > 0
				&& tLoanBorMain.getNextAdjRateDate() < titaVo.getEntDyI()) {
			do {
				dDateUtil.init();
				dDateUtil.setDate_1(nextAdjDate);
				dDateUtil.setMons(tLoanBorMain.getRateAdjFreq()); // 調整周期(單位固定為月)
				nextAdjDate = dDateUtil.getCalenderDay();
			} while (nextAdjDate < titaVo.getEntDyI());

			if (nextAdjDate > tLoanBorMain.getMaturityDate()) {
				nextAdjDate = tLoanBorMain.getMaturityDate();
			}
		}
		this.info("NextAdjRateDateRoutine end nextAdjDate= " + nextAdjDate);
		return nextAdjDate;
	}

	private void ReplyEraseRoutine() throws LogicException {
		this.info("ReplyEraseRoutine ...");

		Slice<LoanBorTx> slLoanBortx = loanBorTxService.custNoTxtNoEq(iCustNo, titaVo.getOrgEntdyI() + 19110000,
				titaVo.getOrgKin(), titaVo.getOrgTlr(), titaVo.getOrgTno(), 0, Integer.MAX_VALUE, titaVo);
		lLoanBorTx = slLoanBortx == null ? null : slLoanBortx.getContent();
		if (lLoanBorTx == null || lLoanBorTx.size() == 0) {
			throw new LogicException(titaVo, "E0001", "交易暫存檔 分行別 = " + titaVo.getOrgKin() + " 交易員代號 = "
					+ titaVo.getOrgTlr() + " 交易序號 = " + titaVo.getOrgTno()); // 查詢資料不存在
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
				wkOvduNo = this.parse.stringToInteger(tTempVo.get("OvduNo"));
				this.info("   wkOvduNo = " + wkOvduNo);
				wkPrincipal = tx.getPrincipal();
				// 更新額度檔
				updFacMainRoutine();
				// 還原撥款主檔
				RestoredLoanBorMainRoutine();
				// 註記交易內容檔
				loanCom.setLoanBorTxHcode(wkCustNo, wkFacmNo, wkBormNo, wkBorxNo, wkNewBorxNo,
						tLoanBorMain.getLoanBal(), titaVo);
				// 還原催收檔
				RestoredLoanOverdueRoutine();

			}
		}

	}

	private void updFacMainRoutine() throws LogicException {
		// 鎖定額度檔
		tFacMain = facMainService.holdById(new FacMainId(iCustNo, wkFacmNo), titaVo);
		this.info("updFacMainRoutine..." + tFacMain.getRecycleCode() + "," + wkPrincipal);
		if (tFacMain == null) {
			throw new LogicException(titaVo, "E3011", "額度主檔 戶號 = " + iCustNo + " 額度編號 = " + wkFacmNo); // 鎖定資料時，發生錯誤
		}
		if (tFacMain.getActFg() == 1) {
			throw new LogicException(titaVo, "E0021",
					"額度檔 戶號 = " + tFacMain.getCustNo() + " 額度編號 =  " + tFacMain.getFacmNo()); // 該筆資料待放行中
		}
		if (wkPrincipal.compareTo(BigDecimal.ZERO) == 0) {
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
		try {
			facMainService.update(tFacMain, titaVo);
		} catch (DBException e) {
			throw new LogicException(titaVo, "E0007", "額度主檔 戶號 = " + iCustNo + " 額度編號 = " + wkFacmNo); // 更新資料時，發生錯誤
		}
	}

	// 計算至入帳日期應繳之金額
	private void CalcRepayRoutine(LoanOverdue od) throws LogicException {
		this.info("CalcRepayRoutine ...");

		int wkTerms = 0;

		// 計算至入帳日期應繳之期數
		wkTerms = loanCom.getTermNo(2, tLoanBorMain.getFreqBase(), tLoanBorMain.getPayIntFreq(),
				tLoanBorMain.getSpecificDate(), tLoanBorMain.getSpecificDd(), iEntryDate);
		if (tLoanBorMain.getPrevPayIntDate() > tLoanBorMain.getDrawdownDate()) {
			wkTerms = wkTerms - loanCom.getTermNo(2, tLoanBorMain.getFreqBase(), tLoanBorMain.getPayIntFreq(),
					tLoanBorMain.getSpecificDate(), tLoanBorMain.getSpecificDd(), tLoanBorMain.getPrevPayIntDate());
		}
		this.info("wkTerms=" + wkTerms);
		if (wkTerms > 0) {
			loanCalcRepayIntCom = loanSetRepayIntCom.setRepayInt(tLoanBorMain, wkTerms, 0, 0, iEntryDate, titaVo);
			lCalcRepayIntVo = loanCalcRepayIntCom.getRepayInt(titaVo);
			wkPrincipal = loanCalcRepayIntCom.getPrincipal();
			wkInterest = loanCalcRepayIntCom.getInterest();
			wkDelayInt = loanCalcRepayIntCom.getDelayInt();
			wkBreachAmt = loanCalcRepayIntCom.getBreachAmt();
			wkRepaidPeriod = loanCalcRepayIntCom.getRepaidPeriod();
			wkPaidTerms = loanCalcRepayIntCom.getPaidTerms();
		} else {
			lCalcRepayIntVo = new ArrayList<CalcRepayIntVo>();
			wkPrincipal = BigDecimal.ZERO;
			wkInterest = BigDecimal.ZERO;
			wkDelayInt = BigDecimal.ZERO;
			wkBreachAmt = BigDecimal.ZERO;
			wkRepaidPeriod = 0;
			wkPaidTerms = 0;
		}
		wkTotalLoanBal = wkTotalLoanBal.add(tLoanBorMain.getLoanBal());
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
		wkTotalPrincipal = wkTotalPrincipal.add(wkPrincipal);
		wkTotalInterest = wkTotalInterest.add(wkInterest);
		wkTotalDelayInt = wkTotalDelayInt.add(wkDelayInt);
		wkTotalBreachAmt = wkTotalBreachAmt.add(wkBreachAmt);

		// 轉催收利息大於利息
		if (od.getOvduIntAmt().compareTo(wkInterest) > 0) {
			throw new LogicException(titaVo, "E0015", " 轉催收利息大於利息= " + od.getOvduIntAmt() + "," + wkInterest); // 檢查錯誤
		}

		// 短繳、費用收回
		getSettleUnpaid();

		// Principal 實收本金 => 含收回欠繳本金
		wkPrincipal = wkPrincipal.add(wkShortfallPrincipal);
		// Interest 實收利息 => 扣除減免、含收回欠繳利息
		wkInterest = wkInterest.add(wkShortfallInterest);
		// 實收清償違約金= 收回欠繳清償違約金
		wkCloseBreachAmt = wkShortCloseBreach;

		if (lCalcRepayIntVo != null && lCalcRepayIntVo.size() > 0) {
			for (CalcRepayIntVo c : lCalcRepayIntVo) {
				OccursList occursList = new OccursList();
				wkIntStartDate = c.getStartDate() < wkIntStartDate ? c.getStartDate() : wkIntStartDate;
				wkIntEndDate = c.getEndDate() > wkIntEndDate ? c.getEndDate() : wkIntEndDate;
				occursList.putParam("OOFacmNo", iFacmNo);
				occursList.putParam("OOBormNo", wkBormNo);
				occursList.putParam("OOIntStartDate", c.getStartDate());
				occursList.putParam("OOIntEndDate", c.getEndDate());
				occursList.putParam("OOAmount", c.getAmount());
				occursList.putParam("OORate", c.getStoreRate());
				occursList.putParam("OOPrincipal", c.getPrincipal());
				occursList.putParam("OOInterest", c.getInterest());
				occursList.putParam("OODelayInt", c.getDelayInt());
				occursList.putParam("OOBreachAmt", c.getBreachAmt());
				occursList.putParam("OOLoanBal", c.getLoanBal());
				// 將每筆資料放入Tota的OcList
				this.totaVo.addOccursList(occursList);
			}
		}
		// 新增計息明細
		wkIntSeq = 0;
		AddLoanIntDetailRoutine();
	}

	// 新增交易暫存檔(放款資料)
	private void AddTxTempBormRoutine(LoanOverdue od) throws LogicException {
		this.info("AddTxTempBormRoutine ... ");
		tTempVo.putParam("BorxNo", wkBorxNo);
		tTempVo.putParam("Status", tLoanBorMain.getStatus());
		tTempVo.putParam("StoreRate", tLoanBorMain.getStoreRate());
		tTempVo.putParam("LoanBal", tLoanBorMain.getLoanBal());
		tTempVo.putParam("RepaidPeriod", tLoanBorMain.getRepaidPeriod());
		tTempVo.putParam("PaidTerms", tLoanBorMain.getPaidTerms());
		tTempVo.putParam("PrevPayIntDate", tLoanBorMain.getPrevPayIntDate());
		tTempVo.putParam("PrevRepaidDate", tLoanBorMain.getPrevRepaidDate());
		tTempVo.putParam("NextPayIntDate", tLoanBorMain.getNextPayIntDate());
		tTempVo.putParam("NextRepayDate", tLoanBorMain.getNextRepayDate());
		tTempVo.putParam("DueAmt", tLoanBorMain.getDueAmt());
		tTempVo.putParam("OvduNo", od.getOvduNo());
		tTempVo.putParam("OvduPrinBal", od.getOvduPrinBal());
		tTempVo.putParam("OvduIntBal", od.getOvduIntBal());
		tTempVo.putParam("OvduBreachBal", od.getOvduBreachBal());
		tTempVo.putParam("OvduBal", od.getOvduBal());
		tTempVo.putParam("ReplyReduceAmt", od.getReplyReduceAmt());
		tTempVo.putParam("NextAdjRateDate", tLoanBorMain.getNextAdjRateDate());
		tTempVo.putParam("LastEntDy", tLoanBorMain.getLastEntDy());
		tTempVo.putParam("LastKinbr", tLoanBorMain.getLastKinbr());
		tTempVo.putParam("LastTlrNo", tLoanBorMain.getLastTlrNo());
		tTempVo.putParam("LastTxtNo", tLoanBorMain.getLastTxtNo());
	}

	// 更新撥款主檔
	private void UpdLoanBorMainRoutine() throws LogicException {
		this.info("UpdLoanBorMainRoutine ... ");

		tLoanBorMain.setLastBorxNo(wkBorxNo);
		tLoanBorMain.setStatus(0); // 0: 正常戶
		if (wkPaidTerms > 0) {
			tLoanBorMain.setStoreRate(loanCalcRepayIntCom.getStoreRate());
			if (tLoanBorMain.getAmortizedCode().equals("3")) {
				tLoanBorMain.setDueAmt(loanCalcRepayIntCom.getDueAmt());
			}
			tLoanBorMain.setLoanBal(loanCalcRepayIntCom.getLoanBal());
			tLoanBorMain.setRepaidPeriod(tLoanBorMain.getRepaidPeriod() + wkRepaidPeriod);
			tLoanBorMain.setPaidTerms(tLoanBorMain.getPaidTerms());
			tLoanBorMain.setPrevPayIntDate(loanCalcRepayIntCom.getPrevPaidIntDate());
			tLoanBorMain.setPrevRepaidDate(loanCalcRepayIntCom.getPrevRepaidDate());
			tLoanBorMain.setNextPayIntDate(loanCalcRepayIntCom.getNextPayIntDate());
			tLoanBorMain.setNextRepayDate(loanCalcRepayIntCom.getNextRepayDate());
		}
		int nextAdjRateDate = NextAdjRateDateRoutine();
		tLoanBorMain.setNextAdjRateDate(nextAdjRateDate);
		tLoanBorMain.setLastEntDy(titaVo.getEntDyI());
		tLoanBorMain.setLastKinbr(titaVo.getKinbr());
		tLoanBorMain.setLastTlrNo(titaVo.getTlrNo());
		tLoanBorMain.setLastTxtNo(titaVo.getTxtNo());
		try {
			loanBorMainService.update(tLoanBorMain);
		} catch (DBException e) {
			throw new LogicException(titaVo, "E0007",
					"撥款主檔 戶號 = " + iCustNo + " 額度編號 = " + iFacmNo + " 撥款序號 = " + wkBormNo); // 更新資料時，發生錯誤
		}
	}

	// 新增計息明細
	private void AddLoanIntDetailRoutine() throws LogicException {
		this.info("AddLoanIntDetailRoutine ... ");

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
	private void AddLoanBorTxRoutine(LoanOverdue od) throws LogicException {
		this.info("AddLoanBorTxRoutine ... ");

		tLoanBorTx = new LoanBorTx();
		tLoanBorTxId = new LoanBorTxId();
		loanCom.setLoanBorTx(tLoanBorTx, tLoanBorTxId, iCustNo, iFacmNo, wkBormNo, wkBorxNo, titaVo);
		tLoanBorTx.setTxDescCode("3440"); // 催收回復
		tLoanBorTx.setAcctCode(tFacMain.getAcctCode());
		tLoanBorTx.setRepayCode(iRpCode); // 還款來源
		tLoanBorTx.setEntryDate(iEntryDate);
		tLoanBorTx.setDueDate(wkDueDate);
		tLoanBorTx.setLoanBal(tLoanBorMain.getLoanBal());
		tLoanBorTx.setRate(tLoanBorMain.getStoreRate());
		tLoanBorTx.setIntStartDate(wkIntStartDate);
		tLoanBorTx.setIntEndDate(wkIntEndDate);
		tLoanBorTx.setPaidTerms(wkPaidTerms);
		tLoanBorTx.setPrincipal(wkPrincipal);
		tLoanBorTx.setInterest(wkInterest.subtract(od.getOvduIntAmt())); // 減轉催收利息
		tLoanBorTx.setDelayInt(wkDelayInt);
		tLoanBorTx.setBreachAmt(wkBreachAmt);
		tLoanBorTx.setCloseBreachAmt(wkCloseBreachAmt);
		tLoanBorTx.setShortfall(wkShortfall); // 累短收收回金額
		// 繳息首筆、繳息次筆
		if (isFirstOvdu) {
			tLoanBorTx.setDisplayflag("F"); // 繳息首筆
		} else {
			tLoanBorTx.setDisplayflag("I"); // 繳息次筆
		}

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
		tTempVo.putParam("BeforeLoanBal", wkBeforeLoanBal); // 交易前放款餘額
		tTempVo.putParam("PaidTerms", wkPaidTerms);
		tTempVo.putParam("PaidTerms", wkPaidTerms);
		
		tLoanBorTx.setOtherFields(tTempVo.getJsonString());

		// 暫收款金額含催收還款金額
		BigDecimal ovDuRepaid = od.getOvduAmt().subtract(parse.stringToBigDecimal(tTempVo.get("OvduBal")));
		tLoanBorTx.setTempAmt(tLoanBorTx.getTempAmt().add(ovDuRepaid));
		tLoanBorTx.setTxAmt(tLoanBorTx.getTxAmt().subtract(ovDuRepaid));
		this.lLoanBorTx.add(tLoanBorTx);
	}

	// 應繳試算 貸方：費用
	private void batxSettleUnpaid() throws LogicException {
		this.baTxList = new ArrayList<BaTxVo>();
		// call 應繳試算
		this.baTxList = baTxCom.settingUnPaid(titaVo.getEntDyI(), iCustNo, iFacmNo, 0, 00, BigDecimal.ZERO, titaVo);
		// 99-費用全部(含未到期)
		wkTotalFee = baTxCom.getShortfall().add(baTxCom.getModifyFee()).add(baTxCom.getAcctFee())
				.add(baTxCom.getFireFee()).add(baTxCom.getLawFee()).add(baTxCom.getCollFireFee())
				.add(baTxCom.getCollLawFee());
		this.info("未收費用 =" + wkTotalFee);

	}

	// 貸方：短繳期金
	private void getSettleUnpaid() throws LogicException {

		this.wkShortfallInterest = BigDecimal.ZERO; // 累短收 - 利息
		this.wkShortfallPrincipal = BigDecimal.ZERO; // 累短收 - 本金
		this.wkShortCloseBreach = BigDecimal.ZERO; // 累短收 - 清償違約金
		this.wkShortfall = BigDecimal.ZERO;
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


	// 更新催收呆帳檔
	private void UpdLoanOvduRoutine() throws LogicException {
		this.info("UpdLoanOvduRoutine ... ");

		tLoanOverdue.setOvduPrinBal(BigDecimal.ZERO);
		tLoanOverdue.setOvduIntBal(BigDecimal.ZERO);
		tLoanOverdue.setOvduBreachBal(BigDecimal.ZERO);
		tLoanOverdue.setOvduBal(BigDecimal.ZERO);
		tLoanOverdue.setReplyReduceAmt(BigDecimal.ZERO);
		tLoanOverdue.setAcDate(wkTbsDy);
		tLoanOverdue.setStatus(4); // 4.催收回復
		try {
			loanOverdueService.update(tLoanOverdue);
		} catch (DBException e) {
			throw new LogicException(titaVo, "E0007",
					"催收呆帳檔 戶號 = " + iCustNo + " 額度編號 = " + iFacmNo + " 撥款序號 = " + wkBormNo + " 催收序號 = " + wkOvduNo); // 更新資料時，發生錯誤
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
		tLoanBorMain.setStatus(this.parse.stringToInteger(tTempVo.get("Status")));
		tLoanBorMain.setStoreRate(this.parse.stringToBigDecimal(tTempVo.get("StoreRate")));
		tLoanBorMain.setLoanBal(this.parse.stringToBigDecimal(tTempVo.get("LoanBal")));
		tLoanBorMain.setRepaidPeriod(this.parse.stringToInteger(tTempVo.get("RepaidPeriod")));
		tLoanBorMain.setPaidTerms(this.parse.stringToInteger(tTempVo.get("PaidTerms")));
		tLoanBorMain.setPrevPayIntDate(this.parse.stringToInteger(tTempVo.get("PrevPayIntDate")));
		tLoanBorMain.setPrevRepaidDate(this.parse.stringToInteger(tTempVo.get("PrevRepaidDate")));
		tLoanBorMain.setNextPayIntDate(this.parse.stringToInteger(tTempVo.get("NextPayIntDate")));
		tLoanBorMain.setNextRepayDate(this.parse.stringToInteger(tTempVo.get("NextRepayDate")));
		tLoanBorMain.setDueAmt(this.parse.stringToBigDecimal(tTempVo.get("DueAmt")));
		tLoanBorMain.setNextAdjRateDate(this.parse.stringToInteger(tTempVo.get("NextAdjRateDate")));
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

	// 還原催收檔
	private void RestoredLoanOverdueRoutine() throws LogicException {
		this.info("RestoredLoanOverdueRoutine ... ");

		tLoanOverdue = loanOverdueService.holdById(new LoanOverdueId(wkCustNo, wkFacmNo, wkBormNo, wkOvduNo));
		if (tLoanOverdue == null) {
			throw new LogicException(titaVo, "E0006",
					"催收呆帳檔 戶號 = " + wkCustNo + " 額度編號 = " + wkFacmNo + " 撥款序號 = " + wkBormNo + " 催收序號 = " + wkOvduNo); // 鎖定資料時，發生錯誤
		}
		tLoanOverdue.setOvduPrinBal(this.parse.stringToBigDecimal(tTempVo.get("OvduPrinBal")));
		tLoanOverdue.setOvduIntBal(this.parse.stringToBigDecimal(tTempVo.get("OvduIntBal")));
		tLoanOverdue.setOvduBreachBal(this.parse.stringToBigDecimal(tTempVo.get("OvduBreachBal")));
		tLoanOverdue.setOvduBal(this.parse.stringToBigDecimal(tTempVo.get("OvduBal")));
		tLoanOverdue.setReplyReduceAmt(this.parse.stringToBigDecimal(tTempVo.get("ReplyReduceAmt")));
		tLoanOverdue.setStatus(this.parse.stringToInteger(tTempVo.get("Status")));
		tLoanOverdue.setAcDate(this.parse.stringToInteger(tTempVo.get("AcDate")));
		try {
			loanOverdueService.update(tLoanOverdue);
		} catch (DBException e) {
			throw new LogicException(titaVo, "E0007",
					"催收呆帳檔 戶號 = " + wkCustNo + " 額度編號 = " + wkFacmNo + " 撥款序號 = " + wkBormNo + " 催收序號 = " + wkOvduNo); // 更新資料時，發生錯誤
		}
	}

}