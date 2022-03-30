package com.st1.ifx.file.item.general;

import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Autowired;

import com.st1.ifx.service.CodeListService;

public class GeneralReportTasklet implements Tasklet {
	private CodeListService codeListService;

	@Autowired
	public void setCodeListService(CodeListService codeListService) {
		this.codeListService = codeListService;
	}

	@Override
	public RepeatStatus execute(StepContribution arg0, ChunkContext arg1) throws Exception {

		return RepeatStatus.FINISHED;
	}

}
