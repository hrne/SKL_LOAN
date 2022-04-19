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
 * CustCross 客戶交互運用檔<br>
 * Gen By Tool
 * 
 * @author AdamPan
 * @version 1.0.0
 */
@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "`CustCross`")
public class CustCross implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3379358666292685577L;

	@EmbeddedId
	private CustCrossId custCrossId;

	// 客戶識別碼
	@Column(name = "`CustUKey`", length = 32, insertable = false, updatable = false)
	private String custUKey;

	// 子公司代碼
	/* 共用代碼檔01: 新光金控02: 新光人壽03: 新光銀行04: 新光信託05: 保險經紀人06: 元富證券 */
	@Column(name = "`SubCompanyCode`", length = 2, insertable = false, updatable = false)
	private String subCompanyCode;

	// 交互運用
	/* Y: 同意使用N: 不同意使用 */
	@Column(name = "`CrossUse`", length = 1)
	private String crossUse;

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

	public CustCrossId getCustCrossId() {
		return this.custCrossId;
	}

	public void setCustCrossId(CustCrossId custCrossId) {
		this.custCrossId = custCrossId;
	}

	/**
	 * 客戶識別碼<br>
	 * 
	 * @return String
	 */
	public String getCustUKey() {
		return this.custUKey == null ? "" : this.custUKey;
	}

	/**
	 * 客戶識別碼<br>
	 * 
	 *
	 * @param custUKey 客戶識別碼
	 */
	public void setCustUKey(String custUKey) {
		this.custUKey = custUKey;
	}

	/**
	 * 子公司代碼<br>
	 * 共用代碼檔 01: 新光金控 02: 新光人壽 03: 新光銀行 04: 新光信託 05: 保險經紀人 06: 元富證券
	 * 
	 * @return String
	 */
	public String getSubCompanyCode() {
		return this.subCompanyCode == null ? "" : this.subCompanyCode;
	}

	/**
	 * 子公司代碼<br>
	 * 共用代碼檔 01: 新光金控 02: 新光人壽 03: 新光銀行 04: 新光信託 05: 保險經紀人 06: 元富證券
	 *
	 * @param subCompanyCode 子公司代碼
	 */
	public void setSubCompanyCode(String subCompanyCode) {
		this.subCompanyCode = subCompanyCode;
	}

	/**
	 * 交互運用<br>
	 * Y: 同意使用 N: 不同意使用
	 * 
	 * @return String
	 */
	public String getCrossUse() {
		return this.crossUse == null ? "" : this.crossUse;
	}

	/**
	 * 交互運用<br>
	 * Y: 同意使用 N: 不同意使用
	 *
	 * @param crossUse 交互運用
	 */
	public void setCrossUse(String crossUse) {
		this.crossUse = crossUse;
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
		return "CustCross [custCrossId=" + custCrossId + ", crossUse=" + crossUse + ", createDate=" + createDate + ", createEmpNo=" + createEmpNo + ", lastUpdate=" + lastUpdate + ", lastUpdateEmpNo="
				+ lastUpdateEmpNo + "]";
	}
}
