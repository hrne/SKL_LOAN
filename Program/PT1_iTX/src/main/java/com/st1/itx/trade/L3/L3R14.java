package com.st1.itx.trade.L3;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;

import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.dataVO.TotaVo;
import com.st1.itx.db.domain.BankRemit;
import com.st1.itx.db.domain.CdBank;
import com.st1.itx.db.domain.CdBankId;
import com.st1.itx.db.domain.FacMain;
import com.st1.itx.db.domain.FacMainId;
import com.st1.itx.db.domain.LoanBorMain;
import com.st1.itx.db.domain.LoanBorMainId;
import com.st1.itx.db.service.AcReceivableService;
import com.st1.itx.db.service.BankRemitService;
import com.st1.itx.db.service.CdBankService;
import com.st1.itx.db.service.FacMainService;
import com.st1.itx.db.service.LoanBorMainService;
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.common.BaTxCom;
import com.st1.itx.util.common.LoanCalcRepayIntCom;
import com.st1.itx.util.common.LoanCom;
import com.st1.itx.util.common.LoanSetRepayIntCom;
import com.st1.itx.util.date.DateUtil;
import com.st1.itx.util.parse.Parse;

/*
 * Tita
 * RimTxCode=X,5
 * RimFKey=9,1
 * RimFuncCode=9,1
 * RimCustNo=9,7
 * RimFacmNo=9,3
 * RimBormNo=9,3
 * RimCurrencyCode=X,3
 * RimExtraRepay=9,14.2
 * RimIncludeIntFlag=X,1 是否內含利息 Y:是 N:否
 * RimRepayTerms=9,2
 * RimRepayType=9,2
 * RimEntryDate=9,7
 * RimCloseBreachAmt=9,14.2
 */

/**
 * L3R14 撥款收息試算
 * 
 * @author iza
 * @version 1.0.0
 */
@Service("L3R14")
@Scope("prototype")
public class L3R14 extends TradeBuffer {

	/* DB服務注入 */
	@Autowired
	public LoanBorMainService loanBorMainService;
	@Autowired
	public AcReceivableService acReceivableService;
	@Autowired
	public BankRemitService bankRemitService;
	@Autowired
	public FacMainService facMainService;
	@Autowired
	public CdBankService cdBankService;

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
	BaTxCom baTxCom;

	private TitaVo titaVo;
	private int iFKey;
	private int iCustNo;
	private int iFacmNo;
	private int iEntryDate;
	private int OBormNo;
	private String ORemk;
	private BigDecimal oLoanBal = BigDecimal.ZERO;
	private BigDecimal oDuePayAmt = BigDecimal.ZERO;
	private BigDecimal oPrincipal = BigDecimal.ZERO;
	private BigDecimal oInterest = BigDecimal.ZERO;
	private BigDecimal oOpenInterest = BigDecimal.ZERO;
	private BigDecimal oRealPayAmt = BigDecimal.ZERO;
	private BigDecimal oRepayAmt = BigDecimal.ZERO;
	private FacMain tFacMain;
	private LoanBorMain tLoanBorMain;
	private LoanBorMain t2LoanBorMain;
//	private LoanEachFeeVo loanEachFeeVo = new LoanEachFeeVo();
	private List<LoanBorMain> lLoanBorMain = new ArrayList<LoanBorMain>();
	private DecimalFormat df = new DecimalFormat("##,###,###,###,##0");

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active L3R14 ");
		this.totaVo.init(titaVo);
		this.titaVo = titaVo;
		loanSetRepayIntCom.setTxBuffer(this.txBuffer);
		baTxCom.setTxBuffer(this.txBuffer);

		// 取得輸入資料
		iCustNo = this.parse.stringToInteger(titaVo.getParam("RimCustNo"));
		iFacmNo = this.parse.stringToInteger(titaVo.getParam("RimFacmNo"));
		iFKey = this.parse.stringToInteger(titaVo.getParam("RimFKey"));
		iEntryDate = this.parse.stringToInteger(titaVo.getParam("RimDrawdownDate"));

		this.info("iCustNo	= " + iCustNo);
		this.info("iFacmNo	= " + iFacmNo);
		this.info("iFKey		= " + iFKey);
		this.info("iEntryDate	= " + iEntryDate);

		tFacMain = facMainService.findById(new FacMainId(iCustNo, iFacmNo), titaVo);
		if (tFacMain == null) {
			throw new LogicException(titaVo, "E0001", " 額度主檔 借款人戶號 = " + iCustNo + " 額度編號 = " + iFacmNo); // 查詢資料不存在
		}

		if (tFacMain.getActFg() == 1 && iFKey == 0) {
			throw new LogicException(titaVo, "E0021",
					"額度檔 戶號 = " + tFacMain.getCustNo() + " 額度編號 =  " + tFacMain.getFacmNo()); // 該筆資料待放行中
		}

		if (!tFacMain.getAmortizedCode().equals("5")) {
			throw new LogicException(titaVo, "E0010", "非按月撥款收息戶)"); // E0010 功能選擇錯誤
		}

		if (tFacMain.getLastBormNo() == 0) {
			throw new LogicException(titaVo, "E0010", "首撥請使用L3100撥款)"); // E0010 功能選擇錯誤
		}

		// 第一筆撥款
		tLoanBorMain = loanBorMainService.findById(new LoanBorMainId(iCustNo, iFacmNo, 001), titaVo);

		// 戶況 = 0.正常戶
		if (tLoanBorMain == null || tLoanBorMain.getStatus() != 0) {
			throw new LogicException(titaVo, "E0010", "戶況有誤"); // E0010 功能選擇錯誤
		}

		// 正常撥款,入帳日 >= 下次應繳日
		// 補撥款不檢查
		if (tFacMain.getLastBormNo() > tLoanBorMain.getPaidTerms()) {
			if (iEntryDate < tLoanBorMain.getNextPayIntDate()) {
				throw new LogicException(titaVo, "E0010", "不可提前撥款，下次應繳日=" + tLoanBorMain.getNextPayIntDate()); // E0010
																												// 功能選擇錯誤
			}
		}

		// 撥款金額
		// 契約終止或停止依約撥款不再撥款，利息全數掛帳
		// 已撥款序號 < 總期數 繼續撥款
		// 撥款金額為0 撥款序號給0
		if (tFacMain.getLastBormNo() < tLoanBorMain.getTotalPeriod()) {
//			oDuePayAmt = tFacMain.getDuePayAmt();
			OBormNo = tFacMain.getLastBormNo() + 1;

		} else {
			this.info("不撥款");
			oDuePayAmt = BigDecimal.ZERO;
			OBormNo = 0;
		}

		RepayAmtRoutine();

		// 掛帳利息
		// 1. 撥款金額為0 = 利息金額全部掛帳
		// 2. 利息金額 - 應繳利息上限金額
//		if (oDuePayAmt.equals(BigDecimal.ZERO)) {
//			this.info("撥款金額為0");
//			oOpenInterest = oInterest;
//		} else if (oInterest.compareTo(tFacMain.getPayIntLimit()) > 0) {
//			this.info("撥款金額不為0");
//			oOpenInterest = oInterest.subtract(tFacMain.getPayIntLimit());
//		}

		// 查詢各項費用
		baTxCom.settingUnPaid(iEntryDate, iCustNo, iFacmNo, 0, 0, BigDecimal.ZERO, titaVo); // 00-費用全部(已到期)

		// 累計撥款金額
//		oLoanBal = oLoanBal.add(tFacMain.getDuePayAmt());

		// 實撥金額 = 撥款金額 + 掛帳利息 - 利息金額
		oRealPayAmt = oDuePayAmt.add(oOpenInterest).subtract(oInterest);
		BankRemit tBankRemit = bankRemitService.findBormNoFirst(t2LoanBorMain.getCustNo(), t2LoanBorMain.getFacmNo(),
				t2LoanBorMain.getBormNo(), titaVo);

		// 取利息,掛帳利息給小數點
		String dfoInterest = df.format(oInterest);
		String dfoOpenInterest = df.format(oOpenInterest);

		// 利息不為0,附言塞利息
		// 掛帳不為0 附言塞掛帳
		if (!oInterest.equals(BigDecimal.ZERO)) {
			ORemk = "利息：" + dfoInterest;
			if (!oOpenInterest.equals(BigDecimal.ZERO))
				ORemk = ORemk + "（掛帳：" + dfoOpenInterest + "）";
		}

		// 契約終止代碼 0.正常 1.契約終止未結案戶 2.停止依約撥款戶
//		this.totaVo.putParam("L3r14CancelCode", tFacMain.getCancelCode()); // 契約終止代碼
		this.totaVo.putParam("L3r14BormNo", OBormNo); // 撥款序號
		this.totaVo.putParam("L3r14CurrencyCode", tLoanBorMain.getCurrencyCode()); // 幣別
		this.totaVo.putParam("L3r14PaidTerms", tLoanBorMain.getPaidTerms() + 2); // 本期期別
		this.totaVo.putParam("L3r14TotalPeriod", tLoanBorMain.getTotalPeriod()); // 總期數
		this.totaVo.putParam("L3r14DuePayAmt", oDuePayAmt); // 撥款金額
		this.totaVo.putParam("L3r14SumLoanBal", oLoanBal); // 累計撥款金額
		this.totaVo.putParam("L3r14Interest", oInterest); // 利息金額
		this.totaVo.putParam("L3r14OpenInterest", oOpenInterest); // 掛帳利息
		this.totaVo.putParam("L3r14RealPayAmt", oRealPayAmt); // 實撥金額
		if (tBankRemit != null) {
			// 取銀行中文
			CdBank tCdBank = new CdBank();
			tCdBank = cdBankService.findById(new CdBankId(tBankRemit.getRemitBank(), tBankRemit.getRemitBranch()),
					titaVo);
			if (tCdBank == null) {
				tCdBank = new CdBank();
			}
			this.totaVo.putParam("L3r14RemitBank", tBankRemit.getRemitBank()); // 匯款銀行
			this.totaVo.putParam("L3r14RemitBranch", tBankRemit.getRemitBranch()); // 匯款分行
			this.totaVo.putParam("L3r14RemitAcctNo", tBankRemit.getRemitAcctNo()); // 匯款帳號
			this.totaVo.putParam("L3r14RemitBankItem", tCdBank.getBankItem() + tCdBank.getBranchItem()); // 匯款銀行匯款分行中文
		} else {
			this.totaVo.putParam("L3r14RemitBank", ""); // 匯款銀行
			this.totaVo.putParam("L3r14RemitBranch", ""); // 匯款分行
			this.totaVo.putParam("L3r14RemitAcctNo", 0); // 匯款帳號
			this.totaVo.putParam("L3r14RemitBankItem", ""); // 匯款銀行匯款分行中文
		}
		this.totaVo.putParam("L3r14CompensateAcct", t2LoanBorMain.getCompensateAcct()); // 戶名
		this.totaVo.putParam("L3r14Remark", ORemk); // 附言

		this.addList(this.totaVo);
		return this.sendList();
	}

	private void RepayAmtRoutine() throws LogicException {
		int wkTerms = 0;
		int wkEndDate = 0;
		int wkTotaCount = 0;

		this.info("RepayAmtRoutine ... ");

		Slice<LoanBorMain> slLoanBorMain = loanBorMainService.bormCustNoEq(iCustNo, iFacmNo, iFacmNo, 1, 900, 0,
				Integer.MAX_VALUE, titaVo);
		lLoanBorMain = slLoanBorMain == null ? null : slLoanBorMain.getContent();
		for (LoanBorMain ln : lLoanBorMain) {
			if (ln.getActFg() == 1) {
				throw new LogicException(titaVo, "E0021",
						"放款主檔 戶號 = " + ln.getCustNo() + " 額度編號 =  " + ln.getFacmNo() + " 撥款序號 = " + ln.getBormNo()); // 該筆資料待放行中
			}

			if (ln.getBormNo() == tFacMain.getLastBormNo())
				t2LoanBorMain = ln;

			if (ln.getNextPayIntDate() > iEntryDate) {
				continue;
			}
			// 計算至入帳日期之應繳期數
			wkTerms = loanCom.getTermNo(2, ln.getFreqBase(), ln.getPayIntFreq(), ln.getSpecificDate(),
					ln.getSpecificDd(), iEntryDate);
			wkEndDate = loanCom.getPayIntEndDate(ln.getFreqBase(), ln.getPayIntFreq(), ln.getSpecificDate(),
					ln.getSpecificDd(), wkTerms, ln.getMaturityDate());
			wkTerms -= ln.getPaidTerms();
			if (wkTerms > 0) {
				loanCalcRepayIntCom = loanSetRepayIntCom.setRepayInt(ln, 0, wkEndDate, 2, iEntryDate, titaVo);
				loanCalcRepayIntCom.getRepayInt(titaVo);
				oLoanBal = oLoanBal.add(ln.getLoanBal());
				oInterest = oInterest.add(loanCalcRepayIntCom.getInterest());
				oRepayAmt = oRepayAmt.add(oPrincipal).add(oInterest);
				wkTotaCount++;
			}

		}
		// 補撥款不檢查可計息放款資料
		if (tFacMain.getLastBormNo() > tLoanBorMain.getPaidTerms()) {
			if (wkTotaCount == 0) {
				throw new LogicException(titaVo, "E3070", "戶號 = " + iCustNo + " 額度編號 = " + iFacmNo); // 查無可計息的放款資料
			}
		}
		this.info("RepayAmtRoutine end ");
	}

}