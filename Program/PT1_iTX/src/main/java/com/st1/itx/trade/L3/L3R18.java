package com.st1.itx.trade.L3;

import java.math.BigDecimal;
import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;

import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.OccursList;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.dataVO.TotaVo;
import com.st1.itx.db.domain.LoanBorTx;
import com.st1.itx.db.domain.LoanFacTmp;
import com.st1.itx.db.service.LoanBorTxService;
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.common.BaTxCom;
import com.st1.itx.util.date.DateUtil;
import com.st1.itx.util.parse.Parse;

@Service("L3R18")
@Scope("prototype")
/**
 * 
 * 
 * @author Mata
 * @version 1.0.0
 */
public class L3R18 extends TradeBuffer {

	/* DB服務注入 */

	@Autowired
	public LoanBorTxService loanBorTxService;
	@Autowired
	public BaTxCom baTxCom;

	/* 日期工具 */
	@Autowired
	public DateUtil dateUtil;

	/* 轉換工具 */
	@Autowired
	public Parse parse;

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active L3R18");
		this.totaVo.init(titaVo);

		// 戶號
		int iCustNo = parse.stringToInteger(titaVo.getParam("RimCustNo"));
		// 會計日期
		int iAcDate = parse.stringToInteger(titaVo.getParam("RimAcDate"));
		// 經辦
		String iTellerNo = titaVo.getParam("RimTellerNo");
		// 交易序號
		String iTxtNo = titaVo.getParam("RimTxtNo");

		// new table
		LoanFacTmp tLoanFacTmp = new LoanFacTmp();

		Slice<LoanBorTx> sloanBorTx = loanBorTxService.custNoTxtNoEq(iCustNo, iAcDate + 19110000, "0000", iTellerNo,
				iTxtNo, 0, Integer.MAX_VALUE, titaVo);
		// 查無資料error
		if (sloanBorTx == null) {
			throw new LogicException(titaVo, "E0001", "撥款內容檔"); // 查詢資料不存在
		}

		int wkIntStartDate = 99991231;
		int wkIntEndDate = 0;
		int wkEntryDate = 0;
		BigDecimal wkPrincipal = BigDecimal.ZERO;
		BigDecimal wkInterest = BigDecimal.ZERO;
		BigDecimal wkDelayInt = BigDecimal.ZERO;
		BigDecimal wkBreachAmt = BigDecimal.ZERO;
		BigDecimal wkExtraCloseBreachAmt = BigDecimal.ZERO;
		BigDecimal wkShortfall = BigDecimal.ZERO;
		BigDecimal wkExcessive = BigDecimal.ZERO;
		BigDecimal wkOverShort = BigDecimal.ZERO;

		for (LoanBorTx t : sloanBorTx.getContent()) {
			if (t.getFeeAmt().compareTo(BigDecimal.ZERO) > 0) {
				continue;
			}
			if (!t.getTitaHCode().equals("0")) {
				throw new LogicException(titaVo, "E0015", "訂正別非正常狀態"); // 檢查錯誤
			}
			if (t.getEntryDate() != 0) {
				wkEntryDate = t.getEntryDate();
			}
			if (t.getIntStartDate() > 0) {
				wkIntStartDate = t.getIntStartDate() < wkIntStartDate ? t.getIntStartDate() : wkIntStartDate;
			}
			if (t.getIntEndDate() > 0) {
				wkIntEndDate = t.getIntEndDate() > wkIntEndDate ? t.getIntEndDate() : wkIntEndDate;
			}
			wkPrincipal = wkPrincipal.add(t.getPrincipal());// 本金
			wkInterest = wkInterest.add(t.getInterest());// 利息
			wkDelayInt = wkDelayInt.add(t.getDelayInt());// 延遲息
			wkBreachAmt = wkBreachAmt.add(t.getBreachAmt()); // 違約金
			wkExtraCloseBreachAmt = wkExtraCloseBreachAmt.add(t.getCloseBreachAmt()); // 清償違約金
			wkShortfall = wkShortfall.add(t.getShortfall()); // 累短收
			wkExcessive = wkExcessive.add(t.getTempAmt()); // 溢短收
			wkOverShort = wkOverShort.add(t.getOverflow().subtract(t.getUnpaidCloseBreach())
					.subtract(t.getUnpaidInterest()).subtract(t.getUnpaidPrincipal())); // 溢短收
			OccursList occursList = new OccursList();
			occursList.putParam("L3r18FacmNo", t.getFacmNo());
			occursList.putParam("L3r18BormNo", t.getBormNo());
			occursList.putParam("L3r18IntStartDate", t.getIntStartDate());
			occursList.putParam("L3r18IntEndDate", t.getIntEndDate());
			occursList.putParam("L3r18Principal", t.getPrincipal());
			occursList.putParam("L3r18Interest", t.getInterest());
			occursList.putParam("L3r18DelayInt", t.getDelayInt());
			occursList.putParam("L3r18BreachAmt", t.getBreachAmt());
			occursList.putParam("L3r18Total",
					t.getPrincipal().add(t.getInterest().add(t.getDelayInt().add(t.getBreachAmt()))));
			this.totaVo.addOccursList(occursList);
		}
		this.totaVo.putParam("L3r18IntStartDate", wkIntStartDate);
		this.totaVo.putParam("L3r18IntEndDate", wkIntEndDate);
		this.totaVo.putParam("L3r18EntryDate", wkEntryDate);
		this.totaVo.putParam("L3r18Principal", wkPrincipal);
		this.totaVo.putParam("L3r18Interest", wkInterest);
		this.totaVo.putParam("L3r18DelayInt", wkDelayInt);
		this.totaVo.putParam("L3r18BreachAmt", wkBreachAmt);
		this.totaVo.putParam("L3r18ExtraCloseBreachAmt", wkExtraCloseBreachAmt);
		this.totaVo.putParam("L3r18Shortfall", wkShortfall);
		this.totaVo.putParam("L3r18Excessive", wkExcessive);
		this.totaVo.putParam("L3r18OverShort", wkOverShort);
		
		this.addList(this.totaVo);
		return this.sendList();
	}
}