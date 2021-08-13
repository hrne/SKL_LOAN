package com.st1.itx.db.domain;

import java.io.Serializable;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 * JobDetail 批次工作明細檔<br>
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
@Embeddable
public class JobDetailId implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5817094027288050692L;

	// 批次執行日期
	/* 執行日期 */
	@Column(name = "`ExecDate`")
	private int execDate = 0;

	// 批次代號
	/* 批次代號 */
	@Column(name = "`JobCode`", length = 10)
	private String jobCode = " ";

	// StepId
	/* 步驟代號 */
	@Column(name = "`StepId`", length = 30)
	private String stepId = " ";

	public JobDetailId() {
	}

	public JobDetailId(int execDate, String jobCode, String stepId) {
		this.execDate = execDate;
		this.jobCode = jobCode;
		this.stepId = stepId;
	}

	/**
	 * 批次執行日期<br>
	 * 執行日期
	 * 
	 * @return Integer
	 */
	public int getExecDate() {
		return this.execDate;
	}

	/**
	 * 批次執行日期<br>
	 * 執行日期
	 *
	 * @param execDate 批次執行日期
	 */
	public void setExecDate(int execDate) {
		this.execDate = execDate;
	}

	/**
	 * 批次代號<br>
	 * 批次代號
	 * 
	 * @return String
	 */
	public String getJobCode() {
		return this.jobCode == null ? "" : this.jobCode;
	}

	/**
	 * 批次代號<br>
	 * 批次代號
	 *
	 * @param jobCode 批次代號
	 */
	public void setJobCode(String jobCode) {
		this.jobCode = jobCode;
	}

	/**
	 * StepId<br>
	 * 步驟代號
	 * 
	 * @return String
	 */
	public String getStepId() {
		return this.stepId == null ? "" : this.stepId;
	}

	/**
	 * StepId<br>
	 * 步驟代號
	 *
	 * @param stepId StepId
	 */
	public void setStepId(String stepId) {
		this.stepId = stepId;
	}

	@Override
	public int hashCode() {
		return Objects.hash(execDate, jobCode, stepId);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null || getClass() != obj.getClass())
			return false;
		JobDetailId jobDetailId = (JobDetailId) obj;
		return execDate == jobDetailId.execDate && jobCode.equals(jobDetailId.jobCode) && stepId.equals(jobDetailId.stepId);
	}

	@Override
	public String toString() {
		return "JobDetailId [execDate=" + execDate + ", jobCode=" + jobCode + ", stepId=" + stepId + "]";
	}
}
