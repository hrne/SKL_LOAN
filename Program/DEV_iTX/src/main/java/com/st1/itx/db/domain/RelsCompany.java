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
 * RelsCompany (準)利害關係人相關事業檔<br>
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "`RelsCompany`")
public class RelsCompany implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7441293480729021532L;

	@EmbeddedId
	private RelsCompanyId relsCompanyId;

	// (準)利害關係人識別碼
	@Column(name = "`RelsUKey`", length = 32, insertable = false, updatable = false)
	private String relsUKey;

	// 相關事業統一編號
	@Column(name = "`CompanyId`", length = 10, insertable = false, updatable = false)
	private String companyId;

	// 公司名稱
	@Column(name = "`CompanyName`", length = 100)
	private String companyName;

	// 持股比率
	@Column(name = "`HoldingRatio`")
	private BigDecimal holdingRatio = new BigDecimal("0");

	// 擔任職務
	@Column(name = "`JobTitle`")
	private int jobTitle = 0;

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

	public RelsCompanyId getRelsCompanyId() {
		return this.relsCompanyId;
	}

	public void setRelsCompanyId(RelsCompanyId relsCompanyId) {
		this.relsCompanyId = relsCompanyId;
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

	/**
	 * 公司名稱<br>
	 * 
	 * @return String
	 */
	public String getCompanyName() {
		return this.companyName == null ? "" : this.companyName;
	}

	/**
	 * 公司名稱<br>
	 * 
	 *
	 * @param companyName 公司名稱
	 */
	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}

	/**
	 * 持股比率<br>
	 * 
	 * @return BigDecimal
	 */
	public BigDecimal getHoldingRatio() {
		return this.holdingRatio;
	}

	/**
	 * 持股比率<br>
	 * 
	 *
	 * @param holdingRatio 持股比率
	 */
	public void setHoldingRatio(BigDecimal holdingRatio) {
		this.holdingRatio = holdingRatio;
	}

	/**
	 * 擔任職務<br>
	 * 
	 * @return Integer
	 */
	public int getJobTitle() {
		return this.jobTitle;
	}

	/**
	 * 擔任職務<br>
	 * 
	 *
	 * @param jobTitle 擔任職務
	 */
	public void setJobTitle(int jobTitle) {
		this.jobTitle = jobTitle;
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
		return "RelsCompany [relsCompanyId=" + relsCompanyId + ", companyName=" + companyName + ", holdingRatio=" + holdingRatio + ", jobTitle=" + jobTitle + ", createDate=" + createDate
				+ ", createEmpNo=" + createEmpNo + ", lastUpdate=" + lastUpdate + ", lastUpdateEmpNo=" + lastUpdateEmpNo + "]";
	}
}
