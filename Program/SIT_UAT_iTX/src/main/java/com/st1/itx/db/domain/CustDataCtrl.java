package com.st1.itx.db.domain;

import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.EntityListeners;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import javax.persistence.Id;
import javax.persistence.Column;

/**
 * CustDataCtrl 結清戶個資控管檔<br>
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "`CustDataCtrl`")
public class CustDataCtrl implements Serializable {


  /**
	 * 
	 */
	private static final long serialVersionUID = -7557696199710592432L;

// 借款人戶號
  @Id
  @Column(name = "`CustNo`")
  private int custNo = 0;

  // 客戶識別碼
  @Column(name = "`CustUKey`", length = 32)
  private String custUKey;

  // 申請記號
  /* 1:客戶申請(案件申請時丟錯誤訊息)2:滿五年自動寫入(案件申請自動刪除)3:解除 */
  @Column(name = "`ApplMark`")
  private int applMark = 0;

  // 解除原因
  @Column(name = "`Reason`", length = 50)
  private String reason;

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
	* 申請記號<br>
	* 1:客戶申請(案件申請時丟錯誤訊息)
2:滿五年自動寫入(案件申請自動刪除)
3:解除
	* @return Integer
	*/
  public int getApplMark() {
    return this.applMark;
  }

/**
	* 申請記號<br>
	* 1:客戶申請(案件申請時丟錯誤訊息)
2:滿五年自動寫入(案件申請自動刪除)
3:解除
  *
  * @param applMark 申請記號
	*/
  public void setApplMark(int applMark) {
    this.applMark = applMark;
  }

/**
	* 解除原因<br>
	* 
	* @return String
	*/
  public String getReason() {
    return this.reason == null ? "" : this.reason;
  }

/**
	* 解除原因<br>
	* 
  *
  * @param reason 解除原因
	*/
  public void setReason(String reason) {
    this.reason = reason;
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
    return "CustDataCtrl [custNo=" + custNo + ", custUKey=" + custUKey + ", applMark=" + applMark + ", reason=" + reason + ", createDate=" + createDate + ", createEmpNo=" + createEmpNo
           + ", lastUpdate=" + lastUpdate + ", lastUpdateEmpNo=" + lastUpdateEmpNo + "]";
  }
}
