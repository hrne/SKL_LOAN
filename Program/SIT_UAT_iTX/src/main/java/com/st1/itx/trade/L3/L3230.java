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
import com.st1.itx.db.domain.AcReceivable;
import com.st1.itx.db.domain.LoanBorTx;
import com.st1.itx.db.domain.LoanBorTxId;
import com.st1.itx.db.domain.LoanOverdue;
import com.st1.itx.db.domain.LoanOverdueId;
import com.st1.itx.db.domain.TxTemp;
import com.st1.itx.db.domain.TxTempId;
import com.st1.itx.db.domain.TxToDoDetail;
import com.st1.itx.db.service.AcReceivableService;
import com.st1.itx.db.service.LoanBorTxService;
import com.st1.itx.db.service.LoanChequeService;
import com.st1.itx.db.service.LoanOverdueService;
import com.st1.itx.db.service.TxTempService;
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.common.AcDetailCom;
import com.st1.itx.util.common.AcNegCom;
import com.st1.itx.util.common.AcPaymentCom;
import com.st1.itx.util.common.BaTxCom;
import com.st1.itx.util.common.LoanCom;
import com.st1.itx.util.common.TxToDoCom;
import com.st1.itx.util.common.data.BaTxVo;
import com.st1.itx.util.date.DateUtil;
import com.st1.itx.util.parse.Parse;
/*
 * L3230 暫收款銷帳
 * a.此功能供沖銷執行費、火險費、帳管費/手續費..等
 */

/*
 * Tita
 * TimCustNo=9,7
 * CustId=X,10
 * ApplNo=9,7
 * FacmNo=9,3
 * TempReasonCode=9,1
 * TempItemCode=9,2
 * CurrencyCode=X,3
 * TimTempAmt=9,14.2
 * ChangeDate=9,7
 * ChangeItemCode=9,2
 * Year=9,3
 * Month=9,2
 * Description=X,30
 * BormNo=9,3
 * RemoveNo=9,7
 */
/**
 * L3230 暫收款銷帳
 * 
 * @author iza
 * @version 1.0.0
 */
@Service("L3230")
@Scope("prototype")
public class L3230 extends TradeBuffer {

	/* DB服務注入 */
	@Autowired
	public AcReceivableService acReceivableService;
	@Autowired
	public LoanChequeService loanChequeService;
	@Autowired
	public TxTempService txTempService;
	@Autowired
	public LoanOverdueService loanOverdueService;
	@Autowired
	public LoanBorTxService loanBorTxService;

	@Autowired
	Parse parse;
	@Autowired
	LoanCom loanCom;
	@Autowired
	AcDetailCom acDetailCom;
	@Autowired
	AcNegCom acNegCom;
	@Autowired
	DateUtil dDateUtil;
	@Autowired
	AcPaymentCom acPaymentCom;
	@Autowired
	TxToDoCom txToDoCom;
	@Autowired
	BaTxCom baTxCom;

	private TitaVo titaVo = new TitaVo();
	private int iCustNo;
	private int iFacmNo;
	private String iTempItemCode;
	private String iRemoveNo;
	private int iTempReasonCode;
	private String iCurrencyCode;
	private BigDecimal iTempAmt;
	private BigDecimal wkTempBal;
	private BigDecimal wkCustTempBal;

	// work area
	private int wkFacmNoStart = 0;
	private int wkFacmNoEnd = 999;
	private int wkCustNo = 0;
	private int wkFacmNo = 0;
	private int wkBormNo = 0;
	private int wkOvduNo = 0;
	private String wkAcctCode;
	private String iAcSubBookCode;
	private LoanOverdue tLoanOverdue;
	private AcDetail acDetail;
	private TxTemp tTxTemp;
	private TxTempId tTxTempId;
	private TempVo tTempVo = new TempVo();
	private List<TxTemp> lTxTemp;
	private List<AcDetail> lAcDetail;
	private List<LoanOverdue> lLoanOverdue = new ArrayList<LoanOverdue>();
	private LoanBorTx tLoanBorTx;
	private LoanBorTxId tLoanBorTxId;
	private BigDecimal acctFee;
	private BigDecimal modifyFee;
	private BigDecimal fireFee;
	private BigDecimal lawFee;

	// initialize variable
	@PostConstruct
	public void init() {
		this.iCustNo = 0;
		this.iFacmNo = 0;
		this.iTempItemCode = "";
		this.iTempReasonCode = 0;
		this.iCurrencyCode = "";
		this.wkCustTempBal = new BigDecimal(0);
		this.iTempAmt = new BigDecimal(0);
		this.iRemoveNo = "";
		this.lAcDetail = new ArrayList<AcDetail>();
		this.acctFee = BigDecimal.ZERO;
		this.modifyFee = BigDecimal.ZERO;
		this.fireFee = BigDecimal.ZERO;
		this.lawFee = BigDecimal.ZERO;
	}

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active L3230 ");
		baTxCom.setTxBuffer(this.txBuffer);
		acNegCom.setTxBuffer(this.txBuffer);

		this.totaVo.init(titaVo);

		// 取得輸入資料
		this.titaVo = titaVo;
		iCustNo = this.parse.stringToInteger(titaVo.getParam("TimCustNo"));
		iFacmNo = this.parse.stringToInteger(titaVo.getParam("FacmNo"));
		iTempAmt = this.parse.stringToBigDecimal(titaVo.getParam("TimTempAmt"));
		iTempReasonCode = this.parse.stringToInteger(titaVo.getParam("TempReasonCode"));
		iTempItemCode = titaVo.getParam("TempItemCode");
		iCurrencyCode = titaVo.getParam("CurrencyCode");
		iRemoveNo = titaVo.getParam("RemoveNo"); // 銷帳編號
		iAcSubBookCode = titaVo.getParam("AcSubBookCode"); // 區隔帳冊

		wkTempBal = iTempAmt;

		// 檢查輸入資料
		if (iTempReasonCode == 0 || iTempReasonCode > 4) {
			throw new LogicException(titaVo, "E0019", "暫收帳戶"); // 輸入資料錯誤
		}
		if (iFacmNo > 0) {
			wkFacmNoStart = iFacmNo;
			wkFacmNoEnd = iFacmNo;
		}

		// 催收檔處理
		if (iTempItemCode.equals("08")) { // 08: 收回呆帳
			if (titaVo.isHcodeNormal()) {
				UpdLoanOverDueNormalRoutine();
			}
			if (titaVo.isHcodeErase()) {
				UpdLoanOverDueEraseRoutine();
			}
		}
// 作業項目              業務科目
// 06.轉帳               收付欄
// 07.沖執行費                     F07	暫付法務費	
// 08.收回呆帳                     F08	收回呆帳及過期帳                                   
// 09.沖火險費                     F09	暫付款－火險保費、TMI	暫收款－火險保費
// 10.沖帳管費/手續費          F10	帳管費 
// 12.聯貸件             F12聯貸件 
// 13.沖什項收入                  F13	什項收入
// 14.NPL-銷項稅額             F14  暫付及待結轉帳項－預所稅－放款部
// 15.921貸款戶                  F15	利息收入－九二一貸款戶
// 16.3200億專案                F16	利息收入－３２００億專案息
// 17.3200億-利變        X                              
// 18.沖備抵呆帳                  F18	備抵呆帳－催收款項
// 19.轉債權協商         T10~T13 債協科目 
// 20.轉應付代收         X => L5708 最大債權撥付出帳 
// 21.88風災                       F21	利息收入－88風災貸款戶
// 22.88風災-保費        ???
// 23.3200億傳統A        X                                            
// 24.沖催收法務費              F24	催收款項－法務費用
// 25.沖催收火險費              F25	催收款項－火險費用
// 27.沖聯貸費用                  F27	聯貸管理費收入    
// 29.貸後契變手續費           F29	契變手續費

		// 帳務處理
		if (titaVo.isHcodeNormal() && this.txBuffer.getTxCom().isBookAcYes()) {
			wkTempBal = iTempAmt;
			TempAcDetailRoutine(); // 借: 暫收款科目
			switch (iTempItemCode) {
			case "06": // 06.轉帳
				this.txBuffer.addAllAcDetailList(lAcDetail);
				// 貸方 收付欄
				acPaymentCom.setTxBuffer(this.getTxBuffer());
				acPaymentCom.run(titaVo);
				break;

			case "07": // 07.沖執行費
			case "09": // 09.沖火險費
			case "10": // 10.沖帳管費/手續費
			case "24": // 24.沖催收法務費
			case "25": // 25.沖催收火險費
			case "12": // 12.聯貸件
			case "27": // 27.聯貸管理費
			case "29": // 29.貸後契變手續費
			case "30": // 30.沖呆帳戶法務費墊付
				settingUnPaid("F" + iTempItemCode);
				this.txBuffer.addAllAcDetailList(lAcDetail);
				break;

			case "19": // 19.轉債權協商 T10~T13 債協科目
				NegAcDetailRoutine();
				this.txBuffer.addAllAcDetailList(lAcDetail);
				break;

			case "20": // 20.轉應付代收
				throw new LogicException(titaVo, "E0010", "由L5708 最大債權撥付出帳 "); // E0010 功能選擇錯誤

			case "16": // 16.沖呆帳戶法務費墊付
				subsidyInterest();
				this.txBuffer.addAllAcDetailList(lAcDetail);
				break;

			case "17": // 17.3200億-利變
			case "23": // 23.3200億傳統A
				throw new LogicException(titaVo, "E0010", "請使用 L6201 其他傳票輸入"); // E0010 功能選擇錯誤

			case "22": // 22.88風災-保費 ???
				throw new LogicException(titaVo, "E0010", "無對應之會計科目"); // E0010 功能選擇錯誤

			default:
				// 貸: 作業項目對應科目
				wkAcctCode = "F" + iTempItemCode;
				ItemAcDetailRoutine();
				this.txBuffer.addAllAcDetailList(lAcDetail);
			}
			// 產生會計分錄
			acDetailCom.setTxBuffer(this.txBuffer);
			acDetailCom.run(titaVo);
			this.setTxBuffer(acDetailCom.getTxBuffer());
		}

		// 放款交易內容檔
		if (iTempAmt.compareTo(new BigDecimal(0)) > 0) {
			setLoanBorTxRoutine();
		}

		this.addList(this.totaVo);
		return this.sendList();
	}

	private void TempAcDetailRoutine() throws LogicException {
		this.info("TempAcDetailRoutine ... ");
		this.info("   iTempReasonCode = " + iTempReasonCode);
		this.info("   iFacmNo         = " + iFacmNo);

		// 查詢會計銷帳檔
		Slice<AcReceivable> slAcReceivable = acReceivableService.acrvFacmNoRange(0, iCustNo, 0, wkFacmNoStart,
				wkFacmNoEnd, 0, Integer.MAX_VALUE);
		List<AcReceivable> lAcReceivable = slAcReceivable == null ? null : slAcReceivable.getContent();
		if (lAcReceivable != null && lAcReceivable.size() > 0) {
			for (AcReceivable ac : lAcReceivable) {
				this.info("   wkTempBal   = " + wkTempBal);
				this.info("   getAcctCode = " + ac.getAcctCode());
				this.info("   getRvBal    = " + ac.getRvBal());
				this.info("   getFacmNo   = " + ac.getFacmNo());
				this.info("   wkTempBal   = " + wkTempBal);
				if ((iTempReasonCode == 1 && (ac.getAcctCode().equals("TAV") || ac.getAcctCode().equals("TLD")))
						|| (iTempReasonCode == 2 && ac.getAcctCode().substring(0, 2).equals("T1"))
						|| (iTempReasonCode == 3 && ac.getAcctCode().substring(0, 2).equals("T2"))
						|| (iTempReasonCode == 4 && ac.getAcctCode().equals("TAM"))) {
					if ((iFacmNo == 0 || iFacmNo == ac.getFacmNo()) && ac.getRvBal().compareTo(new BigDecimal(0)) > 0
							&& wkTempBal.compareTo(new BigDecimal(0)) > 0) {
						// 借方 暫收及待結轉帳項-擔保放款
						AcDetail acDetail = new AcDetail();
						acDetail.setDbCr("D");
						acDetail.setAcctCode(ac.getAcctCode());
						acDetail.setCurrencyCode(iCurrencyCode);
						acDetail.setCustNo(iCustNo);
						acDetail.setFacmNo(ac.getFacmNo());
						acDetail.setSlipNote(titaVo.getParam("Description"));
						lAcDetail.add(acDetail);
						this.info("   TempAcDetail   = " + acDetail.toString());
						wkCustTempBal = wkCustTempBal.add(ac.getRvBal());
						if (wkTempBal.compareTo(ac.getRvBal()) >= 0) {
							acDetail.setTxAmt(ac.getRvBal());
							wkTempBal = wkTempBal.subtract(ac.getRvBal());
						} else {
							acDetail.setTxAmt(wkTempBal);
							wkTempBal = new BigDecimal(0);
						}
					}
				}
			}
		}
		if (wkTempBal.compareTo(new BigDecimal(0)) > 0) {
			this.info("   E3060 wkTempBal      = " + wkTempBal);
			this.info("         wkCustTempBal  = " + wkCustTempBal);
			throw new LogicException(titaVo, "E3060", "目前客戶之暫收款 = " + wkCustTempBal); // 退還金額大於目前客戶之暫收款
		}
	}

	// 貸: 作業項目對應的應繳費用，業務科目、金額需相同
	private void settingUnPaid(String acctCode) throws LogicException {
		ArrayList<BaTxVo> baTxList = new ArrayList<BaTxVo>();
		try {
			baTxList = baTxCom.settingUnPaid(titaVo.getEntDyI(), iCustNo, iFacmNo, 0, 0, iTempAmt, titaVo);
		} catch (LogicException e) {
			throw new LogicException(titaVo, "E0015", "查詢費用 " + e.getMessage()); // 檢查錯誤
		}

		if (baTxList == null || baTxList.size() == 0) {
			throw new LogicException(titaVo, "E0019", "無該項目未銷費用"); // 查詢資料不存在
		}
		BigDecimal totalAmt = BigDecimal.ZERO;
		// 單筆相同金額
		boolean isFind = false;
		for (BaTxVo ba : baTxList) {
			if (acctCode.equals(ba.getAcctCode()) || "F09".equals(acctCode) && "TMI".equals(ba.getAcctCode())) {
				totalAmt = totalAmt.add(ba.getAcctAmt());
				if (iTempAmt.compareTo(ba.getUnPaidAmt()) == 0
						&& ("0000000".equals(iRemoveNo) || iRemoveNo.equals(ba.getRvNo()))) {
					acDetail = new AcDetail();
					acDetail.setDbCr("C");
					acDetail.setAcctCode(ba.getAcctCode());
					acDetail.setTxAmt(ba.getAcctAmt());
					acDetail.setCustNo(ba.getCustNo());
					acDetail.setFacmNo(ba.getFacmNo());
					acDetail.setReceivableFlag(ba.getReceivableFlag());
					acDetail.setRvNo(ba.getRvNo());
					lAcDetail.add(acDetail);
					addFeeRoutine(ba);
					isFind = true;
					break;
				}
			}
		}
		if (!isFind && iTempAmt.compareTo(totalAmt) == 0) {
			for (BaTxVo ba : baTxList) {
				if (acctCode.equals(ba.getAcctCode()) || "F09".equals(acctCode) && "TMI".equals(ba.getAcctCode())) {
					acDetail = new AcDetail();
					acDetail.setDbCr("C");
					acDetail.setAcctCode(ba.getAcctCode());
					acDetail.setTxAmt(ba.getAcctAmt());
					acDetail.setCustNo(ba.getCustNo());
					acDetail.setFacmNo(ba.getFacmNo());
					acDetail.setReceivableFlag(ba.getReceivableFlag());
					acDetail.setRvNo(ba.getRvNo());
					lAcDetail.add(acDetail);
					addFeeRoutine(ba);
					isFind = true;
				}
			}
		}
		if (!isFind) {
			throw new LogicException(titaVo, "E0019", "無金額相同之未銷費用，請使用 L6907 未銷帳餘額明細查詢 "); // 查詢資料不存在
		}
	}

	// 借:暫收可抵繳 貸:利息收入(3200億專案息)
	private void subsidyInterest() throws LogicException {
		AcDetail acDetail = new AcDetail();
		acDetail.setDbCr("D");
		acDetail.setAcctCode("TAV");
		acDetail.setCurrencyCode(iCurrencyCode);
		acDetail.setTxAmt(iTempAmt);
		acDetail.setCustNo(iCustNo);
		acDetail.setSlipNote(titaVo.getParam("Description"));
		lAcDetail.add(acDetail);

		acDetail = new AcDetail();
		acDetail.setDbCr("C");
		acDetail.setAcctCode("F16");
		acDetail.setCurrencyCode(iCurrencyCode);
		acDetail.setTxAmt(iTempAmt);
		acDetail.setCustNo(iCustNo);
		acDetail.setSlipNote(titaVo.getParam("Description"));
		acDetail.setAcSubBookCode(iAcSubBookCode); // 區隔帳冊
		acDetail.setAcBookFlag(3); // 帳冊別記號 (3: 指定帳冊)
		lAcDetail.add(acDetail);
	}

	private void addFeeRoutine(BaTxVo ba) throws LogicException {
		switch (ba.getRepayType()) {
		case 4: // 04-帳管費
			this.acctFee = this.acctFee.add(ba.getAcctAmt());
			break;
		case 5: // 05-火險費
			this.fireFee = this.fireFee.add(ba.getAcctAmt());
			break;
		case 6: // 06-契變手續費
			this.modifyFee = this.modifyFee.add(ba.getAcctAmt());
			break;
		case 7: // 07-法務費
			this.lawFee = this.lawFee.add(ba.getAcctAmt());
			break;
		}
	}

	// 貸: 債權協商對應科目
	private void NegAcDetailRoutine() throws LogicException {
		AcDetail acDetail = new AcDetail();
		acDetail.setDbCr("C");
		acDetail.setAcctCode(acNegCom.getAcctCode(iCustNo, titaVo));
		acDetail.setCurrencyCode(iCurrencyCode);
		acDetail.setTxAmt(iTempAmt);
		acDetail.setCustNo(iCustNo);
		acDetail.setSlipNote(titaVo.getParam("Description"));
		lAcDetail.add(acDetail);
	}

	// 貸: 作業項目對應科目
	private void ItemAcDetailRoutine() throws LogicException {
		AcDetail acDetail = new AcDetail();
		acDetail.setDbCr("C");
		acDetail.setAcctCode(wkAcctCode);
		acDetail.setCurrencyCode(iCurrencyCode);
		acDetail.setTxAmt(iTempAmt);
		acDetail.setCustNo(iCustNo);
		acDetail.setFacmNo(iFacmNo);
		acDetail.setSlipNote(titaVo.getParam("Description"));
		lAcDetail.add(acDetail);
	}

	private void UpdLoanOverDueNormalRoutine() throws LogicException {
		this.info("UpdLoanOverDueNormalRoutine ...");

		BigDecimal wkBadDebtBal = BigDecimal.ZERO;
		BigDecimal wkTotalBadAmt = BigDecimal.ZERO;

		List<Integer> lStatus = new ArrayList<Integer>(); // 1:催收 2:部分轉呆 3:呆帳 4:催收回復
		lStatus.add(2);
		lStatus.add(3);
		Slice<LoanOverdue> slLoanOverdue = loanOverdueService.ovduCustNoRange(iCustNo, wkFacmNoStart, wkFacmNoEnd, 1,
				900, 1, 999, lStatus, 0, Integer.MAX_VALUE);
		lLoanOverdue = slLoanOverdue == null ? null : slLoanOverdue.getContent();
		if (lLoanOverdue == null || lLoanOverdue.size() == 0) {
			throw new LogicException(titaVo, "E0001", "催收呆帳檔"); // 查詢資料不存在
		}
		for (LoanOverdue od : lLoanOverdue) {
			if (od.getBadDebtBal().compareTo(BigDecimal.ZERO) == 0) {
				continue;
			}
			if (wkTempBal.compareTo(BigDecimal.ZERO) == 0) {
				break;
			}
			wkCustNo = od.getCustNo();
			wkFacmNo = od.getFacmNo();
			wkBormNo = od.getBormNo();
			wkOvduNo = od.getOvduNo();
			if (wkTempBal.compareTo(od.getBadDebtBal()) >= 0) {
				wkTempBal = wkTempBal.subtract(od.getBadDebtBal());
				wkBadDebtBal = od.getBadDebtBal();
				od.setBadDebtBal(BigDecimal.ZERO);
			} else {
				od.setBadDebtBal(od.getBadDebtBal().subtract(wkTempBal));
				wkBadDebtBal = wkTempBal;
				wkTempBal = BigDecimal.ZERO;
			}
			wkTotalBadAmt = wkTotalBadAmt.add(wkBadDebtBal);
			if (od.getBadDebtBal().compareTo(BigDecimal.ZERO) == 0) {
				ZeroBadDebtBal();
			}
			tTxTemp = new TxTemp();
			tTxTempId = new TxTempId();
			loanCom.setTxTemp(tTxTempId, tTxTemp, od.getCustNo(), od.getFacmNo(), od.getBormNo(), od.getOvduNo(),
					titaVo);
			tTempVo.clear();
			tTempVo.putParam("BadDebtBal", wkBadDebtBal);
			tTempVo.putParam("ZeroBadDebtBal", od.getBadDebtBal().compareTo(BigDecimal.ZERO) == 0 ? "Y" : "N");
			tTxTemp.setText(tTempVo.getJsonString());
			try {
				txTempService.insert(tTxTemp);
			} catch (DBException e) {
				throw new LogicException(titaVo, "E0005", "交易暫存檔 Key = " + tTxTempId); // 新增資料時，發生錯誤 }
			}
			try {
				loanOverdueService.update(od);
			} catch (DBException e) {
				throw new LogicException(titaVo, "E0007", "催收呆帳檔 戶號 = " + od.getCustNo() + " 額度編號 = " + od.getFacmNo()
						+ " 撥款序號 = " + od.getBormNo() + " 催收序號 = " + od.getOvduNo()); // 更新資料時，發生錯誤
			}
		}
		if (wkTotalBadAmt.compareTo(BigDecimal.ZERO) == 0) {
			throw new LogicException(titaVo, "E0019", "該戶查無呆帳餘額"); // 輸入資料錯誤
		}
		if (wkTempBal.compareTo(BigDecimal.ZERO) > 0) {
			throw new LogicException(titaVo, "E0019", "該戶呆帳餘額 = " + wkTotalBadAmt); // 輸入資料錯誤
		}
	}

	private void UpdLoanOverDueEraseRoutine() throws LogicException {
		this.info("UpdLoanOverDueEraseRoutine ...");

		BigDecimal wkBadDebtBal = BigDecimal.ZERO;

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
			wkBormNo = this.parse.stringToInteger(tx.getSeqNo().substring(10, 13));
			wkOvduNo = this.parse.stringToInteger(tx.getSeqNo().substring(13, 16));
			tTempVo = tTempVo.getVo(tx.getText());
			this.info("   SeqNo    = " + tx.getSeqNo());
			this.info("   wkCustNo = " + wkCustNo);
			this.info("   wkFacmNo = " + wkFacmNo);
			this.info("   wkBormNo = " + wkBormNo);
			this.info("   wkOvduNo = " + wkOvduNo);

			// 還原催收檔
			tLoanOverdue = loanOverdueService.holdById(new LoanOverdueId(wkCustNo, wkFacmNo, wkBormNo, wkOvduNo));
			if (tLoanOverdue == null) {
				throw new LogicException(titaVo, "E0006", "催收呆帳檔 戶號 = " + wkCustNo + " 額度編號 = " + wkFacmNo + " 撥款序號 = "
						+ wkBormNo + " 催收序號 = " + wkOvduNo); // 鎖定資料時，發生錯誤
			}
			wkBadDebtBal = this.parse.stringToBigDecimal(tTempVo.get("BadDebtBal"));
			tLoanOverdue.setBadDebtBal(tLoanOverdue.getBadDebtBal().add(wkBadDebtBal));
			try {
				loanOverdueService.update(tLoanOverdue);
			} catch (DBException e) {
				throw new LogicException(titaVo, "E0007", "催收呆帳檔 戶號 = " + wkCustNo + " 額度編號 = " + wkFacmNo + " 撥款序號 = "
						+ wkBormNo + " 催收序號 = " + wkOvduNo); // 更新資料時，發生錯誤
			}
			if (tTempVo.get("ZeroBadDebtBal").equals("Y")) {
				ZeroBadDebtBal();
			}
		}
	}

	// 額度內呆帳全部收回(餘額為0)自動寫入應處理清單，待擔保品項下呆帳全部回收時，由經辦自行按鈕執行[結案登錄]
	private void ZeroBadDebtBal() throws LogicException {
		txToDoCom.setTxBuffer(this.txBuffer);
		TxToDoDetail tTxToDoDetail = new TxToDoDetail();
		tTxToDoDetail.setItemCode("BDCL00"); // 呆帳還清待結案
		tTxToDoDetail.setCustNo(wkCustNo);
		tTxToDoDetail.setFacmNo(wkFacmNo);
		tTxToDoDetail.setProcessNote("該額度呆帳已還清，待擔保品項下呆帳全部回收時，請按鈕執行[結案登錄]");
		txToDoCom.addDetail(true, titaVo.getHCodeI(), tTxToDoDetail, titaVo); // DupSkip = true ->重複跳過
	}

	// 放款交易內容檔
	private void setLoanBorTxRoutine() throws LogicException {
		int rpCustNo = parse.stringToInteger(titaVo.getParam("RpCustNo1"));
		int rpFacmNo = parse.stringToInteger(titaVo.getParam("RpFacmNo1"));
		if (titaVo.isHcodeNormal()) {
			addLoanBorTxRoutine();
			// 暫收款轉帳
			if ("06".equals(iTempItemCode)) {
				addLoanBorTx06Routine(rpCustNo, rpFacmNo);
			}
		} else {
			loanCom.setFacmBorTxHcode(iCustNo, iFacmNo, titaVo);
			if ("06".equals(iTempItemCode)) {
				loanCom.setFacmBorTxHcode(rpCustNo, rpFacmNo, titaVo);
			}

		}
	}

	// 新增放款交易內容檔
	private void addLoanBorTxRoutine() throws LogicException {
		this.info("addLoanBorTxRoutine ... ");
		tLoanBorTx = new LoanBorTx();
		tLoanBorTxId = new LoanBorTxId();
		loanCom.setFacmBorTx(tLoanBorTx, tLoanBorTxId, iCustNo, iFacmNo, titaVo);
		if ("06".equals(iTempItemCode)) {
			tLoanBorTx.setDesc("暫收款轉帳");
		} else {
			tLoanBorTx.setDesc("暫收款銷帳");
		}
		//
		tLoanBorTx.setDisplayflag("A"); // A:帳務
		tLoanBorTx.setTempAmt(BigDecimal.ZERO.subtract(iTempAmt));
		tTempVo.clear();

		tTempVo.putParam("TempItemCode", iTempItemCode);
		tTempVo.putParam("Description", titaVo.getParam("Description"));
		if (this.acctFee.compareTo(BigDecimal.ZERO) > 0) {
			tTempVo.putParam("AcctFee", this.acctFee);
		}
		if (this.modifyFee.compareTo(BigDecimal.ZERO) > 0) {
			tTempVo.putParam("ModifyFee", this.modifyFee);
		}
		if (this.fireFee.compareTo(BigDecimal.ZERO) > 0) {
			tTempVo.putParam("FireFee", this.fireFee);
		}
		if (this.lawFee.compareTo(BigDecimal.ZERO) > 0) {
			tTempVo.putParam("LawFee", this.lawFee);
		}
		tLoanBorTx.setOtherFields(tTempVo.getJsonString());
		try {
			loanBorTxService.insert(tLoanBorTx);
		} catch (DBException e) {
			throw new LogicException(titaVo, "E0005", "放款交易內容檔 " + e.getErrorMsg()); // 新增資料時，發生錯誤
		}
	}

	// 新增放款交易內容檔
	private void addLoanBorTx06Routine(int rpCustNo, int rpFacmNo) throws LogicException {
		this.info("addLoanBorTx06Routine ... ");
		tLoanBorTx = new LoanBorTx();
		tLoanBorTxId = new LoanBorTxId();
		loanCom.setFacmBorTx(tLoanBorTx, tLoanBorTxId, rpCustNo, rpFacmNo, titaVo);
		tLoanBorTx.setDesc("暫收款轉帳");
		//
		tLoanBorTx.setDisplayflag("A"); // A:帳務
		tLoanBorTx.setTempAmt(iTempAmt);
		tTempVo.clear();
		// 其他欄位
		tTempVo.clear();
		tTempVo.putParam("TempItemCode", iTempItemCode);
		tTempVo.putParam("Description", titaVo.getParam("Description"));
		tLoanBorTx.setOtherFields(tTempVo.getJsonString());
		try {
			loanBorTxService.insert(tLoanBorTx);
		} catch (DBException e) {
			throw new LogicException(titaVo, "E0005", "放款交易內容檔 " + e.getErrorMsg()); // 新增資料時，發生錯誤
		}
	}
}