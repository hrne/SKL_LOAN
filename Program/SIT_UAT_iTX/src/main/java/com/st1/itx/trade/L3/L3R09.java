package com.st1.itx.trade.L3;

import java.math.BigDecimal;
import java.util.ArrayList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.dataVO.TotaVo;
import com.st1.itx.db.domain.CdBaseRate;
import com.st1.itx.db.domain.FacProd;
import com.st1.itx.db.domain.LoanBorMain;
import com.st1.itx.db.domain.LoanBorMainId;
import com.st1.itx.db.domain.LoanRateChange;
import com.st1.itx.db.service.CdBaseRateService;
import com.st1.itx.db.service.FacProdService;
import com.st1.itx.db.service.LoanBorMainService;
import com.st1.itx.db.service.LoanRateChangeService;
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.parse.Parse;

/*
 * Tita
 * RimCustNo=9,7
 * RimFacmNo=9,3
 * RimBormNo=9,3
 * RimEffectDate=9,7
 */

/**
 * L3R09 查詢下次利率調整日期的放款利率變動檔
 * 
 * @author iza
 * @version 1.0.0
 */
@Service("L3R09")
@Scope("prototype")
public class L3R09 extends TradeBuffer {
	private static final Logger logger = LoggerFactory.getLogger(L3R09.class);

	/* DB服務注入 */
	@Autowired
	public CdBaseRateService cdBaseRateService;
	@Autowired
	public FacProdService facProdService;
	@Autowired
	public LoanBorMainService loanBorMainService;
	@Autowired
	public LoanRateChangeService loanRateChangeService;

	@Autowired
	Parse parse;

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active L3R09 ");
		this.totaVo.init(titaVo);

		// 取得輸入資料
		int iCustNo = this.parse.stringToInteger(titaVo.getParam("RimCustNo"));
		int iFacmNo = this.parse.stringToInteger(titaVo.getParam("RimFacmNo"));
		int iBormNo = this.parse.stringToInteger(titaVo.getParam("RimBormNo"));
		int iEffectDate = this.parse.stringToInteger(titaVo.getParam("RimEffectDate"));

		// work area
		BigDecimal wkFitRate;
		FacProd tFacProd;
		CdBaseRate tCdBaseRate;
		LoanBorMain tLoanBorMain;
		LoanRateChange tLoanRateChange;

		// 查尋放款主檔
		tLoanBorMain = loanBorMainService.findById(new LoanBorMainId(iCustNo, iFacmNo, iBormNo), titaVo);
		if (tLoanBorMain == null) {
			throw new LogicException(titaVo, "E0001", "L3R09 放款主檔 戶號 = " + iCustNo + " 額度編號 = " + iFacmNo + " 撥款序號 = " + iBormNo); // 查詢資料不存在
		}
		// 放款利率變動檔
		tLoanRateChange = loanRateChangeService.rateChangeEffectDateDescFirst(iCustNo, iFacmNo, iBormNo, iEffectDate + 19110000, titaVo);
		if (tLoanRateChange == null) {
			throw new LogicException(titaVo, "E0001", "L3R09 放款利率變動檔 戶號 = " + iCustNo + " 額度編號 = " + iFacmNo + " 撥款序號 = " + iBormNo + " 生效日期 = " + iEffectDate); // 查詢資料不存在
		}
		// 查詢指標利率檔
		tCdBaseRate = cdBaseRateService.baseRateCodeDescFirst(tLoanBorMain.getCurrencyCode(), tLoanRateChange.getBaseRateCode(), 10101, tLoanRateChange.getEffectDate() + 19110000, titaVo);
		if (tCdBaseRate == null) {
			throw new LogicException(titaVo, "E0001", "L3R09 指標利率檔 利率代碼 = " + tLoanRateChange.getBaseRateCode() + " 生效日期 = " + tLoanRateChange.getEffectDate()); // 查詢資料不存在
		}
		// 查詢商品參數檔
		tFacProd = facProdService.findById(tLoanRateChange.getProdNo(), titaVo);
		if (tFacProd == null) {
			throw new LogicException(titaVo, "E0001", "L3R09 商品參數檔"); // 查詢資料不存在
		}
		// 利率區分 1: 機動 2: 固動/ 3: 定期機動
		wkFitRate = tLoanRateChange.getRateCode().equals("2") ? tLoanRateChange.getFitRate()
				: tCdBaseRate.getBaseRate().add(tLoanRateChange.getIncrFlag().equals("Y") ? tLoanRateChange.getRateIncr() : tLoanRateChange.getIndividualIncr());

		this.totaVo.putParam("OEffectDate", tLoanRateChange.getEffectDate());
		this.totaVo.putParam("OStatus", tLoanRateChange.getStatus());
		this.totaVo.putParam("ORateCode", tLoanRateChange.getRateCode());
		this.totaVo.putParam("OProdNo", tLoanRateChange.getProdNo());
		this.totaVo.putParam("OProdName", tFacProd.getProdName());
		this.totaVo.putParam("OProdRate", tCdBaseRate.getBaseRate().add(tFacProd.getProdIncr()));
		this.totaVo.putParam("OBaseRateCode", tLoanRateChange.getBaseRateCode());
		this.totaVo.putParam("OBaseRate", tCdBaseRate.getBaseRate());
		this.totaVo.putParam("OIncrFlag", tLoanRateChange.getIncrFlag());
		this.totaVo.putParam("ORateIncr", tLoanRateChange.getRateIncr());
		this.totaVo.putParam("OIndividualIncr", tLoanRateChange.getIndividualIncr());
		this.totaVo.putParam("OFitRate", wkFitRate);
		this.totaVo.putParam("ORemark", tLoanRateChange.getRemark());

		this.addList(this.totaVo);
		return this.sendList();
	}
}