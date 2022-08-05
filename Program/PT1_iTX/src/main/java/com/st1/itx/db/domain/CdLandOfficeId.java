package com.st1.itx.db.domain;

import java.io.Serializable;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 * CdLandOffice 地政收件字檔<br>
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
@Embeddable
public class CdLandOfficeId implements Serializable {


  // 地政所代碼
  @Column(name = "`LandOfficeCode`", length = 2)
  private String landOfficeCode = " ";

  // 收件字代碼
  @Column(name = "`RecWord`", length = 3)
  private String recWord = " ";

  public CdLandOfficeId() {
  }

  public CdLandOfficeId(String landOfficeCode, String recWord) {
    this.landOfficeCode = landOfficeCode;
    this.recWord = recWord;
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
    return Objects.hash(landOfficeCode, recWord);
  }

  @Override
  public boolean equals(Object obj) {
    if(this == obj)
      return true;
    if(obj == null || getClass() != obj.getClass())
      return false;
    CdLandOfficeId cdLandOfficeId = (CdLandOfficeId) obj;
    return landOfficeCode.equals(cdLandOfficeId.landOfficeCode) && recWord.equals(cdLandOfficeId.recWord);
  }

  @Override
  public String toString() {
    return "CdLandOfficeId [landOfficeCode=" + landOfficeCode + ", recWord=" + recWord + "]";
  }
}
