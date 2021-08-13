package com.st1.itx.db.domain;

import java.io.Serializable;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import com.st1.itx.util.StaticTool;
import com.st1.itx.Exception.LogicException;

/**
 * BatxCheque 支票兌現檔<br>
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
@Embeddable
public class BatxChequeId implements Serializable {


  /**
	 * 
	 */
	private static final long serialVersionUID = 2336416810747613453L;

// 會計日期
  @Column(name = "`AcDate`")
  private int acDate = 0;

  // 批號
  @Column(name = "`BatchNo`", length = 6)
  private String batchNo = " ";

  // 支票帳號
  @Column(name = "`ChequeAcct`", length = 9)
  private String chequeAcct = " ";

  // 支票號碼
  @Column(name = "`ChequeNo`", length = 7)
  private String chequeNo = " ";

  public BatxChequeId() {
  }

  public BatxChequeId(int acDate, String batchNo, String chequeAcct, String chequeNo) {
    this.acDate = acDate;
    this.batchNo = batchNo;
    this.chequeAcct = chequeAcct;
    this.chequeNo = chequeNo;
  }

/**
	* 會計日期<br>
	* 
	* @return Integer
	*/
  public int getAcDate() {
    return  StaticTool.bcToRoc(this.acDate);
  }

/**
	* 會計日期<br>
	* 
  *
  * @param acDate 會計日期
  * @throws LogicException when Date Is Warn	*/
  public void setAcDate(int acDate) throws LogicException {
    this.acDate = StaticTool.rocToBc(acDate);
  }

/**
	* 批號<br>
	* 
	* @return String
	*/
  public String getBatchNo() {
    return this.batchNo == null ? "" : this.batchNo;
  }

/**
	* 批號<br>
	* 
  *
  * @param batchNo 批號
	*/
  public void setBatchNo(String batchNo) {
    this.batchNo = batchNo;
  }

/**
	* 支票帳號<br>
	* 
	* @return String
	*/
  public String getChequeAcct() {
    return this.chequeAcct == null ? "" : this.chequeAcct;
  }

/**
	* 支票帳號<br>
	* 
  *
  * @param chequeAcct 支票帳號
	*/
  public void setChequeAcct(String chequeAcct) {
    this.chequeAcct = chequeAcct;
  }

/**
	* 支票號碼<br>
	* 
	* @return String
	*/
  public String getChequeNo() {
    return this.chequeNo == null ? "" : this.chequeNo;
  }

/**
	* 支票號碼<br>
	* 
  *
  * @param chequeNo 支票號碼
	*/
  public void setChequeNo(String chequeNo) {
    this.chequeNo = chequeNo;
  }


  @Override
  public int hashCode() {
    return Objects.hash(acDate, batchNo, chequeAcct, chequeNo);
  }

  @Override
  public boolean equals(Object obj) {
    if(this == obj)
      return true;
    if(obj == null || getClass() != obj.getClass())
      return false;
    BatxChequeId batxChequeId = (BatxChequeId) obj;
    return acDate == batxChequeId.acDate && batchNo.equals(batxChequeId.batchNo) && chequeAcct.equals(batxChequeId.chequeAcct) && chequeNo.equals(batxChequeId.chequeNo);
  }

  @Override
  public String toString() {
    return "BatxChequeId [acDate=" + acDate + ", batchNo=" + batchNo + ", chequeAcct=" + chequeAcct + ", chequeNo=" + chequeNo + "]";
  }
}
