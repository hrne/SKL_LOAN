package com.st1.itx.db.domain;

import java.io.Serializable;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import com.st1.itx.util.StaticTool;
import com.st1.itx.Exception.LogicException;

/**
 * JcicZ446 前置調解結案通知資料<br>
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
@Embeddable
public class JcicZ446Id implements Serializable {


  /**
	 * 
	 */
	private static final long serialVersionUID = -7677066424204430585L;

// 報送單位代號
  /* 三位文數字 */
  @Column(name = "`SubmitKey`", length = 3)
  private String submitKey = " ";

  // 債務人IDN
  /* 目前可輸入四碼 */
  @Column(name = "`CustId`", length = 10)
  private String custId = " ";

  // 調解申請日
  @Column(name = "`ApplyDate`", length = 8)
  private int applyDate = 0;

  // 受理調解機構代號
  /* 三位文數字法院名稱代號表(CdCode.CourtCode)或郵遞區號 */
  @Column(name = "`BankId`", length = 3)
  private String bankId = " ";

  public JcicZ446Id() {
  }

  public JcicZ446Id(String submitKey, String custId, int applyDate, String bankId) {
    this.submitKey = submitKey;
    this.custId = custId;
    this.applyDate = applyDate;
    this.bankId = bankId;
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
	* 目前可輸入四碼
	* @return String
	*/
  public String getCustId() {
    return this.custId == null ? "" : this.custId;
  }

/**
	* 債務人IDN<br>
	* 目前可輸入四碼
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
    JcicZ446Id jcicZ446Id = (JcicZ446Id) obj;
    return submitKey.equals(jcicZ446Id.submitKey) && custId.equals(jcicZ446Id.custId) && applyDate == jcicZ446Id.applyDate && bankId == jcicZ446Id.bankId;
  }

  @Override
  public String toString() {
    return "JcicZ446Id [submitKey=" + submitKey + ", custId=" + custId + ", applyDate=" + applyDate + ", bankId=" + bankId + "]";
  }
}
