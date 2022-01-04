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

/**
 * LM003Batch
 * 
 * @author xiangwei
 * @version 1.0.0
 */
@Service("LM003Batch")
@Scope("step")
public class LM003Batch extends BatchBase implements Tasklet, InitializingBean {

	@Autowired
	LM003Report lM003Report;

	@Override
	public void afterPropertiesSet() throws Exception {
	}

	@Override
	public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
		lM003Report.setParentTranCode(this.getParent());
		return this.exec(contribution, "M");
	}

	@Override
	public void run() throws LogicException {
		this.info("active LM003Batch ");

		// 年月: 年初~本月 YYY / MM
		titaVo.putParam("inputYearStart", titaVo.getEntDyI() / 10000);
		titaVo.putParam("inputMonthStart", 1);

		titaVo.putParam("inputYearEnd", titaVo.getEntDyI() / 10000);
		titaVo.putParam("inputMonthEnd", titaVo.getEntDyI() / 100 % 100);

		lM003Report.exec(titaVo);
	}
}