package com.st1.itx.db.domain;

import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.EntityListeners;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import javax.persistence.EmbeddedId;
import javax.persistence.Column;

/**
 * CdLandSection 地段代碼檔<br>
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "`CdLandSection`")
public class CdLandSection implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5597771632081714898L;

	@EmbeddedId
	private CdLandSectionId cdLandSectionId;

	// 地區別
	/* CdCity.CityCode */
	@Column(name = "`CityCode`", length = 2, insertable = false, updatable = false)
	private String cityCode;

	// 鄉鎮區
	/* CdArea.AreaCode */
	@Column(name = "`AreaCode`", length = 2, insertable = false, updatable = false)
	private String areaCode;

	// 段小段代碼
	@Column(name = "`IrCode`", length = 5, insertable = false, updatable = false)
	private String irCode;

	// 段小段名稱
	@Column(name = "`IrItem`", length = 30)
	private String irItem;

	// 地所代碼
	/* 共用代碼檔 */
	@Column(name = "`LandOfficeCode`", length = 2)
	private String landOfficeCode;

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

	public CdLandSectionId getCdLandSectionId() {
		return this.cdLandSectionId;
	}

	public void setCdLandSectionId(CdLandSectionId cdLandSectionId) {
		this.cdLandSectionId = cdLandSectionId;
	}

	/**
	 * 地區別<br>
	 * CdCity.CityCode
	 * 
	 * @return String
	 */
	public String getCityCode() {
		return this.cityCode == null ? "" : this.cityCode;
	}

	/**
	 * 地區別<br>
	 * CdCity.CityCode
	 *
	 * @param cityCode 地區別
	 */
	public void setCityCode(String cityCode) {
		this.cityCode = cityCode;
	}

	/**
	 * 鄉鎮區<br>
	 * CdArea.AreaCode
	 * 
	 * @return String
	 */
	public String getAreaCode() {
		return this.areaCode == null ? "" : this.areaCode;
	}

	/**
	 * 鄉鎮區<br>
	 * CdArea.AreaCode
	 *
	 * @param areaCode 鄉鎮區
	 */
	public void setAreaCode(String areaCode) {
		this.areaCode = areaCode;
	}

	/**
	 * 段小段代碼<br>
	 * 
	 * @return String
	 */
	public String getIrCode() {
		return this.irCode == null ? "" : this.irCode;
	}

	/**
	 * 段小段代碼<br>
	 * 
	 *
	 * @param irCode 段小段代碼
	 */
	public void setIrCode(String irCode) {
		this.irCode = irCode;
	}

	/**
	 * 段小段名稱<br>
	 * 
	 * @return String
	 */
	public String getIrItem() {
		return this.irItem == null ? "" : this.irItem;
	}

	/**
	 * 段小段名稱<br>
	 * 
	 *
	 * @param irItem 段小段名稱
	 */
	public void setIrItem(String irItem) {
		this.irItem = irItem;
	}

	/**
	 * 地所代碼<br>
	 * 共用代碼檔
	 * 
	 * @return String
	 */
	public String getLandOfficeCode() {
		return this.landOfficeCode == null ? "" : this.landOfficeCode;
	}

	/**
	 * 地所代碼<br>
	 * 共用代碼檔
	 *
	 * @param landOfficeCode 地所代碼
	 */
	public void setLandOfficeCode(String landOfficeCode) {
		this.landOfficeCode = landOfficeCode;
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
		return "CdLandSection [cdLandSectionId=" + cdLandSectionId + ", irItem=" + irItem + ", landOfficeCode=" + landOfficeCode + ", createDate=" + createDate + ", createEmpNo=" + createEmpNo
				+ ", lastUpdate=" + lastUpdate + ", lastUpdateEmpNo=" + lastUpdateEmpNo + "]";
	}
}
