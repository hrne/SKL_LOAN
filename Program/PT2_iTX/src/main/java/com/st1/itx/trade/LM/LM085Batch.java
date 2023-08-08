package com.st1.itx.trade.LM;

import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.st1.itx.Exception.LogicException;
import com.st1.itx.db.service.CdWorkMonthService;
import com.st1.itx.tradeService.BatchBase;

/**
 * LM070Batch
 * 
 * @author
 * @version 1.0.0
 */
@Service("LM085Batch")
@Scope("step")
public class LM085Batch extends BatchBase implements Tasklet, InitializingBean {

	@Autowired
	LM085Report lM085Report;

	@Autowired
	CdWorkMonthService sCdWorkMonthService;

	String tranCode = "LM085";
	String tranName = "逾期月報表";

	@Override
	public void afterPropertiesSet() throws Exception {
		;
	}

	@Override
	public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
		lM085Report.setParentTranCode(this.getParent());
		return this.exec(contribution, "D", chunkContext);
	}

	@Override
	public void run() throws LogicException {
		this.info("active LM085Batch");

		// 帳務日(西元)
		int tbsdy = this.txBuffer.getTxCom().getTbsdyf();
		// 月底日(西元)
		int mfbsdy = this.txBuffer.getTxCom().getMfbsdyf();
		// 上個月底日(西元)
		int lmndy = this.txBuffer.getTxCom().getLmndyf();

		// 年
		int iYear = mfbsdy / 10000;
		// 月
		int iMonth = (mfbsdy / 100) % 100;
		// 當年月
		int thisYM = 0;

		int lastYM = lmndy / 100;
		// 判斷帳務日與月底日是否同一天
		if (tbsdy < mfbsdy) {
			iYear = iMonth - 1 == 0 ? (iYear - 1) : iYear;
			iMonth = iMonth - 1 == 0 ? 12 : iMonth - 1;
		}

		thisYM = iYear * 100 + iMonth;

		// 千 thousand
		// 百萬 million
		// 億 thousandmillion

		titaVo.putParam("UnitCode", "0");
		titaVo.putParam("UnitName", "千元");

		lM085Report.exec(titaVo, thisYM,lastYM);
	}
}