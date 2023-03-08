package com.st1.itx.util.common;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.st1.itx.Exception.DBException;
import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.TempVo;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.dataVO.TotaVo;
import com.st1.itx.db.domain.AcDetail;
import com.st1.itx.db.domain.AcReceivable;
import com.st1.itx.db.domain.LoanBorTx;
import com.st1.itx.db.domain.LoanBorTxId;
import com.st1.itx.db.service.CdCodeService;
import com.st1.itx.db.service.LoanBorTxService;
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.common.data.BaTxVo;
import com.st1.itx.util.format.FormatUtil;
import com.st1.itx.util.parse.Parse;

/**
 * 回收帳務處理<BR>
 * 1.settleLoanRun 放款回收交易帳務及放款交易內容檔處理<BR>
 * 2.settleTempRun 暫收款交易帳務及更新放款交易內容檔處理 <BR>
 * 3.updBorTxAcDetail 更新放款明細檔及帳務明細檔關聯欄<BR>
 * 4.settleOverflow 累溢收(暫收貸)帳務處理
 * 
 * @author st1
 *
 */
@Component("AcRepayCom")
@Scope("prototype")
public class AcRepayCom extends TradeBuffer {

	@Autowired
	public CdCodeService cdCodeService;
	@Autowired
	public LoanBorTxService loanBorTxService;
	@Autowired
	private LoanCom loanCom;
	@Autowired
	private AcDetailCom acDetailCom;
	@Autowired
	private AcReceivableCom acReceivableCom;
	@Autowired
	public Parse parse;

	private TitaVo titaVo;
	private List<AcDetail> lAcDetail = new ArrayList<AcDetail>();
	private List<LoanBorTx> lLoanBorTx = new ArrayList<LoanBorTx>();
	private ArrayList<BaTxVo> baTxList = new ArrayList<BaTxVo>();
	private List<AcReceivable> lAcReceivable = new ArrayList<AcReceivable>();
	private AcDetail acDetail;
	private int iRepayCode; // 還款來源
	private int iEntryDate; // 入帳日;
	private int iCaseCloseCode; // 結案區分
	private BigDecimal wkTxAmtRemaind = BigDecimal.ZERO; // 交易餘額
	private BigDecimal wkTempAmtRemaind = BigDecimal.ZERO; // 暫收餘額
	private HashMap<String, Integer> mapBorxNo = new HashMap<>();

	// initialize variable
	@PostConstruct
	public void init() {
		this.info("AcRepayCom init ...");
		this.lAcDetail = new ArrayList<AcDetail>();
		this.lLoanBorTx = new ArrayList<LoanBorTx>();
		this.baTxList = new ArrayList<BaTxVo>();
		this.lAcReceivable = new ArrayList<AcReceivable>();
		this.iRepayCode = 0;
		this.iEntryDate = 0;
		this.iCaseCloseCode = 0;
		this.wkTxAmtRemaind = BigDecimal.ZERO;
		this.wkTempAmtRemaind = BigDecimal.ZERO;
		this.mapBorxNo = new HashMap<>();
	}

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("AcRepayCom run ...");
		return null;
	}

	/**
	 * 放款回收交易新增帳務及更新放款交易內容檔<BR>
	 * 
	 * @param ilLoanBorTx 放款交易內容檔清單
	 * @param iBaTxList   還款入帳試算表
	 * @param titaVo      TitaVo
	 * @throws LogicException ....
	 */
	public void settleLoanRun(List<LoanBorTx> ilLoanBorTx, ArrayList<BaTxVo> iBaTxList, TitaVo titaVo)
			throws LogicException {
		this.info("settleLoanRun ... ");

		// step 0. 設定出帳使用資料
		if (ilLoanBorTx.size() == 0) {
			return;
		}
		this.titaVo = titaVo;
		this.baTxList = iBaTxList;

		this.iRepayCode = (ilLoanBorTx.get(0).getRepayCode());
		this.iEntryDate = (ilLoanBorTx.get(0).getEntryDate());
		if ("L3420".equals(titaVo.getTxcd())) {
			iCaseCloseCode = this.parse.stringToInteger(titaVo.getParam("CaseCloseCode"));
		}

		loanCom.setTxBuffer(this.txBuffer);

		// step 1. 短繳銷帳檔處理
		updUnpaidAmt(ilLoanBorTx, titaVo);
		if (this.lAcReceivable.size() > 0) {
			acReceivableCom.setTxBuffer(this.getTxBuffer());
			acReceivableCom.mnt(0, this.lAcReceivable, titaVo); // 0-起帳
		}

		// 訂正只處理短繳
		if (titaVo.isHcodeErase()) {
			return;
		}

		// 多段式控制
		if (!this.txBuffer.getTxCom().isBookAcYes()) {
			this.info("skip isBookAcYes=" + this.txBuffer.getTxCom().isBookAcYes());
			return;
		}

		// step 2. 收付欄出帳
		settlePayment(titaVo);

		// step 3. 費用出帳
		settleFee(false, "", titaVo);

		// step 4. 本金利息出帳、計算交易明細的交易金額及暫收借金額
		for (LoanBorTx tx : ilLoanBorTx) {
			BigDecimal debitAmt = settleLoanAmt(tx); // 本金利息出帳
			if (debitAmt.compareTo(BigDecimal.ZERO) > 0) {
				tx.setTxAmt(debitAmt);
			} else {
				BigDecimal repayAmt = updBorTxAcDetail(tx, this.lAcDetail, titaVo); // 更新放款明細檔及帳務明細檔關聯欄
				BigDecimal txAmt = BigDecimal.ZERO;
				BigDecimal tempAmt = BigDecimal.ZERO;
				if (this.wkTempAmtRemaind.compareTo(repayAmt) > 0) {
					tempAmt = repayAmt;
					this.wkTempAmtRemaind = this.wkTempAmtRemaind.subtract(tempAmt);
				} else {
					tempAmt = this.wkTempAmtRemaind;
					this.wkTempAmtRemaind = BigDecimal.ZERO;
					if (this.wkTxAmtRemaind.compareTo(repayAmt.subtract(tempAmt)) > 0) {
						txAmt = repayAmt.subtract(tempAmt);
						this.wkTxAmtRemaind = this.wkTxAmtRemaind.subtract(txAmt);
					} else {
						txAmt = this.wkTxAmtRemaind;
						this.wkTxAmtRemaind = BigDecimal.ZERO;
					}
				}
				tx.setTxAmt(txAmt);
				tx.setTempAmt(tempAmt);
			}
			this.lLoanBorTx.add(tx);
		}

		// step 5. 溢收款出帳、更新尾筆交易金額、暫收借金額、暫收貸金額
		LoanBorTx tx = this.lLoanBorTx.get(this.lLoanBorTx.size() - 1);
		BigDecimal overflow = settleOverflow(tx, this.lAcDetail, titaVo);
		tx.setTxAmt(tx.getTxAmt().add(this.wkTxAmtRemaind));
		tx.setTempAmt(tx.getTempAmt().add(this.wkTempAmtRemaind));
		tx.setOverflow(overflow);

		// step 6. 將本戶累溢收放入Json Field(Excessive)
		this.lLoanBorTx = setExcessive(this.lLoanBorTx, iBaTxList, this.lAcDetail, titaVo);

		// step 7. 暫收轉額度計算並出帳
		this.lAcDetail = loanTransfer(this.lLoanBorTx, this.lAcDetail, titaVo);

		// step 8. 設定交易內容檔序號(borxNo)、入帳順序(AcSeq)
		int lxAcseq = 0;
		for (LoanBorTx lx : this.lLoanBorTx) {
			lxAcseq++;
			lx.setAcSeq(lxAcseq);
			if (lx.getBormNo() == 0) {
				setFacmBorxNo(lx, titaVo);
			}
			this.info(lx.toString());
		}

		// step 9. 寫入放款交易內容檔
		try {
			loanBorTxService.insertAll(this.lLoanBorTx);
		} catch (DBException e) {
			throw new LogicException(titaVo, "E0005", "放款交易內容檔 " + e.getErrorMsg()); // 新增資料時，發生錯誤
		}

		// step 10. 產生會計分錄
		this.txBuffer.setAcDetailList(this.lAcDetail);
		acDetailCom.setTxBuffer(this.txBuffer);
		acDetailCom.run(titaVo);

	}

	/**
	 * 暫收款交易新增帳務及更新放款交易內容檔
	 * 
	 * @param iTempAmt      暫收款金額
	 * @param ilLoanBorTx   放款交易內容檔清單
	 * @param iBaTxList     還款入帳試算表
	 * @param iFeeList      費用明細
	 * @param iAcDetailList 帳務明細
	 * @param titaVo        TitaVo
	 * @throws LogicException ....
	 */
	public void settleTempRun(List<LoanBorTx> ilLoanBorTx, ArrayList<BaTxVo> iBaTxList, List<AcDetail> iAcDetailList,
			TitaVo titaVo) throws LogicException {
		this.info("settleTempRun ... ilLoanBorTx.size=" + ilLoanBorTx.size() + ", iBaTxList.size=" + iBaTxList.size()
				+ ", iAcDetailList.size=" + iAcDetailList.size());
		if (this.baTxList != null) {
			for (BaTxVo ba : this.baTxList) {
				this.info("input " + ba.toString());
			}
		}
		for (AcDetail ac : iAcDetailList) {
			this.info("iinput " + ac.getDbCr() + " " + ac.getAcctCode() + " "
					+ FormatUtil.padLeft("" + ac.getTxAmt(), 11) + " " + ac.getCustNo() + "-" + ac.getFacmNo() + "-"
					+ ac.getBormNo() + " " + ac.getSumNo() + " " + ac.getRvNo());
		}
		for (LoanBorTx tx : ilLoanBorTx) {
			this.info("input " + tx.toString());
		}

		// step 0. 設定出帳使用資料
		this.titaVo = titaVo;
		this.baTxList = iBaTxList;
		if (ilLoanBorTx.size() > 0) {
			this.iRepayCode = ilLoanBorTx.get(0).getRepayCode();
			this.iEntryDate = ilLoanBorTx.get(0).getEntryDate();
		} else {
			this.iRepayCode = 90; // 暫收抵繳
			this.iEntryDate = titaVo.getEntDyI();

		}
		loanCom.setTxBuffer(this.txBuffer);

		// 訂正不處理
		if (titaVo.isHcodeErase()) {
			return;
		}

		// 多段式控制
		if (!this.txBuffer.getTxCom().isBookAcYes()) {
			this.info("skip isBookAcYes=" + this.txBuffer.getTxCom().isBookAcYes());
			return;
		}

		// step 1. 收付欄出帳
		if ("L3210".equals(titaVo.getTxcd())) {
			settlePayment(titaVo);
		}

		// step 2. 搬交易產生的帳務
		for (AcDetail ac : iAcDetailList) {
			this.lAcDetail.add(ac);
		}

		// step 3. 溢收款出帳、更新尾筆交易金額、暫收借金額、暫收貸金額 => 僅L3210暫收款登錄
		BigDecimal overflow = BigDecimal.ZERO;
		int overFacmNo = 0;
		for (LoanBorTx tx : ilLoanBorTx) {
			// 暫收款登錄溢收
			if ("L3210".equals(titaVo.getTxcd())) {
				updBorTxAcDetail(tx, this.lAcDetail, titaVo); // 更新放款明細檔及帳務明細檔關聯欄
				int iTempReasonCode = this.parse.stringToInteger(titaVo.getParam("TempReasonCode"));
				// 0: 債協暫收款 10: AML凍結／未確定
				if (iTempReasonCode == 0 || iTempReasonCode == 10) {
					tx.setOverflow(tx.getTxAmt());
				} else {
					tx.setTxAmt(this.wkTxAmtRemaind);
					tx.setTempAmt(this.wkTempAmtRemaind);
					// 貸方：溢收款
					overflow = settleOverflow(tx, this.lAcDetail, titaVo);
					tx.setOverflow(overflow);
					this.wkTxAmtRemaind = BigDecimal.ZERO;
					this.wkTempAmtRemaind = BigDecimal.ZERO;
					overFacmNo = this.lAcDetail.get(this.lAcDetail.size() - 1).getFacmNo();
				}
			}
			this.lLoanBorTx.add(tx);
		}

		// step 4. 費用出帳
		BigDecimal totalFee = BigDecimal.ZERO;
		int splseq = this.lAcDetail.size();
		String iNote = "";
		if ("L3230".equals(titaVo.getTxcd())) {
			iNote = titaVo.getParam("Description");
		}
		if ("L3210".equals(titaVo.getTxcd()) || "L3230".equals(titaVo.getTxcd())) {
			totalFee = settleFee(true, iNote, titaVo);
		}

		// step 5. 暫收抵繳出帳 ==> 僅L3210暫收款登錄
		if (totalFee.compareTo(BigDecimal.ZERO) > 0 && "L3210".equals(titaVo.getTxcd())) {
			AcDetail acDetail = new AcDetail();
			acDetail.setDbCr("D");
			acDetail.setAcctCode("TAV");
			acDetail.setTxAmt(totalFee);
			acDetail.setCustNo(this.lAcDetail.get(0).getCustNo());
			acDetail.setFacmNo(overFacmNo);
			acDetail.setBormNo(0);
			acDetail.setSumNo("090"); // 暫收抵繳
			this.lAcDetail.add(acDetail);
		}

		// step 6. 暫收轉額度計算並出帳 => 如有費用抵繳
		if (totalFee.compareTo(BigDecimal.ZERO) > 0) {
			this.lAcDetail = tempTransfer(splseq, this.lLoanBorTx, this.lAcDetail, titaVo);
		}

		// step 7. 將本戶累溢收放入Json Field(Excessive)
		this.lLoanBorTx = setExcessive(this.lLoanBorTx, iBaTxList, this.lAcDetail, titaVo);

		// step 8. 設定交易內容檔序號(borxNo)、入帳順序(AcSeq)
		int lxAcseq = 0;
		for (LoanBorTx lx : this.lLoanBorTx) {
			lxAcseq++;
			lx.setAcSeq(lxAcseq);
			if (lx.getBormNo() == 0) {
				setFacmBorxNo(lx, titaVo);
			}
			this.info(lx.toString());
		}

		// step 9. 寫入放款交易內容檔
		try {
			loanBorTxService.insertAll(this.lLoanBorTx);
		} catch (DBException e) {
			throw new LogicException(titaVo, "E0005", "放款交易內容檔 " + e.getErrorMsg()); // 新增資料時，發生錯誤
		}

		// step 10. 產生會計分錄
		this.txBuffer.setAcDetailList(this.lAcDetail);
		acDetailCom.setTxBuffer(this.txBuffer);
		acDetailCom.run(titaVo);
	}

	// 收付欄出帳
	private void settlePayment(TitaVo titaVo) throws LogicException {
		// 借方：收付欄
		for (int i = 1; i <= 50; i++) {
			/* 還款來源／撥款方式為 0 者跳出 */
			if (titaVo.get("RpCode" + i) == null || parse.stringToInteger(titaVo.getParam("RpCode" + i)) == 0)
				break;
			if (parse.stringToBigDecimal(titaVo.getParam("RpAmt" + i)).compareTo(BigDecimal.ZERO) > 0) {
				addPayment(i, titaVo);
			}
		}
		for (AcDetail ac : this.lAcDetail) {
			int sumNo = parse.stringToInteger(ac.getSumNo());
			if ("D".equals(ac.getDbCr())) {
				if (sumNo >= 98) {
					this.wkTxAmtRemaind = wkTxAmtRemaind.add(ac.getTxAmt());
				} else {
					this.wkTempAmtRemaind = this.wkTempAmtRemaind.add(ac.getTxAmt());
				}
			} else {
				if (sumNo >= 98) {
					this.wkTxAmtRemaind = this.wkTxAmtRemaind.subtract(ac.getTxAmt());
				} else {
					this.wkTempAmtRemaind = this.wkTempAmtRemaind.subtract(ac.getTxAmt());
				}
			}
			// 存入暫收為轉帳
			if ("L3210".equals(titaVo.getTxcd()) && sumNo == 90) {
				ac.setSumNo("092");
			}
		}
		this.info("this.wkTxAmtRemaind=" + wkTxAmtRemaind + ", this.wkTempAmtRemaind=" + this.wkTempAmtRemaind);
	}

	// 費用出帳
	private BigDecimal settleFee(boolean isTempRepay, String iNote, TitaVo titaVo) throws LogicException {
		// 依應繳試算 List <BaTxVo>內費用類別
		if (this.baTxList == null) {
			return BigDecimal.ZERO;
		}
		BigDecimal totalFee = BigDecimal.ZERO;
		// 還放款為依序抵繳、暫收款為抵繳費用金額
		for (BaTxVo ba : this.baTxList) {
			if (ba.getRepayType() >= 4 && ba.getAcctAmt().compareTo(BigDecimal.ZERO) > 0) {
				totalFee = totalFee.add(ba.getAcctAmt());
				LoanBorTx tx = addFeeBorTxRoutine(ba, iRepayCode, iNote, iEntryDate, new TempVo(), titaVo);
				settleFeeAmt(ba, tx); // 費用出帳
				if (isTempRepay) {
					tx.setTxAmt(BigDecimal.ZERO);
					tx.setTempAmt(ba.getAcctAmt());
				} else {
					BigDecimal txAmt = BigDecimal.ZERO;
					BigDecimal tempAmt = BigDecimal.ZERO;
					BigDecimal repayAmt = updBorTxAcDetail(tx, this.lAcDetail, titaVo); // 更新放款明細檔及帳務明細檔關聯欄
					if (this.wkTempAmtRemaind.compareTo(repayAmt) > 0) {
						tempAmt = repayAmt;
						this.wkTempAmtRemaind = this.wkTempAmtRemaind.subtract(tempAmt);
					} else {
						tempAmt = this.wkTempAmtRemaind;
						this.wkTempAmtRemaind = BigDecimal.ZERO;
						if (this.wkTxAmtRemaind.compareTo(repayAmt.subtract(tempAmt)) > 0) {
							txAmt = repayAmt.subtract(tempAmt);
							this.wkTxAmtRemaind = this.wkTxAmtRemaind.subtract(txAmt);
						} else {
							txAmt = this.wkTxAmtRemaind;
							this.wkTxAmtRemaind = BigDecimal.ZERO;
						}
					}
					tx.setTxAmt(txAmt);
					tx.setTempAmt(tempAmt);
				}
				ba.setAcctAmt(BigDecimal.ZERO);
				this.lLoanBorTx.add(tx);
			}
		}
		return totalFee;
	}

	// 短繳銷帳檔處理
	private void updUnpaidAmt(List<LoanBorTx> ilLoanBorTx, TitaVo titaVo) throws LogicException {
		for (LoanBorTx tx : ilLoanBorTx) {
			// 短繳利息, 新增銷帳檔
			if (tx.getUnpaidInterest().compareTo(BigDecimal.ZERO) > 0) {
				acRvUnpaidAmt(tx, loanCom.setIntAcctCode(tx.getAcctCode()), tx.getUnpaidInterest());
			}

			// 短繳本金處理, 新增銷帳檔
			if (tx.getUnpaidPrincipal().compareTo(BigDecimal.ZERO) > 0) {
				acRvUnpaidAmt(tx, loanCom.setShortPrinAcctCode(tx.getAcctCode()), tx.getUnpaidPrincipal());
			}

			// 短繳清償違約金處理, 新增銷帳檔
			if (tx.getUnpaidCloseBreach().compareTo(BigDecimal.ZERO) > 0) {
				acRvUnpaidAmt(tx, "IOP", tx.getUnpaidCloseBreach());
			}
		}
	}

	// 短繳金額
	private void acRvUnpaidAmt(LoanBorTx tx, String acctCode, BigDecimal shortAmt) throws LogicException {
		this.info("acRvUnpaidAmt ...");
		AcReceivable tAcReceivable = new AcReceivable();
		tAcReceivable.setReceivableFlag(4); // 短繳期金
		tAcReceivable.setAcctCode(acctCode);
		tAcReceivable.setCustNo(tx.getCustNo());
		if ("L3410".equals(titaVo.getTxcd())) {
			TempVo txTempVo = new TempVo();
			txTempVo = txTempVo.getVo(tx.getOtherFields());
			tAcReceivable.setFacmNo(parse.stringToInteger(txTempVo.get("NewFacmNo")));
			tAcReceivable.setRvNo(parse.IntegerToString(parse.stringToInteger(txTempVo.get("NewFacmNo")), 3));

		} else {
			tAcReceivable.setFacmNo(tx.getFacmNo());
			tAcReceivable.setRvNo(parse.IntegerToString(tx.getBormNo(), 3) + "-" + tx.getBorxNo());
		}
		tAcReceivable.setRvAmt(shortAmt);
		this.lAcReceivable.add(tAcReceivable);
	}

	/* 收付欄出帳 */
	private void addPayment(int i, TitaVo titaVo) throws LogicException {
		String iRpFlag = titaVo.getParam("RpFlag"); /* 收付記號 DECIMAL(1) */
		// 收付金額
		BigDecimal rpAmt = parse.stringToBigDecimal(titaVo.getParam("RpAmt" + i));
		if (rpAmt.compareTo(BigDecimal.ZERO) == 0) {
			return;
		}

		acDetail = new AcDetail();

		acDetail.setTxAmt(rpAmt);

		// 戶號 0123456-890-234
		acDetail.setCustNo(parse.stringToInteger(titaVo.getMrKey().substring(0, 7)));
		// 額度
		// 借貸別 應收-D 應付-C
		if ("1".equals(iRpFlag)) {
			acDetail.setDbCr("D");
			acDetail.setFacmNo(parse.stringToInteger(titaVo.getParam("RpFacmNo" + i)));
		} else {
			acDetail.setDbCr("C");
			if (titaVo.getMrKey().length() >= 11 && titaVo.getMrKey().substring(7, 8).equals("-")) {
				acDetail.setFacmNo(parse.stringToInteger(titaVo.getMrKey().substring(8, 11)));
			}
			// 撥款序號
			if (titaVo.getMrKey().length() >= 15 && titaVo.getMrKey().substring(11, 12).equals("-")) {
				acDetail.setBormNo(parse.stringToInteger(titaVo.getMrKey().substring(12, 15)));
			}
		}

		// 彙總別：撥還共用(0XX)／還款來源(1xx)
// 1-應收  RpAcctCode (整批入帳檔帶入)
//	       101.匯款轉帳 102.銀行扣款 103.員工扣款 104.支票兌現
//	       105.法院扣薪 106.理賠金 107.代收款-債權協商 109.其他 111.匯款轉帳預先作業

// 0-共用
//	      090.暫收抵繳     (額度)       TAV 暫收款－可抵繳
//	      091:借新還舊                  TRO 暫收款－借新還舊
//	      092:暫收轉帳     (戶號+額度)  TAV 暫收款－可抵繳
//	      093:繳抽退票                  TCK 暫收款－支票
//	      094:轉債協暫收款 (戶號)       T1x 債協暫收款      
//	      095:轉債協退還款 (戶號)       T2x 債協退還款  
		if (parse.stringToInteger(titaVo.getParam("RpCode" + i)) >= 90) {
			acDetail.setSumNo("0" + FormatUtil.pad9(titaVo.getParam("RpCode" + i), 2));
		} else {
			acDetail.setSumNo("1" + FormatUtil.pad9(titaVo.getParam("RpCode" + i), 2));
			acDetail.setAcctCode(titaVo.getParam("RpAcctCode" + i));
			String rpAcCode = FormatUtil.padX(titaVo.getParam("RpAcCode" + i).trim(), 18);
			acDetail.setAcNoCode(rpAcCode.substring(0, 11)); // 會科科子細目 11+5+2
			acDetail.setAcSubCode(rpAcCode.substring(11, 16));
			acDetail.setAcDtlCode(rpAcCode.substring(16, 18));
		}

		acDetail.setRvNo(titaVo.get("RpRvno" + i)); /* 銷帳編號 VARCHAR2(30) */

		switch (acDetail.getSumNo()) {
		case "090":
			acDetail.setAcctCode("TAV");
			acDetail.setFacmNo(parse.stringToInteger(titaVo.getParam("RpFacmNo" + i)));
			break;
		case "091":
			acDetail.setAcctCode("TRO");
			// 1:應收 L3410 結案 D 500,000 FacmNo002 原額度002
			// 2:應付 L3100 撥貸 C 500,000 FacmNo002 新額度004，原額度002
			acDetail.setRvNo("FacmNo" + titaVo.getMrKey().substring(8, 11));
			acDetail.setFacmNo(parse.stringToInteger(titaVo.getMrKey().substring(8, 11)));
			break;
		case "092": // 已由L3230程式處理
			break;
		case "093":
			acDetail.setAcctCode("TCK");
			acDetail.setFacmNo(parse.stringToInteger(titaVo.getParam("RpFacmNo" + i)));
			break;
		case "094": // 已由L3230程式處理
			break;
		case "095": // 已由L3230程式處理
			break;
		case "101": // 101.匯款轉帳
			acDetail.setAcctCode("P03");
			break;
		case "102": // 102.銀行扣款 C01 暫收款－非核心資金運用 核心銷帳碼 0010060yyymmdd (銀扣 ACH), 郵局 P01
			if ("C01".equals(acDetail.getAcctCode())) {
				acDetail.setRvNo("0010060" + titaVo.getEntDyI());
			}
			break;
		case "103": // 103.員工扣款 TEM
			break;
		case "104": // 104.支票兌現 TCK
			int iChequeAcct = this.parse.stringToInteger(acDetail.getRvNo().substring(0, 9));
			int iChequeNo = this.parse.stringToInteger(acDetail.getRvNo().substring(10, 17));
			titaVo.putParam("ChequeAcct", iChequeAcct);
			titaVo.putParam("ChequeNo", iChequeNo);
			break;
		// 其他還款來源有核心銷帳碼
		case "105": // 105.法院扣薪
		case "106": // 106.理賠金
		case "107": // 107.代收款-債權協商(已取消)
		case "109": // 109.其他
			break;
		case "111": // 111.匯款轉帳預先作業
			break;
		}
		if (acDetail.getTxAmt().compareTo(BigDecimal.ZERO) > 0) {
			this.lAcDetail.add(acDetail);
		}
	}

	/**
	 * 本戶累溢收放至LoanBorTx
	 * 
	 * @param ilLoanBorTx ArrayList of lLoanBorTx
	 * @param iBaTxList   ArrayList of BaTxVo
	 * @param iAcList     List of AcDetail
	 * @param titaVo      TitaVo
	 * @return ilLoanBorTx ArrayList of lLoanBorTx
	 * @throws LogicException ....
	 */
	private List<LoanBorTx> setExcessive(List<LoanBorTx> ilLoanBorTx, ArrayList<BaTxVo> iBaTxList,
			List<AcDetail> iAcList, TitaVo titaVo) throws LogicException {
		BigDecimal wkExcessive = BigDecimal.ZERO;
		for (BaTxVo ba : this.baTxList) {
			if (ba.getDataKind() == 3 || ba.getDataKind() == 5) {
				wkExcessive = wkExcessive.add(ba.getUnPaidAmt());
			}
		}
		for (AcDetail ac : iAcList) {
			if ("TAV".equals(ac.getAcctCode())
					&& ac.getCustNo() != this.getTxBuffer().getSystemParas().getLoanDeptCustNo()) {
				if ("C".equals(ac.getDbCr())) {
					wkExcessive = wkExcessive.add(ac.getTxAmt());
				} else {
					wkExcessive = wkExcessive.subtract(ac.getTxAmt());
				}
			}
		}
		if (wkExcessive.compareTo(BigDecimal.ZERO) > 0) {
			for (LoanBorTx tx : ilLoanBorTx) {
				TempVo tTempVo = new TempVo();
				tTempVo = tTempVo.getVo(tx.getOtherFields());
				tTempVo.putParam("Excessive", wkExcessive);
				tx.setOtherFields(tTempVo.getJsonString());
			}
		}

		this.info("computeExcessive=" + wkExcessive);
		return ilLoanBorTx;
	}

	/**
	 * 累溢收(暫收貸)帳務處理
	 * 
	 * @param tx      LoanBorTx
	 * @param iAcList List of AcDetail
	 * @param titaVo  titaVo
	 * @return 累溢收金額
	 * @throws LogicException ....
	 */

	public BigDecimal settleOverflow(LoanBorTx tx, List<AcDetail> iAcList, TitaVo titaVo) throws LogicException {
		this.titaVo = titaVo;
		this.lAcDetail = iAcList;
		this.info("settleOverflow ..." + this.lAcDetail.size());

		BigDecimal wkOverflow = BigDecimal.ZERO;

		// 借貸差
		for (AcDetail ac : this.lAcDetail) {
			if ("D".equals(ac.getDbCr())) {
				wkOverflow = wkOverflow.add(ac.getTxAmt());
			} else {
				wkOverflow = wkOverflow.subtract((ac.getTxAmt()));
			}
		}

		if (wkOverflow.compareTo(BigDecimal.ZERO) > 0) {
			AcDetail acDetail = new AcDetail();
			acDetail.setDbCr("C");
			acDetail.setAcctCode("TAV");
			acDetail.setTxAmt(wkOverflow);
			acDetail.setCustNo(tx.getCustNo());
			acDetail.setFacmNo(tx.getFacmNo());
			acDetail.setBormNo(0);
			acDetail.setSumNo("092"); // 暫收轉帳
			this.lAcDetail.add(acDetail);
		}

		this.info("settleOverflow end" + ", wkOverflow=" + wkOverflow);
		return wkOverflow;

	}

	/**
	 * 更新放款明細檔及帳務明細檔關聯欄
	 * 
	 * @param tx      LoanBorTx
	 * @param iAcList List of AcDetail
	 * @param titaVo  TitaVo
	 * @return 作帳金額
	 * @throws LogicException ...
	 */
	public BigDecimal updBorTxAcDetail(LoanBorTx tx, List<AcDetail> iAcList, TitaVo titaVo) throws LogicException {

		this.info("updBorTxAcDetail ... RepayCode=" + tx.getRepayCode() + ", BacthNo=" + titaVo.getBacthNo());
		this.titaVo = titaVo;
		this.lAcDetail = iAcList;
		BigDecimal repayAmt = BigDecimal.ZERO;

		// 彙總傳票批號
		if (tx.getRepayCode() >= 1 && tx.getRepayCode() <= 4) {
			if (titaVo.get("BATCHNO") != null && titaVo.get("BATCHNO").trim().length() == 6
					&& "BATX".equals(titaVo.get("BATCHNO").substring(0, 4))) {
				tx.setSlipSumNo(parse.stringToInteger(titaVo.get("BATCHNO").substring(4, 6)));
			}
		}
		// 收付欄 TempVo
		TempVo tTempVo = new TempVo();
		tTempVo = tTempVo.getVo(tx.getOtherFields());
		// 整批
		if (titaVo.getBacthNo().trim() != "") {
			tTempVo.putParam("BatchNo", titaVo.getBacthNo()); // 整批批號
			tTempVo.putParam("DetailSeq", titaVo.get("RpDetailSeq1")); // 明細序號
			tTempVo.putParam("ReconCode", titaVo.get("RpAcctCode1") == null ? "" : titaVo.get("RpAcctCode1").trim()); // 對帳類別
			tTempVo.putParam("DscptCode", titaVo.get("RpDscpt1")); // 摘要代碼
			tTempVo.putParam("RpRvno", titaVo.get("RpRvno1")); // 銷帳編號
			if (titaVo.get("ResvEntdy") != null) {
				tTempVo.putParam("ResvEntdy", titaVo.get("ResvEntdy")); // 隔日訂正會計日(原始入帳會計日)
				tTempVo.putParam("ResvTxSeq", titaVo.get("ResvTxSeq")); // 隔日訂正交易序號(原始入帳交易序號)
			}
		}
		// 支票繳款
		if (tx.getRepayCode() == 4) {
			tTempVo.putParam("ChequeAcctNo", titaVo.get("ChequeAcctNo")); // 支票帳號
			tTempVo.putParam("ChequeNo", titaVo.get("ChequeNo")); // 支票號碼
			tTempVo.putParam("StampFreeAmt",
					tx.getInterest().add(tx.getDelayInt()).add(tx.getBreachAmt()).add(tx.getCloseBreachAmt()));// 利息免印花稅
		}
		tx.setOtherFields(tTempVo.getJsonString());
		int acSeq = 0;
		for (AcDetail ac : this.lAcDetail) {
			acSeq++;
			if (ac.getAcSeq() > 0) {
				continue;
			}
			ac.setAcSeq(acSeq); // 分錄序號
			ac.setSlipSumNo(tx.getSlipSumNo()); // 彙總傳票批號
			// 作帳金額
			if (parse.stringToInteger(ac.getSumNo()) == 0) {
				if ("C".equals(ac.getDbCr())) {
					repayAmt = repayAmt.add(ac.getTxAmt());
				} else {
					repayAmt = repayAmt.subtract(ac.getTxAmt());
				}
			}
		}
		this.info("updBorTxAcDetail end repayAmt=" + repayAmt + ", AcSeq=" + tx.getAcSeq() + ", SlipSumNo="
				+ tx.getSlipSumNo());
		return repayAmt;
	}

	// 暫收轉額度(暫收款登錄、銷帳)
	private List<AcDetail> tempTransfer(int splseq, List<LoanBorTx> ilLoanBorTx, List<AcDetail> iAcList, TitaVo titaVo)
			throws LogicException {

		this.info("transferTempAmt ... splseq=" + splseq);
		for (AcDetail ac : iAcList) {
			this.info(ac.getDbCr() + " " + ac.getAcctCode() + " " + FormatUtil.padLeft("" + ac.getTxAmt(), 11) + " "
					+ ac.getCustNo() + "-" + ac.getFacmNo() + "-" + ac.getBormNo() + " " + ac.getSumNo() + " "
					+ ac.getRvNo());
		}

		this.titaVo = titaVo;

		// step 1. 累計分錄中的暫收抵繳金額，搬費用前分錄、不含暫收抵繳，暫存費用分錄
		int ii = 0;
		this.lAcDetail = new ArrayList<AcDetail>();
		List<AcDetail> lAcDetailFee = new ArrayList<AcDetail>();
		HashMap<Integer, BigDecimal> tempBalMap = new HashMap<>(); // 暫收抵繳金額
		for (AcDetail ac : iAcList) {
			ii++;
			if ("090".equals(ac.getSumNo()) && "D".equals(ac.getDbCr())) {
				if (tempBalMap.get(ac.getFacmNo()) == null) {
					tempBalMap.put(ac.getFacmNo(), ac.getTxAmt());
				} else {
					tempBalMap.put(ac.getFacmNo(), tempBalMap.get(ac.getFacmNo()).add(ac.getTxAmt()));
				}
				continue;
			}
			// 費用前分錄、不含暫收抵繳
			if (ii <= splseq) {
				this.lAcDetail.add(ac);
			}
			// 暫存費用分錄
			if (ii > splseq && ac.getSumNo().isEmpty()) {
				lAcDetailFee.add(ac);
			}
		}

		// 額度使用金額
		HashMap<Integer, BigDecimal> tempAmtMap = new HashMap<>();
		// 轉額度金額
		HashMap<Integer, BigDecimal> transferAmtMap = new HashMap<>();

		// step 2. 計算費用交易明細中同的暫收借合計、可轉出金額
		BigDecimal tempAmtTotal = BigDecimal.ZERO;
		for (LoanBorTx tx : ilLoanBorTx) {
			if (tx.getFeeAmt().compareTo(BigDecimal.ZERO) > 0) {
				this.info("transferTempAmt=" + tx.toString());
				tempAmtTotal = tempAmtTotal.add(tx.getTempAmt());
				if (tempAmtMap.get(tx.getFacmNo()) == null) {
					tempAmtMap.put(tx.getFacmNo(), tx.getTempAmt());
				} else {
					tempAmtMap.put(tx.getFacmNo(), tx.getTempAmt().add(tempAmtMap.get(tx.getFacmNo())));
				}
				if (transferAmtMap.get(tx.getFacmNo()) == null) {
					transferAmtMap.put(tx.getFacmNo(), tx.getTempAmt());
				} else {
					transferAmtMap.put(tx.getFacmNo(), tx.getTempAmt().add(transferAmtMap.get(tx.getFacmNo())));
				}
			}
		}

		// step 3. 計算本額度暫收抵繳金額、本額度抵用後轉出金額
		// 本額度暫收抵繳金額
		HashMap<Integer, BigDecimal> selfTempMap = new HashMap<>();
		// 優先抵用本額度
		Set<Integer> transferSet = transferAmtMap.keySet();
		for (Iterator<Integer> itTxFacmNo = transferSet.iterator(); itTxFacmNo.hasNext();) {
			int txFacmNo = itTxFacmNo.next();
			BigDecimal txAmt = BigDecimal.ZERO;
			if (transferAmtMap.get(txFacmNo).compareTo(BigDecimal.ZERO) > 0) {
				if (tempBalMap.get(txFacmNo) != null) {
					if (tempBalMap.get(txFacmNo).compareTo(transferAmtMap.get(txFacmNo)) > 0) {
						txAmt = transferAmtMap.get(txFacmNo);
						tempBalMap.put(txFacmNo, tempBalMap.get(txFacmNo).subtract(txAmt));
						transferAmtMap.put(txFacmNo, BigDecimal.ZERO);
					} else {
						txAmt = tempBalMap.get(txFacmNo);
						transferAmtMap.put(txFacmNo, transferAmtMap.get(txFacmNo).subtract(tempBalMap.get(txFacmNo)));
						tempBalMap.put(txFacmNo, BigDecimal.ZERO);
					}
					selfTempMap.put(txFacmNo, txAmt);
					this.info("selfTemp txFacmNo=" + txFacmNo + ", Amt=" + txAmt);
				}
			}
		}

		// step 4. 本額度抵用後餘額轉出 ==> 出帳
		Set<Integer> tempBalSet = tempBalMap.keySet();
		for (Iterator<Integer> itTxFacmNo = tempBalSet.iterator(); itTxFacmNo.hasNext();) {
			int txFacmNo = itTxFacmNo.next();
			this.info("transfer out txFacmNo=" + txFacmNo + ", tempBal" + tempBalMap.get(txFacmNo));
			if (tempBalMap.get(txFacmNo).compareTo(BigDecimal.ZERO) > 0) {
				AcDetail acDetail = new AcDetail();
				acDetail.setDbCr("D");
				acDetail.setAcctCode("TAV");
				acDetail.setTxAmt(tempBalMap.get(txFacmNo));
				acDetail.setCustNo(lAcDetailFee.get(0).getCustNo());
				acDetail.setFacmNo(txFacmNo);
				acDetail.setBormNo(0);
				acDetail.setSumNo("092"); // 暫收轉帳
				this.lAcDetail.add(acDetail);
			}
		}

		// step 5. 轉入抵用其他額度 ==> 出帳
		for (Iterator<Integer> itTxFacmNo = transferSet.iterator(); itTxFacmNo.hasNext();) {
			int txFacmNo = itTxFacmNo.next();
			this.info("transfer in txFacmNo=" + txFacmNo + ", transferAmt=" + transferAmtMap.get(txFacmNo));
			if (transferAmtMap.get(txFacmNo).compareTo(BigDecimal.ZERO) > 0) {
				AcDetail acDetail = new AcDetail();
				acDetail.setDbCr("C");
				acDetail.setAcctCode("TAV");
				acDetail.setTxAmt(transferAmtMap.get(txFacmNo));
				acDetail.setCustNo(lAcDetailFee.get(0).getCustNo());
				acDetail.setFacmNo(txFacmNo);
				acDetail.setBormNo(0);
				acDetail.setSumNo("092"); // 暫收轉帳
				this.lAcDetail.add(acDetail);
			}
		}

		// step 6. 本額度抵繳、轉額度抵繳 ==> 出帳
		Set<Integer> tempAmtSet = tempAmtMap.keySet();
		for (Iterator<Integer> itTxFacmNo = tempAmtSet.iterator(); itTxFacmNo.hasNext();) {
			int txFacmNo = itTxFacmNo.next();
			BigDecimal selfTempAmt = BigDecimal.ZERO;
			// 本額度抵繳
			if (selfTempMap.get(txFacmNo) != null) {
				selfTempAmt = selfTempMap.get(txFacmNo);
				this.info("selfTempRepay txFacmNo=" + txFacmNo + ", selfTempAmt=" + selfTempAmt);
				if (selfTempAmt.compareTo(BigDecimal.ZERO) > 0) {
					AcDetail acDetail = new AcDetail();
					acDetail.setDbCr("D");
					acDetail.setAcctCode("TAV");
					acDetail.setTxAmt(selfTempAmt);
					acDetail.setCustNo(lAcDetailFee.get(0).getCustNo());
					acDetail.setFacmNo(txFacmNo);
					acDetail.setBormNo(0);
					acDetail.setSumNo("090"); // 暫收抵繳
					this.lAcDetail.add(acDetail);
				}
			}
			// 轉額度抵繳
			BigDecimal otherTempAmt = tempAmtMap.get(txFacmNo).subtract(selfTempAmt);
			this.info("otherTempRepay txFacmNo=" + txFacmNo + ", otherTempAmt=" + otherTempAmt);
			if (otherTempAmt.compareTo(BigDecimal.ZERO) > 0) {
				AcDetail acDetail = new AcDetail();
				acDetail.setDbCr("D");
				acDetail.setAcctCode("TAV");
				acDetail.setTxAmt(otherTempAmt);
				acDetail.setCustNo(lAcDetailFee.get(0).getCustNo());
				acDetail.setFacmNo(txFacmNo);
				acDetail.setBormNo(0);
				acDetail.setSumNo("090"); // 暫收抵繳
				this.lAcDetail.add(acDetail);
			}
		}

		// step 7. 搬費用分錄
		for (AcDetail ac : lAcDetailFee) {
			this.lAcDetail.add(ac);
		}

		int acSeq = 0;
		for (AcDetail ac : this.lAcDetail) {
			acSeq++;
			ac.setAcSeq(acSeq);
		}
		for (AcDetail ac : this.lAcDetail) {
			this.info("transferTempAmt end " + ac.getDbCr() + " " + ac.getAcctCode() + " "
					+ FormatUtil.padLeft("" + ac.getTxAmt(), 11) + " " + ac.getCustNo() + "-" + ac.getFacmNo() + "-"
					+ ac.getBormNo() + " " + ac.getSumNo() + " " + ac.getRvNo());
		}

		return this.lAcDetail;
	}

	// 暫收轉額度(放款收回)
	private List<AcDetail> loanTransfer(List<LoanBorTx> ilLoanBorTx, List<AcDetail> iAcList, TitaVo titaVo)
			throws LogicException {
		this.info("loanTransfer ...");
		for (AcDetail ac : iAcList) {
			this.info(ac.getDbCr() + " " + ac.getAcctCode() + " " + FormatUtil.padLeft("" + ac.getTxAmt(), 11) + " "
					+ ac.getCustNo() + "-" + ac.getFacmNo() + "-" + ac.getBormNo() + " " + ac.getSumNo() + " "
					+ ac.getRvNo());
		}

		this.titaVo = titaVo;

		// step 0. 判斷是否分錄中有暫收抵繳，搬暫收款前分錄、暫存原有分錄(lAcDetailOld)

		int splitAcSeq = 0;
		// 暫收抵繳
		List<AcDetail> lAcDetailOld = new ArrayList<AcDetail>();
		int ii = 0;

		boolean isTempTransfer = false;
		for (AcDetail ac : iAcList) {
			if ("090".equals(ac.getSumNo()) && "D".equals(ac.getDbCr())) {
				isTempTransfer = true;
			}
			lAcDetailOld.add(ac);
		}
		// 無抵繳款
		if (!isTempTransfer) {
			this.lAcDetail = iAcList;
			return this.lAcDetail;
		}
		this.lAcDetail = new ArrayList<AcDetail>();

		// 搬暫收款前分錄
		for (AcDetail ac : iAcList) {
			if ("090".equals(ac.getSumNo()) && "D".equals(ac.getDbCr())) {
				break;
			}
			this.lAcDetail.add(ac);
		}

		// step 1. 累計分錄中的暫收抵繳金額

		// 暫收抵繳餘額
		HashMap<Integer, BigDecimal> tempBalMap = new HashMap<>();

		// 搬暫收款後 splitAcSeq
		for (AcDetail ac : iAcList) {
			ii++;
			if ("090".equals(ac.getSumNo()) && "D".equals(ac.getDbCr())) {
				if (tempBalMap.get(ac.getFacmNo()) == null) {
					tempBalMap.put(ac.getFacmNo(), ac.getTxAmt());
				} else {
					tempBalMap.put(ac.getFacmNo(), tempBalMap.get(ac.getFacmNo()).add(ac.getTxAmt()));
				}
				splitAcSeq = ii;
			}
		}
		this.info("isTempTransfer=" + isTempTransfer + ", splitAcSeq=" + splitAcSeq);

		// step 2. 計算交易明細中同的暫收借合計、可轉暫出金額

		// 額度使用金額
		HashMap<Integer, BigDecimal> tempAmtMap = new HashMap<>();
		// 轉額度金額
		HashMap<Integer, BigDecimal> transferAmtMap = new HashMap<>();

		for (LoanBorTx tx : ilLoanBorTx) {
			this.info("transferTempAmt=" + tx.toString());
			if (tx.getTempAmt().compareTo(BigDecimal.ZERO) > 0) {
				if (tempAmtMap.get(tx.getFacmNo()) == null) {
					tempAmtMap.put(tx.getFacmNo(), tx.getTempAmt());
				} else {
					tempAmtMap.put(tx.getFacmNo(), tx.getTempAmt().add(tempAmtMap.get(tx.getFacmNo())));
				}
				if (transferAmtMap.get(tx.getFacmNo()) == null) {
					transferAmtMap.put(tx.getFacmNo(), tx.getTempAmt());
				} else {
					transferAmtMap.put(tx.getFacmNo(), tx.getTempAmt().add(transferAmtMap.get(tx.getFacmNo())));
				}
			}
		}

		// step 3. 計算本額度暫收抵繳金額、本額度抵用後轉出金額

		// 本額度暫收抵繳金額
		HashMap<Integer, BigDecimal> selfTempMap = new HashMap<>();
		// 優先抵用本額度
		Set<Integer> transferSet = transferAmtMap.keySet();
		for (Iterator<Integer> itTxFacmNo = transferSet.iterator(); itTxFacmNo.hasNext();) {
			int txFacmNo = itTxFacmNo.next();
			this.info("transferSet-1 txFacmNo=" + txFacmNo + ", transferAmt=" + transferAmtMap.get(txFacmNo));
			BigDecimal txAmt = BigDecimal.ZERO;
			if (transferAmtMap.get(txFacmNo).compareTo(BigDecimal.ZERO) > 0) {
				if (tempBalMap.get(txFacmNo) != null) {
					if (tempBalMap.get(txFacmNo).compareTo(transferAmtMap.get(txFacmNo)) > 0) {
						txAmt = transferAmtMap.get(txFacmNo);
						tempBalMap.put(txFacmNo, tempBalMap.get(txFacmNo).subtract(txAmt));
						transferAmtMap.put(txFacmNo, BigDecimal.ZERO);
					} else {
						txAmt = tempBalMap.get(txFacmNo);
						transferAmtMap.put(txFacmNo, transferAmtMap.get(txFacmNo).subtract(tempBalMap.get(txFacmNo)));
						tempBalMap.put(txFacmNo, BigDecimal.ZERO);
					}
					selfTempMap.put(txFacmNo, txAmt);
				}
			}
		}

		// step 4. 本額度抵用後餘額轉出 ==> 出帳
		Set<Integer> tempBalSet = tempBalMap.keySet();
		for (Iterator<Integer> itTxFacmNo = tempBalSet.iterator(); itTxFacmNo.hasNext();) {
			int txFacmNo = itTxFacmNo.next();
			this.info("tempBalSet txFacmNo=" + txFacmNo + ", tempBal=" + tempBalMap.get(txFacmNo));
			if (tempBalMap.get(txFacmNo).compareTo(BigDecimal.ZERO) > 0) {
				AcDetail acDetail = new AcDetail();
				acDetail.setDbCr("D");
				acDetail.setAcctCode("TAV");
				acDetail.setTxAmt(tempBalMap.get(txFacmNo));
				acDetail.setCustNo(lAcDetailOld.get(0).getCustNo());
				acDetail.setFacmNo(txFacmNo);
				acDetail.setBormNo(0);
				acDetail.setSumNo("092"); // 暫收轉帳
				this.lAcDetail.add(acDetail);
			}
		}

		// step 5. 轉入抵用其他額度 ==> 出帳
		for (Iterator<Integer> itTxFacmNo = transferSet.iterator(); itTxFacmNo.hasNext();) {
			int txFacmNo = itTxFacmNo.next();
			this.info("transferSet txFacmNo=" + txFacmNo + ", transferAmt=" + transferAmtMap.get(txFacmNo));
			if (transferAmtMap.get(txFacmNo).compareTo(BigDecimal.ZERO) > 0) {
				AcDetail acDetail = new AcDetail();
				acDetail.setDbCr("C");
				acDetail.setAcctCode("TAV");
				acDetail.setTxAmt(transferAmtMap.get(txFacmNo));
				acDetail.setCustNo(lAcDetailOld.get(0).getCustNo());
				acDetail.setFacmNo(txFacmNo);
				acDetail.setBormNo(0);
				acDetail.setSumNo("092"); // 暫收轉帳
				this.lAcDetail.add(acDetail);
			}
		}

		// step 6. 本額度抵繳、轉額度抵繳 ==> 出帳
		Set<Integer> tempAmtSet = tempAmtMap.keySet();
		for (Iterator<Integer> itTxFacmNo = tempAmtSet.iterator(); itTxFacmNo.hasNext();) {
			int txFacmNo = itTxFacmNo.next();
			BigDecimal selfTempAmt = BigDecimal.ZERO;
			// 本額度抵繳
			if (selfTempMap.get(txFacmNo) != null) {
				selfTempAmt = selfTempMap.get(txFacmNo);
				this.info("selfTempRepay txFacmNo=" + txFacmNo + ", selfTempAmt=" + selfTempAmt);
				if (selfTempAmt.compareTo(BigDecimal.ZERO) > 0) {
					AcDetail acDetail = new AcDetail();
					acDetail.setDbCr("D");
					acDetail.setAcctCode("TAV");
					acDetail.setTxAmt(selfTempAmt);
					acDetail.setCustNo(lAcDetailOld.get(0).getCustNo());
					acDetail.setFacmNo(txFacmNo);
					acDetail.setBormNo(0);
					acDetail.setSumNo("090"); // 暫收抵繳
					this.lAcDetail.add(acDetail);
				}
			}
			// 轉額度抵繳
			BigDecimal transferTempAmt = BigDecimal.ZERO;
			transferTempAmt = tempAmtMap.get(txFacmNo).subtract(selfTempAmt);
			this.info("轉額度抵繳 txFacmNo=" + txFacmNo + ", selfTempAmt=" + selfTempAmt);
			if (transferTempAmt.compareTo(BigDecimal.ZERO) > 0) {
				AcDetail acDetail = new AcDetail();
				acDetail.setDbCr("D");
				acDetail.setAcctCode("TAV");
				acDetail.setTxAmt(transferTempAmt);
				acDetail.setCustNo(lAcDetailOld.get(0).getCustNo());
				acDetail.setFacmNo(txFacmNo);
				acDetail.setBormNo(0);
				acDetail.setSumNo("090"); // 暫收抵繳
				this.lAcDetail.add(acDetail);
			}
		}

		// step 7. 搬暫收款後分錄
		for (int i = splitAcSeq; i < lAcDetailOld.size(); i++) {
			this.lAcDetail.add(lAcDetailOld.get(i));
		}

		int acSeq = 0;
		for (AcDetail ac : this.lAcDetail) {
			acSeq++;
			ac.setAcSeq(acSeq);
		}
		for (AcDetail ac : this.lAcDetail) {
			this.info("transferTempAmt end " + ac.getDbCr() + " " + ac.getAcctCode() + " "
					+ FormatUtil.padLeft("" + ac.getTxAmt(), 11) + " " + ac.getCustNo() + "-" + ac.getFacmNo() + "-"
					+ ac.getBormNo() + " " + ac.getSumNo() + " " + ac.getRvNo());
		}

		return this.lAcDetail;
	}

	// 放款收回
	private BigDecimal settleLoanAmt(LoanBorTx tx) throws LogicException {
		BigDecimal debitAmt = BigDecimal.ZERO; // 借方金額
		this.info("settleLoanAmt ... ");
		// 結案登錄
		if ("L3420".equals(titaVo.getTxcd())) {
			// 轉催收 借: 催收款項
			if (iCaseCloseCode == 3) {
				acDetail = new AcDetail();
				acDetail.setDbCr("D");
				acDetail.setAcctCode("990");
				acDetail.setTxAmt(tx.getPrincipal().add(tx.getInterest())); // 催收餘額
				acDetail.setCustNo(tx.getCustNo());
				acDetail.setFacmNo(tx.getFacmNo());
				acDetail.setBormNo(tx.getBormNo());
				lAcDetail.add(acDetail);
				debitAmt = acDetail.getTxAmt();
			}
			// 轉呆帳 借:備抵呆帳
			if (iCaseCloseCode == 7 || iCaseCloseCode == 8) {
				acDetail = new AcDetail();
				acDetail.setDbCr("D");
				acDetail.setAcctCode("F18");
				acDetail.setTxAmt(tx.getPrincipal()); // 催收本金
				acDetail.setCustNo(tx.getCustNo());
				acDetail.setFacmNo(tx.getFacmNo());
				acDetail.setBormNo(tx.getBormNo());
				lAcDetail.add(acDetail);
				debitAmt = acDetail.getTxAmt();
			}
		}
		// 催收回復
		if ("L3440".equals(titaVo.getTxcd())) {
			TempVo txTempVo = new TempVo();
			txTempVo = txTempVo.getVo(tx.getOtherFields());

			// 借:放款
			acDetail = new AcDetail();
			acDetail.setDbCr("D");
			acDetail.setAcctCode(tx.getAcctCode());
			acDetail.setTxAmt(parse.stringToBigDecimal(txTempVo.get("BeforeLoanBal")));
			acDetail.setCustNo(tx.getCustNo());
			acDetail.setFacmNo(tx.getFacmNo());
			acDetail.setBormNo(tx.getBormNo());
			lAcDetail.add(acDetail);
			debitAmt = acDetail.getTxAmt();
			// 貸:催收款項
			acDetail = new AcDetail();
			acDetail.setDbCr("C");
			acDetail.setAcctCode("990");
			acDetail.setTxAmt(parse.stringToBigDecimal(txTempVo.get("OvduBal")));
			acDetail.setCustNo(tx.getCustNo());
			acDetail.setFacmNo(tx.getFacmNo());
			acDetail.setBormNo(tx.getBormNo());
			lAcDetail.add(acDetail);
		}
		// 貸方科目
		BigDecimal shortfallPrincipal = BigDecimal.ZERO; // 累短收 - 本金
		BigDecimal shortfallInterest = BigDecimal.ZERO; // 累短收-利息
		BigDecimal shortCloseBreach = BigDecimal.ZERO; // 累短收 - 清償違約金

		// 累短收收回
		if (tx.getShortfall().compareTo(BigDecimal.ZERO) > 0) {
			for (BaTxVo ba : this.baTxList) {
				if (ba.getDataKind() == 1 && ba.getRepayType() <= 3 && ba.getAcctAmt().compareTo(BigDecimal.ZERO) > 0) {
					if ((ba.getFacmNo() == tx.getFacmNo() || ba.getFacmNo() == 0)
							&& (ba.getBormNo() == tx.getBormNo() || ba.getBormNo() == 0)) {
						shortfallPrincipal = shortfallPrincipal.add(ba.getPrincipal());
						shortfallInterest = shortfallInterest.add(ba.getInterest());
						shortCloseBreach = shortCloseBreach.add(ba.getCloseBreachAmt());
						acDetail = new AcDetail();
						acDetail.setDbCr("C");
						acDetail.setAcctCode(ba.getAcctCode());
						acDetail.setTxAmt(ba.getAcctAmt());
						acDetail.setCustNo(ba.getCustNo());
						acDetail.setFacmNo(ba.getFacmNo());
						acDetail.setBormNo(ba.getBormNo());
						acDetail.setRvNo(ba.getRvNo());
						acDetail.setReceivableFlag(ba.getReceivableFlag());
						this.lAcDetail.add(acDetail);
						ba.setAcctAmt(BigDecimal.ZERO);
					}
				}
			}
		}
		// 本金(放款科目或催收款項)
		if (tx.getPrincipal().subtract(shortfallPrincipal).compareTo(BigDecimal.ZERO) > 0) {
			acDetail = new AcDetail();
			acDetail.setDbCr("C");
			acDetail.setAcctCode(tx.getAcctCode());
			acDetail.setTxAmt(tx.getPrincipal().subtract(shortfallPrincipal)); // 回收短繳另外出帳
			acDetail.setCustNo(tx.getCustNo());
			acDetail.setFacmNo(tx.getFacmNo());
			acDetail.setBormNo(tx.getBormNo());
			lAcDetail.add(acDetail);
		}
		// 利息
		if (tx.getInterest().subtract(shortfallInterest).compareTo(BigDecimal.ZERO) > 0) {
			acDetail = new AcDetail();
			acDetail.setDbCr("C");
			acDetail.setAcctCode(loanCom.setIntAcctCode(tx.getAcctCode()));
			acDetail.setTxAmt(tx.getInterest().subtract(shortfallInterest)); // 回收短繳另外出帳
			acDetail.setCustNo(tx.getCustNo());
			acDetail.setFacmNo(tx.getFacmNo());
			acDetail.setBormNo(tx.getBormNo());
			lAcDetail.add(acDetail);
		}
		// 延滯息
		if (tx.getDelayInt().compareTo(BigDecimal.ZERO) > 0) {
			acDetail = new AcDetail();
			acDetail.setDbCr("C");
			acDetail.setAcctCode("IOV");
			acDetail.setTxAmt(tx.getDelayInt());
			acDetail.setCustNo(tx.getCustNo());
			acDetail.setFacmNo(tx.getFacmNo());
			acDetail.setBormNo(tx.getBormNo());
			lAcDetail.add(acDetail);
		}
		// 違約金
		if (tx.getBreachAmt().compareTo(BigDecimal.ZERO) > 0) {
			acDetail = new AcDetail();
			acDetail.setDbCr("C");
			acDetail.setAcctCode("IOV");
			acDetail.setTxAmt(tx.getBreachAmt());
			acDetail.setCustNo(tx.getCustNo());
			acDetail.setFacmNo(tx.getFacmNo());
			acDetail.setBormNo(tx.getBormNo());
			lAcDetail.add(acDetail);
		}
		// 清償違約金
		if (tx.getCloseBreachAmt().subtract(shortCloseBreach).compareTo(BigDecimal.ZERO) > 0) {
			acDetail = new AcDetail();
			acDetail.setDbCr("C");
			acDetail.setAcctCode("IOP");
			acDetail.setTxAmt(tx.getCloseBreachAmt().subtract(shortCloseBreach)); // 回收短繳另外出帳;
			acDetail.setCustNo(tx.getCustNo());
			acDetail.setFacmNo(tx.getFacmNo());
			acDetail.setBormNo(tx.getBormNo());
			lAcDetail.add(acDetail);
		}
		return debitAmt;
	}

	// 新增放款交易內容檔(收回費用)
	private void settleFeeAmt(BaTxVo ba, LoanBorTx tx) throws LogicException {
		this.info("settleFeeAmt ... ");
		// 轉呆帳 借:備抵呆帳
		if (iCaseCloseCode == 7 || iCaseCloseCode == 8) {
			acDetail = new AcDetail();
			acDetail.setDbCr("D");
			acDetail.setAcctCode("F18");
			acDetail.setTxAmt(ba.getAcctAmt());
			acDetail.setCustNo(ba.getCustNo());
			acDetail.setFacmNo(ba.getFacmNo());
			acDetail.setBormNo(ba.getBormNo());

			lAcDetail.add(acDetail);
			tx.setTxAmt(acDetail.getTxAmt()); // 轉催呆金額放LoanBorTx交易金額
		}
		// 新增帳務分錄
		AcDetail acDetail = new AcDetail();
		acDetail.setDbCr("C");
		acDetail.setAcctCode(ba.getAcctCode());
		acDetail.setTxAmt(ba.getAcctAmt());
		acDetail.setCustNo(ba.getCustNo());
		acDetail.setFacmNo(ba.getFacmNo());
		acDetail.setBormNo(ba.getBormNo());
		acDetail.setRvNo(ba.getRvNo());
		acDetail.setReceivableFlag(ba.getReceivableFlag());
		this.lAcDetail.add(acDetail);

	}

	// 新增放款交易內容檔(收回費用)
	private LoanBorTx addFeeBorTxRoutine(BaTxVo ba, int iRpCode, String iNote, int iEntryDate, TempVo iTempVo,
			TitaVo titaVo) throws LogicException {
		this.info("addFeeBorTxRoutine ... ");

		// 新增放款交易內容檔(收回費用)
		LoanBorTx tLoanBorTx = new LoanBorTx();
		LoanBorTxId tLoanBorTxId = new LoanBorTxId();
		loanCom.setFacmBorTx(tLoanBorTx, tLoanBorTxId, ba.getCustNo(), ba.getFacmNo(), titaVo);
		tLoanBorTx.setCustNo(ba.getCustNo());
		tLoanBorTx.setFacmNo(ba.getFacmNo());
		tLoanBorTx.setTxDescCode("Fee");
		tLoanBorTx.setRepayCode(iRpCode); // 還款來源
		tLoanBorTx.setEntryDate(iEntryDate);
		tLoanBorTx.setDueDate(ba.getPayIntDate());
		tLoanBorTx.setAcctCode(ba.getAcctCode()); // 實收費用金額
		tLoanBorTx.setFeeAmt(ba.getAcctAmt()); // 實收費用金額
		tLoanBorTx.setDisplayflag("A"); // 無繳息
		// 其他欄位
		switch (ba.getRepayType()) {
		case 4:
			iTempVo.putParam("AcctFee", ba.getAcctAmt());
			break;
		case 5:
			iTempVo.putParam("FireFee", ba.getAcctAmt());
			break;
		case 6:
			iTempVo.putParam("ModifyFee", ba.getAcctAmt());
			break;
		case 7:
			iTempVo.putParam("LawFee", ba.getAcctAmt());
			break;
		}
		if (!iNote.isEmpty()) {
			iTempVo.putParam("Note", iNote);
		}
		iTempVo.putParam("RvNo", ba.getRvNo()); // 銷帳編號
		iTempVo.putParam("RvJsonFields", ba.getRvJsonFields()); // 銷帳JsonFields
		tLoanBorTx.setOtherFields(iTempVo.getJsonString());

		return tLoanBorTx;
	}

	// 設定交易內容檔序號
	private int setFacmBorxNo(LoanBorTx tx, TitaVo titaVo) throws LogicException {
		// 同戶號額度則續編
		String tmp = "" + tx.getCustNo() + tx.getFacmNo();
		int borxNo = tx.getBorxNo();
		if (this.mapBorxNo.get(tmp) != null) {
			borxNo = this.mapBorxNo.get(tmp) + 1;
			LoanBorTxId txId = tx.getLoanBorTxId();
			txId.setBorxNo(borxNo);
			tx.setLoanBorTxId(txId);
			tx.setBorxNo(borxNo);
		}
		this.mapBorxNo.put(tmp, borxNo);
		return borxNo;
	}
}