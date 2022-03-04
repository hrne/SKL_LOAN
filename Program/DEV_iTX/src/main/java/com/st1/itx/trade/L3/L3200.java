package com.st1.itx.trade.L3;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

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
import com.st1.itx.db.domain.AcReceivable;
import com.st1.itx.db.domain.FacMain;
import com.st1.itx.db.domain.FacMainId;
import com.st1.itx.db.domain.FacProd;
import com.st1.itx.db.domain.LoanBook;
import com.st1.itx.db.domain.LoanBorMain;
import com.st1.itx.db.domain.LoanBorMainId;
import com.st1.itx.db.domain.LoanBorTx;
import com.st1.itx.db.domain.LoanBorTxId;
import com.st1.itx.db.domain.LoanIntDetail;
import com.st1.itx.db.domain.LoanIntDetailId;
import com.st1.itx.db.domain.LoanOverdue;
import com.st1.itx.db.domain.LoanOverdueId;
import com.st1.itx.db.domain.MlaundryRecord;
import com.st1.itx.db.service.AcReceivableService;
import com.st1.itx.db.service.FacMainService;
import com.st1.itx.db.service.FacProdService;
import com.st1.itx.db.service.LoanBookService;
import com.st1.itx.db.service.LoanBorMainService;
import com.st1.itx.db.service.LoanBorTxService;
import com.st1.itx.db.service.LoanChequeService;
import com.st1.itx.db.service.LoanIntDetailService;
import com.st1.itx.db.service.LoanOverdueService;
import com.st1.itx.db.service.MlaundryRecordService;
import com.st1.itx.db.service.TxTempService;
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.common.AcDetailCom;
import com.st1.itx.util.common.AcPaymentCom;
import com.st1.itx.util.common.AcReceivableCom;
import com.st1.itx.util.common.BaTxCom;
import com.st1.itx.util.common.LoanCalcRepayIntCom;
import com.st1.itx.util.common.LoanCom;
import com.st1.itx.util.common.LoanDueAmtCom;
import com.st1.itx.util.common.LoanSetRepayIntCom;
import com.st1.itx.util.common.PfDetailCom;
import com.st1.itx.util.common.SendRsp;
import com.st1.itx.util.common.data.BaTxVo;
import com.st1.itx.util.common.data.CalcRepayIntVo;
import com.st1.itx.util.common.data.PfDetailVo;
import com.st1.itx.util.data.DataLog;
import com.st1.itx.util.date.DateUtil;
import com.st1.itx.util.parse.Parse;

/*
 * L3200 回收登錄
 * a.使用時機:包括期款回收,部分償還及預繳時,適用於臨櫃交易或匯款轉帳,銀行扣款,扣薪及支票兌現先入暫收款時,由人手補做交易,由暫收款轉出.
 * b.回收輸入方式,可為:依撥款序號回收,依額度集體回收及依戶號集體回收,後兩者方式,回收金額需大於帳上金額,否則需人手依撥款序號逐筆輸入.(即集體回收時,不可有欠繳(短繳)金額)
 * c.單筆撥款回收時,如為本金平均戶或本息平均戶,可欠繳本金(依應繳本金百分比而定),不可欠繳利息及違約金,如按月繳息者,方可欠繳利息(依應繳利息百分比而定).
 * d.部分償還時,可選擇:縮短期數或減少每期攤還額,可欠繳本次期款本金及部分還本息.
 * e.可先執行回收試算查詢,以查詢應繳交之金額.
 * f.現金金額, 電匯金額及支票金額,應擇一輸入.
 * g.回收金額需＜轉催收餘額(當沖轉催收款項時，將沖轉金額輸入在[回收金額]欄位。)
 * h.※期款時收回順序：1.費用。2.欠繳金額。3.應繳日先到先還。5.足夠回收金額最大者優先收回。
 * i.※客戶部分償還時收回順序：1.費用2.欠繳金額3.本金是從利率較高的貸款先還，若金額不足當期期金時，帳務改掛[暫收及待結轉帳項]科目。4.資金用途(週轉金優先收回)5.案件編號(新件優先收回)
 */

/*
 * Tita
 * TimCustNo=9,7
 * CustId=X,10
 * ApplNo=9,7
 * FacmNo=9,3
 * BormNo=9,3
 * RepayType=9,2 還款類別 1.期款 2.部分償還 4.帳管費 5.火險費 6.契變手續費 7.法務費 9.其他
 * TimRepayAmt=9,14.2
 * TimCloseBreachAmt=9,14.2
 * TimExtraRepay=9,14.2
 * IncludeIntFlag=X,1 是否內含利息 Y:是 N:否
 * UnpaidIntFlag=X,1 利息是否可欠繳 Y:是 N:否
 * RepayTerms=9,2
 * EntryDate=9,7
 * TimAcctFee=9,14.2
 * TimModifyFee=9,14.2
 * TimFireFee=9,14.2
 * TimLawFee=9,14.2
 * PayMethod=9,1 1:減少每期攤還金額 2:縮短應繳期數
 * TimReduceAmt=9,14.2
 * TotalRepayAmt=9,14.2 應收付總金額
 * RealRepayAmt=9,14.2 實際收付金額
 * RqspFlag=X,1
 * ShortPrinRate=9,3
 * ShortIntRate=9,3
 */

/**
 * L3200 回收登錄
 * 
 * @author iza
 * @version 1.0.0
 */
@Service("L3200")
@Scope("prototype")
public class L3200 extends TradeBuffer {

	/* DB服務注入 */
	@Autowired
	public FacProdService facProdService;
	@Autowired
	public FacMainService facMainService;
	@Autowired
	public LoanBorMainService loanBorMainService;
	@Autowired
	public LoanBorTxService loanBorTxService;
	@Autowired
	public LoanIntDetailService loanIntDetailService;
	@Autowired
	public AcReceivableService acReceivableService;
	@Autowired
	public LoanChequeService loanChequeService;
	@Autowired
	public LoanBookService loanBookService;
	@Autowired
	public MlaundryRecordService mlaundryRecordService;
	@Autowired
	public LoanOverdueService loanOverdueService;
	@Autowired
	public TxTempService txTempService;

	@Autowired
	Parse parse;
	@Autowired
	DataLog datalog;
	@Autowired
	BaTxCom baTxCom;
	@Autowired
	DateUtil dDateUtil;
	@Autowired
	LoanCom loanCom;
	@Autowired
	SendRsp sendRsp;
	@Autowired
	LoanDueAmtCom loanDueAmtCom;
	@Autowired
	LoanSetRepayIntCom loanSetRepayIntCom;
	@Autowired
	LoanCalcRepayIntCom loanCalcRepayIntCom;
	@Autowired
	AcDetailCom acDetailCom;
	@Autowired
	AcPaymentCom acPaymentCom;
	@Autowired
	AcReceivableCom acReceivableCom;
	@Autowired
	public PfDetailCom pfDetailCom;

	private TitaVo titaVo;
	private int iCustNo;
	private int iFacmNo;
	private int iBormNo;
	private int iRepayType;
	private int iRepayTerms;
	private int iEntryDate;
	private int iRepayIntDate;
	private TempVo iRepayIntDateByFacmNoVo = new TempVo();
	private int iPayMethod;
	private int iOverRpFg; // 1.短收 2.溢收 3->溢收(整批入帳、部分繳款)
	private int iRpCode; // 還款來源
	private String iDscptCode; // 摘要代碼
	private String iRqspFlag;
	private BigDecimal iTxAmt;
	private BigDecimal iRepayAmt;
	private BigDecimal iCloseBreachAmt;
	private BigDecimal iExtraRepay;
	private BigDecimal iReduceAmt;
	private BigDecimal iTotalRepayAmt;
	private BigDecimal iRealRepayAmt;
	private BigDecimal iOvduRepay;
	private BigDecimal iPrincipal;
	private BigDecimal iInterest;
	private BigDecimal iDelayInt;
	private BigDecimal iBreachAmt;
	private BigDecimal iAcctFee;
	private BigDecimal iModifyFee;
	private BigDecimal iFireFee;
	private BigDecimal iLawFee;
	private BigDecimal iShortfallPrin;
	private BigDecimal iShortfallInt;
	private BigDecimal iShortCloseBreach;
	private BigDecimal iOverAmt; // 溢收金額
	private BigDecimal iShortAmt; // 短繳金額
	private BigDecimal iRepayLoan; // 償還本利
	private int iOverRpFacmNo; // 溢收額度
	private BigDecimal iTmpAmt = BigDecimal.ZERO; // 暫收抵繳金額
	private String iExtraRepayFlag;
	private String iUnpaidIntFlag;
	private String iPayFeeFlag; // 是否回收費用
	private FacProd tFacProd;
	private FacMain tFacMain;
	private LoanBorMain tLoanBorMain;
	private LoanBorTx tLoanBorTx;
	private LoanBorTxId tLoanBorTxId;
	private LoanIntDetail tLoanIntDetail;
	private LoanIntDetailId tLoanIntDetailId;
	private LoanOverdue tLoanOverdue;

	// work area
	private int wkCustNo;
	private int wkFacmNo;
	private int wkBormNo;
	private int wkBorxNo;
	private int wkNewBorxNo;
	private int wkOvduNo;
	private int wkTbsDy;
	private int wkIntStartDate = 9991231;
	private int wkIntEndDate = 0;
	private int wkRepaykindCode = 0; // 1:部分償還本金 2:回收期數>0 3:回收期數=0 4:清償違約金 5:催收款
	private int wkRepaidPeriod = 0;
	private int wkPaidTerms = 0;
	private int wkDueDate = 0;
	//
	private int wkFacmNoStart = 1;
	private int wkFacmNoEnd = 999;
	private int wkBormNoStart = 1;
	private int wkBormNoEnd = 900;
	//
	private int wkTerms = 0;
	private int wkTotaCount = 0;
	private int wkPreRepayTermNo = 0;
	private int wkTermNo = 0;
	private int wkPreRepayDate = 0;
	private int wkPrevTermNo = 0;
	private int wkNewTotalPeriod = 0;
	private BigDecimal wkLoanBal = BigDecimal.ZERO;
	private BigDecimal wkTotalExtraRepay = BigDecimal.ZERO;
	private BigDecimal wkTotalPrincipal = BigDecimal.ZERO;
	private BigDecimal wkTotalInterest = BigDecimal.ZERO;
	private BigDecimal wkTotalDelayInt = BigDecimal.ZERO;
	private BigDecimal wkTotalBreachAmt = BigDecimal.ZERO;
	private BigDecimal wkPrincipal = BigDecimal.ZERO;
	private BigDecimal wkInterest = BigDecimal.ZERO;
	private BigDecimal wkDelayInt = BigDecimal.ZERO;
	private BigDecimal wkBreachAmt = BigDecimal.ZERO;
	private BigDecimal wkCloseBreachAmt = BigDecimal.ZERO;
	private BigDecimal wkExtraRepay = BigDecimal.ZERO;
	private BigDecimal wkExtraRepayRemaind = BigDecimal.ZERO;
	private BigDecimal wkReduceAmt = BigDecimal.ZERO;
	private BigDecimal wkReduceAmtRemaind = BigDecimal.ZERO;
	private BigDecimal wkReduceCloseBreachAmtRemaind = BigDecimal.ZERO;
	private BigDecimal wkReduceBreachAmtRemaind = BigDecimal.ZERO;
	private BigDecimal wkReduceDelayIntRemaind = BigDecimal.ZERO;
	private BigDecimal wkReduceInterestRemaind = BigDecimal.ZERO;
	private BigDecimal wkReduceBreachAmt = BigDecimal.ZERO; // 減免清償違約金+減免違約金+減免延滯息
	private BigDecimal wkUnpaidPrin = BigDecimal.ZERO; // 短繳本金
	private BigDecimal wkUnpaidInt = BigDecimal.ZERO; // 短繳利息
	private BigDecimal wkUnpaidCloseBreach = BigDecimal.ZERO; // 短繳清償違約金
	private BigDecimal wkUnpaidAmtRemaind = BigDecimal.ZERO;
	private BigDecimal wkTempAmt = BigDecimal.ZERO;
	private BigDecimal wkNewDueAmt = BigDecimal.ZERO;
	private BigDecimal wkOvduRepayRemaind = BigDecimal.ZERO;
	private BigDecimal wkOvduAmt = BigDecimal.ZERO;
	private BigDecimal wkShortfallPrincipal = BigDecimal.ZERO; // 累短收 - 本金
	private BigDecimal wkShortfallInterest = BigDecimal.ZERO; // 累短收-利息
	private BigDecimal wkShortCloseBreach = BigDecimal.ZERO; // 累短收 - 清償違約金
	private BigDecimal wkAcctFee = BigDecimal.ZERO;
	private BigDecimal wkModifyFee = BigDecimal.ZERO;
	private BigDecimal wkFireFee = BigDecimal.ZERO;
	private BigDecimal wkLawFee = BigDecimal.ZERO;
	private BigDecimal wkTxAmtRemaind = BigDecimal.ZERO; // 交易還款餘額
	private BigDecimal wkTotalRepay = BigDecimal.ZERO; // 總還款金額
	private BigDecimal wkTotalShortAmtLimit = BigDecimal.ZERO; // 總短繳限額

	private String checkMsg = "";
	private AcReceivable tAcReceivable = new AcReceivable();
	private AcDetail acDetail;
	private List<AcDetail> lAcDetail = new ArrayList<AcDetail>();
	private List<AcReceivable> lAcReceivable = new ArrayList<AcReceivable>();
	private List<LoanOverdue> lLoanOverdue = new ArrayList<LoanOverdue>();
	private List<LoanBorMain> lLoanBorMain;
	private List<LoanBorTx> lLoanBorTx;
	private ArrayList<CalcRepayIntVo> lCalcRepayIntVo;
	private ArrayList<BaTxVo> baTxList;
	private boolean isFirstBorm = true;
	private boolean isCalcRepayInt = false;
	private boolean isSettleUnpaid = false;
	private boolean isLoanClose = false; // 最後一期期款/部分償還結案
	private boolean isRepayPrincipal = false; // 回收本金
	private TempVo tTempVo = new TempVo();

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active L3200 ");
		this.info("   isActfgEntry   = " + titaVo.isActfgEntry());
		this.info("   isActfgRelease = " + titaVo.isActfgRelease());
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
		this.wkTbsDy = this.txBuffer.getTxCom().getTbsdy();
		loanCom.setTxBuffer(this.txBuffer);
		loanSetRepayIntCom.setTxBuffer(this.txBuffer);
		baTxCom.setTxBuffer(this.txBuffer);

		// 取得輸入資料
		iCustNo = this.parse.stringToInteger(titaVo.getParam("TimCustNo"));
		iFacmNo = this.parse.stringToInteger(titaVo.getParam("FacmNo"));
		iBormNo = this.parse.stringToInteger(titaVo.getParam("BormNo"));
		iRepayType = this.parse.stringToInteger(titaVo.getParam("RepayType"));
		iRepayTerms = this.parse.stringToInteger(titaVo.getParam("RepayTerms"));
		// 入帳日
		iEntryDate = this.parse.stringToInteger(titaVo.getParam("EntryDate"));
		iTxAmt = parse.stringToBigDecimal(titaVo.getTxAmt());
		iRepayAmt = this.parse.stringToBigDecimal(titaVo.getParam("TimRepayAmt"));
		iExtraRepay = this.parse.stringToBigDecimal(titaVo.getParam("TimExtraRepay"));
		iExtraRepayFlag = titaVo.getParam("IncludeIntFlag");
		iUnpaidIntFlag = titaVo.getParam("UnpaidIntFlag");
		iPayFeeFlag = titaVo.getParam("PayFeeFlag"); // 部分償還是否內含費用
		if (iExtraRepay.compareTo(BigDecimal.ZERO) > 0) {
			iCloseBreachAmt = this.parse.stringToBigDecimal(titaVo.getParam("TimExtraCloseBreachAmt"));
		} else {
			iCloseBreachAmt = this.parse.stringToBigDecimal(titaVo.getParam("TimCloseBreachAmt"));
		}
		iOvduRepay = this.parse.stringToBigDecimal(titaVo.getParam("TimOvduRepay")); // 催收收回金額
		// 收取本金、利息、費用、短繳金額
		iPrincipal = this.parse.stringToBigDecimal(titaVo.getParam("TimPrincipal"));
		iInterest = this.parse.stringToBigDecimal(titaVo.getParam("TimInterest"));
		iDelayInt = this.parse.stringToBigDecimal(titaVo.getParam("TimDelayInt"));
		iBreachAmt = this.parse.stringToBigDecimal(titaVo.getParam("TimBreachAmt"));
		iAcctFee = this.parse.stringToBigDecimal(titaVo.getParam("TimAcctFee"));
		iModifyFee = this.parse.stringToBigDecimal(titaVo.getParam("TimModifyFee"));
		iFireFee = this.parse.stringToBigDecimal(titaVo.getParam("TimFireFee"));
		iLawFee = this.parse.stringToBigDecimal(titaVo.getParam("TimLawFee"));
		iShortfallPrin = this.parse.stringToBigDecimal(titaVo.getParam("TimShortfallPrin"));
		iShortfallInt = this.parse.stringToBigDecimal(titaVo.getParam("TimShortfallInt"));
		iShortCloseBreach = this.parse.stringToBigDecimal(titaVo.getParam("TimShortCloseBreach"));
		//
		iPayMethod = this.parse.stringToInteger(titaVo.getParam("PayMethod"));
		iReduceAmt = this.parse.stringToBigDecimal(titaVo.getParam("TimReduceAmt"));
		iTotalRepayAmt = this.parse.stringToBigDecimal(titaVo.getParam("TotalRepayAmt"));
		iRealRepayAmt = this.parse.stringToBigDecimal(titaVo.getParam("RealRepayAmt"));
		iRqspFlag = titaVo.getParam("RqspFlag");
		iOverRpFg = this.parse.stringToInteger(titaVo.getParam("OverRpFg")); // 1->短收 2->溢收 3->溢收(整批入帳、部分繳款)
		if (iOverRpFg == 1) {
			iShortAmt = this.parse.stringToBigDecimal(titaVo.getParam("OverRpAmt"));
			iOverAmt = BigDecimal.ZERO;
			iOverRpFacmNo = 0;
		} else {
			iShortAmt = BigDecimal.ZERO;
			iOverAmt = this.parse.stringToBigDecimal(titaVo.getParam("OverRpAmt"));
			iOverRpFacmNo = this.parse.stringToInteger(titaVo.getParam("OverRpFacmNo"));
		}
		iRpCode = this.parse.stringToInteger(titaVo.getParam("RpCode1"));
		iDscptCode = this.titaVo.getParam("RpDscpt1"); // 摘要代碼

		// 收付欄金額
		for (int i = 1; i <= 50; i++) {
			if (titaVo.get("RpCode" + i) == null || parse.stringToInteger(titaVo.getParam("RpCode" + i)) == 0)
				break;
			if (parse.stringToInteger(titaVo.getParam("RpCode" + i)) != 90) {
				wkTxAmtRemaind = wkTxAmtRemaind.add(parse.stringToBigDecimal(titaVo.getParam("RpAmt" + i))); // 交易還款餘額
			}
			if (parse.stringToInteger(titaVo.getParam("RpCode" + i)) == 90) {
				iTmpAmt = iTmpAmt.subtract(parse.stringToBigDecimal(titaVo.getParam("RpAmt" + i))); // 暫收抵繳金額
			}
		}
		this.info("iPrincipal=" + iPrincipal + ",iInterest=" + iInterest);
		this.info("iDelayInt=" + iDelayInt + ",iBreachAmt=" + iBreachAmt);
		this.info("iAcctFee=" + iAcctFee + ",iModifyFee1=" + iModifyFee);
		this.info("iFireFee=" + iFireFee + ",iLawFee1=" + iLawFee);
		this.info("iShortfallPrin=" + iShortfallPrin + ",iShortfallInt=" + iShortfallInt + ",iShortCloseBreach="
				+ iShortCloseBreach);
		this.info("iRepayAmt=" + iRepayAmt + ",OverAmt=" + iOverAmt + ",iShortAmt=" + iShortAmt + ",iOverRpFacmNo="
				+ iOverRpFacmNo);
		// 應繳日
		if (titaVo.isTrmtypBatch() && titaVo.get("RepayIntDate") != null) {
			iRepayIntDate = parse.stringToInteger(titaVo.getParam("RepayIntDate"));
			iRepayIntDateByFacmNoVo = iRepayIntDateByFacmNoVo.getVo(titaVo.getParam("RepayIntDateByFacmNoVo"));
		} else {
			iRepayIntDate = 0;
		}
		// 償還本利
		if (titaVo.isTrmtypBatch() && titaVo.get("RepayLoan") != null) {
			iRepayLoan = parse.stringToBigDecimal(titaVo.getParam("RepayLoan"));
		} else {
			iRepayLoan = BigDecimal.ZERO;
		}
		this.info("iRepayIntDate=" + iRepayIntDate + "," + iRepayIntDateByFacmNoVo.toString() + ", iRepayLoan="
				+ iRepayLoan + ", iExtraRepay=" + iExtraRepay);

		this.info("TotalRepayAmt = " + iTotalRepayAmt + ",RealRepayAmt=" + iRealRepayAmt);

		// Check Input
		checkInputRoutine();

		// 按清償違約金、違約金、 延滯息、利息順序減免，計算各自減免金額
		reduceAmtRoutine();

		wkOvduRepayRemaind = iOvduRepay;
		wkUnpaidAmtRemaind = iShortAmt;

		if (iFacmNo > 0) {
			wkFacmNoStart = iFacmNo;
			wkFacmNoEnd = iFacmNo;
		}

		if (iBormNo > 0) {
			wkBormNoStart = iBormNo;
			wkBormNoEnd = iBormNo;
		}

		// 帳務處理
		if (this.txBuffer.getTxCom().isBookAcYes()) {
			// 借方：收付欄
			acPaymentCom.setTxBuffer(this.getTxBuffer());
			acPaymentCom.run(titaVo);

			// 貸方：費用、短繳期金
			if (iRepayType <= 2) { // 還款類別 1.期款 2.部分償還
				batxSettleUnpaid();
			}
		}

		// 依還款類別處理
		switch (iRepayType) {
		case 1: // 1.期款
		case 2: // 2.部分償還
			if (titaVo.isHcodeNormal()) {
				calcRepayNormalRoutine();
			} else {
				calcRepayEraseRoutine();
			}
			// 兌現票入帳處理
			loanChequeRoutine();
			break;
		// case 4: // 4.帳管費
		// case 5: // 5.火險費
		// case 6: // 6.契變手續費
		// case 7: // 7.法務費

		case 9: // 9. 清償違約金
			if (titaVo.isHcodeNormal()) {
				calcCloseBreachNormalRoutine();
			} else {
				calcCloseBreachEraseRoutine();
			}
			break;
		case 12: // 12.催收收回
			if (titaVo.isHcodeNormal()) {
				calcOvduRepayNormalRoutine();
			} else {
				calcOvduRepayEraseRoutine();
			}
			break;
		}

		// 帳務處理
		if (this.txBuffer.getTxCom().isBookAcYes()) {
			// 貸方： 本金利息
			this.txBuffer.addAllAcDetailList(lAcDetail);

			// 產生會計分錄
			acDetailCom.setTxBuffer(this.txBuffer);
			acDetailCom.run(titaVo);
		}

		// 銷帳檔處理(短繳)
		if (lAcReceivable.size() > 0) {
			acReceivableCom.setTxBuffer(this.getTxBuffer());
			if (iRepayType == 9) { // 9. 清償違約金
				acReceivableCom.mnt(1, lAcReceivable, titaVo); // 1-銷帳
			} else {
				acReceivableCom.mnt(0, lAcReceivable, titaVo); // 0-起帳

			}
		}

		// 約定還本檔處理
		if (iRepayType == 2) {
			loanBookRoutine();
		}

		// 疑似洗錢交易訪談記錄檔處理
		if (iRepayType == 2) {
			mlaundryRecordRoutine();
		}

		this.addList(this.totaVo);
		return this.sendList();
	}

	private void checkInputRoutine() throws LogicException {
		// 還款類別 >= 3 不可有短繳金額
		if (iRepayType >= 3 && iShortAmt.compareTo(BigDecimal.ZERO) > 0) {
			throw new LogicException(titaVo, "E3094", "短繳金額 = " + iShortAmt); // 不可有短繳金額
		}

		// 部分償還本金，
		if (iExtraRepay.compareTo(BigDecimal.ZERO) > 0 && iShortAmt.compareTo(BigDecimal.ZERO) > 0) {
			// 利息是否可欠繳
			if (iUnpaidIntFlag.equals("N")) {
				throw new LogicException(titaVo, "E3094", "利息是否可欠繳 = N ");
			}
			if (iShortAmt.compareTo(iInterest.add(iShortfallInt)) > 0) {
				throw new LogicException(titaVo, "E3094", "短繳金額大於利息 " + iInterest.add(iShortfallInt));
			}
		}

		if (titaVo.isHcodeNormal()) {
			// 減免金額超過限額，需主管核可
			if (iRqspFlag.equals("Y")) {
				if (!titaVo.getHsupCode().equals("1")) {
					sendRsp.addvReason(this.txBuffer, titaVo, "0007", "");
				}
			}
		}
	}

	// 按清償違約金、違約金、 延滯息、利息順序減免，計算各自減免金額
	private void reduceAmtRoutine() throws LogicException {
		if (iReduceAmt.compareTo(BigDecimal.ZERO) > 0) {
			wkReduceAmtRemaind = iReduceAmt;
			if (wkReduceAmtRemaind.compareTo(BigDecimal.ZERO) > 0) {
				if (wkReduceAmtRemaind.compareTo(iCloseBreachAmt) >= 0) {
					wkReduceAmtRemaind = wkReduceAmtRemaind.subtract(iCloseBreachAmt);
					wkReduceCloseBreachAmtRemaind = iCloseBreachAmt;
				} else {
					wkReduceCloseBreachAmtRemaind = wkReduceAmtRemaind;
					wkReduceAmtRemaind = BigDecimal.ZERO;
				}
			}
			if (wkReduceAmtRemaind.compareTo(BigDecimal.ZERO) > 0) {
				if (wkReduceAmtRemaind.compareTo(iBreachAmt) >= 0) {
					wkReduceAmtRemaind = wkReduceAmtRemaind.subtract(iBreachAmt);
					wkReduceBreachAmtRemaind = iBreachAmt;
				} else {
					wkReduceBreachAmtRemaind = wkReduceAmtRemaind;
					wkReduceAmtRemaind = BigDecimal.ZERO;
				}
			}
			if (wkReduceAmtRemaind.compareTo(BigDecimal.ZERO) > 0) {
				if (wkReduceAmtRemaind.compareTo(iDelayInt) >= 0) {
					wkReduceAmtRemaind = wkReduceAmtRemaind.subtract(iDelayInt);
					wkReduceDelayIntRemaind = iDelayInt;
				} else {
					wkReduceDelayIntRemaind = wkReduceAmtRemaind;
					wkReduceAmtRemaind = BigDecimal.ZERO;
				}
			}
			if (wkReduceAmtRemaind.compareTo(BigDecimal.ZERO) > 0) {
				if (wkReduceAmtRemaind.compareTo(iInterest) >= 0) {
					wkReduceAmtRemaind = wkReduceAmtRemaind.subtract(iInterest);
					wkReduceInterestRemaind = iInterest;
				} else {
					wkReduceInterestRemaind = wkReduceAmtRemaind;
					wkReduceAmtRemaind = BigDecimal.ZERO;
				}
			}
		}
		this.info("iReduceAmt " + iReduceAmt + "," + wkReduceCloseBreachAmtRemaind + "," + wkReduceBreachAmtRemaind
				+ "," + wkReduceDelayIntRemaind + "," + wkReduceInterestRemaind);
	}

	private void calcRepayNormalRoutine() throws LogicException {
		this.info("calcRepayNormalRoutine ...");
		// 部分償還本金，限額含短收-利息
		if (iExtraRepay.compareTo(BigDecimal.ZERO) > 0) {
			wkRepaykindCode = 1;
			wkTotalShortAmtLimit = wkTotalShortAmtLimit.add(iShortfallInt);
		}
		// 期款
		else {
			if (iRepayTerms > 0) { // 回收期數 > 0
				wkRepaykindCode = 2;
			} else {
				wkRepaykindCode = 3;
			}
		}

		Slice<LoanBorMain> slLoanBorMain = loanBorMainService.bormCustNoEq(iCustNo, wkFacmNoStart, wkFacmNoEnd,
				wkBormNoStart, wkBormNoEnd, 0, Integer.MAX_VALUE, titaVo);
		lLoanBorMain = slLoanBorMain == null ? null : new ArrayList<LoanBorMain>(slLoanBorMain.getContent());
		if (lLoanBorMain == null || lLoanBorMain.size() == 0) {
			throw new LogicException(titaVo, "E0001", "放款主檔"); // 查詢資料不存在
		}
		Collections.sort(lLoanBorMain, new Comparator<LoanBorMain>() {
			public int compare(LoanBorMain c1, LoanBorMain c2) {
				// status
				if (c1.getStatus() != c2.getStatus()) {
					return c1.getStatus() - c2.getStatus();
				}
				// 回收金額 > 0時排序,依應繳日順序由小到大、利率順序由大到小、額度由小到大
				if (iRepayType == 1) {
					if (c1.getNextPayIntDate() != c2.getNextPayIntDate()) {
						return c1.getNextPayIntDate() - c2.getNextPayIntDate();
					}
					if (c1.getStoreRate().compareTo(c2.getStoreRate()) != 0) {
						return (c1.getStoreRate().compareTo(c2.getStoreRate()) > 0 ? -1 : 1);
					}
				}
				// 部分償還金額 > 0時排序
//					利率高至低>用途別>由額度編號大至小
//					用途別為9->1->3->4->5->6->2
//					欄位代碼       欄位說明     
//					1            週轉金    
//					2            購置不動產
//					3            營業用資產
//					4            固定資產  
//					5            企業投資  
//					6            購置動產
//					9            其他					
				if (iRepayType == 2) {
					if (c1.getStoreRate().compareTo(c2.getStoreRate()) != 0) {
						return (c1.getStoreRate().compareTo(c2.getStoreRate()) > 0 ? -1 : 1);
					}
					// 若用途別不同
					if (!c1.getUsageCode().equals(c2.getUsageCode())) {
						int c1UsageCode = Integer.parseInt(c1.getUsageCode());
						int c2UsageCode = Integer.parseInt(c2.getUsageCode());

						// C1優先的特殊情況
						if (c1UsageCode == 9 || c2UsageCode == 2) {
							return -1;
						}
						// C2優先的特殊情況
						if (c1UsageCode == 2 || c2UsageCode == 9) {
							return 1;
						}
						// 一般情況
						return c1UsageCode - c2UsageCode;
					}
				}
				if (c1.getFacmNo() != c2.getFacmNo()) {
					return c2.getFacmNo() - c1.getFacmNo();
				}
				if (c1.getBormNo() != c2.getBormNo()) {
					return c2.getBormNo() - c1.getBormNo();
				}
				return 0;
			}
		});

		// isFirstBorm default true;
		for (

		LoanBorMain ln : lLoanBorMain) {
			if (!(ln.getStatus() == 0)) {
				continue;
			}
			this.info("order1 = " + ln.getLoanBorMainId());
			wkCustNo = ln.getCustNo();
			wkFacmNo = ln.getFacmNo();
			wkBormNo = ln.getBormNo();
			wkBorxNo = ln.getLastBorxNo() + 1;

			// 計算利息
			calcRepayInt(ln);

			// 清償違約金(部分償還)放首筆
			if (isFirstBorm) {
				wkCloseBreachAmt = iCloseBreachAmt;
			} else {
				wkCloseBreachAmt = BigDecimal.ZERO;
			}

			// 計算減免
			calcReduceAmt(ln);

			wkTotalPrincipal = wkTotalPrincipal.add(wkPrincipal);
			wkTotalInterest = wkTotalInterest.add(wkInterest);
			wkTotalDelayInt = wkTotalDelayInt.add(wkDelayInt);
			wkTotalBreachAmt = wkTotalBreachAmt.add(wkBreachAmt);
			wkTotalExtraRepay = wkTotalExtraRepay.add(wkExtraRepay);

			// 計算短繳
			calcUnpaidAmt(ln);

			// 費用、短繳期金
			getSettleUnpaid();

			// 無(計息)、無(費用、短繳期金)
			if (!isCalcRepayInt && !isSettleUnpaid) {
				continue;
			}

			wkTotaCount++;

			// 實收處理
			// Principal 實收本金 => 扣除短收本金、含收回欠繳本金
			// 期款最後一期本金含欠繳本金
			if (isLoanClose) {
				wkPrincipal = wkPrincipal.subtract(wkUnpaidPrin);
			} else {
				wkPrincipal = wkPrincipal.subtract(wkUnpaidPrin).add(wkShortfallPrincipal);
			}
			this.info("本金 =" + wkPrincipal);

			// Interest 實收利息(已扣除減免) => 扣除短收利息、含收回欠繳利息
			wkInterest = wkInterest.subtract(wkUnpaidInt).add(wkShortfallInterest);

			// CloseBreachAmt 清償違約金(部分償還)
			// 清償違約金全額列短繳
			wkUnpaidCloseBreach = wkCloseBreachAmt;
			// 實收清償違約金= 收回欠繳清償違約金
			wkCloseBreachAmt = wkShortCloseBreach;

			// 更新額度檔
			updFacMainRoutine();

			// 鎖定撥款主檔
			tLoanBorMain = loanBorMainService.holdById(new LoanBorMainId(iCustNo, wkFacmNo, wkBormNo), titaVo);
			if (tLoanBorMain == null || tLoanBorMain.getPrevPayIntDate() != ln.getPrevPayIntDate()) {
				throw new LogicException(titaVo, "E0006",
						"撥款主檔 戶號 = " + iCustNo + " 額度編號 = " + wkFacmNo + " 撥款序號 = " + wkBormNo); // 鎖定資料時，發生錯誤
			}
			if (tLoanBorMain.getActFg() == 1) {
				throw new LogicException(titaVo, "E0021", "放款主檔 戶號 = " + tLoanBorMain.getCustNo() + " 額度編號 =  "
						+ tLoanBorMain.getFacmNo() + " 撥款序號 = " + tLoanBorMain.getBormNo()); // 該筆資料待放行中
			}
			// initialize tTempVo
			tTempVo.clear();

			// 新增交易暫存檔(放款資料)
			addRepayTxTempBormRoutine();

			// 更新撥款主檔
			updLoanBorMainRoutine();

			// 新增計息明細
			if (isCalcRepayInt) {
				addLoanIntDetailRoutine();
			}

			// 本次期款、部分償還的短繳金額處理
			if (isCalcRepayInt) {
				unpaidAmtRoutine();
			}

			// 貸方回收金額帳務處理
			acDetailCrRoutine();

			// 計算本筆暫收金額
			compTempAmt();

			// 新增放款交易內容檔
			addRepayBorTxRoutine();

			// 業績處理
			PfDetailRoutine();

			// FirstBorm、LastFacmNo
			isFirstBorm = false;

		}

		if (wkTotaCount == 0) {
			throw new LogicException(titaVo, "E3070", checkMsg); // 查無可計息的放款資料
		}

		if (iShortAmt.compareTo(wkTotalShortAmtLimit) > 0) {
			throw new LogicException(titaVo, "E3096", "短繳金額 = " + iShortAmt + " 短繳限額 = " + wkTotalShortAmtLimit); // 短繳利息超過規定百分比金額
		}

	}

	// 計算短繳
	private void calcUnpaidAmt(LoanBorMain ln) throws LogicException {
		wkUnpaidPrin = BigDecimal.ZERO;
		wkUnpaidInt = BigDecimal.ZERO;
		BigDecimal wkShortAmtLimit = BigDecimal.ZERO;
		if (iShortAmt.compareTo(BigDecimal.ZERO) > 0 && !isLoanClose) {
			if (wkRepaykindCode == 1) { // 部分償還本金欠繳利息
				wkTotalShortAmtLimit = wkTotalShortAmtLimit.add(wkInterest); // 利息全額
				this.info("wkTotalShortAmtLimit= " + wkTotalShortAmtLimit + "," + wkInterest);
				if (wkInterest.compareTo(wkUnpaidAmtRemaind) >= 0) {
					wkUnpaidInt = wkUnpaidInt.add(wkUnpaidAmtRemaind);
					wkUnpaidAmtRemaind = BigDecimal.ZERO;
				} else {
					wkUnpaidInt = wkUnpaidInt.add(wkInterest);
					wkUnpaidAmtRemaind = wkUnpaidAmtRemaind.subtract(wkInterest);
				}
			} else {
				// 到期取息(到期繳息還本)
				if ("2".equals(ln.getAmortizedCode())) {
					throw new LogicException(titaVo, "E3094", "到期繳息還本， 不可有短繳金額");

				}
				// 依 1.還本金額 2.還息金額
				if (isRepayPrincipal) {
					wkShortAmtLimit = wkPrincipal
							.multiply(new BigDecimal(this.txBuffer.getSystemParas().getShortPrinPercent()))
							.divide(new BigDecimal(100)).setScale(0, RoundingMode.HALF_UP);
					wkTotalShortAmtLimit = wkTotalShortAmtLimit.add(wkShortAmtLimit);
					if (wkShortAmtLimit.compareTo(wkUnpaidAmtRemaind) >= 0) {
						wkUnpaidPrin = wkUnpaidPrin.add(wkUnpaidAmtRemaind);
						wkUnpaidAmtRemaind = BigDecimal.ZERO;
					} else {
						wkUnpaidPrin = wkUnpaidPrin.add(wkShortAmtLimit);
						wkUnpaidAmtRemaind = wkUnpaidAmtRemaind.subtract(wkShortAmtLimit);
					}
				} else {
					wkShortAmtLimit = wkInterest
							.multiply(new BigDecimal(this.txBuffer.getSystemParas().getShortIntPercent()))
							.divide(new BigDecimal(100)).setScale(0, RoundingMode.HALF_UP);
					wkTotalShortAmtLimit = wkTotalShortAmtLimit.add(wkShortAmtLimit);
					if (wkShortAmtLimit.compareTo(wkUnpaidAmtRemaind) >= 0) {
						wkUnpaidInt = wkUnpaidInt.add(wkUnpaidAmtRemaind);
						wkUnpaidAmtRemaind = BigDecimal.ZERO;
					} else {
						wkUnpaidInt = wkUnpaidInt.add(wkShortAmtLimit);
						wkUnpaidAmtRemaind = wkUnpaidAmtRemaind.subtract(wkShortAmtLimit);
					}
				}
			}
		}
	}

	// 計算減免
	private void calcReduceAmt(LoanBorMain ln) throws LogicException {
		// 減免前金額
		wkReduceAmt = wkCloseBreachAmt.add(wkBreachAmt).add(wkDelayInt).add(wkInterest); // 減免金額
		wkReduceBreachAmt = wkCloseBreachAmt.add(wkBreachAmt).add(wkDelayInt); // 減免清償違約金+減免違約金+減免延滯息

		if (iReduceAmt.compareTo(BigDecimal.ZERO) > 0) {
			if (wkReduceCloseBreachAmtRemaind.compareTo(BigDecimal.ZERO) > 0) {
				if (wkReduceCloseBreachAmtRemaind.compareTo(wkCloseBreachAmt) >= 0) {
					wkReduceCloseBreachAmtRemaind = wkReduceCloseBreachAmtRemaind.subtract(wkCloseBreachAmt);
					wkCloseBreachAmt = BigDecimal.ZERO;
				} else {
					wkCloseBreachAmt = wkCloseBreachAmt.subtract(wkReduceCloseBreachAmtRemaind);
					wkReduceAmtRemaind = BigDecimal.ZERO;
				}
			}
			if (wkReduceBreachAmtRemaind.compareTo(BigDecimal.ZERO) > 0) {
				if (wkReduceBreachAmtRemaind.compareTo(wkBreachAmt) >= 0) {
					wkReduceBreachAmtRemaind = wkReduceBreachAmtRemaind.subtract(wkBreachAmt);
					wkBreachAmt = BigDecimal.ZERO;
				} else {
					wkBreachAmt = wkBreachAmt.subtract(wkReduceBreachAmtRemaind);
					wkReduceBreachAmtRemaind = BigDecimal.ZERO;
				}
			}
			if (wkReduceDelayIntRemaind.compareTo(BigDecimal.ZERO) > 0) {
				if (wkReduceDelayIntRemaind.compareTo(wkDelayInt) >= 0) {
					wkReduceDelayIntRemaind = wkReduceDelayIntRemaind.subtract(wkDelayInt);
					wkDelayInt = BigDecimal.ZERO;
				} else {
					wkDelayInt = wkDelayInt.subtract(wkReduceDelayIntRemaind);
					wkReduceDelayIntRemaind = BigDecimal.ZERO;
				}
			}
			if (wkReduceInterestRemaind.compareTo(BigDecimal.ZERO) > 0) {
				if (wkReduceInterestRemaind.compareTo(wkInterest) >= 0) {
					wkReduceInterestRemaind = wkReduceInterestRemaind.subtract(wkInterest);
					wkInterest = BigDecimal.ZERO;
				} else {
					wkInterest = wkInterest.subtract(wkReduceInterestRemaind);
					wkReduceInterestRemaind = BigDecimal.ZERO;
				}
			}
		}
		// 本筆減免=減免前金額 - 減免後金額
		wkReduceAmt = wkReduceAmt.subtract(wkCloseBreachAmt).subtract(wkBreachAmt).subtract(wkDelayInt)
				.subtract(wkInterest); // 減免金額
		wkReduceBreachAmt = wkReduceBreachAmt.subtract(wkCloseBreachAmt).subtract(wkBreachAmt).subtract(wkDelayInt); // 減免清償違約金+減免違約金+減免延滯息
	}

	// 計算利息
	private void calcRepayInt(LoanBorMain ln) throws LogicException {
		checkMsg = " 戶號:" + iCustNo + "-" + ln.getFacmNo() + "-" + ln.getBormNo();
		isCalcRepayInt = false;
		isLoanClose = false; // 最後一期期款
		isRepayPrincipal = false; // 回收本金
		BigDecimal wkRepayLoan = wkTotalPrincipal.add(wkTotalInterest).add(wkTotalDelayInt).add(wkTotalBreachAmt);
		this.info("calcRepayInt ..." + checkMsg + " 累計償還本利:" + wkRepayLoan + ", 試算償還本利:" + iRepayLoan);

		// 部分償還餘額 > 0
		if (wkRepaykindCode == 1) {
			wkExtraRepayRemaind = iExtraRepay.subtract(wkRepayLoan);
			if (wkExtraRepayRemaind.compareTo(BigDecimal.ZERO) <= 0) {
				checkMsg += " 累計償還本利:" + wkRepayLoan + ", 超過部分償還金額:" + iExtraRepay;
				this.info(checkMsg);
				return;
			}
		}

		// 整批入帳繳款(依額度還款日期)
		int wkRepayIntDate = iEntryDate;
		if (iRepayIntDate > 0) {
			if (iRepayIntDateByFacmNoVo.get(parse.IntegerToString(ln.getFacmNo(), 3)) == null) {
				this.info(parse.IntegerToString(ln.getFacmNo(), 3) + "RepayIntDate = 0 return");
				return;
			}
			wkRepayIntDate = parse
					.stringToInteger(iRepayIntDateByFacmNoVo.get(parse.IntegerToString(ln.getFacmNo(), 3)));
		}

		wkIntStartDate = 9991231;
		wkIntEndDate = 0;
		wkLoanBal = ln.getLoanBal();
		wkDueDate = ln.getNextPayIntDate();
		wkPrevTermNo = 0;
		wkPrincipal = BigDecimal.ZERO;
		wkInterest = BigDecimal.ZERO;
		wkDelayInt = BigDecimal.ZERO;
		wkBreachAmt = BigDecimal.ZERO;
		wkExtraRepay = BigDecimal.ZERO;
		wkRepaidPeriod = 0;

		if (ln.getActFg() == 1) {
			throw new LogicException(titaVo, "E0021", checkMsg); // 該筆資料待放行中
		}
		// 計算至上次繳息日之期數
		if (ln.getPrevPayIntDate() > ln.getDrawdownDate()) {
			wkPrevTermNo = loanCom.getTermNo(2, ln.getFreqBase(), ln.getPayIntFreq(), ln.getSpecificDate(),
					ln.getSpecificDd(), ln.getPrevPayIntDate());
		}

		// 可回收期數
		wkPreRepayTermNo = loanCom.getTermNo(wkTbsDy >= ln.getMaturityDate() ? 1 : 2, ln.getFreqBase(),
				ln.getPayIntFreq(), ln.getSpecificDate(), ln.getSpecificDd(), wkTbsDy);

		// 可回收期數；可回收期數 = 已到期期數 + 預收期數
		if (titaVo.isTrmtypBatch()) {
			wkPreRepayTermNo = wkPreRepayTermNo + this.txBuffer.getSystemParas().getPreRepayTermsBatch();
		} else {
			wkPreRepayTermNo = wkPreRepayTermNo + this.txBuffer.getSystemParas().getPreRepayTerms();
		}

		// 可回收應繳日
		wkPreRepayDate = loanCom.getPayIntEndDate(ln.getFreqBase(), ln.getPayIntFreq(), ln.getSpecificDate(),
				ln.getSpecificDd(), wkPreRepayTermNo, ln.getMaturityDate());

		switch (wkRepaykindCode) {
		case 1: // 部分償還金額 > 0
			if (ln.getNextPayIntDate() <= iEntryDate) {
				throw new LogicException(titaVo, "E3072", checkMsg + " 應繳息日 = " + ln.getNextPayIntDate()); // 該筆放款尚有其款未回收
			}
			wkTerms = 0;
			break;
		case 2: // 回收期數 > 0
			wkTerms = iRepayTerms;
			if ((wkTerms + wkPrevTermNo) > wkPreRepayTermNo) {
				throw new LogicException(titaVo, "E3082",
						checkMsg + ",可預收迄日" + wkPreRepayDate + ",可回收期數= " + (wkPreRepayTermNo - wkPrevTermNo)); // 回收期數超過可預收期數
			}
			break;
		case 3: // 回收期數 = 0
			// 計算至入帳日或指定應繳日的應繳之期數
			if (wkRepayIntDate <= ln.getPrevPayIntDate() || wkRepayIntDate <= ln.getDrawdownDate()) {
				checkMsg += ", 應繳日:" + wkRepayIntDate + ", 上次繳息日=" + ln.getPrevPayIntDate();
				this.info(checkMsg);
				return;
			}
			wkTermNo = loanCom.getTermNo(wkRepayIntDate >= ln.getMaturityDate() ? 1 : 2, ln.getFreqBase(),
					ln.getPayIntFreq(), ln.getSpecificDate(), ln.getSpecificDd(), wkRepayIntDate);
			// 應繳計算止日
			wkIntEndDate = loanCom.getPayIntEndDate(ln.getFreqBase(), ln.getPayIntFreq(), ln.getSpecificDate(),
					ln.getSpecificDd(), wkTermNo, ln.getMaturityDate());

			// 計算至入帳日期應繳之期數 - 計算至上次繳息日之期數
			wkTerms = wkTermNo - wkPrevTermNo;

			// 應繳之期數不可大於可回收期數
			if (wkTerms <= 0) {
				checkMsg += ", 可預收迄日:" + wkPreRepayDate + ", 試算收息日:" + wkIntEndDate + ",可回收期數= " + wkTerms;
				this.info(checkMsg);
				return;
			}
			break;
		}

		// 回收計算設定值
		if (wkRepaykindCode == 1) { // 部分償還金額
			loanCalcRepayIntCom = loanSetRepayIntCom.setRepayInt(ln, 0, iEntryDate, 1, iEntryDate, titaVo);
			if (wkExtraRepayRemaind.compareTo(ln.getLoanBal()) >= 0 || iEntryDate >= ln.getMaturityDate()) {
				loanCalcRepayIntCom.setCaseCloseFlag("Y"); // 結案試算
			} else {
				loanCalcRepayIntCom.setExtraRepayFlag(iExtraRepayFlag);
				loanCalcRepayIntCom.setExtraRepay(wkExtraRepayRemaind);
			}
		} else {
			loanCalcRepayIntCom = loanSetRepayIntCom.setRepayInt(ln, wkTerms, 0, 0, iEntryDate, titaVo);
		}

		// 回收金額計算
		lCalcRepayIntVo = loanCalcRepayIntCom.getRepayInt(titaVo);

		// 計算金額
		wkPrincipal = loanCalcRepayIntCom.getPrincipal();
		wkInterest = loanCalcRepayIntCom.getInterest();
		wkDelayInt = loanCalcRepayIntCom.getDelayInt();
		wkBreachAmt = loanCalcRepayIntCom.getBreachAmt();
		wkExtraRepay = loanCalcRepayIntCom.getExtraAmt();
		wkRepaidPeriod = loanCalcRepayIntCom.getRepaidPeriod();

		// 最後一期期款/部分償還結案
		if (loanCalcRepayIntCom.getLoanBal().compareTo(BigDecimal.ZERO) == 0) {
			isLoanClose = true;
		}

		// 回收本金
		if (wkPrincipal.compareTo(BigDecimal.ZERO) > 0) {
			isRepayPrincipal = true;
		}

		// 有計息
		isCalcRepayInt = true;

	}

	// 訂正
	private void calcRepayEraseRoutine() throws LogicException {
		this.info("calcRepayEraseRoutine ...");

		Slice<LoanBorTx> slLoanBortx = loanBorTxService.custNoTxtNoEq(iCustNo, titaVo.getOrgEntdyI() + 19110000,
				titaVo.getOrgKin(), titaVo.getOrgTlr(), titaVo.getOrgTno(), 0, Integer.MAX_VALUE, titaVo);
		lLoanBorTx = slLoanBortx == null ? null : slLoanBortx.getContent();
		if (lLoanBorTx == null || lLoanBorTx.size() == 0) {
			throw new LogicException(titaVo, "E0001", "交易暫存檔 分行別 = " + titaVo.getOrgKin() + " 交易員代號 = "
					+ titaVo.getOrgTlr() + " 交易序號 = " + titaVo.getOrgTno()); // 查詢資料不存在
		}
		for (LoanBorTx tx : lLoanBorTx) {
			wkCustNo = tx.getCustNo();
			wkFacmNo = tx.getFacmNo();
			wkBormNo = tx.getBormNo();
			wkBorxNo = tx.getBorxNo();
			tTempVo = tTempVo.getVo(tx.getOtherFields());
			// 還原金額處理
			wkOvduNo = parse.stringToInteger(tTempVo.get("OvduNo"));
			wkPrincipal = tx.getPrincipal();
			wkUnpaidInt = tx.getUnpaidInterest();
			wkUnpaidPrin = tx.getUnpaidPrincipal();
			wkUnpaidCloseBreach = tx.getUnpaidCloseBreach();
			wkExtraRepay = tx.getExtraRepay();
			// 更新額度檔
			updFacMainRoutine();
			// 還原撥款主檔
			RestoredRepayLoanBorMainRoutine();
			// 本次期款、部分償還的短繳金額處理
			unpaidAmtRoutine();
			// 註記交易內容檔
			loanCom.setLoanBorTxHcode(wkCustNo, wkFacmNo, wkBormNo, wkBorxNo, wkNewBorxNo, tLoanBorMain.getLoanBal(),
					titaVo);
			// 業績處理
			PfDetailRoutine();
			// FirstBorm
			isFirstBorm = false;
		}
	}

	private void updFacMainRoutine() throws LogicException {
		// 鎖定額度檔
		tFacMain = facMainService.holdById(new FacMainId(iCustNo, wkFacmNo), titaVo);
		this.info("updFacMainRoutine..." + tFacMain.getRecycleCode() + "," + wkPrincipal);
		if (tFacMain == null) {
			throw new LogicException(titaVo, "E3011", "額度主檔 戶號 = " + iCustNo + " 額度編號 = " + wkFacmNo); // 鎖定資料時，發生錯誤
		}
		if (tFacMain.getActFg() == 1) {
			throw new LogicException(titaVo, "E0021",
					"額度檔 戶號 = " + tFacMain.getCustNo() + " 額度編號 =  " + tFacMain.getFacmNo()); // 該筆資料待放行中
		}
		if (titaVo.isHcodeNormal() && tFacMain.getUtilAmt().compareTo(wkPrincipal) <= 0) {
			throw new LogicException(titaVo, "E010", "額度全部結案，請執行結案登錄， 額度編號 =  " + tFacMain.getFacmNo()); // 功能選擇錯誤
		}
		// 查詢商品參數檔
		tFacProd = facProdService.findById(tFacMain.getProdNo(), titaVo);
		if (tFacProd == null) {
			throw new LogicException(titaVo, "E0001", "商品參數檔 商品代碼 = " + tFacMain.getProdNo()); // 查詢資料不存在
		}

		if (wkPrincipal.compareTo(BigDecimal.ZERO) == 0) {
			return;
		}
		// 貸出金額(放款餘額)、 已動用額度餘額
		if (titaVo.isHcodeNormal()) {
			tFacMain.setUtilAmt(tFacMain.getUtilAmt().subtract(wkPrincipal));
			if (tFacMain.getRecycleCode().equals("1")) {
				tFacMain.setUtilBal(tFacMain.getUtilBal().subtract(wkPrincipal));
			}
		} else {
			tFacMain.setUtilAmt(tFacMain.getUtilAmt().add(wkPrincipal));
			if (tFacMain.getRecycleCode().equals("1")) {
				tFacMain.setUtilBal(tFacMain.getUtilBal().add(wkPrincipal));
			}
		}
		try {
			facMainService.update(tFacMain, titaVo);
		} catch (DBException e) {
			throw new LogicException(titaVo, "E0007", "額度主檔 戶號 = " + iCustNo + " 額度編號 = " + wkFacmNo); // 更新資料時，發生錯誤
		}

	}

	// 清償違約金
	private void calcCloseBreachNormalRoutine() throws LogicException {
		this.info("calcCloseBreachNormalRoutine ... ");
		this.info("   isBookAcYes = " + this.txBuffer.getTxCom().isBookAcYes());

		wkRepaykindCode = 4; // 清償違約金

		// 出帳餘額=清償違約金-減免金額
		BigDecimal wkRemaindAmt = iCloseBreachAmt.subtract(iReduceAmt);
		// 銷帳總金額
		BigDecimal wkTotalCloseBreach = BigDecimal.ZERO;

		Slice<AcReceivable> slAcReceivable = acReceivableService.acrvFacmNoRange(0, iCustNo, 0, iFacmNo,
				iFacmNo == 0 ? 999 : iFacmNo, 0, Integer.MAX_VALUE);
		if (slAcReceivable == null) {
			throw new LogicException(titaVo, "E0001", "會計銷帳檔"); // 查詢資料不存在
		}

		// 銷帳檔全銷(減免導致與入帳金額不一致，需自行銷帳)
		for (AcReceivable ac : slAcReceivable.getContent()) {
			if (ac.getAcctCode().equals("YOP") && (ac.getRvBal().compareTo(BigDecimal.ZERO) > 0)
					&& (iFacmNo == 0 || iFacmNo == ac.getFacmNo())
					&& (iBormNo == 0 || iBormNo == this.parse.stringToInteger(ac.getRvNo()))) {
				wkTotalCloseBreach = wkTotalCloseBreach.add(ac.getRvBal());
				wkFacmNo = ac.getFacmNo();
				wkCloseBreachAmt = BigDecimal.ZERO;
				if (wkRemaindAmt.compareTo(ac.getRvBal()) >= 0) {
					wkCloseBreachAmt = ac.getRvBal();
					wkRemaindAmt = wkRemaindAmt.subtract(ac.getRvBal());
				} else {
					wkCloseBreachAmt = wkRemaindAmt;
					wkRemaindAmt = BigDecimal.ZERO;
				}
				wkReduceAmt = ac.getRvBal().subtract(wkCloseBreachAmt); // 減免金額
				wkReduceBreachAmt = ac.getRvBal().subtract(wkCloseBreachAmt); // 違約金減免金額
				// 貸方帳務處理
				acDetailBreachRoutine();
				// 計算本筆暫收金額
				compTempAmt();
				// 新增放款交易內容檔
				addBreachBorTxRoutine(ac.getRvNo());
				ac.setRvAmt(ac.getRvBal()); // 以銷帳餘額銷帳
				lAcReceivable.add(ac);
				// FirstBorm、LastFacmNo
				isFirstBorm = false;
			}
		}

		if (wkTotalCloseBreach.compareTo(iCloseBreachAmt) != 0) {
			throw new LogicException(titaVo, "E3074", "清償違約金額 = " + wkTotalCloseBreach); // 回收金額超過應回收清償違約金額
		}

		this.info("calcCloseBreachNormalRoutine end ");
	}

	private void calcCloseBreachEraseRoutine() throws LogicException {
		this.info("calcCloseBreachEraseRoutine ...");
		Slice<LoanBorTx> slLoanBortx = loanBorTxService.custNoTxtNoEq(iCustNo, titaVo.getOrgEntdyI() + 19110000,
				titaVo.getOrgKin(), titaVo.getOrgTlr(), titaVo.getOrgTno(), 0, Integer.MAX_VALUE, titaVo);
		lLoanBorTx = slLoanBortx == null ? null : slLoanBortx.getContent();
		if (lLoanBorTx == null || lLoanBorTx.size() == 0) {
			throw new LogicException(titaVo, "E0001", "交易暫存檔 分行別 = " + titaVo.getOrgKin() + " 交易員代號 = "
					+ titaVo.getOrgTlr() + " 交易序號 = " + titaVo.getOrgTno()); // 查詢資料不存在
		}
		for (LoanBorTx tx : lLoanBorTx) {
			wkCustNo = tx.getCustNo();
			wkFacmNo = tx.getFacmNo();
			wkCloseBreachAmt = tx.getCloseBreachAmt();
			tTempVo = tTempVo.getVo(tx.getOtherFields());
			wkReduceBreachAmt = parse.stringToBigDecimal(tTempVo.getParam("ReduceBreachAmt")); // 減免清償違約金+減免違約金+減免延滯息
			// 還原銷帳檔
			tAcReceivable = new AcReceivable();
			tAcReceivable.setReceivableFlag(4); // 短繳期金
			tAcReceivable.setAcctCode("YOP");
			tAcReceivable.setCustNo(wkCustNo);
			tAcReceivable.setFacmNo(wkFacmNo);
			tAcReceivable.setRvNo(tTempVo.get("RvNo"));
			tAcReceivable.setRvAmt(wkCloseBreachAmt.add(wkReduceBreachAmt));
			lAcReceivable.add(tAcReceivable);
			// 註記交易內容檔
			loanCom.setFacmBorTxHcode(iCustNo, wkFacmNo, titaVo);

		}
		this.info("calcCloseBreachEraseRoutine end");

	}

	// 催收收回
	private void calcOvduRepayNormalRoutine() throws LogicException {
		this.info("calcOvduRepayNormalRoutine ...");

		List<Integer> lStatus = new ArrayList<Integer>(); // 1:催收 2:部分轉呆 3:呆帳 4:催收回復
		lStatus.add(1);
		lStatus.add(2);
		Slice<LoanOverdue> slLoanOverdue = loanOverdueService.ovduCustNoRange(iCustNo, wkFacmNoStart, wkFacmNoEnd,
				wkBormNoStart, wkBormNoEnd, 1, 999, lStatus, 0, Integer.MAX_VALUE, titaVo);
		lLoanOverdue = slLoanOverdue == null ? null : new ArrayList<LoanOverdue>(slLoanOverdue.getContent());
		if (lLoanOverdue == null || lLoanOverdue.size() == 0) {
			throw new LogicException(titaVo, "E0001", "催收呆帳檔"); // 查詢資料不存在
		}
		// 保留原催收呆帳檔List
		List<LoanOverdue> listOvdu = new ArrayList<LoanOverdue>();
		for (LoanOverdue od : lLoanOverdue) {
			tLoanOverdue = new LoanOverdue();
			tLoanOverdue.setOvduPrinBal(od.getOvduPrinBal());
			tLoanOverdue.setOvduIntBal(od.getOvduIntBal());
			tLoanOverdue.setOvduBreachBal(od.getOvduBreachBal());
			tLoanOverdue.setOvduBal(od.getOvduBal());
			tLoanOverdue.setAcDate(od.getAcDate());
			listOvdu.add(tLoanOverdue);
		}

		// 催收違約金餘額
		for (LoanOverdue od : lLoanOverdue) {
			if (wkOvduRepayRemaind.compareTo(BigDecimal.ZERO) > 0
					&& od.getOvduBreachBal().compareTo(BigDecimal.ZERO) > 0) {
				if (wkOvduRepayRemaind.compareTo(od.getOvduBreachBal()) >= 0) {
					wkOvduRepayRemaind = wkOvduRepayRemaind.subtract(od.getOvduBreachBal());
					od.setOvduBal(od.getOvduBal().subtract(od.getOvduBreachBal()));
					od.setOvduBreachBal(BigDecimal.ZERO);
				} else {
					od.setOvduBal(od.getOvduBal().subtract(wkOvduRepayRemaind));
					od.setOvduBreachBal(od.getOvduBreachBal().subtract(wkOvduRepayRemaind));
					wkOvduRepayRemaind = BigDecimal.ZERO;
				}
			}
		}
		// 催收利息餘額
		for (LoanOverdue od : lLoanOverdue) {
			if (wkOvduRepayRemaind.compareTo(BigDecimal.ZERO) > 0
					&& od.getOvduIntBal().compareTo(BigDecimal.ZERO) > 0) {
				if (wkOvduRepayRemaind.compareTo(od.getOvduIntBal()) >= 0) {
					wkOvduRepayRemaind = wkOvduRepayRemaind.subtract(od.getOvduIntBal());
					od.setOvduBal(od.getOvduBal().subtract(od.getOvduIntBal()));
					od.setOvduIntBal(BigDecimal.ZERO);
				} else {
					od.setOvduBal(od.getOvduBal().subtract(wkOvduRepayRemaind));
					od.setOvduIntBal(od.getOvduIntBal().subtract(wkOvduRepayRemaind));
					wkOvduRepayRemaind = BigDecimal.ZERO;
				}
				this.info("wkOvduRepayRemaind=" + wkOvduRepayRemaind + ",IntBal=" + od.getOvduIntBal());
			}
		}
		// 催收本金餘額
		for (LoanOverdue od : lLoanOverdue) {
			if (wkOvduRepayRemaind.compareTo(BigDecimal.ZERO) > 0
					&& od.getOvduPrinBal().compareTo(BigDecimal.ZERO) > 0) {
				if (wkOvduRepayRemaind.compareTo(od.getOvduPrinBal()) >= 0) {
					wkOvduRepayRemaind = wkOvduRepayRemaind.subtract(od.getOvduPrinBal());
					od.setOvduBal(od.getOvduBal().subtract(od.getOvduPrinBal()));
					od.setOvduPrinBal(BigDecimal.ZERO);
				} else {
					od.setOvduBal(od.getOvduBal().subtract(wkOvduRepayRemaind));
					od.setOvduPrinBal(od.getOvduPrinBal().subtract(wkOvduRepayRemaind));
					wkOvduRepayRemaind = BigDecimal.ZERO;
				}
			}
		}

		// 超過催收餘額
		if (wkOvduRepayRemaind.compareTo(BigDecimal.ZERO) > 0) {
			throw new LogicException(titaVo, "E0019", "超過催收餘額，溢繳金額 = " + wkOvduRepayRemaind); // E0019輸入資料錯誤
		}

		for (int i = 0; i < lLoanOverdue.size(); i++) {
			wkOvduAmt = listOvdu.get(i).getOvduBal().subtract(lLoanOverdue.get(i).getOvduBal());
			this.info("1.wkOvduAmt=" + wkOvduAmt + ", oldbal=" + listOvdu.get(i).getOvduBal() + ", newdbal="
					+ lLoanOverdue.get(i).getOvduBal());
			if (wkOvduAmt.compareTo(BigDecimal.ZERO) > 0) {
				wkCustNo = lLoanOverdue.get(i).getCustNo();
				wkFacmNo = lLoanOverdue.get(i).getFacmNo();
				wkBormNo = lLoanOverdue.get(i).getBormNo();
				wkOvduNo = lLoanOverdue.get(i).getOvduNo();
				wkPrincipal = listOvdu.get(i).getOvduPrinBal().subtract(lLoanOverdue.get(i).getOvduPrinBal());
				wkInterest = listOvdu.get(i).getOvduIntBal().subtract(lLoanOverdue.get(i).getOvduIntBal());
				wkBreachAmt = listOvdu.get(i).getOvduBreachBal().subtract(lLoanOverdue.get(i).getOvduBreachBal());

				// initialize tTempVo
				tTempVo.clear();

				// 新增交易暫存
				AddTxTempOvduRoutine(listOvdu.get(i)); // old

				// 更新撥款主檔
				updateLoanBorMainRoutine();
				// 帳務處理
				acDetailOvduRoutine();

				// 計算本筆暫收金額
				compTempAmt();

				// 新增放款交易內容檔(催收收回)
				addOvduBorTxRoutine(lLoanOverdue.get(i)); // new
				// 會計日期
				lLoanOverdue.get(i).setAcDate(wkTbsDy);
				// FirstBorm
				isFirstBorm = false;
			}
		}

		// 更新催收呆帳檔
		try {
			loanOverdueService.updateAll(lLoanOverdue, titaVo);
		} catch (DBException e) {
			throw new LogicException(titaVo, "E0007",
					"催收呆帳檔 戶號 = " + wkCustNo + " 額度編號 = " + wkFacmNo + " 撥款序號 = " + wkBormNo + " 催收序號 = " + wkOvduNo); // 更新資料時，發生錯誤
		}

	}

	// 催收收回訂正
	private void calcOvduRepayEraseRoutine() throws LogicException {
		this.info("calcOvduRepayEraseRoutine ...");
		Slice<LoanBorTx> slLoanBortx = loanBorTxService.custNoTxtNoEq(iCustNo, titaVo.getOrgEntdyI() + 19110000,
				titaVo.getOrgKin(), titaVo.getOrgTlr(), titaVo.getOrgTno(), 0, Integer.MAX_VALUE, titaVo);
		lLoanBorTx = slLoanBortx == null ? null : slLoanBortx.getContent();
		if (lLoanBorTx == null || lLoanBorTx.size() == 0) {
			throw new LogicException(titaVo, "E0001", "交易暫存檔 分行別 = " + titaVo.getOrgKin() + " 交易員代號 = "
					+ titaVo.getOrgTlr() + " 交易序號 = " + titaVo.getOrgTno()); // 查詢資料不存在
		}
		for (LoanBorTx tx : lLoanBorTx) {
			wkCustNo = tx.getCustNo();
			wkFacmNo = tx.getFacmNo();
			wkBormNo = tx.getBormNo();
			wkBorxNo = tx.getBorxNo();
			tTempVo = tTempVo.getVo(tx.getOtherFields());
			// 還原撥款主檔
			RestoreLoanBorMainRoutine();
			// 註記交易內容檔
			loanCom.setLoanBorTxHcode(wkCustNo, wkFacmNo, wkBormNo, wkBorxNo, wkNewBorxNo, tLoanBorMain.getLoanBal(),
					titaVo);
			wkOvduNo = parse.stringToInteger(tTempVo.get("OvduNo"));
			// 還原催收呆帳檔
			tLoanOverdue = loanOverdueService.holdById(new LoanOverdueId(wkCustNo, wkFacmNo, wkBormNo, wkOvduNo),
					titaVo);
			if (tLoanOverdue == null) {
				throw new LogicException(titaVo, "E0006", "催收呆帳檔 戶號 = " + wkCustNo + " 額度編號 = " + wkFacmNo + " 撥款序號 = "
						+ wkBormNo + " 催收序號 = " + wkOvduNo); // 鎖定資料時，發生錯誤
			}
			tLoanOverdue.setOvduPrinBal(this.parse.stringToBigDecimal(tTempVo.get("OvduPrinBal")));
			tLoanOverdue.setOvduIntBal(this.parse.stringToBigDecimal(tTempVo.get("OvduIntBal")));
			tLoanOverdue.setOvduBreachBal(this.parse.stringToBigDecimal(tTempVo.get("OvduBreachBal")));
			tLoanOverdue.setOvduBal(this.parse.stringToBigDecimal(tTempVo.get("OvduBal")));
			tLoanOverdue.setAcDate(this.parse.stringToInteger(tTempVo.get("AcDate")));
			try {
				loanOverdueService.update(tLoanOverdue, titaVo);
			} catch (DBException e) {
				throw new LogicException(titaVo, "E0007", "催收呆帳檔 戶號 = " + wkCustNo + " 額度編號 = " + wkFacmNo + " 撥款序號 = "
						+ wkBormNo + " 催收序號 = " + wkOvduNo); // 更新資料時，發生錯誤
			}
		}

	}

	// ------------------------------------------------------------------------------
	// 更新撥款主檔(放款收回)
	private void updLoanBorMainRoutine() throws LogicException {
		this.info("updLoanBorMainRoutine ... ");
//		this.info("   tFacProd.getCharCode() = " + tFacProd.getCharCode());
		wkNewDueAmt = BigDecimal.ZERO;
		wkNewTotalPeriod = 0;
		// 38 ExtraRepayCode 攤還額異動碼
		int wkPayMethod = iPayMethod;
		if (wkPayMethod == 0) {
			if ("1".equals(tFacMain.getExtraRepayCode())) {
				wkPayMethod = 1;
			} else {
				wkPayMethod = 2;
			}
		}
		tLoanBorMain.setLastBorxNo(wkBorxNo);
		tLoanBorMain.setLoanBal(tLoanBorMain.getLoanBal().subtract(wkPrincipal));
		if (tLoanBorMain.getLoanBal().compareTo(BigDecimal.ZERO) == 0)

		{
			tLoanBorMain.setStatus(3);
		}
		if (isCalcRepayInt) {
			tLoanBorMain.setStoreRate(loanCalcRepayIntCom.getStoreRate());
			if (tLoanBorMain.getAmortizedCode().equals("3")
					&& !loanCalcRepayIntCom.getDueAmt().equals(tLoanBorMain.getDueAmt())) {
				wkNewDueAmt = loanCalcRepayIntCom.getDueAmt();
				tLoanBorMain.setDueAmt(wkNewDueAmt);
			}
			tLoanBorMain.setRepaidPeriod(tLoanBorMain.getRepaidPeriod() + loanCalcRepayIntCom.getRepaidPeriod());
			tLoanBorMain.setPaidTerms(loanCalcRepayIntCom.getPaidTerms());
			tLoanBorMain.setPrevPayIntDate(loanCalcRepayIntCom.getPrevPaidIntDate());
			tLoanBorMain.setPrevRepaidDate(loanCalcRepayIntCom.getPrevRepaidDate());
			tLoanBorMain.setNextPayIntDate(loanCalcRepayIntCom.getNextPayIntDate());
			tLoanBorMain.setNextRepayDate(loanCalcRepayIntCom.getNextRepayDate());
			// 部分償還
			if (wkRepaykindCode == 1) {
				int wkGracePeriod = loanCom.getGracePeriod(tLoanBorMain.getAmortizedCode(), tLoanBorMain.getFreqBase(),
						tLoanBorMain.getPayIntFreq(), tLoanBorMain.getSpecificDate(), tLoanBorMain.getSpecificDd(),
						tLoanBorMain.getGraceDate());
				// 剩餘還本期數
				int wkDueTerms = tLoanBorMain.getPaidTerms() > wkGracePeriod
						? tLoanBorMain.getTotalPeriod() - tLoanBorMain.getPaidTerms()
						: tLoanBorMain.getTotalPeriod() - wkGracePeriod;

				// 重算期數
				if (wkPayMethod == 2) {
					wkDueTerms = loanDueAmtCom.getDueTerms(tLoanBorMain.getLoanBal(), tLoanBorMain.getStoreRate(),
							tLoanBorMain.getAmortizedCode(), tLoanBorMain.getFreqBase(), tLoanBorMain.getPayIntFreq(),
							tLoanBorMain.getFinalBal(), tLoanBorMain.getDueAmt(), titaVo);
					// 寬限期 + 剩餘還本期數(寬限期內)；已繳期數 + 剩餘還本期數(超過寬限期)
					int wkTotalPeriod = tLoanBorMain.getPaidTerms() > wkGracePeriod
							? wkDueTerms + tLoanBorMain.getPaidTerms()
							: wkDueTerms + wkGracePeriod;
					tLoanBorMain.setTotalPeriod(wkTotalPeriod);
				}
				// 重算期金
				else {
					wkNewDueAmt = loanDueAmtCom.getDueAmt(tLoanBorMain.getLoanBal(), tLoanBorMain.getStoreRate(),
							tLoanBorMain.getAmortizedCode(), tLoanBorMain.getFreqBase(), wkDueTerms, 0,
							tLoanBorMain.getPayIntFreq(), tLoanBorMain.getFinalBal(), titaVo);
					tLoanBorMain.setDueAmt(wkNewDueAmt);
				}
			}
		}
		tLoanBorMain.setLastEntDy(titaVo.getEntDyI());
		tLoanBorMain.setLastKinbr(titaVo.getKinbr());
		tLoanBorMain.setLastTlrNo(titaVo.getTlrNo());
		tLoanBorMain.setLastTxtNo(titaVo.getTxtNo());
		try {
			loanBorMainService.update(tLoanBorMain, titaVo);
		} catch (DBException e) {
			throw new LogicException(titaVo, "E0007",
					"撥款主檔 戶號 = " + iCustNo + " 額度編號 = " + wkFacmNo + " 撥款序號 = " + wkBormNo); // 更新資料時，發生錯誤
		}
	}

	// 還原撥款主檔(放款收回)
	private void RestoredRepayLoanBorMainRoutine() throws LogicException {
		this.info("RestoredRepayLoanBorMainRoutine ... ");

		tLoanBorMain = loanBorMainService.holdById(new LoanBorMainId(wkCustNo, wkFacmNo, wkBormNo), titaVo);
		if (tLoanBorMain == null) {
			throw new LogicException(titaVo, "E0006",
					"撥款主檔 戶號 = " + iCustNo + " 額度編號 = " + wkFacmNo + " 撥款序號 = " + wkBormNo); // 鎖定資料時，發生錯誤
		}
		if (tLoanBorMain.getActFg() == 1) {
			throw new LogicException(titaVo, "E0021", "放款主檔 戶號 = " + tLoanBorMain.getCustNo() + " 額度編號 =  "
					+ tLoanBorMain.getFacmNo() + " 撥款序號 = " + tLoanBorMain.getBormNo()); // 該筆資料待放行中
		}
		wkNewBorxNo = tLoanBorMain.getLastBorxNo() + 1;
		// 放款交易訂正交易須由最後一筆交易開始訂正
		loanCom.checkEraseBormTxSeqNo(tLoanBorMain, titaVo);
		tLoanBorMain.setLastBorxNo(wkNewBorxNo);
		tLoanBorMain.setStoreRate(this.parse.stringToBigDecimal(tTempVo.get("StoreRate")));
		tLoanBorMain.setLoanBal(this.parse.stringToBigDecimal(tTempVo.get("LoanBal")));
		tLoanBorMain.setStatus(0);
		tLoanBorMain.setRepaidPeriod(this.parse.stringToInteger(tTempVo.get("RepaidPeriod")));
		tLoanBorMain.setPaidTerms(this.parse.stringToInteger(tTempVo.get("PaidTerms")));
		tLoanBorMain.setPrevPayIntDate(this.parse.stringToInteger(tTempVo.get("PrevPayIntDate")));
		tLoanBorMain.setPrevRepaidDate(this.parse.stringToInteger(tTempVo.get("PrevRepaidDate")));
		tLoanBorMain.setNextPayIntDate(this.parse.stringToInteger(tTempVo.get("NextPayIntDate")));
		tLoanBorMain.setNextRepayDate(this.parse.stringToInteger(tTempVo.get("NextRepayDate")));
		tLoanBorMain.setDueAmt(this.parse.stringToBigDecimal(tTempVo.get("DueAmt")));
		tLoanBorMain.setLastEntDy(this.parse.stringToInteger(tTempVo.get("LastEntDy")));
		tLoanBorMain.setLastKinbr(tTempVo.get("LastKinbr"));
		tLoanBorMain.setLastTlrNo(tTempVo.get("LastTlrNo"));
		tLoanBorMain.setLastTxtNo(tTempVo.get("LastTxtNo"));
		try {
			loanBorMainService.update(tLoanBorMain, titaVo);
		} catch (DBException e) {
			throw new LogicException(titaVo, "E0007",
					"撥款主檔 戶號 = " + wkCustNo + " 額度編號 = " + wkFacmNo + " 撥款序號 = " + wkBormNo); // 更新資料時，發生錯誤
		}
	}

	// 更新撥款主檔(催收收回、清償違約金)
	private void updateLoanBorMainRoutine() throws LogicException {
		this.info("updateLoanBorMainRoutine ... ");
		tLoanBorMain = loanBorMainService.holdById(new LoanBorMainId(iCustNo, wkFacmNo, wkBormNo), titaVo);
		if (tLoanBorMain == null) {
			throw new LogicException(titaVo, "E0006",
					"撥款主檔 戶號 = " + iCustNo + " 額度編號 = " + wkFacmNo + " 撥款序號 = " + wkBormNo); // 鎖定資料時，發生錯誤
		}
		if (tLoanBorMain.getActFg() == 1) {
			throw new LogicException(titaVo, "E0021", "放款主檔 戶號 = " + tLoanBorMain.getCustNo() + " 額度編號 =  "
					+ tLoanBorMain.getFacmNo() + " 撥款序號 = " + tLoanBorMain.getBormNo()); // 該筆資料待放行中
		}
		wkBorxNo = tLoanBorMain.getLastBorxNo() + 1;
		tLoanBorMain.setLastBorxNo(wkBorxNo);
		tLoanBorMain.setLastEntDy(titaVo.getEntDyI());
		tLoanBorMain.setLastKinbr(titaVo.getKinbr());
		tLoanBorMain.setLastTlrNo(titaVo.getTlrNo());
		tLoanBorMain.setLastTxtNo(titaVo.getTxtNo());
		try {
			loanBorMainService.update(tLoanBorMain, titaVo);
		} catch (DBException e) {
			throw new LogicException(titaVo, "E0007",
					"撥款主檔 戶號 = " + iCustNo + " 額度編號 = " + wkFacmNo + " 撥款序號 = " + wkBormNo); // 更新資料時，發生錯誤
		}
	}

	// 還原撥款主檔(催收收回)
	private void RestoreLoanBorMainRoutine() throws LogicException {
		this.info("RestoreLoanBorMainRoutine ... ");

		tLoanBorMain = loanBorMainService.holdById(new LoanBorMainId(wkCustNo, wkFacmNo, wkBormNo), titaVo);
		if (tLoanBorMain == null) {
			throw new LogicException(titaVo, "E0006",
					"撥款主檔 戶號 = " + iCustNo + " 額度編號 = " + wkFacmNo + " 撥款序號 = " + wkBormNo); // 鎖定資料時，發生錯誤
		}
		if (tLoanBorMain.getActFg() == 1) {
			throw new LogicException(titaVo, "E0021", "放款主檔 戶號 = " + tLoanBorMain.getCustNo() + " 額度編號 =  "
					+ tLoanBorMain.getFacmNo() + " 撥款序號 = " + tLoanBorMain.getBormNo()); // 該筆資料待放行中
		}
		wkNewBorxNo = tLoanBorMain.getLastBorxNo() + 1;
		// 放款交易訂正交易須由最後一筆交易開始訂正
		loanCom.checkEraseBormTxSeqNo(tLoanBorMain, titaVo);
		tLoanBorMain.setLastBorxNo(wkNewBorxNo);
		tLoanBorMain.setLastEntDy(this.parse.stringToInteger(tTempVo.get("LastEntDy")));
		tLoanBorMain.setLastKinbr(tTempVo.get("LastKinbr"));
		tLoanBorMain.setLastTlrNo(tTempVo.get("LastTlrNo"));
		tLoanBorMain.setLastTxtNo(tTempVo.get("LastTxtNo"));
		try {
			loanBorMainService.update(tLoanBorMain, titaVo);
		} catch (DBException e) {
			throw new LogicException(titaVo, "E0007",
					"撥款主檔 戶號 = " + wkCustNo + " 額度編號 = " + wkFacmNo + " 撥款序號 = " + wkBormNo); // 更新資料時，發生錯誤
		}
	}

	// 新增計息明細
	private void addLoanIntDetailRoutine() throws LogicException {
		this.info("addLoanIntDetailRoutine ... ");

		int wkIntSeq = 0;

		for (CalcRepayIntVo c : lCalcRepayIntVo) {
			wkIntSeq++;
			wkIntStartDate = c.getStartDate() < wkIntStartDate ? c.getStartDate() : wkIntStartDate;
			wkIntEndDate = c.getEndDate() > wkIntEndDate ? c.getEndDate() : wkIntEndDate;
			wkLoanBal = wkLoanBal.subtract(c.getPrincipal());
			tLoanIntDetailId = new LoanIntDetailId();
			tLoanIntDetailId.setCustNo(c.getCustNo());
			tLoanIntDetailId.setFacmNo(c.getFacmNo());
			tLoanIntDetailId.setBormNo(c.getBormNo());
			tLoanIntDetailId.setAcDate(wkTbsDy);
			tLoanIntDetailId.setTlrNo(titaVo.getTlrNo());
			tLoanIntDetailId.setTxtNo(titaVo.getTxtNo());
			tLoanIntDetailId.setIntSeq(wkIntSeq);
			tLoanIntDetail = new LoanIntDetail();
			tLoanIntDetail.setCustNo(c.getCustNo());
			tLoanIntDetail.setFacmNo(c.getFacmNo());
			tLoanIntDetail.setBormNo(c.getBormNo());
			tLoanIntDetail.setAcDate(wkTbsDy);
			tLoanIntDetail.setTlrNo(titaVo.getTlrNo());
			tLoanIntDetail.setTxtNo(titaVo.getTxtNo());
			tLoanIntDetail.setIntSeq(wkIntSeq);
			tLoanIntDetail.setLoanIntDetailId(tLoanIntDetailId);
			tLoanIntDetail.setIntStartDate(c.getStartDate());
			tLoanIntDetail.setIntEndDate(c.getEndDate());
			tLoanIntDetail.setIntDays(c.getDays());
			tLoanIntDetail.setBreachDays(c.getOdDays());
			tLoanIntDetail.setMonthLimit(c.getMonthLimit());
			tLoanIntDetail.setIntFlag(c.getInterestFlag());
			tLoanIntDetail.setCurrencyCode(tFacMain.getCurrencyCode());
			tLoanIntDetail.setIntRate(c.getStoreRate());
			tLoanIntDetail.setRateIncr(c.getRateIncr());
			tLoanIntDetail.setAmount(c.getAmount());
			tLoanIntDetail.setIndividualIncr(c.getIndividualIncr());
			tLoanIntDetail.setPrincipal(c.getPrincipal());
			tLoanIntDetail.setInterest(c.getInterest());
			tLoanIntDetail.setDelayInt(c.getDelayInt());
			tLoanIntDetail.setBreachAmt(c.getBreachAmt());
			tLoanIntDetail.setCloseBreachAmt(c.getCloseBreachAmt());
			tLoanIntDetail.setBreachGetCode(c.getBreachGetCode());
			tLoanIntDetail.setLoanBal(wkLoanBal);
			tLoanIntDetail.setExtraRepayFlag(c.getExtraRepayFlag());
			tLoanIntDetail.setProdNo(tFacMain.getProdNo());
			tLoanIntDetail.setBaseRateCode(tFacMain.getBaseRateCode());
			try {
				loanIntDetailService.insert(tLoanIntDetail, titaVo);
			} catch (DBException e) {
				throw new LogicException(titaVo, "E0005", "計息明細 Key = " + tLoanIntDetailId); // 新增資料時，發生錯誤
			}
		}
	}

	// 新增放款交易內容檔(放款收回)
	private void addRepayBorTxRoutine() throws LogicException {
		this.info("addLoanBorTxRoutine ... ");

		tLoanBorTx = new LoanBorTx();
		tLoanBorTxId = new LoanBorTxId();
		loanCom.setLoanBorTx(tLoanBorTx, tLoanBorTxId, iCustNo, wkFacmNo, wkBormNo, wkBorxNo, titaVo);
		if (iRepayType == 2) {
			tLoanBorTx.setDesc("部分償還本金");
		} else {
			if (isRepayPrincipal) {
				tLoanBorTx.setDesc("回收登錄");
			} else {
				tLoanBorTx.setDesc("回收利息");
			}
		}

		tLoanBorTx.setRepayCode(iRpCode); // 還款來源
		tLoanBorTx.setEntryDate(iEntryDate);
		tLoanBorTx.setDueDate(wkDueDate);
		//
		tLoanBorTx.setLoanBal(tLoanBorMain.getLoanBal());
		tLoanBorTx.setRate(tLoanBorMain.getStoreRate());
		tLoanBorTx.setIntStartDate(wkIntStartDate);
		tLoanBorTx.setIntEndDate(wkIntEndDate);
		tLoanBorTx.setRepaidPeriod(wkRepaidPeriod);
		tLoanBorTx.setPrincipal(wkPrincipal);
		tLoanBorTx.setInterest(wkInterest);
		tLoanBorTx.setDelayInt(wkDelayInt);
		tLoanBorTx.setBreachAmt(wkBreachAmt);
		tLoanBorTx.setCloseBreachAmt(wkCloseBreachAmt);
		tLoanBorTx.setExtraRepay(wkExtraRepay);
		tLoanBorTx.setUnpaidInterest(wkUnpaidInt); // 短繳利息
		tLoanBorTx.setUnpaidPrincipal(wkUnpaidPrin); // 短繳本金
		tLoanBorTx.setUnpaidCloseBreach(wkUnpaidCloseBreach);// 短繳清償違約金
		tLoanBorTx.setTempAmt(wkTempAmt); // 暫收抵繳金額

		// 繳息首筆、繳息次筆
		if (isFirstBorm) {
			tLoanBorTx.setDisplayflag("F"); // 繳息首筆
			tLoanBorTx.setTxAmt(iTxAmt);
			tLoanBorTx.setShortfall(iShortAmt); // 短收
			tLoanBorTx.setOverflow(iOverAmt); // 溢收
		} else if (isCalcRepayInt) {
			tLoanBorTx.setDisplayflag("I"); // 繳息次筆
		} else {
			tLoanBorTx.setDisplayflag("Y"); // 無繳息
		}

		/* OtherFields */

		// 減免金額
		if (wkReduceAmt.compareTo(BigDecimal.ZERO) > 0) {
			tTempVo.putParam("ReduceAmt", wkReduceAmt);
		}
		if (wkReduceBreachAmt.compareTo(BigDecimal.ZERO) > 0) {
			tTempVo.putParam("ReduceBreachAmt", wkReduceBreachAmt); // 減免清償違約金+減免違約金+減免延滯息
		}
		// 支票繳款利息免印花稅
		if (iRpCode == 4) {
			tTempVo.putParam("StampFreeAmt", wkInterest);
		}
		// 短繳金額收回
		if (wkShortfallPrincipal.compareTo(BigDecimal.ZERO) > 0) {
			tTempVo.putParam("ShortfallPrin", wkShortfallPrincipal);
		}
		if (wkShortfallInterest.compareTo(BigDecimal.ZERO) > 0) {
			tTempVo.putParam("ShortfallInt", wkShortfallInterest);
		}
		if (wkShortCloseBreach.compareTo(BigDecimal.ZERO) > 0) {
			tTempVo.putParam("ShortCloseBreach", wkShortCloseBreach);
		}
		if (wkPaidTerms > 0) {
			tTempVo.putParam("PaidTerms", wkPaidTerms);// 回收期數
		}
		if (wkAcctFee.compareTo(BigDecimal.ZERO) > 0) {
			tTempVo.putParam("AcctFee", wkAcctFee);
		}
		if (wkModifyFee.compareTo(BigDecimal.ZERO) > 0) {
			tTempVo.putParam("ModifyFee", wkModifyFee);
		}
		if (wkFireFee.compareTo(BigDecimal.ZERO) > 0) {
			tTempVo.putParam("FireFee", wkFireFee);
		}
		if (wkLawFee.compareTo(BigDecimal.ZERO) > 0) {
			tTempVo.putParam("LawFee", wkLawFee);
		}
		// 新攤還金額、新繳款總期數
		if (wkNewDueAmt.compareTo(BigDecimal.ZERO) > 0) {
			tTempVo.putParam("NewDueAmt", wkNewDueAmt);
		}
		if (wkNewTotalPeriod > 0) {
			tTempVo.putParam("TotalPeriod", wkNewTotalPeriod);
		}
		if (isFirstBorm) {
			tTempVo.putParam("RepayKindCode", wkRepaykindCode);
			tTempVo.putParam("DscptCode", iDscptCode); // 摘要代碼
			if (titaVo.getBacthNo().trim() != "") {
				tTempVo.putParam("BatchNo", titaVo.getBacthNo()); // 整批批號
				tTempVo.putParam("DetailSeq", titaVo.get("RpDetailSeq1")); // 明細序號
			}
			if (iExtraRepay.compareTo(BigDecimal.ZERO) > 0) { // 部分償還本金 > 0
				tTempVo.putParam("ExtraRepayFlag", iExtraRepayFlag);
				tTempVo.putParam("UnpaidIntFlag", iUnpaidIntFlag);
				tTempVo.putParam("PayMethod", iPayMethod);
			}
		}
		tLoanBorTx.setOtherFields(tTempVo.getJsonString());
		try {
			loanBorTxService.insert(tLoanBorTx, titaVo);
		} catch (DBException e) {
			throw new LogicException(titaVo, "E0005", "放款交易內容檔 " + e.getErrorMsg()); // 新增資料時，發生錯誤
		}
	}

	// 交易前撥款主檔留存欄（訂正使用）
	private void addRepayTxTempBormRoutine() throws LogicException {
		this.info("addRepayTxTempBormRoutine ... ");
		tTempVo.putParam("StoreRate", tLoanBorMain.getStoreRate());
		tTempVo.putParam("LoanBal", tLoanBorMain.getLoanBal());
		tTempVo.putParam("RepaidPeriod", tLoanBorMain.getRepaidPeriod());
		tTempVo.putParam("PaidTerms", tLoanBorMain.getPaidTerms());
		tTempVo.putParam("PrevPayIntDate", tLoanBorMain.getPrevPayIntDate());
		tTempVo.putParam("PrevRepaidDate", tLoanBorMain.getPrevRepaidDate());
		tTempVo.putParam("NextPayIntDate", tLoanBorMain.getNextPayIntDate());
		tTempVo.putParam("NextRepayDate", tLoanBorMain.getNextRepayDate());
		tTempVo.putParam("DueAmt", tLoanBorMain.getDueAmt());
		tTempVo.putParam("LastEntDy", tLoanBorMain.getLastEntDy());
		tTempVo.putParam("LastKinbr", tLoanBorMain.getLastKinbr());
		tTempVo.putParam("LastTlrNo", tLoanBorMain.getLastTlrNo());
		tTempVo.putParam("LastTxtNo", tLoanBorMain.getLastTxtNo());
	}

	// 新增放款交易內容檔(清償違約金)
	private void addBreachBorTxRoutine(String rvNo) throws LogicException {
		tLoanBorTx = new LoanBorTx();
		tLoanBorTxId = new LoanBorTxId();
		loanCom.setFacmBorTx(tLoanBorTx, tLoanBorTxId, iCustNo, wkFacmNo, titaVo);
		tLoanBorTx.setRepayCode(iRpCode); // 還款來源
		tLoanBorTx.setDesc("回收登錄-清償違約金");
		tLoanBorTx.setEntryDate(iEntryDate);
		//
		tLoanBorTx.setCloseBreachAmt(wkCloseBreachAmt);
		tLoanBorTx.setTempAmt(wkTempAmt);
		// 首筆
		if (isFirstBorm) {
			tLoanBorTx.setDisplayflag("A");
			tLoanBorTx.setTxAmt(iTxAmt);
		}
		// 其他欄位
		tTempVo.clear();
		tTempVo.putParam("RvNo", rvNo); // 銷帳編號
		// 減免金額
		if (wkReduceAmt.compareTo(BigDecimal.ZERO) > 0) {
			tTempVo.putParam("ReduceAmt", wkReduceAmt);
		}
		if (wkReduceBreachAmt.compareTo(BigDecimal.ZERO) > 0) {
			tTempVo.putParam("ReduceBreachAmt", wkReduceBreachAmt); // 減免清償違約金+減免違約金+減免延滯息
		}
		tLoanBorTx.setOtherFields(tTempVo.getJsonString());
		try {
			loanBorTxService.insert(tLoanBorTx, titaVo);
		} catch (DBException e) {
			throw new LogicException(titaVo, "E0005", "放款交易內容檔 " + e.getErrorMsg()); // 新增資料時，發生錯誤
		}
	}

	// 新增放款交易內容檔(催收收回)
	private void addOvduBorTxRoutine(LoanOverdue od) throws LogicException {
		this.info("addLoanBorTxRoutine ... ");

		tLoanBorTx = new LoanBorTx();
		tLoanBorTxId = new LoanBorTxId();
		loanCom.setLoanBorTx(tLoanBorTx, tLoanBorTxId, iCustNo, wkFacmNo, wkBormNo, wkBorxNo, titaVo);
		tLoanBorTx.setRepayCode(iRpCode); // 還款來源
		tLoanBorTx.setDesc("回收登錄-催收收回");
		tLoanBorTx.setEntryDate(iEntryDate);
		//
		tLoanBorTx.setLoanBal(od.getOvduBal());
		tLoanBorTx.setPrincipal(wkPrincipal);
		tLoanBorTx.setInterest(wkInterest);
		tLoanBorTx.setBreachAmt(wkBreachAmt);
		tLoanBorTx.setDisplayflag("A");
		// 首筆
		if (isFirstBorm) {
			tLoanBorTx.setTxAmt(iTxAmt);
			tLoanBorTx.setTempAmt(wkTempAmt);
		}

		// 交易前撥款主檔留存欄（訂正使用）
		tLoanBorTx.setOtherFields(tTempVo.getJsonString());
		try {
			loanBorTxService.insert(tLoanBorTx, titaVo);
		} catch (DBException e) {
			throw new LogicException(titaVo, "E0005", "放款交易內容檔 " + e.getErrorMsg()); // 新增資料時，發生錯誤
		}
	}

	// 新增交易暫存(催收收回)
	private void AddTxTempOvduRoutine(LoanOverdue od) throws LogicException {
		this.info("AddTxTempOvduRoutine ... ");
		tTempVo.putParam("OvduNo", od.getOvduNo());
		tTempVo.putParam("OvduPrinBal", od.getOvduPrinBal());
		tTempVo.putParam("OvduIntBal", od.getOvduIntBal());
		tTempVo.putParam("OvduBreachBal", od.getOvduBreachBal());
		tTempVo.putParam("OvduBal", od.getOvduBal());
		tTempVo.putParam("AcDate", od.getAcDate());
	}

	// 貸方回收金額帳務處理
	private void acDetailCrRoutine() throws LogicException {
		this.info("acDetailCrRoutine ... ");
		this.info("   isBookAcYes = " + this.txBuffer.getTxCom().isBookAcYes());

		if (!this.txBuffer.getTxCom().isBookAcYes()) {
			return;
		}
		// 本金
		acDetail = new AcDetail();
		acDetail.setDbCr("C");
		acDetail.setAcctCode(tFacMain.getAcctCode());
		acDetail.setTxAmt(wkPrincipal.subtract(wkShortfallPrincipal)); // 回收短繳另外出帳
		acDetail.setCustNo(wkCustNo);
		acDetail.setFacmNo(wkFacmNo);
		acDetail.setBormNo(wkBormNo);
		lAcDetail.add(acDetail);
		// 利息
		acDetail = new AcDetail();
		acDetail.setDbCr("C");
		acDetail.setAcctCode(loanCom.setIntAcctCode(tFacMain.getAcctCode()));
		acDetail.setTxAmt(wkInterest.subtract(wkShortfallInterest)); // 回收短繳另外出帳
		acDetail.setCustNo(wkCustNo);
		acDetail.setFacmNo(wkFacmNo);
		acDetail.setBormNo(wkBormNo);
		lAcDetail.add(acDetail);
		// 延滯息
		acDetail = new AcDetail();
		acDetail.setDbCr("C");
		acDetail.setAcctCode("IOV");
		acDetail.setTxAmt(wkDelayInt);
		acDetail.setCustNo(wkCustNo);
		acDetail.setFacmNo(wkFacmNo);
		acDetail.setBormNo(wkBormNo);
		lAcDetail.add(acDetail);
		// 違約金
		acDetail = new AcDetail();
		acDetail.setDbCr("C");
		acDetail.setAcctCode("IOP");
		acDetail.setTxAmt(wkBreachAmt);
		acDetail.setCustNo(wkCustNo);
		acDetail.setFacmNo(wkFacmNo);
		acDetail.setBormNo(wkBormNo);
		lAcDetail.add(acDetail);

		// 清償違約金掛短繳不出帳；回收短繳另外出帳
	}

	// 貸方帳務處理(清償違約金)
	private void acDetailBreachRoutine() throws LogicException {
		if (!this.txBuffer.getTxCom().isBookAcYes()) {
			return;
		}
		acDetail = new AcDetail();
		acDetail.setDbCr("C");
		acDetail.setAcctCode("IOP");
		acDetail.setTxAmt(wkCloseBreachAmt);
		acDetail.setCustNo(iCustNo);
		acDetail.setFacmNo(wkFacmNo);
		acDetail.setBormNo(0);
		lAcDetail.add(acDetail);
	}

	// 貸方帳務處理(催收收回)
	private void acDetailOvduRoutine() throws LogicException {
		this.info("AcDetailDbCr4Routine ... ");

		if (!this.txBuffer.getTxCom().isBookAcYes()) {
			return;
		}
		// 貸:催收款項
		acDetail = new AcDetail();
		acDetail.setDbCr("C");
		acDetail.setAcctCode("990");
		acDetail.setTxAmt(wkOvduAmt);
		acDetail.setCustNo(iCustNo);
		acDetail.setFacmNo(wkFacmNo);
		acDetail.setBormNo(wkBormNo);
		lAcDetail.add(acDetail);
	}

	// 貸方：費用、短繳期金
	private void batxSettleUnpaid() throws LogicException {
		this.baTxList = new ArrayList<BaTxVo>();
		// call 應繳試算
		this.baTxList = baTxCom.settingUnPaid(iEntryDate, iCustNo, iFacmNo, iBormNo, 0, iTxAmt, titaVo); // 00-費用全部(已到期)
		if (this.baTxList != null) {
			// 部分償還有短繳金額時，短繳金額先扣除累短收-利息，再短繳本次利息
			if (iExtraRepay.compareTo(BigDecimal.ZERO) > 0 && iShortAmt.compareTo(BigDecimal.ZERO) > 0) {
				for (BaTxVo ba : this.baTxList) {
					if (ba.getDataKind() == 1 && ba.getInterest().compareTo(BigDecimal.ZERO) > 0) {
						if (ba.getInterest().compareTo(BigDecimal.ZERO) > 0) {
							if (wkUnpaidAmtRemaind.compareTo(ba.getInterest()) > 0) {
								wkUnpaidAmtRemaind = wkUnpaidAmtRemaind.subtract(ba.getInterest());
								ba.setInterest(BigDecimal.ZERO);
								ba.setAcctAmt(BigDecimal.ZERO);
								this.info(" 1 wkUnpaidAmtRemaind =" + wkUnpaidAmtRemaind + ", iShortAmt=" + iShortAmt);
							} else {
								ba.setInterest(ba.getInterest().subtract(wkUnpaidAmtRemaind));
								ba.setAcctAmt(ba.getAcctAmt().subtract(wkUnpaidAmtRemaind));
								wkUnpaidAmtRemaind = BigDecimal.ZERO;
								this.info(" 2 wkUnpaidAmtRemaind =" + wkUnpaidAmtRemaind + ", iShortAmt=" + iShortAmt);
							}
						}
					}
				}
			}
			// 是否回收費用
			if ("N".equals(iPayFeeFlag)) {
				for (BaTxVo ba : this.baTxList) {
					if (ba.getDataKind() == 1 && ba.getRepayType() >= 4) {
						ba.setAcctAmt(BigDecimal.ZERO);
					}
				}
			}
			// 出帳
			for (BaTxVo ba : this.baTxList) {
				if (ba.getDataKind() == 1 && ba.getAcctAmt().compareTo(BigDecimal.ZERO) > 0) {
					acDetail = new AcDetail();
					acDetail.setDbCr("C");
					acDetail.setAcctCode(ba.getAcctCode());
					acDetail.setTxAmt(ba.getAcctAmt());
					acDetail.setCustNo(ba.getCustNo());
					acDetail.setFacmNo(ba.getFacmNo());
					acDetail.setBormNo(ba.getBormNo());
					acDetail.setRvNo(ba.getRvNo());
					acDetail.setReceivableFlag(ba.getReceivableFlag());
					lAcDetail.add(acDetail);
				}
			}
		}
	}

	// 費用、短繳期金
	private void getSettleUnpaid() throws LogicException {
		isSettleUnpaid = false;
		this.wkAcctFee = BigDecimal.ZERO;
		this.wkModifyFee = BigDecimal.ZERO;
		this.wkFireFee = BigDecimal.ZERO;
		this.wkLawFee = BigDecimal.ZERO;
		this.wkShortfallInterest = BigDecimal.ZERO; // 累短收 - 利息
		this.wkShortfallPrincipal = BigDecimal.ZERO; // 累短收 - 本金
		this.wkShortCloseBreach = BigDecimal.ZERO; // 累短收 - 清償違約金
		// RepayType 同撥款：01-期款, 第一筆：04-帳管費, 05-火險費, 06-契變手續費, 07-法務費
		if (this.baTxList != null) {
			for (BaTxVo ba : this.baTxList) {
				if (ba.getDataKind() == 1 && ba.getAcctAmt().compareTo(BigDecimal.ZERO) > 0) {
					if ((ba.getFacmNo() == wkFacmNo && ba.getBormNo() == wkBormNo && ba.getRepayType() == 1)
							|| ba.getRepayType() > 1) {
						isSettleUnpaid = true;
						if (ba.getRepayType() == 1) {
							this.wkShortfallPrincipal = ba.getPrincipal();
							this.wkShortfallInterest = ba.getInterest();
							this.wkShortCloseBreach = ba.getCloseBreachAmt();
						} else if (ba.getRepayType() == 4) {
							this.wkAcctFee = this.wkAcctFee.add(ba.getAcctAmt());
						} else if (ba.getRepayType() == 5) {
							this.wkFireFee = this.wkFireFee.add(ba.getAcctAmt());
						} else if (ba.getRepayType() == 6) {
							this.wkModifyFee = this.wkModifyFee.add(ba.getAcctAmt());
						} else if (ba.getRepayType() == 7) {
							this.wkLawFee = this.wkLawFee.add(ba.getAcctAmt());
						}
						ba.setAcctAmt(BigDecimal.ZERO);
					}
				}
			}
		}

	}

	// 欠繳金額處理
	private void unpaidAmtRoutine() throws LogicException {
		// 短繳利息, 新增銷帳檔
		if (wkUnpaidInt.compareTo(BigDecimal.ZERO) > 0) {
			acRvUnpaidAmt(loanCom.setShortIntAcctCode(tFacMain.getAcctCode()), wkUnpaidInt);
		}

		// 短繳本金處理, 新增銷帳檔
		if (wkUnpaidPrin.compareTo(BigDecimal.ZERO) > 0) {
			acRvUnpaidAmt(loanCom.setShortPrinAcctCode(tFacMain.getAcctCode()), wkUnpaidPrin);
		}

		// 短繳清償違約金處理, 新增銷帳檔
		if (wkUnpaidCloseBreach.compareTo(BigDecimal.ZERO) > 0) {
			acRvUnpaidAmt("YOP", wkUnpaidCloseBreach);
		}

	}

	// 短繳金額
	private void acRvUnpaidAmt(String acctCode, BigDecimal shortAmt) throws LogicException {
		this.info("acRvUnpaidAmt ...");

		tAcReceivable = new AcReceivable();
		tAcReceivable.setReceivableFlag(4); // 短繳期金
		tAcReceivable.setAcctCode(acctCode);
		tAcReceivable.setCustNo(wkCustNo);
		tAcReceivable.setFacmNo(wkFacmNo);
		tAcReceivable.setRvNo(parse.IntegerToString(wkBormNo, 3));
		tAcReceivable.setRvAmt(shortAmt);
		lAcReceivable.add(tAcReceivable);
	}

	// 業績處理
	private void PfDetailRoutine() throws LogicException {
		this.info("PfDetailRoutine ...");

		if (iExtraRepay.compareTo(BigDecimal.ZERO) == 0) { // 1: 部分償還
			return;
		}
		PfDetailVo pf = new PfDetailVo();
		pf.setCustNo(wkCustNo); // 借款人戶號
		pf.setFacmNo(wkFacmNo); // 額度編號
		pf.setBormNo(wkBormNo); // 撥款序號
		pf.setBorxNo(wkBorxNo); // 交易內容檔序號
		pf.setPieceCode(tLoanBorMain.getPieceCode()); // 計件代碼
		pf.setRepayType(2); // 還款類別 0.撥款 2.部分償還 3.提前結案
		pf.setDrawdownAmt(wkExtraRepay);// 撥款金額/追回金額
		pf.setDrawdownDate(tLoanBorMain.getDrawdownDate());// 撥款日期
		pf.setRepaidPeriod(tLoanBorMain.getRepaidPeriod()); // 已攤還期數
		// 產生業績明細
		pfDetailCom.setTxBuffer(this.getTxBuffer());
		pfDetailCom.addDetail(pf, titaVo);
	}

	// 兌現票入帳處理
	private void loanChequeRoutine() throws LogicException {
		this.info("LoanCheckRoutine ...");
		this.info("   iRpCode = " + iRpCode);

		if (iRpCode == 4) {
			acPaymentCom.setTxBuffer(this.getTxBuffer());
			acPaymentCom.loanCheque(titaVo);
		}
	}

	// 約定還本檔處理
	private void loanBookRoutine() throws LogicException {
		this.info("loanBookRoutine ...");
		Slice<LoanBook> slLoanBook = loanBookService.bookCustNoRange(iCustNo, iCustNo, iFacmNo,
				iFacmNo > 0 ? iFacmNo : 999, iBormNo, iBormNo > 0 ? iBormNo : 900, iEntryDate, this.index,
				Integer.MAX_VALUE, titaVo);
		if (slLoanBook == null) {
			return;
		}
		for (LoanBook t : slLoanBook.getContent()) {
			if (titaVo.isHcodeNormal()) {
				if (t.getStatus() == 0) {
					updateLoanBook(t);
					break;
				}
			} else {
				if (t.getStatus() == 1 && t.getActualDate() == wkTbsDy) {
					updateLoanBook(t);
					break;
				}
			}
		}
	}

	// 約定還本檔更新
	private void updateLoanBook(LoanBook t) throws LogicException {
		LoanBook tLoanBook = loanBookService.holdById(t, titaVo);
		if (titaVo.isHcodeNormal()) {
			tLoanBook.setStatus(1); // 1: 已回收
			tLoanBook.setActualDate(wkTbsDy);
			tLoanBook.setRepayAmt(tLoanBook.getRepayAmt().add(iExtraRepay));// 實際還本金額
		} else {
			tLoanBook.setStatus(0); // 0: 未回收
			tLoanBook.setActualDate(0);
			tLoanBook.setRepayAmt(BigDecimal.ZERO);// 實際還本金額
		}
		try {
			loanBookService.update(tLoanBook, titaVo);
		} catch (DBException e) {
			throw new LogicException(titaVo, "E0007", "放款約定還本檔 " + e.getErrorMsg()); // 更新資料時，發生錯誤
		}
	}

	// 疑似洗錢交易訪談記錄檔處理
	private void mlaundryRecordRoutine() throws LogicException {
		this.info("mlaundryRecordRoutine ...");
		Slice<MlaundryRecord> slMlaundryRecord = mlaundryRecordService.findCustNoEq(iCustNo, iFacmNo,
				iFacmNo > 0 ? iFacmNo : 999, iBormNo, iBormNo > 0 ? iBormNo : 900, iEntryDate, this.index,
				Integer.MAX_VALUE, titaVo);
		if (slMlaundryRecord == null) {
			return;
		}
		for (MlaundryRecord t : slMlaundryRecord.getContent()) {
			if (titaVo.isHcodeNormal()) {
				if (t.getActualRepayDate() == 0) {
					updateMlaundryRecord(t);
					break;
				}
			} else {
				if (t.getActualRepayDate() == wkTbsDy) {
					updateMlaundryRecord(t);
					break;
				}
			}
		}
	}

	// 疑似洗錢交易訪談記錄檔更新
	private void updateMlaundryRecord(MlaundryRecord t) throws LogicException {
		MlaundryRecord tMlaundryRecord = mlaundryRecordService.holdById(t, titaVo);
		if (titaVo.isHcodeNormal()) {
			tMlaundryRecord.setActualRepayDate(iEntryDate);
			tMlaundryRecord.setActualRepayAmt(iExtraRepay);// 實際還本金額
		} else {
			tMlaundryRecord.setActualRepayDate(0);
			tMlaundryRecord.setActualRepayAmt(BigDecimal.ZERO);// 實際還本金額
		}
		try {
			mlaundryRecordService.update(tMlaundryRecord, titaVo);
		} catch (DBException e) {
			throw new LogicException(titaVo, "E0007", "疑似洗錢交易訪談記錄檔 " + e.getErrorMsg()); // 更新資料時，發生錯誤
		}
	}

	// 計算暫收款金額
	private void compTempAmt() throws LogicException {
		// 還款總金額
		BigDecimal wkAcTotal = BigDecimal.ZERO;
		for (AcDetail ac : lAcDetail) {
			if ("C".equals(ac.getDbCr())) {
				wkAcTotal = wkAcTotal.add(ac.getTxAmt());
			} else {
				wkAcTotal = wkAcTotal.subtract((ac.getTxAmt()));
			}
		}
		// 本筆還款金額 、累計還款總金額
		BigDecimal wkRepayAmt = wkAcTotal.subtract(this.wkTotalRepay);
		this.wkTotalRepay = wkAcTotal;

		// 本筆暫收款金額
		this.wkTempAmt = BigDecimal.ZERO;

		if (iOverAmt.compareTo(BigDecimal.ZERO) == 0 && iTmpAmt.compareTo(BigDecimal.ZERO) == 0) {
			return;
		}

		// 有溢繳款放第一筆(暫收款金額為正值)
		if (iOverAmt.compareTo(BigDecimal.ZERO) > 0) {
			if (isFirstBorm) {
				this.wkTempAmt = iOverAmt;
			}
			return;
		}

		// 暫收抵繳金額(暫收款金額為負值) = 交易還款餘額 - 本筆還款金額
		if (wkRepayAmt.compareTo(this.wkTxAmtRemaind) < 0) {
			wkTempAmt = BigDecimal.ZERO;
			this.wkTxAmtRemaind = this.wkTxAmtRemaind.subtract(wkRepayAmt);
		} else {
			wkTempAmt = this.wkTxAmtRemaind.subtract(wkRepayAmt);
			this.wkTxAmtRemaind = BigDecimal.ZERO;
		}
	}

}