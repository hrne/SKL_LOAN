package com.st1.itx.util.common;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Component;

import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.dataVO.TotaVo;
import com.st1.itx.db.domain.AcReceivable;
import com.st1.itx.db.domain.LoanBook;
import com.st1.itx.db.domain.LoanBorMain;
import com.st1.itx.db.service.AcReceivableService;
import com.st1.itx.db.service.LoanBookService;
import com.st1.itx.db.service.LoanBorMainService;
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.common.data.BaTxVo;
import com.st1.itx.util.common.data.CalcRepayIntVo;
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
	LoanCalcRepayIntCom loancalcRepayIntCom;

	@Autowired
	public AcReceivableService acReceivableService;
	@Autowired
	public LoanBorMainService loanBorMainService;
	@Autowired
	public LoanBookService loanBookService;

	private BaTxVo baTxVo;
	private ArrayList<BaTxVo> baTxList;
	private List<AcReceivable> rvList;
	private BigDecimal txBal = BigDecimal.ZERO; // txBal 回收餘額
	private BigDecimal xxBal = BigDecimal.ZERO; // xxBal 可償還金額
	private BigDecimal tmAmt = BigDecimal.ZERO; // tmAmt 需暫收抵繳金額
	private BigDecimal loanBal = BigDecimal.ZERO; // 還款前本金餘額
	private BigDecimal tavAmt = BigDecimal.ZERO; // tavAmt 可暫收抵繳金額

// isPayAllFee 是否全部回收費用
	// true->回收全部費用，false 回收金額足夠的費用
	// RepayType 還款類別
	// 1.期款 2.部分償還 3.結案 => true
	// 4.帳管費 5.火險費 6.契變手續費 7.法務費 => false
	private boolean isPayAllFee = false;

	// 未到期火險費用條件 0.全列已到期、1.續約保單起日 > 入帳月、2.續約保單起日 >=入帳日,
	private int isUnOpenfireFee = 0;

// isPaidAdvance 是否含提前繳期款
	private boolean isTermAdvance = false;

// isAcLoanInt 是否利息提存
	private boolean isAcLoanInt = false;

// 總數資料
	private BigDecimal shortfall = BigDecimal.ZERO; // 累短收
	private BigDecimal shortfallInterest = BigDecimal.ZERO; // 累短收 - 利息
	private BigDecimal shortfallPrincipal = BigDecimal.ZERO; // 累短收 - 本金
	private BigDecimal shortCloseBreach = BigDecimal.ZERO; // 累短收 - 清償違約金
	private BigDecimal excessive = BigDecimal.ZERO; // 累溢收
	private BigDecimal excessiveOther = BigDecimal.ZERO; // 其他額度累溢收
	private BigDecimal tempTax = BigDecimal.ZERO; // 暫付所得稅
	private BigDecimal modifyFee = BigDecimal.ZERO; // 契變手續費 F29
	private BigDecimal acctFee = BigDecimal.ZERO; // 帳管費用 F10
	private BigDecimal fireFee = BigDecimal.ZERO; // 火險費用 TMI F09
	private BigDecimal unOpenfireFee = BigDecimal.ZERO; // 未到期火險費用
	private BigDecimal collFireFee = BigDecimal.ZERO; // 催收火險費 F25
	private BigDecimal lawFee = BigDecimal.ZERO; // 法務費用 F07
	private BigDecimal collLawFee = BigDecimal.ZERO; // 催收法務費 F24
	private BigDecimal openInterest = BigDecimal.ZERO; // 掛帳利息(逆向貸款) ICR
	private BigDecimal totalFee = BigDecimal.ZERO; // 費用、短繳總額
	private BigDecimal principal = BigDecimal.ZERO; // 本金
	private BigDecimal interest = BigDecimal.ZERO; // 利息
	private BigDecimal delayInt = BigDecimal.ZERO; // 延滯息
	private BigDecimal breachAmt = BigDecimal.ZERO; // 違約金
	private BigDecimal tempAmt = BigDecimal.ZERO; // 暫收款金額(存入暫收為正、暫收抵繳為負)
	private BigDecimal repayTotal = BigDecimal.ZERO; // 還款總金額
	private int repayIntDate = 0; // repayIntDate 還款應繳日

	private BigDecimal extraRepayAmt = BigDecimal.ZERO; // 部分還款金額
	private String includeIntFlag = "";// 是否內含利息
	private String unpaidIntFlag = "";// 利息是否可欠繳

//  initialize
	private void init() {
		this.baTxVo = new BaTxVo();
		this.baTxList = new ArrayList<BaTxVo>();
		this.rvList = new ArrayList<AcReceivable>();
		this.txBal = BigDecimal.ZERO; // txBal 回收餘額
		this.xxBal = BigDecimal.ZERO; // xxBal 可償還金額
		this.tmAmt = BigDecimal.ZERO; // tmAmt 需暫收抵繳金額
		this.loanBal = BigDecimal.ZERO; // 還款前本金餘額
		this.tavAmt = BigDecimal.ZERO; // tavAmt 可暫收抵繳金額
		// 費用、短繳
		this.shortfall = BigDecimal.ZERO; // 累短收
		this.shortfallInterest = BigDecimal.ZERO; // 累短收 - 利息
		this.shortfallPrincipal = BigDecimal.ZERO; // 累短收 - 本金
		this.shortCloseBreach = BigDecimal.ZERO; // 累短收 - 清償違約金
		this.excessive = BigDecimal.ZERO; // 累溢收
		this.excessiveOther = BigDecimal.ZERO; // 其他額度累溢收

		this.tempTax = BigDecimal.ZERO; // 暫付所得稅
		this.modifyFee = BigDecimal.ZERO; // 契變手續費 F29
		this.acctFee = BigDecimal.ZERO; // 帳管費用 F10
		this.fireFee = BigDecimal.ZERO; // 火險費用 TMI F09
		this.unOpenfireFee = BigDecimal.ZERO; // 未到期火險費用
		this.collFireFee = BigDecimal.ZERO; // 催收火險費 F25
		this.lawFee = BigDecimal.ZERO; // 法務費用 F07
		this.collLawFee = BigDecimal.ZERO; // 催收法務費 F24
		this.openInterest = BigDecimal.ZERO; // 掛帳利息(逆向貸款) ICR (未用)
		this.totalFee = BigDecimal.ZERO; // 費用、短繳總額

		// 本金、利息
		this.principal = BigDecimal.ZERO; // 本金
		this.interest = BigDecimal.ZERO; // 利息
		this.delayInt = BigDecimal.ZERO; // 延滯息
		this.breachAmt = BigDecimal.ZERO; // 違約金

		// 還款金額
		this.tempAmt = BigDecimal.ZERO; // 暫收款金額(存入暫收為正、暫收抵繳為負)
		this.repayTotal = BigDecimal.ZERO; // 總還款金額
		this.repayIntDate = 0; // repayIntDate 還款應繳日

		// 部分還款
		this.extraRepayAmt = BigDecimal.ZERO; // 部分還款金額
		this.includeIntFlag = "Y";// 是否內含利息
		this.unpaidIntFlag = "N";// 利息是否可欠繳
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
	 * @throws LogicException
	 */

	public ArrayList<BaTxVo> settingUnPaid(int iEntryDate, int iCustNo, int iFacmNo, int iBormNo, int iRepayType,
			BigDecimal iTxAmt, TitaVo titaVo) throws LogicException {
		this.info("BaTxCom settingUnPaid ...");
		this.info("BaTxCom settingUnPaid EntryDate  入帳日=" + iEntryDate);
		this.info("BaTxCom settingUnPaid 戶號=" + iCustNo + "-" + iFacmNo + "-" + iBormNo);
		this.info("BaTxCom settingUnPaid RepayType 還款類別=" + iRepayType);
		this.info("BaTxCom settingUnPaid TxAmt 回收金額=" + iTxAmt);
		init();
		// STEP 1: 設定預設值
		// isPayAllFee 費用是否全部回收->
		if (iRepayType <= 3 || iRepayType == 99)
			this.isPayAllFee = true; // 全部
		else
			this.isPayAllFee = false; // 部分

		// isUnOpenfireFee 未到期火險費用條件
// 還款類別                            未到期火險費用條件           火險費輸出欄說明
// 03-結案, 99-費用全部                0.全列已到期                 fireFee(全部)，unOpenfireFee(續約保單起日>=入帳日)，fireFee內含unOpenfireFee
// 01-期款、02-部分部分償還 、05-火險費     1.續約保單起日 > 入帳月		    fireFee(續約保單起日 <= 入帳月)、 unOpenfireFee(續約保單起日 > 入帳月)
// 00-費用全部(已到期) 、else		       2.續約保單起日 >=入帳日              fireFee(續約保單起日 < 入帳日)、 unOpenfireFee(續約保單起日 >=入帳日)

		if (iRepayType == 03 || iRepayType == 99) {
			isUnOpenfireFee = 0;
		} else if (iRepayType == 01 || iRepayType == 02 || iRepayType == 05) {
			isUnOpenfireFee = 1;
		} else {
			isUnOpenfireFee = 2;
		}

		// isPaidAdvance 是否含提前繳期款 ->不含
		this.isTermAdvance = false;

		// 還款類別:02 與約定還本日期相同
		if (iRepayType == 2) {
			this.extraRepayAmt = iTxAmt; // 部分還款金額
			int wkFacmNoStart = iFacmNo == 0 ? 1 : iFacmNo;
			int wkFacmNoEnd = iFacmNo == 0 ? 999 : iFacmNo;
			int wkBormNoStart = iBormNo == 0 ? 1 : iBormNo;
			int wkBormNoEnd = iBormNo == 0 ? 900 : iBormNo;
			Slice<LoanBook> loanBookList = loanBookService.bookCustNoRange(iCustNo, iCustNo, wkFacmNoStart, wkFacmNoEnd,
					wkBormNoStart, wkBormNoEnd, this.index, Integer.MAX_VALUE, titaVo);
			if (loanBookList != null) {
				for (LoanBook tLoanBook : loanBookList.getContent()) {
					if (tLoanBook.getBookDate() == iEntryDate && tLoanBook.getStatus() == 0) {
						iFacmNo = tLoanBook.getFacmNo(); // 還款額度
						iBormNo = tLoanBook.getBormNo(); // 撥款序號
						this.extraRepayAmt = tLoanBook.getBookAmt(); // 部分還款金額
						this.includeIntFlag = tLoanBook.getIncludeIntFlag(); // 是否內含利息
						this.unpaidIntFlag = tLoanBook.getUnpaidIntFlag();// 利息是否可欠繳
						break;
					}
				}
			}
		}

		// STEP 2: Load UnPaid 1.應收費用+未收費用+短繳期金 3.暫收抵繳 6.另收欠款
		loadUnPaid(iEntryDate, iCustNo, iFacmNo, 0, iRepayType, titaVo);

		// STEP 3: 計算放款本息
		if (iRepayType >= 1 && iRepayType <= 3) {
			repayLoan(iEntryDate, 0, iCustNo, iFacmNo, iBormNo, iRepayType, iTxAmt, 0, titaVo); // Terms = 0
		}

		// STEP 4: 設定總金額
		this.txBal = iTxAmt; // 回收金額
		this.xxBal = this.txBal; // 可償還餘額
		this.tmAmt = BigDecimal.ZERO; // this.tmAmt 暫收抵繳金額
		// 加可抵繳至可償還餘額
		this.xxBal = this.xxBal.add(this.tavAmt);

		// STEP 5: 設定還款順序
		// 1.還款類別(費用)相同 > 2.應收費用 > 3:未收費用 > 4:短繳期金 > 5:已到期應繳本息 > 6:另收欠款 > 7.未到期應繳本息
		settlePriority(iEntryDate, iRepayType); // 還款順序

		// STEP 6 : 計算作帳金額

		// 回收費用 1.還款類別(費用)相同 2.應收費用 3:未收費用
		settleAcctAmt(1);
		settleAcctAmt(2);
		settleAcctAmt(3);

		// 費用全部時收回 4:短繳期金
		if (iRepayType <= 3 || iRepayType == 99) {
			settleAcctAmt(4);
		}

		// 還放款時收回 5:已到期應繳本息
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
	 * @param iPayintDate 應繳日
	 * @param iCustNo     戶號
	 * @param iFacmNo     額度
	 * @param iBormNo     撥款
	 * @param iRepayCode  還款來源
	 * @param iRepayType  還款類別
	 * @param iTxAmt      入帳金額
	 * @param titaVo      TitaVo
	 * @return ArrayList of BaTxVo
	 * @throws LogicException
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

		// isPaidAdvance 是否含提前繳期款 -> 不含
		this.isTermAdvance = false;

		// 還款類別:02 大於等於約定還本金額
		if (iRepayType == 2) {
			this.extraRepayAmt = iTxAmt;
			int wkFacmNoStart = iFacmNo == 0 ? 1 : iFacmNo;
			int wkFacmNoEnd = iFacmNo == 0 ? 999 : iFacmNo;
			int wkBormNoStart = iBormNo == 0 ? 1 : iBormNo;
			int wkBormNoEnd = iBormNo == 0 ? 900 : iBormNo;
			Slice<LoanBook> loanBookList = loanBookService.bookCustNoRange(iCustNo, iCustNo, wkFacmNoStart, wkFacmNoEnd,
					wkBormNoStart, wkBormNoEnd, this.index, Integer.MAX_VALUE, titaVo);
			if (loanBookList != null) {
				for (LoanBook tLoanBook : loanBookList.getContent()) {
					if (tLoanBook.getBookDate() >= iEntryDate && tLoanBook.getStatus() == 0) {
						if (iTxAmt.compareTo(BigDecimal.ZERO) == 0 || iTxAmt.compareTo(tLoanBook.getBookAmt()) >= 0) {
							iFacmNo = tLoanBook.getFacmNo(); // 還款額度
							iBormNo = tLoanBook.getBormNo(); // 撥款序號
							this.extraRepayAmt = tLoanBook.getBookAmt(); // 部分還款金額
							this.includeIntFlag = tLoanBook.getIncludeIntFlag(); // 是否內含利息
							this.unpaidIntFlag = tLoanBook.getUnpaidIntFlag();// 利息是否可欠繳
							break;
						}
					}
				}
			}
		}

		// STEP 2: Load UnPaid 1.應收費用+未收費用+短繳期金 3.暫收抵繳 6.另收欠款
		loadUnPaid(iEntryDate, iCustNo, iFacmNo, 0, iRepayType, titaVo);

		// STEP 3: 計算放款本息

		// 計算部分償還金額
		// 未約定 => min(交易金額, 可部分償還金額)
		// 有約定 => 約定還款金額
		if (iRepayType == 2 && this.extraRepayAmt.compareTo(BigDecimal.ZERO) == 0) {
			this.extraRepayAmt = iTxAmt.add(this.tavAmt).subtract(this.totalFee);
			if (iTxAmt.compareTo(this.extraRepayAmt) < 0) {
				this.extraRepayAmt = iTxAmt;
			}
		}

		// 放款本息計算
		if (iRepayType >= 1 && iRepayType <= 3) {
			repayLoan(iEntryDate, iPayintDate, iCustNo, iFacmNo, iBormNo, iRepayType, iTxAmt, 0, titaVo); // Terms = 0
		}

		// STEP 4: 設定總金額
		this.txBal = iTxAmt; // 回收金額
		this.xxBal = this.txBal; // 可償還餘額
		this.tmAmt = BigDecimal.ZERO; // this.tmAmt 暫收抵繳金額

		// 加可抵繳至可償還餘額
		this.xxBal = this.xxBal.add(this.tavAmt);

		// STEP 5: 設定還款順序
		// 1.還款類別(費用)相同 > 2.應收費用 > 3:未收費用 > 4:短繳期金 > 5:已到期應繳本息 > 6:另收欠款 > 7.未到期應繳本息
		settlePriority(iEntryDate, iRepayType); //

		// STEP 6 : 計算作帳金額
		settleAcctAmt(1);
		settleAcctAmt(2);
		settleAcctAmt(3);

		// 費用全部時收回 4:短繳期金
		if (iRepayType <= 3 || iRepayType == 99) {
			settleAcctAmt(4);
		}

		// 放款回收時 5:已到期應繳本息
		// 回收金額時排序,依應繳日由小到大、計息順序(利率由大到小)、額度由小到大
		if (iRepayType == 1) {
			for (BaTxVo ba : this.baTxList) {
				this.info("sort B: " + ba.toString());
			}
			Collections.sort(baTxList); // 排序優先度(由小到大) payIntDate 應繳日
			for (BaTxVo ba : this.baTxList) {
				this.info("sort A: " + ba.toString());
			}
			settleByPayintDate();
			// 無可回收計算全部欠繳
			if (this.repayIntDate == 0) {
				settleAcctAmt(5);
			}
		}
		if (iRepayType == 2 || iRepayType == 3) {
			settleAcctAmt(5);
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
	 * @throws LogicException
	 */
	public ArrayList<BaTxVo> acLoanInt(int iEntryDate, int iPayintDate, int iCustNo, int iFacmNo, int iBormNo,
			TitaVo titaVo) throws LogicException {
		this.info("BaTxCom acLoanInt ..." + iEntryDate + "/" + iPayintDate + " " + iCustNo + "-" + iFacmNo + "-"
				+ iBormNo);
		init();
		// iEntryDate 入帳日 ==> 月底日曆日
		// iPayintDate 利息計算止日 ==> 次月月初日

		this.baTxList = new ArrayList<BaTxVo>();

		// isPaidAdvance 是否含提前繳期款
		this.isTermAdvance = true;

		// 未到期火險費用條件 ==> 1.續約保單起日 > 入帳月
		isUnOpenfireFee = 1;

		// isAcLoanInt 是否利息提存- > 是
		this.isAcLoanInt = true;

		// 計算利息
		repayLoan(iEntryDate, iPayintDate, iCustNo, iFacmNo, iBormNo, 90, BigDecimal.ZERO, 0, titaVo); // Terms = 0

		// 短繳利息
		loadUnPaid(iEntryDate, iCustNo, iFacmNo, iBormNo, 1, titaVo);

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
	 * @throws LogicException
	 */
	public ArrayList<BaTxVo> termsPay(int iEntryDate, int iCustNo, int iFacmNo, int iBormNo, int iTerms, TitaVo titaVo)
			throws LogicException {
		this.info("BaTxCom acLoanInt ...");
		init();

		// 費用是否全部回收
		this.isPayAllFee = true; // 全部

		// 未到期火險費用條件 1：fireFee(續約保單起日 <= 入帳月)、 unOpenfireFee(續約保單起日 > 入帳月)
		isUnOpenfireFee = 1;

		// isPaidAdvance 是否含提前繳期款
		this.isTermAdvance = true;

		// 計算放款本息
		repayLoan(iEntryDate, 0, iCustNo, iFacmNo, iBormNo, 01, BigDecimal.ZERO, iTerms, titaVo);

		// STEP 3: Load UnPaid 1.應收費用+未收費用+短繳期金 3.暫收抵繳 6.另收欠款
		loadUnPaid(iEntryDate, iCustNo, iFacmNo, 0, 1, titaVo);

		// END
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
	 * @throws LogicException
	 */
	public ArrayList<BaTxVo> cashFlow(int iEntryDate, int iCustNo, int iFacmNo, int iBormNo, TitaVo titaVo)
			throws LogicException {
		this.info("BaTxCom cashFlow ...");
		init();

		// isPaidAdvance 是否含提前繳期款
		this.isTermAdvance = true;

		//
		repayLoan(iEntryDate, 0, iCustNo, iFacmNo, iBormNo, 01, BigDecimal.ZERO, 0, titaVo);

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
		loanSetRepayIntCom.setTxBuffer(this.getTxBuffer());
		int wkFacmNoStart = 1;
		int wkFacmNoEnd = 999;
		int wkBormNoStart = 1;
		int wkBormNoEnd = 900;
		int wkTerms = 0;
		BigDecimal wkExtraRepayAmtRemaind = BigDecimal.ZERO; // 部分還款剩餘金額
		ArrayList<CalcRepayIntVo> lCalcRepayIntVo;

		if (iRepayType <= 3 || iRepayType == 90) {
			if (iFacmNo > 0) {
				wkFacmNoStart = iFacmNo;
				wkFacmNoEnd = iFacmNo;
			}

			if (iBormNo > 0) {
				wkBormNoStart = iBormNo;
				wkBormNoEnd = iBormNo;
			}
			// loanBorMainService.setPageLimit(Integer.MAX_VALUE);
			Slice<LoanBorMain> slLoanBorMain = loanBorMainService.bormCustNoEq(iCustNo, wkFacmNoStart, wkFacmNoEnd,
					wkBormNoStart, wkBormNoEnd, this.index, Integer.MAX_VALUE, titaVo);
			List<LoanBorMain> lLoanBorMain = slLoanBorMain == null ? null
					: new ArrayList<LoanBorMain>(slLoanBorMain.getContent());
			if (lLoanBorMain == null || lLoanBorMain.size() == 0) {
				throw new LogicException(titaVo, "E0001", "戶號有誤"); // 查詢資料不存在
			}
			Collections.sort(lLoanBorMain, new Comparator<LoanBorMain>() {
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
					if (iRepayType == 2) {
						if (c1.getStoreRate().compareTo(c2.getStoreRate()) != 0) {
							return (c1.getStoreRate().compareTo(c2.getStoreRate()) > 0 ? -1 : 1);
						}
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
			for (LoanBorMain ln : lLoanBorMain) {
				if (ln.getStatus() != 0) {
					continue;
				}
				this.loanBal = ln.getLoanBal(); // 還款前本金餘額
				switch (iRepayType) {
				case 1: // 01-期款
					if (iTerms == 0) {
						if (ln.getPrevPayIntDate() >= iEntryDate || ln.getDrawdownDate() == iEntryDate) {
							continue;
						}
						if (ln.getPayIntFreq() == 0 || ln.getPayIntFreq() == 99) {
							wkTerms = 1;
						} else {
							// 計算至入帳日/應繳日的應繳期數
							wkTerms = loanCom.getTermNo(2, ln.getFreqBase(), ln.getPayIntFreq(), ln.getSpecificDate(),
									ln.getSpecificDd(), iPayIntDate > 0 ? iPayIntDate : iEntryDate);
							if (ln.getPrevPayIntDate() > ln.getDrawdownDate()) {
								wkTerms = wkTerms - loanCom.getTermNo(2, ln.getFreqBase(), ln.getPayIntFreq(),
										ln.getSpecificDate(), ln.getSpecificDd(), ln.getPrevPayIntDate());
							}
						}
					} else {
						wkTerms = iTerms;
					}
					// 期款最後一期
					if (iEntryDate >= ln.getMaturityDate() || iPayIntDate >= ln.getMaturityDate()) {
						if (wkTerms == 0) {
							wkTerms = 1;
						}
						if (this.isTermAdvance) {
							loancalcRepayIntCom = loanSetRepayIntCom.setRepayInt(ln, 0, ln.getMaturityDate(), 0,
									iEntryDate, titaVo);
						} else {
							loancalcRepayIntCom = loanSetRepayIntCom.setRepayInt(ln, 0, iEntryDate, 0, iEntryDate,
									titaVo);
						}
					} else {
						loancalcRepayIntCom = loanSetRepayIntCom.setRepayInt(ln, 1, 0, 0, iEntryDate, titaVo);
					}
					// 輸出每期
					for (int i = 1; i <= wkTerms; i++) {
						lCalcRepayIntVo = loancalcRepayIntCom.getRepayInt(titaVo);
						repayLoanBaTxVo(iEntryDate, iPayIntDate, iRepayType, iCustNo, ln, lCalcRepayIntVo);
						loancalcRepayIntCom.setPrincipal(loancalcRepayIntCom.getLoanBal()); // 計息本金
						loancalcRepayIntCom.setStoreRate(loancalcRepayIntCom.getStoreRate()); // 上次收息利率
						loancalcRepayIntCom.setTerms(1); // 本次計息期數
						loancalcRepayIntCom.setIntStartDate(loancalcRepayIntCom.getPrevPaidIntDate()); // 計算起日
						loancalcRepayIntCom.setIntEndDate(0); // 計息止日
						loancalcRepayIntCom.setPaidTerms(loancalcRepayIntCom.getPaidTerms()); // 已繳息期數
						loancalcRepayIntCom.setPrevRepaidDate(loancalcRepayIntCom.getPrevRepaidDate()); // 上次還本日
						loancalcRepayIntCom.setPrevPaidIntDate(loancalcRepayIntCom.getPrevPaidIntDate()); // 上次繳息日
						loancalcRepayIntCom.setNextPayIntDate(loancalcRepayIntCom.getNextPayIntDate()); // 下次繳息日,應繳息日,預定收息日
						loancalcRepayIntCom.setNextRepayDate(loancalcRepayIntCom.getNextRepayDate()); // 下次還本日,應還本日,預定還本日
						loancalcRepayIntCom.setDueAmt(loancalcRepayIntCom.getDueAmt()); // 每期攤還金額
						if (loancalcRepayIntCom.getLoanBal().equals(BigDecimal.ZERO)) {
							break;
						}
						// 期款最後一期
						if (loancalcRepayIntCom.getNextPayIntDate() >= ln.getMaturityDate()) {
							loancalcRepayIntCom.setCaseCloseFlag("Y"); // 結案記號 Y:是 N:否
							if (this.isTermAdvance) {
								loancalcRepayIntCom.setIntEndDate(ln.getMaturityDate());
							} else {
								loancalcRepayIntCom.setIntEndDate(iEntryDate);
							}
						}
					}
					break;
				case 2: // 部分償還金額
					wkExtraRepayAmtRemaind = this.extraRepayAmt.subtract(this.principal).subtract(this.interest)
							.subtract(this.delayInt).subtract(this.breachAmt);
					if (wkExtraRepayAmtRemaind.compareTo(BigDecimal.ZERO) <= 0) {
						break;
					}
					if (ln.getNextPayIntDate() <= this.txBuffer.getTxCom().getTbsdy()) {
						throw new LogicException(titaVo, "E3072",
								parse.IntegerToString(ln.getFacmNo(), 3) + "-"
										+ parse.IntegerToString(ln.getBormNo(), 3) + ", 部分償還前應先償還期款, 應繳息日 = "
										+ ln.getNextPayIntDate()); // 該筆放款尚有未回收期款
					}

					// 部分償還金額超過放款餘額
					if (wkExtraRepayAmtRemaind.compareTo(ln.getLoanBal()) >= 0) {
						loancalcRepayIntCom.setCaseCloseFlag("Y"); // 結案記號 Y:是 N:否
					} else {
						loancalcRepayIntCom = loanSetRepayIntCom.setRepayInt(ln, 0, iEntryDate, 1, iEntryDate, titaVo);
						loancalcRepayIntCom.setExtraRepayFlag(this.includeIntFlag); // 是否內含利息 Y:是 N:否
						loancalcRepayIntCom.setExtraRepay(this.extraRepayAmt);
					}
					lCalcRepayIntVo = loancalcRepayIntCom.getRepayInt(titaVo);
					repayLoanBaTxVo(iEntryDate, iPayIntDate, iRepayType, iCustNo, ln, lCalcRepayIntVo);

					break;
				case 3: // 結案
					loancalcRepayIntCom = loanSetRepayIntCom.setRepayInt(ln, 0, iEntryDate, 1, iEntryDate, titaVo);
					loancalcRepayIntCom.setCaseCloseFlag("Y"); // 結案記號 Y:是 N:否
					lCalcRepayIntVo = loancalcRepayIntCom.getRepayInt(titaVo);
					repayLoanBaTxVo(iEntryDate, iPayIntDate, iRepayType, iCustNo, ln, lCalcRepayIntVo);
					break;
				case 90: // 提存
					// iEntryDate 入帳日 ==> 月底日曆日
					// iPayIntDate 利息計算止日 ==> 次月月初日
					// 上次繳息日超過
					if (ln.getPrevPayIntDate() >= iPayIntDate || ln.getDrawdownDate() >= iPayIntDate) {
						emptyLoanBaTxVo(iEntryDate, iRepayType, iCustNo, ln);
						continue;
					}
					// 計算到利息計算止日之繳息期數 1:指定日期之當期數
					wkTerms = loanCom.getTermNo(1, ln.getFreqBase(), ln.getPayIntFreq(), ln.getSpecificDate(),
							ln.getSpecificDd(), iPayIntDate);
					// 減去計算至上次繳息日之期數
					if (ln.getPrevPayIntDate() > ln.getDrawdownDate()) {
						wkTerms = wkTerms - loanCom.getTermNo(1, ln.getFreqBase(), ln.getPayIntFreq(),
								ln.getSpecificDate(), ln.getSpecificDd(), ln.getPrevPayIntDate());
					}

					loanSetRepayIntCom.setTxBuffer(this.getTxBuffer());
					// 每次計算一期，最後一期計算到利息計算止日
					if (iPayIntDate <= ln.getNextPayIntDate()) {
						loancalcRepayIntCom = loanSetRepayIntCom.setRepayInt(ln, 0, iPayIntDate, 0, iEntryDate, titaVo);
					} else {
						loancalcRepayIntCom = loanSetRepayIntCom.setRepayInt(ln, 1, 0, 0, iEntryDate, titaVo);
					}
					for (int i = 1; i <= wkTerms; i++) {
						if (i == wkTerms) {
							loancalcRepayIntCom.setTerms(0); // 本次計息期數
							loancalcRepayIntCom.setIntEndDate(iPayIntDate); // 計息止日
						}
						lCalcRepayIntVo = loancalcRepayIntCom.getRepayInt(titaVo);
						repayLoanBaTxVo(iEntryDate, iPayIntDate, iRepayType, iCustNo, ln, lCalcRepayIntVo);
						loancalcRepayIntCom.setPrincipal(loancalcRepayIntCom.getLoanBal()); // 計息本金
						loancalcRepayIntCom.setStoreRate(loancalcRepayIntCom.getStoreRate()); // 上次收息利率
						loancalcRepayIntCom.setIntStartDate(loancalcRepayIntCom.getPrevPaidIntDate()); // 計算起日
						loancalcRepayIntCom.setPaidTerms(loancalcRepayIntCom.getPaidTerms()); // 已繳息期數
						loancalcRepayIntCom.setPrevRepaidDate(loancalcRepayIntCom.getPrevRepaidDate()); // 上次還本日
						loancalcRepayIntCom.setPrevPaidIntDate(loancalcRepayIntCom.getPrevPaidIntDate()); // 上次繳息日
						loancalcRepayIntCom.setNextPayIntDate(loancalcRepayIntCom.getNextPayIntDate()); // 下次繳息日,應繳息日,預定收息日
						loancalcRepayIntCom.setNextRepayDate(loancalcRepayIntCom.getNextRepayDate()); // 下次還本日,應還本日,預定還本日
						loancalcRepayIntCom.setDueAmt(loancalcRepayIntCom.getDueAmt()); // 每期攤還金額
						if (loancalcRepayIntCom.getLoanBal().equals(BigDecimal.ZERO)) {
							break;
						}
					}
					break;
				}
			}
		}
		if (this.baTxList != null && this.baTxList.size() > 0)

		{
			this.baTxList.sort((c1, c2) -> {
				return c1.compareTo(c2);
			});
		}
	}

	private void repayLoanBaTxVo(int iEntryDate, int iPayIntDate, int iRepayType, int iCustNo, LoanBorMain ln,
			ArrayList<CalcRepayIntVo> lCalcRepayIntVo) {
		baTxVo = new BaTxVo();
		baTxVo.setDataKind(2); // 2.本金利息
		// 還款類別
		baTxVo.setRepayType(iRepayType);
		baTxVo.setReceivableFlag(0); // 銷帳科目記號 0:非銷帳科目
		baTxVo.setCustNo(iCustNo); // 借款人戶號
		baTxVo.setFacmNo(ln.getFacmNo()); // 額度編號
		baTxVo.setBormNo(ln.getBormNo()); // 撥款序號
		baTxVo.setRvNo(" "); // 銷帳編號
		baTxVo.setPaidTerms(loancalcRepayIntCom.getPaidTerms()); // 繳息期數
		baTxVo.setPayIntDate(loancalcRepayIntCom.getPrevPaidIntDate()); // 應繳息日
		baTxVo.setAcctCode(loancalcRepayIntCom.getAcctCode()); // 業務科目
		baTxVo.setPrincipal(loancalcRepayIntCom.getPrincipal()); // 本金
		baTxVo.setInterest(loancalcRepayIntCom.getInterest()); // // 利息
		baTxVo.setDelayInt(loancalcRepayIntCom.getDelayInt()); // 延滯息
		baTxVo.setBreachAmt(loancalcRepayIntCom.getBreachAmt()); // 違約金
		//
		this.principal = this.principal.add(loancalcRepayIntCom.getPrincipal());
		this.interest = this.interest.add(loancalcRepayIntCom.getInterest());
		this.delayInt = this.delayInt.add(loancalcRepayIntCom.getDelayInt());
		this.breachAmt = this.breachAmt.add(loancalcRepayIntCom.getBreachAmt());
		//
		baTxVo.setUnPaidAmt(
				baTxVo.getPrincipal().add(baTxVo.getInterest()).add(baTxVo.getDelayInt()).add(baTxVo.getBreachAmt())); // 未收金額
		baTxVo.setAcctCode(loanCom.setIntAcctCode(loancalcRepayIntCom.getAcctCode())); // 業務科目
		baTxVo.setDbCr("C"); // 借貸別
		baTxVo.setAcctAmt(BigDecimal.ZERO); // 出帳金額
		baTxVo.setLoanBal(this.loanBal); // 放款餘額(還款前、只放第一期)
		this.loanBal = BigDecimal.ZERO;
		baTxVo.setExtraAmt(loancalcRepayIntCom.getExtraAmt()); // 提前償還金額
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
				// 期款最後一期
				if (baTxVo.getRepayType() == 1 && ca.getEndDate() >= ln.getMaturityDate()) {
					baTxVo.setCloseFg(1); // 1.正常結案
				}
				if (baTxVo.getRepayType() == 3) {
					if (ca.getEndDate() >= ln.getMaturityDate()) {
						baTxVo.setCloseFg(1); // 1.正常結案
					} else {
						baTxVo.setCloseFg(2); // 2.提前結案
					}
				}
			}
		}
		// 是否含提前繳期款 yes -> 應繳日可大於入帳日
		if (this.isTermAdvance) {
			this.baTxList.add(baTxVo);
		} else if (iPayIntDate > 0) {
			if (baTxVo.getPayIntDate() > iPayIntDate) {
				this.info("repayLoan BaTxVo over iPayIntDate) : " + baTxVo);
			} else {
				this.baTxList.add(baTxVo);
			}
		} else {
			if (baTxVo.getPayIntDate() > iEntryDate) {
				this.info("repayLoan BaTxVo over iEntryDate) : " + baTxVo);
			} else {
				this.baTxList.add(baTxVo);
			}
		}
	}

	private void emptyLoanBaTxVo(int iEntryDate, int iRepayType, int iCustNo, LoanBorMain ln) {
		baTxVo = new BaTxVo();
		baTxVo.setDataKind(2); // 2.本金利息
		baTxVo.setRepayType(iRepayType); // 還款類別
		baTxVo.setReceivableFlag(0); // 銷帳科目記號 0:非銷帳科目
		baTxVo.setCustNo(iCustNo); // 借款人戶號
		baTxVo.setFacmNo(ln.getFacmNo()); // 額度編號
		baTxVo.setBormNo(ln.getBormNo()); // 撥款序號
		baTxVo.setRvNo(" "); // 銷帳編號
		baTxVo.setAcctCode(loanCom.setIntAcctCode(loancalcRepayIntCom.getAcctCode())); // 業務科目
		baTxVo.setLoanBal(this.loanBal); // 放款餘額(還款前、只放第一期)
		this.loanBal = BigDecimal.ZERO;
	}

	/* 設定費用還款順序 */
	private void settlePriority(int EntryDate, int RepayType) {
		// 1.還款類別(費用)相同 > 2.應收費用 > 3:未收費用 > 4:短繳期金 > 5:已到期應繳本息 > 6:另收欠款 > 7.未到期應繳本息
		for (BaTxVo ba : this.baTxList) {
			if (ba.getDataKind() == 6) {
				ba.setRepayPriority(6);
			} else if (ba.getDataKind() == 2) {
				if (RepayType == 1 && ba.getPayIntDate() > EntryDate) // 期款 應繳日 > 入帳日
					ba.setRepayPriority(7);
				else
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
				// 全部回收
				if (ba.getRepayPriority() == repayPriority && ba.getAcctAmt().equals(BigDecimal.ZERO)) { // 尚未作帳
					ba.setAcctAmt(ba.getUnPaidAmt()); // AcctAmt = UnPaidAmt
					this.xxBal = this.xxBal.subtract(ba.getAcctAmt());
					this.txBal = this.txBal.subtract(ba.getAcctAmt());
					this.repayTotal = this.repayTotal.add(ba.getUnPaidAmt());
				}
			} else {
				// 部分回收，可償還餘額比作帳金額大時回收
				if (ba.getRepayPriority() == repayPriority && ba.getAcctAmt().equals(BigDecimal.ZERO) // 尚未作帳
						&& this.xxBal.compareTo(ba.getUnPaidAmt()) >= 0) { // this.xxBal >= UnPaidAmt
					ba.setAcctAmt(ba.getUnPaidAmt()); // AcctAmt = UnPaidAmt
					this.xxBal = this.xxBal.subtract(ba.getAcctAmt()); // this.xxBal = this.xxBal - AcctAmt
					this.txBal = this.txBal.subtract(ba.getAcctAmt()); //
					this.repayTotal = this.repayTotal.add(ba.getUnPaidAmt());
				}
			}
		}
	}

	/* 按額度應繳日回收，應繳日由小到大、計息順序(利率由大到小)、額度由小到大 */
	private void settleByPayintDate() {
		this.info("settleByTerm ...xxBal=" + this.xxBal);
		int facmNo = 0;
		int payIntDate = 0;
		BigDecimal payintDateAmt = BigDecimal.ZERO;
		this.repayIntDate = 0;
		for (BaTxVo ba : this.baTxList) {
			if (ba.getRepayPriority() == 5) {
				if (payIntDate != ba.getPayIntDate() || facmNo != ba.getFacmNo()) {
					if (this.xxBal.compareTo(BigDecimal.ZERO) > 0) {
						payIntDate = ba.getPayIntDate();
						facmNo = ba.getFacmNo();
						payintDateAmt = getPayintDateAmt(payIntDate, facmNo);
						this.info("settleByTerm xxBal=" + this.xxBal + " , payintDateAmt=" + payintDateAmt);
						if (this.xxBal.compareTo(payintDateAmt) < 0) {
							break;
						} else {
							this.info("settlePayintDateAmt payintDateAmt=" + payintDateAmt);
							settlePayintDateAmt(payIntDate, facmNo);
						}
					}
				}
			}
		}
	}

	// 取得額度應繳日金額
	private BigDecimal getPayintDateAmt(int payIntDate, int facmNo) {
		BigDecimal unPaidAmt = BigDecimal.ZERO;
		for (BaTxVo ba : this.baTxList) {
			if (ba.getFacmNo() == facmNo && ba.getAcctAmt().equals(BigDecimal.ZERO)) {
				if (ba.getRepayPriority() == 5 && ba.getPayIntDate() == payIntDate) {
					unPaidAmt = unPaidAmt.add(ba.getUnPaidAmt());
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
				}
			}
		}
		this.info("settlePayintDateAmt PayIntDate=" + payIntDate + ", FacmNo=" + facmNo);
		this.repayIntDate = payIntDate;
	}

	/* 計算暫收抵繳作帳金額 */
	private void settleTmpAcctAmt() {
		// 回收餘額不足->需暫收抵繳
		if (this.txBal.compareTo(BigDecimal.ZERO) <= 0) {
			this.tmAmt = BigDecimal.ZERO.subtract(this.txBal);
		}
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
		this.info("tavAmt 可暫收抵繳金額= " + this.tavAmt + ", tmAmt 需暫收抵繳金額=" + this.tmAmt);

	}

	/* 計算溢(C)短(D)繳 */
	private void settleOverAmt(int CustNo, int FacmNo, TitaVo titaVo) {
		// 無額度 ---> 抓最大
		int minFacmNo = FacmNo;
		if (FacmNo == 0)
			minFacmNo = gettingRpFacmNo(CustNo, FacmNo, titaVo);
		else
			minFacmNo = FacmNo;
		baTxVo = new BaTxVo();
		baTxVo.setDataKind(4); // 4.本期溢(+)短(-)繳
		baTxVo.setRepayType(0);
		baTxVo.setCustNo(CustNo);
		baTxVo.setFacmNo(minFacmNo);
		baTxVo.setBormNo(0);
		baTxVo.setRvNo(" ");
		baTxVo.setAcctCode("TAV");
		// 暫收款金額(存入暫收為正、暫收抵繳為負)
		// 可償還餘額xxBal正值取回收餘額txBal，否則取可償還餘額(正值)
		if (this.xxBal.compareTo(BigDecimal.ZERO) >= 0) {
			baTxVo.setDbCr("C");
			baTxVo.setUnPaidAmt(this.txBal);
			baTxVo.setAcctAmt(this.txBal);
			this.tempAmt = this.tempAmt.add(this.txBal);
			this.info("溢繳金額= " + baTxVo.getUnPaidAmt());
		} else {
			baTxVo.setDbCr("D");
			baTxVo.setUnPaidAmt(BigDecimal.ZERO.subtract(this.xxBal));
			this.info("短繳金額= " + baTxVo.getUnPaidAmt());
		}
		this.baTxList.add(baTxVo);
	}

	private int gettingRpFacmNo(int CustNo, int FacmNo, TitaVo titaVo) {
		// 無應繳資料 ---> 抓資負明細科目
		int minFacmNo = FacmNo;
		if (this.baTxList == null || this.baTxList.size() == 0) {
			this.baTxList = new ArrayList<BaTxVo>();

			Slice<AcReceivable> srvList = null;
			rvList = new ArrayList<AcReceivable>();
			if (FacmNo == 0)
				srvList = acReceivableService.acrvFacmNoRange(0, CustNo, 1, 0, 999, this.index, Integer.MAX_VALUE,
						titaVo); // 銷帳記號
			// 0-未銷,
			// 業務科目記號
			// 1:
			// 資負明細科目
			else
				srvList = acReceivableService.acrvFacmNoRange(0, CustNo, 1, FacmNo, FacmNo, this.index,
						Integer.MAX_VALUE, titaVo);
			rvList = srvList == null ? null : srvList.getContent();

			if (rvList != null) {
				for (int i = 0; i < rvList.size(); i++) {
					if (rvList.get(i).getFacmNo() < minFacmNo || minFacmNo == 0)
						minFacmNo = rvList.get(i).getFacmNo();
				}
			}
		} else {
			// 最小值有帳優先
			for (int i = 0; i < this.baTxList.size(); i++) {
				if ((this.baTxList.get(i).getFacmNo() < minFacmNo || minFacmNo == 0)
						&& this.baTxList.get(i).getAcctAmt().compareTo(BigDecimal.ZERO) > 0)
					minFacmNo = this.baTxList.get(i).getFacmNo();
			}
			if (minFacmNo == 0) {
				for (int i = 0; i < this.baTxList.size(); i++) {
					if (this.baTxList.get(i).getFacmNo() < minFacmNo || minFacmNo == 0)
						minFacmNo = this.baTxList.get(i).getFacmNo();
				}
			}
		}
		return minFacmNo;
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
					} else if ("ICR".equals(rv.getAcctCode())) { // ICR 應收利息－放款部
						if (iFacmNo == 0 || iFacmNo == rv.getFacmNo()) {
							baTxVo.setDataKind(6); // 6.掛帳利息(逆向貸款)
							this.openInterest = this.openInterest.add(rv.getRvBal());
						} else
							baTxVo.setDataKind(0);
						baTxVo.setDbCr("D");
					} else {
						if (iFacmNo == 0 || iFacmNo == rv.getFacmNo()) {
							baTxVo.setPayIntDate(rv.getOpenAcDate()); // 應繳息日、應繳日
							baTxVo.setDbCr("C");
							if ("Z".equals(rv.getAcctCode().substring(0, 1))) {
								// 費用不含短繳
								if (iRepayType >= 4 && iRepayType < 99) {
									baTxVo.setDataKind(6); // 6.另收欠款(費用收取之短繳期金)
								} else {
									baTxVo.setDataKind(1); // 1.應收費用+未收費用+短繳期金
									baTxVo.setRepayType(1); // 01-期款
									baTxVo.setPrincipal(rv.getRvBal()); // // 本金
									baTxVo.setBormNo(parse.stringToInteger(rv.getRvNo())); // 短繳期金有撥款序號
									this.shortfall = this.shortfall.add(rv.getRvBal());
									this.shortfallPrincipal = this.shortfallPrincipal.add(rv.getRvBal());
								}
							} else if ("I".equals(rv.getAcctCode().substring(0, 1))) {
								// 費用不含短繳
								if (iRepayType >= 4 && iRepayType < 99) {
									baTxVo.setDataKind(6); // 6.另收欠款(費用收取之短繳期金)
								} else {
									baTxVo.setDataKind(1); // 1.應收費用+未收費用+短繳期金
									baTxVo.setRepayType(1); // 01-期款
									baTxVo.setInterest(rv.getRvBal()); // 利息
									baTxVo.setBormNo(parse.stringToInteger(rv.getRvNo())); // 短繳利息有撥款序號
									this.shortfall = this.shortfall.add(rv.getRvBal());
									this.shortfallInterest = this.shortfallInterest.add(rv.getRvBal());
								}
							} else if ("YOP".equals(rv.getAcctCode())) {
								// 費用不含清償違約金
								if (iRepayType >= 4 && iRepayType < 99) {
									baTxVo.setDataKind(6); // 6.另收欠款(費用收取之短繳期金)
								} else {
									baTxVo.setDataKind(1); // 1.應收費用+未收費用+短繳期金
									baTxVo.setRepayType(1); // 01-期款
									baTxVo.setCloseBreachAmt(rv.getRvBal()); // 清償違約金
									this.shortfall = this.shortfall.add(rv.getRvBal());
									this.shortCloseBreach = this.shortCloseBreach.add(rv.getRvBal());
								}
							} else {
								switch (rv.getAcctCode()) {
								case "F10": // F10 帳管費
									baTxVo.setRepayType(4); // 04-帳管費
									baTxVo.setDataKind(1); // 1.應收費用+未收費用+短繳期金
									this.acctFee = this.acctFee.add(rv.getRvBal());
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
								default:
									baTxVo.setDataKind(0);
									baTxVo.setRepayType(0);
								}
							}
						}
					}
				}
				this.info("baTxVo =" + baTxVo.toString());
				if (baTxVo.getDataKind() > 0) {
					this.baTxList.add(baTxVo);
					if (baTxVo.getDataKind() == 1) {
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
	 * 部分還款金額
	 * 
	 * @return ..
	 */
	public BigDecimal getExtraRepay() {
		return extraRepayAmt;
	}

	/**
	 * 是否內含利息
	 * 
	 * @return ..
	 */
	public String getIncludeIntFlag() {
		return includeIntFlag;
	}

	/**
	 * 利息是否可欠繳
	 * 
	 * @return ..
	 */
	public String getUnpaidIntFlag() {
		return unpaidIntFlag;
	}

}
