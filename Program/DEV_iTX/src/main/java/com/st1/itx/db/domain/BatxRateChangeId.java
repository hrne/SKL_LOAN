package com.st1.itx.db.domain;

import java.io.Serializable;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import com.st1.itx.util.StaticTool;
import com.st1.itx.Exception.LogicException;

/**
 * BatxRateChange 整批利率調整檔<br>
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
@Embeddable
public class BatxRateChangeId implements Serializable {


  /**
	 * 
	 */
	private static final long serialVersionUID = 4999011852780236817L;

// 調整日期
  @Column(name = "`AdjDate`")
  private int adjDate = 0;

  // 戶號
  @Column(name = "`CustNo`")
  private int custNo = 0;

  // 額度
  @Column(name = "`FacmNo`")
  private int facmNo = 0;

  // 撥款序號
  @Column(name = "`BormNo`")
  private int bormNo = 0;

  public BatxRateChangeId() {
  }

  public BatxRateChangeId(int adjDate, int custNo, int facmNo, int bormNo) {
    this.adjDate = adjDate;
    this.custNo = custNo;
    this.facmNo = facmNo;
    this.bormNo = bormNo;
  }

/**
	* 調整日期<br>
	* 
	* @return Integer
	*/
  public int getAdjDate() {
    return  StaticTool.bcToRoc(this.adjDate);
  }

/**
	* 調整日期<br>
	* 
  *
  * @param adjDate 調整日期
  * @throws LogicException when Date Is Warn	*/
  public void setAdjDate(int adjDate) throws LogicException {
    this.adjDate = StaticTool.rocToBc(adjDate);
  }

/**
	* 戶號<br>
	* 
	* @return Integer
	*/
  public int getCustNo() {
    return this.custNo;
  }

/**
	* 戶號<br>
	* 
  *
  * @param custNo 戶號
	*/
  public void setCustNo(int custNo) {
    this.custNo = custNo;
  }

/**
	* 額度<br>
	* 
	* @return Integer
	*/
  public int getFacmNo() {
    return this.facmNo;
  }

/**
	* 額度<br>
	* 
  *
  * @param facmNo 額度
	*/
  public void setFacmNo(int facmNo) {
    this.facmNo = facmNo;
  }

/**
	* 撥款序號<br>
	* 
	* @return Integer
	*/
  public int getBormNo() {
    return this.bormNo;
  }

/**
	* 撥款序號<br>
	* 
  *
  * @param bormNo 撥款序號
	*/
  public void setBormNo(int bormNo) {
    this.bormNo = bormNo;
  }


  @Override
  public int hashCode() {
    return Objects.hash(adjDate, custNo, facmNo, bormNo);
  }

  @Override
  public boolean equals(Object obj) {
    if(this == obj)
      return true;
    if(obj == null || getClass() != obj.getClass())
      return false;
    BatxRateChangeId batxRateChangeId = (BatxRateChangeId) obj;
    return adjDate == batxRateChangeId.adjDate && custNo == batxRateChangeId.custNo && facmNo == batxRateChangeId.facmNo && bormNo == batxRateChangeId.bormNo;
  }

  @Override
  public String toString() {
    return "BatxRateChangeId [adjDate=" + adjDate + ", custNo=" + custNo + ", facmNo=" + facmNo + ", bormNo=" + bormNo + "]";
  }
}
