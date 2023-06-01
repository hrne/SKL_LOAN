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

@Service("LM013")
@Scope("step")
/**
 * 
 * 
 * @author ChihWei
 * @version 1.0.0
 */
public class LM013 extends BatchBase implements Tasklet, InitializingBean {

	@Autowired
	LM013Report lM013report;

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
		return this.exec(contribution, "M");
	}

	@Override
	public void run() throws LogicException {
		this.info("active LM014 ");

		int totalItem = parse.stringToInteger(titaVo.getParam("TotalItem"));

		String subReportCode = "";

		// 金檢日期: 當日 YYYMMDD
		titaVo.putParam("inputDate", titaVo.getEntDyI());
		
		// 核貸總值分界(預設一億)
		titaVo.putParam("inputAmount", 100000000);

		
		
		for (int i = 1; i <= totalItem; i++) {
			if (!titaVo.getParam("BtnShell" + i).trim().isEmpty()
					&& titaVo.getParam("TradeCode" + i).contains("LM013")) {
				this.info(i + "====" + titaVo.getParam("TradeSub" + i));
				subReportCode = titaVo.getParam("TradeCode" + i).trim();
				subReportCode = subReportCode.length() == 5 ? "0"
						: subReportCode.substring(subReportCode.length() - 1, subReportCode.length());
				lM013report.exec(titaVo, titaVo.getParam("TradeCode" + i), titaVo.getParam("TradeName" + i),
						parse.stringToInteger(subReportCode));
			}
		}

	}
}