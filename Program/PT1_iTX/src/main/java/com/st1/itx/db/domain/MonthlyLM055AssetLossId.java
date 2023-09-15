package com.st1.itx.db.domain;

import java.io.Serializable;
import java.util.Objects;
import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import com.st1.itx.util.StaticTool;
import com.st1.itx.Exception.LogicException;

/**
 * MonthlyLM055AssetLoss LM055重要放款餘額明細表<br>
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
@Embeddable
public class MonthlyLM055AssetLossId implements Serializable {


  // 資料年月
  @Column(name = "`YearMonth`")
  private int yearMonth = 0;

  // 放款種類
  /* A.銀行保證放款B.動產擔保放款C.不動產抵押放款D.有價證券質押放款G.政策性專案貸款Z.折溢價與費用 */
  @Column(name = "`LoanType`", length = 1)
  private String loanType = " ";

  public MonthlyLM055AssetLossId() {
  }

  public MonthlyLM055AssetLossId(int yearMonth, String loanType) {
    this.yearMonth = yearMonth;
    this.loanType = loanType;
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
	* 放款種類<br>
	* A.銀行保證放款
B.動產擔保放款
C.不動產抵押放款
D.有價證券質押放款
G.政策性專案貸款
Z.折溢價與費用
	* @return String
	*/
  public String getLoanType() {
    return this.loanType == null ? "" : this.loanType;
  }

/**
	* 放款種類<br>
	* A.銀行保證放款
B.動產擔保放款
C.不動產抵押放款
D.有價證券質押放款
G.政策性專案貸款
Z.折溢價與費用
  *
  * @param loanType 放款種類
	*/
  public void setLoanType(String loanType) {
    this.loanType = loanType;
  }


  @Override
  public int hashCode() {
    return Objects.hash(yearMonth, loanType);
  }

  @Override
  public boolean equals(Object obj) {
    if(this == obj)
      return true;
    if(obj == null || getClass() != obj.getClass())
      return false;
    MonthlyLM055AssetLossId monthlyLM055AssetLossId = (MonthlyLM055AssetLossId) obj;
    return yearMonth == monthlyLM055AssetLossId.yearMonth && loanType.equals(monthlyLM055AssetLossId.loanType);
  }

  @Override
  public String toString() {
    return "MonthlyLM055AssetLossId [yearMonth=" + yearMonth + ", loanType=" + loanType + "]";
  }
}
