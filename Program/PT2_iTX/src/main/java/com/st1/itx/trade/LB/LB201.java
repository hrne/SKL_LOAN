package com.st1.itx.trade.LB;

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

@Service("LB201")
@Scope("step")
/**
 * LB201
 * 
 * @author Eric Chang
 * @version 1.0.0
 */
public class LB201 extends BatchBase implements Tasklet, InitializingBean {

	@Autowired
	LB201Report lB201Report;

	@Autowired
	DateUtil dDateUtil; // 2021-12-20 智偉新增

	@Autowired
	WebClient webClient; // 2021-12-20 智偉新增

	@Override
	public void afterPropertiesSet() throws Exception {
		;
	}

	@Override
	public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
		this.info("LB201 active RepeatStatus execute ");
		return this.exec(contribution, "M");
	}

	@Override
	public void run() throws LogicException {
		this.info("LB201 active LB201 ");
		this.info("LB201 titaVo.getEntDyI() =" + this.titaVo.getEntDyI());

		// String tranCode = "LB201";
		// String tranName = "聯徵授信餘額月報檔";

		lB201Report.setTxBuffer(this.txBuffer);

		// this.titaVo.setDataBaseOnMon(); // 月報資料庫
		// 2021-12-20 智偉修改
		boolean isFinish = lB201Report.exec(titaVo); // 使用月報資料庫

		// webClient.sendPost(dDateUtil.getNowStringBc(), "1800", titaVo.getTlrNo(),
		// "Y", "LC009", titaVo.getTlrNo(),
		// tranCode + tranName + (isFinish ? "已完成" : "查無資料"), titaVo);

	}

}