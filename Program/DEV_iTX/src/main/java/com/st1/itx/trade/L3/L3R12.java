package com.st1.itx.trade.L3;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;

import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.dataVO.TotaVo;
import com.st1.itx.db.domain.LoanOverdue;
import com.st1.itx.db.service.LoanOverdueService;
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.common.BaTxCom;
import com.st1.itx.util.parse.Parse;

/*
 * Tita
 * RimCustNo=9,7
 * RimFacmNo=9,3
 * RimBormNo=9,3
 */
/**
 * L3R12 結案查詢催收呆帳檔金額
 * 
 * @author iza
 * @version 1.0.0
 */
@Service("L3R12")
@Scope("prototype")
public class L3R12 extends TradeBuffer {

	/* DB服務注入 */
	@Autowired
	LoanOverdueService loanOverdueService;
	@Autowired
	BaTxCom baTxCom;

	@Autowired
	Parse parse;

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active L3R12 ");
		this.totaVo.init(titaVo);

		// 取得輸入資料
		int iCustNo = this.parse.stringToInteger(titaVo.getParam("RimCustNo"));
		int iFacmNo = this.parse.stringToInteger(titaVo.getParam("RimFacmNo"));
		int iBormNo = this.parse.stringToInteger(titaVo.getParam("RimBormNo"));

		// work area
		int wkFacmNoStart = 1;
		int wkFacmNoEnd = 999;
		int wkBormNoStart = 1;
		int wkBormNoEnd = 900;
		int wkOvduDate = 9991231;
		BigDecimal wkOvduPrinAmt = BigDecimal.ZERO;
		BigDecimal wkOvduIntAmt = BigDecimal.ZERO;
		BigDecimal wkOvduBreachAmt = BigDecimal.ZERO;
		BigDecimal wkOvduAmt = BigDecimal.ZERO;
		BigDecimal wkOvduPrinBal = BigDecimal.ZERO;
		BigDecimal wkOvduIntBal = BigDecimal.ZERO;
		BigDecimal wkOvduBreachBal = BigDecimal.ZERO;
		BigDecimal wkOvduBal = BigDecimal.ZERO;
		BigDecimal wkReduceInt = BigDecimal.ZERO;
		BigDecimal wkReduceBreach = BigDecimal.ZERO;
		BigDecimal wkBadDebtAmt = BigDecimal.ZERO;
		BigDecimal wkBadDebtBal = BigDecimal.ZERO;
		BigDecimal wkReplyReduceAmt = BigDecimal.ZERO;

		try {
			baTxCom.settingUnPaid(this.txBuffer.getTxCom().getTbsdy(), iCustNo, iFacmNo, iBormNo, 99, BigDecimal.ZERO, titaVo); // 99-費用全部(含未到期)
		} catch (LogicException e) {
			throw new LogicException(titaVo, "E0015", "查詢費用 " + e.getMessage()); // 檢查錯誤
		}

		List<LoanOverdue> lLoanOverdue = new ArrayList<LoanOverdue>();

		if (iFacmNo > 0) {
			wkFacmNoStart = iFacmNo;
			wkFacmNoEnd = iFacmNo;
		}
		if (iBormNo > 0) {
			wkBormNoStart = iBormNo;
			wkBormNoEnd = iBormNo;
		}
		List<Integer> lStatus = new ArrayList<Integer>(); // 1:催收 2:部分轉呆 3:呆帳 4:催收回復
		lStatus.add(1);
		lStatus.add(2);
		Slice<LoanOverdue> slLoanOverdue = loanOverdueService.ovduCustNoRange(iCustNo, wkFacmNoStart, wkFacmNoEnd, wkBormNoStart, wkBormNoEnd, 1, 999, lStatus, 0, Integer.MAX_VALUE, titaVo);
		lLoanOverdue = slLoanOverdue == null ? null : slLoanOverdue.getContent();
		if (lLoanOverdue == null || lLoanOverdue.size() == 0) {
			throw new LogicException(titaVo, "E0001", "催收呆帳檔"); // 查詢資料不存在
		}
		for (LoanOverdue od : lLoanOverdue) {
			if (od.getOvduDate() != 0 && od.getOvduDate() < wkOvduDate) {
				wkOvduDate = od.getOvduDate();
			}
			wkOvduPrinAmt = wkOvduPrinAmt.add(od.getOvduPrinAmt());
			wkOvduIntAmt = wkOvduIntAmt.add(od.getOvduIntAmt());
			wkOvduBreachAmt = wkOvduBreachAmt.add(od.getOvduBreachAmt());
			wkOvduAmt = wkOvduAmt.add(od.getOvduAmt());
			wkOvduPrinBal = wkOvduPrinBal.add(od.getOvduPrinBal());
			wkOvduIntBal = wkOvduIntBal.add(od.getOvduIntBal());
			wkOvduBreachBal = wkOvduBreachBal.add(od.getOvduBreachBal());
			wkOvduBal = wkOvduBal.add(od.getOvduBal());
			wkReduceInt = wkReduceInt.add(od.getReduceInt());
			wkReduceBreach = wkReduceBreach.add(od.getReduceBreach());
			wkBadDebtAmt = wkBadDebtAmt.add(od.getBadDebtAmt());
			wkBadDebtBal = wkBadDebtBal.add(od.getBadDebtBal());
			wkReplyReduceAmt = wkReplyReduceAmt.add(od.getReplyReduceAmt());
		}

		this.totaVo.putParam("L3r12OvduDate", wkOvduDate);
		this.totaVo.putParam("L3r12OvduPrinAmt", wkOvduPrinAmt);
		this.totaVo.putParam("L3r12OvduIntAmt", wkOvduIntAmt);
		this.totaVo.putParam("L3r12OvduBreachAmt", wkOvduBreachAmt);
		this.totaVo.putParam("L3r12OvduAmt", wkOvduAmt);
		this.totaVo.putParam("L3r12OvduPrinBal", wkOvduPrinBal);
		this.totaVo.putParam("L3r12OvduIntBal", wkOvduIntBal);
		this.totaVo.putParam("L3r12OvduBreachBal", wkOvduBreachBal);
		this.totaVo.putParam("L3r12OvduBal", wkOvduBal);
		this.totaVo.putParam("L3r12FireFee", baTxCom.getFireFee());
		this.totaVo.putParam("L3r12LawFee", baTxCom.getLawFee());
		this.totaVo.putParam("L3r12CollFireFee", baTxCom.getCollFireFee());
		this.totaVo.putParam("L3r12CollLawFee", baTxCom.getCollLawFee());
		this.totaVo.putParam("L3r12ReduceInt", wkReduceInt);
		this.totaVo.putParam("L3r12ReduceBreach", wkReduceBreach);
		this.totaVo.putParam("L3r12ReplyReduceAmt", wkReplyReduceAmt);
		this.totaVo.putParam("L3r12BadDebtAmt", wkBadDebtAmt);
		this.totaVo.putParam("L3r12BadDebtBal", wkBadDebtBal);

		this.addList(this.totaVo);
		return this.sendList();
	}
}