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
import com.st1.itx.tradeService.BatchBase;

@Service("LM060")
@Scope("step")
/**
 * 
 * 
 * @author Eric Chang
 * @version 1.0.0
 */
public class LM060 extends BatchBase implements Tasklet, InitializingBean {

	@Autowired
	LM060Report lm060report;

	@Override
	public void afterPropertiesSet() throws Exception {

	}

	@Override
	public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
		return this.exec(contribution, "M", chunkContext);
	}

	@Override
	public void run() throws LogicException {
		this.info("active LM060 ");

		// 帳務日(西元)
		int tbsdy = this.txBuffer.getTxCom().getTbsdyf();
		// 月底日(西元)
		int mfbsdy = this.txBuffer.getTxCom().getMfbsdyf();

		// 上個月底日(西元)
//		int lmndy = this.txBuffer.getTxCom().getLmndyf();
		// 年
		int iYear = mfbsdy / 10000;
		// 月
		int iMonth = (mfbsdy / 100) % 100;
		// 當年月
		int thisYM = 0;
		//月底日
//		int ymEnd = mfbsdy;

		// 月底日是否大於帳務日 判斷取哪個年月
		if (tbsdy < mfbsdy) {
			iYear = iMonth - 1 == 0 ? (iYear - 1) : iYear;
			iMonth = iMonth - 1 == 0 ? 12 : iMonth - 1;
//			ymEnd = lmndy;

		}

		thisYM = iYear * 100 + iMonth;

		lm060report.exec(titaVo, this.txBuffer.getTxCom(),thisYM);
	}
}
