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
import com.st1.itx.tradeService.BatchBase;
import com.st1.itx.util.date.DateUtil;
import com.st1.itx.util.http.WebClient;

/**
 * LM036
 * 
 * @author Chih Wei
 * @version 1.0.0
 */
@Service("LM036")
@Scope("step")
public class LM036 extends BatchBase implements Tasklet, InitializingBean {

	@Autowired
	LM036Report lM036Report;

	@Autowired
	WebClient webClient;

	@Autowired
	DateUtil dDateUtil;

	@Override
	public void afterPropertiesSet() throws Exception {
		;
	}

	@Override
	public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
		return this.exec(contribution, "M");
	}

	@Override
	public void run() throws LogicException {
		this.info("active LM036 ");

		// 帳務日(西元)
		int tbsdy = this.txBuffer.getTxCom().getTbsdyf();
		// 月底日(西元)
		int mfbsdy = this.txBuffer.getTxCom().getMfbsdyf();
		// 年
		int iYear = mfbsdy / 10000;
		// 月
		int iMonth = (mfbsdy / 100) % 100;
		// 當年月
		int thisYM = 0;

		// 判斷帳務日與月底日是否同一天
		if (tbsdy < mfbsdy) {
			iYear = iMonth - 1 == 0 ? (iYear - 1) : iYear;
			iMonth = iMonth - 1 == 0 ? 12 : iMonth - 1;
		}

		thisYM = iYear * 100 + iMonth;


		lM036Report.exec(titaVo, thisYM);

//		webClient.sendPost(dDateUtil.getNowStringBc(), dDateUtil.getNowStringTime(), titaVo.getTlrNo(), "Y", "LC009", titaVo.getTlrNo(), "LM036第一類各項統計表", titaVo);
	}

}