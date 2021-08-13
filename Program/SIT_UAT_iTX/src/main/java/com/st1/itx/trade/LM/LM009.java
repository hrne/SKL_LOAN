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
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.trade.L9.L9131Report;
import com.st1.itx.tradeService.BatchBase;

//@Service("LM009")
//@Scope("prototype")
///**
// * 
// * 
// * @author Eric Chang
// * @version 1.0.0
// */
//public class LM009 extends TradeBuffer {
//	@SuppressWarnings("unused")
//	private static final Logger logger = LoggerFactory.getLogger(LM009.class);
//
//	@Autowired
//	public LM009Report lm009report;
//
//	@Override
//	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
//		this.info("active LM009 ");
//		this.totaVo.init(titaVo);
//
//		lm009report.exec(titaVo);
//
//		this.addList(this.totaVo);
//		return this.sendList();
//	}
//}

@Service("LM009")
@Scope("step")
/**
 * 
 * 
 * @author Eric Chang
 * @version 1.0.0
 */
public class LM009 extends BatchBase implements Tasklet, InitializingBean {

	@Autowired
	LM009Report lM009report;

	@Autowired
	L9131Report l9131Report;
	
	@Override
	public void afterPropertiesSet() throws Exception {
		;
	}

	@Override
	public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
		// logger = LoggerFactory.getLogger(LM009.class);
		return this.exec(contribution, "M");
	}

	@Override
	public void run() throws LogicException {
		this.info("active LM009 ");
		lM009report.setParentTranCode(this.getParent());
		lM009report.exec(titaVo);
		String acDate  = titaVo.getEntDy();
		
		
		// 此處putParam給L9131用
		
		titaVo.putParam("AcDate", acDate);
		titaVo.putParam("BatchNo", "01");
		titaVo.putParam("MediaSeq", "001");
		
		doRpt(titaVo);
	}
	public void doRpt(TitaVo titaVo) throws LogicException {
		this.info("L9131 doRpt started.");

		// 撈資料組報表
		l9131Report.exec(titaVo);

		// 寫產檔記錄到TxReport
		long rptNo = l9131Report.close();

		// 產生PDF檔案
		l9131Report.toPdf(rptNo);

		this.info("L9131 doRpt finished.");

	}
}