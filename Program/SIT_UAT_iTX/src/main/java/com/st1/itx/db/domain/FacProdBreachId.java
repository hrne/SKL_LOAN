package com.st1.itx.db.domain;

import java.io.Serializable;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 * FacProdBreach 商品參數副檔清償金類型<br>
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
@Embeddable
public class FacProdBreachId implements Serializable {


  /**
	 * 
	 */
	private static final long serialVersionUID = 3660704298909394449L;

// 商品代碼或戶號+額度編號
  @Column(name = "`BreachNo`", length = 10)
  private String breachNo = " ";

  // 違約適用方式
  @Column(name = "`BreachCode`", length = 3)
  private String breachCode = " ";

  // 月數(含)以上
  @Column(name = "`MonthStart`")
  private int monthStart = 0;

  public FacProdBreachId() {
  }

  public FacProdBreachId(String breachNo, String breachCode, int monthStart) {
    this.breachNo = breachNo;
    this.breachCode = breachCode;
    this.monthStart = monthStart;
  }

/**
	* 商品代碼或戶號+額度編號<br>
	* 
	* @return String
	*/
  public String getBreachNo() {
    return this.breachNo == null ? "" : this.breachNo;
  }

/**
	* 商品代碼或戶號+額度編號<br>
	* 
  *
  * @param breachNo 商品代碼或戶號+額度編號
	*/
  public void setBreachNo(String breachNo) {
    this.breachNo = breachNo;
  }

/**
	* 違約適用方式<br>
	* 
	* @return String
	*/
  public String getBreachCode() {
    return this.breachCode == null ? "" : this.breachCode;
  }

/**
	* 違約適用方式<br>
	* 
  *
  * @param breachCode 違約適用方式
	*/
  public void setBreachCode(String breachCode) {
    this.breachCode = breachCode;
  }

/**
	* 月數(含)以上<br>
	* 
	* @return Integer
	*/
  public int getMonthStart() {
    return this.monthStart;
  }

/**
	* 月數(含)以上<br>
	* 
  *
  * @param monthStart 月數(含)以上
	*/
  public void setMonthStart(int monthStart) {
    this.monthStart = monthStart;
  }


  @Override
  public int hashCode() {
    return Objects.hash(breachNo, breachCode, monthStart);
  }

  @Override
  public boolean equals(Object obj) {
    if(this == obj)
      return true;
    if(obj == null || getClass() != obj.getClass())
      return false;
    FacProdBreachId facProdBreachId = (FacProdBreachId) obj;
    return breachNo.equals(facProdBreachId.breachNo) && breachCode.equals(facProdBreachId.breachCode) && monthStart == facProdBreachId.monthStart;
  }

  @Override
  public String toString() {
    return "FacProdBreachId [breachNo=" + breachNo + ", breachCode=" + breachCode + ", monthStart=" + monthStart + "]";
  }
}
