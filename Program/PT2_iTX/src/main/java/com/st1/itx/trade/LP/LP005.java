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
// @Service("LP005")
// @Scope("step")
// /**
//  * 
//  * 
//  * @author Eric Chang
//  * @version 1.0.0
//  */
// public class LP005 extends TradeBuffer {
// 	@SuppressWarnings("unused")

// 	@Autowired
// 	public LP005Report lp005report;

// 	@Override
// 	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
// 		this.info("active LP005 ");
// 		this.totaVo.init(titaVo);

// 		lp005report.exec(titaVo);
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
import com.st1.itx.util.date.DateUtil;
import com.st1.itx.util.http.WebClient;

/**
 * 
 * 
 * @author Ted Lin
 * @version 1.0.0
 */
@Service("LP005")
@Scope("step")
public class LP005 extends BatchBase implements Tasklet, InitializingBean {

	@Autowired
	LP005Report lP005Report;

	@Autowired
	WebClient webClient;

	@Autowired
	DateUtil dDateUtil;

	@Override
	public void afterPropertiesSet() throws Exception {
	}

	@Override
	public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
		return this.exec(contribution, "M");
	}

	@Override
	public void run() throws LogicException {
		this.info("active LP005 ");
		lP005Report.setTxBuffer(this.getTxBuffer());
		lP005Report.setParentTranCode(this.getParent());
		lP005Report.exec(titaVo);
		webClient.sendPost(dDateUtil.getNowStringBc(), dDateUtil.getNowStringTime(), titaVo.getTlrNo(), "Y", "LC009", titaVo.getTlrNo(), "LP005協辦考核核算底稿", titaVo);
	}

}
