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
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.dataVO.TotaVo;
import com.st1.itx.db.domain.AcDetail;
import com.st1.itx.db.domain.AcReceivable;
import com.st1.itx.db.domain.FacMain;
import com.st1.itx.db.domain.FacMainId;
import com.st1.itx.db.domain.FacProd;
import com.st1.itx.db.domain.LoanBorMain;
import com.st1.itx.db.domain.LoanBorMainId;
import com.st1.itx.db.domain.LoanBorTx;
import com.st1.itx.db.domain.LoanBorTxId;
import com.st1.itx.db.domain.LoanOverdue;
import com.st1.itx.db.domain.LoanOverdueId;
import com.st1.itx.db.domain.LoanRateChange;
import com.st1.itx.db.service.FacMainService;
import com.st1.itx.db.service.FacProdService;
import com.st1.itx.db.service.LoanBorMainService;
import com.st1.itx.db.service.LoanBorTxService;
import com.st1.itx.db.service.LoanOverdueService;
import com.st1.itx.db.service.LoanRateChangeService;
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.common.AcDetailCom;
import com.st1.itx.util.common.AcReceivableCom;
import com.st1.itx.util.common.AcRepayCom;
import com.st1.itx.util.common.BaTxCom;
import com.st1.itx.util.common.LoanCom;
import com.st1.itx.util.common.LoanDueAmtCom;
import com.st1.itx.util.common.SendRsp;
import com.st1.itx.util.common.data.BaTxVo;
import com.st1.itx.util.date.DateUtil;
import com.st1.itx.util.parse.Parse;

/*
 * L3240 回收沖正(轉換前交易)
 * a.使用時機:包括期款回收,部分償還及預繳，依交易序號執行沖正
 * b.本交易不可訂正
 */

/**
 * L3240 回收登錄
 * 
 * @author iza
 * @version 1.0.0
 */
@Service("L3240")
@Scope("prototype")
public class L3240 extends TradeBuffer {

	/* DB服務注入 */
	@Autowired
	public FacProdService facProdService;
	@Autowired
	public FacMainService facMainService;
	@Autowired
	public LoanBorMainService loanBorMainService;
	@Autowired
	public LoanBorTxService loanBorTxService;
	@Autowired
	public LoanRateChangeService loanRateChangeService;
	@Autowired
	public LoanOverdueService loanOverdueService;
	@Autowired
	public LoanDueAmtCom loanDueAmtCom;
	@Autowired
	BaTxCom baTxCom;

	@Autowired
	Parse parse;
	@Autowired
	DateUtil dDateUtil;
	@Autowired
	LoanCom loanCom;
	@Autowired
	AcDetailCom acDetailCom;
	@Autowired
	AcRepayCom acRepayCom;
	@Autowired
	AcReceivableCom acReceivableCom;
	@Autowired
	public SendRsp sendRsp;

	private TitaVo titaVo;
	private int iCustNo;
	private int iFacmNo;
	private int iEntryDate;
	private int iCaseCloseCode = 0;
	private int wkBorxNo;
	private int wkNewBorxNo;
	private BigDecimal wkTempAmt = BigDecimal.ZERO;
	private BigDecimal wkTxAmt = BigDecimal.ZERO;
	private int wkRepayCode;

	private FacProd tFacProd;
	private FacMain tFacMain;
	private LoanBorMain tLoanBorMain;
	private LoanOverdue tLoanOverdue;
	// work area
	private AcDetail acDetail;
	private List<AcDetail> lAcDetail = new ArrayList<AcDetail>();
	private List<AcReceivable> lAcReceivableDelete = new ArrayList<AcReceivable>();
	private List<AcReceivable> lAcReceivableInsert = new ArrayList<AcReceivable>();
	private ArrayList<BaTxVo> baTxList = new ArrayList<BaTxVo>();

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active L3240 ");
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
		baTxCom.setTxBuffer(this.txBuffer);
		acRepayCom.setTxBuffer(this.getTxBuffer());

		// 取得輸入資料
		iCustNo = this.parse.stringToInteger(titaVo.getParam("TimCustNo"));
		this.info("titaVo.getHsupCode() =" + titaVo.getHsupCode());
		this.info("titaVo.getEmpNos().trim() =" + titaVo.getEmpNos().trim());
		if (!titaVo.getHsupCode().equals("1")) {
			sendRsp.addvReason(this.txBuffer, titaVo, "0005", ""); // 訂正需主管核可
		}

		// Check Input
		checkInputRoutine();

		// 檢查到同戶帳務交易需由最近一筆交易開始訂正
		loanCom.checkEraseCustNoTxSeqNo(iCustNo, titaVo);

		this.baTxList = baTxCom.settingUnPaid(iEntryDate, iCustNo, 0, 0, 99, BigDecimal.ZERO, titaVo); // 99-費用全部
		// 暫收款金額 (暫收借)
		acRepayCom.settleTempAmt(this.baTxList, this.lAcDetail, titaVo);

		// 沖正處理
		repayEraseRoutine();

		if (wkRepayCode == 0) {
			if (wkTxAmt.compareTo(BigDecimal.ZERO) > 0) {
				wkRepayCode = 9; // 其他
			} else {
				wkRepayCode = 90; // 暫收抵繳
			}
		}

		titaVo.setTxAmt(wkTxAmt);
		titaVo.put("CURNM", "TWD");
		titaVo.putParam("RpCode1", wkRepayCode);
		titaVo.putParam("RpAmt1", wkTxAmt);
		titaVo.putParam("RpType1", 9);
		titaVo.putParam("RpCustNo1", iCustNo);
		titaVo.putParam("RpFacmNo1", iFacmNo);

		// Temp 200
		// L3200 TxAmt = 300 TempAmt = -200 Loan 500 ==> HCODE = 3 被沖正
		// L3200 TxAmt = 300 TempAmt = -200 Loan 500 ==> HCODE = 4 沖正
		// L3240 TxAmt = 300 TempAmt = 300 Loan 0 ==> HCODE = 0 (入帳金額轉暫收款-冲正產生)
		// Debit: Loan 500 Credit TAV 500(Tx 300, Temp 200)

		// THC 暫收款－沖正
		if (wkTxAmt.compareTo(BigDecimal.ZERO) > 0) {
			acDetail = new AcDetail();
			acDetail.setDbCr("C");
			acDetail.setAcctCode("THC");
			acDetail.setSumNo("099");
			acDetail.setTxAmt(wkTxAmt);
			acDetail.setCustNo(iCustNo);
			acDetail.setFacmNo(iFacmNo);
			acDetail.setRvNo("" + titaVo.getOrgEntdyI());// 會計日期
			lAcDetail.add(acDetail);
		}
		// 累溢收入帳(暫收貸)
		acRepayCom.settleOverflow(lAcDetail, titaVo);

		// 產生會計分錄
		this.txBuffer.setAcDetailList(lAcDetail);
		acDetailCom.setTxBuffer(this.txBuffer);
		acDetailCom.run(titaVo);

		acReceivableCom.setTxBuffer(this.getTxBuffer());

		// 短繳銷帳檔處理
		if (lAcReceivableDelete.size() > 0) {
			acReceivableCom.mnt(1, lAcReceivableDelete, titaVo); // 1-銷帳
		}
		if (lAcReceivableInsert.size() > 0) {
			acReceivableCom.mnt(0, lAcReceivableInsert, titaVo); // 0-起帳
		}

		this.addList(this.totaVo);
		return this.sendList();
	}

	private void checkInputRoutine() throws LogicException {
		if (titaVo.isHcodeErase()) {
			throw new LogicException(titaVo, "E0010", "本交易不可訂正"); // 功能選擇錯誤
		}
	}

	// 沖正處理
	private void repayEraseRoutine() throws LogicException {
		this.info("calcRepayEraseRoutine ...");
		// 查詢放款交易內容檔
		Slice<LoanBorTx> slLoanBorTx = loanBorTxService.custNoTxtNoEq(iCustNo, titaVo.getOrgEntdyI() + 19110000,
				titaVo.getOrgKin(), titaVo.getOrgTlr(), titaVo.getOrgTno(), 0, Integer.MAX_VALUE, titaVo);
		if (slLoanBorTx == null) {
			throw new LogicException(titaVo, "E0001", "放款交易內容檔"); // 查詢資料不存在
		}
		for (LoanBorTx tx : slLoanBorTx.getContent()) {
			if (!tx.getTitaHCode().equals("0")) {
				throw new LogicException(titaVo, "E0010", "非正常交易（轉換前資料）"); // 功能選擇錯誤
			}
			if (!tx.getCreateEmpNo().equals("999999")) {
				throw new LogicException(titaVo, "E0010", "非轉換資料不可執行L3240回收冲正（轉換前資料）"); // 功能選擇錯誤
			}
			if ("L3440".equals(tx.getTitaTxCd())) {
				throw new LogicException(titaVo, "E0010", "不可執行催收回復冲正（轉換前資料）"); // 功能選擇錯誤
			}
			wkRepayCode = tx.getRepayCode();
			wkTxAmt = wkTxAmt.add(tx.getTxAmt());
			wkBorxNo = tx.getBorxNo();
			if ("L3410".equals(tx.getTitaTxCd()) || "L3420".equals(tx.getTitaTxCd())) {
				iCaseCloseCode = this.parse.stringToInteger(titaVo.getParam("CaseCloseCode"));
				// 還原催收檔
				if (iCaseCloseCode >= 3) {
					RestoredOverdueRoutine(iCaseCloseCode, tx);
				}
			}
			if (iCaseCloseCode > 3) {
				RestoredBorMainRoutine(tx);
				loanCom.setLoanBorTxHcode(tx.getCustNo(), tx.getFacmNo(), tx.getBormNo(), wkBorxNo, wkNewBorxNo,
						tLoanOverdue.getOvduBal(), titaVo);
				// 催收帳務處理
				acDetailRoutine(tx);
			} else {
				// 更新額度檔
				updFacMainRoutine(tx);
				// 還原撥款主檔
				updLoanBorMainRoutine(tx);
				// 註記交易內容檔
				loanCom.setLoanBorTxHcode(tx.getCustNo(), tx.getFacmNo(), tx.getBormNo(), wkBorxNo, wkNewBorxNo,
						tLoanBorMain.getLoanBal(), titaVo);
				// 回收帳務處理
				acDetailRoutine(tx);
				// 欠繳金額處理
				unUpaidRoutine(tx);
			}
		}
	}

	private void updFacMainRoutine(LoanBorTx tx) throws LogicException {
		// 鎖定額度檔
		tFacMain = facMainService.holdById(new FacMainId(iCustNo, iFacmNo), titaVo);
		this.info("updFacMainRoutine..." + tFacMain.getRecycleCode() + "," + tx.getPrincipal());
		if (tFacMain == null) {
			throw new LogicException(titaVo, "E3011", "額度主檔 戶號 = " + iCustNo + "-" + iFacmNo); // 鎖定資料時，發生錯誤
		}
		if (tFacMain.getActFg() == 1) {
			throw new LogicException(titaVo, "E0021",
					"額度檔 戶號 = " + tFacMain.getCustNo() + " 額度編號 =  " + tFacMain.getFacmNo()); // 該筆資料待放行中
		}
		// 查詢商品參數檔
		tFacProd = facProdService.findById(tFacMain.getProdNo(), titaVo);
		if (tFacProd == null) {
			throw new LogicException(titaVo, "E0001", "商品參數檔 商品代碼 = " + tFacMain.getProdNo()); // 查詢資料不存在
		}

		if (tx.getPrincipal().compareTo(BigDecimal.ZERO) == 0) {
			return;
		}
		tFacMain.setUtilAmt(tFacMain.getUtilAmt().add(tx.getPrincipal()));
		if (tFacMain.getRecycleCode().equals("1")) {
			tFacMain.setUtilBal(tFacMain.getUtilBal().add(tx.getPrincipal()));
		}
		try {
			facMainService.update(tFacMain, titaVo);
		} catch (DBException e) {
			throw new LogicException(titaVo, "E0007", "額度主檔 戶號 = " + iCustNo + " 額度編號 = " + iFacmNo); // 更新資料時，發生錯誤
		}

	}

	// 欠繳處理
	private void unUpaidRoutine(LoanBorTx tx) throws LogicException {
		// 刪除本筆回收產生的欠繳

		unpaidAmtRoutine(true, tx);

		LoanBorTx lx = null;
		for (int i = tx.getBorxNo() - 1; i > 1; i--) {
			lx = loanBorTxService.findById(new LoanBorTxId(tx.getCustNo(), tx.getFacmNo(), tx.getBormNo(), i), titaVo);
			if (lx == null) {
				break;
			}
			if ("0".equals(lx.getTitaHCode()) && lx.getTitaTxCd().equals("L3200")) {
				unpaidAmtRoutine(false, lx);
				break;
			}
		}

	}

	// 欠繳金額處理
	private void unpaidAmtRoutine(boolean isDelete, LoanBorTx tx) throws LogicException {
		this.info("unpaidAmtRoutine ..." + tx.toString());
		// 短繳利息, 新增銷帳檔
		if (tx.getUnpaidInterest().compareTo(BigDecimal.ZERO) > 0) {
			acRvUnpaidAmt(isDelete, tx, loanCom.setShortIntAcctCode(tFacMain.getAcctCode()), tx.getUnpaidInterest());
		}

		// 短繳本金處理, 新增銷帳檔
		if (tx.getUnpaidPrincipal().compareTo(BigDecimal.ZERO) > 0) {
			acRvUnpaidAmt(isDelete, tx, loanCom.setShortPrinAcctCode(tFacMain.getAcctCode()), tx.getUnpaidPrincipal());
		}

		// 短繳清償違約金處理, 新增銷帳檔
		if (tx.getUnpaidCloseBreach().compareTo(BigDecimal.ZERO) > 0) {
			acRvUnpaidAmt(isDelete, tx, "IOP", tx.getUnpaidCloseBreach());
		}
	}

	// 短繳金額
	private void acRvUnpaidAmt(boolean isDelete, LoanBorTx tx, String acctCode, BigDecimal shortAmt)
			throws LogicException {

		AcReceivable tAcReceivable = new AcReceivable();
		tAcReceivable.setReceivableFlag(4); // 短繳期金
		tAcReceivable.setAcctCode(acctCode);
		tAcReceivable.setCustNo(iCustNo);
		tAcReceivable.setFacmNo(iFacmNo);
		tAcReceivable.setRvNo(parse.IntegerToString(tx.getBormNo(), 3));
		tAcReceivable.setRvAmt(shortAmt);
		if (isDelete) {
			lAcReceivableDelete.add(tAcReceivable);
		} else {
			lAcReceivableInsert.add(tAcReceivable);
		}
	}

	// 還原撥款主檔(放款收回)
	private void updLoanBorMainRoutine(LoanBorTx tx) throws LogicException {
		this.info("RestoredRepayLoanBorMainRoutine ... ");

		tLoanBorMain = loanBorMainService.holdById(new LoanBorMainId(iCustNo, iFacmNo, tx.getBormNo()), titaVo);
		if (tLoanBorMain == null) {
			throw new LogicException(titaVo, "E0006", "撥款主檔 戶號 = " + iCustNo + "-" + iFacmNo + "-" + tx.getBormNo()); // 鎖定資料時，發生錯誤
		}
		if (tLoanBorMain.getActFg() == 1) {
			throw new LogicException(titaVo, "E0021", "放款主檔 戶號 = " + iCustNo + "-" + iFacmNo + "-" + tx.getBormNo()); // 該筆資料待放行中
		}

		if (tLoanBorMain.getPrevPayIntDate() != tx.getIntEndDate()) {
			throw new LogicException(titaVo, "E0019", "放款主檔 戶號 = " + iCustNo + "-" + iFacmNo + "-" + tx.getBormNo()
					+ " 應沖正繳息迄日=" + tLoanBorMain.getPrevPayIntDate()); // 輸入資料錯誤
		}

		LoanRateChange tLoanRateChange = loanRateChangeService.rateChangeEffectDateDescFirst(iCustNo, iFacmNo,
				tx.getBormNo(), tx.getIntStartDate() + 19110000, titaVo);
		if (tLoanRateChange == null) {
			throw new LogicException(titaVo, "E3926",
					iCustNo + "-" + iFacmNo + "-" + tx.getBormNo() + " 無放款利率變動資料 = " + tx.getIntStartDate()); // 計算利息錯誤，放款利率變動檔查無資料
		}
		tLoanBorMain.setStatus(0);
		tLoanBorMain.setStoreRate(tLoanRateChange.getFitRate());
		tLoanBorMain.setLoanBal(tLoanBorMain.getLoanBal().add(tx.getPrincipal()));
		tLoanBorMain.setPaidTerms(tLoanBorMain.getPaidTerms() - tx.getPaidTerms());
		tLoanBorMain.setPrevPayIntDate(tx.getIntStartDate());

		// 上次還本日
		if ("3".equals(tLoanBorMain.getAmortizedCode()) || "4".equals(tLoanBorMain.getAmortizedCode())) {
			if (tLoanBorMain.getPrevRepaidDate() > 0) {
				if (tx.getIntStartDate() >= tLoanBorMain.getGraceDate()) {
					tLoanBorMain.setPrevRepaidDate(tx.getIntStartDate());
					tLoanBorMain.setRepaidPeriod(tLoanBorMain.getPaidTerms() - tLoanBorMain.getGracePeriod());
				} else {
					tLoanBorMain.setPrevRepaidDate(0);
					tLoanBorMain.setRepaidPeriod(0);
				}
			}
		}

		// 下次繳息日
		tLoanBorMain.setNextPayIntDate(loanCom.getNextPayIntDate(tLoanBorMain.getAmortizedCode(),
				tLoanBorMain.getPayIntFreq(), tLoanBorMain.getFreqBase(), tLoanBorMain.getSpecificDate(),
				tLoanBorMain.getSpecificDd(), tLoanBorMain.getPrevPayIntDate(), tLoanBorMain.getMaturityDate()));

		// 下次還本日
		tLoanBorMain
				.setNextRepayDate(loanCom.getNextRepayDate(tLoanBorMain.getAmortizedCode(), tLoanBorMain.getRepayFreq(),
						tLoanBorMain.getFreqBase(), tLoanBorMain.getSpecificDate(), tLoanBorMain.getSpecificDd(),
						tLoanBorMain.getPrevRepaidDate(), tLoanBorMain.getMaturityDate(), tLoanBorMain.getGraceDate()));

		// 上次繳息日利率與收息利率不同需重算期金
		if (tLoanBorMain.getStoreRate().compareTo(tx.getRate()) != 0) {
			if ("3".equals(tLoanBorMain.getAmortizedCode()) && "1".equals(tFacMain.getExtraRepayCode())) {
				int wkGracePeriod = loanCom.getGracePeriod(tLoanBorMain.getAmortizedCode(), tLoanBorMain.getFreqBase(),
						tLoanBorMain.getPayIntFreq(), tLoanBorMain.getSpecificDate(), tLoanBorMain.getSpecificDd(),
						tLoanBorMain.getGraceDate());
				// 剩餘還本期數
				int wkDueTerms = tLoanBorMain.getPaidTerms() > wkGracePeriod
						? tLoanBorMain.getTotalPeriod() - tLoanBorMain.getPaidTerms()
						: tLoanBorMain.getTotalPeriod() - wkGracePeriod;
				BigDecimal wkNewDueAmt = loanDueAmtCom.getDueAmt(tLoanBorMain.getLoanBal(), tLoanBorMain.getStoreRate(),
						tLoanBorMain.getAmortizedCode(), tLoanBorMain.getFreqBase(), wkDueTerms, 0,
						tLoanBorMain.getPayIntFreq(), tLoanBorMain.getFinalBal(), titaVo);
				tLoanBorMain.setDueAmt(wkNewDueAmt);
			}
		}
		wkNewBorxNo = tLoanBorMain.getLastBorxNo() + 1;

		tLoanBorMain.setLastBorxNo(wkNewBorxNo);
		tLoanBorMain.setLastEntDy(titaVo.getEntDyI());
		tLoanBorMain.setLastKinbr(titaVo.getKinbr());
		tLoanBorMain.setLastTlrNo(titaVo.getTlrNo());
		tLoanBorMain.setLastTxtNo(titaVo.getTxtNo());
		try {
			loanBorMainService.update(tLoanBorMain, titaVo);
		} catch (DBException e) {
			throw new LogicException(titaVo, "E0007", "撥款主檔 戶號 = " + iCustNo + "-" + iFacmNo + "-" + tx.getBormNo()); // 更新資料時，發生錯誤
		}
	}

	// 還原撥款主檔(放款收回)
	private void RestoredBorMainRoutine(LoanBorTx tx) throws LogicException {
		this.info("RestoredRepayLoanBorMainRoutine ... ");

		tLoanBorMain = loanBorMainService.holdById(new LoanBorMainId(iCustNo, iFacmNo, tx.getBormNo()), titaVo);
		if (tLoanBorMain == null) {
			throw new LogicException(titaVo, "E0006", "撥款主檔 戶號 = " + iCustNo + "-" + iFacmNo + "-" + tx.getBormNo()); // 鎖定資料時，發生錯誤
		}
		if (tLoanBorMain.getActFg() == 1) {
			throw new LogicException(titaVo, "E0021", "放款主檔 戶號 = " + iCustNo + "-" + iFacmNo + "-" + tx.getBormNo()); // 該筆資料待放行中
		}
		wkNewBorxNo = tLoanBorMain.getLastBorxNo() + 1;
        if (tLoanOverdue.getStatus() == 1) {
        	tLoanBorMain.setStatus(2);	
        } else {       	
        	tLoanBorMain.setStatus(7);	
        }
		tLoanBorMain.setLastBorxNo(wkNewBorxNo);
		tLoanBorMain.setLastEntDy(titaVo.getEntDyI());
		tLoanBorMain.setLastKinbr(titaVo.getKinbr());
		tLoanBorMain.setLastTlrNo(titaVo.getTlrNo());
		tLoanBorMain.setLastTxtNo(titaVo.getTxtNo());
		try {
			loanBorMainService.update(tLoanBorMain, titaVo);
		} catch (DBException e) {
			throw new LogicException(titaVo, "E0007", "撥款主檔 戶號 = " + iCustNo + "-" + iFacmNo + "-" + tx.getBormNo()); // 更新資料時，發生錯誤
		}
	}

	// 回收帳務處理
	private void acDetailRoutine(LoanBorTx tx) throws LogicException {
		this.info("acDetailCrRoutine ... ");
		this.info("   isBookAcYes = " + this.txBuffer.getTxCom().isBookAcYes());

		if (!this.txBuffer.getTxCom().isBookAcYes()) {
			return;
		}
		// 本金
		if (tx.getPrincipal().compareTo(BigDecimal.ZERO) > 0) {
			acDetail = new AcDetail();
			acDetail.setDbCr("D");
			if (iCaseCloseCode >= 3) {
				acDetail.setAcctCode("990");
			} else {
				acDetail.setAcctCode(tFacMain.getAcctCode());
			}
			acDetail.setTxAmt(tx.getPrincipal());
			acDetail.setCustNo(tx.getCustNo());
			acDetail.setFacmNo(tx.getFacmNo());
			acDetail.setBormNo(tx.getBormNo());
			lAcDetail.add(acDetail);
			// 轉呆帳 借:備抵呆帳
			if (iCaseCloseCode == 7 || iCaseCloseCode == 8) {
				acDetail = new AcDetail();
				acDetail.setDbCr("C");
				acDetail.setAcctCode("F18");
				acDetail.setTxAmt(tx.getPrincipal()); // 催收本金
				acDetail.setCustNo(tx.getCustNo());
				acDetail.setFacmNo(tx.getFacmNo());
				acDetail.setBormNo(tx.getBormNo());
				lAcDetail.add(acDetail);
			}
		}
		// 利息
		if (tx.getInterest().compareTo(BigDecimal.ZERO) > 0) {
			acDetail = new AcDetail();
			acDetail.setDbCr("D");
			acDetail.setAcctCode(loanCom.setIntAcctCode(tFacMain.getAcctCode()));
			acDetail.setTxAmt(tx.getInterest());
			acDetail.setCustNo(tx.getCustNo());
			acDetail.setFacmNo(tx.getFacmNo());
			acDetail.setBormNo(tx.getBormNo());
			lAcDetail.add(acDetail);
		}
		// 延滯息
		if (tx.getDelayInt().compareTo(BigDecimal.ZERO) > 0) {
			acDetail = new AcDetail();
			acDetail.setDbCr("D");
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
			acDetail.setDbCr("D");
			acDetail.setAcctCode("IOV");
			acDetail.setTxAmt(tx.getBreachAmt());
			acDetail.setCustNo(tx.getCustNo());
			acDetail.setFacmNo(tx.getFacmNo());
			acDetail.setBormNo(tx.getBormNo());
			lAcDetail.add(acDetail);
		}
		// 清償違約金
		if (tx.getCloseBreachAmt().compareTo(BigDecimal.ZERO) > 0) {
			acDetail = new AcDetail();
			acDetail.setDbCr("D");
			acDetail.setAcctCode("IOP");
			acDetail.setTxAmt(tx.getCloseBreachAmt());
			acDetail.setCustNo(tx.getCustNo());
			acDetail.setFacmNo(tx.getFacmNo());
			acDetail.setBormNo(tx.getBormNo());
			lAcDetail.add(acDetail);
		}
		wkTempAmt = wkTempAmt.add(tx.getPrincipal().add(tx.getInterest()).add(tx.getDelayInt()).add(tx.getBreachAmt())
				.add(tx.getCloseBreachAmt()));
	}

	// 還原催收檔
	private void RestoredOverdueRoutine(int iCaseCloseCode, LoanBorTx tx) throws LogicException {
		this.info("RestoredOverdueRoutine ... ");
		this.info("   iCaseCloseCode = " + iCaseCloseCode);

		tLoanOverdue = loanOverdueService
				.holdById(new LoanOverdueId(tx.getCustNo(), tx.getFacmNo(), tx.getBormNo(), 1));
		if (tLoanOverdue == null) {
			throw new LogicException(titaVo, "E0006",
					"催收呆帳檔  額度編號 = " + tx.getFacmNo() + " 撥款序號 = " + tx.getBormNo() + " 催收序號 = " + 1); // 鎖定資料時，發生錯誤
		}
		switch (iCaseCloseCode) { // 結案區分
		case 3: // 3:轉催收
			try {
				loanOverdueService.delete(tLoanOverdue);
			} catch (DBException e) {
				throw new LogicException(titaVo, "E0007",
						"催收呆帳檔  額度編號 = " + tx.getFacmNo() + " 撥款序號 = " + tx.getBormNo() + " 催收序號 = " + 1); // 更新資料時，發生錯誤
			}
		case 4: // 4:催收戶本人清償
		case 5: // 5:催收戶保證人代償
		case 6: // 6:催收戶強制執行
			tLoanOverdue.setOvduBal(tx.getPrincipal());
			if (tLoanOverdue.getOvduBal().compareTo(tLoanOverdue.getOvduPrinAmt()) > 0) {
				tLoanOverdue.setOvduPrinBal(tLoanOverdue.getOvduPrinAmt());
				tLoanOverdue.setOvduIntBal(tLoanOverdue.getOvduBal().subtract(tLoanOverdue.getOvduPrinBal()));
			} else {
				tLoanOverdue.setOvduPrinBal(tLoanOverdue.getOvduBal());
				tLoanOverdue.setOvduIntBal(BigDecimal.ZERO);
			}
			tLoanOverdue.setStatus(1); // 1:催收
			break;
		case 7: // 7:轉列呆帳
		case 8: // 8:催收部分轉呆
			tLoanOverdue.setOvduBal(tLoanOverdue.getOvduBal().add(tx.getPrincipal()));
			if (tLoanOverdue.getOvduBal().compareTo(tLoanOverdue.getOvduPrinAmt()) > 0) {
				tLoanOverdue.setOvduPrinBal(tLoanOverdue.getOvduPrinAmt());
				tLoanOverdue.setOvduIntBal(tLoanOverdue.getOvduBal().subtract(tLoanOverdue.getOvduPrinBal()));
			} else {
				tLoanOverdue.setOvduPrinBal(tLoanOverdue.getOvduBal());
				tLoanOverdue.setOvduIntBal(BigDecimal.ZERO);
			}
			tLoanOverdue.setBadDebtAmt(tLoanOverdue.getBadDebtAmt().subtract(tx.getPrincipal()));
			tLoanOverdue.setBadDebtBal(tLoanOverdue.getBadDebtBal().subtract(tx.getPrincipal()));
			if (tLoanOverdue.getBadDebtAmt().compareTo(BigDecimal.ZERO) > 0) {
				tLoanOverdue.setStatus(2); // 2.部分轉呆
			} else {
				tLoanOverdue.setStatus(1); // 1:催收
			}
			break;
		default:
			break;
		}
		tLoanOverdue.setAcDate(titaVo.getEntDyI());
		try {
			loanOverdueService.update(tLoanOverdue);
		} catch (DBException e) {
			throw new LogicException(titaVo, "E0007",
					"催收呆帳檔  額度編號 = " + tx.getFacmNo() + " 撥款序號 = " + tx.getBormNo() + " 催收序號 = " + 1); // 更新資料時，發生錯誤
		}
	}

}