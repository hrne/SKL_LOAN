package com.st1.itx.trade.batch;

import java.io.PrintWriter;
import java.io.StringWriter;

import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.st1.itx.Exception.LogicException;
import com.st1.itx.buffer.TxBuffer;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.eum.ContentName;
import com.st1.itx.tradeService.BatchBase;
import com.st1.itx.util.MySpring;

@Service("StartBS900")
@Scope("step")
/**
 * (月終批次)應收利息提存
 * 
 * @author Chih Wei
 * @version 1.0.0
 */
public class StartBS900 extends BatchBase implements Tasklet, InitializingBean {

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
		return this.exec(contribution, "M", chunkContext);
	}

	@Override
	public void run() throws LogicException {
		this.info("active StartBS900 ");

		TitaVo tempTitaVo = new TitaVo();

		try {
			tempTitaVo.init();
			tempTitaVo.putParam(ContentName.kinbr, "0000");
			tempTitaVo.putParam(ContentName.tlrno, "999999");
			tempTitaVo.putParam(ContentName.empnot, "999999");

			TxBuffer txBuffer = MySpring.getBean("txBuffer", TxBuffer.class);
			txBuffer.init(titaVo);

			// 應收利息提存
			MySpring.newTask("BS900", this.txBuffer, tempTitaVo);

		} catch (Exception e) {
			StringWriter errors = new StringWriter();
			e.printStackTrace(new PrintWriter(errors));
			this.error("StartBS900 error :" + errors.toString());
		} finally {
			tempTitaVo.clear();
			tempTitaVo = null;
			this.info("StartBS900 finished.");
		}
	}
}