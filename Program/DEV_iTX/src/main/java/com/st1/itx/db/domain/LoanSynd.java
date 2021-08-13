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
 * LoanSynd 聯貸案訂約檔<br>
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "`LoanSynd`")
public class LoanSynd implements Serializable {


  /**
	 * 
	 */
	private static final long serialVersionUID = -2256634671715354710L;

@EmbeddedId
  private LoanSyndId loanSyndId;

  // 借款人戶號
  @Column(name = "`CustNo`", insertable = false, updatable = false)
  private int custNo = 0;

  // 聯貸案序號
  @Column(name = "`SyndNo`", insertable = false, updatable = false)
  private int syndNo = 0;

  // 已編BorTx流水號
  @Column(name = "`LastBorxNo`")
  private int lastBorxNo = 0;

  // 借款人識別碼
  @Column(name = "`CustUKey`", length = 32)
  private String custUKey;

  // 保證人識別碼
  @Column(name = "`GuaUKey`", length = 32)
  private String guaUKey;

  // 主辦行
  @Column(name = "`LeadingBank`", length = 7)
  private String leadingBank;

  // 簽約日
  @Column(name = "`SigningDate`")
  private int signingDate = 0;

  // 動撥起日
  @Column(name = "`DrawdownStartDate`")
  private int drawdownStartDate = 0;

  // 動撥迄日
  @Column(name = "`DrawdownEndDate`")
  private int drawdownEndDate = 0;

  // 是否有收承諾費
  /* Y:是 N:否刪除(L3600.L3010有使用一併修改) */
  @Column(name = "`CommitFeeFlag`", length = 1)
  private String commitFeeFlag;

  // 參貸費率
  @Column(name = "`PartRate`")
  private BigDecimal partRate = new BigDecimal("0");

  // 幣別
  @Column(name = "`CurrencyCode`", length = 3)
  private String currencyCode;

  // 聯貸總金額
  @Column(name = "`SyndAmt`")
  private BigDecimal syndAmt = new BigDecimal("0");

  // 參貸金額
  @Column(name = "`PartAmt`")
  private BigDecimal partAmt = new BigDecimal("0");

  // 代理行
  /* 擔保品管理行 */
  @Column(name = "`AgentBank`", length = 7)
  private String agentBank;

  // 授信期間
  @Column(name = "`CreditPeriod`")
  private int creditPeriod = 0;

  // 央行融資
  @Column(name = "`CentralBankPercent`")
  private int centralBankPercent = 0;

  // 母公司識別碼
  /* 刪除(L3600.L3010有使用一併修改) */
  @Column(name = "`MasterCustUkey`", length = 32)
  private String masterCustUkey;

  // 聯貸動撥之子公司一
  /* 刪除(L3600.L3010有使用一併修改) */
  @Column(name = "`SubCustUkey1`", length = 32)
  private String subCustUkey1;

  // 聯貸動撥之子公司二
  /* 刪除(L3600.L3010有使用一併修改) */
  @Column(name = "`SubCustUkey2`", length = 32)
  private String subCustUkey2;

  // 聯貸動撥之子公司三
  /* 刪除(L3600.L3010有使用一併修改) */
  @Column(name = "`SubCustUkey3`", length = 32)
  private String subCustUkey3;

  // 聯貸動撥之子公司四
  /* 刪除(L3600.L3010有使用一併修改) */
  @Column(name = "`SubCustUkey4`", length = 32)
  private String subCustUkey4;

  // 聯貸動撥之子公司五
  /* 刪除(L3600.L3010有使用一併修改) */
  @Column(name = "`SubCustUkey5`", length = 32)
  private String subCustUkey5;

  // 聯貸動撥之子公司六
  /* 刪除(L3600.L3010有使用一併修改) */
  @Column(name = "`SubCustUkey6`", length = 32)
  private String subCustUkey6;

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


  public LoanSyndId getLoanSyndId() {
    return this.loanSyndId;
  }

  public void setLoanSyndId(LoanSyndId loanSyndId) {
    this.loanSyndId = loanSyndId;
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
	* 聯貸案序號<br>
	* 
	* @return Integer
	*/
  public int getSyndNo() {
    return this.syndNo;
  }

/**
	* 聯貸案序號<br>
	* 
  *
  * @param syndNo 聯貸案序號
	*/
  public void setSyndNo(int syndNo) {
    this.syndNo = syndNo;
  }

/**
	* 已編BorTx流水號<br>
	* 
	* @return Integer
	*/
  public int getLastBorxNo() {
    return this.lastBorxNo;
  }

/**
	* 已編BorTx流水號<br>
	* 
  *
  * @param lastBorxNo 已編BorTx流水號
	*/
  public void setLastBorxNo(int lastBorxNo) {
    this.lastBorxNo = lastBorxNo;
  }

/**
	* 借款人識別碼<br>
	* 
	* @return String
	*/
  public String getCustUKey() {
    return this.custUKey == null ? "" : this.custUKey;
  }

/**
	* 借款人識別碼<br>
	* 
  *
  * @param custUKey 借款人識別碼
	*/
  public void setCustUKey(String custUKey) {
    this.custUKey = custUKey;
  }

/**
	* 保證人識別碼<br>
	* 
	* @return String
	*/
  public String getGuaUKey() {
    return this.guaUKey == null ? "" : this.guaUKey;
  }

/**
	* 保證人識別碼<br>
	* 
  *
  * @param guaUKey 保證人識別碼
	*/
  public void setGuaUKey(String guaUKey) {
    this.guaUKey = guaUKey;
  }

/**
	* 主辦行<br>
	* 
	* @return String
	*/
  public String getLeadingBank() {
    return this.leadingBank == null ? "" : this.leadingBank;
  }

/**
	* 主辦行<br>
	* 
  *
  * @param leadingBank 主辦行
	*/
  public void setLeadingBank(String leadingBank) {
    this.leadingBank = leadingBank;
  }

/**
	* 簽約日<br>
	* 
	* @return Integer
	*/
  public int getSigningDate() {
    return StaticTool.bcToRoc(this.signingDate);
  }

/**
	* 簽約日<br>
	* 
  *
  * @param signingDate 簽約日
  * @throws LogicException when Date Is Warn	*/
  public void setSigningDate(int signingDate) throws LogicException {
    this.signingDate = StaticTool.rocToBc(signingDate);
  }

/**
	* 動撥起日<br>
	* 
	* @return Integer
	*/
  public int getDrawdownStartDate() {
    return StaticTool.bcToRoc(this.drawdownStartDate);
  }

/**
	* 動撥起日<br>
	* 
  *
  * @param drawdownStartDate 動撥起日
  * @throws LogicException when Date Is Warn	*/
  public void setDrawdownStartDate(int drawdownStartDate) throws LogicException {
    this.drawdownStartDate = StaticTool.rocToBc(drawdownStartDate);
  }

/**
	* 動撥迄日<br>
	* 
	* @return Integer
	*/
  public int getDrawdownEndDate() {
    return StaticTool.bcToRoc(this.drawdownEndDate);
  }

/**
	* 動撥迄日<br>
	* 
  *
  * @param drawdownEndDate 動撥迄日
  * @throws LogicException when Date Is Warn	*/
  public void setDrawdownEndDate(int drawdownEndDate) throws LogicException {
    this.drawdownEndDate = StaticTool.rocToBc(drawdownEndDate);
  }

/**
	* 是否有收承諾費<br>
	* Y:是 N:否
刪除(L3600.L3010有使用一併修改)
	* @return String
	*/
  public String getCommitFeeFlag() {
    return this.commitFeeFlag == null ? "" : this.commitFeeFlag;
  }

/**
	* 是否有收承諾費<br>
	* Y:是 N:否
刪除(L3600.L3010有使用一併修改)
  *
  * @param commitFeeFlag 是否有收承諾費
	*/
  public void setCommitFeeFlag(String commitFeeFlag) {
    this.commitFeeFlag = commitFeeFlag;
  }

/**
	* 參貸費率<br>
	* 
	* @return BigDecimal
	*/
  public BigDecimal getPartRate() {
    return this.partRate;
  }

/**
	* 參貸費率<br>
	* 
  *
  * @param partRate 參貸費率
	*/
  public void setPartRate(BigDecimal partRate) {
    this.partRate = partRate;
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
	* 聯貸總金額<br>
	* 
	* @return BigDecimal
	*/
  public BigDecimal getSyndAmt() {
    return this.syndAmt;
  }

/**
	* 聯貸總金額<br>
	* 
  *
  * @param syndAmt 聯貸總金額
	*/
  public void setSyndAmt(BigDecimal syndAmt) {
    this.syndAmt = syndAmt;
  }

/**
	* 參貸金額<br>
	* 
	* @return BigDecimal
	*/
  public BigDecimal getPartAmt() {
    return this.partAmt;
  }

/**
	* 參貸金額<br>
	* 
  *
  * @param partAmt 參貸金額
	*/
  public void setPartAmt(BigDecimal partAmt) {
    this.partAmt = partAmt;
  }

/**
	* 代理行<br>
	* 擔保品管理行
	* @return String
	*/
  public String getAgentBank() {
    return this.agentBank == null ? "" : this.agentBank;
  }

/**
	* 代理行<br>
	* 擔保品管理行
  *
  * @param agentBank 代理行
	*/
  public void setAgentBank(String agentBank) {
    this.agentBank = agentBank;
  }

/**
	* 授信期間<br>
	* 
	* @return Integer
	*/
  public int getCreditPeriod() {
    return this.creditPeriod;
  }

/**
	* 授信期間<br>
	* 
  *
  * @param creditPeriod 授信期間
	*/
  public void setCreditPeriod(int creditPeriod) {
    this.creditPeriod = creditPeriod;
  }

/**
	* 央行融資<br>
	* 
	* @return Integer
	*/
  public int getCentralBankPercent() {
    return this.centralBankPercent;
  }

/**
	* 央行融資<br>
	* 
  *
  * @param centralBankPercent 央行融資
	*/
  public void setCentralBankPercent(int centralBankPercent) {
    this.centralBankPercent = centralBankPercent;
  }

/**
	* 母公司識別碼<br>
	* 刪除(L3600.L3010有使用一併修改)
	* @return String
	*/
  public String getMasterCustUkey() {
    return this.masterCustUkey == null ? "" : this.masterCustUkey;
  }

/**
	* 母公司識別碼<br>
	* 刪除(L3600.L3010有使用一併修改)
  *
  * @param masterCustUkey 母公司識別碼
	*/
  public void setMasterCustUkey(String masterCustUkey) {
    this.masterCustUkey = masterCustUkey;
  }

/**
	* 聯貸動撥之子公司一<br>
	* 刪除(L3600.L3010有使用一併修改)
	* @return String
	*/
  public String getSubCustUkey1() {
    return this.subCustUkey1 == null ? "" : this.subCustUkey1;
  }

/**
	* 聯貸動撥之子公司一<br>
	* 刪除(L3600.L3010有使用一併修改)
  *
  * @param subCustUkey1 聯貸動撥之子公司一
	*/
  public void setSubCustUkey1(String subCustUkey1) {
    this.subCustUkey1 = subCustUkey1;
  }

/**
	* 聯貸動撥之子公司二<br>
	* 刪除(L3600.L3010有使用一併修改)
	* @return String
	*/
  public String getSubCustUkey2() {
    return this.subCustUkey2 == null ? "" : this.subCustUkey2;
  }

/**
	* 聯貸動撥之子公司二<br>
	* 刪除(L3600.L3010有使用一併修改)
  *
  * @param subCustUkey2 聯貸動撥之子公司二
	*/
  public void setSubCustUkey2(String subCustUkey2) {
    this.subCustUkey2 = subCustUkey2;
  }

/**
	* 聯貸動撥之子公司三<br>
	* 刪除(L3600.L3010有使用一併修改)
	* @return String
	*/
  public String getSubCustUkey3() {
    return this.subCustUkey3 == null ? "" : this.subCustUkey3;
  }

/**
	* 聯貸動撥之子公司三<br>
	* 刪除(L3600.L3010有使用一併修改)
  *
  * @param subCustUkey3 聯貸動撥之子公司三
	*/
  public void setSubCustUkey3(String subCustUkey3) {
    this.subCustUkey3 = subCustUkey3;
  }

/**
	* 聯貸動撥之子公司四<br>
	* 刪除(L3600.L3010有使用一併修改)
	* @return String
	*/
  public String getSubCustUkey4() {
    return this.subCustUkey4 == null ? "" : this.subCustUkey4;
  }

/**
	* 聯貸動撥之子公司四<br>
	* 刪除(L3600.L3010有使用一併修改)
  *
  * @param subCustUkey4 聯貸動撥之子公司四
	*/
  public void setSubCustUkey4(String subCustUkey4) {
    this.subCustUkey4 = subCustUkey4;
  }

/**
	* 聯貸動撥之子公司五<br>
	* 刪除(L3600.L3010有使用一併修改)
	* @return String
	*/
  public String getSubCustUkey5() {
    return this.subCustUkey5 == null ? "" : this.subCustUkey5;
  }

/**
	* 聯貸動撥之子公司五<br>
	* 刪除(L3600.L3010有使用一併修改)
  *
  * @param subCustUkey5 聯貸動撥之子公司五
	*/
  public void setSubCustUkey5(String subCustUkey5) {
    this.subCustUkey5 = subCustUkey5;
  }

/**
	* 聯貸動撥之子公司六<br>
	* 刪除(L3600.L3010有使用一併修改)
	* @return String
	*/
  public String getSubCustUkey6() {
    return this.subCustUkey6 == null ? "" : this.subCustUkey6;
  }

/**
	* 聯貸動撥之子公司六<br>
	* 刪除(L3600.L3010有使用一併修改)
  *
  * @param subCustUkey6 聯貸動撥之子公司六
	*/
  public void setSubCustUkey6(String subCustUkey6) {
    this.subCustUkey6 = subCustUkey6;
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
    return "LoanSynd [loanSyndId=" + loanSyndId + ", lastBorxNo=" + lastBorxNo + ", custUKey=" + custUKey + ", guaUKey=" + guaUKey + ", leadingBank=" + leadingBank
           + ", signingDate=" + signingDate + ", drawdownStartDate=" + drawdownStartDate + ", drawdownEndDate=" + drawdownEndDate + ", commitFeeFlag=" + commitFeeFlag + ", partRate=" + partRate + ", currencyCode=" + currencyCode
           + ", syndAmt=" + syndAmt + ", partAmt=" + partAmt + ", agentBank=" + agentBank + ", creditPeriod=" + creditPeriod + ", centralBankPercent=" + centralBankPercent + ", masterCustUkey=" + masterCustUkey
           + ", subCustUkey1=" + subCustUkey1 + ", subCustUkey2=" + subCustUkey2 + ", subCustUkey3=" + subCustUkey3 + ", subCustUkey4=" + subCustUkey4 + ", subCustUkey5=" + subCustUkey5 + ", subCustUkey6=" + subCustUkey6
           + ", createDate=" + createDate + ", createEmpNo=" + createEmpNo + ", lastUpdate=" + lastUpdate + ", lastUpdateEmpNo=" + lastUpdateEmpNo + "]";
  }
}
