package com.st1.itx.trade.LQ;

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
@Service("LQ003")
@Scope("step")
public class LQ003 extends BatchBase implements Tasklet, InitializingBean {

	@Autowired
	LQ003Report lQ003Report;

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
		this.info("active LQ003 ");
		lQ003Report.setTxBuffer(this.getTxBuffer());
		lQ003Report.exec(titaVo);
		webClient.sendPost(dDateUtil.getNowStringBc(), dDateUtil.getNowStringTime(), titaVo.getTlrNo(), "Y", "LC009",
				titaVo.getTlrNo(), "LQ003住宅違約統計季報_服務課申報表", titaVo);
	}

}
