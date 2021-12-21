package com.st1.itx.trade.L8;

import java.util.ArrayList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.dataVO.TotaVo;
import com.st1.itx.db.domain.JcicZ044;
import com.st1.itx.db.service.JcicZ044Service;
import com.st1.itx.tradeService.TradeBuffer;

@Service("L8R15")
@Scope("prototype")
/**
 * 
 * 
 * @author Mata
 * @version 1.0.0
 */
public class L8R15 extends TradeBuffer {
	/* DB服務注入 */
	@Autowired
	public JcicZ044Service iJcicZ044Service;

	@Override
	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
		this.info("active L8r15 ");
		this.totaVo.init(titaVo);
		String iUkey = titaVo.getParam("RimUkey");
		JcicZ044 iJcicZ044 = new JcicZ044();
		iJcicZ044 = iJcicZ044Service.ukeyFirst(iUkey, titaVo);

		if (iJcicZ044 == null) {
			throw new LogicException(titaVo, "E0001", ""); // 無此代號錯誤
		} else {
			totaVo.putParam("L8r15CustId", iJcicZ044.getCustId());
			totaVo.putParam("L8r15SubmitKey", iJcicZ044.getSubmitKey());
			totaVo.putParam("L8r15RcDate", iJcicZ044.getRcDate());
			totaVo.putParam("L8r15DebtCode", iJcicZ044.getDebtCode());
			totaVo.putParam("L8r15NonGageAmt", iJcicZ044.getNonGageAmt());
			totaVo.putParam("L8r15Period", iJcicZ044.getPeriod());
			totaVo.putParam("L8r15Rate", iJcicZ044.getRate());
			totaVo.putParam("L8r15MonthPayAmt", iJcicZ044.getMonthPayAmt());
			totaVo.putParam("L8r15ReceYearIncome", iJcicZ044.getReceYearIncome());
			totaVo.putParam("L8r15ReceYear", iJcicZ044.getReceYear());
			totaVo.putParam("L8r15ReceYear2Income", iJcicZ044.getReceYear2Income());
			totaVo.putParam("L8r15ReceYear2", iJcicZ044.getReceYear2());
			totaVo.putParam("L8r15CurrentMonthIncome", iJcicZ044.getCurrentMonthIncome());
			totaVo.putParam("L8r15LivingCost", iJcicZ044.getLivingCost());
			totaVo.putParam("L8r15CompName", iJcicZ044.getCompName());
			totaVo.putParam("L8r15CompId", iJcicZ044.getCompId());
			totaVo.putParam("L8r15CarCnt", iJcicZ044.getCarCnt());
			totaVo.putParam("L8r15HouseCnt", iJcicZ044.getHouseCnt());
			totaVo.putParam("L8r15LandCnt", iJcicZ044.getLandCnt());
			totaVo.putParam("L8r15ChildCnt", iJcicZ044.getChildCnt());
			totaVo.putParam("L8r15ChildRate", iJcicZ044.getChildRate());
			totaVo.putParam("L8r15ParentCnt", iJcicZ044.getParentCnt());
			totaVo.putParam("L8r15ParentRate", iJcicZ044.getParentRate());
			totaVo.putParam("L8r15MouthCnt", iJcicZ044.getMouthCnt());
			totaVo.putParam("L8r15MouthRate", iJcicZ044.getMouthRate());
			totaVo.putParam("L8r15GradeType", iJcicZ044.getGradeType());
			totaVo.putParam("L8r15PayLastAmt", iJcicZ044.getPayLastAmt());
			totaVo.putParam("L8r15Period2", iJcicZ044.getPeriod2());
			totaVo.putParam("L8r15Rate2", iJcicZ044.getRate2());
			totaVo.putParam("L8r15MonthPayAmt2", iJcicZ044.getMonthPayAmt2());
			totaVo.putParam("L8r15PayLastAmt2", iJcicZ044.getPayLastAmt2());
			totaVo.putParam("L8r15TranKey", iJcicZ044.getTranKey());
			totaVo.putParam("L8r15OutJcicTxtDate", iJcicZ044.getOutJcicTxtDate());
		}
		this.addList(this.totaVo);
		return this.sendList();
	}
}