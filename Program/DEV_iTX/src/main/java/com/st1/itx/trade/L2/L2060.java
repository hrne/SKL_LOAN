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
import com.st1.itx.db.domain.LoanSynd;
import com.st1.itx.db.service.CustMainService;
import com.st1.itx.db.service.LoanSyndService;
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.common.LoanCom;
import com.st1.itx.util.parse.Parse;

/*
 * Tita
 * TimCustNo=9,7
 * LeadingBank=9,7
 * SigningDateStart=9,7
 * SigningDateEnd=9,7
 * DrawdownStartDateStart=9,7
 * DrawdownStartDateEnd=9,7
 * DrawdownEndDateStart=9,7
 * DrawdownEndDateEnd=9,7
 * CommitFeeFlag=X,1
 */
/**
 * L2060 聯貸案訂約明細資料查詢
 * 
 * @author iza
 * @version 1.0.0
 */
@Service("L2060")
@Scope("prototype")
public class L2060 extends TradeBuffer {
	/* DB服務注入 */
	@Autowired
	public LoanSyndService loanSyndService;
	@Autowired
	public CustMainService custMainService;

	@Autowired
	Parse parse;
	@Autowired
	LoanCom loanCom;

	private int wkTotalCount = 0;
	private OccursList occursList;
	private Slice<LoanSynd> slLoanSynd;
	private List<LoanSynd> lLoanSynd;

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active L2060 ");
		this.totaVo.init(titaVo);
		loanCom.setTxBuffer(this.txBuffer);

		// 取得輸入資料
		int iCustNo = this.parse.stringToInteger(titaVo.getParam("TimCustNo"));
		String iLeadingBank = titaVo.getParam("LeadingBank").trim() + "%";
		int iSigningDateStart = this.parse.stringToInteger(titaVo.getParam("SigningDateStart"));
		int iSigningDateEnd = this.parse.stringToInteger(titaVo.getParam("SigningDateEnd"));
		int iDrawdownStartDateStart = this.parse.stringToInteger(titaVo.getParam("DrawdownStartDateStart"));
		int iDrawdownStartDateEnd = this.parse.stringToInteger(titaVo.getParam("DrawdownStartDateEnd"));
		int iDrawdownEndDateStart = this.parse.stringToInteger(titaVo.getParam("DrawdownEndDateStart"));
		int iDrawdownEndDateEnd = this.parse.stringToInteger(titaVo.getParam("DrawdownEndDateEnd"));
//		String iCommitFeeFlag = titaVo.getParam("CommitFeeFlag");

		// work area
		int wkCustNoStart = 1;
		int wkCustNoEnd = 9999999;
		List<String> lCommitFeeFlag = new ArrayList<String>();

		if (iCustNo > 0) {
			wkCustNoStart = iCustNo;
			wkCustNoEnd = iCustNo;
		}
//		if (iCommitFeeFlag.trim().equals("")) {
//			lCommitFeeFlag.add("Y");
//			lCommitFeeFlag.add("N");
//		} else {
//			lCommitFeeFlag.add(iCommitFeeFlag);
//		}

		// 設定第幾分頁 titaVo.getReturnIndex() 第一次會是0，如果需折返最後會塞值
		this.index = titaVo.getReturnIndex();

		// 設定每筆分頁的資料筆數 預設500筆 總長不可超過六萬
		this.limit = 100; // 183 * 250 = 45750

		// 查詢放款主檔
		slLoanSynd = loanSyndService.syndCustNoRange(wkCustNoStart, wkCustNoEnd, iLeadingBank,
				iSigningDateStart + 19110000, iSigningDateEnd + 19110000, iDrawdownStartDateStart + 19110000,
				iDrawdownStartDateEnd > 0 ? iDrawdownStartDateEnd + 19110000 : 99991231,
				iDrawdownEndDateStart + 19110000, iDrawdownEndDateEnd > 0 ? iDrawdownEndDateEnd + 19110000 : 99991231,
				this.index, this.limit, titaVo);
		lLoanSynd = slLoanSynd == null ? null : slLoanSynd.getContent();
		if (lLoanSynd == null || lLoanSynd.size() == 0) {
			throw new LogicException(titaVo, "E0001", "聯貸案訂約檔"); // 查詢資料不存在
		}
		for (LoanSynd ln : lLoanSynd) {
			occursList = new OccursList();
			CustMain tCustMain = custMainService.findById(ln.getCustUKey(), titaVo);
			if (tCustMain == null) {
				throw new LogicException(titaVo, "E0001", "客戶資料主檔  客戶識別碼 = " + ln.getCustUKey()); // 查無資料
			}
			occursList.putParam("OOCustNo", ln.getCustNo());
			occursList.putParam("OOSyndNo", ln.getSyndNo());
			occursList.putParam("OOCustId", tCustMain.getCustId());
			occursList.putParam("OOCustIdX", tCustMain.getCustName());
			occursList.putParam("OOLeadingBank", ln.getLeadingBank());
			occursList.putParam("OOSigningDate", ln.getSigningDate());
			occursList.putParam("OODrawdownStartDate", ln.getDrawdownStartDate());
			occursList.putParam("OODrawdownEndDate", ln.getDrawdownEndDate());
			occursList.putParam("OOCurrencyCode", ln.getCurrencyCode());
			occursList.putParam("OOSyndAmt", ln.getSyndAmt());
			occursList.putParam("OOPartAmt", ln.getPartAmt());
			if ("Y".equals(ln.getSyndTypeCodeFlag())) {
				occursList.putParam("OOSyndTypeCode", "Y");
			} else {
				occursList.putParam("OOSyndTypeCode", "");
			}

			// 將每筆資料放入Tota的OcList
			this.totaVo.addOccursList(occursList);
		}

		// 如果有下一分頁 會回true 並且將分頁設為下一頁 如需折返如下 不須折返 直接再次查詢即可
		if (slLoanSynd != null && slLoanSynd.hasNext()) {
			titaVo.setReturnIndex(this.setIndexNext());
			/* 手動折返 */
			this.totaVo.setMsgEndToEnter();
		}

		this.addList(this.totaVo);
		return this.sendList();
	}
}