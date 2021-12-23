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
 * LM013Batch
 * 
 * @author xiangwei
 * @version 1.0.0
 */
@Service("LM013Batch")
@Scope("step")
public class LM013Batch extends BatchBase implements Tasklet, InitializingBean {

	@Autowired
	LM013Report lM013Report;

	@Override
	public void afterPropertiesSet() throws Exception {
	}

	@Override
	public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
		lM013Report.setParentTranCode(this.getParent());
		return this.exec(contribution, "M");
	}

	@Override
	public void run() throws LogicException {
		this.info("active LM013Batch ");
		
		// 金檢日期: 當日 YYYMMDD
		titaVo.putParam("inputDate", titaVo.getEntDyI());
		
		// 核貸總值分界: 一億(參照樣張) 
		titaVo.putParam("inputAmount", 100000000);
		
		lM013Report.exec(titaVo);
	}
}