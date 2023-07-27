package com.st1.itx.trade.batch;

import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.st1.itx.Exception.LogicException;
import com.st1.itx.eum.ContentName;
import com.st1.itx.tradeService.BatchBase;
import com.st1.itx.util.MySpring;

@Service("InsertAmlCustList")
@Scope("step")
/**
 * (日終批次)維護 AmlCustList AML每日有效客戶名單
 * 
 * @author Chih Wei
 * @version 1.0.0
 */
public class InsertAmlCustList extends BatchBase implements Tasklet, InitializingBean {

	@Override
	public void afterPropertiesSet() throws Exception {
		this.titaVo.putParam(ContentName.empnot, "999999");
	}

	@Override
	public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {

		String txSeq = chunkContext.getStepContext().getStepExecution().getExecutionContext().getString("txSeq");
		this.titaVo.putParam("JobTxSeq", txSeq);
		// 第二個參數
		// D=日批
		// M=月批
		return this.exec(contribution, "D", chunkContext);
	}

	@Override
	public void run() throws LogicException {
		this.info("active InsertAmlCustList ");
		// 執行背景交易
		MySpring.newTask("L8110Batch", this.txBuffer, titaVo);
	}
}