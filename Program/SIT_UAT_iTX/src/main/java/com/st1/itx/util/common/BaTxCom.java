package com.st1.itx.util.common;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Component;

import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.TempVo;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.dataVO.TotaVo;
import com.st1.itx.db.domain.AcReceivable;
import com.st1.itx.db.domain.FacClose;
import com.st1.itx.db.domain.LoanBook;
import com.st1.itx.db.domain.LoanBorMain;
import com.st1.itx.db.service.AcReceivableService;
import com.st1.itx.db.service.FacCloseService;
import com.st1.itx.db.service.LoanBookService;
import com.st1.itx.db.service.LoanBorMainService;
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
	public AcReceivableService acReceivableService;
	@Autowired
	public LoanBorMainService loanBorMainService;
	@Autowired
	public LoanBookService loanBookService;
	@Autowired
	public FacCloseService facCloseService;

	private BaTxVo baTxVo;
	private ArrayList<BaTxVo> baTxList;
	private List<AcReceivable> rvList;
	private BigDecimal txBal = BigDecimal.ZERO; // txBal 回收餘額
	private BigDecimal xxBal = BigDecimal.ZERO; // xxBal 可償還金額
	private BigDecimal tmAmt = BigDecimal.ZERO; // tmAmt 需暫收抵繳金額
	private BigDecimal loanBal = BigDecimal.ZERO; // 還款前本金餘額
	private BigDecimal tavAmt = BigDecimal.ZERO; // tavAmt 可暫收抵繳金額
	private BigDecimal shortAmt = BigDecimal.ZERO; // 短繳(正值)
	private BigDecimal overAmt = BigDecimal.ZERO; // 溢繳(正值)
	private int overRpFacmNo = 0; // 溢短繳額度;
	private int iPayIntDate = 0; // 應繳日;

// isPayAllFee 是否全部回收費用
	// true->回收全部費用，false 回收金額足夠的費用
	// RepayType 還款類別
	// 1.期款 3.結案 => true
	// 2.部分償還 按約定，未約定 => true
	// 4.帳管費 5.火險費 6.契變手續費 7.法務費 => false
	private boolean isPayAllFee = false;

	// 未到期火險費用條件 0.全列已到期、1.續約保單起日 > 入帳月、2.續約保單起日 >=入帳日,
	private int isUnOpenfireFee = 0;

// isBatchRepay 是否為整批入帳
	private boolean isBatchRepay = false;

// isAcLoanInt 是否利息提存
	private boolean isAcLoanInt = false;

// isEmptyLoanBaTxVo 是否放未計息餘額
	private boolean isEmptyLoanBaTxVo = false;

//  批次入帳可預收期數
	private int preRepayTermsBatch = 0;

// 總數資料
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
	private BigDecimal openInterest = BigDecimal.ZERO; // 掛帳利息(逆向貸款) ICR
	private BigDecimal totalFee = BigDecimal.ZERO; // 費用總額
	private BigDecimal principal = BigDecimal.ZERO; // 本金
	private BigDecimal interest = BigDecimal.ZERO; // 利息
	private BigDecimal delayInt = BigDecimal.ZERO; // 延滯息
	private BigDecimal breachAmt = BigDecimal.ZERO; // 違約金
	private BigDecimal closeBreachAmt = BigDecimal.ZERO; // 清償違約金
	private BigDecimal tempAmt = BigDecimal.ZERO; // 暫收款金額(存入暫收為正、暫收抵繳為負)
	private BigDecimal repayTotal = BigDecimal.ZERO; // 還款總金額
	private BigDecimal shortAmtLimit = BigDecimal.ZERO;// 短繳限額
	private int rateEffectDate = 0; // 目前利率生效日
	private BigDecimal fitRate = BigDecimal.ZERO; // 目前利率
	private int repayIntDate = 0; // 還款應繳日
	private TempVo repayIntDateByFacmNoVo = new TempVo(); // 額度還款應繳日
	private int ovduTerms = 0; // 逾期數
	private int ovduDays = 0; // 逾期天數
	private int terms = 0; // 繳期數

	private BigDecimal extraRepay = BigDecimal.ZERO; // 部分還款金額
	private String includeIntFlag = "";// 是否內含利息
	private String unpaidIntFlag = "";// 利息是否可欠繳
	private String payFeeFlag = "";// 是否回收費用
	private String payMethod = "";// 繳納方式 1.減少每期攤還金額 2.縮短應繳期數

//  initialize
	private void init() {

		this.isBatchRepay = false; // isBatchRepay 是否為整批入帳
		this.preRepayTermsBatch = 0;// 批次入帳可預收期數

		this.baTxVo = new BaTxVo();
		this.baTxList = new ArrayList<BaTxVo>();
		this.rvList = new ArrayList<AcReceivable>();
		this.txBal = BigDecimal.ZERO; // txBal 回收餘額
		this.xxBal = BigDecimal.ZERO; // xxBal 可償還金額
		this.tmAmt = BigDecimal.ZERO; // tmAmt 需暫收抵繳金額
		this.loanBal = BigDecimal.ZERO; // 還款前本金餘額
		this.tavAmt = BigDecimal.ZERO; // tavAmt 可暫收抵繳金額
		this.shortAmt = BigDecimal.ZERO; // 短繳(正值)
		this.overAmt = BigDecimal.ZERO; // 溢繳(正值)
		this.overRpFacmNo = 0; // 溢短繳額度;

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
		this.openInterest = BigDecimal.ZERO; // 掛帳利息(逆向貸款) ICR (未用)
		this.totalFee = BigDecimal.ZERO; // 費用總額

		// 本金、利息
		this.principal = BigDecimal.ZERO; // 本金
		this.interest = BigDecimal.ZERO; // 利息
		this.delayInt = BigDecimal.ZERO; // 延滯息
		this.breachAmt = BigDecimal.ZERO; // 違約金
		this.closeBreachAmt = BigDecimal.ZERO; // 清償違約金
		this.shortAmtLimit = BigDecimal.ZERO;// 短繳限額計算

		// 還款金額、日期
		this.tempAmt = BigDecimal.ZERO; // 暫收款金額(存入暫收為正、暫收抵繳為負)
		this.repayTotal = BigDecimal.ZERO; // 總還款金額
		this.repayIntDate = 0; // repayIntDate 還款應繳日
		this.repayIntDateByFacmNoVo = new TempVo(); // 額度還款應繳日

		this.ovduTerms = 0; // 逾期數
		this.ovduDays = 0; // 逾期天數
		this.terms = 0; // 繳期數
		this.rateEffectDate = 0; // 目前利率生效日
		this.fitRate = BigDecimal.ZERO; // 目前利率

		// 部分還款
		this.extraRepay = BigDecimal.ZERO; // 部分還款金額
		this.includeIntFlag = "N";// 是否內含利息
		this.unpaidIntFlag = "Y";// 利息是否可欠繳
		this.payFeeFlag = "Y";// 是否回收費用
		this.payMethod = "0";// 繳納方式 1.減少每期攤還金額 2.縮短應繳期數
	}

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		return null;
	}

	/**
	 * 應繳試算
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
		this.info("BaTxCom settingUnPaid ...");
		this.info("BaTxCom settingUnPaid EntryDate  入帳日=" + iEntryDate);
		this.info("BaTxCom settingUnPaid PayIntDate 應繳日=" + this.iPayIntDate);
		this.info("BaTxCom settingUnPaid 戶號=" + iCustNo + "-" + iFacmNo + "-" + iBormNo);
		this.info("BaTxCom settingUnPaid RepayType 還款類別=" + iRepayType);
		this.info("BaTxCom settingUnPaid TxAmt 回收金額=" + iTxAmt);
		init();
		// STEP 1: 設定預設值
		// isPayAllFee 費用是否全部回收->
		if (iRepayType <= 3 || iRepayType == 99) {
			this.isPayAllFee = true; // 全部
		} else {
			this.isPayAllFee = false; // 部分
		}
		// isUnOpenfireFee 未到期火險費用條件
// 還款類別                                                     未到期火險費用條件           火險費輸出欄說明
// 03-結案, 99-費用全部                                0.全列已到期                      fireFee(全部)，unOpenfireFee(續約保單起日>=入帳日)，fireFee內含unOpenfireFee
// 01-期款、02-部分部分償還 、05-火險費     1.續約保單起日 > 入帳月    fireFee(續約保單起日 <= 入帳月)、 unOpenfireFee(續約保單起日 > 入帳月)
// 00-費用全部(已到期) 、else		    2.續約保單起日 >=入帳日    fireFee(續約保單起日  < 入帳日)、 unOpenfireFee(續約保單起日 >=入帳日)

		if (iRepayType == 03 || iRepayType == 99) {
			isUnOpenfireFee = 0;
		} else if (iRepayType == 01 || iRepayType == 02 || iRepayType == 05) {
			isUnOpenfireFee = 1;
		} else {
			isUnOpenfireFee = 2;
		}

		// isEmptyLoanBaTxVo 是否放未計息餘額
		this.isEmptyLoanBaTxVo = false;

		// 還款類別:02 與約定還本日期相同
		if (iRepayType == 2) {
			this.extraRepay = iTxAmt; // 部分還款金額
			Slice<LoanBook> slLoanBook = loanBookService.bookCustNoRange(iCustNo, iCustNo, iFacmNo,
					iFacmNo > 0 ? iFacmNo : 999, iBormNo, iBormNo > 0 ? iBormNo : 900, iEntryDate, this.index,
					Integer.MAX_VALUE, titaVo);
			if (slLoanBook != null) {
				for (LoanBook tLoanBook : slLoanBook.getContent()) {
					if (tLoanBook.getStatus() == 0) {
						if (iTxAmt.compareTo(BigDecimal.ZERO) == 0 || iTxAmt.compareTo(tLoanBook.getBookAmt()) >= 0) {
							iFacmNo = tLoanBook.getFacmNo(); // 還款額度
							iBormNo = tLoanBook.getBormNo(); // 撥款序號
							if (iTxAmt.compareTo(BigDecimal.ZERO) == 0) {
								this.extraRepay = tLoanBook.getBookAmt(); // 部分還款金額
							}
							this.includeIntFlag = tLoanBook.getIncludeIntFlag(); // 是否內含利息
							this.unpaidIntFlag = tLoanBook.getUnpaidIntFlag();// 利息是否可欠繳
							if ("Y".equals(tLoanBook.getIncludeFeeFlag())) {
								this.extraRepay = this.extraRepay.subtract(this.totalFee); // 部分還款金額
								this.payFeeFlag = "Y";
							} else {
								if (this.tavAmt.compareTo(this.totalFee) >= 0) {
									this.payFeeFlag = "Y";
								} else {
									this.payFeeFlag = "N";
								}
							}
							this.payMethod = tLoanBook.getPayMethod();// 繳納方式 1.減少每期攤還金額 2.縮短應繳期數
							break;
						}
					}
				}
			}
		}

		// STEP 2: Load UnPaid 1.應收費用+未收費用+短繳期金 3.暫收抵繳 6.另收欠款
		loadUnPaid(iEntryDate, iCustNo, iFacmNo, 0, iRepayType, titaVo);

		// STEP 3: 計算放款本息
		if (iRepayType >= 1 && iRepayType <= 3) {
			repayLoan(iEntryDate, this.iPayIntDate, iCustNo, iFacmNo, iBormNo, iRepayType, iTxAmt, 0, titaVo);
		}

		// STEP 4: 設定總金額
		this.txBal = iTxAmt; // 回收金額
		this.xxBal = this.txBal; // 可償還餘額
		this.tmAmt = BigDecimal.ZERO; // this.tmAmt 暫收抵繳金額
		// 加可抵繳至可償還餘額
		this.xxBal = this.xxBal.add(this.tavAmt);

		// STEP 5: 設定還款順序
		// 1.還款類別(費用)相同 > 2.應收費用 > 3:未收費用 > 4:短繳期金 > 5:應繳本利 > 6:另收欠款
		settlePriority(iEntryDate, iRepayType); // 還款順序

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
	 * 應繳試算 by 應繳日
	 * 
	 * @param iEntryDate  入帳日
	 * @param iPayintDate 應繳日
	 * @param iCustNo     戶號
	 * @param iFacmNo     額度
	 * @param iBormNo     撥款
	 * @param iRepayType  還款類別
	 * @param iTxAmt      入帳金額
	 * @param titaVo      TitaVo
	 * @return ArrayList of BaTxVo
	 * @throws LogicException ...
	 */
	public ArrayList<BaTxVo> settingPayintDate(int iEntryDate, int iPayintDate, int iCustNo, int iFacmNo, int iBormNo,
			int iRepayType, BigDecimal iTxAmt, TitaVo titaVo) throws LogicException {
		this.iPayIntDate = iPayintDate; // 應繳日;
		this.baTxList = settingUnPaid(iEntryDate, iCustNo, iFacmNo, iBormNo, iRepayType, iTxAmt, titaVo);
		return this.baTxList;
	}

	/**
	 * 還款試算 call by 整批入帳
	 * 
	 * @param iEntryDate  入帳日
	 * @param iPayintDate 應繳日
	 * @param iCustNo     戶號
	 * @param iFacmNo     額度
	 * @param iBormNo     撥款
	 * @param iRepayCode  還款來源
	 * @param iRepayType  還款類別
	 * @param iTxAmt      入帳金額
	 * @param titaVo      TitaVo
	 * @return ArrayList of BaTxVo
	 * @throws LogicException ...
	 */
	public ArrayList<BaTxVo> settleUnPaid(int iEntryDate, int iPayintDate, int iCustNo, int iFacmNo, int iBormNo,
			int iRepayCode, int iRepayType, BigDecimal iTxAmt, TitaVo titaVo) throws LogicException {
		this.info("BaTxCom settleUnPaid ...");
		this.info("BaTxCom settleUnPaid EntryDate  入帳日=" + iEntryDate);
		this.info("BaTxCom settleUnPaid PayintDate 應繳日=" + iPayintDate);
		this.info("BaTxCom settleUnPaid 戶號=" + iCustNo + "-" + iFacmNo + "-" + iBormNo);
		this.info("BaTxCom settleUnPaid RepayType 還款類別=" + iRepayType);
		this.info("BaTxCom settleUnPaid TxAmt 回收金額=" + iTxAmt);
		init();
		// STEP 1: 設定預設值

		// 費用是否全部回收
		if (iRepayType <= 3 || iRepayType == 99)
			this.isPayAllFee = true; // 全部
		else
			this.isPayAllFee = false; // 部分

		// 未到期火險費用條件同settingUnPaid
		if (iRepayType == 03 || iRepayType == 99) {
			isUnOpenfireFee = 0;
		} else if (iRepayType == 01 || iRepayType == 02 || iRepayType == 05) {
			isUnOpenfireFee = 1;
		} else {
			isUnOpenfireFee = 2;
		}

		// isBatchRepay 是否為整批入帳
		this.isBatchRepay = true;

		// 批次可預收期數，限匯款轉帳
		if (iRepayCode == 1) {
			this.preRepayTermsBatch = this.txBuffer.getSystemParas().getPreRepayTermsBatch();
		}

		// isEmptyLoanBaTxVo 是否放未計息餘額
		this.isEmptyLoanBaTxVo = true;

		// STEP 2: Load UnPaid 1.應收費用+未收費用+短繳期金 3.暫收抵繳 6.另收欠款
		loadUnPaid(iEntryDate, iCustNo, iFacmNo, 0, iRepayType, titaVo);

		// STEP 3: 計算放款本息

		// 計算部分償還金額
		// 未約定 => min(交易金額, 可部分償還金額)
		// 有約定 => 約定還款金額
		// 還款類別:02 大於等於約定還本金額
		if (iRepayType == 2) {
			this.extraRepay = iTxAmt; // 部分還款金額
			Slice<LoanBook> slLoanBook = loanBookService.bookCustNoRange(iCustNo, iCustNo, iFacmNo,
					iFacmNo > 0 ? iFacmNo : 999, iBormNo, iBormNo > 0 ? iBormNo : 900, iEntryDate, this.index,
					Integer.MAX_VALUE, titaVo);
			if (slLoanBook != null) {
				for (LoanBook tLoanBook : slLoanBook.getContent()) {
					if (tLoanBook.getStatus() == 0) {
						if (iTxAmt.compareTo(tLoanBook.getBookAmt()) >= 0) {
							iFacmNo = tLoanBook.getFacmNo(); // 還款額度
							iBormNo = tLoanBook.getBormNo(); // 撥款序號
							this.includeIntFlag = tLoanBook.getIncludeIntFlag(); // 是否內含利息
							this.unpaidIntFlag = tLoanBook.getUnpaidIntFlag();// 利息是否可欠繳
							this.payMethod = tLoanBook.getPayMethod();// 繳納方式 1.減少每期攤還金額 2.縮短應繳期數
							// 是否內含費用
							if ("Y".equals(tLoanBook.getIncludeFeeFlag())) {
								this.extraRepay = this.extraRepay.subtract(this.totalFee); // 部分還款金額
								this.payFeeFlag = "Y";
							} else {
								this.payFeeFlag = "N";
							}
							break;
						}
					}
				}
			}
		}

		// 放款本息計算
		if (iRepayType >= 1 && iRepayType <= 3) {
			repayLoan(iEntryDate, iPayintDate, iCustNo, iFacmNo, iBormNo, iRepayType, iTxAmt, 0, titaVo); // Terms = 0
		}

		// 提前償還計算清償違約金
		if (iRepayType == 2 || iRepayType == 3) {
			getCloseBreachAmt(iEntryDate, iCustNo, iFacmNo, iRepayType, iTxAmt, titaVo);
		}

		// STEP 4: 設定總金額
		this.txBal = iTxAmt; // 回收金額
		this.xxBal = this.txBal; // 可償還餘額
		this.tmAmt = BigDecimal.ZERO; // this.tmAmt 暫收抵繳金額

		// 加可抵繳至可償還餘額
		this.xxBal = this.xxBal.add(this.tavAmt);

		// STEP 5: 設定還款順序
		// 1.還款類別(費用)相同 > 2.應收費用 > 3:未收費用 > 4:短繳期金 > 5:已到期應繳本利 > 6:另收欠款
		settlePriority(iEntryDate, iRepayType); //

		// STEP 6 : 計算作帳金額
		// 回收費用
		if ("Y".equals(this.payFeeFlag)) {
			settleAcctAmt(1);
			settleAcctAmt(2);
			settleAcctAmt(3);
		}
		// 全部回收時 4:短繳期金
		if (iRepayType <= 3 || iRepayType == 99) {
			settleAcctAmt(4);
		}

		// 放款回收時 5:應繳本利
		// 回收金額時排序,依應繳日由小到大、計息順序(利率由大到小)、額度由小到大
		if (iRepayType == 1) {
			for (BaTxVo ba : this.baTxList) {
				this.info("sort B: " + ba.toString());
			}
			Collections.sort(baTxList); // 排序優先度(由小到大) payIntDate 應繳日
			for (BaTxVo ba : this.baTxList) {
				this.info("sort A: " + ba.toString());
			}
			settleByPayintDate(iRepayCode);
			// 無可回收算全部已到期本息
			if (this.repayIntDate == 0) {
				settleByTbsdy();
			}
		}
		if (iRepayType == 2 || iRepayType == 3) {
			settleAcctAmt(5);
		}

		// 結案收 6:另收欠款
		if (iRepayType == 3) {
			settleAcctAmt(6);
		}

		// STEP 7 : 計算作帳金額 3.暫收抵繳
		settleTmpAcctAmt();

		/* STEP 8 : 計算溢(C)短(D)繳 */
		settleOverAmt(iCustNo, iFacmNo, titaVo);

		// END
		if (this.baTxList != null) {
			for (BaTxVo ba : this.baTxList) {
				this.info("settleUnPaid " + ba.toString());
			}
		}
		return this.baTxList;
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

		// 未到期火險費用條件 ==> 1.續約保單起日 > 入帳月
		isUnOpenfireFee = 1;

		// isAcLoanInt 是否利息提存- > 是
		this.isAcLoanInt = true;

		// 短繳利息
		loadUnPaid(iEntryDate, iCustNo, iFacmNo, iBormNo, 1, titaVo);

		// 計算利息
		repayLoan(iEntryDate, iPayintDate, iCustNo, iFacmNo, iBormNo, 90, BigDecimal.ZERO, 0, titaVo); // Terms = 0

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
	 * @param titaVo     TitaVo
	 * @return ArrayList of BaTxVo
	 * @throws LogicException ...
	 */
	public ArrayList<BaTxVo> termsPay(int iEntryDate, int iCustNo, int iFacmNo, int iBormNo, int iTerms, TitaVo titaVo)
			throws LogicException {
		this.info("BaTxCom termsPay ..." + iEntryDate + "/ " + iCustNo + "-" + iFacmNo + "-" + iBormNo + "/ " + iTerms);
		init();

		// 費用是否全部回收
		this.isPayAllFee = true; // 全部

		// 未到期火險費用條件 1：fireFee(續約保單起日 <= 入帳月)、 unOpenfireFee(續約保單起日 > 入帳月)
		isUnOpenfireFee = 1;

		// isEmptyLoanBaTxVo 是否放未計息餘額
		this.isEmptyLoanBaTxVo = false;

		// STEP 3: Load UnPaid 1.應收費用+未收費用+短繳期金 3.暫收抵繳 6.另收欠款
		loadUnPaid(iEntryDate, iCustNo, iFacmNo, 0, 1, titaVo);

		// 計算放款本息
		repayLoan(iEntryDate, 0, iCustNo, iFacmNo, iBormNo, 01, BigDecimal.ZERO, iTerms, titaVo);

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

		// 計算利息
		repayLoan(iEntryDate, 0, iCustNo, iFacmNo, iBormNo, 80, BigDecimal.ZERO, 0, titaVo); // Terms = 0

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
		this.isEmptyLoanBaTxVo = false;

		//
		repayLoan(iEntryDate, 0, iCustNo, iFacmNo, iBormNo, 01, BigDecimal.ZERO, 0, titaVo);

		// END
		return this.baTxList;

	}

	/* 本金利息(按應繳日加總至額度) */
	public ArrayList<BaTxVo> addByPayintDate(ArrayList<BaTxVo> iBatxList, TitaVo titaVo) throws LogicException {
		this.baTxList = new ArrayList<BaTxVo>();
		// 按額度、資料類型、還款類別、應繳日，小至大排序
		Collections.sort(iBatxList, new Comparator<BaTxVo>() {
			@Override
			public int compare(BaTxVo c1, BaTxVo c2) {
				if (c1.getFacmNo() != c2.getFacmNo()) {
					return c1.getFacmNo() - c1.getFacmNo();
				}
				if (c1.getDataKind() != c2.getDataKind()) {
					return c1.getDataKind() - c1.getDataKind();
				}
				if (c1.getRepayType() != c2.getRepayType()) {
					return c1.getDataKind() - c1.getDataKind();
				}
				if (c1.getPayIntDate() != c2.getPayIntDate()) {
					return c1.getPayIntDate() - c1.getPayIntDate();
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
				baTxVo.setPrincipal(ba.getPrincipal()); // 本金
				baTxVo.setInterest(ba.getInterest()); // // 利息
				baTxVo.setDelayInt(ba.getDelayInt()); // 延滯息
				baTxVo.setBreachAmt(ba.getBreachAmt()); // 違約金
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
				baTxVo.setLoanBalPaid(baTxVo.getLoanBalPaid().add(ba.getLoanBalPaid())); // 還款後餘額
				baTxVo.setUnPaidAmt(baTxVo.getPrincipal().add(ba.getUnPaidAmt()));
				baTxVo.setAcctAmt(baTxVo.getPrincipal().add(ba.getAcctAmt())); // 出帳金額
				baTxVo.setLoanBal(baTxVo.getPrincipal().add(ba.getLoanBal())); // 放款餘額(還款前、只放第一期)
				baTxVo.setExtraAmt(baTxVo.getPrincipal().add(ba.getExtraAmt())); // 提前償還金額
				if (ba.getIntStartDate() < baTxVo.getIntStartDate()) {
					baTxVo.setIntStartDate(ba.getIntStartDate()); // 計息起日
				}
				if (ba.getIntEndDate() > baTxVo.getIntEndDate()) {
					baTxVo.setIntEndDate(ba.getIntEndDate()); // 計息止日
				}
				baTxVo.setAmount(baTxVo.getPrincipal().add(ba.getAmount())); // 計息本金
				if (ba.getCloseFg() < baTxVo.getCloseFg()) {
					baTxVo.setCloseFg(ba.getCloseFg());
				}
			}
		}
		// END
		return this.baTxList;
	}

	/* 計算放款還款金額 */
	private void repayLoan(int iEntryDate, int iPayIntDate, int iCustNo, int iFacmNo, int iBormNo, int iRepayType,
			BigDecimal iTxAmt, int iTerms, TitaVo titaVo) throws LogicException {
		this.info("BaTxCom repayLoan ...");
		this.info("   EntryDate = " + iEntryDate);
		this.info("   IntPayDate = " + iPayIntDate);
		this.info("   CustNo    = " + iCustNo);
		this.info("   FacmNo    = " + iFacmNo);
		this.info("   BormNo    = " + iBormNo);
		this.info("   RepayType = " + iRepayType);
		this.info("   TxAmt     = " + iTxAmt);
		this.info("   Terms     = " + iTerms);
		this.info("   extraRepay = " + this.extraRepay);
		loanSetRepayIntCom.setTxBuffer(this.getTxBuffer());
		int wkFacmNoStart = 1;
		int wkFacmNoEnd = 999;
		int wkBormNoStart = 1;
		int wkBormNoEnd = 900;
		int wkTerms = 0;
		int nextIntDate = 0;
		int wkPayIntDate = iPayIntDate > 0 ? iPayIntDate : iEntryDate; // 試算應繳日
		BigDecimal wkExtraRepayRemaind = this.extraRepay;
		ArrayList<CalcRepayIntVo> lCalcRepayIntVo;

		if (iFacmNo > 0) {
			wkFacmNoStart = iFacmNo;
			wkFacmNoEnd = iFacmNo;
		}

		if (iBormNo > 0) {
			wkBormNoStart = iBormNo;
			wkBormNoEnd = iBormNo;
		}
		Slice<LoanBorMain> slLoanBorMain = loanBorMainService.bormCustNoEq(iCustNo, wkFacmNoStart, wkFacmNoEnd,
				wkBormNoStart, wkBormNoEnd, this.index, Integer.MAX_VALUE, titaVo);
		List<LoanBorMain> lLoanBorMain = slLoanBorMain == null ? null
				: new ArrayList<LoanBorMain>(slLoanBorMain.getContent());
		if (lLoanBorMain == null || lLoanBorMain.size() == 0) {
			throw new LogicException(titaVo, "E0001", "戶號有誤"); // 查詢資料不存在
		}
		Collections.sort(lLoanBorMain, new Comparator<LoanBorMain>() {
			@Override
			public int compare(LoanBorMain c1, LoanBorMain c2) {
				// status
				if (c1.getStatus() != c2.getStatus()) {
					return c1.getStatus() - c2.getStatus();
				}
				// 回收時排序,依應繳日順序由小到大、利率順序由大到小、額度由小到大
				if (iRepayType == 1) {
					if (c1.getNextPayIntDate() != c2.getNextPayIntDate()) {
						return c1.getNextPayIntDate() - c2.getNextPayIntDate();
					}
					if (c1.getStoreRate().compareTo(c2.getStoreRate()) != 0) {
						return (c1.getStoreRate().compareTo(c2.getStoreRate()) > 0 ? -1 : 1);
					}
				}
				// 部分償還時排序,依利率順序由大到小
//					利率高至低>用途別>由額度編號大至小
//					用途別為9->1->3->4->5->6->2
//					欄位代碼       欄位說明     
//					1            週轉金    
//					2            購置不動產
//					3            營業用資產
//					4            固定資產  
//					5            企業投資  
//					6            購置動產
//					9            其他					
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
				}
				if (c1.getFacmNo() != c2.getFacmNo()) {
					return c2.getFacmNo() - c1.getFacmNo();
				}
				if (c1.getBormNo() != c2.getBormNo()) {
					return c2.getBormNo() - c1.getBormNo();
				}
				return 0;
			}
		});
		for (LoanBorMain ln : lLoanBorMain) {
			if (ln.getStatus() != 0) {
				continue;
			}
			this.info("order2 = " + ln.getLoanBorMainId());
			this.loanBal = ln.getLoanBal(); // 還款前本金餘額
			if (ln.getNextPayIntDate() < nextIntDate || nextIntDate == 0) {
				nextIntDate = ln.getNextPayIntDate();
			}
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
					// isBatchRepay 是否為整批入帳
					if (this.isBatchRepay) {
						// 可回收期數 = 可回收期數 + 批次預收期數
						wkRepayTermNo = loanCom.getTermNo(
								this.txBuffer.getTxCom().getTbsdy() >= ln.getMaturityDate() ? 1 : 2, ln.getFreqBase(),
								ln.getPayIntFreq(), ln.getSpecificDate(), ln.getSpecificDd(),
								this.txBuffer.getTxCom().getTbsdy()) + this.preRepayTermsBatch;
					} else {
						// 可回收期數 = 計算至入帳日/應繳日的應繳期數
						wkRepayTermNo = loanCom.getTermNo(wkPayIntDate >= ln.getMaturityDate() ? 1 : 2,
								ln.getFreqBase(), ln.getPayIntFreq(), ln.getSpecificDate(), ln.getSpecificDd(),
								wkPayIntDate);

					}
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
				if (ln.getNextPayIntDate() <= this.txBuffer.getTxCom().getTbsdy()) {
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

			case 80: // 計算利利率調整後次月份應繳期金
				int wkPrevTermNo = 0;
				int wkRepayTermNo = 0;
				// 計算至上次繳息日之期數
				if (ln.getPrevPayIntDate() > ln.getDrawdownDate()) {
					wkPrevTermNo = loanCom.getTermNo(2, ln.getFreqBase(), ln.getPayIntFreq(), ln.getSpecificDate(),
							ln.getSpecificDd(), ln.getPrevPayIntDate());
				}
				// 可回收期數 = 計算至入帳日/應繳日的當期期數
				wkRepayTermNo = loanCom.getTermNo(1, ln.getFreqBase(), ln.getPayIntFreq(), ln.getSpecificDate(),
						ln.getSpecificDd(), wkPayIntDate);
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
				for (int i = 1; i <= wkTerms; i++) {
					if (i == wkTerms) {
						loanCalcRepayIntCom.setTerms(0); // 本次計息期數
						loanCalcRepayIntCom.setIntEndDate(iPayIntDate); // 計息止日
					}
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
					if (loanCalcRepayIntCom.getLoanBal().equals(BigDecimal.ZERO)) {
						break;
					}
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
		if (nextIntDate > 0 && nextIntDate < iEntryDate)

		{
			dDateUtil.init();
			dDateUtil.setDate_1(nextIntDate);
			dDateUtil.setDate_2(iEntryDate);
			dDateUtil.dateDiff();
			this.ovduTerms = dDateUtil.getMons();
			this.ovduDays = dDateUtil.getDays();
		}

		if (this.baTxList != null && this.baTxList.size() > 0)

		{
			this.baTxList.sort((c1, c2) -> {
				return c1.compareTo(c2);
			});
		}
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
		// 結案時還款本金已含短繳本金，回收金額須扣除
		BigDecimal wkPrincipal = loanCalcRepayIntCom.getPrincipal();
		if (loanCalcRepayIntCom.getLoanBal().compareTo(BigDecimal.ZERO) == 0) {
			for (BaTxVo ba : baTxList) {
				if ("Z".equals(ba.getAcctCode().substring(0, 1)) && ln.getFacmNo() == ba.getFacmNo()
						&& ln.getBormNo() == ba.getBormNo()) {
					this.info("短繳本金 =" + ba.getUnPaidAmt() + ",還款本金= " + wkPrincipal);
					wkPrincipal = wkPrincipal.subtract(ba.getUnPaidAmt());
				}
			}
		}
		baTxVo.setPrincipal(wkPrincipal); // 本金
		baTxVo.setInterest(loanCalcRepayIntCom.getInterest()); // // 利息
		baTxVo.setDelayInt(loanCalcRepayIntCom.getDelayInt()); // 延滯息
		baTxVo.setBreachAmt(loanCalcRepayIntCom.getBreachAmt()); // 違約金
		baTxVo.setLoanBalPaid(loanCalcRepayIntCom.getLoanBal()); // 還款後餘額
		this.principal = this.principal.add(wkPrincipal);
		this.interest = this.interest.add(loanCalcRepayIntCom.getInterest());
		this.delayInt = this.delayInt.add(loanCalcRepayIntCom.getDelayInt());
		this.breachAmt = this.breachAmt.add(loanCalcRepayIntCom.getBreachAmt());

		baTxVo.setUnPaidAmt(
				baTxVo.getPrincipal().add(baTxVo.getInterest()).add(baTxVo.getDelayInt()).add(baTxVo.getBreachAmt())); // 未收金額
		baTxVo.setDbCr("C"); // 借貸別
		baTxVo.setAcctAmt(BigDecimal.ZERO); // 出帳金額
		baTxVo.setLoanBal(this.loanBal); // 放款餘額(還款前、只放第一期)
		this.loanBal = BigDecimal.ZERO;
		baTxVo.setExtraAmt(loanCalcRepayIntCom.getExtraAmt()); // 提前償還金額
		// 結案
		baTxVo.setCloseFg(0);
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
				if (ca.getLoanBal().compareTo(BigDecimal.ZERO) == 0) {
					if (ca.getEndDate() >= ln.getMaturityDate()) {
						baTxVo.setCloseFg(1); // 1.正常結案
					} else {
						baTxVo.setCloseFg(2); // 2.提前結案
					}
				}
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
		}

		this.baTxList.add(baTxVo);
	}

	private void emptyLoanBaTxVo(int iEntryDate, int iRepayType, int iCustNo, LoanBorMain ln) {
		this.info("BaTxCom EmptyLoanBaTxVo ...");
		baTxVo = new BaTxVo();
		baTxVo.setDataKind(2); // 2.本金利息
		baTxVo.setRepayType(iRepayType); // 還款類別
		baTxVo.setReceivableFlag(0); // 銷帳科目記號 0:非銷帳科目
		baTxVo.setCustNo(iCustNo); // 借款人戶號
		baTxVo.setFacmNo(ln.getFacmNo()); // 額度編號
		baTxVo.setBormNo(ln.getBormNo()); // 撥款序號
		baTxVo.setRvNo(" "); // 銷帳編號
		baTxVo.setAcctCode(loanCalcRepayIntCom.getAcctCode()); // 業務科目
		baTxVo.setLoanBalPaid(this.loanBal); // 還款後餘額
		baTxVo.setLoanBal(this.loanBal); // 放款餘額(還款前、只放第一期)
		this.loanBal = BigDecimal.ZERO;
		this.baTxList.add(baTxVo);
	}

	// 計算清償違約金
	private void getCloseBreachAmt(int iEntryDate, int iCustNo, int iFacmNo, int iRepayType, BigDecimal iTxAmt,
			TitaVo titaVo) throws LogicException {
		this.info("getCloseBreachAmt...");
		String collectFlag = "";
		Slice<FacClose> facCloseList = facCloseService.findCustNo(iCustNo, this.index, Integer.MAX_VALUE, titaVo); // 清償作業檔
		// 還清金額需相同
		if (facCloseList != null) {
			for (FacClose tFacClose : facCloseList.getContent()) {
				if (tFacClose.getCloseDate() == 0 && tFacClose.getCloseAmt().equals(iTxAmt)
						&& tFacClose.getFacmNo() == iFacmNo) {
					collectFlag = tFacClose.getCollectFlag();
				}
			}
		}

		loanCloseBreachCom.setTxBuffer(this.txBuffer);
		ArrayList<LoanCloseBreachVo> iListCloseBreach = new ArrayList<LoanCloseBreachVo>();
		ArrayList<LoanCloseBreachVo> oListCloseBreach = new ArrayList<LoanCloseBreachVo>();
		for (BaTxVo baTxVo : this.baTxList) {
			if (baTxVo.getDataKind() == 2 && baTxVo.getExtraAmt().compareTo(BigDecimal.ZERO) > 0) {
				LoanCloseBreachVo v = new LoanCloseBreachVo();
				v.setCustNo(baTxVo.getCustNo());
				v.setFacmNo(baTxVo.getFacmNo());
				v.setBormNo(baTxVo.getBormNo());
				v.setExtraRepay(baTxVo.getExtraAmt());
				v.setEndDate(iEntryDate);
				iListCloseBreach.add(v);
			}
			if ("Y".equals(collectFlag)) {
				oListCloseBreach = loanCloseBreachCom.getCloseBreachAmtAll(iCustNo, iFacmNo, 0, iListCloseBreach,
						titaVo);
			} else {
				oListCloseBreach = loanCloseBreachCom.getCloseBreachAmtPaid(iCustNo, iFacmNo, 0, iListCloseBreach,
						titaVo);
			}

			if (oListCloseBreach != null && oListCloseBreach.size() > 0) {
				for (LoanCloseBreachVo v : oListCloseBreach) {
					this.closeBreachAmt = this.closeBreachAmt.add(v.getCloseBreachAmt());
				}
			}
		}
		//
		if (this.closeBreachAmt.compareTo(BigDecimal.ZERO) > 0) {
			baTxVo.setDataKind(6); // 6.另收欠款
			baTxVo.setRepayType(9); // 09-其他(清償違約金)
			baTxVo.setReceivableFlag(0); // 銷帳科目記號 0:非銷帳科目
			baTxVo.setCustNo(iCustNo); // 借款人戶號
			baTxVo.setFacmNo(iFacmNo); // 額度編號
			baTxVo.setBormNo(0); // 撥款序號
			baTxVo.setRvNo(" "); // 銷帳編號
			baTxVo.setCloseBreachAmt(this.closeBreachAmt); // 違約金
			baTxVo.setUnPaidAmt(baTxVo.getPrincipal().add(this.closeBreachAmt)); // 未收金額
			baTxVo.setDbCr("C"); // 借貸別
			baTxVo.setAcctAmt(BigDecimal.ZERO); // 出帳金額
		}
	}

	/* 設定費用還款順序 */
	private void settlePriority(int EntryDate, int RepayType) {
		// 1.還款類別(費用)相同 > 2.應收費用 > 3:未收費用 > 4:短繳期金 > 5:應繳本利 > 6:另收欠款
		for (BaTxVo ba : this.baTxList) {
			if (ba.getDataKind() == 6) {
				ba.setRepayPriority(6);
			} else if (ba.getDataKind() == 2) {
				ba.setRepayPriority(5);
			} else {
				if (ba.getDataKind() == 1) {
					if (ba.getRepayType() == RepayType && this.txBal.compareTo(ba.getUnPaidAmt()) == 0)
						ba.setRepayPriority(1);
					else if (ba.getRepayType() == RepayType)
						ba.setRepayPriority(2);
					else if (ba.getRepayType() == 1) // 期款
						ba.setRepayPriority(4);
					else
						ba.setRepayPriority(3);
				}
			}
		}
	}

	/* 計算費用作帳金額 */
	private void settleAcctAmt(int repayPriority) {
		this.info("settleAcctAmt repayPriority=" + repayPriority);
		for (BaTxVo ba : this.baTxList) {
			if (this.isPayAllFee) {
				// 全部回收費用
				if (ba.getRepayPriority() == repayPriority && ba.getAcctAmt().equals(BigDecimal.ZERO)) { // 尚未作帳
					ba.setAcctAmt(ba.getUnPaidAmt()); // AcctAmt = UnPaidAmt
					this.xxBal = this.xxBal.subtract(ba.getAcctAmt());
					this.txBal = this.txBal.subtract(ba.getAcctAmt());
					this.repayTotal = this.repayTotal.add(ba.getUnPaidAmt());
					this.overRpFacmNo = ba.getFacmNo();// 溢短繳額度;
				}
			} else {
				// 部分回收費用，可償還餘額比作帳金額大時回收
				if (ba.getRepayPriority() == repayPriority && ba.getAcctAmt().equals(BigDecimal.ZERO) // 尚未作帳
						&& this.xxBal.compareTo(ba.getUnPaidAmt()) >= 0) { // this.xxBal >= UnPaidAmt
					ba.setAcctAmt(ba.getUnPaidAmt()); // AcctAmt = UnPaidAmt
					this.xxBal = this.xxBal.subtract(ba.getAcctAmt()); // this.xxBal = this.xxBal - AcctAmt
					this.txBal = this.txBal.subtract(ba.getAcctAmt()); //
					this.repayTotal = this.repayTotal.add(ba.getUnPaidAmt());
					this.overRpFacmNo = ba.getFacmNo();// 溢短繳額度;
				}
			}
		}
	}

	/* 計算全部已到期本息 */
	private void settleByTbsdy() {
		this.info("settleByTbsdy");
		for (BaTxVo ba : this.baTxList) {
			if (ba.getRepayPriority() == 5 && ba.getAcctAmt().equals(BigDecimal.ZERO)) { // 尚未作帳
				if (baTxVo.getPayIntDate() <= this.txBuffer.getTxCom().getTbsdy()) {
					ba.setAcctAmt(ba.getUnPaidAmt()); // AcctAmt = UnPaidAmt
					this.xxBal = this.xxBal.subtract(ba.getAcctAmt());
					this.txBal = this.txBal.subtract(ba.getAcctAmt());
					this.repayTotal = this.repayTotal.add(ba.getUnPaidAmt());
					if (this.overRpFacmNo == 0) {
						this.overRpFacmNo = ba.getFacmNo();// 溢短繳額度;
					}
				}
			}
		}
	}

	/* 按額度應繳日回收，應繳日由小到大、計息順序(利率由大到小)、額度由小到大 */
	private void settleByPayintDate(int iRepayCode) {
		this.info("settleByPayintDate ...xxBal=" + this.xxBal);
		int facmNo = 0;
		int rePayIntDate = 0;
		BigDecimal payintDateAmt = BigDecimal.ZERO;
		this.repayIntDate = 0;
		for (BaTxVo ba : this.baTxList) {
			if (ba.getRepayPriority() == 5) {
				if (rePayIntDate != ba.getPayIntDate() || facmNo != ba.getFacmNo()) {
					if (this.xxBal.compareTo(BigDecimal.ZERO) > 0) {
						rePayIntDate = ba.getPayIntDate();
						facmNo = ba.getFacmNo();
						payintDateAmt = getPayintDateAmt(rePayIntDate, facmNo);
						this.info("settleByPayintDate xxBal=" + this.xxBal + ", rePayIntDate=" + rePayIntDate
								+ ", payintDateAmt=" + payintDateAmt + ", shortAmtLimit=" + shortAmtLimit);
						// 匯款轉帳最後一期可欠繳
						if (this.xxBal.add(iRepayCode == 1 ? this.shortAmtLimit : BigDecimal.ZERO)
								.compareTo(payintDateAmt) < 0) {
							break;
						} else {
							this.info("settleByPayintDate payintDateAmt=" + payintDateAmt);
							settlePayintDateAmt(rePayIntDate, facmNo);
							this.repayIntDateByFacmNoVo.putParam(parse.IntegerToString(facmNo, 3), rePayIntDate); // 額度還款應繳日
							this.repayIntDate = rePayIntDate;
						}
					}
				}
			}
		}
	}

	// 取得額度應繳日金額，短繳限額
	private BigDecimal getPayintDateAmt(int payIntDate, int facmNo) {
		BigDecimal unPaidAmt = BigDecimal.ZERO;
		this.shortAmtLimit = BigDecimal.ZERO;
		for (BaTxVo ba : this.baTxList) {
			if (ba.getFacmNo() == facmNo && ba.getAcctAmt().equals(BigDecimal.ZERO)) {
				if (ba.getRepayPriority() == 5 && ba.getPayIntDate() == payIntDate) {
					unPaidAmt = unPaidAmt.add(ba.getUnPaidAmt());
					// 依短繳限額 1.還本金額 2.還息金額
					if (ba.getPrincipal().compareTo(BigDecimal.ZERO) > 0) {
						this.shortAmtLimit = ba.getPrincipal()
								.multiply(new BigDecimal(this.txBuffer.getSystemParas().getShortPrinPercent()))
								.divide(new BigDecimal(100)).setScale(0, RoundingMode.HALF_UP);
					} else {
						this.shortAmtLimit = ba.getInterest()
								.multiply(new BigDecimal(this.txBuffer.getSystemParas().getShortIntPercent()))
								.divide(new BigDecimal(100)).setScale(0, RoundingMode.HALF_UP);
					}
				}
			}
		}
		return unPaidAmt;
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
					this.overRpFacmNo = ba.getFacmNo();// 溢短繳額度;
				}
			}
		}
		this.info("settlePayintDateAmt PayIntDate=" + payIntDate + ", FacmNo=" + facmNo);
	}

	/*
	 * // 結案時還款本金已含短繳本金，回收金額須扣除 BigDecimal wkPrincipal =
	 * 
	 */
	/* 計算暫收抵繳作帳金額 */
	private void settleTmpAcctAmt() {
		// 暫收款金額(存入暫收為正、暫收抵繳為負)
		// 回收餘額不足->需暫收抵繳
		if (this.txBal.compareTo(BigDecimal.ZERO) >= 0) {
			this.tempAmt = this.txBal;
		} else {
			this.tmAmt = BigDecimal.ZERO.subtract(this.txBal);
			BigDecimal tmBal = this.tmAmt;
			for (BaTxVo ba : this.baTxList) {
				if (ba.getDataKind() == 3 && tmBal.compareTo(BigDecimal.ZERO) > 0) {
					if (tmBal.compareTo(ba.getUnPaidAmt()) >= 0) {
						ba.setAcctAmt(ba.getUnPaidAmt());
						tmBal = tmBal.subtract(ba.getAcctAmt());
					} else {
						ba.setAcctAmt(tmBal);
						tmBal = BigDecimal.ZERO;
					}
					// 暫收款金額(存入暫收為正、暫收抵繳為負)
					this.tempAmt = this.tempAmt.subtract(ba.getAcctAmt());
				}
			}
		}
		this.info("tavAmt 可暫收抵繳金額= " + this.tavAmt + ", tmAmt 需暫收抵繳金額=" + this.tmAmt + ", tempAmt暫收款金額" + this.tempAmt);

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
		this.info("overRpFacmNo= " + this.overRpFacmNo + ", xxBal=" + this.xxBal + ", tempAmt=" + this.tempAmt);

		baTxVo = new BaTxVo();
		baTxVo.setDataKind(4); // 4.本期溢(+)短(-)繳
		baTxVo.setRepayType(0);
		baTxVo.setCustNo(iCustNo);
		baTxVo.setFacmNo(this.overRpFacmNo);
		baTxVo.setBormNo(0);
		baTxVo.setRvNo(" ");
		baTxVo.setAcctCode("TAV");
		// 溢繳 = 暫收款存入金額(存入暫收為正、暫收抵繳為負)
		// 短繳 = 可償還餘額xxBal為負值時取正值
		if (this.tempAmt.compareTo(BigDecimal.ZERO) >= 0) {
			baTxVo.setDbCr("C");
			baTxVo.setUnPaidAmt(this.tempAmt);
			baTxVo.setAcctAmt(this.tempAmt);
			this.overAmt = baTxVo.getAcctAmt();
			this.info("溢繳金額= " + this.overAmt);
		}
		if (this.xxBal.compareTo(BigDecimal.ZERO) <= 0) {
			baTxVo.setDbCr("D");
			baTxVo.setUnPaidAmt(BigDecimal.ZERO.subtract(this.xxBal));
			this.shortAmt = baTxVo.getUnPaidAmt();
			this.info("短繳金額= " + this.shortAmt);
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
		return rpFacmNo;
	}

	/* Load UnPaid */
	public void loadUnPaid(int iEntryDate, int iCustNo, int iFacmNo, int iBormNo, int iRepayType, TitaVo titaVo)
			throws LogicException {
// 銷帳科目記號ReceivableFlag = 1,2
		// F09 暫付款－火險保費
		// F25 催收款項－火險費用
		// F07 暫付法務費
		// F24 催收款項－法務費用

// 銷帳科目記號ReceivableFlag = 3-未收款
		// F10 帳管費
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
		// YOP 清償違約金 IOP 違約金

//  資料類型 dataKind
		// 1.應收費用+未收費用+短繳期金 <BR>
		// 6.另收欠款(未到期火險費用、費用收取之短繳期金、清償違約金) <BR>

		Slice<AcReceivable> srvList = acReceivableService.acrvFacmNoRange(0, iCustNo, 0, 0, 999, this.index,
				Integer.MAX_VALUE, titaVo); // 銷帳記號 0-未銷, 業務科目記號 0: 一般科目
		rvList = srvList == null ? null : srvList.getContent();
		if (rvList != null) {
			for (AcReceivable rv : rvList) {
				this.info("rvList =" + rv.toString());
				baTxVo = new BaTxVo();
				baTxVo.setReceivableFlag(rv.getReceivableFlag()); // 1:會計銷帳科目(應收) 2:業務銷帳科目(應收) 3:未收費用 4:短繳期金 5.另收欠款
				baTxVo.setCustNo(rv.getCustNo());
				baTxVo.setFacmNo(rv.getFacmNo());
				baTxVo.setBormNo(0);
				baTxVo.setRvNo(rv.getRvNo());
				baTxVo.setAcctCode(rv.getAcctCode());
				baTxVo.setUnPaidAmt(rv.getRvBal());
				baTxVo.setAcctAmt(BigDecimal.ZERO);
				// 利息提存只列欠繳利息
				if (isAcLoanInt) {
					if ("I".equals(rv.getAcctCode().substring(0, 1)) && iFacmNo == rv.getFacmNo()
							&& parse.IntegerToString(iBormNo, 3).equals(rv.getRvNo())) {
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
						if (iFacmNo == 0 || iFacmNo == rv.getFacmNo()) {
							baTxVo.setDataKind(3); // 3.暫收抵繳
							this.tavAmt = this.tavAmt.add(rv.getRvBal());
							this.excessive = this.excessive.add(rv.getRvBal()); // 累溢收
						} else
							baTxVo.setDataKind(5); // 5.其他額度暫收可抵繳
						this.excessiveOther = this.excessiveOther.add(rv.getRvBal()); // 累溢收
						baTxVo.setRepayType(0);
						baTxVo.setDbCr("D");
					} else {
						if (iFacmNo == 0 || iFacmNo == rv.getFacmNo()) {
							baTxVo.setPayIntDate(rv.getOpenAcDate()); // 應繳息日、應繳日
							baTxVo.setDbCr("C");
							// 短繳期金
							if (rv.getReceivableFlag() == 4) {
								baTxVo.setRepayType(1); // 01-期款
								// 還款類別為費用不含短繳
								if (iRepayType >= 4 && iRepayType < 99) {
									baTxVo.setDataKind(6); // 6.另收欠款(費用收取之短繳期金)
								} else {
									baTxVo.setDataKind(1); // 1.應收費用+未收費用+短繳期金
								}
								// 本金
								if ("Z".equals(rv.getAcctCode().substring(0, 1))) {
									baTxVo.setPrincipal(rv.getRvBal());
									baTxVo.setBormNo(parse.stringToInteger(rv.getRvNo())); // 短繳期金有撥款序號
									this.shortfall = this.shortfall.add(rv.getRvBal());
									this.shortfallPrincipal = this.shortfallPrincipal.add(rv.getRvBal());
								}
								// 利息
								if ("I".equals(rv.getAcctCode().substring(0, 1))) {
									baTxVo.setInterest(rv.getRvBal());
									baTxVo.setBormNo(parse.stringToInteger(rv.getRvNo())); // 短繳利息有撥款序號
									this.shortfall = this.shortfall.add(rv.getRvBal());
									this.shortfallInterest = this.shortfallInterest.add(rv.getRvBal());
								}
								// 清償違約金(短繳期金，提前償還有即時清償違約金時寫入)
								if ("YOP".equals(rv.getAcctCode())) {
									baTxVo.setCloseBreachAmt(rv.getRvBal());
									this.shortfall = this.shortfall.add(rv.getRvBal());
									this.shortCloseBreach = this.shortCloseBreach.add(rv.getRvBal());
								}
							} else {
								switch (rv.getAcctCode()) {
								case "F10": // 帳管費/手續費
									baTxVo.setRepayType(4); // 04-帳管費/手續費
									baTxVo.setDataKind(1); // 1.應收費用+未收費用+短繳期金
									this.acctFee = this.acctFee.add(rv.getRvBal());
									break;

								case "F12": // 帳管費企金件
								case "F27": // 聯貸管理費
									// 期款 另收費用
									if (iRepayType >= 4 && iRepayType < 99) {
										if (rv.getReceivableFlag() == 3) {
											baTxVo.setDataKind(1); // 1.應收費用+未收費用+短繳期金
											this.acctFee = this.acctFee.add(rv.getRvBal());
										} else {
											baTxVo.setDataKind(6); // 另收費用
										}
									} else {
										baTxVo.setDataKind(6); // 另收費用
									}
									baTxVo.setRepayType(4); // 04-帳管費/手續費
									break;

								case "F29": // F29 契變手續費
									baTxVo.setRepayType(6); // 06-契變手續費
									baTxVo.setDataKind(1); // 1.應收費用+未收費用+短繳期金
									this.modifyFee = this.modifyFee.add(rv.getRvBal());
									break;
								case "TMI": // 未收火險保費
									baTxVo.setRepayType(5); // 05-火險費
// 0. 還款類別 =	 03-結案, 99-費用全部									
//   fireFee       : 全部
//   unOpenfireFee : 續約保單起日 >=入帳日
//    
// 1. 還款類別 = 01-期款、02-部分部分償還
//   fireFee       : 續約保單起日 <= 入帳月
//   unOpenfireFee : 續約保單起日 > 入帳月
//									
// 2.還款類別 = 05-火險費								
//   fireFee       : 全部
//   unOpenfireFee : 續約保單起日 >=入帳日
									switch (isUnOpenfireFee) {
									case 0:
									case 2:
										baTxVo.setDataKind(1); // 1.應收費用+未收費用+短繳期金
										this.fireFee = this.fireFee.add(rv.getRvBal());
										if (rv.getOpenAcDate() >= iEntryDate) {
											this.unOpenfireFee = this.unOpenfireFee.add(rv.getRvBal());
										}
										break;
									case 1:
										if ((rv.getOpenAcDate() / 100) <= (iEntryDate / 100)) {
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
									baTxVo.setDataKind(1); // 1.應收費用+未收費用+短繳期金
									this.fireFee = this.fireFee.add(rv.getRvBal());
									break;
								case "F25": // F25 催收款項－火險費用
									baTxVo.setRepayType(5); // 05-火險費
									baTxVo.setDataKind(1); // 1.應收費用+未收費用+短繳期金
									this.collFireFee = this.collFireFee.add(rv.getRvBal());
									break;
								case "F07": // F07 暫付法務費
									baTxVo.setRepayType(7); // 07-法務費
									baTxVo.setDataKind(1); // 1.應收費用+未收費用+短繳期金
									this.lawFee = this.lawFee.add(rv.getRvBal());
									break;
								case "F24": // F24 催收款項－法務費用
									baTxVo.setRepayType(7); // 07-法務費
									baTxVo.setDataKind(1); // 1.應收費用+未收費用+短繳期金
									this.collLawFee = this.collLawFee.add(rv.getRvBal());
									break;
								case "F30": // F30 呆帳戶法務費墊付
									baTxVo.setRepayType(7); // 07-法務費
									baTxVo.setDataKind(1); // 1.應收費用+未收費用+短繳期金
									this.lawFee = this.lawFee.add(rv.getRvBal());
									break;
								case "YOP": // YOP 清償違約金(未收費用，清償時寫入)
									baTxVo.setRepayType(9); // 09-其他(清償違約金)
									baTxVo.setDataKind(1); // 1.應收費用+未收費用+短繳期金
									baTxVo.setCloseBreachAmt(rv.getRvBal());
									this.shortCloseBreach = this.shortCloseBreach.add(rv.getRvBal());
									break;
								default:
									baTxVo.setDataKind(0);
									baTxVo.setRepayType(0);
								}
							}
						}
					}
				}
				if (baTxVo.getDataKind() > 0) {
					this.baTxList.add(baTxVo);
					if (baTxVo.getDataKind() == 1 && baTxVo.getRepayType() >= 4) {
						this.totalFee = this.totalFee.add(baTxVo.getUnPaidAmt());
					}
				}
			}
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
	 * 掛帳利息(逆向貸款) ICR
	 * 
	 * @return ..
	 */
	public BigDecimal getOpenInterest() {
		return this.openInterest;
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
	 * 是否回收費用
	 * 
	 * @return Y/N
	 */

	public String getPayFeeFlag() {
		return payFeeFlag;
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
	 * 溢短繳額度
	 * 
	 * @return BigDecimal
	 */
	public int getOverRpFacmNo() {
		return overRpFacmNo;
	}

	/**
	 * 批次入帳可預收期數
	 * 
	 * @return BigDecimal
	 */
	public int getPreRepayTermsBatch() {
		return preRepayTermsBatch;
	}
}
