package com.st1.itx.db.domain;

import java.io.Serializable;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 * LoanCheque 支票檔<br>
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
@Embeddable
public class LoanChequeId implements Serializable {


  /**
	 * 
	 */
	private static final long serialVersionUID = -1232599286754954089L;

// 支票帳號
  @Column(name = "`ChequeAcct`")
  private int chequeAcct = 0;

  // 支票號碼
  @Column(name = "`ChequeNo`")
  private int chequeNo = 0;

  public LoanChequeId() {
  }

  public LoanChequeId(int chequeAcct, int chequeNo) {
    this.chequeAcct = chequeAcct;
    this.chequeNo = chequeNo;
  }

/**
	* 支票帳號<br>
	* 
	* @return Integer
	*/
  public int getChequeAcct() {
    return this.chequeAcct;
  }

/**
	* 支票帳號<br>
	* 
  *
  * @param chequeAcct 支票帳號
	*/
  public void setChequeAcct(int chequeAcct) {
    this.chequeAcct = chequeAcct;
  }

/**
	* 支票號碼<br>
	* 
	* @return Integer
	*/
  public int getChequeNo() {
    return this.chequeNo;
  }

/**
	* 支票號碼<br>
	* 
  *
  * @param chequeNo 支票號碼
	*/
  public void setChequeNo(int chequeNo) {
    this.chequeNo = chequeNo;
  }


  @Override
  public int hashCode() {
    return Objects.hash(chequeAcct, chequeNo);
  }

  @Override
  public boolean equals(Object obj) {
    if(this == obj)
      return true;
    if(obj == null || getClass() != obj.getClass())
      return false;
    LoanChequeId loanChequeId = (LoanChequeId) obj;
    return chequeAcct == loanChequeId.chequeAcct && chequeNo == loanChequeId.chequeNo;
  }

  @Override
  public String toString() {
    return "LoanChequeId [chequeAcct=" + chequeAcct + ", chequeNo=" + chequeNo + "]";
  }
}
