package com.st1.itx.trade.L2;

import java.math.BigDecimal;
import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.dataVO.TotaVo;
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.common.LoanAvailableAmt;
import com.st1.itx.util.parse.Parse;

@Service("L2R41")
@Scope("prototype")
/**
 * 
 * 
 * @author YuJiaXing
 * @version 1.0.0
 */
public class L2R41 extends TradeBuffer {

	@Autowired
	LoanAvailableAmt loanAvailableAmt;

	/* 轉換工具 */
	@Autowired
	public Parse parse;

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active L2R41 ");
		this.totaVo.init(titaVo);
		loanAvailableAmt.setTxBuffer(this.txBuffer);

		parse.stringToInteger(titaVo.getParam("RimFunCd"));
		int iClCode1 = parse.stringToInteger(titaVo.getParam("RimClCode1"));
		int iClCode2 = parse.stringToInteger(titaVo.getParam("RimClCode2"));
		int iClNo = parse.stringToInteger(titaVo.getParam("RimClNo"));
		BigDecimal iEvaAmt = parse.stringToBigDecimal(titaVo.getParam("RimEvaAmt")); // 鑑價總值
		BigDecimal iEvaNetWorth = parse.stringToBigDecimal(titaVo.getParam("RimEvaNetWorth")); // 評估淨值
		BigDecimal iLoanToValue = parse.stringToBigDecimal(titaVo.getParam("RimLoanToValue")); // 貸放成數
		BigDecimal iSettingAmt = parse.stringToBigDecimal(titaVo.getParam("RimSettingAmt")); // 設定金額
		String iSettingStat = titaVo.getParam("RimSettingStat"); // 設定狀態 1設定 2解除
		String iClStat = titaVo.getParam("RimClStat"); // 擔保品狀態 0正常 1塗銷 2處分 3抵押權確定

		// WK
		BigDecimal wkAvailable = BigDecimal.ZERO;
		BigDecimal wkShareCompAmt = BigDecimal.ZERO;
		BigDecimal wkShareTotal = BigDecimal.ZERO;

		// 評估淨值有值時擺評估淨值,否則擺鑑估總值.
		if (iEvaNetWorth.compareTo(BigDecimal.ZERO) > 0) {
			wkShareCompAmt = iEvaNetWorth;
		} else {
			wkShareCompAmt = iEvaAmt;
		}

		wkShareTotal = wkShareCompAmt.multiply(iLoanToValue).divide(new BigDecimal(100)).setScale(0, BigDecimal.ROUND_HALF_UP);

		// 分配金額和設定金額比較 較低的為可分配金額
		this.info("分配金額和設定金額比較 = " + wkShareTotal + "," + iSettingAmt);
		if (iSettingAmt.compareTo(wkShareTotal) < 0) {
			wkShareTotal = iSettingAmt;
		}

		if ("1".equals(iClStat) || "2".equals(iSettingStat)) {
			wkShareTotal = BigDecimal.ZERO;
		}

		wkAvailable = loanAvailableAmt.checkClAvailable(iClCode1, iClCode2, iClNo, wkShareTotal, titaVo); // 可用額度
//		BigDecimal utilAmtFac = loanAvailableAmt.getUtilAmtFac();// 額度放款餘額
//		BigDecimal shareAmtCl = loanAvailableAmt.getShareAmtCl();// 擔保品可分配金額
//		this.info("UtilAmtFac=" + utilAmtFac + ",shareAmtCl=" + shareAmtCl);
		
		if (wkAvailable.compareTo(BigDecimal.ZERO) < 0) {
			//throw new LogicException("E3071", "可分配金額不足 ： = " + wkAvailable);
			this.totaVo.setWarnMsg("可分配金額不足 ： =" + wkAvailable);
		}

		this.addList(this.totaVo);
		return this.sendList();
	}
}