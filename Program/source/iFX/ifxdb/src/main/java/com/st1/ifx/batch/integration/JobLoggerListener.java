package com.st1.ifx.batch.integration;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.annotation.AfterJob;
import org.springframework.batch.core.annotation.BeforeJob;

import com.st1.ifx.etc.FileLog;
import com.st1.util.PoorManFile;
import com.st1.util.PoorManUtil;

public class JobLoggerListener {
	private static final Logger logger = LoggerFactory.getLogger(JobLoggerListener.class);

	@BeforeJob
	public void beforeJob(JobExecution jobExecution) {
		logger.info(jobExecution.getJobInstance().getJobName() + " is beginning execution");
	}

	@AfterJob
	public void afterJob(JobExecution jobExecution) {
		logger.info("!afterJob");
		// TODO log to file, move input file to another direcoty
		String reportFilePath = jobExecution.getJobParameters().getString("reportFilePath");
		String t = jobExecution.getJobInstance().getJobName() + " has completed with the status "
				+ jobExecution.getStatus();
		FileLog.write(reportFilePath, t);
		logger.info(t);
		if (jobExecution.getStatus() == BatchStatus.COMPLETED) {
			// job success
			String helpjsVersionPath = jobExecution.getJobParameters().getString("helpjsVersionPath");
			logger.info("ifx-db helpjs Version Path:" + helpjsVersionPath);
			updateHelpjsVersion(helpjsVersionPath);
		} else if (jobExecution.getStatus() == BatchStatus.FAILED) {
			// job failure
			List<Throwable> list = jobExecution.getAllFailureExceptions();
			for (Throwable th : list) {
				logger.error("after job:" + th.getMessage());
				FileLog.write(reportFilePath, th.getMessage());
			}
		}

	}

	// 新增 Help.js的版號
	public void updateHelpjsVersion(String helpjsversionpath) {
		BufferedReader br = null;
		try {
			br = new BufferedReader(new FileReader(helpjsversionpath));
			String helpjsVersion = br.readLine();
			logger.info("old helpjsVersion!" + helpjsVersion);
			PoorManFile poorFile = new PoorManFile(helpjsversionpath);
			String[] helpver = helpjsVersion.split("-");
			int helpverint = Integer.parseInt(helpver[1]) + 1;
			String todayString = PoorManUtil.getToday();
			logger.info("today String!" + todayString);
			try {
				if (!todayString.equals(helpver[0])) {
					helpjsVersion = todayString + "-1";
				} else {
					helpjsVersion = helpver[0] + "-" + helpverint;
				}
				poorFile.write(helpjsVersion);
				logger.info("new helpjsVersion!:[" + helpjsVersion + "]");

			} catch (IOException e) {
				StringWriter errors = new StringWriter();
				e.printStackTrace(new PrintWriter(errors));
				logger.warn(errors.toString());
			}
		} catch (IOException e) {
			StringWriter errors = new StringWriter();
			e.printStackTrace(new PrintWriter(errors));
			logger.warn(errors.toString());
		} finally {
			try {
				if (br != null)
					br.close();
			} catch (IOException e) {
				StringWriter errors = new StringWriter();
				e.printStackTrace(new PrintWriter(errors));
				logger.warn(errors.toString());
			}
		}
	}
}
