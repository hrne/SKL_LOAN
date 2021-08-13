package com.st1.itx.db.domain;

import java.io.Serializable;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 * FacProdStepRate 商品參數副檔階梯式利率<br>
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
@Embeddable
public class FacProdStepRateId implements Serializable {


  /**
	 * 
	 */
	private static final long serialVersionUID = 1777203963230587291L;

// 商品代碼或戶號+額度編號
  @Column(name = "`ProdNo`", length = 10)
  private String prodNo = " ";

  // 月數(含)以上
  @Column(name = "`MonthStart`")
  private int monthStart = 0;

  public FacProdStepRateId() {
  }

  public FacProdStepRateId(String prodNo, int monthStart) {
    this.prodNo = prodNo;
    this.monthStart = monthStart;
  }

/**
	* 商品代碼或戶號+額度編號<br>
	* 
	* @return String
	*/
  public String getProdNo() {
    return this.prodNo == null ? "" : this.prodNo;
  }

/**
	* 商品代碼或戶號+額度編號<br>
	* 
  *
  * @param prodNo 商品代碼或戶號+額度編號
	*/
  public void setProdNo(String prodNo) {
    this.prodNo = prodNo;
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
    return Objects.hash(prodNo, monthStart);
  }

  @Override
  public boolean equals(Object obj) {
    if(this == obj)
      return true;
    if(obj == null || getClass() != obj.getClass())
      return false;
    FacProdStepRateId facProdStepRateId = (FacProdStepRateId) obj;
    return prodNo.equals(facProdStepRateId.prodNo) && monthStart == facProdStepRateId.monthStart;
  }

  @Override
  public String toString() {
    return "FacProdStepRateId [prodNo=" + prodNo + ", monthStart=" + monthStart + "]";
  }
}
