package com.st1.itx.trade.L3;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.st1.itx.Exception.LogicException;
import com.st1.itx.Exception.DBException;
import com.st1.itx.dataVO.TempVo;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.dataVO.TotaVo;
import com.st1.itx.db.domain.AcDetail;
import com.st1.itx.db.domain.FacMain;
import com.st1.itx.db.domain.FacMainId;
import com.st1.itx.db.domain.LoanBorTx;
import com.st1.itx.db.domain.LoanBorTxId;
import com.st1.itx.db.domain.LoanCheque;
import com.st1.itx.db.domain.LoanChequeId;
import com.st1.itx.db.service.FacMainService;
import com.st1.itx.db.service.LoanBorTxService;
import com.st1.itx.db.service.LoanChequeService;
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.common.AcDetailCom;
import com.st1.itx.util.common.AcNegCom;
import com.st1.itx.util.common.AcPaymentCom;
import com.st1.itx.util.common.BaTxCom;
import com.st1.itx.util.common.LoanCom;
import com.st1.itx.util.common.data.BaTxVo;
import com.st1.itx.util.date.DateUtil;
import com.st1.itx.util.parse.Parse;

/*
 * L3210 暫收款登錄
 * a.此功能供預收客戶現金或期票時登錄暫收款用.
 * b.如係預收期票者,支票帳號,票號,開票日,支票銀行及分行之值,會保留上一筆者,僅清除金額欄,以便利連號支票輸入用.
 * c.暫收款登錄交易之支票繳款功能，增加支票用途欄，如：還本，期款…，使支票兌現時可自動入帳。
 * d.支票繳款時需入到該戶的額度編號下
 * d1.若多筆額度期款收回失敗時，帳掛在第一筆的額度編號。
 * d2.若多筆額度期款收回成功且有溢收金額時，帳掛在第一筆的額度編號。
 */
/*
 * Tita
 * TimCustNo=9,7
 * CustId=X,10
 * ApplNo=9,7
 * EntryDate=9,7
 * CurrencyCode=X,3
 * TimTempAmt=9,14.2
 * TempReasonCode=9,2
 * TempSourceCode=9,2
 * ChequeName=X,60
 * ChequeAcct=9,9
 * ChequeNo=9,7
 * ChequeDate=9,7
 * AreaCode=9,2
 * BankCode=X,7
 * BktwFlag=X,1
 * TsibFlag=X,1
 * MediaFlag=X,1
 * OutsideCode=9,1
 * UsageCode=9,2
 * ServiceCenter=X,1
 * CreditorId=X,10
 * CreditorBank=X,7
 * OtherAcctCode=9,3
 * ReceiptNo=X,5
 */
/**
 * L3210 暫收款登錄
 * 
 * @author iza
 * @version 1.0.0
 */
@Service("L3210")
@Scope("prototype")
public class L3210 extends TradeBuffer {

	/* DB服務注入 */
	@Autowired
	public LoanChequeService loanChequeService;
	@Autowired
	public FacMainService facMainService;
	@Autowired
	public LoanBorTxService loanBorTxService;

	@Autowired
	Parse parse;
	@Autowired
	DateUtil dDateUtil;
	@Autowired
	AcDetailCom acDetailCom;
	@Autowired
	AcPaymentCom acPaymentCom;
	@Autowired
	AcNegCom acNegCom;
	@Autowired
	BaTxCom baTxCom;
	@Autowired
	LoanCom loanCom;

	private TitaVo titaVo;
	private int iCustNo;
	private int iFacmNo;
	private int iEntryDate;
	private int iChequeAcct;
	private int iChequeNo;
	private int iTempReasonCode;
	private int iTempSourceCode;
	private BigDecimal iTempAmt;
	private LoanCheque tLoanCheque;
	private LoanChequeId tLoanChequeId;
	private BigDecimal wkTempAmt;
	private LoanBorTx tLoanBorTx;
	private LoanBorTxId tLoanBorTxId;
	private TempVo tTempVo;
	Boolean isBortx;
	private ArrayList<BaTxVo> baTxList;
	AcDetail acDetail;
	List<AcDetail> lAcDetail = new ArrayList<AcDetail>();

	// initialize variable
	@PostConstruct
	public void init() {
		this.iCustNo = 0;
		this.iFacmNo = 0;
		this.iEntryDate = 0;
		this.iChequeAcct = 0;
		this.iChequeNo = 0;
		this.iTempReasonCode = 0;
		this.iTempAmt = BigDecimal.ZERO;
		this.wkTempAmt = BigDecimal.ZERO;
		this.tTempVo = new TempVo();
		this.lAcDetail = new ArrayList<AcDetail>();
	}

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active L3210 ");
		this.totaVo.init(titaVo);
		this.titaVo = titaVo;
		baTxCom.setTxBuffer(this.txBuffer);
		acNegCom.setTxBuffer(this.txBuffer);
		loanCom.setTxBuffer(this.txBuffer);

		// 取得輸入資料
		iCustNo = this.parse.stringToInteger(titaVo.getParam("TimCustNo"));
		iFacmNo = this.parse.stringToInteger(titaVo.getParam("FacmNo"));
		iEntryDate = this.parse.stringToInteger(titaVo.getParam("EntryDate"));
		iChequeAcct = this.parse.stringToInteger(titaVo.getParam("ChequeAcct"));
		iChequeNo = this.parse.stringToInteger(titaVo.getParam("ChequeNo"));
		iTempReasonCode = this.parse.stringToInteger(titaVo.getParam("TempReasonCode"));
		iTempSourceCode = this.parse.stringToInteger(titaVo.getParam("TempSourceCode"));
		iTempAmt = this.parse.stringToBigDecimal(titaVo.getParam("TimTempAmt"));
		// 交易金額
		if (iTempReasonCode == 3 || iTempReasonCode == 6) // 期票、即期票
			this.isBortx = false;
		else
			this.isBortx = true;

		// 暫收款金額 存入暫收為正
		wkTempAmt = iTempAmt;

		if (iTempSourceCode == 4) { // 支票
			tLoanChequeId = new LoanChequeId(iChequeAcct, iChequeNo);
			if (iTempReasonCode == 8) {
				LoanChequeRoutineA(); // 兌現票入帳處理
			} else {
				LoanChequeRoutineB(); // 期票,即期票現金處理
			}
		}

		// 帳務處理
		AcDetailRoutine();

		// 放款交易內容檔
		if (this.isBortx) {
			if (titaVo.isHcodeNormal()) {
				addLoanBorTxRoutine();
			} else {
				loanCom.setFacmBorTxHcode(iCustNo, iFacmNo, titaVo);
			}
		}

		this.addList(this.totaVo);
		return this.sendList();
	}

	// 兌現票入帳處理
	private void LoanChequeRoutineA() throws LogicException {
		this.info("LoanCheckRoutine ...");
		acPaymentCom.setTxBuffer(this.getTxBuffer());
		acPaymentCom.loanCheque(titaVo);
	}

	// 期票,即期票現金處理
	private void LoanChequeRoutineB() throws LogicException {
		// 正常交易
		if (titaVo.isHcodeNormal()) {
			tLoanCheque = new LoanCheque();
			tLoanCheque.setCustNo(iCustNo);
			tLoanCheque.setChequeAcct(iChequeAcct);
			tLoanCheque.setChequeNo(iChequeNo);
			tLoanCheque.setLoanChequeId(tLoanChequeId);
			tLoanCheque.setStatusCode("0"); // 0: 未處理
			tLoanCheque.setProcessCode("0"); // 0: 未處理
			tLoanCheque.setAcDate(0);
			tLoanCheque.setKinbr("");
			tLoanCheque.setTellerNo("");
			tLoanCheque.setTxtNo("");
			tLoanCheque.setReceiveDate(this.txBuffer.getTxCom().getTbsdy());
			tLoanCheque.setEntryDate(0);
			tLoanCheque.setCurrencyCode(titaVo.getParam("CurrencyCode"));
			tLoanCheque.setChequeAmt(iTempAmt);
			tLoanCheque.setChequeName(titaVo.getParam("ChequeName"));
			tLoanCheque.setChequeDate(this.parse.stringToInteger(titaVo.getParam("ChequeDate")));
			tLoanCheque.setAreaCode(titaVo.getParam("AreaCode"));
			tLoanCheque.setBankCode(titaVo.getParam("BankCode"));
			tLoanCheque.setOutsideCode(titaVo.getParam("OutsideCode"));
			tLoanCheque.setBktwFlag(titaVo.getParam("BktwFlag"));
			tLoanCheque.setTsibFlag(titaVo.getParam("TsibFlag"));
			tLoanCheque.setMediaFlag(titaVo.getParam("MediaFlag"));
			tLoanCheque.setUsageCode(titaVo.getParam("UsageCode"));
			tLoanCheque.setServiceCenter(titaVo.getParam("ServiceCenter"));
			tLoanCheque.setCreditorId(titaVo.getParam("CreditorId"));
			tLoanCheque.setCreditorBankCode(titaVo.getParam("CreditorBankCode"));
			tLoanCheque.setOtherAcctCode("");
			tLoanCheque.setReceiptNo("");
			tLoanCheque.setRepaidAmt(new BigDecimal(0));
			try {
				loanChequeService.insert(tLoanCheque, titaVo);
			} catch (DBException e) {
				throw new LogicException(titaVo, "E0005",
						"戶號 = " + iCustNo + " 支票帳號 = " + iChequeAcct + " 支票號碼 = " + iChequeNo + " " + e.getErrorMsg()); // 新增資料時，發生錯誤
			}
		}
		// 訂正
		if (titaVo.isHcodeErase()) {
			tLoanCheque = loanChequeService.holdById(tLoanChequeId, titaVo);
			if (tLoanCheque == null) {
				throw new LogicException(titaVo, "E0006",
						"戶號 = " + iCustNo + " 支票帳號 = " + iChequeAcct + " 支票號碼 = " + iChequeNo); // 鎖定資料時，發生錯誤
			}
			try {
				loanChequeService.delete(tLoanCheque, titaVo);
			} catch (DBException e) {
				throw new LogicException(titaVo, "E0008",
						"戶號 = " + iCustNo + " 支票帳號 = " + iChequeAcct + " 支票號碼 = " + iChequeNo + " " + e.getErrorMsg()); // 刪除資料時，發生錯誤
			}
		}
	}

	// 帳務處理
	private void AcDetailRoutine() throws LogicException {
		this.info("AcDetailRoutine ...");
		this.info("   isBookAcYes = " + this.txBuffer.getTxCom().isBookAcYes());
		this.info("   getBookAc   = " + this.txBuffer.getTxCom().getBookAc());

		if (this.txBuffer.getTxCom().isBookAcYes()) {
			// 借方 收付欄

			// 貸方 暫收及待結轉帳項-擔保放款
			switch (iTempReasonCode) {
			case 0: // 債協暫收款
				// 借方 收付欄
				acPaymentCom.setTxBuffer(this.getTxBuffer());
				acPaymentCom.run(titaVo);
				// 貸方 債協暫收款
				acDetail = new AcDetail();
				acDetail.setDbCr("C");
				acDetail.setAcctCode(acNegCom.getAcctCode(iCustNo, titaVo));
				acDetail.setCurrencyCode(titaVo.getParam("CurrencyCode"));
				acDetail.setTxAmt(iTempAmt);
				acDetail.setCustNo(iCustNo);
				acDetail.setSlipNote(titaVo.getParam("RpRemark1"));
				lAcDetail.add(acDetail);
				this.info("SlipNote = " + acDetail.getSlipNote());
				/* 5.找債協專戶的匯入款，為一般債權人撥付款 */
				if (iCustNo == this.txBuffer.getSystemParas().getNegDeptCustNo()) {
					List<AcDetail> lAcDetailApp02 = new ArrayList<AcDetail>();
					lAcDetailApp02 = acNegCom.getNegAppr02CustNo(iEntryDate, iTempAmt, iCustNo, titaVo);
					if (lAcDetailApp02.size() > 0) {
						acDetail = new AcDetail();
						acDetail.setDbCr("D");
						acDetail.setAcctCode(acNegCom.getAcctCode(iCustNo, titaVo));
						acDetail.setCurrencyCode(titaVo.getParam("CurrencyCode"));
						acDetail.setTxAmt(iTempAmt);
						acDetail.setCustNo(iCustNo);
						acDetail.setSlipNote("一般債權撥付");
						lAcDetail.add(acDetail);
						// 貸 : 一般債權人撥付款
						lAcDetail.addAll(lAcDetailApp02);
						this.info("SlipNote2 = " + acDetail.getSlipNote());
					}
				}
				this.txBuffer.addAllAcDetailList(lAcDetail);
				break;
			case 3: // 期票
			case 6: // 即期票
				acDetail = new AcDetail();
				acDetail.setDbCr("D");
				acDetail.setAcctCode("RCK");
				acDetail.setCurrencyCode(titaVo.getParam("CurrencyCode"));
				acDetail.setTxAmt(iTempAmt);
				acDetail.setCustNo(iCustNo);
				acDetail.setSlipNote(titaVo.getParam("RpRemark1"));
				lAcDetail.add(acDetail);
				this.txBuffer.addAllAcDetailList(lAcDetail);
				// 貸方 收付欄
				acPaymentCom.setTxBuffer(this.getTxBuffer());
				acPaymentCom.run(titaVo);
				break;
			case 8: // 兌現票入帳
				FacMain tFacMain = facMainService.findById(new FacMainId(iCustNo, iFacmNo), titaVo);
				if (tFacMain == null) {
					throw new LogicException(titaVo, "E0001", "額度主檔 借款人戶號 = " + iCustNo + " 額度編號 = " + iFacmNo); // 查詢資料不存在
				}
				if (tFacMain.getActFg() == 1) {
					throw new LogicException(titaVo, "E0021",
							"額度檔 戶號 = " + tFacMain.getCustNo() + " 額度編號 =  " + tFacMain.getFacmNo()); // 該筆資料待放行中
				}
				// 借方 收付欄
				acPaymentCom.setTxBuffer(this.getTxBuffer());
				acPaymentCom.run(titaVo);
				// 貸方
				if (titaVo.isTrmtypBatch()) {
					acDetailSettleUnpaid();
				} else {
					acDetail = new AcDetail();
					acDetail.setDbCr("C");
					acDetail.setAcctCode("TAV");
					acDetail.setCurrencyCode(titaVo.getParam("CurrencyCode"));
					acDetail.setTxAmt(wkTempAmt);
					acDetail.setCustNo(iCustNo);
					acDetail.setFacmNo(iFacmNo);
					acDetail.setSlipNote(titaVo.getParam("RpRemark1"));
					lAcDetail.add(acDetail);
				}
				this.txBuffer.addAllAcDetailList(lAcDetail);
				break;
			case 10: // AML凍結／未確定
				// 借方 收付欄
				acPaymentCom.setTxBuffer(this.getTxBuffer());
				acPaymentCom.run(titaVo);
				// 貸方 TAM
				acDetail = new AcDetail();
				acDetail.setDbCr("C");
				acDetail.setAcctCode("TAM");
				acDetail.setCurrencyCode(titaVo.getParam("CurrencyCode"));
				acDetail.setTxAmt(iTempAmt);
				acDetail.setCustNo(iCustNo);
				acDetail.setSlipNote(titaVo.getParam("RpRemark1"));
				lAcDetail.add(acDetail);
				this.txBuffer.addAllAcDetailList(lAcDetail);
				break;
			default:
				// 借方 收付欄
				acPaymentCom.setTxBuffer(this.getTxBuffer());
				acPaymentCom.run(titaVo);
				// 整批入帳自動收回費用
				if (titaVo.isTrmtypBatch()) {
					acDetailSettleUnpaid();
				} else {
					acDetail = new AcDetail();
					acDetail.setDbCr("C");
					acDetail.setAcctCode("TAV");
					acDetail.setCurrencyCode(titaVo.getParam("CurrencyCode"));
					acDetail.setTxAmt(wkTempAmt);
					acDetail.setCustNo(iCustNo);
					acDetail.setFacmNo(iFacmNo);
					acDetail.setSlipNote(titaVo.getParam("RpRemark1"));
					lAcDetail.add(acDetail);
				}
				this.txBuffer.addAllAcDetailList(lAcDetail);
				break;
			}

			// 產生會計分錄
			acDetailCom.setTxBuffer(this.txBuffer);
			acDetailCom.run(titaVo);
		}
	}

	// 貸方費用帳務處理
	private void acDetailSettleUnpaid() throws LogicException {
		this.baTxList = new ArrayList<BaTxVo>();
		int iRepayType = parse.stringToInteger(titaVo.getParam("RpType1"));
		if (iRepayType <= 3) {
			iRepayType = 9; // 09-其他
		}
		// call 應繳試算
		this.baTxList = baTxCom.settingUnPaid(iEntryDate, iCustNo, iFacmNo, 0, iRepayType, iTempAmt, titaVo);
		if (this.baTxList != null) {
			for (BaTxVo ba : this.baTxList) {
				if (ba.getAcctAmt().compareTo(BigDecimal.ZERO) > 0) {
					acDetail = new AcDetail();
					acDetail.setDbCr(ba.getDbCr());
					acDetail.setAcctCode(ba.getAcctCode());
					acDetail.setTxAmt(ba.getAcctAmt());
					acDetail.setCustNo(ba.getCustNo());
					acDetail.setFacmNo(ba.getFacmNo());
					acDetail.setBormNo(ba.getBormNo());
					acDetail.setRvNo(ba.getRvNo());
					acDetail.setReceivableFlag(ba.getReceivableFlag());
					lAcDetail.add(acDetail);
				}
			}
		}
		// 暫收款金額
		wkTempAmt = baTxCom.getTempAmt();
	}

	// 新增放款交易內容檔
	private void addLoanBorTxRoutine() throws LogicException {
		this.info("addLoanBorTxRoutine ... ");

		tLoanBorTx = new LoanBorTx();
		tLoanBorTxId = new LoanBorTxId();
		loanCom.setFacmBorTx(tLoanBorTx, tLoanBorTxId, iCustNo, iFacmNo, titaVo);
		tLoanBorTx.setDesc("暫收款登錄");
		tLoanBorTx.setEntryDate(iEntryDate);
		//
		tLoanBorTx.setDisplayflag("A"); // A:帳務
		tLoanBorTx.setTxAmt(iTempAmt);
		tLoanBorTx.setTempAmt(wkTempAmt);
		if (titaVo.get("OverRpFg") != null && "2".equals(titaVo.getParam("OverRpFg"))) { // 1->短收 2->溢收
			tLoanBorTx.setOverflow(parse.stringToBigDecimal(titaVo.getParam("OverRpAmt")));
		}

		// 其他欄位
		tTempVo.clear();
		tTempVo.putParam("TempReasonCode", iTempReasonCode);
		tTempVo.putParam("TempSourceCode", iTempSourceCode);
		tTempVo.putParam("BatchNo", titaVo.getBacthNo()); // 整批批號

		if (titaVo.get("RpCode1") != null) {
			tTempVo.putParam("RepayCode", titaVo.getParam("RpCode1")); // 還款來源
		}
		if (titaVo.get("RpDscpt1") != null) {
			tTempVo.putParam("DscptCode", titaVo.getParam("RpDscpt1")); // 摘要代碼
		}
		if (titaVo.getBacthNo().trim() != "") {
			tTempVo.putParam("BatchNo", titaVo.getBacthNo()); // 整批批號
			tTempVo.putParam("DetailSeq", titaVo.get("RpDetailSeq1")); // 明細序號
		}
		if (titaVo.get("AcctFee") != null && titaVo.get("AcctFee") != "0") {
			tTempVo.putParam("AcctFee", titaVo.getParam("AcctFee"));
		}
		if (titaVo.get("ModifyFee") != null && titaVo.get("ModifyFee") != "0") {
			tTempVo.putParam("ModifyFee", titaVo.getParam("ModifyFee"));
		}
		if (titaVo.get("FireFee") != null && titaVo.get("FireFee") != "0") {
			tTempVo.putParam("FireFee", titaVo.getParam("FireFee"));
		}
		if (titaVo.get("LawFee") != null && titaVo.get("LawFee") != "0") {
			tTempVo.putParam("LawFee", titaVo.getParam("LawFee"));
		}
		if (titaVo.get("ShortfallPrin") != null && titaVo.get("ShortfallPrin") != "0") {
			tTempVo.putParam("ShortfallPrin", titaVo.getParam("ShortfallPrin"));
		}
		if (titaVo.get("ShortfallInt") != null && titaVo.get("ShortfallInt") != "0") {
			tTempVo.putParam("ShortfallInt", titaVo.getParam("ShortfallInt"));
		}
		tLoanBorTx.setOtherFields(tTempVo.getJsonString());
		try {
			loanBorTxService.insert(tLoanBorTx, titaVo);
		} catch (DBException e) {
			throw new LogicException(titaVo, "E0005", "放款交易內容檔 " + e.getErrorMsg()); // 新增資料時，發生錯誤
		}
	}

}