package com.st1.itx.trade.L3;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;

import com.st1.itx.Exception.DBException;
import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.TempVo;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.dataVO.TotaVo;
import com.st1.itx.db.domain.AcClose;
import com.st1.itx.db.domain.AcCloseId;
import com.st1.itx.db.domain.AcDetail;
import com.st1.itx.db.domain.AcReceivable;
import com.st1.itx.db.domain.LoanBorTx;
import com.st1.itx.db.domain.LoanBorTxId;
import com.st1.itx.db.domain.LoanCheque;
import com.st1.itx.db.domain.LoanChequeId;
import com.st1.itx.db.service.AcCloseService;
import com.st1.itx.db.service.AcReceivableService;
import com.st1.itx.db.service.LoanBorTxService;
import com.st1.itx.db.service.LoanChequeService;
import com.st1.itx.db.service.TxTempService;
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.common.AcDetailCom;
import com.st1.itx.util.common.AcNegCom;
import com.st1.itx.util.common.AcPaymentCom;
import com.st1.itx.util.common.AcRepayCom;
import com.st1.itx.util.common.BaTxCom;
import com.st1.itx.util.common.LoanCom;
import com.st1.itx.util.common.TxAmlCom;
import com.st1.itx.util.common.data.BaTxVo;
import com.st1.itx.util.date.DateUtil;
import com.st1.itx.util.parse.Parse;

/*
 * L3220 暫收款退還
 * a.此功能供退還客戶暫收款或期票時登錄用,如退還支票,該支票需未兌現,如退還暫收款,目前客戶之暫收款需>=退還金額.
 * b.暫收款金額及支票金額,應擇一輸入
 * c.支票繳款時需入到該戶的額度編號下
 * c1.若多筆額度期款收回失敗時，帳掛在第一筆的額度編號。
 * c2.若多筆額度期款收回成功且有溢收金額時，帳掛在第一筆的額度編號。
 */

/**
 * L3220 暫收款退還
 * 
 * @author iza
 * @version 1.0.0
 */
@Service("L3220")
@Scope("prototype")
public class L3220 extends TradeBuffer {
	private static final Logger logger = LoggerFactory.getLogger(L3220.class);

	/* DB服務注入 */
	@Autowired
	public AcReceivableService acReceivableService;
	@Autowired
	public LoanChequeService loanChequeService;
	@Autowired
	public TxTempService txTempService;
	@Autowired
	public LoanBorTxService loanBorTxService;
	@Autowired
	public AcCloseService acCloseService;

	@Autowired
	AcDetailCom acDetailCom;
	@Autowired
	AcNegCom acNegCom;
	@Autowired
	DateUtil dDateUtil;
	@Autowired
	Parse parse;
	@Autowired
	AcPaymentCom acPaymentCom;
	@Autowired
	AcRepayCom acRepayCom;
	@Autowired
	TxAmlCom txAmlCom;
	@Autowired
	LoanCom loanCom;
	@Autowired
	BaTxCom baTxCom;

	private long sno = 0;
	private TitaVo titaVo = new TitaVo();
	private int iCustNo;
	private int iFacmNo;
	private int wkFacmNoS;
	private int wkFacmNoE;
	private int iChequeAcct;
	private int iChequeNo;
	private int iTempItemCode;
	private int iTempReasonCode;
	private String iCurrencyCode;
	private BigDecimal iTempAmt;
	private BigDecimal wkTempBal;
	private BigDecimal wkCustTempBal;
	private BigDecimal wkChequeAmt;
	private String wkAcctCode;
	private List<AcDetail> lAcDetail;
	private ArrayList<BaTxVo> baTxList = new ArrayList<BaTxVo>();
	private LoanBorTx tLoanBorTx;
	private LoanBorTxId tLoanBorTxId;
	private TempVo tTempVo = new TempVo();

	// initialize variable
	@PostConstruct
	public void init() {
		this.iCustNo = 0;
		this.iFacmNo = 0;
		this.iChequeAcct = 0;
		this.iChequeNo = 0;
		this.iTempItemCode = 0;
		this.iTempReasonCode = 0;
		this.iCurrencyCode = "";
		this.wkChequeAmt = new BigDecimal(0);
		this.wkCustTempBal = new BigDecimal(0);
		this.wkAcctCode = "";
		this.iTempAmt = new BigDecimal(0);
		this.lAcDetail = new ArrayList<AcDetail>();
	}

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		logger.info("active L3220 ");
		logger.info("   isActfgEntry   = " + titaVo.isActfgEntry());
		logger.info("   isActfgRelease = " + titaVo.isActfgRelease());
		logger.info("   isHcodeNormal  = " + titaVo.isHcodeNormal());
		logger.info("   isHcodeErase   = " + titaVo.isHcodeErase());
		logger.info("   isHcodeModify  = " + titaVo.isHcodeModify());
		logger.info("   EntdyI         = " + titaVo.getEntDyI());
		logger.info("   Kinbr          = " + titaVo.getKinbr());
		logger.info("   TlrNo          = " + titaVo.getTlrNo());
		logger.info("   Tno            = " + titaVo.getTxtNo());
		logger.info("   OrgEntdyI      = " + titaVo.getOrgEntdyI());
		logger.info("   OrgKin         = " + titaVo.getOrgKin());
		logger.info("   OrgTlr         = " + titaVo.getOrgTlr());
		logger.info("   OrgTno         = " + titaVo.getOrgTno());

		this.totaVo.init(titaVo);
		acNegCom.setTxBuffer(this.txBuffer);
		loanCom.setTxBuffer(this.txBuffer);
		acPaymentCom.setTxBuffer(this.txBuffer);
		acRepayCom.setTxBuffer(this.txBuffer);
		baTxCom.setTxBuffer(this.txBuffer);
		// 取得輸入資料
		this.titaVo = titaVo;
		iCustNo = this.parse.stringToInteger(titaVo.getParam("TimCustNo"));
		iFacmNo = this.parse.stringToInteger(titaVo.getParam("TimFacmNo"));
		iTempAmt = this.parse.stringToBigDecimal(titaVo.getParam("TimTempAmt"));
		iChequeAcct = this.parse.stringToInteger(titaVo.getParam("ChequeAcct"));
		iChequeNo = this.parse.stringToInteger(titaVo.getParam("ChequeNo"));
		iTempItemCode = this.parse.stringToInteger(titaVo.getParam("TempItemCode"));
		iTempReasonCode = this.parse.stringToInteger(titaVo.getParam("TempReasonCode"));
		iCurrencyCode = titaVo.getParam("CurrencyCode");
		wkTempBal = iTempAmt;
		if (iFacmNo > 0) {
			wkFacmNoS = iFacmNo;
			wkFacmNoE = iFacmNo;
		} else {
			wkFacmNoS = 000;
			wkFacmNoE = 999;
		}

		// 檢查輸入資料
		if (iTempReasonCode == 0 || iTempReasonCode > 4) {
			throw new LogicException(titaVo, "E0019", "暫收帳戶"); // 輸入資料錯誤
		}

		// 帳務處理
		if (iTempAmt.compareTo(new BigDecimal(0)) > 0) { // 暫收款
			if (titaVo.isHcodeNormal()) {
				titaVo.setBatchNo("RT"); // 整批批號 for 退款
				TempAcDetailRoutine();
			}
		} else {
//			抽退票
			titaVo.setBatchNo("BCK"); // 整批批號 for L4103 列印傳票明細表
			if (titaVo.isHcodeNormal()) {
				ChequeAmtNormalRoutine(); // 抽退票
			} else {
				ChequeAmtEraseRoutine();
			}
			// 帳務處理
			ChequeAcDetailRoutine();
		}

		// AML交易檢核
		if (iTempAmt.compareTo(new BigDecimal(0)) > 0) { // 暫收款
			if (titaVo.isHcodeNormal()) {
				txAmlCom.setTxBuffer(this.txBuffer);
				txAmlCom.remitOut(titaVo);
			}
			// 維護撥款匯款檔
			AcPaymentRoutine();
		}
		// 貸方
		if (titaVo.isHcodeNormal()) {
			// 帳務處理
			AcDetailRoutine();
		}
		// 放款交易內容檔
		if (titaVo.isHcodeNormal()) {
			addLoanBorTxRoutine();
		} else {
			loanCom.checkEraseCustNoTxSeqNo(iCustNo, titaVo);// 檢查到同戶帳務交易需由最近一筆交易開始訂正
			loanCom.setFacmBorTxHcodeByTx(iCustNo, titaVo);// 訂正放款交易內容檔by交易
		}

		// 產生會計分錄
		if (this.txBuffer.getTxCom().isBookAcYes()) {
			this.txBuffer.setAcDetailList(lAcDetail);
			acDetailCom.setTxBuffer(this.txBuffer);
			acDetailCom.run(titaVo);
			this.setTxBuffer(acDetailCom.getTxBuffer());
		}

		this.totaVo.put("PdfSnoF", "" + sno);
		this.addList(this.totaVo);
		return this.sendList();
	}

	private void TempAcDetailRoutine() throws LogicException {
		logger.info("TempAcDetailRoutine ... ");
		logger.info("   iTempAmt  = " + iTempAmt);
		logger.info("   wkTempBal = " + wkTempBal);
		if (iTempReasonCode == 1 && iCustNo != this.txBuffer.getSystemParas().getLoanDeptCustNo()) {
			try {
				this.baTxList = baTxCom.settingUnPaid(titaVo.getEntDyI(), iCustNo, iFacmNo, 0, 9, iTempAmt, titaVo); // 09-暫收(依暫收額度)
			} catch (LogicException e) {
				throw new LogicException(titaVo, "E0015", "查詢費用 " + e.getMessage()); // 檢查錯誤
			}

			// 暫收款金額 (暫收借)
			acRepayCom.settleTempAmt(this.baTxList, this.lAcDetail, titaVo);

			wkCustTempBal = baTxCom.getExcessive();
			wkTempBal = wkTempBal.subtract(wkCustTempBal);
		} else {

			// 查詢會計銷帳檔
			Slice<AcReceivable> slAcReceivable = acReceivableService.acrvFacmNoRange(0, iCustNo, 0, wkFacmNoS,
					wkFacmNoE, 0, Integer.MAX_VALUE);
			List<AcReceivable> lAcReceivable = slAcReceivable == null ? null : slAcReceivable.getContent();
			if (lAcReceivable != null && lAcReceivable.size() > 0) {
				for (AcReceivable ac : lAcReceivable) {
					if (iTempReasonCode == 1 && ac.getAcctCode().equals("TLD")
							|| (iTempReasonCode == 2 && ac.getAcctCode().substring(0, 2).equals("T1"))
							|| (iTempReasonCode == 3 && ac.getAcctCode().substring(0, 2).equals("T2"))
							|| (iTempReasonCode == 4 && ac.getAcctCode().equals("TAM"))) {
						if (ac.getRvBal().compareTo(new BigDecimal(0)) > 0
								&& wkTempBal.compareTo(new BigDecimal(0)) > 0) {
							// 借方 暫收及待結轉帳項-擔保放款
							AcDetail acDetail = new AcDetail();
							acDetail.setDbCr("D");
							acDetail.setRvNo(ac.getRvNo());
							acDetail.setAcctCode(ac.getAcctCode());
							acDetail.setCurrencyCode(iCurrencyCode);
							acDetail.setCustNo(iCustNo);
							acDetail.setFacmNo(ac.getFacmNo());
							acDetail.setSlipNote(titaVo.getParam("Description"));
							wkCustTempBal = wkCustTempBal.add(ac.getRvBal());
							if (wkTempBal.compareTo(ac.getRvBal()) >= 0) {
								acDetail.setTxAmt(ac.getRvBal());
								wkTempBal = wkTempBal.subtract(ac.getRvBal());
							} else {
								acDetail.setTxAmt(wkTempBal);
								wkTempBal = new BigDecimal(0);
							}
							lAcDetail.add(acDetail);
							logger.info("   loop wkTempBal   = " + wkTempBal);
							logger.info("        getRvNo     = " + ac.getRvNo());
							logger.info("        getRvBal    = " + ac.getRvBal());
							logger.info("        getAcctCode = " + ac.getAcctCode());
							logger.info("        getFacmNo   = " + ac.getFacmNo());
						}
					}
				}
			}
		}
		wkAcctCode = this.lAcDetail.get(0).getAcctCode();
		if (wkTempBal.compareTo(new BigDecimal(0)) > 0) {
			throw new LogicException(titaVo, "E3060", "目前客戶之暫收款 = " + wkCustTempBal); // 退還金額大於目前客戶之暫收款
		}
		logger.info("TempAcDetailRoutine end ");
		logger.info("   wkTempBal = " + wkTempBal);

	}

	private void ChequeAmtNormalRoutine() throws LogicException {
		logger.info("ChequeAmtNormalRoutine ... ");

		// 鎖定支票檔
		LoanCheque tLoanCheque = loanChequeService.holdById(new LoanChequeId(iChequeAcct, iChequeNo));
		if (tLoanCheque == null) {
			throw new LogicException(titaVo, "E0006", "支票檔 支票帳號 = " + iChequeAcct + " 支票號碼 =  " + iChequeNo); // 鎖定資料時，發生錯誤
		}
		if (!(tLoanCheque.getStatusCode().equals("0") || tLoanCheque.getStatusCode().equals("5"))) {
			throw new LogicException(titaVo, "E3058", "支票檔 支票帳號 = " + iChequeAcct + " 支票號碼 =  " + iChequeNo); // 該票據狀況碼非未處理與即期票
		}
		// 未關帳時當日不可抽退票,期票未托收不可抽票
		if (tLoanCheque.getReceiveDate() == this.txBuffer.getTxCom().getTbsdy()) {

			AcClose tAcClose = new AcClose();
			AcCloseId tAcCloseId = new AcCloseId();

			tAcCloseId.setAcDate(this.txBuffer.getTxCom().getTbsdy());
			tAcCloseId.setBranchNo(titaVo.getAcbrNo());
			tAcCloseId.setSecNo("02"); // 業務類別: 01-撥款匯款 02-支票繳款 09-放款

			tAcClose = acCloseService.findById(tAcCloseId);
			if (tAcClose != null && tAcClose.getClsFg() != 1) {
				throw new LogicException(titaVo, "E0010", "期票未托收不可抽票"); // 功能選擇錯誤
			}
		}

		wkChequeAmt = tLoanCheque.getChequeAmt();
		switch (iTempItemCode) {
		case 1: // 抽票
			tLoanCheque.setStatusCode("3");
			break;
		case 2: // 退票
			tLoanCheque.setStatusCode("2");
			break;
		case 3: // 服務中心代收抽退票
			tLoanCheque.setStatusCode("3");
		}
		try {
			tLoanCheque.setLastUpdate(
					parse.IntegerToSqlDateO(dDateUtil.getNowIntegerForBC(), dDateUtil.getNowIntegerTime()));
			tLoanCheque.setLastUpdateEmpNo(titaVo.getTlrNo());
			loanChequeService.update(tLoanCheque);
		} catch (DBException e) {
			throw new LogicException(titaVo, "E0007", "支票檔"); // 更新資料時，發生錯誤
		}
	}

	private void ChequeAmtEraseRoutine() throws LogicException {
		logger.info("ChequeAmtEraseRoutine ... ");

		// 鎖定支票檔
		LoanCheque tLoanCheque = loanChequeService.holdById(new LoanChequeId(iChequeAcct, iChequeNo));
		if (tLoanCheque == null) {
			throw new LogicException(titaVo, "E0006", "支票檔 支票帳號 = " + iChequeAcct + " 支票號碼 =  " + iChequeNo); // 鎖定資料時，發生錯誤
		}
		if (!(tLoanCheque.getStatusCode().equals("2") || tLoanCheque.getStatusCode().equals("3"))) {
			throw new LogicException(titaVo, "E3059", "支票檔 支票帳號 = " + iChequeAcct + " 支票號碼 =  " + iChequeNo); // 該票據狀況碼非為抽票或退票
		}
		wkChequeAmt = tLoanCheque.getChequeAmt();
		tLoanCheque.setStatusCode("0"); // 0: 未處理
		try {
			tLoanCheque.setLastUpdate(
					parse.IntegerToSqlDateO(dDateUtil.getNowIntegerForBC(), dDateUtil.getNowIntegerTime()));
			tLoanCheque.setLastUpdateEmpNo(titaVo.getTlrNo());
			loanChequeService.update(tLoanCheque);
		} catch (DBException e) {
			throw new LogicException(titaVo, "E0007", "支票檔"); // 更新資料時，發生錯誤
		}
	}

	private void ChequeAcDetailRoutine() throws LogicException {
		logger.info("ChequeAcDetailRoutine ... ");
		logger.info("   isBookAcYes = " + this.txBuffer.getTxCom().isBookAcYes());

		AcDetail acDetail;
		if (this.txBuffer.getTxCom().isBookAcYes()) {
			// 貸：應收票據－支票(RCK)
			acDetail = new AcDetail();
			acDetail.setDbCr("C");
			acDetail.setAcctCode("BCK");
			acDetail.setCurrencyCode(iCurrencyCode);
			acDetail.setTxAmt(wkChequeAmt);
			acDetail.setCustNo(iCustNo);
			acDetail.setSlipNote(titaVo.getParam("Description"));
			lAcDetail.add(acDetail);
		}
	}

	private void AcDetailRoutine() throws LogicException {
		logger.info("AcDetailRoutine ... ");
		logger.info("   isBookAcYes = " + this.txBuffer.getTxCom().isBookAcYes());

		if (this.txBuffer.getTxCom().isBookAcYes()) {
			// 貸方 收付欄
			acPaymentCom.run(titaVo);
			lAcDetail.addAll(this.txBuffer.getAcDetailList());
		}
		// 累溢收入帳(暫收貸)
		if (iTempReasonCode == 1 && iCustNo != this.txBuffer.getSystemParas().getLoanDeptCustNo()) {
			acRepayCom.settleOverflow(lAcDetail, titaVo);
		}
	}

	// 維護撥款匯款檔
	private void AcPaymentRoutine() throws LogicException {
		logger.info("AcPaymentRoutine ... ");

		if (titaVo.isActfgEntry()) {
			acPaymentCom.remit(titaVo);
		}
//		01:整批匯款
//		02:單筆匯款
//		04:退款台新(存款憑條)
//		05:退款他行(整批匯款)
//		11:退款新光(存款憑條)

		if (titaVo.isHcodeNormal() && titaVo.isActfgEntry() && (iTempItemCode == 4 || iTempItemCode == 11)) {
			sno = acPaymentCom.printRemitForm(titaVo);
		}
	}

	// 新增放款交易內容檔
	private void addLoanBorTxRoutine() throws LogicException {
		logger.info("addLoanBorTxRoutine ... ");
// TempItemCode
//		01	抽票
//		02	退票
//		03	服務中心代收抽票
//		04	退款他行(匯款單)
//		05	核心退款(整批匯款)
//		11	退款新光(存入憑條)
// Temp2ReasonCode
//		1	放款暫收款
//		2	債協暫收款
//		3	債協退還款
//		4	AML暫收款
//      5	聯貸費攤提暫收款

		tLoanBorTx = new LoanBorTx();
		tLoanBorTxId = new LoanBorTxId();
		loanCom.setFacmBorTx(tLoanBorTx, tLoanBorTxId, iCustNo, iFacmNo, titaVo);
		tLoanBorTx.setEntryDate(titaVo.getEntDyI());
		tLoanBorTx.setTxAmt(BigDecimal.ZERO.subtract(iTempAmt));
		// 3221 暫收款退還
		// 3222 抽票退還
		// 3223 退票退還
		// 3224 服務中心代收抽票退還
		// 3225 債協暫收款(退還)
		// 3226 債協退還款退還
		// 3227 AML暫收款退還
		// 3228 聯貸費攤提暫收款
		switch (iTempItemCode) {
		case 1:
			tLoanBorTx.setTxDescCode("3222");
			break;
		case 2:
			tLoanBorTx.setTxDescCode("3223");
			break;
		case 3:
			tLoanBorTx.setTxDescCode("3224");
			break;
		default:
			switch (iTempReasonCode) {
			case 2:
				tLoanBorTx.setTxDescCode("3225");
				break;
			case 3:
				tLoanBorTx.setTxDescCode("3226");
				break;
			case 4:
				tLoanBorTx.setTxDescCode("3227");
				break;
			case 5:
				tLoanBorTx.setTxDescCode("3228");
				break;
			default:
				tLoanBorTx.setTxDescCode("3221");
				break;
			}
		}
		tLoanBorTx.setDisplayflag("A"); // A:帳務
		tLoanBorTx.setTempAmt(wkCustTempBal);
		tLoanBorTx.setOverflow(wkCustTempBal.subtract(iTempAmt));
		tLoanBorTx.setAcctCode(wkAcctCode);
		// 其他欄位
		tTempVo.clear();
		tTempVo.putParam("TempItemCode", iTempItemCode);
		tTempVo.putParam("TempReasonCode", iTempReasonCode);
		tTempVo.putParam("Description", titaVo.getParam("Description"));
		tTempVo.putParam("RemitBank", titaVo.getParam("RpRemitBank1") + titaVo.getParam("RpRemitBranch1"));
		tTempVo.putParam("RemitAcctNo", titaVo.getParam("RpRemitAcctNo1"));
		tTempVo.putParam("RemitCustName", titaVo.getParam("RpCustName1"));
		tTempVo.putParam("RemitRemark", titaVo.getParam("RpRemark1"));
		tTempVo.putParam("RemitAmt", titaVo.getParam("RpAmt1"));
		// 新增摘要
		tTempVo.putParam("Note", titaVo.getParam("Description"));
		tLoanBorTx.setOtherFields(tTempVo.getJsonString());
		// 更新放款明細檔及帳務明細檔關聯欄
		acRepayCom.updBorTxAcDetail(this.tLoanBorTx, lAcDetail, titaVo);
		try {
			loanBorTxService.insert(tLoanBorTx, titaVo);
		} catch (DBException e) {
			throw new LogicException(titaVo, "E0005", "放款交易內容檔 " + e.getErrorMsg()); // 新增資料時，發生錯誤
		}
	}

}