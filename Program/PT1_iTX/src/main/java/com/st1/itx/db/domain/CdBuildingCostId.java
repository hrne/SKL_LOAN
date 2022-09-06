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
	private static final long serialVersionUID = -4452347704212735347L;

// 縣市代碼(地區別)
	/* ref CdCity.CityCode */
	@Column(name = "`CityCode`", length = 2)
	private String cityCode = " ";

	// 建物材料
	/* 1.磚、木、石、金屬構造2.加強磚造、鋼筋混凝土造3.鋼骨鋼筋混凝土造 */
	@Column(name = "`Material`")
	private int material = 0;

	// 總樓層數(下限)
	@Column(name = "`FloorLowerLimit`")
	private int floorLowerLimit = 0;

	// 版本日期
	/* xiangwei 20220302 新增欄位 */
	@Column(name = "`VersionDate`")
	private int versionDate = 0;

	public CdBuildingCostId() {
	}

	public CdBuildingCostId(String cityCode, int material, int floorLowerLimit, int versionDate) {
		this.cityCode = cityCode;
		this.material = material;
		this.floorLowerLimit = floorLowerLimit;
		this.versionDate = versionDate;
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
	 * 建物材料<br>
	 * 1.磚、木、石、金屬構造 2.加強磚造、鋼筋混凝土造 3.鋼骨鋼筋混凝土造
	 * 
	 * @return Integer
	 */
	public int getMaterial() {
		return this.material;
	}

	/**
	 * 建物材料<br>
	 * 1.磚、木、石、金屬構造 2.加強磚造、鋼筋混凝土造 3.鋼骨鋼筋混凝土造
	 *
	 * @param material 建物材料
	 */
	public void setMaterial(int material) {
		this.material = material;
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

	/**
	 * 版本日期<br>
	 * xiangwei 20220302 新增欄位
	 * 
	 * @return Integer
	 */
	public int getVersionDate() {
		return this.versionDate;
	}

	/**
	 * 版本日期<br>
	 * xiangwei 20220302 新增欄位
	 *
	 * @param versionDate 版本日期
	 */
	public void setVersionDate(int versionDate) {
		this.versionDate = versionDate;
	}

	@Override
	public int hashCode() {
		return Objects.hash(cityCode, material, floorLowerLimit, versionDate);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null || getClass() != obj.getClass())
			return false;
		CdBuildingCostId cdBuildingCostId = (CdBuildingCostId) obj;
		return cityCode.equals(cdBuildingCostId.cityCode) && material == cdBuildingCostId.material && floorLowerLimit == cdBuildingCostId.floorLowerLimit
				&& versionDate == cdBuildingCostId.versionDate;
	}

	@Override
	public String toString() {
		return "CdBuildingCostId [cityCode=" + cityCode + ", material=" + material + ", floorLowerLimit=" + floorLowerLimit + ", versionDate=" + versionDate + "]";
	}
}
