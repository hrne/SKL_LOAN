package com.st1.itx.trade.LM;

import java.util.List;

import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;

import com.st1.itx.Exception.LogicException;
import com.st1.itx.db.domain.MonthlyLM042RBC;
import com.st1.itx.db.service.MonthlyLM042RBCService;
import com.st1.itx.tradeService.BatchBase;
import com.st1.itx.util.date.DateUtil;

/**
 * LM042Batch
 * 
 * @author
 * @version 1.0.0
 */
@Service("LM042Batch")
@Scope("step")
public class LM042Batch extends BatchBase implements Tasklet, InitializingBean {

	@Autowired
	LMR42 lMR42;

	@Autowired
	LM042Report lM042Report;

	@Autowired
	MonthlyLM042RBCService sMonthlyLM042RBCService;

	@Autowired
	DateUtil dateUtil;

	String tranCode = "LM042";
	String tranName = "RBC表_會計部";

	@Override
	public void afterPropertiesSet() throws Exception {
	}

	@Override
	public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
		lM042Report.setParentTranCode(this.getParent());
		return this.exec(contribution, "D", chunkContext);
	}

	@Override
	public void run() throws LogicException {
		this.info("active LM042Batch");
		
		// 會計日期: 系統會計日 YYYMMDD
		int acDate =titaVo.getEntDyI();
		titaVo.putParam("YearMonth", acDate);
		
		//先檢查當月資料 新增或更新
		lMR42.run(titaVo);
		
		int yearMonth = (acDate / 100) + 191100;
				
		Slice<MonthlyLM042RBC> sMonthlyLM042RBC;
		sMonthlyLM042RBC = sMonthlyLM042RBCService.findYearMonthAll(yearMonth, 0, 12, titaVo);		
		List<MonthlyLM042RBC> lMonthlyLM042RBC = sMonthlyLM042RBC == null ? null : sMonthlyLM042RBC.getContent();
		
		for (MonthlyLM042RBC tMonthlyLM042RBC : lMonthlyLM042RBC) {
			if ("1".equals(tMonthlyLM042RBC.getLoanType())) {
				switch (tMonthlyLM042RBC.getLoanItem()) {
				case "A":
					titaVo.putParam("RiskFactor1", tMonthlyLM042RBC.getRiskFactor());
					break;
				case "B":
					titaVo.putParam("RiskFactor2", tMonthlyLM042RBC.getRiskFactor());
					break;
				case "C":
					titaVo.putParam("RiskFactor3", tMonthlyLM042RBC.getRiskFactor());
					break;
				case "D":
					titaVo.putParam("RiskFactor4", tMonthlyLM042RBC.getRiskFactor());
					break;
				case "E":
					titaVo.putParam("RiskFactor5", tMonthlyLM042RBC.getRiskFactor());
					break;
				case "F":
					titaVo.putParam("RiskFactor6", tMonthlyLM042RBC.getRiskFactor());
					break;
				}

			}

		}
		
		dateUtil.init();
		dateUtil.setDate_1(dateUtil.getNowIntegerRoc() / 100 * 100 + dateUtil.getMonLimit());
		dateUtil.setDate_2((dateUtil.getDate_1Integer() / 100 * 100)-100 + dateUtil.getMonLimit());

		this.info("lastYMD="+dateUtil.getDate_2Integer());
		this.info("thisYMD="+dateUtil.getDate_1Integer());

		lM042Report.exec(titaVo,dateUtil.getDate_2Integer(),dateUtil.getDate_1Integer());
	}
}