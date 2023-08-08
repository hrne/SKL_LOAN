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
 * LM086Batch
 * 
 * @author Chih Wei
 * @version 1.0.0
 */
@Service("LM086Batch")
@Scope("step")
public class LM086Batch extends BatchBase implements Tasklet, InitializingBean {

	@Autowired
	LM086Report lM086Report;

	@Override
	public void afterPropertiesSet() throws Exception {
	}

	@Override
	public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
		return this.exec(contribution, "M", chunkContext);
	}

	@Override
	public void run() throws LogicException {
		this.info("active LM086Batch ");
		lM086Report.setParentTranCode(this.getParent());
		int entdy = titaVo.getEntDyI();
		int year = entdy / 10000;
		int month = entdy / 100 % 100;
		titaVo.putParam("inputYear", year);
		titaVo.putParam("inputMonth", month);
		titaVo.putParam("lowerLimit", "0");
		lM086Report.exec(titaVo);
	}
}