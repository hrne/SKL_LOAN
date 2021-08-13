package com.st1.itx.db.domain;

import java.io.Serializable;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 * YearlyHouseLoanInt 每年房屋擔保借款繳息工作檔<br>
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
@Embeddable
public class YearlyHouseLoanIntId implements Serializable {


  /**
	 * 
	 */
	private static final long serialVersionUID = -8343765826127125366L;

// 資料年月
  @Column(name = "`YearMonth`")
  private int yearMonth = 0;

  // 戶號
  @Column(name = "`CustNo`")
  private int custNo = 0;

  // 額度編號
  @Column(name = "`FacmNo`")
  private int facmNo = 0;

  // 資金用途別
  /* 02:購置不動產00:全部 */
  @Column(name = "`UsageCode`", length = 2)
  private String usageCode = " ";

  public YearlyHouseLoanIntId() {
  }

  public YearlyHouseLoanIntId(int yearMonth, int custNo, int facmNo, String usageCode) {
    this.yearMonth = yearMonth;
    this.custNo = custNo;
    this.facmNo = facmNo;
    this.usageCode = usageCode;
  }

/**
	* 資料年月<br>
	* 
	* @return Integer
	*/
  public int getYearMonth() {
    return this.yearMonth;
  }

/**
	* 資料年月<br>
	* 
  *
  * @param yearMonth 資料年月
	*/
  public void setYearMonth(int yearMonth) {
    this.yearMonth = yearMonth;
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

/**
	* 資金用途別<br>
	* 02:購置不動產
00:全部
	* @return String
	*/
  public String getUsageCode() {
    return this.usageCode == null ? "" : this.usageCode;
  }

/**
	* 資金用途別<br>
	* 02:購置不動產
00:全部
  *
  * @param usageCode 資金用途別
	*/
  public void setUsageCode(String usageCode) {
    this.usageCode = usageCode;
  }


  @Override
  public int hashCode() {
    return Objects.hash(yearMonth, custNo, facmNo, usageCode);
  }

  @Override
  public boolean equals(Object obj) {
    if(this == obj)
      return true;
    if(obj == null || getClass() != obj.getClass())
      return false;
    YearlyHouseLoanIntId yearlyHouseLoanIntId = (YearlyHouseLoanIntId) obj;
    return yearMonth == yearlyHouseLoanIntId.yearMonth && custNo == yearlyHouseLoanIntId.custNo && facmNo == yearlyHouseLoanIntId.facmNo && usageCode.equals(yearlyHouseLoanIntId.usageCode);
  }

  @Override
  public String toString() {
    return "YearlyHouseLoanIntId [yearMonth=" + yearMonth + ", custNo=" + custNo + ", facmNo=" + facmNo + ", usageCode=" + usageCode + "]";
  }
}
