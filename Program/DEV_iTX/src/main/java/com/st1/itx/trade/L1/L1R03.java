package com.st1.itx.trade.L1;

import java.math.BigDecimal;
import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.dataVO.TotaVo;
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
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.parse.Parse;

@Service("L1R03")
@Scope("prototype")
/**
 * 
 * 
 * @author YuJiaXing
 * @version 1.0.0
 */
public class L1R03 extends TradeBuffer {

	/* DB服務注入 */
	@Autowired
	public CustMainService custMainService;

	/* DB服務注入 */
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
	public Parse iParse;

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active L1R03 ");
		this.totaVo.init(titaVo);

		String iFunCd = titaVo.getParam("FunCd");
//		String iCustId = titaVo.getParam("CustId");
		String iCustUKey = titaVo.getParam("CustUKey");
		String iUKey = titaVo.getParam("UKey");

		// 取統編先確定是否已在客戶資料主檔測試

		CustMain tCustMain = custMainService.findById(iCustUKey, titaVo);

		if (tCustMain == null) {
			throw new LogicException(titaVo, "E0001", "客戶資料主檔 ");
		}

		// 取得客戶識別碼
		String custUKey = tCustMain.getCustUKey();

		// 資產負債表
		FinReportDebtId finReportDebtId = new FinReportDebtId();
		finReportDebtId.setCustUKey(iCustUKey);
		finReportDebtId.setUKey(iUKey);

		FinReportDebt finReportDebt = finReportDebtService.findById(finReportDebtId, titaVo);

		if (finReportDebt == null) {
			throw new LogicException("E0001", "財務報表.資產負債表");
		}

		this.totaVo.putParam("CustId", tCustMain.getCustId());
		this.totaVo.putParam("CustName", tCustMain.getCustName());
		this.totaVo.putParam("StartYY", finReportDebt.getStartYY() - 1911);
		this.totaVo.putParam("StartMM", finReportDebt.getStartMM());
		this.totaVo.putParam("EndYY", finReportDebt.getEndYY() - 1911);
		this.totaVo.putParam("EndMM", finReportDebt.getEndMM());
		this.totaVo.putParam("AssetTotal", finReportDebt.getAssetTotal());
		this.totaVo.putParam("FlowAsset", finReportDebt.getFlowAsset());
		this.totaVo.putParam("Cash", finReportDebt.getCash());
		this.totaVo.putParam("FinAsset", finReportDebt.getFinAsset());
		this.totaVo.putParam("ReceiveTicket", finReportDebt.getReceiveTicket());
		this.totaVo.putParam("ReceiveAccount", finReportDebt.getReceiveAccount());
		this.totaVo.putParam("ReceiveRelation", finReportDebt.getReceiveRelation());
		this.totaVo.putParam("OtherReceive", finReportDebt.getOtherReceive());
		this.totaVo.putParam("Stock", finReportDebt.getStock());
		this.totaVo.putParam("PrepayItem", finReportDebt.getPrepayItem());
		this.totaVo.putParam("OtherFlowAsset", finReportDebt.getOtherFlowAsset());
		this.totaVo.putParam("AccountItem01", finReportDebt.getAccountItem01());
		this.totaVo.putParam("AccountItem02", finReportDebt.getAccountItem02());
		this.totaVo.putParam("AccountItem03", finReportDebt.getAccountItem03());
		this.totaVo.putParam("AccountValue01", finReportDebt.getAccountValue01());
		this.totaVo.putParam("AccountValue02", finReportDebt.getAccountValue02());
		this.totaVo.putParam("AccountValue03", finReportDebt.getAccountValue03());
		this.totaVo.putParam("LongInvest", finReportDebt.getLongInvest());
		this.totaVo.putParam("FixedAsset", finReportDebt.getFixedAsset());
		this.totaVo.putParam("Land", finReportDebt.getLand());
		this.totaVo.putParam("HouseBuild", finReportDebt.getHouseBuild());
		this.totaVo.putParam("MachineEquip", finReportDebt.getMachineEquip());
		this.totaVo.putParam("OtherEquip", finReportDebt.getOtherEquip());
		this.totaVo.putParam("PrepayEquip", finReportDebt.getPrepayEquip());
		this.totaVo.putParam("UnFinish", finReportDebt.getUnFinish());
		this.totaVo.putParam("Depreciation", finReportDebt.getDepreciation());
		this.totaVo.putParam("InvisibleAsset", finReportDebt.getInvisibleAsset());
		this.totaVo.putParam("OtherAsset", finReportDebt.getOtherAsset());
		this.totaVo.putParam("AccountItem04", finReportDebt.getAccountItem04());
		this.totaVo.putParam("AccountItem05", finReportDebt.getAccountItem05());
		this.totaVo.putParam("AccountItem06", finReportDebt.getAccountItem06());
		this.totaVo.putParam("AccountValue04", finReportDebt.getAccountValue04());
		this.totaVo.putParam("AccountValue05", finReportDebt.getAccountValue05());
		this.totaVo.putParam("AccountValue06", finReportDebt.getAccountValue06());
		this.totaVo.putParam("DebtNetTotal", finReportDebt.getDebtNetTotal());
		this.totaVo.putParam("FlowDebt", finReportDebt.getFlowDebt());
		this.totaVo.putParam("ShortLoan", finReportDebt.getShortLoan());
		this.totaVo.putParam("PayShortTicket", finReportDebt.getPayShortTicket());
		this.totaVo.putParam("PayTicket", finReportDebt.getPayTicket());
		this.totaVo.putParam("PayAccount", finReportDebt.getPayAccount());
		this.totaVo.putParam("PayRelation", finReportDebt.getPayRelation());
		this.totaVo.putParam("OtherPay", finReportDebt.getOtherPay());
		this.totaVo.putParam("PreReceiveItem", finReportDebt.getPreReceiveItem());
		this.totaVo.putParam("LongDebtOneYear", finReportDebt.getLongDebtOneYear());
		this.totaVo.putParam("Shareholder", finReportDebt.getShareholder());
		this.totaVo.putParam("OtherFlowDebt", finReportDebt.getOtherFlowDebt());
		this.totaVo.putParam("AccountItem07", finReportDebt.getAccountItem07());
		this.totaVo.putParam("AccountItem08", finReportDebt.getAccountItem08());
		this.totaVo.putParam("AccountItem09", finReportDebt.getAccountItem09());
		this.totaVo.putParam("AccountValue07", finReportDebt.getAccountValue07());
		this.totaVo.putParam("AccountValue08", finReportDebt.getAccountValue08());
		this.totaVo.putParam("AccountValue09", finReportDebt.getAccountValue09());
		this.totaVo.putParam("LongDebt", finReportDebt.getLongDebt());
		this.totaVo.putParam("OtherDebt", finReportDebt.getOtherDebt());
		this.totaVo.putParam("DebtTotal", finReportDebt.getDebtTotal());
		this.totaVo.putParam("NetValue", finReportDebt.getNetValue());
		this.totaVo.putParam("Capital", finReportDebt.getCapital());
		this.totaVo.putParam("CapitalSurplus", finReportDebt.getCapitalSurplus());
		this.totaVo.putParam("RetainProfit", finReportDebt.getRetainProfit());
		this.totaVo.putParam("OtherRight", finReportDebt.getOtherRight());
		this.totaVo.putParam("TreasuryStock", finReportDebt.getTreasuryStock());
		this.totaVo.putParam("UnControlRight", finReportDebt.getUnControlRight());
		this.totaVo.putParam("AccountItem10", finReportDebt.getAccountItem10());
		this.totaVo.putParam("AccountItem11", finReportDebt.getAccountItem11());
		this.totaVo.putParam("AccountValue10", finReportDebt.getAccountValue10());
		this.totaVo.putParam("AccountValue11", finReportDebt.getAccountValue11());

		// 損益表
		FinReportProfitId finReportProfitId = new FinReportProfitId();
		finReportProfitId.setCustUKey(iCustUKey);
		finReportProfitId.setUKey(iUKey);

		FinReportProfit finReportProfit = finReportProfitService.findById(finReportProfitId, titaVo);

		if (finReportProfit == null) {
			this.totaVo.putParam("BusIncome", 0);
			this.totaVo.putParam("GrowRate", 0);
			this.totaVo.putParam("BusCost", 0);
			this.totaVo.putParam("BusGrossProfit", 0);
			this.totaVo.putParam("ManageFee", 0);
			this.totaVo.putParam("BusLossProfit", 0);
			this.totaVo.putParam("BusOtherIncome", 0);
			this.totaVo.putParam("Interest", 0);
			this.totaVo.putParam("BusOtherFee", 0);
			this.totaVo.putParam("BeforeTaxNet", 0);
			this.totaVo.putParam("BusTax", 0);
			this.totaVo.putParam("HomeLossProfit", 0);
			this.totaVo.putParam("OtherComLossProfit", 0);
			this.totaVo.putParam("HomeComLossProfit", 0);
			this.totaVo.putParam("UncontrolRight", 0);
			this.totaVo.putParam("ParentCompanyRight", 0);
			this.totaVo.putParam("EPS", 0);
		} else {
			this.info("L1R03 finReportProfit found");

			this.totaVo.putParam("BusIncome", finReportProfit.getBusIncome());

			// this.totaVo.putParam("GrowRate", finReportProfit.getGrowRate());
			int lastYear = finReportDebt.getStartYY() - 1;
			BigDecimal lastBusIncome = new BigDecimal("0");
			FinReportDebt finReportDebt2 = finReportDebtService.findCustUKeyYearFirst(finReportDebt.getCustUKey(), lastYear, titaVo);
			if (finReportDebt2 != null) {
				FinReportProfitId finReportProfitId2 = new FinReportProfitId();
				finReportProfitId2.setCustUKey(finReportDebt2.getCustUKey());
				finReportProfitId2.setUKey(finReportDebt2.getUKey());

				FinReportProfit finReportProfit2 = finReportProfitService.findById(finReportProfitId2, titaVo);
				if (finReportProfit2 != null) {
					lastBusIncome = finReportProfit2.getBusIncome();
				}
			}
			this.info("L1R03 lastBusIncome = " + lastBusIncome);
			this.info("L1R03 nowBusIncome  = " + finReportProfit.getBusIncome());
			BigDecimal growRate = new BigDecimal("0");
			if (lastBusIncome.compareTo(new BigDecimal("0")) != 0) {
				// 成長率算法 (新-舊)/舊
				BigDecimal iHundred = new BigDecimal("100");
				BigDecimal iGap = finReportProfit.getBusIncome().subtract(lastBusIncome);
				growRate = iGap.divide(lastBusIncome).setScale(4, BigDecimal.ROUND_HALF_UP).multiply(iHundred);
			}
			this.info("L1R03 GrowRate  = " + growRate);
			this.totaVo.putParam("GrowRate", growRate);

			this.totaVo.putParam("BusCost", finReportProfit.getBusCost());
			this.totaVo.putParam("BusGrossProfit", finReportProfit.getBusGrossProfit());
			this.totaVo.putParam("ManageFee", finReportProfit.getManageFee());
			this.totaVo.putParam("BusLossProfit", finReportProfit.getBusLossProfit());
			this.totaVo.putParam("BusOtherIncome", finReportProfit.getBusOtherIncome());
			this.totaVo.putParam("Interest", finReportProfit.getInterest());
			this.totaVo.putParam("BusOtherFee", finReportProfit.getBusOtherFee());
			this.totaVo.putParam("BeforeTaxNet", finReportProfit.getBeforeTaxNet());
			this.totaVo.putParam("BusTax", finReportProfit.getBusTax());
			this.totaVo.putParam("HomeLossProfit", finReportProfit.getHomeLossProfit());
			this.totaVo.putParam("OtherComLossProfit", finReportProfit.getOtherComLossProfit());
			this.totaVo.putParam("HomeComLossProfit", finReportProfit.getHomeComLossProfit());
			this.totaVo.putParam("UncontrolRight", finReportProfit.getUncontrolRight());
			this.totaVo.putParam("ParentCompanyRight", finReportProfit.getParentCompanyRight());
			this.totaVo.putParam("EPS", finReportProfit.getEPS());
		}

		// 現金流量表
		FinReportCashFlowId finReportCashFlowId = new FinReportCashFlowId();
		finReportCashFlowId.setCustUKey(iCustUKey);
		finReportCashFlowId.setUKey(iUKey);

		FinReportCashFlow finReportCashFlow = finReportCashFlowService.findById(finReportCashFlowId, titaVo);

		if (finReportCashFlow == null) {
			this.totaVo.putParam("BusCash", 0);
			this.totaVo.putParam("InvestCash", 0);
			this.totaVo.putParam("FinCash", 0);
			this.totaVo.putParam("CashAccountItem01", "");
			this.totaVo.putParam("CashAccountItem02", "");
			this.totaVo.putParam("CashAccountValue01", 0);
			this.totaVo.putParam("CashAccountValue02", 0);
			this.totaVo.putParam("EndCash", 0);
		} else {
			this.totaVo.putParam("BusCash", finReportCashFlow.getBusCash());
			this.totaVo.putParam("InvestCash", finReportCashFlow.getInvestCash());
			this.totaVo.putParam("FinCash", finReportCashFlow.getFinCash());
			this.totaVo.putParam("CashAccountItem01", finReportCashFlow.getAccountItem01());
			this.totaVo.putParam("CashAccountItem02", finReportCashFlow.getAccountItem02());
			this.totaVo.putParam("CashAccountValue01", finReportCashFlow.getAccountValue01());
			this.totaVo.putParam("CashAccountValue02", finReportCashFlow.getAccountValue02());
			this.totaVo.putParam("EndCash", finReportCashFlow.getEndCash());

		}

		// 財務比率表

		FinReportRateId finReportRateId = new FinReportRateId();
		finReportRateId.setCustUKey(iCustUKey);
		finReportRateId.setUKey(iUKey);

		FinReportRate finReportRate = finReportRateService.findById(finReportRateId, titaVo);

		if (finReportRate == null) {
			this.totaVo.putParam("Flow", 0);
			this.totaVo.putParam("Speed", 0);
			this.totaVo.putParam("RateGuar", 0);
			this.totaVo.putParam("Debt", 0);
			this.totaVo.putParam("Net", 0);
			this.totaVo.putParam("CashFlow", 0);
			this.totaVo.putParam("FixLong", 0);
			this.totaVo.putParam("FinSpend", 0);
			this.totaVo.putParam("GrossProfit", 0);
			this.totaVo.putParam("AfterTaxNet", 0);
			this.totaVo.putParam("NetReward", 0);
			this.totaVo.putParam("TotalAssetReward", 0);
			this.totaVo.putParam("RateStock", 0);
			this.totaVo.putParam("RateReceiveAccount", 0);
			this.totaVo.putParam("TotalAsset", 0);
			this.totaVo.putParam("RatePayAccount", 0);
			this.totaVo.putParam("AveTotalAsset", 0);
			this.totaVo.putParam("AveNetBusCycle", 0);
			this.totaVo.putParam("FinLever", 0);
			this.totaVo.putParam("LoanDebtNet", 0);
			this.totaVo.putParam("BusRate", 0);
			this.totaVo.putParam("PayFinLever", 0);
			this.totaVo.putParam("ADE", 0);
			this.totaVo.putParam("CashGuar", 0);

		} else {
			this.totaVo.putParam("Flow", finReportRate.getFlow());
			this.totaVo.putParam("Speed", finReportRate.getSpeed());
			this.totaVo.putParam("RateGuar", finReportRate.getRateGuar());
			this.totaVo.putParam("Debt", finReportRate.getDebt());
			this.totaVo.putParam("Net", finReportRate.getNet());
			this.totaVo.putParam("CashFlow", finReportRate.getCashFlow());
			this.totaVo.putParam("FixLong", finReportRate.getFixLong());
			this.totaVo.putParam("FinSpend", finReportRate.getFinSpend());
			this.totaVo.putParam("GrossProfit", finReportRate.getGrossProfit());
			this.totaVo.putParam("AfterTaxNet", finReportRate.getAfterTaxNet());
			this.totaVo.putParam("NetReward", finReportRate.getNetReward());
			this.totaVo.putParam("TotalAssetReward", finReportRate.getTotalAssetReward());
			this.totaVo.putParam("RateStock", finReportRate.getStock());
			this.totaVo.putParam("RateReceiveAccount", finReportRate.getReceiveAccount());
			this.totaVo.putParam("TotalAsset", finReportRate.getTotalAsset());
			this.totaVo.putParam("RatePayAccount", finReportRate.getPayAccount());
			this.totaVo.putParam("AveTotalAsset", finReportRate.getAveTotalAsset());
			this.totaVo.putParam("AveNetBusCycle", finReportRate.getAveNetBusCycle());
			this.totaVo.putParam("FinLever", finReportRate.getFinLever());
			this.totaVo.putParam("LoanDebtNet", finReportRate.getLoanDebtNet());
			this.totaVo.putParam("BusRate", finReportRate.getBusRate());
			this.totaVo.putParam("PayFinLever", finReportRate.getPayFinLever());
			this.totaVo.putParam("ADE", finReportRate.getADE());
			this.totaVo.putParam("CashGuar", finReportRate.getCashGuar());

		}

		// 財報品質

		FinReportQualityId finReportQualityId = new FinReportQualityId();
		finReportQualityId.setCustUKey(iCustUKey);
		finReportQualityId.setUKey(iUKey);

		FinReportQuality finReportQuality = finReportQualityService.findById(finReportQualityId, titaVo);

		if (finReportQuality == null) {
			this.totaVo.putParam("ReportType", "");
			this.totaVo.putParam("Opinion", "");
			this.totaVo.putParam("IsCheck", "");
			this.totaVo.putParam("IsChange", "");
			this.totaVo.putParam("OfficeType", "");
			this.totaVo.putParam("PunishRecord", "");
			this.totaVo.putParam("ChangeReason", "");
		} else {
			this.totaVo.putParam("ReportType", finReportQuality.getReportType());
			this.totaVo.putParam("Opinion", finReportQuality.getOpinion());
			this.totaVo.putParam("IsCheck", finReportQuality.getIsCheck());
			this.totaVo.putParam("IsChange", finReportQuality.getIsChange());
			this.totaVo.putParam("OfficeType", finReportQuality.getOfficeType());
			this.totaVo.putParam("PunishRecord", finReportQuality.getPunishRecord());
			this.totaVo.putParam("ChangeReason", finReportQuality.getChangeReason());

		}

		// 審比率

		this.info("active L1R03 FinReportReview");

		FinReportReviewId finReportReviewId = new FinReportReviewId();
		finReportReviewId.setCustUKey(iCustUKey);
		finReportReviewId.setUKey(iUKey);

		FinReportReview finReportReview = finReportReviewService.findById(finReportReviewId, titaVo);

		if (finReportReview == null) {
			this.totaVo.putParam("ReviewCurrentAsset", 0);
			this.totaVo.putParam("ReviewTotalAsset", 0);
			this.totaVo.putParam("ReviewPropertyAsset", 0);
			this.totaVo.putParam("ReviewInvestment", 0);
			this.totaVo.putParam("ReviewInvestmentProperty", 0);
			this.totaVo.putParam("ReviewDepreciation", 0);
			this.totaVo.putParam("ReviewCurrentDebt", 0);
			this.totaVo.putParam("ReviewTotalDebt", 0);
			this.totaVo.putParam("ReviewTotalEquity", 0);
			this.totaVo.putParam("ReviewBondsPayable", 0);
			this.totaVo.putParam("ReviewLongTermBorrowings", 0);
			this.totaVo.putParam("ReviewNonCurrentLease", 0);
			this.totaVo.putParam("ReviewLongTermPayable", 0);
			this.totaVo.putParam("ReviewPreference", 0);
			this.totaVo.putParam("ReviewOperatingRevenue", 0);
			this.totaVo.putParam("ReviewInterestExpense", 0);
			this.totaVo.putParam("ReviewProfitBeforeTax", 0);
			this.totaVo.putParam("ReviewProfitAfterTax", 0);
			this.totaVo.putParam("ReviewWorkingCapitalRatio", 0);
			this.totaVo.putParam("ReviewInterestCoverageRatio1", 0);
			this.totaVo.putParam("ReviewInterestCoverageRatio2", 0);
			this.totaVo.putParam("ReviewLeverageRatio", 0);
			this.totaVo.putParam("ReviewEquityRatio", 0);
			this.totaVo.putParam("ReviewLongFitRatio", 0);
			this.totaVo.putParam("ReviewNetProfitRatio", 0);
		} else {
			this.totaVo.putParam("ReviewCurrentAsset", finReportReview.getCurrentAsset());
			this.totaVo.putParam("ReviewTotalAsset", finReportReview.getTotalAsset());
			this.totaVo.putParam("ReviewPropertyAsset", finReportReview.getPropertyAsset());
			this.totaVo.putParam("ReviewInvestment", finReportReview.getInvestment());
			this.totaVo.putParam("ReviewInvestmentProperty", finReportReview.getInvestmentProperty());
			this.totaVo.putParam("ReviewDepreciation", finReportReview.getDepreciation());
			this.totaVo.putParam("ReviewCurrentDebt", finReportReview.getCurrentDebt());
			this.totaVo.putParam("ReviewTotalDebt", finReportReview.getTotalDebt());
			this.totaVo.putParam("ReviewTotalEquity", finReportReview.getTotalEquity());
			this.totaVo.putParam("ReviewBondsPayable", finReportReview.getBondsPayable());
			this.totaVo.putParam("ReviewLongTermBorrowings", finReportReview.getLongTermBorrowings());
			this.totaVo.putParam("ReviewNonCurrentLease", finReportReview.getNonCurrentLease());
			this.totaVo.putParam("ReviewLongTermPayable", finReportReview.getLongTermPayable());
			this.totaVo.putParam("ReviewPreference", finReportReview.getPreference());
			this.totaVo.putParam("ReviewOperatingRevenue", finReportReview.getOperatingRevenue());
			this.totaVo.putParam("ReviewInterestExpense", finReportReview.getInterestExpense());
			this.totaVo.putParam("ReviewProfitBeforeTax", finReportReview.getProfitBeforeTax());
			this.totaVo.putParam("ReviewProfitAfterTax", finReportReview.getProfitAfterTax());
			this.totaVo.putParam("ReviewWorkingCapitalRatio", finReportReview.getWorkingCapitalRatio());
			this.totaVo.putParam("ReviewInterestCoverageRatio1", finReportReview.getInterestCoverageRatio1());
			this.totaVo.putParam("ReviewInterestCoverageRatio2", finReportReview.getInterestCoverageRatio2());
			this.totaVo.putParam("ReviewLeverageRatio", finReportReview.getLeverageRatio());
			this.totaVo.putParam("ReviewEquityRatio", finReportReview.getEquityRatio());
			this.totaVo.putParam("ReviewLongFitRatio", finReportReview.getLongFitRatio());
			this.totaVo.putParam("ReviewNetProfitRatio", finReportReview.getNetProfitRatio());
		}

		this.addList(this.totaVo);
		return this.sendList();
	}
}