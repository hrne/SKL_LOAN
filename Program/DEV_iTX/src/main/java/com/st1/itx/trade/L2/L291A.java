package com.st1.itx.trade.L2;

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
import com.st1.itx.db.domain.FacShareAppl;
import com.st1.itx.db.service.CustMainService;
import com.st1.itx.db.service.FacMainService;
import com.st1.itx.db.service.FacShareApplService;
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.common.LoanAvailableAmt;
import com.st1.itx.util.parse.Parse;

/*
 * L291A 共用額度登錄
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
 * L291A 共用額度查詢
 * 
 * @author iza
 * @version 1.0.0
 */
@Service("L291A")
@Scope("prototype")
public class L291A extends TradeBuffer {

	/* DB服務注入 */
	@Autowired
	public FacShareApplService facShareApplService;
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
		this.info("active L291A ");
		this.info("   titaVo.getHsupCode() = " + titaVo.getHsupCode());

		loanAvailableAmt.setTxBuffer(this.txBuffer);
		this.totaVo.init(titaVo);

		// 取得輸入資料
		int iApplNo = this.parse.stringToInteger(titaVo.getParam("ApplNo"));
		FacShareAppl tFacShareAppl = facShareApplService.findById(iApplNo);
		if (tFacShareAppl == null) {
			throw new LogicException(titaVo, "E0001", "共同借款人資料檔 核准號碼 = " + iApplNo); // 查詢資料不存在
		}

		Slice<FacShareAppl> slFacShareAppl = facShareApplService.findMainApplNo(tFacShareAppl.getMainApplNo(), 0,
				Integer.MAX_VALUE, titaVo);
		List<FacShareAppl> lFacShareAppl = slFacShareAppl == null ? null : slFacShareAppl.getContent();

		if (lFacShareAppl != null) {
			for (FacShareAppl t : lFacShareAppl) {
				FacMain tFacMain = facMainService.findById(new FacMainId(t.getCustNo(), t.getFacmNo()), titaVo);
				if (tFacMain == null) {
					throw new LogicException(titaVo, "E0001", "額度主檔" + t.getCustNo() + "-" + t.getFacmNo()); // 查詢資料不存在
				}
//		 戶號 核准號碼 案件編號  動支期限 幣別  核准額度  已動用額度餘額 目前餘額 循環動用 是否合併申報
				OccursList occursList = new OccursList();
				occursList.putParam("OOApplNo", t.getApplNo());
				occursList.putParam("OOCustNo", t.getCustNo());
				occursList.putParam("OOFacmNo", t.getFacmNo());
				CustMain tCustMain = custMainService.custNoFirst(t.getCustNo(), t.getCustNo(), titaVo);
				if (tCustMain == null) {
					occursList.putParam("OOCustName", "");
				} else {
					occursList.putParam("OOCustName", tCustMain.getCustName());
				}
				occursList.putParam("OOCaseNo", tFacMain.getCreditSysNo());

				if (tFacMain.getRecycleCode().equals("1")) {
					occursList.putParam("OODeadline", tFacMain.getRecycleDeadline());
					occursList.putParam("OORecycleCode", "Y");// 循環動用 1-循環動用
				} else {
					occursList.putParam("OODeadline", tFacMain.getUtilDeadline());
					occursList.putParam("OORecycleCode", "");// 循環動用 0-非循環動用
				}
				occursList.putParam("OOCurrencyCode", tFacMain.getCurrencyCode());
				occursList.putParam("OOLineAmt", tFacMain.getLineAmt());
				occursList.putParam("OOUtilBal", tFacMain.getUtilBal());
				occursList.putParam("OOUtilAmt", tFacMain.getUtilAmt());
				occursList.putParam("OOJcicMergeFlag", t.getJcicMergeFlag());
				// 將每筆資料放入Tota的OcList
				this.totaVo.addOccursList(occursList);
			}
		}

		this.addList(this.totaVo);
		return this.sendList();
	}
}