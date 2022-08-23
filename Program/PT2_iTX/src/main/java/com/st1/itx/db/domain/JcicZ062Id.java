package com.st1.itx.db.domain;

import java.io.Serializable;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import com.st1.itx.util.StaticTool;
import com.st1.itx.Exception.LogicException;

/**
 * JcicZ062 金融機構無擔保債務變更還款條件協議資料<br>
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
@Embeddable
public class JcicZ062Id implements Serializable {


  /**
	 * 
	 */
	private static final long serialVersionUID = -436154850960642277L;

// 報送單位代號
  /* 三位文數字 */
  @Column(name = "`SubmitKey`", length = 3)
  private String submitKey = " ";

  // 債務人IDN
  @Column(name = "`CustId`", length = 10)
  private String custId = " ";

  // 協商申請日
  /* 原前置協商申請日 */
  @Column(name = "`RcDate`")
  private int rcDate = 0;

  // 申請變更還款條件日
  @Column(name = "`ChangePayDate`")
  private int changePayDate = 0;

  public JcicZ062Id() {
  }

  public JcicZ062Id(String submitKey, String custId, int rcDate, int changePayDate) {
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
	* 協商申請日<br>
	* 原前置協商申請日
	* @return Integer
	*/
  public int getRcDate() {
    return  StaticTool.bcToRoc(this.rcDate);
  }

/**
	* 協商申請日<br>
	* 原前置協商申請日
  *
  * @param rcDate 協商申請日
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
    JcicZ062Id jcicZ062Id = (JcicZ062Id) obj;
    return submitKey.equals(jcicZ062Id.submitKey) && custId.equals(jcicZ062Id.custId) && rcDate == jcicZ062Id.rcDate && changePayDate == jcicZ062Id.changePayDate;
  }

  @Override
  public String toString() {
    return "JcicZ062Id [submitKey=" + submitKey + ", custId=" + custId + ", rcDate=" + rcDate + ", changePayDate=" + changePayDate + "]";
  }
}
