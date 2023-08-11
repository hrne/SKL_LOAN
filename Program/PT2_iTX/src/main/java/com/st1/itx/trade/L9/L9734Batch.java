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
 * L9734Batch
 * 
 * @author Ted
 * @version 1.0.0
 */
@Service("L9734Batch")
@Scope("step")
public class L9734Batch extends BatchBase implements Tasklet, InitializingBean {

	@Autowired
	L9734Report l9734Report;

	@Autowired
	DateUtil dDateUtil;

	@Autowired
	WebClient webClient;

	@Override
	public void afterPropertiesSet() throws Exception {
	}

	@Override
	public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
		l9734Report.setParentTranCode(this.getParent());
		return this.exec(contribution, "D", chunkContext);
	}

	@Override
	public void run() throws LogicException {
		this.info("active L9734Batch ");

		int tbsdyf = this.txBuffer.getTxCom().getTbsdyf();

		l9734Report.exec(titaVo, tbsdyf, 1);
		l9734Report.exec(titaVo, tbsdyf, 2);
		l9734Report.exec(titaVo, tbsdyf, 3);
		l9734Report.exec(titaVo, tbsdyf, 4);
		l9734Report.exec(titaVo, tbsdyf, 5);
		l9734Report.exec(titaVo, tbsdyf, 6);

	}
}