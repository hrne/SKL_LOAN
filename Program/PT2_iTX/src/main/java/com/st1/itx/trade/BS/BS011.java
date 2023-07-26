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
import com.st1.itx.db.domain.AcReceivable;
import com.st1.itx.db.domain.TxToDoDetail;
import com.st1.itx.db.service.AcReceivableService;
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.common.TxToDoCom;
import com.st1.itx.util.date.DateUtil;
import com.st1.itx.util.parse.Parse;

@Component("BS011")
@Scope("prototype")

/**
 * 新增應處理明細－月底日將逾三個月之火險費、法務費轉列催收 <br>
 * 執行時機：日始作業，應處理清單維護(BS001)自動執行 <br>
 * 
 * @author Lai
 * @version 1.0.0
 */

public class BS011 extends TradeBuffer {

	/* 轉型共用工具 */
	@Autowired
	public Parse parse;
	/* 日期工具 */
	@Autowired
	public DateUtil dateUtil;

	@Autowired
	public AcReceivableService acReceivableService;

	@Autowired
	public TxToDoCom txToDoCom;

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("BS011 ......");

		txToDoCom.setTxBuffer(this.txBuffer);

		// 取本月份
		String entryDateMm = parse.IntegerToString(this.txBuffer.getMgBizDate().getTbsDy() / 100, 5).substring(3, 5);
		this.info("取本月份 = " + parse.stringToInteger(entryDateMm));

		// step 1 月底日將逾三個月之火險費轉列催收
		procInsuFeeOverdue(titaVo);
		this.batchTransaction.commit();

		// step 2. 月底日將逾三個月之法務費轉列催收
		procLawFeeOverdue(titaVo);
		this.batchTransaction.commit();

		this.batchTransaction.commit();

		return null;
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
		// acctCode=, clsFlag=, openAcDate <
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