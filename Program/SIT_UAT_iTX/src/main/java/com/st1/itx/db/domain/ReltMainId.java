package com.st1.itx.db.domain;

import java.io.Serializable;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 * ReltMain 借款戶關係人/關係企業主檔<br>
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
@Embeddable
public class ReltMainId implements Serializable {


  /**
	 * 
	 */
	private static final long serialVersionUID = 1830131390365117846L;

// eLoan案件編號
  @Column(name = "`CaseNo`")
  private int caseNo = 0;

  // 借戶人戶號
  @Column(name = "`CustNo`")
  private int custNo = 0;

  // 關係人客戶識別碼
  @Column(name = "`ReltUKey`", length = 32)
  private String reltUKey = " ";

  public ReltMainId() {
  }

  public ReltMainId(int caseNo, int custNo, String reltUKey) {
    this.caseNo = caseNo;
    this.custNo = custNo;
    this.reltUKey = reltUKey;
  }

/**
	* eLoan案件編號<br>
	* 
	* @return Integer
	*/
  public int getCaseNo() {
    return this.caseNo;
  }

/**
	* eLoan案件編號<br>
	* 
  *
  * @param caseNo eLoan案件編號
	*/
  public void setCaseNo(int caseNo) {
    this.caseNo = caseNo;
  }

/**
	* 借戶人戶號<br>
	* 
	* @return Integer
	*/
  public int getCustNo() {
    return this.custNo;
  }

/**
	* 借戶人戶號<br>
	* 
  *
  * @param custNo 借戶人戶號
	*/
  public void setCustNo(int custNo) {
    this.custNo = custNo;
  }

/**
	* 關係人客戶識別碼<br>
	* 
	* @return String
	*/
  public String getReltUKey() {
    return this.reltUKey == null ? "" : this.reltUKey;
  }

/**
	* 關係人客戶識別碼<br>
	* 
  *
  * @param reltUKey 關係人客戶識別碼
	*/
  public void setReltUKey(String reltUKey) {
    this.reltUKey = reltUKey;
  }


  @Override
  public int hashCode() {
    return Objects.hash(caseNo, custNo, reltUKey);
  }

  @Override
  public boolean equals(Object obj) {
    if(this == obj)
      return true;
    if(obj == null || getClass() != obj.getClass())
      return false;
    ReltMainId reltMainId = (ReltMainId) obj;
    return caseNo == reltMainId.caseNo && custNo == reltMainId.custNo && reltUKey.equals(reltMainId.reltUKey);
  }

  @Override
  public String toString() {
    return "ReltMainId [caseNo=" + caseNo + ", custNo=" + custNo + ", reltUKey=" + reltUKey + "]";
  }
}
