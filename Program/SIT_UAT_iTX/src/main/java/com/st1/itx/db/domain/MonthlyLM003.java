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
 * MonthlyLM003 撥款還款金額比較月報工作檔<br>
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "`MonthlyLM003`")
public class MonthlyLM003 implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1887220267902957327L;

	@EmbeddedId
	private MonthlyLM003Id monthlyLM003Id;

	// 企金別
	/* 0.個金,1.企金 */
	@Column(name = "`EntType`", insertable = false, updatable = false)
	private int entType = 0;

	// 資料年份
	@Column(name = "`DataYear`", insertable = false, updatable = false)
	private int dataYear = 0;

	// 資料月份
	@Column(name = "`DataMonth`", insertable = false, updatable = false)
	private int dataMonth = 0;

	// 撥款金額
	@Column(name = "`DrawdownAmt`")
	private BigDecimal drawdownAmt = new BigDecimal("0");

	// 結清-利率高轉貸
	@Column(name = "`CloseLoan`")
	private BigDecimal closeLoan = new BigDecimal("0");

	// 結清-買賣
	@Column(name = "`CloseSale`")
	private BigDecimal closeSale = new BigDecimal("0");

	// 結清-自行還款
	@Column(name = "`CloseSelfRepay`")
	private BigDecimal closeSelfRepay = new BigDecimal("0");

	// 非結清-部份還款
	@Column(name = "`ExtraRepay`")
	private BigDecimal extraRepay = new BigDecimal("0");

	// 非結清-本金攤提
	@Column(name = "`PrincipalAmortize`")
	private BigDecimal principalAmortize = new BigDecimal("0");

	// 非結清-轉催收
	@Column(name = "`Collection`")
	private BigDecimal collection = new BigDecimal("0");

	// 月底餘額
	@Column(name = "`LoanBalance`")
	private BigDecimal loanBalance = new BigDecimal("0");

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

	public MonthlyLM003Id getMonthlyLM003Id() {
		return this.monthlyLM003Id;
	}

	public void setMonthlyLM003Id(MonthlyLM003Id monthlyLM003Id) {
		this.monthlyLM003Id = monthlyLM003Id;
	}

	/**
	 * 企金別<br>
	 * 0.個金,1.企金
	 * 
	 * @return Integer
	 */
	public int getEntType() {
		return this.entType;
	}

	/**
	 * 企金別<br>
	 * 0.個金,1.企金
	 *
	 * @param entType 企金別
	 */
	public void setEntType(int entType) {
		this.entType = entType;
	}

	/**
	 * 資料年份<br>
	 * 
	 * @return Integer
	 */
	public int getDataYear() {
		return this.dataYear;
	}

	/**
	 * 資料年份<br>
	 * 
	 *
	 * @param dataYear 資料年份
	 */
	public void setDataYear(int dataYear) {
		this.dataYear = dataYear;
	}

	/**
	 * 資料月份<br>
	 * 
	 * @return Integer
	 */
	public int getDataMonth() {
		return this.dataMonth;
	}

	/**
	 * 資料月份<br>
	 * 
	 *
	 * @param dataMonth 資料月份
	 */
	public void setDataMonth(int dataMonth) {
		this.dataMonth = dataMonth;
	}

	/**
	 * 撥款金額<br>
	 * 
	 * @return BigDecimal
	 */
	public BigDecimal getDrawdownAmt() {
		return this.drawdownAmt;
	}

	/**
	 * 撥款金額<br>
	 * 
	 *
	 * @param drawdownAmt 撥款金額
	 */
	public void setDrawdownAmt(BigDecimal drawdownAmt) {
		this.drawdownAmt = drawdownAmt;
	}

	/**
	 * 結清-利率高轉貸<br>
	 * 
	 * @return BigDecimal
	 */
	public BigDecimal getCloseLoan() {
		return this.closeLoan;
	}

	/**
	 * 結清-利率高轉貸<br>
	 * 
	 *
	 * @param closeLoan 結清-利率高轉貸
	 */
	public void setCloseLoan(BigDecimal closeLoan) {
		this.closeLoan = closeLoan;
	}

	/**
	 * 結清-買賣<br>
	 * 
	 * @return BigDecimal
	 */
	public BigDecimal getCloseSale() {
		return this.closeSale;
	}

	/**
	 * 結清-買賣<br>
	 * 
	 *
	 * @param closeSale 結清-買賣
	 */
	public void setCloseSale(BigDecimal closeSale) {
		this.closeSale = closeSale;
	}

	/**
	 * 結清-自行還款<br>
	 * 
	 * @return BigDecimal
	 */
	public BigDecimal getCloseSelfRepay() {
		return this.closeSelfRepay;
	}

	/**
	 * 結清-自行還款<br>
	 * 
	 *
	 * @param closeSelfRepay 結清-自行還款
	 */
	public void setCloseSelfRepay(BigDecimal closeSelfRepay) {
		this.closeSelfRepay = closeSelfRepay;
	}

	/**
	 * 非結清-部份還款<br>
	 * 
	 * @return BigDecimal
	 */
	public BigDecimal getExtraRepay() {
		return this.extraRepay;
	}

	/**
	 * 非結清-部份還款<br>
	 * 
	 *
	 * @param extraRepay 非結清-部份還款
	 */
	public void setExtraRepay(BigDecimal extraRepay) {
		this.extraRepay = extraRepay;
	}

	/**
	 * 非結清-本金攤提<br>
	 * 
	 * @return BigDecimal
	 */
	public BigDecimal getPrincipalAmortize() {
		return this.principalAmortize;
	}

	/**
	 * 非結清-本金攤提<br>
	 * 
	 *
	 * @param principalAmortize 非結清-本金攤提
	 */
	public void setPrincipalAmortize(BigDecimal principalAmortize) {
		this.principalAmortize = principalAmortize;
	}

	/**
	 * 非結清-轉催收<br>
	 * 
	 * @return BigDecimal
	 */
	public BigDecimal getCollection() {
		return this.collection;
	}

	/**
	 * 非結清-轉催收<br>
	 * 
	 *
	 * @param collection 非結清-轉催收
	 */
	public void setCollection(BigDecimal collection) {
		this.collection = collection;
	}

	/**
	 * 月底餘額<br>
	 * 
	 * @return BigDecimal
	 */
	public BigDecimal getLoanBalance() {
		return this.loanBalance;
	}

	/**
	 * 月底餘額<br>
	 * 
	 *
	 * @param loanBalance 月底餘額
	 */
	public void setLoanBalance(BigDecimal loanBalance) {
		this.loanBalance = loanBalance;
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
		return "MonthlyLM003 [monthlyLM003Id=" + monthlyLM003Id + ", drawdownAmt=" + drawdownAmt + ", closeLoan=" + closeLoan + ", closeSale=" + closeSale + ", closeSelfRepay=" + closeSelfRepay
				+ ", extraRepay=" + extraRepay + ", principalAmortize=" + principalAmortize + ", collection=" + collection + ", loanBalance=" + loanBalance + ", createDate=" + createDate
				+ ", createEmpNo=" + createEmpNo + ", lastUpdate=" + lastUpdate + ", lastUpdateEmpNo=" + lastUpdateEmpNo + "]";
	}
}
