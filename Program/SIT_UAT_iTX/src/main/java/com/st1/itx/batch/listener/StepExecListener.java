package com.st1.itx.batch.listener;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;

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

		String stepStatus = stepExecution.getExitStatus().getExitCode().equals("COMPLETED") ? "S" : "F";

		this.info("StepExecListener.afterStep : " + stepId);
		this.info("batch execDate     : " + execDate);
		this.info("step endTime       : " + endTime);
		this.info("step stepStatus   : " + stepStatus);

		this.updtaeJobDetail(jobId, stepId, execDate, endTime, false, stepExecution, stepStatus);

		return stepExecution.getExitStatus();
	}

	/**
	 * 更新批次工作明細檔
	 * 
	 * @param jobId      批次代號
	 * @param stepId     程式代號
	 * @param execDate   批次執行日期
	 * @param time       啟動時間 / 結束時間
	 * @param seFg       啟動 / 結束
	 * @param stepStatus 執行狀態
	 */
	private void updtaeJobDetail(String jobId, String stepId, int execDate, Timestamp time, boolean seFg, StepExecution stepExecution, String... stepStatus) {
		JobDetailId jobDetailId = new JobDetailId();
		JobDetail jobDetail = new JobDetail();
		TitaVo titaVo = new TitaVo();
		titaVo.putParam(ContentName.empnot, stepExecution.getJobParameters().getString(ContentName.tlrno, "BAT001"));

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