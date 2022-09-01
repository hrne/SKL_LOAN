package com.st1.itx.trade.L3;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
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
import com.st1.itx.db.domain.AcDetail;
import com.st1.itx.db.domain.AcReceivable;
import com.st1.itx.db.domain.CdBaseRate;
import com.st1.itx.db.domain.ClFac;
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
import com.st1.itx.db.service.AcReceivableService;
import com.st1.itx.db.service.CdBaseRateService;
import com.st1.itx.db.service.ClFacService;
import com.st1.itx.db.service.FacMainService;
import com.st1.itx.db.service.FacProdService;
import com.st1.itx.db.service.FacProdStepRateService;
import com.st1.itx.db.service.LoanBorMainService;
import com.st1.itx.db.service.LoanBorTxService;
import com.st1.itx.db.service.LoanRateChangeService;
import com.st1.itx.db.service.TxTempService;
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.common.AcDetailCom;
import com.st1.itx.util.common.AcPaymentCom;
import com.st1.itx.util.common.AcReceivableCom;
import com.st1.itx.util.common.AuthLogCom;
import com.st1.itx.util.common.LoanCom;
import com.st1.itx.util.common.LoanDueAmtCom;
import com.st1.itx.util.common.PfDetailCom;
import com.st1.itx.util.common.TxAmlCom;
import com.st1.itx.util.common.data.PfDetailVo;
import com.st1.itx.util.date.DateUtil;
import com.st1.itx.util.format.FormatUtil;
import com.st1.itx.util.parse.Parse;

/*
 * L3100 撥款
 * a.此功能供核貸後,且其相關資料(押品,保證人及法人財務簽證資料)建妥,經主管核准撥款時輸入用.
 * b.撥款方式區分為即時(單筆匯款)及預撥(整批匯款),前者以電話通知匯款,後者以報表及匯款單交出納匯款.
 * c.此交易為2段式交易
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
	public ClFacService clFacService;
	@Autowired
	public AcReceivableService acReceivableService;
	@Autowired
	public L3100Report l3100Report;

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
	@Autowired
	TxAmlCom txAmlCom;

	private TitaVo titaVo = new TitaVo();
	private int iCustNo;
	private int iFacmNo;
	private int iBormNo;
	private int iDrawdownDate;
	private int iMaturityDate;
	private int iTxBormNo;
	private BigDecimal iDrawdownAmt;
	private BigDecimal iAcctFee;
	private BigDecimal iHandlingFee;
	private String iCompensateFlag;
	// work area
	private long sno = 0;
	private int wkBormNo = 0;
	private int wkBorxNo = 0;
	private int wkTbsDy;
	private String sProdStepNo = "";
	private String isReleaseErase = "";
	private int wkStartDate = 0;
	private boolean wkReserve = false;
	private TempVo authLogTempVo = new TempVo();
	private TempVo facTempVo = new TempVo();
	private TempVo tTempVo = new TempVo();
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
	private List<FacProdStepRate> lFacProdStepRate = new ArrayList<FacProdStepRate>();
	private Slice<AcReceivable> slAcReceivable;

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
		iHandlingFee = this.parse.stringToBigDecimal(titaVo.getParam("TimHandlingFee"));
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

		// 需檢查是否建檔
		// 檢查業務科目戶號額度 金額相同者
		slAcReceivable = acReceivableService.useL2062Eq("F12", iCustNo, iFacmNo, iFacmNo, 0, 1, 0, Integer.MAX_VALUE,
				titaVo);

		Boolean acctFeeExist = false;
		Boolean handlingFeeExist = false;
		if (slAcReceivable != null) {
			List<AcReceivable> lAcReceivable = slAcReceivable == null ? null : slAcReceivable.getContent();
			for (AcReceivable t : lAcReceivable) {
				// 檢查金額是否相符
				if (t.getRvAmt().compareTo(iAcctFee) == 0) {
					acctFeeExist = true;
				}
				if (t.getRvAmt().compareTo(iHandlingFee) == 0) {
					handlingFeeExist = true;
				}
				if (acctFeeExist && handlingFeeExist) {
					break;
				}
			}
		}
		if (!acctFeeExist) {
			// 帳管費
			AcctFeeRoutine();
		}
		if (!handlingFeeExist) {
			// 手續費
			HandlingFeeRoutine();
		}

		// 預約撥款到期； 撥款於經辦提交時檢核(Call by ApControl)
		if (wkReserve && titaVo.isHcodeNormal()) {
			txAmlCom.setTxBuffer(this.txBuffer);
			txAmlCom.remitOut(titaVo);
		}

		// 維護撥款匯款檔
		AcPaymentRoutine();

		// 帳務處理
		AcDetailRoutine();

		// 業績處理
		PfDetailRoutine();

		totaVo.put("DPdfSnoF", 0);
		// 登錄修正 撥款傳票主管審核

		if (titaVo.isActfgEntry() && (titaVo.isHcodeNormal() || titaVo.isHcodeModify())) {
//			撥款傳票主管審核
			doRptA(titaVo);
		}
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

		// 登錄時檢查
		if (titaVo.isHcodeNormal()) {

			Slice<ClFac> slClFac = clFacService.facmNoEq(iCustNo, iFacmNo, 0, Integer.MAX_VALUE, titaVo);
			if (slClFac == null) {
				throw new LogicException(titaVo, "E0015", "此額度未關聯擔保品不可撥款"); // 檢查錯誤
			}

			// [繳款方式]為[2.銀扣].[週期基準]為[2.月].[攤還方式]不為[2.到期取息]時檢查指定應繳日是否依設定檔設定
			FacMain tFacMain = facMainService.findById((new FacMainId(iCustNo, iFacmNo)), titaVo);
			if (tFacMain == null) {
				throw new LogicException(titaVo, "E0001", "額度主檔 戶號 = " + iCustNo + " 額度編號 = " + iFacmNo); // 查詢資料不存在
			}
			if (tFacMain.getRepayCode() == 2 && tFacMain.getFreqBase().equals("2")
					&& !tFacMain.getAmortizedCode().equals("2")) {
				authLogTempVo = authLogCom.exec(iCustNo, iFacmNo, titaVo);
				if (authLogTempVo == null) {
					throw new LogicException(titaVo, "E0001", "銀扣授權檔 借款人戶號 = " + iCustNo + " 額度編號 = " + iFacmNo); // 查詢資料不存在
				}
				String wkRepayBank = authLogTempVo.getParam("RepayBank");
				int iSpecificDd = this.parse.stringToInteger(titaVo.getParam("SpecificDd"));
//				檢查ACH扣款特定日
				if (!wkRepayBank.equals("700") && this.txBuffer.getSystemParas().getAchDeductFlag() == 1
						&& !(iSpecificDd == this.txBuffer.getSystemParas().getAchDeductDD1()
								|| iSpecificDd == this.txBuffer.getSystemParas().getAchDeductDD2()
								|| iSpecificDd == this.txBuffer.getSystemParas().getAchDeductDD3()
								|| iSpecificDd == this.txBuffer.getSystemParas().getAchDeductDD4()
								|| iSpecificDd == this.txBuffer.getSystemParas().getAchDeductDD5())) {

					throw new LogicException(titaVo, "E3040", "指定應繳日 = " + iSpecificDd); // 指定應繳日與扣款特定日設定不符
				}
//				檢查郵局扣款特定日
				if (wkRepayBank.equals("700") && this.txBuffer.getSystemParas().getPostDeductFlag() == 1
						&& !(iSpecificDd == this.txBuffer.getSystemParas().getPostDeductDD1()
								|| iSpecificDd == this.txBuffer.getSystemParas().getPostDeductDD2()
								|| iSpecificDd == this.txBuffer.getSystemParas().getPostDeductDD3()
								|| iSpecificDd == this.txBuffer.getSystemParas().getPostDeductDD4()
								|| iSpecificDd == this.txBuffer.getSystemParas().getPostDeductDD5())) {

					throw new LogicException(titaVo, "E3040", "指定應繳日 = " + iSpecificDd); // 指定應繳日與扣款特定日設定不符
				}
			}
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

			loanBorMainService.update(tLoanBorMain, titaVo);
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
			tFacMain = facMainService.findById(new FacMainId(iCustNo, iFacmNo), titaVo);
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
		tFacMain = facMainService.holdById(new FacMainId(iCustNo, iFacmNo), titaVo);
		if (tFacMain == null) {
			throw new LogicException(titaVo, "E0006", "額度主檔 戶號 = " + iCustNo + " 額度編號 = " + iFacmNo); // 鎖定資料時，發生錯誤
		}
		if (tFacMain.getActFg() == 1) {
			throw new LogicException(titaVo, "E0021",
					"額度檔 戶號 = " + tFacMain.getCustNo() + " 額度編號 =  " + tFacMain.getFacmNo()); // 該筆資料待放行中
		}
		wkAvailableAmt = tFacMain.getLineAmt().subtract(tFacMain.getUtilBal()).subtract(wkRvDrawdownAmt);
		if (titaVo.isHcodeNormal()) {

			// 借新還舊(同額度)不檢查動支期限
			this.info("RenewFlag" + titaVo.getParam("RenewFlag") + "," + titaVo.getParam("RpFacmNo1") + "="
					+ titaVo.getParam("FacmNo"));
			if ("2".equals(titaVo.getParam("RenewFlag")) && parse.stringToInteger(titaVo.getParam("RpFacmNo1")) == parse
					.stringToInteger(titaVo.getParam("FacmNo"))) {
				this.info("RenewFlag" + titaVo.getParam("RenewFlag") + "," + titaVo.getParam("RpFacmNo1") + "="
						+ titaVo.getParam("FacmNo"));
			} else {
				// 檢查額度 預約撥款到期執行撥款交易不需更新額度主檔
				if (wkAvailableAmt.compareTo(iDrawdownAmt) < 0) {
					throw new LogicException(titaVo, "E3051", "可用額度=" + wkAvailableAmt + "小於撥款金額" + iDrawdownAmt); // 額度不足撥款金額
				}
				if (tFacMain.getRecycleCode().equals("0") && tFacMain.getUtilDeadline() < wkTbsDy) {
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
			// 借新還舊不更新已動用額度餘額
			if ("2".equals(titaVo.getParam("RenewFlag")) && parse.stringToInteger(titaVo.getParam("RpFacmNo1")) == parse
					.stringToInteger(titaVo.getParam("FacmNo"))) {
			} else {
				tFacMain.setUtilBal(tFacMain.getUtilBal().add(iDrawdownAmt));
			}

			wkBormNo = tFacMain.getLastBormNo() + 1;
			// 新增交易暫存檔
			tTxTemp = new TxTemp();
			tTxTempId = new TxTempId();
			loanCom.setTxTemp(tTxTempId, tTxTemp, iCustNo, iFacmNo, 0, 0, titaVo);
			facTempVo.clear();
			facTempVo.putParam("DrawdownAmt", iDrawdownAmt);
			facTempVo.putParam("FirstDrawdownDate", tFacMain.getFirstDrawdownDate());
			facTempVo.putParam("MaturityDate", tFacMain.getMaturityDate());
			facTempVo.putParam("BormNo", wkBormNo);

			tTxTemp.setText(facTempVo.getJsonString());

			try {
				txTempService.insert(tTxTemp, titaVo);
			} catch (DBException e) {
				throw new LogicException(titaVo, "E0005", "交易暫存檔 Key = " + tTxTempId); // 新增資料時，發生錯誤
			}
			titaVo.putParam("BormNo", wkBormNo);
			// 更新額度資料
			if (tFacMain.getFirstDrawdownDate() == 0 || wkBormNo == 1) {
				tFacMain.setFirstDrawdownDate(iDrawdownDate);
			}
			if (tFacMain.getMaturityDate() == 0 || wkBormNo == 1) {
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
			facTempVo = facTempVo.getVo(tTxTemp.getText());
			wkDrawdownAmt = this.parse.stringToBigDecimal(facTempVo.get("DrawdownAmt"));
			wkFirstDrawdownDate = this.parse.stringToInteger(facTempVo.get("FirstDrawdownDate"));
			wkMaturityDate = this.parse.stringToInteger(facTempVo.get("MaturityDate"));

			wkBormNo = this.parse.stringToInteger(facTempVo.get("BormNo"));

			// 準備更新額度資料
			tFacMain.setUtilAmt(tFacMain.getUtilAmt().subtract(wkDrawdownAmt));

			if ("2".equals(titaVo.getParam("RenewFlag")) && parse.stringToInteger(titaVo.getParam("RpFacmNo1")) == parse
					.stringToInteger(titaVo.getParam("FacmNo"))) {
			} else {
				tFacMain.setUtilBal(tFacMain.getUtilBal().subtract(wkDrawdownAmt));
			}

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
			facTempVo.clear();
			facTempVo.putParam("DrawdownAmt", iDrawdownAmt);
			facTempVo.putParam("FirstDrawdownDate", tFacMain.getFirstDrawdownDate());
			facTempVo.putParam("MaturityDate", tFacMain.getMaturityDate());
			facTempVo.putParam("BormNo", wkBormNo);
			facTempVo.putParam("CompensateFlag", tFacMain.getCompensateFlag());
			tTxTemp.setText(facTempVo.getJsonString());
			this.info("test 5 =" + tFacMain.getCompensateFlag());
			try {
				txTempService.insert(tTxTemp, titaVo);
			} catch (DBException e) {
				throw new LogicException(titaVo, "E0007", "交易暫存檔"); // 更新資料時，發生錯誤
			}
			// 準備更新額度資料
			tFacMain.setUtilAmt(tFacMain.getUtilAmt().add(iDrawdownAmt));
			tFacMain.setUtilBal(tFacMain.getUtilBal().add(iDrawdownAmt));

			if (tFacMain.getFirstDrawdownDate() == 0 || wkBormNo == 1) {
				tFacMain.setFirstDrawdownDate(iDrawdownDate);
			}
			if (tFacMain.getMaturityDate() == 0 || wkBormNo == 1) {
				tFacMain.setMaturityDate(iMaturityDate);
			}
		}

		// MRKEY
		titaVo.putParam("MRKEY", FormatUtil.pad9(String.valueOf(iCustNo), 7) + "-"
				+ FormatUtil.pad9(String.valueOf(iFacmNo), 3) + "-" + FormatUtil.pad9(String.valueOf(wkBormNo), 3));

		// 更新額度檔
		try {
			facMainService.update(tFacMain, titaVo);
		} catch (DBException e) {
			throw new LogicException(titaVo, "E0007", "額度主檔"); // 更新資料時，發生錯誤
		}
	}

	private void LoanBorMainRoutine() throws LogicException {
		this.info("LoanBorMainRoutine ...");
//		this.info("   tFacProd.getCharCode() = " + tFacProd.getCharCode());

		// 登錄
		if (titaVo.isActfgEntry() && titaVo.isHcodeNormal()) {
			tLoanBorTx = loanBorTxService.bormNoDescFirst(iCustNo, iFacmNo, wkBormNo, titaVo);
			if (tLoanBorTx != null) {
				wkBorxNo = tLoanBorTx.getBorxNo() + 1;
			} else {
				wkBorxNo = 1;
			}
			// 新增撥款檔
			tLoanBorMain = new LoanBorMain();
			moveLoanBorMain();
			try {
				loanBorMainService.insert(tLoanBorMain, titaVo);
			} catch (DBException e) {
				throw new LogicException(titaVo, "E3009", "撥款檔 " + e.getErrorMsg()); // 新增資料時，發生錯誤
			}
			// 展期借新還舊記號為0:正常時,撥款日期不可小於本營業日
			if (titaVo.getParam("RenewFlag").equals("0")
					&& parse.stringToInteger(titaVo.getParam("DrawdownDate")) < wkTbsDy) {
				throw new LogicException(titaVo, "E3050", "撥款日期 = " + titaVo.getParam("DrawdownDate")); // 非展期／借新還舊，撥款日期不可小於本營業日
			}
		}
		// 登錄訂正
		if (titaVo.isActfgEntry() && titaVo.isHcodeErase()) {
			// 刪除撥款檔
			tLoanBorMain = loanBorMainService.holdById(new LoanBorMainId(iCustNo, iFacmNo, wkBormNo));
			if (tLoanBorMain == null) {
				throw new LogicException(titaVo, "E0006", "撥款主檔"); // 鎖定資料時，發生錯誤
			}
			wkBorxNo = tLoanBorMain.getLastBorxNo();
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
			if (tLoanBorMain == null) {
				throw new LogicException(titaVo, "E0006", "撥款主檔"); // 鎖定資料時，發生錯誤
			}
			authLogTempVo = authLogCom.exec(iCustNo, iFacmNo, titaVo);
			if (authLogTempVo == null) {
				throw new LogicException(titaVo, "E0001", "銀扣授權檔 借款人戶號 = " + iCustNo + " 額度編號 = " + iFacmNo); // 查詢資料不存在
			}
			wkBorxNo = tLoanBorMain.getLastBorxNo();
			tLoanBorMain = new LoanBorMain();
			moveLoanBorMain();
			tLoanBorTx = loanBorTxService.bormNoDescFirst(iCustNo, iFacmNo, wkBormNo, titaVo);
			// 主管訂正後修正，交易序號+2(訂正、修正)
			if (tLoanBorTx != null && "2".equals(tLoanBorTx.getTitaHCode())) {
				tLoanBorMain.setLastBorxNo(wkBorxNo + 2);
				titaVo.putParam("EraseSupNo", tLoanBorTx.getCorrectSeq().substring(12, 18)); // 主管放訂正主管
			}
			try {
				loanBorMainService.update(tLoanBorMain, titaVo);
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
			wkBorxNo = tLoanBorMain.getLastBorxNo();
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
				txTempService.insert(tTxTemp, titaVo);
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
				loanBorMainService.update(tLoanBorMain, titaVo);
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
			wkBorxNo = tLoanBorMain.getLastBorxNo();
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
				loanBorMainService.update(tLoanBorMain, titaVo);
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
				loanRateChangeService.insert(tLoanRateChange, titaVo);
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
		if (titaVo.isHcodeNormal() || titaVo.isHcodeModify()) {
			this.info("lFacProdStepRate = " + lFacProdStepRate);
			for (FacProdStepRate tFacProdStepRate : lFacProdStepRate) {

				// 利率起日
				dDateUtil.init();
				dDateUtil.setMons(tFacProdStepRate.getMonthStart() - 1);
				dDateUtil.setDate_1(tFacMain.getFirstDrawdownDate()); // 階梯式利率月份以額度初貸日為準
				wkStartDate = dDateUtil.getCalenderDay();
//				2022/05/06會議 階梯式利率月份以額度初貸日為準
				// 利率起日、利率起日，小於撥款日
				// 利率起日小於撥款日=> 撥款日為生效日；否則利率起日為生效日
//				if (wkStartDate <= tLoanBorMain.getDrawdownDate()) {
//					wkEffectDate = tLoanBorMain.getDrawdownDate();
//				} else {
//					wkEffectDate = wkStartDate;
//				}
				SetLoanRateChange2(tFacProdStepRate);
				this.info("tFacProdStepRate = " + tFacProdStepRate);
				this.info("wkStartDate = " + wkStartDate);
				this.info("iDrawdownDate = " + tLoanBorMain.getDrawdownDate());
				this.info("tLoanRateChange.getEffectDate() = " + tLoanRateChange.getEffectDate());
				LoanRateChange t1LoanRateChange = loanRateChangeService.holdById(tLoanRateChangeId, titaVo);
				if (t1LoanRateChange == null) {
					try {
						loanRateChangeService.insert(tLoanRateChange, titaVo);
					} catch (DBException e) {
						throw new LogicException(titaVo, "E0005", "放款利率變動檔 " + e.getErrorMsg()); // 新增資料時，發生錯誤
					}
				} else {
					try {
						loanRateChangeService.update(tLoanRateChange, titaVo);
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
		// 放行時更新主管編號
		if (titaVo.isActfgSuprele()) {
			tLoanBorTx = new LoanBorTx();
			tLoanBorTx = loanBorTxService.holdById(new LoanBorTxId(iCustNo, iFacmNo, wkBormNo, wkBorxNo), titaVo);
			if (tLoanBorTx == null) {
				throw new LogicException(titaVo, "E0006", "放款交易內容檔"); // 鎖定資料時，發生錯誤
			}
			if (titaVo.isHcodeNormal()) {
				tLoanBorTx.setTitaEmpNoS(titaVo.getTlrNo());
			} else {
				// 放行訂正先記錄被沖正，經辦修改或訂正時再覆蓋
				tLoanBorTx.setTitaHCode("2"); // 被沖正
				tLoanBorTx.setCorrectSeq(parse.IntegerToString(titaVo.getEntDyI() + 19110000, 8) + titaVo.getTxSeq());
			}
			try {
				loanBorTxService.update(tLoanBorTx, titaVo);
			} catch (DBException e) {
				throw new LogicException(titaVo, "E0007", "放款交易內容檔 " + e.getErrorMsg()); // 更新資料時，發生錯誤
			}
			this.info("return");
			return;
		}
		// 正常交易
		if (titaVo.isHcodeNormal()) {
			// 新增放款交易內容檔
			tLoanBorTx = new LoanBorTx();
			tLoanBorTxId = new LoanBorTxId();
			loanCom.setLoanBorTx(tLoanBorTx, tLoanBorTxId, iCustNo, iFacmNo, wkBormNo, wkBorxNo, titaVo);
			moveLoanBorTx();
			try {
				loanBorTxService.insert(tLoanBorTx, titaVo);
			} catch (DBException e) {
				throw new LogicException(titaVo, "E0005", "放款交易內容檔 " + e.getErrorMsg()); // 新增資料時，發生錯誤
			}
		}
		// 修正時，如為訂正後修正則需新增沖正交易明細、否則修改交易內容
		if (titaVo.isHcodeModify()) {
			// 修正 更新放款交易內容檔
			tLoanBorTx = loanBorTxService.holdById(new LoanBorTxId(iCustNo, iFacmNo, wkBormNo, wkBorxNo));
			if (tLoanBorTx == null) {
				throw new LogicException(titaVo, "E0006", "放款交易內容檔"); // 鎖定資料時，發生錯誤
			}
			if ("2".equals(tLoanBorTx.getTitaHCode())) {
				// 放行後訂正
				loanCom.setLoanBorTxHcode(iCustNo, iFacmNo, wkBormNo, wkBorxNo, wkBorxNo + 1, iDrawdownAmt, titaVo);
				// 新增放款交易內容檔
				tLoanBorTx = new LoanBorTx();
				tLoanBorTxId = new LoanBorTxId();
				loanCom.setLoanBorTx(tLoanBorTx, tLoanBorTxId, iCustNo, iFacmNo, wkBormNo, wkBorxNo + 2, titaVo);
				moveLoanBorTx();
				try {
					loanBorTxService.insert(tLoanBorTx, titaVo);
				} catch (DBException e) {
					throw new LogicException(titaVo, "E0005", "放款交易內容檔 " + e.getErrorMsg()); // 新增資料時，發生錯誤
				}

			} else {
				moveLoanBorTx();
				try {
					loanBorTxService.update(tLoanBorTx);
				} catch (DBException e) {
					throw new LogicException(titaVo, "E0007", "放款交易內容檔 " + e.getErrorMsg()); // 更新資料時，發生錯誤
				}
			}
		}

		if (titaVo.isHcodeErase()) {
			// 刪除放款交易內容檔
			// 註記交易內容檔
			loanCom.setLoanBorTxHcode(iCustNo, iFacmNo, wkBormNo, wkBorxNo, wkBorxNo + 1, iDrawdownAmt, titaVo);
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
			tAcReceivable.setOpenAcDate(this.parse.stringToInteger(titaVo.getParam("FirstDueDate")));
			lAcReceivable.add(tAcReceivable);
			acReceivableCom.setTxBuffer(this.getTxBuffer());
			acReceivableCom.mnt(0, lAcReceivable, titaVo); // 0-起帳 1-銷帳
		}
	}

//	TODO:手續費
	private void HandlingFeeRoutine() throws LogicException {
		this.info("   HandlingFeeRoutine ...");

		// 手續費
		if (this.titaVo.isActfgRelease() && iHandlingFee.compareTo(BigDecimal.ZERO) > 0) {
			tAcReceivable = new AcReceivable();
			lAcReceivable = new ArrayList<AcReceivable>();
			tAcReceivable.setReceivableFlag(3); // 3-未收費用
			tAcReceivable.setAcctCode("F10"); // F10 帳管費
			tAcReceivable.setCustNo(iCustNo);
			tAcReceivable.setFacmNo(iFacmNo);
			tAcReceivable.setRvNo(FormatUtil.pad9(String.valueOf(wkBormNo), 3) + "-2");
			tAcReceivable.setRvAmt(iHandlingFee);
			tAcReceivable.setOpenAcDate(this.parse.stringToInteger(titaVo.getParam("FirstDueDate")));
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
		acPaymentCom.remit(titaVo);

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
		tLoanBorMain.setHandlingFee(this.parse.stringToBigDecimal(titaVo.getParam("TimHandlingFee")));
		tLoanBorMain.setFinalBal(this.parse.stringToBigDecimal(titaVo.getParam("TimFinalBal")));
		tLoanBorMain.setNotYetFlag(titaVo.getParam("NotYetFlag"));
		tLoanBorMain.setRenewFlag(titaVo.getParam("RenewFlag"));
		tLoanBorMain.setPieceCode(titaVo.getParam("PieceCode"));
		tLoanBorMain.setPieceCodeSecond(titaVo.getParam("PieceCodeSecond"));
		tLoanBorMain.setPieceCodeSecondAmt(parse.stringToBigDecimal(titaVo.getParam("TimPieceCodeSecondAmt")));
		tLoanBorMain.setUsageCode(titaVo.getParam("UsageCode"));
		tLoanBorMain.setSyndNo(this.parse.stringToInteger(titaVo.getParam("SyndNo")));

		tLoanBorMain.setRelationCode(authLogTempVo.getParam("RelationCode"));
		tLoanBorMain.setRelationName(authLogTempVo.getParam("RelationName"));
		tLoanBorMain.setRelationId(authLogTempVo.getParam("RelationId"));
		tLoanBorMain.setRelationBirthday(parse.stringToInteger(authLogTempVo.getParam("RelationBirthday")));
		tLoanBorMain.setRelationGender(authLogTempVo.getParam("RelationGender"));
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
					int paymentBank = parse.stringToInteger(titaVo.getParam("RpRemitBank" + i)) * 10000;
					int paymentBranch = parse.stringToInteger(titaVo.getParam("RpRemitBranch" + i));
					tLoanBorMain.setPaymentBank(parse.IntegerToString(paymentBank + paymentBranch, 7));
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

		if ("1".equals(titaVo.getParam("RenewFlag"))) {
			tLoanBorTx.setDesc("展期");
		} else if ("2".equals(titaVo.getParam("RenewFlag"))) {
			tLoanBorTx.setDesc("借新還舊");
		} else {
			tLoanBorTx.setDesc("撥款");
		}
		tLoanBorTx.setTitaHCode("0");
		tLoanBorTx.setEntryDate(tLoanBorMain.getDrawdownDate());
		tLoanBorTx.setTxAmt(this.parse.stringToBigDecimal(titaVo.getTxAmt()));
		tLoanBorTx.setLoanBal(iDrawdownAmt);
		tLoanBorTx.setRepayCode(tFacMain.getRepayCode());
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
		int wkEffectDate;

		// 利率起日為生效日
		dDateUtil.init();
		dDateUtil.setMons(mFacProdStpeRate.getMonthStart() - 1);
		dDateUtil.setDate_1(tFacMain.getFirstDrawdownDate()); // 階梯式利率月份以額度初貸日為準
		wkEffectDate = dDateUtil.getCalenderDay();

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
		tLoanRateChange.setStatus(2);
		// 固定利率、加碼利率
		if ("1".equals(mFacProdStpeRate.getRateType())) {
			tLoanRateChange.setRateCode("2"); // 固定
			tLoanRateChange.setBaseRateCode("99");
			tLoanRateChange.setFitRate(mFacProdStpeRate.getRateIncr());
			tLoanRateChange.setRateIncr(new BigDecimal(0));
			tLoanRateChange.setIndividualIncr(new BigDecimal(0));
		} else {
			tLoanRateChange.setRateCode(tLoanBorMain.getRateCode()); // 與撥款主檔相同
			tLoanRateChange.setBaseRateCode(tFacMain.getBaseRateCode());
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

	public void doRptA(TitaVo titaVo) throws LogicException {
		this.info("L3100 doRpt started.");
		l3100Report.setTxBuffer(txBuffer);
		String parentTranCode = titaVo.getTxcd();

		l3100Report.setParentTranCode(parentTranCode);

		// 撈資料組報表
		l3100Report.exec(titaVo);

		// 寫產檔記錄到TxReport
		long rptNoA = l3100Report.close();

		// 產生PDF檔案
		l3100Report.toPdf(rptNoA);

		this.info("L3100 doRpt finished.");
		totaVo.put("DPdfSnoF", Long.toString(rptNoA));

	}
}