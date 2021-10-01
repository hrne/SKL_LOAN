package com.st1.itx.db.domain;

import java.io.Serializable;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 * MonthlyLM052LoanAsset LM052放款資產表<br>
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
@Embeddable
public class MonthlyLM052LoanAssetId implements Serializable {


  /**
	 * 
	 */
	private static final long serialVersionUID = 595665686952321601L;

// 資料年月
  @Column(name = "`YearMonth`")
  private int yearMonth = 0;

  // 放款資產項目
  /* ref CdCode.LoanAssetCode特定資產：S1：購置住宅+修繕貸款S2：建築貸款非特定資產：NS1：100年後政策性貸款NS2：股票質押NS3：不動產抵押貸款 */
  @Column(name = "`LoanAssetCode`", length = 3)
  private String loanAssetCode = " ";

  public MonthlyLM052LoanAssetId() {
  }

  public MonthlyLM052LoanAssetId(int yearMonth, String loanAssetCode) {
    this.yearMonth = yearMonth;
    this.loanAssetCode = loanAssetCode;
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
	* 放款資產項目<br>
	* ref CdCode.LoanAssetCode
特定資產：
S1：購置住宅+修繕貸款
S2：建築貸款
非特定資產：
NS1：100年後政策性貸款
NS2：股票質押
NS3：不動產抵押貸款
	* @return String
	*/
  public String getLoanAssetCode() {
    return this.loanAssetCode == null ? "" : this.loanAssetCode;
  }

/**
	* 放款資產項目<br>
	* ref CdCode.LoanAssetCode
特定資產：
S1：購置住宅+修繕貸款
S2：建築貸款
非特定資產：
NS1：100年後政策性貸款
NS2：股票質押
NS3：不動產抵押貸款
  *
  * @param loanAssetCode 放款資產項目
	*/
  public void setLoanAssetCode(String loanAssetCode) {
    this.loanAssetCode = loanAssetCode;
  }


  @Override
  public int hashCode() {
    return Objects.hash(yearMonth, loanAssetCode);
  }

  @Override
  public boolean equals(Object obj) {
    if(this == obj)
      return true;
    if(obj == null || getClass() != obj.getClass())
      return false;
    MonthlyLM052LoanAssetId monthlyLM052LoanAssetId = (MonthlyLM052LoanAssetId) obj;
    return yearMonth == monthlyLM052LoanAssetId.yearMonth && loanAssetCode.equals(monthlyLM052LoanAssetId.loanAssetCode);
  }

  @Override
  public String toString() {
    return "MonthlyLM052LoanAssetId [yearMonth=" + yearMonth + ", loanAssetCode=" + loanAssetCode + "]";
  }
}
