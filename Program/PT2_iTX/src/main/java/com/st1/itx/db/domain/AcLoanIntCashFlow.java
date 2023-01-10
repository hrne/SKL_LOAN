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
import com.st1.itx.util.StaticTool;
import com.st1.itx.Exception.LogicException;

/**
 * AcLoanIntCashFlow 現金流量預估明細檔<br>
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "`AcLoanIntCashFlow`")
public class AcLoanIntCashFlow implements Serializable {


  /**
	 * 
	 */
	private static final long serialVersionUID = 4084451698026312655L;

@EmbeddedId
  private AcLoanIntCashFlowId acLoanIntCashFlowId;

  // 提息年月
  @Column(name = "`YearMonth`", insertable = false, updatable = false)
  private int yearMonth = 0;

  // 借款人戶號
  @Column(name = "`CustNo`", insertable = false, updatable = false)
  private int custNo = 0;

  // 額度編號
  @Column(name = "`FacmNo`", insertable = false, updatable = false)
  private int facmNo = 0;

  // 撥款序號
  @Column(name = "`BormNo`", insertable = false, updatable = false)
  private int bormNo = 0;

  // 期數編號
  /* 含短繳利息 */
  @Column(name = "`TermNo`", insertable = false, updatable = false)
  private int termNo = 0;

  // 計息起日
  @Column(name = "`IntStartDate`")
  private int intStartDate = 0;

  // 計息止日
  @Column(name = "`IntEndDate`")
  private int intEndDate = 0;

  // 計息本金
  @Column(name = "`Amount`")
  private BigDecimal amount = new BigDecimal("0");

  // 計息利率
  @Column(name = "`IntRate`")
  private BigDecimal intRate = new BigDecimal("0");

  // 回收本金
  @Column(name = "`Principal`")
  private BigDecimal principal = new BigDecimal("0");

  // 利息
  @Column(name = "`Interest`")
  private BigDecimal interest = new BigDecimal("0");

  // 延滯息
  @Column(name = "`DelayInt`")
  private BigDecimal delayInt = new BigDecimal("0");

  // 違約金
  @Column(name = "`BreachAmt`")
  private BigDecimal breachAmt = new BigDecimal("0");

  // 計息方式
  /* 共用代碼檔1:按日計息  2:按月計息 */
  @Column(name = "`IntCalcCode`", length = 1)
  private String intCalcCode;

  // 攤還方式
  /* 共用代碼檔1.按月繳息(按期繳息到期還本)2.到期取息(到期繳息還本)3.本息平均法(期金)4.本金平均法 */
  @Column(name = "`AmortizedCode`", length = 1)
  private String amortizedCode;

  // 業務科目代號
  /* CdAcCode 會計科子細目設定檔IC1:短擔息IC2:中擔息IC3:長擔息IC4:三十年房貸息 */
  @Column(name = "`AcctCode`", length = 3)
  private String acctCode;

  // 應繳息日
  /* 每期 */
  @Column(name = "`PayIntDate`")
  private int payIntDate = 0;

  // 放款餘額
  /* 放款餘額(還款前、只放第一期) */
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


  public AcLoanIntCashFlowId getAcLoanIntCashFlowId() {
    return this.acLoanIntCashFlowId;
  }

  public void setAcLoanIntCashFlowId(AcLoanIntCashFlowId acLoanIntCashFlowId) {
    this.acLoanIntCashFlowId = acLoanIntCashFlowId;
  }

/**
	* 提息年月<br>
	* 
	* @return Integer
	*/
  public int getYearMonth() {
    return this.yearMonth;
  }

/**
	* 提息年月<br>
	* 
  *
  * @param yearMonth 提息年月
	*/
  public void setYearMonth(int yearMonth) {
    this.yearMonth = yearMonth;
  }

/**
	* 借款人戶號<br>
	* 
	* @return Integer
	*/
  public int getCustNo() {
    return this.custNo;
  }

/**
	* 借款人戶號<br>
	* 
  *
  * @param custNo 借款人戶號
	*/
  public void setCustNo(int custNo) {
    this.custNo = custNo;
  }

/**
	* 額度編號<br>
	* 
	* @return Integer
	*/
  public int getFacmNo() {
    return this.facmNo;
  }

/**
	* 額度編號<br>
	* 
  *
  * @param facmNo 額度編號
	*/
  public void setFacmNo(int facmNo) {
    this.facmNo = facmNo;
  }

/**
	* 撥款序號<br>
	* 
	* @return Integer
	*/
  public int getBormNo() {
    return this.bormNo;
  }

/**
	* 撥款序號<br>
	* 
  *
  * @param bormNo 撥款序號
	*/
  public void setBormNo(int bormNo) {
    this.bormNo = bormNo;
  }

/**
	* 期數編號<br>
	* 含短繳利息
	* @return Integer
	*/
  public int getTermNo() {
    return this.termNo;
  }

/**
	* 期數編號<br>
	* 含短繳利息
  *
  * @param termNo 期數編號
	*/
  public void setTermNo(int termNo) {
    this.termNo = termNo;
  }

/**
	* 計息起日<br>
	* 
	* @return Integer
	*/
  public int getIntStartDate() {
    return StaticTool.bcToRoc(this.intStartDate);
  }

/**
	* 計息起日<br>
	* 
  *
  * @param intStartDate 計息起日
  * @throws LogicException when Date Is Warn	*/
  public void setIntStartDate(int intStartDate) throws LogicException {
    this.intStartDate = StaticTool.rocToBc(intStartDate);
  }

/**
	* 計息止日<br>
	* 
	* @return Integer
	*/
  public int getIntEndDate() {
    return StaticTool.bcToRoc(this.intEndDate);
  }

/**
	* 計息止日<br>
	* 
  *
  * @param intEndDate 計息止日
  * @throws LogicException when Date Is Warn	*/
  public void setIntEndDate(int intEndDate) throws LogicException {
    this.intEndDate = StaticTool.rocToBc(intEndDate);
  }

/**
	* 計息本金<br>
	* 
	* @return BigDecimal
	*/
  public BigDecimal getAmount() {
    return this.amount;
  }

/**
	* 計息本金<br>
	* 
  *
  * @param amount 計息本金
	*/
  public void setAmount(BigDecimal amount) {
    this.amount = amount;
  }

/**
	* 計息利率<br>
	* 
	* @return BigDecimal
	*/
  public BigDecimal getIntRate() {
    return this.intRate;
  }

/**
	* 計息利率<br>
	* 
  *
  * @param intRate 計息利率
	*/
  public void setIntRate(BigDecimal intRate) {
    this.intRate = intRate;
  }

/**
	* 回收本金<br>
	* 
	* @return BigDecimal
	*/
  public BigDecimal getPrincipal() {
    return this.principal;
  }

/**
	* 回收本金<br>
	* 
  *
  * @param principal 回收本金
	*/
  public void setPrincipal(BigDecimal principal) {
    this.principal = principal;
  }

/**
	* 利息<br>
	* 
	* @return BigDecimal
	*/
  public BigDecimal getInterest() {
    return this.interest;
  }

/**
	* 利息<br>
	* 
  *
  * @param interest 利息
	*/
  public void setInterest(BigDecimal interest) {
    this.interest = interest;
  }

/**
	* 延滯息<br>
	* 
	* @return BigDecimal
	*/
  public BigDecimal getDelayInt() {
    return this.delayInt;
  }

/**
	* 延滯息<br>
	* 
  *
  * @param delayInt 延滯息
	*/
  public void setDelayInt(BigDecimal delayInt) {
    this.delayInt = delayInt;
  }

/**
	* 違約金<br>
	* 
	* @return BigDecimal
	*/
  public BigDecimal getBreachAmt() {
    return this.breachAmt;
  }

/**
	* 違約金<br>
	* 
  *
  * @param breachAmt 違約金
	*/
  public void setBreachAmt(BigDecimal breachAmt) {
    this.breachAmt = breachAmt;
  }

/**
	* 計息方式<br>
	* 共用代碼檔
1:按日計息  
2:按月計息
	* @return String
	*/
  public String getIntCalcCode() {
    return this.intCalcCode == null ? "" : this.intCalcCode;
  }

/**
	* 計息方式<br>
	* 共用代碼檔
1:按日計息  
2:按月計息
  *
  * @param intCalcCode 計息方式
	*/
  public void setIntCalcCode(String intCalcCode) {
    this.intCalcCode = intCalcCode;
  }

/**
	* 攤還方式<br>
	* 共用代碼檔
1.按月繳息(按期繳息到期還本)
2.到期取息(到期繳息還本)
3.本息平均法(期金)
4.本金平均法
	* @return String
	*/
  public String getAmortizedCode() {
    return this.amortizedCode == null ? "" : this.amortizedCode;
  }

/**
	* 攤還方式<br>
	* 共用代碼檔
1.按月繳息(按期繳息到期還本)
2.到期取息(到期繳息還本)
3.本息平均法(期金)
4.本金平均法
  *
  * @param amortizedCode 攤還方式
	*/
  public void setAmortizedCode(String amortizedCode) {
    this.amortizedCode = amortizedCode;
  }

/**
	* 業務科目代號<br>
	* CdAcCode 會計科子細目設定檔
IC1:短擔息
IC2:中擔息
IC3:長擔息
IC4:三十年房貸息
	* @return String
	*/
  public String getAcctCode() {
    return this.acctCode == null ? "" : this.acctCode;
  }

/**
	* 業務科目代號<br>
	* CdAcCode 會計科子細目設定檔
IC1:短擔息
IC2:中擔息
IC3:長擔息
IC4:三十年房貸息
  *
  * @param acctCode 業務科目代號
	*/
  public void setAcctCode(String acctCode) {
    this.acctCode = acctCode;
  }

/**
	* 應繳息日<br>
	* 每期
	* @return Integer
	*/
  public int getPayIntDate() {
    return StaticTool.bcToRoc(this.payIntDate);
  }

/**
	* 應繳息日<br>
	* 每期
  *
  * @param payIntDate 應繳息日
  * @throws LogicException when Date Is Warn	*/
  public void setPayIntDate(int payIntDate) throws LogicException {
    this.payIntDate = StaticTool.rocToBc(payIntDate);
  }

/**
	* 放款餘額<br>
	* 放款餘額(還款前、只放第一期)
	* @return BigDecimal
	*/
  public BigDecimal getLoanBal() {
    return this.loanBal;
  }

/**
	* 放款餘額<br>
	* 放款餘額(還款前、只放第一期)
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
    return "AcLoanIntCashFlow [acLoanIntCashFlowId=" + acLoanIntCashFlowId + ", intStartDate=" + intStartDate
           + ", intEndDate=" + intEndDate + ", amount=" + amount + ", intRate=" + intRate + ", principal=" + principal + ", interest=" + interest + ", delayInt=" + delayInt
           + ", breachAmt=" + breachAmt + ", intCalcCode=" + intCalcCode + ", amortizedCode=" + amortizedCode + ", acctCode=" + acctCode + ", payIntDate=" + payIntDate + ", loanBal=" + loanBal
           + ", createDate=" + createDate + ", createEmpNo=" + createEmpNo + ", lastUpdate=" + lastUpdate + ", lastUpdateEmpNo=" + lastUpdateEmpNo + "]";
  }
}
