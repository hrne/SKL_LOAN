package com.st1.itx.trade.L2;

import java.math.BigDecimal;
import java.util.ArrayList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;

import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.dataVO.TotaVo;
import com.st1.itx.db.domain.FacMain;
import com.st1.itx.db.domain.FacProd;
import com.st1.itx.db.domain.FacProdAcctFee;
import com.st1.itx.db.domain.FacProdBreach;
import com.st1.itx.db.domain.FacProdPremium;
import com.st1.itx.db.domain.FacProdStepRate;
import com.st1.itx.db.service.FacMainService;
import com.st1.itx.db.service.FacProdAcctFeeService;
import com.st1.itx.db.service.FacProdBreachService;
import com.st1.itx.db.service.FacProdPremiumService;
import com.st1.itx.db.service.FacProdService;
import com.st1.itx.db.service.FacProdStepRateService;
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.common.LoanCloseBreachCom;
import com.st1.itx.util.parse.Parse;

/* Tita
 * RimFuncCode=9,1
 * RimTxCode=X,5
 * RimProdNo=X,5
 */
/**
 * L2R01 尋找商品參數檔資料
 * 
 * @author iza
 * @version 1.0.0
 */
@Service("L2R01")
@Scope("prototype")
public class L2R01 extends TradeBuffer {

	/* DB服務注入 */
	@Autowired
	public FacProdService facProdService;
	@Autowired
	public FacMainService facMainService;
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
	LoanCloseBreachCom loanCloseBreachCom;

	// work area
	private String wkBreachCode;
	private String BreachDescription = "";
	private FacProd tFacProd = new FacProd();
	private Slice<FacProdStepRate> lFacProdStepRate;
	private Slice<FacProdPremium> lFacProdPremium;
	private Slice<FacProdAcctFee> lFacProdAcctFee;
	private Slice<FacProdBreach> lFacProdBreach;

	private String wkUseProdFg = "";

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active L2R01 ");
		this.totaVo.init(titaVo);

		// 取得輸入資料
		int iRimFuncCode = this.parse.stringToInteger(titaVo.getParam("RimFuncCode"));
		String iRimTxCode = titaVo.getParam("RimTxCode");
		String iRimProdNo = titaVo.getParam("RimProdNo");

		// 檢查輸入資料
		if (iRimTxCode.isEmpty()) {
			throw new LogicException(titaVo, "E0009", "商品參數檔"); // 交易代號不可為空白
		}
		if (!(iRimFuncCode >= 1 && iRimFuncCode <= 5)) {
			throw new LogicException(titaVo, "E0010", "商品參數檔"); // 功能選擇錯誤
		}

		// 初始值Tota
		SetTotaFacProd();
		SetTotaPremium();
		SetTotaAcctFee();
		SetTotaBreach();
		wkUseProdFg = "";
		// 查詢商品參數檔
		tFacProd = facProdService.findById(iRimProdNo, titaVo);
		/* 如有有找到資料 */
		if (tFacProd != null) {
			// 檢查商品參數檔
			if (iRimTxCode.equals("L2101")) {
				if (iRimFuncCode == 1 || iRimFuncCode == 3) {
					throw new LogicException(titaVo, "E0002", "商品參數檔 商品代碼=" + iRimProdNo); // 新增資料已存在
				}
				if (iRimFuncCode == 4 && tFacProd.getStartDate() <= this.txBuffer.getTxCom().getTbsdy()) {
					throw new LogicException(titaVo, "E2052", "商品參數檔"); // 商品參數生效後禁止刪除
				}
			} else {

				if (iRimFuncCode != 5) {
					if (!(tFacProd.getStartDate() <= parse.stringToInteger(titaVo.getCalDy()))) {
						throw new LogicException(titaVo, "E2053", "商品參數檔"); // 此商品尚未生效
					}
					if (tFacProd.getEndDate() > 0) {
						if (tFacProd.getEndDate() < parse.stringToInteger(titaVo.getCalDy())) {
							throw new LogicException(titaVo, "E2054",
									"商品參數檔" + " 商品代碼 = " + iRimProdNo + " 商品截止日期 : " + tFacProd.getEndDate()); // 此商品已截止
						}
					}

					if (!tFacProd.getStatusCode().equals("0")) {
						throw new LogicException(titaVo, "E2055", "商品已停用 =" + tFacProd.getProdNo() + "商品參數檔"); // 此商品已停用
					}
				}

			}

			 BreachDescription = loanCloseBreachCom.getBreachDescription(tFacProd.getProdNo(), titaVo);
			this.info("清償違約說明= " + BreachDescription);

			/* 將每筆資料放入Tota */
			SetTotaFacProd();
		} else {
			if (iRimTxCode.equals("L2101") && (iRimFuncCode == 1 || iRimFuncCode == 3)) {

				this.totaVo.putParam("OUseProdFg", wkUseProdFg);
				this.addList(this.totaVo);
				return this.sendList();
			} else {
				throw new LogicException(titaVo, "E0001", "商品參數檔"); // 查無資料
			}
		}

		// 設定第幾分頁 titaVo.getReturnIndex() 第一次會是0，如果需折返最後會塞值
		this.index = titaVo.getReturnIndex();

		// 設定每筆分頁的資料筆數 預設500筆 總長不可超過六萬
		this.limit = 500;

		// 查詢階梯式利率

		lFacProdStepRate = facProdStepRateService.stepRateProdNoEq(iRimProdNo, 0, 999, this.index, this.limit, titaVo);

		if (!(lFacProdStepRate == null || lFacProdStepRate.isEmpty())) {

			int i = 1;
			for (FacProdStepRate tFacProdStepRate : lFacProdStepRate.getContent()) {
				this.totaVo.putParam("StepMonths" + i, tFacProdStepRate.getMonthStart());
				this.totaVo.putParam("StepMonthE" + i, tFacProdStepRate.getMonthEnd());
				this.totaVo.putParam("StepRateType" + i, tFacProdStepRate.getRateType());
				this.totaVo.putParam("StepRateIncr" + i, tFacProdStepRate.getRateIncr());
				i++;

			}

		}

		// 查詢年繳保費優惠減碼
		lFacProdPremium = facProdPremiumService.premiumProdNoEq(iRimProdNo, new BigDecimal(0.00),
				new BigDecimal(99999999999999.00), this.index, this.limit, titaVo);
		if (!(lFacProdPremium == null || lFacProdPremium.isEmpty())) {
			SetTotaPremium();
		}

		// 查詢帳管費
		lFacProdAcctFee = facProdAcctFeeService.acctFeeProdNoEq(iRimProdNo, new BigDecimal(0.00),
				new BigDecimal(99999999999999.00), this.index, this.limit, titaVo);
		if (!(lFacProdAcctFee == null || lFacProdAcctFee.isEmpty())) {
			SetTotaAcctFee();
		}

		// 查詢清償金類型
		wkBreachCode = tFacProd != null ? tFacProd.getBreachCode() : "0";
		lFacProdBreach = facProdBreachService.breachNoEq(iRimProdNo, wkBreachCode, wkBreachCode, this.index, this.limit,
				titaVo);
		if (!(lFacProdAcctFee == null || lFacProdAcctFee.isEmpty())) {
//			SetTotaBreach();
		}

		FacMain tFacMain = facMainService.findProdNoFirst(iRimProdNo, titaVo);
		if (!(tFacMain == null)) {
			wkUseProdFg = "Y";
		}
		this.totaVo.putParam("OUseProdFg", wkUseProdFg);

		this.info("totaVo = " + this.totaVo);

		this.addList(this.totaVo);
		return this.sendList();
	}

	// 將每筆資料放入Tota
	// 商品參數檔
	private void SetTotaFacProd() throws LogicException {
		this.info("tFacProd.getEndDate() = " + tFacProd.getEndDate());
		this.totaVo.putParam("OProdNo", tFacProd.getProdNo());
		this.totaVo.putParam("OProdName", tFacProd.getProdName());
		this.totaVo.putParam("OStartDate", tFacProd.getStartDate());
		this.totaVo.putParam("OEndDate", tFacProd.getEndDate());
		this.totaVo.putParam("OStatusCode", tFacProd.getStatusCode());
		this.totaVo.putParam("OAgreementFg", tFacProd.getAgreementFg());
		this.totaVo.putParam("OCharCode", 1);
		this.totaVo.putParam("OAcctCode", "");
		this.totaVo.putParam("OAcctAcno", 0);
		this.totaVo.putParam("OAcctSbno", 0);
		this.totaVo.putParam("OCurrencyCode", tFacProd.getCurrencyCode());
		this.totaVo.putParam("OLowAmt", 0);
		this.totaVo.putParam("OHighAmt", 0);
		this.totaVo.putParam("OBaseRateCode", tFacProd.getBaseRateCode());
		this.totaVo.putParam("OProdIncr", tFacProd.getProdIncr());
		this.totaVo.putParam("OGraceIncr", 0);
		this.totaVo.putParam("OLowLimitRate", tFacProd.getLowLimitRate());
		this.totaVo.putParam("ORateCode", tFacProd.getRateCode());
		this.totaVo.putParam("OIncrFlag", tFacProd.getIncrFlag());
		this.totaVo.putParam("OFirstRateAdjFreq", 0);
		this.totaVo.putParam("ORateAdjFreq", 0);
		this.totaVo.putParam("OFixAdjDd", 0);
		this.totaVo.putParam("OAmortizedCode", 9);
		this.totaVo.putParam("OIntCalcCode", 9);
		this.totaVo.putParam("OExtraRepayCode", 9);
		this.totaVo.putParam("ORecycleCode", 9);
		this.totaVo.putParam("OFreqBase", 9);
		this.totaVo.putParam("OPayIntFreq", 99);
		this.totaVo.putParam("ORepayFreq", 99);
		this.totaVo.putParam("OGracePeriod", 999);
		this.totaVo.putParam("OBreachFlag", tFacProd.getBreachFlag());
		this.totaVo.putParam("OBreachCode", tFacProd.getBreachCode());
		this.totaVo.putParam("OBreachGetCode", tFacProd.getBreachGetCode());
//		this.totaVo.putParam("ODecreaseFlag", tFacProd.getDecreaseFlag());
		this.totaVo.putParam("OProhibitMonth", tFacProd.getProhibitMonth());
		this.totaVo.putParam("OGovOfferFlag", tFacProd.getGovOfferFlag());
		this.totaVo.putParam("FinancialFlag", tFacProd.getFinancialFlag());
		this.totaVo.putParam("OEmpFlag", tFacProd.getEmpFlag());
		this.totaVo.putParam("OPayIntLimit", 0);
		this.totaVo.putParam("OBreachPercent", tFacProd.getBreachPercent());
		this.totaVo.putParam("OBreachDecreaseMonth", tFacProd.getBreachDecreaseMonth());
		this.totaVo.putParam("OBreachDecrease", tFacProd.getBreachDecrease());
		this.totaVo.putParam("OBreachStartPercent", tFacProd.getBreachStartPercent());
		this.totaVo.putParam("OBreach", BreachDescription);
		for (int i = 1; i <= 10; i++) {
			this.totaVo.putParam("StepMonths" + i, 0);
			this.totaVo.putParam("StepMonthE" + i, 0);
			this.totaVo.putParam("StepRateType" + i, 0);
			this.totaVo.putParam("StepRateIncr" + i, 0);
		}

	}

	// 年繳保費優惠減碼
	private void SetTotaPremium() throws LogicException {
		if (lFacProdPremium == null || lFacProdPremium.isEmpty()) {
			for (int i = 1; i <= 10; i++) {
				this.totaVo.putParam("Premium" + i, 0);
				this.totaVo.putParam("PremiumIncr" + i, 0);
			}
		} else {
			int i = 1;
			for (FacProdPremium tFacProdPremium : lFacProdPremium.getContent()) {
				this.totaVo.putParam("Premium" + i, tFacProdPremium.getPremiumLow());
				this.totaVo.putParam("PremiumIncr" + i, tFacProdPremium.getPremiumIncr());
				i++;
				if (i > 10)
					break;
			}
		}
	}

	// 帳管費
	private void SetTotaAcctFee() throws LogicException {
		if (lFacProdAcctFee == null || lFacProdAcctFee.isEmpty()) {
			for (int i = 1; i <= 5; i++) {
				this.totaVo.putParam("LoanAmt" + i, 0);
				this.totaVo.putParam("AcctFee" + i, 0);
			}
		} else {
			int i = 1;
			for (FacProdAcctFee tFacProdAcctFee : lFacProdAcctFee.getContent()) {
				this.totaVo.putParam("LoanAmt" + i, tFacProdAcctFee.getLoanLow());
				this.totaVo.putParam("AcctFee" + i, tFacProdAcctFee.getAcctFee());
				i++;
				if (i > 5)
					break;
			}
		}
	}

	// 清償金類型
	private void SetTotaBreach() throws LogicException {
		if (lFacProdBreach == null || lFacProdBreach.isEmpty()) {
			for (int i = 1; i <= 10; i++) {
				this.totaVo.putParam("BreachbMmA" + i, 0);
				this.totaVo.putParam("BreachbMmB" + i, 0);
				this.totaVo.putParam("BreachbPercent" + i, 0);
				this.totaVo.putParam("BreachaYyA" + i, 0);
				this.totaVo.putParam("BreachaYyB" + i, 0);
				this.totaVo.putParam("BreachaPercent" + i, 0);
			}
		} else {
			int i = 1;
			for (FacProdBreach tFacProdBreach : lFacProdBreach.getContent()) {
				if (wkBreachCode.equals("2")) {
					this.totaVo.putParam("BreachbMmA" + i, tFacProdBreach.getMonthStart());
					this.totaVo.putParam("BreachbMmB" + i, tFacProdBreach.getMonthEnd());
					this.totaVo.putParam("BreachbPercent" + i, tFacProdBreach.getBreachPercent());
				} else {
					this.totaVo.putParam("BreachaYyA" + i, tFacProdBreach.getMonthStart() / 12);
					this.totaVo.putParam("BreachaYyB" + i, tFacProdBreach.getMonthEnd() / 12);
					this.totaVo.putParam("BreachaPercent" + i, tFacProdBreach.getBreachPercent());
				}
				i++;
				if (i > 10)
					break;
			}
		}
	}
}