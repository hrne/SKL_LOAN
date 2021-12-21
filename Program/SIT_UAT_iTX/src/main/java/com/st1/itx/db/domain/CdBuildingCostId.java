package com.st1.itx.db.domain;

import java.io.Serializable;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 * CdBuildingCost 建築造價參考檔<br>
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
@Embeddable
public class CdBuildingCostId implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7113252494360690229L;

// 縣市代碼(地區別)
	/* ref CdCity.CityCode */
	@Column(name = "`CityCode`", length = 2)
	private String cityCode = " ";

	// 總樓層數(下限)
	@Column(name = "`FloorLowerLimit`")
	private int floorLowerLimit = 0;

	public CdBuildingCostId() {
	}

	public CdBuildingCostId(String cityCode, int floorLowerLimit) {
		this.cityCode = cityCode;
		this.floorLowerLimit = floorLowerLimit;
	}

	/**
	 * 縣市代碼(地區別)<br>
	 * ref CdCity.CityCode
	 * 
	 * @return String
	 */
	public String getCityCode() {
		return this.cityCode == null ? "" : this.cityCode;
	}

	/**
	 * 縣市代碼(地區別)<br>
	 * ref CdCity.CityCode
	 *
	 * @param cityCode 縣市代碼(地區別)
	 */
	public void setCityCode(String cityCode) {
		this.cityCode = cityCode;
	}

	/**
	 * 總樓層數(下限)<br>
	 * 
	 * @return Integer
	 */
	public int getFloorLowerLimit() {
		return this.floorLowerLimit;
	}

	/**
	 * 總樓層數(下限)<br>
	 * 
	 *
	 * @param floorLowerLimit 總樓層數(下限)
	 */
	public void setFloorLowerLimit(int floorLowerLimit) {
		this.floorLowerLimit = floorLowerLimit;
	}

	@Override
	public int hashCode() {
		return Objects.hash(cityCode, floorLowerLimit);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null || getClass() != obj.getClass())
			return false;
		CdBuildingCostId cdBuildingCostId = (CdBuildingCostId) obj;
		return cityCode.equals(cdBuildingCostId.cityCode) && floorLowerLimit == cdBuildingCostId.floorLowerLimit;
	}

	@Override
	public String toString() {
		return "CdBuildingCostId [cityCode=" + cityCode + ", floorLowerLimit=" + floorLowerLimit + "]";
	}
}
