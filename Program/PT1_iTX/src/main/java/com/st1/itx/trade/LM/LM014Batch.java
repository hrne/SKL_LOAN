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
 * LM014Batch
 * 
 * @author xiangwei
 * @version 1.0.0
 */
@Service("LM014Batch")
@Scope("step")
public class LM014Batch extends BatchBase implements Tasklet, InitializingBean {

	@Autowired
	LM014Report lM014Report;

	@Override
	public void afterPropertiesSet() throws Exception {
	}

	@Override
	public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
		lM014Report.setParentTranCode(this.getParent());
		return this.exec(contribution, "M", chunkContext);
	}

	@Override
	public void run() throws LogicException {
		this.info("active LM014Batch ");

		// 選項: 0 全部
//		titaVo.putParam("inputType", 0);

//		lM014Report.exec(titaVo,"0","平均利率月報表（全部）");
		lM014Report.exec(titaVo, "1", "LM014-1", "平均利率月報表（科目別）");
		lM014Report.exec(titaVo, "2", "LM014-2", "平均利率月報表（種類別）");
		lM014Report.exec(titaVo, "3", "LM014-3", "平均利率月報表（關係人）");
		lM014Report.exec(titaVo, "4", "LM014-4", "平均利率月報表（種類別+關係人）");
	}
}