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
//import com.st1.itx.db.service.springjpa.cm.LD009ServiceImpl;
//import com.st1.itx.tradeService.TradeBuffer;
//
//@Service("LD009")
//@Scope("prototype")
///**
// * 
// * 
// * @author Eric Chang
// * @version 1.0.0
// */
//public class LD009 extends TradeBuffer {
//	@SuppressWarnings("unused")
//
//	@Autowired
//	public LD009ServiceImpl LD009ServiceImpl;
//
//	@Autowired
//	public LD009Report lD009Report;
//
//	@Override
//	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
//		this.info("active LD009 ");
//		this.totaVo.init(titaVo);
//
//		lD009Report.exec(titaVo);
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
 * LD009
 * 
 * @author Eric Chang
 * @version 1.0.0
 */
@Service("LD009")
@Scope("step")
public class LD009 extends BatchBase implements Tasklet, InitializingBean {

	@Autowired
	LD009Report ld009report;

	@Override
	public void afterPropertiesSet() throws Exception {
		;
	}

	@Override
	public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
		ld009report.setParentTranCode(this.getParent());
		return this.exec(contribution, "D");
	}

	@Override
	public void run() throws LogicException {
		this.info("active LD009 ");
		ld009report.exec(titaVo);
	}

}