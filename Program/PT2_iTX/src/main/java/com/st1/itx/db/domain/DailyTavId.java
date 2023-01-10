package com.st1.itx.db.domain;

import java.io.Serializable;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 * DailyTav 每日暫收款餘額檔<br>
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
@Embeddable
public class DailyTavId implements Serializable {


  /**
	 * 
	 */
	private static final long serialVersionUID = 7786901914708118460L;

// 會計日期
  @Column(name = "`AcDate`")
  private int acDate = 0;

  // 借款人戶號
  @Column(name = "`CustNo`")
  private int custNo = 0;

  // 額度編號
  @Column(name = "`FacmNo`")
  private int facmNo = 0;

  public DailyTavId() {
  }

  public DailyTavId(int acDate, int custNo, int facmNo) {
    this.acDate = acDate;
    this.custNo = custNo;
    this.facmNo = facmNo;
  }

/**
	* 會計日期<br>
	* 
	* @return Integer
	*/
  public int getAcDate() {
    return this.acDate;
  }

/**
	* 會計日期<br>
	* 
  *
  * @param acDate 會計日期
	*/
  public void setAcDate(int acDate) {
    this.acDate = acDate;
  }

/**
	* 借款人戶號<br>
	* 
	* @return Integer
	*/
  public int getCustNo() {
    return this.custNo;
  }

/**
	* 借款人戶號<br>
	* 
  *
  * @param custNo 借款人戶號
	*/
  public void setCustNo(int custNo) {
    this.custNo = custNo;
  }

/**
	* 額度編號<br>
	* 
	* @return Integer
	*/
  public int getFacmNo() {
    return this.facmNo;
  }

/**
	* 額度編號<br>
	* 
  *
  * @param facmNo 額度編號
	*/
  public void setFacmNo(int facmNo) {
    this.facmNo = facmNo;
  }


  @Override
  public int hashCode() {
    return Objects.hash(acDate, custNo, facmNo);
  }

  @Override
  public boolean equals(Object obj) {
    if(this == obj)
      return true;
    if(obj == null || getClass() != obj.getClass())
      return false;
    DailyTavId dailyTavId = (DailyTavId) obj;
    return acDate == dailyTavId.acDate && custNo == dailyTavId.custNo && facmNo == dailyTavId.facmNo;
  }

  @Override
  public String toString() {
    return "DailyTavId [acDate=" + acDate + ", custNo=" + custNo + ", facmNo=" + facmNo + "]";
  }
}
