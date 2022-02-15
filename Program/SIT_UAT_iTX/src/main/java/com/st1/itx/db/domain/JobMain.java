package com.st1.itx.db.domain;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Time;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.EntityListeners;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import javax.persistence.EmbeddedId;
import javax.persistence.Column;
import com.st1.itx.util.StaticTool;
import com.st1.itx.Exception.LogicException;

/**
 * JobMain 批次工作主檔<br>
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "`JobMain`")
public class JobMain implements Serializable {


  /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

@EmbeddedId
  private JobMainId jobMainId;

  // 交易序號
  /* 交易序號 */
  @Column(name = "`TxSeq`", length = 20, insertable = false, updatable = false)
  private String txSeq;

  // 批次執行日期
  /* 執行日期 */
  @Column(name = "`ExecDate`", insertable = false, updatable = false)
  private int execDate = 0;

  // 批次代號
  /* 批次代號 */
  @Column(name = "`JobCode`", length = 10, insertable = false, updatable = false)
  private String jobCode;

  // 啟動時間
  /* 啟動時間 */
  @Column(name = "`StartTime`")
  private java.sql.Timestamp startTime;

  // 結束時間
  /* 結束時間 */
  @Column(name = "`EndTime`")
  private java.sql.Timestamp endTime;

  // 狀態記號
  /* U:執行中F:失敗S:成功 */
  @Column(name = "`Status`", length = 1)
  private String status;

  // 建檔人員
  @Column(name = "`CreateEmpNo`", length = 6)
  private String createEmpNo;

  // 建檔日期
  @CreatedDate
  @Column(name = "`CreateDate`")
  private java.sql.Timestamp createDate;

  // 最後維護人員
  @Column(name = "`LastUpdateEmpNo`", length = 6)
  private String lastUpdateEmpNo;

  // 最後維護日期
  @LastModifiedDate
  @Column(name = "`LastUpdate`")
  private java.sql.Timestamp lastUpdate;


  public JobMainId getJobMainId() {
    return this.jobMainId;
  }

  public void setJobMainId(JobMainId jobMainId) {
    this.jobMainId = jobMainId;
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

/**
	* 啟動時間<br>
	* 啟動時間
	* @return java.sql.Timestamp
	*/
  public java.sql.Timestamp getStartTime() {
    return this.startTime;
  }

/**
	* 啟動時間<br>
	* 啟動時間
  *
  * @param startTime 啟動時間
	*/
  public void setStartTime(java.sql.Timestamp startTime) {
    this.startTime = startTime;
  }

/**
	* 結束時間<br>
	* 結束時間
	* @return java.sql.Timestamp
	*/
  public java.sql.Timestamp getEndTime() {
    return this.endTime;
  }

/**
	* 結束時間<br>
	* 結束時間
  *
  * @param endTime 結束時間
	*/
  public void setEndTime(java.sql.Timestamp endTime) {
    this.endTime = endTime;
  }

/**
	* 狀態記號<br>
	* U:執行中
F:失敗
S:成功
	* @return String
	*/
  public String getStatus() {
    return this.status == null ? "" : this.status;
  }

/**
	* 狀態記號<br>
	* U:執行中
F:失敗
S:成功
  *
  * @param status 狀態記號
	*/
  public void setStatus(String status) {
    this.status = status;
  }

/**
	* 建檔人員<br>
	* 
	* @return String
	*/
  public String getCreateEmpNo() {
    return this.createEmpNo == null ? "" : this.createEmpNo;
  }

/**
	* 建檔人員<br>
	* 
  *
  * @param createEmpNo 建檔人員
	*/
  public void setCreateEmpNo(String createEmpNo) {
    this.createEmpNo = createEmpNo;
  }

/**
	* 建檔日期<br>
	* 
	* @return java.sql.Timestamp
	*/
  public java.sql.Timestamp getCreateDate() {
    return this.createDate;
  }

/**
	* 建檔日期<br>
	* 
  *
  * @param createDate 建檔日期
	*/
  public void setCreateDate(java.sql.Timestamp createDate) {
    this.createDate = createDate;
  }

/**
	* 最後維護人員<br>
	* 
	* @return String
	*/
  public String getLastUpdateEmpNo() {
    return this.lastUpdateEmpNo == null ? "" : this.lastUpdateEmpNo;
  }

/**
	* 最後維護人員<br>
	* 
  *
  * @param lastUpdateEmpNo 最後維護人員
	*/
  public void setLastUpdateEmpNo(String lastUpdateEmpNo) {
    this.lastUpdateEmpNo = lastUpdateEmpNo;
  }

/**
	* 最後維護日期<br>
	* 
	* @return java.sql.Timestamp
	*/
  public java.sql.Timestamp getLastUpdate() {
    return this.lastUpdate;
  }

/**
	* 最後維護日期<br>
	* 
  *
  * @param lastUpdate 最後維護日期
	*/
  public void setLastUpdate(java.sql.Timestamp lastUpdate) {
    this.lastUpdate = lastUpdate;
  }


  @Override
  public String toString() {
    return "JobMain [jobMainId=" + jobMainId + ", startTime=" + startTime + ", endTime=" + endTime + ", status=" + status
           + ", createEmpNo=" + createEmpNo + ", createDate=" + createDate + ", lastUpdateEmpNo=" + lastUpdateEmpNo + ", lastUpdate=" + lastUpdate + "]";
  }
}
