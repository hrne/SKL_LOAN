package com.st1.ifx.batch.integration;

import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.job.flow.FlowExecutionStatus;
import org.springframework.batch.core.job.flow.JobExecutionDecider;

public class SkippedItemDecider implements JobExecutionDecider {

	@Override
	public FlowExecutionStatus decide(JobExecution jobExecution, StepExecution stepExeution) {
		if (!ExitStatus.FAILED.equals(jobExecution.getExitStatus()) && stepExeution.getSkipCount() > 0) {
			return new FlowExecutionStatus("COMPLETED WITH SKIPS");
		} else {
			return new FlowExecutionStatus(jobExecution.getExitStatus().toString());
		}
	}

}
