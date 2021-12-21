package com.st1.itx.trade.L3;

import java.math.BigDecimal;
import java.util.ArrayList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;

import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.dataVO.TotaVo;
import com.st1.itx.db.domain.LoanBorMain;
import com.st1.itx.db.service.LoanBorMainService;
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
	private static final Logger logger = LoggerFactory.getLogger(L3R07.class);

	/* DB服務注入 */
	@Autowired
	LoanCloseBreachCom loanCloseBreachCom;
	@Autowired
	LoanBorMainService loanBorMainService;

	@Autowired
	Parse parse;

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		logger.info("active L3R07 ");
		this.totaVo.init(titaVo);

		// 取得輸入資料
		int iCustNo = this.parse.stringToInteger(titaVo.getParam("RimCustNo"));
		int iFacmNo = this.parse.stringToInteger(titaVo.getParam("RimFacmNo"));
		int iBormNo = this.parse.stringToInteger(titaVo.getParam("RimBormNo"));
		BigDecimal oCloseBreachAmt = BigDecimal.ZERO;
		ArrayList<LoanCloseBreachVo> oListCloseBreach = new ArrayList<LoanCloseBreachVo>();

		ArrayList<LoanBorMain> lLoanBorMain = new ArrayList<LoanBorMain>();
		int wkFacmNoS = 0;
		int wkFacmNoE = 999;
		int wkBormNoS = 0;
		int wkBormNoE = 900;
		if (iFacmNo > 0) {
			wkFacmNoS = iFacmNo;
			wkFacmNoE = iFacmNo;
		}
		if (iBormNo > 0) {
			wkBormNoS = iBormNo;
			wkBormNoE = iBormNo;
		}

		// 結案是否滿三年邏輯
		Slice<LoanBorMain> slLoanBorMain = loanBorMainService.bormCustNoEq(iCustNo, wkFacmNoS, wkFacmNoE, wkBormNoS, wkBormNoE, 0, Integer.MAX_VALUE, titaVo);

		lLoanBorMain = slLoanBorMain == null ? null : new ArrayList<LoanBorMain>(slLoanBorMain.getContent());
		int wkAcDate = 0;
		int wkSysDate = 0;
//		0: 正常戶 1:展期 2: 催收戶 3: 結案戶 4: 逾期戶 5: 催收結案戶 6: 呆帳戶 7: 部分轉呆戶 8: 債權轉讓戶 9: 呆帳結案戶 97:預約撥款已刪除 98:預約已撥款 99:預約撥款
		if (lLoanBorMain != null) {
			for (LoanBorMain t : lLoanBorMain) {

				// 跳過展期戶
				if (t.getStatus() == 1) {
					continue;
				}
				// 未正常結案顯示error
				if (t.getStatus() == 0 || t.getStatus() == 2 || t.getStatus() == 6 || t.getStatus() == 7) {
//					error Message
				}

				// 取該戶最大日期
				if (t.getStatus() == 3 && t.getAcDate() > wkAcDate) {
					wkAcDate = t.getAcDate();
				}
			}
		}

		// 結案不滿三年處理
		if (wkAcDate > 0 && wkAcDate + 30000 > this.txBuffer.getTxCom().getTbsdy()) {
			// 計算清償違約金
			oListCloseBreach = loanCloseBreachCom.getCloseBreachAmtAll(iCustNo, iFacmNo, iBormNo, null, titaVo);
			// 輸出清償違約金
			if (oListCloseBreach != null && oListCloseBreach.size() > 0) {
				for (LoanCloseBreachVo v : oListCloseBreach) {
					oCloseBreachAmt = oCloseBreachAmt.add(v.getCloseBreachAmt());
				}
			}
		}

		this.totaVo.putParam("OCloseBreachAmt", oCloseBreachAmt);

		this.addList(this.totaVo);
		return this.sendList();
	}
}