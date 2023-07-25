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
 * CdCashFlow 現金流量預估資料檔<br>
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "`CdCashFlow`")
public class CdCashFlow implements Serializable {


  @EmbeddedId
  private CdCashFlowId cdCashFlowId;

  // 單位別
  @Column(name = "`BranchNo`", length = 4, insertable = false, updatable = false)
  private String branchNo;

  // 年月份
  @Column(name = "`DataYearMonth`", insertable = false, updatable = false)
  private int dataYearMonth = 0;

  // 旬別
  /* 1:上旬2:中旬3:下旬 */
  @Column(name = "`TenDayPeriods`", insertable = false, updatable = false)
  private int tenDayPeriods = 0;

  // 利息收入
  /* BS060維護 */
  @Column(name = "`InterestIncome`")
  private BigDecimal interestIncome = new BigDecimal("0");

  // 本金攤還金額
  /* BS060維護 */
  @Column(name = "`PrincipalAmortizeAmt`")
  private BigDecimal principalAmortizeAmt = new BigDecimal("0");

  // 提前還款金額
  @Column(name = "`PrepaymentAmt`")
  private BigDecimal prepaymentAmt = new BigDecimal("0");

  // 到期清償金額
  /* BS060維護 */
  @Column(name = "`DuePaymentAmt`")
  private BigDecimal duePaymentAmt = new BigDecimal("0");

  // 展期金額
  @Column(name = "`ExtendAmt`")
  private BigDecimal extendAmt = new BigDecimal("0");

  // 貸放金額
  @Column(name = "`LoanAmt`")
  private BigDecimal loanAmt = new BigDecimal("0");

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


  public CdCashFlowId getCdCashFlowId() {
    return this.cdCashFlowId;
  }

  public void setCdCashFlowId(CdCashFlowId cdCashFlowId) {
    this.cdCashFlowId = cdCashFlowId;
  }

/**
	* 單位別<br>
	* 
	* @return String
	*/
  public String getBranchNo() {
    return this.branchNo == null ? "" : this.branchNo;
  }

/**
	* 單位別<br>
	* 
  *
  * @param branchNo 單位別
	*/
  public void setBranchNo(String branchNo) {
    this.branchNo = branchNo;
  }

/**
	* 年月份<br>
	* 
	* @return Integer
	*/
  public int getDataYearMonth() {
    return this.dataYearMonth;
  }

/**
	* 年月份<br>
	* 
  *
  * @param dataYearMonth 年月份
	*/
  public void setDataYearMonth(int dataYearMonth) {
    this.dataYearMonth = dataYearMonth;
  }

/**
	* 旬別<br>
	* 1:上旬
2:中旬
3:下旬
	* @return Integer
	*/
  public int getTenDayPeriods() {
    return this.tenDayPeriods;
  }

/**
	* 旬別<br>
	* 1:上旬
2:中旬
3:下旬
  *
  * @param tenDayPeriods 旬別
	*/
  public void setTenDayPeriods(int tenDayPeriods) {
    this.tenDayPeriods = tenDayPeriods;
  }

/**
	* 利息收入<br>
	* BS060維護
	* @return BigDecimal
	*/
  public BigDecimal getInterestIncome() {
    return this.interestIncome;
  }

/**
	* 利息收入<br>
	* BS060維護
  *
  * @param interestIncome 利息收入
	*/
  public void setInterestIncome(BigDecimal interestIncome) {
    this.interestIncome = interestIncome;
  }

/**
	* 本金攤還金額<br>
	* BS060維護
	* @return BigDecimal
	*/
  public BigDecimal getPrincipalAmortizeAmt() {
    return this.principalAmortizeAmt;
  }

/**
	* 本金攤還金額<br>
	* BS060維護
  *
  * @param principalAmortizeAmt 本金攤還金額
	*/
  public void setPrincipalAmortizeAmt(BigDecimal principalAmortizeAmt) {
    this.principalAmortizeAmt = principalAmortizeAmt;
  }

/**
	* 提前還款金額<br>
	* 
	* @return BigDecimal
	*/
  public BigDecimal getPrepaymentAmt() {
    return this.prepaymentAmt;
  }

/**
	* 提前還款金額<br>
	* 
  *
  * @param prepaymentAmt 提前還款金額
	*/
  public void setPrepaymentAmt(BigDecimal prepaymentAmt) {
    this.prepaymentAmt = prepaymentAmt;
  }

/**
	* 到期清償金額<br>
	* BS060維護
	* @return BigDecimal
	*/
  public BigDecimal getDuePaymentAmt() {
    return this.duePaymentAmt;
  }

/**
	* 到期清償金額<br>
	* BS060維護
  *
  * @param duePaymentAmt 到期清償金額
	*/
  public void setDuePaymentAmt(BigDecimal duePaymentAmt) {
    this.duePaymentAmt = duePaymentAmt;
  }

/**
	* 展期金額<br>
	* 
	* @return BigDecimal
	*/
  public BigDecimal getExtendAmt() {
    return this.extendAmt;
  }

/**
	* 展期金額<br>
	* 
  *
  * @param extendAmt 展期金額
	*/
  public void setExtendAmt(BigDecimal extendAmt) {
    this.extendAmt = extendAmt;
  }

/**
	* 貸放金額<br>
	* 
	* @return BigDecimal
	*/
  public BigDecimal getLoanAmt() {
    return this.loanAmt;
  }

/**
	* 貸放金額<br>
	* 
  *
  * @param loanAmt 貸放金額
	*/
  public void setLoanAmt(BigDecimal loanAmt) {
    this.loanAmt = loanAmt;
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
    return "CdCashFlow [cdCashFlowId=" + cdCashFlowId + ", interestIncome=" + interestIncome + ", principalAmortizeAmt=" + principalAmortizeAmt + ", prepaymentAmt=" + prepaymentAmt
           + ", duePaymentAmt=" + duePaymentAmt + ", extendAmt=" + extendAmt + ", loanAmt=" + loanAmt + ", createDate=" + createDate + ", createEmpNo=" + createEmpNo + ", lastUpdate=" + lastUpdate
           + ", lastUpdateEmpNo=" + lastUpdateEmpNo + "]";
  }
}
