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
 * LM031Batch
 * 
 * @author xiangwei
 * @version 1.0.0
 */
@Service("LM031Batch")
@Scope("step")
public class LM031Batch extends BatchBase implements Tasklet, InitializingBean {

	@Autowired
	LM031Report lM031Report;
	
	@Autowired
	DateUtil dateUtil;

	@Override
	public void afterPropertiesSet() throws Exception {
	}

	@Override
	public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
		lM031Report.setParentTranCode(this.getParent());
		return this.exec(contribution, "M");
	}

	@Override
	public void run() throws LogicException {
		this.info("active LM031Batch ");
		
		dateUtil.init();
		dateUtil.setDate_1(dateUtil.getNowIntegerRoc());
		dateUtil.setDate_2(dateUtil.getDate_1Integer() / 100 * 100 + dateUtil.getMonLimit() + 30000);
	
		// 循環動用日期期限: 此月月底日+三年 (不確定 - 參考樣張推測而來) YYYMMDD
		titaVo.putParam("inputDate", dateUtil.getDate_2Integer());

		lM031Report.exec(titaVo);
	}
}