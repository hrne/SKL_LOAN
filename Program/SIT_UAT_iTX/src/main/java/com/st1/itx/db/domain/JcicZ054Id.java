package com.st1.itx.db.domain;

import java.io.Serializable;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import com.st1.itx.util.StaticTool;
import com.st1.itx.Exception.LogicException;

/**
 * JcicZ054 單獨全數受清償資料檔案<br>
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
@Embeddable
public class JcicZ054Id implements Serializable {


  /**
	 * 
	 */
	private static final long serialVersionUID = -1159804909736208752L;

// 債務人IDN
  @Column(name = "`CustId`", length = 10)
  private String custId = " ";

  // 報送單位代號
  /* 三位文數字 */
  @Column(name = "`SubmitKey`", length = 3)
  private String submitKey = " ";

  // 協商申請日
  @Column(name = "`RcDate`")
  private int rcDate = 0;

  // 最大債權金融機構代號
  /* 三位文數字 */
  @Column(name = "`MaxMainCode`", length = 3)
  private String maxMainCode = " ";

  public JcicZ054Id() {
  }

  public JcicZ054Id(String custId, String submitKey, int rcDate, String maxMainCode) {
    this.custId = custId;
    this.submitKey = submitKey;
    this.rcDate = rcDate;
    this.maxMainCode = maxMainCode;
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
    return Objects.hash(custId, submitKey, rcDate, maxMainCode);
  }

  @Override
  public boolean equals(Object obj) {
    if(this == obj)
      return true;
    if(obj == null || getClass() != obj.getClass())
      return false;
    JcicZ054Id jcicZ054Id = (JcicZ054Id) obj;
    return custId.equals(jcicZ054Id.custId) && submitKey == jcicZ054Id.submitKey && rcDate == jcicZ054Id.rcDate && maxMainCode == jcicZ054Id.maxMainCode;
  }

  @Override
  public String toString() {
    return "JcicZ054Id [custId=" + custId + ", submitKey=" + submitKey + ", rcDate=" + rcDate + ", maxMainCode=" + maxMainCode + "]";
  }
}
