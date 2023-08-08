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

//@Service("LM027")
//@Scope("prototype")
///**
// * 
// * 
// * @author Eric Chang
// * @version 1.0.0
// */
//public class LM027 extends TradeBuffer {
//	@SuppressWarnings("unused")
//
//	@Autowired
//	public LM027Report lm027report;
//
//	@Override
//	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
//		this.info("active LM027 ");
//		this.totaVo.init(titaVo);
//
//		lm027report.exec(titaVo);
////		//測試用
//
//		this.addList(this.totaVo);
//		return this.sendList();
//	}
//
//}

@Service("LM027")
@Scope("step")
/**
 * 
 * 
 * @author Eric Chang
 * @version 1.0.0
 */
public class LM027 extends BatchBase implements Tasklet, InitializingBean {

	@Autowired
	LM027Report lm027report;

	@Override
	public void afterPropertiesSet() throws Exception {
	}

	@Override
	public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
		return this.exec(contribution, "M", chunkContext);
	}

	@Override
	public void run() throws LogicException {
		this.info("active LM027 ");
		lm027report.exec(titaVo);
	}

}