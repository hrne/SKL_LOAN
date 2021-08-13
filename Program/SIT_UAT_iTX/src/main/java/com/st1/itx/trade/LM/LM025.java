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


//@Service("LM025")
//@Scope("prototype")
///**
// * 
// * 
// * @author Eric Chang
// * @version 1.0.0
// */
//public class LM025 extends TradeBuffer {
//	@SuppressWarnings("unused")
//	private static final Logger logger = LoggerFactory.getLogger(LM025.class);
//
//	@Autowired
//	public LM025Report lm025report;
//
//	@Override
//	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
//		this.info("active LM025 ");
//		this.totaVo.init(titaVo);
//		//LM025-減損系統有效利率資料查核
//		//窗口 佩芸 7085
//		//每季出份表-3,6,9,12月
//		//只需要出當月份的明細資料(Excel資料)-固定 浮動
//		
//		lm025report.exec(titaVo);
//		this.addList(this.totaVo);
//		return this.sendList();
//	}
//}

@Service("LM025")
@Scope("step")
/**
 * 
 * 
 * @author Jacky
 * @version 1.0.0
 */
public class LM025 extends BatchBase implements Tasklet, InitializingBean {

	@Autowired
	public LM025Report lM025Report;

	@Override
	public void afterPropertiesSet() throws Exception {
		;
	}

	@Override
	public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
		// logger = LoggerFactory.getLogger(LM025.class);
		return this.exec(contribution, "M");
	}

	@Override
	public void run() throws LogicException {
		this.info("active LM025 ");
		lM025Report.exec(titaVo);
	}

}