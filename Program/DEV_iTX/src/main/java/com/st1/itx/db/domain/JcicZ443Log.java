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
 * JcicZ443Log 前置調解回報有擔保債權金額資料<br>
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "`JcicZ443Log`")
public class JcicZ443Log implements Serializable {


  /**
	 * 
	 */
	private static final long serialVersionUID = 3264272151774828272L;

@EmbeddedId
  private JcicZ443LogId jcicZ443LogId;

  // 流水號
  @Column(name = "`Ukey`", length = 32, insertable = false, updatable = false)
  private String ukey;

  // 交易序號
  @Column(name = "`TxSeq`", length = 18, insertable = false, updatable = false)
  private String txSeq;

  // 交易代碼
  /* A:新增;C:異動;D:刪除 */
  @Column(name = "`TranKey`", length = 1)
  private String tranKey;

  // 是否為最大債權金融機構報送
  /* Y;N */
  @Column(name = "`IsMaxMain`", length = 1)
  private String isMaxMain;

  // 帳號
  /* 最多50字 */
  @Column(name = "`Account`", length = 50)
  private String account;

  // 擔保品類別
  /* CdCode.CollateralType */
  @Column(name = "`GuarantyType`", length = 2)
  private String guarantyType;

  // 原借款金額
  @Column(name = "`LoanAmt`")
  private BigDecimal loanAmt = new BigDecimal("0");

  // 授信餘額
  @Column(name = "`CreditAmt`")
  private BigDecimal creditAmt = new BigDecimal("0");

  // 本金
  @Column(name = "`Principal`")
  private BigDecimal principal = new BigDecimal("0");

  // 利息
  @Column(name = "`Interest`")
  private BigDecimal interest = new BigDecimal("0");

  // 違約金
  @Column(name = "`Penalty`")
  private BigDecimal penalty = new BigDecimal("0");

  // 其他費用
  @Column(name = "`Other`")
  private BigDecimal other = new BigDecimal("0");

  // 每期應付金額
  @Column(name = "`TerminalPayAmt`")
  private BigDecimal terminalPayAmt = new BigDecimal("0");

  // 最近一期繳款金額
  @Column(name = "`LatestPayAmt`")
  private BigDecimal latestPayAmt = new BigDecimal("0");

  // 最後繳息日
  @Column(name = "`FinalPayDay`")
  private int finalPayDay = 0;

  // 已到期尚未清償金額
  @Column(name = "`NotyetacQuit`")
  private BigDecimal notyetacQuit = new BigDecimal("0");

  // 每月應還款日
  /* 01~31 */
  @Column(name = "`MothPayDay`")
  private int mothPayDay = 0;

  // 契約起始年月
  @Column(name = "`BeginDate`")
  private int beginDate = 0;

  // 契約截止年月
  @Column(name = "`EndDate`")
  private int endDate = 0;

  // 轉JCIC文字檔日期
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


  public JcicZ443LogId getJcicZ443LogId() {
    return this.jcicZ443LogId;
  }

  public void setJcicZ443LogId(JcicZ443LogId jcicZ443LogId) {
    this.jcicZ443LogId = jcicZ443LogId;
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
	* A:新增;C:異動;D:刪除
	* @return String
	*/
  public String getTranKey() {
    return this.tranKey == null ? "" : this.tranKey;
  }

/**
	* 交易代碼<br>
	* A:新增;C:異動;D:刪除
  *
  * @param tranKey 交易代碼
	*/
  public void setTranKey(String tranKey) {
    this.tranKey = tranKey;
  }

/**
	* 是否為最大債權金融機構報送<br>
	* Y;N
	* @return String
	*/
  public String getIsMaxMain() {
    return this.isMaxMain == null ? "" : this.isMaxMain;
  }

/**
	* 是否為最大債權金融機構報送<br>
	* Y;N
  *
  * @param isMaxMain 是否為最大債權金融機構報送
	*/
  public void setIsMaxMain(String isMaxMain) {
    this.isMaxMain = isMaxMain;
  }

/**
	* 帳號<br>
	* 最多50字
	* @return String
	*/
  public String getAccount() {
    return this.account == null ? "" : this.account;
  }

/**
	* 帳號<br>
	* 最多50字
  *
  * @param account 帳號
	*/
  public void setAccount(String account) {
    this.account = account;
  }

/**
	* 擔保品類別<br>
	* CdCode.CollateralType
	* @return String
	*/
  public String getGuarantyType() {
    return this.guarantyType == null ? "" : this.guarantyType;
  }

/**
	* 擔保品類別<br>
	* CdCode.CollateralType
  *
  * @param guarantyType 擔保品類別
	*/
  public void setGuarantyType(String guarantyType) {
    this.guarantyType = guarantyType;
  }

/**
	* 原借款金額<br>
	* 
	* @return BigDecimal
	*/
  public BigDecimal getLoanAmt() {
    return this.loanAmt;
  }

/**
	* 原借款金額<br>
	* 
  *
  * @param loanAmt 原借款金額
	*/
  public void setLoanAmt(BigDecimal loanAmt) {
    this.loanAmt = loanAmt;
  }

/**
	* 授信餘額<br>
	* 
	* @return BigDecimal
	*/
  public BigDecimal getCreditAmt() {
    return this.creditAmt;
  }

/**
	* 授信餘額<br>
	* 
  *
  * @param creditAmt 授信餘額
	*/
  public void setCreditAmt(BigDecimal creditAmt) {
    this.creditAmt = creditAmt;
  }

/**
	* 本金<br>
	* 
	* @return BigDecimal
	*/
  public BigDecimal getPrincipal() {
    return this.principal;
  }

/**
	* 本金<br>
	* 
  *
  * @param principal 本金
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
	* 違約金<br>
	* 
	* @return BigDecimal
	*/
  public BigDecimal getPenalty() {
    return this.penalty;
  }

/**
	* 違約金<br>
	* 
  *
  * @param penalty 違約金
	*/
  public void setPenalty(BigDecimal penalty) {
    this.penalty = penalty;
  }

/**
	* 其他費用<br>
	* 
	* @return BigDecimal
	*/
  public BigDecimal getOther() {
    return this.other;
  }

/**
	* 其他費用<br>
	* 
  *
  * @param other 其他費用
	*/
  public void setOther(BigDecimal other) {
    this.other = other;
  }

/**
	* 每期應付金額<br>
	* 
	* @return BigDecimal
	*/
  public BigDecimal getTerminalPayAmt() {
    return this.terminalPayAmt;
  }

/**
	* 每期應付金額<br>
	* 
  *
  * @param terminalPayAmt 每期應付金額
	*/
  public void setTerminalPayAmt(BigDecimal terminalPayAmt) {
    this.terminalPayAmt = terminalPayAmt;
  }

/**
	* 最近一期繳款金額<br>
	* 
	* @return BigDecimal
	*/
  public BigDecimal getLatestPayAmt() {
    return this.latestPayAmt;
  }

/**
	* 最近一期繳款金額<br>
	* 
  *
  * @param latestPayAmt 最近一期繳款金額
	*/
  public void setLatestPayAmt(BigDecimal latestPayAmt) {
    this.latestPayAmt = latestPayAmt;
  }

/**
	* 最後繳息日<br>
	* 
	* @return Integer
	*/
  public int getFinalPayDay() {
    return StaticTool.bcToRoc(this.finalPayDay);
  }

/**
	* 最後繳息日<br>
	* 
  *
  * @param finalPayDay 最後繳息日
  * @throws LogicException when Date Is Warn	*/
  public void setFinalPayDay(int finalPayDay) throws LogicException {
    this.finalPayDay = StaticTool.rocToBc(finalPayDay);
  }

/**
	* 已到期尚未清償金額<br>
	* 
	* @return BigDecimal
	*/
  public BigDecimal getNotyetacQuit() {
    return this.notyetacQuit;
  }

/**
	* 已到期尚未清償金額<br>
	* 
  *
  * @param notyetacQuit 已到期尚未清償金額
	*/
  public void setNotyetacQuit(BigDecimal notyetacQuit) {
    this.notyetacQuit = notyetacQuit;
  }

/**
	* 每月應還款日<br>
	* 01~31
	* @return Integer
	*/
  public int getMothPayDay() {
    return this.mothPayDay;
  }

/**
	* 每月應還款日<br>
	* 01~31
  *
  * @param mothPayDay 每月應還款日
	*/
  public void setMothPayDay(int mothPayDay) {
    this.mothPayDay = mothPayDay;
  }

/**
	* 契約起始年月<br>
	* 
	* @return Integer
	*/
  public int getBeginDate() {
    return this.beginDate;
  }

/**
	* 契約起始年月<br>
	* 
  *
  * @param beginDate 契約起始年月
	*/
  public void setBeginDate(int beginDate) {
    this.beginDate = beginDate;
  }

/**
	* 契約截止年月<br>
	* 
	* @return Integer
	*/
  public int getEndDate() {
    return this.endDate;
  }

/**
	* 契約截止年月<br>
	* 
  *
  * @param endDate 契約截止年月
	*/
  public void setEndDate(int endDate) {
    this.endDate = endDate;
  }

/**
	* 轉JCIC文字檔日期<br>
	* 
	* @return Integer
	*/
  public int getOutJcicTxtDate() {
    return StaticTool.bcToRoc(this.outJcicTxtDate);
  }

/**
	* 轉JCIC文字檔日期<br>
	* 
  *
  * @param outJcicTxtDate 轉JCIC文字檔日期
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
    return "JcicZ443Log [jcicZ443LogId=" + jcicZ443LogId + ", tranKey=" + tranKey + ", isMaxMain=" + isMaxMain + ", account=" + account + ", guarantyType=" + guarantyType
           + ", loanAmt=" + loanAmt + ", creditAmt=" + creditAmt + ", principal=" + principal + ", interest=" + interest + ", penalty=" + penalty + ", other=" + other
           + ", terminalPayAmt=" + terminalPayAmt + ", latestPayAmt=" + latestPayAmt + ", finalPayDay=" + finalPayDay + ", notyetacQuit=" + notyetacQuit + ", mothPayDay=" + mothPayDay + ", beginDate=" + beginDate
           + ", endDate=" + endDate + ", outJcicTxtDate=" + outJcicTxtDate + ", createDate=" + createDate + ", createEmpNo=" + createEmpNo + ", lastUpdate=" + lastUpdate + ", lastUpdateEmpNo=" + lastUpdateEmpNo
           + "]";
  }
}
