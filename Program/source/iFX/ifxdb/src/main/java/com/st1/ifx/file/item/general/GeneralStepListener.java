package com.st1.ifx.file.item.general;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.listener.StepExecutionListenerSupport;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.st1.ifx.etc.FileLog;
import com.st1.ifx.filter.FilterUtils;
import com.st1.ifx.service.CodeListService;
import com.st1.util.PoorManFile;
import com.st1.util.PoorManUtil;

@Component
public class GeneralStepListener extends StepExecutionListenerSupport {

	// 新增 Help.js的版號
	public static String helpjsVersionPath = null;
	public static String helpjsVersion = null;

	private static final Logger logger = LoggerFactory.getLogger(GeneralStepListener.class);

	private CodeListService codeListService;

	@Autowired
	public void setCodeListService(CodeListService codeListService) {
		this.codeListService = codeListService;
	}

	@Override
	public ExitStatus afterStep(StepExecution stepExecution) {
		if (stepExecution.getExitStatus().equals(ExitStatus.COMPLETED)) {
			logger.info("step completet: call evict()");
			this.codeListService.evict();
			addHelpjsVersion();
			String json = this.codeListService.buildJS_noCache(true);

			String reportFilePath = stepExecution.getJobExecution().getJobParameters().getString("reportFilePath");
			FileLog.write(reportFilePath, "HELP.js:\n" + json);
		}
		return stepExecution.getExitStatus();

	}

	// 更新 Help.js的版號
	public static void addHelpjsVersion() {
		BufferedReader br = null;
		String versionpath = System.getProperty("ifx_fxworkfile") + File.separator + "webServerEnv" + File.separator;
		helpjsVersionPath = versionpath + "helpjs-version.txt";
		logger.info("addHelpjsVersion:" + FilterUtils.escape(helpjsVersionPath));
		try {
			br = new BufferedReader(new FileReader(FilterUtils.filter(helpjsVersionPath)));
			helpjsVersion = br.readLine();
			logger.info("old helpjsVersion!" + helpjsVersion);
			PoorManFile poorFile = new PoorManFile(helpjsVersionPath);
			String[] helpver = helpjsVersion.trim().split("-");

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
				logger.error(errors.toString());
			}
		} catch (IOException e) {
			StringWriter errors = new StringWriter();
			e.printStackTrace(new PrintWriter(errors));
			logger.error(errors.toString());
			helpjsVersion = PoorManUtil.getNow();
		} finally {
			try {
				if (br != null)
					br.close();
			} catch (IOException e) {
				StringWriter errors = new StringWriter();
				e.printStackTrace(new PrintWriter(errors));
				logger.error(errors.toString());
			}
		}

		logger.info("helpjsVersion:[" + helpjsVersion + "]");
	}

}
