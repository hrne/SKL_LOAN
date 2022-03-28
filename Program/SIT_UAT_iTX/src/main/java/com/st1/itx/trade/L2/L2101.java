package com.st1.itx.trade.L2;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;

import com.st1.itx.Exception.DBException;
import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.dataVO.TotaVo;
import com.st1.itx.db.domain.FacProd;
import com.st1.itx.db.domain.FacProdAcctFee;
import com.st1.itx.db.domain.FacProdAcctFeeId;
import com.st1.itx.db.domain.FacProdPremium;
import com.st1.itx.db.domain.FacProdPremiumId;
import com.st1.itx.db.domain.FacProdStepRate;
import com.st1.itx.db.domain.FacProdStepRateId;
import com.st1.itx.db.service.FacProdAcctFeeService;
import com.st1.itx.db.service.FacProdBreachService;
import com.st1.itx.db.service.FacProdPremiumService;
import com.st1.itx.db.service.FacProdService;
import com.st1.itx.db.service.FacProdStepRateService;
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.common.SendRsp;
import com.st1.itx.util.data.DataLog;
import com.st1.itx.util.date.DateUtil;
import com.st1.itx.util.parse.Parse;

/*
 * L2101 商品參數維護
 * a.商品參數生效後限制刪除,只允許修改商品狀態、商品截止日期.
 */

/**
 * L2101 商品參數維護
 * 
 * @author iza
 * @version 1.0.0
 */
@Service("L2101")
@Scope("prototype")
public class L2101 extends TradeBuffer {

	/* DB服務注入 */
	@Autowired
	public FacProdService facProdService;
	@Autowired
	public FacProdStepRateService facProdStepRateService;
	@Autowired
	public FacProdAcctFeeService facProdAcctFeeService;
	@Autowired
	public FacProdPremiumService facProdPremiumService;
	@Autowired
	public FacProdBreachService facProdBreachService;

	@Autowired
	Parse parse;
	@Autowired
	SendRsp sendRsp;
	@Autowired
	DateUtil dDateUtil;
	@Autowired
	public DataLog datalog;
	private FacProd beforeFacProd;

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active L2101 ");
		this.info("   titaVo.getHsupCode() = " + titaVo.getHsupCode());

		this.totaVo.init(titaVo);

		// 取得輸入資料
		int iFuncCode = this.parse.stringToInteger(titaVo.getParam("FuncCode"));
		String iProdNo = titaVo.getParam("ProdNo");
		titaVo.getParam("BreachCode");

		// 檢查輸入資料
		if (!(iFuncCode >= 1 && iFuncCode <= 5)) {
			throw new LogicException(titaVo, "E0010", "iFuncCode = " + iFuncCode); // 功能選擇錯誤
		}
		// 商品參數維護，需主管核可
		if (iFuncCode != 5) {
			if (!titaVo.getHsupCode().equals("1")) {
				sendRsp.addvReason(this.txBuffer, titaVo, "0010", "");
			}
		}
		// 更新商品參數主檔
		FacProd tFacProd = new FacProd();
		switch (iFuncCode) {
		case 1: // 新增
			this.info("funcd =  1");
		case 3: // 拷貝
			this.info("funcd =  3");
			moveFacProd(iFuncCode, tFacProd, titaVo);
			try {
				facProdService.insert(tFacProd, titaVo);
			} catch (DBException e) {
				if (e.getErrorId() == 2) {
					throw new LogicException(titaVo, "E0002", "商品代碼 = " + iProdNo + " " + e.getErrorMsg()); // 新增資料已存在
				}
			}
			break;
		case 2: // 修改 商品生效,商品未被額度使用可修改
			tFacProd = facProdService.holdById(iProdNo);
			beforeFacProd = (FacProd) datalog.clone(tFacProd);
			if (tFacProd == null) {
				throw new LogicException(titaVo, "E0003", "商品代碼 = " + iProdNo); // 修改資料不存在
			} else {
				moveFacProd(iFuncCode, tFacProd, titaVo);
			}
			try {
				tFacProd = facProdService.update2(tFacProd, titaVo);
			} catch (DBException e) {
				throw new LogicException(titaVo, "E0007", "商品代碼 = " + iProdNo + " " + e.getErrorMsg()); // 更新資料時，發生錯誤
			}
			datalog.setEnv(titaVo, beforeFacProd, tFacProd);
			datalog.exec("修改商品參數主檔");
			break;
		case 4: // 刪除 商品參數生效後禁止刪除
			try {
				tFacProd = facProdService.holdById(iProdNo);
				if (tFacProd != null)
					if (tFacProd.getStartDate() <= this.txBuffer.getTxCom().getTbsdy()) {
						throw new LogicException(titaVo, "E2056", ""); // 商品參數生效後禁止刪除
					}
				datalog.setEnv(titaVo, tFacProd, tFacProd);
				datalog.exec("刪除商品參數主檔");
				facProdService.delete(tFacProd, titaVo);
			} catch (DBException e) {
				throw new LogicException(titaVo, "E0004", "商品代碼 = " + iProdNo + " " + e.getErrorMsg()); // 刪除資料不存在
			}
			break;
		case 5: // inq
			this.addList(this.totaVo);
			return this.sendList();
		}

		FacProdAcctFee tFacProdAcctFee = new FacProdAcctFee();
		Slice<FacProdAcctFee> slFacProdAcctFee = null;
		List<FacProdAcctFee> lFacProdAcctFee = new ArrayList<FacProdAcctFee>();
		// 更新階梯式利率
		FacProdStepRate tFacProdStepRate = new FacProdStepRate();
		Slice<FacProdStepRate> slFacProdStepRate = facProdStepRateService.stepRateProdNoEq(iProdNo, 0, 999, this.index, Integer.MAX_VALUE);
		List<FacProdStepRate> lFacProdStepRate = slFacProdStepRate == null ? null : slFacProdStepRate.getContent();
		if (lFacProdStepRate != null && lFacProdStepRate.size() > 0) {
			try {
				facProdStepRateService.deleteAll(lFacProdStepRate);
			} catch (DBException e) {
				throw new LogicException(titaVo, "E2008", "階梯式利率"); // 刪除資料時，發生錯誤
			}
		}
		if (iFuncCode == 1 || iFuncCode == 2 || iFuncCode == 3) {
			for (int i = 1; i <= 10; i++) {
				if (titaVo.get("StepMonthS" + i) != null && this.parse.stringToDouble(titaVo.get("StepMonthS" + i)) >= 0) {
					if (this.parse.stringToDouble(titaVo.getParam("StepMonthS" + i)) == 0 && i > 1) {
						break;
					}
					tFacProdStepRate.setProdNo(iProdNo);
					tFacProdStepRate.setMonthStart(this.parse.stringToInteger(titaVo.getParam("StepMonthS" + i)));
					tFacProdStepRate.setFacProdStepRateId(new FacProdStepRateId(iProdNo, this.parse.stringToInteger(titaVo.getParam("StepMonthS" + i))));
					if (i == 10) {
						tFacProdStepRate.setMonthEnd(999);
					} else {
						if (this.parse.stringToInteger(titaVo.getParam("StepMonthS" + (i + 1))) == 0) {
							tFacProdStepRate.setMonthEnd(999);
						} else {
							tFacProdStepRate.setMonthEnd(this.parse.stringToInteger(titaVo.getParam("StepMonthE" + i)));
						}
					}
					tFacProdStepRate.setRateType(titaVo.getParam("StepRateType" + i));
					tFacProdStepRate.setRateIncr(this.parse.stringToBigDecimal(titaVo.getParam("StepRateIncr" + i)));
					tFacProdStepRate.setCreateDate(parse.IntegerToSqlDateO(dDateUtil.getNowIntegerForBC(), dDateUtil.getNowIntegerTime()));
					tFacProdStepRate.setCreateEmpNo(titaVo.getTlrNo());
					tFacProdStepRate.setLastUpdate(parse.IntegerToSqlDateO(dDateUtil.getNowIntegerForBC(), dDateUtil.getNowIntegerTime()));
					tFacProdStepRate.setLastUpdateEmpNo(titaVo.getTlrNo());
					try {
						facProdStepRateService.insert(tFacProdStepRate);
					} catch (DBException e) {
						throw new LogicException(titaVo, "E2009", "階梯式利率"); // 新增資料時，發生錯誤
					}
				} else {
					break;
				}
			}
		}

		// 更新年繳保費優惠減碼
		FacProdPremium tFacProdPremium = new FacProdPremium();
		Slice<FacProdPremium> slFacProdPremium = facProdPremiumService.premiumProdNoEq(iProdNo, new BigDecimal(0.00), new BigDecimal(99999999999999.00), this.index, Integer.MAX_VALUE);
		List<FacProdPremium> lFacProdPremium = slFacProdPremium == null ? null : slFacProdPremium.getContent();
		if (lFacProdPremium != null && lFacProdPremium.size() > 0) {
			try {
				facProdPremiumService.deleteAll(lFacProdPremium);
			} catch (DBException e) {
				throw new LogicException(titaVo, "E2008", "年繳保費優惠減碼"); // 刪除資料時，發生錯誤
			}
		}
		if (iFuncCode == 1 || iFuncCode == 2 || iFuncCode == 3) {
			for (int i = 1; i <= 10; i++) {
				if (titaVo.get("TimPremium" + i) != null && this.parse.stringToDouble(titaVo.get("TimPremium" + i)) > 0) {
					tFacProdPremium.setProdNo(iProdNo);
					tFacProdPremium.setPremiumLow(this.parse.stringToBigDecimal(titaVo.getParam("TimPremium" + i)));
					tFacProdPremium.setFacProdPremiumId(new FacProdPremiumId(iProdNo, this.parse.stringToBigDecimal(titaVo.getParam("TimPremium" + i))));
					if (i == 10) {
						tFacProdPremium.setPremiumHigh(new BigDecimal(99999999999999.00));
					} else {
						if (this.parse.stringToDouble(titaVo.getParam("TimPremium" + (i + 1)).trim()) == 0) {
							tFacProdPremium.setPremiumHigh(new BigDecimal(99999999999999.00));
						} else {
							tFacProdPremium.setPremiumHigh(this.parse.stringToBigDecimal(titaVo.getParam("TimPremium" + i)).subtract(new BigDecimal(1)));
						}
					}
					tFacProdPremium.setPremiumIncr(this.parse.stringToBigDecimal(titaVo.getParam("PremiumIncr" + i)));
					tFacProdPremium.setCreateDate(parse.IntegerToSqlDateO(dDateUtil.getNowIntegerForBC(), dDateUtil.getNowIntegerTime()));
					tFacProdPremium.setCreateEmpNo(titaVo.getTlrNo());
					tFacProdPremium.setLastUpdate(parse.IntegerToSqlDateO(dDateUtil.getNowIntegerForBC(), dDateUtil.getNowIntegerTime()));
					tFacProdPremium.setLastUpdateEmpNo(titaVo.getTlrNo());
					try {
						facProdPremiumService.insert(tFacProdPremium, titaVo);
					} catch (DBException e) {
						throw new LogicException(titaVo, "E2009", "年繳保費優惠減碼"); // 新增資料時，發生錯誤
					}
				} else {
					break;
				}
			}
		}
		// 更新帳管費
		slFacProdAcctFee = facProdAcctFeeService.acctFeeProdNoEq(iProdNo, "1", new BigDecimal(0.00), new BigDecimal(99999999999999.00), this.index, Integer.MAX_VALUE);
		lFacProdAcctFee = slFacProdAcctFee == null ? null : slFacProdAcctFee.getContent();
		if (lFacProdAcctFee != null && lFacProdAcctFee.size() > 0) {
			try {
				facProdAcctFeeService.deleteAll(lFacProdAcctFee);
			} catch (DBException e) {
				throw new LogicException(titaVo, "E2008", "帳管費"); // 刪除資料時，發生錯誤
			}
		}
		if (iFuncCode == 1 || iFuncCode == 2 || iFuncCode == 3) {
			for (int i = 1; i <= 5; i++) {
				tFacProdAcctFee = new FacProdAcctFee();
				if (titaVo.get("TimLoanAmt" + i) != null && this.parse.stringToDouble(titaVo.get("TimLoanAmt" + i)) > 0) {
					tFacProdAcctFee.setProdNo(iProdNo);
					tFacProdAcctFee.setLoanLow(this.parse.stringToBigDecimal(titaVo.getParam("TimLoanAmt" + i)));
					tFacProdAcctFee.setFacProdAcctFeeId(new FacProdAcctFeeId(iProdNo, "1", this.parse.stringToBigDecimal(titaVo.getParam("TimLoanAmt" + i))));
					if (i == 5) {
						tFacProdAcctFee.setLoanHigh(new BigDecimal(99999999999999.00));
					} else {
						if (this.parse.stringToDouble(titaVo.getParam("TimLoanAmt" + (i + 1))) == 0) {
							tFacProdAcctFee.setLoanHigh(new BigDecimal(99999999999999.00));
						} else {
							tFacProdAcctFee.setLoanHigh(this.parse.stringToBigDecimal(titaVo.getParam("TimLoanAmt" + i)).subtract(new BigDecimal(1)));
						}
					}
					tFacProdAcctFee.setAcctFee(this.parse.stringToBigDecimal(titaVo.getParam("TimAcctFee" + i)));

					try {
						facProdAcctFeeService.insert(tFacProdAcctFee, titaVo);
					} catch (DBException e) {
						throw new LogicException(titaVo, "E2009", "帳管費"); // 新增資料時，發生錯誤
					}
				} else {
					break;
				}
			}
		}
		// 更新手續費 handlingFee
		slFacProdAcctFee = facProdAcctFeeService.acctFeeProdNoEq(iProdNo, "2", new BigDecimal(0.00), new BigDecimal(99999999999999.00), this.index, Integer.MAX_VALUE);
		lFacProdAcctFee = slFacProdAcctFee == null ? null : slFacProdAcctFee.getContent();
		if (lFacProdAcctFee != null && lFacProdAcctFee.size() > 0) {
			try {
				facProdAcctFeeService.deleteAll(lFacProdAcctFee, titaVo);
			} catch (DBException e) {
				throw new LogicException(titaVo, "E2008", "手續費"); // 刪除資料時，發生錯誤
			}
		}
		if (iFuncCode == 1 || iFuncCode == 2 || iFuncCode == 3) {
			for (int i = 1; i <= 5; i++) {
				tFacProdAcctFee = new FacProdAcctFee();
				if (titaVo.get("TimHandlingFee" + i) != null && this.parse.stringToDouble(titaVo.get("TimHandlingFee" + i)) > 0) {
					tFacProdAcctFee.setProdNo(iProdNo);
					tFacProdAcctFee.setLoanLow(this.parse.stringToBigDecimal(titaVo.getParam("TimLoanAmtB" + i)));
					tFacProdAcctFee.setFacProdAcctFeeId(new FacProdAcctFeeId(iProdNo, "2", this.parse.stringToBigDecimal(titaVo.getParam("TimLoanAmtB" + i))));
					if (i == 5) {
						tFacProdAcctFee.setLoanHigh(new BigDecimal(99999999999999.00));
					} else {
						if (this.parse.stringToDouble(titaVo.getParam("TimLoanAmtB" + (i + 1))) == 0) {
							tFacProdAcctFee.setLoanHigh(new BigDecimal(99999999999999.00));
						} else {
							tFacProdAcctFee.setLoanHigh(this.parse.stringToBigDecimal(titaVo.getParam("TimLoanAmtB" + i)).subtract(new BigDecimal(1)));
						}
					}
					tFacProdAcctFee.setAcctFee(this.parse.stringToBigDecimal(titaVo.getParam("TimHandlingFee" + i)));

					try {
						facProdAcctFeeService.insert(tFacProdAcctFee, titaVo);
					} catch (DBException e) {
						throw new LogicException(titaVo, "E2009", "帳管費"); // 新增資料時，發生錯誤
					}
				} else {
					break;
				}
			}
		}

		this.addList(this.totaVo);
		return this.sendList();
	}

	private void moveFacProd(int mFuncCode, FacProd mFacProd, TitaVo titaVo) throws LogicException {
		mFacProd.setProdNo(titaVo.getParam("ProdNo"));
		mFacProd.setProdName(titaVo.getParam("ProdName"));
		mFacProd.setStartDate(this.parse.stringToInteger(titaVo.getParam("StartDate")));
		// 商品截止日期未輸入時 給0
		if (titaVo.getParam("EndDate").isEmpty()) {
			mFacProd.setEndDate(0);
		} else {
			mFacProd.setEndDate(this.parse.stringToInteger(titaVo.getParam("EndDate")));
		}

		mFacProd.setStatusCode(titaVo.getParam("StatusCode"));
		mFacProd.setAgreementFg(titaVo.getParam("AgreementFg"));
		mFacProd.setEnterpriseFg(titaVo.getParam("EnterpriseFg"));
		mFacProd.setCurrencyCode(titaVo.getParam("CurrencyCode"));
		mFacProd.setBaseRateCode(titaVo.getParam("BaseRateCode"));
		mFacProd.setProdIncr(this.parse.stringToBigDecimal(titaVo.getParam("ProdIncr")));
		mFacProd.setLowLimitRate(this.parse.stringToBigDecimal(titaVo.getParam("LowLimitRate")));
		mFacProd.setRateCode(titaVo.getParam("RateCode"));
		mFacProd.setIncrFlag(titaVo.getParam("IncrFlag"));
		mFacProd.setBreachFlag(titaVo.getParam("BreachFlag"));
		mFacProd.setBreachCode(titaVo.getParam("BreachCode"));
		mFacProd.setBreachGetCode(titaVo.getParam("BreachGetCode"));
		mFacProd.setBreachPercent(parse.stringToBigDecimal(titaVo.getParam("BreachPercent")));
		mFacProd.setBreachDecreaseMonth(parse.stringToInteger(titaVo.getParam("BreachDecreaseMonth")));
		mFacProd.setBreachDecrease(parse.stringToBigDecimal(titaVo.getParam("BreachDecrease")));
		mFacProd.setBreachStartPercent(parse.stringToInteger(titaVo.getParam("BreachStartPercent")));
		mFacProd.setProhibitMonth(parse.stringToInteger(titaVo.getParam("ProhibitMonth")));
		mFacProd.setGovOfferFlag(titaVo.getParam("GovOfferFlag"));
		mFacProd.setFinancialFlag(titaVo.getParam("FinancialFlag"));
		mFacProd.setEmpFlag(titaVo.getParam("EmpFlag"));

		if (mFuncCode != 2) {
			mFacProd.setCreateDate(parse.IntegerToSqlDateO(dDateUtil.getNowIntegerForBC(), dDateUtil.getNowIntegerTime()));
			mFacProd.setCreateEmpNo(titaVo.getTlrNo());
		}
		mFacProd.setLastUpdate(parse.IntegerToSqlDateO(dDateUtil.getNowIntegerForBC(), dDateUtil.getNowIntegerTime()));
		mFacProd.setLastUpdateEmpNo(titaVo.getTlrNo());
	}
}