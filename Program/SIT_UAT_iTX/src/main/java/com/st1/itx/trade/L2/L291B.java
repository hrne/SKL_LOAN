package com.st1.itx.trade.L2;

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
import com.st1.itx.db.domain.CustMain;
import com.st1.itx.db.domain.FacMain;
import com.st1.itx.db.domain.FacMainId;
import com.st1.itx.db.domain.FacShareLimit;
import com.st1.itx.db.service.CustMainService;
import com.st1.itx.db.service.FacMainService;
import com.st1.itx.db.service.FacShareLimitService;
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.common.LoanAvailableAmt;
import com.st1.itx.util.parse.Parse;

/*
 * L291B 合併額度控管額度查詢
 */
/*
 * Tita
 * CustNo=9,7       -> MRKEY
 * CreditSysNo=9,7
 * CurrencyCode=X,3  
 * LineAmt==9,14.2 -> TXAMT
 * LineAmtCycle==9,14.2 
 * ShareCustNo=9,7
 * ShareFacmNo=9,7
 * iJcicMiainCustFlag=X,1 
 * END=X,1
 */

/**
 * L291B 合併額度控管額度查詢
 * 
 * @author iza
 * @version 1.0.0
 */
@Service("L291B")
@Scope("prototype")
public class L291B extends TradeBuffer {

	/* DB服務注入 */
	@Autowired
	public FacShareLimitService facShareLimitService;
	@Autowired
	public FacMainService facMainService;
	@Autowired
	public CustMainService custMainService;
	@Autowired
	LoanAvailableAmt loanAvailableAmt;

	@Autowired
	Parse parse;

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active L291B ");
		this.totaVo.init(titaVo);

		// 取得輸入資料
		int iApplNo = this.parse.stringToInteger(titaVo.getParam("ApplNo"));
		FacShareLimit tFacShareLimit = facShareLimitService.findById(iApplNo);
		if (tFacShareLimit == null) {
			throw new LogicException(titaVo, "E0001", "合併額度控管資料檔 = " + iApplNo); // 查詢資料不存在
		}
		Slice<FacShareLimit> slFacShareLimit = facShareLimitService.findMainApplNoEq(tFacShareLimit.getMainApplNo(), 0,
				Integer.MAX_VALUE, titaVo);
		List<FacShareLimit> lFacShareLimit = slFacShareLimit == null ? null : slFacShareLimit.getContent();

		// 已動用額度餘額
		BigDecimal wkUtilBal = BigDecimal.ZERO;

		// 目前餘額
		BigDecimal wkUtilAmt = BigDecimal.ZERO;

		// 可用總額度
		BigDecimal wkAvailable = BigDecimal.ZERO;
		BigDecimal wkAvailableCycle = BigDecimal.ZERO;

		if (lFacShareLimit != null) {
			for (FacShareLimit t : lFacShareLimit) {
				FacMain tFacMain = facMainService.findById(new FacMainId(t.getCustNo(), t.getFacmNo()), titaVo);
				if (tFacMain == null) {
					throw new LogicException(titaVo, "E0001", "額度主檔" + t.getCustNo() + "-" + t.getFacmNo()); // 查詢資料不存在
				}
				wkUtilBal = wkUtilBal.add(tFacMain.getUtilBal());
				wkUtilAmt = wkUtilAmt.add(tFacMain.getUtilAmt());
//		 主要戶號-額度 共用戶號-額度        案件編號  動支期限 幣別 核准額度  已動用額度餘額 目前餘額 可用額度 循環動用
				OccursList occursList = new OccursList();
				occursList.putParam("OOApplNo", t.getApplNo());
				occursList.putParam("OOCustNo", t.getCustNo());
				occursList.putParam("OOFacmNo", t.getFacmNo());
				CustMain tCustMain = custMainService.custNoFirst(t.getCustNo(), t.getCustNo(), titaVo);
				if (tCustMain == null) {
					throw new LogicException(titaVo, "E0001", "客戶主檔" + t.getCustNo()); // 查詢資料不存在
				}
				occursList.putParam("OOCustName", tCustMain.getCustName());
				occursList.putParam("OOCaseNo", tFacMain.getCreditSysNo());
				if (tFacMain.getRecycleCode().equals("1")) {
					occursList.putParam("OODeadline", tFacMain.getRecycleDeadline());
					occursList.putParam("OORecycleCode", "Y"); // 循環動用 1-循環動用
				} else {
					occursList.putParam("OODeadline", tFacMain.getUtilDeadline());
					occursList.putParam("OORecycleCode", ""); // 循環動用 0-非循環動用
				}
				occursList.putParam("OOCurrencyCode", tFacMain.getCurrencyCode());
				occursList.putParam("OOLineAmt", tFacMain.getLineAmt());
				occursList.putParam("OOUtilAmt", tFacMain.getUtilAmt());
				occursList.putParam("OOUtilBal", tFacMain.getUtilBal());

				// 將每筆資料放入Tota的OcList
				this.totaVo.addOccursList(occursList);
			}
		}
		// 核准金額 > 已動用額度餘額
		if (tFacShareLimit.getLineAmt().compareTo(wkUtilBal) > 0) {
			wkAvailable = tFacShareLimit.getLineAmt().subtract(wkUtilBal);
		}
		this.totaVo.putParam("OCurrencyCode", tFacShareLimit.getCurrencyCode());
		this.totaVo.putParam("OLineAmt", tFacShareLimit.getLineAmt());
		this.totaVo.putParam("OUtilBal", wkUtilBal);
		this.totaVo.putParam("OUtilAmt", wkUtilAmt);
		this.totaVo.putParam("OAvailable", wkAvailable);

		this.addList(this.totaVo);
		return this.sendList();
	}
}