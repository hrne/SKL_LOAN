package com.st1.itx.tradeService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.st1.itx.Exception.LogicException;
import com.st1.itx.buffer.TxBuffer;
import com.st1.itx.dataVO.TitaVo;
import com.st1.itx.dataVO.TotaVoList;
import com.st1.itx.eum.ThreadVariable;

public abstract class CommBuffer {
	private Logger logger;

	public TxBuffer txBuffer;

	public TitaVo titaVo;

	public TotaVoList totaVoList = new TotaVoList();

	private boolean loggerFg = true;

	private String beanName = "";

	public int index = 0;

	public int limit = 500;

	public CommBuffer() {
		Class<?> enclosingClass = getClass().getEnclosingClass();
		if (enclosingClass != null)
			logger = LoggerFactory.getLogger(enclosingClass.getName());
		else
			logger = LoggerFactory.getLogger(getClass().getName());

		loggerFg = ThreadVariable.isLogger();
	}

	public TitaVo getTitaVo() {
		return titaVo;
	}

	public void setTitaVo(TitaVo titaVo) {
		this.titaVo = titaVo;
	}

	public TxBuffer getTxBuffer() {
		return txBuffer;
	}

	public void setTxBuffer(TxBuffer txBuffer) {
		this.txBuffer = txBuffer;
	}

	public TotaVoList getTotaVoList() {
		return totaVoList;
	}

	public void setTotaVoList(TotaVoList totaVoList) {
		this.totaVoList = totaVoList;
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

	public String getBeanName() {
		return beanName;
	}

	public void setBeanName(String beanName) {
		this.beanName = beanName;
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

	public abstract void exec() throws LogicException;
}
