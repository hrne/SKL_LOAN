package com.st1.itx.util.common;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Component;

import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.db.domain.AcReceivable;
import com.st1.itx.db.domain.LoanRateChange;
import com.st1.itx.db.service.AcReceivableService;
import com.st1.itx.db.service.LoanRateChangeService;
import com.st1.itx.tradeService.CommBuffer;
import com.st1.itx.util.common.data.CalcRepayIntVo;
import com.st1.itx.util.date.DateUtil;
import com.st1.itx.util.parse.Parse;

/**
 * 放款計息<BR>
 * getRepayInt 按指定的計算止日或回收期數，計算利息 call by LXXXX, BaTxCom<BR>
 * 
 * @author st1
 *
 */
@Component("loanCalcRepayIntCom")
@Scope("prototype")
public class LoanCalcRepayIntCom extends CommBuffer {
	// input 共同參數
	private int iCustNo;
	private int iFacmNo;
	private int iBormNo;
	private String iProdNo; // 商品代碼
	private String iAcctCode; // 科目
	private String iCurrencyCode; // 幣別
	private String iBaseRateCode; // 指標利率代碼
	private String iRateCode; // 利率區分 1:機動 2:固動 3:定期機動
	private int iRateAdjFreq; // 利率調整週期
	private int iNextAdjRateDate; // 下次利率調整日
	private BigDecimal iPrincipal; // 計息本金
	private String iIncrFlag; // 加減碼是否依合約 Y:是 N:否
	private BigDecimal iStoreRate; // 上次收息利率
	private BigDecimal iRateIncr; // 加碼利率
	private BigDecimal iIndividualIncr; // 個別加碼利率
	private int iFreqBase; // 週期基準 1:日 2:月 3:週
	private int iPayIntFreq; // 繳息週期
	private int iRepayFreq; // 還本週期
	private int iPaidTerms; // 已繳息期數
	private int iTerms; // 本次繳息期數
	private int iIntStartDate; // 計算起日
	private int iIntEndDate; // 計算止日
	private int iIntEndCode; // 計算止日代碼 0.無計算止日 1.至計算止日 2:利息提存
	private int iFirstDrawdownDate; // 初貸日
	private int iDrawdownDate; // 貸放起日
	private int iMaturityDate; // 貸放止日
	private int iBreachValidDate; // 違約金生效日
	private int iPrevPaidIntDate; // 上次繳息日
	private int iPrevRepaidDate; // 上次還本日
	private int iNextPayIntDate; // 下次繳息日,應繳息日,預定收息日
	private int iNextRepayDate; // 下次還本日,應還本日,預定還本日
	private int iSpecificDate; // 指定基準日期, 利息基準日
	private int iSpecificDd; // 指定應繳日
	private int iFirstDueDate; // 首次應繳日
	private int iGraceDate; // 寬限到期日
	private int iBreachGraceDays; // 違約寬限天數(營業日)
	private String iExtraRepayCode; // 攤還額異動碼 0: 不變 1: 變
	private BigDecimal iDueAmt; // 每期攤還金額
	private BigDecimal iBreachRate; // 違約金之利率
	private BigDecimal iDelayRate; // 遲延息之利率
	private int iUnpaidFlag; // 未繳清記號
	private String iIntCalcCode; // 計息方式 1:按日計息 2:按月計息
	private int iAmortizedCode; // 攤還方式,還本方式 1.按月繳息(按期繳息到期還本) 2.到期取息(到期繳息還本) 3.本息平均法(期金) 4.本金平均法
	private int iDelayFlag; // 0:收遲延息 1: 不收
	private int iNonePrincipalFlag; // 0:契約到期要還本 1:契約到期不還本記號
	private int iTbsDy; // 營業日
	private int iEntryDate; // 入帳日期
	private String iUsageCode; // 資金用途別 1: 週轉金2: 購置不動產3: 營業用資產4: 固定資產5: 企業投資6: 購置動產9: 其他
	private String iBreachReliefFlag; // 減免違約金 Y:是 N:否
	private String iSyndFlag; // 聯貸案件 Y:是 N:否
	private BigDecimal iFinalBal; // 最後一期本金餘額
	private int iGracePeriod; // 寬限期
	private int iTotalPeriod; // 總期數
	// input 個別參數
	private String iCaseCloseFlag; // 結案記號 Y:是 N:否
	private BigDecimal iExtraRepay; // 部分償還本金金額
	private String iExtraRepayFlag; // 部分償還本金是否內含利息 Y:是 N:否

	// output
	private int oNextAdjRateDate; // 下次利率調整日
	private int oRateAdjFreq; // 利率調整週期
	private BigDecimal oStoreRate; // 適用利率
	private BigDecimal oRateIncr; // 加碼利率
	private BigDecimal oIndividualIncr; // 個別加碼利率
	private int oPrevRepaidDate; // 上次還本日
	private int oNextRepayDate; // 下次還本日,應還本日(最後一期為到期日)
	private BigDecimal oLoanBal; // 回收後餘額
	private BigDecimal oPrincipal; // 應還本金總額
	private BigDecimal oBreachAmt; // 違約金
	private BigDecimal oInterest; // 利息總金額
	private BigDecimal oDelayInt; // 延遲息總額
	private BigDecimal oExtraAmt; // 部分清償金額
	private BigDecimal oDueAmt; // 期金
	private int oPaidTerms; // 已繳息期數
	private int oRepaidPeriod; // 本次計息還本期數
	private int oPrevPaidIntDate; // 上次收息日
	private int oNextPayIntDate; // 預定收息日, 下次收息日(最後一期依應繳日計算)
	private int oCalcCount; // 計息筆數
	// work area
	private BigDecimal wkPrincipal; // 計息本金
	private BigDecimal wkDueAmt; // 每期攤還金額
	private BigDecimal wkLoanBal;
	private BigDecimal wkDuraInt; // 累計分段的利息
	private int wkIntStartDate; // 計算起日
	private int wkIntEndDate; // 計算止日
	private int wkIntEndDateX; // 計算止日
	private int wkIntEndDateY; // 計算止日
	private int wkDays; // 計算日數
	private int wkMonthLimit; // 當月日數
	private BigDecimal wkStoreRate; // 上次收息利率
	private BigDecimal wkBeforeStoreRate;
	private BigDecimal wkRateIncr; // 加碼利率
	private BigDecimal wkIndividualIncr; // 個別加碼利率
	private int wkTermEndDate; // 期款止日
	private int wkTerms; // 繳息期數
	private int wkFreqCode; // 1:週期為日 2:週期為月 3:週期為週
	private int wkNextRepayDate; // 下次還本日,應還本日,預定還本日
	private int wkLastTermNo;
	private int wkDate;
	private int wkMonRemainder;
	private int wkType;
	private int wkCalcVoIndex; // 計息明細指標(從0開始)
	private int wkCalcVoCount; // 計息明細總筆數(從0開始)
	private int wkTermIndex;
	private BigDecimal wkAmt;
	private BigDecimal wkFitRate; // 適用利率
	private int wkNextEffectDate; // 下一個利率生效日
	private BigDecimal wkNextFitRate; // 下一個利率
	private int wkProcessCode; // 1:指定收息止日 2:週期為月 3:週期為週
	private boolean isFirstTermMonth; // 是否為首月
	private boolean isRateChange; // 是否為利率變動
	private BigDecimal wkExtraRepay; // 部分償還本金
	private BigDecimal wkTotalExtraRepay; //
	private int wkInterestFlag; // 計息記號 1:按日計息 2:按月計息
	private int wkSpecificDate; // 指定基準日期
	private int wkSpecificMons; // 指定基準月數
	private DecimalFormat df = new DecimalFormat("##,###,###,###,##0");

	/* DB服務注入 */
	@Autowired
	public LoanRateChangeService loanRateChangeService;

	@Autowired
	private AcReceivableService sAcReceivableService;

	@Autowired
	LoanDueAmtCom loanDueAmtCom;
	@Autowired
	DateUtil dDateUtil;
	@Autowired
	Parse parse;

	private LoanRateChange tLoanRateChange;
	private CalcRepayIntVo vCalcRepayIntVo;
	private ArrayList<CalcRepayIntVo> lCalcRepayIntVo;

	/**
	 * 按指定的計算止日或回收期數，計算利息
	 * 
	 * @param titaVo TitaVo
	 * @return 按期數及利率變動的分段計息明細
	 * @throws LogicException LogicException
	 */
	public ArrayList<CalcRepayIntVo> getRepayInt(TitaVo titaVo) throws LogicException {
		this.info("active CalcRepayIntCom ");
		this.titaVo = titaVo;

		// dDateUtil = new DateUtil();
		tLoanRateChange = new LoanRateChange();
		vCalcRepayIntVo = new CalcRepayIntVo();

		// Log Input
		logInputRoutine();

		initWorkRoutine();

		// 檢查輸入資料
		checkInputRoutine(titaVo);

		// 調整違約金生效日遇假日
		adjustBreachValidDateRoutine();

		// 產生按期數及利率變動的分段計息資料
		if (iIntEndDate > 0) {
			specifyPayIntDateRoutine(); // 指定繳息止日處理
		} else {
			specifyPayTermsRoutine(); // 指定繳息期數處理
		}

		// 無計息資料return
		if (wkCalcVoIndex < 0) {
			return null;
		}

		// 計息期數
		wkCalcVoCount = wkCalcVoIndex;

		// 填入計息本金、調整計算金額(含期金調整)
		if (iNextRepayDate > 0) {
			wkNextRepayDate = iNextRepayDate;
			for (int i = 0; i <= wkCalcVoCount; i++) {
				fillPrincipalRoutine(i); // 填入計息本金
			}
			wkAmt = BigDecimal.ZERO;
			wkDuraInt = BigDecimal.ZERO;
			for (int i = 0; i <= wkCalcVoCount; i++) {
				adjustAmtRoutine(i);
			}
		}

		// 移除計算金額=0
		int wkRemoveCnt = 0;
		for (int i = wkCalcVoCount; i >= 0; i--) {
			if (lCalcRepayIntVo.get(i).getAmount().compareTo(BigDecimal.ZERO) == 0) {
				lCalcRepayIntVo.remove(i);
				wkRemoveCnt++;
			}
		}
		wkCalcVoCount = wkCalcVoCount - wkRemoveCnt;

		// 計算利息
		for (int i = 0; i <= wkCalcVoCount; i++) {
			this.info("fillInterestRoutine i = " + i);
			vCalcRepayIntVo = lCalcRepayIntVo.get(i);
			if (vCalcRepayIntVo.getEndDate() >= iIntStartDate) {
				wkAmt = calcInterestRoutine();
				vCalcRepayIntVo.setInterest(wkAmt);
				lCalcRepayIntVo.set(i, vCalcRepayIntVo);
			}
		}

		// 計算違約金
		if (!iBreachReliefFlag.equals("Y")) { // 減免違約金 Y:是 N:否
			wkDuraInt = BigDecimal.ZERO;
			for (int i = 0; i <= wkCalcVoCount; i++) {
				i = fillBreachRoutine(i);
			}
		}

		// 處理部分償還金額
		// 結案記號 = Y 時，部分償還本金是否內含利息 Y:是
		if ("Y".equals(iCaseCloseFlag) || wkExtraRepay.compareTo(BigDecimal.ZERO) > 0) {
			wkTotalExtraRepay = BigDecimal.ZERO;
			for (int i = 0; i <= wkCalcVoCount; i++) {
				if (extraRepayRoutine(i, titaVo) == true) {
					break;
				}
			}
			if (wkExtraRepay.compareTo(BigDecimal.ZERO) < 0) {
				throw new LogicException(titaVo, "E3927", " 金額 = " + wkTotalExtraRepay); // 計算利息錯誤，部分償還本金不足償還本金利息
			}
		}

		// 計算合計數
		for (int i = 0; i <= wkCalcVoCount; i++) {
			totalAmtRoutine(i, titaVo);
		}

		// 計算下次收息日
		nextPayIntDateRoutine();

		// Log Output
		logOutputRoutine();

		return lCalcRepayIntVo;
	}

	public void init() {
		this.info("   CalcRepayIntCom init");

		iCustNo = 0;
		iFacmNo = 0;
		iBormNo = 0;
		iCurrencyCode = "TWD";
		iBaseRateCode = "00";
		iRateCode = "0";
		iRateAdjFreq = 0;
		iNextAdjRateDate = 0;
		iPrincipal = BigDecimal.ZERO;
		iStoreRate = BigDecimal.ZERO;
		iIncrFlag = "Y";
		iRateIncr = BigDecimal.ZERO;
		iIndividualIncr = BigDecimal.ZERO;
		iFreqBase = 0;
		iPayIntFreq = 0;
		iRepayFreq = 0;
		iTerms = 0;
		iPaidTerms = 0;
		iIntStartDate = 0;
		iIntEndDate = 0;
		iFirstDrawdownDate = 0;
		iDrawdownDate = 0;
		iMaturityDate = 0;
		iBreachValidDate = 0;
		iNextPayIntDate = 0;
		iNextRepayDate = 0;
		iSpecificDate = 0;
		iGraceDate = 0;
		iExtraRepayCode = "0";
		iDueAmt = BigDecimal.ZERO;
		iBreachRate = BigDecimal.ZERO;
		iDelayRate = iStoreRate;
		iUnpaidFlag = 0;
		iIntCalcCode = ""; // IntCalcCode 計息方式 1: 按日計息 2: 按月計息
		iAmortizedCode = 0;
		iDelayFlag = 0;
		iNonePrincipalFlag = 0;
		iExtraRepayFlag = "N";
		iExtraRepay = BigDecimal.ZERO;
		iTbsDy = 0;
		iUsageCode = "00";
		iSyndFlag = "N";
		iFinalBal = BigDecimal.ZERO;
		iGracePeriod = 0;
		iTotalPeriod = 0;
	}

	private void initWorkRoutine() {
		this.info("   initWorkRoutine ");

		lCalcRepayIntVo = new ArrayList<CalcRepayIntVo>();
		// output
		oNextAdjRateDate = iNextAdjRateDate;
		oRateAdjFreq = iRateAdjFreq;
		oStoreRate = iStoreRate;
		oRateIncr = iRateIncr;
		oIndividualIncr = iIndividualIncr;
		oPrevPaidIntDate = iPrevPaidIntDate;
		oPrevRepaidDate = iPrevRepaidDate;
		oNextPayIntDate = iNextPayIntDate;
		oNextRepayDate = iNextRepayDate;
		oLoanBal = iPrincipal;
		oPrincipal = BigDecimal.ZERO;
		oBreachAmt = BigDecimal.ZERO;
		oInterest = BigDecimal.ZERO;
		oDelayInt = BigDecimal.ZERO;
		oExtraAmt = BigDecimal.ZERO;
		oRepaidPeriod = 0;
		oDueAmt = iDueAmt;
		oCalcCount = 0;
		// work
		wkPrincipal = iPrincipal; // // 計息本金
		wkLoanBal = iPrincipal;
		wkDueAmt = iDueAmt; // 每期攤還金額
		wkIntStartDate = iIntStartDate; // 計算起日
		wkIntEndDate = iIntEndDate; // 計算止日
		wkIntEndDateX = 0; // 計算止日
		wkIntEndDateY = 0; // 計算止日
		wkDays = 0; // 計算日數
		wkMonthLimit = 0; // 當月日數
		wkStoreRate = iStoreRate; // 上次收息利率
		wkBeforeStoreRate = iStoreRate;
		wkFreqCode = iFreqBase; // 週期基準 1:日 2:月 3:週
		wkTerms = iTerms; // 繳息期數
		wkNextRepayDate = iNextRepayDate; // 下次還本日,應還本日,預定還本日
		wkDate = 0;
		wkMonRemainder = 0;
		wkAmt = BigDecimal.ZERO;
		wkType = 0;
		wkCalcVoIndex = -1;
		wkLastTermNo = 0;
		wkTermIndex = 0;
		wkFitRate = BigDecimal.ZERO;
		wkNextEffectDate = 0;
		wkNextFitRate = BigDecimal.ZERO;
		wkProcessCode = 0;
		isRateChange = false;
		wkInterestFlag = "1".equals(iIntCalcCode) ? 1 : 2; // 1:按日計息 2:按月計息
		wkExtraRepay = iExtraRepay;
		wkTotalExtraRepay = BigDecimal.ZERO;
		// 結案記號 = Y 時，部分償還本金是否內含利息 Y:是
		if ("Y".equals(iCaseCloseFlag)) {
			iExtraRepayFlag = "Y";
		}
		isFirstTermMonth = false; // 是否為首月
		isRateChange = false; // 是否為利率變動
		// 指定應繳日因大小月導致日與指定應繳日不同，需調整指定基準日期調整為當年1月，計算時再以指定基準日月差調整
		// 指定應繳日超過28且指定基準日與指定應繳日不同 => 指定基準日期調整為當年1月
		// 指定基準日期=20200228, 指定應繳日=31 => 指定基準日期=20200131, 指定基準日月差=1
		if (iSpecificDd >= 28 && iSpecificDate % 100 != iSpecificDd) {
			wkSpecificDate = (iSpecificDate / 10000) * 10000 + 100 + iSpecificDd;
			wkSpecificMons = (iSpecificDate / 100) - (wkSpecificDate / 100);
		} else {
			wkSpecificDate = iSpecificDate;
			wkSpecificMons = 0;
		}
	}

	// 檢查輸入資料
	private void checkInputRoutine(TitaVo titaVo) throws LogicException {
		this.info("   checkInputRoutine ");

		if (!(iRateCode.equals("1") || iRateCode.equals("2") || iRateCode.equals("3"))) {
			throw new LogicException(titaVo, "E3911",
					"利率區分 = " + iRateCode + " 戶號 = " + iCustNo + " 額度 = " + iFacmNo + " 撥款 = " + iBormNo); // 計算利息錯誤，利率區分錯誤
		}
//		if (iPrincipal.compareTo(BigDecimal.ZERO) == 0 && iUnpaidFlag == 0) {
//			throw new LogicException(titaVo, "E3912", "戶號 = " + iCustNo + " 額度 = " + iFacmNo + " 撥款 = " + iBormNo); // 計算利息錯誤，計息本金為零
//		}
		if (iIntStartDate == 0) {
			throw new LogicException(titaVo, "E3913", "戶號 = " + iCustNo + " 額度 = " + iFacmNo + " 撥款 = " + iBormNo); // 計算利息錯誤，計息起日為零
		}
		if (iIntEndDate > 0 && iIntEndDate < iIntStartDate) {
			throw new LogicException(titaVo, "E3914", "計息起日 = " + iIntStartDate + " 計息止日 = " + iIntEndDate + " 戶號 = "
					+ iCustNo + " 額度 = " + iFacmNo + " 撥款 = " + iBormNo); // 計算利息錯誤，計息止日小於計息起日
		}
		if (iPayIntFreq == 0) {
			throw new LogicException(titaVo, "E3915", "戶號 = " + iCustNo + " 額度 = " + iFacmNo + " 撥款 = " + iBormNo); // 計算利息錯誤，收息週期為零
		}
		if (iNextAdjRateDate == 0) {
			throw new LogicException(titaVo, "E3916", "戶號 = " + iCustNo + " 額度 = " + iFacmNo + " 撥款 = " + iBormNo); // 計算利息錯誤，下次利率調整日為零
		}
		if (iRateAdjFreq == 0) {
			throw new LogicException(titaVo, "E3917", "戶號 = " + iCustNo + " 額度 = " + iFacmNo + " 撥款 = " + iBormNo); // 計算利息錯誤，利率調整週期為零
		}
// 有每期攤還金額為零的轉換資料
//		if (iNextRepayDate > 0 && (iAmortizedCode == 3 || iAmortizedCode == 4)  &&  "N".equals(iCaseCloseFlag)
//				&& iDueAmt.compareTo(BigDecimal.ZERO) == 0) {
//			throw new LogicException(titaVo, "E3919", "戶號 = " + iCustNo + " 額度 = " + iFacmNo + " 撥款 = " + iBormNo); // 計算利息錯誤，每期攤還金額為零
//		}
		if (iBreachValidDate == 0) {
			throw new LogicException(titaVo, "E3923", "戶號 = " + iCustNo + " 額度 = " + iFacmNo + " 撥款 = " + iBormNo); // 計算利息錯誤，違約金生效日為零
		}
		if (!(iFreqBase == 2 || iFreqBase == 3)) {
			throw new LogicException(titaVo, "E3924",
					"週期基準=" + iFreqBase + " 戶號 = " + iCustNo + " 額度 = " + iFacmNo + " 撥款 = " + iBormNo); // 計算利息錯誤，週期基準錯誤
		}
		if (iIntEndDate == 0 && iTerms == 0) {
			throw new LogicException(titaVo, "E3925", "戶號 = " + iCustNo + " 額度 = " + iFacmNo + " 撥款 = " + iBormNo); // 計算利息錯誤，計息止日及計息期數都為零
		}
		// 計息方式: 1:按日計息 // 2:按月計息
		if (!("1".equals(iIntCalcCode) || "2".equals(iIntCalcCode))) {
			throw new LogicException(titaVo, "E3929",
					" 計息方式= " + iIntCalcCode + " ," + iCustNo + "-" + iFacmNo + "- " + iBormNo); // 計算利息資料錯誤
		}
		// 預定還本日相關資料檢查
		if (iNextRepayDate > 0) {
			if (iRepayFreq == 0) {
				throw new LogicException(titaVo, "E3920", "戶號 = " + iCustNo + " 額度 = " + iFacmNo + " 撥款 = " + iBormNo); // 計算利息錯誤，還本週期為零
			}
			if ((iRepayFreq > 0 && iPayIntFreq == 0) || (iRepayFreq == 0 && iPayIntFreq > 0)) {
				throw new LogicException(titaVo, "E3921", "戶號 = " + iCustNo + " 額度 = " + iFacmNo + " 撥款 = " + iBormNo); // 計算利息錯誤，繳息週期及還本週期錯誤
			}
			if (!(iRepayFreq == 99 || iPayIntFreq == 99)) {
				if ((iRepayFreq % iPayIntFreq) > 0) {
					throw new LogicException(titaVo, "E3922", "還本週期 = " + iRepayFreq + " 繳息週期 " + iPayIntFreq + " 戶號 = "
							+ iCustNo + " 額度 = " + iFacmNo + " 撥款 = " + iBormNo); // 計算利息錯誤，還本週期不為繳息週期的倍數
				}
			}
		}
	}

	// 指定繳息期數處理
	private void specifyPayTermsRoutine() throws LogicException {
		this.info("specifyPayTermsRoutine ... ");
		this.info("   iAmortizedCode = " + iAmortizedCode);
		this.info("   iFreqBase      = " + iFreqBase);
		int wkMons = 0;
		int wkdays = 0;
		// 週期基準為月之期款第一期 => 月差1期 +相對日的零星日
		if (iFreqBase == 2) {
			// 計算起日到指定基準日期的零星日
			if (wkIntStartDate > wkSpecificDate) {
				dDateUtil.init();
				dDateUtil.setDate_1(wkSpecificDate);
				dDateUtil.setDate_2(wkIntStartDate);
				dDateUtil.dateDiffSp();
				this.info("specifyPayTermsRoutine dateDiffSp " + wkSpecificDate + "~" + wkIntStartDate + " ="
						+ dDateUtil.getYears() + "/" + dDateUtil.getMons() + "/" + dDateUtil.getDays());
				wkdays = dDateUtil.getDays();
			}
			// 期款第一期或起日有零星日
			if (wkIntStartDate < iFirstDueDate || wkdays > 0) {
				isFirstTermMonth = true;// 是否為首月
				// 按日計息首月不分段
				if ("1".equals(iIntCalcCode)) {
					wkTerms = 1;
					wkProcessCode = 1;
					wkIntEndDate = iNextPayIntDate;
					this.info("specifyPayTermsRoutine-0");
					specifyTermsRoutine();
				} else {
					dDateUtil.init();
					dDateUtil.setDate_1(wkIntStartDate);
					dDateUtil.setDate_2(iNextPayIntDate);
					dDateUtil.dateDiff();
					// 不滿一期
					if (dDateUtil.getMons() == 0) {
						wkTerms = 1;
						wkProcessCode = 1;
						wkIntEndDate = iNextPayIntDate;
						this.info("specifyPayTermsRoutine-1");
						specifyTermsRoutine();
					} else {
						wkTerms = 1;
						wkProcessCode = 2;
						this.info("specifyPayTermsRoutine-2 ");
						specifyTermsRoutine();
						// 計算到新起日的相對日
						dDateUtil.init();
						dDateUtil.setDate_1(wkSpecificDate);
						dDateUtil.setDate_2(wkIntStartDate);
						dDateUtil.dateDiffSp();
						this.info("specifyPayTermsRoutine first term dateDiffSp " + wkSpecificDate + "~"
								+ wkIntStartDate + " =" + dDateUtil.getMons() + "/" + dDateUtil.getDays());
						wkMons = dDateUtil.getYears() * 12 + dDateUtil.getMons();
						this.info("specifyPayTermsRoutine-3 ");
						// 有零星日，計算滿一期的止日
						if (dDateUtil.getDays() > 0) {
							dDateUtil.init();
							dDateUtil.setDate_1(wkSpecificDate);
							dDateUtil.setMons(wkMons + 1); // 相當月數 + 1
							wkIntEndDate = dDateUtil.getCalenderDay();
							wkTerms = 1;
							wkProcessCode = 1;
							this.info("specifyPayTermsRoutine-4");
							specifyTermsRoutine();
						}
					}
				}
			}
		}
		if (isFirstTermMonth) {
			// 首月 = > 前段已處理第一期
			isFirstTermMonth = false;// 是否為首月
			if (iTerms > 1) {
				wkTerms = iTerms - 1;
				wkProcessCode = iFreqBase;
				specifyTermsRoutine();
			}
		} else {
			// 計息期數 = 回收期數
			wkTerms = iTerms;
			wkProcessCode = iFreqBase;
			specifyTermsRoutine();
		}
	}

	// 指定繳息止日處理
	private void specifyPayIntDateRoutine() throws LogicException {
		this.info("specifyPayIntDateRoutine ... ");
		this.info("   iAmortizedCode = " + iAmortizedCode);
		this.info("   iFreqBase      = " + iFreqBase);

		if (iAmortizedCode == 2) { // 2.到期取息(到期繳息還本)
			wkTerms = 1;
			wkProcessCode = 1;
			specifyTermsRoutine();
			return;
		}
		switch (iFreqBase) {
		case 2:
			specifyPayIntDateMonthRoutine();
			break;
		case 3:
			specifyPayIntDateWeekRoutine();
			break;
		}
		this.info("specifyPayIntDateRoutine end ");
	}

	private void specifyPayIntDateMonthRoutine() throws LogicException {
		this.info("specifyPayIntDateMonthRoutine ... ");
		this.info("iSpecificDate=" + iSpecificDate + ", wkSpecificDate=" + wkSpecificDate + ", wkSpecificMons="
				+ wkSpecificMons);
		this.info("   wkIntStartDate = " + wkIntStartDate);
		this.info("   wkIntEndDate   = " + wkIntEndDate);

		int wkMons = 0;
		int wkdays = 0;
		int wkDate = 0;

		// ------ 計算一天利息 ---------------
		if (wkIntStartDate == wkIntEndDate) {
			wkTerms = 1;
			wkProcessCode = 1;
			this.info("specifyPayIntDateMonth-0 ");
			specifyTermsRoutine();
			return;
		}

		// 計算起日到指定基準日期的零星日
		if (wkIntStartDate > wkSpecificDate) {
			dDateUtil.init();
			dDateUtil.setDate_1(wkSpecificDate);
			dDateUtil.setDate_2(wkIntStartDate);
			dDateUtil.dateDiffSp();
			this.info("specifyPayIntDateMonth dateDiffSp " + wkSpecificDate + "~" + wkIntStartDate + " ="
					+ dDateUtil.getYears() + "/" + dDateUtil.getMons() + "/" + dDateUtil.getDays());
			wkdays = dDateUtil.getDays();
		}
		// -------------------- 計算前段月差一期 ------------------------
		// 期款第一期 => 月差1期 +相對日的零星日
		if (wkIntStartDate < iFirstDueDate || wkdays > 0) {
			isFirstTermMonth = true;// 是否為首月
			// 按日計息首月不分段
			if ("1".equals(iIntCalcCode)) {
				wkTerms = 1;
				wkProcessCode = 1;
				wkIntEndDate = iNextPayIntDate;
				this.info("specifyPayTermsRoutine-0");
				specifyTermsRoutine();
			} else {
				dDateUtil.init();
				dDateUtil.setDate_1(wkIntStartDate);
				dDateUtil.setMons(iPayIntFreq); // 月數=一期
				wkDate = dDateUtil.getCalenderDay();
				// 起日至止日不滿一期
				if (wkIntEndDate < wkDate) {
					wkTerms = 1;
					wkProcessCode = 1;
					wkIntEndDate = iIntEndDate;
					this.info("specifyPayIntDateMonth0-1");
					specifyTermsRoutine();
					return;
				}
				dDateUtil.init();
				dDateUtil.setDate_1(wkIntStartDate);
				dDateUtil.setDate_2(iNextPayIntDate);
				dDateUtil.dateDiff();
				// 第一期不滿月
				if (dDateUtil.getMons() == 0) {
					wkTerms = 1;
					wkProcessCode = 1;
					wkIntEndDate = iNextPayIntDate;
					this.info("specifyPayTermsRoutine-1");
					specifyTermsRoutine();
				} else {
					// 計算一期
					wkTerms = 1;
					wkProcessCode = 2;
					this.info("specifyPayIntDateMonth-2 ");
					specifyTermsRoutine();
				}
				if (wkIntEndDate >= iIntEndDate) {
					return;
				}
				wkIntEndDate = iIntEndDate;
				// 計算到新起日的相對日
				dDateUtil.init();
				dDateUtil.setDate_1(wkSpecificDate);
				dDateUtil.setDate_2(wkIntStartDate);
				dDateUtil.dateDiffSp();
				this.info("specifyPayIntDateMonth first term dateDiffSp " + wkSpecificDate + "~" + wkIntStartDate + " ="
						+ dDateUtil.getYears() + "/" + dDateUtil.getMons() + "/" + dDateUtil.getDays());
				wkMons = dDateUtil.getYears() * 12 + dDateUtil.getMons();
				// 有零星日，計算滿一期的止日
				if (dDateUtil.getDays() > 0) {
					dDateUtil.init();
					dDateUtil.setDate_1(wkSpecificDate);
					dDateUtil.setMons(wkMons + 1); // 相當月數 + 1
					wkDate = dDateUtil.getCalenderDay();
					wkTerms = 1;
					wkProcessCode = 1;
					wkIntEndDate = wkDate > iIntEndDate ? iIntEndDate : wkDate;
					this.info("specifyPayIntDateMonth0-3");
					specifyTermsRoutine();
					// 止日小於相當日
					if (wkIntEndDate >= iIntEndDate) {
						return;
					}
					wkIntEndDate = iIntEndDate;
				}
			}
		}
		
		// ----------------- 計算中段 ---------------
		isFirstTermMonth = false;// 是否為首月
		dDateUtil.init();
		dDateUtil.setDate_1(wkSpecificDate);
		dDateUtil.setDate_2(wkIntEndDate);
		dDateUtil.dateDiff();
		wkMons = dDateUtil.getMons(); // 止日相當月數

		dDateUtil.init();
		dDateUtil.setDate_1(wkSpecificDate);
		dDateUtil.setDate_2(wkIntStartDate);
		dDateUtil.dateDiff();
		wkMons = wkMons - dDateUtil.getMons(); // 總月數 = 止日相當月數 - 起日相當月數

		wkTerms = wkMons / iPayIntFreq;
		wkMonRemainder = wkMons % iPayIntFreq;

		this.info("specifyPayIntDateMonth wkTerms=" + wkTerms + ", wkIntStartDate=" + wkIntStartDate);
		if (wkTerms > 0) {
			wkProcessCode = 2;
			this.info("specifyPayIntDateMonth-4 wkTerms=" + wkTerms);
			specifyTermsRoutine();
		}
		if (wkMonRemainder > 0) {
			// 計算到新起日的相對日
			dDateUtil.init();
			dDateUtil.setDate_1(wkSpecificDate);
			dDateUtil.setDate_2(wkIntStartDate);
			dDateUtil.dateDiff();
			wkMons = dDateUtil.getMons();
			dDateUtil.init();
			dDateUtil.setDate_1(wkSpecificDate);
			dDateUtil.setMons(wkMonRemainder + wkMons); // 月數
			wkDate = dDateUtil.getCalenderDay();
			wkTerms = 1;
			wkProcessCode = 1;
			wkIntEndDate = wkDate;
			this.info("specifyPayIntDateMonth-4");
			specifyTermsRoutine();
			wkIntEndDate = iIntEndDate;
			wkMonRemainder = 0;
		}

		// ------ 計算後段零星日 ---------------
		if (iIntEndDate > wkIntStartDate) {
			wkTerms = 1;
			wkProcessCode = 1;
			wkIntEndDate = iIntEndDate;
			this.info("specifyPayIntDateMonth0-5");
			specifyTermsRoutine();
		}
		this.info("specifyPayIntDateMonthRoutine end ");
	}

	private void specifyPayIntDateWeekRoutine() throws LogicException {
		this.info("specifyPayIntDateWeekRoutine ... ");
		this.info("   iSpecificDate  = " + iSpecificDate + "/" + wkSpecificDate + "/" + wkSpecificMons);
		this.info("   wkIntStartDate = " + wkIntStartDate);
		this.info("   wkIntEndDate   = " + wkIntEndDate);

		int wkDays = 0;
		int wkDate = 0;
		int wkQuotient = 0;
		int wkRemainder = 0;

		dDateUtil.init();
		if (wkIntStartDate < iSpecificDate) {
			dDateUtil.setDate_1(wkIntStartDate);
		} else {
			dDateUtil.setDate_1(iSpecificDate);
		}
		dDateUtil.setDate_2(wkIntStartDate);
		dDateUtil.dateDiff();
		wkDays = dDateUtil.getDays();
		wkQuotient = wkDays / (iPayIntFreq * 7);
		wkRemainder = wkDays % (iPayIntFreq * 7);
		this.info("1.wkDays=" + wkDays + ", wkQuotient=" + wkQuotient + ", wkRemainder=" + wkRemainder);

		if (wkRemainder > 0) {
			dDateUtil.init();
			dDateUtil.setDate_1(iSpecificDate);
			dDateUtil.setDays((wkQuotient + 1) * iPayIntFreq * 7);
			wkDate = dDateUtil.getCalenderDay();
			if (wkIntEndDate < wkDate) {
				wkTerms = 1;
				wkProcessCode = 1;
				specifyTermsRoutine();
				return;
			}
			wkTerms = 1;
			wkProcessCode = 1;
			wkIntEndDate = wkDate;
			specifyTermsRoutine();
		}
		wkIntEndDate = iIntEndDate;
		dDateUtil.init();
		dDateUtil.setDate_1(wkIntStartDate);
		dDateUtil.setDate_2(wkIntEndDate);
		dDateUtil.dateDiff();
		wkDays = dDateUtil.getDays();
		wkQuotient = wkDays / (iPayIntFreq * 7);
		wkRemainder = wkDays % (iPayIntFreq * 7);
		this.info("2.wkDays=" + wkDays + ", wkQuotient=" + wkQuotient + ", wkRemainder=" + wkRemainder);
		if (wkQuotient == 0) {
			wkTerms = 1;
			wkProcessCode = 1;
			specifyTermsRoutine();
		} else {
			wkProcessCode = 3;
			wkTerms = wkQuotient;
			specifyTermsRoutine();
			if (iIntEndDate > wkIntStartDate) {
				wkTerms = 1;
				wkProcessCode = 1;
				wkIntEndDate = iIntEndDate;
				specifyTermsRoutine();
			}
		}
		this.info("specifyPayIntDateWeekRoutine end ");
	}

	// 指定繳息期數處理
	private void specifyTermsRoutine() throws LogicException {
		this.info("specifyTermsRoutine ... ");
		this.info("   iRateCode = " + iRateCode);
		this.info("   wkTerms   = " + wkTerms);
		this.info("   wkIntStartDate = " + wkIntStartDate);
		this.info("   wkIntEndDate   = " + wkIntEndDate);

		for (int i = 1; i <= wkTerms; i++) {
			wkTermIndex = i;
			specifyTermsRateRoutine();
			if (wkIntEndDate >= iMaturityDate) {
				break;
			}
		}

		this.info("specifyTermsRoutine end ");
	}

	private void specifyTermsRateRoutine() throws LogicException {
		this.info("specifyTermsRateRoutine ... ");
		this.info("   wkTermIndex = " + wkTermIndex + " wkTerms = " + wkTerms);
		this.info("   wkProcessCode     = " + wkProcessCode);
		this.info("   wkIntStartDate    = " + wkIntStartDate);
		this.info("   isFirstTermMonth  = " + isFirstTermMonth);
		this.info("   wkSpecificDate    = " + wkSpecificDate + ", wkSpecificMons=" + wkSpecificMons);
		switch (wkProcessCode) {
		case 1: // 1:指定收息止日
			dDateUtil.init();
			dDateUtil.setDate_1(wkIntStartDate);
			wkMonthLimit = dDateUtil.getMonLimit();
			break;
		case 2: // 2:週期為月
			wkIntEndDate = freqMonthRoutine();
			if (wkIntEndDate > iMaturityDate) {
				wkProcessCode = 1;
				dDateUtil.init();
				dDateUtil.setDate_1(wkIntStartDate);
				wkMonthLimit = dDateUtil.getMonLimit();
			} else {
				dDateUtil.init();
				dDateUtil.setDate_1(wkIntStartDate);
				dDateUtil.setDate_2(wkIntEndDate);
				dDateUtil.dateDiff();
				wkMonthLimit = dDateUtil.getDays();
				wkTermEndDate = wkIntEndDate;
			}
			break;
		case 3: // 3:週期為週
			wkMonthLimit = iPayIntFreq * 7;
			wkIntEndDate = freqWeekRoutine();
		}
		this.info("   wkIntEndDate    = " + wkIntEndDate);
		do {
			wkIntEndDateX = 0;
			findRateChangeRoutine(titaVo);
			if (wkIntEndDate > wkNextEffectDate) {
				wkIntEndDateX = wkIntEndDate;
				wkIntEndDate = wkNextEffectDate;
				isRateChange = true;
			} else {
				wkIntEndDateX = 0;
				isRateChange = false;

			}
			wkStoreRate = wkFitRate;

			execRepayDateRoutine();

			// 檢查是否有分段計息
			if (wkIntEndDateX > 0) {
				wkIntStartDate = wkIntEndDate;
				wkIntEndDate = wkIntEndDateX;
			}
			isRateChange = false;
		} while (wkIntEndDateX > 0);

		wkIntStartDate = wkIntEndDate;
		vCalcRepayIntVo = lCalcRepayIntVo.get(wkCalcVoIndex);

		// 若下次利率變動日剛好等於本段計息止日,紀錄下段利率,供下段期金計算時使用
		if (vCalcRepayIntVo.getEndDate() == wkNextEffectDate) {
			vCalcRepayIntVo.setNextStoreRate(wkNextFitRate);
		} else {
			vCalcRepayIntVo.setNextStoreRate(wkFitRate);
		}

		if (isFirstTermMonth) {
			if (wkIntEndDate == iNextPayIntDate) {
				vCalcRepayIntVo.setDuraFlag(1);
			}
		} else {
			if (wkProcessCode != 1) {
				vCalcRepayIntVo.setDuraFlag(1);
			}
		}

		lCalcRepayIntVo.set(wkCalcVoIndex, vCalcRepayIntVo);
		this.info("specifyTermsRateRoutine end wkCalcVoIndex=" + wkCalcVoIndex);
	}

	private int freqMonthRoutine() throws LogicException {
		this.info("freqMonthRoutine ... ");
		this.info("isFirstTermMonth = " + isFirstTermMonth);
		this.info("wkSpecificDate=" + wkSpecificDate + ", wkSpecificMons=" + wkSpecificMons);
		this.info("   wkIntStartDate = " + wkIntStartDate);
		this.info("   iPayIntFreq = " + iPayIntFreq);
		int wkMons = 0;
		int wkEndDate = 0;

		if (isFirstTermMonth) { // 首月
			dDateUtil.init();
			dDateUtil.setDate_1(wkIntStartDate);
			dDateUtil.setMons(iPayIntFreq);
			wkEndDate = dDateUtil.getCalenderDay();
		} else {
			dDateUtil.init();
			dDateUtil.setDate_1(wkSpecificDate);
			dDateUtil.setDate_2(wkIntStartDate);
			dDateUtil.dateDiff();
			wkMons = dDateUtil.getMons() - wkSpecificMons;
			wkMons += iPayIntFreq - (wkMons % iPayIntFreq);
			dDateUtil.init();
			dDateUtil.setDate_1(wkSpecificDate);
			dDateUtil.setMons(wkMons + wkSpecificMons);
			wkEndDate = dDateUtil.getCalenderDay();
		}

		this.info("   return wkEndDate = " + wkEndDate);
		return wkEndDate;
	}

	private int freqWeekRoutine() throws LogicException {
		this.info("freqWeekRoutine ... ");
		this.info("   iSpecificDate = " + iSpecificDate);
		this.info("   wkIntStartDate = " + wkIntStartDate);
		this.info("   iPayIntFreq = " + iPayIntFreq);

		int wkEndDate = 0;
		int wkDays = 0;
		int wkQuotient = 0;
		int wkRemainder = 0;

		if (wkIntStartDate == iSpecificDate) {
			dDateUtil.init();
			dDateUtil.setDate_1(wkIntStartDate);
			dDateUtil.setDays(iPayIntFreq * 7);
			wkEndDate = dDateUtil.getCalenderDay();
		} else {
			dDateUtil.init();
			dDateUtil.setDate_1(iSpecificDate);
			dDateUtil.setDate_2(wkIntStartDate);
			dDateUtil.dateDiff();
			wkDays = dDateUtil.getDays();
			wkQuotient = wkDays / (iPayIntFreq * 7);
			wkRemainder = wkDays % (iPayIntFreq * 7);
			dDateUtil.init();
			if (wkRemainder == 0) {
				dDateUtil.setDate_1(wkIntStartDate);
				dDateUtil.setDays(iPayIntFreq * 7);
			} else {
				if (wkQuotient == 0) {
					dDateUtil.setDate_1(iSpecificDate);
					dDateUtil.setDays(wkRemainder);
				} else {
					dDateUtil.setDate_1(iSpecificDate);
					dDateUtil.setDays(wkQuotient * 7 * iPayIntFreq);
				}
			}
			wkEndDate = dDateUtil.getCalenderDay();
		}

		this.info("   return wkEndDate = " + wkEndDate);
		return wkEndDate;
	}

	// 081400 CALINT-EXPRDT-RTN.
	private void execRepayDateRoutine() throws LogicException {
		this.info("execRepayDateRoutine ... ");
		this.info("   wkTermIndex = " + wkTermIndex);
		this.info("   wkIntStartDate = " + wkIntStartDate);
		this.info("   wkIntEndDate   = " + wkIntEndDate);

		do {
			wkIntEndDateY = 0;
			// if (iUnpaidFlag > 0) {
			// findRateChangeRoutine();
			// }
			execDayCountRoutine(); // 計算計息日數
			if (wkIntEndDateY > 0) {
				wkIntStartDate = wkIntEndDate;
				wkIntEndDate = wkIntEndDateY;
			}
			this.info("   wkIntEndDateY = " + wkIntEndDateY);
		} while (wkIntEndDateY > 0);
		this.info("execRepayDateRoutine end ");
	}

	// 計算計息日數
	private void execDayCountRoutine() throws LogicException {
		this.info("execDayCountRoutine ... ");
		this.info("   wkTermIndex = " + wkTermIndex);

		int wkFlag = 0;

		// 跨契約止日要分段計算
		if (wkIntStartDate < iMaturityDate && iMaturityDate < wkIntEndDate) {
			wkFlag = 1;
			if (wkTermIndex >= wkTerms) {
			}
			if (wkFlag == 1) {
				wkDate = wkIntEndDate;
				wkIntEndDate = iMaturityDate;
				dDateUtil.init();
				dDateUtil.setDate_1(wkIntStartDate);
				dDateUtil.setDate_2(wkIntEndDate);
				dDateUtil.dateDiff();
				wkDays = dDateUtil.getDays();
				wkType = 2;
				moveCalcDataRoutine();
				wkIntStartDate = iMaturityDate;
				wkIntEndDate = wkDate;
				vCalcRepayIntVo = lCalcRepayIntVo.get(wkCalcVoIndex);
				vCalcRepayIntVo.setDuraFlag(1);
				lCalcRepayIntVo.set(wkCalcVoIndex, vCalcRepayIntVo);
				this.info("   wkFlag == 1 " + wkIntStartDate + "~" + wkIntEndDate);
			}
			dDateUtil.init();
			dDateUtil.setDate_1(wkIntStartDate);
			dDateUtil.setDate_2(wkIntEndDate);
			dDateUtil.dateDiff();
			wkDays = dDateUtil.getDays();
			this.info("   wkFlag == 0 " + wkIntStartDate + "~" + wkIntEndDate);
			wkType = 2;
			moveCalcDataRoutine();
			wkIntStartDate = iMaturityDate;
		} else {
			if (wkIntEndDate == iMaturityDate) { // 契約到期
			}
			// 結案時,只計算到計息止日
			if (iCaseCloseFlag.equals("Y") && wkIntEndDate > iIntEndDate) {
				wkIntEndDate = iIntEndDate;
			}
			if (wkIntStartDate == wkIntEndDate && wkIntStartDate == iDrawdownDate) {
				wkDays = 1; // 當日借當日還算1天利息
				if (iExtraRepay.compareTo(BigDecimal.ZERO) > 0 && iExtraRepay.compareTo(iPrincipal) < 0) {
					wkDays = 0;
				}
			} else {
				dDateUtil.init();
				dDateUtil.setDate_1(wkIntStartDate);
				dDateUtil.setDate_2(wkIntEndDate);
				dDateUtil.dateDiff();
				wkDays = dDateUtil.getDays();
			}
			wkType = 2;
			moveCalcDataRoutine();
			wkIntStartDate = wkIntEndDate;
		}
		this.info("execDayCountRoutine end");
	}

	// 利率變動檔
	private void findRateChangeRoutine(TitaVo titaVo) throws LogicException {
		this.info("findRateChangeRoutine ... ");
		this.info("    RateCode=" + iRateCode);
		this.info("    CustNo=" + iCustNo);
		this.info("    FacmNo=" + iFacmNo);
		this.info("    BormNo=" + iBormNo);
		this.info("    wkIntStartDate    =" + wkIntStartDate);

		tLoanRateChange = new LoanRateChange();
		// 利息提存未到期利息以最後一段利率計算
		if (iIntEndCode == 2 && iIntEndDate > 0 && iIntEndDate > iEntryDate) {
			tLoanRateChange = loanRateChangeService.rateChangeEffectDateDescFirst(iCustNo, iFacmNo, iBormNo,
					iEntryDate + 19110000, titaVo);
		} else {
			tLoanRateChange = loanRateChangeService.rateChangeEffectDateDescFirst(iCustNo, iFacmNo, iBormNo,
					wkIntStartDate + 19110000, titaVo);
		}
		if (tLoanRateChange == null) {
			throw new LogicException(titaVo, "E3926",
					iCustNo + "-" + iFacmNo + "-" + iBormNo + " 無放款利率變動資料 = " + wkIntStartDate); // 計算利息錯誤，放款利率變動檔查無資料
		}

		wkFitRate = tLoanRateChange.getFitRate(); // 適用利率
		wkRateIncr = tLoanRateChange.getRateIncr(); // 加碼利率
		wkIndividualIncr = tLoanRateChange.getIndividualIncr(); // 個別加碼利率

		// 利率生效日<=撥款日
		if (tLoanRateChange.getEffectDate() <= iDrawdownDate) {
			wkBeforeStoreRate = tLoanRateChange.getFitRate();
		}

		this.info("   EffectDate        = " + tLoanRateChange.getEffectDate());
		this.info("   FitRate           = " + tLoanRateChange.getFitRate());

		dDateUtil.init();
		dDateUtil.setDate_1(tLoanRateChange.getEffectDate());
		dDateUtil.setDays(1);
		tLoanRateChange = loanRateChangeService.rateChangeEffectDateAscFirst(iCustNo, iFacmNo, iBormNo,
				dDateUtil.getCalenderDay() + 19110000, titaVo);
		// 利率相同跳過、找下一筆
		if (tLoanRateChange != null && tLoanRateChange.getFitRate().compareTo(wkFitRate) == 0) {
			dDateUtil.init();
			dDateUtil.setDate_1(tLoanRateChange.getEffectDate());
			dDateUtil.setDays(1);
			tLoanRateChange = loanRateChangeService.rateChangeEffectDateAscFirst(iCustNo, iFacmNo, iBormNo,
					dDateUtil.getCalenderDay() + 19110000, titaVo);
		}
		wkNextEffectDate = tLoanRateChange != null ? tLoanRateChange.getEffectDate() : 9991231;
		wkNextFitRate = tLoanRateChange != null ? tLoanRateChange.getFitRate() : wkFitRate;

		this.info("findRateChangeRoutine end");
	}

	private int CalcTermNo() throws LogicException {
		this.info("CalcTermNo ... ");
		this.info("   iFreqBase      = " + iFreqBase);
		this.info("   iSpecificDate  = " + iSpecificDate + "/" + wkSpecificDate + "/" + wkSpecificMons);
		this.info("   wkIntStartDate = " + wkIntStartDate);

		int wkMons = 0;
		int wkDays = 0;
		int wkTermNo = 0;

		if (wkIntStartDate <= iSpecificDate) {
			this.info("CalcTermNo end wkTermNo = 1 ");
			return 1;
		} else {
			dDateUtil.init();
			dDateUtil.setDate_1(wkSpecificDate);
			dDateUtil.setDate_2(wkIntStartDate);
			switch (iFreqBase) { // 週期基準 1:日 2:月 3:週
			case 2:
				dDateUtil.dateDiff();
				wkMons = dDateUtil.getMons();
				wkTermNo = ((wkMons - wkSpecificMons) / iPayIntFreq) + 1;
				break;
			case 3:
				dDateUtil.dateDiff();
				wkDays = dDateUtil.getDays();
				wkTermNo = (wkDays / (iPayIntFreq * 7)) + 1;
				break;
			}
		}
		this.info("CalcTermNo end wkTermNo = " + wkTermNo);
		return wkTermNo;
	}

	private void moveCalcDataRoutine() throws LogicException {
		this.info("moveCalcDataRoutine ... ");

		wkCalcVoIndex++;
		oPaidTerms = CalcTermNo();
		vCalcRepayIntVo = new CalcRepayIntVo();
		vCalcRepayIntVo.setCustNo(iCustNo);
		vCalcRepayIntVo.setFacmNo(iFacmNo);
		vCalcRepayIntVo.setBormNo(iBormNo);
		vCalcRepayIntVo.setTermNo(oPaidTerms);
		vCalcRepayIntVo.setType(wkType); // 種類
		vCalcRepayIntVo.setAmount(wkPrincipal); // 計算金額
		vCalcRepayIntVo.setStartDate(wkIntStartDate); // 計算起日
		vCalcRepayIntVo.setEndDate(wkIntEndDate); // 計算止日
		vCalcRepayIntVo.setDays(wkDays); // 計算日數
		vCalcRepayIntVo.setStoreRate(wkStoreRate); // 計算利率
		vCalcRepayIntVo.setRateIncr(wkRateIncr); // 加碼利率
		vCalcRepayIntVo.setIndividualIncr(wkIndividualIncr); // 個別加碼利率
		vCalcRepayIntVo.setPrincipal(BigDecimal.ZERO); // 應還本金
		vCalcRepayIntVo.setInterest(BigDecimal.ZERO); // 利息
		vCalcRepayIntVo.setBreachAmt(BigDecimal.ZERO); // 違約金
		vCalcRepayIntVo.setOdDays(0); // 違約日數
		vCalcRepayIntVo.setDelayInt(BigDecimal.ZERO); // 延遲息,延滯息
		vCalcRepayIntVo.setLoanBal(BigDecimal.ZERO); // 放款餘額
		vCalcRepayIntVo.setBreachGetCode("0"); // 清償違約金收取方式 1:即時收取 2:領清償證明時收取
		vCalcRepayIntVo.setMonthLimit(wkMonthLimit); // 當月日數
		// 按月計息且(整期或雙月收息的零星月)為2:按月計息；否則為 1:按日計息
		vCalcRepayIntVo.setInterestFlag(wkInterestFlag == 2 && (wkProcessCode == 2 || wkMonRemainder > 0) ? 2 : 1);
		vCalcRepayIntVo.setExtraRepayFlag(0); // 部分償還金額記號 0:否 1:是
		// 預設分段計息記號 0:按日計息(零星日) 2:利率分段計息
		vCalcRepayIntVo.setDuraFlag(isRateChange ? 2 : 0); // 分段計息記號 0: 未滿期 1:按週/月計息(滿期) 2:利率分段計息
		vCalcRepayIntVo.setDueAmt(iDueAmt); // 期金
		vCalcRepayIntVo.setRateCode(iRateCode); // 利率區分 1: 機動 2: 固定 3: 定期機動
		lCalcRepayIntVo.add(vCalcRepayIntVo);
		oCalcCount = wkCalcVoIndex + 1;

		this.info("   wkCalcVoIndex   = " + wkCalcVoIndex);
		this.info("   wkIntStartDate  = " + wkIntStartDate);
		this.info("   wkIntEndDate    = " + wkIntEndDate);
		this.info("   wkMonthLimit    = " + wkMonthLimit);
		this.info("   oPaidTerms      = " + oPaidTerms);
		this.info("moveCalcDataRoutine end ");
	}

	// 填入計息本金
	private void fillPrincipalRoutine(int wkIndex) throws LogicException {
		this.info("fillPrincipalRoutine ... ");
		this.info("   wkIndex           = " + wkIndex);
		this.info("   termNo            = " + lCalcRepayIntVo.get(wkIndex).getTermNo());
		this.info("   wkPrincipal       = " + wkPrincipal);
		this.info("   wkDueAmt          = " + wkDueAmt);
		this.info("   wkNextRepayDate   = " + wkNextRepayDate);
		this.info("   iTotalPeriod      = " + iTotalPeriod);
		this.info("   getStatrDate()    = " + lCalcRepayIntVo.get(wkIndex).getStartDate());
		this.info("   getEndDate()      = " + lCalcRepayIntVo.get(wkIndex).getEndDate());

		int wkEndDate = 0;
		int wkMons = 0;
		int wkRemainder = 0;

		vCalcRepayIntVo = lCalcRepayIntVo.get(wkIndex);

		// 計息止日=下次還本日且計息止日<=到期日)，填入本金，否則不變
		if (!(vCalcRepayIntVo.getEndDate() == wkNextRepayDate || vCalcRepayIntVo.getEndDate() > iMaturityDate)) {
			this.info("   getPrincipal()  = " + lCalcRepayIntVo.get(wkIndex).getPrincipal());
			this.info("fillPrincipalRoutine end A");
			return;
		}

		// 計息止日>=到期日(=Only)，填餘額，往後刪除
		if (vCalcRepayIntVo.getEndDate() >= iMaturityDate) {
			vCalcRepayIntVo.setPrincipal(wkPrincipal);
			wkPrincipal = BigDecimal.ZERO;
			wkDueAmt = BigDecimal.ZERO;
			if (wkIndex < wkCalcVoCount) {
				for (int k = wkIndex + 1; k <= wkCalcVoCount; k++) {
					lCalcRepayIntVo.remove(wkIndex + 1);
				}
				wkCalcVoIndex = wkIndex;
				wkCalcVoCount = wkIndex;
				wkTerms = vCalcRepayIntVo.getTermNo();
				oCalcCount = wkCalcVoCount + 1;
			}
			this.info("fillPrincipalRoutine B ");
		}
		// 每期攤還金額比餘額小，填入每期攤還金額，否則填餘額
		else {
			if (wkDueAmt.compareTo(wkPrincipal) < 0) {
				vCalcRepayIntVo.setPrincipal(wkDueAmt);
				wkPrincipal = wkPrincipal.subtract(wkDueAmt);
				this.info("fillPrincipalRoutine C ");
			} else {
				vCalcRepayIntVo.setPrincipal(wkPrincipal);
				wkPrincipal = BigDecimal.ZERO;
				wkDueAmt = BigDecimal.ZERO;
				this.info("fillPrincipalRoutine D ");
			}
		}

		// 無還本週期下次還本日為9991231
		if (iRepayFreq == 99) {
			wkNextRepayDate = 9991231;
			this.info("   getPrincipal()  = " + lCalcRepayIntVo.get(wkIndex).getPrincipal());
			this.info("fillPrincipalRoutine end B");
			lCalcRepayIntVo.set(wkIndex, vCalcRepayIntVo);
			return;
		}

		// 計算下次還本日
		if (wkFreqCode == 2) { // 2:週期為月
			dDateUtil.init();
			dDateUtil.setDate_1(wkSpecificDate);
			dDateUtil.setDate_2(wkNextRepayDate);
			dDateUtil.dateDiff();
			wkMons = dDateUtil.getMons() - wkSpecificMons;
			wkRemainder = wkMons % iRepayFreq;
			dDateUtil.init();
			dDateUtil.setDate_1(wkSpecificDate);
			wkMons += iRepayFreq - wkRemainder;
			dDateUtil.setMons(wkMons + wkSpecificMons);
			wkEndDate = dDateUtil.getCalenderDay();
		}
		if (wkFreqCode == 3) { // 3:週期為週
			dDateUtil.init();
			dDateUtil.setDate_1(wkNextRepayDate);
			dDateUtil.setDays(iRepayFreq * 7);
			wkEndDate = dDateUtil.getCalenderDay();
			this.info("   wkFreqCode == 3, wkNextRepayDate=" + wkNextRepayDate + "iRepayFreq * 7=" + iRepayFreq * 7
					+ ",wkEndDate=" + wkEndDate);
		}
		// 預定還本日超過到期日為到期日
		if (wkNextRepayDate < iMaturityDate && wkEndDate > iMaturityDate) {
			wkNextRepayDate = iMaturityDate;
			oNextRepayDate = iMaturityDate;
		} else {
			wkNextRepayDate = wkEndDate;
			oNextRepayDate = wkEndDate;
		}
		vCalcRepayIntVo.setPrincipalFlag(1); // 還本記號 0:不還本 1:要還
		lCalcRepayIntVo.set(wkIndex, vCalcRepayIntVo);

		this.info("   getPrincipal()  = " + lCalcRepayIntVo.get(wkIndex).getPrincipal());
		this.info("   wkNextRepayDate = " + wkNextRepayDate);
		this.info("fillPrincipalRoutine end C");
	}

	// 調整計算金額(含期金調整)
	private void adjustAmtRoutine(int wkIndex) throws LogicException {
		this.info("adjustAmtRoutine ... ");
		this.info("   wkIndex           = " + wkIndex);
		this.info("   wkInterestFlag    = " + wkInterestFlag);
		this.info("   DuraFlag          = " + vCalcRepayIntVo.getDuraFlag());
		this.info("   isFirstTermMonth  = " + isFirstTermMonth);
		this.info("   Days              = " + lCalcRepayIntVo.get(wkIndex).getDays());
		this.info("   MonthLimit        = " + lCalcRepayIntVo.get(wkIndex).getMonthLimit());
		this.info("   termNo            = " + lCalcRepayIntVo.get(wkIndex).getTermNo());
		this.info("   wkLastTermNo      = " + wkLastTermNo);
		this.info("   wkDuraInt         = " + wkDuraInt);
		this.info("   iAmortizedCode    = " + iAmortizedCode);
		this.info("   wkBeforeStoreRate = " + wkBeforeStoreRate);
		this.info("   getStoreRate()    = " + lCalcRepayIntVo.get(wkIndex).getStoreRate());
		this.info("   getInterestFlag   = " + lCalcRepayIntVo.get(wkIndex).getInterestFlag());
		this.info("   Before Amount     = " + lCalcRepayIntVo.get(wkIndex).getAmount());
		this.info("   Before Principal  = " + lCalcRepayIntVo.get(wkIndex).getPrincipal());

		BigDecimal wkInterest = BigDecimal.ZERO;
		vCalcRepayIntVo = lCalcRepayIntVo.get(wkIndex);

		// 期數不同，中間息清零
		if (wkIndex == 0) {
			wkLastTermNo = vCalcRepayIntVo.getTermNo();
		} else if (wkLastTermNo != vCalcRepayIntVo.getTermNo()) {
			wkLastTermNo = vCalcRepayIntVo.getTermNo();
			wkDuraInt = BigDecimal.ZERO;
		}

		// 計算金額=計算金額減累計還本金額，不足清零
		if (vCalcRepayIntVo.getAmount().compareTo(wkAmt) > 0) {
			vCalcRepayIntVo.setAmount(vCalcRepayIntVo.getAmount().subtract(wkAmt));
		} else {
			vCalcRepayIntVo.setAmount(BigDecimal.ZERO);
		}
		// 還本金額=期金減利息 => 攤還方式 3.本息平均法(期金)
		if (iAmortizedCode == 3) {
			vCalcRepayIntVo.setDueAmt(oDueAmt);
			if (vCalcRepayIntVo.getPrincipalFlag() == 1) { // 還本記號 0:不還本 1:要還
				if (vCalcRepayIntVo.getEndDate() >= iMaturityDate) {
					vCalcRepayIntVo.setPrincipal(vCalcRepayIntVo.getAmount());
				} else {
					dDateUtil.init();
					dDateUtil.setDate_1(vCalcRepayIntVo.getStartDate());
					wkInterest = calcInterestRoutine().add(wkDuraInt);
					if (vCalcRepayIntVo.getDueAmt().compareTo(wkInterest) > 0) {
						if (vCalcRepayIntVo.getDueAmt().subtract(wkInterest)
								.compareTo(vCalcRepayIntVo.getAmount()) > 0) {
							vCalcRepayIntVo.setPrincipal(vCalcRepayIntVo.getAmount());
						} else {
							vCalcRepayIntVo.setPrincipal(vCalcRepayIntVo.getDueAmt().subtract(wkInterest));
						}
					} else {
						vCalcRepayIntVo.setPrincipal(new BigDecimal(0));
					}
				}
				wkDuraInt = BigDecimal.ZERO;
				this.info("   getDueAmt    = " + vCalcRepayIntVo.getDueAmt());
				this.info("   wkInterest   = " + wkInterest);
				this.info("   getStartDate = " + vCalcRepayIntVo.getStartDate());
				this.info("   getDays      = " + vCalcRepayIntVo.getDays());
				this.info("   getAmount    = " + vCalcRepayIntVo.getAmount());
			} else {
				wkDuraInt = wkDuraInt.add(calcInterestRoutine());
			}
		}
		// 累計還本金額
		if (vCalcRepayIntVo.getPrincipal().compareTo(BigDecimal.ZERO) > 0
				&& vCalcRepayIntVo.getEndDate() < iMaturityDate) {
			wkAmt = wkAmt.add(vCalcRepayIntVo.getPrincipal());
		}
		lCalcRepayIntVo.set(wkIndex, vCalcRepayIntVo);

		// 期數不同調整期金
		if (wkIndex == wkCalcVoCount || vCalcRepayIntVo.getTermNo() != lCalcRepayIntVo.get(wkIndex + 1).getTermNo()) {
			adjustDueAmtRoutine(titaVo);
		}

		this.info("   after getInterestFlag = " + vCalcRepayIntVo.getInterestFlag());
		this.info("   after getAmount       = " + vCalcRepayIntVo.getAmount());
		this.info("   after getPrincipal    = " + vCalcRepayIntVo.getPrincipal());
		this.info("adjustAmtRoutine end ");
	}

	// 調整期金
	private void adjustDueAmtRoutine(TitaVo titaVo) throws LogicException {
		// 攤還方式=3.本息平均法 且 攤還額異動碼=1:變 且利率有變動
		if (iAmortizedCode != 3 || !"1".equals(iExtraRepayCode)
				|| vCalcRepayIntVo.getNextStoreRate().compareTo(wkBeforeStoreRate) == 0) {
			return;
		}
		BigDecimal wkBal = vCalcRepayIntVo.getAmount().subtract(vCalcRepayIntVo.getPrincipal());
		// 無餘額、最後一期
		if (wkBal.compareTo(BigDecimal.ZERO) == 0 || vCalcRepayIntVo.getTermNo() >= iTotalPeriod) {
			return;
		}

		int wkRestPeriod = 0;
		int wkGracePeriod = 0;

		// 期數不同且利率有變動 ==> 須調整期金
		if (vCalcRepayIntVo.getNextStoreRate().compareTo(wkBeforeStoreRate) != 0) {
			// 過了寬限期，用剩餘期數計算；寬緩期內用總期數期減寬限期數計算
			if (vCalcRepayIntVo.getTermNo() > iGracePeriod) {
				wkRestPeriod = iTotalPeriod - vCalcRepayIntVo.getTermNo();
				wkGracePeriod = 0;
			} else {
				wkRestPeriod = iTotalPeriod;
				wkGracePeriod = iGracePeriod;
			}

			oDueAmt = loanDueAmtCom.getDueAmt(wkBal, vCalcRepayIntVo.getNextStoreRate(), "3", iFreqBase, wkRestPeriod,
					wkGracePeriod, iPayIntFreq, iFinalBal, titaVo);
			wkBeforeStoreRate = vCalcRepayIntVo.getNextStoreRate();
			this.info(" new DueAmt = " + oDueAmt);
		}
	}

	// 計算利息
	private BigDecimal calcInterestRoutine() {
		BigDecimal wkInterest = BigDecimal.ZERO;

		this.info("calcInterestRoutine ... ");
		this.info("   iPayIntFreq     = " + iPayIntFreq);
		this.info("   getInterestFlag = " + vCalcRepayIntVo.getInterestFlag());
		this.info("   getMonthLimit   = " + vCalcRepayIntVo.getMonthLimit());

		if (vCalcRepayIntVo.getEndDate() < iIntStartDate) {
			return BigDecimal.ZERO;
		}
		// 計算利息
		// if (vCalcRepayIntVo.getStartDate() < iMaturityDate ||
		// (vCalcRepayIntVo.getStartDate() == iDrawdownDate
		// && vCalcRepayIntVo.getEndDate() == iMaturityDate && vCalcRepayIntVo.getDays()
		// == 1)) {
		if (vCalcRepayIntVo.getInterestFlag() == 1) { // 按日計息

			// 變動小數位數處理
			// a = 利率的小數位數
			// b = 天數/36500後的小數位數
			// b = 9 - a
//			int b = getVariableDecimal(vCalcRepayIntVo.getStoreRate());

			// 最後決定固定9
			int b = 9;

			// 2022-02-25 智偉: 模仿AS400 運算過程中，最多到小數點後第九位，超過之位數，無條件捨去
			BigDecimal wkDaysDenominator = new BigDecimal(vCalcRepayIntVo.getDays()).divide(new BigDecimal(36500), b,
					RoundingMode.DOWN);

			wkInterest = vCalcRepayIntVo.getAmount().multiply(vCalcRepayIntVo.getStoreRate())
					.multiply(new BigDecimal(vCalcRepayIntVo.getDays()))
					.divide(new BigDecimal(36500), b, RoundingMode.DOWN).setScale(0, RoundingMode.HALF_UP);

			this.info("   StartDate  = " + vCalcRepayIntVo.getStartDate());
			this.info("   Days       = " + vCalcRepayIntVo.getDays());
			this.info("   Amount     = " + vCalcRepayIntVo.getAmount());
			this.info("   wkInterest = " + wkInterest);
			this.info("   wkDaysDenominator = " + wkDaysDenominator);
			this.info("   A算式 = " + vCalcRepayIntVo.getAmount());
			this.info("         * " + vCalcRepayIntVo.getStoreRate());
			this.info("         * " + vCalcRepayIntVo.getDays());
			this.info("         / 36500");
			this.info("   四捨五入前 = " + vCalcRepayIntVo.getAmount().multiply(vCalcRepayIntVo.getStoreRate())
					.multiply(new BigDecimal(vCalcRepayIntVo.getDays()))
					.divide(new BigDecimal(36500), b, RoundingMode.DOWN));
			this.info("   B算式 = " + vCalcRepayIntVo.getAmount());
			this.info("         * " + vCalcRepayIntVo.getStoreRate());
			this.info("         * " + wkDaysDenominator);
			this.info("   四捨五入前 = "
					+ vCalcRepayIntVo.getAmount().multiply(vCalcRepayIntVo.getStoreRate()).multiply(wkDaysDenominator));
		} else {
			dDateUtil.init();
			dDateUtil.setDate_1(vCalcRepayIntVo.getStartDate());

			// 2022-02-25 智偉: 模仿AS400 運算過程中，最多到小數點後第九位，超過之位數，無條件捨去
			BigDecimal wkMonthDenominator = new BigDecimal(1200)
					.multiply(new BigDecimal((iPayIntFreq == 99 ? 1 : iPayIntFreq) * vCalcRepayIntVo.getMonthLimit()));

			// 變動小數位數處理
			// a = 利率的小數位數
			// b = 天數/36500後的小數位數
			// b = 9 - a
//			int b = getVariableDecimal(vCalcRepayIntVo.getStoreRate());

			// 最後決定固定9
			int b = 9;

			wkInterest = vCalcRepayIntVo.getAmount().multiply(vCalcRepayIntVo.getStoreRate())
					.multiply(new BigDecimal(vCalcRepayIntVo.getDays()))
					.divide(wkMonthDenominator, b, RoundingMode.DOWN).setScale(0, RoundingMode.HALF_UP);

			this.info("   StartDate  = " + vCalcRepayIntVo.getStartDate());
			this.info("   Days       = " + vCalcRepayIntVo.getDays());
			this.info("   Amount     = " + vCalcRepayIntVo.getAmount());
			this.info("   wkInterest = " + wkInterest);
			this.info("   wkMonthDenominator = " + wkMonthDenominator);
		}
		this.info("calcInterestRoutine end ");
		return wkInterest;
	}

	// 計算違約金
	// (1) 貸款戶繳納本息期款有一寬限期間(5個營業日)，超過寬限日仍未繳納者，應按契約收取違約金。
	// (2) 中短期擔保放款，到期仍未還清或辦理展期者，應按契約約定收取違約金。
	// (3) 逾期6個月內罰欠繳金額之一成；逾期6個月以上罰欠繳金之二成，最高收9個月。

	private int fillBreachRoutine(int wkIndex) throws LogicException {
		this.info("fillBreachRoutine ... ");
		this.info("   wkIndex = " + wkIndex);
		this.info("   iBreachValidDate = " + iBreachValidDate);
		this.info("   iNextPayIntDate  = " + iNextPayIntDate);
		this.info("   iMaturityDate    = " + iMaturityDate);
		this.info("   getStartDate     = " + lCalcRepayIntVo.get(wkIndex).getStartDate());
		this.info("   getEndDate       = " + lCalcRepayIntVo.get(wkIndex).getEndDate());
		this.info("   DuraFlag         = " + lCalcRepayIntVo.get(wkIndex).getDuraFlag());
		this.info("   wkDuraInt        = " + wkDuraInt);

		vCalcRepayIntVo = lCalcRepayIntVo.get(wkIndex);
		vCalcRepayIntVo.setOdDays(0);
		vCalcRepayIntVo.setBreachAmt(BigDecimal.ZERO);
		// 區段止日>=違約金止日、且<=到期日，則不計
		if (vCalcRepayIntVo.getEndDate() >= iBreachValidDate && vCalcRepayIntVo.getEndDate() <= iMaturityDate) {
			lCalcRepayIntVo.set(wkIndex, vCalcRepayIntVo);
			this.info("fillBreachRoutine end A");
			return wkIndex;
		}
		// 利率變動、且有還本，設定應繳日為區段止日
		if (vCalcRepayIntVo.getDuraFlag() == 1 && vCalcRepayIntVo.getPrincipal().compareTo(BigDecimal.ZERO) > 0) {
			iNextPayIntDate = vCalcRepayIntVo.getEndDate();
			adjustBreachValidDateRoutine();
		}
		// 區段止日>=違約金止日、且<=應繳日，則不計
		if (iNextPayIntDate >= iBreachValidDate && vCalcRepayIntVo.getEndDate() <= iNextPayIntDate
				&& iNextPayIntDate <= iMaturityDate) {
			lCalcRepayIntVo.set(wkIndex, vCalcRepayIntVo);
			this.info("fillBreachRoutine end B");
			return wkIndex;
		}
		lCalcRepayIntVo.set(wkIndex, vCalcRepayIntVo);

		fillBreachInterestRoutine(wkIndex, titaVo); // 計算延遲息違約金

		this.info("fillBreachRoutine end ");
		return wkIndex;
	}

	// 計算違約金, 遲延息
	private void fillBreachInterestRoutine(int wkIndex, TitaVo titaVo) throws LogicException {
		this.info("fillBreachInterestRoutine ... ");
		this.info("   wkIndex = " + wkIndex);
		this.info("   iUsageCode       資金用途別     = " + iUsageCode);
		this.info("   iBreachValidDate 違約金生效日  = " + iBreachValidDate);
		this.info("   iNextPayIntDate  下次繳息日     = " + iNextPayIntDate);
		this.info("   iMaturityDate    貸放止日        = " + iMaturityDate);
		this.info("   getStartDate     計算起日        = " + lCalcRepayIntVo.get(wkIndex).getStartDate());
		this.info("   getEndDate       計算止日        = " + lCalcRepayIntVo.get(wkIndex).getEndDate());
		this.info("   getPrincipal     應還本金        = " + lCalcRepayIntVo.get(wkIndex).getPrincipal());
		this.info("   getInterest      利息               = " + lCalcRepayIntVo.get(wkIndex).getInterest());
		this.info("   wkDuraInt        中間息            = " + wkDuraInt);
		this.info("   getDuraFlag      分段計息記號  = " + lCalcRepayIntVo.get(wkIndex).getDuraFlag());
		this.info("   getStoreRate     年利率            = " + lCalcRepayIntVo.get(wkIndex).getStoreRate());

		AcReceivable acReceivable;
		BigDecimal shortPrincipal = BigDecimal.ZERO;

		int wkDays = 0;
		int wkOdDays = 0; // 逾期日數
		int wkDaysA = 0; // 逾期6個月內
		int wkDaysB = 0; // 逾期6個月~ 9個月內
		int wkDaysC = 0; // 逾期10個月以上
		int wkDaysLimitA = 0; // 逾期6個月日數
		int wkDaysLimitB = 0; // 逾期9個月日數
		int wkBreachStartDate = 0;
		int wkCount = 0; // 寬限期間(5個營業日)的日數
		int wkBreachGraceDays = 0; // 寬限期日數
		BigDecimal wkBreachAmtA = BigDecimal.ZERO;
		BigDecimal wkBreachAmtB = BigDecimal.ZERO;
		BigDecimal wkBreachAmtC = BigDecimal.ZERO;
		BigDecimal wkDelayInt = BigDecimal.ZERO;
		BigDecimal wkDelayBase = BigDecimal.ZERO;
		BigDecimal wkBreachBase = BigDecimal.ZERO;

		vCalcRepayIntVo = lCalcRepayIntVo.get(wkIndex);
		// 計算違約金起算日，計算起日>=到期日，為計算起日，否則為計算止日
		wkBreachStartDate = vCalcRepayIntVo.getStartDate() >= iMaturityDate ? vCalcRepayIntVo.getStartDate()
				: vCalcRepayIntVo.getEndDate();
		this.info("   wkBreachStartDate   違約金起算日  = " + wkBreachStartDate);

		// 1.計算違約金起算日 >= 違約金生效日，不處理
		if (wkBreachStartDate >= iBreachValidDate) {
			this.info("fillBreachInterestRoutine end 1");
			return;
		}

		// 2.滿期，才處理，否則累計中間息
		if (vCalcRepayIntVo.getDuraFlag() != 1) {
			wkDuraInt = wkDuraInt.add(vCalcRepayIntVo.getInterest());
			this.info("fillBreachInterestRoutine end 2");
			return;
		}

		// 3.計算止日 < 下次繳息日，不處理
		if (vCalcRepayIntVo.getEndDate() < iNextPayIntDate) {
			wkDuraInt = wkDuraInt.add(vCalcRepayIntVo.getInterest());
			this.info("fillBreachInterestRoutine end 3");
			return;
		}

		// 計算逾期日數 = 計算違約金起算日 ~ 違約金生效日
		dDateUtil.init();
		dDateUtil.setDate_1(wkBreachStartDate);
		dDateUtil.setDate_2(iBreachValidDate);
		dDateUtil.dateDiff();
		wkOdDays = dDateUtil.getDays();
		this.info("   wkOdDays  逾期日數   = " + wkOdDays);

		// 4.逾期日數(違約金起算日到違約金生效日) <= 寬限期間(5個日曆日)，不處理
		if (wkOdDays <= iBreachGraceDays) {
			this.info("fillBreachInterestRoutine end 4");
			return;
		}

		// 計算寬限期間(5個營業日)的日數
		wkBreachGraceDays = 0;
		if (iBreachGraceDays > 0) {
			wkCount = 0;
			wkDays = 0;
			do {
				wkDays++;
				dDateUtil.init();
				dDateUtil.setDate_1(wkBreachStartDate);
				dDateUtil.setDays(wkDays);
				wkDate = dDateUtil.getCalenderDay();
				dDateUtil.init();
				dDateUtil.setDate_2(wkDate);
				if (!dDateUtil.isHoliDay()) {
					wkCount++;
				}
			} while (wkCount < iBreachGraceDays);
			dDateUtil.init();
			dDateUtil.setDate_1(wkBreachStartDate);
			dDateUtil.setDate_2(wkDate);
			dDateUtil.dateDiff();
			wkBreachGraceDays = dDateUtil.getDays();
		}
		this.info("   wkGraceDays   寬限期間(5個營業日)的日數  = " + wkBreachGraceDays);

		// 5.逾期日數(違約金起算日到違約金生效日) <= 計算寬限期間(5個營業日)的日數，不處理
		if (wkOdDays <= wkBreachGraceDays) {
			this.info("fillBreachInterestRoutine end 5");
			return;
		}
		vCalcRepayIntVo.setOdDays(wkOdDays);

		// 計算6個月之實際日數 LimitA
		// 違約金起算日 ~ (違約金起算日+6個月的相對日)
		dDateUtil.init();
		dDateUtil.setDate_1(wkBreachStartDate);
		dDateUtil.setMons(6);
		wkDate = dDateUtil.getCalenderDay();
		dDateUtil.init();
		dDateUtil.setDate_1(wkBreachStartDate);
		dDateUtil.setDate_2(wkDate);
		dDateUtil.dateDiff();
		wkDaysLimitA = dDateUtil.getDays();

		// 計算9個月之實際日數 LimitB
		dDateUtil.init();
		dDateUtil.setDate_1(wkBreachStartDate);
		dDateUtil.setMons(9);
		wkDate = dDateUtil.getCalenderDay();
		dDateUtil.init();
		dDateUtil.setDate_1(wkBreachStartDate);
		dDateUtil.setDate_2(wkDate);
		dDateUtil.dateDiff();
		wkDaysLimitB = dDateUtil.getDays();

		// 逾期日數 wkDays
		wkDays = vCalcRepayIntVo.getOdDays();
// 延遲息計算公式
// 購置不動產                         非購置不動產
// 本金*年利率/365*逾期日數延遲息)   (本金+利息)*年利率/365*逾期日數	
		// 資金用途別 1: 週轉金2: 購置不動產3: 營業用資產4: 固定資產5: 企業投資6: 購置動產9: 其他
		if (iUsageCode.equals("02")) {
			wkDelayBase = vCalcRepayIntVo.getPrincipal();
		} else {
			wkDelayBase = vCalcRepayIntVo.getPrincipal().add(vCalcRepayIntVo.getInterest().add(wkDuraInt));
		}
		// 若為最後一筆，且此戶已到期
		if (wkIndex == wkCalcVoCount && iMaturityDate == vCalcRepayIntVo.getEndDate()) {
			acReceivable = sAcReceivableService.acctCodeLikeFirst("Z%", vCalcRepayIntVo.getCustNo(),
					vCalcRepayIntVo.getFacmNo(), parse.IntegerToString(vCalcRepayIntVo.getBormNo(), 3), titaVo);
			if (acReceivable != null) {
				shortPrincipal = acReceivable.getRvBal();
				wkDelayBase = wkDelayBase.subtract(shortPrincipal);
				this.info("已到期且為最後一筆,計算本金減去短收本金");
			}
		}
		// 2022-03-11 智偉: 模仿AS400 運算過程中，最多到小數點後第九位，超過之位數，無條件捨去
//		BigDecimal wkDaysDenominator = new BigDecimal(wkDays).divide(new BigDecimal(36500), 9, RoundingMode.DOWN);

		wkDelayInt = wkDelayBase.multiply(vCalcRepayIntVo.getStoreRate()).multiply(new BigDecimal(wkDays))
				.divide(new BigDecimal(36500), 9, RoundingMode.DOWN).setScale(9, RoundingMode.DOWN);

// 違約金計算公式 
//      資金用途別               非購置不動產                       購置不動產(非寬限期內)        購置不動產(寬限期內)  
//   	逾期6個月內              應繳期款(本金+利息)*年利率/365*逾期日數*0.1   本金*年利率/365*逾期日數*0.1         利息*年利率/365*逾期日數*0.1
//		逾期6個月~9個月內    應繳期款(本金+利息)*年利率/365*逾期日數*0.2   本金*年利率/365*逾期日數*0.2         利息*年利率/365*逾期日數*0.2
//		逾期10個月以上         應繳期款(本金+利息)*年利率/365*逾期日數*0.2   本金*年利率/365*9個月之日數*0.2   利息*年利率/365*9個月之日數*0.2  

		this.info("違約金當期計算基礎判斷：本筆用途別 = " + iUsageCode);

		if (iUsageCode.equals("02")) {
			// 用途別為 購置不動產者
			if (vCalcRepayIntVo.getPrincipal().compareTo(BigDecimal.ZERO) > 0) {
				this.info("違約金當期計算基礎判斷：購置不動產者，非寬限期內，當期計算基礎為當期應繳本金");
				// 非寬限期內
				// 計算基礎為當期應繳本金
				wkBreachBase = vCalcRepayIntVo.getPrincipal();
			} else {
				this.info("違約金當期計算基礎判斷：購置不動產者，寬限期內，當期計算基礎為當期應繳利息");
				// 寬限期內
				// 計算基礎為當期應繳利息
				wkBreachBase = vCalcRepayIntVo.getInterest().add(wkDuraInt);
			}
		} else {
			this.info("違約金當期計算基礎判斷：非購置不動產者，當期計算基礎為當期應繳期款(本金+利息)");
			// 用途別 非購置不動產者
			// 計算基礎為當期應繳期款(本金+利息)
			wkBreachBase = vCalcRepayIntVo.getPrincipal().add(vCalcRepayIntVo.getInterest().add(wkDuraInt));
		}
		// 若為最後一筆，且此戶已到期
		if (wkIndex == wkCalcVoCount && iMaturityDate == vCalcRepayIntVo.getEndDate()) {
			if (shortPrincipal.compareTo(BigDecimal.ZERO) != 0) {
				wkBreachBase = wkBreachBase.subtract(shortPrincipal);
				this.info("已到期且為最後一筆,違約金計算本金減去短收本金");
			}
		}

		this.info("違約金當期計算基礎結果：wkBreachBase = " + wkBreachBase);

		this.info("違約金當期計算天數判斷： iUsageCode = " + iUsageCode);
		this.info("違約金當期計算天數判斷： wkDays = " + wkDays);
		this.info("違約金當期計算天數判斷： wkDaysLimitA = " + wkDaysLimitA);
		this.info("違約金當期計算天數判斷： wkDaysLimitB = " + wkDaysLimitB);
		// 逾期6個月內
		if (wkDays <= wkDaysLimitA) {
			this.info("違約金當期計算天數判斷：逾期６個月內");
			// 6個月內的天數用A段算
			wkDaysA = wkDays;
			wkDaysB = 0;
			wkDaysC = 0;
		} else {
			// 逾期6個月~9個月內
			if (wkDays <= wkDaysLimitB) {
				this.info("違約金當期計算天數判斷：逾期６～９個月");
				// 2022-04-21 智偉修改:逾期6~9個月時
				// 6個月內的天數用A段算
				// 6~9個月的天數用B段算
				wkDaysA = wkDaysLimitA;
				wkDaysB = wkDays - wkDaysLimitA;
				wkDaysC = 0;
			} else {
				this.info("違約金當期計算天數判斷：超過９個月");
				// 超過9個月的情況
				if (iUsageCode.equals("02")) {
					this.info("違約金當期計算天數判斷：用途別為購置不動產者，最多收9個月");
					// 2022-04-21 智偉修改:用途別為購置不動產者
					// 最多收9個月
					// 6個月內的天數用A段算
					// 6~9個月天數用B段算
					// 超過9個月的天數不算
					wkDaysA = wkDaysLimitA;
					wkDaysB = wkDaysLimitB - wkDaysLimitA;
					wkDaysC = 0;
				} else {
					this.info("違約金當期計算天數判斷：用途別為非購置不動產者，天數無上限");
					// 2022-04-21 智偉修改:用途別為 非購置不動產者
					// 天數無上限
					// 6個月內的天數用A段算
					// 6~9個月天數用B段算
					// 超過9個月的天數用C段算
					wkDaysA = wkDaysLimitA;
					wkDaysB = wkDaysLimitB - wkDaysLimitA;
					wkDaysC = wkDays - wkDaysLimitB;
				}
			}
		}
		this.info("違約金當期計算天數結果： wkDaysA = " + wkDaysA);
		this.info("違約金當期計算天數結果： wkDaysB = " + wkDaysB);
		this.info("違約金當期計算天數結果： wkDaysC = " + wkDaysC);

		// 2022-03-11 智偉: 模仿AS400 運算過程中，最多到小數點後第九位，超過之位數，無條件捨去
//		BigDecimal wkDaysDenominatorA = new BigDecimal(wkDaysA).divide(new BigDecimal(36500), 9, RoundingMode.DOWN);
//		BigDecimal wkDaysDenominatorB = new BigDecimal(wkDaysB).divide(new BigDecimal(36500), 9, RoundingMode.DOWN);
//		BigDecimal wkDaysDenominatorC = new BigDecimal(wkDaysC).divide(new BigDecimal(36500), 9, RoundingMode.DOWN);

		this.info("違約金與延遲息判斷： wkDelayBase = " + wkDelayBase);
		this.info("違約金與延遲息判斷： wkBreachBase = " + wkBreachBase);
		this.info("違約金與延遲息判斷： wkDays = " + wkDays);
		this.info("違約金與延遲息判斷： wkDaysA = " + wkDaysA);
		this.info("違約金與延遲息判斷： wkDaysB = " + wkDaysB);
		this.info("違約金與延遲息判斷： wkDaysC = " + wkDaysC);
		if (wkDelayBase.compareTo(wkBreachBase) == 0 && wkDays == wkDaysA + wkDaysB + wkDaysC) {
			this.info("延遲息與違約金計算基礎及計算天數皆相同");
			// 2022-04-13 智偉增加
			// 計息止日 至 入帳日期 之間若有利率變動，需分段計算違約金與延遲息
			int tempEndDate = vCalcRepayIntVo.getEndDate();
			dDateUtil.init();
			dDateUtil.setDate_1(tempEndDate);
			dDateUtil.setDays(wkDays);
			int tempEntryDate = dDateUtil.getCalenderDay();
//			iCustNo + "-" + iFacmNo + "-" + iBormNo
			Slice<LoanRateChange> sLoanRateChange = loanRateChangeService.rateChangeEffectDateRange(iCustNo, iFacmNo,
					iFacmNo, iBormNo, iBormNo, tempEndDate + 19110000, tempEntryDate + 19110000, 0, Integer.MAX_VALUE,
					titaVo);
			List<LoanRateChange> listLoanRateChange = sLoanRateChange == null ? new ArrayList<>()
					: new ArrayList<>(sLoanRateChange.getContent());

			// 2022-04-13 智偉增加
			// 若無利率變動資料,照舊
			if (listLoanRateChange == null || listLoanRateChange.isEmpty()) {
				// 2022-03-14 智偉修改
				// 因為原系統延遲息及違約金為一個欄位運算及顯示
				// 然而，新系統需求是要拆分為兩個欄位顯示
				// 若分開運算後各自四捨五入，合計後會比原本合計多一元
				// 這裡原本係數是 0.1 改為1.1 ，0.2改為1.2
				// 下段運算再減去延遲息所代表的1的部分
				wkBreachAmtA = wkBreachBase.multiply(vCalcRepayIntVo.getStoreRate()).multiply(new BigDecimal(wkDaysA))
						.divide(new BigDecimal(36500), 9, RoundingMode.DOWN).multiply(new BigDecimal("1.1"))
						.setScale(9, RoundingMode.DOWN); // AS400 運算過程小數位數最多九位
				wkBreachAmtB = wkBreachBase.multiply(vCalcRepayIntVo.getStoreRate()).multiply(new BigDecimal(wkDaysB))
						.divide(new BigDecimal(36500), 9, RoundingMode.DOWN).multiply(new BigDecimal("1.2"))
						.setScale(9, RoundingMode.DOWN); // AS400 運算過程小數位數最多九位
				wkBreachAmtC = wkBreachBase.multiply(vCalcRepayIntVo.getStoreRate()).multiply(new BigDecimal(wkDaysC))
						.divide(new BigDecimal(36500), 9, RoundingMode.DOWN).multiply(new BigDecimal("1.2"))
						.setScale(9, RoundingMode.DOWN); // AS400 運算過程小數位數最多九位

				if (wkDaysA > 0) {
					this.info("A段延遲息及違約金 = " + wkBreachAmtA + " = " + wkBreachBase + " * "
							+ vCalcRepayIntVo.getStoreRate() + " * " + wkDaysA + " / 36500 * 1.1 ");
				}
				if (wkDaysB > 0) {
					this.info("B段延遲息及違約金 = " + wkBreachAmtB + " = " + wkBreachBase + " * "
							+ vCalcRepayIntVo.getStoreRate() + " * " + wkDaysB + " / 36500 * 1.2 ");
				}
				if (wkDaysC > 0) {
					this.info("C段延遲息及違約金 = " + wkBreachAmtC + " = " + wkBreachBase + " * "
							+ vCalcRepayIntVo.getStoreRate() + " * " + wkDaysC + " / 36500 * 1.2 ");
				}
			} else {

				listLoanRateChange.sort((c1, c2) -> {
					if (c1.getEffectDate() > c2.getEffectDate()) {
						return 1;
					}
					if (c1.getEffectDate() < c2.getEffectDate()) {
						return -1;
					}
					return 0;
				});

				// 2022-04-13 智偉增加
				// 若有利率變動資料,分段算
				// 違約金A
				// 違約金B
				// 違約金C
				// 延遲息
				int startDateA = tempEndDate;// 違約金A-起日
				dDateUtil.init();
				dDateUtil.setDate_1(startDateA);
				dDateUtil.setDays(wkDaysA);
				int endDateA = dDateUtil.getCalenderDay();// 違約金A-止日

				int startDateB = endDateA;// 違約金B-起日
				dDateUtil.init();
				dDateUtil.setDate_1(startDateB);
				dDateUtil.setDays(wkDaysB);
				int endDateB = dDateUtil.getCalenderDay();// 違約金B-止日

				int startDateC = endDateB;// 違約金C-起日
				dDateUtil.init();
				dDateUtil.setDate_1(startDateC);
				dDateUtil.setDays(wkDaysC);
				int endDateC = dDateUtil.getCalenderDay();// 違約金C-止日

				int startDateD = tempEndDate;// 延遲息-起日
				int endDateD = tempEntryDate;// 延遲息-止日

				this.info("startDateA=" + startDateA);
				this.info("endDateA=" + endDateA);
				this.info("startDateB=" + startDateB);
				this.info("endDateB=" + endDateB);
				this.info("startDateC=" + startDateC);
				this.info("endDateC=" + endDateC);
				this.info("startDateD=" + startDateD);
				this.info("endDateD=" + endDateD);

				wkDelayInt = BigDecimal.ZERO;
				BigDecimal lastRateA = vCalcRepayIntVo.getStoreRate();
				BigDecimal lastRateB = vCalcRepayIntVo.getStoreRate();
				BigDecimal lastRateC = vCalcRepayIntVo.getStoreRate();
				BigDecimal lastRateD = vCalcRepayIntVo.getStoreRate();
				for (LoanRateChange tempLoanRateChange : listLoanRateChange) {

					int tempEffectDate = tempLoanRateChange.getEffectDate();
					BigDecimal tempRate = tempLoanRateChange.getFitRate();

					this.info("tempEffectDate=" + tempEffectDate);
					this.info("lastRateA=" + lastRateA);
					this.info("tempRate=" + tempRate);

					// 若利率變動日在違約金A區間
					if (tempEffectDate >= startDateA && tempEffectDate <= endDateA) {

						endDateA = tempEffectDate;
						dDateUtil.init();
						dDateUtil.setDate_1(startDateA);
						dDateUtil.setDate_2(endDateA);
						dDateUtil.dateDiff();
						if (dDateUtil.getDays() > 0) {
							this.info("A段延遲息及違約金(多段) startDateA = " + startDateA);
							this.info("A段延遲息及違約金(多段) endDateA = " + endDateA);
							this.info("A段延遲息及違約金(多段) 天數 = " + dDateUtil.getDays());
							wkBreachAmtA = wkBreachAmtA
									.add(wkBreachBase.multiply(lastRateA).multiply(new BigDecimal(dDateUtil.getDays()))
											.divide(new BigDecimal(36500), 9, RoundingMode.DOWN)
											.multiply(new BigDecimal("1.1")).setScale(0, RoundingMode.HALF_UP)); // AS400
																													// 運算過程小數位數最多九位
							this.info("A段延遲息及違約金(多段) = " + wkBreachBase + " * " + lastRateA + " * "
									+ dDateUtil.getDays() + " / 36500 * 1.1 ");
							this.info("A段延遲息及違約金(多段) 四捨五入前 = "
									+ wkBreachBase.multiply(lastRateA).multiply(new BigDecimal(dDateUtil.getDays()))
											.divide(new BigDecimal(36500), 9, RoundingMode.DOWN)
											.multiply(new BigDecimal("1.1")).setScale(0, RoundingMode.HALF_UP));
						}
						startDateA = tempEffectDate;
						endDateA = tempEntryDate;
						lastRateA = tempRate;
					}
					// 若利率變動日在違約金B區間
					if (tempEffectDate >= startDateB && tempEffectDate <= endDateB) {

						endDateB = tempEffectDate;
						dDateUtil.init();
						dDateUtil.setDate_1(startDateB);
						dDateUtil.setDate_2(endDateB);
						dDateUtil.dateDiff();
						if (dDateUtil.getDays() > 0) {
							this.info("B段延遲息及違約金(多段) startDateB = " + startDateB);
							this.info("B段延遲息及違約金(多段) endDateB = " + endDateB);
							this.info("B段延遲息及違約金(多段) 天數 = " + dDateUtil.getDays());
							wkBreachAmtB = wkBreachAmtB
									.add(wkBreachBase.multiply(lastRateB).multiply(new BigDecimal(dDateUtil.getDays()))
											.divide(new BigDecimal(36500), 9, RoundingMode.DOWN)
											.multiply(new BigDecimal("1.2")).setScale(0, RoundingMode.HALF_UP)); // AS400
																													// 運算過程小數位數最多九位
							this.info("B段延遲息及違約金(多段) 算式 = " + wkBreachBase + " * " + lastRateB + " * "
									+ dDateUtil.getDays() + " / 36500 * 1.2 ");
							this.info("B段延遲息及違約金(多段) 此段 = "
									+ wkBreachBase.multiply(lastRateB).multiply(new BigDecimal(dDateUtil.getDays()))
											.divide(new BigDecimal(36500), 9, RoundingMode.DOWN)
											.multiply(new BigDecimal("1.2")).setScale(0, RoundingMode.HALF_UP));
						}
						startDateB = tempEffectDate;
						endDateB = tempEntryDate;
						lastRateB = tempRate;
					}
					// 若利率變動日在違約金C區間
					if (tempEffectDate >= startDateC && tempEffectDate <= endDateC) {

						endDateC = tempEffectDate;
						dDateUtil.init();
						dDateUtil.setDate_1(startDateC);
						dDateUtil.setDate_2(endDateC);
						dDateUtil.dateDiff();
						if (dDateUtil.getDays() > 0) {
							this.info("C段延遲息及違約金(多段) startDateC = " + startDateC);
							this.info("C段延遲息及違約金(多段) endDateC = " + endDateC);
							this.info("C段延遲息及違約金(多段) 天數 = " + dDateUtil.getDays());
							wkBreachAmtC = wkBreachAmtC
									.add(wkBreachBase.multiply(lastRateC).multiply(new BigDecimal(dDateUtil.getDays()))
											.divide(new BigDecimal(36500), 9, RoundingMode.DOWN)
											.multiply(new BigDecimal("1.2")).setScale(0, RoundingMode.HALF_UP)); // AS400
																													// 運算過程小數位數最多九位
							this.info("C段延遲息及違約金(多段) 算式 = " + wkBreachBase + " * " + lastRateC + " * "
									+ dDateUtil.getDays() + " / 36500 * 1.2 ");
							this.info("C段延遲息及違約金(多段) 此段 = "
									+ wkBreachBase.multiply(lastRateC).multiply(new BigDecimal(dDateUtil.getDays()))
											.divide(new BigDecimal(36500), 9, RoundingMode.DOWN)
											.multiply(new BigDecimal("1.2")).setScale(0, RoundingMode.HALF_UP));
						}
						startDateC = tempEffectDate;
						endDateC = tempEntryDate;
						lastRateC = tempRate;
					}
					// 若利率變動日在延遲息區間
					if (tempEffectDate >= startDateD && tempEffectDate <= endDateD) {

						endDateD = tempEffectDate;
						dDateUtil.init();
						dDateUtil.setDate_1(startDateD);
						dDateUtil.setDate_2(endDateD);
						dDateUtil.dateDiff();
						if (dDateUtil.getDays() > 0) {
							this.info("延遲息(多段) startDateD = " + startDateD);
							this.info("延遲息(多段) endDateD = " + endDateD);
							this.info("延遲息(多段) 天數 = " + dDateUtil.getDays());
							wkDelayInt = wkDelayInt
									.add(wkDelayBase.multiply(lastRateD).multiply(new BigDecimal(dDateUtil.getDays()))
											.divide(new BigDecimal(36500), 9, RoundingMode.DOWN)
											.setScale(0, RoundingMode.HALF_UP));
							this.info("延遲息(多段) 算式 =  " + wkDelayBase + " * " + lastRateD + " * " + dDateUtil.getDays()
									+ " / 36500");
							this.info("延遲息(多段) 此段 = "
									+ wkDelayBase.multiply(lastRateD).multiply(new BigDecimal(dDateUtil.getDays()))
											.divide(new BigDecimal(36500), 9, RoundingMode.DOWN)
											.setScale(0, RoundingMode.HALF_UP));
						}
						startDateD = tempEffectDate;
						endDateD = tempEntryDate;
						lastRateD = tempRate;
					}
				}
				// 若違約金A仍有剩餘天數
				dDateUtil.init();
				dDateUtil.setDate_1(startDateA);
				dDateUtil.setDate_2(endDateA);
				dDateUtil.dateDiff();
				if (dDateUtil.getDays() > 0) {
					this.info("A段延遲息及違約金(多段) startDateA = " + startDateA);
					this.info("A段延遲息及違約金(多段) endDateA = " + endDateA);
					this.info("A段延遲息及違約金(多段) 天數 = " + dDateUtil.getDays());
					wkBreachAmtA = wkBreachAmtA
							.add(wkBreachBase.multiply(lastRateA).multiply(new BigDecimal(dDateUtil.getDays()))
									.divide(new BigDecimal(36500), 9, RoundingMode.DOWN).multiply(new BigDecimal("1.1"))
									.setScale(0, RoundingMode.HALF_UP)); // AS400
																			// 運算過程小數位數最多九位
					this.info("A段延遲息及違約金(多段) = " + wkBreachBase + " * " + lastRateA + " * " + dDateUtil.getDays()
							+ " / 36500 * 1.1 ");
					this.info("A段延遲息及違約金(多段) 算式 = "
							+ wkBreachBase.multiply(lastRateA).multiply(new BigDecimal(dDateUtil.getDays()))
									.divide(new BigDecimal(36500), 9, RoundingMode.DOWN).multiply(new BigDecimal("1.1"))
									.setScale(0, RoundingMode.HALF_UP));
				}
				// 若違約金B仍有剩餘天數
				dDateUtil.init();
				dDateUtil.setDate_1(startDateB);
				dDateUtil.setDate_2(endDateB);
				dDateUtil.dateDiff();
				if (dDateUtil.getDays() > 0) {
					this.info("B段延遲息及違約金(多段) startDateB = " + startDateB);
					this.info("B段延遲息及違約金(多段) endDateB = " + endDateB);
					this.info("B段延遲息及違約金(多段) 天數 = " + dDateUtil.getDays());
					wkBreachAmtB = wkBreachAmtB
							.add(wkBreachBase.multiply(lastRateB).multiply(new BigDecimal(dDateUtil.getDays()))
									.divide(new BigDecimal(36500), 9, RoundingMode.DOWN).multiply(new BigDecimal("1.2"))
									.setScale(0, RoundingMode.HALF_UP)); // AS400
																			// 運算過程小數位數最多九位
					this.info("B段延遲息及違約金(多段) 算式 = " + wkBreachBase + " * " + lastRateB + " * " + dDateUtil.getDays()
							+ " / 36500 * 1.2 ");
					this.info("B段延遲息及違約金(多段) 此段 = "
							+ wkBreachBase.multiply(lastRateB).multiply(new BigDecimal(dDateUtil.getDays()))
									.divide(new BigDecimal(36500), 9, RoundingMode.DOWN).multiply(new BigDecimal("1.2"))
									.setScale(0, RoundingMode.HALF_UP));
				}
				// 若違約金C仍有剩餘天數
				dDateUtil.init();
				dDateUtil.setDate_1(startDateC);
				dDateUtil.setDate_2(endDateC);
				dDateUtil.dateDiff();
				if (dDateUtil.getDays() > 0) {
					this.info("C段延遲息及違約金(多段) startDateC = " + startDateC);
					this.info("C段延遲息及違約金(多段) endDateC = " + endDateC);
					this.info("C段延遲息及違約金(多段) 天數 = " + dDateUtil.getDays());
					wkBreachAmtC = wkBreachAmtC
							.add(wkBreachBase.multiply(lastRateC).multiply(new BigDecimal(dDateUtil.getDays()))
									.divide(new BigDecimal(36500), 9, RoundingMode.DOWN).multiply(new BigDecimal("1.2"))
									.setScale(0, RoundingMode.HALF_UP)); // AS400
																			// 運算過程小數位數最多九位
					this.info("C段延遲息及違約金(多段) 算式 = " + wkBreachBase + " * " + lastRateC + " * " + dDateUtil.getDays()
							+ " / 36500 * 1.2 ");
					this.info("C段延遲息及違約金(多段) 此段 = "
							+ wkBreachBase.multiply(lastRateC).multiply(new BigDecimal(dDateUtil.getDays()))
									.divide(new BigDecimal(36500), 9, RoundingMode.DOWN).multiply(new BigDecimal("1.2"))
									.setScale(0, RoundingMode.HALF_UP));
				}
				// 若延遲息仍有剩餘天數
				dDateUtil.init();
				dDateUtil.setDate_1(startDateD);
				dDateUtil.setDate_2(endDateD);
				dDateUtil.dateDiff();
				if (dDateUtil.getDays() > 0) {
					this.info("延遲息(多段) startDateD = " + startDateD);
					this.info("延遲息(多段) endDateD = " + endDateD);
					this.info("延遲息(多段) 天數 = " + dDateUtil.getDays());
					wkDelayInt = wkDelayInt.add(wkDelayBase.multiply(lastRateD)
							.multiply(new BigDecimal(dDateUtil.getDays()))
							.divide(new BigDecimal(36500), 9, RoundingMode.DOWN).setScale(0, RoundingMode.HALF_UP));
					this.info("延遲息(多段) 算式 =  " + wkDelayBase + " * " + lastRateD + " * " + dDateUtil.getDays()
							+ " / 36500");
					this.info("延遲息(多段) 此段 = " + wkDelayBase.multiply(lastRateD)
							.multiply(new BigDecimal(dDateUtil.getDays()))
							.divide(new BigDecimal(36500), 9, RoundingMode.DOWN).setScale(0, RoundingMode.HALF_UP));
				}
			}
		} else {
			this.info("延遲息與違約金計算基礎或計算天數不同");
			wkBreachAmtA = wkBreachBase.multiply(vCalcRepayIntVo.getStoreRate()).multiply(new BigDecimal(wkDaysA))
					.divide(new BigDecimal(36500), 9, RoundingMode.DOWN).multiply(new BigDecimal("0.1"))
					.setScale(9, RoundingMode.DOWN); // AS400 運算過程小數位數最多九位
			wkBreachAmtB = wkBreachBase.multiply(vCalcRepayIntVo.getStoreRate()).multiply(new BigDecimal(wkDaysB))
					.divide(new BigDecimal(36500), 9, RoundingMode.DOWN).multiply(new BigDecimal("0.2"))
					.setScale(9, RoundingMode.DOWN); // AS400 運算過程小數位數最多九位
			wkBreachAmtC = wkBreachBase.multiply(vCalcRepayIntVo.getStoreRate()).multiply(new BigDecimal(wkDaysC))
					.divide(new BigDecimal(36500), 9, RoundingMode.DOWN).multiply(new BigDecimal("0.2"))
					.setScale(9, RoundingMode.DOWN); // AS400 運算過程小數位數最多九位
			if (wkDaysA > 0) {
				this.info("A段違約金 = " + wkBreachAmtA + " = " + wkBreachBase + " * " + vCalcRepayIntVo.getStoreRate()
						+ " * " + wkDaysA + " / 36500 * 0.1 ");
			}
			if (wkDaysB > 0) {
				this.info("B段違約金 = " + wkBreachAmtB + " = " + wkBreachBase + " * " + vCalcRepayIntVo.getStoreRate()
						+ " * " + wkDaysB + " / 36500 * 0.2 ");
			}
			if (wkDaysC > 0) {
				this.info("C段違約金 = " + wkBreachAmtC + " = " + wkBreachBase + " * " + vCalcRepayIntVo.getStoreRate()
						+ " * " + wkDaysC + " / 36500 * 0.2 ");
			}
		}

		BigDecimal wkBreachAmt = (wkBreachAmtA.add(wkBreachAmtB).add(wkBreachAmtC));
		this.info("違約金 (四捨五入前) = " + wkBreachAmt);
		wkBreachAmt = wkBreachAmt.setScale(0, RoundingMode.HALF_UP);
		this.info("違約金 (四捨五入後) = " + wkBreachAmt);

		this.info("延遲息 (四捨五入前) = " + wkDelayInt);
		wkDelayInt = wkDelayInt.setScale(0, RoundingMode.HALF_UP);
		this.info("延遲息 (四捨五入後) = " + wkDelayInt);

		if (wkDelayBase.compareTo(wkBreachBase) == 0 && wkDays == wkDaysA + wkDaysB + wkDaysC) {
			// 2022-03-14 智偉增加
			// 因為原系統延遲息及違約金為一個欄位運算及顯示
			// 然而，新系統需求是要拆分為兩個欄位顯示
			// 若分開運算後各自四捨五入，合計後會比原本合計多一元
			// 這裡修改為先算出總額(四捨五入) 減去 延遲息(四捨五入)，得違約金
			wkBreachAmt = wkBreachAmt.subtract(wkDelayInt);
			this.info("違約金 ( 延遲息及違約金合併計算總額 - 延遲息 ) = " + wkBreachAmt);
		}

		vCalcRepayIntVo.setBreachAmt(wkBreachAmt);
		vCalcRepayIntVo.setDelayInt(wkDelayInt);
		lCalcRepayIntVo.set(wkIndex, vCalcRepayIntVo);

		this.info("fillBreachInterestRoutine end ");
		wkDuraInt = BigDecimal.ZERO;
	}

	// 部分償還本金處理方式
	// a.一般案件要先收息、不可欠繳利息(交易控制)
	// b.聯貸案件部分償還時不計算利息。
	// c.以還本金的金額計算利息，以日計息。
	// d 部分償還本金內含利息時扣除全部利息
	// e.是否重算期金，由交易處理。

	private boolean extraRepayRoutine(int wkIndex, TitaVo titaVo) throws LogicException {
		this.info("extraRepayRoutine ... ");
		this.info("   wkIndex         = " + wkIndex);
		this.info("   wkCalcVoCount   = " + wkCalcVoCount);
		this.info("   iExtraRepayFlag = " + iExtraRepayFlag);
		this.info("   iCaseCloseFlag  = " + iCaseCloseFlag);
		this.info("   wkExtraRepay    = " + wkExtraRepay);
		this.info("   iSyndFlag       = " + iSyndFlag);
		this.info("   DuraFlag()      = " + vCalcRepayIntVo.getDuraFlag());
		this.info("   oPaidTerms      = " + oPaidTerms);
		this.info("   oPaidTerms      = " + oPaidTerms);
		oPaidTerms--;

		BigDecimal wkIntAmt = BigDecimal.ZERO;

		vCalcRepayIntVo = lCalcRepayIntVo.get(wkIndex);
		// 計算wkTotalExtraRepay(部分償還累計金額 )及 wkExtraRepay(部分償還餘額)
		BigDecimal wkRepayAmt = vCalcRepayIntVo.getPrincipal().add(vCalcRepayIntVo.getInterest())
				.add(vCalcRepayIntVo.getDelayInt()).add(vCalcRepayIntVo.getBreachAmt());
		if (iExtraRepay.compareTo(BigDecimal.ZERO) > 0) {
			// 收回期款(計息止日<=期款止日
			if (vCalcRepayIntVo.getEndDate() <= wkTermEndDate) {
				wkExtraRepay = wkExtraRepay.subtract(wkRepayAmt);
				wkTotalExtraRepay = wkTotalExtraRepay.add(wkRepayAmt);
				if (wkExtraRepay.compareTo(BigDecimal.ZERO) < 0) {
					throw new LogicException(titaVo, "E3928", "超過金額 = " + df.format(wkExtraRepay.add(BigDecimal.ONE))); // 計算利息錯誤，部分償還本金超過應償還本金利息
				}
			}
			// 部分還本金額利息計篹
			else {
				// 聯貸案件不計算利息，聯貸案件 Y:是 N:否
				if (iSyndFlag.equals("Y")) {
					vCalcRepayIntVo.setAmount(BigDecimal.ZERO);
					vCalcRepayIntVo.setInterest(BigDecimal.ZERO);
				} else {
					// 部分償還本金計算利息，計息止日大於期款止日
					vCalcRepayIntVo.setAmount(wkExtraRepay);
					wkIntAmt = calcInterestRoutine();
					vCalcRepayIntVo.setInterest(wkIntAmt);
				}
				lCalcRepayIntVo.set(wkIndex, vCalcRepayIntVo);
				// 部分償還內含利息(結案不內涵
				if (iExtraRepayFlag.equals("Y")) {
					wkTotalExtraRepay = wkTotalExtraRepay.add(wkIntAmt);
				}
				this.info("   wkExtraRepay = " + wkExtraRepay);
				this.info("   wkIntAmt     = " + wkIntAmt);
				this.info("   Amount       = " + vCalcRepayIntVo.getAmount());
				this.info("   Principal    = " + vCalcRepayIntVo.getPrincipal());
				this.info("   Interest     = " + vCalcRepayIntVo.getInterest());
			}
		}

		// 最後一筆處理
		if (wkIndex == wkCalcVoCount) {
			// 分段計息記號 0:按日計息 1:按週/月計息 2:利率分段計息
			switch (vCalcRepayIntVo.getDuraFlag()) {
			// 按日計息時(零星日)，結案時將計息金額(餘額)放入還款金額，回收時放入部分償還餘額
			case 0:
				if (iCaseCloseFlag.equals("Y") || iExtraRepay.compareTo(BigDecimal.ZERO) >= 0) {
					if (iCaseCloseFlag.equals("Y")) {
						vCalcRepayIntVo.setPrincipal(vCalcRepayIntVo.getAmount());
						oExtraAmt = vCalcRepayIntVo.getAmount();
					} else {
						oPaidTerms--;
						vCalcRepayIntVo.setExtraRepayFlag(1); // 部分償還金額記號 0:否 1:是
						wkExtraRepay = iExtraRepay.subtract(wkTotalExtraRepay);
						vCalcRepayIntVo.setPrincipal(wkExtraRepay);
						oExtraAmt = wkExtraRepay;
						wkTotalExtraRepay = wkTotalExtraRepay.add(wkExtraRepay);
					}
					wkExtraRepay = BigDecimal.ZERO;
					this.info("   getAmount   = " + vCalcRepayIntVo.getAmount());
					this.info("   getInterest = " + vCalcRepayIntVo.getInterest());
					this.info(" b getAmount   = " + lCalcRepayIntVo.get(wkIndex).getAmount());
					this.info(" b getInterest = " + lCalcRepayIntVo.get(wkIndex).getInterest());
					lCalcRepayIntVo.set(wkIndex, vCalcRepayIntVo);
				}
				break;
			// 按週/月計息(整期)時，新增一筆計息明細
			case 1:
				if (iCaseCloseFlag.equals("Y")
						&& vCalcRepayIntVo.getAmount().compareTo(vCalcRepayIntVo.getPrincipal()) == 0) {
					this.info("extraRepayRoutine end A");
					return false;
				}
				if (iCaseCloseFlag.equals("Y") || wkExtraRepay.compareTo(BigDecimal.ZERO) > 0) {
					wkCalcVoCount++;
					CalcRepayIntVo w = new CalcRepayIntVo();
					w.setCustNo(iCustNo);
					w.setFacmNo(iFacmNo);
					w.setBormNo(iBormNo);
					w.setTermNo(vCalcRepayIntVo.getTermNo());
					w.setType(vCalcRepayIntVo.getType()); // 種類
					w.setAmount(vCalcRepayIntVo.getAmount().subtract(vCalcRepayIntVo.getPrincipal())); // 計算金額
					w.setStartDate(vCalcRepayIntVo.getEndDate()); // 計算起日
					w.setEndDate(vCalcRepayIntVo.getEndDate()); // 計算止日
					w.setDays(0); // 計算日數
					w.setStoreRate(vCalcRepayIntVo.getStoreRate()); // 計算利率
					w.setRateIncr(vCalcRepayIntVo.getRateIncr()); // 加碼利率
					w.setIndividualIncr(vCalcRepayIntVo.getIndividualIncr()); // 個別加碼利率
					if (iCaseCloseFlag.equals("Y")) {
						w.setPrincipal(w.getAmount());
						oExtraAmt = w.getAmount();
					} else {
						w.setPrincipal(wkExtraRepay); // 應還本金
						oExtraAmt = wkExtraRepay;
					}
					w.setInterest(BigDecimal.ZERO); // 利息
					w.setBreachAmt(BigDecimal.ZERO); // 違約金
					w.setOdDays(0); // 違約日數
					w.setDelayInt(BigDecimal.ZERO); // 遲延息,延滯息
					w.setBreachGetCode("0"); // 清償違約金收取方式 1:即時收取 2:領清償證明時收取
					w.setMonthLimit(vCalcRepayIntVo.getMonthLimit()); // 當月日數
					w.setInterestFlag(vCalcRepayIntVo.getInterestFlag()); // 1:按日計息 2:按月計息
					w.setRateCode(vCalcRepayIntVo.getRateCode()); // 利率區分 1: 機動 2: 固定 3: 定期機動
					w.setExtraRepayFlag(1); // 部分償還金額記號 0:否 1:是
					wkExtraRepay = BigDecimal.ZERO;
					lCalcRepayIntVo.add(w);
					oCalcCount = wkCalcVoCount + 1;
					this.info("extraRepayRoutine end B");
					return true;
				}
				break;
			}
		}
		this.info("   wkExtraRepay      = " + wkExtraRepay);
		this.info("   wkTotalExtraRepay = " + wkTotalExtraRepay);
		this.info("extraRepayRoutine end ");
		return false;
	}

	private void totalAmtRoutine(int wkIndex, TitaVo titaVo) throws LogicException {
		this.info("totalAmtRoutine ... ");
		this.info("   wkIndex    = " + wkIndex);
		this.info("   iSyndFlag  = " + iSyndFlag);
		this.info("   iNonePrincipalFlag = " + iNonePrincipalFlag);
		this.info("   oPrincipal = " + oPrincipal);

		vCalcRepayIntVo = lCalcRepayIntVo.get(wkIndex);
		// vCalcRepayIntVo.setLoanBal(vCalcRepayIntVo.getAmount().subtract(vCalcRepayIntVo.getPrincipal()));
		wkLoanBal = wkLoanBal.subtract(vCalcRepayIntVo.getPrincipal());
		// 部分償還本金超過應償還本金利息
		if (wkLoanBal.compareTo(BigDecimal.ZERO) < 0) {
			throw new LogicException(titaVo, "E3928",
					"超過金額 = " + df.format(wkTotalExtraRepay.subtract(wkLoanBal).add(BigDecimal.ONE))); // 計算利息錯誤，部分償還本金超過應償還本金利息
		}

		vCalcRepayIntVo.setLoanBal(wkLoanBal);
		switch (iSyndFlag) {
		case "Y":
			oPrevPaidIntDate = vCalcRepayIntVo.getEndDate(); // 上次收息日
			break;
		case "N":
			if (vCalcRepayIntVo.getDuraFlag() == 1
					|| (vCalcRepayIntVo.getDuraFlag() != 1 && vCalcRepayIntVo.getExtraRepayFlag() == 0)) {
				oPrevPaidIntDate = vCalcRepayIntVo.getEndDate(); // 上次收息日
			}
			break;
		}
		oRateIncr = vCalcRepayIntVo.getRateIncr(); // 加碼利率
		oIndividualIncr = vCalcRepayIntVo.getIndividualIncr(); // 個別加碼利率
		if (iCaseCloseFlag.equals("Y") && wkIndex == wkCalcVoCount
				&& vCalcRepayIntVo.getPrincipal().compareTo(BigDecimal.ZERO) > 0) {
			oRepaidPeriod += 1;
			oPrevRepaidDate = vCalcRepayIntVo.getEndDate();
		} else {
			if (vCalcRepayIntVo.getPrincipal().compareTo(BigDecimal.ZERO) > 0 && vCalcRepayIntVo.getDuraFlag() == 1) {
				oRepaidPeriod += 1;
				oPrevRepaidDate = vCalcRepayIntVo.getEndDate();
			}
		}
		oStoreRate = vCalcRepayIntVo.getStoreRate();
		if (iNonePrincipalFlag == 0) {
			oPrincipal = oPrincipal.add(vCalcRepayIntVo.getPrincipal());
			oLoanBal = oLoanBal.subtract(vCalcRepayIntVo.getPrincipal());
		}
		oInterest = oInterest.add(vCalcRepayIntVo.getInterest());
		oDelayInt = oDelayInt.add(vCalcRepayIntVo.getDelayInt());
		oBreachAmt = oBreachAmt.add(vCalcRepayIntVo.getBreachAmt());

		this.info("      oPrevPaidIntDate= " + oPrevPaidIntDate);
		this.info("      oPrincipal      = " + oPrincipal);
		this.info("      oInterest       = " + oInterest);
		this.info("      oDelayInt       = " + oDelayInt);
		this.info("      oBreachAmt      = " + oBreachAmt);
		this.info("totalAmtRoutine end ");
	}

	// 計算下次收息日 167800 CALINT-OOIDATE-RTN.
	private void nextPayIntDateRoutine() throws LogicException {
		this.info("nextPayIntDateRoutine ... ");
		this.info("iAmortizedCode  = " + iAmortizedCode);
		this.info("iPayIntFreq     = " + iPayIntFreq);
		this.info("iSpecificDate   = " + iSpecificDate + "/" + wkSpecificDate + "/" + wkSpecificMons);
		this.info("oPrevPaidIntDate= " + oPrevPaidIntDate);

		int wkDate = 0;
		int wkMons = 0;
		int wkDays = 0;
		int wkTerms = 0;

		if (iAmortizedCode == 2) { // 2.到期取息(到期繳息還本)
			oNextPayIntDate = iMaturityDate;
			return;
		}
		if (iPayIntFreq == 99) {
			oNextPayIntDate = 9991231;
		}
		// 計算基準日到上次繳息日的日/月差
		dDateUtil.init();
		dDateUtil.setDate_1(iSpecificDate);
		if (oPrevPaidIntDate < iSpecificDate) {
			dDateUtil.setDate_2(iSpecificDate);
		} else {
			dDateUtil.setDate_2(oPrevPaidIntDate);
		}
		switch (iFreqBase) { // 週期基準 1:日 2:月 3:週
		case 2:
			dDateUtil.dateDiff();
			wkMons = dDateUtil.getMons();
			wkTerms = wkMons / iPayIntFreq;
			wkTerms += 1;
			wkMons = wkTerms * iPayIntFreq;
			dDateUtil.init();
			dDateUtil.setDate_1(wkSpecificDate);
			dDateUtil.setMons(wkMons + wkSpecificMons);
			wkDate = dDateUtil.getCalenderDay();
			break;
		case 3:
			dDateUtil.dateDiff();
			wkDays = dDateUtil.getDays();
			wkDays = wkDays + (iPayIntFreq * 7);
			dDateUtil.init();
			dDateUtil.setDate_1(iSpecificDate);
			dDateUtil.setDays(wkDays);
			wkDate = dDateUtil.getCalenderDay();
			break;
		}
		// 預定收息日是否已超過到期日放到期日；按月繳息者最後一期按應繳日計算
		if (oPrevPaidIntDate < iMaturityDate && wkDate > iMaturityDate) {
			if (iFreqBase == 2) {
				oNextPayIntDate = wkDate;
			} else {
				oNextPayIntDate = iMaturityDate;
			}
		} else {
			oNextPayIntDate = wkDate;
		}
		this.info("   oNextPayIntDate = " + oNextPayIntDate);
		this.info("nextPayIntDateRoutine end ");
	}

	// 調整違約金生效日遇假日
	private void adjustBreachValidDateRoutine() throws LogicException {
// 入帳日(違約金生效日)在下次應繳日(遇假日順延)前，則違約金生效日為下次應繳日
//       |     五    |    六     |    日    |    一    |
//                    下次繳息日
//                                             入帳日
//                    違約金生效日
// 下次繳息日為星期六(假日)，則入帳日為星期一 ，違約金生效日均為星期六
//         
// 違約寬限天數(營業日)參數設為零時，才有調整意義
		this.info("adjustBreachValidDateRoutine ..." + "iNextPayIntDate=" + iNextPayIntDate + " ,iBreachValidDate="
				+ iBreachValidDate);

		// 入帳日(違約金生效日)在 下次應繳日，則不變
		if (iBreachValidDate <= iNextPayIntDate) {
			return;
		}

		// 下次應繳日為營業日，則不變
		dDateUtil.init();
		dDateUtil.setDate_2(iNextPayIntDate);
		if (!dDateUtil.isHoliDay()) {
			return;
		}

		int wkDate = iNextPayIntDate;
		do {
			dDateUtil.init();
			dDateUtil.setDate_1(wkDate);
			dDateUtil.setDays(1);
			wkDate = dDateUtil.getCalenderDay();
			dDateUtil.init();
			dDateUtil.setDate_2(wkDate);
		} while (dDateUtil.isHoliDay());

		// 入帳日(違約金生效日)在下次應繳日(遇假日順延)前，則違約金生效日為下次應繳日
		if (iBreachValidDate <= wkDate) {
			iBreachValidDate = iNextPayIntDate;
		}
		this.info("adjustBreachValidDateRoutine end, iBreachValidDate= " + iBreachValidDate);
	}

	private void logInputRoutine() {
		this.info(" Caculate log Input ... 戶號= " + iCustNo + "-" + iFacmNo + "-" + iBormNo + " , 結案記號 = "
				+ iCaseCloseFlag + " , 部分償還本金 = " + iExtraRepay + " , 部分償還本金是否內含利息 = " + iExtraRepayFlag
				+ " , 本次繳息期數 = " + iTerms + " , 計算起日 = " + iIntStartDate + " , 計算止日 = " + iIntEndDate + " , 幣別 = "
				+ iCurrencyCode + " , 商品代碼 = " + iProdNo + " , 指標利率代碼 = " + iBaseRateCode + " , 利率區分 = " + iRateCode
				+ " , 利率調整週期 = " + iRateAdjFreq + " , 下次利率調整日 = " + iNextAdjRateDate + " , 計息本金(放款餘額) = " + iPrincipal
				+ " , 加減碼是否依合約 = " + iIncrFlag + " , 上次收息利率 = " + iStoreRate + " , 加碼利率 = " + iRateIncr + " , 個別加碼利率 = "
				+ iIndividualIncr + " , 週期基準 = " + iFreqBase + " , 繳息週期 = " + iPayIntFreq + " , 還本週期 = " + iRepayFreq
				+ " , 已繳息期數 = " + iPaidTerms + " , 貸放起日 = " + iDrawdownDate + " , 貸放止日 = " + iMaturityDate
				+ " , 違約金生效日 = " + iBreachValidDate + " , 上次繳息日 = " + iPrevPaidIntDate + " , 上次還本日 = " + iPrevRepaidDate
				+ " , 下次繳息日 = " + iNextPayIntDate + " , 下次還本日 = " + iNextRepayDate + " , 指定基準日期 = " + iSpecificDate
				+ " , 指定應繳日 = " + iSpecificDd + " , 首次應繳日 = " + iFirstDueDate + " , 寬限到期日 = " + iGraceDate
				+ " , 違約寬限天數 = " + iBreachGraceDays + " , 攤還額異動碼 = " + iExtraRepayCode + " , 每期攤還金額 = " + iDueAmt
				+ " , 違約金之利率 = " + iBreachRate + " , 遲延息之利率 = " + iDelayRate + " , 未繳清記號 = " + iUnpaidFlag
				+ " , 計息方式 = " + iIntCalcCode + " , 攤還方式 = " + iAmortizedCode + " , 收遲延息 = " + iDelayFlag
				+ " , 契約到期還本記號 = " + iNonePrincipalFlag + " , 資金用途別 = " + iUsageCode + " , 聯貸案件   = " + iSyndFlag
				+ " , 減免違約金 = " + iBreachReliefFlag + " , 營業日期 = " + iTbsDy + " , 入帳日期 = " + iEntryDate
				+ " , 最後一期本金餘額 = " + iFinalBal + " , 寬限期 = " + iGracePeriod + " , 總期數 = " + iTotalPeriod
				+ " , 計算止日代碼 = " + iIntEndCode + " , 初貸日 = " + iFirstDrawdownDate);

	}

	private void logOutputRoutine() {
		this.info("Caculate log Output ... 戶號= " + iCustNo + "-" + iFacmNo + "-" + iBormNo + ", 適用利率 = " + oStoreRate
				+ " , 加碼利率 = " + oRateIncr + " , 個別加碼利率 = " + oIndividualIncr + " , 上次還本日 = " + oPrevRepaidDate
				+ " , 下次還本日 = " + oNextRepayDate + " , 期金 = " + oDueAmt + " , 應還本金總額 = " + oPrincipal + " , 利息總金額 = "
				+ oInterest + " , 延遲息總額 = " + oDelayInt + " , 違約金總額 = " + oBreachAmt + " , 部分償還金額 = " + oExtraAmt
				+ " , 本次還本期數 = " + oRepaidPeriod + " , 已繳息期數 = " + oPaidTerms + " , 上次收息日 = " + oPrevPaidIntDate
				+ " ,  下次收息日 = " + oNextPayIntDate + " , 計息筆數 = " + oCalcCount + " , 計息後餘額 = " + oLoanBal);

		for (int i = 0; i <= wkCalcVoCount; i++) {
			CalcRepayIntVo tCalcRepayIntVo = lCalcRepayIntVo.get(i);
			this.info("Caculate log 期數編號 = " + tCalcRepayIntVo.getTermNo() + ", 種類 = " + tCalcRepayIntVo.getType()
					+ ", 計算金額 = " + tCalcRepayIntVo.getAmount() + ", 計算起日 = " + tCalcRepayIntVo.getStartDate()
					+ ", 計算止日 = " + tCalcRepayIntVo.getEndDate() + ", 計算日數 = " + tCalcRepayIntVo.getDays() + ", 計算利率 = "
					+ tCalcRepayIntVo.getStoreRate() + ", 應還本金 = " + tCalcRepayIntVo.getPrincipal() + ", 利息 = "
					+ tCalcRepayIntVo.getInterest() + ", 違約金 = " + tCalcRepayIntVo.getBreachAmt() + ", 違約日數 = "
					+ tCalcRepayIntVo.getOdDays() + ", 延遲息 = " + tCalcRepayIntVo.getDelayInt() + ", 期金 = "
					+ tCalcRepayIntVo.getDueAmt() + ", 放款餘額 = " + tCalcRepayIntVo.getLoanBal() + ", 分段計息記號 = "
					+ tCalcRepayIntVo.getDuraFlag() + ", 還本記號 = " + tCalcRepayIntVo.getPrincipalFlag() + ", 當月天數 = "
					+ tCalcRepayIntVo.getMonthLimit() + ", 計息記號 = " + tCalcRepayIntVo.getInterestFlag()
					+ ", 部分償還金額記號 = " + tCalcRepayIntVo.getExtraRepayFlag() + ", 下段適用利率 = "
					+ tCalcRepayIntVo.getNextStoreRate());
		}
	}

	public void setCustNo(int CustNo) {
		this.iCustNo = CustNo;
	}

	public void setFacmNo(int FacmNo) {
		this.iFacmNo = FacmNo;
	}

	public void setBormNo(int BormNo) {
		this.iBormNo = BormNo;
	}

	public void setProdNo(String ProdNo) {
		this.iProdNo = ProdNo;
	}

	public void setAcctCode(String AcctCode) {
		this.iAcctCode = AcctCode;
	}

	public void setCurrencyCode(String CurrencyCode) {
		this.iCurrencyCode = CurrencyCode;
	}

	public void setBaseRateCode(String BaseRateCode) {
		this.iBaseRateCode = BaseRateCode;
	}

	public void setRateCode(String RateCode) {
		this.iRateCode = RateCode;
	}

	public void setRateAdjFreq(int RateAdjFreq) {
		this.iRateAdjFreq = RateAdjFreq;
	}

	public void setNextAdjRateDate(int NextAdjRateDate) {
		this.iNextAdjRateDate = NextAdjRateDate;
	}

	public void setPrincipal(BigDecimal Principal) {
		this.iPrincipal = Principal;
	}

	public void setIncrFlag(String IncrFlag) {
		this.iIncrFlag = IncrFlag;
	}

	public void setStoreRate(BigDecimal StoreRate) {
		this.iStoreRate = StoreRate;
	}

	public void setRateIncr(BigDecimal RateIncr) {
		this.iRateIncr = RateIncr;
	}

	public void setIndividualIncr(BigDecimal IndividualIncr) {
		this.iIndividualIncr = IndividualIncr;
	}

	public void setFreqBase(int FreqBase) {
		this.iFreqBase = FreqBase;
	}

	public void setPayIntFreq(int PayIntFreq) {
		this.iPayIntFreq = PayIntFreq;
	}

	public void setRepayFreq(int RepayFreq) {
		this.iRepayFreq = RepayFreq;
	}

	public void setTerms(int Terms) {
		this.iTerms = Terms;
	}

	public void setPaidTerms(int PaidTerms) {
		this.iPaidTerms = PaidTerms;
	}

	public void setIntStartDate(int IntStartDate) {
		this.iIntStartDate = IntStartDate;
	}

	public void setIntEndDate(int IntEndDate) {
		this.iIntEndDate = IntEndDate;
	}

	public void setIntEndCode(int IntEndCode) {
		this.iIntEndCode = IntEndCode;
	}

	public void setFirstDrawdownDate(int FirstDrawdownDate) {
		this.iFirstDrawdownDate = FirstDrawdownDate;
	}

	public void setDrawdownDate(int DrawdownDate) {
		this.iDrawdownDate = DrawdownDate;
	}

	public void setMaturityDate(int MaturityDate) {
		this.iMaturityDate = MaturityDate;
	}

	public void setBreachValidDate(int BreachValidDate) {
		this.iBreachValidDate = BreachValidDate;
	}

	public void setPrevPaidIntDate(int PrevPaidIntDate) {
		this.iPrevPaidIntDate = PrevPaidIntDate;
	}

	public void setPrevRepaidDate(int PrevRepaidDate) {
		this.iPrevRepaidDate = PrevRepaidDate;
	}

	public void setNextPayIntDate(int NextPayIntDate) {
		this.iNextPayIntDate = NextPayIntDate;
	}

	public void setNextRepayDate(int NextRepayDate) {
		this.iNextRepayDate = NextRepayDate;
	}

	public void setSpecificDate(int SpecificDate) {
		this.iSpecificDate = SpecificDate;
	}

	public void setSpecificDd(int SpecificDd) {
		this.iSpecificDd = SpecificDd;
	}

	public void setFirstDueDate(int FirstDueDate) {
		this.iFirstDueDate = FirstDueDate;
	}

	public void setGraceDate(int GraceDate) {
		this.iGraceDate = GraceDate;
	}

	public void setBreachGraceDays(int BreachGraceDays) {
		this.iBreachGraceDays = BreachGraceDays;
	}

	public void setExtraRepayCode(String ExtraRepayCode) {
		this.iExtraRepayCode = ExtraRepayCode;
	}

	public void setDueAmt(BigDecimal DueAmt) {
		this.iDueAmt = DueAmt;
	}

	public void setBreachRate(BigDecimal BreachRate) {
		this.iBreachRate = BreachRate;
	}

	public void setDelayRate(BigDecimal DelayRate) {
		this.iDelayRate = DelayRate;
	}

	public void setUnpaidFlag(int UnpaidFlag) {
		this.iUnpaidFlag = UnpaidFlag;
	}

	public void setIntCalcCode(String IntCalcCode) {
		this.iIntCalcCode = IntCalcCode;
	}

	public void setAmortizedCode(int AmortizedCode) {
		this.iAmortizedCode = AmortizedCode;
	}

	public void setDelayFlag(int DelayFlag) {
		this.iDelayFlag = DelayFlag;
	}

	public void setNonePrincipalFlag(int NonePrincipalFlag) {
		this.iNonePrincipalFlag = NonePrincipalFlag;
	}

	public void setExtraRepay(BigDecimal ExtraRepay) {
		this.iExtraRepay = ExtraRepay;
	}

	public void setExtraRepayFlag(String ExtraRepayFlag) {
		this.iExtraRepayFlag = ExtraRepayFlag;
	}

	public void setTbsDy(int TbsDy) {
		this.iTbsDy = TbsDy;
	}

	public void setEntryDate(int EntryDate) {
		this.iEntryDate = EntryDate;
	}

	public void setUsageCode(String UsageCode) {
		this.iUsageCode = UsageCode;
	}

	public void setCaseCloseFlag(String CaseCloseFlag) {
		this.iCaseCloseFlag = CaseCloseFlag;
	}

	public void setBreachReliefFlag(String BreachReliefFlag) {
		this.iBreachReliefFlag = BreachReliefFlag;
	}

	public void setSyndFlag(String SyndFlag) {
		this.iSyndFlag = SyndFlag;
	}

	public void setFinalBal(BigDecimal FinalBal) {
		this.iFinalBal = FinalBal;
	}

	public void setGracePeriod(int GracePeriod) {
		this.iGracePeriod = GracePeriod;
	}

	public void setTotalPeriod(int TotalPeriod) {
		this.iTotalPeriod = TotalPeriod;
	}
	// ----------------------------------------------------------------------------

	public int getNextAdjRateDate() {
		return oNextAdjRateDate;
	}

	public int getAdjRateFreq() {
		return oRateAdjFreq;
	}

	public BigDecimal getStoreRate() {
		return oStoreRate;
	}

	public BigDecimal getRateIncr() {
		return oRateIncr;
	}

	public BigDecimal getIndividualIncr() {
		return oIndividualIncr;
	}

	public int getPrevRepaidDate() {
		return oPrevRepaidDate;
	}

	public int getNextRepayDate() {
		return oNextRepayDate;
	}

	public BigDecimal getLoanBal() {
		return oLoanBal;
	}

	public BigDecimal getPrincipal() {
		return oPrincipal;
	}

	public BigDecimal getBreachAmt() {
		return oBreachAmt;
	}

	public BigDecimal getInterest() {
		return oInterest;
	}

	public BigDecimal getDelayInt() {
		return oDelayInt;
	}

	public BigDecimal getExtraAmt() {
		return oExtraAmt;
	}

	public int getRepaidPeriod() {
		return oRepaidPeriod;
	}

	public int getPrevPaidIntDate() {
		return oPrevPaidIntDate;
	}

	public int getNextPayIntDate() {
		return oNextPayIntDate;
	}

	public int getCalcCount() {
		return oCalcCount;
	}

	public int getRepayTerms() {
		return iTerms;
	}

	public int getPaidTerms() {
		return oPaidTerms;
	}

	public String getProdNo() {
		return iProdNo;
	}

	public String getAcctCode() {
		return iAcctCode;
	}

	public BigDecimal getDueAmt() {
		return oDueAmt;
	}

	@Override
	public void exec() throws LogicException {
		// TODO Auto-generated method stub

	}
}
