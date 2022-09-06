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
import com.st1.itx.db.domain.LoanSynd;
import com.st1.itx.db.service.LoanSyndService;
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.common.LoanCom;
import com.st1.itx.util.parse.Parse;

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
	Parse parse;
	@Autowired
	LoanCom loanCom;

	private OccursList occursList;
	private Slice<LoanSynd> slLoanSynd;
	private List<LoanSynd> lLoanSynd;

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active L2060 ");
		this.totaVo.init(titaVo);
		loanCom.setTxBuffer(this.txBuffer);

		// 取得輸入資料
		String iLeadingBank = titaVo.getParam("LeadingBank").trim() + "%";
		int iSyndNoStart = this.parse.stringToInteger(titaVo.getParam("SyndNoSt"));
		int iSyndNoEnd = this.parse.stringToInteger(titaVo.getParam("SyndNoEnd"));
		int iSigningDateStart = this.parse.stringToInteger(titaVo.getParam("SigningDateStart"));
		int iSigningDateEnd = this.parse.stringToInteger(titaVo.getParam("SigningDateEnd"));

		// 設定第幾分頁 titaVo.getReturnIndex() 第一次會是0，如果需折返最後會塞值
		this.index = titaVo.getReturnIndex();

		// 設定每筆分頁的資料筆數 預設500筆 總長不可超過六萬
		this.limit = 100; // 183 * 250 = 45750
		if (iSigningDateEnd == 0) {
			iSigningDateEnd = 99999999;
		} else {
			iSigningDateEnd = iSigningDateEnd + 19110000;
		}
		// 查詢聯貸案主檔 聯貸案編號區間
		if (iSyndNoStart > 0) {
			slLoanSynd = loanSyndService.syndNoRange(iSyndNoStart, iSyndNoEnd, iLeadingBank, iSigningDateStart + 19110000, iSigningDateEnd, this.index, this.limit, titaVo);
		} else {

		}

		lLoanSynd = slLoanSynd == null ? null : slLoanSynd.getContent();
		if (lLoanSynd == null || lLoanSynd.size() == 0) {
			throw new LogicException(titaVo, "E0001", "聯貸案訂約檔"); // 查詢資料不存在
		}
		for (LoanSynd ln : lLoanSynd) {
			occursList = new OccursList();

			occursList.putParam("OOSyndNo", ln.getSyndNo());
			occursList.putParam("OOSyndName", ln.getSyndName());
			occursList.putParam("OOLeadingBank", ln.getLeadingBank());
			occursList.putParam("OOSigningDate", ln.getSigningDate());
			occursList.putParam("OOCurrencyCode", ln.getCurrencyCode());
			occursList.putParam("OOSyndAmt", ln.getSyndAmt());
			occursList.putParam("OOPartAmt", ln.getPartAmt());
			occursList.putParam("OOSyndTypeCode", ln.getSyndTypeCodeFlag());

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