package com.st1.ifx.file;

import java.util.Set;

import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.InitializingBean;
import org.junit.Assert;

import com.st1.ifx.service.FileImportService;

public class ImportToJobInstanceMappingTasklet implements Tasklet, InitializingBean {

	private String original;
	private String importId;
	private FileImportService fileImportService;
	private Set<String> directSources;

	public void setOriginal(String original) {
		this.original = original;
	}

	public void setImportId(String importId) {
		this.importId = importId;
	}

	public void setFileImportService(FileImportService fileImportService) {
		this.fileImportService = fileImportService;
	}

	public void setDirectSources(Set<String> directSources) {
		this.directSources = directSources;
	}

	@Override
	public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {

		Long jobInstanceId = chunkContext.getStepContext().getStepExecution().getJobExecution().getJobInstance()
				.getId();

		if (directSources != null && directSources.contains(original.toLowerCase())) {
			fileImportService.createFileImport(original, importId);
		}
		fileImportService.mapImporttoJobInstance(original, importId, jobInstanceId);

		return RepeatStatus.FINISHED;
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		Assert.assertNotNull(original, importId);
	}

}
