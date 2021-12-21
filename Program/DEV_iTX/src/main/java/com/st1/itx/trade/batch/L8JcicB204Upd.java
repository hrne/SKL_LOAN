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

@Service("L8JcicB204Upd")
@Scope("step")
/**
 * (每日日終批次)維護 JcicB204 聯徵授信餘額日報檔
 *
 * @author Chih Wei
 * @version 1.0.0
 */
public class L8JcicB204Upd extends BatchBase implements Tasklet, InitializingBean {

	@Autowired
	JobMainService sJobMainService;

	@Override
	public void afterPropertiesSet() throws Exception {
		this.titaVo.putParam(ContentName.empnot, "BAT001");
	}

	@Override
	public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
		// // logger = LoggerFactory.getLogger(L8JcicB204Upd.class);

		// 第二個參數
		// D=日批
		// M=月批
		return this.exec(contribution, "D");
	}

	@Override
	public void run() throws LogicException {
		this.info("active L8JcicB204Upd ");

		int tbsdyf = this.txBuffer.getTxCom().getTbsdyf();

		// int mfbsdyf = this.txBuffer.getTxCom().getMfbsdyf();

		String empNo = titaVo.getTlrNo();

		this.info("active L8JcicB204Upd tbsdyf=" + tbsdyf);
		this.info("active L8JcicB204Upd empNo =" + empNo);

		// 此為月底日日終批次,讀onlineDB
//		this.titaVo.putParam(ContentName.dataBase, ContentName.onLine);

		sJobMainService.Usp_L8_JcicB204_Upd(tbsdyf, empNo);
	}

}