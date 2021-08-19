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
 * JcicZ447Log 前置調解金融機構無擔保債務協議資料<br>
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "`JcicZ447Log`")
public class JcicZ447Log implements Serializable {


  /**
	 * 
	 */
	private static final long serialVersionUID = -2746764935608033518L;

@EmbeddedId
  private JcicZ447LogId jcicZ447LogId;

  // 流水號
  @Column(name = "`Ukey`", length = 32, insertable = false, updatable = false)
  private String ukey;

  // 交易序號
  @Column(name = "`TxSeq`", length = 18, insertable = false, updatable = false)
  private String txSeq;

  // 交易代碼
  /* A:新增;C:異動;D:刪除;X:補件 */
  @Column(name = "`TranKey`", length = 1)
  private String tranKey;

  // 依民法第323條計算之債務總金額
  @Column(name = "`Civil323Amt`")
  private BigDecimal civil323Amt = new BigDecimal("0");

  // 簽約總債務金額
  @Column(name = "`TotalAmt`")
  private BigDecimal totalAmt = new BigDecimal("0");

  // 簽約完成日期
  @Column(name = "`SignDate`")
  private int signDate = 0;

  // 首期應繳款日
  @Column(name = "`FirstPayDate`")
  private int firstPayDate = 0;

  // 期數
  @Column(name = "`Period`")
  private int period = 0;

  // 利率
  /* XX.XX */
  @Column(name = "`Rate`")
  private BigDecimal rate = new BigDecimal("0");

  // 月付金
  @Column(name = "`MonthPayAmt`")
  private int monthPayAmt = 0;

  // 繳款帳號
  /* 長度20 */
  @Column(name = "`PayAccount`", length = 20)
  private String payAccount;

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


  public JcicZ447LogId getJcicZ447LogId() {
    return this.jcicZ447LogId;
  }

  public void setJcicZ447LogId(JcicZ447LogId jcicZ447LogId) {
    this.jcicZ447LogId = jcicZ447LogId;
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
	* A:新增;C:異動;D:刪除;X:補件
	* @return String
	*/
  public String getTranKey() {
    return this.tranKey == null ? "" : this.tranKey;
  }

/**
	* 交易代碼<br>
	* A:新增;C:異動;D:刪除;X:補件
  *
  * @param tranKey 交易代碼
	*/
  public void setTranKey(String tranKey) {
    this.tranKey = tranKey;
  }

/**
	* 依民法第323條計算之債務總金額<br>
	* 
	* @return BigDecimal
	*/
  public BigDecimal getCivil323Amt() {
    return this.civil323Amt;
  }

/**
	* 依民法第323條計算之債務總金額<br>
	* 
  *
  * @param civil323Amt 依民法第323條計算之債務總金額
	*/
  public void setCivil323Amt(BigDecimal civil323Amt) {
    this.civil323Amt = civil323Amt;
  }

/**
	* 簽約總債務金額<br>
	* 
	* @return BigDecimal
	*/
  public BigDecimal getTotalAmt() {
    return this.totalAmt;
  }

/**
	* 簽約總債務金額<br>
	* 
  *
  * @param totalAmt 簽約總債務金額
	*/
  public void setTotalAmt(BigDecimal totalAmt) {
    this.totalAmt = totalAmt;
  }

/**
	* 簽約完成日期<br>
	* 
	* @return Integer
	*/
  public int getSignDate() {
    return StaticTool.bcToRoc(this.signDate);
  }

/**
	* 簽約完成日期<br>
	* 
  *
  * @param signDate 簽約完成日期
  * @throws LogicException when Date Is Warn	*/
  public void setSignDate(int signDate) throws LogicException {
    this.signDate = StaticTool.rocToBc(signDate);
  }

/**
	* 首期應繳款日<br>
	* 
	* @return Integer
	*/
  public int getFirstPayDate() {
    return StaticTool.bcToRoc(this.firstPayDate);
  }

/**
	* 首期應繳款日<br>
	* 
  *
  * @param firstPayDate 首期應繳款日
  * @throws LogicException when Date Is Warn	*/
  public void setFirstPayDate(int firstPayDate) throws LogicException {
    this.firstPayDate = StaticTool.rocToBc(firstPayDate);
  }

/**
	* 期數<br>
	* 
	* @return Integer
	*/
  public int getPeriod() {
    return this.period;
  }

/**
	* 期數<br>
	* 
  *
  * @param period 期數
	*/
  public void setPeriod(int period) {
    this.period = period;
  }

/**
	* 利率<br>
	* XX.XX
	* @return BigDecimal
	*/
  public BigDecimal getRate() {
    return this.rate;
  }

/**
	* 利率<br>
	* XX.XX
  *
  * @param rate 利率
	*/
  public void setRate(BigDecimal rate) {
    this.rate = rate;
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
	* 繳款帳號<br>
	* 長度20
	* @return String
	*/
  public String getPayAccount() {
    return this.payAccount == null ? "" : this.payAccount;
  }

/**
	* 繳款帳號<br>
	* 長度20
  *
  * @param payAccount 繳款帳號
	*/
  public void setPayAccount(String payAccount) {
    this.payAccount = payAccount;
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
    return "JcicZ447Log [jcicZ447LogId=" + jcicZ447LogId + ", tranKey=" + tranKey + ", civil323Amt=" + civil323Amt + ", totalAmt=" + totalAmt + ", signDate=" + signDate
           + ", firstPayDate=" + firstPayDate + ", period=" + period + ", rate=" + rate + ", monthPayAmt=" + monthPayAmt + ", payAccount=" + payAccount + ", outJcicTxtDate=" + outJcicTxtDate
           + ", createDate=" + createDate + ", createEmpNo=" + createEmpNo + ", lastUpdate=" + lastUpdate + ", lastUpdateEmpNo=" + lastUpdateEmpNo + "]";
  }
}
