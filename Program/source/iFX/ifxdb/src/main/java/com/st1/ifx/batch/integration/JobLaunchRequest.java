package com.st1.ifx.batch.integration;

import java.util.Collections;
import java.util.Map;

public class JobLaunchRequest {
	private String jobName;
	private Map<String, String> jobParameters;

	public JobLaunchRequest(String jobName) {
		this(jobName, Collections.EMPTY_MAP);
	}

	public JobLaunchRequest(String jobName, Map<String, String> jobParams) {
		super();
		this.jobName = jobName;
		this.jobParameters = jobParams;
	}

	public String getJobName() {
		return jobName;
	}

	@SuppressWarnings("unchecked")
	public Map<String, String> getJobParameters() {
		return jobParameters == null ? Collections.EMPTY_MAP : Collections.unmodifiableMap(jobParameters);
	}

	@Override
	public String toString() {
		return "JobLaunchRequest [jobName=" + jobName + ", jobParameters=" + jobParameters + "]";
	}

}
