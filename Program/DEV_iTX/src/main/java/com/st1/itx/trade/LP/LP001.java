package com.st1.itx.trade.LP;
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



@Service("LP001")
@Scope("step")
/**
 * 
 * 
 * @author Ted Lin
 * @version 1.0.0
 */


public class LP001 extends BatchBase implements Tasklet, InitializingBean {

	@Autowired
	LP001Report lp001report;

	@Override
	public void afterPropertiesSet() throws Exception {

	}
 
	@Override
	public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
		// logger = LoggerFactory.getLogger(LP001.class);
		return this.exec(contribution, "M");
	}
 
	@Override
	public void run() throws LogicException {
		this.info("active LP001 ");
//		lp001report.setTxBuffer(this.getTxBuffer());
		lp001report.setParentTranCode(this.getParent());
		lp001report.exec(titaVo);
	}

}

