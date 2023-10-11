package com.st1.itx.db.domain;

import java.io.Serializable;
import java.util.Objects;
import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import com.st1.itx.util.StaticTool;
import com.st1.itx.Exception.LogicException;

/**
 * MonthlyLM042Statis LM042RBC統計數<br>
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
@Embeddable
public class MonthlyLM042StatisId implements Serializable {


  // 資料年月
  @Column(name = "`YearMonth`")
  private int yearMonth = 0;

  // 放款項目
  /* A：銀行保證放款B：動產擔保放款C：不動產擔保放款D：有價證券質押放款Z：政策性專案貸款6：折溢價與費用 */
  @Column(name = "`LoanItem`", length = 1)
  private String loanItem = " ";

  // 是否為利害關係人
  /* Y/N */
  @Column(name = "`RelatedCode`", length = 1)
  private String relatedCode = " ";

  // 資產五分類代號
  @Column(name = "`AssetClass`", length = 1)
  private String assetClass = " ";

  public MonthlyLM042StatisId() {
  }

  public MonthlyLM042StatisId(int yearMonth, String loanItem, String relatedCode, String assetClass) {
    this.yearMonth = yearMonth;
    this.loanItem = loanItem;
    this.relatedCode = relatedCode;
    this.assetClass = assetClass;
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
	* 放款項目<br>
	* A：銀行保證放款
B：動產擔保放款
C：不動產擔保放款
D：有價證券質押放款
Z：政策性專案貸款
6：折溢價與費用
	* @return String
	*/
  public String getLoanItem() {
    return this.loanItem == null ? "" : this.loanItem;
  }

/**
	* 放款項目<br>
	* A：銀行保證放款
B：動產擔保放款
C：不動產擔保放款
D：有價證券質押放款
Z：政策性專案貸款
6：折溢價與費用
  *
  * @param loanItem 放款項目
	*/
  public void setLoanItem(String loanItem) {
    this.loanItem = loanItem;
  }

/**
	* 是否為利害關係人<br>
	* Y/N
	* @return String
	*/
  public String getRelatedCode() {
    return this.relatedCode == null ? "" : this.relatedCode;
  }

/**
	* 是否為利害關係人<br>
	* Y/N
  *
  * @param relatedCode 是否為利害關係人
	*/
  public void setRelatedCode(String relatedCode) {
    this.relatedCode = relatedCode;
  }

/**
	* 資產五分類代號<br>
	* 
	* @return String
	*/
  public String getAssetClass() {
    return this.assetClass == null ? "" : this.assetClass;
  }

/**
	* 資產五分類代號<br>
	* 
  *
  * @param assetClass 資產五分類代號
	*/
  public void setAssetClass(String assetClass) {
    this.assetClass = assetClass;
  }


  @Override
  public int hashCode() {
    return Objects.hash(yearMonth, loanItem, relatedCode, assetClass);
  }

  @Override
  public boolean equals(Object obj) {
    if(this == obj)
      return true;
    if(obj == null || getClass() != obj.getClass())
      return false;
    MonthlyLM042StatisId monthlyLM042StatisId = (MonthlyLM042StatisId) obj;
    return yearMonth == monthlyLM042StatisId.yearMonth && loanItem.equals(monthlyLM042StatisId.loanItem) && relatedCode.equals(monthlyLM042StatisId.relatedCode) && assetClass.equals(monthlyLM042StatisId.assetClass);
  }

  @Override
  public String toString() {
    return "MonthlyLM042StatisId [yearMonth=" + yearMonth + ", loanItem=" + loanItem + ", relatedCode=" + relatedCode + ", assetClass=" + assetClass + "]";
  }
}
