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
 * CdIndustry 行業別代號檔<br>
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "`CdIndustry`")
public class CdIndustry implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 9035899544096202655L;

// 行業代號
	/* 位數不足6碼者，前補零 */
	@Id
	@Column(name = "`IndustryCode`", length = 6)
	private String industryCode = " ";

	// 行業說明
	@Column(name = "`IndustryItem`", length = 50)
	private String industryItem;

	// 主計處大類
	@Column(name = "`MainType`", length = 1)
	private String mainType;

	// 企金放款產業評等
	/* 報表使用LM048企業放款風險承擔限額控管表值為NULL時不進表 */
	@Column(name = "`IndustryRating`", length = 1)
	private String industryRating;

	// 啟用記號
	/* Y:啟用 , N:未啟用 */
	@Column(name = "`Enable`", length = 1)
	private String enable;

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
	 * 行業代號<br>
	 * 位數不足6碼者，前補零
	 * 
	 * @return String
	 */
	public String getIndustryCode() {
		return this.industryCode == null ? "" : this.industryCode;
	}

	/**
	 * 行業代號<br>
	 * 位數不足6碼者，前補零
	 *
	 * @param industryCode 行業代號
	 */
	public void setIndustryCode(String industryCode) {
		this.industryCode = industryCode;
	}

	/**
	 * 行業說明<br>
	 * 
	 * @return String
	 */
	public String getIndustryItem() {
		return this.industryItem == null ? "" : this.industryItem;
	}

	/**
	 * 行業說明<br>
	 * 
	 *
	 * @param industryItem 行業說明
	 */
	public void setIndustryItem(String industryItem) {
		this.industryItem = industryItem;
	}

	/**
	 * 主計處大類<br>
	 * 
	 * @return String
	 */
	public String getMainType() {
		return this.mainType == null ? "" : this.mainType;
	}

	/**
	 * 主計處大類<br>
	 * 
	 *
	 * @param mainType 主計處大類
	 */
	public void setMainType(String mainType) {
		this.mainType = mainType;
	}

	/**
	 * 企金放款產業評等<br>
	 * 報表使用 LM048企業放款風險承擔限額控管表 值為NULL時不進表
	 * 
	 * @return String
	 */
	public String getIndustryRating() {
		return this.industryRating == null ? "" : this.industryRating;
	}

	/**
	 * 企金放款產業評等<br>
	 * 報表使用 LM048企業放款風險承擔限額控管表 值為NULL時不進表
	 *
	 * @param industryRating 企金放款產業評等
	 */
	public void setIndustryRating(String industryRating) {
		this.industryRating = industryRating;
	}

	/**
	 * 啟用記號<br>
	 * Y:啟用 , N:未啟用
	 * 
	 * @return String
	 */
	public String getEnable() {
		return this.enable == null ? "" : this.enable;
	}

	/**
	 * 啟用記號<br>
	 * Y:啟用 , N:未啟用
	 *
	 * @param enable 啟用記號
	 */
	public void setEnable(String enable) {
		this.enable = enable;
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
		return "CdIndustry [industryCode=" + industryCode + ", industryItem=" + industryItem + ", mainType=" + mainType + ", industryRating=" + industryRating + ", enable=" + enable + ", createDate="
				+ createDate + ", createEmpNo=" + createEmpNo + ", lastUpdate=" + lastUpdate + ", lastUpdateEmpNo=" + lastUpdateEmpNo + "]";
	}
}
