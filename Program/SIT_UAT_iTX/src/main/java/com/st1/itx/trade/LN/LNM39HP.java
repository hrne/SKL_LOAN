package com.st1.itx.trade.LN;

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

@Service("LNM39HP")
@Scope("step")
/**
 * 
 * 
 * @author Eric Chang
 * @version 1.0.0
 */
public class LNM39HP extends BatchBase implements Tasklet, InitializingBean {

	@Autowired
	public LNM39HPReport lnm39hpReport;

	@Override
	public void afterPropertiesSet() throws Exception {
		;
	}

	@Override
	public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
		// logger = LoggerFactory.getLogger(LNM39HP.class);
		this.info("LNM39HP active RepeatStatus execute ");
		return this.exec(contribution, "M");
	}

	@Override
	public void run() throws LogicException {
		this.info("LNM39HP active LNM39HP ");
		this.info("LNM39HP titaVo.getEntDyI() =" + this.titaVo.getEntDyI());

		this.titaVo.setDataBaseOnMon(); // 月報資料庫
		lnm39hpReport.exec(titaVo); // 使用月報資料庫
	}

}

//@Service("LNM39HP")
//@Scope("prototype")
///**
// * 
// * 
// * @author Eric Chang
// * @version 1.0.0
// */
//public class LNM39HP extends TradeBuffer {
//	@SuppressWarnings("unused")
//	private static final Logger logger = LoggerFactory.getLogger(LNM39HP.class);
//
//	@Autowired
//	public LNM39HPReport lnm39hpReport;
//
//	@Override
//	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
//		this.info("active LNM39HP ");
//		this.totaVo.init(titaVo);
//
//		lnm39hpReport.exec(titaVo);
//
//		this.addList(this.totaVo);
//		return this.sendList();
//	}
//
//}