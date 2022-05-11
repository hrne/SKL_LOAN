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
import com.st1.itx.db.service.FinReportDebtService;
import com.st1.itx.db.domain.FinReportProfit;
import com.st1.itx.db.domain.FinReportProfitId;
import com.st1.itx.db.service.FinReportProfitService;
import com.st1.itx.tradeService.TradeBuffer;

@Service("L1R19")
@Scope("prototype")
/**
 * 
 * 
 * @author Fegie
 * @version 1.0.0
 */
public class L1R19 extends TradeBuffer {

	/* DB服務注入 */
	@Autowired
	public FinReportProfitService iFinReportProfitService;
	@Autowired
	public FinReportDebtService iFinReportDebtService;
	@Autowired
	public CustMainService iCustMainService;

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active L1R19 ");
		this.totaVo.init(titaVo);

		String iFunCd = titaVo.getParam("RimFunCd");

		if ("1".equals(iFunCd)) {
			computeGrowRate(titaVo);
		} else if ("2".equals(iFunCd)) {
			checkDup(titaVo);
		}
		this.addList(this.totaVo);
		return this.sendList();
	}

	private void checkDup(TitaVo titaVo) throws LogicException {
		this.info("L1R19.checkDup");

		String iCustId = titaVo.getParam("RimCustId");
		int iStartYYRoc = Integer.valueOf(titaVo.getParam("RimYear"));
		int iStartYY = iStartYYRoc + 1911;

		CustMain custMain = iCustMainService.custIdFirst(iCustId, titaVo);
		if (custMain == null) {
			throw new LogicException("E1003", "客戶資料主檔 : " + iCustId);
		}

		FinReportDebt finReportDebt = iFinReportDebtService.findCustUKeyYearFirst(custMain.getCustUKey(), iStartYY, titaVo);
		if (finReportDebt != null) {
			throw new LogicException("E0012", iStartYYRoc + "年度財務報表 ");
		}

		totaVo.putParam("L1R19GrowRate", 0);
	}

	private void computeGrowRate(TitaVo titaVo) throws LogicException {
		this.info("L1R19.computeGrowRate");

		String iCustId = titaVo.getParam("RimCustId");
		int iYear = Integer.valueOf(titaVo.getParam("RimYear")) + 1911 - 1;

		CustMain iCustMain = iCustMainService.custIdFirst(iCustId, titaVo);
		if (iCustMain == null) { // 客戶檔無統編key error
			throw new LogicException("E0001", "無此統一編號:" + iCustId);
		}

		BigDecimal iLastBusIncome = new BigDecimal("0"); // 去年收入
		BigDecimal iThisBusIncome = new BigDecimal(titaVo.getParam("RimBusIncome")); // 今年收入

		FinReportDebt finReportDebt = iFinReportDebtService.findCustUKeyYearFirst(iCustMain.getCustUKey(), iYear, titaVo);
		if (finReportDebt != null) {
			FinReportProfitId finReportProfitId = new FinReportProfitId();
			finReportProfitId.setCustUKey(finReportDebt.getCustUKey());
			finReportProfitId.setUkey(finReportDebt.getUkey());

			FinReportProfit FinReportProfit = iFinReportProfitService.findById(finReportProfitId, titaVo);
			if (FinReportProfit != null) {
				iLastBusIncome = FinReportProfit.getBusIncome();
			}
		}

		BigDecimal iPercent = new BigDecimal("0");

		if (iLastBusIncome.compareTo(new BigDecimal("0")) != 0) {
			// 成長率算法 (新-舊)/舊
			BigDecimal iHundred = new BigDecimal("100");
			BigDecimal iGap = iThisBusIncome.subtract(iLastBusIncome);
			iPercent = iGap.divide(iLastBusIncome,4, BigDecimal.ROUND_HALF_UP).multiply(iHundred);
		}

		totaVo.putParam("L1R19GrowRate", iPercent);

	}

	public ArrayList<TotaVo> run2(TitaVo titaVo) throws LogicException {
		this.info("active L1R19 ");
		this.totaVo.init(titaVo);

		FinReportDebt iFinReportDebt = new FinReportDebt();
		FinReportProfit iFinReportProfit = new FinReportProfit();
		CustMain iCustMain = new CustMain();
		String iCustId = titaVo.getParam("RimCustId");
		int iYear = Integer.valueOf(titaVo.getParam("RimYear")) + 1911;
		this.info("統編====" + iCustId);
		this.info("年度====" + iYear);
		int iLastYear = iYear - 1;
		String iLastUKey = "";
		BigDecimal iLastBusIncome = new BigDecimal("0"); // 去年收入
		BigDecimal iThisBusIncome = new BigDecimal(titaVo.getParam("RimBusIncome")); // 今年收入
		String iCustUKey = "";
		BigDecimal iHundred = new BigDecimal("100");
		if (iLastYear <= 1911) { // 若去年年分小於等於0則回傳0
			this.info("若去年年分小於等於0");
			totaVo.putParam("L1R19GrowRate", 0);
		} else {
			iCustMain = iCustMainService.custIdFirst(iCustId, titaVo);
			if (iCustMain == null) { // 客戶檔無統編key error
				this.info("客戶檔無統編key error");
				throw new LogicException("E0001", "無此統一編號:" + iCustId);
			}
			iCustUKey = iCustMain.getCustUKey();
			iFinReportDebt = iFinReportDebtService.findCustUKeyYearFirst(iCustUKey, iLastYear, titaVo);
			if (iFinReportDebt == null) { // 資產負債表無去年資料回傳0
				this.info("資產負債表無去年資料回傳0");
				totaVo.putParam("L1R19GrowRate", 0);
			} else {
				iLastUKey = iFinReportDebt.getUkey();
				FinReportProfitId iFinReportProfitId = new FinReportProfitId();
				iFinReportProfitId.setCustUKey(iCustUKey);
				iFinReportProfitId.setUkey(iLastUKey);
				iFinReportProfit = iFinReportProfitService.findById(iFinReportProfitId, titaVo);
				if (iFinReportProfit == null) { // 損益表無去年資料回傳0
					this.info("損益表無去年資料回傳0");
					totaVo.putParam("L1R19GrowRate", 0);
				} else {
					iLastBusIncome = iFinReportProfit.getBusIncome();

					// 成長率算法 (新-舊)/舊
					BigDecimal iGap = iThisBusIncome.subtract(iLastBusIncome);
					BigDecimal iPercent = iGap.divide(iLastBusIncome).setScale(4, BigDecimal.ROUND_HALF_UP).multiply(iHundred);
					this.info("去年====" + iLastBusIncome);
					this.info("差距====" + iGap);
					this.info("成長率====" + iPercent);
					totaVo.putParam("L1R19GrowRate", iPercent);
				}
			}
		}

		this.addList(this.totaVo);
		return this.sendList();
	}
}