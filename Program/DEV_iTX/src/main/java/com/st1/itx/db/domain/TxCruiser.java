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
 * TxCruiser 批次發動交易紀錄<br>
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "`TxCruiser`")
public class TxCruiser implements Serializable {


  /**
	 * 
	 */
	private static final long serialVersionUID = 8238661350850279997L;

@EmbeddedId
  private TxCruiserId txCruiserId;

  // 交易序號
  @Column(name = "`TxSeq`", length = 20, insertable = false, updatable = false)
  private String txSeq;

  // 發動經辦
  @Column(name = "`TlrNo`", length = 6, insertable = false, updatable = false)
  private String tlrNo;

  // 發動交易
  @Column(name = "`TxCode`", length = 10)
  private String txCode;

  // 批次執行清單
  @Column(name = "`JobList`", length = 800)
  private String jobList;

  // 參數
  @Column(name = "`Parameter`", length = 3000)
  private String parameter;

  // 執行狀態
  /* U:執行中S:完成F:失敗 */
  @Column(name = "`Status`", length = 1)
  private String status;

  // 關閉訊息通知交易記號
  /* 1:無連動交易 */
  @Column(name = "`SendMSgChainOff`", length = 1)
  private String sendMSgChainOff;

  // 建檔日期時間
  @CreatedDate
  @Column(name = "`CreateDate`")
  private java.sql.Timestamp createDate;

  // 建檔人員
  @Column(name = "`CreateEmpNo`", length = 6)
  private String createEmpNo;

  // 最後更新日期時間
  @LastModifiedDate
  @Column(name = "`LastUpdate`")
  private java.sql.Timestamp lastUpdate;

  // 最後更新人員
  @Column(name = "`LastUpdateEmpNo`", length = 6)
  private String lastUpdateEmpNo;


  public TxCruiserId getTxCruiserId() {
    return this.txCruiserId;
  }

  public void setTxCruiserId(TxCruiserId txCruiserId) {
    this.txCruiserId = txCruiserId;
  }

/**
	* 交易序號<br>
	* 
	* @return String
	*/
  public String getTxSeq() {
    return this.txSeq == null ? "" : this.txSeq;
  }

/**
	* 交易序號<br>
	* 
  *
  * @param txSeq 交易序號
	*/
  public void setTxSeq(String txSeq) {
    this.txSeq = txSeq;
  }

/**
	* 發動經辦<br>
	* 
	* @return String
	*/
  public String getTlrNo() {
    return this.tlrNo == null ? "" : this.tlrNo;
  }

/**
	* 發動經辦<br>
	* 
  *
  * @param tlrNo 發動經辦
	*/
  public void setTlrNo(String tlrNo) {
    this.tlrNo = tlrNo;
  }

/**
	* 發動交易<br>
	* 
	* @return String
	*/
  public String getTxCode() {
    return this.txCode == null ? "" : this.txCode;
  }

/**
	* 發動交易<br>
	* 
  *
  * @param txCode 發動交易
	*/
  public void setTxCode(String txCode) {
    this.txCode = txCode;
  }

/**
	* 批次執行清單<br>
	* 
	* @return String
	*/
  public String getJobList() {
    return this.jobList == null ? "" : this.jobList;
  }

/**
	* 批次執行清單<br>
	* 
  *
  * @param jobList 批次執行清單
	*/
  public void setJobList(String jobList) {
    this.jobList = jobList;
  }

/**
	* 參數<br>
	* 
	* @return String
	*/
  public String getParameter() {
    return this.parameter == null ? "" : this.parameter;
  }

/**
	* 參數<br>
	* 
  *
  * @param parameter 參數
	*/
  public void setParameter(String parameter) {
    this.parameter = parameter;
  }

/**
	* 執行狀態<br>
	* U:執行中
S:完成
F:失敗
	* @return String
	*/
  public String getStatus() {
    return this.status == null ? "" : this.status;
  }

/**
	* 執行狀態<br>
	* U:執行中
S:完成
F:失敗
  *
  * @param status 執行狀態
	*/
  public void setStatus(String status) {
    this.status = status;
  }

/**
	* 關閉訊息通知交易記號<br>
	* 1:無連動交易
	* @return String
	*/
  public String getSendMSgChainOff() {
    return this.sendMSgChainOff == null ? "" : this.sendMSgChainOff;
  }

/**
	* 關閉訊息通知交易記號<br>
	* 1:無連動交易
  *
  * @param sendMSgChainOff 關閉訊息通知交易記號
	*/
  public void setSendMSgChainOff(String sendMSgChainOff) {
    this.sendMSgChainOff = sendMSgChainOff;
  }

/**
	* 建檔日期時間<br>
	* 
	* @return java.sql.Timestamp
	*/
  public java.sql.Timestamp getCreateDate() {
    return this.createDate;
  }

/**
	* 建檔日期時間<br>
	* 
  *
  * @param createDate 建檔日期時間
	*/
  public void setCreateDate(java.sql.Timestamp createDate) {
    this.createDate = createDate;
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
	* 最後更新日期時間<br>
	* 
	* @return java.sql.Timestamp
	*/
  public java.sql.Timestamp getLastUpdate() {
    return this.lastUpdate;
  }

/**
	* 最後更新日期時間<br>
	* 
  *
  * @param lastUpdate 最後更新日期時間
	*/
  public void setLastUpdate(java.sql.Timestamp lastUpdate) {
    this.lastUpdate = lastUpdate;
  }

/**
	* 最後更新人員<br>
	* 
	* @return String
	*/
  public String getLastUpdateEmpNo() {
    return this.lastUpdateEmpNo == null ? "" : this.lastUpdateEmpNo;
  }

/**
	* 最後更新人員<br>
	* 
  *
  * @param lastUpdateEmpNo 最後更新人員
	*/
  public void setLastUpdateEmpNo(String lastUpdateEmpNo) {
    this.lastUpdateEmpNo = lastUpdateEmpNo;
  }


  @Override
  public String toString() {
    return "TxCruiser [txCruiserId=" + txCruiserId + ", txCode=" + txCode + ", jobList=" + jobList + ", parameter=" + parameter + ", status=" + status
           + ", sendMSgChainOff=" + sendMSgChainOff + ", createDate=" + createDate + ", createEmpNo=" + createEmpNo + ", lastUpdate=" + lastUpdate + ", lastUpdateEmpNo=" + lastUpdateEmpNo + "]";
  }
}
