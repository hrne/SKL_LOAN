package com.st1.itx.trade.L3;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.TempVo;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.dataVO.TotaVo;
import com.st1.itx.db.domain.LoanBorTx;
import com.st1.itx.db.domain.LoanBorTxId;
import com.st1.itx.db.service.LoanBorTxService;
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.date.DateUtil;
import com.st1.itx.util.parse.Parse;

@Service("L3R31")
@Scope("prototype")
/**
 * 
 * 
 * @author YuJiaXing
 * @version 1.0.0
 */
public class L3R31 extends TradeBuffer {

	/* DB服務注入 */
	@Autowired
	public LoanBorTxService sLoanBorTxService;

	/* 日期工具 */
	@Autowired
	public DateUtil dateUtil;

	/* 轉換工具 */
	@Autowired
	public Parse parse;

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active L3R31 ");
		this.totaVo.init(titaVo);

		// 戶號
		int iCustNo = parse.stringToInteger(titaVo.getParam("RimCustNo"));
		// 額度
		int iFacmNo = parse.stringToInteger(titaVo.getParam("RimFacmNo"));
		// 備忘錄序號
		int iBormNo = parse.stringToInteger(titaVo.getParam("RimBormNo"));

		int iBorxNo = parse.stringToInteger(titaVo.getParam("RimBorxNo"));

		this.info("iCustNo    = " + iCustNo);
		this.info("iFacmNo    = " + iFacmNo);
		this.info("iBormNo    = " + iBormNo);
		this.info("iBorxNo    = " + iBorxNo);

//		String iNote = titaVo.getParam("Note");
		TempVo tTempVo = new TempVo();
		LoanBorTx tLoanBorTx = sLoanBorTxService.findById(new LoanBorTxId(iCustNo, iFacmNo, iBormNo, iBorxNo), titaVo);
		if (tLoanBorTx == null) {
			this.totaVo.putParam("L3r31Note", "");
		} else {
			tTempVo = tTempVo.getVo(tLoanBorTx.getOtherFields());
			this.totaVo.putParam("L3r31Note", tTempVo.getParam("Note"));
		}

		this.addList(this.totaVo);
		return this.sendList();
	}
}