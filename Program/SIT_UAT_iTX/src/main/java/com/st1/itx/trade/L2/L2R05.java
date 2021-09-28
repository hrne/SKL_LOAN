package com.st1.itx.trade.L2;

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
import com.st1.itx.dataVO.TempVo;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.dataVO.TotaVo;
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
import com.st1.itx.db.service.CustMainService;
import com.st1.itx.db.service.FacCaseApplService;
import com.st1.itx.db.service.FacMainService;
import com.st1.itx.db.service.FacProdBreachService;
import com.st1.itx.db.service.FacProdService;
import com.st1.itx.db.service.FacProdStepRateService;
import com.st1.itx.db.service.LoanBorMainService;
import com.st1.itx.db.service.LoanNotYetService;
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.common.AuthLogCom;
import com.st1.itx.util.common.LoanAvailableAmt;
import com.st1.itx.util.format.FormatUtil;
import com.st1.itx.util.parse.Parse;

/* Tita
 * RimFuncCode=9,1
 * RimTxCode=X,5
 * RimFKey=9,1
 * RimCustNo=9,7
 * RimFacmNo=9,3
 * RimApplNo=9,7
 * RimCaseNo=9,7
 */
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
	public FacProdBreachService facProdBreachService;
	@Autowired
	public FacProdStepRateService facProdStepRateService;
	@Autowired
	public LoanBorMainService loanBorMainService;
	@Autowired
	public FacCaseApplService facCaseApplService;
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
	private int facLastBormNo = 0;
	private BigDecimal wkBaseRate = BigDecimal.ZERO;
	private TitaVo titaVo = new TitaVo();
	private String wkCurrencyCode = "TWD";
	private String wkCustId = "";
	private String wkGroupId = "";
	private String wkBreachCode;
	private String wkBreachNo;
	private String sProdNo;
	private String wkCloseFg = "N"; // 未齊件未消註記
	private BigDecimal wkAcctFee = BigDecimal.ZERO;
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

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active L2R05 ");
		loanAvailableAmt.setTxBuffer(this.txBuffer);

		this.totaVo.init(titaVo);
		this.titaVo = titaVo;

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
			throw new LogicException(titaVo, "E0009", "L2R05"); // 交易代號不可為空白
		}
		if (!(iFuncCode == 1 || iFuncCode == 2 || iFuncCode == 4 || iFuncCode == 5)) {
			throw new LogicException(titaVo, "E0010", "L2R05"); // 功能選擇錯誤
		}
		if (iApplNo == 0 && iCustNo == 0 && iCaseNo == 0) {
			throw new LogicException(titaVo, "E0019", "L2R05 核准號碼, 借款人戶號, 案件編號不可皆為0"); // 輸入資料錯誤
		}

		// 查詢額度主檔
		if (iApplNo > 0) {
			tFacMain = facMainService.facmApplNoFirst(iApplNo, titaVo);
			if (tFacMain == null) {
				throw new LogicException(titaVo, "E0001", "L2R05 額度主檔 核准編號 = " + iApplNo); // 查詢資料不存在
			}
			if ((iTxCode.equals("L3410") || iTxCode.equals("L3420")) && iCustNo > 0) {
				if (iCustNo != tFacMain.getCustNo()) {
					throw new LogicException(titaVo, "E3077", "L2R05 新核准號碼 戶號 = " + tFacMain.getCustNo()); // 新核准號碼與原核准號碼，非屬於同一人
				}
			}
			// L2118新增時檢查 撥款後不可再建檔 TODO:測試期間暫不控管
//			if (iTxCode.equals("L2118") & iFuncCode == 1) {
//				if (tFacMain.getLastBormNo() > 0) {
//					throw new LogicException(titaVo, "E2066", "L2R05 核准號碼  = " + tFacMain.getApplNo()); // 已撥款不可再建檔
//				}
//			}
		} else {
			if (iCustNo > 0) {
				tFacMain = facMainService.findById(new FacMainId(iCustNo, iFacmNo), titaVo);
				if (tFacMain == null) {
					throw new LogicException(titaVo, "E0001", "L2R05 額度主檔 借款人戶號 = " + iCustNo + " 額度編號 = " + iFacmNo); // 查詢資料不存在
				}
			}
			if (iCaseNo > 0) {
				tFacMain = facMainService.facmCreditSysNoFirst(iCaseNo, iCaseNo, iFacmNo, iFacmNo, titaVo);
				if (tFacMain == null) {
					throw new LogicException(titaVo, "E0001", "L2R05 額度主檔 案件編號 = " + iCaseNo + " 額度編號 = " + iFacmNo); // 查詢資料不存在
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
				throw new LogicException(titaVo, "E0001", "L2R05 額度主檔 借款人戶號 = " + wkCustNo + " 額度編號 = " + wkFacmNo); // 查詢資料不存在
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
			if (iFKey != 7) {
				if (tFacMain.getLineAmt().compareTo(tFacMain.getUtilBal()) <= 0) {
					throw new LogicException(titaVo, "E2072",
							"L2R05 額度主檔 借款人戶號 = " + tFacMain.getCustNo() + " 額度編號 = " + tFacMain.getFacmNo()
									+ " 核准額度 = " + df.format(tFacMain.getLineAmt()) + " 已動用額度餘額 = "
									+ df.format(tFacMain.getUtilBal())); // 該筆額度編號沒有可用額度
				}
			}
//			if (tFacProd.getCharCode().equals("2") && tFacMain.getUtilAmt().compareTo(BigDecimal.ZERO) > 0) {   //TODO 已房養老刪除
//				throw new LogicException(titaVo, "E3099", "L2R05 額度主檔 借款人戶號 = " + tFacMain.getCustNo() + " 額度編號 = "
//						+ tFacMain.getFacmNo() + " 已動用額度 = " + df.format(tFacMain.getUtilAmt())); // 該筆額度(以房養老)已撥款
//			}
		}

		tCustMain = custMainService.custNoFirst(tFacMain.getCustNo(), tFacMain.getCustNo(), titaVo);
		if (tCustMain == null) {
			throw new LogicException(titaVo, "E2003", "L2R05 客戶資料主檔 UKey=" + tFacMain.getCustNo()); // 查無資料
		}
		wkCustId = tCustMain.getCustId();
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

		// 查詢清償金類型
		SetTotaBreach();
		wkBreachNo = FormatUtil.pad9(String.valueOf(tFacMain.getCustNo()), 7)
				+ FormatUtil.pad9(String.valueOf(tFacMain.getFacmNo()), 3);
		wkBreachCode = tFacProd.getBreachCode();
		slFacProdBreach = facProdBreachService.breachNoEq(wkBreachNo, wkBreachCode, wkBreachCode, this.index,
				this.limit, titaVo);
		if (!(slFacProdBreach == null || slFacProdBreach.isEmpty())) {
			SetTotaBreach();
		}
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
		facLastBormNo = tFacMain.getLastBormNo();
		wkRvBormNo = wkRvCnt + facLastBormNo + 1;
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

		this.totaVo.putParam("OBaseRate", wkBaseRate); // 指標利率
		this.totaVo.putParam("OCloseFg", wkCloseFg); // 未齊件註記
		this.totaVo.putParam("ORvBormNo", wkRvBormNo); // 預定撥款序號
		this.totaVo.putParam("ORvDrawdownAmt", wkRvDrawdownAmt); // 已預約撥款金額
		for (int i = 1; i <= 10; i++) {
			this.totaVo.putParam("StepMonths" + i, 0);
			this.totaVo.putParam("StepMonthE" + i, 0);
			this.totaVo.putParam("StepRateType" + i, 0);
			this.totaVo.putParam("StepRateIncr" + i, 0);
		}

		sProdNo = FormatUtil.pad9(String.valueOf(tFacMain.getCustNo()), 7)
				+ FormatUtil.pad9(String.valueOf(tFacMain.getFacmNo()), 3);

		// 查詢階梯式利率
		slFacProdStepRate = facProdStepRateService.stepRateProdNoEq(sProdNo, 0, 999, this.index, this.limit, titaVo);

		if (slFacProdStepRate != null) {
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
				&& tFacMain.getUtilAmt().compareTo(BigDecimal.ZERO) > 0) {
			wkAcctFee = BigDecimal.ZERO;
		}
		this.info("tempVo =   " + tempVo);
		this.totaVo.putParam("OCustId", wkCustId);
		this.totaVo.putParam("OCustNo", tFacMain.getCustNo());
		this.totaVo.putParam("OFacmNo", tFacMain.getFacmNo());
		this.totaVo.putParam("OApplNo", tFacMain.getApplNo());
		this.totaVo.putParam("OProdNo", tFacMain.getProdNo());
		this.totaVo.putParam("OBaseRateCode", tFacMain.getBaseRateCode());
		this.totaVo.putParam("ORateIncr", tFacMain.getRateIncr());
		this.totaVo.putParam("OIndividualIncr", tFacMain.getIndividualIncr());
		this.totaVo.putParam("OApproveRate", tFacMain.getApproveRate());
		this.totaVo.putParam("ORateCode", tFacMain.getRateCode());
		this.totaVo.putParam("OFirstRateAdjFreq", tFacMain.getFirstRateAdjFreq());
		this.totaVo.putParam("OFirstAdjRateDate", FirstAdjRateDate);

		this.totaVo.putParam("ORateAdjFreq", tFacMain.getRateAdjFreq());
		this.totaVo.putParam("OCurrencyCode", tFacMain.getCurrencyCode());
		this.totaVo.putParam("OLineAmt", tFacMain.getLineAmt());
		this.totaVo.putParam("OUtilAmt", tFacMain.getUtilAmt());
		this.totaVo.putParam("OUtilBal", tFacMain.getUtilBal());
		this.totaVo.putParam("OAcctCode", tFacMain.getAcctCode());
		this.totaVo.putParam("OLoanTermYy", tFacMain.getLoanTermYy());
		this.totaVo.putParam("OLoanTermMm", tFacMain.getLoanTermMm());
		this.totaVo.putParam("OLoanTermDd", tFacMain.getLoanTermDd());
		this.totaVo.putParam("OFirstDrawdownDate", tFacMain.getFirstDrawdownDate());
		this.totaVo.putParam("OMaturityDate", tFacMain.getMaturityDate());
		this.totaVo.putParam("OIntCalcCode", tFacMain.getIntCalcCode()); 
		this.totaVo.putParam("OAmortizedCode", tFacMain.getAmortizedCode());
		this.totaVo.putParam("OFreqBase", tFacMain.getFreqBase());
		this.totaVo.putParam("OPayIntFreq", tFacMain.getPayIntFreq());
		this.totaVo.putParam("ORepayFreq", tFacMain.getRepayFreq());
		this.totaVo.putParam("OUtilDeadline", tFacMain.getUtilDeadline());
		this.totaVo.putParam("OGracePeriod", tFacMain.getGracePeriod());
		this.totaVo.putParam("OAcctFee", wkAcctFee);
//		this.totaVo.putParam("ODuePayAmt", tFacMain.getDuePayAmt());
//		this.totaVo.putParam("ODuePayLimit", tFacMain.getDuePayLimit());
//		this.totaVo.putParam("OPayIntLimit", tFacMain.getPayIntLimit());
		this.totaVo.putParam("OExtraRepayCode", tFacMain.getExtraRepayCode());
		this.totaVo.putParam("OCustomerCode", tFacMain.getCustTypeCode());
		this.totaVo.putParam("ORuleCode", tFacMain.getRuleCode());
		this.totaVo.putParam("ORecycleCode", tFacMain.getRecycleCode());
		this.totaVo.putParam("ORecycleDeadline", tFacMain.getRecycleDeadline());
		this.totaVo.putParam("OUsageCode", tFacMain.getUsageCode());
		this.totaVo.putParam("ODepartmentCode", tFacMain.getDepartmentCode());
		this.totaVo.putParam("OIncomeTaxFlag", tFacMain.getIncomeTaxFlag());
		this.totaVo.putParam("OCompensateFlag", tFacMain.getCompensateFlag());
		this.totaVo.putParam("OIrrevocableFlag", tFacMain.getIrrevocableFlag());
		this.totaVo.putParam("OPieceCode", tFacMain.getPieceCode());
		this.totaVo.putParam("ORateAdjNoticeCode", tFacMain.getRateAdjNoticeCode());
		this.totaVo.putParam("ORepayCode", tFacMain.getRepayCode());
		this.totaVo.putParam("ORepayBank", wkRepayBank);
		this.totaVo.putParam("ORepayAcctNo", wkRepayAcctNo);
		this.totaVo.putParam("OPostCode", wkPostCode);
		this.totaVo.putParam("OIntroducer", tFacMain.getIntroducer());
		this.totaVo.putParam("ODistrict", tFacMain.getDistrict());
		this.totaVo.putParam("OFireOfficer", tFacMain.getFireOfficer());
		this.totaVo.putParam("OEstimate", tFacMain.getEstimate());
		this.totaVo.putParam("OCreditOfficer", tFacMain.getCreditOfficer());
		this.totaVo.putParam("OLoanOfficer", tFacMain.getLoanOfficer());
		this.totaVo.putParam("OBusinessOfficer", tFacMain.getBusinessOfficer());
		this.totaVo.putParam("OApprovedLevel", tFacMain.getApprovedLevel());
		this.totaVo.putParam("OSupervisor", tFacMain.getSupervisor());
		this.totaVo.putParam("OInvestigateOfficer", tFacMain.getInvestigateOfficer());
		this.totaVo.putParam("OEstimateReview", tFacMain.getEstimateReview());
		this.totaVo.putParam("OCoorgnizer", tFacMain.getCoorgnizer());
		this.totaVo.putParam("OGroupId", wkGroupId);
		this.totaVo.putParam("OAdvanceCloseCode", tFacMain.getAdvanceCloseCode());
		this.totaVo.putParam("OBreachCode", tFacProd.getBreachCode());
		this.totaVo.putParam("OBreachGetCode", tFacProd.getBreachGetCode());
//		this.totaVo.putParam("ODecreaseFlag", tFacProd.getDecreaseFlag());
//		this.totaVo.putParam("OCopyFlag", tFacMain.getCopyFlag());
		this.totaVo.putParam("OProdBreachFlag", tFacMain.getProdBreachFlag());
		this.totaVo.putParam("OBreach", tFacMain.getBreachDescription());
		this.totaVo.putParam("OCreditScore", tFacMain.getCreditScore());
		this.totaVo.putParam("OGuaranteeDate", tFacMain.getGuaranteeDate());
		this.totaVo.putParam("OContractNo", tFacMain.getContractNo());
		this.totaVo.putParam("OAchAuthCode", wkAchAuthCode);
		this.totaVo.putParam("OCreditSysNo", tFacMain.getCreditSysNo());
		this.totaVo.putParam("ORelationCode", wkRelationCode);
		this.totaVo.putParam("ORelationName", wkRelationName);
		this.totaVo.putParam("ORelationId", wkRelationId);
		this.totaVo.putParam("ORelationBirthday", wkRelationBirthday);
		this.totaVo.putParam("ORelationGender", wkRelationGender);
		this.totaVo.putParam("OPrevPayIntDate", 0);
		this.totaVo.putParam("ONextPayIntDate", 0);
		this.totaVo.putParam("OSpecificDd", 0);
		this.totaVo.putParam("OSpecificDate", 0);
		this.totaVo.putParam("OBormCurrencyCode", wkCurrencyCode);
		this.totaVo.putParam("OLoanBal", 0);
		this.totaVo.putParam("OBormCount", 0);
		this.totaVo.putParam("OBormNo", tFacMain.getLastBormNo());
		this.totaVo.putParam("ODueAmt", 0);
//		this.totaVo.putParam("OCancelCode", tFacMain.getCancelCode());
		// 可用額度=Min(共用額度「可用總額度/循環動用」,Min(額度核准金額,擔保品分配金額) -已動用額度餘額))
		BigDecimal wkAvailableAmt = loanAvailableAmt.caculate(tFacMain, titaVo); // 可用額度
		// 限額計算方式 F-核准額度, C-擔保品 S-合併額度控管
		String wkLimitFlag = loanAvailableAmt.getLimitFlag();
		this.totaVo.putParam("OAvailableAmt", wkAvailableAmt);
		this.totaVo.putParam("OLimitFlag", wkLimitFlag);

	}

	// 清償金類型
	private void SetTotaBreach() throws LogicException {
		if (slFacProdBreach == null || slFacProdBreach.isEmpty()) {
			for (int i = 1; i <= 10; i++) {
				this.totaVo.putParam("BreachbMmA" + i, 0);
				this.totaVo.putParam("BreachbMmB" + i, 0);
				this.totaVo.putParam("BreachbPercent" + i, 0);
				this.totaVo.putParam("BreachaYyA" + i, 0);
				this.totaVo.putParam("BreachaYyB" + i, 0);
				this.totaVo.putParam("BreachaPercent" + i, 0);
			}
		} else {
			int i = 1;
			for (FacProdBreach tFacProdBreach : slFacProdBreach.getContent()) {
				if (wkBreachCode.equals("002")) {
					this.totaVo.putParam("BreachbMmA" + i, tFacProdBreach.getMonthStart());
					this.totaVo.putParam("BreachbMmB" + i, tFacProdBreach.getMonthEnd());
					this.totaVo.putParam("BreachbPercent" + i, tFacProdBreach.getBreachPercent());
				} else {
					this.totaVo.putParam("BreachaYyA" + i, tFacProdBreach.getMonthStart() / 12);
					this.totaVo.putParam("BreachaYyB" + i, tFacProdBreach.getMonthEnd() / 12);
					this.totaVo.putParam("BreachaPercent" + i, tFacProdBreach.getBreachPercent());
				}
				i++;
				if (i > 10)
					break;
			}
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
			this.totaVo.putParam("OPrevPayIntDate", wkPrevPayIntDate);
			this.totaVo.putParam("ONextPayIntDate", wkNextPayIntDate);
			this.totaVo.putParam("OSpecificDd", wkSpecificDd);
			this.totaVo.putParam("OSpecificDate", wkSpecificDate);
			this.totaVo.putParam("OBormCurrencyCode", wkCurrencyCode);
			this.totaVo.putParam("OLoanBal", wkLoanBal);
			this.totaVo.putParam("OBormCount", wkBormCount);
			this.totaVo.putParam("ODueAmt", wkDueAmt);
		}
		this.info("   wkBormCount = " + wkBormCount);
		if (wkBormCount == 0) {
			if (iTxCode.equals("L3711") || iTxCode.equals("L3712")) {
				throw new LogicException(titaVo, "E0001", "L2R05 查無該額度的最近繳息日"); // 查詢資料不存在
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
			this.totaVo.putParam("StepMonths" + i, tFacProdStepRate.getMonthStart());
			this.totaVo.putParam("StepMonthE" + i, tFacProdStepRate.getMonthEnd());
			this.totaVo.putParam("StepRateType" + i, tFacProdStepRate.getRateType());
			this.totaVo.putParam("StepRateIncr" + i, tFacProdStepRate.getRateIncr());
			i++;
		}
	}
}