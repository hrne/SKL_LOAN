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
 * LM016Batch
 * 
 * @author  
 * @version 1.0.0
 */
@Service("LM016Batch")
@Scope("step")
public class LM016Batch extends BatchBase implements Tasklet, InitializingBean {

	@Autowired
	LM016Report LM016Report;

	String tranCode = "LM016";
	String tranName = "寬限條件控管繳息";

	@Override
	public void afterPropertiesSet() throws Exception {
		;
	}

	@Override
	public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
		LM016Report.setParentTranCode(this.getParent());
		return this.exec(contribution, "D", chunkContext);
	}

	@Override
	public void run() throws LogicException {
		this.info("active LM016Batch");

		String custNoMin = "0000000" ;
		String custNoMax = "9999999" ;
		
		titaVo.putParam("CustNoMin", custNoMin);
		titaVo.putParam("CustNoMax", custNoMax);

		LM016Report.exec(titaVo);
	}
}