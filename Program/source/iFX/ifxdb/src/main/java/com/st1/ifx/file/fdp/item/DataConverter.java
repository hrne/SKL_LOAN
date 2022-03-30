package com.st1.ifx.file.fdp.item;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Scope;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Component;

import com.st1.ifx.filter.FilterUtils;

@Component
@Scope("prototype")
public class DataConverter {
	private static final Logger logger = LoggerFactory.getLogger(DataConverter.class);

	@Value("${import.databox}")
	private String outputdataFolder;

	@Value("${import.copytoFolder}")
	private String copytoFolder;

	@Value("${import.backupFolder}")
	private String backupFolder;

	@Value("${import.convertDataToExt}")
	private String extName = ".data";

	@ServiceActivator
	public void convert(Message msg) throws Throwable {
		logger.info("converting msg:{}", msg);
		File file = (File) msg.getPayload();
		logger.info(FilterUtils.escape("convert " + file.getAbsolutePath() + " to " + outputdataFolder));

		copyFileToOutputFolder(file.getAbsolutePath());
		moveFileToBackupFolder(file.getAbsolutePath());

	}

	private void copyFileToOutputFolder(String filePath) {
		// errorFolder
		String fileName = FilenameUtils.getName(filePath);
		String newFilePath = FilenameUtils.concat(outputdataFolder, fileName);
		File f = new File(newFilePath);
		if (f.exists()) {
			logger.warn("convertTodata " + newFilePath + " exists, delete it");
			f.delete();
		}

		try {
			String copyto = FilenameUtils.concat(FilterUtils.filter(copytoFolder),
					FilenameUtils.getPath(FilterUtils.filter(newFilePath)));
			logger.info("copyto " + copyto);
			File filePathtp = new File(FilterUtils.filter(filePath));
			FileUtils.copyFileToDirectory(filePathtp, new File(FilterUtils.filter(outputdataFolder)), true);
			File copytotmp = new File(FilterUtils.filter(copyto));
			FileUtils.copyFileToDirectory(filePathtp, copytotmp, true);
			copytotmp.setExecutable(true, false);
			copytotmp.setWritable(true, false);
			copytotmp.setReadable(true, false);
		} catch (IOException e) {
			logger.error("convertTodata copy file", e);
		}

		logger.info("convertTodata " + fileName + " is copy to " + newFilePath);
	}

	private void moveFileToBackupFolder(String filePath) {
		// errorFolder
		String fileName = FilenameUtils.getName(filePath);
		String newFilePath = FilenameUtils.concat(backupFolder, fileName);
		File f = new File(newFilePath);
		if (f.exists()) {
			logger.warn("convertTodata " + newFilePath + " exists, delete it");
			f.delete();
		}

		try {
			FileUtils.moveFileToDirectory(new File(FilterUtils.filter(filePath)),
					new File(FilterUtils.filter(backupFolder)), true);
		} catch (IOException e) {
			logger.error("convertTodata move file", e);
		}

		logger.info("convertTodata " + fileName + " is moved to " + newFilePath);
	}
}
