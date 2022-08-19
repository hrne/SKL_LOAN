package com.st1.itx.db.domain;

import java.io.Serializable;
import java.util.Objects;
import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import com.st1.itx.util.StaticTool;
import com.st1.itx.Exception.LogicException;

/**
 * JcicZ041 協商開始暨停催通知資料<br>
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
@Embeddable
public class JcicZ041Id implements Serializable {


  // 報送單位代號
  /* 三位文數字 */
  @Column(name = "`SubmitKey`", length = 3)
  private String submitKey = " ";

  // 債務人IDN
  /* 身份證字號 */
  @Column(name = "`CustId`", length = 10)
  private String custId = " ";

  // 協商申請日
  /* 西元年月 */
  @Column(name = "`RcDate`")
  private int rcDate = 0;

  public JcicZ041Id() {
  }

  public JcicZ041Id(String submitKey, String custId, int rcDate) {
    this.submitKey = submitKey;
    this.custId = custId;
    this.rcDate = rcDate;
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

/**
	* 債務人IDN<br>
	* 身份證字號
	* @return String
	*/
  public String getCustId() {
    return this.custId == null ? "" : this.custId;
  }

/**
	* 債務人IDN<br>
	* 身份證字號
  *
  * @param custId 債務人IDN
	*/
  public void setCustId(String custId) {
    this.custId = custId;
  }

/**
	* 協商申請日<br>
	* 西元年月
	* @return Integer
	*/
  public int getRcDate() {
    return  StaticTool.bcToRoc(this.rcDate);
  }

/**
	* 協商申請日<br>
	* 西元年月
  *
  * @param rcDate 協商申請日
  * @throws LogicException when Date Is Warn	*/
  public void setRcDate(int rcDate) throws LogicException {
    this.rcDate = StaticTool.rocToBc(rcDate);
  }


  @Override
  public int hashCode() {
    return Objects.hash(submitKey, custId, rcDate);
  }

  @Override
  public boolean equals(Object obj) {
    if(this == obj)
      return true;
    if(obj == null || getClass() != obj.getClass())
      return false;
    JcicZ041Id jcicZ041Id = (JcicZ041Id) obj;
    return submitKey.equals(jcicZ041Id.submitKey) && custId.equals(jcicZ041Id.custId) && rcDate == jcicZ041Id.rcDate;
  }

  @Override
  public String toString() {
    return "JcicZ041Id [submitKey=" + submitKey + ", custId=" + custId + ", rcDate=" + rcDate + "]";
  }
}
