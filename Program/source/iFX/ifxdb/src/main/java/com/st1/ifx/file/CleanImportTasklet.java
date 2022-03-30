package com.st1.ifx.file;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;

public class CleanImportTasklet implements Tasklet {
	private static final Logger logger = LoggerFactory.getLogger(CleanImportTasklet.class);

	@Override
	public RepeatStatus execute(StepContribution arg0, ChunkContext arg1) throws Exception {
		logger.info("\n\n clean import.....\n\n");
		return RepeatStatus.FINISHED;
	}

}
