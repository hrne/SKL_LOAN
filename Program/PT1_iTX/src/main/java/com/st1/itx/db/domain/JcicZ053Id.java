package com.st1.itx.db.domain;

import java.io.Serializable;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import com.st1.itx.util.StaticTool;
import com.st1.itx.Exception.LogicException;

/**
 * JcicZ053 同意報送例外處理檔案<br>
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
@Embeddable
public class JcicZ053Id implements Serializable {


  /**
	 * 
	 */
	private static final long serialVersionUID = 1323282843317450532L;

// 報送單位代號
  /* 三位文數字 */
  @Column(name = "`SubmitKey`", length = 3)
  private String submitKey = " ";

  // 債務人IDN
  @Column(name = "`CustId`", length = 10)
  private String custId = " ";

  // 協商申請日
  /* 指最大債權金融機構實際之收件日期 */
  @Column(name = "`RcDate`")
  private int rcDate = 0;

  // 最大債權金融機構代號
  /* 三位文數字 */
  @Column(name = "`MaxMainCode`", length = 3)
  private String maxMainCode = " ";

  // 申請變更還款條件日
  @Column(name = "`ChangePayDate`")
  private int changePayDate = 0;

  public JcicZ053Id() {
  }

  public JcicZ053Id(String submitKey, String custId, int rcDate, String maxMainCode, int changePayDate) {
    this.submitKey = submitKey;
    this.custId = custId;
    this.rcDate = rcDate;
    this.maxMainCode = maxMainCode;
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
	* 指最大債權金融機構實際之收件日期
	* @return Integer
	*/
  public int getRcDate() {
    return  StaticTool.bcToRoc(this.rcDate);
  }

/**
	* 協商申請日<br>
	* 指最大債權金融機構實際之收件日期
  *
  * @param rcDate 協商申請日
  * @throws LogicException when Date Is Warn	*/
  public void setRcDate(int rcDate) throws LogicException {
    this.rcDate = StaticTool.rocToBc(rcDate);
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
    return Objects.hash(submitKey, custId, rcDate, maxMainCode, changePayDate);
  }

  @Override
  public boolean equals(Object obj) {
    if(this == obj)
      return true;
    if(obj == null || getClass() != obj.getClass())
      return false;
    JcicZ053Id jcicZ053Id = (JcicZ053Id) obj;
    return submitKey.equals(jcicZ053Id.submitKey) && custId.equals(jcicZ053Id.custId) && rcDate == jcicZ053Id.rcDate && maxMainCode == jcicZ053Id.maxMainCode && changePayDate == jcicZ053Id.changePayDate;
  }

  @Override
  public String toString() {
    return "JcicZ053Id [submitKey=" + submitKey + ", custId=" + custId + ", rcDate=" + rcDate + ", maxMainCode=" + maxMainCode + ", changePayDate=" + changePayDate + "]";
  }
}
