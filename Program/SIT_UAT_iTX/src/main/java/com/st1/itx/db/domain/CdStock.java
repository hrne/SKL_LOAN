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
 * CdStock 股票代號檔<br>
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "`CdStock`")
public class CdStock implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5332886958434659955L;

// 股票代號
	@Id
	@Column(name = "`StockCode`", length = 10)
	private String stockCode = " ";

	// 股票簡稱
	@Column(name = "`StockItem`", length = 20)
	private String stockItem;

	// 股票公司名稱
	@Column(name = "`StockCompanyName`", length = 50)
	private String stockCompanyName;

	// 幣別
	/* CdCode.Currency */
	@Column(name = "`Currency`", length = 3)
	private String currency;

	// 前日收盤價
	@Column(name = "`YdClosePrice`")
	private BigDecimal ydClosePrice = new BigDecimal("0");

	// 一個月平均價
	@Column(name = "`MonthlyAvg`")
	private BigDecimal monthlyAvg = new BigDecimal("0");

	// 三個月平均價
	@Column(name = "`ThreeMonthAvg`")
	private BigDecimal threeMonthAvg = new BigDecimal("0");

	// 上市上櫃記號
	/* 1:上市2:上櫃 */
	@Column(name = "`StockType`")
	private int stockType = 0;

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
	 * 股票代號<br>
	 * 
	 * @return String
	 */
	public String getStockCode() {
		return this.stockCode == null ? "" : this.stockCode;
	}

	/**
	 * 股票代號<br>
	 * 
	 *
	 * @param stockCode 股票代號
	 */
	public void setStockCode(String stockCode) {
		this.stockCode = stockCode;
	}

	/**
	 * 股票簡稱<br>
	 * 
	 * @return String
	 */
	public String getStockItem() {
		return this.stockItem == null ? "" : this.stockItem;
	}

	/**
	 * 股票簡稱<br>
	 * 
	 *
	 * @param stockItem 股票簡稱
	 */
	public void setStockItem(String stockItem) {
		this.stockItem = stockItem;
	}

	/**
	 * 股票公司名稱<br>
	 * 
	 * @return String
	 */
	public String getStockCompanyName() {
		return this.stockCompanyName == null ? "" : this.stockCompanyName;
	}

	/**
	 * 股票公司名稱<br>
	 * 
	 *
	 * @param stockCompanyName 股票公司名稱
	 */
	public void setStockCompanyName(String stockCompanyName) {
		this.stockCompanyName = stockCompanyName;
	}

	/**
	 * 幣別<br>
	 * CdCode.Currency
	 * 
	 * @return String
	 */
	public String getCurrency() {
		return this.currency == null ? "" : this.currency;
	}

	/**
	 * 幣別<br>
	 * CdCode.Currency
	 *
	 * @param currency 幣別
	 */
	public void setCurrency(String currency) {
		this.currency = currency;
	}

	/**
	 * 前日收盤價<br>
	 * 
	 * @return BigDecimal
	 */
	public BigDecimal getYdClosePrice() {
		return this.ydClosePrice;
	}

	/**
	 * 前日收盤價<br>
	 * 
	 *
	 * @param ydClosePrice 前日收盤價
	 */
	public void setYdClosePrice(BigDecimal ydClosePrice) {
		this.ydClosePrice = ydClosePrice;
	}

	/**
	 * 一個月平均價<br>
	 * 
	 * @return BigDecimal
	 */
	public BigDecimal getMonthlyAvg() {
		return this.monthlyAvg;
	}

	/**
	 * 一個月平均價<br>
	 * 
	 *
	 * @param monthlyAvg 一個月平均價
	 */
	public void setMonthlyAvg(BigDecimal monthlyAvg) {
		this.monthlyAvg = monthlyAvg;
	}

	/**
	 * 三個月平均價<br>
	 * 
	 * @return BigDecimal
	 */
	public BigDecimal getThreeMonthAvg() {
		return this.threeMonthAvg;
	}

	/**
	 * 三個月平均價<br>
	 * 
	 *
	 * @param threeMonthAvg 三個月平均價
	 */
	public void setThreeMonthAvg(BigDecimal threeMonthAvg) {
		this.threeMonthAvg = threeMonthAvg;
	}

	/**
	 * 上市上櫃記號<br>
	 * 1:上市 2:上櫃
	 * 
	 * @return Integer
	 */
	public int getStockType() {
		return this.stockType;
	}

	/**
	 * 上市上櫃記號<br>
	 * 1:上市 2:上櫃
	 *
	 * @param stockType 上市上櫃記號
	 */
	public void setStockType(int stockType) {
		this.stockType = stockType;
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
		return "CdStock [stockCode=" + stockCode + ", stockItem=" + stockItem + ", stockCompanyName=" + stockCompanyName + ", currency=" + currency + ", ydClosePrice=" + ydClosePrice + ", monthlyAvg="
				+ monthlyAvg + ", threeMonthAvg=" + threeMonthAvg + ", stockType=" + stockType + ", createDate=" + createDate + ", createEmpNo=" + createEmpNo + ", lastUpdate=" + lastUpdate
				+ ", lastUpdateEmpNo=" + lastUpdateEmpNo + "]";
	}
}
