
package com.st1.itx.trade.LH;

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
 * LH001
 * 
 * @author Ted Lin
 * @version 1.0.0
 */
@Service("LH001")
@Scope("step")
public class LH001 extends BatchBase implements Tasklet, InitializingBean {

	@Autowired
	LH001Report lH001Report;

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
		return this.exec(contribution, "M");
	}

	@Override
	public void run() throws LogicException {
		this.info("active LH001 ");
		lH001Report.setTxBuffer(this.getTxBuffer());
		lH001Report.exec(titaVo);

		webClient.sendPost(dDateUtil.getNowStringBc(), dDateUtil.getNowStringTime(), titaVo.getTlrNo(), "Y", "LC009",
				titaVo.getTlrNo(), "LH001表A07_會計部申報表", titaVo);
	}

}
