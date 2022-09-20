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
import com.st1.itx.db.domain.CustMain;
import com.st1.itx.db.domain.FacCaseAppl;
import com.st1.itx.db.domain.FacMain;
import com.st1.itx.db.domain.FacMainId;
import com.st1.itx.db.domain.FacProd;
import com.st1.itx.db.domain.FacProdStepRate;
import com.st1.itx.db.domain.FacProdStepRateId;
import com.st1.itx.db.domain.TxTemp;
import com.st1.itx.db.domain.TxTempId;
import com.st1.itx.db.service.CdGseqService;
import com.st1.itx.db.service.CustMainService;
import com.st1.itx.db.service.FacCaseApplService;
import com.st1.itx.db.service.FacMainService;
import com.st1.itx.db.service.FacProdBreachService;
import com.st1.itx.db.service.FacProdService;
import com.st1.itx.db.service.FacProdStepRateService;
import com.st1.itx.db.service.TxTempService;
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.common.BankAuthActCom;
import com.st1.itx.util.common.ClFacCom;
import com.st1.itx.util.common.CustCom;
import com.st1.itx.util.common.GSeqCom;
import com.st1.itx.util.common.LoanCloseBreachCom;
import com.st1.itx.util.common.LoanCom;
import com.st1.itx.util.date.DateUtil;
import com.st1.itx.util.format.FormatUtil;
import com.st1.itx.util.parse.Parse;

/*
 * L2153 核准額度登錄
 * a.申請案件准時,若尚未有戶號,則由電腦產生戶號.
 * b.申請案件准時,由電腦產生額度編號.
 * c.資料拷貝碼:若此案件為團體戶則出現此欄位
 * 'Y’:將此次核准之資料保留下來,作為下 次同一團體戶之案件核准時的初值資料
 */

/**
 * L2153 核准額度登錄
 * 
 * @author iza
 * @version 1.0.0
 */
@Service("L2153")
@Scope("prototype")
public class L2153 extends TradeBuffer {

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
	public CdGseqService cdGseqService;
	@Autowired
	public TxTempService txTempService;

	@Autowired
	Parse parse;
	@Autowired
	LoanCom loanCom;
	@Autowired
	public CustCom custCom;
	@Autowired
	GSeqCom gGSeqCom;
	@Autowired
	DateUtil dDateUtil;
	@Autowired
	BankAuthActCom bankAuthActCom;
	@Autowired
	public ClFacCom clFacCom;
	@Autowired
	LoanCloseBreachCom loanCloseBreachCom;

	// input area
	private TitaVo titaVo = new TitaVo();
	private int iApplNo;
	private int iCustNo;
	private int iFacmNo;
	private String iCustId;
	private String iProdNo;
	private int iApproveDate;

	// work area
	private int wkApplNo;
	private int wkCustNo;
	private int wkFacmNo;
	private String wkCustUKey = "";

	private String sProdNo = "";
	private TempVo tTempVo = new TempVo();
	private TxTemp tTxTemp;
	private TxTempId tTxTempId;
	private FacProd tFacProd;
	private FacMain tFacMain;
	private FacMainId tFacMainId;
	private FacCaseAppl tFacCaseAppl;
	private List<TxTemp> lTxTemp;
	private boolean isElaonUpdate = false;
	private boolean isEloan = false;
	private TitaVo txtitaVo;

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active L2153");
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

		bankAuthActCom.setTxBuffer(txBuffer);
		clFacCom.setTxBuffer(this.txBuffer);

		// 取得輸入資料
		iCustId = titaVo.getParam("CustId").trim();
		iApplNo = this.parse.stringToInteger(titaVo.getParam("ApplNo"));
		iCustNo = this.parse.stringToInteger(titaVo.getParam("CustNo"));
		iFacmNo = this.parse.stringToInteger(titaVo.getParam("FacmNo"));
		iProdNo = titaVo.getParam("ProdNo");
		iApproveDate = this.parse.stringToInteger(titaVo.getParam("ApproveDate"));
		// isEloan
		if (titaVo.isEloan() || "ELTEST".equals(titaVo.getTlrNo())) {
			this.isEloan = true;
			titaVo.putParam("RELCD", "1");
			titaVo.putParam("ACTFG", "0");
		}

		// 查詢商品參數檔
		tFacProd = facProdService.findById(iProdNo, titaVo);
		if (tFacProd == null) {
			throw new LogicException(titaVo, "E2003", "商品參數檔  商品代碼=" + iProdNo); // 查無資料
		}

		// Eloan 清償違約說明
		if (this.isEloan) {
			titaVo.putParam("Breach", loanCloseBreachCom.getBreachDescription(iProdNo, titaVo));
		}

		isElaonUpdate = false;
		// Eloan check 是否重送 Y -> 修正
		if (this.isEloan) {
			tFacCaseAppl = facCaseApplService.findById(iApplNo, titaVo);
			if (tFacCaseAppl == null) {
				throw new LogicException(titaVo, "E0006", "案件申請檔"); // 鎖定資料時，發生錯誤
			}
			// 處理情形 1:准
			if ("1".equals(tFacCaseAppl.getProcessCode())) {
				isElaonUpdate = true;
			}
			this.info(" isElaonUpdate=" + isElaonUpdate);
		}

		// 登錄
		if (titaVo.isActfgEntry() && titaVo.isHcodeNormal()) {
			// 取得客戶編號及額度編號
			GetCustNoAndFacmNoRoutine();
			EntryNormalRoutine();
			// 檢查案件隸屬單位為1 企金時 檢查商品是否為企金可使用JAVA VAR檢查
			if ("1".equals(titaVo.get("DepartmentCode")) && !("Y".equals(tFacProd.getEnterpriseFg()))) {
				throw new LogicException(titaVo, "E0006", "商品參數不正確"); // 鎖定資料時，發生錯誤
			}
		}
		// 登錄 修正
		if (titaVo.isActfgEntry() && titaVo.isHcodeModify()) {
			EntryEraseRoutine();
			GetCustNoAndFacmNoRoutine();
			EntryNormalRoutine();
		}
		// 登錄 訂正
		if (titaVo.isActfgEntry() && titaVo.isHcodeErase()) {
			EntryEraseRoutine();
		}
		// 放行及放行訂正
		if (titaVo.isActfgSuprele()) {
			ReleaseRoutine();
		}
		// 銀扣授權帳號檔
		if (titaVo.isActfgRelease()) {
//			bankAuthActCom.add("A",titaVo);
			// 新還款帳號(含還款方式)刪除
			if ("02".equals(titaVo.getParam("RepayCode"))) {
				bankAuthActCom.add("A", titaVo);
			} else {
				txtitaVo = new TitaVo();
				txtitaVo = (TitaVo) titaVo.clone();
				txtitaVo.putParam("RepayCode", titaVo.getParam("RepayCode"));
				bankAuthActCom.addRepayActChangeLog(txtitaVo);
			}
		}

		// 額度與擔保品關聯檔變動處理
		clFacCom.changeClFac(iApplNo, titaVo);

		this.info("relcd" + titaVo.toString());
		this.totaVo.putParam("OCustNo", wkCustNo);
		this.totaVo.putParam("OFacmNo", wkFacmNo);
		this.addList(this.totaVo);
		return this.sendList();
	}

	// 登錄
	private void EntryNormalRoutine() throws LogicException {
		this.info("EntryNormalRoutine ... ");

		tFacCaseAppl = facCaseApplService.holdById(iApplNo, titaVo);
		if (tFacCaseAppl == null) {
			throw new LogicException(titaVo, "E0006", "案件申請檔"); // 鎖定資料時，發生錯誤
		}

		// 新增交易暫存檔
		AddTxTempRoutine();

		if (isElaonUpdate) {
			// 更新額度檔
			tFacMain = facMainService.facmApplNoFirst(iApplNo, titaVo);
			if (tFacCaseAppl == null) {
				throw new LogicException(titaVo, "E0006", "額度檔" + iApplNo); // 鎖定資料時，發生錯誤
			}
			wkCustNo = tFacMain.getCustNo();
			wkFacmNo = tFacMain.getFacmNo();
			UpdateFacMainRoutine();
		} else {
			// 更新案件申請檔
			UpdateFacCaseApplRoutine();
			// 新增額度檔
			InsertFacMainRoutine();
		}
		// 更新階梯式利率 //
		UpdateFacProdStepRateRoutine();

		titaVo.putParam("CustNo", wkCustNo);
		titaVo.putParam("FacmNo", wkFacmNo);
		titaVo.putParam("MRKEY",
				FormatUtil.pad9(String.valueOf(wkCustNo), 7) + "-" + FormatUtil.pad9(String.valueOf(wkFacmNo), 3));

	}

	// 登錄 訂正
	private void EntryEraseRoutine() throws LogicException {
		this.info("EntryEraseRoutine ... ");

		Slice<TxTemp> slTxTemp = txTempService.txTempTxtNoEq(titaVo.getOrgEntdyI() + 19110000, titaVo.getOrgKin(),
				titaVo.getOrgTlr(), titaVo.getOrgTno(), this.index, Integer.MAX_VALUE, titaVo);
		lTxTemp = slTxTemp == null ? null : slTxTemp.getContent();
		if (lTxTemp == null || lTxTemp.size() == 0) {
			throw new LogicException(titaVo, "E0001", "交易暫存檔 分行別 = " + titaVo.getOrgKin() + " 交易員代號 = "
					+ titaVo.getOrgTlr() + " 交易序號 = " + titaVo.getOrgTno()); // 查詢資料不存在
		}
		for (TxTemp tx : lTxTemp) {
			wkCustNo = this.parse.stringToInteger(tx.getSeqNo().substring(0, 7));
			wkFacmNo = this.parse.stringToInteger(tx.getSeqNo().substring(7, 10));
			tTempVo = tTempVo.getVo(tx.getText());
			wkApplNo = this.parse.stringToInteger(tTempVo.getParam("ApplNo"));
			this.info("   wkApplNo = " + wkApplNo);
			this.info("   wkCustNo = " + wkCustNo);
			this.info("   wkFacmNo = " + wkFacmNo);
			// 還原案件申請檔
			RestoredFacCaseApplRoutine();
			// 刪除額度檔
			DeleteFacMainRoutine();
			// 刪除階梯式利率
			DeleteFacProdStepRateRoutine();
		}
	}

	// 放行及放行訂正
	private void ReleaseRoutine() throws LogicException {
		this.info("ReleaseRoutine ... ");
		Slice<TxTemp> slTxTemp = txTempService.txTempTxtNoEq(titaVo.getOrgEntdyI() + 19110000, titaVo.getOrgKin(),
				titaVo.getOrgTlr(), titaVo.getOrgTno(), this.index, Integer.MAX_VALUE, titaVo);
		lTxTemp = slTxTemp == null ? null : slTxTemp.getContent();
		if (lTxTemp == null || lTxTemp.size() == 0) {
			throw new LogicException(titaVo, "E0001", "交易暫存檔 分行別 = " + titaVo.getOrgKin() + " 交易員代號 = "
					+ titaVo.getOrgTlr() + " 交易序號 = " + titaVo.getOrgTno()); // 查詢資料不存在
		}
		for (TxTemp tx : lTxTemp) {
			wkCustNo = this.parse.stringToInteger(tx.getSeqNo().substring(0, 7));
			wkFacmNo = this.parse.stringToInteger(tx.getSeqNo().substring(7, 10));
			tTempVo = tTempVo.getVo(tx.getText());
			wkApplNo = this.parse.stringToInteger(tTempVo.getParam("ApplNo"));
			this.info("   wkApplNo = " + wkApplNo);
			this.info("   wkCustNo = " + wkCustNo);
			this.info("   wkFacmNo = " + wkFacmNo);
			tFacMain = facMainService.holdById(new FacMainId(wkCustNo, wkFacmNo), titaVo);
			if (tFacMain == null) {
				throw new LogicException(titaVo, "E0006", "額度主檔"); // 鎖定資料時，發生錯誤
			}
			// 放行 一般
			if (titaVo.isHcodeNormal()) {
				if (tFacMain.getActFg() != 1) {
					throw new LogicException(titaVo, "E0017", "額度主檔 戶號 = " + wkCustNo + "額度編號 = " + wkFacmNo); // 該筆交易狀態非待放行，不可做交易放行
				}
				// 新增交易暫存檔
				TxTemp tTxTemp = new TxTemp();
				TxTempId tTxTempId = new TxTempId();
				loanCom.setTxTemp(tTxTempId, tTxTemp, wkCustNo, wkFacmNo, 0, 0, titaVo);
				tTempVo.clear();
				tTempVo.putParam("ActFg", tFacMain.getActFg());
				tTempVo.putParam("LastAcctDate", tFacMain.getLastAcctDate());
				tTempVo.putParam("LastKinbr", tFacMain.getLastKinbr());
				tTempVo.putParam("LastTlrNo", tFacMain.getLastTlrNo());
				tTempVo.putParam("LastTxtNo", tFacMain.getLastTxtNo());
				tTxTemp.setText(tTempVo.getJsonString());
				try {
					txTempService.insert(tTxTemp, titaVo);
				} catch (DBException e) {
					throw new LogicException(titaVo, "E0005", "交易暫存檔 Key = " + tTxTempId); // 新增資料時，發生錯誤
				}
				// 更新額度主檔
				tFacMain.setActFg(titaVo.getActFgI());
				tFacMain.setLastAcctDate(titaVo.getEntDyI());
				tFacMain.setLastKinbr(titaVo.getKinbr());
				tFacMain.setLastTlrNo(titaVo.getTlrNo());
				tFacMain.setLastTxtNo(titaVo.getTxtNo());
			}
			// 放行訂正
			if (titaVo.isHcodeErase()) {
				// 放款交易訂正交易須由最後一筆交易開始訂正
				loanCom.checkEraseFacmTxSeqNo(tFacMain, titaVo);
				tTempVo = tTempVo.getVo(tx.getText());
				tFacMain.setActFg(this.parse.stringToInteger(tTempVo.getParam("ActFg")));
				tFacMain.setLastAcctDate(this.parse.stringToInteger(tTempVo.getParam("LastAcctDate")));
				tFacMain.setLastKinbr(tTempVo.getParam("LastKinbr"));
				tFacMain.setLastTlrNo(tTempVo.getParam("LastTlrNo"));
				tFacMain.setLastTxtNo(tTempVo.getParam("LastTxtNo"));

			}
			try {
				tFacMain = facMainService.update2(tFacMain, titaVo);
			} catch (DBException e) {
				throw new LogicException(titaVo, "E0008",
						"額度主檔 戶號 = " + wkCustNo + "額度編號 = " + wkFacmNo + e.getErrorMsg()); // 新增資料時，發生錯誤
			}
		}
	}

	// 取得客戶編號及額度編號
	private void GetCustNoAndFacmNoRoutine() throws LogicException {
		this.info("GetCustNoAndFacmNoRoutine ...");
		CustMain tCustMain = null;
		tCustMain = custMainService.custIdFirst(iCustId, titaVo);
		if (tCustMain == null) {
			throw new LogicException(titaVo, "E2003", "客戶資料主檔 = " + iCustId); // 查無資料
		}
		this.info("GetCustNoAndFacmNoRoutine wkCustUKey=" + tCustMain.getCustId() + "-" + wkCustUKey);
		wkCustUKey = tCustMain.getCustUKey().trim();
		tCustMain = custMainService.holdById(wkCustUKey, titaVo);
		if (tCustMain == null) {
			throw new LogicException(titaVo, "E2011", "客戶資料主檔"); // 鎖定資料時，發生錯誤
		}
		wkCustNo = iCustNo;
		wkFacmNo = iFacmNo;
		if (isElaonUpdate) {
			tFacMain = facMainService.facmApplNoFirst(iApplNo, titaVo);
			wkCustNo = tFacMain.getCustNo();
			wkFacmNo = tFacMain.getFacmNo();
		} else {
			if (titaVo.isHcodeNormal()) {
				wkCustNo = tCustMain.getCustNo();
				if (wkCustNo == 0) {
					wkCustNo = gGSeqCom.getSeqNo(0, 0, "L2", "0001", 9999999, titaVo);
					tCustMain.setCustNo(wkCustNo);
				}
				wkFacmNo = tCustMain.getLastFacmNo() + 1;
				tCustMain.setLastFacmNo(wkFacmNo);
				try {
					custMainService.update(tCustMain, titaVo);
				} catch (DBException e) {
					throw new LogicException(titaVo, "E2010", "客戶資料主檔"); // 更新資料時，發生錯誤
				}
			}
		}
	}

	// 新增額度檔
	private void InsertFacMainRoutine() throws LogicException {
		this.info("InsertFacMainRoutine ...");

		tFacMain = new FacMain();
		tFacMainId = new FacMainId();
		tFacMainId.setCustNo(wkCustNo);
		tFacMainId.setFacmNo(wkFacmNo);
		tFacMain.setCustNo(wkCustNo);
		tFacMain.setFacmNo(wkFacmNo);
		tFacMain.setFacMainId(tFacMainId);
		tFacMain.setLastBormNo(0);
		tFacMain.setLastBormRvNo(900);
		setFacMainRoutine();
		try {
			facMainService.insert(tFacMain, titaVo);
		} catch (DBException e) {
			throw new LogicException(titaVo, "E0005", "額度主檔"); // 新增資料時，發生錯誤
		}
	}

	// 更新額度檔
	private void UpdateFacMainRoutine() throws LogicException {
		this.info("UpdateFacMainRoutine ...");

		tFacMain = facMainService.holdById(new FacMainId(wkCustNo, wkFacmNo), titaVo);
		if (tFacMain == null) {
			throw new LogicException(titaVo, "E0006", "額度主檔 戶號 = " + wkCustNo + " 額度編號 = " + wkFacmNo); // 鎖定資料時，發生錯誤
		}
		if (tFacMain.getLastBormNo() > 0) {

		}
		tFacMain.setLastBormRvNo(900);
		setFacMainRoutine();
		try {
			facMainService.update(tFacMain, titaVo);
		} catch (DBException e) {
			throw new LogicException(titaVo, "E0007", "額度主檔"); // 更新資料時，發生錯誤
		}
	}

	private void setFacMainRoutine() throws LogicException {
		this.info("setFacMainRoutine ...");
		tFacMain.setApplNo(this.parse.stringToInteger(titaVo.getParam("ApplNo")));
		tFacMain.setCreditSysNo(this.parse.stringToInteger(titaVo.getParam("CreditSysNo")));
		tFacMain.setProdNo(titaVo.getParam("ProdNo"));
//		E-LOAN處理 
		if (isEloan) {
//			指標利率代碼為NULL時放99自訂利率
			if (titaVo.get("BaseRateCode") == null) {
				tFacMain.setBaseRateCode("99");
			} else {
				tFacMain.setBaseRateCode(titaVo.get("BaseRateCode"));
			}

			// 綠色授信註記
			if (titaVo.get("Grcd") != null) {
				tFacMain.setGrcd(titaVo.get("Grcd"));
			}

			// 綠色支出類別
			if (titaVo.get("GrKind") != null) {
				tFacMain.setGrKind(titaVo.get("GrKind"));
			}

			// 永續績效連結授信
			if (titaVo.get("EsGcd") != null) {
				tFacMain.setEsGcd(titaVo.get("EsGcd"));
			}

			// 永續績效連結授信類別
			if (titaVo.get("EsGKind") != null) {
				tFacMain.setEsGKind(titaVo.get("EsGKind"));
			}

			// 永續績效連結授信約定條件全部未達成通報
			if (titaVo.get("EsGcnl") != null) {
				tFacMain.setEsGcnl(titaVo.get("EsGcnl"));
			}
		} else {
			tFacMain.setBaseRateCode(titaVo.getParam("BaseRateCode"));
			tFacMain.setGrcd(titaVo.getParam("Grcd"));
			tFacMain.setGrKind(titaVo.getParam("GrKind"));
			tFacMain.setEsGcd(titaVo.getParam("EsGcd"));
			tFacMain.setEsGKind(titaVo.getParam("EsGKind"));
			tFacMain.setEsGcnl(titaVo.getParam("EsGcnl"));
		}
		if ("N".equals(tFacProd.getIncrFlag())) {
			tFacMain.setRateIncr(new BigDecimal("0"));
			tFacMain.setIndividualIncr(this.parse.stringToBigDecimal(titaVo.getParam("RateIncr")));
		} else {
			tFacMain.setRateIncr(this.parse.stringToBigDecimal(titaVo.getParam("RateIncr")));
			tFacMain.setIndividualIncr(new BigDecimal("0"));
		}
		tFacMain.setApproveRate(this.parse.stringToBigDecimal(titaVo.getParam("ApproveRate")));
		tFacMain.setAnnualIncr(new BigDecimal("0"));
		tFacMain.setEmailIncr(new BigDecimal("0"));
		tFacMain.setGraceIncr(new BigDecimal("0"));
		tFacMain.setRateCode(titaVo.getParam("RateCode"));
		tFacMain.setFirstRateAdjFreq(this.parse.stringToInteger(titaVo.getParam("FirstRateAdjFreq")));
		tFacMain.setRateAdjFreq(this.parse.stringToInteger(titaVo.getParam("RateAdjFreq")));
		tFacMain.setCurrencyCode(titaVo.getParam("CurrencyCode"));
		tFacMain.setLineAmt(this.parse.stringToBigDecimal(titaVo.getParam("TimApplAmt")));
		tFacMain.setUtilAmt(new BigDecimal("0"));
		tFacMain.setUtilBal(new BigDecimal("0"));
		tFacMain.setAcctCode(titaVo.getParam("AcctCode"));
		tFacMain.setLoanTermYy(this.parse.stringToInteger(titaVo.getParam("LoanTermYy")));
		tFacMain.setLoanTermMm(this.parse.stringToInteger(titaVo.getParam("LoanTermMm")));
		tFacMain.setLoanTermDd(this.parse.stringToInteger(titaVo.getParam("LoanTermDd")));
		tFacMain.setFirstDrawdownDate(0);
		tFacMain.setMaturityDate(0);
		tFacMain.setAmortizedCode(titaVo.getParam("AmortizedCode"));
		tFacMain.setFreqBase(titaVo.getParam("FreqBase"));
		tFacMain.setPayIntFreq(this.parse.stringToInteger(titaVo.getParam("PayIntFreq")));
		tFacMain.setRepayFreq(this.parse.stringToInteger(titaVo.getParam("RepayFreq")));
		tFacMain.setUtilDeadline(this.parse.stringToInteger(titaVo.getParam("UtilDeadline")));
		tFacMain.setGracePeriod(this.parse.stringToInteger(titaVo.getParam("GracePeriod")));
		tFacMain.setAcctFee(this.parse.stringToBigDecimal(titaVo.getParam("TimAcctFee")));
		tFacMain.setHandlingFee(this.parse.stringToBigDecimal(titaVo.getParam("TimHandlingFee")));
		tFacMain.setExtraRepayCode(titaVo.getParam("ExtraRepayCode"));
		tFacMain.setIntCalcCode(titaVo.getParam("IntCalcCode"));
		String custTypeCode = titaVo.getParam("CustTypeCode");
		if (isEloan) {
			if (!custTypeCode.isEmpty()) {
				custTypeCode = custCom.eLoanCustTypeCode(titaVo, custTypeCode);
			}
		}
		tFacMain.setCustTypeCode(custTypeCode);
		tFacMain.setRuleCode(titaVo.getParam("RuleCode"));
		tFacMain.setRecycleCode(titaVo.getParam("RecycleCode"));
		tFacMain.setRecycleDeadline(this.parse.stringToInteger(titaVo.getParam("RecycleDeadline")));
		tFacMain.setUsageCode(FormatUtil.pad9(titaVo.getParam("UsageCode"), 2)); // 6/16eloan進來需補0
		tFacMain.setDepartmentCode(titaVo.getParam("DepartmentCode"));
		tFacMain.setIncomeTaxFlag(titaVo.getParam("IncomeTaxFlag"));
		tFacMain.setCompensateFlag(titaVo.getParam("CompensateFlag"));
		tFacMain.setIrrevocableFlag(titaVo.getParam("IrrevocableFlag"));
		tFacMain.setRateAdjNoticeCode(titaVo.getParam("RateAdjNoticeCode"));
		tFacMain.setPieceCode(tFacCaseAppl.getPieceCode());
		tFacMain.setRepayCode(this.parse.stringToInteger(titaVo.getParam("RepayCode")));
		tFacMain.setIntroducer(titaVo.getParam("Introducer"));
		tFacMain.setDistrict(titaVo.getParam("District"));
		tFacMain.setFireOfficer(titaVo.getParam("FireOfficer"));
		tFacMain.setEstimate(titaVo.getParam("Estimate"));
		tFacMain.setCreditOfficer(titaVo.getParam("CreditOfficer"));
		tFacMain.setLoanOfficer(titaVo.getParam("BusinessOfficer"));
		tFacMain.setBusinessOfficer(titaVo.getParam("BusinessOfficer"));
		tFacMain.setApprovedLevel(titaVo.getParam("ApprovedLevel"));
		tFacMain.setSupervisor(titaVo.getParam("Supervisor"));
		tFacMain.setInvestigateOfficer(titaVo.getParam("InvestigateOfficer"));
		tFacMain.setEstimateReview(titaVo.getParam("EstimateReview"));
		tFacMain.setCoorgnizer(titaVo.getParam("Coorgnizer"));

		tFacMain.setProdBreachFlag(titaVo.getParam("ProdBreachFlag"));
		tFacMain.setBreachDescription(titaVo.getParam("Breach"));
		tFacMain.setCreditScore(this.parse.stringToInteger(titaVo.getParam("CreditScore")));
		tFacMain.setGuaranteeDate(this.parse.stringToInteger(titaVo.getParam("GuaranteeDate")));
		tFacMain.setContractNo("");
		tFacMain.setColSetFlag("N");
		tFacMain.setActFg(titaVo.getActFgI());
		tFacMain.setLastAcctDate(titaVo.getEntDyI());
		tFacMain.setLastKinbr(titaVo.getKinbr());
		tFacMain.setLastTlrNo(titaVo.getTlrNo());
		tFacMain.setLastTxtNo(titaVo.getTxtNo());
		tFacMain.setAcDate(this.txBuffer.getTxCom().getTbsdy());
		tFacMain.setL9110Flag("N");
//		企金，案件申請准駁日期寫入額度設定日
		if (!isEloan) {
			tFacMain.setSettingDate(iApproveDate);
		}
	}

	// 刪除額度檔
	private void DeleteFacMainRoutine() throws LogicException {
		this.info("DeleteFacMainRoutine ...");

		tFacMain = facMainService.holdById(new FacMainId(wkCustNo, wkFacmNo), titaVo);
		if (tFacMain == null) {
			throw new LogicException(titaVo, "E0006", "額度主檔 戶號 = " + wkCustNo + " 額度編號 = " + wkFacmNo); // 鎖定資料時，發生錯誤
		}
		// 訂正時檢查為該戶號最後一筆額度 客戶主檔該額度-1
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

		// 額度交易訂正交易須由最後一筆交易開始訂正
		loanCom.checkEraseFacmTxSeqNo(tFacMain, titaVo);
		try {
			facMainService.delete(tFacMain, titaVo);
		} catch (DBException e) {
			throw new LogicException(titaVo, "E0008", "額度主檔"); // 刪除資料時，發生錯誤
		}

		// 抓更新刪除額度後目前最後一筆額度 2022.4.13
		tFacMain = facMainService.findLastFacmNoFirst(wkCustNo, titaVo);
		if (tFacMain == null) {
			tCustMain.setLastFacmNo(0);
		} else {
			tCustMain.setLastFacmNo(tFacMain.getFacmNo());
		}
		try {
			custMainService.update(tCustMain, titaVo);
		} catch (DBException e) {
			throw new LogicException(titaVo, "E2010", "客戶資料主檔"); // 更新資料時，發生錯誤
		}

	}

	// 更新案件申請檔
	private void UpdateFacCaseApplRoutine() throws LogicException {
		this.info("UpdateFacCaseApplRoutine ...");

		if (!tFacCaseAppl.getCustUKey().trim().equals(wkCustUKey)) {
			this.info("CustUKey=" + wkCustUKey);
			this.info("ApplUKey=" + tFacCaseAppl.getCustUKey().trim());
			throw new LogicException(titaVo, "E2061", ""); // 統一編號與申請案件不符
		}
		if (tFacCaseAppl.getProcessCode().equals("1")) {
			throw new LogicException(titaVo, "E2064", ""); // 申請案件已核准
		}
		if (tFacCaseAppl.getProcessCode().equals("2")) {
			throw new LogicException(titaVo, "E2065", ""); // 申請案件已駁回
		}
		tFacCaseAppl.setProcessCode("1"); // 核准
		tFacCaseAppl.setApproveDate(iApproveDate);
		try {
			facCaseApplService.update(tFacCaseAppl, titaVo);
		} catch (DBException e) {
			throw new LogicException(titaVo, "E0007", "案件申請檔"); // 更新資料時，發生錯誤
		}
	}

	// 還原案件申請檔
	private void RestoredFacCaseApplRoutine() throws LogicException {
		this.info("RestoredFacCaseApplRoutine ...");

		tFacCaseAppl = facCaseApplService.holdById(wkApplNo, titaVo);
		if (tFacCaseAppl == null) {
			throw new LogicException(titaVo, "E0006", "案件申請檔 申請號碼 = " + wkApplNo); // 鎖定資料時，發生錯誤
		}
		tFacCaseAppl.setProcessCode(tTempVo.getParam("ProcessCode")); // 核准
		tFacCaseAppl.setApproveDate(this.parse.stringToInteger(tTempVo.getParam("ApproveDate")));
		try {
			facCaseApplService.update(tFacCaseAppl, titaVo);
		} catch (DBException e) {
			throw new LogicException(titaVo, "E0007", "案件申請檔"); // 更新資料時，發生錯誤
		}
	}

	// 新增交易暫存檔
	private void AddTxTempRoutine() throws LogicException {
		this.info("AddTxTempRoutine ...");
		tTxTemp = new TxTemp();
		tTxTempId = new TxTempId();
		loanCom.setTxTemp(tTxTempId, tTxTemp, wkCustNo, wkFacmNo, 0, 0, titaVo);
		tTempVo.clear();
		tTempVo.putParam("ApplNo", tFacCaseAppl.getApplNo());
		tTempVo.putParam("ProcessCode", tFacCaseAppl.getProcessCode());
		tTempVo.putParam("ApproveDate", tFacCaseAppl.getApproveDate());
		tTxTemp.setText(tTempVo.getJsonString());
		try {
			txTempService.insert(tTxTemp, titaVo);
		} catch (DBException e) {
			throw new LogicException(titaVo, "E0005", "交易暫存檔 Key = " + tTxTempId); // 新增資料時，發生錯誤
		}
	}

	// 更新階梯式利率
	private void UpdateFacProdStepRateRoutine() throws LogicException {
		this.info("UpdateFacProdStepRateRoutine ...");

		DeleteFacProdStepRateRoutine();
		// 更新階梯式利率
		FacProdStepRate tFacProdStepRate = new FacProdStepRate();

		for (int i = 1; i <= 10; i++) {
			this.info(("StepMonthS=" + titaVo.get("StepMonthS" + i)) + " ," + titaVo.get("StepRateCode" + i));
			if (titaVo.get("StepMonthS" + i) != null
					&& this.parse.stringToInteger(titaVo.getParam("StepMonthE" + i)) > 0) {
				tFacProdStepRate.setProdNo(sProdNo);
				tFacProdStepRate.setMonthStart(this.parse.stringToInteger(titaVo.getParam("StepMonthS" + i)));
				tFacProdStepRate.setFacProdStepRateId(
						new FacProdStepRateId(sProdNo, this.parse.stringToInteger(titaVo.getParam("StepMonthS" + i))));

				tFacProdStepRate.setMonthEnd(this.parse.stringToInteger(titaVo.getParam("StepMonthE" + i)));

				tFacProdStepRate.setRateType(titaVo.getParam("StepRateType" + i));
				tFacProdStepRate.setRateIncr(parse.stringToBigDecimal(titaVo.getParam("StepRateIncr" + i)));
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