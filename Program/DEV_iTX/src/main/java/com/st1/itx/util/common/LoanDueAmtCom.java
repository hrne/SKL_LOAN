package com.st1.itx.util.common;

import java.math.BigDecimal;
import java.math.RoundingMode;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.tradeService.CommBuffer;

/**
 * 計算期金<BR>
 * 1 getDueAmt 計算期金 call by LXXXX<BR>
 * 1.1 寬限期內，依總期數與寬限期數計算 1.2.超過寬限期，依剩餘期數(寬限期數=0)計算
 * 
 * @author st1
 *
 */
@Component("loanDueAmtCom")
@Scope("prototype")
public class LoanDueAmtCom extends CommBuffer {

	/**
	 * 計算期金
	 * 
	 * @param iPrincipal     本金餘額
	 * @param iRate          利率
	 * @param iAmortizedCode 攤還方式
	 * @param iFreqBase      週期基準
	 * @param iLoanTerm      總期數(寬限期內)、剩餘期數(超過寬限期)
	 * @param iGracePeriod   寬限期數(寬限期內)、0(超過寬限期)
	 * @param iPayIntFreq    繳息週期
	 * @param iFinalBal      最後一期本金餘額
	 * @param titaVo         TitaVo
	 * @return 期金
	 * @throws LogicException
	 */
	public BigDecimal getDueAmt(BigDecimal iPrincipal, BigDecimal iRate, String iAmortizedCode, int iFreqBase,
			int iLoanTerm, int iGracePeriod, int iPayIntFreq, BigDecimal iFinalBal, TitaVo titaVo)
			throws LogicException {
		this.info("DueAmtCom.getDueAmt ... ");
		this.info("   iPrincipal     = " + iPrincipal);
		this.info("   iRate          = " + iRate);
		this.info("   iAmortizedCode = " + iAmortizedCode);
		this.info("   iFreqBase      = " + iFreqBase);
		this.info("   iLoanTerm      = " + iLoanTerm);
		this.info("   iGracePeriod   = " + iGracePeriod);
		this.info("   iPayIntFreq    = " + iPayIntFreq);
		this.info("   iFinalBal      = " + iFinalBal);

		BigDecimal oDueAmt = new BigDecimal(0);
		BigDecimal wkFreqBaseConstant;
		// 週期基準 1:日 2:月 3:週
		if (iFreqBase == 3) {
			wkFreqBaseConstant = new BigDecimal(5200);
		} else {
			wkFreqBaseConstant = new BigDecimal(1200);
		}
		switch (iAmortizedCode) {
		case "1": // 按月繳息
			oDueAmt = new BigDecimal(0);
			break;
		case "2": // 到期取息
			oDueAmt = new BigDecimal(0);
			break;
		case "3": // 本息平均法
			BigDecimal wkRate;
			BigDecimal wkRateA;
			BigDecimal wkRateB;
			BigDecimal wkRateC;
			BigDecimal wkPrinciple;
			BigDecimal wkFinalInterest;
			int wkTerm;

			wkTerm = iLoanTerm - iGracePeriod;
			// 償還本金=放款餘額 - 最後一期本金餘額
			wkPrinciple = iPrincipal.subtract(iFinalBal);
			if (iRate.compareTo(BigDecimal.ZERO) == 0) {
				throw new LogicException(titaVo, "E0010", "LoanDueAmtCom 利率 = 0 "); // 功能選擇錯誤
			}
			// 月利率 = 年利率÷1200(換算為每期利率)
			wkRate = iRate.divide(wkFreqBaseConstant, 15, RoundingMode.HALF_UP).multiply(new BigDecimal(iPayIntFreq))
					.setScale(15, RoundingMode.HALF_UP);
			// [(1＋月利率)^月數]
			wkRateA = wkRate.add(new BigDecimal(1)).pow(wkTerm).setScale(15, RoundingMode.HALF_UP);

			// [(1＋月利率)^月數]－1
			wkRateB = wkRateA.subtract(new BigDecimal(1)).setScale(15, RoundingMode.HALF_UP);

			// 每月應付本息之平均攤還率={[(1＋月利率)^月數]×月利率}÷{[(1＋月利率)^月數]－1}
			wkRateC = wkRate.multiply(wkRateA).divide(wkRateB, 15, RoundingMode.HALF_UP);

			// 最後一期本金餘額的每月利息=最後一期本金餘額×月利率
			wkFinalInterest = iFinalBal.multiply(wkRate).setScale(0, RoundingMode.HALF_UP);

			// 每期攤還金額 = 償還本金×每月應付本息之平均攤還率＋ 最後一期本金餘額的每月利息
			oDueAmt = wkPrinciple.multiply(wkRateC).add(wkFinalInterest).setScale(0, RoundingMode.HALF_UP);
			this.info(
					"   wkRate= " + wkRate + ", wkRateA=" + wkRateA + ", wkRateB=" + wkRateB + ", wkRateC=" + wkRateC);
			this.info("   FinalInterest = " + wkFinalInterest + ", DueAmt=" + oDueAmt);

			break;
		case "4": // 本金平均法
			BigDecimal wkPeriod = new BigDecimal(iLoanTerm - iGracePeriod);
			oDueAmt = iPrincipal.subtract(iFinalBal).divide(wkPeriod, 15, RoundingMode.HALF_UP).setScale(0,
					RoundingMode.HALF_UP);
			break;
		default:
			throw new LogicException(titaVo, "E0010", "LoanDueAmtCom 攤還方式 = " + iAmortizedCode); // 功能選擇錯誤
		}

		this.info("DueAmtCom.getDueAmt end oDueAmt = " + oDueAmt);
		return oDueAmt;
	}

	
	/**
	 * 計算還本期數
	 * @param iPrincipal     本金餘額
	 * @param iRate          利率
	 * @param iAmortizedCode 攤還方式
	 * @param iFreqBase      週期基準
	 * @param iPayIntFreq    繳息週期
	 * @param iFinalBal      最後一期本金餘額
	 * @param iDueAmt        期金
	 * @param titaVo          TitaVo
	 * @return 還本期數
	 * @throws LogicException
	 */
	public int getDueTerms(BigDecimal iPrincipal, BigDecimal iRate, String iAmortizedCode, int iFreqBase,
			int iPayIntFreq, BigDecimal iFinalBal, BigDecimal iDueAmt, TitaVo titaVo) throws LogicException {
		this.info("DueAmtCom.getTotalPeriod ... ");
		this.info("   iPrincipal     = " + iPrincipal);
		this.info("   iRate          = " + iRate);
		this.info("   iAmortizedCode = " + iAmortizedCode);
		this.info("   iFreqBase      = " + iFreqBase);
		this.info("   iPayIntFreq    = " + iPayIntFreq);
		this.info("   iDueAmt        = " + iDueAmt);

		BigDecimal wkFreqBaseConstant;
		int oTerms = 0;
		// 週期基準 1:日 2:月 3:週
		if (iFreqBase == 3) {
			wkFreqBaseConstant = new BigDecimal(5200);
		} else {
			wkFreqBaseConstant = new BigDecimal(1200);
		}
		BigDecimal wkLoanBal = iPrincipal.subtract(iFinalBal);
		switch (iAmortizedCode) {
		case "1": // 按月繳息
			oTerms = 1;
			break;
		case "2": // 到期取息
			oTerms = 1;
			break;
		case "3": // 本息平均法
			// 第一次的利息需小於期金
			// 本息攤還金額 = 每期攤還金額 - 最後一期本金餘額的每月利息
			// 月利率 = 年利率÷1200(換算為每期利率)
			BigDecimal wkRate = iRate.divide(wkFreqBaseConstant, 15, RoundingMode.HALF_UP)
					.multiply(new BigDecimal(iPayIntFreq)).setScale(15, RoundingMode.HALF_UP);
			BigDecimal wkFinalInterest = iFinalBal.multiply(wkRate).setScale(0, RoundingMode.HALF_UP);
			BigDecimal wkDueAmt = iDueAmt.subtract(wkFinalInterest);
			if (iPrincipal.multiply(wkRate).compareTo(wkDueAmt) >= 0) {
				throw new LogicException(titaVo, "E0019", "期金需大於利息"); // 輸入資料錯誤
			}
			while (wkLoanBal.compareTo(BigDecimal.ZERO) > 0) {
				wkLoanBal = wkLoanBal
						.subtract(wkDueAmt.subtract(wkLoanBal.multiply(wkRate).setScale(0, RoundingMode.HALF_UP)));
				oTerms++;
			}
			break;
		case "4": // 本金平均法
			while (wkLoanBal.compareTo(BigDecimal.ZERO) > 0) {
				wkLoanBal = wkLoanBal.subtract(iDueAmt);
				oTerms++;
			}
			break;
		default:
			throw new LogicException(titaVo, "E0010", "LoanDueAmtCom 攤還方式 = " + iAmortizedCode); // 功能選擇錯誤
		}

		this.info("DueAmtCom.getTotalPeriod end TotalPeriod = " + oTerms);
		return oTerms;
	}

	@Override
	public void exec() throws LogicException {
		// TODO Auto-generated method stub

	}

}
