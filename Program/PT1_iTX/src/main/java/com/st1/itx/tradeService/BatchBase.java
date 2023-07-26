package com.st1.itx.tradeService;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.sql.SQLException;
import java.util.Objects;

import javax.annotation.PostConstruct;

import org.hibernate.exception.SQLGrammarException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import com.st1.itx.Exception.LogicException;
import com.st1.itx.buffer.TxBuffer;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.db.domain.TxCruiser;
import com.st1.itx.db.domain.TxCruiserId;
import com.st1.itx.db.service.TxCruiserService;
import com.st1.itx.eum.ContentName;
import com.st1.itx.eum.ThreadVariable;

public abstract class BatchBase {

	@Autowired
	private TxCruiserService txCruiserService;

	@Autowired
	public TxBuffer txBuffer;

	@Autowired
	public TitaVo titaVo;

	private Logger logger;

	private boolean loggerFg = true;

	private String jobId;

	private String tlrNo;

	private String excuteMode;

	private String dataBase;

	private String parent;

	private String logFg;

	private String txSeq;

	@PostConstruct
	private void init() {
		try {
			Class<?> enclosingClass = getClass().getEnclosingClass();
			if (enclosingClass != null)
				logger = LoggerFactory.getLogger(enclosingClass.getName());
			else
				logger = LoggerFactory.getLogger(getClass().getName());

			loggerFg = ThreadVariable.isLogger();

			TxCruiser txCruiser = txCruiserService.findById(new TxCruiserId(this.getTxSeq(), this.getTlrNo()));
			if (Objects.isNull(txCruiser)) {
				this.titaVo = new TitaVo();
				this.titaVo.init();

				this.titaVo.putParam(ContentName.kinbr, "0000");
				this.titaVo.putParam(ContentName.tlrno, tlrNo);
				this.titaVo.putParam(ContentName.empnot, tlrNo);
			} else
				this.titaVo = this.titaVo.getVo(txCruiser.getParameter());

			if ("L6870".equals(this.getParent())) {
				this.titaVo.putParam(ContentName.tlrno, "999999");
				this.titaVo.putParam(ContentName.empnot, "999999");
			}

			if ("0".equals(this.excuteMode))
				this.titaVo.putParam(ContentName.dataBase, this.dataBase);
			else {
				if (this.txBuffer.getTxCom().getTbsdyf() == this.txBuffer.getTxCom().getTmndyf())
					this.titaVo.setDataBaseOnMon();
				else
					this.titaVo.setDataBaseOnDay();
			}

			this.txBuffer.init(this.titaVo);
			this.titaVo.putParam(ContentName.entdy, "0" + this.txBuffer.getTxCom().getTbsdy());
		} catch (LogicException | IOException e) {
			if (e instanceof LogicException)
				this.error(((LogicException) e).getErrorMsgId() + " " + ((LogicException) e).getErrorMsg());
			else {
				StringWriter errors = new StringWriter();
				e.printStackTrace(new PrintWriter(errors));
				this.error(errors.toString());
			}
		}
	}

	public TxBuffer getTxBuffer() {
		return this.txBuffer;
	}

	public void setTxBuffer(TxBuffer txBuffer) {
		this.txBuffer = txBuffer;
	}

	public boolean getLoggerFg() {
		return loggerFg;
	}

	public void setLoggerFg(String loggerFg) {
		this.loggerFg = loggerFg != null && loggerFg.equals("1") ? true : false;
	}

	public void setLoggerFg(String loggerFg, String name) {
		if (logger == null && name != null)
			logger = LoggerFactory.getLogger(name);
		this.loggerFg = loggerFg != null && loggerFg.equals("1") ? true : false;
	}

	public void mustInfo(String msg) {
		logger.info(msg);
	}

	public void info(String msg) {
		if (loggerFg)
			logger.info(msg);
	}

	public void warn(String msg) {
		if (loggerFg)
			logger.warn(msg);
	}

	public void error(String msg) {
		logger.error(msg);
	}

	@Value("#{jobParameters['jobId']}")
	public void setJobId(final String jobId) {
		this.jobId = jobId;
	}

	public String getJobId() {
		return jobId;
	}

	@Value("#{jobParameters['TLRNO']}")
	public void setTlrNo(final String tlrNo) {
		this.tlrNo = tlrNo;
	}

	public String getTlrNo() {
		return this.tlrNo;
	}

	public String getExcuteMode() {
		return excuteMode;
	}

	@Value("#{jobParameters['excuteMode']}")
	public void setExcuteMode(String excuteMode) {
		this.excuteMode = excuteMode;
	}

	public String getDataBase() {
		return dataBase;
	}

	@Value("#{jobParameters['DATABASE']}")
	public void setDataBase(String dataBase) {
		this.dataBase = dataBase;
	}

	public String getParent() {
		return parent;
	}

	@Value("#{jobParameters['parent']}")
	public void setParent(String parent) {
		this.parent = parent;
	}

	public String getLogFg() {
		return logFg;
	}

	@Value("#{jobParameters['loogerFg']}")
	public void setLogFg(String logFg) {
		this.logFg = logFg;
	}

	public String getTxSeq() {
		return txSeq;
	}

	@Value("#{jobParameters['txSeq']}")
	public void setTxSeq(String txSeq) {
		this.txSeq = txSeq;
	}

	public abstract void run() throws LogicException;

	/**
	 * M = Monthly<br>
	 * D = Daily
	 * 
	 * @param sc StepContribution
	 * @param md M = Monthly, D = Daily
	 * @return RepeatStatus...
	 */
	public RepeatStatus exec(StepContribution sc, String md) {
		this.info("batch run...." + md);
		this.info(this.titaVo.toString());

		try {
			this.run();
			sc.setExitStatus(ExitStatus.COMPLETED);
			return RepeatStatus.FINISHED;
		} catch (LogicException e) {
			StringWriter errors = new StringWriter();
			e.printStackTrace(new PrintWriter(errors));
			this.error(errors.toString());
			if (e.getErrorMsgId().equals("S0001")) {
				sc.setExitStatus(ExitStatus.STOPPED);
			} else {
				sc.setExitStatus(ExitStatus.FAILED);
				String errorCode = e.getErrorMsgId();
				String errorMsg = e.getMessage();
				this.titaVo.putParam("TaskErrorCode", errorCode);
				this.titaVo.putParam("TaskErrorMsg", errorMsg);
			}
			return RepeatStatus.FINISHED;
		} catch (Throwable e) {
			StringWriter errors = new StringWriter();
			e.printStackTrace(new PrintWriter(errors));
			this.error(errors.toString());
			sc.setExitStatus(ExitStatus.FAILED);
			String errorMsg = e.getMessage();
			this.titaVo.putParam("TaskErrorCode", "E0013");
			this.titaVo.putParam("TaskErrorMsg", errorMsg);
			return RepeatStatus.FINISHED;
		}
	}

	public RepeatStatus exec(StepContribution sc, String md, ChunkContext chunkContext) {
		this.info("batch run...." + md);
		this.info(this.titaVo.toString());

		ExecutionContext ec = chunkContext.getStepContext().getStepExecution().getJobExecution().getExecutionContext();

		try {
			this.run();
			sc.setExitStatus(ExitStatus.COMPLETED);
			return RepeatStatus.FINISHED;
		} catch (LogicException e) {
			StringWriter errors = new StringWriter();
			e.printStackTrace(new PrintWriter(errors));
			this.error(errors.toString());
			if (e.getErrorMsgId().equals("S0001")) {
				sc.setExitStatus(ExitStatus.STOPPED);
			} else {
				sc.setExitStatus(ExitStatus.FAILED);
				ec.put("TaskErrorCode", e.getErrorMsgId());
				ec.put("TaskErrorMsg", e.getMessage());
			}
			return RepeatStatus.FINISHED;
		} catch (Throwable e) {
			StringWriter errors = new StringWriter();
			e.printStackTrace(new PrintWriter(errors));
			this.error(errors.toString());
			sc.setExitStatus(ExitStatus.FAILED);
			String errorMsg = e.getMessage();
			ec.put("TaskErrorCode", "E0013"); // 程式邏輯錯誤
			ec.put("TaskErrorMsg", errorMsg);
			return RepeatStatus.FINISHED;
		}
	}

	public void handleUspException(Exception ex) throws LogicException {
		Throwable cause = ex.getCause();
		while (!(cause instanceof SQLException) && cause != null) {
			cause = cause.getCause();
		}
		String errorMsg = ex.getMessage();
		if (cause instanceof SQLException) {
			SQLException sqlEx = (SQLException) cause;
			errorMsg = sqlEx.getMessage();
			this.error("SQLException Error Msg: " + errorMsg);
		} else if (cause instanceof SQLGrammarException) {
			SQLGrammarException sqlge = (SQLGrammarException) cause;
			errorMsg = sqlge.getMessage();
			this.error("SQLGrammarException Error Msg: " + errorMsg);
		} else {
			this.error("Unexpected sql exception Error Msg: " + errorMsg);
		}
		StringWriter errors = new StringWriter();
		ex.printStackTrace(new PrintWriter(errors));
		String stackTrace = errors.toString();
		// 完整錯誤輸出
		this.error("Stack trace: " + stackTrace);
		throw new LogicException("EC007", errorMsg); // 資料庫批次錯誤
	}
}
