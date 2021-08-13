package com.st1.itx.db.domain;

import java.io.Serializable;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 * AcLoanInt 提息明細檔<br>
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
@Embeddable
public class AcLoanIntId implements Serializable {


  /**
	 * 
	 */
	private static final long serialVersionUID = -993908753606402031L;

// 提息年月
  @Column(name = "`YearMonth`")
  private int yearMonth = 0;

  // 借款人戶號
  @Column(name = "`CustNo`")
  private int custNo = 0;

  // 額度編號
  @Column(name = "`FacmNo`")
  private int facmNo = 0;

  // 撥款序號
  @Column(name = "`BormNo`")
  private int bormNo = 0;

  // 期數編號
  /* 含短繳利息 */
  @Column(name = "`TermNo`")
  private int termNo = 0;

  public AcLoanIntId() {
  }

  public AcLoanIntId(int yearMonth, int custNo, int facmNo, int bormNo, int termNo) {
    this.yearMonth = yearMonth;
    this.custNo = custNo;
    this.facmNo = facmNo;
    this.bormNo = bormNo;
    this.termNo = termNo;
  }

/**
	* 提息年月<br>
	* 
	* @return Integer
	*/
  public int getYearMonth() {
    return this.yearMonth;
  }

/**
	* 提息年月<br>
	* 
  *
  * @param yearMonth 提息年月
	*/
  public void setYearMonth(int yearMonth) {
    this.yearMonth = yearMonth;
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
	* 期數編號<br>
	* 含短繳利息
	* @return Integer
	*/
  public int getTermNo() {
    return this.termNo;
  }

/**
	* 期數編號<br>
	* 含短繳利息
  *
  * @param termNo 期數編號
	*/
  public void setTermNo(int termNo) {
    this.termNo = termNo;
  }


  @Override
  public int hashCode() {
    return Objects.hash(yearMonth, custNo, facmNo, bormNo, termNo);
  }

  @Override
  public boolean equals(Object obj) {
    if(this == obj)
      return true;
    if(obj == null || getClass() != obj.getClass())
      return false;
    AcLoanIntId acLoanIntId = (AcLoanIntId) obj;
    return yearMonth == acLoanIntId.yearMonth && custNo == acLoanIntId.custNo && facmNo == acLoanIntId.facmNo && bormNo == acLoanIntId.bormNo && termNo == acLoanIntId.termNo;
  }

  @Override
  public String toString() {
    return "AcLoanIntId [yearMonth=" + yearMonth + ", custNo=" + custNo + ", facmNo=" + facmNo + ", bormNo=" + bormNo + ", termNo=" + termNo + "]";
  }
}
