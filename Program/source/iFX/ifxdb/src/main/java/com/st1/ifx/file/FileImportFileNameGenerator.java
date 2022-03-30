package com.st1.ifx.file;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.integration.file.FileNameGenerator;
import org.springframework.integration.support.MessageBuilder;
import org.springframework.messaging.Message;

public class FileImportFileNameGenerator implements FileNameGenerator {
	private static final Logger logger = LoggerFactory.getLogger(FileImportFileNameGenerator.class);

	public String generateFileName(Message<?> message) {
		// String payload = (String) message.getPayload();
		String importId = (String) message.getHeaders().get("importId");
		String prefix = (String) message.getHeaders().get("filePrefix");
		importId = importId.trim();
		return extractName(prefix, importId);
	}

	private String extractName(String prefix, String importId) {

		return prefix + "_" + importId + ".txt";
	}

	private static String getTimestamp() {
		DateFormat dateFormat = new SimpleDateFormat("yyyyMMdd-HHmmssS");
		return dateFormat.format(new Date());
	}

	public static void main(String[] args) {
		logger.info("###in:FileImportFileNameGenerator");
		FileImportFileNameGenerator g = new FileImportFileNameGenerator();
		String importId = "txcd-" + getTimestamp();
		Message<String> m = MessageBuilder.withPayload("abcdefghijklm1231232323232").setHeader("filePrefix", "web")
				.setHeader("importId", importId).build();
		String filename = g.generateFileName(m);
		logger.info("file::" + filename);

	}
}
