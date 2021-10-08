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
import com.st1.itx.util.common.LoanCloseBreachCom;
import com.st1.itx.util.common.LoanCom;
import com.st1.itx.util.common.LoanSetRepayIntCom;
import com.st1.itx.util.common.data.CalcRepayIntVo;
import com.st1.itx.util.common.data.LoanCloseBreachVo;
import com.st1.itx.util.date.DateUtil;
import com.st1.itx.util.parse.Parse;

/*
 * L3921 回收試算
 * a.此功能供回收期款前試算其應回收之本金，利息及違約金等。
 * b.撥款序號如未輸入，則該額度下各筆撥款均列入計算;額度編號如未輸入，則該戶號下各筆撥款均列入計算。
 */
/*
 * Tita
 * TimCustNo=9,7
 * CustId=X,10
 * ApplNo=9,7
 * FacmNo=9,3
 * BormNo=9,3
 * CurrencyCode=X,3
 * TimExtraRepay=9,14.2
 * IncludeIntFlag=X,1
 * RepayTerms=9,2
 * EntryDate=9,7
 */

/**
 * L3921 回收試算
 * 
 * @author iza
 * @version 1.0.0
 */
@Service("L3921")
@Scope("prototype")
public class L3921 extends TradeBuffer {

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
	LoanSetRepayIntCom loanSetRepayIntCom;
	@Autowired
	LoanCalcRepayIntCom loanCalcRepayIntCom;
	@Autowired
	LoanCloseBreachCom loanCloseBreachCom;
	@Autowired
	BaTxCom baTxCom;

	private int iCustNo;
	private int iFacmNo;
	private int iBormNo;
	private int iRepayTerms;
	private int iEntryDate;
	private BigDecimal iExtraRepay;
	private String iExtraRepayFlag;
	private List<LoanBorMain> lLoanBorMain = new ArrayList<LoanBorMain>();
	private ArrayList<CalcRepayIntVo> lCalcRepayIntVo = new ArrayList<CalcRepayIntVo>();
	private ArrayList<LoanCloseBreachVo> iListCloseBreach = new ArrayList<LoanCloseBreachVo>();
	private ArrayList<LoanCloseBreachVo> oListCloseBreach = new ArrayList<LoanCloseBreachVo>();

	private int wkFacmNoStart = 1;
	private int wkFacmNoEnd = 999;
	private int wkBormNoStart = 1;
	private int wkBormNoEnd = 900;
	private int wkTerms = 0;
	private int wkTotaCount = 0;
	private int wkPreRepayTermNo = 0;
	private int wkPreRepayDate = 0;
	private int wkIntEndDate = 0;
	private BigDecimal oLoanBal = BigDecimal.ZERO;
	private int oIntStartDate = 9991231;
	private int oIntEndDate = 0;
	private BigDecimal oRate = BigDecimal.ZERO;
	private String oCurrencyCode = "";
	private BigDecimal oPrincipal = BigDecimal.ZERO;
	private BigDecimal oInterest = BigDecimal.ZERO;
	private BigDecimal oDelayInt = BigDecimal.ZERO;
	private BigDecimal oBreachAmt = BigDecimal.ZERO;
	private BigDecimal oCloseBreachAmt = BigDecimal.ZERO;
	private BigDecimal wkExtraRepay = BigDecimal.ZERO;

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active L3921 ");
		this.totaVo.init(titaVo);
		loanSetRepayIntCom.setTxBuffer(this.txBuffer);
		loanCloseBreachCom.setTxBuffer(this.txBuffer);

		// 取得輸入資料
		iCustNo = this.parse.stringToInteger(titaVo.getParam("TimCustNo"));
		iFacmNo = this.parse.stringToInteger(titaVo.getParam("FacmNo"));
		iBormNo = this.parse.stringToInteger(titaVo.getParam("BormNo"));
		iRepayTerms = this.parse.stringToInteger(titaVo.getParam("RepayTerms"));
		iEntryDate = this.parse.stringToInteger(titaVo.getParam("EntryDate"));
		iExtraRepay = this.parse.stringToBigDecimal(titaVo.getParam("TimExtraRepay"));
		iExtraRepayFlag = titaVo.getParam("IncludeIntFlag");

		this.info("   iExtraRepay = " + iExtraRepay);
		this.info("   iRepayTerms = " + iRepayTerms);
		this.info("   iEntryDate  = " + iEntryDate);

		if (iFacmNo > 0) {
			wkFacmNoStart = iFacmNo;
			wkFacmNoEnd = iFacmNo;
		}

		if (iBormNo > 0) {
			wkBormNoStart = iBormNo;
			wkBormNoEnd = iBormNo;
		}
		// 各項費用
		baTxCom.settingUnPaid(iEntryDate, iCustNo, iFacmNo, iBormNo, 0, BigDecimal.ZERO, titaVo); // 00-費用全部(已到期)
		// 設定第幾分頁 titaVo.getReturnIndex() 第一次會是0，如果需折返最後會塞值
		this.index = titaVo.getReturnIndex();

		// 設定每筆分頁的資料筆數 預設500筆 總長不可超過六萬
		this.limit = 300; // 295 + 122 * 300 = 41695

		Slice<LoanBorMain> slLoanBorMain = loanBorMainService.bormCustNoEq(iCustNo, wkFacmNoStart, wkFacmNoEnd, wkBormNoStart, wkBormNoEnd, this.index, this.limit, titaVo);
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
				// 部分償還金額 > 0時排序,依利率順序由大到小 else 依應繳日順序由小到大
				if (iExtraRepay.compareTo(BigDecimal.ZERO) > 0) {
					if (c1.getStoreRate().compareTo(c2.getStoreRate()) != 0) {
						return (c1.getStoreRate().compareTo(c2.getStoreRate()) > 0 ? -1 : 1);
					}
				} else {
					if (c1.getNextPayIntDate() != c2.getNextPayIntDate()) {
						return c1.getNextPayIntDate() - c2.getNextPayIntDate();
					}
					if (c1.getStoreRate().compareTo(c2.getStoreRate()) != 0) {
						return (c1.getStoreRate().compareTo(c2.getStoreRate()) > 0 ? -1 : 1);
					}
				}
				if (c1.getFacmNo() != c2.getFacmNo()) {
					return c1.getFacmNo() - c2.getFacmNo();
				}
				if (c1.getBormNo() != c2.getBormNo()) {
					return c1.getBormNo() - c2.getBormNo();
				}
				return 0;
			}
		});
		wkExtraRepay = iExtraRepay;
		// 償還期款
		for (LoanBorMain ln : lLoanBorMain) {
			if (ln.getStatus() == 0 || ln.getStatus() == 4) {
				if ("5".equals(ln.getAmortizedCode())) { // 攤還方式 = 5.按月撥款收息(逆向貸款)
					continue;
				}
				if (ln.getPrevPayIntDate() > ln.getDrawdownDate()) {
					wkPreRepayTermNo = loanCom.getTermNo(2, ln.getFreqBase(), ln.getPayIntFreq(), ln.getSpecificDate(), ln.getSpecificDd(), ln.getPrevPayIntDate());
				}
				if (iRepayTerms > 0) { // 回收期數 > 0
					wkTerms = loanCom.getTermNo(2, ln.getFreqBase(), ln.getPayIntFreq(), ln.getSpecificDate(), ln.getSpecificDd(), ln.getMaturityDate()) + 1 - wkPreRepayTermNo;
					if (wkTerms > iRepayTerms) {
						wkTerms = iRepayTerms;
					}
				} else {
					if (ln.getPrevPayIntDate() >= iEntryDate || ln.getDrawdownDate() == iEntryDate) {
						continue;
					}
					// 計算至入帳日期應繳之期數 - 計算至上次繳息日之期數
					wkTerms = loanCom.getTermNo(2, ln.getFreqBase(), ln.getPayIntFreq(), ln.getSpecificDate(), ln.getSpecificDd(), iEntryDate);
					if (ln.getPrevPayIntDate() > ln.getDrawdownDate()) {
						wkTerms = wkTerms - wkPreRepayTermNo;
					}
				}

				if (wkTerms <= 0) {
					continue;
				}

				// 計算
				loanCalcRepayIntCom = loanSetRepayIntCom.setRepayInt(ln, wkTerms, 0, 0, iEntryDate, titaVo);
				lCalcRepayIntVo = loanCalcRepayIntCom.getRepayInt(titaVo);
				oLoanBal = oLoanBal.add(ln.getLoanBal());
				oRate = ln.getStoreRate();
				oCurrencyCode = ln.getCurrencyCode();
				oPrincipal = oPrincipal.add(loanCalcRepayIntCom.getPrincipal());
				oInterest = oInterest.add(loanCalcRepayIntCom.getInterest());
				oDelayInt = oDelayInt.add(loanCalcRepayIntCom.getDelayInt());
				oBreachAmt = oBreachAmt.add(loanCalcRepayIntCom.getBreachAmt());
				wkExtraRepay = wkExtraRepay.subtract(oPrincipal).subtract(oInterest).subtract(oDelayInt).subtract(oBreachAmt);
				addOccurs(ln, titaVo);
				if (iExtraRepay.compareTo(BigDecimal.ZERO) > 0) {
					ln.setStoreRate(loanCalcRepayIntCom.getStoreRate());
					ln.setLoanBal(ln.getLoanBal().subtract(loanCalcRepayIntCom.getPrincipal()));
					ln.setRepaidPeriod(ln.getRepaidPeriod() + loanCalcRepayIntCom.getRepaidPeriod());
					ln.setPaidTerms(loanCalcRepayIntCom.getPaidTerms());
					ln.setPrevPayIntDate(loanCalcRepayIntCom.getPrevPaidIntDate());
					ln.setPrevRepaidDate(loanCalcRepayIntCom.getPrevRepaidDate());
					ln.setNextPayIntDate(loanCalcRepayIntCom.getNextPayIntDate());
					ln.setNextRepayDate(loanCalcRepayIntCom.getNextRepayDate());
				}
			}
		}

		// 部分償還本金
		BigDecimal wkExtraRepayRmd = wkExtraRepay;
		if (wkExtraRepay.compareTo(BigDecimal.ZERO) > 0) {
			for (LoanBorMain ln : lLoanBorMain) {
				if (ln.getStatus() == 0 || ln.getStatus() == 4) {
					if (wkExtraRepay.compareTo(BigDecimal.ZERO) <= 0) {
						break;
					} else {
						loanCalcRepayIntCom = loanSetRepayIntCom.setRepayInt(ln, 0, iEntryDate, 1, iEntryDate, titaVo);
						this.info("wkExtraRepay= " + wkExtraRepay + ", LoanBal=" + ln.getLoanBal());
						if (wkExtraRepay.compareTo(ln.getLoanBal()) >= 0) {
							loanCalcRepayIntCom.setCaseCloseFlag("Y"); // 結案試算
						} else {
							loanCalcRepayIntCom.setExtraRepayFlag(iExtraRepayFlag);
							loanCalcRepayIntCom.setExtraRepay(wkExtraRepay); // 部分償還本金試算
						}
						// 計算
						lCalcRepayIntVo = loanCalcRepayIntCom.getRepayInt(titaVo);
						oLoanBal = oLoanBal.add(ln.getLoanBal());
						oRate = ln.getStoreRate();
						oCurrencyCode = ln.getCurrencyCode();
						oPrincipal = oPrincipal.add(loanCalcRepayIntCom.getPrincipal());
						oInterest = oInterest.add(loanCalcRepayIntCom.getInterest());
						oDelayInt = oDelayInt.add(loanCalcRepayIntCom.getDelayInt());
						oBreachAmt = oBreachAmt.add(loanCalcRepayIntCom.getBreachAmt());
						wkExtraRepay = wkExtraRepay.subtract(oPrincipal).subtract(oInterest).subtract(oDelayInt).subtract(oBreachAmt);
						addOccurs(ln, titaVo);
						LoanCloseBreachVo v = new LoanCloseBreachVo();
						// 放入清償違約金計算List
						v.setCustNo(ln.getCustNo());
						v.setFacmNo(ln.getFacmNo());
						v.setBormNo(ln.getBormNo());
						v.setExtraRepay(loanCalcRepayIntCom.getExtraAmt());
						v.setEndDate(iEntryDate);
						iListCloseBreach.add(v);
					}
				}
			}
		}
		// 期款
		if (wkTotaCount == 0) {
			throw new LogicException(titaVo, "E3070", ""); // 查無可計息的放款資料

		}

		// 計算清償違約金，收取方式 "1":即時收取
		if (iExtraRepay.compareTo(BigDecimal.ZERO) > 0) {
			oListCloseBreach = loanCloseBreachCom.getCloseBreachAmtPaid(iCustNo, iFacmNo, iBormNo, iListCloseBreach, titaVo);
			// 輸出清償違約金
			if (oListCloseBreach != null && oListCloseBreach.size() > 0) {
				for (LoanCloseBreachVo v : oListCloseBreach) {
					if (v.getCloseBreachAmtPaid().compareTo(BigDecimal.ZERO) > 0) {
						oCloseBreachAmt = oCloseBreachAmt.add(v.getCloseBreachAmtPaid());
					}
				}
			}
		}
		this.totaVo.putParam("OLoanBal", oLoanBal);
		this.totaVo.putParam("OIntStartDate", oIntStartDate == 9991231 ? 0 : oIntStartDate);
		this.totaVo.putParam("OIntEndDate", oIntEndDate);
		this.totaVo.putParam("ORate", oRate);
		this.totaVo.putParam("OCurrencyCode", oCurrencyCode);
		this.totaVo.putParam("OPrincipal", oPrincipal);
		this.totaVo.putParam("OInterest", oInterest);
		this.totaVo.putParam("ODelayInt", oDelayInt);
		this.totaVo.putParam("OBreachAmt", oBreachAmt);
		this.totaVo.putParam("OCloseBreachAmt", oCloseBreachAmt);
		this.totaVo.putParam("OShortfall", baTxCom.getShortfall());
		this.totaVo.putParam("OShortfallInt", baTxCom.getShortfallInterest());
		this.totaVo.putParam("OShortfallPrin", baTxCom.getShortfallPrincipal());
		this.totaVo.putParam("OShortCloseBreach", baTxCom.getShortCloseBreach());
		this.totaVo.putParam("OExcessive", baTxCom.getExcessive());
		this.totaVo.putParam("OTempTax", baTxCom.getTempTax());
		this.totaVo.putParam("OModifyFee", baTxCom.getModifyFee());
		this.totaVo.putParam("OAcctFee", baTxCom.getAcctFee());
		this.totaVo.putParam("OFireFee", baTxCom.getFireFee());
		this.totaVo.putParam("OOvduFireFee", baTxCom.getCollFireFee());
		this.totaVo.putParam("OLawFee", baTxCom.getLawFee());
		this.totaVo.putParam("OOvduLawFee", baTxCom.getCollLawFee());

		this.addList(this.totaVo);

		if (iExtraRepay.compareTo(BigDecimal.ZERO) > 0 && wkExtraRepayRmd.compareTo(BigDecimal.ZERO) < 0) {
			this.totaVo.init(titaVo);
			this.totaVo.setWarnMsg("不足應繳期款" + wkExtraRepayRmd);
			this.addList(this.totaVo);
		}
		return this.sendList();
	}

	private void addOccurs(LoanBorMain ln, TitaVo titaVo) throws LogicException {

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