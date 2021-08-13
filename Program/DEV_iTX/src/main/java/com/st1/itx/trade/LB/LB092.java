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

@Service("LB092")
@Scope("step")
/**
 * 
 * 
 * @author Eric Chang
 * @version 1.0.0
 */
public class LB092 extends BatchBase implements Tasklet, InitializingBean {

	@Autowired
	public LB092Report lb092Report;

	@Override
	public void afterPropertiesSet() throws Exception {
		;
	}

	@Override
	public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
		// logger = LoggerFactory.getLogger(LB092.class);
		this.info("LB092 active RepeatStatus execute ");
		return this.exec(contribution, "M");
	}

	@Override
	public void run() throws LogicException {
		this.info("LB092 active LB092 ");
		this.info("LB092 titaVo.getEntDyI() =" + this.titaVo.getEntDyI());

		this.titaVo.setDataBaseOnMon(); // 月報資料庫
		lb092Report.exec(titaVo); // 使用月報資料庫
	}

}

//@Service("LB092")
//@Scope("prototype")
///**
// * 
// * 
// * @author Eric Chang
// * @version 1.0.0
// */
//public class LB092 extends TradeBuffer {
//	@SuppressWarnings("unused")
//	// private static final Logger logger = LoggerFactory.getLogger(LB092.class);
//
//	@Autowired
//	public LB092Report lb092Report;
//
//	@Override
//	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
//		this.info("active LB092 ");
//		this.totaVo.init(titaVo);
//
//		lb092Report.exec(titaVo);
//
//		this.addList(this.totaVo);
//		return this.sendList();
//	}
//
//}