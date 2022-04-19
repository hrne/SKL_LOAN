package com.st1.itx.main;

import java.io.PrintWriter;
import java.io.StringWriter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.st1.itx.Exception.LogicException;
import com.st1.itx.db.transaction.BatchTransaction;
import com.st1.itx.eum.ContentName;
import com.st1.itx.eum.ThreadVariable;
import com.st1.itx.tradeService.CommBuffer;
import com.st1.itx.util.MySpring;
import com.st1.itx.util.date.DateUtil;
import com.st1.itx.util.http.WebClient;

@Component("taskBatch")
@Scope("prototype")
public class TaskBatch extends CommBuffer implements Runnable {
	@Autowired
	private WebClient webClient;

	@Autowired
	public DateUtil dateUtil;

	@Override
	public void exec() throws LogicException {
		this.run();
	}

	@Override
	public void run() {
		boolean isOK = true;
		this.setLog();
		this.info("TaskBatch exec...");

		BatchTransaction batchTransaction = (BatchTransaction) MySpring.getBean("batchTransaction");

		try {
			MySpring.getBean(this.getBeanName(), this.txBuffer, this.titaVo, batchTransaction);
			if (!"apControl".equals(this.getBeanName()))
				batchTransaction.commitEnd();
		} catch (Throwable e) {
			StringWriter errors = new StringWriter();
			e.printStackTrace(new PrintWriter(errors));
			this.error(errors.toString());
			if (!"apControl".equals(this.getBeanName()))
				batchTransaction.rollBackEnd();
			isOK = !isOK;
		} finally {
			if (!isOK)
				webClient.sendPost(dateUtil.getNowStringBc(), "2300", titaVo.getTlrNo(), "Y", this.getBeanName(), titaVo.getTlrNo(), this.getBeanName() + "執行失敗", titaVo);
		}

		batchTransaction = null;
		ThreadVariable.clearThreadLocal();
	}

	private void setLog() {
		ThreadVariable.setObject(ContentName.empnot, this.titaVo.getEmpNot());
		if (this.getLoggerFg())
			ThreadVariable.setObject(ContentName.loggerFg, true);
		else
			ThreadVariable.setObject(ContentName.loggerFg, false);
	}
}
