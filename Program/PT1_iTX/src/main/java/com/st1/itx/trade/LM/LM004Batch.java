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
 * LM004Batch
 * 
 * @author 
 * @version 1.0.0
 */
@Service("LM004Batch")
@Scope("step")
public class LM004Batch extends BatchBase implements Tasklet, InitializingBean {

	@Autowired
	LM004Report LM004Report;


	String tranCode = "LM004";
	String tranName = "長中短期放款到其明細表";

	@Override
	public void afterPropertiesSet() throws Exception {
		;
	}

	@Override
	public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
		LM004Report.setParentTranCode(this.getParent());
		return this.exec(contribution, "D");
	}

	@Override
	public void run() throws LogicException {
		this.info("active LM004Batch");

		int today = Integer.valueOf(titaVo.getParam("ENTDY"));
		int prinbal = 0;

		titaVo.putParam("DueDate", today);
		titaVo.putParam("PrinBal", prinbal);

		LM004Report.exec(titaVo);
	}
}