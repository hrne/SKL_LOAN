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
import com.st1.itx.db.service.MonthlyLM052AssetClassService;
import com.st1.itx.db.service.MonthlyLM052LoanAssetService;
import com.st1.itx.db.service.MonthlyLM052OvduService;
import com.st1.itx.tradeService.BatchBase;

@Service("LM052")
@Scope("step")
/**
 * 
 * 
 * @author Eric Chang
 * @version 1.0.0
 */
public class LM052 extends BatchBase implements Tasklet, InitializingBean {

	@Autowired
	MonthlyLM052AssetClassService sLM052AssetClass;

	@Autowired
	MonthlyLM052LoanAssetService sLM052LoanAsset;

	@Autowired
	MonthlyLM052OvduService sLM052Ovdu;

	@Autowired
	public LM052Report lM052report;

	String empNo = "";

	int yearMonth = 0;

	@Override
	public void afterPropertiesSet() throws Exception {
	}

	@Override
	public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
		return this.exec(contribution, "M");
	}

	@Override
	public void run() throws LogicException {
		
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

		this.info("exec updLM052SP");
		updLM052ReportSP(thisYM);

		this.info("active LM052 ");
		
		lM052report.exec(titaVo,thisYM);
	}

	private void updLM052ReportSP(int yearMonth) {

		empNo = titaVo.getTlrNo();
		
		this.info("yearMonth=" + yearMonth);
		sLM052AssetClass.Usp_L9_MonthlyLM052AssetClass_Ins(yearMonth, empNo, titaVo);
		sLM052LoanAsset.Usp_L9_MonthlyLM052LoanAsset_Ins(yearMonth, empNo, titaVo);
		sLM052Ovdu.Usp_L9_MonthlyLM052Ovdu_Ins(yearMonth, empNo, titaVo);

		this.info("upd LM052 SP finished.");
	}

}
