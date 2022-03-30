package com.st1.ifx.batch.integration;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.listener.ItemListenerSupport;

@SuppressWarnings("rawtypes")
public class ItemFailureLoggerListener extends ItemListenerSupport {
	private static Logger logger = LoggerFactory.getLogger("item.error");

	public void onReadError(Exception ex) {
		logger.error("Encountered error on read", ex);
	}

	public void onWriteError(Exception ex, Object item) {
		logger.error("Encountered error on write", ex);
	}
}