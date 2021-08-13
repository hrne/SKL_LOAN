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
 * JcicZ043Log 回報有擔保債權金額資料<br>
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "`JcicZ043Log`")
public class JcicZ043Log implements Serializable {


  /**
	 * 
	 */
	private static final long serialVersionUID = -4729823820654188222L;

@EmbeddedId
  private JcicZ043LogId jcicZ043LogId;

  // 流水號
  @Column(name = "`Ukey`", length = 32, insertable = false, updatable = false)
  private String ukey;

  // 交易序號
  @Column(name = "`TxSeq`", length = 18, insertable = false, updatable = false)
  private String txSeq;

  // 交易代碼
  /* A:新增,C:異動,D:刪除 */
  @Column(name = "`TranKey`", length = 1)
  private String tranKey;

  // 擔保品類別
  @Column(name = "`CollateralType`", length = 2)
  private String collateralType;

  // 原借款金額
  @Column(name = "`OriginLoanAmt`")
  private BigDecimal originLoanAmt = new BigDecimal("0");

  // 授信餘額
  @Column(name = "`CreditBalance`")
  private BigDecimal creditBalance = new BigDecimal("0");

  // 每期應付金額
  @Column(name = "`PerPeriordAmt`")
  private BigDecimal perPeriordAmt = new BigDecimal("0");

  // 最近一期繳款金額
  @Column(name = "`LastPayAmt`")
  private BigDecimal lastPayAmt = new BigDecimal("0");

  // 最後繳息日
  @Column(name = "`LastPayDate`")
  private int lastPayDate = 0;

  // 已到期尚未償還金額
  @Column(name = "`OutstandAmt`")
  private BigDecimal outstandAmt = new BigDecimal("0");

  // 每月應還款日
  @Column(name = "`RepayPerMonDay`")
  private int repayPerMonDay = 0;

  // 契約起始年月
  @Column(name = "`ContractStartYM`")
  private int contractStartYM = 0;

  // 契約截止年月
  @Column(name = "`ContractEndYM`")
  private int contractEndYM = 0;

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


  public JcicZ043LogId getJcicZ043LogId() {
    return this.jcicZ043LogId;
  }

  public void setJcicZ043LogId(JcicZ043LogId jcicZ043LogId) {
    this.jcicZ043LogId = jcicZ043LogId;
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
	* A:新增,C:異動,D:刪除
	* @return String
	*/
  public String getTranKey() {
    return this.tranKey == null ? "" : this.tranKey;
  }

/**
	* 交易代碼<br>
	* A:新增,C:異動,D:刪除
  *
  * @param tranKey 交易代碼
	*/
  public void setTranKey(String tranKey) {
    this.tranKey = tranKey;
  }

/**
	* 擔保品類別<br>
	* 
	* @return String
	*/
  public String getCollateralType() {
    return this.collateralType == null ? "" : this.collateralType;
  }

/**
	* 擔保品類別<br>
	* 
  *
  * @param collateralType 擔保品類別
	*/
  public void setCollateralType(String collateralType) {
    this.collateralType = collateralType;
  }

/**
	* 原借款金額<br>
	* 
	* @return BigDecimal
	*/
  public BigDecimal getOriginLoanAmt() {
    return this.originLoanAmt;
  }

/**
	* 原借款金額<br>
	* 
  *
  * @param originLoanAmt 原借款金額
	*/
  public void setOriginLoanAmt(BigDecimal originLoanAmt) {
    this.originLoanAmt = originLoanAmt;
  }

/**
	* 授信餘額<br>
	* 
	* @return BigDecimal
	*/
  public BigDecimal getCreditBalance() {
    return this.creditBalance;
  }

/**
	* 授信餘額<br>
	* 
  *
  * @param creditBalance 授信餘額
	*/
  public void setCreditBalance(BigDecimal creditBalance) {
    this.creditBalance = creditBalance;
  }

/**
	* 每期應付金額<br>
	* 
	* @return BigDecimal
	*/
  public BigDecimal getPerPeriordAmt() {
    return this.perPeriordAmt;
  }

/**
	* 每期應付金額<br>
	* 
  *
  * @param perPeriordAmt 每期應付金額
	*/
  public void setPerPeriordAmt(BigDecimal perPeriordAmt) {
    this.perPeriordAmt = perPeriordAmt;
  }

/**
	* 最近一期繳款金額<br>
	* 
	* @return BigDecimal
	*/
  public BigDecimal getLastPayAmt() {
    return this.lastPayAmt;
  }

/**
	* 最近一期繳款金額<br>
	* 
  *
  * @param lastPayAmt 最近一期繳款金額
	*/
  public void setLastPayAmt(BigDecimal lastPayAmt) {
    this.lastPayAmt = lastPayAmt;
  }

/**
	* 最後繳息日<br>
	* 
	* @return Integer
	*/
  public int getLastPayDate() {
    return StaticTool.bcToRoc(this.lastPayDate);
  }

/**
	* 最後繳息日<br>
	* 
  *
  * @param lastPayDate 最後繳息日
  * @throws LogicException when Date Is Warn	*/
  public void setLastPayDate(int lastPayDate) throws LogicException {
    this.lastPayDate = StaticTool.rocToBc(lastPayDate);
  }

/**
	* 已到期尚未償還金額<br>
	* 
	* @return BigDecimal
	*/
  public BigDecimal getOutstandAmt() {
    return this.outstandAmt;
  }

/**
	* 已到期尚未償還金額<br>
	* 
  *
  * @param outstandAmt 已到期尚未償還金額
	*/
  public void setOutstandAmt(BigDecimal outstandAmt) {
    this.outstandAmt = outstandAmt;
  }

/**
	* 每月應還款日<br>
	* 
	* @return Integer
	*/
  public int getRepayPerMonDay() {
    return this.repayPerMonDay;
  }

/**
	* 每月應還款日<br>
	* 
  *
  * @param repayPerMonDay 每月應還款日
	*/
  public void setRepayPerMonDay(int repayPerMonDay) {
    this.repayPerMonDay = repayPerMonDay;
  }

/**
	* 契約起始年月<br>
	* 
	* @return Integer
	*/
  public int getContractStartYM() {
    return this.contractStartYM;
  }

/**
	* 契約起始年月<br>
	* 
  *
  * @param contractStartYM 契約起始年月
	*/
  public void setContractStartYM(int contractStartYM) {
    this.contractStartYM = contractStartYM;
  }

/**
	* 契約截止年月<br>
	* 
	* @return Integer
	*/
  public int getContractEndYM() {
    return this.contractEndYM;
  }

/**
	* 契約截止年月<br>
	* 
  *
  * @param contractEndYM 契約截止年月
	*/
  public void setContractEndYM(int contractEndYM) {
    this.contractEndYM = contractEndYM;
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
    return "JcicZ043Log [jcicZ043LogId=" + jcicZ043LogId + ", tranKey=" + tranKey + ", collateralType=" + collateralType + ", originLoanAmt=" + originLoanAmt + ", creditBalance=" + creditBalance
           + ", perPeriordAmt=" + perPeriordAmt + ", lastPayAmt=" + lastPayAmt + ", lastPayDate=" + lastPayDate + ", outstandAmt=" + outstandAmt + ", repayPerMonDay=" + repayPerMonDay + ", contractStartYM=" + contractStartYM
           + ", contractEndYM=" + contractEndYM + ", outJcicTxtDate=" + outJcicTxtDate + ", createDate=" + createDate + ", createEmpNo=" + createEmpNo + ", lastUpdate=" + lastUpdate + ", lastUpdateEmpNo=" + lastUpdateEmpNo
           + "]";
  }
}
