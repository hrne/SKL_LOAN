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
 * CustomerAmlRating 客戶AML評級資料檔<br>
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "`CustomerAmlRating`")
public class CustomerAmlRating implements Serializable {


  /**
	 * 
	 */
	private static final long serialVersionUID = 8750812032713942525L;

// 身份證字號/統一編號
  @Id
  @Column(name = "`CustId`", length = 10)
  private String custId = " ";

  // AML評級
  /* H/M/L */
  @Column(name = "`AmlRating`", length = 1)
  private String amlRating;

  // 利害關係人記號
  /* 0-非、1-是 */
  @Column(name = "`IsRelated`", length = 1)
  private String isRelated;

  // 準利害關係人記號
  /* 0-非、1-是 */
  @Column(name = "`IsLnrelNear`", length = 1)
  private String isLnrelNear;

  // 授信限制對象記號
  /* 0-非、1-是 */
  @Column(name = "`IsLimit`", length = 1)
  private String isLimit;

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
	* 身份證字號/統一編號<br>
	* 
	* @return String
	*/
  public String getCustId() {
    return this.custId == null ? "" : this.custId;
  }

/**
	* 身份證字號/統一編號<br>
	* 
  *
  * @param custId 身份證字號/統一編號
	*/
  public void setCustId(String custId) {
    this.custId = custId;
  }

/**
	* AML評級<br>
	* H/M/L
	* @return String
	*/
  public String getAmlRating() {
    return this.amlRating == null ? "" : this.amlRating;
  }

/**
	* AML評級<br>
	* H/M/L
  *
  * @param amlRating AML評級
	*/
  public void setAmlRating(String amlRating) {
    this.amlRating = amlRating;
  }

/**
	* 利害關係人記號<br>
	* 0-非、1-是
	* @return String
	*/
  public String getIsRelated() {
    return this.isRelated == null ? "" : this.isRelated;
  }

/**
	* 利害關係人記號<br>
	* 0-非、1-是
  *
  * @param isRelated 利害關係人記號
	*/
  public void setIsRelated(String isRelated) {
    this.isRelated = isRelated;
  }

/**
	* 準利害關係人記號<br>
	* 0-非、1-是
	* @return String
	*/
  public String getIsLnrelNear() {
    return this.isLnrelNear == null ? "" : this.isLnrelNear;
  }

/**
	* 準利害關係人記號<br>
	* 0-非、1-是
  *
  * @param isLnrelNear 準利害關係人記號
	*/
  public void setIsLnrelNear(String isLnrelNear) {
    this.isLnrelNear = isLnrelNear;
  }

/**
	* 授信限制對象記號<br>
	* 0-非、1-是
	* @return String
	*/
  public String getIsLimit() {
    return this.isLimit == null ? "" : this.isLimit;
  }

/**
	* 授信限制對象記號<br>
	* 0-非、1-是
  *
  * @param isLimit 授信限制對象記號
	*/
  public void setIsLimit(String isLimit) {
    this.isLimit = isLimit;
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
    return "CustomerAmlRating [custId=" + custId + ", amlRating=" + amlRating + ", isRelated=" + isRelated + ", isLnrelNear=" + isLnrelNear + ", isLimit=" + isLimit + ", createDate=" + createDate
           + ", createEmpNo=" + createEmpNo + ", lastUpdate=" + lastUpdate + ", lastUpdateEmpNo=" + lastUpdateEmpNo + "]";
  }
}
