package com.st1.itx.util.common;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.dataVO.TotaVo;
import com.st1.itx.db.domain.FacCaseAppl;
import com.st1.itx.db.domain.FacMain;
import com.st1.itx.db.domain.FacMainId;
import com.st1.itx.db.domain.FacProd;
import com.st1.itx.db.domain.LoanBorMain;
import com.st1.itx.db.service.FacCaseApplService;
import com.st1.itx.db.service.FacMainService;
import com.st1.itx.db.service.FacProdService;
import com.st1.itx.db.service.LoanBorMainService;
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.date.DateUtil;
import com.st1.itx.util.parse.Parse;

/**
 * 設定放款共同計息參數
 * 
 * @author st1
 *
 */
@Component("loanSetRepayIntCom")
@Scope("prototype")
public class LoanSetRepayIntCom extends TradeBuffer {

	/* DB服務注入 */
	@Autowired
	FacProdService facProdService;
	@Autowired
	FacMainService facMainService;
	@Autowired
	FacCaseApplService facCaseApplService;
	@Autowired
	LoanBorMainService loanBorMainService;

	@Autowired
	Parse parse;
	@Autowired
	LoanCalcRepayIntCom loanCalcRepayIntCom;
	@Autowired
	LoanCom loanCom;

	@Autowired
	DateUtil dDateUtil;

	/**
	 * 設定放款計息參數
	 * 
	 * @param t           LoanBorMain
	 * @param iRepayTerms 回收期數
	 * @param iIntEndDate 計算止日
	 * @param iIntEndCode 計算止日代碼 0.無計算止日 1.至計算止日 2:利息提存
	 * @param iEntryDate  入帳日期
	 * @param titaVo      TitaVo
	 * @return loanCalcRepayIntCom
	 * @throws LogicException LogicException
	 */
	public LoanCalcRepayIntCom setRepayInt(LoanBorMain t, int iRepayTerms, int iIntEndDate, int iIntEndCode,
			int iEntryDate, TitaVo titaVo) throws LogicException {
		this.info("active setRepayInt ");
		this.info("   RepayTerms = " + iRepayTerms);
		this.info("   IntEndDate = " + iIntEndDate);
		this.info("   IntEndCode = " + iIntEndCode);
		this.info("   EntryDate  = " + iEntryDate);
		this.info("   PrevPayIntDate  = " + t.getPrevPayIntDate());
		int prevPayIntDate = t.getPrevPayIntDate() == 0 ? t.getDrawdownDate() : t.getPrevPayIntDate();
		this.info("   prevPayIntDate = " + prevPayIntDate);

		// 查詢額度檔
		FacMain tFacMain = new FacMain();
		tFacMain = facMainService.findById(new FacMainId(t.getCustNo(), t.getFacmNo()), titaVo);
		if (tFacMain == null) {
			throw new LogicException(titaVo, "E0001", "額度主檔 借款人戶號 = " + t.getCustNo() + "額度編號 = " + t.getFacmNo()); // 查詢資料不存在
		}
		// 查詢商品參數檔
		FacProd tFacProd = facProdService.findById(tFacMain.getProdNo(), titaVo);
		if (tFacProd == null) {
			throw new LogicException(titaVo, "E0001", "商品參數檔 商品代碼 = " + tFacMain.getProdNo()); // 查詢資料不存在
		}
		// 查詢案件申請檔
		FacCaseAppl tFacCaseAppl = facCaseApplService.findById(tFacMain.getApplNo(), titaVo);
		if (tFacCaseAppl == null) {
			throw new LogicException(titaVo, "E0001", "案件申請檔 申請號碼 = " + tFacMain.getApplNo()); // 查詢資料不存在
		}

		loanCalcRepayIntCom.init();
		loanCalcRepayIntCom.setCustNo(t.getCustNo());
		loanCalcRepayIntCom.setFacmNo(t.getFacmNo());
		loanCalcRepayIntCom.setBormNo(t.getBormNo());
		loanCalcRepayIntCom.setProdNo(tFacMain.getProdNo()); // 商品代碼
		loanCalcRepayIntCom.setAcctCode(tFacMain.getAcctCode()); // 核准科目
		loanCalcRepayIntCom.setCurrencyCode(t.getCurrencyCode()); // 幣別
		loanCalcRepayIntCom.setBaseRateCode(tFacMain.getBaseRateCode()); // 指標利率代碼
		loanCalcRepayIntCom.setRateCode(t.getRateCode()); // 利率區分 1:機動 2:固動 3:定期機動
		loanCalcRepayIntCom.setRateAdjFreq(t.getRateAdjFreq() == 0 ? 99 : t.getRateAdjFreq()); // 利率調整週期
		loanCalcRepayIntCom.setNextAdjRateDate(t.getNextAdjRateDate() == 0 ? 9991231 : t.getNextAdjRateDate()); // 下次利率調整日
		loanCalcRepayIntCom.setPrincipal(t.getLoanBal()); // 計息本金
		loanCalcRepayIntCom.setIncrFlag(tFacProd.getIncrFlag()); // 加減碼是否依合約 Y:是 N:否
		loanCalcRepayIntCom.setStoreRate(t.getStoreRate()); // 上次收息利率
		loanCalcRepayIntCom.setRateIncr(t.getRateIncr()); // 加碼利率
		loanCalcRepayIntCom.setIndividualIncr(t.getIndividualIncr()); // 個別加碼利率
		loanCalcRepayIntCom.setFreqBase(t.getFreqBase()); // 週期基準 1:日 2:月 3:週
		loanCalcRepayIntCom.setPayIntFreq(t.getPayIntFreq() == 0 ? 99 : t.getPayIntFreq()); // 繳息週期
		loanCalcRepayIntCom.setRepayFreq(t.getRepayFreq() == 0 ? 99 : t.getRepayFreq()); // 還本週期
		loanCalcRepayIntCom.setTerms(iRepayTerms); // 本次繳息期數
		loanCalcRepayIntCom.setPaidTerms(t.getPaidTerms()); // 已繳息期數
		loanCalcRepayIntCom.setIntStartDate(prevPayIntDate); // 計算起日
		loanCalcRepayIntCom.setIntEndCode(iIntEndCode); // 計算止日代碼 0.無計算止日 1.至計算止日 2:利息提存
		// 計算止日小於上次繳息日，則以上次繳息日為計算止日
		loanCalcRepayIntCom
				.setIntEndDate((iIntEndCode == 1 && iIntEndDate < prevPayIntDate) ? prevPayIntDate : iIntEndDate); // 計算止日
		loanCalcRepayIntCom.setFirstDrawdownDate(tFacMain.getFirstDrawdownDate()); // 初貸日
		loanCalcRepayIntCom.setDrawdownDate(t.getDrawdownDate()); // 貸放起日
		loanCalcRepayIntCom.setMaturityDate(t.getMaturityDate()); // 貸放止日
		loanCalcRepayIntCom.setBreachValidDate(iEntryDate); // 違約金生效日
		loanCalcRepayIntCom.setPrevRepaidDate(t.getPrevRepaidDate() == 0 ? t.getDrawdownDate() : t.getPrevRepaidDate()); // 上次還本日
		loanCalcRepayIntCom.setPrevPaidIntDate(prevPayIntDate); // 上次繳息日
		loanCalcRepayIntCom.setNextPayIntDate(t.getNextPayIntDate()); // 下次繳息日,應繳息日,預定收息日
		loanCalcRepayIntCom.setNextRepayDate(t.getNextRepayDate()); // 下次還本日,應還本日,預定還本日
		loanCalcRepayIntCom.setSpecificDate(t.getSpecificDate()); // 指定基準日期, 利息基準日
		loanCalcRepayIntCom.setSpecificDd(t.getSpecificDd()); // 指定應繳日
		loanCalcRepayIntCom.setFirstDueDate(t.getFirstDueDate()); // 首次應繳日
		loanCalcRepayIntCom.setGraceDate(t.getGraceDate()); // 寬限到期日
		loanCalcRepayIntCom.setBreachGraceDays(this.txBuffer.getSystemParas().getGraceDays()); // 違約寬限天數(營業日)
		loanCalcRepayIntCom.setExtraRepayCode(tFacMain.getExtraRepayCode()); // 攤還額異動碼 0: 不變 1: 變
		loanCalcRepayIntCom.setDueAmt(t.getDueAmt()); // 每期攤還金額
		loanCalcRepayIntCom.setBreachRate(t.getStoreRate()); // 違約金之利率
		loanCalcRepayIntCom.setDelayRate(t.getStoreRate()); // 遲延息之利率
		loanCalcRepayIntCom.setUnpaidFlag(0); // 未繳清記號
		loanCalcRepayIntCom.setIntCalcCode(t.getIntCalcCode());// 計息方式 1:按日計息 2:按月計息

		// 1.按月繳息(按期繳息到期還本)
		// 2.到期取息(到期繳息還本)
		// 3.本息平均法(期金)
		// 4.本金平均法
		loanCalcRepayIntCom.setAmortizedCode(this.parse.stringToInteger(t.getAmortizedCode())); // 攤還方式,還本方式

		// 利息提存
		// 1.短擔以日計算
		// 2.中長擔，已到期按月(下次繳息日 <= 下個月1日)；未到期，首次繳款按日
		// 3.已過到期日，按日、不分期(以到期取息(到期繳息還本)計算)
		// 4.未到期以最後一段利率計算(計息程式內處理)
		int nextMonth01 = (this.txBuffer.getTxCom().getNbsdy() / 100) * 100 + 1;
		int thisMonth01 = (this.txBuffer.getTxCom().getTbsdy() / 100) * 100 + 1;
		if (iIntEndCode == 2) { // 每月月底提息參數設定-開始
			String intCalcCode = t.getIntCalcCode();
			String amortizedCode = t.getAmortizedCode();
			// 1.短擔
			if (tFacMain.getAcctCode().equals("310")) {
				intCalcCode = "1";// 計息方式 1:按日計息
			} else {// 2. 中長擔
				// 2022-04-19 智偉增加
				// 若帳號計息方式是1:按日計息 且 繳息日期(2碼)為1
				if (intCalcCode.equals("1") && t.getSpecificDd() == 1) {
					// 計息方式改為2:按月計息
					intCalcCode = "2";
				}
				// 2022-04-19 智偉增加
				// 若帳號計息方式是2:按月計息 且 下繳日大於等於下月1日 且 上繳日小於本月1日
				if (intCalcCode.equals("2") && t.getNextPayIntDate() >= nextMonth01
						&& t.getPrevPayIntDate() < thisMonth01) {
					// 計息方式改為1:按日計息
					intCalcCode = "1";
				}
				// 2022-04-19 智偉補充說明:其他照帳號原本的計息方式
			}
			// 2022-05-04 智偉: 到期日早於本月月底日
			if (t.getMaturityDate() < nextMonth01 && t.getPrevPayIntDate() < t.getMaturityDate()) {

				// 2022-05-04 智偉: 上繳日與到期日超過一期
				dDateUtil.init();
				dDateUtil.setDate_1(t.getPrevPayIntDate());
				dDateUtil.setDate_2(t.getMaturityDate());
				dDateUtil.dateDiff();
				int unpayTerms = dDateUtil.getMons();

				if (unpayTerms >= 2) {
					// 計息方式調成1:以日計息
//					intCalcCode = "1";
//					amortizedCode = "2";
//					loanCalcRepayIntCom.setDueAmt(BigDecimal.ZERO); // 每期攤還金額

					// 2022-05-09 智偉增加判斷
					// 符合此條件者
					// 照AS400多算一天利息
//					int orignalMaturityDate = t.getMaturityDate();
//					dDateUtil.init();
//					dDateUtil.setDate_1(orignalMaturityDate);
//					dDateUtil.setDays(1);
//					int newMaturityDate = dDateUtil.getCalenderDay();
//					loanCalcRepayIntCom.setMaturityDate(newMaturityDate);
				}
			}
			loanCalcRepayIntCom.setIntCalcCode(intCalcCode);
			loanCalcRepayIntCom.setAmortizedCode(this.parse.stringToInteger(amortizedCode));
			this.info("Caculate log Set ... 戶號= " + t.getCustNo() + "-" + t.getFacmNo() + "-" + t.getBormNo()
					+ ", AcctCode=" + tFacMain.getAcctCode() + ", CalcCode =" + t.getIntCalcCode() + "/" + intCalcCode
					+ ", AmortizedCode=" + t.getAmortizedCode() + "/" + amortizedCode + ", SpecificDate ="
					+ t.getSpecificDate() + " ,prevPayIntDate =" + prevPayIntDate);
		} // 提息參數設定-結束
		loanCalcRepayIntCom.setDelayFlag(0); // 0:收遲延息 1: 不收
		loanCalcRepayIntCom.setNonePrincipalFlag(0); // 0:契約到期要還本 1:契約到期不還本記號
		loanCalcRepayIntCom.setTbsDy(this.txBuffer.getTxCom().getTbsdy()); // 營業日期
		loanCalcRepayIntCom.setEntryDate(iEntryDate); // 入帳日期
		loanCalcRepayIntCom.setUsageCode(t.getUsageCode()); // 資金用途別 1: 週轉金2: 購置不動產3: 營業用資產4: 固定資產5: 企業投資6: 購置動產9: 其他
		loanCalcRepayIntCom.setCaseCloseFlag("N"); // 結案記號 Y:是 N:否

		// 減免違約金 Y:是 N:否
		loanCalcRepayIntCom.setBreachReliefFlag("N");
		// 最後一期期款，入帳日在應繳日前減免違約金，即只收至到期日的利息、不收延滯息及違約金
		// if (t.getNextPayIntDate() > t.getMaturityDate() && iEntryDate <=
		// t.getNextPayIntDate()) {
		// loanCalcRepayIntCom.setBreachReliefFlag("Y");
		// }

		// 聯貸案件 Y:是 N:否
		if (tFacCaseAppl.getSyndNo() > 0) { // 聯貸案編號
			loanCalcRepayIntCom.setSyndFlag("Y");
		} else {
			loanCalcRepayIntCom.setSyndFlag("N");
		}
		loanCalcRepayIntCom.setFinalBal(t.getFinalBal()); // 最後一期本金餘額

		// 重新計算寬限期數
		loanCalcRepayIntCom.setGracePeriod(loanCom.getGracePeriod(t.getAmortizedCode(), t.getFreqBase(),
				t.getPayIntFreq(), t.getSpecificDate(), t.getSpecificDd(), t.getGraceDate()));
		loanCalcRepayIntCom.setTotalPeriod(t.getTotalPeriod()); // 總期數

		this.info("   setRepayInt iRepayTerms = " + loanCalcRepayIntCom.getRepayTerms());
		this.info("   setRepayInt end");
		return loanCalcRepayIntCom;
	}

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		// TODO Auto-generated method stub
		return null;
	}
}
