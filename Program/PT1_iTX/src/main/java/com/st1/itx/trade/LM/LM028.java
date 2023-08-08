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

@Service("LM028")
@Scope("step")
/**
 * LM028-預估現金流量
 * 
 * @author Eric Chang
 * @version 1.0.0
 */
public class LM028 extends BatchBase implements Tasklet, InitializingBean {

	@Autowired
	LM028Report lM028Report;

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
		this.info("active LM028 ");
		this.info(this.getParent());
		lM028Report.setParentTranCode(this.getParent());
		lM028Report.setTxBuffer(this.getTxBuffer());
		lM028Report.exec(titaVo);
	}

}