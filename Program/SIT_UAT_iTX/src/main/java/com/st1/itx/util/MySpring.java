package com.st1.itx.util;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import com.st1.itx.Exception.DBException;
import com.st1.itx.Exception.LogicException;
import com.st1.itx.buffer.TxBuffer;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.dataVO.TotaVoList;
import com.st1.itx.db.transaction.BatchTransaction;
import com.st1.itx.eum.ContentName;
import com.st1.itx.eum.ThreadVariable;
import com.st1.itx.main.ApControl;
import com.st1.itx.main.BpControl;
import com.st1.itx.main.TaskBatch;
import com.st1.itx.main.TaskBatchCallable;
import com.st1.itx.tradeService.TradeBuffer;
import com.st1.itx.util.log.SysLogger;

public class MySpring extends SysLogger implements ApplicationContextAware {
	private static final Logger logger = LoggerFactory.getLogger(MySpring.class);
	private static ApplicationContext appCtx;

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		appCtx = applicationContext;
		if (ThreadVariable.isLogger())
			logger.info("spring context injected");
	}

	/**
	 * 
	 * @param <T>          t
	 * @param name         bean name
	 * @param requiredType class type
	 * @return trade object
	 * @throws BeansException when bean name dose not exist
	 */
	public static <T> T getBean(String name, Class<T> requiredType) throws BeansException {
		if (ThreadVariable.isLogger())
			logger.info("getBean:" + name);
		return appCtx.getBean(name, requiredType);
	}

	/**
	 * when TXBuffer not change call this
	 * 
	 * @param name bean name
	 * @return trade object
	 * @throws BeansException when bean name dose not exist
	 */
	public static Object getBean(String name) throws BeansException {
		if (ThreadVariable.isLogger())
			logger.info("getBean : " + name);
		return appCtx.getBean(name);
	}

	/**
	 * 
	 * @param name             beanName
	 * @param txBuffer         TxBuffer
	 * @param titaVo           TitaVo
	 * @param batchTransaction BatchTransaction
	 * @return TotaVoList TotaVoList
	 * @throws BeansException when bean name does not exist
	 * @throws LogicException when logic warn
	 * @throws DBException    DBException
	 * @throws Exception      Exception
	 */
	public static TotaVoList getBean(String name, TxBuffer txBuffer, TitaVo titaVo, BatchTransaction... batchTransaction) throws BeansException, LogicException, DBException, Exception {
		if (ThreadVariable.isLogger()) {
			logger.info("getBean:" + name);
			logger.info(titaVo.toString());
		}
		if (!name.equals("apControl")) {
			TradeBuffer trade = (TradeBuffer) appCtx.getBean(name);
//			trade.setLoggerFg(titaVo.getLoggerFg(), "com.st1.itx.trade." + titaVo.getTxCode().substring(0, 2) + "." + titaVo.getTxCode());
			trade.setTxBuffer(txBuffer);

			if (ThreadVariable.isLogger())
				logger.info(batchTransaction.length + "");
			if (batchTransaction.length != 0)
				trade.setBatchTransaction(batchTransaction[0]);

			return (TotaVoList) trade.run(titaVo);
		} else {
			ApControl apControl = (ApControl) appCtx.getBean(name);
			apControl.callTrade(titaVo);
			return apControl.getTotaVoList();
		}
	}

	/**
	 * batch jobLaunch
	 * 
	 * @param jobName String Name
	 * @param params  Job Parametres
	 */
	public static void jobLaunch(String jobName, JobParameters params) {
		JobLauncher jobLauncher = (JobLauncher) appCtx.getBean("jobLauncherAsync");
		Job job = (Job) appCtx.getBean(jobName);

		try {
			jobLauncher.run(job, params);
		} catch (JobExecutionAlreadyRunningException | JobRestartException | JobInstanceAlreadyCompleteException | JobParametersInvalidException e) {
			StringWriter errors = new StringWriter();
			e.printStackTrace(new PrintWriter(errors));
			logger.error(errors.toString());
		}

	}

	/**
	 * call batch by bpContorl <br>
	 * In Method TitaVo will be Clone,So OrgTitaVo will not change
	 * 
	 * @param txBuffer TxBuffer
	 * @param titaVo   TitaVo
	 * @throws LogicException When TxBuffer Or TitaVo Is Null
	 */
	public static void batchAp(TxBuffer txBuffer, TitaVo titaVo) throws LogicException {
		if (txBuffer == null || titaVo == null)
			throw new LogicException("CE000", "TxBuffer or TitaVo Is Null");

		TitaVo titaVoTemp = (TitaVo) titaVo.clone();
		if (ThreadVariable.isLogger())
			logger.info("Batch Ap Call : " + titaVo.getTxCode());
		ThreadPoolTaskExecutor taskExecutor = (ThreadPoolTaskExecutor) MySpring.getBean("taskExecutor");
		BpControl bpControl = (BpControl) MySpring.getBean("bpControl");
		bpControl.setTxBuffer(txBuffer);
		bpControl.setTitaVo(titaVoTemp);
		if (ThreadVariable.isLogger())
			bpControl.setLoggerFg("1");

		taskExecutor.execute(bpControl);
	}

	/**
	 * Call Ap In newTask<br>
	 * In Method TitaVo will be Clone,So OrgTitaVo will not change
	 * 
	 * @param beanName TxCode
	 * @param txBuffer TxBuffer
	 * @param titaVo   TitaVo
	 * @throws LogicException When beanName Or txBuffer Or titaVo Is Empty
	 */
	public static void newTask(String beanName, TxBuffer txBuffer, TitaVo titaVo) throws LogicException {
		if (txBuffer == null || titaVo == null || beanName == null || beanName.trim().isEmpty())
			throw new LogicException("CE000", "Parameter Not Complete..");

		TitaVo titaVoTemp = (TitaVo) titaVo.clone();
//		titaVoTemp.putParam(ContentName.txCode, beanName);
		if (ThreadVariable.isLogger())
			logger.info("newTask Call : " + beanName);
		ThreadPoolTaskExecutor taskExecutor = (ThreadPoolTaskExecutor) MySpring.getBean("taskExecutor");

		TaskBatch taskBatch = (TaskBatch) MySpring.getBean("taskBatch");
		taskBatch.setTxBuffer(txBuffer);
		taskBatch.setTitaVo(titaVoTemp);
		taskBatch.setBeanName(beanName);
		if (ThreadVariable.isLogger())
			taskBatch.setLoggerFg("1");
		taskExecutor.execute(taskBatch);
	}

	/**
	 * Call Ap In newTask<br>
	 * In Method TitaVo will be Clone,So OrgTitaVo will not change
	 * 
	 * @param beanName    TxCode
	 * @param txBuffer    TxBuffer
	 * @param titaVo      TitaVo
	 * @param callByApCtl true(call by apCtl) false(call ap)
	 * @throws LogicException When beanName Or txBuffer Or titaVo Is Empty
	 */
	public static void newTask(String beanName, TxBuffer txBuffer, TitaVo titaVo, Boolean callByApCtl) throws LogicException {
		if (txBuffer == null || titaVo == null || beanName == null || beanName.trim().isEmpty())
			throw new LogicException("CE000", "Parameter Not Complete..");

		TitaVo titaVoTemp = (TitaVo) titaVo.clone();
		if (callByApCtl)
			titaVoTemp.putParam(ContentName.txCode, beanName);
		if (ThreadVariable.isLogger())
			logger.info("newTask Call : " + titaVoTemp.getTxCode());

		ThreadPoolTaskExecutor taskExecutor = (ThreadPoolTaskExecutor) MySpring.getBean("taskExecutor");
		TaskBatch taskBatch = (TaskBatch) MySpring.getBean("taskBatch");
		taskBatch.setTxBuffer(txBuffer);
		taskBatch.setTitaVo(titaVoTemp);
		if (callByApCtl)
			taskBatch.setBeanName("apControl");
		else
			taskBatch.setBeanName(beanName);

		if (ThreadVariable.isLogger())
			taskBatch.setLoggerFg("1");

		taskExecutor.execute(taskBatch);
	}

	public static TotaVoList newTaskFuture(String beanName, TxBuffer txBuffer, TitaVo titaVo) throws LogicException {
		if (txBuffer == null || titaVo == null || beanName == null || beanName.trim().isEmpty())
			throw new LogicException("CE000", "Parameter Not Complete..");

		TitaVo titaVoTemp = (TitaVo) titaVo.clone();
//		titaVoTemp.putParam(ContentName.txCode, beanName);
		if (ThreadVariable.isLogger())
			logger.info("newTask Call : " + titaVoTemp.getTxCode());
		ThreadPoolTaskExecutor taskExecutor = (ThreadPoolTaskExecutor) MySpring.getBean("taskExecutor");

		TaskBatchCallable taskBatchCallable = (TaskBatchCallable) MySpring.getBean("taskBatchCallable");
		taskBatchCallable.setTxBuffer(txBuffer);
		taskBatchCallable.setTitaVo(titaVoTemp);
		taskBatchCallable.setBeanName(beanName);
		if (ThreadVariable.isLogger())
			taskBatchCallable.setLoggerFg("1");
		Future<TotaVoList> future = taskExecutor.submit(taskBatchCallable);

		TotaVoList re = null;
		try {
			re = future.get();
		} catch (InterruptedException | ExecutionException e) {
			StringWriter errors = new StringWriter();
			e.printStackTrace(new PrintWriter(errors));
			logger.error(errors.toString());
		}
		return re;
	}

}
