package com.st1.itx.db.domain;

import java.io.Serializable;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 * BankRelationCompany 金控利害關係人_關係企業資料<br>
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
@Embeddable
public class BankRelationCompanyId implements Serializable {


  /**
	 * 
	 */
	private static final long serialVersionUID = -2935508795297865463L;

// 借款戶所屬公司名稱
  @Column(name = "`CustName`", length = 70)
  private String custName = " ";

  // 借款戶統編/親屬統編
  @Column(name = "`CustId`", length = 11)
  private String custId = " ";

  // 關係企業統編
  @Column(name = "`CompanyId`", length = 11)
  private String companyId = " ";

  public BankRelationCompanyId() {
  }

  public BankRelationCompanyId(String custName, String custId, String companyId) {
    this.custName = custName;
    this.custId = custId;
    this.companyId = companyId;
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
	* 借款戶統編/親屬統編<br>
	* 
	* @return String
	*/
  public String getCustId() {
    return this.custId == null ? "" : this.custId;
  }

/**
	* 借款戶統編/親屬統編<br>
	* 
  *
  * @param custId 借款戶統編/親屬統編
	*/
  public void setCustId(String custId) {
    this.custId = custId;
  }

/**
	* 關係企業統編<br>
	* 
	* @return String
	*/
  public String getCompanyId() {
    return this.companyId == null ? "" : this.companyId;
  }

/**
	* 關係企業統編<br>
	* 
  *
  * @param companyId 關係企業統編
	*/
  public void setCompanyId(String companyId) {
    this.companyId = companyId;
  }


  @Override
  public int hashCode() {
    return Objects.hash(custName, custId, companyId);
  }

  @Override
  public boolean equals(Object obj) {
    if(this == obj)
      return true;
    if(obj == null || getClass() != obj.getClass())
      return false;
    BankRelationCompanyId bankRelationCompanyId = (BankRelationCompanyId) obj;
    return custName.equals(bankRelationCompanyId.custName) && custId.equals(bankRelationCompanyId.custId) && companyId.equals(bankRelationCompanyId.companyId);
  }

  @Override
  public String toString() {
    return "BankRelationCompanyId [custName=" + custName + ", custId=" + custId + ", companyId=" + companyId + "]";
  }
}
