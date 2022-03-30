package com.st1.ifx.batch.integration;

import org.springframework.batch.core.JobExecution;

public interface BatchMonitorNotifier {
	void notify(JobExecution jobExecution);
}
