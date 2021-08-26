package com.st1.itx.util.common;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/* 套件 */
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Slice;
//import org.springframework.stereotype.Service;
//import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/* 錯誤處理 */
import com.st1.itx.Exception.LogicException;
import com.st1.itx.Exception.DBException;
import com.st1.itx.dataVO.TempVo;
/* Tita & Tota 資料物件 */
//import com.st1.itx.dataVO.OccursList;
import com.st1.itx.dataVO.TitaVo;
//import com.st1.itx.dataVO.TotaVo;

/* DB容器 */
import com.st1.itx.db.domain.NegMain;
import com.st1.itx.db.domain.NegMainId;

import com.st1.itx.db.domain.NegTrans;
import com.st1.itx.db.domain.NegTransId;
//import com.st1.itx.db.domain.NegTransId;
import com.st1.itx.db.domain.TxTemp;
import com.st1.itx.db.domain.TxTempId;
import com.st1.itx.db.domain.NegAppr01;
import com.st1.itx.db.domain.NegAppr01Id;
//import com.st1.itx.db.domain.NegApprId;
import com.st1.itx.db.domain.CustMain;
import com.st1.itx.db.domain.JcicZ046;
import com.st1.itx.db.domain.JcicZ046Id;
import com.st1.itx.db.domain.JcicZ050;
import com.st1.itx.db.domain.JcicZ050Id;
import com.st1.itx.db.domain.JcicZ450;
import com.st1.itx.db.domain.JcicZ450Id;
import com.st1.itx.db.domain.JcicZ573;
import com.st1.itx.db.domain.JcicZ573Id;
import com.st1.itx.db.domain.AcReceivable;
import com.st1.itx.db.domain.CdBank;
//import com.st1.itx.db.domain.AcReceivableId;
import com.st1.itx.db.domain.CdBankId;
import com.st1.itx.db.domain.NegAppr02;
import com.st1.itx.db.domain.NegFinAcct;
//import com.st1.itx.db.domain.NegAppr02Id;
import com.st1.itx.db.domain.NegFinShare;
//import com.st1.itx.db.domain.NegFinShareId;
/* DB服務 */
import com.st1.itx.db.service.NegMainService;
import com.st1.itx.db.service.NegTransService;
import com.st1.itx.db.service.TxTempService;
import com.st1.itx.db.service.springjpa.cm.L5051ServiceImpl;
import com.st1.itx.db.service.springjpa.cm.L597AServiceImpl;
import com.st1.itx.tradeService.CommBuffer;
import com.st1.itx.db.service.NegApprService;
import com.st1.itx.db.service.NegFinAcctService;
import com.st1.itx.db.service.NegFinShareService;
import com.st1.itx.db.service.CustMainService;
import com.st1.itx.db.service.JcicZ046Service;
import com.st1.itx.db.service.JcicZ050Service;
import com.st1.itx.db.service.JcicZ573Service;
import com.st1.itx.db.service.JcicZ450Service;
import com.st1.itx.db.service.NegAppr01Service;
import com.st1.itx.db.service.AcReceivableService;
import com.st1.itx.db.service.CdBankService;
import com.st1.itx.db.service.NegAppr02Service;
import com.st1.itx.util.data.DataLog;
/* 交易共用組件 */
import com.st1.itx.util.date.DateUtil;
import com.st1.itx.util.parse.Parse;

/**
 * 使用到的地方 L5074 債務協商作業－應處理事項清單 L597A 整批處理 L5075 債務協商滯繳/應繳明細查詢
 * 
 * @author Jacky Lu
 */
@Component("negCom")
@Scope("prototype")
public class NegCom extends CommBuffer {
	/* DB服務注入 */
	@Autowired
	public NegMainService sNegMainService;

	@Autowired
	public NegTransService sNegTransService;

	@Autowired
	public NegApprService sNegApprService;

	@Autowired
	public CustMainService sCustMainService;

	@Autowired
	public AcReceivableService sAcReceivableService;

	@Autowired
	public NegAppr02Service sNegAppr02Service;

	@Autowired
	public NegAppr01Service sNegAppr01Service;

	@Autowired
	public NegFinShareService sNegFinShareService;

	@Autowired
	public NegFinAcctService sNegFinAcctService;

	@Autowired
	public JcicZ050Service sJcicZ050Service;
	@Autowired
	public JcicZ046Service sJcicZ046Service;
	@Autowired
	public JcicZ450Service sJcicZ450Service;
	@Autowired
	public JcicZ573Service sJcicZ573Service;

	@Autowired
	public TxTempService sTxTempService;

	@Autowired
	public L597AServiceImpl l597AServiceImpl;
	@Autowired
	public L5051ServiceImpl l5051ServiceImpl;

	@Autowired
	public GSeqCom gSeqCom;
	/* 日期工具 */
	@Autowired
	DateUtil dateUtil;
	@Autowired
	public CdBankService cdBankService;

	/* 轉型共用工具 */
	@Autowired
	Parse parse;
	@Autowired
	public DataLog dataLog;

	private String ConnectWord = ",";// Key值區分字串
	private int ChekUpdDB = 0;// 0:不異動 1:異動
	private String status = "";
	private String isMainFin = "";
	private String mainFinCode = "";
	private BigDecimal mainDueAmt = BigDecimal.ZERO;// 期金
	private String caseKindCode = "";
	private int mainTotalPeriod = 0;// 總期數
	private int mainRepaidPeriod = 0;// 已繳期數
	private BigDecimal mainAccuTempAmt = BigDecimal.ZERO;;// 累繳金額
	private BigDecimal mainAccuOverAmt = BigDecimal.ZERO;// 累溢收金額
	private BigDecimal mainAccuDueAmt = BigDecimal.ZERO;// 累應還金額
	private BigDecimal mainAccuSklShareAmt = BigDecimal.ZERO;;// 累計新壽攤分
	private BigDecimal mainIntRate = BigDecimal.ZERO;;// 利率
	private int remainPeriod = 0;// 剩餘期數

	private int mainFirstDueDate = 0;// 首次應繳日
	private int mainNextPayDate = 0;// 下次應繳日
	private int mainPayIntDate = 0;// 繳息迄日
	private int newStatusDate = 0;// 戶況日期

	private BigDecimal mainPrincipalBal = BigDecimal.ZERO;// 總本金餘額
	private BigDecimal mainRepayPrincipal = BigDecimal.ZERO;// 累償還本金
	private BigDecimal mainRepayInterest = BigDecimal.ZERO;// 累償還利息

	// NegTrans
	private int transTxStatus = 0;// 交易狀態 0:未入帳;1:待處理;2:已入帳;

	private String transTxKind = "";// 交易別 0:正常;1:溢繳;2:短繳;3:提前還本;4:結清;5:提前清償;6:待處理
	private BigDecimal transTxAmt = BigDecimal.ZERO;// 交易金額
	private BigDecimal orgTransPrincipalBal = BigDecimal.ZERO;// 本金餘額
	private BigDecimal transPrincipalBal = BigDecimal.ZERO;
	private BigDecimal transReturnAmt = BigDecimal.ZERO;// 退還金額
	private BigDecimal transSklShareAmt = BigDecimal.ZERO;// 新壽攤分
	private BigDecimal transApprAmt = BigDecimal.ZERO;// 撥付金額
	private int transExportDate = 0;// 撥付製檔日
	private int transExportAcDate = 0;// 撥付出帳日
	BigDecimal transTempRepayAmt = BigDecimal.ZERO;// 暫收抵繳金額
	BigDecimal transOverRepayAmt = BigDecimal.ZERO;// 溢收抵繳金額
	BigDecimal transPrincipalAmt = BigDecimal.ZERO;// 本金金額
	BigDecimal transInterestAmt = BigDecimal.ZERO;// 利息金額
	BigDecimal transOverAmt = BigDecimal.ZERO;// 轉入溢收金額
	private int transIntStartDate = 0;// 繳息起日
	private int transIntEndDate = 0;// 繳息迄日
	private int transRepayPeriod = 0;// 還款期數
	private int transRepayDate = 0;// 入帳還款日期

	private BigDecimal transOrgAccuOverAmt = BigDecimal.ZERO;// 累溢繳款(交易前)
	private BigDecimal transAccuOverAmt = BigDecimal.ZERO;// 累溢繳款(交易後)
	private int transShouldPayPeriod = 0;// 本次應還期數
	private BigDecimal transDueAmt = BigDecimal.ZERO;// 期金
	private BigDecimal newTransDueAmt = BigDecimal.ZERO;// 本次應還金額
	private int custNo = 0;
	private int caseSeq = 0;
	private String custId = "";
	private Map<String, String> mapNeg = new HashMap<String, String>();
	private CustMain tCustMain;
	NegTrans tNegTransUpd = new NegTrans();
	NegMain tNegMainUpd = new NegMain();

	private String Data[][] = {
			// RIM對應名稱,Map對應名稱,中文名稱,特殊規則
			{ "L5r03CustId", "CustId", "身份證字號", "" }, { "L5r03CaseSeq", "CaseSeq", "案件序號", "" },
			{ "L5r03CustNo", "CustNo", "戶號", "" }, { "L5r03CustName", "CustName", "戶名", "" },
			{ "L5r03Status", "Status", "戶況(舊)", "" }, { "L5r03NewStatus", "NewStatus", "戶況(新)", "" },
			{ "L5r03CustLoanKind", "CustLoanKind", "債權戶別", "" }, { "L5r03ApplDate", "ApplDate", "協商申請日", "" },
			{ "L5r03MainDueAmt", "MainDueAmt", "每期期款", "" }, { "L5r03TotalPeriod", "TotalPeriod", "總期數", "" },
			{ "L5r03IntRate", "IntRate", "每期利率", "" }, { "L5r03RepaidPeriod", "RepaidPeriod", "已繳期數(前)", "" },
			{ "L5r03NewRepaidPeriod", "NewRepaidPeriod", "已繳期數(後)", "" },
			{ "L5r03IsMainFin", "IsMainFin", "是否最大債權", "" }, { "L5r03MainFinCode", "MainFinCode", "最大債權機構", "" },
			{ "L5r03OrgPrincipalBal", "OrgPrincipalBal", "總本金餘額(前)", "" },
			{ "L5r03NewPrincipalBal", "NewPrincipalBal", "總本金餘額(後)", "" },
			{ "L5r03OrgAccuTempAmt", "OrgAccuTempAmt", "累繳金額(前)", "" },
			{ "L5r03NewAccuTempAmt", "NewAccuTempAmt", "累繳金額(後)", "" },
			{ "L5r03OrgAccuOverAmt", "OrgAccuOverAmt", "累溢收金額(前)", "" },
			{ "L5r03NewAccuOverAmt", "NewAccuOverAmt", "累溢收金額(後)", "" },
			{ "L5r03OrgAccuDueAmt", "OrgAccuDueAmt", "累應還金額(前)", "" },
			{ "L5r03NewAccuDueAmt", "NewAccuDueAmt", "累應還金額(後)", "" },
			{ "L5r03OrgAccuSklShareAmt", "OrgAccuSklShareAmt", "累新壽分攤金額(前)", "" },
			{ "L5r03NewAccuSklShareAmt", "NewAccuSklShareAmt", "累新壽分攤金額(後)", "" },
			{ "L5r03OrgNextPayDate", "OrgNextPayDate", "下次應繳日(前)", "" },
			{ "L5r03NewNextPayDate", "NewNextPayDate", "下次應繳日(後)", "" },
			{ "L5r03OrgRepayPrincipal", "OrgRepayPrincipal", "還本本金(前)", "" },
			{ "L5r03NewRepayPrincipal", "NewRepayPrincipal", "還本本金(後)", "" },
			{ "L5r03OrgRepayInterest", "OrgRepayInterest", "還本利息(前)", "" },
			{ "L5r03NewRepayInterest", "NewRepayInterest", "還本利息(後)", "" },
			{ "L5r03TwoStepCode", "TwoStepCode", "二階段註記", "" }, { "L5r03ChgCondDate", "ChgCondDate", "申請變更還款條件日", "" },
			{ "L5r03PayIntDate", "PayIntDate", "繳息迄日(前)", "" },
			{ "L5r03NewPayIntDate", "NewPayIntDate", "繳息迄日(後)", "" },
			{ "L5r03OrgStatusDate", "OrgStatusDate", "戶況日期(前)", "" },
			{ "L5r03NewStatusDate", "NewStatusDate", "戶況日期(後)", "" }, { "L5r03TransAcDate", "TransAcDate", "會計日期", "" },
			{ "L5r03TransTitaTlrNo", "transTitaTlrNo", "經辦", "" },
			{ "L5r03TransTitaTxtNo", "transTitaTxtNo", "交易序號", "" }, { "L5r03TransCustNo", "TransCustNo", "戶號", "" },
			{ "L5r03TransCaseSeq", "TransCaseSeq", "案件序號", "" },
			{ "L5r03TransEntryDate", "transEntryDate", "入帳日期", "" },
			{ "L5r03TransTxStatus", "transTxStatus", "交易狀態(前)", "" },
			{ "L5r03NewTransTxStatus", "NewtransTxStatus", "交易狀態(後)", "" },
			{ "L5r03TransTxKind", "transTxKind", "交易別(前)", "" },
			{ "L5r03NewTransTxKind", "NewtransTxKind", "交易別(後)", "" },
			{ "L5r03TransTxAmt", "transTxAmt", "交易金額(前)", "" },
			{ "L5r03NewTransTxAmt", "NewtransTxAmt", "交易金額(後)", "" },
			{ "L5r03TransPrincipalBal", "TransPrincipalBal", "本金餘額(前)", "" },
			{ "L5r03NewTransPrincipalBal", "NewTransPrincipalBal", "本金餘額(後)", "" },
			{ "L5r03TransReturnAmt", "TransReturnAmt", "退還金額(前)", "" },
			{ "L5r03NewTransReturnAmt", "NewTransReturnAmt", "退還金額(後)", "" },
			{ "L5r03TransSklShareAmt", "TransSklShareAmt", "攤分金額(前)", "" },
			{ "L5r03NewTransSklShareAmt", "NewTransSklShareAmt", "攤分金額(後)", "" },
			{ "L5r03TransApprAmt", "TransApprAmt", "撥付金額(前)", "" },
			{ "L5r03NewTransApprAmt", "NewTransApprAmt", "撥付金額(後)", "" },
			{ "L5r03TransExportDate", "transExportDate", "撥付製檔日", "" },
			{ "L5r03TransExportAcDate", "transExportAcDate", "撥付出帳日", "" },
			{ "L5r03TransTempRepayAmt", "transTempRepayAmt", "暫收抵繳金額(前)", "" },
			{ "L5r03NewTransTempRepayAmt", "NewtransTempRepayAmt", "暫收抵繳金額(後)", "" },
			{ "L5r03TransOverRepayAmt", "TransOverRepayAmt", "溢收抵繳金額(前)", "" },
			{ "L5r03NewTransOverRepayAmt", "NewTransOverRepayAmt", "溢收抵繳金額(後)", "" },
			{ "L5r03TransPrincipalAmt", "TransPrincipalAmt", "沖銷本金(前)", "" },
			{ "L5r03NewTransPrincipalAmt", "NewTransPrincipalAmt", "沖銷本金(後)", "" },
			{ "L5r03TransInterestAmt", "TransInterestAmt", "沖銷利息(前)", "" },
			{ "L5r03NewTransInterestAmt", "NewTransInterestAmt", "沖銷利息(後)", "" },
			{ "L5r03TransOverAmt", "TransOverAmt", "轉入溢收金額(前)", "" },
			{ "L5r03NewTransOverAmt", "NewTransOverAmt", "轉入溢收金額(後)", "" },
			{ "L5r03TransIntStartDate", "TransIntStartDate", "繳息起日(前)", "" },
			{ "L5r03NewTransIntStartDate", "NewTransIntStartDate", "繳息起日(後)", "" },
			{ "L5r03TransIntEndDate", "TransIntEndDate", "繳息迄日(前)", "" },
			{ "L5r03NewTransIntEndDate", "NewTransIntEndDate", "繳息迄日(後)", "" },
			{ "L5r03TransRepayPeriod", "TransRepayPeriod", "還款期數(前)", "" },
			{ "L5r03NewTransRepayPeriod", "NewTransRepayPeriod", "還款期數(後)", "" },
			{ "L5r03TransShouldPayPeriod", "TransShouldPayPeriod", "本次應還期數(前)", "" },
			{ "L5r03NewTransShouldPayPeriod", "NewTransShouldPayPeriod", "本次應還期數(後)", "" },
			{ "L5r03TransDueAmt", "TransDueAmt", "本次應還金額(前)", "" },
			{ "L5r03NewTransDueAmt", "NewTransDueAmt", "本次應還金額(後)", "" },
			{ "L5r03TransRepayDate", "TransRepayDate", "入帳還款日期(前)", "" }, // DateRocToAc
			{ "L5r03NewTransRepayDate", "NewTransRepayDate", "入帳還款日期(後)", "" }, // DateRocToAc
			{ "L5r03TransOrgAccuOverAmt", "TransOrgAccuOverAmt", "累溢繳款(交易後)(前)", "" },
			{ "L5r03NewTransOrgAccuOverAmt", "NewTransOrgAccuOverAmt", "累溢繳款(交易後)(後)", "" },
			{ "L5r03TransAccuOverAmt", "TransAccuOverAmt", "累溢繳款(交易後)(前)", "" },
			{ "L5r03NewTransAccuOverAmt", "NewTransAccuOverAmt", "累溢繳款(交易後)(後)", "" },
			{ "L5r03TrialFunc", "TrialFunc", "程式功能內部用代號", "" } };

	public NegCom() {

	}

	public String[][] getData() {
		return Data;
	}

	/* 設定第幾分頁 titaVo.getReturnIndex() 第一次會是0，如果需折返最後會塞值 */
	private int index = 0;
	/* 設定每筆分頁的資料筆數 預設500筆 總長不可超過六萬 */
	private Integer limit = Integer.MAX_VALUE;// 查全部

	/**
	 * 
	 * @param tNegTrans    債務協商交易檔
	 * @param iTrialFunc   試算是否為一開始的,還是後面修改後的調Rim 0:一開始試算 1:異動後 試算 2:UPDATE
	 * @param iNewTxKind 異動後交易別
	 * @param titaVo       TitaVo
	 * @return MapNeg
	 * @throws LogicException ..
	 */
	public Map<String, String> trialNegtrans(NegTrans tNegTrans, String iTrialFunc, String iNewTxKind, TitaVo titaVo)
			throws LogicException {
		ChekUpdDB = 0;// 0:不異動 1:異動
		if (("2").equals(iTrialFunc)) {
			ChekUpdDB = 1;
		}

		// int Today=dateUtil.getNowIntegerForBC();
		int Today = titaVo.getOrgEntdyI();// 會計日期
		this.info("NegtransTrial Today=[" + Today + "]");
//		Map<String, String> mapNeg = new HashMap<String, String>();
		// 1.得到NegTrans資料取用NegMains資料

		this.custNo = tNegTrans.getCustNo();
		this.caseSeq = tNegTrans.getCaseSeq();
		NegMainId tNegMainId = new NegMainId();
		tNegMainId.setCaseSeq(this.caseSeq);
		tNegMainId.setCustNo(this.custNo);

		NegMain tNegMain = new NegMain();
		tNegMain = sNegMainService.findById(tNegMainId, titaVo);
		tCustMain = sCustMainService.custNoFirst(custNo, custNo, titaVo);
		if (tCustMain == null) {
			throw new LogicException(titaVo, "E0001", "客戶資料主檔");
		}

		// 2.得到Main的資料
		if (tNegMain == null) {
			throw new LogicException(titaVo, "E0001", "債務協商案件主檔");
		}

		// NegMain
		status = tNegMain.getStatus();
		isMainFin = tNegMain.getIsMainFin();
		mainFinCode = tNegMain.getMainFinCode();
		mainDueAmt = tNegMain.getDueAmt();// 期金
		caseKindCode = tNegMain.getCaseKindCode();
		mainTotalPeriod = tNegMain.getTotalPeriod();// 總期數
		mainRepaidPeriod = tNegMain.getRepaidPeriod();// 已繳期數
		mainAccuTempAmt = tNegMain.getAccuTempAmt();// 累繳金額
		mainAccuOverAmt = tNegMain.getAccuOverAmt();// 累溢收金額
		mainAccuDueAmt = tNegMain.getAccuDueAmt();// 累應還金額
		mainAccuSklShareAmt = tNegMain.getAccuSklShareAmt();// 累計新壽攤分
		mainIntRate = tNegMain.getIntRate();// 利率\
		remainPeriod = mainTotalPeriod - mainRepaidPeriod;// 剩餘期數

		mainFirstDueDate = tNegMain.getFirstDueDate();// 首次應繳日
		mainNextPayDate = tNegMain.getNextPayDate();// 下次應繳日
		mainPayIntDate = tNegMain.getPayIntDate();// 繳息迄日
		newStatusDate = tNegMain.getStatusDate();// 戶況日期

		mainPrincipalBal = tNegMain.getPrincipalBal();// 總本金餘額
		if (mainPrincipalBal.compareTo(BigDecimal.ZERO) == 0) {
			mainPrincipalBal = tNegMain.getTotalContrAmt();
		}
		mainRepayPrincipal = tNegMain.getRepayPrincipal();// 累償還本金
		mainRepayInterest = tNegMain.getRepayInterest();// 累償還利息

		// NegTrans
		transTxStatus = tNegTrans.getTxStatus();// 交易狀態 0:未入帳;1:待處理;2:已入帳;

		transTxKind = tNegTrans.getTxKind();// 交易別 0:正常;1:溢繳;2:短繳;3:提前還本;4:結清;5:提前清償;6:待處理
		transTxAmt = tNegTrans.getTxAmt();// 交易金額
		orgTransPrincipalBal = tNegTrans.getPrincipalBal();// 本金餘額
		if (orgTransPrincipalBal.compareTo(BigDecimal.ZERO) == 0) {
			orgTransPrincipalBal = mainPrincipalBal;
		}
		transPrincipalBal = BigDecimal.ZERO;
		transReturnAmt = tNegTrans.getReturnAmt();// 退還金額
		transSklShareAmt = tNegTrans.getSklShareAmt();// 新壽攤分
		transApprAmt = tNegTrans.getApprAmt();// 撥付金額
		transExportDate = tNegTrans.getExportDate();// 撥付製檔日
		transExportAcDate = tNegTrans.getExportAcDate();// 撥付出帳日
		transTempRepayAmt = tNegTrans.getTempRepayAmt();// 暫收抵繳金額
		transOverRepayAmt = tNegTrans.getOverRepayAmt();// 溢收抵繳金額
		transPrincipalAmt = tNegTrans.getPrincipalAmt();// 本金金額
		transInterestAmt = tNegTrans.getInterestAmt();// 利息金額
		transOverAmt = tNegTrans.getOverAmt();// 轉入溢收金額
		transIntStartDate = tNegTrans.getIntStartDate();// 繳息起日
		transIntEndDate = tNegTrans.getIntEndDate();// 繳息迄日
		transRepayPeriod = tNegTrans.getRepayPeriod();// 還款期數
		transRepayDate = tNegTrans.getRepayDate();// 入帳還款日期

		transOrgAccuOverAmt = tNegMain.getAccuOverAmt();// 累溢繳款(交易前)
		transAccuOverAmt = tNegTrans.getAccuOverAmt();// 累溢繳款(交易後)
		transShouldPayPeriod = tNegTrans.getShouldPayPeriod();// 本次應還期數
		transDueAmt = tNegTrans.getDueAmt();// 期金
		newTransDueAmt = BigDecimal.ZERO;// 本次應還金額
		
		if (("3").equals(status)) {
			throw new LogicException(titaVo, "E5009", "債務協商案件已結案");
		}
		
		if (mainDueAmt.compareTo(BigDecimal.ZERO) == 0) {
			// E5009 資料檢核錯誤
			throw new LogicException(titaVo, "E5009", "[期金]為0");
		}

		if (transDueAmt.compareTo(BigDecimal.ZERO) == 0) {
			// 期金
			transDueAmt = mainDueAmt;

		}

		// 用日期去算到今天這戶頭應該還幾期
		// NextPayDate 下次應繳日 與本次會計日相比差了多少個月
		if (mainNextPayDate == 0) {
			// E5009 資料檢核錯誤
			throw new LogicException(titaVo, "E5009", "[下次應繳日]未填寫.請至L5071查詢後維護.");
		} else {
			if (mainNextPayDate > Today) {
				transShouldPayPeriod = 0;
			} else {
				transShouldPayPeriod = DiffMonth(1, mainNextPayDate, Today) + 1;// 月份差異
			}
		}
		// -----
		if (transShouldPayPeriod != 0) {
			newTransDueAmt = mainDueAmt.multiply(BigDecimal.valueOf(transShouldPayPeriod));// Trans 本次應還金額
		}

		int DiffMonth = DiffMonth(1, mainFirstDueDate, Today) + 1;// 月份差異=首次應繳日 與 會計日的月份差+1
		mainAccuDueAmt = mainDueAmt.multiply(BigDecimal.valueOf(DiffMonth));// Main累應還金額=Main期金*Trans本次應還期數

		// transTxKind 0:正常;1:溢繳;2:短繳;3:提前還本;4:結清;5:提前清償;6:待處理
		// 0:正常-匯入款＋溢收款 >= 期款
		// 1:溢繳(預收多期)-匯入款 > 期款
		// 2:短繳-匯入款＋溢收款 < 期款
		// 3:提前還本-匯入款 >= 5期期款
		// 4:結清-匯入款＋溢收款 >=最後一期期款
		// 5:提前清償-匯入款＋溢收款 >= 剩餘期款
		// 6:待處理
		// ※入帳訂正需在撥付產擋前，依反向順序訂正

		// String OringnalTxKind=transTxKind;
		transTxKind = "";

		// 可抵繳金額=交易金額+累溢收
		BigDecimal CanCountAmt = transTxAmt.add(mainAccuOverAmt);

		// 剩餘本利和=總本金餘額(1+年利率/12/100) ,依期數計算
		BigDecimal sumInterest = calAccuDueAmt(tNegMain, transShouldPayPeriod , 1);//最後一位參數傳1代表計算利息加總
		BigDecimal CloseAmt = mainPrincipalBal.add(sumInterest);

		// 剩餘本利和<=可分配金額
		if (CloseAmt.compareTo(CanCountAmt) <= 0) {
			// 5:提前清償
			// 4:結清
			transRepayPeriod = remainPeriod;

			// 剩餘期數>1
			if (remainPeriod > 1) {
				// 5:提前清償
				transTxKind = "5";
				transRepayPeriod = transShouldPayPeriod;//計算幾期利息
			} else {
				// 4:結清
				transTxKind = "4";
			}
		} else {
			// 還款期數
			transRepayPeriod = Integer.parseInt(String
					.valueOf(CanCountAmt.divide(mainDueAmt, 2, RoundingMode.DOWN).setScale(0, RoundingMode.DOWN)));

			// 3:提前還本-匯入款 >= 5期期款
			if (transTxAmt.compareTo(mainDueAmt.multiply(new BigDecimal(5)).setScale(0, RoundingMode.UP)) >= 0) {
				transTxKind = "3";
			}
			// 1:溢繳(預收多期)-匯入款 > 期款
			else if (transTxAmt.compareTo(mainDueAmt) > 0) {
				transTxKind = "1";
			}
			// 0:正常-匯入款＋溢收款 >= 期款
			else if (CanCountAmt.compareTo(mainDueAmt) >= 0) {
				transTxKind = "0";
			}
			// 2:短繳-匯入款＋溢收款 < 期款
			else {
				transTxKind = "2";
			}

		}

		this.info("NegCom transTxKind=[" + transTxKind + "],NewTxKind=[" + iNewTxKind + "]");
		// 0:一開始試算 1:異動後 試算 2:UPDATE
		// USER 自己輸入 交易別(提前還本可改為溢繳)
		if (("1").equals(iTrialFunc) || ("2").equals(iTrialFunc)) {
			if (!iNewTxKind.equals(transTxKind)) {
				transTxKind = iNewTxKind;
			}
		}
		this.info("NegCom transTxKind After Trial=[" + transTxKind + "]");

		// 還本息金額 = 本金+利息
		BigDecimal rePayAmt = BigDecimal.ZERO;
		transReturnAmt = BigDecimal.ZERO;// 退還金額
		switch (transTxKind) {
		case "0": // 0:正常
			rePayAmt = mainDueAmt;
			transRepayPeriod = 1;// 還款期數
			break;
		case "1": // 1:溢繳(預收多期)
			rePayAmt = mainDueAmt.multiply(new BigDecimal(transRepayPeriod));
			break;
		case "2": // 2:短繳
			rePayAmt = BigDecimal.ZERO;
			transRepayPeriod = 0;// 還款期數
			break;
		case "3": // 3:提前還本
			rePayAmt = CanCountAmt;
			transRepayPeriod = transShouldPayPeriod;//計算幾期利息
			break;

		case "4": // 4:結清-匯入款＋溢收款 >=最後一期期款
		case "5": // 5:提前清償-匯入款＋溢收款 >= 剩餘期款
			status = "3";
			newStatusDate = Today;
			rePayAmt = CloseAmt;
			transReturnAmt = CanCountAmt.subtract(CloseAmt); // 退還金額
			mainNextPayDate = 0; 
			break;
		}
		transTxStatus = 2;// 交易狀態 0:未入帳;1:待處理;2:已入帳;
		transRepayDate = Today; // 入帳還款日

		// 交易金額 > 還款金額
		if (transTxAmt.compareTo(rePayAmt) > 0) {
			transTempRepayAmt = rePayAmt;// 暫收抵繳金額 = 還款金額
			transOverRepayAmt = BigDecimal.ZERO;// 溢收抵繳金額
			transOverAmt = transTxAmt.subtract(rePayAmt).subtract(transReturnAmt);// 轉入溢收金額= 暫收金額減還款金額減退還金額，為正時
		} else {
			transTempRepayAmt = transTxAmt;// 暫收抵繳金額 = 暫收金額
			transOverRepayAmt = rePayAmt.subtract(transTxAmt); // 溢收抵繳金額 = 暫收金額減還款金額，為負時
			transOverAmt = BigDecimal.ZERO;// 轉入溢收金額
		}

		mainAccuTempAmt = mainAccuTempAmt.add(transTxAmt);// Main累繳金額
		mainAccuOverAmt = mainAccuOverAmt.add(transOverAmt).subtract(transOverRepayAmt); // Main累溢收金額=Main累溢收金額+轉入溢收金額-溢收抵繳金額
		mainAccuOverAmt = mainAccuOverAmt.subtract(transReturnAmt); // 累溢收金額減退還金額
		transSklShareAmt = BigDecimal.ZERO; // 新壽攤分
		transApprAmt = BigDecimal.ZERO;// 撥付金額
		// 最大債權分配
		if ("Y".equals(tNegMain.getIsMainFin())) {
			updNegFinShare(tNegTrans, tNegMain, transTxAmt.subtract(transReturnAmt), titaVo);
		} else {
			transSklShareAmt = transTxAmt;
		}

		// 計算本利(非短繳)
		if (!"2".equals(transTxKind)) {
			calInterestAmt(rePayAmt, transRepayPeriod , titaVo);
		}

		mainRepayPrincipal = mainRepayPrincipal.add(transPrincipalAmt);// 還本本金
		mainRepayInterest = mainRepayInterest.add(transInterestAmt); // 還本利息
		mainPrincipalBal = mainPrincipalBal.subtract(transPrincipalAmt);
		if (mainPrincipalBal.compareTo(BigDecimal.ZERO) < 0) {
			throw new LogicException(titaVo, "E5009", "[本金餘額]不可為負值.");// E5009 資料檢核錯誤
		}
		mainAccuSklShareAmt = mainAccuSklShareAmt.add(transSklShareAmt);
		transPrincipalBal = mainPrincipalBal;// 本金餘額
		transAccuOverAmt = mainAccuOverAmt;// 累溢繳款(交易後)
		
		if (mainPayIntDate == 0) {
			mainPayIntDate = getRepayDate(mainFirstDueDate, -1, titaVo);
		}
		if (transRepayPeriod > 0) {
			transIntStartDate = mainPayIntDate; // 繳息起日 = 繳息迄日(主檔)
			transIntEndDate = getRepayDate(mainPayIntDate, transRepayPeriod, titaVo);
			mainPayIntDate = transIntEndDate; // 新繳息起日
			mainNextPayDate = getRepayDate(mainPayIntDate, 1, titaVo); // 計算新下次繳款日
		}

		if ("3".equals(status)) {	//結案
			transRepayPeriod = remainPeriod;
			transIntEndDate = tNegMain.getLastDueDate();
			mainPayIntDate = transIntEndDate;
			mainNextPayDate = 0;	// 下次應繳日 歸零
			mainAccuDueAmt = mainAccuTempAmt.subtract(transReturnAmt);//結清時等於累繳金額減退還金額
		}

		mainRepaidPeriod = mainRepaidPeriod + transRepayPeriod;// 已繳期數
		
		// 更新資料庫
		if (ChekUpdDB == 1) {
			updateDB(tNegMain, tNegTrans, titaVo);
			this.info("ChekUpdDB 1");
		}

		// 4.更新jcic報送檔
		if (ChekUpdDB == 1 && "Y".equals(tNegMain.getIsMainFin())) {
			updateJcic(tNegMainUpd, tNegTransUpd, titaVo);
			this.info("ChekUpdDB 2");
		}
		// put map
		mapNegPut(iTrialFunc, tNegTrans, tNegMain, titaVo);
		this.info("mapNegPut Done");
		// end
		return mapNeg;
	}

	// 計算本利
	public void calInterestAmt(BigDecimal rePayAmt, int iLoanTerm, TitaVo titaVo) throws LogicException {
		this.info("NegCom iLoanTerm=[" + iLoanTerm + "]");
		BigDecimal balance = mainPrincipalBal;// 剩餘本金
		BigDecimal interest = BigDecimal.ZERO;
		BigDecimal principal = BigDecimal.ZERO;

		int count = 0;
		for (int i = 0; i < iLoanTerm; i++) {
			count++;
			if (balance.compareTo(BigDecimal.ZERO) <= 0) {
				break;
			}
			// iDueAmt 期金
			interest = (balance.multiply(mainIntRate)).divide(new BigDecimal(1200), 0, RoundingMode.HALF_UP);// 應繳利息
			principal = mainDueAmt.subtract(interest);
			balance = balance.subtract(principal);
			transInterestAmt = transInterestAmt.add(interest);// 利息金額=應繳利息
			this.info("NegCom Count=" + count + " ,principal=" + principal + " ,interest=" + interest + " , balance=" + balance);
		}

		if (rePayAmt.compareTo(transPrincipalAmt.add(transInterestAmt)) < 0) {
			throw new LogicException(titaVo, "E0013",
					"還款金額計算有誤" + rePayAmt + "," + transPrincipalAmt + "," + transInterestAmt);// 程式邏輯有誤
		}

		// 本金金額 = 還本息金額 - 利息金額
		transPrincipalAmt = rePayAmt.subtract(transInterestAmt);

	}

	// 更新DB
	public void updateDB(NegMain tNegMain, NegTrans tNegTrans, TitaVo titaVo) throws LogicException {

		tNegTransUpd = sNegTransService.holdById(tNegTrans, titaVo);
		if (tNegTransUpd == null) {
			throw new LogicException(titaVo, "E0007", "債務協商交易檔");// 發生錯誤
		}
		NegTrans tNegTransOrg = (NegTrans) dataLog.clone(tNegTransUpd);// 資料異動前
		tNegTransUpd.setTxStatus(transTxStatus);// 交易狀態0:未入帳;1:待處理;2:已入帳;
		tNegTransUpd.setTxKind(transTxKind);// 交易別0:正常;1:溢繳;2:短繳;3:提前還本;4:結清;5:提前清償;6:待處理
		tNegTransUpd.setTxAmt(transTxAmt);// 交易金額
		tNegTransUpd.setPrincipalBal(transPrincipalBal);// 本金餘額
		tNegTransUpd.setReturnAmt(transReturnAmt);// 退還金額
		tNegTransUpd.setSklShareAmt(transSklShareAmt);// 新壽攤分
		tNegTransUpd.setApprAmt(transApprAmt);// 撥付金額
		tNegTransUpd.setExportDate(transExportDate);// 撥付製檔日
		tNegTransUpd.setExportAcDate(transExportAcDate);// 撥付出帳日
		tNegTransUpd.setTempRepayAmt(transTempRepayAmt);// 暫收抵繳金額
		tNegTransUpd.setOverRepayAmt(transOverRepayAmt);// 溢收抵繳金額
		tNegTransUpd.setPrincipalAmt(transPrincipalAmt);// 本金金額
		tNegTransUpd.setInterestAmt(transInterestAmt);// 利息金額
		tNegTransUpd.setOverAmt(transOverAmt);// 轉入溢收金額
		tNegTransUpd.setIntStartDate(transIntStartDate);// 繳息起日
		tNegTransUpd.setIntEndDate(transIntEndDate);// 繳息迄日
		tNegTransUpd.setRepayPeriod(transRepayPeriod);// 還款期數
		tNegTransUpd.setRepayDate(transRepayDate);// 入帳還款日期
		tNegTransUpd.setOrgAccuOverAmt(transOrgAccuOverAmt);// 累溢繳款(交易前)
		tNegTransUpd.setAccuOverAmt(transAccuOverAmt);// 累溢繳款(交易後)
		tNegTransUpd.setShouldPayPeriod(transShouldPayPeriod);// 本次應還期數
		tNegTransUpd.setDueAmt(transDueAmt);// 期金
		String thisSeqNo = tNegTransUpd.getThisSeqNo();
		String thisSeqNoAdd = "";
		if (thisSeqNo != null && thisSeqNo.length() != 0) {
			thisSeqNoAdd = String.valueOf(Integer.parseInt(thisSeqNo) + 1);
		} else {
			// Insert
			thisSeqNo = "0";
			thisSeqNoAdd = "0";
		}
		tNegTransUpd.setLastEntdy(tNegTransUpd.getThisEntdy());// 上次交易日
		tNegTransUpd.setLastKinbr(tNegTransUpd.getThisKinbr());// 上次分行別
		tNegTransUpd.setLastTlrNo(tNegTransUpd.getThisTlrNo());// 上次交易員代號
		tNegTransUpd.setLastTxtNo(tNegTransUpd.getThisTxtNo());// 上次交易序號
		tNegTransUpd.setLastSeqNo(thisSeqNo);// 上次序號

		tNegTransUpd.setThisEntdy(titaVo.getEntDyI());// 本次交易日
		tNegTransUpd.setThisKinbr(titaVo.getKinbr());// 本次分行別
		tNegTransUpd.setThisTlrNo(titaVo.getTlrNo());// 本次交易員代號
		tNegTransUpd.setThisTxtNo(titaVo.getTxtNo());// 本次交易序號
		tNegTransUpd.setThisSeqNo(thisSeqNoAdd);// 本次序號
		try {
			sNegTransService.update(tNegTransUpd, titaVo);
		} catch (DBException e) {
			throw new LogicException(titaVo, "E0007", e.getErrorMsg()); // 更新資料時，發生錯誤
		}

		tNegMainUpd = sNegMainService.holdById(tNegMain, titaVo);
		NegMain tNegMainOrg = (NegMain) dataLog.clone(tNegMainUpd);// 資料異動前
		tNegMainUpd.setPrincipalBal(mainPrincipalBal);// 總本金餘額
		tNegMainUpd.setRepaidPeriod(mainRepaidPeriod);// 已繳期數
		tNegMainUpd.setAccuTempAmt(mainAccuTempAmt);// 累繳金額
		tNegMainUpd.setAccuOverAmt(mainAccuOverAmt);// 累溢收金額
		tNegMainUpd.setAccuDueAmt(mainAccuDueAmt);// 累應還金額
		tNegMainUpd.setNextPayDate(mainNextPayDate);// 下次應繳日
		tNegMainUpd.setPayIntDate(mainPayIntDate);// 繳息迄日
		tNegMainUpd.setRepayPrincipal(mainRepayPrincipal);// 還本本金
		tNegMainUpd.setRepayInterest(mainRepayInterest);// 還本利息
		tNegMainUpd.setAccuSklShareAmt(mainAccuSklShareAmt);// 累新壽攤分
		tNegMainUpd.setStatus(status);// 戶況
		tNegMainUpd.setThisAcDate(titaVo.getEntDyI());// AcDate 本次會計日期
		tNegMainUpd.setThisTitaTlrNo(titaVo.getTlrNo());// TitaTlrNo 本次經辦
		tNegMainUpd.setThisTitaTxtNo(parse.stringToInteger(titaVo.getTxtNo()));// TitaTxtNo 本次交易序號
		tNegMainUpd.setLastAcDate(tNegMainOrg.getThisAcDate());// LastAcDate 上次會計日期
		tNegMainUpd.setLastTitaTlrNo(tNegMainOrg.getThisTitaTlrNo());// LastTitaTlrNo 上次經辦
		tNegMainUpd.setLastTitaTxtNo(tNegMainOrg.getThisTitaTxtNo());// LastTitaTxtNo 上次交易序號
		try {
			sNegMainService.update(tNegMainUpd, titaVo);
		} catch (DBException e) {
			throw new LogicException(titaVo, "E0007", e.getErrorMsg()); // 更新資料時，發生錯誤
		}

		// Insert Temp
		insertTxTemp(titaVo, tNegMainOrg, tNegTransOrg);

	}

	public void mapNegPut(String iTrialFunc, NegTrans tNegTrans, NegMain tNegMain, TitaVo titaVo)
			throws LogicException {
		mapNeg.put("CustId", tCustMain.getCustId());
		mapNeg.put("CaseSeq", String.valueOf(tNegMain.getCaseSeq()));
		mapNeg.put("CustNo", String.valueOf(custNo));
		mapNeg.put("CustName", tCustMain.getCustName());
		mapNeg.put("Status", status);
		mapNeg.put("NewStatus", status);
		mapNeg.put("CustLoanKind", tNegMain.getCustLoanKind());
		mapNeg.put("ApplDate", String.valueOf(tNegMain.getApplDate()));// 協商申請日
		mapNeg.put("MainDueAmt", String.valueOf(tNegMain.getDueAmt()));
		mapNeg.put("TotalPeriod", String.valueOf(tNegMain.getTotalPeriod()));
		mapNeg.put("IntRate", String.valueOf(mainIntRate));
		mapNeg.put("RepaidPeriod", String.valueOf(tNegMain.getRepaidPeriod()));// 已繳期數
		mapNeg.put("NewRepaidPeriod", String.valueOf(mainRepaidPeriod));// 已繳期數

		mapNeg.put("IsMainFin", isMainFin);
		mapNeg.put("MainFinCode", mainFinCode);

		mapNeg.put("OrgPrincipalBal", String.valueOf(tNegMain.getPrincipalBal()));// 總本金餘額(舊)
		mapNeg.put("NewPrincipalBal", String.valueOf(mainPrincipalBal));// 總本金餘額(新)
		this.info("NegCom MainPrincipalBal=[" + mainPrincipalBal + "]-5");
		mapNeg.put("OrgAccuTempAmt", String.valueOf(tNegMain.getAccuTempAmt()));// 累繳金額
		mapNeg.put("NewAccuTempAmt", String.valueOf(mainAccuTempAmt));

		mapNeg.put("OrgAccuOverAmt", String.valueOf(tNegMain.getAccuOverAmt()));// 累溢收金額
		mapNeg.put("NewAccuOverAmt", String.valueOf(mainAccuOverAmt));

		mapNeg.put("OrgAccuDueAmt", String.valueOf(tNegMain.getAccuDueAmt()));// 累應還金額
		mapNeg.put("NewAccuDueAmt", String.valueOf(mainAccuDueAmt));

		mapNeg.put("OrgAccuSklShareAmt", String.valueOf(tNegMain.getAccuSklShareAmt()));// 新壽攤分
		mapNeg.put("NewAccuSklShareAmt", String.valueOf(mainAccuSklShareAmt));

		mapNeg.put("OrgNextPayDate", String.valueOf(tNegMain.getNextPayDate()));// 下期應繳日
		mapNeg.put("NewNextPayDate", String.valueOf(DcToRoc(mainNextPayDate)));//

		mapNeg.put("OrgRepayPrincipal", String.valueOf(tNegMain.getRepayPrincipal()));// 還本本金
		mapNeg.put("NewRepayPrincipal", String.valueOf(mainRepayPrincipal));

		mapNeg.put("OrgRepayInterest", String.valueOf(tNegMain.getRepayInterest()));// 還本利息
		mapNeg.put("NewRepayInterest", String.valueOf(mainRepayInterest));

		mapNeg.put("TwoStepCode", String.valueOf(tNegMain.getTwoStepCode()));
		mapNeg.put("ChgCondDate", String.valueOf(tNegMain.getChgCondDate()));

		mapNeg.put("PayIntDate", String.valueOf(tNegMain.getPayIntDate()));// 繳息迄日
		mapNeg.put("NewPayIntDate", String.valueOf(DcToRoc(mainPayIntDate)));// 繳息迄日

		mapNeg.put("OrgStatusDate", String.valueOf(tNegMain.getStatusDate()));// 戶況日期
		mapNeg.put("NewStatusDate", String.valueOf(DcToRoc(newStatusDate)));
		mapNeg.put("TransAcDate", String.valueOf(tNegTrans.getAcDate()));
		mapNeg.put("transTitaTlrNo", String.valueOf(tNegTrans.getTitaTlrNo()));
		mapNeg.put("transTitaTxtNo", String.valueOf(tNegTrans.getTitaTxtNo()));
		mapNeg.put("TransCustNo", String.valueOf(custNo));
		mapNeg.put("TransCaseSeq", String.valueOf(tNegTrans.getCaseSeq()));
		mapNeg.put("transEntryDate", String.valueOf(tNegTrans.getEntryDate()));// 入帳日期

		mapNeg.put("transTxStatus", String.valueOf(tNegTrans.getTxStatus()));// 交易狀態
		mapNeg.put("NewtransTxStatus", String.valueOf(transTxStatus));// 交易狀態0:未入帳;1:待處理;2:已入帳;

		mapNeg.put("transTxKind", String.valueOf(tNegTrans.getTxKind()));// 交易別0:正常;1:溢繳;2:短繳;3:提前還本;4:結清;5:提前清償;6:待處理
		mapNeg.put("NewtransTxKind", String.valueOf(transTxKind));

		mapNeg.put("transTxAmt", String.valueOf(tNegTrans.getTxAmt()));// 交易金額
		mapNeg.put("NewtransTxAmt", String.valueOf(transTxAmt));

		mapNeg.put("TransPrincipalBal", String.valueOf(orgTransPrincipalBal));// 本金餘額
		mapNeg.put("NewTransPrincipalBal", String.valueOf(transPrincipalBal));

		mapNeg.put("TransReturnAmt", String.valueOf(tNegTrans.getReturnAmt()));// 退還金額
		mapNeg.put("NewTransReturnAmt", String.valueOf(transReturnAmt));

		mapNeg.put("TransSklShareAmt", String.valueOf(tNegTrans.getSklShareAmt()));// 新壽攤分
		mapNeg.put("NewTransSklShareAmt", String.valueOf(transSklShareAmt));

		mapNeg.put("TransApprAmt", String.valueOf(tNegTrans.getApprAmt()));// 撥付金額
		mapNeg.put("NewTransApprAmt", String.valueOf(transApprAmt));

		mapNeg.put("transExportDate", String.valueOf(tNegTrans.getExportDate()));// 撥付製檔日
		// MapNeg.put("NewtransExportDate",String.valueOf(DcToRoc(transExportDate)));--

		mapNeg.put("transExportAcDate", String.valueOf(tNegTrans.getExportAcDate()));// 撥付出帳日
		// MapNeg.put("NewtransExportAcDate",String.valueOf(DcToRoc(transExportAcDate)));

		mapNeg.put("transTempRepayAmt", String.valueOf(tNegTrans.getTempRepayAmt()));// 暫收抵繳金額
		mapNeg.put("NewtransTempRepayAmt", String.valueOf(transTempRepayAmt));

		mapNeg.put("TransOverRepayAmt", String.valueOf(tNegTrans.getOverRepayAmt()));// 溢收抵繳金額
		mapNeg.put("NewTransOverRepayAmt", String.valueOf(transOverRepayAmt));

		mapNeg.put("TransPrincipalAmt", String.valueOf(tNegTrans.getPrincipalAmt()));// 本金金額
		mapNeg.put("NewTransPrincipalAmt", String.valueOf(transPrincipalAmt));

		mapNeg.put("TransInterestAmt", String.valueOf(tNegTrans.getInterestAmt()));// 利息金額
		mapNeg.put("NewTransInterestAmt", String.valueOf(transInterestAmt));

		mapNeg.put("TransOverAmt", String.valueOf(tNegTrans.getOverAmt()));// 轉入溢收金額
		mapNeg.put("NewTransOverAmt", String.valueOf(transOverAmt));

		mapNeg.put("TransIntStartDate", String.valueOf(tNegTrans.getIntStartDate()));// 繳息起日
		mapNeg.put("NewTransIntStartDate", String.valueOf(DcToRoc(transIntStartDate)));

		mapNeg.put("TransIntEndDate", String.valueOf(tNegTrans.getIntEndDate()));// 繳息迄日
		mapNeg.put("NewTransIntEndDate", String.valueOf(DcToRoc(transIntEndDate)));

		mapNeg.put("TransRepayPeriod", String.valueOf(tNegTrans.getRepayPeriod()));// 還款期數
		mapNeg.put("NewTransRepayPeriod", String.valueOf(transRepayPeriod));

		mapNeg.put("TransShouldPayPeriod", String.valueOf(tNegTrans.getShouldPayPeriod()));// 本次應還期數
		mapNeg.put("NewTransShouldPayPeriod", String.valueOf(transShouldPayPeriod));

		mapNeg.put("TransDueAmt", String.valueOf(tNegTrans.getDueAmt()));// 期金
		mapNeg.put("NewTransDueAmt", String.valueOf(newTransDueAmt));

		mapNeg.put("TransRepayDate", String.valueOf(tNegTrans.getRepayDate()));// 入帳還款日期
		mapNeg.put("NewTransRepayDate", String.valueOf(transRepayDate));

		mapNeg.put("TransOrgAccuOverAmt", String.valueOf(tNegMain.getAccuOverAmt()));// 累溢繳款(交易前)(前)
		mapNeg.put("NewTransOrgAccuOverAmt", String.valueOf(transOrgAccuOverAmt));// 累溢繳款(交易前)(後)
		mapNeg.put("TransAccuOverAmt", String.valueOf(tNegTrans.getAccuOverAmt()));// 累溢繳款(交易後)(前)
		mapNeg.put("NewTransAccuOverAmt", String.valueOf(transAccuOverAmt));// 累溢繳款(交易後)(後)

		mapNeg.put("Trial", iTrialFunc);

	}

	// 最大債權分配
	public void updNegFinShare(NegTrans tNegTrans, NegMain tNegMain, BigDecimal shareAmSum, TitaVo titaVo)
			throws LogicException {
		Slice<NegFinShare> slNegFinShare = sNegFinShareService.FindAllFinCode(tNegTrans.getCustNo(),
				tNegTrans.getCaseSeq(), this.index, this.limit, titaVo);
		if (slNegFinShare == null) {
			throw new LogicException(titaVo, "E0001", "債務協商債權分攤檔"); // 查詢資料不存在
		}

		List<NegFinShare> lNegFinShare = slNegFinShare == null ? null : slNegFinShare.getContent();
		BigDecimal shareAmtRemaind = shareAmSum;
		BigDecimal shareAmt = BigDecimal.ZERO;
		NegFinShare tNegFinShare = new NegFinShare();
		this.info("lNegFinShare==" + lNegFinShare);
		if (titaVo.isHcodeNormal()) {
			for (int i = 0; i < lNegFinShare.size(); i++) {
				tNegFinShare = lNegFinShare.get(i);
				if (i == lNegFinShare.size() - 1) {
					// 最後一筆資料要用減的
					shareAmt = shareAmtRemaind;
				} else {
					shareAmt = shareAmSum.multiply(tNegFinShare.getAmtRatio(), MathContext.DECIMAL128)
							.divide(new BigDecimal(100), 0, RoundingMode.HALF_UP);
					shareAmtRemaind = shareAmtRemaind.subtract(shareAmt);
				}
				if (("458").equals(tNegFinShare.getFinCode())) {// 458 不寫入NegAppr01
					transSklShareAmt = transSklShareAmt.add(shareAmt);
				} else {
					transApprAmt = transApprAmt.add(shareAmt);
					if (ChekUpdDB == 1) {
						updAppr01(tNegFinShare, tNegTrans, tNegMain, shareAmt, titaVo);
					}
				}
			}
		} else {
			for (int i = 0; i < lNegFinShare.size(); i++) {
				tNegFinShare = lNegFinShare.get(i);
				if (("458").equals(tNegFinShare.getFinCode())) {// 458跳過-458不會寫入NgeAppr01
					continue;
				}
				NegAppr01Id tNegAppr01Id = new NegAppr01Id();
				tNegAppr01Id.setFinCode(tNegFinShare.getFinCode());
				tNegAppr01Id.setAcDate(tNegTrans.getAcDate());
				tNegAppr01Id.setTitaTlrNo(tNegTrans.getTitaTlrNo());
				tNegAppr01Id.setTitaTxtNo(tNegTrans.getTitaTxtNo());
				NegAppr01 tNegAppr01 = sNegAppr01Service.holdById(tNegAppr01Id, titaVo);
				if (tNegAppr01 != null) {
					try {
						sNegAppr01Service.delete(tNegAppr01, titaVo);
					} catch (DBException e) {
						throw new LogicException(titaVo, "E0008", "最大債權撥付資料檔"); // E0008 刪除資料時，發生錯誤
					}
				}
			}
		}
	}

	public void updAppr01(NegFinShare tNegFinShare, NegTrans tNegTrans, NegMain tNegMain, BigDecimal shareAmt,
			TitaVo titaVo) throws LogicException {
		NegAppr01Id tNegAppr01Id = new NegAppr01Id();
		tNegAppr01Id.setFinCode(tNegFinShare.getFinCode());
		tNegAppr01Id.setAcDate(tNegTrans.getAcDate());
		tNegAppr01Id.setTitaTlrNo(tNegTrans.getTitaTlrNo());
		tNegAppr01Id.setTitaTxtNo(tNegTrans.getTitaTxtNo());
		NegFinAcct tNegFinAcct = sNegFinAcctService.findById(tNegFinShare.getFinCode(), titaVo);
		if (tNegFinAcct == null) {
			throw new LogicException(titaVo, "E0001", "債務協商債權機構帳戶檔");// 查詢資料不存在
		}
		NegAppr01 tNegAppr01 = sNegAppr01Service.findById(tNegAppr01Id, titaVo);
		if (tNegAppr01 != null) {
			throw new LogicException(titaVo, "E0013", "最大債權撥付資料檔資料已存在");// 程式邏輯有誤
		}
		BigDecimal SumCustNoFinCode = SumCustNoFinCode(tNegTrans.getCustNo(), tNegTrans.getCaseSeq(),
				tNegFinShare.getFinCode(), titaVo);
		NegAppr01 tNegAppr01Upd = new NegAppr01();
		tNegAppr01Upd.setNegAppr01Id(tNegAppr01Id);
		tNegAppr01Upd.setCustNo(tNegTrans.getCustNo());// 戶號
		tNegAppr01Upd.setCaseSeq(tNegTrans.getCaseSeq());// 案件序號
		tNegAppr01Upd.setCaseKindCode(tNegMain.getCaseKindCode());// 案件種類
		tNegAppr01Upd.setApprAmt(shareAmt);// 撥付金額
		tNegAppr01Upd.setAccuApprAmt(SumCustNoFinCode.add(shareAmt));// 累計撥付金額
		tNegAppr01Upd.setAmtRatio(tNegFinShare.getAmtRatio());// 撥付比例
		tNegAppr01Upd.setExportDate(0);// 製檔日期
		tNegAppr01Upd.setApprDate(0);// 撥付日期
		tNegAppr01Upd.setBringUpDate(0);// 提兌日
		tNegAppr01Upd.setRemitBank(tNegFinShare.getFinCode());// 匯款銀行
		tNegAppr01Upd.setRemitAcct(tNegFinAcct.getRemitAcct());// 匯款帳號
		tNegAppr01Upd.setDataSendUnit(tNegFinAcct.getDataSendSection());// 資料傳送單位
		tNegAppr01Upd.setApprAcDate(0);// 撥付傳票日
		tNegAppr01Upd.setReplyCode("");// 回應代碼
		try {
			sNegAppr01Service.insert(tNegAppr01Upd, titaVo);
		} catch (DBException e) {
			throw new LogicException(titaVo, "E0005", "最大債權撥付資料檔");// E0005 新增資料時，發生錯誤
		}

	}

	// 更新jcic報送檔
	public void updateJcic(NegMain tNegMain, NegTrans tNegTrans, TitaVo titaVo) throws LogicException {
		// CaseKindCode 1:協商 2:調解 3:更生 4:清算 ; 無JCIC報送日期 才可更正
		switch (caseKindCode) {
		case "1":
			// 協商->JcicZ050 ->結案時報送 JCICZ046
			DbJcicZ050(tCustMain.getCustId(), tNegMain, tNegTrans, titaVo);
			break;
		case "2":
			// 調解->Jcic450
			DbJcicZ450(tCustMain.getCustId(), tNegMain, tNegTrans, titaVo);
		case "3":
			// 更生->JcicZ573
			DbJcicZ573(tCustMain.getCustId(), tNegMain, tNegTrans, titaVo);
			break;
		case "4":
			// 清算->Jcic

			break;
		}
	}

	public void DbJcicZ573(String CustId, NegMain tNegMain, NegTrans tNegTrans, TitaVo titaVo) throws LogicException {
		JcicZ573Id tJcicZ573Id = new JcicZ573Id();
		tJcicZ573Id.setApplyDate(tNegMain.getApplDate());
		tJcicZ573Id.setCustId(CustId);
		tJcicZ573Id.setPayDate(tNegTrans.getEntryDate());
		tJcicZ573Id.setSubmitKey(tNegMain.getMainFinCode());
		JcicZ573 tJcicZ573 = sJcicZ573Service.holdById(tJcicZ573Id, titaVo);
		if (tJcicZ573 != null) {
			if (tJcicZ573.getOutJcicTxtDate() != 0) {
				// E5009 資料檢核錯誤
				throw new LogicException(titaVo, "E5009", "已報送聯徵更生債務人繳款資料");
			}
		}
		String tranKey = "";
		BigDecimal payAmt = tNegTrans.getTxAmt().subtract(transReturnAmt); // 本次繳款金額減退還金額
		BigDecimal totalPayAmt = tNegMain.getAccuTempAmt().subtract(transReturnAmt);// 累計實際還款金額:累繳金額減退還金額
		if (titaVo.isHcodeNormal()) {
			// 正向
			if (tJcicZ573 != null) {
				// Update

				tJcicZ573.setPayAmt(tJcicZ573.getPayAmt() + payAmt.intValue());
				tJcicZ573.setTotalPayAmt(tJcicZ573.getTotalPayAmt() + payAmt.intValue());
				tJcicZ573.setTranKey(tranKey);
				try {
					sJcicZ573Service.update(tJcicZ573, titaVo);
				} catch (DBException e) {
					// E0007 更新資料時，發生錯誤
					throw new LogicException(titaVo, "E0007", "更生債務人繳款資料");
				}
			} else {
				// Insert
				tranKey = "A";
				tJcicZ573 = new JcicZ573();
				tJcicZ573.setJcicZ573Id(tJcicZ573Id);
				tJcicZ573.setPayAmt(payAmt.intValue());
				tJcicZ573.setTotalPayAmt(totalPayAmt.intValue());
				tJcicZ573.setTranKey(tranKey);
				tJcicZ573.setOutJcicTxtDate(0);
				String iKey = "";
				iKey = UUID.randomUUID().toString().toUpperCase().replaceAll("-", "");
				tJcicZ573.setUkey(iKey);
				try {
					sJcicZ573Service.insert(tJcicZ573, titaVo);
				} catch (DBException e) {
					// E0005 新增資料時，發生錯誤
					throw new LogicException(titaVo, "E0005", "更生債務人繳款資料");
				}
			}
		} else {
			// 訂正
			if (tJcicZ573 == null) {
				throw new LogicException(titaVo, "E0003", "更生債務人繳款資料");
			}
			// 訂正最後一筆
			if (tJcicZ573.getPayAmt() == payAmt.intValue()) {
				// DELETE
				try {
					sJcicZ573Service.delete(tJcicZ573, titaVo);
				} catch (DBException e) {
					// E0008 刪除資料時，發生錯誤
					throw new LogicException(titaVo, "E0008", "更生債務人繳款資料");
				}
				// 訂正非最後一筆
			} else {
				tJcicZ573.setPayAmt(tJcicZ573.getPayAmt() - payAmt.intValue());
				tJcicZ573.setTotalPayAmt(tJcicZ573.getTotalPayAmt() - payAmt.intValue());
				try {
					sJcicZ573Service.update(tJcicZ573, titaVo);
				} catch (DBException e) {
					// E0007 更新資料時，發生錯誤
					throw new LogicException(titaVo, "E0007", "更生債務人繳款資料");
				}
			}
		}
	}

	public void DbJcicZ046(JcicZ046Id tJcicZ046Id, TitaVo titaVo) throws LogicException {
		// 已報送過結案,則不再自動報送,改由人工建檔
		Slice<JcicZ046> slJcicZ046 = sJcicZ046Service.HadZ046(tJcicZ046Id.getCustId(), tJcicZ046Id.getRcDate(),
				tJcicZ046Id.getSubmitKey(), 0, Integer.MAX_VALUE, titaVo);
		List<JcicZ046> lJcicZ046 = slJcicZ046 == null ? null : slJcicZ046.getContent();
		if (lJcicZ046 != null) {
			for (JcicZ046 sJcicZ046 : lJcicZ046) {
				if (!("99").equals(sJcicZ046.getCloseCode())) { // 99:依債務清償方案履行完畢
					break;
				}
			}
		}

		JcicZ046 tJcicZ046 = sJcicZ046Service.holdById(tJcicZ046Id, titaVo);
		if (tJcicZ046 != null) {
			if (tJcicZ046.getOutJcicTxtDate() != 0) {
				// E5009 資料檢核錯誤
				throw new LogicException(titaVo, "E5009", "已報送聯徵結案通知資料");
			}
		}
		if (titaVo.isHcodeNormal()) {
			// 正向
			if (tJcicZ046 != null) {
				// 已存在不可新增
				throw new LogicException(titaVo, "E0002", "結案通知資料");

			} else {
				// Insert
				tJcicZ046 = new JcicZ046();
				tJcicZ046.setJcicZ046Id(tJcicZ046Id);
				tJcicZ046.setTranKey("A");// 交易代碼 A:新增 C:異動 D:刪除
				tJcicZ046.setCloseCode("99");// 結案原因代號 99:依債務清償方案履行完畢
				tJcicZ046.setBreakCode("");// 毀諾原因代號
				tJcicZ046.setOutJcicTxtDate(0);// 轉出JCIC文字檔日期
				String iKey = "";
				iKey = UUID.randomUUID().toString().toUpperCase().replaceAll("-", "");
				tJcicZ046.setUkey(iKey);
				try {
					sJcicZ046Service.insert(tJcicZ046, titaVo);
				} catch (DBException e) {
					// E0005 新增資料時，發生錯誤
					throw new LogicException(titaVo, "E0005", "結案通知資料");
				}
			}

		} else {
			// 訂正
			if (tJcicZ046 == null) {
				// E0007 更新資料時，發生錯誤
				throw new LogicException(titaVo, "E0007", "結案通知資料");
			}
			// DELETE
			try {
				sJcicZ046Service.delete(tJcicZ046, titaVo);
			} catch (DBException e) {
				// E0008 刪除資料時，發生錯誤
				throw new LogicException(titaVo, "E0008", "結案通知資料");
			}

		}

	}

	public void DbJcicZ450(String CustId, NegMain tNegMain, NegTrans tNegTrans, TitaVo titaVo) throws LogicException {
		JcicZ450Id tJcicZ450Id = new JcicZ450Id();
		tJcicZ450Id.setApplyDate(tNegMain.getApplDate());
		tJcicZ450Id.setCourtCode(tNegMain.getCourCode());
		tJcicZ450Id.setCustId(CustId);
		tJcicZ450Id.setPayDate(tNegTrans.getEntryDate());
		tJcicZ450Id.setSubmitKey(tNegMain.getMainFinCode());

		JcicZ450 tJcicZ450 = sJcicZ450Service.holdById(tJcicZ450Id, titaVo);
		if (tJcicZ450 != null) {
			if (tJcicZ450.getOutJcicTxtDate() != 0) {
				// E5009 資料檢核錯誤
				throw new LogicException(titaVo, "E5009", "已報送聯徵前置調解債務人繳款資料");
			}
		}
		BigDecimal payAmt = tNegTrans.getTxAmt().subtract(transReturnAmt); // 本次繳款金額減退還金額
		String payStatus = ""; // 債權結案註記 Y N
		BigDecimal sumRepayActualAmt = tNegMain.getAccuTempAmt().subtract(transReturnAmt);// 累計實際還款金額:累繳金額減退還金額
		BigDecimal sumRepayShouldAmt = tNegMain.getAccuDueAmt();// 截至目前累計應還款金額-累期款金額
		String tranKey = "";// 交易代碼
		if (("3").equals(tNegMain.getStatus())) {
			// 結案
			payStatus = "Y";
		} else {
			payStatus = "N";
		}
		if (titaVo.isHcodeNormal()) {
			// 正向
			if (tJcicZ450 != null) {
				// Update
				tJcicZ450.setPayAmt(tJcicZ450.getPayAmt() + payAmt.intValue());// 本次繳款金額
				tJcicZ450.setPayStatus(payStatus);// 債權結案註記 Y N
				tJcicZ450.setSumRepayActualAmt(tJcicZ450.getSumRepayActualAmt() + payAmt.intValue());// 累計實際還款金額
				try {
					sJcicZ450Service.update(tJcicZ450, titaVo);
				} catch (DBException e) {
					// E0007 更新資料時，發生錯誤
					throw new LogicException(titaVo, "E0007", "前置調解債務人繳款資料");
				}
			} else {
				// Insert

				tranKey = "A";
				tJcicZ450 = new JcicZ450();
				tJcicZ450.setJcicZ450Id(tJcicZ450Id);

				tJcicZ450.setPayAmt(payAmt.intValue());// 本次繳款金額
				tJcicZ450.setPayStatus(payStatus);// 債權結案註記 Y N
				tJcicZ450.setSumRepayActualAmt(sumRepayActualAmt.intValue());// 累計實際還款金額
				tJcicZ450.setSumRepayShouldAmt(sumRepayShouldAmt.intValue());// 截至目前累計應還款金額
				tJcicZ450.setTranKey(tranKey);// 交易代碼
				tJcicZ450.setOutJcicTxtDate(0);
				String iKey = "";
				iKey = UUID.randomUUID().toString().toUpperCase().replaceAll("-", "");
				tJcicZ450.setUkey(iKey);

				try {
					sJcicZ450Service.insert(tJcicZ450, titaVo);
				} catch (DBException e) {
					// E0005 新增資料時，發生錯誤
					throw new LogicException(titaVo, "E0005", "前置調解債務人繳款資料");
				}
			}

		} else {
			// 訂正
			if (tJcicZ450 == null) {
				// E0003 修改資料不存在
				throw new LogicException(titaVo, "E0003", "前置調解債務人繳款資料");
			}
			payStatus = tJcicZ450.getPayStatus(); // 訂正前交易別
			// 訂正最後一筆
			if (tJcicZ450.getPayAmt() == payAmt.intValue()) {
				// Delete
				try {
					sJcicZ450Service.delete(tJcicZ450, titaVo);
				} catch (DBException e) {
					// E0008 刪除資料時，發生錯誤
					throw new LogicException(titaVo, "E0008", "前置調解債務人繳款資料");
				}
				// 訂正非最後一筆
			} else {
				tJcicZ450.setPayAmt(tJcicZ450.getPayAmt() - payAmt.intValue()); // 本次繳款金額
				tJcicZ450.setPayStatus("N");// 債權結案註記 Y N
				tJcicZ450.setSumRepayActualAmt(tJcicZ450.getSumRepayActualAmt() - payAmt.intValue());// 累計實際還款金額

				try {
					sJcicZ450Service.update(tJcicZ450, titaVo);
				} catch (DBException e) {
					// E0007 更新資料時，發生錯誤
					throw new LogicException(titaVo, "E0007", "前置調解債務人繳款資料");
				}
			}
		}
		return;
	}

	public void DbJcicZ050(String CustId, NegMain tNegMain, NegTrans tNegTrans, TitaVo titaVo) throws LogicException {
		JcicZ050Id tJcicZ050Id = new JcicZ050Id();
		tJcicZ050Id.setCustId(CustId);
		tJcicZ050Id.setPayDate(tNegTrans.getEntryDate()); // 繳款日期-NegTrans入帳日
		tJcicZ050Id.setRcDate(tNegMain.getApplDate()); // 協商申請日-NegMain協商申請日
		tJcicZ050Id.setSubmitKey(tNegMain.getMainFinCode());
		JcicZ050 tJcicZ050 = sJcicZ050Service.holdById(tJcicZ050Id, titaVo);
		if (tJcicZ050 != null) {
			// Status = tJcicZ050.getStatus();
			if (tJcicZ050.getOutJcicTxtDate() != 0) {
				// E5009 資料檢核錯誤
				throw new LogicException(titaVo, "E5009", "已報送聯徵債務人繳款資料");
			}
		}

		BigDecimal payAmt = tNegTrans.getTxAmt().subtract(transReturnAmt); // 本次繳款金額減退還金額
		String payStatus = ""; // 債權結案註記 Y N
		BigDecimal sumRepayActualAmt = tNegMain.getAccuTempAmt().subtract(transReturnAmt);// 累計實際還款金額:累繳金額減退還金額
		BigDecimal sumRepayShouldAmt = tNegMain.getAccuDueAmt();// 截至目前累計應還款金額-累期款金額
		String tranKey = ""; // 交易代碼
		int CloseDate = tNegTrans.getEntryDate(); // 結案日期

		if (("3").equals(tNegMain.getStatus())) {
			// 結案
			payStatus = "Y";
		} else {
			payStatus = "N";
		}

		if (titaVo.isHcodeNormal()) {
			// 正向
			if (tJcicZ050 != null) {
				tJcicZ050.setPayAmt(tJcicZ050.getPayAmt() + payAmt.intValue());
				tJcicZ050.setSumRepayActualAmt(tJcicZ050.getSumRepayActualAmt() + payAmt.intValue());
				tJcicZ050.setStatus(payStatus);
				try {
					sJcicZ050Service.update(tJcicZ050, titaVo);
				} catch (DBException e) {
					// E0007 更新資料時，發生錯誤
					throw new LogicException(titaVo, "E0007", "債務人繳款資料");
				}
			} else {
				// Insert
				tranKey = "A";// 交易代碼 A:新增;C:異動;D:刪除
				tJcicZ050 = new JcicZ050();
				tJcicZ050.setJcicZ050Id(tJcicZ050Id);

				tJcicZ050.setTranKey(tranKey);
				tJcicZ050.setPayAmt(payAmt.intValue());
				tJcicZ050.setSumRepayActualAmt(sumRepayActualAmt.intValue());
				tJcicZ050.setSumRepayShouldAmt(sumRepayShouldAmt.intValue());
				tJcicZ050.setStatus(payStatus);
				tJcicZ050.setSecondRepayYM(0); // 進入第二階梯還款年月,目前不使用
				tJcicZ050.setOutJcicTxtDate(0);
				String iKey = "";
				iKey = UUID.randomUUID().toString().toUpperCase().replaceAll("-", "");
				tJcicZ050.setUkey(iKey);
				try {
					sJcicZ050Service.insert(tJcicZ050, titaVo);
				} catch (DBException e) {
					// E0005 新增資料時，發生錯誤
					throw new LogicException(titaVo, "E0005", "債務人繳款資料");
				}
			}

		} else {
			// 訂正
			if (tJcicZ050 == null) {
				throw new LogicException(titaVo, "E0003", "債務人繳款資料");
			}
			payStatus = tJcicZ050.getStatus(); // 訂正前交易別
			// 訂正最後一筆
			if (tJcicZ050.getPayAmt() == payAmt.intValue()) {
				try {
					sJcicZ050Service.delete(tJcicZ050, titaVo);
				} catch (DBException e) {
					// E0008 刪除資料時，發生錯誤
					throw new LogicException(titaVo, "E0008", "債務人繳款資料");
				}

				// 訂正非最後一筆
			} else {
				tJcicZ050.setPayAmt(tJcicZ050.getPayAmt() - payAmt.intValue());
				tJcicZ050.setSumRepayActualAmt(tJcicZ050.getSumRepayActualAmt() - payAmt.intValue());
				tJcicZ050.setStatus("N");
				try {
					sJcicZ050Service.update(tJcicZ050, titaVo);
				} catch (DBException e) {
					// E0007 更新資料時，發生錯誤
					throw new LogicException(titaVo, "E0007", "債務人繳款資料");
				}
			}
		}

		// 債務全數清償
		if (("Y").equals(payStatus)) {
			JcicZ046Id tJcicZ046Id = new JcicZ046Id();
			tJcicZ046Id.setCloseDate(CloseDate);
			tJcicZ046Id.setCustId(CustId);
			tJcicZ046Id.setRcDate(tNegMain.getApplDate());
			tJcicZ046Id.setSubmitKey(tNegMain.getMainFinCode());
			DbJcicZ046(tJcicZ046Id, titaVo);
		}

		return;
	}

	// 訂正
	public void NegRepayEraseRoutine(TitaVo titaVo) throws LogicException {
		this.info("NegRepayEraseRoutine ...");

		int EntDyI = Integer.parseInt(l5051ServiceImpl.RocToDc(String.valueOf(titaVo.getOrgEntdyI())));
		Slice<TxTemp> slTxTemp = sTxTempService.txTempTxtNoEq(EntDyI, titaVo.getOrgKin(), titaVo.getOrgTlr(),
				titaVo.getOrgTno(), this.index, this.limit, titaVo);
		List<TxTemp> lTxTemp = slTxTemp == null ? null : slTxTemp.getContent();
		if (lTxTemp == null || lTxTemp.size() == 0) {
			throw new LogicException(titaVo, "E0001", "交易暫存檔 日期=" + EntDyI + " 分行別 = " + titaVo.getOrgKin()
					+ " 交易員代號 = " + titaVo.getOrgTlr() + " 交易序號 = " + titaVo.getOrgTno()); // 查詢資料不存在
		}
		for (TxTemp tx : lTxTemp) {
			String SeqNo = tx.getSeqNo();
			this.info("NegRepayEraseRoutine tx SeqNo=[" + SeqNo + "]");
			TempVo tTempVo = new TempVo();
			tTempVo = tTempVo.getVo(tx.getText());

			int custNo = parse.stringToInteger(tTempVo.get("CustNo"));// 戶號
			int caseSeq = parse.stringToInteger(tTempVo.get("CaseSeq"));// 案件序號
			NegMainId tNegMainId = new NegMainId();
			tNegMainId.setCustNo(custNo);
			tNegMainId.setCaseSeq(caseSeq);
			NegMain tNegMain = sNegMainService.holdById(tNegMainId, titaVo);
			if (tNegMain == null) {
				throw new LogicException(titaVo, "E0006", "債務協商案件主檔");// E0006 鎖定資料時，發生錯誤
			}
			this.info("NegCom NegRepayEraseRoutine tNegMain!=null ");
			if (tNegMain.getThisAcDate() != titaVo.getOrgEntdyI()
					|| !tNegMain.getThisTitaTlrNo().equals(titaVo.getOrgTlr())
					|| tNegMain.getThisTitaTxtNo() != parse.stringToInteger(titaVo.getOrgTno())) {
				throw new LogicException(titaVo, "E0019", "最近一筆交易序號 = " + tNegMain.getThisAcDate() + "-"
						+ tNegMain.getThisTitaTlrNo() + "-" + tNegMain.getThisTitaTxtNo());
			} // 輸入資料錯誤
			int tranAcDate = parse.stringToInteger(tTempVo.get("TranAcDate"));
			String tranTitaTlrNo = tTempVo.get("TranTitaTlrNo");// 經辦
			int tranTitaTxtNo = parse.stringToInteger(tTempVo.get("TranTitaTxtNo"));

			NegTransId tNegTransId = new NegTransId();
			tNegTransId.setAcDate(tranAcDate);// 會計日期
			tNegTransId.setTitaTlrNo(tranTitaTlrNo);// 經辦
			tNegTransId.setTitaTxtNo(tranTitaTxtNo);// 交易序號
			NegTrans tNegTrans = sNegTransService.holdById(tNegTransId, titaVo);
			if (tNegTrans == null) {
				throw new LogicException(titaVo, "E0006", "債務協商交易檔");
			}
			if (tNegTrans.getExportDate() > 0) {
				throw new LogicException(titaVo, "E0015", "已撥付製檔不可訂正");
			}
			
			custId = "";
			tCustMain = sCustMainService.custNoFirst(custNo, custNo, titaVo);
			if (tCustMain != null) {
				custId = tCustMain.getCustId();
			} else {
				throw new LogicException(titaVo, "E0001", "客戶資料主檔處理時發生錯誤 戶號[" + custNo + "]");// E0001 查詢資料不存在
			}
			// 刪除最大債權分配
			if ("Y".equals(tNegMain.getIsMainFin())) {
				updNegFinShare(tNegTrans, tNegMain, transTxAmt.subtract(transReturnAmt), titaVo);
			}

			// 還原主檔、明細檔
			updateDbReverse(tTempVo, tNegMain, tNegTrans, titaVo);

			// 更新jcic報送檔
			if ("Y".equals(tNegMain.getIsMainFin())) {
				caseKindCode = tTempVo.get("CaseKindCode");
				updateJcic(tNegMain, tNegTrans, titaVo);
			}
		}
		return;

	}

	public void updateDbReverse(TempVo tTempVo, NegMain tNegMain, NegTrans tNegTrans, TitaVo titaVo)
			throws LogicException {
		tNegMain.setCaseKindCode(tTempVo.get("CaseKindCode"));// 案件種類
		tNegMain.setStatus(tTempVo.get("Status"));// 戶況
		tNegMain.setCustLoanKind(tTempVo.get("CustLoanKind"));// 債權戶別
		tNegMain.setDeferYMStart(parse.stringToInteger(tTempVo.get("DeferYMStart")));// 延期繳款年月(起)
		tNegMain.setDeferYMEnd(parse.stringToInteger(tTempVo.get("DeferYMEnd")));// 延期繳款年月(訖)
		tNegMain.setApplDate(parse.stringToInteger(tTempVo.get("ApplDate")));// 協商申請日
		tNegMain.setDueAmt(parse.stringToBigDecimal(tTempVo.get("DueAmt")));// 月付金(期款)
		tNegMain.setTotalPeriod(parse.stringToInteger(tTempVo.get("TotalPeriod")));// 期數
		tNegMain.setIntRate(parse.stringToBigDecimal(tTempVo.get("IntRate")));// 計息條件(利率)
		tNegMain.setFirstDueDate(parse.stringToInteger(tTempVo.get("FirstDueDate")));// 首次應繳日
		tNegMain.setLastDueDate(parse.stringToInteger(tTempVo.get("LastDueDate")));// 還款結束日
		tNegMain.setIsMainFin(tTempVo.get("IsMainFin"));// 是否最大債權
		tNegMain.setTotalContrAmt(parse.stringToBigDecimal(tTempVo.get("TotalContrAmt")));// 簽約總金額
		tNegMain.setMainFinCode(tTempVo.get("MainFinCode"));// 最大債權機構
		tNegMain.setPrincipalBal(parse.stringToBigDecimal(tTempVo.get("PrincipalBal")));// 總本金餘額
		tNegMain.setAccuTempAmt(parse.stringToBigDecimal(tTempVo.get("AccuTempAmt")));// 累繳金額
		tNegMain.setAccuOverAmt(parse.stringToBigDecimal(tTempVo.get("AccuOverAmt")));// 累溢收金額
		tNegMain.setAccuDueAmt(parse.stringToBigDecimal(tTempVo.get("AccuDueAmt")));// 累應還金額
		tNegMain.setAccuSklShareAmt(parse.stringToBigDecimal(tTempVo.get("AccuSklShareAmt")));// 累新壽分攤金額
		tNegMain.setRepaidPeriod(parse.stringToInteger(tTempVo.get("RepaidPeriod")));// 已繳期數
		tNegMain.setTwoStepCode(tTempVo.get("TwoStepCode"));// 二階段註記
		tNegMain.setChgCondDate(parse.stringToInteger(tTempVo.get("ChgCondDate")));// 申請變更還款條件日
		tNegMain.setNextPayDate(parse.stringToInteger(tTempVo.get("NextPayDate")));// 下次應繳日
		tNegMain.setPayIntDate(parse.stringToInteger(tTempVo.get("PayIntDate")));// 繳息迄日
		tNegMain.setRepayPrincipal(parse.stringToBigDecimal(tTempVo.get("RepayPrincipal")));// 還本本金
		tNegMain.setRepayInterest(parse.stringToBigDecimal(tTempVo.get("RepayInterest")));// 還本利息
		tNegMain.setStatusDate(parse.stringToInteger(tTempVo.get("StatusDate")));// 戶況日期
		tNegMain.setThisAcDate(parse.stringToInteger(tTempVo.get("ThisAcDate")));// 本次會計日期
		tNegMain.setThisTitaTlrNo(tTempVo.get("ThisTitaTlrNo"));// 本次經辦
		tNegMain.setThisTitaTxtNo(parse.stringToInteger(tTempVo.get("ThisTitaTxtNo")));// 本次交易序號
		tNegMain.setLastAcDate(parse.stringToInteger(tTempVo.get("LastAcDate")));// 上次會計日期
		tNegMain.setLastTitaTlrNo(tTempVo.get("LastTitaTlrNo"));// 上次經辦
		tNegMain.setLastTitaTxtNo(parse.stringToInteger(tTempVo.get("LastTitaTxtNo")));// 上次交易序號
		try {
			sNegMainService.update(tNegMain, titaVo);
		} catch (DBException e) {
			throw new LogicException(titaVo, "E0007", "債務協商案件主檔");// 更新資料時，發生錯誤
		}

		tNegTrans.setCustNo(parse.stringToInteger(tTempVo.get("TranCustNo")));// 戶號
		tNegTrans.setCaseSeq(parse.stringToInteger(tTempVo.get("TranCaseSeq")));// 案件序號
		tNegTrans.setEntryDate(parse.stringToInteger(tTempVo.get("TranEntryDate")));// 入帳日期
		tNegTrans.setTxStatus(parse.stringToInteger(tTempVo.get("TranTxStatus")));// 交易狀態
		tNegTrans.setTxKind(tTempVo.get("TranTxKind"));// 交易別
		tNegTrans.setTxAmt(parse.stringToBigDecimal(tTempVo.get("TranTxAmt")));// 交易金額
		tNegTrans.setPrincipalBal(parse.stringToBigDecimal(tTempVo.get("TranPrincipalBal")));// 本金餘額
		transReturnAmt = tNegTrans.getReturnAmt();	// FOR JCIC更正使用
		tNegTrans.setReturnAmt(parse.stringToBigDecimal(tTempVo.get("TranReturnAmt")));// 退還金額
		tNegTrans.setSklShareAmt(parse.stringToBigDecimal(tTempVo.get("TranSklShareAmt")));// 新壽攤分
		tNegTrans.setApprAmt(parse.stringToBigDecimal(tTempVo.get("TranApprAmt")));// 撥付金額
		tNegTrans.setExportDate(parse.stringToInteger(tTempVo.get("TranExportDate")));// 撥付製檔日
		tNegTrans.setExportAcDate(parse.stringToInteger(tTempVo.get("TranExportAcDate")));// 撥付出帳日
		tNegTrans.setTempRepayAmt(parse.stringToBigDecimal(tTempVo.get("TranTempRepayAmt")));// 暫收抵繳金額
		tNegTrans.setOverRepayAmt(parse.stringToBigDecimal(tTempVo.get("TranOverRepayAmt")));// 溢收抵繳金額
		tNegTrans.setPrincipalAmt(parse.stringToBigDecimal(tTempVo.get("TranPrincipalAmt")));// 本金金額
		tNegTrans.setInterestAmt(parse.stringToBigDecimal(tTempVo.get("TranInterestAmt")));// 利息金額
		tNegTrans.setOverAmt(parse.stringToBigDecimal(tTempVo.get("TranOverAmt")));// 轉入溢收金額
		tNegTrans.setIntStartDate(parse.stringToInteger(tTempVo.get("TranIntStartDate")));// 繳息起日
		tNegTrans.setIntEndDate(parse.stringToInteger(tTempVo.get("TranIntEndDate")));// 繳息迄日
		tNegTrans.setRepayPeriod(parse.stringToInteger(tTempVo.get("TranRepayPeriod")));// 還款期數
		tNegTrans.setRepayDate(parse.stringToInteger(tTempVo.get("TranRepayDate")));// 入帳還款日期
		tNegTrans.setOrgAccuOverAmt(parse.stringToBigDecimal(tTempVo.get("TranOrgAccuOverAmt")));// 累溢繳款(交易前)
		tNegTrans.setAccuOverAmt(parse.stringToBigDecimal(tTempVo.get("TranAccuOverAmt")));// 累溢繳款(交易後)
		tNegTrans.setShouldPayPeriod(parse.stringToInteger(tTempVo.get("TranShouldPayPeriod")));// 本次應還期數
		tNegTrans.setDueAmt(parse.stringToBigDecimal(tTempVo.get("TranDueAmt")));// 期金
		tNegTrans.setThisEntdy(parse.stringToInteger(tTempVo.get("TranThisEntdy")));// 本次交易日
		tNegTrans.setThisKinbr(tTempVo.get("TranThisKinbr"));// 本次分行別
		tNegTrans.setThisTlrNo(tTempVo.get("TranThisTlrNo"));// 本次交易員代號
		tNegTrans.setThisTxtNo(tTempVo.get("TranThisTxtNo"));// 本次交易序號
		tNegTrans.setThisSeqNo(tTempVo.get("TranThisSeqNo"));// 本次序號
		tNegTrans.setLastEntdy(parse.stringToInteger(tTempVo.get("TranLastEntdy")));// 上次交易日
		tNegTrans.setLastKinbr(tTempVo.get("TranLastKinbr"));// 上次分行別
		tNegTrans.setLastTlrNo(tTempVo.get("TranLastTlrNo"));// 上次交易員代號
		tNegTrans.setLastTxtNo(tTempVo.get("TranLastTxtNo"));// 上次交易序號
		tNegTrans.setLastSeqNo(tTempVo.get("TranLastSeqNo"));// 上次序號
		// sNegTransService.update(tNegTrans,titaVo);
		try {
			sNegTransService.update(tNegTrans, titaVo);
		} catch (DBException e) {
			throw new LogicException(titaVo, "E0007", "債務協商交易檔");// E0007 更新資料時，發生錯誤
		}
	}

	// 正向
	public void insertTxTemp(TitaVo titaVo, NegMain tNegMainOrg, NegTrans tNegTrans) throws LogicException {
		TxTemp tTxTemp = new TxTemp();
		TxTempId tTxTempId = new TxTempId();
		String wkSeqNo = tNegTrans.getThisSeqNo().trim();
		if (wkSeqNo != null && wkSeqNo.length() != 0) {

		} else {
			wkSeqNo = "0";
		}
		this.info("NegCom NegMainInsertTxTemp wkSeqNo=[" + wkSeqNo + "]");
		tTxTempId.setEntdy(titaVo.getEntDyI());
		tTxTempId.setKinbr(titaVo.getKinbr());
		tTxTempId.setTlrNo(titaVo.getTlrNo());
		tTxTempId.setTxtNo(titaVo.getTxtNo());
		tTxTempId.setSeqNo(wkSeqNo);

		tTxTemp.setTxTempId(tTxTempId);
		tTxTemp.setEntdy(titaVo.getEntDyI());
		tTxTemp.setKinbr(titaVo.getKinbr());
		tTxTemp.setTlrNo(titaVo.getTlrNo());
		tTxTemp.setTxtNo(titaVo.getTxtNo());
		tTxTemp.setSeqNo(wkSeqNo);

		TempVo tTempVo = new TempVo();
		tTempVo.clear();
		tTempVo.putParam("CustNo", String.valueOf(tNegMainOrg.getCustNo()));// 戶號
		tTempVo.putParam("CaseSeq", String.valueOf(tNegMainOrg.getCaseSeq()));// 案件序號
		tTempVo.putParam("CaseKindCode", String.valueOf(tNegMainOrg.getCaseKindCode()));// 案件種類
		tTempVo.putParam("Status", String.valueOf(tNegMainOrg.getStatus()));// 戶況
		tTempVo.putParam("CustLoanKind", String.valueOf(tNegMainOrg.getCustLoanKind()));// 債權戶別
		tTempVo.putParam("DeferYMStart", String.valueOf(tNegMainOrg.getDeferYMStart()));// 延期繳款年月(起)
		tTempVo.putParam("DeferYMEnd", String.valueOf(tNegMainOrg.getDeferYMEnd()));// 延期繳款年月(訖)
		tTempVo.putParam("ApplDate", String.valueOf(tNegMainOrg.getApplDate()));// 協商申請日
		tTempVo.putParam("DueAmt", String.valueOf(tNegMainOrg.getDueAmt()));// 月付金(期款)
		tTempVo.putParam("TotalPeriod", String.valueOf(tNegMainOrg.getTotalPeriod()));// 期數
		tTempVo.putParam("IntRate", String.valueOf(tNegMainOrg.getIntRate()));// 計息條件(利率)
		tTempVo.putParam("FirstDueDate", String.valueOf(tNegMainOrg.getFirstDueDate()));// 首次應繳日
		tTempVo.putParam("LastDueDate", String.valueOf(tNegMainOrg.getLastDueDate()));// 還款結束日
		tTempVo.putParam("IsMainFin", String.valueOf(tNegMainOrg.getIsMainFin()));// 是否最大債權
		tTempVo.putParam("TotalContrAmt", String.valueOf(tNegMainOrg.getTotalContrAmt()));// 簽約總金額
		tTempVo.putParam("MainFinCode", tNegMainOrg.getMainFinCode());// 最大債權機構
		tTempVo.putParam("PrincipalBal", String.valueOf(tNegMainOrg.getPrincipalBal()));// 總本金餘額
		tTempVo.putParam("AccuTempAmt", String.valueOf(tNegMainOrg.getAccuTempAmt()));// 累繳金額
		tTempVo.putParam("AccuOverAmt", String.valueOf(tNegMainOrg.getAccuOverAmt()));// 累溢收金額
		tTempVo.putParam("AccuDueAmt", String.valueOf(tNegMainOrg.getAccuDueAmt()));// 累應還金額
		tTempVo.putParam("AccuSklShareAmt", String.valueOf(tNegMainOrg.getAccuSklShareAmt()));// 累新壽分攤金額
		tTempVo.putParam("RepaidPeriod", String.valueOf(tNegMainOrg.getRepaidPeriod()));// 已繳期數
		tTempVo.putParam("TwoStepCode", String.valueOf(tNegMainOrg.getTwoStepCode()));// 二階段註記
		tTempVo.putParam("ChgCondDate", String.valueOf(tNegMainOrg.getChgCondDate()));// 申請變更還款條件日
		tTempVo.putParam("NextPayDate", String.valueOf(tNegMainOrg.getNextPayDate()));// 下次應繳日
		tTempVo.putParam("PayIntDate", String.valueOf(tNegMainOrg.getPayIntDate()));// 繳息迄日
		tTempVo.putParam("RepayPrincipal", String.valueOf(tNegMainOrg.getRepayPrincipal()));// 還本本金
		tTempVo.putParam("RepayInterest", String.valueOf(tNegMainOrg.getRepayInterest()));// 還本利息
		tTempVo.putParam("StatusDate", String.valueOf(tNegMainOrg.getStatusDate()));// 戶況日期

		tTempVo.putParam("ThisAcDate", String.valueOf(tNegMainOrg.getThisAcDate()));// 本次會計日期
		tTempVo.putParam("ThisTitaTlrNo", String.valueOf(tNegMainOrg.getThisTitaTlrNo()));// 本次經辦
		tTempVo.putParam("ThisTitaTxtNo", String.valueOf(tNegMainOrg.getThisTitaTxtNo()));// 本次交易序號
		tTempVo.putParam("LastAcDate", String.valueOf(tNegMainOrg.getLastAcDate()));// 上次會計日期
		tTempVo.putParam("LastTitaTlrNo", String.valueOf(tNegMainOrg.getLastTitaTlrNo()));// 上次經辦
		tTempVo.putParam("LastTitaTxtNo", String.valueOf(tNegMainOrg.getLastTitaTxtNo()));// 上次交易序號

		tTempVo.putParam("CreateDate", String.valueOf(tNegMainOrg.getCreateDate()));// 建檔日期時間
		tTempVo.putParam("CreateEmpNo", String.valueOf(tNegMainOrg.getCreateEmpNo()));// 建檔人員
		tTempVo.putParam("LastUpdate", String.valueOf(tNegMainOrg.getLastUpdate()));// 最後更新日期時間
		tTempVo.putParam("LastUpdateEmpNo", String.valueOf(tNegMainOrg.getLastUpdateEmpNo()));// 最後更新人員

		tTempVo.putParam("TranAcDate", String.valueOf(tNegTrans.getAcDate()));// 會計日期
		tTempVo.putParam("TranTitaTlrNo", String.valueOf(tNegTrans.getTitaTlrNo()));// 經辦
		tTempVo.putParam("TranTitaTxtNo", String.valueOf(tNegTrans.getTitaTxtNo()));// 交易序號
		tTempVo.putParam("TranCustNo", String.valueOf(tNegTrans.getCustNo()));// 戶號
		tTempVo.putParam("TranCaseSeq", String.valueOf(tNegTrans.getCaseSeq()));// 案件序號
		tTempVo.putParam("TranEntryDate", String.valueOf(tNegTrans.getEntryDate()));// 入帳日期
		tTempVo.putParam("TranTxStatus", String.valueOf(tNegTrans.getTxStatus()));// 交易狀態
		tTempVo.putParam("TranTxKind", String.valueOf(tNegTrans.getTxKind()));// 交易別
		tTempVo.putParam("TranTxAmt", String.valueOf(tNegTrans.getTxAmt()));// 交易金額
		tTempVo.putParam("TranPrincipalBal", String.valueOf(tNegTrans.getPrincipalBal()));// 本金餘額
		tTempVo.putParam("TranReturnAmt", String.valueOf(tNegTrans.getReturnAmt()));// 退還金額
		tTempVo.putParam("TranSklShareAmt", String.valueOf(tNegTrans.getSklShareAmt()));// 新壽攤分
		tTempVo.putParam("TranApprAmt", String.valueOf(tNegTrans.getApprAmt()));// 撥付金額
		tTempVo.putParam("TranExportDate", String.valueOf(tNegTrans.getExportDate()));// 撥付製檔日
		tTempVo.putParam("TranExportAcDate", String.valueOf(tNegTrans.getExportAcDate()));// 撥付出帳日
		tTempVo.putParam("TranTempRepayAmt", String.valueOf(tNegTrans.getTempRepayAmt()));// 暫收抵繳金額
		tTempVo.putParam("TranOverRepayAmt", String.valueOf(tNegTrans.getOverRepayAmt()));// 溢收抵繳金額
		tTempVo.putParam("TranPrincipalAmt", String.valueOf(tNegTrans.getPrincipalAmt()));// 本金金額
		tTempVo.putParam("TranInterestAmt", String.valueOf(tNegTrans.getInterestAmt()));// 利息金額
		tTempVo.putParam("TranOverAmt", String.valueOf(tNegTrans.getOverAmt()));// 轉入溢收金額
		tTempVo.putParam("TranIntStartDate", String.valueOf(tNegTrans.getIntStartDate()));// 繳息起日
		tTempVo.putParam("TranIntEndDate", String.valueOf(tNegTrans.getIntEndDate()));// 繳息迄日
		tTempVo.putParam("TranRepayPeriod", String.valueOf(tNegTrans.getRepayPeriod()));// 還款期數
		tTempVo.putParam("TranRepayDate", String.valueOf(tNegTrans.getRepayDate()));// 入帳還款日期
		tTempVo.putParam("TranOrgAccuOverAmt", String.valueOf(tNegTrans.getOrgAccuOverAmt()));// 累溢繳款(交易前)
		tTempVo.putParam("TranAccuOverAmt", String.valueOf(tNegTrans.getAccuOverAmt()));// 累溢繳款(交易後)
		tTempVo.putParam("TranShouldPayPeriod", String.valueOf(tNegTrans.getShouldPayPeriod()));// 本次應還期數
		tTempVo.putParam("TranDueAmt", String.valueOf(tNegTrans.getDueAmt()));// 期金
		tTempVo.putParam("TranThisEntdy", String.valueOf(tNegTrans.getThisEntdy()));// 本次交易日
		tTempVo.putParam("TranThisKinbr", String.valueOf(tNegTrans.getThisKinbr()));// 本次分行別
		tTempVo.putParam("TranThisTlrNo", String.valueOf(tNegTrans.getThisTlrNo()));// 本次交易員代號
		tTempVo.putParam("TranThisTxtNo", String.valueOf(tNegTrans.getThisTxtNo()));// 本次交易序號
		tTempVo.putParam("TranThisSeqNo", String.valueOf(tNegTrans.getThisSeqNo()));// 本次序號
		tTempVo.putParam("TranLastEntdy", String.valueOf(tNegTrans.getLastEntdy()));// 上次交易日
		tTempVo.putParam("TranLastKinbr", String.valueOf(tNegTrans.getLastKinbr()));// 上次分行別
		tTempVo.putParam("TranLastTlrNo", String.valueOf(tNegTrans.getLastTlrNo()));// 上次交易員代號
		tTempVo.putParam("TranLastTxtNo", String.valueOf(tNegTrans.getLastTxtNo()));// 上次交易序號
		tTempVo.putParam("TranLastSeqNo", String.valueOf(tNegTrans.getLastSeqNo()));// 上次序號

		tTxTemp.setText(tTempVo.getJsonString());
		try {
			sTxTempService.insert(tTxTemp, titaVo);
		} catch (DBException e) {
			throw new LogicException(titaVo, "E0005", "交易暫存檔 Key = " + tTxTempId); // 新增資料時，發生錯誤 }
		}
		return;
	}

	public String[] NegServiceList1(int AcDate, int IsMainFin, int State, int Detail, int ExportDateYN, int IsBtn,
			TitaVo titaVo) throws LogicException {
		checkData(AcDate, IsMainFin, State, Detail, ExportDateYN, IsBtn);
		this.info("NegService AcDate=[" + AcDate + "],IsMainFin=[" + IsMainFin + "],State=[" + State + "],Detail=["
				+ Detail + "],ExportDateYN=[" + ExportDateYN + "],IsBtn=[" + IsBtn + "]");
		String data[] = new String[2];// 筆數,總金額

		String sql = "";
		List<String[]> Data = null;
		try {
			sql = l597AServiceImpl.FindL597A(titaVo, AcDate, IsMainFin, State, Detail, ExportDateYN, IsBtn, "");
			sql = l597AServiceImpl.FindL5074(sql);
		} catch (Exception e) {
			// E5003 組建SQL語法發生問題
			this.info("NegCom ErrorForSql=" + e);
			throw new LogicException(titaVo, "E5003", "");
		}
		try {
			Data = l597AServiceImpl.FindL597A(l597AServiceImpl.FindData(this.index, this.limit, sql, titaVo, AcDate,
					IsMainFin, State, Detail, ExportDateYN, IsBtn), "L5074");
		} catch (Exception e) {
			// E5004 讀取DB時發生問題
			this.info("L5051 ErrorForDB=" + e);
			throw new LogicException(titaVo, "E5004", "");
		}
		if (Data != null && Data.size() != 0) {
			String lData[] = Data.get(0);
			for (int i = 0; i < 2; i++) {
				data[i] = lData[i];
				if (i == 0) {
					if (Integer.parseInt(data[i]) == 0) {
						data[i + 1] = "0";
						break;
					}
				}
			}

		}
		return data;
	}

	/**
	 * L5074與L597A 專用 用來查驗資料輸入是否正確
	 * 
	 * @param AcDate       會計日期(西元年月日)
	 * @param IsMainFin    是否為最大債權,1是0否
	 * @param State        選項
	 * @param Detail       細項 無,債協,調解,更生,清算
	 * @param ExportDateYN 製檔與否
	 * @param IsBtn        是否為Btn,1是0否
	 * @throws LogicException 交易程式需承接LogicException
	 * @author Jacky Lu
	 */
	public void checkData(int AcDate, int IsMainFin, int State, int Detail, int ExportDateYN, int IsBtn)
			throws LogicException {
		if (AcDate >= 0) {

		} else {
			// E5009 資料檢核錯誤
			throw new LogicException(titaVo, "E5009", "[會計日期(" + AcDate + ")]輸入有誤");
		}
		if (0 <= IsMainFin && IsMainFin <= 1) {

		} else {
			// E5009 資料檢核錯誤
			throw new LogicException(titaVo, "E5009", "[債權區分(" + IsMainFin + ")]輸入有誤");
		}
		if (1 <= State && State <= 17) {

		} else {
			// E5009 資料檢核錯誤
			throw new LogicException(titaVo, "E5009", "[查詢選項(" + State + ")]輸入有誤");
		}
		if (0 <= Detail && Detail <= 4) {

		} else {
			// E5009 資料檢核錯誤
			throw new LogicException(titaVo, "E5009", "[查詢細項(" + Detail + ")]輸入有誤");
		}

		if (0 <= ExportDateYN && ExportDateYN <= 2) {

		} else {
			// E5009 資料檢核錯誤
			throw new LogicException(titaVo, "E5009", "[是否製檔(" + ExportDateYN + ")]輸入有誤");
		}

		if (0 <= IsBtn && IsBtn <= 1) {

		} else {
			// E5009 資料檢核錯誤
			throw new LogicException(titaVo, "E5009", "[是否為按鈕(" + IsBtn + ")]輸入有誤");
		}
	}

	/**
	 * 計算應繳日
	 * 
	 * @param payIntDate   繳息迄日
	 * @param repaidPeriod 繳期數
	 * @return 應繳日
	 * @throws LogicException
	 */
	public int getRepayDate(int payIntDate, int repaidPeriod, TitaVo titaVo) throws LogicException {
		if (payIntDate == 0) {
			throw new LogicException(titaVo, "E5009", "繳息迄日=0");// E5009 資料檢核錯誤
		}
		int repayDate = payIntDate;
		if (repaidPeriod != 0) {
			dateUtil.init();
			dateUtil.setDate_1(payIntDate);
			dateUtil.setMons(repaidPeriod);
			repayDate = dateUtil.getCalenderDay();
		}
		return repayDate;

	}

	/**
	 * 讀取文字檔，編碼需為UTF-8
	 * 
	 * @param YyyyMm      西元年月
	 * @param AdjustMonth 調整的月份數正負號皆可
	 * @return YyyyMm年月
	 * @throws LogicException 交易程式需承接LogicException
	 * @author Jacky Lu
	 */
	public int AdjustYyyyMm(int YyyyMm, int AdjustMonth) throws LogicException {
		String StrYyyyyMm = String.valueOf(YyyyMm);
		if (StrYyyyyMm != null && StrYyyyyMm.length() == 6) {
			int Yyyy = Integer.parseInt(StrYyyyyMm.substring(0, 4));
			int Mm = Integer.parseInt(StrYyyyyMm.substring(4, 6));
			int trialAddOrMinus = 0;
			if (AdjustMonth > 0) {
				trialAddOrMinus = 1;
			} else if (AdjustMonth < 0) {
				trialAddOrMinus = -1;
			}

			int AdjustYyyy = Math.abs(AdjustMonth) / 12;
			AdjustMonth = Math.abs(AdjustMonth) % 12;

			Yyyy = Yyyy + trialAddOrMinus * AdjustYyyy;

			Mm = Mm + trialAddOrMinus * AdjustMonth;

			if (Mm > 0) {
				if (Mm / 12 >= 1) {
					if (Mm % 12 > 0) {
						Yyyy = Yyyy + trialAddOrMinus * Mm / 12;
						Mm = Mm % 12;
					}
				}
			} else if (Mm < 0) {
				Yyyy = Yyyy - 1;
				Mm = Mm + 12;
			} else {
				Mm = 12;
			}
			YyyyMm = Yyyy * 100 + Mm;
		}
		return YyyyMm;
	}

	public Map<String, String> NegMainSholdPay(NegMain tNegMain, int trialDate) throws LogicException {
		// int Today=dateUtil.getNowIntegerForBC();
		// L5075使用本段程式
		String OOPayTerm = "";// 應還期數
		String OOPayAmt = "";// 應繳金額
		String OOOverDueAmt = "";// OOOverDueAmt 應催繳金額=OOPayAmt 應繳金額 減 AccuOverAmt 累溢收

		int lastDueDate = tNegMain.getLastDueDate();// 還款結束日
		int nextPayDate = tNegMain.getNextPayDate();// 下次應繳日
		BigDecimal accuOverAmt = tNegMain.getAccuOverAmt();// 累溢收

		int finallDate = 0;// 最後結束的日期-計算剩餘期數
		if (trialDate <= lastDueDate) {
			finallDate = trialDate;
		} else {
			finallDate = lastDueDate;
		}

		int diffPeriod = DiffMonth(1, nextPayDate, finallDate) + 1;
		OOPayTerm = String.valueOf(diffPeriod);
		//OOPayAmt = String.valueOf(dueAmt.multiply(BigDecimal.valueOf(diffPeriod)));
		//計算應繳金額
		BigDecimal accuDueAmt = calAccuDueAmt(tNegMain, diffPeriod , 0);//最後一位參數傳0代表計算本利和
		OOPayAmt = String.valueOf(accuDueAmt);

		OOOverDueAmt = String.valueOf(BigDecimal.valueOf(Double.parseDouble(OOPayAmt)).subtract(accuOverAmt));
		Map<String, String> Data = new HashMap<String, String>();
		Data.put("OOPayTerm", OOPayTerm);
		Data.put("OOPayAmt", OOPayAmt);
		Data.put("OOOverDueAmt", OOOverDueAmt);
		return Data;
	}

	public BigDecimal calAccuDueAmt(NegMain tNegMain, int diffPeriod , int type) {
		//type=0 本利和 , 1=利息加總
		BigDecimal dueAmt = tNegMain.getDueAmt();// 月付金(期款)
		BigDecimal oBalance = tNegMain.getPrincipalBal();// 剩餘本金
		BigDecimal irate = tNegMain.getIntRate();// 利率
		BigDecimal oPrincipal = BigDecimal.ZERO;// 應繳本金
		BigDecimal oInterest = BigDecimal.ZERO;// 應繳利息
		BigDecimal osum = BigDecimal.ZERO;// 本利和
		BigDecimal accuDueAmt = BigDecimal.ZERO; // 應繳金額
		BigDecimal sumInterest = BigDecimal.ZERO;// 利息加總

		if (diffPeriod > 0) {
			for (int i = 0; i < diffPeriod; i++) {
				if (oBalance.compareTo(BigDecimal.ZERO) <= 0) {
					break;
				}
				oInterest = (oBalance.multiply(irate)).divide(new BigDecimal(1200), 0, RoundingMode.HALF_UP);// 應繳利息
				if (oBalance.compareTo(dueAmt) <= 0) {
					oPrincipal = oBalance;
				} else {
					oPrincipal = dueAmt.subtract(oInterest);// 應繳本金
				}
				osum = oInterest.add(oPrincipal);// 本利合計
				sumInterest = sumInterest.add(oInterest);	//利息加總
				if (osum.compareTo(dueAmt) < 0) {
					oPrincipal = oBalance;
					oBalance = BigDecimal.ZERO;
					osum = oPrincipal.add(oInterest);
				} else {
					oBalance = oBalance.subtract(oPrincipal);
				}
				accuDueAmt = accuDueAmt.add(osum);// 應繳金額
			}
			if(type==1) {//回傳利息加總
				accuDueAmt = sumInterest;
			}
		}
		return accuDueAmt;
	}
	
	
	public int DcToRoc(Integer Ymd) {
		if (Ymd != null && Ymd != 0) {
			if (String.valueOf(Ymd).length() == 8) {
				Ymd = Ymd - 19110000;
			}
		}
		return Ymd;
	}

	/**
	 * 算出兩個YYYYmmdd差幾個月,進而得到差幾期
	 * 
	 * @param PerPeriod 一期有幾個月
	 * @param Date1     日期1
	 * @param Date2     日期2
	 * @return 差幾期
	 * @throws LogicException 交易程式需承接LogicException
	 * @author Jacky Lu
	 */
	public int DiffMonth(int PerPeriod, int Date1, int Date2) throws LogicException {
		dateUtil.init();
		dateUtil.setDate_1(Date1 < Date2 ? Date1 : Date2);
		dateUtil.setDate_2(Date2 > Date1 ? Date2 : Date1);
		dateUtil.dateDiff();
		return dateUtil.getMons();
	}

	public String NegTransKeyValue(NegTrans tNegTrans) throws LogicException {
		String Key = "";
		// AcDate,TitaTlrNo,TitaTxtNo
		int AcDate = tNegTrans.getAcDate();
		String TitaTlrNo = tNegTrans.getTitaTlrNo();
		int TitaTxtNo = tNegTrans.getTitaTxtNo();
		Key = String.valueOf(AcDate) + ConnectWord + TitaTlrNo + ConnectWord + String.valueOf(TitaTxtNo);
		return Key;
	}

	public String AcReceivableKeyValue(AcReceivable tAcReceivable) throws LogicException {
		String Key = "";
		// AcctCode,CustNo,FacmNo,RvNo
		String AcctCode = tAcReceivable.getAcctCode();
		int CustNo = tAcReceivable.getCustNo();
		int FacmNo = tAcReceivable.getFacmNo();
		String RvNo = tAcReceivable.getRvNo();
		Key = AcctCode + ConnectWord + String.valueOf(CustNo) + ConnectWord + String.valueOf(FacmNo) + ConnectWord
				+ RvNo;
		return Key;
	}

	public String NegAppr02Value(NegAppr02 tNegAppr02) throws LogicException {
		String Key = "";
		// BringUpDate,FinCode,TxSeq
		int BringUpDate = tNegAppr02.getBringUpDate();
		String FinCode = tNegAppr02.getFinCode();
		String TxSeq = tNegAppr02.getTxSeq();
		Key = String.valueOf(BringUpDate) + ConnectWord + FinCode + ConnectWord + TxSeq;
		return Key;
	}

	public Map<String, String> FindCustMain(String CustId, String CustNo, TitaVo titaVo) {
		CustMain tCustMain = new CustMain();
		int IntCustNo = 0;
		if (CustId != null && CustId.length() != 0) {
			tCustMain = sCustMainService.custIdFirst(CustId, titaVo);
		} else {
			if (CustNo != null && CustNo.length() != 0) {
				IntCustNo = Integer.parseInt(CustNo);
				if (IntCustNo > 0) {
					tCustMain = sCustMainService.custNoFirst(IntCustNo, IntCustNo, titaVo);
				}
			}
		}
		String CustName = "";
		if (tCustMain != null) {
			CustId = tCustMain.getCustId();
			IntCustNo = tCustMain.getCustNo();
			CustName = tCustMain.getCustName();
		}
		Map<String, String> MapData = new HashMap<String, String>();
		MapData.put("CustId", CustId);
		MapData.put("CustName", CustName);
		MapData.put("CustNo", String.valueOf(IntCustNo));
		return MapData;
	}

	/**
	 * PMT function ported from Excel to Java to use BigDecimals.
	 * 
	 * @param interestRate                   interest rate for the loan.(利率(月))
	 * @param numberOfPayments               is the total number of payments for the
	 *                                       loan.(期數)
	 * @param principal                      is the present value; also known as the
	 *                                       principal.(本金)
	 * @param futureValue                    It is the future value, or the balance
	 *                                       that you want to have left after the
	 *                                       last payment. If fv is omitted, the fv
	 *                                       is assumed to be zero.(未來值)
	 * @param paymentsDueAtBeginningOfPeriod payments are due at the beginning of
	 *                                       the period. (是否在期初付款)
	 * @return payment
	 */
	public BigDecimal pmt(BigDecimal interestRate, int numberOfPayments, BigDecimal principal, BigDecimal futureValue,
			boolean paymentsDueAtBeginningOfPeriod) {
		final BigDecimal n = new BigDecimal(numberOfPayments);
		if (BigDecimal.ZERO.compareTo(interestRate) == 0) {
			return (futureValue.add(principal)).divide(n, MathContext.DECIMAL128).negate();
		} else {
			if (numberOfPayments == 1) {
				return (futureValue.add(principal).add(principal.multiply(interestRate)))
						.divide(n, MathContext.DECIMAL128).negate();
			}
			final BigDecimal r1 = interestRate.add(BigDecimal.ONE);
			final BigDecimal pow = r1.pow(numberOfPayments);

			final BigDecimal divisor;
			if (paymentsDueAtBeginningOfPeriod) {
				divisor = r1.multiply(BigDecimal.ONE.subtract(pow));
			} else {
				divisor = BigDecimal.ONE.subtract(pow);
			}
			return (principal.multiply(pow).add(futureValue)).multiply(interestRate).divide(divisor,
					MathContext.DECIMAL128);
		}
	}

	/**
	 * nper 找出我需要繳幾期
	 * 
	 * @param LoanAmt 本金
	 * @param DueAmt  期金
	 * @param Rate    利率(年)
	 * @return int 要繳幾期
	 * @throws LogicException ..
	 */
	public int nper(BigDecimal LoanAmt, BigDecimal DueAmt, BigDecimal Rate) throws LogicException {
		// L5970使用
		this.info("NegCom nper LoanAmt=[" + LoanAmt + "] DueAmt=[" + DueAmt + "] Rate=[" + Rate + "]");
		int Period = 0;// 應繳期數
		if (Rate.compareTo(BigDecimal.ZERO) != 0) {
			BigDecimal Interest = LoanAmt.multiply(Rate).divide(new BigDecimal(1200), 5, RoundingMode.HALF_UP);// 第一次的利息
			if (Interest.compareTo(DueAmt) >= 0) {
				// E5009 資料檢核錯誤
				throw new LogicException(titaVo, "E5009", "期金小於等於利息,該公式為發散型無法計算期數");
			} else {
				while (LoanAmt.compareTo(BigDecimal.ZERO) > 0) {
					// LoanAmt-(DueAmt-LoanAmt*Rate)
					LoanAmt = LoanAmt.subtract(DueAmt
							.subtract(LoanAmt.multiply(Rate).divide(new BigDecimal(1200), 5, RoundingMode.HALF_UP)));
					Period++;
				}
			}
		} else {
			Period = LoanAmt.divide(new BigDecimal(1200), 0, RoundingMode.HALF_UP).intValue();
		}
		if (Period <= 0) {
			// E5009 資料檢核錯誤
			throw new LogicException(titaVo, "E5009", "期數小於等於零");
		} else {
			if (Period >= 9999) {
				throw new LogicException(titaVo, "E5009", "期數大於9999期");
			}
		}

		return Period;
	}

	public String[] FindNegFinAcc(String strBankCode, TitaVo titaVo) {
		this.info("NegCom FindCdBank strBankCode=[" + strBankCode + "]");
		String str[] = { "", "" };
		if (strBankCode != null && strBankCode.length() != 0) {
			NegFinAcct tNegFinAcct = sNegFinAcctService.findById(strBankCode, titaVo);
			if (tNegFinAcct != null) {
				// String FinCode=tNegFinAcct.getFinCode().trim();
				String FinItem = tNegFinAcct.getFinItem().trim();
				if (FinItem != null && FinItem.length() != 0) {
					str[0] = FinItem;// 行庫名稱
					str[1] = "";// 分行名稱
				}
			}
		}
		if (str[0] != null && str[0].length() != 0) {

		} else {
			str = FindCdBank(strBankCode, titaVo);
		}
		return str;
	}

	public String[] FindCdBank(String strBankCode, TitaVo titaVo) {
		this.info("NegCom FindCdBank strBankCode=[" + strBankCode + "]");
		String str[] = { "", "" };
		if (strBankCode != null && strBankCode.length() != 0) {
			int strBankCodeL = strBankCode.length();
			CdBankId tCdBankId = new CdBankId();
			String BankCode = "";
			String BranchCode = "    ";
			if (strBankCodeL >= 3) {
				BankCode = strBankCode.substring(0, 3);
			}
			if (strBankCodeL >= 7) {
				BranchCode = strBankCode.substring(3, 7);
				if (("0000").equals(BranchCode)) {
					BranchCode = "    ";
				}
			}
			this.info("NegCom FindCdBank BankCode=[" + BankCode + "],BranchCode=[" + BranchCode + "]");
			tCdBankId.setBankCode(BankCode);
			tCdBankId.setBranchCode(BranchCode);
			CdBank tCdBank = cdBankService.findById(tCdBankId, titaVo);
			if (tCdBank != null) {
				str[0] = tCdBank.getBankItem().trim();// 行庫名稱
				str[1] = tCdBank.getBranchItem().trim();// 分行名稱
				if (str[1] != null && str[1].length() != 0) {

				} else {
					str[1] = "";
				}
				if (!str[1].contains(str[0])) {
					str[1] = str[0] + str[1];
				}
			}
		}

		return str;
	}

	public String checkLengthAneThrowError(String str, int len, String CaseType, int reError) throws LogicException {

		// str - 原始字串
		// len - 指定字串長度
		// CaseType - 型態
		// reError - 是否回傳錯誤訊息 0:否 1:是
		String reStr = "";
		int strL = str.length();
		if (len < strL) {
			switch (CaseType) {
			case "TitaTxtNo":
				// 交易序號-正常應該是8碼以內,但BatchTx02進來的定義是10碼
				if (strL > 2) {
					if (("00").equals(str.substring(0, 2))) {
						reStr = str.substring(2, strL);
					}
				}

				if (reError == 1) {
					// 要報送錯誤
					if (reStr == null || reStr.length() == 0) {
						this.info("NegCom checkLengthAneThrowError TitaTxtNo str=[" + str + "]");
						throw new LogicException(titaVo, "E5006", "發生未預期的錯誤.");
					}
				}
				break;
			default:
				this.info("NegCom checkLengthAneThrowError CaseType=[" + CaseType + "]");
				throw new LogicException(titaVo, "E5006", "發生未預期的錯誤.");
			}
		} else {
			reStr = str;
		}
		return reStr;
	}

	public int getNewCustNo(String CustId, TitaVo titaVo) throws LogicException {
		int custNo = 0;
		if (CustId != null && CustId.length() != 0) {
			CustMain custMain = sCustMainService.custIdFirst(CustId, titaVo);
			if (custMain.getCustNo() == 0) {
				// 共用代碼檔 00一般 01員工 02首購 03關企公司 04關企員工 05保戶 07員工二親等 09新二階員工
				List<String> CustTypeCode = new ArrayList<String>();
				CustTypeCode.add("05");
				this.info("NegCom CustTypeCode=[" + custMain.getCustTypeCode() + "]");
				if (CustTypeCode.contains(custMain.getCustTypeCode())) {
					// 如果屬於保戶,可以自動給戶號 參考L2153
					CustMain updCustMain = sCustMainService.holdById(custMain, titaVo);
					if (updCustMain != null) {
						custNo = gSeqCom.getSeqNo(0, 0, "L2", "0001", 9999999, titaVo);
						if (custNo != 0) {
							this.info("NegCom custNo=[" + custNo + "]");
							updCustMain.setCustNo(custNo);
							try {
								sCustMainService.update(updCustMain, titaVo);
							} catch (DBException e) {
								// E0007 更新資料時，發生錯誤
								throw new LogicException(titaVo, "E0007", "客戶資料主檔");
							}
						} else {
							// E0007 更新資料時，發生錯誤
							throw new LogicException(titaVo, "E0007", "");
						}
					} else {
						// E0006 鎖定資料時，發生錯誤
						throw new LogicException(titaVo, "E0006", "");
					}
				} else {
					throw new LogicException(titaVo, "E0007", "此客戶非保貸戶,不可直接給予戶號");
				}
			}
		}

		return custNo;
	}

	private BigDecimal SumCustNoFinCode(int CustNo, int CaseSeq, String FinCode, TitaVo titaVo) {
		BigDecimal AccuApprAmt = BigDecimal.ZERO;
		Slice<NegAppr01> sNegAppr01 = sNegAppr01Service.SumCustNoFinCode(CustNo, CaseSeq, FinCode, 0, Integer.MAX_VALUE,
				titaVo);
		List<NegAppr01> lsNegAppr01 = sNegAppr01 == null ? null : sNegAppr01.getContent();
		if (lsNegAppr01 != null) {
			for (NegAppr01 tNegAppr01 : lsNegAppr01) {
				AccuApprAmt = AccuApprAmt.add(tNegAppr01.getApprAmt());
			}
		}
		return AccuApprAmt;
	}

	@Override
	public void exec() throws LogicException {
		// TODO Auto-generated method stub

	}
}
