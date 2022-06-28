package com.st1.itx.db.domain;

import java.io.Serializable;
import java.util.Objects;
import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import com.st1.itx.util.StaticTool;
import com.st1.itx.Exception.LogicException;

/**
 * AcLoanRenew 會計借新還舊檔<br>
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
@Embeddable
public class AcLoanRenewId implements Serializable {


  // 戶號
  @Column(name = "`CustNo`")
  private int custNo = 0;

  // 新額度編號
  @Column(name = "`NewFacmNo`")
  private int newFacmNo = 0;

  // 新撥款序號
  @Column(name = "`NewBormNo`")
  private int newBormNo = 0;

  // 舊額度編號
  @Column(name = "`OldFacmNo`")
  private int oldFacmNo = 0;

  // 舊撥款序號
  @Column(name = "`OldBormNo`")
  private int oldBormNo = 0;

  public AcLoanRenewId() {
  }

  public AcLoanRenewId(int custNo, int newFacmNo, int newBormNo, int oldFacmNo, int oldBormNo) {
    this.custNo = custNo;
    this.newFacmNo = newFacmNo;
    this.newBormNo = newBormNo;
    this.oldFacmNo = oldFacmNo;
    this.oldBormNo = oldBormNo;
  }

/**
	* 戶號<br>
	* 
	* @return Integer
	*/
  public int getCustNo() {
    return this.custNo;
  }

/**
	* 戶號<br>
	* 
  *
  * @param custNo 戶號
	*/
  public void setCustNo(int custNo) {
    this.custNo = custNo;
  }

/**
	* 新額度編號<br>
	* 
	* @return Integer
	*/
  public int getNewFacmNo() {
    return this.newFacmNo;
  }

/**
	* 新額度編號<br>
	* 
  *
  * @param newFacmNo 新額度編號
	*/
  public void setNewFacmNo(int newFacmNo) {
    this.newFacmNo = newFacmNo;
  }

/**
	* 新撥款序號<br>
	* 
	* @return Integer
	*/
  public int getNewBormNo() {
    return this.newBormNo;
  }

/**
	* 新撥款序號<br>
	* 
  *
  * @param newBormNo 新撥款序號
	*/
  public void setNewBormNo(int newBormNo) {
    this.newBormNo = newBormNo;
  }

/**
	* 舊額度編號<br>
	* 
	* @return Integer
	*/
  public int getOldFacmNo() {
    return this.oldFacmNo;
  }

/**
	* 舊額度編號<br>
	* 
  *
  * @param oldFacmNo 舊額度編號
	*/
  public void setOldFacmNo(int oldFacmNo) {
    this.oldFacmNo = oldFacmNo;
  }

/**
	* 舊撥款序號<br>
	* 
	* @return Integer
	*/
  public int getOldBormNo() {
    return this.oldBormNo;
  }

/**
	* 舊撥款序號<br>
	* 
  *
  * @param oldBormNo 舊撥款序號
	*/
  public void setOldBormNo(int oldBormNo) {
    this.oldBormNo = oldBormNo;
  }


  @Override
  public int hashCode() {
    return Objects.hash(custNo, newFacmNo, newBormNo, oldFacmNo, oldBormNo);
  }

  @Override
  public boolean equals(Object obj) {
    if(this == obj)
      return true;
    if(obj == null || getClass() != obj.getClass())
      return false;
    AcLoanRenewId acLoanRenewId = (AcLoanRenewId) obj;
    return custNo == acLoanRenewId.custNo && newFacmNo == acLoanRenewId.newFacmNo && newBormNo == acLoanRenewId.newBormNo && oldFacmNo == acLoanRenewId.oldFacmNo && oldBormNo == acLoanRenewId.oldBormNo;
  }

  @Override
  public String toString() {
    return "AcLoanRenewId [custNo=" + custNo + ", newFacmNo=" + newFacmNo + ", newBormNo=" + newBormNo + ", oldFacmNo=" + oldFacmNo + ", oldBormNo=" + oldBormNo + "]";
  }
}
