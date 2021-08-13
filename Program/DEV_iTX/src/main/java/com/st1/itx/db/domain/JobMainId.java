package com.st1.itx.db.domain;

import java.io.Serializable;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 * JobMain 批次工作主檔<br>
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
@Embeddable
public class JobMainId implements Serializable {


  /**
	 * 
	 */
	private static final long serialVersionUID = 5042747557821051943L;

// 批次執行日期
  /* 執行日期 */
  @Column(name = "`ExecDate`")
  private int execDate = 0;

  // 批次代號
  /* 批次代號 */
  @Column(name = "`JobCode`", length = 10)
  private String jobCode = " ";

  public JobMainId() {
  }

  public JobMainId(int execDate, String jobCode) {
    this.execDate = execDate;
    this.jobCode = jobCode;
  }

/**
	* 批次執行日期<br>
	* 執行日期
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


  @Override
  public int hashCode() {
    return Objects.hash(execDate, jobCode);
  }

  @Override
  public boolean equals(Object obj) {
    if(this == obj)
      return true;
    if(obj == null || getClass() != obj.getClass())
      return false;
    JobMainId jobMainId = (JobMainId) obj;
    return execDate == jobMainId.execDate && jobCode.equals(jobMainId.jobCode);
  }

  @Override
  public String toString() {
    return "JobMainId [execDate=" + execDate + ", jobCode=" + jobCode + "]";
  }
}
