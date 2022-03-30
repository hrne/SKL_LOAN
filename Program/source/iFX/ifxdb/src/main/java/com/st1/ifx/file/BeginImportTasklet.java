package com.st1.ifx.file;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;

public class BeginImportTasklet implements Tasklet {
	private static final Logger logger = LoggerFactory.getLogger(BeginImportTasklet.class);

	@Override
	public RepeatStatus execute(StepContribution arg0, ChunkContext arg1) throws Exception {
		logger.info("\n\n begin import.....\n\n");
		return RepeatStatus.FINISHED;
	}

}
