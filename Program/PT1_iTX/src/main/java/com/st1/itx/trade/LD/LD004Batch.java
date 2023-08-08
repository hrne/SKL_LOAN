package com.st1.itx.trade.LD;

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
 * LD004Batch
 * 
 * @author Chih Wei
 * @version 1.0.0
 */
@Service("LD004Batch")
@Scope("step")
public class LD004Batch extends BatchBase implements Tasklet, InitializingBean {

	@Autowired
	LD004Report lD004Report;

	@Autowired
	DateUtil dDateUtil;

	@Autowired
	WebClient webClient;

	String tranCode = "LD004";
	String tranName = "企金戶還本收據及繳息收據";

	@Override
	public void afterPropertiesSet() throws Exception {
	}

	@Override
	public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
		lD004Report.setParentTranCode(this.getParent());
		return this.exec(contribution, "D", chunkContext);
	}

	@Override
	public void run() throws LogicException {
		this.info("active LD004Batch ");

		int tbsdyf = this.txBuffer.getTxCom().getTbsdyf();

		titaVo.putParam("inputAcDate", tbsdyf);
		titaVo.putParam("inputSlipNoStart", 0);
		titaVo.putParam("inputSlipNoEnd", 999999);
		titaVo.putParam("inputTitaTxtNoStart", 0);
		titaVo.putParam("inputTitaTxtNoEnd", 99999999);

		// 各產一次 1:還本收據;2:繳息收據
		titaVo.putParam("inputOption", 1);

		lD004Report.exec(titaVo);

		// 各產一次 1:還本收據;2:繳息收據
		titaVo.putParam("inputOption", 2);

		lD004Report.exec(titaVo);
	}
}