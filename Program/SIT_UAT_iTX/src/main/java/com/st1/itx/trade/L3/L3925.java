package com.st1.itx.trade.L3;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;

import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.OccursList;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.dataVO.TotaVo;
import com.st1.itx.db.domain.LoanBorMain;
import com.st1.itx.db.service.LoanBorMainService;
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.common.BaTxCom;
import com.st1.itx.util.common.LoanCalcRepayIntCom;
import com.st1.itx.util.common.LoanCom;
import com.st1.itx.util.common.LoanDueAmtCom;
import com.st1.itx.util.common.LoanSetRepayIntCom;
import com.st1.itx.util.common.data.CalcRepayIntVo;
import com.st1.itx.util.date.DateUtil;
import com.st1.itx.util.parse.Parse;
/*
 * L3925 還款分配試算
 * a.如指定單一撥款時,輸入一定還款金額,由電腦推算償還至入帳日之應繳金額,若有餘額,擇列入部分償還金額,並假設總期數不變,重算每期攤還金額.
 * b.如未輸入撥款序號時,擇由電腦根據入帳日及應繳日順序,逐筆撥款攤還,若有餘額,則列入溢短收.
 */
/*
 * Tita
 * TimCustNo=9,7
 * CustId=X,10
 * ApplNo=9,7
 * FacmNo=9,3
 * BormNo=9,3
 * CurrencyCode=X,3
 * TimRepayAmt=9,14.2
 * BreachFlag=9,1 減免違約金 0.不減免 1.減免
 * EntryDate=9,7
 */

/**
 * L3925 還款分配試算
 * 
 * @author iza
 * @version 1.0.0
 */
@Service("L3925")
@Scope("prototype")
public class L3925 extends TradeBuffer {

	/* DB服務注入 */
	@Autowired
	public LoanBorMainService loanBorMainService;

	@Autowired
	Parse parse;
	@Autowired
	DateUtil dDateUtil;
	@Autowired
	LoanCom loanCom;
	@Autowired
	LoanDueAmtCom loanDueAmtCom;
	@Autowired
	LoanSetRepayIntCom loanSetRepayIntCom;
	@Autowired
	LoanCalcRepayIntCom loanCalcRepayIntCom;
	@Autowired
	BaTxCom baTxCom;

	private int iCustNo = 0;
	private int iFacmNo = 0;
	private int iBormNo = 0;
	private int iEntryDate = 0;
	private int iBreachFlag = 0;
	private BigDecimal iRepayAmt = BigDecimal.ZERO;
	private BigDecimal wkTotalAmt = BigDecimal.ZERO;
	private BigDecimal wkLoanBal = BigDecimal.ZERO;
	private BigDecimal oLoanBal = BigDecimal.ZERO;
	private BigDecimal oRate = BigDecimal.ZERO;
	private BigDecimal oPrincipal = BigDecimal.ZERO;
	private BigDecimal oInterest = BigDecimal.ZERO;
	private BigDecimal oDelayInt = BigDecimal.ZERO;
	private BigDecimal oBreachAmt = BigDecimal.ZERO;
	private BigDecimal oCloseBreachAmt = BigDecimal.ZERO;
	private BigDecimal oTempTax = BigDecimal.ZERO;
	private BigDecimal oExtraRepay = BigDecimal.ZERO;
	private BigDecimal oNewDueAmt = BigDecimal.ZERO;
	private int oIntStartDate = 9991231;
	private int oIntEndDate = 0;
	private int oLeftTerms = 0;
	private int wkTotaCount = 0;

	private List<LoanBorMain> lLoanBorMain = new ArrayList<LoanBorMain>();
	private ArrayList<CalcRepayIntVo> lCalcRepayIntVo = new ArrayList<CalcRepayIntVo>();

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active L3925 ");
		this.totaVo.init(titaVo);
		loanSetRepayIntCom.setTxBuffer(this.txBuffer);

		// 取得輸入資料
		iCustNo = this.parse.stringToInteger(titaVo.getParam("TimCustNo"));
		iFacmNo = this.parse.stringToInteger(titaVo.getParam("FacmNo"));
		iBormNo = this.parse.stringToInteger(titaVo.getParam("BormNo"));
		iEntryDate = this.parse.stringToInteger(titaVo.getParam("EntryDate"));
		iBreachFlag = this.parse.stringToInteger(titaVo.getParam("BreachFlag"));
		iRepayAmt = this.parse.stringToBigDecimal(titaVo.getParam("TimRepayAmt"));
		this.info("iEntryDate=" + iEntryDate);

		// work area
		int wkFacmNoStart = 1;
		int wkFacmNoEnd = 999;
		int wkBormNoStart = 1;
		int wkBormNoEnd = 900;
		if (iFacmNo > 0) {
			wkFacmNoStart = iFacmNo;
			wkFacmNoEnd = iFacmNo;
		}

		if (iBormNo > 0) {
			wkBormNoStart = iBormNo;
			wkBormNoEnd = iBormNo;
		}

		// 設定第幾分頁 titaVo.getReturnIndex() 第一次會是0，如果需折返最後會塞值
		this.index = titaVo.getReturnIndex();

		// 設定每筆分頁的資料筆數 預設500筆 總長不可超過六萬
		this.limit = 400; // 183 + 138 * 400 = 55383

		Slice<LoanBorMain> slLoanBorMain = loanBorMainService.bormCustNoEq(iCustNo, wkFacmNoStart, wkFacmNoEnd, wkBormNoStart, wkBormNoEnd, this.index, this.limit, titaVo);
		lLoanBorMain = slLoanBorMain == null ? null : new ArrayList<LoanBorMain>(slLoanBorMain.getContent());
		if (lLoanBorMain == null || lLoanBorMain.size() == 0) {
			throw new LogicException(titaVo, "E0001", "放款主檔"); // 查詢資料不存在
		}
		// 排序,依應繳日順序由小到大
		Collections.sort(lLoanBorMain, new Comparator<LoanBorMain>() {
			public int compare(LoanBorMain c1, LoanBorMain c2) {
				return c1.getNextPayIntDate() - c2.getNextPayIntDate();
			}
		});

		// 查詢各項費用
		baTxCom.settingUnPaid(iEntryDate, iCustNo, iFacmNo, 0, 0, BigDecimal.ZERO, titaVo); // 00-費用全部(已到期)

		// 應繳期款試算
		RepayTermRoutine(titaVo);

		// 還款餘額
		if (iRepayAmt.compareTo(wkTotalAmt) < 0) {
			throw new LogicException(titaVo, "E3065", " 總金額 = " + wkTotalAmt); // 還款分配金額不足
		}
		oExtraRepay = iRepayAmt.subtract(wkTotalAmt);
		this.info("iRepayAmt=" + iRepayAmt + ", wkTotalAmt=" + wkTotalAmt + ", ExtraRepay=" + oExtraRepay);

		// 部分還款試算
		if (iBormNo > 0) {
			extraRepayRoutine(oExtraRepay, titaVo);
		}

		if (wkTotaCount == 0) {
			throw new LogicException(titaVo, "E3070", ""); // 查無可計息的放款資料
		}
		this.totaVo.putParam("OPrincipal", oPrincipal);
		this.totaVo.putParam("OIntStartDate", oIntStartDate == 9991231 ? 0 : oIntStartDate);
		this.totaVo.putParam("OIntEndDate", oIntEndDate);
		this.totaVo.putParam("ORate", oRate);
		this.totaVo.putParam("OInterest", oInterest);
		this.totaVo.putParam("ODelayInt", oDelayInt);
		this.totaVo.putParam("OBreachAmt", oBreachAmt);
		this.totaVo.putParam("OCloseBreachAmt", oCloseBreachAmt);
		this.totaVo.putParam("OTempTax", oTempTax);
		this.totaVo.putParam("OShortfall", baTxCom.getShortfall());
		this.totaVo.putParam("OShortfallInt", baTxCom.getShortfallInterest());
		this.totaVo.putParam("OShortfallPrin", baTxCom.getShortfallPrincipal());
		this.totaVo.putParam("OShortCloseBreach", baTxCom.getShortCloseBreach());
		this.totaVo.putParam("OExcessive", baTxCom.getExcessive());
		this.totaVo.putParam("OExtraRepay", oExtraRepay);
		this.totaVo.putParam("OLoanBal", oLoanBal.subtract(oPrincipal));
		this.totaVo.putParam("ONewDueAmt", oNewDueAmt);
		this.totaVo.putParam("OLeftTerms", oLeftTerms);

		this.addList(this.totaVo);
		return this.sendList();
	}

	private void extraRepayRoutine(BigDecimal wkExtraRepay, TitaVo titaVo) throws LogicException {
		this.info("extraRepayRoutine ... wkExtraRepay=" + wkExtraRepay);
		this.totaVo.init(titaVo);
		wkTotalAmt = BigDecimal.ZERO;
		wkLoanBal = BigDecimal.ZERO;
		oLoanBal = BigDecimal.ZERO;
		oExtraRepay = BigDecimal.ZERO;
		oRate = BigDecimal.ZERO;
		oPrincipal = BigDecimal.ZERO;
		oInterest = BigDecimal.ZERO;
		oDelayInt = BigDecimal.ZERO;
		oBreachAmt = BigDecimal.ZERO;
		oCloseBreachAmt = BigDecimal.ZERO;
		oTempTax = BigDecimal.ZERO;
		oNewDueAmt = BigDecimal.ZERO;
		oIntStartDate = 9991231;
		oIntEndDate = 0;
		oLeftTerms = 0;
		lCalcRepayIntVo = new ArrayList<CalcRepayIntVo>();

		int wkTerms = 0;

		// int wkTerms = 0;
		for (LoanBorMain ln : lLoanBorMain) {
			if (ln.getStatus() != 0) {
				continue;
			}
			if (ln.getPrevPayIntDate() >= iEntryDate || ln.getDrawdownDate() == iEntryDate) {
				throw new LogicException(titaVo, "E3064", " 上次繳息日 = " + ln.getPrevPayIntDate()); // 該筆放款應繳日尚未到
			}
			loanCalcRepayIntCom = loanSetRepayIntCom.setRepayInt(ln, 0, iEntryDate, 1, iEntryDate, titaVo);
			loanCalcRepayIntCom.setExtraRepayFlag("Y");
			loanCalcRepayIntCom.setExtraRepay(wkExtraRepay);
			loanCalcRepayIntCom.setBreachReliefFlag(iBreachFlag == 1 ? "Y" : "N");
			// execCalculate
			lCalcRepayIntVo = loanCalcRepayIntCom.getRepayInt(titaVo);
			// outCalculate
			oLoanBal = oLoanBal.add(ln.getLoanBal());
			if (loanCalcRepayIntCom.getStoreRate().compareTo(oRate) > 0) {
				oRate = loanCalcRepayIntCom.getStoreRate();
			}
			oPrincipal = oPrincipal.add(loanCalcRepayIntCom.getPrincipal());
			oInterest = oInterest.add(loanCalcRepayIntCom.getInterest());
			oDelayInt = oDelayInt.add(loanCalcRepayIntCom.getDelayInt());
			oBreachAmt = oBreachAmt.add(loanCalcRepayIntCom.getBreachAmt());
			// oCloseBreachAmt =
			// oCloseBreachAmt.add(loanCalcRepayIntCom.getCloseBreachAmtPaid());
			oExtraRepay = oExtraRepay.add(loanCalcRepayIntCom.getExtraAmt());
			wkTotalAmt = oPrincipal.add(oInterest).add(oDelayInt).add(oBreachAmt).add(oCloseBreachAmt);
			oLeftTerms = oLeftTerms + ln.getTotalPeriod() - loanCalcRepayIntCom.getPaidTerms();
			int wkGracePeriod = loanCom.getGracePeriod(ln.getAmortizedCode(), ln.getFreqBase(), ln.getPayIntFreq(), ln.getSpecificDate(),
					ln.getSpecificDd(), ln.getGraceDate());
			oNewDueAmt = loanDueAmtCom.getDueAmt(ln.getLoanBal().subtract(oPrincipal), loanCalcRepayIntCom.getStoreRate(), ln.getAmortizedCode(), ln.getFreqBase(),
					(loanCalcRepayIntCom.getPaidTerms() > wkGracePeriod ? ln.getTotalPeriod() - loanCalcRepayIntCom.getPaidTerms() : ln.getTotalPeriod() - wkGracePeriod),
					ln.getGracePeriod(), ln.getPayIntFreq(), ln.getFinalBal(), titaVo);
			wkLoanBal = ln.getLoanBal();
			this.info(" xx BormNo = " + ln.getBormNo());
			this.info(" oPrincipal = " + oPrincipal + "*" + loanCalcRepayIntCom.getPrincipal());
			this.info(" oInterest = " + oInterest + "*" + loanCalcRepayIntCom.getInterest());
			this.info(" oDelayInt = " + oDelayInt + "*" + loanCalcRepayIntCom.getDelayInt());
			this.info(" oBreachAmt = " + oBreachAmt + "*" + loanCalcRepayIntCom.getBreachAmt());
			this.info(" wkTotalAmt = " + wkTotalAmt);
			for (CalcRepayIntVo c : lCalcRepayIntVo) {
				OccursList occursList = new OccursList();
				wkLoanBal = wkLoanBal.subtract(c.getPrincipal());
				oIntStartDate = c.getStartDate() < oIntStartDate ? c.getStartDate() : oIntStartDate;
				oIntEndDate = c.getEndDate() > oIntEndDate ? c.getEndDate() : oIntEndDate;
				occursList.putParam("OOFacmNo", ln.getFacmNo());
				occursList.putParam("OOBormNo", ln.getBormNo());
				occursList.putParam("OOIntStartDate", c.getStartDate());
				occursList.putParam("OOIntEndDate", c.getEndDate());
				occursList.putParam("OOAmount", c.getAmount());
				occursList.putParam("OORate", c.getStoreRate());
				occursList.putParam("OOPrincipal", c.getPrincipal());
				occursList.putParam("OOInterest", c.getInterest());
				occursList.putParam("OODelayInt", c.getDelayInt());
				occursList.putParam("OOBreachAmt", c.getBreachAmt());
				occursList.putParam("OOCloseBreachAmt", c.getCloseBreachAmt());
				occursList.putParam("OOLoanBal", c.getLoanBal());
				// 將每筆資料放入Tota的OcList
				this.totaVo.addOccursList(occursList);
				wkTotaCount++;
			}

		}

	}

	private void RepayTermRoutine(TitaVo titaVo) throws LogicException {
		this.info("RepayTermRoutine ... iEntryDate=" + iEntryDate);
		int wkTerms = 0;

		for (LoanBorMain ln : lLoanBorMain) {
			if (ln.getStatus() != 0) {
				continue;
			}
			if (ln.getPrevPayIntDate() >= iEntryDate || ln.getDrawdownDate() == iEntryDate) {
				this.info(" skip Borm = " + ln.getFacmNo() + "-" + ln.getBormNo() + ", PrevPayIntDate=" + ln.getPrevPayIntDate());
				continue;
			}
			// 計算至入帳日期應繳之期數 - 計算至上次繳息日之期數
			wkTerms = loanCom.getTermNo(2, ln.getFreqBase(), ln.getPayIntFreq(), ln.getSpecificDate(), ln.getSpecificDd(), iEntryDate);
			if (ln.getPrevPayIntDate() > ln.getDrawdownDate()) {
				wkTerms = wkTerms - loanCom.getTermNo(2, ln.getFreqBase(), ln.getPayIntFreq(), ln.getSpecificDate(), ln.getSpecificDd(), ln.getPrevPayIntDate());
			}
			if (wkTerms == 0) {
				this.info(" wkTerms = 0, Borm = " + ln.getFacmNo() + "-" + ln.getBormNo() + ", NextPayIntDate=" + ln.getNextPayIntDate());
			} else {
				// setCalculate
				loanCalcRepayIntCom = loanSetRepayIntCom.setRepayInt(ln, wkTerms, 0, 0, iEntryDate, titaVo);
				loanCalcRepayIntCom.setBreachReliefFlag(iBreachFlag == 1 ? "Y" : "N");
				// execCalculate
				lCalcRepayIntVo = loanCalcRepayIntCom.getRepayInt(titaVo);
				// outCalculate
				oPrincipal = oPrincipal.add(loanCalcRepayIntCom.getPrincipal());
				oInterest = oInterest.add(loanCalcRepayIntCom.getInterest());
				oDelayInt = oDelayInt.add(loanCalcRepayIntCom.getDelayInt());
				oBreachAmt = oBreachAmt.add(loanCalcRepayIntCom.getBreachAmt());
				wkTotalAmt = oPrincipal.add(oInterest).add(oDelayInt).add(oBreachAmt);
				oLoanBal = oLoanBal.add(ln.getLoanBal());
				if (iBormNo == 0) {
					for (CalcRepayIntVo c : lCalcRepayIntVo) {
						OccursList occursList = new OccursList();
						oIntStartDate = c.getStartDate() < oIntStartDate ? c.getStartDate() : oIntStartDate;
						oIntEndDate = c.getEndDate() > oIntEndDate ? c.getEndDate() : oIntEndDate;
						occursList.putParam("OOFacmNo", ln.getFacmNo());
						occursList.putParam("OOBormNo", ln.getBormNo());
						occursList.putParam("OOIntStartDate", c.getStartDate());
						occursList.putParam("OOIntEndDate", c.getEndDate());
						occursList.putParam("OOAmount", c.getAmount());
						occursList.putParam("OORate", c.getStoreRate());
						occursList.putParam("OOPrincipal", c.getPrincipal());
						occursList.putParam("OOInterest", c.getInterest());
						occursList.putParam("OODelayInt", c.getDelayInt());
						occursList.putParam("OOBreachAmt", c.getBreachAmt());
						occursList.putParam("OOCloseBreachAmt", c.getCloseBreachAmt());
						occursList.putParam("OOLoanBal", c.getLoanBal());
						// 將每筆資料放入Tota的OcList
						this.totaVo.addOccursList(occursList);
						wkTotaCount++;
					}
				}
			}
		}
	}
}