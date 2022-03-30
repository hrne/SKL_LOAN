package com.st1.ifx.swiftauto;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Component;

@Component
public class SwiftConverter {
	static final Logger logger = LoggerFactory.getLogger(SwiftConverter.class);

	@Autowired
	ApplicationContext applicationContext;

	// 傳進來的是檔案
	// @SuppressWarnings("rawtypes")
	// @ServiceActivator
	// public void convertFile(Message msg) throws Throwable {
	// logger.info("converting msg file:{}", msg);
	// File file = (File) msg.getPayload();
	// String t = FileUtils.readFileToString(file);
	// handleString(t);
	// logger.info("convert " + file.getAbsolutePath());
	// }

	// 傳進來的是字串
	@ServiceActivator
	public void convertString(Message<String> msg) throws Throwable {
		logger.info("converting msg string:{}", msg);
		String t = msg.getPayload();
		handleString(t);
	}

	private void handleString(String t) {
		TotaToPrn totaToPrn = applicationContext.getBean(TotaToPrn.class);
		totaToPrn.fromTota(t);
		totaToPrn.setLinePrefix("    ");
		String filePath = totaToPrn.generate();

		logger.debug("converted to " + filePath);
	}

}
