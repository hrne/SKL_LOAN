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

@Service("batchTransaction")
@Scope("prototype")
public class BatchTransaction extends SysLogger {
	@Autowired
	private JpaTransactionManager transactionManager;

	private DefaultTransactionDefinition def;

	private TransactionStatus status;

	@PostConstruct
	public void init() {
		this.info("baseService init() getTransaction...");
		this.def = new DefaultTransactionDefinition();
		this.def.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRES_NEW);
		this.def.setTimeout(1800);
//		this.def.setPropagationBehavior(TransactionDefinition.PROPAGATION_NESTED);
		this.def.setReadOnly(true);
		this.def.setIsolationLevel(TransactionDefinition.ISOLATION_READ_COMMITTED);
		this.status = transactionManager.getTransaction(def);
	}

	public void rollBack() {
		this.info("rollback...");
		transactionManager.rollback(status);
		this.init();
//		this.status = transactionManager.getTransaction(def);
	}

	public void rollBackEnd() {
		this.info("rollback...End");
		transactionManager.rollback(status);
//		this.init();
//		this.status = transactionManager.getTransaction(def);
	}

	public void commit() {
		this.info("commit...");
		transactionManager.commit(status);
		this.init();
//		this.status = transactionManager.getTransaction(def);
	}

	public void commitEnd() {
		this.info("commit...End");
		transactionManager.commit(status);
//		this.status = transactionManager.getTransaction(def);
	}
}
