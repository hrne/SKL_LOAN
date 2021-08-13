package com.st1.itx.db.domain;

import java.io.Serializable;
import java.util.Objects;
import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 * FacProdAcctFee 商品參數副檔帳管費<br>
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
@Embeddable
public class FacProdAcctFeeId implements Serializable {


  /**
	 * 
	 */
	private static final long serialVersionUID = 4934845359260174629L;

// 商品代碼
  @Column(name = "`ProdNo`", length = 9)
  private String prodNo = " ";

  // 貸款金額(含)以上
  @Column(name = "`LoanLow`")
  private BigDecimal loanLow = new BigDecimal("0");

  public FacProdAcctFeeId() {
  }

  public FacProdAcctFeeId(String prodNo, BigDecimal loanLow) {
    this.prodNo = prodNo;
    this.loanLow = loanLow;
  }

/**
	* 商品代碼<br>
	* 
	* @return String
	*/
  public String getProdNo() {
    return this.prodNo == null ? "" : this.prodNo;
  }

/**
	* 商品代碼<br>
	* 
  *
  * @param prodNo 商品代碼
	*/
  public void setProdNo(String prodNo) {
    this.prodNo = prodNo;
  }

/**
	* 貸款金額(含)以上<br>
	* 
	* @return BigDecimal
	*/
  public BigDecimal getLoanLow() {
    return this.loanLow;
  }

/**
	* 貸款金額(含)以上<br>
	* 
  *
  * @param loanLow 貸款金額(含)以上
	*/
  public void setLoanLow(BigDecimal loanLow) {
    this.loanLow = loanLow;
  }


  @Override
  public int hashCode() {
    return Objects.hash(prodNo, loanLow);
  }

  @Override
  public boolean equals(Object obj) {
    if(this == obj)
      return true;
    if(obj == null || getClass() != obj.getClass())
      return false;
    FacProdAcctFeeId facProdAcctFeeId = (FacProdAcctFeeId) obj;
    return prodNo.equals(facProdAcctFeeId.prodNo) && loanLow == facProdAcctFeeId.loanLow;
  }

  @Override
  public String toString() {
    return "FacProdAcctFeeId [prodNo=" + prodNo + ", loanLow=" + loanLow + "]";
  }
}
