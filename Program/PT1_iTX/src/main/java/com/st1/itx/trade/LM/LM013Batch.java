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
		return this.exec(contribution, "M", chunkContext);
	}

	@Override
	public void run() throws LogicException {
		this.info("active LM013Batch ");

		// 金檢日期: 當日 YYYMMDD
		titaVo.putParam("inputDate", titaVo.getEntDyI());

		// 核貸總值分界: 一億(參照樣張)
		titaVo.putParam("inputAmount", 100000000);
		// 0總表,1非關係自然人,2非關係法人,3關係自然人,4關係法人
		lM013Report.exec(titaVo, "LM013", "金檢報表", 0);
		lM013Report.exec(titaVo, "LM013", "非關係自然人", 1);
		lM013Report.exec(titaVo, "LM013", "關係自然人", 2);
		lM013Report.exec(titaVo, "LM013", "非關係法人", 3);
		lM013Report.exec(titaVo, "LM013", "關係法人", 4);
	}
}