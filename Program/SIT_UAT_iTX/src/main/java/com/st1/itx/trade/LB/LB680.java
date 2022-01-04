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

@Service("LB680")
@Scope("step")
/**
 * 
 * 
 * @author Eric Chang
 * @version 1.0.0
 */
public class LB680 extends BatchBase implements Tasklet, InitializingBean {

	@Autowired
	public LB680Report lb680Report;

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
		this.info("LB680 active RepeatStatus execute ");
		return this.exec(contribution, "M");
	}

	@Override
	public void run() throws LogicException {
		this.info("LB680 active LB680 ");
		this.info("LB680 titaVo.getEntDyI() =" + this.titaVo.getEntDyI());

		// String tranCode = "LB680";
		// String tranName = "「貸款餘額(擔保放款餘額加上部分擔保、副擔保貸款餘額)扣除擔保品鑑估值」之金額資料檔";

		this.titaVo.setDataBaseOnMon(); // 月報資料庫
		boolean isFinish = lb680Report.exec(titaVo); // 使用月報資料庫

		// webClient.sendPost(dDateUtil.getNowStringBc(), "1800", titaVo.getTlrNo(),
		// "Y", "LC009", titaVo.getTlrNo(),
		// tranCode + tranName + (isFinish ? "已完成" : "查無資料"), titaVo);

	}

}

//@Service("LB680")
//@Scope("prototype")
///**
// * 
// * 
// * @author Eric Chang
// * @version 1.0.0
// */
//public class LB680 extends TradeBuffer {
//	@SuppressWarnings("unused")
//
//	@Autowired
//	public LB680Report lb680Report;
//
//	@Override
//	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
//		this.info("active LB680 ");
//		this.totaVo.init(titaVo);
//
//		lb680Report.exec(titaVo);
//
//		this.addList(this.totaVo);
//		return this.sendList();
//	}
//
//}