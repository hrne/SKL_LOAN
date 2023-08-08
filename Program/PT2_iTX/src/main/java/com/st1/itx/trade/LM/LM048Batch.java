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
 * LM048 企業放款風險承擔限額控管表
 * 
 * @author Chih Wei Huang
 * @version 1.0.0
 */
@Service("LM048Batch")
@Scope("step")
public class LM048Batch extends BatchBase implements Tasklet, InitializingBean {

	@Autowired
	LM048Report lM048Report;

	String tranCode = "LM048";
	String tranName = "企業放款風險承擔限額控管表";

	@Override
	public void afterPropertiesSet() throws Exception {
		;
	}

	@Override
	public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
		lM048Report.setParentTranCode(this.getParent());
		return this.exec(contribution, "D", chunkContext);
	}

	@Override
	public void run() throws LogicException {
		this.info("active LM048Batch");

		int inputYearMonth = Integer.parseInt(titaVo.getParam("ENTDY")) / 100;

		int entLoanBalLimit = 0;

		titaVo.putParam("YearMonth", inputYearMonth);
		titaVo.putParam("EntLoanBalLimit", entLoanBalLimit);

		lM048Report.exec(titaVo);
	}
}