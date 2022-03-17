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
import com.st1.itx.util.date.DateUtil;
import com.st1.itx.util.http.WebClient;

@Service("LB080")
@Scope("step")
/**
 * 
 * 
 * @author Eric Chang
 * @version 1.0.0
 */
public class LB080 extends BatchBase implements Tasklet, InitializingBean {

	@Autowired
	LB080Report lb080Report;

	@Autowired
	DateUtil dDateUtil; 

	@Autowired
	WebClient webClient;

	@Override
	public void afterPropertiesSet() throws Exception {
		;
	}

	@Override
	public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
		this.info("LB080 active RepeatStatus execute ");
		return this.exec(contribution, "M");
	}

	@Override
	public void run() throws LogicException {
		this.info("LB080 active LB080 ");
		this.info("LB080 titaVo.getEntDyI() =" + this.titaVo.getEntDyI());

		//String tranCode = "LB080";
		//String tranName = "授信額度資料檔";

		// this.titaVo.setDataBaseOnMon(); // 月報資料庫
		boolean isFinish = lb080Report.exec(titaVo); // 使用月報資料庫

		//webClient.sendPost(dDateUtil.getNowStringBc(), "1800", titaVo.getTlrNo(), "Y", "LC009", titaVo.getTlrNo(),
		//		tranCode + tranName + (isFinish ? "已完成" : "查無資料"), titaVo);
		
	}

}

//@Service("LB080")
//@Scope("prototype")
///**
// * 
// * 
// * @author Eric Chang
// * @version 1.0.0
// */
//public class LB080 extends TradeBuffer {
//	@SuppressWarnings("unused")
//
//	@Autowired
//	public LB080Report lb080Report;
//
//	@Override
//	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
//		this.info("active LB080 ");
//		this.totaVo.init(titaVo);
//
//		lb080Report.exec(titaVo);
//
//		this.addList(this.totaVo);
//		return this.sendList();
//	}
//
//}