package com.st1.itx.db.domain;

import java.io.Serializable;
import java.math.BigDecimal;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.EntityListeners;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import javax.persistence.EmbeddedId;
import javax.persistence.Column;

/**
 * FacProdAcctFee 商品參數副檔帳管費<br>
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "`FacProdAcctFee`")
public class FacProdAcctFee implements Serializable {


  /**
	 * 
	 */
	private static final long serialVersionUID = 5160815435009359391L;

@EmbeddedId
  private FacProdAcctFeeId facProdAcctFeeId;

  // 商品代碼
  @Column(name = "`ProdNo`", length = 9, insertable = false, updatable = false)
  private String prodNo;

  // 貸款金額(含)以上
  @Column(name = "`LoanLow`", insertable = false, updatable = false)
  private BigDecimal loanLow = new BigDecimal("0");

  // 貸款金額(含)以下
  @Column(name = "`LoanHigh`")
  private BigDecimal loanHigh = new BigDecimal("0");

  // 帳管費
  @Column(name = "`AcctFee`")
  private BigDecimal acctFee = new BigDecimal("0");

  // 建檔日期時間
  @CreatedDate
  @Column(name = "`CreateDate`")
  private java.sql.Timestamp createDate;

  // 建檔人員
  @Column(name = "`CreateEmpNo`", length = 6)
  private String createEmpNo;

  // 最後更新日期時間
  @LastModifiedDate
  @Column(name = "`LastUpdate`")
  private java.sql.Timestamp lastUpdate;

  // 最後更新人員
  @Column(name = "`LastUpdateEmpNo`", length = 6)
  private String lastUpdateEmpNo;


  public FacProdAcctFeeId getFacProdAcctFeeId() {
    return this.facProdAcctFeeId;
  }

  public void setFacProdAcctFeeId(FacProdAcctFeeId facProdAcctFeeId) {
    this.facProdAcctFeeId = facProdAcctFeeId;
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

/**
	* 貸款金額(含)以下<br>
	* 
	* @return BigDecimal
	*/
  public BigDecimal getLoanHigh() {
    return this.loanHigh;
  }

/**
	* 貸款金額(含)以下<br>
	* 
  *
  * @param loanHigh 貸款金額(含)以下
	*/
  public void setLoanHigh(BigDecimal loanHigh) {
    this.loanHigh = loanHigh;
  }

/**
	* 帳管費<br>
	* 
	* @return BigDecimal
	*/
  public BigDecimal getAcctFee() {
    return this.acctFee;
  }

/**
	* 帳管費<br>
	* 
  *
  * @param acctFee 帳管費
	*/
  public void setAcctFee(BigDecimal acctFee) {
    this.acctFee = acctFee;
  }

/**
	* 建檔日期時間<br>
	* 
	* @return java.sql.Timestamp
	*/
  public java.sql.Timestamp getCreateDate() {
    return this.createDate;
  }

/**
	* 建檔日期時間<br>
	* 
  *
  * @param createDate 建檔日期時間
	*/
  public void setCreateDate(java.sql.Timestamp createDate) {
    this.createDate = createDate;
  }

/**
	* 建檔人員<br>
	* 
	* @return String
	*/
  public String getCreateEmpNo() {
    return this.createEmpNo == null ? "" : this.createEmpNo;
  }

/**
	* 建檔人員<br>
	* 
  *
  * @param createEmpNo 建檔人員
	*/
  public void setCreateEmpNo(String createEmpNo) {
    this.createEmpNo = createEmpNo;
  }

/**
	* 最後更新日期時間<br>
	* 
	* @return java.sql.Timestamp
	*/
  public java.sql.Timestamp getLastUpdate() {
    return this.lastUpdate;
  }

/**
	* 最後更新日期時間<br>
	* 
  *
  * @param lastUpdate 最後更新日期時間
	*/
  public void setLastUpdate(java.sql.Timestamp lastUpdate) {
    this.lastUpdate = lastUpdate;
  }

/**
	* 最後更新人員<br>
	* 
	* @return String
	*/
  public String getLastUpdateEmpNo() {
    return this.lastUpdateEmpNo == null ? "" : this.lastUpdateEmpNo;
  }

/**
	* 最後更新人員<br>
	* 
  *
  * @param lastUpdateEmpNo 最後更新人員
	*/
  public void setLastUpdateEmpNo(String lastUpdateEmpNo) {
    this.lastUpdateEmpNo = lastUpdateEmpNo;
  }


  @Override
  public String toString() {
    return "FacProdAcctFee [facProdAcctFeeId=" + facProdAcctFeeId + ", loanHigh=" + loanHigh + ", acctFee=" + acctFee + ", createDate=" + createDate + ", createEmpNo=" + createEmpNo
           + ", lastUpdate=" + lastUpdate + ", lastUpdateEmpNo=" + lastUpdateEmpNo + "]";
  }
}
