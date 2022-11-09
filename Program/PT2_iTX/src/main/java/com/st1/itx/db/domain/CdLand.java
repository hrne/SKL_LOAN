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
 * CdLand 縣市地政檔<br>
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "`CdLand`")
public class CdLand implements Serializable {


  /**
	 * 
	 */
	private static final long serialVersionUID = -5371579470738030083L;

@EmbeddedId
  private CdLandId cdLandId;

  // 縣市代碼
  @Column(name = "`CityCode`", length = 4, insertable = false, updatable = false)
  private String cityCode;

  // 地政所代碼
  @Column(name = "`LandOfficeCode`", length = 2, insertable = false, updatable = false)
  private String landOfficeCode;

  // 地政所說明
  @Column(name = "`LandOfficeItem`", length = 30)
  private String landOfficeItem;

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


  public CdLandId getCdLandId() {
    return this.cdLandId;
  }

  public void setCdLandId(CdLandId cdLandId) {
    this.cdLandId = cdLandId;
  }

/**
	* 縣市代碼<br>
	* 
	* @return String
	*/
  public String getCityCode() {
    return this.cityCode == null ? "" : this.cityCode;
  }

/**
	* 縣市代碼<br>
	* 
  *
  * @param cityCode 縣市代碼
	*/
  public void setCityCode(String cityCode) {
    this.cityCode = cityCode;
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
	* 地政所說明<br>
	* 
	* @return String
	*/
  public String getLandOfficeItem() {
    return this.landOfficeItem == null ? "" : this.landOfficeItem;
  }

/**
	* 地政所說明<br>
	* 
  *
  * @param landOfficeItem 地政所說明
	*/
  public void setLandOfficeItem(String landOfficeItem) {
    this.landOfficeItem = landOfficeItem;
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
    return "CdLand [cdLandId=" + cdLandId + ", landOfficeItem=" + landOfficeItem + ", createDate=" + createDate + ", createEmpNo=" + createEmpNo + ", lastUpdate=" + lastUpdate
           + ", lastUpdateEmpNo=" + lastUpdateEmpNo + "]";
  }
}
