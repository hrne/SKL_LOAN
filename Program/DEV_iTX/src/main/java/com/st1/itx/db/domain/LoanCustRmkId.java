package com.st1.itx.db.domain;

import java.io.Serializable;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 * LoanCustRmk 帳務備忘錄明細檔<br>
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
@Embeddable
public class LoanCustRmkId implements Serializable {


  /**
	 * 
	 */
	private static final long serialVersionUID = -791701434723623882L;

// 借款人戶號
  @Column(name = "`CustNo`")
  private int custNo = 0;

  // 備忘錄序號
  @Column(name = "`RmkNo`")
  private int rmkNo = 0;

  public LoanCustRmkId() {
  }

  public LoanCustRmkId(int custNo, int rmkNo) {
    this.custNo = custNo;
    this.rmkNo = rmkNo;
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
	* 備忘錄序號<br>
	* 
	* @return Integer
	*/
  public int getRmkNo() {
    return this.rmkNo;
  }

/**
	* 備忘錄序號<br>
	* 
  *
  * @param rmkNo 備忘錄序號
	*/
  public void setRmkNo(int rmkNo) {
    this.rmkNo = rmkNo;
  }


  @Override
  public int hashCode() {
    return Objects.hash(custNo, rmkNo);
  }

  @Override
  public boolean equals(Object obj) {
    if(this == obj)
      return true;
    if(obj == null || getClass() != obj.getClass())
      return false;
    LoanCustRmkId loanCustRmkId = (LoanCustRmkId) obj;
    return custNo == loanCustRmkId.custNo && rmkNo == loanCustRmkId.rmkNo;
  }

  @Override
  public String toString() {
    return "LoanCustRmkId [custNo=" + custNo + ", rmkNo=" + rmkNo + "]";
  }
}
