package com.st1.itx.db.domain;

import java.io.Serializable;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 * Ias39LoanCommit IAS39放款承諾明細檔<br>
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
@Embeddable
public class Ias39LoanCommitId implements Serializable {


  /**
	 * 
	 */
	private static final long serialVersionUID = 6828899719749682818L;

// 年月份
  @Column(name = "`DataYm`")
  private int dataYm = 0;

  // 戶號
  @Column(name = "`CustNo`")
  private int custNo = 0;

  // 額度編號
  @Column(name = "`FacmNo`")
  private int facmNo = 0;

  // 核准號碼
  @Column(name = "`ApplNo`")
  private int applNo = 0;

  public Ias39LoanCommitId() {
  }

  public Ias39LoanCommitId(int dataYm, int custNo, int facmNo, int applNo) {
    this.dataYm = dataYm;
    this.custNo = custNo;
    this.facmNo = facmNo;
    this.applNo = applNo;
  }

/**
	* 年月份<br>
	* 
	* @return Integer
	*/
  public int getDataYm() {
    return this.dataYm;
  }

/**
	* 年月份<br>
	* 
  *
  * @param dataYm 年月份
	*/
  public void setDataYm(int dataYm) {
    this.dataYm = dataYm;
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
	* 核准號碼<br>
	* 
	* @return Integer
	*/
  public int getApplNo() {
    return this.applNo;
  }

/**
	* 核准號碼<br>
	* 
  *
  * @param applNo 核准號碼
	*/
  public void setApplNo(int applNo) {
    this.applNo = applNo;
  }


  @Override
  public int hashCode() {
    return Objects.hash(dataYm, custNo, facmNo, applNo);
  }

  @Override
  public boolean equals(Object obj) {
    if(this == obj)
      return true;
    if(obj == null || getClass() != obj.getClass())
      return false;
    Ias39LoanCommitId ias39LoanCommitId = (Ias39LoanCommitId) obj;
    return dataYm == ias39LoanCommitId.dataYm && custNo == ias39LoanCommitId.custNo && facmNo == ias39LoanCommitId.facmNo && applNo == ias39LoanCommitId.applNo;
  }

  @Override
  public String toString() {
    return "Ias39LoanCommitId [dataYm=" + dataYm + ", custNo=" + custNo + ", facmNo=" + facmNo + ", applNo=" + applNo + "]";
  }
}
