package com.st1.itx.db.domain;

import java.io.Serializable;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import com.st1.itx.util.StaticTool;
import com.st1.itx.Exception.LogicException;

/**
 * JcicZ046 結案通知資料檔案格式<br>
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
@Embeddable
public class JcicZ046Id implements Serializable {


  /**
	 * 
	 */
	private static final long serialVersionUID = 405258800351009932L;

// 報送單位代號
  /* 三位文數字 */
  @Column(name = "`SubmitKey`", length = 3)
  private String submitKey = " ";

  // 債務人IDN
  @Column(name = "`CustId`", length = 10)
  private String custId = " ";

  // 協商申請日
  @Column(name = "`RcDate`")
  private int rcDate = 0;

  // 結案日期
  @Column(name = "`CloseDate`")
  private int closeDate = 0;

  public JcicZ046Id() {
  }

  public JcicZ046Id(String submitKey, String custId, int rcDate, int closeDate) {
    this.submitKey = submitKey;
    this.custId = custId;
    this.rcDate = rcDate;
    this.closeDate = closeDate;
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
	* 結案日期<br>
	* 
	* @return Integer
	*/
  public int getCloseDate() {
    return  StaticTool.bcToRoc(this.closeDate);
  }

/**
	* 結案日期<br>
	* 
  *
  * @param closeDate 結案日期
  * @throws LogicException when Date Is Warn	*/
  public void setCloseDate(int closeDate) throws LogicException {
    this.closeDate = StaticTool.rocToBc(closeDate);
  }


  @Override
  public int hashCode() {
    return Objects.hash(submitKey, custId, rcDate, closeDate);
  }

  @Override
  public boolean equals(Object obj) {
    if(this == obj)
      return true;
    if(obj == null || getClass() != obj.getClass())
      return false;
    JcicZ046Id jcicZ046Id = (JcicZ046Id) obj;
    return submitKey.equals(jcicZ046Id.submitKey) && custId.equals(jcicZ046Id.custId) && rcDate == jcicZ046Id.rcDate && closeDate == jcicZ046Id.closeDate;
  }

  @Override
  public String toString() {
    return "JcicZ046Id [submitKey=" + submitKey + ", custId=" + custId + ", rcDate=" + rcDate + ", closeDate=" + closeDate + "]";
  }
}
