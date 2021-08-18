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
 * ClOwnerRelation 擔保品所有權人與授信戶關係檔<br>
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "`ClOwnerRelation`")
public class ClOwnerRelation implements Serializable {


  /**
	 * 
	 */
	private static final long serialVersionUID = 4192792105988795720L;

@EmbeddedId
  private ClOwnerRelationId clOwnerRelationId;

  // 擔保品代號1
  /* 擔保品代號檔CdCl */
  @Column(name = "`ClCode1`", insertable = false, updatable = false)
  private int clCode1 = 0;

  // 擔保品代號2
  /* 擔保品代號檔CdCl */
  @Column(name = "`ClCode2`", insertable = false, updatable = false)
  private int clCode2 = 0;

  // 擔保品編號
  @Column(name = "`ClNo`", insertable = false, updatable = false)
  private int clNo = 0;

  // 核准號碼
  @Column(name = "`ApplNo`", insertable = false, updatable = false)
  private int applNo = 0;

  // 客戶識別碼
  @Column(name = "`OwnerCustUKey`", length = 32, insertable = false, updatable = false)
  private String ownerCustUKey;

  // 與授信戶關係
  /* 參考CdGuarantor */
  @Column(name = "`OwnerRelCode`", length = 2)
  private String ownerRelCode;

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


  public ClOwnerRelationId getClOwnerRelationId() {
    return this.clOwnerRelationId;
  }

  public void setClOwnerRelationId(ClOwnerRelationId clOwnerRelationId) {
    this.clOwnerRelationId = clOwnerRelationId;
  }

/**
	* 擔保品代號1<br>
	* 擔保品代號檔CdCl
	* @return Integer
	*/
  public int getClCode1() {
    return this.clCode1;
  }

/**
	* 擔保品代號1<br>
	* 擔保品代號檔CdCl
  *
  * @param clCode1 擔保品代號1
	*/
  public void setClCode1(int clCode1) {
    this.clCode1 = clCode1;
  }

/**
	* 擔保品代號2<br>
	* 擔保品代號檔CdCl
	* @return Integer
	*/
  public int getClCode2() {
    return this.clCode2;
  }

/**
	* 擔保品代號2<br>
	* 擔保品代號檔CdCl
  *
  * @param clCode2 擔保品代號2
	*/
  public void setClCode2(int clCode2) {
    this.clCode2 = clCode2;
  }

/**
	* 擔保品編號<br>
	* 
	* @return Integer
	*/
  public int getClNo() {
    return this.clNo;
  }

/**
	* 擔保品編號<br>
	* 
  *
  * @param clNo 擔保品編號
	*/
  public void setClNo(int clNo) {
    this.clNo = clNo;
  }

/**
	* 核准號碼<br>
	* 
	* @return Integer
	*/
  public int getApplNo() {
    return this.applNo;
  }

/**
	* 核准號碼<br>
	* 
  *
  * @param applNo 核准號碼
	*/
  public void setApplNo(int applNo) {
    this.applNo = applNo;
  }

/**
	* 客戶識別碼<br>
	* 
	* @return String
	*/
  public String getOwnerCustUKey() {
    return this.ownerCustUKey == null ? "" : this.ownerCustUKey;
  }

/**
	* 客戶識別碼<br>
	* 
  *
  * @param ownerCustUKey 客戶識別碼
	*/
  public void setOwnerCustUKey(String ownerCustUKey) {
    this.ownerCustUKey = ownerCustUKey;
  }

/**
	* 與授信戶關係<br>
	* 參考CdGuarantor
	* @return String
	*/
  public String getOwnerRelCode() {
    return this.ownerRelCode == null ? "" : this.ownerRelCode;
  }

/**
	* 與授信戶關係<br>
	* 參考CdGuarantor
  *
  * @param ownerRelCode 與授信戶關係
	*/
  public void setOwnerRelCode(String ownerRelCode) {
    this.ownerRelCode = ownerRelCode;
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
    return "ClOwnerRelation [clOwnerRelationId=" + clOwnerRelationId + ", ownerRelCode=" + ownerRelCode
           + ", createDate=" + createDate + ", createEmpNo=" + createEmpNo + ", lastUpdate=" + lastUpdate + ", lastUpdateEmpNo=" + lastUpdateEmpNo + "]";
  }
}
