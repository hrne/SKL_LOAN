package com.st1.itx.trade.L2;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;

import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.TempVo;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.dataVO.TotaVo;
import com.st1.itx.db.domain.AcReceivable;
import com.st1.itx.db.domain.AcReceivableId;
import com.st1.itx.db.domain.CustMain;
import com.st1.itx.db.domain.FacCaseAppl;
import com.st1.itx.db.domain.FacMain;
import com.st1.itx.db.domain.FacMainId;
import com.st1.itx.db.domain.FacProd;
import com.st1.itx.db.domain.FacProdBreach;
import com.st1.itx.db.domain.FacProdStepRate;
import com.st1.itx.db.domain.LoanBorMain;
import com.st1.itx.db.domain.LoanBorMainId;
import com.st1.itx.db.domain.LoanNotYet;
import com.st1.itx.db.service.AcReceivableService;
import com.st1.itx.db.service.CustMainService;
import com.st1.itx.db.service.FacCaseApplService;
import com.st1.itx.db.service.FacMainService;
import com.st1.itx.db.service.FacProdService;
import com.st1.itx.db.service.FacProdStepRateService;
import com.st1.itx.db.service.LoanBorMainService;
import com.st1.itx.db.service.LoanNotYetService;
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.common.AuthLogCom;
import com.st1.itx.util.common.LoanAvailableAmt;
import com.st1.itx.util.format.FormatUtil;
import com.st1.itx.util.parse.Parse;



/**
 * L2R05 尋找額度檔資料 BY 1.CUSTNO + FACMNO or 2.APPLNO
 * 
 * @author iza
 * @version 1.0.0
 */
@Service("L2R05")
@Scope("prototype")
public class L2R05 extends TradeBuffer {

	/* DB服務注入 */
	@Autowired
	public CustMainService custMainService;
	@Autowired
	public FacMainService facMainService;
	@Autowired
	public FacProdService facProdService;
	@Autowired
	public FacProdStepRateService facProdStepRateService;
	@Autowired
	public LoanBorMainService loanBorMainService;
	@Autowired
	public FacCaseApplService facCaseApplService;
	@Autowired
	public AcReceivableService acReceivableService;
	@Autowired
	public LoanNotYetService sLoanNotYetService;
	@Autowired
	public AuthLogCom authLogCom;
	@Autowired
	LoanAvailableAmt loanAvailableAmt;

	@Autowired
	Parse parse;

	private int iFuncCode;
	private String iTxCode;
	private int iFKey;
	private int iCustNo;
	private int iFacmNo;
	private int iApplNo;
	private int iCaseNo;

	// work area
	private int wkCustNo = 0;
	private int wkFacmNo = 0;
	private int wkRvBormNo = 0;
	private int wkNextRvBormNo = 0;
	private int facLastBormNo = 0;
	private int facLastRvBormNo = 0;
	private BigDecimal wkBaseRate = BigDecimal.ZERO;
	private TitaVo titaVo = new TitaVo();
	private String wkCurrencyCode = "TWD";
	private String wkCustId = "";
	private String wkCustName = "";
	private String wkGroupId = "";
	private String wkBreachNo;
	private String sProdNo;
	private String wkCloseFg = "N"; // 未齊件未消註記
	private BigDecimal wkAcctFee = BigDecimal.ZERO;
	private BigDecimal wkRenewBal = BigDecimal.ZERO;
	private FacProd tFacProd;
	private FacMain tFacMain;
	private CustMain tCustMain;
	private Slice<FacProdBreach> slFacProdBreach;
	private Slice<FacProdStepRate> slFacProdStepRate;
	private DecimalFormat df = new DecimalFormat("##,###,###,###,##0");
	private int FirstAdjRateDate = 0;
	private TempVo tempVo = new TempVo();

	private String wkRepayBank = "";
	private String wkRepayAcctNo = "";
	private String wkPostCode = "";
	private String wkAchAuthCode = "";
	private String wkRelationCode = "";
	private String wkRelationName = "";
	private String wkRelationId = "";
	private int wkRelationBirthday = 0;
	private String wkRelationGender = "";
	private int wkRvCnt = 0;
	private BigDecimal wkRvDrawdownAmt = new BigDecimal(0);
	private AcReceivable tAcReceivable;

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active L2R05 ");
		loanAvailableAmt.setTxBuffer(this.txBuffer);

		this.totaVo.init(titaVo);
		this.titaVo = titaVo;
		initset();

		// 取得輸入資料
		iFuncCode = this.parse.stringToInteger(titaVo.getParam("RimFuncCode"));
		iTxCode = titaVo.getParam("RimTxCode");
		iFKey = this.parse.stringToInteger(titaVo.getParam("RimFKey"));
		iCustNo = this.parse.stringToInteger(titaVo.getParam("RimCustNo"));
		iFacmNo = this.parse.stringToInteger(titaVo.getParam("RimFacmNo"));
		iApplNo = this.parse.stringToInteger(titaVo.getParam("RimApplNo"));
		iCaseNo = this.parse.stringToInteger(titaVo.getParam("RimCaseNo"));

		List<LoanNotYet> lLoanNotYet = new ArrayList<LoanNotYet>();

		// 檢查輸入資料
		if (iTxCode.isEmpty()) {
			throw new LogicException(titaVo, "E0009", ""); // 交易代號不可為空白
		}
		if (!(iFuncCode == 1 || iFuncCode == 2 || iFuncCode == 4 || iFuncCode == 5)) {
			throw new LogicException(titaVo, "E0010", ""); // 功能選擇錯誤
		}
		if (iApplNo == 0 && iCustNo == 0 && iCaseNo == 0) {
			throw new LogicException(titaVo, "E0019", " 核准號碼, 借款人戶號, 案件編號不可皆為0"); // 輸入資料錯誤
		}

		// 查詢額度主檔
		if (iApplNo > 0) {
			tFacMain = facMainService.facmApplNoFirst(iApplNo, titaVo);
			if (tFacMain == null) {
				if (iTxCode.equals("L2118") & iFuncCode == 5) {

					this.addList(this.totaVo);
					return this.sendList();
				}
				throw new LogicException(titaVo, "E0001", " 額度主檔 核准編號 = " + iApplNo); // 查詢資料不存在
			}
			if (iTxCode.equals("L2118") & iFuncCode == 1) {
				if (tFacMain.getLastBormNo() > 0) {
					throw new LogicException(titaVo, "E2066", " 核准號碼  = " + tFacMain.getApplNo()); // 已撥款不可再建檔
				}
			}
		} else {
			if (iCustNo > 0) {
				tFacMain = facMainService.findById(new FacMainId(iCustNo, iFacmNo), titaVo);
				if (tFacMain == null) {
					throw new LogicException(titaVo, "E0001", " 額度主檔 借款人戶號 = " + iCustNo + " 額度編號 = " + iFacmNo); // 查詢資料不存在
				}
			}
			if (iCaseNo > 0) {
				tFacMain = facMainService.facmCreditSysNoFirst(iCaseNo, iCaseNo, iFacmNo, iFacmNo, titaVo);
				if (tFacMain == null) {
					throw new LogicException(titaVo, "E0001", " 額度主檔 案件編號 = " + iCaseNo + " 額度編號 = " + iFacmNo); // 查詢資料不存在
				}
			}
		}
		// 展期檢查
		if ((iTxCode.equals("L3410") || iTxCode.equals("L3420")) && iCustNo > 0) {
			if (titaVo.get("NewApplNo") != null) {
				int iNewApplNo = parse.stringToInteger(titaVo.get("NewApplNo"));
				FacMain tNewFacMain = facMainService.facmApplNoFirst(iNewApplNo, titaVo);
				if (tNewFacMain == null) {
					throw new LogicException(titaVo, "E0001", " 額度主檔 核准編號 = " + iNewApplNo); // 查詢資料不存在
				}
				if (iCustNo != tNewFacMain.getCustNo()) {
					throw new LogicException(titaVo, "E3077", " 新核准號碼 戶號 = " + tNewFacMain.getCustNo()); // 新核准號碼與原核准號碼，非屬於同一人
				}
			}
		}

		wkCustNo = tFacMain.getCustNo();
		wkFacmNo = tFacMain.getFacmNo();

		// 查詢商品參數檔
		tFacProd = facProdService.findById(tFacMain.getProdNo(), titaVo);
		if (tFacProd == null) {
			throw new LogicException(titaVo, "E0001", "商品參數檔"); // 查詢資料不存在
		}

		if (tFacMain.getRepayCode() == 02) {
			tempVo = authLogCom.exec(wkCustNo, wkFacmNo, titaVo);
			if (tempVo == null) {
				throw new LogicException(titaVo, "E0001", " 額度主檔 借款人戶號 = " + wkCustNo + " 額度編號 = " + wkFacmNo); // 查詢資料不存在
			}
			wkRepayBank = tempVo.getParam("RepayBank");
			wkRepayAcctNo = tempVo.getParam("RepayAcctNo");
			wkPostCode = tempVo.getParam("PostCode");
			wkAchAuthCode = tempVo.getParam("AchAuthCode");
			wkRelationCode = tempVo.getParam("RelationCode");
			wkRelationName = tempVo.getParam("RelationName");
			wkRelationId = tempVo.getParam("RelationId");
			wkRelationBirthday = parse.stringToInteger(tempVo.getParam("RelationBirthday"));
			wkRelationGender = tempVo.getParam("RelationGender");

		}

		this.info("tempVo =" + tempVo);

		if (tFacMain.getActFg() == 1 && iFKey == 0) {
			throw new LogicException(titaVo, "E0021",
					"額度檔 戶號 = " + tFacMain.getCustNo() + " 額度編號 =  " + tFacMain.getFacmNo()); // 該筆資料待放行中
		}
		if (iTxCode.equals("L3100") || iTxCode.equals("L3110")) {

			if (!tFacMain.getL9110Flag().equals("Y")) {
				throw new LogicException(titaVo, "E3083", "額度主檔 戶號 = " + iCustNo + " 額度編號 = " + iFacmNo); // 撥款審核資料表尚未列印，請先作L9110交易
			}
			if (iFKey != 7) {
				if ((tFacMain.getLineAmt().compareTo(tFacMain.getUtilBal()) <= 0)
						|| (tFacMain.getRecycleCode().equals("0")
								&& tFacMain.getUtilDeadline() < this.txBuffer.getTxCom().getTbsdy())
						|| (tFacMain.getRecycleCode().equals("1")
								&& tFacMain.getRecycleDeadline() < this.txBuffer.getTxCom().getTbsdy())) {
					// TODO:檢查此額度是否為借新還舊
					tAcReceivable = acReceivableService.findById(new AcReceivableId("TRO", iCustNo, iFacmNo,
							"FacmNo" + StringUtils.leftPad(String.valueOf(iFacmNo), 3, "0")), titaVo);
					if (tAcReceivable == null) {
						throw new LogicException(titaVo, "E2072",
								" 額度主檔 借款人戶號 = " + tFacMain.getCustNo() + " 額度編號 = " + tFacMain.getFacmNo() + " 核准額度 = "
										+ df.format(tFacMain.getLineAmt()) + " 已動用額度餘額 = "
										+ df.format(tFacMain.getUtilBal())); // 該筆額度編號沒有可用額度
					}
					wkRenewBal = tAcReceivable.getAcBal();
				}
			}
		}

		tCustMain = custMainService.custNoFirst(tFacMain.getCustNo(), tFacMain.getCustNo(), titaVo);
		if (tCustMain == null) {
			throw new LogicException(titaVo, "E2003", " 客戶資料主檔 UKey=" + tFacMain.getCustNo()); // 查無資料
		}
		wkCustId = tCustMain.getCustId();
		wkCustName = tCustMain.getCustName();
		// 查詢案件申請檔
		FacCaseAppl tFacCaseAppl = facCaseApplService.findById(tFacMain.getApplNo(), titaVo);
		if (tFacCaseAppl != null) {
			tCustMain = custMainService.findById(tFacCaseAppl.getCustUKey(), titaVo);
			if (tCustMain != null) {
				wkGroupId = tCustMain.getCustId();
			}
		}

		// 設定第幾分頁 titaVo.getReturnIndex() 第一次會是0，如果需折返最後會塞值
		this.index = titaVo.getReturnIndex();

		// 設定每筆分頁的資料筆數 預設500筆 總長不可超過六萬
		this.limit = 500;

		wkBreachNo = FormatUtil.pad9(String.valueOf(tFacMain.getCustNo()), 7)
				+ FormatUtil.pad9(String.valueOf(tFacMain.getFacmNo()), 3);
		// 檢查是否未齊件
		Slice<LoanNotYet> slLoanNotYet = sLoanNotYetService.notYetCustNoEq(iCustNo, iFacmNo, iFacmNo, 0, 99991231, 0,
				99991231, this.index, this.limit, titaVo);
		lLoanNotYet = slLoanNotYet == null ? null : slLoanNotYet.getContent();
		if (lLoanNotYet != null) {
			for (LoanNotYet tLoanNotYet : lLoanNotYet) {
				if (tLoanNotYet.getCloseDate() == 0) {
					wkCloseFg = "Y";
				}
			}
		}

		// 預定撥款序號 (LoanBorMain戶況為99筆數)+(FacMain最後撥款序號)+1
		List<LoanBorMain> lLoanBorMain = new ArrayList<LoanBorMain>();
		Slice<LoanBorMain> slLoanBorMain = loanBorMainService.findStatusEq(Arrays.asList(99), iCustNo, iFacmNo, iFacmNo,
				0, Integer.MAX_VALUE, titaVo);
		if (slLoanBorMain != null) {
			for (LoanBorMain rv : slLoanBorMain.getContent()) {
				wkRvCnt++;
				wkRvDrawdownAmt = wkRvDrawdownAmt.add(rv.getDrawdownAmt());
			}
		}
		if (iTxCode.equals("L3100") || iTxCode.equals("L3110")) {
			if (iFKey != 7) {
				if (tFacMain.getLineAmt().compareTo(tFacMain.getUtilBal().add(wkRvDrawdownAmt)) <= 0) {
					// TODO:檢查此額度是否為借新還舊
					tAcReceivable = acReceivableService.findById(new AcReceivableId("TRO", iCustNo, iFacmNo,
							"FacmNo" + StringUtils.leftPad(String.valueOf(iFacmNo), 3, "0")), titaVo);
					if (tAcReceivable == null) {
						throw new LogicException(titaVo, "E2072", " 額度主檔 借款人戶號 = " + tFacMain.getCustNo() + " 額度編號 = "
								+ tFacMain.getFacmNo() + " 核准額度 = " + df.format(tFacMain.getLineAmt()) + " 已動用額度餘額 = "
								+ df.format(tFacMain.getUtilBal()) + " 已預約撥款金額 = " + df.format(wkRvDrawdownAmt)); // 該筆額度編號沒有可用額度
					}
					wkRenewBal = tAcReceivable.getAcBal();
				}
			}
		}
		facLastBormNo = tFacMain.getLastBormNo();
		facLastRvBormNo = tFacMain.getLastBormRvNo();
		wkRvBormNo = wkRvCnt + facLastBormNo + 1;
		wkNextRvBormNo = facLastRvBormNo + 1;
		this.info("tFacMain.getLastBormRvNo() = " + tFacMain.getLastBormRvNo());
		this.info("wkNextRvBormNo = " + wkNextRvBormNo);
		// 登錄取首筆撥款下次利率調整日期
		if (tFacMain.getLastBormNo() >= 1) {
			this.info("登錄取首筆撥款下次利率調整日期");
			LoanBorMain tLoanBorMain = loanBorMainService.findById(new LoanBorMainId(iCustNo, iFacmNo, 1), titaVo);
			if (tLoanBorMain != null) {
				FirstAdjRateDate = tLoanBorMain.getNextAdjRateDate();
			}

		}

		// 指標利率
		if (tFacMain.getBaseRateCode().equals("99")) {
			wkBaseRate = BigDecimal.ZERO;
		} else {
			if (tFacMain.getIndividualIncr().equals(BigDecimal.ZERO)) {
				wkBaseRate = tFacMain.getApproveRate().subtract(tFacMain.getRateIncr());
			} else {
				wkBaseRate = tFacMain.getApproveRate().subtract(tFacMain.getIndividualIncr());
			}
		}

		MoveTotaFacMain();

		PrevIntDateRoutine();

		this.totaVo.putParam("L2r05BaseRate", wkBaseRate); // 指標利率
		this.totaVo.putParam("L2r05CloseFg", wkCloseFg); // 未齊件註記
		this.totaVo.putParam("L2r05RvBormNo", wkRvBormNo); // 預定撥款序號
		this.totaVo.putParam("L2r05NextRvBormNo", wkNextRvBormNo); // 預定撥款序號
		this.totaVo.putParam("L2r05RvDrawdownAmt", wkRvDrawdownAmt); // 已預約撥款金額
		for (int i = 1; i <= 10; i++) {
			this.totaVo.putParam("L2r05StepRateMonths" + i, 0);
			this.totaVo.putParam("L2r05StepRateMonthE" + i, 0);
			this.totaVo.putParam("L2r05StepRateType" + i, 0);
			this.totaVo.putParam("L2r05StepRateIncr" + i, 0);
		}

		sProdNo = FormatUtil.pad9(String.valueOf(tFacMain.getCustNo()), 7)
				+ FormatUtil.pad9(String.valueOf(tFacMain.getFacmNo()), 3);

		// 查詢階梯式利率
		slFacProdStepRate = facProdStepRateService.stepRateProdNoEq(sProdNo, 0, 999, this.index, this.limit, titaVo);

		if (slFacProdStepRate != null && slFacProdStepRate.getContent().size() > 1) {
			SetTotaStepRated();
		}

		this.info("tota = " + this.totaVo);

		this.addList(this.totaVo);
		return this.sendList();
	}

	// 額度主檔
	private void MoveTotaFacMain() throws LogicException {
		wkAcctFee = tFacMain.getAcctFee();
		if ((iTxCode.equals("L3100") || iTxCode.equals("L3110"))
				&& (tFacMain.getLastBormNo() > 0 || tFacMain.getLastBormRvNo() > 900)) {
			wkAcctFee = BigDecimal.ZERO;
		}
		this.info("tempVo =   " + tempVo);
		this.totaVo.putParam("L2r05CustId", wkCustId);
		this.totaVo.putParam("L2r05CustName", wkCustName);
		this.totaVo.putParam("L2r05CustNo", tFacMain.getCustNo());
		this.totaVo.putParam("L2r05FacmNo", tFacMain.getFacmNo());
		this.totaVo.putParam("L2r05ApplNo", tFacMain.getApplNo());
		this.totaVo.putParam("L2r05ProdNo", tFacMain.getProdNo());
		this.totaVo.putParam("L2r05BaseRateCode", tFacMain.getBaseRateCode());
		this.totaVo.putParam("L2r05IncrFlag", tFacProd.getIncrFlag());
		this.totaVo.putParam("L2r05RateIncr", tFacMain.getRateIncr());
		this.totaVo.putParam("L2r05IndividualIncr", tFacMain.getIndividualIncr());
		this.totaVo.putParam("L2r05ApproveRate", tFacMain.getApproveRate());
		this.totaVo.putParam("L2r05RateCode", tFacMain.getRateCode());
		this.totaVo.putParam("L2r05FirstRateAdjFreq", tFacMain.getFirstRateAdjFreq());
		this.totaVo.putParam("L2r05FirstAdjRateDate", FirstAdjRateDate);

		this.totaVo.putParam("L2r05RateAdjFreq", tFacMain.getRateAdjFreq());
		this.totaVo.putParam("L2r05CurrencyCode", tFacMain.getCurrencyCode());
		this.totaVo.putParam("L2r05LineAmt", tFacMain.getLineAmt());
		this.totaVo.putParam("L2r05UtilAmt", tFacMain.getUtilAmt());
		this.totaVo.putParam("L2r05UtilBal", tFacMain.getUtilBal());
		this.totaVo.putParam("L2r05AcctCode", tFacMain.getAcctCode());
		this.totaVo.putParam("L2r05LoanTermYy", tFacMain.getLoanTermYy());
		this.totaVo.putParam("L2r05LoanTermMm", tFacMain.getLoanTermMm());
		this.totaVo.putParam("L2r05LoanTermDd", tFacMain.getLoanTermDd());
		this.totaVo.putParam("L2r05FirstDrawdownDate", tFacMain.getFirstDrawdownDate());
		this.totaVo.putParam("L2r05MaturityDate", tFacMain.getMaturityDate());
		this.totaVo.putParam("L2r05IntCalcCode", tFacMain.getIntCalcCode());
		this.totaVo.putParam("L2r05AmortizedCode", tFacMain.getAmortizedCode());
		this.totaVo.putParam("L2r05FreqBase", tFacMain.getFreqBase());
		this.totaVo.putParam("L2r05PayIntFreq", tFacMain.getPayIntFreq());
		this.totaVo.putParam("L2r05RepayFreq", tFacMain.getRepayFreq());
		this.totaVo.putParam("L2r05UtilDeadline", tFacMain.getUtilDeadline());
		this.totaVo.putParam("L2r05GracePeriod", tFacMain.getGracePeriod());
		this.totaVo.putParam("L2r05AcctFee", wkAcctFee);
		this.totaVo.putParam("L2r05HandlingFee", tFacMain.getHandlingFee());
//		this.totaVo.putParam("L2r05DuePayAmt", tFacMain.getDuePayAmt());
//		this.totaVo.putParam("L2r05DuePayLimit", tFacMain.getDuePayLimit());
//		this.totaVo.putParam("L2r05PayIntLimit", tFacMain.getPayIntLimit());
		this.totaVo.putParam("L2r05ExtraRepayCode", tFacMain.getExtraRepayCode());
		this.totaVo.putParam("L2r05CustTypeCode", tFacMain.getCustTypeCode());
		this.totaVo.putParam("L2r05RuleCode", tFacMain.getRuleCode());
		this.totaVo.putParam("L2r05RecycleCode", tFacMain.getRecycleCode());
		this.totaVo.putParam("L2r05RecycleDeadline", tFacMain.getRecycleDeadline());
		this.totaVo.putParam("L2r05UsageCode", tFacMain.getUsageCode());
		this.totaVo.putParam("L2r05DepartmentCode", tFacMain.getDepartmentCode());
		this.totaVo.putParam("L2r05IncomeTaxFlag", tFacMain.getIncomeTaxFlag());
		this.totaVo.putParam("L2r05CompensateFlag", tFacMain.getCompensateFlag());
		this.totaVo.putParam("L2r05IrrevocableFlag", tFacMain.getIrrevocableFlag());
		this.totaVo.putParam("L2r05PieceCode", tFacMain.getPieceCode());
		this.totaVo.putParam("L2r05RateAdjNoticeCode", tFacMain.getRateAdjNoticeCode());
		this.totaVo.putParam("L2r05RepayCode", tFacMain.getRepayCode());
		this.totaVo.putParam("L2r05RepayBank", wkRepayBank);
		this.totaVo.putParam("L2r05RepayAcctNo", wkRepayAcctNo);
		this.totaVo.putParam("L2r05PostCode", wkPostCode);
		this.totaVo.putParam("L2r05Introducer", tFacMain.getIntroducer());
		this.totaVo.putParam("L2r05District", tFacMain.getDistrict());
		this.totaVo.putParam("L2r05FireOfficer", tFacMain.getFireOfficer());
		this.totaVo.putParam("L2r05Estimate", tFacMain.getEstimate());
		this.totaVo.putParam("L2r05CreditOfficer", tFacMain.getCreditOfficer());
		this.totaVo.putParam("L2r05LoanOfficer", tFacMain.getLoanOfficer());
		this.totaVo.putParam("L2r05BusinessOfficer", tFacMain.getBusinessOfficer());
		this.totaVo.putParam("L2r05ApprovedLevel", tFacMain.getApprovedLevel());
		this.totaVo.putParam("L2r05Supervisor", tFacMain.getSupervisor());
		this.totaVo.putParam("L2r05InvestigateOfficer", tFacMain.getInvestigateOfficer());
		this.totaVo.putParam("L2r05EstimateReview", tFacMain.getEstimateReview());
		this.totaVo.putParam("L2r05Coorgnizer", tFacMain.getCoorgnizer());
		this.totaVo.putParam("L2r05GroupId", wkGroupId);
		this.totaVo.putParam("L2r05AdvanceCloseCode", tFacMain.getAdvanceCloseCode());

		this.totaVo.putParam("L2r05ProdBreachFlag", tFacMain.getProdBreachFlag());
		this.totaVo.putParam("L2r05BreachCode", tFacMain.getBreachCode());
		this.totaVo.putParam("L2r05BreachGetCode", tFacMain.getBreachGetCode());

		this.totaVo.putParam("L2r05Breach", tFacMain.getBreachDescription());
		if (tFacMain.getBreachDescription().isEmpty()) {
			this.totaVo.putParam("L2r05Breach", getBreachDescription(tFacMain, titaVo));
		} else {
			this.totaVo.putParam("L2r05Breach", tFacMain.getBreachDescription());
		}
		this.totaVo.putParam("L2r05BreachFlag", tFacMain.getBreachFlag());
		this.totaVo.putParam("L2r05ProhibitMonth", tFacMain.getProhibitMonth());
		this.totaVo.putParam("L2r05BreachPercent", tFacMain.getBreachPercent());
		this.totaVo.putParam("L2r05BreachDecreaseMonth", tFacMain.getBreachDecreaseMonth());
		this.totaVo.putParam("L2r05BreachDecrease", tFacMain.getBreachDecrease());
		this.totaVo.putParam("L2r05BreachStartPercent", tFacMain.getBreachStartPercent());

		this.totaVo.putParam("L2r05CreditScore", tFacMain.getCreditScore());
		this.totaVo.putParam("L2r05GuaranteeDate", tFacMain.getGuaranteeDate());
		this.totaVo.putParam("L2r05ContractNo", tFacMain.getContractNo());
		this.totaVo.putParam("L2r05AchAuthCode", wkAchAuthCode);
		this.totaVo.putParam("L2r05CreditSysNo", tFacMain.getCreditSysNo());
		this.totaVo.putParam("L2r05RelationCode", wkRelationCode);
		this.totaVo.putParam("L2r05RelationName", wkRelationName);
		this.totaVo.putParam("L2r05RelationId", wkRelationId);
		this.totaVo.putParam("L2r05RelationBirthday", wkRelationBirthday);
		this.totaVo.putParam("L2r05RelationGender", wkRelationGender);
		this.totaVo.putParam("L2r05PrevPayIntDate", 0);
		this.totaVo.putParam("L2r05NextPayIntDate", 0);
		this.totaVo.putParam("L2r05SpecificDd", 0);
		this.totaVo.putParam("L2r05SpecificDate", 0);
		this.totaVo.putParam("L2r05BormCurrencyCode", wkCurrencyCode);
		this.totaVo.putParam("L2r05LoanBal", 0);
		this.totaVo.putParam("L2r05BormCount", 0);
		this.totaVo.putParam("L2r05BormNo", tFacMain.getLastBormNo());
		this.totaVo.putParam("L2r05DueAmt", 0);
//		this.totaVo.putParam("L2r05CancelCode", tFacMain.getCancelCode());
		// 可用額度=Min(共用額度「可用總額度/循環動用」,Min(額度核准金額,擔保品分配金額) -已動用額度餘額))
		BigDecimal wkAvailableAmt = loanAvailableAmt.caculate(tFacMain, titaVo); // 可用額度
		// 限額計算方式 F-核准額度, C-擔保品 S-合併額度控管
		String wkLimitFlag = loanAvailableAmt.getLimitFlag();
		BigDecimal wkAvailableCl = loanAvailableAmt.getAvailableCl();
		// 借新還舊處理
		this.totaVo.putParam("L2r05AvailableAmt", wkAvailableAmt.add(wkRenewBal));
		this.totaVo.putParam("L2r05AvailableCl", wkAvailableCl);

		this.totaVo.putParam("L2r05RenewFlag", wkRenewBal.compareTo(BigDecimal.ZERO) > 0 ? "Y" : "N");
		this.totaVo.putParam("L2r05LimitFlag", wkLimitFlag);

		// 綠色授信
		this.totaVo.putParam("L2r05Grcd", tFacMain.getGrcd());
		this.totaVo.putParam("L2r05GrKind", tFacMain.getGrKind());
		this.totaVo.putParam("L2r05EsGcd", tFacMain.getEsGcd());
		this.totaVo.putParam("L2r05EsGKind", tFacMain.getEsGKind());
		this.totaVo.putParam("L2r05EsGcnl", tFacMain.getEsGcnl());
		// 購地貸款相關
		if (tFacMain.getPreStarBuildingYM() > 0) {
			this.totaVo.putParam("L2r05PreStarBuildingYM", tFacMain.getPreStarBuildingYM() - 191100);
		} else {
			this.totaVo.putParam("L2r05PreStarBuildingYM", tFacMain.getPreStarBuildingYM());
		}
		if (tFacMain.getStarBuildingYM() > 0) {
			this.totaVo.putParam("L2r05StarBuildingYM", tFacMain.getStarBuildingYM() - 191100);
		} else {
			this.totaVo.putParam("L2r05StarBuildingYM", tFacMain.getStarBuildingYM());
		}

	}

	private void PrevIntDateRoutine() throws LogicException {
		this.info("PrevIntDateRoutine ...");

		int wkPrevPayIntDate = 0;
		int wkNextPayIntDate = 0;
		int wkSpecificDd = 0;
		int wkSpecificDate = 0;
		int wkBormCount = 0;
		int wkBormNo = 0;
		int wkDate = 0;
		BigDecimal wkLoanBal = BigDecimal.ZERO;
		BigDecimal wkDueAmt = BigDecimal.ZERO;

		// 設定第幾分頁 titaVo.getReturnIndex() 第一次會是0，如果需折返最後會塞值
		this.index = titaVo.getReturnIndex();

		// 設定每筆分頁的資料筆數 預設500筆 總長不可超過六萬
		this.limit = Integer.MAX_VALUE;

		Slice<LoanBorMain> lLoanBorMain = loanBorMainService.bormCustNoEq(wkCustNo, wkFacmNo, wkFacmNo, 1, 900,
				this.index, this.limit, titaVo);
		if (!(lLoanBorMain == null || lLoanBorMain.isEmpty())) {
			for (LoanBorMain ln : lLoanBorMain.getContent()) {
				if (ln.getStatus() == 0 || ln.getStatus() == 4) { // 0: 正常戶 4: 逾期戶
					wkDate = ln.getPrevPayIntDate() == 0 ? ln.getDrawdownDate() : ln.getPrevPayIntDate();
					if (wkDate > wkPrevPayIntDate) { // && ln.getPrevPayIntDate() < wkTbsDy) {
						wkPrevPayIntDate = wkDate;
						wkNextPayIntDate = ln.getNextPayIntDate();
						wkSpecificDd = ln.getSpecificDd();
						wkSpecificDate = ln.getSpecificDate();
						wkCurrencyCode = ln.getCurrencyCode();
					}
					wkLoanBal = wkLoanBal.add(ln.getLoanBal());
					wkDueAmt = wkDueAmt.add(ln.getDueAmt());
					wkBormCount++;
				}
				this.info("   wkBormCount = " + wkBormCount);
			}
			this.totaVo.putParam("L2r05PrevPayIntDate", wkPrevPayIntDate);
			this.totaVo.putParam("L2r05NextPayIntDate", wkNextPayIntDate);
			this.totaVo.putParam("L2r05SpecificDd", wkSpecificDd);
			this.totaVo.putParam("L2r05SpecificDate", wkSpecificDate);
			this.totaVo.putParam("L2r05BormCurrencyCode", wkCurrencyCode);
			this.totaVo.putParam("L2r05LoanBal", wkLoanBal);
			this.totaVo.putParam("L2r05BormCount", wkBormCount);
			this.totaVo.putParam("L2r05DueAmt", wkDueAmt);
		}
		this.info("   wkBormCount = " + wkBormCount);
		if (wkBormCount == 0) {
			if (iTxCode.equals("L3711") || iTxCode.equals("L3712")) {
				throw new LogicException(titaVo, "E0001", " 查無該額度的最近繳息日"); // 查詢資料不存在
			}
		}

		this.info("PrevIntDateRoutine end");
		this.info("   wkPrevPayIntDate = " + wkPrevPayIntDate);
		this.info("   wkBormCount = " + wkBormCount);
	}

	// 階梯式利率
	private void SetTotaStepRated() throws LogicException {

		int i = 1;
		for (FacProdStepRate tFacProdStepRate : slFacProdStepRate.getContent()) {
			this.totaVo.putParam("L2r05StepRateMonths" + i, tFacProdStepRate.getMonthStart());
			this.totaVo.putParam("L2r05StepRateMonthE" + i, tFacProdStepRate.getMonthEnd());
			this.totaVo.putParam("L2r05StepRateType" + i, tFacProdStepRate.getRateType());
			this.totaVo.putParam("L2r05StepRateIncr" + i, tFacProdStepRate.getRateIncr());
			i++;
		}
	}

	private void initset() throws LogicException {

		this.totaVo.putParam("L2r05CustId", "");
		this.totaVo.putParam("L2r05CustNo", 0);
		this.totaVo.putParam("L2r05FacmNo", 0);
		this.totaVo.putParam("L2r05ApplNo", 0);
		this.totaVo.putParam("L2r05ProdNo", "");
		this.totaVo.putParam("L2r05BaseRateCode", "");
		this.totaVo.putParam("L2r05RateIncr", 0);
		this.totaVo.putParam("L2r05IndividualIncr", 0);
		this.totaVo.putParam("L2r05ApproveRate", 0);
		this.totaVo.putParam("L2r05RateCode", "");
		this.totaVo.putParam("L2r05FirstRateAdjFreq", 0);
		this.totaVo.putParam("L2r05FirstAdjRateDate", 0);
		this.totaVo.putParam("L2r05RateAdjFreq", 0);
		this.totaVo.putParam("L2r05CurrencyCode", "");
		this.totaVo.putParam("L2r05LineAmt", 0);
		this.totaVo.putParam("L2r05UtilAmt", 0);
		this.totaVo.putParam("L2r05UtilBal", 0);
		this.totaVo.putParam("L2r05AcctCode", "");
		this.totaVo.putParam("L2r05LoanTermYy", 0);
		this.totaVo.putParam("L2r05LoanTermMm", 0);
		this.totaVo.putParam("L2r05LoanTermDd", 0);
		this.totaVo.putParam("L2r05FirstDrawdownDate", 0);
		this.totaVo.putParam("L2r05MaturityDate", 0);
		this.totaVo.putParam("L2r05IntCalcCode", "");
		this.totaVo.putParam("L2r05AmortizedCode", "");
		this.totaVo.putParam("L2r05FreqBase", "");
		this.totaVo.putParam("L2r05PayIntFreq", "");
		this.totaVo.putParam("L2r05RepayFreq", "");
		this.totaVo.putParam("L2r05UtilDeadline", "");
		this.totaVo.putParam("L2r05GracePeriod", 0);
		this.totaVo.putParam("L2r05AcctFee", 0);
		this.totaVo.putParam("L2r05HandlingFee", 0);
		this.totaVo.putParam("L2r05ExtraRepayCode", "");
		this.totaVo.putParam("L2r05CustomerCode", "");
		this.totaVo.putParam("L2r05RuleCode", "");
		this.totaVo.putParam("L2r05RecycleCode", "");
		this.totaVo.putParam("L2r05RecycleDeadline", "");
		this.totaVo.putParam("L2r05UsageCode", "");
		this.totaVo.putParam("L2r05DepartmentCode", "");
		this.totaVo.putParam("L2r05IncomeTaxFlag", "");
		this.totaVo.putParam("L2r05CompensateFlag", "");
		this.totaVo.putParam("L2r05IrrevocableFlag", "");
		this.totaVo.putParam("L2r05PieceCode", "");
		this.totaVo.putParam("L2r05RateAdjNoticeCode", "");
		this.totaVo.putParam("L2r05RepayCode", "");
		this.totaVo.putParam("L2r05RepayBank", "");
		this.totaVo.putParam("L2r05RepayAcctNo", "");
		this.totaVo.putParam("L2r05PostCode", "");
		this.totaVo.putParam("L2r05Introducer", "");
		this.totaVo.putParam("L2r05District", "");
		this.totaVo.putParam("L2r05FireOfficer", "");
		this.totaVo.putParam("L2r05Estimate", "");
		this.totaVo.putParam("L2r05CreditOfficer", "");
		this.totaVo.putParam("L2r05LoanOfficer", "");
		this.totaVo.putParam("L2r05BusinessOfficer", "");
		this.totaVo.putParam("L2r05ApprovedLevel", "");
		this.totaVo.putParam("L2r05Supervisor", "");
		this.totaVo.putParam("L2r05InvestigateOfficer", "");
		this.totaVo.putParam("L2r05EstimateReview", "");
		this.totaVo.putParam("L2r05Coorgnizer", "");
		this.totaVo.putParam("L2r05GroupId", "");
		this.totaVo.putParam("L2r05AdvanceCloseCode", "");
		this.totaVo.putParam("L2r05BreachCode", "");
		this.totaVo.putParam("L2r05BreachGetCode", "");
		this.totaVo.putParam("L2r05ProdBreachFlag", "");
		this.totaVo.putParam("L2r05BreachFlag", "");
		this.totaVo.putParam("L2r05ProhibitMonth", 0);
		this.totaVo.putParam("L2r05BreachPercent", 0);
		this.totaVo.putParam("L2r05BreachDecreaseMonth", 0);
		this.totaVo.putParam("L2r05BreachDecrease", 0);
		this.totaVo.putParam("L2r05BreachStartPercent", 0);
		this.totaVo.putParam("L2r05Breach", "");
		this.totaVo.putParam("L2r05CreditScore", "");
		this.totaVo.putParam("L2r05GuaranteeDate", "");
		this.totaVo.putParam("L2r05ContractNo", "");
		this.totaVo.putParam("L2r05AchAuthCode", "");
		this.totaVo.putParam("L2r05CreditSysNo", "");
		this.totaVo.putParam("L2r05RelationCode", "");
		this.totaVo.putParam("L2r05RelationName", "");
		this.totaVo.putParam("L2r05RelationId", "");
		this.totaVo.putParam("L2r05RelationBirthday", "");
		this.totaVo.putParam("L2r05RelationGender", "");
		this.totaVo.putParam("L2r05PrevPayIntDate", 0);
		this.totaVo.putParam("L2r05NextPayIntDate", 0);
		this.totaVo.putParam("L2r05SpecificDd", 0);
		this.totaVo.putParam("L2r05SpecificDate", 0);
		this.totaVo.putParam("L2r05BormCurrencyCode", wkCurrencyCode);
		this.totaVo.putParam("L2r05LoanBal", 0);
		this.totaVo.putParam("L2r05BormCount", 0);
		this.totaVo.putParam("L2r05BormNo", "");
		this.totaVo.putParam("L2r05DueAmt", 0);
		this.totaVo.putParam("L2r05AvailableAmt", "");
		this.totaVo.putParam("L2r05LimitFlag", "");
		this.totaVo.putParam("L2r05PrevPayIntDate", "");
		this.totaVo.putParam("L2r05NextPayIntDate", "");
		this.totaVo.putParam("L2r05SpecificDd", "");
		this.totaVo.putParam("L2r05SpecificDate", "");
		this.totaVo.putParam("L2r05BormCurrencyCode", "");
		this.totaVo.putParam("L2r05LoanBal", "");
		this.totaVo.putParam("L2r05BormCount", "");
		this.totaVo.putParam("L2r05DueAmt", "");
		this.totaVo.putParam("L2r05BaseRate", 0);
		this.totaVo.putParam("L2r05CloseFg", "");
		this.totaVo.putParam("L2r05RvBormNo", 0);
		this.totaVo.putParam("L2r05NextRvBormNo", 0); // 預定撥款序號
		this.totaVo.putParam("L2r05RvDrawdownAmt", 0);
		this.totaVo.putParam("L2r05RenewFlag", "N");
		for (int i = 1; i <= 10; i++) {
			this.totaVo.putParam("L2r05StepRateMonths" + i, 0);
			this.totaVo.putParam("L2r05StepRateMonthE" + i, 0);
			this.totaVo.putParam("L2r05StepRateType" + i, "");
			this.totaVo.putParam("L2r05StepRateIncr" + i, 0);
		}

		// 綠色授信
		this.totaVo.putParam("L2r05Grcd", "");
		this.totaVo.putParam("L2r05GrKind", "");
		this.totaVo.putParam("L2r05EsGcd", "");
		this.totaVo.putParam("L2r05EsGKind", "");
		this.totaVo.putParam("L2r05EsGcnl", "");
		// 購地貸款相關
		this.totaVo.putParam("L2r05PreStarBuildingYM", 0);
		this.totaVo.putParam("L2r05StarBuildingYM", 0);
	}

	// 清償違約說明
	public String getBreachDescription(FacMain t, TitaVo titaVo) throws LogicException {
		this.info("getBreachDescription  ");

		String wkBreachDescription = "";
		String wkBreachA = "";
		String wkBreachB = "";
		String wkBreachC = "";
		String wkBreachD = "";
		String wkBreachE = "";
		String wkBreachF = "";

		if (t != null) {
			if ("Y".equals(t.getBreachFlag())) {
				wkBreachA = "自借款日起算，於未滿 " + t.getProhibitMonth() + "個月期間提前清償者";
				if (t.getBreachStartPercent() != 0) {
					wkBreachB = "，還款金額達 " + t.getBreachStartPercent() + "% 以上時";
				}
				switch (t.getBreachCode()) {
				case "001":
					wkBreachC = "，按各次提前清償金額";
					break;
				case "002":
					wkBreachC = "，按各次提前清償金額";
					break;
				case "003":
					wkBreachC = "，每次還款按核准額度";
					break;
				case "004":
					wkBreachC = "，每次還款依撥款金額";
					break;
				case "005":
					wkBreachC = "，按各次提前清償金額";
					break;
				}
				if (t.getBreachPercent().compareTo(BigDecimal.ZERO) > 0) {
					wkBreachD = "，" + t.getBreachPercent() + "% 計付違約金";
				}
				if (t.getBreachDecreaseMonth() != 0) {
					wkBreachE = "，但每" + t.getBreachDecreaseMonth() + "個月遞減違約金" + t.getBreachDecrease() + "%";
				}
				switch (t.getBreachGetCode()) {
				case "1":
					wkBreachF = "，即時收取";
					break;

				case "2":
					wkBreachF = "，領清償證明時收取";
					break;
				}

			}
			wkBreachDescription = wkBreachA + wkBreachB + wkBreachC + wkBreachD + wkBreachE + wkBreachF;

		}

		return wkBreachDescription;
	}

}