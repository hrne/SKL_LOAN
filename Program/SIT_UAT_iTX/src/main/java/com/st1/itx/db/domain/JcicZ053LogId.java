package com.st1.itx.db.domain;

import java.io.Serializable;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 * JcicZ053Log 同意報送例外處理檔案<br>
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
@Embeddable
public class JcicZ053LogId implements Serializable {


  /**
	 * 
	 */
	private static final long serialVersionUID = -6202428684932595759L;

// 流水號
  @Column(name = "`Ukey`", length = 32)
  private String ukey = " ";

  // 交易序號
  @Column(name = "`TxSeq`", length = 18)
  private String txSeq = " ";

  public JcicZ053LogId() {
  }

  public JcicZ053LogId(String ukey, String txSeq) {
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
    JcicZ053LogId jcicZ053LogId = (JcicZ053LogId) obj;
    return ukey.equals(jcicZ053LogId.ukey) && txSeq.equals(jcicZ053LogId.txSeq);
  }

  @Override
  public String toString() {
    return "JcicZ053LogId [ukey=" + ukey + ", txSeq=" + txSeq + "]";
  }
}
