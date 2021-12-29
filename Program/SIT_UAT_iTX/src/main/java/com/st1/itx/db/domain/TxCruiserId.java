package com.st1.itx.db.domain;

import java.io.Serializable;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 * TxCruiser 批次發動交易紀錄<br>
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
@Embeddable
public class TxCruiserId implements Serializable {


  /**
	 * 
	 */
	private static final long serialVersionUID = 6653767500545503713L;

// 交易序號
  @Column(name = "`TxSeq`", length = 20)
  private String txSeq = " ";

  // 發動經辦
  @Column(name = "`TlrNo`", length = 6)
  private String tlrNo = " ";

  public TxCruiserId() {
  }

  public TxCruiserId(String txSeq, String tlrNo) {
    this.txSeq = txSeq;
    this.tlrNo = tlrNo;
  }

/**
	* 交易序號<br>
	* 
	* @return String
	*/
  public String getTxSeq() {
    return this.txSeq == null ? "" : this.txSeq;
  }

/**
	* 交易序號<br>
	* 
  *
  * @param txSeq 交易序號
	*/
  public void setTxSeq(String txSeq) {
    this.txSeq = txSeq;
  }

/**
	* 發動經辦<br>
	* 
	* @return String
	*/
  public String getTlrNo() {
    return this.tlrNo == null ? "" : this.tlrNo;
  }

/**
	* 發動經辦<br>
	* 
  *
  * @param tlrNo 發動經辦
	*/
  public void setTlrNo(String tlrNo) {
    this.tlrNo = tlrNo;
  }


  @Override
  public int hashCode() {
    return Objects.hash(txSeq, tlrNo);
  }

  @Override
  public boolean equals(Object obj) {
    if(this == obj)
      return true;
    if(obj == null || getClass() != obj.getClass())
      return false;
    TxCruiserId txCruiserId = (TxCruiserId) obj;
    return txSeq.equals(txCruiserId.txSeq) && tlrNo.equals(txCruiserId.tlrNo);
  }

  @Override
  public String toString() {
    return "TxCruiserId [txSeq=" + txSeq + ", tlrNo=" + tlrNo + "]";
  }
}
