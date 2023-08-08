package com.st1.itx.db.domain;

import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.EntityListeners;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import javax.persistence.EmbeddedId;
import javax.persistence.Column;

/**
 * JobDetail 批次工作明細檔<br>
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "`JobDetail`")
public class JobDetail implements Serializable {


  /**
	 * 
	 */
	private static final long serialVersionUID = -5013391170287566126L;

@EmbeddedId
  private JobDetailId jobDetailId;

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

  // StepId
  /* 步驟代號 */
  @Column(name = "`StepId`", length = 30, insertable = false, updatable = false)
  private String stepId;

  // 類別
  /* D:日批M:月批 */
  @Column(name = "`BatchType`", length = 1)
  private String batchType;

  // 執行結果
  /* S:成功F:失敗 */
  @Column(name = "`Status`", length = 1)
  private String status;

  // 錯誤碼
  /* 五碼:一般交易錯誤DB000:DB異常LG000:邏輯錯誤 */
  @Column(name = "`ErrCode`", length = 15)
  private String errCode;

  // 錯誤內容
  /* 除了一般錯誤,其他皆會儲存Exception內容 */
  @Column(name = "`ErrContent`", length = 3000)
  private String errContent;

  // 啟動時間
  /* 啟動時間 */
  @Column(name = "`StepStartTime`")
  private java.sql.Timestamp stepStartTime;

  // 結束時間
  /* 結束時間 */
  @Column(name = "`StepEndTime`")
  private java.sql.Timestamp stepEndTime;

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

  // 子批次代號
  /* 子批次代號 */
  @Column(name = "`NestJobCode`", length = 30)
  private String nestJobCode;


  public JobDetailId getJobDetailId() {
    return this.jobDetailId;
  }

  public void setJobDetailId(JobDetailId jobDetailId) {
    this.jobDetailId = jobDetailId;
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
	* StepId<br>
	* 步驟代號
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

/**
	* 類別<br>
	* D:日批
M:月批
	* @return String
	*/
  public String getBatchType() {
    return this.batchType == null ? "" : this.batchType;
  }

/**
	* 類別<br>
	* D:日批
M:月批
  *
  * @param batchType 類別
	*/
  public void setBatchType(String batchType) {
    this.batchType = batchType;
  }

/**
	* 執行結果<br>
	* S:成功
F:失敗
	* @return String
	*/
  public String getStatus() {
    return this.status == null ? "" : this.status;
  }

/**
	* 執行結果<br>
	* S:成功
F:失敗
  *
  * @param status 執行結果
	*/
  public void setStatus(String status) {
    this.status = status;
  }

/**
	* 錯誤碼<br>
	* 五碼:一般交易錯誤
DB000:DB異常
LG000:邏輯錯誤
	* @return String
	*/
  public String getErrCode() {
    return this.errCode == null ? "" : this.errCode;
  }

/**
	* 錯誤碼<br>
	* 五碼:一般交易錯誤
DB000:DB異常
LG000:邏輯錯誤
  *
  * @param errCode 錯誤碼
	*/
  public void setErrCode(String errCode) {
    this.errCode = errCode;
  }

/**
	* 錯誤內容<br>
	* 除了一般錯誤,其他皆會儲存Exception內容
	* @return String
	*/
  public String getErrContent() {
    return this.errContent == null ? "" : this.errContent;
  }

/**
	* 錯誤內容<br>
	* 除了一般錯誤,其他皆會儲存Exception內容
  *
  * @param errContent 錯誤內容
	*/
  public void setErrContent(String errContent) {
    this.errContent = errContent;
  }

/**
	* 啟動時間<br>
	* 啟動時間
	* @return java.sql.Timestamp
	*/
  public java.sql.Timestamp getStepStartTime() {
    return this.stepStartTime;
  }

/**
	* 啟動時間<br>
	* 啟動時間
  *
  * @param stepStartTime 啟動時間
	*/
  public void setStepStartTime(java.sql.Timestamp stepStartTime) {
    this.stepStartTime = stepStartTime;
  }

/**
	* 結束時間<br>
	* 結束時間
	* @return java.sql.Timestamp
	*/
  public java.sql.Timestamp getStepEndTime() {
    return this.stepEndTime;
  }

/**
	* 結束時間<br>
	* 結束時間
  *
  * @param stepEndTime 結束時間
	*/
  public void setStepEndTime(java.sql.Timestamp stepEndTime) {
    this.stepEndTime = stepEndTime;
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

/**
	* 子批次代號<br>
	* 子批次代號
	* @return String
	*/
  public String getNestJobCode() {
    return this.nestJobCode == null ? "" : this.nestJobCode;
  }

/**
	* 子批次代號<br>
	* 子批次代號
  *
  * @param nestJobCode 子批次代號
	*/
  public void setNestJobCode(String nestJobCode) {
    this.nestJobCode = nestJobCode;
  }


  @Override
  public String toString() {
    return "JobDetail [jobDetailId=" + jobDetailId + ", batchType=" + batchType + ", status=" + status
           + ", errCode=" + errCode + ", errContent=" + errContent + ", stepStartTime=" + stepStartTime + ", stepEndTime=" + stepEndTime + ", createEmpNo=" + createEmpNo + ", createDate=" + createDate
           + ", lastUpdateEmpNo=" + lastUpdateEmpNo + ", lastUpdate=" + lastUpdate + ", nestJobCode=" + nestJobCode + "]";
  }
}
