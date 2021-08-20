package com.st1.itx.trade.L3;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
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
import com.st1.itx.db.domain.CdBaseRate;
import com.st1.itx.db.domain.FacMain;
import com.st1.itx.db.domain.FacMainId;
import com.st1.itx.db.domain.FacProd;
import com.st1.itx.db.domain.FacProdStepRate;
import com.st1.itx.db.domain.LoanBorMain;
import com.st1.itx.db.domain.LoanBorMainId;
import com.st1.itx.db.domain.LoanBorTx;
import com.st1.itx.db.domain.LoanBorTxId;
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
import com.st1.itx.db.service.LoanRateChangeService;
import com.st1.itx.db.service.TxTempService;
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.date.DateUtil;
import com.st1.itx.util.format.FormatUtil;
import com.st1.itx.util.parse.Parse;
import com.st1.itx.util.common.AcDetailCom;
import com.st1.itx.util.common.AcPaymentCom;
import com.st1.itx.util.common.AcReceivableCom;
import com.st1.itx.util.common.AuthLogCom;
import com.st1.itx.util.common.LoanCom;
import com.st1.itx.util.common.LoanDueAmtCom;
import com.st1.itx.util.common.PfDetailCom;
import com.st1.itx.util.common.data.PfDetailVo;

/*
 * L3100 撥款
 * a.此功能供核貸後,且其相關資料(押品,保證人及法人財務簽證資料)建妥,經主管核准撥款時輸入用.
 * b.撥款方式區分為即時(單筆匯款)及預撥(整批匯款),前者以電話通知匯款,後者以報表及匯款單交出納匯款.
 * c.此交易為2段式交易
 */
/*
 * Tita
 * TimCustNo=9,7
 * FacmNo=9,3
 * BormNo=9,3
 * RateIncr=+9,2.4
 * ApproveRate=9,2.4
 * RateCode=9,1
 * RateAdjFreq=9,2
 * DrawdownCode=9,1
 * CurrencyCode=X,3
 * TimDrawdownAmt=9,14.2
 * DrawdownDate=9,7
 * LoanTermYy=9,2
 * LoanTermMm=9,2
 * LoanTermDd=9,3
 * MaturityDate=9,7
 * AmortizedCode=9,1
 * FreqBase=9,1
 * PayIntFreq=9,2
 * RepayFreq=9,2
 * GracePeriod=9,3
 * GraceDate=9,7
 * SpecificDd=9,2
 * SPECIFICDATE=9,8
 * FirstDueDate=9,7
 * FirstAdjRateDate=9,7
 * NextIntDate=9,8
 * NextRepayDate=9,8
 * TOTALPERIOD=9,3
 * TimAcctFee=9,14.2
 * TimFinalBal=9,14.2
 * NotYetFlag=X,1
 * PieceCode=X,1
 * UsageCode=9,1
 * SynLoan=9,3
 * RenewFlag=X,1
 * OLDFacmNo=9,3
 * OLDBormNo=9,3
 */
/**
 * L3100 撥款
 * 
 * @author iza
 * @version 1.0.0
 */
@Service("L3100")
@Scope("prototype")
public class L3100 extends TradeBuffer {

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
	LoanCom loanCom;
	@Autowired
	AcPaymentCom acPaymentCom;
	@Autowired
	AcReceivableCom acReceivableCom;
	@Autowired
	public PfDetailCom pfDetailCom;
	@Autowired
	public AuthLogCom authLogCom;

	private TitaVo titaVo = new TitaVo();
	private int iCustNo;
	private int iFacmNo;
	private int iBormNo;
	private int iDrawdownDate;
	private int iMaturityDate;
	private int iTxBormNo;
	private BigDecimal iDrawdownAmt;
	private BigDecimal iAcctFee;
	private String iCompensateFlag;
	private String wkCompensateFlag;

	// work area
	private long sno = 0;
	private int wkBormNo = 0;
	private int wkBorxNo = 1;
	private int wkTbsDy;
	private String sProdStepNo = "";
	private int wkStartDate = 0;
	private int wkEffectDate = 0;
	private boolean wkReserve = false;
	private TempVo tTempVo = new TempVo();
	private TempVo tempVo = new TempVo();
	private FacProd tFacProd;
	private FacMain tFacMain;
	private LoanBorMain tLoanBorMain;
	private LoanBorTx tLoanBorTx;
	private LoanBorTxId tLoanBorTxId;
	private LoanRateChange tLoanRateChange;
	private LoanRateChangeId tLoanRateChangeId;
	private TxTemp tTxTemp;
	private TxTempId tTxTempId;
	private AcDetail acDetail;
	private AcReceivable tAcReceivable = new AcReceivable();
	private List<AcReceivable> lAcReceivable = new ArrayList<AcReceivable>();
	private DecimalFormat df = new DecimalFormat("##,###,###,###,##0");
	private List<FacProdStepRate> lFacProdStepRate = new ArrayList<FacProdStepRate>();

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active L3100 ");
		this.info("   isActfgEntry   = " + titaVo.isActfgEntry());
		this.info("   isActfgSuprele = " + titaVo.isActfgSuprele());
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
		loanCom.setTxBuffer(this.txBuffer);

		wkTbsDy = this.txBuffer.getTxCom().getTbsdy();

		// 取得輸入資料
		iCustNo = this.parse.stringToInteger(titaVo.getParam("TimCustNo"));
		iFacmNo = this.parse.stringToInteger(titaVo.getParam("FacmNo"));
		iBormNo = this.parse.stringToInteger(titaVo.getParam("BormNo"));
		iDrawdownAmt = this.parse.stringToBigDecimal(titaVo.getParam("TimDrawdownAmt"));
		iAcctFee = this.parse.stringToBigDecimal(titaVo.getParam("TimAcctFee"));
		iDrawdownDate = this.parse.stringToInteger(titaVo.getParam("DrawdownDate"));
		iMaturityDate = this.parse.stringToInteger(titaVo.getParam("MaturityDate"));
		iCompensateFlag = titaVo.getParam("CompensateFlag");
		iTxBormNo = this.parse.stringToInteger(titaVo.getParam("TxBormNo"));
		// 交易檢核
		txCheckRoutine();

		if (iTxBormNo > 900) {
			wkReserve = true;
		}

		// 維護放款主檔, 更新戶況為98:預約已撥款
		if (wkReserve) {
			ReserveRoutine();
		}

		// 維護額度主檔, 預約撥款到期執行撥款交易不需更新額度主檔
		facMainRoutine();

		// 查詢商品參數檔
		tFacProd = facProdService.findById(tFacMain.getProdNo());
		if (tFacProd == null) {
			throw new LogicException(titaVo, "E0001", "商品參數檔"); // 查詢資料不存在
		}
		// 維護撥款檔
		LoanBorMainRoutine();

		// 維護放款利率變動檔
		LoanRateChangeMaintain();

		// 維護放款交易內容檔
		LoanBorTxRoutine();

		// 帳管費
		AcctFeeRoutine();

		// 維護撥款匯款檔
		AcPaymentRoutine();

		// 帳務處理
		AcDetailRoutine();

		// 業績處理
		PfDetailRoutine();

		this.totaVo.putParam("BormNo", wkBormNo);
		this.totaVo.put("PdfSnoF", "" + sno);
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

	private void ReserveRoutine() throws LogicException {
		this.info("   ReserveRoutine ...");

		if (titaVo.isActfgSuprele()) {
			return;
		}

		tLoanBorMain = loanBorMainService.holdById(new LoanBorMainId(iCustNo, iFacmNo, iTxBormNo));
		if (tLoanBorMain == null) {
			throw new LogicException(titaVo, "E0006", "撥款主檔"); // 鎖定資料時，發生錯誤
		}

		// 一般交易
		if (titaVo.isHcodeNormal()) {
			if (tLoanBorMain.getStatus() != 99) {
				throw new LogicException(titaVo, "E3055", "撥款主檔 "); // 該筆資料非預約撥款
			}
			if (iDrawdownDate != titaVo.getEntDyI()) {
				throw new LogicException(titaVo, "E0010", "預約到期撥款不可跨日，需刪除預約，人工撥款 "); // E0010 功能選擇錯誤
			}
			tLoanBorMain.setStatus(98); // 98:預約已撥款
		}

		// 更正交易
		if (titaVo.isHcodeErase()) {
			tLoanBorMain.setStatus(99); // 99:預約撥款
		}

		// 更新放款主檔
		try {
			tLoanBorMain.setLastUpdate(
					parse.IntegerToSqlDateO(dDateUtil.getNowIntegerForBC(), dDateUtil.getNowIntegerTime()));
			tLoanBorMain.setLastUpdateEmpNo(titaVo.getTlrNo());
			loanBorMainService.update(tLoanBorMain);
		} catch (DBException e) {
			throw new LogicException(titaVo, "E0007", "撥款主檔 " + e.getErrorMsg()); // 更新資料時，發生錯誤
		}
	}

	private void facMainRoutine() throws LogicException {
		this.info("   facMainRoutine ...");

		BigDecimal wkAvailableAmt = new BigDecimal(0);
		BigDecimal wkDrawdownAmt = new BigDecimal(0);
		BigDecimal wkRvDrawdownAmt = new BigDecimal(0);
		int wkFirstDrawdownDate = 0;
		int wkMaturityDate = 0;

		if (titaVo.isActfgSuprele()) {
			tFacMain = facMainService.findById(new FacMainId(iCustNo, iFacmNo));
			if (tFacMain == null) {
				throw new LogicException(titaVo, "E0001", "額度主檔 戶號 = " + iCustNo + " 額度編號 = " + iFacmNo); // 查詢資料不存在
			}
			wkBormNo = iBormNo;
			return;
		}
		// 已預約金額
		Slice<LoanBorMain> slLoanBorMain = loanBorMainService.findStatusEq(Arrays.asList(99), iCustNo, iFacmNo, iFacmNo,
				0, Integer.MAX_VALUE, titaVo);
		if (slLoanBorMain != null) {
			for (LoanBorMain rv : slLoanBorMain.getContent()) {
				wkRvDrawdownAmt = wkRvDrawdownAmt.add(rv.getDrawdownAmt());
			}
		}
		// 鎖定額度檔
		tFacMain = facMainService.holdById(new FacMainId(iCustNo, iFacmNo));
		if (tFacMain == null) {
			throw new LogicException(titaVo, "E0006", "額度主檔 戶號 = " + iCustNo + " 額度編號 = " + iFacmNo); // 鎖定資料時，發生錯誤
		}
		if (tFacMain.getActFg() == 1) {
			throw new LogicException(titaVo, "E0021",
					"額度檔 戶號 = " + tFacMain.getCustNo() + " 額度編號 =  " + tFacMain.getFacmNo()); // 該筆資料待放行中
		}
		wkAvailableAmt = tFacMain.getLineAmt().subtract(tFacMain.getUtilBal()).subtract(wkRvDrawdownAmt);
		if (titaVo.isHcodeNormal()) {
			// 檢查額度 預約撥款到期執行撥款交易不需更新額度主檔
			if (wkAvailableAmt.compareTo(iDrawdownAmt) < 0) {
				throw new LogicException(titaVo, "E3051", "可用額度=" + wkAvailableAmt + "小於撥款金額" + iDrawdownAmt); // 額度不足撥款金額
			}
			// 借新還舊(同額度)不檢查動支期限
			if ("Y".equals(titaVo.getParam("RenewFlag")) && titaVo.getParam("RpFacmNo1") == titaVo.getParam("FacmNo")) {
				this.info("RenewFlag" + titaVo.getParam("RenewFlag") + "," + titaVo.getParam("RpFacmNo1") + "="
						+ titaVo.getParam("FacmNo"));
			} else {
				if (tFacMain.getUtilDeadline() < wkTbsDy) {
					throw new LogicException(titaVo, "E3052", "動支期限=" + tFacMain.getUtilDeadline()); // 已超過動支期限
				}
				if (tFacMain.getRecycleCode().equals("1") && tFacMain.getRecycleDeadline() < wkTbsDy) {
					throw new LogicException(titaVo, "E3053", "循環動用期限=" + tFacMain.getRecycleDeadline()); // 已超過循環動用期限
				}
			}
			if (!tFacMain.getL9110Flag().equals("Y")) {
				throw new LogicException(titaVo, "E3083", "額度主檔 戶號 = " + iCustNo + " 額度編號 = " + iFacmNo); // 撥款審核資料表尚未列印，請先作L9110交易
			}
			if (tFacMain.getFirstDrawdownDate() > 0 && iDrawdownDate < tFacMain.getFirstDrawdownDate()) {
				throw new LogicException(titaVo, "E0019", "撥款日期須 >= " + tFacMain.getFirstDrawdownDate()); // 輸入資料錯誤
			}

			tFacMain.setUtilAmt(tFacMain.getUtilAmt().add(iDrawdownAmt));
			tFacMain.setUtilBal(tFacMain.getUtilBal().add(iDrawdownAmt));

			wkBormNo = tFacMain.getLastBormNo() + 1;
			// 新增交易暫存檔
			tTxTemp = new TxTemp();
			tTxTempId = new TxTempId();
			loanCom.setTxTemp(tTxTempId, tTxTemp, iCustNo, iFacmNo, 0, 0, titaVo);
			tTempVo.clear();
			tTempVo.putParam("DrawdownAmt", iDrawdownAmt);
			tTempVo.putParam("FirstDrawdownDate", tFacMain.getFirstDrawdownDate());
			tTempVo.putParam("MaturityDate", tFacMain.getMaturityDate());
			tTempVo.putParam("BormNo", wkBormNo);

			tTxTemp.setText(tTempVo.getJsonString());

			try {
				txTempService.insert(tTxTemp);
			} catch (DBException e) {
				throw new LogicException(titaVo, "E0005", "交易暫存檔 Key = " + tTxTempId); // 新增資料時，發生錯誤
			}
			titaVo.putParam("BormNo", wkBormNo);
			// 更新額度資料
			if (tFacMain.getFirstDrawdownDate() == 0) {
				tFacMain.setFirstDrawdownDate(iDrawdownDate);
			}
			if (tFacMain.getMaturityDate() == 0) {
				tFacMain.setMaturityDate(iMaturityDate);
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
			wkFirstDrawdownDate = this.parse.stringToInteger(tTempVo.get("FirstDrawdownDate"));
			wkMaturityDate = this.parse.stringToInteger(tTempVo.get("MaturityDate"));

			wkBormNo = this.parse.stringToInteger(tTempVo.get("BormNo"));

			// 準備更新額度資料

			tFacMain.setUtilAmt(tFacMain.getUtilAmt().subtract(wkDrawdownAmt));
			tFacMain.setUtilBal(tFacMain.getUtilBal().subtract(wkDrawdownAmt));

			tFacMain.setFirstDrawdownDate(wkFirstDrawdownDate);
			tFacMain.setMaturityDate(wkMaturityDate);

		}
		// 更新已撥款序號
		if (titaVo.isHcodeNormal())
			tFacMain.setLastBormNo(wkBormNo);
		else if (titaVo.isHcodeErase()) {
			if (wkBormNo != tFacMain.getLastBormNo())
				throw new LogicException(titaVo, "E3088", "需先訂正，撥款依序 = " + tFacMain.getLastBormNo()); // 放款交易訂正須由最近一筆交易開始訂正
			else
				tFacMain.setLastBormNo(wkBormNo - 1);
		}

		if (titaVo.isHcodeModify()) {
			// 新增交易暫存檔
			tTxTemp = new TxTemp();
			tTxTempId = new TxTempId();
			loanCom.setTxTemp(tTxTempId, tTxTemp, iCustNo, iFacmNo, 0, 0, titaVo);
			tTempVo.clear();
			tTempVo.putParam("DrawdownAmt", iDrawdownAmt);
			tTempVo.putParam("FirstDrawdownDate", tFacMain.getFirstDrawdownDate());
			tTempVo.putParam("MaturityDate", tFacMain.getMaturityDate());
			tTempVo.putParam("BormNo", wkBormNo);
			tTempVo.putParam("CompensateFlag", tFacMain.getCompensateFlag());
			tTxTemp.setText(tTempVo.getJsonString());
			this.info("test 5 =" + tFacMain.getCompensateFlag());
			try {
				txTempService.insert(tTxTemp);
			} catch (DBException e) {
				throw new LogicException(titaVo, "E0007", "交易暫存檔"); // 更新資料時，發生錯誤
			}
			// 準備更新額度資料
			tFacMain.setUtilAmt(tFacMain.getUtilAmt().add(iDrawdownAmt));
			tFacMain.setUtilBal(tFacMain.getUtilBal().add(iDrawdownAmt));

			if (tFacMain.getFirstDrawdownDate() == 0) {
				tFacMain.setFirstDrawdownDate(iDrawdownDate);
			}
			if (tFacMain.getMaturityDate() == 0) {
				tFacMain.setMaturityDate(iMaturityDate);
			}
		}

		// MRKEY
		titaVo.putParam("MRKEY", FormatUtil.pad9(String.valueOf(iCustNo), 7) + "-"
				+ FormatUtil.pad9(String.valueOf(iFacmNo), 3) + "-" + FormatUtil.pad9(String.valueOf(wkBormNo), 3));

		// 更新額度檔
		try {
			facMainService.update(tFacMain);
		} catch (DBException e) {
			throw new LogicException(titaVo, "E0007", "額度主檔"); // 更新資料時，發生錯誤
		}
	}

	private void LoanBorMainRoutine() throws LogicException {
		this.info("LoanBorMainRoutine ...");
//		this.info("   tFacProd.getCharCode() = " + tFacProd.getCharCode());

		// 登錄
		if (titaVo.isActfgEntry() && titaVo.isHcodeNormal()) {
			// 新增撥款檔
			tLoanBorMain = new LoanBorMain();
			moveLoanBorMain();
			try {
				loanBorMainService.insert(tLoanBorMain);
			} catch (DBException e) {
				throw new LogicException(titaVo, "E3009", "撥款檔 " + e.getErrorMsg()); // 新增資料時，發生錯誤
			}
		}
		// 登錄訂正
		if (titaVo.isActfgEntry() && titaVo.isHcodeErase()) {
			// 刪除撥款檔
			tLoanBorMain = loanBorMainService.holdById(new LoanBorMainId(iCustNo, iFacmNo, wkBormNo));
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
			tLoanBorMain = loanBorMainService.holdById(new LoanBorMainId(iCustNo, iFacmNo, wkBormNo));
			tempVo = authLogCom.exec(iCustNo, iFacmNo, titaVo);

			if (tLoanBorMain == null || tempVo == null) {
				throw new LogicException(titaVo, "E0006", "撥款主檔"); // 鎖定資料時，發生錯誤
			}
			tLoanBorMain = new LoanBorMain();
			moveLoanBorMain();
			try {
				loanBorMainService.update(tLoanBorMain);
			} catch (DBException e) {
				throw new LogicException(titaVo, "E0008", "撥款檔 " + e.getErrorMsg()); // 新增資料時，發生錯誤
			}
		}
		// 放行
		if (titaVo.isActfgSuprele() && titaVo.isHcodeNormal()) {
			tLoanBorMain = loanBorMainService.holdById(new LoanBorMainId(iCustNo, iFacmNo, wkBormNo));
			if (tLoanBorMain == null) {
				throw new LogicException(titaVo, "E0006", "撥款主檔"); // 鎖定資料時，發生錯誤
			}
			if (tLoanBorMain.getActFg() != 1) {
				throw new LogicException(titaVo, "E0017",
						"撥款主檔 戶號 = " + iCustNo + "額度編號 = " + iFacmNo + "撥款序號 = " + wkBormNo); // 該筆交易狀態非待放行，不可做交易放行
			}
			// 新增交易暫存檔
			tTxTemp = new TxTemp();
			tTxTempId = new TxTempId();
			loanCom.setTxTemp(tTxTempId, tTxTemp, iCustNo, iFacmNo, wkBormNo, 0, titaVo);
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
			tLoanBorMain = loanBorMainService.holdById(new LoanBorMainId(iCustNo, iFacmNo, wkBormNo));
			if (tLoanBorMain == null) {
				throw new LogicException(titaVo, "E0006",
						"撥款主檔 戶號 = " + iCustNo + "額度編號 = " + iFacmNo + "撥款序號 = " + wkBormNo); // 鎖定資料時，發生錯誤
			}
			// 放款交易訂正交易須由最後一筆交易開始訂正
			loanCom.checkEraseBormTxSeqNo(tLoanBorMain, titaVo);
			// 查詢交易暫存檔
			String wkSeqNo = FormatUtil.pad9(String.valueOf(iCustNo), 7) + FormatUtil.pad9(String.valueOf(iFacmNo), 3)
					+ FormatUtil.pad9(String.valueOf(wkBormNo), 3) + "000";
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
						"撥款主檔 戶號 = " + iCustNo + "額度編號 = " + iFacmNo + "撥款序號 = " + wkBormNo + e.getErrorMsg()); // 新增資料時，發生錯誤
			}
		}
	}

	// 維護利率變動檔
	private void LoanRateChangeMaintain() throws LogicException {
		this.info("LoanRateChangeMaintain ...");
		this.info("titaVo.isActfgSuprele() = " + titaVo.isActfgSuprele());
		if (titaVo.isActfgSuprele()) {
			this.info("return");
			return;
		}

		// 刪除放款利率變動檔
		LoanRateChangeDelete();

		sProdStepNo = FormatUtil.pad9(String.valueOf(tFacMain.getCustNo()), 7)
				+ FormatUtil.pad9(String.valueOf(tFacMain.getFacmNo()), 3);

		// 維護放款利率變動檔, 階梯式利率
		Slice<FacProdStepRate> slFacProdStepRate = facProdStepRateService.stepRateProdNoEq(sProdStepNo, 0, 999, 0,
				Integer.MAX_VALUE);
		lFacProdStepRate = slFacProdStepRate == null ? null : slFacProdStepRate.getContent();
		if (lFacProdStepRate != null && lFacProdStepRate.size() > 0) {
			// 維護放款利率變動檔, 階梯式利率
			LoanRateChange2Routine();

		} else {
			// 維護放款利率變動檔, 核准利率
			LoanRateChange1Routine();
		}
	}

	// 刪除放款利率變動檔
	private void LoanRateChangeDelete() throws LogicException {
		this.info("LoanRateChangeDelete ...");
		this.info("titaVo.isActfgSuprele()   = " + titaVo.isActfgSuprele());
		if (titaVo.isActfgSuprele()) {
			this.info("return");
			return;
		}
		this.info("titaVo.isHcodeErase()   = " + titaVo.isHcodeErase());
		this.info("titaVo.isHcodeModify()   = " + titaVo.isHcodeModify());
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
	}

	private void LoanRateChange1Routine() throws LogicException {
		this.info("LoanRateChange1Routine ...");
		this.info("titaVo.isActfgSuprele()   = " + titaVo.isActfgSuprele());
		if (titaVo.isActfgSuprele()) {
			this.info("return");
			return;
		}

		if (titaVo.isHcodeNormal() || titaVo.isHcodeModify()) {
			// 新增放款利率變動檔, 核准利率
			tLoanRateChange = new LoanRateChange();
			tLoanRateChangeId = new LoanRateChangeId();
			tLoanRateChangeId.setCustNo(iCustNo);
			tLoanRateChangeId.setFacmNo(iFacmNo);
			tLoanRateChangeId.setBormNo(wkBormNo);
			tLoanRateChangeId.setEffectDate(iDrawdownDate);
			SetLoanRateChange1();
			try {
				loanRateChangeService.insert(tLoanRateChange);
			} catch (DBException e) {
				throw new LogicException(titaVo, "E0005", "放款利率變動檔 " + e.getErrorMsg()); // 新增資料時，發生錯誤
			}
		}
	}

	private void LoanRateChange2Routine() throws LogicException {
		this.info("   LoanRateChange2Routine ActfgSuprele" + titaVo.isActfgSuprele());
		this.info("   titaVo.isActfgSuprele()    = " + titaVo.isActfgSuprele());
		if (titaVo.isActfgSuprele()) {
			this.info("return");
			return;
		}
		wkStartDate = 0;
		wkEffectDate = 0;
		if (titaVo.isHcodeNormal() || titaVo.isHcodeModify()) {
			this.info("lFacProdStepRate = " + lFacProdStepRate);
			for (FacProdStepRate tFacProdStepRate : lFacProdStepRate) {
				// 利率起日
				dDateUtil.init();
				dDateUtil.setMons(tFacProdStepRate.getMonthStart() - 1);
				dDateUtil.setDate_1(tFacMain.getFirstDrawdownDate()); // 階梯式利率月份以額度初貸日為準
				wkStartDate = dDateUtil.getCalenderDay();
				// 利率起日、利率起日，小於撥款日

				// 利率起日小於撥款日=> 撥款日為生效日；否則利率起日為生效日
				if (wkStartDate <= tLoanBorMain.getDrawdownDate()) {
					wkEffectDate = tLoanBorMain.getDrawdownDate();
				} else {
					wkEffectDate = wkStartDate;
				}
				SetLoanRateChange2(tFacProdStepRate);
				this.info("tFacProdStepRate = " + tFacProdStepRate);
				this.info("wkStartDate = " + wkStartDate);
				this.info("iDrawdownDate = " + tLoanBorMain.getDrawdownDate());
				this.info("tLoanRateChange.getEffectDate() = " + tLoanRateChange.getEffectDate());
				LoanRateChange t1LoanRateChange = loanRateChangeService.holdById(tLoanRateChangeId, titaVo);
				if (t1LoanRateChange == null) {
					try {
						loanRateChangeService.insert(tLoanRateChange);
					} catch (DBException e) {
						throw new LogicException(titaVo, "E0005", "放款利率變動檔 " + e.getErrorMsg()); // 新增資料時，發生錯誤
					}
				} else {
					try {
						loanRateChangeService.update(tLoanRateChange);
					} catch (DBException e) {
						throw new LogicException(titaVo, "E0007", "放款利率變動檔 " + e.getErrorMsg()); // 新增資料時，發生錯誤
					}

				}
			}
		}
	}

	private void LoanBorTxRoutine() throws LogicException {
		this.info("   LoanBorTxRoutine ...");
		this.info("   titaVo.isActfgSuprele()    = " + titaVo.isActfgSuprele());
		if (titaVo.isActfgSuprele()) {
			this.info("return");
			return;
		}
		if (titaVo.isHcodeErase() || titaVo.isHcodeModify()) {
			// 刪除放款交易內容檔
			tLoanBorTx = loanBorTxService.holdById(new LoanBorTxId(iCustNo, iFacmNo, wkBormNo, wkBorxNo));
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
			loanCom.setLoanBorTx(tLoanBorTx, tLoanBorTxId, iCustNo, iFacmNo, wkBormNo, wkBorxNo, titaVo);

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
			acDetail = new AcDetail();
			List<AcDetail> lAcDetail = new ArrayList<AcDetail>();
			// 借方 貸出金額
			acDetail.setDbCr("D");
			acDetail.setAcctCode(tFacMain.getAcctCode());
			acDetail.setTxAmt(iDrawdownAmt);
			acDetail.setCustNo(iCustNo);
			acDetail.setFacmNo(iFacmNo);
			acDetail.setBormNo(wkBormNo);
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

	private void AcctFeeRoutine() throws LogicException {
		this.info("   AcctFeeRoutine ...");

		// 帳管費，首期回收時先收取。
		if (this.titaVo.isActfgRelease() && iAcctFee.compareTo(BigDecimal.ZERO) > 0) {
			tAcReceivable = new AcReceivable();
			tAcReceivable.setReceivableFlag(3); // 3-未收費用
			tAcReceivable.setAcctCode("F10"); // F10 帳管費
			tAcReceivable.setCustNo(iCustNo);
			tAcReceivable.setFacmNo(iFacmNo);
			tAcReceivable.setRvNo(FormatUtil.pad9(String.valueOf(wkBormNo), 3));
			tAcReceivable.setRvAmt(iAcctFee);
			lAcReceivable.add(tAcReceivable);
			acReceivableCom.setTxBuffer(this.getTxBuffer());
			acReceivableCom.mnt(0, lAcReceivable, titaVo); // 0-起帳 1-銷帳
		}
	}

	// 維護撥款匯款檔
	private void AcPaymentRoutine() throws LogicException {
		this.info("   AcPaymentRoutine ...");
		this.info("   isActfgEntry = " + titaVo.isActfgEntry());

		acPaymentCom.setTxBuffer(this.getTxBuffer());
		if (titaVo.isActfgEntry()) {
			acPaymentCom.remit(titaVo);
		}

		if (titaVo.isHcodeNormal() && titaVo.isActfgRelease()
				&& parse.stringToInteger(titaVo.getParam("DrawdownCode")) == 2) {
			sno = acPaymentCom.printRemitForm(titaVo);

		}
	}

	// 業績處理
	private void PfDetailRoutine() throws LogicException {
		this.info("   PfDetailRoutine ...");
		pfDetailCom.setTxBuffer(this.getTxBuffer());
		PfDetailVo pf = new PfDetailVo();
		pf.setCustNo(iCustNo); // 借款人戶號
		pf.setFacmNo(iFacmNo); // 額度編號
		pf.setBormNo(wkBormNo); // 撥款序號
		pf.setBorxNo(tLoanBorMain.getLastBorxNo()); // 交易內容檔序號
		pf.setRepayType(0); // 還款類別 0.撥款
		pf.setDrawdownAmt(tLoanBorMain.getDrawdownAmt());// 撥款金額/追回金額
		pf.setPieceCode(tLoanBorMain.getPieceCode()); // 計件代碼
		pf.setPieceCodeSecond(tLoanBorMain.getPieceCodeSecond()); // 計件代碼2
		pf.setPieceCodeSecondAmt(tLoanBorMain.getPieceCodeSecondAmt());// 計件代碼2金額
		pf.setDrawdownDate(tLoanBorMain.getDrawdownDate());// 撥款日期
		pf.setRepaidPeriod(0); // 已攤還期數
		pfDetailCom.addDetail(pf, titaVo);

	}

	// -------------------------------------------------------------------------------
	private void moveLoanBorMain() throws LogicException {
		this.info("   moveLoanBorMain ...");

		BigDecimal wkDueAmt = BigDecimal.ZERO;
		int wGracePeriod = 0;
		int wkNextRepayDate = 0;
		int wkNewSpecificDate = 0;
		tLoanBorMain.setCustNo(iCustNo);
		tLoanBorMain.setFacmNo(iFacmNo);
		tLoanBorMain.setBormNo(wkBormNo);
		tLoanBorMain.setLoanBorMainId(new LoanBorMainId(iCustNo, iFacmNo, wkBormNo));
		tLoanBorMain.setLastBorxNo(wkBorxNo);
		tLoanBorMain.setLastOvduNo(0);
		tLoanBorMain.setStatus(0);
		tLoanBorMain.setRateIncr(this.parse.stringToBigDecimal(titaVo.getParam("RateIncr")));
		tLoanBorMain.setIndividualIncr(new BigDecimal(0));
		tLoanBorMain.setApproveRate(this.parse.stringToBigDecimal(titaVo.getParam("ApproveRate")));
		tLoanBorMain.setStoreRate(this.parse.stringToBigDecimal(titaVo.getParam("ApproveRate")));
		tLoanBorMain.setRateCode(titaVo.getParam("RateCode"));
		tLoanBorMain.setRateAdjFreq(this.parse.stringToInteger(titaVo.getParam("RateAdjFreq")));
		tLoanBorMain.setDrawdownCode(titaVo.getParam("DrawdownCode"));
		tLoanBorMain.setCurrencyCode(titaVo.getParam("CurrencyCode"));
		tLoanBorMain.setDrawdownAmt(iDrawdownAmt);
		tLoanBorMain.setLoanBal(iDrawdownAmt);
		tLoanBorMain.setDrawdownDate(this.parse.stringToInteger(titaVo.getParam("DrawdownDate")));
		tLoanBorMain.setLoanTermYy(this.parse.stringToInteger(titaVo.getParam("LoanTermYy")));
		tLoanBorMain.setLoanTermMm(this.parse.stringToInteger(titaVo.getParam("LoanTermMm")));
		tLoanBorMain.setLoanTermDd(this.parse.stringToInteger(titaVo.getParam("LoanTermDd")));
		tLoanBorMain.setMaturityDate(this.parse.stringToInteger(titaVo.getParam("MaturityDate")));
		tLoanBorMain.setIntCalcCode(titaVo.getParam("IntCalcCode"));
		tLoanBorMain.setAmortizedCode(titaVo.getParam("AmortizedCode"));
		tLoanBorMain.setFreqBase(this.parse.stringToInteger(titaVo.getParam("FreqBase")));
		tLoanBorMain.setPayIntFreq(this.parse.stringToInteger(titaVo.getParam("PayIntFreq")));
		tLoanBorMain.setRepayFreq(this.parse.stringToInteger(titaVo.getParam("RepayFreq")));
		tLoanBorMain.setTotalPeriod(this.parse.stringToInteger(titaVo.getParam("TotalPeriod")));
		tLoanBorMain.setRepaidPeriod(0);
		tLoanBorMain.setPaidTerms(0);
		tLoanBorMain.setPrevPayIntDate(0);
		tLoanBorMain.setPrevRepaidDate(0);
		tLoanBorMain.setNextPayIntDate(this.parse.stringToInteger(titaVo.getParam("FirstDueDate")));
		tLoanBorMain.setNextRepayDate(this.parse.stringToInteger(titaVo.getParam("NextRepayDate")));
		tLoanBorMain.setGracePeriod(this.parse.stringToInteger(titaVo.getParam("GracePeriod")));
		tLoanBorMain.setGraceDate(this.parse.stringToInteger(titaVo.getParam("GraceDate")));
		tLoanBorMain.setSpecificDd(this.parse.stringToInteger(titaVo.getParam("SpecificDd")));
		tLoanBorMain.setSpecificDate(this.parse.stringToInteger(titaVo.getParam("SpecificDate")));
		tLoanBorMain.setFirstDueDate(this.parse.stringToInteger(titaVo.getParam("FirstDueDate")));
		tLoanBorMain.setFirstAdjRateDate(this.parse.stringToInteger(titaVo.getParam("FirstAdjRateDate")));
		tLoanBorMain.setNextAdjRateDate(this.parse.stringToInteger(titaVo.getParam("FirstAdjRateDate")));
		tLoanBorMain.setAcctFee(this.parse.stringToBigDecimal(titaVo.getParam("TimAcctFee")));
		tLoanBorMain.setFinalBal(this.parse.stringToBigDecimal(titaVo.getParam("TimFinalBal")));
		tLoanBorMain.setNotYetFlag(titaVo.getParam("NotYetFlag"));
		tLoanBorMain.setRenewFlag(titaVo.getParam("RenewFlag"));
		tLoanBorMain.setPieceCode(titaVo.getParam("PieceCode"));
		tLoanBorMain.setPieceCodeSecond(titaVo.getParam("PieceCodeSecond"));
		tLoanBorMain.setPieceCodeSecondAmt(parse.stringToBigDecimal(titaVo.getParam("TimPieceCodeSecondAmt")));
		tLoanBorMain.setUsageCode(titaVo.getParam("UsageCode"));
		tLoanBorMain.setSyndNo(this.parse.stringToInteger(titaVo.getParam("SyndNo")));

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
		tLoanBorMain.setRemitBank("");
		tLoanBorMain.setRemitBranch("");
		tLoanBorMain.setRemitAcctNo(new BigDecimal(0));
		tLoanBorMain.setCompensateAcct("");
		for (int i = 1; i <= 5; i++) {
			if (titaVo.getSecNo().equals("01") && titaVo.get("RpCode" + i) != null) {
				if (parse.stringToInteger(titaVo.getParam("RpCode" + i)) > 0
						&& parse.stringToInteger(titaVo.getParam("RpCode" + i)) < 90) {
					tLoanBorMain.setRemitBank(titaVo.getParam("RpRemitBank" + i));
					tLoanBorMain.setRemitBranch(titaVo.getParam("RpRemitBranch" + i));
					tLoanBorMain.setRemitAcctNo(parse.stringToBigDecimal(titaVo.getParam("RpRemitAcctNo" + i)));
					tLoanBorMain.setCompensateAcct(titaVo.getParam("RpCustName" + i));
					tLoanBorMain.setRemark(titaVo.getParam("RpRemark" + i));
				}
			}
		}

		tLoanBorMain.setAcDate(titaVo.getEntDyI());
		tLoanBorMain.setNextAcDate(0);
		tLoanBorMain.setBranchNo(tFacMain.getBranchNo());

		// 重算新指定基準日期
		if (tLoanBorMain.getSpecificDd() > 0) {
			wkNewSpecificDate = loanCom.getSpecificDate(tLoanBorMain.getSpecificDd(), tLoanBorMain.getFirstDueDate(),
					tLoanBorMain.getPayIntFreq());
			tLoanBorMain.setSpecificDate(wkNewSpecificDate);
		}

		// 計算寬限期數
		if (tLoanBorMain.getGraceDate() > 0) {
			wGracePeriod = loanCom.getGracePeriod(tLoanBorMain.getAmortizedCode(), tLoanBorMain.getFreqBase(),
					tLoanBorMain.getPayIntFreq(), tLoanBorMain.getSpecificDate(), tLoanBorMain.getSpecificDd(),
					tLoanBorMain.getGraceDate());
			tLoanBorMain.setGracePeriod(wGracePeriod);
		}

		// 計算期金
		wkDueAmt = loanDueAmtCom.getDueAmt(tLoanBorMain.getDrawdownAmt(), tLoanBorMain.getApproveRate(),
				tLoanBorMain.getAmortizedCode(), tLoanBorMain.getFreqBase(), tLoanBorMain.getTotalPeriod(),
				tLoanBorMain.getGracePeriod(), tLoanBorMain.getPayIntFreq(), tLoanBorMain.getFinalBal(), titaVo);
		tLoanBorMain.setDueAmt(wkDueAmt);

		if (tLoanBorMain.getSpecificDd() > 0) {
			// 下次還本日
			wkNextRepayDate = loanCom.getNextRepayDate(tLoanBorMain.getAmortizedCode(), tLoanBorMain.getRepayFreq(),
					tLoanBorMain.getFreqBase(), tLoanBorMain.getSpecificDate(), tLoanBorMain.getSpecificDd(),
					tLoanBorMain.getPrevRepaidDate(), tLoanBorMain.getMaturityDate(), tLoanBorMain.getGraceDate());
			tLoanBorMain.setNextRepayDate(wkNextRepayDate);
		}

	}

	private void moveLoanBorTx() throws LogicException {
		this.info("   moveLoanBorTx ...");

		if (wkReserve) {
			tLoanBorTx.setTxTypeCode(1); // 0: 臨櫃交易 1: 批次交易
		}
		if ("Y".equals(titaVo.getParam("RenewFlag"))) {
			tLoanBorTx.setDesc("展期");
		} else {
			tLoanBorTx.setDesc("撥款");
		}
		tLoanBorTx.setEntryDate(tLoanBorMain.getDrawdownDate());
		tLoanBorTx.setTxAmt(this.parse.stringToBigDecimal(titaVo.getTxAmt()));
		tLoanBorTx.setLoanBal(iDrawdownAmt);
		tLoanBorTx.setRate(tLoanBorMain.getStoreRate());
		tLoanBorTx.setDisplayflag("A");
		// 其他欄位
		tTempVo.clear();
		for (int i = 1; i <= 5; i++) {
			if (parse.stringToInteger(titaVo.getParam("RpCode" + i)) > 0
					&& parse.stringToInteger(titaVo.getParam("RpCode" + i)) < 90) {
				tTempVo.putParam("RemitBank",
						titaVo.getParam("RpRemitBank" + i) + titaVo.getParam("RpRemitBranch" + i));
				tTempVo.putParam("RemitAcctNo", titaVo.getParam("RpRemitAcctNo" + i));
				tTempVo.putParam("RemitCustName", titaVo.getParam("RpCustName" + i));
				tTempVo.putParam("RemitRemark", titaVo.getParam("RpRemark" + i));
				tTempVo.putParam("RemitAmt", titaVo.getParam("RpAmt" + i));
				break;
			}
		}
		tTempVo.putParam("CompensateFlag", iCompensateFlag);

		tLoanBorTx.setOtherFields(tTempVo.getJsonString());
	}

	private void SetLoanRateChange1() throws LogicException {
		this.info("SetLoanRateChange1 ...");

		tLoanRateChange.setCustNo(iCustNo);
		tLoanRateChange.setFacmNo(iFacmNo);
		tLoanRateChange.setBormNo(wkBormNo);
		tLoanRateChange.setEffectDate(iDrawdownDate);
		tLoanRateChange.setLoanRateChangeId(tLoanRateChangeId);
		tLoanRateChange.setStatus(0);
		tLoanRateChange.setRateCode(tLoanBorMain.getRateCode());
		tLoanRateChange.setProdNo(tFacMain.getProdNo());
		tLoanRateChange.setBaseRateCode(tFacMain.getBaseRateCode());
		tLoanRateChange.setIncrFlag(tFacProd.getIncrFlag());
		if (tFacProd.getIncrFlag().equals("Y")) {
			tLoanRateChange.setRateIncr(tLoanBorMain.getRateIncr());
			tLoanRateChange.setIndividualIncr(new BigDecimal(0));
		} else {
			tLoanRateChange.setRateIncr(tLoanBorMain.getRateIncr());
			tLoanRateChange.setIndividualIncr(tLoanBorMain.getIndividualIncr());
		}
		tLoanRateChange.setFitRate(tLoanBorMain.getApproveRate());
		tLoanRateChange.setRemark("");
		tLoanRateChange.setAcDate(titaVo.getEntDyI());
		tLoanRateChange.setTellerNo(titaVo.getTlrNo());
		tLoanRateChange.setTxtNo(titaVo.getTxtNo());
	}

	private void SetLoanRateChange2(FacProdStepRate mFacProdStpeRate) throws LogicException {
		this.info("SetLoanRateChange2 ...");
		int wkStartDate;
		int wkEndDate;
		int wkEffectDate;

		// 利率起日
		dDateUtil.init();
		dDateUtil.setMons(mFacProdStpeRate.getMonthStart() - 1);
		dDateUtil.setDate_1(tFacMain.getFirstDrawdownDate()); // 階梯式利率月份以額度初貸日為準
		wkStartDate = dDateUtil.getCalenderDay();
		// 利率起日
		dDateUtil.init();
		dDateUtil.setMons(mFacProdStpeRate.getMonthEnd());
		dDateUtil.setDate_1(tFacMain.getFirstDrawdownDate());
		wkEndDate = dDateUtil.getCalenderDay();

		// 利率起日、利率起日，小於撥款日
		if (wkStartDate < iDrawdownDate && wkEndDate < iDrawdownDate) {
			return;
		}

		// 利率起日小於撥款日=> 撥款日為生效日；否則利率起日為生效日
		if (wkStartDate < iDrawdownDate) {
			wkEffectDate = iDrawdownDate;
		} else {
			wkEffectDate = wkStartDate;
		}
		// 查詢指標利率檔
		CdBaseRate tCdBaseRate = cdBaseRateService.baseRateCodeDescFirst(tLoanBorMain.getCurrencyCode(),
				tFacMain.getBaseRateCode(), 10101, wkEffectDate + 19110000);
		if (tCdBaseRate == null) {
			throw new LogicException(titaVo, "E0001", "指標利率檔"); // 查詢資料不存在
		}

		tLoanRateChangeId = new LoanRateChangeId();
		tLoanRateChangeId.setCustNo(tLoanBorMain.getCustNo());
		tLoanRateChangeId.setFacmNo(tLoanBorMain.getFacmNo());
		tLoanRateChangeId.setBormNo(wkBormNo);
		tLoanRateChangeId.setEffectDate(wkEffectDate);
		tLoanRateChange = new LoanRateChange();
		tLoanRateChange.setCustNo(tLoanBorMain.getCustNo());
		tLoanRateChange.setFacmNo(tLoanBorMain.getFacmNo());
		tLoanRateChange.setBormNo(wkBormNo);
		tLoanRateChange.setEffectDate(wkEffectDate);
		tLoanRateChange.setLoanRateChangeId(tLoanRateChangeId);
		tLoanRateChange.setStatus(0);
		// 指標利率代碼與額度檔相同(01: 保單分紅利率 02: 中華郵政二年期定儲機動利率 99: 自訂利率)
		tLoanRateChange.setBaseRateCode(tFacMain.getBaseRateCode());
		// RateCode 利率區分 (1:機動 2:固定 3:定期機動)，機動的固定利率與撥款主檔不同 (例如：郵局機動利率)
		// RateType 利率型態 1: 固定利率 2: 加碼利率
		if ("1".equals(tLoanBorMain.getRateCode()) && "1".equals(mFacProdStpeRate.getRateType())) {
			tLoanRateChange.setRateCode("2"); // 固定
		} else {
			tLoanRateChange.setRateCode(tLoanBorMain.getRateCode()); // 與撥款主檔相同
		}
		// 固定利率
		if ("1".equals(mFacProdStpeRate.getRateType())) {
			tLoanRateChange.setFitRate(mFacProdStpeRate.getRateIncr());
			tLoanRateChange.setRateIncr(new BigDecimal(0));
			tLoanRateChange.setIndividualIncr(new BigDecimal(0));
		} else
		// 加碼利率
		{
			if (tFacProd.getIncrFlag().equals("Y")) {
				tLoanRateChange.setRateIncr(mFacProdStpeRate.getRateIncr());
				tLoanRateChange.setIndividualIncr(new BigDecimal(0));
			} else {
				tLoanRateChange.setRateIncr(mFacProdStpeRate.getRateIncr());
				tLoanRateChange.setIndividualIncr(mFacProdStpeRate.getRateIncr());
			}
			tLoanRateChange.setFitRate(tCdBaseRate.getBaseRate().add(mFacProdStpeRate.getRateIncr()));
		}
		tLoanRateChange.setIncrFlag(tFacProd.getIncrFlag());
		tLoanRateChange.setProdNo(tFacMain.getProdNo());
		tLoanRateChange.setRemark("");
		tLoanRateChange.setAcDate(titaVo.getEntDyI());
		tLoanRateChange.setTellerNo(titaVo.getTlrNo());
		tLoanRateChange.setTxtNo(titaVo.getTxtNo());
	}
}