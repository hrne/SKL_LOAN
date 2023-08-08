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

@Service("LM001")
@Scope("step")
/**
 * LM001-公會無自用住宅統計
 * 
 * @author Eric Chang
 * @version 1.0.0
 */
public class LM001 extends BatchBase implements Tasklet, InitializingBean {

	@Autowired
	LM001Report lM001Report;

	@Override
	public void afterPropertiesSet() throws Exception {
		;
	}

	@Override
	public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
		return this.exec(contribution, "M", chunkContext);
	}

	@Override
	public void run() throws LogicException {
		this.info("active LM001 ");
		lM001Report.setParentTranCode(this.getParent());
		lM001Report.setTxBuffer(this.getTxBuffer());
		lM001Report.exec(titaVo);
	}

}
