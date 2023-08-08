package com.st1.itx.batch.listener;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.StepExecutionListener;
import org.springframework.batch.item.ExecutionContext;
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
		ThreadVariable.setObject(ContentName.empnot,
				stepExecution.getJobExecution().getJobParameters().getString(ContentName.tlrno, "999999"));

		String jobId = stepExecution.getJobExecution().getJobParameters().getString(ContentName.jobId);
		String nestedJobId = stepExecution.getJobExecution().getJobInstance().getJobName();
		this.info("Nested Job ID: " + nestedJobId);
		String stepId = stepExecution.getStepName();
		Date time = stepExecution.getJobExecution().getJobParameters().getDate(ContentName.batchDate);
		int execDate = Integer.valueOf(new SimpleDateFormat("yyyyMMdd").format(time));
		Timestamp startTime = new Timestamp(stepExecution.getStartTime().getTime());

		String txSeq = stepExecution.getJobExecution().getJobParameters().getString(ContentName.txSeq);
		String oriTxSeq = stepExecution.getJobExecution().getExecutionContext().getString("OriTxSeq");

		this.info("StepExecListener.beforeStep : " + stepId);
		this.info("batch execDate    : " + execDate);
		this.info("step startTime  : " + startTime);
		this.info("txSeq  : " + txSeq);
		this.info("oriTxSeq  : " + oriTxSeq);

		this.updateJobDetail(jobId, nestedJobId, stepId, execDate, startTime, true, stepExecution);

		stepExecution.getExecutionContext().put("txSeq", txSeq);

		if (!oriTxSeq.isEmpty()) {
			this.chkOriJobDetail(oriTxSeq, jobId, stepId, stepExecution);
		}
	}

	@Override
	public ExitStatus afterStep(StepExecution stepExecution) {
		String jobId = stepExecution.getJobExecution().getJobParameters().getString(ContentName.jobId);
		String stepId = stepExecution.getStepName();

		Date time = stepExecution.getJobExecution().getJobParameters().getDate(ContentName.batchDate);
		int execDate = Integer.valueOf(new SimpleDateFormat("yyyyMMdd").format(time));

		Date date = new Date();
		Timestamp endTime = new Timestamp(date.getTime());

		// 2022-04-12 智偉增加:
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

		// 2023-08-08 Wei
		String oriStatus = "";
		if (stepExecution.getExecutionContext().containsKey("OriStatus")) {
			oriStatus = stepExecution.getExecutionContext().getString("OriStatus");
		}

		this.info("StepExecListener.afterStep : " + stepId);
		this.info("batch execDate     : " + execDate);
		this.info("step endTime       : " + endTime);
		this.info("step stepStatus   : " + stepExecution.getExitStatus().getExitCode());
		this.info("step oriStatus : " + oriStatus);

		if (oriStatus.equals("S")) {
			this.deleteJobDetail(jobId, null, stepId, execDate, endTime, stepExecution);
		} else {
			this.updateJobDetail(jobId, null, stepId, execDate, endTime, false, stepExecution, stepStatus);
		}

		if (!Thread.currentThread().getName()
				.equals(stepExecution.getJobExecution().getExecutionContext().getString(ContentName.threadName)))
			ThreadVariable.clearThreadLocal();

		return stepExecution.getExitStatus();
	}

	/**
	 * 更新批次工作明細檔
	 * 
	 * @param jobId         批次代號
	 * @param nestedJobId   子批次代號
	 * @param stepId        程式代號
	 * @param execDate      批次執行日期
	 * @param time          啟動時間 / 結束時間
	 * @param seFg          啟動 / 結束
	 * @param stepExecution 執行階段
	 * @param stepStatus    執行狀態
	 */
	private void updateJobDetail(String jobId, String nestedJobId, String stepId, int execDate, Timestamp time,
			boolean seFg, StepExecution stepExecution, String... stepStatus) {
		JobDetailId jobDetailId = new JobDetailId();
		JobDetail jobDetail = new JobDetail();
		TitaVo titaVo = new TitaVo();
		titaVo.putParam(ContentName.empnot, stepExecution.getJobParameters().getString(ContentName.tlrno, "999999"));

		String txSeq = stepExecution.getJobExecution().getJobParameters().getString(ContentName.txSeq);
		String batchType = stepExecution.getJobExecution().getJobParameters().getString(ContentName.batchType);
		txSeq = Objects.isNull(txSeq) || txSeq.trim().isEmpty()
				? stepExecution.getJobExecution().getExecutionContext().getString(ContentName.txSeq, "")
				: txSeq;

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
				jobDetail.setNestJobCode(nestedJobId);
				jobDetail.setBatchType(batchType);
				jobDetail.setStepStartTime(time);
				jobDetailService.insert(jobDetail, titaVo);
				this.info("jobDetail insert. " + jobDetail.toString());
			} else {
				String errorCode = "";
				String errorMsg = "";
				List<Throwable> failureExceptions = stepExecution.getFailureExceptions();
				for (Throwable t : failureExceptions) {
					errorCode = "E0013"; // 程式邏輯錯誤
					// 取得錯誤訊息
					errorMsg = t.getMessage();
					this.error("Error message: " + errorMsg);
				}

				ExecutionContext ec = stepExecution.getJobExecution().getExecutionContext();
				if (ec.containsKey("TaskErrorCode")) {
					// 2023-07-26 智偉 把 task 在執行時出的錯誤記錄下來
					errorCode = ec.getString("TaskErrorCode");
					errorMsg = ec.getString("TaskErrorMsg");
					this.error("TaskErrorCode: " + errorCode);
					this.error("TaskErrorMsg: " + errorMsg);
				}

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
					jobDetail.setErrCode(errorCode);
					jobDetail.setErrContent(errorMsg);
					jobDetailService.update(jobDetail, titaVo);
					this.info("jobDetail update. " + jobDetail.toString());
				}
			}
		} catch (DBException e) {
			this.error(e.getErrorId() + " " + e.getErrorMsg());
		}
	}

	/**
	 * 找原執行結果
	 * 
	 * @param oriTxSeq      原交易序號
	 * @param jobId         原批次名稱
	 * @param stepId        原步驟名稱
	 * @param stepExecution 執行階段
	 */
	private void chkOriJobDetail(String oriTxSeq, String jobId, String stepId, StepExecution stepExecution) {
		TitaVo titaVo = new TitaVo();
		titaVo.putParam(ContentName.empnot, stepExecution.getJobParameters().getString(ContentName.tlrno, "999999"));
		JobDetail oriJobDetail = jobDetailService.findStepFirst(oriTxSeq, jobId, stepId, titaVo);
		if (oriJobDetail != null) {
			String status = oriJobDetail.getStatus();
			// 2023-08-08 Wei 新壽IT說在L6970勾重新執行的時候,已經成功的步驟不要重新執行
			// 因此新增OriTxSeq找讓StepExecuter可以找出原本的步驟執行結果
			stepExecution.getExecutionContext().putString("OriStatus", status);
		} else {
			this.error("chkOriJobDetail cannot find ori jobDetail.");
		}
	}

	private void deleteJobDetail(String jobId, Object object, String stepId, int execDate, Timestamp endTime,
			StepExecution stepExecution) {

		JobDetailId jobDetailId = new JobDetailId();
		JobDetail jobDetail = new JobDetail();
		TitaVo titaVo = new TitaVo();
		titaVo.putParam(ContentName.empnot, stepExecution.getJobParameters().getString(ContentName.tlrno, "999999"));

		String txSeq = stepExecution.getJobExecution().getJobParameters().getString(ContentName.txSeq);
		txSeq = txSeq.trim().isEmpty()
				? stepExecution.getJobExecution().getExecutionContext().getString(ContentName.txSeq, "")
				: txSeq;

		if (txSeq.trim().isEmpty()) {
			this.info("txSeq is Null or Empty..");
			return;
		} else {
			jobDetailId.setTxSeq(txSeq);
		}

		try {
			jobDetailId.setExecDate(execDate);
			jobDetailId.setJobCode(jobId);
			jobDetailId.setStepId(stepId);
			jobDetail = jobDetailService.holdById(jobDetailId);
			if (jobDetail == null) {
				this.error("jobDetail is null. " + jobDetailId.toString());
			} else {
				jobDetailService.delete(jobDetail, titaVo);
				this.info("jobDetail 這筆是重跑時的skip,進行 delete. " + jobDetail.toString());
			}
		} catch (DBException e) {
			this.error(e.getErrorId() + " " + e.getErrorMsg());
		}
	}
}