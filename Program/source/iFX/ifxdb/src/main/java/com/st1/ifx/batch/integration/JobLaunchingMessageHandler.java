package com.st1.ifx.batch.integration;

import java.io.File;
import java.io.IOException;
import java.util.Map;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.configuration.JobRegistry;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.launch.NoSuchJobException;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.integration.annotation.ServiceActivator;

import com.st1.ifx.etc.FileLog;
import com.st1.ifx.filter.FilterUtils;

public class JobLaunchingMessageHandler {
	private static final Logger logger = LoggerFactory.getLogger(JobLaunchingMessageHandler.class);
	private JobRegistry jobRegistry;
	private JobLauncher jobLauncher;

	public JobLaunchingMessageHandler(JobRegistry jobRegistry, JobLauncher jobLauncher) {
		super();
		this.jobRegistry = jobRegistry;
		this.jobLauncher = jobLauncher;
	}

	private @Value("${import.errorFolder}") String errorFolder;

	@ServiceActivator
	public String launch(JobLaunchRequest request) throws NoSuchJobException, JobExecutionAlreadyRunningException,
			JobRestartException, JobInstanceAlreadyCompleteException, JobParametersInvalidException {
		logger.info("!JobLaunchingMessageHandler launch");
		Job job = jobRegistry.getJob(request.getJobName());
		logger.info("!Job:" + job);
		JobParametersBuilder builder = new JobParametersBuilder();
		for (Map.Entry<String, String> entry : request.getJobParameters().entrySet()) {
			builder.addString(entry.getKey(), entry.getValue());
		}
		String reportFile = request.getJobParameters().get("reportFilePath");
		String inputFile = request.getJobParameters().get("inputFile");
		String jobName = request.getJobName();
		try {
			logger.info("@ in Job:" + reportFile, "Starting Job:" + jobName + ", file:" + inputFile);
			FileLog.write(reportFile, "Starting Job:" + jobName + ", file:" + inputFile);
			logger.info("@ after FileLog.write");
			JobExecution r = jobLauncher.run(job, builder.toJobParameters());
			logger.info("@ after JobExecution");
			return "Job " + request.getJobName() + " Completed:" + r.toString();
		} catch (JobExecutionAlreadyRunningException ex) {
			String t = "Job " + jobName + " Already Running:" + ex.getMessage();
			FileLog.write(reportFile, t);
			logger.error("JobExecutionAlreadyRunningException :" + t);
			moveFileToErrorFolder(inputFile);
			return t;
		} catch (JobRestartException ex) {
			String t = "Job " + jobName + " Restart Error:" + ex.getMessage();
			FileLog.write(reportFile, t);
			logger.error("JobRestartException :" + t);
			moveFileToErrorFolder(inputFile);
			return t;
		} catch (Exception ex) {
			String t = "Job " + jobName + " Exception Error:" + ex.getMessage();
			FileLog.write(reportFile, t);
			logger.error("Exception :" + t);
			moveFileToErrorFolder(inputFile);
			return t;
		}
	}

	private void moveFileToErrorFolder(String filePath) {
		// errorFolder
		String fileName = FilenameUtils.getName(filePath);
		String newFilePath = FilenameUtils.concat(errorFolder, fileName);
		File f = new File(newFilePath);
		if (f.exists()) {
			logger.warn(newFilePath + " exists, delete it");
			f.delete();
		}

		try {
			FileUtils.moveFileToDirectory(new File(filePath), new File(FilterUtils.filter(errorFolder)), true);
		} catch (IOException e) {
			logger.error("move file", e);
		}

		logger.info(fileName + " is moved to " + newFilePath);
	}

}
