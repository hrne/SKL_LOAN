package com.st1.itx.db.domain;

import java.io.Serializable;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 * ReltCompany 利害關係人相關事業檔<br>
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
@Embeddable
public class ReltCompanyId implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5555084170661667559L;

	// 利害關係人識別碼
	@Column(name = "`ReltUKey`", length = 32)
	private String reltUKey = " ";

	// 相關事業統一編號
	@Column(name = "`CompanyId`", length = 10)
	private String companyId = " ";

	public ReltCompanyId() {
	}

	public ReltCompanyId(String reltUKey, String companyId) {
		this.reltUKey = reltUKey;
		this.companyId = companyId;
	}

	/**
	 * 利害關係人識別碼<br>
	 * 
	 * @return String
	 */
	public String getReltUKey() {
		return this.reltUKey == null ? "" : this.reltUKey;
	}

	/**
	 * 利害關係人識別碼<br>
	 * 
	 *
	 * @param reltUKey 利害關係人識別碼
	 */
	public void setReltUKey(String reltUKey) {
		this.reltUKey = reltUKey;
	}

	/**
	 * 相關事業統一編號<br>
	 * 
	 * @return String
	 */
	public String getCompanyId() {
		return this.companyId == null ? "" : this.companyId;
	}

	/**
	 * 相關事業統一編號<br>
	 * 
	 *
	 * @param companyId 相關事業統一編號
	 */
	public void setCompanyId(String companyId) {
		this.companyId = companyId;
	}

	@Override
	public int hashCode() {
		return Objects.hash(reltUKey, companyId);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null || getClass() != obj.getClass())
			return false;
		ReltCompanyId reltCompanyId = (ReltCompanyId) obj;
		return reltUKey.equals(reltCompanyId.reltUKey) && companyId.equals(reltCompanyId.companyId);
	}

	@Override
	public String toString() {
		return "ReltCompanyId [reltUKey=" + reltUKey + ", companyId=" + companyId + "]";
	}
}
