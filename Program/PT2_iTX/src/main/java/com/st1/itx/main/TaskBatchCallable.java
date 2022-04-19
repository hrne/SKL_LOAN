package com.st1.itx.main;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.concurrent.Callable;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.st1.itx.Exception.LogicException;
import com.st1.itx.dataVO.TotaVoList;
import com.st1.itx.db.transaction.BaseTransaction;
import com.st1.itx.eum.ContentName;
import com.st1.itx.eum.ThreadVariable;
import com.st1.itx.tradeService.CommBuffer;
import com.st1.itx.util.MySpring;

@Component("taskBatchCallable")
@Scope("prototype")
public class TaskBatchCallable extends CommBuffer implements Callable<TotaVoList> {
	@Autowired
	public BaseTransaction baseTransaction;

	@Override
	public void exec() throws LogicException {
		if (this.getLoggerFg())
			ThreadVariable.setObject(ContentName.loggerFg, true);
		else
			ThreadVariable.setObject(ContentName.loggerFg, false);

		this.info("TaskBatch exec...");
		try {
			this.call();
		} catch (Exception e) {
			StringWriter errors = new StringWriter();
			e.printStackTrace(new PrintWriter(errors));
			this.error(errors.toString());
		}
	}

	@Override
	public TotaVoList call() throws Exception {
		try {
			return MySpring.getBean(this.getBeanName(), this.txBuffer, this.titaVo);
		} catch (Throwable e) {
			StringWriter errors = new StringWriter();
			e.printStackTrace(new PrintWriter(errors));
			this.error(errors.toString());
			return new TotaVoList();
		}
	}
}
