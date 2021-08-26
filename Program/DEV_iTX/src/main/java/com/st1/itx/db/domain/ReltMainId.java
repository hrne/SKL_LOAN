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
	private static final long serialVersionUID = -4561118716496303221L;

// eLoan案件編號
  @Column(name = "`CaseNo`")
  private int caseNo = 0;

  // 借戶人戶號
  @Column(name = "`CustNo`")
  private int custNo = 0;

  // 關係人身分證字號
  @Column(name = "`ReltId`", length = 10)
  private String reltId = " ";

  public ReltMainId() {
  }

  public ReltMainId(int caseNo, int custNo, String reltId) {
    this.caseNo = caseNo;
    this.custNo = custNo;
    this.reltId = reltId;
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
	* 關係人身分證字號<br>
	* 
	* @return String
	*/
  public String getReltId() {
    return this.reltId == null ? "" : this.reltId;
  }

/**
	* 關係人身分證字號<br>
	* 
  *
  * @param reltId 關係人身分證字號
	*/
  public void setReltId(String reltId) {
    this.reltId = reltId;
  }


  @Override
  public int hashCode() {
    return Objects.hash(caseNo, custNo, reltId);
  }

  @Override
  public boolean equals(Object obj) {
    if(this == obj)
      return true;
    if(obj == null || getClass() != obj.getClass())
      return false;
    ReltMainId reltMainId = (ReltMainId) obj;
    return caseNo == reltMainId.caseNo && custNo == reltMainId.custNo && reltId.equals(reltMainId.reltId);
  }

  @Override
  public String toString() {
    return "ReltMainId [caseNo=" + caseNo + ", custNo=" + custNo + ", reltId=" + reltId + "]";
  }
}
