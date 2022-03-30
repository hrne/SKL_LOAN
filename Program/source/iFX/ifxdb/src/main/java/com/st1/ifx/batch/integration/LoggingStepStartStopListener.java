package com.st1.ifx.batch.integration;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.annotation.AfterStep;
import org.springframework.batch.core.annotation.BeforeStep;

public class LoggingStepStartStopListener {
	private static final Logger logger = LoggerFactory.getLogger(LoggingStepStartStopListener.class);

	@BeforeStep
	public void beforeStep(StepExecution execution) {
		logger.info(execution.getStepName() + " has begun!");
	}

	@AfterStep
	public ExitStatus afterStep(StepExecution execution) {
		logger.info(execution.getStepName() + " has ended!");

		return execution.getExitStatus();
	}
}
