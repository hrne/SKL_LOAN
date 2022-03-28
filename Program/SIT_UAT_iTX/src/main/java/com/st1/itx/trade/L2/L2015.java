package com.st1.itx.trade.L2;

import java.util.ArrayList;
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
import com.st1.itx.db.service.CustMainService;
import com.st1.itx.db.service.FacMainService;
import com.st1.itx.db.service.LoanBorMainService;
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.common.LoanCom;
import com.st1.itx.util.parse.Parse;

/*
 * L2015 額度明細資料查詢
 * a.此功能提供以戶號查詢額度之明細資料
 */

/**
 * L2015 額度明細資料查詢
 * 
 * @author iza
 * @version 1.0.0
 */
@Service("L2015")
@Scope("prototype")
public class L2015 extends TradeBuffer {

	/* DB服務注入 */
	@Autowired
	public CustMainService custMainService;
	@Autowired
	public FacMainService facMainService;
	@Autowired
	public LoanBorMainService sLoanBorMainService;
	@Autowired
	public LoanCom loanCom;

	@Autowired
	Parse parse;

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active L2015 ");
		this.totaVo.init(titaVo);

		// 取得輸入資料
		String iCustId = titaVo.getParam("CustId").trim();
		int iCustNo1 = this.parse.stringToInteger(titaVo.getParam("CustNo1"));
		int iCustNo2 = this.parse.stringToInteger(titaVo.getParam("CustNo2"));
		int iFacmNo = this.parse.stringToInteger(titaVo.getParam("FacmNo"));

		int wkCustNoSt = 0;
		int wkCustNoEd = 9999999;

		if (iCustNo1 > 0) {
			wkCustNoSt = iCustNo1;
		}
		if (iCustNo2 > 0) {
			wkCustNoEd = iCustNo2;
		}

		int wkFacmNo1;
		int wkFacmNo2;

		if (iFacmNo == 0) {
			wkFacmNo1 = 1;
			wkFacmNo2 = 999;
		} else {
			wkFacmNo1 = iFacmNo;
			wkFacmNo2 = iFacmNo;
		}

		// 設定第幾分頁 titaVo.getReturnIndex() 第一次會是0，如果需折返最後會塞值
		this.index = titaVo.getReturnIndex();

		// 設定每筆分頁的資料筆數 預設500筆 總長不可超過六萬
		this.limit = 100; // 70 * 500 = 35000

		if (!"".equals(iCustId)) {

			CustMain tCustMain = custMainService.custIdFirst(iCustId, titaVo);
			if (tCustMain != null) {
				wkCustNoSt = tCustMain.getCustNo();
				wkCustNoEd = tCustMain.getCustNo();
			} else {
				throw new LogicException(titaVo, "E2003", "客戶資料主檔"); // 查無資料
			}

		}
		// 查詢額度主檔
		Slice<FacMain> lFacMain = facMainService.facmCustNoRange(wkCustNoSt, wkCustNoEd, wkFacmNo1, wkFacmNo2,
				this.index, this.limit, titaVo);
		if (lFacMain == null || lFacMain.isEmpty()) {
			throw new LogicException(titaVo, "E2003", "額度主檔"); // 查無資料
		}

		// 如有有找到資料
		for (FacMain tFacMain : lFacMain.getContent()) {

			String dShow = "Y";
			// 該額度有撥款或預約撥款,不顯示刪除按鈕
			if (tFacMain.getLastBormNo() > 0 || tFacMain.getLastBormRvNo() > 900) {
				// hide
				dShow = "N";
			}

			OccursList occursList = new OccursList();
			occursList.putParam("OOCustNo", tFacMain.getCustNo());
			occursList.putParam("OOCustName", loanCom.getCustNameByNo(tFacMain.getCustNo()));
			occursList.putParam("OOFacmNo", tFacMain.getFacmNo());
			occursList.putParam("OOApplNo", tFacMain.getApplNo());
			occursList.putParam("OOCurrencyCode", tFacMain.getCurrencyCode());
			occursList.putParam("OOLineAmt", tFacMain.getLineAmt());
			occursList.putParam("OOApproveRate", tFacMain.getApproveRate());
			occursList.putParam("OOUtilAmt", tFacMain.getUtilAmt());
			occursList.putParam("OOUtilBal", tFacMain.getUtilBal());
			occursList.putParam("OOAcctCode", tFacMain.getAcctCode());
			occursList.putParam("OOLoanFg", dShow);
			// 將每筆資料放入Tota的OcList
			this.totaVo.addOccursList(occursList);
		}

		// 如果有下一分頁 會回true 並且將分頁設為下一頁 如需折返如下 不須折返 直接再次查詢即可
		if (lFacMain != null && lFacMain.hasNext()) {
			titaVo.setReturnIndex(this.setIndexNext());
			/* 手動折返 */
			this.totaVo.setMsgEndToEnter();
		}

		this.addList(this.totaVo);
		return this.sendList();
	}
}