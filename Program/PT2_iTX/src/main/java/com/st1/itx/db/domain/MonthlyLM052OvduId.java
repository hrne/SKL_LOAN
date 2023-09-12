package com.st1.itx.db.domain;

import java.io.Serializable;
import java.util.Objects;
import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import com.st1.itx.util.StaticTool;
import com.st1.itx.Exception.LogicException;

/**
 * MonthlyLM052Ovdu LM052逾期分類表<br>
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
@Embeddable
public class MonthlyLM052OvduId implements Serializable {


  // 資料年月
  @Column(name = "`YearMonth`")
  private int yearMonth = 0;

  // 逾期期數代號
  /* 1:逾清償1期2:逾清償2期3:逾清償3-6期 */
  @Column(name = "`OvduNo`", length = 1)
  private String ovduNo = " ";

  // 業務科目
  /* 310:短期放款320:中期放款330:長期放款 */
  @Column(name = "`AcctCode`", length = 3)
  private String acctCode = " ";

  public MonthlyLM052OvduId() {
  }

  public MonthlyLM052OvduId(int yearMonth, String ovduNo, String acctCode) {
    this.yearMonth = yearMonth;
    this.ovduNo = ovduNo;
    this.acctCode = acctCode;
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
	* 逾期期數代號<br>
	* 1:逾清償1期
2:逾清償2期
3:逾清償3-6期
	* @return String
	*/
  public String getOvduNo() {
    return this.ovduNo == null ? "" : this.ovduNo;
  }

/**
	* 逾期期數代號<br>
	* 1:逾清償1期
2:逾清償2期
3:逾清償3-6期
  *
  * @param ovduNo 逾期期數代號
	*/
  public void setOvduNo(String ovduNo) {
    this.ovduNo = ovduNo;
  }

/**
	* 業務科目<br>
	* 310:短期放款
320:中期放款
330:長期放款
	* @return String
	*/
  public String getAcctCode() {
    return this.acctCode == null ? "" : this.acctCode;
  }

/**
	* 業務科目<br>
	* 310:短期放款
320:中期放款
330:長期放款
  *
  * @param acctCode 業務科目
	*/
  public void setAcctCode(String acctCode) {
    this.acctCode = acctCode;
  }


  @Override
  public int hashCode() {
    return Objects.hash(yearMonth, ovduNo, acctCode);
  }

  @Override
  public boolean equals(Object obj) {
    if(this == obj)
      return true;
    if(obj == null || getClass() != obj.getClass())
      return false;
    MonthlyLM052OvduId monthlyLM052OvduId = (MonthlyLM052OvduId) obj;
    return yearMonth == monthlyLM052OvduId.yearMonth && ovduNo.equals(monthlyLM052OvduId.ovduNo) && acctCode.equals(monthlyLM052OvduId.acctCode);
  }

  @Override
  public String toString() {
    return "MonthlyLM052OvduId [yearMonth=" + yearMonth + ", ovduNo=" + ovduNo + ", acctCode=" + acctCode + "]";
  }
}
