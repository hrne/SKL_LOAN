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
import com.st1.itx.util.date.DateUtil;
import com.st1.itx.util.http.WebClient;

/**
 * LM050
 * 
 * @author Chih Wei
 * @version 1.0.0
 */
@Service("LM050")
@Scope("step")
public class LM050 extends BatchBase implements Tasklet, InitializingBean {

	@Autowired
	LM050Report lM050Report;

	@Autowired
	DateUtil dDateUtil;

	@Autowired
	WebClient webClient;

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
		this.info("active LM050 ");
		lM050Report.exec(titaVo);
		webClient.sendPost(dDateUtil.getNowStringBc(), "1800", titaVo.getTlrNo(), "Y", "LC009", titaVo.getTlrNo(), "LM050放款保險法第3條利害關係人放款餘額表_限額控管已完成", titaVo);

	}
}