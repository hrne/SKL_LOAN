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
 * LM033Batch
 * 
 * @author xiangwei
 * @version 1.0.0
 */
@Service("LM033Batch")
@Scope("step")
public class LM033Batch extends BatchBase implements Tasklet, InitializingBean {

	@Autowired
	LM033Report lM033Report;
	
	@Autowired
	DateUtil dateUtil;

	@Override
	public void afterPropertiesSet() throws Exception {
	}

	@Override
	public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
		lM033Report.setParentTranCode(this.getParent());
		return this.exec(contribution, "M", chunkContext);
	}

	@Override
	public void run() throws LogicException {
		this.info("active LM033Batch ");
		
		dateUtil.init();
		dateUtil.setDate_1(dateUtil.getNowIntegerRoc() / 100 * 100 + 1);
		dateUtil.setDate_2(dateUtil.getNowIntegerRoc() / 100 * 100 + dateUtil.getMonLimit());

		// 准駁日期區間: 本月月初至本月月底 YYYMMDD ~ YYYMMDD
		titaVo.putParam("inputDateStart", dateUtil.getDate_1Integer());
		titaVo.putParam("inputDateEnd", dateUtil.getDate_2Integer());

		lM033Report.exec(titaVo);
	}
}