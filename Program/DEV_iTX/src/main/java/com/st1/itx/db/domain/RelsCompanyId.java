package com.st1.itx.db.domain;

import java.io.Serializable;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 * RelsCompany (準)利害關係人相關事業檔<br>
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
@Embeddable
public class RelsCompanyId implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5118026670649861846L;

	// (準)利害關係人識別碼
	@Column(name = "`RelsUKey`", length = 32)
	private String relsUKey = " ";

	// 相關事業統一編號
	@Column(name = "`CompanyId`", length = 10)
	private String companyId = " ";

	public RelsCompanyId() {
	}

	public RelsCompanyId(String relsUKey, String companyId) {
		this.relsUKey = relsUKey;
		this.companyId = companyId;
	}

	/**
	 * (準)利害關係人識別碼<br>
	 * 
	 * @return String
	 */
	public String getRelsUKey() {
		return this.relsUKey == null ? "" : this.relsUKey;
	}

	/**
	 * (準)利害關係人識別碼<br>
	 * 
	 *
	 * @param relsUKey (準)利害關係人識別碼
	 */
	public void setRelsUKey(String relsUKey) {
		this.relsUKey = relsUKey;
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
		return Objects.hash(relsUKey, companyId);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null || getClass() != obj.getClass())
			return false;
		RelsCompanyId relsCompanyId = (RelsCompanyId) obj;
		return relsUKey.equals(relsCompanyId.relsUKey) && companyId.equals(relsCompanyId.companyId);
	}

	@Override
	public String toString() {
		return "RelsCompanyId [relsUKey=" + relsUKey + ", companyId=" + companyId + "]";
	}
}
