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
 * CdConvertCode 代碼轉換檔<br>
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "`CdConvertCode`")
public class CdConvertCode implements Serializable {


  /**
	 * 
	 */
	private static final long serialVersionUID = -102277670847123997L;

@EmbeddedId
  private CdConvertCodeId cdConvertCodeId;

  // 代碼轉換類別
  /* eloan CustTypeCode */
  @Column(name = "`CodeType`", length = 20, insertable = false, updatable = false)
  private String codeType;

  // 原始代碼
  @Column(name = "`orgCode`", length = 20, insertable = false, updatable = false)
  private String orgCode;

  // 原始代碼說明
  @Column(name = "`orgItem`", length = 50)
  private String orgItem;

  // 新貸中代碼
  @Column(name = "`newCode`", length = 20)
  private String newCode;

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


  public CdConvertCodeId getCdConvertCodeId() {
    return this.cdConvertCodeId;
  }

  public void setCdConvertCodeId(CdConvertCodeId cdConvertCodeId) {
    this.cdConvertCodeId = cdConvertCodeId;
  }

/**
	* 代碼轉換類別<br>
	* eloan CustTypeCode
	* @return String
	*/
  public String getCodeType() {
    return this.codeType == null ? "" : this.codeType;
  }

/**
	* 代碼轉換類別<br>
	* eloan CustTypeCode
  *
  * @param codeType 代碼轉換類別
	*/
  public void setCodeType(String codeType) {
    this.codeType = codeType;
  }

/**
	* 原始代碼<br>
	* 
	* @return String
	*/
  public String getOrgCode() {
    return this.orgCode == null ? "" : this.orgCode;
  }

/**
	* 原始代碼<br>
	* 
  *
  * @param orgCode 原始代碼
	*/
  public void setOrgCode(String orgCode) {
    this.orgCode = orgCode;
  }

/**
	* 原始代碼說明<br>
	* 
	* @return String
	*/
  public String getOrgItem() {
    return this.orgItem == null ? "" : this.orgItem;
  }

/**
	* 原始代碼說明<br>
	* 
  *
  * @param orgItem 原始代碼說明
	*/
  public void setOrgItem(String orgItem) {
    this.orgItem = orgItem;
  }

/**
	* 新貸中代碼<br>
	* 
	* @return String
	*/
  public String getNewCode() {
    return this.newCode == null ? "" : this.newCode;
  }

/**
	* 新貸中代碼<br>
	* 
  *
  * @param newCode 新貸中代碼
	*/
  public void setNewCode(String newCode) {
    this.newCode = newCode;
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
    return "CdConvertCode [cdConvertCodeId=" + cdConvertCodeId + ", orgItem=" + orgItem + ", newCode=" + newCode + ", createDate=" + createDate + ", createEmpNo=" + createEmpNo
           + ", lastUpdate=" + lastUpdate + ", lastUpdateEmpNo=" + lastUpdateEmpNo + "]";
  }
}
