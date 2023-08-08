package com.st1.itx.trade.LM;

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
import com.st1.itx.util.parse.Parse;

@Service("LM014")
@Scope("step")
/**
 * 
 * 
 * @author ChihWei
 * @version 1.0.0
 */
public class LM014 extends BatchBase implements Tasklet, InitializingBean {

	@Autowired
	LM014Report lM014report;

	@Autowired
	WebClient webClient;
	
	@Autowired
	DateUtil dDateUtil;

	@Autowired
	Parse parse;
	
	@Override
	public void afterPropertiesSet() throws Exception {
		;
	}

	@Override
	public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
		return this.exec(contribution, "M", chunkContext);
	}

	@Override
	public void run() throws LogicException {
		this.info("active LM014 ");
		
		int totalItem = parse.stringToInteger(titaVo.getParam("TotalItem"));

		String subReportCode = "";
		
		for (int i = 1; i <= totalItem; i++) {
			if (!titaVo.getParam("BtnShell" + i).trim().isEmpty() && titaVo.getParam("TradeCode" + i).contains("LM014")) {
				this.info(i + "====" + titaVo.getParam("TradeSub" + i));
				subReportCode = titaVo.getParam("TradeSub" + i);
				lM014report.exec(titaVo,subReportCode, titaVo.getParam("TradeCode" + i),titaVo.getParam("TradeName" + i));
			}
		}
		

	}
}