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
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.dataVO.TotaVo;
import com.st1.itx.db.domain.LoanIntDetail;
import com.st1.itx.db.service.LoanIntDetailService;
import com.st1.itx.tradeService.TradeBuffer;
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
	private static final Logger logger = LoggerFactory.getLogger(L3R07.class);

	/* DB服務注入 */
	@Autowired
	public LoanIntDetailService loanIntDetailService;

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

		// work area
		List<LoanIntDetail> lLoanIntDetail;
		int wkFacmNoStart = 1;
		int wkFacmNoEnd = 999;
		int wkBormNoStart = 1;
		int wkBormNoEnd = 900;
		BigDecimal wkCloseBreachAmt = BigDecimal.ZERO;

		if (iFacmNo > 0) {
			wkFacmNoStart = iFacmNo;
			wkFacmNoEnd = iFacmNo;
		}

		if (iBormNo > 0) {
			wkBormNoStart = iBormNo;
			wkBormNoEnd = iBormNo;
		}

		Slice<LoanIntDetail> slLoanIntDetail = loanIntDetailService.intDetailBreachGetCodeEq(iCustNo, wkFacmNoStart, wkFacmNoEnd, wkBormNoStart, wkBormNoEnd, "2", 0, Integer.MAX_VALUE, titaVo);
		lLoanIntDetail = slLoanIntDetail == null ? null : slLoanIntDetail.getContent();
		if (lLoanIntDetail == null || lLoanIntDetail.size() == 0) {
			throw new LogicException(titaVo, "E0001", "計息明細檔"); // 查詢資料不存在
		}
		for (LoanIntDetail t : lLoanIntDetail) {
			wkCloseBreachAmt = wkCloseBreachAmt.add(t.getCloseBreachAmt());
		}
		this.totaVo.putParam("OCloseBreachAmt", wkCloseBreachAmt);

		this.addList(this.totaVo);
		return this.sendList();
	}
}