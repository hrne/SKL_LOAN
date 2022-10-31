package com.st1.itx.trade.L3;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;

import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.OccursList;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.dataVO.TotaVo;
import com.st1.itx.db.domain.FacProd;
import com.st1.itx.db.domain.LoanBorMain;
import com.st1.itx.db.domain.LoanRateChange;
import com.st1.itx.db.service.FacProdService;
import com.st1.itx.db.service.LoanBorMainService;
import com.st1.itx.db.service.LoanBorTxService;
import com.st1.itx.db.service.LoanRateChangeService;
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.parse.Parse;

/*
 * TimCustNo=9,7
 * FacmNo=9,3
 * BormNo=9,3
 */

/**
 * L3932 借戶利率查詢
 * 
 * @author iza
 * @version 1.0.0
 */
@Service("L3932")
@Scope("prototype")
public class L3932 extends TradeBuffer {

	/* DB服務注入 */
	@Autowired
	public FacProdService facProdService;
	@Autowired
	public LoanBorMainService loanBorMainService;
	@Autowired
	public LoanBorTxService loanBorTxService;
	@Autowired
	public LoanRateChangeService loanRateChangeService;

	@Autowired
	Parse parse;

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active L3932 ");
		this.totaVo.init(titaVo);

		// 取得輸入資料
		int iCustNo = this.parse.stringToInteger(titaVo.getParam("TimCustNo"));
		int iFacmNo = this.parse.stringToInteger(titaVo.getParam("FacmNo"));
		int iBormNo = this.parse.stringToInteger(titaVo.getParam("BormNo"));

		int iCustDataCtrl = this.getTxBuffer().getTxCom().getCustDataCtrl();

		// work area
		int wkFacmNoStart = 1;
		int wkFacmNoEnd = 999;
		int wkBormNoStart = 1;
		int wkBormNoEnd = 900;
		String wkChangeFg = "Y";

		BigDecimal wkBaseRate = BigDecimal.ZERO;
		BigDecimal wkFitRate = BigDecimal.ZERO;
		FacProd tFacProd = new FacProd();

		List<LoanRateChange> lLoanRateChange;

		if (iFacmNo > 0) {
			wkFacmNoStart = iFacmNo;
			wkFacmNoEnd = iFacmNo;
		}

		if (iBormNo > 0) {
			wkBormNoStart = iBormNo;
			wkBormNoEnd = iBormNo;
		}

		int effectAdjDate = 0;
		int effectPaidDate = 0;
		int effectBatchDate = 0;

		Slice<LoanBorMain> slLoanBorMain = loanBorMainService.bormCustNoEq(iCustNo, wkFacmNoStart, wkFacmNoEnd,
				wkBormNoStart, wkBormNoEnd, 0, Integer.MAX_VALUE, titaVo);
		List<LoanBorMain> lLoanBorMain = slLoanBorMain == null ? null
				: new ArrayList<LoanBorMain>(slLoanBorMain.getContent());
		if (lLoanBorMain == null || lLoanBorMain.size() == 0) {
			throw new LogicException(titaVo, "E0001", "放款主檔"); // 查詢資料不存在
		}

		for (LoanBorMain ln : lLoanBorMain) {

			// 上次繳息日
			effectPaidDate = ln.getPrevPayIntDate(); // 繳息日前的一筆

			// 查詢放款利率變動檔
			Slice<LoanRateChange> slLoanRateChange = loanRateChangeService.rateChangeFacmNoRange(iCustNo,
					ln.getFacmNo(), ln.getFacmNo(), ln.getBormNo(), ln.getBormNo(), 0, 99991231, this.index,
					Integer.MAX_VALUE, titaVo);
			lLoanRateChange = slLoanRateChange == null ? null : slLoanRateChange.getContent();
			if (lLoanRateChange == null || lLoanRateChange.size() == 0) {
				break;
			}

			// 上次調整利率日期
			effectAdjDate = lLoanRateChange.get(0).getEffectDate(); // 放下一次利率調整日期的前一筆
			// 已生效利率日期
			effectBatchDate = lLoanRateChange.get(0).getEffectDate(); // 整批調整最後的一筆

			for (LoanRateChange r : lLoanRateChange) {

				if (r.getEffectDate() < ln.getNextAdjRateDate()) {
					effectAdjDate = r.getEffectDate();
				}
				// 已生效利率日期 ==> 整批調整 or 小於繳息日 or 利率日期 <=本月
				if (r.getStatus() == 1 || r.getEffectDate() < ln.getPrevPayIntDate()
						|| r.getEffectDate() / 100 <= this.txBuffer.getTxCom().getTbsdy() / 100) {
					effectBatchDate = r.getEffectDate();
				}
			}
			for (LoanRateChange r : lLoanRateChange) {
				// 查詢商品參數檔
				if (tFacProd == null || tFacProd.getProdNo() != r.getProdNo()) {
					tFacProd = facProdService.findById(r.getProdNo(), titaVo);
					if (tFacProd == null) {
						throw new LogicException(titaVo, "E0001", "商品參數檔"); // 查詢資料不存在
					}
				}
// wkChangeFg
//   "Y":  繳息日後的每一筆，按鈕<變更> 僅可變更、刪除利率資料，自動顯示變更的生效日期不可改
//				
				if (r.getEffectDate() >= effectPaidDate
						&& (r.getStatus() == 2 || r.getEffectDate() > ln.getDrawdownDate())) {
					wkChangeFg = "Y";
				} else {
					wkChangeFg = " ";
				}

				if (ln.getStatus() != 0) {
					wkChangeFg = " ";
				}

				if ("99".equals(r.getBaseRateCode()) || r.getEffectDate() > effectBatchDate) {
					wkBaseRate = BigDecimal.ZERO;
				} else {
					wkBaseRate = r.getFitRate()
							.subtract(r.getIncrFlag().equals("Y") ? r.getRateIncr() : r.getIndividualIncr());
				}

				// 未生效利率
				if (!"99".equals(r.getBaseRateCode()) && r.getEffectDate() > effectBatchDate) {
					wkFitRate = BigDecimal.ZERO;
					wkBaseRate = BigDecimal.ZERO;
				} else {
					wkFitRate = r.getFitRate();
				}
				OccursList occursList = new OccursList();
				occursList.putParam("OOCustNo", r.getCustNo());

				occursList.putParam("OOFacmNo", r.getFacmNo());
				occursList.putParam("OOBormNo", r.getBormNo());
				occursList.putParam("OOEffectDate", r.getEffectDate());
				occursList.putParam("OOFitRate", wkFitRate);
				occursList.putParam("OOProdNo", r.getProdNo());
				occursList.putParam("OOProdName", tFacProd.getProdName());
				occursList.putParam("OORateCode", r.getRateCode());
				occursList.putParam("OOBaseRateCode", r.getBaseRateCode());
				occursList.putParam("OOBaseRate", wkBaseRate);
				occursList.putParam("OOProdRate", wkBaseRate.add(tFacProd.getProdIncr()));
				occursList.putParam("OORateIncr", r.getRateIncr());
				occursList.putParam("OOIndividualIncr", r.getIndividualIncr());
				// 每額度在最後一筆撥款序號的最新生效的那筆顯示下一次利率調整日期
				if (ln.getStatus() != 0) {
					occursList.putParam("OONextAdjRateDate", "");
				} else if (r.getEffectDate() == effectAdjDate) {
					occursList.putParam("OONextAdjRateDate", ln.getNextAdjRateDate());
				} else {
					occursList.putParam("OONextAdjRateDate", "");
				}
				// 未生效的指標利率放0
				occursList.putParam("OORemark", r.getRemark());
				occursList.putParam("OOChangeFg", wkChangeFg);
				occursList.putParam("OOStatus", ln.getStatus());

				// 將每筆資料放入Tota的OcList
				this.totaVo.addOccursList(occursList);
			}
		}

		this.addList(this.totaVo);
		return this.sendList();
	}
}