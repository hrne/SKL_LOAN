package com.st1.itx.trade.L3;

import java.math.BigDecimal;
import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.dataVO.TotaVo;
import com.st1.itx.db.domain.CdBaseRate;
import com.st1.itx.db.domain.FacMain;
import com.st1.itx.db.domain.FacMainId;
import com.st1.itx.db.domain.FacProd;
import com.st1.itx.db.domain.LoanBorMain;
import com.st1.itx.db.domain.LoanBorMainId;
import com.st1.itx.db.domain.LoanRateChange;
import com.st1.itx.db.service.CdBaseRateService;
import com.st1.itx.db.service.FacMainService;
import com.st1.itx.db.service.FacProdService;
import com.st1.itx.db.service.LoanBorMainService;
import com.st1.itx.db.service.LoanRateChangeService;
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.parse.Parse;

/**
 * L3R09 查詢下次利率調整日期的放款利率變動檔
 * 
 * @author iza
 * @version 1.0.0
 */
@Service("L3R09")
@Scope("prototype")
public class L3R09 extends TradeBuffer {

	/* DB服務注入 */
	@Autowired
	public CdBaseRateService cdBaseRateService;
	@Autowired
	public FacProdService facProdService;
	@Autowired
	public FacMainService facMainService;
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
//		變更記號 Y變更 N額度 A下次生效利率(L2980個人房貸使用)
		String iChangFg = titaVo.get("RimChangFg");

		if (iChangFg == null) {
			iChangFg = "N";
		}
		// work area
		FacProd tFacProd;
		CdBaseRate tCdBaseRate;
		LoanBorMain tLoanBorMain;
		LoanRateChange tLoanRateChange;
		LoanRateChange tNextLoanRateChange;
		int wkNextEffectDate = 0;
		BigDecimal wkNextFitRate = BigDecimal.ZERO;
		// 查尋放款主檔
		tLoanBorMain = loanBorMainService.findById(new LoanBorMainId(iCustNo, iFacmNo, iBormNo), titaVo);
		if (tLoanBorMain == null) {
			throw new LogicException(titaVo, "E0001",
					" 放款主檔 戶號 = " + iCustNo + " 額度編號 = " + iFacmNo + " 撥款序號 = " + iBormNo); // 查詢資料不存在
		}
		wkNextEffectDate = tLoanBorMain.getNextAdjRateDate();
		// 額度主檔
		FacMain tFacMain = facMainService.findById(new FacMainId(iCustNo, iFacmNo), titaVo);
		if (tFacMain == null) {
			throw new LogicException(titaVo, "E0001", " 額度主檔"); // 查詢資料不存在
		}
		// 放款利率變動檔
		tLoanRateChange = loanRateChangeService.rateChangeEffectDateDescFirst(iCustNo, iFacmNo, iBormNo,
				iEffectDate + 19110000, titaVo);
		if (tLoanRateChange == null) {
			throw new LogicException(titaVo, "E0001", " 放款利率變動檔 戶號 = " + iCustNo + " 額度編號 = " + iFacmNo + " 撥款序號 = "
					+ iBormNo + " 生效日期 = " + iEffectDate); // 查詢資料不存在
		}
		if ("A".equals(iChangFg)) {
			tNextLoanRateChange = loanRateChangeService.rateChangeEffectDateAscFirst(iCustNo, iFacmNo, iBormNo,
					iEffectDate + 19110001, titaVo);
			if (tNextLoanRateChange != null) {
				wkNextEffectDate = tNextLoanRateChange.getEffectDate();
				wkNextFitRate = tNextLoanRateChange.getFitRate();
			}
		}

		// 查詢指標利率檔
		tCdBaseRate = cdBaseRateService.baseRateCodeDescFirst(tLoanBorMain.getCurrencyCode(),
				tLoanRateChange.getBaseRateCode(), 10101, tLoanRateChange.getEffectDate() + 19110000, titaVo);
		if (tCdBaseRate == null) {
			throw new LogicException(titaVo, "E0001", " 指標利率檔 利率代碼 = " + tLoanRateChange.getBaseRateCode() + " 生效日期 = "
					+ tLoanRateChange.getEffectDate()); // 查詢資料不存在
		}
		// 查詢商品參數檔
		tFacProd = facProdService.findById(tLoanRateChange.getProdNo(), titaVo);
		if (tFacProd == null) {
			throw new LogicException(titaVo, "E0001", " 商品參數檔"); // 查詢資料不存在
		}

		this.totaVo.putParam("L3r09EffectDate", tLoanRateChange.getEffectDate());
		this.totaVo.putParam("L3r09Status", tLoanRateChange.getStatus());
		this.totaVo.putParam("L3r09RateCode", tLoanRateChange.getRateCode());
		this.totaVo.putParam("L3r09ProdNo", tLoanRateChange.getProdNo());
		this.totaVo.putParam("L3r09ProdName", tFacProd.getProdName());
		this.totaVo.putParam("L3r09ProdRate", tCdBaseRate.getBaseRate().add(tFacProd.getProdIncr()));
		this.totaVo.putParam("L3r09BaseRateCode", tLoanRateChange.getBaseRateCode());
		this.totaVo.putParam("L3r09BaseRate", tCdBaseRate.getBaseRate());
		this.totaVo.putParam("L3r09IncrFlag", tLoanRateChange.getIncrFlag());
		this.totaVo.putParam("L3r09RateIncr", tLoanRateChange.getRateIncr());
		this.totaVo.putParam("L3r09IndividualIncr", tLoanRateChange.getIndividualIncr());
		this.totaVo.putParam("L3r09FitRate", tLoanRateChange.getFitRate());
		this.totaVo.putParam("L3r09Remark", tLoanRateChange.getRemark());
		// 變更記號=Y時帶原本值

		if ("N".equals(iChangFg)) {
			this.totaVo.putParam("L3r09FacProdNo", tFacMain.getProdNo());
			this.totaVo.putParam("L3r09FacBaseRateCode", tFacMain.getBaseRateCode());
		} else {
			this.totaVo.putParam("L3r09FacProdNo", tLoanRateChange.getProdNo());
			this.totaVo.putParam("L3r09FacBaseRateCode", tLoanRateChange.getBaseRateCode());
		}
		this.totaVo.putParam("L3r09NextEffectDate", wkNextEffectDate);
		this.totaVo.putParam("L3r09NextFitRate", wkNextFitRate);

		this.addList(this.totaVo);
		return this.sendList();
	}
}