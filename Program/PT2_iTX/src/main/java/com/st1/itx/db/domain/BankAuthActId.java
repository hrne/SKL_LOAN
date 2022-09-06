package com.st1.itx.db.domain;

import java.io.Serializable;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 * BankAuthAct 銀扣授權帳號檔<br>
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
@Embeddable
public class BankAuthActId implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7009368685342367775L;

// 戶號
	@Column(name = "`CustNo`")
	private int custNo = 0;

	// 額度
	/* 兩個額度共用同一扣款帳號則ACH授權記錄檔只有第一個額度送出授權，但授權帳號檔會寫兩筆 */
	@Column(name = "`FacmNo`")
	private int facmNo = 0;

	// 授權類別
	/* CdCode.AuthCode00:期款 (ACH)01:期款 (郵局)02:火險 (郵局) */
	@Column(name = "`AuthType`", length = 2)
	private String authType = " ";

	public BankAuthActId() {
	}

	public BankAuthActId(int custNo, int facmNo, String authType) {
		this.custNo = custNo;
		this.facmNo = facmNo;
		this.authType = authType;
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
	 * CdCode.AuthCode 00:期款 (ACH) 01:期款 (郵局) 02:火險 (郵局)
	 * 
	 * @return String
	 */
	public String getAuthType() {
		return this.authType == null ? "" : this.authType;
	}

	/**
	 * 授權類別<br>
	 * CdCode.AuthCode 00:期款 (ACH) 01:期款 (郵局) 02:火險 (郵局)
	 *
	 * @param authType 授權類別
	 */
	public void setAuthType(String authType) {
		this.authType = authType;
	}

	@Override
	public int hashCode() {
		return Objects.hash(custNo, facmNo, authType);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null || getClass() != obj.getClass())
			return false;
		BankAuthActId bankAuthActId = (BankAuthActId) obj;
		return custNo == bankAuthActId.custNo && facmNo == bankAuthActId.facmNo && authType.equals(bankAuthActId.authType);
	}

	@Override
	public String toString() {
		return "BankAuthActId [custNo=" + custNo + ", facmNo=" + facmNo + ", authType=" + authType + "]";
	}
}
