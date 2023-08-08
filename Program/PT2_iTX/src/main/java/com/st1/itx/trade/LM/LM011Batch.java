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
 * LM011Batch
 * 
 * @author xiangwei
 * @version 1.0.0
 */
@Service("LM011Batch")
@Scope("step")
public class LM011Batch extends BatchBase implements Tasklet, InitializingBean {

	@Autowired
	LM011Report lM011Report;

	@Override
	public void afterPropertiesSet() throws Exception {
	}

	@Override
	public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
		lM011Report.setParentTranCode(this.getParent());
		return this.exec(contribution, "M", chunkContext);
	}

	@Override
	public void run() throws LogicException {
		this.info("active LM011Batch ");
		
		// 年月: 當日YYY / MM
		titaVo.putParam("Year", titaVo.getEntDyI() / 10000);
		titaVo.putParam("Month", titaVo.getEntDyI() / 100 % 100);		
		
		// 是否重新產生: N
		//titaVo.putParam("RemakeYN", "N");
		
		lM011Report.exec(titaVo);
	}
}