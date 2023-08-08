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
 * L9137Batch
 * 
 * @author Ted
 * @version 1.0.0
 */
@Service("L9137Batch")
@Scope("step")
public class L9137Batch extends BatchBase implements Tasklet, InitializingBean {

	@Autowired
	L9137Report L9137Report;

	@Autowired
	DateUtil dDateUtil;

	@Autowired
	WebClient webClient;

	String tranCode = "L9137";
	String tranName = "放款餘額總表";

	@Override
	public void afterPropertiesSet() throws Exception {
	}

	@Override
	public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
		L9137Report.setParentTranCode(this.getParent());
		return this.exec(contribution, "D", chunkContext);
	}

	@Override
	public void run() throws LogicException {
		this.info("active L9137Batch ");

		// 各產一張 0:放款餘額總表;1:關係人放款餘額總表
//		titaVo.putParam("inputShowType", 0);

		L9137Report.exec(titaVo, "0");

		// 各產一張 0:放款餘額總表;1:關係人放款餘額總表
//		titaVo.putParam("inputShowType", 1);

		L9137Report.exec(titaVo, "1");
	}
}