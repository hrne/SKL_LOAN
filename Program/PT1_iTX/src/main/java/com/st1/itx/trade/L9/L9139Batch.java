package com.st1.itx.trade.L9;

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
 * L9139Batch
 * 
 * @author Chih Wei
 * @version 1.0.0
 */
@Service("L9139Batch")
@Scope("step")
public class L9139Batch extends BatchBase implements Tasklet, InitializingBean {

	@Autowired
	L9139Report oL9139Report;

	@Autowired
	DateUtil dDateUtil;

	@Autowired
	WebClient webClient;

	@Override
	public void afterPropertiesSet() throws Exception {
	}

	@Override
	public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
		oL9139Report.setParentTranCode(this.getParent());
		return this.exec(contribution, "D");
	}

	@Override
	public void run() throws LogicException {
		this.info("active L9139Batch ");

		String tranCode = "L9139";
		String tranName = "暫收款日餘額前後差異比較表";

		this.info(tranCode + "p titaVo.getTxcd() = " + titaVo.getTxcd());
		String parentTranCode = titaVo.getTxcd();

		oL9139Report.setParentTranCode(parentTranCode);

		// 帳務日(民國)
		int tbsdy = this.txBuffer.getTxCom().getTbsdy();

		// ServiceImpl.findAll 接收民國年月日
		titaVo.putParam("StartDate", tbsdy);
		titaVo.putParam("EndDate", tbsdy);

		boolean isFinish = oL9139Report.exec(titaVo);

		if (isFinish) {
			webClient.sendPost(dDateUtil.getNowStringBc(), "1800", titaVo.getParam("TLRNO"), "Y", "LC009",
					titaVo.getParam("TLRNO"), tranCode + tranName + "已完成", titaVo);
		} else {
			webClient.sendPost(dDateUtil.getNowStringBc(), "1800", titaVo.getParam("TLRNO"), "Y", "LC009",
					titaVo.getParam("TLRNO"), tranCode + tranName + "查無資料", titaVo);
		}
	}
}