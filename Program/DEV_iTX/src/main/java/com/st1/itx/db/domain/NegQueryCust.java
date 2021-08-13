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
 * NegQueryCust 債務協商客戶分攤檔產生<br>
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "`NegQueryCust`")
public class NegQueryCust implements Serializable {


  /**
	 * 
	 */
	private static final long serialVersionUID = 1885705645113510734L;

@EmbeddedId
  private NegQueryCustId negQueryCustId;

  // 會計日期
  @Column(name = "`AcDate`", insertable = false, updatable = false)
  private int acDate = 0;

  // 身份證字號/統一編號
  /* 保貸戶須建立客戶主檔 */
  @Column(name = "`CustId`", length = 10, insertable = false, updatable = false)
  private String custId;

  // 是否已製檔
  /* Y;N */
  @Column(name = "`FileYN`", length = 1)
  private String fileYN;

  // 批號
  /* 從1開始編 */
  @Column(name = "`SeqNo`")
  private int seqNo = 0;

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


  public NegQueryCustId getNegQueryCustId() {
    return this.negQueryCustId;
  }

  public void setNegQueryCustId(NegQueryCustId negQueryCustId) {
    this.negQueryCustId = negQueryCustId;
  }

/**
	* 會計日期<br>
	* 
	* @return Integer
	*/
  public int getAcDate() {
    return this.acDate;
  }

/**
	* 會計日期<br>
	* 
  *
  * @param acDate 會計日期
	*/
  public void setAcDate(int acDate) {
    this.acDate = acDate;
  }

/**
	* 身份證字號/統一編號<br>
	* 保貸戶須建立客戶主檔
	* @return String
	*/
  public String getCustId() {
    return this.custId == null ? "" : this.custId;
  }

/**
	* 身份證字號/統一編號<br>
	* 保貸戶須建立客戶主檔
  *
  * @param custId 身份證字號/統一編號
	*/
  public void setCustId(String custId) {
    this.custId = custId;
  }

/**
	* 是否已製檔<br>
	* Y;N
	* @return String
	*/
  public String getFileYN() {
    return this.fileYN == null ? "" : this.fileYN;
  }

/**
	* 是否已製檔<br>
	* Y;N
  *
  * @param fileYN 是否已製檔
	*/
  public void setFileYN(String fileYN) {
    this.fileYN = fileYN;
  }

/**
	* 批號<br>
	* 從1開始編
	* @return Integer
	*/
  public int getSeqNo() {
    return this.seqNo;
  }

/**
	* 批號<br>
	* 從1開始編
  *
  * @param seqNo 批號
	*/
  public void setSeqNo(int seqNo) {
    this.seqNo = seqNo;
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
    return "NegQueryCust [negQueryCustId=" + negQueryCustId + ", fileYN=" + fileYN + ", seqNo=" + seqNo + ", createDate=" + createDate + ", createEmpNo=" + createEmpNo
           + ", lastUpdate=" + lastUpdate + ", lastUpdateEmpNo=" + lastUpdateEmpNo + "]";
  }
}
