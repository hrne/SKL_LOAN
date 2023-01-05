package com.st1.itx.util.common;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Component;

import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.TempVo;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.dataVO.TotaVo;
import com.st1.itx.db.domain.AcDetail;
import com.st1.itx.db.domain.AcReceivable;
import com.st1.itx.db.domain.FacMain;
import com.st1.itx.db.domain.LoanBook;
import com.st1.itx.db.domain.LoanBorMain;
import com.st1.itx.db.domain.LoanFacTmp;
import com.st1.itx.db.service.AcReceivableService;
import com.st1.itx.db.service.LoanBookService;
import com.st1.itx.db.service.LoanBorMainService;
import com.st1.itx.db.service.LoanFacTmpService;
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.common.data.BaTxVo;
import com.st1.itx.util.common.data.CalcRepayIntVo;
import com.st1.itx.util.common.data.LoanCloseBreachVo;
import com.st1.itx.util.date.DateUtil;
import com.st1.itx.util.parse.Parse;

/**
 * 還款入帳試算<BR>
 * 1.settingUnPaid：應繳試算 call by L3XXX、L4XXX(產出扣款檔)、Report<BR>
 * 2.settleUnPaid：還款試算 call by TxBatchCom(整批入帳)<BR>
 * 3.acLoanInt：提存試算 call by BS900(月底提存計息)<BR>
 * 4.termsPay： 應繳期款by期數 call by Report<BR>
 * 5.cashFlow： 現金流量預估 call by BS060 現金流量預估資料檔維護<BR>
 * 
 * @author st1
 *
 */
@Component("baTxCom")
@Scope("prototype")
public class BaTxCom extends TradeBuffer {

	/* 轉型共用工具 */
	@Autowired
	public Parse parse;
	@Autowired
	DateUtil dDateUtil;
	@Autowired
	LoanCom loanCom;
	@Autowired
	LoanSetRepayIntCom loanSetRepayIntCom;
	@Autowired
	LoanCalcRepayIntCom loanCalcRepayIntCom;
	@Autowired
	LoanCloseBreachCom loanCloseBreachCom;
	@Autowired
	FacStatusCom facStatusCom;

	@Autowired
	public AcReceivableService acReceivableService;
	@Autowired
	public LoanBorMainService loanBorMainService;
	@Autowired
	public LoanBookService loanBookService;
	@Autowired
	public LoanFacTmpService loanFacTmpService;

	private BaTxVo baTxVo;
	private ArrayList<BaTxVo> baTxList;
	private TempVo tempVo;
	private List<AcReceivable> rvList;
	private Slice<LoanBorMain> slLoanBorMain;
	private List<LoanBorMain> lLoanBorMain;
	private List<LoanFacTmp> lLoanFacTmp;
	private BigDecimal txBal = BigDecimal.ZERO; // txBal 回收餘額
	private BigDecimal xxBal = BigDecimal.ZERO; // xxBal 可償還金額
	private BigDecimal calcLoanBal = BigDecimal.ZERO; // 還款前本金餘額
	private BigDecimal tavAmt = BigDecimal.ZERO; // tavAmt 可暫收抵繳金額
	private BigDecimal mergeAmt = BigDecimal.ZERO; // mergeAmt 合併檢核總金額
	private BigDecimal shortAmt = BigDecimal.ZERO; // 短繳(正值)
	private BigDecimal overAmt = BigDecimal.ZERO; // 溢繳(正值)
	private int shortFacmNo = 0; // 短繳額度;
	private int overRpFacmNo = 0; // 溢繳額度;
	private int facStatus = 0; // 戶況

	// isPayAllFee 是否全部回收費用
	// true->回收全部費用，false 回收金額足夠的費用
	// RepayType 還款類別
	// 1.期款 3.結案 => true
	// 2.部分償還 按約定，未約定 => true
	// 4.帳管費 5.火險費 6.契變手續費 7.法務費 => false
	private boolean isPayAllFee = false;

	// 未到期火險費用條件 0.全列已到期、1.續約保單起日 > 入帳月
	private int isUnOpenfireFee = 0;

// isAcLoanInt 是否利息提存
	private boolean isAcLoanInt = false;

// isEmptyLoanBaTxVo 是否放未計息餘額
	private boolean isEmptyLoanBaTxVo = false;

// isLoadRvList 是否LoadRvList
	private boolean isLoadRvList = true;

// 總數資料
	private BigDecimal loanBal = BigDecimal.ZERO; // 還款前本金餘額
	private BigDecimal shortfall = BigDecimal.ZERO; // 累短收
	private BigDecimal shortfallInterest = BigDecimal.ZERO; // 累短收 - 利息
	private BigDecimal shortfallPrincipal = BigDecimal.ZERO; // 累短收 - 本金
	private BigDecimal shortCloseBreach = BigDecimal.ZERO; // 累短收 - 清償違約金
	private BigDecimal excessive = BigDecimal.ZERO; // 累溢收
	private BigDecimal excessiveOther = BigDecimal.ZERO; // 其他額度累溢收
	private BigDecimal tempTax = BigDecimal.ZERO; // 暫付所得稅
	private BigDecimal modifyFee = BigDecimal.ZERO; // 契變手續費 F29
	private BigDecimal acctFee = BigDecimal.ZERO; // 帳管費/手續費 F10 F12 F27
	private BigDecimal fireFee = BigDecimal.ZERO; // 火險費用 TMI F09
	private BigDecimal unOpenfireFee = BigDecimal.ZERO; // 未到期火險費用
	private BigDecimal collFireFee = BigDecimal.ZERO; // 催收火險費 F25
	private BigDecimal lawFee = BigDecimal.ZERO; // 法務費用 F07
	private BigDecimal collLawFee = BigDecimal.ZERO; // 催收法務費 F24
	private BigDecimal totalFee = BigDecimal.ZERO; // 費用總額
	private BigDecimal principal = BigDecimal.ZERO; // 本金
	private BigDecimal interest = BigDecimal.ZERO; // 利息
	private BigDecimal delayInt = BigDecimal.ZERO; // 延滯息
	private BigDecimal breachAmt = BigDecimal.ZERO; // 違約金
	private BigDecimal closeBreachAmt = BigDecimal.ZERO; // 清償違約金
	private BigDecimal tempAmt = BigDecimal.ZERO; // 暫收款金額(存入暫收為正、暫收抵繳為負)
	private BigDecimal repayTotal = BigDecimal.ZERO; // 還款總金額
	private BigDecimal shortAmtLimit = BigDecimal.ZERO;// 短繳限額
	private BigDecimal unPayLoan = BigDecimal.ZERO;// 應繳本利
	private int rateEffectDate = 0; // 目前利率生效日
	private BigDecimal fitRate = BigDecimal.ZERO; // 目前利率
	private int repayIntDate = 0; // 還款應繳日
	private TempVo repayIntDateByFacmNoVo = new TempVo(); // 額度還款應繳日
	private int prevPayIntDate; // 上次繳息日
	private int nextPayIntDate; // 下次繳息日
	private int ovduTerms = 0; // 逾期數
	private int ovduDays = 0; // 逾期天數
	private int terms = 0; // 繳期數
	private int endFlag = 0; // 0:正常 1.至下次利率調整日
	private String unpaidFlag = "";// 是否可欠繳 Y/N
	private int preRepayTerms = 0; // 可預收期數
	private BigDecimal extraRepay = BigDecimal.ZERO; // 部分還款金額
	private String includeIntFlag = "";// 是否內含利息
	private String includeFeeFlag = "";// 是否內含費用
	private String unpaidIntFlag = "";// 利息是否可欠繳
	private String payFeeFlag = "";// 是否回收費用
	private String unPayFeeX = "";// 未收費用
	private String payMethod = "";// 繳納方式 1.減少每期攤還金額 2.縮短應繳期數
	private String tmpFacmNoX = "";// 暫收指定額度
	private DecimalFormat df = new DecimalFormat("##,###,###,###,##0");

	private String payFeeMethod = "";// 回收費用方式 Y/N

	// initialize variable
	@PostConstruct
	public void initial() {
		this.payFeeMethod = "";
	}

//  initialize
	private void init() {
		// 是否回收費用 Y-是 N-否
		if ("N".equals(this.payFeeMethod)) {
			this.payFeeFlag = "N";
		} else {
			this.payFeeFlag = "Y";
		}

		this.baTxVo = new BaTxVo();
		this.baTxList = new ArrayList<BaTxVo>();
		this.tempVo = new TempVo();
		this.lLoanBorMain = new ArrayList<LoanBorMain>();
		this.lLoanFacTmp = new ArrayList<LoanFacTmp>();
		this.rvList = null;
		this.slLoanBorMain = null;
		this.isLoadRvList = true;
		this.txBal = BigDecimal.ZERO; // 回收餘額
		this.xxBal = BigDecimal.ZERO; // 可償還金額
		this.loanBal = BigDecimal.ZERO; // 還款前本金餘額
		this.shortAmt = BigDecimal.ZERO; // 短繳(正值)
//		this.shortFacmNo = 0; // 短繳額度;
		this.overAmt = BigDecimal.ZERO; // 溢繳(正值)
//		this.overRpFacmNo = 0; // 溢短繳額度;
		this.facStatus = 0; // 戶況
		this.mergeAmt = BigDecimal.ZERO; // 合併檢核總金額
		this.tmpFacmNoX = ""; // 暫收指定額度

		// 本金、利息
		this.principal = BigDecimal.ZERO; // 本金
		this.interest = BigDecimal.ZERO; // 利息
		this.delayInt = BigDecimal.ZERO; // 延滯息
		this.breachAmt = BigDecimal.ZERO; // 違約金
		this.closeBreachAmt = BigDecimal.ZERO; // 清償違約金
		this.shortAmtLimit = BigDecimal.ZERO;// 短繳限額計算
		this.unPayLoan = BigDecimal.ZERO;// 應繳本利

		// 還款金額、日期、其他
		this.tempAmt = BigDecimal.ZERO; // 暫收款金額(存入暫收為正、暫收抵繳為負)
		this.repayTotal = BigDecimal.ZERO; // 總還款金額
		this.ovduTerms = 0; // 逾期數
		this.ovduDays = 0; // 逾期天數
		this.terms = 0; // 繳期數
		this.rateEffectDate = 0; // 目前利率生效日
		this.fitRate = BigDecimal.ZERO; // 目前利率
		this.calcLoanBal = BigDecimal.ZERO; // 還款前本金餘額

		// 期款
		this.unpaidFlag = "Y";// 是否可欠繳
		this.preRepayTerms = 0; // 期款可預收期數
		this.repayIntDate = 0; // 還款應繳日
		this.repayIntDateByFacmNoVo = new TempVo(); // 額度還款應繳日
		this.prevPayIntDate = 0; // 上次繳息日
		this.nextPayIntDate = 0; // 下次應繳日

		// 部分還款
		this.extraRepay = BigDecimal.ZERO; // 部分還款金額
		this.includeFeeFlag = "N";// 是否內含費用
		this.includeIntFlag = "N";// 是否內含利息
		this.unpaidIntFlag = "Y";// 利息是否可欠繳
		this.payMethod = "1";// 繳納方式 1.減少每期攤還金額 2.縮短應繳期數
		this.endFlag = 0; // 0:正常 1.至下次利率調整日

	}

	private void initRv() {
		this.tavAmt = BigDecimal.ZERO; // 可暫收抵繳金額
		// 費用、短繳
		this.shortfall = BigDecimal.ZERO; // 累短收
		this.shortfallInterest = BigDecimal.ZERO; // 累短收 - 利息
		this.shortfallPrincipal = BigDecimal.ZERO; // 累短收 - 本金
		this.shortCloseBreach = BigDecimal.ZERO; // 累短收 - 清償違約金
		this.excessive = BigDecimal.ZERO; // 累溢收
		this.excessiveOther = BigDecimal.ZERO; // 其他額度累溢收

		this.tempTax = BigDecimal.ZERO; // 暫付所得稅
		this.modifyFee = BigDecimal.ZERO; // 契變手續費 F29
		this.acctFee = BigDecimal.ZERO; // 帳管費/手續費 F10 F12 F27
		this.fireFee = BigDecimal.ZERO; // 火險費用 TMI F09
		this.unOpenfireFee = BigDecimal.ZERO; // 未到期火險費用
		this.collFireFee = BigDecimal.ZERO; // 催收火險費 F25
		this.lawFee = BigDecimal.ZERO; // 法務費用 F07
		this.collLawFee = BigDecimal.ZERO; // 催收法務費 F24
		this.totalFee = BigDecimal.ZERO; // 費用總額
		this.unPayFeeX = "";// 未收費用
	}

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		return null;
	}

	/**
	 * 應繳試算 call by L3XXX、L4XXX(產出扣款檔)、Report<BR>
	 * 
	 * @param iEntryDate 入帳日
	 * @param iCustNo    戶號
	 * @param iFacmNo    額度
	 * @param iBormNo    撥款
	 * @param iRepayType 還款類別
	 * @param iTxAmt     入帳金額
	 * @param titaVo     TitaVo
	 * @return ArrayList of BaTxVo
	 * @throws LogicException ...
	 */

	public ArrayList<BaTxVo> settingUnPaid(int iEntryDate, int iCustNo, int iFacmNo, int iBormNo, int iRepayType,
			BigDecimal iTxAmt, TitaVo titaVo) throws LogicException {
		this.baTxList = settingPayintDate(iEntryDate, iEntryDate, iCustNo, iFacmNo, iBormNo, iRepayType, iTxAmt,
				titaVo);
		return this.baTxList;
	}

	/**
	 * 應繳試算
	 * 
	 * @param iEntryDate  入帳日
	 * @param iPayIntDate 應繳日
	 * @param iCustNo     戶號
	 * @param iFacmNo     額度
	 * @param iBormNo     撥款
	 * @param iRepayType  還款類別
	 * @param iTxAmt      入帳金額
	 * @param titaVo      TitaVo
	 * @return ArrayList of BaTxVo
	 * @throws LogicException ...
	 */
	public ArrayList<BaTxVo> settingPayintDate(int iEntryDate, int iPayIntDate, int iCustNo, int iFacmNo, int iBormNo,
			int iRepayType, BigDecimal iTxAmt, TitaVo titaVo) throws LogicException {
		this.info("BaTxCom settingUnPaid ...");
		this.info("BaTxCom settingUnPaid EntryDate  入帳日=" + iEntryDate);
		this.info("BaTxCom settingUnPaid PayIntDate 應繳日=" + iPayIntDate);
		this.info("BaTxCom settingUnPaid 戶號=" + iCustNo + "-" + iFacmNo + "-" + iBormNo);
		this.info("BaTxCom settingUnPaid RepayType 還款類別=" + iRepayType);
		this.info("BaTxCom settingUnPaid TxAmt 回收金額=" + iTxAmt);
		this.info("BaTxCom settingUnPaid TxAmt 回收費用方式=" + this.payFeeMethod);
		init();
		this.info("BaTxCom settingUnPaid TxAmt 是否回收費用=" + this.payFeeMethod);

		// STEP 1: 設定預設值
		// isPayAllFee 費用是否全部回收->
		if (iRepayType <= 3 || iRepayType == 99) {
			this.isPayAllFee = true; // 全部
		} else {
			this.isPayAllFee = false; // 部分
		}

		// isEmptyLoanBaTxVo 是否放未計息餘額
		this.isEmptyLoanBaTxVo = false;

		// 還款類別:02 與約定還本日期相同
		if (iRepayType == 2) {
			this.payFeeFlag = "N";
			this.extraRepay = iTxAmt; // 部分還款金額
			Slice<LoanBook> slLoanBook = loanBookService.bookCustNoRange(iCustNo, iCustNo, iFacmNo,
					iFacmNo > 0 ? iFacmNo : 999, iBormNo, iBormNo > 0 ? iBormNo : 900, iEntryDate, 0, Integer.MAX_VALUE,
					titaVo);
			if (slLoanBook != null) {
				for (LoanBook tLoanBook : slLoanBook.getContent()) {
					if (tLoanBook.getStatus() == 0) {
						if (iTxAmt.compareTo(BigDecimal.ZERO) == 0 || iTxAmt.compareTo(tLoanBook.getBookAmt()) >= 0) {
							iFacmNo = tLoanBook.getFacmNo(); // 還款額度
							iBormNo = tLoanBook.getBormNo(); // 撥款序號
							this.payFeeFlag = tLoanBook.getIncludeFeeFlag();
							if (iTxAmt.compareTo(BigDecimal.ZERO) == 0) {
								this.extraRepay = tLoanBook.getBookAmt(); // 部分還款金額
							}
							this.includeIntFlag = tLoanBook.getIncludeIntFlag(); // 是否內含利息
							this.unpaidIntFlag = tLoanBook.getUnpaidIntFlag();// 利息是否可欠繳
							this.payMethod = tLoanBook.getPayMethod();// 繳納方式 1.減少每期攤還金額 2.縮短應繳期數
							break;
						}
					}
				}
			}
		}

		// STEP 2: Load factmp、UnPaid

		// Load 暫收指定額度清單
		loadFacTmpList(iCustNo, titaVo);

		// Load UnPaid 1.應收費用+未收費用+短繳期金 3.暫收抵繳 6.另收欠款
		loadUnPaid(iPayIntDate, iCustNo, iFacmNo, iBormNo, iRepayType, titaVo);

		// STEP 3: 計算放款本息
		if (iRepayType == 2) {
			if ("Y".equals(this.payFeeFlag)) {
				this.extraRepay = this.extraRepay.subtract(this.totalFee); // 部分還款金額
				this.payFeeFlag = "Y";
			} else {
				if (this.tavAmt.compareTo(this.totalFee) >= 0) {
					this.payFeeFlag = "Y";
				} else {
					this.payFeeFlag = "N";
				}
			}
		}

		if (iRepayType >= 1 && iRepayType <= 3) {
			loadRepayLoan(iEntryDate, iCustNo, iFacmNo, iBormNo, iRepayType, titaVo); // 還款主檔清單
			calcRepayLoan(iEntryDate, iPayIntDate, iCustNo, iFacmNo, iBormNo, iRepayType, iTxAmt, 0, titaVo);
		}

		// STEP 4: 設定總金額
		this.txBal = iTxAmt; // 回收金額
		this.xxBal = this.txBal; // 可償還餘額
		// 加可抵繳至可償還餘額(費用類別不抵繳)
		if (iRepayType <= 3 || iRepayType >= 9) {
			this.xxBal = this.xxBal.add(this.tavAmt);
		}
		this.info("xxBal=" + this.xxBal);
		// STEP 5: 設定還款順序
		// 1.還款類別、金額相同 > 2.還款類別相同 > 3:未收費用 > 4:短繳期金 > 5:應繳本利 > 6:另收欠款
		settlePriority(iRepayType, iTxAmt); // 還款順序

		// STEP 6 : 計算作帳金額

		// 回收費用 1.還款類別(費用)相同 2.應收費用 3:未收費用
		if ("Y".equals(this.payFeeFlag)) {
			settleAcctAmt(1);
			settleAcctAmt(2);
			settleAcctAmt(3);
		}

		// 費用全部時收回 4:短繳期金
		if (iRepayType <= 3 || iRepayType == 99) {
			settleAcctAmt(4);
		}

		// 還放款時收回 5:應繳本利
		if (iRepayType >= 1 && iRepayType <= 3) {
			settleAcctAmt(5);
		}

		// STEP 7 : 計算作帳金額 3.暫收抵繳
		settleTmpAcctAmt();

		/* STEP 8 : 計算溢(C)短(D)繳 */
		settleOverAmt(iCustNo, iFacmNo, titaVo);

		// END
		if (this.baTxList != null) {
			for (BaTxVo ba : this.baTxList) {
				this.info("settingUnPaid " + ba.toString());
			}
		}
		return this.baTxList;
	}

	/**
	 * 還款試算 call by 整批入帳
	 * 
	 * @param iEntryDate  入帳日
	 * @param iPayIntDate 應繳日
	 * @param iCustNo     戶號
	 * @param iFacmNo     額度
	 * @param iBormNo     撥款
	 * @param iRepayCode  還款來源
	 * @param iRepayType  還款類別
	 * @param iTxAmt      入帳金額
	 * @param iTempVo     處理說明
	 * @param titaVo      TitaVo
	 * @return ArrayList of BaTxVo
	 * @throws LogicException ...
	 */
	public ArrayList<BaTxVo> settleUnPaid(int iEntryDate, int iPayIntDate, int iCustNo, int iFacmNo, int iBormNo,
			int iRepayCode, int iRepayType, BigDecimal iTxAmt, TempVo iTempVo, TitaVo titaVo) throws LogicException {

		this.info("BaTxCom settleUnPaid ...");
		this.info("BaTxCom settleUnPaid EntryDate  入帳日=" + iEntryDate);
		this.info("BaTxCom settleUnPaid PayintDate 應繳日=" + iPayIntDate);
		this.info("BaTxCom settleUnPaid 戶號=" + iCustNo + "-" + iFacmNo + "-" + iBormNo);
		this.info("BaTxCom settleUnPaid RepayCode 還款來源=" + iRepayCode);
		this.info("BaTxCom settleUnPaid RepayType 還款類別=" + iRepayType);
		this.info("BaTxCom settleUnPaid TxAmt 回收金額=" + iTxAmt);
		this.info("BaTxCom settleUnPaid TempVo 處理說明=" + iTempVo.toString());
// initial		
		init();
		this.tempVo = iTempVo;
		if (tempVo.get("PayFeeMethod") != null) {
			this.payFeeMethod = tempVo.get("PayFeeMethod");
		}

// STEP 1:  Load repayLoanList facTempList, UnPaidlist 

		// input 還款類別
		int inputRepayType = iRepayType;
		// input 還款額度
		int inputFacmNo = iFacmNo;

		// 回收金額(合併總金額)
		BigDecimal repayAmt = iTxAmt;
		if (tempVo.get("MergeAmt") != null) {
			this.mergeAmt = parse.stringToBigDecimal(tempVo.get("MergeAmt"));
			repayAmt = this.mergeAmt;
		}

		// Load 還款主檔清單
		if (iCustNo != this.txBuffer.getSystemParas().getLoanDeptCustNo()) {
			loadRepayLoan(iEntryDate, iCustNo, iFacmNo, iBormNo, iRepayType, titaVo);
			// Load 暫收指定額度清單
			loadFacTmpList(iCustNo, titaVo);
		}

		// 取得期款還款額度(期金是否相同)
		if (iRepayType == 1 && iFacmNo == 0) {
			iFacmNo = getDueAmtFacmNo(repayAmt);
		}

		// 依還款類別作回收排序(無還款類別同期款)後，依指定額度取得回收放款主檔清單
		if (iRepayType <= 2) {
			// 回收排序
			sortRepayLoan(iRepayType == 0 ? 1 : iRepayType, titaVo);
			// 取得還款額度
			iFacmNo = getRepayFacmNo(iFacmNo, this.overRpFacmNo);
			// 重新依還款類別及還款額度取得放款主檔清單
			reLoadRepayLoan(iRepayType, iFacmNo, titaVo);
		}

		// Load All UnPaid 1.應收費用+未收費用+短繳期金 3.暫收抵繳 6.另收欠款
		if (iCustNo != this.txBuffer.getSystemParas().getLoanDeptCustNo()) {
			loadUnPaid(iPayIntDate, iCustNo, iFacmNo, iBormNo, 99, titaVo);
		}

		// 銀扣期款不回收費用
		if (iRepayCode == 2 && iRepayType == 1) {
			this.payFeeFlag = "N";
			if (this.totalFee.compareTo(BigDecimal.ZERO) > 0) {
				this.tempVo.putParam("UnPayFeeX", this.unPayFeeX);
			}
		}

		// 費用或其他的還款額度
		if (iFacmNo == 0 && iRepayType > 4) {
			iFacmNo = getFeeRepayFacmNo(iRepayType, iTxAmt, this.overRpFacmNo);
		}

// STEP 2:  設定還款類別(還款類別 = 0)及還款額度
		if (iRepayType == 0) {
// 1).回收金額 = 費用金額 -> 4~9.費用或其他
			for (BaTxVo ba : this.baTxList) {
				// 相同還款金額(另收欠款)；應收及未收由轉暫收或放款回收時收回
				if (ba.getRepayType() >= 4 && ba.getDataKind() == 6 && iTxAmt.compareTo(ba.getUnPaidAmt()) == 0) {
					this.info("Feetype=" + ba.toString());
					iRepayType = ba.getRepayType();
					iFacmNo = getRepayFacmNo(iFacmNo, ba.getFacmNo()); // 取得還款額度
				}
				if (ba.getRepayType() >= 4 && repayAmt.compareTo(ba.getUnPaidAmt()) == 0) {
					this.info("Feetype=" + ba.toString());
					iRepayType = ba.getRepayType();
					iFacmNo = getRepayFacmNo(iFacmNo, ba.getFacmNo()); // 取得還款額度
				}
			}

// 2).戶況 > 0 -> 9.其他 
			if (iRepayType == 0 && this.facStatus > 0) {
				iRepayType = 9;
				iFacmNo = getRepayFacmNo(iFacmNo, this.overRpFacmNo); // 取得還款額度
			}

// 依部分償還 => 取得還款額度
			if (iRepayType == 0) {
				// 取得還款額度
				iFacmNo = getRepayFacmNo(iFacmNo, this.overRpFacmNo);
				// 重新依還款類別及還款額度取得放款主檔清單
				reLoadRepayLoan(iRepayType, iFacmNo, titaVo);
			}

// 3).回收金額 + 暫收款金額  > 放款餘額 -> 3.結案 
			if (iRepayType == 0 && this.loanBal.compareTo(BigDecimal.ZERO) > 0
					&& (repayAmt.add(this.tavAmt)).compareTo(this.loanBal) >= 0) {
				iRepayType = 3;
			}

// 4).應繳日   >= 到期日 -> 3.結案
			if (iRepayType == 0) {
				for (LoanBorMain ln : lLoanBorMain) {
					if (ln.getStatus() == 0 && iPayIntDate >= ln.getMaturityDate()) {
						iRepayType = 3;
						break;
					}
				}
			}

// 5).應繳日   >= 應繳日(會計日) -> 1.期款，不預繳
			if (iRepayType == 0 && this.nextPayIntDate > 0) {
				if (this.nextPayIntDate <= iPayIntDate) {
					iRepayType = 1;
					this.tempVo.putParam("PreRepayTerms", 0);
				}
			}

// 6).還款來源為暫收抵繳	-> 9 其他		
			if (iRepayType == 0 && iRepayCode == 90) {
				iRepayType = 9;
				iFacmNo = getRepayFacmNo(iFacmNo, this.overRpFacmNo);
			}

// 7).else -> 2.部分償還
			if (iRepayType == 0) {
				iRepayType = 2;
			}
			if (iRepayType == 1 || iRepayType == 2) {
				// 回收排序
				sortRepayLoan(iRepayType, titaVo);
				// 重新依還款類別及還款額度取得放款主檔清單
				reLoadRepayLoan(iRepayType, iFacmNo, titaVo);
				iFacmNo = getRepayFacmNo(iFacmNo, this.overRpFacmNo);
			}
		}
		// 回傳設定還款類別、還款額度
		this.tempVo.putParam("RepayType", iRepayType);
		this.tempVo.putParam("RepayFacmNo", iFacmNo);
		if (iRepayType != inputRepayType || iFacmNo != inputFacmNo) {
			this.info("inputRepayType=" + inputRepayType + "/" + iRepayType + ", inputFacmNo=" + inputFacmNo + "/"
					+ iFacmNo);
		}
// STEP 3: 例外處理

		// reLoad UnPaid 1.應收費用+未收費用+短繳期金 3.暫收抵繳 6.另收欠款
		this.baTxList = new ArrayList<BaTxVo>();
		loadUnPaid(iPayIntDate, iCustNo, iFacmNo, iBormNo, iRepayType, titaVo);

		// 合併檢核轉暫收，不回收費用
		if (tempVo.get("MergeAmt") != null) {
			if (iRepayType >= 1 && iRepayType <= 3) {
				if (!tempVo.get("MergeSeq").equals(tempVo.get("MergeCnt"))) {
					this.payFeeFlag = "N";
					iRepayType = 9;
				}
			}
		}

// STEP 4: 還款類別計算設定值
		// 費用是否全部回收
		if (iRepayType <= 3 || iRepayType == 99) {
			this.isPayAllFee = true; // 全部
		} else {
			this.isPayAllFee = false; // 部分
		}

		// isEmptyLoanBaTxVo 是否放未計息餘額
		this.isEmptyLoanBaTxVo = true;

// 1). 01-期款
		if (iRepayType == 1) {
			// 暫收抵繳期款，不可預收，不可欠繳
			if (iRepayCode == 90) {
				this.preRepayTerms = 0;
				this.unpaidFlag = "N";
			} else
			// 按設定預收期數
			if (tempVo.get("PreRepayTerms") != null) {
				this.preRepayTerms = parse.stringToInteger(tempVo.get("PreRepayTerms"));
			}
			// 按批次預收期數
			else {
				this.preRepayTerms = this.txBuffer.getSystemParas().getPreRepayTermsBatch();
			}
		}
// 3). 02-部分償還
		if (iRepayType == 2) {
			// 有期款未回收，變更還款類別為期款 -> 下次應繳日 < 應繳日
			if (this.nextPayIntDate <= iPayIntDate) {
				iRepayType = 1;
				tempVo.putParam("RepayTypeChange", 1);
				tempVo.putParam("NextPayIntDate", this.nextPayIntDate); // 下次應繳日
			}
			this.extraRepay = repayAmt; // 部分還款金額
			// 有約定還本按約定設定
			if (tempVo.get("IncludeIntFlag)") != null) {
				this.includeIntFlag = tempVo.getParam("IncludeIntFlag"); // 是否內含利息 default=N
				this.includeFeeFlag = tempVo.getParam("IncludeIntFlag"); // 是否內含費用 default=N
				this.unpaidIntFlag = tempVo.getParam("UnpaidIntFlag");// 利息是否可欠繳 default=Y
				this.payMethod = tempVo.getParam("PayMethod");// 繳納方式 1.減少每期攤還金額 2.縮短應繳期數 default=1
				// 是否內含費用
				if ("Y".equals(this.includeFeeFlag)) {
					this.extraRepay = this.extraRepay.subtract(this.totalFee);
					this.payFeeFlag = "Y";
				}
			}
			// 暫收款可抵繳全部費用則回收費用
			if (this.tavAmt.compareTo(this.totalFee) >= 0) {
				this.payFeeFlag = "Y";
			}
			// 部分還款條件=> TempVo
			tempVo.putParam("IncludeIntFlag", this.includeIntFlag); // 是否內含利息
			tempVo.putParam("UnpaidIntFlag", this.unpaidIntFlag);// 利息是否可欠繳
			tempVo.putParam("PayMethod", this.payMethod);// 繳納方式 1.減少每期攤還金額 2.縮短應繳期數
			tempVo.putParam("ExtraRepay", this.extraRepay);// 部分還款金額
		}

// STEP 5: 放款計息金額計算
		if (iRepayType >= 1 && iRepayType <= 3) {
			calcRepayLoan(iEntryDate, iPayIntDate, iCustNo, iFacmNo, iBormNo, iRepayType, repayAmt, 0, titaVo);
		}

// STEP 6: 提前償還計算清償違約金
		if (iRepayType == 2 || iRepayType == 3) {
			getCloseBreachAmt(iEntryDate, iCustNo, iFacmNo, iRepayType, titaVo);
		}

// STEP 4: 設定總金額
		this.txBal = iTxAmt; // 回收金額
		this.xxBal = this.txBal; // 可償還餘額

		// 合併檢核轉暫收金額加入暫收可抵繳
		if ("L420A".equals(titaVo.getTxcd()) && tempVo.get("MergeAmt") != null) {
			if (tempVo.getParam("MergeSeq").equals(tempVo.getParam("MergeCnt"))) {
				this.tavAmt = addMergeTempAmt(iCustNo, this.getOverRpFacmNo(),
						parse.stringToBigDecimal(tempVo.getParam("MergeTempAmt")));
			}
		}

		// 加可抵繳至可償還餘額(費用類別不抵繳)
		if (iRepayType <= 3 || iRepayType >= 9) {
			this.xxBal = this.xxBal.add(this.tavAmt);
		}

// STEP 5: 設定還款順序
		// 1.還款類別、金額相同 > 2.還款類別相同 > 3:未收費用 > 4:短繳期金 > 5:應繳本利 > 6:另收欠款
		settlePriority(iRepayType, iTxAmt); //

// STEP 6 : 計算作帳金額

		// 回收費用，費用類別入帳只會自動收類別相同者
		if ("Y".equals(this.payFeeFlag)) {
			settleAcctAmt(1); // 1.還款類別、金額相同
			settleAcctAmt(2); // 2.還款類別相同
		}

		// 全部回收時或轉暫收
		if (iRepayType <= 3 || iRepayType >= 9) {
			if ("Y".equals(this.payFeeFlag)) {
				settleAcctAmt(3); // 3:未收費用
			}
		}

		// 放款回收時
		if (iRepayType <= 3 || iRepayType == 99) {
			settleAcctAmt(4); // 4:短繳期金
		}

		// 期款回收金額時排序,依應繳日由小到大、計息順序(利率由大到小)、額度由小到大
		if (iRepayType == 1) {
			for (BaTxVo ba : this.baTxList) {
				this.info("sort B: " + ba.toString());
			}
			Collections.sort(baTxList); // 排序優先度(由小到大) payIntDate 應繳日
			for (BaTxVo ba : this.baTxList) {
				this.info("sort A: " + ba.toString());
			}
			// RepayIntDate 還款應繳日(0: -> 無實際還款
			this.repayIntDate = settleByPayintDate();
			tempVo.putParam("NextPayIntDate", this.nextPayIntDate); // 下次應繳日
		}

		// 部分償還、結案
		if (iRepayType == 2 || iRepayType == 3) {
			settleAcctAmt(5); // 5:應繳本利
			if (this.xxBal.compareTo(BigDecimal.ZERO) < 0) {
				this.repayIntDate = 0;
			} else {
				this.repayIntDate = iPayIntDate;
			}
		}

		// 結案收 6:另收欠款
		if (iRepayType == 3) {
			settleAcctAmt(6);
		}

// STEP 7 : 計算作帳金額 3.暫收抵繳
		settleTmpAcctAmt();

// STEP 8 : 計算溢(C)短(D)繳 
		settleOverAmt(iCustNo, iFacmNo, titaVo);

// STEP 9 : TempVo 共同設定值

		tempVo.putParam("PayFeeFlag", this.payFeeFlag); // 是否回收費用

		// 戶號狀況
		if (this.facStatus > 0) {
			tempVo.putParam("FacStatus", this.facStatus);
		}

		// 還款應繳日、額度應繳日
		if (iRepayType == 1) {
			this.tempVo.putParam("RepayIntDate", this.repayIntDate);
			this.tempVo.putParam("RepayIntDateByFacmNoVo", this.repayIntDateByFacmNoVo.getJsonString());
		}

// END
		if (this.baTxList != null) {
			for (BaTxVo ba : this.baTxList) {
				this.info("settleUnPaid " + ba.toString());
			}
		}

		// 本金利息(加總至撥款)
		addByBormNoBaTxList();

		// 計算作帳金額(匯款轉帳檢核報表)
		settleAcAmtBatxList(iRepayType, iTxAmt, this.shortAmt);
		this.info("TempVo=" + tempVo.toString());

		return this.baTxList;
	}

	// 取還款費用額度
	private int getFeeRepayFacmNo(int iRepayType, BigDecimal iTxAmt, int checkFacmNo) {
		/* 設定費用還款順序 */
		int feeFacmNo = 0;
		// 設定還款順序 1.還款類別、金額相同 > 2.還款類別相同 > 3:未收費用 > 4:短繳期金 > 5:應繳本利 > 6:另收欠款
		settlePriority(iRepayType, iTxAmt);
		for (int i = 1; i <= 6; i++) {
			if (i == 1 || i == 2 || i == 3 || i == 6) {
				for (BaTxVo ba : this.baTxList) {
					if (ba.getRepayPriority() == i) {
						feeFacmNo = ba.getFacmNo();
						break;
					}
				}
			}
		}
		int repayFacmNo = 0;
		// 取費用還款額度或溢收額度
		if (feeFacmNo > 0) {
			repayFacmNo = getRepayFacmNo(0, feeFacmNo);
		} else {
			repayFacmNo = getRepayFacmNo(0, checkFacmNo);
		}

		this.info("getFeeRepayFacmNo end iRepayType=" + iRepayType + ", FeeFacmNo=" + feeFacmNo + ", RepayFacmNo="
				+ repayFacmNo);

		// 取費用還款額度
		return repayFacmNo;
	}

	// 取還款額度
	private int getRepayFacmNo(int iFacmNo, int checkFacmNo) {
		// 全部額度 ：03-結案、98-全費用類別(已到期)、99-全部(含未到期)
		// 1.值為指定額度：輸入額度>0時為輸入額度
		// 2.值為暫收指定額度：輸入額度=0時且回收額度為暫收指定額度時，只回收該暫收指定額度。
		// 3.值為0：輸入額度=0時且回收額度為非暫收指定額度時，只回收非暫收指定額度、但結案為全部
		int repayFacmNo = iFacmNo;
		if (repayFacmNo == 0) {
			Boolean isLoanFacTmp = false;
			for (LoanFacTmp t : lLoanFacTmp) {
				if (checkFacmNo == t.getFacmNo()) {
					isLoanFacTmp = true;
				}
			}
			if (isLoanFacTmp) {
				repayFacmNo = checkFacmNo;
			}
		}
		this.info("getRepayFacmNo end iFacmNo=" + iFacmNo + ", checkFacmNo=" + checkFacmNo + ", RepayFacmNo="
				+ repayFacmNo);
		return repayFacmNo;
	}

	// 取得期款還款額度(期金是否相同)
	private int getDueAmtFacmNo(BigDecimal iTxAmt) {
		int checkFacmNo = 0;
		int facmNo = 0;
		// 1.值為暫收指定額度：輸入額度=0時且回收金額=期金時暫收指定額度期金時，只回收該暫收指定額度。
		// 2.值為0：輸入額度=0時且回收額度為非暫收指定額度時，只回收非暫收指定額度
		HashMap<Integer, BigDecimal> dueAmt = new HashMap<>();
		for (LoanBorMain ln : this.lLoanBorMain) {
			if (ln.getStatus() == 0) {
				if (dueAmt.containsKey(ln.getFacmNo())) {
					dueAmt.put(ln.getFacmNo(), dueAmt.get(ln.getFacmNo()).add(ln.getDueAmt()));
				} else {
					dueAmt.put(ln.getFacmNo(), ln.getDueAmt());
				}

			}
		}
		for (Entry<Integer, BigDecimal> entry : dueAmt.entrySet()) {
			if (entry.getValue().compareTo(iTxAmt) == 0) {
				checkFacmNo = entry.getKey();
			}
		}
		if (checkFacmNo > 0) {
			Boolean isLoanFacTmp = false;
			for (LoanFacTmp t : lLoanFacTmp) {
				if (checkFacmNo == t.getFacmNo()) {
					isLoanFacTmp = true;
				}
			}
			if (isLoanFacTmp) {
				facmNo = checkFacmNo;
			}
		}
		this.info("getDueAmtFacmNo end iTxAm=" + iTxAmt + ", RepayFacmNo=" + facmNo);
		return facmNo;
	}

	// 重新依還款類別及還款額度取得放款主檔清單
	private void reLoadRepayLoan(int iRepayType, int iFacmNo, TitaVo titaVo) throws LogicException {
		// 期款及部分償還時
		// 1.還款額度 >0：該額度的放款主檔清單
		// 2.還款額度為0：期款及部分償還時為非暫收指定額度，其他為全部
		this.lLoanBorMain = new ArrayList<LoanBorMain>();
		if (iRepayType == 1 || iRepayType == 2 || iFacmNo > 0) {
			if (iFacmNo > 0) {
				for (LoanBorMain ln : slLoanBorMain.getContent()) {
					if (ln.getFacmNo() == iFacmNo) {
						this.lLoanBorMain.add(ln);
					}
				}
			} else {
				for (LoanBorMain ln : slLoanBorMain.getContent()) {
					Boolean isLoanFacTmp = false;
					for (LoanFacTmp t : lLoanFacTmp) {
						if (ln.getFacmNo() == t.getFacmNo()) {
							isLoanFacTmp = true;
							break;
						}
					}
					if (!isLoanFacTmp) {
						this.lLoanBorMain.add(ln);
					}
				}
			}
		} else {
			for (LoanBorMain ln : slLoanBorMain.getContent()) {
				this.lLoanBorMain.add(ln);
			}
		}
		// 回收排序
		sortRepayLoan(iRepayType, titaVo);

		this.loanBal = BigDecimal.ZERO;
		this.nextPayIntDate = 0;
		this.prevPayIntDate = 0;
		for (LoanBorMain ln : this.lLoanBorMain) {
			if (ln.getStatus() == 0) {
				this.loanBal = this.loanBal.add(ln.getLoanBal()); // 還款前本金餘額
				if (this.nextPayIntDate == 0 || ln.getNextPayIntDate() < this.nextPayIntDate) {
					this.nextPayIntDate = ln.getNextPayIntDate(); // 下次應繳日
					this.overRpFacmNo = ln.getFacmNo();
				}
				if (this.prevPayIntDate == 0 || ln.getPrevPayIntDate() < this.prevPayIntDate) {
					this.prevPayIntDate = ln.getPrevPayIntDate(); // 上次繳息日
				}
			}
		}
		this.info("getRepayFacmNoLoan end overRpFacmNo=" + this.overRpFacmNo + ", size=" + this.lLoanBorMain.size());
	}

	/**
	 * 提存試算
	 * 
	 * @param iEntryDate  入帳日
	 * @param iPayintDate 應繳日
	 * @param iCustNo     戶號
	 * @param iFacmNo     額度
	 * @param iBormNo     撥款
	 * @param titaVo      TitaVo
	 * @return ArrayList of BaTxVo
	 * @throws LogicException ...
	 */
	public ArrayList<BaTxVo> acLoanInt(int iEntryDate, int iPayintDate, int iCustNo, int iFacmNo, int iBormNo,
			TitaVo titaVo) throws LogicException {
		this.info("BaTxCom acLoanInt ..." + iEntryDate + "/" + iPayintDate + " " + iCustNo + "-" + iFacmNo + "-"
				+ iBormNo);
		init();
		// iEntryDate 入帳日 ==> 月底日曆日
		// iPayintDate 利息計算止日 ==> 次月月初日

		this.baTxList = new ArrayList<BaTxVo>();

		// isEmptyLoanBaTxVo 是否放未計息餘額
		this.isEmptyLoanBaTxVo = true;

		// isAcLoanInt 是否利息提存- > 是
		this.isAcLoanInt = true;

		// 短繳利息
		loadUnPaid(iEntryDate, iCustNo, iFacmNo, iBormNo, 1, titaVo);

		// 還款主檔清單
		loadRepayLoan(iEntryDate, iCustNo, iFacmNo, iBormNo, 90, titaVo);

		// 還款金額計算
		calcRepayLoan(iEntryDate, iPayintDate, iCustNo, iFacmNo, iBormNo, 90, BigDecimal.ZERO, 0, titaVo); // Terms = 0

		// END
		return this.baTxList;

	}

	/**
	 * 應繳期款by期數
	 * 
	 * @param iEntryDate 入帳日
	 * @param iCustNo    戶號
	 * @param iFacmNo    額度
	 * @param iBormNo    撥款
	 * @param iTerms     期數
	 * @param iEndFlag   0:正常 1.至下次利率調整日
	 * @param titaVo     TitaVo
	 * @return ArrayList of BaTxVo
	 * @throws LogicException ...
	 */
	public ArrayList<BaTxVo> termsPay(int iEntryDate, int iCustNo, int iFacmNo, int iBormNo, int iTerms, int iEndFlag,
			TitaVo titaVo) throws LogicException {
		this.info("BaTxCom termsPay ..." + iEntryDate + "/ " + iCustNo + "-" + iFacmNo + "-" + iBormNo + "/ " + iTerms);
		init();
		// 0:正常 1.至下次利率調整日
		this.endFlag = iEndFlag;

		// 費用是否全部回收
		this.isPayAllFee = true; // 全部

		// isEmptyLoanBaTxVo 是否放未計息餘額
		this.isEmptyLoanBaTxVo = false;

		// STEP 3: Load UnPaid 1.應收費用+未收費用+短繳期金 3.暫收抵繳 6.另收欠款
		loadUnPaid(iEntryDate, iCustNo, iFacmNo, iBormNo, 1, titaVo);

		// 還款主檔清單
		loadRepayLoan(iEntryDate, iCustNo, iFacmNo, iBormNo, 01, titaVo); // 還款主檔清單

		// 還款金額計算
		calcRepayLoan(iEntryDate, iEntryDate, iCustNo, iFacmNo, iBormNo, 01, BigDecimal.ZERO, iTerms, titaVo);

		// END
		if (this.baTxList != null) {
			for (BaTxVo ba : this.baTxList) {
				this.info("termsPay " + ba.toString());
			}
		}
		return this.baTxList;
	}

	/**
	 * 計算至利率調整後次月份應繳期金
	 * 
	 * @param iEntryDate 入帳日
	 * @param iCustNo    戶號
	 * @param iFacmNo    額度
	 * @param iBormNo    撥款
	 * @param titaVo     TitaVo
	 * @return ArrayList of BaTxVo
	 * @throws LogicException ...
	 */
	public ArrayList<BaTxVo> getDueAmt(int iEntryDate, int iCustNo, int iFacmNo, int iBormNo, TitaVo titaVo)
			throws LogicException {
		this.info("BaTxCom getDueAmt ..." + iEntryDate + "/ " + iCustNo + "-" + iFacmNo + "-" + iBormNo);
		init();

		// isEmptyLoanBaTxVo 是否放未計息餘額
		this.isEmptyLoanBaTxVo = false;

		// Load UnPaid 1.應收費用+未收費用+短繳期金 3.暫收抵繳 6.另收欠款
		loadUnPaid(iEntryDate, iCustNo, iFacmNo, iBormNo, 1, titaVo);

		// 還款主檔清單
		loadRepayLoan(iEntryDate, iCustNo, iFacmNo, iBormNo, 01, titaVo); // 還款主檔清單

		// 還款金額計算
		calcRepayLoan(iEntryDate, iEntryDate, iCustNo, iFacmNo, iBormNo, 80, BigDecimal.ZERO, 0, titaVo); // Terms = 0

		if (this.baTxList != null) {
			for (BaTxVo ba : this.baTxList) {
				this.info("getDueAmt " + ba.toString());
			}
		}

		return this.baTxList;
	}

	/**
	 * 現金流量預估
	 * 
	 * @param iEntryDate 入帳日
	 * @param iCustNo    戶號
	 * @param iFacmNo    額度
	 * @param iBormNo    撥款
	 * @param titaVo     TitaVo
	 * @return ArrayList of BaTxVo
	 * @throws LogicException ...
	 */
	public ArrayList<BaTxVo> cashFlow(int iEntryDate, int iCustNo, int iFacmNo, int iBormNo, TitaVo titaVo)
			throws LogicException {
		this.info("BaTxCom cashFlow ...");
		init();

		// isEmptyLoanBaTxVo 是否放未計息餘額
		this.isEmptyLoanBaTxVo = true;

		// 還款主檔清單
		loadRepayLoan(iEntryDate, iCustNo, iFacmNo, iBormNo, 01, titaVo); // 還款主檔清單

		calcRepayLoan(iEntryDate, iEntryDate, iCustNo, iFacmNo, iBormNo, 01, BigDecimal.ZERO, 0, titaVo);

		// END
		return this.baTxList;

	}

	/**
	 * 本金利息(按應繳日加總至額度)
	 * 
	 * @param iBatxList 還款明細
	 * @param titaVo    TitaVo
	 * @return 還款明細
	 * @throws LogicException ...
	 */
	public ArrayList<BaTxVo> addByPayintDate(ArrayList<BaTxVo> iBatxList, TitaVo titaVo) throws LogicException {
		this.info("addByPayintDate .... ");
		this.baTxList = new ArrayList<BaTxVo>();
		// 按額度、資料類型、還款類別、應繳日，小至大排序
		Collections.sort(iBatxList, new Comparator<BaTxVo>() {
			@Override
			public int compare(BaTxVo c1, BaTxVo c2) {
				if (c1.getFacmNo() != c2.getFacmNo()) {
					return c1.getFacmNo() - c2.getFacmNo();
				}
				if (c1.getDataKind() != c2.getDataKind()) {
					return c1.getDataKind() - c2.getDataKind();
				}
				if (c1.getRepayType() != c2.getRepayType()) {
					return c1.getRepayType() - c2.getRepayType();
				}
				if (c1.getPayIntDate() != c2.getPayIntDate()) {
					return c1.getPayIntDate() - c2.getPayIntDate();
				}
				return 0;
			}
		});

		int dataKind = 0;
		int repayTyp = 0;
		int facmNo = 0;
		int payIntDate = 0;

		for (BaTxVo ba : iBatxList) {
			if (ba.getFacmNo() != facmNo || ba.getDataKind() != dataKind || ba.getRepayType() != repayTyp
					|| ba.getPayIntDate() != payIntDate) {
				baTxVo = new BaTxVo();
				baTxVo.setDataKind(ba.getDataKind());
				baTxVo.setRepayType(ba.getRepayType());
				baTxVo.setReceivableFlag(ba.getReceivableFlag());
				baTxVo.setCustNo(ba.getCustNo()); // 借款人戶號
				baTxVo.setFacmNo(ba.getFacmNo()); // 額度編號
				baTxVo.setBormNo(0); // 撥款序號
				baTxVo.setRvNo(ba.getRvNo()); // 銷帳編號
				baTxVo.setPaidTerms(ba.getPaidTerms()); // 繳息期數
				baTxVo.setPayIntDate(ba.getPayIntDate()); // 應繳息日
				baTxVo.setAcctCode(ba.getAcctCode()); // 業務科目
				baTxVo.setDbCr(ba.getDbCr()); // 借貸別
				baTxVo.setPrincipal(ba.getPrincipal()); // 本金
				baTxVo.setInterest(ba.getInterest()); // // 利息
				baTxVo.setDelayInt(ba.getDelayInt()); // 延滯息
				baTxVo.setBreachAmt(ba.getBreachAmt()); // 違約金
				baTxVo.setUnpaidPrin(ba.getUnpaidPrin()); // 短繳本金
				baTxVo.setUnpaidInt(ba.getUnpaidInt()); // 短繳利息
				baTxVo.setLoanBalPaid(ba.getLoanBalPaid()); // 還款後餘額
				baTxVo.setUnPaidAmt(ba.getUnPaidAmt());
				baTxVo.setAcctAmt(ba.getAcctAmt()); // 出帳金額
				baTxVo.setLoanBal(ba.getLoanBal()); // 放款餘額(還款前、只放第一期)
				baTxVo.setExtraAmt(ba.getExtraAmt()); // 提前償還金額
				baTxVo.setIntStartDate(ba.getIntStartDate()); // 計息起日
				baTxVo.setIntEndDate(ba.getIntEndDate()); // 計息止日
				baTxVo.setAmount(ba.getAmount()); // 計息本金
				baTxVo.setIntRate(ba.getIntRate()); // 計息利率
				baTxVo.setRateIncr(ba.getRateIncr()); // 加碼利率
				baTxVo.setIndividualIncr(ba.getIndividualIncr()); // 個別加碼利率 }
				baTxVo.setCloseFg(ba.getCloseFg());
				dataKind = ba.getDataKind();
				repayTyp = ba.getRepayType();
				facmNo = ba.getFacmNo();
				payIntDate = ba.getPayIntDate();
				this.baTxList.add(baTxVo);
			} else {
				baTxVo.setPrincipal(baTxVo.getPrincipal().add(ba.getPrincipal())); // 本金
				baTxVo.setInterest(baTxVo.getInterest().add(ba.getInterest())); // // 利息
				baTxVo.setDelayInt(baTxVo.getDelayInt().add(ba.getDelayInt())); // 延滯息
				baTxVo.setBreachAmt(baTxVo.getBreachAmt().add(ba.getBreachAmt())); // 違約金
				baTxVo.setUnpaidPrin(baTxVo.getUnpaidPrin().add(ba.getUnpaidPrin())); // 短繳本金
				baTxVo.setUnpaidInt(baTxVo.getUnpaidInt().add(ba.getUnpaidInt())); // 短繳利息
				baTxVo.setLoanBalPaid(baTxVo.getLoanBalPaid().add(ba.getLoanBalPaid())); // 還款後餘額
				baTxVo.setUnPaidAmt(baTxVo.getPrincipal().add(ba.getUnPaidAmt()));
				baTxVo.setAcctAmt(baTxVo.getAcctAmt().add(ba.getAcctAmt())); // 出帳金額
				baTxVo.setLoanBal(baTxVo.getLoanBal().add(ba.getLoanBal())); // 放款餘額(還款前、只放第一期)
				baTxVo.setExtraAmt(baTxVo.getExtraAmt().add(ba.getExtraAmt())); // 提前償還金額
				if (ba.getIntStartDate() < baTxVo.getIntStartDate()) {
					baTxVo.setIntStartDate(ba.getIntStartDate()); // 計息起日
				}
				if (ba.getIntEndDate() > baTxVo.getIntEndDate()) {
					baTxVo.setIntEndDate(ba.getIntEndDate()); // 計息止日
				}
				baTxVo.setAmount(baTxVo.getAmount().add(ba.getAmount())); // 計息本金
				if (ba.getCloseFg() < baTxVo.getCloseFg()) {
					baTxVo.setCloseFg(ba.getCloseFg());
				}
			}
		}
		// END
		return this.baTxList;
	}

	/* 讀取還款主檔清單 */
	private void loadRepayLoan(int iEntryDate, int iCustNo, int iFacmNo, int iBormNo, int iRepayType, TitaVo titaVo)
			throws LogicException {
		this.info(" loadRepayLoan ...");
		this.info("   EntryDate = " + iEntryDate);
		this.info("   CustNo    = " + iCustNo);
		this.info("   FacmNo    = " + iFacmNo);
		this.info("   BormNo    = " + iBormNo);
		this.info("   RepayType = " + iRepayType);
		int wkFacmNoStart = 1;
		int wkFacmNoEnd = 999;
		int wkBormNoStart = 1;
		int wkBormNoEnd = 900;
		if (iFacmNo > 0) {
			wkFacmNoStart = iFacmNo;
			wkFacmNoEnd = iFacmNo;
		}

		if (iBormNo > 0) {
			wkBormNoStart = iBormNo;
			wkBormNoEnd = iBormNo;
		}
		slLoanBorMain = loanBorMainService.bormCustNoEq(iCustNo, wkFacmNoStart, wkFacmNoEnd, wkBormNoStart, wkBormNoEnd,
				0, Integer.MAX_VALUE, titaVo);
		if (slLoanBorMain == null) {
			throw new LogicException(titaVo, "E0001", "該戶號無撥款資料"); // 查詢資料不存在
		}
		boolean isStautsNormal = false;
		for (LoanBorMain ln : slLoanBorMain.getContent()) {
			this.lLoanBorMain.add(ln);
			if (ln.getStatus() == 0) {
				isStautsNormal = true;
				this.loanBal = this.loanBal.add(ln.getLoanBal()); // 還款前本金餘額
				if (this.nextPayIntDate == 0 || ln.getNextPayIntDate() < this.nextPayIntDate) {
					this.nextPayIntDate = ln.getNextPayIntDate(); // 下次應繳日
					this.overRpFacmNo = ln.getFacmNo(); // 溢繳額度;
				}
				if (this.prevPayIntDate == 0 || ln.getPrevPayIntDate() < this.prevPayIntDate) {
					this.prevPayIntDate = ln.getPrevPayIntDate(); // 上次繳息日
				}
			}
		}
		// 戶況
		if (!isStautsNormal) {
			this.facStatus = facStatusCom.settingStatus(slLoanBorMain.getContent(), iEntryDate);
			this.overRpFacmNo = facStatusCom.getFacmNo(); // 溢繳額度;
			this.info("FacStatus =" + this.facStatus + ", overRpFacmNo=" + this.overRpFacmNo);
		}
		this.info("loadRepayLoan end FacStatus =" + this.facStatus + ", overRpFacmNo=" + this.overRpFacmNo + ", size="
				+ this.lLoanBorMain.size());
	}

	/* 還款主檔清單排序 */
	private void sortRepayLoan(int iRepayType, TitaVo titaVo) throws LogicException {
		this.info("sortRepayLoan ... iRepayType=" + iRepayType + ", lLoanBorMain size=" + lLoanBorMain.size());
		if (this.lLoanBorMain.size() == 0) {
			return;
		}
		Collections.sort(lLoanBorMain, new Comparator<LoanBorMain>() {
			@Override
			public int compare(LoanBorMain c1, LoanBorMain c2) {
				// status
				if (c1.getStatus() != c2.getStatus()) {
					return c1.getStatus() - c2.getStatus();
				}
				// 回收金額 > 0時排序,依應繳日順序由小到大、利率順序由大到小、額度由大到小、期金由大到小
				if (iRepayType == 1) {
					if (c1.getNextPayIntDate() != c2.getNextPayIntDate()) {
						return c1.getNextPayIntDate() - c2.getNextPayIntDate();
					}
					if (c1.getStoreRate().compareTo(c2.getStoreRate()) != 0) {
						return (c1.getStoreRate().compareTo(c2.getStoreRate()) > 0 ? -1 : 1);
					}
					if (c1.getFacmNo() != c2.getFacmNo()) {
						return c2.getFacmNo() - c1.getFacmNo();
					}
					if (c1.getDueAmt().compareTo(c2.getDueAmt()) != 0) {
						return c2.getDueAmt().compareTo(c1.getDueAmt());
					}
				}
				// 部分償還金額 > 0時排序
//				利率高至低>用途別>由額度編號大至小、撥款由大到小
//				用途別為9->1->3->4->5->6->2
//				欄位代碼       欄位說明     
//				1            週轉金    
//				2            購置不動產
//				3            營業用資產
//				4            固定資產  
//				5            企業投資  
//				6            購置動產
//				9            其他					
				if (iRepayType == 2) {
					if (c1.getStoreRate().compareTo(c2.getStoreRate()) != 0) {
						return (c1.getStoreRate().compareTo(c2.getStoreRate()) > 0 ? -1 : 1);
					}
					// 若用途別不同
					if (!c1.getUsageCode().equals(c2.getUsageCode())) {
						int c1UsageCode = Integer.parseInt(c1.getUsageCode());
						int c2UsageCode = Integer.parseInt(c2.getUsageCode());

						// C1優先的特殊情況
						if (c1UsageCode == 9 || c2UsageCode == 2) {
							return -1;
						}
						// C2優先的特殊情況
						if (c1UsageCode == 2 || c2UsageCode == 9) {
							return 1;
						}
						// 一般情況
						return c1UsageCode - c2UsageCode;
					}
					if (c1.getFacmNo() != c2.getFacmNo()) {
						return c2.getFacmNo() - c1.getFacmNo();
					}
					if (c1.getBormNo() != c2.getBormNo()) {
						return c2.getBormNo() - c1.getBormNo();
					}
				}
				return 0;
			}
		});

		this.overRpFacmNo = this.lLoanBorMain.get(0).getFacmNo(); // 溢繳額度;
		this.info("sortRepayLoan end overRpFacmNo = " + this.lLoanBorMain.get(0).getFacmNo());

	}

	/* 計算放款還款金額 */
	private void calcRepayLoan(int iEntryDate, int iPayIntDate, int iCustNo, int iFacmNo, int iBormNo, int iRepayType,
			BigDecimal iTxAmt, int iTerms, TitaVo titaVo) throws LogicException {
		this.info("BaTxCom calcRepayLoan ...");
		this.info("   EntryDate = " + iEntryDate);
		this.info("   IntPayDate = " + iPayIntDate);
		this.info("   CustNo    = " + iCustNo);
		this.info("   FacmNo    = " + iFacmNo);
		this.info("   BormNo    = " + iBormNo);
		this.info("   RepayType = " + iRepayType);
		this.info("   TxAmt     = " + iTxAmt);
		this.info("   Terms     = " + iTerms);
		this.info("   extraRepay = " + this.extraRepay);
		if (this.lLoanBorMain.size() == 0) {
			return;
		}

		// RepayInt begin
		loanSetRepayIntCom.setTxBuffer(this.getTxBuffer());

		BigDecimal wkExtraRepayRemaind = this.extraRepay;
		ArrayList<CalcRepayIntVo> lCalcRepayIntVo;
		int wkTerms = 0;

		for (LoanBorMain ln : this.lLoanBorMain) {
			if (ln.getStatus() != 0) {
				continue;
			}
			if (iRepayType == 1) {
				this.info("order1 = " + ln.getLoanBorMainId() + " NextPayIntDate=" + ln.getNextPayIntDate()
						+ ", StoreRate=" + ln.getStoreRate() + ", FacmNo" + ln.getFacmNo() + ", DueAmt="
						+ ln.getDueAmt());
			} else if (iRepayType == 2) {
				this.info("order2 = " + ln.getLoanBorMainId() + " StoreRate=" + ln.getStoreRate() + ", UsageCode="
						+ ln.getUsageCode() + ", FacmNo" + ln.getFacmNo() + ", BormNo=" + ln.getBormNo());
			} else {
				this.info("order3 = " + ln.getLoanBorMainId());
			}

			this.calcLoanBal = ln.getLoanBal(); // 還款前本金餘額
			switch (iRepayType) {
			case 1: // 01-期款
				if (iTerms == 0) {
					int wkPrevTermNo = 0;
					int wkRepayTermNo = 0;
					// 計算至上次繳息日之期數
					if (ln.getPrevPayIntDate() > ln.getDrawdownDate()) {
						wkPrevTermNo = loanCom.getTermNo(2, ln.getFreqBase(), ln.getPayIntFreq(), ln.getSpecificDate(),
								ln.getSpecificDd(), ln.getPrevPayIntDate());
					}
					// 可回收期數 = 計算至應繳日的應繳期數 + 批次預收期數
					wkRepayTermNo = loanCom.getTermNo(iPayIntDate >= ln.getMaturityDate() ? 1 : 2, ln.getFreqBase(),
							ln.getPayIntFreq(), ln.getSpecificDate(), ln.getSpecificDd(), iPayIntDate)
							+ this.preRepayTerms;

					wkTerms = wkRepayTermNo - wkPrevTermNo;
				} else {
					wkTerms = iTerms;
				}

				// 計息參數
				loanCalcRepayIntCom = loanSetRepayIntCom.setRepayInt(ln, 1, 0, 0, iEntryDate, titaVo);

				// 無可計息， isEmptyLoanBaTxVo 是否放未計息餘額
				if (wkTerms <= 0 && isEmptyLoanBaTxVo) {
					emptyLoanBaTxVo(iEntryDate, iRepayType, iCustNo, ln);
					break;
				}

				// 輸出每期
				for (int i = 1; i <= wkTerms; i++) {
					lCalcRepayIntVo = loanCalcRepayIntCom.getRepayInt(titaVo);
					// 其款試算至下次利率調整日
					if (this.endFlag == 1 && ln.getNextAdjRateDate() > 0) {
						if (loanCalcRepayIntCom.getPrevPaidIntDate() > ln.getNextAdjRateDate()) {
							break;
						}
					}
					repayLoanBaTxVo(iEntryDate, iPayIntDate, iRepayType, iCustNo, ln, lCalcRepayIntVo, i);
					loanCalcRepayIntCom.setPrincipal(loanCalcRepayIntCom.getLoanBal()); // 計息本金
					loanCalcRepayIntCom.setStoreRate(loanCalcRepayIntCom.getStoreRate()); // 上次收息利率
					loanCalcRepayIntCom.setTerms(1); // 本次計息期數
					loanCalcRepayIntCom.setIntStartDate(loanCalcRepayIntCom.getPrevPaidIntDate()); // 計算起日
					loanCalcRepayIntCom.setIntEndDate(0); // 計息止日
					loanCalcRepayIntCom.setPaidTerms(loanCalcRepayIntCom.getPaidTerms()); // 已繳息期數
					loanCalcRepayIntCom.setPrevRepaidDate(loanCalcRepayIntCom.getPrevRepaidDate()); // 上次還本日
					loanCalcRepayIntCom.setPrevPaidIntDate(loanCalcRepayIntCom.getPrevPaidIntDate()); // 上次繳息日
					loanCalcRepayIntCom.setNextPayIntDate(loanCalcRepayIntCom.getNextPayIntDate()); // 下次繳息日,應繳息日,預定收息日
					loanCalcRepayIntCom.setNextRepayDate(loanCalcRepayIntCom.getNextRepayDate()); // 下次還本日,應還本日,預定還本日
					loanCalcRepayIntCom.setDueAmt(loanCalcRepayIntCom.getDueAmt()); // 每期攤還金額
					if (loanCalcRepayIntCom.getLoanBal().equals(BigDecimal.ZERO)) {
						break;
					}
				}
				break;
			case 2: // 部分償還金額
				if (ln.getNextPayIntDate() <= iEntryDate) {
					throw new LogicException(titaVo, "E3072", "額度=" + parse.IntegerToString(ln.getFacmNo(), 3)
							+ ", 部分償還前應先償還期款, 應繳息日 = " + ln.getNextPayIntDate()); // 該筆放款尚有未回收期款
				}
				if (wkExtraRepayRemaind.compareTo(BigDecimal.ZERO) <= 0) {
					break;
				}
				loanCalcRepayIntCom = loanSetRepayIntCom.setRepayInt(ln, 0, iEntryDate, 1, iEntryDate, titaVo);
				// 部分償還金額超過放款餘額
				if (wkExtraRepayRemaind.compareTo(ln.getLoanBal()) >= 0 || iEntryDate >= ln.getMaturityDate()) {
					loanCalcRepayIntCom.setCaseCloseFlag("Y"); // 結案記號 Y:是 N:否
				} else {
					loanCalcRepayIntCom.setExtraRepayFlag(this.includeIntFlag); // 是否內含利息 Y:是 N:否
					loanCalcRepayIntCom.setExtraRepay(wkExtraRepayRemaind);
				}
				lCalcRepayIntVo = loanCalcRepayIntCom.getRepayInt(titaVo);
				// 部分還本金額
				wkExtraRepayRemaind = wkExtraRepayRemaind.subtract(loanCalcRepayIntCom.getPrincipal())
						.subtract(loanCalcRepayIntCom.getInterest()).subtract(loanCalcRepayIntCom.getDelayInt())
						.subtract(loanCalcRepayIntCom.getBreachAmt());
				repayLoanBaTxVo(iEntryDate, iPayIntDate, iRepayType, iCustNo, ln, lCalcRepayIntVo, 0);

				break;
			case 3: // 結案
				loanCalcRepayIntCom = loanSetRepayIntCom.setRepayInt(ln, 0, iEntryDate, 1, iEntryDate, titaVo);
				loanCalcRepayIntCom.setCaseCloseFlag("Y"); // 結案記號 Y:是 N:否
				lCalcRepayIntVo = loanCalcRepayIntCom.getRepayInt(titaVo);
				repayLoanBaTxVo(iEntryDate, iPayIntDate, iRepayType, iCustNo, ln, lCalcRepayIntVo, 0);
				break;

			case 80: // 計算利率調整後次月份應繳期金
				int wkPrevTermNo = 0;
				int wkRepayTermNo = 0;
				// 計算至上次繳息日之期數
				if (ln.getPrevPayIntDate() > ln.getDrawdownDate()) {
					wkPrevTermNo = loanCom.getTermNo(2, ln.getFreqBase(), ln.getPayIntFreq(), ln.getSpecificDate(),
							ln.getSpecificDd(), ln.getPrevPayIntDate());
				}
				// 可回收期數 = 計算至入帳日/應繳日的當期期數
				wkRepayTermNo = loanCom.getTermNo(1, ln.getFreqBase(), ln.getPayIntFreq(), ln.getSpecificDate(),
						ln.getSpecificDd(), iPayIntDate);
				wkTerms = wkRepayTermNo - wkPrevTermNo;
				loanCalcRepayIntCom = loanSetRepayIntCom.setRepayInt(ln, wkTerms, 0, 0, iEntryDate, titaVo);
				if (wkTerms > 0) {
					// 計息參數
					lCalcRepayIntVo = loanCalcRepayIntCom.getRepayInt(titaVo);
					loanCalcRepayIntCom.setPrincipal(loanCalcRepayIntCom.getLoanBal()); // 計息本金
					loanCalcRepayIntCom.setStoreRate(loanCalcRepayIntCom.getStoreRate()); // 上次收息利率
					loanCalcRepayIntCom.setIntStartDate(loanCalcRepayIntCom.getPrevPaidIntDate()); // 計算起日
					loanCalcRepayIntCom.setPaidTerms(loanCalcRepayIntCom.getPaidTerms()); // 已繳息期數
					loanCalcRepayIntCom.setPrevRepaidDate(loanCalcRepayIntCom.getPrevRepaidDate()); // 上次還本日
					loanCalcRepayIntCom.setPrevPaidIntDate(loanCalcRepayIntCom.getPrevPaidIntDate()); // 上次繳息日
					loanCalcRepayIntCom.setNextPayIntDate(loanCalcRepayIntCom.getNextPayIntDate()); // 下次繳息日,應繳息日,預定收息日
					loanCalcRepayIntCom.setNextRepayDate(loanCalcRepayIntCom.getNextRepayDate()); // 下次還本日,應還本日,預定還本日
					loanCalcRepayIntCom.setDueAmt(loanCalcRepayIntCom.getDueAmt()); // 每期攤還金額
					if (loanCalcRepayIntCom.getLoanBal().equals(BigDecimal.ZERO)) {
						break;
					}
				}
				// 再收一期
				loanCalcRepayIntCom.setTerms(1);
				lCalcRepayIntVo = loanCalcRepayIntCom.getRepayInt(titaVo);
				repayLoanBaTxVo(iEntryDate, iPayIntDate, iRepayType, iCustNo, ln, lCalcRepayIntVo, 1);
				break;

			case 90: // 提存
				// iEntryDate 入帳日 ==> 月底日曆日
				// iPayIntDate 利息計算止日 ==> 次月月初日
				// 上次繳息日超過
				if (ln.getPrevPayIntDate() >= iPayIntDate || ln.getDrawdownDate() >= iPayIntDate) {
					loanCalcRepayIntCom = loanSetRepayIntCom.setRepayInt(ln, 0, iPayIntDate, 0, iEntryDate, titaVo);
					emptyLoanBaTxVo(iEntryDate, iRepayType, iCustNo, ln);
					continue;
				}
				// 計算到利息計算止日之繳息期數 1:指定日期之當期數
				wkTerms = loanCom.getTermNo(1, ln.getFreqBase(), ln.getPayIntFreq(), ln.getSpecificDate(),
						ln.getSpecificDd(), iPayIntDate);
				// 減去計算至上次繳息日之期數
				if (ln.getPrevPayIntDate() > ln.getDrawdownDate()) {
					wkTerms = wkTerms - loanCom.getTermNo(1, ln.getFreqBase(), ln.getPayIntFreq(), ln.getSpecificDate(),
							ln.getSpecificDd(), ln.getPrevPayIntDate());
				}
				// 每次計算一期，最後一期計算到利息計算止日
				if (iPayIntDate <= ln.getNextPayIntDate()) {
					loanCalcRepayIntCom = loanSetRepayIntCom.setRepayInt(ln, 0, iPayIntDate, 2, iEntryDate, titaVo);
				} else {
					loanCalcRepayIntCom = loanSetRepayIntCom.setRepayInt(ln, 1, 0, 2, iEntryDate, titaVo);
				}
				// 每月月底提息特殊參數設定
				caculateSpecialAdjust(iPayIntDate, ln.getPrevPayIntDate(), ln.getNextPayIntDate(), ln,
						loanCalcRepayIntCom, titaVo);
				for (int i = 1; i <= wkTerms; i++) {
					if (i == wkTerms) {
						loanCalcRepayIntCom.setTerms(0); // 本次計息期數
						loanCalcRepayIntCom.setIntEndDate(iPayIntDate); // 計息止日
					}
					// 計算利息
					lCalcRepayIntVo = loanCalcRepayIntCom.getRepayInt(titaVo);
					repayLoanBaTxVo(iEntryDate, iPayIntDate, iRepayType, iCustNo, ln, lCalcRepayIntVo, i);
					loanCalcRepayIntCom.setPrincipal(loanCalcRepayIntCom.getLoanBal()); // 計息本金
					loanCalcRepayIntCom.setStoreRate(loanCalcRepayIntCom.getStoreRate()); // 上次收息利率
					loanCalcRepayIntCom.setIntStartDate(loanCalcRepayIntCom.getPrevPaidIntDate()); // 計算起日
					loanCalcRepayIntCom.setPaidTerms(loanCalcRepayIntCom.getPaidTerms()); // 已繳息期數
					loanCalcRepayIntCom.setPrevRepaidDate(loanCalcRepayIntCom.getPrevRepaidDate()); // 上次還本日
					loanCalcRepayIntCom.setPrevPaidIntDate(loanCalcRepayIntCom.getPrevPaidIntDate()); // 上次繳息日
					loanCalcRepayIntCom.setNextPayIntDate(loanCalcRepayIntCom.getNextPayIntDate()); // 下次繳息日,應繳息日,預定收息日
					loanCalcRepayIntCom.setNextRepayDate(loanCalcRepayIntCom.getNextRepayDate()); // 下次還本日,應還本日,預定還本日
					loanCalcRepayIntCom.setDueAmt(loanCalcRepayIntCom.getDueAmt()); // 每期攤還金額
					if (loanCalcRepayIntCom.getLoanBal().equals(BigDecimal.ZERO)
							|| loanCalcRepayIntCom.getPrevPaidIntDate() >= iPayIntDate) {
						break;
					}
					// 每月月底提息特殊參數設定
					caculateSpecialAdjust(iPayIntDate, loanCalcRepayIntCom.getPrevRepaidDate(),
							loanCalcRepayIntCom.getNextPayIntDate(), ln, loanCalcRepayIntCom, titaVo);
				}
				this.info("Caculate log Set ... 戶號= " + ln.getCustNo() + "-" + ln.getFacmNo() + "-" + ln.getBormNo()
						+ ", principal=" + this.principal + ", interest=" + this.interest + ", delayInt="
						+ this.delayInt + " ,breachAmt=" + this.breachAmt);
				break;
			}
			if (wkTerms > this.terms) {
				this.terms = wkTerms; // 繳期數
			}

		}
		// 逾期期數、逾期天數
		if (nextPayIntDate > 0 && nextPayIntDate < iPayIntDate) {
			dDateUtil.init();
			dDateUtil.setDate_1(nextPayIntDate);
			dDateUtil.setDate_2(iPayIntDate);
			dDateUtil.dateDiff();
			this.ovduTerms = dDateUtil.getMons();
			this.ovduDays = dDateUtil.getDays();
		}

		// 期款依應繳日排序
		if (iRepayType == 1 && this.baTxList != null && this.baTxList.size() > 0) {
			this.baTxList.sort((c1, c2) -> {
				return c1.compareTo(c2);
			});
		}
	}

	// 每月月底提息特殊參數設定
	private void caculateSpecialAdjust(int iPayIntDate, int iPrevPaidIntDate, int iNextPayIntDate, LoanBorMain ln,
			LoanCalcRepayIntCom loanCalcRepayIntCom, TitaVo titaVo) throws LogicException {
// 1.短擔以日計算 
// 2.中長擔，已到期按月(下次繳息日 <= 下個月1日)；未到期，首次繳款按日  
// 3. 未到期利息=>以日計息，不分期且到期日當日也算1天 
// 4.未到期以最後一段利率計算(計息程式內處理)

		int nextMonth01 = (this.txBuffer.getTxCom().getNbsdy() / 100) * 100 + 1;
		int thisMonth01 = (this.txBuffer.getTxCom().getTbsdy() / 100) * 100 + 1;

		String intCalcCode = ln.getIntCalcCode();
		int amortizedCode = this.parse.stringToInteger(ln.getAmortizedCode());
		int maturityDate = ln.getMaturityDate();
		String acctCode = loanCalcRepayIntCom.getAcctCode();

		// 短擔=>按日計息
		// 中長擔應繳日為1日=>按月計息但下繳日大於等於下月1日 且 上繳日小於本月1日=>按日計息
		if (acctCode.equals("310")) {
			// 1.短擔
			intCalcCode = "1";// 計息方式 1:按日計息
		} else {
			// 2. 中長擔
			// 2022-04-19 智偉增加
			// 若帳號計息方式是1:按日計息 且 繳息日期(2碼)為1
			if (intCalcCode.equals("1") && ln.getSpecificDd() == 1) {
				// 計息方式改為2:按月計息
				intCalcCode = "2";
			}
			// 2022-04-19 智偉增加
			// 若帳號計息方式是2:按月計息 且 下繳日 大於等於下月1日 且 上繳日小於本月1日
			if (intCalcCode.equals("2") && ln.getNextPayIntDate() >= nextMonth01
					&& ln.getPrevPayIntDate() < thisMonth01) {
				// 計息方式改為1:按日計息
				intCalcCode = "1";
			}
			// 2022-04-19 智偉補充說明:其他照帳號原本的計息方式
		}

		// 已到期
		// 1.繳息日(NextPayIntDate)月份 <= 當月
		// 2.計息起日(PrevPaidIntDate)月份 <= 當月
		// 3.計息止日月份(NextPayIntDate) <= 當月
		// 4.到期日(NextPayIntDate)月份 > 當月
		// else 未到期
		// 未到期利息=>以日計息，不分期且到期日當日也算1天
		int yearMonth = this.txBuffer.getTxCom().getTbsdy() / 100;
		boolean isShouldPaid = false; // 已到期
		if (iPrevPaidIntDate / 100 <= yearMonth && iNextPayIntDate / 100 <= yearMonth
				&& ln.getMaturityDate() / 100 > yearMonth) {
			isShouldPaid = true;
		}
		// 到期日為下月1日前
		if (ln.getMaturityDate() < nextMonth01) {
			dDateUtil.init();
			dDateUtil.setDate_1(maturityDate);
			dDateUtil.setDays(1);
			maturityDate = dDateUtil.getCalenderDay();
		}

		boolean isDefault = false; // 按原設定
		// 未到期繳息日為1日超過1個月=>按原設定
		if (!isShouldPaid) {
			if (iPrevPaidIntDate == thisMonth01 && iNextPayIntDate >= nextMonth01) {
				isDefault = true;
			}
		}
		if (!isShouldPaid && !isDefault) {
			intCalcCode = "1"; // 1:按日計息
			amortizedCode = 2; // 2.到期取息(到期繳息還本)
			int intEndDate = iPayIntDate > maturityDate ? maturityDate : iPayIntDate;
			loanCalcRepayIntCom.setIntEndDate(intEndDate); // 計息止日
			loanCalcRepayIntCom.setTerms(0); // 本次計息期數
			loanCalcRepayIntCom.setNextPayIntDate(maturityDate); // 下次繳息日,應繳息日,預定收息日
			loanCalcRepayIntCom.setNextRepayDate(maturityDate); // 下次還本日,應還本日,預定還本日
			loanCalcRepayIntCom.setMaturityDate(maturityDate);
			loanCalcRepayIntCom.setDueAmt(BigDecimal.ZERO);
		}

		loanCalcRepayIntCom.setIntCalcCode(intCalcCode);
		loanCalcRepayIntCom.setAmortizedCode(amortizedCode);
		this.info("Caculate log BaTxCom Set ... 戶號= " + ln.getCustNo() + "-" + ln.getFacmNo() + "-" + ln.getBormNo()
				+ ", isShouldPaid=" + isShouldPaid + ", AcctCode=" + acctCode + ", CalcCode=" + ln.getIntCalcCode()
				+ "/" + intCalcCode + ", AmortizedCode=" + ln.getAmortizedCode() + "/" + amortizedCode
				+ ", SpecificDate=" + ln.getSpecificDate() + " ,PrevPaidIntDate=" + ln.getPrevPayIntDate() + "/"
				+ iPrevPaidIntDate + " ,NextPayIntDate=" + ln.getNextRepayDate() + "/" + iNextPayIntDate
				+ " ,MaturityDate=" + ln.getMaturityDate() + "/" + maturityDate);

	}

	private void repayLoanBaTxVo(int iEntryDate, int iPayIntDate, int iRepayType, int iCustNo, LoanBorMain ln,
			ArrayList<CalcRepayIntVo> lCalcRepayIntVo, int terms) {
		baTxVo = new BaTxVo();
		baTxVo.setDataKind(2); // 2.本金利息
		// 還款類別
		baTxVo.setRepayType(iRepayType);
		baTxVo.setReceivableFlag(0); // 銷帳科目記號 0:非銷帳科目
		baTxVo.setCustNo(iCustNo); // 借款人戶號
		baTxVo.setFacmNo(ln.getFacmNo()); // 額度編號
		baTxVo.setBormNo(ln.getBormNo()); // 撥款序號
		baTxVo.setRvNo(" "); // 銷帳編號
		baTxVo.setPaidTerms(terms); // 繳息期數
		baTxVo.setPayIntDate(loanCalcRepayIntCom.getPrevPaidIntDate()); // 應繳息日
		baTxVo.setAcctCode(loanCalcRepayIntCom.getAcctCode()); // 業務科目
		// 含短繳本金之還款本金超過餘額，回收本金須扣除
		BigDecimal wkPrincipal = loanCalcRepayIntCom.getPrincipal();
		for (BaTxVo ba : baTxList) {
			if ("Z".equals(ba.getAcctCode().substring(0, 1)) && ln.getFacmNo() == ba.getFacmNo()
					&& ln.getBormNo() == ba.getBormNo()) {
				if (wkPrincipal.add(ba.getUnPaidAmt()).compareTo(ln.getLoanBal()) > 0) {
					wkPrincipal = ln.getLoanBal().subtract(ba.getUnPaidAmt());
					this.info("短繳本金" + ba.getUnPaidAmt() + " 回收本金" + loanCalcRepayIntCom.getPrincipal() + " 超過餘額 "
							+ ln.getLoanBal() + ", 還款本金= " + wkPrincipal);
				}
			}
		}
		baTxVo.setPrincipal(wkPrincipal); // 本金
		baTxVo.setInterest(loanCalcRepayIntCom.getInterest()); // // 利息
		baTxVo.setDelayInt(loanCalcRepayIntCom.getDelayInt()); // 延滯息
		baTxVo.setBreachAmt(loanCalcRepayIntCom.getBreachAmt()); // 違約金
		baTxVo.setLoanBalPaid(ln.getLoanBal().subtract(wkPrincipal)); // 還款後餘額
		this.principal = this.principal.add(wkPrincipal);
		this.interest = this.interest.add(loanCalcRepayIntCom.getInterest());
		this.delayInt = this.delayInt.add(loanCalcRepayIntCom.getDelayInt());
		this.breachAmt = this.breachAmt.add(loanCalcRepayIntCom.getBreachAmt());
		baTxVo.setUnPaidAmt(
				baTxVo.getPrincipal().add(baTxVo.getInterest()).add(baTxVo.getDelayInt()).add(baTxVo.getBreachAmt())); // 未收金額
		baTxVo.setDbCr("C"); // 借貸別
		baTxVo.setAcctAmt(BigDecimal.ZERO); // 出帳金額
		baTxVo.setLoanBal(this.calcLoanBal); // 放款餘額(還款前、只放第一期)
		this.calcLoanBal = BigDecimal.ZERO;
		baTxVo.setExtraAmt(loanCalcRepayIntCom.getExtraAmt()); // 提前償還金額
		if (lCalcRepayIntVo != null) {
			for (CalcRepayIntVo ca : lCalcRepayIntVo) {
				if (baTxVo.getIntStartDate() == 0) {
					baTxVo.setIntStartDate(ca.getStartDate()); // 計息起日
					baTxVo.setAmount(ca.getAmount()); // 計息本金
					baTxVo.setIntRate(ca.getStoreRate()); // 計息利率
					baTxVo.setRateIncr(ca.getRateIncr()); // 加碼利率
					baTxVo.setIndividualIncr(ca.getIndividualIncr()); // 個別加碼利率
				}
				baTxVo.setIntEndDate(ca.getEndDate()); // 計息止日
				// 目前利率
				if (this.rateEffectDate == 0) {
					this.rateEffectDate = baTxVo.getIntStartDate();
					this.fitRate = ca.getStoreRate();
				}
				if (baTxVo.getIntStartDate() > rateEffectDate
						&& baTxVo.getIntStartDate() <= this.txBuffer.getTxCom().getTbsdy()) {
					this.rateEffectDate = baTxVo.getIntStartDate();
					this.fitRate = ca.getStoreRate();
				}
			}
			if (baTxVo.getLoanBalPaid().compareTo(BigDecimal.ZERO) == 0) {
				if (baTxVo.getIntEndDate() >= ln.getMaturityDate()) {
					baTxVo.setCloseFg(1); // 1.正常結案
				} else {
					baTxVo.setCloseFg(2); // 2.提前結案
				}
			}
		}
		// 應繳本利
		if (baTxVo.getPayIntDate() <= this.txBuffer.getTxCom().getTbsdy() || baTxVo.getRepayType() >= 2) {
			this.unPayLoan = this.unPayLoan.add(baTxVo.getUnPaidAmt());
		}
		this.baTxList.add(baTxVo);
	}

	private void emptyLoanBaTxVo(int iEntryDate, int iRepayType, int iCustNo, LoanBorMain ln) {
		this.info("BaTxCom EmptyLoanBaTxVo ...");
		baTxVo = new BaTxVo();
		baTxVo.setDataKind(2); // 2.本金利息
		baTxVo.setIntStartDate(ln.getPrevPayIntDate()); // 上次繳息
		baTxVo.setIntEndDate(ln.getNextPayIntDate()); // 下次繳息日
		baTxVo.setRepayType(iRepayType); // 還款類別
		baTxVo.setReceivableFlag(0); // 銷帳科目記號 0:非銷帳科目
		baTxVo.setCustNo(iCustNo); // 借款人戶號
		baTxVo.setFacmNo(ln.getFacmNo()); // 額度編號
		baTxVo.setBormNo(ln.getBormNo()); // 撥款序號
		baTxVo.setRvNo(" "); // 銷帳編號
		baTxVo.setAcctCode(loanCalcRepayIntCom.getAcctCode()); // 業務科目
		baTxVo.setLoanBalPaid(this.calcLoanBal); // 還款後餘額
		baTxVo.setLoanBal(this.calcLoanBal); // 放款餘額(還款前、只放第一期)
		this.calcLoanBal = BigDecimal.ZERO;
		this.baTxList.add(baTxVo);
	}

	// 計算清償違約金
	private void getCloseBreachAmt(int iEntryDate, int iCustNo, int iFacmNo, int iRepayType, TitaVo titaVo)
			throws LogicException {
		this.info("getCloseBreachAmt...");
		String collectFlag = this.tempVo.getParam("CollectFlag"); // 是否領取清償證明(Y/N/)

		loanCloseBreachCom.setTxBuffer(this.txBuffer);
		ArrayList<LoanCloseBreachVo> iListCloseBreach = new ArrayList<LoanCloseBreachVo>();
		ArrayList<LoanCloseBreachVo> oListCloseBreach = new ArrayList<LoanCloseBreachVo>();
		for (BaTxVo baTxVo : this.baTxList) {
			if (baTxVo.getDataKind() == 2) {
				LoanCloseBreachVo v = new LoanCloseBreachVo();
				v.setCustNo(baTxVo.getCustNo());
				v.setFacmNo(baTxVo.getFacmNo());
				v.setBormNo(baTxVo.getBormNo());
				v.setExtraRepay(baTxVo.getExtraAmt());
				v.setEndDate(iEntryDate);
				iListCloseBreach.add(v);
			}
		}
		if ("Y".equals(collectFlag)) {
			oListCloseBreach = loanCloseBreachCom.getCloseBreachAmtAll(iEntryDate, iCustNo, iFacmNo, 0,
					iListCloseBreach, titaVo);
		} else {
			oListCloseBreach = loanCloseBreachCom.getCloseBreachAmtPaid(iCustNo, iFacmNo, 0, iListCloseBreach, titaVo);
		}

		if (oListCloseBreach != null && oListCloseBreach.size() > 0) {
			for (LoanCloseBreachVo v : oListCloseBreach) {
				if (v.getCloseBreachAmt().compareTo(BigDecimal.ZERO) > 0) {
					this.closeBreachAmt = this.closeBreachAmt.add(v.getCloseBreachAmt());
					baTxVo = new BaTxVo();
					baTxVo.setDataKind(2); // 2.本金利息=>提前償還有即時清償違約金列短繳(TxBatchCom:人工處理)
					baTxVo.setRepayType(iRepayType);
					baTxVo.setReceivableFlag(0); // 銷帳科目記號 0:非銷帳科目
					baTxVo.setCustNo(iCustNo); // 借款人戶號
					baTxVo.setFacmNo(v.getFacmNo()); // 額度編號
					baTxVo.setBormNo(v.getBormNo()); // 撥款序號
					baTxVo.setRvNo(" "); // 銷帳編號
					baTxVo.setCloseBreachAmt(v.getCloseBreachAmt()); // 違約金
					baTxVo.setUnPaidAmt(v.getCloseBreachAmt()); // 未收金額
					baTxVo.setDbCr("C"); // 借貸別
					baTxVo.setAcctAmt(BigDecimal.ZERO); // 出帳金額
					this.baTxList.add(baTxVo);
				}
			}
		}
		this.info("getCloseBreachAmt end collectFlag=" + collectFlag + ", CloseBreachAmt=" + this.closeBreachAmt);

	}

	/* 設定費用還款順序 */
	private void settlePriority(int iRepayType, BigDecimal iTxAmt) {
		// 1.還款類別、金額相同 > 2.還款類別相同 > 3:未收費用 > 4:短繳期金 > 5:應繳本利 > 6:另收欠款
		for (BaTxVo ba : this.baTxList) {
			if (ba.getRepayType() >= 4 && ba.getRepayType() == iRepayType && iTxAmt.compareTo(ba.getUnPaidAmt()) == 0) {
				ba.setRepayPriority(1);
			} else if (ba.getDataKind() == 1) {
				if (ba.getRepayType() >= 4 && ba.getRepayType() == iRepayType) {
					ba.setRepayPriority(2);
				} else if (ba.getRepayType() == 1) {
					ba.setRepayPriority(4);
				} else {
					ba.setRepayPriority(3);
				}
			} else if (ba.getDataKind() == 2) {
				ba.setRepayPriority(5);
			} else if (ba.getDataKind() == 6) {
				ba.setRepayPriority(6);
			} else {
			}
		}
	}

	/* 計算作帳金額 */
	private void settleAcctAmt(int repayPriority) {
		this.info("settleAcctAmt repayPriority=" + repayPriority);
		// 短繳期金
		if (repayPriority == 4) {
			for (BaTxVo ba : this.baTxList) {
				if (ba.getDataKind() == 1 && ba.getRepayType() == 1) {
					boolean isCaculate = false;
					for (BaTxVo t : this.baTxList) {
						if (t.getDataKind() == 2 && ba.getFacmNo() == t.getFacmNo()
								&& ba.getBormNo() == t.getBormNo()) {
							ba.setAcctCode(t.getAcctCode());
							ba.setPayIntDate(t.getPayIntDate());
							ba.setIntStartDate(t.getIntStartDate());
							ba.setIntEndDate(t.getIntStartDate());
							isCaculate = true;
							break;
						}
					}
					// 未計息放期款第一筆
					if (!isCaculate) {
						for (BaTxVo t : this.baTxList) {
							if (t.getDataKind() == 2) {
								ba.setAcctCode(t.getAcctCode());
								ba.setPayIntDate(t.getPayIntDate());
								ba.setIntStartDate(t.getIntStartDate());
								ba.setIntEndDate(t.getIntStartDate());
								break;
							}
						}
					}
				}
			}
		}

		for (BaTxVo ba : this.baTxList) {
			if (this.isPayAllFee) {
				// 全部回收費用
				if (ba.getRepayPriority() == repayPriority && ba.getAcctAmt().equals(BigDecimal.ZERO)) { // 尚未作帳
					ba.setAcctAmt(ba.getUnPaidAmt()); // AcctAmt = UnPaidAmt
					this.xxBal = this.xxBal.subtract(ba.getAcctAmt());
					this.txBal = this.txBal.subtract(ba.getAcctAmt());
					this.repayTotal = this.repayTotal.add(ba.getUnPaidAmt());
					this.shortFacmNo = ba.getFacmNo();// 短繳額度;
					if (this.overRpFacmNo == 0) {
						this.overRpFacmNo = ba.getFacmNo();// 溢短繳額度;
					}
				}
			} else {
				// 部分回收費用，可償還餘額比作帳金額大時回收
				if (ba.getRepayPriority() == repayPriority && ba.getAcctAmt().equals(BigDecimal.ZERO) // 尚未作帳
						&& this.xxBal.compareTo(ba.getUnPaidAmt()) >= 0) { // this.xxBal >= UnPaidAmt
					ba.setAcctAmt(ba.getUnPaidAmt()); // AcctAmt = UnPaidAmt
					this.xxBal = this.xxBal.subtract(ba.getAcctAmt()); // this.xxBal = this.xxBal - AcctAmt
					this.txBal = this.txBal.subtract(ba.getAcctAmt()); //
					this.repayTotal = this.repayTotal.add(ba.getUnPaidAmt());
					this.shortFacmNo = ba.getFacmNo();// 短繳額度;
					if (this.overRpFacmNo == 0) {
						this.overRpFacmNo = ba.getFacmNo();// 溢短繳額度;
					}
				}
			}
		}
	}

	/* 按額度應繳日回收，應繳日由小到大、計息順序(利率由大到小)、額度由小到大 */
	/* 最後額度可欠繳、允許第二順位還款額度 */
	private int settleByPayintDate() {
		this.info("settleByPayintDate ...xxBal=" + this.xxBal);
		// 本金利息(按應繳日加總至額度)
		int facmNo = 0;
		int payIntDate = 0;
		BigDecimal payintDateAmt = BigDecimal.ZERO;
		int repayIntDate = 0;
		for (BaTxVo ba : this.baTxList) {
			if (ba.getRepayPriority() == 5 && ba.getPayIntDate() > 0) {
				if (payIntDate != ba.getPayIntDate() || facmNo != ba.getFacmNo()) {
					payIntDate = ba.getPayIntDate();
					facmNo = ba.getFacmNo();
					payintDateAmt = getPayintDateAmt(payIntDate, facmNo);
					// 足額還款額度設定作帳金額；不足還款額度設定Priority=9
					if (this.xxBal.add(this.shortAmtLimit).compareTo(payintDateAmt) >= 0) {
						settlePayintDateAmt(payIntDate, facmNo);
						this.repayIntDateByFacmNoVo.putParam(parse.IntegerToString(facmNo, 3), payIntDate); // 額度還款應繳日
						repayIntDate = payIntDate;
					} else {
						for (BaTxVo b : this.baTxList) {
							if (b.getRepayPriority() == 5 && b.getFacmNo() == facmNo
									&& b.getAcctAmt().equals(BigDecimal.ZERO)) {
								b.setRepayPriority(9);
							}
						}
					}
				}
			}
		}

		// 回復不足還款額度Priority
		for (BaTxVo b : this.baTxList) {
			if (b.getRepayPriority() == 9) {
				b.setRepayPriority(5);
			}
		}

		// 無還款資料，計算第一筆
		if (repayIntDate == 0) {
			for (BaTxVo ba : this.baTxList) {
				if (ba.getRepayPriority() == 5 && ba.getPayIntDate() > 0) {
					payIntDate = ba.getPayIntDate();
					facmNo = ba.getFacmNo();
					payintDateAmt = getPayintDateAmt(payIntDate, facmNo);
					settlePayintDateAmt(payIntDate, facmNo);
					break;
				}
			}
		}

		return repayIntDate;
	}

	// 取得額度應繳日金額，短繳限額
	private BigDecimal getPayintDateAmt(int payIntDate, int facmNo) {
		BigDecimal wkPayintDateAmt = BigDecimal.ZERO;
		BigDecimal wkShortAmtLimit = BigDecimal.ZERO;
		BigDecimal wkNowBal = this.xxBal;
		BigDecimal wkUnpaidAmt = BigDecimal.ZERO;
		this.shortAmtLimit = BigDecimal.ZERO;
		for (BaTxVo ba : this.baTxList) {
			if (ba.getFacmNo() == facmNo && ba.getAcctAmt().equals(BigDecimal.ZERO)) {
				if (ba.getRepayPriority() == 5 && ba.getPayIntDate() == payIntDate) {
					wkPayintDateAmt = wkPayintDateAmt.add(ba.getUnPaidAmt());
					// 可欠繳且最後一期可欠繳
					if (this.unpaidFlag.equals("Y") && this.xxBal.compareTo(BigDecimal.ZERO) > 0) {
						// 依短繳限額 1.還本金額 2.還息金額
						if (ba.getPrincipal().compareTo(BigDecimal.ZERO) > 0) {
							wkShortAmtLimit = ba.getPrincipal()
									.multiply(new BigDecimal(this.txBuffer.getSystemParas().getShortPrinPercent()))
									.divide(new BigDecimal(100)).setScale(0, RoundingMode.HALF_UP);
							if (this.txBuffer.getSystemParas().getShortPrinLimit() > 0 && wkShortAmtLimit.compareTo(
									new BigDecimal(this.txBuffer.getSystemParas().getShortPrinLimit())) > 0) {
								wkShortAmtLimit = new BigDecimal(this.txBuffer.getSystemParas().getShortPrinLimit());
							}
							// 短繳金額= 負餘額(取正值)-已短繳餘額
							wkNowBal = wkNowBal.subtract(ba.getUnPaidAmt());
							if (wkNowBal.compareTo(BigDecimal.ZERO) < 0) {
								ba.setUnpaidPrin(BigDecimal.ZERO.subtract(wkNowBal).subtract(wkUnpaidAmt));
								wkUnpaidAmt = wkUnpaidAmt.add(ba.getUnpaidPrin());
							}
						} else {
							wkShortAmtLimit = ba.getInterest()
									.multiply(new BigDecimal(this.txBuffer.getSystemParas().getShortIntPercent()))
									.divide(new BigDecimal(100)).setScale(0, RoundingMode.HALF_UP);
							// 短繳金額= 負餘額(取正值)-已短繳餘額
							wkNowBal = wkNowBal.subtract(ba.getUnPaidAmt());
							if (wkNowBal.compareTo(BigDecimal.ZERO) < 0) {
								ba.setUnpaidInt(BigDecimal.ZERO.subtract(wkNowBal).subtract(wkUnpaidAmt));
								wkUnpaidAmt = wkUnpaidAmt.add(ba.getUnpaidInt());
							}
						}
						this.shortAmtLimit = this.shortAmtLimit.add(wkShortAmtLimit);
					}
				}
			}
		}
		this.info("getPayintDateAmt end xxBal=" + this.xxBal + ", FacmNo= " + facmNo + ", rePayIntDate=" + payIntDate
				+ ", payintDateAmt=" + wkPayintDateAmt + ", wkUnpaidAmt=" + wkUnpaidAmt + ", shortAmtLimit="
				+ shortAmtLimit);

		return wkPayintDateAmt;

	}

	// 更新額度應繳日金額
	private void settlePayintDateAmt(int payIntDate, int facmNo) {
		for (BaTxVo ba : this.baTxList) {
			if (ba.getFacmNo() == facmNo && ba.getAcctAmt().equals(BigDecimal.ZERO)) { // 尚未作帳
				if (ba.getRepayPriority() == 5 && ba.getPayIntDate() == payIntDate) {
					ba.setAcctAmt(ba.getUnPaidAmt()); // AcctAmt = UnPaidAmt
					this.repayTotal = this.repayTotal.add(ba.getUnPaidAmt());
					this.xxBal = this.xxBal.subtract(ba.getAcctAmt());
					this.txBal = this.txBal.subtract(ba.getAcctAmt());
					this.shortFacmNo = ba.getFacmNo();// 短繳額度;
					if (this.overRpFacmNo == 0) {
						this.overRpFacmNo = ba.getFacmNo();// 溢繳額度;
					}
				}
			}
		}
		this.info("settlePayintDateAmt end " + payIntDate + ", FacmNo=" + facmNo + ", xxbal=" + this.xxBal);
	}

	/* 計算暫收抵繳作帳金額 */
	private void settleTmpAcctAmt() {
		for (BaTxVo ba : this.baTxList) {
			if (ba.getDataKind() == 3) {
				ba.setAcctAmt(ba.getUnPaidAmt());
				this.tempAmt = this.tempAmt.add(ba.getAcctAmt());
			}
		}
		this.info("tavAmt 可暫收抵繳金額= " + this.tavAmt + ", tempAmt暫收款金額" + this.tempAmt);
	}

	/* 計算溢(C)短(D)繳 */
	private void settleOverAmt(int iCustNo, int iFacmNo, TitaVo titaVo) {
		// 溢短繳額度;
		if (iFacmNo > 0) {
			this.overRpFacmNo = iFacmNo;
		}
		if (this.overRpFacmNo == 0) {
			this.overRpFacmNo = gettingRpFacmNo(iCustNo, titaVo);
		}
		this.info("settleOverAmt ... overRpFacmNo= " + this.overRpFacmNo + ", xxBal=" + this.xxBal + ", tempAmt="
				+ this.tempAmt);

		baTxVo = new BaTxVo();
		baTxVo.setDataKind(4); // 4.本期溢(+)短(-)繳
		baTxVo.setRepayType(0);
		baTxVo.setCustNo(iCustNo);
		baTxVo.setBormNo(0);
		baTxVo.setRvNo(" ");
		baTxVo.setAcctCode("TAV");
		baTxVo.setFacmNo(this.overRpFacmNo);
		// 溢繳 = 可償還餘額xxBal為正值時
		// 短繳 = 可償還餘額xxBal為負值時取正值
		if (this.xxBal.compareTo(BigDecimal.ZERO) >= 0) {
			baTxVo.setDbCr("C");
			baTxVo.setUnPaidAmt(this.xxBal);
			baTxVo.setAcctAmt(this.xxBal);
			this.overAmt = baTxVo.getAcctAmt();
			this.info("overRpFacmNo= " + this.overRpFacmNo + ", xxBal=" + this.xxBal + ", tempAmt=" + this.tempAmt
					+ ", 溢繳金額= " + this.overAmt);
		}
		if (this.xxBal.compareTo(BigDecimal.ZERO) < 0) {
			baTxVo.setDbCr("D");
			baTxVo.setUnPaidAmt(BigDecimal.ZERO.subtract(this.xxBal));
			this.shortAmt = baTxVo.getUnPaidAmt();
			this.overRpFacmNo = this.shortFacmNo;
			baTxVo.setFacmNo(this.shortFacmNo);
			this.info("shortRpFacmNo= " + this.shortFacmNo + ", xxBal=" + this.xxBal + ", tempAmt=" + this.tempAmt
					+ ", 短繳金額= " + this.shortAmt);
		}
		this.baTxList.add(baTxVo);
	}

	private int gettingRpFacmNo(int CustNo, TitaVo titaVo) {
		// 無應繳資料 ---> 業務科目記號 1:資負明細科目，最大
		int rpFacmNo = 0;
		if (this.baTxList != null && this.baTxList.size() > 0) {
			// 計息第一筆
			for (BaTxVo ba : this.baTxList) {
				if (ba.getDataKind() == 2) {
					rpFacmNo = ba.getFacmNo();
					break;
				}
			}
			// 其他第一筆
			if (rpFacmNo == 0) {
				for (BaTxVo ba : this.baTxList) {
					rpFacmNo = ba.getFacmNo();
					break;
				}
			}
		}
		if (rpFacmNo == 0) {
			// 資負明細科目，額度最大，先找未銷
			// 0-未銷
			Slice<AcReceivable> srvList = acReceivableService.acrvFacmNoRange(0, CustNo, 1, 0, 999, 0,
					Integer.MAX_VALUE, titaVo);
			// 1-已銷
			if (srvList == null) {
				srvList = acReceivableService.acrvFacmNoRange(1, CustNo, 1, 0, 999, 0, Integer.MAX_VALUE, titaVo);
			}
			if (srvList != null) {
				rpFacmNo = srvList.getContent().get(srvList.getContent().size() - 1).getFacmNo();
			}
		}
		this.info("gettingRpFacmNo=" + rpFacmNo);
		return rpFacmNo;
	}

	// 計算作帳金額(檢核報表)
	private void settleAcAmtBatxList(int iRepayType, BigDecimal iTxAmt, BigDecimal iShortAmt) {
		// 計算短繳金額，期款在計算可回收時已計算
		// 部分償還有短繳金額時，短繳金額先扣除累短收-利息，再短繳本次利息，期款部分已計算
		if (iRepayType == 2) {
			BigDecimal wkUnpaidAmtRemaind = iShortAmt;
			for (BaTxVo ba : this.baTxList) {
				if (ba.getDataKind() == 1 && ba.getRepayType() <= 3 && ba.getAcctAmt().compareTo(BigDecimal.ZERO) > 0) {
					if (wkUnpaidAmtRemaind.compareTo(ba.getInterest()) > 0) {
						wkUnpaidAmtRemaind = wkUnpaidAmtRemaind.subtract(ba.getInterest());
						ba.setUnpaidInt(ba.getInterest());
					} else {
						ba.setUnpaidInt(wkUnpaidAmtRemaind);
						wkUnpaidAmtRemaind = BigDecimal.ZERO;
					}
					this.info(" 2-1 UnpaidInt=" + ba.getUnpaidInt() + ", wkUnpaidAmtRemaind =" + wkUnpaidAmtRemaind);
				}
			}
			for (BaTxVo ba : this.baTxList) {
				if (ba.getDataKind() == 2 && ba.getAcctAmt().compareTo(BigDecimal.ZERO) > 0) {
					if (wkUnpaidAmtRemaind.compareTo(ba.getInterest()) > 0) {
						wkUnpaidAmtRemaind = wkUnpaidAmtRemaind.subtract(ba.getInterest());
						ba.setUnpaidInt(ba.getInterest());
					} else {
						ba.setUnpaidInt(wkUnpaidAmtRemaind);
						wkUnpaidAmtRemaind = BigDecimal.ZERO;
					}
					this.info(" 2-2 UnpaidInt=" + ba.getUnpaidInt() + ", wkUnpaidAmtRemaind =" + wkUnpaidAmtRemaind);
				}
			}
		}

		// 計算作帳金額
		BigDecimal wkTxBal = iTxAmt;
		BigDecimal wkOverBal = BigDecimal.ZERO;
		BigDecimal wkTempBal = BigDecimal.ZERO;
		BigDecimal wkAcBal = BigDecimal.ZERO;
		boolean isRepayLoan = false;

		for (BaTxVo ba : this.baTxList) {
			switch (ba.getDataKind()) {
			case 1:
			case 2:
			case 6:
				// 計算作帳金額(扣除短繳)
				ba.setAcAmt(ba.getAcctAmt().subtract(ba.getUnpaidPrin()).subtract(ba.getUnpaidInt()));
				// 計算作帳金額
				wkAcBal = wkAcBal.add(ba.getAcAmt());

				// 計算還放款還款金額
				if ((ba.getRepayType() >= 1 && ba.getRepayType() <= 3)) {
					isRepayLoan = true;
				}
			case 3:
				// 計算暫收抵繳
				wkTempBal = wkTempBal.add(ba.getAcctAmt());
				break;
			case 4:
				// 計算短溢收金額 ，溢(C)短(D)繳
				if ("C".equals(ba.getDbCr())) {
					wkOverBal = wkOverBal.add(ba.getAcctAmt());
				}
				break;
			default:
				break;
			}
			this.info("settleAcAmt= " + ba.toString());
		}

		BigDecimal wkDiff = wkTxBal.add(wkTempBal).subtract(wkAcBal).subtract(wkOverBal);
		this.info("settleAcAmt isRepayLoan=" + isRepayLoan + ", wkTxBal=" + wkTxBal + ", wkTempBal=" + wkTempBal
				+ ", wkAcBal=" + wkAcBal + ", wkOverBal=" + wkOverBal + ", diff=" + wkDiff);
		// 額度科目
		for (BaTxVo ba : this.baTxList) {
			if (ba.getDataKind() == 2) {
				ba.setFacAcctCode(ba.getAcctCode());
			} else if (this.facStatus > 0) {
				ba.setFacAcctCode("999");
			} else if (ba.getFacmNo() == 0) {
				ba.setFacAcctCode("999");
			} else {
				for (BaTxVo da : this.baTxList) {
					if (da.getDataKind() == 2 && da.getFacmNo() == ba.getFacmNo()) {
						ba.setFacAcctCode(da.getAcctCode());
						break;
					}
				}
			}
		}

		// 回收短繳=>放在償還本利那筆
		for (BaTxVo ba : this.baTxList) {
			if (ba.getDataKind() == 1 && ba.getRepayType() <= 3 && ba.getAcAmt().compareTo(BigDecimal.ZERO) > 0) {
				for (BaTxVo da : this.baTxList) {
					if (da.getDataKind() == 2 && da.getAcAmt().compareTo(BigDecimal.ZERO) > 0
							&& (da.getFacmNo() == ba.getFacmNo() || ba.getFacmNo() == 0)
							&& (da.getBormNo() == ba.getBormNo() || ba.getBormNo() == 0)) {
						da.setShortFallPrin(ba.getPrincipal());
						da.setShortFallInt(ba.getInterest());
						da.setShortFallCloseBreach(ba.getCloseBreachAmt());
						da.setAcAmt(da.getAcAmt().add(ba.getAcAmt()));
						ba.setAcAmt(BigDecimal.ZERO);
					}
				}
			}
		}

		// 交易金額、暫收借、暫收貸
		// 存暫收
		if (!isRepayLoan) {
			for (BaTxVo ba : this.baTxList) {
				if (ba.getDataKind() == 4) {
					ba.setTxAmt(wkTxBal);
					ba.setTempAmt(wkTempBal);
					ba.setOverflow(wkTxBal.add(wkTempBal));
					wkTxBal = BigDecimal.ZERO;
					wkTempBal = BigDecimal.ZERO;
				}
			}
		}
		int acSeq = 0;
		// 費用 => 還放款為依序抵繳、暫收款為抵繳費用金額
		for (BaTxVo ba : this.baTxList) {
			if (ba.getRepayType() >= 4 && ba.getAcAmt().compareTo(BigDecimal.ZERO) > 0) {
				acSeq++;
				ba.setAcSeq(acSeq);
				// 還放款
				if (isRepayLoan) {
					if (wkTempBal.compareTo(ba.getAcAmt()) >= 0) {
						ba.setTempAmt(ba.getAcAmt());
						ba.setTxAmt(BigDecimal.ZERO);
						wkTempBal = wkTempBal.subtract(ba.getTempAmt());
					} else {
						ba.setTempAmt(wkTempBal);
						ba.setTxAmt(ba.getAcAmt().subtract(wkTempBal));
						wkTempBal = BigDecimal.ZERO;
						wkTxBal = wkTxBal.subtract(ba.getTxAmt());
					}
				} else {
					ba.setTempAmt(ba.getAcAmt());
					ba.setTxAmt(BigDecimal.ZERO);
				}
			}
		}
		// 還放款
		if (isRepayLoan) {
			for (LoanBorMain ln : this.lLoanBorMain) {
				for (BaTxVo ba : this.baTxList) {
					if (ba.getRepayType() <= 3 && ba.getAcAmt().compareTo(BigDecimal.ZERO) > 0) {
						if (ba.getAcSeq() == 0) {
							if ((ba.getFacmNo() == ln.getFacmNo() || ba.getFacmNo() == 0)
									&& (ba.getBormNo() == ln.getBormNo() || ba.getBormNo() == 0)) {
								acSeq++;
								ba.setAcSeq(acSeq);
								if (wkTempBal.compareTo(ba.getAcAmt()) >= 0) {
									ba.setTempAmt(ba.getAcAmt());
									ba.setTxAmt(BigDecimal.ZERO);
									wkTempBal = wkTempBal.subtract(ba.getTempAmt());
								} else {
									ba.setTempAmt(wkTempBal);
									ba.setTxAmt(ba.getAcAmt().subtract(wkTempBal));
									wkTempBal = BigDecimal.ZERO;
									wkTxBal = wkTxBal.subtract(ba.getTxAmt());
								}
							}
						}
					}
				}
			}
		}
		// 有作帳金額的最後一筆，計算溢繳
		if (isRepayLoan) {
			if (acSeq > 0) {
				for (BaTxVo ba : this.baTxList) {
					if (ba.getAcSeq() == acSeq) {
						ba.setTempAmt(ba.getTempAmt().add(wkTempBal));
						ba.setTxAmt(ba.getTxAmt().add(wkTxBal));
						ba.setOverflow(ba.getTempAmt().add(ba.getTxAmt()).subtract(ba.getAcAmt()));
					}
				}
			}
		}
		// 無作帳金額
		for (BaTxVo ba : this.baTxList) {
			if (ba.getRepayType() >= 1 && ba.getAcAmt().compareTo(BigDecimal.ZERO) == 0) {
				acSeq++;
				ba.setAcSeq(acSeq);
			}
		}

		this.info("settleAcAmt end" + ", wkTxBal=" + wkTxBal + ", wkTempBal=" + wkTempBal + ", wkOverBal=" + wkOverBal);

	}

	// 本金利息(加總至撥款)
	private void addByBormNoBaTxList() {
		this.info(" addByBormNo .... ");
		ArrayList<BaTxVo> listbaTxVo = new ArrayList<BaTxVo>();
		for (BaTxVo ba : this.baTxList) {
			if ((ba.getDataKind() == 1 && ba.getRepayType() >= 4) || ba.getDataKind() >= 3) {
				listbaTxVo.add(ba);
			} else {
				if (ba.getAcctAmt().compareTo(BigDecimal.ZERO) > 0) {
					listbaTxVo.add(ba);
				}
			}
		}
		this.baTxList = new ArrayList<BaTxVo>();

		// 不加總
		for (BaTxVo ba : listbaTxVo) {
			if ((ba.getDataKind() == 1 && ba.getRepayType() >= 4) || ba.getDataKind() >= 3) {
				this.baTxList.add(ba);
			}
		}
		// 加總本金利息
		for (BaTxVo ba : listbaTxVo) {
			if (ba.getDataKind() == 2) {
				boolean isNew = true;
				for (BaTxVo da : this.baTxList) {
					if (da.getDataKind() == 2 && da.getFacmNo() == ba.getFacmNo() && da.getBormNo() == ba.getBormNo()) {
						addBaTxList(ba, da);
						isNew = false;
						break;
					}
				}
				if (isNew) {
					this.baTxList.add(ba);
				}
			}
		}

		// 加總短繳、清償違約金
		for (BaTxVo ba : listbaTxVo) {
			if ((ba.getDataKind() == 1 || ba.getDataKind() == 6) && ba.getRepayType() <= 3) {
				boolean isNew = true;
				for (BaTxVo da : this.baTxList) {
					if (da.getDataKind() == ba.getDataKind() && da.getRepayType() == ba.getRepayType()
							&& (da.getFacmNo() == ba.getFacmNo() || ba.getFacmNo() == 0)
							&& (da.getBormNo() == ba.getBormNo() || ba.getBormNo() == 0)) {
						addBaTxList(ba, da);
						isNew = false;
						break;
					}
				}
				if (isNew) {
					this.baTxList.add(ba);
				}
			}
		}
	}

	// 加總
	private void addBaTxList(BaTxVo ba, BaTxVo baTxVo) {
		baTxVo.setPrincipal(baTxVo.getPrincipal().add(ba.getPrincipal())); // 本金
		baTxVo.setInterest(baTxVo.getInterest().add(ba.getInterest())); // // 利息
		baTxVo.setDelayInt(baTxVo.getDelayInt().add(ba.getDelayInt())); // 延滯息
		baTxVo.setBreachAmt(baTxVo.getBreachAmt().add(ba.getBreachAmt())); // 違約金
		baTxVo.setLoanBalPaid(baTxVo.getLoanBalPaid().add(ba.getLoanBalPaid())); // 還款後餘額
		baTxVo.setUnpaidPrin(baTxVo.getUnpaidPrin().add(ba.getUnpaidPrin())); // 短繳本金
		baTxVo.setUnpaidInt(baTxVo.getUnpaidInt().add(ba.getUnpaidInt())); // 短繳利息
		baTxVo.setCloseBreachAmt(baTxVo.getCloseBreachAmt().add(ba.getCloseBreachAmt())); // 清償違約金
		baTxVo.setFeeAmt(baTxVo.getFeeAmt().add(ba.getFeeAmt())); // 費用金額
		baTxVo.setUnPaidAmt(baTxVo.getUnPaidAmt().add(ba.getUnPaidAmt()));
		baTxVo.setAcctAmt(baTxVo.getAcctAmt().add(ba.getAcctAmt())); // 出帳金額
		baTxVo.setLoanBal(baTxVo.getLoanBal().add(ba.getLoanBal())); // 放款餘額(還款前、只放第一期)
		baTxVo.setExtraAmt(baTxVo.getExtraAmt().add(ba.getExtraAmt())); // 提前償還金額
		if (ba.getPaidTerms() > baTxVo.getPaidTerms()) {
			baTxVo.setPaidTerms(ba.getPaidTerms()); // 繳息期數
		}
		if (ba.getIntStartDate() > 0 && ba.getIntStartDate() < baTxVo.getIntStartDate()) {
			baTxVo.setIntStartDate(ba.getIntStartDate()); // 計息起日
		}
		if (ba.getIntEndDate() > baTxVo.getIntEndDate()) {
			baTxVo.setIntEndDate(ba.getIntEndDate()); // 計息止日
		}
		if (baTxVo.getAmount().compareTo(baTxVo.getAmount()) > 0) {
			baTxVo.setAmount(baTxVo.getAmount()); // 計息本金
		}
		if (ba.getCloseFg() < baTxVo.getCloseFg()) {
			baTxVo.setCloseFg(ba.getCloseFg());
		}
	}

	// 合併檢核暫收金額
	private BigDecimal addMergeTempAmt(int iCustNo, int iFacmNo, BigDecimal iMergeTempAmt) {
		this.info("addMergeAmt this.tavAmt=" + this.tavAmt);
		this.tavAmt = this.tavAmt.add(iMergeTempAmt);
		this.excessive = this.excessive.add(iMergeTempAmt); // 累溢收
		baTxVo = new BaTxVo();
		baTxVo.setDataKind(3); // 3.暫收抵繳
		baTxVo.setReceivableFlag(2); // 1:會計銷帳科目(應收) 2:業務銷帳科目(應收) 3:未收費用 4:短繳期金 5.另收欠款
		baTxVo.setCustNo(iCustNo);
		baTxVo.setFacmNo(this.overRpFacmNo);
		baTxVo.setBormNo(0);
		baTxVo.setRvNo(" ");
		baTxVo.setAcctCode("TAV");
		baTxVo.setUnPaidAmt(iMergeTempAmt);
		baTxVo.setAcctAmt(BigDecimal.ZERO);
		this.baTxList.add(baTxVo);
		this.info("addMergeAmt end this.tavAmt=" + this.tavAmt);

		return this.tavAmt;
	}

// 是否額度可抵繳
// 按指定額度：00-全費用類別、 01-期款、02-部分償還、 04~08-費用、09-暫收
//  1.iFacmNo >0 該額度為指定額度則只有該額度可抵繳,如該額度為非指定額度則全部非指定額度可抵繳
//  2.iFacmNo =0 全部非指定額度可抵繳
// 全部額度 ： 03-結案、98-全費用類別(已到期)、99-全部(含未到期)
//  1.iFacmNo >0 該額度為指定額度則只有該額度可抵繳,如該額度為非指定額度則全部非指定額度可抵繳
//  2.iFacmNo =0 全部額度可抵繳
// 暫收可抵繳轉帳；96-轉帳
//	1.iFacmNo >0 限該額度可抵繳

	private Boolean isTmpFacmNo(int iRepayType, int iFacmNo, int facmNo) {

		Boolean isTmpFacmNo = false;

		// 全部額度 ： 03-結案、98-全費用類別(已到期)、99-全部(含未到期)
		// 2.iFacmNo =0 全部額度可抵繳
		if ((iRepayType >= 98 || iRepayType == 3) && iFacmNo == 0) {
			isTmpFacmNo = true;
			this.info("isTmpFacmNo 0 " + isTmpFacmNo);
		} else if (iRepayType == 96) {
			// 暫收可抵繳轉帳；96-轉帳
			// 1.iFacmNo >0 限該額度可抵繳
			if (facmNo == iFacmNo) {
				isTmpFacmNo = true;
			}
		} else {
			// 按指定額度：00-全費用類別、 01-期款、02-部分償還、 04~08-費用、09-暫收
			// 1.iFacmNo >0 該額度為指定額度則只有該額度可抵繳,如該額度為非指定額度則全部非指定額度可抵繳
			// 2.iFacmNo =0 全部非指定額度可抵繳
			// 全部額度 ： 03-結案、98-全費用類別(已到期)、99-全部(含未到期)
			if (iFacmNo > 0) {
				Boolean isLoanFacTmp = false;
				for (LoanFacTmp t : lLoanFacTmp) {
					if (iFacmNo == t.getFacmNo()) {
						isLoanFacTmp = true;
					}
				}
				// 輸入額度為指定額度則只有該額度可暫收抵繳
				if (isLoanFacTmp) {
					if (facmNo == iFacmNo) {
						isTmpFacmNo = true;
						this.info("isTmpFacmNo 1 " + isTmpFacmNo);
					}
				}
				// 輸入額度為非指定額度則全部非指定額度可暫收抵繳
				else {
					isLoanFacTmp = false;
					isTmpFacmNo = false;
					for (LoanFacTmp tmp : lLoanFacTmp) {
						this.info("tmp.getFacmNo = " + tmp.getFacmNo());
						if (facmNo == tmp.getFacmNo()) {
							isLoanFacTmp = true;
						}
					}
					if (!isLoanFacTmp) {
						isTmpFacmNo = true;
						this.info("isTmpFacmNo 3 " + isTmpFacmNo);
					}
				}
			}
			// 2.iFacmNo =0 全部非指定額度可抵繳
			if (iFacmNo == 0) {
				Boolean isLoanFacTmp = false;
				for (LoanFacTmp t : lLoanFacTmp) {
					if (facmNo == t.getFacmNo()) {
						isLoanFacTmp = true;
					}
				}
				if (!isLoanFacTmp) {
					isTmpFacmNo = true;
					this.info("isTmpFacmNo 4 " + isTmpFacmNo);
				}
			}
		}

		this.info("isTmpFacmNo end " + isTmpFacmNo + ", iRepayType = " + iRepayType + ", iFacmNo = " + iFacmNo
				+ ", facmNo = " + facmNo);
		return isTmpFacmNo;
	}

// 是否為費用額度
// 按指定額度：00-全費用類別、 01-期款、02-部分償還、 04~08-費用
//   1.iFacmNo >0 該額度費用
//   2.iFacmNo =0 全部非指定額度費用
// 全部額度 ： 03-結案、98-全費用類別(已到期)、99-全部(含未到期)
//   1.iFacmNo >0 該額度費用
//   2.iFacmNo =0 全部額度費用
// 暫收可抵繳轉帳；96-轉帳
//	 1.無
	private Boolean isFeeFacmNo(int iRepayType, int iFacmNo, int facmNo) {
		Boolean isFeeFacmNo = false;
		if (iRepayType == 96) {
			return false;
		}
		if (iFacmNo > 0) {
			if (iFacmNo == facmNo) {
				isFeeFacmNo = true;
			}
		}

		if (iFacmNo == 0) {
			if (iRepayType >= 98 || iRepayType == 3) {
				isFeeFacmNo = true;
			} else {
				Boolean isLoanFacTmp = false;
				for (LoanFacTmp t : lLoanFacTmp) {
					if (facmNo == t.getFacmNo()) {
						isLoanFacTmp = true;
					}
				}
				if (!isLoanFacTmp) {
					isFeeFacmNo = true;
				}
			}
		}

		return isFeeFacmNo;
	}

	// Load 暫收指定額度清單
	private void loadFacTmpList(int iCustNo, TitaVo titaVo) throws LogicException {
		Slice<LoanFacTmp> slLoanFacTmp = loanFacTmpService.findCustNo(iCustNo, 0, Integer.MAX_VALUE, titaVo);
		if (slLoanFacTmp != null) {
			this.lLoanFacTmp = slLoanFacTmp == null ? null : new ArrayList<LoanFacTmp>(slLoanFacTmp.getContent());
			String x = "";
			for (LoanFacTmp t : lLoanFacTmp) {
				this.tmpFacmNoX += x + parse.IntegerToString(t.getFacmNo(), 3);
				x = ",";
			}
		}
	}

	/* Load UnPaid */
	/**
	 * 
	 * @param iPayIntDate 還款應繳日
	 * @param iCustNo     戶號
	 * @param iFacmNo     額度
	 * @param iBormNo     撥款
	 * @param iRepayType  還款類別
	 * @param titaVo      tita
	 * @throws LogicException ....
	 */
	public void loadUnPaid(int iPayIntDate, int iCustNo, int iFacmNo, int iBormNo, int iRepayType, TitaVo titaVo)
			throws LogicException {
		this.info("loadUnPaid ... " + " iPayIntDate=" + iPayIntDate + ", iFacmNo=" + iFacmNo + ", iRepayType="
				+ iRepayType);

// 銷帳科目記號ReceivableFlag = 1,2
		// F09 暫付款－火險保費
		// F25 催收款項－火險費用
		// F07 暫付法務費
		// F24 催收款項－法務費用

// 銷帳科目記號ReceivableFlag = 3-未收款
		// TMI 火險保費
		// F29 契變手續費
		// F10 帳管費/手續費
		// F12 聯貸件
		// F27 聯貸管理費
// 銷帳科目記號ReceivableFlag = 4-短繳期金
		// Z10 短期擔保放款 310 短期擔保放款
		// Z20 中期擔保放款 320 中期擔保放款
		// Z30 長期擔保放款 330 長期擔保放款
		// Z40 三十年房貸 340 三十年房貸
		// IC1 短擔息
		// IC2 中擔息
		// IC3 長擔息
		// IC4 三十年房貸息
		// IOP 違約金

//  資料類型 dataKind
		// 1.應收費用+未收費用+短繳期金 <BR>
		// 6.另收欠款(未到期火險費用、費用收取之短繳期金、清償違約金) <BR>

		initRv();
		if (this.isLoadRvList) {
			Slice<AcReceivable> srvList = acReceivableService.acrvFacmNoRange(0, iCustNo, 0, 0, 999, 0,
					Integer.MAX_VALUE, titaVo); // 銷帳記號 0-未銷, 業務科目記號 0: 一般科目
			rvList = srvList == null ? null : srvList.getContent();
			this.isLoadRvList = false;
		}
		if (rvList != null) {
			for (AcReceivable rv : rvList) {
				baTxVo = new BaTxVo();
				baTxVo.setReceivableFlag(rv.getReceivableFlag()); // 1:會計銷帳科目(應收) 2:業務銷帳科目(應收) 3:未收費用 4:短繳期金 5.另收欠款
				baTxVo.setCustNo(rv.getCustNo());
				baTxVo.setFacmNo(rv.getFacmNo());
				baTxVo.setBormNo(0);
				baTxVo.setRvNo(rv.getRvNo());
				baTxVo.setAcctCode(rv.getAcctCode());
				baTxVo.setUnPaidAmt(rv.getRvBal());
				baTxVo.setAcctAmt(BigDecimal.ZERO);
				baTxVo.setRvJsonFields(rv.getJsonFields());
				// 利息提存只列欠繳利息
				if (isAcLoanInt) {
					if ("I".equals(rv.getAcctCode().substring(0, 1)) && iFacmNo == rv.getFacmNo()
							&& parse.IntegerToString(iBormNo, 3).equals(rv.getRvNo().substring(0, 3))) {
						baTxVo.setBormNo(iBormNo);
						baTxVo.setDataKind(1); // 1.應收費用+未收費用+短繳期金
						baTxVo.setRepayType(1); // 01-期款
						baTxVo.setInterest(rv.getRvBal()); // 利息
						baTxVo.setPayIntDate(rv.getOpenAcDate()); // 應繳息日、應繳日
						baTxVo.setDbCr("C");
						this.shortfall = this.shortfall.add(rv.getRvBal());
						this.shortfallInterest = this.shortfallInterest.add(rv.getRvBal());
					}
				} else {
					if ("TAV".equals(rv.getAcctCode())) { // TAV 暫收款－可抵繳
						if (isTmpFacmNo(iRepayType, iFacmNo, rv.getFacmNo())) {
							baTxVo.setDataKind(3); // 3.暫收抵繳
							this.tavAmt = this.tavAmt.add(rv.getRvBal());
							this.excessive = this.excessive.add(rv.getRvBal()); // 累溢收
						} else {
							baTxVo.setDataKind(5); // 5.其他額度暫收可抵繳
							this.excessiveOther = this.excessiveOther.add(rv.getRvBal()); // 累溢收
						}
						baTxVo.setRepayType(0);
						baTxVo.setDbCr("D");
					} else {
						if (isFeeFacmNo(iRepayType, iFacmNo, rv.getFacmNo())) {
							baTxVo.setPayIntDate(rv.getOpenAcDate()); // 應繳息日、應繳日
							baTxVo.setDbCr("C");
							// 短繳期金
							if (rv.getReceivableFlag() == 4) {
								baTxVo.setRepayType(1); // 01-期款
								// 本金
								if ("Z".equals(rv.getAcctCode().substring(0, 1))) {
									baTxVo.setPrincipal(rv.getRvBal());
									baTxVo.setBormNo(parse.stringToInteger(rv.getRvNo().substring(0, 3))); // 短繳期金有撥款序號
									this.shortfall = this.shortfall.add(rv.getRvBal());
									this.shortfallPrincipal = this.shortfallPrincipal.add(rv.getRvBal());
								}
								// 利息
								if ("IC".equals(rv.getAcctCode().substring(0, 2))) {
									baTxVo.setInterest(rv.getRvBal());
									baTxVo.setBormNo(parse.stringToInteger(rv.getRvNo().substring(0, 3))); // 短繳期金有撥款序號
									this.shortfall = this.shortfall.add(rv.getRvBal());
									this.shortfallInterest = this.shortfallInterest.add(rv.getRvBal());
								}
								// 清償違約金(短繳期金，提前償還有即時清償違約金時寫入)
								if ("IOP".equals(rv.getAcctCode())) {
									baTxVo.setCloseBreachAmt(rv.getRvBal());
									this.shortfall = this.shortfall.add(rv.getRvBal());
									this.shortCloseBreach = this.shortCloseBreach.add(rv.getRvBal());
								}
								// 還款類別為費用不含短繳
								if (iRepayType >= 4 && iRepayType <= 90) {
									baTxVo.setDataKind(6); // 6.另收欠款(費用收取之短繳期金)
								} else {
									if (iBormNo == 0 || baTxVo.getBormNo() == 0 || iBormNo == baTxVo.getBormNo()) {
										baTxVo.setDataKind(1); // 1.應收費用+未收費用+短繳期金
									} else {
										baTxVo.setDataKind(6); // 6.另收欠款(費用收取之短繳期金)
									}
								}

							} else {
								switch (rv.getAcctCode()) {
								case "F10": // 帳管費/手續費
									baTxVo.setRepayType(4); // 04-帳管費/手續費
									baTxVo.setFeeAmt(rv.getRvBal());
									if (iRepayType <= 3 && iPayIntDate < rv.getOpenAcDate()) {
										baTxVo.setDataKind(6); // 6.另收欠款(帳管費/手續費)
									} else {
										baTxVo.setDataKind(1); // 1.應收費用+未收費用+短繳期金
										this.acctFee = this.acctFee.add(rv.getRvBal());
									}
									break;

								// 企金費用與期款分別收
								case "F12": // 帳管費企金件
								case "F27": // 聯貸管理費
									baTxVo.setRepayType(4); // 04-帳管費/手續費
									baTxVo.setFeeAmt(rv.getRvBal());
									if (iRepayType == 3 || iRepayType >= 4) {
										baTxVo.setDataKind(1); // 1.應收費用+未收費用+短繳期金
										this.acctFee = this.acctFee.add(rv.getRvBal());
									} else {
										baTxVo.setDataKind(6); // 另收費用
									}
									break;

								case "F29": // F29 契變手續費
									baTxVo.setRepayType(6); // 06-契變手續費
									baTxVo.setFeeAmt(rv.getRvBal());
									baTxVo.setDataKind(1); // 1.應收費用+未收費用+短繳期金
									this.modifyFee = this.modifyFee.add(rv.getRvBal());
									break;

// 全部 : 03-結案, 99-費用全部									
//   fireFee       : 全部
//   unOpenfireFee : 續約保單起日 >=入帳日
//    
// 已到期： 1. 還款類別 = 00-同期款，01-期款、02-部分部分償還、05-火險費、09-其他 
//   fireFee       : 續約保單起日 <= 入帳月
//   unOpenfireFee : 續約保單起日 > 入帳月
								case "TMI": // 未收火險保費
									baTxVo.setRepayType(5); // 05-火險費
									baTxVo.setFeeAmt(rv.getRvBal());

									if (iRepayType == 3 || iRepayType == 99) {
										this.isUnOpenfireFee = 0;
									} else {
										this.isUnOpenfireFee = 1;
									}
									switch (this.isUnOpenfireFee) {
									case 0: // 全部
										baTxVo.setDataKind(1); // 1.應收費用+未收費用+短繳期金
										this.fireFee = this.fireFee.add(rv.getRvBal());
										if (rv.getOpenAcDate() >= iPayIntDate) {
											this.unOpenfireFee = this.unOpenfireFee.add(rv.getRvBal());
										}
										break;
									case 1: // 已到期
										TempVo tTempVo = new TempVo();
										int insuYearMonth = rv.getOpenAcDate() / 100;

										tTempVo = tTempVo.getVo(rv.getJsonFields());
										if (tTempVo.get("InsuYearMonth") != null) {
											insuYearMonth = parse.stringToInteger(tTempVo.get("InsuYearMonth"))
													- 191100;
										}
										if (insuYearMonth <= (iPayIntDate / 100)) {
											baTxVo.setDataKind(1); // 1.應收費用+未收費用+短繳期金
											this.fireFee = this.fireFee.add(rv.getRvBal());
										} else {
											baTxVo.setDataKind(6); // 6.另收欠款(未到期火險費用)
											this.unOpenfireFee = this.unOpenfireFee.add(rv.getRvBal());
										}
										break;
									}
									break;
								case "F09": // F09 暫付款－火險保費
									baTxVo.setRepayType(5); // 05-火險費
									baTxVo.setFeeAmt(rv.getRvBal());
									baTxVo.setDataKind(1); // 1.應收費用+未收費用+短繳期金
									this.fireFee = this.fireFee.add(rv.getRvBal());
									break;
								case "F25": // F25 催收款項－火險費用
									baTxVo.setRepayType(5); // 05-火險費
									baTxVo.setFeeAmt(rv.getRvBal());
									baTxVo.setDataKind(1); // 1.應收費用+未收費用+短繳期金
									this.collFireFee = this.collFireFee.add(rv.getRvBal());
									break;
								case "F07": // F07 暫付法務費
									baTxVo.setRepayType(7); // 07-法務費
									baTxVo.setFeeAmt(rv.getRvBal());
									baTxVo.setDataKind(1); // 1.應收費用+未收費用+短繳期金
									this.lawFee = this.lawFee.add(rv.getRvBal());
									break;
								case "F24": // F24 催收款項－法務費用
									baTxVo.setRepayType(7); // 07-法務費
									baTxVo.setFeeAmt(rv.getRvBal());
									baTxVo.setDataKind(1); // 1.應收費用+未收費用+短繳期金
									this.collLawFee = this.collLawFee.add(rv.getRvBal());
									break;
								case "F30": // F30 呆帳戶法務費墊付
									baTxVo.setRepayType(7); // 07-法務費
									baTxVo.setFeeAmt(rv.getRvBal());
									baTxVo.setDataKind(1); // 1.應收費用+未收費用+短繳期金
									this.lawFee = this.lawFee.add(rv.getRvBal());
									break;
								case "IOP": // IOP 清償違約金(未收費用，清償時寫入)
									baTxVo.setRepayType(10); // 10-清償違約金
									baTxVo.setDataKind(1); // 1.應收費用+未收費用+短繳期金
									baTxVo.setCloseBreachAmt(rv.getRvBal());
									this.shortCloseBreach = this.shortCloseBreach.add(rv.getRvBal());
									break;
								default:
									baTxVo.setDataKind(9);
									baTxVo.setRepayType(0);
								}
							}
						}
					}
				}
				if (baTxVo.getDataKind() > 0) {
					this.baTxList.add(baTxVo);
					if (baTxVo.getDataKind() == 1) {
						this.totalFee = this.totalFee.add(baTxVo.getFeeAmt());
					}
				}
				if (this.totalFee.compareTo(BigDecimal.ZERO) > 0) {
					this.unPayFeeX = "";
					if (this.acctFee.compareTo(BigDecimal.ZERO) > 0) {
						this.unPayFeeX += " 帳管費:" + this.acctFee;
					}
					if (this.fireFee.add(this.collFireFee).compareTo(BigDecimal.ZERO) > 0) {
						this.unPayFeeX += " 火險費:" + df.format(this.fireFee.add(this.collFireFee));
					}
					if (this.modifyFee.compareTo(BigDecimal.ZERO) > 0) {
						this.unPayFeeX += " 契變手續費:" + df.format(this.modifyFee);
					}
					if (this.lawFee.add(this.collLawFee).compareTo(BigDecimal.ZERO) > 0) {
						this.unPayFeeX += " 法務費:" + df.format(this.lawFee.add(this.collLawFee));
					}
				}

			}
		}

		for (BaTxVo baTxVo : this.baTxList) {
			this.info("rv =" + baTxVo.toString());
		}

	}

	/**
	 * 累短收
	 * 
	 * @return ..
	 */
	public BigDecimal getShortfall() {
		return this.shortfall;
	}

	/**
	 * 累短收 - 利息
	 * 
	 * @return ..
	 */
	public BigDecimal getShortfallInterest() {
		return this.shortfallInterest;
	}

	/**
	 * 累短收 - 本金
	 * 
	 * @return ..
	 */
	public BigDecimal getShortfallPrincipal() {
		return this.shortfallPrincipal;
	}

	/**
	 * 累短收 - 清償違約金
	 * 
	 * @return ..
	 */
	public BigDecimal getShortCloseBreach() {
		return this.shortCloseBreach;
	}

	/**
	 * 累溢收
	 * 
	 * @return ..
	 */
	public BigDecimal getExcessive() {
		return this.excessive;
	}

	/**
	 * 其他額度累溢收
	 * 
	 * @return ..
	 */
	public BigDecimal getExcessiveOther() {
		return this.excessiveOther;
	}

	/**
	 * 暫付所得稅
	 * 
	 * @return ..
	 */
	public BigDecimal getTempTax() {
		return this.tempTax;
	}

	/**
	 * 契變手續費 F29
	 * 
	 * @return ..
	 */
	public BigDecimal getModifyFee() {
		return this.modifyFee;
	}

	/**
	 * 帳管費用 F10
	 * 
	 * @return ..
	 */
	public BigDecimal getAcctFee() {
		return this.acctFee;
	}

	/**
	 * 火險費用 TMI F09
	 * 
	 * @return ..
	 */
	public BigDecimal getFireFee() {
		return this.fireFee;
	}

	/**
	 * 未到期火險費用 TMI
	 * 
	 * @return ..
	 */
	public BigDecimal getUnOpenfireFee() {
		return this.unOpenfireFee;
	}

	/**
	 * 催收火險費 F25
	 * 
	 * @return ..
	 */
	public BigDecimal getCollFireFee() {
		return this.collFireFee;
	}

	/**
	 * 法務費用 F07
	 * 
	 * @return ..
	 */
	public BigDecimal getLawFee() {
		return this.lawFee;
	}

	/**
	 * 催收法務費 F24
	 * 
	 * @return ..
	 */
	public BigDecimal getCollLawFee() {
		return this.collLawFee;
	}

	/**
	 * 本金
	 * 
	 * @return ..
	 */
	public BigDecimal getPrincipal() {
		return principal;
	}

	/**
	 * 利息
	 * 
	 * @return ..
	 */
	public BigDecimal getInterest() {
		return interest;
	}

	/**
	 * 延滯息
	 * 
	 * @return ..
	 */
	public BigDecimal getDelayInt() {
		return delayInt;
	}

	/**
	 * 違約金
	 * 
	 * @return ..
	 */
	public BigDecimal getBreachAmt() {
		return breachAmt;
	}

	/**
	 * 清償違約金
	 * 
	 * @return ..
	 */
	public BigDecimal getCloseBreachAmt() {
		return closeBreachAmt;
	}

	/**
	 * 暫收款金額(存入暫收為正、暫收抵繳為負)
	 * 
	 * @return ..
	 */
	public BigDecimal getTempAmt() {
		return tempAmt;
	}

	/**
	 * 還款總金額
	 * 
	 * @return ..
	 */
	public BigDecimal getRepayTotal() {
		return repayTotal;
	}

	/**
	 * 還款應繳日
	 * 
	 * @return ..
	 */
	public int getRepayIntDate() {
		return repayIntDate;
	}

	/**
	 * 額度還款應繳日
	 * 
	 * @return TempVo
	 */
	public TempVo getRepayIntDateByFacmNoVo() {
		return repayIntDateByFacmNoVo;
	}

	/**
	 * 是否回收費用
	 * 
	 * @return Y/N
	 */

	public String getPayFeeFlag() {
		return payFeeFlag;
	}

	/**
	 * 回收費用方式
	 * 
	 * @param payFeeMethod Y/N
	 */
	public void setPayFeeMethod(String payFeeMethod) {
		this.payFeeMethod = payFeeMethod;
	}

	/**
	 * 是否可欠繳
	 * 
	 * @return Y/N
	 */
	public String getUnpaidFlag() {
		return unpaidFlag;
	}

	// 可預收期數
	/**
	 * 可預收期數
	 * 
	 * @return 可預收期數
	 */
	public int getPreRepayTerms() {
		return preRepayTerms;
	}

	/**
	 * 部分還款金額
	 * 
	 * @return ..
	 */
	public BigDecimal getExtraRepay() {
		return extraRepay;
	}

	/**
	 * 是否內含利息
	 * 
	 * @return Y/N
	 */
	public String getIncludeIntFlag() {
		return includeIntFlag;
	}

	/**
	 * 利息是否可欠繳
	 * 
	 * @return Y/N
	 */
	public String getUnpaidIntFlag() {
		return unpaidIntFlag;
	}

	/**
	 * 繳納方式
	 * 
	 * @return 1.減少每期攤還金額 2.縮短應繳期數
	 */
	public String getPayMethod() {
		return payMethod;
	}

	/**
	 * 逾期數
	 * 
	 * @return 逾期數
	 */
	public int getOverTerms() {
		return ovduTerms;
	}

	/**
	 * 逾期天數
	 * 
	 * @return 逾期天數
	 */
	public int getOvduDays() {
		return ovduDays;
	}

	/**
	 * 目前利率
	 * 
	 * @return 目前利率
	 */
	public BigDecimal getFitRate() {
		return fitRate;
	}

	/**
	 * 繳期數
	 * 
	 * @return 繳期數
	 */
	public int getTerms() {
		return terms;
	}

	/**
	 * 短繳(正值)
	 * 
	 * @return BigDecimal
	 */
	public BigDecimal getShortAmt() {
		return shortAmt;
	}

	/**
	 * 溢繳(正值)
	 * 
	 * @return BigDecimal
	 */
	public BigDecimal getOverAmt() {
		return overAmt;
	}

	/**
	 * 溢繳繳額度
	 * 
	 * @return 溢繳繳額度
	 */
	public int getOverRpFacmNo() {
		return overRpFacmNo;
	}

	/**
	 * 戶況
	 * 
	 * @return 戶況
	 */
	public int getFacStatus() {
		return facStatus;
	}

	/**
	 * 處理說明
	 * 
	 * @return TempVo
	 */
	public TempVo getTempVo() {
		return tempVo;
	}

	/**
	 * 上次繳息日
	 * 
	 * @return 上次繳息日
	 */
	public int getPrevPayIntDate() {
		return prevPayIntDate;
	}

	/**
	 * 下次繳息日
	 * 
	 * @return 下次繳息日
	 */
	public int getNextPayIntDate() {
		return nextPayIntDate;
	}

	/**
	 * 應繳本利
	 * 
	 * @return 應繳本利
	 */
	public BigDecimal getUnPayLoan() {
		return unPayLoan;
	}

	/**
	 * 暫收指定額度
	 * 
	 * @return 暫收指定額度
	 */
	public String getTmpFacmNoX() {
		return tmpFacmNoX;
	}

}
