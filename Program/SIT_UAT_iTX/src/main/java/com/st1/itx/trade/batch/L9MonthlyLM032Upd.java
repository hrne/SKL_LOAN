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

@Service("L9MonthlyLM032Upd")
@Scope("step")
/**
 * (月底日日終批次)維護MonthlyLM028月報工作檔
 * 
 * @author Chih Wei
 * @version 1.0.0
 */
public class L9MonthlyLM032Upd extends BatchBase implements Tasklet, InitializingBean {

	@Autowired
	JobMainService sJobMainService;

	@Override
	public void afterPropertiesSet() throws Exception {
		this.titaVo.putParam(ContentName.empnot, "BAT001");
	}

	@Override
	public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
		// // logger = LoggerFactory.getLogger(L9MonthlyLM032Upd.class);

		// 第二個參數
		// D=日批
		// M=月批
		return this.exec(contribution, "M");
	}

	@Override
	public void run() throws LogicException {
		this.info("active L9MonthlyLM032Upd ");

		// 報表日
		int tbsdyf = this.txBuffer.getTxCom().getTbsdyf();

		// 上月月底日
		int lmndyf = this.txBuffer.getTxCom().getLmndyf();

		String empNo = titaVo.getTlrNo();

		// 此為月底日日終批次,讀onlineDB
//		this.titaVo.putParam(ContentName.dataBase, ContentName.onLine);

		sJobMainService.Usp_L9_MonthlyLM032_Upd(tbsdyf, empNo, lmndyf, titaVo);
	}

}