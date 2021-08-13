package com.st1.itx.trade.L3;

import java.math.BigDecimal;
import java.util.ArrayList;
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
import com.st1.itx.db.domain.LoanOverdue;
import com.st1.itx.db.service.LoanBorMainService;
import com.st1.itx.db.service.LoanOverdueService;
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.common.BaTxCom;
import com.st1.itx.util.common.LoanCalcRepayIntCom;
import com.st1.itx.util.common.LoanCom;
import com.st1.itx.util.common.LoanSetRepayIntCom;
import com.st1.itx.util.common.data.CalcRepayIntVo;
import com.st1.itx.util.date.DateUtil;
import com.st1.itx.util.parse.Parse;

/*
 * Tita
 * TimCustNo=9,7
 * CustId=X,10
 * ApplNo=9,7
 * FacmNo=9,3
 * EntryDate=9,7
 */
/**
 * L3924 催收回復試算
 * 
 * @author iza
 * @version 1.0.0
 */
@Service("L3924")
@Scope("prototype")
public class L3924 extends TradeBuffer {
	// private static final Logger logger = LoggerFactory.getLogger(L3924.class);

	/* DB服務注入 */
	@Autowired
	public LoanOverdueService loanOverdueService;
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
	BaTxCom baTxCom;

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active L3924 ");
		this.totaVo.init(titaVo);
		loanSetRepayIntCom.setTxBuffer(this.txBuffer);

		// 取得輸入資料
		int iCustNo = this.parse.stringToInteger(titaVo.getParam("TimCustNo"));
		int iFacmNo = this.parse.stringToInteger(titaVo.getParam("FacmNo"));
		int iEntryDate = this.parse.stringToInteger(titaVo.getParam("EntryDate"));

		// work area
		ArrayList<CalcRepayIntVo> lCalcRepayIntVo = new ArrayList<CalcRepayIntVo>();
		int wkTerms = 0;
		int wkMons = 0;
		int wkWeeks = 0;
		int wkTotaCount = 0;
		int wkSpecificDate;
		BigDecimal oLoanBal = BigDecimal.ZERO;
		int oIntStartDate = 9991231;
		int oIntEndDate = 0;
		BigDecimal oRate = BigDecimal.ZERO;
		String oCurrencyCode = "";
		String oRemark = "";
		BigDecimal oPrincipal = BigDecimal.ZERO;
		BigDecimal oInterest = BigDecimal.ZERO;
		BigDecimal oDelayInt = BigDecimal.ZERO;
		BigDecimal oBreachAmt = BigDecimal.ZERO;
		BigDecimal oOvduRepaid = BigDecimal.ZERO;
		BigDecimal oOvduReduceAmt = BigDecimal.ZERO;
		LoanBorMain tLoanBorMain = new LoanBorMain();
		List<LoanOverdue> lLoanOverdue = new ArrayList<LoanOverdue>();

		// 查詢各項費用
		baTxCom.settingUnPaid(iEntryDate, iCustNo, iFacmNo, 0, 0, BigDecimal.ZERO, titaVo); // 00-費用全部(已到期)
		// 設定第幾分頁 titaVo.getReturnIndex() 第一次會是0，如果需折返最後會塞值
		this.index = titaVo.getReturnIndex();

		// 設定每筆分頁的資料筆數 預設500筆 總長不可超過六萬
		this.limit = 400; // 288 + 122 * 400 = 49238

		List<Integer> lStatus = new ArrayList<Integer>(); // 1:催收 2:部分轉呆 3:呆帳 4:催收回復
		lStatus.add(1);
		lStatus.add(2);
		Slice<LoanOverdue> slLoanOverdue = loanOverdueService.ovduCustNoRange(iCustNo, iFacmNo, iFacmNo, 1, 900, 1, 999,
				lStatus, this.index, this.limit, titaVo);
		lLoanOverdue = slLoanOverdue == null ? null : slLoanOverdue.getContent();
		if (lLoanOverdue == null || lLoanOverdue.size() == 0) {
			throw new LogicException(titaVo, "E0001", "催收呆帳檔"); // 查詢資料不存在
		}
		for (LoanOverdue od : lLoanOverdue) {
			oRemark = od.getRemark();
			oOvduRepaid = oOvduRepaid.add(od.getOvduAmt()).subtract(od.getOvduBal());
			oOvduReduceAmt = oOvduReduceAmt.add(od.getReduceInt()).add(od.getReduceBreach());
			// oOvduFireFee = oOvduFireFee.add(od.getOvduFireBal());
			// oOvduLawFee = oOvduLawFee.add(od.getOvduLawBal());
			tLoanBorMain = loanBorMainService.findById(new LoanBorMainId(iCustNo, iFacmNo, od.getBormNo()), titaVo);
			if (tLoanBorMain == null) {
				throw new LogicException(titaVo, "E0001",
						"放款主檔 戶號 = " + iCustNo + " 額度編號 =  " + iFacmNo + " 撥款序號 = " + od.getBormNo()); // 查詢資料不存在
			}
			// 計算至入帳日期應繳之期數 - 計算至上次繳息日之期數
			wkTerms = loanCom.getTermNo(2, tLoanBorMain.getFreqBase(), tLoanBorMain.getPayIntFreq(),
					tLoanBorMain.getSpecificDate(), tLoanBorMain.getSpecificDd(), iEntryDate);
			if (tLoanBorMain.getPrevPayIntDate() > 0) {
				wkTerms = wkTerms - loanCom.getTermNo(2, tLoanBorMain.getFreqBase(), tLoanBorMain.getPayIntFreq(),
						tLoanBorMain.getSpecificDate(), tLoanBorMain.getSpecificDd(), tLoanBorMain.getPrevPayIntDate());
			}
			if (wkTerms > 0) {
				loanCalcRepayIntCom = loanSetRepayIntCom.setRepayInt(tLoanBorMain, wkTerms, 0, 0, iEntryDate, titaVo);
				lCalcRepayIntVo = loanCalcRepayIntCom.getRepayInt(titaVo);
				oLoanBal = oLoanBal.add(tLoanBorMain.getLoanBal());
				oRate = tLoanBorMain.getStoreRate();
				oCurrencyCode = tLoanBorMain.getCurrencyCode();
				oPrincipal = oPrincipal.add(loanCalcRepayIntCom.getPrincipal());
				oInterest = oInterest.add(loanCalcRepayIntCom.getInterest());
				oDelayInt = oDelayInt.add(loanCalcRepayIntCom.getDelayInt());
				oBreachAmt = oBreachAmt.add(loanCalcRepayIntCom.getBreachAmt());
			} else {
				oLoanBal = oLoanBal.add(tLoanBorMain.getLoanBal());
				oRate = tLoanBorMain.getStoreRate();
				oCurrencyCode = tLoanBorMain.getCurrencyCode();
				oPrincipal = oPrincipal.add(BigDecimal.ZERO);
				oInterest = oInterest.add(BigDecimal.ZERO);
				oDelayInt = oDelayInt.add(BigDecimal.ZERO);
				oBreachAmt = oBreachAmt.add(BigDecimal.ZERO);
			}

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
				wkTotaCount++;
			}
		}

		this.totaVo.putParam("ORemark", oRemark);
		this.totaVo.putParam("OLoanBal", oLoanBal);
		this.totaVo.putParam("OIntStartDate", oIntStartDate == 9991231 ? 0 : oIntStartDate);
		this.totaVo.putParam("OIntEndDate", oIntEndDate);
		this.totaVo.putParam("ORate", oRate);
		this.totaVo.putParam("OCurrencyCode", oCurrencyCode);
		this.totaVo.putParam("OPrincipal", oPrincipal);
		this.totaVo.putParam("OInterest", oInterest);
		this.totaVo.putParam("ODelayInt", oDelayInt);
		this.totaVo.putParam("OBreachAmt", oBreachAmt);
		this.totaVo.putParam("OOvduRepaid", oOvduRepaid);
		this.totaVo.putParam("OOvduReduceAmt", oOvduReduceAmt);
		this.totaVo.putParam("OShortfall", baTxCom.getShortfall());
		this.totaVo.putParam("OShortfallInt", baTxCom.getShortfallInterest());
		this.totaVo.putParam("OShortfallPrin", baTxCom.getShortfallPrincipal());
		this.totaVo.putParam("OShortCloseBreach", baTxCom.getShortCloseBreach());
		this.totaVo.putParam("OExcessive", baTxCom.getExcessive());
		this.totaVo.putParam("OAcctFee", baTxCom.getAcctFee());
		this.totaVo.putParam("OFireFee", baTxCom.getFireFee());
		this.totaVo.putParam("OModifyFee", baTxCom.getModifyFee());
		this.totaVo.putParam("OOvduFireFee", baTxCom.getCollFireFee());
		this.totaVo.putParam("OLawFee", baTxCom.getLawFee());
		this.totaVo.putParam("OOvduLawFee", baTxCom.getCollLawFee());

		this.addList(this.totaVo);
		return this.sendList();
	}
}