package com.st1.ifx.file;

import java.io.File;
import java.io.PrintWriter;
import java.io.StringWriter;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;

public class FileMoveTasklet implements Tasklet {
	private static final Logger logger = LoggerFactory.getLogger(FileMoveTasklet.class);

	private Resource sourceFile;
	private Resource targetFolder;

	public void setSourceFile(Resource sourceFile) {
		this.sourceFile = sourceFile;
	}

	public void setTargetFolder(Resource targetFolder) {
		this.targetFolder = targetFolder;
	}

	@Override
	public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
		String filename = FilenameUtils.concat(targetFolder.getFile().getAbsolutePath(), sourceFile.getFilename());

		File f = new File(filename);
		logger.info("###filename:" + filename);
		if (f.exists()) {
			logger.warn(filename + " exists, delete it");
			f.delete();
		}

		FileUtils.moveFileToDirectory(sourceFile.getFile(), targetFolder.getFile(), true);

		logger.info(sourceFile.getFilename() + " is moved to " + targetFolder.getFile().getAbsolutePath());
		return RepeatStatus.FINISHED;
	}

	public static void main(String[] args) {
		logger.info("###FileMoveTasklet");

		FileMoveTasklet m = new FileMoveTasklet();

		Resource source = new FileSystemResource("D:/temp/import-folder/host_echo-20.txt");
		Resource target = new FileSystemResource("D:/temp/import-bak");
		m.setSourceFile(source);
		m.setTargetFolder(target);
		logger.info("###source:" + source + ",target:" + target);
		try {
			m.execute(null, null);
		} catch (Exception e) {
			StringWriter errors = new StringWriter();
			e.printStackTrace(new PrintWriter(errors));
			logger.error(errors.toString());
		}
	}
}
