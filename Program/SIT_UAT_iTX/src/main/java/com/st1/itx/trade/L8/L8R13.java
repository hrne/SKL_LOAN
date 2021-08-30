package com.st1.itx.trade.L8;

import java.util.ArrayList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.dataVO.TotaVo;
import com.st1.itx.db.domain.JcicZ042;
import com.st1.itx.db.service.JcicZ042Service;
import com.st1.itx.tradeService.TradeBuffer;

@Service("L8R13")
@Scope("prototype")
/**
 * 
 * 
 * @author Mata
 * @version 1.0.0
 */
public class L8R13 extends TradeBuffer {
	/* DB服務注入 */
	@Autowired
	public JcicZ042Service iJcicZ042Service;


	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active L8r13 ");
		this.totaVo.init(titaVo);
		String iUkey = titaVo.getParam("RimUkey");
		JcicZ042 iJcicZ042 = new JcicZ042();
		iJcicZ042 = iJcicZ042Service.ukeyFirst(iUkey, titaVo);
		
		if (iJcicZ042 == null) {
			throw new LogicException(titaVo, "E0001", ""); // 無此代號錯誤
		}else {
			totaVo.putParam("L8r13CustId", iJcicZ042.getCustId());
			totaVo.putParam("L8r13SubmitKey", iJcicZ042.getSubmitKey());
			totaVo.putParam("L8r13RcDate",iJcicZ042.getRcDate());
			totaVo.putParam("L8r13MaxMainCode",iJcicZ042.getMaxMainCode());
			totaVo.putParam("L8r13IsClaims", iJcicZ042.getIsClaims());
			totaVo.putParam("L8r13GuarLoanCnt", iJcicZ042.getGuarLoanCnt());
			totaVo.putParam("L8r13ExpLoanAmt", iJcicZ042.getExpLoanAmt());
			totaVo.putParam("L8r13Civil323ExpAmt", iJcicZ042.getCivil323ExpAmt());
			totaVo.putParam("L8r13ReceExpAmt", iJcicZ042.getReceExpAmt());
			totaVo.putParam("L8r13CashCardAmt", iJcicZ042.getCashCardAmt());
			totaVo.putParam("L8r13Civil323CashAmt", iJcicZ042.getCivil323CashAmt());
			totaVo.putParam("L8r13ReceCashAmt", iJcicZ042.getReceCashAmt());
			totaVo.putParam("L8r13CreditCardAmt", iJcicZ042.getCreditCardAmt());
			totaVo.putParam("L8r13Civil323CreditAmt", iJcicZ042.getCivil323CreditAmt());
			totaVo.putParam("L8r13ReceCreditAmt", iJcicZ042.getReceCreditAmt());
			totaVo.putParam("L8r13ReceExpPrin", iJcicZ042.getReceExpPrin());
			totaVo.putParam("L8r13ReceExpInte", iJcicZ042.getReceExpInte());
			totaVo.putParam("L8r13ReceExpPena", iJcicZ042.getReceExpPena());
			totaVo.putParam("L8r13ReceExpOther", iJcicZ042.getReceExpOther());
			totaVo.putParam("L8r13CashCardPrin", iJcicZ042.getCashCardPrin());
			totaVo.putParam("L8r13CashCardInte", iJcicZ042.getCashCardInte());
			totaVo.putParam("L8r13CashCardPena", iJcicZ042.getCashCardPena());
			totaVo.putParam("L8r13CashCardOther", iJcicZ042.getCashCardOther());
			totaVo.putParam("L8r13CreditCardPrin", iJcicZ042.getCreditCardPrin());
			totaVo.putParam("L8r13CreditCardInte", iJcicZ042.getCreditCardInte());
			totaVo.putParam("L8r13CreditCardPena", iJcicZ042.getCreditCardPena());
			totaVo.putParam("L8r13CreditCardOther", iJcicZ042.getCreditCardOther());
			totaVo.putParam("L8r13TranKey", iJcicZ042.getTranKey());	
			totaVo.putParam("L8r13OutJcicTxtDate", iJcicZ042.getOutJcicTxtDate());
		}	
		this.addList(this.totaVo);
		return this.sendList();
	}
}