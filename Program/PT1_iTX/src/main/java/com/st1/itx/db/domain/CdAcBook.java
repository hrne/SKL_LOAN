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
 * CdAcBook 帳冊別金額設定檔<br>
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "`CdAcBook`")
public class CdAcBook implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2507344434224192603L;

	@EmbeddedId
	private CdAcBookId cdAcBookId;

	// 帳冊別
	/* 共用代碼檔 000：全公司 */
	@Column(name = "`AcBookCode`", length = 3, insertable = false, updatable = false)
	private String acBookCode;

	// 區隔帳冊
	/* 共用代碼檔 201:利變年金帳冊 */
	@Column(name = "`AcSubBookCode`", length = 3, insertable = false, updatable = false)
	private String acSubBookCode;

	// 幣別
	@Column(name = "`CurrencyCode`", length = 3)
	private String currencyCode;

	// 放款目標金額
	@Column(name = "`TargetAmt`")
	private BigDecimal targetAmt = new BigDecimal("0");

	// 放款實際金額
	@Column(name = "`ActualAmt`")
	private BigDecimal actualAmt = new BigDecimal("0");

	// 分配順序
	@Column(name = "`AssignSeq`")
	private int assignSeq = 0;

	// 資金來源
	@Column(name = "`AcctSource`", length = 1)
	private String acctSource;

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

	public CdAcBookId getCdAcBookId() {
		return this.cdAcBookId;
	}

	public void setCdAcBookId(CdAcBookId cdAcBookId) {
		this.cdAcBookId = cdAcBookId;
	}

	/**
	 * 帳冊別<br>
	 * 共用代碼檔 000：全公司
	 * 
	 * @return String
	 */
	public String getAcBookCode() {
		return this.acBookCode == null ? "" : this.acBookCode;
	}

	/**
	 * 帳冊別<br>
	 * 共用代碼檔 000：全公司
	 *
	 * @param acBookCode 帳冊別
	 */
	public void setAcBookCode(String acBookCode) {
		this.acBookCode = acBookCode;
	}

	/**
	 * 區隔帳冊<br>
	 * 共用代碼檔 201:利變年金帳冊
	 * 
	 * @return String
	 */
	public String getAcSubBookCode() {
		return this.acSubBookCode == null ? "" : this.acSubBookCode;
	}

	/**
	 * 區隔帳冊<br>
	 * 共用代碼檔 201:利變年金帳冊
	 *
	 * @param acSubBookCode 區隔帳冊
	 */
	public void setAcSubBookCode(String acSubBookCode) {
		this.acSubBookCode = acSubBookCode;
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
	 * 放款目標金額<br>
	 * 
	 * @return BigDecimal
	 */
	public BigDecimal getTargetAmt() {
		return this.targetAmt;
	}

	/**
	 * 放款目標金額<br>
	 * 
	 *
	 * @param targetAmt 放款目標金額
	 */
	public void setTargetAmt(BigDecimal targetAmt) {
		this.targetAmt = targetAmt;
	}

	/**
	 * 放款實際金額<br>
	 * 
	 * @return BigDecimal
	 */
	public BigDecimal getActualAmt() {
		return this.actualAmt;
	}

	/**
	 * 放款實際金額<br>
	 * 
	 *
	 * @param actualAmt 放款實際金額
	 */
	public void setActualAmt(BigDecimal actualAmt) {
		this.actualAmt = actualAmt;
	}

	/**
	 * 分配順序<br>
	 * 
	 * @return Integer
	 */
	public int getAssignSeq() {
		return this.assignSeq;
	}

	/**
	 * 分配順序<br>
	 * 
	 *
	 * @param assignSeq 分配順序
	 */
	public void setAssignSeq(int assignSeq) {
		this.assignSeq = assignSeq;
	}

	/**
	 * 資金來源<br>
	 * 
	 * @return String
	 */
	public String getAcctSource() {
		return this.acctSource == null ? "" : this.acctSource;
	}

	/**
	 * 資金來源<br>
	 * 
	 *
	 * @param acctSource 資金來源
	 */
	public void setAcctSource(String acctSource) {
		this.acctSource = acctSource;
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
		return "CdAcBook [cdAcBookId=" + cdAcBookId + ", currencyCode=" + currencyCode + ", targetAmt=" + targetAmt + ", actualAmt=" + actualAmt + ", assignSeq=" + assignSeq + ", acctSource="
				+ acctSource + ", createDate=" + createDate + ", createEmpNo=" + createEmpNo + ", lastUpdate=" + lastUpdate + ", lastUpdateEmpNo=" + lastUpdateEmpNo + "]";
	}
}
