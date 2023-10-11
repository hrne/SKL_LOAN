package com.st1.itx.db.domain;

import java.io.Serializable;
import java.util.Objects;
import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import com.st1.itx.util.StaticTool;
import com.st1.itx.Exception.LogicException;

/**
 * MonthlyLM042RBC LM042RBC會計報表<br>
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
@Embeddable
public class MonthlyLM042RBCId implements Serializable {


  // 資料年月
  @Column(name = "`YearMonth`")
  private int yearMonth = 0;

  // 放款種類
  /* 1:一般放款2:專案放款 */
  @Column(name = "`LoanType`", length = 1)
  private String loanType = " ";

  // 放款項目
  /* A：非授信限制對象-銀行保證放款B：非授信限制對象-動產擔保放款C： 非授信限制對象-不動產擔保放款D：非授信限制對象-有價證券質押放款E： 授信限制對象-非具控制與從屬關係F：授信限制對象-具控制與從屬關係 */
  @Column(name = "`LoanItem`", length = 1)
  private String loanItem = " ";

  public MonthlyLM042RBCId() {
  }

  public MonthlyLM042RBCId(int yearMonth, String loanType, String loanItem) {
    this.yearMonth = yearMonth;
    this.loanType = loanType;
    this.loanItem = loanItem;
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
	* 1:一般放款
2:專案放款
	* @return String
	*/
  public String getLoanType() {
    return this.loanType == null ? "" : this.loanType;
  }

/**
	* 放款種類<br>
	* 1:一般放款
2:專案放款
  *
  * @param loanType 放款種類
	*/
  public void setLoanType(String loanType) {
    this.loanType = loanType;
  }

/**
	* 放款項目<br>
	* A：非授信限制對象-銀行保證放款
B：非授信限制對象-動產擔保放款
C： 非授信限制對象-不動產擔保放款
D：非授信限制對象-有價證券質押放款
E： 授信限制對象-非具控制與從屬關係
F：授信限制對象-具控制與從屬關係
	* @return String
	*/
  public String getLoanItem() {
    return this.loanItem == null ? "" : this.loanItem;
  }

/**
	* 放款項目<br>
	* A：非授信限制對象-銀行保證放款
B：非授信限制對象-動產擔保放款
C： 非授信限制對象-不動產擔保放款
D：非授信限制對象-有價證券質押放款
E： 授信限制對象-非具控制與從屬關係
F：授信限制對象-具控制與從屬關係
  *
  * @param loanItem 放款項目
	*/
  public void setLoanItem(String loanItem) {
    this.loanItem = loanItem;
  }


  @Override
  public int hashCode() {
    return Objects.hash(yearMonth, loanType, loanItem);
  }

  @Override
  public boolean equals(Object obj) {
    if(this == obj)
      return true;
    if(obj == null || getClass() != obj.getClass())
      return false;
    MonthlyLM042RBCId monthlyLM042RBCId = (MonthlyLM042RBCId) obj;
    return yearMonth == monthlyLM042RBCId.yearMonth && loanType.equals(monthlyLM042RBCId.loanType) && loanItem.equals(monthlyLM042RBCId.loanItem);
  }

  @Override
  public String toString() {
    return "MonthlyLM042RBCId [yearMonth=" + yearMonth + ", loanType=" + loanType + ", loanItem=" + loanItem + "]";
  }
}
