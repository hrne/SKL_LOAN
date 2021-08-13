package com.st1.itx.trade.L3;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;

import com.st1.itx.Exception.LogicException;
import com.st1.itx.Exception.DBException;
import com.st1.itx.dataVO.TempVo;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.dataVO.TotaVo;
import com.st1.itx.db.domain.AcDetail;
import com.st1.itx.db.domain.LoanBorTx;
import com.st1.itx.db.domain.LoanBorTxId;
import com.st1.itx.db.domain.LoanSynd;
import com.st1.itx.db.domain.LoanSyndId;
import com.st1.itx.db.domain.TxTemp;
import com.st1.itx.db.domain.TxTempId;
import com.st1.itx.db.service.LoanBorTxService;
import com.st1.itx.db.service.LoanSyndService;
import com.st1.itx.db.service.TxTempService;
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.common.AcDetailCom;
import com.st1.itx.util.common.AcPaymentCom;
import com.st1.itx.util.common.LoanCom;
import com.st1.itx.util.data.DataLog;
import com.st1.itx.util.parse.Parse;

/*
 * Tita
 * CustNo=9,7
 * SyndNo=9,3
 * FacmNo=9,3
 * CurrencyCode=X,3
 * EntryDate=9,7
 * TimLeadingFee=9,14.2
 * TimManageFee=9,14.2
 * TimCommitFee=9,14.2
 * TimPartFee=9,14.2
 * TimAgencyFee=9,14.2
 * TimUnderwriteFee=9,14.2
 * TimOtherFee=9,14.2
 * TimSumFee=9,14.2
 * TimIncomeFee=9,14.2
 * TimPrepaidFee=9,14.2
 */
/**
 * L3610 補收聯貸費用
 * 
 * @author iza
 * @version 1.0.0
 */
@Service("L3610")
@Scope("prototype")
public class L3610 extends TradeBuffer {
	private static final Logger logger = LoggerFactory.getLogger(L3610.class);

	/* DB服務注入 */
	@Autowired
	public LoanSyndService loanSyndService;
	@Autowired
	public TxTempService txTempService;
	@Autowired
	public LoanBorTxService loanBorTxService;

	@Autowired
	Parse parse;
	@Autowired
	DataLog datalog;
	@Autowired
	AcDetailCom acDetailCom;
	@Autowired
	LoanCom loanCom;
	@Autowired
	AcPaymentCom acPaymentCom;

	private TitaVo titaVo;
	private int iCustNo;
	private int iSyndNo;
	private int iFacmNo;
	private int iEntryDate;
	private BigDecimal iLeadingFee;
	private BigDecimal iManageFee;
	private BigDecimal iCommitFee;
	private BigDecimal iPartFee;
	private BigDecimal iAgencyFee;
	private BigDecimal iUnderwriteFee;
	private BigDecimal iOtherFee;
	private BigDecimal iSumFee;
	private BigDecimal iIncomeFee;
	private BigDecimal iPrepaidFee;
	private BigDecimal iTotalRepayAmt;
	private BigDecimal iRealRepayAmt;

	// work area
	private int wkCustNo;
	private int wkFacmNo;
	private int wkSyndNo;
	private int wkOvduNo;
	private int wkBorxNo;
	private int wkNewBorxNo;
	private BigDecimal wkTempAmt = BigDecimal.ZERO;
	private AcDetail acDetail;
	private TxTemp tTxTemp;
	private TxTempId tTxTempId;
	private TempVo tTempVo = new TempVo();
	private LoanSynd tLoanSynd = new LoanSynd();
	private LoanBorTx tLoanBorTx;
	private LoanBorTxId tLoanBorTxId;
	private List<TxTemp> lTxTemp;
	private List<AcDetail> lAcDetail = new ArrayList<AcDetail>();

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active L3610 ");
		this.totaVo.init(titaVo);
		this.titaVo = titaVo;
		loanCom.setTxBuffer(this.txBuffer);

		// 取得輸入資料
		iCustNo = this.parse.stringToInteger(titaVo.getParam("CustNo"));
		iSyndNo = this.parse.stringToInteger(titaVo.getParam("SyndNo"));
		iFacmNo = this.parse.stringToInteger(titaVo.getParam("FacmNo"));
		iEntryDate = this.parse.stringToInteger(titaVo.getParam("EntryDate"));
		iLeadingFee = this.parse.stringToBigDecimal(titaVo.getParam("TimLeadingFee"));
		iManageFee = this.parse.stringToBigDecimal(titaVo.getParam("TimManageFee"));
		iCommitFee = this.parse.stringToBigDecimal(titaVo.getParam("TimCommitFee"));
		iPartFee = this.parse.stringToBigDecimal(titaVo.getParam("TimPartFee"));
		iAgencyFee = this.parse.stringToBigDecimal(titaVo.getParam("TimAgencyFee"));
		iUnderwriteFee = this.parse.stringToBigDecimal(titaVo.getParam("TimUnderwriteFee"));
		iOtherFee = this.parse.stringToBigDecimal(titaVo.getParam("TimOtherFee"));
		iSumFee = this.parse.stringToBigDecimal(titaVo.getParam("TimSumFee"));
		iIncomeFee = this.parse.stringToBigDecimal(titaVo.getParam("TimIncomeFee"));
		iPrepaidFee = this.parse.stringToBigDecimal(titaVo.getParam("TimPrepaidFee"));
		iTotalRepayAmt = this.parse.stringToBigDecimal(titaVo.getParam("TotalRepayAmt"));
		iRealRepayAmt = this.parse.stringToBigDecimal(titaVo.getParam("RealRepayAmt"));
		wkTempAmt = iRealRepayAmt.subtract(iTotalRepayAmt);

		if (titaVo.isHcodeNormal()) {
			SyndFeeNormalRoutine();
		} else {
			SyndFeeEraseRoutine();
		}

		// 借方收付欄帳務處理
		acDetailDbRoutine();

		this.addList(this.totaVo);
		return this.sendList();
	}

	private void SyndFeeNormalRoutine() throws LogicException {
		this.info("SyndFeeNormalRoutine ...");

		// 鎖定聯貸訂約檔
		tLoanSynd = loanSyndService.holdById(new LoanSyndId(iCustNo, iSyndNo));
		if (tLoanSynd == null) {
			throw new LogicException(titaVo, "E0006", "聯貸訂約檔 戶號 = " + iCustNo + " 聯貸案序號 = " + iSyndNo); // 鎖定資料時，發生錯誤
		}
		// 新增交易暫存檔
		addTxTempSyndFeeRoutine();
		// 更新聯貸訂約檔
		updLoanSyndRoutine();
		// 新增放款交易內容檔
		addLoanBorTxRoutine();
		// 補收費用貸方帳務處理
		acDetailCrRoutine();
	}

	// 訂正
	private void SyndFeeEraseRoutine() throws LogicException {
		this.info("SyndFeeEraseRoutine ...");

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
			wkSyndNo = this.parse.stringToInteger(tx.getSeqNo().substring(10, 13));
			wkOvduNo = this.parse.stringToInteger(tx.getSeqNo().substring(13, 16));
			tTempVo = tTempVo.getVo(tx.getText());
			wkBorxNo = this.parse.stringToInteger(tTempVo.get("BorxNo"));
			this.info("   SeqNo    = " + tx.getSeqNo());
			this.info("   wkCustNo = " + wkCustNo);
			this.info("   wkFacmNo = " + wkFacmNo);
			this.info("   wkSyndNo = " + wkSyndNo);
			this.info("   wkOvduNo = " + wkOvduNo);
			this.info("   wkBorxNo = " + wkBorxNo);
			// 還原聯貸訂約檔
			RestoredLoanSyndRoutine();
			// 註記交易內容檔
			loanCom.setLoanBorTxHcode(wkCustNo, wkFacmNo, wkSyndNo, wkBorxNo, wkNewBorxNo, BigDecimal.ZERO, titaVo);
			// 補收費用貸方帳務處理
			acDetailCrRoutine();
		}
	}

	// 新增交易暫存檔
	private void addTxTempSyndFeeRoutine() throws LogicException {
		this.info("addTxTempBormRoutine ... ");

		wkBorxNo = tLoanSynd.getLastBorxNo() + 1;
		tTxTemp = new TxTemp();
		tTxTempId = new TxTempId();
		loanCom.setTxTemp(tTxTempId, tTxTemp, iCustNo, 999, iSyndNo, 0, titaVo);
		tTempVo.clear();
		tTempVo.putParam("BorxNo", wkBorxNo);
		tTempVo.putParam("LeadingFee", iLeadingFee);
		tTempVo.putParam("ManageFee", iManageFee);
		tTempVo.putParam("CommitFee", iCommitFee);
		tTempVo.putParam("PartFee", iPartFee);
		tTempVo.putParam("AgencyFee", iAgencyFee);
		tTempVo.putParam("UnderwriteFee", iUnderwriteFee);
		tTempVo.putParam("OtherFee", iOtherFee);
		tTempVo.putParam("SumFee", iSumFee);
		tTempVo.putParam("IncomeFee", iIncomeFee);
		tTempVo.putParam("PrepaidFee", iPrepaidFee);
		tTempVo.putParam("TotalRepayAmt", iTotalRepayAmt);
		tTempVo.putParam("RealRepayAmt", iRealRepayAmt);
		tTxTemp.setText(tTempVo.getJsonString());
		try {
			txTempService.insert(tTxTemp);
		} catch (DBException e) {
			throw new LogicException(titaVo, "E0005", "交易暫存檔 Key = " + tTxTempId); // 新增資料時，發生錯誤 }
		}
	}

	// 更新聯貸訂約檔
	private void updLoanSyndRoutine() throws LogicException {
		this.info("updLoanSyndRoutine ... ");

		tLoanSynd.setLastBorxNo(wkBorxNo);
		try {
			loanSyndService.update(tLoanSynd);
		} catch (DBException e) {
			throw new LogicException(titaVo, "E0007", "聯貸訂約檔 戶號 = " + iCustNo + " 聯貸案序號 = " + iSyndNo); // 更新資料時，發生錯誤
		}

	}

	// 補收費用貸方帳務處理
	private void acDetailCrRoutine() throws LogicException {
		this.info("acDetailCrRoutine ... ");
		this.info("   isBookAcYes = " + this.txBuffer.getTxCom().isBookAcYes());

		if (!this.txBuffer.getTxCom().isBookAcYes()) {
			return;
		}
		iLeadingFee = this.parse.stringToBigDecimal(titaVo.getParam("TimLeadingFee"));
		iManageFee = this.parse.stringToBigDecimal(titaVo.getParam("TimManageFee"));
		iCommitFee = this.parse.stringToBigDecimal(titaVo.getParam("TimCommitFee"));
		iPartFee = this.parse.stringToBigDecimal(titaVo.getParam("TimPartFee"));
		iAgencyFee = this.parse.stringToBigDecimal(titaVo.getParam("TimAgencyFee"));
		iUnderwriteFee = this.parse.stringToBigDecimal(titaVo.getParam("TimUnderwriteFee"));
		iOtherFee = this.parse.stringToBigDecimal(titaVo.getParam("TimOtherFee"));
		iSumFee = this.parse.stringToBigDecimal(titaVo.getParam("TimSumFee"));
		iIncomeFee = this.parse.stringToBigDecimal(titaVo.getParam("TimIncomeFee"));
		iPrepaidFee = this.parse.stringToBigDecimal(titaVo.getParam("TimPrepaidFee"));
		iTotalRepayAmt = this.parse.stringToBigDecimal(titaVo.getParam("TotalRepayAmt"));
		iRealRepayAmt = this.parse.stringToBigDecimal(titaVo.getParam("RealRepayAmt"));
		// 聯貸管理費收入
		if (iManageFee.compareTo(BigDecimal.ZERO) > 0) {
			acDetail = new AcDetail();
			acDetail.setDbCr("C");
			acDetail.setAcctCode("F27"); 
			acDetail.setTxAmt(iManageFee);
			acDetail.setCustNo(iCustNo);
			acDetail.setFacmNo(iFacmNo);
			acDetail.setBormNo(0);
			acDetail.setSlipNote("聯貸管理費");
			lAcDetail.add(acDetail);
		}
		// 帳管費收入
		if (iLeadingFee.compareTo(BigDecimal.ZERO) > 0) {
			acDetail = new AcDetail();
			acDetail.setDbCr("C");
			acDetail.setAcctCode("F10");  
			acDetail.setTxAmt(iLeadingFee);
			acDetail.setCustNo(iCustNo);
			acDetail.setFacmNo(iFacmNo);
			acDetail.setBormNo(0);
			acDetail.setSlipNote("聯貸主辦費");
			lAcDetail.add(acDetail);
		}
		// 帳管費收入
		if (iCommitFee.compareTo(BigDecimal.ZERO) > 0) {
			acDetail = new AcDetail();
			acDetail.setDbCr("C");
			acDetail.setAcctCode("F10");  
			acDetail.setTxAmt(iCommitFee);
			acDetail.setCustNo(iCustNo);
			acDetail.setFacmNo(iFacmNo);
			acDetail.setBormNo(0);
			acDetail.setSlipNote("聯貸承諾費");
			lAcDetail.add(acDetail);
		}
		// 帳管費收入
		if (iPartFee.compareTo(BigDecimal.ZERO) > 0) {
			acDetail = new AcDetail();
			acDetail.setDbCr("C");
			acDetail.setAcctCode("F10");  
			acDetail.setTxAmt(iPartFee);
			acDetail.setCustNo(iCustNo);
			acDetail.setFacmNo(iFacmNo);
			acDetail.setBormNo(0);
			acDetail.setSlipNote("聯貸參貸費");
			lAcDetail.add(acDetail);
		}
		// 帳管費收入
		if (iAgencyFee.compareTo(BigDecimal.ZERO) > 0) {
			acDetail = new AcDetail();
			acDetail.setDbCr("C");
			acDetail.setAcctCode("F10");  
			acDetail.setTxAmt(iAgencyFee);
			acDetail.setCustNo(iCustNo);
			acDetail.setFacmNo(iFacmNo);
			acDetail.setBormNo(0);
			acDetail.setSlipNote("聯貸代理費");
			lAcDetail.add(acDetail);
		}
		// 帳管費收入
		if (iUnderwriteFee.compareTo(BigDecimal.ZERO) > 0) {
			acDetail = new AcDetail();
			acDetail.setDbCr("C");
			acDetail.setAcctCode("F10");  
			acDetail.setTxAmt(iUnderwriteFee);
			acDetail.setCustNo(iCustNo);
			acDetail.setFacmNo(iFacmNo);
			acDetail.setBormNo(0);
			acDetail.setSlipNote("聯貸包銷費");
			lAcDetail.add(acDetail);
		}
		// 帳管費收入
		if (iOtherFee.compareTo(BigDecimal.ZERO) > 0) {
			acDetail = new AcDetail();
			acDetail.setDbCr("C");
			acDetail.setAcctCode("F10");  
			acDetail.setTxAmt(iOtherFee);
			acDetail.setCustNo(iCustNo);
			acDetail.setFacmNo(iFacmNo);
			acDetail.setBormNo(0);
			acDetail.setSlipNote("聯貸其他費用");
			lAcDetail.add(acDetail);
		}

	}

	// 新增放款交易內容檔
	private void addLoanBorTxRoutine() throws LogicException {
		this.info("addLoanBorTxRoutine ... ");

		tLoanBorTx = new LoanBorTx();
		tLoanBorTxId = new LoanBorTxId();
		loanCom.setLoanBorTx(tLoanBorTx, tLoanBorTxId, iCustNo, 999, iSyndNo, wkBorxNo, titaVo);
		tLoanBorTx.setDesc("補收聯貸費用");
		tLoanBorTx.setEntryDate(iEntryDate);
		tLoanBorTx.setTxAmt(this.parse.stringToBigDecimal(titaVo.getTxAmt()));
		tLoanBorTx.setTempAmt(wkTempAmt);
		tLoanBorTx.setDisplayflag("A"); // 帳務
		// 其他欄位
		tTempVo.clear();
		tTempVo.putParam("LeadingFee", iLeadingFee);
		tTempVo.putParam("ManageFee", iManageFee);
		tTempVo.putParam("CommitFee", iCommitFee);
		tTempVo.putParam("PartFee", iPartFee);
		tTempVo.putParam("AgencyFee", iAgencyFee);
		tTempVo.putParam("UnderwriteFee", iUnderwriteFee);
		tTempVo.putParam("OtherFee", iOtherFee);
		tTempVo.putParam("SumFee", iSumFee);
		tTempVo.putParam("IncomeFee", iIncomeFee);
		tTempVo.putParam("PrepaidFee", iPrepaidFee);
		tTempVo.putParam("TotalRepayAmt", iTotalRepayAmt);
		tTempVo.putParam("RealRepayAmt", iRealRepayAmt);
		tLoanBorTx.setOtherFields(tTempVo.getJsonString());
		try {
			loanBorTxService.insert(tLoanBorTx);
		} catch (DBException e) {
			throw new LogicException(titaVo, "E0005", "放款交易內容檔 " + e.getErrorMsg()); // 新增資料時，發生錯誤
		}
	}

	// 借方收付欄帳務處理
	private void acDetailDbRoutine() throws LogicException {
		this.info("acDetailDbRoutine ... ");
		this.info("   isBookAcYes = " + this.txBuffer.getTxCom().isBookAcYes());

		if (!this.txBuffer.getTxCom().isBookAcYes()) {
			return;
		}
		// 借方 收付欄
		acPaymentCom.setTxBuffer(this.getTxBuffer());
		acPaymentCom.run(titaVo);
		this.txBuffer.addAllAcDetailList(lAcDetail);
		// 產生會計分錄
		acDetailCom.setTxBuffer(this.txBuffer);
		acDetailCom.run(titaVo);
	}

	// 註記放款交易內容檔
	/*
	 * private void RemarkLoanBorTxRoutine() throws LogicException {
	 * this.info("RemarkLoanBorTxRoutine ... "); this.info("   wkCustNo    = " +
	 * wkCustNo); this.info("   wkFacmNo    = " + wkFacmNo);
	 * this.info("   wkSyndNo    = " + wkSyndNo); this.info("   wkBorxNo    = "
	 * + wkBorxNo); this.info("   wkNewBorxNo = " + wkNewBorxNo);
	 * 
	 * int wkAcDate = 0;
	 * 
	 * tLoanBorTx = loanBorTxService.holdById(new LoanBorTxId(wkCustNo, wkFacmNo,
	 * wkSyndNo, wkBorxNo)); if (tLoanBorTx == null) { throw new
	 * LogicException(titaVo, "E0006", "放款交易內容檔 戶號 = " + wkCustNo + " 額度編號 = " +
	 * wkFacmNo + " 撥款序號 = " + wkSyndNo + " 交易內容檔序號 = " + wkBorxNo); // 鎖定資料時，發生錯誤 }
	 * wkAcDate = tLoanBorTx.getAcDate(); tLoanBorTx.setTitaHCode(wkAcDate ==
	 * wkTbsDy ? "2" : "4"); // 0: 未訂正 1: 訂正 2: 被訂正 3: 沖正 4: 被沖正 try {
	 * loanBorTxService.update(tLoanBorTx); } catch (DBException e) { throw new
	 * LogicException(titaVo, "E0005", "放款交易內容檔 " + e.getErrorMsg()); // 新增資料時，發生錯誤
	 * }
	 * 
	 * // 新增放款交易內容檔 LoanBorTx tLoanBorTx2 = (LoanBorTx) datalog.clone(tLoanBorTx);
	 * LoanBorTxId tLoanBorTx2Id = new LoanBorTxId();
	 * loanCom.setLoanBorTx(tLoanBorTx2, tLoanBorTx2Id, wkCustNo, wkFacmNo,
	 * wkSyndNo, wkNewBorxNo, wkTbsDy, titaVo); tLoanBorTx2.setTitaHCode(wkAcDate ==
	 * wkTbsDy ? "1" : "3"); // 0: 未訂正 1: 訂正 2: 被訂正 3: 沖正 4: 被沖正
	 * tLoanBorTx2.setDisplayflag(tLoanBorTx.getDisplayflag()); try {
	 * loanBorTxService.insert(tLoanBorTx2); } catch (DBException e) { throw new
	 * LogicException(titaVo, "E0005", "放款交易內容檔 " + e.getErrorMsg()); // 新增資料時，發生錯誤
	 * } }
	 */

	// 還原聯貸訂約檔
	private void RestoredLoanSyndRoutine() throws LogicException {
		this.info("RestoredLoanSyndRoutine ... ");

		tLoanSynd = loanSyndService.holdById(new LoanSyndId(wkCustNo, wkSyndNo));
		if (tLoanSynd == null) {
			throw new LogicException(titaVo, "E0006",
					"聯貸訂約檔 戶號 = " + wkCustNo + " 額度編號 = " + wkFacmNo + " 聯貸案序號 = = " + wkSyndNo); // 鎖定資料時，發生錯誤
		}
		wkNewBorxNo = tLoanSynd.getLastBorxNo() + 1;
		tLoanSynd.setLastBorxNo(wkNewBorxNo);
		try {
			loanSyndService.update(tLoanSynd);
		} catch (DBException e) {
			throw new LogicException(titaVo, "E0007",
					"聯貸訂約檔 戶號 = " + wkCustNo + " 額度編號 = " + wkFacmNo + " 聯貸案序號 = = " + wkSyndNo); // 更新資料時，發生錯誤
		}
	}
}