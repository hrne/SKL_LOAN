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
import com.st1.itx.db.domain.CustMain;
import com.st1.itx.db.domain.FacMain;
import com.st1.itx.db.domain.FacMainId;
import com.st1.itx.db.domain.LoanBorTx;
import com.st1.itx.db.domain.LoanBorTxId;
import com.st1.itx.db.domain.LoanOverdue;
import com.st1.itx.db.domain.LoanOverdueId;
import com.st1.itx.db.domain.TxTemp;
import com.st1.itx.db.domain.TxTempId;
import com.st1.itx.db.domain.TxToDoDetail;
import com.st1.itx.db.service.AcReceivableService;
import com.st1.itx.db.service.CustMainService;
import com.st1.itx.db.service.FacMainService;
import com.st1.itx.db.service.LoanBorTxService;
import com.st1.itx.db.service.LoanOverdueService;
import com.st1.itx.db.service.TxTempService;
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.common.AcDetailCom;
import com.st1.itx.util.common.AcNegCom;
import com.st1.itx.util.common.AcRepayCom;
import com.st1.itx.util.common.BaTxCom;
import com.st1.itx.util.common.LoanCom;
import com.st1.itx.util.common.SendRsp;
import com.st1.itx.util.common.TxToDoCom;
import com.st1.itx.util.common.data.BaTxVo;
import com.st1.itx.util.parse.Parse;
/*
 * L3230 暫收款銷帳
 * a.此功能供沖銷執行費、火險費、帳管費/手續費..等
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
	public TxTempService txTempService;
	@Autowired
	public LoanOverdueService loanOverdueService;
	@Autowired
	public LoanBorTxService loanBorTxService;
	@Autowired
	public CustMainService custMainService;
	@Autowired
	public FacMainService facMainService;

	@Autowired
	Parse parse;
	@Autowired
	LoanCom loanCom;
	@Autowired
	AcDetailCom acDetailCom;
	@Autowired
	AcNegCom acNegCom;
	@Autowired
	TxToDoCom txToDoCom;
	@Autowired
	BaTxCom baTxCom;
	@Autowired
	AcRepayCom acRepayCom;
	@Autowired
	SendRsp sendRsp;

	private TitaVo titaVo = new TitaVo();
	private int iCustNo;
	private int iFacmNo;
	private int iRpCustNo = 0;
	private int iRpFacmNo = 0;
	private int iRpCode = 90;
	private BigDecimal iRpAmt = BigDecimal.ZERO;
	private String iTempItemCode;
	private String iRemoveNo;
	private int iTempReasonCode;
	private String iCurrencyCode;
	private String iNote;
	private BigDecimal iTempAmt;
	private BigDecimal wkTempBal = BigDecimal.ZERO;
	private BigDecimal wkCustTempBal = BigDecimal.ZERO;
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
	private ArrayList<BaTxVo> baTxList = new ArrayList<BaTxVo>();
	private List<LoanBorTx> lLoanBorTx = new ArrayList<LoanBorTx>();
	private LoanBorTx tLoanBorTx;
	private LoanBorTxId tLoanBorTxId;

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
		this.iNote = "";
		this.lAcDetail = new ArrayList<AcDetail>();
		this.lLoanBorTx = new ArrayList<LoanBorTx>();
	}

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active L3230 ");
		baTxCom.setTxBuffer(this.txBuffer);
		loanCom.setTxBuffer(this.txBuffer);
		acNegCom.setTxBuffer(this.txBuffer);
		acRepayCom.setTxBuffer(this.txBuffer);
		this.totaVo.init(titaVo);

		// 取得輸入資料
		this.titaVo = titaVo;
		iCustNo = this.parse.stringToInteger(titaVo.getParam("TimCustNo"));
		iFacmNo = this.parse.stringToInteger(titaVo.getParam("FacmNo"));
		iTempAmt = this.parse.stringToBigDecimal(titaVo.getParam("TimTempAmt"));
		iTempReasonCode = this.parse.stringToInteger(titaVo.getParam("TempReasonCode"));
		iTempItemCode = titaVo.getParam("TempItemCode");
		iCurrencyCode = titaVo.getParam("CurrencyCode");
		iRemoveNo = titaVo.getParam("RemoveNo").trim(); // 銷帳編號
		iAcSubBookCode = titaVo.getParam("AcSubBookCode"); // 區隔帳冊
		iNote = titaVo.getParam("Description"); // 摘要內容
		
		wkTempBal = iTempAmt;

		// 檢查輸入資料
		if (iTempReasonCode == 0 || iTempReasonCode > 5) {
			throw new LogicException(titaVo, "E0019", "暫收帳戶"); // 輸入資料錯誤
		}
		if (iTempReasonCode == 2 && iCustNo != this.txBuffer.getSystemParas().getNegDeptCustNo()) {
			throw new LogicException(titaVo, "E0019", "戶號需為前置協商收款專戶"); // 輸入資料錯誤
		}
		if (iFacmNo > 0) {
			wkFacmNoStart = iFacmNo;
			wkFacmNoEnd = iFacmNo;
		}
		if ("06".equals(iTempItemCode) && titaVo.isHcodeNormal()) {
			Checktransfer();
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
// 12.企金件             F12企金帳管費收入
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
		if (titaVo.isHcodeNormal()) {
			// Load 暫收款 && 費用
			loadUnPaidRoutine();
			switch (iTempItemCode) {
			case "06": // 06.轉帳
				// 借：暫收款科目
				TempAcDetailRoutine();
				// 放款交易明細
				addLoanBorTxRoutine();
				this.txBuffer.setAcDetailList(lAcDetail);
				// 貸：暫收款科目
				for (int i = 1; i <= 50; i++) {
					/* 還款來源／撥款方式為 0 者跳出 */
					if (titaVo.get("RpCode" + i) == null || parse.stringToInteger(titaVo.getParam("RpCode" + i)) == 0)
						break;
					iRpCustNo = parse.stringToInteger(titaVo.getParam("RpCustNo" + i));
					iRpFacmNo = parse.stringToInteger(titaVo.getParam("RpFacmNo" + i));
					iRpCode = parse.stringToInteger(titaVo.getParam("RpCode" + i));
					iRpAmt = parse.stringToBigDecimal(titaVo.getParam("RpAmt" + i));
					// 新增分錄、放款交易明細
					settingUnPaid06();
					this.txBuffer.setAcDetailList(lAcDetail);
				}
				break;
			case "07": // 07.沖執行費
			case "09": // 09.沖火險費
			case "10": // 10.沖帳管費/手續費
			case "24": // 24.沖催收法務費
			case "25": // 25.沖催收火險費
			case "12": // 12.聯貸管理費
			case "27": // 27.放款帳管費
			case "29": // 29.貸後契變手續費
			case "30": // 30.沖呆帳戶法務費墊付
				setUnPaidFee("F" + iTempItemCode);
				// 借： 暫收款科目
				TempAcDetailRoutine();
				// 貸：acRepayCom.settleTempRun 出帳
				break;

			case "19": // 19.轉債權協商 T10~T13 債協科目
				// 借：暫收款科目
				TempAcDetailRoutine();
				// 貸：acRepayCom.settleTempRun 債協科目
				NegAcDetailRoutine();
				// 新贈放款交易明細
				addLoanBorTxRoutine();
				break;

			case "20": // 20.轉應付代收
				throw new LogicException(titaVo, "E0010", "由L5708 最大債權撥付出帳 "); // E0010 功能選擇錯誤

			case "16": // 16.3200億專案
				// 借：暫收款科目
				TempAcDetailRoutine();
				// 貸：補貼息科目
				subsidyInterest();
				// 新贈放款交易明細
				addLoanBorTxRoutine();
				break;

			case "22": // 22.88風災-保費 ???
				throw new LogicException(titaVo, "E0010", "無對應之會計科目"); // E0010 功能選擇錯誤

			default:
				// 借：暫收款科目
				TempAcDetailRoutine();
				// 貸: 作業項目對應科目
				wkAcctCode = "F" + iTempItemCode;
				ItemAcDetailRoutine();
				addLoanBorTxRoutine();
			}
		}
		// 訂正放款交易內容檔by交易
		if (titaVo.isHcodeErase()) {
			// 此段訂正點調原先程式,改為比照L3210訂正?? 2023/3/8修改,待確認
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

//			loanCom.checkEraseCustNoTxSeqNo(iCustNo, titaVo);// 檢查到同戶帳務交易需由最近一筆交易開始訂正
//			loanCom.setFacmBorTxHcodeByTx(iCustNo, titaVo);
//			if ("06".equals(iTempItemCode)) {
//				for (int i = 1; i <= 50; i++) {
//					if (titaVo.get("RpCode" + i) == null || parse.stringToInteger(titaVo.getParam("RpCode" + i)) == 0)
//						break;
//					iRpCustNo = parse.stringToInteger(titaVo.getParam("RpCustNo" + i));
//					if (iRpCustNo != iCustNo) {
//						loanCom.checkEraseCustNoTxSeqNo(iRpCustNo, titaVo);// 檢查到同戶帳務交易需由最近一筆交易開始訂正
//						loanCom.setFacmBorTxHcodeByTx(iRpCustNo, titaVo);
//					}
//				}
//			}
		}

		// 暫收款交易新增帳務及更新放款交易內容檔
		if (titaVo.isHcodeNormal()) {
			acRepayCom.settleTempRun(this.lLoanBorTx, this.baTxList, this.lAcDetail, titaVo);
		}

		this.addList(this.totaVo);
		return this.sendList();
	}

	private void loadUnPaidRoutine() throws LogicException {
		this.info("settingUnPaid ... ");
		this.info("   iTempReasonCode = " + iTempReasonCode);
		this.info("   iFacmNo         = " + iFacmNo);

		// 是否額度可抵繳
		// 按指定額度：00-全費用類別
		// 1.iFacmNo >0 該額度為指定額度則只有該額度可抵繳,如該額度為非指定額度則全部非指定額度可抵繳
		// 2.iFacmNo =0 全部非指定額度可抵繳
		// 暫收可抵繳轉帳；96-轉帳
//				1.iFacmNo >0 限該額度可抵繳
		try {
			this.baTxList = baTxCom.settingUnPaid(titaVo.getEntDyI(), iCustNo, iFacmNo, 0,
					"06".equals(iTempItemCode) ? 96 : 0, iTempAmt, titaVo);
		} catch (LogicException e) {
			throw new LogicException(titaVo, "E0015", "查詢費用 " + e.getMessage()); // 檢查錯誤
		}

		// 出帳金額需重算
		if (this.baTxList != null) {
			for (BaTxVo ba : this.baTxList) {
				// 出帳金額歸零
				ba.setAcctAmt(BigDecimal.ZERO); // 出帳金額
				// 暫收款：可出帳餘額(不吻合帳戶別清零)、費用：未出帳餘額
				ba.setAcAmt(BigDecimal.ZERO);
				if ((iTempReasonCode == 1 && ba.getDataKind() == 3)
						|| (iTempReasonCode == 2 && ba.getAcctCode().equals("TAV")
								&& iCustNo == this.txBuffer.getSystemParas().getNegDeptCustNo())
						|| (iTempReasonCode == 2 && ba.getAcctCode().substring(0, 2).equals("T1"))
						|| (iTempReasonCode == 4 && ba.getAcctCode().equals("TAM"))) {
					ba.setAcAmt(ba.getUnPaidAmt());
				}
				wkCustTempBal = wkCustTempBal.add(ba.getAcAmt());
				this.info("ba= " + ba.toString());
			}
		}
	}

	private void TempAcDetailRoutine() throws LogicException {
		this.info("TempAcDetailRoutine ... ");
		this.info("   iTempReasonCode = " + iTempReasonCode);
		this.info("   iFacmNo         = " + iFacmNo);
		if (this.baTxList != null) {
			// 先抵相同費用額度
			for (BaTxVo fa : this.baTxList) {
				if (fa.getRepayType() != 0 && fa.getAcAmt().compareTo(BigDecimal.ZERO) > 0) {
					for (BaTxVo ta : this.baTxList) {
						if (ta.getDataKind() == 3 && ta.getFacmNo() == fa.getFacmNo()
								&& ta.getAcAmt().compareTo(BigDecimal.ZERO) > 0) {
							BigDecimal acctAmt = BigDecimal.ZERO;
							if (ta.getAcAmt().compareTo(fa.getAcAmt()) > 0) {
								acctAmt = fa.getAcAmt();
							} else {
								acctAmt = ta.getAcAmt();
							}
							wkTempBal = wkTempBal.subtract(acctAmt);
							ta.setAcctAmt(ta.getAcctAmt().add(acctAmt));
							ta.setAcAmt(ta.getAcAmt().subtract(acctAmt));
							fa.setAcAmt(fa.getAcAmt().subtract(acctAmt));
							this.info("fa= " + fa.toString());
							this.info("ta= " + ta.toString());
						}
					}
				}
			}
			this.info("   wkTempBal         = " + wkTempBal);
			// 再按順序抵用
			for (BaTxVo ta : this.baTxList) {
				if (ta.getDataKind() == 3 || ta.getDataKind() == 9)
					if (ta.getAcAmt().compareTo(BigDecimal.ZERO) > 0 && wkTempBal.compareTo(BigDecimal.ZERO) > 0) {
						BigDecimal acctAmt = BigDecimal.ZERO;
						if (wkTempBal.compareTo(ta.getAcAmt()) >= 0) {
							acctAmt = ta.getAcAmt();
						} else {
							acctAmt = wkTempBal;
						}
						wkTempBal = wkTempBal.subtract(acctAmt);
						ta.setAcctAmt(ta.getAcctAmt().add(acctAmt));
						ta.setAcAmt(ta.getAcAmt().subtract(acctAmt));
					}
			}

		}

		if (wkTempBal.compareTo(BigDecimal.ZERO) > 0) {
			this.info("   E3060 wkTempBal      = " + wkTempBal);
			this.info("         wkCustTempBal  = " + wkCustTempBal);
			throw new LogicException(titaVo, "E3060", "目前客戶之暫收款 = " + wkCustTempBal); // 退還金額大於目前客戶之暫收款
		}

		// 借：暫收款
		for (BaTxVo ba : this.baTxList) {
			if (ba.getDataKind() == 3 || ba.getDataKind() == 9) {
				if (ba.getAcctAmt().compareTo(BigDecimal.ZERO) > 0) {
					AcDetail acDetail = new AcDetail();
					acDetail.setDbCr("D");
					acDetail.setTxAmt(ba.getAcctAmt());
					acDetail.setSumNo("06".equals(iTempItemCode) ? "092" : "090"); // 暫收轉帳 / 暫收抵繳
					acDetail.setRvNo(ba.getRvNo());
					acDetail.setAcctCode(ba.getAcctCode());
					wkAcctCode = ba.getAcctCode();
					acDetail.setCurrencyCode(iCurrencyCode);
					acDetail.setCustNo(iCustNo);
					acDetail.setFacmNo(ba.getFacmNo());
					lAcDetail.add(acDetail);
				}
			}
		}

	}

	// 貸: 作業項目對應的應繳費用，業務科目、金額需相同
	private void setUnPaidFee(String acctCode) throws LogicException {
		this.info("acctCode = " + acctCode);

		if (this.baTxList == null || this.baTxList.size() == 0) {
			throw new LogicException(titaVo, "E0019", "無該項目未銷費用"); // 查詢資料不存在
		}

		BigDecimal totalAmt = BigDecimal.ZERO;
		// 單筆相同金額，銷帳編號相同
		boolean isFind = false;
		for (BaTxVo ba : this.baTxList) {
			if (acctCode.equals(ba.getAcctCode()) || ("F09".equals(acctCode) && "TMI".equals(ba.getAcctCode()))) {
				if (iTempAmt.compareTo(ba.getUnPaidAmt()) == 0) {
					if ((iRemoveNo.isEmpty()) || (iRemoveNo.length() > 0 && ba.getRvNo().length() >= iRemoveNo.length()
							&& ba.getRvNo().substring(0, iRemoveNo.length()).equals(iRemoveNo))) {
						ba.setAcctAmt(ba.getUnPaidAmt());
						ba.setAcAmt(ba.getUnPaidAmt());
						isFind = true;
						break;
					}
				}
			}
		}
		// 多筆總金額金額，銷帳編號相同
		if (!isFind) {
			for (BaTxVo ba : this.baTxList) {
				if (acctCode.equals(ba.getAcctCode()) || ("F09".equals(acctCode) && "TMI".equals(ba.getAcctCode()))) {
					if ((iRemoveNo.isEmpty()) || (iRemoveNo.length() > 0 && ba.getRvNo().length() >= iRemoveNo.length()
							&& ba.getRvNo().substring(0, iRemoveNo.length()).equals(iRemoveNo))) {
						totalAmt = totalAmt.add(ba.getUnPaidAmt());

					}
				}
			}
			if (iTempAmt.compareTo(totalAmt) == 0) {
				for (BaTxVo ba : this.baTxList) {
					if (acctCode.equals(ba.getAcctCode())
							|| ("F09".equals(acctCode) && "TMI".equals(ba.getAcctCode()))) {
						if ((iRemoveNo.isEmpty())
								|| (iRemoveNo.length() > 0 && ba.getRvNo().length() >= iRemoveNo.length()
										&& ba.getRvNo().substring(0, iRemoveNo.length()).equals(iRemoveNo))) {

							ba.setAcctAmt(ba.getUnPaidAmt());
							ba.setAcAmt(ba.getUnPaidAmt());
							isFind = true;
						}
					}
				}
			}
		}
		if (!isFind) {
			throw new LogicException(titaVo, "E0019", "無金額相同之未銷費用，請使用 L6907 未銷帳餘額明細查詢 "); // 查詢資料不存在
		}
	}

	private void settingUnPaid06() throws LogicException {

		// 暫收貸
		switch (iRpCode) {
		case 92: // 放款暫收款
			// 96 : 單一額度轉帳
			if (iRpCustNo != this.txBuffer.getSystemParas().getLoanDeptCustNo()) {
				FacMain tFacMain = facMainService.findById(new FacMainId(iRpCustNo, iRpFacmNo), titaVo);
				if (tFacMain == null) {
					throw new LogicException(titaVo, "E0015", "額度不存在 戶號 = " + iRpCustNo + "-" + iRpFacmNo); // 檢查錯誤
				}
			}
			acDetail = new AcDetail();
			acDetail.setDbCr("C");
			acDetail.setAcctCode("TAV");
			acDetail.setSumNo("092");
			acDetail.setTxAmt(iRpAmt);
			acDetail.setCustNo(iRpCustNo);
			acDetail.setFacmNo(iRpFacmNo);
			lAcDetail.add(acDetail);
			break;
		case 94: // 轉債協暫收款
			acDetail = new AcDetail();
			acDetail.setDbCr("C");
			acDetail.setAcctCode(acNegCom.getAcctCode(iRpCustNo, titaVo));
			acDetail.setCustNo(iRpCustNo);
			acDetail.setSumNo("094");
			acDetail.setTxAmt(iRpAmt);
			// 由債協專戶入一般債權TAV
			if (iCustNo == this.txBuffer.getSystemParas().getNegDeptCustNo()
					&& iRpCustNo != this.txBuffer.getSystemParas().getLoanDeptCustNo()) {
				if (acNegCom.getAcctCode(iRpCustNo, titaVo).equals("TAV")) {
					TempVo tempVo = new TempVo();
					tempVo = acNegCom.getReturnAcctCode(iRpCustNo, titaVo);
					acDetail.setCustNo(parse.stringToInteger(tempVo.getParam("CustNo")));
					acDetail.setFacmNo(parse.stringToInteger(tempVo.getParam("FacmNo")));
					acDetail.setAcctCode("TAV");
					acDetail.setSumNo("092");
					acDetail.setSlipNote("一般債權");
					acDetail.setJsonFields(tempVo.getJsonString());// 記錄實際借款人戶號對應之匯款人戶號
				}
			}
			lAcDetail.add(acDetail);
			break;
		case 95: // 轉債協退還款
			throw new LogicException(titaVo, "E0015", "需使用放款暫收款帳戶 "); // 檢查錯誤
		}
		wkAcctCode = acDetail.getAcctCode();
//		addLoanBorTx06Routine(iRpCustNo, iRpFacmNo, iRpAmt);  ?? 2023/3/8修改,待確認
		addLoanBorTx06Routine(acDetail.getCustNo(), acDetail.getFacmNo(), iRpAmt);
	}

	// 借:暫收可抵繳 貸:利息收入(3200億專案息)
	private void subsidyInterest() throws LogicException {
		wkAcctCode = "F16";
		acDetail = new AcDetail();
		acDetail.setDbCr("C");
		acDetail.setAcctCode(wkAcctCode);
		acDetail.setCurrencyCode(iCurrencyCode);
		acDetail.setTxAmt(iTempAmt);
		acDetail.setCustNo(iCustNo);
		acDetail.setAcSubBookCode(iAcSubBookCode); // 區隔帳冊
		acDetail.setAcBookFlag(3); // 帳冊別記號 (3: 指定帳冊)
		lAcDetail.add(acDetail);
	}

	// 貸: 債權協商對應科目
	private void NegAcDetailRoutine() throws LogicException {
		wkAcctCode = acNegCom.getAcctCode(iCustNo, titaVo);
		AcDetail acDetail = new AcDetail();
		acDetail.setDbCr("C");
		acDetail.setAcctCode(wkAcctCode);
		acDetail.setCurrencyCode(iCurrencyCode);
		acDetail.setTxAmt(iTempAmt);
		acDetail.setCustNo(iCustNo);
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
			tTempVo.putParam("TempReasonCode", iTempReasonCode);
			tTempVo.putParam("TempItemCode", iTempItemCode);
			tTempVo.putParam("RemoveNo", iRemoveNo);
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

	// 新增放款交易內容檔
	private void addLoanBorTxRoutine() throws LogicException {
		this.info("addLoanBorTxRoutine ... ");
		tLoanBorTx = new LoanBorTx();
		tLoanBorTxId = new LoanBorTxId();
		loanCom.setFacmBorTx(tLoanBorTx, tLoanBorTxId, iCustNo, iFacmNo, titaVo);

		tLoanBorTx.setEntryDate(titaVo.getEntDyI());
		tLoanBorTx.setRepayCode(90);
// Temp2ReasonCode
//		1	放款暫收款
//		2	債協暫收款
//		3	債協退還款
//		4	AML暫收款
//      5	聯貸費攤提暫收款
// Temp2ItemCode
//		06	轉帳
//		07	沖執行費
//		08	收回呆帳
//		09	沖火險費
//		10	沖帳管費/手續費
//		12	企金件
//		13	沖什項收入
//		14	NPL-銷項稅額
//		15	921貸款戶
//		16	3200億專案
//		17	3200億-利變
//		18	沖備抵呆帳
//		19	轉債協暫收款
//		20	轉應付代收
//		21	88風災
//		22	88風災-保費
//		23	3200億傳統A
//		24	沖催收法務費
//		25	沖催收火險費
//		27	沖聯貸費用
//		29	貸後契變手續費
//		30	呆帳戶法務費墊付

		if ("06".equals(iTempItemCode)) {
			// 3230 暫收款轉出
			// 3231 債協暫收款轉出
			// 3232 債協退還款轉出
			// 3233 AML暫收款轉出
			// 3234 聯貸費攤提暫收款轉出
			switch (iTempReasonCode) {
			case 2:
				tLoanBorTx.setTxDescCode("3231");
				break;
			case 3:
				tLoanBorTx.setTxDescCode("3232");
				break;
			case 4:
				tLoanBorTx.setTxDescCode("3233");
				break;
			case 5:
				tLoanBorTx.setTxDescCode("3234");
				break;
			default:
				tLoanBorTx.setTxDescCode("3230");
				break;
			}
		} else {
			tLoanBorTx.setTxDescCode("Fee");
		}
		tLoanBorTx.setDisplayflag("A"); // A:帳務
		tLoanBorTx.setTempAmt(iTempAmt);
		tLoanBorTx.setOverflow(BigDecimal.ZERO);
		tLoanBorTx.setAcctCode(wkAcctCode);
		tTempVo.clear();
		tTempVo.putParam("TempReasonCode", iTempReasonCode);
		tTempVo.putParam("TempItemCode", iTempItemCode);
		tTempVo.putParam("RemoveNo", iRemoveNo);
		tTempVo.putParam("Note", iNote);
		tLoanBorTx.setOtherFields(tTempVo.getJsonString());
		this.lLoanBorTx.add(tLoanBorTx);
	}

	// 新增放款交易內容檔
	private void addLoanBorTx06Routine(int iRpCustNo, int iRpFacmNo, BigDecimal iRpAmt) throws LogicException {
		this.info("addLoanBorTx06Routine ... ");
		tLoanBorTx = new LoanBorTx();
		tLoanBorTxId = new LoanBorTxId();
		loanCom.setFacmBorTx(tLoanBorTx, tLoanBorTxId, iRpCustNo, iRpFacmNo, titaVo);
//      092:暫收轉帳     (戶號+額度)  TAV 暫收款－可抵繳
//      094:轉債協暫收款 (戶號)       T1x 債協暫收款      
//      095:轉債協退還款 (戶號)       T2x 債協退還款  
		// 3235 轉入放款暫收款
		// 3236 轉入債協暫收款
		// 3237 轉入債協退還款
		switch (iRpCode) {
		case 94:
			tLoanBorTx.setTxDescCode("3236");
			break;
		case 95:
			tLoanBorTx.setTxDescCode("3237");
			break;
		default:
			tLoanBorTx.setTxDescCode("3235");
			break;
		}
		tLoanBorTx.setEntryDate(titaVo.getEntDyI());
		tLoanBorTx.setRepayCode(iRpCode);
		tLoanBorTx.setDisplayflag("A"); // A:帳務
		tLoanBorTx.setTempAmt(BigDecimal.ZERO);
		tLoanBorTx.setOverflow(iRpAmt);
		tLoanBorTx.setAcctCode(wkAcctCode);

		tTempVo.clear();
		// 其他欄位
		tTempVo.clear();
		tTempVo.putParam("TempItemCode", iTempItemCode);
		// 新增摘要
		tTempVo.putParam("Note", iNote);
		tLoanBorTx.setOtherFields(tTempVo.getJsonString());
		this.lLoanBorTx.add(tLoanBorTx);
	}

//	1.收款人必須存在客戶主檔
//	2.專戶可轉專戶 其他戶號可轉專戶 專戶可轉其他戶號
//	3.同戶號可轉其他額度
	private void Checktransfer() throws LogicException {
		// 放款暫收款轉帳時需主管授權
		if (iTempReasonCode == 1) {
			if (titaVo.getEmpNos().trim().isEmpty()) {
				sendRsp.addvReason(this.txBuffer, titaVo, "0004", "放款暫收款轉帳");
			}
		}

		boolean wkCustSpecial = false;
		for (int i = 1; i <= 50; i++) {

			// 檢查轉入暫收款
			if (titaVo.get("RpCode" + i) == null || parse.stringToInteger(titaVo.getParam("RpCode" + i)) == 0
					|| parse.stringToInteger(titaVo.getParam("RpCode" + i)) != 92) {
				break;
			}
			int iRpCustNo = parse.stringToInteger(titaVo.getParam("RpCustNo" + i));

			// 檢查轉入戶號是否為專戶
			if (iRpCustNo == this.txBuffer.getSystemParas().getLoanDeptCustNo()
					|| iRpCustNo == this.txBuffer.getSystemParas().getNegDeptCustNo()) {
				continue;
			}
			// 0610940戶號(放款部專戶)
			// 0601776戶號(前置協商收款專戶)
			// 檢查轉出是否為專戶
			if (iCustNo == this.txBuffer.getSystemParas().getLoanDeptCustNo()
					|| iCustNo == this.txBuffer.getSystemParas().getNegDeptCustNo()) {
				wkCustSpecial = true;
			}

			// 必須輸入轉入戶號
			if (parse.stringToInteger(titaVo.getParam("RpCustNo" + i)) == 0) {
				throw new LogicException(titaVo, "E1002", "必須輸入轉入戶號"); // E1002,戶號不得為0
			}
			CustMain tCustMain = new CustMain();
			tCustMain = custMainService.custNoFirst(parse.stringToInteger(titaVo.getParam("RpCustNo" + i)),
					parse.stringToInteger(titaVo.getParam("RpCustNo" + i)), titaVo);
			// 轉入戶號必須存在客戶主檔
			if (tCustMain == null) {
				throw new LogicException(titaVo, "E1004", "轉入戶號 : " + iRpCustNo); // E1004 此戶號不存在客戶主檔
			}
//			專戶可轉專戶 其他戶號可轉專戶 專戶可轉其他戶號
//			不同戶號時檢查是否為專戶 不為專戶顯示錯誤訊息
			if (iCustNo != iRpCustNo) {
				if (!wkCustSpecial) {
					throw new LogicException(titaVo, "E6002", "不可轉至不同戶號"); // E6002 收付欄檢核有誤
				}
			}
			if (iFacmNo > 0 && iRpFacmNo > 0 && iFacmNo == iRpFacmNo) {
				throw new LogicException(titaVo, "E6002", "不可轉至同額度"); // E6002 收付欄檢核有誤
			}
		}
	}

}