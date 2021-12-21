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
 * BankAuthAct 銀扣授權帳號檔<br>
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "`BankAuthAct`")
public class BankAuthAct implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1627936528762818443L;

	@EmbeddedId
	private BankAuthActId bankAuthActId;

	// 戶號
	@Column(name = "`CustNo`", insertable = false, updatable = false)
	private int custNo = 0;

	// 額度
	/* 兩個額度共用同一扣款帳號則ACH授權記錄檔只有第一個額度送出授權，但授權帳號檔會寫兩筆 */
	@Column(name = "`FacmNo`", insertable = false, updatable = false)
	private int facmNo = 0;

	// 授權類別
	/* CdCode:AuthCode00.期款 (ACH)01.期款 (郵局)02.火險 (郵局) */
	@Column(name = "`AuthType`", length = 2, insertable = false, updatable = false)
	private String authType;

	// 扣款銀行
	@Column(name = "`RepayBank`", length = 3)
	private String repayBank;

	// 郵局存款別
	/* CdCode:PostDepCode存簿：P劃撥：G */
	@Column(name = "`PostDepCode`", length = 1)
	private String postDepCode;

	// 扣款帳號
	/* 變更扣款帳號時授權成功才會更新 */
	@Column(name = "`RepayAcct`", length = 14)
	private String repayAcct;

	// 狀態碼
	/*
	 * 空白:未授權0:授權成功 授權提回更新 1:停止使用 0:授權成功時維護；恢復=&amp;gt;維護回0:授權成功2.取消授權 授權提回更新
	 * 8.授權失敗9:已送出授權
	 */
	@Column(name = "`Status`", length = 1)
	private String status;

	// 每筆扣款限額
	/* ACH */
	@Column(name = "`LimitAmt`")
	private BigDecimal limitAmt = new BigDecimal("0");

	// 帳號碼
	/* 該戶號之第一個扣款帳號為空白，其後依序01起編(郵局用) */
	@Column(name = "`AcctSeq`", length = 2)
	private String acctSeq;

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

	public BankAuthActId getBankAuthActId() {
		return this.bankAuthActId;
	}

	public void setBankAuthActId(BankAuthActId bankAuthActId) {
		this.bankAuthActId = bankAuthActId;
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
	 * 額度<br>
	 * 兩個額度共用同一扣款帳號則ACH授權記錄檔只有第一個額度送出授權，但授權帳號檔會寫兩筆
	 * 
	 * @return Integer
	 */
	public int getFacmNo() {
		return this.facmNo;
	}

	/**
	 * 額度<br>
	 * 兩個額度共用同一扣款帳號則ACH授權記錄檔只有第一個額度送出授權，但授權帳號檔會寫兩筆
	 *
	 * @param facmNo 額度
	 */
	public void setFacmNo(int facmNo) {
		this.facmNo = facmNo;
	}

	/**
	 * 授權類別<br>
	 * CdCode:AuthCode 00.期款 (ACH) 01.期款 (郵局) 02.火險 (郵局)
	 * 
	 * @return String
	 */
	public String getAuthType() {
		return this.authType == null ? "" : this.authType;
	}

	/**
	 * 授權類別<br>
	 * CdCode:AuthCode 00.期款 (ACH) 01.期款 (郵局) 02.火險 (郵局)
	 *
	 * @param authType 授權類別
	 */
	public void setAuthType(String authType) {
		this.authType = authType;
	}

	/**
	 * 扣款銀行<br>
	 * 
	 * @return String
	 */
	public String getRepayBank() {
		return this.repayBank == null ? "" : this.repayBank;
	}

	/**
	 * 扣款銀行<br>
	 * 
	 *
	 * @param repayBank 扣款銀行
	 */
	public void setRepayBank(String repayBank) {
		this.repayBank = repayBank;
	}

	/**
	 * 郵局存款別<br>
	 * CdCode:PostDepCode 存簿：P劃撥：G
	 * 
	 * @return String
	 */
	public String getPostDepCode() {
		return this.postDepCode == null ? "" : this.postDepCode;
	}

	/**
	 * 郵局存款別<br>
	 * CdCode:PostDepCode 存簿：P劃撥：G
	 *
	 * @param postDepCode 郵局存款別
	 */
	public void setPostDepCode(String postDepCode) {
		this.postDepCode = postDepCode;
	}

	/**
	 * 扣款帳號<br>
	 * 變更扣款帳號時授權成功才會更新
	 * 
	 * @return String
	 */
	public String getRepayAcct() {
		return this.repayAcct == null ? "" : this.repayAcct;
	}

	/**
	 * 扣款帳號<br>
	 * 變更扣款帳號時授權成功才會更新
	 *
	 * @param repayAcct 扣款帳號
	 */
	public void setRepayAcct(String repayAcct) {
		this.repayAcct = repayAcct;
	}

	/**
	 * 狀態碼<br>
	 * 空白:未授權 0:授權成功 授權提回更新 1:停止使用 0:授權成功時維護；恢復=&amp;gt;維護回0:授權成功 2.取消授權 授權提回更新
	 * 8.授權失敗 9:已送出授權
	 * 
	 * @return String
	 */
	public String getStatus() {
		return this.status == null ? "" : this.status;
	}

	/**
	 * 狀態碼<br>
	 * 空白:未授權 0:授權成功 授權提回更新 1:停止使用 0:授權成功時維護；恢復=&amp;gt;維護回0:授權成功 2.取消授權 授權提回更新
	 * 8.授權失敗 9:已送出授權
	 *
	 * @param status 狀態碼
	 */
	public void setStatus(String status) {
		this.status = status;
	}

	/**
	 * 每筆扣款限額<br>
	 * ACH
	 * 
	 * @return BigDecimal
	 */
	public BigDecimal getLimitAmt() {
		return this.limitAmt;
	}

	/**
	 * 每筆扣款限額<br>
	 * ACH
	 *
	 * @param limitAmt 每筆扣款限額
	 */
	public void setLimitAmt(BigDecimal limitAmt) {
		this.limitAmt = limitAmt;
	}

	/**
	 * 帳號碼<br>
	 * 該戶號之第一個扣款帳號為空白，其後依序01起編(郵局用)
	 * 
	 * @return String
	 */
	public String getAcctSeq() {
		return this.acctSeq == null ? "" : this.acctSeq;
	}

	/**
	 * 帳號碼<br>
	 * 該戶號之第一個扣款帳號為空白，其後依序01起編(郵局用)
	 *
	 * @param acctSeq 帳號碼
	 */
	public void setAcctSeq(String acctSeq) {
		this.acctSeq = acctSeq;
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
		return "BankAuthAct [bankAuthActId=" + bankAuthActId + ", repayBank=" + repayBank + ", postDepCode=" + postDepCode + ", repayAcct=" + repayAcct + ", status=" + status + ", limitAmt="
				+ limitAmt + ", acctSeq=" + acctSeq + ", createDate=" + createDate + ", createEmpNo=" + createEmpNo + ", lastUpdate=" + lastUpdate + ", lastUpdateEmpNo=" + lastUpdateEmpNo + "]";
	}
}
