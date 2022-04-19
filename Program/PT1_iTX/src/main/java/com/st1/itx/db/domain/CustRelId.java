package com.st1.itx.db.domain;

import java.io.Serializable;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 * CustRel 客戶關聯檔<br>
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
@Embeddable
public class CustRelId implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -86560326660058150L;

// 客戶識別碼
	@Column(name = "`CustUKey`", length = 32)
	private String custUKey = " ";

	// 關聯戶識別碼
	@Column(name = "`RelUKey`", length = 32)
	private String relUKey = " ";

	public CustRelId() {
	}

	public CustRelId(String custUKey, String relUKey) {
		this.custUKey = custUKey;
		this.relUKey = relUKey;
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
	 * 關聯戶識別碼<br>
	 * 
	 * @return String
	 */
	public String getRelUKey() {
		return this.relUKey == null ? "" : this.relUKey;
	}

	/**
	 * 關聯戶識別碼<br>
	 * 
	 *
	 * @param relUKey 關聯戶識別碼
	 */
	public void setRelUKey(String relUKey) {
		this.relUKey = relUKey;
	}

	@Override
	public int hashCode() {
		return Objects.hash(custUKey, relUKey);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null || getClass() != obj.getClass())
			return false;
		CustRelId custRelId = (CustRelId) obj;
		return custUKey.equals(custRelId.custUKey) && relUKey.equals(custRelId.relUKey);
	}

	@Override
	public String toString() {
		return "CustRelId [custUKey=" + custUKey + ", relUKey=" + relUKey + "]";
	}
}
