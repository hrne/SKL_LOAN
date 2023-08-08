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
 * LY007Batch
 * 
 * @author xiangwei
 * @version 1.0.0
 */
@Service("LY007Batch")
@Scope("step")
public class LY007Batch extends BatchBase implements Tasklet, InitializingBean {

	@Autowired
	LY007Report lY007Report;

	@Override
	public void afterPropertiesSet() throws Exception {
	}

	@Override
	public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
		lY007Report.setParentTranCode(this.getParent());
		return this.exec(contribution, "M", chunkContext);
	}

	@Override
	public void run() throws LogicException {
		this.info("active LY007Batch ");
		
		int entdyf = titaVo.getEntDyI() + 19110000;

		int iYear = entdyf / 10000;

		int iMonth = entdyf % 10000;

		if (iMonth != 12) {
			iYear = iYear - 1;
		}


		// 年月: 年初~本月 YYY / MM
		titaVo.putParam("RocYear", iYear);
		
		lY007Report.exec(titaVo);
	}
}