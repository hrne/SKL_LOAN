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
import com.st1.itx.util.StaticTool;
import com.st1.itx.Exception.LogicException;

/**
 * LoanBook 放款約定還本檔<br>
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "`LoanBook`")
public class LoanBook implements Serializable {


  /**
	 * 
	 */
	private static final long serialVersionUID = 3991493654242324571L;

@EmbeddedId
  private LoanBookId loanBookId;

  // 借款人戶號
  @Column(name = "`CustNo`", insertable = false, updatable = false)
  private int custNo = 0;

  // 額度編號
  @Column(name = "`FacmNo`", insertable = false, updatable = false)
  private int facmNo = 0;

  // 撥款序號
  @Column(name = "`BormNo`", insertable = false, updatable = false)
  private int bormNo = 0;

  // 約定還本日期
  @Column(name = "`BookDate`", insertable = false, updatable = false)
  private int bookDate = 0;

  // 實際還本日期
  /* 入帳日期 */
  @Column(name = "`ActualDate`")
  private int actualDate = 0;

  // 狀態
  /* 0: 未回收1: 已回收 */
  @Column(name = "`Status`")
  private int status = 0;

  // 幣別
  /* 共用代碼檔TWD: 新台幣 */
  @Column(name = "`CurrencyCode`", length = 3)
  private String currencyCode;

  // 是否內含利息
  @Column(name = "`IncludeIntFlag`", length = 1)
  private String includeIntFlag;

  // 利息是否可欠繳
  @Column(name = "`UnpaidIntFlag`", length = 1)
  private String unpaidIntFlag;

  // 約定還本金額
  @Column(name = "`BookAmt`")
  private BigDecimal bookAmt = new BigDecimal("0");

  // 實際還本金額
  @Column(name = "`RepayAmt`")
  private BigDecimal repayAmt = new BigDecimal("0");

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


  public LoanBookId getLoanBookId() {
    return this.loanBookId;
  }

  public void setLoanBookId(LoanBookId loanBookId) {
    this.loanBookId = loanBookId;
  }

/**
	* 借款人戶號<br>
	* 
	* @return Integer
	*/
  public int getCustNo() {
    return this.custNo;
  }

/**
	* 借款人戶號<br>
	* 
  *
  * @param custNo 借款人戶號
	*/
  public void setCustNo(int custNo) {
    this.custNo = custNo;
  }

/**
	* 額度編號<br>
	* 
	* @return Integer
	*/
  public int getFacmNo() {
    return this.facmNo;
  }

/**
	* 額度編號<br>
	* 
  *
  * @param facmNo 額度編號
	*/
  public void setFacmNo(int facmNo) {
    this.facmNo = facmNo;
  }

/**
	* 撥款序號<br>
	* 
	* @return Integer
	*/
  public int getBormNo() {
    return this.bormNo;
  }

/**
	* 撥款序號<br>
	* 
  *
  * @param bormNo 撥款序號
	*/
  public void setBormNo(int bormNo) {
    this.bormNo = bormNo;
  }

/**
	* 約定還本日期<br>
	* 
	* @return Integer
	*/
  public int getBookDate() {
    return StaticTool.bcToRoc(this.bookDate);
  }

/**
	* 約定還本日期<br>
	* 
  *
  * @param bookDate 約定還本日期
  * @throws LogicException when Date Is Warn	*/
  public void setBookDate(int bookDate) throws LogicException {
    this.bookDate = StaticTool.rocToBc(bookDate);
  }

/**
	* 實際還本日期<br>
	* 入帳日期
	* @return Integer
	*/
  public int getActualDate() {
    return StaticTool.bcToRoc(this.actualDate);
  }

/**
	* 實際還本日期<br>
	* 入帳日期
  *
  * @param actualDate 實際還本日期
  * @throws LogicException when Date Is Warn	*/
  public void setActualDate(int actualDate) throws LogicException {
    this.actualDate = StaticTool.rocToBc(actualDate);
  }

/**
	* 狀態<br>
	* 0: 未回收
1: 已回收
	* @return Integer
	*/
  public int getStatus() {
    return this.status;
  }

/**
	* 狀態<br>
	* 0: 未回收
1: 已回收
  *
  * @param status 狀態
	*/
  public void setStatus(int status) {
    this.status = status;
  }

/**
	* 幣別<br>
	* 共用代碼檔
TWD: 新台幣
	* @return String
	*/
  public String getCurrencyCode() {
    return this.currencyCode == null ? "" : this.currencyCode;
  }

/**
	* 幣別<br>
	* 共用代碼檔
TWD: 新台幣
  *
  * @param currencyCode 幣別
	*/
  public void setCurrencyCode(String currencyCode) {
    this.currencyCode = currencyCode;
  }

/**
	* 是否內含利息<br>
	* 
	* @return String
	*/
  public String getIncludeIntFlag() {
    return this.includeIntFlag == null ? "" : this.includeIntFlag;
  }

/**
	* 是否內含利息<br>
	* 
  *
  * @param includeIntFlag 是否內含利息
	*/
  public void setIncludeIntFlag(String includeIntFlag) {
    this.includeIntFlag = includeIntFlag;
  }

/**
	* 利息是否可欠繳<br>
	* 
	* @return String
	*/
  public String getUnpaidIntFlag() {
    return this.unpaidIntFlag == null ? "" : this.unpaidIntFlag;
  }

/**
	* 利息是否可欠繳<br>
	* 
  *
  * @param unpaidIntFlag 利息是否可欠繳
	*/
  public void setUnpaidIntFlag(String unpaidIntFlag) {
    this.unpaidIntFlag = unpaidIntFlag;
  }

/**
	* 約定還本金額<br>
	* 
	* @return BigDecimal
	*/
  public BigDecimal getBookAmt() {
    return this.bookAmt;
  }

/**
	* 約定還本金額<br>
	* 
  *
  * @param bookAmt 約定還本金額
	*/
  public void setBookAmt(BigDecimal bookAmt) {
    this.bookAmt = bookAmt;
  }

/**
	* 實際還本金額<br>
	* 
	* @return BigDecimal
	*/
  public BigDecimal getRepayAmt() {
    return this.repayAmt;
  }

/**
	* 實際還本金額<br>
	* 
  *
  * @param repayAmt 實際還本金額
	*/
  public void setRepayAmt(BigDecimal repayAmt) {
    this.repayAmt = repayAmt;
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
    return "LoanBook [loanBookId=" + loanBookId + ", actualDate=" + actualDate + ", status=" + status
           + ", currencyCode=" + currencyCode + ", includeIntFlag=" + includeIntFlag + ", unpaidIntFlag=" + unpaidIntFlag + ", bookAmt=" + bookAmt + ", repayAmt=" + repayAmt + ", createDate=" + createDate
           + ", createEmpNo=" + createEmpNo + ", lastUpdate=" + lastUpdate + ", lastUpdateEmpNo=" + lastUpdateEmpNo + "]";
  }
}
