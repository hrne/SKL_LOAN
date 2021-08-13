package com.st1.itx.batch.listener;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobExecutionListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.st1.itx.Exception.DBException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.db.domain.JobMain;
import com.st1.itx.db.domain.JobMainId;
import com.st1.itx.db.service.JobMainService;
import com.st1.itx.eum.ContentName;
import com.st1.itx.util.log.SysLogger;
import com.st1.itx.util.parse.Parse;

@Service
@Scope("prototype")
public class JobExecListener extends SysLogger implements JobExecutionListener {
	@Autowired
	JobMainService jobMainService;

	@Autowired
	Parse parse;

	@Override
	public void beforeJob(JobExecution jobExecution) {
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
		String jobStatus = jobExecution.getExitStatus().getExitCode();

		this.info("batch JobConfiguration Name : " + jobIdC);
		this.info("batch Job Name    : " + jobId);
		this.info("batch execDate    : " + execDate);
		this.info("batch endTime    : " + endTime);
		this.info("jobStatus    : " + jobStatus);

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

		jobMainId.setExecDate(execDate);
		jobMainId.setJobCode(jobId);

		try {
			if (seFg) {

				jobMain = jobMainService.findById(jobMainId, titaVo);

				if (jobMain != null) {
					jobMainService.delete(jobMain, titaVo);
					jobMain.setJobMainId(jobMainId);
					jobMain.setStartTime(time);
					jobMainService.insert(jobMain, titaVo);
				} else {
					jobMain = new JobMain();
					jobMain.setJobMainId(jobMainId);
					jobMain.setStartTime(time);
					jobMainService.insert(jobMain, titaVo);
				}

			} else {
				jobMain = jobMainService.holdById(jobMainId);
				jobMain.setEndTime(time);
				jobMainService.update(jobMain, titaVo);
			}
		} catch (DBException e) {
			this.error(e.getErrorId() + " " + e.getErrorMsg());
		}
	}

}
