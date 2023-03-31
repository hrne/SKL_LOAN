package com.st1.itx.trade.L9;

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
import com.st1.itx.util.http.WebClient;

/**
 * L9742Batch
 * 
 * @author Chih Wei
 * @version 1.0.0
 */
@Service("L9742Batch")
@Scope("step")
public class L9742Batch extends BatchBase implements Tasklet, InitializingBean {

	@Autowired
	L9742Report l9742Report;

	@Autowired
	DateUtil dDateUtil;

	@Autowired
	WebClient webClient;

	String tranCode = "L9742";
	String tranName = "企金戶還本收據及繳息收據";

	@Override
	public void afterPropertiesSet() throws Exception {
	}

	@Override
	public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
		l9742Report.setParentTranCode(this.getParent());
		return this.exec(contribution, "D");
	}

	@Override
	public void run() throws LogicException {
		this.info("active L9742Batch ");

		int tbsdyf = this.txBuffer.getTxCom().getTbsdyf();

		titaVo.putParam("inputAcDate", tbsdyf);
		titaVo.putParam("inputSlipNoStart", 0);
		titaVo.putParam("inputSlipNoEnd", 999999);
		titaVo.putParam("inputTitaTxtNoStart", 0);
		titaVo.putParam("inputTitaTxtNoEnd", 99999999);

		// 各產一次 1:還本收據;2:繳息收據
		l9742Report.exec(titaVo,1);
		l9742Report.exec(titaVo,2);
	}
}