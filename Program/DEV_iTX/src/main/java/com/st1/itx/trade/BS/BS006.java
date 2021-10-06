package com.st1.itx.trade.BS;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Component;

import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.dataVO.TotaVo;

import com.st1.itx.db.domain.LoanCheque;
import com.st1.itx.db.domain.TxToDoDetail;
import com.st1.itx.db.service.LoanChequeService;
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.common.TxToDoCom;
import com.st1.itx.util.date.DateUtil;
import com.st1.itx.util.parse.Parse;

/**
 * 新增應處理明細－支票兌現檢核 <br>
 * 執行時機：日始作業，系統換日後(BS006執行)自動執行<br>
 * 1.BS006 日始作業，系統換日後自動執行 <br>
 * 2.L4200 支票兌現檔上傳後自動執行<br>
 * 
 * @author Lai
 * @version 1.0.0
 */

@Component("BS006")
@Scope("prototype")
public class BS006 extends TradeBuffer {

	/* 轉型共用工具 */
	@Autowired
	public Parse parse;
	/* 日期工具 */
	@Autowired
	public DateUtil dateUtil;

	@Autowired
	public TxToDoCom txToDoCom;

	@Autowired
	public LoanChequeService loanChequeService;

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active BS006 ......");

		txToDoCom.setTxBuffer(this.txBuffer);

		/* 支票兌現檢核，逾期未兌現時(本埠：到期日+一營業日 外埠：到期日+兩營業日)寫入應處理清單 */
		txToDoCom.setTxBuffer(this.txBuffer);
		// 刪除該項目
		txToDoCom.delByItemCode("CHCK00", titaVo);
		
		// 前一營業日
		int LbsDy = this.txBuffer.getMgBizDate().getLbsDy();
		List<String> lStatus = new ArrayList<String>();
		lStatus.add("0"); // 0: 未處理

		Slice<LoanCheque> slLoanCheque = loanChequeService.statusCodeRange(lStatus, 0, LbsDy + 19110000, this.index,
				this.limit, titaVo);
		if (slLoanCheque != null) {
			for (LoanCheque c : slLoanCheque.getContent()) {
				int cashDate = getCashDate(c, titaVo);
				// OutsideCode 本埠外埠 1: 本埠 2: 外埠
				if (cashDate <= this.txBuffer.getMgBizDate().getTbsDy()) {
					TxToDoDetail tTxToDoDetail = new TxToDoDetail();
					tTxToDoDetail.setItemCode("CHCK00"); // 支票兌現檢核
					tTxToDoDetail.setCustNo(c.getCustNo());
					tTxToDoDetail.setDtlValue(c.getChequeAcct() + " " + c.getChequeNo()); // 支票帳號 + 支票號碼
					if ("1".equals(c.getOutsideCode())) {
						tTxToDoDetail.setProcessNote("本埠票未兌現 ，到期日=" + c.getChequeDate() + "，應兌現日=" + cashDate);
					} else {
						tTxToDoDetail.setProcessNote("外埠票未兌現 ，到期日=" + c.getChequeDate() + "，應兌現日=" + cashDate);
					}
					txToDoCom.addDetail(true, 0, tTxToDoDetail, titaVo); // DupSkip = true ->重複跳過
				}
			}
		}
		return null;
	}

	private int getCashDate(LoanCheque c, TitaVo titaVo) throws LogicException {
		int bussCnt = 0;
		dateUtil.init();
		dateUtil.setDate_2(c.getChequeDate());
		if (dateUtil.isHoliDay()) {
			bussCnt++;
		}
		bussCnt++;
		if ("2".equals(c.getOutsideCode())) {
			bussCnt++;
		}
		dateUtil.init();
		int caseDate = dateUtil.getbussDate(c.getChequeDate(), bussCnt);
		return caseDate;

	}
}