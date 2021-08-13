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
 * JcicZ062Log 金融機構無擔保債務變更還款條件協議資料<br>
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "`JcicZ062Log`")
public class JcicZ062Log implements Serializable {


  /**
	 * 
	 */
	private static final long serialVersionUID = 4389092628030187957L;

@EmbeddedId
  private JcicZ062LogId jcicZ062LogId;

  // 流水號
  @Column(name = "`Ukey`", length = 32, insertable = false, updatable = false)
  private String ukey;

  // 交易序號
  @Column(name = "`TxSeq`", length = 18, insertable = false, updatable = false)
  private String txSeq;

  // 交易代碼
  /* A:新增;C:異動 */
  @Column(name = "`TranKey`", length = 1)
  private String tranKey;

  // 變更還款條件已履約期數
  @Column(name = "`CompletePeriod`")
  private int completePeriod = 0;

  // (第一階梯)期數
  @Column(name = "`Period`")
  private int period = 0;

  // (第一階梯)利率
  /* XX.XX */
  @Column(name = "`Rate`")
  private BigDecimal rate = new BigDecimal("0");

  // 信用貸款協商剩餘債務簽約餘額
  @Column(name = "`ExpBalanceAmt`")
  private int expBalanceAmt = 0;

  // 現金卡協商剩餘債務簽約餘額
  @Column(name = "`CashBalanceAmt`")
  private int cashBalanceAmt = 0;

  // 信用卡協商剩餘債務簽約餘額
  @Column(name = "`CreditBalanceAmt`")
  private int creditBalanceAmt = 0;

  // 變更還款條件簽約總債務金額
  @Column(name = "`ChaRepayAmt`")
  private BigDecimal chaRepayAmt = new BigDecimal("0");

  // 變更還款條件協議完成日
  @Column(name = "`ChaRepayAgreeDate`")
  private int chaRepayAgreeDate = 0;

  // 變更還款條件面談日期
  @Column(name = "`ChaRepayViewDate`")
  private int chaRepayViewDate = 0;

  // 變更還款條件簽約完成日期
  @Column(name = "`ChaRepayEndDate`")
  private int chaRepayEndDate = 0;

  // 變更還款條件首期應繳款日
  @Column(name = "`ChaRepayFirstDate`")
  private int chaRepayFirstDate = 0;

  // 繳款帳號
  /* 20字元 */
  @Column(name = "`PayAccount`", length = 20)
  private String payAccount;

  // 最大債權金融機構聲請狀送達地址
  /* 38個中文字 */
  @Column(name = "`PostAddr`", length = 76)
  private String postAddr;

  // 月付金
  @Column(name = "`MonthPayAmt`")
  private int monthPayAmt = 0;

  // 屬階梯式還款註記
  /* Y;N */
  @Column(name = "`GradeType`", length = 1)
  private String gradeType;

  // 第二階梯期數
  @Column(name = "`Period2`")
  private int period2 = 0;

  // 第二階梯利率
  /* XX.XX */
  @Column(name = "`Rate2`")
  private BigDecimal rate2 = new BigDecimal("0");

  // 第二階段月付金
  @Column(name = "`MonthPayAmt2`")
  private int monthPayAmt2 = 0;

  // 轉出JCIC文字檔日期
  @Column(name = "`OutJcicTxtDate`")
  private int outJcicTxtDate = 0;

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


  public JcicZ062LogId getJcicZ062LogId() {
    return this.jcicZ062LogId;
  }

  public void setJcicZ062LogId(JcicZ062LogId jcicZ062LogId) {
    this.jcicZ062LogId = jcicZ062LogId;
  }

/**
	* 流水號<br>
	* 
	* @return String
	*/
  public String getUkey() {
    return this.ukey == null ? "" : this.ukey;
  }

/**
	* 流水號<br>
	* 
  *
  * @param ukey 流水號
	*/
  public void setUkey(String ukey) {
    this.ukey = ukey;
  }

/**
	* 交易序號<br>
	* 
	* @return String
	*/
  public String getTxSeq() {
    return this.txSeq == null ? "" : this.txSeq;
  }

/**
	* 交易序號<br>
	* 
  *
  * @param txSeq 交易序號
	*/
  public void setTxSeq(String txSeq) {
    this.txSeq = txSeq;
  }

/**
	* 交易代碼<br>
	* A:新增;C:異動
	* @return String
	*/
  public String getTranKey() {
    return this.tranKey == null ? "" : this.tranKey;
  }

/**
	* 交易代碼<br>
	* A:新增;C:異動
  *
  * @param tranKey 交易代碼
	*/
  public void setTranKey(String tranKey) {
    this.tranKey = tranKey;
  }

/**
	* 變更還款條件已履約期數<br>
	* 
	* @return Integer
	*/
  public int getCompletePeriod() {
    return this.completePeriod;
  }

/**
	* 變更還款條件已履約期數<br>
	* 
  *
  * @param completePeriod 變更還款條件已履約期數
	*/
  public void setCompletePeriod(int completePeriod) {
    this.completePeriod = completePeriod;
  }

/**
	* (第一階梯)期數<br>
	* 
	* @return Integer
	*/
  public int getPeriod() {
    return this.period;
  }

/**
	* (第一階梯)期數<br>
	* 
  *
  * @param period (第一階梯)期數
	*/
  public void setPeriod(int period) {
    this.period = period;
  }

/**
	* (第一階梯)利率<br>
	* XX.XX
	* @return BigDecimal
	*/
  public BigDecimal getRate() {
    return this.rate;
  }

/**
	* (第一階梯)利率<br>
	* XX.XX
  *
  * @param rate (第一階梯)利率
	*/
  public void setRate(BigDecimal rate) {
    this.rate = rate;
  }

/**
	* 信用貸款協商剩餘債務簽約餘額<br>
	* 
	* @return Integer
	*/
  public int getExpBalanceAmt() {
    return this.expBalanceAmt;
  }

/**
	* 信用貸款協商剩餘債務簽約餘額<br>
	* 
  *
  * @param expBalanceAmt 信用貸款協商剩餘債務簽約餘額
	*/
  public void setExpBalanceAmt(int expBalanceAmt) {
    this.expBalanceAmt = expBalanceAmt;
  }

/**
	* 現金卡協商剩餘債務簽約餘額<br>
	* 
	* @return Integer
	*/
  public int getCashBalanceAmt() {
    return this.cashBalanceAmt;
  }

/**
	* 現金卡協商剩餘債務簽約餘額<br>
	* 
  *
  * @param cashBalanceAmt 現金卡協商剩餘債務簽約餘額
	*/
  public void setCashBalanceAmt(int cashBalanceAmt) {
    this.cashBalanceAmt = cashBalanceAmt;
  }

/**
	* 信用卡協商剩餘債務簽約餘額<br>
	* 
	* @return Integer
	*/
  public int getCreditBalanceAmt() {
    return this.creditBalanceAmt;
  }

/**
	* 信用卡協商剩餘債務簽約餘額<br>
	* 
  *
  * @param creditBalanceAmt 信用卡協商剩餘債務簽約餘額
	*/
  public void setCreditBalanceAmt(int creditBalanceAmt) {
    this.creditBalanceAmt = creditBalanceAmt;
  }

/**
	* 變更還款條件簽約總債務金額<br>
	* 
	* @return BigDecimal
	*/
  public BigDecimal getChaRepayAmt() {
    return this.chaRepayAmt;
  }

/**
	* 變更還款條件簽約總債務金額<br>
	* 
  *
  * @param chaRepayAmt 變更還款條件簽約總債務金額
	*/
  public void setChaRepayAmt(BigDecimal chaRepayAmt) {
    this.chaRepayAmt = chaRepayAmt;
  }

/**
	* 變更還款條件協議完成日<br>
	* 
	* @return Integer
	*/
  public int getChaRepayAgreeDate() {
    return StaticTool.bcToRoc(this.chaRepayAgreeDate);
  }

/**
	* 變更還款條件協議完成日<br>
	* 
  *
  * @param chaRepayAgreeDate 變更還款條件協議完成日
  * @throws LogicException when Date Is Warn	*/
  public void setChaRepayAgreeDate(int chaRepayAgreeDate) throws LogicException {
    this.chaRepayAgreeDate = StaticTool.rocToBc(chaRepayAgreeDate);
  }

/**
	* 變更還款條件面談日期<br>
	* 
	* @return Integer
	*/
  public int getChaRepayViewDate() {
    return StaticTool.bcToRoc(this.chaRepayViewDate);
  }

/**
	* 變更還款條件面談日期<br>
	* 
  *
  * @param chaRepayViewDate 變更還款條件面談日期
  * @throws LogicException when Date Is Warn	*/
  public void setChaRepayViewDate(int chaRepayViewDate) throws LogicException {
    this.chaRepayViewDate = StaticTool.rocToBc(chaRepayViewDate);
  }

/**
	* 變更還款條件簽約完成日期<br>
	* 
	* @return Integer
	*/
  public int getChaRepayEndDate() {
    return StaticTool.bcToRoc(this.chaRepayEndDate);
  }

/**
	* 變更還款條件簽約完成日期<br>
	* 
  *
  * @param chaRepayEndDate 變更還款條件簽約完成日期
  * @throws LogicException when Date Is Warn	*/
  public void setChaRepayEndDate(int chaRepayEndDate) throws LogicException {
    this.chaRepayEndDate = StaticTool.rocToBc(chaRepayEndDate);
  }

/**
	* 變更還款條件首期應繳款日<br>
	* 
	* @return Integer
	*/
  public int getChaRepayFirstDate() {
    return StaticTool.bcToRoc(this.chaRepayFirstDate);
  }

/**
	* 變更還款條件首期應繳款日<br>
	* 
  *
  * @param chaRepayFirstDate 變更還款條件首期應繳款日
  * @throws LogicException when Date Is Warn	*/
  public void setChaRepayFirstDate(int chaRepayFirstDate) throws LogicException {
    this.chaRepayFirstDate = StaticTool.rocToBc(chaRepayFirstDate);
  }

/**
	* 繳款帳號<br>
	* 20字元
	* @return String
	*/
  public String getPayAccount() {
    return this.payAccount == null ? "" : this.payAccount;
  }

/**
	* 繳款帳號<br>
	* 20字元
  *
  * @param payAccount 繳款帳號
	*/
  public void setPayAccount(String payAccount) {
    this.payAccount = payAccount;
  }

/**
	* 最大債權金融機構聲請狀送達地址<br>
	* 38個中文字
	* @return String
	*/
  public String getPostAddr() {
    return this.postAddr == null ? "" : this.postAddr;
  }

/**
	* 最大債權金融機構聲請狀送達地址<br>
	* 38個中文字
  *
  * @param postAddr 最大債權金融機構聲請狀送達地址
	*/
  public void setPostAddr(String postAddr) {
    this.postAddr = postAddr;
  }

/**
	* 月付金<br>
	* 
	* @return Integer
	*/
  public int getMonthPayAmt() {
    return this.monthPayAmt;
  }

/**
	* 月付金<br>
	* 
  *
  * @param monthPayAmt 月付金
	*/
  public void setMonthPayAmt(int monthPayAmt) {
    this.monthPayAmt = monthPayAmt;
  }

/**
	* 屬階梯式還款註記<br>
	* Y;N
	* @return String
	*/
  public String getGradeType() {
    return this.gradeType == null ? "" : this.gradeType;
  }

/**
	* 屬階梯式還款註記<br>
	* Y;N
  *
  * @param gradeType 屬階梯式還款註記
	*/
  public void setGradeType(String gradeType) {
    this.gradeType = gradeType;
  }

/**
	* 第二階梯期數<br>
	* 
	* @return Integer
	*/
  public int getPeriod2() {
    return this.period2;
  }

/**
	* 第二階梯期數<br>
	* 
  *
  * @param period2 第二階梯期數
	*/
  public void setPeriod2(int period2) {
    this.period2 = period2;
  }

/**
	* 第二階梯利率<br>
	* XX.XX
	* @return BigDecimal
	*/
  public BigDecimal getRate2() {
    return this.rate2;
  }

/**
	* 第二階梯利率<br>
	* XX.XX
  *
  * @param rate2 第二階梯利率
	*/
  public void setRate2(BigDecimal rate2) {
    this.rate2 = rate2;
  }

/**
	* 第二階段月付金<br>
	* 
	* @return Integer
	*/
  public int getMonthPayAmt2() {
    return this.monthPayAmt2;
  }

/**
	* 第二階段月付金<br>
	* 
  *
  * @param monthPayAmt2 第二階段月付金
	*/
  public void setMonthPayAmt2(int monthPayAmt2) {
    this.monthPayAmt2 = monthPayAmt2;
  }

/**
	* 轉出JCIC文字檔日期<br>
	* 
	* @return Integer
	*/
  public int getOutJcicTxtDate() {
    return StaticTool.bcToRoc(this.outJcicTxtDate);
  }

/**
	* 轉出JCIC文字檔日期<br>
	* 
  *
  * @param outJcicTxtDate 轉出JCIC文字檔日期
  * @throws LogicException when Date Is Warn	*/
  public void setOutJcicTxtDate(int outJcicTxtDate) throws LogicException {
    this.outJcicTxtDate = StaticTool.rocToBc(outJcicTxtDate);
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
    return "JcicZ062Log [jcicZ062LogId=" + jcicZ062LogId + ", tranKey=" + tranKey + ", completePeriod=" + completePeriod + ", period=" + period + ", rate=" + rate
           + ", expBalanceAmt=" + expBalanceAmt + ", cashBalanceAmt=" + cashBalanceAmt + ", creditBalanceAmt=" + creditBalanceAmt + ", chaRepayAmt=" + chaRepayAmt + ", chaRepayAgreeDate=" + chaRepayAgreeDate + ", chaRepayViewDate=" + chaRepayViewDate
           + ", chaRepayEndDate=" + chaRepayEndDate + ", chaRepayFirstDate=" + chaRepayFirstDate + ", payAccount=" + payAccount + ", postAddr=" + postAddr + ", monthPayAmt=" + monthPayAmt + ", gradeType=" + gradeType
           + ", period2=" + period2 + ", rate2=" + rate2 + ", monthPayAmt2=" + monthPayAmt2 + ", outJcicTxtDate=" + outJcicTxtDate + ", createDate=" + createDate + ", createEmpNo=" + createEmpNo
           + ", lastUpdate=" + lastUpdate + ", lastUpdateEmpNo=" + lastUpdateEmpNo + "]";
  }
}
