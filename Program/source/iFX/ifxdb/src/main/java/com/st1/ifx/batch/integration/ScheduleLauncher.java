package com.st1.ifx.batch.integration;

import java.util.Collections;
import java.util.Date;
import java.util.Map;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.context.support.GenericXmlApplicationContext;

public class ScheduleLauncher {
	private JobLauncher jobLauncher;
	private Job job;
	private Map<String, String> jobParameters;

	public void setJobLauncher(JobLauncher jobLauncher) {
		this.jobLauncher = jobLauncher;
	}

	public void setJob(Job job) {
		this.job = job;
	}

	public void setJobParameters(Map<String, String> jobParameters) {
		this.jobParameters = jobParameters;
	}

	public ScheduleLauncher(Job job) {
		this(job, Collections.EMPTY_MAP);
	}

	public ScheduleLauncher(Job job, Map<String, String> jobParams) {
		super();
		this.job = job;
		this.jobParameters = jobParams;
	}

	public void launch() throws Exception {
		JobParametersBuilder builder = new JobParametersBuilder();
		for (Map.Entry<String, String> entry : jobParameters.entrySet()) {
			builder.addString(entry.getKey(), entry.getValue());
		}
		builder.addString("timeStamp", new Date().toString());
		jobLauncher.run(job, builder.toJobParameters());
	}

}
