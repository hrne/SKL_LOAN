package com.st1.itx.util.common;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
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

	String ConnectWord = ",";// Key值區分字串
	String CombineWord = ";";// 連接字串
	String UseDbNegTrans = "NegTrans";
	String UseDbAcReceivable = "AcReceivable";
	String UseDbNegAppr02 = "NegAppr02";

	BigDecimal BigDecimalZero = BigDecimal.ZERO;

	private String Data[][] = {
			// RIM對應名稱,Map對應名稱,中文名稱,特殊規則
			{ "L5r03CustId", "CustId", "身份證字號", "" }, { "L5r03CaseSeq", "CaseSeq", "案件序號", "" }, { "L5r03CustNo", "CustNo", "戶號", "" }, { "L5r03CustName", "CustName", "戶名", "" },
			{ "L5r03Status", "Status", "戶況(舊)", "" }, { "L5r03NewStatus", "NewStatus", "戶況(新)", "" }, { "L5r03CustLoanKind", "CustLoanKind", "債權戶別", "" }, { "L5r03ApplDate", "ApplDate", "協商申請日", "" },
			{ "L5r03MainDueAmt", "MainDueAmt", "每期期款", "" }, { "L5r03TotalPeriod", "TotalPeriod", "總期數", "" }, { "L5r03IntRate", "IntRate", "每期利率", "" },
			{ "L5r03RepaidPeriod", "RepaidPeriod", "已繳期數(前)", "" }, { "L5r03NewRepaidPeriod", "NewRepaidPeriod", "已繳期數(後)", "" }, { "L5r03IsMainFin", "IsMainFin", "是否最大債權", "" },
			{ "L5r03MainFinCode", "MainFinCode", "最大債權機構", "" }, { "L5r03OrgPrincipalBal", "OrgPrincipalBal", "總本金餘額(前)", "" }, { "L5r03NewPrincipalBal", "NewPrincipalBal", "總本金餘額(後)", "" },
			{ "L5r03OrgAccuTempAmt", "OrgAccuTempAmt", "累繳金額(前)", "" }, { "L5r03NewAccuTempAmt", "NewAccuTempAmt", "累繳金額(後)", "" }, { "L5r03OrgAccuOverAmt", "OrgAccuOverAmt", "累溢收金額(前)", "" },
			{ "L5r03NewAccuOverAmt", "NewAccuOverAmt", "累溢收金額(後)", "" }, { "L5r03OrgAccuDueAmt", "OrgAccuDueAmt", "累應還金額(前)", "" }, { "L5r03NewAccuDueAmt", "NewAccuDueAmt", "累應還金額(後)", "" },
			{ "L5r03OrgAccuSklShareAmt", "OrgAccuSklShareAmt", "累新壽分攤金額(前)", "" }, { "L5r03NewAccuSklShareAmt", "NewAccuSklShareAmt", "累新壽分攤金額(後)", "" },
			{ "L5r03OrgNextPayDate", "OrgNextPayDate", "下次應繳日(前)", "" }, { "L5r03NewNextPayDate", "NewNextPayDate", "下次應繳日(後)", "" }, { "L5r03OrgRepayPrincipal", "OrgRepayPrincipal", "還本本金(前)", "" },
			{ "L5r03NewRepayPrincipal", "NewRepayPrincipal", "還本本金(後)", "" }, { "L5r03OrgRepayInterest", "OrgRepayInterest", "還本利息(前)", "" },
			{ "L5r03NewRepayInterest", "NewRepayInterest", "還本利息(後)", "" }, { "L5r03TwoStepCode", "TwoStepCode", "二階段註記", "" }, { "L5r03ChgCondDate", "ChgCondDate", "申請變更還款條件日", "" },
			{ "L5r03PayIntDate", "PayIntDate", "繳息迄日(前)", "" }, { "L5r03NewPayIntDate", "NewPayIntDate", "繳息迄日(後)", "" }, { "L5r03OrgStatusDate", "OrgStatusDate", "戶況日期(前)", "" },
			{ "L5r03NewStatusDate", "NewStatusDate", "戶況日期(後)", "" }, { "L5r03TransAcDate", "TransAcDate", "會計日期", "" }, { "L5r03TransTitaTlrNo", "TransTitaTlrNo", "經辦", "" },
			{ "L5r03TransTitaTxtNo", "TransTitaTxtNo", "交易序號", "" }, { "L5r03TransCustNo", "TransCustNo", "戶號", "" }, { "L5r03TransCaseSeq", "TransCaseSeq", "案件序號", "" },
			{ "L5r03TransEntryDate", "TransEntryDate", "入帳日期", "" }, { "L5r03TransTxStatus", "TransTxStatus", "交易狀態(前)", "" }, { "L5r03NewTransTxStatus", "NewTransTxStatus", "交易狀態(後)", "" },
			{ "L5r03TransTxKind", "TransTxKind", "交易別(前)", "" }, { "L5r03NewTransTxKind", "NewTransTxKind", "交易別(後)", "" }, { "L5r03TransTxAmt", "TransTxAmt", "交易金額(前)", "" },
			{ "L5r03NewTransTxAmt", "NewTransTxAmt", "交易金額(後)", "" }, { "L5r03TransPrincipalBal", "TransPrincipalBal", "本金餘額(前)", "" },
			{ "L5r03NewTransPrincipalBal", "NewTransPrincipalBal", "本金餘額(後)", "" }, { "L5r03TransReturnAmt", "TransReturnAmt", "退還金額(前)", "" },
			{ "L5r03NewTransReturnAmt", "NewTransReturnAmt", "退還金額(後)", "" }, { "L5r03TransSklShareAmt", "TransSklShareAmt", "攤分金額(前)", "" },
			{ "L5r03NewTransSklShareAmt", "NewTransSklShareAmt", "攤分金額(後)", "" }, { "L5r03TransApprAmt", "TransApprAmt", "撥付金額(前)", "" }, { "L5r03NewTransApprAmt", "NewTransApprAmt", "撥付金額(後)", "" },
			{ "L5r03TransExportDate", "TransExportDate", "撥付製檔日", "" }, { "L5r03TransExportAcDate", "TransExportAcDate", "撥付出帳日", "" },
			{ "L5r03TransTempRepayAmt", "TransTempRepayAmt", "暫收抵繳金額(前)", "" }, { "L5r03NewTransTempRepayAmt", "NewTransTempRepayAmt", "暫收抵繳金額(後)", "" },
			{ "L5r03TransOverRepayAmt", "TransOverRepayAmt", "溢收抵繳金額(前)", "" }, { "L5r03NewTransOverRepayAmt", "NewTransOverRepayAmt", "溢收抵繳金額(後)", "" },
			{ "L5r03TransPrincipalAmt", "TransPrincipalAmt", "沖銷本金(前)", "" }, { "L5r03NewTransPrincipalAmt", "NewTransPrincipalAmt", "沖銷本金(後)", "" },
			{ "L5r03TransInterestAmt", "TransInterestAmt", "沖銷利息(前)", "" }, { "L5r03NewTransInterestAmt", "NewTransInterestAmt", "沖銷利息(後)", "" },
			{ "L5r03TransOverAmt", "TransOverAmt", "轉入溢收金額(前)", "" }, { "L5r03NewTransOverAmt", "NewTransOverAmt", "轉入溢收金額(後)", "" }, { "L5r03TransIntStartDate", "TransIntStartDate", "繳息起日(前)", "" },
			{ "L5r03NewTransIntStartDate", "NewTransIntStartDate", "繳息起日(後)", "" }, { "L5r03TransIntEndDate", "TransIntEndDate", "繳息迄日(前)", "" },
			{ "L5r03NewTransIntEndDate", "NewTransIntEndDate", "繳息迄日(後)", "" }, { "L5r03TransRepayPeriod", "TransRepayPeriod", "還款期數(前)", "" },
			{ "L5r03NewTransRepayPeriod", "NewTransRepayPeriod", "還款期數(後)", "" }, { "L5r03TransShouldPayPeriod", "TransShouldPayPeriod", "本次應還期數(前)", "" },
			{ "L5r03NewTransShouldPayPeriod", "NewTransShouldPayPeriod", "本次應還期數(後)", "" }, { "L5r03TransDueAmt", "TransDueAmt", "本次應還金額(前)", "" },
			{ "L5r03NewTransDueAmt", "NewTransDueAmt", "本次應還金額(後)", "" }, { "L5r03TransRepayDate", "TransRepayDate", "入帳還款日期(前)", "" }, // DateRocToAc
			{ "L5r03NewTransRepayDate", "NewTransRepayDate", "入帳還款日期(後)", "" }, // DateRocToAc
			{ "L5r03TransOrgAccuOverAmt", "TransOrgAccuOverAmt", "累溢繳款(交易後)(前)", "" }, { "L5r03NewTransOrgAccuOverAmt", "NewTransOrgAccuOverAmt", "累溢繳款(交易後)(後)", "" },
			{ "L5r03TransAccuOverAmt", "TransAccuOverAmt", "累溢繳款(交易後)(前)", "" }, { "L5r03NewTransAccuOverAmt", "NewTransAccuOverAmt", "累溢繳款(交易後)(後)", "" },
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
	 * NegTrans試算專用
	 * 
	 * @param titaVo       titaVo
	 * @param tNegTrans    NegTrans的一筆資料
	 * @param TrialFunc    試算是否為一開始的,還是後面修改後的調Rim 0:一開始試算 1:異動後 試算 2:UPDATE
	 * @param NewTxKind    異動後交易別
	 * @param NewReturnAmt 異動後退還金額
	 * @return MapNeg注意:NegTrans開頭會註記Trans,NegMain開投註記Main
	 * @throws LogicException 交易程式需承接LogicException
	 * @author Jacky Lu
	 */
	public Map<String, String> NegTransTrial(TitaVo titaVo, NegTrans tNegTrans, String TrialFunc, String NewTxKind, String NewReturnAmt) throws LogicException {
		int ChekUpdDB = 0;// 0:不異動 1:異動
		if (("2").equals(TrialFunc)) {
			// 更新資料庫
			ChekUpdDB = 1;
		}

		if (NewReturnAmt != null && NewReturnAmt.length() != 0) {

		} else {
			NewReturnAmt = "0.0";
		}

		// int Today=dateUtil.getNowIntegerForBC();
		int Today = titaVo.getOrgEntdyI();// 會計日期
		this.info("NegTransTrial Today=[" + Today + "]");
		Map<String, String> MapNeg = new HashMap<String, String>();
		// 1.得到NegTrans資料取用NegMains資料
		if (tNegTrans != null) {
			int ThisCustNo = tNegTrans.getCustNo();
			int ThisCaseSeq = tNegTrans.getCaseSeq();
			NegMainId tNegMainId = new NegMainId();
			tNegMainId.setCaseSeq(ThisCaseSeq);
			tNegMainId.setCustNo(ThisCustNo);

			NegMain tNegMain = new NegMain();
			tNegMain = sNegMainService.findById(tNegMainId, titaVo);

			String custId = "";
			String custNo = "";
			String custName = "";

			Map<String, String> CustData = FindCustMain(custId, String.valueOf(ThisCustNo), titaVo);
			custId = CustData.get("CustId");
			custNo = CustData.get("CustNo");
			custName = CustData.get("CustName");
			// 2.得到Main的資料
			if (tNegMain != null) {
				// 3.單筆試算

				// NegMain
				String status = tNegMain.getStatus();
				String IsMainFin = tNegMain.getIsMainFin();
				String mainFinCode = tNegMain.getMainFinCode();
				BigDecimal mainDueAmt = tNegMain.getDueAmt();// 期金
				String caseKindCode = tNegMain.getCaseKindCode();
				int mainTotalPeriod = tNegMain.getTotalPeriod();// 總期數
				int mainRepaidPeriod = tNegMain.getRepaidPeriod();// 已繳期數
				BigDecimal mainAccuTempAmt = tNegMain.getAccuTempAmt();// 累繳金額
				BigDecimal mainAccuOverAmt = tNegMain.getAccuOverAmt();// 累溢收金額
				BigDecimal mainAccuDueAmt = tNegMain.getAccuDueAmt();// 累應還金額
				BigDecimal mainAccuSklShareAmt = tNegMain.getAccuSklShareAmt();// 累計新壽攤分
				BigDecimal mainIntRate = tNegMain.getIntRate();// 利率\
				int remainPeriod = mainTotalPeriod - mainRepaidPeriod;// 剩餘期數

				int mainFirstDueDate = tNegMain.getFirstDueDate();// 首次應繳日
				int mainNextPayDate = tNegMain.getNextPayDate();// 下次應繳日
				int mainPayIntDate = tNegMain.getPayIntDate();// 繳息迄日
				int newStatusDate = tNegMain.getStatusDate();// 戶況日期
				int newPayIntDate = 0;// 繳息迄日

				BigDecimal mainPrincipalBal = tNegMain.getPrincipalBal();// 總本金餘額
				if (mainPrincipalBal.compareTo(BigDecimalZero) == 0) {
					mainPrincipalBal = tNegMain.getTotalContrAmt();
				}
				BigDecimal mainRepayPrincipal = tNegMain.getRepayPrincipal();// 累償還本金
				BigDecimal mainRepayInterest = tNegMain.getRepayInterest();// 累償還利息

				// NegTrans
				int TransTxStatus = tNegTrans.getTxStatus();// 交易狀態 0:未入帳;1:待處理;2:已入帳;

				String transTxKind = tNegTrans.getTxKind();// 交易別 0:正常;1:溢繳;2:短繳;3:提前還本;4:結清;5:提前清償;6:待處理
				BigDecimal transTxAmt = tNegTrans.getTxAmt();// 交易金額
				BigDecimal orgTransPrincipalBal = tNegTrans.getPrincipalBal();// 本金餘額
				if (orgTransPrincipalBal.compareTo(BigDecimalZero) == 0) {
					orgTransPrincipalBal = mainPrincipalBal;
				}
				BigDecimal TransPrincipalBal = BigDecimal.ZERO;
				BigDecimal TransReturnAmt = tNegTrans.getReturnAmt();// 退還金額
				BigDecimal TransSklShareAmt = tNegTrans.getSklShareAmt();// 新壽攤分
				BigDecimal TransApprAmt = tNegTrans.getApprAmt();// 撥付金額
				int TransExportDate = tNegTrans.getExportDate();// 撥付製檔日
				int TransExportAcDate = tNegTrans.getExportAcDate();// 撥付出帳日
				BigDecimal TransTempRepayAmt = tNegTrans.getTempRepayAmt();// 暫收抵繳金額
				BigDecimal TransOverRepayAmt = tNegTrans.getOverRepayAmt();// 溢收抵繳金額
				BigDecimal TransPrincipalAmt = tNegTrans.getPrincipalAmt();// 本金金額
				BigDecimal TransInterestAmt = tNegTrans.getInterestAmt();// 利息金額
				BigDecimal TransOverAmt = tNegTrans.getOverAmt();// 轉入溢收金額
				int TransIntStartDate = tNegTrans.getIntStartDate();// 繳息起日
				int TransIntEndDate = tNegTrans.getIntEndDate();// 繳息迄日
				int TransRepayPeriod = tNegTrans.getRepayPeriod();// 還款期數
				int TransRepayDate = tNegTrans.getRepayDate();// 入帳還款日期

				BigDecimal TransOrgAccuOverAmt = tNegMain.getAccuOverAmt();// 累溢繳款(交易前)
				BigDecimal TransAccuOverAmt = tNegTrans.getAccuOverAmt();// 累溢繳款(交易後)
				int TransShouldPayPeriod = tNegTrans.getShouldPayPeriod();// 本次應還期數
				BigDecimal TransDueAmt = tNegTrans.getDueAmt();// 期金
				BigDecimal NewTransDueAmt = BigDecimal.ZERO;// 本次應還金額
				if (mainDueAmt.compareTo(BigDecimalZero) == 0) {
					// E5009 資料檢核錯誤
					throw new LogicException(titaVo, "E5009", "[期金]為0");
				}

				if (TransDueAmt.compareTo(BigDecimalZero) == 0) {
					// 期金
					TransDueAmt = mainDueAmt;

				}

				// 用日期去算到今天這戶頭應該還幾期
				// NextPayDate 下次應繳日 與本次會計日相比差了多少個月
				if (mainNextPayDate == 0) {
					// E5009 資料檢核錯誤
					throw new LogicException(titaVo, "E5009", "[下次應繳日]未填寫.請至L5071查詢後維護.");
				} else {
					TransShouldPayPeriod = DiffMonth(1, mainNextPayDate, Today) + 1;// 月份差異
				}
				// -----
				if (TransShouldPayPeriod != 0) {
					NewTransDueAmt = mainDueAmt.multiply(BigDecimal.valueOf(TransShouldPayPeriod));// Trans 本次應還金額
				}

				int DiffMonth = DiffMonth(1, mainFirstDueDate, Today) + 1;// 月份差異=首次應繳日 與 會計日的月份差+1
				mainAccuDueAmt = mainDueAmt.multiply(BigDecimal.valueOf(DiffMonth));// Main累應還金額=Main期金*Trans本次應還期數

				// TransTxKind 0:正常;1:溢繳;2:短繳;3:提前還本;4:結清;5:提前清償;6:待處理
				// 0:正常-匯入款＋溢收款 >= 期款
				// 1:溢繳(預收多期)-匯入款 > 期款
				// 2:短繳-匯入款＋溢收款 < 期款
				// 3:提前還本-匯入款 >= 5期期款
				// 4:結清-匯入款＋溢收款 >=最後一期期款
				// 5:提前清償-匯入款＋溢收款 >= 剩餘期款
				// 6:待處理
				// ※入帳訂正需在撥付產擋前，依反向順序訂正

				// String OringnalTxKind=TransTxKind;
				transTxKind = "";

				// 剩餘本利和=總本金餘額(1+年利率/12/100)
				BigDecimal SumPrincipalAndIntRate = mainPrincipalBal.add(mainPrincipalBal.multiply(mainIntRate).divide(new BigDecimal(1200), 0, RoundingMode.HALF_UP));//

				// 可分配金額=交易金額+累繳金額+累溢收-(累償還本金+累償還利息)
				BigDecimal CanCountAmt = transTxAmt.add(mainAccuTempAmt).add(mainAccuOverAmt).subtract((mainRepayPrincipal.add(mainRepayInterest)));

				// 剩餘本利和<=可分配金額
				if (SumPrincipalAndIntRate.compareTo(CanCountAmt) <= 0) {
					// 5:提前清償
					// 4:結清
					TransRepayPeriod = remainPeriod;

					// 剩餘期數>1
					if (remainPeriod > 1) {
						// 5:提前清償
						transTxKind = "5";
					} else {
						// 4:結清
						transTxKind = "4";
					}
				} else {
					// 還款期數
					TransRepayPeriod = Integer.parseInt(String.valueOf(CanCountAmt.divide(mainDueAmt, 2, RoundingMode.DOWN).setScale(0, RoundingMode.DOWN)));

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

				this.info("NegCom TransTxKind=[" + transTxKind + "],NewTxKind=[" + NewTxKind + "]");
				// 0:一開始試算 1:異動後 試算 2:UPDATE
				// USER 自己輸入 交易別(提前還本可改為溢繳)
				if (("1").equals(TrialFunc) || ("2").equals(TrialFunc)) {
					if (!NewTxKind.equals(transTxKind)) {
						transTxKind = NewTxKind;
					}
				}
				this.info("NegCom TransTxKind After Trial=[" + transTxKind + "]");
				BigDecimal DueAmtMultiTransRepayPeriod = BigDecimalZero;

				if (("0").equals(transTxKind)) {
					TransTxStatus = 2;// 交易狀態 0:未入帳;1:待處理;2:已入帳;
					// 正常
					// 0:正常->可分配金額 = 期款

					// 還款期數
					TransRepayPeriod = 1;
					TransReturnAmt = BigDecimalZero;// 退還金額

					int CompareTransTxAmtMainDueAmt = transTxAmt.compareTo(mainDueAmt);
					if (CompareTransTxAmtMainDueAmt >= 0) {
						// 交易金額>=期金
						TransTempRepayAmt = mainDueAmt;// Trans暫收抵繳金額=期金
						TransOverRepayAmt = BigDecimalZero;// 溢收抵繳金額
						TransOverAmt = transTxAmt.subtract(mainDueAmt);// 轉入溢收金額
					} else {
						// 交易金額<期金

						// 可分配金額=交易金額+累繳金額+累溢收-(累償還本金+累償還利息)
						// CanCountAmt =
						// TransTxAmt.add(MainAccuTempAmt).add(MainAccuOverAmt).subtract((MainRepayPrincipal.add(MainRepayInterest)));

						TransTempRepayAmt = mainDueAmt;// Trans暫收抵繳金額=繳交金額
						TransOverRepayAmt = mainDueAmt.subtract(transTxAmt);// 溢收抵繳金額
						TransOverAmt = CanCountAmt.subtract(mainDueAmt);// 轉入溢收金額
					}

					mainAccuTempAmt = mainAccuTempAmt.add(transTxAmt);// Main累繳金額
					mainAccuOverAmt = mainAccuOverAmt.add(TransOverAmt); // Main累溢收金額=Main累溢收金額+轉入溢收金額-溢收抵繳金額
					mainRepaidPeriod = mainRepaidPeriod + TransRepayPeriod;// 已繳期數
					TransRepayDate = Today; // 入帳還款日
				} else if (("2").equals(transTxKind)) {
					// 2:短繳-匯入款＋溢收款 < 期款
					TransTxStatus = 2;// 交易狀態 0:未入帳;1:待處理;2:已入帳;
					TransRepayPeriod = 0;
					TransReturnAmt = BigDecimalZero;// 退還金額
					TransTempRepayAmt = BigDecimalZero;// Trans暫收抵繳金額
					TransOverRepayAmt = BigDecimalZero;// 溢收抵繳金額
					TransOverAmt = transTxAmt;// 轉入溢收金額

					mainAccuTempAmt = mainAccuTempAmt.add(transTxAmt);// Main累繳金額
					// MainAccuOverAmt=MainAccuOverAmt.add(TransOverAmt).subtract(TransOverRepayAmt);
					// //Main累溢收金額+轉入溢收金額-溢收抵繳金額
					mainRepaidPeriod = mainRepaidPeriod + TransRepayPeriod;// 已繳期數
					TransPrincipalAmt = BigDecimal.ZERO;// 還本本金-沖銷本金
					TransInterestAmt = BigDecimal.ZERO;// 還本利息-沖銷利息
					TransRepayDate = Today; // 入帳還款日
				} else if (("1").equals(transTxKind)) {
					// 1:溢繳(預收多期)-匯入款 > 期款
					TransTxStatus = 2;// 交易狀態 0:未入帳;1:待處理;2:已入帳;
					TransReturnAmt = BigDecimalZero;// 退還金額
					// 繳交期數
					// TransRepayPeriod =
					// Integer.parseInt(String.valueOf(CanCountAmt.divide(MainDueAmt, 2,
					// RoundingMode.DOWN).setScale(0, RoundingMode.DOWN)));

					TransTempRepayAmt = mainDueAmt.multiply(new BigDecimal(TransRepayPeriod));// Trans暫收抵繳金額
					TransOverAmt = CanCountAmt.subtract(TransTempRepayAmt);// 轉入溢收金額
					if (TransOverAmt.compareTo(BigDecimalZero) >= 0) {
						TransOverRepayAmt = BigDecimalZero;// 溢收抵繳金額
					} else {
						TransOverRepayAmt = TransOverAmt.multiply(new BigDecimal(-1));// 溢收抵繳金額
						TransOverAmt = BigDecimalZero;
					}

					this.info("NegService Case 1 TransTxKind=[" + transTxKind + "],TransRepayPeriod=[" + TransRepayPeriod + "]");

					mainAccuTempAmt = mainAccuTempAmt.add(transTxAmt);// Main累繳金額
					mainAccuOverAmt = mainAccuOverAmt.add(TransOverAmt); // Main累溢收金額+轉入溢收金額-溢收抵繳金額
					mainRepaidPeriod = mainRepaidPeriod + TransRepayPeriod;// 已繳期數
					TransRepayDate = Today; // 入帳還款日
				} else if (("3").equals(transTxKind)) {
					// 3:提前還本-匯入款 >= 5期期款
					// 只繳一期,其他的全部算繳交本金
					TransTxStatus = 2;// 交易狀態 0:未入帳;1:待處理;2:已入帳;
					TransReturnAmt = BigDecimalZero;// 退還金額
					// 繳交期數
					TransRepayPeriod = 1;

					this.info("NegService Case 3 TransTxKind=[" + transTxKind + "],TransRepayPeriod=[" + TransRepayPeriod + "]");
					// 不用用到 累溢收金額
					TransTempRepayAmt = transTxAmt;// Trans暫收抵繳金額
					TransOverRepayAmt = BigDecimalZero;// 溢收抵繳金額

					if (TransTempRepayAmt.compareTo(transTxAmt) <= 0) {
						// TransOverAmt = TransTxAmt.subtract(TransTempRepayAmt);// 轉入溢收金額
						TransOverAmt = BigDecimalZero;// 轉入溢收金額
					} else {
						throw new LogicException(titaVo, "E5006", "發生未預期的錯誤");
					}
					mainAccuTempAmt = mainAccuTempAmt.add(transTxAmt);// Main累繳金額
					mainAccuOverAmt = mainAccuOverAmt.add(TransOverAmt).subtract(TransOverRepayAmt); // Main累溢收金額+轉入溢收金額-溢收抵繳金額
					mainRepaidPeriod = mainRepaidPeriod + TransRepayPeriod;// 已繳期數
					TransRepayDate = Today; // 入帳還款日
				} else if (("4").equals(transTxKind) || ("5").equals(transTxKind)) {
					// 4:結清-匯入款＋溢收款 >=最後一期期款
					// 5:提前清償-匯入款＋溢收款 >= 剩餘期款
					// 繳交期數
					TransTxStatus = 2;// 交易狀態 0:未入帳;1:待處理;2:已入帳;

					// 結清
					status = "3";
					newStatusDate = Today;

					DueAmtMultiTransRepayPeriod = mainDueAmt.multiply(BigDecimal.valueOf(TransRepayPeriod));
					if (DueAmtMultiTransRepayPeriod.compareTo(transTxAmt) > 0) {
						// 用到 累溢收金額
						TransTempRepayAmt = transTxAmt;// Trans暫收抵繳金額
						TransOverRepayAmt = DueAmtMultiTransRepayPeriod.subtract(TransTempRepayAmt);// 溢收抵繳金額
						TransOverAmt = BigDecimalZero;// 轉入溢收金額
					} else {
						// 不用用到 累溢收金額
						TransTempRepayAmt = DueAmtMultiTransRepayPeriod;// Trans暫收抵繳金額
						TransOverRepayAmt = BigDecimalZero;// 溢收抵繳金額

						if (TransTempRepayAmt.compareTo(transTxAmt) <= 0) {
							TransOverAmt = transTxAmt.subtract(TransTempRepayAmt);// 轉入溢收金額
						} else {
							throw new LogicException(titaVo, "E5006", "發生未預期的錯誤");
						}
						// TransOverAmt=;//轉入溢收金額
					}
					// BigDecimal TransOrgAccuOverAmt=tNegMain.getAccuOverAmt();//累溢繳款(交易前)
					mainAccuOverAmt = mainAccuOverAmt.add(TransOverAmt).subtract(TransOverRepayAmt); // Main累溢收金額+轉入溢收金額-溢收抵繳金額
					TransReturnAmt = TransReturnAmt.add(mainAccuOverAmt);
					mainAccuTempAmt = mainAccuTempAmt.add(transTxAmt).subtract(mainAccuOverAmt);// Main累繳金額
					mainAccuOverAmt = BigDecimalZero;
					TransOverAmt = BigDecimalZero;
					mainRepaidPeriod = mainRepaidPeriod + TransRepayPeriod;// 已繳期數
					TransRepayDate = Today; // 入帳還款日

				} else if (("6").equals(transTxKind)) {
					// 6:待處理
					TransRepayPeriod = 0;
					TransTxStatus = 1;// 交易狀態 0:未入帳;1:待處理;2:已入帳;
				} else {
					TransRepayPeriod = 0;
					transTxKind = "9";
					TransTxStatus = 1;// 待處理
				}

				// 新壽攤分
				// 此處由L5702入帳時寫入,L5707只是把撥付日期給壓上
				if (transTxKind != null && transTxKind.length() != 0 && !("6").equals(transTxKind)) {
					Slice<NegFinShare> slNegFinShare = sNegFinShareService.FindAllFinCode(ThisCustNo, ThisCaseSeq, this.index, this.limit, titaVo);
					List<NegFinShare> lNegFinShare = slNegFinShare == null ? null : slNegFinShare.getContent();
					if (lNegFinShare != null && lNegFinShare.size() != 0) {
						int lNegFinShareS = lNegFinShare.size();
						int lNegFinShareS1 = lNegFinShareS - 1;
						BigDecimal SumShareAmt = BigDecimal.ZERO;
						for (int i = 0; i < lNegFinShareS; i++) {
							NegFinShare tNegFinShare = lNegFinShare.get(i);
							String FinCode = tNegFinShare.getFinCode();// 債權機構
							if (FinCode != null && FinCode.length() != 0) {

								BigDecimal AmtRatio = tNegFinShare.getAmtRatio();// 債權比例%
								BigDecimal ShareAmt = BigDecimal.ZERO;// 依債權比例分配金額
								if (i == lNegFinShareS1) {
									// 最後一筆資料要用減的
									ShareAmt = transTxAmt.subtract(SumShareAmt);
								} else {
									ShareAmt = transTxAmt.multiply(AmtRatio, MathContext.DECIMAL128).divide(new BigDecimal(100), 0, RoundingMode.HALF_UP);
									SumShareAmt = SumShareAmt.add(ShareAmt);
								}
								if (("458").equals(FinCode)) {
									TransSklShareAmt = ShareAmt;
									// 458 不寫入NegAppr01
									continue;
								}
								// NegAppr01 撥款時寫入 L5707 只把撥款日期壓上
								if (ChekUpdDB == 1) {
									// 檢查是否已有入帳還款日
									if (tNegTrans.getRepayDate() != 0) {
										this.info("NegCom 入帳還款日 tNegTransId=[" + tNegTrans.getNegTransId().toString() + "]");
										// E5009 資料檢核錯誤
										throw new LogicException(titaVo, "E5009", "[入帳還款日]已有資料,不可異動交易別! 戶號=[" + tNegTrans.getCustNo() + "]");
									}
									// 異動資料庫
									switch (IsMainFin) {
									case "Y":
										// 最大債權
										// 寫入NegAppr01
										NegAppr01Id tNegAppr01Id = new NegAppr01Id();
										tNegAppr01Id.setFinCode(FinCode);
										tNegAppr01Id.setAcDate(tNegTrans.getAcDate());
										tNegAppr01Id.setTitaTlrNo(tNegTrans.getTitaTlrNo());
										tNegAppr01Id.setTitaTxtNo(tNegTrans.getTitaTxtNo());
										NegAppr01 tNegAppr01 = sNegAppr01Service.findById(tNegAppr01Id, titaVo);

										NegFinAcct tNegFinAcct = sNegFinAcctService.findById(FinCode, titaVo);
										if (tNegFinAcct != null) {
											BigDecimal SumCustNoFinCode = SumCustNoFinCode(tNegTrans.getCustNo(), tNegTrans.getCaseSeq(), FinCode, titaVo);
											NegAppr01 tNegAppr01Upd = new NegAppr01();
											tNegAppr01Upd.setNegAppr01Id(tNegAppr01Id);
											tNegAppr01Upd.setCustNo(ThisCustNo);// 戶號
											tNegAppr01Upd.setCaseSeq(ThisCaseSeq);// 案件序號
											tNegAppr01Upd.setCaseKindCode(caseKindCode);// 案件種類
											tNegAppr01Upd.setApprAmt(ShareAmt);// 撥付金額
											SumCustNoFinCode = SumCustNoFinCode.add(ShareAmt);
											tNegAppr01Upd.setAccuApprAmt(SumCustNoFinCode);// 累計撥付金額
											tNegAppr01Upd.setAmtRatio(AmtRatio);// 撥付比例
											tNegAppr01Upd.setExportDate(0);// 製檔日期
											tNegAppr01Upd.setApprDate(0);// 撥付日期
											tNegAppr01Upd.setBringUpDate(0);// 提兌日
											tNegAppr01Upd.setRemitBank(FinCode);// 匯款銀行
											tNegAppr01Upd.setRemitAcct(tNegFinAcct.getRemitAcct());// 匯款帳號
											tNegAppr01Upd.setDataSendUnit(tNegFinAcct.getDataSendSection());// 資料傳送單位
											tNegAppr01Upd.setApprAcDate(0);// 撥付傳票日
											tNegAppr01Upd.setReplyCode("");// 回應代碼
											if (tNegAppr01 != null) {
												// E0013 程式邏輯有誤
												throw new LogicException(titaVo, "E0013", "最大債權撥付資料檔資料已存在");
											} else {
												// Insert
												try {
													sNegAppr01Service.insert(tNegAppr01Upd, titaVo);
												} catch (DBException e) {
													// E0005 新增資料時，發生錯誤
													throw new LogicException(titaVo, "E0005", "最大債權撥付資料檔");
												}
											}

										} else {
											// E5009 資料檢核錯誤
											throw new LogicException(titaVo, "E5009", "債務協商債權機構帳戶檔(未找到銀行代碼[" + FinCode + "]請至L5703登錄)");
										}
										break;
									case "N":
										// 一般債權
										// 不用考慮
										break;
									default:

									}
								}
							} else {
								// E5009 資料檢核錯誤
								throw new LogicException(titaVo, "E5009", "[提兌日]為空值");
							}
						}
					}
					TransApprAmt = transTxAmt.subtract(TransSklShareAmt);// 撥付金額
				}

//				// User 自行輸入的資料 退還金額專用
//				if (("1").equals(Trial) || ("2").equals(Trial)) {
////					if (TransReturnAmt.compareTo(BigDecimal.valueOf(Double.parseDouble(NewReturnAmt))) != 0) {
////						TransReturnAmt = BigDecimal.valueOf(Double.parseDouble(NewReturnAmt));
////					}
//				} else if (("3").equals(Trial)) {
//
//				}

				List<String> NotDo = new ArrayList<String>();
				NotDo.add("2");// 短繳
				NotDo.add("6");// 待處理
				if (!NotDo.contains(transTxKind)) {
					// 參考L5970的公式
					int iLoanTerm = nper(mainPrincipalBal, mainDueAmt, mainIntRate);// 期數 (剩餘本金,期金,利率)
					this.info("NegCom iLoanTerm=[" + iLoanTerm + "]");
					BigDecimal OBalance = mainPrincipalBal;// 剩餘本金
					this.info("NegCom OBalance=[" + mainPrincipalBal + "]");
					int Count = 0;
					for (int i = 0; i <= iLoanTerm; i++) {
						Count++;
						if (OBalance.compareTo(BigDecimal.ZERO) <= 0) {
							break;
						}
						// iDueAmt 期金
						BigDecimal OInterest = (OBalance.multiply(mainIntRate)).divide(new BigDecimal(1200), 0, RoundingMode.HALF_UP);// 應繳利息
						BigDecimal OPrincipal = BigDecimal.ZERO;// 應繳本金
						int UseAdjPrincipal[] = { 0, 0, 0, 0 };// 是否使用到調整應繳本金
						if (OBalance.compareTo(mainDueAmt) <= 0) {
							// 剩餘本金<期金 => 應繳本金=剩餘本金
							OPrincipal = OBalance;
							this.info("NegCom 1 OPrincipal=[" + OPrincipal + "]");
							UseAdjPrincipal[0] = 1;
						} else {
							// 剩餘本金>=期金
							OPrincipal = mainDueAmt.subtract(OInterest);// 應繳本金
							UseAdjPrincipal[1] = 1;
							this.info("NegCom 2 OPrincipal=[" + OPrincipal + "]");
						}
						BigDecimal Osum = OInterest.add(OPrincipal);// 本利合計
						if (Osum.compareTo(mainDueAmt) == 0) {
							// 本利和=期金
							OBalance = OBalance.subtract(OPrincipal);
							this.info("NegCom 4 OPrincipal=[" + OPrincipal + "]");
							UseAdjPrincipal[2] = 1;
						} else {
							if (Osum.compareTo(mainDueAmt) < 0) {
								// 本利和<期金
								OPrincipal = OBalance;
								OBalance = BigDecimal.ZERO;
								Osum = OPrincipal.add(OInterest);
								this.info("NegCom 3 OPrincipal=[" + OPrincipal + "]");
								UseAdjPrincipal[3] = 1;
							} else if (Osum.compareTo(mainDueAmt) > 0) {
								// 本利和>期金
								// E5009 資料檢核錯誤
								throw new LogicException(titaVo, "E5009", "[本利和]>[期金]");
							}
						}

						if (("3").equals(transTxKind)) {
							// 提前還本-期數只有一
							this.info("NegCom 本次沖銷本金 期金-應繳利息 OPrincipal=[" + OPrincipal + "]");
							if (UseAdjPrincipal[2] != 0 && UseAdjPrincipal[1] != 0) {
								// 剩餘本金==期金 || 剩餘本金>=期金
								OPrincipal = OPrincipal.add(transTxAmt).subtract(mainDueAmt);
							} else {
								if (UseAdjPrincipal[0] != 0 || UseAdjPrincipal[3] != 0) {
									// 剩餘本金<期金 || 本利和<期金

									// OPrincipal = OPrincipal.add(TransTxAmt).subtract(OInterest);
								}
							}
						}
						TransPrincipalAmt = TransPrincipalAmt.add(OPrincipal);// 本金金額=應繳本金
						TransInterestAmt = TransInterestAmt.add(OInterest);// 利息金額=應繳利息

						this.info("NegCom Count=[" + Count + "],OPrincipal=[" + OPrincipal + "],OInterest=[" + OInterest + "]");
						this.info("NegCom TransPrincipalAmt=[" + TransPrincipalAmt + "],TransInterestAmt=[" + TransInterestAmt + "]");
						if (i >= TransRepayPeriod - 1) {
							break;
						}

					}
				}

				mainRepayPrincipal = mainRepayPrincipal.add(TransPrincipalAmt);// 還本本金
				mainRepayInterest = mainRepayInterest.add(TransInterestAmt); // 還本利息
				this.info("NegCom MainPrincipalBal=[" + mainPrincipalBal + "],MainRepayPrincipal=[" + mainRepayPrincipal + "]-3");
				mainPrincipalBal = mainPrincipalBal.subtract(TransPrincipalAmt);
				// 這裡可能會產生負值
				if (mainPrincipalBal.compareTo(BigDecimalZero) < 0) {
					// E5009 資料檢核錯誤
					throw new LogicException(titaVo, "E5009", "[本金餘額]不可為負值.");
				}
				this.info("NegCom MainPrincipalBal=[" + mainPrincipalBal + "]-4");
				mainAccuSklShareAmt = mainAccuSklShareAmt.add(TransSklShareAmt);
				TransPrincipalBal = mainPrincipalBal;// 本金餘額
				TransAccuOverAmt = mainAccuOverAmt;// 累溢繳款(交易後)

				if (TransTxStatus == 2) {
					// 已入帳
					if (mainFirstDueDate != 0) {
						if (mainNextPayDate == 0) {
							// 下次應繳日
							mainNextPayDate = mainFirstDueDate;
						}

					} else {
						// E5009 資料檢核錯誤
						throw new LogicException(titaVo, "E5009", "[首次應繳日]未填寫,請至L5701查詢後修改.");
					}

					if (TransRepayPeriod == 0) { // 短繳
						TransIntStartDate = 0;
						TransIntEndDate = 0;
						newPayIntDate = mainPayIntDate;
					} else {
						TransIntStartDate = mainNextPayDate;
						TransIntEndDate = AdjMonth(TransIntStartDate, TransRepayPeriod - 1, 0);
						newPayIntDate = TransIntEndDate;
					}
					// 下次應繳日+本次還款期數
					mainNextPayDate = AdjMonth(mainNextPayDate, TransRepayPeriod, 0);

					/*
					 * Main-FirstDueDate 首次應繳日 Main-LastDueDate 還款結束日 MainNextPayDate 下次應繳日
					 * Main-PayIntDate 繳息迄日 Tran-IntStartDate 繳息起日 Tran-IntEndDate 繳息迄日
					 */
					// MainNextPayDate
				}

				if (("4").equals(transTxKind) || ("5").equals(transTxKind)) {
					// 交易別 4:結清;5:提前清償
					// 下次應繳日 歸零
					mainNextPayDate = 0;
				}
				if (ChekUpdDB == 1) {
					if (("6").equals(transTxKind)) {
						transTxKind = tNegTrans.getTxKind();// 保留原始資料
					}
					// 更新資料庫
					// getEntityManager

					NegMain tNegMainUpd = new NegMain();
					tNegMainUpd = sNegMainService.holdById(tNegMainId, titaVo);

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

					NegTrans tNegTransUpd = new NegTrans();
					tNegTransUpd = sNegTransService.findById(tNegTrans.getNegTransId(), titaVo);

					int TrialUpdOrIns = 0;// 0:Insert 1:Update
					if (tNegTransUpd != null) {
						TrialUpdOrIns = 1;
					} else {
						tNegTransUpd = tNegTrans;
						TrialUpdOrIns = 0;
					}
					tNegTransUpd.setTxStatus(TransTxStatus);// 交易狀態0:未入帳;1:待處理;2:已入帳;
					tNegTransUpd.setTxKind(transTxKind);// 交易別0:正常;1:溢繳;2:短繳;3:提前還本;4:結清;5:提前清償;6:待處理
					tNegTransUpd.setTxAmt(transTxAmt);// 交易金額
					tNegTransUpd.setPrincipalBal(TransPrincipalBal);// 本金餘額
					tNegTransUpd.setReturnAmt(TransReturnAmt);// 退還金額
					tNegTransUpd.setSklShareAmt(TransSklShareAmt);// 新壽攤分
					tNegTransUpd.setApprAmt(TransApprAmt);// 撥付金額
					tNegTransUpd.setExportDate(TransExportDate);// 撥付製檔日
					tNegTransUpd.setExportAcDate(TransExportAcDate);// 撥付出帳日
					tNegTransUpd.setTempRepayAmt(TransTempRepayAmt);// 暫收抵繳金額
					tNegTransUpd.setOverRepayAmt(TransOverRepayAmt);// 溢收抵繳金額
					tNegTransUpd.setPrincipalAmt(TransPrincipalAmt);// 本金金額
					tNegTransUpd.setInterestAmt(TransInterestAmt);// 利息金額
					tNegTransUpd.setOverAmt(TransOverAmt);// 轉入溢收金額
					tNegTransUpd.setIntStartDate(TransIntStartDate);// 繳息起日
					tNegTransUpd.setIntEndDate(TransIntEndDate);// 繳息迄日
					tNegTransUpd.setRepayPeriod(TransRepayPeriod);// 還款期數
					tNegTransUpd.setRepayDate(TransRepayDate);// 入帳還款日期
					tNegTransUpd.setOrgAccuOverAmt(TransOrgAccuOverAmt);// 累溢繳款(交易前)
					tNegTransUpd.setAccuOverAmt(TransAccuOverAmt);// 累溢繳款(交易後)
					tNegTransUpd.setShouldPayPeriod(TransShouldPayPeriod);// 本次應還期數
					tNegTransUpd.setDueAmt(TransDueAmt);// 期金

					String ThisSeqNo = tNegTransUpd.getThisSeqNo().trim();
					String ThisSeqNoAdd = "";
					if (ThisSeqNo != null && ThisSeqNo.length() != 0) {
						// Update
						ThisSeqNoAdd = String.valueOf(Integer.parseInt(ThisSeqNo) + 1);
					} else {
						// Insert
						ThisSeqNo = "0";
						ThisSeqNoAdd = "0";
					}

					tNegTransUpd.setThisEntdy(titaVo.getEntDyI());// 本次交易日
					tNegTransUpd.setThisKinbr(titaVo.getKinbr());// 本次分行別
					tNegTransUpd.setThisTlrNo(titaVo.getTlrNo());// 本次交易員代號
					tNegTransUpd.setThisTxtNo(titaVo.getTxtNo());// 本次交易序號
					tNegTransUpd.setThisSeqNo(ThisSeqNoAdd);// 本次序號
					try {
						// Key
						tNegMainUpd.setCaseSeq(tNegMainId.getCaseSeq());
						tNegMainUpd.setCustNo(tNegMainId.getCustNo());

						tNegMainUpd.setThisAcDate(tNegTrans.getNegTransId().getAcDate());// AcDate 本次會計日期
						tNegMainUpd.setThisTitaTlrNo(tNegTrans.getNegTransId().getTitaTlrNo());// TitaTlrNo 本次經辦
						tNegMainUpd.setThisTitaTxtNo(tNegTrans.getNegTransId().getTitaTxtNo());// TitaTxtNo 本次交易序號

						tNegMainUpd.setLastAcDate(tNegMainOrg.getThisAcDate());// LastAcDate 上次會計日期
						tNegMainUpd.setLastTitaTlrNo(tNegMainOrg.getThisTitaTlrNo());// LastTitaTlrNo 上次經辦
						tNegMainUpd.setLastTitaTxtNo(tNegMainOrg.getThisTitaTxtNo());// LastTitaTxtNo 上次交易序號

						tNegMainUpd.setCreateDate(tNegMainOrg.getCreateDate());
						tNegMainUpd.setCreateEmpNo(tNegMainOrg.getCreateEmpNo());
						sNegMainService.update2(tNegMainUpd, titaVo);// 資料異動後-1
						dataLog.setEnv(titaVo, tNegMainOrg, tNegMainUpd);// 資料異動後-2
						dataLog.exec();// 資料異動後-3
						if (TrialUpdOrIns == 1) {
							NegTrans OrgtNegTrans = sNegTransService.holdById(tNegTrans.getNegTransId(), titaVo);
							// Key
							tNegTransUpd.setAcDate(tNegTrans.getNegTransId().getAcDate());
							tNegTransUpd.setTitaTlrNo(tNegTrans.getNegTransId().getTitaTlrNo());
							tNegTransUpd.setTitaTxtNo(tNegTrans.getNegTransId().getTitaTxtNo());

							tNegTransUpd.setLastEntdy(tNegTransUpd.getThisEntdy());// 上次交易日
							tNegTransUpd.setLastKinbr(tNegTransUpd.getThisKinbr());// 上次分行別
							tNegTransUpd.setLastTlrNo(tNegTransUpd.getThisTlrNo());// 上次交易員代號
							tNegTransUpd.setLastTxtNo(tNegTransUpd.getThisTxtNo());// 上次交易序號
							tNegTransUpd.setLastSeqNo(ThisSeqNo);// 上次序號

							tNegTransUpd.setCreateDate(tNegTransUpd.getCreateDate());
							tNegTransUpd.setCreateEmpNo(tNegTransUpd.getCreateEmpNo());
							sNegTransService.holdById(tNegTransUpd.getNegTransId(), titaVo);
							sNegTransService.update2(tNegTransUpd, titaVo);// 資料異動後-1
							dataLog.setEnv(titaVo, OrgtNegTrans, tNegTransUpd);// 資料異動後-2
							dataLog.exec();// 資料異動後-3
						} else {
							sNegTransService.insert(tNegTransUpd, titaVo);
						}
						// Insert 的時候寫入Temp
						NegMainInsertTxTemp(titaVo, tNegMainOrg, tNegTrans);

					} catch (DBException e) {
						// E0007 更新資料時，發生錯誤
						throw new LogicException(titaVo, "E0007", e.getErrorMsg());
					}
				}

				// 4.丟資料回去
				MapNeg.put("CustId", custId);
				MapNeg.put("CaseSeq", String.valueOf(tNegMainId.getCaseSeq()));
				MapNeg.put("CustNo", custNo);
				MapNeg.put("CustName", custName);
				MapNeg.put("Status", status);
				MapNeg.put("NewStatus", status);
				MapNeg.put("CustLoanKind", tNegMain.getCustLoanKind());
				MapNeg.put("ApplDate", String.valueOf(tNegMain.getApplDate()));// 協商申請日
				MapNeg.put("MainDueAmt", String.valueOf(tNegMain.getDueAmt()));
				MapNeg.put("TotalPeriod", String.valueOf(tNegMain.getTotalPeriod()));
				MapNeg.put("IntRate", String.valueOf(mainIntRate));
				MapNeg.put("RepaidPeriod", String.valueOf(tNegMain.getRepaidPeriod()));// 已繳期數
				MapNeg.put("NewRepaidPeriod", String.valueOf(mainRepaidPeriod));// 已繳期數

				MapNeg.put("IsMainFin", IsMainFin);
				MapNeg.put("MainFinCode", mainFinCode);

				MapNeg.put("OrgPrincipalBal", String.valueOf(tNegMain.getPrincipalBal()));// 總本金餘額(舊)
				MapNeg.put("NewPrincipalBal", String.valueOf(mainPrincipalBal));// 總本金餘額(新)
				this.info("NegCom MainPrincipalBal=[" + mainPrincipalBal + "]-5");
				MapNeg.put("OrgAccuTempAmt", String.valueOf(tNegMain.getAccuTempAmt()));// 累繳金額
				MapNeg.put("NewAccuTempAmt", String.valueOf(mainAccuTempAmt));

				MapNeg.put("OrgAccuOverAmt", String.valueOf(tNegMain.getAccuOverAmt()));// 累溢收金額
				MapNeg.put("NewAccuOverAmt", String.valueOf(mainAccuOverAmt));

				MapNeg.put("OrgAccuDueAmt", String.valueOf(tNegMain.getAccuDueAmt()));// 累應還金額
				MapNeg.put("NewAccuDueAmt", String.valueOf(mainAccuDueAmt));

				MapNeg.put("OrgAccuSklShareAmt", String.valueOf(tNegMain.getAccuSklShareAmt()));// 新壽攤分
				MapNeg.put("NewAccuSklShareAmt", String.valueOf(mainAccuSklShareAmt));

				MapNeg.put("OrgNextPayDate", String.valueOf(tNegMain.getNextPayDate()));// 下期應繳日
				MapNeg.put("NewNextPayDate", String.valueOf(DcToRoc(mainNextPayDate)));//

				MapNeg.put("OrgRepayPrincipal", String.valueOf(tNegMain.getRepayPrincipal()));// 還本本金
				MapNeg.put("NewRepayPrincipal", String.valueOf(mainRepayPrincipal));

				MapNeg.put("OrgRepayInterest", String.valueOf(tNegMain.getRepayInterest()));// 還本利息
				MapNeg.put("NewRepayInterest", String.valueOf(mainRepayInterest));

				MapNeg.put("TwoStepCode", String.valueOf(tNegMain.getTwoStepCode()));
				MapNeg.put("ChgCondDate", String.valueOf(tNegMain.getChgCondDate()));

				MapNeg.put("PayIntDate", String.valueOf(tNegMain.getPayIntDate()));// 繳息迄日
				MapNeg.put("NewPayIntDate", String.valueOf(DcToRoc(newPayIntDate)));// 繳息迄日

				// MapNeg.put("MainPayIntDate", String.valueOf(MainPayIntDate));//繳息迄日

				MapNeg.put("OrgStatusDate", String.valueOf(tNegMain.getStatusDate()));// 戶況日期
				MapNeg.put("NewStatusDate", String.valueOf(DcToRoc(newStatusDate)));

				MapNeg.put("TransAcDate", String.valueOf(tNegTrans.getAcDate()));
				MapNeg.put("TransTitaTlrNo", String.valueOf(tNegTrans.getTitaTlrNo()));
				MapNeg.put("TransTitaTxtNo", String.valueOf(tNegTrans.getTitaTxtNo()));
				MapNeg.put("TransCustNo", custNo);
				MapNeg.put("TransCaseSeq", String.valueOf(tNegTrans.getCaseSeq()));
				MapNeg.put("TransEntryDate", String.valueOf(tNegTrans.getEntryDate()));// 入帳日期

				MapNeg.put("TransTxStatus", String.valueOf(tNegTrans.getTxStatus()));// 交易狀態
				MapNeg.put("NewTransTxStatus", String.valueOf(TransTxStatus));// 交易狀態0:未入帳;1:待處理;2:已入帳;

				MapNeg.put("TransTxKind", String.valueOf(tNegTrans.getTxKind()));// 交易別0:正常;1:溢繳;2:短繳;3:提前還本;4:結清;5:提前清償;6:待處理
				MapNeg.put("NewTransTxKind", String.valueOf(transTxKind));

				MapNeg.put("TransTxAmt", String.valueOf(tNegTrans.getTxAmt()));// 交易金額
				MapNeg.put("NewTransTxAmt", String.valueOf(transTxAmt));

				MapNeg.put("TransPrincipalBal", String.valueOf(orgTransPrincipalBal));// 本金餘額
				MapNeg.put("NewTransPrincipalBal", String.valueOf(TransPrincipalBal));

				MapNeg.put("TransReturnAmt", String.valueOf(tNegTrans.getReturnAmt()));// 退還金額
				MapNeg.put("NewTransReturnAmt", String.valueOf(TransReturnAmt));

				MapNeg.put("TransSklShareAmt", String.valueOf(tNegTrans.getSklShareAmt()));// 新壽攤分
				MapNeg.put("NewTransSklShareAmt", String.valueOf(TransSklShareAmt));

				MapNeg.put("TransApprAmt", String.valueOf(tNegTrans.getApprAmt()));// 撥付金額
				MapNeg.put("NewTransApprAmt", String.valueOf(TransApprAmt));

				MapNeg.put("TransExportDate", String.valueOf(tNegTrans.getExportDate()));// 撥付製檔日
				// MapNeg.put("NewTransExportDate",String.valueOf(DcToRoc(TransExportDate)));--

				MapNeg.put("TransExportAcDate", String.valueOf(tNegTrans.getExportAcDate()));// 撥付出帳日
				// MapNeg.put("NewTransExportAcDate",String.valueOf(DcToRoc(TransExportAcDate)));

				MapNeg.put("TransTempRepayAmt", String.valueOf(tNegTrans.getTempRepayAmt()));// 暫收抵繳金額
				MapNeg.put("NewTransTempRepayAmt", String.valueOf(TransTempRepayAmt));

				MapNeg.put("TransOverRepayAmt", String.valueOf(tNegTrans.getOverRepayAmt()));// 溢收抵繳金額
				MapNeg.put("NewTransOverRepayAmt", String.valueOf(TransOverRepayAmt));

				MapNeg.put("TransPrincipalAmt", String.valueOf(tNegTrans.getPrincipalAmt()));// 本金金額
				MapNeg.put("NewTransPrincipalAmt", String.valueOf(TransPrincipalAmt));

				MapNeg.put("TransInterestAmt", String.valueOf(tNegTrans.getInterestAmt()));// 利息金額
				MapNeg.put("NewTransInterestAmt", String.valueOf(TransInterestAmt));

				MapNeg.put("TransOverAmt", String.valueOf(tNegTrans.getOverAmt()));// 轉入溢收金額
				MapNeg.put("NewTransOverAmt", String.valueOf(TransOverAmt));

				MapNeg.put("TransIntStartDate", String.valueOf(tNegTrans.getIntStartDate()));// 繳息起日
				MapNeg.put("NewTransIntStartDate", String.valueOf(DcToRoc(TransIntStartDate)));

				MapNeg.put("TransIntEndDate", String.valueOf(tNegTrans.getIntEndDate()));// 繳息迄日
				MapNeg.put("NewTransIntEndDate", String.valueOf(DcToRoc(TransIntEndDate)));

				MapNeg.put("TransRepayPeriod", String.valueOf(tNegTrans.getRepayPeriod()));// 還款期數
				MapNeg.put("NewTransRepayPeriod", String.valueOf(TransRepayPeriod));

				MapNeg.put("TransShouldPayPeriod", String.valueOf(tNegTrans.getShouldPayPeriod()));// 本次應還期數
				MapNeg.put("NewTransShouldPayPeriod", String.valueOf(TransShouldPayPeriod));

				MapNeg.put("TransDueAmt", String.valueOf(tNegTrans.getDueAmt()));// 期金
				MapNeg.put("NewTransDueAmt", String.valueOf(NewTransDueAmt));

				MapNeg.put("TransRepayDate", String.valueOf(tNegTrans.getRepayDate()));// 入帳還款日期
				MapNeg.put("NewTransRepayDate", String.valueOf(TransRepayDate));

				MapNeg.put("TransOrgAccuOverAmt", String.valueOf(tNegMain.getAccuOverAmt()));// 累溢繳款(交易前)(前)
				MapNeg.put("NewTransOrgAccuOverAmt", String.valueOf(TransOrgAccuOverAmt));// 累溢繳款(交易前)(後)
				MapNeg.put("TransAccuOverAmt", String.valueOf(tNegTrans.getAccuOverAmt()));// 累溢繳款(交易後)(前)
				MapNeg.put("NewTransAccuOverAmt", String.valueOf(TransAccuOverAmt));// 累溢繳款(交易後)(後)

				MapNeg.put("Trial", TrialFunc);
				if (ChekUpdDB == 1) {
					// CaseKindCode 1:協商 2:調解 3:更生 4:清算
					switch (caseKindCode) {
					case "1":
						// 協商->JcicZ050

						Map<String, String> JcicZ050Map = new HashMap<String, String>();
						JcicZ050Map.put("CustId", custId);
						JcicZ050Map.put("PayDate", String.valueOf(tNegTrans.getEntryDate()));// 繳款日期-NegTrans入帳日
						JcicZ050Map.put("RcDate", String.valueOf(tNegMain.getApplDate()));// 協商申請日-NegMain協商申請日
						JcicZ050Map.put("SubmitKey", mainFinCode);// 報送單位代號-最大債權
						JcicZ050Map.put("TranKey", "A");// 交易代碼 A:新增;C:異動;D:刪除
						JcicZ050Map.put("PayAmt", String.valueOf(transTxAmt));// 本次繳款金額
						JcicZ050Map.put("SumRepayActualAmt", String.valueOf(mainAccuTempAmt));// 累計實際還款金額
						JcicZ050Map.put("SumRepayShouldAmt", String.valueOf(mainAccuDueAmt));// 累應還款金額
						String JcicZ050Status = "";
						if (("4").equals(transTxKind) || ("5").equals(transTxKind)) {
							// 4:結清-匯入款＋溢收款 >=最後一期期款
							// 5:提前清償-匯入款＋溢收款 >= 剩餘期款
							// Status="";//債權結案註記-0:請選擇;Y:債務全數清償;N:債務尚未全數清償
							JcicZ050Status = "Y";
						} else {
							JcicZ050Status = "N";
						}
						JcicZ050Map.put("Status", JcicZ050Status);// 債權結案註記-0:請選擇;Y:債務全數清償;N:債務尚未全數清償
						String SecondRepayYM = String.valueOf(tNegMain.getDeferYMStart());
						if (SecondRepayYM != null && SecondRepayYM.length() >= 6) {
							SecondRepayYM = SecondRepayYM.substring(0, 6);
						} else {
							SecondRepayYM = "";
						}
						JcicZ050Map.put("SecondRepayYM", SecondRepayYM);// 進入第二階梯還款年月-延期繳款年月(起)
						JcicZ050Map.put("OutJcicTxtDate", "0");// 轉出JCIC文字檔日期-預設為0
						DbJcicZ050(titaVo, JcicZ050Map);
						break;
					case "2":
						// 調解->Jcic450

						DbJcicZ450(titaVo, custId, tNegMain, tNegTrans);
//						無JCIC報送日期 才可更正
//						債協->JCICZ050 債務人繳款資料檔案 ->結案時報送 JCICZ046
					case "3":
						// 更生->JcicZ067 ->變更為JcicZ573
						DbJcicZ573(titaVo, custId, tNegMain, tNegTrans);
						break;
					case "4":
						// 清算->Jcic

						break;
					}

				}
			} else {
				throw new LogicException(titaVo, "E0001", "查無債務協商案件主檔的資料請查驗");
			}
		} else

		{
			throw new LogicException(titaVo, "E0001", "查無債務協商交易檔的資料請查驗");
		}

		return MapNeg;
	}

	public void DbJcicZ573(TitaVo titaVo, String CustId, NegMain tNegMain, NegTrans tNegTrans) throws LogicException {
		JcicZ573Id tJcicZ573Id = new JcicZ573Id();
		tJcicZ573Id.setApplyDate(tNegMain.getApplDate());
		tJcicZ573Id.setCustId(CustId);
		tJcicZ573Id.setPayDate(tNegTrans.getEntryDate());
		tJcicZ573Id.setSubmitKey(tNegMain.getMainFinCode());
		JcicZ573 tJcicZ573 = sJcicZ573Service.holdById(tJcicZ573Id, titaVo);
		if (tJcicZ573 != null) {
			if (tJcicZ573.getOutJcicTxtDate() != 0) {
				// E5009 資料檢核錯誤
				throw new LogicException(titaVo, "E5009", "已有JCICZ573文字檔日期");
			}
		}
		String tranKey = "";
		BigDecimal payAmt = tNegTrans.getTxAmt();
		BigDecimal totalPayAmt = tNegMain.getAccuTempAmt();
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
					throw new LogicException(titaVo, "E0007", "JcicZ573");
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
					throw new LogicException(titaVo, "E0005", "JcicZ573");
				}
			}
		} else {
			// 訂正
			if (tJcicZ573 == null) {
				throw new LogicException(titaVo, "E0003", "JcicZ573-未存在資料");
			}
			// 訂正最後一筆
			if (tJcicZ573.getPayAmt() == payAmt.intValue()) {
				// DELETE
				try {
					sJcicZ573Service.delete(tJcicZ573, titaVo);
				} catch (DBException e) {
					// E0008 刪除資料時，發生錯誤
					throw new LogicException(titaVo, "E0008", "JcicZ573");
				}
				// 訂正非最後一筆
			} else {
				tJcicZ573.setPayAmt(tJcicZ573.getPayAmt() - payAmt.intValue());
				tJcicZ573.setTotalPayAmt(tJcicZ573.getTotalPayAmt() - payAmt.intValue());
				try {
					sJcicZ573Service.update(tJcicZ573, titaVo);
				} catch (DBException e) {
					// E0007 更新資料時，發生錯誤
					throw new LogicException(titaVo, "E0007", "JcicZ573-未存在資料");
				}
			}
		}
	}

	public void DbJcicZ046(TitaVo titaVo, JcicZ046Id tJcicZ046Id) throws LogicException {
		// 已報送過毀諾結案,則不再自動報送毀諾後清償,改由人工建檔
		Slice<JcicZ046> slJcicZ046 = sJcicZ046Service.HadZ046(tJcicZ046Id.getCustId(), tJcicZ046Id.getRcDate(), tJcicZ046Id.getSubmitKey(), 0, Integer.MAX_VALUE, titaVo);
		List<JcicZ046> lJcicZ046 = slJcicZ046 == null ? null : slJcicZ046.getContent();
		if (lJcicZ046 != null) {
			for (JcicZ046 sJcicZ046 : lJcicZ046) {
				this.info("***DbJcicZ046***getBreakCode=" + sJcicZ046.getBreakCode());
				if (("00").equals(sJcicZ046.getBreakCode())) {
					return;
				}
			}
		}

		JcicZ046 tJcicZ046 = sJcicZ046Service.holdById(tJcicZ046Id, titaVo);
		if (tJcicZ046 != null) {
			if (tJcicZ046.getOutJcicTxtDate() != 0) {
				// E5009 資料檢核錯誤
				throw new LogicException(titaVo, "E5009", "已有JCICZ046文字檔日期");
			}
		}
		if (titaVo.isHcodeNormal()) {
			// 正向
			if (tJcicZ046 != null) {
				// 已存在不可新增
				throw new LogicException(titaVo, "E0002", "JcicZ046");

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
					throw new LogicException(titaVo, "E0005", "JcicZ046");
				}
			}

		} else {
			// 訂正
			if (tJcicZ046 == null) {
				// E0007 更新資料時，發生錯誤
				throw new LogicException(titaVo, "E0007", "JcicZ046-未存在資料");
			}
			// DELETE
			try {
				sJcicZ046Service.delete(tJcicZ046, titaVo);
			} catch (DBException e) {
				// E0008 刪除資料時，發生錯誤
				throw new LogicException(titaVo, "E0008", "JcicZ046");
			}

		}

	}

	public void DbJcicZ450(TitaVo titaVo, String CustId, NegMain tNegMain, NegTrans tNegTrans) throws LogicException {
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
				throw new LogicException(titaVo, "E5009", "已有JCICZ450文字檔日期");
			}
		}
		BigDecimal payAmt = tNegTrans.getTxAmt();// 本次繳款金額
		String payStatus = "";// 債權結案註記 Y N
		BigDecimal sumRepayActualAmt = tNegMain.getAccuTempAmt();// 累計實際還款金額-累繳金額
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
					throw new LogicException(titaVo, "E0007", "JcicZ40");
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
					throw new LogicException(titaVo, "E0005", "JcicZ450");
				}
			}

//			TranKey	交易代碼
//			CustId	債務人IDN *
//			SubmitKey	報送單位代號*
//			ApplyDate	款項統一收付申請日*
//			BankId	異動債權金機構代號*
//			PayDate	繳款日期
//			PayAmt	本次繳款金額
//			SumRepayActualAmt	累計實際還款金額
//			SumRepayShouldAmt	截至目前累計應還款金額
//			PayStatus	債權結案註記
//			OutJcicTxtDate	轉JCIC文字檔日期

		} else {
			// 訂正
			if (tJcicZ450 == null) {
				// E0003 修改資料不存在
				throw new LogicException(titaVo, "E0003", "JcicZ450處理時發生錯誤");
			}
			payStatus = tJcicZ450.getPayStatus(); // 訂正前交易別
			// 訂正最後一筆
			if (tJcicZ450.getPayAmt() == payAmt.intValue()) {
				// Delete
				try {
					sJcicZ450Service.delete(tJcicZ450, titaVo);
				} catch (DBException e) {
					// E0008 刪除資料時，發生錯誤
					throw new LogicException(titaVo, "E0008", "JcicZ450");
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
					throw new LogicException(titaVo, "E0007", "JcicZ450");
				}
			}
		}
		return;
	}

	public void DbJcicZ050(TitaVo titaVo, Map<String, String> JcicZ050Map) throws LogicException {
		// Func 1:正向 0:訂正
		// int Today = titaVo.getOrgEntdyI();// 會計日期
		int CloseDate = 0;// 結案日期

		String CustId = JcicZ050Map.get("CustId");
		int PayDate = Integer.parseInt(JcicZ050Map.get("PayDate"));// 繳款日期
		int RcDate = Integer.parseInt(JcicZ050Map.get("RcDate"));// 協商申請日
		String SubmitKey = JcicZ050Map.get("SubmitKey");// 報送單位代號-最大債權
		String TranKey = JcicZ050Map.get("TranKey");// 交易代碼 A:新增;C:異動;D:刪除
		int PayAmt = parse.stringToInteger(JcicZ050Map.get("PayAmt"));// 本次繳款金額
		int SumRepayActualAmt = parse.stringToInteger(JcicZ050Map.get("SumRepayActualAmt"));// 累計實際還款金額
		int SumRepayShouldAmt = parse.stringToInteger(JcicZ050Map.get("SumRepayShouldAmt"));// 累應還款金額
		String Status = JcicZ050Map.get("Status");// 債權結案註記-0:請選擇;Y:債務全數清償;N:債務尚未全數清償
		int SecondRepayYM = parse.stringToInteger(JcicZ050Map.get("SecondRepayYM"));// 進入第二階梯還款年月
		int OutJcicTxtDate = Integer.parseInt(JcicZ050Map.get("OutJcicTxtDate"));// 轉出JCIC文字檔日期

		CloseDate = PayDate;

		JcicZ050Id tJcicZ050Id = new JcicZ050Id();
		tJcicZ050Id.setCustId(CustId);
		tJcicZ050Id.setPayDate(PayDate);
		tJcicZ050Id.setRcDate(RcDate);
		tJcicZ050Id.setSubmitKey(SubmitKey);
		JcicZ050 tJcicZ050 = sJcicZ050Service.holdById(tJcicZ050Id, titaVo);
		if (tJcicZ050 != null) {
			// Status = tJcicZ050.getStatus();
			if (tJcicZ050.getOutJcicTxtDate() != 0) {
				// E5009 資料檢核錯誤
				throw new LogicException(titaVo, "E5009", "已有JCICZ050文字檔日期");
			}
		}

		if (titaVo.isHcodeNormal()) {
			// 正向
			if (tJcicZ050 != null) {
				tJcicZ050.setPayAmt(tJcicZ050.getPayAmt() + PayAmt);
				tJcicZ050.setSumRepayActualAmt(tJcicZ050.getSumRepayActualAmt() + PayAmt);
				tJcicZ050.setStatus(Status);
				try {
					sJcicZ050Service.update(tJcicZ050, titaVo);
				} catch (DBException e) {
					// E0007 更新資料時，發生錯誤
					throw new LogicException(titaVo, "E0007", "JcicZ050");
				}
			} else {
				// Insert
				TranKey = "A";// 交易代碼 A:新增;C:異動;D:刪除
				tJcicZ050 = new JcicZ050();
				tJcicZ050.setJcicZ050Id(tJcicZ050Id);

				tJcicZ050.setTranKey(TranKey);
				tJcicZ050.setPayAmt(PayAmt);
				tJcicZ050.setSumRepayActualAmt(SumRepayActualAmt);
				tJcicZ050.setSumRepayShouldAmt(SumRepayShouldAmt);
				tJcicZ050.setStatus(Status);
				tJcicZ050.setSecondRepayYM(SecondRepayYM);
				tJcicZ050.setOutJcicTxtDate(OutJcicTxtDate);
				String iKey = "";
				iKey = UUID.randomUUID().toString().toUpperCase().replaceAll("-", "");
				tJcicZ050.setUkey(iKey);
				try {
					sJcicZ050Service.insert(tJcicZ050, titaVo);
				} catch (DBException e) {
					// E0005 新增資料時，發生錯誤
					throw new LogicException(titaVo, "E0005", "JcicZ050");
				}
			}

		} else {
			// 訂正
			if (tJcicZ050 == null) {
				throw new LogicException(titaVo, "E0003", "JcicZ050處理時發生錯誤");
			}
			Status = tJcicZ050.getStatus(); // 訂正前交易別
			// 訂正最後一筆
			if (tJcicZ050.getPayAmt() == PayAmt) {
				try {
					sJcicZ050Service.delete(tJcicZ050, titaVo);
				} catch (DBException e) {
					// E0008 刪除資料時，發生錯誤
					throw new LogicException(titaVo, "E0008", "JcicZ050");
				}

				// 訂正非最後一筆
			} else {
				tJcicZ050.setPayAmt(tJcicZ050.getPayAmt() - PayAmt);
				tJcicZ050.setSumRepayActualAmt(tJcicZ050.getSumRepayActualAmt() - PayAmt);
				tJcicZ050.setStatus("N");
				try {
					sJcicZ050Service.update(tJcicZ050, titaVo);
				} catch (DBException e) {
					// E0007 更新資料時，發生錯誤
					throw new LogicException(titaVo, "E0007", "JcicZ050");
				}
			}
		}

		// 債務全數清償
		if (("Y").equals(Status)) {
			JcicZ046Id tJcicZ046Id = new JcicZ046Id();
			tJcicZ046Id.setCloseDate(CloseDate);
			tJcicZ046Id.setCustId(CustId);
			tJcicZ046Id.setRcDate(RcDate);
			tJcicZ046Id.setSubmitKey(SubmitKey);
			DbJcicZ046(titaVo, tJcicZ046Id);
		}

		return;
	}

	// 訂正
	public void NegRepayEraseRoutine(TitaVo titaVo) throws LogicException {
		this.info("NegRepayEraseRoutine ...");

		int EntDyI = Integer.parseInt(l5051ServiceImpl.RocToDc(String.valueOf(titaVo.getEntDyI())));
		Slice<TxTemp> slTxTemp = sTxTempService.txTempTxtNoEq(EntDyI, titaVo.getOrgKin(), titaVo.getOrgTlr(), titaVo.getOrgTno(), this.index, this.limit, titaVo);
		List<TxTemp> lTxTemp = slTxTemp == null ? null : slTxTemp.getContent();
		if (lTxTemp == null || lTxTemp.size() == 0) {
			throw new LogicException(titaVo, "E0001", "交易暫存檔 日期=" + EntDyI + " 分行別 = " + titaVo.getOrgKin() + " 交易員代號 = " + titaVo.getOrgTlr() + " 交易序號 = " + titaVo.getOrgTno()); // 查詢資料不存在
		}
		for (TxTemp tx : lTxTemp) {
			String SeqNo = tx.getSeqNo();
			this.info("NegRepayEraseRoutine tx SeqNo=[" + SeqNo + "]");
			TempVo tTempVo = new TempVo();
			tTempVo = tTempVo.getVo(tx.getText());

			String CustNo = tTempVo.get("CustNo");// 戶號
			String CaseSeq = tTempVo.get("CaseSeq");// 案件序號
			String CaseKindCode = tTempVo.get("CaseKindCode");// 案件種類
			String Status = tTempVo.get("Status");// 戶況
			String CustLoanKind = tTempVo.get("CustLoanKind");// 債權戶別
			String DeferYMStart = tTempVo.get("DeferYMStart");// 延期繳款年月(起)
			String DeferYMEnd = tTempVo.get("DeferYMEnd");// 延期繳款年月(訖)
			String ApplDate = tTempVo.get("ApplDate");// 協商申請日
			String DueAmt = tTempVo.get("DueAmt");// 月付金(期款)
			String TotalPeriod = tTempVo.get("TotalPeriod");// 期數
			String IntRate = tTempVo.get("IntRate");// 計息條件(利率)
			String FirstDueDate = tTempVo.get("FirstDueDate");// 首次應繳日
			String LastDueDate = tTempVo.get("LastDueDate");// 還款結束日
			String IsMainFin = tTempVo.get("IsMainFin");// 是否最大債權
			String TotalContrAmt = tTempVo.get("TotalContrAmt");// 簽約總金額
			String MainFinCode = tTempVo.get("MainFinCode");// 最大債權機構
			String PrincipalBal = tTempVo.get("PrincipalBal");// 總本金餘額
			String AccuTempAmt = tTempVo.get("AccuTempAmt");// 累繳金額
			String AccuOverAmt = tTempVo.get("AccuOverAmt");// 累溢收金額
			String AccuDueAmt = tTempVo.get("AccuDueAmt");// 累應還金額
			String AccuSklShareAmt = tTempVo.get("AccuSklShareAmt");// 累新壽分攤金額
			String RepaidPeriod = tTempVo.get("RepaidPeriod");// 已繳期數
			String TwoStepCode = tTempVo.get("TwoStepCode");// 二階段註記
			String ChgCondDate = tTempVo.get("ChgCondDate");// 申請變更還款條件日
			String NextPayDate = tTempVo.get("NextPayDate");// 下次應繳日
			String PayIntDate = tTempVo.get("PayIntDate");// 繳息迄日
			String RepayPrincipal = tTempVo.get("RepayPrincipal");// 還本本金
			String RepayInterest = tTempVo.get("RepayInterest");// 還本利息
			String StatusDate = tTempVo.get("StatusDate");// 戶況日期
			String ThisAcDate = tTempVo.get("ThisAcDate");// 本次會計日期
			String ThisTitaTlrNo = tTempVo.get("ThisTitaTlrNo");// 本次經辦
			String ThisTitaTxtNo = tTempVo.get("ThisTitaTxtNo");// 本次交易序號
			String LastAcDate = tTempVo.get("LastAcDate");// 上次會計日期
			String LastTitaTlrNo = tTempVo.get("LastTitaTlrNo");// 上次經辦
			String LastTitaTxtNo = tTempVo.get("LastTitaTxtNo");// 上次交易序號
//			String CreateDate = tTempVo.get("CreateDate");//建檔日期時間
//			String CreateEmpNo = tTempVo.get("CreateEmpNo");//建檔人員
//			String LastUpdate = tTempVo.get("LastUpdate");//最後更新日期時間
//			String LastUpdateEmpNo = tTempVo.get("LastUpdateEmpNo");//最後更新人員

			String TranAcDate = tTempVo.get("TranAcDate");// 會計日期
			String TranTitaTlrNo = tTempVo.get("TranTitaTlrNo");// 經辦
			String TranTitaTxtNo = tTempVo.get("TranTitaTxtNo");// 交易序號
			String TranCustNo = tTempVo.get("TranCustNo");// 戶號
			String TranCaseSeq = tTempVo.get("TranCaseSeq");// 案件序號
			String TranEntryDate = tTempVo.get("TranEntryDate");// 入帳日期
			String TranTxStatus = tTempVo.get("TranTxStatus");// 交易狀態
			String TranTxKind = tTempVo.get("TranTxKind");// 交易別
			String TranTxAmt = tTempVo.get("TranTxAmt");// 交易金額
			String TranPrincipalBal = tTempVo.get("TranPrincipalBal");// 本金餘額
			String TranReturnAmt = tTempVo.get("TranReturnAmt");// 退還金額
			String TranSklShareAmt = tTempVo.get("TranSklShareAmt");// 新壽攤分
			String TranApprAmt = tTempVo.get("TranApprAmt");// 撥付金額
			String TranExportDate = tTempVo.get("TranExportDate");// 撥付製檔日
			String TranExportAcDate = tTempVo.get("TranExportAcDate");// 撥付出帳日
			String TranTempRepayAmt = tTempVo.get("TranTempRepayAmt");// 暫收抵繳金額
			String TranOverRepayAmt = tTempVo.get("TranOverRepayAmt");// 溢收抵繳金額
			String TranPrincipalAmt = tTempVo.get("TranPrincipalAmt");// 本金金額
			String TranInterestAmt = tTempVo.get("TranInterestAmt");// 利息金額
			String TranOverAmt = tTempVo.get("TranOverAmt");// 轉入溢收金額
			String TranIntStartDate = tTempVo.get("TranIntStartDate");// 繳息起日
			String TranIntEndDate = tTempVo.get("TranIntEndDate");// 繳息迄日
			String TranRepayPeriod = tTempVo.get("TranRepayPeriod");// 還款期數
			String TranRepayDate = tTempVo.get("TranRepayDate");// 入帳還款日期
			String TranOrgAccuOverAmt = tTempVo.get("TranOrgAccuOverAmt");// 累溢繳款(交易前)
			String TranAccuOverAmt = tTempVo.get("TranAccuOverAmt");// 累溢繳款(交易後)
			String TranShouldPayPeriod = tTempVo.get("TranShouldPayPeriod");// 本次應還期數
			String TranDueAmt = tTempVo.get("TranDueAmt");// 期金
			String TranThisEntdy = tTempVo.get("TranThisEntdy");// 本次交易日
			String TranThisKinbr = tTempVo.get("TranThisKinbr");// 本次分行別
			String TranThisTlrNo = tTempVo.get("TranThisTlrNo");// 本次交易員代號
			String TranThisTxtNo = tTempVo.get("TranThisTxtNo");// 本次交易序號
			String TranThisSeqNo = tTempVo.get("TranThisSeqNo");// 本次序號
			String TranLastEntdy = tTempVo.get("TranLastEntdy");// 上次交易日
			String TranLastKinbr = tTempVo.get("TranLastKinbr");// 上次分行別
			String TranLastTlrNo = tTempVo.get("TranLastTlrNo");// 上次交易員代號
			String TranLastTxtNo = tTempVo.get("TranLastTxtNo");// 上次交易序號
			String TranLastSeqNo = tTempVo.get("TranLastSeqNo");// 上次序號

			int IntCustNo = parse.stringToInteger(CustNo);
			int IntCaseSeq = parse.stringToInteger(CaseSeq);
			NegMainId tNegMainId = new NegMainId();
			tNegMainId.setCustNo(IntCustNo);
			tNegMainId.setCaseSeq(IntCaseSeq);

			NegMain tNegMain = sNegMainService.holdById(tNegMainId, titaVo);
			if (tNegMain != null) {
				this.info("NegCom NegRepayEraseRoutine tNegMain!=null ");
				NegMain tNegMainOrg = (NegMain) dataLog.clone(tNegMain);// 資料異動前
				tNegMain.setCustNo(IntCustNo);// 戶號
				tNegMain.setCaseSeq(IntCaseSeq);// 案件序號
				tNegMain.setCaseKindCode(CaseKindCode);// 案件種類
				tNegMain.setStatus(Status);// 戶況
				tNegMain.setCustLoanKind(CustLoanKind);// 債權戶別
				tNegMain.setDeferYMStart(parse.stringToInteger(DeferYMStart));// 延期繳款年月(起)
				tNegMain.setDeferYMEnd(parse.stringToInteger(DeferYMEnd));// 延期繳款年月(訖)
				tNegMain.setApplDate(parse.stringToInteger(ApplDate));// 協商申請日
				tNegMain.setDueAmt(parse.stringToBigDecimal(DueAmt));// 月付金(期款)
				tNegMain.setTotalPeriod(parse.stringToInteger(TotalPeriod));// 期數
				tNegMain.setIntRate(parse.stringToBigDecimal(IntRate));// 計息條件(利率)
				tNegMain.setFirstDueDate(parse.stringToInteger(FirstDueDate));// 首次應繳日
				tNegMain.setLastDueDate(parse.stringToInteger(LastDueDate));// 還款結束日
				tNegMain.setIsMainFin(IsMainFin);// 是否最大債權
				tNegMain.setTotalContrAmt(parse.stringToBigDecimal(TotalContrAmt));// 簽約總金額
				tNegMain.setMainFinCode(MainFinCode);// 最大債權機構
				tNegMain.setPrincipalBal(parse.stringToBigDecimal(PrincipalBal));// 總本金餘額
				tNegMain.setAccuTempAmt(parse.stringToBigDecimal(AccuTempAmt));// 累繳金額
				tNegMain.setAccuOverAmt(parse.stringToBigDecimal(AccuOverAmt));// 累溢收金額
				tNegMain.setAccuDueAmt(parse.stringToBigDecimal(AccuDueAmt));// 累應還金額
				tNegMain.setAccuSklShareAmt(parse.stringToBigDecimal(AccuSklShareAmt));// 累新壽分攤金額
				tNegMain.setRepaidPeriod(parse.stringToInteger(RepaidPeriod));// 已繳期數
				tNegMain.setTwoStepCode(TwoStepCode);// 二階段註記
				tNegMain.setChgCondDate(parse.stringToInteger(ChgCondDate));// 申請變更還款條件日
				tNegMain.setNextPayDate(parse.stringToInteger(NextPayDate));// 下次應繳日
				tNegMain.setPayIntDate(parse.stringToInteger(PayIntDate));// 繳息迄日
				tNegMain.setRepayPrincipal(parse.stringToBigDecimal(RepayPrincipal));// 還本本金
				tNegMain.setRepayInterest(parse.stringToBigDecimal(RepayInterest));// 還本利息
				tNegMain.setStatusDate(parse.stringToInteger(StatusDate));// 戶況日期
				tNegMain.setThisAcDate(parse.stringToInteger(ThisAcDate));// 本次會計日期
				tNegMain.setThisTitaTlrNo(ThisTitaTlrNo);// 本次經辦
				tNegMain.setThisTitaTxtNo(parse.stringToInteger(ThisTitaTxtNo));// 本次交易序號
				tNegMain.setLastAcDate(parse.stringToInteger(LastAcDate));// 上次會計日期
				tNegMain.setLastTitaTlrNo(LastTitaTlrNo);// 上次經辦
				tNegMain.setLastTitaTxtNo(parse.stringToInteger(LastTitaTxtNo));// 上次交易序號
				// sNegMainService.update(tNegMain,titaVo);
				try {
					sNegMainService.update2(tNegMain, titaVo);
				} catch (DBException e) {
					// E0007 更新資料時，發生錯誤
					throw new LogicException(titaVo, "E0007", "債務協商案件主檔");
				} // 資料異動後-1
				dataLog.setEnv(titaVo, tNegMainOrg, tNegMain);// 資料異動後-2
				dataLog.exec();// 資料異動後-3
				this.info("NegCom NegRepayEraseRoutine tNegMain Update Safe ");
			} else {
				this.info("NegCom NegRepayEraseRoutine tNegMain==null ");
				// E0006 鎖定資料時，發生錯誤
				throw new LogicException(titaVo, "E0006", "債務協商案件主檔");
			}
			int IntTranAcDate = parse.stringToInteger(TranAcDate);
			int IntTranTitaTxtNo = parse.stringToInteger(TranTitaTxtNo);

			NegTransId tNegTransId = new NegTransId();
			tNegTransId.setAcDate(IntTranAcDate);// 會計日期
			tNegTransId.setTitaTlrNo(TranTitaTlrNo);// 經辦
			tNegTransId.setTitaTxtNo(IntTranTitaTxtNo);// 交易序號
			NegTrans tNegTrans = sNegTransService.holdById(tNegTransId, titaVo);
			if (tNegTrans != null) {
				this.info("NegCom NegRepayEraseRoutine tNegTrans !=null ");
				NegTrans OrgNegTrans = (NegTrans) dataLog.clone(tNegTrans);// 資料異動前
				tNegTrans.setAcDate(IntTranAcDate);// 會計日期
				tNegTrans.setTitaTlrNo(TranTitaTlrNo);// 經辦
				tNegTrans.setTitaTxtNo(IntTranTitaTxtNo);// 交易序號
				tNegTrans.setCustNo(parse.stringToInteger(TranCustNo));// 戶號
				tNegTrans.setCaseSeq(parse.stringToInteger(TranCaseSeq));// 案件序號
				tNegTrans.setEntryDate(parse.stringToInteger(TranEntryDate));// 入帳日期
				tNegTrans.setTxStatus(parse.stringToInteger(TranTxStatus));// 交易狀態
				tNegTrans.setTxKind(TranTxKind);// 交易別
				tNegTrans.setTxAmt(parse.stringToBigDecimal(TranTxAmt));// 交易金額
				tNegTrans.setPrincipalBal(parse.stringToBigDecimal(TranPrincipalBal));// 本金餘額
				tNegTrans.setReturnAmt(parse.stringToBigDecimal(TranReturnAmt));// 退還金額
				tNegTrans.setSklShareAmt(parse.stringToBigDecimal(TranSklShareAmt));// 新壽攤分
				tNegTrans.setApprAmt(parse.stringToBigDecimal(TranApprAmt));// 撥付金額
				tNegTrans.setExportDate(parse.stringToInteger(TranExportDate));// 撥付製檔日
				tNegTrans.setExportAcDate(parse.stringToInteger(TranExportAcDate));// 撥付出帳日
				tNegTrans.setTempRepayAmt(parse.stringToBigDecimal(TranTempRepayAmt));// 暫收抵繳金額
				tNegTrans.setOverRepayAmt(parse.stringToBigDecimal(TranOverRepayAmt));// 溢收抵繳金額
				tNegTrans.setPrincipalAmt(parse.stringToBigDecimal(TranPrincipalAmt));// 本金金額
				tNegTrans.setInterestAmt(parse.stringToBigDecimal(TranInterestAmt));// 利息金額
				tNegTrans.setOverAmt(parse.stringToBigDecimal(TranOverAmt));// 轉入溢收金額
				tNegTrans.setIntStartDate(parse.stringToInteger(TranIntStartDate));// 繳息起日
				tNegTrans.setIntEndDate(parse.stringToInteger(TranIntEndDate));// 繳息迄日
				tNegTrans.setRepayPeriod(parse.stringToInteger(TranRepayPeriod));// 還款期數
				tNegTrans.setRepayDate(parse.stringToInteger(TranRepayDate));// 入帳還款日期
				tNegTrans.setOrgAccuOverAmt(parse.stringToBigDecimal(TranOrgAccuOverAmt));// 累溢繳款(交易前)
				tNegTrans.setAccuOverAmt(parse.stringToBigDecimal(TranAccuOverAmt));// 累溢繳款(交易後)
				tNegTrans.setShouldPayPeriod(parse.stringToInteger(TranShouldPayPeriod));// 本次應還期數
				tNegTrans.setDueAmt(parse.stringToBigDecimal(TranDueAmt));// 期金
				tNegTrans.setThisEntdy(parse.stringToInteger(TranThisEntdy));// 本次交易日
				tNegTrans.setThisKinbr(TranThisKinbr);// 本次分行別
				tNegTrans.setThisTlrNo(TranThisTlrNo);// 本次交易員代號
				tNegTrans.setThisTxtNo(TranThisTxtNo);// 本次交易序號
				tNegTrans.setThisSeqNo(TranThisSeqNo);// 本次序號
				tNegTrans.setLastEntdy(parse.stringToInteger(TranLastEntdy));// 上次交易日
				tNegTrans.setLastKinbr(TranLastKinbr);// 上次分行別
				tNegTrans.setLastTlrNo(TranLastTlrNo);// 上次交易員代號
				tNegTrans.setLastTxtNo(TranLastTxtNo);// 上次交易序號
				tNegTrans.setLastSeqNo(TranLastSeqNo);// 上次序號
				// sNegTransService.update(tNegTrans,titaVo);
				try {
					sNegTransService.update2(tNegTrans, titaVo);
				} catch (DBException e) {
					// E0007 更新資料時，發生錯誤
					throw new LogicException(titaVo, "E0007", "債務協商案件主檔");
				} // 資料異動後-1
				dataLog.setEnv(titaVo, OrgNegTrans, tNegTrans);// 資料異動後-2
				dataLog.exec();// 資料異動後-3
				this.info("NegCom NegRepayEraseRoutine tNegTrans Update Safe ");
			} else {
				this.info("NegCom NegRepayEraseRoutine tNegTrans ==null ");
				// E0006 鎖定資料時，發生錯誤
				throw new LogicException(titaVo, "E0006", "債務協商交易檔");
			}
			this.info("NegCom 訂正 tNegMain != null (" + String.valueOf(tNegMain != null) + ") && tNegTrans != null(" + String.valueOf(tNegTrans != null) + ")");
			if (tNegMain != null && tNegTrans != null) {
				String CustId = "";
				CustMain tCustMain = sCustMainService.custNoFirst(tNegTrans.getCustNo(), tNegTrans.getCustNo(), titaVo);
				if (tCustMain != null) {
					CustId = tCustMain.getCustId();
				} else {
					// E0001 查詢資料不存在
					throw new LogicException(titaVo, "E0001", "客戶資料主檔處理時發生錯誤 CustNo[" + IntCustNo + "]");
				}

				// NegAppr01 處理

				Slice<NegFinShare> slNegFinShare = sNegFinShareService.FindAllFinCode(tNegTrans.getCustNo(), tNegTrans.getCaseSeq(), this.index, this.limit, titaVo);
				List<NegFinShare> lNegFinShare = slNegFinShare == null ? null : slNegFinShare.getContent();
				if (lNegFinShare != null && lNegFinShare.size() != 0) {
					List<NegAppr01> lNegAppr01 = new ArrayList<NegAppr01>();
					for (NegFinShare tNegFinShare : lNegFinShare) {
						String FinCode = tNegFinShare.getFinCode();
//						if(("458").equals(FinCode)) {
//							//458跳過-458不會寫入NgeAppr01
//							continue;
//						}
						NegAppr01Id tNegAppr01Id = new NegAppr01Id();
						tNegAppr01Id.setAcDate(tNegTrans.getAcDate());
						tNegAppr01Id.setFinCode(FinCode);
						tNegAppr01Id.setTitaTlrNo(tNegTrans.getTitaTlrNo());
						tNegAppr01Id.setTitaTxtNo(tNegTrans.getTitaTxtNo());
						NegAppr01 tNegAppr01 = sNegAppr01Service.findById(tNegAppr01Id, titaVo);
						if (tNegAppr01 != null) {
							tNegAppr01 = sNegAppr01Service.holdById(tNegAppr01Id, titaVo);
							lNegAppr01.add(tNegAppr01);
						}
						this.info("NegCom 訂正 NegAppr01 (" + tNegAppr01Id.toString() + ")");
					}
					if (lNegAppr01 != null && lNegAppr01.size() != 0) {
						try {
							sNegAppr01Service.deleteAll(lNegAppr01, titaVo);
						} catch (DBException e) {
							// E0008 刪除資料時，發生錯誤
							throw new LogicException(titaVo, "E0001", "最大債權撥付資料檔");
						}
					}
				}
				// CaseKindCode 1:協商 2:調解 3:更生 4:清算
				// 協商->JcicZ050 債務人繳款資料
				// 調解->JCICZ450 前置調解債務人繳款資料
				// 更生->JCICZ573 更生債務人繳款資料
				// 清算-> x
				switch (CaseKindCode) {
				case "1":
					// 協商->JcicZ050
					Map<String, String> JcicZ050Map = new HashMap<String, String>();
					String TranKey = "";
					if (tNegTrans.getRepayDate() > 0) {
						TranKey = "C";
					} else {
						// 刪除
						TranKey = "D";
					}
					JcicZ050Map.put("CustId", CustId);
					JcicZ050Map.put("PayDate", String.valueOf(tNegTrans.getEntryDate()));// 繳款日期-NegTrans入帳日
					JcicZ050Map.put("RcDate", String.valueOf(tNegMain.getApplDate()));// 協商申請日-NegMain協商申請日
					JcicZ050Map.put("SubmitKey", tNegMain.getMainFinCode());// 報送單位代號-最大債權
					JcicZ050Map.put("TranKey", TranKey);// 交易代碼 A:新增;C:異動;D:刪除
					JcicZ050Map.put("PayAmt", String.valueOf(tNegTrans.getTxAmt()));// 本次繳款金額
					JcicZ050Map.put("SumRepayActualAmt", String.valueOf(tNegMain.getAccuTempAmt()));// 累計實際還款金額
					JcicZ050Map.put("SumRepayShouldAmt", String.valueOf(tNegMain.getAccuDueAmt()));// 累應還款金額
					String JcicZ050Status = "";
					String TransTxKind = tNegTrans.getTxKind();
					if (("4").equals(TransTxKind) || ("5").equals(TransTxKind)) {
						// 4:結清-匯入款＋溢收款 >=最後一期期款
						// 5:提前清償-匯入款＋溢收款 >= 剩餘期款
						// Status="";//債權結案註記-0:請選擇;Y:債務全數清償;N:債務尚未全數清償
						JcicZ050Status = "Y";
					} else {
						JcicZ050Status = "N";
					}
					JcicZ050Map.put("Status", JcicZ050Status);// 債權結案註記-0:請選擇;Y:債務全數清償;N:債務尚未全數清償
					String SecondRepayYM = String.valueOf(tNegMain.getDeferYMStart());
					if (SecondRepayYM != null && SecondRepayYM.length() >= 6) {
						SecondRepayYM = SecondRepayYM.substring(0, 6);
					} else {
						SecondRepayYM = "0";
					}
					JcicZ050Map.put("SecondRepayYM", SecondRepayYM);// 進入第二階梯還款年月-延期繳款年月(起)
					JcicZ050Map.put("OutJcicTxtDate", "0");// 轉出JCIC文字檔日期-預設為0
					DbJcicZ050(titaVo, JcicZ050Map);
					break;
				case "2":
					// 調解->Jcic (結清需增加JcicZ446)
					DbJcicZ450(titaVo, CustId, tNegMain, tNegTrans);
					break;
				case "3":
					// 更生->JcicZ067 ->變更為JcicZ573
					DbJcicZ573(titaVo, CustId, tNegMain, tNegTrans);
					break;
				case "4":
					// 清算->Jcic

					break;
				}

			}

		}
		return;
	}

	// 正向
	public void NegMainInsertTxTemp(TitaVo titaVo, NegMain tNegMainOrg, NegTrans tNegTrans) throws LogicException {
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

	public String[] NegServiceList1(int AcDate, int IsMainFin, int State, int Detail, int ExportDateYN, int IsBtn, TitaVo titaVo) throws LogicException {
		checkData(AcDate, IsMainFin, State, Detail, ExportDateYN, IsBtn);
		this.info("NegService AcDate=[" + AcDate + "],IsMainFin=[" + IsMainFin + "],State=[" + State + "],Detail=[" + Detail + "],ExportDateYN=[" + ExportDateYN + "],IsBtn=[" + IsBtn + "]");
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
			Data = l597AServiceImpl.FindL597A(l597AServiceImpl.FindData(this.index, this.limit, sql, titaVo, AcDate, IsMainFin, State, Detail, ExportDateYN, IsBtn), "L5074");
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
	public void checkData(int AcDate, int IsMainFin, int State, int Detail, int ExportDateYN, int IsBtn) throws LogicException {
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

	public int AdjMonth(Integer Ymd, int AddMonth, int AddDay) throws LogicException {
		if (Ymd != null && Ymd != 0) {
			if (String.valueOf(Ymd).length() == 7 || String.valueOf(Ymd).length() == 6) {
				Ymd = Ymd + 19110000;
			}
		}
		this.info("NegCom AddMonth(Integer Ymd=[" + Ymd + "],int AddMonth=[" + AddMonth + "],int AddDay[" + AddDay + "])");
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
		Date dt = null;
		try {
			dt = sdf.parse(String.valueOf(Ymd));
		} catch (ParseException e) {
			// E5009 資料檢核錯誤
			throw new LogicException(titaVo, "E5009", "日期轉換發生錯誤");
		}
		Calendar rightNow = Calendar.getInstance();
		rightNow.setTime(dt);
		if (AddMonth != 0) {
			rightNow.add(Calendar.MONTH, AddMonth);
		}
		if (AddDay != 0) {
			rightNow.add(Calendar.DAY_OF_MONTH, AddDay);
		}
		Date dt1 = rightNow.getTime();
		String reStr = sdf.format(dt1);
		Ymd = Integer.parseInt(reStr);
		return Ymd;
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

	public Map<String, String> NegMainSholdPay(NegMain tNegMain, int TrialDate) throws LogicException {
		// int Today=dateUtil.getNowIntegerForBC();
		// L5075使用本段程式
		String OOPayTerm = "";// 應還期數
		String OOPayAmt = "";// 應繳金額
		String OOOverDueAmt = "";// OOOverDueAmt 應催繳金額=OOPayTerm 應還期數-AccuOverAmt 累溢收

		BigDecimal DueAmt = tNegMain.getDueAmt();// 月付金(期款)
		// int TotalPeriod=tNegMain.getTotalPeriod();//期數

		// int FirstDueDate=tNegMain.getFirstDueDate();//首次應繳日
		int LastDueDate = tNegMain.getLastDueDate();// 還款結束日

		// BigDecimal TotalContrAmt=tNegMain.getTotalContrAmt();//簽約總金額
		int NextPayDate = tNegMain.getNextPayDate();// 下次應繳日
		// int RepaidPeriod=tNegMain.getRepaidPeriod();//已繳期數
		// BigDecimal RepayPrincipal=tNegMain.getRepayPrincipal();//還本本金
		// BigDecimal RepayInterest=tNegMain.getRepayInterest();//還本利息

		BigDecimal AccuOverAmt = tNegMain.getAccuOverAmt();// 累溢收
		int FinallDate = 0;// 最後結束的日期-計算剩餘期數
		if (TrialDate <= LastDueDate) {
			FinallDate = TrialDate;
		} else {
			FinallDate = LastDueDate;
		}

		int DiffPeriod = DiffMonth(1, NextPayDate, FinallDate) + 1;

		OOPayTerm = String.valueOf(DiffPeriod);
		OOPayAmt = String.valueOf(DueAmt.multiply(BigDecimal.valueOf(DiffPeriod)));

		OOOverDueAmt = String.valueOf(BigDecimal.valueOf(Double.parseDouble(OOPayAmt)).subtract(AccuOverAmt));
		Map<String, String> Data = new HashMap<String, String>();
		Data.put("OOPayTerm", OOPayTerm);
		Data.put("OOPayAmt", OOPayAmt);
		Data.put("OOOverDueAmt", OOOverDueAmt);
		return Data;
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
		Key = AcctCode + ConnectWord + String.valueOf(CustNo) + ConnectWord + String.valueOf(FacmNo) + ConnectWord + RvNo;
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
	public BigDecimal pmt(BigDecimal interestRate, int numberOfPayments, BigDecimal principal, BigDecimal futureValue, boolean paymentsDueAtBeginningOfPeriod) {
		final BigDecimal n = new BigDecimal(numberOfPayments);
		if (BigDecimal.ZERO.compareTo(interestRate) == 0) {
			return (futureValue.add(principal)).divide(n, MathContext.DECIMAL128).negate();
		} else {
			if (numberOfPayments == 1) {
				return (futureValue.add(principal).add(principal.multiply(interestRate))).divide(n, MathContext.DECIMAL128).negate();
			}
			final BigDecimal r1 = interestRate.add(BigDecimal.ONE);
			final BigDecimal pow = r1.pow(numberOfPayments);

			final BigDecimal divisor;
			if (paymentsDueAtBeginningOfPeriod) {
				divisor = r1.multiply(BigDecimal.ONE.subtract(pow));
			} else {
				divisor = BigDecimal.ONE.subtract(pow);
			}
			return (principal.multiply(pow).add(futureValue)).multiply(interestRate).divide(divisor, MathContext.DECIMAL128);
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
					LoanAmt = LoanAmt.subtract(DueAmt.subtract(LoanAmt.multiply(Rate).divide(new BigDecimal(1200), 5, RoundingMode.HALF_UP)));
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

//	private int AvoidPeriodOver(int TransRepayPeriod, int RemainPeriod) {
//		// 避免期數大於剩餘期數
//		// MainTotalPeriod 總期數
//		// MainRepaidPeriod 已繳期數
//		if (TransRepayPeriod > RemainPeriod) {
//			TransRepayPeriod = RemainPeriod;

//		}
//		return TransRepayPeriod;
//	}

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
		Slice<NegAppr01> sNegAppr01 = sNegAppr01Service.SumCustNoFinCode(CustNo, CaseSeq, FinCode, 0, Integer.MAX_VALUE, titaVo);
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
