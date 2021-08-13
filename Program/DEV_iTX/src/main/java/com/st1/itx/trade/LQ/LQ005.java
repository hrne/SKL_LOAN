//import java.util.ArrayList;
//
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.context.annotation.Scope;
//import org.springframework.stereotype.Service;
//
//import com.st1.itx.Exception.LogicException;
//import com.st1.itx.dataVO.TitaVo;
//import com.st1.itx.dataVO.TotaVo;
//import com.st1.itx.tradeService.TradeBuffer;

// @Service("LQ005")
// @Scope("step")
// /**
//  * 
//  * 
//  * @author  Eric Chang
//  * @version 1.0.0
//  */
//public class LQ005 extends TradeBuffer {
//	@SuppressWarnings("unused")
//	// private static final Logger logger = LoggerFactory.getLogger(LQ005.class);
//
//	@Autowired
//	public LQ005Report lq005report;
//
//	@Override
//	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
//		this.info("active LQ005 ");
//		this.totaVo.init(titaVo);
//
//		lq005report.exec(titaVo);
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


@Service("LQ005")
@Scope("step")
/**
 * 
 * 
 * @author  Ted Lin
 * @version 1.0.0
 */
public class LQ005 extends BatchBase implements Tasklet, InitializingBean {

	@Autowired
	LQ005Report lq005report;

	@Override
	public void afterPropertiesSet() throws Exception {
		;
	}

	@Override
	public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
		// logger = LoggerFactory.getLogger(LQ005.class);
		return this.exec(contribution, "M");
	}

	@Override
	public void run() throws LogicException {
		this.info("active LQ005 ");
		lq005report.setTxBuffer(this.getTxBuffer());
		lq005report.exec(titaVo);
	}

}

