package com.st1.itx.db.domain;

import java.io.Serializable;
import java.util.Objects;
import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import com.st1.itx.util.StaticTool;
import com.st1.itx.Exception.LogicException;

/**
 * CdCode 共用代碼檔<br>
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
@Embeddable
public class CdCodeId implements Serializable {


  // 代碼檔代號
  @Column(name = "`DefCode`", length = 20)
  private String defCode = " ";

  // 代碼
  @Column(name = "`Code`", length = 20)
  private String code = " ";

  public CdCodeId() {
  }

  public CdCodeId(String defCode, String code) {
    this.defCode = defCode;
    this.code = code;
  }

/**
	* 代碼檔代號<br>
	* 
	* @return String
	*/
  public String getDefCode() {
    return this.defCode == null ? "" : this.defCode;
  }

/**
	* 代碼檔代號<br>
	* 
  *
  * @param defCode 代碼檔代號
	*/
  public void setDefCode(String defCode) {
    this.defCode = defCode;
  }

/**
	* 代碼<br>
	* 
	* @return String
	*/
  public String getCode() {
    return this.code == null ? "" : this.code;
  }

/**
	* 代碼<br>
	* 
  *
  * @param code 代碼
	*/
  public void setCode(String code) {
    this.code = code;
  }


  @Override
  public int hashCode() {
    return Objects.hash(defCode, code);
  }

  @Override
  public boolean equals(Object obj) {
    if(this == obj)
      return true;
    if(obj == null || getClass() != obj.getClass())
      return false;
    CdCodeId cdCodeId = (CdCodeId) obj;
    return defCode.equals(cdCodeId.defCode) && code.equals(cdCodeId.code);
  }

  @Override
  public String toString() {
    return "CdCodeId [defCode=" + defCode + ", code=" + code + "]";
  }
}
