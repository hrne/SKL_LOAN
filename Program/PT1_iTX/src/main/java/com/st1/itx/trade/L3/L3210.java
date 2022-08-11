package com.st1.itx.trade.L3;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.st1.itx.Exception.DBException;
import com.st1.itx.Exception.LogicException;
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
 * 1.此功能供存入暫收款或存入期票時登錄用.
 * 2.暫收款登錄交易之支票繳款功能，增加支票用途欄，如：還本，期款…，使支票兌現時可自動入帳。
 * 3.支票繳款時需入到該戶的額度編號下(可多個額度)
 * 4.暫收原因為債協暫收款：
 * 4.1.依案件種類出帳：債協暫收款－收款專戶(601776戶號)、 債協暫收款－抵繳款 (債協)、前調暫收款－抵繳款 (調解)、更生暫收款－抵繳款  (更生,清算)
 * 4.2.專戶的匯入款，為一般債權人撥付款(一般債權撥付資料檔，提兌日 = 入帳日，金額相同)時，自動轉入該戶債協暫收款。                           
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
	public LoanBorTxService loanBorTxService;;

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
	private String iTempReasonCodeX;
	private int iTempSourceCode;
	private int iOverRpFacmNo;
	private int iRpCode = 0; // 還款來源
	private BigDecimal iTempAmt;
	private LoanCheque tLoanCheque;
	private LoanChequeId tLoanChequeId;
	private LoanBorTx tLoanBorTx;
	private LoanBorTxId tLoanBorTxId;
	private TempVo tTempVo;
	private ArrayList<BaTxVo> baTxList;
	private AcDetail acDetail;
	private List<AcDetail> lAcDetail = new ArrayList<AcDetail>();
	private boolean isRepaidFee;
	private int repayFacmNo;

	// initialize variable
	@PostConstruct
	public void init() {
		this.iCustNo = 0;
		this.iFacmNo = 0;
		this.iOverRpFacmNo = 0;
		this.iEntryDate = 0;
		this.iChequeAcct = 0;
		this.iChequeNo = 0;
		this.iTempReasonCode = 0;
		this.iTempAmt = BigDecimal.ZERO;
		this.tTempVo = new TempVo();
		this.lAcDetail = new ArrayList<AcDetail>();
		this.isRepaidFee = false;
		this.repayFacmNo = 0;
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
		iTempReasonCodeX = titaVo.getParam("TempReasonCodeX");
		iTempSourceCode = this.parse.stringToInteger(titaVo.getParam("TempSourceCode"));
		iTempAmt = this.parse.stringToBigDecimal(titaVo.getParam("TimTempAmt"));
		iFacmNo = this.parse.stringToInteger(titaVo.getParam("FacmNo"));
		if (titaVo.get("RpCode1") != null) {
			iRpCode = this.parse.stringToInteger(titaVo.getParam("RpCode1"));
		}
		if (iFacmNo > 0) {
			this.repayFacmNo = iFacmNo;
		}
		if (iFacmNo == 0 && titaVo.isTrmtypBatch()) {
			iOverRpFacmNo = this.parse.stringToInteger(titaVo.getParam("OverRpFacmNo"));
			iFacmNo = iOverRpFacmNo;
		}

		// 系統交易記錄檔的金額為實際支付金額或暫收款金額
		titaVo.setTxAmt(iTempAmt);

		// 費用抵繳是否抵繳(整批入帳)
		this.isRepaidFee = false;
		if (titaVo.get("PayFeeFlag") != null && "Y".equals(titaVo.get("PayFeeFlag"))) {
			this.isRepaidFee = true;
		}

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

		// 訂正放款交易內容檔by交易
		if (titaVo.isHcodeErase()) {
			loanCom.setFacmBorTxHcodeByTx(iCustNo, titaVo);
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
				lAcDetail.addAll(this.txBuffer.getAcDetailList());

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

				addLoanBorTxRoutine();
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

				// 貸方 收付欄
				acPaymentCom.setTxBuffer(this.getTxBuffer());
				acPaymentCom.run(titaVo);
				lAcDetail.addAll(this.txBuffer.getAcDetailList());
				addLoanBorTxRoutine();
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
				lAcDetail.addAll(this.txBuffer.getAcDetailList());

				// 貸方 暫收可抵繳
				acDetailSettleUnpaid();

				break;
			case 10: // AML凍結／未確定
				// 借方 收付欄
				acPaymentCom.setTxBuffer(this.getTxBuffer());
				acPaymentCom.run(titaVo);
				lAcDetail.addAll(this.txBuffer.getAcDetailList());
				// 貸方 TAM
				acDetail = new AcDetail();
				acDetail.setDbCr("C");
				acDetail.setAcctCode("TAM");
				acDetail.setCurrencyCode(titaVo.getParam("CurrencyCode"));
				acDetail.setTxAmt(iTempAmt);
				acDetail.setCustNo(iCustNo);
				acDetail.setSlipNote(titaVo.getParam("RpRemark1"));
				lAcDetail.add(acDetail);
				addLoanBorTxRoutine();
				break;
			default:
				// 借方 收付欄
				acPaymentCom.setTxBuffer(this.getTxBuffer());
				acPaymentCom.run(titaVo);
				lAcDetail.addAll(this.txBuffer.getAcDetailList());
				// 貸方 暫收可抵繳
				acDetailSettleUnpaid();
				break;
			}

			// 產生會計分錄
			this.txBuffer.setAcDetailList(lAcDetail);
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
		this.baTxList = baTxCom.settingUnPaid(titaVo.getEntDyI(), iCustNo, this.repayFacmNo, 0, iRepayType, iTempAmt,
				titaVo);

		if (iTempAmt.compareTo(BigDecimal.ZERO) > 0) {
			// 借方：交易金額(已出)

			// 借方：暫收款金額 (暫收借)
			loanCom.settleTempAmt(this.baTxList, this.lAcDetail, titaVo);

			// 貸方：累溢收入帳(暫收貸)
			loanCom.settleOverflow(lAcDetail, titaVo);

			// 新增放款交易內容檔
			addLoanBorTxRoutine();

		}
		// 費用
		if (isRepaidFee) {
			// 收回費用處理(新增帳務及放款交易內容檔)
			loanCom.settleFeeRoutine(this.baTxList, iRpCode, iEntryDate, new TempVo(), lAcDetail, titaVo);
		}
		// 無費用項目可抵繳
		if (iRpCode == 90 && lAcDetail.size() == 0) {
			throw new LogicException(titaVo, "E0010", "無費用項目可抵繳，請執行<轉暫收> "); // 功能選擇錯誤
		}
		
	}

	// 新增放款交易內容檔
	private void addLoanBorTxRoutine() throws LogicException {
		this.info("addLoanBorTxRoutine ... ");
		tTempVo.clear();
		tLoanBorTx = new LoanBorTx();
		tLoanBorTxId = new LoanBorTxId();
		loanCom.setFacmBorTx(tLoanBorTx, tLoanBorTxId, iCustNo, iFacmNo, titaVo);
// TempReasonCodeX
//		00	債協暫收款
//		01	溢繳
//		02	不足利息
//		03	期票
//		04	本金異動
//		05	積欠期款
//		06	即期票現金
//		07	火險、帳管
//		08	兌現票入帳
//		09	其他
//		10	AML凍結／未確定

		if (iTempReasonCode == 0 || iTempReasonCode == 3 || iTempReasonCode == 6 || iTempReasonCode == 10) {
			tLoanBorTx.setDesc(iTempReasonCodeX + "登錄");
		} else {
			tLoanBorTx.setDesc("暫收款登錄");
			tTempVo.putParam("Note", iTempReasonCodeX);
		}
		tLoanBorTx.setEntryDate(iEntryDate);
		tLoanBorTx.setRepayCode(iRpCode); // 還款來源
		//
		tLoanBorTx.setDisplayflag("A"); // A:帳務

		// 其他欄位
		tTempVo.putParam("TempReasonCode", iTempReasonCode);
		tTempVo.putParam("BatchNo", titaVo.getBacthNo()); // 整批批號

		if (titaVo.get("RpDscpt1") != null) {
			tTempVo.putParam("DscptCode", titaVo.getParam("RpDscpt1")); // 摘要代碼
		}
		if (titaVo.getBacthNo().trim() != "") {
			tTempVo.putParam("BatchNo", titaVo.getBacthNo()); // 整批批號
			tTempVo.putParam("DetailSeq", titaVo.get("RpDetailSeq1")); // 明細序號
		}

		tLoanBorTx.setOtherFields(tTempVo.getJsonString());

		// 更新放款明細檔及帳務明細檔關聯欄
		loanCom.updBorTxAcDetail(this.tLoanBorTx, lAcDetail);
		try {
			loanBorTxService.insert(tLoanBorTx, titaVo);
		} catch (DBException e) {
			throw new LogicException(titaVo, "E0005", "放款交易內容檔 " + e.getErrorMsg()); // 新增資料時，發生錯誤
		}
	}

}