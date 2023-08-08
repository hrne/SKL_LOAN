package com.st1.itx.trade.LD;

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
 * LD008Batch
 * 
 * @author Chih Wei
 * @version 1.0.0
 */
@Service("LD008Batch")
@Scope("step")
public class LD008Batch extends BatchBase implements Tasklet, InitializingBean {

	@Autowired
	LD008Report lD008Report;

	@Autowired
	DateUtil dDateUtil;

	@Autowired
	WebClient webClient;

	String tranCode = "LD008";
	String tranName = "放款餘額總表";

	@Override
	public void afterPropertiesSet() throws Exception {
	}

	@Override
	public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
		lD008Report.setParentTranCode(this.getParent());
		return this.exec(contribution, "D", chunkContext);
	}

	@Override
	public void run() throws LogicException {
		this.info("active LD008Batch ");

		// 各產一張 0:放款餘額總表;1:關係人放款餘額總表
//		titaVo.putParam("inputShowType", 0);

		lD008Report.exec(titaVo, "0");

		// 各產一張 0:放款餘額總表;1:關係人放款餘額總表
//		titaVo.putParam("inputShowType", 1);

		lD008Report.exec(titaVo, "1");
	}
}