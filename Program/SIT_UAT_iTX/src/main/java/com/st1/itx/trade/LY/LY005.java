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


@Service("LY005")
@Scope("step")
/**
 * 
 * 
 * @author  Ted Lin
 * @version 1.0.0
 */
public class LY005 extends BatchBase implements Tasklet, InitializingBean {

	@Autowired
	LY005Report ly005report;

	@Override
	public void afterPropertiesSet() throws Exception {
		;
	}

	@Override
	public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
		// logger = LoggerFactory.getLogger(LY005.class);
		return this.exec(contribution, "M");
	}

	@Override
	public void run() throws LogicException {
		this.info("active LY005 ");
		ly005report.setTxBuffer(this.getTxBuffer());
		ly005report.exec(titaVo);
	}

}

