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

@Service("LM081")
@Scope("step")
/**
 * 
 * 
 * @author Eric Chang
 * @version 1.0.0
 */
public class LM081 extends BatchBase implements Tasklet, InitializingBean {

	@Autowired
	LM081Report lm081report;

	@Override
	public void afterPropertiesSet() throws Exception {

	}

	@Override
	public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
		// logger = LoggerFactory.getLogger(LM081.class);
		return this.exec(contribution, "M");
	}

	@Override
	public void run() throws LogicException {
		this.info("active LM081Report ");
		lm081report.exec(titaVo);
	}

}
