package com.st1.itx.db.domain;

import java.io.Serializable;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import com.st1.itx.util.StaticTool;
import com.st1.itx.Exception.LogicException;

/**
 * JcicZ575 更生債權金額異動通知資料<br>
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
@Embeddable
public class JcicZ575Id implements Serializable {


  /**
	 * 
	 */
	private static final long serialVersionUID = -370132355255842891L;

// 報送單位代號
  /* 3位文數字 */
  @Column(name = "`SubmitKey`", length = 3)
  private String submitKey = " ";

  // 債務人IDN
  @Column(name = "`CustId`", length = 10)
  private String custId = " ";

  // 申請日期
  @Column(name = "`ApplyDate`")
  private int applyDate = 0;

  // 異動債權金機構代號
  /* 3位文數字 */
  @Column(name = "`BankId`", length = 3)
  private String bankId = " ";

  public JcicZ575Id() {
  }

  public JcicZ575Id(String submitKey, String custId, int applyDate, String bankId) {
    this.submitKey = submitKey;
    this.custId = custId;
    this.applyDate = applyDate;
    this.bankId = bankId;
  }

/**
	* 報送單位代號<br>
	* 3位文數字
	* @return String
	*/
  public String getSubmitKey() {
    return this.submitKey == null ? "" : this.submitKey;
  }

/**
	* 報送單位代號<br>
	* 3位文數字
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

/**
	* 異動債權金機構代號<br>
	* 3位文數字
	* @return String
	*/
  public String getBankId() {
    return this.bankId == null ? "" : this.bankId;
  }

/**
	* 異動債權金機構代號<br>
	* 3位文數字
  *
  * @param bankId 異動債權金機構代號
	*/
  public void setBankId(String bankId) {
    this.bankId = bankId;
  }


  @Override
  public int hashCode() {
    return Objects.hash(submitKey, custId, applyDate, bankId);
  }

  @Override
  public boolean equals(Object obj) {
    if(this == obj)
      return true;
    if(obj == null || getClass() != obj.getClass())
      return false;
    JcicZ575Id jcicZ575Id = (JcicZ575Id) obj;
    return submitKey.equals(jcicZ575Id.submitKey) && custId.equals(jcicZ575Id.custId) && applyDate == jcicZ575Id.applyDate && bankId == jcicZ575Id.bankId;
  }

  @Override
  public String toString() {
    return "JcicZ575Id [submitKey=" + submitKey + ", custId=" + custId + ", applyDate=" + applyDate + ", bankId=" + bankId + "]";
  }
}
