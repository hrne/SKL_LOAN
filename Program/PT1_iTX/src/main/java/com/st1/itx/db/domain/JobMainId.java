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
	private static final long serialVersionUID = -2821264237950133459L;

// 交易序號
  /* 交易序號 */
  @Column(name = "`TxSeq`", length = 20)
  private String txSeq = " ";

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

  public JobMainId(String txSeq, int execDate, String jobCode) {
    this.txSeq = txSeq;
    this.execDate = execDate;
    this.jobCode = jobCode;
  }

/**
	* 交易序號<br>
	* 交易序號
	* @return String
	*/
  public String getTxSeq() {
    return this.txSeq == null ? "" : this.txSeq;
  }

/**
	* 交易序號<br>
	* 交易序號
  *
  * @param txSeq 交易序號
	*/
  public void setTxSeq(String txSeq) {
    this.txSeq = txSeq;
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
    return Objects.hash(txSeq, execDate, jobCode);
  }

  @Override
  public boolean equals(Object obj) {
    if(this == obj)
      return true;
    if(obj == null || getClass() != obj.getClass())
      return false;
    JobMainId jobMainId = (JobMainId) obj;
    return txSeq.equals(jobMainId.txSeq) && execDate == jobMainId.execDate && jobCode.equals(jobMainId.jobCode);
  }

  @Override
  public String toString() {
    return "JobMainId [txSeq=" + txSeq + ", execDate=" + execDate + ", jobCode=" + jobCode + "]";
  }
}
