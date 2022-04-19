package com.st1.itx.trade.LM;

import java.math.BigDecimal;

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
 * LM012Batch
 * 
 * @author xiangwei
 * @version 1.0.0
 */
@Service("LM012Batch")
@Scope("step")
public class LM012Batch extends BatchBase implements Tasklet, InitializingBean {

	@Autowired
	LM012Report lM012Report;

	@Autowired
	DateUtil dateUtil;

	@Override
	public void afterPropertiesSet() throws Exception {
	}

	@Override
	public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
		lM012Report.setParentTranCode(this.getParent());
		return this.exec(contribution, "M");
	}

	@Override
	public void run() throws LogicException {
		this.info("active LM012Batch ");

		// 利率上限下限: 1~6 (參照樣張)
		titaVo.putParam("RateMinimum", 1);
		titaVo.putParam("RateMaximum", 6);

		// 利率區間: 0.25 (參照樣張)
		titaVo.putParam("RateRange", new BigDecimal("0.25"));

		// 年月: 當日 YYY / MM
		titaVo.putParam("DataYear", titaVo.getEntDyI() / 10000);
		titaVo.putParam("DataMonth", titaVo.getEntDyI() / 100 % 100);

		lM012Report.exec(titaVo);
	}
}