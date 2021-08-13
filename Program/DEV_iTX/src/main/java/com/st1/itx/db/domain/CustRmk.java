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
 * CustRmk 顧客控管警訊檔<br>
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "`CustRmk`")
public class CustRmk implements Serializable {


  /**
	 * 
	 */
	private static final long serialVersionUID = -7529395291286569830L;

@EmbeddedId
  private CustRmkId custRmkId;

  // 借款人戶號
  @Column(name = "`CustNo`", insertable = false, updatable = false)
  private int custNo = 0;

  // 備忘錄序號
  @Column(name = "`RmkNo`", insertable = false, updatable = false)
  private int rmkNo = 0;

  // 客戶識別碼
  @Column(name = "`CustUKey`", length = 32)
  private String custUKey;

  // 備忘錄代碼
  /* 共用代碼檔 */
  @Column(name = "`RmkCode`", length = 2)
  private String rmkCode;

  // 備忘錄說明
  @Column(name = "`RmkDesc`", length = 120)
  private String rmkDesc;

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


  public CustRmkId getCustRmkId() {
    return this.custRmkId;
  }

  public void setCustRmkId(CustRmkId custRmkId) {
    this.custRmkId = custRmkId;
  }

/**
	* 借款人戶號<br>
	* 
	* @return Integer
	*/
  public int getCustNo() {
    return this.custNo;
  }

/**
	* 借款人戶號<br>
	* 
  *
  * @param custNo 借款人戶號
	*/
  public void setCustNo(int custNo) {
    this.custNo = custNo;
  }

/**
	* 備忘錄序號<br>
	* 
	* @return Integer
	*/
  public int getRmkNo() {
    return this.rmkNo;
  }

/**
	* 備忘錄序號<br>
	* 
  *
  * @param rmkNo 備忘錄序號
	*/
  public void setRmkNo(int rmkNo) {
    this.rmkNo = rmkNo;
  }

/**
	* 客戶識別碼<br>
	* 
	* @return String
	*/
  public String getCustUKey() {
    return this.custUKey == null ? "" : this.custUKey;
  }

/**
	* 客戶識別碼<br>
	* 
  *
  * @param custUKey 客戶識別碼
	*/
  public void setCustUKey(String custUKey) {
    this.custUKey = custUKey;
  }

/**
	* 備忘錄代碼<br>
	* 共用代碼檔
	* @return String
	*/
  public String getRmkCode() {
    return this.rmkCode == null ? "" : this.rmkCode;
  }

/**
	* 備忘錄代碼<br>
	* 共用代碼檔
  *
  * @param rmkCode 備忘錄代碼
	*/
  public void setRmkCode(String rmkCode) {
    this.rmkCode = rmkCode;
  }

/**
	* 備忘錄說明<br>
	* 
	* @return String
	*/
  public String getRmkDesc() {
    return this.rmkDesc == null ? "" : this.rmkDesc;
  }

/**
	* 備忘錄說明<br>
	* 
  *
  * @param rmkDesc 備忘錄說明
	*/
  public void setRmkDesc(String rmkDesc) {
    this.rmkDesc = rmkDesc;
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
    return "CustRmk [custRmkId=" + custRmkId + ", custUKey=" + custUKey + ", rmkCode=" + rmkCode + ", rmkDesc=" + rmkDesc + ", createDate=" + createDate
           + ", createEmpNo=" + createEmpNo + ", lastUpdate=" + lastUpdate + ", lastUpdateEmpNo=" + lastUpdateEmpNo + "]";
  }
}
