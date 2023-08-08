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

/**
 * LM083Batch
 * 
 * @author
 * @version 1.0.0
 */
@Service("LM083Batch")
@Scope("step")
public class LM083Batch extends BatchBase implements Tasklet, InitializingBean {

	@Autowired
	LM083Report lM083Report;

	@Override
	public void afterPropertiesSet() throws Exception {
	}

	@Override
	public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
		lM083Report.setParentTranCode(this.getParent());
		return this.exec(contribution, "M", chunkContext);
	}

	@Override
	public void run() throws LogicException {
		this.info("active LM083Batch ");
		int iBcDate = titaVo.getEntDyI() + 19110000;
		int inputBCYear = iBcDate / 10000;
		int inputMonth = (iBcDate / 100) % 100;

		titaVo.putParam("InputYear", inputBCYear);
		titaVo.putParam("InputMonth", inputMonth);
		lM083Report.exec(titaVo);
	}
}