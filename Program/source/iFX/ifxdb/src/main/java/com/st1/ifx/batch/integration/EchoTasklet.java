package com.st1.ifx.batch.integration;

import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;

public class EchoTasklet implements Tasklet {

	@Override
	public RepeatStatus execute(StepContribution contribution, ChunkContext chunkConttext) throws Exception {
		System.out.println("\n\nhello\n\n\n");
		System.out.println(chunkConttext.getStepContext().getJobParameters());
		return RepeatStatus.FINISHED;
	}

}
