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

@Service("LM009")
@Scope("step")
/**
 * 
 * 
 * @author Eric Chang
 * @version 1.0.0
 */
public class LM009 extends BatchBase implements Tasklet, InitializingBean {

	@Autowired
	LM009Report lM009report;

	@Autowired
	WebClient webClient;
	
	@Autowired
	DateUtil dDateUtil;
	
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
		this.info("active LM009 ");
		lM009report.setParentTranCode(this.getParent());
		lM009report.exec(titaVo);

		webClient.sendPost(dDateUtil.getNowStringBc(), "1800", titaVo.getParam("TLRNO"), "Y", "LC009", titaVo.getParam("TLRNO"), "LM009應收利息總表已完成", titaVo);
	}
}