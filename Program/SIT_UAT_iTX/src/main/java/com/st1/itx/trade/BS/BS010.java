package com.st1.itx.trade.BS;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Component;

import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.TempVo;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.dataVO.TotaVo;
import com.st1.itx.db.domain.AcReceivable;
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
 * 新增應處理明細－轉列催收 <br>
 * 執行時機：日始作業，應處理清單維護(BS003)後自動執行 <br>
 * 1.逾期放款應於清償期屆滿六個月內轉入「催收款項」<br>
 * 2.月底日將逾三個月之火險費轉列催收<br>
 * 3.月底日將逾三個月之法務費轉列催收<br>
 * 
 * @author Lai
 * @version 1.0.0
 */

public class BS010 extends TradeBuffer {
	private static final Logger logger = LoggerFactory.getLogger(BS010.class);

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

	private int tbsDyf;

	private int mfbsDyf;
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

		// 本營業日(西元)
		this.tbsDyf = this.getTxBuffer().getMgBizDate().getTbsDyf();

		// 月底營業日(西元)
		this.mfbsDyf = this.getTxBuffer().getMgBizDate().getMfbsDyf();

		// step 1. 逾期放款應於清償期屆滿六個月內轉入「催收款項」
		procLoanOverdue(titaVo);
		this.batchTransaction.commit();

		// step 2. 月底日將逾三個月之火險費轉列催收
		if (this.tbsDyf == this.mfbsDyf) {
			procInsuFeeOverdue(titaVo);
			this.batchTransaction.commit();
		}

		// step 3. 月底日將逾三個月之法務費轉列催收
		if (this.tbsDyf == this.mfbsDyf) {
			procLawFeeOverdue(titaVo);
			this.batchTransaction.commit();
		}

		this.batchTransaction.commit();

		return null;
	}

	/* 逾期放款應於清償期屆滿六個月內轉入「催收款項」 */
	private void procLoanOverdue(TitaVo titaVo) throws LogicException {
		this.info("procLoanOverdue ...");
		int iPayDate = 0;
		// 遇假日提前
		dateUtil.init();
		dateUtil.setDate_1(this.getTxBuffer().getMgBizDate().getNbsDyf());
		dateUtil.setDays(-1);
		iPayDate = dateUtil.getCalenderDay();

		dateUtil.init();
		dateUtil.setDate_1(iPayDate);
		dateUtil.setMons(-6);
		iPayDate = dateUtil.getCalenderDay();
		this.info("應繳息日nextPayIntDate+ <= " + iPayDate);
		baTxCom.setTxBuffer(this.getTxBuffer());

		// find data
		Slice<LoanBorMain> slLoanBorMain = loanBorMainService.nextPayIntDateRange(0, iPayDate, 0, this.index,
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

	/* 月底日將逾三個月之火險費轉列催收 */
	private void procInsuFeeOverdue(TitaVo titaVo) throws LogicException {
		this.info("procInsuOverdue ...");
		List<AcReceivable> lAcReceivable = new ArrayList<AcReceivable>();
		int ym = this.getTxBuffer().getMgBizDate().getTbsDyf() / 100;
		dateUtil.init();
		dateUtil.setDate_1(ym * 100 + 01);
		dateUtil.setMons(-2);
		int payDate = dateUtil.getCalenderDay();

		this.info("火險費轉列催收日期 < " + payDate);
		// find data
		// F09 暫付火險保費
		Slice<AcReceivable> slAcReceivable = acReceivableService.acrvOpenAcDateLq("F09", 0, payDate, this.index,
				Integer.MAX_VALUE); // acctCode=, clsFlag=, openAcDate <
		lAcReceivable = slAcReceivable == null ? null : slAcReceivable.getContent();
//test	lAcReceivable = acReceivableService.acrvOpenAcDateLq("F09", 0, 99999999); // acctCode=, clsFlag=, openAcDate <
		// data size > 0 -> 新增應處理明細
		TxToDoDetail tTxToDoDetail;
		if (lAcReceivable != null) {
			for (AcReceivable rv : lAcReceivable) {
				tTxToDoDetail = new TxToDoDetail();
				tTxToDoDetail.setItemCode("TRIS00"); // 火險費轉列催收
				tTxToDoDetail.setCustNo(rv.getCustNo());
				tTxToDoDetail.setFacmNo(rv.getFacmNo());
				tTxToDoDetail.setDtlValue(rv.getRvNo());
				txToDoCom.addDetail(true, titaVo.getHCodeI(), tTxToDoDetail, titaVo); // DupSkip = true ->重複跳過
			}
		}
	}

	/* 月底日將逾三個月之法務費轉列催收 */
	private void procLawFeeOverdue(TitaVo titaVo) throws LogicException {
		List<AcReceivable> lAcReceivable = new ArrayList<AcReceivable>();
		int ym = this.getTxBuffer().getMgBizDate().getTbsDyf() / 100;
		dateUtil.init();
		dateUtil.setDate_1(ym * 100 + 01);
		dateUtil.setMons(-2);
		int payDate = dateUtil.getCalenderDay();
		// 上月月底日之前
		this.info("法務費轉列催收日期 < " + payDate);
		// find data
		// F07 暫付法務費
		Slice<AcReceivable> slAcReceivable = acReceivableService.acrvOpenAcDateLq("F07", 0, payDate, this.index,
				Integer.MAX_VALUE); // acctCode=, clsFlag=, openAcDate <
		lAcReceivable = slAcReceivable == null ? null : slAcReceivable.getContent();
//test	lAcReceivable = acReceivableService.acrvOpenAcDateLq("F07", 0, 99999999); //
		// acctCode=, clsFlag=, openAcDate <
		this.info("lAcReceivable" + lAcReceivable.toString());
		// data size > 0 -> 新增應處理明細
		TxToDoDetail tTxToDoDetail;
		if (lAcReceivable != null) {
			for (AcReceivable rv : lAcReceivable) {
				if (rv.getRvNo().length() == 7) {
					tTxToDoDetail = new TxToDoDetail();
					tTxToDoDetail.setItemCode("TRLW00"); // 法務費轉列催收
					tTxToDoDetail.setCustNo(rv.getCustNo());
					tTxToDoDetail.setFacmNo(rv.getFacmNo());
					tTxToDoDetail.setDtlValue(rv.getRvNo());
					txToDoCom.addDetail(true, titaVo.getHCodeI(), tTxToDoDetail, titaVo); // DupSkip = true ->重複跳過
				}
			}
		}
	}
}