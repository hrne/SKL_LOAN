package com.st1.itx.trade.batch;

import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.st1.itx.Exception.LogicException;
import com.st1.itx.db.service.JobMainService;
import com.st1.itx.eum.ContentName;
import com.st1.itx.tradeService.BatchBase;
import com.st1.itx.util.date.DateUtil;
import com.st1.itx.util.MySpring;

@Service("StartBS996")
@Scope("step")
/**
 * (日終批次)啟動業績重算檔
 * 
 * @author Chih Wei
 * @version 1.0.0
 */
public class StartBS996 extends BatchBase implements Tasklet, InitializingBean {

	/* 日期工具 */
	@Autowired
	DateUtil dateUtil;

	@Autowired
	JobMainService sJobMainService;

	@Override
	public void afterPropertiesSet() throws Exception {
		this.titaVo.putParam(ContentName.empnot, "BAT001");
	}

	@Override
	public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {

		// 第二個參數
		// D=日批
		// M=月批
		return this.exec(contribution, "D");
	}

	@Override
	public void run() throws LogicException {
		this.info("active StartBS996 ");

		String parm = "0,N,0,0";

		titaVo.putParam("Parm", parm);

		MySpring.newTask("BS996", this.txBuffer, titaVo);
	}

}