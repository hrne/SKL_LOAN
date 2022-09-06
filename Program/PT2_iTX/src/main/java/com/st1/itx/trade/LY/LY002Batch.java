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

/**
 * LY002Batch
 * 
 * @author xiangwei
 * @version 1.0.0
 */
@Service("LY002Batch")
@Scope("step")
public class LY002Batch extends BatchBase implements Tasklet, InitializingBean {

	@Autowired
	LY002Report lY002Report;

	@Override
	public void afterPropertiesSet() throws Exception {
	}

	@Override
	public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
		lY002Report.setParentTranCode(this.getParent());
		return this.exec(contribution, "M");
	}

	@Override
	public void run() throws LogicException {
		this.info("active LY002Batch ");

		// 年月: 年初~本月 YYY / MM
		titaVo.putParam("RocYear", titaVo.getEntDyI() / 10000);

		lY002Report.exec(titaVo);
	}
}