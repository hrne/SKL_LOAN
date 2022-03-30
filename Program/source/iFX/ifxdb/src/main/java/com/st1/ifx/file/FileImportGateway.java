package com.st1.ifx.file;

import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;

public interface FileImportGateway {
	void importContent(@Payload String content, @Header("importId") String importId,
			@Header("filePrefix") String prefix);
}
