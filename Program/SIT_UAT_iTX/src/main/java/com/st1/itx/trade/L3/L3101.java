package com.st1.itx.trade.L3;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

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
import com.st1.itx.db.domain.AcDetail;
import com.st1.itx.db.domain.CdBaseRate;
import com.st1.itx.db.domain.FacMain;
import com.st1.itx.db.domain.FacMainId;
import com.st1.itx.db.domain.FacProd;
import com.st1.itx.db.domain.LoanBorMain;
import com.st1.itx.db.domain.LoanBorMainId;
import com.st1.itx.db.domain.LoanBorTx;
import com.st1.itx.db.domain.LoanBorTxId;
import com.st1.itx.db.domain.LoanIntDetail;
import com.st1.itx.db.domain.LoanIntDetailId;
import com.st1.itx.db.domain.LoanRateChange;
import com.st1.itx.db.domain.LoanRateChangeId;
import com.st1.itx.db.domain.TxTemp;
import com.st1.itx.db.domain.TxTempId;
import com.st1.itx.db.service.CdBaseRateService;
import com.st1.itx.db.service.FacMainService;
import com.st1.itx.db.service.FacProdService;
import com.st1.itx.db.service.FacProdStepRateService;
import com.st1.itx.db.service.LoanBorMainService;
import com.st1.itx.db.service.LoanBorTxService;
import com.st1.itx.db.service.LoanIntDetailService;
import com.st1.itx.db.service.LoanRateChangeService;
import com.st1.itx.db.service.TxTempService;
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.common.AcDetailCom;
import com.st1.itx.util.common.AcPaymentCom;
import com.st1.itx.util.common.AuthLogCom;
import com.st1.itx.util.common.LoanCalcRepayIntCom;
import com.st1.itx.util.common.LoanCom;
import com.st1.itx.util.common.LoanDueAmtCom;
import com.st1.itx.util.common.LoanSetRepayIntCom;
import com.st1.itx.util.common.PfDetailCom;
import com.st1.itx.util.common.data.CalcRepayIntVo;
import com.st1.itx.util.common.data.PfDetailVo;
import com.st1.itx.util.date.DateUtil;
import com.st1.itx.util.format.FormatUtil;
import com.st1.itx.util.parse.Parse;

/*
 * L3100 撥款收息
 * a.此功能供逆向抵押貸款首撥後續的撥款收息
 * b.一律為整批匯款
 * c.此交易為2段式交易
 */
/*
 * Tita
 * TimCustNo=9,7
 * FacmNo=9,3
 * BormNo=9,3            
 * DrawdownDate=9,7       
 * CurrencyCode=X,3
 * PaidTerms=9,3
 * TOTALPERIOD=9,3
 * TimDrawdownAmt=9,14.2
 * TimSumLoanBal=9,14.2
 * TimInterest=9,14.2
 * TimOpenInterest=9,14.2
 * TimSumOpenInterest=9,14.2
 * TimRealPayAmt=9,14.2
 
 */
/**
 * L3101 撥款收息
 * 
 * @author iza
 * @version 1.0.0
 */
@Service("L3101")
@Scope("prototype")
public class L3101 extends TradeBuffer {
	private static final Logger logger = LoggerFactory.getLogger(L3101.class);

	/* DB服務注入 */
	@Autowired
	public CdBaseRateService cdBaseRateService;
	@Autowired
	public FacProdService facProdService;
	@Autowired
	public FacProdStepRateService facProdStepRateService;
	@Autowired
	public FacMainService facMainService;
	@Autowired
	public LoanBorMainService loanBorMainService;
	@Autowired
	public LoanBorTxService loanBorTxService;
	@Autowired
	public LoanRateChangeService loanRateChangeService;
	@Autowired
	public LoanIntDetailService loanIntDetailService;
	@Autowired
	public TxTempService txTempService;

	@Autowired
	Parse parse;
	@Autowired
	DateUtil dDateUtil;
	@Autowired
	AcDetailCom acDetailCom;
	@Autowired
	LoanDueAmtCom loanDueAmtCom;
	@Autowired
	LoanSetRepayIntCom loanSetRepayIntCom;
	@Autowired
	LoanCalcRepayIntCom loanCalcRepayIntCom;
	@Autowired
	LoanCom loanCom;
	@Autowired
	AcPaymentCom acPaymentCom;
	@Autowired
	public PfDetailCom pfDetailCom;
	@Autowired
	public AuthLogCom authLogCom;

	private TempVo tempVo = new TempVo();

	private TitaVo titaVo = new TitaVo();
	private int iCustNo;
	private int iFacmNo;
	private int iBormNo;
	private int iDrawdownDate;
	private BigDecimal iDrawdownAmt;
	private BigDecimal iInterest;
	private BigDecimal iOpenInterest;
	private int iPaidTerms;
	private int iTotalPeriod;

	// work area
	private int wkBorxNo = 1;
	private int wkNewBorxNo;
	private int wkTbsDy = 0;
	private int wkBormNo = 0;
	private int wkIntStartDate = 9991231;
	private int wkIntEndDate = 0;
	private int wkDueDate = 0;
	private int wkRepaidPeriod = 0;
	private int wkPaidTerms = 0;
	private BigDecimal wkTotalInterest = BigDecimal.ZERO;
	private BigDecimal wkUtilAmt = BigDecimal.ZERO;
	private TempVo tTempVo = new TempVo();
	private FacProd tFacProd;
	private FacMain tFacMain;
	private LoanBorMain tLoanBorMain;
	private LoanBorMain t1LoanBorMain;
	private LoanBorTx tLoanBorTx;
	private LoanBorTxId tLoanBorTxId;
	private LoanRateChange tLoanRateChange;
	private LoanRateChangeId tLoanRateChangeId;
	private TxTemp tTxTemp;
	private TxTempId tTxTempId;
	private AcDetail acDetail;
	private List<LoanBorMain> lLoanBorMain;
	private List<TxTemp> lTxTemp;

	private ArrayList<CalcRepayIntVo> lCalcRepayIntVo;

	private DecimalFormat df = new DecimalFormat("##,###,###,###,##0");

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active L3100 ");
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
		loanCom.setTxBuffer(this.txBuffer);
		loanSetRepayIntCom.setTxBuffer(this.getTxBuffer());
		acDetailCom.setTxBuffer(this.getTxBuffer());
		acPaymentCom.setTxBuffer(this.getTxBuffer());
		pfDetailCom.setTxBuffer(this.getTxBuffer());

		wkTbsDy = this.txBuffer.getTxCom().getTbsdy();

		// 取得輸入資料
		iCustNo = this.parse.stringToInteger(titaVo.getParam("TimCustNo"));
		iFacmNo = this.parse.stringToInteger(titaVo.getParam("FacmNo"));
		iBormNo = this.parse.stringToInteger(titaVo.getParam("BormNo"));
		iDrawdownAmt = this.parse.stringToBigDecimal(titaVo.getParam("TimDrawdownAmt"));
		iDrawdownDate = this.parse.stringToInteger(titaVo.getParam("DrawdownDate"));
		iPaidTerms = this.parse.stringToInteger(titaVo.getParam("PaidTerms"));
		iTotalPeriod = this.parse.stringToInteger(titaVo.getParam("TotalPeriod"));
		iInterest = this.parse.stringToBigDecimal(titaVo.getParam("TimInterest"));
		iOpenInterest = this.parse.stringToBigDecimal(titaVo.getParam("TimOpenInterest"));

		// 交易檢核
		txCheckRoutine();

		// 額度檔
		tFacMain = facMainService.findById(new FacMainId(iCustNo, iFacmNo));
		if (tFacMain == null) {
			throw new LogicException(titaVo, "E0001", "額度主檔 戶號 = " + iCustNo + " 額度編號 = " + iFacmNo); // 查詢資料不存在
		}

		// 查詢商品參數檔
		tFacProd = facProdService.findById(tFacMain.getProdNo());
		if (tFacProd == null) {
			throw new LogicException(titaVo, "E0001", "商品參數檔"); // 查詢資料不存在
		}

		// 首筆撥款檔
		t1LoanBorMain = loanBorMainService.holdById(new LoanBorMainId(iCustNo, iFacmNo, 1));
		if (t1LoanBorMain == null) {
			throw new LogicException(titaVo, "E0001", "首筆撥款檔"); // 查詢資料不存在
		}

		// 收息處理，經辦登錄、訂正
		if (titaVo.isActfgEntry()) {
			if (titaVo.isHcodeNormal()) {
				RepayNormalRoutine();
			}
			if (titaVo.isHcodeErase()) {
				RepayEraseRoutine();
			}
		}

		// 維護額度主檔
		facMainRoutine();

		if (iDrawdownAmt.compareTo(BigDecimal.ZERO) > 0) {

			wkBorxNo = 1;

			// 維護撥款檔
			LoanBorMainRoutine();

			// 維護放款利率變動檔, 核准利率
			LoanRateChangeRoutine();

			// 維護放款交易內容檔
			LoanBorTxRoutine();

			// 維護撥款匯款檔
			AcPaymentRoutine();

			// 業績處理
			PfDetailRoutine();
		}

		// 帳務處理
		AcDetailRoutine();

		this.addList(this.totaVo);
		return this.sendList();
	}

	// -------------------------------------------------------------------------------

	private void txCheckRoutine() throws LogicException {
		// 不可跨日訂正
		if (titaVo.isHcodeErase()) {
			if (titaVo.getOrgEntdyI() != titaVo.getEntDyI())
				throw new LogicException(titaVo, "E0010", "不可跨日訂正，原交易日= " + titaVo.getOrgEntdyI()); // E0010 功能選擇錯誤
		}
	}

	private void facMainRoutine() throws LogicException {
		this.info("   facMainRoutine ...");

		BigDecimal wkAvailableAmt = new BigDecimal(0);
		BigDecimal wkDrawdownAmt = new BigDecimal(0);

		// 主管放行不更新額度檔
		if (titaVo.isActfgSuprele()) {
			return;
		}
		// 撥款金額為0不更新額度檔
		if (iDrawdownAmt.compareTo(BigDecimal.ZERO) == 0) {
			return;
		}
		// 鎖定額度檔
		if (tFacMain.getActFg() == 1) {
			throw new LogicException(titaVo, "E0021",
					"額度檔 戶號 = " + tFacMain.getCustNo() + " 額度編號 =  " + tFacMain.getFacmNo()); // 該筆資料待放行中
		}
		if (titaVo.isHcodeNormal()) {
			// 檢查額度更新額度主檔
			wkAvailableAmt = tFacMain.getLineAmt().subtract(tFacMain.getUtilBal());
			if (wkAvailableAmt.compareTo(iDrawdownAmt) < 0) {
				throw new LogicException(titaVo, "E3051", "可用額度=" + wkAvailableAmt); // 額度不足撥款金額
			}
			if (tFacMain.getUtilDeadline() < iDrawdownDate) {
				throw new LogicException(titaVo, "E3052", "動支期限=" + tFacMain.getUtilDeadline()); // 已超過動支期限

			}
			if (tFacMain.getRecycleCode().equals("1") && tFacMain.getRecycleDeadline() < iDrawdownDate) {
				throw new LogicException(titaVo, "E3053", "循環動用期限=" + tFacMain.getRecycleDeadline()); // 已超過循環動用期限
			}
			if (!tFacMain.getL9110Flag().equals("Y")) {
				throw new LogicException(titaVo, "E3083", "額度主檔 戶號 = " + iCustNo + " 額度編號 = " + iFacmNo); // 撥款審核資料表尚未列印，請先作L9110交易
			}
			if (tFacMain.getFirstDrawdownDate() > 0 && iDrawdownDate < tFacMain.getFirstDrawdownDate()) {
				throw new LogicException(titaVo, "E0019", "撥款日期須 >= " + tFacMain.getFirstDrawdownDate()); // 輸入資料錯誤
			}
			if (iBormNo != (tFacMain.getLastBormNo() + 1)) {
				throw new LogicException(titaVo, "E0019", "撥款序號不符 " + iBormNo + "/" + (tFacMain.getLastBormNo() + 1)); // 輸入資料錯誤
			}
			wkUtilAmt = tFacMain.getUtilAmt();
			tFacMain.setUtilAmt(tFacMain.getUtilAmt().add(iDrawdownAmt));
			tFacMain.setUtilBal(tFacMain.getUtilBal().add(iDrawdownAmt));

			// 新增交易暫存檔
			tTxTemp = new TxTemp();
			tTxTempId = new TxTempId();
			loanCom.setTxTemp(tTxTempId, tTxTemp, iCustNo, iFacmNo, 0, 0, titaVo);
			tTempVo.clear();
			tTempVo.putParam("DrawdownAmt", iDrawdownAmt);
			tTxTemp.setText(tTempVo.getJsonString());
			try {
				txTempService.insert(tTxTemp);
			} catch (DBException e) {
				throw new LogicException(titaVo, "E0005", "交易暫存檔 Key = " + tTxTempId); // 新增資料時，發生錯誤
			}
		}
		// 更新已撥款序號
		if (iDrawdownAmt.compareTo(BigDecimal.ZERO) > 0) {
			if (titaVo.isHcodeNormal())
				tFacMain.setLastBormNo(iBormNo);
			else if (titaVo.isHcodeErase()) {
				if (iBormNo != tFacMain.getLastBormNo())
					throw new LogicException(titaVo, "E3088", "需先訂正，撥款依序 = " + tFacMain.getLastBormNo()); // 放款交易訂正須由最近一筆交易開始訂正
				else
					tFacMain.setLastBormNo(iBormNo - 1);
			}
		}
		if (titaVo.isHcodeErase() || titaVo.isHcodeModify()) {
			// 查詢交易暫存檔
			String wkSeqNo = FormatUtil.pad9(String.valueOf(iCustNo), 7) + FormatUtil.pad9(String.valueOf(iFacmNo), 3)
					+ "000000";
			tTxTemp = txTempService.findById(new TxTempId(titaVo.getOrgEntdyI() + 19110000, titaVo.getOrgKin(),
					titaVo.getOrgTlr(), titaVo.getOrgTno(), wkSeqNo));
			if (tTxTemp == null) {
				throw new LogicException(titaVo, "E0001", "交易暫存檔"); // 查詢資料不存在
			}
			tTempVo = tTempVo.getVo(tTxTemp.getText());
			wkDrawdownAmt = this.parse.stringToBigDecimal(tTempVo.get("DrawdownAmt"));
			tFacMain.setUtilAmt(tFacMain.getUtilAmt().subtract(wkDrawdownAmt));
			tFacMain.setUtilBal(tFacMain.getUtilBal().subtract(wkDrawdownAmt));
		}

		if (titaVo.isHcodeModify()) {
			// 新增交易暫存檔
			tTxTemp = new TxTemp();
			tTxTempId = new TxTempId();
			loanCom.setTxTemp(tTxTempId, tTxTemp, iCustNo, iFacmNo, 0, 0, titaVo);
			tTempVo.clear();
			tTempVo.putParam("DrawdownAmt", iDrawdownAmt);
			tTxTemp.setText(tTempVo.getJsonString());
			try {
				txTempService.insert(tTxTemp);
			} catch (DBException e) {
				throw new LogicException(titaVo, "E0007", "交易暫存檔"); // 更新資料時，發生錯誤
			}
			// 準備更新額度資料
			tFacMain.setUtilAmt(tFacMain.getUtilAmt().add(iDrawdownAmt));
			tFacMain.setUtilBal(tFacMain.getUtilBal().add(iDrawdownAmt));
		}

		// 更新額度檔
		try {
			facMainService.update(tFacMain);
		} catch (DBException e) {
			throw new LogicException(titaVo, "E0007", "額度主檔"); // 更新資料時，發生錯誤
		}
	}

	private void LoanBorMainRoutine() throws LogicException {
		this.info("LoanBorMainRoutine ...");
		this.info("   tFacMain.getUtilAmt()  = " + tFacMain.getUtilAmt());
		this.info("   wkUtilAmt = " + wkUtilAmt);

		// 登錄
		if (titaVo.isActfgEntry() && titaVo.isHcodeNormal()) {
//			if (tFacProd.getCharCode().equals("2") && wkUtilAmt.compareTo(BigDecimal.ZERO) == 0) {
//				throw new LogicException(titaVo, "E3099", "額度主檔 借款人戶號 = " + tFacMain.getCustNo() + " 額度編號 = "
//						+ tFacMain.getFacmNo() + " 已動用額度 = " + df.format(wkUtilAmt)); // 該筆額度(以房養老)已撥款
//			}
			// 新增撥款檔
			tLoanBorMain = new LoanBorMain();
			moveLoanBorMain(t1LoanBorMain);
			try {
				loanBorMainService.insert(tLoanBorMain);
			} catch (DBException e) {
				throw new LogicException(titaVo, "E3009", "撥款檔 " + e.getErrorMsg()); // 新增資料時，發生錯誤
			}
		}
		// 登錄訂正
		if (titaVo.isActfgEntry() && titaVo.isHcodeErase()) {
			// 刪除撥款檔
			tLoanBorMain = loanBorMainService.holdById(new LoanBorMainId(iCustNo, iFacmNo, iBormNo));
			if (tLoanBorMain == null) {
				throw new LogicException(titaVo, "E0006", "撥款主檔"); // 鎖定資料時，發生錯誤
			}
			// 放款交易訂正交易須由最後一筆交易開始訂正
			loanCom.checkEraseBormTxSeqNo(tLoanBorMain, titaVo);
			try {
				loanBorMainService.delete(tLoanBorMain);
			} catch (DBException e) {
				throw new LogicException(titaVo, "E0008", "撥款主檔 " + e.getErrorMsg()); // 新增資料時，發生錯誤
			}
		}
		// 登錄修正
		if (titaVo.isActfgEntry() && titaVo.isHcodeModify()) {
			// 修改撥款檔
			tLoanBorMain = loanBorMainService.holdById(new LoanBorMainId(iCustNo, iFacmNo, iBormNo));
			if (tLoanBorMain == null) {
				throw new LogicException(titaVo, "E0006", "撥款主檔"); // 鎖定資料時，發生錯誤
			}
			tLoanBorMain = new LoanBorMain();
			moveLoanBorMain(t1LoanBorMain);
			try {
				loanBorMainService.update(tLoanBorMain);
			} catch (DBException e) {
				throw new LogicException(titaVo, "E0008", "撥款檔 " + e.getErrorMsg()); // 新增資料時，發生錯誤
			}
		}
		// 放行
		if (titaVo.isActfgSuprele() && titaVo.isHcodeNormal()) {
			tLoanBorMain = loanBorMainService.holdById(new LoanBorMainId(iCustNo, iFacmNo, iBormNo));
			if (tLoanBorMain == null) {
				throw new LogicException(titaVo, "E0006", "撥款主檔"); // 鎖定資料時，發生錯誤
			}
			if (tLoanBorMain.getActFg() != 1) {
				throw new LogicException(titaVo, "E0017",
						"撥款主檔 戶號 = " + iCustNo + "額度編號 = " + iFacmNo + "撥款序號 = " + iBormNo); // 該筆交易狀態非待放行，不可做交易放行
			}
			// 新增交易暫存檔
			tTxTemp = new TxTemp();
			tTxTempId = new TxTempId();
			loanCom.setTxTemp(tTxTempId, tTxTemp, iCustNo, iFacmNo, iBormNo, 0, titaVo);
			tTempVo.clear();
			tTempVo.putParam("ActFg", tLoanBorMain.getActFg());
			tTempVo.putParam("LastEntDy", tLoanBorMain.getLastEntDy());
			tTempVo.putParam("LastKinbr", tLoanBorMain.getLastKinbr());
			tTempVo.putParam("LastTlrNo", tLoanBorMain.getLastTlrNo());
			tTempVo.putParam("LastTxtNo", tLoanBorMain.getLastTxtNo());
			tTxTemp.setText(tTempVo.getJsonString());
			try {
				txTempService.insert(tTxTemp);
			} catch (DBException e) {
				throw new LogicException(titaVo, "E0005", "交易暫存檔 Key = " + tTxTempId); // 新增資料時，發生錯誤
			}
			// 更新撥款主檔
			tLoanBorMain.setActFg(titaVo.getActFgI());
			tLoanBorMain.setLastEntDy(titaVo.getEntDyI());
			tLoanBorMain.setLastKinbr(titaVo.getKinbr());
			tLoanBorMain.setLastTlrNo(titaVo.getTlrNo());
			tLoanBorMain.setLastTxtNo(titaVo.getTxtNo());
			try {
				loanBorMainService.update(tLoanBorMain);
			} catch (DBException e) {
				throw new LogicException(titaVo, "E0008", "撥款主檔 " + e.getErrorMsg()); // 新增資料時，發生錯誤
			}
		}
		// 放行訂正
		if (titaVo.isActfgSuprele() && titaVo.isHcodeErase()) {
			tLoanBorMain = loanBorMainService.holdById(new LoanBorMainId(iCustNo, iFacmNo, iBormNo));
			if (tLoanBorMain == null) {
				throw new LogicException(titaVo, "E0006",
						"撥款主檔 戶號 = " + iCustNo + "額度編號 = " + iFacmNo + "撥款序號 = " + iBormNo); // 鎖定資料時，發生錯誤
			}
			// 放款交易訂正交易須由最後一筆交易開始訂正
			loanCom.checkEraseBormTxSeqNo(tLoanBorMain, titaVo);
			// 查詢交易暫存檔
			String wkSeqNo = FormatUtil.pad9(String.valueOf(iCustNo), 7) + FormatUtil.pad9(String.valueOf(iFacmNo), 3)
					+ FormatUtil.pad9(String.valueOf(iBormNo), 3) + "000";
			tTxTemp = txTempService.findById(new TxTempId(titaVo.getOrgEntdyI() + 19110000, titaVo.getOrgKin(),
					titaVo.getOrgTlr(), titaVo.getOrgTno(), wkSeqNo));
			if (tTxTemp == null) {
				throw new LogicException(titaVo, "E0001", "交易暫存檔"); // 查詢資料不存在
			}
			tTempVo = tTempVo.getVo(tTxTemp.getText());
			tLoanBorMain.setActFg(this.parse.stringToInteger(tTempVo.get("ActFg")));
			tLoanBorMain.setLastEntDy(this.parse.stringToInteger(tTempVo.get("LastEntDy")));
			tLoanBorMain.setLastKinbr(tTempVo.get("LastKinbr"));
			tLoanBorMain.setLastTlrNo(tTempVo.get("LastTlrNo"));
			tLoanBorMain.setLastTxtNo(tTempVo.get("LastTxtNo"));
			try {
				loanBorMainService.update(tLoanBorMain);
			} catch (DBException e) {
				throw new LogicException(titaVo, "E0008",
						"撥款主檔 戶號 = " + iCustNo + "額度編號 = " + iFacmNo + "撥款序號 = " + iBormNo + e.getErrorMsg()); // 新增資料時，發生錯誤
			}
		}
	}

	private void LoanRateChangeRoutine() throws LogicException {
		this.info("LoanRateChange1Routine ...");

		if (titaVo.isActfgSuprele()) {
			return;
		}
		if (titaVo.isHcodeErase() || titaVo.isHcodeModify()) {
			// 放款利率變動檔
			Slice<LoanRateChange> slLoanRateChange = loanRateChangeService.rateChangeTxtNoEq(
					titaVo.getOrgEntdyI() + 19110000, titaVo.getOrgTlr(), titaVo.getOrgTno(), 0, Integer.MAX_VALUE);
			List<LoanRateChange> lLoanRateChange = slLoanRateChange == null ? null : slLoanRateChange.getContent();
			if (lLoanRateChange != null && lLoanRateChange.size() > 0) {
				try {
					loanRateChangeService.deleteAll(lLoanRateChange);
				} catch (DBException e) {
					throw new LogicException(titaVo, "E0008", "放款利率變動檔 " + e.getErrorMsg()); // 刪除資料時，發生錯誤
				}
			}
		}

		if (titaVo.isHcodeNormal() || titaVo.isHcodeModify()) {
			// 新增放款利率變動檔, 核准利率
			tLoanRateChange = new LoanRateChange();
			tLoanRateChangeId = new LoanRateChangeId();
			tLoanRateChangeId.setCustNo(iCustNo);
			tLoanRateChangeId.setFacmNo(iFacmNo);
			tLoanRateChangeId.setBormNo(iBormNo);
			tLoanRateChangeId.setEffectDate(iDrawdownDate);
			SetLoanRateChange();
			try {
				loanRateChangeService.insert(tLoanRateChange);
			} catch (DBException e) {
				throw new LogicException(titaVo, "E0005", "放款利率變動檔 " + e.getErrorMsg()); // 新增資料時，發生錯誤
			}
		}
	}

	private void LoanBorTxRoutine() throws LogicException {
		this.info("   LoanBorTxRoutine ...");

		if (titaVo.isActfgSuprele()) {
			return;
		}
		if (titaVo.isHcodeErase() || titaVo.isHcodeModify()) {
			// 刪除放款交易內容檔
			tLoanBorTx = loanBorTxService.holdById(new LoanBorTxId(iCustNo, iFacmNo, iBormNo, wkBorxNo));
			if (tLoanBorTx == null) {
				throw new LogicException(titaVo, "E0006", "放款交易內容檔"); // 鎖定資料時，發生錯誤
			}
			try {
				loanBorTxService.delete(tLoanBorTx);
			} catch (DBException e) {
				throw new LogicException(titaVo, "E0008", "放款交易內容檔 " + e.getErrorMsg()); // 刪除資料時，發生錯誤
			}
		}

		if (titaVo.isHcodeNormal() || titaVo.isHcodeModify()) {
			// 新增放款交易內容檔
			tLoanBorTx = new LoanBorTx();
			tLoanBorTxId = new LoanBorTxId();
			loanCom.setLoanBorTx(tLoanBorTx, tLoanBorTxId, iCustNo, iFacmNo, iBormNo, wkBorxNo, titaVo);
			moveLoanBorTx();
			try {
				loanBorTxService.insert(tLoanBorTx);
			} catch (DBException e) {
				throw new LogicException(titaVo, "E0005", "放款交易內容檔 " + e.getErrorMsg()); // 新增資料時，發生錯誤
			}
		}
	}

	private void AcDetailRoutine() throws LogicException {
		this.info("   AcDetailRoutine ...");

		if (this.txBuffer.getTxCom().isBookAcYes()) {
			List<AcDetail> lAcDetail = new ArrayList<AcDetail>();
			// 借方 撥貸金額
			acDetail = new AcDetail();
			acDetail.setDbCr("D");
			acDetail.setAcctCode(tFacMain.getAcctCode());
			acDetail.setTxAmt(iDrawdownAmt);
			acDetail.setCustNo(iCustNo);
			acDetail.setFacmNo(iFacmNo);
			acDetail.setBormNo(iBormNo);
			lAcDetail.add(acDetail);
			// 借方 掛帳利息
			acDetail = new AcDetail();
			acDetail.setDbCr("D");
			acDetail.setAcctCode("ICR"); // ICR 應收利息－放款部
			acDetail.setTxAmt(iOpenInterest);
			acDetail.setCustNo(iCustNo);
			acDetail.setFacmNo(iFacmNo);
			acDetail.setBormNo(0);
			lAcDetail.add(acDetail);
			// 貸方 利息
			acDetail = new AcDetail();
			acDetail.setDbCr("C");
			acDetail.setAcctCode(loanCom.setIntAcctCode(tFacMain.getAcctCode()));
			acDetail.setTxAmt(iInterest);
			acDetail.setCustNo(iCustNo);
			acDetail.setFacmNo(iFacmNo);
			acDetail.setBormNo(0);
			lAcDetail.add(acDetail);

			this.txBuffer.addAllAcDetailList(lAcDetail);

			// 貸方 收付欄
			acPaymentCom.setTxBuffer(this.getTxBuffer());
			acPaymentCom.run(titaVo);

			// 產生會計分錄
			acDetailCom.setTxBuffer(this.txBuffer);
			acDetailCom.run(titaVo);
			this.setTxBuffer(acDetailCom.getTxBuffer());
		}
	}

	// 維護撥款匯款檔
	private void AcPaymentRoutine() throws LogicException {
		this.info("   AcPaymentRoutine ...");
		this.info("   isActfgEntry = " + titaVo.isActfgEntry());

		if (titaVo.isActfgEntry()) {
			acPaymentCom.setTxBuffer(this.getTxBuffer());
			acPaymentCom.remit(titaVo);
		}
	}

	// 業績處理
	private void PfDetailRoutine() throws LogicException {
		this.info("   PfDetailRoutine ...");

		PfDetailVo pf = new PfDetailVo();
		pf.setCustNo(iCustNo); // 借款人戶號
		pf.setFacmNo(iFacmNo); // 額度編號
		pf.setBormNo(iBormNo); // 撥款序號
		pf.setRepayType(0); // 還款類別 0.撥款 2.部分償還 3.提前結案
		pf.setRepaidPeriod(0); // 已攤還期數
		pf.setPieceCode(tLoanBorMain.getPieceCode()); // 計件代碼
		pf.setDrawdownDate(tLoanBorMain.getDrawdownDate());// 撥款日期
		pf.setDrawdownAmt(tLoanBorMain.getDrawdownAmt());// 撥款金額/追回金額
		pfDetailCom.setTxBuffer(this.getTxBuffer());
		pfDetailCom.addDetail(pf, titaVo); // 產生業績明細
	}

	// -------------------------------------------------------------------------------
	private void moveLoanBorMain(LoanBorMain ln) throws LogicException {
		this.info("   moveLoanBorMain ...");

		tLoanBorMain.setCustNo(iCustNo);
		tLoanBorMain.setFacmNo(iFacmNo);
		tLoanBorMain.setBormNo(iBormNo);
		tLoanBorMain.setLoanBorMainId(new LoanBorMainId(iCustNo, iFacmNo, iBormNo));
		tLoanBorMain.setLastBorxNo(wkBorxNo);
		tLoanBorMain.setLastOvduNo(0);
		tLoanBorMain.setStatus(0);
		tLoanBorMain.setRateIncr(tFacMain.getRateIncr());
		tLoanBorMain.setIndividualIncr(tFacMain.getIndividualIncr());
		CdBaseRate tCdBaseRate = cdBaseRateService.baseRateCodeDescFirst(titaVo.getParam("CurrencyCode"),
				tFacMain.getBaseRateCode(), 10101, iDrawdownDate + 19110000, titaVo);
		if (tCdBaseRate == null) {
			throw new LogicException(titaVo, "E0001", " 指標利率檔" + tFacMain.getBaseRateCode() + " ," + iDrawdownDate); // 查無資料
		}
		if (tFacProd.getIncrFlag().equals("Y")) {
			tLoanBorMain.setApproveRate(tCdBaseRate.getBaseRate().add(tFacMain.getRateIncr()));
		} else {
			tLoanBorMain.setApproveRate(tCdBaseRate.getBaseRate().add(tFacMain.getIndividualIncr()));
		}
		tempVo = authLogCom.exec(tFacMain.getCustNo(), tFacMain.getFacmNo(), titaVo);
		if (tempVo == null) {
			throw new LogicException(titaVo, "E0001", "L2R05 額度主檔 借款人戶號 = " + tFacMain.getCustNo() + " 額度編號 = " + tFacMain.getFacmNo()); // 查詢資料不存在
		}
		tLoanBorMain.setStoreRate(tFacMain.getApproveRate());
		tLoanBorMain.setRateCode(tFacMain.getRateCode());
		tLoanBorMain.setRateAdjFreq(tFacMain.getRateAdjFreq());
		tLoanBorMain.setDrawdownCode("1");
		tLoanBorMain.setCurrencyCode(titaVo.getParam("CurrencyCode"));
		tLoanBorMain.setDrawdownAmt(iDrawdownAmt);
		tLoanBorMain.setLoanBal(iDrawdownAmt);
		tLoanBorMain.setDrawdownDate(iDrawdownDate);
		tLoanBorMain.setLoanTermYy(0);
		tLoanBorMain.setLoanTermMm(0);
		tLoanBorMain.setLoanTermDd(0);
		tLoanBorMain.setMaturityDate(tFacMain.getMaturityDate());
		tLoanBorMain.setAmortizedCode(tFacMain.getAmortizedCode());
		tLoanBorMain.setFreqBase(this.parse.stringToInteger(tFacMain.getFreqBase()));
		tLoanBorMain.setPayIntFreq(tFacMain.getPayIntFreq());
		tLoanBorMain.setRepayFreq(tFacMain.getRepayFreq());
		tLoanBorMain.setTotalPeriod(iTotalPeriod - iPaidTerms + 1);
		tLoanBorMain.setRepaidPeriod(0);
		tLoanBorMain.setPaidTerms(0);
		tLoanBorMain.setPrevPayIntDate(0);
		tLoanBorMain.setPrevRepaidDate(0);
		tLoanBorMain.setNextPayIntDate(ln.getNextPayIntDate());
		tLoanBorMain.setNextRepayDate(ln.getNextRepayDate());
		tLoanBorMain.setGracePeriod(0);
		tLoanBorMain.setGraceDate(0);
		tLoanBorMain.setSpecificDd(ln.getSpecificDd());
		tLoanBorMain.setSpecificDate(ln.getPrevPayIntDate()); // 指定基準日期 = 首筆的繳息迄日
		tLoanBorMain.setFirstDueDate(ln.getNextPayIntDate()); // 首次應繳日 = 筆的下次繳息日
		tLoanBorMain.setFirstAdjRateDate(ln.getNextAdjRateDate());
		tLoanBorMain.setNextAdjRateDate(ln.getNextAdjRateDate());
		tLoanBorMain.setAcctFee(BigDecimal.ZERO);
		tLoanBorMain.setFinalBal(BigDecimal.ZERO);
		tLoanBorMain.setNotYetFlag("N");
		tLoanBorMain.setRenewFlag("N");
		tLoanBorMain.setPieceCode(ln.getPieceCode());
		tLoanBorMain.setUsageCode(ln.getUsageCode());
		tLoanBorMain.setSyndNo(ln.getSyndNo());
		tLoanBorMain.setRelationCode(tempVo.getParam("RelationCode"));
		tLoanBorMain.setRelationName(tempVo.getParam("RelationName"));
		tLoanBorMain.setRelationId(tempVo.getParam("RelationId"));
		tLoanBorMain.setRelationBirthday(parse.stringToInteger(tempVo.getParam("RelationBirthday")));
		tLoanBorMain.setRelationGender(tempVo.getParam("RelationGender"));
		tLoanBorMain.setActFg(titaVo.getActFgI());
		tLoanBorMain.setLastEntDy(titaVo.getEntDyI());
		tLoanBorMain.setLastKinbr(titaVo.getKinbr());
		tLoanBorMain.setLastTlrNo(titaVo.getTlrNo());
		tLoanBorMain.setLastTxtNo(titaVo.getTxtNo());
		tLoanBorMain.setRemitBank(titaVo.getParam("RpRemitBank1"));
		tLoanBorMain.setRemitBranch(titaVo.getParam("RpRemitBranch1"));
		tLoanBorMain.setRemitAcctNo(parse.stringToBigDecimal(titaVo.getParam("RpRemitAcctNo1")));
		tLoanBorMain.setCompensateAcct(titaVo.getParam("RpCustName1"));
		tLoanBorMain.setRemark(titaVo.getParam("RpRemark1"));
		tLoanBorMain.setAcDate(titaVo.getEntDyI());
		tLoanBorMain.setNextAcDate(0);
		tLoanBorMain.setDueAmt(ln.getDueAmt());
	}

	private void moveLoanBorTx() throws LogicException {
		this.info("   moveLoanBorTx ...");

		tLoanBorTx.setDesc("撥款收息－撥款");
		tLoanBorTx.setEntryDate(tLoanBorMain.getDrawdownDate());
		tLoanBorTx.setTxAmt(this.parse.stringToBigDecimal(titaVo.getTxAmt()));
		tLoanBorTx.setLoanBal(iDrawdownAmt);
		tLoanBorTx.setRate(tLoanBorMain.getStoreRate());
		tLoanBorTx.setDisplayflag("A");
		// 其他欄位
		tTempVo.clear();
		tTempVo.putParam("RemitBank", titaVo.getParam("RpRemitBank1") + titaVo.getParam("RpRemitBranch1"));
		tTempVo.putParam("RemitAcctNo", titaVo.getParam("RpRemitAcctNo1"));
		tLoanBorTx.setOtherFields(tTempVo.getJsonString());
	}

	private void SetLoanRateChange() throws LogicException {
		this.info("SetLoanRateChange ...");
		tLoanRateChange.setCustNo(iCustNo);
		tLoanRateChange.setFacmNo(iFacmNo);
		tLoanRateChange.setBormNo(iBormNo);
		tLoanRateChange.setEffectDate(iDrawdownDate);
		tLoanRateChange.setLoanRateChangeId(tLoanRateChangeId);
		tLoanRateChange.setStatus(0);
		tLoanRateChange.setRateCode(tFacMain.getRateCode());
		tLoanRateChange.setProdNo(tFacMain.getProdNo());
		tLoanRateChange.setBaseRateCode(tFacMain.getBaseRateCode());
		tLoanRateChange.setIncrFlag(tFacProd.getIncrFlag());
		if (tFacProd.getIncrFlag().equals("Y")) {
			tLoanRateChange.setRateIncr(tFacMain.getRateIncr());
			tLoanRateChange.setIndividualIncr(new BigDecimal(0));
			tLoanRateChange.setFitRate(tLoanBorMain.getApproveRate());
		} else {
			tLoanRateChange.setRateIncr(new BigDecimal(0));
			tLoanRateChange.setIndividualIncr(tFacMain.getIndividualIncr());
			tLoanRateChange.setFitRate(tLoanBorMain.getApproveRate());
		}
		tLoanRateChange.setFitRate(tLoanBorMain.getApproveRate());
		tLoanRateChange.setRemark("");
		tLoanRateChange.setAcDate(titaVo.getEntDyI());
		tLoanRateChange.setTellerNo(titaVo.getTlrNo());
		tLoanRateChange.setTxtNo(titaVo.getTxtNo());
	}

	// 收息正常處理
	private void RepayNormalRoutine() throws LogicException {

		int wkTotaCount = 0;
		wkTotalInterest = BigDecimal.ZERO;
		int wkTermNo = 0;
		int wkTerms = 0;
		int wkEndDate = 0;
		Slice<LoanBorMain> slLoanBorMain = loanBorMainService.bormCustNoEq(iCustNo, iFacmNo, iFacmNo, 1, 900, 0,
				Integer.MAX_VALUE);
		lLoanBorMain = slLoanBorMain == null ? null : new ArrayList<LoanBorMain>(slLoanBorMain.getContent());
		if (lLoanBorMain == null || lLoanBorMain.size() == 0) {
			throw new LogicException(titaVo, "E0001", "放款主檔"); // 查詢資料不存在
		}
		for (LoanBorMain ln : lLoanBorMain) {
			if (!(ln.getStatus() == 0 || ln.getStatus() == 4)) {
				continue;
			}
			wkBormNo = ln.getBormNo();
			wkBorxNo = ln.getLastBorxNo() + 1;
			wkIntStartDate = 9991231;
			wkIntEndDate = 0;
			wkDueDate = ln.getNextPayIntDate();

	//		wkTermNo = loanCom.getTermNo(2, ln.getFreqBase(), ln.getPayIntFreq(), ln.getSpecificDate(), iDrawdownDate);
			wkTerms = wkTermNo - ln.getPaidTerms();

			// 補撥款不檢查回收期數
			if (wkTerms <= 0) {
				if (tFacMain.getLastBormNo() <= t1LoanBorMain.getPaidTerms()) {
					return;
				} else {
					throw new LogicException(titaVo, "E3082",
							" 撥款序號 = " + ln.getBormNo() + ", 回收期數超過可收期數" + ln.getPaidTerms() + "/" + wkTermNo);
				}
			}
	//		wkEndDate = loanCom.getPayIntEndDate(ln.getFreqBase(), ln.getPayIntFreq(), ln.getSpecificDate(), wkTermNo,
	//				ln.getMaturityDate());

			loanCalcRepayIntCom = loanSetRepayIntCom.setRepayInt(ln, 0, wkEndDate, 2, iDrawdownDate, titaVo);
			lCalcRepayIntVo = loanCalcRepayIntCom.getRepayInt(titaVo);
			// BigDecimal wkInterest = loanCalcRepayIntCom.getInterest();
			wkTotalInterest = wkTotalInterest.add(loanCalcRepayIntCom.getInterest());
			wkRepaidPeriod = loanCalcRepayIntCom.getRepaidPeriod();
			wkPaidTerms = loanCalcRepayIntCom.getPaidTerms();
			wkTotaCount++;

			// 鎖定撥款主檔
			tLoanBorMain = loanBorMainService.holdById(new LoanBorMainId(iCustNo, iFacmNo, wkBormNo));
			if (tLoanBorMain == null) {
				throw new LogicException(titaVo, "E0006",
						"撥款主檔 戶號 = " + iCustNo + " 額度編號 = " + iFacmNo + " 撥款序號 = " + wkBormNo); // 鎖定資料時，發生錯誤
			}
			// 新增交易暫存檔(放款資料)
			addRepayTxTempBormRoutine();
			// 更新撥款主檔
			updLoanBorMainRoutine();
			// 新增計息明細
			addLoanIntDetailRoutine();
			// 新增放款交易內容檔
			addLoanBorTxRoutine();
		}
		if (wkTotaCount == 0)

		{
			throw new LogicException(titaVo, "E3070", ""); // 查無可計息的放款資料
		}

		if (wkTotalInterest.compareTo(iInterest) != 0) {
			throw new LogicException(titaVo, "E0013", "利息計算有誤" + wkTotalInterest + "<>" + iInterest); // E0013 程式邏輯有誤
		}

	}

	// 收息訂正處理
	private void RepayEraseRoutine() throws LogicException {
		this.info("calcRepayEraseRoutine ...");

		Slice<TxTemp> slTxTemp = txTempService.txTempTxtNoEq(titaVo.getOrgEntdyI() + 19110000, titaVo.getOrgKin(),
				titaVo.getOrgTlr(), titaVo.getOrgTno(), 0, Integer.MAX_VALUE);
		lTxTemp = slTxTemp == null ? null : slTxTemp.getContent();
		if (lTxTemp == null || lTxTemp.size() == 0) {
			throw new LogicException(titaVo, "E0001", "交易暫存檔 分行別 = " + titaVo.getOrgKin() + " 交易員代號 = "
					+ titaVo.getOrgTlr() + " 交易序號 = " + titaVo.getOrgTno()); // 查詢資料不存在
		}
		for (TxTemp tx : lTxTemp) {
			wkBormNo = this.parse.stringToInteger(tx.getSeqNo().substring(10, 13));
			if (wkBormNo != 0) { // 0為額度
				tTempVo = tTempVo.getVo(tx.getText());
				wkBorxNo = this.parse.stringToInteger(tTempVo.get("BorxNo"));
				this.info("   wkBormNo = " + wkBormNo);
				this.info("   wkBorxNo = " + wkBorxNo);
				// 還原撥款主檔
				RestoredRepayLoanBorMainRoutine();
				// 註記交易內容檔
				loanCom.setLoanBorTxHcode(iCustNo, iFacmNo, wkBormNo, wkBorxNo, wkNewBorxNo, tLoanBorMain.getLoanBal(),
						titaVo);
			}
		}
	}

	// 新增交易暫存檔(放款資料)
	private void addRepayTxTempBormRoutine() throws LogicException {
		this.info("addRepayTxTempBormRoutine ... ");

		tTxTemp = new TxTemp();
		tTxTempId = new TxTempId();
		loanCom.setTxTemp(tTxTempId, tTxTemp, iCustNo, iFacmNo, wkBormNo, 0, titaVo);
		tTempVo.clear();
		tTempVo.putParam("StoreRate", tLoanBorMain.getStoreRate());
		tTempVo.putParam("Principal", loanCalcRepayIntCom.getPrincipal());
		tTempVo.putParam("DrawdownAmt", tLoanBorMain.getDrawdownAmt());
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
		tTempVo.putParam("BorxNo", wkBorxNo);
		tTxTemp.setText(tTempVo.getJsonString());
		try {
			txTempService.insert(tTxTemp);
		} catch (DBException e) {
			throw new LogicException(titaVo, "E0005", "交易暫存檔 Key = " + tTxTempId); // 新增資料時，發生錯誤 }
		}
	}

	// 新增計息明細
	private void addLoanIntDetailRoutine() throws LogicException {
		this.info("addLoanIntDetailRoutine ... ");
		LoanIntDetail tLoanIntDetail;
		LoanIntDetailId tLoanIntDetailId;

		int wkIntSeq = 0;

		for (CalcRepayIntVo c : lCalcRepayIntVo) {
			wkIntSeq++;
			wkIntStartDate = c.getStartDate() < wkIntStartDate ? c.getStartDate() : wkIntStartDate;
			wkIntEndDate = c.getEndDate() > wkIntEndDate ? c.getEndDate() : wkIntEndDate;
			tLoanIntDetailId = new LoanIntDetailId();
			tLoanIntDetailId.setCustNo(c.getCustNo());
			tLoanIntDetailId.setFacmNo(c.getFacmNo());
			tLoanIntDetailId.setBormNo(c.getBormNo());
			tLoanIntDetailId.setAcDate(this.txBuffer.getTxCom().getTbsdy());
			tLoanIntDetailId.setTlrNo(titaVo.getTlrNo());
			tLoanIntDetailId.setTxtNo(titaVo.getTxtNo());
			tLoanIntDetailId.setIntSeq(wkIntSeq);
			tLoanIntDetail = new LoanIntDetail();
			tLoanIntDetail.setCustNo(c.getCustNo());
			tLoanIntDetail.setFacmNo(c.getFacmNo());
			tLoanIntDetail.setBormNo(c.getBormNo());
			tLoanIntDetail.setAcDate(this.txBuffer.getTxCom().getTbsdy());
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
			tLoanIntDetail.setLoanBal(c.getPrincipal());
			tLoanIntDetail.setExtraRepayFlag(c.getExtraRepayFlag());
			tLoanIntDetail.setProdNo(tFacMain.getProdNo());
			tLoanIntDetail.setBaseRateCode(tFacMain.getBaseRateCode());
			try {
				loanIntDetailService.insert(tLoanIntDetail);
			} catch (DBException e) {
				throw new LogicException(titaVo, "E0005", "計息明細 Key = " + tLoanIntDetailId); // 新增資料時，發生錯誤
			}
		}
	}

	// 更新撥款主檔
	private void updLoanBorMainRoutine() throws LogicException {
		this.info("updLoanBorMainRoutine ... ");

		tLoanBorMain.setLastBorxNo(wkBorxNo);
		tLoanBorMain.setStoreRate(loanCalcRepayIntCom.getStoreRate());
		tLoanBorMain.setRepaidPeriod(tLoanBorMain.getRepaidPeriod() + loanCalcRepayIntCom.getRepaidPeriod());
		tLoanBorMain.setPaidTerms(loanCalcRepayIntCom.getPaidTerms());
		tLoanBorMain.setPrevPayIntDate(loanCalcRepayIntCom.getPrevPaidIntDate());
		tLoanBorMain.setPrevRepaidDate(loanCalcRepayIntCom.getPrevRepaidDate());
		tLoanBorMain.setNextPayIntDate(loanCalcRepayIntCom.getNextPayIntDate());
		tLoanBorMain.setNextRepayDate(loanCalcRepayIntCom.getNextRepayDate());
		tLoanBorMain.setLastEntDy(titaVo.getEntDyI());
		tLoanBorMain.setLastKinbr(titaVo.getKinbr());
		tLoanBorMain.setLastTlrNo(titaVo.getTlrNo());
		tLoanBorMain.setLastTxtNo(titaVo.getTxtNo());
		try {
			loanBorMainService.update(tLoanBorMain);
		} catch (DBException e) {
			throw new LogicException(titaVo, "E0007",
					"撥款主檔 戶號 = " + iCustNo + " 額度編號 = " + iFacmNo + " 撥款序號 = " + wkBormNo); // 更新資料時，發生錯誤
		}
	}

	// 還原撥款主檔
	private void RestoredRepayLoanBorMainRoutine() throws LogicException {
		this.info("RestoredRepayLoanBorMainRoutine ... ");

		tLoanBorMain = loanBorMainService.holdById(new LoanBorMainId(iCustNo, iFacmNo, wkBormNo));
		if (tLoanBorMain == null) {
			throw new LogicException(titaVo, "E0006",
					"撥款主檔 戶號 = " + iCustNo + " 額度編號 = " + iFacmNo + " 撥款序號 = " + wkBormNo); // 鎖定資料時，發生錯誤
		}
		wkNewBorxNo = tLoanBorMain.getLastBorxNo() + 1;

		tLoanBorMain.setLastBorxNo(wkNewBorxNo);
		tLoanBorMain.setStoreRate(this.parse.stringToBigDecimal(tTempVo.get("StoreRate")));
		tLoanBorMain.setDrawdownAmt(this.parse.stringToBigDecimal(tTempVo.get("DrawdownAmt")));
		tLoanBorMain.setLoanBal(this.parse.stringToBigDecimal(tTempVo.get("LoanBal")));
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
			loanBorMainService.update(tLoanBorMain);
		} catch (DBException e) {
			throw new LogicException(titaVo, "E0007",
					"撥款主檔 戶號 = " + iCustNo + " 額度編號 = " + iFacmNo + " 撥款序號 = " + wkBormNo); // 更新資料時，發生錯誤
		}
	}

	// 新增放款交易內容檔
	private void addLoanBorTxRoutine() throws LogicException {
		this.info("addLoanBorTxRoutine ... ");

		tLoanBorTx = new LoanBorTx();
		tLoanBorTxId = new LoanBorTxId();
		loanCom.setLoanBorTx(tLoanBorTx, tLoanBorTxId, iCustNo, iFacmNo, wkBormNo, wkBorxNo, titaVo);
		tLoanBorTx.setDesc("撥款收息－收息");
		tLoanBorTx.setEntryDate(wkTbsDy);
		tLoanBorTx.setDueDate(wkDueDate);
		tLoanBorTx.setTxAmt(this.parse.stringToBigDecimal(titaVo.getTxAmt()));
		tLoanBorTx.setLoanBal(tLoanBorMain.getLoanBal());
		tLoanBorTx.setRate(tLoanBorMain.getStoreRate());
		tLoanBorTx.setIntStartDate(wkIntStartDate);
		tLoanBorTx.setIntEndDate(wkIntEndDate);
		tLoanBorTx.setRepaidPeriod(wkRepaidPeriod);
		tLoanBorTx.setPrincipal(BigDecimal.ZERO);
		tLoanBorTx.setInterest(wkTotalInterest);
		tLoanBorTx.setDelayInt(BigDecimal.ZERO);
		tLoanBorTx.setBreachAmt(BigDecimal.ZERO);
		tLoanBorTx.setCloseBreachAmt(BigDecimal.ZERO);
		tLoanBorTx.setTempAmt(BigDecimal.ZERO);
		tLoanBorTx.setExtraRepay(BigDecimal.ZERO);
		tLoanBorTx.setUnpaidInterest(BigDecimal.ZERO);
		tLoanBorTx.setShortfall(BigDecimal.ZERO);
		tLoanBorTx.setDisplayflag("I"); // 繳息
		// 其他欄位
		tTempVo.clear();
		tTempVo.putParam("PaidTerms", wkPaidTerms);
		tLoanBorTx.setOtherFields(tTempVo.getJsonString());
		try {
			loanBorTxService.insert(tLoanBorTx);
		} catch (DBException e) {
			throw new LogicException(titaVo, "E0005", "放款交易內容檔 " + e.getErrorMsg()); // 新增資料時，發生錯誤
		}
	}

}