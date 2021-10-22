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
import com.st1.itx.db.domain.CdWorkMonth;
import com.st1.itx.db.service.CdWorkMonthService;
import com.st1.itx.tradeService.BatchBase;
import com.st1.itx.util.date.DateUtil;
import com.st1.itx.util.http.WebClient;

/**
 * LD006Batch
 * 
 * @author Chih Wei
 * @version 1.0.0
 */
@Service("LD006Batch")
@Scope("step")
public class LD006Batch extends BatchBase implements Tasklet, InitializingBean {

	@Autowired
	LD006Report lD006Report;

	@Autowired
	CdWorkMonthService sCdWorkMonthService;

	@Autowired
	DateUtil dDateUtil;

	@Autowired
	WebClient webClient;

	String tranCode = "LD006";
	String tranName = "三階放款明細統計";

	@Override
	public void afterPropertiesSet() throws Exception {
		;
	}

	@Override
	public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
		lD006Report.setParentTranCode(this.getParent());
		return this.exec(contribution, "D");
	}

	@Override
	public void run() throws LogicException {
		this.info("active LD006Batch ");

		int tbsdyf = this.txBuffer.getTxCom().getTbsdyf();

		CdWorkMonth cdWorkMonth = sCdWorkMonthService.findDateFirst(tbsdyf, tbsdyf, titaVo);

		int year = cdWorkMonth.getYear();
		int month = cdWorkMonth.getMonth();

		titaVo.putParam("inputYearStart", year);
		titaVo.putParam("inputYearEnd", year);
		titaVo.putParam("inputMonthStart", month);
		titaVo.putParam("inputMonthEnd", month);

		lD006Report.exec(titaVo);
	}
}