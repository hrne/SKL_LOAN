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

@Service("LM054")
@Scope("step")
/**
 * 
 * @author Eric Chang
 * @version 1.0.0
 */
public class LM054 extends BatchBase implements Tasklet, InitializingBean {

	@Autowired
	public LM054Report lM054report;

	@Override
	public void afterPropertiesSet() throws Exception {
	}

	@Override
	public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
		return this.exec(contribution, "M");
	}

	@Override
	public void run() throws LogicException {
		this.info("active LM054 ");

		// 帳務日(西元)
		int tbsdy = this.txBuffer.getTxCom().getTbsdyf();
		// 月底日(西元)
		int mfbsdy = this.txBuffer.getTxCom().getMfbsdyf();
		// 上個月底日(西元)
		int lmndy = this.txBuffer.getTxCom().getLmndyf();

		// 判斷帳務日與月底日是否同一天
		if (tbsdy == mfbsdy) {
			lM054report.exec(titaVo, mfbsdy);
		} else {
			lM054report.exec(titaVo, lmndy);
		}

	}
}
