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
import com.st1.itx.util.StaticTool;
import com.st1.itx.Exception.LogicException;

/**
 * TxAgent 代理人檔<br>
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "`TxAgent`")
public class TxAgent implements Serializable {


  /**
	 * 
	 */
	private static final long serialVersionUID = 2110033698703828146L;

@EmbeddedId
  private TxAgentId txAgentId;

  // 櫃員
  @Column(name = "`TlrNo`", length = 6, insertable = false, updatable = false)
  private String tlrNo;

  // 代理櫃員
  @Column(name = "`AgentTlrNo`", length = 6, insertable = false, updatable = false)
  private String agentTlrNo;

  // 開始代理日期
  @Column(name = "`BeginDate`")
  private int beginDate = 0;

  // 開始代理時間
  @Column(name = "`BeginTime`")
  private int beginTime = 0;

  // 結束代理日期
  @Column(name = "`EndDate`")
  private int endDate = 0;

  // 結束代理時間
  @Column(name = "`EndTime`")
  private int endTime = 0;

  // 代理狀態
  /* 0.未生效 1.生效 */
  @Column(name = "`Status`")
  private int status = 0;

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


  public TxAgentId getTxAgentId() {
    return this.txAgentId;
  }

  public void setTxAgentId(TxAgentId txAgentId) {
    this.txAgentId = txAgentId;
  }

/**
	* 櫃員<br>
	* 
	* @return String
	*/
  public String getTlrNo() {
    return this.tlrNo == null ? "" : this.tlrNo;
  }

/**
	* 櫃員<br>
	* 
  *
  * @param tlrNo 櫃員
	*/
  public void setTlrNo(String tlrNo) {
    this.tlrNo = tlrNo;
  }

/**
	* 代理櫃員<br>
	* 
	* @return String
	*/
  public String getAgentTlrNo() {
    return this.agentTlrNo == null ? "" : this.agentTlrNo;
  }

/**
	* 代理櫃員<br>
	* 
  *
  * @param agentTlrNo 代理櫃員
	*/
  public void setAgentTlrNo(String agentTlrNo) {
    this.agentTlrNo = agentTlrNo;
  }

/**
	* 開始代理日期<br>
	* 
	* @return Integer
	*/
  public int getBeginDate() {
    return StaticTool.bcToRoc(this.beginDate);
  }

/**
	* 開始代理日期<br>
	* 
  *
  * @param beginDate 開始代理日期
  * @throws LogicException when Date Is Warn	*/
  public void setBeginDate(int beginDate) throws LogicException {
    this.beginDate = StaticTool.rocToBc(beginDate);
  }

/**
	* 開始代理時間<br>
	* 
	* @return Integer
	*/
  public int getBeginTime() {
    return this.beginTime;
  }

/**
	* 開始代理時間<br>
	* 
  *
  * @param beginTime 開始代理時間
	*/
  public void setBeginTime(int beginTime) {
    this.beginTime = beginTime;
  }

/**
	* 結束代理日期<br>
	* 
	* @return Integer
	*/
  public int getEndDate() {
    return StaticTool.bcToRoc(this.endDate);
  }

/**
	* 結束代理日期<br>
	* 
  *
  * @param endDate 結束代理日期
  * @throws LogicException when Date Is Warn	*/
  public void setEndDate(int endDate) throws LogicException {
    this.endDate = StaticTool.rocToBc(endDate);
  }

/**
	* 結束代理時間<br>
	* 
	* @return Integer
	*/
  public int getEndTime() {
    return this.endTime;
  }

/**
	* 結束代理時間<br>
	* 
  *
  * @param endTime 結束代理時間
	*/
  public void setEndTime(int endTime) {
    this.endTime = endTime;
  }

/**
	* 代理狀態<br>
	* 0.未生效 1.生效
	* @return Integer
	*/
  public int getStatus() {
    return this.status;
  }

/**
	* 代理狀態<br>
	* 0.未生效 1.生效
  *
  * @param status 代理狀態
	*/
  public void setStatus(int status) {
    this.status = status;
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
    return "TxAgent [txAgentId=" + txAgentId + ", beginDate=" + beginDate + ", beginTime=" + beginTime + ", endDate=" + endDate + ", endTime=" + endTime
           + ", status=" + status + ", createDate=" + createDate + ", createEmpNo=" + createEmpNo + ", lastUpdate=" + lastUpdate + ", lastUpdateEmpNo=" + lastUpdateEmpNo + "]";
  }
}
