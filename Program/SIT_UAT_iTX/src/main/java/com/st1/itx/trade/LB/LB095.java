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

@Service("LB095")
@Scope("step")
/**
 * 
 * 
 * @author Eric Chang
 * @version 1.0.0
 */
public class LB095 extends BatchBase implements Tasklet, InitializingBean {

	@Autowired
	public LB095Report lb095Report;

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
		this.info("LB095 active RepeatStatus execute ");
		return this.exec(contribution, "M");
	}

	@Override
	public void run() throws LogicException {
		this.info("LB095 active LB095 ");
		this.info("LB095 titaVo.getEntDyI() =" + this.titaVo.getEntDyI());

		// String tranCode = "LB095";
		// String tranName = "動產擔保品明細－建號附加檔";

		this.titaVo.setDataBaseOnMon(); // 月報資料庫
		boolean isFinish = lb095Report.exec(titaVo); // 使用月報資料庫

		// webClient.sendPost(dDateUtil.getNowStringBc(), "1800", titaVo.getTlrNo(),
		// "Y", "LC009", titaVo.getTlrNo(),
		// tranCode + tranName + (isFinish ? "已完成" : "查無資料"), titaVo);

	}

}

//@Service("LB095")
//@Scope("prototype")
///**
// * 
// * 
// * @author Eric Chang
// * @version 1.0.0
// */
//public class LB095 extends TradeBuffer {
//	@SuppressWarnings("unused")
//
//	@Autowired
//	public LB095Report lb095Report;
//
//	@Override
//	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
//		this.info("active LB095 ");
//		this.totaVo.init(titaVo);
//
//		lb095Report.exec(titaVo);
//
//		this.addList(this.totaVo);
//		return this.sendList();
//	}
//
//}