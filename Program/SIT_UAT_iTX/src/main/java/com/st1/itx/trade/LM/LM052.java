package com.st1.itx.trade.LM;

import java.text.SimpleDateFormat;
import java.util.Calendar;

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

		this.info("exec updLM052SP");
		updLM052ReportSP();

		this.info("active LM052 ");
		lM052report.exec(titaVo);
	}

	public void updLM052ReportSP() {

		int iEntdy = Integer.valueOf(titaVo.get("ENTDY")) + 19110000;
		int iYear = (Integer.valueOf(titaVo.get("ENTDY")) + 19110000) / 10000;
		int iMonth = ((Integer.valueOf(titaVo.get("ENTDY")) + 19110000) / 100) % 100;

		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
		// 當日
		int nowDate = Integer.valueOf(iEntdy);
		Calendar calMonthDate = Calendar.getInstance();
		// 設當年月底日 0是月底
		calMonthDate.set(iYear, iMonth, 0);

		int thisMonthEndDate = Integer.valueOf(dateFormat.format(calMonthDate.getTime()));

		boolean isMonthZero = iMonth - 1 == 0;

		if (nowDate < thisMonthEndDate) {
			iYear = isMonthZero ? (iYear - 1) : iYear;
			iMonth = isMonthZero ? 12 : iMonth - 1;
		}

		empNo = titaVo.getTlrNo();
		yearMonth = (iYear * 100) + iMonth;

		sLM052AssetClass.Usp_L9_MonthlyLM052AssetClass_Ins(yearMonth, empNo, titaVo);
		sLM052LoanAsset.Usp_L9_MonthlyLM052LoanAsset_Ins(yearMonth, empNo, titaVo);
		sLM052Ovdu.Usp_L9_MonthlyLM052Ovdu_Ins(yearMonth, empNo, titaVo);
		
		this.info("upd LM052 SP finished.");
	}

}
