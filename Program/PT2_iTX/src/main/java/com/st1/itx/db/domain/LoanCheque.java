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
 * LoanCheque 支票檔<br>
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "`LoanCheque`")
public class LoanCheque implements Serializable {


  @EmbeddedId
  private LoanChequeId loanChequeId;

  // 借款人戶號
  @Column(name = "`CustNo`")
  private int custNo = 0;

  // 支票帳號
  @Column(name = "`ChequeAcct`", insertable = false, updatable = false)
  private int chequeAcct = 0;

  // 支票號碼
  @Column(name = "`ChequeNo`", insertable = false, updatable = false)
  private int chequeNo = 0;

  // 票據狀況碼
  /* 共用代碼檔0:未處理1:兌現入帳2:退票3:抽票4:兌現未入帳5:即期票 */
  @Column(name = "`StatusCode`", length = 1)
  private String statusCode;

  // 處理代碼
  /* 共用代碼檔1:不處理    2:已處理 */
  @Column(name = "`ProcessCode`", length = 1)
  private String processCode;

  // 交易序號-會計日期
  @Column(name = "`AcDate`")
  private int acDate = 0;

  // 交易單位
  @Column(name = "`Kinbr`", length = 4)
  private String kinbr;

  // 交易序號-櫃員
  @Column(name = "`TellerNo`", length = 6)
  private String tellerNo;

  // 交易序號-流水號
  @Column(name = "`TxtNo`", length = 8)
  private String txtNo;

  // 收票日
  /* 原as400CHKRDT收票日 */
  @Column(name = "`ReceiveDate`")
  private int receiveDate = 0;

  // 入帳日
  /* 原as400CHKLTD異動日 */
  @Column(name = "`EntryDate`")
  private int entryDate = 0;

  // 幣別
  @Column(name = "`CurrencyCode`", length = 3)
  private String currencyCode;

  // 支票金額
  @Column(name = "`ChequeAmt`")
  private BigDecimal chequeAmt = new BigDecimal("0");

  // 發票人姓名
  @Column(name = "`ChequeName`", length = 60)
  private String chequeName;

  // 支票到期日
  @Column(name = "`ChequeDate`")
  private int chequeDate = 0;

  // 交換區號
  /* 共用代碼檔01:總所03:台中市分所04:台南市分所05:高雄市分所07:桃園縣分所08:新竹市分所09:苗栗縣分所11:南投縣分所13:雲林縣分所14:嘉義市分所15:台南縣分所17:屏東縣分所18:宜蘭縣分所19:花蓮縣分所20:台東縣分所21:澎湖縣分所 */
  @Column(name = "`AreaCode`", length = 2)
  private String areaCode;

  // 行庫代號
  @Column(name = "`BankCode`", length = 7)
  private String bankCode;

  // 支票銀行
  @Column(name = "`BankItem`", length = 50)
  private String bankItem;

  // 支票分行
  @Column(name = "`BranchItem`", length = 50)
  private String branchItem;

  // 本埠外埠
  /* 共用代碼檔1:本埠   2:外埠 */
  @Column(name = "`OutsideCode`", length = 1)
  private String outsideCode;

  // 是否為台支
  /* Y:是N:否 */
  @Column(name = "`BktwFlag`", length = 1)
  private String bktwFlag;

  // 是否為台新
  /* Y:是N:否 */
  @Column(name = "`TsibFlag`", length = 1)
  private String tsibFlag;

  // 入媒體檔
  /* Y:是N:否 */
  @Column(name = "`MediaFlag`", length = 1)
  private String mediaFlag;

  // 支票用途
  /* 共用代碼檔01:期款02:部分償還03:結案04:帳管費05:火險費06:契變手續費07:法務費09:其他 */
  @Column(name = "`UsageCode`", length = 2)
  private String usageCode;

  // 服務中心別
  /* 共用代碼檔A:建北B:台中D:台南E:高雄F:板橋H:桃園N:彰化Y:站前 */
  @Column(name = "`ServiceCenter`", length = 1)
  private String serviceCenter;

  // 債權統一編號
  @Column(name = "`CreditorId`", length = 10)
  private String creditorId;

  // 債權機構
  @Column(name = "`CreditorBankCode`", length = 7)
  private String creditorBankCode;

  // 對方業務科目
  /* 共用代碼檔310:短期擔保放款 320:中期擔保放款330:長期擔保放款340:三十年房貸 */
  @Column(name = "`OtherAcctCode`", length = 3)
  private String otherAcctCode;

  // 收據號碼
  @Column(name = "`ReceiptNo`", length = 5)
  private String receiptNo;

  // 已入帳金額
  @Column(name = "`RepaidAmt`")
  private BigDecimal repaidAmt = new BigDecimal("0");

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


  public LoanChequeId getLoanChequeId() {
    return this.loanChequeId;
  }

  public void setLoanChequeId(LoanChequeId loanChequeId) {
    this.loanChequeId = loanChequeId;
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
	* 支票帳號<br>
	* 
	* @return Integer
	*/
  public int getChequeAcct() {
    return this.chequeAcct;
  }

/**
	* 支票帳號<br>
	* 
  *
  * @param chequeAcct 支票帳號
	*/
  public void setChequeAcct(int chequeAcct) {
    this.chequeAcct = chequeAcct;
  }

/**
	* 支票號碼<br>
	* 
	* @return Integer
	*/
  public int getChequeNo() {
    return this.chequeNo;
  }

/**
	* 支票號碼<br>
	* 
  *
  * @param chequeNo 支票號碼
	*/
  public void setChequeNo(int chequeNo) {
    this.chequeNo = chequeNo;
  }

/**
	* 票據狀況碼<br>
	* 共用代碼檔
0:未處理
1:兌現入帳
2:退票
3:抽票
4:兌現未入帳
5:即期票
	* @return String
	*/
  public String getStatusCode() {
    return this.statusCode == null ? "" : this.statusCode;
  }

/**
	* 票據狀況碼<br>
	* 共用代碼檔
0:未處理
1:兌現入帳
2:退票
3:抽票
4:兌現未入帳
5:即期票
  *
  * @param statusCode 票據狀況碼
	*/
  public void setStatusCode(String statusCode) {
    this.statusCode = statusCode;
  }

/**
	* 處理代碼<br>
	* 共用代碼檔
1:不處理    
2:已處理
	* @return String
	*/
  public String getProcessCode() {
    return this.processCode == null ? "" : this.processCode;
  }

/**
	* 處理代碼<br>
	* 共用代碼檔
1:不處理    
2:已處理
  *
  * @param processCode 處理代碼
	*/
  public void setProcessCode(String processCode) {
    this.processCode = processCode;
  }

/**
	* 交易序號-會計日期<br>
	* 
	* @return Integer
	*/
  public int getAcDate() {
    return StaticTool.bcToRoc(this.acDate);
  }

/**
	* 交易序號-會計日期<br>
	* 
  *
  * @param acDate 交易序號-會計日期
  * @throws LogicException when Date Is Warn	*/
  public void setAcDate(int acDate) throws LogicException {
    this.acDate = StaticTool.rocToBc(acDate);
  }

/**
	* 交易單位<br>
	* 
	* @return String
	*/
  public String getKinbr() {
    return this.kinbr == null ? "" : this.kinbr;
  }

/**
	* 交易單位<br>
	* 
  *
  * @param kinbr 交易單位
	*/
  public void setKinbr(String kinbr) {
    this.kinbr = kinbr;
  }

/**
	* 交易序號-櫃員<br>
	* 
	* @return String
	*/
  public String getTellerNo() {
    return this.tellerNo == null ? "" : this.tellerNo;
  }

/**
	* 交易序號-櫃員<br>
	* 
  *
  * @param tellerNo 交易序號-櫃員
	*/
  public void setTellerNo(String tellerNo) {
    this.tellerNo = tellerNo;
  }

/**
	* 交易序號-流水號<br>
	* 
	* @return String
	*/
  public String getTxtNo() {
    return this.txtNo == null ? "" : this.txtNo;
  }

/**
	* 交易序號-流水號<br>
	* 
  *
  * @param txtNo 交易序號-流水號
	*/
  public void setTxtNo(String txtNo) {
    this.txtNo = txtNo;
  }

/**
	* 收票日<br>
	* 原as400CHKRDT收票日
	* @return Integer
	*/
  public int getReceiveDate() {
    return StaticTool.bcToRoc(this.receiveDate);
  }

/**
	* 收票日<br>
	* 原as400CHKRDT收票日
  *
  * @param receiveDate 收票日
  * @throws LogicException when Date Is Warn	*/
  public void setReceiveDate(int receiveDate) throws LogicException {
    this.receiveDate = StaticTool.rocToBc(receiveDate);
  }

/**
	* 入帳日<br>
	* 原as400CHKLTD異動日
	* @return Integer
	*/
  public int getEntryDate() {
    return StaticTool.bcToRoc(this.entryDate);
  }

/**
	* 入帳日<br>
	* 原as400CHKLTD異動日
  *
  * @param entryDate 入帳日
  * @throws LogicException when Date Is Warn	*/
  public void setEntryDate(int entryDate) throws LogicException {
    this.entryDate = StaticTool.rocToBc(entryDate);
  }

/**
	* 幣別<br>
	* 
	* @return String
	*/
  public String getCurrencyCode() {
    return this.currencyCode == null ? "" : this.currencyCode;
  }

/**
	* 幣別<br>
	* 
  *
  * @param currencyCode 幣別
	*/
  public void setCurrencyCode(String currencyCode) {
    this.currencyCode = currencyCode;
  }

/**
	* 支票金額<br>
	* 
	* @return BigDecimal
	*/
  public BigDecimal getChequeAmt() {
    return this.chequeAmt;
  }

/**
	* 支票金額<br>
	* 
  *
  * @param chequeAmt 支票金額
	*/
  public void setChequeAmt(BigDecimal chequeAmt) {
    this.chequeAmt = chequeAmt;
  }

/**
	* 發票人姓名<br>
	* 
	* @return String
	*/
  public String getChequeName() {
    return this.chequeName == null ? "" : this.chequeName;
  }

/**
	* 發票人姓名<br>
	* 
  *
  * @param chequeName 發票人姓名
	*/
  public void setChequeName(String chequeName) {
    this.chequeName = chequeName;
  }

/**
	* 支票到期日<br>
	* 
	* @return Integer
	*/
  public int getChequeDate() {
    return StaticTool.bcToRoc(this.chequeDate);
  }

/**
	* 支票到期日<br>
	* 
  *
  * @param chequeDate 支票到期日
  * @throws LogicException when Date Is Warn	*/
  public void setChequeDate(int chequeDate) throws LogicException {
    this.chequeDate = StaticTool.rocToBc(chequeDate);
  }

/**
	* 交換區號<br>
	* 共用代碼檔
01:總所
03:台中市分所
04:台南市分所
05:高雄市分所
07:桃園縣分所
08:新竹市分所
09:苗栗縣分所
11:南投縣分所
13:雲林縣分所
14:嘉義市分所
15:台南縣分所
17:屏東縣分所
18:宜蘭縣分所
19:花蓮縣分所
20:台東縣分所
21:澎湖縣分所
	* @return String
	*/
  public String getAreaCode() {
    return this.areaCode == null ? "" : this.areaCode;
  }

/**
	* 交換區號<br>
	* 共用代碼檔
01:總所
03:台中市分所
04:台南市分所
05:高雄市分所
07:桃園縣分所
08:新竹市分所
09:苗栗縣分所
11:南投縣分所
13:雲林縣分所
14:嘉義市分所
15:台南縣分所
17:屏東縣分所
18:宜蘭縣分所
19:花蓮縣分所
20:台東縣分所
21:澎湖縣分所
  *
  * @param areaCode 交換區號
	*/
  public void setAreaCode(String areaCode) {
    this.areaCode = areaCode;
  }

/**
	* 行庫代號<br>
	* 
	* @return String
	*/
  public String getBankCode() {
    return this.bankCode == null ? "" : this.bankCode;
  }

/**
	* 行庫代號<br>
	* 
  *
  * @param bankCode 行庫代號
	*/
  public void setBankCode(String bankCode) {
    this.bankCode = bankCode;
  }

/**
	* 支票銀行<br>
	* 
	* @return String
	*/
  public String getBankItem() {
    return this.bankItem == null ? "" : this.bankItem;
  }

/**
	* 支票銀行<br>
	* 
  *
  * @param bankItem 支票銀行
	*/
  public void setBankItem(String bankItem) {
    this.bankItem = bankItem;
  }

/**
	* 支票分行<br>
	* 
	* @return String
	*/
  public String getBranchItem() {
    return this.branchItem == null ? "" : this.branchItem;
  }

/**
	* 支票分行<br>
	* 
  *
  * @param branchItem 支票分行
	*/
  public void setBranchItem(String branchItem) {
    this.branchItem = branchItem;
  }

/**
	* 本埠外埠<br>
	* 共用代碼檔
1:本埠   
2:外埠
	* @return String
	*/
  public String getOutsideCode() {
    return this.outsideCode == null ? "" : this.outsideCode;
  }

/**
	* 本埠外埠<br>
	* 共用代碼檔
1:本埠   
2:外埠
  *
  * @param outsideCode 本埠外埠
	*/
  public void setOutsideCode(String outsideCode) {
    this.outsideCode = outsideCode;
  }

/**
	* 是否為台支<br>
	* Y:是
N:否
	* @return String
	*/
  public String getBktwFlag() {
    return this.bktwFlag == null ? "" : this.bktwFlag;
  }

/**
	* 是否為台支<br>
	* Y:是
N:否
  *
  * @param bktwFlag 是否為台支
	*/
  public void setBktwFlag(String bktwFlag) {
    this.bktwFlag = bktwFlag;
  }

/**
	* 是否為台新<br>
	* Y:是
N:否
	* @return String
	*/
  public String getTsibFlag() {
    return this.tsibFlag == null ? "" : this.tsibFlag;
  }

/**
	* 是否為台新<br>
	* Y:是
N:否
  *
  * @param tsibFlag 是否為台新
	*/
  public void setTsibFlag(String tsibFlag) {
    this.tsibFlag = tsibFlag;
  }

/**
	* 入媒體檔<br>
	* Y:是
N:否
	* @return String
	*/
  public String getMediaFlag() {
    return this.mediaFlag == null ? "" : this.mediaFlag;
  }

/**
	* 入媒體檔<br>
	* Y:是
N:否
  *
  * @param mediaFlag 入媒體檔
	*/
  public void setMediaFlag(String mediaFlag) {
    this.mediaFlag = mediaFlag;
  }

/**
	* 支票用途<br>
	* 共用代碼檔
01:期款
02:部分償還
03:結案
04:帳管費
05:火險費
06:契變手續費
07:法務費
09:其他
	* @return String
	*/
  public String getUsageCode() {
    return this.usageCode == null ? "" : this.usageCode;
  }

/**
	* 支票用途<br>
	* 共用代碼檔
01:期款
02:部分償還
03:結案
04:帳管費
05:火險費
06:契變手續費
07:法務費
09:其他
  *
  * @param usageCode 支票用途
	*/
  public void setUsageCode(String usageCode) {
    this.usageCode = usageCode;
  }

/**
	* 服務中心別<br>
	* 共用代碼檔
A:建北
B:台中
D:台南
E:高雄
F:板橋
H:桃園
N:彰化
Y:站前
	* @return String
	*/
  public String getServiceCenter() {
    return this.serviceCenter == null ? "" : this.serviceCenter;
  }

/**
	* 服務中心別<br>
	* 共用代碼檔
A:建北
B:台中
D:台南
E:高雄
F:板橋
H:桃園
N:彰化
Y:站前
  *
  * @param serviceCenter 服務中心別
	*/
  public void setServiceCenter(String serviceCenter) {
    this.serviceCenter = serviceCenter;
  }

/**
	* 債權統一編號<br>
	* 
	* @return String
	*/
  public String getCreditorId() {
    return this.creditorId == null ? "" : this.creditorId;
  }

/**
	* 債權統一編號<br>
	* 
  *
  * @param creditorId 債權統一編號
	*/
  public void setCreditorId(String creditorId) {
    this.creditorId = creditorId;
  }

/**
	* 債權機構<br>
	* 
	* @return String
	*/
  public String getCreditorBankCode() {
    return this.creditorBankCode == null ? "" : this.creditorBankCode;
  }

/**
	* 債權機構<br>
	* 
  *
  * @param creditorBankCode 債權機構
	*/
  public void setCreditorBankCode(String creditorBankCode) {
    this.creditorBankCode = creditorBankCode;
  }

/**
	* 對方業務科目<br>
	* 共用代碼檔
310:短期擔保放款 
320:中期擔保放款
330:長期擔保放款
340:三十年房貸
	* @return String
	*/
  public String getOtherAcctCode() {
    return this.otherAcctCode == null ? "" : this.otherAcctCode;
  }

/**
	* 對方業務科目<br>
	* 共用代碼檔
310:短期擔保放款 
320:中期擔保放款
330:長期擔保放款
340:三十年房貸
  *
  * @param otherAcctCode 對方業務科目
	*/
  public void setOtherAcctCode(String otherAcctCode) {
    this.otherAcctCode = otherAcctCode;
  }

/**
	* 收據號碼<br>
	* 
	* @return String
	*/
  public String getReceiptNo() {
    return this.receiptNo == null ? "" : this.receiptNo;
  }

/**
	* 收據號碼<br>
	* 
  *
  * @param receiptNo 收據號碼
	*/
  public void setReceiptNo(String receiptNo) {
    this.receiptNo = receiptNo;
  }

/**
	* 已入帳金額<br>
	* 
	* @return BigDecimal
	*/
  public BigDecimal getRepaidAmt() {
    return this.repaidAmt;
  }

/**
	* 已入帳金額<br>
	* 
  *
  * @param repaidAmt 已入帳金額
	*/
  public void setRepaidAmt(BigDecimal repaidAmt) {
    this.repaidAmt = repaidAmt;
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
    return "LoanCheque [loanChequeId=" + loanChequeId + ", custNo=" + custNo + ", statusCode=" + statusCode + ", processCode=" + processCode + ", acDate=" + acDate
           + ", kinbr=" + kinbr + ", tellerNo=" + tellerNo + ", txtNo=" + txtNo + ", receiveDate=" + receiveDate + ", entryDate=" + entryDate + ", currencyCode=" + currencyCode
           + ", chequeAmt=" + chequeAmt + ", chequeName=" + chequeName + ", chequeDate=" + chequeDate + ", areaCode=" + areaCode + ", bankCode=" + bankCode + ", bankItem=" + bankItem
           + ", branchItem=" + branchItem + ", outsideCode=" + outsideCode + ", bktwFlag=" + bktwFlag + ", tsibFlag=" + tsibFlag + ", mediaFlag=" + mediaFlag + ", usageCode=" + usageCode
           + ", serviceCenter=" + serviceCenter + ", creditorId=" + creditorId + ", creditorBankCode=" + creditorBankCode + ", otherAcctCode=" + otherAcctCode + ", receiptNo=" + receiptNo + ", repaidAmt=" + repaidAmt
           + ", createDate=" + createDate + ", createEmpNo=" + createEmpNo + ", lastUpdate=" + lastUpdate + ", lastUpdateEmpNo=" + lastUpdateEmpNo + "]";
  }
}
