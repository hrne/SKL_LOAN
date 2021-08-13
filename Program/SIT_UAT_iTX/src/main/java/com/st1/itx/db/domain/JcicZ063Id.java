package com.st1.itx.db.domain;

import java.io.Serializable;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import com.st1.itx.util.StaticTool;
import com.st1.itx.Exception.LogicException;

/**
 * JcicZ063 變更還款方案結案通知資料<br>
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
@Embeddable
public class JcicZ063Id implements Serializable {


  /**
	 * 
	 */
	private static final long serialVersionUID = -293920779134449770L;

// 報送單位代號
  /* 三位文數字 */
  @Column(name = "`SubmitKey`", length = 3)
  private String submitKey = " ";

  // 債務人IDN
  @Column(name = "`CustId`", length = 10)
  private String custId = " ";

  // 原前置協商申請日
  @Column(name = "`RcDate`", length = 8)
  private int rcDate = 0;

  // 申請變更還款條件日
  @Column(name = "`ChangePayDate`", length = 8)
  private int changePayDate = 0;

  // 變更還款條件結案日期
  @Column(name = "`ClosedDate`", length = 8)
  private int closedDate = 0;

  public JcicZ063Id() {
  }

  public JcicZ063Id(String submitKey, String custId, int rcDate, int changePayDate, int closedDate) {
    this.submitKey = submitKey;
    this.custId = custId;
    this.rcDate = rcDate;
    this.changePayDate = changePayDate;
    this.closedDate = closedDate;
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
	* 
	* @return Integer
	*/
  public int getRcDate() {
    return  StaticTool.bcToRoc(this.rcDate);
  }

/**
	* 原前置協商申請日<br>
	* 
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

/**
	* 變更還款條件結案日期<br>
	* 
	* @return Integer
	*/
  public int getClosedDate() {
    return  StaticTool.bcToRoc(this.closedDate);
  }

/**
	* 變更還款條件結案日期<br>
	* 
  *
  * @param closedDate 變更還款條件結案日期
  * @throws LogicException when Date Is Warn	*/
  public void setClosedDate(int closedDate) throws LogicException {
    this.closedDate = StaticTool.rocToBc(closedDate);
  }


  @Override
  public int hashCode() {
    return Objects.hash(submitKey, custId, rcDate, changePayDate, closedDate);
  }

  @Override
  public boolean equals(Object obj) {
    if(this == obj)
      return true;
    if(obj == null || getClass() != obj.getClass())
      return false;
    JcicZ063Id jcicZ063Id = (JcicZ063Id) obj;
    return submitKey.equals(jcicZ063Id.submitKey) && custId.equals(jcicZ063Id.custId) && rcDate == jcicZ063Id.rcDate && changePayDate == jcicZ063Id.changePayDate && closedDate == jcicZ063Id.closedDate;
  }

  @Override
  public String toString() {
    return "JcicZ063Id [submitKey=" + submitKey + ", custId=" + custId + ", rcDate=" + rcDate + ", changePayDate=" + changePayDate + ", closedDate=" + closedDate + "]";
  }
}
