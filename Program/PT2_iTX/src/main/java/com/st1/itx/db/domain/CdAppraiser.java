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
 * CdAppraiser 估價人員檔<br>
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "`CdAppraiser`")
public class CdAppraiser implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7158734746143578311L;

// 估價人員代號
	@Id
	@Column(name = "`AppraiserCode`", length = 6)
	private String appraiserCode = " ";

	// 估價人員姓名
	@Column(name = "`AppraiserItem`", length = 100)
	private String appraiserItem;

	// 公司名稱
	@Column(name = "`Company`", length = 100)
	private String company;

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
	 * 估價人員代號<br>
	 * 
	 * @return String
	 */
	public String getAppraiserCode() {
		return this.appraiserCode == null ? "" : this.appraiserCode;
	}

	/**
	 * 估價人員代號<br>
	 * 
	 *
	 * @param appraiserCode 估價人員代號
	 */
	public void setAppraiserCode(String appraiserCode) {
		this.appraiserCode = appraiserCode;
	}

	/**
	 * 估價人員姓名<br>
	 * 
	 * @return String
	 */
	public String getAppraiserItem() {
		return this.appraiserItem == null ? "" : this.appraiserItem;
	}

	/**
	 * 估價人員姓名<br>
	 * 
	 *
	 * @param appraiserItem 估價人員姓名
	 */
	public void setAppraiserItem(String appraiserItem) {
		this.appraiserItem = appraiserItem;
	}

	/**
	 * 公司名稱<br>
	 * 
	 * @return String
	 */
	public String getCompany() {
		return this.company == null ? "" : this.company;
	}

	/**
	 * 公司名稱<br>
	 * 
	 *
	 * @param company 公司名稱
	 */
	public void setCompany(String company) {
		this.company = company;
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
		return "CdAppraiser [appraiserCode=" + appraiserCode + ", appraiserItem=" + appraiserItem + ", company=" + company + ", createDate=" + createDate + ", createEmpNo=" + createEmpNo
				+ ", lastUpdate=" + lastUpdate + ", lastUpdateEmpNo=" + lastUpdateEmpNo + "]";
	}
}
