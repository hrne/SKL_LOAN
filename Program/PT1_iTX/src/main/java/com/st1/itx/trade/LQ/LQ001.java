//
//import java.util.ArrayList;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.batch.core.StepContribution;
//import org.springframework.batch.core.scope.context.ChunkContext;
//import org.springframework.batch.core.step.tasklet.Tasklet;
//import org.springframework.batch.repeat.RepeatStatus;
//import org.springframework.beans.factory.InitializingBean;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.context.annotation.Scope;
//import org.springframework.stereotype.Service;
//
//import com.st1.itx.Exception.LogicException;
//import com.st1.itx.dataVO.TitaVo;
//import com.st1.itx.dataVO.TotaVo;
//import com.st1.itx.trade.LM.LM001;
//import com.st1.itx.trade.LM.LM001Report;
//import com.st1.itx.tradeService.BatchBase;
//import com.st1.itx.tradeService.TradeBuffer;
// @Service("LQ001")
// @Scope("step")
// /**
//  * 
//  * 
//  * @author Eric Chang
//  * @version 1.0.0
//  */
//public class LQ001 extends TradeBuffer {
//	@SuppressWarnings("unused")
//
//	@Autowired
//	public LQ001Report lq001report;
//
//	@Override
//	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
//		this.info("active LQ001 ");
//		this.totaVo.init(titaVo);
//
//		lq001report.exec(titaVo);
//		this.addList(this.totaVo);
//		return this.sendList();
//	}
//}
package com.st1.itx.trade.LQ;

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

@Service("LQ001")
@Scope("step")
/**
 * 
 * 
 * @author Ted Lin
 * @version 1.0.0
 */
public class LQ001 extends BatchBase implements Tasklet, InitializingBean {

	@Autowired
	LQ001Report lQ001Report;

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
		this.info("active LQ001 ");
		lQ001Report.setTxBuffer(this.getTxBuffer());
		lQ001Report.exec(titaVo);
	}

}
