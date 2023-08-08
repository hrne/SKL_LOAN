//package com.st1.itx.trade.LD;
//
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
//import com.st1.itx.db.service.springjpa.cm.LD003ServiceImpl;
//import com.st1.itx.tradeService.TradeBuffer;
//
//@Service("LD003")
//@Scope("prototype")
///**
// * 
// * 
// * @author Eric Chang
// * @version 1.0.0
// */
//public class LD003 extends TradeBuffer {
//	@SuppressWarnings("unused")
//
//	@Autowired
//	public LD003ServiceImpl LD003ServiceImpl;
//
//	@Autowired
//	public LD003Report lD003Report;
//
//	@Override
//	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
//		this.info("active LD003 ");
//		this.totaVo.init(titaVo);
//
//		lD003Report.exec(titaVo);
//
//		this.addList(this.totaVo);
//		return this.sendList();
//	}
//
//}

package com.st1.itx.trade.LD;

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
 * LD003
 * 
 * @author Eric Chang
 * @version 1.0.0
 */
@Service("LD003")
@Scope("step")
public class LD003 extends BatchBase implements Tasklet, InitializingBean {

	@Autowired
	LD003Report ld003report;

	@Override
	public void afterPropertiesSet() throws Exception {
		;
	}

	@Override
	public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
		ld003report.setParentTranCode(this.getParent());
		return this.exec(contribution, "D", chunkContext);
	}

	@Override
	public void run() throws LogicException {
		this.info("active LD003 ");
		ld003report.exec(titaVo);
	}

}