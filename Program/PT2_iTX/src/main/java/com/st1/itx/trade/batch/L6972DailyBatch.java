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
import com.st1.itx.db.service.TxArchiveTableService;
import com.st1.itx.eum.ContentName;
import com.st1.itx.tradeService.BatchBase;
import com.st1.itx.util.MySpring;

@Service("L6972DailyBatch")
@Scope("step")
/**
 * (日終批次) 搬運資料<BR>
 * L6972搬運資料衍生問題相關議題<BR>
 * 會議記錄<BR>
 * 2.2.搬運資料作業應加入月底批次，系統自動完成。
 * 
 * @author Chih Wei
 * @version 1.0.0
 */
public class L6972DailyBatch extends BatchBase implements Tasklet, InitializingBean {

	@Autowired
	TxArchiveTableService txArchiveTableService;

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
		return this.exec(contribution, "D");
	}

	@Override
	public void run() throws LogicException {
		this.info("active L6972DailyBatch ");

		txArchiveTableService.Usp_L6_ArchiveFiveYearTx_Copy(titaVo.getEntDyI() + 19110000, titaVo.getTlrNo(), titaVo);
		
		// 2023-06-07 Wei 搬運完資料,發動刪除資料
		MySpring.newTask("L6971p", this.txBuffer, titaVo);
	}

}