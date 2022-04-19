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
	 * @throws LogicException Exception
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
			BigDecimal wkRate = BigDecimal.ZERO;
			BigDecimal wkRateA = BigDecimal.ZERO;
			BigDecimal wkRateB = BigDecimal.ZERO;
			BigDecimal wkRateC = BigDecimal.ZERO;
			BigDecimal wkPrinciple = BigDecimal.ZERO;
			BigDecimal wkFinalInterest = BigDecimal.ZERO;
			int wkTerm = 0;

			wkTerm = iLoanTerm - iGracePeriod;
			// 償還本金=放款餘額 - 最後一期本金餘額
			wkPrinciple = iPrincipal.subtract(iFinalBal);
			if (iRate.compareTo(BigDecimal.ZERO) == 0) {
				BigDecimal wkPeriod = new BigDecimal(iLoanTerm - iGracePeriod);
				oDueAmt = iPrincipal.subtract(iFinalBal).divide(wkPeriod, 15, RoundingMode.HALF_UP).setScale(0,
						RoundingMode.HALF_UP);
			} else if (wkTerm == 0) {
				oDueAmt = iPrincipal;
			} else {
				// 月利率 = 年利率÷1200(換算為每期利率)
				wkRate = iRate.divide(wkFreqBaseConstant, 15, RoundingMode.HALF_UP)
						.multiply(new BigDecimal(iPayIntFreq)).setScale(15, RoundingMode.HALF_UP);
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

//				// 2022-04-08 智偉新增 照AS400計算
//				// LA850 69行 CHANGE FIELD(#WK1PR) TO('1 + (#IRTRAT * #LMSISC / 1200)')
//				// 註:#WK1PR DECIMAL(11,9)
//				BigDecimal wk1pr = new BigDecimal("1").add(
//						iRate.multiply(new BigDecimal(iPayIntFreq).divide(wkFreqBaseConstant, 9, RoundingMode.DOWN))
//								.setScale(9, RoundingMode.DOWN))
//						.setScale(9, RoundingMode.DOWN);
//				;
//
//				// LA850 70行 CHANGE FIELD(#WKPXR) TO('#LMSLBL * #IRTRAT * #LMSISC / 1200)')
//				// 註:#WKPXR DECIMAL(11,9)
//				BigDecimal wkpxr = wkPrinciple.multiply(iRate).multiply(new BigDecimal(iPayIntFreq))
//						.divide(wkFreqBaseConstant, 9, RoundingMode.DOWN);
//
//				// LA850 71行 CHANGE FIELD(#LMSTPRWK #WK1RN) TO(1)
//				BigDecimal lmstprwk = new BigDecimal("1");
//				BigDecimal wk1rn = new BigDecimal("1");
//
//				// LA850 72行 DOWHILE COND('#LMSTPRWK *LE #LMSTPRWK2')
//				// LA850 73行 CHANGE FIELD(#WK1RN) TO('#WK1RN * #WK1PR')
//				// LA850 74行 CHANGE FIELD(#LMSTPRWK) TO('#LMSTPRWK + 1')
//				// LA850 75行 ENDWHILE
//				BigDecimal lmstprwk2 = new BigDecimal(wkTerm);
//				this.info("lmstprwk = " + lmstprwk);
//				this.info("lmstprwk2 = " + lmstprwk2);
//				this.info("wk1rn = " + wk1rn);
//				this.info("wk1pr = " + wk1pr);
//				while (lmstprwk.compareTo(lmstprwk2) <= 0) {
//					wk1rn = wk1rn.multiply(wk1pr).setScale(9, RoundingMode.DOWN);
//					lmstprwk = lmstprwk.add(new BigDecimal("1"));
//				}
//
//				// LA850 77行 CHANGE FIELD(#LMSPPA) TO('(#WKPXR * #WK1RN) / (#WK1RN - 1)')
//				// ROUND_UP(*YES)
//				BigDecimal lmsppa = wkpxr.multiply(wk1rn)
//						.divide(wk1rn.subtract(new BigDecimal("1")), 9, RoundingMode.DOWN)
//						.setScale(0, RoundingMode.HALF_UP);
//				this.info("lmsppa四捨五入前 = "
//						+ wkpxr.multiply(wk1rn).divide(wk1rn.subtract(new BigDecimal("1")), 9, RoundingMode.DOWN));
//				this.info("oDueAmt = " + oDueAmt);
//				this.info("lmsppa = " + lmsppa);
//				oDueAmt = lmsppa;
			}
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
	 * 
	 * @param iPrincipal     本金餘額
	 * @param iRate          利率
	 * @param iAmortizedCode 攤還方式
	 * @param iFreqBase      週期基準
	 * @param iPayIntFreq    繳息週期
	 * @param iFinalBal      最後一期本金餘額
	 * @param iDueAmt        期金
	 * @param titaVo         TitaVo
	 * @return 還本期數
	 * @throws LogicException Exception
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
