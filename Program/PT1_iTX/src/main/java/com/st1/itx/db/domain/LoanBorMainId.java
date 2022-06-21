package com.st1.itx.db.domain;

import java.io.Serializable;
import java.util.Objects;
import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import com.st1.itx.util.StaticTool;
import com.st1.itx.Exception.LogicException;

/**
 * LoanBorMain 放款主檔<br>
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
@Embeddable
public class LoanBorMainId implements Serializable {


  // 借款人戶號
  @Column(name = "`CustNo`")
  private int custNo = 0;

  // 額度編號
  @Column(name = "`FacmNo`")
  private int facmNo = 0;

  // 撥款序號, 預約序號
  @Column(name = "`BormNo`")
  private int bormNo = 0;

  public LoanBorMainId() {
  }

  public LoanBorMainId(int custNo, int facmNo, int bormNo) {
    this.custNo = custNo;
    this.facmNo = facmNo;
    this.bormNo = bormNo;
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
	* 撥款序號, 預約序號<br>
	* 
	* @return Integer
	*/
  public int getBormNo() {
    return this.bormNo;
  }

/**
	* 撥款序號, 預約序號<br>
	* 
  *
  * @param bormNo 撥款序號, 預約序號
	*/
  public void setBormNo(int bormNo) {
    this.bormNo = bormNo;
  }


  @Override
  public int hashCode() {
    return Objects.hash(custNo, facmNo, bormNo);
  }

  @Override
  public boolean equals(Object obj) {
    if(this == obj)
      return true;
    if(obj == null || getClass() != obj.getClass())
      return false;
    LoanBorMainId loanBorMainId = (LoanBorMainId) obj;
    return custNo == loanBorMainId.custNo && facmNo == loanBorMainId.facmNo && bormNo == loanBorMainId.bormNo;
  }

  @Override
  public String toString() {
    return "LoanBorMainId [custNo=" + custNo + ", facmNo=" + facmNo + ", bormNo=" + bormNo + "]";
  }
}
