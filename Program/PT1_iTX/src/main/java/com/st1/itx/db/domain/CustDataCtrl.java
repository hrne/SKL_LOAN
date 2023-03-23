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
import javax.persistence.Id;
import javax.persistence.Column;
import com.st1.itx.util.StaticTool;
import com.st1.itx.Exception.LogicException;

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


  // 借款人戶號
  @Id
  @Column(name = "`CustNo`")
  private int custNo = 0;

  // 客戶識別碼
  @Column(name = "`CustUKey`", length = 32)
  private String custUKey;

  // 申請記號
  /* 1:客戶申請(改客戶主檔ID)2:滿五年自動寫入(案件申請自動刪除)3:解除 */
  @Column(name = "`ApplMark`")
  private int applMark = 0;

  // 解除原因
  @Column(name = "`Reason`", length = 50)
  private String reason;

  // 原始身份證字號/統一編號
  /* 客戶申請留存原始ID用 by eric 2022.3.25 */
  @Column(name = "`CustId`", length = 10)
  private String custId;

  // 原始戶名/公司名稱
  /* 客戶申請留存原始戶名用 by eric 2022.3.25 */
  @Column(name = "`CustName`", length = 100)
  private String custName;

  // 申請後身份證字號/統一編號
  /* 客戶申請後留存變更後ID by Mata 2023.3.23 */
  @Column(name = "`XXCustId`", length = 10)
  private String xXCustId;

  // 設定日期
  /* by eric 2022.3.31 */
  @Column(name = "`SetDate`")
  private java.sql.Timestamp setDate;

  // 設定人員
  /* by eric 2022.3.31 */
  @Column(name = "`SetEmpNo`", length = 6)
  private String setEmpNo;

  // 解除日期
  /* by eric 2022.3.31 */
  @Column(name = "`ReSetDate`")
  private java.sql.Timestamp reSetDate;

  // 解除人員
  /* by eric 2022.3.31 */
  @Column(name = "`ReSetEmpNo`", length = 6)
  private String reSetEmpNo;

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
	* 1:客戶申請(改客戶主檔ID)
2:滿五年自動寫入(案件申請自動刪除)
3:解除
	* @return Integer
	*/
  public int getApplMark() {
    return this.applMark;
  }

/**
	* 申請記號<br>
	* 1:客戶申請(改客戶主檔ID)
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
	* 原始身份證字號/統一編號<br>
	* 客戶申請留存原始ID用 by eric 2022.3.25
	* @return String
	*/
  public String getCustId() {
    return this.custId == null ? "" : this.custId;
  }

/**
	* 原始身份證字號/統一編號<br>
	* 客戶申請留存原始ID用 by eric 2022.3.25
  *
  * @param custId 原始身份證字號/統一編號
	*/
  public void setCustId(String custId) {
    this.custId = custId;
  }

/**
	* 原始戶名/公司名稱<br>
	* 客戶申請留存原始戶名用 by eric 2022.3.25
	* @return String
	*/
  public String getCustName() {
    return this.custName == null ? "" : this.custName;
  }

/**
	* 原始戶名/公司名稱<br>
	* 客戶申請留存原始戶名用 by eric 2022.3.25
  *
  * @param custName 原始戶名/公司名稱
	*/
  public void setCustName(String custName) {
    this.custName = custName;
  }

/**
	* 申請後身份證字號/統一編號<br>
	* 客戶申請後留存變更後ID by Mata 2023.3.23
	* @return String
	*/
  public String getXXCustId() {
    return this.xXCustId == null ? "" : this.xXCustId;
  }

/**
	* 申請後身份證字號/統一編號<br>
	* 客戶申請後留存變更後ID by Mata 2023.3.23
  *
  * @param xXCustId 申請後身份證字號/統一編號
	*/
  public void setXXCustId(String xXCustId) {
    this.xXCustId = xXCustId;
  }

/**
	* 設定日期<br>
	* by eric 2022.3.31
	* @return java.sql.Timestamp
	*/
  public java.sql.Timestamp getSetDate() {
    return this.setDate;
  }

/**
	* 設定日期<br>
	* by eric 2022.3.31
  *
  * @param setDate 設定日期
	*/
  public void setSetDate(java.sql.Timestamp setDate) {
    this.setDate = setDate;
  }

/**
	* 設定人員<br>
	* by eric 2022.3.31
	* @return String
	*/
  public String getSetEmpNo() {
    return this.setEmpNo == null ? "" : this.setEmpNo;
  }

/**
	* 設定人員<br>
	* by eric 2022.3.31
  *
  * @param setEmpNo 設定人員
	*/
  public void setSetEmpNo(String setEmpNo) {
    this.setEmpNo = setEmpNo;
  }

/**
	* 解除日期<br>
	* by eric 2022.3.31
	* @return java.sql.Timestamp
	*/
  public java.sql.Timestamp getReSetDate() {
    return this.reSetDate;
  }

/**
	* 解除日期<br>
	* by eric 2022.3.31
  *
  * @param reSetDate 解除日期
	*/
  public void setReSetDate(java.sql.Timestamp reSetDate) {
    this.reSetDate = reSetDate;
  }

/**
	* 解除人員<br>
	* by eric 2022.3.31
	* @return String
	*/
  public String getReSetEmpNo() {
    return this.reSetEmpNo == null ? "" : this.reSetEmpNo;
  }

/**
	* 解除人員<br>
	* by eric 2022.3.31
  *
  * @param reSetEmpNo 解除人員
	*/
  public void setReSetEmpNo(String reSetEmpNo) {
    this.reSetEmpNo = reSetEmpNo;
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
    return "CustDataCtrl [custNo=" + custNo + ", custUKey=" + custUKey + ", applMark=" + applMark + ", reason=" + reason + ", custId=" + custId + ", custName=" + custName
           + ", xXCustId=" + xXCustId + ", setDate=" + setDate + ", setEmpNo=" + setEmpNo + ", reSetDate=" + reSetDate + ", reSetEmpNo=" + reSetEmpNo + ", createDate=" + createDate
           + ", createEmpNo=" + createEmpNo + ", lastUpdate=" + lastUpdate + ", lastUpdateEmpNo=" + lastUpdateEmpNo + "]";
  }
}
