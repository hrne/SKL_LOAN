package com.st1.itx.db.domain;

import java.io.Serializable;
import java.util.Objects;
import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import com.st1.itx.util.StaticTool;
import com.st1.itx.Exception.LogicException;

/**
 * JcicZ060 債務人繳款資料檔案<br>
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
@Embeddable
public class JcicZ060Id implements Serializable {


  // 報送單位代號
  /* 三位文數字 */
  @Column(name = "`SubmitKey`", length = 3)
  private String submitKey = " ";

  // 債務人IDN
  @Column(name = "`CustId`", length = 10)
  private String custId = " ";

  // 原前置協商申請日
  /* 指債務人原前置協商申請日期同(["40"前置協商受理申請暨請求回報債權通知資料]-協商申請日) */
  @Column(name = "`RcDate`")
  private int rcDate = 0;

  // 申請變更還款條件日
  @Column(name = "`ChangePayDate`")
  private int changePayDate = 0;

  public JcicZ060Id() {
  }

  public JcicZ060Id(String submitKey, String custId, int rcDate, int changePayDate) {
    this.submitKey = submitKey;
    this.custId = custId;
    this.rcDate = rcDate;
    this.changePayDate = changePayDate;
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
	* 原前置協商申請日<br>
	* 指債務人原前置協商申請日期同(["40"前置協商受理申請暨請求回報債權通知資料]-協商申請日)
	* @return Integer
	*/
  public int getRcDate() {
    return  StaticTool.bcToRoc(this.rcDate);
  }

/**
	* 原前置協商申請日<br>
	* 指債務人原前置協商申請日期同(["40"前置協商受理申請暨請求回報債權通知資料]-協商申請日)
  *
  * @param rcDate 原前置協商申請日
  * @throws LogicException when Date Is Warn	*/
  public void setRcDate(int rcDate) throws LogicException {
    this.rcDate = StaticTool.rocToBc(rcDate);
  }

/**
	* 申請變更還款條件日<br>
	* 
	* @return Integer
	*/
  public int getChangePayDate() {
    return  StaticTool.bcToRoc(this.changePayDate);
  }

/**
	* 申請變更還款條件日<br>
	* 
  *
  * @param changePayDate 申請變更還款條件日
  * @throws LogicException when Date Is Warn	*/
  public void setChangePayDate(int changePayDate) throws LogicException {
    this.changePayDate = StaticTool.rocToBc(changePayDate);
  }


  @Override
  public int hashCode() {
    return Objects.hash(submitKey, custId, rcDate, changePayDate);
  }

  @Override
  public boolean equals(Object obj) {
    if(this == obj)
      return true;
    if(obj == null || getClass() != obj.getClass())
      return false;
    JcicZ060Id jcicZ060Id = (JcicZ060Id) obj;
    return submitKey.equals(jcicZ060Id.submitKey) && custId.equals(jcicZ060Id.custId) && rcDate == jcicZ060Id.rcDate && changePayDate == jcicZ060Id.changePayDate;
  }

  @Override
  public String toString() {
    return "JcicZ060Id [submitKey=" + submitKey + ", custId=" + custId + ", rcDate=" + rcDate + ", changePayDate=" + changePayDate + "]";
  }
}
