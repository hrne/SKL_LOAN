package com.st1.ifx.file;

import java.io.File;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import org.apache.commons.io.FilenameUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.stereotype.Component;

import com.st1.ifx.batch.integration.JobLaunchRequest;
import com.st1.ifx.filter.FilterUtils;

@Component
public class FileImportToJobLaunchRequestHandler {
	private static final Logger logger = LoggerFactory.getLogger(FileImportToJobLaunchRequestHandler.class);
	private @Value("${import.report}") String reportFolder;

	private @Value("${import.versionpath}") String helpjsVersionPath;

	@ServiceActivator
	public JobLaunchRequest adapt(File file) {
		// filename : source_importId.txt
		// web_txcd-20120303-22.txt
		// importId : table id + "-" + timestamp
		logger.info("###FileImportToJobLaunchRequestHandler...adapt");
		Map<String, String> jobParams = new HashMap<String, String>();
		String fileExt = FilenameUtils.getExtension(file.getAbsolutePath());
		String fileName = FilenameUtils.getBaseName(file.getAbsolutePath());

		String[] ss = fileName.split("_");
		String original = ss[0];
		String importId = ss[1];

		String jobName;
		logger.info("###File_!adapt-original:::" + original + ",importId:" + importId + ",fileExt:" + fileExt.toLowerCase() + ",fileName:" + fileName);
		if (fileExt.toLowerCase(Locale.TAIWAN).equals("t1")) {
			jobName = "import-general";
			jobParams.put("help", (ss[1].split("-")[0]).toUpperCase());
		} else if (fileExt.toLowerCase(Locale.TAIWAN).equals("t2")) {
			jobName = "import-rim";
		} else {
			jobName = "import-" + (ss[1].split("-")[0]).toLowerCase();
		}
		String reportFilePath = FilenameUtils.concat(reportFolder, fileName + ".txt");
		jobParams.put("original", original);
		jobParams.put("importId", importId);
		jobParams.put("reportFilePath", reportFilePath);
		jobParams.put("helpjsVersionPath", helpjsVersionPath);
		jobParams.put("inputFile", file.getAbsolutePath());

		return new JobLaunchRequest(jobName, jobParams);
	}

	public static void main(String[] args) {
		logger.info("###in:FileImportToJobLaunchRequestHandler");
		File file = new File("d:/temp/import-folder/web_TXCD-222.txt");
		FileImportToJobLaunchRequestHandler hnd = new FileImportToJobLaunchRequestHandler();
		JobLaunchRequest req = hnd.adapt(file);

		logger.info("file:::" + FilterUtils.escape(req.toString()));
	}
}
