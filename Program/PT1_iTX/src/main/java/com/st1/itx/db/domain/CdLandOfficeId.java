package com.st1.itx.db.domain;

import java.io.Serializable;
import java.util.Objects;
import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import com.st1.itx.util.StaticTool;
import com.st1.itx.Exception.LogicException;

/**
 * CdLandOffice 地政收件字檔<br>
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
@Embeddable
public class CdLandOfficeId implements Serializable {


  // 縣市別代碼
  @Column(name = "`CityCode`", length = 4)
  private String cityCode = " ";

  // 地政所代碼
  @Column(name = "`LandOfficeCode`", length = 2)
  private String landOfficeCode = " ";

  // 收件字代碼
  @Column(name = "`RecWord`", length = 3)
  private String recWord = " ";

  public CdLandOfficeId() {
  }

  public CdLandOfficeId(String cityCode, String landOfficeCode, String recWord) {
    this.cityCode = cityCode;
    this.landOfficeCode = landOfficeCode;
    this.recWord = recWord;
  }

/**
	* 縣市別代碼<br>
	* 
	* @return String
	*/
  public String getCityCode() {
    return this.cityCode == null ? "" : this.cityCode;
  }

/**
	* 縣市別代碼<br>
	* 
  *
  * @param cityCode 縣市別代碼
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


  @Override
  public int hashCode() {
    return Objects.hash(cityCode, landOfficeCode, recWord);
  }

  @Override
  public boolean equals(Object obj) {
    if(this == obj)
      return true;
    if(obj == null || getClass() != obj.getClass())
      return false;
    CdLandOfficeId cdLandOfficeId = (CdLandOfficeId) obj;
    return cityCode.equals(cdLandOfficeId.cityCode) && landOfficeCode.equals(cdLandOfficeId.landOfficeCode) && recWord.equals(cdLandOfficeId.recWord);
  }

  @Override
  public String toString() {
    return "CdLandOfficeId [cityCode=" + cityCode + ", landOfficeCode=" + landOfficeCode + ", recWord=" + recWord + "]";
  }
}
