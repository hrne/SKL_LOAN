package com.st1.itx.trade.L1;

import java.util.ArrayList;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.st1.itx.Exception.LogicException;
import com.st1.itx.Exception.DBException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.dataVO.TotaVo;
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.db.domain.CustMain;
import com.st1.itx.db.service.CustMainService;
import com.st1.itx.db.domain.FinReportDebt;
import com.st1.itx.db.domain.FinReportDebtId;
import com.st1.itx.db.service.FinReportDebtService;
import com.st1.itx.db.domain.FinReportProfit;
import com.st1.itx.db.domain.FinReportProfitId;
import com.st1.itx.db.service.FinReportProfitService;
import com.st1.itx.db.domain.FinReportCashFlow;
import com.st1.itx.db.domain.FinReportCashFlowId;
import com.st1.itx.db.service.FinReportCashFlowService;
import com.st1.itx.db.domain.FinReportRate;
import com.st1.itx.db.domain.FinReportRateId;
import com.st1.itx.db.service.FinReportRateService;
import com.st1.itx.db.domain.FinReportQuality;
import com.st1.itx.db.domain.FinReportQualityId;
import com.st1.itx.db.service.FinReportQualityService;
import com.st1.itx.db.domain.FinReportReview;
import com.st1.itx.db.domain.FinReportReviewId;
import com.st1.itx.db.service.FinReportReviewService;
import com.st1.itx.util.data.DataLog;
import com.st1.itx.util.parse.Parse;

@Service("L1107")
@Scope("prototype")
/**
 * 
 * 
 * @author eric chang
 * @version 1.0.0
 */
public class L1107 extends TradeBuffer {
	private static final Logger logger = LoggerFactory.getLogger(L1107.class);

	/* DB服務注入 */
	@Autowired
	public CustMainService custMainService;

	@Autowired
	public FinReportDebtService finReportDebtService;

	@Autowired
	public FinReportProfitService finReportProfitService;

	@Autowired
	public FinReportCashFlowService finReportCashFlowService;

	@Autowired
	public FinReportRateService finReportRateService;

	@Autowired
	public FinReportQualityService finReportQualityService;

	@Autowired
	public FinReportReviewService finReportReviewService;

	/* 轉換工具 */
	@Autowired
	public Parse parse;

	@Autowired
	public DataLog dataLog;

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active L1107 ");
		this.totaVo.init(titaVo);

		String iFunCd = titaVo.getParam("FunCd");
		String iCustId = titaVo.getParam("CustId");
		String iCustUKey = titaVo.getParam("CustUKey");
		String iUKey = titaVo.getParam("UKey");
		int iStartYY = Integer.valueOf(titaVo.getParam("StartYY"));

		if ("1".equals(iFunCd)) { // 新增
			CustMain custMain = custMainService.custIdFirst(iCustId, titaVo);
			if (custMain == null) {
				throw new LogicException("E1003", "客戶資料主檔 : " + iCustId);
			}
			iCustUKey = custMain.getCustUKey();
			iUKey = UUID.randomUUID().toString().toUpperCase().replaceAll("-", "");
			FinReportDebt finReportDebt = finReportDebtService.findCustUKeyYearFirst(iCustUKey, iStartYY, titaVo);
			if (finReportDebt != null) {
				throw new LogicException("E0002", iStartYY + "年度財務報表 ");
			}
			finReportDebt = new FinReportDebt();

			FinReportDebtId finReportDebtId = new FinReportDebtId();
			finReportDebtId.setCustUKey(iCustUKey);
			finReportDebtId.setUKey(iUKey);
			finReportDebt.setFinReportDebtId(finReportDebtId);

			finReportDebt = setFinReportDebt(titaVo, finReportDebt);

			try {
				finReportDebtService.insert(finReportDebt, titaVo);
			} catch (DBException e) {
				throw new LogicException("E0005", "資產負債表(FinReportDebt)");
			}

			mntFinReportReview(titaVo, iCustUKey, iUKey);
			mntFinReportProfit(titaVo, iCustUKey, iUKey);
			mntFinReportCashFlow(titaVo, iCustUKey, iUKey);
			mntFinReportRate(titaVo, iCustUKey, iUKey);
			mntFinReportQuality(titaVo, iCustUKey, iUKey);
		} else if ("2".equals(iFunCd)) { // 修改
			FinReportDebtId finReportDebtId = new FinReportDebtId();
			finReportDebtId.setCustUKey(iCustUKey);
			finReportDebtId.setUKey(iUKey);

			FinReportDebt finReportDebt = finReportDebtService.holdById(finReportDebtId, titaVo);

			if (finReportDebt == null) {
				throw new LogicException("E0003", "資產負債表(FinReportDebt)");
			}

			FinReportDebt finReportDebt2 = (FinReportDebt) dataLog.clone(finReportDebt);

			finReportDebt = setFinReportDebt(titaVo, finReportDebt);

			try {
				finReportDebt = finReportDebtService.update2(finReportDebt, titaVo);

				dataLog.setEnv(titaVo, finReportDebt2, finReportDebt);
				dataLog.exec();

			} catch (DBException e) {
				throw new LogicException("E0007", "資產負債表(FinReportDebt)");
			}

			mntFinReportReview(titaVo, iCustUKey, iUKey);
			mntFinReportProfit(titaVo, iCustUKey, iUKey);
			mntFinReportCashFlow(titaVo, iCustUKey, iUKey);
			mntFinReportRate(titaVo, iCustUKey, iUKey);
			mntFinReportQuality(titaVo, iCustUKey, iUKey);
		} else if ("4".equals(iFunCd)) { // 刪除
			FinReportDebtId finReportDebtId = new FinReportDebtId();
			finReportDebtId.setCustUKey(iCustUKey);
			finReportDebtId.setUKey(iUKey);

			FinReportDebt finReportDebt = finReportDebtService.holdById(finReportDebtId, titaVo);

			if (finReportDebt == null) {
				throw new LogicException("E0004", "資產負債表(FinReportDebt)");
			}

			try {
				finReportDebtService.delete(finReportDebt, titaVo);

			} catch (DBException e) {
				throw new LogicException("E0008", "資產負債表(FinReportDebt)");
			}

			// 現金流量表

			FinReportCashFlowId finReportCashFlowId = new FinReportCashFlowId();
			finReportCashFlowId.setCustUKey(iCustUKey);
			finReportCashFlowId.setUKey(iUKey);

			FinReportCashFlow finReportCashFlow = finReportCashFlowService.holdById(finReportCashFlowId, titaVo);

			if (finReportCashFlow != null) {
				try {
					finReportCashFlowService.delete(finReportCashFlow, titaVo);

				} catch (DBException e) {
					throw new LogicException("E0008", "現金流量表(FinReportCashFlow)");
				}
			}

			// 財務比率表

			FinReportRateId finReportRateId = new FinReportRateId();
			finReportRateId.setCustUKey(iCustUKey);
			finReportRateId.setUKey(iUKey);

			FinReportRate finReportRate = finReportRateService.holdById(finReportRateId, titaVo);

			if (finReportRate != null) {
				try {
					finReportRateService.delete(finReportRate, titaVo);

				} catch (DBException e) {
					throw new LogicException("E0008", "財報品質(FinReportQuality)");
				}
			}

			// 財報品質

			FinReportQualityId finReportQualityId = new FinReportQualityId();
			finReportQualityId.setCustUKey(iCustUKey);
			finReportQualityId.setUKey(iUKey);

			FinReportQuality finReportQuality = finReportQualityService.holdById(finReportQualityId, titaVo);

			if (finReportQuality != null) {
				try {
					finReportQualityService.delete(finReportQuality, titaVo);

				} catch (DBException e) {
					throw new LogicException("E0008", "財報品質(FinReportQuality)");
				}
			}

			// 覆審比率

			FinReportReviewId finReportReviewId = new FinReportReviewId();
			finReportReviewId.setCustUKey(iCustUKey);
			finReportReviewId.setUKey(iUKey);

			FinReportReview finReportReview = finReportReviewService.holdById(finReportReviewId, titaVo);

			if (finReportReview != null) {
				try {
					finReportReviewService.delete(finReportReview, titaVo);

				} catch (DBException e) {
					throw new LogicException("E0008", "財報品質(FinReportReview)");
				}
			}
		}

		this.addList(this.totaVo);
		return this.sendList();
	}

	private void mntFinReportReview(TitaVo titaVo, String custUKey, String uKey) throws LogicException {
		boolean exist = false;

		FinReportReview finReportReview2 = new FinReportReview();

		FinReportReviewId finReportReviewId = new FinReportReviewId();
		finReportReviewId.setCustUKey(custUKey);
		finReportReviewId.setUKey(uKey);

		FinReportReview finReportReview = finReportReviewService.holdById(finReportReviewId, titaVo);

		if (finReportReview == null) {
			finReportReview = new FinReportReview();
			finReportReview.setFinReportReviewId(finReportReviewId);
		} else {
			exist = true;
			finReportReview2 = (FinReportReview) dataLog.clone(finReportReview);
		}

		finReportReview.setCurrentAsset(parse.stringToBigDecimal(titaVo.getParam("ReviewCurrentAsset")));
		finReportReview.setTotalAsset(parse.stringToBigDecimal(titaVo.getParam("ReviewTotalAsset")));
		finReportReview.setPropertyAsset(parse.stringToBigDecimal(titaVo.getParam("ReviewPropertyAsset")));
		finReportReview.setInvestment(parse.stringToBigDecimal(titaVo.getParam("ReviewInvestment")));
		finReportReview.setInvestmentProperty(parse.stringToBigDecimal(titaVo.getParam("ReviewInvestmentProperty")));
		finReportReview.setDepreciation(parse.stringToBigDecimal(titaVo.getParam("ReviewDepreciation")));
		finReportReview.setCurrentDebt(parse.stringToBigDecimal(titaVo.getParam("ReviewCurrentDebt")));
		finReportReview.setTotalDebt(parse.stringToBigDecimal(titaVo.getParam("ReviewTotalDebt")));
		finReportReview.setTotalEquity(parse.stringToBigDecimal(titaVo.getParam("ReviewTotalEquity")));
		finReportReview.setBondsPayable(parse.stringToBigDecimal(titaVo.getParam("ReviewBondsPayable")));
		finReportReview.setLongTermBorrowings(parse.stringToBigDecimal(titaVo.getParam("ReviewLongTermBorrowings")));
		finReportReview.setNonCurrentLease(parse.stringToBigDecimal(titaVo.getParam("ReviewNonCurrentLease")));
		finReportReview.setLongTermPayable(parse.stringToBigDecimal(titaVo.getParam("ReviewLongTermPayable")));
		finReportReview.setPreference(parse.stringToBigDecimal(titaVo.getParam("ReviewPreference")));
		finReportReview.setOperatingRevenue(parse.stringToBigDecimal(titaVo.getParam("ReviewOperatingRevenue")));
		finReportReview.setInterestExpense(parse.stringToBigDecimal(titaVo.getParam("ReviewInterestExpense")));
		finReportReview.setProfitBeforeTax(parse.stringToBigDecimal(titaVo.getParam("ReviewProfitBeforeTax")));
		finReportReview.setProfitAfterTax(parse.stringToBigDecimal(titaVo.getParam("ReviewProfitAfterTax")));
		finReportReview.setWorkingCapitalRatio(parse.stringToBigDecimal(titaVo.getParam("ReviewWorkingCapitalRatio")));
		finReportReview
				.setInterestCoverageRatio1(parse.stringToBigDecimal(titaVo.getParam("ReviewInterestCoverageRatio1")));
		finReportReview
				.setInterestCoverageRatio2(parse.stringToBigDecimal(titaVo.getParam("ReviewInterestCoverageRatio2")));
		finReportReview.setLeverageRatio(parse.stringToBigDecimal(titaVo.getParam("ReviewLeverageRatio")));
		finReportReview.setEquityRatio(parse.stringToBigDecimal(titaVo.getParam("ReviewEquityRatio")));
		finReportReview.setLongFitRatio(parse.stringToBigDecimal(titaVo.getParam("ReviewLongFitRatio")));
		finReportReview.setLongFitRatio(parse.stringToBigDecimal(titaVo.getParam("ReviewLongFitRatio")));
		finReportReview.setNetProfitRatio(parse.stringToBigDecimal(titaVo.getParam("ReviewNetProfitRatio")));

		if (exist) {
			try {
				finReportReview = finReportReviewService.update2(finReportReview, titaVo);

				dataLog.setEnv(titaVo, finReportReview2, finReportReview);
				dataLog.exec();

			} catch (DBException e) {
				throw new LogicException("E0007", "財報品質(FinReportReview)");
			}
		} else {
			try {
				finReportReviewService.insert(finReportReview, titaVo);
			} catch (DBException e) {
				throw new LogicException("E0005", "財報品質(FinReportReview)");
			}
		}
	}

	private void mntFinReportQuality(TitaVo titaVo, String custUKey, String uKey) throws LogicException {
		boolean exist = false;

		FinReportQuality finReportQuality2 = new FinReportQuality();

		FinReportQualityId finReportQualityId = new FinReportQualityId();
		finReportQualityId.setCustUKey(custUKey);
		finReportQualityId.setUKey(uKey);

		FinReportQuality finReportQuality = finReportQualityService.holdById(finReportQualityId, titaVo);

		if (finReportQuality == null) {
			finReportQuality = new FinReportQuality();
			finReportQuality.setFinReportQualityId(finReportQualityId);
		} else {
			exist = true;
			finReportQuality2 = (FinReportQuality) dataLog.clone(finReportQuality);
		}

		finReportQuality.setReportType(titaVo.getParam("ReportType"));
		finReportQuality.setOpinion(titaVo.getParam("Opinion"));
		finReportQuality.setIsCheck(titaVo.getParam("IsCheck"));
		finReportQuality.setIsChange(titaVo.getParam("IsChange"));
		finReportQuality.setOfficeType(titaVo.getParam("OfficeType"));
		finReportQuality.setPunishRecord(titaVo.getParam("PunishRecord"));
		finReportQuality.setChangeReason(titaVo.getParam("ChangeReason"));

		if (exist) {
			try {
				finReportQuality = finReportQualityService.update2(finReportQuality, titaVo);

				dataLog.setEnv(titaVo, finReportQuality2, finReportQuality);
				dataLog.exec();

			} catch (DBException e) {
				throw new LogicException("E0007", "財報品質(FinReportQuality)");
			}
		} else {
			try {
				finReportQualityService.insert(finReportQuality, titaVo);
			} catch (DBException e) {
				throw new LogicException("E0005", "財報品質(FinReportQuality)");
			}
		}
	}

	private void mntFinReportRate(TitaVo titaVo, String custUKey, String uKey) throws LogicException {
		boolean exist = false;

		FinReportRate finReportRate2 = new FinReportRate();

		FinReportRateId finReportRateId = new FinReportRateId();
		finReportRateId.setCustUKey(custUKey);
		finReportRateId.setUKey(uKey);

		FinReportRate finReportRate = finReportRateService.holdById(finReportRateId, titaVo);

		if (finReportRate == null) {
			finReportRate = new FinReportRate();
			finReportRate.setFinReportRateId(finReportRateId);
		} else {
			exist = true;
			finReportRate2 = (FinReportRate) dataLog.clone(finReportRate);
		}

		finReportRate.setFlow(parse.stringToBigDecimal(titaVo.getParam("Flow")));
		finReportRate.setSpeed(parse.stringToBigDecimal(titaVo.getParam("Speed")));
		finReportRate.setRateGuar(parse.stringToBigDecimal(titaVo.getParam("RateGuar")));
		finReportRate.setDebt(parse.stringToBigDecimal(titaVo.getParam("Debt")));
		finReportRate.setNet(parse.stringToBigDecimal(titaVo.getParam("Net")));
		finReportRate.setCashFlow(parse.stringToBigDecimal(titaVo.getParam("CashFlow")));
		finReportRate.setFixLong(parse.stringToBigDecimal(titaVo.getParam("FixLong")));
		finReportRate.setFinSpend(parse.stringToBigDecimal(titaVo.getParam("FinSpend")));
		finReportRate.setGrossProfit(parse.stringToBigDecimal(titaVo.getParam("GrossProfit")));
		finReportRate.setAfterTaxNet(parse.stringToBigDecimal(titaVo.getParam("AfterTaxNet")));
		finReportRate.setNetReward(parse.stringToBigDecimal(titaVo.getParam("NetReward")));
		finReportRate.setTotalAssetReward(parse.stringToBigDecimal(titaVo.getParam("TotalAssetReward")));
		finReportRate.setStock(parse.stringToBigDecimal(titaVo.getParam("RateStock")));
		finReportRate.setReceiveAccount(parse.stringToBigDecimal(titaVo.getParam("RateReceiveAccount")));
		finReportRate.setTotalAsset(parse.stringToBigDecimal(titaVo.getParam("TotalAsset")));
		finReportRate.setPayAccount(parse.stringToBigDecimal(titaVo.getParam("RatePayAccount")));
		finReportRate.setAveTotalAsset(parse.stringToBigDecimal(titaVo.getParam("AveTotalAsset")));
		finReportRate.setAveNetBusCycle(parse.stringToBigDecimal(titaVo.getParam("AveNetBusCycle")));
		finReportRate.setFinLever(parse.stringToBigDecimal(titaVo.getParam("FinLever")));
		finReportRate.setLoanDebtNet(parse.stringToBigDecimal(titaVo.getParam("LoanDebtNet")));
		finReportRate.setBusRate(parse.stringToBigDecimal(titaVo.getParam("BusRate")));
		finReportRate.setPayFinLever(parse.stringToBigDecimal(titaVo.getParam("PayFinLever")));
		finReportRate.setADE(parse.stringToBigDecimal(titaVo.getParam("ADE")));
		finReportRate.setCashGuar(parse.stringToBigDecimal(titaVo.getParam("CashGuar")));

		if (exist) {
			try {
				finReportRate = finReportRateService.update2(finReportRate, titaVo);

				dataLog.setEnv(titaVo, finReportRate2, finReportRate);
				dataLog.exec();

			} catch (DBException e) {
				throw new LogicException("E0007", "財務比率表(FinReportCashFlow)");
			}
		} else {
			try {
				finReportRateService.insert(finReportRate, titaVo);
			} catch (DBException e) {
				throw new LogicException("E0005", "財務比率表(FinReportCashFlow)");
			}
		}
	}

	private void mntFinReportCashFlow(TitaVo titaVo, String custUKey, String uKey) throws LogicException {
		boolean exist = false;

		FinReportCashFlow finReportCashFlow2 = new FinReportCashFlow();

		FinReportCashFlowId finReportCashFlowId = new FinReportCashFlowId();
		finReportCashFlowId.setCustUKey(custUKey);
		finReportCashFlowId.setUKey(uKey);

		FinReportCashFlow finReportCashFlow = finReportCashFlowService.holdById(finReportCashFlowId, titaVo);

		if (finReportCashFlow == null) {
			finReportCashFlow = new FinReportCashFlow();
			finReportCashFlow.setFinReportCashFlowId(finReportCashFlowId);
		} else {
			exist = true;
			finReportCashFlow2 = (FinReportCashFlow) dataLog.clone(finReportCashFlow);
		}

		finReportCashFlow.setBusCash(parse.stringToBigDecimal(titaVo.getParam("BusCash")));
		finReportCashFlow.setInvestCash(parse.stringToBigDecimal(titaVo.getParam("InvestCash")));
		finReportCashFlow.setFinCash(parse.stringToBigDecimal(titaVo.getParam("FinCash")));
		finReportCashFlow.setAccountItem01(titaVo.getParam("CashAccountItem01"));
		finReportCashFlow.setAccountItem02(titaVo.getParam("CashAccountItem02"));
		finReportCashFlow.setAccountValue01(parse.stringToBigDecimal(titaVo.getParam("CashAccountValue01")));
		finReportCashFlow.setAccountValue02(parse.stringToBigDecimal(titaVo.getParam("CashAccountValue02")));
		finReportCashFlow.setEndCash(parse.stringToBigDecimal(titaVo.getParam("EndCash")));

		if (exist) {
			try {
				finReportCashFlow = finReportCashFlowService.update2(finReportCashFlow, titaVo);

				dataLog.setEnv(titaVo, finReportCashFlow2, finReportCashFlow);
				dataLog.exec();

			} catch (DBException e) {
				throw new LogicException("E0007", "現金流量表(FinReportCashFlow)");
			}
		} else {
			try {
				finReportCashFlowService.insert(finReportCashFlow, titaVo);
			} catch (DBException e) {
				throw new LogicException("E0005", "現金流量表(FinReportCashFlow)");
			}
		}
	}

	private void mntFinReportProfit(TitaVo titaVo, String custUKey, String uKey) throws LogicException {
		boolean exist = false;

		FinReportProfit finReportProfit2 = new FinReportProfit();

		FinReportProfitId finReportProfitId = new FinReportProfitId();
		finReportProfitId.setCustUKey(custUKey);
		finReportProfitId.setUKey(uKey);

		FinReportProfit finReportProfit = finReportProfitService.holdById(finReportProfitId, titaVo);
		if (finReportProfit == null) {
			finReportProfit = new FinReportProfit();
			finReportProfit.setFinReportProfitId(finReportProfitId);
		} else {
			exist = true;
			finReportProfit2 = (FinReportProfit) dataLog.clone(finReportProfit);
		}

		finReportProfit.setBusIncome(parse.stringToBigDecimal(titaVo.getParam("BusIncome")));
		finReportProfit.setGrowRate(parse.stringToBigDecimal(titaVo.getParam("GrowRate")));
		finReportProfit.setBusCost(parse.stringToBigDecimal(titaVo.getParam("BusCost")));
		finReportProfit.setBusGrossProfit(parse.stringToBigDecimal(titaVo.getParam("BusGrossProfit")));
		finReportProfit.setManageFee(parse.stringToBigDecimal(titaVo.getParam("ManageFee")));
		finReportProfit.setBusLossProfit(parse.stringToBigDecimal(titaVo.getParam("BusLossProfit")));
		finReportProfit.setBusOtherIncome(parse.stringToBigDecimal(titaVo.getParam("BusOtherIncome")));
		finReportProfit.setInterest(parse.stringToBigDecimal(titaVo.getParam("Interest")));
		finReportProfit.setBusOtherFee(parse.stringToBigDecimal(titaVo.getParam("BusOtherFee")));
		finReportProfit.setBeforeTaxNet(parse.stringToBigDecimal(titaVo.getParam("BeforeTaxNet")));
		finReportProfit.setBusTax(parse.stringToBigDecimal(titaVo.getParam("BusTax")));
		finReportProfit.setHomeLossProfit(parse.stringToBigDecimal(titaVo.getParam("HomeLossProfit")));
		finReportProfit.setOtherComLossProfit(parse.stringToBigDecimal(titaVo.getParam("OtherComLossProfit")));
		finReportProfit.setHomeComLossProfit(parse.stringToBigDecimal(titaVo.getParam("HomeComLossProfit")));
		finReportProfit.setUncontrolRight(parse.stringToBigDecimal(titaVo.getParam("UncontrolRight")));
		finReportProfit.setParentCompanyRight(parse.stringToBigDecimal(titaVo.getParam("ParentCompanyRight")));
		finReportProfit.setEPS(parse.stringToBigDecimal(titaVo.getParam("EPS")));

		if (exist) {
			try {
				finReportProfit = finReportProfitService.update2(finReportProfit, titaVo);

				dataLog.setEnv(titaVo, finReportProfit2, finReportProfit);
				dataLog.exec();

			} catch (DBException e) {
				throw new LogicException("E0007", "損益表(FinReportProfit)");
			}
		} else {
			try {
				finReportProfitService.insert(finReportProfit, titaVo);
			} catch (DBException e) {
				throw new LogicException("E0005", "損益表(FinReportProfit)");
			}
		}

	}

	private FinReportDebt setFinReportDebt(TitaVo titaVo, FinReportDebt finReportDebt) throws LogicException {
		finReportDebt.setStartYY(Integer.valueOf(titaVo.getParam("StartYY")) + 1911);
		finReportDebt.setStartMM(Integer.valueOf(titaVo.getParam("StartMM")));
		finReportDebt.setEndYY(Integer.valueOf(titaVo.getParam("EndYY")) + 1911);
		finReportDebt.setEndMM(Integer.valueOf(titaVo.getParam("EndMM")));
		finReportDebt.setAssetTotal(parse.stringToBigDecimal(titaVo.getParam("AssetTotal")));
		finReportDebt.setFlowAsset(parse.stringToBigDecimal(titaVo.getParam("FlowAsset")));
		finReportDebt.setCash(parse.stringToBigDecimal(titaVo.getParam("Cash")));
		finReportDebt.setFinAsset(parse.stringToBigDecimal(titaVo.getParam("FinAsset")));
		finReportDebt.setReceiveTicket(parse.stringToBigDecimal(titaVo.getParam("ReceiveTicket")));
		finReportDebt.setReceiveAccount(parse.stringToBigDecimal(titaVo.getParam("ReceiveAccount")));
		finReportDebt.setReceiveRelation(parse.stringToBigDecimal(titaVo.getParam("ReceiveRelation")));
		finReportDebt.setOtherReceive(parse.stringToBigDecimal(titaVo.getParam("OtherReceive")));
		finReportDebt.setStock(parse.stringToBigDecimal(titaVo.getParam("Stock")));
		finReportDebt.setPrepayItem(parse.stringToBigDecimal(titaVo.getParam("PrepayItem")));
		finReportDebt.setOtherFlowAsset(parse.stringToBigDecimal(titaVo.getParam("OtherFlowAsset")));
		finReportDebt.setAccountItem01(titaVo.getParam("AccountItem01"));
		finReportDebt.setAccountItem02(titaVo.getParam("AccountItem02"));
		finReportDebt.setAccountItem03(titaVo.getParam("AccountItem03"));
		finReportDebt.setAccountValue01(parse.stringToBigDecimal(titaVo.getParam("AccountValue01")));
		finReportDebt.setAccountValue02(parse.stringToBigDecimal(titaVo.getParam("AccountValue02")));
		finReportDebt.setAccountValue03(parse.stringToBigDecimal(titaVo.getParam("AccountValue03")));
		finReportDebt.setLongInvest(parse.stringToBigDecimal(titaVo.getParam("LongInvest")));
		finReportDebt.setFixedAsset(parse.stringToBigDecimal(titaVo.getParam("FixedAsset")));
		finReportDebt.setLand(parse.stringToBigDecimal(titaVo.getParam("Land")));
		finReportDebt.setHouseBuild(parse.stringToBigDecimal(titaVo.getParam("HouseBuild")));
		finReportDebt.setMachineEquip(parse.stringToBigDecimal(titaVo.getParam("MachineEquip")));
		finReportDebt.setOtherEquip(parse.stringToBigDecimal(titaVo.getParam("OtherEquip")));
		finReportDebt.setPrepayEquip(parse.stringToBigDecimal(titaVo.getParam("PrepayEquip")));
		finReportDebt.setUnFinish(parse.stringToBigDecimal(titaVo.getParam("UnFinish")));
		finReportDebt.setDepreciation(parse.stringToBigDecimal(titaVo.getParam("Depreciation")));
		finReportDebt.setInvisibleAsset(parse.stringToBigDecimal(titaVo.getParam("InvisibleAsset")));
		finReportDebt.setOtherAsset(parse.stringToBigDecimal(titaVo.getParam("OtherAsset")));
		finReportDebt.setAccountItem04(titaVo.getParam("AccountItem04"));
		finReportDebt.setAccountItem05(titaVo.getParam("AccountItem05"));
		finReportDebt.setAccountItem06(titaVo.getParam("AccountItem06"));
		finReportDebt.setAccountValue04(parse.stringToBigDecimal(titaVo.getParam("AccountValue04")));
		finReportDebt.setAccountValue05(parse.stringToBigDecimal(titaVo.getParam("AccountValue05")));
		finReportDebt.setAccountValue06(parse.stringToBigDecimal(titaVo.getParam("AccountValue06")));
		finReportDebt.setDebtNetTotal(parse.stringToBigDecimal(titaVo.getParam("DebtNetTotal")));
		finReportDebt.setFlowDebt(parse.stringToBigDecimal(titaVo.getParam("FlowDebt")));
		finReportDebt.setShortLoan(parse.stringToBigDecimal(titaVo.getParam("ShortLoan")));
		finReportDebt.setPayShortTicket(parse.stringToBigDecimal(titaVo.getParam("PayShortTicket")));
		finReportDebt.setPayTicket(parse.stringToBigDecimal(titaVo.getParam("PayTicket")));
		finReportDebt.setPayAccount(parse.stringToBigDecimal(titaVo.getParam("PayAccount")));
		finReportDebt.setPayRelation(parse.stringToBigDecimal(titaVo.getParam("PayRelation")));
		finReportDebt.setOtherPay(parse.stringToBigDecimal(titaVo.getParam("OtherPay")));
		finReportDebt.setPreReceiveItem(parse.stringToBigDecimal(titaVo.getParam("PreReceiveItem")));
		finReportDebt.setLongDebtOneYear(parse.stringToBigDecimal(titaVo.getParam("LongDebtOneYear")));
		finReportDebt.setShareholder(parse.stringToBigDecimal(titaVo.getParam("Shareholder")));
		finReportDebt.setOtherFlowDebt(parse.stringToBigDecimal(titaVo.getParam("OtherFlowDebt")));
		finReportDebt.setAccountItem07(titaVo.getParam("AccountItem07"));
		finReportDebt.setAccountItem08(titaVo.getParam("AccountItem08"));
		finReportDebt.setAccountItem09(titaVo.getParam("AccountItem09"));
		finReportDebt.setAccountValue07(parse.stringToBigDecimal(titaVo.getParam("AccountValue07")));
		finReportDebt.setAccountValue08(parse.stringToBigDecimal(titaVo.getParam("AccountValue08")));
		finReportDebt.setAccountValue09(parse.stringToBigDecimal(titaVo.getParam("AccountValue09")));
		finReportDebt.setLongDebt(parse.stringToBigDecimal(titaVo.getParam("LongDebt")));
		finReportDebt.setOtherDebt(parse.stringToBigDecimal(titaVo.getParam("OtherDebt")));
		finReportDebt.setDebtTotal(parse.stringToBigDecimal(titaVo.getParam("DebtTotal")));
		finReportDebt.setNetValue(parse.stringToBigDecimal(titaVo.getParam("NetValue")));
		finReportDebt.setCapital(parse.stringToBigDecimal(titaVo.getParam("Capital")));
		finReportDebt.setCapitalSurplus(parse.stringToBigDecimal(titaVo.getParam("CapitalSurplus")));
		finReportDebt.setRetainProfit(parse.stringToBigDecimal(titaVo.getParam("RetainProfit")));
		finReportDebt.setOtherRight(parse.stringToBigDecimal(titaVo.getParam("OtherRight")));
		finReportDebt.setTreasuryStock(parse.stringToBigDecimal(titaVo.getParam("TreasuryStock")));
		finReportDebt.setUnControlRight(parse.stringToBigDecimal(titaVo.getParam("UnControlRight")));
		finReportDebt.setAccountItem10(titaVo.getParam("AccountItem10"));
		finReportDebt.setAccountItem11(titaVo.getParam("AccountItem11"));
		finReportDebt.setAccountValue10(parse.stringToBigDecimal(titaVo.getParam("AccountValue10")));
		finReportDebt.setAccountValue11(parse.stringToBigDecimal(titaVo.getParam("AccountValue11")));

		return finReportDebt;
	}
}