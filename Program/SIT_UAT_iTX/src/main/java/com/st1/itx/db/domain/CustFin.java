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
 * CustFin 公司戶財務狀況檔<br>
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "`CustFin`")
public class CustFin implements Serializable {


  /**
	 * 
	 */
	private static final long serialVersionUID = -2831640431597273784L;

@EmbeddedId
  private CustFinId custFinId;

  // 客戶識別碼
  @Column(name = "`CustUKey`", length = 32, insertable = false, updatable = false)
  private String custUKey;

  // 年度
  @Column(name = "`DataYear`", insertable = false, updatable = false)
  private int dataYear = 0;

  // 資產總額
  @Column(name = "`AssetTotal`")
  private BigDecimal assetTotal = new BigDecimal("0");

  // 現金/銀存
  @Column(name = "`Cash`")
  private BigDecimal cash = new BigDecimal("0");

  // 短期投資
  @Column(name = "`ShortInv`")
  private BigDecimal shortInv = new BigDecimal("0");

  // 應收帳款票據
  @Column(name = "`AR`")
  private BigDecimal aR = new BigDecimal("0");

  // 存貨
  @Column(name = "`Inventory`")
  private BigDecimal inventory = new BigDecimal("0");

  // 長期投資
  @Column(name = "`LongInv`")
  private BigDecimal longInv = new BigDecimal("0");

  // 固定資產
  @Column(name = "`FixedAsset`")
  private BigDecimal fixedAsset = new BigDecimal("0");

  // 其他資產
  @Column(name = "`OtherAsset`")
  private BigDecimal otherAsset = new BigDecimal("0");

  // 負債總額
  @Column(name = "`LiabTotal`")
  private BigDecimal liabTotal = new BigDecimal("0");

  // 銀行借款
  @Column(name = "`BankLoan`")
  private BigDecimal bankLoan = new BigDecimal("0");

  // 其他流動負債
  @Column(name = "`OtherCurrLiab`")
  private BigDecimal otherCurrLiab = new BigDecimal("0");

  // 長期負債
  @Column(name = "`LongLiab`")
  private BigDecimal longLiab = new BigDecimal("0");

  // 其他負債
  @Column(name = "`OtherLiab`")
  private BigDecimal otherLiab = new BigDecimal("0");

  // 淨值總額
  @Column(name = "`NetWorthTotal`")
  private BigDecimal netWorthTotal = new BigDecimal("0");

  // 資本
  @Column(name = "`Capital`")
  private BigDecimal capital = new BigDecimal("0");

  // 公積保留盈餘
  @Column(name = "`RetainEarning`")
  private BigDecimal retainEarning = new BigDecimal("0");

  // 營業收入
  @Column(name = "`OpIncome`")
  private BigDecimal opIncome = new BigDecimal("0");

  // 營業成本
  @Column(name = "`OpCost`")
  private BigDecimal opCost = new BigDecimal("0");

  // 營業毛利
  @Column(name = "`OpProfit`")
  private BigDecimal opProfit = new BigDecimal("0");

  // 管銷費用
  @Column(name = "`OpExpense`")
  private BigDecimal opExpense = new BigDecimal("0");

  // 營業利益
  @Column(name = "`OpRevenue`")
  private BigDecimal opRevenue = new BigDecimal("0");

  // 營業外收入
  @Column(name = "`NopIncome`")
  private BigDecimal nopIncome = new BigDecimal("0");

  // 財務支出
  @Column(name = "`FinExpense`")
  private BigDecimal finExpense = new BigDecimal("0");

  // 其他營業外支
  @Column(name = "`NopExpense`")
  private BigDecimal nopExpense = new BigDecimal("0");

  // 稅後淨利
  @Column(name = "`NetIncome`")
  private BigDecimal netIncome = new BigDecimal("0");

  // 簽證會計師
  @Column(name = "`Accountant`", length = 14)
  private String accountant;

  // 簽證日期
  @Column(name = "`AccountDate`")
  private int accountDate = 0;

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


  public CustFinId getCustFinId() {
    return this.custFinId;
  }

  public void setCustFinId(CustFinId custFinId) {
    this.custFinId = custFinId;
  }

/**
	* 客戶識別碼<br>
	* 
	* @return String
	*/
  public String getCustUKey() {
    return this.custUKey == null ? "" : this.custUKey;
  }

/**
	* 客戶識別碼<br>
	* 
  *
  * @param custUKey 客戶識別碼
	*/
  public void setCustUKey(String custUKey) {
    this.custUKey = custUKey;
  }

/**
	* 年度<br>
	* 
	* @return Integer
	*/
  public int getDataYear() {
    return this.dataYear;
  }

/**
	* 年度<br>
	* 
  *
  * @param dataYear 年度
	*/
  public void setDataYear(int dataYear) {
    this.dataYear = dataYear;
  }

/**
	* 資產總額<br>
	* 
	* @return BigDecimal
	*/
  public BigDecimal getAssetTotal() {
    return this.assetTotal;
  }

/**
	* 資產總額<br>
	* 
  *
  * @param assetTotal 資產總額
	*/
  public void setAssetTotal(BigDecimal assetTotal) {
    this.assetTotal = assetTotal;
  }

/**
	* 現金/銀存<br>
	* 
	* @return BigDecimal
	*/
  public BigDecimal getCash() {
    return this.cash;
  }

/**
	* 現金/銀存<br>
	* 
  *
  * @param cash 現金/銀存
	*/
  public void setCash(BigDecimal cash) {
    this.cash = cash;
  }

/**
	* 短期投資<br>
	* 
	* @return BigDecimal
	*/
  public BigDecimal getShortInv() {
    return this.shortInv;
  }

/**
	* 短期投資<br>
	* 
  *
  * @param shortInv 短期投資
	*/
  public void setShortInv(BigDecimal shortInv) {
    this.shortInv = shortInv;
  }

/**
	* 應收帳款票據<br>
	* 
	* @return BigDecimal
	*/
  public BigDecimal getAR() {
    return this.aR;
  }

/**
	* 應收帳款票據<br>
	* 
  *
  * @param aR 應收帳款票據
	*/
  public void setAR(BigDecimal aR) {
    this.aR = aR;
  }

/**
	* 存貨<br>
	* 
	* @return BigDecimal
	*/
  public BigDecimal getInventory() {
    return this.inventory;
  }

/**
	* 存貨<br>
	* 
  *
  * @param inventory 存貨
	*/
  public void setInventory(BigDecimal inventory) {
    this.inventory = inventory;
  }

/**
	* 長期投資<br>
	* 
	* @return BigDecimal
	*/
  public BigDecimal getLongInv() {
    return this.longInv;
  }

/**
	* 長期投資<br>
	* 
  *
  * @param longInv 長期投資
	*/
  public void setLongInv(BigDecimal longInv) {
    this.longInv = longInv;
  }

/**
	* 固定資產<br>
	* 
	* @return BigDecimal
	*/
  public BigDecimal getFixedAsset() {
    return this.fixedAsset;
  }

/**
	* 固定資產<br>
	* 
  *
  * @param fixedAsset 固定資產
	*/
  public void setFixedAsset(BigDecimal fixedAsset) {
    this.fixedAsset = fixedAsset;
  }

/**
	* 其他資產<br>
	* 
	* @return BigDecimal
	*/
  public BigDecimal getOtherAsset() {
    return this.otherAsset;
  }

/**
	* 其他資產<br>
	* 
  *
  * @param otherAsset 其他資產
	*/
  public void setOtherAsset(BigDecimal otherAsset) {
    this.otherAsset = otherAsset;
  }

/**
	* 負債總額<br>
	* 
	* @return BigDecimal
	*/
  public BigDecimal getLiabTotal() {
    return this.liabTotal;
  }

/**
	* 負債總額<br>
	* 
  *
  * @param liabTotal 負債總額
	*/
  public void setLiabTotal(BigDecimal liabTotal) {
    this.liabTotal = liabTotal;
  }

/**
	* 銀行借款<br>
	* 
	* @return BigDecimal
	*/
  public BigDecimal getBankLoan() {
    return this.bankLoan;
  }

/**
	* 銀行借款<br>
	* 
  *
  * @param bankLoan 銀行借款
	*/
  public void setBankLoan(BigDecimal bankLoan) {
    this.bankLoan = bankLoan;
  }

/**
	* 其他流動負債<br>
	* 
	* @return BigDecimal
	*/
  public BigDecimal getOtherCurrLiab() {
    return this.otherCurrLiab;
  }

/**
	* 其他流動負債<br>
	* 
  *
  * @param otherCurrLiab 其他流動負債
	*/
  public void setOtherCurrLiab(BigDecimal otherCurrLiab) {
    this.otherCurrLiab = otherCurrLiab;
  }

/**
	* 長期負債<br>
	* 
	* @return BigDecimal
	*/
  public BigDecimal getLongLiab() {
    return this.longLiab;
  }

/**
	* 長期負債<br>
	* 
  *
  * @param longLiab 長期負債
	*/
  public void setLongLiab(BigDecimal longLiab) {
    this.longLiab = longLiab;
  }

/**
	* 其他負債<br>
	* 
	* @return BigDecimal
	*/
  public BigDecimal getOtherLiab() {
    return this.otherLiab;
  }

/**
	* 其他負債<br>
	* 
  *
  * @param otherLiab 其他負債
	*/
  public void setOtherLiab(BigDecimal otherLiab) {
    this.otherLiab = otherLiab;
  }

/**
	* 淨值總額<br>
	* 
	* @return BigDecimal
	*/
  public BigDecimal getNetWorthTotal() {
    return this.netWorthTotal;
  }

/**
	* 淨值總額<br>
	* 
  *
  * @param netWorthTotal 淨值總額
	*/
  public void setNetWorthTotal(BigDecimal netWorthTotal) {
    this.netWorthTotal = netWorthTotal;
  }

/**
	* 資本<br>
	* 
	* @return BigDecimal
	*/
  public BigDecimal getCapital() {
    return this.capital;
  }

/**
	* 資本<br>
	* 
  *
  * @param capital 資本
	*/
  public void setCapital(BigDecimal capital) {
    this.capital = capital;
  }

/**
	* 公積保留盈餘<br>
	* 
	* @return BigDecimal
	*/
  public BigDecimal getRetainEarning() {
    return this.retainEarning;
  }

/**
	* 公積保留盈餘<br>
	* 
  *
  * @param retainEarning 公積保留盈餘
	*/
  public void setRetainEarning(BigDecimal retainEarning) {
    this.retainEarning = retainEarning;
  }

/**
	* 營業收入<br>
	* 
	* @return BigDecimal
	*/
  public BigDecimal getOpIncome() {
    return this.opIncome;
  }

/**
	* 營業收入<br>
	* 
  *
  * @param opIncome 營業收入
	*/
  public void setOpIncome(BigDecimal opIncome) {
    this.opIncome = opIncome;
  }

/**
	* 營業成本<br>
	* 
	* @return BigDecimal
	*/
  public BigDecimal getOpCost() {
    return this.opCost;
  }

/**
	* 營業成本<br>
	* 
  *
  * @param opCost 營業成本
	*/
  public void setOpCost(BigDecimal opCost) {
    this.opCost = opCost;
  }

/**
	* 營業毛利<br>
	* 
	* @return BigDecimal
	*/
  public BigDecimal getOpProfit() {
    return this.opProfit;
  }

/**
	* 營業毛利<br>
	* 
  *
  * @param opProfit 營業毛利
	*/
  public void setOpProfit(BigDecimal opProfit) {
    this.opProfit = opProfit;
  }

/**
	* 管銷費用<br>
	* 
	* @return BigDecimal
	*/
  public BigDecimal getOpExpense() {
    return this.opExpense;
  }

/**
	* 管銷費用<br>
	* 
  *
  * @param opExpense 管銷費用
	*/
  public void setOpExpense(BigDecimal opExpense) {
    this.opExpense = opExpense;
  }

/**
	* 營業利益<br>
	* 
	* @return BigDecimal
	*/
  public BigDecimal getOpRevenue() {
    return this.opRevenue;
  }

/**
	* 營業利益<br>
	* 
  *
  * @param opRevenue 營業利益
	*/
  public void setOpRevenue(BigDecimal opRevenue) {
    this.opRevenue = opRevenue;
  }

/**
	* 營業外收入<br>
	* 
	* @return BigDecimal
	*/
  public BigDecimal getNopIncome() {
    return this.nopIncome;
  }

/**
	* 營業外收入<br>
	* 
  *
  * @param nopIncome 營業外收入
	*/
  public void setNopIncome(BigDecimal nopIncome) {
    this.nopIncome = nopIncome;
  }

/**
	* 財務支出<br>
	* 
	* @return BigDecimal
	*/
  public BigDecimal getFinExpense() {
    return this.finExpense;
  }

/**
	* 財務支出<br>
	* 
  *
  * @param finExpense 財務支出
	*/
  public void setFinExpense(BigDecimal finExpense) {
    this.finExpense = finExpense;
  }

/**
	* 其他營業外支<br>
	* 
	* @return BigDecimal
	*/
  public BigDecimal getNopExpense() {
    return this.nopExpense;
  }

/**
	* 其他營業外支<br>
	* 
  *
  * @param nopExpense 其他營業外支
	*/
  public void setNopExpense(BigDecimal nopExpense) {
    this.nopExpense = nopExpense;
  }

/**
	* 稅後淨利<br>
	* 
	* @return BigDecimal
	*/
  public BigDecimal getNetIncome() {
    return this.netIncome;
  }

/**
	* 稅後淨利<br>
	* 
  *
  * @param netIncome 稅後淨利
	*/
  public void setNetIncome(BigDecimal netIncome) {
    this.netIncome = netIncome;
  }

/**
	* 簽證會計師<br>
	* 
	* @return String
	*/
  public String getAccountant() {
    return this.accountant == null ? "" : this.accountant;
  }

/**
	* 簽證會計師<br>
	* 
  *
  * @param accountant 簽證會計師
	*/
  public void setAccountant(String accountant) {
    this.accountant = accountant;
  }

/**
	* 簽證日期<br>
	* 
	* @return Integer
	*/
  public int getAccountDate() {
    return StaticTool.bcToRoc(this.accountDate);
  }

/**
	* 簽證日期<br>
	* 
  *
  * @param accountDate 簽證日期
  * @throws LogicException when Date Is Warn	*/
  public void setAccountDate(int accountDate) throws LogicException {
    this.accountDate = StaticTool.rocToBc(accountDate);
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
    return "CustFin [custFinId=" + custFinId + ", assetTotal=" + assetTotal + ", cash=" + cash + ", shortInv=" + shortInv + ", aR=" + aR
           + ", inventory=" + inventory + ", longInv=" + longInv + ", fixedAsset=" + fixedAsset + ", otherAsset=" + otherAsset + ", liabTotal=" + liabTotal + ", bankLoan=" + bankLoan
           + ", otherCurrLiab=" + otherCurrLiab + ", longLiab=" + longLiab + ", otherLiab=" + otherLiab + ", netWorthTotal=" + netWorthTotal + ", capital=" + capital + ", retainEarning=" + retainEarning
           + ", opIncome=" + opIncome + ", opCost=" + opCost + ", opProfit=" + opProfit + ", opExpense=" + opExpense + ", opRevenue=" + opRevenue + ", nopIncome=" + nopIncome
           + ", finExpense=" + finExpense + ", nopExpense=" + nopExpense + ", netIncome=" + netIncome + ", accountant=" + accountant + ", accountDate=" + accountDate + ", createDate=" + createDate
           + ", createEmpNo=" + createEmpNo + ", lastUpdate=" + lastUpdate + ", lastUpdateEmpNo=" + lastUpdateEmpNo + "]";
  }
}
