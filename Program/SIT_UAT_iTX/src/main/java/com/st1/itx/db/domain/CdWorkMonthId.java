package com.st1.itx.db.domain;

import java.io.Serializable;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 * CdWorkMonth 放款業績工作月對照檔<br>
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
@Embeddable
public class CdWorkMonthId implements Serializable {


  /**
	 * 
	 */
	private static final long serialVersionUID = -3523015871861025009L;

// 業績年度
  @Column(name = "`Year`")
  private int year = 0;

  // 工作月份
  /* 13個月 */
  @Column(name = "`Month`")
  private int month = 0;

  public CdWorkMonthId() {
  }

  public CdWorkMonthId(int year, int month) {
    this.year = year;
    this.month = month;
  }

/**
	* 業績年度<br>
	* 
	* @return Integer
	*/
  public int getYear() {
    return this.year;
  }

/**
	* 業績年度<br>
	* 
  *
  * @param year 業績年度
	*/
  public void setYear(int year) {
    this.year = year;
  }

/**
	* 工作月份<br>
	* 13個月
	* @return Integer
	*/
  public int getMonth() {
    return this.month;
  }

/**
	* 工作月份<br>
	* 13個月
  *
  * @param month 工作月份
	*/
  public void setMonth(int month) {
    this.month = month;
  }


  @Override
  public int hashCode() {
    return Objects.hash(year, month);
  }

  @Override
  public boolean equals(Object obj) {
    if(this == obj)
      return true;
    if(obj == null || getClass() != obj.getClass())
      return false;
    CdWorkMonthId cdWorkMonthId = (CdWorkMonthId) obj;
    return year == cdWorkMonthId.year && month == cdWorkMonthId.month;
  }

  @Override
  public String toString() {
    return "CdWorkMonthId [year=" + year + ", month=" + month + "]";
  }
}
