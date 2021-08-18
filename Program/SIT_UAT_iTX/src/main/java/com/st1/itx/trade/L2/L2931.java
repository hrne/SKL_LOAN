package com.st1.itx.trade.L2;

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
import com.st1.itx.db.service.LoanBorTxService;
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.common.LoanCalcRepayIntCom;
import com.st1.itx.util.common.LoanCloseBreachCom;
import com.st1.itx.util.common.LoanCom;
import com.st1.itx.util.common.LoanSetRepayIntCom;
import com.st1.itx.util.common.data.CalcRepayIntVo;
import com.st1.itx.util.common.data.LoanCloseBreachVo;
import com.st1.itx.util.date.DateUtil;
import com.st1.itx.util.parse.Parse;

/**
 * Tita<br>
 * CUSTNO=9,7<br>
 * CUSTNO1=9,3<br>
 * CUSTNO2=9,3<br>
 * END=X,1<br>
 */

@Service("L2931")
@Scope("prototype")
/**
 * 
 * 
 * @author YuJiaXing
 * @version 1.0.0
 */
public class L2931 extends TradeBuffer {

	/* DB服務注入 */
	@Autowired
	public LoanBorTxService sLoanBorTxService;

	@Autowired
	public LoanBorMainService loanBorMainService;
	/* 日期工具 */
	@Autowired
	public DateUtil dateUtil;

	/* 轉換工具 */
	@Autowired
	public Parse parse;

	@Autowired
	LoanCloseBreachCom loanCloseBreachCom;
	@Autowired
	LoanSetRepayIntCom loanSetRepayIntCom;
	@Autowired
	LoanCalcRepayIntCom loanCalcRepayIntCom;
	@Autowired
	LoanCom loanCom;

	private int iCustNo;
	private int iFacmNo;
	private int iBormNo;
	private int iEntryDate;
	private int iInqKind;
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
		this.info("active L2931 ");
		this.totaVo.init(titaVo);
		loanCom.setTxBuffer(this.txBuffer);
		loanSetRepayIntCom.setTxBuffer(this.txBuffer);
		loanCloseBreachCom.setTxBuffer(this.txBuffer);

		// 戶號
		iCustNo = parse.stringToInteger(titaVo.getParam("CustNo"));
		// 額度
		iFacmNo = parse.stringToInteger(titaVo.getParam("FacmNo"));
		// 撥款
		iBormNo = parse.stringToInteger(titaVo.getParam("BormNo"));
		// 查詢方式 0-清償違約明細 1-清償違約明細+部分償還試算 2-清償違約明細+結案試算
		iInqKind = this.parse.stringToInteger(titaVo.getParam("InqKind"));
		// 入帳日期
		iEntryDate = this.parse.stringToInteger(titaVo.getParam("EntryDate"));
		// 部分償還本金
		iExtraRepay = this.parse.stringToBigDecimal(titaVo.getParam("TimExtraRepay"));
		// 是否內含利息(Y/N)
		iExtraRepayFlag = titaVo.getParam("IncludeIntFlag");

		ArrayList<LoanCloseBreachVo> oListCloseBreach = new ArrayList<LoanCloseBreachVo>();

		// 計息
		if (iInqKind == 1 || iInqKind == 2) {
			caculate(titaVo);
		}
		// 計算清償違約金
		oListCloseBreach = loanCloseBreachCom.getCloseBreachAmtAll(iCustNo, iFacmNo, iBormNo, iListCloseBreach, titaVo);
		if (oListCloseBreach == null || oListCloseBreach.size() == 0) {
			throw new LogicException(titaVo, "E2003", ""); // 查無資料
		}
		// 輸出清償違約金
		for (LoanCloseBreachVo v : oListCloseBreach) {
			if (v.getCloseBreachAmt().compareTo(BigDecimal.ZERO) > 0) {
				OccursList occursList = new OccursList();
				occursList.putParam("OOFacmNo", v.getFacmNo()); // 額度
				occursList.putParam("OOBormNo", v.getBormNo()); // 撥款
				occursList.putParam("OOAcDate", v.getAcDate()); // 會計日期
				occursList.putParam("OOTitaTxtNo", v.getTitaTxtNo()); // 交易序號
				occursList.putParam("OOTitaTxCd", v.getTitaTxCd()); // 交易摘要
				occursList.putParam("OOEntryDate", v.getEndDate()); // 入帳日期
				occursList.putParam("OOExtraRepay", v.getExtraRepay()); // 提前還款金額
				occursList.putParam("OOCloseBreachAmt", v.getCloseBreachAmt()); // 清償違約金
				occursList.putParam("OOExtraRepayAcc", v.getExtraRepayAcc()); // 提前還款金額累計
				occursList.putParam("OOBreachStartAmt", v.getBreachStartAmt()); // 違約起算金額
				occursList.putParam("OOAmount", v.getAmount()); // 計算金額
				occursList.putParam("OOStartDate", v.getStartDate()); // 首撥日期
				occursList.putParam("OOMonIdx", v.getMonIdx()); // 遞減段數
				occursList.putParam("OOBreachRate", v.getBreachRate()); // 計算百分比
				occursList.putParam("OOBreachGetCode", v.getBreachGetCode()); // 清償違約金收取方式 "1":即時收取 "2":領清償證明時收取

				this.info("occursList L2931" + occursList);
				this.totaVo.addOccursList(occursList);
			}
		}

		this.addList(this.totaVo);
		return this.sendList();
	}

	private void caculate(TitaVo titaVo) throws LogicException {
		if (iFacmNo > 0) {
			wkFacmNoStart = iFacmNo;
			wkFacmNoEnd = iFacmNo;
		}

		if (iBormNo > 0) {
			wkBormNoStart = iBormNo;
			wkBormNoEnd = iBormNo;
		}
		Slice<LoanBorMain> slLoanBorMain = loanBorMainService.bormCustNoEq(iCustNo, wkFacmNoStart, wkFacmNoEnd,
				wkBormNoStart, wkBormNoEnd, this.index, this.limit, titaVo);
		lLoanBorMain = slLoanBorMain == null ? null : new ArrayList<LoanBorMain>(slLoanBorMain.getContent());
		if (lLoanBorMain == null || lLoanBorMain.size() == 0) {
			throw new LogicException(titaVo, "E0001", "放款主檔"); // 查詢資料不存在
		}
		Collections.sort(lLoanBorMain, new Comparator<LoanBorMain>() {
			public int compare(LoanBorMain c1, LoanBorMain c2) {
				// 部分償還金額 > 0時排序,依利率順序由大到小 else 依應繳日順序由小到大
				if (iExtraRepay.compareTo(BigDecimal.ZERO) > 0) {
					if (c1.getStoreRate().compareTo(c2.getStoreRate()) != 0) {
						return (c1.getStoreRate().compareTo(c2.getStoreRate()) > 0 ? 1 : -1);
					}
				} else {
					if (c1.getNextPayIntDate() != c2.getNextPayIntDate()) {
						return c1.getNextPayIntDate() - c2.getNextPayIntDate();
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
		//
		for (LoanBorMain ln : lLoanBorMain) {
			// 部分償還金額先償還期款
			if (iExtraRepay.compareTo(BigDecimal.ZERO) > 0) {
				if (ln.getStatus() == 0 || ln.getStatus() == 4) {
					if (ln.getPrevPayIntDate() >= iEntryDate || ln.getDrawdownDate() == iEntryDate) {
						continue;
					}
					// 計算至入帳日期應繳之期數 - 計算至上次繳息日之期數
					wkTerms = loanCom.getTermNo(2, ln.getFreqBase(), ln.getPayIntFreq(), ln.getSpecificDate(),
							ln.getSpecificDd(), iEntryDate);
					if (ln.getPrevPayIntDate() > ln.getDrawdownDate()) {
						wkPreRepayTermNo = loanCom.getTermNo(2, ln.getFreqBase(), ln.getPayIntFreq(),
								ln.getSpecificDate(), ln.getSpecificDd(), ln.getPrevPayIntDate());
						wkTerms = wkTerms - wkPreRepayTermNo;
					}

					if (wkTerms <= 0) {
						continue;
					}

					// 計算
					loanCalcRepayIntCom = loanSetRepayIntCom.setRepayInt(ln, wkTerms, 0, 0, iEntryDate, titaVo);
					lCalcRepayIntVo = loanCalcRepayIntCom.getRepayInt(titaVo);
					ln.setStoreRate(loanCalcRepayIntCom.getStoreRate());
					ln.setLoanBal(ln.getLoanBal().subtract(loanCalcRepayIntCom.getPrincipal()));
					ln.setRepaidPeriod(ln.getRepaidPeriod() + loanCalcRepayIntCom.getRepaidPeriod());
					ln.setPaidTerms(loanCalcRepayIntCom.getPaidTerms());
					ln.setPrevPayIntDate(loanCalcRepayIntCom.getPrevPaidIntDate());
					ln.setPrevRepaidDate(loanCalcRepayIntCom.getPrevRepaidDate());
					ln.setNextPayIntDate(loanCalcRepayIntCom.getNextPayIntDate());
					ln.setNextRepayDate(loanCalcRepayIntCom.getNextRepayDate());
					wkExtraRepay = wkExtraRepay.subtract(loanCalcRepayIntCom.getPrincipal())
							.subtract(loanCalcRepayIntCom.getInterest()).subtract(loanCalcRepayIntCom.getDelayInt())
							.subtract(loanCalcRepayIntCom.getBreachAmt());
				}
			}
		}
		this.info("ExtraRepay= " + iExtraRepay + "," + wkExtraRepay);

		for (

		LoanBorMain ln : lLoanBorMain) {
			if (ln.getStatus() == 0 || ln.getStatus() == 4) {
				if (iInqKind == 2 || wkExtraRepay.compareTo(BigDecimal.ZERO) >= 0) {
					loanCalcRepayIntCom = loanSetRepayIntCom.setRepayInt(ln, 0, iEntryDate, 1, iEntryDate, titaVo);
					if (iInqKind == 2 || wkExtraRepay.compareTo(ln.getLoanBal()) >= 0) {
						loanCalcRepayIntCom.setCaseCloseFlag("Y"); // 結案試算
					} else {
						loanCalcRepayIntCom.setExtraRepayFlag(iExtraRepayFlag);
						loanCalcRepayIntCom.setExtraRepay(wkExtraRepay); // 部分償還本金試算
					}
					// 計算
					lCalcRepayIntVo = loanCalcRepayIntCom.getRepayInt(titaVo);
					wkTotaCount++;
					LoanCloseBreachVo v = new LoanCloseBreachVo();
					// 放入清償違約金計算List
					v.setCustNo(ln.getCustNo());
					v.setFacmNo(ln.getFacmNo());
					v.setBormNo(ln.getBormNo());
					v.setExtraRepay(loanCalcRepayIntCom.getExtraAmt());
					v.setEndDate(iEntryDate);
					iListCloseBreach.add(v);
					wkExtraRepay = wkExtraRepay.subtract(loanCalcRepayIntCom.getPrincipal())
							.subtract(loanCalcRepayIntCom.getInterest()).subtract(loanCalcRepayIntCom.getDelayInt())
							.subtract(loanCalcRepayIntCom.getBreachAmt());
				}
			}
		}

		if (wkTotaCount == 0) {
			throw new LogicException(titaVo, "E3070", ""); // 查無可計息的放款資料

		}

	}
}