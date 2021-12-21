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

/**
 * FinReportReview 客戶財務報表.覆審比率表<br>
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "`FinReportReview`")
public class FinReportReview implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1842870499171634351L;

	@EmbeddedId
	private FinReportReviewId finReportReviewId;

	// 客戶識別碼
	@Column(name = "`CustUKey`", length = 32, insertable = false, updatable = false)
	private String custUKey;

	// 識別碼
	@Column(name = "`UKey`", length = 32, insertable = false, updatable = false)
	private String uKey;

	// 流動資產
	@Column(name = "`CurrentAsset`")
	private BigDecimal currentAsset = new BigDecimal("0");

	// 資產總額
	@Column(name = "`TotalAsset`")
	private BigDecimal totalAsset = new BigDecimal("0");

	// 不動產、廠房及設備淨額
	@Column(name = "`PropertyAsset`")
	private BigDecimal propertyAsset = new BigDecimal("0");

	// 權益法之投資
	@Column(name = "`Investment`")
	private BigDecimal investment = new BigDecimal("0");

	// 投資性不動產
	@Column(name = "`InvestmentProperty`")
	private BigDecimal investmentProperty = new BigDecimal("0");

	// 折舊及攤銷
	@Column(name = "`Depreciation`")
	private BigDecimal depreciation = new BigDecimal("0");

	// 流動負債
	@Column(name = "`CurrentDebt`")
	private BigDecimal currentDebt = new BigDecimal("0");

	// 負債合計
	@Column(name = "`TotalDebt`")
	private BigDecimal totalDebt = new BigDecimal("0");

	// 權益合計
	@Column(name = "`TotalEquity`")
	private BigDecimal totalEquity = new BigDecimal("0");

	// 應付公司債
	@Column(name = "`BondsPayable`")
	private BigDecimal bondsPayable = new BigDecimal("0");

	// 長期借款
	@Column(name = "`LongTermBorrowings`")
	private BigDecimal longTermBorrowings = new BigDecimal("0");

	// 應付租賃款-非流動
	@Column(name = "`NonCurrentLease`")
	private BigDecimal nonCurrentLease = new BigDecimal("0");

	// 長期應付票據及款項-關係人
	@Column(name = "`LongTermPayable`")
	private BigDecimal longTermPayable = new BigDecimal("0");

	// 特別股負債
	@Column(name = "`Preference`")
	private BigDecimal preference = new BigDecimal("0");

	// 營業收入
	@Column(name = "`OperatingRevenue`")
	private BigDecimal operatingRevenue = new BigDecimal("0");

	// 利息支出
	@Column(name = "`InterestExpense`")
	private BigDecimal interestExpense = new BigDecimal("0");

	// 稅前淨利
	@Column(name = "`ProfitBeforeTax`")
	private BigDecimal profitBeforeTax = new BigDecimal("0");

	// 本期淨利(稅後)
	@Column(name = "`ProfitAfterTax`")
	private BigDecimal profitAfterTax = new BigDecimal("0");

	// 流動比率
	@Column(name = "`WorkingCapitalRatio`")
	private BigDecimal workingCapitalRatio = new BigDecimal("0");

	// 利息保障倍數1
	@Column(name = "`InterestCoverageRatio1`")
	private BigDecimal interestCoverageRatio1 = new BigDecimal("0");

	// 利息保障倍數2
	@Column(name = "`InterestCoverageRatio2`")
	private BigDecimal interestCoverageRatio2 = new BigDecimal("0");

	// 槓桿比率
	@Column(name = "`LeverageRatio`")
	private BigDecimal leverageRatio = new BigDecimal("0");

	// 權益比率
	@Column(name = "`EquityRatio`")
	private BigDecimal equityRatio = new BigDecimal("0");

	// 固定長期適合率
	@Column(name = "`LongFitRatio`")
	private BigDecimal longFitRatio = new BigDecimal("0");

	// 純益率(稅後)
	@Column(name = "`NetProfitRatio`")
	private BigDecimal netProfitRatio = new BigDecimal("0");

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

	public FinReportReviewId getFinReportReviewId() {
		return this.finReportReviewId;
	}

	public void setFinReportReviewId(FinReportReviewId finReportReviewId) {
		this.finReportReviewId = finReportReviewId;
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
	 * 識別碼<br>
	 * 
	 * @return String
	 */
	public String getUKey() {
		return this.uKey == null ? "" : this.uKey;
	}

	/**
	 * 識別碼<br>
	 * 
	 *
	 * @param uKey 識別碼
	 */
	public void setUKey(String uKey) {
		this.uKey = uKey;
	}

	/**
	 * 流動資產<br>
	 * 
	 * @return BigDecimal
	 */
	public BigDecimal getCurrentAsset() {
		return this.currentAsset;
	}

	/**
	 * 流動資產<br>
	 * 
	 *
	 * @param currentAsset 流動資產
	 */
	public void setCurrentAsset(BigDecimal currentAsset) {
		this.currentAsset = currentAsset;
	}

	/**
	 * 資產總額<br>
	 * 
	 * @return BigDecimal
	 */
	public BigDecimal getTotalAsset() {
		return this.totalAsset;
	}

	/**
	 * 資產總額<br>
	 * 
	 *
	 * @param totalAsset 資產總額
	 */
	public void setTotalAsset(BigDecimal totalAsset) {
		this.totalAsset = totalAsset;
	}

	/**
	 * 不動產、廠房及設備淨額<br>
	 * 
	 * @return BigDecimal
	 */
	public BigDecimal getPropertyAsset() {
		return this.propertyAsset;
	}

	/**
	 * 不動產、廠房及設備淨額<br>
	 * 
	 *
	 * @param propertyAsset 不動產、廠房及設備淨額
	 */
	public void setPropertyAsset(BigDecimal propertyAsset) {
		this.propertyAsset = propertyAsset;
	}

	/**
	 * 權益法之投資<br>
	 * 
	 * @return BigDecimal
	 */
	public BigDecimal getInvestment() {
		return this.investment;
	}

	/**
	 * 權益法之投資<br>
	 * 
	 *
	 * @param investment 權益法之投資
	 */
	public void setInvestment(BigDecimal investment) {
		this.investment = investment;
	}

	/**
	 * 投資性不動產<br>
	 * 
	 * @return BigDecimal
	 */
	public BigDecimal getInvestmentProperty() {
		return this.investmentProperty;
	}

	/**
	 * 投資性不動產<br>
	 * 
	 *
	 * @param investmentProperty 投資性不動產
	 */
	public void setInvestmentProperty(BigDecimal investmentProperty) {
		this.investmentProperty = investmentProperty;
	}

	/**
	 * 折舊及攤銷<br>
	 * 
	 * @return BigDecimal
	 */
	public BigDecimal getDepreciation() {
		return this.depreciation;
	}

	/**
	 * 折舊及攤銷<br>
	 * 
	 *
	 * @param depreciation 折舊及攤銷
	 */
	public void setDepreciation(BigDecimal depreciation) {
		this.depreciation = depreciation;
	}

	/**
	 * 流動負債<br>
	 * 
	 * @return BigDecimal
	 */
	public BigDecimal getCurrentDebt() {
		return this.currentDebt;
	}

	/**
	 * 流動負債<br>
	 * 
	 *
	 * @param currentDebt 流動負債
	 */
	public void setCurrentDebt(BigDecimal currentDebt) {
		this.currentDebt = currentDebt;
	}

	/**
	 * 負債合計<br>
	 * 
	 * @return BigDecimal
	 */
	public BigDecimal getTotalDebt() {
		return this.totalDebt;
	}

	/**
	 * 負債合計<br>
	 * 
	 *
	 * @param totalDebt 負債合計
	 */
	public void setTotalDebt(BigDecimal totalDebt) {
		this.totalDebt = totalDebt;
	}

	/**
	 * 權益合計<br>
	 * 
	 * @return BigDecimal
	 */
	public BigDecimal getTotalEquity() {
		return this.totalEquity;
	}

	/**
	 * 權益合計<br>
	 * 
	 *
	 * @param totalEquity 權益合計
	 */
	public void setTotalEquity(BigDecimal totalEquity) {
		this.totalEquity = totalEquity;
	}

	/**
	 * 應付公司債<br>
	 * 
	 * @return BigDecimal
	 */
	public BigDecimal getBondsPayable() {
		return this.bondsPayable;
	}

	/**
	 * 應付公司債<br>
	 * 
	 *
	 * @param bondsPayable 應付公司債
	 */
	public void setBondsPayable(BigDecimal bondsPayable) {
		this.bondsPayable = bondsPayable;
	}

	/**
	 * 長期借款<br>
	 * 
	 * @return BigDecimal
	 */
	public BigDecimal getLongTermBorrowings() {
		return this.longTermBorrowings;
	}

	/**
	 * 長期借款<br>
	 * 
	 *
	 * @param longTermBorrowings 長期借款
	 */
	public void setLongTermBorrowings(BigDecimal longTermBorrowings) {
		this.longTermBorrowings = longTermBorrowings;
	}

	/**
	 * 應付租賃款-非流動<br>
	 * 
	 * @return BigDecimal
	 */
	public BigDecimal getNonCurrentLease() {
		return this.nonCurrentLease;
	}

	/**
	 * 應付租賃款-非流動<br>
	 * 
	 *
	 * @param nonCurrentLease 應付租賃款-非流動
	 */
	public void setNonCurrentLease(BigDecimal nonCurrentLease) {
		this.nonCurrentLease = nonCurrentLease;
	}

	/**
	 * 長期應付票據及款項-關係人<br>
	 * 
	 * @return BigDecimal
	 */
	public BigDecimal getLongTermPayable() {
		return this.longTermPayable;
	}

	/**
	 * 長期應付票據及款項-關係人<br>
	 * 
	 *
	 * @param longTermPayable 長期應付票據及款項-關係人
	 */
	public void setLongTermPayable(BigDecimal longTermPayable) {
		this.longTermPayable = longTermPayable;
	}

	/**
	 * 特別股負債<br>
	 * 
	 * @return BigDecimal
	 */
	public BigDecimal getPreference() {
		return this.preference;
	}

	/**
	 * 特別股負債<br>
	 * 
	 *
	 * @param preference 特別股負債
	 */
	public void setPreference(BigDecimal preference) {
		this.preference = preference;
	}

	/**
	 * 營業收入<br>
	 * 
	 * @return BigDecimal
	 */
	public BigDecimal getOperatingRevenue() {
		return this.operatingRevenue;
	}

	/**
	 * 營業收入<br>
	 * 
	 *
	 * @param operatingRevenue 營業收入
	 */
	public void setOperatingRevenue(BigDecimal operatingRevenue) {
		this.operatingRevenue = operatingRevenue;
	}

	/**
	 * 利息支出<br>
	 * 
	 * @return BigDecimal
	 */
	public BigDecimal getInterestExpense() {
		return this.interestExpense;
	}

	/**
	 * 利息支出<br>
	 * 
	 *
	 * @param interestExpense 利息支出
	 */
	public void setInterestExpense(BigDecimal interestExpense) {
		this.interestExpense = interestExpense;
	}

	/**
	 * 稅前淨利<br>
	 * 
	 * @return BigDecimal
	 */
	public BigDecimal getProfitBeforeTax() {
		return this.profitBeforeTax;
	}

	/**
	 * 稅前淨利<br>
	 * 
	 *
	 * @param profitBeforeTax 稅前淨利
	 */
	public void setProfitBeforeTax(BigDecimal profitBeforeTax) {
		this.profitBeforeTax = profitBeforeTax;
	}

	/**
	 * 本期淨利(稅後)<br>
	 * 
	 * @return BigDecimal
	 */
	public BigDecimal getProfitAfterTax() {
		return this.profitAfterTax;
	}

	/**
	 * 本期淨利(稅後)<br>
	 * 
	 *
	 * @param profitAfterTax 本期淨利(稅後)
	 */
	public void setProfitAfterTax(BigDecimal profitAfterTax) {
		this.profitAfterTax = profitAfterTax;
	}

	/**
	 * 流動比率<br>
	 * 
	 * @return BigDecimal
	 */
	public BigDecimal getWorkingCapitalRatio() {
		return this.workingCapitalRatio;
	}

	/**
	 * 流動比率<br>
	 * 
	 *
	 * @param workingCapitalRatio 流動比率
	 */
	public void setWorkingCapitalRatio(BigDecimal workingCapitalRatio) {
		this.workingCapitalRatio = workingCapitalRatio;
	}

	/**
	 * 利息保障倍數1<br>
	 * 
	 * @return BigDecimal
	 */
	public BigDecimal getInterestCoverageRatio1() {
		return this.interestCoverageRatio1;
	}

	/**
	 * 利息保障倍數1<br>
	 * 
	 *
	 * @param interestCoverageRatio1 利息保障倍數1
	 */
	public void setInterestCoverageRatio1(BigDecimal interestCoverageRatio1) {
		this.interestCoverageRatio1 = interestCoverageRatio1;
	}

	/**
	 * 利息保障倍數2<br>
	 * 
	 * @return BigDecimal
	 */
	public BigDecimal getInterestCoverageRatio2() {
		return this.interestCoverageRatio2;
	}

	/**
	 * 利息保障倍數2<br>
	 * 
	 *
	 * @param interestCoverageRatio2 利息保障倍數2
	 */
	public void setInterestCoverageRatio2(BigDecimal interestCoverageRatio2) {
		this.interestCoverageRatio2 = interestCoverageRatio2;
	}

	/**
	 * 槓桿比率<br>
	 * 
	 * @return BigDecimal
	 */
	public BigDecimal getLeverageRatio() {
		return this.leverageRatio;
	}

	/**
	 * 槓桿比率<br>
	 * 
	 *
	 * @param leverageRatio 槓桿比率
	 */
	public void setLeverageRatio(BigDecimal leverageRatio) {
		this.leverageRatio = leverageRatio;
	}

	/**
	 * 權益比率<br>
	 * 
	 * @return BigDecimal
	 */
	public BigDecimal getEquityRatio() {
		return this.equityRatio;
	}

	/**
	 * 權益比率<br>
	 * 
	 *
	 * @param equityRatio 權益比率
	 */
	public void setEquityRatio(BigDecimal equityRatio) {
		this.equityRatio = equityRatio;
	}

	/**
	 * 固定長期適合率<br>
	 * 
	 * @return BigDecimal
	 */
	public BigDecimal getLongFitRatio() {
		return this.longFitRatio;
	}

	/**
	 * 固定長期適合率<br>
	 * 
	 *
	 * @param longFitRatio 固定長期適合率
	 */
	public void setLongFitRatio(BigDecimal longFitRatio) {
		this.longFitRatio = longFitRatio;
	}

	/**
	 * 純益率(稅後)<br>
	 * 
	 * @return BigDecimal
	 */
	public BigDecimal getNetProfitRatio() {
		return this.netProfitRatio;
	}

	/**
	 * 純益率(稅後)<br>
	 * 
	 *
	 * @param netProfitRatio 純益率(稅後)
	 */
	public void setNetProfitRatio(BigDecimal netProfitRatio) {
		this.netProfitRatio = netProfitRatio;
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
		return "FinReportReview [finReportReviewId=" + finReportReviewId + ", currentAsset=" + currentAsset + ", totalAsset=" + totalAsset + ", propertyAsset=" + propertyAsset + ", investment="
				+ investment + ", investmentProperty=" + investmentProperty + ", depreciation=" + depreciation + ", currentDebt=" + currentDebt + ", totalDebt=" + totalDebt + ", totalEquity="
				+ totalEquity + ", bondsPayable=" + bondsPayable + ", longTermBorrowings=" + longTermBorrowings + ", nonCurrentLease=" + nonCurrentLease + ", longTermPayable=" + longTermPayable
				+ ", preference=" + preference + ", operatingRevenue=" + operatingRevenue + ", interestExpense=" + interestExpense + ", profitBeforeTax=" + profitBeforeTax + ", profitAfterTax="
				+ profitAfterTax + ", workingCapitalRatio=" + workingCapitalRatio + ", interestCoverageRatio1=" + interestCoverageRatio1 + ", interestCoverageRatio2=" + interestCoverageRatio2
				+ ", leverageRatio=" + leverageRatio + ", equityRatio=" + equityRatio + ", longFitRatio=" + longFitRatio + ", netProfitRatio=" + netProfitRatio + ", createDate=" + createDate
				+ ", createEmpNo=" + createEmpNo + ", lastUpdate=" + lastUpdate + ", lastUpdateEmpNo=" + lastUpdateEmpNo + "]";
	}
}
