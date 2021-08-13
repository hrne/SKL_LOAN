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

@Service("LM008")
@Scope("step")

/**
 * LM008-應收利息明細表
 * 
 * @author ST1-Wei
 * @version 1.0.0
 */
public class LM008 extends BatchBase implements Tasklet, InitializingBean {

	@Autowired
	LM008Report lM008report;

	@Override
	public void afterPropertiesSet() throws Exception {
		;
	}

	@Override
	public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
		// logger = LoggerFactory.getLogger(LM008.class);
		return this.exec(contribution, "M");
	}

	@Override
	public void run() throws LogicException {
		this.info("active LM008 ");
		lM008report.setParentTranCode(this.getParent());
		lM008report.setTxBuffer(this.getTxBuffer());
		lM008report.exec(titaVo);
	}

}