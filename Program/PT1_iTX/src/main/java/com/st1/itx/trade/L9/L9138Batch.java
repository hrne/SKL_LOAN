package com.st1.itx.trade.L9;

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
import com.st1.itx.util.date.DateUtil;
import com.st1.itx.util.http.WebClient;

/**
 * L9138Batch
 * 
 * @author Ted
 * @version 1.0.0
 */
@Service("L9138Batch")
@Scope("step")
public class L9138Batch extends BatchBase implements Tasklet, InitializingBean {

	@Autowired
	L9138Report L9138Report;

	@Autowired
	DateUtil dDateUtil;

	@Autowired
	WebClient webClient;

	String tranCode = "L9138";
	String tranName = "放款餘額總表";

	@Override
	public void afterPropertiesSet() throws Exception {
	}

	@Override
	public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
		L9138Report.setParentTranCode(this.getParent());
		return this.exec(contribution, "D", chunkContext);
	}

	@Override
	public void run() throws LogicException {
		this.info("active L9138Batch ");

		// 帳務日(民國)
		int tbsdy = this.txBuffer.getTxCom().getTbsdy();


		L9138Report.exec(titaVo,tbsdy);


	}
}