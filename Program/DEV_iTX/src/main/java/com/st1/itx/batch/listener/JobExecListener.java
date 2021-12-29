package com.st1.itx.batch.listener;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;

import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobExecutionListener;
import org.springframework.batch.core.StepExecution;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.st1.itx.Exception.DBException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.db.domain.JobMain;
import com.st1.itx.db.domain.JobMainId;
import com.st1.itx.db.service.JobMainService;
import com.st1.itx.eum.ContentName;
import com.st1.itx.eum.ThreadVariable;
import com.st1.itx.util.log.SysLogger;
import com.st1.itx.util.parse.Parse;

@Service
@Scope("prototype")
public class JobExecListener extends SysLogger implements JobExecutionListener {
	@Autowired
	private JobMainService jobMainService;

	@Autowired
	Parse parse;

	private static int maxHostTranC = 9999999;
	public static AtomicInteger atomNext = new AtomicInteger(0);

	@Override
	public void beforeJob(JobExecution jobExecution) {
		jobExecution.getExecutionContext().put(ContentName.threadName, Thread.currentThread().getName());

		if ("true".equals(jobExecution.getJobParameters().getString("loogerFg")))
			ThreadVariable.setObject(ContentName.loggerFg, true);
		ThreadVariable.setObject(ContentName.empnot, jobExecution.getJobParameters().getString(ContentName.tlrno, "BAT001"));

		String jobId = jobExecution.getJobParameters().getString(ContentName.jobId);
		String jobIdC = jobExecution.getJobConfigurationName();
		Date time = jobExecution.getJobParameters().getDate(ContentName.batchDate);
		int execDate = Integer.valueOf(new SimpleDateFormat("yyyyMMdd").format(time));
		Timestamp startTime = new Timestamp(jobExecution.getStartTime().getTime());

		this.info("batch JobConfiguration Name : " + jobIdC);
		this.info("batch Job Name    : " + jobId);
		this.info("batch execDate    : " + execDate);
		this.info("before startTime : " + startTime);

		this.updtaeJobMain(jobId, execDate, startTime, true, jobExecution);
	}

	@Override
	public void afterJob(JobExecution jobExecution) {
		String jobId = jobExecution.getJobParameters().getString(ContentName.jobId);
		String jobIdC = jobExecution.getJobConfigurationName();
		Date time = jobExecution.getJobParameters().getDate(ContentName.batchDate);
		int execDate = Integer.valueOf(new SimpleDateFormat("yyyyMMdd").format(time));
		Timestamp endTime = new Timestamp(jobExecution.getEndTime().getTime());

		this.info("batch JobConfiguration Name : " + jobIdC);
		this.info("batch Job Name    : " + jobId);
		this.info("batch execDate    : " + execDate);
		this.info("batch endTime    : " + endTime);

		ThreadVariable.clearThreadLocal();
		this.updtaeJobMain(jobId, execDate, endTime, false, jobExecution);
	}

	/**
	 * 更新批次工作主檔
	 * 
	 * @param jobId    批次代號
	 * @param execDate 批次執行日期
	 * @param time     啟動時間 / 結束時間
	 * @param seFg     啟動 / 結束
	 */
	private void updtaeJobMain(String jobId, int execDate, Timestamp time, boolean seFg, JobExecution jobExecution) {
		JobMainId jobMainId = new JobMainId();
		JobMain jobMain = new JobMain();
		TitaVo titaVo = new TitaVo();

		titaVo.putParam(ContentName.empnot, jobExecution.getJobParameters().getString(ContentName.tlrno, "BAT001"));

		String txSeq = jobExecution.getJobParameters().getString(ContentName.txSeq);
		txSeq = Objects.isNull(txSeq) ? jobExecution.getExecutionContext().getString(ContentName.txSeq) : txSeq;

		if (Objects.isNull(txSeq)) {
			jobMainId.setTxSeq(new Date().getTime() + this.getNextHostTranC());
			jobExecution.getExecutionContext().put(ContentName.txSeq, jobMainId.getTxSeq());
		} else
			jobMainId.setTxSeq(txSeq);
		jobMainId.setExecDate(execDate);
		jobMainId.setJobCode(jobId);

		try {
			if (seFg) {

				jobMain = jobMainService.findById(jobMainId, titaVo);

				if (jobMain != null) {
					jobMainService.delete(jobMain, titaVo);
					jobMain.setJobMainId(jobMainId);
					jobMain.setStartTime(time);
					jobMain.setStatus("U");
					jobMainService.insert(jobMain, titaVo);
				} else {
					jobMain = new JobMain();
					jobMain.setJobMainId(jobMainId);
					jobMain.setStartTime(time);
					jobMain.setStatus("U");
					jobMainService.insert(jobMain, titaVo);
				}

			} else {
				boolean status = true;
				Collection<StepExecution> stepEli = jobExecution.getStepExecutions();
				for (StepExecution se : stepEli)
					status = !se.getExitStatus().getExitCode().equals("COMPLETED") ? !status : status;

				jobMain = jobMainService.holdById(jobMainId);
				jobMain.setEndTime(time);
				if (!status)
					jobMain.setStatus("F");
				else
					jobMain.setStatus("S");
				jobMainService.update(jobMain, titaVo);
			}
		} catch (DBException e) {
			this.error(e.getErrorId() + " " + e.getErrorMsg());
		}
	}

	protected String getNextHostTranC() {
		atomNext.compareAndSet(maxHostTranC, 0); // 如果到底了 就歸零
		int hosttranc = atomNext.incrementAndGet();
		this.info("TranC : " + String.format("%07d", hosttranc));
		return String.format("%07d", hosttranc);
	}

}
