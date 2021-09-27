package com.st1.itx.trade.L5;

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
import com.st1.itx.util.common.NegCom;
import com.st1.itx.util.parse.Parse;

/*
 * L5970 貸款試算
 *     a.此功能供客戶洽詢時,輸入假設之條件(例如:本金,利率,期數等),由電腦試算出每期應攤還之本金,利息及放款餘額.
 *     b.繳息週期位輸入時,內定值為1月
 */
/**
 * Tita<br>
 * Principal=9,14.2<br>
 * Rate=9,2.4<br>
 * LoanTerm=9,2<br>
 * DueAmt=9,14.2<br>
 */

@Service("L5970")
@Scope("prototype")
/**
 * 
 * 
 * @author Jacky
 * @version 1.0.0
 */
public class L5970 extends TradeBuffer {
	@Autowired
	Parse parse;

	@Autowired
	NegCom sNegCom;

	@Autowired
	LoanDueAmtCom LoanDueAmtCom;

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active L5970 ");
		this.totaVo.init(titaVo);
		BigDecimal iPrincipal = this.parse.stringToBigDecimal(titaVo.getParam("Principal").trim());// 本金
		BigDecimal iRate = this.parse.stringToBigDecimal(titaVo.getParam("Rate").trim());// 利率(年)
		int iLoanTerm = this.parse.stringToInteger(titaVo.getParam("LoanTerm").trim());// 期數
		BigDecimal iDueAmt = this.parse.stringToBigDecimal(titaVo.getParam("DueAmt").trim());// 期金

		if (iPrincipal.compareTo(BigDecimal.ZERO) <= 0) {
			throw new LogicException(titaVo, "", "本金請大於零"); //
		}

		this.info("L5970 計算前 本金(iPrincipal)=[" + iPrincipal + "] 年利率(iRate)=[" + iRate + "] 期數(iLoanTerm)=[" + iLoanTerm + "] 期金(iDueAmt)=[" + iDueAmt + "]");

		if (iLoanTerm == 0 && iDueAmt.compareTo(BigDecimal.ZERO) > 0) {
			// 沒期數,有期金
			// =(ROUNDUP(NPER(利率,-1*期金,本金),0))
			iLoanTerm = sNegCom.nper(iPrincipal, iDueAmt, iRate);
			this.info("L5970 計算後 本金(iPrincipal)=[" + iPrincipal + "] 年利率(iRate)=[" + iRate + "] 期數(iLoanTerm)=[" + iLoanTerm + "] 期金(iDueAmt)=[" + iDueAmt + "]");
			BigDecimal OBalance = iPrincipal;// 剩餘本金
			for (int i = 1; i <= iLoanTerm; i++) {
				if (OBalance.compareTo(BigDecimal.ZERO) <= 0) {
					break;
				}
				OccursList occursList = new OccursList();
				occursList.putParam("OLoanTerm", i);// 繳款期數
				// iDueAmt 期金
				BigDecimal OInterest = (OBalance.multiply(iRate)).divide(new BigDecimal(1200), 0, RoundingMode.HALF_UP);// 應繳利息
				BigDecimal OPrincipal = BigDecimal.ZERO;// 應繳本金

				if (OBalance.compareTo(iDueAmt) <= 0) {
					OPrincipal = OBalance;
				} else {
					OPrincipal = iDueAmt.subtract(OInterest);// 應繳本金
				}
				BigDecimal Osum = OInterest.add(OPrincipal);// 本利合計
				if (Osum.compareTo(iDueAmt) < 0) {
					OPrincipal = OBalance;
					OBalance = BigDecimal.ZERO;
					Osum = OPrincipal.add(OInterest);
				} else {
					OBalance = OBalance.subtract(OPrincipal);
				}

				if (OPrincipal.add(OInterest).compareTo(iDueAmt) != 0) {

				}
				occursList.putParam("OPrincipal", OPrincipal);// 應繳本金
				occursList.putParam("OInterest", OInterest);// 應繳利息
				occursList.putParam("OBalance", OBalance);// 剩餘本金
				this.totaVo.addOccursList(occursList);

			}
		} else if (iLoanTerm > 0 && iDueAmt.compareTo(BigDecimal.ZERO) == 0) {
			// 參考L3901

			// 有期數,沒期金
			// =ROUNDUP(-PMT(月利率,期數,本金),0)
			// iDueAmt=(sNegCom.pmt(iRate, iLoanTerm, iPrincipal, BigDecimal.ZERO,
			// true).multiply(new BigDecimal("-1"))).setScale(0,RoundingMode.UP);
			// int iFreqBase =
			// this.parse.stringToInteger(titaVo.getParam("FreqBase"));//週期基準
			// int iGracePeriod =
			// this.parse.stringToInteger(titaVo.getParam("GracePeriod"));//寬限期
			// int iPayIntFreq =
			// this.parse.stringToInteger(titaVo.getParam("PayIntFreq"));//繳息周期
			// BigDecimal iFinalBal =
			// this.parse.stringToBigDecimal(titaVo.getParam("FinalBal"));//最後一期餘額
			int iFreqBase = 2;// 週期基準 1:日 2:月 3:週
			int iGracePeriod = 0;// 寬限期
			int iPayIntFreq = 1;// 繳息周期
			BigDecimal iFinalBal = BigDecimal.ZERO;// 最後一期餘額
			// 設定每筆分頁的資料筆數 預設500筆 總長不可超過六萬
			// sFacCaseApplService.setPageLimit(1000); // 45 * 1000 = 45000
			BigDecimal wkInterest;
			BigDecimal wkRepay;
			BigDecimal wkBalance = iPrincipal;
			// BigDecimal wkPeriod = new BigDecimal(iLoanTerm - iGracePeriod);
			BigDecimal wkFreqBaseConstant;
			OccursList occursList;
			if (iFreqBase == 3) {
				wkFreqBaseConstant = new BigDecimal(5200);
			} else {
				wkFreqBaseConstant = new BigDecimal(1200);
			}

			// 本息平均法
			// wkRepay = calcAmortize(iPrincipal, iFinalBal, iRate, wkFreqBaseConstant,
			// iPayIntFreq, iLoanTerm,
			// iGracePeriod);
			if (iRate.compareTo(BigDecimal.ZERO) == 0) {//利率為0時不使用本息平均法,改為[本金除以期數]
				wkRepay = iPrincipal.divide(new BigDecimal(iLoanTerm), 0, RoundingMode.HALF_UP);
			} else {
				wkRepay = LoanDueAmtCom.getDueAmt(iPrincipal, iRate, "3", iFreqBase, iLoanTerm, iGracePeriod,
						iPayIntFreq, iFinalBal, titaVo);
			}
			for (int i = 1; i <= iLoanTerm; i++) {
				occursList = new OccursList();
				wkInterest = wkBalance.multiply(iRate).divide(wkFreqBaseConstant, 5, RoundingMode.HALF_UP).multiply(new BigDecimal(iPayIntFreq)).setScale(0, RoundingMode.HALF_UP);
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
		} else {
			if (iLoanTerm == 0 && iDueAmt.compareTo(BigDecimal.ZERO) == 0) {
				// E5002 繳款期數/繳納期款，擇一輸入
				throw new LogicException(titaVo, "E5002", ""); //
			} else {
				throw new LogicException(titaVo, "", "發生未預測到的錯誤"); //
			}
		}

		/*
		 * occursList = new OccursList(); occursList.putParam("OLoanTerm", 1);//繳款期數
		 * occursList.putParam("OPrincipal", iPrincipal);//應繳本金
		 * occursList.putParam("OInterest", wkInterest);//應繳利息
		 * occursList.putParam("OSum", 0);//本利和 occursList.putParam("OBalance",
		 * 0);//剩餘本金 this.totaVo.addOccursList(occursList);
		 */

		this.addList(this.totaVo);
		return this.sendList();
	}
}