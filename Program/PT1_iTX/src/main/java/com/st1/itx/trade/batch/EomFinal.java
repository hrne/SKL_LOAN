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

/**
 * 月終批次
 * 
 * @author Chih Wei
 * @version 1.0.0
 */
@Service("EomFinal")
@Scope("step")
public class EomFinal extends BatchBase implements Tasklet, InitializingBean {

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
		this.info("active EomFinal");

		// 帳務日
		int tbsdyf = this.txBuffer.getTxCom().getTbsdyf();
		// 月底日
		int mfbsdyf = this.txBuffer.getTxCom().getMfbsdyf();

		this.info("EomFinal tbsdyf : " + tbsdyf);
		this.info("EomFinal mfbsdyf : " + mfbsdyf);

		String yearMonth = String.valueOf((tbsdyf / 100));

		// 每年年底日才執行
		if (yearMonth.length() >= 2 && yearMonth.substring(yearMonth.length() - 2).equals("12")) {
			this.info("EomFinal 本日為年底日,執行年底日日終維護.");
		} else {
			// TODO: 非年底日,發動Oracle DB鏡像備份
			throw new LogicException("S0001", "本日非年底日,啟動備份.");
		}
		
		this.info("EomFinal exit.");
	}
}