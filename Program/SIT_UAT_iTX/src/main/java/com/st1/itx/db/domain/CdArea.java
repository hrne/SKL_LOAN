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
 * CdArea 縣市與鄉鎮區對照檔<br>
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "`CdArea`")
public class CdArea implements Serializable {


  /**
	 * 
	 */
	private static final long serialVersionUID = -6153882300605388951L;

@EmbeddedId
  private CdAreaId cdAreaId;

  // 縣市別代碼
  /* CdCity.CityCode */
  @Column(name = "`CityCode`", length = 2, insertable = false, updatable = false)
  private String cityCode;

  // 鄉鎮區代碼
  @Column(name = "`AreaCode`", length = 2, insertable = false, updatable = false)
  private String areaCode;

  // 鄉鎮區名稱
  @Column(name = "`AreaItem`", length = 12)
  private String areaItem;

  // 縣市簡稱
  @Column(name = "`CityShort`", length = 6)
  private String cityShort;

  // 鄉鎮簡稱
  @Column(name = "`AreaShort`", length = 8)
  private String areaShort;

  // JCIC縣市碼
  /* by eric 2021.9.29 */
  @Column(name = "`JcicCityCode`", length = 1)
  private String jcicCityCode;

  // JCIC鄉鎮碼
  /* by eric 2021.9.29 */
  @Column(name = "`JcicAreaCode`", length = 2)
  private String jcicAreaCode;

  // 地區類別
  @Column(name = "`CityType`", length = 2)
  private String cityType;

  // 郵遞區號
  @Column(name = "`Zip3`", length = 3)
  private String zip3;

  // 部室代號
  @Column(name = "`DepartCode`", length = 6)
  private String departCode;

  // 組合地區別
  /* A.北, B.中, C.南, D.東 */
  @Column(name = "`CityGroup`", length = 1)
  private String cityGroup;

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


  public CdAreaId getCdAreaId() {
    return this.cdAreaId;
  }

  public void setCdAreaId(CdAreaId cdAreaId) {
    this.cdAreaId = cdAreaId;
  }

/**
	* 縣市別代碼<br>
	* CdCity.CityCode
	* @return String
	*/
  public String getCityCode() {
    return this.cityCode == null ? "" : this.cityCode;
  }

/**
	* 縣市別代碼<br>
	* CdCity.CityCode
  *
  * @param cityCode 縣市別代碼
	*/
  public void setCityCode(String cityCode) {
    this.cityCode = cityCode;
  }

/**
	* 鄉鎮區代碼<br>
	* 
	* @return String
	*/
  public String getAreaCode() {
    return this.areaCode == null ? "" : this.areaCode;
  }

/**
	* 鄉鎮區代碼<br>
	* 
  *
  * @param areaCode 鄉鎮區代碼
	*/
  public void setAreaCode(String areaCode) {
    this.areaCode = areaCode;
  }

/**
	* 鄉鎮區名稱<br>
	* 
	* @return String
	*/
  public String getAreaItem() {
    return this.areaItem == null ? "" : this.areaItem;
  }

/**
	* 鄉鎮區名稱<br>
	* 
  *
  * @param areaItem 鄉鎮區名稱
	*/
  public void setAreaItem(String areaItem) {
    this.areaItem = areaItem;
  }

/**
	* 縣市簡稱<br>
	* 
	* @return String
	*/
  public String getCityShort() {
    return this.cityShort == null ? "" : this.cityShort;
  }

/**
	* 縣市簡稱<br>
	* 
  *
  * @param cityShort 縣市簡稱
	*/
  public void setCityShort(String cityShort) {
    this.cityShort = cityShort;
  }

/**
	* 鄉鎮簡稱<br>
	* 
	* @return String
	*/
  public String getAreaShort() {
    return this.areaShort == null ? "" : this.areaShort;
  }

/**
	* 鄉鎮簡稱<br>
	* 
  *
  * @param areaShort 鄉鎮簡稱
	*/
  public void setAreaShort(String areaShort) {
    this.areaShort = areaShort;
  }

/**
	* JCIC縣市碼<br>
	* by eric 2021.9.29
	* @return String
	*/
  public String getJcicCityCode() {
    return this.jcicCityCode == null ? "" : this.jcicCityCode;
  }

/**
	* JCIC縣市碼<br>
	* by eric 2021.9.29
  *
  * @param jcicCityCode JCIC縣市碼
	*/
  public void setJcicCityCode(String jcicCityCode) {
    this.jcicCityCode = jcicCityCode;
  }

/**
	* JCIC鄉鎮碼<br>
	* by eric 2021.9.29
	* @return String
	*/
  public String getJcicAreaCode() {
    return this.jcicAreaCode == null ? "" : this.jcicAreaCode;
  }

/**
	* JCIC鄉鎮碼<br>
	* by eric 2021.9.29
  *
  * @param jcicAreaCode JCIC鄉鎮碼
	*/
  public void setJcicAreaCode(String jcicAreaCode) {
    this.jcicAreaCode = jcicAreaCode;
  }

/**
	* 地區類別<br>
	* 
	* @return String
	*/
  public String getCityType() {
    return this.cityType == null ? "" : this.cityType;
  }

/**
	* 地區類別<br>
	* 
  *
  * @param cityType 地區類別
	*/
  public void setCityType(String cityType) {
    this.cityType = cityType;
  }

/**
	* 郵遞區號<br>
	* 
	* @return String
	*/
  public String getZip3() {
    return this.zip3 == null ? "" : this.zip3;
  }

/**
	* 郵遞區號<br>
	* 
  *
  * @param zip3 郵遞區號
	*/
  public void setZip3(String zip3) {
    this.zip3 = zip3;
  }

/**
	* 部室代號<br>
	* 
	* @return String
	*/
  public String getDepartCode() {
    return this.departCode == null ? "" : this.departCode;
  }

/**
	* 部室代號<br>
	* 
  *
  * @param departCode 部室代號
	*/
  public void setDepartCode(String departCode) {
    this.departCode = departCode;
  }

/**
	* 組合地區別<br>
	* A.北, B.中, C.南, D.東
	* @return String
	*/
  public String getCityGroup() {
    return this.cityGroup == null ? "" : this.cityGroup;
  }

/**
	* 組合地區別<br>
	* A.北, B.中, C.南, D.東
  *
  * @param cityGroup 組合地區別
	*/
  public void setCityGroup(String cityGroup) {
    this.cityGroup = cityGroup;
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
    return "CdArea [cdAreaId=" + cdAreaId + ", areaItem=" + areaItem + ", cityShort=" + cityShort + ", areaShort=" + areaShort + ", jcicCityCode=" + jcicCityCode
           + ", jcicAreaCode=" + jcicAreaCode + ", cityType=" + cityType + ", zip3=" + zip3 + ", departCode=" + departCode + ", cityGroup=" + cityGroup + ", createDate=" + createDate
           + ", createEmpNo=" + createEmpNo + ", lastUpdate=" + lastUpdate + ", lastUpdateEmpNo=" + lastUpdateEmpNo + "]";
  }
}
