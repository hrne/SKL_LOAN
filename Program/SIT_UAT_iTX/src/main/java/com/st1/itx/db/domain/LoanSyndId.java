package com.st1.itx.db.domain;

import java.io.Serializable;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 * LoanSynd 聯貸案訂約檔<br>
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
@Embeddable
public class LoanSyndId implements Serializable {


  /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

// 借款人戶號
  @Column(name = "`CustNo`")
  private int custNo = 0;

  // 聯貸編號
  @Column(name = "`SyndNo`")
  private int syndNo = 0;

  public LoanSyndId() {
  }

  public LoanSyndId(int custNo, int syndNo) {
    this.custNo = custNo;
    this.syndNo = syndNo;
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
	* 聯貸編號<br>
	* 
	* @return Integer
	*/
  public int getSyndNo() {
    return this.syndNo;
  }

/**
	* 聯貸編號<br>
	* 
  *
  * @param syndNo 聯貸編號
	*/
  public void setSyndNo(int syndNo) {
    this.syndNo = syndNo;
  }


  @Override
  public int hashCode() {
    return Objects.hash(custNo, syndNo);
  }

  @Override
  public boolean equals(Object obj) {
    if(this == obj)
      return true;
    if(obj == null || getClass() != obj.getClass())
      return false;
    LoanSyndId loanSyndId = (LoanSyndId) obj;
    return custNo == loanSyndId.custNo && syndNo == loanSyndId.syndNo;
  }

  @Override
  public String toString() {
    return "LoanSyndId [custNo=" + custNo + ", syndNo=" + syndNo + "]";
  }
}
