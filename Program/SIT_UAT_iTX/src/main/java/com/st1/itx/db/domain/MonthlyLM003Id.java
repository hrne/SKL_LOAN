package com.st1.itx.db.domain;

import java.io.Serializable;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 * MonthlyLM003 撥款還款金額比較月報工作檔<br>
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
@Embeddable
public class MonthlyLM003Id implements Serializable {


  /**
	 * 
	 */
	private static final long serialVersionUID = -797375789257165036L;

// 企金別
  /* 0.個金,1.企金 */
  @Column(name = "`EntType`")
  private int entType = 0;

  // 資料年份
  @Column(name = "`DataYear`")
  private int dataYear = 0;

  // 資料月份
  @Column(name = "`DataMonth`")
  private int dataMonth = 0;

  public MonthlyLM003Id() {
  }

  public MonthlyLM003Id(int entType, int dataYear, int dataMonth) {
    this.entType = entType;
    this.dataYear = dataYear;
    this.dataMonth = dataMonth;
  }

/**
	* 企金別<br>
	* 0.個金,1.企金
	* @return Integer
	*/
  public int getEntType() {
    return this.entType;
  }

/**
	* 企金別<br>
	* 0.個金,1.企金
  *
  * @param entType 企金別
	*/
  public void setEntType(int entType) {
    this.entType = entType;
  }

/**
	* 資料年份<br>
	* 
	* @return Integer
	*/
  public int getDataYear() {
    return this.dataYear;
  }

/**
	* 資料年份<br>
	* 
  *
  * @param dataYear 資料年份
	*/
  public void setDataYear(int dataYear) {
    this.dataYear = dataYear;
  }

/**
	* 資料月份<br>
	* 
	* @return Integer
	*/
  public int getDataMonth() {
    return this.dataMonth;
  }

/**
	* 資料月份<br>
	* 
  *
  * @param dataMonth 資料月份
	*/
  public void setDataMonth(int dataMonth) {
    this.dataMonth = dataMonth;
  }


  @Override
  public int hashCode() {
    return Objects.hash(entType, dataYear, dataMonth);
  }

  @Override
  public boolean equals(Object obj) {
    if(this == obj)
      return true;
    if(obj == null || getClass() != obj.getClass())
      return false;
    MonthlyLM003Id monthlyLM003Id = (MonthlyLM003Id) obj;
    return entType == monthlyLM003Id.entType && dataYear == monthlyLM003Id.dataYear && dataMonth == monthlyLM003Id.dataMonth;
  }

  @Override
  public String toString() {
    return "MonthlyLM003Id [entType=" + entType + ", dataYear=" + dataYear + ", dataMonth=" + dataMonth + "]";
  }
}
