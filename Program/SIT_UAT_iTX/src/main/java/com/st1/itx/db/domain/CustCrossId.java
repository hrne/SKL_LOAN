package com.st1.itx.db.domain;

import java.io.Serializable;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 * CustCross 客戶交互運用檔<br>
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
@Embeddable
public class CustCrossId implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5363004919117593805L;

// 客戶識別碼
	@Column(name = "`CustUKey`", length = 32)
	private String custUKey = " ";

	// 子公司代碼
	/* 共用代碼檔01: 新光金控02: 新光人壽03: 新光銀行04: 新光信託05: 保險經紀人06: 元富證券 */
	@Column(name = "`SubCompanyCode`", length = 2)
	private String subCompanyCode = " ";

	public CustCrossId() {
	}

	public CustCrossId(String custUKey, String subCompanyCode) {
		this.custUKey = custUKey;
		this.subCompanyCode = subCompanyCode;
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
	 * 子公司代碼<br>
	 * 共用代碼檔 01: 新光金控 02: 新光人壽 03: 新光銀行 04: 新光信託 05: 保險經紀人 06: 元富證券
	 * 
	 * @return String
	 */
	public String getSubCompanyCode() {
		return this.subCompanyCode == null ? "" : this.subCompanyCode;
	}

	/**
	 * 子公司代碼<br>
	 * 共用代碼檔 01: 新光金控 02: 新光人壽 03: 新光銀行 04: 新光信託 05: 保險經紀人 06: 元富證券
	 *
	 * @param subCompanyCode 子公司代碼
	 */
	public void setSubCompanyCode(String subCompanyCode) {
		this.subCompanyCode = subCompanyCode;
	}

	@Override
	public int hashCode() {
		return Objects.hash(custUKey, subCompanyCode);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null || getClass() != obj.getClass())
			return false;
		CustCrossId custCrossId = (CustCrossId) obj;
		return custUKey.equals(custCrossId.custUKey) && subCompanyCode.equals(custCrossId.subCompanyCode);
	}

	@Override
	public String toString() {
		return "CustCrossId [custUKey=" + custUKey + ", subCompanyCode=" + subCompanyCode + "]";
	}
}
