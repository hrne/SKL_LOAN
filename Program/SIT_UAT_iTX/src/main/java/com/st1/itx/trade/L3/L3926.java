package com.st1.itx.trade.L3;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
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
	private static final Logger logger = LoggerFactory.getLogger(L3926.class);

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

		// work area
		int oIntStartDate = 9991231;
		int oIntEndDate = 0;
		int oLeftTerms = 0;
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
		LoanBorMain tLoanBorMain = new LoanBorMain();
		ArrayList<CalcRepayIntVo> lCalcRepayIntVo = new ArrayList<CalcRepayIntVo>();

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

		if (tLoanBorMain.getPrevPayIntDate() >= iEntryDate || tLoanBorMain.getDrawdownDate() == iEntryDate) {
			throw new LogicException(titaVo, "E3064", " 上次繳息日 = " + tLoanBorMain.getPrevPayIntDate()); // 該筆放款應繳日尚未到
		}
		// 查詢各項費用
		baTxCom.settingUnPaid(iEntryDate, iCustNo, iFacmNo, 0, 0, BigDecimal.ZERO, titaVo); // 00-費用全部(已到期)
		
		// 計算至入帳日應繳之期數
		int wkTermNo = loanCom.getTermNo(2, tLoanBorMain.getFreqBase(), tLoanBorMain.getPayIntFreq(),
				tLoanBorMain.getSpecificDate(), tLoanBorMain.getSpecificDd(), iEntryDate);
		oLeftTerms = tLoanBorMain.getTotalPeriod() - wkTermNo;
		oLoanBal =tLoanBorMain.getLoanBal();
		wkStoreRate = tLoanBorMain.getStoreRate();
		// 計算至上次繳息日之期數
		if (tLoanBorMain.getPrevPayIntDate() > 0) {
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
			if (iNewDueAmt.compareTo(wkFinalInterest) <= 0) {
				throw new LogicException(titaVo, "E3067", " 最後本金餘額的利息 = " + wkFinalInterest); // 因該筆放款有最後本金餘額，新攤還金額不足以繳息
			}
			wkNewLoanBal = iNewDueAmt.subtract(wkFinalInterest).divide(wkRateC, 15, RoundingMode.HALF_UP).setScale(0,
					RoundingMode.HALF_UP);
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
		this.totaVo.putParam("OLeftTerms", oLeftTerms);

		this.addList(this.totaVo);
		return this.sendList();
	}
}