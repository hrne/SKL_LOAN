package com.st1.itx.db.domain;

import java.io.Serializable;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 * CdLandSection 地段代碼檔<br>
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
@Embeddable
public class CdLandSectionId implements Serializable {


  /**
	 * 
	 */
	private static final long serialVersionUID = -1155522733945333767L;

// 地區別
  /* CdCity.CityCode */
  @Column(name = "`CityCode`", length = 2)
  private String cityCode = " ";

  // 鄉鎮區
  /* CdArea.AreaCode */
  @Column(name = "`AreaCode`", length = 2)
  private String areaCode = " ";

  // 段小段代碼
  @Column(name = "`IrCode`", length = 5)
  private String irCode = " ";

  public CdLandSectionId() {
  }

  public CdLandSectionId(String cityCode, String areaCode, String irCode) {
    this.cityCode = cityCode;
    this.areaCode = areaCode;
    this.irCode = irCode;
  }

/**
	* 地區別<br>
	* CdCity.CityCode
	* @return String
	*/
  public String getCityCode() {
    return this.cityCode == null ? "" : this.cityCode;
  }

/**
	* 地區別<br>
	* CdCity.CityCode
  *
  * @param cityCode 地區別
	*/
  public void setCityCode(String cityCode) {
    this.cityCode = cityCode;
  }

/**
	* 鄉鎮區<br>
	* CdArea.AreaCode
	* @return String
	*/
  public String getAreaCode() {
    return this.areaCode == null ? "" : this.areaCode;
  }

/**
	* 鄉鎮區<br>
	* CdArea.AreaCode
  *
  * @param areaCode 鄉鎮區
	*/
  public void setAreaCode(String areaCode) {
    this.areaCode = areaCode;
  }

/**
	* 段小段代碼<br>
	* 
	* @return String
	*/
  public String getIrCode() {
    return this.irCode == null ? "" : this.irCode;
  }

/**
	* 段小段代碼<br>
	* 
  *
  * @param irCode 段小段代碼
	*/
  public void setIrCode(String irCode) {
    this.irCode = irCode;
  }


  @Override
  public int hashCode() {
    return Objects.hash(cityCode, areaCode, irCode);
  }

  @Override
  public boolean equals(Object obj) {
    if(this == obj)
      return true;
    if(obj == null || getClass() != obj.getClass())
      return false;
    CdLandSectionId cdLandSectionId = (CdLandSectionId) obj;
    return cityCode.equals(cdLandSectionId.cityCode) && areaCode.equals(cdLandSectionId.areaCode) && irCode.equals(cdLandSectionId.irCode);
  }

  @Override
  public String toString() {
    return "CdLandSectionId [cityCode=" + cityCode + ", areaCode=" + areaCode + ", irCode=" + irCode + "]";
  }
}
