package com.st1.itx.db.domain;

import java.io.Serializable;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 * ClOwnerRelation 擔保品所有權人與授信戶關係檔<br>
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
@Embeddable
public class ClOwnerRelationId implements Serializable {


  /**
	 * 
	 */
	private static final long serialVersionUID = -7931573753819359578L;

// 案件編號
  /* 徵審系統案號(eLoan案件編號) */
  @Column(name = "`CreditSysNo`")
  private int creditSysNo = 0;

  // 借款人戶號
  @Column(name = "`CustNo`")
  private int custNo = 0;

  // 客戶識別碼
  @Column(name = "`OwnerCustUKey`", length = 32)
  private String ownerCustUKey = " ";

  public ClOwnerRelationId() {
  }

  public ClOwnerRelationId(int creditSysNo, int custNo, String ownerCustUKey) {
    this.creditSysNo = creditSysNo;
    this.custNo = custNo;
    this.ownerCustUKey = ownerCustUKey;
  }

/**
	* 案件編號<br>
	* 徵審系統案號(eLoan案件編號)
	* @return Integer
	*/
  public int getCreditSysNo() {
    return this.creditSysNo;
  }

/**
	* 案件編號<br>
	* 徵審系統案號(eLoan案件編號)
  *
  * @param creditSysNo 案件編號
	*/
  public void setCreditSysNo(int creditSysNo) {
    this.creditSysNo = creditSysNo;
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
	* 客戶識別碼<br>
	* 
	* @return String
	*/
  public String getOwnerCustUKey() {
    return this.ownerCustUKey == null ? "" : this.ownerCustUKey;
  }

/**
	* 客戶識別碼<br>
	* 
  *
  * @param ownerCustUKey 客戶識別碼
	*/
  public void setOwnerCustUKey(String ownerCustUKey) {
    this.ownerCustUKey = ownerCustUKey;
  }


  @Override
  public int hashCode() {
    return Objects.hash(creditSysNo, custNo, ownerCustUKey);
  }

  @Override
  public boolean equals(Object obj) {
    if(this == obj)
      return true;
    if(obj == null || getClass() != obj.getClass())
      return false;
    ClOwnerRelationId clOwnerRelationId = (ClOwnerRelationId) obj;
    return creditSysNo == clOwnerRelationId.creditSysNo && custNo == clOwnerRelationId.custNo && ownerCustUKey.equals(clOwnerRelationId.ownerCustUKey);
  }

  @Override
  public String toString() {
    return "ClOwnerRelationId [creditSysNo=" + creditSysNo + ", custNo=" + custNo + ", ownerCustUKey=" + ownerCustUKey + "]";
  }
}
