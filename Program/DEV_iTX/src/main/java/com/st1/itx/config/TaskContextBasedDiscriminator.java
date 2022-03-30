package com.st1.itx.config;

import com.st1.itx.eum.ThreadVariable;

import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.sift.AbstractDiscriminator;

public class TaskContextBasedDiscriminator extends AbstractDiscriminator<ILoggingEvent> {
	private static String LOG_FILE_KEY = "userId";
	private static String DEFAULT_VALUE = "SysTem";

	public String getKey() {
		return LOG_FILE_KEY;
	}

	public String getDiscriminatingValue(ILoggingEvent iLoggingEvent) {
		String userId = ThreadVariable.getEmpNot();
		if (userId.isEmpty())
			return DEFAULT_VALUE;
		else
			return userId;
	}
}