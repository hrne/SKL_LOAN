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

/**
 * LM040Batch
 * 
 * @author xiangwei
 * @version 1.0.0
 */
@Service("LM040Batch")
@Scope("step")
public class LM040Batch extends BatchBase implements Tasklet, InitializingBean {

	@Autowired
	LM040Report lM040Report;

	@Autowired
	DateUtil dateUtil;

	@Override
	public void afterPropertiesSet() throws Exception {
	}

	@Override
	public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
		lM040Report.setParentTranCode(this.getParent());
		return this.exec(contribution, "M");
	}

	@Override
	public void run() throws LogicException {
		this.info("active LM040Batch ");

		// InputDate: 日曆日 YYYMMDD
		titaVo.putParam("InputDate", dateUtil.getNowIntegerRoc());

		lM040Report.exec(titaVo);
	}
}