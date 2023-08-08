package com.st1.itx.trade.LP;

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
 * 
 * 
 * @author Ted Lin
 * @version 1.0.0
 */
@Service("LP003")
@Scope("step")
public class LP003 extends BatchBase implements Tasklet, InitializingBean {

	@Autowired
	LP003Report lP003Report;

	@Autowired
	WebClient webClient;

	@Autowired
	DateUtil dDateUtil;

	@Override
	public void afterPropertiesSet() throws Exception {
	}

	@Override
	public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
		return this.exec(contribution, "M", chunkContext);
	}

	@Override
	public void run() throws LogicException {
		this.info("active LP003 ");
		lP003Report.setTxBuffer(this.getTxBuffer());
		lP003Report.setParentTranCode(this.getParent());
		lP003Report.exec(titaVo);
		webClient.sendPost(dDateUtil.getNowStringBc(), dDateUtil.getNowStringTime(), titaVo.getTlrNo(), "Y", "LC009", titaVo.getTlrNo(), "LP003部專暨房專業績累計表", titaVo);
	}

}
