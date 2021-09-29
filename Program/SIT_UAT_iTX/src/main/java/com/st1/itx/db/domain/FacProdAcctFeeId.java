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
	private static final long serialVersionUID = 7822704564860159310L;

// 商品代碼
  @Column(name = "`ProdNo`", length = 9)
  private String prodNo = " ";

  // 費用類別
  /* 1:帳管費2:手續費 */
  @Column(name = "`FeeType`", length = 1)
  private String feeType = " ";

  // 貸款金額(含)以上
  @Column(name = "`LoanLow`")
  private BigDecimal loanLow = new BigDecimal("0");

  public FacProdAcctFeeId() {
  }

  public FacProdAcctFeeId(String prodNo, String feeType, BigDecimal loanLow) {
    this.prodNo = prodNo;
    this.feeType = feeType;
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
	* 費用類別<br>
	* 1:帳管費
2:手續費
	* @return String
	*/
  public String getFeeType() {
    return this.feeType == null ? "" : this.feeType;
  }

/**
	* 費用類別<br>
	* 1:帳管費
2:手續費
  *
  * @param feeType 費用類別
	*/
  public void setFeeType(String feeType) {
    this.feeType = feeType;
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
    return Objects.hash(prodNo, feeType, loanLow);
  }

  @Override
  public boolean equals(Object obj) {
    if(this == obj)
      return true;
    if(obj == null || getClass() != obj.getClass())
      return false;
    FacProdAcctFeeId facProdAcctFeeId = (FacProdAcctFeeId) obj;
    return prodNo.equals(facProdAcctFeeId.prodNo) && feeType.equals(facProdAcctFeeId.feeType) && loanLow == facProdAcctFeeId.loanLow;
  }

  @Override
  public String toString() {
    return "FacProdAcctFeeId [prodNo=" + prodNo + ", feeType=" + feeType + ", loanLow=" + loanLow + "]";
  }
}
