package com.st1.itx.db.domain;

import java.io.Serializable;
import java.util.Objects;
import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import com.st1.itx.util.StaticTool;
import com.st1.itx.Exception.LogicException;

/**
 * JcicZ570 受理更生款項統一收付通知資料<br>
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
@Embeddable
public class JcicZ570Id implements Serializable {


  // 報送單位代號
  @Column(name = "`SubmitKey`", length = 3)
  private String submitKey = " ";

  // 債務人IDN
  @Column(name = "`CustId`", length = 10)
  private String custId = " ";

  // 申請日期
  @Column(name = "`ApplyDate`")
  private int applyDate = 0;

  public JcicZ570Id() {
  }

  public JcicZ570Id(String submitKey, String custId, int applyDate) {
    this.submitKey = submitKey;
    this.custId = custId;
    this.applyDate = applyDate;
  }

/**
	* 報送單位代號<br>
	* 
	* @return String
	*/
  public String getSubmitKey() {
    return this.submitKey == null ? "" : this.submitKey;
  }

/**
	* 報送單位代號<br>
	* 
  *
  * @param submitKey 報送單位代號
	*/
  public void setSubmitKey(String submitKey) {
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
	* 申請日期<br>
	* 
	* @return Integer
	*/
  public int getApplyDate() {
    return  StaticTool.bcToRoc(this.applyDate);
  }

/**
	* 申請日期<br>
	* 
  *
  * @param applyDate 申請日期
  * @throws LogicException when Date Is Warn	*/
  public void setApplyDate(int applyDate) throws LogicException {
    this.applyDate = StaticTool.rocToBc(applyDate);
  }


  @Override
  public int hashCode() {
    return Objects.hash(submitKey, custId, applyDate);
  }

  @Override
  public boolean equals(Object obj) {
    if(this == obj)
      return true;
    if(obj == null || getClass() != obj.getClass())
      return false;
    JcicZ570Id jcicZ570Id = (JcicZ570Id) obj;
    return submitKey.equals(jcicZ570Id.submitKey) && custId.equals(jcicZ570Id.custId) && applyDate == jcicZ570Id.applyDate;
  }

  @Override
  public String toString() {
    return "JcicZ570Id [submitKey=" + submitKey + ", custId=" + custId + ", applyDate=" + applyDate + "]";
  }
}
