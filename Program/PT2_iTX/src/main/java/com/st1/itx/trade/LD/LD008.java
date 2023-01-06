package com.st1.itx.trade.LD;

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
import com.st1.itx.util.parse.Parse;

/**
 * LD008
 * 
 * @author ChihWei
 * @version 1.0.0
 */
@Service("LD008")
@Scope("step")
public class LD008 extends BatchBase implements Tasklet, InitializingBean {

	@Autowired
	LD008Report lD008report;

	@Autowired
	Parse parse;

	@Override
	public void afterPropertiesSet() throws Exception {
		;
	}

	@Override
	public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
		lD008report.setParentTranCode(this.getParent());
		return this.exec(contribution, "D");
	}

	@Override
	public void run() throws LogicException {
		this.info("active LD008 ");

		if (!titaVo.containsKey("TotalItem")) {
			lD008report.exec(titaVo, "0");
			lD008report.exec(titaVo, "1");
			return;
		}

		int totalItem = parse.stringToInteger(titaVo.getParam("TotalItem"));

		String subReportCode = "";

		for (int i = 1; i <= totalItem; i++) {
			if (!titaVo.getParam("BtnShell" + i).trim().isEmpty() && "LD008".equals(titaVo.getParam("TradeCode" + i))) {
				this.info(i + "====" + titaVo.getParam("TradeSub" + i));
				subReportCode = titaVo.getParam("TradeSub" + i);
				lD008report.exec(titaVo, subReportCode);
			}
		}

	}

}