package com.st1.itx.trade.L3;

import java.math.BigDecimal;
import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.dataVO.TotaVo;
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.common.LoanCloseBreachCom;
import com.st1.itx.util.common.data.LoanCloseBreachVo;
/*
 * Tita
 * RimCustNo=9,7
 * RimFacmNo=9,3
 * RimBormNo=9,3
 */
import com.st1.itx.util.parse.Parse;

/**
 * L3R07 查詢清償違約金
 * 
 * @author iza
 * @version 1.0.0
 */
@Service("L3R07")
@Scope("prototype")
public class L3R07 extends TradeBuffer {

	/* DB服務注入 */
	@Autowired
	LoanCloseBreachCom loanCloseBreachCom;

	@Autowired
	Parse parse;

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active L3R07 ");
		this.totaVo.init(titaVo);

		// 取得輸入資料
		int iCustNo = this.parse.stringToInteger(titaVo.getParam("RimCustNo"));
		int iFacmNo = this.parse.stringToInteger(titaVo.getParam("RimFacmNo"));
		int iBormNo = this.parse.stringToInteger(titaVo.getParam("RimBormNo"));
		BigDecimal oCloseBreachAmt = BigDecimal.ZERO;
		ArrayList<LoanCloseBreachVo> oListCloseBreach = new ArrayList<LoanCloseBreachVo>();
		// 計算清償違約金
		oListCloseBreach = loanCloseBreachCom.getCloseBreachAmtAll(iCustNo, iFacmNo, iBormNo, null, titaVo);
		// 輸出清償違約金
		if (oListCloseBreach != null && oListCloseBreach.size() > 0) {
			for (LoanCloseBreachVo v : oListCloseBreach) {
				oCloseBreachAmt = oCloseBreachAmt.add(v.getCloseBreachAmt());
			}
		}
		this.totaVo.putParam("OCloseBreachAmt", oCloseBreachAmt);

		this.addList(this.totaVo);
		return this.sendList();
	}
}