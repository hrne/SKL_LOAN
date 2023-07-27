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

@Service("L9YearlyHouseLoanIntUpd")
@Scope("step")
/**
 * (年底日日終批次)維護 YearlyHouseLoanInt 每年房屋擔保借款繳息工作檔
 * 
 * @author Chih Wei
 * @version 1.0.0
 */
public class L9YearlyHouseLoanIntUpd extends BatchBase implements Tasklet, InitializingBean {

	@Autowired
	JobMainService sJobMainService;

	@Override
	public void afterPropertiesSet() throws Exception {
		this.titaVo.putParam(ContentName.empnot, "999999");
	}

	@Override
	public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {

		String txSeq = chunkContext.getStepContext().getStepExecution().getExecutionContext().getString("txSeq");
		this.titaVo.putParam("JobTxSeq", txSeq);
		// 第二個參數
		// D=日批
		// M=月批
		return this.exec(contribution, "Y", chunkContext);
	}

	@Override
	public void run() throws LogicException {
		this.info("active L9YearlyHouseLoanIntUpd ");

		int tbsdyf = this.txBuffer.getTxCom().getTbsdyf();

		// int mfbsdyf = this.txBuffer.getTxCom().getMfbsdyf();

		String empNo = titaVo.getTlrNo();

		// 此為月底日日終批次,讀onlineDB
//		this.titaVo.putParam(ContentName.dataBase, ContentName.onLine);

		String txSeq = titaVo.getParam("JobTxSeq");

		sJobMainService.Usp_L9_YearlyHouseLoanInt_Upd(tbsdyf, empNo, 0, 0, 0, "", txSeq, titaVo);
	}

}