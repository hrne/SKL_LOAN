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

@Service("L7Ias34DpUpd")
@Scope("step")
/**
 * (每月日終批次)維護 Ias34Dp 每月IA34Dp資料欄位清單D檔
 *
 * @author Chih Wei
 * @version 1.0.0
 */
public class L7Ias34DpUpd extends BatchBase implements Tasklet, InitializingBean {

	@Autowired
	JobMainService sJobMainService;

	@Override
	public void afterPropertiesSet() throws Exception {
		this.titaVo.putParam(ContentName.empnot, "BAT001");
	}

	@Override
	public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
		// // logger = LoggerFactory.getLogger(L7Ias34DpUpd.class);

		// 第二個參數
		// D=日批
		// M=月批
		return this.exec(contribution, "M");
	}

	@Override
	public void run() throws LogicException {
		this.info("active L7Ias34DpUpd ");

		int tbsdyf = this.txBuffer.getTxCom().getTbsdyf();

		//int mfbsdyf = this.txBuffer.getTxCom().getMfbsdyf();

		String empNo = titaVo.getTlrNo();

		int newAcFg = 0;  // 新會計科目記號：0=使用舊會計科目(8碼) 1=使用新會計科目(11碼)

		this.info("active L7Ias34DpUpd tbsdyf=" + tbsdyf);
		this.info("active L7Ias34DpUpd empNo =" + empNo);

		// 此為月底日日終批次,讀onlineDB
//		this.titaVo.putParam(ContentName.dataBase, ContentName.onLine);

		sJobMainService.Usp_L7_Ias34Dp_Upd(tbsdyf, empNo, newAcFg);
	}

}