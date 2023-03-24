package com.st1.itx.trade.L3;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Slice;
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
import com.st1.itx.util.common.AcRepayCom;
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
	AcRepayCom acRepayCom;
	@Autowired
	AcNegCom acNegCom;
	@Autowired
	BaTxCom baTxCom;
	@Autowired
	LoanCom loanCom;
	@Autowired
	AcPaymentCom acPaymentCom;
	@Autowired
	AcDetailCom acDetailCom;

	private TitaVo titaVo;
	private int iCustNo;
	private int iFacmNo;
	private int iEntryDate;
	private int iChequeAcct;
	private int iChequeNo;
	private int iTempReasonCode;
	private int iTempSourceCode;
	private int iOverRpFacmNo;
	private int iRpCode = 0; // 還款來源
	private BigDecimal iTempAmt;
	private LoanCheque tLoanCheque;
	private LoanChequeId tLoanChequeId;
	private LoanBorTx tLoanBorTx;
	private LoanBorTxId tLoanBorTxId;
	private TempVo tTempVo;
	private ArrayList<BaTxVo> baTxList = new ArrayList<BaTxVo>();
	private AcDetail acDetail;
	private List<LoanBorTx> lLoanBorTx = new ArrayList<LoanBorTx>();
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
		this.lLoanBorTx = new ArrayList<LoanBorTx>();
		this.baTxList = new ArrayList<BaTxVo>();
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
		acRepayCom.setTxBuffer(this.txBuffer);
		// 取得輸入資料
		iCustNo = this.parse.stringToInteger(titaVo.getParam("TimCustNo"));
		iFacmNo = this.parse.stringToInteger(titaVo.getParam("FacmNo"));
		iEntryDate = this.parse.stringToInteger(titaVo.getParam("EntryDate"));
		iChequeAcct = this.parse.stringToInteger(titaVo.getParam("ChequeAcct"));
		iChequeNo = this.parse.stringToInteger(titaVo.getParam("ChequeNo"));
		iTempReasonCode = this.parse.stringToInteger(titaVo.getParam("TempReasonCode"));
		iTempSourceCode = this.parse.stringToInteger(titaVo.getParam("TempSourceCode"));
		iTempAmt = this.parse.stringToBigDecimal(titaVo.getParam("TimTempAmt"));
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
		if (iCustNo == this.txBuffer.getSystemParas().getLoanDeptCustNo()) {
			iFacmNo = 0;
			this.repayFacmNo = 0;
		}

		// 系統交易記錄檔的金額為實際支付金額或暫收款金額
		titaVo.setTxAmt(iTempAmt);

		// 期票、即期票 => 業務類別 = 02-支票繳款 else 09-放款
		if (iTempReasonCode == 3 || iTempReasonCode == 6) {
			titaVo.putParam("SECNO", "02");
		} else {
			titaVo.putParam("SECNO", "09");
		}

		// 費用抵繳是否抵繳(整批入帳)
		this.isRepaidFee = false;
		if (titaVo.get("PayFeeFlag") != null && "Y".equals(titaVo.get("PayFeeFlag"))) {
			this.isRepaidFee = true;
		}

		// 07火險、帳管
		if (iTempReasonCode == 7) {
			this.isRepaidFee = true;
		}

		if (iTempSourceCode == 4) { // 支票
			tLoanChequeId = new LoanChequeId(iChequeAcct, iChequeNo);
			if (iTempReasonCode != 8) {
				LoanChequeRoutineB(); // 期票,即期票現金處理
			}
		}

		// 帳務處理
		if (titaVo.isHcodeNormal()) {
			AcDetailRoutine();
		}
		// 更新疑似洗錢交易訪談記錄檔
		if (iRpCode == 1 || iRpCode == 2 || iRpCode == 4) {// 還款來源限1.匯款轉帳,2.銀扣,4.支票
			loanCom.updateMlaundryRecord(iCustNo, 0, 0, iEntryDate, iTempAmt, titaVo);
		}

		// 訂正處理
		if (titaVo.isHcodeErase()) {
			Slice<LoanBorTx> slLoanBortx = loanBorTxService.acDateTxtNoEq(titaVo.getOrgEntdyI() + 19110000,
					titaVo.getOrgKin(), titaVo.getOrgTlr(), titaVo.getOrgTno(), 0, Integer.MAX_VALUE, titaVo);
			this.lLoanBorTx = slLoanBortx == null ? null : slLoanBortx.getContent();
			if (lLoanBorTx == null || lLoanBorTx.size() == 0) {
				throw new LogicException(titaVo, "E0001", "交易明細暫存檔 分行別 = " + titaVo.getOrgKin() + " 經辦代號 = "
						+ titaVo.getOrgTlr() + " 交易序號 = " + titaVo.getOrgTno()); // 查詢資料不存在
			}
			for (LoanBorTx tx : lLoanBorTx) {
				loanCom.checkEraseCustNoTxSeqNo(tx.getCustNo(), titaVo);// 檢查到同戶帳務交易需由最近一筆交易開始訂正
				loanCom.setFacmBorTxHcode(tx.getCustNo(), tx.getFacmNo(), tx.getBorxNo(), titaVo);
			}
		}

		// 暫收款交易新增帳務及更新放款交易內容檔
		if (titaVo.isHcodeNormal()) {
			if (iTempReasonCode == 3 || iTempReasonCode == 6) {
				// 借方 應收票據
				this.txBuffer.addAllAcDetailList(lAcDetail);
				// 貸方 收付欄
				acPaymentCom.setTxBuffer(this.getTxBuffer());
				acPaymentCom.run(titaVo);

				// 產生會計分錄
				acDetailCom.setTxBuffer(this.txBuffer);
				acDetailCom.run(titaVo);
				this.setTxBuffer(acDetailCom.getTxBuffer());

			} else {
				acRepayCom.settleTempRun(this.lLoanBorTx, this.baTxList, this.lAcDetail, titaVo);
			}
		}

		this.addList(this.totaVo);
		return this.sendList();

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
			// tLoanCheque.setTsibFlag(titaVo.getParam("TsibFlag"));
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
		// 貸方 暫收及待結轉帳項-擔保放款
		switch (iTempReasonCode) {
		case 0: // 債協暫收款
			// 貸方 :最大債權 => 債協暫收款、一般債權 => 暫收可抵繳

			/* 5.找債協專戶的匯入款，為一般債權人撥付款 */
			boolean havedata = false;
			if (iCustNo == this.txBuffer.getSystemParas().getNegDeptCustNo()) {
				List<AcDetail> lAcDetailApp02 = new ArrayList<AcDetail>();
				lAcDetailApp02 = acNegCom.getNegAppr02CustNo(iEntryDate, iTempAmt, iCustNo, titaVo);
				if (lAcDetailApp02.size() > 0) {
					havedata = true;
					lAcDetail.addAll(lAcDetailApp02);
					acDetail = new AcDetail();
					for (int i = 0; i < lAcDetailApp02.size(); i++) {
						acDetail = lAcDetailApp02.get(i);
						addLoanBorTxRoutine(acDetail);
						this.info("SlipNote2 = " + acDetail.getSlipNote());
					}
				}
			}
			if (!havedata) {// 專戶若找不到一般債權資料,則須出T10入暫收款
				acDetail = new AcDetail();
				String acctCode = acNegCom.getAcctCode(iCustNo, titaVo);
				acDetail.setDbCr("C");
				acDetail.setAcctCode(acctCode);
				acDetail.setCurrencyCode(titaVo.getParam("CurrencyCode"));
				acDetail.setTxAmt(iTempAmt);
				acDetail.setCustNo(iCustNo);
				if ("TAV".equals(acctCode)) {
					TempVo tempVo = new TempVo();
					tempVo = acNegCom.getReturnAcctCode(iCustNo, titaVo);
					acDetail.setCustNo(parse.stringToInteger(tempVo.getParam("CustNo")));
					acDetail.setFacmNo(parse.stringToInteger(tempVo.getParam("FacmNo")));
					acDetail.setSumNo("092"); // 轉暫收款可抵繳
					acDetail.setSlipNote("一般債權");
					// acDetail.setJsonFields(tTempVo.getJsonString());

				} else {
					acDetail.setSumNo("094"); // 轉債協暫收款
				}
				lAcDetail.add(acDetail);
				this.info("SlipNote = " + acDetail.getSlipNote());
				addLoanBorTxRoutine(acDetail);
			}
			break;
		case 3: // 期票
		case 6: // 即期票
			acDetail = new AcDetail();
			acDetail.setDbCr("D");
			acDetail.setAcctCode("RCK"); // 應收票據－一般票據－非核心
			acDetail.setCurrencyCode(titaVo.getParam("CurrencyCode"));
			acDetail.setTxAmt(iTempAmt);
			acDetail.setCustNo(iCustNo);
			lAcDetail.add(acDetail);
			addLoanBorTxRoutine(acDetail);
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
			// 應繳試算
			settleUnpaid();

			break;
		case 10: // AML凍結／未確定
			// 貸方 TAM
			acDetail = new AcDetail();
			acDetail.setDbCr("C");
			acDetail.setAcctCode("TAM");
			acDetail.setSumNo("092"); // 暫收款
			acDetail.setCurrencyCode(titaVo.getParam("CurrencyCode"));
			acDetail.setTxAmt(iTempAmt);
			acDetail.setCustNo(iCustNo);
			lAcDetail.add(acDetail);
			addLoanBorTxRoutine(acDetail);
			break;
		default:
			// 應繳試算
			settleUnpaid();
			break;
		}
	}

	// 應繳試算
	private void settleUnpaid() throws LogicException {
		this.baTxList = new ArrayList<BaTxVo>();
		int iRepayType = parse.stringToInteger(titaVo.getParam("RpType1"));
		if (iRepayType <= 3) {
			iRepayType = 9; // 09-其他
		}
		// call 應繳試算
		this.baTxList = baTxCom.settingUnPaid(titaVo.getEntDyI(), iCustNo, this.repayFacmNo, 0, iRepayType, iTempAmt,
				titaVo);
		// 貸方：暫收可抵繳
		if (iTempAmt.compareTo(BigDecimal.ZERO) > 0) {
			AcDetail acDetail = new AcDetail();
			acDetail.setAcctCode("TAV");
			acDetail.setCustNo(iCustNo);
			acDetail.setFacmNo(iFacmNo);

			// 新增放款交易內容檔
			addLoanBorTxRoutine(acDetail);
		}
		// 費用
		if (!isRepaidFee) {
			for (BaTxVo ba : this.baTxList) {
				ba.setAcctAmt(BigDecimal.ZERO);
			}
		}

		// 暫收抵繳，無費用項目可抵繳
		if (iRpCode == 90) {
			boolean isFee = false;
			for (BaTxVo ba : this.baTxList) {
				if (ba.getRepayType() >= 4 && ba.getAcctAmt().compareTo(BigDecimal.ZERO) > 0) {
					isFee = true;
					break;
				}
			}
			if (!isFee) {
				throw new LogicException(titaVo, "E0010", "無費用項目可抵繳，請執行<轉暫收> "); // 功能選擇錯誤
			}
		}

	}

	// 新增放款交易內容檔
	private void addLoanBorTxRoutine(AcDetail ac) throws LogicException {
		this.info("addLoanBorTxRoutine ... ");
		tTempVo.clear();
		tLoanBorTx = new LoanBorTx();
		tLoanBorTxId = new LoanBorTxId();
		loanCom.setFacmBorTx(tLoanBorTx, tLoanBorTxId, ac.getCustNo(), ac.getFacmNo(), titaVo);

		// TempReasonCode
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
		// 3210 暫收款登錄
		// 3211 債協暫收款登錄
		// 3212 期票登錄
		// 3213 即期票現金登錄
		// 3214 支票兌現
		// 3215 AML凍結／未確定
		switch (iTempReasonCode) {
		case 0:
			tLoanBorTx.setTxDescCode("3211");
			break;
		case 3:
			tLoanBorTx.setTxDescCode("3212");
			break;
		case 6:
			tLoanBorTx.setTxDescCode("3213");
			break;
		case 8:
			tLoanBorTx.setTxDescCode("3214");
			break;
		case 10:
			tLoanBorTx.setTxDescCode("3215");
			break;
		default:
			tLoanBorTx.setTxDescCode("3210");
			break;
		}
		tLoanBorTx.setEntryDate(iEntryDate);
		tLoanBorTx.setRepayCode(iRpCode); // 還款來源
		//
		tLoanBorTx.setDisplayflag("A"); // A:帳務
		tLoanBorTx.setAcctCode(ac.getAcctCode()); // 業務科目

		// 0: 債協暫收款 10: AML凍結／未確定
		if (iTempReasonCode == 0 || iTempReasonCode == 10) {
			tLoanBorTx.setTxAmt(ac.getTxAmt());
		}

		// 其他欄位
		tTempVo.putParam("TempReasonCode", iTempReasonCode);

		tLoanBorTx.setOtherFields(tTempVo.getJsonString());

		this.lLoanBorTx.add(tLoanBorTx);
		if (iTempReasonCode == 3 || iTempReasonCode == 6) {
			try {
				loanBorTxService.insertAll(this.lLoanBorTx);
			} catch (DBException e) {
				throw new LogicException(titaVo, "E0005", "放款交易內容檔 " + e.getErrorMsg()); // 新增資料時，發生錯誤
			}
		}
	}

}