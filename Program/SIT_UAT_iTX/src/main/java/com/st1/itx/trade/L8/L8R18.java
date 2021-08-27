package com.st1.itx.trade.L8;

import java.util.ArrayList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.dataVO.TotaVo;
import com.st1.itx.db.domain.JcicZ047;
import com.st1.itx.db.service.JcicZ047Service;
import com.st1.itx.tradeService.TradeBuffer;

@Service("L8R18")
@Scope("prototype")
/**
 * 
 * 
 * @author Mata
 * @version 1.0.0
 */
public class L8R18 extends TradeBuffer {
	/* DB服務注入 */
	@Autowired
	public JcicZ047Service iJcicZ047Service;


	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active L8r18 ");
		this.totaVo.init(titaVo);
		String iUkey = titaVo.getParam("RimUkey");
		JcicZ047 iJcicZ047 = new JcicZ047();
		iJcicZ047 = iJcicZ047Service.ukeyFirst(iUkey, titaVo);
		
		if (iJcicZ047 == null) {
			throw new LogicException(titaVo, "E0001", ""); // 無此代號錯誤
		}else {
			totaVo.putParam("L8r18CustId", iJcicZ047.getCustId());
			totaVo.putParam("L8r18SubmitKey", iJcicZ047.getSubmitKey());
			totaVo.putParam("L8r18RcDate",iJcicZ047.getRcDate());
			totaVo.putParam("L8r18Period",iJcicZ047.getPeriod()); 
			totaVo.putParam("L8r18Rate",iJcicZ047.getRate());
			totaVo.putParam("L8r18Civil323ExpAmt", iJcicZ047.getCivil323ExpAmt());
			totaVo.putParam("L8r18ExpLoanAmt", iJcicZ047.getExpLoanAmt());
			totaVo.putParam("L8r18Civil323CashAmt", iJcicZ047.getCivil323CashAmt());
			totaVo.putParam("L8r18CashCardAmt", iJcicZ047.getCashCardAmt());
			totaVo.putParam("L8r18Civil323CreditAmt", iJcicZ047.getCivil323CreditAmt());
			totaVo.putParam("L8r18CreditCardAmt", iJcicZ047.getCreditCardAmt());
			totaVo.putParam("L8r18Civil323Amt", iJcicZ047.getCivil323Amt());
			totaVo.putParam("L8r18TotalAmt", iJcicZ047.getTotalAmt());
			totaVo.putParam("L8r18PassDate", iJcicZ047.getPassDate());
			totaVo.putParam("L8r18InterviewDate", iJcicZ047.getInterviewDate());
			totaVo.putParam("L8r18SignDate", iJcicZ047.getSignDate());
			totaVo.putParam("L8r18LimitDate", iJcicZ047.getLimitDate());
			totaVo.putParam("L8r18FirstPayDate", iJcicZ047.getFirstPayDate());
			totaVo.putParam("L8r18MonthPayAmt", iJcicZ047.getMonthPayAmt());
			totaVo.putParam("L8r18PayAccount", iJcicZ047.getPayAccount());
			totaVo.putParam("L8r18PostAddr", iJcicZ047.getPostAddr());
			totaVo.putParam("L8r18GradeType", iJcicZ047.getGradeType());
			totaVo.putParam("L8r18PayLastAmt", iJcicZ047.getPayLastAmt());
			totaVo.putParam("L8r18Period2", iJcicZ047.getPeriod2());
			totaVo.putParam("L8r18Rate2", iJcicZ047.getRate2());
			totaVo.putParam("L8r18MonthPayAmt2", iJcicZ047.getMonthPayAmt2());
			totaVo.putParam("L8r18PayLastAmt2", iJcicZ047.getPayLastAmt2());
			totaVo.putParam("L8r18TranKey", iJcicZ047.getTranKey());	
			totaVo.putParam("L8r18OutJcicTxtDate", iJcicZ047.getOutJcicTxtDate());
		}	
		this.addList(this.totaVo);
		return this.sendList();
	}
}