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

/**
 * LM030Batch
 * 
 * @author xiangwei
 * @version 1.0.0
 */
@Service("LM030Batch")
@Scope("step")
public class LM030Batch extends BatchBase implements Tasklet, InitializingBean {

	@Autowired
	LM030Report lM030Report;

	@Override
	public void afterPropertiesSet() throws Exception {
	}

	@Override
	public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
		lM030Report.setParentTranCode(this.getParent());
		return this.exec(contribution, "M");
	}

	@Override
	public void run() throws LogicException {
		this.info("active LM030Batch ");

		// 會計日期: 系統會計日 YYYMMDD
		titaVo.putParam("AcDate", titaVo.getEntDyI());

		// 戶號: 0
		titaVo.putParam("CustNo", 0);

		// 額度: 0
		titaVo.putParam("FacmNo", 0);

		// 滯繳條件: 期數 1
		titaVo.putParam("DelayCondition", 1);

		// 滯繳期數: 4~5
		titaVo.putParam("OvduTermMin", 4);
		titaVo.putParam("OvduTermMax", 5);

		// 滯繳日數: 0
		titaVo.putParam("OvduDayMin", 0);
		titaVo.putParam("OvduDayMax", 0);

		// 繳款方式: 全部 0
		titaVo.putParam("PayMethod", 0);

		// 戶別: 全部 0
		titaVo.putParam("EntCode", 0);

		lM030Report.exec(titaVo);
	}
}