package com.st1.ifx.filter;

import java.io.Closeable;
import java.io.PrintWriter;
import java.io.StringWriter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SafeClose {
	static final Logger logger = LoggerFactory.getLogger(SafeClose.class);

	/**
	 * 安全關閉IO
	 * 
	 * @param io
	 */
	public static void close(Closeable io) {
		if (io != null) {
			try {
				io.close();
			} catch (Throwable t) {
				StringWriter errors = new StringWriter();
				t.printStackTrace(new PrintWriter(errors));
				logger.warn(errors.toString());
			}
		}
	}
}
