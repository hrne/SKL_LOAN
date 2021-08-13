package com.st1.itx.db.domain;

import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.EntityListeners;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import javax.persistence.Id;
import javax.persistence.Column;

/**
 * CdLandOffice 地政事務所轄區代碼檔<br>
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "`CdLandOffice`")
public class CdLandOffice implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6480627899554143872L;

// 所區碼
	@Id
	@Column(name = "`LandOfficeCode`", length = 4)
	private String landOfficeCode = " ";

	// 事務所名稱
	@Column(name = "`LandOfficeItem`", length = 8)
	private String landOfficeItem;

	// 縣市名稱
	@Column(name = "`City`", length = 6)
	private String city;

	// 行政區名稱
	@Column(name = "`Town`", length = 8)
	private String town;

	// 地區別
	/* 地區別與鄉鎮區對照檔CdArea */
	@Column(name = "`CityCode`", length = 2)
	private String cityCode;

	// 鄉鎮區
	/* 地區別與鄉鎮區對照檔CdArea */
	@Column(name = "`AreaCode`", length = 3)
	private String areaCode;

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

	/**
	 * 所區碼<br>
	 * 
	 * @return String
	 */
	public String getLandOfficeCode() {
		return this.landOfficeCode == null ? "" : this.landOfficeCode;
	}

	/**
	 * 所區碼<br>
	 * 
	 *
	 * @param landOfficeCode 所區碼
	 */
	public void setLandOfficeCode(String landOfficeCode) {
		this.landOfficeCode = landOfficeCode;
	}

	/**
	 * 事務所名稱<br>
	 * 
	 * @return String
	 */
	public String getLandOfficeItem() {
		return this.landOfficeItem == null ? "" : this.landOfficeItem;
	}

	/**
	 * 事務所名稱<br>
	 * 
	 *
	 * @param landOfficeItem 事務所名稱
	 */
	public void setLandOfficeItem(String landOfficeItem) {
		this.landOfficeItem = landOfficeItem;
	}

	/**
	 * 縣市名稱<br>
	 * 
	 * @return String
	 */
	public String getCity() {
		return this.city == null ? "" : this.city;
	}

	/**
	 * 縣市名稱<br>
	 * 
	 *
	 * @param city 縣市名稱
	 */
	public void setCity(String city) {
		this.city = city;
	}

	/**
	 * 行政區名稱<br>
	 * 
	 * @return String
	 */
	public String getTown() {
		return this.town == null ? "" : this.town;
	}

	/**
	 * 行政區名稱<br>
	 * 
	 *
	 * @param town 行政區名稱
	 */
	public void setTown(String town) {
		this.town = town;
	}

	/**
	 * 地區別<br>
	 * 地區別與鄉鎮區對照檔CdArea
	 * 
	 * @return String
	 */
	public String getCityCode() {
		return this.cityCode == null ? "" : this.cityCode;
	}

	/**
	 * 地區別<br>
	 * 地區別與鄉鎮區對照檔CdArea
	 *
	 * @param cityCode 地區別
	 */
	public void setCityCode(String cityCode) {
		this.cityCode = cityCode;
	}

	/**
	 * 鄉鎮區<br>
	 * 地區別與鄉鎮區對照檔CdArea
	 * 
	 * @return String
	 */
	public String getAreaCode() {
		return this.areaCode == null ? "" : this.areaCode;
	}

	/**
	 * 鄉鎮區<br>
	 * 地區別與鄉鎮區對照檔CdArea
	 *
	 * @param areaCode 鄉鎮區
	 */
	public void setAreaCode(String areaCode) {
		this.areaCode = areaCode;
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
		return "CdLandOffice [landOfficeCode=" + landOfficeCode + ", landOfficeItem=" + landOfficeItem + ", city=" + city + ", town=" + town + ", cityCode=" + cityCode + ", areaCode=" + areaCode
				+ ", createDate=" + createDate + ", createEmpNo=" + createEmpNo + ", lastUpdate=" + lastUpdate + ", lastUpdateEmpNo=" + lastUpdateEmpNo + "]";
	}
}
