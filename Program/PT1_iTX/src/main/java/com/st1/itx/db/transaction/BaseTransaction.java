package com.st1.itx.db.transaction;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.stereotype.Service;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import com.st1.itx.util.log.SysLogger;

@Service("baseTransaction")
@Scope("prototype")
public class BaseTransaction extends SysLogger {
	@Autowired
	private JpaTransactionManager transactionManager;

	private DefaultTransactionDefinition def;

	private TransactionStatus status;

	private boolean txFg = false;

	@PostConstruct
	public void init() {
		this.info("baseService init() getTransaction...");
		this.def = new DefaultTransactionDefinition();
		this.def.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRED);
		this.def.setTimeout(85);
//		this.def.setPropagationBehavior(TransactionDefinition.PROPAGATION_NESTED);
		this.def.setReadOnly(true);
		this.def.setIsolationLevel(TransactionDefinition.ISOLATION_READ_COMMITTED);
		this.status = transactionManager.getTransaction(def);
	}

	public void newInit() {
		if (!this.txFg)
			this.commitEnd();
		this.info("baseService init() getTransaction New...");
		this.def = new DefaultTransactionDefinition();
		this.def.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRES_NEW);
		this.def.setTimeout(85);
//		this.def.setPropagationBehavior(TransactionDefinition.PROPAGATION_NESTED);
		this.def.setReadOnly(true);
		this.def.setIsolationLevel(TransactionDefinition.ISOLATION_READ_COMMITTED);
		this.status = transactionManager.getTransaction(def);
		this.txFg = true;
	}

	public void rollBack() {
		this.info("rollback...");
		transactionManager.rollback(status);
		if (!this.txFg)
			this.init();
		else
			this.newInit();
//		this.status = transactionManager.getTransaction(def);
	}

	public void rollBackEnd() {
		this.info("rollback...End [" + status.isCompleted() + "]");
		if (!status.isCompleted())
			transactionManager.rollback(status);
//		this.init();
//		this.status = transactionManager.getTransaction(def);
	}

	public void commit() {
		this.info("commit...");
		transactionManager.commit(status);
		if (!this.txFg)
			this.init();
		else
			this.newInit();
//		this.status = transactionManager.getTransaction(def);
	}

	public void commitEnd() {
		this.info("commit...End [" + status.isCompleted() + "]");
		if (!status.isCompleted())
			transactionManager.commit(status);
//		this.status = transactionManager.getTransaction(def);
	}
	
	public boolean isTxFg() {
		return txFg;
	}

	public void setTxFg(boolean txFg) {
		this.txFg = txFg;
	}
}