package com.st1.itx.db.domain;

import java.io.Serializable;
import java.util.Objects;
import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import com.st1.itx.util.StaticTool;
import com.st1.itx.Exception.LogicException;

/**
 * JcicZ450 前置調解債務人繳款資料<br>
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
@Embeddable
public class JcicZ450Id implements Serializable {


  // 報送單位代號
  /* 三位文數字 */
  @Column(name = "`SubmitKey`", length = 3)
  private String submitKey = " ";

  // 債務人IDN
  @Column(name = "`CustId`", length = 10)
  private String custId = " ";

  // 調解申請日
  @Column(name = "`ApplyDate`")
  private int applyDate = 0;

  // 受理調解機構代號
  /* 三位文數字法院名稱代號表(CdCode.CourtCode)或郵遞區號 */
  @Column(name = "`CourtCode`", length = 3)
  private String courtCode = " ";

  // 繳款日期
  @Column(name = "`PayDate`")
  private int payDate = 0;

  public JcicZ450Id() {
  }

  public JcicZ450Id(String submitKey, String custId, int applyDate, String courtCode, int payDate) {
    this.submitKey = submitKey;
    this.custId = custId;
    this.applyDate = applyDate;
    this.courtCode = courtCode;
    this.payDate = payDate;
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
  public String getCourtCode() {
    return this.courtCode == null ? "" : this.courtCode;
  }

/**
	* 受理調解機構代號<br>
	* 三位文數字
法院名稱代號表(CdCode.CourtCode)或郵遞區號
  *
  * @param courtCode 受理調解機構代號
	*/
  public void setCourtCode(String courtCode) {
    this.courtCode = courtCode;
  }

/**
	* 繳款日期<br>
	* 
	* @return Integer
	*/
  public int getPayDate() {
    return  StaticTool.bcToRoc(this.payDate);
  }

/**
	* 繳款日期<br>
	* 
  *
  * @param payDate 繳款日期
  * @throws LogicException when Date Is Warn	*/
  public void setPayDate(int payDate) throws LogicException {
    this.payDate = StaticTool.rocToBc(payDate);
  }


  @Override
  public int hashCode() {
    return Objects.hash(submitKey, custId, applyDate, courtCode, payDate);
  }

  @Override
  public boolean equals(Object obj) {
    if(this == obj)
      return true;
    if(obj == null || getClass() != obj.getClass())
      return false;
    JcicZ450Id jcicZ450Id = (JcicZ450Id) obj;
    return submitKey.equals(jcicZ450Id.submitKey) && custId.equals(jcicZ450Id.custId) && applyDate == jcicZ450Id.applyDate && courtCode == jcicZ450Id.courtCode && payDate == jcicZ450Id.payDate;
  }

  @Override
  public String toString() {
    return "JcicZ450Id [submitKey=" + submitKey + ", custId=" + custId + ", applyDate=" + applyDate + ", courtCode=" + courtCode + ", payDate=" + payDate + "]";
  }
}
