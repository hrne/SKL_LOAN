package com.st1.itx.db.domain;

import java.io.Serializable;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 * CdInsurer 保險公司資料檔<br>
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
@Embeddable
public class CdInsurerId implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2363147267072437890L;

// 公司種類
	/* 1:保險公司2:鑑定公司 */
	@Column(name = "`InsurerType`", length = 1)
	private String insurerType = " ";

	// 公司代號
	@Column(name = "`InsurerCode`", length = 2)
	private String insurerCode = " ";

	public CdInsurerId() {
	}

	public CdInsurerId(String insurerType, String insurerCode) {
		this.insurerType = insurerType;
		this.insurerCode = insurerCode;
	}

	/**
	 * 公司種類<br>
	 * 1:保險公司 2:鑑定公司
	 * 
	 * @return String
	 */
	public String getInsurerType() {
		return this.insurerType == null ? "" : this.insurerType;
	}

	/**
	 * 公司種類<br>
	 * 1:保險公司 2:鑑定公司
	 *
	 * @param insurerType 公司種類
	 */
	public void setInsurerType(String insurerType) {
		this.insurerType = insurerType;
	}

	/**
	 * 公司代號<br>
	 * 
	 * @return String
	 */
	public String getInsurerCode() {
		return this.insurerCode == null ? "" : this.insurerCode;
	}

	/**
	 * 公司代號<br>
	 * 
	 *
	 * @param insurerCode 公司代號
	 */
	public void setInsurerCode(String insurerCode) {
		this.insurerCode = insurerCode;
	}

	@Override
	public int hashCode() {
		return Objects.hash(insurerType, insurerCode);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null || getClass() != obj.getClass())
			return false;
		CdInsurerId cdInsurerId = (CdInsurerId) obj;
		return insurerType.equals(cdInsurerId.insurerType) && insurerCode.equals(cdInsurerId.insurerCode);
	}

	@Override
	public String toString() {
		return "CdInsurerId [insurerType=" + insurerType + ", insurerCode=" + insurerCode + "]";
	}
}
