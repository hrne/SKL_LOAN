package com.st1.itx.trade.L2;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;

import com.st1.itx.Exception.DBException;
import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.TempVo;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.dataVO.TotaVo;
import com.st1.itx.db.domain.ClFac;
import com.st1.itx.db.domain.CustMain;
import com.st1.itx.db.domain.FacCaseAppl;
import com.st1.itx.db.domain.FacMain;
import com.st1.itx.db.domain.FacMainId;
import com.st1.itx.db.domain.FacProd;
import com.st1.itx.db.domain.FacProdStepRate;
import com.st1.itx.db.domain.FacProdStepRateId;
import com.st1.itx.db.domain.LoanBorTx;
import com.st1.itx.db.domain.LoanBorTxId;
import com.st1.itx.db.service.CdGseqService;
import com.st1.itx.db.service.ClFacService;
import com.st1.itx.db.service.CustMainService;
import com.st1.itx.db.service.FacCaseApplService;
import com.st1.itx.db.service.FacMainService;
import com.st1.itx.db.service.FacProdBreachService;
import com.st1.itx.db.service.FacProdService;
import com.st1.itx.db.service.FacProdStepRateService;
import com.st1.itx.db.service.LoanBorMainService;
import com.st1.itx.db.service.LoanBorTxService;
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.common.BankAuthActCom;
import com.st1.itx.util.common.LoanCom;
import com.st1.itx.util.data.DataLog;
import com.st1.itx.util.date.DateUtil;
import com.st1.itx.util.format.FormatUtil;
import com.st1.itx.util.parse.Parse;

/*
 * L2154 額度資料維護
 * a.修改:
 * a1.若已撥款則額度之核准額度須>=已貸金額
 * a2.修改資料須刷主管卡之欄位:基本利率代碼,核准利率,利率調整週期,攤還額異動碼,動支期限,還本週期,循環動用期限,代繳所得稅,攤還方式,寬限期到期日,繳款方式,繳息週期,客戶別,核准額度
 * b.刪除:額度尚未撥款前才可刪除額度資料
 */

/**
 * L2154 額度資料維護
 * 
 * @author iza
 * @version 1.0.0
 */
@Service("L2154")
@Scope("prototype")
public class L2154 extends TradeBuffer {

	/* DB服務注入 */
	@Autowired
	public FacProdService facProdService;
	@Autowired
	public FacProdBreachService facProdBreachService;
	@Autowired
	public FacProdStepRateService facProdStepRateService;
	@Autowired
	public FacMainService facMainService;
	@Autowired
	public FacCaseApplService facCaseApplService;
	@Autowired
	public CustMainService custMainService;
	@Autowired
	public LoanBorMainService loanBorMainService;
	@Autowired
	public CdGseqService cdGseqService;
	@Autowired
	public LoanBorTxService loanBorTxService;;
	@Autowired
	public ClFacService sClFacService;

	@Autowired
	Parse parse;
	@Autowired
	LoanCom loanCom;
	@Autowired
	DateUtil dDateUtil;
	@Autowired
	DataLog datalog;
	@Autowired
	BankAuthActCom bankAuthActCom;

	// input area
	private TitaVo titaVo = new TitaVo();
	private int iFuncCode;
	private int iCustNo;
	private int iFacmNo;
	private String iProdNo;

	// work area
	private String sProdNo = "";
	private TempVo tTempVo = new TempVo();
	private FacProd tFacProd;
	private FacMain tFacMain;
	private FacMain beforeFacMain;
	private TitaVo txtitaVo;

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active L2154 ");
		this.info("   isActfgEntry   = " + titaVo.isActfgEntry());
		this.info("   isActfgSuprele = " + titaVo.isActfgSuprele());
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

		bankAuthActCom.setTxBuffer(this.txBuffer);
		loanCom.setTxBuffer(this.txBuffer);

		// 取得輸入資料
		iFuncCode = this.parse.stringToInteger(titaVo.getParam("FuncCode"));
		iCustNo = this.parse.stringToInteger(titaVo.getParam("CustNo"));
		iFacmNo = this.parse.stringToInteger(titaVo.getParam("FacmNo"));
		iProdNo = titaVo.getParam("ProdNo");
		// 檢查輸入資料
		if (!(iFuncCode == 2 || iFuncCode == 4 || iFuncCode == 5)) {
			throw new LogicException(titaVo, "E2004", "功能"); // 功能選擇錯誤
		}
		if (iFuncCode == 5) {
			this.addList(this.totaVo);
			return this.sendList();
		}

		// 查詢商品參數檔
		tFacProd = facProdService.findById(iProdNo, titaVo);
		if (tFacProd == null) {
			throw new LogicException(titaVo, "E2003", "商品參數檔  商品代碼=" + iProdNo); // 查無資料
		}

		// 登錄
		if (titaVo.isActfgEntry() && titaVo.isHcodeNormal()) {
			EntryNormalRoutine();
		}
		// 登錄 修正
		if (titaVo.isActfgEntry() && titaVo.isHcodeModify()) {
			if (iFuncCode == 4) { // 刪除
				throw new LogicException(titaVo, "E0020", "額度主檔 戶號=" + iCustNo + " 額度編號=" + iFacmNo); // 已刪除資料，不可做修正
			}
			EntryNormalRoutine();
		}
		// 登錄 訂正
		if (titaVo.isActfgEntry() && titaVo.isHcodeErase()) {
			EntryEraseRoutine();
		}
		// 放行
		if (titaVo.isActfgSuprele()) {
			if (titaVo.isHcodeErase()) { // 放行後不可訂正
				throw new LogicException(titaVo, "E0010", "放行後不可訂正"); // 功能選擇錯誤
			}

			ReleaseRoutine();

		}
		// 一段式或兩段式放行 更新階梯式利率
		if (titaVo.isActfgRelease()) {
			UpdateFacProdStepRateRoutine();
		}
		// 銀扣授權帳號檔
		bankAuthActRoutine();

		this.addList(this.totaVo);
		return this.sendList();
	}

	// 登錄
	private void EntryNormalRoutine() throws LogicException {
		this.info("EntryNormalRoutine ... ");

		tFacMain = facMainService.holdById(new FacMainId(iCustNo, iFacmNo), titaVo);
		if (tFacMain == null) {
			throw new LogicException(titaVo, "E0003", "額度主檔 戶號=" + iCustNo + " 額度編號=" + iFacmNo); // 修改資料不存在
		}

		// 新增交易暫存檔
		addLoanBorTxRoutine();

		// 更新額度主檔
		if (tFacMain.getActFg() == 1 && titaVo.isHcodeNormal()) {
			throw new LogicException(titaVo, "E0021",
					"額度檔 戶號 = " + tFacMain.getCustNo() + " 額度編號 =  " + tFacMain.getFacmNo()); // 該筆資料待放行中
		}
		switch (iFuncCode) {
		case 2: // 修改
			UpdateFacMainRoutine();
			break;
		case 4: // 刪除, 額度尚未撥款前才可刪除額度資料, 放行時才真正刪除檔案

			if (tFacMain.getLastBormNo() > 0) {
				throw new LogicException(titaVo, "E2071", "撥款序號=" + tFacMain.getLastBormNo()); // 額度已撥款後，禁止刪除
			}
			if (tFacMain.getLastBormRvNo() > 900) {
				throw new LogicException(titaVo, "E2071", "預約撥款序號=" + tFacMain.getLastBormRvNo()); // 額度已撥款後，禁止刪除預約撥款
			}
			Slice<ClFac> slClFac = sClFacService.approveNoEq(tFacMain.getApplNo(), 0, Integer.MAX_VALUE, titaVo);
			List<ClFac> lClFac = slClFac == null ? null : slClFac.getContent();
//			該額度與擔保品關聯不可刪除,請先解除關聯
			if (lClFac != null) {
				throw new LogicException(titaVo, "E2073", "核准編號 =" + tFacMain.getApplNo()); // 該額度與擔保品關聯，不可刪除
			}
			tFacMain.setActFg(titaVo.getActFgI());
			tFacMain.setLastAcctDate(titaVo.getEntDyI());
			tFacMain.setLastKinbr(titaVo.getKinbr());
			tFacMain.setLastTlrNo(titaVo.getTlrNo());
			tFacMain.setLastTxtNo(titaVo.getTxtNo());
			try {
				facMainService.update(tFacMain, titaVo);
			} catch (DBException e) {
				throw new LogicException(titaVo, "E2010", "額度主檔"); // 更新資料時，發生錯誤
			}
			break;
		}

	}

	// 登錄 訂正
	private void EntryEraseRoutine() throws LogicException {
		this.info("EntryEraseRoutine ... ");
		LoanBorTx tLoanBorTx = loanBorTxService.custNoTxtNoFirst(iCustNo, iFacmNo, 0, titaVo.getOrgEntdyI() + 19110000,
				titaVo.getOrgTlr(), titaVo.getOrgTno(), titaVo);
		if (tLoanBorTx == null) {
			throw new LogicException(titaVo, "E0006", "放款交易內容檔"); // 鎖定資料時，發生錯誤
		}
		tTempVo = tTempVo.getVo((tLoanBorTx.getOtherFields()));
		tLoanBorTx = loanBorTxService.holdById(tLoanBorTx, titaVo);
		try {
			loanBorTxService.delete(tLoanBorTx, titaVo);
		} catch (DBException e) {
			throw new LogicException(titaVo, "E0008", "放款交易內容檔"); // 刪除資料時，發生錯誤
		}
		switch (iFuncCode) {
		case 2: // 修改
				// 還原額度檔
			RestoredFacMainRoutine();
//					RestoredFacProdBreachRoutine();
			break;
		case 4: // 刪除 還原交序號
			tFacMain = facMainService.holdById(new FacMainId(iCustNo, iFacmNo), titaVo);
			if (tFacMain == null) {
				throw new LogicException(titaVo, "E0006", "額度主檔 戶號 = " + iCustNo + " 額度編號 = " + iFacmNo); // 鎖定資料時，發生錯誤
			}
			tFacMain.setActFg(this.parse.stringToInteger(tTempVo.getParam("ActFg")));
			tFacMain.setLastAcctDate(this.parse.stringToInteger(tTempVo.getParam("LastAcctDate")));
			tFacMain.setLastKinbr(tTempVo.getParam("LastKinbr"));
			tFacMain.setLastTlrNo(tTempVo.getParam("LastTlrNo"));
			tFacMain.setLastTxtNo(tTempVo.getParam("LastTxtNo"));
			try {
				tFacMain = facMainService.update(tFacMain, titaVo);
			} catch (DBException e) {
				throw new LogicException(titaVo, "E0007",
						"額度主檔 戶號 = " + iCustNo + " 額度編號 = " + iFacmNo + " " + e.getErrorMsg()); // 更新資料時，發生錯誤
			}
			break;
		}

	}

	// 放行
	private void ReleaseRoutine() throws LogicException {
		this.info("ReleaseRoutine ... ");
		tFacMain = facMainService.holdById(new FacMainId(iCustNo, iFacmNo), titaVo);
		if (tFacMain == null) {
			throw new LogicException(titaVo, "E0006", "額度主檔"); // 鎖定資料時，發生錯誤
		}
		switch (iFuncCode) {
		case 2: // 修改, 更新額度主檔
			// 更新額度主檔
			tFacMain.setActFg(titaVo.getActFgI());
			try {
				tFacMain = facMainService.update(tFacMain, titaVo);
			} catch (DBException e) {
				throw new LogicException(titaVo, "E0008",
						"額度主檔 戶號 = " + iCustNo + "額度編號 = " + iFacmNo + e.getErrorMsg()); // 新增資料時，發生錯誤
			}
			break;
		case 4: // 刪除, 刪除額度資料及清償金類型
			// 刪除時更新案件申請檔
			UpdateAppl(tFacMain.getApplNo());
			// 刪除時檢查為該戶號最後一筆額度 客戶主檔該額度-1
			CustMain tCustMain = null;
			tCustMain = custMainService.custNoFirst(tFacMain.getCustNo(), tFacMain.getCustNo(), titaVo);
			if (tCustMain == null) {
				throw new LogicException(titaVo, "E2003", "客戶資料主檔 = " + tFacMain.getCustNo()); // 查無資料
			}
			String wkCustUKey = tCustMain.getCustUKey().trim();
			tCustMain = custMainService.holdById(wkCustUKey, titaVo);
			if (tCustMain == null) {
				throw new LogicException(titaVo, "E2011", "客戶資料主檔"); // 鎖定資料時，發生錯誤
			}
			if (tFacMain.getFacmNo() == tCustMain.getLastFacmNo()) {
				tCustMain.setLastFacmNo(tCustMain.getLastFacmNo() - 1);
				try {
					custMainService.update(tCustMain, titaVo);
				} catch (DBException e) {
					throw new LogicException(titaVo, "E2010", "客戶資料主檔"); // 更新資料時，發生錯誤
				}
			}
			try {
				facMainService.delete(tFacMain, titaVo);
			} catch (DBException e) {
				throw new LogicException(titaVo, "E2008", "額度主檔"); // 刪除資料時，發生錯誤
			}
			// 刪除階梯式利率
			DeleteFacProdStepRateRoutine();
			break;
		}
	}

	// 刪除時更新案件申請檔
	private void UpdateAppl(int applNo) throws LogicException {
		this.info("UpdateAppl ...");
		FacCaseAppl tfacCaseAppl = facCaseApplService.holdById(applNo, titaVo);
		tfacCaseAppl.setProcessCode("0");
		try {
			facCaseApplService.update(tfacCaseAppl, titaVo);
		} catch (DBException e) {
			throw new LogicException(titaVo, "E0007", "案件申請檔"); // 更新資料時，發生錯誤
		}
	}

	// 新增放款交易內容檔
	private void addLoanBorTxRoutine() throws LogicException {
		this.info("AddTxTempRoutine ...");
		LoanBorTx tLoanBorTx = new LoanBorTx();
		LoanBorTxId tLoanBorTxId = new LoanBorTxId();
		// 修正時刪除放款交易內容檔
		if (titaVo.isHcodeModify()) {
			tLoanBorTx = loanBorTxService.custNoTxtNoFirst(iCustNo, iFacmNo, 0, titaVo.getOrgEntdyI() + 19110000,
					titaVo.getOrgTlr(), titaVo.getOrgTno(), titaVo);
			if (tLoanBorTx == null) {
				throw new LogicException(titaVo, "E0006", "放款交易內容檔"); // 鎖定資料時，發生錯誤
			}
			// 登錄時的主檔保留資料
			tTempVo = tTempVo.getVo(tLoanBorTx.getOtherFields());
			tLoanBorTx = loanBorTxService.holdById(tLoanBorTx, titaVo);
			try {
				loanBorTxService.delete(tLoanBorTx, titaVo);
			} catch (DBException e) {
				throw new LogicException(titaVo, "E0008", "放款交易內容檔"); // 刪除資料時，發生錯誤
			}
		}
		// 新增放款交易內容檔
		loanCom.setFacmBorTx(tLoanBorTx, tLoanBorTxId, iCustNo, iFacmNo, titaVo);
		tLoanBorTx.setDisplayflag("N");
		// 登錄時保留主檔資料
		if (titaVo.isHcodeNormal()) {
			setTempVo();
		}
		tLoanBorTx.setOtherFields(tTempVo.getJsonString());
		try {
			loanBorTxService.insert(tLoanBorTx, titaVo);
		} catch (DBException e) {
			throw new LogicException(titaVo, "E0005", "放款交易內容檔 Key = " + tLoanBorTxId); // 新增資料時，發生錯誤
		}
	}

	private void setTempVo() throws LogicException {
		tTempVo.clear();
		tTempVo.putParam("LastBormNo", tFacMain.getLastBormNo());
		tTempVo.putParam("LastBormRvNo", tFacMain.getLastBormRvNo());
		tTempVo.putParam("ApplNo", tFacMain.getApplNo());
		tTempVo.putParam("AnnualIncr", tFacMain.getAnnualIncr());
		tTempVo.putParam("EmailIncr", tFacMain.getEmailIncr());
		tTempVo.putParam("GraceIncr", tFacMain.getGraceIncr());
		tTempVo.putParam("CurrencyCode", tFacMain.getCurrencyCode());
		tTempVo.putParam("UtilAmt", tFacMain.getUtilAmt());
		tTempVo.putParam("UtilBal", tFacMain.getUtilBal());
		tTempVo.putParam("AcctCode", tFacMain.getAcctCode());
		tTempVo.putParam("FirstDrawdownDate", tFacMain.getFirstDrawdownDate());
		tTempVo.putParam("MaturityDate", tFacMain.getMaturityDate());
		tTempVo.putParam("CreditScore", tFacMain.getCreditScore());
		tTempVo.putParam("GuaranteeDate", tFacMain.getGuaranteeDate());
		tTempVo.putParam("ContractNo", tFacMain.getContractNo());
		tTempVo.putParam("ColSetFlag", tFacMain.getColSetFlag());
		tTempVo.putParam("L9110Flag", tFacMain.getL9110Flag());
		// 以下為可維護項目
		tTempVo.putParam("ProdNo", tFacMain.getProdNo());
		tTempVo.putParam("CreditSysNo", tFacMain.getCreditSysNo());
		tTempVo.putParam("BaseRateCode", tFacMain.getBaseRateCode());
		tTempVo.putParam("RateIncr", tFacMain.getRateIncr());
		tTempVo.putParam("IndividualIncr", tFacMain.getIndividualIncr());
		tTempVo.putParam("ApproveRate", tFacMain.getApproveRate());
		tTempVo.putParam("RateCode", tFacMain.getRateCode());
		tTempVo.putParam("FirstRateAdjFreq", tFacMain.getFirstRateAdjFreq());
		tTempVo.putParam("RateAdjFreq", tFacMain.getRateAdjFreq());
		tTempVo.putParam("LineAmt", tFacMain.getLineAmt());
		tTempVo.putParam("AcctCode", tFacMain.getAcctCode());
		tTempVo.putParam("LoanTermYy", tFacMain.getLoanTermYy());
		tTempVo.putParam("LoanTermMm", tFacMain.getLoanTermMm());
		tTempVo.putParam("LoanTermDd", tFacMain.getLoanTermDd());
		tTempVo.putParam("AmortizedCode", tFacMain.getAmortizedCode());
		tTempVo.putParam("FreqBase", tFacMain.getFreqBase());
		tTempVo.putParam("PayIntFreq", tFacMain.getPayIntFreq());
		tTempVo.putParam("RepayFreq", tFacMain.getRepayFreq());
		tTempVo.putParam("UtilDeadline", tFacMain.getUtilDeadline());
		tTempVo.putParam("GracePeriod", tFacMain.getGracePeriod());
		tTempVo.putParam("AcctFee", tFacMain.getAcctFee());
		tTempVo.putParam("HandlingFee", tFacMain.getHandlingFee());
		tTempVo.putParam("GuaranteeDate", tFacMain.getGuaranteeDate());
		tTempVo.putParam("ExtraRepayCode", tFacMain.getExtraRepayCode());
		tTempVo.putParam("CustTypeCode", tFacMain.getCustTypeCode());
		tTempVo.putParam("RuleCode", tFacMain.getRuleCode());
		tTempVo.putParam("RecycleCode", tFacMain.getRecycleCode());
		tTempVo.putParam("RecycleDeadline", tFacMain.getRecycleDeadline());
		tTempVo.putParam("UsageCode", tFacMain.getUsageCode());
		tTempVo.putParam("DepartmentCode", tFacMain.getDepartmentCode());
		tTempVo.putParam("IncomeTaxFlag", tFacMain.getIncomeTaxFlag());
		tTempVo.putParam("CompensateFlag", tFacMain.getCompensateFlag());
		tTempVo.putParam("IrrevocableFlag", tFacMain.getIrrevocableFlag());
		tTempVo.putParam("RateAdjNoticeCode", tFacMain.getRateAdjNoticeCode());
		tTempVo.putParam("PieceCode", tFacMain.getPieceCode());
		tTempVo.putParam("ProdBreachFlag", tFacMain.getProdBreachFlag());

		tTempVo.putParam("BreachFlag", tFacMain.getBreachFlag());
		tTempVo.putParam("BreachCode", tFacMain.getBreachCode());
		tTempVo.putParam("BreachGetCode", tFacMain.getBreachGetCode());
		tTempVo.putParam("ProhibitMonth", tFacMain.getProhibitMonth());
		tTempVo.putParam("BreachPercent", tFacMain.getBreachPercent());
		tTempVo.putParam("BreachDecreaseMonth", tFacMain.getBreachDecreaseMonth());
		tTempVo.putParam("BreachDecrease", tFacMain.getBreachDecrease());
		tTempVo.putParam("BreachStartPercent", tFacMain.getBreachStartPercent());

		tTempVo.putParam("Breach", tFacMain.getBreachDescription());
		tTempVo.putParam("CreditScore", tFacMain.getCreditScore());
		tTempVo.putParam("RepayCode", tFacMain.getRepayCode());
		tTempVo.putParam("RepayBank", titaVo.getParam("OldRepayBank"));
		tTempVo.putParam("RepayAcctNo", titaVo.getParam("OldAcctNo"));
		tTempVo.putParam("PostCode", titaVo.getParam("OldPostCode"));
		tTempVo.putParam("RelationCode", titaVo.getParam("OldRelationCode"));
		tTempVo.putParam("RelationName", titaVo.getParam("OldRelationName"));
		tTempVo.putParam("RelationId", titaVo.getParam("OldRelationId"));
		tTempVo.putParam("RelationBirthday", titaVo.getParam("OldRelationBirthday"));
		tTempVo.putParam("RelationGender", titaVo.getParam("OldRelationGender"));
		tTempVo.putParam("Introducer", tFacMain.getIntroducer());
		tTempVo.putParam("District", tFacMain.getDistrict());
		tTempVo.putParam("FireOfficer", tFacMain.getFireOfficer());
		tTempVo.putParam("Estimate", tFacMain.getEstimate());
		tTempVo.putParam("CreditOfficer", tFacMain.getCreditOfficer());
		tTempVo.putParam("LoanOfficer", tFacMain.getLoanOfficer());
		tTempVo.putParam("BusinessOfficer", tFacMain.getBusinessOfficer());
		tTempVo.putParam("ApprovedLevel", tFacMain.getApprovedLevel());
		tTempVo.putParam("Supervisor", tFacMain.getSupervisor());
		tTempVo.putParam("InvestigateOfficer", tFacMain.getInvestigateOfficer());
		tTempVo.putParam("EstimateReview", tFacMain.getEstimateReview());
		tTempVo.putParam("Coorgnizer", tFacMain.getCoorgnizer());
		tTempVo.putParam("AdvanceCloseCode", tFacMain.getAdvanceCloseCode());
		tTempVo.putParam("ActFg", tFacMain.getActFg());
		tTempVo.putParam("LastAcctDate", tFacMain.getLastAcctDate());
		tTempVo.putParam("LastKinbr", tFacMain.getLastKinbr());
		tTempVo.putParam("LastTlrNo", tFacMain.getLastTlrNo());
		tTempVo.putParam("LastTxtNo", tFacMain.getLastTxtNo());
		tTempVo.putParam("AcDate", tFacMain.getAcDate());
	}

	private void UpdateFacMainRoutine() throws LogicException {
		this.info("UpdateFacMainRoutine ...");

		beforeFacMain = (FacMain) datalog.clone(tFacMain);
		tFacMain.setCreditSysNo(this.parse.stringToInteger(titaVo.getParam("CreditSysNo")));
		tFacMain.setProdNo(titaVo.getParam("ProdNo"));
		tFacMain.setBaseRateCode(titaVo.getParam("BaseRateCode"));

		if ("N".equals(tFacProd.getIncrFlag())) {
			tFacMain.setRateIncr(this.parse.stringToBigDecimal(titaVo.getParam("RateIncr")));
			tFacMain.setIndividualIncr(this.parse.stringToBigDecimal(titaVo.getParam("RateIncr")));
		} else {
			tFacMain.setRateIncr(this.parse.stringToBigDecimal(titaVo.getParam("RateIncr")));
			tFacMain.setIndividualIncr(new BigDecimal("0"));
		}
		tFacMain.setApproveRate(this.parse.stringToBigDecimal(titaVo.getParam("ApproveRate")));
		tFacMain.setRateCode(titaVo.getParam("RateCode"));
		tFacMain.setFirstRateAdjFreq(this.parse.stringToInteger(titaVo.getParam("FirstRateAdjFreq")));
		tFacMain.setRateAdjFreq(this.parse.stringToInteger(titaVo.getParam("RateAdjFreq")));
		tFacMain.setAcctCode(titaVo.getParam("AcctCode"));
		tFacMain.setLineAmt(this.parse.stringToBigDecimal(titaVo.getParam("TimApplAmt")));
		tFacMain.setLoanTermYy(this.parse.stringToInteger(titaVo.getParam("LoanTermYy")));
		tFacMain.setLoanTermMm(this.parse.stringToInteger(titaVo.getParam("LoanTermMm")));
		tFacMain.setLoanTermDd(this.parse.stringToInteger(titaVo.getParam("LoanTermDd")));
		tFacMain.setAmortizedCode(titaVo.getParam("AmortizedCode"));
		tFacMain.setFreqBase(titaVo.getParam("FreqBase"));
		tFacMain.setPayIntFreq(this.parse.stringToInteger(titaVo.getParam("PayIntFreq")));
		tFacMain.setRepayFreq(this.parse.stringToInteger(titaVo.getParam("RepayFreq")));
		tFacMain.setUtilDeadline(this.parse.stringToInteger(titaVo.getParam("UtilDeadline")));
		tFacMain.setGracePeriod(this.parse.stringToInteger(titaVo.getParam("GracePeriod")));
		tFacMain.setAcctFee(this.parse.stringToBigDecimal(titaVo.getParam("TimAcctFee")));
		tFacMain.setHandlingFee(this.parse.stringToBigDecimal(titaVo.getParam("TimHandlingFee")));
		tFacMain.setGuaranteeDate(this.parse.stringToInteger(titaVo.getParam("GuaranteeDate")));
		tFacMain.setExtraRepayCode(titaVo.getParam("ExtraRepayCode"));
		tFacMain.setCustTypeCode(titaVo.getParam("CustTypeCode"));
		tFacMain.setRuleCode(titaVo.getParam("RuleCode"));
		tFacMain.setRecycleCode(titaVo.getParam("RecycleCode"));
		tFacMain.setRecycleDeadline(this.parse.stringToInteger(titaVo.getParam("RecycleDeadline")));
		tFacMain.setUsageCode(titaVo.getParam("UsageCode"));
		tFacMain.setDepartmentCode(titaVo.getParam("DepartmentCode"));
		tFacMain.setIncomeTaxFlag(titaVo.getParam("IncomeTaxFlag"));
		tFacMain.setCompensateFlag(titaVo.getParam("CompensateFlag"));
		tFacMain.setIrrevocableFlag(titaVo.getParam("IrrevocableFlag"));
		tFacMain.setRateAdjNoticeCode(titaVo.getParam("RateAdjNoticeCode"));
		tFacMain.setPieceCode(titaVo.getParam("PieceCode"));
		tFacMain.setProdBreachFlag(titaVo.getParam("ProdBreachFlag"));

		tFacMain.setBreachFlag(titaVo.getParam("BreachFlag"));
		tFacMain.setBreachCode(titaVo.getParam("BreachCode"));
		tFacMain.setBreachGetCode(titaVo.getParam("BreachGetCode"));
		tFacMain.setProhibitMonth(parse.stringToInteger(titaVo.getParam("ProhibitMonth")));
		tFacMain.setBreachPercent(parse.stringToBigDecimal(titaVo.getParam("BreachPercent")));
		tFacMain.setBreachDecreaseMonth(parse.stringToInteger(titaVo.getParam("BreachDecreaseMonth")));
		tFacMain.setBreachDecrease(parse.stringToBigDecimal(titaVo.getParam("BreachDecrease")));
		tFacMain.setBreachStartPercent(parse.stringToInteger(titaVo.getParam("BreachStartPercent")));

		// ProdBreachFlag為N時寫入
		if ("N".equals(tFacMain.getProdBreachFlag())) {
			tFacMain.setBreachDescription(tTempVo.getParam("Breach"));
		}
		tFacMain.setCreditScore(this.parse.stringToInteger(titaVo.getParam("CreditScore")));
		tFacMain.setRepayCode(this.parse.stringToInteger(titaVo.getParam("RepayCode")));
		tFacMain.setIntroducer(titaVo.getParam("Introducer"));
		tFacMain.setDistrict(titaVo.getParam("District"));
		tFacMain.setFireOfficer(titaVo.getParam("FireOfficer"));
		tFacMain.setEstimate(titaVo.getParam("Estimate"));
		tFacMain.setCreditOfficer(titaVo.getParam("CreditOfficer"));
		tFacMain.setBusinessOfficer(titaVo.getParam("BusinessOfficer"));
		tFacMain.setApprovedLevel(titaVo.getParam("ApprovedLevel"));
		tFacMain.setSupervisor(titaVo.getParam("Supervisor"));
		tFacMain.setInvestigateOfficer(titaVo.getParam("InvestigateOfficer"));
		tFacMain.setEstimateReview(titaVo.getParam("EstimateReview"));
		tFacMain.setCoorgnizer(titaVo.getParam("Coorgnizer"));
		tFacMain.setActFg(titaVo.getActFgI());
		tFacMain.setLastAcctDate(titaVo.getEntDyI());
		tFacMain.setLastKinbr(titaVo.getKinbr());
		tFacMain.setLastTlrNo(titaVo.getTlrNo());
		tFacMain.setLastTxtNo(titaVo.getTxtNo());
		tFacMain.setAcDate(this.txBuffer.getTxCom().getTbsdy());

		this.info("RepayCode = " + titaVo.getParam("RepayCode"));

		try {
			tFacMain = facMainService.update2(tFacMain, titaVo);
		} catch (DBException e) {
			throw new LogicException(titaVo, "E2010", "額度主檔"); // 更新資料時，發生錯誤
		}
		datalog.setEnv(titaVo, beforeFacMain, tFacMain);
		datalog.exec();

	}

	// 訂正時, 還原額度檔
	private void RestoredFacMainRoutine() throws LogicException {
		this.info("RestoredFacMainRoutine ... ");

		tFacMain = facMainService.holdById(new FacMainId(iCustNo, iFacmNo), titaVo);
		if (tFacMain == null) {
			throw new LogicException(titaVo, "E0006", "額度主檔 戶號 = " + iCustNo + " 額度編號 = " + iFacmNo); // 鎖定資料時，發生錯誤
		}
		// 放款交易訂正交易須由最後一筆交易開始訂正
		loanCom.checkEraseFacmTxSeqNo(tFacMain, titaVo);
		beforeFacMain = (FacMain) datalog.clone(tFacMain);
		tFacMain.setCreditSysNo(this.parse.stringToInteger(tTempVo.getParam("CreditSysNo")));
		tFacMain.setProdNo(tTempVo.getParam("ProdNo"));
		tFacMain.setBaseRateCode(tTempVo.getParam("BaseRateCode"));

		tFacMain.setRateIncr(this.parse.stringToBigDecimal(tTempVo.getParam("RateIncr")));
		tFacMain.setIndividualIncr(this.parse.stringToBigDecimal(tTempVo.getParam("IndividualIncr")));
		tFacMain.setApproveRate(this.parse.stringToBigDecimal(tTempVo.getParam("ApproveRate")));
		tFacMain.setRateCode(tTempVo.getParam("RateCode"));
		tFacMain.setFirstRateAdjFreq(this.parse.stringToInteger(tTempVo.getParam("FirstRateAdjFreq")));
		tFacMain.setRateAdjFreq(this.parse.stringToInteger(tTempVo.getParam("RateAdjFreq")));
		tFacMain.setLineAmt(this.parse.stringToBigDecimal(tTempVo.getParam("LineAmt")));
		tFacMain.setAcctCode(tTempVo.getParam("AcctCode"));
		tFacMain.setLoanTermYy(this.parse.stringToInteger(tTempVo.getParam("LoanTermYy")));
		tFacMain.setLoanTermMm(this.parse.stringToInteger(tTempVo.getParam("LoanTermMm")));
		tFacMain.setLoanTermDd(this.parse.stringToInteger(tTempVo.getParam("LoanTermDd")));
		tFacMain.setAmortizedCode(tTempVo.getParam("AmortizedCode"));
		tFacMain.setFreqBase(tTempVo.getParam("FreqBase"));
		tFacMain.setPayIntFreq(this.parse.stringToInteger(tTempVo.getParam("PayIntFreq")));
		tFacMain.setRepayFreq(this.parse.stringToInteger(tTempVo.getParam("RepayFreq")));
		tFacMain.setUtilDeadline(this.parse.stringToInteger(tTempVo.getParam("UtilDeadline")));
		tFacMain.setGracePeriod(this.parse.stringToInteger(tTempVo.getParam("GracePeriod")));
		tFacMain.setAcctFee(this.parse.stringToBigDecimal(tTempVo.getParam("AcctFee")));
		tFacMain.setHandlingFee(this.parse.stringToBigDecimal(tTempVo.getParam("HandlingFee")));
		tFacMain.setGuaranteeDate(this.parse.stringToInteger(tTempVo.getParam("GuaranteeDate")));
		tFacMain.setExtraRepayCode(tTempVo.getParam("ExtraRepayCode"));
		tFacMain.setCustTypeCode(tTempVo.getParam("CustTypeCode"));
		tFacMain.setRuleCode(tTempVo.getParam("RuleCode"));
		tFacMain.setRecycleCode(tTempVo.getParam("RecycleCode"));
		tFacMain.setRecycleDeadline(this.parse.stringToInteger(tTempVo.getParam("RecycleDeadline")));
		tFacMain.setUsageCode(tTempVo.getParam("UsageCode"));
		tFacMain.setDepartmentCode(tTempVo.getParam("DepartmentCode"));
		tFacMain.setIncomeTaxFlag(tTempVo.getParam("IncomeTaxFlag"));
		tFacMain.setCompensateFlag(tTempVo.getParam("CompensateFlag"));
		tFacMain.setIrrevocableFlag(tTempVo.getParam("IrrevocableFlag"));
		tFacMain.setRateAdjNoticeCode(tTempVo.getParam("RateAdjNoticeCode"));
		tFacMain.setPieceCode(tTempVo.getParam("PieceCode"));
		tFacMain.setProdBreachFlag(tTempVo.getParam("ProdBreachFlag"));

		tFacMain.setBreachFlag(tTempVo.getParam("BreachFlag"));
		tFacMain.setBreachCode(tTempVo.getParam("BreachCode"));
		tFacMain.setBreachGetCode(tTempVo.getParam("BreachGetCode"));
		tFacMain.setProhibitMonth(parse.stringToInteger(tTempVo.getParam("ProhibitMonth")));
		tFacMain.setBreachPercent(parse.stringToBigDecimal(tTempVo.getParam("BreachPercent")));
		tFacMain.setBreachDecreaseMonth(parse.stringToInteger(tTempVo.getParam("BreachDecreaseMonth")));
		tFacMain.setBreachDecrease(parse.stringToBigDecimal(tTempVo.getParam("BreachDecrease")));
		tFacMain.setBreachStartPercent(parse.stringToInteger(tTempVo.getParam("BreachStartPercent")));

		// ProdBreachFlag為N時寫入
		if ("N".equals(tFacMain.getProdBreachFlag())) {
			tFacMain.setBreachDescription(tTempVo.getParam("Breach"));
		}
		tFacMain.setCreditScore(this.parse.stringToInteger(tTempVo.getParam("CreditScore")));
		tFacMain.setRepayCode(this.parse.stringToInteger(tTempVo.getParam("RepayCode")));
		tFacMain.setIntroducer(tTempVo.getParam("Introducer"));
		tFacMain.setDistrict(tTempVo.getParam("District"));
		tFacMain.setFireOfficer(tTempVo.getParam("FireOfficer"));
		tFacMain.setEstimate(tTempVo.getParam("Estimate"));
		tFacMain.setCreditOfficer(tTempVo.getParam("CreditOfficer"));
		tFacMain.setLoanOfficer(tTempVo.getParam("LoanOfficer"));
		tFacMain.setBusinessOfficer(tTempVo.getParam("BusinessOfficer"));
		tFacMain.setApprovedLevel(tTempVo.getParam("ApprovedLevel"));
		tFacMain.setSupervisor(tTempVo.getParam("Supervisor"));
		tFacMain.setInvestigateOfficer(tTempVo.getParam("InvestigateOfficer"));
		tFacMain.setEstimateReview(tTempVo.getParam("EstimateReview"));
		tFacMain.setCoorgnizer(tTempVo.getParam("Coorgnizer"));
		tFacMain.setActFg(this.parse.stringToInteger(tTempVo.getParam("ActFg")));
		tFacMain.setLastAcctDate(this.parse.stringToInteger(tTempVo.getParam("LastAcctDate")));
		tFacMain.setLastKinbr(tTempVo.getParam("LastKinbr"));
		tFacMain.setLastTlrNo(tTempVo.getParam("LastTlrNo"));
		tFacMain.setLastTxtNo(tTempVo.getParam("LastTxtNo"));
		tFacMain.setAcDate(this.parse.stringToInteger(tTempVo.getParam("AcDate")));

		try {
			tFacMain = facMainService.update2(tFacMain, titaVo);
		} catch (DBException e) {
			throw new LogicException(titaVo, "E0007",
					"額度主檔 戶號 = " + iCustNo + " 額度編號 = " + iFacmNo + " " + e.getErrorMsg()); // 更新資料時，發生錯誤
		}
		datalog.setEnv(titaVo, beforeFacMain, tFacMain);
		datalog.exec("修改額度主檔資料");
	}

	private void bankAuthActRoutine() throws LogicException {

		this.info("diff (RepayCode)" + titaVo.getParam("RepayCode") + " " + titaVo.getParam("OldRepayCode"));
		this.info("diff(RepayBank)" + titaVo.getParam("RepayBank") + " " + titaVo.getParam("OldRepayBank"));
		this.info("diff(PostCode)" + titaVo.getParam("PostCode") + " " + titaVo.getParam("OldPostCode"));
		this.info("diff(RepayAcctNo)" + titaVo.getParam("RepayAcctNo") + " " + titaVo.getParam("OldAcctNo"));
		this.info("diff(RepayAcctNo)" + titaVo.getParam("RelationCode") + " " + titaVo.getParam("OldRelationCode"));
		this.info("diff(RepayAcctNo)" + titaVo.getParam("RelationName") + " " + titaVo.getParam("OldRelationName"));
		this.info("diff(RepayAcctNo)" + titaVo.getParam("RelationBirthday") + " "
				+ titaVo.getParam("OldRelationBirthday"));
		this.info("diff(RepayAcctNo)" + titaVo.getParam("RelationGender") + " " + titaVo.getParam("OldRelationGender"));
		this.info("diff(RepayAcctNo)" + titaVo.getParam("RelationId") + " " + titaVo.getParam("OldRelationId"));
		// 還款方式、授權帳號未變動則return
		if (titaVo.getParam("RepayCode").equals(titaVo.getParam("OldRepayCode"))
				&& titaVo.getParam("RepayBank").equals(titaVo.getParam("OldRepayBank"))
				&& titaVo.getParam("PostCode").equals(titaVo.getParam("OldPostCode"))
				&& titaVo.getParam("RepayAcctNo").equals(titaVo.getParam("OldAcctNo"))
				&& titaVo.getParam("RelationCode").equals(titaVo.getParam("OldRelationCode"))
				&& titaVo.getParam("RelationName").equals(titaVo.getParam("OldRelationName"))
				&& titaVo.getParam("RelationBirthday").equals(titaVo.getParam("OldRelationBirthday"))
				&& titaVo.getParam("RelationGender").equals(titaVo.getParam("OldRelationGender"))
				&& titaVo.getParam("RelationId").equals(titaVo.getParam("OldRelationId"))) {
			return;
		}

		txtitaVo = new TitaVo();
		txtitaVo = (TitaVo) titaVo.clone();
		txtitaVo.putParam("RepayCode", titaVo.getParam("OldRepayCode"));
		txtitaVo.putParam("PostCode", titaVo.getParam("OldPostCode"));
		txtitaVo.putParam("RepayAcctNo", titaVo.getParam("OldAcctNo"));
		txtitaVo.putParam("RelationCode", titaVo.getParam("OldRelationCode"));
		txtitaVo.putParam("RelationName", titaVo.getParam("OldRelationName"));
		txtitaVo.putParam("RelationBirthday", titaVo.getParam("OldRelationBirthday"));
		txtitaVo.putParam("RelationGender", titaVo.getParam("OldRelationGender"));
		txtitaVo.putParam("RelationId", titaVo.getParam("OldRelationId"));
		txtitaVo.putParam("RepayBank", titaVo.getParam("OldRepayBank"));
		// 授權帳號變更時Rim會檢查須先建授權資料，此時僅作
		// 2段式放行或1段式
		if (titaVo.isActfgRelease()) {
			// 舊還款帳號(含還款方式)刪除
			if ("02".equals(titaVo.getParam("OldRepayCode"))) {
				bankAuthActCom.del("A", txtitaVo);
			}
			// 變更帳號檔
			if ("02".equals(titaVo.getParam("RepayCode"))) {
				bankAuthActCom.add("A", titaVo);
			}
			// 還款方式變更記錄檔
			if (!"02".equals(titaVo.getParam("RepayCode"))) {
				bankAuthActCom.addRepayActChangeLog(titaVo);
			}
		} else {
			// 經辦登錄檢查:額度變更時Rim會檢查須先建授權帳號資料
			if ("02".equals(titaVo.getParam("RepayCode"))) {
				bankAuthActCom.add("A", titaVo);
			}
		}

	}

	// 更新階梯式利率
	private void UpdateFacProdStepRateRoutine() throws LogicException {
		this.info("UpdateFacProdStepRateRoutine ...");

		DeleteFacProdStepRateRoutine();
		// 更新階梯式利率
		FacProdStepRate tFacProdStepRate = new FacProdStepRate();

		for (int i = 1; i <= 10; i++) {
			if (this.parse.stringToDouble(titaVo.getParam("StepMonthE" + i)) > 0) {
				tFacProdStepRate.setProdNo(sProdNo);
				tFacProdStepRate.setMonthStart(this.parse.stringToInteger(titaVo.getParam("StepMonthS" + i)));
				tFacProdStepRate.setFacProdStepRateId(
						new FacProdStepRateId(sProdNo, this.parse.stringToInteger(titaVo.getParam("StepMonthS" + i))));

				tFacProdStepRate.setMonthEnd(this.parse.stringToInteger(titaVo.getParam("StepMonthE" + i)));

				tFacProdStepRate.setRateType(titaVo.getParam("StepRateType" + i));
				tFacProdStepRate.setRateIncr(this.parse.stringToBigDecimal(titaVo.getParam("StepRateIncr" + i)));
				tFacProdStepRate.setCreateDate(
						parse.IntegerToSqlDateO(dDateUtil.getNowIntegerForBC(), dDateUtil.getNowIntegerTime()));
				tFacProdStepRate.setCreateEmpNo(titaVo.getTlrNo());
				tFacProdStepRate.setLastUpdate(
						parse.IntegerToSqlDateO(dDateUtil.getNowIntegerForBC(), dDateUtil.getNowIntegerTime()));
				tFacProdStepRate.setLastUpdateEmpNo(titaVo.getTlrNo());

				try {
					facProdStepRateService.insert(tFacProdStepRate, titaVo);
				} catch (DBException e) {
					throw new LogicException(titaVo, "E2009", "階梯式利率"); // 新增資料時，發生錯誤
				}
			} else {
				break;
			}
		}

	}

	// 刪除階梯式利率
	private void DeleteFacProdStepRateRoutine() throws LogicException {
		this.info("DeleteFacProdStepRateRoutine ...");

		sProdNo = FormatUtil.pad9(String.valueOf(tFacMain.getCustNo()), 7)
				+ FormatUtil.pad9(String.valueOf(tFacMain.getFacmNo()), 3);

		Slice<FacProdStepRate> slFacProdStepRate = facProdStepRateService.stepRateProdNoEq(sProdNo, 0, 999, this.index,
				Integer.MAX_VALUE, titaVo);
		List<FacProdStepRate> lFacProdStepRate = slFacProdStepRate == null ? null : slFacProdStepRate.getContent();
		if (lFacProdStepRate != null && lFacProdStepRate.size() > 0) {
			try {
				facProdStepRateService.deleteAll(lFacProdStepRate, titaVo);
			} catch (DBException e) {
				throw new LogicException(titaVo, "E2008", "階梯式利率"); // 刪除資料時，發生錯誤
			}
		}

	}

}