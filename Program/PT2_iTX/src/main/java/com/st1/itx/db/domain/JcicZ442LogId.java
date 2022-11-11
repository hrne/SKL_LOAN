package com.st1.itx.db.domain;

import java.io.Serializable;
import java.util.Objects;
import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import com.st1.itx.util.StaticTool;
import com.st1.itx.Exception.LogicException;

/**
 * JcicZ442Log 前置調解回報無擔保債權金額資料<br>
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
@Embeddable
public class JcicZ442LogId implements Serializable {


  // 流水號
  @Column(name = "`Ukey`", length = 32)
  private String ukey = " ";

  // 交易序號
  @Column(name = "`TxSeq`", length = 18)
  private String txSeq = " ";

  public JcicZ442LogId() {
  }

  public JcicZ442LogId(String ukey, String txSeq) {
    this.ukey = ukey;
    this.txSeq = txSeq;
  }

/**
	* 流水號<br>
	* 
	* @return String
	*/
  public String getUkey() {
    return this.ukey == null ? "" : this.ukey;
  }

/**
	* 流水號<br>
	* 
  *
  * @param ukey 流水號
	*/
  public void setUkey(String ukey) {
    this.ukey = ukey;
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


  @Override
  public int hashCode() {
    return Objects.hash(ukey, txSeq);
  }

  @Override
  public boolean equals(Object obj) {
    if(this == obj)
      return true;
    if(obj == null || getClass() != obj.getClass())
      return false;
    JcicZ442LogId jcicZ442LogId = (JcicZ442LogId) obj;
    return ukey.equals(jcicZ442LogId.ukey) && txSeq.equals(jcicZ442LogId.txSeq);
  }

  @Override
  public String toString() {
    return "JcicZ442LogId [ukey=" + ukey + ", txSeq=" + txSeq + "]";
  }
}
