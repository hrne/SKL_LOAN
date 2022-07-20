package com.st1.itx.trade.L3;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;

import com.st1.itx.Exception.LogicException;
import com.st1.itx.Exception.DBException;
import com.st1.itx.dataVO.OccursList;
import com.st1.itx.dataVO.TempVo;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.dataVO.TotaVo;
import com.st1.itx.db.domain.AcDetail;
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
import com.st1.itx.db.domain.TxTemp;
import com.st1.itx.db.domain.TxTempId;
import com.st1.itx.db.service.FacMainService;
import com.st1.itx.db.service.LoanBorMainService;
import com.st1.itx.db.service.LoanBorTxService;
import com.st1.itx.db.service.LoanIntDetailService;
import com.st1.itx.db.service.LoanOverdueService;
import com.st1.itx.db.service.TxTempService;
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.common.AcDetailCom;
import com.st1.itx.util.common.AcPaymentCom;
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
	public TxTempService txTempService;
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
	AcDetailCom acDetailCom;
	@Autowired
	AcPaymentCom acPaymentCom;
	@Autowired
	LoanCom loanCom;
	@Autowired
	LoanSetRepayIntCom loanSetRepayIntCom;
	@Autowired
	LoanCalcRepayIntCom loanCalcRepayIntCom;
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
	private BigDecimal iTmpAmt = BigDecimal.ZERO; // 暫收抵繳金額

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
	private BigDecimal wkTempAmt = BigDecimal.ZERO;
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
	private BigDecimal wkTxAmtRemaind = BigDecimal.ZERO; // 交易還款餘額
	private BigDecimal wkTotalRepay = BigDecimal.ZERO; // 總還款金額
	private BigDecimal wkAcctFee = BigDecimal.ZERO;
	private BigDecimal wkModifyFee = BigDecimal.ZERO;
	private BigDecimal wkFireFee = BigDecimal.ZERO;
	private BigDecimal wkLawFee = BigDecimal.ZERO;
	private BigDecimal wkTotalFee = BigDecimal.ZERO;
	private TxTemp tTxTemp;
	private TxTempId tTxTempId;
	private TempVo tTempVo = new TempVo();
	private FacMain tFacMain;
	private LoanBorTx tLoanBorTx;
	private LoanBorTxId tLoanBorTxId;
	private LoanBorMain tLoanBorMain;
	private LoanOverdue tLoanOverdue;
	private LoanIntDetail tLoanIntDetail;
	private LoanIntDetailId tLoanIntDetailId;
	private AcDetail acDetail;
	private List<TxTemp> lTxTemp;
	private List<AcDetail> lAcDetail = new ArrayList<AcDetail>();
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
		iTotalRepayAmt = BigDecimal.ZERO; // this.parse.stringToBigDecimal(titaVo.getParam("TotalRepayAmt"));
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
		// 暫收抵繳金額
		for (int i = 1; i <= 50; i++) {
			if (titaVo.get("RpCode" + i) == null || parse.stringToInteger(titaVo.getParam("RpCode" + i)) == 0)
				break;
			if (parse.stringToInteger(titaVo.getParam("RpCode" + i)) != 90) {
				wkTxAmtRemaind = wkTxAmtRemaind.add(parse.stringToBigDecimal(titaVo.getParam("RpAmt" + i))); // 交易還款餘額
			}
			if (parse.stringToInteger(titaVo.getParam("RpCode" + i)) == 90) {
				iTmpAmt = iTmpAmt.subtract(parse.stringToBigDecimal(titaVo.getParam("RpAmt" + i))); // 暫收抵繳金額
			}
		}

		// 帳務處理
		if (this.txBuffer.getTxCom().isBookAcYes()) {
			// 借方：收付欄
			acPaymentCom.setTxBuffer(this.getTxBuffer());
			acPaymentCom.run(titaVo);

			// 貸方：費用、短繳期金
			batxSettleUnpaid();
		}

		if (titaVo.isHcodeNormal()) {
			ReplyNormalRoutine();
		} else {
			ReplyEraseRoutine();
		}

		// 帳務處理
		if (this.txBuffer.getTxCom().isBookAcYes()) {
			// 貸方： 本金利息
			this.txBuffer.addAllAcDetailList(lAcDetail);

			// 產生會計分錄
			acDetailCom.setTxBuffer(this.txBuffer);
			acDetailCom.run(titaVo);
		}

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
			CalcRepayRoutine();
			tFacMain = facMainService.holdById(new FacMainId(iCustNo, iFacmNo));
			if (tFacMain.getActFg() == 1) {
				throw new LogicException(titaVo, "E0021",
						"額度檔 戶號 = " + tFacMain.getCustNo() + " 額度編號 =  " + tFacMain.getFacmNo()); // 該筆資料待放行中
			}
			tFacMain.setUtilAmt(tFacMain.getUtilAmt().subtract(wkPrincipal)); // 貸出金額(放款餘額)
			if (tFacMain.getRecycleCode().equals("1")) {
				tFacMain.setUtilBal(tFacMain.getUtilBal().subtract(wkPrincipal));// 已動用額度餘額
			}
			try {
				facMainService.update(tFacMain);
			} catch (DBException e) {
				throw new LogicException(titaVo, "E0007", "額度主檔 戶號 = " + iCustNo + " 額度編號 = " + wkFacmNo); // 更新資料時，發生錯誤
			}
			// 新增交易暫存檔(放款資料)
			AddTxTempBormRoutine(od);
			// 更新撥款主檔
			UpdLoanBorMainRoutine();
			// 帳務處理
			AcDetailDbCrRoutine(od);
			// 計算本筆暫收金額
			compTempAmt();
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

		Slice<TxTemp> slTxTemp = txTempService.txTempTxtNoEq(titaVo.getOrgEntdyI() + 19110000, titaVo.getOrgKin(),
				titaVo.getOrgTlr(), titaVo.getOrgTno(), 0, Integer.MAX_VALUE);
		lTxTemp = slTxTemp == null ? null : slTxTemp.getContent();
		if (lTxTemp == null || lTxTemp.size() == 0) {
			throw new LogicException(titaVo, "E0001", "交易暫存檔 分行別 = " + titaVo.getOrgKin() + " 交易員代號 = "
					+ titaVo.getOrgTlr() + " 交易序號 = " + titaVo.getOrgTno()); // 查詢資料不存在
		}
		for (TxTemp tx : lTxTemp) {
			wkCustNo = this.parse.stringToInteger(tx.getSeqNo().substring(0, 7));
			wkFacmNo = this.parse.stringToInteger(tx.getSeqNo().substring(7, 10));
			wkBormNo = this.parse.stringToInteger(tx.getSeqNo().substring(10, 13));
			wkOvduNo = this.parse.stringToInteger(tx.getSeqNo().substring(13, 16));
			tTempVo = tTempVo.getVo(tx.getText());
			wkBorxNo = this.parse.stringToInteger(tTempVo.get("BorxNo"));
			wkPrincipal = this.parse.stringToBigDecimal(tTempVo.get("Principal"));
			wkInterest = this.parse.stringToBigDecimal(tTempVo.get("Interest"));
			wkDelayInt = this.parse.stringToBigDecimal(tTempVo.get("DelayInt"));
			wkBreachAmt = this.parse.stringToBigDecimal(tTempVo.get("BreachAmt"));
			this.info("   SeqNo    = " + tx.getSeqNo());
			this.info("   wkCustNo = " + wkCustNo);
			this.info("   wkFacmNo = " + wkFacmNo);
			this.info("   wkBormNo = " + wkBormNo);
			this.info("   wkBorxNo = " + wkBorxNo);
			this.info("   wkOvduNo = " + wkOvduNo);
			// 鎖定額度檔
			tFacMain = facMainService.findById(new FacMainId(wkCustNo, wkFacmNo));
			if (tFacMain == null) {
				throw new LogicException(titaVo, "E3011", "額度主檔 戶號 = " + wkCustNo + " 額度編號 = " + wkFacmNo); // 鎖定資料時，發生錯誤
			}
			if (tFacMain.getActFg() == 1) {
				throw new LogicException(titaVo, "E0021",
						"額度檔 戶號 = " + tFacMain.getCustNo() + " 額度編號 =  " + tFacMain.getFacmNo()); // 該筆資料待放行中
			}
			// 訂正時, 循環額度須還原額度檔
			tFacMain = facMainService.holdById(new FacMainId(wkCustNo, wkFacmNo));
			if (tFacMain.getActFg() == 1) {
				throw new LogicException(titaVo, "E0021",
						"額度檔 戶號 = " + tFacMain.getCustNo() + " 額度編號 =  " + tFacMain.getFacmNo()); // 該筆資料待放行中
			}
			tFacMain.setUtilAmt(tFacMain.getUtilAmt().add(wkPrincipal));
			if (tFacMain.getRecycleCode().equals("1")) {
				tFacMain.setUtilBal(tFacMain.getUtilBal().add(wkPrincipal));
			}
			try {
				facMainService.update(tFacMain);
			} catch (DBException e) {
				throw new LogicException(titaVo, "E0007", "額度主檔 戶號 = " + iCustNo + " 額度編號 = " + wkFacmNo); // 更新資料時，發生錯誤
			}
			// 還原撥款主檔
			RestoredLoanBorMainRoutine();
			// 註記交易內容檔
			loanCom.setLoanBorTxHcode(wkCustNo, wkFacmNo, wkBormNo, wkBorxNo, wkNewBorxNo, tLoanBorMain.getLoanBal(),
					titaVo);
			// 還原催收檔
			RestoredLoanOverdueRoutine();
			// 帳務處理
			AcDetailDbCrRoutine(tLoanOverdue);
		}

	}

	// 計算至入帳日期應繳之金額
	private void CalcRepayRoutine() throws LogicException {
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

		tTxTemp = new TxTemp();
		tTxTempId = new TxTempId();
		loanCom.setTxTemp(tTxTempId, tTxTemp, iCustNo, iFacmNo, wkBormNo, wkOvduNo, titaVo);
		tTempVo.clear();
		tTempVo.putParam("BorxNo", wkBorxNo);
		tTempVo.putParam("Status", tLoanBorMain.getStatus());
		tTempVo.putParam("StoreRate", tLoanBorMain.getStoreRate());
		tTempVo.putParam("Principal", wkPrincipal);
		tTempVo.putParam("Interest", wkInterest);
		tTempVo.putParam("DelayInt", wkDelayInt);
		tTempVo.putParam("BreachAmt", wkBreachAmt);
		tTempVo.putParam("LoanBal", tLoanBorMain.getLoanBal());
		tTempVo.putParam("RepaidPeriod", tLoanBorMain.getRepaidPeriod());
		tTempVo.putParam("PaidTerms", tLoanBorMain.getPaidTerms());
		tTempVo.putParam("PrevPayIntDate", tLoanBorMain.getPrevPayIntDate());
		tTempVo.putParam("PrevRepaidDate", tLoanBorMain.getPrevRepaidDate());
		tTempVo.putParam("NextPayIntDate", tLoanBorMain.getNextPayIntDate());
		tTempVo.putParam("NextRepayDate", tLoanBorMain.getNextRepayDate());
		tTempVo.putParam("DueAmt", tLoanBorMain.getDueAmt());
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
		tTxTemp.setText(tTempVo.getJsonString());
		try {
			txTempService.insert(tTxTemp);
		} catch (DBException e) {
			throw new LogicException(titaVo, "E0005", "交易暫存檔 Key = " + tTxTempId); // 新增資料時，發生錯誤 }
		}
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
		tLoanBorTx.setDesc("催收回復登錄");
		tLoanBorTx.setRepayCode(iRpCode); // 還款來源
		tLoanBorTx.setEntryDate(iEntryDate);
		tLoanBorTx.setDueDate(wkDueDate);
		tLoanBorTx.setTxAmt(this.parse.stringToBigDecimal(titaVo.getTxAmt()));
		tLoanBorTx.setLoanBal(tLoanBorMain.getLoanBal());
		tLoanBorTx.setRate(tLoanBorMain.getStoreRate());
		tLoanBorTx.setIntStartDate(wkIntStartDate);
		tLoanBorTx.setIntEndDate(wkIntEndDate);
		tLoanBorTx.setPaidTerms(wkPaidTerms);
		tLoanBorTx.setPrincipal(wkPrincipal);
		tLoanBorTx.setInterest(wkInterest);
		tLoanBorTx.setDelayInt(wkDelayInt);
		tLoanBorTx.setBreachAmt(wkBreachAmt);
		tLoanBorTx.setCloseBreachAmt(wkCloseBreachAmt);
		tLoanBorTx.setTempAmt(wkTempAmt); // 暫收抵繳金額
		// 繳息首筆、繳息次筆
		if (isFirstOvdu) {
			tLoanBorTx.setDisplayflag("F"); // 繳息首筆
			tLoanBorTx.setTxAmt(iTxAmt);
		} else {
			tLoanBorTx.setDisplayflag("I"); // 繳息次筆
		}
		// 其他欄位
		tTempVo.clear();
		// 減免金額
		if (wkReduceAmt.compareTo(BigDecimal.ZERO) > 0) {
			tTempVo.putParam("ReduceAmt", wkReduceAmt);
		}
		if (wkReduceBreachAmt.compareTo(BigDecimal.ZERO) > 0) {
			tTempVo.putParam("ReduceBreachAmt", wkReduceBreachAmt); // 減免違約金+減免延滯息
		}
		// 支票繳款利息免印花稅
		if (iRpCode == 4) {
			tTempVo.putParam("StampFreeAmt", wkInterest.add(wkDelayInt).add(wkBreachAmt).add(wkCloseBreachAmt));
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
		tTempVo.putParam("BeforeLoanBal", wkBeforeLoanBal); // 交易前放款餘額
		tTempVo.putParam("PaidTerms", wkPaidTerms);
		tLoanBorTx.setOtherFields(tTempVo.getJsonString());
		try {
			loanBorTxService.insert(tLoanBorTx);
		} catch (DBException e) {
			throw new LogicException(titaVo, "E0005", "放款交易內容檔 " + e.getErrorMsg()); // 新增資料時，發生錯誤
		}
	}

	// 帳務處理
	private void AcDetailDbCrRoutine(LoanOverdue od) throws LogicException {
		this.info("AcDetailCrRoutine ... " + od.toString());
		this.info("   isBookAcYes = " + this.txBuffer.getTxCom().isBookAcYes());

		if (!this.txBuffer.getTxCom().isBookAcYes()) {
			return;
		}
		// 借:放款
		acDetail = new AcDetail();
		acDetail.setDbCr("D");
		acDetail.setAcctCode(tFacMain.getAcctCode());
		acDetail.setTxAmt(wkBeforeLoanBal);
		acDetail.setCustNo(wkCustNo);
		acDetail.setFacmNo(wkFacmNo);
		acDetail.setBormNo(wkBormNo);
		lAcDetail.add(acDetail);
		// 借:利息(轉催收利息大於利息則回沖差額)
		if (od.getOvduIntAmt().compareTo(wkInterest.subtract(wkShortfallInterest)) > 0) {// 回收短繳另外出帳)
			acDetail = new AcDetail();
			acDetail.setDbCr("D");
			acDetail.setAcctCode(loanCom.setIntAcctCode(tFacMain.getAcctCode()));
			acDetail.setTxAmt(od.getOvduIntAmt().subtract(wkInterest.subtract(wkShortfallInterest)));
			acDetail.setCustNo(wkCustNo);
			acDetail.setFacmNo(wkFacmNo);
			acDetail.setBormNo(wkBormNo);
			lAcDetail.add(acDetail);
		}
		// 貸:催收款項
		acDetail = new AcDetail();
		acDetail.setDbCr("C");
		acDetail.setAcctCode("990");
		acDetail.setTxAmt(od.getOvduBal());
		acDetail.setCustNo(wkCustNo);
		acDetail.setFacmNo(wkFacmNo);
		acDetail.setBormNo(wkBormNo);
		lAcDetail.add(acDetail);
		// 本金
		acDetail = new AcDetail();
		acDetail.setDbCr("C");
		acDetail.setAcctCode(tFacMain.getAcctCode());
		acDetail.setTxAmt(wkPrincipal.subtract(wkShortfallPrincipal)); // 回收短繳另外出帳;
		acDetail.setCustNo(wkCustNo);
		acDetail.setFacmNo(wkFacmNo);
		acDetail.setBormNo(wkBormNo);
		lAcDetail.add(acDetail);
		// 利息(利息大於轉催收利息則差額出帳)
		if (od.getOvduIntAmt().compareTo(wkInterest.subtract(wkShortfallInterest)) < 0) { // 回收短繳另外出帳;
			acDetail = new AcDetail();
			acDetail.setDbCr("C");
			acDetail.setAcctCode(loanCom.setIntAcctCode(tFacMain.getAcctCode()));
			acDetail.setTxAmt(wkInterest.subtract(wkShortfallInterest).subtract(od.getOvduIntAmt()));
			acDetail.setCustNo(wkCustNo);
			acDetail.setFacmNo(wkFacmNo);
			acDetail.setBormNo(wkBormNo);
			lAcDetail.add(acDetail);
		}
		// 延滯息
		acDetail = new AcDetail();
		acDetail.setDbCr("C");
		acDetail.setAcctCode("IOV");
		acDetail.setTxAmt(wkDelayInt);
		acDetail.setCustNo(wkCustNo);
		acDetail.setFacmNo(wkFacmNo);
		acDetail.setBormNo(wkBormNo);
		lAcDetail.add(acDetail);
		// 違約金
		acDetail = new AcDetail();
		acDetail.setDbCr("C");
		acDetail.setAcctCode("IOV");
		acDetail.setTxAmt(wkBreachAmt);
		acDetail.setCustNo(wkCustNo);
		acDetail.setFacmNo(wkFacmNo);
		acDetail.setBormNo(wkBormNo);
		lAcDetail.add(acDetail);

	}

	// 貸方：費用、短繳期金
	private void batxSettleUnpaid() throws LogicException {
		this.baTxList = new ArrayList<BaTxVo>();
		// call 應繳試算
		this.baTxList = baTxCom.settingUnPaid(titaVo.getEntDyI(), iCustNo, iFacmNo, 0, 99, BigDecimal.ZERO, titaVo); // //
		// 99-費用全部(含未到期)
		wkTotalFee = baTxCom.getShortfall().add(baTxCom.getModifyFee()).add(baTxCom.getAcctFee())
				.add(baTxCom.getFireFee()).add(baTxCom.getLawFee()).add(baTxCom.getCollFireFee())
				.add(baTxCom.getCollLawFee());
		this.info("未收費用 =" + wkTotalFee);
	}

	// 貸方：費用、短繳期金
	private void getSettleUnpaid() throws LogicException {
		this.wkAcctFee = BigDecimal.ZERO;
		this.wkModifyFee = BigDecimal.ZERO;
		this.wkFireFee = BigDecimal.ZERO;
		this.wkLawFee = BigDecimal.ZERO;
		this.wkShortfallInterest = BigDecimal.ZERO; // 累短收 - 利息
		this.wkShortfallPrincipal = BigDecimal.ZERO; // 累短收 - 本金
		this.wkShortCloseBreach = BigDecimal.ZERO; // 累短收 - 清償違約金
		// RepayType 同撥款：01-期款, 第一筆：04-帳管費, 05-火險費, 06-契變手續費, 07-法務費
		if (this.baTxList != null) {
			for (BaTxVo ba : this.baTxList) {
				if (ba.getAcctAmt().compareTo(BigDecimal.ZERO) > 0) {
					if ((ba.getRepayType() == 1 && (ba.getFacmNo() == wkFacmNo || ba.getFacmNo() == 0)
							&& (ba.getBormNo() == wkBormNo || ba.getBormNo() == 0)) || ba.getRepayType() > 1) {
						acDetail = new AcDetail();
						acDetail.setDbCr("C");
						acDetail.setAcctCode(ba.getAcctCode());
						acDetail.setTxAmt(ba.getAcctAmt());
						acDetail.setCustNo(ba.getCustNo());
						acDetail.setFacmNo(ba.getFacmNo());
						acDetail.setBormNo(ba.getBormNo());
						acDetail.setRvNo(ba.getRvNo());
						acDetail.setReceivableFlag(ba.getReceivableFlag());
						lAcDetail.add(acDetail);
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

	// 計算暫收款金額
	private void compTempAmt() throws LogicException {
		// 還款總金額
		BigDecimal wkAcTotal = BigDecimal.ZERO;
		for (AcDetail ac : lAcDetail) {
			if ("C".equals(ac.getDbCr())) {
				wkAcTotal = wkAcTotal.add(ac.getTxAmt());
			} else {
				wkAcTotal = wkAcTotal.subtract((ac.getTxAmt()));
			}
		}
		// 本筆還款金額 、累計還款總金額
		BigDecimal wkRepayAmt = wkAcTotal.subtract(this.wkTotalRepay);
		this.wkTotalRepay = wkAcTotal;

		// 本筆暫收款金額
		this.wkTempAmt = BigDecimal.ZERO;

		if (iOverAmt.compareTo(BigDecimal.ZERO) == 0 && iTmpAmt.compareTo(BigDecimal.ZERO) == 0) {
			return;
		}

		// 有溢繳款放第一筆(暫收款金額為正值)
		if (iOverAmt.compareTo(BigDecimal.ZERO) > 0) {
			if (isFirstOvdu) {
				this.wkTempAmt = iOverAmt;
			}
			return;
		}

		// 暫收抵繳金額(暫收款金額為負值) = 交易還款餘額 - 本筆還款金額
		if (wkRepayAmt.compareTo(this.wkTxAmtRemaind) < 0) {
			wkTempAmt = BigDecimal.ZERO;
			this.wkTxAmtRemaind = this.wkTxAmtRemaind.subtract(wkRepayAmt);
		} else {
			wkTempAmt = this.wkTxAmtRemaind.subtract(wkRepayAmt);
			this.wkTxAmtRemaind = BigDecimal.ZERO;
		}
	}
}