package com.st1.itx.db.domain;

import java.io.Serializable;
import java.util.Objects;
import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import com.st1.itx.util.StaticTool;
import com.st1.itx.Exception.LogicException;

/**
 * JcicZ050 債務人繳款資料檔案<br>
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
@Embeddable
public class JcicZ050Id implements Serializable {


  // 債務人IDN
  @Column(name = "`CustId`", length = 10)
  private String custId = " ";

  // 協商申請日
  @Column(name = "`RcDate`")
  private int rcDate = 0;

  // 繳款日期
  /* 債務人實際繳款日期;若當日有多次還款，應累計當日繳款金額合併報送，不同繳款日期，應分別報送繳款紀錄。 */
  @Column(name = "`PayDate`")
  private int payDate = 0;

  // 報送單位代號
  /* 三位文數字 */
  @Column(name = "`SubmitKey`", length = 3)
  private String submitKey = " ";

  public JcicZ050Id() {
  }

  public JcicZ050Id(String custId, int rcDate, int payDate, String submitKey) {
    this.custId = custId;
    this.rcDate = rcDate;
    this.payDate = payDate;
    this.submitKey = submitKey;
  }

/**
	* 債務人IDN<br>
	* 
	* @return String
	*/
  public String getCustId() {
    return this.custId == null ? "" : this.custId;
  }

/**
	* 債務人IDN<br>
	* 
  *
  * @param custId 債務人IDN
	*/
  public void setCustId(String custId) {
    this.custId = custId;
  }

/**
	* 協商申請日<br>
	* 
	* @return Integer
	*/
  public int getRcDate() {
    return  StaticTool.bcToRoc(this.rcDate);
  }

/**
	* 協商申請日<br>
	* 
  *
  * @param rcDate 協商申請日
  * @throws LogicException when Date Is Warn	*/
  public void setRcDate(int rcDate) throws LogicException {
    this.rcDate = StaticTool.rocToBc(rcDate);
  }

/**
	* 繳款日期<br>
	* 債務人實際繳款日期;
若當日有多次還款，應累計當日繳款金額合併報送，不同繳款日期，應分別報送繳款紀錄。
	* @return Integer
	*/
  public int getPayDate() {
    return  StaticTool.bcToRoc(this.payDate);
  }

/**
	* 繳款日期<br>
	* 債務人實際繳款日期;
若當日有多次還款，應累計當日繳款金額合併報送，不同繳款日期，應分別報送繳款紀錄。
  *
  * @param payDate 繳款日期
  * @throws LogicException when Date Is Warn	*/
  public void setPayDate(int payDate) throws LogicException {
    this.payDate = StaticTool.rocToBc(payDate);
  }

/**
	* 報送單位代號<br>
	* 三位文數字
	* @return String
	*/
  public String getSubmitKey() {
    return this.submitKey == null ? "" : this.submitKey;
  }

/**
	* 報送單位代號<br>
	* 三位文數字
  *
  * @param submitKey 報送單位代號
	*/
  public void setSubmitKey(String submitKey) {
    this.submitKey = submitKey;
  }


  @Override
  public int hashCode() {
    return Objects.hash(custId, rcDate, payDate, submitKey);
  }

  @Override
  public boolean equals(Object obj) {
    if(this == obj)
      return true;
    if(obj == null || getClass() != obj.getClass())
      return false;
    JcicZ050Id jcicZ050Id = (JcicZ050Id) obj;
    return custId.equals(jcicZ050Id.custId) && rcDate == jcicZ050Id.rcDate && payDate == jcicZ050Id.payDate && submitKey == jcicZ050Id.submitKey;
  }

  @Override
  public String toString() {
    return "JcicZ050Id [custId=" + custId + ", rcDate=" + rcDate + ", payDate=" + payDate + ", submitKey=" + submitKey + "]";
  }
}
