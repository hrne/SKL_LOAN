package com.st1.itx.util.common.data;

import java.util.ArrayList;
import java.util.LinkedHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.st1.itx.Exception.LogicException;
import com.st1.itx.eum.ThreadVariable;

/**
 * @author ST1-Chih Wei
 *
 */
public abstract class FileVo extends LinkedHashMap<String, Object> {
	private static final long serialVersionUID = -6230783957408765718L;

	private Logger logger;

	private boolean loggerFg = false;

	public FileVo() {
		Class<?> enclosingClass = getClass().getEnclosingClass();
		if (enclosingClass != null)
			logger = LoggerFactory.getLogger(enclosingClass.getName());
		else
			logger = LoggerFactory.getLogger(getClass().getName());

		loggerFg = ThreadVariable.isLogger();
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
		if (ThreadVariable.isLogger())
			logger.info(msg);
	}

	public void warn(String msg) {
		if (ThreadVariable.isLogger())
			logger.warn(msg);
	}

	public void error(String msg) {
		logger.error(msg);
	}

	abstract void setValueFromFile(ArrayList<String> lineList) throws LogicException;

	abstract ArrayList<String> toFile();

}
