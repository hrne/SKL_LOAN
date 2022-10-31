package com.st1.itx.util.common;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

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
import com.st1.itx.db.domain.CdCode;
import com.st1.itx.db.domain.CdCodeId;
import com.st1.itx.db.domain.LoanBorTx;
import com.st1.itx.db.domain.LoanBorTxId;
import com.st1.itx.db.service.CdCodeService;
import com.st1.itx.db.service.LoanBorTxService;
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.common.data.BaTxVo;
import com.st1.itx.util.format.FormatUtil;
import com.st1.itx.util.parse.Parse;

/**
 * 收付欄處理<BR>
 * 1.run 收付欄出帳<BR>
 * 2.settleRun 放款回收帳務產生處理 <BR>
 * 3.settleFeeByTemp 暫收款收回費用處理(新增帳務及放款交易內容檔) <BR>
 * 4.settleTempAmt 暫收款金額 (暫收借) 帳務處理<BR>
 * 5.settleOverflow 累溢收(暫收貸)帳務處理 <BR>
 * 6.updBorTxAcDetail 更新放款明細檔及帳務明細檔關聯欄<BR>
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
	LoanCom loanCom;
	@Autowired
	AcDetailCom acDetailCom;
	@Autowired
	AcReceivableCom acReceivableCom;
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

	// initialize variable
	@PostConstruct
	public void init() {
		this.info("AcRepayCom init ...");
		this.lAcDetail = new ArrayList<AcDetail>();
		this.lLoanBorTx = new ArrayList<LoanBorTx>();
		this.baTxList = new ArrayList<BaTxVo>();
		this.lAcReceivable = new ArrayList<AcReceivable>();
		iRepayCode = 0;
		iEntryDate = 0;
		iCaseCloseCode = 0;
	}

	/*-----------  收付欄(含溢收)出帳  -------------- */
	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("AcRepayCom run ...");
		loanCom.setTxBuffer(this.txBuffer);
		/* 1:應收 2:應付 */
		for (int i = 1; i <= 50; i++) {
			/* 還款來源／撥款方式為 0 者跳出 */
			if (titaVo.get("RpCode" + i) == null || parse.stringToInteger(titaVo.getParam("RpCode" + i)) == 0)
				break;
			if (parse.stringToBigDecimal(titaVo.getParam("RpAmt" + i)).compareTo(BigDecimal.ZERO) > 0) {
				addPayment(i, titaVo);
			}
		}

		/* 將處理完的AcDetail List 放回txBuffer */
		this.txBuffer.addAllAcDetailList(this.lAcDetail);
		for (int i = 0; i < this.txBuffer.getAcDetailList().size(); i++) {
			this.info("AcRepayCom AcDetailList = " + i + this.txBuffer.getAcDetailList().get(i));
		}

		return null;
	}

	/**
	 * 放款回收帳務產生處理
	 * 
	 * @param ilLoanBorTx 放款交易內容檔清單
	 * @param iBaTxList   還款入帳試算表
	 * @param titaVo      TitaVo
	 * @throws LogicException ....
	 */
	public void settleRun(List<LoanBorTx> ilLoanBorTx, ArrayList<BaTxVo> iBaTxList, TitaVo titaVo)
			throws LogicException {
		this.info("settleRun ... ");
		//
		if (ilLoanBorTx.size() == 0) {
			return;
		}
		this.titaVo = titaVo;
		this.baTxList = iBaTxList;

		iRepayCode = (ilLoanBorTx.get(0).getRepayCode());
		iEntryDate = (ilLoanBorTx.get(0).getEntryDate());
		if ("L3420".equals(titaVo.getTxcd())) {
			iCaseCloseCode = this.parse.stringToInteger(titaVo.getParam("CaseCloseCode"));
		}

		loanCom.setTxBuffer(this.txBuffer);

		// 處理短繳
		updUnpaidAmt(ilLoanBorTx, titaVo);
		// 銷帳檔處理(短繳)
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

		// 借方：收付欄
		for (int i = 1; i <= 50; i++) {
			/* 還款來源／撥款方式為 0 者跳出 */
			if (titaVo.get("RpCode" + i) == null || parse.stringToInteger(titaVo.getParam("RpCode" + i)) == 0)
				break;
			if (parse.stringToBigDecimal(titaVo.getParam("RpAmt" + i)).compareTo(BigDecimal.ZERO) > 0) {
				addPayment(i, titaVo);
			}
		}

		// 借方：暫收借
		settleTempAmt(this.baTxList, this.lAcDetail, titaVo);

		BigDecimal txAmtRemaind = BigDecimal.ZERO; // 交易餘額
		BigDecimal tempAmtRemaind = BigDecimal.ZERO; // 暫收餘額

		for (AcDetail ac : this.lAcDetail) {
			int sumNo = parse.stringToInteger(ac.getSumNo());
			if ("D".equals(ac.getDbCr())) {
				if (sumNo >= 98) {
					txAmtRemaind = txAmtRemaind.add(ac.getTxAmt());
				} else {
					tempAmtRemaind = tempAmtRemaind.add(ac.getTxAmt());
				}
			}
		}
		this.info("txAmtRemaind=" + txAmtRemaind + ", tempAmtRemaind=" + tempAmtRemaind);

		// 貸方：費用
		for (BaTxVo ba : baTxList) {
			if (ba.getRepayType() >= 4 && ba.getAcctAmt().compareTo(BigDecimal.ZERO) > 0) {
				LoanBorTx tx = addFeeBorTxRoutine(ba, iRepayCode, iEntryDate, new TempVo(), titaVo);
				settleFeeAmt(ba, tx); // 費用出帳
				ba.setAcctAmt(BigDecimal.ZERO);
				if (tx.getTxAmt().compareTo(BigDecimal.ZERO) == 0) {
					BigDecimal txAmt = BigDecimal.ZERO;
					BigDecimal tempAmt = BigDecimal.ZERO;
					BigDecimal repayAmt = updBorTxAcDetail(tx, this.lAcDetail, titaVo); // 更新放款明細檔及帳務明細檔關聯欄
					if (tempAmtRemaind.compareTo(repayAmt) > 0) {
						tempAmt = repayAmt;
						tempAmtRemaind = tempAmtRemaind.subtract(tempAmt);
					} else {
						tempAmt = tempAmtRemaind;
						tempAmtRemaind = BigDecimal.ZERO;
						if (txAmtRemaind.compareTo(repayAmt.subtract(tempAmt)) > 0) {
							txAmt = repayAmt.subtract(tempAmt);
							txAmtRemaind = txAmtRemaind.subtract(txAmt);
						} else {
							txAmt = txAmtRemaind;
							txAmtRemaind = BigDecimal.ZERO;
						}
					}
					tx.setTxAmt(txAmt);
					tx.setTempAmt(tempAmt);
				}
				this.lLoanBorTx.add(tx);
			}
		}

		// 貸方：本金利息
		for (LoanBorTx tx : ilLoanBorTx) {
			settleLoanAmt(tx); // 本金利息出帳
			if (tx.getTxAmt().compareTo(BigDecimal.ZERO) == 0) {
				BigDecimal repayAmt = updBorTxAcDetail(tx, this.lAcDetail, titaVo); // 更新放款明細檔及帳務明細檔關聯欄
				BigDecimal txAmt = BigDecimal.ZERO;
				BigDecimal tempAmt = BigDecimal.ZERO;
				if (tempAmtRemaind.compareTo(repayAmt) > 0) {
					tempAmt = repayAmt;
					tempAmtRemaind = tempAmtRemaind.subtract(tempAmt);
				} else {
					tempAmt = tempAmtRemaind;
					tempAmtRemaind = BigDecimal.ZERO;
					if (txAmtRemaind.compareTo(repayAmt.subtract(tempAmt)) > 0) {
						txAmt = repayAmt.subtract(tempAmt);
						txAmtRemaind = txAmtRemaind.subtract(txAmt);
					} else {
						txAmt = txAmtRemaind;
						txAmtRemaind = BigDecimal.ZERO;
					}
				}
				tx.setTxAmt(txAmt);
				tx.setTempAmt(tempAmt);
			}
			this.lLoanBorTx.add(tx);
		}

		// 貸方：溢收款
		BigDecimal overflow = settleOverflow(this.lAcDetail, titaVo);

		// 更新尾筆交易金額、暫收借金額、暫收貸金額
		LoanBorTx tx = this.lLoanBorTx.get(this.lLoanBorTx.size() - 1);
		updBorTxAcDetail(tx, this.lAcDetail, titaVo); // 更新放款明細檔及帳務明細檔關聯欄
		tx.setTxAmt(tx.getTxAmt().add(txAmtRemaind));
		tx.setTempAmt(tx.getTempAmt().add(tempAmtRemaind));
		tx.setOverflow(overflow);

		try {
			loanBorTxService.updateAll(this.lLoanBorTx);
		} catch (DBException e) {
			throw new LogicException(titaVo, "E0005", "放款交易內容檔 " + e.getErrorMsg()); // 新增資料時，發生錯誤
		}

		// 產生會計分錄
		this.txBuffer.setAcDetailList(this.lAcDetail);
		acDetailCom.setTxBuffer(this.txBuffer);
		acDetailCom.run(titaVo);

	}

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
		acDetail = new AcDetail();

		acDetail.setDscptCode(titaVo.get("RpDscpt" + i)); // 摘要代號
		acDetail.setSlipNote(titaVo.get("RpNote" + i)); // 摘要
		// 收付金額
		acDetail.setTxAmt(parse.stringToBigDecimal(titaVo.getParam("RpAmt" + i)));

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
//	      099:暫收沖正     (戶號)       THC 暫收款－沖正
		if (titaVo.get("BATCHNO") != null && titaVo.get("BATCHNO").trim().length() == 6
				&& "RESV00".equals(titaVo.get("BATCHNO"))) {
			acDetail.setSumNo("099");
		} else if (parse.stringToInteger(titaVo.getParam("RpCode" + i)) >= 90) {
			acDetail.setSumNo("0" + FormatUtil.pad9(titaVo.getParam("RpCode" + i), 2));
		} else {
			acDetail.setSumNo("1" + FormatUtil.pad9(titaVo.getParam("RpCode" + i), 2));
			acDetail.setAcctCode(titaVo.getParam("RpAcctCode" + i));
			String rpAcCode = FormatUtil.padX(titaVo.getParam("RpAcCode" + i).trim(), 18);
			acDetail.setAcNoCode(rpAcCode.substring(0, 11)); // 會科科子細目 11+5+2
			acDetail.setAcSubCode(rpAcCode.substring(11, 16));
			acDetail.setAcDtlCode(rpAcCode.substring(16, 18));
		}

		// 銷帳編號
		acDetail.setRvNo(titaVo.get("RpRvno" + i)); /* 銷帳編號 VARCHAR2(30) */

		switch (acDetail.getSumNo()) {
		case "090":
			throw new LogicException(titaVo, "E0015", "請自行執行L3230 暫收款銷帳(06-轉帳), 額度" + titaVo.getParam("RpFacmNo" + i)
					+ ", 金額" + titaVo.getParam("RpAmt" + i)); // 檢查錯誤
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
		case "099":
			acDetail.setAcctCode("THC");
			break;
		case "101": // 101.匯款轉帳
			acDetail.setAcctCode("P03");
			break;
		case "102": // 102.銀行扣款 C01 暫收款－非核心資金運用 核心銷帳碼 0010060yyymmdd (銀扣 ACH), 郵局 P01
			if ("C01".equals(acDetail.getAcctCode())) {
				acDetail.setRvNo("0010060" + titaVo.getEntDyI());
			}
			break;
		case "103": // 103.員工扣款
			break;
		case "104": // 104.支票兌現
			break;
		// 其他還款來源有核心銷帳碼
		case "105": // 105.法院扣薪
		case "106": // 106.理賠金
		case "107": // 107.代收款-債權協商
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
	 * 暫收款金額 (暫收借) 帳務處理
	 * 
	 * @param iBaTxList ArrayList of BaTxVo
	 * @param iAcList   List of AcDetail
	 * @param titaVo    TitaVo
	 * @return 暫收款金額
	 * @throws LogicException ....
	 */
	public BigDecimal settleTempAmt(ArrayList<BaTxVo> iBaTxList, List<AcDetail> iAcList, TitaVo titaVo)
			throws LogicException {
		BigDecimal wkTempAmt = BigDecimal.ZERO;
		this.titaVo = titaVo;
		this.baTxList = iBaTxList;
		this.lAcDetail = iAcList;
		this.info("settleTempAmt ... " + this.lAcDetail.size());
		for (BaTxVo ba : this.baTxList) {
			if (ba.getDataKind() == 3 && ba.getAcctAmt().compareTo(BigDecimal.ZERO) > 0) {
				wkTempAmt = wkTempAmt.add(ba.getAcctAmt());
				AcDetail acDetail = new AcDetail();
				acDetail.setDbCr("D");
				acDetail.setAcctCode(ba.getAcctCode());
				acDetail.setSumNo("090");
				acDetail.setTxAmt(ba.getAcctAmt());
				acDetail.setCustNo(ba.getCustNo());
				acDetail.setFacmNo(ba.getFacmNo());
				acDetail.setBormNo(ba.getBormNo());
				acDetail.setRvNo(ba.getRvNo());
				acDetail.setReceivableFlag(ba.getReceivableFlag());
				this.lAcDetail.add(acDetail);
				this.info("settleTempAmt ba " + acDetail.toString());
				ba.setAcctAmt(BigDecimal.ZERO);
			}
		}
		this.info("settleTempAmt end " + this.lAcDetail.size());
		return wkTempAmt;
	}

	/**
	 * 累溢收(暫收貸)帳務處理
	 * 
	 * @param iAcList List of AcDetail
	 * @param titaVo  titaVo
	 * @return 累溢收金額
	 * @throws LogicException ....
	 */

	public BigDecimal settleOverflow(List<AcDetail> iAcList, TitaVo titaVo) throws LogicException {
		this.titaVo = titaVo;
		this.lAcDetail = iAcList;
		this.info("settleOverflow ..." + this.lAcDetail.size());

		int wkCustNo = 0;
		int wkFacmNo = 0;
		BigDecimal wkOverflow = BigDecimal.ZERO;

		// 暫收款餘額
		for (AcDetail ac : this.lAcDetail) {
			wkCustNo = ac.getCustNo();
			// Overflow 累溢收金額 暫收貸(正值)
			if ("D".equals(ac.getDbCr())) {
				wkOverflow = wkOverflow.add(ac.getTxAmt());
			} else {
				wkOverflow = wkOverflow.subtract((ac.getTxAmt()));
			}
			if (ac.getFacmNo() > 0) {
				wkFacmNo = ac.getFacmNo();
			}
		}

		if (wkOverflow.compareTo(BigDecimal.ZERO) > 0) {
			AcDetail acDetail = new AcDetail();
			acDetail.setDbCr("C");
			acDetail.setAcctCode("TAV");
			acDetail.setTxAmt(wkOverflow);
			acDetail.setCustNo(wkCustNo);
			acDetail.setFacmNo(wkFacmNo);
			acDetail.setBormNo(0);
			acDetail.setSumNo("090"); // 暫收可抵繳
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

		int acSeq = 0;
		for (AcDetail ac : this.lAcDetail) {
			acSeq++;
			if (ac.getAcSeq() > 0) {
				continue;
			}
			ac.setSlipSumNo(tx.getSlipSumNo()); // 彙總傳票批號
			ac.setAcSeq(acSeq); // 分錄序號
			if (tx.getAcSeq() == 0) {
				tx.setAcSeq(acSeq);
			}
			// 作帳金額
			if (parse.stringToInteger(ac.getSumNo()) == 0) {
				if ("C".equals(ac.getDbCr())) {
					repayAmt = repayAmt.add(ac.getTxAmt());
				} else {
					repayAmt = repayAmt.subtract(ac.getTxAmt());
				}
			}
			TempVo acTempVo = new TempVo();
			acTempVo = acTempVo.getVo(ac.getJsonFields());
			acTempVo.putParam("BorxNo", tx.getBorxNo());
			if (ac.getFacmNo() != tx.getFacmNo()) {
				acTempVo.putParam("FacmNo", tx.getFacmNo());
			}
			if (ac.getBormNo() != tx.getBormNo()) {
				acTempVo.putParam("BormNo", tx.getBormNo());
			}
			ac.setJsonFields(acTempVo.getJsonString());
		}
		this.info("updBorTxAcDetail end repayAmt=" + repayAmt + ", AcSeq=" + tx.getAcSeq() + ", SlipSumNo="
				+ tx.getSlipSumNo());
		return repayAmt;
	}

	// 放款收回
	private void settleLoanAmt(LoanBorTx tx) throws LogicException {
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
				tx.setTxAmt(acDetail.getTxAmt()); // 轉催呆金額放LoanBorTx交易金額
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
				tx.setTxAmt(acDetail.getTxAmt()); // 轉催呆金額放LoanBorTx交易金額
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
	}

	/**
	 * 暫收款收回費用處理(新增帳務及放款交易內容檔)
	 * 
	 * @param iRpCode       還款來源
	 * @param iEntryDate    入帳日
	 * @param iTempAmt      暫收款金額
	 * @param iFeeList      費用明細
	 * @param iAcDetailList 帳務明細
	 * @param titaVo        TitaVo
	 * @throws LogicException ....
	 */
	public void settleFeeByTemp(int iRpCode, int iEntryDate, BigDecimal iTempAmt, ArrayList<BaTxVo> iFeeList,
			List<AcDetail> iAcDetailList, TitaVo titaVo) throws LogicException {
		this.info("settleFeeByTemp ... ");
		this.titaVo = titaVo;
		this.baTxList = iFeeList;
		this.lAcDetail = iAcDetailList;

		this.iRepayCode = iRpCode;
		this.iEntryDate = iEntryDate;
		this.iCaseCloseCode = 0;
		BigDecimal tempAmtRemaind = iTempAmt;
		// 貸方：費用
		for (BaTxVo ba : baTxList) {
			if (ba.getRepayType() >= 4 && ba.getAcctAmt().compareTo(BigDecimal.ZERO) > 0) {
				LoanBorTx tx = addFeeBorTxRoutine(ba, iRepayCode, iEntryDate, new TempVo(), titaVo);
				settleFeeAmt(ba, tx);
				ba.setAcctAmt(BigDecimal.ZERO);
				BigDecimal repayAmt = updBorTxAcDetail(tx, this.lAcDetail, titaVo); // 更新放款明細檔及帳務明細檔關聯欄
				BigDecimal tempAmt = BigDecimal.ZERO;
				if (tempAmtRemaind.compareTo(repayAmt) > 0) {
					tempAmt = repayAmt;
					tempAmtRemaind = tempAmtRemaind.subtract(tempAmt);
				} else {
					tempAmt = tempAmtRemaind;
					tempAmtRemaind = BigDecimal.ZERO;
				}
				tx.setTempAmt(tempAmt);
				this.lLoanBorTx.add(tx);
			}
		}
		// 貸方：溢收款
		BigDecimal overflow = settleOverflow(this.lAcDetail, titaVo);

		// 更新尾筆暫收借金額、暫收貸金額
		LoanBorTx tx = this.lLoanBorTx.get(this.lLoanBorTx.size() - 1);
		updBorTxAcDetail(tx, this.lAcDetail, titaVo); // 更新放款明細檔及帳務明細檔關聯欄
		tx.setTempAmt(tx.getTempAmt().add(tempAmtRemaind));
		tx.setOverflow(overflow);

		try {
			loanBorTxService.updateAll(this.lLoanBorTx);
		} catch (DBException e) {
			throw new LogicException(titaVo, "E0005", "放款交易內容檔 " + e.getErrorMsg()); // 新增資料時，發生錯誤
		}
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
	private LoanBorTx addFeeBorTxRoutine(BaTxVo ba, int iRpCode, int iEntryDate, TempVo iTempVo, TitaVo titaVo)
			throws LogicException {
		this.info("addFeeBorTxRoutine ... ");

		// 新增放款交易內容檔(收回費用)
		String desc = "";
		if ("L3210".equals(titaVo.getTxcd())) {
			desc = "暫收銷";
		}
		if ("L3230".equals(titaVo.getTxcd())) {
			desc = "暫收退";
		}
		// 費用科目代碼
		desc += getCdCodeX("AcctCode", ba.getAcctCode(), titaVo);
		if (iCaseCloseCode == 7 || iCaseCloseCode == 8) {
			desc += "轉呆帳";
		}
		LoanBorTx tLoanBorTx = new LoanBorTx();
		LoanBorTxId tLoanBorTxId = new LoanBorTxId();
		loanCom.setFacmBorTx(tLoanBorTx, tLoanBorTxId, ba.getCustNo(), ba.getFacmNo(), titaVo);
		tLoanBorTx.setCustNo(ba.getCustNo());
		tLoanBorTx.setFacmNo(ba.getFacmNo());
		tLoanBorTx.setDesc(desc);
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
		iTempVo.putParam("RvNo", ba.getRvNo()); // 銷帳編號
		tLoanBorTx.setOtherFields(iTempVo.getJsonString());
		try {
			loanBorTxService.insert(tLoanBorTx, titaVo);
		} catch (DBException e) {
			throw new LogicException(titaVo, "E0005", "放款交易內容檔 " + e.getErrorMsg()); // 新增資料時，發生錯誤
		}
		return tLoanBorTx;
	}

	// 取得代碼說明
	private String getCdCodeX(String defCode, String cdCode, TitaVo titaVo) throws LogicException {
		CdCode tCdCode = cdCodeService.findById(new CdCodeId(defCode, cdCode), titaVo);
		return tCdCode == null ? "" : tCdCode.getItem();
	}

}
