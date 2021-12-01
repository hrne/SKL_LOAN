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
import com.st1.itx.db.domain.CdWorkMonth;
import com.st1.itx.db.service.CdWorkMonthService;
import com.st1.itx.tradeService.BatchBase;
import com.st1.itx.util.date.DateUtil;
import com.st1.itx.util.http.WebClient;

/**
 * LD006Batch
 * 
 * @author Chih Wei
 * @version 1.0.0
 */
@Service("LD006Batch")
@Scope("step")
public class LD006Batch extends BatchBase implements Tasklet, InitializingBean {

	@Autowired
	LD006Report lD006Report;

	@Autowired
	CdWorkMonthService sCdWorkMonthService;

	@Autowired
	DateUtil dDateUtil;

	@Autowired
	WebClient webClient;

	String tranCode = "LD006";
	String tranName = "三階放款明細統計";

	@Override
	public void afterPropertiesSet() throws Exception {
		;
	}

	@Override
	public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
		lD006Report.setParentTranCode(this.getParent());
		return this.exec(contribution, "D");
	}

	@Override
	public void run() throws LogicException {
		this.info("active LD006Batch ");

		int tbsdyf = this.txBuffer.getTxCom().getTbsdyf();

		CdWorkMonth cdWorkMonth = sCdWorkMonthService.findDateFirst(tbsdyf, tbsdyf, titaVo);

		int year = cdWorkMonth.getYear() - 1911;
		int month = cdWorkMonth.getMonth();

		titaVo.putParam("inputYearStart", year);
		titaVo.putParam("inputYearEnd", year);
		titaVo.putParam("inputMonthStart", month);
		titaVo.putParam("inputMonthEnd", month);

		lD006Report.exec(titaVo);
	}
}