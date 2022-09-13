package com.st1.itx.trade.L3;

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
import com.st1.itx.db.domain.LoanCheque;
import com.st1.itx.db.service.AcReceivableService;
import com.st1.itx.db.service.CustMainService;
import com.st1.itx.db.service.LoanChequeService;
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.parse.Parse;

/**
 * L3009 支票明細資料查詢-全部
 * 
 * @author iza
 * @version 1.0.0
 */
@Service("L3009")
@Scope("prototype")
public class L3009 extends TradeBuffer {

	/* DB服務注入 */
	@Autowired
	public LoanChequeService loanChequeService;
	@Autowired
	public AcReceivableService acReceivableService;
	@Autowired
	public CustMainService custMainService;

	@Autowired
	Parse parse;

	private int wkTotalCount = 0;
	private OccursList occursList;
	private Slice<LoanCheque> slLoanCheque;
	private List<LoanCheque> lLoanCheque;

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active L3009 ");
		this.totaVo.init(titaVo);
		int iChequeDateEd = 9991231;

		// 取得輸入資料
		int iChequeDateSt = this.parse.stringToInteger(titaVo.getParam("ChequeDateSt"));
		if (parse.stringToInteger(titaVo.getParam("ChequeDateEd")) != 0) {
			iChequeDateEd = this.parse.stringToInteger(titaVo.getParam("ChequeDateEd"));
		}
		int iChequeAcct = this.parse.stringToInteger(titaVo.getParam("ChequeAcct"));
		int iChequeAcctEd = 999999999;
		if (iChequeAcct > 0) {
			iChequeAcctEd = iChequeAcct;
		}
		int iChequeNo = this.parse.stringToInteger(titaVo.getParam("ChequeNo"));
		int iChequeNoEd = 999999999;
		if (iChequeNo > 0) {
			iChequeNoEd = iChequeNo;
		}
		String iStatusCode = titaVo.getParam("StatusCode");

		// 設定第幾分頁 titaVo.getReturnIndex() 第一次會是0，如果需折返最後會塞值
		this.index = titaVo.getReturnIndex();

		// 設定每筆分頁的資料筆數 預設500筆 總長不可超過六萬
		this.limit = 100; // 153 * 300 = 45900

		this.info("	ChequeDateSt	=" + iChequeDateSt + 19110000);
		this.info("	ChequeDateEd	=" + iChequeDateEd + 19110000);
		this.info("	ChequeAcct		=" + iChequeAcct);
		this.info("	iChequeAcctEd		=" + iChequeAcctEd);
		this.info("	ChequeNo		=" + iChequeNo);
		this.info("	iChequeNoEd		=" + iChequeNoEd);
		this.info("	StatusCode		=" + iStatusCode);
		// 查詢放款主檔
		if (!iStatusCode.isEmpty()) {
			this.info("有輸入支票狀況");
			slLoanCheque = loanChequeService.forStatusCodeSelect(iChequeDateSt + 19110000, iChequeDateEd + 19110000, iChequeAcct, iChequeAcctEd, iChequeNo, iChequeNoEd, iStatusCode, this.index,
					this.limit, titaVo);
		} else {
			this.info("無輸入支票狀況");
			slLoanCheque = loanChequeService.chequeDateRange(iChequeDateSt + 19110000, iChequeDateEd + 19110000, iChequeAcct, iChequeAcctEd, iChequeNo, iChequeNoEd, this.index, this.limit, titaVo);
		}

		lLoanCheque = slLoanCheque == null ? null : slLoanCheque.getContent();
		if (lLoanCheque == null || lLoanCheque.size() == 0) {
			throw new LogicException(titaVo, "E0001", "支票檔"); // 查詢資料不存在
		}
		for (LoanCheque tLoanCheque : lLoanCheque) {
			occursList = new OccursList();
			occursList.putParam("OOChequeDate", tLoanCheque.getChequeDate());
			occursList.putParam("OOChequeAcct", tLoanCheque.getChequeAcct());
			occursList.putParam("OOChequeNo", tLoanCheque.getChequeNo());
			occursList.putParam("OOCurrencyCode", tLoanCheque.getCurrencyCode());
			occursList.putParam("OOChequeAmt", tLoanCheque.getChequeAmt());
			occursList.putParam("OOCustNo", tLoanCheque.getCustNo());
			occursList.putParam("OOStatusCode", tLoanCheque.getStatusCode());
			occursList.putParam("OOReceiveDate", tLoanCheque.getReceiveDate());
			
			// 查詢客戶資料主檔
			CustMain tCustMain = custMainService.custNoFirst(tLoanCheque.getCustNo(), tLoanCheque.getCustNo(), titaVo);
			if (tCustMain == null) {
				occursList.putParam("OOCustNoX", "");
			} else {
				occursList.putParam("OOCustNoX", tCustMain.getCustName());
			}

			// 將每筆資料放入Tota的OcList
			this.totaVo.addOccursList(occursList);
			wkTotalCount++;
		}

		if (wkTotalCount == 0) {
			throw new LogicException(titaVo, "E0001", "支票檔"); // 查詢資料不存在
		}
		// 如果有下一分頁 會回true 並且將分頁設為下一頁 如需折返如下 不須折返 直接再次查詢即可
		if (slLoanCheque != null && slLoanCheque.hasNext()) {
			titaVo.setReturnIndex(this.setIndexNext());
			/* 手動折返 */
			this.totaVo.setMsgEndToEnter();
		}

		this.addList(this.totaVo);
		return this.sendList();
	}

}