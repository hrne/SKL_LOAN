package com.st1.itx.batch.listener;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;

import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.StepExecutionListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.st1.itx.Exception.DBException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.db.domain.JobDetail;
import com.st1.itx.db.domain.JobDetailId;
import com.st1.itx.db.service.JobDetailService;
import com.st1.itx.eum.ContentName;
import com.st1.itx.eum.ThreadVariable;
import com.st1.itx.util.log.SysLogger;
import com.st1.itx.util.parse.Parse;

@Service
@Scope("prototype")
public class StepExecListener extends SysLogger implements StepExecutionListener {

	@Autowired
	JobDetailService jobDetailService;

	@Autowired
	Parse parse;

	@Override
	public void beforeStep(StepExecution stepExecution) {
		ThreadVariable.setObject(ContentName.loggerFg, true);
		if ("true".equals(stepExecution.getJobExecution().getJobParameters().getString("loogerFg")))
			ThreadVariable.setObject(ContentName.loggerFg, true);
		ThreadVariable.setObject(ContentName.empnot, stepExecution.getJobExecution().getJobParameters().getString(ContentName.tlrno, "BAT001"));

		String jobId = stepExecution.getJobExecution().getJobParameters().getString(ContentName.jobId);
		String stepId = stepExecution.getStepName();
		Date time = stepExecution.getJobExecution().getJobParameters().getDate(ContentName.batchDate);
		int execDate = Integer.valueOf(new SimpleDateFormat("yyyyMMdd").format(time));
		Timestamp startTime = new Timestamp(stepExecution.getStartTime().getTime());

		this.info("StepExecListener.beforeStep : " + stepId);
		this.info("batch execDate    : " + execDate);
		this.info("step startTime  : " + startTime);

		this.updtaeJobDetail(jobId, stepId, execDate, startTime, true, stepExecution);
	}

	@Override
	public ExitStatus afterStep(StepExecution stepExecution) {
		String jobId = stepExecution.getJobExecution().getJobParameters().getString(ContentName.jobId);
		String stepId = stepExecution.getStepName();

		Date time = stepExecution.getJobExecution().getJobParameters().getDate(ContentName.batchDate);
		int execDate = Integer.valueOf(new SimpleDateFormat("yyyyMMdd").format(time));

		Date date = new Date();
		Timestamp endTime = new Timestamp(date.getTime());

		// 2022-04-12 ????????????:
		String exitCode = stepExecution.getExitStatus().getExitCode();
		String stepStatus = "";
		switch (exitCode) {
		case "COMPLETED":
			stepStatus = "S";
			break;
		case "STOPPED":
			stepStatus = "S";
			break;
		default:
			stepStatus = "F";
			break;
		}

		this.info("StepExecListener.afterStep : " + stepId);
		this.info("batch execDate     : " + execDate);
		this.info("step endTime       : " + endTime);
		this.info("step stepStatus   : " + stepExecution.getExitStatus().getExitCode());

		this.updtaeJobDetail(jobId, stepId, execDate, endTime, false, stepExecution, stepStatus);

		if (!Thread.currentThread().getName().equals(stepExecution.getJobExecution().getExecutionContext().getString(ContentName.threadName)))
			ThreadVariable.clearThreadLocal();

		return stepExecution.getExitStatus();
	}

	/**
	 * ???????????????????????????
	 * 
	 * @param jobId      ????????????
	 * @param stepId     ????????????
	 * @param execDate   ??????????????????
	 * @param time       ???????????? / ????????????
	 * @param seFg       ?????? / ??????
	 * @param stepStatus ????????????
	 */
	private void updtaeJobDetail(String jobId, String stepId, int execDate, Timestamp time, boolean seFg, StepExecution stepExecution, String... stepStatus) {
		JobDetailId jobDetailId = new JobDetailId();
		JobDetail jobDetail = new JobDetail();
		TitaVo titaVo = new TitaVo();
		titaVo.putParam(ContentName.empnot, stepExecution.getJobParameters().getString(ContentName.tlrno, "BAT001"));

		String txSeq = stepExecution.getJobExecution().getJobParameters().getString(ContentName.txSeq);
		txSeq = Objects.isNull(txSeq) || txSeq.trim().isEmpty() ? stepExecution.getJobExecution().getExecutionContext().getString(ContentName.txSeq, "") : txSeq;

		if (Objects.isNull(txSeq) || txSeq.trim().isEmpty()) {
			this.info("txSeq is Null or Empty..");
			return;
		} else
			jobDetailId.setTxSeq(txSeq);

		try {
			if (seFg) {
				jobDetailId.setExecDate(execDate);
				jobDetailId.setJobCode(jobId);
				jobDetailId.setStepId(stepId);

				jobDetail = jobDetailService.findById(jobDetailId, titaVo);
				if (jobDetail != null) {
					jobDetailService.delete(jobDetail, titaVo);
					this.info("jobDetail delete. " + jobDetail.toString());
				}
				jobDetail = new JobDetail();

				jobDetail.setJobDetailId(jobDetailId);
				jobDetail.setStepStartTime(time);
				jobDetailService.insert(jobDetail, titaVo);
				this.info("jobDetail insert. " + jobDetail.toString());
			} else {
				jobDetailId.setExecDate(execDate);
				jobDetailId.setJobCode(jobId);
				jobDetailId.setStepId(stepId);
				jobDetail = jobDetailService.holdById(jobDetailId);
				if (jobDetail == null) {
					this.error("jobDetail is null. " + jobDetailId.toString());
				} else {
					if (stepStatus != null) {
						jobDetail.setStatus(stepStatus[0].substring(0, 1));
					}
					jobDetail.setStepEndTime(time);
					jobDetailService.update(jobDetail, titaVo);
					this.info("jobDetail update. " + jobDetail.toString());
				}
			}
		} catch (DBException e) {
			this.error(e.getErrorId() + " " + e.getErrorMsg());
		}
	}
}