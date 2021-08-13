package com.st1.itx.db.domain;

import java.io.Serializable;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 * LoanSyndItem 聯貸案動撥條件檔<br>
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
@Embeddable
public class LoanSyndItemId implements Serializable {


  /**
	 * 
	 */
	private static final long serialVersionUID = -7873746275746543495L;

// 借款人戶號
  @Column(name = "`CustNo`")
  private int custNo = 0;

  // 聯貸案序號
  @Column(name = "`SyndNo`")
  private int syndNo = 0;

  // 項別
  @Column(name = "`Item`", length = 10)
  private String item = " ";

  public LoanSyndItemId() {
  }

  public LoanSyndItemId(int custNo, int syndNo, String item) {
    this.custNo = custNo;
    this.syndNo = syndNo;
    this.item = item;
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
	* 聯貸案序號<br>
	* 
	* @return Integer
	*/
  public int getSyndNo() {
    return this.syndNo;
  }

/**
	* 聯貸案序號<br>
	* 
  *
  * @param syndNo 聯貸案序號
	*/
  public void setSyndNo(int syndNo) {
    this.syndNo = syndNo;
  }

/**
	* 項別<br>
	* 
	* @return String
	*/
  public String getItem() {
    return this.item == null ? "" : this.item;
  }

/**
	* 項別<br>
	* 
  *
  * @param item 項別
	*/
  public void setItem(String item) {
    this.item = item;
  }


  @Override
  public int hashCode() {
    return Objects.hash(custNo, syndNo, item);
  }

  @Override
  public boolean equals(Object obj) {
    if(this == obj)
      return true;
    if(obj == null || getClass() != obj.getClass())
      return false;
    LoanSyndItemId loanSyndItemId = (LoanSyndItemId) obj;
    return custNo == loanSyndItemId.custNo && syndNo == loanSyndItemId.syndNo && item.equals(loanSyndItemId.item);
  }

  @Override
  public String toString() {
    return "LoanSyndItemId [custNo=" + custNo + ", syndNo=" + syndNo + ", item=" + item + "]";
  }
}
