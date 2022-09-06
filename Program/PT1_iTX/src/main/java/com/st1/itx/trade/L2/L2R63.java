package com.st1.itx.trade.L2;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.TempVo;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.dataVO.TotaVo;
import com.st1.itx.db.domain.CdEmp;
import com.st1.itx.db.domain.TxToDoDetailReserve;
import com.st1.itx.db.service.BatxRateChangeService;
import com.st1.itx.db.service.CdEmpService;
import com.st1.itx.db.service.LoanBorMainService;
import com.st1.itx.db.service.TxToDoDetailReserveService;
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.date.DateUtil;
import com.st1.itx.util.parse.Parse;

@Service("L2R63")
@Scope("prototype")
/**
 * 
 * 
 * @author YuHeng
 * @version 1.0.0
 */
public class L2R63 extends TradeBuffer {

	/* DB服務注入 */
	@Autowired
	public BatxRateChangeService batxRateChangeService;

	@Autowired
	public LoanBorMainService loanBorMainService;

	@Autowired
	public CdEmpService cdEmpService;

	/* 日期工具 */
	@Autowired
	public DateUtil dateUtil;

	@Autowired
	private TxToDoDetailReserveService txToDoDetailReserveService;

	/* 轉換工具 */
	@Autowired
	public Parse parse;

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active L2R63 ");
		this.totaVo.init(titaVo);

		int iCustNo = this.parse.stringToInteger(titaVo.getParam("RimCustNo"));
		int iFacmNo = this.parse.stringToInteger(titaVo.getParam("RimFacmNo"));
		int iBormNo = this.parse.stringToInteger(titaVo.getParam("RimBormNo"));

		this.info("CustNo = " + iCustNo);
		this.info("FacmNo = " + iFacmNo);
		this.info("BormNo = " + iBormNo);

		TxToDoDetailReserve tTxToDoDetailReserve = txToDoDetailReserveService.FindL2880First("L2880", iCustNo, iFacmNo, iBormNo, titaVo);

		this.totaVo.putParam("L2r63PrintDate", "");
		this.totaVo.putParam("L2r63TlrNo", "");
		this.totaVo.putParam("L2r63ExpectedRate", "");

		if (tTxToDoDetailReserve != null) {
			TempVo tTempVo = new TempVo();
			tTempVo = tTempVo.getVo(tTxToDoDetailReserve.getProcessNote());

			this.totaVo.putParam("L2r63PrintDate", tTempVo.getParam("LastPrintDate")); // 最後更新日期

			CdEmp cdEmp = cdEmpService.findById(tTempVo.getParam("TlrNo"), titaVo);

			if (cdEmp != null) {
				this.totaVo.putParam("L2r63TlrNo", cdEmp.getFullname()); // 最後更新櫃員
			}

			if (tTempVo.get("LastExpectedRate") != null) {
				this.totaVo.putParam("L2r63ExpectedRate", tTempVo.getParam("LastExpectedRate")); // 擬調利率
			}
		}

		this.addList(this.totaVo);
		return this.sendList();
	}
}