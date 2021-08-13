package com.st1.itx.db.domain;

import java.io.Serializable;
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
 * JcicZ061 回報協商剩餘債權金額資料<br>
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "`JcicZ061`")
public class JcicZ061 implements Serializable {


  /**
	 * 
	 */
	private static final long serialVersionUID = 2090719638929249155L;

@EmbeddedId
  private JcicZ061Id jcicZ061Id;

  // 交易代碼
  /* A:新增;C:異動 */
  @Column(name = "`TranKey`", length = 1)
  private String tranKey;

  // 債權金融機構代號
  /* 三位文數字 */
  @Column(name = "`SubmitKey`", length = 3, insertable = false, updatable = false)
  private String submitKey;

  // 債務人IDN
  @Column(name = "`CustId`", length = 10, insertable = false, updatable = false)
  private String custId;

  // 原前置協商申請日
  @Column(name = "`RcDate`", insertable = false, updatable = false)
  private int rcDate = 0;

  // 申請變更還款條件日
  @Column(name = "`ChangePayDate`", insertable = false, updatable = false)
  private int changePayDate = 0;

  // 最大債權金融機構代號
  /* 三位文數字 */
  @Column(name = "`MaxMainCode`", length = 3)
  private String maxMainCode;

  // 信用貸款協商剩餘債權餘額
  @Column(name = "`ExpBalanceAmt`")
  private int expBalanceAmt = 0;

  // 現金卡協商剩餘債權餘額
  @Column(name = "`CashBalanceAmt`")
  private int cashBalanceAmt = 0;

  // 信用卡協商剩餘債權餘額
  @Column(name = "`CreditBalanceAmt`")
  private int creditBalanceAmt = 0;

  // 最大債權金融機構報送註記
  /* Y;N */
  @Column(name = "`MaxMainNote`", length = 1)
  private String maxMainNote;

  // 是否有保證人
  /* Y;N */
  @Column(name = "`IsGuarantor`", length = 1)
  private String isGuarantor;

  // 是否同意債務人申請變更還款條件方案
  /* Y;N */
  @Column(name = "`IsChangePayment`", length = 1)
  private String isChangePayment;

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


  public JcicZ061Id getJcicZ061Id() {
    return this.jcicZ061Id;
  }

  public void setJcicZ061Id(JcicZ061Id jcicZ061Id) {
    this.jcicZ061Id = jcicZ061Id;
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
	* 債權金融機構代號<br>
	* 三位文數字
	* @return String
	*/
  public String getSubmitKey() {
    return this.submitKey == null ? "" : this.submitKey;
  }

/**
	* 債權金融機構代號<br>
	* 三位文數字
  *
  * @param submitKey 債權金融機構代號
	*/
  public void setSubmitKey(String submitKey) {
    this.submitKey = submitKey;
  }

/**
	* 債務人IDN<br>
	* 
	* @return String
	*/
  public String getCustId() {
    return this.custId == null ? "" : this.custId;
  }

/**
	* 債務人IDN<br>
	* 
  *
  * @param custId 債務人IDN
	*/
  public void setCustId(String custId) {
    this.custId = custId;
  }

/**
	* 原前置協商申請日<br>
	* 
	* @return Integer
	*/
  public int getRcDate() {
    return StaticTool.bcToRoc(this.rcDate);
  }

/**
	* 原前置協商申請日<br>
	* 
  *
  * @param rcDate 原前置協商申請日
  * @throws LogicException when Date Is Warn	*/
  public void setRcDate(int rcDate) throws LogicException {
    this.rcDate = StaticTool.rocToBc(rcDate);
  }

/**
	* 申請變更還款條件日<br>
	* 
	* @return Integer
	*/
  public int getChangePayDate() {
    return StaticTool.bcToRoc(this.changePayDate);
  }

/**
	* 申請變更還款條件日<br>
	* 
  *
  * @param changePayDate 申請變更還款條件日
  * @throws LogicException when Date Is Warn	*/
  public void setChangePayDate(int changePayDate) throws LogicException {
    this.changePayDate = StaticTool.rocToBc(changePayDate);
  }

/**
	* 最大債權金融機構代號<br>
	* 三位文數字
	* @return String
	*/
  public String getMaxMainCode() {
    return this.maxMainCode == null ? "" : this.maxMainCode;
  }

/**
	* 最大債權金融機構代號<br>
	* 三位文數字
  *
  * @param maxMainCode 最大債權金融機構代號
	*/
  public void setMaxMainCode(String maxMainCode) {
    this.maxMainCode = maxMainCode;
  }

/**
	* 信用貸款協商剩餘債權餘額<br>
	* 
	* @return Integer
	*/
  public int getExpBalanceAmt() {
    return this.expBalanceAmt;
  }

/**
	* 信用貸款協商剩餘債權餘額<br>
	* 
  *
  * @param expBalanceAmt 信用貸款協商剩餘債權餘額
	*/
  public void setExpBalanceAmt(int expBalanceAmt) {
    this.expBalanceAmt = expBalanceAmt;
  }

/**
	* 現金卡協商剩餘債權餘額<br>
	* 
	* @return Integer
	*/
  public int getCashBalanceAmt() {
    return this.cashBalanceAmt;
  }

/**
	* 現金卡協商剩餘債權餘額<br>
	* 
  *
  * @param cashBalanceAmt 現金卡協商剩餘債權餘額
	*/
  public void setCashBalanceAmt(int cashBalanceAmt) {
    this.cashBalanceAmt = cashBalanceAmt;
  }

/**
	* 信用卡協商剩餘債權餘額<br>
	* 
	* @return Integer
	*/
  public int getCreditBalanceAmt() {
    return this.creditBalanceAmt;
  }

/**
	* 信用卡協商剩餘債權餘額<br>
	* 
  *
  * @param creditBalanceAmt 信用卡協商剩餘債權餘額
	*/
  public void setCreditBalanceAmt(int creditBalanceAmt) {
    this.creditBalanceAmt = creditBalanceAmt;
  }

/**
	* 最大債權金融機構報送註記<br>
	* Y;N
	* @return String
	*/
  public String getMaxMainNote() {
    return this.maxMainNote == null ? "" : this.maxMainNote;
  }

/**
	* 最大債權金融機構報送註記<br>
	* Y;N
  *
  * @param maxMainNote 最大債權金融機構報送註記
	*/
  public void setMaxMainNote(String maxMainNote) {
    this.maxMainNote = maxMainNote;
  }

/**
	* 是否有保證人<br>
	* Y;N
	* @return String
	*/
  public String getIsGuarantor() {
    return this.isGuarantor == null ? "" : this.isGuarantor;
  }

/**
	* 是否有保證人<br>
	* Y;N
  *
  * @param isGuarantor 是否有保證人
	*/
  public void setIsGuarantor(String isGuarantor) {
    this.isGuarantor = isGuarantor;
  }

/**
	* 是否同意債務人申請變更還款條件方案<br>
	* Y;N
	* @return String
	*/
  public String getIsChangePayment() {
    return this.isChangePayment == null ? "" : this.isChangePayment;
  }

/**
	* 是否同意債務人申請變更還款條件方案<br>
	* Y;N
  *
  * @param isChangePayment 是否同意債務人申請變更還款條件方案
	*/
  public void setIsChangePayment(String isChangePayment) {
    this.isChangePayment = isChangePayment;
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
    return "JcicZ061 [jcicZ061Id=" + jcicZ061Id + ", tranKey=" + tranKey + ", maxMainCode=" + maxMainCode
           + ", expBalanceAmt=" + expBalanceAmt + ", cashBalanceAmt=" + cashBalanceAmt + ", creditBalanceAmt=" + creditBalanceAmt + ", maxMainNote=" + maxMainNote + ", isGuarantor=" + isGuarantor + ", isChangePayment=" + isChangePayment
           + ", outJcicTxtDate=" + outJcicTxtDate + ", createDate=" + createDate + ", createEmpNo=" + createEmpNo + ", lastUpdate=" + lastUpdate + ", lastUpdateEmpNo=" + lastUpdateEmpNo + "]";
  }
}
