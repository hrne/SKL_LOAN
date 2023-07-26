package com.st1.itx.trade.BS;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Component;

import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.TempVo;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.dataVO.TotaVo;
import com.st1.itx.db.domain.LoanBorMain;
import com.st1.itx.db.domain.TxToDoDetail;
import com.st1.itx.db.service.AcReceivableService;
import com.st1.itx.db.service.LoanBorMainService;
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.common.BaTxCom;
import com.st1.itx.util.common.TxToDoCom;
import com.st1.itx.util.date.DateUtil;
import com.st1.itx.util.parse.Parse;

@Component("BS010")
@Scope("prototype")

/**
 * 新增應處理明細－放款轉列催收 <br>
 * 逾期放款應於清償期屆滿六個月內轉入「催收款項」(前三營業日寫入應處理清單)<br>
 * 執行時機：日始作業，應處理清單維護(BS001)自動執行 <br>
 * 
 * @author Lai
 * @version 1.0.0
 */

public class BS010 extends TradeBuffer {

	/* 轉型共用工具 */
	@Autowired
	public Parse parse;
	/* 日期工具 */
	@Autowired
	public DateUtil dateUtil;

	@Autowired
	public LoanBorMainService loanBorMainService;

	@Autowired
	public AcReceivableService acReceivableService;

	@Autowired
	public TxToDoCom txToDoCom;
	@Autowired
	public BaTxCom baTxCom;

	int custNo = 0;
	int facmNo = 0;
	int ovduMonth = 0; // 逾期日期-月
	int ovduDay = 0; // 逾期日期-日
	private BigDecimal ovduPrinAmt = BigDecimal.ZERO; // 轉催收本金
	private BigDecimal ovduIntAmt = BigDecimal.ZERO; // 轉催收利息
	private BigDecimal ovduFeeAmt = BigDecimal.ZERO; // 轉催收費用
	private BigDecimal ovduAmt = BigDecimal.ZERO; // 轉催收金額

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("BS010 ......");

		txToDoCom.setTxBuffer(this.txBuffer);

		// 逾期放款應於清償期屆滿六個月內轉入「催收款項」(前三營業日寫入應處理清單)
		procLoanOverdue(titaVo);
		this.batchTransaction.commit();

		this.batchTransaction.commit();

		return null;
	}

	/* 逾期放款應於清償期屆滿六個月內轉入「催收款項」(前三營業日寫入應處理清單) */
	private void procLoanOverdue(TitaVo titaVo) throws LogicException {
		this.info("procLoanOverdue ...");
		// 取得前三營業日
		int iPayDate = this.getTxBuffer().getMgBizDate().getTbsDy();
		int count = 0;
		do {
			dateUtil.init();
			dateUtil.setDate_1(iPayDate);
			dateUtil.setDays(1);
			iPayDate = dateUtil.getCalenderDay();
			dateUtil.init();
			dateUtil.setDate_2(iPayDate);
			if (!dateUtil.isHoliDay()) {
				count++;
				this.info("cont=" + count);
			}
		} while (count < 2);

		dateUtil.init();
		dateUtil.setDate_1(iPayDate);
		dateUtil.setMons(-6);
		iPayDate = dateUtil.getCalenderDay();
		this.info("應繳息日nextPayIntDate+ <= " + iPayDate);
		baTxCom.setTxBuffer(this.getTxBuffer());

		// find data
		Slice<LoanBorMain> slLoanBorMain = loanBorMainService.nextPayIntDateRange(0, iPayDate + 19110000, 0, this.index,
				Integer.MAX_VALUE);
		List<LoanBorMain> lLoanBorMain = slLoanBorMain == null ? null : slLoanBorMain.getContent();
		// size > 0 -> 新增應處理明細
		TxToDoDetail tTxToDoDetail;
		if (lLoanBorMain != null) {
			for (LoanBorMain b : lLoanBorMain) {
				if (custNo == b.getCustNo() && facmNo == b.getFacmNo()) {
					continue;
				}
				settingUnPaid(b.getCustNo(), b.getFacmNo(), b.getNextPayIntDate(), titaVo);
				tTxToDoDetail = new TxToDoDetail();
				tTxToDoDetail.setItemCode("TRLN00"); // 放款轉列催收
				tTxToDoDetail.setCustNo(b.getCustNo());
				tTxToDoDetail.setFacmNo(b.getFacmNo());
				TempVo tTempVo = new TempVo();
				tTempVo.putParam("OvduMonth", ovduMonth);
				tTempVo.putParam("OvduDay", ovduDay);
				tTempVo.putParam("OvduPrinAmt", ovduPrinAmt);
				tTempVo.putParam("OvduIntAmt", ovduIntAmt);
				tTempVo.putParam("OvduFeeAmt", ovduFeeAmt);
				tTempVo.putParam("OvduAmt", ovduAmt);
				tTxToDoDetail.setProcessNote(tTempVo.getJsonString());
				txToDoCom.addDetail(true, 0, tTxToDoDetail, titaVo); // DupSkip = true ->重複跳過
				custNo = b.getCustNo();
				facmNo = b.getFacmNo();
			}
		}

	}

	private void settingUnPaid(int iCustno, int iFacmNo, int payIntDate, TitaVo titaVo) throws LogicException {
		int tbsdy = this.txBuffer.getTxCom().getTbsdy();
		ovduMonth = 0; // 逾期日期-月
		ovduDay = 0; // 逾期日期-日
		ovduPrinAmt = BigDecimal.ZERO; // 轉催收本金
		ovduIntAmt = BigDecimal.ZERO; // 轉催收利息
		ovduFeeAmt = BigDecimal.ZERO; // 轉催收費用
		ovduAmt = BigDecimal.ZERO; // 轉催收金額
		try {
			baTxCom.settingUnPaid(tbsdy, iCustno, iFacmNo, 0, 3, BigDecimal.ZERO, titaVo); // 3-結案
		} catch (LogicException e) {
			this.info("ErrorMsg :" + e.getErrorMsg(titaVo) + " " + iCustno + "-" + iFacmNo);
		}
		// 轉催收本金 = 本金
		// 轉催收利息 = 短繳利息 + 利息
		ovduPrinAmt = baTxCom.getPrincipal();
		ovduIntAmt = baTxCom.getInterest().add(baTxCom.getShortfallInterest());
		ovduAmt = ovduPrinAmt.add(ovduIntAmt).add(ovduFeeAmt);
		// 應繳日至本日
		if (payIntDate > 0) {
			dateUtil.init();
			dateUtil.setDate_1(payIntDate);
			dateUtil.setDate_2(tbsdy);
			dateUtil.dateDiff();
			ovduMonth = dateUtil.getMons(); // 逾期期數
			ovduDay = dateUtil.getDays(); // 逾期天數
		}
	}

}