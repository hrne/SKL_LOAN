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
// @Service("LP004")
// @Scope("step")
// /**
//  * 
//  * 
//  * @author Eric Chang
//  * @version 1.0.0
//  */

// public class LP004 extends TradeBuffer {
// 	@SuppressWarnings("unused")

// 	@Autowired
// 	public LP004Report lp004report;

// 	@Override
// 	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
// 		this.info("active LP004 ");
// 		this.totaVo.init(titaVo);

// 		lp004report.exec(titaVo);
// 		this.addList(this.totaVo);
// 		return this.sendList();
// 	}
// }
package com.st1.itx.trade.LP;

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
 * 
 * 
 * @author Eric Chang
 * @version 1.0.0
 */
@Service("LP004")
@Scope("step")
public class LP004 extends BatchBase implements Tasklet, InitializingBean {

	@Autowired
	LP004Report lP004Report;

	@Override
	public void afterPropertiesSet() throws Exception {
	}

	@Override
	public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
		return this.exec(contribution, "M");
	}

	@Override
	public void run() throws LogicException {
		this.info("active LP004 ");
		lP004Report.setTxBuffer(this.getTxBuffer());
		lP004Report.exec(titaVo);
	}

}
