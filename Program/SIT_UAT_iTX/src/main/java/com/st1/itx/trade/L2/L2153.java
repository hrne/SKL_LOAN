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
import com.st1.itx.util.common.GSeqCom;
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
/*
 * Tita
 * CustId=X,10
 * ApplNo=9,7
 * CustNo=9,7
 * FacmNo=9,3
 * ProdNo=X,5
 * BaseRateCode=9,2
 * RateIncr=+9,2.4
 * ApproveRate=9,2.4
 * RateCode=9,1
 * FirstRateAdjFreq=9,2
 * RateAdjFreq=9,2
 * CurrencyCode=X,3
 * TimApplAmt=9,14.2
 * AcctCode=9,3
 * ApproveDate=9,7
 * LoanTermYy=9,2
 * LoanTermMm=9,2
 * LoanTermDd=9,3
 * AmortizedCode=9,1
 * FreqBase=9,1
 * PayIntFreq=9,2
 * RepayFreq=9,2
 * UtilDeadline=9,7
 * TimDuePayAmt=9,14.2
 * TimDuePayLimit=9,14.2
 * TimPayIntLimit=9,14.2
 * GracePeriod=9,3
 * TimAcctFee=9,14.2
 * ExtraRepayCode=9,1
 * CustTypeCode=X,1
 * RuleCode=X,2
 * RecycleCode=9,1
 * RecycleDeadline=9,7
 * UsageCode=9,1
 * DepartmentCode=9,1
 * IncomeTaxFlag=9,1
 * CompensateFlag=9,1
 * IrrevocableFlag=9,1
 * RateAdjNoticeCode=9,1
 * RepayCode=9,2
 * RepayBank=9,3
 * RepayAcctNo=9,14
 * PostCode=X,1
 * Introducer=X,6
 * District=X,6
 * FireOfficer=X,6
 * Estimate=X,6
 * CreditOfficer=X,6
 * LoanOfficer=X,6
 * BusinessOfficer=X,6
 * Supervisor=X,6
 * Coorgnizer=X,6
 * AdvanceCloseCode=9,1
 * BreachCode=9,3
 * BreachGetCode=9,1
 * DecreaseFlag=X,1
 * GroupId=X,10
 * CopyCode=X,1
 * CreditScore=9,3
 * GuaranteeDate=9,7
 * ContractNo=X,10
 * RelationCode=X,2
 * RelationName=X,100
 * RelationId=X,10
 * RelationBirthday=9,7
 * RelationGender=X,1
 * AchAuthCode=X,1
 * AchBank=9,4
 * AchAuthNo=X,6
 * BreachaYyA1=9,1
 * BreachaYyB1=9,1
 * BreachaPercent1=9,1.2
 * BreachaYyA2=9,1
 * BreachaYyB2=9,1
 * BreachaPercent2=9,1.2
 * BreachaYyA3=9,1
 * BreachaYyB3=9,1
 * BreachaPercent3=9,1.2
 * BreachaYyA4=9,1
 * BreachaYyB4=9,1
 * BreachaPercent4=9,1.2
 * BreachaYyA5=9,1
 * BreachaYyB5=9,1
 * BreachaPercent5=9,1.2
 * BreachaYyA6=9,1
 * BreachaYyB6=9,1
 * BreachaPercent6=9,1.2
 * BreachaYyA7=9,1
 * BreachaYyB7=9,1
 * BreachaPercent7=9,1.2
 * BreachaYyA8=9,1
 * BreachaYyB8=9,1
 * BreachaPercent8=9,1.2
 * BreachaYyA9=9,1
 * BreachaYyB9=9,1
 * BreachaPercent9=9,1.2
 * BreachaYyA10=9,1
 * BreachaYyB10=9,1
 * BreachaPercent10=9,1.2
 * BreachbMmA1=9,2
 * BreachbMmB1=9,2
 * BreachbPercent1=9,1.2
 * BreachbMmA2=9,2
 * BreachbMmB2=9,2
 * BreachbPercent2=9,1.2
 * BreachbMmA3=9,2
 * BreachbMmB3=9,2
 * BreachbPercent3=9,1.2
 * BreachbMmA4=9,2
 * BreachbMmB4=9,2
 * BreachbPercent4=9,1.2
 * BreachbMmA5=9,2
 * BreachbMmB5=9,2
 * BreachbPercent5=9,1.2
 * BreachbMmA6=9,2
 * BreachbMmB6=9,2
 * BreachbPercent6=9,1.2
 * BreachbMmA7=9,2
 * BreachbMmB7=9,2
 * BreachbPercent7=9,1.2
 * BreachbMmA8=9,2
 * BreachbMmB8=9,2
 * BreachbPercent8=9,1.2
 * BreachbMmA9=9,2
 * BreachbMmB9=9,2
 * BreachbPercent9=9,1.2
 * BreachbMmA10=9,2
 * BreachbMmB10=9,2
 * BreachbPercent10=9,1.2
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
	GSeqCom gGSeqCom;
	@Autowired
	DateUtil dDateUtil;
	@Autowired
	BankAuthActCom bankAuthActCom;
	@Autowired
	public ClFacCom clFacCom;

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
			bankAuthActCom.acctCheck(titaVo);
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
				throw new LogicException(titaVo, "E0006", "案額度檔" + iApplNo); // 鎖定資料時，發生錯誤
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

				bankAuthActCom.acctCheck(titaVo);
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
		tFacMain.setBaseRateCode(titaVo.getParam("BaseRateCode"));
		tFacMain.setRateIncr(this.parse.stringToBigDecimal(titaVo.getParam("RateIncr")));
		tFacMain.setIndividualIncr(new BigDecimal("0"));
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
//	tFacMain.setDuePayAmt(BigDecimal.ZERO);
//	tFacMain.setDuePayLimit(BigDecimal.ZERO);
//	tFacMain.setPayIntLimit(BigDecimal.ZERO);
//	tFacMain.setCancelCode(0); // 預設0正常,該欄位需進維護交易L2154維護
		tFacMain.setGracePeriod(this.parse.stringToInteger(titaVo.getParam("GracePeriod")));
		tFacMain.setAcctFee(this.parse.stringToBigDecimal(titaVo.getParam("TimAcctFee")));
		tFacMain.setExtraRepayCode(titaVo.getParam("ExtraRepayCode"));
		tFacMain.setIntCalcCode(titaVo.getParam("IntCalcCode"));
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
		tFacMain.setPieceCode(tFacCaseAppl.getPieceCode());
		tFacMain.setRepayCode(this.parse.stringToInteger(titaVo.getParam("RepayCode")));
		tFacMain.setIntroducer(titaVo.getParam("Introducer"));
		tFacMain.setDistrict(titaVo.getParam("District"));
		tFacMain.setFireOfficer(titaVo.getParam("FireOfficer"));
		tFacMain.setEstimate(titaVo.getParam("Estimate"));
		tFacMain.setCreditOfficer(titaVo.getParam("CreditOfficer"));
		tFacMain.setLoanOfficer(titaVo.getParam("BusinessOfficer"));
		tFacMain.setBusinessOfficer(titaVo.getParam("BusinessOfficer"));
		tFacMain.setSupervisor(titaVo.getParam("Supervisor"));
		tFacMain.setInvestigateOfficer("");
		tFacMain.setEstimateReview("");
		tFacMain.setCoorgnizer(titaVo.getParam("Coorgnizer"));
//	tFacMain.setAdvanceCloseCode("00"); 
//		tFacMain.setBreachCode(titaVo.getParam("BreachCode")); // TODO DB刪除
//		tFacMain.setBreachGetCode(titaVo.getParam("BreachGetCode")); // TODO DB刪除
//		tFacMain.setDecreaseFlag(titaVo.getParam("DecreaseFlag")); // TODO DB刪除
//		tFacMain.setProhibityear(parse.stringToInteger(titaVo.getParam("Prohibityear"))); // TODO DB刪除
		// tFacMain.setGroupId(titaVo.getParam("GroupId"));
//	tFacMain.setCopyFlag(titaVo.getParam("CopyFlag"));
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
		if (tFacMain.getFacmNo() == tCustMain.getLastFacmNo()) {
			tCustMain.setLastFacmNo(tCustMain.getLastFacmNo() - 1);
			try {
				custMainService.update(tCustMain, titaVo);
			} catch (DBException e) {
				throw new LogicException(titaVo, "E2010", "客戶資料主檔"); // 更新資料時，發生錯誤
			}
		}
		// 額度交易訂正交易須由最後一筆交易開始訂正
		loanCom.checkEraseFacmTxSeqNo(tFacMain, titaVo);
		try {
			facMainService.delete(tFacMain, titaVo);
		} catch (DBException e) {
			throw new LogicException(titaVo, "E0008", "額度主檔"); // 刪除資料時，發生錯誤
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
//				if (i == 10) {
//					tFacProdStepRate.setMonthEnd(999);
//				} else {
//					if (this.parse.stringToInteger(titaVo.getParam("StepMonths" + (i + 1))) == 0) {
//						tFacProdStepRate.setMonthEnd(999);
//					} else {
				tFacProdStepRate.setMonthEnd(this.parse.stringToInteger(titaVo.getParam("StepMonthE" + i)));
//					}
//				}
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