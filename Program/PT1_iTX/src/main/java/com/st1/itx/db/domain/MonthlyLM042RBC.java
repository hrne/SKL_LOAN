package com.st1.itx.db.domain;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Time;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.EntityListeners;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import javax.persistence.EmbeddedId;
import javax.persistence.Column;
import com.st1.itx.util.StaticTool;
import com.st1.itx.Exception.LogicException;

/**
 * MonthlyLM042RBC LM042RBC會計報表<br>
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "`MonthlyLM042RBC`")
public class MonthlyLM042RBC implements Serializable {


  @EmbeddedId
  private MonthlyLM042RBCId monthlyLM042RBCId;

  // 資料年月
  @Column(name = "`YearMonth`", insertable = false, updatable = false)
  private int yearMonth = 0;

  // 放款種類
  /* 1:一般放款2:專案放款 */
  @Column(name = "`LoanType`", length = 1, insertable = false, updatable = false)
  private String loanType;

  // 放款項目
  /* A：非授信限制對象-銀行保證放款B：非授信限制對象-動產擔保放款C： 非授信限制對象-不動產擔保放款D：非授信限制對象-有價證券質押放款E： 授信限制對象-具控制與從屬關係F：授信限制對象-非具控制與從屬關係 */
  @Column(name = "`LoanItem`", length = 1, insertable = false, updatable = false)
  private String loanItem;

  // 放款金額
  /* 放款餘額扣除備呆LoanType=1,LoanItem=C含折溢價及費用 */
  @Column(name = "`LoanAmount`")
  private BigDecimal loanAmount = new BigDecimal("0");

  // 風險係數
  @Column(name = "`RiskFactor`")
  private BigDecimal riskFactor = new BigDecimal("0");

  // 風險量金額
  /* 放款金額*風險係數=LoanAmount * RiskFactor */
  @Column(name = "`RiskFactorAmount`")
  private BigDecimal riskFactorAmount = new BigDecimal("0");

  // 建檔日期時間
  @CreatedDate
  @Column(name = "`CreateDate`")
  private java.sql.Timestamp createDate;

  // 建檔人員
  @Column(name = "`CreateEmpNo`", length = 6)
  private String createEmpNo;

  // 最後更新日期時間
  @LastModifiedDate
  @Column(name = "`LastUpdate`")
  private java.sql.Timestamp lastUpdate;

  // 最後更新人員
  @Column(name = "`LastUpdateEmpNo`", length = 6)
  private String lastUpdateEmpNo;


  public MonthlyLM042RBCId getMonthlyLM042RBCId() {
    return this.monthlyLM042RBCId;
  }

  public void setMonthlyLM042RBCId(MonthlyLM042RBCId monthlyLM042RBCId) {
    this.monthlyLM042RBCId = monthlyLM042RBCId;
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
E： 授信限制對象-具控制與從屬關係
F：授信限制對象-非具控制與從屬關係
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
E： 授信限制對象-具控制與從屬關係
F：授信限制對象-非具控制與從屬關係
  *
  * @param loanItem 放款項目
	*/
  public void setLoanItem(String loanItem) {
    this.loanItem = loanItem;
  }

/**
	* 放款金額<br>
	* 放款餘額扣除備呆
LoanType=1,LoanItem=C
含折溢價及費用
	* @return BigDecimal
	*/
  public BigDecimal getLoanAmount() {
    return this.loanAmount;
  }

/**
	* 放款金額<br>
	* 放款餘額扣除備呆
LoanType=1,LoanItem=C
含折溢價及費用
  *
  * @param loanAmount 放款金額
	*/
  public void setLoanAmount(BigDecimal loanAmount) {
    this.loanAmount = loanAmount;
  }

/**
	* 風險係數<br>
	* 
	* @return BigDecimal
	*/
  public BigDecimal getRiskFactor() {
    return this.riskFactor;
  }

/**
	* 風險係數<br>
	* 
  *
  * @param riskFactor 風險係數
	*/
  public void setRiskFactor(BigDecimal riskFactor) {
    this.riskFactor = riskFactor;
  }

/**
	* 風險量金額<br>
	* 放款金額*風險係數=LoanAmount * RiskFactor
	* @return BigDecimal
	*/
  public BigDecimal getRiskFactorAmount() {
    return this.riskFactorAmount;
  }

/**
	* 風險量金額<br>
	* 放款金額*風險係數=LoanAmount * RiskFactor
  *
  * @param riskFactorAmount 風險量金額
	*/
  public void setRiskFactorAmount(BigDecimal riskFactorAmount) {
    this.riskFactorAmount = riskFactorAmount;
  }

/**
	* 建檔日期時間<br>
	* 
	* @return java.sql.Timestamp
	*/
  public java.sql.Timestamp getCreateDate() {
    return this.createDate;
  }

/**
	* 建檔日期時間<br>
	* 
  *
  * @param createDate 建檔日期時間
	*/
  public void setCreateDate(java.sql.Timestamp createDate) {
    this.createDate = createDate;
  }

/**
	* 建檔人員<br>
	* 
	* @return String
	*/
  public String getCreateEmpNo() {
    return this.createEmpNo == null ? "" : this.createEmpNo;
  }

/**
	* 建檔人員<br>
	* 
  *
  * @param createEmpNo 建檔人員
	*/
  public void setCreateEmpNo(String createEmpNo) {
    this.createEmpNo = createEmpNo;
  }

/**
	* 最後更新日期時間<br>
	* 
	* @return java.sql.Timestamp
	*/
  public java.sql.Timestamp getLastUpdate() {
    return this.lastUpdate;
  }

/**
	* 最後更新日期時間<br>
	* 
  *
  * @param lastUpdate 最後更新日期時間
	*/
  public void setLastUpdate(java.sql.Timestamp lastUpdate) {
    this.lastUpdate = lastUpdate;
  }

/**
	* 最後更新人員<br>
	* 
	* @return String
	*/
  public String getLastUpdateEmpNo() {
    return this.lastUpdateEmpNo == null ? "" : this.lastUpdateEmpNo;
  }

/**
	* 最後更新人員<br>
	* 
  *
  * @param lastUpdateEmpNo 最後更新人員
	*/
  public void setLastUpdateEmpNo(String lastUpdateEmpNo) {
    this.lastUpdateEmpNo = lastUpdateEmpNo;
  }


  @Override
  public String toString() {
    return "MonthlyLM042RBC [monthlyLM042RBCId=" + monthlyLM042RBCId + ", loanAmount=" + loanAmount + ", riskFactor=" + riskFactor + ", riskFactorAmount=" + riskFactorAmount
           + ", createDate=" + createDate + ", createEmpNo=" + createEmpNo + ", lastUpdate=" + lastUpdate + ", lastUpdateEmpNo=" + lastUpdateEmpNo + "]";
  }
}
