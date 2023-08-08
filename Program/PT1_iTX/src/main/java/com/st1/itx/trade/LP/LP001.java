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

@Service("LP001")
@Scope("step")
/**
 * 
 * 
 * @author Ted Lin
 * @version 1.0.0
 */

public class LP001 extends BatchBase implements Tasklet, InitializingBean {

	@Autowired
	LP001Report lP001Report;

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
		this.info("active LP001 ");
		lP001Report.setTxBuffer(this.getTxBuffer());
		lP001Report.setParentTranCode(this.getParent());
		lP001Report.exec(titaVo);
		webClient.sendPost(dDateUtil.getNowStringBc(), dDateUtil.getNowStringTime(), titaVo.getTlrNo(), "Y", "LC009", titaVo.getTlrNo(), "LP001工作月放款審查課各區業績累計", titaVo);
	}

}
