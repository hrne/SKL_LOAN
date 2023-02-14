package com.st1.itx.trade.LY;

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
 * LY006
 * 
 * @author Ted Lin
 * @version 1.0.0
 */
@Service("LY006")
@Scope("step")
public class LY006 extends BatchBase implements Tasklet, InitializingBean {

	@Autowired
	LY006Report ly006report;

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
		this.info("active LY006 ");
		ly006report.setTxBuffer(this.getTxBuffer());
		ly006report.exec(titaVo);
		
//		String ntxbuf = titaVo.getTlrNo() + FormatUtil.padX("LY006", 60)+Integer.parseInt(titaVo.getEntDy());
//
//		webClient.sendPost(dDateUtil.getNowStringBc(), dDateUtil.getNowStringTime(), titaVo.getTlrNo(), "Y", "LC009", "LY006 B117關係人明細表", titaVo);
		webClient.sendPost(dDateUtil.getNowStringBc(), dDateUtil.getNowStringTime(), titaVo.getTlrNo(), "Y", "LC009", titaVo.getTlrNo(), "LY006 B117關係人明細表",titaVo);
	}
	
	
	

}
