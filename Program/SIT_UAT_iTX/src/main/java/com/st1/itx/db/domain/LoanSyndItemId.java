package com.st1.itx.db.domain;

import java.io.Serializable;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 * LoanSyndItem 聯貸案費用檔<br>
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
	private static final long serialVersionUID = -6747172815474963198L;

// 聯貸案編號
  @Column(name = "`SyndNo`")
  private int syndNo = 0;

  // 聯貸案序號
  @Column(name = "`SyndSeq`")
  private int syndSeq = 0;

  public LoanSyndItemId() {
  }

  public LoanSyndItemId(int syndNo, int syndSeq) {
    this.syndNo = syndNo;
    this.syndSeq = syndSeq;
  }

/**
	* 聯貸案編號<br>
	* 
	* @return Integer
	*/
  public int getSyndNo() {
    return this.syndNo;
  }

/**
	* 聯貸案編號<br>
	* 
  *
  * @param syndNo 聯貸案編號
	*/
  public void setSyndNo(int syndNo) {
    this.syndNo = syndNo;
  }

/**
	* 聯貸案序號<br>
	* 
	* @return Integer
	*/
  public int getSyndSeq() {
    return this.syndSeq;
  }

/**
	* 聯貸案序號<br>
	* 
  *
  * @param syndSeq 聯貸案序號
	*/
  public void setSyndSeq(int syndSeq) {
    this.syndSeq = syndSeq;
  }


  @Override
  public int hashCode() {
    return Objects.hash(syndNo, syndSeq);
  }

  @Override
  public boolean equals(Object obj) {
    if(this == obj)
      return true;
    if(obj == null || getClass() != obj.getClass())
      return false;
    LoanSyndItemId loanSyndItemId = (LoanSyndItemId) obj;
    return syndNo == loanSyndItemId.syndNo && syndSeq == loanSyndItemId.syndSeq;
  }

  @Override
  public String toString() {
    return "LoanSyndItemId [syndNo=" + syndNo + ", syndSeq=" + syndSeq + "]";
  }
}
