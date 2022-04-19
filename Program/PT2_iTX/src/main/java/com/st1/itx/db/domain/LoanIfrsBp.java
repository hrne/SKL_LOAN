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
 * LoanIfrsBp IFRS9欄位清單2<br>
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "`LoanIfrsBp`")
public class LoanIfrsBp implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1520779346876122278L;

	@EmbeddedId
	private LoanIfrsBpId loanIfrsBpId;

	// 年月份
	@Column(name = "`DataYM`", insertable = false, updatable = false)
	private int dataYM = 0;

	// 戶號
	@Column(name = "`CustNo`", insertable = false, updatable = false)
	private int custNo = 0;

	// 借款人ID / 統編
	@Column(name = "`CustId`", length = 10)
	private String custId;

	// 額度編號
	@Column(name = "`FacmNo`", insertable = false, updatable = false)
	private int facmNo = 0;

	// 撥款序號
	@Column(name = "`BormNo`", insertable = false, updatable = false)
	private int bormNo = 0;

	// 貸放利率
	/*
	 * 至小數點後第6位。例如，利率為2.12%，則本欄位值表示0.021200ex:五月底給資料，則機動/階梯型利率需拋出契約約定之多段加碼幅度後利率(
	 * 浮動利率i以現在的i代入)；但固定則需六月之後的利率都要給(用以估算未來的現金流量)浮動階梯以尚未生效的利率加碼值，算出利率後拋出
	 */
	@Column(name = "`LoanRate`")
	private BigDecimal loanRate = new BigDecimal("0");

	// 利率調整方式
	/* 1=機動；2=固定；3=固定階梯；4=浮動階梯； */
	@Column(name = "`RateCode`")
	private int rateCode = 0;

	// 利率欄位生效日
	/* YYYYMMDD 例：20100108 */
	@Column(name = "`EffectDate`", insertable = false, updatable = false)
	private int effectDate = 0;

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

	public LoanIfrsBpId getLoanIfrsBpId() {
		return this.loanIfrsBpId;
	}

	public void setLoanIfrsBpId(LoanIfrsBpId loanIfrsBpId) {
		this.loanIfrsBpId = loanIfrsBpId;
	}

	/**
	 * 年月份<br>
	 * 
	 * @return Integer
	 */
	public int getDataYM() {
		return this.dataYM;
	}

	/**
	 * 年月份<br>
	 * 
	 *
	 * @param dataYM 年月份
	 */
	public void setDataYM(int dataYM) {
		this.dataYM = dataYM;
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
	 * 借款人ID / 統編<br>
	 * 
	 * @return String
	 */
	public String getCustId() {
		return this.custId == null ? "" : this.custId;
	}

	/**
	 * 借款人ID / 統編<br>
	 * 
	 *
	 * @param custId 借款人ID / 統編
	 */
	public void setCustId(String custId) {
		this.custId = custId;
	}

	/**
	 * 額度編號<br>
	 * 
	 * @return Integer
	 */
	public int getFacmNo() {
		return this.facmNo;
	}

	/**
	 * 額度編號<br>
	 * 
	 *
	 * @param facmNo 額度編號
	 */
	public void setFacmNo(int facmNo) {
		this.facmNo = facmNo;
	}

	/**
	 * 撥款序號<br>
	 * 
	 * @return Integer
	 */
	public int getBormNo() {
		return this.bormNo;
	}

	/**
	 * 撥款序號<br>
	 * 
	 *
	 * @param bormNo 撥款序號
	 */
	public void setBormNo(int bormNo) {
		this.bormNo = bormNo;
	}

	/**
	 * 貸放利率<br>
	 * 至小數點後第6位。例如，利率為2.12%，則本欄位值表示0.021200
	 * ex:五月底給資料，則機動/階梯型利率需拋出契約約定之多段加碼幅度後利率(浮動利率i以現在的i代入)；但固定則需六月之後的利率都要給(用以估算未來的現金流量)
	 * 浮動階梯以尚未生效的利率加碼值，算出利率後拋出
	 * 
	 * @return BigDecimal
	 */
	public BigDecimal getLoanRate() {
		return this.loanRate;
	}

	/**
	 * 貸放利率<br>
	 * 至小數點後第6位。例如，利率為2.12%，則本欄位值表示0.021200
	 * ex:五月底給資料，則機動/階梯型利率需拋出契約約定之多段加碼幅度後利率(浮動利率i以現在的i代入)；但固定則需六月之後的利率都要給(用以估算未來的現金流量)
	 * 浮動階梯以尚未生效的利率加碼值，算出利率後拋出
	 *
	 * @param loanRate 貸放利率
	 */
	public void setLoanRate(BigDecimal loanRate) {
		this.loanRate = loanRate;
	}

	/**
	 * 利率調整方式<br>
	 * 1=機動； 2=固定； 3=固定階梯； 4=浮動階梯；
	 * 
	 * @return Integer
	 */
	public int getRateCode() {
		return this.rateCode;
	}

	/**
	 * 利率調整方式<br>
	 * 1=機動； 2=固定； 3=固定階梯； 4=浮動階梯；
	 *
	 * @param rateCode 利率調整方式
	 */
	public void setRateCode(int rateCode) {
		this.rateCode = rateCode;
	}

	/**
	 * 利率欄位生效日<br>
	 * YYYYMMDD 例：20100108
	 * 
	 * @return Integer
	 */
	public int getEffectDate() {
		return this.effectDate;
	}

	/**
	 * 利率欄位生效日<br>
	 * YYYYMMDD 例：20100108
	 *
	 * @param effectDate 利率欄位生效日
	 */
	public void setEffectDate(int effectDate) {
		this.effectDate = effectDate;
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
		return "LoanIfrsBp [loanIfrsBpId=" + loanIfrsBpId + ", custId=" + custId + ", loanRate=" + loanRate + ", rateCode=" + rateCode + ", createDate=" + createDate + ", createEmpNo=" + createEmpNo
				+ ", lastUpdate=" + lastUpdate + ", lastUpdateEmpNo=" + lastUpdateEmpNo + "]";
	}
}
