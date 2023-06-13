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
import com.st1.itx.dataVO.OccursList;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.dataVO.TotaVo;
import com.st1.itx.db.domain.LoanBorMain;
import com.st1.itx.db.domain.LoanBorMainId;
import com.st1.itx.db.service.LoanBorMainService;
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.common.BaTxCom;
import com.st1.itx.util.common.LoanCalcRepayIntCom;
import com.st1.itx.util.common.LoanCom;
import com.st1.itx.util.common.LoanDueAmtCom;
import com.st1.itx.util.common.LoanSetRepayIntCom;
import com.st1.itx.util.common.data.CalcRepayIntVo;
import com.st1.itx.util.common.data.LoanCloseBreachVo;
import com.st1.itx.util.date.DateUtil;
import com.st1.itx.util.parse.Parse;

/*
 * Tita
 * TimCustNo=9,7
 * CustId=X,10
 * ApplNo=9,7
 * FacmNo=9,3
 * BormNo=9,3
 * CurrencyCode=X,3
 * NewDueAmt=9,14.2
 * EntryDate=9,7
 */

/**
 * L3926 變更期款試算
 * 
 * @author iza
 * @version 1.0.0
 */
@Service("L3926")
@Scope("prototype")
public class L3926 extends TradeBuffer {

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

	private int wkBormNoStart = 1;
	private int wkBormNoEnd = 900;

	// work area
	int oIntStartDate = 9991231;
	int oIntEndDate = 0;
	int oLeftTerms = 0;
	int wkoLeftTerms = 0;
	String oCurrencyCode = "";
	BigDecimal oLoanBal = BigDecimal.ZERO;
	BigDecimal oRate = BigDecimal.ZERO;
	BigDecimal oPrincipal = BigDecimal.ZERO;
	BigDecimal oInterest = BigDecimal.ZERO;
	BigDecimal oDelayInt = BigDecimal.ZERO;
	BigDecimal oBreachAmt = BigDecimal.ZERO;
	BigDecimal oExtraRepay = BigDecimal.ZERO;
	BigDecimal wkTotalAmt = BigDecimal.ZERO;
	BigDecimal wkTotalRepay = BigDecimal.ZERO;
	BigDecimal wkNewLoanBal = BigDecimal.ZERO;
	BigDecimal wkRate = BigDecimal.ZERO;
	BigDecimal wkStoreRate = BigDecimal.ZERO;
	BigDecimal wkRateA = BigDecimal.ZERO;
	BigDecimal wkRateB = BigDecimal.ZERO;
	BigDecimal wkRateC = BigDecimal.ZERO;
	BigDecimal wkFinalInterest = BigDecimal.ZERO;
	private BigDecimal wkExtraRepay = BigDecimal.ZERO;

	LoanBorMain tLoanBorMain = new LoanBorMain();
	ArrayList<CalcRepayIntVo> lCalcRepayIntVo = new ArrayList<CalcRepayIntVo>();
	private List<LoanBorMain> lLoanBorMain = new ArrayList<LoanBorMain>();
	private ArrayList<LoanCloseBreachVo> iListCloseBreach = new ArrayList<LoanCloseBreachVo>();

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active L3926 ");
		this.totaVo.init(titaVo);
		loanSetRepayIntCom.setTxBuffer(this.txBuffer);

		// 取得輸入資料
		int iCustNo = this.parse.stringToInteger(titaVo.getParam("TimCustNo"));
		int iFacmNo = this.parse.stringToInteger(titaVo.getParam("FacmNo"));
		int iBormNo = this.parse.stringToInteger(titaVo.getParam("BormNo"));
		int iEntryDate = this.parse.stringToInteger(titaVo.getParam("EntryDate"));
		BigDecimal iNewDueAmt = this.parse.stringToBigDecimal(titaVo.getParam("NewDueAmt"));
		BigDecimal iExtraRepay = this.parse.stringToBigDecimal(titaVo.getParam("TimExtraRepay"));

		if (iBormNo > 0) {
			wkBormNoStart = iBormNo;
			wkBormNoEnd = iBormNo;
		}

		// 輸入新攤還金額
		if (iNewDueAmt.compareTo(BigDecimal.ZERO) > 0) {

			tLoanBorMain = loanBorMainService.findById(new LoanBorMainId(iCustNo, iFacmNo, iBormNo), titaVo);
			if (tLoanBorMain == null) {
				throw new LogicException(titaVo, "E0001", "放款主檔"); // 查詢資料不存在
			}
			if (!(tLoanBorMain.getStatus() == 0 || tLoanBorMain.getStatus() == 4)) {
				throw new LogicException(titaVo, "E3063", ""); // 該筆放款戶況非正常戶
			}
			if (!("3".equals(tLoanBorMain.getAmortizedCode()) || "4".equals(tLoanBorMain.getAmortizedCode()))) {
				throw new LogicException(titaVo, "E3066", " 攤還方式 = " + tLoanBorMain.getAmortizedCode()); // 該筆放款攤還方式非本息平均法及本金平均法
			}

			// 查詢各項費用
			baTxCom.settingUnPaid(iEntryDate, iCustNo, iFacmNo, 0, 98, BigDecimal.ZERO, titaVo); // 不分指定額度 98-全費用類別(已到期)

			// 計算至入帳日應繳之期數
			int wkTermNo = loanCom.getTermNo(2, tLoanBorMain.getFreqBase(), tLoanBorMain.getPayIntFreq(),
					tLoanBorMain.getSpecificDate(), tLoanBorMain.getSpecificDd(), iEntryDate);

			oLeftTerms = tLoanBorMain.getTotalPeriod() - wkTermNo;

			oLoanBal = tLoanBorMain.getLoanBal();
			wkStoreRate = tLoanBorMain.getStoreRate();
			// 計算至上次繳息日之期數
			if (tLoanBorMain.getPrevPayIntDate() > tLoanBorMain.getDrawdownDate()) {
				wkTermNo = wkTermNo - loanCom.getTermNo(2, tLoanBorMain.getFreqBase(), tLoanBorMain.getPayIntFreq(),
						tLoanBorMain.getSpecificDate(), tLoanBorMain.getSpecificDd(), tLoanBorMain.getPrevPayIntDate());
			}
			if (wkTermNo > 0) {
				loanCalcRepayIntCom = loanSetRepayIntCom.setRepayInt(tLoanBorMain, wkTermNo, 0, 0, iEntryDate, titaVo);
				lCalcRepayIntVo = loanCalcRepayIntCom.getRepayInt(titaVo);
				oPrincipal = oPrincipal.add(loanCalcRepayIntCom.getPrincipal());
				oInterest = oInterest.add(loanCalcRepayIntCom.getInterest());
				oDelayInt = oDelayInt.add(loanCalcRepayIntCom.getDelayInt());
				oBreachAmt = oBreachAmt.add(loanCalcRepayIntCom.getBreachAmt());
				wkTotalAmt = oPrincipal.add(oInterest).add(oDelayInt).add(oBreachAmt);
				wkStoreRate = loanCalcRepayIntCom.getStoreRate();
			}
			switch (tLoanBorMain.getAmortizedCode()) {
			case "3": // 3.本息平均法(期金)
				wkRate = wkStoreRate
						.divide(new BigDecimal(tLoanBorMain.getFreqBase() == 2 ? 1200 : 5200), 15, RoundingMode.HALF_UP)
						.multiply(new BigDecimal(tLoanBorMain.getPayIntFreq())).setScale(15, RoundingMode.HALF_UP);
				wkRateA = wkRate.add(new BigDecimal(1)).pow(oLeftTerms).setScale(15, RoundingMode.HALF_UP);
				wkRateB = wkRateA.subtract(new BigDecimal(1)).setScale(15, RoundingMode.HALF_UP);
				wkRateC = wkRate.multiply(wkRateA).divide(wkRateB, 15, RoundingMode.HALF_UP);
				wkFinalInterest = tLoanBorMain.getFinalBal().multiply(wkRate).setScale(0, RoundingMode.HALF_UP);
				this.info("   wkRate     = " + wkRate);
				this.info("   wkRateA    = " + wkRateA);
				this.info("   wkRateB    = " + wkRateB);
				this.info("   wkRateC    = " + wkRateC);
				this.info("   wkFinalInterest = " + wkFinalInterest);
				if (iNewDueAmt.compareTo(BigDecimal.ZERO) > 0 && iNewDueAmt.compareTo(wkFinalInterest) <= 0) {
					throw new LogicException(titaVo, "E3067", " 最後本金餘額的利息 = " + wkFinalInterest); // 因該筆放款有最後本金餘額，新攤還金額不足以繳息
				}
				wkNewLoanBal = iNewDueAmt.subtract(wkFinalInterest).divide(wkRateC, 15, RoundingMode.HALF_UP)
						.setScale(0, RoundingMode.HALF_UP);
				break;
			case "4": // 4.本金平均法
				wkNewLoanBal = iNewDueAmt.multiply(new BigDecimal(oLeftTerms)).add(tLoanBorMain.getFinalBal());
				break;
			}
			// oExtraRepay = oLoanBal.subtract(oPrincipal).subtract(wkNewLoanBal);
			oExtraRepay = oLoanBal.subtract(wkNewLoanBal);
			wkTotalRepay = oExtraRepay; // oExtraRepay.add(wkTotalAmt);
			this.info("   wkTotalAmt = " + wkTotalAmt);
			this.info("   oLeftTerms = " + oLeftTerms);
			this.info("   wkNewLoanBal = " + wkNewLoanBal);
			this.info("   oPrincipal   = " + oPrincipal);
			this.info("   oExtraRepay = " + oExtraRepay);
			this.info("   wkTotalRepay = " + wkTotalRepay);

			oLoanBal = BigDecimal.ZERO;
			oRate = BigDecimal.ZERO;
			oPrincipal = BigDecimal.ZERO;
			oInterest = BigDecimal.ZERO;
			oDelayInt = BigDecimal.ZERO;
			oBreachAmt = BigDecimal.ZERO;
			wkTotalAmt = BigDecimal.ZERO;

			loanCalcRepayIntCom = loanSetRepayIntCom.setRepayInt(tLoanBorMain, 0, iEntryDate, 1, iEntryDate, titaVo);
			if (iBormNo > 0 && oExtraRepay.compareTo(BigDecimal.ZERO) > 0) {
				loanCalcRepayIntCom.setExtraRepayFlag("N"); // 部分償還本金是否內含利息 Y:是 N:否
				loanCalcRepayIntCom.setExtraRepay(oExtraRepay.add(oPrincipal));
			}
			lCalcRepayIntVo = loanCalcRepayIntCom.getRepayInt(titaVo);
			oLoanBal = oLoanBal.add(tLoanBorMain.getLoanBal());
			if (loanCalcRepayIntCom.getStoreRate().compareTo(oRate) > 0) {
				oRate = loanCalcRepayIntCom.getStoreRate();
			}
			oPrincipal = oPrincipal.add(loanCalcRepayIntCom.getPrincipal());
			oInterest = oInterest.add(loanCalcRepayIntCom.getInterest());
			oDelayInt = oDelayInt.add(loanCalcRepayIntCom.getDelayInt());
			oBreachAmt = oBreachAmt.add(loanCalcRepayIntCom.getBreachAmt());
			wkTotalAmt = oPrincipal.add(oInterest).add(oDelayInt).add(oBreachAmt);
			oLeftTerms = tLoanBorMain.getTotalPeriod() - loanCalcRepayIntCom.getPaidTerms();
			for (CalcRepayIntVo c : lCalcRepayIntVo) {
				OccursList occursList = new OccursList();
				oIntStartDate = c.getStartDate() < oIntStartDate ? c.getStartDate() : oIntStartDate;
				oIntEndDate = c.getEndDate() > oIntEndDate ? c.getEndDate() : oIntEndDate;
				occursList.putParam("OOFacmNo", tLoanBorMain.getFacmNo());
				occursList.putParam("OOBormNo", tLoanBorMain.getBormNo());
				occursList.putParam("OOIntStartDate", c.getStartDate());
				occursList.putParam("OOIntEndDate", c.getEndDate());
				occursList.putParam("OOAmount", c.getAmount());
				occursList.putParam("OORate", c.getStoreRate());
				occursList.putParam("OOPrincipal", c.getPrincipal());
				occursList.putParam("OOInterest", c.getInterest());
				occursList.putParam("OODelayInt", c.getDelayInt());
				occursList.putParam("OOBreachAmt", c.getBreachAmt());
				occursList.putParam("OOLoanBal", c.getLoanBal());
				// 將每筆資料放入Tota的OcList
				this.totaVo.addOccursList(occursList);
			}

		}
		// 輸入部分償還金額
		else {

			Slice<LoanBorMain> slLoanBorMain = loanBorMainService.bormCustNoEq(iCustNo, iFacmNo, iFacmNo, wkBormNoStart,
					wkBormNoEnd, this.index, this.limit, titaVo);
			if (slLoanBorMain != null) {
				for (LoanBorMain ln : slLoanBorMain.getContent()) {
					// 正常戶且非預撥
					if (ln.getStatus() == 0 && ln.getDrawdownDate() < iEntryDate) {
						lLoanBorMain.add(ln);
					}
				}
			}
			if (lLoanBorMain.size() == 0) {
				throw new LogicException(titaVo, "E3070", ""); // 查無可計息的放款資料
			}
			Collections.sort(lLoanBorMain, new Comparator<LoanBorMain>() {
				public int compare(LoanBorMain c1, LoanBorMain c2) {
					// status
					if (c1.getStatus() != c2.getStatus()) {
						return c1.getStatus() - c2.getStatus();
					}
					// 期款依應繳日順序由小到大 > 依利率順序由大到小
					// 部分償還金額 > 0時排序
//						利率高至低>用途別>由額度編號大至小
//						用途別為9->1->3->4->5->6->2
//						欄位代碼       欄位說明     
//						1            週轉金    
//						2            購置不動產
//						3            營業用資產
//						4            固定資產  
//						5            企業投資  
//						6            購置動產
//						9            其他					
					if (iExtraRepay.compareTo(BigDecimal.ZERO) > 0) {
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
					} else {
						if (c1.getNextPayIntDate() != c2.getNextPayIntDate()) {
							return c1.getNextPayIntDate() - c2.getNextPayIntDate();
						}
						if (c1.getStoreRate().compareTo(c2.getStoreRate()) != 0) {
							return (c1.getStoreRate().compareTo(c2.getStoreRate()) > 0 ? -1 : 1);
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
			wkExtraRepay = iExtraRepay;

			if (wkExtraRepay.compareTo(BigDecimal.ZERO) > 0) {
				for (LoanBorMain ln : lLoanBorMain) {
					if (ln.getStatus() == 0 || ln.getStatus() == 4) {
						if (wkExtraRepay.compareTo(BigDecimal.ZERO) <= 0) {
							break;
						} else {
							loanCalcRepayIntCom = loanSetRepayIntCom.setRepayInt(ln, 0, iEntryDate, 1, iEntryDate,
									titaVo);
							this.info("wkExtraRepay= " + wkExtraRepay + ", LoanBal=" + ln.getLoanBal());
							if (wkExtraRepay.compareTo(ln.getLoanBal()) >= 0) {
								loanCalcRepayIntCom.setCaseCloseFlag("Y"); // 結案試算
							} else {
								loanCalcRepayIntCom.setExtraRepayFlag("Y");
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
							wkExtraRepay = wkExtraRepay.subtract(oPrincipal).subtract(oInterest).subtract(oDelayInt)
									.subtract(oBreachAmt);
							addOccurs(ln, titaVo);
							LoanCloseBreachVo v = new LoanCloseBreachVo();
							// 放入清償違約金計算List
							v.setCustNo(ln.getCustNo());
							v.setFacmNo(ln.getFacmNo());
							v.setBormNo(ln.getBormNo());
							v.setExtraRepay(loanCalcRepayIntCom.getExtraAmt());
							v.setEndDate(iEntryDate);
							iListCloseBreach.add(v);
							oLeftTerms = loanDueAmtCom.getDueTerms(loanCalcRepayIntCom.getLoanBal(), ln.getStoreRate(),
									ln.getAmortizedCode(), ln.getFreqBase(), ln.getPayIntFreq(), ln.getFinalBal(),
									ln.getDueAmt(), titaVo);
						}
					}

					this.info("期數 oLeftTerms=" + oLeftTerms);
					if (oLeftTerms > wkoLeftTerms) {
						this.info("寫入wkoLeftTerms ");
						wkoLeftTerms = oLeftTerms;
					}

				}

			}

		}
		this.info("wk期數 =" + wkoLeftTerms);
		this.totaVo.putParam("ONewDueAmt", iNewDueAmt);
		this.totaVo.putParam("OPrincipal", oPrincipal);
		this.totaVo.putParam("OIntStartDate", oIntStartDate == 9991231 ? 0 : oIntStartDate);
		this.totaVo.putParam("OIntEndDate", oIntEndDate);
		this.totaVo.putParam("ORate", oRate);
		this.totaVo.putParam("OInterest", oInterest);
		this.totaVo.putParam("ODelayInt", oDelayInt);
		this.totaVo.putParam("OBreachAmt", oBreachAmt);
		this.totaVo.putParam("OShortfall", baTxCom.getShortfall());
		this.totaVo.putParam("OShortfallInt", baTxCom.getShortfallInterest());
		this.totaVo.putParam("OShortfallPrin", baTxCom.getShortfallPrincipal());
		this.totaVo.putParam("OShortCloseBreach", baTxCom.getShortCloseBreach());
		this.totaVo.putParam("OExcessive", baTxCom.getExcessive());
		this.totaVo.putParam("OExtraRepay", oExtraRepay);
		this.totaVo.putParam("OLoanBal", oLoanBal.subtract(oPrincipal));
		this.totaVo.putParam("OLeftTerms", wkoLeftTerms > 0 ? wkoLeftTerms : oLeftTerms);

		this.addList(this.totaVo);
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
			occursList.putParam("OOLoanBal", c.getLoanBal());
			// 將每筆資料放入Tota的OcList
			this.totaVo.addOccursList(occursList);

		}
	}

}