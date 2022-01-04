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
import com.st1.itx.util.date.DateUtil;
import com.st1.itx.util.http.WebClient;

@Service("LNM39GP")
@Scope("step")
/**
 * 
 * 
 * @author Eric Chang
 * @version 1.0.0
 */
public class LNM39GP extends BatchBase implements Tasklet, InitializingBean {

	@Autowired
	public LNM39GPReport lnm39gpReport;

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
		this.info("LNM39GP active RepeatStatus execute ");
		return this.exec(contribution, "M");
	}

	@Override
	public void run() throws LogicException {
		this.info("LNM39GP active LNM39GP ");
		this.info("LNM39GP titaVo.getEntDyI() =" + this.titaVo.getEntDyI());

		// String tranCode = "LNM39GP";
		// String tranName = "LNM39GP 欄位清單７";

		this.titaVo.setDataBaseOnMon(); // 月報資料庫
		boolean isFinish = lnm39gpReport.exec(titaVo); // 使用月報資料庫

		// webClient.sendPost(dDateUtil.getNowStringBc(), "1800", titaVo.getTlrNo(),
		// "Y", "LC009", titaVo.getTlrNo(),
		// tranCode + tranName + (isFinish ? "已完成" : "查無資料"), titaVo);

	}

}

//@Service("LNM39GP")
//@Scope("prototype")
///**
// * 
// * 
// * @author Eric Chang
// * @version 1.0.0
// */
//public class LNM39GP extends TradeBuffer {
//	@SuppressWarnings("unused")
//
//	@Autowired
//	public LNM39GPReport lnm39gpReport;
//
//	@Override
//	public ArrayList<TotaVo> run(TitaVo titaVo) throws LogicException {
//		this.info("active LNM39GP ");
//		this.totaVo.init(titaVo);
//
//		lnm39gpReport.exec(titaVo);
//
//		this.addList(this.totaVo);
//		return this.sendList();
//	}
//
//}