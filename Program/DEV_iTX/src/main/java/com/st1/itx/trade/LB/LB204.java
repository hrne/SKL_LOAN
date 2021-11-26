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

@Service("LB204")
@Scope("step")
/**
 * 
 * 
 * @author Eric Chang
 * @version 1.0.0
 */
public class LB204 extends BatchBase implements Tasklet, InitializingBean {

	@Autowired
	LB204Report lB204Report;

	@Override
	public void afterPropertiesSet() throws Exception {
		;
	}

	@Override
	public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
		this.info("LB204 active RepeatStatus execute ");
		return this.exec(contribution, "M");
	}

	@Override
	public void run() throws LogicException {
		this.info("LB204 active LB204 ");
		this.info("LB204 titaVo.getEntDyI() =" + this.titaVo.getEntDyI());

		titaVo.putParam("AcDateStart", Integer.parseInt(titaVo.getEntDy()));
		titaVo.putParam("AcDateEnd", Integer.parseInt(titaVo.getEntDy()));
		
		lB204Report.exec(titaVo);
	}

}

//@Service("LB204")
//@Scope("prototype")
///**
// * 
// * 
// * @author Eric Chang
// * @version 1.0.0
// */
//public class LB204 extends TradeBuffer {
//	@SuppressWarnings("unused")
//
//	@Autowired
//	public LB204Report lb204Report;
//
//	@Override
//	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
//		this.info("active LB204 ");
//		this.totaVo.init(titaVo);
//
//		lb204Report.exec(titaVo);
//
//		this.addList(this.totaVo);
//		return this.sendList();
//	}
//
//}