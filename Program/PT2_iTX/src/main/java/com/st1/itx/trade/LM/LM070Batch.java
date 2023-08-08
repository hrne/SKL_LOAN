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
import com.st1.itx.db.domain.CdWorkMonth;
import com.st1.itx.db.service.CdWorkMonthService;
import com.st1.itx.tradeService.BatchBase;

/**
 * LM070Batch
 * 
 * @author  
 * @version 1.0.0
 */
@Service("LM070Batch")
@Scope("step")
public class LM070Batch extends BatchBase implements Tasklet, InitializingBean {

	@Autowired
	LM070Report LM070Report;

	@Autowired
	CdWorkMonthService sCdWorkMonthService;

	String tranCode = "LM070";
	String tranName = "介紹人加碼獎勵津貼明細";

	@Override
	public void afterPropertiesSet() throws Exception {
		;
	}

	@Override
	public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
		LM070Report.setParentTranCode(this.getParent());
		return this.exec(contribution, "D", chunkContext);
	}

	@Override
	public void run() throws LogicException {
		this.info("active LM070Batch");

		int tbsdyf = this.txBuffer.getTxCom().getTbsdyf();

		CdWorkMonth cdWorkMonth = sCdWorkMonthService.findDateFirst(tbsdyf, tbsdyf, titaVo);

		int year = cdWorkMonth.getYear() - 1911;
		int month = cdWorkMonth.getMonth();

		
		titaVo.putParam("inputYear", year);
		titaVo.putParam("inputMonth", month);

		LM070Report.exec(titaVo);
	}
}