package com.st1.ifx.file.item.general;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.st1.ifx.service.CodeListService;

@Component
public class GeneralLineDone implements Tasklet {
	private static final Logger logger = LoggerFactory.getLogger(GeneralLineDone.class);

	private CodeListService codeListService;

	@Autowired
	public void setCodeListService(CodeListService codeListService) {
		this.codeListService = codeListService;
	}

	@Override
	public RepeatStatus execute(StepContribution arg0, ChunkContext arg1) throws Exception {
		// logger.info("evict code list");
		// this.codeListService.evict();
		// TODO move file?
		return RepeatStatus.FINISHED;
	}

}
