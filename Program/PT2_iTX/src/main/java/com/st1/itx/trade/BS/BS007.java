package com.st1.itx.trade.BS;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;

import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.dataVO.TotaVo;
import com.st1.itx.db.domain.CdLoanNotYet;
import com.st1.itx.db.domain.LoanNotYet;
import com.st1.itx.db.domain.TxToDoDetail;
import com.st1.itx.db.service.CdLoanNotYetService;
import com.st1.itx.db.service.LoanNotYetService;
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.common.TxToDoCom;
import com.st1.itx.util.date.DateUtil;

@Service("BS007")
@Scope("prototype")
/**
 * 有「L2801 未齊案件管理」交易權限者,應將未齊件資料於[齊件訖日]到期前3工作日開始,顯示於該使用者"處理清單"中
 * 
 * @author eric chang
 * @version 1.0.0
 */
public class BS007 extends TradeBuffer {
	@Autowired
	public TxToDoCom txToDoCom;

	@Autowired
	public LoanNotYetService loanNotYetService;

	@Autowired
	public CdLoanNotYetService cdLoanNotYetService;

	/* 日期工具 */
	@Autowired
	public DateUtil dateUtil;

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active BS007 ");

		txToDoCom.delByItemCode("L2921", titaVo);

		dateUtil.setDate_1(titaVo.getCalDy());
		dateUtil.setDays(3);
		int tbsdy = dateUtil.getCalenderDay() + 19110000;
		this.info("active tbsdy = " + tbsdy);

		Slice<LoanNotYet> slLoanNotYet = loanNotYetService.allNoClose(0, tbsdy, 0, Integer.MAX_VALUE, titaVo);
		List<LoanNotYet> lLoanNotYet = slLoanNotYet == null ? null : slLoanNotYet.getContent();

		txToDoCom.setTxBuffer(this.getTxBuffer());

		if (lLoanNotYet != null && lLoanNotYet.size() > 0) {
			for (LoanNotYet loanNotYet : lLoanNotYet) {
				TxToDoDetail tTxToDoDetail = new TxToDoDetail();
				tTxToDoDetail.setItemCode("L2921");
				tTxToDoDetail.setCustNo(loanNotYet.getCustNo());
				tTxToDoDetail.setFacmNo(loanNotYet.getFacmNo());

				/*
				 * 未齊件代碼說明2022.2.9 by 昱衡
				 */
				String NotYetCode = loanNotYet.getNotYetCode();
				CdLoanNotYet cdLoanNotYet = cdLoanNotYetService.findById(NotYetCode, titaVo);
				if (cdLoanNotYet != null) {
					tTxToDoDetail.setDtlValue(cdLoanNotYet.getNotYetCode());
					tTxToDoDetail.setProcessNote(cdLoanNotYet.getNotYetItem());
				} else {
					tTxToDoDetail.setDtlValue("");
					tTxToDoDetail.setProcessNote("");
				}

				txToDoCom.addDetail(false, 0, tTxToDoDetail, titaVo); // addDetail
			}
		}

		this.batchTransaction.commit();

		return null;
	}
}