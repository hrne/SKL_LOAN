package com.st1.itx.batch.listener;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import org.codehaus.jettison.json.JSONObject;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.JobExecution;
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
import com.st1.itx.db.domain.TxCruiser;
import com.st1.itx.db.domain.TxCruiserId;
import com.st1.itx.db.service.JobDetailService;
import com.st1.itx.db.service.TxCruiserService;
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
	private TxCruiserService txCruiserService;

	@Autowired
	Parse parse;

	@Override
	public void beforeStep(StepExecution stepExecution) {
		ThreadVariable.setObject(ContentName.loggerFg, true);
		if ("true".equals(stepExecution.getJobExecution().getJobParameters().getString("loogerFg")))
			ThreadVariable.setObject(ContentName.loggerFg, true);
		ThreadVariable.setObject(ContentName.empnot,
				stepExecution.getJobExecution().getJobParameters().getString(ContentName.tlrno, "999999"));

		JobExecution jobExecution = stepExecution.getJobExecution();

		String jobId = jobExecution.getJobParameters().getString(ContentName.jobId);
		String nestedJobId = stepExecution.getJobExecution().getJobInstance().getJobName();
		this.info("Nested Job ID: " + nestedJobId);
		String stepId = stepExecution.getStepName();
		Date time = jobExecution.getJobParameters().getDate(ContentName.batchDate);
		int execDate = Integer.valueOf(new SimpleDateFormat("yyyyMMdd").format(time));
		Timestamp startTime = new Timestamp(stepExecution.getStartTime().getTime());

		String txSeq = jobExecution.getJobParameters().getString(ContentName.txSeq);

		ExecutionContext jobEc = jobExecution.getExecutionContext();

		String oriTxSeq = jobEc.getString("OriTxSeq");
		String oriStep = "";
		String rerunType = "";
		if (jobEc.containsKey("RerunType")) {
			rerunType = jobEc.getString("RerunType");
		}
		if (jobEc.containsKey("OriStep")) {
			oriStep = jobEc.getString("OriStep");
		}

		this.info("StepExecListener.beforeStep : " + stepId);
		this.info("batch execDate    : " + execDate);
		this.info("step startTime  : " + startTime);
		this.info("txSeq  : " + txSeq);
		this.info("oriTxSeq  : " + oriTxSeq);
		this.info("rerunType  : " + rerunType);
		this.info("oriStep  : " + oriStep);

		this.updateJobDetail(jobId, nestedJobId, stepId, execDate, startTime, true, stepExecution);

		stepExecution.getExecutionContext().put("txSeq", txSeq);

		// rerunType為A時,全部重跑
		if (!oriTxSeq.isEmpty() && !rerunType.isEmpty() && !rerunType.equals("A")) {
			this.rerunHandler(oriTxSeq, jobId, rerunType, stepId, oriStep, stepExecution);
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
		
		// 2023-08-09 Wei
		JobExecution jobExecution = stepExecution.getJobExecution();
		ExecutionContext jobEc = jobExecution.getExecutionContext();
		String rerunType = "";
		if (jobEc.containsKey("RerunType")) {
			rerunType = jobEc.getString("RerunType");
		}

		this.info("StepExecListener.afterStep : " + stepId);
		this.info("batch execDate     : " + execDate);
		this.info("step endTime       : " + endTime);
		this.info("step stepStatus   : " + stepExecution.getExitStatus().getExitCode());
		this.info("step oriStatus : " + oriStatus);
		this.info("step rerunType : " + rerunType);

		if ((!rerunType.equals("A")) && oriStatus.equals("S")) {
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

	private void rerunHandler(String oriTxSeq, String jobId, String rerunType, String stepId, String oriStep,
			StepExecution stepExecution) {
		if (rerunType.equals("S")) {
			if (oriStep.equals(stepId)) {
				// 單支重跑:Step名稱相同,重跑該Step
				stepExecution.getExecutionContext().putString("OriStatus", "F");
				return;
			} else {
				// 單支重跑:Step名稱不同,當作該Step已成功
				stepExecution.getExecutionContext().putString("OriStatus", "S");
				return;
			}
		}

		TitaVo titaVo = new TitaVo();
		titaVo.putParam(ContentName.empnot, stepExecution.getJobParameters().getString(ContentName.tlrno, "999999"));
		JobDetail oriJobDetail = jobDetailService.findStepFirst(oriTxSeq, jobId, stepId, titaVo);
		if (oriJobDetail != null) {
			String status = oriJobDetail.getStatus();
			// 2023-08-08 Wei 新壽IT說在L6970勾重新執行的時候,已經成功的步驟不要重新執行
			// 因此新增OriTxSeq找讓StepExecuter可以找出原本的步驟執行結果
			stepExecution.getExecutionContext().putString("OriStatus", status);
		} else {
			this.error("chkOriJobDetail cannot find ori jobDetail. oriTxSeq = " + oriTxSeq);
			// 找出原交易序號的原交易序號,直到有用原交易序號找到對應jobDetail為止,或是原交易序號為空白
			String oriOriTxSeq = getOriTxSeq(oriTxSeq, titaVo);
			if (oriOriTxSeq != null && !oriOriTxSeq.isEmpty()) {
				// 遞迴處理
				rerunHandler(oriOriTxSeq, jobId, rerunType, stepId, oriStep, stepExecution);
			}
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
				this.info("jobDetail 這筆是單支重跑/失敗重跑時的skip,進行 delete. " + jobDetail.toString());
			}
		} catch (DBException e) {
			this.error(e.getErrorId() + " " + e.getErrorMsg());
		}
	}

	private String getOriTxSeq(String txSeq, TitaVo titaVo) {
		String oriTxSeq = "";
		TxCruiserId txCruiserId = new TxCruiserId();
		txCruiserId.setTxSeq(txSeq);
		if (!txSeq.contains("-")) {
			this.error("getOriTxSeq txSeq has not \"-\" , txSeq = " + txSeq);
			return oriTxSeq;
		}
		String[] s = txSeq.split("-");
		if (s.length < 2) {
			this.error("getOriTxSeq txSeq.split(\"-\") length < 2 , txSeq = " + txSeq);
			return oriTxSeq;
		}
		txCruiserId.setTlrNo(s[1]);
		TxCruiser txCruiser = txCruiserService.findById(txCruiserId, titaVo);
		if (txCruiser == null) {
			return oriTxSeq;
		}
		String parameters = txCruiser.getParameter();
		JSONObject p;
		String oriStatus;
		try {
			p = new JSONObject(parameters);
			oriTxSeq = p.getString("OOJobTxSeq");
			oriStatus = p.getString("OOStatus");
			if (oriStatus.equals("S")) {
				// 若原本勾的那筆STEP是成功的 就全部重跑
				// 只要將oriTxSeq放空白 就會全部重跑
				return "";
			}
		} catch (Exception e) {
			StringWriter errors = new StringWriter();
			e.printStackTrace(new PrintWriter(errors));
			this.error(
					"getOriTxSeq parameters transfer to JSONObject and getString(\"OOJobTxSeq\") error, parameters = "
							+ parameters);
			this.error("Exception = " + e.getMessage());
			return "";
		}
		return oriTxSeq;
	}
}