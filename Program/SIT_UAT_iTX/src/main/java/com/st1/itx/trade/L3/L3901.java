package com.st1.itx.trade.L3;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.OccursList;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.dataVO.TotaVo;
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.common.LoanDueAmtCom;
import com.st1.itx.util.parse.Parse;

/*
 * L3901 貸款試算
 * a.此功能供客戶洽詢時,輸入假設之條件(例如:本金,利率,期數等),由電腦試算出每期應攤還之本金,利息及放款餘額.
 * b.繳息週期位輸入時,內定值為1月
 */
/*
 * Tita
 * Principal=9,14
 * Rate=9,2.4
 * AmortizedCode=9,1
 * FreqBase=9,1
 * LoanTerm=9,3
 * GracePeriod=9,3
 * PayIntFreq=9,2
 * FinalBal=9,14
 */
/**
 * L3901 貸款試算
 * 
 * @author iza
 * @version 1.0.0
 */
@Service("L3901")
@Scope("prototype")
public class L3901 extends TradeBuffer {

	@Autowired
	Parse parse;
	@Autowired
	LoanDueAmtCom loanDueAmtCom;

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active L3901 ");
		this.totaVo.init(titaVo);

		// 取得輸入資料
		BigDecimal iPrincipal = this.parse.stringToBigDecimal(titaVo.getParam("Principal"));
		BigDecimal iRate = this.parse.stringToBigDecimal(titaVo.getParam("Rate1"));
		int iAmortizedCode = this.parse.stringToInteger(titaVo.getParam("AmortizedCode"));
		int iFreqBase = this.parse.stringToInteger(titaVo.getParam("FreqBase"));
		int iLoanTerm = this.parse.stringToInteger(titaVo.getParam("LoanTerm"));
		int iGracePeriod = this.parse.stringToInteger(titaVo.getParam("GracePeriod"));
		int iPayIntFreq = this.parse.stringToInteger(titaVo.getParam("PayIntFreq"));
		BigDecimal iFinalBal = this.parse.stringToBigDecimal(titaVo.getParam("FinalBal"));

		// 檢查輸入資料
		if (!(iAmortizedCode >= 1 && iAmortizedCode <= 4)) {
			throw new LogicException(titaVo, "E2004", "攤還方式"); // 功能選擇錯誤
		}

		// 設定第幾分頁 titaVo.getReturnIndex() 第一次會是0，如果需折返最後會塞值
		// sFacCaseApplService.setPageIndex(titaVo.getReturnIndex());

		// 設定每筆分頁的資料筆數 預設500筆 總長不可超過六萬
		// sFacCaseApplService.setPageLimit(1000); // 45 * 1000 = 45000
		BigDecimal wkInterest = BigDecimal.ZERO;
		BigDecimal wkPrincipal = BigDecimal.ZERO;
		BigDecimal wkRepay = BigDecimal.ZERO;
		BigDecimal wkBalance = iPrincipal;
		// BigDecimal wkPeriod = new BigDecimal(iLoanTerm - iGracePeriod);
		BigDecimal wkFreqBaseConstant;
		BigDecimal wkRate = BigDecimal.ZERO;
		int wkTerm = 0;

		OccursList occursList;
		if (iFreqBase == 1) {
			wkFreqBaseConstant = new BigDecimal(36500);
		} else if (iFreqBase == 3) {
			wkFreqBaseConstant = new BigDecimal(5200);
		} else {
			wkFreqBaseConstant = new BigDecimal(1200);
		}
		switch (iAmortizedCode) {
		case 1: // 按月繳息
			for (int i = 1; i <= iLoanTerm; i++) {
				occursList = new OccursList();
				iRate = getInterestRate(i, titaVo);
				wkInterest = iPrincipal.multiply(iRate).divide(wkFreqBaseConstant, 5, RoundingMode.HALF_UP)
						.multiply(new BigDecimal(iPayIntFreq)).setScale(0, RoundingMode.HALF_UP);
				occursList.putParam("OLoanTerm", i);
				occursList.putParam("OInterest", wkInterest);
				if (i == iLoanTerm) {
					occursList.putParam("OPrincipal", iPrincipal);
					occursList.putParam("OBalance", 0);
				} else {
					occursList.putParam("OPrincipal", 0);
					occursList.putParam("OBalance", iPrincipal);
				}
				// 將每筆資料放入Tota的OcList
				this.totaVo.addOccursList(occursList);
			}
			break;
		case 2: // 到期取息
			for (int i = 1; i <= 3; i++) {
				if (parse.stringToInteger(titaVo.getParam("STerm" + i)) != 0) {
					iRate = parse.stringToBigDecimal(titaVo.getParam("Rate" + i));
					wkTerm = parse.stringToInteger(titaVo.getParam("ETerm" + i))
							- parse.stringToInteger(titaVo.getParam("STerm" + i)) + 1;
					wkInterest = wkInterest
							.add(iPrincipal.multiply(iRate).divide(wkFreqBaseConstant, 5, RoundingMode.HALF_UP)
									.multiply(new BigDecimal(wkTerm)).setScale(0, RoundingMode.HALF_UP));
				}
			}
			occursList = new OccursList();
			occursList.putParam("OLoanTerm", 1);
			occursList.putParam("OInterest", wkInterest);
			occursList.putParam("OPrincipal", iPrincipal);
			occursList.putParam("OBalance", 0);
			this.totaVo.addOccursList(occursList);
			break;
		case 3: // 本息平均法
			for (int i = 1; i <= iLoanTerm; i++) {
				occursList = new OccursList();
				iRate = getInterestRate(i, titaVo);
				// 寬限期內=>總期數與寬限期數； 超過寬限期=>剩餘期數、寬限期數=0
				if (iRate.compareTo(wkRate) != 0) {
					wkRepay = loanDueAmtCom.getDueAmt(wkBalance, iRate, "3", iFreqBase,
							i <= iGracePeriod ? iLoanTerm : iLoanTerm - i + 1, i <= iGracePeriod ? iGracePeriod : 0,
							iPayIntFreq, iFinalBal, titaVo);
					wkRate = iRate;
				}
				wkInterest = wkBalance.multiply(iRate).divide(wkFreqBaseConstant, 5, RoundingMode.HALF_UP)
						.multiply(new BigDecimal(iPayIntFreq)).setScale(0, RoundingMode.HALF_UP);
				occursList.putParam("OLoanTerm", i);
				occursList.putParam("OInterest", wkInterest);
				if (i == iLoanTerm) {
					occursList.putParam("OPrincipal", wkBalance.subtract(iFinalBal));
					occursList.putParam("OBalance", iFinalBal);
				} else {
					if (i > iGracePeriod) {
						occursList.putParam("OPrincipal", wkRepay.subtract(wkInterest));
						wkBalance = wkBalance.subtract(wkRepay.subtract(wkInterest));
						occursList.putParam("OBalance", wkBalance);
					} else {
						occursList.putParam("OPrincipal", 0);
						occursList.putParam("OBalance", wkBalance);
					}
				}
				// 將每筆資料放入Tota的OcList
				this.totaVo.addOccursList(occursList);
			}
			break;
		case 4: // 本金平均法
			wkPrincipal = loanDueAmtCom.getDueAmt(iPrincipal, BigDecimal.ZERO, "4", iFreqBase, iLoanTerm, iGracePeriod,
					iPayIntFreq, iFinalBal, titaVo);
			for (int i = 1; i <= iLoanTerm; i++) {
				occursList = new OccursList();
				iRate = getInterestRate(i, titaVo);
				wkInterest = wkBalance.multiply(iRate).divide(wkFreqBaseConstant, 15, RoundingMode.HALF_UP)
						.multiply(new BigDecimal(iPayIntFreq)).setScale(0, RoundingMode.HALF_UP);
				occursList.putParam("OLoanTerm", i);
				occursList.putParam("OInterest", wkInterest);
				if (i == iLoanTerm) {
					occursList.putParam("OPrincipal", wkBalance.subtract(iFinalBal));
					occursList.putParam("OBalance", iFinalBal);
				} else {
					if (i > iGracePeriod) {
						occursList.putParam("OPrincipal", wkPrincipal);
						wkBalance = wkBalance.subtract(wkPrincipal);
						occursList.putParam("OBalance", wkBalance);
					} else {
						occursList.putParam("OPrincipal", 0);
						occursList.putParam("OBalance", wkBalance);
					}
				}
				// 將每筆資料放入Tota的OcList
				this.totaVo.addOccursList(occursList);
			}
			break;
		}

		this.addList(this.totaVo);
		return this.sendList();
	}

	private BigDecimal getInterestRate(int termNo, TitaVo titaVo) throws LogicException {
		BigDecimal wkRate = BigDecimal.ZERO;
		for (int i = 1; i <= 3; i++) {
			if (termNo >= parse.stringToInteger(titaVo.getParam("STerm" + i))
					&& termNo <= parse.stringToInteger(titaVo.getParam("ETerm" + i))) {
				wkRate = parse.stringToBigDecimal(titaVo.getParam("Rate" + i));
			}
		}
		return wkRate;
	}
}