package com.st1.itx.trade.batch;

import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.st1.itx.Exception.LogicException;
import com.st1.itx.eum.ContentName;
import com.st1.itx.tradeService.BatchBase;

/**
 * 日終批次
 * 
 * @author Chih Wei
 * @version 1.0.0
 */
@Service("EodFinal")
@Scope("step")
public class EodFinal extends BatchBase implements Tasklet, InitializingBean {

	@Override
	public void afterPropertiesSet() throws Exception {
		this.titaVo.putParam(ContentName.empnot, "999999");
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
		this.info("active EodFinal");

		// 帳務日
		int tbsdyf = this.txBuffer.getTxCom().getTbsdyf();
		// 月底日
		int mfbsdyf = this.txBuffer.getTxCom().getMfbsdyf();

		this.info("EodFinal tbsdyf : " + tbsdyf);
		this.info("EodFinal mfbsdyf : " + mfbsdyf);

		// 每月月底日才執行
		if (tbsdyf == mfbsdyf) {
			this.info("EodFinal 本日為月底日,執行月底日日終維護.");
		} else {
			// TODO: 非月底日,發動Oracle DB鏡像備份
			throw new LogicException("S0001", "本日非月底日,啟動備份.");
		}

		this.info("EodFinal exit.");
	}
}