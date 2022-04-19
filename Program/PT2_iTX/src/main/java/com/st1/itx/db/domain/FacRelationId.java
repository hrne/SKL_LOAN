package com.st1.itx.db.domain;

import java.io.Serializable;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 * FacRelation 交易關係人檔<br>
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
@Embeddable
public class FacRelationId implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5072363664524499099L;

// 案件編號
	/* 徵審系統案號(eLoan案件編號) */
	@Column(name = "`CreditSysNo`")
	private int creditSysNo = 0;

	// 客戶識別碼
	@Column(name = "`CustUKey`", length = 32)
	private String custUKey = " ";

	public FacRelationId() {
	}

	public FacRelationId(int creditSysNo, String custUKey) {
		this.creditSysNo = creditSysNo;
		this.custUKey = custUKey;
	}

	/**
	 * 案件編號<br>
	 * 徵審系統案號(eLoan案件編號)
	 * 
	 * @return Integer
	 */
	public int getCreditSysNo() {
		return this.creditSysNo;
	}

	/**
	 * 案件編號<br>
	 * 徵審系統案號(eLoan案件編號)
	 *
	 * @param creditSysNo 案件編號
	 */
	public void setCreditSysNo(int creditSysNo) {
		this.creditSysNo = creditSysNo;
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

	@Override
	public int hashCode() {
		return Objects.hash(creditSysNo, custUKey);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null || getClass() != obj.getClass())
			return false;
		FacRelationId facRelationId = (FacRelationId) obj;
		return creditSysNo == facRelationId.creditSysNo && custUKey.equals(facRelationId.custUKey);
	}

	@Override
	public String toString() {
		return "FacRelationId [creditSysNo=" + creditSysNo + ", custUKey=" + custUKey + "]";
	}
}
