package com.st1.itx.util.log;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.st1.itx.eum.ThreadVariable;
import com.st1.itx.util.format.FormatUtil;

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
			logger.info("[*" + FormatUtil.padX(ThreadVariable.getEmpNot(), 10) + "*]" + msg);
	}

	public void warn(String msg) {
		if (ThreadVariable.isLogger())
			logger.warn("[*" + FormatUtil.padX(ThreadVariable.getEmpNot(), 10) + "*]" + msg);
	}

	public void error(String msg) {
		logger.error("[*" + FormatUtil.padX(ThreadVariable.getEmpNot(), 10) + "*]" + msg);
	}

	public void mustInfo(String msg) {
		logger.info("[*" + FormatUtil.padX(ThreadVariable.getEmpNot(), 10) + "*]" + msg);
	}

}
