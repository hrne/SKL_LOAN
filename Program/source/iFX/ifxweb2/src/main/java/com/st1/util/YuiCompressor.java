package com.st1.util;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.zip.GZIPOutputStream;

import org.mozilla.javascript.ErrorReporter;
import org.mozilla.javascript.EvaluatorException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class YuiCompressor {
	private static final Logger logger = LoggerFactory.getLogger(YuiCompressor.class);

	private static class YuiCompressorErrorReporter implements ErrorReporter {
		public void warning(String message, String sourceName, int line, String lineSource, int lineOffset) {
			if (line < 0) {
				logger.warn(message);
			} else {
				logger.warn(line + ':' + lineOffset + ':' + message);
			}
		}

		public void error(String message, String sourceName, int line, String lineSource, int lineOffset) {
			if (line < 0) {
				logger.error(message);
			} else {
				logger.error(line + ':' + lineOffset + ':' + message);
			}
		}

		public EvaluatorException runtimeError(String message, String sourceName, int line, String lineSource, int lineOffset) {
			error(message, sourceName, line, lineSource, lineOffset);
			return new EvaluatorException(message);
		}
	}

	public static class Options {
		public String charset = "UTF-8";
		public int lineBreakPos = -1;
		public boolean munge = true;
		public boolean verbose = false;
		public boolean preserveAllSemiColons = false;
		public boolean disableOptimizations = false;
	}

	public static void compressJavaScript(String inputFilename, String outputFilename, Options o) throws IOException {
		logger.warn("compressJavaScript no excute, becouse this function is blank...apan");
	}

	static void gzip(String content, String fileName) throws Exception {
		FileOutputStream output = new FileOutputStream(fileName);
		try {
			Writer writer = new OutputStreamWriter(new GZIPOutputStream(output), "UTF-8");
			try {
				writer.write(content);
			} finally {
				writer.close();
			}
		} finally {
			output.close();
		}
	}

}
