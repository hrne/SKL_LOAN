package com.st1.itx.db.domain;

import java.io.Serializable;
import java.math.BigDecimal;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.EntityListeners;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import javax.persistence.EmbeddedId;
import javax.persistence.Column;

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


  /**
	 * 
	 */
	private static final long serialVersionUID = -4408816963666083608L;

@EmbeddedId
  private MonthlyLM042RBCId monthlyLM042RBCId;

  // 資料年月
  @Column(name = "`YearMonth`", insertable = false, updatable = false)
  private int yearMonth = 0;

  // 放款種類
  @Column(name = "`LoanType`", length = 1, insertable = false, updatable = false)
  private String loanType;

  // 放款項目
  @Column(name = "`LoanItem`", length = 1, insertable = false, updatable = false)
  private String loanItem;

  // 對象關係人
  @Column(name = "`RelatedCode`", length = 1, insertable = false, updatable = false)
  private String relatedCode;

  // 放款金額
  @Column(name = "`LoanAmount`")
  private BigDecimal loanAmount = new BigDecimal("0");

  // 風險係數
  @Column(name = "`RiskFactor`")
  private BigDecimal riskFactor = new BigDecimal("0");

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
	* 
	* @return String
	*/
  public String getLoanType() {
    return this.loanType == null ? "" : this.loanType;
  }

/**
	* 放款種類<br>
	* 
  *
  * @param loanType 放款種類
	*/
  public void setLoanType(String loanType) {
    this.loanType = loanType;
  }

/**
	* 放款項目<br>
	* 
	* @return String
	*/
  public String getLoanItem() {
    return this.loanItem == null ? "" : this.loanItem;
  }

/**
	* 放款項目<br>
	* 
  *
  * @param loanItem 放款項目
	*/
  public void setLoanItem(String loanItem) {
    this.loanItem = loanItem;
  }

/**
	* 對象關係人<br>
	* 
	* @return String
	*/
  public String getRelatedCode() {
    return this.relatedCode == null ? "" : this.relatedCode;
  }

/**
	* 對象關係人<br>
	* 
  *
  * @param relatedCode 對象關係人
	*/
  public void setRelatedCode(String relatedCode) {
    this.relatedCode = relatedCode;
  }

/**
	* 放款金額<br>
	* 
	* @return BigDecimal
	*/
  public BigDecimal getLoanAmount() {
    return this.loanAmount;
  }

/**
	* 放款金額<br>
	* 
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
    return "MonthlyLM042RBC [monthlyLM042RBCId=" + monthlyLM042RBCId + ", loanAmount=" + loanAmount + ", riskFactor=" + riskFactor
           + ", createDate=" + createDate + ", createEmpNo=" + createEmpNo + ", lastUpdate=" + lastUpdate + ", lastUpdateEmpNo=" + lastUpdateEmpNo + "]";
  }
}
