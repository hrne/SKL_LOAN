package com.st1.itx.db.domain;

import java.io.Serializable;
import java.util.Objects;
import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import com.st1.itx.util.StaticTool;
import com.st1.itx.Exception.LogicException;

/**
 * TxRecord 交易記錄檔<br>
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
@Embeddable
public class TxRecordId implements Serializable {


  // 帳務日
  @Column(name = "`Entdy`")
  private int entdy = 0;

  // 交易編號(單位+使用者編號+交易序號)
  @Column(name = "`TxNo`", length = 18)
  private String txNo = " ";

  public TxRecordId() {
  }

  public TxRecordId(int entdy, String txNo) {
    this.entdy = entdy;
    this.txNo = txNo;
  }

/**
	* 帳務日<br>
	* 
	* @return Integer
	*/
  public int getEntdy() {
    return  StaticTool.bcToRoc(this.entdy);
  }

/**
	* 帳務日<br>
	* 
  *
  * @param entdy 帳務日
  * @throws LogicException when Date Is Warn	*/
  public void setEntdy(int entdy) throws LogicException {
    this.entdy = StaticTool.rocToBc(entdy);
  }

/**
	* 交易編號(單位+使用者編號+交易序號)<br>
	* 
	* @return String
	*/
  public String getTxNo() {
    return this.txNo == null ? "" : this.txNo;
  }

/**
	* 交易編號(單位+使用者編號+交易序號)<br>
	* 
  *
  * @param txNo 交易編號(單位+使用者編號+交易序號)
	*/
  public void setTxNo(String txNo) {
    this.txNo = txNo;
  }


  @Override
  public int hashCode() {
    return Objects.hash(entdy, txNo);
  }

  @Override
  public boolean equals(Object obj) {
    if(this == obj)
      return true;
    if(obj == null || getClass() != obj.getClass())
      return false;
    TxRecordId txRecordId = (TxRecordId) obj;
    return entdy == txRecordId.entdy && txNo.equals(txRecordId.txNo);
  }

  @Override
  public String toString() {
    return "TxRecordId [entdy=" + entdy + ", txNo=" + txNo + "]";
  }
}
