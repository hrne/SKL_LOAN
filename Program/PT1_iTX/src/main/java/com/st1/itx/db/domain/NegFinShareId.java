package com.st1.itx.db.domain;

import java.io.Serializable;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 * NegFinShare 債務協商債權分攤檔<br>
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
@Embeddable
public class NegFinShareId implements Serializable {


  // 債務人戶號
  /* 保貸戶須建立客戶主檔 */
  @Column(name = "`CustNo`")
  private int custNo = 0;

  // 案件序號
  @Column(name = "`CaseSeq`")
  private int caseSeq = 0;

  // 債權機構
  @Column(name = "`FinCode`", length = 8)
  private String finCode = " ";

  public NegFinShareId() {
  }

  public NegFinShareId(int custNo, int caseSeq, String finCode) {
    this.custNo = custNo;
    this.caseSeq = caseSeq;
    this.finCode = finCode;
  }

/**
	* 債務人戶號<br>
	* 保貸戶須建立客戶主檔
	* @return Integer
	*/
  public int getCustNo() {
    return this.custNo;
  }

/**
	* 債務人戶號<br>
	* 保貸戶須建立客戶主檔
  *
  * @param custNo 債務人戶號
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

/**
	* 債權機構<br>
	* 
	* @return String
	*/
  public String getFinCode() {
    return this.finCode == null ? "" : this.finCode;
  }

/**
	* 債權機構<br>
	* 
  *
  * @param finCode 債權機構
	*/
  public void setFinCode(String finCode) {
    this.finCode = finCode;
  }


  @Override
  public int hashCode() {
    return Objects.hash(custNo, caseSeq, finCode);
  }

  @Override
  public boolean equals(Object obj) {
    if(this == obj)
      return true;
    if(obj == null || getClass() != obj.getClass())
      return false;
    NegFinShareId negFinShareId = (NegFinShareId) obj;
    return custNo == negFinShareId.custNo && caseSeq == negFinShareId.caseSeq && finCode.equals(negFinShareId.finCode);
  }

  @Override
  public String toString() {
    return "NegFinShareId [custNo=" + custNo + ", caseSeq=" + caseSeq + ", finCode=" + finCode + "]";
  }
}
