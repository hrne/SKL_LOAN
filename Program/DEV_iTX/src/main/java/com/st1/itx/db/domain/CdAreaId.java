package com.st1.itx.db.domain;

import java.io.Serializable;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 * CdArea 縣市與鄉鎮區對照檔<br>
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
@Embeddable
public class CdAreaId implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1075882331782561181L;

// 縣市別代碼
	/* CdCity.CityCode */
	@Column(name = "`CityCode`", length = 2)
	private String cityCode = " ";

	// 鄉鎮區代碼
	@Column(name = "`AreaCode`", length = 2)
	private String areaCode = " ";

	public CdAreaId() {
	}

	public CdAreaId(String cityCode, String areaCode) {
		this.cityCode = cityCode;
		this.areaCode = areaCode;
	}

	/**
	 * 縣市別代碼<br>
	 * CdCity.CityCode
	 * 
	 * @return String
	 */
	public String getCityCode() {
		return this.cityCode == null ? "" : this.cityCode;
	}

	/**
	 * 縣市別代碼<br>
	 * CdCity.CityCode
	 *
	 * @param cityCode 縣市別代碼
	 */
	public void setCityCode(String cityCode) {
		this.cityCode = cityCode;
	}

	/**
	 * 鄉鎮區代碼<br>
	 * 
	 * @return String
	 */
	public String getAreaCode() {
		return this.areaCode == null ? "" : this.areaCode;
	}

	/**
	 * 鄉鎮區代碼<br>
	 * 
	 *
	 * @param areaCode 鄉鎮區代碼
	 */
	public void setAreaCode(String areaCode) {
		this.areaCode = areaCode;
	}

	@Override
	public int hashCode() {
		return Objects.hash(cityCode, areaCode);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null || getClass() != obj.getClass())
			return false;
		CdAreaId cdAreaId = (CdAreaId) obj;
		return cityCode.equals(cdAreaId.cityCode) && areaCode.equals(cdAreaId.areaCode);
	}

	@Override
	public String toString() {
		return "CdAreaId [cityCode=" + cityCode + ", areaCode=" + areaCode + "]";
	}
}
