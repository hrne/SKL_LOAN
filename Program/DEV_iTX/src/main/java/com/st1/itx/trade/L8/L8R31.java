package com.st1.itx.trade.L8;

import java.util.ArrayList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.dataVO.TotaVo;
import com.st1.itx.db.domain.JcicZ062;
import com.st1.itx.db.service.JcicZ062Service;
import com.st1.itx.tradeService.TradeBuffer;

@Service("L8R31")
@Scope("prototype")
/**
 * 
 * @author Luisito / Mata
 * @version 1.0.0
 */
public class L8R31 extends TradeBuffer {
	/* DB服務注入 */
	@Autowired
	public JcicZ062Service iJcicZ062Service;


	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active L8r31 ");
		this.totaVo.init(titaVo);
		String iUkey = titaVo.getParam("RimUkey");
		JcicZ062 iJcicZ062 = new JcicZ062();
		iJcicZ062 = iJcicZ062Service.ukeyFirst(iUkey, titaVo);
		
		if (iJcicZ062 == null) {
			throw new LogicException(titaVo, "E0001", ""); // 無此代號錯誤
		}else {
			totaVo.putParam("L8r31TranKey",iJcicZ062.getTranKey());// 交易代碼
			totaVo.putParam("L8r31CustId", iJcicZ062.getCustId());// 債務人IDN
			totaVo.putParam("L8r31SubmitKey", iJcicZ062.getSubmitKey());// 報送單位代號
			totaVo.putParam("L8r31RcDate", iJcicZ062.getRcDate());// 原前置協商申請日
			totaVo.putParam("L8r31ChangePayDate", iJcicZ062.getChangePayDate());// 申請變更還款條件日
			totaVo.putParam("L8r31CompletePeriod", iJcicZ062.getCompletePeriod());// 變更還款條件已履約期數
			totaVo.putParam("L8r31Period", iJcicZ062.getPeriod());// (第一階梯)期數
			totaVo.putParam("L8r31Rate", iJcicZ062.getRate());// (第一階梯)利率
			totaVo.putParam("L8r31ExpBalanceAmt", iJcicZ062.getExpBalanceAmt());// 信用貸款協商剩餘債務簽約餘額
			totaVo.putParam("L8r31CashBalanceAmt", iJcicZ062.getCashBalanceAmt());// 現金卡協商剩餘債務簽約餘額
			totaVo.putParam("L8r31CreditBalanceAmt", iJcicZ062.getCreditBalanceAmt());// 信用卡協商剩餘債務簽約餘額
			totaVo.putParam("L8r31ChaRepayAmt", iJcicZ062.getChaRepayAmt());// 變更還款條件簽約總債務金額
			totaVo.putParam("L8r31ChaRepayAgreeDate", iJcicZ062.getChaRepayAgreeDate());// 變更還款條件協議完成日
			totaVo.putParam("L8r31ChaRepayViewDate", iJcicZ062.getChaRepayViewDate());// 變更還款條件面談日期
			totaVo.putParam("L8r31ChaRepayEndDate", iJcicZ062.getChaRepayEndDate());// 變更還款條件簽約完成日期
			totaVo.putParam("L8r31ChaRepayFirstDate", iJcicZ062.getChaRepayFirstDate());// 變更還款條件首期應繳款日
			totaVo.putParam("L8r31PayAccount", iJcicZ062.getPayAccount());// 繳款帳號
			totaVo.putParam("L8r31PostAddr", iJcicZ062.getPostAddr());// 最大債權金融機構聲請狀送達地址
			totaVo.putParam("L8r31MonthPayAmt", iJcicZ062.getMonthPayAmt());// 月付金
			totaVo.putParam("L8r31GradeType", iJcicZ062.getGradeType());// 屬階梯式還款註記
			totaVo.putParam("L8r31Period2", iJcicZ062.getPeriod2());// 第二階梯期數
			totaVo.putParam("L8r31Rate2", iJcicZ062.getRate2());// 第二階梯利率
			totaVo.putParam("L8r31MonthPayAmt2", iJcicZ062.getMonthPayAmt2());// 第二階段月付金
			totaVo.putParam("L8r31OutJcicTxtDate", iJcicZ062.getOutJcicTxtDate());// 轉JCIC文字檔日期
		}

		
		this.addList(this.totaVo);
		return this.sendList();
	}
}