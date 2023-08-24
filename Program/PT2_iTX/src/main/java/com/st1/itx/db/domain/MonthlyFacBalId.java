package com.st1.itx.db.domain;

import java.io.Serializable;
import java.util.Objects;
import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import com.st1.itx.util.StaticTool;
import com.st1.itx.Exception.LogicException;

/**
 * MonthlyFacBal 額度月報工作檔<br>
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
@Embeddable
public class MonthlyFacBalId implements Serializable {


  // 資料年月
  @Column(name = "`YearMonth`")
  private int yearMonth = 0;

  // 戶號
  @Column(name = "`CustNo`")
  private int custNo = 0;

  // 額度
  @Column(name = "`FacmNo`")
  private int facmNo = 0;

  public MonthlyFacBalId() {
  }

  public MonthlyFacBalId(int yearMonth, int custNo, int facmNo) {
    this.yearMonth = yearMonth;
    this.custNo = custNo;
    this.facmNo = facmNo;
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


  @Override
  public int hashCode() {
    return Objects.hash(yearMonth, custNo, facmNo);
  }

  @Override
  public boolean equals(Object obj) {
    if(this == obj)
      return true;
    if(obj == null || getClass() != obj.getClass())
      return false;
    MonthlyFacBalId monthlyFacBalId = (MonthlyFacBalId) obj;
    return yearMonth == monthlyFacBalId.yearMonth && custNo == monthlyFacBalId.custNo && facmNo == monthlyFacBalId.facmNo;
  }

  @Override
  public String toString() {
    return "MonthlyFacBalId [yearMonth=" + yearMonth + ", custNo=" + custNo + ", facmNo=" + facmNo + "]";
  }
}
