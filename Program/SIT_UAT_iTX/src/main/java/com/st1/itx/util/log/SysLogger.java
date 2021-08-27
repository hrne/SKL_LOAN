package com.st1.itx.util.log;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.st1.itx.eum.ThreadVariable;

public class SysLogger {
	private final Logger logger;

	public SysLogger() {
		Class<?> enclosingClass = getClass().getEnclosingClass();
		if (enclosingClass != null)
			logger = LoggerFactory.getLogger(enclosingClass.getName());
		else
			logger = LoggerFactory.getLogger(getClass().getName());

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
	
	public void mustInfo(String msg) {
		logger.info(msg);
	}
}
