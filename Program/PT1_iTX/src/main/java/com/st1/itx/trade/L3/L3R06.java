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
import com.st1.itx.util.common.data.BaTxVo;
import com.st1.itx.util.common.data.LoanCloseBreachVo;
import com.st1.itx.util.date.DateUtil;
import com.st1.itx.util.parse.Parse;

/**
 * L3R06 回收試算
 * 
 * @author iza
 * @version 1.0.0
 */
@Service("L3R06")
@Scope("prototype")
public class L3R06 extends TradeBuffer {

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
	BaTxCom baTxCom;
	@Autowired
	LoanSetRepayIntCom loanSetRepayIntCom;
	@Autowired
	LoanCalcRepayIntCom loanCalcRepayIntCom;
	@Autowired
	LoanCloseBreachCom loanCloseBreachCom;

	private TitaVo titaVo;
	private int iFuncCode;
	private int iFKey;
	private int iCustNo;
	private int iFacmNo;
	private int iBormNo;
	private int oIntStartDate = 9991231;
	private int oIntEndDate = 0;
	private int iRepayTerms;
	private int iRepayType;
	private int iEntryDate;
	private int payMethodFg = 0;

	private String iExtraRepayFlag;
	private String iTxCode = "";
	private String oCurrencyCode = "";
	private BigDecimal iExtraRepay;
	private BigDecimal oLoanBal = BigDecimal.ZERO;
	private BigDecimal oRate = BigDecimal.ZERO;
	private BigDecimal oPrincipal = BigDecimal.ZERO;
	private BigDecimal oInterest = BigDecimal.ZERO;
	private BigDecimal oDelayInt = BigDecimal.ZERO;
	private BigDecimal oBreachAmt = BigDecimal.ZERO;
	private BigDecimal oCloseBreachAmt = BigDecimal.ZERO;
	private BigDecimal oShortfall = BigDecimal.ZERO;
	private BigDecimal oShortfallInt = BigDecimal.ZERO;
	private BigDecimal oShortfallPrin = BigDecimal.ZERO;
	private BigDecimal oShortCloseBreach = BigDecimal.ZERO;
	private BigDecimal oExcessive = BigDecimal.ZERO;
	private BigDecimal oTempTax = BigDecimal.ZERO;
	private BigDecimal oModifyFee = BigDecimal.ZERO;
	private BigDecimal oAcctFee = BigDecimal.ZERO;
	private BigDecimal oFireFee = BigDecimal.ZERO;
	private BigDecimal oCollFireFee = BigDecimal.ZERO;
	private BigDecimal oLawFee = BigDecimal.ZERO;
	private BigDecimal oCollLawFee = BigDecimal.ZERO;
	private BigDecimal oRepayAmt = BigDecimal.ZERO;
	private BigDecimal oTotalFee = BigDecimal.ZERO;
	private int oRpFacmNo = 0;
	private int oShortFacmNo = 0;
	private BigDecimal oCloseBreachAmtUnpaid = BigDecimal.ZERO;
	private List<LoanBorMain> lLoanBorMain = new ArrayList<LoanBorMain>();
	private ArrayList<LoanCloseBreachVo> iListCloseBreach = new ArrayList<LoanCloseBreachVo>();
	private ArrayList<LoanCloseBreachVo> oListCloseBreach = new ArrayList<LoanCloseBreachVo>();
	private ArrayList<BaTxVo> baTxList;

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active L3R06 ");
		this.totaVo.init(titaVo);
		this.titaVo = titaVo;
		loanSetRepayIntCom.setTxBuffer(this.txBuffer);
		loanCloseBreachCom.setTxBuffer(this.txBuffer);

		// 取得輸入資料
		iFuncCode = this.parse.stringToInteger(titaVo.getParam("RimFuncCode"));
		iTxCode = titaVo.getParam("RimTxCode");
		iFKey = this.parse.stringToInteger(titaVo.getParam("RimFKey"));
		iCustNo = this.parse.stringToInteger(titaVo.getParam("RimCustNo"));
		iFacmNo = this.parse.stringToInteger(titaVo.getParam("RimFacmNo"));
		iBormNo = this.parse.stringToInteger(titaVo.getParam("RimBormNo"));
		iRepayTerms = this.parse.stringToInteger(titaVo.getParam("RimRepayTerms"));
		iRepayType = this.parse.stringToInteger(titaVo.getParam("RimRepayType"));
		iEntryDate = this.parse.stringToInteger(titaVo.getParam("RimEntryDate"));
		iExtraRepay = this.parse.stringToBigDecimal(titaVo.getParam("RimExtraRepay"));
		this.parse.stringToBigDecimal(titaVo.getParam("RimCloseBreachAmt"));
		iExtraRepayFlag = titaVo.getParam("RimIncludeIntFlag");

		switch (iFuncCode) {
		case 1:
			CloseBreachAmtRoutine();
			break;
		case 2:
			RepayAmtRoutine();
			break;
		case 3:
			EachFeeRoutine();
			break;
		default:
			throw new LogicException(titaVo, "E0010", " iFuncCode = " + iFuncCode); // 功能選擇錯誤
		}

		this.totaVo.putParam("L3r06LoanBal", oLoanBal);
		this.totaVo.putParam("L3r06IntStartDate", oIntStartDate);
		this.totaVo.putParam("L3r06IntEndDate", oIntEndDate);
		this.totaVo.putParam("L3r06Rate", oRate);
		this.totaVo.putParam("L3r06CurrencyCode", oCurrencyCode);
		this.totaVo.putParam("L3r06Principal", oPrincipal);
		this.totaVo.putParam("L3r06Interest", oInterest);
		this.totaVo.putParam("L3r06DelayInt", oDelayInt);
		this.totaVo.putParam("L3r06BreachAmt", oBreachAmt);
		this.totaVo.putParam("L3r06CloseBreachAmt", oCloseBreachAmt);
		this.totaVo.putParam("L3r06Shortfall", oShortfall);
		this.totaVo.putParam("L3r06ShortfallInt", oShortfallInt);
		this.totaVo.putParam("L3r06ShortfallPrin", oShortfallPrin);
		this.totaVo.putParam("L3r06ShortCloseBreach", oShortCloseBreach);
		this.totaVo.putParam("L3r06Excessive", oExcessive);
		this.totaVo.putParam("L3r06TempTax", oTempTax);
		this.totaVo.putParam("L3r06ModifyFee", oModifyFee);
		this.totaVo.putParam("L3r06AcctFee", oAcctFee);
		this.totaVo.putParam("L3r06FireFee", oFireFee);
		this.totaVo.putParam("L3r06CollFireFee", oCollFireFee);
		this.totaVo.putParam("L3r06LawFee", oLawFee);
		this.totaVo.putParam("L3r06CollLawFee", oCollLawFee);
		this.totaVo.putParam("L3r06RepayAmt", oRepayAmt);
		this.totaVo.putParam("L3r06TotalFee", oTotalFee);
		this.totaVo.putParam("L3r06RpFacmNo", oRpFacmNo);
		this.totaVo.putParam("L3r06ShortFacmNo", oShortFacmNo);
		this.totaVo.putParam("L3r06CloseBreachAmtUnpaid", oCloseBreachAmtUnpaid);
		this.totaVo.putParam("L3r06PayMethodFg", payMethodFg);

		this.addList(this.totaVo);
		return this.sendList();
	}

	private void CloseBreachAmtRoutine() throws LogicException {
		this.info("CloseBreachAmtRoutine ... ");
		// 查詢各項費用
		baTxCom.settingUnPaid(iEntryDate, iCustNo, iFacmNo, iBormNo, 0, BigDecimal.ZERO, titaVo);
		oCloseBreachAmt = baTxCom.getShortCloseBreach();
		oRepayAmt = oCloseBreachAmt;
		this.info("   oCloseBreachAmt = " + oCloseBreachAmt);
		this.info("CloseBreachAmtRoutine end ");
	}

	private void RepayAmtRoutine() throws LogicException {
		int wkFacmNoStart = 1;
		int wkFacmNoEnd = 999;
		int wkBormNoStart = 1;
		int wkBormNoEnd = 900;
		int wkTerms = 0;
		int wkTotaCount = 0;
		int wkPreRepayTermNo = 0;
		int wkTermNo = 0;
		int wkPreRepayDate = 0;
		int wkPrevTermNo = 0;

		this.info("RepayAmtRoutine ... ");

		// 查詢各項費用
		this.baTxList = new ArrayList<BaTxVo>();
		this.baTxList = baTxCom.settingUnPaid(iEntryDate, iCustNo, iFacmNo, iBormNo, 0, BigDecimal.ZERO, titaVo); // 00-費用全部(已到期)

		oAcctFee = baTxCom.getAcctFee();
		oFireFee = baTxCom.getFireFee();
		oModifyFee = baTxCom.getModifyFee();
		oLawFee = baTxCom.getLawFee();
		oCollFireFee = baTxCom.getCollFireFee();
		oCollLawFee = baTxCom.getCollLawFee();
		oShortfall = baTxCom.getShortfall();
		oShortfallInt = baTxCom.getShortfallInterest();
		oShortfallPrin = baTxCom.getShortfallPrincipal();
		oShortCloseBreach = baTxCom.getShortCloseBreach();
		oExcessive = baTxCom.getExcessive().add(baTxCom.getExcessiveOther());

		oTotalFee = oTotalFee.add(oModifyFee).add(oAcctFee).add(oFireFee).add(oCollFireFee).add(oLawFee)
				.add(oCollLawFee);

		if (iFacmNo > 0) {
			wkFacmNoStart = iFacmNo;
			wkFacmNoEnd = iFacmNo;
			oRpFacmNo = iFacmNo;
		}

		if (iBormNo > 0) {
			wkBormNoStart = iBormNo;
			wkBormNoEnd = iBormNo;
		}

		BigDecimal wkExtraRepay = iExtraRepay;
		Slice<LoanBorMain> slLoanBorMain = loanBorMainService.bormCustNoEq(iCustNo, wkFacmNoStart, wkFacmNoEnd,
				wkBormNoStart, wkBormNoEnd, 0, Integer.MAX_VALUE, titaVo);
		lLoanBorMain = slLoanBorMain == null ? null : slLoanBorMain.getContent();
		if (lLoanBorMain == null || lLoanBorMain.size() == 0) {
			throw new LogicException(titaVo, "E0001", "放款主檔"); // 查詢資料不存在
		}
//		檢核
		for (LoanBorMain ln : lLoanBorMain) {
			if ((iTxCode.equals("L3440") && ln.getStatus() == 2)
					|| (!iTxCode.equals("L3440") && (ln.getStatus() == 0 || ln.getStatus() == 4))) {
				if (ln.getActFg() == 1 && iFKey == 0) {
					throw new LogicException(titaVo, "E0021",
							"放款主檔 戶號 = " + ln.getCustNo() + " 額度編號 =  " + ln.getFacmNo() + " 撥款序號 = " + ln.getBormNo()); // 該筆資料待放行中
				}
				if ("5".equals(ln.getAmortizedCode())) { // 攤還方式 = 5.按月撥款收息(逆向貸款)
					continue;
				}
				if (iExtraRepay.compareTo(BigDecimal.ZERO) > 0) { // 部分償還本金 > 0
					if (ln.getNextPayIntDate() <= iEntryDate) {
						throw new LogicException(titaVo, "E3072", "應繳息日 = " + ln.getNextPayIntDate()); // 該筆放款尚有其款未回收
					}
				}
			}
		}

//		TODO: Sorting 部分償還時 需要繳費用的額度才需要檢查攤還方式是否為3
		if (iRepayType == 2) {
			Sorting();
		}
		int test = 0;
		for (LoanBorMain ln : lLoanBorMain) {
			if ((iTxCode.equals("L3440") && ln.getStatus() == 2)
					|| (!iTxCode.equals("L3440") && (ln.getStatus() == 0 || ln.getStatus() == 4))) {
				if ("5".equals(ln.getAmortizedCode())) { // 攤還方式 = 5.按月撥款收息(逆向貸款)
					continue;
				}
				if (iExtraRepay.compareTo(BigDecimal.ZERO) > 0) { // 部分償還本金 > 0
					wkTerms = 0;
					loanCalcRepayIntCom = loanSetRepayIntCom.setRepayInt(ln, 0, iEntryDate, 1, iEntryDate, titaVo);
					if (wkExtraRepay.compareTo(ln.getLoanBal()) >= 0 || iEntryDate >= ln.getMaturityDate()) {
						loanCalcRepayIntCom.setCaseCloseFlag("Y");
					} else {
						loanCalcRepayIntCom.setExtraRepayFlag(iExtraRepayFlag);
						loanCalcRepayIntCom.setExtraRepay(wkExtraRepay);
					}
				} else {
					wkPrevTermNo = 0;
					wkPrevTermNo = 0;
					// 計算至上次繳息日之期數
					if (ln.getPrevPayIntDate() > 0) {
						wkPrevTermNo = loanCom.getTermNo(2, ln.getFreqBase(), ln.getPayIntFreq(), ln.getSpecificDate(),
								ln.getSpecificDd(), ln.getPrevPayIntDate());
					}

					// 可回收期數
					wkPreRepayTermNo = loanCom.getTermNo(iEntryDate >= ln.getMaturityDate() ? 1 : 2, ln.getFreqBase(),
							ln.getPayIntFreq(), ln.getSpecificDate(), ln.getSpecificDd(),
							this.txBuffer.getTxCom().getTbsdy());

					// 可回收期數；可回收期數 = 已到期期數 + 預收期數
					wkPreRepayTermNo = wkPreRepayTermNo + this.txBuffer.getSystemParas().getPreRepayTerms();

					if (iRepayTerms > 0) { // 回收期數 > 0
						if ((iRepayTerms + wkPrevTermNo) > wkPreRepayTermNo) {
							this.info(" 戶號:" + iCustNo + "-" + ln.getFacmNo() + "-" + ln.getBormNo() + ",可預收迄日"
									+ wkPreRepayDate + ",可回收期數= " + (wkPreRepayTermNo - wkPrevTermNo));
							continue;
						}
						loanCalcRepayIntCom = loanSetRepayIntCom.setRepayInt(ln, iRepayTerms, 0, 0, iEntryDate, titaVo);
					} else {
						if (ln.getPrevPayIntDate() >= iEntryDate || ln.getDrawdownDate() == iEntryDate) {
							continue;
						}
						// 計算至入帳日期應繳之期數 - 計算至上次繳息日之期數
						wkTerms = loanCom.getTermNo(iEntryDate >= ln.getMaturityDate() ? 1 : 2, ln.getFreqBase(),
								ln.getPayIntFreq(), ln.getSpecificDate(), ln.getSpecificDd(), iEntryDate);
						if (ln.getPrevPayIntDate() > 0) {
							wkTerms = wkTerms - loanCom.getTermNo(2, ln.getFreqBase(), ln.getPayIntFreq(),
									ln.getSpecificDate(), ln.getSpecificDd(), ln.getPrevPayIntDate());
						}
						if (wkTerms <= 0) {
							this.info(" 戶號:" + iCustNo + "-" + ln.getFacmNo() + "-" + ln.getBormNo() + ",可回收期數= "
									+ wkTerms);
							continue;
						}
						loanCalcRepayIntCom = loanSetRepayIntCom.setRepayInt(ln, wkTerms, 0, 0, iEntryDate, titaVo);
					}
				}
//				TODO:UI控制 繳納方式: 攤還方式為3時記號為1否則為0 1.顯示可輸入 2.不顯示不可輸入 
				test++;
				this.info("L3R06 test = " + lLoanBorMain.size() + test);
				this.info("L3R06 ln攤還方式 = " + ln.getAmortizedCode());
				if ("3".equals(ln.getAmortizedCode()) || "4".equals(ln.getAmortizedCode())) {
					payMethodFg = 1;
				}
				loanCalcRepayIntCom.getRepayInt(titaVo);
				oLoanBal = oLoanBal.add(ln.getLoanBal());
				oRate = ln.getStoreRate();
				oCurrencyCode = ln.getCurrencyCode();
				oPrincipal = oPrincipal.add(loanCalcRepayIntCom.getPrincipal());
				oInterest = oInterest.add(loanCalcRepayIntCom.getInterest());
				oDelayInt = oDelayInt.add(loanCalcRepayIntCom.getDelayInt());
				oBreachAmt = oBreachAmt.add(loanCalcRepayIntCom.getBreachAmt());
				if(oRpFacmNo==0) {
					oRpFacmNo = ln.getFacmNo();
				}
				oShortFacmNo = ln.getFacmNo();
				wkTotaCount++;
				if (iExtraRepay.compareTo(BigDecimal.ZERO) > 0) { // 部分償還本金 > 0
					LoanCloseBreachVo v = new LoanCloseBreachVo();
					v.setCustNo(ln.getCustNo());
					v.setFacmNo(ln.getFacmNo());
					v.setBormNo(ln.getBormNo());
					v.setExtraRepay(loanCalcRepayIntCom.getExtraAmt());
					v.setEndDate(iEntryDate);
					iListCloseBreach.add(v);
					wkExtraRepay = iExtraRepay.subtract(oPrincipal).subtract(oInterest).subtract(oDelayInt)
							.subtract(oBreachAmt);
					if (wkExtraRepay.compareTo(BigDecimal.ZERO) <= 0) {
						break;
					}
				}
			}
		}
		if (oShortfall.compareTo(BigDecimal.ZERO) == 0 && wkTotaCount == 0
				&& iExtraRepay.compareTo(BigDecimal.ZERO) == 0 && !iTxCode.equals("L3440")) {
			if (iBormNo > 0) {
				throw new LogicException(titaVo, "E3070",
						"戶號 = " + iCustNo + " 額度編號 = " + iFacmNo + " 撥款序號 = " + iBormNo); // 查無可計息的放款資料
			}
			if (iFacmNo > 0) {
				throw new LogicException(titaVo, "E3070", "戶號 = " + iCustNo + " 額度編號 = " + iFacmNo); // 查無可計息的放款資料
			}
			throw new LogicException(titaVo, "E3070", "戶號 = " + iCustNo); // 查無可計息的放款資料

		}
		// 還款金額
		oRepayAmt = oRepayAmt.add(oPrincipal).add(oInterest).add(oDelayInt).add(oBreachAmt);

		// 輸出清償違約金
		// 計算清償違約金，收取方式 "1":即時收取
		if (iExtraRepay.compareTo(BigDecimal.ZERO) > 0) {
			oListCloseBreach = loanCloseBreachCom.getCloseBreachAmtPaid(iCustNo, iFacmNo, iBormNo, iListCloseBreach,
					titaVo);
			if (oListCloseBreach != null && oListCloseBreach.size() > 0) {
				for (LoanCloseBreachVo v : oListCloseBreach) {
					if (v.getCloseBreachAmtPaid().compareTo(BigDecimal.ZERO) > 0) {
						oCloseBreachAmt = oCloseBreachAmt.add(v.getCloseBreachAmtPaid());
					}
				}
			}
		}

		this.info("RepayAmtRoutine end ");
	}

	private void Sorting() throws LogicException {
		Collections.sort(lLoanBorMain, new Comparator<LoanBorMain>() {
			public int compare(LoanBorMain c1, LoanBorMain c2) {
				// status
				if (c1.getStatus() != c2.getStatus()) {
					return c1.getStatus() - c2.getStatus();
				}
				// 回收金額 > 0時排序,依應繳日順序由小到大、利率順序由大到小、額度由小到大
//				if (iRepayType == 1) {
//					if (c1.getNextPayIntDate() != c2.getNextPayIntDate()) {
//						return c1.getNextPayIntDate() - c2.getNextPayIntDate();
//					}
//					if (c1.getStoreRate().compareTo(c2.getStoreRate()) != 0) {
//						return (c1.getStoreRate().compareTo(c2.getStoreRate()) > 0 ? -1 : 1);
//					}
//				}
				// 部分償還金額 > 0時排序
//					利率高至低>用途別>由額度編號大至小
//					用途別為9->1->3->4->5->6->2
//					欄位代碼       欄位說明     
//					1            週轉金    
//					2            購置不動產
//					3            營業用資產
//					4            固定資產  
//					5            企業投資  
//					6            購置動產
//					9            其他					
				if (iRepayType == 2) {
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
	}

	private void EachFeeRoutine() throws LogicException {
		this.info("EachFeeRoutine ... ");

		// 查詢各項費用
		baTxCom.settingUnPaid(iEntryDate, iCustNo, iFacmNo, iBormNo, 0, BigDecimal.ZERO, titaVo);

		oAcctFee = baTxCom.getAcctFee();
		oFireFee = baTxCom.getFireFee();
		oModifyFee = baTxCom.getModifyFee();
		oLawFee = baTxCom.getLawFee();
		oCollFireFee = baTxCom.getCollFireFee();
		oCollLawFee = baTxCom.getCollLawFee();
		oShortfall = baTxCom.getShortfall();
		oShortfallInt = baTxCom.getShortfallInterest();
		oShortfallPrin = baTxCom.getShortfallPrincipal();
		oShortCloseBreach = baTxCom.getShortCloseBreach();

		switch (iRepayType) {
		case 4:
			if (oAcctFee.compareTo(BigDecimal.ZERO) == 0) {
				throw new LogicException(titaVo, "E3089",
						" 戶號 = " + iCustNo + " 額度編號 = " + iFacmNo + " 撥款序號 = " + iBormNo); // 查無未收帳管費
			}
			break;
		case 5:
			if (oFireFee.compareTo(BigDecimal.ZERO) == 0) {
				throw new LogicException(titaVo, "E3090",
						" 戶號 = " + iCustNo + " 額度編號 = " + iFacmNo + " 撥款序號 = " + iBormNo); // 查無未收火險費
			}
			break;
		case 6:
			if (oModifyFee.compareTo(BigDecimal.ZERO) == 0) {
				throw new LogicException(titaVo, "E3091",
						" 戶號 = " + iCustNo + " 額度編號 = " + iFacmNo + " 撥款序號 = " + iBormNo); // 查無未收契變收續費
			}
			break;
		case 7:
			if (oLawFee.compareTo(BigDecimal.ZERO) == 0) {
				throw new LogicException(titaVo, "E3092",
						" 戶號 = " + iCustNo + " 額度編號 = " + iFacmNo + " 撥款序號 = " + iBormNo); // 查無未收法務費
			}
		}

		this.info("   oAcctFee        = " + oAcctFee);
		this.info("   oFireFee        = " + oFireFee);
		this.info("   oModifyFee      = " + oModifyFee);
		this.info("   oLawFee         = " + oLawFee);
		this.info("   oShortfall      = " + oShortfall);
		this.info("EachFeeRoutine end ");
	}
}