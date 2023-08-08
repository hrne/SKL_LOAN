package com.st1.itx.trade.L9;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;

import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.db.domain.LoanCheque;
import com.st1.itx.db.service.LoanChequeService;
import com.st1.itx.tradeService.BatchBase;
import com.st1.itx.util.date.DateUtil;
import com.st1.itx.util.http.WebClient;

/**
 * L9743Batch
 * 
 * @author Chih Wei
 * @version 1.0.0
 */
@Service("L9743Batch")
@Scope("step")
public class L9743Batch extends BatchBase implements Tasklet, InitializingBean {

	@Autowired
	L9743Report l9743Report;

	@Autowired
	DateUtil dDateUtil;

	@Autowired
	WebClient webClient;

	@Autowired
	LoanChequeService sLoanChequeService;

	String tranCode = "L9743";
	String tranName = "企金戶還本收據及繳息收據";

	@Override
	public void afterPropertiesSet() throws Exception {
		;
	}

	@Override
	public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
		l9743Report.setParentTranCode(this.getParent());
		return this.exec(contribution, "D", chunkContext);
	}

	@Override
	public void run() throws LogicException {
		this.info("active L9743Batch ");

		int tbsdyf = this.txBuffer.getTxCom().getTbsdyf();

		List<String> statusCode = new ArrayList<>();

		// TODO: 與User確認 要進表的StatusCode
		statusCode.add("0"); // 未處理
		statusCode.add("1"); // 兌現入帳
		statusCode.add("2"); // 退票
		statusCode.add("3"); // 抽票
		statusCode.add("4"); // 兌現未入帳
		statusCode.add("5"); // 即期票

		Slice<LoanCheque> sLoanCheque = sLoanChequeService.receiveDateRange(tbsdyf, tbsdyf, statusCode, 0,
				Integer.MAX_VALUE, titaVo);

		List<LoanCheque> listLoanCheque = sLoanCheque == null ? new ArrayList<>()
				: new ArrayList<>(sLoanCheque.getContent());

		this.info("listLoanCheque.size="+listLoanCheque.size());
		
		if(listLoanCheque.size() == 0) {
			
			titaVo.putParam("inputCustNo", 0);
			titaVo.putParam("inputDate", tbsdyf);

			l9743Report.exec(titaVo);
		}
		
		Map<Integer, Integer> custNoMap = new HashMap<>();

		for (LoanCheque loanCheque : listLoanCheque) {

			int custNo = loanCheque.getCustNo();

			if (!custNoMap.containsKey(custNo)) {
				TitaVo tmpTitaVo = (TitaVo) this.titaVo.clone();
				
				this.info("inputCustNo="+custNo);
				this.info("tbsdyf="+tbsdyf);
				
				tmpTitaVo.putParam("inputCustNo", custNo);
				tmpTitaVo.putParam("inputDate", tbsdyf);

				l9743Report.exec(titaVo);

				custNoMap.put(custNo, 0);
			}
		}
		
		
		
	}
}