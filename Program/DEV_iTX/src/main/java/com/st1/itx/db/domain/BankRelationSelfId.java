package com.st1.itx.db.domain;

import java.io.Serializable;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 * BankRelationSelf 金控利害關係人_關係人員工資料<br>
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
@Embeddable
public class BankRelationSelfId implements Serializable {


  /**
	 * 
	 */
	private static final long serialVersionUID = 6859834181329598021L;

// 借款戶所屬公司名稱
  @Column(name = "`CustName`", length = 70)
  private String custName = " ";

  // 借款戶統編
  @Column(name = "`CustId`", length = 11)
  private String custId = " ";

  public BankRelationSelfId() {
  }

  public BankRelationSelfId(String custName, String custId) {
    this.custName = custName;
    this.custId = custId;
  }

/**
	* 借款戶所屬公司名稱<br>
	* 
	* @return String
	*/
  public String getCustName() {
    return this.custName == null ? "" : this.custName;
  }

/**
	* 借款戶所屬公司名稱<br>
	* 
  *
  * @param custName 借款戶所屬公司名稱
	*/
  public void setCustName(String custName) {
    this.custName = custName;
  }

/**
	* 借款戶統編<br>
	* 
	* @return String
	*/
  public String getCustId() {
    return this.custId == null ? "" : this.custId;
  }

/**
	* 借款戶統編<br>
	* 
  *
  * @param custId 借款戶統編
	*/
  public void setCustId(String custId) {
    this.custId = custId;
  }


  @Override
  public int hashCode() {
    return Objects.hash(custName, custId);
  }

  @Override
  public boolean equals(Object obj) {
    if(this == obj)
      return true;
    if(obj == null || getClass() != obj.getClass())
      return false;
    BankRelationSelfId bankRelationSelfId = (BankRelationSelfId) obj;
    return custName.equals(bankRelationSelfId.custName) && custId.equals(bankRelationSelfId.custId);
  }

  @Override
  public String toString() {
    return "BankRelationSelfId [custName=" + custName + ", custId=" + custId + "]";
  }
}
