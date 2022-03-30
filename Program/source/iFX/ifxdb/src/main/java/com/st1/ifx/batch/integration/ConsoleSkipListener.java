package com.st1.ifx.batch.integration;

import org.springframework.batch.core.annotation.OnSkipInRead;
import org.springframework.batch.item.file.FlatFileParseException;

// spring batch in action page 257
public class ConsoleSkipListener {
	@OnSkipInRead
	public void log(Throwable t) {
		if (t instanceof FlatFileParseException) {
			FlatFileParseException ffpe = (FlatFileParseException) t;
			System.out.println("Line:" + ffpe.getLineNumber() + ":" + ffpe.getInput());
		}
	}
}
