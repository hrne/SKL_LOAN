package com.st1.ifx.service;

import com.st1.ifx.file.FileImport;

public interface FileImportService {
	void createFileImport(String original, String importId) throws Exception;

	void mapImporttoJobInstance(String original, String importId, Long jobInstanceId);

	FileImport get(String original, String importId);
}
