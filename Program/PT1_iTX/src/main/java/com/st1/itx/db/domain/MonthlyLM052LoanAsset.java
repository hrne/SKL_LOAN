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
 * MonthlyLM052LoanAsset LM052放款資產表<br>
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "`MonthlyLM052LoanAsset`")
public class MonthlyLM052LoanAsset implements Serializable {


  /**
	 * 
	 */
	private static final long serialVersionUID = 4470696799520972403L;

@EmbeddedId
  private MonthlyLM052LoanAssetId monthlyLM052LoanAssetId;

  // 資料年月
  @Column(name = "`YearMonth`", insertable = false, updatable = false)
  private int yearMonth = 0;

  // 放款資產項目
  /* ref CdCode.LoanAssetCode特定資產：S1:購置住宅+修繕貸款S2:建築貸款非特定資產：NS1:100年後政策性貸款NS2:股票質押NS3:不動產抵押貸款 */
  @Column(name = "`LoanAssetCode`", length = 3, insertable = false, updatable = false)
  private String loanAssetCode;

  // 放款餘額
  @Column(name = "`LoanBal`")
  private BigDecimal loanBal = new BigDecimal("0");

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


  public MonthlyLM052LoanAssetId getMonthlyLM052LoanAssetId() {
    return this.monthlyLM052LoanAssetId;
  }

  public void setMonthlyLM052LoanAssetId(MonthlyLM052LoanAssetId monthlyLM052LoanAssetId) {
    this.monthlyLM052LoanAssetId = monthlyLM052LoanAssetId;
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
S1:購置住宅+修繕貸款
S2:建築貸款
非特定資產：
NS1:100年後政策性貸款
NS2:股票質押
NS3:不動產抵押貸款
	* @return String
	*/
  public String getLoanAssetCode() {
    return this.loanAssetCode == null ? "" : this.loanAssetCode;
  }

/**
	* 放款資產項目<br>
	* ref CdCode.LoanAssetCode
特定資產：
S1:購置住宅+修繕貸款
S2:建築貸款
非特定資產：
NS1:100年後政策性貸款
NS2:股票質押
NS3:不動產抵押貸款
  *
  * @param loanAssetCode 放款資產項目
	*/
  public void setLoanAssetCode(String loanAssetCode) {
    this.loanAssetCode = loanAssetCode;
  }

/**
	* 放款餘額<br>
	* 
	* @return BigDecimal
	*/
  public BigDecimal getLoanBal() {
    return this.loanBal;
  }

/**
	* 放款餘額<br>
	* 
  *
  * @param loanBal 放款餘額
	*/
  public void setLoanBal(BigDecimal loanBal) {
    this.loanBal = loanBal;
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
    return "MonthlyLM052LoanAsset [monthlyLM052LoanAssetId=" + monthlyLM052LoanAssetId + ", loanBal=" + loanBal + ", createDate=" + createDate + ", createEmpNo=" + createEmpNo + ", lastUpdate=" + lastUpdate
           + ", lastUpdateEmpNo=" + lastUpdateEmpNo + "]";
  }
}
