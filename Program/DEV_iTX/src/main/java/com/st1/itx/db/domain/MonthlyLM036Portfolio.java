package com.st1.itx.db.domain;

import java.io.Serializable;
import java.math.BigDecimal;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.EntityListeners;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import javax.persistence.Id;
import javax.persistence.Column;

/**
 * MonthlyLM036Portfolio LM036Portfolio<br>
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "`MonthlyLM036Portfolio`")
public class MonthlyLM036Portfolio implements Serializable {


  /**
	 * 
	 */
	private static final long serialVersionUID = -3937829583188910427L;

// 資料年月
  @Id
  @Column(name = "`DataMonth`")
  private int dataMonth = 0;

  // 月底日期
  @Column(name = "`MonthEndDate`")
  private int monthEndDate = 0;

  // 授信組合餘額
  /* 自然人放款+法人放款+溢折價與催收費用 */
  @Column(name = "`PortfolioTotal`")
  private BigDecimal portfolioTotal = new BigDecimal("0");

  // 自然人放款
  @Column(name = "`NaturalPersonLoanBal`")
  private BigDecimal naturalPersonLoanBal = new BigDecimal("0");

  // 法人放款
  @Column(name = "`LegalPersonLoanBal`")
  private BigDecimal legalPersonLoanBal = new BigDecimal("0");

  // 聯貸案
  /* 法人放款之細項 */
  @Column(name = "`SyndLoanBal`")
  private BigDecimal syndLoanBal = new BigDecimal("0");

  // 股票質押
  /* 法人放款之細項 */
  @Column(name = "`StockLoanBal`")
  private BigDecimal stockLoanBal = new BigDecimal("0");

  // 一般法人放款
  /* 法人放款之細項 */
  @Column(name = "`OtherLoanbal`")
  private BigDecimal otherLoanbal = new BigDecimal("0");

  // 溢折價
  @Column(name = "`AmortizeTotal`")
  private BigDecimal amortizeTotal = new BigDecimal("0");

  // 催收費用
  /* 催收法務費用+催收火險費用 */
  @Column(name = "`OvduExpense`")
  private BigDecimal ovduExpense = new BigDecimal("0");

  // 自然人大額授信件件數
  /* 自然人放款一千萬以上 */
  @Column(name = "`NaturalPersonLargeCounts`")
  private BigDecimal naturalPersonLargeCounts = new BigDecimal("0");

  // 自然人大額授信件餘額
  /* 自然人放款一千萬以上 */
  @Column(name = "`NaturalPersonLargeTotal`")
  private BigDecimal naturalPersonLargeTotal = new BigDecimal("0");

  // 法人大額授信件件數
  /* 法人放款三千萬以上 */
  @Column(name = "`LegalPersonLargeCounts`")
  private BigDecimal legalPersonLargeCounts = new BigDecimal("0");

  // 法人大額授信件餘額
  /* 法人放款三千萬以上 */
  @Column(name = "`LegalPersonLargeTotal`")
  private BigDecimal legalPersonLargeTotal = new BigDecimal("0");

  // 自然人放款占比
  @Column(name = "`NaturalPersonPercent`")
  private BigDecimal naturalPersonPercent = new BigDecimal("0");

  // 法人放款占比
  @Column(name = "`LegalPersonPercent`")
  private BigDecimal legalPersonPercent = new BigDecimal("0");

  // 聯貸案占比
  @Column(name = "`SyndPercent`")
  private BigDecimal syndPercent = new BigDecimal("0");

  // 股票質押占比
  @Column(name = "`StockPercent`")
  private BigDecimal stockPercent = new BigDecimal("0");

  // 一般法人放款占比
  @Column(name = "`OtherPercent`")
  private BigDecimal otherPercent = new BigDecimal("0");

  // 企業放款動用率
  @Column(name = "`EntUsedPercent`")
  private BigDecimal entUsedPercent = new BigDecimal("0");

  // 保單分紅利率
  @Column(name = "`InsuDividendRate`")
  private BigDecimal insuDividendRate = new BigDecimal("0");

  // 自然人當月平均利率
  @Column(name = "`NaturalPersonRate`")
  private BigDecimal naturalPersonRate = new BigDecimal("0");

  // 法人當月平均利率
  @Column(name = "`LegalPersonRate`")
  private BigDecimal legalPersonRate = new BigDecimal("0");

  // 聯貸案平均利率
  @Column(name = "`SyndRate`")
  private BigDecimal syndRate = new BigDecimal("0");

  // 股票質押平均利率
  @Column(name = "`StockRate`")
  private BigDecimal stockRate = new BigDecimal("0");

  // 一般法人放款平均利率
  @Column(name = "`OtherRate`")
  private BigDecimal otherRate = new BigDecimal("0");

  // 放款平均利率
  @Column(name = "`AvgRate`")
  private BigDecimal avgRate = new BigDecimal("0");

  // 房貸通路當月毛報酬率
  @Column(name = "`HouseRateOfReturn`")
  private BigDecimal houseRateOfReturn = new BigDecimal("0");

  // 企金通路當月毛報酬率
  @Column(name = "`EntRateOfReturn`")
  private BigDecimal entRateOfReturn = new BigDecimal("0");

  // 放款毛報酬率
  @Column(name = "`RateOfReturn`")
  private BigDecimal rateOfReturn = new BigDecimal("0");

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


/**
	* 資料年月<br>
	* 
	* @return Integer
	*/
  public int getDataMonth() {
    return this.dataMonth;
  }

/**
	* 資料年月<br>
	* 
  *
  * @param dataMonth 資料年月
	*/
  public void setDataMonth(int dataMonth) {
    this.dataMonth = dataMonth;
  }

/**
	* 月底日期<br>
	* 
	* @return Integer
	*/
  public int getMonthEndDate() {
    return this.monthEndDate;
  }

/**
	* 月底日期<br>
	* 
  *
  * @param monthEndDate 月底日期
	*/
  public void setMonthEndDate(int monthEndDate) {
    this.monthEndDate = monthEndDate;
  }

/**
	* 授信組合餘額<br>
	* 自然人放款
+法人放款
+溢折價與催收費用
	* @return BigDecimal
	*/
  public BigDecimal getPortfolioTotal() {
    return this.portfolioTotal;
  }

/**
	* 授信組合餘額<br>
	* 自然人放款
+法人放款
+溢折價與催收費用
  *
  * @param portfolioTotal 授信組合餘額
	*/
  public void setPortfolioTotal(BigDecimal portfolioTotal) {
    this.portfolioTotal = portfolioTotal;
  }

/**
	* 自然人放款<br>
	* 
	* @return BigDecimal
	*/
  public BigDecimal getNaturalPersonLoanBal() {
    return this.naturalPersonLoanBal;
  }

/**
	* 自然人放款<br>
	* 
  *
  * @param naturalPersonLoanBal 自然人放款
	*/
  public void setNaturalPersonLoanBal(BigDecimal naturalPersonLoanBal) {
    this.naturalPersonLoanBal = naturalPersonLoanBal;
  }

/**
	* 法人放款<br>
	* 
	* @return BigDecimal
	*/
  public BigDecimal getLegalPersonLoanBal() {
    return this.legalPersonLoanBal;
  }

/**
	* 法人放款<br>
	* 
  *
  * @param legalPersonLoanBal 法人放款
	*/
  public void setLegalPersonLoanBal(BigDecimal legalPersonLoanBal) {
    this.legalPersonLoanBal = legalPersonLoanBal;
  }

/**
	* 聯貸案<br>
	* 法人放款之細項
	* @return BigDecimal
	*/
  public BigDecimal getSyndLoanBal() {
    return this.syndLoanBal;
  }

/**
	* 聯貸案<br>
	* 法人放款之細項
  *
  * @param syndLoanBal 聯貸案
	*/
  public void setSyndLoanBal(BigDecimal syndLoanBal) {
    this.syndLoanBal = syndLoanBal;
  }

/**
	* 股票質押<br>
	* 法人放款之細項
	* @return BigDecimal
	*/
  public BigDecimal getStockLoanBal() {
    return this.stockLoanBal;
  }

/**
	* 股票質押<br>
	* 法人放款之細項
  *
  * @param stockLoanBal 股票質押
	*/
  public void setStockLoanBal(BigDecimal stockLoanBal) {
    this.stockLoanBal = stockLoanBal;
  }

/**
	* 一般法人放款<br>
	* 法人放款之細項
	* @return BigDecimal
	*/
  public BigDecimal getOtherLoanbal() {
    return this.otherLoanbal;
  }

/**
	* 一般法人放款<br>
	* 法人放款之細項
  *
  * @param otherLoanbal 一般法人放款
	*/
  public void setOtherLoanbal(BigDecimal otherLoanbal) {
    this.otherLoanbal = otherLoanbal;
  }

/**
	* 溢折價<br>
	* 
	* @return BigDecimal
	*/
  public BigDecimal getAmortizeTotal() {
    return this.amortizeTotal;
  }

/**
	* 溢折價<br>
	* 
  *
  * @param amortizeTotal 溢折價
	*/
  public void setAmortizeTotal(BigDecimal amortizeTotal) {
    this.amortizeTotal = amortizeTotal;
  }

/**
	* 催收費用<br>
	* 催收法務費用
+催收火險費用
	* @return BigDecimal
	*/
  public BigDecimal getOvduExpense() {
    return this.ovduExpense;
  }

/**
	* 催收費用<br>
	* 催收法務費用
+催收火險費用
  *
  * @param ovduExpense 催收費用
	*/
  public void setOvduExpense(BigDecimal ovduExpense) {
    this.ovduExpense = ovduExpense;
  }

/**
	* 自然人大額授信件件數<br>
	* 自然人放款一千萬以上
	* @return BigDecimal
	*/
  public BigDecimal getNaturalPersonLargeCounts() {
    return this.naturalPersonLargeCounts;
  }

/**
	* 自然人大額授信件件數<br>
	* 自然人放款一千萬以上
  *
  * @param naturalPersonLargeCounts 自然人大額授信件件數
	*/
  public void setNaturalPersonLargeCounts(BigDecimal naturalPersonLargeCounts) {
    this.naturalPersonLargeCounts = naturalPersonLargeCounts;
  }

/**
	* 自然人大額授信件餘額<br>
	* 自然人放款一千萬以上
	* @return BigDecimal
	*/
  public BigDecimal getNaturalPersonLargeTotal() {
    return this.naturalPersonLargeTotal;
  }

/**
	* 自然人大額授信件餘額<br>
	* 自然人放款一千萬以上
  *
  * @param naturalPersonLargeTotal 自然人大額授信件餘額
	*/
  public void setNaturalPersonLargeTotal(BigDecimal naturalPersonLargeTotal) {
    this.naturalPersonLargeTotal = naturalPersonLargeTotal;
  }

/**
	* 法人大額授信件件數<br>
	* 法人放款三千萬以上
	* @return BigDecimal
	*/
  public BigDecimal getLegalPersonLargeCounts() {
    return this.legalPersonLargeCounts;
  }

/**
	* 法人大額授信件件數<br>
	* 法人放款三千萬以上
  *
  * @param legalPersonLargeCounts 法人大額授信件件數
	*/
  public void setLegalPersonLargeCounts(BigDecimal legalPersonLargeCounts) {
    this.legalPersonLargeCounts = legalPersonLargeCounts;
  }

/**
	* 法人大額授信件餘額<br>
	* 法人放款三千萬以上
	* @return BigDecimal
	*/
  public BigDecimal getLegalPersonLargeTotal() {
    return this.legalPersonLargeTotal;
  }

/**
	* 法人大額授信件餘額<br>
	* 法人放款三千萬以上
  *
  * @param legalPersonLargeTotal 法人大額授信件餘額
	*/
  public void setLegalPersonLargeTotal(BigDecimal legalPersonLargeTotal) {
    this.legalPersonLargeTotal = legalPersonLargeTotal;
  }

/**
	* 自然人放款占比<br>
	* 
	* @return BigDecimal
	*/
  public BigDecimal getNaturalPersonPercent() {
    return this.naturalPersonPercent;
  }

/**
	* 自然人放款占比<br>
	* 
  *
  * @param naturalPersonPercent 自然人放款占比
	*/
  public void setNaturalPersonPercent(BigDecimal naturalPersonPercent) {
    this.naturalPersonPercent = naturalPersonPercent;
  }

/**
	* 法人放款占比<br>
	* 
	* @return BigDecimal
	*/
  public BigDecimal getLegalPersonPercent() {
    return this.legalPersonPercent;
  }

/**
	* 法人放款占比<br>
	* 
  *
  * @param legalPersonPercent 法人放款占比
	*/
  public void setLegalPersonPercent(BigDecimal legalPersonPercent) {
    this.legalPersonPercent = legalPersonPercent;
  }

/**
	* 聯貸案占比<br>
	* 
	* @return BigDecimal
	*/
  public BigDecimal getSyndPercent() {
    return this.syndPercent;
  }

/**
	* 聯貸案占比<br>
	* 
  *
  * @param syndPercent 聯貸案占比
	*/
  public void setSyndPercent(BigDecimal syndPercent) {
    this.syndPercent = syndPercent;
  }

/**
	* 股票質押占比<br>
	* 
	* @return BigDecimal
	*/
  public BigDecimal getStockPercent() {
    return this.stockPercent;
  }

/**
	* 股票質押占比<br>
	* 
  *
  * @param stockPercent 股票質押占比
	*/
  public void setStockPercent(BigDecimal stockPercent) {
    this.stockPercent = stockPercent;
  }

/**
	* 一般法人放款占比<br>
	* 
	* @return BigDecimal
	*/
  public BigDecimal getOtherPercent() {
    return this.otherPercent;
  }

/**
	* 一般法人放款占比<br>
	* 
  *
  * @param otherPercent 一般法人放款占比
	*/
  public void setOtherPercent(BigDecimal otherPercent) {
    this.otherPercent = otherPercent;
  }

/**
	* 企業放款動用率<br>
	* 
	* @return BigDecimal
	*/
  public BigDecimal getEntUsedPercent() {
    return this.entUsedPercent;
  }

/**
	* 企業放款動用率<br>
	* 
  *
  * @param entUsedPercent 企業放款動用率
	*/
  public void setEntUsedPercent(BigDecimal entUsedPercent) {
    this.entUsedPercent = entUsedPercent;
  }

/**
	* 保單分紅利率<br>
	* 
	* @return BigDecimal
	*/
  public BigDecimal getInsuDividendRate() {
    return this.insuDividendRate;
  }

/**
	* 保單分紅利率<br>
	* 
  *
  * @param insuDividendRate 保單分紅利率
	*/
  public void setInsuDividendRate(BigDecimal insuDividendRate) {
    this.insuDividendRate = insuDividendRate;
  }

/**
	* 自然人當月平均利率<br>
	* 
	* @return BigDecimal
	*/
  public BigDecimal getNaturalPersonRate() {
    return this.naturalPersonRate;
  }

/**
	* 自然人當月平均利率<br>
	* 
  *
  * @param naturalPersonRate 自然人當月平均利率
	*/
  public void setNaturalPersonRate(BigDecimal naturalPersonRate) {
    this.naturalPersonRate = naturalPersonRate;
  }

/**
	* 法人當月平均利率<br>
	* 
	* @return BigDecimal
	*/
  public BigDecimal getLegalPersonRate() {
    return this.legalPersonRate;
  }

/**
	* 法人當月平均利率<br>
	* 
  *
  * @param legalPersonRate 法人當月平均利率
	*/
  public void setLegalPersonRate(BigDecimal legalPersonRate) {
    this.legalPersonRate = legalPersonRate;
  }

/**
	* 聯貸案平均利率<br>
	* 
	* @return BigDecimal
	*/
  public BigDecimal getSyndRate() {
    return this.syndRate;
  }

/**
	* 聯貸案平均利率<br>
	* 
  *
  * @param syndRate 聯貸案平均利率
	*/
  public void setSyndRate(BigDecimal syndRate) {
    this.syndRate = syndRate;
  }

/**
	* 股票質押平均利率<br>
	* 
	* @return BigDecimal
	*/
  public BigDecimal getStockRate() {
    return this.stockRate;
  }

/**
	* 股票質押平均利率<br>
	* 
  *
  * @param stockRate 股票質押平均利率
	*/
  public void setStockRate(BigDecimal stockRate) {
    this.stockRate = stockRate;
  }

/**
	* 一般法人放款平均利率<br>
	* 
	* @return BigDecimal
	*/
  public BigDecimal getOtherRate() {
    return this.otherRate;
  }

/**
	* 一般法人放款平均利率<br>
	* 
  *
  * @param otherRate 一般法人放款平均利率
	*/
  public void setOtherRate(BigDecimal otherRate) {
    this.otherRate = otherRate;
  }

/**
	* 放款平均利率<br>
	* 
	* @return BigDecimal
	*/
  public BigDecimal getAvgRate() {
    return this.avgRate;
  }

/**
	* 放款平均利率<br>
	* 
  *
  * @param avgRate 放款平均利率
	*/
  public void setAvgRate(BigDecimal avgRate) {
    this.avgRate = avgRate;
  }

/**
	* 房貸通路當月毛報酬率<br>
	* 
	* @return BigDecimal
	*/
  public BigDecimal getHouseRateOfReturn() {
    return this.houseRateOfReturn;
  }

/**
	* 房貸通路當月毛報酬率<br>
	* 
  *
  * @param houseRateOfReturn 房貸通路當月毛報酬率
	*/
  public void setHouseRateOfReturn(BigDecimal houseRateOfReturn) {
    this.houseRateOfReturn = houseRateOfReturn;
  }

/**
	* 企金通路當月毛報酬率<br>
	* 
	* @return BigDecimal
	*/
  public BigDecimal getEntRateOfReturn() {
    return this.entRateOfReturn;
  }

/**
	* 企金通路當月毛報酬率<br>
	* 
  *
  * @param entRateOfReturn 企金通路當月毛報酬率
	*/
  public void setEntRateOfReturn(BigDecimal entRateOfReturn) {
    this.entRateOfReturn = entRateOfReturn;
  }

/**
	* 放款毛報酬率<br>
	* 
	* @return BigDecimal
	*/
  public BigDecimal getRateOfReturn() {
    return this.rateOfReturn;
  }

/**
	* 放款毛報酬率<br>
	* 
  *
  * @param rateOfReturn 放款毛報酬率
	*/
  public void setRateOfReturn(BigDecimal rateOfReturn) {
    this.rateOfReturn = rateOfReturn;
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
    return "MonthlyLM036Portfolio [dataMonth=" + dataMonth + ", monthEndDate=" + monthEndDate + ", portfolioTotal=" + portfolioTotal + ", naturalPersonLoanBal=" + naturalPersonLoanBal + ", legalPersonLoanBal=" + legalPersonLoanBal + ", syndLoanBal=" + syndLoanBal
           + ", stockLoanBal=" + stockLoanBal + ", otherLoanbal=" + otherLoanbal + ", amortizeTotal=" + amortizeTotal + ", ovduExpense=" + ovduExpense + ", naturalPersonLargeCounts=" + naturalPersonLargeCounts + ", naturalPersonLargeTotal=" + naturalPersonLargeTotal
           + ", legalPersonLargeCounts=" + legalPersonLargeCounts + ", legalPersonLargeTotal=" + legalPersonLargeTotal + ", naturalPersonPercent=" + naturalPersonPercent + ", legalPersonPercent=" + legalPersonPercent + ", syndPercent=" + syndPercent + ", stockPercent=" + stockPercent
           + ", otherPercent=" + otherPercent + ", entUsedPercent=" + entUsedPercent + ", insuDividendRate=" + insuDividendRate + ", naturalPersonRate=" + naturalPersonRate + ", legalPersonRate=" + legalPersonRate + ", syndRate=" + syndRate
           + ", stockRate=" + stockRate + ", otherRate=" + otherRate + ", avgRate=" + avgRate + ", houseRateOfReturn=" + houseRateOfReturn + ", entRateOfReturn=" + entRateOfReturn + ", rateOfReturn=" + rateOfReturn
           + ", createDate=" + createDate + ", createEmpNo=" + createEmpNo + ", lastUpdate=" + lastUpdate + ", lastUpdateEmpNo=" + lastUpdateEmpNo + "]";
  }
}
