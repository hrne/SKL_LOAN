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
		return this.exec(contribution, "D", chunkContext);
	}

	@Override
	public void run() throws LogicException {
		this.info("active LD006Batch ");

		int tbsdyf = this.txBuffer.getTxCom().getTbsdyf();

		CdWorkMonth cdWorkMonth = sCdWorkMonthService.findDateFirst(tbsdyf, tbsdyf, titaVo);
		
		if (cdWorkMonth == null)
			throw new LogicException("E0001", "放款業績工作月對照檔查無本日資料");

		int year = cdWorkMonth.getYear() - 1911;
		int month = cdWorkMonth.getMonth();
		
		// ServiceImpl.findAll 接收民國年月
		titaVo.putParam("workMonthStart", year * 100 + month);
		titaVo.putParam("workMonthEnd", year * 100 + month);
		titaVo.putParam("custNo", 0);
		titaVo.putParam("facmNo", 0);
		titaVo.putParam("Introducer","");
		
		lD006Report.exec(titaVo);
	}
}