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
import com.st1.itx.util.date.DateUtil;
import com.st1.itx.util.http.WebClient;

/**
 * LD004Batch
 * 
 * @author Chih Wei
 * @version 1.0.0
 */
@Service("LD004Batch")
@Scope("step")
public class LD004Batch extends BatchBase implements Tasklet, InitializingBean {

	@Autowired
	LD004Report lD004Report;

	@Autowired
	DateUtil dDateUtil;

	@Autowired
	WebClient webClient;

	String tranCode = "LD004";
	String tranName = "企金戶還本收據及繳息收據";

	@Override
	public void afterPropertiesSet() throws Exception {
		;
	}

	@Override
	public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
		lD004Report.setParentTranCode(this.getParent());
		return this.exec(contribution, "D");
	}

	@Override
	public void run() throws LogicException {
		this.info("active LD004Batch ");

		this.info(tranCode + " this.getParent()= " + this.getParent());
		String parentTranCode = this.getParent();

		lD004Report.setParentTranCode(parentTranCode);

		int tbsdyf = this.txBuffer.getTxCom().getTbsdyf();

		titaVo.putParam("inputAcDate", tbsdyf);
		titaVo.putParam("inputSlipNoStart", 0);
		titaVo.putParam("inputSlipNoEnd", 999999);
		titaVo.putParam("inputTitaTxtNoStart", 0);
		titaVo.putParam("inputTitaTxtNoEnd", 99999999);

		// 各產一次 1:還本收據;2:繳息收據
		titaVo.putParam("inputOption", 1);

		lD004Report.exec(titaVo);

		// 各產一次 1:還本收據;2:繳息收據
		titaVo.putParam("inputOption", 2);

		lD004Report.exec(titaVo);
	}
}