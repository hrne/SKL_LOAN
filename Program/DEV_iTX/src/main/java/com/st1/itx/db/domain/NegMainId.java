package com.st1.itx.db.domain;

import java.io.Serializable;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 * NegMain 債務協商案件主檔<br>
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
@Embeddable
public class NegMainId implements Serializable {


  /**
	 * 
	 */
	private static final long serialVersionUID = 4897551107033446749L;

// 戶號
  /* 保貸戶須建立客戶主檔 */
  @Column(name = "`CustNo`")
  private int custNo = 0;

  // 案件序號
  @Column(name = "`CaseSeq`")
  private int caseSeq = 0;

  public NegMainId() {
  }

  public NegMainId(int custNo, int caseSeq) {
    this.custNo = custNo;
    this.caseSeq = caseSeq;
  }

/**
	* 戶號<br>
	* 保貸戶須建立客戶主檔
	* @return Integer
	*/
  public int getCustNo() {
    return this.custNo;
  }

/**
	* 戶號<br>
	* 保貸戶須建立客戶主檔
  *
  * @param custNo 戶號
	*/
  public void setCustNo(int custNo) {
    this.custNo = custNo;
  }

/**
	* 案件序號<br>
	* 
	* @return Integer
	*/
  public int getCaseSeq() {
    return this.caseSeq;
  }

/**
	* 案件序號<br>
	* 
  *
  * @param caseSeq 案件序號
	*/
  public void setCaseSeq(int caseSeq) {
    this.caseSeq = caseSeq;
  }


  @Override
  public int hashCode() {
    return Objects.hash(custNo, caseSeq);
  }

  @Override
  public boolean equals(Object obj) {
    if(this == obj)
      return true;
    if(obj == null || getClass() != obj.getClass())
      return false;
    NegMainId negMainId = (NegMainId) obj;
    return custNo == negMainId.custNo && caseSeq == negMainId.caseSeq;
  }

  @Override
  public String toString() {
    return "NegMainId [custNo=" + custNo + ", caseSeq=" + caseSeq + "]";
  }
}
