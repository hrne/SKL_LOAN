package com.st1.itx.batch.listener;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;

import org.codehaus.jettison.json.JSONObject;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobExecutionListener;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.st1.itx.Exception.DBException;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.db.domain.JobMain;
import com.st1.itx.db.domain.JobMainId;
import com.st1.itx.db.domain.TxCruiser;
import com.st1.itx.db.domain.TxCruiserId;
import com.st1.itx.db.service.JobMainService;
import com.st1.itx.db.service.TxCruiserService;
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
	private TxCruiserService txCruiserService;

	@Autowired
	Parse parse;

	private static int maxHostTranC = 9999999;
	public static AtomicInteger atomNext = new AtomicInteger(0);

	@Override
	public void beforeJob(JobExecution jobExecution) {
		jobExecution.getExecutionContext().put(ContentName.threadName, Thread.currentThread().getName());

		if ("true".equals(jobExecution.getJobParameters().getString("loogerFg")))
			ThreadVariable.setObject(ContentName.loggerFg, true);
		ThreadVariable.setObject(ContentName.empnot,
				jobExecution.getJobParameters().getString(ContentName.tlrno, "999999"));

		String jobId = jobExecution.getJobParameters().getString(ContentName.jobId);
		String jobIdC = jobExecution.getJobConfigurationName();
		Date time = jobExecution.getJobParameters().getDate(ContentName.batchDate);
		int execDate = Integer.valueOf(new SimpleDateFormat("yyyyMMdd").format(time));
		Timestamp startTime = new Timestamp(jobExecution.getStartTime().getTime());

		this.info("batch JobConfiguration Name : " + jobIdC);
		this.info("batch Job Name    : " + jobId);
		this.info("batch execDate    : " + execDate);
		this.info("before startTime : " + startTime);

		this.updJobMain(jobId, execDate, startTime, true, jobExecution);
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
		this.updJobMain(jobId, execDate, endTime, false, jobExecution);
	}

	/**
	 * 更新批次工作主檔
	 * 
	 * @param jobId        批次代號
	 * @param execDate     批次執行日期
	 * @param time         啟動時間 / 結束時間
	 * @param seFg         啟動 / 結束
	 * @param jobExecution 執行階段
	 */
	private void updJobMain(String jobId, int execDate, Timestamp time, boolean seFg, JobExecution jobExecution) {
		JobMainId jobMainId = new JobMainId();
		JobMain jobMain = new JobMain();
		TitaVo titaVo = new TitaVo();

		titaVo.putParam(ContentName.empnot, jobExecution.getJobParameters().getString(ContentName.tlrno, "999999"));

		String txSeq = jobExecution.getJobParameters().getString(ContentName.txSeq);
		txSeq = Objects.isNull(txSeq) || txSeq.trim().isEmpty()
				? jobExecution.getExecutionContext().getString(ContentName.txSeq, "")
				: txSeq;

		if (Objects.isNull(txSeq) || txSeq.trim().isEmpty()) {
			jobMainId.setTxSeq(new Date().getTime() + this.getNextHostTranC());
			jobExecution.getExecutionContext().put(ContentName.txSeq, jobMainId.getTxSeq());
		} else {
			jobMainId.setTxSeq(txSeq);
		}
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
				
				ExecutionContext jobExecutionContext = jobExecution.getExecutionContext();
				// 若不存在才跑
				if (!jobExecutionContext.containsKey("OriTxSeq")) {
					// 2023-08-08 Wei 新壽IT說在L6970勾重新執行的時候,已經成功的步驟不要重新執行
					// 因此新增OriTxSeq找讓StepExecuter可以找出原本的步驟執行結果
					String oriTxSeq = getOriTxSeq(txSeq, titaVo);
					jobExecution.getExecutionContext().putString("OriTxSeq", oriTxSeq);
				}
			} else {
				boolean status = true;
				Collection<StepExecution> stepEli = jobExecution.getStepExecutions();
				for (StepExecution se : stepEli)
					status = se.getExitStatus().getExitCode().equals("FAILED") ? false : status;

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

	protected String getNextHostTranC() {
		atomNext.compareAndSet(maxHostTranC, 0); // 如果到底了 就歸零
		int hosttranc = atomNext.incrementAndGet();
		this.info("TranC : " + String.format("%07d", hosttranc));
		return String.format("%07d", hosttranc);
	}

}
