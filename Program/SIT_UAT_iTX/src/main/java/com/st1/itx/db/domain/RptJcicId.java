package com.st1.itx.db.domain;

import java.io.Serializable;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 * RptJcic 報表Jcic<br>
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
@Embeddable
public class RptJcicId implements Serializable {


  /**
	 * 
	 */
	private static final long serialVersionUID = -4856498499077774309L;

// 單位別
  @Column(name = "`BranchNo`", length = 4)
  private String branchNo = " ";

  // 戶號
  @Column(name = "`CustNo`")
  private int custNo = 0;

  // 額度號碼
  @Column(name = "`FacmNo`")
  private int facmNo = 0;

  public RptJcicId() {
  }

  public RptJcicId(String branchNo, int custNo, int facmNo) {
    this.branchNo = branchNo;
    this.custNo = custNo;
    this.facmNo = facmNo;
  }

/**
	* 單位別<br>
	* 
	* @return String
	*/
  public String getBranchNo() {
    return this.branchNo == null ? "" : this.branchNo;
  }

/**
	* 單位別<br>
	* 
  *
  * @param branchNo 單位別
	*/
  public void setBranchNo(String branchNo) {
    this.branchNo = branchNo;
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
	* 額度號碼<br>
	* 
	* @return Integer
	*/
  public int getFacmNo() {
    return this.facmNo;
  }

/**
	* 額度號碼<br>
	* 
  *
  * @param facmNo 額度號碼
	*/
  public void setFacmNo(int facmNo) {
    this.facmNo = facmNo;
  }


  @Override
  public int hashCode() {
    return Objects.hash(branchNo, custNo, facmNo);
  }

  @Override
  public boolean equals(Object obj) {
    if(this == obj)
      return true;
    if(obj == null || getClass() != obj.getClass())
      return false;
    RptJcicId rptJcicId = (RptJcicId) obj;
    return branchNo.equals(rptJcicId.branchNo) && custNo == rptJcicId.custNo && facmNo == rptJcicId.facmNo;
  }

  @Override
  public String toString() {
    return "RptJcicId [branchNo=" + branchNo + ", custNo=" + custNo + ", facmNo=" + facmNo + "]";
  }
}
