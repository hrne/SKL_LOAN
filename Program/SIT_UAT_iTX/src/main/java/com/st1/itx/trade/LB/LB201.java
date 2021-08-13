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

@Service("LB201")
@Scope("step")
/**
 * 
 * 
 * @author Eric Chang
 * @version 1.0.0
 */
public class LB201 extends BatchBase implements Tasklet, InitializingBean {

	@Autowired
	LB201Report lB201Report;

	@Override
	public void afterPropertiesSet() throws Exception {
		;
	}

	@Override
	public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
		// logger = LoggerFactory.getLogger(LB201.class);
		this.info("LB201 active RepeatStatus execute ");
		return this.exec(contribution, "M");
	}

	@Override
	public void run() throws LogicException {
		this.info("LB201 active LB201 ");
		this.info("LB201 titaVo.getEntDyI() =" + this.titaVo.getEntDyI());

		this.titaVo.setDataBaseOnMon(); // 月報資料庫
		lB201Report.exec(titaVo); // 使用月報資料庫
	}

}

//@Service("LB201")
//@Scope("prototype")
///**
// * 
// * 
// * @author Eric Chang
// * @version 1.0.0
// */
//public class LB201 extends TradeBuffer {
//	@SuppressWarnings("unused")
//	private static final Logger logger = LoggerFactory.getLogger(LB201.class);
//
//	@Autowired
//	public LB201Report lb201Report;
//
//	@Override
//	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
//		this.info("active LB201 ");
//		this.totaVo.init(titaVo);
//
//		lb201Report.exec(titaVo);
//
//		this.addList(this.totaVo);
//		return this.sendList();
//	}
//
//}