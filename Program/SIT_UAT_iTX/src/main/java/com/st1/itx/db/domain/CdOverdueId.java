package com.st1.itx.db.domain;

import java.io.Serializable;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 * CdOverdue 逾期新增減少原因檔<br>
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
@Embeddable
public class CdOverdueId implements Serializable {


  /**
	 * 
	 */
	private static final long serialVersionUID = -828532107787992439L;

// 逾期增減碼
  /* 1: 增加 2: 減少 */
  @Column(name = "`OverdueSign`", length = 1)
  private String overdueSign = " ";

  // 增減原因代號
  /* 目前資料看起來取前2碼就足夠 */
  @Column(name = "`OverdueCode`", length = 4)
  private String overdueCode = " ";

  public CdOverdueId() {
  }

  public CdOverdueId(String overdueSign, String overdueCode) {
    this.overdueSign = overdueSign;
    this.overdueCode = overdueCode;
  }

/**
	* 逾期增減碼<br>
	* 1: 增加 2: 減少
	* @return String
	*/
  public String getOverdueSign() {
    return this.overdueSign == null ? "" : this.overdueSign;
  }

/**
	* 逾期增減碼<br>
	* 1: 增加 2: 減少
  *
  * @param overdueSign 逾期增減碼
	*/
  public void setOverdueSign(String overdueSign) {
    this.overdueSign = overdueSign;
  }

/**
	* 增減原因代號<br>
	* 目前資料看起來取前2碼就足夠
	* @return String
	*/
  public String getOverdueCode() {
    return this.overdueCode == null ? "" : this.overdueCode;
  }

/**
	* 增減原因代號<br>
	* 目前資料看起來取前2碼就足夠
  *
  * @param overdueCode 增減原因代號
	*/
  public void setOverdueCode(String overdueCode) {
    this.overdueCode = overdueCode;
  }


  @Override
  public int hashCode() {
    return Objects.hash(overdueSign, overdueCode);
  }

  @Override
  public boolean equals(Object obj) {
    if(this == obj)
      return true;
    if(obj == null || getClass() != obj.getClass())
      return false;
    CdOverdueId cdOverdueId = (CdOverdueId) obj;
    return overdueSign.equals(cdOverdueId.overdueSign) && overdueCode.equals(cdOverdueId.overdueCode);
  }

  @Override
  public String toString() {
    return "CdOverdueId [overdueSign=" + overdueSign + ", overdueCode=" + overdueCode + "]";
  }
}
