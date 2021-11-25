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
 * LoanOverdue 催收呆帳檔<br>
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "`LoanOverdue`")
public class LoanOverdue implements Serializable {


  /**
	 * 
	 */
	private static final long serialVersionUID = 5707264757202613143L;

@EmbeddedId
  private LoanOverdueId loanOverdueId;

  // 借款人戶號
  @Column(name = "`CustNo`", insertable = false, updatable = false)
  private int custNo = 0;

  // 額度編號
  @Column(name = "`FacmNo`", insertable = false, updatable = false)
  private int facmNo = 0;

  // 撥款序號
  @Column(name = "`BormNo`", insertable = false, updatable = false)
  private int bormNo = 0;

  // 催收序號
  @Column(name = "`OvduNo`", insertable = false, updatable = false)
  private int ovduNo = 0;

  // 狀態
  /* 1: 催收2. 部分轉呆3: 呆帳4: 催收回復5: 催呆結案 */
  @Column(name = "`Status`")
  private int status = 0;

  // 帳務科目
  /* 共用代碼檔990: 催收款項 */
  @Column(name = "`AcctCode`", length = 3)
  private String acctCode;

  // 轉催收日期
  @Column(name = "`OvduDate`")
  private int ovduDate = 0;

  // 轉呆帳日期
  @Column(name = "`BadDebtDate`")
  private int badDebtDate = 0;

  // 催收回復日期
  @Column(name = "`ReplyDate`")
  private int replyDate = 0;

  // 轉催收本金
  @Column(name = "`OvduPrinAmt`")
  private BigDecimal ovduPrinAmt = new BigDecimal("0");

  // 轉催收利息
  @Column(name = "`OvduIntAmt`")
  private BigDecimal ovduIntAmt = new BigDecimal("0");

  // 轉催收違約金
  /* 未用 */
  @Column(name = "`OvduBreachAmt`")
  private BigDecimal ovduBreachAmt = new BigDecimal("0");

  // 轉催收金額
  @Column(name = "`OvduAmt`")
  private BigDecimal ovduAmt = new BigDecimal("0");

  // 催收本金餘額
  @Column(name = "`OvduPrinBal`")
  private BigDecimal ovduPrinBal = new BigDecimal("0");

  // 催收利息餘額
  @Column(name = "`OvduIntBal`")
  private BigDecimal ovduIntBal = new BigDecimal("0");

  // 催收違約金餘額
  /* 未用 */
  @Column(name = "`OvduBreachBal`")
  private BigDecimal ovduBreachBal = new BigDecimal("0");

  // 催收餘額
  @Column(name = "`OvduBal`")
  private BigDecimal ovduBal = new BigDecimal("0");

  // 減免利息金額
  @Column(name = "`ReduceInt`")
  private BigDecimal reduceInt = new BigDecimal("0");

  // 減免違約金金額
  @Column(name = "`ReduceBreach`")
  private BigDecimal reduceBreach = new BigDecimal("0");

  // 轉呆帳金額
  /* 含火險費、催收火險費、法務費、催收法務費轉呆金額 */
  @Column(name = "`BadDebtAmt`")
  private BigDecimal badDebtAmt = new BigDecimal("0");

  // 呆帳餘額
  @Column(name = "`BadDebtBal`")
  private BigDecimal badDebtBal = new BigDecimal("0");

  // 催收回復減免金額
  @Column(name = "`ReplyReduceAmt`")
  private BigDecimal replyReduceAmt = new BigDecimal("0");

  // 處理日期
  @Column(name = "`ProcessDate`")
  private int processDate = 0;

  // 催收處理情形
  @Column(name = "`OvduSituaction`", length = 30)
  private String ovduSituaction;

  // 附言
  @Column(name = "`Remark`", length = 60)
  private String remark;

  // 會計日期
  @Column(name = "`AcDate`")
  private int acDate = 0;

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


  public LoanOverdueId getLoanOverdueId() {
    return this.loanOverdueId;
  }

  public void setLoanOverdueId(LoanOverdueId loanOverdueId) {
    this.loanOverdueId = loanOverdueId;
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
	* 催收序號<br>
	* 
	* @return Integer
	*/
  public int getOvduNo() {
    return this.ovduNo;
  }

/**
	* 催收序號<br>
	* 
  *
  * @param ovduNo 催收序號
	*/
  public void setOvduNo(int ovduNo) {
    this.ovduNo = ovduNo;
  }

/**
	* 狀態<br>
	* 1: 催收
2. 部分轉呆
3: 呆帳
4: 催收回復
5: 催呆結案
	* @return Integer
	*/
  public int getStatus() {
    return this.status;
  }

/**
	* 狀態<br>
	* 1: 催收
2. 部分轉呆
3: 呆帳
4: 催收回復
5: 催呆結案
  *
  * @param status 狀態
	*/
  public void setStatus(int status) {
    this.status = status;
  }

/**
	* 帳務科目<br>
	* 共用代碼檔
990: 催收款項
	* @return String
	*/
  public String getAcctCode() {
    return this.acctCode == null ? "" : this.acctCode;
  }

/**
	* 帳務科目<br>
	* 共用代碼檔
990: 催收款項
  *
  * @param acctCode 帳務科目
	*/
  public void setAcctCode(String acctCode) {
    this.acctCode = acctCode;
  }

/**
	* 轉催收日期<br>
	* 
	* @return Integer
	*/
  public int getOvduDate() {
    return StaticTool.bcToRoc(this.ovduDate);
  }

/**
	* 轉催收日期<br>
	* 
  *
  * @param ovduDate 轉催收日期
  * @throws LogicException when Date Is Warn	*/
  public void setOvduDate(int ovduDate) throws LogicException {
    this.ovduDate = StaticTool.rocToBc(ovduDate);
  }

/**
	* 轉呆帳日期<br>
	* 
	* @return Integer
	*/
  public int getBadDebtDate() {
    return StaticTool.bcToRoc(this.badDebtDate);
  }

/**
	* 轉呆帳日期<br>
	* 
  *
  * @param badDebtDate 轉呆帳日期
  * @throws LogicException when Date Is Warn	*/
  public void setBadDebtDate(int badDebtDate) throws LogicException {
    this.badDebtDate = StaticTool.rocToBc(badDebtDate);
  }

/**
	* 催收回復日期<br>
	* 
	* @return Integer
	*/
  public int getReplyDate() {
    return StaticTool.bcToRoc(this.replyDate);
  }

/**
	* 催收回復日期<br>
	* 
  *
  * @param replyDate 催收回復日期
  * @throws LogicException when Date Is Warn	*/
  public void setReplyDate(int replyDate) throws LogicException {
    this.replyDate = StaticTool.rocToBc(replyDate);
  }

/**
	* 轉催收本金<br>
	* 
	* @return BigDecimal
	*/
  public BigDecimal getOvduPrinAmt() {
    return this.ovduPrinAmt;
  }

/**
	* 轉催收本金<br>
	* 
  *
  * @param ovduPrinAmt 轉催收本金
	*/
  public void setOvduPrinAmt(BigDecimal ovduPrinAmt) {
    this.ovduPrinAmt = ovduPrinAmt;
  }

/**
	* 轉催收利息<br>
	* 
	* @return BigDecimal
	*/
  public BigDecimal getOvduIntAmt() {
    return this.ovduIntAmt;
  }

/**
	* 轉催收利息<br>
	* 
  *
  * @param ovduIntAmt 轉催收利息
	*/
  public void setOvduIntAmt(BigDecimal ovduIntAmt) {
    this.ovduIntAmt = ovduIntAmt;
  }

/**
	* 轉催收違約金<br>
	* 未用
	* @return BigDecimal
	*/
  public BigDecimal getOvduBreachAmt() {
    return this.ovduBreachAmt;
  }

/**
	* 轉催收違約金<br>
	* 未用
  *
  * @param ovduBreachAmt 轉催收違約金
	*/
  public void setOvduBreachAmt(BigDecimal ovduBreachAmt) {
    this.ovduBreachAmt = ovduBreachAmt;
  }

/**
	* 轉催收金額<br>
	* 
	* @return BigDecimal
	*/
  public BigDecimal getOvduAmt() {
    return this.ovduAmt;
  }

/**
	* 轉催收金額<br>
	* 
  *
  * @param ovduAmt 轉催收金額
	*/
  public void setOvduAmt(BigDecimal ovduAmt) {
    this.ovduAmt = ovduAmt;
  }

/**
	* 催收本金餘額<br>
	* 
	* @return BigDecimal
	*/
  public BigDecimal getOvduPrinBal() {
    return this.ovduPrinBal;
  }

/**
	* 催收本金餘額<br>
	* 
  *
  * @param ovduPrinBal 催收本金餘額
	*/
  public void setOvduPrinBal(BigDecimal ovduPrinBal) {
    this.ovduPrinBal = ovduPrinBal;
  }

/**
	* 催收利息餘額<br>
	* 
	* @return BigDecimal
	*/
  public BigDecimal getOvduIntBal() {
    return this.ovduIntBal;
  }

/**
	* 催收利息餘額<br>
	* 
  *
  * @param ovduIntBal 催收利息餘額
	*/
  public void setOvduIntBal(BigDecimal ovduIntBal) {
    this.ovduIntBal = ovduIntBal;
  }

/**
	* 催收違約金餘額<br>
	* 未用
	* @return BigDecimal
	*/
  public BigDecimal getOvduBreachBal() {
    return this.ovduBreachBal;
  }

/**
	* 催收違約金餘額<br>
	* 未用
  *
  * @param ovduBreachBal 催收違約金餘額
	*/
  public void setOvduBreachBal(BigDecimal ovduBreachBal) {
    this.ovduBreachBal = ovduBreachBal;
  }

/**
	* 催收餘額<br>
	* 
	* @return BigDecimal
	*/
  public BigDecimal getOvduBal() {
    return this.ovduBal;
  }

/**
	* 催收餘額<br>
	* 
  *
  * @param ovduBal 催收餘額
	*/
  public void setOvduBal(BigDecimal ovduBal) {
    this.ovduBal = ovduBal;
  }

/**
	* 減免利息金額<br>
	* 
	* @return BigDecimal
	*/
  public BigDecimal getReduceInt() {
    return this.reduceInt;
  }

/**
	* 減免利息金額<br>
	* 
  *
  * @param reduceInt 減免利息金額
	*/
  public void setReduceInt(BigDecimal reduceInt) {
    this.reduceInt = reduceInt;
  }

/**
	* 減免違約金金額<br>
	* 
	* @return BigDecimal
	*/
  public BigDecimal getReduceBreach() {
    return this.reduceBreach;
  }

/**
	* 減免違約金金額<br>
	* 
  *
  * @param reduceBreach 減免違約金金額
	*/
  public void setReduceBreach(BigDecimal reduceBreach) {
    this.reduceBreach = reduceBreach;
  }

/**
	* 轉呆帳金額<br>
	* 含火險費、催收火險費、法務費、催收法務費轉呆金額
	* @return BigDecimal
	*/
  public BigDecimal getBadDebtAmt() {
    return this.badDebtAmt;
  }

/**
	* 轉呆帳金額<br>
	* 含火險費、催收火險費、法務費、催收法務費轉呆金額
  *
  * @param badDebtAmt 轉呆帳金額
	*/
  public void setBadDebtAmt(BigDecimal badDebtAmt) {
    this.badDebtAmt = badDebtAmt;
  }

/**
	* 呆帳餘額<br>
	* 
	* @return BigDecimal
	*/
  public BigDecimal getBadDebtBal() {
    return this.badDebtBal;
  }

/**
	* 呆帳餘額<br>
	* 
  *
  * @param badDebtBal 呆帳餘額
	*/
  public void setBadDebtBal(BigDecimal badDebtBal) {
    this.badDebtBal = badDebtBal;
  }

/**
	* 催收回復減免金額<br>
	* 
	* @return BigDecimal
	*/
  public BigDecimal getReplyReduceAmt() {
    return this.replyReduceAmt;
  }

/**
	* 催收回復減免金額<br>
	* 
  *
  * @param replyReduceAmt 催收回復減免金額
	*/
  public void setReplyReduceAmt(BigDecimal replyReduceAmt) {
    this.replyReduceAmt = replyReduceAmt;
  }

/**
	* 處理日期<br>
	* 
	* @return Integer
	*/
  public int getProcessDate() {
    return StaticTool.bcToRoc(this.processDate);
  }

/**
	* 處理日期<br>
	* 
  *
  * @param processDate 處理日期
  * @throws LogicException when Date Is Warn	*/
  public void setProcessDate(int processDate) throws LogicException {
    this.processDate = StaticTool.rocToBc(processDate);
  }

/**
	* 催收處理情形<br>
	* 
	* @return String
	*/
  public String getOvduSituaction() {
    return this.ovduSituaction == null ? "" : this.ovduSituaction;
  }

/**
	* 催收處理情形<br>
	* 
  *
  * @param ovduSituaction 催收處理情形
	*/
  public void setOvduSituaction(String ovduSituaction) {
    this.ovduSituaction = ovduSituaction;
  }

/**
	* 附言<br>
	* 
	* @return String
	*/
  public String getRemark() {
    return this.remark == null ? "" : this.remark;
  }

/**
	* 附言<br>
	* 
  *
  * @param remark 附言
	*/
  public void setRemark(String remark) {
    this.remark = remark;
  }

/**
	* 會計日期<br>
	* 
	* @return Integer
	*/
  public int getAcDate() {
    return StaticTool.bcToRoc(this.acDate);
  }

/**
	* 會計日期<br>
	* 
  *
  * @param acDate 會計日期
  * @throws LogicException when Date Is Warn	*/
  public void setAcDate(int acDate) throws LogicException {
    this.acDate = StaticTool.rocToBc(acDate);
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
    return "LoanOverdue [loanOverdueId=" + loanOverdueId + ", status=" + status + ", acctCode=" + acctCode
           + ", ovduDate=" + ovduDate + ", badDebtDate=" + badDebtDate + ", replyDate=" + replyDate + ", ovduPrinAmt=" + ovduPrinAmt + ", ovduIntAmt=" + ovduIntAmt + ", ovduBreachAmt=" + ovduBreachAmt
           + ", ovduAmt=" + ovduAmt + ", ovduPrinBal=" + ovduPrinBal + ", ovduIntBal=" + ovduIntBal + ", ovduBreachBal=" + ovduBreachBal + ", ovduBal=" + ovduBal + ", reduceInt=" + reduceInt
           + ", reduceBreach=" + reduceBreach + ", badDebtAmt=" + badDebtAmt + ", badDebtBal=" + badDebtBal + ", replyReduceAmt=" + replyReduceAmt + ", processDate=" + processDate + ", ovduSituaction=" + ovduSituaction
           + ", remark=" + remark + ", acDate=" + acDate + ", createDate=" + createDate + ", createEmpNo=" + createEmpNo + ", lastUpdate=" + lastUpdate + ", lastUpdateEmpNo=" + lastUpdateEmpNo
           + "]";
  }
}
