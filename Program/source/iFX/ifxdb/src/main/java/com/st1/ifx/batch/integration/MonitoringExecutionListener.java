package com.st1.ifx.batch.integration;

import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.annotation.AfterJob;
import org.springframework.batch.core.annotation.BeforeJob;

public class MonitoringExecutionListener {
	private BatchMonitorNotifier notifier;

	public void setNotifier(BatchMonitorNotifier notifier) {
		this.notifier = notifier;
	}

	@BeforeJob
	public void executeBeforeJob(JobExecution jobExecution) {
		// Do nothing
	}

	@AfterJob
	public void executeAfterJob(JobExecution jobExecution) {
		if (jobExecution.getStatus() == BatchStatus.FAILED) {
			notifier.notify(jobExecution);
		}
	}
}
