package com.st1.itx.db.domain;

import java.io.Serializable;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 * CdConvertCode 代碼轉換檔<br>
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
@Embeddable
public class CdConvertCodeId implements Serializable {


  /**
	 * 
	 */
	private static final long serialVersionUID = 5507295668799893618L;

// 代碼轉換類別
  /* eloan CustTypeCode */
  @Column(name = "`CodeType`", length = 20)
  private String codeType = " ";

  // 原始代碼
  @Column(name = "`orgCode`", length = 20)
  private String orgCode = " ";

  public CdConvertCodeId() {
  }

  public CdConvertCodeId(String codeType, String orgCode) {
    this.codeType = codeType;
    this.orgCode = orgCode;
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


  @Override
  public int hashCode() {
    return Objects.hash(codeType, orgCode);
  }

  @Override
  public boolean equals(Object obj) {
    if(this == obj)
      return true;
    if(obj == null || getClass() != obj.getClass())
      return false;
    CdConvertCodeId cdConvertCodeId = (CdConvertCodeId) obj;
    return codeType.equals(cdConvertCodeId.codeType) && orgCode.equals(cdConvertCodeId.orgCode);
  }

  @Override
  public String toString() {
    return "CdConvertCodeId [codeType=" + codeType + ", orgCode=" + orgCode + "]";
  }
}
