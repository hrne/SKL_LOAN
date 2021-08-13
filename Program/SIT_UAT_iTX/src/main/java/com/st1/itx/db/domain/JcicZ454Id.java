package com.st1.itx.db.domain;

import java.io.Serializable;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import com.st1.itx.util.StaticTool;
import com.st1.itx.Exception.LogicException;

/**
 * JcicZ454 前置調解單獨全數受清償資料<br>
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
@Embeddable
public class JcicZ454Id implements Serializable {


  /**
	 * 
	 */
	private static final long serialVersionUID = 5298061321884779239L;

// 報送單位代號
  /* 三位文數字 */
  @Column(name = "`SubmitKey`", length = 3)
  private String submitKey = " ";

  // 債務人IDN
  @Column(name = "`CustId`", length = 10)
  private String custId = " ";

  // 調解申請日
  @Column(name = "`ApplyDate`", length = 8)
  private int applyDate = 0;

  // 受理調解機構代號
  /* 三位文數字法院名稱代號表(CdCode.CourtCode)或郵遞區號 */
  @Column(name = "`BankId`", length = 3)
  private String bankId = " ";

  // 最大債權金融機構代號
  /* 三位文數字 */
  @Column(name = "`MaxMainCode`", length = 3)
  private String maxMainCode = " ";

  public JcicZ454Id() {
  }

  public JcicZ454Id(String submitKey, String custId, int applyDate, String bankId, String maxMainCode) {
    this.submitKey = submitKey;
    this.custId = custId;
    this.applyDate = applyDate;
    this.bankId = bankId;
    this.maxMainCode = maxMainCode;
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
	* 調解申請日<br>
	* 
	* @return Integer
	*/
  public int getApplyDate() {
    return  StaticTool.bcToRoc(this.applyDate);
  }

/**
	* 調解申請日<br>
	* 
  *
  * @param applyDate 調解申請日
  * @throws LogicException when Date Is Warn	*/
  public void setApplyDate(int applyDate) throws LogicException {
    this.applyDate = StaticTool.rocToBc(applyDate);
  }

/**
	* 受理調解機構代號<br>
	* 三位文數字
法院名稱代號表(CdCode.CourtCode)或郵遞區號
	* @return String
	*/
  public String getBankId() {
    return this.bankId == null ? "" : this.bankId;
  }

/**
	* 受理調解機構代號<br>
	* 三位文數字
法院名稱代號表(CdCode.CourtCode)或郵遞區號
  *
  * @param bankId 受理調解機構代號
	*/
  public void setBankId(String bankId) {
    this.bankId = bankId;
  }

/**
	* 最大債權金融機構代號<br>
	* 三位文數字
	* @return String
	*/
  public String getMaxMainCode() {
    return this.maxMainCode == null ? "" : this.maxMainCode;
  }

/**
	* 最大債權金融機構代號<br>
	* 三位文數字
  *
  * @param maxMainCode 最大債權金融機構代號
	*/
  public void setMaxMainCode(String maxMainCode) {
    this.maxMainCode = maxMainCode;
  }


  @Override
  public int hashCode() {
    return Objects.hash(submitKey, custId, applyDate, bankId, maxMainCode);
  }

  @Override
  public boolean equals(Object obj) {
    if(this == obj)
      return true;
    if(obj == null || getClass() != obj.getClass())
      return false;
    JcicZ454Id jcicZ454Id = (JcicZ454Id) obj;
    return submitKey.equals(jcicZ454Id.submitKey) && custId.equals(jcicZ454Id.custId) && applyDate == jcicZ454Id.applyDate && bankId == jcicZ454Id.bankId && maxMainCode == jcicZ454Id.maxMainCode;
  }

  @Override
  public String toString() {
    return "JcicZ454Id [submitKey=" + submitKey + ", custId=" + custId + ", applyDate=" + applyDate + ", bankId=" + bankId + ", maxMainCode=" + maxMainCode + "]";
  }
}
