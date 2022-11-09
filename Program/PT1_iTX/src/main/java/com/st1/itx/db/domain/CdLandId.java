package com.st1.itx.db.domain;

import java.io.Serializable;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 * CdLand 縣市地政檔<br>
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
@Embeddable
public class CdLandId implements Serializable {


  /**
	 * 
	 */
	private static final long serialVersionUID = 2777547867708394620L;

// 縣市代碼
  @Column(name = "`CityCode`", length = 4)
  private String cityCode = " ";

  // 地政所代碼
  @Column(name = "`LandOfficeCode`", length = 2)
  private String landOfficeCode = " ";

  public CdLandId() {
  }

  public CdLandId(String cityCode, String landOfficeCode) {
    this.cityCode = cityCode;
    this.landOfficeCode = landOfficeCode;
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


  @Override
  public int hashCode() {
    return Objects.hash(cityCode, landOfficeCode);
  }

  @Override
  public boolean equals(Object obj) {
    if(this == obj)
      return true;
    if(obj == null || getClass() != obj.getClass())
      return false;
    CdLandId cdLandId = (CdLandId) obj;
    return cityCode.equals(cdLandId.cityCode) && landOfficeCode.equals(cdLandId.landOfficeCode);
  }

  @Override
  public String toString() {
    return "CdLandId [cityCode=" + cityCode + ", landOfficeCode=" + landOfficeCode + "]";
  }
}
