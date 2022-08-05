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
 * CdLandOffice 地政收件字檔<br>
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "`CdLandOffice`")
public class CdLandOffice implements Serializable {


  @EmbeddedId
  private CdLandOfficeId cdLandOfficeId;

  // 地政所代碼
  @Column(name = "`LandOfficeCode`", length = 2, insertable = false, updatable = false)
  private String landOfficeCode;

  // 收件字代碼
  @Column(name = "`RecWord`", length = 3, insertable = false, updatable = false)
  private String recWord;

  // 收件字說明
  @Column(name = "`RecWordItem`", length = 30)
  private String recWordItem;

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


  public CdLandOfficeId getCdLandOfficeId() {
    return this.cdLandOfficeId;
  }

  public void setCdLandOfficeId(CdLandOfficeId cdLandOfficeId) {
    this.cdLandOfficeId = cdLandOfficeId;
  }

/**
	* 地政所代碼<br>
	* 
	* @return String
	*/
  public String getLandOfficeCode() {
    return this.landOfficeCode == null ? "" : this.landOfficeCode;
  }

/**
	* 地政所代碼<br>
	* 
  *
  * @param landOfficeCode 地政所代碼
	*/
  public void setLandOfficeCode(String landOfficeCode) {
    this.landOfficeCode = landOfficeCode;
  }

/**
	* 收件字代碼<br>
	* 
	* @return String
	*/
  public String getRecWord() {
    return this.recWord == null ? "" : this.recWord;
  }

/**
	* 收件字代碼<br>
	* 
  *
  * @param recWord 收件字代碼
	*/
  public void setRecWord(String recWord) {
    this.recWord = recWord;
  }

/**
	* 收件字說明<br>
	* 
	* @return String
	*/
  public String getRecWordItem() {
    return this.recWordItem == null ? "" : this.recWordItem;
  }

/**
	* 收件字說明<br>
	* 
  *
  * @param recWordItem 收件字說明
	*/
  public void setRecWordItem(String recWordItem) {
    this.recWordItem = recWordItem;
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
    return "CdLandOffice [cdLandOfficeId=" + cdLandOfficeId + ", recWordItem=" + recWordItem + ", createDate=" + createDate + ", createEmpNo=" + createEmpNo + ", lastUpdate=" + lastUpdate
           + ", lastUpdateEmpNo=" + lastUpdateEmpNo + "]";
  }
}
