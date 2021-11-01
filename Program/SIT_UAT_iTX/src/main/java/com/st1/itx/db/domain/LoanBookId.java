package com.st1.itx.db.domain;

import java.io.Serializable;
import java.util.Objects;
import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import com.st1.itx.util.StaticTool;
import com.st1.itx.Exception.LogicException;

/**
 * LoanBook 放款約定還本檔<br>
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
@Embeddable
public class LoanBookId implements Serializable {


  // 借款人戶號
  @Column(name = "`CustNo`")
  private int custNo = 0;

  // 額度編號
  @Column(name = "`FacmNo`")
  private int facmNo = 0;

  // 撥款序號
  @Column(name = "`BormNo`")
  private int bormNo = 0;

  // 約定還本日期
  @Column(name = "`BookDate`")
  private int bookDate = 0;

  public LoanBookId() {
  }

  public LoanBookId(int custNo, int facmNo, int bormNo, int bookDate) {
    this.custNo = custNo;
    this.facmNo = facmNo;
    this.bormNo = bormNo;
    this.bookDate = bookDate;
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
    return  StaticTool.bcToRoc(this.bookDate);
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


  @Override
  public int hashCode() {
    return Objects.hash(custNo, facmNo, bormNo, bookDate);
  }

  @Override
  public boolean equals(Object obj) {
    if(this == obj)
      return true;
    if(obj == null || getClass() != obj.getClass())
      return false;
    LoanBookId loanBookId = (LoanBookId) obj;
    return custNo == loanBookId.custNo && facmNo == loanBookId.facmNo && bormNo == loanBookId.bormNo && bookDate == loanBookId.bookDate;
  }

  @Override
  public String toString() {
    return "LoanBookId [custNo=" + custNo + ", facmNo=" + facmNo + ", bormNo=" + bormNo + ", bookDate=" + bookDate + "]";
  }
}
