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

@Service("LM002")
@Scope("step")
/**
 * LM002-房貸專案放款
 * 
 * @author Eric Chang
 * @version 1.0.0
 */
public class LM002 extends BatchBase implements Tasklet, InitializingBean {

	@Autowired
	LM002Report lm002report;

	@Override
	public void afterPropertiesSet() throws Exception {}

	@Override
	public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
		// logger = LoggerFactory.getLogger(LM002.class);
		return this.exec(contribution, "M");
	}

	public void run() throws LogicException {
		this.info("active LM002 ");
		lm002report.setTxBuffer(this.getTxBuffer());
		lm002report.exec(titaVo);
	}

}
