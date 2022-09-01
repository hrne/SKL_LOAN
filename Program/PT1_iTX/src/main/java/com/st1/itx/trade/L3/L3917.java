package com.st1.itx.trade.L3;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;

import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.TempVo;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.dataVO.TotaVo;
import com.st1.itx.db.domain.LoanBorTx;
import com.st1.itx.db.service.LoanBorTxService;
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.common.LoanCom;
import com.st1.itx.util.parse.Parse;

@Service("L3917")
@Scope("prototype")
/**
 * 
 * 
 * @author YuJiaXing
 * @version 1.0.0
 */
public class L3917 extends TradeBuffer {
	private static final Logger logger = LoggerFactory.getLogger(L3917.class);

	/* DB服務注入 */
	@Autowired
	public LoanBorTxService loanBorTxService;

	@Autowired
	Parse parse;
	@Autowired
	LoanCom loanCom;

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active L3917 ");
		this.totaVo.init(titaVo);

		// 取得輸入資料
		int iCustNo = this.parse.stringToInteger(titaVo.getParam("CustNo"));
		int iFacmNo = this.parse.stringToInteger(titaVo.getParam("FacmNo"));
		int iIntEndDate = this.parse.stringToInteger(titaVo.getParam("IntEndDate"));
		int iAcDate = this.parse.stringToInteger(titaVo.getParam("AcDate"));
		String iTellerNo = titaVo.getParam("TellerNo");
		String iTxtNo = titaVo.getParam("TxtNo");
		// wk
		String wkCurrencyCode = "";
		BigDecimal wkRepayAmt = new BigDecimal("0");// 回收金額
		BigDecimal wkTempRepayAmt = new BigDecimal("0");// 暫收抵繳
		BigDecimal wkReduceAmt = new BigDecimal("0");// 減免金額
		BigDecimal wkPrinciPal = new BigDecimal("0");// 本金
		BigDecimal wkInterext = new BigDecimal("0");// 利息
		BigDecimal wkDelayInt = new BigDecimal("0");// 延遲息
		BigDecimal wkBreachAmt = new BigDecimal("0");// 違約金
		BigDecimal wkCloseBreachAmt = new BigDecimal("0");// 清償違約金
		BigDecimal wkShortfall = new BigDecimal("0");// 短收
		BigDecimal wkOverflow = new BigDecimal("0");// 溢收
		BigDecimal wkTempAmt = new BigDecimal("0");// 暫收金額
		BigDecimal wkTempRepay = new BigDecimal("0");// 暫收抵繳
		BigDecimal wkTxAmt = new BigDecimal("0");// 交易金額
		int entryDate = 0;
		int intStartDate = 0;

		List<LoanBorTx> lLoanBorTx;
		// 查詢放款交易內容檔

		List<String> ltitaHCode = new ArrayList<String>();
		ltitaHCode.add("0"); // 正常
		Slice<LoanBorTx> slLoanBorTx = loanBorTxService.findIntEndDateEq(iCustNo, iFacmNo, 1, 990,
				iIntEndDate + 19110000, ltitaHCode, iAcDate + 19110000, iTellerNo, iTxtNo, 0, Integer.MAX_VALUE,
				titaVo);

		lLoanBorTx = slLoanBorTx == null ? null : slLoanBorTx.getContent();
		if (lLoanBorTx == null || lLoanBorTx.size() == 0) {
			throw new LogicException(titaVo, "E0001", "放款交易內容檔"); // 查詢資料不存在
		}
		// 暫收金額(第一筆)
		wkTempAmt = lLoanBorTx.get(0).getTempAmt();
		for (LoanBorTx t : lLoanBorTx) {
			// Tota
			wkCurrencyCode = t.getTitaCurCd();
			entryDate = t.getEntryDate();
			if (intStartDate == 0 || intStartDate > t.getIntStartDate()) {
				intStartDate = t.getIntStartDate();
			}
			TempVo tTempVo = new TempVo();

			tTempVo = tTempVo.getVo(t.getOtherFields());
			wkOverflow = t.getOverflow(); // 最後一筆
			wkReduceAmt = wkReduceAmt.add(this.parse.stringToBigDecimal(tTempVo.getParam("ReduceAmt"))); // 減免金額
			wkPrinciPal = wkPrinciPal.add(t.getPrincipal()); // 本金
			wkInterext = wkInterext.add(t.getInterest()); // 利息
			wkDelayInt = wkDelayInt.add(t.getDelayInt()); // 延遲息
			wkBreachAmt = wkBreachAmt.add(t.getBreachAmt()); // 違約金
			wkCloseBreachAmt = wkCloseBreachAmt.add(t.getCloseBreachAmt()); // 清償違約金
			// 回收金額
			BigDecimal repayAmt = t.getPrincipal().add(t.getInterest()).add(t.getDelayInt()).add(t.getBreachAmt())
					.add(t.getCloseBreachAmt()); // 回收金額
			wkRepayAmt = wkRepayAmt.add(repayAmt);
			wkShortfall = wkShortfall
					.add(t.getUnpaidInterest().add(t.getUnpaidPrincipal()).add(t.getUnpaidCloseBreach())); // 短收金額
			// 交易金額
			wkTxAmt = wkTxAmt.add(t.getTxAmt());
		}
		// 交易金額(100) + 暫收借(900) = 回收金額(300) + 暫收貸(700)
		// 暫收抵繳 = (回收金額(300) - 交易金額(100) > 0
		if (wkRepayAmt.compareTo(wkTxAmt) > 0) {
			wkTempRepay = wkRepayAmt.subtract(wkTxAmt);
		}
		this.totaVo.putParam("OCustNo", iCustNo); // 戶號
		this.totaVo.putParam("OFacmNo", iFacmNo); // 額度編號
		this.totaVo.putParam("OCustName", loanCom.getCustNameByNo(iCustNo)); // 戶名
		this.totaVo.putParam("OCurrencyCode", wkCurrencyCode); // 幣別

		this.totaVo.putParam("OAcDate", iAcDate); // 會計日期
		this.totaVo.putParam("OEntryDate", entryDate); // 入帳日期
		this.totaVo.putParam("OIntStartDate", intStartDate); // 計息起日
		this.totaVo.putParam("OIntEndDate", iIntEndDate); // 計息迄日
		this.totaVo.putParam("OTellerNo", iTellerNo); // 經辦

		this.totaVo.putParam("OTellerName", loanCom.getEmpFullnameByEmpNo(iTellerNo)); // 經辦姓名
		this.totaVo.putParam("OTxtNo", iTxtNo); // 交易序號

		this.totaVo.putParam("OTxAmt", wkTxAmt); // 交易金額
		this.totaVo.putParam("ORepayAmt", wkRepayAmt); // 回收金額

		this.totaVo.putParam("OTempAmt", wkTempAmt); // 暫收金額
		this.totaVo.putParam("OTempRepay", wkTempRepay); // 暫收抵繳

		this.totaVo.putParam("OReduceAmt", wkReduceAmt); // 減免金額
		this.totaVo.putParam("OPrinciPal", wkPrinciPal); // 本金
		this.totaVo.putParam("OInterext", wkInterext); // 利息
		this.totaVo.putParam("ODelayInt", wkDelayInt); // 延遲息
		this.totaVo.putParam("OBreachAmt", wkBreachAmt); // 違約金
		this.totaVo.putParam("OCloseBreachAmt", wkCloseBreachAmt); // 清償違約金
		this.totaVo.putParam("OShortFall", wkShortfall);// 短收金額
		this.totaVo.putParam("OOverflow", wkOverflow);// 溢收金額

		this.addList(this.totaVo);
		return this.sendList();
	}
}