package com.st1.itx.trade.BS;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Component;

import com.st1.itx.Exception.DBException;
import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.dataVO.TotaVo;
import com.st1.itx.db.domain.CdBaseRate;
import com.st1.itx.db.domain.FacMain;
import com.st1.itx.db.domain.FacMainId;
import com.st1.itx.db.domain.FacProd;
import com.st1.itx.db.domain.FacProdStepRate;
import com.st1.itx.db.domain.LoanBorMain;
import com.st1.itx.db.domain.LoanBorMainId;
import com.st1.itx.db.domain.LoanRateChange;
import com.st1.itx.db.domain.LoanRateChangeId;
import com.st1.itx.db.service.CdBaseRateService;
import com.st1.itx.db.service.FacMainService;
import com.st1.itx.db.service.FacProdService;
import com.st1.itx.db.service.FacProdStepRateService;
import com.st1.itx.db.service.LoanBorMainService;
import com.st1.itx.db.service.LoanBorTxService;
import com.st1.itx.db.service.LoanRateChangeService;
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.date.DateUtil;
import com.st1.itx.util.format.FormatUtil;
import com.st1.itx.util.parse.Parse;

/**
 * 日始作業 <br>
 * 執行時機：patch LoanRateChange
 * 
 * @author Lai
 * @version 1.0.0
 */

@Component("BS998")
@Scope("prototype")
public class BS998 extends TradeBuffer {
	private static final Logger logger = LoggerFactory.getLogger(BS998.class);

	/* 轉型共用工具 */
	@Autowired

	public Parse parse;
	/* 日期工具 */
	@Autowired
	public DateUtil dateUtil;

	@Autowired
	public FacProdStepRateService facProdStepRateService;
	@Autowired
	public FacMainService facMainService;
	@Autowired
	public LoanBorMainService loanBorMainService;
	@Autowired
	public LoanBorTxService loanBorTxService;
	@Autowired
	public LoanRateChangeService loanRateChangeService;
	@Autowired
	public CdBaseRateService cdBaseRateService;
	@Autowired
	public FacProdService facProdService;

	@Autowired
	DateUtil dDateUtil;

	private FacProd tFacProd;
	private FacMain tFacMain;
	private LoanBorMain tLoanBorMain;
	private LoanRateChange tLoanRateChange;
	private LoanRateChangeId tLoanRateChangeId;
	private List<LoanBorMain> lLoanBorMain = new ArrayList<LoanBorMain>();
	private List<FacProdStepRate> lFacProdStepRate = new ArrayList<FacProdStepRate>();
	private TitaVo titaVo = new TitaVo();

	// work area
	private int wkBormNo = 0;
	private String sProdStepNo = "";
	int wkStartDate = 0;
	int wkEffectDate = 0;

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("BS998 ......");
		// 維護利率變動檔
		this.titaVo = titaVo;
		Slice<LoanBorMain> slLoanBorMain = loanBorMainService.findAll(0, Integer.MAX_VALUE, titaVo);
		lLoanBorMain = slLoanBorMain == null ? null : slLoanBorMain.getContent();
		if (lLoanBorMain == null || lLoanBorMain.size() == 0) {
			throw new LogicException(titaVo, "E0001", "放款主檔"); // 查詢資料不存在
		}
		// 戶況 0:正常戶1:展期2:催收戶3: 結案戶4:逾期戶5:催收結案戶6:呆帳戶7:部分轉呆戶8:債權轉讓戶9:呆帳結案戶
		for (LoanBorMain ln : lLoanBorMain) {
			if (ln.getBormNo() > 900) {
				continue;
			}
			// 鎖定額度檔
			tLoanBorMain = loanBorMainService.findById(new LoanBorMainId(ln.getCustNo(), ln.getFacmNo(), ln.getBormNo()), titaVo);
			tFacMain = facMainService.holdById(new FacMainId(tLoanBorMain.getCustNo(), tLoanBorMain.getFacmNo()));
			if (tFacMain == null || tFacMain.getFirstDrawdownDate() == 0) {
				continue;
			}
			tFacProd = facProdService.findById(tFacMain.getProdNo());
			if (tFacProd == null) {
				throw new LogicException(titaVo, "E0001", "商品參數檔"); // 查詢資料不存在
			}
			wkBormNo = tLoanBorMain.getBormNo();
			sProdStepNo = FormatUtil.pad9(String.valueOf(tFacMain.getCustNo()), 7) + FormatUtil.pad9(String.valueOf(tFacMain.getFacmNo()), 3);

			// 維護放款利率變動檔, 階梯式利率
			Slice<FacProdStepRate> slFacProdStepRate = facProdStepRateService.stepRateProdNoEq(sProdStepNo, 0, 999, 0, Integer.MAX_VALUE);
			lFacProdStepRate = slFacProdStepRate == null ? null : slFacProdStepRate.getContent();
			if (lFacProdStepRate != null && lFacProdStepRate.size() > 0) {
				// 維護放款利率變動檔, 階梯式利率
				LoanRateChange2Routine();
			}
		}

		// commitEnd
		this.batchTransaction.commit();

		/*---------- Step 2. 系統換日過帳(含年初損益類結轉) ----------*/

		// end
		return null;
	}

	private void LoanRateChange2Routine() throws LogicException {
		this.info("   LoanRateChange2Routine ActfgSuprele" + titaVo.isActfgSuprele());
		this.info("   titaVo.isActfgSuprele()    = " + titaVo.isActfgSuprele());
		if (titaVo.isActfgSuprele()) {
			this.info("return");
			return;
		}
		wkStartDate = 0;
		wkEffectDate = 0;
		if (titaVo.isHcodeNormal() || titaVo.isHcodeModify()) {
			this.info("lFacProdStepRate = " + lFacProdStepRate);
			for (FacProdStepRate tFacProdStepRate : lFacProdStepRate) {
				// 利率起日
				dDateUtil.init();
				dDateUtil.setMons(tFacProdStepRate.getMonthStart() - 1);
				dDateUtil.setDate_1(tFacMain.getFirstDrawdownDate()); // 階梯式利率月份以額度初貸日為準
				wkStartDate = dDateUtil.getCalenderDay();
				// 利率起日、利率起日，小於撥款日

				// 利率起日小於撥款日=> 撥款日為生效日；否則利率起日為生效日
				if (wkStartDate <= tLoanBorMain.getDrawdownDate()) {
					wkEffectDate = tLoanBorMain.getDrawdownDate();
				} else {
					wkEffectDate = wkStartDate;
				}
				SetLoanRateChange2(tFacProdStepRate);
				this.info("tFacProdStepRate = " + tFacProdStepRate);
				this.info("wkStartDate = " + wkStartDate);
				this.info("iDrawdownDate = " + tLoanBorMain.getDrawdownDate());
				this.info("tLoanRateChange.getEffectDate() = " + tLoanRateChange.getEffectDate());
				LoanRateChange t1LoanRateChange = loanRateChangeService.holdById(tLoanRateChangeId, titaVo);
				if (t1LoanRateChange == null) {
					try {
						loanRateChangeService.insert(tLoanRateChange);
					} catch (DBException e) {
						throw new LogicException(titaVo, "E0005", "放款利率變動檔 " + e.getErrorMsg()); // 新增資料時，發生錯誤
					}
				} else {
					try {
						loanRateChangeService.update(tLoanRateChange);
					} catch (DBException e) {
						throw new LogicException(titaVo, "E0007", "放款利率變動檔 " + e.getErrorMsg()); // 新增資料時，發生錯誤
					}

				}
			}
		}
	}

	private void SetLoanRateChange2(FacProdStepRate mFacProdStpeRate) throws LogicException {
		this.info("SetLoanRateChange2 ...");

		// 查詢指標利率檔
		CdBaseRate tCdBaseRate = cdBaseRateService.baseRateCodeDescFirst(tLoanBorMain.getCurrencyCode(), tFacMain.getBaseRateCode(), 10101, wkEffectDate + 19110000);
		if (tCdBaseRate == null) {
			throw new LogicException(titaVo, "E0001", "指標利率檔"); // 查詢資料不存在
		}

		tLoanRateChangeId = new LoanRateChangeId();
		tLoanRateChangeId.setCustNo(tLoanBorMain.getCustNo());
		tLoanRateChangeId.setFacmNo(tLoanBorMain.getFacmNo());
		tLoanRateChangeId.setBormNo(wkBormNo);
		tLoanRateChangeId.setEffectDate(wkEffectDate);
		tLoanRateChange = new LoanRateChange();
		tLoanRateChange.setCustNo(tLoanBorMain.getCustNo());
		tLoanRateChange.setFacmNo(tLoanBorMain.getFacmNo());
		tLoanRateChange.setBormNo(wkBormNo);
		tLoanRateChange.setEffectDate(wkEffectDate);
		tLoanRateChange.setLoanRateChangeId(tLoanRateChangeId);
		tLoanRateChange.setStatus(0);
		// 指標利率代碼與額度檔相同(01: 保單分紅利率 02: 中華郵政二年期定儲機動利率 99: 自訂利率)
		tLoanRateChange.setBaseRateCode(tFacMain.getBaseRateCode());
		// RateCode 利率區分 (1:機動 2:固定 3:定期機動)，機動的固定利率與撥款主檔不同 (例如：郵局機動利率)
		// RateType 利率型態 1: 固定利率 2: 加碼利率
		if ("1".equals(tLoanBorMain.getRateCode()) && "1".equals(mFacProdStpeRate.getRateType())) {
			tLoanRateChange.setRateCode("2"); // 固定
		} else {
			tLoanRateChange.setRateCode(tLoanBorMain.getRateCode()); // 與撥款主檔相同
		}
		// 固定利率
		if ("1".equals(mFacProdStpeRate.getRateType())) {
			tLoanRateChange.setFitRate(mFacProdStpeRate.getRateIncr());
			tLoanRateChange.setRateIncr(new BigDecimal(0));
			tLoanRateChange.setIndividualIncr(new BigDecimal(0));
		} else
		// 加碼利率
		{
			if (tFacProd.getIncrFlag().equals("Y")) {
				tLoanRateChange.setRateIncr(mFacProdStpeRate.getRateIncr());
				tLoanRateChange.setIndividualIncr(new BigDecimal(0));
			} else {
				tLoanRateChange.setRateIncr(mFacProdStpeRate.getRateIncr());
				tLoanRateChange.setIndividualIncr(mFacProdStpeRate.getRateIncr());
			}
			tLoanRateChange.setFitRate(tCdBaseRate.getBaseRate().add(mFacProdStpeRate.getRateIncr()));
		}
		tLoanRateChange.setIncrFlag(tFacProd.getIncrFlag());
		tLoanRateChange.setProdNo(tFacMain.getProdNo());
		tLoanRateChange.setRemark("");
		tLoanRateChange.setAcDate(titaVo.getEntDyI());
		tLoanRateChange.setTellerNo(titaVo.getTlrNo());
		tLoanRateChange.setTxtNo(titaVo.getTxtNo());
	}

}