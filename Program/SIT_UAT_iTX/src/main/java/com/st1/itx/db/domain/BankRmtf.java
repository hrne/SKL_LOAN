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
 * BankRmtf 匯款轉帳檔<br>
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "`BankRmtf`")
public class BankRmtf implements Serializable {


  /**
	 * 
	 */
	private static final long serialVersionUID = 7612366996203587195L;

@EmbeddedId
  private BankRmtfId bankRmtfId;

  // 會計日
  @Column(name = "`AcDate`", insertable = false, updatable = false)
  private int acDate = 0;

  // 批號
  @Column(name = "`BatchNo`", length = 6, insertable = false, updatable = false)
  private String batchNo;

  // 明細序號
  @Column(name = "`DetailSeq`", insertable = false, updatable = false)
  private int detailSeq = 0;

  // 戶號
  @Column(name = "`CustNo`")
  private int custNo = 0;

  // 還款類別
  /* CdCode:RepayType1.期款2.部分償還3.結案4.帳管費5.火險費6.契變手續費7.法務費9.其他 */
  @Column(name = "`RepayType`", length = 2)
  private String repayType;

  // 還款金額
  @Column(name = "`RepayAmt`")
  private BigDecimal repayAmt = new BigDecimal("0");

  // 存摺帳號
  @Column(name = "`DepAcctNo`", length = 14)
  private String depAcctNo;

  // 入帳日期
  @Column(name = "`EntryDate`")
  private int entryDate = 0;

  // 摘要代碼
  @Column(name = "`DscptCode`", length = 4)
  private String dscptCode;

  // 虛擬帳號
  @Column(name = "`VirtualAcctNo`", length = 14)
  private String virtualAcctNo;

  // 提款
  @Column(name = "`WithdrawAmt`")
  private BigDecimal withdrawAmt = new BigDecimal("0");

  // 存款
  @Column(name = "`DepositAmt`")
  private BigDecimal depositAmt = new BigDecimal("0");

  // 結餘
  @Column(name = "`Balance`")
  private BigDecimal balance = new BigDecimal("0");

  // 匯款銀行代碼
  @Column(name = "`RemintBank`", length = 7)
  private String remintBank;

  // 交易人資料
  @Column(name = "`TraderInfo`", length = 20)
  private String traderInfo;

  // AML回應碼
  /* CdCode:AmlCheckItem0.非可疑名單/已完成名單確認1.需審查/確認2.為凍結名單/未確定名單 */
  @Column(name = "`AmlRsp`", length = 1)
  private String amlRsp;

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


  public BankRmtfId getBankRmtfId() {
    return this.bankRmtfId;
  }

  public void setBankRmtfId(BankRmtfId bankRmtfId) {
    this.bankRmtfId = bankRmtfId;
  }

/**
	* 會計日<br>
	* 
	* @return Integer
	*/
  public int getAcDate() {
    return StaticTool.bcToRoc(this.acDate);
  }

/**
	* 會計日<br>
	* 
  *
  * @param acDate 會計日
  * @throws LogicException when Date Is Warn	*/
  public void setAcDate(int acDate) throws LogicException {
    this.acDate = StaticTool.rocToBc(acDate);
  }

/**
	* 批號<br>
	* 
	* @return String
	*/
  public String getBatchNo() {
    return this.batchNo == null ? "" : this.batchNo;
  }

/**
	* 批號<br>
	* 
  *
  * @param batchNo 批號
	*/
  public void setBatchNo(String batchNo) {
    this.batchNo = batchNo;
  }

/**
	* 明細序號<br>
	* 
	* @return Integer
	*/
  public int getDetailSeq() {
    return this.detailSeq;
  }

/**
	* 明細序號<br>
	* 
  *
  * @param detailSeq 明細序號
	*/
  public void setDetailSeq(int detailSeq) {
    this.detailSeq = detailSeq;
  }

/**
	* 戶號<br>
	* 
	* @return Integer
	*/
  public int getCustNo() {
    return this.custNo;
  }

/**
	* 戶號<br>
	* 
  *
  * @param custNo 戶號
	*/
  public void setCustNo(int custNo) {
    this.custNo = custNo;
  }

/**
	* 還款類別<br>
	* CdCode:RepayType
1.期款
2.部分償還
3.結案
4.帳管費
5.火險費
6.契變手續費
7.法務費
9.其他
	* @return String
	*/
  public String getRepayType() {
    return this.repayType == null ? "" : this.repayType;
  }

/**
	* 還款類別<br>
	* CdCode:RepayType
1.期款
2.部分償還
3.結案
4.帳管費
5.火險費
6.契變手續費
7.法務費
9.其他
  *
  * @param repayType 還款類別
	*/
  public void setRepayType(String repayType) {
    this.repayType = repayType;
  }

/**
	* 還款金額<br>
	* 
	* @return BigDecimal
	*/
  public BigDecimal getRepayAmt() {
    return this.repayAmt;
  }

/**
	* 還款金額<br>
	* 
  *
  * @param repayAmt 還款金額
	*/
  public void setRepayAmt(BigDecimal repayAmt) {
    this.repayAmt = repayAmt;
  }

/**
	* 存摺帳號<br>
	* 
	* @return String
	*/
  public String getDepAcctNo() {
    return this.depAcctNo == null ? "" : this.depAcctNo;
  }

/**
	* 存摺帳號<br>
	* 
  *
  * @param depAcctNo 存摺帳號
	*/
  public void setDepAcctNo(String depAcctNo) {
    this.depAcctNo = depAcctNo;
  }

/**
	* 入帳日期<br>
	* 
	* @return Integer
	*/
  public int getEntryDate() {
    return StaticTool.bcToRoc(this.entryDate);
  }

/**
	* 入帳日期<br>
	* 
  *
  * @param entryDate 入帳日期
  * @throws LogicException when Date Is Warn	*/
  public void setEntryDate(int entryDate) throws LogicException {
    this.entryDate = StaticTool.rocToBc(entryDate);
  }

/**
	* 摘要代碼<br>
	* 
	* @return String
	*/
  public String getDscptCode() {
    return this.dscptCode == null ? "" : this.dscptCode;
  }

/**
	* 摘要代碼<br>
	* 
  *
  * @param dscptCode 摘要代碼
	*/
  public void setDscptCode(String dscptCode) {
    this.dscptCode = dscptCode;
  }

/**
	* 虛擬帳號<br>
	* 
	* @return String
	*/
  public String getVirtualAcctNo() {
    return this.virtualAcctNo == null ? "" : this.virtualAcctNo;
  }

/**
	* 虛擬帳號<br>
	* 
  *
  * @param virtualAcctNo 虛擬帳號
	*/
  public void setVirtualAcctNo(String virtualAcctNo) {
    this.virtualAcctNo = virtualAcctNo;
  }

/**
	* 提款<br>
	* 
	* @return BigDecimal
	*/
  public BigDecimal getWithdrawAmt() {
    return this.withdrawAmt;
  }

/**
	* 提款<br>
	* 
  *
  * @param withdrawAmt 提款
	*/
  public void setWithdrawAmt(BigDecimal withdrawAmt) {
    this.withdrawAmt = withdrawAmt;
  }

/**
	* 存款<br>
	* 
	* @return BigDecimal
	*/
  public BigDecimal getDepositAmt() {
    return this.depositAmt;
  }

/**
	* 存款<br>
	* 
  *
  * @param depositAmt 存款
	*/
  public void setDepositAmt(BigDecimal depositAmt) {
    this.depositAmt = depositAmt;
  }

/**
	* 結餘<br>
	* 
	* @return BigDecimal
	*/
  public BigDecimal getBalance() {
    return this.balance;
  }

/**
	* 結餘<br>
	* 
  *
  * @param balance 結餘
	*/
  public void setBalance(BigDecimal balance) {
    this.balance = balance;
  }

/**
	* 匯款銀行代碼<br>
	* 
	* @return String
	*/
  public String getRemintBank() {
    return this.remintBank == null ? "" : this.remintBank;
  }

/**
	* 匯款銀行代碼<br>
	* 
  *
  * @param remintBank 匯款銀行代碼
	*/
  public void setRemintBank(String remintBank) {
    this.remintBank = remintBank;
  }

/**
	* 交易人資料<br>
	* 
	* @return String
	*/
  public String getTraderInfo() {
    return this.traderInfo == null ? "" : this.traderInfo;
  }

/**
	* 交易人資料<br>
	* 
  *
  * @param traderInfo 交易人資料
	*/
  public void setTraderInfo(String traderInfo) {
    this.traderInfo = traderInfo;
  }

/**
	* AML回應碼<br>
	* CdCode:AmlCheckItem
0.非可疑名單/已完成名單確認
1.需審查/確認
2.為凍結名單/未確定名單
	* @return String
	*/
  public String getAmlRsp() {
    return this.amlRsp == null ? "" : this.amlRsp;
  }

/**
	* AML回應碼<br>
	* CdCode:AmlCheckItem
0.非可疑名單/已完成名單確認
1.需審查/確認
2.為凍結名單/未確定名單
  *
  * @param amlRsp AML回應碼
	*/
  public void setAmlRsp(String amlRsp) {
    this.amlRsp = amlRsp;
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
    return "BankRmtf [bankRmtfId=" + bankRmtfId + ", custNo=" + custNo + ", repayType=" + repayType + ", repayAmt=" + repayAmt
           + ", depAcctNo=" + depAcctNo + ", entryDate=" + entryDate + ", dscptCode=" + dscptCode + ", virtualAcctNo=" + virtualAcctNo + ", withdrawAmt=" + withdrawAmt + ", depositAmt=" + depositAmt
           + ", balance=" + balance + ", remintBank=" + remintBank + ", traderInfo=" + traderInfo + ", amlRsp=" + amlRsp + ", createDate=" + createDate + ", createEmpNo=" + createEmpNo
           + ", lastUpdate=" + lastUpdate + ", lastUpdateEmpNo=" + lastUpdateEmpNo + "]";
  }
}
