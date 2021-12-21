package com.st1.itx.db.domain;

import java.io.Serializable;
import java.math.BigDecimal;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.EntityListeners;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import javax.persistence.EmbeddedId;
import javax.persistence.Column;

/**
 * CdBuildingCost 建築造價參考檔<br>
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "`CdBuildingCost`")
public class CdBuildingCost implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4332862392657527239L;

	@EmbeddedId
	private CdBuildingCostId cdBuildingCostId;

	// 縣市代碼(地區別)
	/* ref CdCity.CityCode */
	@Column(name = "`CityCode`", length = 2, insertable = false, updatable = false)
	private String cityCode;

	// 總樓層數(下限)
	@Column(name = "`FloorLowerLimit`", insertable = false, updatable = false)
	private int floorLowerLimit = 0;

	// 建築造價
	/* 單位:新臺幣元/坪 */
	@Column(name = "`Cost`")
	private BigDecimal cost = new BigDecimal("0");

	// 建檔日期時間
	@CreatedDate
	@Column(name = "`CreateDate`")
	private java.sql.Timestamp createDate;

	// 建檔人員
	@Column(name = "`CreateEmpNo`", length = 6)
	private String createEmpNo;

	// 最後更新日期時間
	@LastModifiedDate
	@Column(name = "`LastUpdate`")
	private java.sql.Timestamp lastUpdate;

	// 最後更新人員
	@Column(name = "`LastUpdateEmpNo`", length = 6)
	private String lastUpdateEmpNo;

	public CdBuildingCostId getCdBuildingCostId() {
		return this.cdBuildingCostId;
	}

	public void setCdBuildingCostId(CdBuildingCostId cdBuildingCostId) {
		this.cdBuildingCostId = cdBuildingCostId;
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

	/**
	 * 建築造價<br>
	 * 單位:新臺幣元/坪
	 * 
	 * @return BigDecimal
	 */
	public BigDecimal getCost() {
		return this.cost;
	}

	/**
	 * 建築造價<br>
	 * 單位:新臺幣元/坪
	 *
	 * @param cost 建築造價
	 */
	public void setCost(BigDecimal cost) {
		this.cost = cost;
	}

	/**
	 * 建檔日期時間<br>
	 * 
	 * @return java.sql.Timestamp
	 */
	public java.sql.Timestamp getCreateDate() {
		return this.createDate;
	}

	/**
	 * 建檔日期時間<br>
	 * 
	 *
	 * @param createDate 建檔日期時間
	 */
	public void setCreateDate(java.sql.Timestamp createDate) {
		this.createDate = createDate;
	}

	/**
	 * 建檔人員<br>
	 * 
	 * @return String
	 */
	public String getCreateEmpNo() {
		return this.createEmpNo == null ? "" : this.createEmpNo;
	}

	/**
	 * 建檔人員<br>
	 * 
	 *
	 * @param createEmpNo 建檔人員
	 */
	public void setCreateEmpNo(String createEmpNo) {
		this.createEmpNo = createEmpNo;
	}

	/**
	 * 最後更新日期時間<br>
	 * 
	 * @return java.sql.Timestamp
	 */
	public java.sql.Timestamp getLastUpdate() {
		return this.lastUpdate;
	}

	/**
	 * 最後更新日期時間<br>
	 * 
	 *
	 * @param lastUpdate 最後更新日期時間
	 */
	public void setLastUpdate(java.sql.Timestamp lastUpdate) {
		this.lastUpdate = lastUpdate;
	}

	/**
	 * 最後更新人員<br>
	 * 
	 * @return String
	 */
	public String getLastUpdateEmpNo() {
		return this.lastUpdateEmpNo == null ? "" : this.lastUpdateEmpNo;
	}

	/**
	 * 最後更新人員<br>
	 * 
	 *
	 * @param lastUpdateEmpNo 最後更新人員
	 */
	public void setLastUpdateEmpNo(String lastUpdateEmpNo) {
		this.lastUpdateEmpNo = lastUpdateEmpNo;
	}

	@Override
	public String toString() {
		return "CdBuildingCost [cdBuildingCostId=" + cdBuildingCostId + ", cost=" + cost + ", createDate=" + createDate + ", createEmpNo=" + createEmpNo + ", lastUpdate=" + lastUpdate
				+ ", lastUpdateEmpNo=" + lastUpdateEmpNo + "]";
	}
}
